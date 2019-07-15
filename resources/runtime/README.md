# TSTool #

This folder contains TSTool software resources.
This folder comprises all of the TSTool software and is self-contained.
The only additional resources on the system are registry settings on Windows to facilitate uninstall.

* `bin/` - TSTool software files:
	+ `TSTool.exe` - TSTool executable program for Windows,
	which launches the Java Runtime Environment to run TSTool software
	+ `tstool` - script to run TSTool for Linux
	+ `TSTool.l4j.ini` - TSTool launcher configuration file, used to change memory limits
	+ `*.jar` - Java archive files for libraries used by TSTool
	+ `*.DLL`, `*.dll` - native Windows libraries, typically for specific datastores or file formats
* `datastores/` - Datastore configurations that are distributed with TSTool.
	+ Datastores that are generally useful are enabled by default,
	for example US federal agency web services.
	+ Datastores that require other components, such as installing a local State of Colorado HydroBase
	database, are disabled by default, and can be enabled by users once components are installed.
	+ Datastore configurations can be overridden and extended by user's `.tstool/N/datastores` files.
* `doc`/ - TSTool documentation.
	+ Because TSTool documentation is now online, a simple README is provided
	with link to the online documentation.
* `examples/` - Example data and command files.
	+ **This will be phased out because information is in online documentation and automated tests.**
* `plugins/` - Plugins that provide custom features beyond core TSTool.
	+ This is primarily used on Linux systems where TSTool is installed in a shared location.
	+ User-installed plugins exist in the user's `.tstool/N/plugins` folder.
* `python/` - Python code that supports TSTool.
	+ **This will be updated as more Python resources are developed.**
* `system/` - TSTool main configuration files.
	+ The `TSTool.cfg` properties can be overridden and extended by user's `.tstool/N/system/TSTool.cfg` file.
