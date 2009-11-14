//------------------------------------------------------------------------------
// TSTool - Main program class for TSTool
//------------------------------------------------------------------------------
// Copyright:	See the COPYRIGHT file.
//------------------------------------------------------------------------------
// History:
//
// 23 Oct 1997	Daniel Weiler, RTi	Created initial class description.
// 07 Apr 1998	Steven A. Malers, RTi	Revisited code to handle current
//					time series identifiers.
// 06 Oct 1998	SAM, RTi		Add units conversion data.
// 31 Dec 1998	SAM, RTi		Update for 3.07.  Extend the
//					-fillhistave option from frost dates to
//					monthly data (omit daily data for now).
// 07 Jan 1999	SAM, RTi		Update to 3.08.  Make the averaging
//					period apply to frost dates too.  Also
//					implement the add() function for frost
//					dates.
// 27 Jan 1999	SAM, RTi		Update to 3.09.  React to feedback from
//					users.  Implement the -datasource
//					option.  Move the Data Source to the
//					top of the list since it controls other
//					options.
// 23 Feb 1999	SAM, RTi		Update to version 4.00 to reflect Java
//					1.2 upgrade.
// 09 Mar 1999	SAM, RTi		Back down to 3.10 to deal with some
//					feedback from Boyle.  Had not really
//					made any changes for 4.00.
// 05 Apr 1999	SAM, RTi		Go back up to 4.00 and add HBDMI to
//					queries.
// 20 May 1999	SAM, RTi		Add -detailedheader to help.
// 22 Jul 1999	SAM, RTi		Add -rti for RTi enhancements.
// 03 Aug 1999	SAM, RTi		Update version for initial release.
// 28 Oct 1999	SAM, RTi		Add feature where data <= 0 can be
//					ignored for averages.
// 27 Jan 2000	SAM, RTi		Add -missing Value1,Value2.
// 19 Mar 2000	SAM, RTi		Initialize messaging levels here.
//					Changes to HBParse make this necessary.
// 21 Jul 2000	SAM, RTi		Update to include new list and binary
//					options.
// 25 Sep 2000	SAM, RTi		Update to have the -include_missing_ts
//					option.
// 11 Dec 2000	SAM, RTi		Remove -rti option.  Features are now
//					in by default.
// 07 Jan 2001	SAM, RTi		Change IO to IOUtil.
// 11 Jan 2001	SAM, RTi		Enable running as an applet with only a
//					canvas.
// 15 Mar 2001	SAM, RTi		Update to 05.01.00 to clean up
//					functionality.  Use IOUtil.isBatch()
//					rather than local variable.
// 05 Apr 2001	SAM, RTi		Update to 05.02.00.  Start a follow up
//					release to deal with cleanup and
//					selective feature enhancements.
// 22 Aug 2001	SAM, RTi		Add runProgram() command to run an
//					external process.  Enable automated
//					graph creation.
// 13 Sep 2001	SAM, RTi		Change version to 05.02.00 2001-09-13
//					and make an official release.
// 23 Sep 2001	SAM, RTi		Update to version 05.02.01.
// 11 Nov 2001	SAM, RTi		Update to version 05.03.00.  Enable
//					PeriodOfRecord graph in RTi tools so the
//					Visualize package can be removed.
//					Set the program name and version when
//					an applet.
// 2002-01-10	SAM, RTi		Use a configuration file if found in the
//					system directory.  Use information in
//					the configuration file to enable/disable
//					RTi features.
//					Add -server option to start in server
//					mode.  This allows batch processing of
//					commands.
// 2002-02-17	SAM, RTi		Strip out most of the printUsage() text
//					since the command list is so long and
//					is now fully documented in the program
//					documentation.  Update to version
//					05.04.00 since so many changes have
//					acrued.
// 2002-03-22	SAM, RTi		Update to version 05.05.00.  This
//					version includes more detailed features
//					for data filling and analysis to support
//					TVA, BPA, CDSS, and NWSRFS calibration.
// 2002-04-03	SAM, RTi		Update to version 05.05.01 for a minor
//					update to correct some MOVE2 analysis
//					problems.
// 2002-04-04	SAM, RTi		Update to version 05.05.02 for a minor
//					update to re-enable OLS regression,
//					which had been broken before due to
//					MOVE2 changes.
// 2002-04-16	SAM, RTi		Update to version 05.05.03 to include
//					shiftTimeByInterval() and improved
//					handling of DateValue files (aliases,
//					etc.).
// 2002-04-17	SAM, RTi		Update to version 05.05.04 to include
//					ARMA.
// 2002-04-18	SAM, RTi		Update to version 05.05.05 to include
//					disaggregate().
// 2002-04-19	SAM, RTi		Update to version 05.05.06 to fix ARMA
//					interval bug.
// 2002-04-22	SAM, RTi		Update to version 05.05.07.  Includes
//					cosmetic cleanup for dialogs to do
//					"final" documented release to TVA and
//					State of CO.
// 2002-04-23	SAM, RTi		Update to version 05.05.08.  Fix bug
//					that caused extra time series to list.
// 2002-04-23	SAM, RTi		Update to version 05.05.09.  More
//					cosmetic cleanup.
// 2002-04-26	SAM, RTi		Update to version 05.05.10.  Fix 
//					readTimeSeries() to handle spaces in
//					file names.
// 2002-04-26	SAM, RTi		Update to version 05.05.11.  Fix the
//					File...Process TS Product to work again
//					(broke in 05.05.09).
// 2002-04-29	SAM, RTi		Update to version 05.05.12.  Expand the 
//					ARMA features.  Add left and right
//					legend position to graphs.
// 2002-05-01	SAM, RTi		Update to version 05.05.13.  Allow
//					ARMA() to use ARMA interval greater
//					than the data interval.
// 2002-05-08	SAM, RTi		Update to version 05.05.14 - minor
//					cosmetic changes based on a review of
//					the documentation.
// 2002-05-12	SAM, RTi		Update to version 05.06.00 - add
//					readNwsCard(), writeNwsCard().
//					Clean up so log files are always created
//					in this class, not the TSToolMainGUI or
//					the TSEngine.  Take out the image
//					generation code.  It can be done with
//					commands and time series product
//					configuration files.  Add support for
//					-nomaingui option to only show the graph
//					window.
// 2002-06-06	SAM, RTi		Update to version 05.06.01.  Add new
//					monthly summary reports under Tools.
// 2002-06-06	SAM, RTi		Update to version 05.06.02.  Allow
//					RiverWare time series to be saved from
//					the File menu.  Try to finalize the
//					writeRiverWare() command.  Set the
//					working directory when a commands file
//					is read/written.
// 2002-06-13	SAM, RTi		Update to version 05.06.03.  Fix a bug
//					where incomplete DateValue files were
//					not being properly handled.
//					Add MODSIM output file support.
// 2002-06-26	SAM, RTi		Update to version 05.06.04.  Add
//					initial RiversideDB support to actually
//					read time series.  Only support in the
//					GUI for now.
// 2002-07-12	SAM, RTi		Update to version 05.06.05.  Add
//					File...Set Working Directory.
// 2002-07-25	SAM, RTi		Update to version 05.06.06.  Fix a bug
//					in writeRiverWare().
// 2002-08-26	SAM, RTi		Add addConstant().
// 2002-08-27	SAM, RTi		Update to version 05.06.07.  Change
//					day_to_month_reservoir() to
//					newEndOfMonthTSFromDayTS() and make
//					consistent with the new functionality.
// 2003-01-08	SAM, RTi		Update to version 05.06.08.  Rework the
//					RiversideDB connection and clean up some
//					HydroBase connection features in the
//					GUI.
// 2003-01-09	SAM, RTi		Update to version 05.07.00.  Enable the
//					handling of license properties in the
//					TSTool.cfg file.  From this version
//					forward a TSTool.cfg file with license
//					information is required.  Update the
//					directories searched for documentation
//					for the new directory convention.
// 2003-03-12	SAM, RTi		Update to version 05.08.00.
//					* Add "StateModX" as an input type for
//					  statemod output.
// 2003-04-17	SAM, RTi		Update to version 05.08.01.
//					* Fix bug where DIADvisor rain sensors
//					  were not listing out Data Value 2.
// 2003-05-17	SAM, RTi		Update to version 05.08.02.
//					* Add Intercept property for regression.
// 2003-06-11	SAM, RTi		Update to version 06.00.00.
//					* Start converting AWT to Swing.
//					* Use the new TS package (e.g., DateTime
//					  instead of TSDate).
//					* Use HydroBaseDMI instead of HBDMI.
//					* Remove the batch mode operation from
//					  here.  It will be handled from the
//					  GUI if necessary.
// 2003-12-02	SAM, RTi		* Update to version 06.00.01 for beta
//					  delivery to the State.
// 2003-12-04	SAM, RTi		* Update to version 06.00.02 for next
//					  beta release.
//					* Enable 10+ more command editors.
//					* Fix bug where DATAUNIT file was not
//					  getting read.
// 2003-12-18	SAM, RTi		Update to version 06.00.03 for next
//					beta release.
//					* All command dialogs that are supported
//					  have been enabled - others will be
//					  phased out or updated.
// 2004-01-05	SAM, RTi		Update to version 06.00.04 for next
//					beta release.
//					* Enable input filters for HydroBase.
// 2004-01-07	SAM, RTi		Update to version 06.00.05 for next
//					beta release.
//					* Change run buttons to run all commands
//					  and run selected commands.
// 2004-01-14	SAM, RTi		Update to version 06.00.06 for next
//					beta release.
//					* Allow well time series to be read
//					  using the USGS or USBR ID.
// 2004-01-15	SAM, RTi		Update to version 06.00.07 for beta
//					release.
//					* Enabled writeStateMod() command!
//					* Other enhancements documented in
//					  TSTool_JFrame and TSEngine.
// 2004-01-31	SAM, RTi		Update to version 06.00.08 for beta
//					release.
//					* Enable writing StateMod daily files.
//					* Other enhancements documented in
//					  TSTool_JFrame and TSEngine.
// 2004-02-06	SAM, RTi		Update to version 06.00.09 for beta
//					release.
//					* Enable reading StateCU time series
//					  files.
//					* Enable HydroBase
//					  agricultural_crop_statistics.
//					* Re-enable -d #,# command line option.
//					* Re-enable -commands and batch mode.
//					* Other enhancements documented in
//					  TSTool_JFrame and TSEngine.
// 2004-02-20	SAM, RTi		Update to version 06.01.00 for official
//					release.
//					* Other enhancements documented in
//					  TSTool_JFrame and TSEngine.
// 2004-03-02	SAM, RTi		Update to version 06.02.00 for official
//					release.
//					* Includes a number of minor bug fixes
//					  in the GUI and supporting libraries.
// 2004-03-28	SAM, RTi		Update to version 06.03.00 for official
//					release.
//					* Put code to set the icon in the
//					  setIcon() method, so that it can be
//					  called from the GUI after checking the
//					  license information.
// 2004-04-02	SAM, RTi		Update to version 06.04.00.
//					* StateCU frost dates were not being
//					  read.
// 2004-04-14	SAM, RTi		Update to version 06.05.00.
//					* Change ESPTraceEnsemble to
//					  NWRSFS_ESPTraceEnsemble.
//					* Fix problems where TSTool was not
//					  reading RiversideDB properly.
// 2004-04-23	SAM, RTi		Update to version 06.06.00.
//					* Fix problem in readDateValue_JDialog.
//					* Fix problem reading NWS Card files.
// 2004-05-24	SAM, RTi		Update to version 06.07.00.
//					* Officially release ESP trace ensemble
//					  support.
//					* Add WIS time series support.
//					* Update to Microsoft drivers for DMI.
//					* Other miscellaneous cleanup.
// 2004-07-11	SAM, RTi		Update to version 06.08.00.
//					* Enhancements to read StateCU IWR/WSL
//					  files with a wildcard.
//					* Enhancements to read StateModB files
//					  with a wildcard on the location.
//					* See TSEngine for more changes.
// 2004-07-20	SAM, RTi		Update to version 06.08.01.
//					* Use new default database OdbcDsn
//					  from the config file.
// 2004-07-27	SAM, RTi		Update to version 06.08.02.
//					* Minor corrections based on
//					  documentation review.
// 2004-08-08	SAM, RTi		* Add code to not load icons when
//					  running in -release mode, to test
//					  Jar files.
// 2004-08-27	SAM, RTi		Update to version 06.09.00.
//					* Add readHydroBase() command.
// 2004-09-01	SAM, RTi		Update to version 06.09.01.
//					* Add initial NWSRFS FS5Files support -
//					  more support in the next release.
//					* Fix summary reports (day totals and
//					  day mean) to handle minute data.
// 2004-09-13	SAM, RTi		Final changes to NWSRFS FS5Files code
//					before release to BPA.
// 2004-10-05	SAM, RTi		Update to version 06.09.02.
//					* Fix bug in NWSRFS FS5Files support
//					  where IDs with _ were mishandled.
//					* Add complete notes for StateModB
//					  data types.
//					* Other minor maintenance.
// 2004-11-17	SAM, RTi		* Fix bug where -release was not being
//					  parsed as a command line argument.
//					* Fix bug parsing "-d10", etc.
// 2004-12-21	SAM, RTi		Update to version 06.09.03.
//					* Fix bug where setting the initial
//					  working directory when processing
//					  commands had errors if the path
//					  included spaces.
// 2005-02-16	SAM, RTi		Update to version 06.10.00.
//					* Enable changeInterval() that is more
//					  generic, if limited in capability.
//					* Begin work on mixed station model
//					  enhancements.
//					* Start using the new message log
//					  viewer with tags.
//					* Change to not display messages to the
//					  terminal by default, in order to
//					  increase performance.
// 2005-06-01	SAM, RTi		Change the version to 06.10.00 BETA to
//					reflect the limited release.
// 2005-06-03	SAM, RTi		Change the version to 06.10.01 BETA.
//					* Enable full data flags for daily and
//					  monthly data.
// 2005-06-08	J. Thomas Sapienza, RTi	Added a call to setApplicationHomeDir().
// 2005-06-28	SAM, RTi		Change the version to 6.10.02 BETA.
// 2005-07-08	SAM, RTi		Change the version to 6.10.03 BETA for
//					State testing.
// 2005-07-20	SAM, RTi		Change the version to 6.10.04 for
//					release.
// 2005-08-01	SAM, RTi		Change the version to 6.10.05 for
//					release.
// 2005-08-04	SAM, RTi		Change the version to 6.10.06 for
//					release.
// 2005-08-24	SAM, RTi		Change the version to 6.10.07 for
//					release.
//					* Additional conversion to named
//					  parameter commands.
// 2005-09-07	SAM, RTi		Change the version to 6.10.08.
//					* Additional conversion to named
//					  parameter commands.
// 2005-09-28	SAM, RTi		Change the version to 6.10.09.
//					* Additional conversion to named
//					  parameter commands.
// 2005-10-05	SAM, RTi		Change the version to 6.11.00.
//					* Begin adding support for alert
//					  annotations.
//					* Fix bug where if a commands file is
//					  specified in batch mode using a
//					  relative path, the working directory
//					  was not getting set correctly.
//					* Add ability to accept "=" properties
//					  on the command line, to supplement
//					  configuration file information.
// 2005-10-05	SAM, RTi		Change the version to 6.12.00.
//					* Improve error handling for time series
//					  products, in particular to cause a
//					  non-zero exit status if there is a
//					  data problem.
// 2005-11-13	SAM, RTi		Change the version to 6.13.00.
//					* Check the Jar file for normal/extended
//					  code and disable features if
//					  necessary.
// 2005-12-14	SAM, RTi		Change the version to 6.14.00.
//					* Change setQueryPeriod() to
//					  setInputPeriod() and allow backward
//					  compatibility.
//					* Change the copyright to include 2006.
//					* Combine readNwsCard() commands to use
//					  one command class and dialog and add
//					  the new Read24HourCommand parameter.
// 2006-01-16	SAM, RTi		Change the version to 6.15.00.
//					* Implement dynamic link to map
//					  interface.
//					* readNwsCard() and write*ESP*()
//					  commands have been moved to the
//					  NWSRFS_DMI package.
// 2006-01-31	SAM, RTi		Change the version to 6.16.00.
//					* Add the NDFD input type.
// 2006-02-16	SAM, RTi		Change the version to 6.16.01.
//					* Make some changes in the GUI to
//					  minimize GUI initialization when no
//					  main GUI is shown.
//					* Make changes in utility code to handle
//					  UNC for the application home.
//					* Finalize initial support for the map
//					  interaction.
// 2006-04-17	SAM, RTi		Change the version to 6.16.02.
//					* Initial data test commands are
//					  included (under development).
//					* Enable fillMOVE2(), which was
//					  accidentally commented out during
//					  previous updates.
// 2006-04-17	SAM, RTi		Change the version to 6.17.00.
//					* Add compareFiles() to help with
//					  unit and regression testing.
// 2006-05-02	SAM, RTi		Change the version to 6.18.00
//					* Add runCommands() to facilitate
//					  automated regression testing.
// 2006-05-19	SAM, RTi		Change the version to 6.19.00.
//					* Update fillUsingDiversionComments() to
//					  automatically extend the period if
//					  no query or output period is given.
// 2006-06-15	SAM, RTi		Change the version to 6.20.00.
//					* Add NDFD support, in particular to
//					  manage NDFD adapters.
// 2006-10-31	SAM, RTi		Change the version to 7.00.00.
//					* Begin doing development using the
//					  new development environment.
//					* Begin distributing using the NSIS
//					  build process.
//					* Add HydroBase livestock and human
//					  population time series.
// 2006-11-07   KAT, RTi        Fixing bug were home argument
//                    wasn't working for relative paths.  Added
//                    some code to get canonical path for home.
// 2007-01-26   KAT, RTi    Found out that if command line argument
//                      -D was used but wasn't given a debug level that
//                      the code was allowing a debug of level one as a
//                      fallback, but was also not checking the args array
//                      bounds before going on.  This was causing an
//                      ArrayOutOfBoundsException() so I added some checks
//                      in the parseArgs() method so this doesn't happen.
// 2007-02-11	SAM, RTi		Update version to 7.01.00.
//					Clean up code based on Eclipse feedback.
// 2007-03-09	SAM, RTi		Update to version 7.02.00.
//					* Work on Mixed Station Model tool and commands.
//					* Add difference time series to compareTimeSeries().
//					* Verify support for old and new versions of StateCU CDS file.
// 2007-04-16	SAM, RTi		Update to version 7.03.00
//					* Verify support for old and new versions of StateCU IPY file.
//------------------------------------------------------------------------------
//EndHeader

package DWR.DMI.tstool;

import java.io.File;
import java.util.List;
import java.util.Vector;

import java.awt.Frame;
import javax.swing.JApplet;
import javax.swing.JFrame;

import rti.tscommandprocessor.core.TSCommandFileRunner;

import DWR.DMI.HydroBaseDMI.HydroBaseDMI;
import DWR.DMI.HydroBaseDMI.HydroBase_Util;

import RTi.DMI.RiversideDB_DMI.RiversideDB_DMI;
import RTi.GRTS.TSViewGraphJFrame;
import RTi.GRTS.TSViewSummaryJFrame;
import RTi.GRTS.TSViewTableJFrame;

import RTi.Util.GUI.JGUIUtil;
import RTi.Util.IO.DataUnits;
import RTi.Util.IO.IOUtil;
import RTi.Util.IO.Prop;
import RTi.Util.IO.PropList;
import RTi.Util.Message.Message;
import RTi.Util.Message.MessageEventQueue;
import RTi.Util.String.StringUtil;

/**
Main (application startup) class for TSTool.  This class will start the TSTool GUI
or run the TSCommandProcessor in batch mode with a command file.  The methods in
this file are called by the startup TSTool and CDSS versions of TSTool.
*/
public class TSToolMain extends JApplet
{
public static final String PROGRAM_NAME = "TSTool";
public static final String PROGRAM_VERSION = "9.05.03 (2009-11-13)";

/**
Main GUI instance, used when running interactively.
*/
private static TSTool_JFrame __tstool_JFrame;

/**
Home directory for system install.
*/
private static String __home = null;

/**
Path to the configuration file.  This cannot be defaulted until the -home command line parameter is processed.
*/
private static String __configFile = "";

/**
List of properties to control the software from the configuration file and passed in on the command line.
*/
private static PropList __tstool_props = null;

/**
Indicates whether TSTool is running in server mode - under development.
*/
private static boolean __is_server = false;

/**
Indicates whether the command file should run after loading, when used in GUI mode.
*/
private static boolean __run_commands_on_load = false;  

/**
Indicates whether the main GUI is shown, for cases where TSTool is run in in batch mode,
with only the plot window shown.  If running in interactive mode the GUI is always shown.
*/
private static boolean __showMainGUI = true;

/**
Indicates whether the -nomaingui command line argument is set.  This is used instead of
just the above to know for sure the combination of command line parameters.
*/
private static boolean __noMainGUIArgSpecified = false;    

/**
Command file being processed when run in batch mode with -commands File.
*/
private static String __commandFile = null;

/**
Return the command file that is being processed, or null if not being run in batch mode.
@return the path to the command file to run.
*/
private static String getCommandFile ()
{
	return __commandFile;
}

/**
Return the name of the configuration file for the session.  This file will be determined
from the -home command line parameter during command line parsing (default) and can be
specified with -config File on the command line (typically used to test different configurations).
@return the full path to the configuration file.
*/
private static String getConfigFile ()
{
    return __configFile;
}

/**
Return the JFrame for the main TSTool GUI.
@return the JFrame instance for use with low-level code that needs to pop up dialogs, etc.
*/
public static JFrame getJFrame ()
{	return (JFrame)__tstool_JFrame;
}

/**
Return a TSTool property.  The properties are defined in the TSTool configuration file.
@param property name of property to look up.
@return the value for a TSTool configuration property, or null if a properties file does not exist.
Return null if the property is not found (or if no configuration file exists for TSTool).
*/
public static String getPropValue ( String property )
{	if ( __tstool_props == null ) {
		return null;
	}
	return __tstool_props.getValue ( property );
}

/**
Indicate whether the commands should be run after loaded into the GUI (when a command
file is specified on the command line).
@return true if the commands should automatically be run after loading.
*/
public static boolean getRunOnLoad ()
{
    return __run_commands_on_load;
}

/**
Instantiates the application instance as an applet.
*/
public void init()
{	String routine = "TSToolMain.init";
	IOUtil.setApplet ( this );
	IOUtil.setProgramData ( PROGRAM_NAME, PROGRAM_VERSION, null );
	// Set up handler for GUI event queue, for exceptions that may otherwise get swallowed by a JRE launcher
    new MessageEventQueue();
    try {
        parseArgs( this );
	}
	catch ( Exception e ) {
        Message.printWarning( 1, routine, "Error parsing command line arguments.  Using default behavior if necessary." );
		Message.printWarning ( 3, routine, e );
    }

    // Instantiate main GUI

	initializeLoggingLevelsAfterLogOpened();

	// Full GUI as applet (no log file)...
	// Show the main GUI, although later might be able to start up just
	// the TSView part via a web site.
	__tstool_JFrame = new TSTool_JFrame ( null, false );
}

/**
Initialize important data and set message levels for application after startup.
*/
private static void initializeLoggingLevelsAfterLogOpened ()
{	// Initialize message levels...
    // FIXME SAM 2008-01-11 Need to have initialize2() reset message levels to not show on the console.
	Message.setDebugLevel ( Message.TERM_OUTPUT, 0 );
	Message.setDebugLevel ( Message.LOG_OUTPUT, 0 );
	Message.setStatusLevel ( Message.TERM_OUTPUT, 0 );
	Message.setStatusLevel ( Message.LOG_OUTPUT, 2 );
	Message.setWarningLevel ( Message.TERM_OUTPUT, 0 );
	Message.setWarningLevel ( Message.LOG_OUTPUT, 3 );

	// Indicate that message levels should be shown in messages, to allow
	// for a filter when displaying messages...

	Message.setPropValue ( "ShowMessageLevel=true" );
	Message.setPropValue ( "ShowMessageTag=true" );
}

/**
Initialize important data and set message levels for console startup.
*/
private static void initializeLoggingLevelsBeforeLogOpened ()
{   // Initialize message levels...
    // FIXME SAM 2008-01-11 Need to have initialize2() reset message levels to not show on the console.
    Message.setDebugLevel ( Message.TERM_OUTPUT, 0 );
    Message.setDebugLevel ( Message.LOG_OUTPUT, 0 );
    Message.setStatusLevel ( Message.TERM_OUTPUT, 2 );
    Message.setStatusLevel ( Message.LOG_OUTPUT, 2 );
    Message.setWarningLevel ( Message.TERM_OUTPUT, 2 );
    Message.setWarningLevel ( Message.LOG_OUTPUT, 3 );

    // Indicate that message levels should be shown in messages, to allow
    // for a filter when displaying messages...

    Message.setPropValue ( "ShowMessageLevel=true" );
    Message.setPropValue ( "ShowMessageTag=true" );
}

/**
Initialize important data relative to the installation home.
*/
private static void initializeAfterHomeIsKnown ()
{	String routine = "TSToolMain.initializeAfterHomeIsKnown";

	// Initialize the system data...

	String units_file = __home + File.separator + "system" + File.separator + "DATAUNIT";

	Message.printStatus ( 2, routine, "Reading the units file \"" +	units_file + "\"" );
	try {
        DataUnits.readUnitsFile( units_file );
	}
	catch ( Exception e ) {
		Message.printWarning ( 2, routine,
		"Error reading units file \"" + units_file + "\"\n" +
		"Some conversions will not be supported.\n" +
		"Default output precisions may not be appropriate." );
	}
}

/**
Indicate whether TSTool is running in server mode.  This feature is under development.
@return true if running in server mode.
*/
public static boolean isServer()
{	return __is_server;
}
    
/**
Start the main application instance.
@param args Command line arguments.
*/
public static void main ( String args[] )
{	String routine = "TSToolMain.main";

	try {
	// Main try...

	initializeLoggingLevelsBeforeLogOpened();
	setWorkingDirInitial ();
	IOUtil.setProgramData ( PROGRAM_NAME, PROGRAM_VERSION, args );
	JGUIUtil.setAppNameForWindows("TSTool");
	
	// Set up handler for GUI event queue, for exceptions that may otherwise get swallowed by a JRE launcher
	new MessageEventQueue();

	// Note that messages will not be printed to the log file until the log file is opened below.

	initializeLoggingLevelsAfterLogOpened();

	try {
        parseArgs ( args );
        // The result of this is that the full path to the command file will be set.
        // Or the GUI will need to start up in the current directory.
	}
	catch ( Exception e ) {
        Message.printWarning ( 1, routine, 
            "Error parsing command line arguments.  Using default behavior if necessary." );
		Message.printWarning ( 3, routine, e );
	}
	
	// Set the icon to RTi's logo by default.  This may be reset later after
    // the license is checked in the GUI.  Do not do this in pure batch mode because it is not
	// needed and may cause problems with X-Windows on UNIX.
	// Do need to load it when -nomaingui is used because the windows that are shown will need
	// to look nice with the icon.

	Message.printStatus( 2, routine, "isBatch=" + IOUtil.isBatch() +
	        " -nomaingui specified = " + __noMainGUIArgSpecified );
	if ( !IOUtil.isBatch() || __noMainGUIArgSpecified ) {
	    // Not "pure" batch so need to have the icon initialized
	    try {
	        setIcon ( "RTi" );
	    }
	    catch ( Exception e ) {
	        // FIXME SAM 2008-08-29 Why doesn't the above work on Linux in batch mode
	        // to avoid trying?
	        Message.printWarning( 2, routine, "Error setting icon graphic." );
	        Message.printWarning( 3, routine, e );
	    }
	}

	// Read the data units...

	initializeAfterHomeIsKnown ();

	Message.printStatus ( 1, routine,
	        "Setup completed.  showmain = " + __showMainGUI + " isbatch=" + IOUtil.isBatch() );
	
	if ( IOUtil.isBatch() ) {
		// Running like "tstool -commands file" (possibly with -nomaingui)
		TSCommandFileRunner runner = new TSCommandFileRunner();
	    // Open the HydroBase connection if the configuration file specifies the information.  Do this before
		// reading the command file because commands may try to run discovery during load.
        openHydroBase ( runner );
        // Open the RiversideDB connection if the configuration file specifies the information.  Do this before
        // reading the command file because commands may try to run discovery during load.
        openRiversideDB ( runner );
		try {
		    String command_file_full = getCommandFile();
		    Message.printStatus( 1, routine, "Running command file in batch mode:  \"" + command_file_full + "\"" );
			runner.readCommandFile ( command_file_full );
		}
		catch ( Exception e ) {
			Message.printWarning ( 1, routine, "Error reading command file \"" +
					getCommandFile() + "\".  Unable to run commands." );
			Message.printWarning ( 1, routine, e );
			quitProgram ( 1 );
		}
		// If running with -nomaingui, then plot windows should be displayed and when closed cause the
		// run to end - this should be used with external applications that use TSTool as a plotting tool
		if ( !__showMainGUI ) {
		    // Create a hidden listener to handle close-out of the application when a plot window is closed.
		    Message.printStatus(2,routine, "Displaying plots with no main GUI.");
		    TSToolBatchWindowListener windowListener = new TSToolBatchWindowListener();
		    runner.getProcessor().setPropContents("TSViewWindowListener",windowListener);
		}
		try {
		    // The following will throw an exception if there are any errors running.
            runner.runCommands();
            if ( __showMainGUI ) {
                // No special handling of windows since -nomaingui was not not specified.  just exit.
                quitProgram ( 0 );
            }
            else {
                // Not showing the main GUI.  Exit here if there are no plot windows - otherwise
                // will hang.  If windows are found, let the GUI WindowListener handle when to close the
                // application.
                Frame [] frameArray = Frame.getFrames();
                int size = 0;
                if ( frameArray != null ) {
                    size = frameArray.length;
                }
                boolean openWindowFound = false;    // Are any windows open?  If no, exit
                // Check for windows that could be open as part of visualization, including
                // the graph, summary, and table windows.  If any are visible, then need to wait
                // until the user closes all.  Then WindowClosing will be called since no more
                // windows are shown.
                for ( int i = 0; i < size; i++ ) {
                    if ( frameArray[i] instanceof TSViewGraphJFrame || frameArray[i] instanceof TSViewSummaryJFrame ||
                        frameArray[i] instanceof TSViewTableJFrame ) {
                        if ( frameArray[i].isVisible() ) {
                            openWindowFound = true;
                            Message.printStatus(2,routine,
                                    "Open, visible window detected.  Waiting for close of window to exit.");
                            break;
                        }
                    }
                }
                // If no open window was found quit.  Otherwise let the TSToolBatchWindowListener
                // handle the close.
                if ( !openWindowFound ) {
                    Message.printStatus(2,routine, "No open, visible windows detected.  Exiting.");
                    quitProgram ( 0 );
                }
            }
		}
		catch ( Exception e ) {
			// Some type of error
			Message.printWarning ( 1, routine, "Error running command file \"" + getCommandFile() + "\"." );
			Message.printWarning ( 1, routine, e );
			quitProgram ( 1 );
		}
	}
	else if ( __is_server ) {
		// Run in server mode via the GUI object.  This goes into a loop...
		//__tstool_JFrame.runServer();
		// FIXME SAM 2007-11-06 Need to pull in XML-RPC or other server hook.
	}
	else {
		// Run the GUI...
		Message.printStatus ( 2, routine, "Starting TSTool GUI..." );
		try {
            __tstool_JFrame = new TSTool_JFrame ( getCommandFile(), getRunOnLoad() );
		}
		catch ( Exception e ) {
			Message.printWarning ( 1, routine, "Error starting TSTool GUI." );
			Message.printWarning ( 1, routine, e );
			quitProgram ( 1 );
		}
	}
	}
	catch ( Exception e2 ) {
		// Main catch.
		Message.printWarning ( 1, routine, "Error starting TSTool." );
		Message.printWarning ( 1, routine, e2 );
		quitProgram ( 1 );
	}
}

/**
Open the HydroBase connection using the CDSS configuration file information, when running
in batch mode.  The CDSS configuration file is used to determine HydroBase server and
database name properties to use for the initial connection.  If no configuration file
exists, then a default connection is attempted.
*/
private static void openHydroBase ( TSCommandFileRunner runner )
{   String routine = "TSToolMain.openHydroBase";
    boolean HydroBase_enabled = false;  // Whether HydroBaseEnabled = true in TSTool config file
    String propval = __tstool_props.getValue ( "TSTool.HydroBaseEnabled");
    if ( (propval != null) && propval.equalsIgnoreCase("true") ) {
        HydroBase_enabled = true;
    }
    if ( !HydroBase_enabled ) {
        Message.printStatus ( 2, routine, "HydroBase is not enabled in TSTool configuation so not opening connection." );
        return; 
    }
    if ( IOUtil.isBatch() ) {
        // Running in batch mode or without a main GUI so automatically
        // open HydroBase from the CDSS.cfg file information...
        // Get the input needed to process the file...
        String hbcfg = HydroBase_Util.getConfigurationFile();
        PropList props = null;
        
        if ( IOUtil.fileExists(hbcfg) ) {
            // Use the configuration file to get HydroBase properties...
            Message.printStatus(2, routine, "HydroBase configuration file \"" + hbcfg +
            "\" is being used to open HydroBase connection at startup." );
            try {
                props = HydroBase_Util.readConfiguration(hbcfg);
            }
            catch ( Exception e ) {
                Message.printWarning ( 1, routine,
                "Error reading CDSS configuration file \""+ hbcfg + "\".  Using defaults for HydroBase." );
                Message.printWarning ( 3, routine, e );
                props = null;
            }
        }
        else {
            Message.printStatus(2, routine, "HydroBase configuration file \"" + hbcfg +
                "\" does not exist - not opening HydroBase connection at startup." );
        }
        
        try {
            // Now open the database...
            // This uses the guest login.  If properties were not found,
            // then default HydroBase information will be used.
            HydroBaseDMI hbdmi = new HydroBaseDMI ( props );
            hbdmi.open();
            List hbdmi_Vector = new Vector(1);
            hbdmi_Vector.add ( hbdmi );
            runner.getProcessor().setPropContents ( "HydroBaseDMIList", hbdmi_Vector );
        }
        catch ( Exception e ) {
            Message.printWarning ( 1, routine, "Error opening HydroBase.  HydroBase features will be disabled." );
            Message.printWarning ( 3, routine, e );
        }
    }
}

/**
Open the RiversideDB connection using the TSTool configuration file information, when running
in batch mode.  The configuration file is used to determine the database server and
name properties to use for the initial connection.
*/
private static void openRiversideDB ( TSCommandFileRunner runner )
{   String routine = "TSToolMain.openRiversideDB";
    boolean RiversideDB_enabled = false;  // Whether RiversideDBEnabled = true in TSTool config file
    String propval = __tstool_props.getValue ( "TSTool.RiversideDBEnabled");
    if ( (propval != null) && propval.equalsIgnoreCase("true") ) {
        RiversideDB_enabled = true;
    }
    if ( !RiversideDB_enabled ) {
        Message.printStatus ( 2, routine, "RiversideDB is not enabled in TSTool configuation so not opening connection." );
        return; 
    }
    String configFile = getConfigFile();
    if ( IOUtil.isBatch() ) {
        // Use the configuration file to get RiversideDB properties...
        Message.printStatus(2, routine, "TSTool configuration file \"" + configFile +
        "\" is being used to open RiversideDB connection at startup." );
        String databaseEngine = getPropValue ( "RiversideDB.DatabaseEngine" );
        String databaseServer = getPropValue ( "RiversideDB.DatabaseServer" );
        String databaseName = getPropValue ( "RiversideDB.Database" );
        // FIXME SAM 2009-06-17 Need to evaluate encryption of password.
        String systemLogin = getPropValue ( "RiversideDB.SystemLogin" ); // OK if null
        String systemPassword = getPropValue ( "RiversideDB.SystemPassword" ); // OK if null
        if ( (databaseEngine == null) || databaseEngine.equals("") ) {
            Message.printWarning ( 1, routine,
            "No RiversideDB.DatabaseEngine property defined in TSTool configuration file \""+ configFile + "\"." +
        		"  Cannot open RiversideDB connection for batch run." );
        }
        if ( (databaseServer == null) || databaseServer.equals("") ) {
            Message.printWarning ( 1, routine,
            "No RiversideDB.DatabaseServer property defined in TSTool configuration file \""+ configFile + "\"." +
                "  Cannot open RiversideDB connection for batch run." );
        }
        if ( (databaseName == null) || databaseName.equals("") ) {
            Message.printWarning ( 1, routine,
            "No RiversideDB.Database property defined in TSTool configuration file \""+ configFile + "\"." +
                "  Cannot open RiversideDB connection for batch run." );
        }
        
        try {
            // Now open the database...
            // This uses the guest login.  If properties were not found,
            // then default HydroBase information will be used.
            RiversideDB_DMI rdmi = new RiversideDB_DMI ( databaseEngine, databaseServer, databaseName,
                    -1, systemLogin, systemPassword );
            rdmi.open();
            runner.getProcessor().setPropContents ( "RiversideDBDMI", rdmi );
        }
        catch ( Exception e ) {
            Message.printWarning ( 1, routine, "Error opening RiversideDB.  RiversideDB features will be disabled (" + e + ")." );
            Message.printWarning ( 3, routine, e );
        }
    }
}

/**
Open the log file.  This should be done as soon as the application home
directory is known so that remaining information can be captured in the log file.
*/
private static void openLogFile ()
{	String routine = "TSToolMain.openLogFile", __logfile = "";
	String user = IOUtil.getProgramUser();

	if ( IOUtil.isApplet() ) {
		Message.printWarning ( 2, routine, "Running as applet - no TSTool log file opened." );
	}
	else {
	    // FIXME SAM 2008-01-11 need to open log file in user space (e.g., /home/$USER/.TSTool/.. on Linux)
	    if ( (__home == null) || (__home.length() == 0) || (__home.charAt(0) == '.')) {
			Message.printWarning ( 2, routine, "Home directory is not defined.  Not opening log file.");
		}
		else {
            if ( (user == null) || user.trim().equals("")) {
				__logfile = __home + File.separator + "logs" + File.separator + "TSTool.log";
			}
			else {
                __logfile = __home + File.separator + "logs" + File.separator + "TSTool_" +	user + ".log";
			}
			Message.printStatus ( 1, routine, "Log file name: " + __logfile );
			try {
                Message.openLogFile ( __logfile );
			}
			catch (Exception e) {
				Message.printWarning ( 1, routine, "Error opening log file \"" + __logfile + "\"");
			}
		}
	}
}

/**
Parse command line arguments.
@param args Command line arguments.
*/
public static void parseArgs ( String[] args )
throws Exception
{	String routine = "TSToolMain.parseArgs";
	int pos = 0;	// Position in a string.

    // Allow setting of -home via system property "tstool.home". This
    // can be supplied by passing the -Dtstool.home=HOME option to the java vm.
    // The following inserts the passed values into the front of the args array to
	// make sure that the install home can be considered by following parameters.
    if (System.getProperty("tstool.home") != null) {
        String[] extArgs = new String[args.length + 2];
        System.arraycopy(args, 0, extArgs, 2, args.length);
        extArgs[0] = "-home";
        extArgs[1] = System.getProperty("tstool.home");
        args = extArgs;
    }

	for (int i = 0; i < args.length; i++) {
		if (args[i].equalsIgnoreCase("-commands")) {
		    // Command file name
			if ((i + 1)== args.length) {
				Message.printWarning(1,routine, "No argument provided to '-commands'");
				throw new Exception("No argument provided to '-commands'");
			}
			i++;
			setupUsingCommandFile ( args[i], true );
		}
		else if (args[i].equalsIgnoreCase("-config")) {
		    // Configuration file name
            if ((i + 1)== args.length) {
                Message.printWarning(1,routine, "No argument provided to '-config'");
                throw new Exception("No argument provided to '-config'");
            }
            i++;
            setConfigFile ( IOUtil.verifyPathForOS(IOUtil.getPathUsingWorkingDir(args[i])) );
            Message.printStatus(1 , routine, "Using configuration file \"" + getConfigFile() + "\"" );
            // Read the configuration file specified here.  Defaults read immediately after -home was
            // parsed will be reset if in both files.
            readConfigFile(getConfigFile());
	    }
		else if ( args[i].regionMatches(true,0,"-d",0,2)) {
			// Set debug information...
			if ((i + 1)== args.length) {
				// No argument.  Turn terminal and log file debug on to level 1...
				Message.isDebugOn = true;
				Message.setDebugLevel ( Message.TERM_OUTPUT, 1);
				Message.setDebugLevel ( Message.LOG_OUTPUT, 1);
			}
			i++;
			if ( (i + 1) == args.length && args[i].indexOf(",") >= 0 ) {
				// Comma, set screen and file debug to different levels...
				String token = StringUtil.getToken(args[i],",",0,0);
				if ( StringUtil.isInteger(token) ) {
					Message.isDebugOn = true;
					Message.setDebugLevel (	Message.TERM_OUTPUT, StringUtil.atoi(token) );
				}
				token=StringUtil.getToken(args[i],",",0,1);
				if ( StringUtil.isInteger(token) ) {
					Message.isDebugOn = true;
					Message.setDebugLevel (	Message.LOG_OUTPUT, StringUtil.atoi(token) );
				}
			}
			else if ( (i + 1) == args.length ) {
			    // No comma.  Turn screen and log file debug on to the requested level...
				if ( StringUtil.isInteger(args[i]) ) {
					Message.isDebugOn = true;
					Message.setDebugLevel (	Message.TERM_OUTPUT, StringUtil.atoi(args[i]) );
					Message.setDebugLevel ( Message.LOG_OUTPUT,	StringUtil.atoi(args[i]) );
				}
			}
		}
		else if (args[i].equalsIgnoreCase("-home")) {
		    // Should be specified in batch file or script that runs TSTool, or in properties for
	        // a executable launcher.  Therefore this should be processed before any user command line
	        // parameters and the log file should open up before much else is done.
			if ((i + 1)== args.length) {
				Message.printWarning(1,routine, "No argument provided to '-home'");
				throw new Exception("No argument provided to '-home'");
			}
			i++;
           
            //Changed __home since old way wasn't supporting relative paths __home = args[i];
            __home = (new File(args[i])).getCanonicalPath().toString();
           
			// Open the log file so that remaining messages will be seen in the log file...
			openLogFile();
			Message.printStatus ( 1, routine, "Home directory for TSTool is \"" + __home + "\"" );
			// The default configuration file location is relative to the install home.  This works
			// as long as the -home argument is first in the command line.
			setConfigFile ( __home + File.separator + "system" + File.separator + "TSTool.cfg" );
			// Don't call setProgramWorkingDir or setLastFileDialogDirectory this since we set to user.dir at startup
			//IOUtil.setProgramWorkingDir(__home);
			//JGUIUtil.setLastFileDialogDirectory(__home);
			IOUtil.setApplicationHomeDir(__home);
			// Also reset the java.library.path system property to include the application
			// home + "/bin" so that DLLs installed with TSTool are found
			String javaLibraryPath = System.getProperty ( "java.library.path" );
			System.setProperty( "java.library.path",
			         __home + "/bin" + System.getProperty("path.separator") + javaLibraryPath );
			Message.printStatus( 2, routine, "Reset java.library.path to \"" +
			        System.getProperty ( "java.library.path" ) + "\"" );
	        // Read the configuration file to get default TSTool properties,
            // so that later command-line parameters can override them...
            readConfigFile(getConfigFile());
		}
		// User specified or specified by a script/system call to the normal TSTool script/launcher.
		else if (args[i].equalsIgnoreCase("-nomaingui")) {
			// Don't make the main GUI visible...
			Message.printStatus ( 1, routine, "Will process command file without main GUI (plot windows only)." );
			__showMainGUI = false;
			__noMainGUIArgSpecified = true;
		}
	    // User specified or specified by a script/system call to the normal TSTool script/launcher.
        else if (args[i].equalsIgnoreCase("-runcommandsonload")) {
            Message.printStatus ( 1, routine, "Will run commands on load." );
            __run_commands_on_load = true;
        }
		// User specified or specified by a script/system call to the normal TSTool script/launcher.
		else if (args[i].equalsIgnoreCase("-server")) {
			Message.printStatus ( 1, routine, "Starting TSTool in server mode" );
			__is_server = true;
		}
		// User specified (generally by developers)
		else if (args[i].equalsIgnoreCase("-test")) {
			IOUtil.testing(true);
			Message.printStatus ( 1, routine, "Running in test mode." );
		}
		// User specified or specified by a script/system call to the normal TSTool script/launcher.
		else if ( (pos = args[i].indexOf("=")) > 0 ) {
			// A command line argument of the form:  Property=Value
			// For example, specify a database login for batch mode.
		    // The properties can be interpreted by the GUI or other code.
			String propname = args[i].substring(0,pos);
			String propval = args[i].substring((pos+1), args[i].length());
			Prop prop = new Prop ( propname, propval );
			Message.printStatus ( 1, routine, "Using run-time parameter " + propname + "=\"" + propval + "\"" );
			prop.setHowSet ( Prop.SET_AT_RUNTIME_BY_USER );
			if ( __tstool_props == null ) {
				// Create a PropList.  This should not normally happen because a PropList should have been
				// read when -home was encountered.
				__tstool_props = new PropList ( "TSTool" );
			}
			__tstool_props.set ( prop );
		}
		// User specified or specified by a script/system call to the normal TSTool script/launcher.
		else {
		    // Assume that a command file has been specified on the command line
		    setupUsingCommandFile ( args[i], false );
		}
	}
}

// TODO - need to make these work as expected.
/**
Parse the command-line arguments for the applet, determined from the applet data.
@param a JApplet for this application.
*/
public static void parseArgs ( JApplet a )
throws Exception
{	
    // Convert the applet parameters to an array of strings and call the other parse method.
    List args = new Vector();
	if ( a.getParameter("-home") != null ) {
	    args.add ( "-home" );
	    args.add ( a.getParameter("-home") );
	}
    if ( a.getParameter("-test") != null ) {
        args.add ( "-test" );
    }
    parseArgs ( (String [])args.toArray() );
}

/**
Print the program usage to the log file.
*/
public static void printUsage ( )
{	String nl = System.getProperty ( "line.separator" );
	String routine = "TSToolMain.printUsage";
	String usage =  nl +
	"Usage:  " + PROGRAM_NAME + " [options] [[-commands CommandFile] | CommandFile]" + nl + nl +
	"TSTool displays, analyzes, and manipulates time series." + nl+
	"" + nl+
	PROGRAM_NAME + " -commands CommandFile" + nl +
	"                Runs the commands in batch mode and exits." + nl+
	PROGRAM_NAME + " -commands CommandFile -nomaingui" + nl +
	"                Runs the commands in batch mode, displays product windows" + nl +
	"                (no main window), and exists when the window(s) are closed." + nl+
	PROGRAM_NAME + " CommandFile" + nl +
	"                Opens up the main GUI and loads the command file." + nl +
	"" + nl+
	"See the documentation for more information." + nl + nl;
	System.out.println ( usage );
	Message.printStatus ( 1, routine, usage );
	quitProgram(0);
}

/**
Print the program version and exit the program.
*/
public static void printVersion ( )
{	String nl = System.getProperty ( "line.separator" );
	System.out.println (  nl + PROGRAM_NAME + " version: " + PROGRAM_VERSION + nl + nl );
	quitProgram (0);
}

/**
Clean up and quit the program.
@param status Program exit status.
*/
public static void quitProgram ( int status )
{	String	routine = "TSToolMain.quitProgram";

	Message.printStatus ( 1, routine, "Exiting with status " + status + "." );

	System.out.print( "STOP " + status + "\n" );
	Message.closeLogFile ();
	System.exit ( status ); 
}

/**
Read the configuration file.  This should be done as soon as the application home is known.
@param configFile Name of the configuration file.
*/
private static void readConfigFile ( String configFile )
{	String routine = "TSToolMain.readConfigFile";
    Message.printStatus ( 2, routine, "Reading TSTool configuration informtion from \"" + configFile + "\"." );
	if ( IOUtil.fileReadable(configFile) ) {
		__tstool_props = new PropList ( configFile );
		__tstool_props.setPersistentName ( configFile );
		try {
            __tstool_props.readPersistent ();
            // Print out the configuration information since it is useful in troubleshooting.
            int size = __tstool_props.size();
            for ( int i = 0; i < size; i++ ) {
                Prop prop = __tstool_props.elementAt(i);
                Message.printStatus( 2, routine, prop.getKey() + "=" + prop.getValue() );
            }
		}
		catch ( Exception e ) {
			Message.printWarning ( 1, routine,
			"Error reading TSTool configuration file \"" + configFile + "\".  TSTool may not start." );
		}
	}
}

/**
Set the command file that is being used with TSTool.
@param configFile Command file being processed, when started with
-commands File parameter.  This indicates that a batch run should be done, with
no main TSTool GUI, although windows may display for graphical products.
*/
private static void setCommandFile ( String configFile )
{
	__commandFile = configFile;
}

/**
Set the configuration file that is being used with TSTool.  If a relative path is
given, then the file is made into an absolute path by using the working directory.
Typically an absolute path is provided when the -home command line parameter is parsed
at startup, and a relative path may be provided if -config is specified on the command
line.
@param configFile Configuration file.
*/
private static void setConfigFile ( String configFile )
{
    __configFile = configFile;
}

/**
Set the icon for the application.  This will be used for all windows.
@param icon_type If CDSS, the application icon will be searched for using
TSToolCDSSIcon32.gif.  Otherwise, the RTi icon will be used by searching for
TSToolRTiIcon32.gif, both using a path for the main application.
*/
public static void setIcon ( String icon_type )
{	// First try loading the icon from the JAR file or class path...
	String icon_file = "TSToolRTiIcon32.gif";
	String icon_path ="";
	if ( icon_type.equalsIgnoreCase("CDSS") ) {
		icon_file = "TSToolCDSSIcon32.gif";
	}
	try {
        // The icon files live in the main application folder in the classpath.
        icon_path = "DWR/DMI/tstool/" + icon_file;
		JGUIUtil.setIconImage( icon_path );
	}
	catch ( Exception e ) {
		Message.printStatus ( 2, "", "TSTool icon \"" + icon_path +	"\" does not exist in classpath." );
	}
}

/**
Setup the application using the specific command file, which either came in on the
command line with "-commands CommandFile" or simply as an argument.
@param command_file_arg Command file from the command line argument (no processing on the argument
before this call).
@param is_batch Indicates if the command file was specified with "-commands CommandFile",
indicating that a batch run is requested.
*/
private static void setupUsingCommandFile ( String command_file_arg, boolean is_batch )
{   String routine = "TSToolMain.setupUsingCommandFile";

    // Make sure that the command file is an absolute path because it indicates the working
    // directory for all other processing.
    String user_dir = System.getProperty("user.dir");
    Message.printStatus (1, routine, "Startup (user.dir) directory is \"" + user_dir + "\"");
    String command_file_canonical = null;   // Does not need to be absolute
    File command_file_File = new File(command_file_arg);
    File command_file_full_File = null;
    String command_file_full = null;
    try {
        command_file_canonical = command_file_File.getCanonicalPath();
        Message.printStatus( 1, routine, "Canonical path for command file is \"" + command_file_canonical + "\"" );
    }
    catch ( Exception e ) {
        String message = "Unable to determine canonical path for \"" + command_file_arg + "\"." +
        "Check that the file exists and read permissions are granted.  Not using command file.";
        Message.printWarning ( 1, routine, message );
        System.out.println ( message );
                
        return;
    }
    
    // Get the absolute path to the command file.
    // TODO SAM 2008-01-11 Shouldn't a canonical path always be absolute?
    File command_file_canonical_File = new File ( command_file_canonical );
    if ( command_file_canonical_File.isAbsolute() ) {
        command_file_full = command_file_canonical;
    }
    else {
        // Append the command file to the user directory and set the working directory to
        // the resulting directory.
        command_file_full = user_dir + File.separator + command_file_full;
    }
    
    // Save the full path to the command file so it can be processed when the GUI initializes.
    // TODO SAM 2007-09-09 Evaluate phasing global command file out - needs to be handled in
    // the command processor.
    Message.printStatus ( 1, routine, "Command file is \"" + command_file_full + "\"" );
    // FIXME SAM 2008-09-04 Confirm no negative effects from taking this out
    //IOUtil.setProgramCommandFile ( command_file_full );
    setCommandFile ( command_file_full );
    
    setWorkingDirUsingCommandFile ( command_file_full );
    
    command_file_full_File = new File ( command_file_full );
    if ( !command_file_full_File.exists() ) {
        String message = "Command file \"" + command_file_full + "\" does not exist.";
        Message.printWarning(1, routine, message );
        System.out.println ( message );
        if ( is_batch ) {
             // Exit because there is nothing to do...
            quitProgram ( 1 );
        }
        else {
            // In GUI mode, go ahead and start up but there will be more warnings about no file to read.
        }
    }
    
    // Indicate whether running in batch mode...
    
    IOUtil.isBatch ( is_batch );
}

/**
Set the working directory as the system "user.dir" property.
*/
private static void setWorkingDirInitial()
{String routine = "TSToolMain.setWorkingDirInitial";
    String working_dir = System.getProperty("user.dir");
    IOUtil.setProgramWorkingDir ( working_dir );
    // Set the dialog because if the running in batch mode and interaction with the graph
    // occurs, this default for dialogs should be the home of the command file.
    JGUIUtil.setLastFileDialogDirectory( working_dir );
    String message = "Setting working directory to user directory \"" + working_dir +"\".";
    Message.printStatus ( 1, routine, message );
    System.out.println(message);
}

/**
Set the working directory as the parent of the command file.
*/
private static void setWorkingDirUsingCommandFile ( String command_file_full )
{   File command_file_full_File = new File ( command_file_full );
    String working_dir = command_file_full_File.getParent();
    IOUtil.setProgramWorkingDir ( working_dir );
    // Set the dialog because if the running in batch mode and interaction with the graph
    // occurs, this default for dialogs should be the home of the command file.
    JGUIUtil.setLastFileDialogDirectory( working_dir );
    // Print at level 1 because the log file is not yet initialized.
    String message = "Setting working directory to command file folder \"" + working_dir + ".\"";
    //Message.printStatus ( 1, routine, message );
    System.out.println(message);
}

} // End TSTool
