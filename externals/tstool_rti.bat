@echo off
rem ---------------------------------------------------------------------------
rem Batch Program to run the TSTool as a stand alone application using JRE 118
rem ---------------------------------------------------------------------------
rem Notes:	(1)	This batch file is updated during the installation
rem			process, with the environment settings taken from
rem			the installation process.
rem
rem		(2)	Because tstool can be executed in batch mode, jre must
rem			be used (instead of jrew) so that the batch screen
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
rem ---------------------------------------------------------------------------

rem Set the temporary environment variables (configured at install time)...

SET ARCHIVE_DBHOST=hbserver
SET DBHOST=hbserver
rem Use the following if a local Access database should be the default...
SET DBHOST=localpc
SET HOMED=\CDSS
SET JREHOMED=\CDSS\JRE_118

%JREHOMED%\bin\jre -mx128m -cp %HOMED%\bin\TSTool_118.jar;%HOMED%\bin\HB_118.jar;%HOMED%\bin\HBGUI_118.jar;%HOMED%\RiversideDB_DMI_118.jar;%HOMED%\bin\Informix_118.jar;%HOMED%\msbase.jar;%HOMED%\mssqlserver.jar;%HOMED%\msutil.jar;%HOMED%\bin\Una2000_118.jar;%HOMED%\bin\RTi_118.jar;%HOMED%\bin\SMutil_118.jar;%HOMED%\bin\Symantec_118.jar DWR.DMI.tstool.tstool -home %HOMED% -dbhost %DBHOST% -archive_dbhost %ARCHIVE_DBHOST% %1 %2 %3 %4 %5 %6 %7 %8 %9

rem Clean up the temporary environment variables so that memory is freed for
rem other applications...

set ARCHIVE_DBHOST=
set DBHOST=
set HOMED=
set JREHOMED=
