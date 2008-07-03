#! /usr/bin/env python
#
# The above is included to run Python automatically on Linux.

"""
This script processes an NRCS SNOTEL "update" file into a single DateValue
time series file format.  A single day of values will be included.  The file
can then be read by TSTool commands to merge the one day into a longer period.
The filename MUST include the date, which is used to determine the date for data.
This script DOES NOT download the file.
A sample of the file is shown below:

United States           Natural Resources             Water and Climate Center
Department of           Conservation                          Portland, Oregon
Agriculture             Service                                               
 
          S N O W  -  P R E C I P I T A T I O N    U P D A T E
 
              Based on Mountain Data from NRCS SNOTEL Sites
                    As of THURSDAY: JUNE 26 , 2008 
-----------------------------------------------------------------------------
BASIN                    ELEV.  SNOW WATER EQUIVALENT   TOTAL PRECIPITATION
 Data Site Name          (Ft)                      %                      %  
                                Current  Average  Avg  Current  Average  Avg
-----------------------------------------------------------------------------

COLORADO


GUNNISON RIVER BASIN

 BUTTE                   10160      .1      .0           26.0    22.1   118 
 COCHETOPA PASS          10020      .2      -M      *    10.3      -M      *
 COLUMBINE PASS           9400      -M      .0      *    32.1    28.4   113 
 IDARADO                  9800      .2      .0           29.9    24.9   120 
 MC CLURE PASS            9500      -M      .0      *    30.1    28.5   106 
 MESA LAKES              10000      .5      .2   250     35.1    30.1   117 
 OVERLAND RES.            9840      .1      .0           24.9    22.2   112 
 PARK CONE                9600      .0      .0   100     20.7    17.7   117 
 PARK RESERVOIR           9960      .0     1.7     0     41.4    35.2   118 
 PORPHYRY CREEK          10760      .2      .3    67     22.3    21.4   104 
 RED MOUNTAIN PASS       11200      .0      .7     0     40.4    33.5   121 
 SCHOFIELD PASS          10700      .8     3.6    22     53.5    42.5   126 
 SLUMGULLION             11440      .0      .0   100     20.9    18.3   114 
                                                -----                  -----
                Basin wide percent of average     23                    116 


UPPER COLORADO RIVER BASIN
... etc ...
-----------------------------------------------------------------------------


 -M   = Missing data
  *   = Data may not provide a valid measure of conditions.
Units = inches for the Current and Average Snow Water Equivalent and 
	Total Precipitation values
 
If the Basin wide percent of average value is flagged as potentially
invalid, care should be taken to evaluate if the value is representative
of conditions in the basin.

The SNOW WATER EQUIVALENT Percent of Average represents the snow water
equivalent found at selected SNOTEL sites in or near the basin compared to
the average value for those sites on this day.

The TOTAL PRECIPITATION Percent of Average represents the total precipitation
(beginning October 1st) found at selected SNOTEL sites in or near the basin
compared to the average value for those sites on this day.

Contact your state water supply staff for assistance.

Reference period for average conditions is 1961-90.

"""

import datetime
import logging
import os
import subprocess
import sys
import tempfile
import traceback

# Log file is global in module because it is used to print a message
# at the end of this script     
logfile = "unknown"

# The main function follows, with all other functions being alphabetized after.

def main ():
	"""
	Main program to control the processing.
	"""
	# Script name as typed on the command line, for messages, without path
	scriptname = os.path.basename(sys.argv[0])

	# The script version, to allow tracking changes over time
	version = "1.00 (2008-06-26)"

	# The main location of the files.
	# Project at RTi
	snotelHomeDir = "K:/PROJECTS/1015_Bureau of Reclamation-Phase 3 SNODAS/SnowDataTools/SNOTEL"
	# FIXME SAM 2008-07-02 Home at CWCB to be determined
	#
	# The directory structure is then:
	#	${snotelHomeDir}\
	#		bydate\
	#			coYYMMDD.txt
	#			coYYMMDD.dv
	#		current\
	#			cosnotel.csv
	#		history\
	#			cosnotel.dv
	snotelArchiveDir = snotelHomeDir + "/alldays"
	snotelHistoryFile = snotelArchiveDir + "/cosnotel.dv"
	snotelByDateDir = snotelHomeDir + "/bydate"
	snotelCurrentDir = snotelHomeDir + "/current"
	snotelCurrentFile = snotelCurrentDir + "/cosnotel.csv"

	# The SNOTEL history period to maintain, to allow graphs of longterm data
	# Change this when new years are needed and the history file will be extended
	historyStart = datetime.datetime(1998,10,1)
	historyEnd = datetime.datetime(2008,9,30)

	# The number of days to process if dates are not provided
	daysToProcessDefault = 14

	# TSTool executable to use.  This should be set to a version that has been tested
	# out with this process.  Use the following for development on CYGWIN
	#tstool = "/cygdrive/c/CDSS/TSTool-08.15.03/bin/tstool.exe"
	# Use the following for Windows.  It will need to be set for the system.
	tstool = "C:/CDSS/TSTool-08.15.03/bin/tstool.exe"

	# Set the user to be used for log files (default to current login)
	user = getUser ()

	# The command file to be run by TSTool
	#tstoolCommandFile = tempfile.gettempdir() + "/snotel-" + user + "TSTool"
	tstoolCommandFile = snotelHomeDir + "/TSTool/snotel-" + user + ".TSTool"

	# Initialize logging to capture messages
	logger, log = initializeLogging ( user, scriptname )
	# Set the global variable for logfile
	logfile = log
	print ""
	print "Opened log file " + logfile

	# Check the command line parameters
	if ( not checkCommandLineParameters ( version, daysToProcessDefault ) ):
		printUsage(daysToProcessDefault)
		exit ( 1 )

	# By default FTP is not done
	doFTP = False

	# List of SNOTEL files to convert
	snotelFiles = []
	# Get the input and output files from the command line (argv[0] is script name)
	# Input file to processs
	snotelFiles = getSnotelFilesFromCommandLine ( snotelFiles )
	if ( len(snotelFiles) == 0 ):
		# A file to process has not been specified so retrieve files for the
		# specified dates and then process
		logger.info ( "No SNOTEL file was specified so download from FTP site using dates..." )
		# Default dates
		snotelStart = datetime.date.today()
		snotelEnd = snotelStart + datetime.timedelta(days=-(daysToProcessDefault - 1))
		# Reset to command line, if specified
		snotelStart, snotelEnd = getDatesFromCommandLine ( snotelStart, snotelEnd )
		# Get the SNOTEL files from the FTP site and return a list to be processed locally
		snotelFiles = ftpGetSnotelFiles ( snotelStart, snotelEnd )
		doFTP = True
	else:
		logger.info ( "Processing SNOTEL files specified on the command line..." )

	if ( len(snotelFiles) == 0 ):
		logger.error ( "Unable to determine list of SNOTEL files to process from command line." )
		printUsage(daysToProcessDefault)
		exit ( 1 )

	# Process the files.  Commands are dynamically added to a TSTool command file for
	# each SNOTEL file, and then TSTool is run once to process the files.
	logger.info ( "Temporary TSTool command file to run is \"" + tstoolCommandFile + "\"" )
	f = initializeTSToolCommandFile ( tstoolCommandFile, snotelHistoryFile, historyStart, historyEnd )
	for snotelFile in snotelFiles:
		print "Processing file \"" + snotelFile + "\"..."
		logger.info ( "Processing file \"" + snotelFile + "\"" )
		# Read the NRCS file into a dictionary
		dict, dataDate = convertNrcsSnotelUpdateFileToDict ( snotelFile )

		# Change the extension on the DateValue output file
		dvFile = snotelFile.replace ("txt", "dv")

		# Convert the dictionary to DateValue format
		convertDictToDateValueFile ( dict, dvFile, dataDate )

		# Append to the TSTool file the commands needed to process the file.
		appendToTSToolCommandFile ( f, dvFile )

	# Add ending commands to write out the history file
	finalizeTSToolCommandFile ( f, snotelHistoryFile )
	# Call TSTool to process into historical DateValue files.  Do this once so
	# that TSTool starts once and the log file will contain all processing.
	processDataUsingTSTool ( tstool, tstoolCommandFile )

	return

def appendToTSToolCommandFile ( f, dvFile ):
	"""
	Append TSTool commands to the command file to process the commands.

	f - open file to write to
	dvFile - DateValue file containing single day of data to process
	"""
	f.write ( "#\n" )
	f.write ( "##\n" )
	f.write ( "### Commands to process file \"" + dvFile + "\"\n" )
	f.write ( "###\n" )
	f.write ( "##\n" )
	f.write ( "#\n" )
	f.write ( "# Read the DateValue file to process\n" )
	f.write ( "ReadDateValue(InputFile=\"" + dvFile + "\")\n" )
	return

def appendToDateValueHeaderStrings( TSID, description, units, missingVal,
	dataFlags, dateLine, delim, locName, dataType ):
	"""
	Append to the DateValue header strings.
	This can be called with basin or station time series data.

	TSID - the TSID data for the DateValue file, from previous time series
	description - the description data for the DateValue file, from previous time series
	units - the units data for the DateValue file, from previous time series
	missingVal - the missing value data for the DateValue file, from previous time series
	dataFlags - the data flags for the DateValue file, from previous time series
	dateLine - the "Date" line at the end of the header, from previous time series
	delim - the delimiter to use
	locName - location (basin or station) name
	dataType - data type for time series

	Return a tuple having the header strings:
	TSID, description, units, missingVal, dataFlags, dateLine
	"""
	delim2 = delim
	if ( len(TSID) == 0 ):
		delim2 = ""
	TSID = TSID + delim2 + "\"" + locName + ".SNOTEL." + dataType + ".Day\""
	description = description + delim2 + "\"" + dataType + "\""
	if ( dataType.find("Percent") > 0 ):
		units = units + delim2 + "\"Percent\""
	else:
		units = units + delim2 + "\"IN\""
	missingVal = missingVal + delim2 + "-999"
	dataFlags = dataFlags + delim2 + "true"
	if ( len(dateLine) == 0 ):
		dateLine = "Date"
	dateLine = dateLine + delim + "\"" + locName + ", " + dataType + "\"" + delim + "DataFlag"
	return TSID, description, units, missingVal, dataFlags, dateLine

def checkCommandLineParameters ( version, daysToProcessDefault ):
	"""
	Verify that the specified command line parameters are recognized.
	Do not parse.  Just do simple checks and print version or usage if requested.

	version - the script version
	daysToProcessDefault - the number of days to process, inclusive of today if
		dates are not provided

	Return False if there were errors, True if everything checked out.
	"""
	numErrors = 0
	for arg in sys.argv[1:]:
		arg_lower = arg.lower()
		if ( arg_lower.startswith("end=") ):
			pass
		elif ( arg_lower == "-h" ):
			printUsage(daysToProcessDefault)
			exit(0)
		elif ( arg_lower.startswith("in=") ):
			pass
		elif ( arg_lower.startswith("start=") ):
			pass
		elif ( arg_lower == "-v" ):
			printVersion(version)
			exit(0)
		else:
			print ( 'Unrecognized command parameter "' + arg + '"' )
			logger = logging.getLogger()
			logger.warning ( 'Unrecognized command parameter "' + arg + '"' )
			numErrors = numErrors + 1
	if ( numErrors > 0 ):
		return False
	else:
		return True

def convertDictToDateValueFile ( dict, dvFile, dataDate ):
	"""
	Convert the dictionary of time series data values and flags to a
	DateValue file.

	dict - dictionary containing additional dictionaries with data values
	dvFile - DateValue file to write
	"""
	f = open ( dvFile, 'w' )
	delim = ','
	writeDateValueHeader ( f, dict, dataDate, delim )
	writeDateValueData ( f, dict, dataDate, delim )
	f.close()
	return

def convertNrcsSnotelUpdateFileToDict ( updateFile ):
	"""
	Read the file from the NRCS SNOTEL "update" format file to a dictionary.

	updateFile - NRCS SNOTEL file to convert

	Return a tuple of the dictionary and the date for the data (from the file header).
	The dictionary has the form:

		fileDict {
			key = "BasinName", oject = basinDict {
				key = "DataDict", object = dict {
					key = dataType, object = string flag or number value
					...
				}
				key = "StationName", object = dict {
					key = "DataDict", object = dict valueDict {
						key = dataType, object = string flag or number value
						...
					}
				}
			}
	"""
	# Get logger
	logger = logging.getLogger()
	# Dictionary for file (top of the data model)
	basinCount = 0
	stationCount = 0
	fileDict = dict()
	infp = open( updateFile)
	# Find the "COLORADO" line
	for line in infp:
		logger.info ( 'Reading line "' + line.rstrip() + '"' )
		if ( line.startswith("COLORADO") ):
			# Done with header
			break
		elif ( line.strip().startswith("As of") ):
			# Need to get the data date from string like:
			# As of MONDAY: OCTOBER 1 , 2007
			dateString = line[line.find(":") + 1:].strip()
			# Split into tokens, note the comma will be a token
			parts = dateString.split()
			monthString = parts[0]
			if ( monthString == "JANUARY" ):
				month = 1
			elif ( monthString == "FEBRUARY" ):
				month = 2
			elif ( monthString == "MARCH" ):
				month = 3
			elif ( monthString == "APRIL" ):
				month = 4
			elif ( monthString == "MAY" ):
				month = 5
			elif ( monthString == "JUNE" ):
				month = 6
			elif ( monthString == "JULY" ):
				month = 7
			elif ( monthString == "AUGUST" ):
				month = 8
			elif ( monthString == "SEPTEMBER" ):
				month = 9
			elif ( monthString == "OCTOBER" ):
				month = 10
			elif ( monthString == "NOVEMBER" ):
				month = 11
			elif ( monthString == "DECEMBER" ):
				month = 12
			else:
				logger.error('Unable to determine month number from "' + monthString + '"' )
				exit ( 1 )
			day = parts[1]
			year = parts[3]
			dataDate = datetime.date ( int(year), int(month), int(day) )

	# Now read until a dash is encountered, indicating the end of all data
	# Lines that don't start with a space are the basin name.
	# Lines that start with space and have the second character non-space are the station name.
	# Lines that start with spaces and "Basin wide" are the basin percent of average
	# Data values are managed using a dictionary, where the key is a data parameter name and
	# the object is the data value:
	#	SWE (number)
	#	SWEFlag (string)
	#	SWEAverage (number)
	#	SWEPercentOfAverage (number)
	#	SWEPercentOfAverageFlag (string)
	# Missing values are indicated by -999 and the character flags are taken from the report
	# (see above for explanation):
	#	M
	#	*
	#
	fileDict = dict()
	for line in infp:
		lineStripped = line.strip()
		logger.info ( 'Reading line "' + line.rstrip() + '"' )
		if ( line.startswith("-") ):
			# No more data.
			break
		elif ( (len(lineStripped) == 0) or lineStripped.startswith("-----  ") ):
			# Blank line or other non-data
			continue
		elif ( lineStripped.startswith("Basin wide") ):
			# Basin totals - end of this basin.
			# Get from the specific columns, possibly with data flags
			basinCount = basinCount + 1
			basinSWEPercentOfAverage, basinSWEPercentOfAverageFlag = parseDataValue ( line[47:53].strip() )
			# Add an entry for the basin
			basinDataDict = dict()
			basinDict["DataDict"] = basinDataDict
			basinDataDict["BasinSWEPercentOfAverage"] = basinSWEPercentOfAverage
			basinDataDict["BasinSWEPercentOfAverageFlag"] = basinSWEPercentOfAverageFlag
		elif ( line[0:1] != ' ' ):
			# Basin name - start of this basin.
			basinName = line.strip()
			logger.info('Initializing dictionary for basin "' + basinName + '"' )
			basinDict = dict()
			# Create a new station dictionary for the basin to hole station data
			stationsForBasinDict = dict()
			basinDict["StationsForBasinDict"] = stationsForBasinDict
			fileDict[basinName] = basinDict
		elif ( (line[0:1] == ' ') and (line[1:2] != ' ') ):
			# Station name
			stationName = line[1:24].strip()
			# Remove any periods in the name because this interferes with time series identifiers
			if ( stationName.find(".") >= 0 ):
				logger.warn ( "Removing period from station name: \"" + stationName + "\"" )
				stationName = stationName.replace(".","")
			stationCount = stationCount + 1
			stationSWE, stationSWEFlag = parseDataValue ( line[32:38].strip() )
			stationSWEAverage, stationSWEAverageFlag = parseDataValue ( line[39:46].strip() )
			stationSWEPercentOfAverage, stationSWEPercentOfAverageFlag = parseDataValue ( line[47:53].strip() )
			# Add entries for the station
			valueDict = dict()
			stationsForBasinDict[stationName] = valueDict
			valueDict["StationSWE"] = stationSWE
			valueDict["StationSWEFlag"] = stationSWEFlag
			valueDict["StationSWEAverage"] = stationSWEAverage
			valueDict["StationSWEAverageFlag"] = stationSWEAverageFlag
			valueDict["StationSWEPercentOfAverage"] = stationSWEPercentOfAverage
			valueDict["StationSWEPercentOfAverageFlag"] = stationSWEPercentOfAverageFlag
	infp.close()
	logger.info ( "Read " + str(basinCount) + " basins, with a total of " + str(stationCount) + " stations" )
	logger.info ( "The number of time series is " + str(basinCount) + "+3*" + str(stationCount) + "=" +
		str(basinCount + 3*stationCount) )
	return fileDict, dataDate

def finalizeTSToolCommandFile ( f, historyFile ):
	"""
	Finalize the TSTool command file by adding commands to rewrite the
	history DateValue file.  Also close the command file.

	f - open command file
	"""
	f.write ( "WriteDateValue(OutputFile=\"" + historyFile + "\")" )
	f.close()
	return

def ftpGet ( server, login, password, remoteDirectory, remoteFilename, localFilename, transferMode ):
	"""
	Retrieve a single file from the remote server.
	"""
	ftp = ftplib.FTP(server)
	ftp.login ( login, password )
	ftp.cwc ( remoteDirectory )
	if ( transferMode == "ASCII" ):
		f = open(localFilename,'w')
		ftp.retrlines('RETR ' + remoteFilename, f.write)
		f.close()
	else:
		f = open(localFilename,'wb')
		ftp.retrbinary('RETR ' + remoteFilename, f.write)
		f.close()
	ftp.close()
	return

def ftpGetSnotelFile ( snotelDate ):
	"""
	Download a single Snotel file from the NRCS FTP site.
	"""
	# Need to get the file from the NRCS site
	ftpGet ( server, login, password, remoteDirectory, remoteFilename, localFilename, transferMode )
	return

def ftpGetSnotelFiles ( snotelStart, snotelEnd ):
	"""
	Download a series of files from the NRCS web site for the specified dates.
	Only one FTP connection is made to increase performance and potential problems
	with the FTP site rejecting multiple connections.
	"""
	logger = logging.getLogger()
	logger.info ( "Retrieving SNOTEL update files from NRCS site for period " +
		str(snotelStart) + " to " + str(snotelEnd) )
	# Define the NRCS server information
	server = "ftp.wcc.nrcs.usda.gov"
	login = "anonymous"
	password = "anonymous"
	remoteDirectory = "/data/snow/update/co"
	testing = True
	if ( testing == True ):
		server = "ftp.riverside.com"
		password = "ok2ftp2"
	snotelFiles = []
	return snotelFiles

def ftpOpenConnection ( server, login, password, remoteDirectory ):
	"""
	Open a connection on the remote server.
	"""
	ftp = ftplib.FTP(server)
	ftp.login ( login, password )
	ftp.cwc ( remoteDirectory )
	return

def getDatesFromCommandLine ( snotelStart, snotelEnd ):
	"""
	Get the SNOTEL dates to process from the command line.

	snotelStart - default starting date if not found in the command line.
	snotelEnd - default ending date if not found in the command line.
	"""
	for arg in sys.argv:
		if ( arg.lower().startswith("in=") ):
			snotelFile = arg[3:]
			break
	return snotelStart, snotelEnd

def getSnotelFilesFromCommandLine ( snotelFiles ):
	"""
	Get the SNOTEL files to process from the command line.

	snotelFiles - default value if not found in the command line.
	"""
	for arg in sys.argv:
		# Allow more thanone file
		if ( arg.lower().startswith("in=") ):
			snotelFiles.append ( arg[3:] )
	return snotelFiles

def getUser ():
	"""
	Return the current user, taken from the LOGNAME environment variable.
	The script will exit if this information is not available.
	"""
	user = None
	if ( os.name == "posix" ):
		# Linux
		user = os.getenv ( "LOGNAME" )
		if ( user == None ):
			user = os.getenv ( "USER" )
	elif ( os.name == 'nt' ):
		# Windows
		user = os.environ["USERNAME"]
	if ( user == None ):
	        print 'Environment variable LOGNAME is not defined.  Cannot determine user.'
	        exit ( 1 )
	return user

def initializeLogging ( user, appname ):
	"""
	Initialize logging for the script.
	Currently a log file is created in /tmp with the user name and date.

	user - user running the software
	appname - the application (script) name, to use in the log file name

	Return a tuple with the logger and the log file for this application (script).
	"""
	# TODO SAM Discuss with IPC naming convention and location of log
	today = datetime.date.today()
	tmp = tempfile.gettempdir()
	if ( (user == None) or (user.strip() == "") ):
		# No user name
		logfile = tmp + "/" + appname + "." + today.strftime("%Y%m%d") + ".txt"
	else:
		# Have user name
		logfile = tmp + "/" + appname + "." + user + "." + today.strftime("%Y%m%d") + ".txt"
	is_python24 = False
	version = str(sys.version_info[0]) + "." + str(sys.version_info[1])
	if ( float(version) >= 2.4 ):
		is_python24 = True
	if ( is_python24 == True ):
		# Use the newer function to configure logging
		logging.basicConfig ( format = '%(asctime)s %(levelname)8s %(message)s',
			filename=logfile, filemode = 'w' )
		logger = logging.getLogger()
	else:
		# Use the following for Python older than 2.4
		# For now use the root logger
		logger = logging.getLogger ()
		#logger = logging.getLogger(appname)
		# Open a file with mode ? so as to NOT append
		hdlr = logging.FileHandler(logfile,'w')
		formatter = logging.Formatter('%(asctime)s %(levelname)s %(message)s')
		hdlr.setFormatter(formatter)
		logger.addHandler(hdlr)
	# For now log everything above info but may need to see what low level code puts out
	logger.setLevel(logging.INFO)
	# TODO SAM 2008-03-03 Evaluate putting in more header information similar to Java ioutil.printCreatorHeader()
	logger.info ( "Running as user:  " + user )
	return logger, logfile

def initializeTSToolCommandFile ( tstoolCommandFile, historyFile, historyStart, historyEnd ):
	"""
	Initialize the TSTool command file.

	tstoolCommandFile - name of the TSTool command file
	historyFile - SNOTEL history file
	historyStart - the starting datetime of the SNOTEL history file
	historyEnd - the ending datetime of the SNOTEL history file

	Return an open file object.
	"""
	f = open ( tstoolCommandFile, "w" )
	f.write ( "StartLog(Logfile=\"" + tstoolCommandFile + ".log\"\n" )
	f.write ( "# Read the archive file containing the historical period.\n" )
	f.write ( "ReadDateValue(InputFile=\"" + historyFile + "\")\n" )
	f.write ( "# Ensure that the historical period is as desired.\n" )
	f.write ( "ChangePeriod(TSList=AllTS,NewStart=\"" +
		historyStart.strftime("%Y-%m-%d") + "\",NewEnd=\"" +
		historyEnd.strftime("%Y-%m-%d") + "\")\n" )
	return f

def processDataUsingTSTool ( tstool, tstoolCommandFile ):
	"""
	Merge the individual DateValue time series files for a date into the full
	period file.

	tstool - path to TSTool program
	tstoolCommandFile - command file to run
	"""
	syscall = tstool + " -commands \"" + tstoolCommandFile + "\""
	excludePatterns = None
	if ( runProgram( syscall,runProgramLogger,excludePatterns ) != 0 ):             
		logger = logging.getLogger()
		logger.error ( "Error running TSTool with: " + syscall )             
		exit ( 1 )

	return

def parseDataValue ( dataString ):
	"""
	Parse a data string and return a tuple of data value and flag.

	dataString - the string to parse, with data value and/or flag (number, -M, or Number*)

	Return the data vale, flag (flag can be "").
	"""
	missingValue = -999.0
	nrcsMissing = "-M"
	nrcsMissingShort = "M"
	nrcsUncertain = "*"
	dataString = dataString.strip()
	if ( dataString.endswith(nrcsUncertain) ):
		# Seems to be basin percent average only?
		# The value will be blank or can be a number
		flag = nrcsUncertain
		if ( len(dataString) == 1 ):
			# No number
			value = missingValue
		else:
			value = dataString[:len(dataString) - 1]
	else:
		# The value can be a number or a flag
		if ( dataString == "" ):
			value = missingValue
			flag = ""
		elif ( dataString == nrcsMissing ):
			value = missingValue
			# Don't use the dash for the flag
			flag = nrcsMissingShort
		elif ( dataString == nrcsUncertain ):
			value = missingValue
			flag = nrcsUncertain
		else:
			value = dataString
			flag = ""
	logger = logging.getLogger()
	logger.info("parsing \"" + dataString + "\" gives value \"" +
		str(value) + "\" flag \"" + flag + "\"" )
	return value, flag

#-------------------------------------------------------------------------------
def printUsage(daysToProcessDefault):
	"""
	Print the script usage.
	"""
	print ""
	print "Optionally download, convert an NRCS SNOTEL update file to a DateValue file,"
	print "and merge the file into the archive DateValue file."
	print ""
	print os.path.basename(sys.argv[0]) + " in=updateFile OR start=YYYYMMDD [end=YYYYMMDD]"
	print ""
	print "If a filename is specified, it is converted."
	print "If start and end dates are specified, files are FTPed and then converted."
	print "If end date is specified, start is defaulted and files are FTPed and then converted."
	print ""
	print "in=updateFile - NRCS SNOTEL update file name (input)"
	print "  DateValue file will have same name with .dv extension"
	print "start=YYYYMMDD - starting date to process (default=today - " + \
		str(daysToProcessDefault - 1) + " days)"
	print "end=YYYYMMDD - starting date to process (default=today)"
	print ""
	return

#-------------------------------------------------------------------------------
def runProgram ( cmd, outputHandler, excludePatterns ):
	"""
	Run a program and capture its standard error and output.

	cmd - the command to run
	outputHandler - a function to call to handle the output
	excludePatterns - a list of regular expression patterns to exclude, for
		example to avoid echoing all the SHEF data records to the
		main log file

	Return the exit status of the program being run as an integer.
	"""
	# Run the command redirecting standard error to standard output to the pipe
	o = os.popen(cmd + " 2>&1")
	# Open the pipe
	fd = o.fileno()
	# Log file that is indicated in program terminal output
	logger = logging.getLogger()
	programLogfile = None
	while 1:
		# Read 100 bytes of output at a time - problem, line breaks are not
		# regular so logging does not look good
		#s = os.read(fd,100)
		# Read a line of output.  This may block but since we are not doing
		# any thread interrupts that is OK.
		s = o.readline()
		if len(s) == 0:
			# No more to read
			break
		# Log the output line
		if ( outputHandler != None ):
			# Pass the output to a handler, removing trailing newlines
			outputHandler(s.rstrip())
	exitCode = o.close()
	if ( exitCode == None ):
		exitCode = 0
	logger.info("Exit code from program is " + str(exitCode) )
	return exitCode

#-------------------------------------------------------------------------------
def runProgramLogger ( s ):
	"""
	Log the standard output and error from running a program.

	s - a line of output from running the program
	"""
	logger = logging.getLogger()
	logger.info ( "term: " + s )
	return

#-------------------------------------------------------------------------------
def runProgramStdoutLogger ( s ):
	"""
	Log the standard output from running a program.

	s - a line of output from running the program
	"""
	logger = logging.getLogger()
	logger.info ( "stdout: " + s )
	return

#-------------------------------------------------------------------------------
def writeDateValueData ( f, dict, dataDate, delim ):
	"""
	Write the data for the DateValue file.

	f - file descriptor
	dict - dictionary of time series data
	dataDate - the date associated with the SNOTEL data file
	delim - the delimiter to use between data values
	"""
	logger = logging.getLogger()
	# Write the date (no delimiter)
	f.write ( dataDate.strftime("%Y-%m-%d") )
	# Iterate through the basins in the main dictionary
	basinNames = dict.keys()
	basinNames.sort()
	for basinName in basinNames:
		# Iterate through the stations in the basin
		logger.info ( 'Writing data for basin "' + basinName + '"' )
		# Get the dictionary that contains the list of stations
		basinDict = dict[basinName]
		stationsForBasinDict = basinDict["StationsForBasinDict"]
		# Data values for the basin
		basinDataDict = basinDict["DataDict"]
		# Time series for the basin
		for dataType in basinDataDict.keys():
			if ( dataType.find("Flag") >= 0 ):
				# Process the data value and look up the correcponding flag below
				continue
			# Write the value
			f.write ( delim + str(basinDataDict[dataType]) )
			# Write the flag
			f.write ( delim + "\"" + basinDataDict[dataType + "Flag"] + "\"" )
		# Stations in the basin, each with data values.
		stationNames = stationsForBasinDict.keys()
		stationNames.sort()
		for stationName in stationNames:
			logger.info ( 'Writing data for station "' + stationName +
				'" in basin "' + basinName + '"' )
			stationDataDict = stationsForBasinDict[stationName]
			# Time series for the station
			for dataType in stationDataDict.keys():
				if ( dataType.find("Flag") >= 0 ):
					# Process the data value and look up the correcponding flag below
					continue
				# Write the value
				f.write ( delim + str(stationDataDict[dataType]) )
				# Write the flag
				f.write ( delim + "\"" + stationDataDict[dataType + "Flag"] + "\"" )
	# print a newline to finish the line after all basin and station time series
	f.write("\n")
	return

#-------------------------------------------------------------------------------
def writeDateValueHeader ( f, dict, dataDate, delim ):
	"""
	Write the header for the DateValue file.

	f - file descriptor
	dict - dictionary of time series data
	dataDate - the date associated with the SNOTEL data file
	delim - the delimiter to use between data values
	"""
	logger = logging.getLogger()
	# Loop through the basins and then the stations for each basin, appending
	# to the strings in the header, which are initialized here...
	numTS = 0
	TSID = ""
	description = ""
	units = ""
	missingVal = ""
	dataFlags = ""
	dateLine = ""
	# Iterate through the basins in the main dictionary
	basinNames = dict.keys()
	basinNames.sort()
	logger.info("There are " + str(len(basinNames)) + " basins to process.")
	for basinName in basinNames:
		# Iterate through the stations in the basin
		logger.info ( 'Processing header for basin "' + basinName + '"' )
		# Get the dictionary that contains the list of stations
		basinDict = dict[basinName]
		stationsForBasinDict = basinDict["StationsForBasinDict"]
		# Data values for the basin
		basinDataDict = basinDict["DataDict"]
		# Time series for the basin
		for dataType in basinDataDict.keys():
			if ( dataType.find("Flag") >= 0 ):
				# Skip the flag since it does not figure into the
				# count
				continue
			numTS = numTS + 1
			TSID, description, units, missingVal, dataFlags, dateLine = \
				appendToDateValueHeaderStrings(TSID, description, units, missingVal, dataFlags, dateLine,
				delim, basinName, dataType )
		# Stations in the basin, each with data values.
		stationNames = stationsForBasinDict.keys()
		stationNames.sort()
		i = 0
		for stationName in stationNames:
			i = i + 1
			logger.info ( 'Processing station "' + stationName +
				'" in basin "' + basinName + '" (' + str(i) + " of " + str(len(stationNames)) + ")" )
			stationDataDict = stationsForBasinDict[stationName]
			# Time series for the station
			for dataType in stationDataDict.keys():
				if ( dataType.find("Flag") >= 0 ):
					# Skip the flag since it does not figure into the count
					continue
				numTS = numTS + 1
				TSID, description, units, missingVal, dataFlags, dateLine = \
				appendToDateValueHeaderStrings(TSID, description, units, missingVal, dataFlags, dateLine,
				delim, stationName, dataType )
	# Now write the header
	delim = ","
	f.write ( "# DateValueTS 1.4 file\n" )
	# Only one day of data in the file
	f.write ( "Delimiter = \"" + delim + "\"\n" )
	f.write ( "NumTS       = " + str(numTS) + "\n" )
	# Write the lists
	f.write ( "TSID        = " + TSID + "\n" )
	f.write ( "Description = " + description + "\n" )
	f.write ( "DataFlags   = " + dataFlags + "\n" )
	f.write ( "Units       = " + units + "\n" )
	f.write ( "MissingVal  = " + missingVal + "\n" )
	f.write ( "Start       = " + dataDate.strftime("%Y-%m-%d") + "\n" )
	f.write ( "End         = " + dataDate.strftime("%Y-%m-%d") + "\n" )
	f.write ( dateLine + "\n" )
	return

#-------------------------------------------------------------------------------
# Call the main function, which does all the work, and handle major unexpected
# errors.  Put the following at the end of the script because functions need to
# be defined before they can be called.

try:
	# Run the main program if this is a main script
	if __name__ == "__main__":
		main ()

except SystemExit, error:
	# This exception is raised when system.exit() is called.
	# Just let normal exit happen to shut down this script.
	#print 'Exiting...' + str(error)
	exit(int(str(error)))

except:
	# Using the logger here assumes that basic logging setup was successful
	# in the main function.
	logger = logging.getLogger()
	logger.exception ( "Unexpected exception" )
	print 'Unexpected error.  See the details in \"' + logfile + '"'
	traceback.print_exc()
	exit(1)

# Done - exit with success status (no exception handled because script is done)
exit(0)
