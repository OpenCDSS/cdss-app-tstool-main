# Python script to extract time series data from an Excel worksheet
# This only works for annual data
"""
Extract annual time series from an Excel spreadsheet (.xls, not .xlsx)
Usage:

python extract-ts.py DataMapFile.py OutputFile.csv

The worksheet specified by the "WorkSheet" variable below must have
named cells defined for the cells above the data rows.
The DataMapFile.py is included in this script at runtime and can contain
the following definitions:

Required/
Optional  Variable                   Description

Required  Workbook                   Path to the xls workbook file
Required  Worksheet                  Name of the worksheet (tab) to process.
Optional  OutputComment              If specified, this note will be added to the comments in the
                                     output file.

Optional  TimeDirection              'Horizontal' for cases where years progress to the right
                                     'Vertical' is not supported
Required  DataColumnNames            A list of named cells for the columns that contain data values.
Required  DateTimes                  A list of years corresponding to data columns.

Req/Opt   LocColumnName              The named cell for the column that contains location identifiers.
                                     If not specified, LocList is required.
Req/Opt   LocList                    The list of locations to use, when a column of location
                                     identifiers is not available.
                                     If not specified, LocColumnName is required.
Optional  IfLocBlank                 Action if the location column is blank (when used with LocColumnName).
                                     If 'Exit', stop processing data.
                                     If 'Skip', skip the line during processing - exit when at end of worksheet.

Optional  LocTranslatorAfterLookup   The name of a function to call to translate location identifiers after
                                     looking up in the lookup table.
Optional  LocTranslatorBeforeLookup  The name of a function to call to translate location identifiers before
                                     looking up in the lookup table.
Optional  LocLookupWorkbook          The workbook file name, used to translate location identifiers found in
                                     the data sheet to other values (for example to convert a verbose identifier
                                     to a shorter identifier suitable for data processing).
Optional  LocLookupWorksheet         The worksheetused to translate location identifiers.
Optional  LocLookupColumnName        The named cell for the column of the origional location ID to match.
Optional  LocLookupColumnName2       The named cell for the column of the new location ID to use.
"""

# Standard modules...
import logging
import os
import sys
import time
import traceback

# Only need this if using .NET directly
# .NET modules
#import clr
#clr.AddReference("Microsoft.Office.Interop.Excel")
#from Microsoft.Office.Interop import Excel

# Excel utilities
import exceldbutil
# Logging utilities
import loggingutil

# 3rd party modules (put after BNDSS settings because settings add to
# the system path for finding the modules)
import xlrd
assert xlrd.__VERSION__ == "0.7.1"

# Log file name is global in module because it is used to print a message
# at the end of this script
LOGFILE = os.path.basename(sys.argv[0]).replace(".py",".log")

def main():
    """
    Main function.  Does all the work and calls other functions as needed.
    """
    logger = logging.getLogger()
    # Read the named cells information which include information
    # about cells to transfer
    if ( len(sys.argv) < 2 ):
        printUsage()
        return
    mapFile = sys.argv[1] 
    if ( not os.path.exists(mapFile) ):
        message = "Data sheet map file does not exist: \"" + sys.argv[1] + "\""
        print ( message )
        logger.error ( message )
        printUsage()
        return
    outputFile = None
    if ( len(sys.argv) > 2 ):
        # Have an output file
        outputFile = sys.argv[2]
    # Execute the map file, which explains how to map named cells to time series
    # A dictionary instance is used to store the results so that it does not interfere
    # with the function location dictionary or globals
    logger.info ( "Importing data map Python module file \"" + mapFile + "\"" )
    # Use a local dictionary to store the data map values initilized from the file
    dataMap = {}
    execfile(mapFile,dataMap,dataMap)
    xlsFile = dataMap["Workbook"]
    sheetName = dataMap["Worksheet"]
    # What to do if the location is blank ("Exit" or "Skip")
    ifLocBlank = "Exit"
    try:
        ifLocBlank = dataMap["IfLocBlank"]
    except KeyError:
        # Default
        ifLocBlank = "Exit"
    # Column name for the time series location
    # If not specified expect LocList to be specified
    locColumnName = None
    try:
        locColumnName = dataMap["LocColumnName"]
    except KeyError:
        # Default
        locColumnName = None
    locList = None
    try:
        locList = dataMap["LocList"]
    except KeyError:
        # Default
        locList = None
    # Make sure that LocColumnName or LocList are specified
    if ( (locColumnName == None) and (locList == None) ):
        print "LocColumnName or LocList must be specified"
        exit(1)
    if ( (locColumnName != None) and (locList != None) ):
        print "LocColumnName or LocList must be specified"
        exit(1)
    # Output comment
    outputComment = ""
    try:
        outputComment = dataMap["OutputComment"]
    except KeyError:
        # Default
        outputComment = ""
    # Data column names
    dataColumnNames = dataMap["DataColumnNames"]
    # Read the data file that has the lookup information
    locLookupDict = None
    try:
        locLookupWorkbook = dataMap["LocLookupWorkbook"]
        locLookupWorksheet = dataMap["LocLookupWorksheet"]
        locLookupColumnName = dataMap["LocLookupColumnName"]
        locLookupColumnName2 = dataMap["LocLookupColumnName2"]
        locLookupDict = readLocLookupDictionary ( locLookupWorkbook, locLookupWorksheet,
            locLookupColumnName, locLookupColumnName2 )
        if ( len(locLookupDict) == 0 ):
            # No dictionary was read
            locLookupDict = None
    except KeyError:
        # Lookup is not used
        logger.info("Location lookup table is being not used to translate location names.")
        locLookupDict = None
    # Read the time series from the XLS file...
    # This uses xlrd...
    xlsFileAbs = exceldbutil.absolutePath(xlsFile)
    logger.info ( "Opening workbook \"" + xlsFileAbs + "\"." )
    book = xlrd.open_workbook ( xlsFileAbs )
    sheet = book.sheet_by_name(sheetName)
    logger.info ( "Processing worksheet \"" + sheet.name + "\" nrows=" + str(sheet.nrows) + " ncols=" + str(sheet.ncols) )
    # Named cells are for the entire workbook
    namedCells = exceldbutil.readNamedCells ( book, sheet )
    # Get the row for the header named cell (data will be in following row)
    logger.info ( "Time series location column is in named column \"" + str(locColumnName) + "\"" )
    logger.info ( "Time series first data column is in named column \"" + dataColumnNames[0] + "\"" )
    if ( locColumnName != None ):
        # Use the locColumnName to find the header row
        locXlrdName = lookupNamedCellInList ( locColumnName, namedCells )
    else:
        # Use the dataColumnNames[0] to find the header row
        locXlrdName = lookupNamedCellInList ( dataColumnNames[0], namedCells )
    xsheet, rowxlo, rowxhi, colxlo, colxhi = locXlrdName.area2d()
    # Note that row is 0-index based
    headerRow = rowxlo
    logger.info ( "Column header names are in Excel row " + str(headerRow + 1) +
        ".  Data start in Excel row " + str(headerRow + 2) + "." )
    # Determine the columns for the location and data
    locColumnNumber = colxlo
    dataColumnNumbers = []
    # Get the date/times corresponding to the data
    dateTimes = dataMap["DateTimes"]
    if ( len(dataColumnNames) != len(dateTimes) ):
        message = "The number of items in the data map for \"DataColumnNames\" (" + \
            str(len(dataColumnNames)) + ") is not the same as \"DateTimes\" (" + str(len(dateTimes)) + ")."
        loggingutil.warning(logger,message)
        raise RuntimeError(message)
    # Look up the Excel columns for the requested values
    dataColumnCount = 0
    for dataColumnName in dataColumnNames:
    	locXlrdName = lookupNamedCellInList ( dataColumnName, namedCells )
        if ( locXlrdName == None ):
            loggingutil.warning(logger,"Requested data column name \"" + dataColumnName +
                "\" was not found in data sheet - check defined names." )
        else:
            xsheet, rowxlo, rowxhi, colxlo, colxhi = locXlrdName.area2d()
            logger.info("Requested data column name \"" + dataColumnName +
                "\" for date/time " + str(dateTimes[dataColumnCount]) + " is in Excel column " +
                excelColumnStringAddressFromNumber ( colxlo ) )
	    dataColumnNumbers.append(colxlo)
        dataColumnCount = dataColumnCount + 1
    if ( len(dataColumnNumbers) != len(dataColumnNames) ):
        message = "One or more of the column names in the data map \"DataColumnNames\"" + \
            " is not defined as a named cell in the worksheet.  Fix and rerun."
        loggingutil.warning(logger,message)
        raise RuntimeError(message)
    # Allocate an array with rows corresponding to date/time and columns to the locations/time series
    locIDList = [] # List of locations with time series, using time series ID string
    if ( locList != None ):
        # User specified with data map (don't set values below)
        locIDList = locList
    numRows = len(dateTimes)
    if ( ifLocBlank == "Exit" ):
        # Sheet is configured so that first blank is exit of data
        numCols = exceldbutil.countNonBlankCellsInColumn ( sheet, (headerRow + 1),
            (sheet.nrows - 1), locColumnNumber, True )
    else:
        # Sheet is expected to have blank lines
        numCols = exceldbutil.countNonBlankCellsInColumn ( sheet, (headerRow + 1),
            (sheet.nrows - 1), locColumnNumber, False )
    cellValues = [ [None for icol in range(numCols)] for irow in range(numRows) ]
    logger.info("Created data array with " + str(numRows) + " rows for date/times and " +
        str(numCols) + " columns for locations (time series).")
    # Start reading first data row and process each row
    # Data will be the rows after that.
    # "row" is 0-index whereas logging messages are Excel rows 1+
    row = headerRow
    logger.info("Start reading data rows in Excel row " + str(row + 2) + " headerRow=" + str(headerRow + 1))
    tsIndex = 0 # needed to handle blank data rows - match non-blank data to data array time series
    while True:
        # Increment the row to read - for first row it will be the row after the headerRow
        row = row + 1
        # Output for user
        if ( (((row + 1) % 25) == 0) or (row == (headerRow + 1)) or (row == (sheet.nrows - 1)) ):
            # Print a message so user can see progress
            print "Processing Excel row " + str(row + 1)
        # For troubleshooting...
        #print "Processing Excel row " + str(row + 1)
        # Read the data from the sheet.  To avoid extra checks, if the location column value is
        # blank, exit or skip
        # The first check works because row is zero index
        # row is zero index and sheet.nrows is row count so have to subtract 1
        if ( row == (sheet.nrows - 1) ):
            print ( "End processing at Excel row " + str(row + 1) + " since last row found.")
            logger.info ( "Processed " + str(row) + " rows of data - ending processing since last row found.")
            break
        elif ( sheet.cell_value(row,locColumnNumber) == '' ):
            # The location cell is blank so check if it should be ignored or should exit
            if ( ifLocBlank == "Exit" ):
                # Print and log messages since important to user to know why ending
                print ( "End processing at Excel row " + str(row + 1) + " since blank location or last row found.")
                logger.info ( "Processed " + str(row) + " rows of data - ending processing since blank row found.")
                break
            elif ( ifLocBlank == "Skip" ):
                # Blank location may be common depending on sheet formatting so need for message
                continue
        # Read the location for the row
	locCell = sheet.cell_value(row,locColumnNumber)
        if ( locList == None ):
            locIDList.append ( str(locCell) )
	# Loop through the years in the data row...
        dataColumnCount = -1
        for dataColumnNumber in dataColumnNumbers:
            dataColumnCount = dataColumnCount + 1 # Will be 0+
            if ( dataColumnNumber == None ):
                cellValue = None
            else:
	        cellValue = sheet.cell_value(row,dataColumnNumber)
            logger.info("Excel row " + str(row + 1) + " location \"" + str(locCell) + "\" for " +
                str(dateTimes[dataColumnCount]) + " = " + str(cellValue) )
            cellType = sheet.cell_type(row,dataColumnNumber)
            # Even when a field should be a float it is sometimes represented
            # in Excel as an integer, so enforce the type here.
            try:
                cellValue0 = cellValue
                cellValue = exceldbutil.enforceType ( cellValue, cellType, float )
                logger.info("Original cell value=\"" + str(cellValue0) + "\" cellValue=\"" + str(cellValue) + "\"" )
                if ( (cellValue0 != None) and (cellValue == None) ):
                    # The value was set to none.  This is a data error
                    loggingutil.warning ( logger,"Cell value at Excel row " + str(row + 1) +
                        " and Excel column " + str(excelColumnStringAddressFromNumber (dataColumnNumber)) +
                        " is invalid - verify that the value is a number - setting to None." )
            except:
                cellValue = None
            # Now set the data array.  Since the original date/times are listed horizontally but
            # the data array is with date/time vertical, reverse the indices
            logger.info("Setting [" + str(dataColumnCount) + "][" + str(tsIndex) + "] = " + str(cellValue) )
            cellValues[dataColumnCount][tsIndex] = cellValue
        # If here, then a data row was processed successfully
        tsIndex = tsIndex + 1
    # Write the values out with date/times in column 1 and each time series in a separate column
    # The time series column names match the location identifiers with no data types or units
    # Use standard output if no output file has been specified
    logger.info("Read " + str(len(locIDList)) + " locations from file for period " +
        dateTimes[0] + " to " + dateTimes[len(dateTimes) - 1] )
    out = sys.stdout
    if ( outputFile != None ):
        out = open(outputFile,"w")
    # Write the standard header
    writeOutputHeader ( out, mapFile, xlsFile, sheetName, outputComment )
    out.write("\"Date/time\"")
    locTranslatorBeforeLookup = None
    try:
        locTranslatorBeforeLookup = dataMap["LocTranslatorBeforeLookup"]
    except KeyError:
        # Translator is not used
        locTranslatorBeforeLookup = None
    locTranslatorAfterLookup = None
    try:
        locTranslatorAfterLookup = dataMap["LocTranslatorAfterLookup"]
    except KeyError:
        # Translator is not used
        locTranslatorAfterLookup = None
    for loc in locIDList:
        # Single quotes are replaced by triple quotes in output if they still exist
        # (but hopefully the translation/lookup results in a "nice" location string)
        #
        # First run the translator on input if available
        if ( locTranslatorBeforeLookup != None ):
            loc = locTranslatorBeforeLookup(loc)
        # Always strip whitespace
        loc = loc.strip()
        # Use the lookup table to get the more appropriate location
        if ( locLookupDict != None ):
            try:
                loc = lookupAlternateLocation ( locLookupDict, loc )
            except KeyError:
                logger.warning("Location \"" + loc + "\" could not be looked up.  Using original.")
        if ( locTranslatorAfterLookup != None ):
            loc = locTranslatorAfterLookup(loc)
        # Make sure that quotes are handled appropriately before final output
        loc = loc.replace(".","").replace('"','"""' )
        out.write(",\"" + loc + "\"")
    out.write("\n")
    # Now write the data rows
    for dateTimePos in range(len(dateTimes)):
        out.write(str(dateTimes[dateTimePos]))
        for locPos in range(len(locIDList)):
            if ( cellValues[dateTimePos][locPos] == None ):
                out.write(",")
            else:
                out.write("," + str(cellValues[dateTimePos][locPos]) )
        out.write("\n")
    if ( outputFile != None ):
        out.close()
    return

def excelColumnStringAddressFromNumber ( idx ):
    """
    Convert an Excel column 0+ to string form "A", etc.
    The following was converted from:
    http://stackoverflow.com/questions/181596/how-to-convert-a-column-number-eg-127-into-an-excel-column-eg-aa
    idx - Excel column 0+
    """
    # Original code expects columns 1+ so add one here
    idx = idx + 1
    if idx < 1:
        raise ValueError("Index is too small")
    result = ""
    while True:
        if idx > 26:
            idx, r = divmod(idx - 1, 26)
            result = chr(r + ord('A')) + result
        else:
            return chr(idx + ord('A') - 1) + result

def lookupAlternateLocation ( locLookupDict, loc ):
    """
    Lookup the alternate location identifier in the dictionary.
    """
    return locLookupDict[loc]

def readLocLookupDictionary ( locLookupWorkbook, locLookupWorksheet,
    locLookupColumnName, locLookupColumnName2 ):
    """
    Read an XLS file that contains location data that can be used to lookup
    alternate location identifiers that that in the time series data.
    This is needed because the raw data identifiers may be difficult to
    deal with in TSTool, have special characters, etc.
    locLookupWorkbook - the XLS file to read
    locLookupWorksheet - the worksheet in the XLS file
    locLookupColumn - the named cell for the column containing the original location
    locLookupColumn2 - the named cell for the column containing the location ID to use in time series
    """
    logger = logging.getLogger()
    dict = {}
    # If the lookup file has not been specified, return an empty dictionary
    if ( locLookupWorkbook == None ):
        return dict
    xlsFileAbs = exceldbutil.absolutePath(locLookupWorkbook)
    logger.info ( "Opening location lookup workbook \"" + xlsFileAbs + "\"." )
    book = xlrd.open_workbook ( xlsFileAbs )
    sheet = book.sheet_by_name(locLookupWorksheet)
    # Named cells are for the entire workbook
    namedCells = exceldbutil.readNamedCells ( book, sheet )
    logger.info ( "Processing location lookup worksheet \"" + sheet.name + "\"." )
    # Named cells are for the entire workbook
    namedCells = exceldbutil.readNamedCells ( book, sheet )
    # Get the row for the location named cell (data will be in following row)
    locXlrdName = lookupNamedCellInList ( locLookupColumnName, namedCells )
    if ( locXlrdName == None ):
        message = "Unable to find named cell \"" + locLookupColumnName + "\" in \"" + locLookupWorkbook + "\""
        logger.warning ( message )
        raise RuntimeError(message)
    xsheet, rowxlo, rowxhi, colxlo, colxhi = locXlrdName.area2d()
    locLookupColumnNameNumber = colxlo
    logger.info ( "Original location column is in named column \"" + locLookupColumnName + "\", Excel column " +
       excelColumnStringAddressFromNumber ( locLookupColumnNameNumber ) )
    # Note that row is 0-index based
    headerRow = rowxlo
    logger.info ( "Column names are in Excel row " + str(headerRow + 1) +
        ".  Data start in Excel row " + str(headerRow + 2) + "." )
    locXlrdName = lookupNamedCellInList ( locLookupColumnName2, namedCells )
    if ( locXlrdName == None ):
        message = "Unable to find named cell \"" + locLookupColumnName2 + "\" in \"" + locLookupWorkbook + "\""
        logger.warning ( message )
        raise RuntimeError(message)
    xsheet, rowxlo, rowxhi, colxlo, colxhi = locXlrdName.area2d()
    locLookupColumnNameNumber2 = colxlo
    logger.info ( "Alternate (looked up) location column is in named column \"" +
       locLookupColumnName2 + "\", Excel column " +
       excelColumnStringAddressFromNumber ( locLookupColumnNameNumber2 ) )
    # Read through the XLS and extract the original and looked up locations
    for irow in range(headerRow + 1,sheet.nrows):
	locCell = sheet.cell_value(irow,locLookupColumnNameNumber)
        if ( (locCell == None) or (locCell.strip() == "") ):
            logger.info("Original location name is blank in Excel row " +
                str(irow + 1) + " - stop reading data" )
            break
        locCell = locCell.strip()
	locCell2 = sheet.cell_value(irow,locLookupColumnNameNumber2)
        if ( (locCell2 == None) or (locCell2.strip() == "") ):
            # Set to the same value
            loggingutil.warning(logger,"Original location name \"" + str(locCell) +
                "\" does not have alternate in Excel row " +
                str(irow + 1) + " - will not be able to translate")
	    dict[locCell] = locCell.strip()
        else:
            # Set to the looked-up value
	    dict[locCell] = locCell2.strip()
        logger.info("Location \"" + locCell + "\" will be translated to \"" + dict[locCell] + "\"")
    return dict

def lookupNamedCellInList ( name, namedCells ):
    """
    Lookup a named cell in a list of named cells, using the cell name.
    Return None if a match is not made.
    name - name to look up
    namedCells - list of XLRD Name objects to look through
    """
    for namedCell in namedCells:
        if ( name.upper() == namedCell.name.upper() ):
             return namedCell
    return None

def printUsage ():
    """
    Print the script usage.
    """
    print ""
    print "Usage:  ipy " + os.path.basename(sys.argv[0]) + " XLSfile loaderMapFile"
    print ""
    return

def writeOutputHeader ( out, mapFile, xlsFile, sheetName, outputComment ):
    """
    Write a standard output file for the CSV file to identify the file contents.
    out - open file
    mapFile - name of map file being used for data mapping
    xlsFile - name of workbook file
    sheetName - name of sheet in the workbook
    outputComment - output comment to add to the top of the file
    """
    if ( outputComment == None ):
        outputComment = ""
    currentTime = time.strftime("%a, %d %b %Y %H:%M:%S",time.localtime())
    out.write ( "# " + outputComment + "\n" )
    out.write ( "#\n" )
    out.write ( "# Produced by export-ts.py Python script\n" )
    out.write ( "# " + currentTime + "\n" )
    out.write ( "# Data map file: \"" + mapFile + "\"\n" )
    out.write ( "# Workbook file: \"" + xlsFile + "\"\n" )
    out.write ( "# Worksheet name: \"" + sheetName + "\"\n" )
    out.write ( "#\n" )
    return

if __name__ == "__main__":
    logging.basicConfig(
        filename=LOGFILE,
        level=logging.INFO,
        format='%(asctime)s %(levelname)-8s %(message)s',
        filemode='w'
    )
    #===============================================================================
    # Entry point into the script, which calls the main function.
    # Put at the end because functions need to be defined before they can be called
    try:
            main ()
            if ( loggingutil.warningCount > 0 ):
                print ("There were " + str(loggingutil.warningCount) +
                    " warnings processing data.  See the log file \"" + LOGFILE + "\".")
    except SystemExit, error:
            # This exception is raised when system.exit() is called.
            # Just let normal exit happen to shut down this script.
            print 'Exiting (' + str(error) + ')'
            # Call the internal function to close out gracefully
            exit(int(str(error)))
    except:
            # Using the logger here assumes that basic logging setup was successful
            # in the main function.
            logger = logging.getLogger()
            logger.exception ( "Unexpected exception" )
            print 'Unexpected error.  See the detail in \"' + LOGFILE + '"'
            traceback.print_exc()
            exit(1)

    # Done - exit with success status (no exception handled because script is done)
    # Probably won't get here because system.exit() gets called once and is
    # therefore handled in the above SystemExect except.
    exit(0)
