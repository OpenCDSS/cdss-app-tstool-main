# datastores #

This folder contains datastore configuration files, to enable access to data.

* Because TSTool supports the State of Colorado,
Colorado-centric datastores are enabled by default if possible.
* Datastores that are generally useful are enabled by default,
for example US federal agency web services.
* Datastores that require other components, such as installing a local State of Colorado HydroBase,
database, are disabled by default, and can be enabled by users once components are installed.
* Datastore configurations can be overridden and extended by user's `.tstool/N/datastores` files.
* The name of the datastore file is user-defined but is typically similar to the
datastore type.  The `Name` property in the configuration file is what is used by commands.

Datastore configuration files distributed with TSTool include:

* `HydroBase.cfg` - local Colorado HydroBase database using CDSS acount
* `HydroBase-HBGuest.cfg` - local Colorado HydroBase database using HBGuest account
* `HydroBaseWeb.cfg` - Colorado HydroBase REST web services
* `NrcsAwdb.cfg` - Natural Resources Conservation Service (NRCS) AWDB web services
* `RCC-ACIS.cfg` - Regional Climate Center (RCC) Applied Climate Information System (ACIS) web services
* `UsgsNwisDaily.cfg` - United States Geological Survey (USGS) National Water Information System (NWIS) daily data
* `UsgsNwisGroundwater.cfg` - USGS NWIS groundwater data
* `UsgsNwisInstantaneous.cfg` - USGS NWIS instantaneous (15 minute) data
