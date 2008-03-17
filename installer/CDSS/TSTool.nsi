#############################################################
# AUTHOR: Ian Schneider
# DATE: Mar 17 2008

!define DISPLAYNAME "TSTool"
!define VERSION 7.00.00
!define FILE_EXT ".TSTool"
!define FILE_DESC "TSTool commands file"
!define CONFIGURE_HYDROBASE "true"
!define INST_BUILD_DIR "dist\install-CDSS"

# uncomment to skip some sections to allow installer compilation to run faster
#!define TEST "true"

# change working directory to product root to make paths more sane!
!cd "..\..\"

!include externals\CDSS\installer\cdss.nsh