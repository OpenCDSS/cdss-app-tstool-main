# bin #

This folder contains TSTool software executable resources.

## Windows ##

The following files are used on Windows.

* `TSTool.exe` - TSTool executable program for Windows,
which launches the Java Runtime Environment to run TSTool software
* `TSTool.l4j.ini` - TSTool launcher configuration file, used to change memory limits
* `*.jar` - Java archive files for libraries used by TSTool
* `*.DLL`, `*.dll` - native Windows libraries, typically for specific datastores or file formats

## Linux ##

The following files are used on Linux.
Scripts are also distributed on Windows to facilitate creating the Linux software installer from
Windows installed files.

* `build-linux-distro.bash` - script to build Linux TSTool installer from Windows installation
* `setup-linux-tstool.sh` - script that is packaged in TSTool installer and will run
when files are uncompressed during installation
* `tstool` - script to run TSTool on Linux, Mac, Cygwin, etc.
* `*.jar` - Java archive files for libraries used by TSTool
