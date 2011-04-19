#
# Simple Python script to convert a CSV file in format:
#
# Year,ID,Value
# NNNN,ID1,value
# NNNN,ID2,value
# ...
#
# where multiple year and ID values are in a long list of data values,
# to a table in format (ID is represented in each year):
# USE ONLY IF THE IDS ARE REPRESENTED IN EACH YEAR (SAME NUMBER OF DATA
# VALUES EACH YEAR).
#
# Year,ID1,ID2,ID3
# NNNN,value1,value2,value3
# ...
#
# This is useful for:
#
#  * converting a stream of data to a table that TSTool can read
#
# There is very little error checking in this script.
#
# 1. The input is allowed to have # comments and double-quoted header lines at the top -
#    the lines are discarded.
# 2. The individual cell contents are trimmed (leading and trailing whitespace is removed).
# 3. Filenames with \ in the names replace with \\ for internal use.
#
#
# Usage:  python datalist-to-table.py infile.csv outfile.csv
#
# Steve Malers, 2011-02-28

import re
import sys

def getIdList ( dataRows ):
    """
    Return the unique list of identifiers from the data.
    The sorted data rows are parsed until an ID shows up again.
    """
    idList = []
    dateTimePrev = ""
    for dataRow in dataRows:
        cols = dataRow.split(delim)
        dateTime = cols[0].strip()
        if ( (dateTimePrev != "") and (dateTimePrev != dateTime) ):
            # New date/time so ID list is complete
            break
        else:
            # Add the ID to the list
            id = cols[1].strip()
            idList.append ( id )
        dateTimePrev = dateTime
    return idList

# Get the file names - assume incoming paths may have single \ but need \\ internally
infile = sys.argv[1].replace('\\','\\\\')
outfile = sys.argv[2].replace('\\','\\\\')

# Read through the input file and save all the strings in a list
delim = ","
dataRows = []
inf = open(infile,"r")
for line in inf:
    if ( line.startswith("#") or line.startswith("\"") ):
        # Comment
        continue
    # Save the data row to output later
    dataRows.append(line)
inf.close()
# Sort the lines, which will sort by date and ID
dataRows.sort()

# Get the list of identifiers
idList = getIdList(dataRows)

# Loop through data and output in rows by date

outf = open(outfile,"w")
outf.write("# Created by:\n" )
outf.write("# " + sys.argv[0] + " " + infile + " " + outfile + "\n" )
outf.write("#\n" )
dateTimePrev = ""
for dataRow in dataRows:
    # Split the values
    cols = dataRow.split(delim)
    dateTime = cols[0].strip()
    id = cols[1].strip()
    value = cols[2].strip()
    if ( dateTime != dateTimePrev ):
        # If necessary write the main header and start a new line
        if ( dateTimePrev == "" ):
            # Write the first line of data
            outf.write('"Year"')
            for id in idList:
                outf.write(',"' + id + '"' )
        outf.write("\n" + dateTime )
        dateTimePrev = dateTime
    # Write the data value
    outf.write(delim + value )
# Final newline
outf.write("\n")
outf.close()

