# Table class
#
# This is a scaled down version of the Java DataTable code.
# In the future, more complete Python or Jython modules may be available.

import logging
import re

def readTableFromDelimitedFile ( filename, delim="," ):
    """
    Read a table from a delimited file.  Comment lines start with #.
    """
    logger = logging.getLogger()
    inf = open(filename,"r")
    nrows = 0
    columnNamesRead = False
    # Parse column data allowing quoted strings
    # See http://stackoverflow.com/questions/2785755/how-to-split-but-ignore-separators-in-quoted-strings-in-python
    #pattern = re.compile(r'''((?:[^''' + delim + r'''"']|"[^"]*"|'[^']*')+)''')
    # The following results in delimiters as separate fields
    #pattern = re.compile(r'''((?:[^,"']|"[^"]*"|'[^']*')+)''')
    pattern = re.compile(''',(?=(?:[^'"]|'[^']*'|"[^"]*")*$)''')
    columnNames = []
    table = None
    for line in inf:
        logger.info("Processing line:  " + line)
        if ( line.startswith("#") ):
            # Comment
            continue
        if ( columnNamesRead == False ):
            # Expecting that the first non-comment row is column names
            # TODO SAM 2011-06-17 Handle quoted strings with delimiters - for now just strip quotes
            columnNames = pattern.split(line.strip())
            # The split does not remove the quotes so do that here
            for i in range(0,len(columnNames)):
                columnNames[i] = columnNames[i].replace('"','').strip()
            columnNamesRead = True
            logger.info("Column names:  " + str(columnNames))
            table = Table ( columnNames )
        else:
            table.appendRow(pattern.split(line.strip()))
    inf.close()
    return table

class Table ( object ):
    def __init__ ( self, columnNames ):
        """
        Initialize a new table.  Data can be added by calling appendRow().

        columnNames - the names of the columns
        """
        self.__columnNames = columnNames
        self.__rows = []
        return

    def getColumnNames ( self ):
        """
        Return the table column names.
        """
        return self.__columnNames

    def getColumnNumber ( self, columnNameReq ):
        """
        Return the column number (0+) for a column name.
        """
        columnNameReq = columnNameReq.upper()
        columnNumber = -1
        #print "getColumnNames: " + str(self.getColumnNames())
        for columnName in self.getColumnNames():
            columnNumber = columnNumber + 1
            if ( columnName.upper() == columnNameReq ):
                return columnNumber
        return -1

    def getColumnValues ( self, columnName ):
        """
        Return the column values for the column name.
        """
        columnNumber = self.getColumnNumber ( columnName )
        print "Column number is " + str(columnNumber)
        columnValues = []
        if ( columnNumber < 0 ):
            return columnValues
        for row in self.__rows:
            columnValues.append(row[columnNumber])
        return columnValues

    def appendRow ( self, rowValues ):
        """
        Add a row of data for the table - the number of values much match the number of columns.

        rowValues - the values for the row
        """
        self.__rows.append(rowValues)
        return
