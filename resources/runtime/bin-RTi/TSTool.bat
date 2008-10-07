@echo off
rem ---------------------------------------------------------------------------
rem Batch Program to run the TSTool
rem ---------------------------------------------------------------------------
rem Notes:	(1)	This batch file is updated during the installation
rem			process, with the environment settings taken from
rem			the installation process.
rem
rem		(2)	Because tstool can be executed in batch mode, java must
rem			be used (instead of javaw) so that the batch screen
rem			output is visible.  However, this will cause a DOS
rem			window to appear when running the GUI.
rem
rem		(3)	It is recommended that the TSTool.exe launcher be used
rem			instead of this batch file.  However, this batch file
rem			can be used during troubleshooting.
rem ---------------------------------------------------------------------------
rem The HOMED variable is set during the installation process to indicate
rem the software installation home, under which will be bin, doc, logs,
rem jre_142, system, etc.
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

rem Install home (replaced at install using user selection)
SET HOMED=C:\Program Files\RTi\TSTool-08.17.00
SET JREHOMED=%HOMED%\jre_142
SET MAIN_CLASS=rti.app.tstool.TSTool
rem Class path (replaced at software build usin current software dependencies).
SET TSTOOL_CP="%HOMED%\bin\batik-awt-util.jar;%HOMED%\bin\batik-dom.jar;%HOMED%\bin\batik-ext.jar;%HOMED%\bin\batik-svggen.jar;%HOMED%\bin\batik-util.jar;%HOMED%\bin\batik-xml.jar;%HOMED%\bin\cdss.domain_142.jar;%HOMED%\bin\jcommon.jar;%HOMED%\bin\jfreechart.jar;%HOMED%\bin\jsr173_1.0_api.jar;%HOMED%\bin\jython.jar;%HOMED%\bin\libXMLJava.jar;%HOMED%\bin\TSTool_142.jar;%HOMED%\bin\TSCommandProcessor_142.jar;%HOMED%\bin\HydroBaseDMI_142.jar;%HOMED%\bin\RiversideDB_DMI_142.jar;%HOMED%\bin\NWSRFS_DMI_142.jar;%HOMED%\bin\msbase.jar;%HOMED%\bin\mssqlserver.jar;%HOMED%\bin\msutil.jar;%HOMED%\bin\RTi_Common_142.jar;%HOMED%\bin\StateMod_142.jar;%HOMED%\bin\Blowfish_142.jar;%HOMED%\bin\SatmonSysDMI_142.jar;%HOMED%\bin\xbean.jar;%HOMED%\bin\xerces.jar;%HOMED%\bin\h2.jar"

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
if "%4" == "-commands" goto batch
if "%5" == "-commands" goto batch
if "%6" == "-commands" goto batch
if "%7" == "-commands" goto batch
if "%8" == "-commands" goto batch
if "%9" == "-commands" goto batch
goto interactive

rem Run in batch mode with output to console...
:batch
"%JREHOMED%\bin\java" -Xmx768m -cp %TSTOOL_CP% %MAIN_CLASS% -home "%HOMED%" %1 %2 %3 %4 %5 %6 %7 %8 %9
goto end

rem Run in interactive mode with no output to console...
:interactive
"%JREHOMED%\bin\javaw" -Xmx768m -cp %TSTOOL_CP% %MAIN_CLASS% -home "%HOMED%" %1 %2 %3 %4 %5 %6 %7 %8 %9
goto end

rem Clean up the temporary environment variables so that memory is freed for
rem other applications...

:end
set HOMED=
set JREHOMED=
set MAIN_CLASS=
set TSTOOL_CP=
