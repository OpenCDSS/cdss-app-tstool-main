#! /usr/bin/env python
#
# The above is included to run Python automatically on Linux.

"""
This script processes an NRCS SNOW COURSE historical file into a comma-separated-value
(CSV) delimited file with date, value, and flag.  An example of data is shown below, retrieved
from the URL:  http://www.wcc.nrcs.usda.gov/ftpref/data/snow/snow_course/table/history/colorado/05j37s.txt
The delimited file can then be read into TSTool using the ReadDelimitedFile() command.

=================

Station : 05J37S, JOE WRIGHT PILLOW
-------
Unit = inches
year/   January        February        March          April           May            June     
card date dep  swe  date dep  swe  date dep  swe  date dep  swe  date dep  swe  date dep  swe 
     ---- --- ----- ---- --- ----- ---- --- ----- ---- --- ----- ---- --- ----- ---- --- -----
67-1                E/ST      10.5 E/ST      15.6 E/ST      19.2 E/ST      23.5
68-1                E/ST      11.6 E/ST      16.2 E/ST      19.8 E/ST      26.2
69-1                E/ST      13.5 E/ST      16.3 E/ST      18.9 E/ST      20.9

70-1                E/ST      17.6 E/ST      20.7 E/ST      24.6 E/ST      32.0
71-1                E/ST      15.6 E/ST      20.6 E/ST      24.0 E/ST      30.1 E/ST      28.0
72-1 E/ST       8.1 E/ST      13.2 E/ST      15.9 E/ST      18.6 E/ST      24.6 E/ST      12.7
73-1 E/ST      10.5 E/ST      14.1 E/ST      15.3 E/ST      19.5 E/ST      28.3 E/ST      26.1
74-1 E/ST       8.8 E/ST      15.2 E/ST      18.2 E/ST      21.8 E/ST      30.2 E/ST      13.4

75-1 E/ST       9.2 E/ST      13.2 E/ST      15.9 E/ST      21.9 E/ST      24.8 E/ST      22.0
76-1 E/ST       8.0 E/ST      11.0 E/ST      15.7 E/ST      19.4 E/ST      23.7 E/ST      15.5
77-1 E/ST       5.1 E/ST       8.2 E/ST      10.2 E/ST      14.2 E/ST      16.3 E/ST       7.9
78-1 E/ST      12.1 E/ST      17.5 E/ST      22.3 E/ST      23.9 E/ST      28.2 E/ST      21.8
79-1 1/01      11.9 2/01      22.0 3/01      24.3

80-1 1/01       7.8 2/01      15.0 3/01      18.8 4/01      24.3 5/01      27.6 6/01      21.6
81-1 1/01       5.9 2/01       7.7 3/01       9.6 4/01      15.0 5/01      11.5 6/01       7.8
82-1 1/01      12.3 2/01      16.3 3/01      17.6 4/01      20.8 5/01      23.0 6/01      24.6
83-1 1/01      12.7 2/01      14.4 3/01      17.1 4/01      26.6 5/01      35.1 6/01      36.4
84-1 1/01      17.6 2/01      20.3 3/01      22.8 4/01      26.5 5/01      34.9 6/01      19.5

85-1 1/01       9.8 2/01      13.8 3/01      15.6 4/01      19.1 5/01      23.6 6/01      13.4
86-1 1/01      15.6 2/01      15.9 3/01      23.6 4/01      28.3 5/01      34.6 6/01      23.5
87-1 1/01      10.4 2/01      13.3 3/01      15.3 4/01      18.6 5/01      14.9 6/01       3.3
88-1 1/01       9.1 2/01      13.4 3/01      17.6 4/01      24.5 5/01      26.2 6/01      17.1
89-1 1/01       7.8 2/01      10.7 3/01      15.0 4/01      18.6 5/01 169   0.0 6/01       1.7

90-1 1/01      11.5 2/01      12.2 3/01      15.7 4/01      25.1 5/01      28.9 6/01      23.0
91-1 1/01       7.3 2/01      11.0 3/01      13.4 4/01      19.8 5/01      24.3 6/01      12.9
92-1 1/01       6.4 2/01      10.2 3/01      13.7 4/01      18.7 5/01      20.3 6/01       9.7
93-1 1/01       9.4 2/01      12.8 3/01      18.7 4/01      23.6 5/01      32.7 6/01      17.1
94-1 1/01      10.9 2/01      13.3 3/01      18.1 4/01      20.5 5/01      20.2 6/01       0.9

95-1 1/01       5.1 2/01       8.5 3/01      11.5 4/01      14.9 5/01      23.9 6/01      33.6
96-1 1/01      11.8 2/01      19.9 3/01      23.7 4/01      28.7 5/01      34.3 6/01      22.8
97-1 1/01      12.2 2/01      18.6 3/01      20.8 4/01      24.5 5/01      35.0 6/01      20.6
98-1 1/01       8.7 2/01      12.6 3/01      16.4 4/01      19.5 5/01      23.2 6/01      10.6
99-1 1/01       7.7 2/01      14.5 3/01      17.4 4/01      19.4 5/01      22.6 6/01      16.6

 0-1 1/01       7.0 2/01      11.9 3/01      16.8 4/01      22.4 5/01      24.7 6/01       8.1
 1-1 1/01       9.9 2/01      11.2 3/01      14.5 4/01      18.1 5/01      20.3 6/01       2.4
 2-1 1/01       5.8 2/01       7.8 3/01      12.0 4/01      14.2 5/01      12.6 6/01       0.5
 3-1 1/01       8.5 2/01      10.5 3/01      16.2 4/01      24.4 5/01      30.1 6/01      18.8
 4-1 1/01       7.4 2/01       8.6 3/01      12.2 4/01      13.8 5/01      16.8 6/01       4.0

 5-1 1/01      10.0 2/01      12.5 3/01      14.9 4/01      19.3 5/01      22.3 6/01      12.6
 6-1 1/01      13.0 2/01      15.9 3/01      17.9 4/01      22.0 5/01      20.3 6/01       6.0
 7-1 1/01      11.4 2/01      13.6 3/01      17.1 4/01      19.8 5/01      22.4 6/01      14.3
 8-1 1/01       9.4 2/01      14.7 3/01      19.9 4/01      23.5 5/01      26.8 6/01      20.0
 9-1 1/01      10.9 2/01      16.1 3/01      18.6 4/01      23.6 5/01      26.3 6/01      12.4

70-2                                              E/ST      27.0
73-2                                                             E/ST      28.5
74-2                                                             E/ST      25.5

75-2                                                             E/ST      22.8
76-2                                                             E/ST      20.3
77-2                                                             E/ST      10.2
78-2                                                             E/ST      30.0
79-2 1/15      15.8 2/15      23.1 3/15      27.0

80-2 1/15      12.1 2/15      15.6 3/15      21.5 4/15      27.0 5/15      27.9 6/15       1.9
81-2 1/15       5.9 2/15       7.9 3/15      11.6 4/15      15.6 5/15      12.1 6/15       0.0
82-2 1/15      14.5 2/15      16.8 3/15      20.0 4/15      22.8 5/15      26.3 6/15      15.0
83-2 1/15      13.0 2/15      15.9 3/15      22.3 4/15      29.8 5/15      35.4 6/15      29.7
84-2 1/15      18.1 2/15      21.6 3/15      24.3 4/15      30.2 5/15      34.1 6/15      14.2

85-2 1/15      10.5 2/15      14.2 3/15      17.1 4/15      21.0 5/15      20.8 6/15       0.0
86-2 1/15      15.8 2/15      18.8 3/15      25.3 4/15      34.1 5/15      30.7 6/15      12.6
87-2 1/15      12.2 2/15      14.4 3/15      16.6 4/15      18.8 5/15      11.0 6/15       0.0
88-2 1/15      10.6 2/15      16.6 3/15      21.8 4/15      24.8 5/15      22.1 6/15       2.5
89-2 1/15      10.1 2/15      13.5 3/15      16.1 4/15      20.5 5/15      15.4 6/15       0.0

90-2 1/15      11.7 2/15      14.8 3/15      21.3 4/15      27.4 5/15      30.2 6/15       5.2
91-2 1/15       8.4 2/15      12.1 3/15      17.1 4/15      21.8 5/15      22.0 6/15       1.4
92-2 1/15       9.6 2/15      11.6 3/15      16.1 4/15      18.3 5/15      13.4 6/15       0.0
93-2 1/15      11.3 2/15      16.0 3/15      20.3 4/15      29.1 5/15      29.8 6/15      15.0
94-2 1/15      12.5 2/15      15.7 3/15      18.7 4/15      22.0 5/15      14.8 6/15       0.0

95-2 1/15       6.6 2/15      11.1 3/15      12.8 4/15      17.0 5/15      32.0 6/15      25.5
96-2 1/15      14.7 2/15      20.6 3/15      25.9 4/15      31.2 5/15      30.6 6/15       6.5
97-2 1/15      16.9 2/15      19.8 3/15      21.8 4/15      28.9 5/15      32.2 6/15       9.1
98-2 1/15      10.5 2/15      14.1 3/15      18.0 4/15      22.4 5/15      21.6 6/15       5.8
99-2 1/15      10.0 2/15      15.8 3/15      19.4 4/15      21.2 5/15      23.9 6/15       7.5

 0-2 1/15       9.1 2/15      14.3 3/15      19.6 4/15      22.6 5/15      19.7 6/15       0.0
 1-2 1/15      10.3 2/15      13.0 3/15      15.6 4/15      20.5 5/15      15.7 6/15       0.0
 2-2 1/15       6.6 2/15       9.4 3/15      14.1 4/15      12.1 5/15       8.1 6/15       0.0
 3-2 1/15       9.3 2/15      12.5 3/15      17.1 4/15      24.3 5/15      33.0 6/15       8.5
 4-2 1/15       8.1 2/15       9.8 3/15      12.9 4/15      16.3 5/15      11.1 6/15       0.0

 5-2 1/15      12.0 2/15      13.6 3/15      15.8 4/15      20.5 5/15      23.7 6/15       7.2
 6-2 1/15      14.1 2/15      17.4 3/15      20.1 4/15      20.7 5/15      19.4 6/15       0.0
 7-2 1/15      12.2 2/15      13.9 3/15      18.7 4/15      22.1 5/15      20.7 6/15       4.4
 8-2 1/15      12.4 2/15      17.9 3/15      21.0 4/15      27.4 5/15      29.6 6/15      12.4
 9-2 1/15      13.0 2/15      17.0 3/15      19.8 4/15      24.6 5/15      23.5 6/15       3.0


FIRST OF MONTH MEASUREMENTS
average depth and swe :
                9.7           13.5           17.0           21.0      169  24.5           15.3
years       0    38        0    43        0    43        0    42        1    42        0    38
1971-2000 average :
                9.7           13.9           17.3           21.5           25.1           17.0


MID-MONTH MEASUREMENTS
average depth and swe :
               11.5           15.1           19.0           23.3           23.0            6.2
years       0    31        0    31        0    31        0    31        0    36        0    30
1971-2000 average :
               11.6           15.5           19.0           23.6           23.8            7.4


NOTES:  O/dd - October, J/dd - November, K/dd - December, E/ST - estimate
        Card type 1 = First of Month, 2 = Mid-Month, and 3 = Special Measurement

----------

"""

from datetime import date
import getpass
import logging
import os
import re
import sys
import traceback

# Log file is global in module because it is used to print a message
# at the end of this script     
logfile = None

# The main function follows, with all other functions being alphabetized after.

def main ():
	"""
	Main program to control the processing.
	"""
	# Script name as typed on the command line, for messages, without path
	scriptname = os.path.basename(sys.argv[0])

	# The script version, to allow tracking changes over time
	version = "1.00 (2012-04-20)"

	# Get the log file name from the command line and initialize logging
	logFile = getCommandLineArgument ( 'logFile' )
	# Initialize logging to capture messages
	# If the logFIle=None, then standard output will be used for logging
	logger, theLogFile = initializeLogging ( logFile )
	# Get the input and output file names from the command line
	inputFile = getCommandLineArgument ( 'inputFile' )
	if ( inputFile == None ):
		print "** No input file specified **"
		printUsage()
		exit(1)
	else:
		logger.info('Processing input snow course file "' + inputFile + '"' )
	outputFile = getCommandLineArgument ( 'outputFile' )
	if ( outputFile == None ):
		print "** No output file specified **"
		printUsage()
		exit(1)
	else:
		logger.info('Creating output CSV file "' + outputFile + '"' )
	convertSnowCourseToCsv ( inputFile, outputFile, logger )
	return

def convertSnowCourseToCsv ( inputFile, outputFile, logger ):
	"""
	Convert the snow course file to a CSV file.

	inputFile - name of input file to read
	outputFile - name of output file to write
	logger - logger to use for logging (cannot be null)
	"""
	logger = logging.getLogger()
	logger.info("Writing data to CSV file \"" + outputFile + "\"" )
	delim = ","
	dataStringList = [] # Data records
	today = date.today()
	# 2 digit year for today
	todayYear = today.year - (today.year/100)*100
	f = open(inputFile,'r')
	i = 0
	# Iterate through the file.  Accumulate parsed data values and output at the end
	readingData = False
	monthHeaderFound = False
	for line in f:
		i = i + 1
		lineStripped = line.strip()
		if ( line.startswith("Station :") ):
			# Station ID and name
			pos = line.find(":")
			if ( pos > 0 ):
				linePart = line[pos + 1:].strip()
				pos = line.find(",")
				if ( pos > 0 ):
					lineParts = linePart.split(",")
					stationID = lineParts[0].strip()
					stationName = lineParts[1].strip()
		elif ( line.startswith("year/   January        February        March          April           May            June") ):
			# This line indicates the month order
			# If different, then this script does not know how to handle
			monthHeaderFound = True
		elif ( line.startswith("Units =") ):
			# Data units for snow course
			lineParts = line.split("=")
			units = lineParts[1].strip()
		elif ( line.startswith("     ---- --- -----" ) ):
			# The end of the header before the data line
			readingData = True
		elif ( line.startswith("FIRST OF MONTH MEASUREMENTS") ):
			# Done reading data
			break
		elif ( lineStripped == "" ):
			# Blank line
			continue
		elif ( readingData == True ):
			# Processing a data line
			# Pad to 94 characters to simplify parsing
			linePadded = line.ljust(94, " ")
			# Split out the fixed fields
			yearDateString = linePadded[0:4]
			yearDateStringParts = yearDateString.split("-")
			# 2 digit year, calendar year
			year = int(yearDateStringParts[0].strip())
			if ( year >= 100 ):
				year = year - 100
			# Convert to 4 digits for output
			if ( year <= todayYear ):
				# Assume 2000+
				year = year + 2000
			else:
				# Assume 1900+
				year = year + 1900
			readingCount = int(yearDateString[3:].strip())
			for month in range(1,7):
				# Loop through the months
				# Each month uses 15 characters, with 5 for the year/readingCount
				monthStart = 5 + (month - 1)*15 # First character for month
				monthDateStringOrig = linePadded[monthStart:monthStart + 4].strip()
				monthDepString = linePadded[monthStart + 5:monthStart + 8].strip()
				monthSweString = linePadded[monthStart + 9:monthStart + 14].strip()
				monthFlag = ""
				logger.info('Month date string = "' + monthDateStringOrig + '"')
				monthDateString = monthDateStringOrig
				if ( (monthDateStringOrig == "") and (monthDepString == "") and (monthSweString == "") ):
					# No date for month and no data so don't process
					continue
				else:
					# Date string will be something like 1/01 or 3/15
					# Make sure it is 01-01 or 03-15
					monthDateString = monthDateString.replace('/','-')
					if ( len(monthDateString) == 4 ):
						monthDateString = "0" + monthDateString
				# Reset some data based on whether date is "E/ST"
				if ( monthDateStringOrig == "E/ST" ):
					monthFlag = monthDateStringOrig
					if ( readingCount == 1 ):
						# Assume the first of the month
						logger.info("Assuming 1st of month for month " + str(month) + " data in cal year " + str(year))
						monthDateString = "%02d" % month + "-01"
					elif ( readingCount == 2 ):
						# Assume the 15th of the month
						logger.info("Assuming 15th of month for month " + str(month) + " data in cal year " + str(year))
						monthDateString = "%02d" % month + "-15"
					else:
						# Assume the 13th of the month + reading count (increment beyond day 15)
						logger.info("Assuming " + str(13+readingCount) + "th day of month for month " +
							str(month) + " data in cal year " + str(year) + " reading " + str(readingCount))
						monthDateString = "%02d" % month + "-" + str(13 + readingCount)
				dateString = str(year) + "-" + monthDateString
				#print "Processing " + dateString
				# Format the data lines as text, with date at front. Because multiple readings
				# may occur in a month but are listed separately in the file, the values will be out of order
				dataStringList.append(dateString + "," + monthDepString + "," + monthSweString + ',"' + monthFlag + '"' )
	# If the expected month header was not found, could have an unsupported file so exit
	if ( monthHeaderFound == False ):
		logger.warning("*** File format appears to be different than what is supported ***" )
		logger.warning("*** See script for example ***" )
		exit(1)
	# Now output the delimited file, starting with a commented header
	f.close()
	# Sort the data strings
	dataStringList.sort()
	# Finally, write the data strings to the output file
	f = open(outputFile,"w")
	f.write("# NRCS Snow Course time series\n")
	f.write("# StationID = " + stationID + "\n" )
	f.write("# StationName = " + stationName + "\n" )
	f.write("#\n" )
	f.write("# Original data with date E/ST is set to 1st day of month and flag is set to E/ST.\n" )
	f.write("# Similarly, observation 2 is set to 15th of month if E/ST.\n" )
	f.write("#\n" )
	f.write("# Date, Depth (in), SWE (in), Flag\n" )
	f.write('"Date","Depth","SWE","Flag"\n' )
	for dataString in dataStringList:
		f.write(dataString + "\n")
	f.close()
	return

def exit ( exitCode ):
	"""
	Exit the script.  This will throw a SystemExit exception that needs to be handled.

	exitCode - exit status integer to pass to the parent program, generally
		0 for success, 1 for failure
	"""
	logger = logging.getLogger()
	if ( exitCode != 0 ):
		if ( logfile == None ):
			print "Exiting with status " + str(exitCode) + "."
		else:
			print "Exiting with status " + str(exitCode) + ".  See the log file for explanation."
		logger.error ( "Exiting with status " + str(exitCode) )
	else:
		logger.info ( "Exiting with status " + str(exitCode) )
	sys.exit ( exitCode )
	return

def getCommandLineArgument ( argName ):
	"""
	Return the requested command line argument, or None if not specified.
	Command line arguments of form name=value are checked.
	"""
	argNameLower = argName.lower() + "="
	for arg in sys.argv[1:]:
		argLower = arg.lower()
		if ( argLower.startswith(argNameLower) ):
			return arg[len(argNameLower):]
	return None

def initializeLogging ( logFile ):
	"""
	Initialize logging for the script.
	A log file is created in the logs folder under the SNOTEL file
	location ith the user name and date.

	logFile - the logfile name

	Return a tuple with the logger and the log file for this script.
	"""
	theLogFile = logFile
	# Make sure that the log file directory exists
	#if ( (theLogFile != None) and not os.path.exists(os.path.dirname(theLogFile) ) ):
	#	os.makedirs ( os.path.dirname(theLogFile) )
	is_python24 = False
	version = str(sys.version_info[0]) + "." + str(sys.version_info[1])
	if ( float(version) >= 2.4 ):
		is_python24 = True
	if ( is_python24 == True ):
		# Use the newer function to configure logging
		if ( theLogFile == None ):
			# Default is to log to stderr
			logging.basicConfig ( format = '%(asctime)s %(levelname)8s %(message)s' )
		else:
			logging.basicConfig ( format = '%(asctime)s %(levelname)8s %(message)s',
				filename=theLogFile, filemode = 'w' )
		logger = logging.getLogger()
	else:
		# Use the following for Python older than 2.4
		# For now use the root logger
		logger = logging.getLogger ()
		if ( theLogFile != None ):
			hdlr = logging.FileHandler(theLogFile,'w')
		formatter = logging.Formatter('%(asctime)s %(levelname)s %(message)s')
		hdlr.setFormatter(formatter)
		logger.addHandler(hdlr)
	# For now log everything above as info but may need to see what low level code puts out
	logger.setLevel(logging.INFO)
	# TODO SAM 2008-03-03 Evaluate putting in more header information similar to Java ioutil.printCreatorHeader()
	logger.info("Running script:  " + sys.argv[0])
	if ( len(sys.argv) > 1 ):
		for arg in sys.argv[1:]:
			logger.info("     " + arg)
	logger.info("")
	logger.info("User:  " + getpass.getuser())
	logger.info("")
	return logger, theLogFile

def printUsage():
	"""
	Print the script usage.
	"""
	# Get the script name without the .py since this script is called by a batch file.
	scriptName=os.path.basename(sys.argv[0]).replace(".py","")
	print ""
	print "Command line usage:"
	print ""
	print scriptName + " inputFile=XXXX outputFile=XXXX [logFile=XXXX]"
	print ""
	print "Convert a snow course station historical data file to a CSV file."
	print ""
	print "Brackets indicate optional parameters."
	print ""
	print "inputFile = snow course historical data file"
	print "outputFile = output CSV file to create"
	print "logFile = log file (default is log to stderr)"
	print ""
	return

#-------------------------------------------------------------------------------
# Call the main function, which does all the work, and handle major unexpected
# errors.  Put the following at the end of the script.

try:
	# Run the main program if this is a main script
	if __name__ == "__main__":
		main ()

except SystemExit, error:
	# This exception is raised when system.exit() is called, as from
	# the exit() function.
	# Just let normal exit happen to shut down this script.
	#print 'Exiting...' + str(error)
	# Don't re-catch the exception that will be generated.
	pass

except:
	# Using the logger here assumes that basic logging setup was successful
	# in the main function.
	logger = logging.getLogger()
	logger.exception ( "Unexpected exception" )
	if ( logfile == None ):
		print 'Unexpected error.'
	else:
		print 'Unexpected error.  See the details in \"' + logfile + '"'
	traceback.print_exc()
	exit(1)

# Done - exit with success status (no exception handled because script is done)
exit(0)
