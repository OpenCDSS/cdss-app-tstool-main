# system #

This folder contains main configuration files and supporting data for TSTool, for use in the development environment.
Similar files for production release exist in the repository "resources/runtime".
Additional configuration files are datastore configuration in the ../datastores folder.
Files in this "system" folder include:

* `CDSS.cfg` - HydroBase configuration for HydroBase database selector dialog for interactive database selection
* `ClimateDivisions.csv` - climate division data used by NCDC and other federal US agencies, used by RCC ACIS datastore
* `DATAUNIT` - data unit conversion factors
* `FIPS-county.csv` - US federal FIPS data for counties, used by some some datastores
* `FIPS-state.csv` - US federal FIPS data for states, used by some some datastores
* `TSTool.cfg` - main TSTool configuration file
* `TSToolLogging.cfg` - logging configuration for `java.util.logging` package, used by some third-party packages
(TODO smalers 2019-07-12 need to better integrate with built in Message class)
* `TSTool-Reclamation.cfg` - `TSTool.cfg` file with Reclamation HDB database enabled, for HDB testing
