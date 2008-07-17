# Time series class
#
# This is a scaled down version of the RTi Java time series code, to
# support and evaluate data conversion.  In the future, more complete
# Python or Jython modules may be available.

import re

class TSlite ( object ):
	def __init__ ( self, tsid="unknown", description="", interval="Irregular",
			units="", missing = None ):
		"""
		Initialize a new time series.  Data can be added by alling setDataValue().

		tsid - the time series identifier, as per TSTool
		description - a single line description for the time series
		interval - the data interval (e.g., "Day", "6Hour", "Irregular")
		units - units of data
		missing - missing data value
		"""
		self.__tsid = tsid
		self.__description = description
		self.__interval = interval
		self.__units = units
		self.__missing = missing
		self.__missing = missing
		self.__tsdata = dict()
		self.__hasDataFlags = False
		return

	def getDataType ( self ):
		"""
		Return the time series description.
		"""
		if ( self.__tsid == None ):
			return ""
		else:
			return re.split("[.]",self.__tsid)[2]

	def getDataValue ( self, datetime ):
		"""
		Return a tuple of (datetime, value, flag) for the requested datetime,
		or None if the date is not found.
		"""
		return self.__tsdata[str(datetime)]

	def getDescription ( self ):
		"""
		Return the time series description.
		"""
		return self.__description

	def getHasDataFlags ( self ):
		"""
		Return whether the time series is using data flags.
		"""
		return self.__hasDataFlags

	def getMissing ( self ):
		"""
		Return the time series missing data value.
		"""
		return self.__missing

	def getTSID ( self ):
		"""
		Return the time series identifier.
		"""
		return self.__tsid

	def getUnits ( self ):
		"""
		Return the time series data units.
		"""
		return self.__units

	def setDataValue ( self, datetime, value=None, flag=None ):
		"""
		Set the data value for a datetime.

		datetime - datetime to set
		value - data value to set
		flag - data flat to set
		"""
		# TODO SAM 2008-07-03 Is str() needed?
		self.__tsdata[str(datetime)] = ( datetime, value, flag )
		if ( flag != None ):
			self.__hasDataFlags = True
		return
