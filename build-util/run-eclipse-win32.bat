@echo off
rem Configure and run Eclipse Neon 3 32-bit Windows for standard TSTool development environment.
rem - 32-bit is used until some legacy issues can be resolved, such as 32-bit library dlls
rem - this script assumes that Eclipse is installed in a specific location and more
rem   locations can be added

rem Determine the script folder and workspace folder.
rem - %~dp0 has backslash at end
set scriptFolder=%~dp0
set workspaceFolder=%scriptFolder%..\..\..\eclipse-workspace
echo Batch file folder:  %scriptFolder%
echo Workspace folder: %workspaceFolder%

rem It is assumed that the development environment follows the standard folder structure, for example:
rem - %USERPROFILE%\cdss-dev\TSTool\git-repos\cdss-app-tstool-main
rem - HECLIB_FOLDER environment variable is used in cdss-app-tstool-main run configuration to find
rem   dlls for HEC DSS support

rem TODO smalers 2017-03-11 Need to evaluate how to handle 64-bit and Linux versions of libraries
rem SET HECLIB_FOLDER=%USERPROFILE%\cdss-dev\TSTool\git-repos\cdss-lib-processor-ts-java\lib\heclib\win32
SET HECLIB_FOLDER=%USERPROFILE%\cdss-dev\TSTool\git-repos\cdss-lib-processor-ts-java\lib\heclib

rem Run Eclipse
rem - the following specifically sets the VM location, which works fine if developers follow that convention
rem - TODO smalers 2017-03-18 could change this script to fall back to using JAVA_HOME
echo HECLIB_FOLDER=%HECLIB_FOLDER%

rem Set the absolute path to Eclipse program
rem - sort with the most recent last so that the newest supported version is run
rem - this assumes that the developer is using the newest version installed in a known location
rem - additional "standard" installation locations can be added for TSTool developers
set eclipseExe=""
echo Checking for Eclipse in standard locations, oldest supported versions first...
set eclipseTryExe="C:\Program Files (x86)\eclipse-java-neon-2-win32\eclipse\eclipse.exe"
echo Checking %eclipseTryExe%
if exist %eclipseTryExe% set eclipseExe=%eclipseTryExe%
set eclipseTryExe="C:\Program Files (x86)\eclipse-java-neon-3-win32\eclipse\eclipse.exe"
echo Checking %eclipseTryExe%
if exist %eclipseTryExe% set eclipseExe=%eclipseTryExe%
rem The eclipseExe variable already contains surrounding double quotes so don't need to use below
if not exist %eclipseExe% goto noeclipse

rem Also try to find Java 8
rem - this is 32-bit given TSTool is not yet 64-bit
rem - a symbolic link was defined from jdk8 to the specific version to generalize the location
set javawExe=""
echo Checking for Java in standard locations, oldest supported versions first...
set javawTryExe="c:\Program Files (x86)\Java\jdk8\bin\javaw.exe"
echo Checking %javawTryExe%
if exist %javawTryExe% set javawExe=%javawTryExe%
rem The javawExe variable already contains surrounding double quotes so don't need to use below
if not exist %javawExe% goto nojavaw

rem If here, run Eclipse using the executable that is known to exist from above checks
rem - also set the title of the window
echo Starting Eclipse using %eclipseExe%
title Eclipse configured for TSTool development
rem The -data option must come before -vm, etc.
%eclipseExe% -data %workspaceFolder% -vm %javawExe% -vmargs -Xmx1024M
rem Tried the following when Eclipse fails to start
rem %eclipseExe% -data %workspaceFolder% -vm %javawExe% -vmargs -Xmx1024M -consoleLog
rem %eclipseExe% -data %workspaceFolder% -vm %javawExe% -vmargs -Xmx1024M -clean -clearPersistedState
goto end

:noeclipse
rem Expected Eclipse (eclipse.exe) was not found
echo Eclipse was not found using expected locations.
echo Update the run script to find Eclipse or run a different script.
exit /b 1

:nojavaw
rem Expected Java (javaw.exe) was not found
echo Java was not found using expected locations.
echo Update the run script to find Java or run a different script.
exit /b 1

rem Successfully found Eclipse and Java to run
:end
