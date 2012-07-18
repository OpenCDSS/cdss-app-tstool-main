#! /usr/bin/env python
#
# The above is included to run Python automatically on Linux.

"""
This script processes a NOAA MEI (Multivariate ENSO Index) file into a one-column CSV file,
which can be more easily processed.  An example of data is shown below, retrieved
from the URL:  http://www.esrl.noaa.gov/psd/data/correlation/mei.data
The delimited file can then be read into TSTool using the ReadDelimitedFile() command.

=================

1950 2012
1950   -1.022  -1.146  -1.289  -1.058   -1.42  -1.366  -1.342  -1.066  -0.576  -0.394  -1.154  -1.247
1951   -1.068  -1.196  -1.209  -0.437  -0.275   0.464   0.756   0.864   0.779   0.752   0.728   0.467
1952    0.406   0.133   0.088   0.262  -0.267  -0.638  -0.235  -0.157   0.362   0.311  -0.338  -0.125
...
2009   -0.753  -0.715  -0.713  -0.159    0.37   0.943   0.929   0.934   0.762   1.018   1.061   1.007
2010    1.152    1.52    1.39   0.863   0.577  -0.433  -1.217  -1.849  -2.037  -1.948  -1.606   -1.58
2011   -1.678   -1.56  -1.559  -1.492  -0.322  -0.169  -0.147  -0.503  -0.772  -0.968   -0.98  -0.979
2012   -1.046  -0.702   -0.41   0.059   0.706   0.903    -999    -999    -999    -999    -999    -999
 -999
 MEI timeseries
 Info at http://www.esrl.noaa.gov/psd/data/climateindices/Link/

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
	version = "1.00 (2012-07-16)"

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
		logger.info('Processing input MEI file "' + inputFile + '"' )
	outputFile = getCommandLineArgument ( 'outputFile' )
	if ( outputFile == None ):
		print "** No output file specified **"
		printUsage()
		exit(1)
	else:
		logger.info('Creating output CSV file "' + outputFile + '"' )
	convertMeiToCsv ( inputFile, outputFile, logger )
	return

def convertMeiToCsv ( inputFile, outputFile, logger ):
	"""
	Convert the MEI file to a CSV file.

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
	for line in f:
		if ( (len(line) == 0) or (line[0] == ' ') ):
			# Blank line or not a data line
			continue
		# Else a data line
		i = i + 1
		lineStripped = line.strip()
		lineParts = re.split(r'[ ]+',lineStripped)
		# Should always be year plus 12 values, although some may be missing (-999)
		if ( len(lineParts) != 13 ):
			continue
		for i in range(1,13):
			monthString = '%02d' % (i)
			dataStringList.append("" + lineParts[0] + '-' + monthString + ',' + lineParts[i])
	# Now output the delimited file, starting with a commented header
	f.close()
	# Sort the data strings
	dataStringList.sort()
	# Finally, write the data strings to the output file
	f = open(outputFile,"w")
	f.write("# NOAA Multivariate ENSO Index time series\n")
	f.write('"Date","MEI"\n' )
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
	print "Convert a NOAA MEI data file to a CSV file."
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
