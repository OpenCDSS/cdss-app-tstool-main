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

def convertDictToDateValueFile ( dict, dvFile, dataDate ):
	"""
	Convert the dictionary of time series data values and flags to a
	DateValue file.

	dict - dictionary containing additional dictionaries with data values
	dvFile - DateValue file to write
	"""
	f = open ( dvFile, 'w' )
	writeDateValueHeader ( f, dict, dataDate )
	f.close()
	return

def convertNrcsSnotelUpdateFileToDict ( updateFile ):
	"""
	Read the file from the NRCS SNOTEL "update" format file to a dictionary.

	updateFile - NRCS SNOTEL file to convert

	Return a tuple of the dictionary and the date for the data (from the file header).
	The dictionary has the form:

		key = Basin/Station Name
		object = dict {
			
			}
	"""
	infp = open( updateFile)
	# Find the "COLORADO" line
	for line in infp:
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
	# Data values are managed using a dictionary for each station, where the object is another dictionary
	# that includes (type in parenthesis):
	#	Basin (string)
	#	SWE (number)
	#	SWEFlag (string)
	#	SWEAverage (number)
	#	SWEPercentOfAverage (number)
	#	SWEPercentOfAverageFlag (string)
	# Missing values are indicated by -999 and the character flags are taken from the report:
	#
	updateDict = dict()
	for line in infp:
		if ( line.startswith("-") ):
			# No more data.
			break
		elif ( (len(line.strip()) == 0) or (line.strip().startswith("-----  ")) ):
			# Blank line or other non-data
			continue
		elif ( line.strip().startswith("Basin wide") ):
			# Basin totals.  Get from the specific columns, possibly with data flags
			basinSWEAverage, basinSWEAverageFlag = parseDataValue ( line[47:53].strip() )
			# Add an entry for the basin
			basinDict = dict()
			basinDict["BasinSWEAverage"] = basinSWEAverage
			basinDict["BasinSWEAverageFlag"] = basinSWEAverageFlag
			updateDict[basinName] = basinDict
		elif ( (line[0:1] == ' ') and (line[1:2] != ' ') ):
			# Basin name
			basinName = line.strip()
		elif ( (line[0:2] == '  ') and (line[2:3] != ' ') ):
			# Station name
			stationName = line[1:24].strip()
			stationSWE, stationSWEFlag = parseDataValue ( line[32:38].strip() )
			stationSWEAverage, stationSWEAverageFlag = parseDataValue ( line[39:46].strip() )
			stationSWEPercentOfAverage, stationSWEPercentOfAverageFlag = parseDataValue ( line[47:53].strip() )
			# Add an entry for the station
			stationDict = dict()
			stationDict["Basin"] = basinName
			stationDict["StationSWE"] = stationSWE
			stationDict["StationSWEFlag"] = stationSWEFlag
			stationDict["StationSWEAverage"] = stationSWEAverage
			stationDict["StationSWEAverageFlag"] = stationSWEAverageFlag
			stationDict["StationSWEPercentOfAverage"] = stationSWEPercentOfAverage
			stationDict["StationSWEPercentOfAverageFlag"] = stationSWEPercentOfAverageFlag
			updateDict[stationName] = stationDict
	infp.close()
	return updateDict, dataDate

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

	dataString - the string to parse, with data value and/or flag

	Return the data vale, flag (flag can be "").
	"""
	missingValue = -999.0
	nrcsMissing = "-M"
	nrcsUncertain = "*"
	if ( dataString == "" ):
		value = missingValue
		flag = ""
	if ( dataString == nrcsMissing ):
		value = missingValue
		# Don't use the dash for the flag
		flag = nrcsMissing[1:]
	elif ( dataString == nrcsUncertain ):
		value = missingValue
		flag = nrcsUncertain
	else:
		value = dataString
		flag = ""
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
        print os.path.basename(sys.argv[0]) + " updateFile dvFile"
        print ""
        print "updateFile - NRCS SNOTEL update file name (input)"
        print "dvFile - DateValue file name (output)"
        print ""
        return

#-------------------------------------------------------------------------------
def writeDateValueHeader ( f, dict, dataDate ):
	"""
	Write the header for the DateValue file.

	f - file descriptor
	dict - dictionary of time series data
	dataDate - the date associated with the SNOTEL data file
	"""
	# Loop through the basins and then the stations for each basin, appending
	# to the strings in the header
	numBasins = 0
	for key in dict.keys():
		basinForStation = None
		stationName = None
		dataDict = dict[key]
		try:
			basinForStation = dataDict["BasinName"]
		except KeyError:
			# Ignore because the time series may be for a basin (no station)
			pass
		if ( basinForStation == None ):
			# This is a basin
			print "Processing basin " + key 
	# Now write the header
	delim = ","
	f.write ( "# DateValueTS 1.4 file\n" )
	# Only one day of data in the file
	f.write ( "Delimiter = \"" + delim + "\"\n" )
	f.write ( "NumTS     = " + str(len(dict)) + "\n" )
	f.write ( "Start     = " + dataDate.strftime("%Y-%m-%d") + "\n" )
	f.write ( "End       = " + dataDate.strftime("%Y-%m-%d") + "\n" )
	return

#-------------------------------------------------------------------------------
# Call the main function, which does all the work, and handle major unexpected
# errors.  Put the following at the end of the script because functions need to
# be defined before they can be called.

try:
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
