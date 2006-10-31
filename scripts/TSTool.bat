@echo off
rem ---------------------------------------------------------------------------
rem Batch Program to run the TSTool as a stand alone application using JRE 142
rem ---------------------------------------------------------------------------
rem Notes:	(1)	This batch file is updated during the installation
rem			process, with the environment settings taken from
rem			the installation process.
rem
rem		(2)	Because tstool can be executed in batch mode, java must
rem			be used (instead of javaw) so that the batch screen
rem			output is visible.  However, this will cause a DOS
rem			window to appear when running the GUI.
rem ---------------------------------------------------------------------------
rem History:
rem
rem 26 May 1998	Steven A. Malers,	Update to handle database configuration.
rem		Chad G. Bierbaum,
rem		Riverside Technology,
rem		inc.
rem 09 Dec 1998	SAM, RTi		Update to clean up environment
rem					variables after exiting.
rem 28 May 1999	SAM, RTi		Update for Java 1.1.8 release using
rem					JAR files.
rem 24 Jul 2000	SAM, RTi		Add -mx option to help with memory
rem					problems for large data sets.
rem 25 Oct 2000	CEN, RTi		Adding Una2000.jar
rem 2002-09-30	SAM, RTi		Add the JRE version.  Remove the BROWSER
rem					and DOCHOME settings since they are
rem					determined at run time.
rem 2002-10-11	SAM, RTi		Add separate RiversideDB_DMI Jar.
rem 2003-01-08	SAM, RTi		Add jar files for Microsoft SQL Server
rem					driver (ms*.jar).
rem 2003-12-02	SAM, RTi		Update for Java 142.
rem 2004-02-19	SAM, RTi		Add jar file for StateCU support.
rem					Add quotes around command-line
rem					parameters to allow use with directories
rem					that contain spaces.
rem 2004-06-01	SAM, RTi		* Add additional comments explaining
rem					  the configuration variables.
rem					* Remove Una2000.jar file reference in
rem					  favor of the Microsoft-supplied
rem					  jar files.
rem 2005-08-03	SAM, RTi		* Change java -mx256m option to
rem					  -Xmx256m.
rem					* If -commands is one of the first 3
rem					  options then run using java, else run
rem					  using javaw.
rem ---------------------------------------------------------------------------

rem The HOMED and JREHOMED variables are set during the installation process.
rem The names are hopefully unique enough that they do not conflict with other
rem software settings.
rem 
rem Guidelines for setting HOMED and JREHOMED:
rem
rem 1)If software is installed on a server and the location is consistently
rem   mapped to the same drive letter for all users and computers, specify an
rem   absolute path to the location.  This allows the software to be run from
rem   other drives and still find the install location for configuration and
rem   log files.  For example, use:
rem
rem      HOMED=P:\CDSS
rem      JREHOMED=P:\CDSS\jre_142
rem
rem   It is also possible to use a UNC (Universal Naming Convention) to indicate
rem   the installation point.  This can be used when a drive letter is not
rem   always mapped but a machine provides access to the software files.  For
rem   example:
rem
rem      HOMED=\\CDSSServer\CDSS
rem      JREHOMED=\\CDSSServer\CDSS\jre_142
rem
rem 2)If software is installed on a local drive (e.g., C:) or a CD drive
rem   (e.g., D:), and the software will ALWAYS be run from the drive where the
rem   software are installed, the drive letter can be omitted from the
rem   variables.  This allows the software to be installed/copied generically
rem   without further configuration.  If the software is run from a different
rem   drive, the software may not be able to find supporting files or open a
rem   log file.  For example, use:
rem
rem	HOMED=\CDSS
rem	JREHOMED=\CDSS\jre_142
rem
rem 3)If the software is installed on a local drive (e.g., C:) but may be run
rem   from a different drive, use approach (1).

SET HOMED=\CDSS
SET JREHOMED=\CDSS\jre_142

rem Run the Java Runtime Environment (JRE), which runs the TSTool software.
rem If there is an error, try using "java" instead of "javaw" below.  Using
rem "java" will not hide the command shell window and therefore allows more
rem troubleshooting.

rem If -commands is used, then TSTool is being run in batch mode so use "java"
rem to show the console output.  If -commands is not used, use "javaw" to hide
rem the console output from the interactive session.

if "%1" == "-commands" goto batch
if "%2" == "-commands" goto batch
if "%3" == "-commands" goto batch
goto interactive

rem Run in batch mode with output to console...
:batch
"%JREHOMED%\bin\java" -Xmx256m -cp "%HOMED%\bin\TSTool_142.jar;%HOMED%\bin\HydroBaseDMI_142.jar;%HOMED%\bin\RiversideDB_DMI_142.jar;%HOMED%\bin\NWSRFS_DMI_142.jar;%HOMED%\bin\msbase.jar;%HOMED%\bin\mssqlserver.jar;%HOMED%\bin\msutil.jar;%HOMED%\bin\RTi_142.jar;%HOMED%\bin\StateMod_142.jar;%HOMED%\bin\StateCU_142.jar" DWR.DMI.tstool.tstool -home "%HOMED%" %1 %2 %3 %4 %5 %6 %7 %8 %9
goto end

rem Run in interactive mode with no output to console...
:interactive
"%JREHOMED%\bin\javaw" -Xmx256m -cp "%HOMED%\bin\TSTool_142.jar;%HOMED%\bin\HydroBaseDMI_142.jar;%HOMED%\bin\RiversideDB_DMI_142.jar;%HOMED%\bin\NWSRFS_DMI_142.jar;%HOMED%\bin\msbase.jar;%HOMED%\bin\mssqlserver.jar;%HOMED%\bin\msutil.jar;%HOMED%\bin\RTi_142.jar;%HOMED%\bin\StateMod_142.jar;%HOMED%\bin\StateCU_142.jar" DWR.DMI.tstool.tstool -home "%HOMED%" %1 %2 %3 %4 %5 %6 %7 %8 %9
goto end

rem Clean up the temporary environment variables so that memory is freed for
rem other applications...

:end
set HOMED=
set JREHOMED=
