# This NSIS file will be used when creating the TSTool installer.

# Program name.
!define DISPLAYNAME "TSTool"

# File extension to be associated with TSTool:
# - used to be '.TSTool' but more recent versions default to lower case
!define FILE_EXT ".tstool"

# Description for the extension.
!define FILE_DESC "TSTool command file"

# By default prompt to configure HydroBase.
!define CONFIGURE_HYDROBASE "true"

# Location of files to be packaged in the installer.
!define INST_BUILD_DIR "dist\install-CDSS"

# Uncomment to skip some sections to allow installer compilation to run faster.
#!define TEST "true"

# Now include the more general installation code that creates the installer.
!include externals\CDSS\installer\cdss.nsh
