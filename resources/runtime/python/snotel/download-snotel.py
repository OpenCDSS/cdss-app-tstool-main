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
import ftplib
import glob
import logging
import os
import re
import shutil
import subprocess
import sys
import tempfile
import traceback

import TSlite

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

	# Set the user to be used for log files (default to current login)
	user = getUser ()

	# The script version, to allow tracking changes over time
	version = "1.05 (2008-07-24)"

	# The main location of the files.
	snotelHomeDirDefault = "C:/CDSS/snotel"
	# Check for override on the command line
	snotelHomeDir = getSnotelHomeDirFromCommandLine ( snotelHomeDirDefault )
	print "Home for SNOTEL data files: \"" + snotelHomeDir + "\""
	if ( snotelHomeDir == "" ):
		# Most likely a problem in the calling batch file.
		print 'Home folder for SNOTEL data is blank - check the download-snotel.bat file.'
		exit ( 1 )
	if ( not os.path.exists(snotelHomeDir) ):
		print 'Creating folder for SNOTEL data "' + snotelHomeDir + '"'
		# logger.info('Creating folder for SNOTEL data "' + snotelHomeDir + '"' )
		os.makedirs ( snotelHomeDir )

	# Initialize logging to capture messages
	logger, theLogFile = initializeLogging ( user, scriptname, snotelHomeDir )
	# Set the global module variable for logfile
	logfile = theLogFile
	print ""
	print "Opened log file " + logfile
	logger.info ( "Home for SNOTEL data files: \"" + snotelHomeDir + "\"" )

	# The directory structure is then:
	#	${snotelHomeDir}\
	#		bydate\
	#			coYYMMDD.txt
	#			coYYMMDD.dv
	#		current\
	#			cosnotel.csv
	#		history\
	#			StationName.dv
	#		logs\
	#			download-snotel.sam.YYYYMMDD.log
	snotelHistoryDir = snotelHomeDir + "/history"
	if ( not os.path.exists(snotelHistoryDir) ):
		logger.info('Creating folder for SNOTEL history "' +
			snotelHistoryDir + '"' )
		os.makedirs ( snotelHistoryDir )
	logger.info ( "SNOTEL history folder: \"" + snotelHistoryDir + "\"" )

	snotelByDateDir = snotelHomeDir + "/bydate"
	logger.info ( "SNOTEL data by date folder: \"" + snotelByDateDir + "\"" )
	if ( not os.path.exists(snotelByDateDir) ):
		logger.info('Creating folder for SNOTEL by date files "' +
			snotelByDateDir + '"' )
		os.makedirs ( snotelByDateDir )

	snotelCurrentDir = snotelHomeDir + "/current"
	if ( not os.path.exists(snotelCurrentDir) ):
		logger.info('Creating folder for SNOTEL current data "' +
			snotelCurrentDir + '"' )
		os.makedirs ( snotelCurrentDir )
	snotelCurrentFile = snotelCurrentDir + "/cosnotel.csv"
	logger.info ( "SNOTEL current file: \"" + snotelCurrentFile + "\"" )

	snotelTstoolDir = snotelHomeDir + "/tstool"
	if ( not os.path.exists(snotelTstoolDir) ):
		logger.info('Creating folder for SNOTEL tstool files "' +
			snotelTstoolDir + '"' )
		os.makedirs ( snotelTstoolDir )

	# The SNOTEL history period to maintain, to allow graphs of longterm data
	# Change this when new years are needed and the history file will be extended
	# The data at the following address start in 1997-11-06:
	#  ftp://ftp.wcc.nrcs.usda.gov/data/snow/update/co
	doHistoryDefault = False
	doHistory = getDoHistoryFromCommandLine ( doHistoryDefault )
	historyStart = datetime.datetime(1997,11,1)
	historyEnd = datetime.datetime(2008,9,30)

	# The number of days to process if dates are not provided - will be inclusive period
	daysToProcessDefault = 14

	# TSTool executable to use.  This should be set to a version that has been tested
	# out with this process.
	tstool = "C:/CDSS/TSTool-08.16.02/bin/tstool.exe"
	if ( not os.path.exists(snotelHomeDir) ):
		logger.error ( "The TSTool program to use does not exist: \"" + tstool + "\"" )
		logger.error ( "Verify the configuration in " + sys.argv[0] )
		exit ( 1 )

	# The command file to be run by TSTool
	tstoolCommandFile = snotelTstoolDir + "/snotel-" + user + ".TSTool"

	# Check the command line parameters
	if ( not checkCommandLineParameters ( version, daysToProcessDefault, snotelHomeDir, doHistoryDefault ) ):
		printUsage(daysToProcessDefault, snotelHomeDirDefault)
		exit ( 1 )

	# By default FTP is not done

	# List of SNOTEL files to convert
	snotelFiles = []
	# Get the input and output files from the command line (argv[0] is script name)
	# Input file to processs
	snotelFiles = getSnotelFilesFromCommandLine ( snotelFiles, snotelByDateDir )
	if ( len(snotelFiles) == 0 ):
		# A file to process has not been specified so retrieve files for the
		# specified dates and then process
		logger.info ( "No SNOTEL file was specified so download from FTP site using dates..." )
		# Default dates
		snotelEnd = datetime.date.today()
		snotelStart = snotelEnd - datetime.timedelta(days=(daysToProcessDefault - 1))
		logger.info("Default dates to process: " + str(snotelStart) + " to " + str(snotelEnd) )
		# Reset to command line, if specified
		snotelStart, snotelEnd = getDatesFromCommandLine ( snotelStart, snotelEnd )
		logger.info("Processing dates: " + str(snotelStart) + " to " + str(snotelEnd) )
		# Get the SNOTEL files from the FTP site and return a list to be processed locally
		snotelFiles = ftpGetSnotelFiles ( snotelStart, snotelEnd, snotelByDateDir )
	else:
		logger.info ( "Processing SNOTEL files specified on the command line..." )

	if ( len(snotelFiles) == 0 ):
		logger.error ( "Unable to determine list of SNOTEL files to process from command line." )
		printUsage(daysToProcessDefault, snotelHomeDirDefault)
		exit ( 1 )

	if ( doHistory == True ):
		# Commands are dynamically added to a TSTool command file for
		# each SNOTEL file, and then TSTool is run once to process the files.
		logger.info ( "Temporary TSTool command file to run is \"" + tstoolCommandFile + "\"" )
		# Initialize the command file
		f, numHistoryTS = initializeTSToolCommandFile ( tstoolCommandFile, snotelHistoryDir,
			historyStart, historyEnd )
		# List of time series identifiers with history information
		tsidsHistory = []
	for snotelFile in snotelFiles:
		print "Processing file \"" + snotelFile + "\"..."
		logger.info ( "Processing file \"" + snotelFile + "\"" )
		# Read the NRCS file into a list of time series
		tslist, tslocList, tslocTypeList, dataDate = readNrcsSnotelUpdateFile ( snotelFile )

		# Change the extension on the DateValue output file
		dvFile = snotelFile.replace ("txt", "dv")

		# Write the list of time series objects to DateValue format
		writeSnotelDataToDateValueFile ( tslist, dvFile, dataDate )

		# Write the data to the CSV file used to link with the GIS
		csvFile = snotelFile.replace ("txt", "csv")
		writeSnotelDataToCsvFile ( tslist, tslocList, tslocTypeList, csvFile, dataDate )
		# If the last file being processed, copy to the current file
		if ( snotelFile == snotelFiles[len(snotelFiles) - 1] ):
			message = "Copying the last CSV file created (\"" + csvFile + \
				"\") to current (\"" + snotelCurrentFile + "\")."
			print message
			logger.info ( message )
			dataDateString = "%04d%02d%02d" % (dataDate.year, dataDate.month, dataDate.day)
			# Get the date from the filename, no extension, no leading "co"
			snotelFileDate = re.split("[.]",os.path.basename(snotelFile))[0][2:]
			fileyear = snotelFileDate[0:2]
			# Assume data since 1970 are listed as 70+ as first 2 digits
			if ( int(fileyear) < 70 ):
				snotelFileDate = "20" + snotelFileDate
			else:
				snotelFileDate = "19" + snotelFileDate
			if ( snotelFileDate != dataDateString ):
				# Print a warning about provisional data
				message = "Filename date is: " + snotelFileDate
				print message
				logger.info ( message )
				message = "Date in file is:  " + dataDateString
				print message
				logger.info ( message )
				message = "Data in the file are provisional and may change later."
				print message
				logger.info ( message )
			shutil.copy ( csvFile, snotelCurrentFile )

		if ( doHistory == True ):
			# Append to the TSTool file the commands needed to process the file.
			appendToTSToolCommandFile ( f, numHistoryTS, dvFile, tslist, tsidsHistory )

	if ( doHistory == True ):
		# Add ending commands to write out the history file
		finalizeTSToolCommandFile ( f, tslist, snotelHistoryDir )

		# Call TSTool to process into historical DateValue files.  Do this once so
		# that TSTool starts once and the log file will contain all processing.
		print "Merging SNOTEL data from files with history using TSTool..."
		logger.info( "Merging SNOTEL data from files with history using TSTool...")
		processDataUsingTSTool ( tstool, tstoolCommandFile )
	return

def appendToTSToolCommandFile ( f, numHistoryTS, dvFile, tslist, tsidsHistory ):
	"""
	Append TSTool commands to the command file to process the time series.
	The history DateValue files will already have been read.  The identifiers
	in both time series files will be the same so it is important to use the
	proper TSList parameter value to match the first and last time series.

	f - open file to write to
	numHistoryTS - number of total time series in the history (read from
		multiple DateValue files)
	dvFile - DateValue file containing single day of data to process
	tslist - the list of time series to process for a specific date
	tsidsHistory - the list of time series with histories (files or in memory)

	return the updated list of time series identifiers with histories (new items may
		be added if initialization of the histories is occurring)
	"""
	f.write ( "#\n" )
	f.write ( "##\n" )
	f.write ( "### Commands to process file \"" + dvFile + "\"\n" )
	f.write ( "##\n" )
	f.write ( "#\n" )
	f.write ( "# Read the DateValue file to process, containing all time series for a date.\n" )
	f.write ( "ReadDateValue(InputFile=\"" + dvFile + "\")\n" )
	# Loop through all of the time series in the file and set in the history
	# Only include the command if the station in the TS matches a history file
	# (otherwise this is the first time and just need to write out the results).
	for ts in tslist:
		tsid = ts.getTSID()
		# Determine if the time series has a history time series, either read
		# from the history files or initialized in memory from another file
		historyFound = False
		for tsidHistory in tsidsHistory:
			if ( tsid == tsidHistory ):
				historyFound = True
				break
		if ( historyFound == True ):
			f.write ( "# Set the individual DateValue file contents on the history\n" )
			f.write ( "SetFromTS(TSList=FirstMatchingTSID,TSID=\"" + tsid +
				"\",IndependentTSList=LastMatchingTSID,IndependentTSID=\"" + tsid +
				"\",TransferHow=ByDateTime)\n" )
			f.write ( "# Now free the data for a date so that a file for another date can be processed.\n" )
			f.write ( "# This is necessary because the time series for each date have the same identifiers.\n" )
			f.write ( "Free(TSList=LastMatchingTSID,TSID=\"" + tsid + "\")\n" )
		else:
			# Allow the time series to exist in memory to be added to by other data files
			# Don't free because there was no history file to begin with
			f.write ( "# Time series \"" + tsid +
				"\" history will be initialized from the above file (first occurance in data files).\n" )
			tsidsHistory.append ( tsid )
	return tsidsHistory

def appendToDateValueHeaderStrings( TSIDAll, TSID, descriptionAll, description,
	unitsAll, units, missingValAll, missingVal, dataFlagsAll, dataFlags,
	dateLineAll, delim ):
	"""
	Append individual time series information to the cumulative DateValue header strings.

	TSID - the TSID data for the DateValue file, from previous time series
	description - the description data for the DateValue file, from previous time series
	units - the units data for the DateValue file, from previous time series
	missingVal - the missing value data for the DateValue file, from previous time series
	dataFlags - the data flags for the DateValue file, from previous time series
	dateLine - the "Date" line at the end of the header, from previous time series
	delim - the delimiter to use

	Return a tuple having the updated header strings:
	TSID, description, units, missingVal, dataFlags, dateLine
	"""
	delim2 = delim
	if ( len(TSIDAll) == 0 ):
		# Don't need a delimiter yet
		delim2 = ""
	TSIDAll = TSIDAll + delim2 + "\"" + TSID + "\""
	descriptionAll = descriptionAll + delim2 + "\"" + description + "\""
	unitsAll = unitsAll + delim2 + "\"" + units + "\""
	missingValAll = missingValAll + delim2 + str(missingVal)
	dataFlagsAll = dataFlagsAll + delim2 + "\"" + str(dataFlags) + "\""
	if ( len(dateLineAll) == 0 ):
		dateLineAll = "Date"
	locName = re.split("[.]",TSID)[0]
	dataType = re.split("[.]",TSID)[2]
	if ( dataFlags == True ):
		dateLineAll = dateLineAll + delim + "\"" + locName + ", " + dataType + "\"" + delim + "DataFlag"
	else:
		dateLineAll = dateLineAll + delim + "\"" + locName + ", " + dataType + "\""
	return TSIDAll, descriptionAll, unitsAll, missingValAll, dataFlagsAll, dateLineAll

def checkCommandLineParameters ( version, daysToProcessDefault, snotelHomeDirDefault, doHistoryDefault ):
	"""
	Verify that the specified command line parameters are recognized.
	Do not parse.  Just do simple checks and print version or usage if requested.

	version - the script version
	daysToProcessDefault - the number of days to process, inclusive of today if
		dates are not provided
	snotelHomeDirDefault - the home folder for SNOTEL data files
	doHistoryDefault - default doHistory value if not found in the command line.

	Return False if there were errors, True if everything checked out.
	"""
	numErrors = 0
	for arg in sys.argv[1:]:
		arg_lower = arg.lower()
		if ( arg_lower.startswith("end=") ):
			pass
		elif ( arg_lower == "-h" ):
			printUsage(daysToProcessDefault, snotelHomeDirDefault, doHistoryDefault)
			exit(0)
		elif ( arg_lower.startswith("history=") ):
			pass
		elif ( arg_lower.startswith("in=") ):
			pass
		elif ( arg_lower.startswith("snotelhome=") ):
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


def exit ( exitCode ):
	"""
	Exit the script.  This will throw a SystemExit exception that needs to
	be handled.

	exitCode - exit status integer to pass to the parent program, generally
		0 for success, 1 for failure
	"""
	logger = logging.getLogger()
	if ( exitCode != 0 ):
		print "Exiting with status " + str(exitCode) + \
		".  See the log file for explanation."
		logger.error ( "Exiting with status " + str(exitCode) )
	else:
		logger.info ( "Exiting with status " + str(exitCode) )
	sys.exit ( exitCode )
	return

def finalizeTSToolCommandFile ( f, tslist, historyDir ):
	"""
	Finalize the TSTool command file by adding commands to rewrite the
	history DateValue file.  Also close the command file.  Only history files for
	time series being processed need to be written (otherwise no change to history).

	f - open command file
	tslist - list of time series to write
	historyDir - directory where SNOTEL history files exist
	"""
	f.write ( "# Re-write the history files with the merged data.\n" )
	historyFilePrev = ""
	# Time series at the same location are written to the same file
	for ts in tslist:
		historyFile = historyDir + "/" + ts.getLocation() + ".dv"
		if ( historyFile != historyFilePrev ):
			f.write ( "WriteDateValue(OutputFile=\"" + historyFile +
			"\",TSList=AllMatchingTSID,TSID=\"" + ts.getLocation() + "*\",Precision=1,Delimiter=",")\n" )
		historyFilePrev = historyFile
	f.close()
	return

def ftpFileExists ( ftp, remoteFilenameRequested, cacheDict ):
	"""
	Determine whether a file exists on a remote FTP site.

	ftp - open FTP connection
	remoteFilenameRequested - name of the remote filename to check
	cacheDict - if not None, use as a dictionary to cache remote directory listing,
		to improve performance (only specify if remote directory contents are
		not expected to change frequently)

	Return True if the file exists, False if not.
	"""
	pwd = ftp.pwd()
	remoteFilenamesCached = None
	if ( cacheDict != None ):
		# See if a dictionary item is available (a list for the directory)
		try:
			remoteFilenamesCached = cacheDict[pwd]
			remoteFilenames = remoteFilenamesCached
		except KeyError, error:
			remoteFilenamesCached = None
	if ( remoteFilenamesCached == None ):
		# Need to get the list
		remoteFilenames = ftp.nlst()
		cacheDict[pwd] = remoteFilenames
	logger = logging.getLogger()
	#logger.info ( "Remote filename (requested) is \"" + remoteFilenameRequested +
	#	"\" in dir \"" + pwd + "\"" )
	for remoteFilename in remoteFilenames:
		# Check to see if the remote filename matches the requested filename (exact case)
		#logger.info ( "Remote filename is \"" + remoteFilename + "\" in \"" + pwd + "\"" )
		if ( remoteFilename == remoteFilenameRequested ):
			return True
	return False

def ftpGet ( ftp, remoteFilename, localFilename, transferMode ):
	"""
	Retrieve a single file from the remote server.

	ftp - open FTP connection
	"""
	logger = logging.getLogger()
	message = "Retrieving remote file \"" + remoteFilename + "\" to \"" + localFilename + "\""
	print message
	logger.info(message)
	if ( transferMode == "ASCII" ):
		f = open(localFilename,'w')
		# Need to do the following so the CRLF characters are not stripped
		ftp.retrlines('RETR ' + remoteFilename, lambda line: f.write('%s\n' % line))
		f.close()
	else:
		f = open(localFilename,'wb')
		ftp.retrbinary('RETR ' + remoteFilename, f.write)
		f.close()
	return

def ftpGetSnotelFiles ( snotelStart, snotelEnd, snotelByDateDir ):
	"""
	Download a series of files from the NRCS web site for the specified dates.
	Only one FTP connection is made to increase performance and potential problems
	with the FTP site rejecting multiple connections.

	snotelStart - datetime to start download
	snotelEnd - datetime to end download
	snotelByDateDir - directory into which to save SNOTEL files
	"""
	logger = logging.getLogger()
	logger.info ( "Retrieving SNOTEL update files from NRCS site for period " +
		str(snotelStart) + " to " + str(snotelEnd) )
	# Define the NRCS server information
	server = "ftp.wcc.nrcs.usda.gov"
	login = "anonymous"
	password = "anonymous"
	# Main remote directory, below which are wy2008, etc.
	remoteMainDirectory = "/data/snow/update/co"
	# Valid file names relative to the current directory
	snotelFiles = []
	# Open the connection (retry if there is a problem)...
	retries = 5
	ftptry = 0
	while ( True ):
		ftptry = ftptry + 1
		try:
			ftp = ftpOpenConnection ( server, login, password, remoteMainDirectory )
			break
		except ftplib.all_errors, error:
			logger.exception ( "Error opening FTP connection, try " + str(ftptry) )
			if ( ftptry == retries ):
				message = "Unable to open FTP connection to \"" + server + "\".  Try again later."
				print message
				logger.error ( message )
				exit(1)
	# Loop though the dates to process and pull back
	# Start one day before start because it gets incremented at start of loop
	snotelDate = snotelStart - datetime.timedelta(days=1)
	remoteDirPrev = ""	# Previosu remote directory, to increase performance
	cacheDict = dict()
	while ( True ):
		snotelDate = snotelDate + datetime.timedelta(days=1)
		if ( snotelDate.toordinal() > snotelEnd.toordinal() ):
			break
		# Increment the date once here instead of multiple places below 
		# Figure out the water year for the folder...
		month = snotelDate.month
		if ( (month >= 10) and (month <= 12) ):
			wyFolder = "wy" + str(snotelDate.year - 1)
		else:
			wyFolder = "wy" + str(snotelDate.year)
		remoteDir = remoteMainDirectory + "/" + wyFolder
		remoteFile = "co" + snotelDate.strftime("%y%m%d") + ".txt"
		localFile = snotelByDateDir + "/co" + snotelDate.strftime("%y%m%d") + ".txt"
		# Change to the remote directory so that files can be listed
		if ( not remoteDir == remoteDirPrev ):
			ftp.cwd ( remoteDir )
			remoteDirPrev = remoteDir
		# Verify that the file exists before retrieving to avoid exceptions
		if ( not ftpFileExists(ftp, remoteFile, cacheDict) ):
			logger.info ( "Remote file \"" + remoteFile + "\" in \"" + remoteDir +
			"\" does not exist.  Skipping." )
			continue
		logger.info ( "FTP get remote file \"" + remoteFile +
			"\" to local file \"" + localFile + "\"" )
		try:
			ftpGet ( ftp, remoteFile, localFile, 'ASCII' )
			snotelFiles.append ( localFile )
		except ftplib.all_errors, error:
			logger.exception ( "Error getting file \"" + remoteFile +
				"\" from \"" + remoteDir + "\" (" + str(error) + ").  Skipping." )
			# Comment out so as to not print to the console in operations
			#traceback.print_exc()
			# Don't add to the list
			continue
	# Close the connection and return
	ftp.close()
	return snotelFiles

def ftpOpenConnection ( server, login, password, remoteDirectory ):
	"""
	Open a connection on the remote server.

	server - server to connect to
	login - login name to use
	password - password to use
	remoteDirectory - starting remote directory

	Return the ftp object.
	"""
	ftp = ftplib.FTP(server)
	ftp.login ( login, password )
	ftp.cwd ( remoteDirectory )
	logger = logging.getLogger()
	logger.info("Opened FTP connection to server \"" + server +
		"\" and changed to directory \"" + remoteDirectory + "\"" )
	return ftp

def getDatesFromCommandLine ( snotelStart, snotelEnd ):
	"""
	Get the SNOTEL dates to process from the command line.

	snotelStart - default starting date if not found in the command line.
	snotelEnd - default ending date if not found in the command line.
	"""
	for arg in sys.argv:
		if ( arg.lower().startswith("start=") ):
			snotelStart = datetime.datetime(int(arg[6:10]),int(arg[10:12]),int(arg[12:]))
		elif ( arg.lower().startswith("end=") ):
			snotelEnd = datetime.datetime(int(arg[4:8]),int(arg[8:10]),int(arg[10:]))
	return snotelStart, snotelEnd

def getDataTypeStrings ():
	"""
	Return the data type strings for SWE, SWEAvg, SWEPctAvg, to allow changes
	in the code to only occur in this place.  For flags, add "Flag" to what is
	returned.
	"""
	return "SWE", "SWEAvg", "SWEPctAvg"

def getDoHistoryFromCommandLine ( doHistoryDefault ):
	"""
	Get value of the history command line option.

	doHistoryDefault - default doHistory value if not found in the command line.
	"""
	doHistory = doHistoryDefault
	for arg in sys.argv:
		if ( arg.lower().startswith("history=") ):
			doHistory = arg[8:]
			break
	return doHistory

def getNumHistoryTS ( snotelHistoryFile ):
	"""
	Determine the number of time series in the history file.  This is needed to
	compute offsets when processing other files.
	The "NumTS = num" line in the DateValue file is checked.

	snotelHistoryFile - the history file to examine
	"""
	numHistoryTS = 0
	f = open ( snotelHistoryFile, "r" )
	for line in f:
		if ( line.lower().startswith("numts") ):
			parts = re.split("[=]",line)
			numHistoryTS = int(parts[1].strip())
			break;
	f.close()
	return numHistoryTS

def getSnotelFilesFromCommandLine ( snotelFiles, snotelByDateDir ):
	"""
	Get the SNOTEL files to process from the command line.

	snotelFiles - default value if not found in the command line.
	snotelByDateDir - directory where dated SNOTEL files live

	Return a list of SNOTEL files to process, with leading path.
	"""
	logger = logging.getLogger()
	errorCount = 0
	for arg in sys.argv:
		# Allow more thanone file
		if ( arg.lower().startswith("in=") ):
			snotelFile = arg[3:]
			# Do not want path.
			if ( snotelFile != os.path.basename(snotelFile) ):
				logger.error ( "The file to process must not contain a leading path:  " +
					snotelFile )
				errorCount = errorCount + 1
				continue
			snotelFileWithPath = snotelByDateDir + "/" + snotelFile
			if ( not os.path.exists(snotelFileWithPath) ):
				logger.error ( "The SNOTEL file to process (after adding folder) does not exist:  " +
				snotelFileWithPath )
				errorCount = errorCount + 1
				continue
			snotelFiles.append ( snotelFileWithPath )
	if ( errorCount > 0 ):
		exit ( 1 )
	return snotelFiles

def getSnotelHomeDirFromCommandLine ( snotelHomeDir ):
	"""
	Get the home folder for SNOTEL files from the command line.

	snotelHomeDir - default SNOTEL home if not found in the command line.
	"""
	for arg in sys.argv:
		if ( arg.lower().startswith("snotelhome=") ):
			snotelHomeDir = arg[11:]
			break
	return snotelHomeDir

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

def initializeLogging ( user, appname, snotelHomeDir ):
	"""
	Initialize logging for the script.
	A log file is created in the logs folder under the SNOTEL file
	location ith the user name and date.

	user - user running the software
	appname - the application (script) name, to use in the log file name
	snotelHomeDir - the home directory for SNOTEL files

	Return a tuple with the logger and the log file for this application (script).
	"""
	# Don't need the file extension in appname if it is present
	appname = appname.replace(".py","")
	today = datetime.date.today()
	#loc = tempfile.gettempdir()
	loc = snotelHomeDir + "/logs"
	if ( (user == None) or (user.strip() == "") ):
		# No user name
		theLogfile = loc + "/" + appname + "." + today.strftime("%Y%m%d") + ".log"
	else:
		# Have user name
		theLogfile = loc + "/" + appname + "." + user + "." + today.strftime("%Y%m%d") + ".log"
	# Make sure that the log file directory exists
	if ( not os.path.exists(os.path.dirname(theLogfile) ) ):
		os.makedirs ( os.path.dirname(theLogfile) )
	is_python24 = False
	version = str(sys.version_info[0]) + "." + str(sys.version_info[1])
	if ( float(version) >= 2.4 ):
		is_python24 = True
	if ( is_python24 == True ):
		# Use the newer function to configure logging
		logging.basicConfig ( format = '%(asctime)s %(levelname)8s %(message)s',
			filename=theLogfile, filemode = 'w' )
		logger = logging.getLogger()
	else:
		# Use the following for Python older than 2.4
		# For now use the root logger
		logger = logging.getLogger ()
		#logger = logging.getLogger(appname)
		# Open a file with mode ? so as to NOT append
		hdlr = logging.FileHandler(theLogfile,'w')
		formatter = logging.Formatter('%(asctime)s %(levelname)s %(message)s')
		hdlr.setFormatter(formatter)
		logger.addHandler(hdlr)
	# For now log everything above info but may need to see what low level code puts out
	logger.setLevel(logging.INFO)
	# TODO SAM 2008-03-03 Evaluate putting in more header information similar to Java ioutil.printCreatorHeader()
	logger.info ( "Running as user:  " + user )
	return logger, theLogfile

def initializeTSToolCommandFile ( tstoolCommandFile, historyDir, historyStart, historyEnd ):
	"""
	Initialize the TSTool command file.  This consists of reading all of the
	history files, regardless of the stations being processed.

	tstoolCommandFile - name of the TSTool command file
	historyDir - folder where SNOTEL history files exist
	historyStart - the starting datetime of the SNOTEL history file
	historyEnd - the ending datetime of the SNOTEL history file

	Return a touple of an open file object (to allow additional commands to be added)
	and the number of historical time series total that are read.
	"""
	numHistoryTS = 0
	f = open ( tstoolCommandFile, "w" )
	f.write ( "StartLog(Logfile=\"" + tstoolCommandFile + ".log\"\n" )
	f.write ( "# This TSTool command file will read SNOTEL data in DateValue files\n" )
	f.write ( "# and merge them into the history files.\n" )
	historyFiles = glob.glob ( historyDir + "/*.dv" )
	if ( len(historyFiles) == 0 ):
		f.write ( "# No history files exist - they will be initialized below.\n" )
	else:
		f.write ( "# Read the archive files containing the historical " +
		"period (one file per basin/station).\n" )
		for historyFile in historyFiles:
			f.write ( "ReadDateValue(InputFile=\"" + historyFile + "\")\n" )
			numHistoryTS = numHistoryTS + getNumHistoryTS ( historyFile )
		f.write ( "# Ensure that the historical period is as desired (change in Python script if not).\n" )
		f.write ( "ChangePeriod(TSList=AllTS,NewStart=\"" +
			historyStart.strftime("%Y-%m-%d") + "\",NewEnd=\"" +
			historyEnd.strftime("%Y-%m-%d") + "\")\n" )
	logger = logging.getLogger()
	logging.info ( "The total number of time series from the history files is " +
		str(numHistoryTS) )
	return f, numHistoryTS

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
def printUsage(daysToProcessDefault, snotelHomeDirDefault, doHistoryDefault):
	"""
	Print the script usage.

	daysToProcessDefault - default days to process, will be subtraced from end
	snotelHomeDirDefault - default home folder for SNOTEL data files
	doHistoryDefault - default for whether the history should be processed
	"""
	# Get the script name without the .py since this script is called by a batch file.
	scriptName=os.path.basename(sys.argv[0]).replace(".py","")
	print ""
	print "Command line usage:"
	print ""
	print scriptName + " [[[start=YYYYMMDD] end=YYYYMMDD] OR in=updateFile] [snotelHome=folder] [history=true|false]"
	print ""
	print "Brackets indicate optional parameters and grouping of parameters."
	print "Download (if requested) and process NRCS SNOTEL \"update\" format files."
	print "Each file is processed to create:"
	print " 1) A CSV file suitable for linking to GIS maps."
	print " 2) A DateValue file suitable for use with TSTool."
	print " 3) (optionally) DateValue files containing the cumulative history"
	print "    for processed files." 
	print ""
	print "If a filename is specified, it is converted and optionlly merged into the history archive."
	print "If start and end dates are specified, files are FTPed and then converted/merged."
	print "If only end date is specified, start is defaulted to process " + str(daysToProcessDefault)
	print "   days of data and files are FTPed and then converted/merged."
	print ""
	print "start=YYYYMMDD - starting date to process (default=today - " + \
		str(daysToProcessDefault - 1) + " days)"
	print "end=YYYYMMDD - ending date to process (default=today)"
	print "in=updateFile - specify the NRCS SNOTEL update file (no leading path) to process"
	print "  (use for testing or to process old data)"
	print "snotelHome=folder - specify the path to the SNOTEL data location"
	print "  (default=\"" + snotelHomeDirDefault + "\" - use to override default for testing)"
	print "history=true|false - indicate whether the history should be updated,"
	print "  slower and under development (default=" + str(doHistoryDefault) + ")"
	print ""
	print "Example to download and process 14 days of data including today:"
	print ""
	print " " + scriptName
	print ""
	print "Example to download and process a range of dates:"
	print ""
	print " " + scriptName + " start=20071001 end=20071031"
	print ""
	print "Example to process a previously downloaded data file:"
	print ""
	print " " + scriptName + " in=co20071001.txt"
	print ""
	return

#-------------------------------------------------------------------------------
def printVersion(version):
	"""
	Print the script version.

	version = script version, as string
	"""
	print ""
	print os.path.basename(sys.argv[0]) + " version:  " + version
	print ""
	return

def readNrcsSnotelUpdateFile ( updateFile ):
	"""
	Read the file from the NRCS SNOTEL "update" format file to a 
	list of time series (TSlite objects).

	updateFile - NRCS SNOTEL file to convert

	Return a tuple of the list of time series, a list of the actual names used in the file
	(needed because some have periods that interfere with time series identifiers)
	and the date for the data (from the file header), and a list of location type as "basin"
	or "station" (needed to filter out basins for CSV file).
	"""
	# Get logger
	logger = logging.getLogger()
	basinCount = 0
	stationCount = 0
	tslist = []
	tslocList = []
	tslocTypeList = []
	infp = open( updateFile)
	# Loop over unused header information, finding the date as lines are read
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

	# Get the data type strings for time series data types
	dataTypeSWE, dataTypeSWEAvg, dataTypeSWEPctAvg = getDataTypeStrings()

	# Now read until a dash is encountered, indicating the end of all data
	# Lines that don't start with a space are the basin name.
	# Lines that start with space and have the second character non-space are the station name.
	# Lines that start with spaces and "Basin wide" are the basin percent of average
	# Data values are managed using TSlite objects:
	#	SWE (number) (station)
	#	SWEFlag (string) (station)
	#	SWEAverage (number) (station)
	#	SWEPercentOfAverage (number) (station and basin)
	#	SWEPercentOfAverageFlag (string) (station and basin)
	# Missing values are indicated by -999 and the character flags are taken from the report
	# (see above for explanation):
	#	M
	#	*
	#
	for line in infp:
		lineStripped = line.strip()
		logger.info ( 'Reading line "' + line.rstrip() + '"' )
		if ( line.startswith("-") ):
			# No more data.
			break
		elif ( (len(lineStripped) == 0) or lineStripped.startswith("-----  ") ):
			# Blank line or other non-data
			continue
		#
		# Start lines to handle basin data...
		#
		elif ( line[0:1] != ' ' ):
			# Basin name - start of this basin.  The basin data are handled in the
			# next "elif" clause...
			basinName = line.strip()
			logger.info('Initializing basin time series for "' + basinName + '"' )
			# Initialize a new time series for basin data...
			dataType = dataTypeSWEPctAvg
			tsid = basinName + ".NRCS." + dataType + ".Day"
			description = basinName + " " + dataType
			interval = "Day"
			units = "PCT"
			missing = -999
			tsBasinSWEPercentOfAverage = TSlite.TSlite ( tsid, description, interval, units, missing )
			# Append the time series to the list here so that it is at the head of stations in the basin
			tslist.append ( tsBasinSWEPercentOfAverage )
			tslocList.append ( basinName )
			tslocTypeList.append ( "basin" )
		elif ( lineStripped.startswith("Basin wide") ):
			# Basin totals - end of this basin.
			# Get from the specific columns, possibly with data flags
			basinCount = basinCount + 1
			basinSWEPercentOfAverage, basinSWEPercentOfAverageFlag = parseDataValue ( line[47:53].strip() )
			# Set the data values in the time series
			tsBasinSWEPercentOfAverage.setDataValue ( dataDate,
				basinSWEPercentOfAverage, basinSWEPercentOfAverageFlag )
		#
		# ...end lines to handle basin data
		#
		# Start lines to handle station data...
		#
		elif ( (line[0:1] == ' ') and (line[1:2] != ' ') ):
			# Station name
			stationName0 = line[1:24].strip()	# Retain exact name from file
			stationName = stationName0
			# Determine if the station has already been encountered in the file.  In this case
			# avoid adding again
			tsaddedPreviously = False
			for tsadded in tslist:
				if ( tsadded.getLocation() == stationName ):
					logger.info ( "Station \"" + stationName +
					"\" time series have already been added in another basin - not adding again." )
					tsaddedPreviously = True
					break
			if ( tsaddedPreviously ):
				# No need to add
				continue
			# Remove any periods in the name because this interferes with time series identifiers
			if ( stationName.find(".") >= 0 ):
				logger.warn ( "Removing period from station name: \"" + stationName + "\"" )
				stationName = stationName.replace(".","")
			stationCount = stationCount + 1
			stationSWE, stationSWEFlag = parseDataValue ( line[32:38].strip() )
			stationSWEAverage, stationSWEAverageFlag = parseDataValue ( line[39:46].strip() )
			stationSWEPercentOfAverage, stationSWEPercentOfAverageFlag = parseDataValue ( line[47:53].strip() )
			# Initialize a new time series for station data and set the data value
			#
			# SWE
			#
			dataType = dataTypeSWE
			tsid = stationName + ".NRCS." + dataType + ".Day"
			description = basinName + " " + dataType
			interval = "Day"
			units = "IN"
			missing = -999
			tsStationSWE = TSlite.TSlite ( tsid, description, interval, units, missing )
			tsStationSWE.setDataValue ( dataDate, stationSWE, stationSWEFlag )
			tslist.append ( tsStationSWE )
			tslocList.append ( stationName0 )
			tslocTypeList.append ( "station" )
			#
			# SWEAverage
			#
			dataType = dataTypeSWEAvg
			tsid = stationName + ".NRCS." + dataType + ".Day"
			description = basinName + " " + dataType
			interval = "Day"
			units = "IN"
			missing = -999
			tsStationSWEAverage = TSlite.TSlite ( tsid, description, interval, units, missing )
			tsStationSWEAverage.setDataValue ( dataDate, stationSWEAverage, stationSWEAverageFlag )
			tslist.append ( tsStationSWEAverage )
			tslocList.append ( stationName0 )
			tslocTypeList.append ( "station" )
			#
			# SWEPercentOfAverage
			#
			dataType = dataTypeSWEPctAvg
			tsid = stationName + ".NRCS." + dataType + ".Day"
			description = basinName + " " + dataType
			interval = "Day"
			units = "PCT"
			missing = -999
			tsStationSWEPercentOfAverage = TSlite.TSlite ( tsid, description, interval, units, missing )
			tsStationSWEPercentOfAverage.setDataValue ( dataDate,
				stationSWEPercentOfAverage, stationSWEPercentOfAverageFlag )
			tslist.append ( tsStationSWEPercentOfAverage )
			tslocList.append ( stationName0 )
			tslocTypeList.append ( "station" )
		#
		# ...end lines to handle station data.
		#
	infp.close()
	logger.info ( "Read " + str(basinCount) + " basins, with a total of " + str(stationCount) + " stations" )
	logger.info ( "The number of time series is " + str(basinCount) + "+3*" + str(stationCount) + "=" +
		str(basinCount + 3*stationCount) )
	return tslist, tslocList, tslocTypeList, dataDate

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
def writeDateValueData ( f, tslist, dataDate, delim ):
	"""
	Write the data for the DateValue file.

	f - file descriptor
	tslist - list of time series to write
	dataDate - the date associated with the SNOTEL data file
	delim - the delimiter to use between data values
	"""
	logger = logging.getLogger()
	# Write the date (no delimiter)
	f.write ( dataDate.strftime("%Y-%m-%d") )
	for ts in tslist:
		# Iterate through the time series
		logger.info ( 'Writing data for time series "' + ts.getTSID() + '"' )
		theDate, theValue, theFlag = ts.getDataValue(dataDate)
		# Write the value
		f.write ( delim + str(theValue) )
		# Write the flag
		if ( ts.getHasDataFlags() ):
			f.write ( delim + "\"" + theFlag + "\"" )
	# write a newline to finish the line after all time series
	f.write("\n")
	return

#-------------------------------------------------------------------------------
def writeDateValueHeader ( f, tslist, dataDate, delim ):
	"""
	Write the header for the DateValue file.

	f - file descriptor
	tslist - list of time series
	dataDate - the date associated with the SNOTEL data file
	delim - the delimiter to use between data values
	"""
	logger = logging.getLogger()
	# Loop through the basins and then the stations for each basin, appending
	# to the strings in the header, which are initialized here...
	TSID = ""
	description = ""
	units = ""
	missingVal = ""
	dataFlags = ""
	dateLine = ""
	# Iterate through the time series
	numTS = len(tslist)
	logger.info("There are " + str(numTS) + " time series to process.")
	its = 0
	for ts in tslist:
		its = its + 1
		logger.info ( 'Processing time series "' + ts.getTSID() + '" (' + str(its) + " of " + str(numTS) + ")" )
		TSID, description, units, missingVal, dataFlags, dateLine = \
			appendToDateValueHeaderStrings(TSID, ts.getTSID(), description, ts.getDescription(),
			units, ts.getUnits(), missingVal, ts.getMissing(), dataFlags, ts.getHasDataFlags(),
			dateLine, delim )
	# Now write the header
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

def writeSnotelDataToCsvFile ( tslist, tslocList, tslocTypeList, csvFile, dataDate ):
	"""
	Write the time series data to the CSV file to be joined with GIS.

	tslist - list of time series to write
	tslocList - list of locations from the original SNOTEL file, which may
		contain "." that cause problems in time series IDs.  The order of
		tslist and tslocList are the same
	tslocTypeList - list of location types for time series as "basin" or "station"
		(only stations are written to the CSV file)
	csvFile - the name of the CSV file to write
	dataDate - datetime for the data being processed
	"""
	logger = logging.getLogger()
	logger.info("Writing data to CSV file \"" + csvFile + "\"" )
	delim = ","
	f = open(csvFile,'w')
	# Write the header
	# Get the data type strings for time series data types
	dataDateString = "%04d%02d%02d" % (dataDate.year, dataDate.month, dataDate.day)
	dataTypeSWE, dataTypeSWEAvg, dataTypeSWEPctAvg = getDataTypeStrings()
	f.write ( "Location" + delim + "Date" + delim + dataTypeSWE + delim +
		dataTypeSWE + "Flag" + delim +
		dataTypeSWEAvg + delim + dataTypeSWEAvg + "Flag" + delim +
		dataTypeSWEPctAvg + delim + dataTypeSWEPctAvg + "Flag\n" )
	i = -1
	# Iterate through the time series.  Multiple time series are written to one
	# line and the order depends on the original insertion into the list.
	for ts in tslist:
		i = i + 1
		if ( tslocTypeList[i] == "basin" ):
			# No need to process
			continue
		theDate, theValue, theFlag = ts.getDataValue(dataDate)
		dataType = ts.getDataType()
		# No need for quotes if basins are excluded
		#f.write ( "\"" + tslocList[i] + "\"" )
		if ( dataType == dataTypeSWE ):
			# First time series in a group so write the location and date
			f.write ( tslocList[i] + delim + dataDateString )
		f.write( delim + str(theValue) )
		if ( ts.getHasDataFlags() == True ):
			f.write( delim + theFlag )
		if ( dataType == dataTypeSWEPctAvg ):
			# Also write a newline to finish the line for the station
			f.write("\n")
	f.close()
	return

def writeSnotelDataToDateValueFile ( tslist, dvFile, dataDate ):
	"""
	Convert the list of time series objects to a DateValue file.

	tslist - list of time series to write
	dvFile - DateValue file to write
	dataDate - date for the data
	"""
	f = open ( dvFile, 'w' )
	delim = ','
	writeDateValueHeader ( f, tslist, dataDate, delim )
	writeDateValueData ( f, tslist, dataDate, delim )
	f.close()
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
	if ( not logger == "unknown" ):
		print 'Unexpected error.  See the details in \"' + logfile + '"'
	else:
		print 'Unexpected error.'
	traceback.print_exc()
	exit(1)

# Done - exit with success status (no exception handled because script is done)
exit(0)
