@echo off
rem This batch file runs a Python script to download SNOTEL "update" files
rem for today and the past 21 days, updating a local database and creating
rem a CSV file for linking to GIS maps.
rem
rem Script created by Steve Malers, RTi

rem Pass the command line parameters to the script.
rem See the script usage for options

rem Python to use (in case need to pick between different versions on system)
rem By default configure to use what is in the PATH
set python=python

rem Development version at RTi...
set scriptHome=C:\develop\TSTool_SourceBuild\TSTool\resources\runtime\python\snotel
set snotelHome=K:/PROJECTS/1015_Bureau of Reclamation-Phase 3 SNODAS/SnowDataTools/SNOTEL

rem Installed version (uncomment for delivery - confirm with CWCB)...
rem set scriptHome=C:\CDSS\TSTool=08.16.01\python\snotel
rem set snotelHome=C:\CDSS\snotel

rem DOS/Windows command line replaces = with space so need to do some work to pass the
rem desired in=X, start=X, end=X to python

set arg1=
set arg2=
set arg3=
if "%1"=="-v" set arg1="-v"
if "%1"=="-h" set arg1="-h"
if "%1"=="in" set arg1="in=%2"
if "%1"=="start" set arg1="start=%2"
if "%1"=="end" set arg1="end=%2"

if "%3"=="-v" set arg2="-v"
if "%3"=="-h" set arg2="-h"
if "%3"=="in" set arg2="in=%4"
if "%3"=="start" set arg2="start=%4"
if "%3"=="end" set arg2="end=%4"

if "%5"=="-v" set arg3="-v"
if "%5"=="-h" set arg3="-h"
if "%5"=="in" set arg3="in=%6"
if "%5"=="start" set arg3="start=%6"
if "%5"=="end" set arg3="end=%6"

rem Now run
%python% %scriptHome%\download-snotel.py "snotelHome=%snotelHome%" %arg1% %arg2% %arg3%

rem Clear variables

set python=
set scriptHome=
set snotelHome=
set arg1=
set arg2=
set arg3=
