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

import javax.swing.JApplet;
import javax.swing.JFrame;

import RTi.Util.GUI.JGUIUtil;
import RTi.Util.IO.DataUnits;
import RTi.Util.IO.IOUtil;
import RTi.Util.IO.Prop;
import RTi.Util.IO.PropList;
import RTi.Util.Message.Message;
import RTi.Util.String.StringUtil;

public class tstool extends JApplet
{
public static final String PROGRAM_NAME = "TSTool";
public static final String PROGRAM_VERSION = "8.00.00 (2007-07-22)";

private static TSTool_JFrame	__tstool_JFrame;	// Main GUI
private static String		__home = null;		// Home directory for
							// system install.
private static PropList		__tstool_props = null;	// Properties.
private static boolean		__is_server = false;	// Indicates whether
							// TSTool is running in
							// server mode - under
							// development.
private static boolean		__show_main_gui = true;	// Indicates whether the
							// main GUI is shown.

/**
Return the JFrame for the main TSTool GUI.
@return the JFrame instance for use with low-level code that needs to pop up
dialogs, etc.
*/
public static JFrame getJFrame ()
{	return (JFrame)__tstool_JFrame;
}

/**
Return a TSTool property.
@param property name of property to look up.
@return the value for a TSTool configuration property, or null if a properties
file does not exist.  Return null if the property is not found (or if no
configuration file exists for TSTool).
*/
public static String getPropValue ( String property )
{	if ( __tstool_props == null ) {
		return null;
	}
	return __tstool_props.getValue ( property );
}

/**
Instantiates the application instance as an applet.
*/
public void init()
{	String routine = "TSTool.init";
	IOUtil.setApplet ( this );
	IOUtil.setProgramData ( PROGRAM_NAME, PROGRAM_VERSION, null );
        try {	parseArgs( this );
	}
	catch ( Exception e ) {
                Message.printWarning( 1, routine,
                "Error parsing command line arguments.  Using default " +
		"behavior if necessary." );
		Message.printWarning ( 3, routine, e );
        }

        // Instantiate main GUI

	initialize();

	// Full GUI as applet (no log file)...
	// Show the main GUI, although later might be able to start up just
	// the TSView part via a web site.
	__tstool_JFrame = new TSTool_JFrame ( true );
}

/**
Initialize important data.
*/
private static void initialize ()
{	// Initialize message levels...

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
Initialize important data.
*/
private static void initialize2 ()
{	String routine = "tstool.initialize2";

	// Initialize the system data...

	String units_file = __home + File.separator +
				"system" + File.separator + "DATAUNIT";

	Message.printStatus ( 2, routine, "Reading the units file \"" +
		units_file + "\"" );
	try {	DataUnits.readUnitsFile( units_file );
	}
	catch ( Exception e ) {
		Message.printWarning ( 2, routine,
		"Error reading units file \"" + units_file + "\"\n" +
		"Some conversions will not be supported.\n" +
		"Default output precisions may not be appropriate." );
	}
}

/**
Indicate whether TSTool is running in server mode.  This feature is under
development.
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
{	String routine = "TSTool.main";

	try {
	// Main try...

	IOUtil.setProgramData ( PROGRAM_NAME, PROGRAM_VERSION, args );
	JGUIUtil.setAppNameForWindows("TSTool");

	// Set the icon to RTi's logo by default.  This may be reset later after
	// the license is checked in the GUI.

	setIcon ( "RTi" );

	// Note that messages will not be printed to the log file until
	// the log file is opened below.

	initialize();

	try {	parseArgs ( args );
	}
	catch ( Exception e ) {
                Message.printWarning ( 1, routine, 
                "Error parsing command line arguments.  Using default " +
		"behavior if necessary." );
		Message.printWarning ( 3, routine, e );
	}

	// Read the data units...

	initialize2 ();

	// Run the GUI...

	Message.printStatus ( 1, routine,
	"Starting GUI, showmain = " + __show_main_gui + " isbatch=" +
	IOUtil.isBatch() );
	__tstool_JFrame = new TSTool_JFrame ( __show_main_gui );
	if ( __is_server ) {
		// Run in server mode via the GUI object.  This goes into a
		// loop...
		__tstool_JFrame.runServer();
	}
	}
	catch ( Exception e ) {
		Message.printWarning ( 1, routine,
		"Error starting TSTool." );
		Message.printWarning ( 1, routine, e );
		quitProgram ( 1 );
	}
}

/**
Open the log file.  This should be done as soon as the application home
directory is known so that remaining information can be captured in the log
file.
*/
private static void openLogFile ()
{	String routine = "TSTool.openLogFile", __logfile = "";
	String user = IOUtil.getProgramUser();

	if ( IOUtil.isApplet() ) {
		Message.printWarning ( 2, routine,
		"Running as applet - no TSTool log file opened." );
	}
	else {	if (	(__home == null) || (__home.length() == 0) || 
			(__home.charAt(0) == '.')) {
			Message.printWarning ( 2, routine, "Home directory is "+
			"not defined.  Not opening log file.");
		}
		else {	if ( (user == null) || user.trim().equals("")) {
				__logfile = __home + File.separator + "logs" +
						File.separator + "TSTool.log";
			}
			else {	__logfile = __home + File.separator + "logs" +
						File.separator + "TSTool_" +
						user + ".log";
			}
			Message.printStatus ( 1, routine, "Log file name: " +
				__logfile );
			try {	Message.openLogFile ( __logfile );
			}
			catch (Exception e) {
				Message.printWarning ( 1, routine,
				"Error opening log file \"" + __logfile + "\"");
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
{	String routine = "TSTool.parseArgs";
	int pos = 0;	// Position in a string.
	for (int i = 0; i < args.length; i++) {
		if (args[i].equalsIgnoreCase("-commands")) {
			if ((i + 1)== args.length) {
				Message.printWarning(1,routine,
				"No argument provided to '-commands'");
				throw new Exception("No argument provided to "
					+ "'-commands'");
			}
			i++;
			String user_dir = System.getProperty("user.dir");
			Message.printStatus (1, routine, "Startup (user) directory is \"" +
					user_dir + "\"");
			String commands = (new File(args[i])).getCanonicalPath();;
			
            Message.printStatus ( 1, routine,
			"Commands file is \"" + commands + "\"" );
			// Save this so it can be processed when the GUI
			// initializes.
			IOUtil.setProgramCommandFile ( commands );
			IOUtil.isBatch ( true );
                   
            File f = new File ( commands );
			// If absolute, set the working directory...
            String working_dir = null;
			if ( f.isAbsolute() ) {
				working_dir = f.getParent();
			}
			else if ( commands.startsWith("..") ) {
				// Append the commands file to the user
				// directory and set the working directory to
				// the resulting directory.
				String commands_full = user_dir +
					File.separator + commands;
				f = new File ( commands_full );
				working_dir = f.getParent();
			}
			else {	// Else the working directory is the current
				// directory for the application...
				working_dir = user_dir;
			}
            
			IOUtil.setProgramWorkingDir ( working_dir );
			JGUIUtil.setLastFileDialogDirectory( working_dir );
			// REVISIT SAM 2005-10-18 This does not display
			// because the log file is not yet initialized.
			Message.printStatus ( 2, routine,
			"Setting working directory to commands file " +
			"directory: \"" + working_dir +"\".");
			
			if ( !f.exists() ) {
				Message.printWarning(1, routine, "Commands file \"" + commands +
						"\" does not exist.  Exiting." );
				quitProgram ( 1 );
						
			}
		}
		else if ( args[i].regionMatches(true,0,"-d",0,2)) {
			// Set debug information...
			if ((i + 1)== args.length) {
				// No argument.  Turn terminal and log file
				// debug on to level 1...
				Message.isDebugOn = true;
				Message.setDebugLevel ( Message.TERM_OUTPUT, 1);
				Message.setDebugLevel ( Message.LOG_OUTPUT, 1);
			}
			i++;
			if ( (i + 1) == args.length && args[i].indexOf(",") >= 0 ) {
				// Comma, set screen and file debug to different
				// levels...
				String token =
					StringUtil.getToken(args[i],",",0,0);
				if ( StringUtil.isInteger(token) ) {
					Message.isDebugOn = true;
					Message.setDebugLevel (
					Message.TERM_OUTPUT,
					StringUtil.atoi(token) );
				}
				token=StringUtil.getToken(args[i],",",0,1);
				if ( StringUtil.isInteger(token) ) {
					Message.isDebugOn = true;
					Message.setDebugLevel (
					Message.LOG_OUTPUT,
					StringUtil.atoi(token) );
				}
			}
			else if ( (i + 1) == args.length ) {	// No comma.  Turn screen and log file debug on
				// to the requested level...
				if ( StringUtil.isInteger(args[i]) ) {
					Message.isDebugOn = true;
					Message.setDebugLevel (
					Message.TERM_OUTPUT,
					StringUtil.atoi(args[i]) );
					Message.setDebugLevel (
					Message.LOG_OUTPUT,
					StringUtil.atoi(args[i]) );
				}
			}
		}
		else if (args[i].equalsIgnoreCase("-home")) {
			if ((i + 1)== args.length) {
				Message.printWarning(1,routine,
				"No argument provided to '-home'");
				throw new Exception("No argument provided to "
					+ "'-home'");
			}
			i++;
           
            //Changed __home since old way wasn't supporting relative paths
            //__home = args[i];
            __home = (new File(args[i])).getCanonicalPath().toString();
           
			// Open the log file so that remaining messages will
			// be seen in the log file...
			openLogFile();
			// Read the configuration file to get TSTool properties,
			// so that later command-line parameters can override
			// them...
			readConfiguration();
			Message.printStatus ( 1, routine,
			"Home directory for TSTool is \"" + __home + "\"" );
			IOUtil.setProgramWorkingDir(__home);
			IOUtil.setApplicationHomeDir(__home);
			JGUIUtil.setLastFileDialogDirectory(__home);
		}
		else if (args[i].equalsIgnoreCase("-nomaingui")) {
			// Don't make the main GUI visible...
			Message.printStatus ( 1, routine,
			"Will process command file using hidden main GUI." );
			__show_main_gui = false;
		}
		else if (args[i].equalsIgnoreCase("-release")) {
			IOUtil.setRelease(true);
			Message.printStatus ( 1, routine,
			"Running in release mode." );
		}
		else if (args[i].equalsIgnoreCase("-server")) {
			Message.printStatus ( 1, routine,
			"Starting TSTool in server mode" );
			__is_server = true;
		}
		else if (args[i].equalsIgnoreCase("-test")) {
			IOUtil.testing(true);
			Message.printStatus ( 1, routine,
			"Running in test mode." );
		}
		else if ( (pos = args[i].indexOf("=")) > 0 ) {
			// A command line argument of the form:
			// Prop.erty=Value
			// For example, specify a database login for batch
			// mode.  The properties can be interpreted by the
			// GUI or other code.
			String propname = args[i].substring(0,pos);
			String propval = 
				args[i].substring((pos+1), args[i].length());
			Prop prop = new Prop ( propname, propval );
			Message.printStatus ( 1, routine,
				"Using run-time parameter " + propname + "=\"" +
				propval + "\"" );
			prop.setHowSet ( Prop.SET_AT_RUNTIME_BY_USER );
			if ( __tstool_props == null ) {
				// Create a PropList.  This should not normally
				// happen because a PropList should have been
				// read when -home was encountered.
				__tstool_props = new PropList ( "TSTool" );
			}
			__tstool_props.set ( prop );
		}
	}
}

// REVISIT - need to make these work as expected.
/**
Parse the command-line arguments for the applet, determined from the applet
data.
@param a JApplet for this application.
*/
public static void parseArgs ( JApplet a )
throws Exception
{	String home = a.getParameter("-home");
	String test = a.getParameter("-test");
	String release = a.getParameter("-release");

	if ( home != null ) {
		__home = (new File(home)).getCanonicalPath().toString();
		IOUtil.setProgramWorkingDir(__home);
		IOUtil.setApplicationHomeDir(__home);
		JGUIUtil.setLastFileDialogDirectory(__home);
	}

	if ( test != null ) {
		IOUtil.testing(true);
	}

	if ( release != null ) {
		IOUtil.setRelease(true);
	}
}

/**
Print the program usage to the log file.
*/
public static void printUsage ( )
{	String nl = System.getProperty ( "line.separator" );
	String routine = "tstool.printUsage";
	String usage =  nl +
"Usage:  " + PROGRAM_NAME + " [options] [-commands File]" + nl + nl +
"TSTool displays, analyzes, and manipulates time series." + nl+
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
	System.out.println (  nl +
	PROGRAM_NAME + " version: " + PROGRAM_VERSION + nl + nl );
	quitProgram (0);
}

/**
Clean up and quit the program.
@param status Program exit status.
*/
public static void quitProgram ( int status )
{	String	routine="tstool.quitProgram";

	Message.printStatus ( 1, routine, 
	"Exiting with status " + status + "." );

	System.out.print( "STOP " + status + "\n" );
	Message.closeLogFile ();
	System.exit ( status ); 
}

/**
Read the configuration file.  This should be done as soon as the application
home is known.
*/
private static void readConfiguration ()
{	String config_file = __home + File.separator +
			"system" + File.separator + "TSTool.cfg",
	routine = "TSTool.readConfiguration";
	
	if ( IOUtil.fileReadable(config_file) ) {
		__tstool_props = new PropList ( config_file );
		__tstool_props.setPersistentName ( config_file );
		try {	__tstool_props.readPersistent ();
		}
		catch ( Exception e ) {
			Message.printWarning ( 1, routine,
			"Error reading TSTool configuration file \"" +
			config_file + "\".  TSTool may not start." );
		}
	}
}

/**
Set the icon for the application.  This will be used for all windows.
@param icon_type If CDSS, the RTi icon will be searched for using
TSToolCDSSIcon32.gif.  Otherwise, the RTi icon will be used by searching for
TSToolRTiIcon32.gif.
*/
public static void setIcon ( String icon_type )
{	// First try loading the icon from the JAR file or class path...
	String icon_file = "TSToolRTiIcon32.gif";
	String icon_path ="";
	if ( icon_type.equalsIgnoreCase("CDSS") ) {
		icon_file = "TSToolCDSSIcon32.gif";
	}
	try {	icon_path = "DWR/DMI/tstool/" + icon_file;
		JGUIUtil.setIconImage( icon_path );
	}
	catch ( Exception e ) {
		Message.printStatus ( 2, "", "TSTool icon \"" + icon_path +
			"\" does not exist in classpath." );
		// Try using the file on RTi's development system...
		if ( !IOUtil.release() ) {
			icon_path = "J:\\CDSS\\develop\\doc\\graphics\\" +
				icon_file;
			if ( IOUtil.fileExists( icon_path ) ) {
				try {	JGUIUtil.setIconImage( icon_path );
				}
				catch ( Exception e2 ) {
					Message.printStatus ( 2, "",
					"Development TSTool icon \"" +
					icon_path +
					"\" can't be set." );
				}
			}
			else {	// Not on RTi's development system so default
				// to normal Java icon...
				Message.printStatus ( 2, "",
				"TSTool icon file does " +
				"not exist...defaulting to Java");
			}
		}
	}
}

} // End TSTool
