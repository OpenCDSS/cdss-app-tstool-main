@echo off
rem Script to figure out the latest (newest) TSTool and run the tstool.exe in that folder.
rem This allows using the PATH and file extension association to automatically
rem recognize the latest version.
rem For example, double-clicking on a *.TSTool file in Windows Explorer should work.
rem This work-around script will not be needed when the TSTool installer is updated to
rem a new version of Launch4J.

rem To install:
rem 1) Create a folder C:\CDSS\TSTool-latest\bin
rem 2) Copy this script into that folder.
rem 3) For the *.TSTool file extension association in Windows explorer, associate with the batch file.

rem See: http://stackoverflow.com/questions/10519389/get-last-created-directory-batch-command 

rem The following works to return the newest install but may not be correct
rem if an old version is re-installed.
rem 
rem /b is for bare format
rem /ad-h lists non-hidden directories
rem t:c sort by creation date
rem /od sort oldest first
rem FOR /F "delims=" %%i IN ('dir "c:\CDSS\TSTool-*" /b /ad-h /t:c /od') DO SET a=%%i

rem The following works sorting alphabetically on install folder name since
rem zero-padded version numbers will cause proper sort>
rem Ignore "TSTool-latest" because that is the special folder that this script is in.
FOR /F "delims=" %%i IN ('dir "c:\CDSS\TSTool-*" /b /ad-h /on') DO (
	if not %%i == TSTool-latest SET a=%%i
)

rem Now run TSTool with all the original command line parameters
rem The following works to find dlls, etc. that need to be loaded
c:
cd \CDSS\%a%\bin
tstool.exe %*

rem The following does not load HEC-DSS dlls properly
rem C:\CDSS\%a%\bin\tstool.exe %*
