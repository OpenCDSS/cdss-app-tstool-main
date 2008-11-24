@echo off
rem This batch file runs the Python script "download-snotel.py" to download SNOTEL
rem "update" files for today and the past several days (as configured in the Python
rem script), and creates:
rem 
rem 1) a CSV file for each SNOTEL file, for linking to GIS maps
rem 2) a DateValue file for use with TSTool to merge into...
rem 3) (optionally) an updated local history database containing the full
rem    SNOTEL period of record, using DateValue files that are compatible with TSTool.
rem
rem This script was created by Steve Malers, Riverside Technology, inc. (RTi)

rem Pass the command line parameters to the script.
rem See the script usage for options

rem Python to use (in case need to pick between different versions on system)
set python=C:\python24\python
rem The following commented line is more generic and can be used if it points to
rem a version that tests out with this script.
rem set python="python"

rem Configuration information for installed operational version
rem snotelHome default is in the rem snow-update.py script.
rem Update the following location to correspond to the installed version of TSTool.
set scriptHome=C:\CDSS\TSTool-08.18.02\python\snotel

rem Configuration information for Development version at RTi (comment out for operational system)
rem set scriptHome=C:\develop\TSTool_SourceBuild\TSTool\resources\runtime\python\snotel
rem set snotelHome=K:/PROJECTS/1015_Bureau of Reclamation-Phase 3 SNODAS/SnowDataTools/SNOTEL

rem DOS/Windows command line replaces = with space so need to do some work to pass the
rem desired in=X, start=X, end=X to the Python script.

set arg1=
set arg2=
set arg3=
set arg4=
if "%1"=="-v" set arg1="-v"
if "%1"=="-h" set arg1="-h"
if "%1"=="in" set arg1="in=%2"
if "%1"=="start" set arg1="start=%2"
if "%1"=="end" set arg1="end=%2"
if "%1"=="history" set arg1="history=%2"

if "%3"=="-v" set arg2="-v"
if "%3"=="-h" set arg2="-h"
if "%3"=="in" set arg2="in=%4"
if "%3"=="start" set arg2="start=%4"
if "%3"=="end" set arg2="end=%4"
if "%3"=="history" set arg2="history=%4"

if "%5"=="-v" set arg3="-v"
if "%5"=="-h" set arg3="-h"
if "%5"=="in" set arg3="in=%6"
if "%5"=="start" set arg3="start=%6"
if "%5"=="end" set arg3="end=%6"
if "%5"=="history" set arg3="history=%6"

if "%7"=="-v" set arg4="-v"
if "%7"=="-h" set arg4="-h"
if "%7"=="in" set arg4="in=%6"
if "%7"=="start" set arg4="start=%6"
if "%7"=="end" set arg4="end=%6"
if "%7"=="history" set arg4="history=%6"

rem Now run
if "%snotelHome%"=="" %python% %scriptHome%\download-snotel.py %arg1% %arg2% %arg3% %arg4%
if not "%snotelHome%"=="" %python% %scriptHome%\download-snotel.py "snotelHome=%snotelHome%" %arg1% %arg2% %arg3% %arg4%

rem Clear variables

set python=
set scriptHome=
set snotelHome=
set arg1=
set arg2=
set arg3=
set arg4=
