# This module contains common functionality related to Excel processing

import logging
import os

import xlrd # For reading from Excel
assert xlrd.__VERSION__ == "0.7.1"

import loggingutil

def absolutePath(filename):
    """
    Convert a filename to an absolute path if not already an absolute path.
    """
    if ( os.path.isabs(filename) ):
        return filename
    else:
        return os.path.abspath(filename)
    
def enforceType ( cellValue, cellType, dataMapType ):
    """
    Enforce the cell value type based on what is requested.  For example, a column may
    be known to be an integer; however, Excel may represent internally as a float with
    a decimal.
    Booleans allow input as Yes/Y/1/True/T, No/N/0/False/F, with all others being set to None.
    cellValue - the original cell value, from XLRD Cell.value
    cellType - the cell type, from XLRD Cell.ctype
    dataMapType - the cell type from the data map (Python type)
    """
    newCellValue = cellValue
    if ( dataMapType == int ):
        # Convert to an integer or set to None if not an integer
        try:
            newCellValue = int(cellValue)
        except ValueError:
            newCellValue = None
    elif ( dataMapType == float ):
        # Convert to a float or set to None if not a float
        logger = logging.getLogger()
        logger.info("convert to float")
        try:
            newCellValue = float(cellValue)
        except ValueError:
            newCellValue = None
    elif ( dataMapType == string ):
        # Convert to a string, should always work, but maybe str() is not implemented
        try:
            newCellValue = str(cellValue)
        except ValueError:
            newCellValue = None
    if ( False == True ):
        # Comment this out for now - rely on validateCellContents to point out bad boolean strings
        if ( dataMapType is bool ):
            # Should be simple representation so convert to a string and do checks
            stringValue = str(cellValue)
            stringValueUpper = stringValue.upper()
            if ( stringValueUpper in ["YES", "Y", "1", "TRUE", "T"] ):
                newCellValue = True
            elif ( stringValueUpper in ["NO", "N", "0", "FALSE", "F" ] ):
                newCellValue = False
            else:
                # Should have been checked during validation
                newCellValue = None
    return newCellValue

def formatLoadTableSQLForDataSheet ( sheet, loadTableName, columnNameDictKey, columnTypeDictKey,
    columnTypeLenDictKey, requiredDictKey, keyColumn, namedCells, loadMap ):
    """
    Used when processing a data sheet Excel file (such as for providers),
    which is being extracted to a flat row and inserted into a temporary load table.
    Create the SQL to process named cell values into the raw database table.
    Return a tuple with (createSQL, insertSQL, updateSQL), to create the raw table,
    insert a row, and update a row.
    sheet - the sheet that is being processed
    loadTableName - the name of the raw table to create
    columnNameDictKey - the dictionary key that has the column name for the load
    columnTypeDictKey - the dictionary key that has the column type (for validation)
    columnTypeLenDictKey - the dictionary key that has the column type length (for table creation)
    requiredDictKey - the dictionary key that indicates whether the value is required
    keyColumn - the column name that is the unique key for inserts/updates
    namedCells - a list of xlrd Name classes for user-defined named cells
    loadMap - a list of dictionaries that describe the loadMapDict from cell values to database tables
    """
    logger = logging.getLogger()
    # The part of the SQL that contains the table and column names
    create1 = "CREATE TABLE " + loadTableName + " ("
    insert1 = "INSERT INTO " + loadTableName + " ("
    # The part of the SQL that contains the data values
    insert2 = ") VALUES ("
    update1 = "UPDATE " + loadTableName + " SET "
    # Only want to insert rows that have named cell values.
    # Therefore, construct the insert based on what is in the named cell list (other columns will default)
    #
    # Accumulate multiple errors and check needed data against None in the loop
    # in order to completely warn about data issues.
    count = 0 # Number of named cells processed
    goodCount = 0 # Number of named cells with proper data loadMapDict information
    keyValue = None
    for namedCell in namedCells:
        count += 1
        try:
            cell = namedCell.cell()
            cellValue = cell.value
        except xlrd.XLRDError:
            loggingutil.warning ( logger, "Could not get named cell value (not simple cell) for name " + namedCell.name ) 
            cellValue = None
        # Get load information from the map definition
        loadMapDict = getLoadMapForNamedCell ( columnNameDictKey, loadMap, namedCell.name )
        if ( loadMapDict == None ):
            loggingutil.warning ( logger, "No load loadMapDict entry exists for named cell \"" + namedCell.name + "\"" )
            continue
        # Get information out of the loadMapDict
        if ( namedCell.name.upper() == keyColumn.upper() ):
            keyValue = namedCell.name
        columnType = loadMapDict[columnTypeDictKey]
        # Optional length (for strings)
        try:
            columnWidth = loadMapDict[columnTypeLenDictKey]
        except KeyError:
            columnWidth = None # Not needed or use default
        rawColumn = loadMapDict[columnNameDictKey]
        if ( validateCellContents(namedCell.name, None, cellValue, columnType,
            loadMapDict[requiredDictKey]) == False ):
            # The cell value does not agree with the expected type
            loggingutil.warning ( logger, "The expected column type " + str(columnType) +
                "\" does not agree with the cell contents for named cell " +
                namedCell.name + "\" - can't load." )
        else:
            # Have enough information to do the insert
            if ( count > 1 ):
                # Add a comma to separate values
                create1 += ","
                insert1 += ","
                insert2 += ","
                update1 += ","
            # Now add the column name
            # Create...
            sqlType = getSqlDataType(columnType, columnWidth )
            if ( sqlType == None ):
                loggingutil.warning ( logger, "The column type " + str(columnType) +
                    " does not correspond to a SQL Server data type." )
                continue
            create1 += rawColumn + " " + sqlType
            # Insert...
            insert1 += rawColumn
            if ( columnType is str ):
                # Surround in quotes
                insert2 += " '" + str(cellValue) + "'"
            else:
                insert2 += str(cellValue)
            # Update...
            if ( columnType is str ):
                update1 += rawColumn + "='" + str(cellValue) + "'"
            else:
                update1 += str(cellValue)
            goodCount = goodCount + 1
    if ( goodCount == count ):
        # Did not have any warnings so the SQL can be completely formatted
        createSQL = create1 + ")"
        insertSQL = insert1 + ") " + insert2 + ")"
        if ( keyValue != None ):
            updateSQL = update1 + " WHERE " + keyColumn + "='" + keyValue + "'"
        else:
            loggingutil.warning ( logger, "The '" + keyColumn +
                "' named cell is not defined - can't create update SQL." )
            updateSQL = None
        return (createSQL, insertSQL, updateSQL)
    else:
        # Don't have any valid named cells to process
        return (None, None, None)
    
def formatLoadTableSQLForDataRow ( loadMap, row, loadTableName, namedCellDictKey,
    recordKeyColumnDictKey, columnNameDictKey, columnTypeDictKey, columnTypeLenDictKey,
    requiredDictKey, namedCells, cellValues, cellTypes ):
    """
    Used when processing a column-based sheet Excel file (such as for NCNA list),
    which is into a database table.
    Create the SQL to process named cell values into the raw database table.
    Return a tuple with (createSQL, insertSQL, updateSQL), to create the raw table,
    insert a row, and update a row.
    row - row being loaded, for messaging
    loadMap - the list of dictionaries defining the data load
    loadTableName - the name of the raw table to create
    namedCellDictKey - the dictionary key corresponding to the Excel named cell/column
    recordKeyColumnDictKey - the dictionary key corresponding to the primary key column in the table to load
    columnNameDictKey - the dictionary key corresponding to the column name for the load
        (this will also be used for the WHERE in updates if the column is identified as the primary
        key column based on recordKeyColumnDictKey)
    columnTypeDictKey - the dictionary key corresponding to the column type (for validation)
    columnTypeLenDictKey - the dictionary key corresponding to the column type length (for table creation)
    requiredDictKey - the dictionary key that indicates whether the value is required
    namedCells - a list of xlrd Name classes for user-defined named cells (may contain None
        entries for map cells that are not defined in the Excel sheet)
    cellValues - a list of xlrd Cell values corresponding to the cells being processed
    cellTypes - a list of xlrd Cell types corresponding to the cells being processed
    """
    logger = logging.getLogger()
    # The part of the SQL that contains the table and column names
    create1 = "CREATE TABLE " + loadTableName + " ("
    insert1 = "INSERT INTO " + loadTableName + " ("
    # The part of the SQL that contains the data values
    insert2 = "VALUES ("
    update1 = "UPDATE " + loadTableName + " SET "
    # Only want to insert rows that have named cell values.
    # Therefore, construct the insert based on what is in the named cell list (other columns will default)
    #
    # Accumulate multiple errors and check needed data against None in the loop
    # in order to completely warn about data issues.
    count = 0 # Number of named cells processed
    goodCount = 0 # Number of named cells with proper data map information
    keyColumn = None # not defined
    keyValue = None # The value from the sheet to use as the key for an update
    keyType = None # the Python type for the key
    # Loop through load map cells to load
    for loadMapDict in loadMap:
        namedCell = namedCells[count]
        cellValue = cellValues[count]
        cellType = cellTypes[count]
        count += 1
        # If no Excel named cell was found, skip the value (warning would have been
        # generated during initial sheet processing)
        if ( namedCell == None ):
            continue
        # Get information out of the map
        if ( loadMapDict[namedCellDictKey].upper() == recordKeyColumnDictKey.upper() ):
            # The named cell is for the primary key column used to do updates
            keyValue = cellValue
            keyColumn = loadMapDict[columnNameDictKey] # key column in table being loaded
            keyType = loadMapDict[columnTypeDictKey] # key column in table being loaded
        columnType = loadMapDict[columnTypeDictKey]
        # Optional length (for strings)
        try:
            columnWidth = loadMapDict[columnTypeLenDictKey]
        except KeyError:
            columnWidth = None # Not needed or use default
        columnName = loadMapDict[columnNameDictKey]
        # TODO SAM 2010-07-09 Evaluate whether validation should occur here.
        # For now do it here because data are actually being processed
        if ( validateCellContents(columnName, row, cellValue, cellType, columnType,
            loadMapDict[requiredDictKey]) == True ):
            # Have enough information to do the insert
            if ( count > 1 ):
                # Add a comma to separate values
                create1 += ","
                insert1 += ","
                insert2 += ","
                update1 += ","
            # Now add the column name
            # Create...
            sqlType = getSqlDataType(columnType, columnWidth )
            if ( sqlType == None ):
                loggingutil.warning ( logger, "The column type " + str(columnType) +
                    " does not correspond to a SQL Server data type." )
                continue
            create1 += columnName + " " + sqlType
            # Insert...
            insert1 += columnName
            insert2 += " " + formatSqlValueString(columnType, cellValue)
            # Update...
            update1 += columnName + "=" + formatSqlValueString(columnType,cellValue)
            # Was able to process so increment the count of good values
            goodCount = goodCount + 1
    if ( goodCount == count ):
        # Did not have any warnings so the SQL can be completely formatted
        createSQL = create1 + ")"
        insertSQL = insert1 + ") " + insert2 + ")"
        if ( keyValue != None ):
            updateSQL = update1 + " WHERE " + keyColumn + "=" + formatSqlValueString(keyType,keyValue)
        else:
            if ( keyColumn == None ):
                loggingutil.warning ( logger, "The key column could not be determined for row " +
                    str(row) + " - can't create UPDATE SQL." )
            else:
                loggingutil.warning ( logger, "The key '" + str(keyColumn) +
                    "' value is not defined in row " + str(row) + " - can't create UPDATE SQL." )
            updateSQL = None
        return (createSQL, insertSQL, updateSQL)
    else:
        # Don't have valid named cells to process
        return (None, None, None)
    
def formatSqlValueString ( type, value ):
    """
    Convert the value into a string suitable for use in SQL statements.  The string will
    be surrounded by single quotes and any single quotes in the string will be replaced by
    two single quotes, as per SQL Server conventions.
    type - Python type
    value - string value to process
    """
    if ( type is bool ):
        if ( value == True ):
            return str(1)
        else:
            return str(0)
    elif ( type is str ):
        # Convert to string just in case value from spreadsheet came out as something else
        return "'" + str(value).replace("'", "''") + "'"
    else:
        return str(value)

def countNonBlankCellsInColumn ( sheet, startRow, endRow, column, returnOnFirst ):
    """
    Determine how many cell values in a column are non-blank.
    Counting stops as soon as a blank occurs.
    sheet - the XLRD sheet object
    startRow - starting row (0+) to process
    endRow - ending row (0+) to process
    column - the column to read
    returnOnFirst - if True, return when the first blank cell is found;
        if False, process the full row range
    """
    nonBlankCount = 0
    for row in range(startRow,(endRow + 1)):
        # Output for user
        if ( sheet.cell_value(row,column) == ''):
            if ( returnOnFirst == True ):
            	break
        else:
            # Have a non-blank
            nonBlankCount = nonBlankCount + 1
    return nonBlankCount

def getLoadMapNamedCells ( dataSheetLoadMap, cellNameKey, namedCells,
    loadFinal, loadColumnKey, finalColumnKey ):
    """
    Return a list of xlrd Name instances that match the load map named cells.
    If a load map named cell does not exist in the sheet, print a warning and set
    the value to None in the returned list.
    dataSheetLoadMap - the list of dictionaries containing load map information
    cellNameKey - the dictionary key that contains the named cell name
    namcedCells - the list of xlrd Name instances read from the sheet
    """
    logger = logging.getLogger()
    loadMapNamedCells = []
    for loadMapDict in dataSheetLoadMap:
        # Check whether final or temporary table is being loaded and skip the
        # column if the data map is not defined
        loadTableColumn = None
        finalTableColumn = None
        try:
            loadTableColumn = loadMapDict[loadColumnKey]
        except KeyError:
            loadTableColumn = None
        try:
            finalTableColumn = loadMapDict[finalColumnKey]
        except KeyError:
            finalTableColumn = None
        if ( loadFinal == False ):
            # If the load column is not specified then ignore the item
            if ( loadTableColumn == None ):
                continue
        else:
            # If the final column is not specified then ignore the item
            if ( finalTableColumn == None ):
                continue
        cellNameKeyUpper = loadMapDict[cellNameKey].upper()
        found = False
        for namedCell in namedCells:
            if ( cellNameKeyUpper == namedCell.name.upper() ):
                loadMapNamedCells.append(namedCell)
                found = True
                break
        if ( found == False ):
            loggingutil.warning(logger, "Load map named cell \"" + loadMapDict[cellNameKey] +
                "\" is not defined in the sheet.  Cannot load the corresponding data." )
            loadMapNamedCells.append(None)
    return loadMapNamedCells

def getLoadMapForNamedCell ( cellNameKey, loadMap, cellName ):
    """
    Return the sheet to database map for the name cell, or None if the cell name is
    not found.  Name comparisons are case-insensitive.
    cellNameKey - the dictionary key used for the cell name
    loadMap - list of dictionaries that define the data load map
    cellName - name of cell to lookup map (e.g., "cellName" for provider sheets and
        "columnName" for column-oriented sheets).
    """
    cellNameUpper = cellName.upper()
    for cellDict in loadMap:
        if ( cellDict[cellNameKey].upper() == cellNameUpper ):
            # Found the cell of interest
            return cellDict
    return None

def getSqlDataType(columnType, typelen):
    """
    Determine the SQL data type given the Python data type used in the map file.
    Return a string type for SQL Server.
    columnType - the Python column type
    typelen - the length to apply to the column (mainly used for varchar strings) - if None,
    default to 20
    """
    if ( columnType is bool ):
        return "bit"
    elif ( columnType is float ):
        return "float(24)"
    elif ( columnType is int ):
        return "int"
    elif ( columnType is str ):
        if ( typelen == None ):
            typelen = 20
        return "varchar(" + str(typelen) + ")"
    return None

def openExcelApp ( wbFile ):
    """
    Open an Excel application and return the application and workbook instance
    as a tuple.
    """
    """
    excelApp = Excel.ApplicationClass()
    excelApp.Visible = True
    logger = logging.getLogger()
    logger.info ( "Opening Excel file \"" + wbFile + "\"" )
    wb = excelApp.Workbooks.Open ( wbFile )
    return excelApp, wb
    """

def readNamedCells ( book, sheet = None ):
    """
    Read the named cell information from an open workbook.
    Return a list of xlrd Name objects.
    book - an open xlrd book
    sheet - if not None, limit the named cells to the specified sheet
    """
    logger = logging.getLogger()
    namedCells = []
    for name in book.name_obj_list:
        if ( name.builtin != 0 ):
            # Not a user-defined name so don't add
            continue
        try:
            name.cell()
        except xlrd.XLRDError:
            # Not a simple cell (multi-cell named area) so ignore
            # TODO SAM 2010-07-06 Isn't there an easier way to do this to avoid the stdout dump?
            continue
        if ( sheet != None ):
            # Only want the names for a specific sheet
            try:
                sheet_object, rowxlo, rowxhi, colxlo, colxhi = name.area2d()
                if ( sheet_object != sheet ):
                    # Not the same sheet so don't add
                    continue
            except xlrd.XLRDError:
                # Name does not refer to a rectangular area (so don't want to process)
                continue
        if ( name.macro == 0 ):
            # A simple name defined by the user
            logger.info ( "Read named cell=\"" + name.name + "\"" )
            # Append the full name to the list
            namedCells.append ( name )
    if ( sheet == None ):
        logger.info ( "Read " + str(len(namedCells)) + " user-defined named cells (all sheets)." )
    else:
        logger.info ( "Read " + str(len(namedCells)) +
            " user-defined named cells (sheet \"" + sheet.name + "\")." )
    return namedCells

def validateCellContents ( cellName, row, value, xlType, mapColumnType, isRequired ):
    """
    Validate the cell value against the expected column type.
    Return True if the cell value validates, False if not.
    cellName - the named cell (or column) being processed
    row - the row being processed (if None then a specific cell is being processed)
    value - value of the cell, from XLRD Cell.value
    xltype - the XLRD cell type, from Cell.ctype
    mapColumnType - the expected column type, from the data map (Python types)
    isRequired - if True, a non-blank cell value is required
    """
    logger = logging.getLogger()
    isValid = True
    if ( row != None ):
        endString = " for named column " + cellName + "\" and row " + str(row) + " - can't load."
    else:
        endString = " for named cell " + cellName + "\" - can't load."
    # First check to see if a value is required but instead is blank
    if ( (xlType == xlrd.XL_CELL_EMPTY) or (xlType == xlrd.XL_CELL_BLANK) or (value == None) ):
        if ( isRequired == True ):
            isValid = False
            loggingutil.warning ( logger, "The expected data type " + str(mapColumnType) +
                "\" is required to be non-blank" + endString )
        else:
            # OK to be blank/None, but can't continue with checks
            return isValid
    if ( isValid == True ):
        # Do additional checks...
        stringValue = str(value).strip()
        stringValueUpper = stringValue.upper()
        # Check that required numbers are actually numbers
        if ( mapColumnType is bool ):
            # Expect the contents to be Yes/Y,1/True/T or No/N/0/False/F - convert to string and check
            if ( not stringValueUpper in ["YES", "Y", "1", "TRUE", "T", "NO", "N", "0", "FALSE", "F"] ):
                loggingutil.warning ( logger, "The expected data type " + str(mapColumnType) +
                "\" (Yes/Y/1/True/T No/N/0/False/F) does not agree with the cell contents (" +
                stringValue + ")" + endString )
                logger.info ( "xlType=" + str(xlType) + " isRequired=" + str(isRequired) )
                isValid = False
        elif ( (mapColumnType is float) or (mapColumnType is int) ):
            if ( xlType != xlrd.XL_CELL_NUMBER ):
                loggingutil.warning ( logger, "The expected data type " + str(mapColumnType) +
                    "\" does not agree with the cell contents (" + stringValue + ")" + endString )
                isValid = False
    # TODO SAM 2010-07-06 Evaluate whether more checks are needed.
    return isValid
