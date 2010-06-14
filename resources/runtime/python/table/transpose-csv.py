#
# Simple Python script to transpose a CSV file so that rows are columns.
# This is useful for:
#
#  * getting around the Excel 256 column limit.
#  * taking a column of identifiers and creating a single row of csv
#
# There is very little error checking in this script.
#
# 1. The input is allowed to have # comments at the top - they are discarded.
# 2. The individual cell contents are trimmed (leading and trailing whitespace is removed).
# 3. Filenames with \ in the names replace with \\ for internal use.
#
# Usage:  python transpose-csv.py infile.csv outfile.csv
#
# Steve Malers, 2010-05-21

import re
import sys

# Get the file names - assume incoming paths may have single \ but need \\ internally
infile = sys.argv[1].replace('\\','\\\\')
outfile = sys.argv[2].replace('\\','\\\\')

# Read through the input file and put into a 2-dimensional array
delim = ","
cells = []
inf = open(infile,"r")
nrows = 0
for line in inf:
	if ( line.startswith("#") ):
		# Comment
		continue
	cells.append(re.split(delim,line.strip()))
	nrows = nrows + 1
inf.close()

# Write the 2-D array in transposed fashion.
# Assume that the number of columns is consistent

outf = open(outfile,"w")
ncols = len(cells[0])
print ( "Read file with " + str(nrows) + " rows and " + str(ncols) + " columns" )
for icol in range(0,ncols):
	for irow in range(0,nrows):
		if ( irow > 0 ):
			outf.write(delim)
		outf.write(cells[irow][icol].strip())
	outf.write("\n")
outf.close()
