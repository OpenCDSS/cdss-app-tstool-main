# Python script to extract time series data from an Excel worksheet

# Standard modules...
import logging
import os
import sys
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
    logger.info ( "Processing worksheet \"" + sheet.name + "\"." )
    # Named cells are for the entire workbook
    namedCells = exceldbutil.readNamedCells ( book, sheet )
    # Get the row for the location named cell (data will be in following row)
    logger.info ( "Time series location column is in named column \"" + dataMap["LocColumnName"] + "\"" )
    locXlrdName = lookupNamedCellInList ( dataMap["LocColumnName"], namedCells )
    xsheet, rowxlo, rowxhi, colxlo, colxhi = locXlrdName.area2d()
    # Note that row is 0-index based
    headerRow = rowxlo
    logger.info ( "Column names are in Excel row " + str(headerRow + 1) +
        ".  Data start in Excel row " + str(headerRow + 2) + "." )
    # Determine the columns for the location and data
    locColumnNumber = colxlo
    dataColumnNumbers = []
    dataColumnNames = dataMap["DataColumnNames"]
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
        message = "One or more of the column names in the data data map \"DataColumnNames\"" + \
            " is not defined as a named cell in the worksheet.  Fix and rerun."
        loggingutil.warning(logger,message)
        raise RuntimeError(message)
    # Allocate an array with rows corresponding to date/time and columns to the locations/time series
    locList = []   # List of locations with time series, using time series ID string
    numRows = len(dateTimes)
    numCols = exceldbutil.countNonBlankCellsInColumn ( sheet, (headerRow + 1),
        sheet.nrows, locColumnNumber, True )
    cellValues = [ [None for icol in range(numCols)] for irow in range(numRows) ]
    logger.info("Created data array with " + str(numRows) + " rows for date/times and " +
        str(numCols) + " columns for locations.")
    # Start reading first data row and process each row until a blank row is found
    # Data will be the non-blank rows after that.
    # "row" is 0-index whereas logging messages are Excel rows 1+
    row = headerRow
    logger.info("Start reading data rows in Excel row " + str(row + 2) + " headerRow=" + str(headerRow + 1))
    while True:
        # Increment the row to read - for first row it will be the row after the headerRow
        row = row + 1
        # Output for user
        if ( (((row + 1) % 25) == 0) or (row == (headerRow + 1)) or (row == (sheet.nrows - 1)) ):
            # Print a message so user can see progress
            print "Processing Excel row " + str(row + 1)
        # Read the data from the sheet.  To avoid extra checks, if the first column value is
        # blank, break
        # The first check works because row is zero index
        if ( (row == sheet.nrows) or (sheet.cell_value(row,locColumnNumber) == '') ):
            # Row is the count prior to this row (since zero indexed)
            print ( "End processing at Excel row " + str(row + 1) + " since blank or last row found.")
            logger.info ( "Processed " + str(row) + " rows of data - ending processing since blank row found.")
            break
        # Read the location for the row
	locCell = sheet.cell_value(row,locColumnNumber)
        locList.append ( str(locCell) )
	# Loop through the years...
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
                cellValue = exceldbutil.enforceType ( cellValue, cellType, "float" )
                if ( (cellValue0 != None) and (cellValue == None) ):
                    # The value was set to none.  This is a data error
                    loggingutil.warning ( logger,"Cell value at Excel row " + str(row + 1) +
                        " and Excel column " + str(excelColumnStringAddressFromNumber (dataColumnNumber)) +
                        " is invalid - verify that the value is a number." )
            except:
                cellValue = None
            # Now set the data array.  Since the original date/times are listed horizontally but
            # the data array is with date/time vertical, reverse the indices
            cellValues[dataColumnCount][row - headerRow - 1] = cellValue
            logger.info("Setting [" + str(dataColumnCount) + "][" + str(row - headerRow - 1) +
                "] = " + str(cellValue) )
    # Write the values out with date/times in column 1 and each time series in a separate column
    # The time series column names match the location identifiers with no data types or units
    # Use standard output if no output file has been specified
    logger.info("Read " + str(len(locList)) + " locations from file for period " +
        dateTimes[0] + " to " + dateTimes[len(dateTimes) - 1] )
    out = sys.stdout
    if ( outputFile != None ):
        out = open(outputFile,"w")
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
    for loc in locList:
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
        for locPos in range(len(locList)):
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
    Read an XLS file that contains provider data that can be used to lookup
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
    logger.info ( "Original location column is in named column \"" + locLookupColumnName + "\"" )
    locXlrdName = lookupNamedCellInList ( locLookupColumnName, namedCells )
    if ( locXlrdName == None ):
        message = "Unable to find named cell \"" + locLookupColumnName + "\" in \"" + locLookupWorkbook + "\""
        logger.warning ( message )
        raise RuntimeError(message)
    xsheet, rowxlo, rowxhi, colxlo, colxhi = locXlrdName.area2d()
    locLookupColumnNameNumber = colxlo
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
    # Read through the XLS and extract the original and looked up locations
    for irow in range(headerRow + 2,sheet.nrows):
	locCell = sheet.cell_value(irow,locLookupColumnNameNumber)
        if ( locCell == None ):
            logger.warning("Original location name is blank in Excel row " +
                str(irow + 1) + " - will not be able to translate")
            continue
        locCell = locCell.strip()
	locCell2 = sheet.cell_value(irow,locLookupColumnNameNumber2)
        if ( locCell2 == None ):
            # Set to the same value
            logger.warning("Original location name does not have alternate in Excel row " +
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
