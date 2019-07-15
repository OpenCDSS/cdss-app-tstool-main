# bin #

**This `README-dev.md` file is NOT intended to be included in the software installer
because it provides information to software developers.**

This folder contains executable software needed in the runtime environment.
The `conf/build.xml` file copies these files to the distribution folder when creating the installer.

* `build-linux-distro.bash` - script to build the Linux distribution from Windows distribution
* `tstool` - script to run TSTool on Linux and MacOS
* `tstool-latest.bat` - batch file to run TSTool on Windows,
not typically needed because Windows has `TSTool.exe` created by Launch4j
