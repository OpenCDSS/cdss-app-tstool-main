# Simple Python script to convert TSTool 1-column summary file to a csv
#
# Run with:
#
# python summary2csv.py input-files
#
# Output files will have the same name as input with .csv appended
#
# Steve Malers, 2010-09-12


import glob
import re
import sys

# Get the file names - single file or wildcard pattern
infiles = glob.glob(sys.argv[1])

# Loop through the files to process
for infile in infiles:
	# Assume incoming paths may have single \ but need \\ internally
	infile = infile.replace('\\','\\\\')
	outfile = infile + ".csv"
	# Read through the input file and put into a 2-dimensional array
	delim = ","
	cells = []
	inf = open(infile,"r")
	outf = open(outfile,"w")
	print("Converting \"" + infile + "\" to \"" + outfile + "\"..." )
	outf.write("# Converted TSTool summary to csv using Python summary2csv script\n")
	outf.write("#\n")
	nrows = 0
	dataFound = False
	for line in inf:
		if ( dataFound == False ):
			# Just print as a comment
			outf.write("# " + line.strip() + "\n" )
			if ( line.startswith("-------") ):
				# Wile take effect in the next line
				dataFound = True
			# Not a data line so don't try to reformat
			continue
		else:
			if ( line.startswith("-------") ):
				# Skip
				continue
			else:
				# Data line
				colonpos = line.find(":")
				if ( colonpos > 0 ):
					# Have date and time so always have year, month, and day
					# Find the space after the :
					spacepos = line.find(" ",colonpos)
					datetime = line[0:spacepos].strip()
					value = line[spacepos:].strip()
				else:
					# Just date
					datetime = re.split(line," ")[0].trim()
					value = re.split(line," ")[1].trim()
				outf.write("\"" + datetime + "\"," + value + "\n")
	inf.close()
	outf.close()
