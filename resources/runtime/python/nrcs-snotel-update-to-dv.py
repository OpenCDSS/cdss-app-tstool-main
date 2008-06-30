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

        # Set the user to be used for NWSRFS output/log files (default to current login)
	# FIXME SAM 2008-06-27
	user = ""
        #user = getUser ()

        # Initialize logging to capture messages
        logger, log = initializeLogging ( user, scriptname )
        # Set the global variable for logfile
        logfile = log
        print ""
        print "Opened log file " + logfile

	# Get the input and output files from the command line (argv[0] is script name)
	if ( len(sys.argv) < 3 ):
		printUsage()
		exit(1)

	# Input file to processs
	updateFile = sys.argv[1]
	# Output file to create
	dvFile = sys.argv[2]

	# Read the NRCS file
	dict, dataDate = convertNrcsSnotelUpdateFileToDict ( updateFile )

	# Create the DateValue file
	convertDictToDateValueFile ( dict, dvFile, dataDate )

	# Call TSTool to process into historical DateValue files
	#runTSTool()
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

def getUser ():
        """
        Return the current user, taken from the LOGNAME environment variable.
        The script will exit if this information is not available.
        """
	user = None
	if ( os.name == "posix" ):
		# Linux
        	user = os.getenv ( "LOGNAME" )
	elif ( os.name == 'nt' ):
		# Windows
		# FIXME SAM 2008-06-27 Need to figure out how to get this
		user = None
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
		logging.basicConfig ( format = '%(asctime)s %(levelname)8s %(message)s', filename=logfile, filemode = 'w' )
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
def printUsage():
        """
        Print the script usage.
        """
        print ""
	print "Convert an NRCS SNOTEL update file to a DateValue file."
        print ""
        print os.path.basename(sys.argv[0]) + " usage:"
        print ""
        print os.path.basename(sys.argv[0]) + " in=updateFile OR startdate=YYYYMMDD [enddate=YYYYMMDD]"
        print ""
        print "in=updateFile - NRCS SNOTEL update file name (input)"
        print "  DateValue file will have same name with .dv extension"
        print "startdate=YYYYMMDD - ending date to process (default=today - 7)"
        print "enddate=YYYYMMDD - ending date to process (default=today)"
        print ""
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

except SystemExit:
        # This exception is raised when system.exit() is called.
        # Just let normal exit happen to shut down this script.
        print 'Exiting...' + str(SystemExit)
except:
        # Using the logger here assumes that basic logging setup was successful
        # in the main function.
        logger = logging.getLogger()
        logger.exception ( "Unexpected exception" )
        print 'Unexpected error.  See the detail in \"' + logfile + '"'
        traceback.print_exc()
        exit(1)

# Done - exit with success status (no exception handled because script is done)
exit(0)
