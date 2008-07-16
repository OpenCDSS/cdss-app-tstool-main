# Time series class
#
# This is a scaled down version of the RTi Java time series code, to
# support and evaluate data conversion.  In the future, more complete
# Python or Jython modules may be available.

class TS ( object ):
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
		self.__tsdata = dict()
		return

	def getDataValue ( self, datetime ):
		"""
		Return a tuple of (datetime, value, flag) for the requested datetime,
		or None if the date is not found.
		"""
		return self.__tsdata[str(datetime)]

	def getTSID ( self ):
		"""
		Return the time series identifier.
		"""
		return self.__tsid

	def setDataValue ( self, datetime, value=None, flag=None ):
		"""
		Set the data value for a datetime.

		datetime - datetime to set
		value - data value to set
		flag - data flat to set
		"""
		# TODO SAM 2008-07-03 Is str() needed?
		self.__tsdata[str(datetime)] = ( value, flag )
		return
