@echo off
rem ---------------------------------------------------------------------------
rem Batch Program to run TSTool via the JRE
rem ---------------------------------------------------------------------------
rem Notes:
rem  1) This batch file is NOT normally used to run TSTool.
rem     Instead, use the TSTool.exe program created by Launch4j,
rem     which provides a more integrated start-up.
rem     Use this batch file for troubleshooting installation issues.
rem     Because the batch file is not used in production,
rem     it may be out of date compared with recent development.
rem
rem  1) This batch file is not updated during the installation process.
rem     It finds files relative to the batch file.
rem
rem  3) TSTool command line parameters can be provided and will be passed to the software.
rem ---------------------------------------------------------------------------
rem 
rem Guidelines for setting HOMED and JREHOMED:
rem
rem Batch file home:
rem - the path will contain the trailing \
rem - this is also the "bin" folder where jar files can be found
SET "SCRIPT_FOLDER=%~dp0"

rem Other locations are relative to the batch file folder:
rem - JAVA_BIN_FOLDER - the location of the JRE bin files
rem - HOME_FOLDER = the main installation folder
SET JRE_BIN_FOLDER=%SCRIPT_FOLDER%..\jre_11\bin
SET HOME_FOLDER=%SCRIPT_FOLDER%..

rem Main class to launch the software.
SET MAIN_CLASS=DWR.DMI.tstool.TSToolMain

rem Class path (all files are in the bin folder):
rem - the script folder includes the trailing backslash
SET TSTOOL_CP=%SCRIPT_FOLDER%*

rem Echo configuration variables.
echo SCRIPT_FOLDER="%SCRIPT_FOLDER%"
echo JRE_BIN_FOLDER="%JRE_BIN_FOLDER%"
echo HOME_FOLDER="%HOME_FOLDER%"
echo TSTOOL_CP="%TSTOOL_CP%"

rem Run the Java Runtime Environment (JRE), which runs the TSTool software:
rem - pass all the command line parameters
echo Running: "%JRE_BIN_FOLDER%\java" -Xmx2048m -cp "%TSTOOL_CP%" "%MAIN_CLASS%" --home "%HOME_FOLDER%" %1 %2 %3 %4 %5 %6 %7 %8 %9
"%JRE_BIN_FOLDER%\java" -Xmx2048m -cp "%TSTOOL_CP%" "%MAIN_CLASS%" --home "%HOME_FOLDER%" %1 %2 %3 %4 %5 %6 %7 %8 %9
goto end

rem Clean up the temporary environment variables so that memory is freed for other applications.
:end
set SCRIPT_FOLDER=
set JAVA_BIN_FOLDER=
set HOME_FOLDER=
set MAIN_CLASS=
set TSTOOL_CP=
