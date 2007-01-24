//-----------------------------------------------------------------------------
// TSTool_JFrame - TSTool main JFrame
//-----------------------------------------------------------------------------
// Copyright:	See the COPYRIGHT file.
//-----------------------------------------------------------------------------
// History:
//
// 09/23/97	Daniel Weiler, RTi	Created initial class.
// 05/12/98	Catherine E.
//		Nutting-Lane, RTi	Rework layout.
// 05/16/98	CEN			Added export option set/get routines.
// 30 Jul 1998	Steven A. Malers, RTi	Add running average and period of record
//					graph.  Add summary with no statistics.
//					Finish frost dates time series.
//					Alphabetize all functions (except
//					constructor) so it is easier to navigate
//					this code!
// 31 Aug 1998	SAM, RTi		Change fillconst to setconst since it
//					does not actually fill.
// 29 Sep 1998	SAM, RTi		Clean up the fill pattern GUI code and
//					make sure that the GUI understands
//					options in a commands file.
// 13 Oct 1998	SAM, RTi		Fix some of the outstanding GUI known
//					issues (add running average to GUI).
// 20 Oct 1998	SAM, RTi		Start adding Exceedance Probability and
//					Double Mass plot.
// 03 Dec 1998	SAM, RTi		Add several features to react to Ross'
//					input.  This is version 3.07.
//					Get rid of old sort order code and just
//					order initial list by station name.
// 05 Jan 1999	CEN, RTi		Corrected problem in lookupStation when
//					working with StateMod as the source.
// 					Need to use station id AND station name.
//					Using only id can lead to problems 
//					since same id can be found in any number
//					of files.
// 07 Jan 1999	SAM, RTi		Update to 3.08.  Make so the
//					-fillhistave option applies to frost
//					dates the same as for other data.
//					Enable the add() method for frost dates.
// 27 Jan 1999	SAM, RTi		Update to 3.09.  React to user feedback.
//					Change imports to only import the
//					needed classes to try to speed load.
//					Put the data source at the top since it
//					controls enable/disables of choices.
// 23 Feb 1999	SAM, CEN, RTi		Add enhancements for RGDSS, including
//					exporting daily time series to StateMod
//					format file.
// 05 Apr 1999	SAM, RTi		Add HBDMI to queries.
// 21 Apr 1999	SAM, RTi		Add TSUnits read.
// 17 May 1999	CEN, RTi		Modified displayResults to continue if
//					size of list to display is 0 only when
//					not working with the intermediate list.
// 17 May 1999	SAM, RTi		Recompile in 1.1.8 and make a few
//					changes in response to Ray Bennett,
//					including changing the default for
//					missing data on StateMod file output to
//					-999.  Add Cut/Copy/Paste on the final
//					time series list.  Add -detailedheader
//					option.  Change so that -datasource in a
//					commands file is recognized.
// 09 Jun 1999	SAM, RTi		Enable export and graph from the
//					intermediate area and allow some edit
//					menu features (cut/copy/paste are not
//					yet allowed because have not figured out
//					how to intercept the multilist event).
//					Start enabling some advanced features
//					from RTi internal development.  Add
//					preview and non-preview menu options for
//					summary export.
// 22 Jul 1999	SAM, RTi		Add reservoir measurements (daily).
//					Enable daily diversions.  Change some
//					methods (e.g., appendResultsFinal) to be
//					more readable (e.g.,
//					appendResultsToFinalList).
// 04 Aug 1999	SAM, RTi		Continue enhancements.  4.01 was
//					released to a few users.
// 06 Sep 1999	SAM, RTi		Use standard HBDatabaseInformationGUI
//					to show database information.
// 28 Oct 1999	SAM, RTi		Add feature to ignore <= zero for
//					averages.
// 09 Nov 1999	SAM, RTi		Add ability to remember last directory
//					that was accessed so user does not have
//					to navigate directories repeatedly.
// 28 May 2000	SAM, RTi		Add Well Level data type.
// 19 Jul 2000	SAM, RTi		Add ability to use BinaryTS file for
//					StateMod processing.  Similar to CWRAT
//					and StateView, and move select database
//					to preferences.
// 04 Oct 2000	SAM, RTi		Enable DateValue time series output.
//					Fix GUI switches for StateMod, NWSCard,
//					DateValue time series.  Fix so when
//					getting the TS list for StateMod that
//					only the file header is read - much
//					faster!  Enable delete key to have the
//					same effect as pressing ClearTS.
// 11 Oct 2000	SAM, RTi		Add annual traces plot.
// 26 Oct 2000	SAM, RTi		Add scatter plot.
// 30 Oct 2000	SAM, RTi		Add duration curve.  Add bar graph.
//					Add Run menu to run commands file
//					without loading.  Add to TSManipulations
//					to support intermediate and final list
//					edits.
// 28 Nov 2000	SAM, RTi		Start finalizing the new interpreter
//					command editing via the GUI.  Add help
//					to show DateValue, StateMod, and NWSCARD
//					formats.  Change menu and button
//					label/commands to static strings to make
//					it easier to organize and maintain code.
//					Add dialogs to manipulate expressions
//					in the final list to support multi-step
//					processing.  Change so comments are not
//					selected with expression in final list.
// 11 Dec 2000	SAM, RTi		Complete transition to new framework
//					where time series are carried around in
//					a Vector or BinaryTS and then are
//					viewed/exported.  Comment out the
//					intermediate time series list since the
//					final window can now edit sufficiently.
// 21 Dec 2000	SAM, RTi		OK, back to two lists on the main GUI!
//					This time, the middle list is the
//					expressions and the final one is the
//					list if in-memory or BinaryTS files.
//					That way you can select which time
//					series are to be viewed or saved.
//					Remove all the old intermediate list
//					code, change the "final list" code to
//					"expression list" and add a new final
//					list for the in-memory time series.
// 07 Jan 2001	SAM, RTi		Change GUI to GUIUtil and IO to IOUtil.
//					MANY! other changes.
// 15 Mar 2001	SAM, RTi		Clean up to be more backward compatible
//					with old command files.  Add
//					createTraces_Dialog,
//					createFromList_Dialog,
//					setBinaryTS*_Dialog.
// 11 Apr 2001	SAM, RTi		Add readDateValue() and fully-enable
//					DateValue format as per the
//					documentation.  Fix bug where failing
//					to open log file in GUI caused GUI to
//					display as white.
// 08 May 2001	SAM, RTi		Add multiply(), divide() commands.
// 25 Jun 2001	SAM, RTi		Add initial map frame to phase in some
//					design concepts.  Change from
//					processWindowEvent() to WindowListener
//					methods.
// 14 Aug 2001	SAM, RTi		Fix bug where selecting a DateValue
//					file does not correctly transfer the
//					interval to the identifier in the
//					commands.
// 22 Aug 2001	SAM, RTi		Add blend(), copy(), cumulate(),
//					ouputBreak, relativeDiff(),
//					runProgram().
//					Remove the "Select All for Output" and
//					"Deselect All" buttons since the mouse
//					can be used to select time series in the
//					output list.  Move the "Get TS Data" to
//					"Get Time Series Data" above the final
//					list.
// 05 Sep 2001	SAM, RTi		Add TS Commands...Graph Time Series
//					menus.  Add writeSummary() to support
//					batch creation of frost dates.  Change
//					so bar graphs can be drawn with bars to
//					the left or right of the date or
//					centered on the date.
// 2001-11-01	SAM, RTi		Update to version 05.03.00.  Use
//					RTi.Util.GUI.HelpAboutDialog for help
//					instead of AboutWindow.  Enable the
//					period of record graph.  Enable all
//					common View menus in the right-click in
//					the final list.  Use
//					URLHelp.initialize() rather than the
//					command-line options.
//					Change startsWith() calls to
//					regionMatches() to allow users more
//					flexibility (and less errors) in
//					commands.  Change some String.indexOf()
//					calls to StringUtil.indexOfIgnoreCase()
//					to be more flexible.  Had to make a
//					kludge to make sure the help system
//					starts up correctly.
// 2002-01-10	SAM, RTi		Enable NWSRFS, RiversideDB, and
//					RiverWare input sources.  Rework so that
//					the choices and query list results
//					columns reset appropriately for each
//					input source.  Get rid of intermediate
//					HBStationLocationMeasurementType objects
//					for station list - this is not needed
//					anymore due to the intermediate list
//					going away.  Start phasing out NWS
//					data type abbreviations to avoid
//					duplicate lookups (for now, to minimize
//					negative effects on modelers, just add
//					HydroBase types to main list in addition
//					to NWS - layer may switch to HydroBase
//					types).  Change so HydroBase
//					comments added to the commands files
//					only include the station/structure name
//					given that the other information is
//					redundant and the period will change as
//					the database gets updated.
//					Update to use new GRTS that allows
//					Swing.  Add test menu to process
//					TSProduct file.
// 2002-01-30	SAM, RTi		Start using a "Storage" column
//					associated with time series rather than
//					using the scenario.  This allows much
//					more flexibility and consistency when
//					reading time series from input sources
//					that allow multiple time series in a
//					file/database.  Start adding preliminary
//					support for USGSNWIS format.
// 2002-02-08	SAM, RTi		Change "storage" to input type and name,
//					consistent with TSIdent.  Phase in the
//					new ~ convention throughout (but still
//					support old conventions).  Change some
//					menus and dialogs to have "HydroBase"
//					to allow updates to support additional
//					databases.  Remove help menus that
//					provided help with time series
//					identifiers and input types.  The
//					documentation now contains detailed
//					appendices containing this information.
// 2002-02-25	SAM, RTi		Add fillMOVE2().
// 2002-03-22	SAM, RTi		Make fillRegression() and the
//					fillMOVE*() features similar.  For
//					05-05-05, rework menus to get rid of
//					Properties and further isolate HydroBase
//					commands.  Get rid of internal code to
//					do single or sub-menus for manipulation-
//					sub-menus seem to work.  Rework code
//					that has long lists of menus and
//					commands to have more explicit names to
//					match the GUI and replace "expression"
//					with "command" throughout to match
//					current nomenclature. Add _app_PropList
//					to define application properties - phase
//					in over time.  For now concentrate on
//					the WorkingDir property, which can be
//					set once and then passed to edit dialogs
//					for read commands (to remove absolute
//					paths).  Add support for commands:
//					fillFromTS(),
//					fillMOVE1() (disabled),
//					readUsgsNwis(),
//					setAutoExtendPeriod(),
//					setBinaryTSPeriod(),
//					setFromTS(),
//					setQueryPeriod().
// 2002-04-15	SAM, RTi		TSTool version 05.05.03.
//					Add shiftTimeByInterval().
//					Set the commands used by TSEngine as
//					commands in IOUtil so output headers
//					will include the information.
//					Keep track of the commands file that is
//					selected so that it can be reused when
//					saving the commands.
// 2002-04-17	SAM, RTi		TSTool version 05.05.04.  Add ARMA().
//					Add setRegressionPeriod() to be backward
//					compatible with old TSTool.
// 2002-04-18	SAM, RTi		Fully enable disaggregate() in 05.05.05.
// 2002-04-22	SAM, RTi		Update to 05.05.07 to include cosmetic
//					cleanup on remaining dialogs so that
//					final release can be made.
// 2002-04-23	SAM, RTi		Update to 05.05.08 to include more
//					cosmetic cleanup - show the alias in
//					the time series list.  Figure out why
//					free() does not seem to be working.
// 2002-04-23	SAM, RTi		Update to 05.05.09 - more cosmetic
//					cleanup to do official release.
//					Add readDateValue().  Add
//					processTSProduct().
// 2002-04-26	SAM, RTi		Update to 05.05.10.  Fix a problem with
//					readTimeSeries() and spaces in file
//					names.
// 2002-04-26	SAM, RTi		Update to 05.05.11.  Fix File...Process
//					TS Product menu feature.  Remove this
//					class as a TSSupplier since the TSEngine
//					now performs this function.
// 2002-04-29	SAM, RTi		Update to 05.05.12.  Expand ARMA
//					features.  Add left and right legend
//					position.
// 2002-05-08	SAM, RTi		Use "nedit" when editing commands file
//					on UNIX.  Update to 05.05.14 - minor
//					cosmetic edits based on a review of the
//					manual.  Fix bug where selecting a data
//					type sometimes does not reset subsequent
//					selections for HydroBase.
// 2002-05-12	SAM, RTi		Update to 05.06.00.  Add readNwsCard()
//					and writeNwsCard() commands.  Add
//					comment to save dialog suggesting that
//					commands file should end in .tstool.
//					Open the log file with a name that
//					includes the user login (from the
//					system, not the login dialog).
// 2002-05-27	SAM, RTi		Add adjustExtremes().
// 2002-05-29	SAM, RTi		Add support for RiverWare data files.
//					Add support for RiverTrak.
//					Add -nomaingui command line argument to
//					hide the main GUI.  This can be used
//					when calling TSTool from other
//					applications.  In this mode a commands
//					file is automatically read and
//					processed.
// 2002-06-05	SAM, RTi		Add monthly summary report under
//					Tools.
// 2002-06-06	SAM, RTi		Add File...Save as RiverWare if the
//					RiverWare input type is enabled.
//					Make so HydroBase login host choice is
//					set to the current value so user does
//					not need to select.
// 2002-06-13	SAM, RTi		Add MODSIM support.
// 2002-06-26	SAM, RTi		Update to pass RiversideDB_DMI instance
//					to TSEngine constructor.  Add File...
//					Open RiversideDB... menu to select a
//					RiverTrak config file.
// 2002-07-12	SAM, RTi		Update to version 05.06.05.  Add
//					File...Set Working Directory.
// 2002-07-25	SAM, RTi		Update to version 05.06.06.  Fix a
//					bug in the writeRiverWare() code.
// 2002-08-23	SAM, RTi		Update to version 05.06.07.  For
//					DateValue files, change so that the time
//					series list shows an additional row for
//					the alias if one is available for the
//					time series.  Add the addConstant()
//					command.
// 2002-08-27	SAM, RTi		Change day_to_month_reservoir() to
//					TS X = newEndOfMonthTSFromDayTS().  It
//					is now consistent with the other
//					commands.
//					On main interface, change "TS Commands"
//					to "Commands".  Change
//					"Get Time Series Data" to
//					"Run Commands".
// 2002-10-11	SAM, RTi		Change ProcessManager to ProcessManager1
//					to allow transition to Java 1.4.x.
// 2002-10-17	SAM, RTi		Change back to ProcessManager since the
//					updated version seems to be performing
//					well.  Use a command array to run the
//					command.
// 2002-11-11	SAM, RTi		Add support for the Mexico CSMN data
//					format.  Ideally in the future a
//					"TimeSeriesSupplier" will be added for
//					this input type so that classes can be
//					dynamically loaded.  For now include
//					support by default - this is the first
//					case where a catalog file is used to get
//					the list of time series - this may be
//					used for other input types in the
//					future.
//					Change the Edit menu to replace
//					"Time Series Command" with "Command".
// 2003-01-03	SAM, RTi		Change the RiversideDB support to use
//					the new DMI constructor and
//					configuration file properties.
// 2003-01-09	SAM, RTi		Update to version 05.07.00.  Enable
//					licensing.  Change "HydroBaseCO" in the
//					config file to "HydroBase" (support the
//					old also).  Add Time Series Properties
//					to the View menu.
// 2003-03-12	SAM, RTi		Update to version 05.08.00.
//					* Add "StateModX" as an input type for
//					  StateMod output files.
//					* Add setHydroBaseWDIDLength() command
//					  to control how WDIDs are formed.
//					  After consideration - change to use
//					  File...Set HydroBase WDID Length.
//					  Leave the command code in for now but
//					  do not enable - it is unclear if it
//					  will ever be needed.
//					* Add more detail to the newTimeSeries()
//					  command editor to make it easier to
//					  use.
//					* Add a convertDataUnits() command under
//					  the Manipulation menu to convert the
//					  units of a time series.
//					* Enable the DIADvisor input type, only
//					  for RTi licenses.
//					* Change _dataSource_Choice to
//					  __input_type_Choice to agree with the
//					  current TSTool interface.
// 2003-04-17	SAM, RTi		Update to version 05.08.01.
//					* For DIADvisor, RAIN sensor types
//					  always have DateValue2 enabled when
//					  listing - cumulative rain.
// 2003-04-23	SAM, RTi		Try to fix some potential problems
//					because of large methods:
//					* Split up actionPerformed().
//					* Split up initGUI().
// 2003-06-12	SAM, RTi		Update to version 06.00.00.
//					* Use Swing.
//					* Use new TS (DateTime instead of
//					  TSDate, etc.).
//					* Use new HydroBaseDMI.
//					* Use new Swing GRTS.
//					* Synchronize commands processing code
//					  with StateDMI to simplify maintenance.
//					* Use new menu structure that is more
//					  like Microsoft applications.
//					* Move map interface to View and use a
//					  JCheckBoxMenuItem.
// 2003-07-28	SAM, RTi		* Add ESPTraceEnsemble input type.
// 2003-08-19	SAM, RTi		* Change __SOURCE_* to __INPUT_TYPE_*.
//					* Handle alias in list with DateValue
//					  input type.
//					* Change File menus to be more
//					  compartmentalized.  For example,
//					  File...Open Commands File becomes
//					  File...Open...Commands File.  This
//					  makes the top-level File menu cleaner
//					  and allows for hiding specific
//					  functionality.
// 2003-11-02	SAM, RTi		* Enable SHEF A format.
// 2003-11-18	SAM, RTi		* Add TS X = average().
//					* Change StateModX to StateModB.
//					* Change _app_PropList to __props.
//					* Enable readESPTraceEnsemble() command.
//					* Use JFileChooserFactory throughout to
//					  work around Java bug.
//					* Add writeESPTraceEnsemble().
//					* Start removing column definitions -
//					  put in the table models.
//					* When picking a StateMod binary file,
//					  immediately request the file name so
//					  that data types can be automatically
//					  shown to the user.
//					* Enable the HydroBase login.
//					* Remove the Data Type Modifier choice
//					  since subdatatypes are being phased
//					  in.
//					* Remove the data type definitions
//					  strings as HydroBase data types are
//					  determined from software calls now.
// 2003-12-22	SAM, RTi		* Start phasing in the
//					  InputFilter_JPanel for the Where/Is
//					  part of the interface.
// 2004-01-05	SAM, RTi		* Remove the __last_directory_selected
//					  data member and use
//					  JGUIUtil.setLastFileDialogDirectory()
//					  and corresponding get method instead.
//					  This ensures that the various GUI
//					  tools have access to the same
//					  directory.  If more resolution is
//					  needed, the JGUIUtil method can be
//					  updated to track more than one
//					  directory.
//					* Remove __where_JComboBox and
//					  __where_is_JTextField now that the
//					  filters are in place - phase in
//					  filters for the remaining input types.
//					* Enable the remaining input types,
//					  other than NWSRFS.
// 2004-01-08	SAM, RTi		* Change "Run Commands" to
//					  "Run Selected Commands" and
//					  "Run All Commands".
// 2004-01-26	SAM, RTi		* Add StateCU as an input type in order
//					  to read/write the frost dates and
//					  other data.
//					* Add File...Properties...Commands Run
//					  to show the output period, etc.
//					* Enable a HydroBase.WDIDLength property
//					  in the config file and pass to the
//					  HydroBase table model.
//					* Add Tools...Options dialog and
//					  initialize with basic information,
//					  including setting the HydroBase WDID.
// 2004-02-03	SAM, RTi		Version 06.08.00 Beta.
//					* Add openHydroBase() command and
//					  remove setDatabaseEngine(),
//					  setDatabaseHost(), and
//					  setDataSource().
//					* Show the HydroBase connection in the
//					  session and commands run properties.
// 2004-02-05	SAM, RTi		Version 06.00.09 Beta.
//					* Finalize support for reading StateCU
//					  file formats.
//					* Add writeStateCU() for StateCU frost
//					  dates.
//					* To be able to track selected file
//					  types better, start having file filter
//					  instances available throughout the
//					  class.
//					* Disable setUseDiversionComments() and
//					  add fillUsingDiversionComments(), but
//					  only shown when HydroBase is enabled.
//					* Enable setConstantBefore() - it may
//					  later be phased into setConstant() but
//					  need to release the software now.
// 2004-02-20	SAM, RTi		Version 06.01.00
//					* Move HydroBase input filter setup to
//					  HydroBase_GUI_Util.
//					* Add input filter for HydroBase SFUT
//					  structure time series to allow filter
//					  for water class data types.
//					* Comment out fillMOVE1() menu - it is
//					  not planned to be enabled and is just
//					  taking up menu space.
//					* Add fillRepeat().
//					* Add fillProrate().
//					* Enable setMax().
//					* Fix bug in comment dialog where cancel
//					  still inserts comments.
//					* Start removing code that looks up
//					  time series for dialogs even when the
//					  dialogs don't need the data - this
//					  should increase performance some.
//					* Remove calls to checkEditMenu()
//					  and instead call checkGUIState(), in
//					  order to centralized GUI handling.
//					* Enable conversion of time series
//					  identifiers to readTimeSeries()
//					  commands.  Previously it was thought
//					  that special menus would be needed
//					  to convert to input types.  However,
//					  now that the input types are in the
//					  identifier, the input type can be
//					  detected on the fly, simplifying the
//					  conversion process.  For now, enable
//					  readTimeSeries() generically as
//					  before, to allow assigning an alias.
//					* Make PopupMenu items data members and
//					  enable/disable in checkGUIState().
// 2004-02-27	SAM, RTi		* Fix bug where CASS filter was being
//					  used for the NASS data type.
// 2004-03-02	SAM, RTi		Updated to 06.02.00.
// 2004-03-15	SAM, RTi		* Add test code to create annual total
//					  time series and then a delimited file
//					  with the mean values.
//					* Add __ignore_ListSelectionEvent to
//					  ignore valueChanged() calls in some
//					  cases - this improves performance.
//					* Change the disabled select() to the
//					  enabled selectTimeSeries().
//					* Only set the working directory at
//					  startup if running the GUI - it is
//					  set when parsing command line
//					  arguments in batch mode.
//					* Remove disabled outputBreak() - it is
//					  not needed now that selectTimeSeries()
//					  and deselectTimeSeries() are in place.
//					* Order editCommand() code in the same
//					  order as the GUI.
//					* Enable the readStateModB() command so
//					  that summary statistics for map
//					  displays can be quickly produced.
// 2004-03-28	SAM, RTi		Updated to 06.03.00.
//					* Set the icon to RTi or CDSS based on
//					  the license.
// 2004-03-28	SAM, RTi		Updated to 06.04.00.
//					* Fix so the StateCU frost dates files
//					  can be listed.
// 2004-04-13	SAM, RTi		Updated to 06.05.00.
//					* Change ESPTraceEnsemble to
//					  NWSRFS_ESPTraceEnsemble.
// 2004-04-11	SAM, RTi		* Change NWSRFS package to NWSRFS_DMI.
// 2004-05-19	SAM, RTi		* Enable WIS time series.
//					* Change ESPTraceEnsemble input type
//					  from "ESPTraceEnsemble" to
//					  "NWSRFS_ESPTraceEnsemble".
//					* Change readESPTraceEnsemble() command
//					  to readNWSRFSESPTraceEnsemble().
//					* Change writeESPTraceEnsemble() command
//					  to writeNWSRFSESPTraceEnsemble().
// 2004-05-27	SAM, RTi		* For the TSTool session properties,
//					  list whether input types are
//					  enabled/disabled.
//					* Fix so that look and feel on Linux is
//					  the default for the system.
// 2004-06-01	SAM, RTi		* Group the StateMod binary output
//					  parameters to simplify use - the data
//					  type list is now similar to HydroBase
//					  except that the groups are not by
//					  location type.
// 2004-07-20	SAM, RTi		* When opening a HydroBase connection,
//					  default to the selection given by the
//					  OdbcDsn configuration property, if
//					  available.
// 2004-07-27	SAM, RTi		* StateCU handling was not recognizing
//					  the DDC file filter.
//					* Don't include the Graph - Annual
//					  Traces or Graph - Double Mass Curve.
//					  There are no current plans to enable.
// 2004-08-24	SAM, RTi		Update to version 06.09.00.
//					* Add readHydroBase() command.
// 2004-08-28	SAM, RTi		* Rework the input filter code in order
//					  to reuse the HydroBase input filters
//					  in readHydroBase().
// 2004-09-01	SAM, RTi		Update to version 06.09.01.
//					* Add support for NWSRFS FS5Files.
//					* Add updateDynamicProps() to streamline
//					  some processing.
// 2004-11-24	SAM, RTi		* Add support for sequence number in
//					  ESP Trace Ensemble time series
//					  identifiers.
//					* Add support for sequence number in
//					  DateValue time series identifiers.
//					* Remove sequence number from other
//					  displays (only show for ESP and
//					  DateValue).
//					* Change labels from "FS5Files" to
//					  "FS5 Files".
// 2004-12-21	SAM, RTi		* The initial set working directory
//					  command was not adding quotes to the
//					  path - fix.
// 2005-02-16	SAM, RTi		Update to version 06.10.00.
//					* Enable the changeInterval() command
//					  since a generic version is now
//					  available.
// 2005-04-05	SAM, RTi		* Add the fillMixedStationModel()
//					  command and Tools...Analysis...
//					  Mixed Station Analysis.
//					* Implement MessageLogListener and
//					  begin phasing in message tags.
//					* Add getSelectedTimeSeriesList().
//					* Add "Analyze Time Series" and
//					  "Model Time Series" menus as
//					  placeholders for advanced features.
//					* Add compare() to help test the new
//					  HydroBase stored procedures.
//					* Add addCommands() and make
//					  addCommand() public.  These methods
//					  will likely be called by some
//					  dialogs to transfer commands back to
//					  the main interface.
//					* Add analyzePattern().
//					* Add "Graph - Point" graph type.
//					* Change command_JDialog to
//					  commandString_JDialog to support old
//					  code while phasing in the new
//					  Command_JDialog.
//					* Add "Graph - Predicted Value" and
//					  "Graph - Predicted Value Residual"
//					  graph types.
//					* Use a courier font for commands to
//					  make spacing more consistent.
//					* Add the sortTimeSeries() command in
//					  the write section to facilitate
//					  comparison of output files.
//					* Add readNwsCard() to read 1+ time
//					  series.
//					* Enable compareTimeSeries().
// 2005-05-13	SAM, RTi		* Add startLog() command and reorder
//					  the general commands to be more
//					  consistent with GUI standards.
// 2005-05-17	SAM, RTi		* Convert fillHistMonthAverage() to use
//					  command class.
//					* Add support for reading NwsCard single
//					  or trace time series files.
// 2005-05-18	SAM, RTi		* Convert fillHistYearAverage() to use
//					  command class.
// 2005-05-19	SAM, RTi		* Transfer command-based classes to the
//					  TS package to allow for general use.
//					* Convert changeInterval() to use a
//					  command class.
// 2005-05-31	SAM, RTi		* Convert writeRiverWare() to new
//					  command class.
// 2005-06-08	SAM, RTi		* Convert openHydroBase() to a command
//					  class and move to the HydroBaseDMI
//					  package.
// 2005-06-21	JTS, RTi	 	Converted
//					__input_filter_generic_JPanel to 
//					an InputFilter_JPanel instead of just
//					a JPanel, in order to avoid class cast
//					exceptions.
// 2005-07-25	SAM, RTi		* Put "(under development)" in some
//					  menu item strings, until final testing
//					  has been done.
// 2005-07-11	SAM, RTi		* Add a placeholder for the lagK()
//					  command under models.
// 2005-07-18	J. Thomas Sapienza, RTi	* Input filters are now also set when
//					  the time step combo box is changed.
//					* WellLevel / Irregular time series 
//					  now use the station input filters 
//					  rather than groundwater well filters.
// 2005-08-23	JTS, RTi		Users are now presented with a login
//					dialog when choosing to connect to 
//					RiversideDB.
// 2005-08-24	SAM, RTi		Update to 06.10.07.
//					* Include the above item and add those
//					  below.
//					* Convert scale() code to class.
//					* Add Tools menu for the TSProduct
//					  manager for RiversideDB.
//					* Change menus from "TS X..." to
//					  "TS Alias = ...".
//					* Convert copy() code to class.
//					* Change so double-click on command
//					  edits the command.
// 2005-08-30	SAM, RTi		* Convert writeStateMod() code to class.
//					* If commands are changed to/from
//					  comments, mark the commands as dirty.
//					* Convert readStateMod() code to class.
// 2005-09-07	SAM, RTi		Update to 06.10.08.
//					* Convert fillConstant() to a command
//					  class.
//					* Add TS Alias =
//					  createYearlyStatisticTS() command.
// 2005-09-20	SAM, RTi		* Convert TS Alias = newTimeSeries() to
//					  a command class.
// 2005-09-28	SAM, RTi		Update to 6.10.09.
//					* Convert cumulate() to a command class.
//					* Convert readStateModB() to a command
//					  class.
// 2005-09-10	SAM, RTi		Update to 6.11.00.
//					* Change so that the HydroBaseDMI
//					  connection will open using information
//					  in the CDSS.cfg file, for batch mode
//					  runs.
//					* Add connection information for the
//					  ColoradoSMS database.  This is only
//					  used for annotations on HydroBase
//					  time series and full queries of the
//					  SMS time series are not enabled.
//					* Do not initialize input filters when
//					  the GUI is NOT being shown.  This
//					  increases performance.
//					* Convert processTSProduct() to a
//					  command class.
// 2005-11-07	JTS, RTi		Added properties so that users can copy
//					data out of the upper-right JWorksheet
//					in the app.
// 2005-11-13	SAM, RTi		* Handle normal/extended Jar files for
//					  binary ESP files.
// 2005-11-15	JTS, RTi		* WellLevel Day was broken out of the
//					  regular HydroBase table model and 
//					  given its own.
// ...					Update to version 06.14.00.
// 2005-12-07	JTS, RTi		* The command list now calls 
//					  setPrototypeCellValue() so that line
//					  heights will accomodate blank lines
//					  nicely.
// 2005-12-14	SAM, RTi		* Change setQueryPeriod() to
//					  setInputPeriod() and use command
//					  class.  Allow backward compatibility.
//					* Rearrange some menus so that "helper"
//					  commands are before other commands
//					  that use settings.
//					* Add 2006 to Help About copyright.
// 2006-01-15	SAM, RTi		* Update so that for StateMod binary
//					  file format 11.x+, the data types are
//					  read from the parameter information in
//					  the file.
//					* Fix bug for StateModB and StateCU, the
//					  user could not cancel a browse for a
//					  file.
// 2006-01-16	SAM, RTi		Update to version 06.15.00.
//					* Add dynamic link to map interface,
//					  in particular Tools...Select on Map.
//					* Reorder the general menus to match
//					  StateDMI, which is an improvement.
// 2006-01-17	JTS, RTi		* Added code to print an exception if
//					  Time Steps cannot be retrieved for
//					  RiversideDB.
//					* LoginJDialog was moved from the 
//					  RiverTrakAssistant package to the
//					  RiversideDB_DMI package.
// 2006-01-18	JTS, RTi		* NWSCardTS is now in
//					  RTi.DMI.NWSRFS_DMI.
// 2006-01-31	SAM, RTi		Update to version 06.16.00.
//					* Add TS Alias = readNDFD() command.
//					* Add openNDFD() command.
// 2006-02-01	JTS, RTi		* NWSCardTS extensions for reading and 
//					  writing are now retrieved from a 
//					  dynamic method in the NWSCardTS class.
// 2006-02-16	SAM, RTi		* Wrapped menu and query results list
//					  initialization so that it is only done
//					  when the main GUI is shown,
//					  in order to increase performance.
// 2006-03-02	SAM, RTi		* Finalize basic map interaction.
//					* Change View...Map Interface to
//					  View...Map.
// 2006-04-03	SAM, RTi		* Add data test commands to the
//					  Analyze Time Series commands.
// 2006-04-16	SAM, RTi		* Convert fillMOVE2 to commands class.
// 2006-04-17	JTS, RTi		* Corrected command JList cell sizing 
//					  problem that was causing the JList to 
//					  never scroll horizontally, but 
//					  instead to trim long lines.
// 2006-04-19	SAM, RTi		Update to version 6.17.00.
//					* Add compareFiles() command.
//					* Change readHydroBase() to use a
//					  command class.
// 2006-05-02	SAM, RTi		Update to version 6.18.00.
//					* Add runCommands() command, to automate
//					  regression testing.
// 2006-06-15	SAM, RTi		Update to version 6.20.00.
//					* Add test code to process real-time
//					  streamflow data into shapefile with
//					  percent of average statistics.
// 2006-10-31	SAM, RTi		Update to version 7.00.00.
//					* Add input filters for HydroBase CASS
//					  livestock and CUPopulation.
// 2006-11-07   KAT, RTi        Commented out a line in
//                    TSTool_JFrame that was changing the
//                    working directory when it shouldn't
//                    have been.
// 2007-01-11   KAT, RTi        Set the preferred size for the 
//                    Time Series Results Panel .  The problem
//                    was that when the Input Panel shrunk to fit
//                    its corresponding data, so did the results
//                    panel.  The change is to set the minimum size
//                    for that panel so that it can increase but will
//                    not descrease past a certain threshold.  The new
//                    dimensions are the same as the default
//                    dimensions for the Input panel when HydroBase is
//                    chosen.
// 2007-01-23	SAM, RTi	Try some changes to get initial paint to draw
//				completely - still not quite there but better?
//-----------------------------------------------------------------------------
//EndHeader

// REVISIT SAM 2004-08-29 Need to use HydroBase_Util preferredWDIDLength instead
// of carrying around a property - isolate all HydroBase properties to that
// package

package DWR.DMI.tstool;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.filechooser.FileFilter;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.UIManager;

import RTi.GR.GRLimits;
import RTi.GR.GRPoint;
import RTi.GR.GRShape;

import RTi.GRTS.TSProcessor;
import RTi.GRTS.TSProduct;
import RTi.GRTS.TSPropertiesJFrame;
import RTi.GRTS.TSViewJFrame;

import RTi.GIS.GeoView.GeoLayer;
import RTi.GIS.GeoView.GeoLayerView;
import RTi.GIS.GeoView.GeoRecord;
import RTi.GIS.GeoView.GeoViewJFrame;
import RTi.GIS.GeoView.GeoViewListener;

import RTi.DMI.DMIUtil;

import RTi.DMI.DIADvisorDMI.DIADvisorDMI;
import RTi.DMI.DIADvisorDMI.DIADvisor_SensorDef;
import RTi.DMI.DIADvisorDMI.DIADvisor_SysConfig;
import RTi.DMI.DIADvisorDMI.SelectDIADvisorDialog;

import RTi.DMI.NWSRFS_DMI.NWSCardTS;
import RTi.DMI.NWSRFS_DMI.NWSRFS_DMI;
import RTi.DMI.NWSRFS_DMI.NWSRFS_ESPTraceEnsemble;
import RTi.DMI.NWSRFS_DMI.NWSRFS_ConvertJulianHour_JDialog;
import RTi.DMI.NWSRFS_DMI.NWSRFS_TS_InputFilter_JPanel;
import RTi.DMI.NWSRFS_DMI.NWSRFS_Util;	// New

import RTi.DMI.RiversideDB_DMI.LoginJDialog;
import RTi.DMI.RiversideDB_DMI.RiversideDB_DataType;
import RTi.DMI.RiversideDB_DMI.RiversideDB_DMI;
import RTi.DMI.RiversideDB_DMI.RiversideDB_MeasLoc;
import RTi.DMI.RiversideDB_DMI.RiversideDB_MeasType;
import RTi.DMI.RiversideDB_DMI.RiversideDB_TSProductManager_JFrame;

import RTi.TS.BinaryTS;
import RTi.TS.DateValueTS;
import RTi.TS.DayTS;
import RTi.TS.fillMixedStation_Command;
import RTi.TS.fillMixedStation_JDialog;
import RTi.TS.HourTS;
import RTi.TS.ModsimTS;
import RTi.TS.MexicoCsmnTS;
import RTi.TS.RiverWareTS;
import RTi.TS.ShefATS;
import RTi.TS.TS;
import RTi.TS.TSAnalyst;
import RTi.TS.TSCommandFactory;
import RTi.TS.TSCommandProcessor;
import RTi.TS.TSIdent;
import RTi.TS.TSUtil;
import RTi.TS.UsgsNwisTS;
import RTi.TS.YearTS;

import RTi.Util.GUI.FindInJListJDialog;
import RTi.Util.GUI.JFileChooserFactory;
import RTi.Util.GUI.JGUIUtil;
import RTi.Util.GUI.JScrollWorksheet;
import RTi.Util.GUI.JWorksheet;
import RTi.Util.GUI.JWorksheet_AbstractRowTableModel;
import RTi.Util.GUI.JWorksheet_Listener;
import RTi.Util.GUI.HelpAboutJDialog;
import RTi.Util.GUI.InputFilter;
import RTi.Util.GUI.InputFilter_JPanel;
import RTi.Util.GUI.ReportJFrame;
import RTi.Util.GUI.ResponseJDialog;
import RTi.Util.GUI.SimpleFileFilter;
import RTi.Util.GUI.SimpleJButton;
import RTi.Util.GUI.SimpleJComboBox;
import RTi.Util.GUI.SimpleJMenuItem;
import RTi.Util.GUI.TextResponseJDialog;
import RTi.Util.Help.URLHelp;
//import RTi.Util.Help.URLHelpGUI;
import RTi.Util.IO.Command;
import RTi.Util.IO.DataType;
import RTi.Util.IO.DataUnits;
import RTi.Util.IO.EndianRandomAccessFile;
import RTi.Util.IO.GenericCommand;
import RTi.Util.IO.InvalidCommandParameterException;
import RTi.Util.IO.InvalidCommandSyntaxException;
import RTi.Util.IO.IOUtil;
import RTi.Util.IO.LicenseManager;
import RTi.Util.IO.PrintJGUI;
import RTi.Util.IO.ProcessManager;
import RTi.Util.IO.Prop;
import RTi.Util.IO.PropList;
import RTi.Util.IO.SHEFType;
import RTi.Util.IO.UnknownCommandException;
import RTi.Util.Math.MathUtil;
import RTi.Util.Math.StandardNormal;
import RTi.Util.Message.Message;
import RTi.Util.Message.DiagnosticsJFrame;
import RTi.Util.Message.MessageLogListener;
import RTi.Util.String.StringUtil;
import RTi.Util.IO.DataUnits;
import RTi.Util.Table.DataTable;
import RTi.Util.Table.TableField;
import RTi.Util.Table.TableRecord;
import RTi.Util.Time.DateTime;
import RTi.Util.Time.DateTimeBuilderJDialog;
import RTi.Util.Time.StopWatch;
import RTi.Util.Time.TimeUtil;
import RTi.Util.Time.TimeInterval;

import DWR.DMI.HydroBaseDMI.HydroBaseDMI;
import DWR.DMI.HydroBaseDMI.HydroBase_AgriculturalCASSCropStats;
import DWR.DMI.HydroBaseDMI.HydroBase_AgriculturalNASSCropStats;
import DWR.DMI.HydroBaseDMI.HydroBase_CountyRef;
import DWR.DMI.HydroBaseDMI.HydroBase_GUI_AgriculturalCASSCropStats_InputFilter_JPanel;
import DWR.DMI.HydroBaseDMI.HydroBase_GUI_AgriculturalCASSLivestockStats_InputFilter_JPanel;
import DWR.DMI.HydroBaseDMI.HydroBase_GUI_AgriculturalNASSCropStats_InputFilter_JPanel;
import DWR.DMI.HydroBaseDMI.HydroBase_GUI_CUPopulation_InputFilter_JPanel;
import DWR.DMI.HydroBaseDMI.HydroBase_GUI_GroundWater_InputFilter_JPanel;
import DWR.DMI.HydroBaseDMI.HydroBase_GUI_SheetNameWISFormat_InputFilter_JPanel;
import DWR.DMI.HydroBaseDMI.HydroBase_GUI_StationGeolocMeasType_InputFilter_JPanel;
import DWR.DMI.HydroBaseDMI.HydroBase_GUI_StructureGeolocStructMeasType_InputFilter_JPanel;
import DWR.DMI.HydroBaseDMI.HydroBase_GUI_StructureIrrigSummaryTS_InputFilter_JPanel;
import DWR.DMI.HydroBaseDMI.HydroBase_GUI_Util;
import DWR.DMI.HydroBaseDMI.HydroBase_StationGeolocMeasType;
import DWR.DMI.HydroBaseDMI.HydroBase_StructureGeolocStructMeasType;
import DWR.DMI.HydroBaseDMI.HydroBase_Util;
import DWR.DMI.HydroBaseDMI.HydroBase_WaterDistrict;
import DWR.DMI.HydroBaseDMI.SelectHydroBaseJDialog;

import DWR.DMI.SatMonSysDMI.SatMonSysDMI;
import DWR.DMI.SatMonSysDMI.SatMonSys_Util;

import DWR.StateCU.StateCU_CropPatternTS;
import DWR.StateCU.StateCU_IrrigationPracticeTS;
import DWR.StateCU.StateCU_TS;

import DWR.StateMod.StateMod_BTS;
import DWR.StateMod.StateMod_DataSet;
import DWR.StateMod.StateMod_Util;
import DWR.StateMod.StateMod_TS;

import RTi.TS.TSCommandProcessorUI;
import RTi.TS.TSEngine;

public class TSTool_JFrame extends JFrame
implements ActionListener, GeoViewListener, ItemListener, JWorksheet_Listener,
KeyListener, ListSelectionListener, MessageLogListener, MouseListener,
WindowListener, TSCommandProcessorUI
{

//  Data Members

private LicenseManager	__license_manager = null;
						// The license manager to verify
						// the license, etc.
private GeoViewJFrame	__geoview_JFrame = null;// Map interface

// Query area...

private SimpleJComboBox	__input_type_JComboBox,	// Available input types
						// including enabled file and
						// databases.
			__input_name_JComboBox,	// Available input names for the
						// input type (history of recent
						// choices and allow browse
						// where necessary).
			__data_type_JComboBox,	// Data type choice.
			__time_step_JComboBox;  // Time step for initial query.
private FileFilter __input_name_FileFilter = null;
						// FileFilter for current input
						// name, checked with StateCU
						// (and more being phased in).
						// Having as a data member
						// allows selection of a filter
						// in one place and a check
						// later (e.g., when the time
						// series list is generated and
						// displayed).
private SimpleFileFilter
	__input_name_StateCU_ddy_FileFilter = null,
	__input_name_StateCU_ddc_FileFilter = null,
	__input_name_StateCU_ddh_FileFilter = null,
	__input_name_StateCU_iwrrep_FileFilter = null,
	__input_name_StateCU_iwr_FileFilter = null,
	__input_name_StateCU_precip_FileFilter = null,
	__input_name_StateCU_temp_FileFilter = null,
	__input_name_StateCU_cds_FileFilter = null,
	__input_name_StateCU_frost_FileFilter = null,
	__input_name_StateCU_ipy_FileFilter = null,
	__input_name_StateCU_tsp_FileFilter = null,
	__input_name_StateCU_wsl_FileFilter = null;
						// FileFilters used with StateCU
						// input type.
						// __input_name_FileFilter will
						// be set to one of these.
private Vector __input_filter_JPanel_Vector = new Vector(5);
						// Vector of InputFilter_JPanel
						// or JPanel (for input types
						// that do not support input
						// filters).  One of these will
						// be visible at any time.
private JPanel __selected_input_filter_JPanel = null;
						// The currently selected input
						// filter JPanel, used to check
						// input and get the filter
						// information for queries.
						// Not an InputFilter_JPanel
						// because of the generic case.
private InputFilter_JPanel __input_filter_HydroBase_CASSCropStats_JPanel = null;
						// InputFilter_JPanel for
						// HydroBase CASS agricultural
						// crop statistics time series.
private InputFilter_JPanel __input_filter_HydroBase_CASSLivestockStats_JPanel =
						null;
						// InputFilter_JPanel for
						// HydroBase CASS agricultural
						// livestock statistics
						// time series.
private InputFilter_JPanel __input_filter_HydroBase_CUPopulation_JPanel = null;
						// InputFilter_JPanel for
						// HydroBase CUPopulation
						// time series.
private InputFilter_JPanel __input_filter_HydroBase_NASS_JPanel = null;
						// InputFilter_JPanel for
						// HydroBase NASS agricultural
						// crop statistics time series.
private InputFilter_JPanel __input_filter_HydroBase_irrigts_JPanel = null;
						// InputFilter_JPanel for
						// HydroBase structure
						// irrig_summary_ts time series.
private InputFilter_JPanel __input_filter_HydroBase_station_JPanel = null;
						// InputFilter_JPanel for
						// HydroBase station time
						// series.
private InputFilter_JPanel __input_filter_HydroBase_structure_JPanel = null;
						// InputFilter_JPanel for
						// HydroBase structure time
						// series - those that do not
						// use SFUT.
private InputFilter_JPanel __input_filter_HydroBase_structure_sfut_JPanel =null;
						// InputFilter_JPanel for
						// HydroBase structure time
						// series - those that do use
						// SFUT.
private InputFilter_JPanel __input_filter_HydroBase_WIS_JPanel =null;
						// InputFilter_JPanel for
						// HydroBase WIS time
						// series.
private InputFilter_JPanel __input_filter_MexicoCSMN_JPanel = null;
						// InputFilter_JPanel for
						// Mexico CSMN stations time
						// series.
private InputFilter_JPanel __input_filter_NWSRFS_FS5Files_JPanel = null;
						// InputFilter_JPanel for
						// NWSRFS_FS5Files time series.
private InputFilter_JPanel __input_filter_HydroBase_wells_JPanel = null;
						// InputFilter_JPanel for
						// groundwater wells data
private InputFilter_JPanel __input_filter_generic_JPanel = null;
						// JPanel for input types that
						// do not support input filters.
private int __input_filter_y = 0;		// Used to keep track of the
						// layout position for the input
						// filter panels because the
						// panel cannot be initialized
						// until after database
						// connections are made.
private Vector		__input_name_NWSRFS_FS5Files = new Vector();
						// The NWSFFS FS5Files
						// directories that have
						// been selected during the
						// session, to allow switching
						// between input types but not
						// losing the list of files.
private Vector		__input_name_StateCU = new Vector();
						// The StateCU files that have
						// been selected during the
						// session, to allow switching
						// between input types but not
						// losing the list of files.
private String		__input_name_StateCU_last = null;
						// The last StateCU file that
						// was selected, to reset after
						// cancelling a browse.
private Vector		__input_name_StateModB = new Vector();
						// The StateModB files that have
						// been selected during the
						// session, to allow switching
						// between input types but not
						// losing the list of files.
private String		__input_name_StateModB_last = null;
						// The last StateModB file that
						// was selected, to reset after
						// cancelling a browse.
private JPopupMenu __input_name_JPopupMenu = null;
						// REVISIT - does not seem to
						// work! - popup on popup
						// Used with input name to allow
						// a new input name to be
						// specified.  SAM tried using
						// a "Browse..." choice but the
						// problem is that an event
						// will not be generated if the
						// item state does not change.
private final String __USE_APPS_DEFAULTS =	// String to display in input
		"Use Apps Defaults";		// name combo box to indicate
						// that the NWSRFS_FS5Files
						// input type should use the
						// current apps defaults to find
						// the files to read.
private final String __PLEASE_SELECT =		// String to display in input
		"Please select...";		// name combo box to indicate
						// that something should be
						// selected.  The selection will
						// force an event to happen,
						// which will allow the proper
						// settings.
private final String __BROWSE = "Browse...";	// String to display in input
						// name combo box to browse for
						// a new file...
private SimpleJButton	__get_ts_list_JButton,	// Get TS List Button
			__CopySelectedToCommands_JButton,
						// Copy selected time series
						// from list to commands (as
						// time series identifiers).
			__CopyAllToCommands_JButton;
						// Copy all time series
						// from list to commands (as
						// time series identifiers).
private JPanel		__query_input_JPanel,	// Panel for input options.
			__query_results_JPanel,	// Panel to hold results of
						// time series header lists.
			__commands_JPanel,	// Panel for commands
			__ts_JPanel;		// Panel for TS results

private JWorksheet	__query_JWorksheet;	// Query results
private JWorksheet_AbstractRowTableModel __query_TableModel = null;
						// Table model for query
						// results.
private String 		__selected_input_type = null,
						// Input type when
						// "Get Time Series List"
						// is selected
			__selected_input_name = null,
						// Input name displayed in the
						// "Input Name: choice."
			__selected_data_type = null,
						// Data type when
						// "Get Time Series List" 
						// is selected - only containing
						// the data type used in time
						// series identifiers.  The
						// leading or trailing extra
						// information is removed after
						// selection to simplify use.
			__selected_data_type_full = null,
						// Full data type from the
						// visible choice - this allows
						// input types like HydroBase
						// to differentiate between
						// stations and structures.
			__selected_time_step = null;
						// Time step when
						// "Get Time Series List"
						// is selected

// Commands area...

private JList		__commands_JList;	// List to hold commands.
private DefaultListModel __commands_JListModel;	// JList model for commands
						// (basically a Vector of
						// commands associated with
						// __commands_JList).
private SimpleJButton	__Run_SelectedCommands_JButton,
						// Run the selected commands
			__Run_AllCommands_JButton,
						// Run all the commands
			__ClearCommands_JButton;
						// Clear commands(s)
private Vector		__commands_cut_buffer = new Vector(100,100);
						// Commands list buffer for
						// cut/paste.
private Vector __fill_pattern_files = 	new Vector (10,10);
						// paths to pattern files
private Vector __fill_pattern_ids = new Vector ( 10, 10 );
						// Time series with pattern
						// data.
private boolean __commands_dirty = false;	// Indicates whether the
						// commands have been edited
						// without being saved.
private String __commands_file_name = null;	// Name of the commands file.
						// Don't set until selected by
						// the user (or on the command
						// line).

// Results area...

private JList		__ts_JList;		// Final list showing in-memory
						// time series.
private DefaultListModel __ts_JListModel;	// JList model for final time
						// series (basically a Vector of
						// time series associated with
						// __ts_JList).

private TSEngine __final_ts_engine = null;	// TSEngine used to process the
						// final list.  This TSEngine
						// maintains the final list of
						// time series for quick product
						// generation.
private JPopupMenu __results_JPopupMenu = null;

// Status-area related...

private final String __STATUS_READY = "Ready";	// General status string to
						// indicate that the GUI is
						// ready for user input.
private final String __STATUS_BUSY = "Wait";	// General status string to
						// indicate that the user should
						// wait for the GUI to finish a
						// task.
private JTextField __message_JTextField;	// Message area text field
						// (e.g., "Processing
						// commands...") - long and
						// left-most.
private JProgressBar __processor_JProgressBar;	// Progress bar to show progress
						// running commands.
private JTextField __status_JTextField;		// Status area text field (e.g.,
						// "READY", "WAIT") - small and
						// right-most

// General...

private PropList __props;			// TSTool application
						// properties.  These control
						// how the GUI behaves before
						// commands are run.  For
						// example, if the output
						// period is set in the
						// setOutputPeriod_Dialog, then
						// the output period string
						// properties can be passed back
						// to this main GUI.  Then,
						// subsequent edit dialogs can
						// use the information to
						// display appropriate
						// properties or instructions.
						// It may be somewhat difficult
						// to use this because the
						// properties are singular but
						// properties can change during
						// a run.
private String __initial_working_dir = "";	// The initial working directory
						// corresponding to a commands
						// file read/write or File...
						// Set Working Dir.  This is
						// used when processing the list
						// of setWorkingDir() commands
						// passed to command editors.
						// Without the initial working
						// directory, relative changes
						// in the working directory will
						// result in an inaccurate
						// initial state.

private boolean __gui_initialized = false;	// Lets the checkGUIState()
						// method avoid checking for
						// many null components during
						// startup.

private boolean __ignore_ItemEvent;		// Use this to temporarily
						// ignore item listener events,
						// necessary when
						// programatically modifying the
						// contents of combo boxes.
private boolean __ignore_ListSelectionEvent = false;
						// This is used in some cases to
						// disable updates during bulk
						// operations to the commands
						// list.

private boolean	__source_ColoradoSMS_enabled = false,
		__source_DateValue_enabled = true,	// Indicates which data
		__source_DIADvisor_enabled = false,	// sources are enabled,
		__source_HydroBase_enabled = true,	// initialized to
		__source_MexicoCSMN_enabled = false,	// defaults
		__source_MODSIM_enabled = true,
		__source_NDFD_enabled = true,
		__source_NWSCard_enabled = true,
		__source_NWSRFS_enabled = true,
		__source_NWSRFS_FS5Files_enabled = false,
		__source_NWSRFS_ESPTraceEnsemble_enabled = false,
		__source_RiversideDB_enabled = false,
		__source_RiverWare_enabled = true,
		__source_SHEF_enabled = true,
		__source_StateCU_enabled = true,
		__source_StateMod_enabled = true,
		__source_StateModB_enabled = true,
		__source_USGSNWIS_enabled = true;

private boolean __show_main = true;		// Indicates if the main GUI
						// should be shown.

private RiversideDB_DMI __rdmi = null;		// DMI for RiversideDB
private DIADvisorDMI __DIADvisor_dmi = null;	// DMIs for DIADvisor
private DIADvisorDMI __DIADvisor_archive_dmi =	// operational and archive
					null;	// databases.

private HydroBaseDMI __hbdmi = null;		// HydroBaseDMI object for
						// HydroBase input type.
private SatMonSysDMI __smsdmi = null;		// SatMonSysDMI object for
						// ColoradoSMS input type.

private NWSRFS_DMI __nwsrfs_dmi = null;		// NWSRFS_DMI object.

// REVISIT
//URLHelpGUI _help_index_gui = null;		// GUI for help.

// List in order of the menus...

private JPopupMenu
	__Commands_JPopupMenu;

private JMenuItem
	__CommandsPopup_Edit_CommandWithErrorChecking_JMenuItem,
	__CommandsPopup_Edit_CommandNoErrorChecking_JMenuItem,

	__CommandsPopup_Cut_JMenuItem,
	__CommandsPopup_Copy_JMenuItem,
	__CommandsPopup_Paste_JMenuItem,

	__CommandsPopup_Delete_JMenuItem,
	__CommandsPopup_FindCommands_JMenuItem,

	__CommandsPopup_SelectAll_JMenuItem,
	__CommandsPopup_DeselectAll_JMenuItem,

	__CommandsPopup_Run_AllCommandsCreateOutput_JMenuItem,
	__CommandsPopup_Run_AllCommandsIgnoreOutput_JMenuItem,
	__CommandsPopup_Run_SelectedCommandsCreateOutput_JMenuItem,
	__CommandsPopup_Run_SelectedCommandsIgnoreOutput_JMenuItem,

	__CommandsPopup_ConvertSelectedCommandsToComments_JMenuItem,
	__CommandsPopup_ConvertSelectedCommandsFromComments_JMenuItem;

// File menu...

private JMenu
	__File_JMenu = null,
	__File_Open_JMenu = null;
		private JMenuItem
		__File_Open_CommandsFile_JMenuItem = null,
		__File_Open_DIADvisor_JMenuItem = null,
		__File_Open_HydroBase_JMenuItem = null,
		__File_Open_RiversideDB_JMenuItem = null;
private JMenu
	__File_Save_JMenu = null;
		private JMenuItem
		__File_Save_Commands_JMenuItem = null,
		__File_Save_CommandsAs_JMenuItem = null,
		__File_Save_TimeSeriesAs_JMenuItem = null;
private JMenu
	__File_Print_JMenu = null;
		private JMenuItem
		__File_Print_Commands_JMenuItem = null;
private JMenu
	__File_Properties_JMenu = null;
		private JMenuItem
		__File_Properties_CommandsRun_JMenuItem = null,
		__File_Properties_TSToolSession_JMenuItem = null,
		__File_Properties_ColoradoSMS_JMenuItem = null,
		__File_Properties_DIADvisor_JMenuItem = null,
		__File_Properties_HydroBase_JMenuItem = null,
		__File_Properties_NWSRFSFS5Files_JMenuItem = null,
		__File_Properties_RiversideDB_JMenuItem = null,
	__File_SetWorkingDirectory_JMenuItem = null,
	__File_Exit_JMenuItem = null;

// Edit menu...

private JMenu
	__Edit_JMenu = null;
private JMenuItem
	__Edit_CutCommands_JMenuItem = null,
	__Edit_CopyCommands_JMenuItem = null,
	__Edit_PasteCommands_JMenuItem = null,
	// --
	__Edit_DeleteCommands_JMenuItem = null,
	// --
	__Edit_SelectAllCommands_JMenuItem = null,
	__Edit_DeselectAllCommands_JMenuItem = null,
	// --
	__Edit_CommandsFile_JMenuItem = null,
	__Edit_CommandNoErrorChecking_JMenuItem = null,
	__Edit_CommandWithErrorChecking_JMenuItem = null,
	// --
	__Edit_ConvertSelectedCommandsToComments_JMenuItem = null,
	__Edit_ConvertSelectedCommandsFromComments_JMenuItem = null;

// View menu...

private JMenu
	__View_JMenu = null;
private JCheckBoxMenuItem
	__View_MapInterface_JCheckBoxMenuItem = null;

// Commands (Create Time Series)...

JMenu
	__Commands_JMenu = null,
	__Commands_CreateTimeSeries_JMenu = null;
JMenuItem
	__Commands_Create_createFromList_JMenuItem,
	__Commands_Create_createTraces_JMenuItem,
	__Commands_Create_TS_average_JMenuItem,
	__Commands_Create_TS_changeInterval_JMenuItem,
	__Commands_Create_TS_copy_JMenuItem,
	__Commands_Create_TS_disaggregate_JMenuItem,
	__Commands_Create_TS_newDayTSFromMonthAndDayTS_JMenuItem,
	__Commands_Create_TS_newEndOfMonthTSFromDayTS_JMenuItem,
	__Commands_Create_TS_newStatisticYearTS_JMenuItem,
	__Commands_Create_TS_newTimeSeries_JMenuItem,
	__Commands_Create_TS_normalize_JMenuItem,
	__Commands_Create_TS_relativeDiff_JMenuItem,
	__Commands_Create_TS_weightTraces_JMenuItem;

// Commands (Convert TSID to Read Command)...

JMenu
	__Commands_ConvertTSIDToReadCommand_JMenu = null;
JMenuItem
	__Commands_ConvertTSIDTo_readTimeSeries_JMenuItem = null,
	__Commands_ConvertTSIDTo_readDateValue_JMenuItem = null,
	__Commands_ConvertTSIDTo_readHydroBase_JMenuItem = null,
	__Commands_ConvertTSIDTo_readMODSIM_JMenuItem = null,
	__Commands_ConvertTSIDTo_readNwsCard_JMenuItem = null,
	__Commands_ConvertTSIDTo_readNWSRFSFS5Files_JMenuItem = null,
	__Commands_ConvertTSIDTo_readRiverWare_JMenuItem = null,
	__Commands_ConvertTSIDTo_readStateMod_JMenuItem = null,
	__Commands_ConvertTSIDTo_readStateModB_JMenuItem = null,
	__Commands_ConvertTSIDTo_readUsgsNwis_JMenuItem = null;

// Commands (Read Time Series)...

JMenu
	__Commands_ReadTimeSeries_JMenu = null;
JMenuItem
	//--
	// NOT TS Alias = commands...
	__Commands_Read_readDateValue_JMenuItem,
	__Commands_Read_readHydroBase_JMenuItem,
	__Commands_Read_readMODSIM_JMenuItem,
	__Commands_Read_readNwsCard_JMenuItem,
	__Commands_Read_readNWSRFSESPTraceEnsemble_JMenuItem,
	__Commands_Read_readNWSRFSFS5Files_JMenuItem,
	__Commands_Read_readStateCU_JMenuItem,
	__Commands_Read_readStateMod_JMenuItem,
	__Commands_Read_readStateModB_JMenuItem,
	__Commands_Read_statemodMax_JMenuItem,
	// TS Alias = commands...
	__Commands_Read_TS_readDateValue_JMenuItem,
	__Commands_Read_TS_readHydroBase_JMenuItem,
	__Commands_Read_TS_readMODSIM_JMenuItem,
	__Commands_Read_TS_readNDFD_JMenuItem,
	__Commands_Read_TS_readNwsCard_JMenuItem,
	__Commands_Read_TS_readNWSRFSFS5Files_JMenuItem,
	__Commands_Read_TS_readRiverWare_JMenuItem,
	__Commands_Read_TS_readStateMod_JMenuItem,
	__Commands_Read_TS_readStateModB_JMenuItem,
	__Commands_Read_TS_readUsgsNwis_JMenuItem,

	__Commands_Read_setIncludeMissingTS_JMenuItem,
	__Commands_Read_setInputPeriod_JMenuItem;

	// Commands...Fill Time Series....
JMenu
	__Commands_FillTimeSeries_JMenu = null;
JMenuItem
	__Commands_Fill_fillCarryForward_JMenuItem,
	__Commands_Fill_fillConstant_JMenuItem,
	__Commands_Fill_fillDayTSFrom2MonthTSAnd1DayTS_JMenuItem,
	__Commands_Fill_fillFromTS_JMenuItem,
	__Commands_Fill_fillHistMonthAverage_JMenuItem,
	__Commands_Fill_fillHistYearAverage_JMenuItem,
	__Commands_Fill_fillInterpolate_JMenuItem,
	__Commands_Fill_fillMixedStation_JMenuItem,
	__Commands_Fill_fillMOVE1_JMenuItem,
	__Commands_Fill_fillMOVE2_JMenuItem,
	__Commands_Fill_fillPattern_JMenuItem,
	__Commands_Fill_fillProrate_JMenuItem,
	__Commands_Fill_fillRegression_JMenuItem,
	__Commands_Fill_fillRepeat_JMenuItem,
	__Commands_Fill_fillUsingDiversionComments_JMenuItem,

	__Commands_Fill_setAutoExtendPeriod_JMenuItem,
	__Commands_Fill_setAveragePeriod_JMenuItem,
	__Commands_Fill_setIgnoreLEZero_JMenuItem,
	__Commands_Fill_setMissingDataValue_JMenuItem,
	__Commands_Fill_setPatternFile_JMenuItem,
	__Commands_Fill_setRegressionPeriod_JMenuItem;

	// Commands...Set Time Series....
JMenu
	__Commands_SetTimeSeries_JMenu = null;
JMenuItem
	//--
	__Commands_Set_replaceValue_JMenuItem,
	//--
	__Commands_Set_setConstant_JMenuItem,
	__Commands_Set_setConstantBefore_JMenuItem,
	__Commands_Set_setDataValue_JMenuItem,
	__Commands_Set_setFromTS_JMenuItem,
	__Commands_Set_setMax_JMenuItem;


// Commands...Manipulate Time Series....
JMenu
	__Commands_ManipulateTimeSeries_JMenu = null;
JMenuItem
	__Commands_Manipulate_add_JMenuItem,
	__Commands_Manipulate_addConstant_JMenuItem,
	__Commands_Manipulate_adjustExtremes_JMenuItem,
	__Commands_Manipulate_ARMA_JMenuItem,
	__Commands_Manipulate_blend_JMenuItem,
	__Commands_Manipulate_convertDataUnits_JMenuItem,
	__Commands_Manipulate_cumulate_JMenuItem,
	__Commands_Manipulate_divide_JMenuItem,
	__Commands_Manipulate_free_JMenuItem,
	__Commands_Manipulate_multiply_JMenuItem,
	__Commands_Manipulate_runningAverage_JMenuItem,
	__Commands_Manipulate_scale_JMenuItem,
	__Commands_Manipulate_shiftTimeByInterval_JMenuItem,
	__Commands_Manipulate_subtract_JMenuItem;

// Commands...Analyze Time Series....
JMenu
	__Commands_AnalyzeTimeSeries_JMenu = null;
JMenuItem
	__Commands_Analyze_analyzePattern_JMenuItem = null,
	__Commands_Analyze_compareTimeSeries_JMenuItem = null,

	__Commands_Analyze_newDataTest_JMenuItem = null,
	__Commands_Analyze_readDataTestFromRiversideDB_JMenuItem = null,
	__Commands_Analyze_runDataTests_JMenuItem = null,
	__Commands_Analyze_processDataTestResults_JMenuItem = null;

// Commands...Models....
JMenu
	__Commands_Models_JMenu = null;
JMenuItem
	__Commands_Models_lagK_JMenuItem = null;

// Commands...Output Time Series....
JMenu
	__Commands_OutputTimeSeries_JMenu = null;
JMenuItem
	__Commands_Output_deselectTimeSeries_JMenuItem,
	__Commands_Output_setOutputDetailedHeaders_JMenuItem,
	__Commands_Output_setOutputPeriod_JMenuItem,
	__Commands_Output_setOutputYearType_JMenuItem,
	__Commands_Output_selectTimeSeries_JMenuItem,
	__Commands_Output_sortTimeSeries_JMenuItem,
	__Commands_Output_writeDateValue_JMenuItem,
	__Commands_Output_writeNwsCard_JMenuItem,
	__Commands_Output_writeNWSRFSESPTraceEnsemble_JMenuItem,
	__Commands_Output_writeRiverWare_JMenuItem,
	__Commands_Output_writeStateCU_JMenuItem,
	__Commands_Output_writeStateMod_JMenuItem,
	__Commands_Output_writeSummary_JMenuItem,

	__Commands_Output_processTSProduct_JMenuItem;


// Commands (General)...
JMenu
	__Commands_General_JMenu = null;
JMenuItem
	__Commands_General_startLog_JMenuItem,
	__Commands_General_setDebugLevel_JMenuItem,
	__Commands_General_setWarningLevel_JMenuItem,

	__Commands_General_setWorkingDir_JMenuItem,

	__Commands_General_comment_JMenuItem,
	__Commands_General_startComment_JMenuItem,
	__Commands_General_endComment_JMenuItem,

	__Commands_General_exit_JMenuItem,

	__Commands_General_compareFiles_JMenuItem = null,
	__Commands_General_runCommands_JMenuItem = null,
	__Commands_General_runProgram_JMenuItem = null;


// Commands (HydroBase)...
JMenu
	__Commands_HydroBase_JMenu = null;
JMenuItem
	__Commands_HydroBase_openHydroBase_JMenuItem;

// Commands (NDFD)...
JMenu
	__Commands_NDFD_JMenu = null;
JMenuItem
	__Commands_NDFD_openNDFD_JMenuItem;

// Run...

private JMenu
	__Run_JMenu = null;
private JMenuItem
	__Run_AllCommandsCreateOutput_JMenuItem,
	__Run_AllCommandsIgnoreOutput_JMenuItem,
	__Run_SelectedCommandsCreateOutput_JMenuItem,
	__Run_SelectedCommandsIgnoreOutput_JMenuItem,
	__Run_CommandsFromFile_JMenuItem,
	__Run_ProcessTSProductPreview_JMenuItem,
	__Run_ProcessTSProductOutput_JMenuItem;

// Results...

private JMenu
	__Results_JMenu = null,
	__Results_Graph_JMenu = null;
private JMenuItem
	__Results_Graph_AnnualTraces_JMenuItem = null,
	__Results_Graph_BarsLeft_JMenuItem = null,
	__Results_Graph_BarsCenter_JMenuItem = null,
	__Results_Graph_BarsRight_JMenuItem = null,
	__Results_Graph_DoubleMass_JMenuItem = null,
	__Results_Graph_Duration_JMenuItem = null,
	__Results_Graph_Line_JMenuItem = null,
	__Results_Graph_LineLogY_JMenuItem = null,
	__Results_Graph_PercentExceedance_JMenuItem = null,
	__Results_Graph_PeriodOfRecord_JMenuItem = null,
	__Results_Graph_Point_JMenuItem = null,
	__Results_Graph_PredictedValue_JMenuItem = null,
	__Results_Graph_PredictedValueResidual_JMenuItem = null,
	__Results_Graph_XYScatter_JMenuItem = null,
	__Results_Table_JMenuItem = null,
	__Results_Report_Summary_JMenuItem = null,
	__Results_TimeSeriesProperties_JMenuItem = null;

// Tools...

private JMenu
	__Tools_JMenu = null,
	__Tools_Analysis_JMenu = null;
private JMenuItem
	__Tools_Analysis_MixedStationAnalysis_JMenuItem = null;
private JMenu
	__Tools_Report_JMenu = null;
private JMenuItem
	__Tools_Report_DataCoverageByYear_JMenuItem = null,
	__Tools_Report_DataLimitsSummary_JMenuItem = null,
	__Tools_Report_MonthSummaryDailyMeans_JMenuItem = null,
	__Tools_Report_MonthSummaryDailyTotals_JMenuItem = null,
	__Tools_Report_YearToDateTotal_JMenuItem = null,
	__Tools_Report_YearMeanMeanStatistic = null,
	__Tools_Report_YearTotalMeanStatistic = null;
private JMenu
	__Tools_NWSRFS_JMenu = null;
private JMenuItem
	__Tools_NWSRFS_ConvertNWSRFSESPTraceEnsemble_JMenuItem = null,
	__Tools_NWSRFS_ConvertJulianHour_JMenuItem = null;
private JMenu
	__Tools_RiversideDB_JMenu = null;
private JMenuItem
	__Tools_RiversideDB_TSProductManager_JMenuItem = null,

	__Tools_SelectOnMap_JMenuItem = null,

	__Tools_Options_JMenuItem = null;

private JMenu		
	__Help_JMenu = null;
private JMenuItem
	__Help_AboutTSTool_JMenuItem = null;

private final static int
			__UPDATE_COMMAND = 1,	// Update command.
			__INSERT_COMMAND = 2;	// Insert new command.

// String labels for buttons and menus...

private String	

	TAB = "",	// not needed with reorganization?
	//TAB = "       ",
	// Buttons (in order from top to bottom of GUI)...

	BUTTON_TOP_GET_TIME_SERIES = "Get Time Series List",
	BUTTON_TOP_COPY_ALL_TO_COMMANDS ="Copy All to Commands",
	BUTTON_TOP_COPY_SELECTED_TO_COMMANDS ="Copy Selected to Commands",

	__Button_RunSelectedCommands_String = "Run Selected Commands",
	__Button_RunAllCommands_String = "Run All Commands",
	__Button_ClearCommands_String = "Clear Commands",

	BUTTON_TS_SELECT_ALL = "Select All for Output",
	BUTTON_TS_DESELECT_ALL = "Deselect All",

	// Menus in order that they appear in the GUI.  "ActionString"s are
	// defined where needed to avoid ambiguity in the menu labels.

	// Popup only...

	__CommandsPopup_FindCommands_String = "Find Command(s)...",

	// File menu (order in GUI)...

	__File_String = "File",
		__File_Open_String = "Open",
			__File_Open_CommandsFile_String = "Commands File...", 
			__File_Open_DIADvisor_String = "DIADvisor...",
			__File_Open_HydroBase_String = "HydroBase...",
			__File_Open_RiversideDB_String = "RiversideDB...",
		__File_Save_String = "Save",
			//__File_Save_Commands_ActionString =
				//"File...Save...Commands...", 
			__File_Save_Commands_String = "Commands", 
			__File_Save_CommandsAs_String = "Commands As...", 
			__File_Save_TimeSeriesAs_String = "Time Series As...", 
		__File_Print_String = "Print",
			__File_Print_Commands_ActionString =
				"File...Print...Commands...", 
			__File_Print_Commands_String = "Commands...", 
		__File_Properties_String = "Properties",
			__File_Properties_CommandsRun_String="Commands Run",
			__File_Properties_TSToolSession_String="TSTool Session",
			__File_Properties_ColoradoSMS_String = "ColoradoSMS",
			__File_Properties_DIADvisor_String = "DIADvisor",
			__File_Properties_HydroBase_String ="HydroBase",
			__File_Properties_NWSRFSFS5Files_String =
				"NWSRFS FS5 Files",
			__File_Properties_RiversideDB_String = "RiversideDB",
		__File_SetWorkingDirectory_String = "Set Working Directory...",
		__File_Exit_String = "Exit",

	// Edit menu (order in GUI)...

	__Edit_String = "Edit",
		__Edit_CutCommands_String = "Cut Command(s)",
		__Edit_CopyCommands_String = "Copy Command(s)",
		__Edit_PasteCommands_String = "Paste Command(s)",
		__Edit_DeleteCommands_String = "Delete Command(s)",
		__Edit_SelectAllCommands_String ="Select All Commands",
		__Edit_DeselectAllCommands_String = "Deselect All Commands",
		__Edit_CommandsFile_String = "Commands File...",
		__Edit_CommandNoErrorChecking_String =
		"Command (no error checking)...",
		__Edit_CommandWithErrorChecking_String =
		"Command (with error checking)...",
		__Edit_ConvertSelectedCommandsToComments_String = 
		"Convert selected commands to # comments",
		__Edit_ConvertSelectedCommandsFromComments_String =
		"Convert selected commands from # comments",

	// View menu (order in GUI)...

	__View_String = "View",
		__View_MapInterface_String = "Map",

	// Commands menu (order in GUI)...

	__Commands_String = "Commands",

	__Commands_ConvertTSIDToReadCommand_String =
		"Convert TS identifier to read command",
	__Commands_ConvertTSIDTo_readTimeSeries_String = TAB +
		"Convert TS identifier (X.X.X.X.X) to TS Alias = " +
		"readTimeSeries()",
	__Commands_ConvertTSIDTo_readDateValue_String = TAB +
		"Convert TS identifier (X.X.X.X.X) to TS Alias = " +
		"readDateValue()",
	__Commands_ConvertTSIDTo_readHydroBase_String = TAB +
		"Convert TS identifier (X.X.X.X.X) to TS Alias = " +
		"readHydroBase()",
	__Commands_ConvertTSIDTo_readMODSIM_String = TAB +
		"Convert TS identifier (X.X.X.X.X) to TS Alias = readMODSIM()",
	__Commands_ConvertTSIDTo_readNwsCard_String = TAB +
		"Convert TS identifier (X.X.X.X.X) to TS Alias = readNwsCard()",
	__Commands_ConvertTSIDTo_readNWSRFSFS5Files_String = TAB +
		"Convert TS identifier (X.X.X.X.X) to TS Alias = " +
		"readNWSRFSFS5Files()",
	__Commands_ConvertTSIDTo_readRiverWare_String = TAB +
		"Convert TS identifier (X.X.X.X.X) to TS Alias = " +
		"readRiverWare()",
	__Commands_ConvertTSIDTo_readStateMod_String = TAB +
		"Convert TS identifier (X.X.X.X.X) to TS Alias = " +
		"readStateMod()",
	__Commands_ConvertTSIDTo_readStateModB_String = TAB +
		"Convert TS identifier (X.X.X.X.X) to TS Alias = " +
		"readStateModB()",
	__Commands_ConvertTSIDTo_readUsgsNwis_String = TAB +
		"Convert TS identifier (X.X.X.X.X) to TS Alias = " +
		"readUsgsNwis()",

	__Commands_CreateTimeSeries_String = "Create Time Series",
	__Commands_Create_createFromList_String = TAB +
		"createFromList()...  <read 1(+) time series "
		+ "from a list of identifiers>",
	__Commands_Create_createTraces_String = TAB +
		"createTraces()...  <convert 1 time series " +
		"to 1+ annual traces>",
	__Commands_Create_TS_average_String = TAB +
		"TS Alias = average()...  " +
		"<create a time series as average of others>",
	__Commands_Create_TS_changeInterval_String = TAB +
		"TS Alias = changeInterval()...  " +
		"<convert time series to one with a different interval (under" +
		" development)>",
	__Commands_Create_TS_copy_String = TAB +
		"TS Alias = copy()...  <copy a time series>",
	__Commands_Create_TS_disaggregate_String =TAB+
		"TS Alias = disaggregate()...  " +
		"<disaggregate longer interval to shorter>",
	__Commands_Create_TS_newDayTSFromMonthAndDayTS_String =TAB+
		"TS Alias = newDayTSFromMonthAndDayTS()...  " +
		"<create daily time " +
		"series from monthly total and daily pattern>",
	__Commands_Create_TS_newEndOfMonthTSFromDayTS_String = TAB +
		"TS Alias = newEndOfMonthTSFromDayTS()...  " +
		"<convert daily data " +
		"to end of month time series>",
	__Commands_Create_TS_newStatisticYearTS_String = TAB +
		"TS Alias = newStatisticYearTS()... <create a " +
		"year time series using a statistic from another time series>",
	__Commands_Create_TS_newTimeSeries_String = TAB +
		"TS Alias = newTimeSeries()... <create and " +
		"initialize a new time series>",
	__Commands_Create_TS_normalize_String = TAB +
		"TS Alias = normalize()... <normalize time series" +
		" to unitless values>",
	__Commands_Create_TS_relativeDiff_String = TAB +
		"TS Alias = relativeDiff()... <relative difference of " +
		"time series>",
	__Commands_Create_TS_weightTraces_String = TAB +
		"TS Alias = weightTraces()... <weight traces" +
		" to create a new time series>",

	__Commands_Read_setIncludeMissingTS_String = TAB+
		"setIncludeMissingTS()... <create empty time series if no " +
		"data>",
	__Commands_Read_setInputPeriod_String = TAB+
		"setInputPeriod()... <for reading data>",

	__Commands_ReadTimeSeries_String = "Read Time Series",
	__Commands_Read_readDateValue_String =TAB+
		"readDateValue()...  <read 1(+) time " +
		"series from a DateValue file>",
	__Commands_Read_readHydroBase_String =TAB+
		"readHydroBase()...  <read 1(+) time " +
		"series from HydroBase>",
	__Commands_Read_readMODSIM_String =TAB+
		"readMODSIM()...  <read 1(+) time " +
		"series from a MODSIM output file>",
	__Commands_Read_readNwsCard_String =TAB+
		"readNwsCard()...  <read 1(+) time " +
		"series from an NWS CARD file>",
	__Commands_Read_readNWSRFSESPTraceEnsemble_String =TAB+
		"readNWSRFSESPTraceEnsemble()...  <read 1(+) time " +
		"series from an NWSRFS ESP trace ensemble file>",
	__Commands_Read_readNWSRFSFS5Files_String = TAB+
		"readNWSRFSFS5Files()...  <read 1(+) time " +
		"series from an NWSRFS FS5 Files>",
	__Commands_Read_readStateCU_String =TAB+
		"readStateCU()...  <read 1(+) time " +
		"series from a StateCU file>",
	__Commands_Read_readStateMod_String =TAB+
		"readStateMod()...  <read 1(+) time " +
		"series from a StateMod file>",
	__Commands_Read_readStateModB_String =TAB+
		"readStateModB()...  <read 1(+) time " +
		"series from a StateMod binary output file>",
	__Commands_Read_statemodMax_String = TAB +
		"statemodMax()...  <generate 1(+) time series "+
		"as max() of TS in two StateMod files>",

	__Commands_Read_TS_readDateValue_String = TAB+
		"TS Alias = readDateValue()...  <read 1 time " +
		"series from a DateValue file>",
	__Commands_Read_TS_readHydroBase_String = TAB+
		"TS Alias = readHydroBase()...  <read 1 time " +
		"series from HydroBase>",
	__Commands_Read_TS_readMODSIM_String = TAB+
		"TS Alias = readMODSIM()...  <read 1 time " +
		"series from a MODSIM output file>",
	__Commands_Read_TS_readNDFD_String = TAB+
		"TS Alias = readNDFD()...  <read 1 time " +
		"series from NDFD web service>",
	__Commands_Read_TS_readNwsCard_String = TAB+
		"TS Alias = readNwsCard()...  <read 1 time " +
		"series from an NWS CARD file>",
	__Commands_Read_TS_readNWSRFSFS5Files_String = TAB+
		"TS Alias = readNWSRFSFS5Files()...  <read 1 time " +
		"series from an NWSRFS FS5 Files>",
	__Commands_Read_TS_readRiverWare_String = TAB+
		"TS Alias = readRiverWare()...  <read 1 time " +
		"series from a RiverWare file>",
	__Commands_Read_TS_readStateMod_String = TAB+
		"TS Alias = readStateMod()...  <read 1 time " +
		"series from a StateMod file>",
	__Commands_Read_TS_readStateModB_String = TAB+
		"TS Alias = readStateModB()...  <read 1 time " +
		"series from a StateMod binary file>",
	__Commands_Read_TS_readUsgsNwis_String = TAB+
		"TS Alias = readUsgsNwis()...  <read 1 time " +
		"series from a USGS NWIS file>",

	// Commands... Fill Time Series...

	__Commands_FillTimeSeries_String = "Fill Time Series Data",
	__Commands_Fill_fillCarryForward_String =TAB+
		"fillCarryForward()...  <Fill TS by carrying forward - " +
		"** see fillRepeat()**>",
	__Commands_Fill_fillConstant_String =TAB+
		"fillConstant()...  <Fill TS with constant>",
	__Commands_Fill_fillDayTSFrom2MonthTSAnd1DayTS_String =TAB+
		"fillDayTSFrom2MonthTSAnd1DayTS()...  " +
		"<fill daily time series using D1 = D2*M1/M2>",
	__Commands_Fill_fillFromTS_String =TAB+
		"fillFromTS()...  <fill time series with " +
		"values from another time series>",
	__Commands_Fill_fillHistMonthAverage_String =TAB+
		"fillHistMonthAverage()...  " +
		"<Fill monthly TS using historic average>",
	__Commands_Fill_fillHistYearAverage_String =TAB+
		"fillHistYearAverage()...  <Fill yearly TS using historic " +
		"average>",
	__Commands_Fill_fillInterpolate_String =TAB+
		"fillInterpolate()...  <Fill TS using interpolation>",
	__Commands_Fill_fillMixedStation_String = TAB +
		"fillMixedStation()...  <Fill TS using mixed stations (under" +
		" development)>",
	__Commands_Fill_fillMOVE1_String = TAB +
		"fillMOVE1()...  <Fill TS using MOVE1 method>",
	__Commands_Fill_fillMOVE2_String = TAB +
		"fillMOVE2()...  <Fill TS using MOVE2 method>",
	__Commands_Fill_fillPattern_String = TAB +
		"fillPattern()...  <Fill TS using WET/DRY/AVG pattern>",
	__Commands_Fill_fillProrate_String = TAB +
		"fillProrate()...  <Fill TS by prorating another time series>",
	__Commands_Fill_fillRegression_String = TAB +
		"fillRegression()...  <Fill TS using regression>",
	__Commands_Fill_fillRepeat_String = TAB +
		"fillRepeat()...  <Fill TS by repeating values>",
	__Commands_Fill_fillUsingDiversionComments_String = TAB +
		"fillUsingDiversionComments()... <use diversion " +
		"comments as data  - HydroBase ONLY>",
	// SAMX - need to add later...
	//MENU_INTERMEDIATE_FILL_WEIGHTS_String =
	//	"Fill Using Weights...",

	__Commands_Fill_setAutoExtendPeriod_String = TAB+
		"setAutoExtendPeriod()... <for data filling and manipulation>",
	__Commands_Fill_setAveragePeriod_String = TAB+
		"setAveragePeriod()... <for data filling>",
	__Commands_Fill_setIgnoreLEZero_String = TAB+
		"setIgnoreLEZero()... <ignore values <= 0 in " +
		"historical averages>",
	__Commands_Fill_setMissingDataValue_String = TAB+
		"setMissingDataValue()... <for data filling>",
	__Commands_Fill_setPatternFile_String = TAB +
		"setPatternFile()... <for use with fillPattern() >",
	__Commands_Fill_setRegressionPeriod_String = TAB+
		"setRegressionPeriod()... <for fillRegression()>",

	__Commands_SetTimeSeries_String = "Set Time Series Contents",
	__Commands_Set_replaceValue_String = TAB +
		"replaceValue()...  <replace value (range) with constant in" +
		" TS>",
	__Commands_Set_setConstant_String = TAB +
		"setConstant()...  <set all values to constant in TS>",
	__Commands_Set_setConstantBefore_String = TAB +
		"setConstantBefore()...  <set all values on and before a date"+
		" to constant in TS>",
	__Commands_Set_setDataValue_String = TAB +
		"setDataValue()...  <set a single data value in a TS>",
	__Commands_Set_setFromTS_String =TAB+
		"setFromTS()...  <set time series " +
		"values from another time series>",
	__Commands_Set_setMax_String = TAB +
		"setMax()...  <set values to maximum of time series>",

	// Commands...Manipulate Time Series menu...

	__Commands_Manipulate_add_String = TAB +
		"add()...  <Add one or more TS to another>",
	__Commands_Manipulate_addConstant_String = TAB +
		"addConstant()...  <Add a constant value to a TS>",
	__Commands_Manipulate_adjustExtremes_String = TAB +
		"adjustExtremes()...  <adjust extreme values>",
	__Commands_Manipulate_ARMA_String = TAB +
		"ARMA()...  <lag/attenuate a time series using ARMA>",
	__Commands_Manipulate_blend_String = TAB +
		"blend()...  <Blend one TS with another>",
	__Commands_Manipulate_convertDataUnits_String = TAB +
		"convertDataUnits()...  <Convert data units>",
	__Commands_Manipulate_cumulate_String = TAB +
		"cumulate()...  <Cumulate values over time>",
	__Commands_Manipulate_divide_String = TAB +
		"divide()...  <Divide one TS by another TS>",
	__Commands_Manipulate_free_String = TAB +
		"free()...  <Free TS>",
	__Commands_Manipulate_multiply_String = TAB +
		"multiply()...  <Multiply one TS by another TS>",
	__Commands_Manipulate_runningAverage_String = TAB +
		"runningAverage()...  <Convert TS to running average>",
	__Commands_Manipulate_scale_String = TAB +
		"scale()...  <Scale TS by a constant>",
	__Commands_Manipulate_shiftTimeByInterval_String = TAB +
		"shiftTimeByInterval()...  " +
		"<Shift TS by an even interval>",
	__Commands_Manipulate_subtract_String = TAB +
		"subtract()...  <Subtract one or more TS from another>",

	// Commands...Output Series menu...

	__Commands_OutputTimeSeries_String = "Output Time Series",
	__Commands_Output_deselectTimeSeries_String = TAB +
		"deselectTimeSeries()...  <deselect time series for output>",
	__Commands_Output_selectTimeSeries_String = TAB +
		"selectTimeSeries()...  <select time series for output>",
	__Commands_Output_setOutputDetailedHeaders_String = TAB +
		"setOutputDetailedHeaders()... <in summary reports>",
	__Commands_Output_setOutputPeriod_String = TAB+
		"setOutputPeriod()... <for output products>",
	__Commands_Output_setOutputYearType_String = TAB +
		"setOutputYearType()... <e.g., Water, Calendar>",
	__Commands_Output_sortTimeSeries_String = TAB +
		"sortTimeSeries()...  <sort time series>",
	__Commands_Output_writeDateValue_String = TAB +
		"writeDateValue()...  <write DateValue file>",
	__Commands_Output_writeNwsCard_String = TAB +
		"writeNwsCard()...  <write NWS Card file>",
	__Commands_Output_writeNWSRFSESPTraceEnsemble_String = TAB +
		"writeNWSRFSESPTraceEnsemble()...  " +
		"<write ESP trace ensemble file>",
	__Commands_Output_writeRiverWare_String = TAB +
		"writeRiverWare()...  <write RiverWare file>",
	__Commands_Output_writeStateCU_String = TAB +
		"writeStateCU()...  <write StateCU file>",
	__Commands_Output_writeStateMod_String = TAB +
		"writeStateMod()...  <write StateMod file>",
	__Commands_Output_writeSummary_String = TAB +
		"writeSummary()...  <write Summary file>",
	__Commands_Output_processTSProduct_String = TAB +
		"processTSProduct()...  <process a time series product file>",

	// Commands...Analyze Time Series...

	__Commands_AnalyzeTimeSeries_String = "Analyze Time Series",
	__Commands_Analyze_analyzePattern_String = TAB +
		"analyzePattern()... <determine pattern(s) for fillPattern()" +
		" (under development)>",
	__Commands_Analyze_compareTimeSeries_String = TAB +
		"compareTimeSeries()... <find differences>",

	__Commands_Analyze_newDataTest_String = TAB +
		"DataTest TestID = newDataTest()... <create a new data test>" +
		" (under development)",
	__Commands_Analyze_readDataTestFromRiversideDB_String = TAB +
		"readDataTestFromRiversideDB()... " +
		"<read 1 data test from RiversideDB>" +
		" (under development)",
	__Commands_Analyze_runDataTests_String = TAB +
		"runDataTests()... <run data tests to evaluate time series>" +
		" (under development)",
	__Commands_Analyze_processDataTestResults_String = TAB +
		"processDataTestResults()... <process data test results>" +
		" (under development)",

	// Commands...Models...

	__Commands_Models_String = "Models",
	__Commands_Models_lagK_String =
		"TS Alias = lagK()... <lag and attenuate (route) " +
			"(under development)>",

	// HydroBase commands...

	__Commands_HydroBase_String = "HydroBase",
	__Commands_HydroBase_openHydroBase_String = TAB+
		"openHydroBase()... <open HydroBase database connection>",

	// NDFD commands...

	__Commands_NDFD_String = "NDFD",
	__Commands_NDFD_openNDFD_String = TAB+
		"openNDFD()... <open NDFD web site connection>",

	// General Commands...

	__Commands_General_String = "General",
	__Commands_General_startLog_String = TAB +
		"startLog()... <(re)start the log file>",
	__Commands_General_setDebugLevel_String = TAB+
		"setDebugLevel()... <set debug message level>",
	__Commands_General_setWarningLevel_String = TAB+
		"setWarningLevel()... <set debug message level>",
	__Commands_General_setWorkingDir_String = TAB +
		"setWorkingDir()... <set the working " +
		"directory for relative paths>",
	__Commands_General_Comment_String = TAB +
		"# comment(s)...",
	__Commands_General_startComment_String = TAB +
		"/*   <start comment>",
	__Commands_General_endComment_String = TAB +
		"*/   <end comment>",
	__Commands_General_exit_String = TAB +
		"exit  <to end processing>",
	__Commands_General_compareFiles_String = TAB +
		"compareFiles()... <compare files>",
	__Commands_General_runCommands_String = TAB+
		"runCommands()... <run a commands file> (under development)",
	__Commands_General_runProgram_String = TAB+
		"runProgram()... <run external program>",

	// Results menu choices (order in GUI)...

	__Results_Graph_AnnualTraces_String = "Graph - Annual Traces...",
	__Results_Graph_BarsLeft_String = "Graph - Bar (left of date)",
	__Results_Graph_BarsCenter_String = "Graph - Bar (center on date)",
	__Results_Graph_BarsRight_String = "Graph - Bar (right of date)",
	__Results_Graph_DoubleMass_String = "Graph - Double Mass Curve",
	__Results_Graph_Duration_String = "Graph - Duration",
	__Results_Graph_Line_String = "Graph - Line",
	__Results_Graph_LineLogY_String = "Graph - Line (log Y-axis)",
	__Results_Graph_PeriodOfRecord_String = "Graph - Period of Record",
	__Results_Graph_Point_String = "Graph - Point",
	__Results_Graph_PredictedValue_String =
		"Graph - Predicted Value (under development)",
	__Results_Graph_PredictedValueResidual_String =
		"Graph - Predicted Value Residual (under development)",
	__Results_Graph_XYScatter_String = "Graph - XY-Scatter",

	__Results_Table_String = "Table",
	__Results_Report_Summary_String = "Report - Summary",
	__Results_TimeSeriesProperties_String = "Time Series Properties",

	// Only for popup Results menu...

	__Results_FindTimeSeries_String = "Find Time Series...",

	// Run menu (order in GUI)...

	__Run_String = "Run",
		__Run_AllCommandsCreateOutput_String =
			"All Commands (create all output)",
		__Run_AllCommandsIgnoreOutput_String =
			"All Commands (ignore output commands)",
		__Run_SelectedCommandsCreateOutput_String =
			"Selected Commands (create all output)",
		__Run_SelectedCommandsIgnoreOutput_String =
			"Selected Commands (ignore output commands)",
		__Run_CommandsFromFile_String = "Commands From File...",
		__Run_ProcessTSProductPreview_String =
			"Process TS Product File (preview)...",
		__Run_ProcessTSProductOutput_String =
			"Process TS Product File (create output)...",

	// Tools menu (order in GUI)...

	__Tools_String = "Tools",
		__Tools_Analysis_String = "Analysis",
			__Tools_Analysis_MixedStationAnalysis_String =
				"Mixed Station Analysis... (under development)",
		__Tools_Report_String = "Report",
			__Tools_Report_DataCoverageByYear_String =
				"Data Coverage by Year...",
			__Tools_Report_DataLimitsSummary_String =
				"Data Limits Summary...",
			__Tools_Report_MonthSummaryDailyMeans_String =
				"Month Summary (Daily Means)...",
			__Tools_Report_MonthSummaryDailyTotals_String =
				"Month Summary (Daily Totals)...",
			__Tools_Report_YearToDateTotal_String =
				"Year to Date Total... " +
				"<Daily or real-time CFS Only!>",
			__Tools_Report_YearMeanMeanStatistic_String =
				"Year Mean (Mean Statistic)... ",
			__Tools_Report_YearTotalMeanStatistic_String =
				"Year Total (Mean Statistic)... ",
		__Tools_NWSRFS_String = "NWSRFS",
			__Tools_NWSRFS_ConvertNWSRFSESPTraceEnsemble_String =
				"Convert NWSRFS ESP Trace Ensemble File to " +
				"Text...",
			__Tools_NWSRFS_ConvertJulianHour_String =
				"Convert Julian Hour...",
		__Tools_RiversideDB_String = "RiversideDB",
			__Tools_RiversideDB_TSProductManager_String =
				"Manage Time Series Products in RiversideDB...",
		__Tools_SelectOnMap_String = "Select on Map",
		__Tools_Options_String = "Options",

	// Help menu (order in GUI)...

	__Help_String = "Help",
		__Help_AboutTSTool_String = "About TSTool",

	// Strings used in popup menu for other components...

	__InputName_BrowseStateModB_String =
		"Browse for a StateMod binary file...",

	__DATA_TYPE_AUTO	= "Auto",

	// Input types for the __input_type_JComboBox...

	__INPUT_TYPE_ColoradoSMS = "ColoradoSMS",
	__INPUT_TYPE_DateValue = "DateValue",
	__INPUT_TYPE_DIADvisor = "DIADvisor",
	__INPUT_TYPE_HydroBase = "HydroBase",
	__INPUT_TYPE_MEXICO_CSMN = "MexicoCSMN",
	__INPUT_TYPE_MODSIM = "MODSIM",
	__INPUT_TYPE_NWSCARD = "NWSCARD",
	__INPUT_TYPE_NWSRFS_FS5Files = "NWSRFS_FS5Files",
	__INPUT_TYPE_NWSRFS_ESPTraceEnsemble = "NWSRFS_ESPTraceEnsemble",
	__INPUT_TYPE_RiversideDB = "RiversideDB",
	__INPUT_TYPE_RiverWare = "RiverWare",
	__INPUT_TYPE_StateCU = "StateCU",
	__INPUT_TYPE_StateMod = "StateMod",
	__INPUT_TYPE_StateModB = "StateModB",
	__INPUT_TYPE_USGSNWIS = "USGSNWIS",

	// Time steps for __time_step_JComboBox...

	__TIMESTEP_AUTO		= "Auto",
	__TIMESTEP_MINUTE	= "Minute",
	__TIMESTEP_HOUR		= "Hour",
	__TIMESTEP_DAY		= "Day",
	__TIMESTEP_MONTH	= "Month",
	__TIMESTEP_YEAR		= "Year",
	__TIMESTEP_REALTIME	= "Real-time",
	__TIMESTEP_IRREGULAR	= "Irregular";

// Columns in the time series list.

private final static int
	__DIADvisor_COL_ROW_COUNT	= 0,
	__DIADvisor_COL_ID		= 1,	// Sensor ID
	__DIADvisor_COL_NAME		= 2,	// Sensor Name
	__DIADvisor_COL_DATA_SOURCE	= 3,	// blank
	__DIADvisor_COL_DATA_TYPE	= 4,	// "Data Value" and
						// "Data Value 2"
	__DIADvisor_COL_TIMESTEP	= 5,
	__DIADvisor_COL_SCENARIO	= 6,	// Blank or Archive
	__DIADvisor_COL_UNITS		= 7,	// "Display Units" and
						// "Display Units 2"
	__DIADvisor_COL_START		= 8,
	__DIADvisor_COL_END		= 9,
	__DIADvisor_COL_INPUT_TYPE	= 10;	// DIADvisor

/**
TSTool_JFrame constructor.
@param show_main Indicates whether the JFrame should be displayed at creation
(true) or not (false).
*/
public TSTool_JFrame ( boolean show_main )
{	super();
	StopWatch swMain = new StopWatch();
	swMain.start();
	String message, rtn = "TSTool_JFrame";
	__show_main = show_main;

   
	if ( !IOUtil.isBatch() ) {
		Message.setTopLevel ( this );
		// If batch this should have been set in the main program
		// when command line options were parsed...
		String last_directory_selected=System.getProperty ("user.dir") +
			System.getProperty("file.separator");
		//IOUtil.setProgramWorkingDir(last_directory_selected);
		JGUIUtil.setLastFileDialogDirectory(last_directory_selected);
	}
    
	__props = new PropList("TSTool_JFrame");
	__props.set ("WorkingDir=" + IOUtil.getProgramWorkingDir());	
	__initial_working_dir = __props.getValue ( "WorkingDir" );
 
	addWindowListener ( this );

	// Determine which data sources are available.  This controls the look
	// and feel of the GUI.  Alphabetize the checks.  Initialize default
	// properties for each area if necessary (all other properties are
	// defaulted after checking the individual input types)...

	// DateValue always enabled by default...

	String prop_value = null;
	__source_DateValue_enabled = true;

	// ColoradoSMS disabled by default...

	prop_value = tstool.getPropValue ( "TSTool.ColoradoSMSEnabled" );
	if ( prop_value != null ) {
		if ( prop_value.equalsIgnoreCase("false") ) {
			__source_ColoradoSMS_enabled = false;
		}
		else if ( prop_value.equalsIgnoreCase("true") ) {
			__source_ColoradoSMS_enabled = true;
		}
	}

	// DIADvisor not enabled by default...

	prop_value = tstool.getPropValue ( "TSTool.DIADvisorEnabled" );
	if ( prop_value != null ) {
		if ( prop_value.equalsIgnoreCase("false") ) {
			__source_DIADvisor_enabled = false;
		}
		else if ( prop_value.equalsIgnoreCase("true") ) {
			__source_DIADvisor_enabled = true;
		}
	}

	// NWSRFS_ESPTraceEnsemble not enabled by default...

	prop_value = tstool.getPropValue (
		"TSTool.NWSRFSESPTraceEnsembleEnabled" );
	if ( prop_value != null ) {
		if ( prop_value.equalsIgnoreCase("false") ) {
			__source_NWSRFS_ESPTraceEnsemble_enabled = false;
		}
		else if ( prop_value.equalsIgnoreCase("true") ) {
			__source_NWSRFS_ESPTraceEnsemble_enabled = true;
		}
	}
	// Always disable if not available in the Jar file...
	if (	!IOUtil.classCanBeLoaded(
		"RTi.DMI.NWSRFS_DMI.NWSRFS_ESPTraceEnsemble") ) {
		__source_NWSRFS_ESPTraceEnsemble_enabled = false;
	}

	// NWSRFS_FS5Files not enabled by default...

	prop_value = tstool.getPropValue (
		"TSTool.NWSRFSFS5FilesEnabled" );
	if ( prop_value != null ) {
		if ( prop_value.equalsIgnoreCase("false") ) {
			__source_NWSRFS_FS5Files_enabled = false;
		}
		else if ( prop_value.equalsIgnoreCase("true") ) {
			__source_NWSRFS_FS5Files_enabled = true;
		}
	}

	// State of Colorado HydroBase enabled by default...

	// Newer...
	prop_value = tstool.getPropValue ( "TSTool.HydroBaseEnabled" );
	if ( prop_value == null ) {
		// Older...
		prop_value = tstool.getPropValue ("TSTool.HydroBaseCOEnabled" );
	}
	if ( (prop_value != null) && prop_value.equalsIgnoreCase("false") ) {
		__source_HydroBase_enabled = false;
	}
	if ( __source_HydroBase_enabled ) {
		// Use newer notation...
		__props.set ( "TSTool.HydroBaseEnabled", "true" );
		prop_value = tstool.getPropValue ( "HydroBase.WDIDLength" );
		if ( (prop_value != null) && StringUtil.isInteger(prop_value)){
			__props.set ( "HydroBase.WDIDLength", prop_value );
			// Also set in global location.
			HydroBase_Util.setPreferredWDIDLength (
				StringUtil.atoi ( prop_value ) );
		}
		else {	// Default...
			__props.set ( "HydroBase.WDIDLength", "7" );
		}
		prop_value = tstool.getPropValue ( "HydroBase.OdbcDsn" );
		if ( prop_value != null ) {
			__props.set ( "HydroBase.OdbcDsn", prop_value );
		}
	}
	else {	__props.set ( "TSTool.HydroBaseEnabled", "false" );
	}

	// Mexico CSMN disabled by default...

	prop_value = tstool.getPropValue ( "TSTool.MexicoCSMNEnabled" );
	if ( prop_value != null ) {
		if ( prop_value.equalsIgnoreCase("false") ) {
			__source_MexicoCSMN_enabled = false;
		}
		else if ( prop_value.equalsIgnoreCase("true") ) {
			__source_MexicoCSMN_enabled = true;
		}
	}

	// MODSIM always enabled by default...

	__source_MODSIM_enabled = true;
	prop_value = tstool.getPropValue ( "TSTool.MODSIMEnabled" );
	if ( (prop_value != null) && prop_value.equalsIgnoreCase("false") ) {
		__source_MODSIM_enabled = false;
	}

	// NDFD enabled for RTi, disabled for CDSS by default...

	prop_value = tstool.getPropValue ( "TSTool.NDFDEnabled" );
	if ( (prop_value != null) && prop_value.equalsIgnoreCase("false") ) {
		__source_NDFD_enabled = false;
	}

	// NWS Card disabled by default...

	prop_value = tstool.getPropValue ( "TSTool.NWSCardEnabled" );
	if ( (prop_value != null) && prop_value.equalsIgnoreCase("false") ) {
		__source_NWSCard_enabled = false;
	}

	// NWSRFS FS5Files disabled by default...

	prop_value = tstool.getPropValue ( "TSTool.NWSRFSFS5FilesEnabled" );
	if ( (prop_value != null) && prop_value.equalsIgnoreCase("false") ) {
		__source_NWSRFS_FS5Files_enabled = false;
	}
	else if ( (prop_value != null) && prop_value.equalsIgnoreCase("true") ){
		__source_NWSRFS_FS5Files_enabled = true;
		prop_value = tstool.getPropValue (
		"NWSRFSFS5Files.UseAppsDefaults" );
		if ( prop_value != null ) {
			__props.set(
			"NWSRFSFS5Files.UseAppsDefaults",prop_value );
		}
		else {	// Default is to use files.  If someone has Apps
			// Defaults configured, this property can be changed.
			__props.set ( "NWSRFSFS5Files.UseAppsDefaults","false");
		}
		prop_value = tstool.getPropValue ( "NWSRFSFS5Files.InputName" );
		if ( prop_value != null ) {
			__props.set( "NWSRFSFS5Files.InputName",prop_value );
		}
	}

	// RiversideDB disabled by default...

	prop_value = tstool.getPropValue ( "TSTool.RiversideDBEnabled" );

	if ( prop_value != null ) {
		if ( prop_value.equalsIgnoreCase("false") ) {
			__source_RiversideDB_enabled = false;
		}
		else if ( prop_value.equalsIgnoreCase("true") ) {
			__source_RiversideDB_enabled = true;
		}
	}

	// RiverWare disabled by default...

	prop_value = tstool.getPropValue ( "TSTool.RiverWareEnabled" );
	if ( prop_value != null ) {
		if ( prop_value.equalsIgnoreCase("false") ) {
			__source_RiverWare_enabled = false;
		}
		else if ( prop_value.equalsIgnoreCase("true") ) {
			__source_RiverWare_enabled = true;
		}
	}

	// SHEF disabled by default...

	prop_value = tstool.getPropValue ( "TSTool.SHEFEnabled" );
	if ( prop_value != null ) {
		if ( prop_value.equalsIgnoreCase("false") ) {
			__source_SHEF_enabled = false;
		}
		else if ( prop_value.equalsIgnoreCase("true") ) {
			__source_SHEF_enabled = true;
		}
	}

	// StateMod enabled by default (no config file)...

	prop_value = tstool.getPropValue ( "TSTool.StateModEnabled" );
	if ( (prop_value != null) && prop_value.equalsIgnoreCase("false") ) {
		__source_StateMod_enabled = false;
	}

	// StateCU enabled by default (no config file)...

	prop_value = tstool.getPropValue ( "TSTool.StateCUEnabled" );
	if ( (prop_value != null) && prop_value.equalsIgnoreCase("false") ) {
		__source_StateCU_enabled = false;
	}

	// StateModB enabled by default...

	prop_value = tstool.getPropValue ( "TSTool.StateModBEnabled" );
	if ( (prop_value != null) && prop_value.equalsIgnoreCase("false") ) {
		__source_StateModB_enabled = false;
	}

	// USGSNWIS enabled by default...

	prop_value = tstool.getPropValue ( "TSTool.USGSNWISEnabled" );
	if ( (prop_value != null) && prop_value.equalsIgnoreCase("false") ) {
		__source_USGSNWIS_enabled = false;
	}

	prop_value = null;

	// Values determined at run-time...

	__props.setHowSet ( Prop.SET_AS_RUNTIME_DEFAULT );
	__props.set ( "WorkingDir", IOUtil.getProgramWorkingDir() );
	Message.printStatus ( 1, "", "Working directory is " +
		__props.getValue ( "WorkingDir" ) );
	__props.setHowSet ( Prop.SET_AT_RUNTIME_BY_USER );

	// Read the license information and disable/enable input types based on
	// the license...

	readLicense ();
	checkInputTypesForLicense ();
	// If the license type is CDSS, the icon will be set to the CDSS icon.
	// Otherwise, the icon will be set to the RTi icon...
	tstool.setIcon ( __license_manager.getLicenseType() );

	// create the GUI ...
	StopWatch in = new StopWatch();
	in.start();
	initGUI ( show_main );
	in.stop();
	Message.printDebug(1, "", "JTS - InitGUI: " + in.getSeconds());

	// Check the license.  Do this after GUI is initialized so that a dialog
	// can be shown...

	checkLicense ();

        // Get database connection information.  Force a login if the
	// database connection cannot be made.  The login is interactive and
	// is disabled if no main GUI is to be shown.

	StopWatch sms = new StopWatch();
	sms.start();
	if ( __source_ColoradoSMS_enabled ) {
		// Login to Colorado SMS database using information in the
		// CDSS.cfg file (pending Doug Stenzel input)...
		openColoradoSMS ( true );
	}
	sms.stop();
	Message.printDebug(1, "", "JTS - OpenColoradoSMS: " + sms.getSeconds());
	swMain.stop();
	Message.printDebug(1, "", "JTS - TSTool_JFrame(): " 
		+ swMain.getSeconds());
	if ( __source_HydroBase_enabled ) {
		// Login to HydroBase using information in the TSTool
		// configuration file...
		openHydroBase ( true );
		// Force the choices to refresh...
		if ( __show_main && (__hbdmi != null) ) {
			__input_type_JComboBox.select ( null );
			__input_type_JComboBox.select (__INPUT_TYPE_HydroBase);
		}
	}
	if ( __source_NWSRFS_FS5Files_enabled ) {
		// Open NWSRFS FS5Files using information in the TSTool
		// configuration file...
		openNWSRFSFS5Files ( null, true );
		// Force the choices to refresh...
		if ( __nwsrfs_dmi != null ) {
			/* REVISIT SAM 2004-09-10 DO NOT DO THIS BECAUSE IT
			  THEN PROMPTS FOR a choice...
			__input_type_JComboBox.select ( null );
			__input_type_JComboBox.select (
				__INPUT_TYPE_NWSRFS_FS5Files );
			*/
		}
	}
	if ( __source_RiversideDB_enabled ) {
		// Login to the RiversideDB using information in the TSTool
		// configuration file...
		openRiversideDB ( null, true );
	}

	// Connect the Message class to output to the status bar in the main
	// GUI...

	if ( show_main ) {
		Message.setOutputFunction ( Message.STATUS_BAR_OUTPUT, this,
		"printStatusMessages" );
	}

	// Add GUI features that depend on the databases...
	// The appropriate panel will be set visible as input types and data
	// types are chosen.  The panels are currently only intialized once so
	// changing databases or enabling new databases after setup may require
	// some code changes.  Set the wait cursor because queries are
	// done during setup.

	if ( show_main ) {
		// Don't do this in batch mode because it may cause a
		// performance hit querying databases (e.g., HydroBase stored
		// procedures).
		JGUIUtil.setWaitCursor ( this, true );
		try {	initGUIInputFilters ( __input_filter_y );
					// Add one vertical position even though
					// it may actually be more than one slot
					// based on what is in the panel.
		}
		catch ( Exception e ) {
			Message.printWarning ( 3, rtn,
			"For developers:  caught exception initializing " +
			"input filters at setup." );
			Message.printWarning ( 3, rtn, e );
		}
		// REVISIT SAM 2007-01-23
		// Force everything to refresh based on the current GUI
		// layout.
		// Still evaluating this.
		this.invalidate ();
		JGUIUtil.setWaitCursor ( this, false );
	}

	// Use the window listener

	if ( show_main ) {
		checkGUIState ();
	}

	// If running with a commands file from the command line, we don't
	// normally want to see the main window so handle with a special case...

	// REVISIT SAM 2005-10-28
	// In the future might allow, for example, clicking on a TSTool file
	// to bring up the GUI with that commands file, without running.  In
	// that case the GUI would show the commands but not automatically run,
	// so the following code would probably not be executed.

	String commands_file = IOUtil.getProgramCommandFile();
	if ( (commands_file != null) && (commands_file.length() > 0) ) {

		try {	Message.printStatus ( 2, rtn,
			"Running commands file \"" + commands_file + 
			"\" with no main GUI..." );
			runNoMainGUI ( commands_file );
			Message.printStatus ( 2, rtn,
			"...done running with no main GUI." );
		}
		catch ( Exception e ) {
			Message.printWarning ( 2, rtn,
			"Error running commands in \"" + commands_file +
			"\" with no main GUI." );
			// REVISIT SAM 2005-10-28
			// Do we need this?...
			// Need to close the GUI, even if hidden...
			//closeClicked();
			tstool.quitProgram ( 1 );
		}

		// In this mode it is assumed that a graph window was created
		// which when closed will close the application based on a
		// WindowListener method call.

		if ( show_main ) {
			// In a batch run without -nomaingui, the show_main
			// option is left as true.  In this case, we need to
			// close down the interface manually...
			closeClicked();
		}
	}
}

/**
Carry out the edit command action, triggered from a popup menu edit of a command
or pressing enter on the command list.
@param check_errors Indicates whether a dialog should be used that checks
errors.  If false, a general editor dialog is used.
*/
private void actionEditCommand ( boolean check_errors )
{	int selected_size = 0;
	int [] selected = __commands_JList.getSelectedIndices();
	if ( selected != null ) {
		selected_size = selected.length;
	}
	if ( selected_size > 0 ) {
		String command = ((String)
			__commands_JListModel.get (selected[0])).trim();
		Vector v = null;
		if ( command.startsWith("#") ) {
			// Allow multiple lines to be edited in a comment...
			v = new Vector ( selected_size );
			for ( int i = 0; i < selected_size; i++ ) {
				v.addElement ( (String)
					__commands_JListModel.get(selected[i]));
			}
		}
		else {	// Commands are one line...
			v = new Vector ( 1 );
			v.addElement ( command );
		}
		editCommand ( "", v, __UPDATE_COMMAND, check_errors );
		v = null;
		selected = null;
		command = null;
	}
}

/**
Handle action events (menu and button actions).
@param event Event to handle.
*/
public void actionPerformed (ActionEvent event)
{	String message, rtn = "TSTool_JFrame.actionPerformed";

	try {	// This will chain, calling several methods, so that each method
		// does not get so large...

		actionPerformed1(event);

		// Check the GUI state and disable buttons, etc., depending on
		// the selections that are made...

		checkGUIState ();
	}
	catch ( Exception e ) {
		if ( Message.isDebugOn ) {
			Message.printWarning ( 2, rtn, e );
		}
		JGUIUtil.setWaitCursor ( this, false );
	}
}

/**
Handle a group of actions.
@param event Event to handle.
*/
private void actionPerformed1 (ActionEvent event)
throws Exception
{	String command = event.getActionCommand();
	Object o = event.getSource();
	String rtn = "TSTool_JFrame.actionPerformed1";

	// Next list menus or commands...
	if (command.equals(BUTTON_TOP_GET_TIME_SERIES) ) {
		getTimeSeriesListClicked();
	}
	else if ( o == __CopySelectedToCommands_JButton ) {
		// Transfer from the time series list to the commands...
		transferSelectedTSFromListToCommands();
	}
	else if ( o == __CopyAllToCommands_JButton ) {
		// Transfer from the time series list to the commands...
		transferAllTSFromListToCommands();
	}
	else if (command.equals(__Button_RunSelectedCommands_String) ) {
		// Can be a button or a menu?
		runCommands ( false, true );
	}
	else if (command.equals(__Button_RunAllCommands_String) ) {
		// Can be a button or a menu?
		runCommands ( true, true );
	}
	else if (command.equals(__Button_ClearCommands_String) ) {
		clearCommands();
		checkGUIState();
	}
	else if (command.equals(BUTTON_TS_SELECT_ALL) ) {
		JGUIUtil.selectAll ( __ts_JList );
	}
	else if (command.equals(BUTTON_TS_DESELECT_ALL) ) {
		__ts_JList.clearSelection();
	}

	// Menu actions - listed left to to right and top to bottom.

	// File Menu actions...

	else if ( o == __File_Open_CommandsFile_JMenuItem ) {
		try {	openCommandsFile();
		}
		catch ( Exception e ) {
			Message.printWarning ( 1, rtn,
				"Error reading commands file" );
		}
	}
	else if ( command.equals ( __File_Open_DIADvisor_String )) {
		// Open a connections to the DIADvisor operational and archive
		// databases - currently assumed to be MS Access. 
		openDIADvisor ();
		// Force the choices to refresh...
		if (	(__DIADvisor_dmi != null) &&
			(__DIADvisor_archive_dmi != null) ) {
			__input_type_JComboBox.select ( null );
			__input_type_JComboBox.select ( __INPUT_TYPE_DIADvisor);
		}
	}
	else if ( command.equals ( __File_Open_HydroBase_String )) {
		openHydroBase ( false );
		// Force the choices to refresh...
		if ( __hbdmi != null ) {
			__input_type_JComboBox.select ( null );
			__input_type_JComboBox.select (__INPUT_TYPE_HydroBase);
		}
	}
	else if ( command.equals ( __File_Open_RiversideDB_String )) {
		// Read a RiverTrak config file, get the RiversideDB properties,
		// and open the database...
		JFileChooser fc = JFileChooserFactory.createJFileChooser(
			JGUIUtil.getLastFileDialogDirectory() );
		fc.setDialogTitle(
		"Select a RiverTrak or TSTool Configuration File" );
		SimpleFileFilter sff =
			new SimpleFileFilter ( "cfg",
			"RiverTrak/TSTool Configuration File" );
		fc.addChoosableFileFilter ( sff );
		fc.setFileFilter ( sff );
		if ( fc.showOpenDialog(this) != JFileChooser.APPROVE_OPTION ) {
			return;
		}
		// Don't save the directory because it is probably a one-off
		// selection and won't be picked again.
		String path = fc.getSelectedFile().getPath();
		// Read into a PropList...
		PropList rprops = new PropList("");
		rprops.setPersistentName ( path );
		rprops.readPersistent ();
		openRiversideDB ( rprops, false );
		// Force the choices to refresh...
		__input_type_JComboBox.select ( null );
		__input_type_JComboBox.select ( __INPUT_TYPE_RiversideDB );
	}
	else if ( o == __File_Save_Commands_JMenuItem ) {
		try {	if ( __commands_file_name != null ) {
				// Use the existing name...
				writeCommandsFile ( __commands_file_name,false);
			}
			else {	// Prompt for the name...
				writeCommandsFile ( __commands_file_name, true);
			}
		}
		catch ( Exception e ) {
			Message.printWarning ( 1, rtn,
			"Error writing commands file." );
		}
	}
	else if ( o == __File_Save_CommandsAs_JMenuItem ) {
		try {	// Prompt for the name...
			writeCommandsFile ( __commands_file_name, true);
		}
		catch ( Exception e ) {
			Message.printWarning ( 1, rtn,
			"Error writing commands file." );
		}
	}
        else if ( command.equals(__File_Save_TimeSeriesAs_String) ) {
		// Can save in a number of formats.  Allow the user to pick
		// using a file chooser...
		saveTimeSeries ();
	}
	else if (command.equals(__File_Print_Commands_ActionString) ) {
		try {	PrintJGUI.print ( this, StringUtil.toVector(
				__commands_JListModel.elements()), null, 10 );
		}
		catch ( Exception e ) {
			Message.printWarning ( 1, rtn,
			"Error printing commands." );
		}
	}
        else if ( command.equals(__File_Properties_CommandsRun_String) ) {
		// Simple text display of last commands run data from TSEngine.
		PropList reportProp = new PropList ("ReportJFrame.props");
		// Too big (make this big when we have more stuff)...
		//reportProp.set ( "TotalWidth", "750" );
		//reportProp.set ( "TotalHeight", "550" );
		reportProp.set ( "TotalWidth", "600" );
		reportProp.set ( "TotalHeight", "300" );
		reportProp.set ( "DisplayFont", "Courier" );
		reportProp.set ( "DisplaySize", "11" );
		reportProp.set ( "PrintFont", "Courier" );
		reportProp.set ( "PrintSize", "7" );
		reportProp.set ( "Title", "TSTool Commands Run Properties" );
		Vector v = new Vector ( 4 );
		v.addElement ( "Properties from the last commands run." );
		v.addElement (
		"Note that property values are as of the end of the run." );
		v.addElement (
		"Properties are set as defaults and change value when" +
		" a command is processed." );
		v.addElement ( "" );
		if ( __final_ts_engine == null ) {
			v.addElement ( "The commands have not been run." );
		}
		else {	DateTime date1, date2;
			String s1 = "", s2 = "";
			// Query period...
			date1 = __final_ts_engine.getQueryDate1();
			if ( date1 == null ) {
				s1 = "NOT SPECIFIED (use available)";
			}
			else {	s1 = date1.toString();
			}
			date2 = __final_ts_engine.getQueryDate2();
			if ( date2 == null ) {
				s2 = "NOT SPECIFIED (use available)";
			}
			else {	s2 = date2.toString();
			}
			v.addElement ( "Query period: " + s1 + " to " + s2 );
			// Output period...
			date1 = __final_ts_engine.getOutputStart();
			if ( date1 == null ) {
				s1 = "NOT SPECIFIED (use available)";
			}
			else {	s1 = date1.toString();
			}
			date2 = __final_ts_engine.getOutputEnd();
			if ( date2 == null ) {
				s2 = "NOT SPECIFIED (use available)";
			}
			else {	s2 = date2.toString();
			}
			v.addElement ( "Output period: " + s1 + " to " + s2 );
			// Auto-extend period...
			v.addElement ( "Automatically extend period to output "+
				"period during read: " +
				__final_ts_engine.autoExtendPeriod () );
			// Include missing TS automatically...
			v.addElement ( "Include missing TS automatically: " +
				__final_ts_engine.includeMissingTS() );
			if ( __source_HydroBase_enabled ) {
				v.addElement ( "" );
				if ( __final_ts_engine.getHydroBaseDMI()==null){
					v.addElement (
					"Command processor HydroBase " +
					"connection not defined.");
				}
				else {	v.addElement ( "Command processor " +
					"HydroBase connection information:" );
					StringUtil.addListToStringList ( v,
					StringUtil.toVector(
					__final_ts_engine.getHydroBaseDMI().
					getVersionComments() ) );
				}
			}
		}
		v.addElement ( "" );
		v.addElement ( "Current working directory: " +
			IOUtil.getProgramWorkingDir() );
		new ReportJFrame ( v, reportProp );
		// Clean up...
		v = null;
		reportProp = null;
	}
        else if ( command.equals(__File_Properties_TSToolSession_String) ) {
		// Simple text display of session data, including last
		// commands file that was read.  Put here where in the past
		// information was shown in labels.  Now need the label space
		// for other information.
		PropList reportProp = new PropList ("ReportJFrame.props");
		// Too big (make this big when we have more stuff)...
		//reportProp.set ( "TotalWidth", "750" );
		//reportProp.set ( "TotalHeight", "550" );
		reportProp.set ( "TotalWidth", "600" );
		reportProp.set ( "TotalHeight", "300" );
		reportProp.set ( "DisplayFont", "Courier" );
		reportProp.set ( "DisplaySize", "11" );
		reportProp.set ( "PrintFont", "Courier" );
		reportProp.set ( "PrintSize", "7" );
		reportProp.set ( "Title", "TSTool Session Properties" );
		Vector v = new Vector ( 4 );
		v.addElement ( "TSTool Session Properties" );
		v.addElement ( "" );
		if ( __commands_file_name == null ) {
			v.addElement (
			"No commands file has been read or saved." );
		}
		else {	v.addElement ( "Last commands file read/saved: \"" +
			__commands_file_name + "\"" );
		}
		v.addElement ( "Current working directory = " +
			IOUtil.getProgramWorkingDir() );
		// List open database information...
		if ( __source_ColoradoSMS_enabled ) {
			v.addElement ( "" );
			if ( __smsdmi == null ) {
				v.addElement (
				"GUI ColoradoSMS connection not defined.");
			}
			else {	v.addElement (
				"GUI ColoradoSMS connection information:" );
				StringUtil.addListToStringList ( v,
				StringUtil.toVector(
				__smsdmi.getVersionComments() ) );
			}
		}
		if ( __source_HydroBase_enabled ) {
			v.addElement ( "" );
			if ( __hbdmi == null ) {
				v.addElement (
				"GUI HydroBase connection not defined.");
			}
			else {	v.addElement (
				"GUI HydroBase connection information:" );
				StringUtil.addListToStringList ( v,
				StringUtil.toVector(
				__hbdmi.getVersionComments() ) );
			}
		}
		// List enabled data types...
		v.addElement ( "Input types and whether enabled:" );
		v.addElement ( "" );
		if ( __source_HydroBase_enabled ) {
			v.addElement ( "ColoradoSMS input type is enabled" );
		}
		else {	v.addElement ( "ColoradoSMS input type is not enabled");
		}
		if ( __source_DateValue_enabled ) {
			v.addElement ( "DateValue input type is enabled" );
		}
		else {	v.addElement ( "DateValue input type is not enabled" );
		}
		if ( __source_DIADvisor_enabled ) {
			v.addElement ( "DIADvisor input type is enabled" );
		}
		else {	v.addElement ( "DIADvisor input type is not enabled" );
		}
		if ( __source_HydroBase_enabled ) {
			v.addElement ( "HydroBase input type is enabled" );
		}
		else {	v.addElement ( "HydroBase input type is not enabled" );
		}
		if ( __source_MexicoCSMN_enabled ) {
			v.addElement ( "MexicoCSMN input type is enabled" );
		}
		else {	v.addElement ( "MexicoCSMN input type is not enabled" );
		}
		if ( __source_MODSIM_enabled ) {
			v.addElement ( "MODSIM input type is enabled" );
		}
		else {	v.addElement ( "MODSIM input type is not enabled" );
		}
		if ( __source_NDFD_enabled  ) {
			v.addElement ( "NDFD input type is enabled" );
		}
		else {	v.addElement ( "NDFD input type is not enabled" );
		}
		if ( __source_NWSCard_enabled  ) {
			v.addElement ( "NWSCARD input type is enabled" );
		}
		else {	v.addElement ( "NWSCARD input type is not enabled" );
		}
		if ( __source_NWSRFS_FS5Files_enabled ) {
			v.addElement ( "NWSRFS FS5Files input type is enabled");
		}
		else {	v.addElement (
			"NWSRFS FS5Files input type is not enabled" );
		}
		if ( __source_NWSRFS_ESPTraceEnsemble_enabled ) {
			v.addElement (
			"NWSRFS_ESPTraceEnsemble input type is enabled" );
		}
		else {	v.addElement (
			"NWSRFS_ESPTraceEnsemble input type is not enabled" );
		}
		if ( __source_RiversideDB_enabled ) {
			v.addElement ( "RiversideDB input type is enabled" );
		}
		else {	v.addElement ( "RiversideDB input type is not enabled");
		}
		if ( __source_RiverWare_enabled ) {
			v.addElement ( "RiverWare input type is enabled" );
		}
		else {	v.addElement ( "RiverWare input type is not enabled");
		}
		if ( __source_SHEF_enabled ) {
			v.addElement ( "SHEF input type is enabled" );
		}
		else {	v.addElement ( "SHEF input type is not enabled");
		}
		if ( __source_StateCU_enabled ) {
			v.addElement ( "StateCU input type is enabled" );
		}
		else {	v.addElement ( "StateCU input type is not enabled");
		}
		if ( __source_StateMod_enabled ) {
			v.addElement ( "StateMod input type is enabled" );
		}
		else {	v.addElement ( "StateMod input type is not enabled");
		}
		if ( __source_StateModB_enabled ) {
			v.addElement ( "StateModB input type is enabled" );
		}
		else {	v.addElement ( "StateModB input type is not enabled");
		}
		if ( __source_USGSNWIS_enabled ) {
			v.addElement ( "USGSNWIS input type is enabled" );
		}
		else {	v.addElement ( "USGSNWIS input type is not enabled");
		}
		new ReportJFrame ( v, reportProp );
		// Clean up...
		v = null;
		reportProp = null;
	}
        else if ( command.equals(__File_Properties_ColoradoSMS_String) ) {
		// Simple text display of HydroBase properties.
		PropList reportProp = new PropList ("Colorado SMS Properties");
		reportProp.set ( "TotalWidth", "600" );
		reportProp.set ( "TotalHeight", "300" );
		reportProp.set ( "DisplayFont", "Courier" );
		reportProp.set ( "DisplaySize", "11" );
		reportProp.set ( "PrintFont", "Courier" );
		reportProp.set ( "PrintSize", "7" );
		reportProp.set ( "Title", "Colorado SMS Properties" );
		Vector v = null;
		if ( __hbdmi == null ) {
			v.addElement ( "Colorado SMS Properties" );
			v.addElement ( "" );
			v.addElement("No Colorado SMS database is available." );
		}
		else {	v = __smsdmi.getDatabaseProperties();
		}
		new ReportJFrame ( v, reportProp );
		// Clean up...
		v = null;
		reportProp = null;
	}
        else if ( command.equals(__File_Properties_DIADvisor_String) ) {
		PropList reportProp = new PropList ("DIADvisor.props");
		// Too big (make this big when we have more stuff)...
		reportProp.set ( "TotalWidth", "600" );
		reportProp.set ( "TotalHeight", "300" );
		reportProp.set ( "DisplayFont", "Courier" );
		reportProp.set ( "DisplaySize", "11" );
		reportProp.set ( "PrintFont", "Courier" );
		reportProp.set ( "PrintSize", "7" );
		reportProp.set ( "Title", "DIADvisor Properties" );
		Vector v = new Vector();
		v.addElement ( "DIADvisor Operational Database:" );
		v.addElement ( "" );
		StringUtil.addListToStringList ( v,
			 __DIADvisor_dmi.getDatabaseProperties ( 3 ) );
		v.addElement ( "" );
		v.addElement ( "DIADvisor Archive Database:" );
		v.addElement ( "" );
		StringUtil.addListToStringList ( v,
			 __DIADvisor_archive_dmi.getDatabaseProperties ( 3 ) );
		new ReportJFrame ( v, reportProp );
		// Clean up...
		v = null;
		reportProp = null;
	}
        else if ( command.equals(__File_Properties_HydroBase_String) ) {
		// Simple text display of HydroBase properties.
		PropList reportProp = new PropList ("HydroBase Properties");
		reportProp.set ( "TotalWidth", "600" );
		reportProp.set ( "TotalHeight", "300" );
		reportProp.set ( "DisplayFont", "Courier" );
		reportProp.set ( "DisplaySize", "11" );
		reportProp.set ( "PrintFont", "Courier" );
		reportProp.set ( "PrintSize", "7" );
		reportProp.set ( "Title", "HydroBase Properties" );
		Vector v = null;
		if ( __hbdmi == null ) {
			v.addElement ( "HydroBase Properties" );
			v.addElement ( "" );
			v.addElement ( "No HydroBase database is available." );
		}
		else {	v = __hbdmi.getDatabaseProperties();
		}
		new ReportJFrame ( v, reportProp );
		// Clean up...
		v = null;
		reportProp = null;
	}
        else if ( command.equals(__File_Properties_NWSRFSFS5Files_String) ) {
		PropList reportProp = new PropList ("NWSRFSFS5Files.props");
		// Too big (make this big when we have more stuff)...
		reportProp.set ( "TotalWidth", "600" );
		reportProp.set ( "TotalHeight", "300" );
		reportProp.set ( "DisplayFont", "Courier" );
		reportProp.set ( "DisplaySize", "11" );
		reportProp.set ( "PrintFont", "Courier" );
		reportProp.set ( "PrintSize", "7" );
		reportProp.set ( "Title", "NWSRFS FS5Files Properties" );
		Vector v = null;
		if ( __nwsrfs_dmi == null ) {
			v = new Vector ( 1 );
			v.addElement (
			"The NWSRFS FS5Files connection is not open." );
		}
		else {	v = __nwsrfs_dmi.getDatabaseProperties ( 3 );
		}
		new ReportJFrame ( v, reportProp );
		// Clean up...
		v = null;
		reportProp = null;
	}
        else if ( command.equals(__File_Properties_RiversideDB_String) ) {
		PropList reportProp = new PropList ("RiversideDB.props");
		// Too big (make this big when we have more stuff)...
		reportProp.set ( "TotalWidth", "600" );
		reportProp.set ( "TotalHeight", "300" );
		reportProp.set ( "DisplayFont", "Courier" );
		reportProp.set ( "DisplaySize", "11" );
		reportProp.set ( "PrintFont", "Courier" );
		reportProp.set ( "PrintSize", "7" );
		reportProp.set ( "Title", "RiversideDB Properties" );
		Vector v = __rdmi.getDatabaseProperties ( 3 );
		new ReportJFrame ( v, reportProp );
		// Clean up...
		v = null;
		reportProp = null;
	}
        else if ( o == __File_SetWorkingDirectory_JMenuItem ) {
		JFileChooser fc = JFileChooserFactory.createJFileChooser (
			JGUIUtil.getLastFileDialogDirectory() );
		fc.setDialogTitle(
		"Set the Working Directory (normally only use if a commands" +
		" file was not opened or saved)");
		fc.setFileSelectionMode (JFileChooser.DIRECTORIES_ONLY );
		if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			String directory = fc.getSelectedFile().getPath();
			IOUtil.setProgramWorkingDir(directory);
			JGUIUtil.setLastFileDialogDirectory(directory);
			// REVISIT - is this needed with Swing?
			// Reset to make sure the ending delimiter is removed...
			__props.set("WorkingDir",IOUtil.getProgramWorkingDir());
			__initial_working_dir = __props.getValue ("WorkingDir");
			JGUIUtil.setLastFileDialogDirectory(directory);
		}
	}
        else if (command.equals(__Run_ProcessTSProductPreview_String) ) {
		try {	processTSProductFile ( true );
		}
		catch ( Exception te ) {
			Message.printWarning ( 1, "",
			"Error processing TSProduct file.\n" +
			"If necessary, make sure the working directory is\n" +
			"correct by running a setWorkingDir() command." );
			Message.printWarning ( 2, "", te );
		}
	}
        else if ( command.equals(__File_Exit_String) ) {
		closeClicked();
        }

	// Edit menu actions (in order of menu)...

        else if ( command.equals(__Edit_CutCommands_String) ) {
		// Need to think whether this should work for the time series
		// list or only the commands.  Copy to the buffer...
		setCommandListCutBuffer();
		// Now clear the list...
		clearCommands();
		__commands_dirty = true;
		checkGUIState();
	}
        else if ( command.equals(__Edit_CopyCommands_String) ) {
		// Copy to the buffer...
		setCommandListCutBuffer();
		__commands_dirty = true;
		checkGUIState();
	}
        else if ( command.equals(__Edit_PasteCommands_String) ) {
		// Copy the buffer to the final list...
		pasteCommandListCutBuffer();
		updateStatus ();
		__commands_dirty = true;
		checkGUIState();
	}
        else if ( command.equals(__Edit_DeleteCommands_String) ) {
		// Essentially the same as CUT
		setCommandListCutBuffer();
		// Now clear the list...
		clearCommands();
		__commands_dirty = true;
		checkGUIState();
	}
        else if ( command.equals(__Edit_SelectAllCommands_String) ) {
		JGUIUtil.selectAll(__commands_JList);
		updateStatus ();
		checkGUIState();
	}
        else if ( command.equals(__Edit_DeselectAllCommands_String) ) {
		deselectAllCommands ();
	}
        else if ( command.equals(__Edit_CommandsFile_String) ) {
		// Get the name of the file to edit and then use notepad...
		//
		// Instantiate a file dialog object with no default...
		//
		JFileChooser fc = JFileChooserFactory.createJFileChooser (
				JGUIUtil.getLastFileDialogDirectory() );
		fc.setDialogTitle ( "Select File" );
		if(__license_manager.getLicenseType().equalsIgnoreCase("CDSS")){
			// Ray Bennett favorite...
			fc.addChoosableFileFilter(
			new SimpleFileFilter("cmx", "TSTool Commands File") );
		}
		SimpleFileFilter sff = new SimpleFileFilter ( "TSTool",
			"TSTool Commands File" );
		fc.addChoosableFileFilter ( sff );
		fc.setFileFilter ( sff );
		if ( fc.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
			// Return if no file name is selected...
			return;
		}
		String path = fc.getSelectedFile().getPath();
		JGUIUtil.setLastFileDialogDirectory (
			fc.getSelectedFile().getParent() );
		String [] command_array = new String[2];
		if ( IOUtil.isUNIXMachine() ) {
			command_array[0] = "nedit";
		}
		else {	command_array[0] = "notepad";
		}
		if ( path != null ) {
			try {	command_array[1] = path;
				ProcessManager p = new ProcessManager(
				command_array );
				Thread thread = new Thread ( p );
				thread.start ();
			}
			catch ( Exception e2 ) {
				Message.printWarning ( 1, rtn,
				"Unable to edit file \"" + path + "\"" );
			}
		}
	}
	else if ( command.equals(__Edit_CommandNoErrorChecking_String) ) {
		// Edit the first selected item, unless a comment, in which
		// case all are edited...
		actionEditCommand ( false );
	}
	else if ( command.equals(__Edit_CommandWithErrorChecking_String) ) {
		// Edit the first selected item, unless a comment, in which
		// case all are edited...
		actionEditCommand ( true );
	}
	else if (command.equals(
		__Edit_ConvertSelectedCommandsToComments_String) ) {
		convertCommandsToComments ( true );
	}
	else if (command.equals(
		__Edit_ConvertSelectedCommandsFromComments_String) ) {
		convertCommandsToComments ( false );
	}
	// Popup only...
	else if (command.equals(__CommandsPopup_FindCommands_String) ) {
		new FindInJListJDialog(this,__commands_JList,"Find Command(s)");
		checkGUIState();
	}

	// Run menu (order in menu)...

        else if ( command.equals(__Run_AllCommandsCreateOutput_String) ) {
		// Process time series and create all output from write*
		// commands...
		runCommands ( true, true );
	}
        else if ( command.equals(__Run_AllCommandsIgnoreOutput_String) ) {
		// Process time series but ignore write* commands...
		runCommands ( true, false );
	}
        else if ( command.equals(__Run_SelectedCommandsCreateOutput_String) ) {
		// Process selected commands and create all output from write*
		// commands...
		runCommands ( false, true );
	}
        else if ( command.equals(__Run_SelectedCommandsIgnoreOutput_String) ) {
		// Process selected commands but ignore write* commands...
		runCommands ( false, false );
	}
        else if ( command.equals(__Run_CommandsFromFile_String) ) {
		// Get the name of the file to run and then execute a TSEngine
		// as if in batch mode...
		//
		// Instantiate a file dialog object with no default...
		//
/* REVISIT
		FileDialog fd = new FileDialog(this, "Run Commands File",
		FileDialog.LOAD );
		if ( __last_directory_selected != null ) {
			fd.setDirectory ( __last_directory_selected );
		}
		fd.setVisible(true);

		// Determine the name of the commands file as specified from 
		// the FileDialog object        

		// Return if no file name is selected

		if ( fd.getFile() == null || fd.getFile().equals("") ) {
			return;
		}
		if ( fd.getDirectory() != null ) {
			__last_directory_selected = fd.getDirectory();
		}

		String fileName = fd.getDirectory() + fd.getFile();        

		TSEngine engine = null;
		try {	// Process as if in batch mode...
			JGUIUtil.setWaitCursor ( this, true );
			engine = new TSEngine(__hbdmi, __rdmi, __DIADvisor_dmi,
					__DIADvisor_archive_dmi,
					__nwsrfs_dmi, __smsdmi );
			engine.processCommands(__hbdmi,fileName);
			engine = null;
			Message.printStatus ( 1, rtn,
			"Successfully processed commands file \"" + fileName +
			"\"" );
			JGUIUtil.setWaitCursor ( this, false );
		}
		catch ( Exception e ) {
			JGUIUtil.setWaitCursor ( this, false );
			Message.printWarning ( 1, rtn,
			"Error running commands file \"" + fileName + "\"" );
			Message.printWarning ( 2, rtn, e );
			engine = null;
		}
*/
	}
        else if (command.equals(__Run_ProcessTSProductOutput_String) ) {
		// Test code...
		try {	processTSProductFile ( false );
		}
		catch ( Exception te ) {
			Message.printWarning ( 1, "",
			"Error processing TSProduct file.\n" +
			"If necessary, make sure the working directory is\n" +
			"correct by running a setWorkingDir() command." );
			Message.printWarning ( 2, "", te );
		}
	}
	else {	// Chain to the next method...
		actionPerformed2(event);
	}
}

/**
Handle a group of actions.
@param event Event to handle.
*/
private void actionPerformed2 (ActionEvent event)
throws Exception
{	String command = event.getActionCommand();
	Object o = event.getSource();

	// Commands...

	// REVISIT SAM 2006-05-02
	// Why is all of this needed?  Should rely on the command factory for
	// commands.  Evaluate this when all commands are in the factory.

	// Create Time Series...

	if (command.equals( __Commands_Create_createFromList_String)){
		editCommand ( __Commands_Create_createFromList_String,
			null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Create_createTraces_String)){
		editCommand ( __Commands_Create_createTraces_String,
			null, __INSERT_COMMAND );
	}

	// Convert TS Identifier to read command...

	else if ( o == __Commands_ConvertTSIDTo_readTimeSeries_JMenuItem ) {
		editCommand ( __Commands_ConvertTSIDTo_readTimeSeries_String,
			getCommand(), __UPDATE_COMMAND );
	}
	else if ( o == __Commands_ConvertTSIDTo_readDateValue_JMenuItem ) {
		editCommand ( __Commands_ConvertTSIDTo_readDateValue_String,
			getCommand(), __UPDATE_COMMAND );
	}
	else if ( o == __Commands_ConvertTSIDTo_readHydroBase_JMenuItem ) {
		editCommand ( __Commands_ConvertTSIDTo_readHydroBase_String,
			getCommand(), __UPDATE_COMMAND );
	}
	else if ( o == __Commands_ConvertTSIDTo_readMODSIM_JMenuItem ) {
		editCommand ( __Commands_ConvertTSIDTo_readMODSIM_String,
			getCommand(), __UPDATE_COMMAND );
	}
	else if ( o == __Commands_ConvertTSIDTo_readNwsCard_JMenuItem ) {
		editCommand ( __Commands_ConvertTSIDTo_readNwsCard_String,
			getCommand(), __UPDATE_COMMAND );
	}
	else if ( o == __Commands_ConvertTSIDTo_readRiverWare_JMenuItem ) {
		editCommand ( __Commands_ConvertTSIDTo_readRiverWare_String,
			getCommand(), __UPDATE_COMMAND );
	}
	else if ( o == __Commands_ConvertTSIDTo_readStateMod_JMenuItem ) {
		editCommand ( __Commands_ConvertTSIDTo_readStateMod_String,
			getCommand(), __UPDATE_COMMAND );
	}
	else if ( o == __Commands_ConvertTSIDTo_readStateModB_JMenuItem ) {
		editCommand ( __Commands_ConvertTSIDTo_readStateModB_String,
			getCommand(), __UPDATE_COMMAND );
	}
	else if ( o == __Commands_ConvertTSIDTo_readUsgsNwis_JMenuItem ) {
		editCommand ( __Commands_ConvertTSIDTo_readUsgsNwis_String,
			getCommand(), __UPDATE_COMMAND );
	}

	// Read Time Series...

	else if (command.equals( __Commands_Read_readDateValue_String)){
		editCommand ( __Commands_Read_readDateValue_String,
			null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Read_readHydroBase_String)){
		editCommand ( __Commands_Read_readHydroBase_String,
			null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Read_readMODSIM_String)){
		editCommand ( __Commands_Read_readMODSIM_String,
			null, __INSERT_COMMAND );
	}
	else if (command.equals(
		__Commands_Read_readNwsCard_String)){
		editCommand ( __Commands_Read_readNwsCard_String,
			null, __INSERT_COMMAND );
	}
	else if (command.equals(
		__Commands_Read_readNWSRFSESPTraceEnsemble_String)){
		editCommand ( __Commands_Read_readNWSRFSESPTraceEnsemble_String,
			null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Read_readNWSRFSFS5Files_String)){
		editCommand ( __Commands_Read_readNWSRFSFS5Files_String,
			null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Read_readStateCU_String)){
		editCommand ( __Commands_Read_readStateCU_String,
			null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Read_readStateModB_String)){
		editCommand ( __Commands_Read_readStateModB_String,
			null, __INSERT_COMMAND );
	}
	// Put after longer versions...
	else if (command.equals( __Commands_Read_readStateMod_String)){
		editCommand ( __Commands_Read_readStateMod_String,
			null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Read_statemodMax_String)){
		editCommand ( __Commands_Read_statemodMax_String,
			null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Read_TS_readDateValue_String)){
		editCommand ( __Commands_Read_TS_readDateValue_String,
			null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Read_TS_readHydroBase_String)){
		editCommand ( __Commands_Read_TS_readHydroBase_String,
			null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Read_TS_readMODSIM_String)){
		editCommand ( __Commands_Read_TS_readMODSIM_String,
			null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Read_TS_readNDFD_String)){
		editCommand ( __Commands_Read_TS_readNDFD_String,
			null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Read_TS_readNwsCard_String)){
		editCommand ( __Commands_Read_TS_readNwsCard_String,
			null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Read_TS_readNWSRFSFS5Files_String)){
		editCommand ( __Commands_Read_TS_readNWSRFSFS5Files_String,
			null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Read_TS_readRiverWare_String)){
		editCommand ( __Commands_Read_TS_readRiverWare_String,
			null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Read_TS_readStateModB_String)){
		editCommand ( __Commands_Read_TS_readStateModB_String,
			null, __INSERT_COMMAND );
	}
	// Put after longer versions...
	else if (command.equals( __Commands_Read_TS_readStateMod_String)){
		editCommand ( __Commands_Read_TS_readStateMod_String,
			null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Read_TS_readUsgsNwis_String)){
		editCommand ( __Commands_Read_TS_readUsgsNwis_String,
			null, __INSERT_COMMAND );
	}
	else if (command.equals(__Commands_Read_setIncludeMissingTS_String)){
		editCommand ( __Commands_Read_setIncludeMissingTS_String,
			null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Read_setInputPeriod_String) ) {
		editCommand ( __Commands_Read_setInputPeriod_String,
			null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Create_TS_average_String)){
		editCommand ( __Commands_Create_TS_average_String,
			null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Create_TS_changeInterval_String)){
		editCommand ( __Commands_Create_TS_changeInterval_String,
			null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Create_TS_copy_String)){
		editCommand ( __Commands_Create_TS_copy_String,
			null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Create_TS_disaggregate_String)){
		editCommand ( __Commands_Create_TS_disaggregate_String,
			null, __INSERT_COMMAND );
	}
	else if (command.equals(
		__Commands_Create_TS_newDayTSFromMonthAndDayTS_String)){
		editCommand (
			__Commands_Create_TS_newDayTSFromMonthAndDayTS_String,
			null, __INSERT_COMMAND );
	}
	else if (command.equals(
		__Commands_Create_TS_newEndOfMonthTSFromDayTS_String) ) {
		editCommand (
			__Commands_Create_TS_newEndOfMonthTSFromDayTS_String,
			getCommand(), __INSERT_COMMAND );
	}
	else if (command.equals(
		__Commands_Create_TS_newStatisticYearTS_String)){
		editCommand ( __Commands_Create_TS_newStatisticYearTS_String,
			null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Create_TS_newTimeSeries_String)){
		editCommand ( __Commands_Create_TS_newTimeSeries_String,
			null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Create_TS_normalize_String)){
		editCommand ( __Commands_Create_TS_normalize_String,
			null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Create_TS_relativeDiff_String)){
		editCommand ( __Commands_Create_TS_relativeDiff_String,
			null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Create_TS_weightTraces_String)){
		editCommand ( __Commands_Create_TS_weightTraces_String,
			null, __INSERT_COMMAND );
	}

	// Commands... Fill Time Series...

	else if (command.equals( __Commands_Fill_fillCarryForward_String)){
		editCommand ( __Commands_Fill_fillCarryForward_String,
			null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Fill_fillConstant_String)){
		editCommand ( __Commands_Fill_fillConstant_String,
			null, __INSERT_COMMAND );
	}
	else if (command.equals(
		__Commands_Fill_fillDayTSFrom2MonthTSAnd1DayTS_String)){
		editCommand (
		__Commands_Fill_fillDayTSFrom2MonthTSAnd1DayTS_String,
			null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Fill_fillFromTS_String)){
		editCommand ( __Commands_Fill_fillFromTS_String,
			null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Fill_fillHistMonthAverage_String)){
		editCommand ( __Commands_Fill_fillHistMonthAverage_String,
			null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Fill_fillHistYearAverage_String)){
		editCommand ( __Commands_Fill_fillHistYearAverage_String,
			null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Fill_fillInterpolate_String)){
		editCommand ( __Commands_Fill_fillInterpolate_String,
			null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Fill_fillMixedStation_String)){
		editCommand ( __Commands_Fill_fillMixedStation_String,
			null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Fill_fillMOVE1_String)){
		editCommand ( __Commands_Fill_fillMOVE1_String,
			null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Fill_fillMOVE2_String)){
		editCommand ( __Commands_Fill_fillMOVE2_String,
			null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Fill_fillPattern_String) ) {
		editCommand ( __Commands_Fill_fillPattern_String,
			null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Fill_fillProrate_String) ) {
		editCommand ( __Commands_Fill_fillProrate_String,
			null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Fill_fillRegression_String) ) {
		editCommand ( __Commands_Fill_fillRegression_String,
			null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Fill_fillRepeat_String) ) {
		editCommand ( __Commands_Fill_fillRepeat_String,
			null, __INSERT_COMMAND );
	}
	else if(command.equals(
		__Commands_Fill_fillUsingDiversionComments_String)){
		editCommand(__Commands_Fill_fillUsingDiversionComments_String,
			null, __INSERT_COMMAND );
	}
	else if (command.equals(__Commands_Fill_setAutoExtendPeriod_String)){
		editCommand ( __Commands_Fill_setAutoExtendPeriod_String,
			null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Fill_setAveragePeriod_String) ) {
		editCommand ( __Commands_Fill_setAveragePeriod_String,
			null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Fill_setIgnoreLEZero_String) ) {
		editCommand ( __Commands_Fill_setIgnoreLEZero_String,
			null, __INSERT_COMMAND );
	}
	else if (command.equals(__Commands_Fill_setMissingDataValue_String)){
		editCommand ( __Commands_Fill_setMissingDataValue_String,
			null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Fill_setPatternFile_String) ) {
		editCommand ( __Commands_Fill_setPatternFile_String,
			null, __INSERT_COMMAND );
	}
	else if (command.equals(__Commands_Fill_setRegressionPeriod_String)){
		editCommand ( __Commands_Fill_setRegressionPeriod_String,
			null, __INSERT_COMMAND );
	}

	else if (command.equals( __Commands_Set_replaceValue_String)){
		editCommand ( __Commands_Set_replaceValue_String,
			null, __INSERT_COMMAND );
	}

	else if (command.equals( __Commands_Set_setConstant_String)){
		editCommand ( __Commands_Set_setConstant_String,
			null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Set_setConstantBefore_String)){
		editCommand ( __Commands_Set_setConstantBefore_String,
			null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Set_setDataValue_String)){
		editCommand ( __Commands_Set_setDataValue_String,
			null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Set_setFromTS_String)){
		editCommand ( __Commands_Set_setFromTS_String,
			null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Set_setMax_String)){
		editCommand ( __Commands_Set_setMax_String,
			null, __INSERT_COMMAND );
	}

	// Commands... Manipulate Time Series...

	else if (command.equals( __Commands_Manipulate_add_String)){
		editCommand ( __Commands_Manipulate_add_String,
			null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Manipulate_addConstant_String)){
		editCommand ( __Commands_Manipulate_addConstant_String,
			null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Manipulate_adjustExtremes_String)){
		editCommand ( __Commands_Manipulate_adjustExtremes_String,
			null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Manipulate_ARMA_String)){
		editCommand ( __Commands_Manipulate_ARMA_String,
			null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Manipulate_blend_String)){
		editCommand ( __Commands_Manipulate_blend_String,
			null, __INSERT_COMMAND );
	}
	else if (command.equals(__Commands_Manipulate_convertDataUnits_String)){
		editCommand ( __Commands_Manipulate_convertDataUnits_String,
			null, __INSERT_COMMAND );
	}
	else if (command.equals(__Commands_Manipulate_cumulate_String) ) {
		editCommand ( __Commands_Manipulate_cumulate_String,
			null, __INSERT_COMMAND );
	}
	else if (command.equals(__Commands_Manipulate_divide_String) ) {
		editCommand ( __Commands_Manipulate_divide_String,
			null, __INSERT_COMMAND );
	}
	else if (command.equals(__Commands_Manipulate_free_String) ) {
		editCommand ( __Commands_Manipulate_free_String,
			null, __INSERT_COMMAND );
	}
	else if (command.equals(__Commands_Manipulate_multiply_String) ) {
		editCommand ( __Commands_Manipulate_multiply_String,
			null, __INSERT_COMMAND );
	}
	else if (command.equals(__Commands_Manipulate_runningAverage_String) ) {
		editCommand ( __Commands_Manipulate_runningAverage_String,
			null, __INSERT_COMMAND );
	}
	else if (command.equals(__Commands_Manipulate_scale_String) ) {
		editCommand ( __Commands_Manipulate_scale_String,
			null, __INSERT_COMMAND );
	}
	else if (command.equals(
		__Commands_Manipulate_shiftTimeByInterval_String) ) {
		editCommand ( __Commands_Manipulate_shiftTimeByInterval_String,
			null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Manipulate_subtract_String)){
		editCommand ( __Commands_Manipulate_subtract_String,
			null, __INSERT_COMMAND );
	}

	// Commands... Analyze Time Series...

	else if (command.equals( __Commands_Analyze_analyzePattern_String)){
		editCommand ( __Commands_Analyze_analyzePattern_String,
			null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Analyze_compareTimeSeries_String)){
		editCommand ( __Commands_Analyze_compareTimeSeries_String,
			null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Analyze_newDataTest_String)){
		editCommand ( __Commands_Analyze_newDataTest_String,
			null, __INSERT_COMMAND );
	}
	else if (command.equals(
		__Commands_Analyze_readDataTestFromRiversideDB_String)){
		editCommand (
			__Commands_Analyze_readDataTestFromRiversideDB_String,
			null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Analyze_runDataTests_String)){
		editCommand ( __Commands_Analyze_runDataTests_String,
			null, __INSERT_COMMAND );
	}
	else if (command.equals(
		__Commands_Analyze_processDataTestResults_String)){
		editCommand ( __Commands_Analyze_processDataTestResults_String,
			null, __INSERT_COMMAND );
	}

	// Commands... Models...

	else if (command.equals( __Commands_Models_lagK_String)){
		editCommand ( __Commands_Models_lagK_String,
			null, __INSERT_COMMAND );
	}

	// Commands... Output Time Series...

	else if (command.equals( __Commands_Output_deselectTimeSeries_String)){
		editCommand ( __Commands_Output_deselectTimeSeries_String,
			null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Output_selectTimeSeries_String)){
		editCommand ( __Commands_Output_selectTimeSeries_String,
			null, __INSERT_COMMAND );
	}
	else if (command.equals(
		__Commands_Output_setOutputDetailedHeaders_String) ) {
		editCommand (__Commands_Output_setOutputDetailedHeaders_String,
			null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Output_setOutputPeriod_String) ) {
		editCommand ( __Commands_Output_setOutputPeriod_String,
			null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Output_setOutputYearType_String) ){
		editCommand ( __Commands_Output_setOutputYearType_String,
			null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Output_sortTimeSeries_String)){
		editCommand ( __Commands_Output_sortTimeSeries_String,
			null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Output_writeDateValue_String)){
		editCommand ( __Commands_Output_writeDateValue_String,
			null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Output_writeNwsCard_String)){
		editCommand ( __Commands_Output_writeNwsCard_String,
			null, __INSERT_COMMAND );
	}
	else if(command.equals(
		__Commands_Output_writeNWSRFSESPTraceEnsemble_String)){
		editCommand (
			__Commands_Output_writeNWSRFSESPTraceEnsemble_String,
			null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Output_writeRiverWare_String)){
		editCommand ( __Commands_Output_writeRiverWare_String,
			null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Output_writeStateCU_String)){
		editCommand ( __Commands_Output_writeStateCU_String,
			null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Output_writeStateMod_String)){
		editCommand ( __Commands_Output_writeStateMod_String,
			null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Output_writeSummary_String)){
		editCommand ( __Commands_Output_writeSummary_String,
			null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Output_processTSProduct_String)){
		editCommand ( __Commands_Output_processTSProduct_String,
			null, __INSERT_COMMAND );
	}
	else {	// Chain to next list of commands...
		actionPerformed3 ( event );
	}
}

/**
Handle a group of actions.
@param event Event to handle.
*/
private void actionPerformed3 (ActionEvent event)
throws Exception
{	String command = event.getActionCommand();
	String routine = "TSTool_JFrame.actionPerformed3";
	Object o = event.getSource();

	// HydroBase commands...

	if (command.equals(__Commands_HydroBase_openHydroBase_String)){
		editCommand ( __Commands_HydroBase_openHydroBase_String,
			null, __INSERT_COMMAND );
	}

	// NDFD commands...

	if (command.equals(__Commands_NDFD_openNDFD_String)){
		editCommand ( __Commands_NDFD_openNDFD_String,
			null, __INSERT_COMMAND );
	}

	// General commands...

	else if (command.equals( __Commands_General_startLog_String) ) {
		editCommand ( __Commands_General_startLog_String,
			null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_General_setDebugLevel_String) ) {
		editCommand ( __Commands_General_setDebugLevel_String,
			null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_General_setWarningLevel_String) ) {
		editCommand ( __Commands_General_setWarningLevel_String,
			null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_General_setWorkingDir_String) ) {
		editCommand ( __Commands_General_setWorkingDir_String,
			null, __INSERT_COMMAND );
	}
	else if (command.equals(__Commands_General_Comment_String) ) {
		editCommand ( __Commands_General_Comment_String,
			null, __INSERT_COMMAND );
	}
	else if (command.equals(__Commands_General_startComment_String) ) {
		editCommand ( __Commands_General_startComment_String,
			null, __INSERT_COMMAND );
	}
	else if (command.equals(__Commands_General_endComment_String) ) {
		editCommand ( __Commands_General_endComment_String,
			null, __INSERT_COMMAND );
	}
	else if (command.equals(__Commands_General_exit_String) ) {
		int selected_size = JGUIUtil.selectedSize ( __commands_JList );
		if ( selected_size > 0 ) {
			// Add after the last selected item...
			int selected_indices[] =
				__commands_JList.getSelectedIndices();
			if (	selected_indices[selected_size - 1] ==
				(__commands_JListModel.size() - 1) ) {
			}
			else {	__commands_JListModel.insertElementAt (
				"exit",
				(selected_indices[selected_size - 1] + 1 ) );
			}
			selected_indices = null;
		}
		else {	// Add at end...
			addCommand ( "exit" );
		}
	}
	else if (command.equals( __Commands_General_compareFiles_String)){
		editCommand ( __Commands_General_compareFiles_String,
			null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_General_runCommands_String) ) {
		editCommand ( __Commands_General_runCommands_String,
			null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_General_runProgram_String) ) {
		editCommand ( __Commands_General_runProgram_String,
			null, __INSERT_COMMAND );
	}

	// View menu (order of GUI)...

        else if ( command.equals(__Results_Graph_AnnualTraces_String) ) {
		// Only do if data are selected...
		String response = new TextResponseJDialog ( 
			this, "Graph Annual Traces", 
			"Enter the 4-digit year to use as a reference.",
			ResponseJDialog.OK|ResponseJDialog.CANCEL ).response();
		if ( response == null ) {
			return;
		}
		graphTS("-oannual_traces_graph " + StringUtil.atoi(response) );
	}
        else if ( command.equals(__Results_Graph_BarsLeft_String) ) {
		graphTS("-obar_graph", "BarsLeftOfDate");
	}
        else if ( command.equals(__Results_Graph_BarsCenter_String) ) {
		graphTS("-obar_graph", "BarsCenteredOnDate");
	}
        else if ( command.equals(__Results_Graph_BarsRight_String) ) {
		graphTS("-obar_graph", "BarsRightOfDate");
	}
        else if ( command.equals(__Results_Graph_DoubleMass_String) ) {
			//Message.printWarning ( 1, rtn, "Two time " +
			//"series can be graphed for Double Mass graph." );
		//{}
		//else {	
		graphTS("-odoublemassgraph");
		//}
	}
        else if ( command.equals(__Results_Graph_Duration_String) ) {
		// Only do if data are selected...
		graphTS("-oduration_graph " );
	}
        else if ( command.equals(__Results_Graph_Line_String) ) {
		graphTS("-olinegraph");
	}
        else if ( command.equals(__Results_Graph_LineLogY_String) ) {
		graphTS("-olinelogygraph");
	}
/* Not enabled.
        else if ( command.equals("GraphPercentExceed") ) {
		// Because Visualize can only handle grid-based data, only allow
		// one time series to be graphed to ensure that the data set is
		// nice...
		if ( _final__commands_JList != 1 ) {
			Message.printWarning ( 1, rtn, "Only one time " +
			"series can be graphed for Percent Exceedance Curve." );
		}
		else {	// OK to do the graph...
			graphTS("-opercentexceedgraph");
		}
	}
*/
        else if ( command.equals(__Results_Graph_PeriodOfRecord_String) ) {
		graphTS("-oporgraph");
	}
        else if ( command.equals(__Results_Graph_Point_String) ) {
		graphTS("-opointgraph");
	}
        else if ( command.equals(__Results_Graph_PredictedValue_String) ) {
		graphTS("-oPredictedValue_graph" );
	}
        else if (command.equals(__Results_Graph_PredictedValueResidual_String)){
		graphTS("-oPredictedValueResidual_graph" );
	}
        else if ( command.equals(__Results_Graph_XYScatter_String) ) {
		graphTS("-oxyscatter_graph" );
	}

        else if ( command.equals(__Results_Table_String) ) {
		// For now handle similar to the summary report, forcing a
		// preview...
		export("-otable", "-preview" );
	}

        else if ( command.equals(__Results_Report_Summary_String) ) {
		// SAM.  Let the ReportJFrame prompt for this.  This is
		// different from the StateMod output in that when a summary is
		// exported in the GUI, the user views first and then saves to
		// disk.
		export("-osummary", "-preview" );
	}

	// Only on View pop-up...

	else if (command.equals(__Results_FindTimeSeries_String) ) {
		new FindInJListJDialog ( this, __ts_JList, "Find Time Series" );
	}
        else if ( command.equals(__Results_TimeSeriesProperties_String) ) {
		// Get the first time series selected in the view window...
		if ( __final_ts_engine != null ) {
			int pos = 0;
			if ( JGUIUtil.selectedSize(__ts_JList) == 0 ) {
				pos = 0;
			}
			else {	pos = JGUIUtil.selectedIndex(__ts_JList,0);
			}
			// Now display the properties...
			if ( pos >= 0 ) {
				new TSPropertiesJFrame ( this,
				__final_ts_engine.getTimeSeries(pos) );
			}
		}
	}

	else if ( o == __Tools_Analysis_MixedStationAnalysis_JMenuItem ) {
		// Create the dialog using the available time series...
		try {	Command c = new fillMixedStation_Command(false);
			c.initializeCommand ( command,
				new TSCommandProcessor(__final_ts_engine),
				null,	// Command tag
				1,	// Warning level
				false );// Minimal initialization
			new fillMixedStation_JDialog ( this, c, 0 );
		}
		catch ( Exception e ) {
			Message.printWarning ( 1, routine,
			"Error performing mixed station analysis." );
			Message.printWarning ( 2, routine, e );
		}
	}
	else if (command.equals(__Tools_Report_DataCoverageByYear_String) ) {
		// Use graph TS because it does not take an output file...
		graphTS ( "-odata_coverage_report " );
	}
	else if (command.equals(__Tools_Report_DataLimitsSummary_String) ) {
		// Use graph TS because it does not take an output file...
		graphTS ( "-odata_limits_report " );
	}
	else if (command.equals(__Tools_Report_MonthSummaryDailyMeans_String)) {
		// Use graph TS because it does not take an output file...
		graphTS ( "-omonth_mean_summary_report " );
	}
	else if (command.equals(__Tools_Report_MonthSummaryDailyTotals_String)){
		// Use graph TS because it does not take an output file...
		graphTS ( "-omonth_total_summary_report " );
	}
	else if (command.equals(__Tools_Report_YearToDateTotal_String) ) {
		// Set the averaging end date...
		PropList props = new PropList( "DateBuilder properties" );
		props.set ( "DatePrecision", "Day" );
		props.set ( "DateFormat", "US" );
		props.set ( "EnableYear", "false" );
		props.set ( "DateLabel",
			"Enter Ending Date for Annual Total:" );
		props.set ( "Title",
			"Enter Ending Date for Annual Total" );
		DateTime to = new DateTime ( DateTime.DATE_CURRENT |
					DateTime.PRECISION_DAY );
		try {	JTextField tmpJTextField = new JTextField();
			new DateTimeBuilderJDialog ( this, tmpJTextField, to,
					props );
			to = DateTime.parse ( tmpJTextField.getText() ); 
		}
		catch ( Exception ex ) {
			Message.printWarning ( 1, "TSTool_JFrame",
			"There was an error processing the date for the " +
			"report." );
		}
		// Use graph TS because it does not take an output file...
		graphTS ( "-oyear_to_date_report ",
			to.toString(DateTime.FORMAT_MM_SLASH_DD) );
	}
	else if ( o == __Tools_NWSRFS_ConvertNWSRFSESPTraceEnsemble_JMenuItem ){
		// Prompt for the ESP file...
		JFileChooser fc = JFileChooserFactory.createJFileChooser (
				JGUIUtil.getLastFileDialogDirectory() );
		fc.setDialogTitle ( "Select NWSRFS ESP Trace Ensemble File" );
		SimpleFileFilter sff = new SimpleFileFilter ( "CS",
			"Conditional Simulation Ensemble File" );
		fc.addChoosableFileFilter ( sff );
		fc.setFileFilter ( sff );
		if ( fc.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
			// Return if no file name is selected...
			return;
		}
		String esp_file = fc.getSelectedFile().getPath();
		String directory = fc.getSelectedFile().getParent();
		// Prompt for the output file...
		fc = JFileChooserFactory.createJFileChooser ( directory );
		fc.setDialogTitle ( "Select Text Output File" );
		sff = new SimpleFileFilter (
			"txt","NWSRFS ESP Ensemble File as Text" );
		fc.addChoosableFileFilter ( sff );
		fc.setSelectedFile ( new File(esp_file + ".txt") );
		fc.setFileFilter ( sff );
		if ( fc.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
			// Return if no file name is selected...
			return;
		}
		directory = fc.getSelectedFile().getParent();
		JGUIUtil.setLastFileDialogDirectory ( directory );
		String out_file = fc.getSelectedFile().getPath();
		String units = new TextResponseJDialog ( 
			this, "Specify Output Units",
			"Specify the output units for the file (blank to " +
			"default).",
			ResponseJDialog.OK| ResponseJDialog.CANCEL ).response();
		if ( units == null ) {
			return;
		}
		units = units.trim();
		// Convert...
		try {	NWSRFS_ESPTraceEnsemble.convertESPTraceEnsembleToText (
			esp_file, IOUtil.enforceFileExtension(out_file,"txt"),
			units );
		}
		catch ( Exception e ) {
			Message.printWarning ( 1, "TSTool_JFrame",
			"There was an error converting the ESP trace " +
			"ensemble file to text.  " +
			"Partial results may be available." );
		}
	}
	else if ( o == __Tools_NWSRFS_ConvertJulianHour_JMenuItem ) {
		// Display the NWSRFS dialog...
		new NWSRFS_ConvertJulianHour_JDialog(this);
	}
	else if ( o == __Tools_RiversideDB_TSProductManager_JMenuItem ) {
		Vector rdmi_Vector = new Vector();
		if ( __rdmi != null ) {
			rdmi_Vector.addElement ( __rdmi );
		}
		new RiversideDB_TSProductManager_JFrame ( rdmi_Vector, null );
	}
	else if ( o == __Tools_SelectOnMap_JMenuItem ) {
		try {	selectOnMap ();
		}
		catch ( Exception e ) {
			JGUIUtil.setWaitCursor ( this, false );
			Message.printWarning ( 1, routine,
			"Unable to select locations on map." );
			Message.printWarning ( 3, routine, e );
		}
	}
	else if ( o == __Tools_Options_JMenuItem ) {
		new TSTool_Options_JDialog ( this, __props );
		// Reset as necessary...
		if ( __query_TableModel instanceof TSTool_HydroBase_TableModel){
			((TSTool_HydroBase_TableModel)__query_TableModel).
			setWDIDLength(StringUtil.atoi(
			__props.getValue("HydroBase.WDIDLength")));
		}
		else if ( __query_TableModel instanceof 
			TSTool_HydroBase_WellLevel_Day_TableModel){
			((TSTool_HydroBase_WellLevel_Day_TableModel)
			__query_TableModel).setWDIDLength(StringUtil.atoi(
			__props.getValue("HydroBase.WDIDLength")));
		}		
	}

	// Help menu (order of GUI)...

	else if ( command.equals ( __Help_AboutTSTool_String )) {
		showHelpAbout ();
	}
	else if (command.equals("Test") ) {
		// Test code...
		try {	test();
		}
		catch ( Exception te ) {
			Message.printWarning ( 1, "", "Error running test" );
			Message.printWarning ( 2, "", te );
		}
	}
}

/**
Add a command at the end of the command list and force the GUI state to be
updated.
@param command Command to add at the end of the list.
*/
public void addCommand ( String command )
{	addCommand ( command, false );	
}

/**
Add a command at the end of the command list.
@param command Command to add at the end of the list.
@param check_gui_state If true, the GUI state is checked.
*/
public void addCommand ( String command, boolean check_gui_state )
{	__commands_JListModel.addElement ( command );
	setCommandsDirty ( true );
	updateStatus ( check_gui_state );
}

/**
Add a list of commands at the end of the command list, updating the GUI state
and status after adding.
@param commands Vector of commands to add at the end of the list.
*/
public void addCommands ( Vector commands )
{	int size = 0;
	if ( commands != null ) {
		size = commands.size();
	}
	for ( int i = 0; i < size; i++ ) {
		__commands_JListModel.addElement (
		(String)commands.elementAt(i) );
	}
	setCommandsDirty ( true );
	updateStatus ( true );
}

/**
Add a time series at the end of the time series list.
@param ts_info Time series information to add at the end of the list.
*/
private void addTimeSeries ( String ts_info )
{	__ts_JListModel.addElement ( ts_info );
	//setCommandsDirty ( true );
	//updateStatus ();
}

/**
Display the results in a string format.  These results are APPENDED to the list
of strings already found in the __commands_JList.  
@param location Location part of TSIdent.
@param source Source part of TSIdent.
@param type Data type part of TSIdent.
@param interval Interval part of TSIdent.
@param scenario Scenario part of TSIdent (null or empty for no scenario).
@param sequence_number Sequence number part of TSIdent (null or empty for no
sequence number).
@param input_type The type of input (e.g., DateValue, NWSCard, USGSNWIS).
@param input_name The input location for the data (a database connection or
file name).
@param comment Comment for time series (null or empty for no comment).
@param use_alias If true, then the location contains the alias and the other
TSID fields are ignored when putting together the TSID command.
*/
private void appendResultsToCommands (	String location,
					String source,
					String type,
					String interval,
					String scenario,
					String sequence_number,
					String input_type,
					String input_name,
					String comment,
					boolean use_alias )
{	// Add after the last selected item or at the end if nothing
	// is selected.
	int selected_indices[] = __commands_JList.getSelectedIndices();
	int selected_size = 0;
	if ( selected_indices != null ) {
		selected_size = selected_indices.length;
	}
	int insert_pos = 0;
	String scenario2 = "";
	if ( (scenario != null) && !scenario.equals("") ) {
		scenario2 = "." + scenario;
	}
	String sequence_number2 = "";
	if ( (sequence_number != null) && !sequence_number.equals("") ) {
		sequence_number2 = "[" + sequence_number + "]";
	}
	// If there is an input_name, always use an input_type.
	String input = "";
	if ( (input_name != null) && (input_name.length() != 0) ) {
		input = "~" + input_type + "~" + input_name;
	}
	else if ( (input_type != null) && (input_type.length() != 0) ) {
		// Only have the input type...
		input = "~" + input_type;
	}
	if ( selected_size > 0 ) {
		// Insert after the last item.
		insert_pos = selected_indices[selected_size - 1] + 1;
	}
	else {	// Insert at the end...
		insert_pos = __commands_JListModel.size();
	}
	
	int i = 0;	// Leave this in case we add looping.
	int offset = 0;	// Handles cases where a comment is inserted.
	if ( (comment != null) && !comment.equals("") ) {
		__commands_JListModel.insertElementAt ( "# " + comment,
			(insert_pos + i*2));
		offset = 1;
	}
	String tsident_string = null;
	if ( use_alias ) {
		// Just use the short alias...
		tsident_string = location + input;
	}
	else {	// Use a full time series identifier...
		tsident_string = location + "." + source + "." + type + "." +
			interval + scenario2 + sequence_number2 + input;
	}
	if ( Message.isDebugOn ) {
		Message.printDebug ( 1, "", "Inserting \"" + tsident_string +
		"\" at " + (insert_pos + i*2 + offset) );
	}
	__commands_JListModel.insertElementAt ( tsident_string,
		(insert_pos + i*2 + offset));
	setCommandsDirty ( true );
	updateStatus();
}

/**
Enable/disable the ColoradoSMS input type features depending on whether a
ColoradoSMS connection has been made.
*/
private void checkColoradoSMSFeatures ()
{	if ( (__smsdmi != null) && __smsdmi.isOpen() ) {
		/* REVISIT SAM 2005-10-18 Currently time series features are not
		available...
		if ( __input_type_JComboBox != null ) {
			// Make sure HydroBase is in the input type list...
			int count = __input_type_JComboBox.getItemCount();
			boolean hbfound = false;
			for ( int i = 0; i < count; i++ ) {
				if ( __input_type_JComboBox.getItem(i).equals(
					__INPUT_TYPE_ColoradoSMS) ) {
					hbfound = true;
					break;
				}
			}
			if ( !hbfound ) {
				// Repopulate the input types...
				setInputTypeChoices();
			}
		}
		*/
		JGUIUtil.setEnabled (
			__File_Properties_ColoradoSMS_JMenuItem, true );
	}
	else {	// Remove ColoradoSMS from the data source list if necessary...
		/* REVISIT SAM 2005-10-18 Currently time series cannot be
		queried
		try {	__input_type_JComboBox.remove ( __INPUT_TYPE_HydroBase);
		}
		catch ( Exception e ) {
			// Ignore - probably already removed...
		}
		*/
		JGUIUtil.setEnabled (
			__File_Properties_ColoradoSMS_JMenuItem, false );
	}
}

/**
Enable/disable DIADvisor menus and choices based on whether a connection was
successful.
*/
private void checkDIADvisorFeatures()
{	if (	(__DIADvisor_dmi != null) &&
		(__DIADvisor_archive_dmi != null) &&
		(__input_type_JComboBox != null) ) {
		// Make sure DIADvisor is in the data source list...
		int count = __input_type_JComboBox.getItemCount();
		boolean rfound = false;
		for ( int i = 0; i < count; i++ ) {
			if ( __input_type_JComboBox.getItem(i).equals(
				__INPUT_TYPE_DIADvisor) ) {
				rfound = true;
				break;
			}
		}
		if ( !rfound ) {
			// Repopulate the data sources...
			setInputTypeChoices();
		}
		JGUIUtil.setEnabled(__File_Properties_DIADvisor_JMenuItem,true);
	}
	else {	// Remove DIADvisor from the data source list if necessary...
		try {	__input_type_JComboBox.remove ( __INPUT_TYPE_DIADvisor);
		}
		catch ( Exception e ) {
			// Ignore - probably already removed...
		}
		JGUIUtil.setEnabled ( __File_Properties_DIADvisor_JMenuItem,
			false );
	}
}

/**
Check to make sure necessary input is available for dialogs, including lists
of available static data.
*/
private void checkDialogInput ()
{	/* REVISIT for TSTool
	if ( __region1_Vector == null ) {
		// Get the counties from HydroBase...
		__region1_Vector = new Vector();
		if ( __hbdmi != null ) {
			Vector v = __hbdmi.getCountyRef();
			int size = 0;
			if ( v != null ) {
				size = v.size();
			}
			HydroBase_CountyRef county = null;
			for ( int i = 0; i < size; i++ ) {
				county = (HydroBase_CountyRef)v.elementAt(i);
				__region1_Vector.addElement (
					county.getCounty() );
			}
		}
	}
	if ( __region2_Vector == null ) {
		// Get the HUC from HydroBase...
		__region2_Vector = new Vector();
		if ( __hbdmi != null ) {
			__region2_Vector = __hbdmi.getHUC();
		}
	}
	if ( __cropchar_cumethod_Vector == null ) {
		// Get the Cropchar CU methods from HydroBase...
		__cropchar_cumethod_Vector = new Vector();
		if ( __hbdmi != null ) {
			Vector v = __hbdmi.getCropcharCUMethod();
			int size = 0;
			if ( v != null ) {
				size = v.size();
			}
			HydroBase_Cropchar cropchar = null;
			for ( int i = 0; i < size; i++ ) {
				cropchar = (HydroBase_Cropchar)v.elementAt(i);
				__cropchar_cumethod_Vector.addElement (
					cropchar.getMethod_desc() );
			}
		}
	}
	if ( __blaney_criddle_cumethod_Vector == null ) {
		// Get the CU methods from HydroBase...
		__blaney_criddle_cumethod_Vector = new Vector();
		if ( __hbdmi != null ) {
			Vector v = __hbdmi.getBlaneyCriddleCUMethod();
			int size = 0;
			if ( v != null ) {
				size = v.size();
			}
			HydroBase_CUBlaneyCriddle cubc = null;
			for ( int i = 0; i < size; i++ ) {
				cubc = (HydroBase_CUBlaneyCriddle)
				v.elementAt(i);
				__blaney_criddle_cumethod_Vector.addElement (
					cubc.getMethod_desc() );
			}
		}
	}
	*/
}

/**
Check the state of the GUI and enable/disable features as necessary.  For
example, if no time series are selected, disable output options.
*/
private void checkGUIState ()
{	// Early on the GUI may not be initialized and some of the components
	// seem to still be null...

	if ( !__gui_initialized ) {
		return;
	}

	boolean enabled = true;		// Used to enable/disable main menus
					// based on submenus.

	// Get the needed list sizes...

	int query_list_size = 0;
	if ( __query_TableModel != null ) {
		query_list_size = __query_TableModel.getRowCount();
	}

	int command_list_size = 0;
	int selected_commands_size = 0;
	if ( __commands_JListModel != null ) {
		command_list_size = __commands_JListModel.size();
		selected_commands_size = JGUIUtil.selectedSize (
						__commands_JList );
	}

	int ts_list_size = 0;
	if ( __ts_JListModel != null ) {
		ts_list_size = __ts_JListModel.size();
	}

	// List in the order of the GUI.  Popup menu items are checked as
	// needed mixed in below...

	// File menu...

	enabled = false;	// Use for File...Save menu.
	if ( command_list_size > 0 ) {
		// Have commands shown...
		if ( __commands_dirty  ) {
			JGUIUtil.setEnabled ( __File_Save_Commands_JMenuItem,
			true );
		}
		else {	JGUIUtil.setEnabled ( __File_Save_Commands_JMenuItem,
			false );
		}
		JGUIUtil.setEnabled ( __File_Save_CommandsAs_JMenuItem, true );
		JGUIUtil.setEnabled ( __File_Print_Commands_JMenuItem, true );
		JGUIUtil.setEnabled ( __File_Print_JMenu, true );
		enabled = true;
	}
	else {	// No commands are shown.
		JGUIUtil.setEnabled ( __File_Save_Commands_JMenuItem, false );
		JGUIUtil.setEnabled ( __File_Save_CommandsAs_JMenuItem, false );
		JGUIUtil.setEnabled ( __File_Print_Commands_JMenuItem, false );
		JGUIUtil.setEnabled ( __File_Print_JMenu, false );
	}
	if ( ts_list_size > 0 ) {
		JGUIUtil.setEnabled ( __File_Save_TimeSeriesAs_JMenuItem, true);
		enabled = true;
	}
	else {	JGUIUtil.setEnabled ( __File_Save_TimeSeriesAs_JMenuItem,false);
	}
	JGUIUtil.setEnabled ( __File_Save_JMenu, enabled );

	if ( __smsdmi != null ) {
		JGUIUtil.setEnabled ( __File_Properties_ColoradoSMS_JMenuItem,
			true );
	}
	else {	JGUIUtil.setEnabled ( __File_Properties_ColoradoSMS_JMenuItem,
			false );
	}
	if ( __DIADvisor_dmi != null ) {
		JGUIUtil.setEnabled ( __File_Properties_DIADvisor_JMenuItem,
			true );
	}
	else {	JGUIUtil.setEnabled ( __File_Properties_DIADvisor_JMenuItem,
			false );
	}
	if ( __hbdmi != null ) {
		JGUIUtil.setEnabled ( __File_Properties_HydroBase_JMenuItem,
			true );
	}
	else {	JGUIUtil.setEnabled ( __File_Properties_HydroBase_JMenuItem,
			false );
	}
	if ( __nwsrfs_dmi != null ) {
		JGUIUtil.setEnabled (__File_Properties_NWSRFSFS5Files_JMenuItem,
			true );
	}
	else {	JGUIUtil.setEnabled (__File_Properties_NWSRFSFS5Files_JMenuItem,
			false );
	}
	if ( __rdmi != null ) {
		JGUIUtil.setEnabled ( __File_Properties_RiversideDB_JMenuItem,
			true );
	}
	else {	JGUIUtil.setEnabled ( __File_Properties_RiversideDB_JMenuItem,
			false );
	}

	// Edit menu...

	if ( command_list_size > 0 ) {
		JGUIUtil.setEnabled ( __Edit_SelectAllCommands_JMenuItem, true);
		JGUIUtil.setEnabled ( __CommandsPopup_SelectAll_JMenuItem,true);
		JGUIUtil.setEnabled (__Edit_DeselectAllCommands_JMenuItem,true);
		JGUIUtil.setEnabled(__CommandsPopup_DeselectAll_JMenuItem,true);
	}
	else {	// No commands are shown.
		JGUIUtil.setEnabled ( __Edit_SelectAllCommands_JMenuItem,false);
		JGUIUtil.setEnabled (__CommandsPopup_SelectAll_JMenuItem,false);
		JGUIUtil.setEnabled(__Edit_DeselectAllCommands_JMenuItem,false);
		JGUIUtil.setEnabled(__CommandsPopup_DeselectAll_JMenuItem,
			false);
	}
	if ( selected_commands_size > 0 ) {
		JGUIUtil.setEnabled ( __Edit_CutCommands_JMenuItem,true);
		JGUIUtil.setEnabled ( __CommandsPopup_Cut_JMenuItem,true);
		JGUIUtil.setEnabled ( __Edit_CopyCommands_JMenuItem,true);
		JGUIUtil.setEnabled ( __CommandsPopup_Copy_JMenuItem,true);
		JGUIUtil.setEnabled ( __Edit_DeleteCommands_JMenuItem,true);
		JGUIUtil.setEnabled ( __CommandsPopup_Delete_JMenuItem,true);
		JGUIUtil.setEnabled (
			__Edit_CommandNoErrorChecking_JMenuItem, true );
		JGUIUtil.setEnabled(
			__Edit_CommandWithErrorChecking_JMenuItem, true );
		JGUIUtil.setEnabled (
			__CommandsPopup_Edit_CommandNoErrorChecking_JMenuItem,
			true );
		JGUIUtil.setEnabled(
			__CommandsPopup_Edit_CommandWithErrorChecking_JMenuItem,
			true );
		JGUIUtil.setEnabled (
		__Edit_ConvertSelectedCommandsToComments_JMenuItem,true);
		JGUIUtil.setEnabled (
		__CommandsPopup_ConvertSelectedCommandsToComments_JMenuItem,
		true);
		JGUIUtil.setEnabled (
		__Edit_ConvertSelectedCommandsFromComments_JMenuItem,true);
		JGUIUtil.setEnabled (
		__CommandsPopup_ConvertSelectedCommandsFromComments_JMenuItem,
		true);
	}
	else {	JGUIUtil.setEnabled ( __Edit_CutCommands_JMenuItem, false );
		JGUIUtil.setEnabled ( __CommandsPopup_Cut_JMenuItem, false );
		JGUIUtil.setEnabled ( __Edit_CopyCommands_JMenuItem, false );
		JGUIUtil.setEnabled ( __CommandsPopup_Copy_JMenuItem, false );
		JGUIUtil.setEnabled ( __Edit_DeleteCommands_JMenuItem, false );
		JGUIUtil.setEnabled ( __CommandsPopup_Delete_JMenuItem, false );
		JGUIUtil.setEnabled (
			__Edit_CommandNoErrorChecking_JMenuItem, false);
		JGUIUtil.setEnabled (
			__Edit_CommandWithErrorChecking_JMenuItem, false );
		JGUIUtil.setEnabled (
			__CommandsPopup_Edit_CommandNoErrorChecking_JMenuItem,
			false);
		JGUIUtil.setEnabled (
			__CommandsPopup_Edit_CommandWithErrorChecking_JMenuItem,
			false );
		JGUIUtil.setEnabled (
		__Edit_ConvertSelectedCommandsToComments_JMenuItem,false);
		JGUIUtil.setEnabled (
		__CommandsPopup_ConvertSelectedCommandsToComments_JMenuItem,
		false);
		JGUIUtil.setEnabled (
		__Edit_ConvertSelectedCommandsFromComments_JMenuItem,false);
		JGUIUtil.setEnabled (
		__CommandsPopup_ConvertSelectedCommandsFromComments_JMenuItem,
		false);
	}
	if ( __commands_cut_buffer.size() > 0 ) {
		// Paste button should be enabled...
		JGUIUtil.setEnabled ( __Edit_PasteCommands_JMenuItem, true );
		JGUIUtil.setEnabled ( __CommandsPopup_Paste_JMenuItem, true );
	}
	else {	JGUIUtil.setEnabled ( __Edit_PasteCommands_JMenuItem, false );
		JGUIUtil.setEnabled ( __CommandsPopup_Paste_JMenuItem, false );
	}

	// View menu...

	// Commands menu...

	if ( command_list_size > 0 ) {
		JGUIUtil.setEnabled (
			__Commands_Create_createTraces_JMenuItem, true);
		JGUIUtil.setEnabled (
			__Commands_Create_TS_average_JMenuItem, true);
		JGUIUtil.setEnabled (
			__Commands_Create_TS_changeInterval_JMenuItem, true);
		JGUIUtil.setEnabled (__Commands_Create_TS_copy_JMenuItem, true);
		JGUIUtil.setEnabled (
			__Commands_Create_TS_disaggregate_JMenuItem, true);
		JGUIUtil.setEnabled (
		__Commands_Create_TS_newDayTSFromMonthAndDayTS_JMenuItem,
			true );
		JGUIUtil.setEnabled (
			__Commands_Create_TS_newEndOfMonthTSFromDayTS_JMenuItem,
			true);
		JGUIUtil.setEnabled (
			__Commands_Create_TS_newStatisticYearTS_JMenuItem,true);
		JGUIUtil.setEnabled (
			__Commands_Create_TS_normalize_JMenuItem, true);
		JGUIUtil.setEnabled (
			__Commands_Create_TS_relativeDiff_JMenuItem, true);
		JGUIUtil.setEnabled (
			__Commands_Create_TS_weightTraces_JMenuItem, true);

		JGUIUtil.setEnabled (
			__Commands_Fill_fillCarryForward_JMenuItem, true);
		JGUIUtil.setEnabled (
			__Commands_Fill_fillConstant_JMenuItem, true);
		JGUIUtil.setEnabled (
		__Commands_Fill_fillDayTSFrom2MonthTSAnd1DayTS_JMenuItem, true);
		JGUIUtil.setEnabled (
			__Commands_Fill_fillFromTS_JMenuItem, true);
		JGUIUtil.setEnabled (
			__Commands_Fill_fillHistMonthAverage_JMenuItem, true);
		JGUIUtil.setEnabled (
			__Commands_Fill_fillHistYearAverage_JMenuItem, true);
		JGUIUtil.setEnabled (
			__Commands_Fill_fillInterpolate_JMenuItem, true);
		// REVISIT SAM 2005-04-26 This fill method is not enabled.
		//JGUIUtil.setEnabled(__Commands_Fill_fillMOVE1_JMenuItem,true);
		JGUIUtil.setEnabled(__Commands_Fill_fillMOVE2_JMenuItem, true);
		JGUIUtil.setEnabled (
			__Commands_Fill_fillMixedStation_JMenuItem,true );
		JGUIUtil.setEnabled(__Commands_Fill_fillPattern_JMenuItem,true);
		JGUIUtil.setEnabled (
			__Commands_Fill_fillProrate_JMenuItem,true);
		JGUIUtil.setEnabled (
			__Commands_Fill_fillRegression_JMenuItem,true);
		JGUIUtil.setEnabled (
			__Commands_Fill_fillRepeat_JMenuItem,true);
		JGUIUtil.setEnabled (
			__Commands_Fill_fillUsingDiversionComments_JMenuItem,
			true);
		JGUIUtil.setEnabled ( __Commands_FillTimeSeries_JMenu, true );

		JGUIUtil.setEnabled(__Commands_Set_replaceValue_JMenuItem,true);
		JGUIUtil.setEnabled(__Commands_Set_setConstant_JMenuItem, true);
		JGUIUtil.setEnabled (
			__Commands_Set_setConstantBefore_JMenuItem, true);
		JGUIUtil.setEnabled(__Commands_Set_setDataValue_JMenuItem,true);
		JGUIUtil.setEnabled ( __Commands_Set_setFromTS_JMenuItem, true);
		JGUIUtil.setEnabled ( __Commands_Set_setMax_JMenuItem, true);
		JGUIUtil.setEnabled ( __Commands_SetTimeSeries_JMenu, true );

		JGUIUtil.setEnabled (__Commands_Manipulate_add_JMenuItem, true);
		JGUIUtil.setEnabled (
			__Commands_Manipulate_addConstant_JMenuItem, true);
		JGUIUtil.setEnabled (
			__Commands_Manipulate_adjustExtremes_JMenuItem, true);
		JGUIUtil.setEnabled(__Commands_Manipulate_ARMA_JMenuItem, true);
		JGUIUtil.setEnabled(__Commands_Manipulate_blend_JMenuItem,true);
		JGUIUtil.setEnabled (
			__Commands_Manipulate_convertDataUnits_JMenuItem,true);
		JGUIUtil.setEnabled (
			__Commands_Manipulate_cumulate_JMenuItem,true);
		JGUIUtil.setEnabled (
			__Commands_Manipulate_divide_JMenuItem,true);
		JGUIUtil.setEnabled (__Commands_Manipulate_free_JMenuItem,true);
		JGUIUtil.setEnabled (
			__Commands_Manipulate_multiply_JMenuItem,true);
		JGUIUtil.setEnabled (
			__Commands_Manipulate_runningAverage_JMenuItem, true);
		JGUIUtil.setEnabled(__Commands_Manipulate_scale_JMenuItem,true);
		JGUIUtil.setEnabled (
			__Commands_Manipulate_shiftTimeByInterval_JMenuItem,
			true);
		JGUIUtil.setEnabled (
			__Commands_Manipulate_subtract_JMenuItem, true);
		JGUIUtil.setEnabled(__Commands_ManipulateTimeSeries_JMenu,true);

		JGUIUtil.setEnabled (
			__Commands_Analyze_analyzePattern_JMenuItem, true);
		JGUIUtil.setEnabled (
			__Commands_Analyze_compareTimeSeries_JMenuItem, true);
		JGUIUtil.setEnabled ( __Commands_AnalyzeTimeSeries_JMenu, true);

		JGUIUtil.setEnabled ( __Commands_Models_JMenu, true);

		JGUIUtil.setEnabled (
			__Commands_Output_sortTimeSeries_JMenuItem, true);
		JGUIUtil.setEnabled (
			__Commands_Output_writeDateValue_JMenuItem, true);
		JGUIUtil.setEnabled (
			__Commands_Output_writeNwsCard_JMenuItem,true);
		JGUIUtil.setEnabled (
			__Commands_Output_writeNWSRFSESPTraceEnsemble_JMenuItem,
			true);
		JGUIUtil.setEnabled (
			__Commands_Output_writeRiverWare_JMenuItem, true);
		JGUIUtil.setEnabled (
			__Commands_Output_writeStateCU_JMenuItem, true);
		JGUIUtil.setEnabled (
			__Commands_Output_writeStateMod_JMenuItem, true);
		JGUIUtil.setEnabled (
			__Commands_Output_writeSummary_JMenuItem, true);
		JGUIUtil.setEnabled(
			__Commands_Output_deselectTimeSeries_JMenuItem, true);
		JGUIUtil.setEnabled (
			__Commands_Output_selectTimeSeries_JMenuItem, true);

		/* REVISIT - it is irritating to not be able to run commands
		  when external input changes (or during debugging)...
		if (	__commands_dirty ||
			(!__commands_dirty && (ts_list_size == 0)) ) {
			JGUIUtil.setEnabled (
				__run_commands_JButton, true );
		}
		else {	JGUIUtil.setEnabled ( __run_commands_JButton, false );
		}
		*/
		if ( selected_commands_size > 0 ) {
			JGUIUtil.setEnabled ( __Run_SelectedCommands_JButton,
				true );
		}
		else {	JGUIUtil.setEnabled ( __Run_SelectedCommands_JButton,
				false );
		}
		JGUIUtil.setEnabled ( __Run_AllCommands_JButton, true );
		JGUIUtil.setEnabled ( __ClearCommands_JButton, true );
	}
	else {	// No commands are shown.
		JGUIUtil.setEnabled (
			__Commands_Create_createTraces_JMenuItem, false);
		JGUIUtil.setEnabled (
			__Commands_Create_TS_average_JMenuItem,false);
		JGUIUtil.setEnabled (
			__Commands_Create_TS_changeInterval_JMenuItem,false);
		JGUIUtil.setEnabled(__Commands_Create_TS_copy_JMenuItem, false);
		JGUIUtil.setEnabled (
			__Commands_Create_TS_disaggregate_JMenuItem, false);
		JGUIUtil.setEnabled (
		__Commands_Create_TS_newDayTSFromMonthAndDayTS_JMenuItem,false);
		JGUIUtil.setEnabled (
			__Commands_Create_TS_newEndOfMonthTSFromDayTS_JMenuItem,
			false);
		JGUIUtil.setEnabled (
			__Commands_Create_TS_newStatisticYearTS_JMenuItem,
			false );
		JGUIUtil.setEnabled (
			__Commands_Create_TS_normalize_JMenuItem, false);
		JGUIUtil.setEnabled (
			__Commands_Create_TS_relativeDiff_JMenuItem, false);
		JGUIUtil.setEnabled (
			__Commands_Create_TS_weightTraces_JMenuItem, false);

		JGUIUtil.setEnabled (
			__Commands_Fill_fillCarryForward_JMenuItem, false);
		JGUIUtil.setEnabled (
			__Commands_Fill_fillConstant_JMenuItem, false);
		JGUIUtil.setEnabled (
		__Commands_Fill_fillDayTSFrom2MonthTSAnd1DayTS_JMenuItem,
			false);
		JGUIUtil.setEnabled(__Commands_Fill_fillFromTS_JMenuItem,false);
		JGUIUtil.setEnabled (
			__Commands_Fill_fillHistMonthAverage_JMenuItem,false);
		JGUIUtil.setEnabled (
			__Commands_Fill_fillHistYearAverage_JMenuItem, false);
		JGUIUtil.setEnabled (
			__Commands_Fill_fillInterpolate_JMenuItem, false);
		JGUIUtil.setEnabled(__Commands_Fill_fillMixedStation_JMenuItem,
			false);
		JGUIUtil.setEnabled(__Commands_Fill_fillMOVE1_JMenuItem, false);
		JGUIUtil.setEnabled(__Commands_Fill_fillMOVE2_JMenuItem, false);
		JGUIUtil.setEnabled (
			__Commands_Fill_fillPattern_JMenuItem, false);
		JGUIUtil.setEnabled (
			__Commands_Fill_fillProrate_JMenuItem, false);
		JGUIUtil.setEnabled (
			__Commands_Fill_fillRegression_JMenuItem, false);
		JGUIUtil.setEnabled (
			__Commands_Fill_fillRepeat_JMenuItem, false);
		JGUIUtil.setEnabled (
			__Commands_Fill_fillUsingDiversionComments_JMenuItem,
			false);
		JGUIUtil.setEnabled ( __Commands_FillTimeSeries_JMenu, false );

		JGUIUtil.setEnabled (
			__Commands_Set_replaceValue_JMenuItem, false);
		JGUIUtil.setEnabled(__Commands_Set_setConstant_JMenuItem,false);
		JGUIUtil.setEnabled (
			__Commands_Set_setConstantBefore_JMenuItem, false);
		JGUIUtil.setEnabled (
			__Commands_Set_setDataValue_JMenuItem, false);
		JGUIUtil.setEnabled (__Commands_Set_setFromTS_JMenuItem, false);
		JGUIUtil.setEnabled ( __Commands_Set_setMax_JMenuItem, false);
		JGUIUtil.setEnabled ( __Commands_SetTimeSeries_JMenu, false );

		JGUIUtil.setEnabled (__Commands_Manipulate_add_JMenuItem,false);
		JGUIUtil.setEnabled (
			__Commands_Manipulate_addConstant_JMenuItem, false);
		JGUIUtil.setEnabled (
			__Commands_Manipulate_adjustExtremes_JMenuItem,false);
		JGUIUtil.setEnabled(__Commands_Manipulate_ARMA_JMenuItem,false);
		JGUIUtil.setEnabled (
			__Commands_Manipulate_blend_JMenuItem, false);
		JGUIUtil.setEnabled (
			__Commands_Manipulate_convertDataUnits_JMenuItem,false);
		JGUIUtil.setEnabled (
			__Commands_Manipulate_cumulate_JMenuItem,false);
		JGUIUtil.setEnabled (
			__Commands_Manipulate_divide_JMenuItem,false);
		JGUIUtil.setEnabled(__Commands_Manipulate_free_JMenuItem,false);
		JGUIUtil.setEnabled (
			__Commands_Manipulate_multiply_JMenuItem,false);
		JGUIUtil.setEnabled (
			__Commands_Manipulate_runningAverage_JMenuItem, false);
		JGUIUtil.setEnabled (
			__Commands_Manipulate_scale_JMenuItem,false);
		JGUIUtil.setEnabled (
			__Commands_Manipulate_shiftTimeByInterval_JMenuItem,
			false);
		JGUIUtil.setEnabled (
			__Commands_Manipulate_subtract_JMenuItem, false);
		JGUIUtil.setEnabled(
			__Commands_ManipulateTimeSeries_JMenu,false);

		JGUIUtil.setEnabled (
			__Commands_Analyze_analyzePattern_JMenuItem, false);
		JGUIUtil.setEnabled (
			__Commands_Analyze_compareTimeSeries_JMenuItem, false);
		JGUIUtil.setEnabled (__Commands_AnalyzeTimeSeries_JMenu, false);

		// REVISIT SAM 2005-07-11 For now enable because models can
		// create time series...
		//JGUIUtil.setEnabled ( __Commands_Models_JMenu, false );

		JGUIUtil.setEnabled(
			__Commands_Output_sortTimeSeries_JMenuItem, false);
		JGUIUtil.setEnabled(
			__Commands_Output_writeDateValue_JMenuItem, false);
		JGUIUtil.setEnabled (
			__Commands_Output_writeNwsCard_JMenuItem,false);
		JGUIUtil.setEnabled(
			__Commands_Output_writeNWSRFSESPTraceEnsemble_JMenuItem,
			false);
		JGUIUtil.setEnabled (
			__Commands_Output_writeRiverWare_JMenuItem, false );
		JGUIUtil.setEnabled (
			__Commands_Output_writeStateCU_JMenuItem,false);
		JGUIUtil.setEnabled (
			__Commands_Output_writeStateMod_JMenuItem,false);
		JGUIUtil.setEnabled (
			__Commands_Output_writeSummary_JMenuItem,false);
		JGUIUtil.setEnabled (
			__Commands_Output_deselectTimeSeries_JMenuItem,false);
		JGUIUtil.setEnabled (
			__Commands_Output_selectTimeSeries_JMenuItem,false);

		JGUIUtil.setEnabled ( __Run_SelectedCommands_JButton, false );
		JGUIUtil.setEnabled ( __Run_AllCommands_JButton, false );
		JGUIUtil.setEnabled ( __ClearCommands_JButton, false );
	}
	if ( selected_commands_size > 0 ) {
		JGUIUtil.setEnabled (
			__Commands_ConvertTSIDTo_readTimeSeries_JMenuItem,true);
		JGUIUtil.setEnabled (
			__Commands_ConvertTSIDToReadCommand_JMenu,true);
	}
	else {	JGUIUtil.setEnabled (
			__Commands_ConvertTSIDTo_readDateValue_JMenuItem,false);
		JGUIUtil.setEnabled (
			__Commands_ConvertTSIDToReadCommand_JMenu,false);
	}

	// Run menu...

	if ( command_list_size > 0 ) {
		/* Revisit
		if (	__commands_dirty ||
			(!__commands_dirty && (ts_list_size == 0)) ) {
			JGUIUtil.setEnabled (
				__run_commands_JButton, true );
		}
		else {	JGUIUtil.setEnabled ( __run_commands_JButton, false );
		}
		*/
		JGUIUtil.setEnabled(
			__Run_AllCommandsCreateOutput_JMenuItem, true);
		JGUIUtil.setEnabled(
			__CommandsPopup_Run_AllCommandsCreateOutput_JMenuItem,
			true);
		JGUIUtil.setEnabled(
			__Run_AllCommandsIgnoreOutput_JMenuItem, true);
		JGUIUtil.setEnabled(
			__CommandsPopup_Run_AllCommandsIgnoreOutput_JMenuItem,
			true);
		if ( selected_commands_size > 0 ) {
			JGUIUtil.setEnabled(
			__Run_SelectedCommandsCreateOutput_JMenuItem, true);
			JGUIUtil.setEnabled(
			__CommandsPopup_Run_SelectedCommandsCreateOutput_JMenuItem,
			true);
			JGUIUtil.setEnabled(
			__Run_SelectedCommandsIgnoreOutput_JMenuItem, true);
			JGUIUtil.setEnabled(
			__CommandsPopup_Run_SelectedCommandsIgnoreOutput_JMenuItem,
			true);
			JGUIUtil.setEnabled (
				__Run_SelectedCommands_JButton, true );
		}
		else {	JGUIUtil.setEnabled(
			__Run_SelectedCommandsCreateOutput_JMenuItem, false);
			JGUIUtil.setEnabled(
			__CommandsPopup_Run_SelectedCommandsCreateOutput_JMenuItem,
			false);
			JGUIUtil.setEnabled(
			__Run_SelectedCommandsIgnoreOutput_JMenuItem, false);
			JGUIUtil.setEnabled(
			__CommandsPopup_Run_SelectedCommandsIgnoreOutput_JMenuItem,
			false);
			JGUIUtil.setEnabled (
				__Run_SelectedCommands_JButton, false );
		}
		JGUIUtil.setEnabled ( __Run_AllCommands_JButton, true );
		JGUIUtil.setEnabled ( __ClearCommands_JButton, true );
	}
	else {	// No commands in list...
		JGUIUtil.setEnabled (
			__Run_AllCommandsCreateOutput_JMenuItem,false);
		JGUIUtil.setEnabled (
			__CommandsPopup_Run_AllCommandsCreateOutput_JMenuItem,
			false);
		JGUIUtil.setEnabled (
			__Run_AllCommandsIgnoreOutput_JMenuItem,false);
		JGUIUtil.setEnabled (
			__CommandsPopup_Run_AllCommandsIgnoreOutput_JMenuItem,
			false);
		JGUIUtil.setEnabled (
			__Run_SelectedCommandsCreateOutput_JMenuItem,false);
		JGUIUtil.setEnabled (
		__CommandsPopup_Run_SelectedCommandsCreateOutput_JMenuItem,
			false);
		JGUIUtil.setEnabled (
			__Run_SelectedCommandsIgnoreOutput_JMenuItem,false);
		JGUIUtil.setEnabled (
		__CommandsPopup_Run_SelectedCommandsIgnoreOutput_JMenuItem,
			false);
		JGUIUtil.setEnabled ( __Run_SelectedCommands_JButton, false );
		JGUIUtil.setEnabled ( __Run_AllCommands_JButton, false );
		JGUIUtil.setEnabled ( __ClearCommands_JButton, false );
	}

	// Results menu...

	if ( ts_list_size > 0 ) {
		JGUIUtil.setEnabled ( __Results_JMenu, true );
	}
	else {	// DISABLE file and view options...
		JGUIUtil.setEnabled ( __Results_JMenu, false );
	}

	// Tools menu...

	if ( ts_list_size > 0 ) {
		JGUIUtil.setEnabled ( __Tools_Analysis_JMenu, true );
		JGUIUtil.setEnabled ( __Tools_Report_JMenu, true );
	}
	else {	JGUIUtil.setEnabled ( __Tools_Analysis_JMenu, false );
		JGUIUtil.setEnabled ( __Tools_Report_JMenu, false );
	}
	if ( (query_list_size > 0) && (__geoview_JFrame != null) ) {
		JGUIUtil.setEnabled ( __Tools_SelectOnMap_JMenuItem, true );
	}
	else {	JGUIUtil.setEnabled ( __Tools_SelectOnMap_JMenuItem, false );
	}

	// Enable/disable features related to the query list...

	if ( query_list_size > 0 ) {
		JGUIUtil.setEnabled ( __CopyAllToCommands_JButton, true );
	}
	else {	JGUIUtil.setEnabled ( __CopyAllToCommands_JButton, false );
	}
	if (	(query_list_size > 0) && (__query_JWorksheet != null) &&
		(__query_JWorksheet.getSelectedRowCount() > 0) ) {
		JGUIUtil.setEnabled ( __CopySelectedToCommands_JButton, true );
	}
	else {	JGUIUtil.setEnabled ( __CopySelectedToCommands_JButton, false );
	}

	// There should be no reason that the help system does not start up
	// other than some synchronization issue.  For now put this in to try
	// to catch problems...
/* REVISIT
	if ( _help_index_gui == null ) {
		try {	_help_index_gui = new URLHelpGUI ( 0, "TSTool Help");
			_help_index_gui.attachMainMenu ( _help_JMenu );
		}
		catch ( Exception e ) {
		}
	}
*/
	// Disable all of the following menus until the dialogs can be enabled.
	// If not enabled below, the command may be phased out or merged with
	// another command...

	// REVISIT - need to create a dialog for the following...
	JGUIUtil.setEnabled(__Commands_Create_TS_average_JMenuItem,false);

	// REVISIT - all of these are new features.  The intent is to convert
	// a TSID to a specific read command, as available in other menus,
	// simplifying the conversion from time series browsing, to command
	// language.
	JGUIUtil.setEnabled (
		__Commands_ConvertTSIDTo_readDateValue_JMenuItem,false);
	JGUIUtil.setEnabled (
		__Commands_ConvertTSIDTo_readHydroBase_JMenuItem,false);
	JGUIUtil.setEnabled (
		__Commands_ConvertTSIDTo_readMODSIM_JMenuItem,false);
	JGUIUtil.setEnabled (
		__Commands_ConvertTSIDTo_readNwsCard_JMenuItem,false);
	JGUIUtil.setEnabled (
		__Commands_ConvertTSIDTo_readNWSRFSFS5Files_JMenuItem,false);
	JGUIUtil.setEnabled (
		__Commands_ConvertTSIDTo_readRiverWare_JMenuItem,false);
	JGUIUtil.setEnabled (
		__Commands_ConvertTSIDTo_readStateMod_JMenuItem,false);
	JGUIUtil.setEnabled (
		__Commands_ConvertTSIDTo_readStateModB_JMenuItem,false);
	JGUIUtil.setEnabled (
		__Commands_ConvertTSIDTo_readUsgsNwis_JMenuItem,false);

	// REVISIT SAM 2005-09-02
	// Proposed new commands
	JGUIUtil.setEnabled(__Commands_Read_TS_readStateModB_JMenuItem,false);
	JGUIUtil.setEnabled(__Commands_Read_TS_readStateMod_JMenuItem,false);

	// REVISIT
	// Not available as a command yet - logic not coded...
	JGUIUtil.setEnabled(__Commands_Fill_fillMOVE1_JMenuItem,false);

	// REVISIT - make sure this is doing what it should
	JGUIUtil.setEnabled(
		__Commands_Fill_setMissingDataValue_JMenuItem,false);

	// REVISIT - should not need this since fillRegression() does this
	JGUIUtil.setEnabled(__Commands_Fill_setRegressionPeriod_JMenuItem,
		false);

	// REVISIT - can this be phased out?
	JGUIUtil.setEnabled(
		__Commands_Output_setOutputDetailedHeaders_JMenuItem,false);
}

/**
Enable/disable the HydroBase input type features depending on whether a
HydroBaseDMI connection has been made.
*/
private void checkHydroBaseFeatures ()
{	if ( (__hbdmi != null) && __hbdmi.isOpen() ) {
		if ( __input_type_JComboBox != null ) {
			// Make sure HydroBase is in the input type list...
			int count = __input_type_JComboBox.getItemCount();
			boolean hbfound = false;
			for ( int i = 0; i < count; i++ ) {
				if ( __input_type_JComboBox.getItem(i).equals(
					__INPUT_TYPE_HydroBase) ) {
					hbfound = true;
					break;
				}
			}
			if ( !hbfound ) {
				// Repopulate the input types...
				setInputTypeChoices();
			}
		}
		JGUIUtil.setEnabled (
			__File_Properties_HydroBase_JMenuItem, true );
	}
	else {	// Remove HydroBase from the input type list if necessary...
		try {	__input_type_JComboBox.remove ( __INPUT_TYPE_HydroBase);
		}
		catch ( Exception e ) {
			// Ignore - probably already removed...
		}
		JGUIUtil.setEnabled (
			__File_Properties_HydroBase_JMenuItem, false );
	}
}

/**
Disable input types depending on the license.  For example, if the license type
is "CDSS", then RTi-developed input types like RiversideDB and DIADvisor are
disabled.  For the most part, the RTi license should be the default and only
CDSS turns input types off in this method.
*/
private void checkInputTypesForLicense ()
{	if ( __license_manager == null ) {
		return;
	}
	String routine = "TSTool_JFrame.checkInputTypesForLicense";
	if ( __license_manager.getLicenseType().equalsIgnoreCase("CDSS") ) {
		if ( !__source_StateCU_enabled ) {
			// Might not be in older config files...
			Message.printStatus ( 1, routine,
			"StateCU input type being enabled for CDSS." );
			__source_StateCU_enabled = true;
		}
		if ( !__source_StateMod_enabled ) {
			// Might not be in older config files...
			Message.printStatus ( 1, routine,
			"StateMod input type being enabled for CDSS." );
			__source_StateMod_enabled = true;
		}
		Message.printStatus ( 2, routine,
		"DIADvisor input type being disabled for CDSS." );
		__source_DIADvisor_enabled = false;
		Message.printStatus ( 2, routine,
		"Mexico CSMN input type being disabled for CDSS." );
		__source_MexicoCSMN_enabled = false;
		Message.printStatus ( 2, routine,
		"MODSIM input type being disabled for CDSS." );
		__source_MODSIM_enabled = false;
		Message.printStatus ( 2, routine,
		"NDFD input type being disabled for CDSS." );
		__source_NDFD_enabled = false;
		Message.printStatus ( 2, routine,
		"NWSCard input type being disabled for CDSS." );
		__source_NWSCard_enabled = false;
		Message.printStatus ( 2, routine,
		"NWSRFS FS5Files input type being disabled for CDSS." );
		__source_NWSRFS_FS5Files_enabled = false;
		Message.printStatus ( 2, routine,
		"NWSRFS ESPTraceEnsemble input type being disabled for CDSS." );
		__source_NWSRFS_ESPTraceEnsemble_enabled = false;
		Message.printStatus ( 2, routine,
		"RiversideDB input type being disabled for CDSS." );
		__source_RiversideDB_enabled = false;
		Message.printStatus ( 2, routine,
		"SHEF input type being disabled for CDSS." );
		__source_SHEF_enabled = false;
	}
}

/**
Check the license information to make sure that TSTool has a valid license.
If not print a warning and exit.  The data member __license_manager is created
for use elsewhere (e.g., in Help About).
*/
private void checkLicense ()
{	try {	if (	(__license_manager == null) ||
			!__license_manager.isLicenseValid() ) {
			Message.printWarning ( 1, "TSTool.checkLicense",
			"The license is invalid.  TSTool will exit." );
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			setVisible(false);
			dispose();
			Message.closeLogFile();
			System.exit(0);
		}
	}
	catch ( Exception e ) {
		Message.printWarning ( 1, "TSTool.checkLicense",
		"Error checking the license.  TSTool will exit." );
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(false);
		dispose();
		Message.closeLogFile();
		System.exit(0);
	}
	if ( __license_manager.getLicenseType().equalsIgnoreCase("Demo") ) {
		if ( __license_manager.isLicenseExpired() ) {
			Message.printWarning ( 1, "TSTool.checkLicense",
			"The demonstration license has expired." +
			"  TSTool will exit." );
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			setVisible(false);
			dispose();
			Message.closeLogFile();
			System.exit(0);
		}
		else {	Message.printWarning ( 1, "TSTool.checkLicense",
			"This is a demonstration version of TSTool and will " +
			"expire on " + __license_manager.getLicenseExpires() );
		}
	}
}

/**
Check the GUI for NWSRFS FS5Files features.
*/
private void checkNWSRFSFS5FilesFeatures ()
{	if ( (__nwsrfs_dmi != null) && (__input_type_JComboBox != null) ) {
		// Make sure NWSRFS FS5Files is in the data source list...
		int count = __input_type_JComboBox.getItemCount();
		boolean rfound = false;
		for ( int i = 0; i < count; i++ ) {
			if ( __input_type_JComboBox.getItem(i).equals(
				__INPUT_TYPE_NWSRFS_FS5Files) ) {
				rfound = true;
				break;
			}
		}
		if ( !rfound ) {
			// Repopulate the data sources...
			setInputTypeChoices();
		}
		if (__File_Properties_NWSRFSFS5Files_JMenuItem != null ) {
			__File_Properties_NWSRFSFS5Files_JMenuItem.setEnabled(
				true);
		}
	}
	else {	// NWSRFS FS5Files connection failed, but do not remove from
		// input types because a new connection currently can be
		// established from the interactive.
		/*
		try {	__input_type_JComboBox.remove(__INPUT_TYPE_RiversideDB);
		}
		catch ( Exception e ) {
			// Ignore - probably already removed...
		}
		*/
		if (__File_Properties_NWSRFSFS5Files_JMenuItem != null ) {
			__File_Properties_NWSRFSFS5Files_JMenuItem.setEnabled (
				false );
		}
	}
}

/**
Check the GUI for RiversideDB features.
*/
private void checkRiversideDBFeatures ()
{	if ( (__rdmi != null) && (__input_type_JComboBox != null) ) {
		// Make sure RiversideDB is in the data source list...
		int count = __input_type_JComboBox.getItemCount();
		boolean rfound = false;
		for ( int i = 0; i < count; i++ ) {
			if ( __input_type_JComboBox.getItem(i).equals(
				__INPUT_TYPE_RiversideDB) ) {
				rfound = true;
				break;
			}
		}
		if ( !rfound ) {
			// Repopulate the data sources...
			setInputTypeChoices();
		}
		if (__File_Properties_RiversideDB_JMenuItem != null ) {
			__File_Properties_RiversideDB_JMenuItem.setEnabled(
				true);
		}
	}
	else {	// RiversideDB connection failed so remove RiversideDB from the
		// data source list if necessary...
		try {	__input_type_JComboBox.remove(__INPUT_TYPE_RiversideDB);
		}
		catch ( Exception e ) {
			// Ignore - probably already removed...
		}
		if (__File_Properties_RiversideDB_JMenuItem != null ) {
			__File_Properties_RiversideDB_JMenuItem.setEnabled (
				false );
		}
	}
}

/*
Clear selected items from the command list (or all if none are selected).  Save
what was cleared in the __command_cut_buffer Vector so that it can be used
with Paste.
*/
private void clearCommands ()
{	int size = 0;
	int [] selected_indices = __commands_JList.getSelectedIndices();
	if ( selected_indices != null ) {
		size = selected_indices.length;
	}
	if ( (size == __commands_JListModel.size()) || (size == 0) ) {
		int x = new ResponseJDialog ( this, "Delete Commands?",
			"Are you sure you want to delete ALL the commands?",
			ResponseJDialog.YES|ResponseJDialog.NO).response();
		if ( x == ResponseJDialog.NO ) {
			return;
		}
	}
	if ( size == 0 ) {
		// nothing selected
		// Remove all...
		__commands_JListModel.removeAllElements();
	}
	else {	// Need to remove from back of selected_indices so that removing
		// elements will not affect the index of items before that
		// index.  At some point need to add an undo feature.
		JGUIUtil.setWaitCursor ( this, true );
		String item;
		__ignore_ItemEvent = true;
		__ignore_ListSelectionEvent = true;
		for ( int i = (size - 1); i >= 0; i-- ) {
			item = (String)
				__commands_JListModel.get(selected_indices[i]);
			__commands_JListModel.removeElementAt (
				selected_indices[i] );
		}
		__ignore_ItemEvent = false;
		__ignore_ListSelectionEvent = false;
		selected_indices = null;
		item = null;
		JGUIUtil.setWaitCursor ( this, false );
	}
	setCommandsDirty ( true );
	clearTSList();
	updateStatus();
}

/**
Clear the query list (e.g., when a choice changes).
*/
private void clearQueryList ()
{	if ( (__query_JWorksheet == null) || (__query_TableModel == null) ) {
		// Not initialized...
		return;
	}
	if ( __query_TableModel.getRowCount() > 0 ) {
        	try {	__query_JWorksheet.clear();       
		}
		catch ( Exception e ) {
			// Absorb the exception in most cases - print if
			// developing to see if this issue can be resolved.
			if ( Message.isDebugOn && IOUtil.testing()  ) {
				String routine = "TSTool_JFrame.clearQueryList";
				Message.printWarning ( 3, routine,
				"For developers:  caught exception in " +
				"clearQueryList JWorksheet at setup." );
				Message.printWarning ( 3, routine, e );
			}
		}
	}

	// Set the data lists to empty and redraw the label (check for null
	// because it seems to be an issue at startup)...

	updateStatus ();
}

/**
Clear the final time series List.  Updates to the label are also done.
Also set the engine to null.
*/
private void clearTSList()
{	__ts_JListModel.removeAllElements();
	// Also set the engine to null.  Any new displays will require a
	// re-query...
	__final_ts_engine = null;
}

/**
Handle "File...Exit" and X actions.
*/
private void closeClicked ()
{	// If the commands are dirty, see if they want to save them...
	// This code is also in openCommandsFile - might be able to remove
	// copy once all actions are implemented...
	int x = ResponseJDialog.YES;	// Default for batch mode
	if ( !tstool.isServer() && !IOUtil.isBatch() ) {
		if ( __show_main && __commands_dirty ) {
			if ( __commands_file_name == null ) {
				// Have not been saved before...
				x = ResponseJDialog.NO;
				if ( __commands_JListModel.size() > 0 ) {
					x = new ResponseJDialog ( this,
					IOUtil.getProgramName(),
					"Do you want to save the changes you " +
					"made?",
					ResponseJDialog.YES| ResponseJDialog.NO|
					ResponseJDialog.CANCEL).response();
				}
				if ( x == ResponseJDialog.CANCEL ) {
					setDefaultCloseOperation(
						DO_NOTHING_ON_CLOSE);
					return;
				}
				else if ( x == ResponseJDialog.YES ) {
					// Prompt for the name and then save...
					writeCommandsFile (
					__commands_file_name, true);
				}
			}
			else {	// A commands file exists...  Warn the user.
				// They can save to the existing file name or
				// can cancel and File...Save As... to a
				// different name.  Have not been saved
				// before...
				x = ResponseJDialog.NO;
				if ( __commands_JListModel.size() > 0 ) {
					x = new ResponseJDialog ( this,
					IOUtil.getProgramName(), "Do you want "+
					"to save the changes you made to\n\""
					+ __commands_file_name + "\"?",
					ResponseJDialog.YES| ResponseJDialog.NO|
					ResponseJDialog.CANCEL).response();
				}
				if ( x == ResponseJDialog.CANCEL ) {
					setDefaultCloseOperation(
						DO_NOTHING_ON_CLOSE);
					return;
				}
				else if ( x == ResponseJDialog.YES ) {
					writeCommandsFile (
					__commands_file_name, false );
				}
				// Else if No will just exit below...
			}
		}
		// Now make sure the user wants to exit - they might have a lot
		// of data processed...
		if ( __show_main ) {
			x = new ResponseJDialog (this,
			"Exit TSTool", "Are you sure you want to exit TSTool?",
			ResponseJDialog.YES| ResponseJDialog.NO).response();
		}
	}
	if ( x == ResponseJDialog.YES ) {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(false);
		try {	dispose();
		}
		catch ( Exception e ) {
			// Why is this a problem?
		}
		Message.closeLogFile();
		System.exit(0);
	}
	else {	// Cancel...
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
	}
}

/**
Determine whether commands are equal.  To allow for multi-line commands, each
command is stored in a Vector (but typically only the first String is used.
*/
private boolean commandsAreEqual(Vector original_command, Vector edited_command)
{	if ( (original_command == null) && (edited_command != null) ) {
		return false;
	}
	else if ( (original_command != null) && (edited_command == null) ) {
		return false;
	}
	else if ( (original_command == null) && (edited_command == null) ) {
		// Should never occur???
		return true;
	}
	int original_size = original_command.size();
	int edited_size = edited_command.size();
	if ( original_size != edited_size ) {
		return false;
	}
	String original_String, edited_String;
	for ( int i = 0; i < original_size; i++ ) {
		original_String = (String)original_command.elementAt(i);
		edited_String = (String)edited_command.elementAt(i);
		// Must be an exact match...
		if (	(original_String == null) && (edited_String != null) ) {
			return false;
		}
		else if ((original_String != null) && (edited_String == null)) {
			return false;
		}
		else if ((original_String == null) && (edited_String == null)) {
			continue;
		}
		else if ( !original_String.equals(edited_String) ) {
			return false;
		}
	}
	// Must be the same...
	return true;
}

/**
Convert selected commands to comments.
@param to_comment If true, convert commands to comments, if false, from
comments.
*/
private void convertCommandsToComments ( boolean to_comment )
{	int selected_indexes[] = __commands_JList.getSelectedIndices();
	int selected_size = JGUIUtil.selectedSize ( __commands_JList );
	String s = null;
	for ( int i = 0; i < selected_size; i++ ) {
		s = (String)__commands_JListModel.get(selected_indexes[i]);
		if ( to_comment ) {
			// Replace with a new string that has the
			// comment character...
			__commands_JListModel.setElementAt (
			("# " + s), selected_indexes[i]);
			setCommandsDirty ( true );
		}
		else {	// Remove comment...
			if ( s.startsWith("#") ) {
				__commands_JListModel.setElementAt (
				s.substring(1).trim(), selected_indexes[i]);
			}
			setCommandsDirty ( true );
		}
	}
	updateStatus ();
}

/**
This function fills in appropriate selections for the time step and choices.
This also calls timestepChoiceClicked() to set the data type modifier.
*/
private void dataTypeChoiceClicked()
{	String rtn = "TSTool_JFrame.dataTypeChoiceClicked";
	if (	(__input_type_JComboBox == null) ||
		(__data_type_JComboBox == null) ) {
		if ( Message.isDebugOn ) {
			Message.printDebug ( 1, rtn, "Data type has been " +
			"selected but GUI is not initialized." );
		}
		return;	// Not done initializing.
	}
	__selected_data_type_full = __data_type_JComboBox.getSelected();
	if ( __selected_data_type_full == null ) {
		// Apparently this happens when setData() or similar is called
		// on the JComboBox, and before select() is called.
		if ( Message.isDebugOn ) {
			Message.printDebug ( 1, rtn, "Data type has been " +
			"selected:  null (select is ignored)" );
		}
		return;
	}
	// For some input types, data types have additional label-only
	// information...
	if ( __selected_data_type_full.indexOf('-') >= 0 ) {
		if (	__selected_input_type.equals(__INPUT_TYPE_HydroBase) ||
			__selected_input_type.equals(__INPUT_TYPE_StateModB)) {
			// Data type group is first and data type second...
			__selected_data_type = StringUtil.getToken(
				__selected_data_type_full, "-", 0, 1).trim();
		}
		else {	// Data type is first and explanation second...
			__selected_data_type = StringUtil.getToken(
				__selected_data_type_full, "-", 0, 0).trim();
		}
	}
	else {	__selected_data_type = __selected_data_type_full;
	}
	if ( Message.isDebugOn ) {
		Message.printDebug ( 1, rtn, "Data type has been selected:  \""
		+ __selected_data_type + "\"" );
	}

	// Set the appropriate settings for the current data input type and
	// data type...

	if ( __selected_input_type.equals(__INPUT_TYPE_DateValue) ) {
		// DateValue file...
		__time_step_JComboBox.removeAll ();
		__time_step_JComboBox.add ( __TIMESTEP_AUTO );
		__time_step_JComboBox.select ( null );
		__time_step_JComboBox.select ( __TIMESTEP_AUTO );
		__time_step_JComboBox.setEnabled ( false );
	}
	else if ( __selected_input_type.equals(__INPUT_TYPE_DIADvisor) ) {
		// The time steps are always available in both the operational
		// and archive database.  However, "DataValue2" is only
		// available in the operational database.  Also, regular
		// interval data seem to only be available for Rain group.
		String group =StringUtil.getToken(__selected_data_type,"-",0,0);
		__time_step_JComboBox.removeAll ();
		__time_step_JComboBox.setEnabled ( true );
		if (	__selected_data_type.endsWith("DataValue") &&
			group.equalsIgnoreCase("Rain") ) {
			__time_step_JComboBox.add ( __TIMESTEP_DAY );
			__time_step_JComboBox.add ( __TIMESTEP_HOUR );
		}
		__time_step_JComboBox.add ( __TIMESTEP_IRREGULAR );
		if (	__selected_data_type.endsWith("DataValue") &&
			group.equalsIgnoreCase("Rain") ) {
			try {	DIADvisor_SysConfig config =
				__DIADvisor_dmi.readSysConfig();
				__time_step_JComboBox.add ( "" +
					config.getInterval() +
					__TIMESTEP_MINUTE );
			}
			catch ( Exception e ) {
				Message.printWarning ( 2, rtn,
				"Could not determine"
				+ " DIADvisor interval for time step choice." );
				Message.printWarning ( 2, rtn, e );
			}
		}
		__time_step_JComboBox.select ( null );
		__time_step_JComboBox.select ( 0 );
	}
	else if ( __selected_input_type.equals(__INPUT_TYPE_HydroBase) ) {
		Vector time_steps =
			HydroBase_Util.getTimeSeriesTimeSteps (__hbdmi,
			__selected_data_type,
			HydroBase_Util.DATA_TYPE_AGRICULTURE |
			HydroBase_Util.DATA_TYPE_DEMOGRAPHICS_ALL |
			HydroBase_Util.DATA_TYPE_HARDWARE |
			HydroBase_Util.DATA_TYPE_STATION_ALL |
			HydroBase_Util.DATA_TYPE_STRUCTURE_ALL |
			HydroBase_Util.DATA_TYPE_WIS );
		__time_step_JComboBox.setData ( time_steps );
		__time_step_JComboBox.select ( null );
		__time_step_JComboBox.setEnabled ( true );
		// Select monthly as the default if available...
		if ( JGUIUtil.isSimpleJComboBoxItem(__time_step_JComboBox,
			"Month", JGUIUtil.NONE, null, null ) ) {
			__time_step_JComboBox.select ( "Month" );
		}
		else {	// Select the first item...
			try {	__time_step_JComboBox.select ( 0 );
			}
			catch ( Exception e ) {
				// For cases when for some reason no choice
				// is available.
				__time_step_JComboBox.setEnabled ( false );
			}
		}
		// If the data type is for a diversion or reservoir data type,
		// hide the abbreviation column in the table model.  Else show
		// the column.
	}
	else if ( __selected_input_type.equals(__INPUT_TYPE_MEXICO_CSMN) ) {
		// Mexico CSMN file...
		__time_step_JComboBox.removeAll ();
		__time_step_JComboBox.add ( __TIMESTEP_DAY );
		__time_step_JComboBox.select ( null );
		__time_step_JComboBox.select ( __TIMESTEP_DAY );
		__time_step_JComboBox.setEnabled ( true );
	}
	else if ( __selected_input_type.equals(__INPUT_TYPE_MODSIM) ) {
		// MODSIM file...
		__time_step_JComboBox.removeAll ();
		__time_step_JComboBox.add ( __TIMESTEP_AUTO );
		__time_step_JComboBox.select ( null );
		__time_step_JComboBox.select ( __TIMESTEP_AUTO );
		__time_step_JComboBox.setEnabled ( false );
	}
	else if ( __selected_input_type.equals(__INPUT_TYPE_NWSCARD) ) {
		// NWSCard file...
		__time_step_JComboBox.removeAll ();
		__time_step_JComboBox.add ( __TIMESTEP_HOUR );
		__time_step_JComboBox.select ( null );
		__time_step_JComboBox.select ( __TIMESTEP_HOUR );
		__time_step_JComboBox.setEnabled ( false );
	}
	else if ( __selected_input_type.equals(__INPUT_TYPE_NWSRFS_FS5Files) ) {
		// Time steps are determined from the system...
		Vector time_steps = NWSRFS_Util.getDataTypeIntervals (
			__nwsrfs_dmi, __selected_data_type );
		__time_step_JComboBox.setData ( time_steps );
		__time_step_JComboBox.select ( null );
		__time_step_JComboBox.setEnabled ( true );
		// Select 6Hour as the default if available...
		if ( JGUIUtil.isSimpleJComboBoxItem(__time_step_JComboBox,
			"6Hour", JGUIUtil.NONE, null, null ) ) {
			__time_step_JComboBox.select ( "6Hour" );
		}
		else {	// Select the first item...
			try {	__time_step_JComboBox.select ( 0 );
			}
			catch ( Exception e ) {
				// For cases when for some reason no choice
				// is available.
				__time_step_JComboBox.setEnabled ( false );
			}
		}
	}
	else if ( __selected_input_type.equals(
		__INPUT_TYPE_NWSRFS_ESPTraceEnsemble) ){
		// ESP Trace Ensemble file...
		__time_step_JComboBox.removeAll ();
		__time_step_JComboBox.add ( __TIMESTEP_AUTO );
		__time_step_JComboBox.select ( null );
		__time_step_JComboBox.select ( __TIMESTEP_AUTO );
		__time_step_JComboBox.setEnabled ( false );
	}
	else if ( __selected_input_type.equals(__INPUT_TYPE_RiversideDB) ) {
		// Time steps are determined from the database based on the
		// data type that is selected...
		String data_type = StringUtil.getToken(
			__data_type_JComboBox.getSelected()," ",0,0).trim();
		Vector v = null;
		try {	v = __rdmi.readMeasTypeListForTSIdent (
					".." + data_type + ".." );
		}
		catch ( Exception e ) {
			Message.printWarning(2, rtn, "Error getting time "
				+ "steps from RiversideDB.");
			Message.printWarning(2, rtn, e);
			v = null;
		}
		int size = 0;
		if ( v != null ) {
			size = v.size();
		}
		RiversideDB_MeasType mt = null;
		String timestep;
		String time_step_base;
		long time_step_mult;
		__time_step_JComboBox.removeAll ();
		if ( size > 0 ) {
			for ( int i = 0; i < size; i++ ) {
				mt = (RiversideDB_MeasType)v.elementAt(i);
				// Only add if not already listed.
				// Alternatively - add a "distinct" query
				time_step_base = mt.getTime_step_base();
				time_step_mult = mt.getTime_step_mult();
				if (	time_step_base.equalsIgnoreCase(
					"IRREGULAR") ||
					DMIUtil.isMissing(time_step_mult) ) {
					timestep = mt.getTime_step_base();
				}
				else {	timestep = "" + mt.getTime_step_mult() +
						mt.getTime_step_base();
				}
				if (	!JGUIUtil.isSimpleJComboBoxItem(
					__time_step_JComboBox,
					timestep, JGUIUtil.NONE, null, null)){
					__time_step_JComboBox.add(timestep);
				}
			}
			__time_step_JComboBox.select ( null );
			__time_step_JComboBox.select ( 0 );
			__time_step_JComboBox.setEnabled ( true );
		}
		else {	__time_step_JComboBox.setEnabled ( false );
		}
	}
	else if ( __selected_input_type.equals(__INPUT_TYPE_RiverWare) ) {
		// RiverWare file...
		__time_step_JComboBox.removeAll ();
		__time_step_JComboBox.add ( __TIMESTEP_AUTO );
		__time_step_JComboBox.select ( null );
		__time_step_JComboBox.select ( __TIMESTEP_AUTO );
		__time_step_JComboBox.setEnabled ( false );
	}
	else if ( __selected_input_type.equals(__INPUT_TYPE_StateCU) ) {
		// StateCU files...
		__time_step_JComboBox.removeAll ();
		__time_step_JComboBox.add ( __TIMESTEP_AUTO );
		__time_step_JComboBox.setEnabled ( false );
	}
	else if ( __selected_input_type.equals(__INPUT_TYPE_StateMod) ) {
		__time_step_JComboBox.removeAll ();
		__time_step_JComboBox.add ( __TIMESTEP_DAY );
		__time_step_JComboBox.add ( __TIMESTEP_MONTH );
		__time_step_JComboBox.select ( null );
		__time_step_JComboBox.select ( __TIMESTEP_MONTH );
		__time_step_JComboBox.setEnabled ( true );
	}
	else if ( __selected_input_type.equals(__INPUT_TYPE_StateModB) ) {
		// StateMod binary output file - the time step is set when the
		// input name is selected so do nothing here.
	}
	else if ( __selected_input_type.equals(__INPUT_TYPE_USGSNWIS) ) {
		// USGS NWIS file...
		__time_step_JComboBox.removeAll ();
		__time_step_JComboBox.add ( __TIMESTEP_AUTO );
		__time_step_JComboBox.select ( null );
		__time_step_JComboBox.select ( __TIMESTEP_AUTO );
		__time_step_JComboBox.setEnabled ( false );
	}

	// Set the filter where clauses based on the data type changing...

	setInputFilters();
}

/**
Delete all commands in the list.
@param index Index in the list at which item should be removed.
*/
private void deleteCommands ()
{	// Do this so that the status only needs to be updated once...
	__commands_JListModel.removeAllElements();
	setCommandsDirty ( true );
	updateStatus ();
}

/**
Deselect all commands in the commands list.
*/
private void deselectAllCommands()
{	__commands_JList.clearSelection();
	updateStatus ();
	checkGUIState();
}

/**
Edit or update a time series command, check for errors in the appropriate
dialogs.
@param action Menu action to carry out.  Pass as an empty string if the edit
is not initiated from a conversion or insert.
@param command_Vector Command to update, when mode is __UPDATE_COMMAND.
@param mode __UPDATE_COMMAND if editing or __INSERT_COMMAND if adding a new
command.
*/
private void editCommand ( String action, Vector command_Vector, int mode )
{	editCommand ( action, command_Vector, mode, true );
}

/**
Edit a command in the command list.
@param action the string containing the event's action value.
@param command_Vector if an update command, the current values in the 
command in the list.  If a new command, null.
@param mode the action to take when editing the command (__INSERT_COMMAND for a
new command or __UPDATE_COMMAND for an existing command).
@param check_errors whether the command will be checked for errors (i.e.,
opened in its own dialog) or not.
*/
private void editCommand (	String action, Vector command_Vector,
				int mode, boolean check_errors )
{	String routine = "TSTool_JFrame.editCommand";
	String command = null;	// Single string command to edit
	Vector cv = null;	// Local reference to command_Vector
	int dl = 1;		// Debug level

	Vector original_cv = null;

	try {	// Main try to help with troubleshooting, especially during
		// transition to new command structure.

	// Set the Vectors used in dialogs (this setup will not be used when
	// running in batch mode)...

	checkDialogInput ();

	if ( mode == __UPDATE_COMMAND ) {
		// Updating the command so pass in the original command...
		if ( (command_Vector != null) && (command_Vector.size() == 0)) {
			// Should never happen???
			return;
		}
		cv = command_Vector;
		if ( Message.isDebugOn ) {
			Message.printDebug ( 1, routine,
			"Command vector is: " + cv );
		}
		command = (String)cv.elementAt(0);
		// Save a copy of the original command vector so that it can be
		// compared at the end of the edit.  If a change occurs, the
		// commands list will be be marked as dirty...
		int size = cv.size();
		original_cv = new Vector(size);
		for ( int i = 0; i < size; i++ ) {
			// Force new strings to be created so the comparison is
			// made on the string content, not the reference to the
			// Vector that is used below...
			original_cv.addElement ( new String(
				(String)cv.elementAt(i) ) );
		}
	}
	else {	// New command so pass in a blank Vector...
		cv = new Vector ( 1 );
		original_cv = null;	// No original because it's new
		command = "";
		cv.addElement ( command );
	}
	String command_trimmed = command.trim();	// Use for some checks.
	if ( command.length() > 0 ) {
		if ( check_errors ) {
			Message.printStatus ( 1, routine, "Editing \"" +
			command + "\"..." );
		}
		else {	Message.printStatus ( 1, routine, "Editing \"" +
			command + "\" (no error checks)..." );
		}
	}
	if ( Message.isDebugOn ) {
		Message.printDebug ( 1, routine, "Action item is \"" + action +
		"\"" );
	}

	Vector edited_cv = null;
	if ( !check_errors ) {
		// Generic command editor...
		edited_cv = new commandString_JDialog ( this, cv,
			TSEngine.getTSIdentifiersFromCommands(
			getCommandsAboveSelected ())).getText();
	}

	// These are listed in the same order as the GUI...

	// Create time series...

	else if ( action.equals( __Commands_Create_createFromList_String)||
		(StringUtil.indexOfIgnoreCase(command,"createFromList(",0)>=0)||
		(StringUtil.indexOfIgnoreCase(
		command,"createFromList (",0) >= 0) ) {
		if ( Message.isDebugOn ) {
			Message.printDebug ( dl, routine,
			"Opening dialog for createFromList()" );
		}
		// Update the shared application properties to set the
		// "WorkingDir" property, which is used in the following
		// dialog...
		updateDynamicProps ();
		edited_cv = new createFromList_JDialog ( this, __props,
			cv, TSEngine.getTSIdentifiersFromCommands(
			getCommandsAboveSelected ())).getText();
	}
	else if ( action.equals( __Commands_Create_createTraces_String)||
		(StringUtil.indexOfIgnoreCase(command,"createTraces(",0) >= 0)||
		(StringUtil.indexOfIgnoreCase(command,"createTraces (",0) >=0)){
		if ( Message.isDebugOn ) {
			Message.printDebug ( dl, routine,
			"Opening dialog for createTraces()" );
		}
		edited_cv = new createTraces_JDialog ( this, cv,
			TSEngine.getTSIdentifiersFromCommands(
			getCommandsAboveSelected ())).getText();
	}
	// REVISIT SAM 2005-05-18 need to enable - the command string checked
	// below is in conflice with the fillHistMonthAverage() command so that
	// needs to be resolved.
	/*
	else if ( action.equals( __Commands_Create_TS_average_String)||
		(((StringUtil.indexOfIgnoreCase( command,"average(",0) >= 0) ||
		(StringUtil.indexOfIgnoreCase( command,"average (",0) >= 0)) &&
		(command.indexOf("=") >= 0)) ) {
		if ( Message.isDebugOn ) {
			Message.printDebug ( dl, routine,
			"Opening dialog for TS Alias = average()" );
		}
		// Need to check the above combinations to avoid conflict with
		// historical average commands.
		edited_cv = new average_JDialog ( this, cv,
			TSEngine.getTSIdentifiersFromCommands(
			getCommandsAboveSelected ())).getText();
	}
	*/
	else if ( action.equals( __Commands_Create_TS_disaggregate_String)||
		(StringUtil.indexOfIgnoreCase(command,"disaggregate(",0) >= 0)||
		(StringUtil.indexOfIgnoreCase(command,"disaggregate (",0) >=0)){
		if ( Message.isDebugOn ) {
			Message.printDebug ( dl, routine,
			"Opening dialog for TS Alias = disaggregate()" );
		}
		edited_cv = new disaggregate_JDialog ( this, cv,
			TSEngine.getTSIdentifiersFromCommands(
			getCommandsAboveSelected ())).getText();
	}
	else if ( action.equals(
		__Commands_Create_TS_newDayTSFromMonthAndDayTS_String)||
		(StringUtil.indexOfIgnoreCase(
		command,"newDayTSFromMonthAndDayTS(",0) >= 0) ||
		(StringUtil.indexOfIgnoreCase(
		command,"newDayTSFromMonthAndDayTS (",0) >= 0) ) {
		if ( Message.isDebugOn ) {
			Message.printDebug ( dl, routine,
			"Opening dialog for TS Alias =" +
			"newDayTSFromMonthAndDayTS()");
		}
		edited_cv = new newDayTSFromMonthAndDayTS_JDialog (this,cv,
			TSEngine.getTSIdentifiersFromCommands(
			getCommandsAboveSelected ())).getText();
	}
	else if ( action.equals(
		__Commands_Create_TS_newEndOfMonthTSFromDayTS_String)||
		(StringUtil.indexOfIgnoreCase(
		command,"newEndOfMonthTSFromDayTS",0) >= 0) ) {
		if ( Message.isDebugOn ) {
			Message.printDebug ( dl, routine,
			"Opening dialog for TS Alias =" +
			"newEndOfMonthTSFromDayTS()" );
		}
		edited_cv = new newEndOfMonthTSFromDayTS_JDialog (this,cv,
			TSEngine.getTSIdentifiersFromCommands(
			getCommandsAboveSelected ())).getText();
	}
	else if ( action.equals( __Commands_Create_TS_normalize_String)||
		(StringUtil.indexOfIgnoreCase(command,"normalize(",0) >= 0)||
		(StringUtil.indexOfIgnoreCase(command,"normalize (",0)>= 0)){
		if ( Message.isDebugOn ) {
			Message.printDebug ( dl, routine,
			"Opening dialog for TS Alias = normalize()" );
		}
		edited_cv = new normalize_JDialog ( this, cv,
			TSEngine.getTSIdentifiersFromCommands(
			getCommandsAboveSelected ())).getText();
	}
	else if ( action.equals( __Commands_Create_TS_relativeDiff_String)||
		(StringUtil.indexOfIgnoreCase(
		command,"relativeDiff(",0)>=0)||
		(StringUtil.indexOfIgnoreCase(
		command,"relativeDiff (",0)>=0) ) {
		if ( Message.isDebugOn ) {
			Message.printDebug ( dl, routine,
			"Opening dialog for TS Alias = relativeDiff()" );
		}
		edited_cv = new relativeDiff_JDialog ( this, cv,
			TSEngine.getTSIdentifiersFromCommands(
			getCommandsAboveSelected ())).getText();
	}
	else if ( action.equals( __Commands_Create_TS_weightTraces_String)||
		(StringUtil.indexOfIgnoreCase(
		command,"weightTraces(",0) >= 0) ||
		(StringUtil.indexOfIgnoreCase(
		command,"weightTraces (",0) >= 0) ) {
		if ( Message.isDebugOn ) {
			Message.printDebug ( dl, routine,
			"Opening dialog for TS Alias = weightTraces()" );
		}
		edited_cv = new weightTraces_JDialog ( this, cv,
			TSEngine.getTraceIdentifiersFromCommands(
			getCommandsAboveSelected ())).getText();
	}

	// Convert time series...

	else if (action.equals(__Commands_ConvertTSIDTo_readTimeSeries_String)||
		StringUtil.indexOfIgnoreCase(command,"readTimeSeries",0)>5 ) {
		// TS Alias = ...
		updateDynamicProps ();
		if ( Message.isDebugOn ) {
			Message.printDebug ( dl, routine,
			"Opening dialog for TS Alias = readTimeSeries()" );
		}
		edited_cv = new readTimeSeries_JDialog ( this, __props,
			cv, TSEngine.getTSIdentifiersFromCommands(
			getCommandsAboveSelected ())).getText();
	}

	// Read Time Series...

	else if ( action.equals( __Commands_Read_readDateValue_String)||
		(!command_trimmed.regionMatches(true,0,"TS",0,2) &&
		(StringUtil.indexOfIgnoreCase(command,"readDateValue",0)>= 0))){
		if ( Message.isDebugOn ) {
			Message.printDebug ( dl, routine,
			"Opening dialog for readDateValue()" );
		}
		updateDynamicProps ();
		edited_cv = new readDateValue_JDialog ( this, __props,
			cv, null ).getText();
	}
	else if ( action.equals(
		__Commands_Read_readNWSRFSESPTraceEnsemble_String)||
		(!command_trimmed.regionMatches(true,0,"TS",0,2) &&
		(StringUtil.indexOfIgnoreCase(command,
			"readNWSRFSESPTraceEnsemble",0)>= 0))){
		if ( Message.isDebugOn ) {
			Message.printDebug ( dl, routine,
			"Opening dialog for readNWSRFSESPTraceEnsemble()" );
		}
		updateDynamicProps ();
		edited_cv = new readNWSRFSESPTraceEnsemble_JDialog (
			this, __props,
			cv, null ).getText();
	}
	else if ( action.equals( __Commands_Read_readMODSIM_String)||
		(!command_trimmed.regionMatches(true,0,"TS",0,2) &&
		(StringUtil.indexOfIgnoreCase(
		command,"readMODSIM(",0) >= 0) ||
		(StringUtil.indexOfIgnoreCase(
		command,"readMODSIM (",0) >= 0)) ) {
		if ( Message.isDebugOn ) {
			Message.printDebug ( dl, routine,
			"Opening dialog for readMODSIM()" );
		}
		updateDynamicProps ();
		edited_cv = new readMODSIM_JDialog ( this, __props,
			cv, null ).getText();
	}
	// Put this in front of the shorter version...
	else if ( action.equals( __Commands_Read_TS_readNWSRFSFS5Files_String)||
		(command_trimmed.regionMatches(true,0,"TS",0,2) &&
		((StringUtil.indexOfIgnoreCase(
		command,"readNWSRFSFS5Files(",0) >= 0)||
		(StringUtil.indexOfIgnoreCase(
		command,"readNWSRFSFS5Files(",0) >=0)))){
		if ( Message.isDebugOn ) {
			Message.printDebug ( dl, routine,
			"Opening dialog for TS Alias = readNWSRFSFS5Files()" );
		}
		updateDynamicProps ();
		edited_cv = new readNWSRFSFS5Files_JDialog ( this, __props,
			cv, true, __nwsrfs_dmi ).getText();
	}
	else if ( action.equals( __Commands_Read_readNWSRFSFS5Files_String)||
		(StringUtil.indexOfIgnoreCase(
		command,"readNWSRFSFS5Files(",0) >= 0) ||
		(StringUtil.indexOfIgnoreCase(
		command,"readNWSRFSFS5Files (",0) >= 0) ) {
		if ( Message.isDebugOn ) {
			Message.printDebug ( dl, routine,
			"Opening dialog for readNWSRFSFS5Files()" );
		}
		updateDynamicProps ();
		edited_cv = new readNWSRFSFS5Files_JDialog ( this, __props,
			cv, false, __nwsrfs_dmi ).getText();
	}
	// Put this in front of the shorter version...
	else if ( action.equals( __Commands_Read_readStateCU_String)||
		(StringUtil.indexOfIgnoreCase(
		command,"readStateCU(",0) >= 0) ||
		(StringUtil.indexOfIgnoreCase(
		command,"readStateCU (",0) >= 0) ) {
		if ( Message.isDebugOn ) {
			Message.printDebug ( dl, routine,
			"Opening dialog for readStateCU()" );
		}
		updateDynamicProps ();
		edited_cv = new readStateCU_JDialog ( this, __props,
			cv, null ).getText();
	}
	else if ( action.equals( __Commands_Read_statemodMax_String)||
		StringUtil.startsWithIgnoreCase(command,"statemodMax") ) {
		if ( Message.isDebugOn ) {
			Message.printDebug ( dl, routine,
			"Opening dialog for readStateModMax()" );
		}
		updateDynamicProps ();
		edited_cv = new statemodMax_JDialog ( this, __props,
			cv, null ).getText();
	}
	// These commands are "TS Alias" commands...
	else if ( action.equals( __Commands_Read_TS_readDateValue_String)||
		(command_trimmed.regionMatches(true,0,"TS",0,2) &&
		(StringUtil.indexOfIgnoreCase(command,"readDateValue",0) >=0))){
		if ( Message.isDebugOn ) {
			Message.printDebug ( dl, routine,
			"Opening dialog for TS Alias = readDateValue()" );
		}
		updateDynamicProps ();
		edited_cv = new TSreadDateValue_JDialog ( this, __props,
			cv, null ).getText();
	}
	else if ( action.equals( __Commands_Read_TS_readMODSIM_String)||
		(command_trimmed.regionMatches(true,0,"TS",0,2) &&
		((StringUtil.indexOfIgnoreCase(
		command,"readMODSIM(",0) >= 0) ||
		(StringUtil.indexOfIgnoreCase(
		command,"readMODSIM (",0) >= 0)) ) ) {
		if ( Message.isDebugOn ) {
			Message.printDebug ( dl, routine,
			"Opening dialog for TS Alias = readMODSIM()" );
		}
		updateDynamicProps ();
		edited_cv = new TSreadMODSIM_JDialog ( this, __props,
			cv, null ).getText();
	}
	else if ( action.equals( __Commands_Read_TS_readRiverWare_String)||
		(command_trimmed.regionMatches(true,0,"TS",0,2) &&
		((StringUtil.indexOfIgnoreCase(
			command,"readRiverWare(",0) >= 0)||
		(StringUtil.indexOfIgnoreCase(
			command,"readRiverWare(",0) >=0)))){
		if ( Message.isDebugOn ) {
			Message.printDebug ( dl, routine,
			"Opening dialog for TS Alias = readRiverWare()" );
		}
		updateDynamicProps ();
		edited_cv = new TSreadRiverWare_JDialog ( this, __props,
			cv, null ).getText();
	}
	else if ( action.equals( __Commands_Read_TS_readUsgsNwis_String)||
		(StringUtil.indexOfIgnoreCase(
		command,"readUsgsNwis(",0) >= 0) ||
		(StringUtil.indexOfIgnoreCase(
		command,"readUsgsNwis (",0) >= 0) ) {
		if ( Message.isDebugOn ) {
			Message.printDebug ( dl, routine,
			"Opening dialog for TS Alias = readUsgsNwis()" );
		}
		updateDynamicProps ();
		edited_cv = new TSreadUsgsNwis_JDialog ( this, __props,
			cv, null ).getText();
	}
	else if ( action.equals(__Commands_Read_setIncludeMissingTS_String)||
		command.regionMatches(true,0,"setIncludeMissingTS",0,19) ) {
		if ( Message.isDebugOn ) {
			Message.printDebug ( dl, routine,
			"Opening dialog for setIncludeMissingTS()" );
		}
		edited_cv = new setIncludeMissingTS_JDialog ( this, cv,
			TSEngine.getTSIdentifiersFromCommands(
			getCommandsAboveSelected ())).getText();
	}

	// Fill Time Series Data...

	else if ( action.equals( __Commands_Fill_fillCarryForward_String)||
		command.regionMatches(true,0,"fillCarryForward",0,16) ) {
		if ( Message.isDebugOn ) {
			Message.printDebug ( dl, routine,
			"Opening dialog for fillCarryForward()" );
		}
		edited_cv = new fillCarryForward_JDialog ( this, cv,
			TSEngine.getTSIdentifiersFromCommands(
			getCommandsAboveSelected ())).getText();
	}
	else if ( action.equals(
		__Commands_Fill_fillDayTSFrom2MonthTSAnd1DayTS_String)||
		command.regionMatches(
			true,0,"fillDayTSFrom2MonthTSAnd1DayTS",0,30) ) {
		if ( Message.isDebugOn ) {
			Message.printDebug ( dl, routine,
			"Opening dialog for fillDayTSFrom2MonthTSAnd1DayTS()" );
		}
		edited_cv = new fillDayTSFrom2MonthTSAnd1DayTS_JDialog ( 
			this, cv, TSEngine.getTSIdentifiersFromCommands(
			getCommandsAboveSelected ())).getText();
	}
	else if ( action.equals( __Commands_Fill_fillFromTS_String)||
		command.regionMatches( true,0,"fillFromTS",0,10) ) {
		if ( Message.isDebugOn ) {
			Message.printDebug ( dl, routine,
			"Opening dialog for fillFromTS()" );
		}
		edited_cv = new fillFromTS_JDialog ( this, cv,
			TSEngine.getTSIdentifiersFromCommands(
			getCommandsAboveSelected ())).getText();
	}
	else if ( action.equals( __Commands_Fill_fillInterpolate_String)||
		command.regionMatches(true,0,"fillInterpolate",0,15) ) {
		if ( Message.isDebugOn ) {
			Message.printDebug ( dl, routine,
			"Opening dialog for fillInterpolate()" );
		}
		edited_cv = new fillInterpolate_JDialog ( this, cv,
			TSEngine.getTSIdentifiersFromCommands(
			getCommandsAboveSelected ())).getText();
	}
	else if ( action.equals(__Commands_Fill_fillPattern_String) ||
		command.regionMatches(true,0,"fillPattern",0,11) ) {
		if ( Message.isDebugOn ) {
			Message.printDebug ( dl, routine,
			"Opening dialog for fillPattern()" );
		}
		updateDynamicProps ();
		// Get the pattern time series from the previous commands...
		Vector needed_commands_Vector = new Vector();
		needed_commands_Vector.addElement ( "setPatternFile" );
		Vector found_commands_Vector =
			getCommandsAboveInsertPosition( needed_commands_Vector,
			true );
		// Transfer the file names to the data vector...
		__fill_pattern_files.removeAllElements();
		int psize = 0;
		if ( found_commands_Vector != null ) {
			psize = found_commands_Vector.size();
		}
		Vector tokens;
		for ( int ip = 0; ip < psize; ip++ ) {
			tokens = StringUtil.breakStringList (
				(String)found_commands_Vector.elementAt(ip),
				"() ", StringUtil.DELIM_SKIP_BLANKS|
				StringUtil.DELIM_ALLOW_STRINGS );
			if ( (tokens != null) && (tokens.size() == 2) ) {
				__fill_pattern_files.addElement (
				((String)tokens.elementAt(1)).trim() );
			}
		}
		// Now get a list of the patterns so that they can be used in
		// the dialog to make selections...
		if ( __fill_pattern_ids.size() == 0 ) {
			// Read the pattern information so it can be passed
			// to the dialog.
			readPatternTS ();
		}
		edited_cv = new fillPattern_JDialog ( this, __props,
			__fill_pattern_files, cv,
			TSEngine.getTSIdentifiersFromCommands(
			getCommandsAboveSelected ()), __fill_pattern_ids
			).getText();
	}
	else if (action.equals( __Commands_Fill_fillProrate_String)||
		command.regionMatches( true,0,"fillProrate",0,11)){
		if ( Message.isDebugOn ) {
			Message.printDebug ( dl, routine,
			"Opening dialog for fillProrate()" );
		}
		edited_cv = new fillProrate_JDialog ( this, cv,
			TSEngine.getTSIdentifiersFromCommands(
			getCommandsAboveSelected ())).getText();
	}
	/* REVISIT SAM 2005-05-09 Use the factory code at the end of the
	if statements...
	else if ( action.equals(__Commands_Fill_fillRegression_String) ||
		command.regionMatches(true,0,"fillRegression",0,14) ||
		command.regionMatches(true,0,"regress(",0,8) ||
		command.regionMatches(true,0,"regress (",0,9) ||
		command.regionMatches(true,0,"regresslog(",0,11) ||
		command.regionMatches(true,0,"regresslog (",0,12) ||
		command.regionMatches(true,0,"regress12(",0,10) ||
		command.regionMatches(true,0,"regress12 (",0,11) ||
		command.regionMatches(true,0,"regressMonthly(",0,15) ||
		command.regionMatches(true,0,"regressMonthly (",0,16) ||
		command.regionMatches(true,0,"regressMonthlyLog(",0,18) ||
		command.regionMatches(true,0,"regressMonthlyLog (",0,19) ) {
		if ( Message.isDebugOn ) {
			Message.printDebug ( dl, routine,
			"Opening dialog for fillRegression()" );
		}
		edited_cv = new fillRegression_JDialog ( this, cv,
			TSEngine.getTSIdentifiersFromCommands(
			getCommandsAboveSelected ())).getText();
	}
	*/
	else if (action.equals( __Commands_Fill_fillRepeat_String)||
		command.regionMatches( true,0,"fillRepeat",0,10)){
		if ( Message.isDebugOn ) {
			Message.printDebug ( dl, routine,
			"Opening dialog for fillRepeat()" );
		}
		edited_cv = new fillRepeat_JDialog ( this, cv,
			TSEngine.getTSIdentifiersFromCommands(
			getCommandsAboveSelected ())).getText();
	}
	else if (action.equals(
		__Commands_Fill_fillUsingDiversionComments_String)||
		command.regionMatches(
		true,0,"fillUsingDiversionComments",0,26)){
		if ( Message.isDebugOn ) {
			Message.printDebug ( dl, routine,
			"Opening dialog for fillDiversionComments()" );
		}
		edited_cv = new fillUsingDiversionComments_JDialog ( this, cv,
			TSEngine.getTSIdentifiersFromCommands(
			getCommandsAboveSelected ())).getText();
	}
	else if ( action.equals(__Commands_Fill_setAutoExtendPeriod_String)||
		command.regionMatches(true,0,"setAutoExtendPeriod",0,19) ) {
		if ( Message.isDebugOn ) {
			Message.printDebug ( dl, routine,
			"Opening dialog for setAutoExtendPeriod()" );
		}
		edited_cv = new setAutoExtendPeriod_JDialog ( this, cv,
			TSEngine.getTSIdentifiersFromCommands(
			getCommandsAboveSelected ())).getText();
	}
	else if ( action.equals( __Commands_Fill_setAveragePeriod_String) ||
		command.regionMatches(true,0,"setAveragePeriod",0,16) ) {
		if ( Message.isDebugOn ) {
			Message.printDebug ( dl, routine,
			"Opening dialog for setAveragePeriod()" );
		}
		edited_cv = new setAveragePeriod_JDialog ( this, cv,
			TSEngine.getTSIdentifiersFromCommands(
			getCommandsAboveSelected ())).getText();
	}
	else if ( action.equals( __Commands_Fill_setIgnoreLEZero_String) ||
		command.regionMatches(true,0,"setIgnoreLEZero",0,15) ) {
		if ( Message.isDebugOn ) {
			Message.printDebug ( dl, routine,
			"Opening dialog for setIgnoreLEZero()" );
		}
		edited_cv = new setIgnoreLEZero_JDialog ( this, cv,
			TSEngine.getTSIdentifiersFromCommands(
			getCommandsAboveSelected ())).getText();
	}
/*
	else if ( action.equals(__Commands_Fill_setMissingDataValue_String)||
		command.regionMatches(true,0,"setMissingDataValue",0,19) ) {
		if ( Message.isDebugOn ) {
			Message.printDebug ( dl, routine,
			"Opening dialog for setMissingDataValue()" );
		}
		edited_cv = new setMissingDataValue_JDialog ( this, cv,
			TSEngine.getTSIdentifiersFromCommands(
			getCommandsAboveSelected ())).getText();
	}
*/
	else if ( action.equals( __Commands_Fill_setPatternFile_String) ||
		command.regionMatches(true,0,"setPatternFile",0,14) ) {
		if ( Message.isDebugOn ) {
			Message.printDebug ( dl, routine,
			"Opening dialog for setPatternFile()" );
		}
		updateDynamicProps ();
		edited_cv = new setPatternFile_JDialog ( this, __props,
			cv, TSEngine.getTSIdentifiersFromCommands(
			getCommandsAboveSelected ())).getText();
		if ( edited_cv != null ) {
			// Read the pattern file information, at least enough
			// to get a list of the identifiers that are available
			// as reference gages.
			Vector tokens = StringUtil.breakStringList (
				(String)edited_cv.elementAt(0),
				" (,)", StringUtil.DELIM_SKIP_BLANKS |
				StringUtil.DELIM_ALLOW_STRINGS );
			if ( tokens.size() == 2 ) {
				// Save the name of the pattern file.  It will
				// be read if we actually edit
				// fillPattern() commands...
				__fill_pattern_files.addElement (
				((String)tokens.elementAt(1)).trim() );
			}
		}
	}

/* REVISIT - phasing out
	else if ( action.equals(__Commands_Fill_setRegressionPeriod_String)||
		command.regionMatches(true,0,"setRegressionPeriod",0,19) ) {
		if ( Message.isDebugOn ) {
			Message.printDebug ( dl, routine,
			"Opening dialog for setRegressionPeriod()" );
		}
		edited_cv = new setRegressionPeriod_Dialog ( this, cv,
			TSEngine.getTSIdentifiersFromCommands(
			getCommandsAboveSelected ())).getText();
	}
*/
	// Set time series contents...

	else if ( action.equals( __Commands_Set_replaceValue_String)||
		command.regionMatches(true,0,"replaceValue",0,12) ) {
		if ( Message.isDebugOn ) {
			Message.printDebug ( dl, routine,
			"Opening dialog for replaceValue()" );
		}
		edited_cv = new replaceValue_JDialog ( this, cv,
			TSEngine.getTSIdentifiersFromCommands(
			getCommandsAboveSelected ())).getText();
	}
	else if ( action.equals( __Commands_Set_setConstant_String) ||
		command.regionMatches(true,0,"setConstant",0,11) ) {
		if ( Message.isDebugOn ) {
			Message.printDebug ( dl, routine,
			"Opening dialog for setConstant()" );
		}
		edited_cv = new setConstant_JDialog ( this, cv,
			TSEngine.getTSIdentifiersFromCommands(
			getCommandsAboveSelected ())).getText();
	}
	else if ( action.equals( __Commands_Set_setConstantBefore_String)||
		command.regionMatches(true,0,"setConstantBefore",0,17) ) {
		if ( Message.isDebugOn ) {
			Message.printDebug ( dl, routine,
			"Opening dialog for setConstantBefore()" );
		}
		edited_cv = new setConstantBefore_JDialog ( this, cv,
			TSEngine.getTSIdentifiersFromCommands(
			getCommandsAboveSelected ())).getText();
	}
	else if ( action.equals( __Commands_Set_setDataValue_String)||
		command.regionMatches(true,0,"setDataValue",0,12) ) {
		if ( Message.isDebugOn ) {
			Message.printDebug ( dl, routine,
			"Opening dialog for setDataValue()" );
		}
		edited_cv = new setDataValue_JDialog ( this, cv,
			TSEngine.getTSIdentifiersFromCommands(
			getCommandsAboveSelected ())).getText();
	}
	else if ( action.equals( __Commands_Set_setFromTS_String ) ||
		command.regionMatches( true,0,"setFromTS",0,9) ) {
		if ( Message.isDebugOn ) {
			Message.printDebug ( dl, routine,
			"Opening dialog for setFromTS()" );
		}
		edited_cv = new setFromTS_JDialog ( this, cv,
			TSEngine.getTSIdentifiersFromCommands(
			getCommandsAboveSelected ())).getText();
	}
	else if ( action.equals( __Commands_Set_setMax_String)||
		command.regionMatches(true,0,"setMax",0,6) ) {
		if ( Message.isDebugOn ) {
			Message.printDebug ( dl, routine,
			"Opening dialog for setMax()" );
		}
		edited_cv = new setMax_JDialog ( this, cv,
			TSEngine.getTSIdentifiersFromCommands(
			getCommandsAboveSelected ())).getText();
	}

	// Manipulate time series...

	else if ( action.equals( __Commands_Manipulate_add_String)||
		command.regionMatches(true,0,"add(",0,4) ||
		command.regionMatches(true,0,"add (",0,5) ) {
		// Use the above because there may be a conflict with other
		// commands that start with "add".
		if ( Message.isDebugOn ) {
			Message.printDebug ( dl, routine,
			"Opening dialog for add()" );
		}
		edited_cv = new add_JDialog ( this, cv, true,
			TSEngine.getTSIdentifiersFromCommands(
			getCommandsAboveSelected ())).getText();
	}
	else if ( action.equals( __Commands_Manipulate_addConstant_String)||
		command.regionMatches(true,0,"addConstant",0,11) ) {
		if ( Message.isDebugOn ) {
			Message.printDebug ( dl, routine,
			"Opening dialog for addConstant()" );
		}
		edited_cv = new addConstant_JDialog ( this, cv,
			TSEngine.getTSIdentifiersFromCommands(
			getCommandsAboveSelected ())).getText();
	}
	else if ( action.equals( __Commands_Manipulate_adjustExtremes_String)||
		command.regionMatches(true,0,"adjustExtremes",0,14) ) {
		if ( Message.isDebugOn ) {
			Message.printDebug ( dl, routine,
			"Opening dialog for adjustExtremes()" );
		}
		edited_cv = new adjustExtremes_JDialog ( this, cv,
			TSEngine.getTSIdentifiersFromCommands(
			getCommandsAboveSelected ())).getText();
	}
	else if ( action.equals( __Commands_Manipulate_ARMA_String)||
		command.regionMatches(true,0,"ARMA",0,4) ) {
		if ( Message.isDebugOn ) {
			Message.printDebug ( dl, routine,
			"Opening dialog for ARMA()" );
		}
		edited_cv = new ARMA_JDialog ( this, cv,
			TSEngine.getTSIdentifiersFromCommands(
			getCommandsAboveSelected ())).getText();
	}
	else if ( action.equals( __Commands_Manipulate_blend_String)||
		command.regionMatches(true,0,"blend(",0,6) ||
		command.regionMatches(true,0,"blend (",0,7) ) {
		if ( Message.isDebugOn ) {
			Message.printDebug ( dl, routine,
			"Opening dialog for blend()" );
		}
		edited_cv = new blend_JDialog ( this, cv,
			TSEngine.getTSIdentifiersFromCommands(
			getCommandsAboveSelected ())).getText();
	}
	else if ( action.equals(__Commands_Manipulate_convertDataUnits_String)||
		command.regionMatches(true,0,"convertDataUnits",0,16) ) {
		if ( Message.isDebugOn ) {
			Message.printDebug ( dl, routine,
			"Opening dialog for convertDataUnits()" );
		}
		edited_cv = new convertDataUnits_JDialog ( this, cv,
			TSEngine.getTSIdentifiersFromCommands(
			getCommandsAboveSelected ())).getText();
	}
	else if ( action.equals(__Commands_Manipulate_divide_String) ||
		command.regionMatches(true,0,"divide",0,6) ) {
		if ( Message.isDebugOn ) {
			Message.printDebug ( dl, routine,
			"Opening dialog for divide()" );
		}
		edited_cv = new divide_JDialog ( this, cv,
			TSEngine.getTSIdentifiersFromCommands(
			getCommandsAboveSelected ())).getText();
	}
	else if ( action.equals(__Commands_Manipulate_free_String) ||
		command.regionMatches(true,0,"free(",0,5) ||
		command.regionMatches(true,0,"free (",0,6) ) {
		// Check with the parenthesis above because some command might
		// actually start with the word "free".
		if ( Message.isDebugOn ) {
			Message.printDebug ( dl, routine,
			"Opening dialog for free()" );
		}
		edited_cv = new free_JDialog ( this, cv,
			TSEngine.getTSIdentifiersFromCommands(
			getCommandsAboveSelected ())).getText();
	}
	else if ( action.equals(__Commands_Manipulate_multiply_String) ||
		command.regionMatches(true,0,"multiply",0,8) ) {
		if ( Message.isDebugOn ) {
			Message.printDebug ( dl, routine,
			"Opening dialog for multiply()" );
		}
		edited_cv = new multiply_JDialog ( this, cv,
			TSEngine.getTSIdentifiersFromCommands(
			getCommandsAboveSelected ())).getText();
	}
	else if ( action.equals(__Commands_Manipulate_runningAverage_String) ||
		command.regionMatches(true,0,"runningAverage",0,14) ) {
		if ( Message.isDebugOn ) {
			Message.printDebug ( dl, routine,
			"Opening dialog for runningAverage()" );
		}
		edited_cv = new runningAverage_JDialog ( this, cv,
			TSEngine.getTSIdentifiersFromCommands(
			getCommandsAboveSelected ())).getText();
	}
	else if(action.equals(
		__Commands_Manipulate_shiftTimeByInterval_String)||
		command.regionMatches(true,0,"shiftTimeByInterval",0,19) ) {
		if ( Message.isDebugOn ) {
			Message.printDebug ( dl, routine,
			"Opening dialog for shiftTimeByInterval()" );
		}
		edited_cv = new shiftTimeByInterval_JDialog ( this, cv,
			TSEngine.getTSIdentifiersFromCommands(
			getCommandsAboveSelected ())).getText();
	}
	else if ( action.equals( __Commands_Manipulate_subtract_String)||
		command.regionMatches(true,0,"subtract",0,8) ) {
		// Reuse the add dialog...
		if ( Message.isDebugOn ) {
			Message.printDebug ( dl, routine,
			"Opening dialog for subtract()" );
		}
		edited_cv = new add_JDialog ( this, cv, false,
			TSEngine.getTSIdentifiersFromCommands(
			getCommandsAboveSelected ())).getText();
	}

	// Analyze Time Series...

	// Models (all handled by the TSCommandFactory)...

	// Output Time Series...

	else if ( action.equals(
		__Commands_Output_deselectTimeSeries_String) ||
		command.regionMatches(true,0,"deselectTimeSeries",0,18)){
		// Re-use the selectTimeSeries() command dialog...
		if ( Message.isDebugOn ) {
			Message.printDebug ( dl, routine,
			"Opening dialog for deselectTimeSeries()" );
		}
		edited_cv = new selectTimeSeries_JDialog (this, cv,
			TSEngine.getTSIdentifiersFromCommands(
			getCommandsAboveSelected ()), false ).getText();
	}
	else if ( action.equals(
		__Commands_Output_selectTimeSeries_String) ||
		command.regionMatches(true,0,"selectTimeSeries",0,16)){
		if ( Message.isDebugOn ) {
			Message.printDebug ( dl, routine,
			"Opening dialog for selectTimeSeries()" );
		}
		edited_cv = new selectTimeSeries_JDialog (this, cv,
			TSEngine.getTSIdentifiersFromCommands(
			getCommandsAboveSelected ()), true ).getText();
	}
/* REVISIT - phasing out
	else if ( action.equals(
		__Commands_Output_setOutputDetailedHeaders_String) ||
		command.regionMatches(true,0,"setOutputDetailedHeaders",0,24)){
		edited_cv = new setOutputDetailedHeaders_JDialog (this, cv,
			TSEngine.getTSIdentifiersFromCommands(
			getCommandsAboveSelected ())).getText();
	}
*/
	else if ( action.equals( __Commands_Output_setOutputPeriod_String) ||
		command.regionMatches(true,0,"setOutputPeriod",0,15) ) {
		if ( Message.isDebugOn ) {
			Message.printDebug ( dl, routine,
			"Opening dialog for setOutputPeriod()" );
		}
		edited_cv = new setOutputPeriod_JDialog ( this, cv, null ).
							getText();
	}
	else if ( action.equals( __Commands_Output_setOutputYearType_String) ||
		command.regionMatches(true,0,"setOutputYearType",0,17) ) {
		if ( Message.isDebugOn ) {
			Message.printDebug ( dl, routine,
			"Opening dialog for setOutputYearType()" );
		}
		edited_cv = new setOutputYearType_JDialog ( this, cv, null).
							getText();
	}
	else if ( action.equals( __Commands_Output_writeDateValue_String)||
		command.regionMatches(true,0,"writeDateValue",0,14) ) {
		if ( Message.isDebugOn ) {
			Message.printDebug ( dl, routine,
			"Opening dialog for writeDateValue()" );
		}
		updateDynamicProps ();
		edited_cv = new writeDateValue_JDialog ( this, __props,
			cv, TSEngine.getTSIdentifiersFromCommands(
			getCommandsAboveSelected ())).getText();
	}
	else if ( action.equals( __Commands_Output_writeNwsCard_String)||
		command.regionMatches(true,0,"writeNwsCard",0,12) ) {
		if ( Message.isDebugOn ) {
			Message.printDebug ( dl, routine,
			"Opening dialog for writeNwscard()" );
		}
		updateDynamicProps ();
		edited_cv = new writeNwsCard_JDialog ( this, __props,
			cv, TSEngine.getTSIdentifiersFromCommands(
			getCommandsAboveSelected ())).getText();
	}
	/*
	else if (action.equals(
		__Commands_Output_writeNWSRFSESPTraceEnsemble_String)||
		command.regionMatches(
		true,0,"writeNWSRFSESPTraceEnsemble",0,21) ) {
		if ( Message.isDebugOn ) {
			Message.printDebug ( dl, routine,
			"Opening dialog for writeNWSRFSESPTraceEnsemble()" );
		}
		updateDynamicProps ();
		edited_cv = new writeNWSRFSESPTraceEnsemble_JDialog (
			this, __props,
			cv, TSEngine.getTSIdentifiersFromCommands(
			getCommandsAboveSelected ())).getText();
	}
	*/
	else if ( action.equals( __Commands_Output_writeStateCU_String)||
		command.regionMatches(true,0,"writeStateCU",0,12) ) {
		if ( Message.isDebugOn ) {
			Message.printDebug ( dl, routine,
			"Opening dialog for writeStateCU()" );
		}
		updateDynamicProps ();
		edited_cv = new writeStateCU_JDialog ( this, __props,
			cv, TSEngine.getTSIdentifiersFromCommands(
			getCommandsAboveSelected ())).getText();
	}
	else if ( action.equals( __Commands_Output_writeSummary_String)||
		command.regionMatches(true,0,"writeSummary",0,12) ) {
		if ( Message.isDebugOn ) {
			Message.printDebug ( dl, routine,
			"Opening dialog for writeSummary()" );
		}
		updateDynamicProps ();
		edited_cv = new writeSummary_JDialog ( this, __props,
			cv, TSEngine.getTSIdentifiersFromCommands(
			getCommandsAboveSelected ())).getText();
	}

	// General...

	else if ( action.equals( __Commands_General_runProgram_String)||
		(StringUtil.indexOfIgnoreCase(command,"runProgram",0)>= 0) ) { 
		if ( Message.isDebugOn ) {
			Message.printDebug ( dl, routine,
			"Opening dialog for runProgram()" );
		}
		edited_cv = new runProgram_JDialog ( this, cv,
			TSEngine.getTSIdentifiersFromCommands(
			getCommandsAboveSelected ())).getText();
	}
/*
	else if (action.equals(__Commands_General_setBinaryTSDayCutoff_String)||
		command.regionMatches(true,0,"setBinaryTSDayCutoff(",0,21) ||
		command.regionMatches(true,0,"setBinaryTSDayCutoff (",0,22)){
		edited_cv = new setBinaryTSDayCutoff_Dialog ( this, cv,
			TSEngine.getTSIdentifiersFromCommands(
			getCommandsAboveSelected ())).getText();
	}
	else if ( action.equals( __Commands_General_setBinaryTSFile_String) ||
		command.regionMatches(true,0,"setBinaryTSFile(",0,16) ||
		command.regionMatches(true,0,"setBinaryTSFile (",0,17) ) {
		updateDynamicProps ();
		edited_cv = new setBinaryTSFile_Dialog ( this, __props,
			cv, TSEngine.getTSIdentifiersFromCommands(
			getCommandsAboveSelected ())).getText();
	}
	else if (action.equals( __Commands_General_setBinaryTSPeriod_String) ||
		command.regionMatches(true,0,"setBinaryTSPeriod(",0,18) ||
		command.regionMatches(true,0,"setBinaryTSPeriod (",0,19)){
		edited_cv = new setBinaryTSPeriod_Dialog ( this, cv,
			TSEngine.getTSIdentifiersFromCommands(
			getCommandsAboveSelected ())).getText();
	}
*/
	else if ( action.equals( __Commands_General_setDebugLevel_String) ||
		command.regionMatches(true,0,"setDebugLevel",0,13) ) {
		if ( Message.isDebugOn ) {
			Message.printDebug ( dl, routine,
			"Opening dialog for setDebugLevel()" );
		}
		edited_cv = new setDebugLevel_JDialog ( this, cv,
			TSEngine.getTSIdentifiersFromCommands(
			getCommandsAboveSelected ())).getText();
	}
	else if ( action.equals( __Commands_General_setWarningLevel_String) ||
		command.regionMatches(true,0,"setWarningLevel",0,15) ) {
		if ( Message.isDebugOn ) {
			Message.printDebug ( dl, routine,
			"Opening dialog for setWarningLevel()" );
		}
		edited_cv = new setWarningLevel_JDialog ( this, cv,
			TSEngine.getTSIdentifiersFromCommands(
			getCommandsAboveSelected ())).getText();
	}
	else if ( action.equals( __Commands_General_setWorkingDir_String) ||
		command.regionMatches(true,0,"setWorkingDir",0,13) ) {
		if ( Message.isDebugOn ) {
			Message.printDebug ( dl, routine,
			"Opening dialog for setWorkingDir()" );
		}
		updateDynamicProps ();
		edited_cv = new setWorkingDir_JDialog ( this, __props, cv,
			TSEngine.getTSIdentifiersFromCommands(
			getCommandsAboveSelected ())).getText();
	}
	else if ( action.equals(__Commands_General_Comment_String) ||
		command.startsWith("#") ) {
		if ( Message.isDebugOn ) {
			Message.printDebug ( dl, routine,
			"Opening dialog for comments" );
		}
		edited_cv = new comment_JDialog ( this, cv, null ).getText();
	}
	else if ( action.equals(__Commands_General_startComment_String) ) {
		// edited_cv is just a new comment...
		if ( Message.isDebugOn ) {
			Message.printDebug ( dl, routine,
			"Adding start of comment block." );
		}
		edited_cv = new Vector(1);
		edited_cv.addElement( "/*" );
	}
	else if ( action.equals(__Commands_General_endComment_String) ) {
		// edited_cv is just a new comment...
		if ( Message.isDebugOn ) {
			Message.printDebug ( dl, routine,
			"Adding end of comment block." );
		}
		edited_cv = new Vector(1);
		edited_cv.addElement( "*/" );
	}

	// Unrecognized command...

	else {	// Try new approach with a factory...
		if ( Message.isDebugOn ) {
			Message.printDebug ( dl, routine,
			"Using new command code to edit command..." );
		}
		try {	// Do this to update the working directory based on
			// previous commands...
			updateDynamicProps ();
			// Now try to process the command...
			if ( Message.isDebugOn ) {
				Message.printDebug ( dl, routine,
				"Trying command factory for \""+command + "\"");
			}
			TSCommandFactory cf = new TSCommandFactory();
			Command c = null;
			if ( mode == __UPDATE_COMMAND ) {
				// REVISIT SAM 2005-05-09 Need to change this
				// when commands are maintained in the command
				// list data model (in that case just pass the
				// existing command)...
				if ( Message.isDebugOn ) {
					Message.printDebug ( dl, routine,
					"Using command factory to create a new"+
					" command for \"" + command + "\"" );
				}
				c = cf.newCommand(command);
				try {	c.initializeCommand ( command,
					new TSCommandProcessor(
					new TSEngine ( __hbdmi, __rdmi,
					__DIADvisor_dmi,
					__DIADvisor_archive_dmi, __nwsrfs_dmi,
					__smsdmi, this )),
					null,	// Command tag
					1,	// Warning level
					true );	// Full initialization
					if ( Message.isDebugOn ) {
						Message.printDebug ( dl,
						routine,
						"Initialized command " +
						"for \"" + command + "\"" );
					}
				}
				catch ( Exception e ) {
					// Absorb the warning and make the user
					// try to deal with it in the editor
					// dialog.  They can always cancel out.

					// REVISIT SAM 2005-05-09 Need to handle
					// parse error.  Should the editor come
					// up with limited information?
					Message.printWarning ( 3, routine,
					"Unexpected error initializing " +
					"command." );
					Message.printWarning ( 3, routine, e );
				}
			}
			else {	// Else a new blank command is OK but get the
				// command name from the action (menu)...
				// REVISIT SAM 2005-05-09 how to handle TS
				// Alias = commands?  Perhaps the factory needs
				// to search for the leading TS and the command
				// name?  should work!
				// Get the string before the ")" to ignore the
				// extra information in menus.
				// REVISIT SAM 2005-05-10 This should work with
				// everything but special commands like comments
				// and time series identifiers.
				command = StringUtil.getToken(action,")",0,0)+
						")";
				if ( Message.isDebugOn ) {
					Message.printDebug ( dl, routine,
					"Using command factory to create new " +
					"command for \"" + command + "\"" );
				}
				c = cf.newCommand( command );
				c.initializeCommand ( command,
					new TSCommandProcessor(
					new TSEngine ( __hbdmi, __rdmi,
					__DIADvisor_dmi,
					__DIADvisor_archive_dmi, __nwsrfs_dmi,
					__smsdmi, this) ),
					null,	// Command tag
					1,	// Warning level
					false);	// Minimal initialization
				if ( Message.isDebugOn ) {
					Message.printDebug ( dl, routine,
					"Initialized blank command for \"" +
					command + "\"" );
				}
			}
			try {	if ( Message.isDebugOn ) {
					Message.printDebug ( dl, routine,
					"Editing command..." );
				}
				if ( c.editCommand ( this ) ) {
					// Command edits were committed...
					edited_cv = new Vector(1);
					edited_cv.addElement ( c.toString() );
					if ( Message.isDebugOn ) {
						Message.printDebug ( dl,
						routine, "Command edits " +
						"were committed.");
					}
				}
				else {	edited_cv = null;
				}
			}
			catch ( Exception e2 ) {
				// Unexpected error...
				Message.printWarning ( 1, routine,
				"Unexpected error editing command." );
				Message.printWarning ( 3, routine, e2 );
			}
		}
		catch ( UnknownCommandException e ) {
			Message.printStatus ( 2, routine,
			"Unknown command \"" + command +
			"\".  Using generic editor." );
			GenericCommand c = new GenericCommand ();
			try {
			c.initializeCommand ( command,
				new TSCommandProcessor(
				new TSEngine ( __hbdmi, __rdmi,
				__DIADvisor_dmi,
				__DIADvisor_archive_dmi, __nwsrfs_dmi, __smsdmi,
				this )),
				null,	// Command tag
				1,	// Warning level
				false);	// Minimal initialization
			if ( c.editCommand ( this ) ) {
				// Command edits were committed...
				edited_cv = new Vector(1);
				edited_cv.addElement ( c.toString() );
			}
			}
			catch ( Exception e2 ) {
				// Should not happen with the generic editor
				// since it is just editing strings.
				Message.printWarning ( 1, routine,
				"Unexpected error editing command." );
				Message.printWarning ( 3, routine, e2 );
			}
		}
		catch ( InvalidCommandParameterException e ) {
			// REVISIT SAM 2005-05-09 How to edit a command that
			// has bad syntax when errors are checked outside the
			// JDialog?
			Message.printWarning ( 3, routine, e );
		}
		catch ( InvalidCommandSyntaxException e ) {
			// REVISIT SAM 2005-05-09 How to edit a command that
			// has bad syntax when errors are checked outside the
			// JDialog?
			Message.printWarning ( 3, routine, e );
		}
		catch ( Exception e ) {
			// REVISIT SAM 2005-05-09 Unexpected error... 
			Message.printWarning ( 3, routine, e );
		}
	}

	// If the command was actually edited, update it in the commands list...

	if ( edited_cv != null ) {
		updateCommand ( original_cv, edited_cv, mode );
	}

	}
	catch ( Exception e2 ) {
		// REVISIT SAM 2005-05-18 Unexpected error... 
		Message.printWarning ( 3, routine, e2 );
	}
}

/**
Export time series to a file. 
*/
private void export(String format )
{	export ( format, null );
}

/**
Export time series to a file.   Assume that if this is called, the state of the
GUI is such that time series are available from the TSEngine.
@param format Format to export, as a tstool command line option.
@param filename Name of file to save as, as a tstool command line option (e.g.,
"-o filename").  If previewing output, this will be "-preview".
*/
private void export ( String format, String filename )
{	String routine = "TSTool_JFrame.export";
	if ( Message.isDebugOn ) {
		Message.printDebug ( 1, routine, "In export" );
	}

	// Get the time series list

	PropList props = new PropList ( "SaveAsProps" );
	props.set ( "OutputFormat=" + format );
	props.set ( "OutputFile=" + filename );
	// Final list is selected...
	if ( __final_ts_engine != null ) {
		try {	int selected_ts = JGUIUtil.selectedSize(__ts_JList);
			if ( selected_ts == 0 ) {
				__final_ts_engine.processTimeSeries(null,props);
			}
			else {	__final_ts_engine.processTimeSeries (
				__ts_JList.getSelectedIndices(), props );
			}
		}
		catch ( Exception e ) {
			Message.printWarning ( 1, routine,
			"Unable to save time series." );
		}
	}
}

/**
Handle the label redraw event from another GeoView (likely a ReferenceGeoView).
Do not do anything here because we assume that application code is setting
the labels.
@param record Feature being draw.
*/
public String geoViewGetLabel(GeoRecord record) {
	return null;
}

/**
Do nothing.  REVISIT - Should this do the same as a select?
@param devlimits Limits of select in device coordinates(pixels).
@param datalimits Limits of select in data coordinates.
@param selected Vector of selected GeoRecord.  Currently ignored.
*/
public void geoViewInfo(GRShape devlimits, GRShape datalimits,
Vector selected) {}

public void geoViewInfo(GRPoint devlimits, GRPoint datalimits, 
Vector selected) {}

public void geoViewInfo(GRLimits devlimits, GRLimits datalimits,
Vector selected) {}

/**
Handle the mouse motion event from another GeoView (likely a ReferenceGeoView).
Does nothing.
@param devpt Coordinates of mouse in device coordinates(pixels).
@param datapt Coordinates of mouse in data coordinates.
*/
public void geoViewMouseMotion(GRPoint devpt, GRPoint datapt) {}

// REVISIT SAM 2006-03-02
// Should the select use the coordinates?  Not all time series have this
// information available
/**
If a selection is made from the map, use the attributes specified in the lookup
table file to match the attributes in the layer with time series in the query
list.  The coordinates of the select ARE NOT used in the selection.
@param devlimits Limits of select in device coordinates(pixels).
@param datalimits Limits of select in data coordinates.
@param selected Vector of selected GeoRecord.  This is used to match the
attributes for the selected feature with the time series query list.
*/
public void geoViewSelect (	GRShape devlimits, GRShape datalimits,
				Vector selected, boolean append )
{	String routine = "TSTool_JFrame.geoViewSelect";

	try {	// Main try, for development and troubleshooting
	Message.printStatus ( 1, routine,
	"Selecting time series that match map selection." );

	// Select from the time series query list matching the attributes in
	// the selected layer.

	// REVISIT SAM 2006-03-02
	// Need to evaluate how to best read this file once.

	// Read the time series to layer lookup file...

	String filename = tstool.getPropValue ( "TSTool.MapLayerLookupFile" );
	if ( filename == null ) {
		Message.printWarning ( 1, routine,
		"The TSTool.MapLayerLookupFile is not defined - " +
		"cannot link map and time series." );
		return;
	}

	String full_filename = IOUtil.getPathUsingWorkingDir ( filename );
	if ( !IOUtil.fileExists(full_filename) ) {
		Message.printWarning ( 1, routine,
		"The map layer lookup file \"" + full_filename +
		"\" does not exist.  Cannot link map and time series." );
		return;
	}
	
	PropList props = new PropList ("");
	props.set ( "Delimiter=," );		// see existing prototype
	props.set ( "CommentLineIndicator=#" );	// New - skip lines that start
						// with this
	props.set ( "TrimStrings=True" );	// If true, trim strings after
						// reading.
	DataTable table = null;
	try {	table = DataTable.parseFile ( full_filename, props );
	}
	catch ( Exception e ) {
		Message.printWarning ( 1, routine,
		"Error reading the map layer lookup file \"" + full_filename +
		"\".  Cannot link map and time series." );
		return;
	}
	
	int tsize = 0;
	int TS_InputTypeCol_int = -1;
	int TS_DataTypeCol_int = -1;
	int TS_IntervalCol_int = -1;
	int Layer_NameCol_int = -1;
	int Layer_LocationCol_int = -1;
	int Layer_DataSourceCol_int = -1;
	if ( table != null ) {
		tsize = table.getNumberOfRecords();

		try {
		TS_InputTypeCol_int = table.getFieldIndex ( "TS_InputType" );
		}
		catch ( Exception e ) {
			Message.printWarning ( 1, routine,
			"Error finding TS_InputType column in lookup file \"" +
			full_filename +
			"\".  Cannot link map and time series." );
			Message.printWarning ( 3, routine, e );
			JGUIUtil.setWaitCursor ( this, false);
			return;
		}
		try {	TS_DataTypeCol_int = table.getFieldIndex("TS_DataType");
		}
		catch ( Exception e ) {
			Message.printWarning ( 1, routine,
			"Error finding TS_InputType column in lookup file \"" +
			full_filename +
			"\".  Cannot link map and time series." );
			Message.printWarning ( 3, routine, e );
			JGUIUtil.setWaitCursor ( this, false);
			return;
		}
		try {	TS_IntervalCol_int = table.getFieldIndex("TS_Interval");
		}
		catch ( Exception e ) {
			Message.printWarning ( 1, routine,
			"Error finding TS_Interval column in lookup file \"" +
			full_filename +
			"\".  Cannot link map and time series." );
			Message.printWarning ( 3, routine, e );
			JGUIUtil.setWaitCursor ( this, false);
			return;
		}
		try {	Layer_NameCol_int = table.getFieldIndex ( "Layer_Name");
		}
		catch ( Exception e ) {
			Message.printWarning ( 1, routine,
			"Error finding Layer_Name column in lookup file \"" +
			full_filename +
			"\".  Cannot link map and time series." );
			Message.printWarning ( 3, routine, e );
			JGUIUtil.setWaitCursor ( this, false);
			return;
		}
		try {	Layer_LocationCol_int = table.getFieldIndex (
				"Layer_Location");
		}
		catch ( Exception e ) {
			Message.printWarning ( 1, routine,
			"Error finding Layer_Location column in lookup file \""+
			full_filename +
			"\".  Cannot link map and time series." );
			Message.printWarning ( 3, routine, e );
			JGUIUtil.setWaitCursor ( this, false);
			return;
		}
		try {	Layer_DataSourceCol_int = table.getFieldIndex (
			"Layer_DataSource");
		}
		catch ( Exception e ) {
			// Non-fatal...
			Message.printWarning ( 3, routine,
			"Error finding TS_InputType column in lookup file \"" +
			full_filename +
			"\".  Data source will not be considered in lookups." );
			Layer_DataSourceCol_int = -1;
		}
		Message.printStatus ( 2, routine, "TimeSeriesLookup table for "+
		"map has columns...TS_InputType=" + TS_InputTypeCol_int +
		",TS_DataType=" + TS_DataTypeCol_int +
		",TS_Interval=" + TS_IntervalCol_int +
		",Layer_Name=" + Layer_NameCol_int +
		",Layer_Location=" + Layer_LocationCol_int +
		",Layer_DataSource=" + Layer_DataSourceCol_int );
	}

	// Now search through the time series that are in the query list and
	// select those that match the Vector of GeoRecord.  The current
	// selections ARE NOT cleared first (the user can clear them manually
	// if they want).  In this way multiple selections can be made from the
	// map.

	int nrows = __query_TableModel.getRowCount();
	Message.printStatus ( 1, routine,
	"Selecting query list time series based on map selections..." );
	JGUIUtil.setWaitCursor ( this, true );
	int iend = nrows - 1;
	__ignore_ListSelectionEvent = true;	// To increase performance
						// during transfer...
	__ignore_ItemEvent = true;	// To increase performance
	int match_count = 0;
	int ngeo = 0;
	if ( selected != null ) {
		ngeo = selected.size();
	}
	// Loop through the selected features...
	GeoRecord georec;
	TableRecord georec_tablerec = null;	// Attribute table record
	GeoLayerView geolayerview = null;
	TableRecord rec = null;			// Lookup table record.
	String geolayer_name = null;
	TSTool_TS_TableModel generic_tm = null;
	TSTool_HydroBase_TableModel HydroBase_tm = null;
	String ts_inputtype = null;	// The TS input type from lookup file
	String ts_datatype = null;	// The TS data type from lookup file
	String ts_interval = null;	// The TS interval from lookup file
	String layer_name = null;	// The layer name from lookup file
	String layer_location = null;	// The layer location attribute from the
					// lookup file
	int layer_location_field_int =0;// Column index in attribute table for
					// georecord location
	String layer_datasource = null;	// The layer data source attribute from
					// the lookup file
	int layer_datasource_field_int =0;// Column index in attribute table for
					// georecord data source
	String georec_location = null;	// The georec location value.
	String georec_datasource = null;// The georec data source value.
	String tslist_id = null;	// The time series location ID from the
					// time series list.
	String tslist_datasource = null;// The time series data source from the
					// time series list.
	String tslist_datatype = null;	// The time series data type from the
					// time series list.
	String tslist_interval = null;	// The time series interval from the
					// time series list.
	for ( int igeo = 0; igeo < ngeo; igeo++ ) {
		// Get the GeoRecord that was selected...
		georec = (GeoRecord)selected.elementAt(igeo);
		// Get the name of the layer that corresponds to the GeoRecord,
		// which is used to locate the record in the lookup file...
		geolayerview = georec.getLayerView();
		geolayer_name = geolayerview.getName();
		// Find layer that matches the record, using the layer name in
		// the lookup table.  More than one visible layer may be
		// searched.  The data interval is also used to find a layer
		// to match.
		for ( int ilook = 0; ilook < tsize; ilook++ ) {
			Message.printStatus ( 2, routine,
			"Searching lookup file for the layer named \"" +
			geolayer_name + "\"" );
			try {	rec = table.getRecord(ilook);
				ts_inputtype = (String)rec.getFieldValue(
						TS_InputTypeCol_int);
				ts_datatype = (String)rec.getFieldValue(
						TS_DataTypeCol_int);
				ts_interval = (String)rec.getFieldValue(
						TS_IntervalCol_int);
				layer_name = (String)rec.getFieldValue(
						Layer_NameCol_int);
				layer_location = (String)rec.getFieldValue(
						Layer_LocationCol_int);
				layer_datasource = (String)rec.getFieldValue(
						Layer_DataSourceCol_int);
				// REVISIT SAM 2006-03-02
				// Add layer_interval if such an attribute
				// exists, and use this in addition to the
				// layer name to find an appropriate layer in
				// the lookup table.
				Message.printStatus ( 2, routine,
				"Lookup file [" + ilook + "] " +
				"TS_InputType=\"" + ts_inputtype + "\" " +
				"TS_DataType=\"" + ts_datatype + "\" " +
				"TS_Interval=\"" + ts_interval + "\" " +
				"Layer_Name=\"" + layer_name + "\" " +
				"Layer_Location=\"" + layer_location + "\" " +
				"Layer_DataSource=\"" + layer_datasource+"\"" );
			}
			catch ( Exception e ) {
				// Just ignore...
				Message.printWarning ( 3, routine,
				"Unable to get data from lookup.  " +
				"Ignoring record." );
				Message.printWarning ( 3, routine, e );
				continue;
			}
			if ( !geolayer_name.equalsIgnoreCase( layer_name) ) {
				// The lookup record layer name does not match
				// the selected GeoRecord layer name.
				continue;
			}

			// If here then a layer was found so match the selected
			// features with time series in the list.

			Message.printStatus ( 1, routine,
			"Matching layer \"" + layer_name +
			"\" features with time series using " +
			"attribute \"" + layer_location + "\"." );

			try {	layer_location_field_int =
				georec.getLayer().getAttributeTable().
				getFieldIndex( layer_location );
				Message.printStatus ( 2, routine,
				"Attribute \"" + layer_location +
				"\" is field [" + layer_location_field_int+"]");
			}
			catch ( Exception e ) {
				Message.printWarning ( 1, routine,
				"Layer attribute column \"" +
				layer_location + "\" is not found.  " +
				"Cannot link time series and map." );
				Message.printWarning ( 3, routine, e );
				JGUIUtil.setWaitCursor ( this, false);
				return;
			}
			if ( Layer_DataSourceCol_int >= 0 ) {
				try {	layer_datasource =
					(String)rec.getFieldValue(
					Layer_DataSourceCol_int);
					try {	layer_datasource_field_int =
						georec.getLayer().
						getAttributeTable().
						getFieldIndex(layer_datasource);
						Message.printStatus ( 2,routine,
						"Attribute \"" +
						layer_datasource +
						"\" is field [" +
						layer_datasource_field_int+"]");
					}
					catch ( Exception e ) {
						Message.printWarning ( 3,
						routine, "Data source " +
						"attribute column \"" +
						layer_datasource +
						"\" is not found.  " +
						"Ignoring data source." );
						Message.printWarning ( 3,
						routine, e );
						JGUIUtil.setWaitCursor ( this,
						false);
						layer_datasource = null;
					}
				}
				catch ( Exception e ) {
					Message.printWarning ( 3,
					routine,
					"Unable to determine data " +
					"data source from layer " +
					"attribute table.  Ignoring" +
					" data source." );
					// Ignore for now.
					layer_datasource = null;
				}
			}

			// REVISIT SAM 2006-03-02
			// For now only deal with string attributes -
			// later need to handle other types.

			// Get the attribute to be checked for the ID,
			// based on the attribute name in the lookup
			// file...

			// REVISIT SAM 2006-03-02
			// Need to implement DbaseDataTable.getTableRecord to
			// handle on the fly reading to make this a little more
			// directy...

			try {	georec_location =
				(String)georec.getLayer()
				.getAttributeTable().getFieldValue(
				georec.getShape().index,
				layer_location_field_int );
				Message.printStatus ( 2, routine,
				"Trying to find time series with location ID \""
				+ georec_location + "\"..." );
			}
			catch ( Exception e ) {
				Message.printWarning ( 3, routine,
				"Cannot get value for \"" + layer_location +
				"\" attribute.  Skipping record." );
				// Ignore for now.
				Message.printWarning ( 3, routine, e );
				continue;
			}
			// Get the attribute to be checked for the data source,
			// based on the attribute name in the lookup
			// file...
			if (	(Layer_DataSourceCol_int >= 0) &&
				(layer_datasource != null) ) {
			try {	georec_datasource =
				(String)georec.getLayer()
				.getAttributeTable().getFieldValue(
				georec.getShape().index,
				layer_datasource_field_int );
				Message.printStatus ( 2, routine,
				"Trying to find time series with data source \""
				+ georec_datasource + "\"..." );
			}
			catch ( Exception e ) {
				Message.printWarning ( 3, routine,
				"Cannot get value for \"" + layer_datasource +
				"\" attribute.  Skipping record." );
				// Ignore for now.
				Message.printWarning ( 3, routine, e );
				continue;
			}
			}
			// Now use the TS fields in the lookup table to match
			// time series that are listed in the query results.
			// This is done brute force by searching everything in
			// the table model...
			for ( int its = 0; its < nrows; its++ ) {
				// Get the attributes from the time series
				// query list table model.
				// REVISIT SAM 2006-03-02
				// Probably what is needed here is a generic
				// interface on time series table models to
				// have methods that return TSIdent or TSID
				// parts.  For now do it brute force.
				if (	__query_TableModel instanceof
					TSTool_TS_TableModel ) {
					generic_tm = (TSTool_TS_TableModel)
						__query_TableModel;
					tslist_id = (String)generic_tm.
						getValueAt(
						its, generic_tm.COL_ID );
					tslist_datasource = (String)generic_tm.
						getValueAt(
						its,generic_tm.COL_DATA_SOURCE);
					tslist_datatype = (String)generic_tm.
						getValueAt(
						its, generic_tm.COL_DATA_TYPE );
					tslist_interval = (String)generic_tm.
						getValueAt(
						its, generic_tm.COL_TIME_STEP );
				}
				else if(__query_TableModel instanceof
					TSTool_HydroBase_TableModel ) {
					HydroBase_tm =
						(TSTool_HydroBase_TableModel)
						__query_TableModel;
					tslist_id = (String)HydroBase_tm.
						getValueAt(
						its, HydroBase_tm.COL_ID );
					tslist_datasource =(String)HydroBase_tm.
						getValueAt(
						its,HydroBase_tm.
						COL_DATA_SOURCE);
					tslist_datatype = (String)HydroBase_tm.
						getValueAt(
						its,HydroBase_tm.COL_DATA_TYPE);
					tslist_interval = (String)HydroBase_tm.
						getValueAt(
						its,HydroBase_tm.COL_TIME_STEP);
				}
				// Check the attributes against that in the
				// map.  Use some of the look interval
				// information for data type and interval,
				// unless that is supplied in a layer
				// attribute.
				// REVISIT SAM 2006-03-02
				// Currently check the ID and get the data type
				// and interval from the lookup file.  Later
				// can get the interval from the layer attribute
				// data.
				if ( !georec_location.equalsIgnoreCase(
					tslist_id) ) {
					// The ID in the selected feature does
					// not match the time series ID in the
					// list...
					continue;
				}
				if (	(georec_datasource != null) &&
					!georec_datasource.equalsIgnoreCase(
					tslist_datasource) ) {
					// The data source in the selected
					// feature does not match the time
					// series ID in the list...
					continue;
				}
				if ( !ts_datatype.equalsIgnoreCase(
					tslist_datatype) ) {
					// The data type in lookup file for
					// the selected layer does not match the
					// time series data type in the
					// list...
					continue;
				}
				if ( !ts_interval.equalsIgnoreCase(
					tslist_interval) ) {
					// The data interval in lookup file for
					// the selected layer does not match the
					// time series data interval in the
					// list...
					continue;
				}
				// The checked attributes match so select the
				// time series and increment the count (do not
				// deselect first)...
				Message.printStatus ( 2, routine,
				"Selecting time seris [" + its + "]" );
				__query_JWorksheet.selectRow ( its, false );
				// REVISIT SAM 2006-03-02
				// Should the worksheet automatically scroll to
				// the last select?
				++match_count;
			}
		}
	}
	if ( match_count != ngeo ) {
		Message.printWarning ( 1, routine,
		"The number of matched time series (" + match_count +
		") is less than the number of selected features (" + ngeo +
		").\nVerify that the map layer matches the time series list " +
		"and that the lookup file has accurate information." );
	}
	__ignore_ListSelectionEvent = false;
	__ignore_ItemEvent = false;
	updateStatus ( true );
	JGUIUtil.setWaitCursor ( this, false );
	Message.printStatus ( 1, routine, "Selected all time series." );
	}
	catch ( Exception e ) {
		// Unexpected error...
		Message.printWarning ( 1, routine,
		"Unable to link map and TSTool.  See log file." );
		Message.printWarning ( 3, routine, e );
		JGUIUtil.setWaitCursor ( this, false );
	}
}

public void geoViewSelect(GRPoint devlimits, GRPoint datalimits, 
Vector selected, boolean append) {
	geoViewSelect((GRShape)devlimits, (GRShape)datalimits, selected, 
		append);
}

public void geoViewSelect(GRLimits devlimits, GRLimits datalimits,
Vector selected, boolean append) {
	geoViewSelect((GRShape)devlimits, (GRShape)datalimits, selected, 
		append);
}

/**
Handle the zoom event from the GeoView map interface.
This resets the data limits for this GeoView to those specified (if not
null) and redraws the GeoView.  In this class it does nothing.
@param devlimits Limits of zoom in device coordinates(pixels).
@param datalimits Limits of zoom in data coordinates.
*/
public void geoViewZoom(GRShape devlimits, GRShape datalimits) {}

public void geoViewZoom (GRLimits devlim, GRLimits datalim ) {}

/**
Return the command(s) that are currently selected in the final list.
If the command contains only comments, they are all returned.  If it contains
commands and time series identifiers, only the first command (or time series
identifier) is returned.
@return selected commands in final list or null if none are selected.
Also return null if more than one command is selected.
*/
private Vector getCommand ()
{	// First get the list...
	Vector command = getCommands();
	// Now make sure there is only one command, allowing comments in
	// front...
	if ( command == null ) {
		return null;
	}
	int size = command.size();
	boolean comment_found = false;
	String string = null;

	// First check to see if all comments.  If so, then return all of them
	// (an editor only for the comments will be used)...
	// Initialize
	comment_found = true;
	for ( int i = 0; i < size; i++ ) {
		string = (String)command.elementAt(i);
		if ( !isCommentLine(string) ) {
			comment_found = false;
		}
	}
	if ( comment_found ) {
		// All we had was comments so return...
		return command;
	}
	// Else may have mixed comments.  Want to pull out only the
	// first non-comments and assume it is an command.
	for ( int i = 0; i < size; i++ ) {
		string = (String)command.elementAt(i);
		if ( !isCommentLine(string) ) {
			Vector v = new Vector ( 1 );
			v.addElement ( string );
			command = null;
			return v;
		}
	}
/*
	// Else may have mixed comments.  Want to pull out only the
	// non-comments after an optional set of commments...
	for ( int i = 0; i < size; i++ ) {
		string = (String)command.elementAt(i);
		if (	(i == 0) && !isCommentLine(string) ) {
			// First line is not a comment so set like it is
			// so we know to quit when the next comment is found...
			comment_found = true;
		}
		else if ( comment_found && isCommentLine(string) ) {
			// Found a new comment so delete the remaining
			// strings and return what we have so far...
			for ( int j = (size - 1); j <= i; j-- ) {
				command.removeElementAt(j);
			}
			return command;
		}
		else if ( isCommentLine(string) ) {
			// Found a comment so ignore it and indicate that we
			// have found comments.
			comment_found = true;
			command.removeElementAt(i);
			--i;
		}
	}
*/
	return command;
}

/**
Get the list of commands to process.  If any are selected, only they will be
returned.  If none are selected, all will be returned.
@return the commands as a Vector of String.
*/
private Vector getCommands ( )
{	return getCommands ( false );
}

/**
Get the list of commands to process.
@return the commands as a Vector of String.
@param get_all If false, return those that are selected
unless none are selected, in which case all are returned.  If true, all are
returned, regardless of which are selected.
*/
private Vector getCommands (boolean get_all)
{	String rtn="StateDMI_JFrame.getCommands";

	if ( __commands_JListModel.size() == 0 ) {
		// Nothing in list (probably should never see this if the
		// GUI state is being managed properly)...
		Message.printWarning ( 1, rtn, 
		"No commands are in the list." );
		return null;
	}

	int [] selected = __commands_JList.getSelectedIndices();
	int size = 0;
	if ( selected != null ) {
		size = selected.length;
	}

	if ( (size == 0) || get_all ) {
		// Nothing selected or want to get all, get all...
		size = __commands_JListModel.size();
		Vector itemVector = new Vector(size);
		for ( int i = 0; i < size; i++ ) {
			itemVector.addElement ( __commands_JListModel.get(i) );
		}
		return itemVector;
	}
	else {	// Else something selected so get them...
		Vector itemVector = new Vector(size);
		for ( int i = 0; i < size; i++ ) {
			itemVector.addElement (
			(String)__commands_JListModel.get(selected[i]) );
		}
		return itemVector;
	}
}

/**
Get the commands above a command insert position.  Only the requested commands
are returned.  Use this, for example, to get the setWorkingDir() commands above
the insert position for a readXXX() command, so the working directory can be
defined and used in the readXXX_Dialog.  The returned Vector can be processed
by the StateDMI_Processor() constructor.
@return List of commands above the insert point that match the commands in
the needed_commands_Vector.  This will always return a non-null Vector, even if
no commands are in the Vector.
@param needed_commands_Vector Vector of commands that need to be processed
(e.g., "setWorkingDir").  Only the main command name should be defined.
@param get_all if false, only the first found item above the insert point
is returned.  If true, all matching commands above the point are returned in
the order from top to bottom.
*/
public Vector getCommandsAboveInsertPosition (	Vector needed_commands_Vector,
						boolean get_all )
{	// Determine the insert position, which will be the first selected
	// command (or the end of the list if none are selected)...
	int selectedsize = 0;
	int [] selected_indices = __commands_JList.getSelectedIndices();
	if ( selected_indices != null ) {
		selectedsize = selected_indices.length;
	}
	int insert_pos = 0;
	if ( selectedsize == 0 ) {
		// The insert position is the end of the list (same as size)...
		insert_pos = __commands_JListModel.size();
	}
	else {	// The insert position is the first selected item...
		insert_pos = selected_indices[0];
	}
	// Now search backwards matching commands for each of the requested
	// commands...
	int size = 0;
	if ( needed_commands_Vector != null ) {
		size = needed_commands_Vector.size();
	}
	String command;
	Vector found_commands = new Vector();
	// Now loop up through the command list...
	for ( int ic = (insert_pos - 1); ic >= 0; ic-- ) {
		for ( int i = 0; i < size; i++ ) {
			command = (String)needed_commands_Vector.elementAt(i);
			//((String)_command_List.getItem(ic)).trim() );
			if (	command.regionMatches(true,0,((String)
				__commands_JListModel.get(ic)).trim(),0,
				command.length() ) ) {
				found_commands.addElement ( (String)
				__commands_JListModel.get(ic) );
				//Message.printStatus ( 1, "",
				//"Adding command \"" + 
				//__commands_JListModel.get(ic) + "\"" );
				if ( !get_all ) {
					// Don't need to search any more...
					break;
				}
			}
		}
	}
	// Reverse the commands so they are listed in the order of the list...
	size = found_commands.size();
	if ( size <= 1 ) {
		return found_commands;
	}
	Vector found_commands_sorted = new Vector(size);
	for ( int i = size - 1; i >= 0; i-- ) {
		found_commands_sorted.addElement ( found_commands.elementAt(i));
	}
	return found_commands_sorted;
}

/**
Get the commands above the first selected row in the final list.
If nothing is selected, return all the items.
@return final list items above first selected item or all if 
nothing selected.  Return empty non-null Vector if first item is selected.
*/
public Vector getCommandsAboveSelected ()
{	if ( __commands_JListModel.size() == 0) {
		return new Vector();
	}

	int selectedIndices[] = __commands_JList.getSelectedIndices();
	int selectedSize = selectedIndices.length;	
	
	if ( selectedSize == 0 ) {
		// Return all...
		Vector v = new Vector();
		int size = __commands_JListModel.size(); 
		for (int i = 0; i < size; i++) {
			v.addElement (__commands_JListModel.get(i));
		}
		return v;
	}
	if ( selectedIndices[0] == 0 ) {
		// Nothing above.
		selectedIndices = null;
		return new Vector();
	}
	else {	// Return above first selected...
		Vector v = new Vector(selectedIndices[0] + 1);
		for (int i = 0; i < selectedIndices[0]; i++) {
			v.addElement (__commands_JListModel.get(i));
		}
		selectedIndices = null;
		return v;
	}
}

/**
Return a setWorkingDir(xxx) command where xxx is the initial working directory
set when opening/closing a commands file or using File...Set Working Directory.
This command should be prepended to the list of setWorkingDir() commands that
are processed when determining the working directory for an edit dialog.
*/
private String getInitialSetWorkingDirCommand()
{	return "setWorkingDir(\"" + __initial_working_dir + "\")";
}

/**
Return the list of time series objects corresponding to those selected in the
interface (this does not check the isSelected() method in each time series).
The list of selected indices is used to request the corresponding time series
from the TSEngine object.
@return a Vector of the selected time series.
@param return_all_if_none_selected If true return all if no time series happen
to be selected.
*/
private Vector getSelectedTimeSeriesList ( boolean return_all_if_none_selected )
{	if ( __final_ts_engine == null ) {
		return new Vector(1);
	}
	int selected_ts = JGUIUtil.selectedSize(__ts_JList);
	if ( (selected_ts == 0) && return_all_if_none_selected ) {
		// Return all...
		return __final_ts_engine.getTimeSeriesList ( null );
	}
	else {	// Return the selected...
		return __final_ts_engine.getTimeSeriesList (
				__ts_JList.getSelectedIndices() );
	}
}

/**
Respond to "Get Time Series List" being clicked.
*/
private void getTimeSeriesListClicked()
{	String message, routine = "TSTool_JFrame.getTimeSeriesListClicked";
	Message.printStatus ( 1, routine,
	"Getting time series list from " + __selected_input_type +
	" input type..." );

	// Verify that the input filters have valid data...

	if ( __selected_input_filter_JPanel != __input_filter_generic_JPanel ) {
		if (	!((InputFilter_JPanel)__selected_input_filter_JPanel).
			checkInput(true) ) {
			// An input error was detected so don't get the time
			// series...
			return;
		}
	}

	// Read the time series list and display in the Time Series List
	// area.  Return if an error occurs because the message at the bottom
	// should only be printed if successful.

	if ( __selected_input_type.equals (__INPUT_TYPE_DateValue)) {
		try {	readDateValueHeaders ();
		}
		catch ( Exception e ) {
			message = "Error reading DateValue file.  Cannot " +
			"display time series list.";
			Message.printWarning ( 1, routine, message );
			Message.printWarning ( 2, routine, e );
			return;
		}
	}
	else if ( __selected_input_type.equals (__INPUT_TYPE_DIADvisor)) {
		try {	readDIADvisorHeaders(); 
		}
		catch ( Exception e ) {
			message = "Error reading DIADvisor.  Cannot " +
			"display time series list.";
			Message.printWarning ( 1, routine, message );
			Message.printWarning ( 2, routine, e );
			return;
		}
	}
	else if ( __selected_input_type.equals (__INPUT_TYPE_HydroBase)) {
		try {	readHydroBaseHeaders ( null ); 
		}
		catch ( Exception e ) {
			message = "Error reading HydroBase.  Cannot " +
			"display time series list.";
			Message.printWarning ( 1, routine, message );
			Message.printWarning ( 2, routine, e );
			return;
		}
	}
	else if ( __selected_input_type.equals (__INPUT_TYPE_MEXICO_CSMN)) {
		try {	readMexicoCSMNHeaders ();
		}
		catch ( Exception e ) {
			message =
			"Error reading Mexico CSMN catalog file.  Cannot " +
			"display time series list.";
			Message.printWarning ( 1, routine, message );
			Message.printWarning ( 2, routine, e );
			return;
		}
	}
	else if ( __selected_input_type.equals (__INPUT_TYPE_MODSIM)) {
		try {	readMODSIMHeaders ();
		}
		catch ( Exception e ) {
			message = "Error reading MODSIM file.  Cannot " +
			"display time series list.";
			Message.printWarning ( 1, routine, message );
			Message.printWarning ( 2, routine, e );
			return;
		}
	}
	else if ( __selected_input_type.equals (__INPUT_TYPE_NWSCARD)) {
		try {	readNWSCARDHeaders ();
		}
		catch ( Exception e ) {
			message = "Error reading NWS CARD file.  Cannot " +
			"display time series list.";
			Message.printWarning ( 1, routine, message );
			Message.printWarning ( 2, routine, e );
			return;
		}
	}
	else if ( __selected_input_type.equals(
		__INPUT_TYPE_NWSRFS_ESPTraceEnsemble)) {
		try {	readNWSRFSESPTraceEnsembleHeaders ();
		}
		catch ( Exception e ) {
			message =
			"Error reading NWSRFS_ESPTraceEnsemble file.  Cannot " +
			"display time series list.";
			Message.printWarning ( 1, routine, message );
			Message.printWarning ( 2, routine, e );
			return;
		}
	}
	else if ( __selected_input_type.equals (__INPUT_TYPE_NWSRFS_FS5Files)) {
		try {	// This reads the time series headers and displays the
			// results in the list...
			readNWSRFSFS5FilesHeaders ();
		}
		catch ( Exception e ) {
			message =
			"Error reading NWSRFS FS5Files time series list. " +
			"Cannot display time series list.";
			Message.printWarning ( 1, routine, message );
			Message.printWarning ( 2, routine, e );
			return;
		}
	}
	else if ( __selected_input_type.equals (__INPUT_TYPE_RiversideDB)) {
		try {	readRiversideDBHeaders(); 
		}
		catch ( Exception e ) {
			message = "Error reading RiversideDB.  Cannot " +
			"display time series list.";
			Message.printWarning ( 1, routine, message );
			Message.printWarning ( 2, routine, e );
			return;
		}
	}
	else if ( __selected_input_type.equals (__INPUT_TYPE_RiverWare)) {
		try {	readRiverWareHeaders ();
		}
		catch ( Exception e ) {
			message = "Error reading RiverWare file.  Cannot " +
			"display time series list.";
			Message.printWarning ( 1, routine, message );
			Message.printWarning ( 2, routine, e );
			return;
		}
	}
	else if ( __selected_input_type.equals (__INPUT_TYPE_StateCU)) {
		try {	readStateCUHeaders ();
		}
		catch ( Exception e ) {
			message = "Error reading StateCU file.  Cannot " +
			"display time series list.";
			Message.printWarning ( 1, routine, message );
			Message.printWarning ( 2, routine, e );
			return;
		}
	}
	else if ( __selected_input_type.equals (__INPUT_TYPE_StateMod)) {
		try {	readStateModHeaders ();
		}
		catch ( Exception e ) {
			message = "Error reading StateMod file.  Cannot " +
			"display time series list.";
			Message.printWarning ( 1, routine, message );
			Message.printWarning ( 2, routine, e );
			return;
		}
	}
	else if ( __selected_input_type.equals (__INPUT_TYPE_StateModB)) {
		try {	readStateModBHeaders ();
		}
		catch ( Exception e ) {
			message = "Error reading StateMod binary file.  Cannot "
			+ "display time series list.";
			Message.printWarning ( 1, routine, message );
			Message.printWarning ( 2, routine, e );
			return;
		}
	}
	else if ( __selected_input_type.equals (__INPUT_TYPE_USGSNWIS)) {
		try {	readUsgsNwisHeaders ();
		}
		catch ( Exception e ) {
			message = "Error reading USGS NWIS file.  Cannot " +
			"display time series list.";
			Message.printWarning ( 1, routine, message );
			Message.printWarning ( 2, routine, e );
			return;
		}
	}
	Message.printStatus ( 1, routine,
	"Time series list from " + __selected_input_type +
	" input type are listed in Time Series List area." );
}

/**
Handle actions from the message log viewer.  In particular, when a command is
selected and the user wants to go to the command in the interface.
@param tag Tag that identifies the command.  This is of the format:
<pre>
<command,count>
</pre>
where "command" is the command number (1+) and "count" is an optional count of
warnings for the command.
*/
public void goToMessageTag ( String tag )
{	String command_line = "";
	if ( tag.indexOf(",") >= 0 ) {
		String first_token = StringUtil.getToken(tag,",",0,0);
		if ( first_token.equalsIgnoreCase("ProcessCommands") ) {
			// Get the command number from the second token...
			command_line = StringUtil.getToken(tag,",",0,1);
		}
		else {	// Get the command number from the first token...
			command_line = StringUtil.getToken(tag,",",0,0);
		}
	}
	else {	// Get the command number from the only tag...
		if ( StringUtil.isInteger(tag) ) {
			command_line = tag;
		}
	}
	if ( StringUtil.isInteger(command_line) ) {
		int iline = StringUtil.atoi(command_line) - 1;
		if ( (iline >= 0) && (iline < __commands_JListModel.size()) ) {
			// Clear previous selections...
			__commands_JList.clearSelection();
			// Select the current tag...
			selectCommand ( iline );
			// Position the list...
			__commands_JList.ensureIndexIsVisible(iline);
		}
	}
}

/**
Graph time series that are in the final list.
*/
private void graphTS ( String type )
{	graphTS ( type, null );
}

/**
Graph time series that are in the final list.
@param graph_type Type of graph (same as tstool command line arguments).
*/
private void graphTS ( String graph_type, String params ) 
{	String routine = "TSTool_JFrame.graphTS";

	try {	JGUIUtil.setWaitCursor ( this, true );
		PropList props = new PropList ( "GraphProps" );
		props.set ( "OutputFormat=" + graph_type );
		if ( params != null ) {
			props.set ( "Parameters=" + params );
		}
		// Final list is selected...
		if ( __final_ts_engine != null ) {
			try {	int selected_ts=
					JGUIUtil.selectedSize(__ts_JList);
				if ( selected_ts == 0 ) {
					__final_ts_engine.processTimeSeries (
					null, props );
				}
				else {	__final_ts_engine.processTimeSeries (
					__ts_JList.getSelectedIndices(), props);
				}
			}
			catch ( Exception e ) {
				Message.printWarning ( 1, routine,
				"Unable to graph time series." );
			}
		}
		JGUIUtil.setWaitCursor ( this, false );
	}
	catch ( Exception e ) {
		JGUIUtil.setWaitCursor ( this, false );
	}
}

/**
Initialize the GUI.
@param show_main Indicates if the main interface should be shown.
*/
private void initGUI ( boolean show_main )
{	String routine = "TSTool_JFrame.initGUI";
	try {	// To catch layout problems...
	int y;

	try {	JGUIUtil.setSystemLookAndFeel(true);
	}
	catch (Exception e) {
		Message.printWarning ( 2, routine, e );
	}

	// Need this even if no main GUI...

	JGUIUtil.setIcon(this, JGUIUtil.getIconImage());

	if ( show_main ) {
		// If not showing main, don't initialize menus, to speed
		// performance.
		initGUIMenus ();
	}

	// Remainder of main window...

	// objects used throughout the GUI layout
	int buffer = 3;
	Insets insetsNLNR = new Insets(0,buffer,0,buffer);
	Insets insetsNNNR = new Insets(0,0,0,buffer);
	Insets insetsNLNN = new Insets(0,buffer,0,0);
        Insets insetsTLBR = new Insets(buffer,buffer,buffer,buffer);
        Insets insetsNLBR = new Insets(0,buffer,buffer,buffer);
	Insets insetsTLNR = new Insets(buffer,buffer,0,buffer);
	Insets insetsNNNN = new Insets(0,0,0,0);
       	GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();

	// Panel to hold the query components, added to the top of the main
	// content pane...

	if ( show_main ) {
        JPanel query_JPanel = new JPanel();
        query_JPanel.setLayout(new BorderLayout());
        getContentPane().add("North", query_JPanel);
        
	// --------------------------------------------------------------------
	// Query input components...
	// --------------------------------------------------------------------

	__query_input_JPanel = new JPanel();
	__query_input_JPanel.setLayout(gbl);
	__query_input_JPanel.setBorder(
		BorderFactory.createTitledBorder (
		BorderFactory.createLineBorder(Color.black),
		"Input/Query Options" ));
	
    query_JPanel.add("West", __query_input_JPanel);
 
	y=0;
        JGUIUtil.addComponent(__query_input_JPanel, new JLabel("Input Type:"), 
		0, y, 1, 1, 0.0, 0.0, insetsNLNN, gbc.NONE, gbc.EAST);
        __input_type_JComboBox = new SimpleJComboBox(false);
        __input_type_JComboBox.setMaximumRowCount ( 15 );
        __input_type_JComboBox.setToolTipText (
		"<HTML>The input type is the file/database format being read."+
		"</HTML>" );
	__input_type_JComboBox.addItemListener( this );
        JGUIUtil.addComponent(__query_input_JPanel, __input_type_JComboBox, 
		1, y++, 2, 1, 1.0, 0.0, insetsNNNR, gbc.NONE, gbc.WEST);

        JGUIUtil.addComponent(__query_input_JPanel, new JLabel("Input Name:"), 
		0, y, 1, 1, 0.0, 0.0, insetsNLNN, gbc.NONE, gbc.EAST);
        __input_name_JComboBox = new SimpleJComboBox(false);
        __input_name_JComboBox.setToolTipText (
		"<HTML>The input name is the name of the file or database" +
		" being read.<BR>It will default or be promted for after " +
		"selecting the input type.</HTML>" );
	__input_name_JComboBox.addItemListener( this );
        JGUIUtil.addComponent(__query_input_JPanel, __input_name_JComboBox, 
		1, y++, 2, 1, 1.0, 0.0, insetsNNNR, gbc.NONE, gbc.WEST);

        JGUIUtil.addComponent(__query_input_JPanel, new JLabel("Data Type:"), 
		0, y, 1, 1, 0.0, 0.0, insetsNLNN, gbc.NONE, gbc.EAST);
	__data_type_JComboBox = new SimpleJComboBox(false);
        __data_type_JComboBox.setMaximumRowCount ( 20 );
	__data_type_JComboBox.setToolTipText (
		"<HTML>The data type is used to filter the list of time " +
		"series.</HTML>" );
	__data_type_JComboBox.addItemListener( this );
        JGUIUtil.addComponent(__query_input_JPanel, __data_type_JComboBox, 
		1, y++, 2, 1, 1.0, 0.0, insetsNNNR, gbc.NONE, gbc.WEST);

        JGUIUtil.addComponent(__query_input_JPanel, new JLabel("Time Step:"), 
		0, y, 1, 1, 0.0, 0.0, insetsNLNN, gbc.NONE, gbc.EAST);
        __time_step_JComboBox = new SimpleJComboBox(false);
	__time_step_JComboBox.setToolTipText (
		"<HTML>The time step is used to filter the list of time " +
		"series.</HTML>" );
	__time_step_JComboBox.addItemListener( this );
        JGUIUtil.addComponent(__query_input_JPanel, __time_step_JComboBox, 
		1, y++, 2, 1, 1.0, 0.0, insetsNNNR, gbc.NONE, gbc.WEST);

	// Save the position for the input filters, which will be added after
	// database connections are made...

	__input_filter_y = y;
	++y;	// Increment for GUI features below.

	// Force the choices to assume some reasonable values...

	setInputTypeChoices();

        __get_ts_list_JButton = new SimpleJButton(
		BUTTON_TOP_GET_TIME_SERIES,this);
	__get_ts_list_JButton.setToolTipText (
		"<HTML>Get a list of time series but not the full time " +
		"series data.<BR>Time series can then be selected for " +
		"processing.</HTML>" );
        JGUIUtil.addComponent(__query_input_JPanel, __get_ts_list_JButton, 
		2, y++, 1, 1, 0, 0, insetsTLNR, gbc.NONE, gbc.EAST);

	// --------------------------------------------------------------------
	// Query results components...
	// --------------------------------------------------------------------

	__query_results_JPanel = new JPanel();
    __query_results_JPanel.setLayout(gbl);
	__query_results_JPanel.setBorder(
		BorderFactory.createTitledBorder (
		BorderFactory.createLineBorder(Color.black),
		"Time Series List"));
    
    //KAT
    // set the minimum size for the panel based on the default size
    // used for HydroBase
   __query_results_JPanel.setPreferredSize(new Dimension( 460, 200 ));
    
    query_JPanel.add("Center", __query_results_JPanel);

	// Add the table for time series list...

	y=0;
	try {
	PropList props = new PropList ( "QueryList" );
	props.add("JWorksheet.ShowRowHeader=true");
	props.add("JWorksheet.AllowCopy=true");
	JScrollWorksheet sjw = new JScrollWorksheet ( 0, 0, props );
	__query_JWorksheet = sjw.getJWorksheet ();
	__query_JWorksheet.setPreferredScrollableViewportSize(null);
	// Listen for mouse events to enable the buttons in the Time Series
	// area...
	__query_JWorksheet.addMouseListener ( this );
	__query_JWorksheet.addJWorksheetListener ( this );
        JGUIUtil.addComponent(__query_results_JPanel, sjw,
		0, y++, 3, 7, 1.0, 1.0, insetsNLBR, gbc.BOTH, gbc.WEST);
	}
	catch ( Exception e ) {
		// Absorb the exception in most cases - print if developing to
		// see if this issue can be resolved.
		if ( Message.isDebugOn && IOUtil.testing()  ) {
			Message.printWarning ( 3, routine,
			"For developers:  caught exception initializing " +
			"JWorksheet at setup." );
			Message.printWarning ( 3, routine, e );
		}
	}

	// Add the button to select all the time series...

	y = 7;
        __CopySelectedToCommands_JButton = new SimpleJButton(
		BUTTON_TOP_COPY_SELECTED_TO_COMMANDS,this);
	__CopySelectedToCommands_JButton.setToolTipText (
		"<HTML>Copy selected time series from above to the Commands " +
		"list,<BR>as time series identifiers.</HTML>" );
        JGUIUtil.addComponent ( __query_results_JPanel,
		__CopySelectedToCommands_JButton, 
		1, y, 1, 1, 0.0, 0.0, insetsNNNR, gbc.NONE, gbc.EAST );
        __CopyAllToCommands_JButton = new SimpleJButton(
		BUTTON_TOP_COPY_ALL_TO_COMMANDS,this);
	__CopyAllToCommands_JButton.setToolTipText (
		"<HTML>Copy all time series from above to the Commands " +
		"list,<BR>as time series identifiers.</HTML>" );
        JGUIUtil.addComponent ( __query_results_JPanel,
		__CopyAllToCommands_JButton, 
		2, y, 1, 1, 0.0, 0.0, insetsNNNR, gbc.NONE, gbc.EAST );

	} // end if ( show_main )
 
	// --------------------------------------------------------------------
	// Command components...
	// --------------------------------------------------------------------

	// Use a panel to include both the commands and time series panels and
	// place in the center, to allow resizing.

	JPanel center_JPanel = new JPanel ();
        center_JPanel.setLayout(gbl);
        getContentPane().add("Center", center_JPanel);

        // Commands JPanel - 8 columns wide for grid bag layout
        __commands_JPanel = new JPanel();
        __commands_JPanel.setLayout(gbl);
	__commands_JPanel.setBorder(
		BorderFactory.createTitledBorder (
		BorderFactory.createLineBorder(Color.black), "Commands" ));
	JGUIUtil.addComponent(center_JPanel, __commands_JPanel,
		0, 0, 1, 1, 1.0, 1.0, insetsNNNN, gbc.BOTH, gbc.CENTER);

	__commands_JListModel = new DefaultListModel();
	__commands_JList = new JList ( __commands_JListModel );
	__commands_JList.setFont ( new Font("Courier", Font.PLAIN, 11 ) );
	// The following prototype value looks like nonsense, but should ensure
	// that the line height accomodates both very tall characters, and those
	// that swoop below the line.
	__commands_JList.setPrototypeCellValue("gjqqyAZ");
	//Dimension minimum_Dimension = new Dimension ( 300, 100 );
	//__commands_JList.setMinimumSize ( minimum_Dimension );
	__commands_JList.addListSelectionListener ( this );
	__commands_JList.addKeyListener ( this );
	__commands_JList.addMouseListener ( this );
	JScrollPane commands_JScrollPane = new JScrollPane ( __commands_JList );

	// The following line works in tandem with the call to 
	// setPrototypeCellValue() above.  setPrototypeCellValue() changes the
	// fixedCellWidth and fixedCellHeight on the JList so that the given
	// string above can fit, which is good.  The downside is that setting
	// the fixedCellWidth makes it so that the JList never scrolls 
	// horizontally, but instead trims long strings with an ellipsis.  
	// If the call to setPrototypeCellValue() is removed for some reason,
	// the following line should be removed as well.
	__commands_JList.setFixedCellWidth(-1);
	
	JGUIUtil.addComponent(__commands_JPanel, commands_JScrollPane,
		0, 0, 8, 5, 1.0, 1.0, insetsNLNR, gbc.BOTH, gbc.WEST);
	// Popup menu for the commands list...
	initGUIMenus_CommandsPopup ();

	// Popup menu for the input name field...
	__input_name_JPopupMenu = new JPopupMenu("Input Name Actions");

	// Buttons that correspond to the command list 

	// Put on left because user is typically working in that area...
	__Run_SelectedCommands_JButton =
		new SimpleJButton(__Button_RunSelectedCommands_String,
		__Run_SelectedCommandsCreateOutput_String, this);
	__Run_SelectedCommands_JButton.setToolTipText (
		"<HTML>Run selected commands from above to generate time " +
		"series,<BR>which will be shown in the Time Series Results " +
		"list below.</HTML>" );
	JGUIUtil.addComponent(__commands_JPanel,
		__Run_SelectedCommands_JButton,
		5, 5, 1, 1, 0.0, 0.0, insetsTLNR, gbc.NONE, gbc.EAST);
	__Run_AllCommands_JButton =
		new SimpleJButton(__Button_RunAllCommands_String,
		__Run_AllCommandsCreateOutput_String, this);
	__Run_AllCommands_JButton.setToolTipText (
		"<HTML>Run all commands from above to generate time series," +
		"<BR>which will be shown in the Time Series Results list " +
		"below.</HTML>" );
	JGUIUtil.addComponent(__commands_JPanel, __Run_AllCommands_JButton,
		6, 5, 1, 1, 0.0, 0.0, insetsTLNR, gbc.NONE, gbc.EAST);
	// Put on right because we want it to be a decision to clear...
	__ClearCommands_JButton = new SimpleJButton(
		__Button_ClearCommands_String, this);
	__ClearCommands_JButton.setToolTipText (
		"<HTML>Clear selected commands from above." +
		"<BR>Clear all commands if none are selected.</HTML>" );
	JGUIUtil.addComponent(__commands_JPanel, __ClearCommands_JButton, 
		7, 5, 1, 1, 0.0, 0.0, insetsTLNR, gbc.NONE, gbc.EAST);

	// --------------------------------------------------------------------
	// Time series output components...
	// --------------------------------------------------------------------

	// Final time series list...

        __ts_JPanel = new JPanel();
        __ts_JPanel.setLayout(gbl);
	__ts_JPanel.setBorder(
		BorderFactory.createTitledBorder (
		BorderFactory.createLineBorder(Color.black),
		"Time Series Results" ));
	JGUIUtil.addComponent(center_JPanel, __ts_JPanel,
		0, 1, 1, 1, 1.0, 1.0, insetsNNNN, gbc.BOTH, gbc.CENTER);

	__ts_JListModel = new DefaultListModel();
	__ts_JList = new JList ( __ts_JListModel );
	//minimum_Dimension = new Dimension ( 300, 100 );
	//__ts_JList.setMinimumSize ( minimum_Dimension );
	__ts_JList.addKeyListener ( this );
	__ts_JList.addListSelectionListener ( this );
	__ts_JList.addMouseListener ( this );
	JScrollPane ts_JScrollPane = new JScrollPane ( __ts_JList );
	JGUIUtil.addComponent(__ts_JPanel, ts_JScrollPane, 
		0, 15, 8, 5, 1.0, 1.0, insetsNLNR, gbc.BOTH, gbc.WEST);

	// Popup menu for the time series results list...
	initGUIMenus_ResultsPopup ();

	// --------------------------------------------------------------------
	// Status messages...
	// --------------------------------------------------------------------

	// Bottom panel for the information TextFields.  Add this as the south
	// panel of the main interface since it is not resizable...

	JPanel bottom_JPanel = new JPanel();
	bottom_JPanel.setLayout (gbl);

	__message_JTextField = new JTextField();
	__message_JTextField.setEditable(false);
	JGUIUtil.addComponent(bottom_JPanel, __message_JTextField,
		0, 0, 7, 1, 1.0, 0.0, gbc.HORIZONTAL, gbc.WEST);
	/* REVISIT - SAM 2004-02-13 - enable later when commands are a thread
	  for now replace with a blank text field
	__processor_JProgressBar = new JProgressBar ();
	__processor_JProgressBar.setStringPainted ( true );
	JGUIUtil.addComponent(bottom_JPanel, __processor_JProgressBar,
		7, 0, 2, 1, 0.0, 0.0, gbc.NONE, gbc.WEST);
	*/
	JTextField temp_JTextField = new JTextField ( 5 );
	temp_JTextField.setEditable ( false );
	JGUIUtil.addComponent(bottom_JPanel, temp_JTextField,
		7, 0, 2, 1, 0.0, 0.0, gbc.NONE, gbc.WEST);
	// END REVISIT
	__status_JTextField = new JTextField ( 5 );
	__status_JTextField.setEditable(false);
	JGUIUtil.addComponent(bottom_JPanel, __status_JTextField,
		9, 0, 1, 1, 0.0, 0.0, gbc.NONE, gbc.WEST);

	getContentPane().add ("South", bottom_JPanel);

	updateTextFields ( -1, "TSTool_JFrame.initGUI",
			"Open a commands file or add new commands.",
			__STATUS_READY );
	updateStatus ();
        pack();
	setSize ( 800, 600 );
	JGUIUtil.center ( this );
	if ( !tstool.isServer() && !IOUtil.isBatch() ) {
        	setVisible ( show_main );
		// Do this to make sure the GUI redraws.  Otherwise, sometimes
		// it may show up with gray areas...
		// SAM testing... does not fix problem yet.
		//this.invalidate();
	}
	}
	catch ( Exception e ) {
		Message.printWarning ( 2, "", e );
	}
	__gui_initialized = true;
	// Select an input type...
	if ( show_main ) {
	if ( __hbdmi != null ) {
		// Select HydroBase for CDSS use...
		__input_type_JComboBox.select( null );
		__input_type_JComboBox.select( __INPUT_TYPE_HydroBase );
	}
	else {	__input_type_JComboBox.select( null );
		__input_type_JComboBox.select( __INPUT_TYPE_DateValue );
	}
	}
    
}

// REVISIT - is this code also called when a new database connection is made
// dynamically or assume that the filters will not change?
/**
Initialize the input filters.  An input filter is defined and added for each
enabled input type but only one is set visible at a time.  Later, as an input
type is selected, the appropriate input filter is made visible.
@param y Layout position to add the input filters.
*/
private void initGUIInputFilters ( int y )
{	// Define and add specific input filters...
	String routine = "TSTool_JFrame.initGUIInputFilters";
	int buffer = 3;
	Insets insets = new Insets(0,buffer,0,0);
	GridBagConstraints gbc = new GridBagConstraints ();
	if ( __source_HydroBase_enabled && (__hbdmi != null) ) {
		// Add input filters for stations...

		try {	__input_filter_HydroBase_station_JPanel = new
			HydroBase_GUI_StationGeolocMeasType_InputFilter_JPanel(
			__hbdmi);
        		JGUIUtil.addComponent(__query_input_JPanel,
				__input_filter_HydroBase_station_JPanel,
				0, y, 3, 1, 0.0, 0.0, insets, gbc.HORIZONTAL,
				gbc.EAST );
			__input_filter_JPanel_Vector.addElement (
				__input_filter_HydroBase_station_JPanel );
		}
		catch ( Exception e ) {
			Message.printWarning ( 2, routine,
			"Unable to initialize input filter for HydroBase" +
			" stations." );
			Message.printWarning ( 2, routine, e );
		}

		// Add input filters for structures - there is one panel for
		// "total" time series and one for water class time series that
		// can be filtered by SFUT...

		try {	__input_filter_HydroBase_structure_JPanel = new
				HydroBase_GUI_StructureGeolocStructMeasType_InputFilter_JPanel(
				__hbdmi, false );
        		JGUIUtil.addComponent(__query_input_JPanel,
				__input_filter_HydroBase_structure_JPanel,
				0, y, 3, 1, 0.0, 0.0, insets, gbc.HORIZONTAL,
				gbc.EAST );
			__input_filter_JPanel_Vector.addElement (
				__input_filter_HydroBase_structure_JPanel );
		}
		catch ( Exception e ) {
			Message.printWarning ( 2, routine,
			"Unable to initialize input filter for HydroBase" +
			" structures." );
			Message.printWarning ( 2, routine, e );
		}

		try {	__input_filter_HydroBase_structure_sfut_JPanel = new
			HydroBase_GUI_StructureGeolocStructMeasType_InputFilter_JPanel(
				__hbdmi, true );
        		JGUIUtil.addComponent(__query_input_JPanel,
				__input_filter_HydroBase_structure_sfut_JPanel,
				0, y, 3, 1, 0.0, 0.0, insets, gbc.HORIZONTAL,
				gbc.EAST );
			__input_filter_JPanel_Vector.addElement (
				__input_filter_HydroBase_structure_sfut_JPanel);
		}
		catch ( Exception e ) {
			Message.printWarning ( 2, routine,
			"Unable to initialize input filter for HydroBase" +
			" structures with SFUT." );
			Message.printWarning ( 2, routine, e );
		}

		// Add input filters for structure irrig_summary_ts,

		try {	__input_filter_HydroBase_irrigts_JPanel = new
			HydroBase_GUI_StructureIrrigSummaryTS_InputFilter_JPanel
				( __hbdmi );
        		JGUIUtil.addComponent(__query_input_JPanel,
				__input_filter_HydroBase_irrigts_JPanel,
				0, y, 3, 1, 0.0, 0.0, insets, gbc.HORIZONTAL,
				gbc.EAST );
			__input_filter_JPanel_Vector.addElement (
				__input_filter_HydroBase_irrigts_JPanel );
		}
		catch ( Exception e ) {
			Message.printWarning ( 2, routine,
			"Unable to initialize input filter for HydroBase" +
			" irrigation summary time series - old database?" );
			Message.printWarning ( 2, routine, e );
		}

		// Add input filters for CASS agricultural crop statistics,
		// only available for newer databases.  For now, just catch an
		// exception when not supported.

		try {	__input_filter_HydroBase_CASSCropStats_JPanel = new
			HydroBase_GUI_AgriculturalCASSCropStats_InputFilter_JPanel
				( __hbdmi );
        		JGUIUtil.addComponent(__query_input_JPanel,
				__input_filter_HydroBase_CASSCropStats_JPanel,
				0, y, 3, 1, 0.0, 0.0, insets, gbc.HORIZONTAL,
				gbc.EAST );
			__input_filter_JPanel_Vector.addElement (
				__input_filter_HydroBase_CASSCropStats_JPanel );
		}
		catch ( Exception e ) {
			// Agricultural_CASS_crop_stats probably not in
			// HydroBase...
			Message.printWarning ( 2, routine,
			"Unable to initialize input filter for HydroBase" +
			" CASS crop statistics - old database?" );
			Message.printWarning ( 2, routine, e );
		}

		// Add input filters for CASS agricultural livestock statistics,
		// only available for newer databases.  For now, just catch an
		// exception when not supported.

		try {	__input_filter_HydroBase_CASSLivestockStats_JPanel = new
			HydroBase_GUI_AgriculturalCASSLivestockStats_InputFilter_JPanel
				( __hbdmi );
        		JGUIUtil.addComponent(__query_input_JPanel,
				__input_filter_HydroBase_CASSLivestockStats_JPanel,
				0, y, 3, 1, 0.0, 0.0, insets, gbc.HORIZONTAL,
				gbc.EAST );
			__input_filter_JPanel_Vector.addElement (
			__input_filter_HydroBase_CASSLivestockStats_JPanel );
		}
		catch ( Exception e ) {
			// Agricultural_CASS_livestock_stats probably not in
			// HydroBase...
			Message.printWarning ( 2, routine,
			"Unable to initialize input filter for HydroBase" +
			" CASS livestock statistics - old database?" );
			Message.printWarning ( 2, routine, e );
		}

		// Add input filters for CU population data, only available for
		// newer databases.  For now, just catch an exception when not
		// supported.

		try {	__input_filter_HydroBase_CUPopulation_JPanel = new
			HydroBase_GUI_CUPopulation_InputFilter_JPanel( __hbdmi);
        		JGUIUtil.addComponent(__query_input_JPanel,
				__input_filter_HydroBase_CUPopulation_JPanel,
				0, y, 3, 1, 0.0, 0.0, insets, gbc.HORIZONTAL,
				gbc.EAST );
			__input_filter_JPanel_Vector.addElement (
			__input_filter_HydroBase_CUPopulation_JPanel );
		}
		catch ( Exception e ) {
			// CUPopulation probably not in HydroBase...
			Message.printWarning ( 2, routine,
			"Unable to initialize input filter for HydroBase" +
			" CU population data - old database?" );
			Message.printWarning ( 2, routine, e );
		}

		// Add input filters for NASS agricultural statistics, only
		// available for newer databases.  For now, just catch an
		// exception when not supported.

		try {	__input_filter_HydroBase_NASS_JPanel = new
			HydroBase_GUI_AgriculturalNASSCropStats_InputFilter_JPanel
				( __hbdmi );
        		JGUIUtil.addComponent(__query_input_JPanel,
				__input_filter_HydroBase_NASS_JPanel,
				0, y, 3, 1, 0.0, 0.0, insets, gbc.HORIZONTAL,
				gbc.EAST );
			__input_filter_JPanel_Vector.addElement (
				__input_filter_HydroBase_NASS_JPanel );
		}
		catch ( Exception e ) {
			// Agricultural_NASS_crop_stats probably not in
			// HydroBase...
			Message.printWarning ( 2, routine,
			"Unable to initialize input filter for HydroBase" +
			" agricultural_NASS_crop_stats - old database?" );
			Message.printWarning ( 2, routine, e );
		}

		// Add input filters for WIS.  For now, just catch an
		// exception when not supported.

		try {	__input_filter_HydroBase_WIS_JPanel = new
			HydroBase_GUI_SheetNameWISFormat_InputFilter_JPanel
				( __hbdmi );
        		JGUIUtil.addComponent(__query_input_JPanel,
				__input_filter_HydroBase_WIS_JPanel,
				0, y, 3, 1, 0.0, 0.0, insets, gbc.HORIZONTAL,
				gbc.EAST );
			__input_filter_JPanel_Vector.addElement (
				__input_filter_HydroBase_WIS_JPanel );
		}
		catch ( Exception e ) {
			// WIS tables probably not in HydroBase...
			Message.printWarning ( 2, routine,
			"Unable to initialize input filter for HydroBase" +
			" WIS - data tables not in database?" );
			Message.printWarning ( 2, routine, e );
		}

		try {	
			__input_filter_HydroBase_wells_JPanel =
				new HydroBase_GUI_GroundWater_InputFilter_JPanel
				(__hbdmi, null, true);
        		JGUIUtil.addComponent(__query_input_JPanel,
				__input_filter_HydroBase_wells_JPanel,
				0, y, 3, 1, 0.0, 0.0, insets, gbc.HORIZONTAL,
				gbc.EAST);
			__input_filter_JPanel_Vector.addElement(
				__input_filter_HydroBase_wells_JPanel);
		}
		catch ( Exception e ) {
			// Agricultural_NASS_crop_stats probably not in
			// HydroBase...
			Message.printWarning ( 2, routine,
			"Unable to initialize input filter for HydroBase" +
			" agricultural_NASS_crop_stats - old database?" );
			Message.printWarning ( 2, routine, e );
		}		
	}

	PropList filter_props = new PropList ( "InputFilter" );
	Vector input_filters = null;
	InputFilter filter = null;

	if ( __source_MexicoCSMN_enabled ) {
		// Add input filters using text fields...
		// Later may put this code in the MexicoCSMN package since it
		// may be used by other interfaces...
		input_filters = new Vector(2);
		Vector statenum_Vector = new Vector (32);
		statenum_Vector.addElement ( "01 - Aguascalientes" );
		statenum_Vector.addElement ( "02 - Baja California" );
		statenum_Vector.addElement ( "03 - Baja California Sur" );
		statenum_Vector.addElement ( "04 - Campeche" );
		statenum_Vector.addElement ( "05 - Coahuila" );
		statenum_Vector.addElement ( "06 - Colima" );
		statenum_Vector.addElement ( "07 - Chiapas" );
		statenum_Vector.addElement ( "08 - Chihuahua" );
		statenum_Vector.addElement ( "09 - Distrito Federal" );
		statenum_Vector.addElement ( "10 - Durango" );
		statenum_Vector.addElement ( "11 - Guanajuato" );
		statenum_Vector.addElement ( "12 - Guerrero" );
		statenum_Vector.addElement ( "13 - Hidalgo" );
		statenum_Vector.addElement ( "14 - Jalisco" );
		statenum_Vector.addElement ( "15 - Mexico" );
		statenum_Vector.addElement ( "16 - Michoacan" );
		statenum_Vector.addElement ( "17 - Morelos" );
		statenum_Vector.addElement ( "18 - Nayarit" );
		statenum_Vector.addElement ( "19 - Nuevo Leon" );
		statenum_Vector.addElement ( "20 - Oaxaca" );
		statenum_Vector.addElement ( "21 - Puebla" );
		statenum_Vector.addElement ( "22 - Queretaro" );
		statenum_Vector.addElement ( "23 - Quintana Roo" );
		statenum_Vector.addElement ( "24 - San Luis Potosi" );
		statenum_Vector.addElement ( "25 - Sinaloa" );
		statenum_Vector.addElement ( "26 - Sonora" );
		statenum_Vector.addElement ( "27 - Tabasco" );
		statenum_Vector.addElement ( "28 - Tamaulipas" );
		statenum_Vector.addElement ( "29 - Tlaxcala" );
		statenum_Vector.addElement ( "30 - Veracruz" );
		statenum_Vector.addElement ( "31 - Yucatan" );
		statenum_Vector.addElement ( "32 - Zacatecas" );
		input_filters.addElement ( new InputFilter (
			"", "",
			StringUtil.TYPE_STRING,
			null, null, true ) );	// Blank to disable filter
		input_filters.addElement ( new InputFilter (
			"Station Name", "Station Name",
			StringUtil.TYPE_STRING,
			null, null, true ) );
		filter = new InputFilter (
			"State Number", "Station Number",
			StringUtil.TYPE_INTEGER,
			statenum_Vector, statenum_Vector, true );
		filter.setTokenInfo("-",0);
		input_filters.addElement ( filter );
		filter_props.set ( "NumFilterGroups=2" );
        	JGUIUtil.addComponent(__query_input_JPanel,
			__input_filter_MexicoCSMN_JPanel =
			new InputFilter_JPanel ( input_filters, filter_props ), 
			0, y, 3, 1, 0.0, 0.0, insets, gbc.HORIZONTAL,
			gbc.EAST );
		__input_filter_MexicoCSMN_JPanel.setToolTipText (
			"<HTML>Mexico CSMN queries can be filtered" +
			"<BR>based on station data.</HTML>" );
		__input_filter_JPanel_Vector.addElement (
			__input_filter_MexicoCSMN_JPanel );
	}

	if ( __source_NWSRFS_FS5Files_enabled ) {
		// Add input filters for structure irrig_summary_ts,

		try {	__input_filter_NWSRFS_FS5Files_JPanel = new
			NWSRFS_TS_InputFilter_JPanel ();
        		JGUIUtil.addComponent(__query_input_JPanel,
				__input_filter_NWSRFS_FS5Files_JPanel,
				0, y, 3, 1, 0.0, 0.0, insets, gbc.HORIZONTAL,
				gbc.EAST );
			__input_filter_JPanel_Vector.addElement (
				__input_filter_NWSRFS_FS5Files_JPanel );
		}
		catch ( Exception e ) {
			Message.printWarning ( 2, routine,
			"Unable to initialize input filter for NWSRFS" +
			" FS5Files.");
			Message.printWarning ( 2, routine, e );
		}
	}

	// Always add a generic input filter JPanel that is shared by input
	// types that do not have filter capabilities and when database
	// connections are not set up...

       	JGUIUtil.addComponent(__query_input_JPanel,
		__input_filter_generic_JPanel = new InputFilter_JPanel (),
			0, y, 3, 1, 0.0, 0.0, insets, gbc.HORIZONTAL,
			gbc.EAST );
	__input_filter_generic_JPanel.setToolTipText (
		"<HTML>The selected input type does not support" +
			"<BR>input filters.</HTML>" );
	__input_filter_JPanel_Vector.addElement (
		__input_filter_generic_JPanel );
	// The appropriate JPanel will be set visible later based on the input
	// type that is selected.

	// Because a component is added to the original GUI, need to refresh
	// the GUI layout...

	setInputFilters();
	validate();
	repaint();

}

/**
Initialize the GUI menus.  Split out of the main initGUI method to keep the
method size under 64K.
*/
private void initGUIMenus ()
{	JMenuBar menu_bar = new JMenuBar();
	initGUIMenus_File ( menu_bar );
	initGUIMenus_Edit ( menu_bar );
	initGUIMenus_View ( menu_bar );
	initGUIMenus_Commands ( menu_bar );
	initGUIMenus_CommandsGeneral ();
	initGUIMenus_Run ( menu_bar );
	initGUIMenus_Results ( menu_bar );
	initGUIMenus_Tools ( menu_bar );
	initGUIMenus_Help ( menu_bar );
	setJMenuBar ( menu_bar );
}

/**
Initialize the GUI "Commands" menu.
*/
private void initGUIMenus_Commands ( JMenuBar menu_bar )
{	// "Commands...TS Create/Convert/Read Time Series"...

	menu_bar.add( __Commands_JMenu = new JMenu( "Commands", true ) );	

	__Commands_JMenu.add ( __Commands_CreateTimeSeries_JMenu=
		new JMenu(__Commands_CreateTimeSeries_String) );

	// Create...

	__Commands_CreateTimeSeries_JMenu.add(
		__Commands_Create_createFromList_JMenuItem =
		new SimpleJMenuItem(
		__Commands_Create_createFromList_String, this) );

	__Commands_CreateTimeSeries_JMenu.add(
		__Commands_Create_createTraces_JMenuItem = new SimpleJMenuItem(
		__Commands_Create_createTraces_String, this) );

	/* REVISIT SAM 2005-05-18 need to enable when functionality is added
	__Commands_CreateTimeSeries_JMenu.add(
		__Commands_Create_TS_average_JMenuItem = new SimpleJMenuItem(
		__Commands_Create_TS_average_String, this) );
	*/

	__Commands_CreateTimeSeries_JMenu.add(
		__Commands_Create_TS_changeInterval_JMenuItem =
		new SimpleJMenuItem(
		__Commands_Create_TS_changeInterval_String, this) );

	__Commands_CreateTimeSeries_JMenu.add(
		__Commands_Create_TS_copy_JMenuItem = new SimpleJMenuItem(
		__Commands_Create_TS_copy_String, this) );

	__Commands_CreateTimeSeries_JMenu.add(
		__Commands_Create_TS_disaggregate_JMenuItem =
		new SimpleJMenuItem(
		__Commands_Create_TS_disaggregate_String, this) );

	__Commands_CreateTimeSeries_JMenu.add(
		__Commands_Create_TS_newDayTSFromMonthAndDayTS_JMenuItem =
		new SimpleJMenuItem(
		__Commands_Create_TS_newDayTSFromMonthAndDayTS_String, this) );

	__Commands_CreateTimeSeries_JMenu.add (
		__Commands_Create_TS_newEndOfMonthTSFromDayTS_JMenuItem =
		new SimpleJMenuItem(
		__Commands_Create_TS_newEndOfMonthTSFromDayTS_String, this ) );

	__Commands_CreateTimeSeries_JMenu.add (
		__Commands_Create_TS_newStatisticYearTS_JMenuItem =
		new SimpleJMenuItem(
		__Commands_Create_TS_newStatisticYearTS_String, this ) );

	__Commands_CreateTimeSeries_JMenu.add (
		__Commands_Create_TS_newTimeSeries_JMenuItem =
		new SimpleJMenuItem(
		__Commands_Create_TS_newTimeSeries_String, this ) );

	__Commands_CreateTimeSeries_JMenu.add(
		__Commands_Create_TS_normalize_JMenuItem = new SimpleJMenuItem(
		__Commands_Create_TS_normalize_String, this ) );

	__Commands_CreateTimeSeries_JMenu.add (
		__Commands_Create_TS_relativeDiff_JMenuItem =
		new SimpleJMenuItem( 
		__Commands_Create_TS_relativeDiff_String, this ) );

	__Commands_CreateTimeSeries_JMenu.add (
		__Commands_Create_TS_weightTraces_JMenuItem =
		new SimpleJMenuItem(
		__Commands_Create_TS_weightTraces_String, this ) );

	// Convert...

	__Commands_JMenu.add ( __Commands_ConvertTSIDToReadCommand_JMenu=
		new JMenu(__Commands_ConvertTSIDToReadCommand_String) );

	__Commands_ConvertTSIDToReadCommand_JMenu.add (
		__Commands_ConvertTSIDTo_readTimeSeries_JMenuItem =
			new SimpleJMenuItem(
			__Commands_ConvertTSIDTo_readTimeSeries_String, this ));

	if ( __source_DateValue_enabled ) {
		__Commands_ConvertTSIDToReadCommand_JMenu.add (
			__Commands_ConvertTSIDTo_readDateValue_JMenuItem =
			new SimpleJMenuItem(
			__Commands_ConvertTSIDTo_readDateValue_String, this ));
	}

	if ( __source_HydroBase_enabled ) {
		__Commands_ConvertTSIDToReadCommand_JMenu.add (
			__Commands_ConvertTSIDTo_readHydroBase_JMenuItem =
			new SimpleJMenuItem(
			__Commands_ConvertTSIDTo_readHydroBase_String, this ) );
	}

	if ( __source_MODSIM_enabled ) {
		__Commands_ConvertTSIDToReadCommand_JMenu.add (
			__Commands_ConvertTSIDTo_readMODSIM_JMenuItem =
			new SimpleJMenuItem(
			__Commands_ConvertTSIDTo_readMODSIM_String, this ) );
	}

	if ( __source_NWSCard_enabled ) {
		__Commands_ConvertTSIDToReadCommand_JMenu.add (
			__Commands_ConvertTSIDTo_readNwsCard_JMenuItem =
			new SimpleJMenuItem(
			__Commands_ConvertTSIDTo_readNwsCard_String, this ) );
	}

	if ( __source_NWSRFS_FS5Files_enabled ) {
		__Commands_ConvertTSIDToReadCommand_JMenu.add (
			__Commands_ConvertTSIDTo_readNWSRFSFS5Files_JMenuItem =
			new SimpleJMenuItem(
			__Commands_ConvertTSIDTo_readNWSRFSFS5Files_String,
			this ) );
	}

	if ( __source_RiverWare_enabled ) {
		__Commands_ConvertTSIDToReadCommand_JMenu.add (
			__Commands_ConvertTSIDTo_readRiverWare_JMenuItem =
			new SimpleJMenuItem(
			__Commands_ConvertTSIDTo_readRiverWare_String, this ) );
	}

	if ( __source_StateMod_enabled ) {
		__Commands_ConvertTSIDToReadCommand_JMenu.add (
			__Commands_ConvertTSIDTo_readStateMod_JMenuItem =
			new SimpleJMenuItem(
			__Commands_ConvertTSIDTo_readStateMod_String, this ) );
	}

	if ( __source_StateMod_enabled ) {
		__Commands_ConvertTSIDToReadCommand_JMenu.add (
			__Commands_ConvertTSIDTo_readStateModB_JMenuItem =
			new SimpleJMenuItem(
			__Commands_ConvertTSIDTo_readStateModB_String, this ) );
	}

	if ( __source_StateMod_enabled ) {
		__Commands_ConvertTSIDToReadCommand_JMenu.add (
			__Commands_ConvertTSIDTo_readUsgsNwis_JMenuItem =
			new SimpleJMenuItem(
			__Commands_ConvertTSIDTo_readUsgsNwis_String, this ) );
	}

	// Read...

	__Commands_JMenu.add ( __Commands_ReadTimeSeries_JMenu=
		new JMenu(__Commands_ReadTimeSeries_String) );

	__Commands_ReadTimeSeries_JMenu.add (
		__Commands_Read_setIncludeMissingTS_JMenuItem =
		new SimpleJMenuItem(
		__Commands_Read_setIncludeMissingTS_String, this ) );

	__Commands_ReadTimeSeries_JMenu.add (
		__Commands_Read_setInputPeriod_JMenuItem=new SimpleJMenuItem(
		__Commands_Read_setInputPeriod_String, this ) );

	__Commands_ReadTimeSeries_JMenu.addSeparator ();

	if ( __source_DateValue_enabled ) {
		__Commands_ReadTimeSeries_JMenu.add(
			__Commands_Read_readDateValue_JMenuItem =
			new SimpleJMenuItem(
			__Commands_Read_readDateValue_String, this) );
	}

	if ( __source_HydroBase_enabled ) {
		__Commands_ReadTimeSeries_JMenu.add(
			__Commands_Read_readHydroBase_JMenuItem =
			new SimpleJMenuItem(
			__Commands_Read_readHydroBase_String, this) );
	}

	if ( __source_MODSIM_enabled ) {
		__Commands_ReadTimeSeries_JMenu.add(
			__Commands_Read_readMODSIM_JMenuItem =
			new SimpleJMenuItem(
			__Commands_Read_readMODSIM_String, this) );
	}

	if ( __source_NWSCard_enabled ) {
		__Commands_ReadTimeSeries_JMenu.add(
			__Commands_Read_readNwsCard_JMenuItem =
			new SimpleJMenuItem(
			__Commands_Read_readNwsCard_String, this) );
	}

	if ( __source_NWSRFS_ESPTraceEnsemble_enabled ) {
		__Commands_ReadTimeSeries_JMenu.add(
			__Commands_Read_readNWSRFSESPTraceEnsemble_JMenuItem =
			new SimpleJMenuItem(
			__Commands_Read_readNWSRFSESPTraceEnsemble_String,
			this) );
	}

	/* REVISIT SAM 2004-09-11 need to enable
	if ( __source_NWSRFS_FS5Files_enabled ) {
		__Commands_ReadTimeSeries_JMenu.add(
			__Commands_Read_readNWSRFSFS5Files_JMenuItem =
			new SimpleJMenuItem(
			__Commands_Read_readNWSRFSFS5Files_String, this) );
	}
	*/

	if ( __source_StateCU_enabled ) {
		__Commands_ReadTimeSeries_JMenu.add(
			__Commands_Read_readStateCU_JMenuItem =
			new SimpleJMenuItem(
			__Commands_Read_readStateCU_String, this) );
	}

	if ( __source_StateMod_enabled ) {
		__Commands_ReadTimeSeries_JMenu.add(
			__Commands_Read_readStateMod_JMenuItem =
			new SimpleJMenuItem(
			__Commands_Read_readStateMod_String, this) );
	}

	if ( __source_StateModB_enabled ) {
		__Commands_ReadTimeSeries_JMenu.add(
			__Commands_Read_readStateModB_JMenuItem =
			new SimpleJMenuItem(
			__Commands_Read_readStateModB_String, this) );
	}

	if ( __source_StateMod_enabled ) {
		__Commands_ReadTimeSeries_JMenu.addSeparator ();
		__Commands_ReadTimeSeries_JMenu.add(
			__Commands_Read_statemodMax_JMenuItem =
			new SimpleJMenuItem(
			__Commands_Read_statemodMax_String, this) );
	}

	__Commands_ReadTimeSeries_JMenu.addSeparator ();

	if ( __source_DateValue_enabled ) {
		__Commands_ReadTimeSeries_JMenu.add (
			__Commands_Read_TS_readDateValue_JMenuItem =
			new SimpleJMenuItem(
			__Commands_Read_TS_readDateValue_String, this) );
	}

	if ( __source_HydroBase_enabled ) {
		__Commands_ReadTimeSeries_JMenu.add (
			__Commands_Read_TS_readHydroBase_JMenuItem =
			new SimpleJMenuItem(
			__Commands_Read_TS_readHydroBase_String, this) );
	}

	if ( __source_MODSIM_enabled ) {
		__Commands_ReadTimeSeries_JMenu.add(
			__Commands_Read_TS_readMODSIM_JMenuItem =
			new SimpleJMenuItem(
			__Commands_Read_TS_readMODSIM_String, this) );
	}

	if ( __source_NDFD_enabled ) {
		__Commands_ReadTimeSeries_JMenu.add(
			__Commands_Read_TS_readNDFD_JMenuItem =
			new SimpleJMenuItem(
			__Commands_Read_TS_readNDFD_String, this) );
	}

	if ( __source_NWSCard_enabled ) {
		__Commands_ReadTimeSeries_JMenu.add(
			__Commands_Read_TS_readNwsCard_JMenuItem =
			new SimpleJMenuItem(
			__Commands_Read_TS_readNwsCard_String, this) );
	}

	if ( __source_NWSRFS_FS5Files_enabled ) {
		__Commands_ReadTimeSeries_JMenu.add(
			__Commands_Read_TS_readNWSRFSFS5Files_JMenuItem =
			new SimpleJMenuItem(
			__Commands_Read_TS_readNWSRFSFS5Files_String, this) );
	}

	if ( __source_RiverWare_enabled ) {
		__Commands_ReadTimeSeries_JMenu.add(
			__Commands_Read_TS_readRiverWare_JMenuItem =
			new SimpleJMenuItem(
			__Commands_Read_TS_readRiverWare_String, this) );
	}

	if ( __source_StateMod_enabled ) {
		__Commands_ReadTimeSeries_JMenu.add(
			__Commands_Read_TS_readStateMod_JMenuItem =
			new SimpleJMenuItem(
			__Commands_Read_TS_readStateMod_String, this) );
	}

	if ( __source_StateModB_enabled ) {
		__Commands_ReadTimeSeries_JMenu.add(
			__Commands_Read_TS_readStateModB_JMenuItem =
			new SimpleJMenuItem(
			__Commands_Read_TS_readStateModB_String, this) );
	}

	if ( __source_USGSNWIS_enabled ) {
		__Commands_ReadTimeSeries_JMenu.add(
			__Commands_Read_TS_readUsgsNwis_JMenuItem =
			new SimpleJMenuItem(
			__Commands_Read_TS_readUsgsNwis_String, this) );
	}

	// "Commands...Fill Time Series"...

	__Commands_JMenu.add ( __Commands_FillTimeSeries_JMenu=
		new JMenu(__Commands_FillTimeSeries_String));

	__Commands_FillTimeSeries_JMenu.add (
		__Commands_Fill_fillCarryForward_JMenuItem =
		new SimpleJMenuItem(
		__Commands_Fill_fillCarryForward_String, this ) );

	__Commands_FillTimeSeries_JMenu.add (
		__Commands_Fill_fillConstant_JMenuItem = new SimpleJMenuItem(
		__Commands_Fill_fillConstant_String, this ) );

	__Commands_FillTimeSeries_JMenu.add (
		__Commands_Fill_fillDayTSFrom2MonthTSAnd1DayTS_JMenuItem =
		new SimpleJMenuItem(
		__Commands_Fill_fillDayTSFrom2MonthTSAnd1DayTS_String,this) );

	__Commands_FillTimeSeries_JMenu.add (
		__Commands_Fill_fillFromTS_JMenuItem = new SimpleJMenuItem(
		__Commands_Fill_fillFromTS_String,this) );

	__Commands_FillTimeSeries_JMenu.add(
		__Commands_Fill_fillHistMonthAverage_JMenuItem =
		new SimpleJMenuItem( 
		__Commands_Fill_fillHistMonthAverage_String, this ) );

	__Commands_FillTimeSeries_JMenu.add (
		__Commands_Fill_fillHistYearAverage_JMenuItem =
		new SimpleJMenuItem( 
		__Commands_Fill_fillHistYearAverage_String, this ) );

	__Commands_FillTimeSeries_JMenu.add (
		__Commands_Fill_fillInterpolate_JMenuItem = new SimpleJMenuItem(
		__Commands_Fill_fillInterpolate_String, this ) );

	/* REVISIT SAM 2004-02-21 Comment out this menu - it may be added later.
	__Commands_FillTimeSeries_JMenu.add (
		__Commands_Fill_fillMOVE1_JMenuItem = new SimpleJMenuItem( 
		__Commands_Fill_fillMOVE1_String, this ) );
	__Commands_Fill_fillMOVE1_JMenuItem.setEnabled ( false );
	*/

	__Commands_FillTimeSeries_JMenu.add (
		__Commands_Fill_fillMixedStation_JMenuItem =new SimpleJMenuItem(
		__Commands_Fill_fillMixedStation_String, this ) );

	__Commands_FillTimeSeries_JMenu.add (
		__Commands_Fill_fillMOVE2_JMenuItem = new SimpleJMenuItem(
		__Commands_Fill_fillMOVE2_String, this ) );

	__Commands_FillTimeSeries_JMenu.add (
		__Commands_Fill_fillPattern_JMenuItem = new SimpleJMenuItem(
		__Commands_Fill_fillPattern_String, this ) );

	__Commands_FillTimeSeries_JMenu.add (
		__Commands_Fill_fillProrate_JMenuItem = new SimpleJMenuItem( 
		__Commands_Fill_fillProrate_String, this ) );

	__Commands_FillTimeSeries_JMenu.add (
		__Commands_Fill_fillRegression_JMenuItem = new SimpleJMenuItem( 
		__Commands_Fill_fillRegression_String, this ) );

	__Commands_FillTimeSeries_JMenu.add (
		__Commands_Fill_fillRepeat_JMenuItem = new SimpleJMenuItem( 
		__Commands_Fill_fillRepeat_String, this ) );

	if ( __source_HydroBase_enabled ) {
		__Commands_FillTimeSeries_JMenu.add(
			__Commands_Fill_fillUsingDiversionComments_JMenuItem =
			new SimpleJMenuItem(
			__Commands_Fill_fillUsingDiversionComments_String,
			this ) );
	}

	/* SAMX DISABLE THIS FOR now - this was an RTi freebie and needs to be
		reworked
	_JmenuTSFillWeights = new SimpleJMenuItem(
		MENU_INTERMEDIATE_FILL_WEIGHTS,
		this );
	ts_Jmenu.add( _JmenuTSFillWeights );
	*/

	__Commands_FillTimeSeries_JMenu.addSeparator();

	__Commands_FillTimeSeries_JMenu.add (
		__Commands_Fill_setAutoExtendPeriod_JMenuItem =
		new SimpleJMenuItem(
		__Commands_Fill_setAutoExtendPeriod_String, this ) );

	__Commands_FillTimeSeries_JMenu.add (
		__Commands_Fill_setAveragePeriod_JMenuItem =new SimpleJMenuItem(
		__Commands_Fill_setAveragePeriod_String, this ) );

	__Commands_FillTimeSeries_JMenu.add(
		__Commands_Fill_setIgnoreLEZero_JMenuItem = new SimpleJMenuItem(
		__Commands_Fill_setIgnoreLEZero_String, this ) );

	__Commands_FillTimeSeries_JMenu.add (
		__Commands_Fill_setMissingDataValue_JMenuItem =
		new SimpleJMenuItem(
		__Commands_Fill_setMissingDataValue_String, this ) );

	__Commands_FillTimeSeries_JMenu.add (
		__Commands_Fill_setPatternFile_JMenuItem=new SimpleJMenuItem(
		__Commands_Fill_setPatternFile_String, this ) );

	__Commands_FillTimeSeries_JMenu.add (
		__Commands_Fill_setRegressionPeriod_JMenuItem =
		new SimpleJMenuItem(
		__Commands_Fill_setRegressionPeriod_String, this ) );


	// "Commands...Set Time Series"...

	__Commands_JMenu.add ( __Commands_SetTimeSeries_JMenu=
		new JMenu(__Commands_SetTimeSeries_String));

	__Commands_SetTimeSeries_JMenu.add (
		__Commands_Set_replaceValue_JMenuItem =
		new SimpleJMenuItem( __Commands_Set_replaceValue_String, this));
	__Commands_SetTimeSeries_JMenu.addSeparator();

	__Commands_SetTimeSeries_JMenu.add (
		__Commands_Set_setConstant_JMenuItem =
		new SimpleJMenuItem( __Commands_Set_setConstant_String, this ));

	__Commands_SetTimeSeries_JMenu.add (
		__Commands_Set_setConstantBefore_JMenuItem =new SimpleJMenuItem(
		__Commands_Set_setConstantBefore_String, this) );

	__Commands_SetTimeSeries_JMenu.add (
		__Commands_Set_setDataValue_JMenuItem = new SimpleJMenuItem(
		__Commands_Set_setDataValue_String, this ) );

	__Commands_SetTimeSeries_JMenu.add (
		__Commands_Set_setFromTS_JMenuItem = new SimpleJMenuItem(
		__Commands_Set_setFromTS_String, this ) );

	__Commands_SetTimeSeries_JMenu.add (
		__Commands_Set_setMax_JMenuItem = new SimpleJMenuItem(
		__Commands_Set_setMax_String, this ) );

	// "Commands...Manipulate Time Series"...

	__Commands_JMenu.add (__Commands_ManipulateTimeSeries_JMenu=
		new JMenu("Manipulate Time Series"));

	__Commands_ManipulateTimeSeries_JMenu.add (
		__Commands_Manipulate_add_JMenuItem = new SimpleJMenuItem(
		__Commands_Manipulate_add_String,this) );

	__Commands_ManipulateTimeSeries_JMenu.add (
		__Commands_Manipulate_addConstant_JMenuItem=new SimpleJMenuItem(
		__Commands_Manipulate_addConstant_String,this) );

	__Commands_ManipulateTimeSeries_JMenu.add(
		__Commands_Manipulate_adjustExtremes_JMenuItem =
		new SimpleJMenuItem(
		__Commands_Manipulate_adjustExtremes_String, this ) );

	__Commands_ManipulateTimeSeries_JMenu.add (
		__Commands_Manipulate_ARMA_JMenuItem = new SimpleJMenuItem(
		__Commands_Manipulate_ARMA_String, this ) );

	__Commands_ManipulateTimeSeries_JMenu.add (
		__Commands_Manipulate_blend_JMenuItem = new SimpleJMenuItem(
		__Commands_Manipulate_blend_String, this ) );

	__Commands_ManipulateTimeSeries_JMenu.add (
		__Commands_Manipulate_convertDataUnits_JMenuItem =
		new SimpleJMenuItem(
		__Commands_Manipulate_convertDataUnits_String, this ) );

	__Commands_ManipulateTimeSeries_JMenu.add (
		__Commands_Manipulate_cumulate_JMenuItem = new SimpleJMenuItem(
		__Commands_Manipulate_cumulate_String, this ) );

	__Commands_ManipulateTimeSeries_JMenu.add (
		__Commands_Manipulate_divide_JMenuItem = new SimpleJMenuItem(
		__Commands_Manipulate_divide_String, this ) );

	__Commands_ManipulateTimeSeries_JMenu.add (
		__Commands_Manipulate_free_JMenuItem = new SimpleJMenuItem(
		__Commands_Manipulate_free_String, this ) );

	__Commands_ManipulateTimeSeries_JMenu.add (
		__Commands_Manipulate_multiply_JMenuItem = new SimpleJMenuItem(
		__Commands_Manipulate_multiply_String, this ) );

	__Commands_ManipulateTimeSeries_JMenu.add (
		__Commands_Manipulate_runningAverage_JMenuItem =
		new SimpleJMenuItem(
		__Commands_Manipulate_runningAverage_String, this ) );

	__Commands_ManipulateTimeSeries_JMenu.add (
		__Commands_Manipulate_scale_JMenuItem = new SimpleJMenuItem(
		__Commands_Manipulate_scale_String, this ) );

	__Commands_ManipulateTimeSeries_JMenu.add (
		__Commands_Manipulate_shiftTimeByInterval_JMenuItem=
		new SimpleJMenuItem(
		__Commands_Manipulate_shiftTimeByInterval_String, this ) );

	__Commands_ManipulateTimeSeries_JMenu.add (
		__Commands_Manipulate_subtract_JMenuItem = new SimpleJMenuItem(
		__Commands_Manipulate_subtract_String, this ) );

	// "Commands...Analyze Time Series"...

	__Commands_JMenu.add ( __Commands_AnalyzeTimeSeries_JMenu=
		new JMenu(__Commands_AnalyzeTimeSeries_String) );
	__Commands_AnalyzeTimeSeries_JMenu.add (
		__Commands_Analyze_analyzePattern_JMenuItem =
		new SimpleJMenuItem(
		__Commands_Analyze_analyzePattern_String, this ) );

	__Commands_AnalyzeTimeSeries_JMenu.add (
		__Commands_Analyze_compareTimeSeries_JMenuItem =
		new SimpleJMenuItem(
		__Commands_Analyze_compareTimeSeries_String, this ) );

	__Commands_AnalyzeTimeSeries_JMenu.addSeparator ();
	__Commands_AnalyzeTimeSeries_JMenu.add (
		__Commands_Analyze_newDataTest_JMenuItem =
		new SimpleJMenuItem(
		__Commands_Analyze_newDataTest_String, this ) );
	__Commands_AnalyzeTimeSeries_JMenu.add (
		__Commands_Analyze_readDataTestFromRiversideDB_JMenuItem =
		new SimpleJMenuItem(
		__Commands_Analyze_readDataTestFromRiversideDB_String, this ) );
	__Commands_AnalyzeTimeSeries_JMenu.add (
		__Commands_Analyze_runDataTests_JMenuItem =
		new SimpleJMenuItem(
		__Commands_Analyze_runDataTests_String, this ) );
	__Commands_AnalyzeTimeSeries_JMenu.add (
		__Commands_Analyze_processDataTestResults_JMenuItem =
		new SimpleJMenuItem(
		__Commands_Analyze_processDataTestResults_String, this ) );

	// "Commands...Models"...

	__Commands_JMenu.add ( __Commands_Models_JMenu =
		new JMenu(__Commands_Models_String) );
	__Commands_Models_JMenu.add (
		__Commands_Models_lagK_JMenuItem =
		new SimpleJMenuItem( __Commands_Models_lagK_String, this ) );

	// "Commands...Output Time Series"...

	__Commands_JMenu.add ( __Commands_OutputTimeSeries_JMenu=
		new JMenu(__Commands_OutputTimeSeries_String) );

	__Commands_OutputTimeSeries_JMenu.add (
		__Commands_Output_deselectTimeSeries_JMenuItem =
		new SimpleJMenuItem(
		__Commands_Output_deselectTimeSeries_String, this ) );
	__Commands_OutputTimeSeries_JMenu.add (
		__Commands_Output_selectTimeSeries_JMenuItem =
		new SimpleJMenuItem(
		__Commands_Output_selectTimeSeries_String, this ) );
	__Commands_OutputTimeSeries_JMenu.add (
		__Commands_Output_setOutputDetailedHeaders_JMenuItem =
		new SimpleJMenuItem(
		__Commands_Output_setOutputDetailedHeaders_String, this ) );
	__Commands_OutputTimeSeries_JMenu.add(
		__Commands_Output_setOutputPeriod_JMenuItem =
		new SimpleJMenuItem(
		__Commands_Output_setOutputPeriod_String, this ) );
	__Commands_OutputTimeSeries_JMenu.add (
		__Commands_Output_setOutputYearType_JMenuItem =
		new SimpleJMenuItem(
		__Commands_Output_setOutputYearType_String, this ) );

	__Commands_OutputTimeSeries_JMenu.addSeparator ();

	__Commands_OutputTimeSeries_JMenu.add (
		__Commands_Output_sortTimeSeries_JMenuItem =
		new SimpleJMenuItem(
		__Commands_Output_sortTimeSeries_String, this ) );

	__Commands_OutputTimeSeries_JMenu.addSeparator ();
	__Commands_OutputTimeSeries_JMenu.add (
		__Commands_Output_writeDateValue_JMenuItem =
		new SimpleJMenuItem(
		__Commands_Output_writeDateValue_String, this ) );

	if ( __source_NWSCard_enabled ) {
		__Commands_OutputTimeSeries_JMenu.add (
			__Commands_Output_writeNwsCard_JMenuItem =
			new SimpleJMenuItem(
			__Commands_Output_writeNwsCard_String, this ) );
	}

	if ( __source_NWSRFS_ESPTraceEnsemble_enabled ) {
		__Commands_OutputTimeSeries_JMenu.add (
			__Commands_Output_writeNWSRFSESPTraceEnsemble_JMenuItem=
			new SimpleJMenuItem(
			__Commands_Output_writeNWSRFSESPTraceEnsemble_String,
			this ));
	}

	if ( __source_RiverWare_enabled ) {
		__Commands_OutputTimeSeries_JMenu.add(
			__Commands_Output_writeRiverWare_JMenuItem =
			new SimpleJMenuItem(
			__Commands_Output_writeRiverWare_String, this ) );
	}

	if ( __source_StateCU_enabled ) {
		__Commands_OutputTimeSeries_JMenu.add (
		__Commands_Output_writeStateCU_JMenuItem = new SimpleJMenuItem(
		__Commands_Output_writeStateCU_String, this ) );
	}

	if ( __source_StateMod_enabled ) {
		__Commands_OutputTimeSeries_JMenu.add (
		__Commands_Output_writeStateMod_JMenuItem = new SimpleJMenuItem(
		__Commands_Output_writeStateMod_String, this ) );
	}

	__Commands_OutputTimeSeries_JMenu.add (
		__Commands_Output_writeSummary_JMenuItem = new SimpleJMenuItem(
		__Commands_Output_writeSummary_String, this ) );

	__Commands_OutputTimeSeries_JMenu.addSeparator ();

	__Commands_OutputTimeSeries_JMenu.add (
		__Commands_Output_processTSProduct_JMenuItem =
		new SimpleJMenuItem(
		__Commands_Output_processTSProduct_String, this ) );
}

/**
Initialize the GUI "Commands...General".
*/
private void initGUIMenus_CommandsGeneral ()
{	if ( __source_HydroBase_enabled ) {
		__Commands_JMenu.addSeparator();
		__Commands_JMenu.add( __Commands_HydroBase_JMenu =
			new JMenu( __Commands_HydroBase_String, true ) );	
		__Commands_HydroBase_JMenu.add (
			__Commands_HydroBase_openHydroBase_JMenuItem =
			new SimpleJMenuItem(
			__Commands_HydroBase_openHydroBase_String, this ) );
	}
	if ( __source_NDFD_enabled ) {
		__Commands_JMenu.addSeparator();
		__Commands_JMenu.add( __Commands_NDFD_JMenu =
			new JMenu( __Commands_NDFD_String, true ) );	
		__Commands_NDFD_JMenu.add (
			__Commands_NDFD_openNDFD_JMenuItem =
			new SimpleJMenuItem(
			__Commands_NDFD_openNDFD_String, this ) );
	}

	__Commands_JMenu.addSeparator();
	__Commands_JMenu.add( __Commands_General_JMenu =
		new JMenu( __Commands_General_String, true ) );	

	__Commands_General_JMenu.add(
		__Commands_General_startLog_JMenuItem =new SimpleJMenuItem(
		__Commands_General_startLog_String, this ) );
	__Commands_General_JMenu.add (
		__Commands_General_setDebugLevel_JMenuItem =new SimpleJMenuItem(
		__Commands_General_setDebugLevel_String, this ) );
	__Commands_General_JMenu.add (
		__Commands_General_setWarningLevel_JMenuItem =
		new SimpleJMenuItem(
		__Commands_General_setWarningLevel_String, this ) );

	__Commands_General_JMenu.addSeparator();
	__Commands_General_JMenu.add(
		__Commands_General_setWorkingDir_JMenuItem =new SimpleJMenuItem(
		__Commands_General_setWorkingDir_String, this ) );

	__Commands_General_JMenu.addSeparator();
	__Commands_General_JMenu.add (
		__Commands_General_comment_JMenuItem = new SimpleJMenuItem(
		__Commands_General_Comment_String, this ) );
	__Commands_General_JMenu.add (
		__Commands_General_startComment_JMenuItem = new SimpleJMenuItem(
		__Commands_General_startComment_String, this ) );
	__Commands_General_JMenu.add(
		__Commands_General_endComment_JMenuItem = new SimpleJMenuItem(
		__Commands_General_endComment_String, this ) );

	__Commands_General_JMenu.addSeparator();
	__Commands_General_JMenu.add (
		__Commands_General_exit_JMenuItem = new SimpleJMenuItem(
		__Commands_General_exit_String, this ) );
	__Commands_General_JMenu.addSeparator ();
	__Commands_General_JMenu.add (
		__Commands_General_compareFiles_JMenuItem =
		new SimpleJMenuItem(
		__Commands_General_compareFiles_String, this ) );
	__Commands_General_JMenu.add (__Commands_General_runCommands_JMenuItem =
		new SimpleJMenuItem(
		__Commands_General_runCommands_String,this));
	__Commands_General_JMenu.add ( __Commands_General_runProgram_JMenuItem =
		new SimpleJMenuItem(__Commands_General_runProgram_String,this));
}

/**
Define the popup menu for the commands area.  In some cases the words that are
shown are different from the corresponding menu because the popup mixes submenus
from different menus and also popup menus are typically more abbreviated.
*/
private void initGUIMenus_CommandsPopup ()
{	// Pop-up menu to manipulate commands...
	__Commands_JPopupMenu = new JPopupMenu("Command Actions");
	__Commands_JPopupMenu.add(
		__CommandsPopup_Edit_CommandWithErrorChecking_JMenuItem =
		new SimpleJMenuItem(
		"Edit", __Edit_CommandWithErrorChecking_String, this ) );
	__Commands_JPopupMenu.add(
		__CommandsPopup_Edit_CommandNoErrorChecking_JMenuItem =
		new SimpleJMenuItem ( "Edit (no error checks)",
		__Edit_CommandNoErrorChecking_String, this ) );
	__Commands_JPopupMenu.addSeparator();
	__Commands_JPopupMenu.add( __CommandsPopup_Cut_JMenuItem =
		new SimpleJMenuItem( "Cut", __Edit_CutCommands_String, this ) );
	__Commands_JPopupMenu.add( __CommandsPopup_Copy_JMenuItem =
		new SimpleJMenuItem ( "Copy", __Edit_CopyCommands_String,this));
	__Commands_JPopupMenu.add( __CommandsPopup_Paste_JMenuItem=
		new SimpleJMenuItem("Paste", __Edit_PasteCommands_String,this));
	__Commands_JPopupMenu.addSeparator();
	__Commands_JPopupMenu.add(__CommandsPopup_Delete_JMenuItem=
		new SimpleJMenuItem(
		"Delete", __Edit_DeleteCommands_String, this ) );
	__Commands_JPopupMenu.addSeparator();
	__Commands_JPopupMenu.add( __CommandsPopup_FindCommands_JMenuItem =
		new SimpleJMenuItem(__CommandsPopup_FindCommands_String, this));
	__Commands_JPopupMenu.add(
		__CommandsPopup_SelectAll_JMenuItem =
		new SimpleJMenuItem (
		"Select All", __Edit_SelectAllCommands_String, this ) );
	__Commands_JPopupMenu.add(
		__CommandsPopup_DeselectAll_JMenuItem =
		new SimpleJMenuItem(
		"Deselect All", __Edit_DeselectAllCommands_String, this ) );
	__Commands_JPopupMenu.add(
		__CommandsPopup_ConvertSelectedCommandsToComments_JMenuItem =
		new SimpleJMenuItem (
		__Edit_ConvertSelectedCommandsToComments_String, this ) );
	__Commands_JPopupMenu.add(
		__CommandsPopup_ConvertSelectedCommandsFromComments_JMenuItem =
		new SimpleJMenuItem (
		__Edit_ConvertSelectedCommandsFromComments_String, this ) );
	__Commands_JPopupMenu.addSeparator();
	__Commands_JPopupMenu.add(
		__CommandsPopup_Run_AllCommandsCreateOutput_JMenuItem =
		new SimpleJMenuItem (
		"Run " + __Run_AllCommandsCreateOutput_String,
		__Run_AllCommandsCreateOutput_String, this ) );
	__Commands_JPopupMenu.add(
		__CommandsPopup_Run_AllCommandsIgnoreOutput_JMenuItem =
		new SimpleJMenuItem (
		"Run " + __Run_AllCommandsIgnoreOutput_String,
		__Run_AllCommandsIgnoreOutput_String, this ) );
	__Commands_JPopupMenu.add(
		__CommandsPopup_Run_SelectedCommandsCreateOutput_JMenuItem =
		new SimpleJMenuItem (
		"Run " + __Run_SelectedCommandsCreateOutput_String,
		__Run_SelectedCommandsCreateOutput_String, this ) );
	__Commands_JPopupMenu.add(
		__CommandsPopup_Run_SelectedCommandsIgnoreOutput_JMenuItem =
		new SimpleJMenuItem (
		"Run " + __Run_SelectedCommandsIgnoreOutput_String,
		__Run_SelectedCommandsIgnoreOutput_String, this ) );
}

/**
Initialize the GUI "Edit" menu.
*/
private void initGUIMenus_Edit ( JMenuBar menu_bar )
{	JMenu __Edit_JMenu = new JMenu( "Edit", true);
	menu_bar.add( __Edit_JMenu );	

	__Edit_JMenu.add( __Edit_CutCommands_JMenuItem = new SimpleJMenuItem( 
		__Edit_CutCommands_String, this ) );
	__Edit_CutCommands_JMenuItem.setEnabled ( false );

	__Edit_JMenu.add( __Edit_CopyCommands_JMenuItem = new SimpleJMenuItem( 
		__Edit_CopyCommands_String, this ) );
	__Edit_CopyCommands_JMenuItem.setEnabled ( false );

	__Edit_JMenu.add( __Edit_PasteCommands_JMenuItem = new SimpleJMenuItem( 
		__Edit_PasteCommands_String, this ) );
	__Edit_PasteCommands_JMenuItem.setEnabled ( false );

	__Edit_JMenu.addSeparator( );

	__Edit_JMenu.add( __Edit_DeleteCommands_JMenuItem = new SimpleJMenuItem(
		__Edit_DeleteCommands_String, this) );
	__Edit_DeleteCommands_JMenuItem.setEnabled ( false );

	__Edit_JMenu.addSeparator( );

	__Edit_JMenu.add(
		__Edit_SelectAllCommands_JMenuItem = new SimpleJMenuItem( 
		__Edit_SelectAllCommands_String, this ) );
	__Edit_JMenu.add(
		__Edit_DeselectAllCommands_JMenuItem = new SimpleJMenuItem( 
		__Edit_DeselectAllCommands_String, this ) );

	__Edit_JMenu.addSeparator( );

	__Edit_JMenu.add( __Edit_CommandsFile_JMenuItem = new SimpleJMenuItem (
		__Edit_CommandsFile_String, this ) );
	__Edit_JMenu.add(
		__Edit_CommandNoErrorChecking_JMenuItem = new SimpleJMenuItem (
		__Edit_CommandNoErrorChecking_String, this ) );
	__Edit_JMenu.add(
		__Edit_CommandWithErrorChecking_JMenuItem = new SimpleJMenuItem(
		__Edit_CommandWithErrorChecking_String, this ) );
	__Edit_JMenu.addSeparator( );
	__Edit_JMenu.add(
		__Edit_ConvertSelectedCommandsToComments_JMenuItem=
		new SimpleJMenuItem (
		__Edit_ConvertSelectedCommandsToComments_String, this ) );
	__Edit_JMenu.add( __Edit_ConvertSelectedCommandsFromComments_JMenuItem =
		new SimpleJMenuItem (
		__Edit_ConvertSelectedCommandsFromComments_String, this ) );
}

/**
Initialize the GUI "File" menu.
*/
private void initGUIMenus_File ( JMenuBar menu_bar )
{	__File_JMenu = new JMenu( __File_String, true );
	menu_bar.add( __File_JMenu );

	__File_JMenu.add( __File_Open_JMenu=new JMenu(__File_Open_String,true));

	__File_Open_JMenu.add( __File_Open_CommandsFile_JMenuItem =
		new SimpleJMenuItem( __File_Open_CommandsFile_String, this ) );

	boolean separator_added = false;
	if ( __source_DIADvisor_enabled ) {
		if ( !separator_added ) {
			__File_Open_JMenu.addSeparator( );
			separator_added = true;
		}
		__File_Open_JMenu.add (
			__File_Open_DIADvisor_JMenuItem = new SimpleJMenuItem(
				__File_Open_DIADvisor_String, this ) );
	}
	if ( __source_HydroBase_enabled ) {
		if ( !separator_added ) {
			__File_Open_JMenu.addSeparator( );
			separator_added = true;
		}
		__File_Open_JMenu.add (
			__File_Open_HydroBase_JMenuItem = new SimpleJMenuItem( 
			__File_Open_HydroBase_String, this ) );
	}

	if ( __source_RiversideDB_enabled ) {
		if ( !separator_added ) {
			__File_Open_JMenu.addSeparator( );
			separator_added = true;
		}
		__File_Open_JMenu.add ( __File_Open_RiversideDB_JMenuItem =
			new SimpleJMenuItem(
			__File_Open_RiversideDB_String, this ) );
	}

	__File_JMenu.add( __File_Save_JMenu=new JMenu(__File_Save_String,true));
	//__File_Save_Commands_JMenuItem = new SimpleJMenuItem(
		//__File_Save_Commands_String,__File_Save_Commands_ActionString,
		//this );
	__File_Save_JMenu.add ( __File_Save_Commands_JMenuItem =
		new SimpleJMenuItem( __File_Save_Commands_String, this ) );
	__File_Save_JMenu.add ( __File_Save_CommandsAs_JMenuItem =
		new SimpleJMenuItem( __File_Save_CommandsAs_String, this ) );

	__File_Save_JMenu.add (
		__File_Save_TimeSeriesAs_JMenuItem = new SimpleJMenuItem(
		__File_Save_TimeSeriesAs_String, this ) );

	__File_JMenu.add( __File_Print_JMenu=
		new JMenu(__File_Print_String,true));
	__File_Print_JMenu.add ( __File_Print_Commands_JMenuItem =
		new SimpleJMenuItem( __File_Print_Commands_String,
		__File_Print_Commands_ActionString, this ) );

	__File_JMenu.addSeparator( );

	__File_JMenu.add( __File_Properties_JMenu=
		new JMenu(__File_Properties_String,true));
	if ( __source_DIADvisor_enabled ) {
		__File_Properties_JMenu.add (
			__File_Properties_DIADvisor_JMenuItem =
			new SimpleJMenuItem(
			__File_Properties_DIADvisor_String, this ) );
	}

	__File_Properties_JMenu.add(
		__File_Properties_CommandsRun_JMenuItem = new SimpleJMenuItem(
		__File_Properties_CommandsRun_String, this ) );
	__File_Properties_JMenu.add(
		__File_Properties_TSToolSession_JMenuItem = new SimpleJMenuItem(
		__File_Properties_TSToolSession_String, this ) );

	boolean seperator_added = false;

	if ( __source_ColoradoSMS_enabled ) {
		if ( !seperator_added ) {
			__File_Properties_JMenu.addSeparator ();
			seperator_added = true;
		}
		__File_Properties_JMenu.add (
			__File_Properties_ColoradoSMS_JMenuItem =
			new SimpleJMenuItem(
			__File_Properties_ColoradoSMS_String, this ) );
	}
	if ( __source_HydroBase_enabled ) {
		if ( !seperator_added ) {
			__File_Properties_JMenu.addSeparator ();
			seperator_added = true;
		}
		__File_Properties_JMenu.add (
			__File_Properties_HydroBase_JMenuItem =
			new SimpleJMenuItem(
			__File_Properties_HydroBase_String, this ) );
	}

	if ( __source_NWSRFS_FS5Files_enabled ) {
		if ( !seperator_added ) {
			__File_Properties_JMenu.addSeparator ();
			seperator_added = true;
		}
		__File_Properties_JMenu.add (
			__File_Properties_NWSRFSFS5Files_JMenuItem =
			new SimpleJMenuItem (
			__File_Properties_NWSRFSFS5Files_String, this ) );
		__File_Properties_NWSRFSFS5Files_JMenuItem.setEnabled ( false );
	}

	if ( __source_RiversideDB_enabled ) {
		if ( !seperator_added ) {
			__File_Properties_JMenu.addSeparator ();
			seperator_added = true;
		}
		__File_Properties_JMenu.add (
			__File_Properties_RiversideDB_JMenuItem =
			new SimpleJMenuItem (
			__File_Properties_RiversideDB_String, this ) );
		__File_Properties_RiversideDB_JMenuItem.setEnabled ( false );
	}

	__File_JMenu.addSeparator( );
	__File_JMenu.add ( __File_SetWorkingDirectory_JMenuItem =
		new SimpleJMenuItem( __File_SetWorkingDirectory_String, this ));

	// Add a test menu...

	String args[] = IOUtil.getProgramArguments();
	int argc = 0;
	if ( args != null ) {
		argc = args.length;
	}
	for ( int i = 0; i < argc; i++ ) {
		if ( args[i].equalsIgnoreCase("-test") ) {
			IOUtil.testing(true);
			// Add a menu for testing...
			args = null;
			__File_JMenu.add ( new SimpleJMenuItem( "Test", "Test",
				this ) );
			break;
		}
	}
	args = null;

	__File_JMenu.addSeparator( );
	__File_JMenu.add( new SimpleJMenuItem( __File_Exit_String, this ) );
}

/**
Initialize the GUI "Help" menu.
*/
private void initGUIMenus_Help ( JMenuBar menu_bar )
{	String routine = "TSTool_JFrame.initGUIMenus_Help";
	__Help_JMenu = new JMenu ( __Help_String );
	menu_bar.add ( __Help_JMenu );
	// REVISIT - not implemented by Java?
	//menu_bar.setHelpMenu ( _help_JMenu );
	__Help_JMenu.add ( new SimpleJMenuItem(__Help_AboutTSTool_String,this));
/* REVISIT SAM 2004-05-24 Help index features are not working as well now that
	documentation is huge and PDF is available.  Rely on tool tips and PDF
	and not extensive on-line help.
	__Help_JMenu.addSeparator();
	// Initialize help to run-time values...
	// Specify URL end parts for documentation home and help index file...
	// To debug the help file...
	Vector helphome = new Vector();
	if (	(__license_manager == null) ||
		__license_manager.getLicenseType().equalsIgnoreCase("CDSS") ) {
		// Default behavior is CDSS...
		// Development - do not leave uncommented for an official
		// release...
		//helphome.addElement (
		//"J:/crdss/dmi/apps/TSTool/doc/UserManual/05.03.00/html");
		helphome.addElement ( HBParse.getHome() +
				"\\doc\\TSTool\\UserManual" );
		helphome.addElement ( "http://cdss.state.co.us/manuals/TSTool");
	}
	else {	// Assume an RTi installation and look for help in RTi
		// locations (home will be something like
		// C:\Program Files\RTi\RiverTrak...
		helphome.addElement ( HBParse.getHome() +
				"\\doc\\TSTool\\UserManual" );
		helphome.addElement(
		"C:\\Program Files\\RTi\\RiverTrak\\doc\\TSTool\\UserManual" );
		helphome.addElement(
		"D:\\Program Files\\RTi\\RiverTrak\\doc\\TSTool\\UserManual" );
		// Need to get TSTool documentation on the RTi web site once all
		// the licensing issues are resolved...
	}
	URLHelp.initialize ( null, helphome, "tstool_help_index.txt" );
	helphome = null;
	// Now hook in the help system.  For some reason the code does not
	// seem to be synchronizing...
	try {	_help_index_gui = new URLHelpGUI ( 0, "TSTool Help");
		_help_index_gui.attachMainMenu ( _help_JMenu );
	}
	catch ( Exception e ) {
		Message.printWarning ( 2, routine,
		"Error setting up help system:" );
		Message.printWarning ( 2, routine, e );
	}
*/
}

/**
Initialize the GUI "Results" menu.
*/
private void initGUIMenus_Results ( JMenuBar menu_bar )
{	menu_bar.add( __Results_JMenu = new JMenu( "Results", true ) );	

	/* REVISIT SAM 2004-08-04 Add later?
	__Results_JMenu.add( __Results_Graph_AnnualTraces_JMenuItem =
		new SimpleJMenuItem(__Results_Graph_AnnualTraces_String, this));
	__Results_Graph_AnnualTraces_JMenuItem.setEnabled ( false );
	*/

	__Results_JMenu.add( __Results_Graph_BarsLeft_JMenuItem =
		new SimpleJMenuItem( __Results_Graph_BarsLeft_String, this ) );

	__Results_JMenu.add( __Results_Graph_BarsCenter_JMenuItem =
		new SimpleJMenuItem( __Results_Graph_BarsCenter_String, this ));

	__Results_JMenu.add( __Results_Graph_BarsRight_JMenuItem =
		new SimpleJMenuItem( __Results_Graph_BarsRight_String, this ));

	/* REVISIT SAM 2004-08-04 Add later?
	__Results_JMenu.add( __Results_Graph_DoubleMass_JMenuItem =
		new SimpleJMenuItem( __Results_Graph_DoubleMass_String, this));
	__Results_Graph_DoubleMass_JMenuItem.setEnabled ( false );
	*/

	__Results_JMenu.add( __Results_Graph_Duration_JMenuItem =
		new SimpleJMenuItem( __Results_Graph_Duration_String, this ));

	__Results_JMenu.add( __Results_Graph_Line_JMenuItem=new SimpleJMenuItem(
		__Results_Graph_Line_String, this ) );

	__Results_JMenu.add(
		__Results_Graph_LineLogY_JMenuItem = new SimpleJMenuItem(
		__Results_Graph_LineLogY_String, this ) );

	/* SAMX Not enabled
	_view_graph_percent_exceed_JMenuItem = new SimpleJMenuItem(
	"Percent Exceedance Curve", "GraphPercentExceed", this );
	_Results_JMenu.add( _view_graph_percent_exceed_JMenuItem );
	_view_graph_percent_exceed.setEnabled ( false );
	*/

	__Results_JMenu.add(
		__Results_Graph_PeriodOfRecord_JMenuItem = new SimpleJMenuItem(
		__Results_Graph_PeriodOfRecord_String, this ) );

	__Results_JMenu.add(__Results_Graph_Point_JMenuItem=new SimpleJMenuItem(
		__Results_Graph_Point_String, this ) );

	__Results_JMenu.add(
		__Results_Graph_PredictedValue_JMenuItem = new SimpleJMenuItem(
		__Results_Graph_PredictedValue_String, this ) );

	__Results_JMenu.add(
		__Results_Graph_PredictedValueResidual_JMenuItem =
		new SimpleJMenuItem(
		__Results_Graph_PredictedValueResidual_String, this ) );

	__Results_JMenu.add(
		__Results_Graph_XYScatter_JMenuItem = new SimpleJMenuItem(
		__Results_Graph_XYScatter_String, this ) );

	__Results_JMenu.addSeparator();

	__Results_JMenu.add( __Results_Table_JMenuItem = new SimpleJMenuItem( 
		__Results_Table_String, this ) );

	__Results_JMenu.addSeparator();

	__Results_JMenu.add(
		__Results_Report_Summary_JMenuItem = new SimpleJMenuItem( 
		__Results_Report_Summary_String, this ) );

	__Results_JMenu.addSeparator();

	__Results_JMenu.add(
		__Results_TimeSeriesProperties_JMenuItem = new SimpleJMenuItem( 
		__Results_TimeSeriesProperties_String, this ) );
}
   
/**
Define the popup menu for results.
*/
private void initGUIMenus_ResultsPopup ()
{	__results_JPopupMenu = new JPopupMenu("View Actions");
	__results_JPopupMenu.add( new SimpleJMenuItem (
		__Results_Graph_BarsLeft_String, this ) );
	__results_JPopupMenu.add( new SimpleJMenuItem (
		__Results_Graph_BarsCenter_String,this));
	__results_JPopupMenu.add( new SimpleJMenuItem (
		__Results_Graph_BarsRight_String, this ));
	__results_JPopupMenu.add( new SimpleJMenuItem (
		__Results_Graph_Duration_String, this ));
	__results_JPopupMenu.add( new SimpleJMenuItem (
		__Results_Graph_Line_String, this ) );
	__results_JPopupMenu.add( new SimpleJMenuItem (
		__Results_Graph_LineLogY_String, this ) );
	__results_JPopupMenu.add( new SimpleJMenuItem (
		__Results_Graph_PeriodOfRecord_String, this ) );
	__results_JPopupMenu.add( new SimpleJMenuItem (
		__Results_Graph_Point_String, this ) );
	__results_JPopupMenu.add( new SimpleJMenuItem (
		__Results_Graph_PredictedValue_String, this));
	__results_JPopupMenu.add( new SimpleJMenuItem (
		__Results_Graph_PredictedValueResidual_String, this));
	__results_JPopupMenu.add( new SimpleJMenuItem (
		__Results_Graph_XYScatter_String, this));
	__results_JPopupMenu.addSeparator();
	__results_JPopupMenu.add( new SimpleJMenuItem (
		__Results_Table_String, this ) );
	__results_JPopupMenu.addSeparator();
	__results_JPopupMenu.add( new SimpleJMenuItem (
		__Results_Report_Summary_String, this ) );
	__results_JPopupMenu.addSeparator();
	__results_JPopupMenu.add( new SimpleJMenuItem (
		__Results_FindTimeSeries_String, this ) );
	__results_JPopupMenu.add( new SimpleJMenuItem (
		BUTTON_TS_SELECT_ALL, this ) );
	__results_JPopupMenu.add( new SimpleJMenuItem (
		BUTTON_TS_DESELECT_ALL, this ) );
	__results_JPopupMenu.addSeparator();
	__results_JPopupMenu.add( new SimpleJMenuItem (
		__Results_TimeSeriesProperties_String, this ) );
}

/**
Initialize the GUI "Run" menu.
*/
private void initGUIMenus_Run ( JMenuBar menu_bar )
{	__Run_JMenu = new JMenu( "Run", true);
	menu_bar.add ( __Run_JMenu );	
	__Run_JMenu.add( __Run_AllCommandsCreateOutput_JMenuItem =
		new SimpleJMenuItem(__Run_AllCommandsCreateOutput_String,this));
	__Run_JMenu.add( __Run_AllCommandsIgnoreOutput_JMenuItem =
		new SimpleJMenuItem(__Run_AllCommandsIgnoreOutput_String,this));
	__Run_JMenu.add( __Run_SelectedCommandsCreateOutput_JMenuItem =
		new SimpleJMenuItem(
		__Run_SelectedCommandsCreateOutput_String,this));
	__Run_JMenu.add( __Run_SelectedCommandsIgnoreOutput_JMenuItem =
		new SimpleJMenuItem(
		__Run_SelectedCommandsIgnoreOutput_String,this));
	__Run_JMenu.add(__Run_CommandsFromFile_JMenuItem = new SimpleJMenuItem (
		__Run_CommandsFromFile_String, this ));
	__Run_JMenu.addSeparator();
	__Run_JMenu.add ( __Run_ProcessTSProductPreview_JMenuItem =
		new SimpleJMenuItem(__Run_ProcessTSProductPreview_String,this));
	__Run_JMenu.add ( __Run_ProcessTSProductOutput_JMenuItem =
		new SimpleJMenuItem(__Run_ProcessTSProductOutput_String,this));
}

/**
Initialize the GUI "Tools" menu.
*/
private void initGUIMenus_Tools ( JMenuBar menu_bar )
{	__Tools_JMenu = new JMenu();
	menu_bar.add( __Tools_JMenu = new JMenu( "Tools", true ) );	

	__Tools_JMenu.add ( __Tools_Analysis_JMenu =
		new JMenu(__Tools_Analysis_String, true ) );

	__Tools_Analysis_JMenu.add(
		__Tools_Analysis_MixedStationAnalysis_JMenuItem =
		new SimpleJMenuItem(
		__Tools_Analysis_MixedStationAnalysis_String, this ) );

	__Tools_JMenu.add ( __Tools_Report_JMenu =
		new JMenu(__Tools_Report_String, true ) );

	__Tools_Report_JMenu.add(
		__Tools_Report_DataCoverageByYear_JMenuItem=new SimpleJMenuItem(
		__Tools_Report_DataCoverageByYear_String, this ) );

	__Tools_Report_JMenu.add (
		__Tools_Report_DataLimitsSummary_JMenuItem =new SimpleJMenuItem(
		__Tools_Report_DataLimitsSummary_String, this ) );

	__Tools_Report_JMenu.add (
		__Tools_Report_MonthSummaryDailyMeans_JMenuItem =
		new SimpleJMenuItem(
		__Tools_Report_MonthSummaryDailyMeans_String, this ) );

	__Tools_Report_JMenu.add (
		__Tools_Report_MonthSummaryDailyTotals_JMenuItem =
		new SimpleJMenuItem(
		__Tools_Report_MonthSummaryDailyTotals_String, this ) );

	__Tools_Report_JMenu.add (
		__Tools_Report_YearToDateTotal_JMenuItem = new SimpleJMenuItem(
		__Tools_Report_YearToDateTotal_String, this ) );

	if (	__source_NWSRFS_FS5Files_enabled || __source_NWSCard_enabled ||
		__source_NWSRFS_ESPTraceEnsemble_enabled ) {
		// Add NWSRFS-related tools (check all above because on
		// non-UNIX system only card type may be enabled)...
		__Tools_JMenu.addSeparator ();
		__Tools_JMenu.add ( __Tools_NWSRFS_JMenu =
			new JMenu(__Tools_NWSRFS_String, true ) );

		__Tools_NWSRFS_JMenu.add(
			__Tools_NWSRFS_ConvertNWSRFSESPTraceEnsemble_JMenuItem=
			new SimpleJMenuItem(
			__Tools_NWSRFS_ConvertNWSRFSESPTraceEnsemble_String,
			this ) );
		__Tools_NWSRFS_JMenu.add(
			__Tools_NWSRFS_ConvertJulianHour_JMenuItem=
			new SimpleJMenuItem(
			__Tools_NWSRFS_ConvertJulianHour_String, this ) );
	}

	if ( __source_RiversideDB_enabled ) {
		// Add RiversideDB-related tools...
		__Tools_JMenu.addSeparator ();
		__Tools_JMenu.add ( __Tools_RiversideDB_JMenu =
			new JMenu(__Tools_RiversideDB_String, true ) );

		__Tools_RiversideDB_JMenu.add(
			__Tools_RiversideDB_TSProductManager_JMenuItem=
			new SimpleJMenuItem(
			__Tools_RiversideDB_TSProductManager_String,
			this ) );
	}

	// Options menu...

	__Tools_JMenu.addSeparator ();
	__Tools_JMenu.add ( __Tools_SelectOnMap_JMenuItem=
			new SimpleJMenuItem( __Tools_SelectOnMap_String, this));

	__Tools_JMenu.addSeparator ();
	__Tools_JMenu.add ( __Tools_Options_JMenuItem=
			new SimpleJMenuItem( __Tools_Options_String, this ) );

	// Create the diagnostics GUI, specifying the key for the help
	// button.  Events are handled from within the diagnostics GUI.
	__Tools_JMenu.addSeparator ();
	DiagnosticsJFrame diagnostics_JFrame = new DiagnosticsJFrame ();
		// REVISIT SAM 2005-04-05 some day need to enable on-line help.
		// ("TSTool.PreferencesMenu");
	diagnostics_JFrame.attachMainMenu ( __Tools_JMenu );
	Message.addMessageLogListener ( this );
}

/**
Initialize the View menu.
*/
private void initGUIMenus_View ( JMenuBar menuBar )
{	JMenu viewJMenu = new JMenu ("View", true);
	menuBar.add (viewJMenu);

	viewJMenu.add ( __View_MapInterface_JCheckBoxMenuItem =
		new JCheckBoxMenuItem(__View_MapInterface_String) );
	__View_MapInterface_JCheckBoxMenuItem.setState ( false );
	__View_MapInterface_JCheckBoxMenuItem.addItemListener ( this );
}

/**
Reset the query options choices based on the selected input name.  Other
method calls are cascaded to fully reset the choices.
*/
private void inputNameChoiceClicked()
{	String routine = "TSTool_JFrame.inputNameChoiceClicked";
	if ( __input_name_JComboBox == null ) {
		return;
	}

	__selected_input_name = __input_name_JComboBox.getSelected();

	if ( Message.isDebugOn ) {
		Message.printDebug ( 1, routine, "Input name has been " +
		"selected:  \"" + __selected_input_name + "\"" );
	}

	// List alphabetically...
	try {
	if ( __selected_input_type.equals ( __INPUT_TYPE_NWSRFS_FS5Files ) ) {
		// Reset the data types...
		__data_type_JComboBox.setEnabled ( true );
		__data_type_JComboBox.removeAll ();
		// REVISIT SAM 2004-09-01 need to find a way to not re-read the
		// data types file.
		Vector data_types = NWSRFS_Util.getTimeSeriesDataTypes (
			__nwsrfs_dmi,
			true );		// Include description
		__data_type_JComboBox.setData ( data_types );
		__data_type_JComboBox.select ( null );
		__data_type_JComboBox.select ( 0 );
	
	}
	}
	catch ( Exception e ) {
		Message.printWarning ( 2, routine, e );
	}
}

/**
Reset the query options choices based on the selected input type.  Other
method calls are cascaded to fully reset the choices.  This method also
shows/hides columns in the query results multilist to be appropriate for the
data input source.
*/
private void inputTypeChoiceClicked()
{	String routine = "TSTool_JFrame.inputTypeChoiceClicked";
	if ( __input_type_JComboBox == null ) {
		if ( Message.isDebugOn ) {
			Message.printDebug ( 1, routine, "Input type has been "+
			"selected but GUI is not yet initialized.");
		}
		return;	// Not done initializing.
	}
	__selected_input_type = __input_type_JComboBox.getSelected();
	if ( __selected_input_type == null ) {
		// Apparently this happens when setData() or similar is called
		// on the JComboBox, and before select() is called.
		if ( Message.isDebugOn ) {
			Message.printDebug ( 1, routine, "Input type has been "+
			"selected:  null (select is ignored)" );
		}
		return;
	}

	if ( Message.isDebugOn ) {
		Message.printDebug ( 1, routine, "Input type has been " +
		"selected:  \"" + __selected_input_type + "\"" );
	}

	// List alphabetically...
	try {
	if ( __selected_input_type.equals ( __INPUT_TYPE_DateValue ) ) {
		// Most information is determined from the file so set the other
		// choices to be inactive...
		__input_name_JComboBox.removeAll();
		__input_name_JComboBox.setEnabled ( false );

		__data_type_JComboBox.setEnabled ( false );
		__data_type_JComboBox.removeAll ();
		__data_type_JComboBox.add( __DATA_TYPE_AUTO );

		// Initialize with blank data vector...

		__query_TableModel = new TSTool_TS_TableModel ( null, true );
		TSTool_TS_CellRenderer cr = new TSTool_TS_CellRenderer(
			(TSTool_TS_TableModel)__query_TableModel);
		// REVISIT
		// Seems to be null at startup?
		if ( __query_JWorksheet != null ) {
			try {	__query_JWorksheet.setCellRenderer ( cr );
				__query_JWorksheet.setModel (
				__query_TableModel );
				__query_JWorksheet.setColumnWidths (
				cr.getColumnWidths() );
			}
			catch ( Exception e ) {
				// Absorb the exception in most cases - print if
				// developing to see if this issue can be
				// resolved.
				if ( Message.isDebugOn && IOUtil.testing()  ) {
					Message.printWarning ( 3, routine,
					"For developers:  caught exception in "+
					"clearQueryList JWorksheet at setup." );
					Message.printWarning ( 3, routine, e );
				}
			}
		}
	}
	else if ( __selected_input_type.equals ( __INPUT_TYPE_DIADvisor ) ) {
		__data_type_JComboBox.setEnabled ( true );
		__data_type_JComboBox.removeAll ();
		// Get the data types from the GroupDef table.  Because two
		// values may be available, append "-DataValue" and
		// "-DataValue2" to each group.
		Vector sensordef_Vector = null;
		try {	sensordef_Vector =
			__DIADvisor_dmi.readSensorDefListForDistinctGroup();
		}
		catch ( Exception e ) {
			Message.printWarning ( 2, routine, e );
			Message.printWarning ( 2, routine,
			__DIADvisor_dmi.getLastSQLString() );
		}
		// For now always list a DataValue and DataValue2, even though
		// DataValue2 may not be available - need to get more info from
		// DIAD to know how to limit...

		int size = 0;
		if ( sensordef_Vector != null ) {
			size = sensordef_Vector.size();
		}
		DIADvisor_SensorDef sensordef = null;
		for ( int i = 0; i < size; i++ ) {
			sensordef = (DIADvisor_SensorDef)
				sensordef_Vector.elementAt(i);
			__data_type_JComboBox.add(
			sensordef.getGroup() + "-DataValue" );
			__data_type_JComboBox.add(
			sensordef.getGroup() + "-DataValue2" );
		}

		// Default to first in the list...
		__data_type_JComboBox.select( null );
		__data_type_JComboBox.select( 0 );

		/* REVISIT - need to put these in input filters...
		__where_JComboBox.setEnabled ( true );
		__where_JComboBox.removeAll ();
		// SAMX Need to decide what to put here...
		__where_JComboBox.add("Sensor Description");
		__where_JComboBox.add("Sensor ID");
		__where_JComboBox.add("Sensor Rating Type");
        	__where_JComboBox.add("Sensor Type");
		__where_JComboBox.add("Site ID");
		__where_JComboBox.select ( 1 );
		*/

        	String [] width_array = new String[10];
        	String [] heading_array = new String[10];
		heading_array[__DIADvisor_COL_ID]	= "ID";
		width_array[__DIADvisor_COL_ID]		=  "75";
		heading_array[__DIADvisor_COL_NAME]	= "Name/Description";
		width_array[__DIADvisor_COL_NAME]	= "120";
		heading_array[__DIADvisor_COL_DATA_SOURCE] = "Data Source";
		width_array[__DIADvisor_COL_DATA_SOURCE]= "110";
		heading_array[__DIADvisor_COL_DATA_TYPE]= "Data Type";
		width_array[__DIADvisor_COL_DATA_TYPE]	= "110";
		heading_array[__DIADvisor_COL_TIMESTEP]	= "Time Step";
		width_array[__DIADvisor_COL_TIMESTEP]	= "70";
		heading_array[__DIADvisor_COL_SCENARIO]	= "Scenario";
		width_array[__DIADvisor_COL_SCENARIO]	= "70";
		heading_array[__DIADvisor_COL_UNITS]	= "Units";
		width_array[__DIADvisor_COL_UNITS]	= "70";
		heading_array[__DIADvisor_COL_START]	= "Start";
		width_array[__DIADvisor_COL_START]	= "50";
		heading_array[__DIADvisor_COL_END]	= "End";
		width_array[__DIADvisor_COL_END]	= "50";
		heading_array[__DIADvisor_COL_INPUT_TYPE]= "Input Type";
		width_array[__DIADvisor_COL_INPUT_TYPE]	= "85";
		// set MultiList column headings, widths and justifications
/* REVISIT
        	try {	_queryList_MultiList.clear();
			_queryList_MultiList.redraw();
			_queryList_MultiList.setHeadings( heading_array );
                	_queryList_MultiList.setColumnSizes( width_array );
                	//_list.setColumnAlignments( justify_array );
        	}
        	catch ( Exception e ) {
                	Message.printWarning( 2, routine, e );
        	}
*/
        	width_array = null;
        	heading_array = null;
	}
	else if ( __selected_input_type.equals ( __INPUT_TYPE_HydroBase )) {
		// Input name cleared and disabled...
		__input_name_JComboBox.removeAll();
		__input_name_JComboBox.setEnabled ( false );
		// Data type - get the time series choices from the
		// HydroBase_Util code...
		__data_type_JComboBox.setEnabled ( true );
		__data_type_JComboBox.removeAll ();
		Vector data_types =
			HydroBase_Util.getTimeSeriesDataTypes (__hbdmi,
			HydroBase_Util.DATA_TYPE_AGRICULTURE |
			HydroBase_Util.DATA_TYPE_DEMOGRAPHICS_ALL |
			HydroBase_Util.DATA_TYPE_HARDWARE |
			HydroBase_Util.DATA_TYPE_STATION_ALL |
			HydroBase_Util.DATA_TYPE_STRUCTURE_ALL |
			HydroBase_Util.DATA_TYPE_WIS,
			true );	// Add notes
		__data_type_JComboBox.setData ( data_types );

		// Select the default (this causes the other choices to be
		// updated)...

		__data_type_JComboBox.select( null );
		__data_type_JComboBox.select(
			HydroBase_Util.getDefaultTimeSeriesDataType(
					__hbdmi, true ) );

		// Initialize with blank data vector...

		try {	__query_TableModel = new TSTool_HydroBase_TableModel(
			__query_JWorksheet, StringUtil.atoi(__props.getValue(
			"HydroBase.WDIDLength")), null);
			TSTool_HydroBase_CellRenderer cr =
			new TSTool_HydroBase_CellRenderer(
			(TSTool_HydroBase_TableModel)__query_TableModel);
			__query_JWorksheet.setCellRenderer ( cr );
			__query_JWorksheet.setModel ( __query_TableModel );
			__query_JWorksheet.setColumnWidths (
				cr.getColumnWidths() );
		}
		catch ( Exception e ) {
			// Absorb the exception in most cases - print if
			// developing to see if this issue can be resolved.
			if ( Message.isDebugOn && IOUtil.testing()  ) {
				Message.printWarning ( 3, routine,
				"For developers:  caught exception " +
				"blanking HydroBase JWorksheet at setup." );
				Message.printWarning ( 3, routine, e );
		}
	}
	}
	else if ( __selected_input_type.equals ( __INPUT_TYPE_MEXICO_CSMN ) ) {
		// Most information is determined from the file but let the
		// user limit the time series that will be liste...
		__input_name_JComboBox.removeAll();
		__input_name_JComboBox.setEnabled ( false );

		__data_type_JComboBox.removeAll ();
		__data_type_JComboBox.add ( "EV - Evaporation" );
		__data_type_JComboBox.add ( "PP - Precipitation" );
		__data_type_JComboBox.setEnabled ( true );

		// Set the default, which cascades other settings...

		__data_type_JComboBox.select ( 0 );

		// Initialize with blank data vector...

		__query_TableModel = new TSTool_TS_TableModel(null);
		TSTool_TS_CellRenderer cr = new TSTool_TS_CellRenderer(
			(TSTool_TS_TableModel)__query_TableModel);
		__query_JWorksheet.setCellRenderer ( cr );
		__query_JWorksheet.setModel ( __query_TableModel );
		__query_JWorksheet.setColumnWidths ( cr.getColumnWidths() );
	}
	else if ( __selected_input_type.equals ( __INPUT_TYPE_MODSIM ) ) {
		// Most information is determined from the file...
		__data_type_JComboBox.setEnabled ( false );
		__data_type_JComboBox.removeAll ();
		__data_type_JComboBox.add( __DATA_TYPE_AUTO );

		// Initialize with blank data vector...

		__query_TableModel = new TSTool_TS_TableModel(null);
		TSTool_TS_CellRenderer cr = new TSTool_TS_CellRenderer(
			(TSTool_TS_TableModel)__query_TableModel);
		__query_JWorksheet.setCellRenderer ( cr );
		__query_JWorksheet.setModel ( __query_TableModel );
		// Remove columns that are not appropriate...
		__query_JWorksheet.removeColumn ( ((TSTool_TS_TableModel)
			__query_TableModel).COL_SEQUENCE );
		__query_JWorksheet.setColumnWidths ( cr.getColumnWidths() );
	}
	else if ( __selected_input_type.equals ( __INPUT_TYPE_NWSCARD ) ) {
		// Most information is determined from the file...
		__input_name_JComboBox.removeAll();
		__input_name_JComboBox.setEnabled ( false );

		__data_type_JComboBox.setEnabled ( false );
		__data_type_JComboBox.removeAll();
		__data_type_JComboBox.add( __DATA_TYPE_AUTO );

		// Initialize with blank data vector...

		__query_TableModel = new TSTool_TS_TableModel(null);
		TSTool_TS_CellRenderer cr = new TSTool_TS_CellRenderer(
			(TSTool_TS_TableModel)__query_TableModel);
		__query_JWorksheet.setCellRenderer ( cr );
		__query_JWorksheet.setModel ( __query_TableModel );
		// Remove columns that are not appropriate...
		__query_JWorksheet.removeColumn ( ((TSTool_TS_TableModel)
			__query_TableModel).COL_SEQUENCE );
		__query_JWorksheet.setColumnWidths ( cr.getColumnWidths() );
	}
	else if ( __selected_input_type.equals ( __INPUT_TYPE_NWSRFS_FS5Files)){
		// Update the input name and let the user choose Apps Defaults
		// or pick a directory...
		__ignore_ItemEvent = true;		// Do this to prevent
							// item event cascade
		__input_name_JComboBox.removeAll();
		if ( IOUtil.isUNIXMachine() ) {
			__input_name_JComboBox.add ( __PLEASE_SELECT );
			__input_name_JComboBox.add ( __USE_APPS_DEFAULTS );
			__input_name_JComboBox.select ( __PLEASE_SELECT );
		}
		__input_name_JComboBox.add ( __BROWSE );
		// If previous choices were selected, show them...
		int size = __input_name_NWSRFS_FS5Files.size();
		for ( int i = 0; i < size; i++ ) {
			__input_name_JComboBox.add ( (String)
				__input_name_NWSRFS_FS5Files.elementAt(i) );
		}
		// Try to select the current DMI (it should always work if
		// the logic is correct)...

		boolean choice_ok = false;	// Needed to force data types
						// to cascade correctly...
		if ( __nwsrfs_dmi != null ) {
			String path = __nwsrfs_dmi.getFS5FilesLocation();
			try {	__input_name_JComboBox.select ( path );
				choice_ok = true;
			}
			catch ( Exception e ) {
				Message.printWarning ( 2, routine,
				"Unable to select current NWSRFS FS5Files \"" +
				path + "\" in input names." );
			}
		}
		__input_name_JComboBox.setEnabled ( true );

		// Enable when an input name is picked...

		__data_type_JComboBox.setEnabled ( false );
		__data_type_JComboBox.removeAll ();

		// Enable when a data type is picked...

		__time_step_JComboBox.setEnabled ( false );
		__time_step_JComboBox.removeAll ();

		__ignore_ItemEvent = false;		// Item events OK again

		if (	(__input_name_JComboBox.getItemCount() == 1) &&
			(__input_name_JComboBox.getSelected().equals(
			__BROWSE))){
			// Only Browse is in the list so if the user picks, the
			// component does not generate an event because the
			// choice has not changed.
			try {	selectInputName_NWSRFS_FS5Files ( false );
			}
			catch ( Exception e ) {
				Message.printWarning ( 1, routine,
				"Error opening NWSRFS FS5Files connection." );
				Message.printWarning ( 2, routine, e );
			}
		}
		else if ( choice_ok ) {
			// The current open DMI was able to be selected above.
			// Need to force the data types to cascade...
			inputNameChoiceClicked();
		}

		// Display the NWSRFS input filters...
		setInputFilters();

		// Initialize with blank data vector...

		__query_TableModel = new TSTool_TS_TableModel(null);
		TSTool_TS_CellRenderer cr = new TSTool_TS_CellRenderer(
			(TSTool_TS_TableModel)__query_TableModel);
		__query_JWorksheet.setCellRenderer ( cr );
		__query_JWorksheet.setModel ( __query_TableModel );
		// Remove columns that are not appropriate...
		__query_JWorksheet.removeColumn ( ((TSTool_TS_TableModel)
			__query_TableModel).COL_SEQUENCE );
		__query_JWorksheet.setColumnWidths ( cr.getColumnWidths() );
	}
	else if(__selected_input_type.equals (
		__INPUT_TYPE_NWSRFS_ESPTraceEnsemble)) {
		// Most information is determined from the file...
		__input_name_JComboBox.removeAll();
		__input_name_JComboBox.setEnabled ( false );

		__data_type_JComboBox.setEnabled ( false );
		__data_type_JComboBox.removeAll ();
		__data_type_JComboBox.add( __DATA_TYPE_AUTO );

		// Initialize with blank data vector...

		__query_TableModel = new TSTool_TS_TableModel ( null, true );
		TSTool_TS_CellRenderer cr = new TSTool_TS_CellRenderer(
			(TSTool_TS_TableModel)__query_TableModel);
		__query_JWorksheet.setCellRenderer ( cr );
		__query_JWorksheet.setModel ( __query_TableModel );
		__query_JWorksheet.setColumnWidths ( cr.getColumnWidths() );
	}
	else if ( __selected_input_type.equals ( __INPUT_TYPE_RiversideDB ) ) {
		__data_type_JComboBox.setEnabled ( true );
		__data_type_JComboBox.removeAll ();
		Vector mts = null;
		Vector dts = null;
		try {	mts = __rdmi.readMeasTypeListForDistinctData_type();
			dts = __rdmi.readDataTypeList();
		}
		catch ( Exception e ) {
			Message.printWarning ( 2, routine, e );
			Message.printWarning ( 2, routine,
			__rdmi.getLastSQLString() );
			mts = null;
		}
		int size = 0;
		if ( mts != null ) {
			size = mts.size();
		}
		if ( size > 0 ) {
			String dt_description;
			RiversideDB_MeasType mt = null;
			int pos;
			String data_type;
			for ( int i = 0; i < size; i++ ) {
				mt = (RiversideDB_MeasType)mts.elementAt(i);
				pos = RiversideDB_DataType.indexOf (
					dts, mt.getData_type() );
				if ( pos < 0 ) {
					__data_type_JComboBox.add(
					mt.getData_type() );
				}
				else {	data_type = mt.getData_type() +
					" - " + ((RiversideDB_DataType)
					dts.elementAt(pos)).getDescription();
					if ( data_type.length() > 30 ) {
						__data_type_JComboBox.add(
							data_type.substring(
							0,30) + "..." );
					}
					else {	__data_type_JComboBox.add(
							data_type );
					}
				}
			}
			__data_type_JComboBox.select ( null );
			__data_type_JComboBox.select ( 0 );
		}

		// Default to first in the list...
		//__data_type_JComboBox.select( 0 );

		/* REVISIT
		__where_JComboBox.setEnabled ( true );
		__where_JComboBox.removeAll ();
		// SAMX Need to decide what to put here...
		__where_JComboBox.add("Data Source");
        	__where_JComboBox.add("Location ID");
		__where_JComboBox.add("Location Name");
		__where_JComboBox.select ( 2 );
		*/

		// Initialize with blank data vector...

		__query_TableModel = new TSTool_RiversideDB_TableModel(null);
		TSTool_RiversideDB_CellRenderer cr =
			new TSTool_RiversideDB_CellRenderer(
			(TSTool_RiversideDB_TableModel)__query_TableModel);
		__query_JWorksheet.setCellRenderer ( cr );
		__query_JWorksheet.setModel ( __query_TableModel );
		// Remove columns that are not appropriate...
		//__query_JWorksheet.removeColumn (
			//((TSTool_RiversideDB_TableModel)
			//__query_TableModel).COL_SEQUENCE );
		__query_JWorksheet.setColumnWidths ( cr.getColumnWidths() );
	}
	else if ( __selected_input_type.equals ( __INPUT_TYPE_RiverWare ) ) {
		// Most information is determined from the file...
		__input_name_JComboBox.removeAll();
		__input_name_JComboBox.setEnabled ( false );

		__data_type_JComboBox.setEnabled ( false );
		__data_type_JComboBox.removeAll();
		__data_type_JComboBox.add( __DATA_TYPE_AUTO );

		// Initialize with blank data vector...

		__query_TableModel = new TSTool_TS_TableModel(null);
		TSTool_TS_CellRenderer cr = new TSTool_TS_CellRenderer(
			(TSTool_TS_TableModel)__query_TableModel);
		__query_JWorksheet.setCellRenderer ( cr );
		__query_JWorksheet.setModel ( __query_TableModel );
		// Remove columns that are not appropriate...
		__query_JWorksheet.removeColumn ( ((TSTool_TS_TableModel)
			__query_TableModel).COL_SEQUENCE );
		__query_JWorksheet.setColumnWidths ( cr.getColumnWidths() );
	}
	else if ( __selected_input_type.equals ( __INPUT_TYPE_StateCU ) ) {
		// Prompt for a StateCU file and update choices...
		selectInputName_StateCU ( true );
	}
	else if ( __selected_input_type.equals ( __INPUT_TYPE_StateMod ) ) {
		// We disable all but the time step so the user can pick from
		// appropriate files...
		__input_name_JComboBox.removeAll();
		__input_name_JComboBox.setEnabled ( false );

		__data_type_JComboBox.setEnabled ( false );
		__data_type_JComboBox.removeAll ();
		__data_type_JComboBox.add( __DATA_TYPE_AUTO );
		__data_type_JComboBox.select ( null );
		__data_type_JComboBox.select ( 0 );

		// Initialize with blank data vector...

		__query_TableModel = new TSTool_TS_TableModel(null);
		TSTool_TS_CellRenderer cr = new TSTool_TS_CellRenderer(
			(TSTool_TS_TableModel)__query_TableModel);
		__query_JWorksheet.setCellRenderer ( cr );
		__query_JWorksheet.setModel ( __query_TableModel );
		// Turn off columns in the table model that do not apply...
		__query_JWorksheet.removeColumn (
			((TSTool_TS_TableModel)__query_TableModel).COL_ALIAS );
		__query_JWorksheet.removeColumn (
			((TSTool_TS_TableModel)
			__query_TableModel).COL_DATA_SOURCE );
		__query_JWorksheet.removeColumn (
			((TSTool_TS_TableModel)
			__query_TableModel).COL_DATA_TYPE );
		__query_JWorksheet.removeColumn (
			((TSTool_TS_TableModel)
			__query_TableModel).COL_SCENARIO);
		__query_JWorksheet.removeColumn ( ((TSTool_TS_TableModel)
			__query_TableModel).COL_SEQUENCE );
		__query_JWorksheet.setColumnWidths ( cr.getColumnWidths() );
	}
	else if ( __selected_input_type.equals ( __INPUT_TYPE_StateModB ) ) {
		// Prompt for a StateMod binary file and update choices...
		selectInputName_StateModB ( true );
	}
	else if ( __selected_input_type.equals ( __INPUT_TYPE_USGSNWIS ) ) {
		// Most information is determined from the file...
		__input_name_JComboBox.removeAll();
		__input_name_JComboBox.setEnabled ( false );

		__data_type_JComboBox.setEnabled ( false );
		__data_type_JComboBox.removeAll ();
		__data_type_JComboBox.add( __DATA_TYPE_AUTO );

		// Initialize with blank data vector...

		__query_TableModel = new TSTool_TS_TableModel(null);
		TSTool_TS_CellRenderer cr = new TSTool_TS_CellRenderer(
			(TSTool_TS_TableModel)__query_TableModel);
		__query_JWorksheet.setCellRenderer ( cr );
		__query_JWorksheet.setModel ( __query_TableModel );
		// Turn off columns in the table model that do not apply...
		__query_JWorksheet.removeColumn (
			((TSTool_TS_TableModel)__query_TableModel).COL_ALIAS );
		__query_JWorksheet.removeColumn (
			((TSTool_TS_TableModel)
			__query_TableModel).COL_DATA_SOURCE );
		__query_JWorksheet.removeColumn (
			((TSTool_TS_TableModel)
			__query_TableModel).COL_DATA_TYPE );
		__query_JWorksheet.removeColumn (
			((TSTool_TS_TableModel)
			__query_TableModel).COL_SCENARIO);
		__query_JWorksheet.removeColumn ( ((TSTool_TS_TableModel)
			__query_TableModel).COL_SEQUENCE );
		__query_JWorksheet.setColumnWidths ( cr.getColumnWidths() );
	}
	else {	// Not a recognized input source...
		Message.printWarning ( 1, routine,
		"\"" + __selected_input_type +
		"\" is not a recognized input source." );
		return;
	}

	}
	catch ( Exception e ) {
		Message.printWarning ( 2, routine, e );
	}

	routine = null;
}

/**
Insert a command in the list.  If any commands are selected, insert before the
first selected command.
SAMX - may want to change to insert in front of the LAST command selected (in
time, not position).
@param command Command to insert.
*/
private void insertCommand ( String command )
{	int selectedIndices[] = __commands_JList.getSelectedIndices();
	int selectedSize = selectedIndices.length;	
	if ( selectedSize > 0 ) {
		// Insert before the first selected command...
		__commands_JListModel.insertElementAt(command,
			selectedIndices[0]);
	}
	else {	// Insert at the end...
		__commands_JListModel.addElement(command);
	}
	setCommandsDirty ( true );
	updateStatus ();
}

/**
Determine whether an command line is a comment.
@return true if a comment line, false if not.
*/
public boolean isCommentLine ( String line )
{	if ( line == null ) {
		// Blank line...
		return true;
	}
	String s = line.trim();
	if ( line.length() == 0 ) {
		return true;
	}
	if ( line.charAt(0) == '#' ) {
		return true;
	}
	return false;
}

/**
Respond to ItemEvents.  If in the final list, the behavior is similar to
Microsoft tools:
<ol>
<li>	Click selects on item.</li>
<li>	Shift-click selects forward or backward to nearest selection.</li>
<li>	Control-click toggles one item.</li>
</ol>
*/
public void itemStateChanged ( ItemEvent evt )
{	String	routine="TSTool_JFrame.itemStateChanged";

	if ( !__gui_initialized ) {
		// Some objects are probably still null so ignore the event...
		return;
	}

	if ( __ignore_ItemEvent ) {
		// A programatic change to a list is occurring and we want to
		// ignore the event that will result...
		return;
	}

	Object	o = evt.getItemSelectable();
	int	dl = 10;

	// If any of the choices are changed, clear the main list so that the
	// choices and list are in agreement...

	try {
	// List in the order of the GUI...

	if (	(o == __input_type_JComboBox) &&
		(evt.getStateChange() == ItemEvent.SELECTED) ) {
		// New input type selected...
		clearQueryList();
		inputTypeChoiceClicked();
	}
	else if((o == __input_name_JComboBox) &&
		(evt.getStateChange() == ItemEvent.SELECTED) ) {
		// New input name selected...
		if (	__selected_input_type.equals(
			__INPUT_TYPE_NWSRFS_FS5Files) &&
			!__input_name_JComboBox.getSelected().equals(
			__PLEASE_SELECT) ) {
			// If the default "Please Select" is shown, the it is
			// initialization - don't force a selection...
			try {	selectInputName_NWSRFS_FS5Files ( false );
			}
			catch ( Exception e ) {
				Message.printWarning ( 1, routine,
				"Error opening NWSRFS FS5Files connection." );
				Message.printWarning ( 2, routine, e );
			}
		}
		else if ( __selected_input_type.equals(__INPUT_TYPE_StateCU ) ){
			selectInputName_StateCU ( false );
		}
		else if(__selected_input_type.equals(__INPUT_TYPE_StateModB )) {
			selectInputName_StateModB ( false );
		}
	}
	else if ( o == __data_type_JComboBox &&
		(evt.getStateChange() == ItemEvent.SELECTED) ) {
		clearQueryList();
		dataTypeChoiceClicked();
	}
	else if ( o == __time_step_JComboBox &&
		(evt.getStateChange() == ItemEvent.SELECTED) ) {
		clearQueryList();
		timeStepChoiceClicked();
	}
        else if ( o == __View_MapInterface_JCheckBoxMenuItem ) {
		if ( __View_MapInterface_JCheckBoxMenuItem.isSelected() ) {
			// User wants the map to be displayed...
			try {	if ( __geoview_JFrame != null ) {
					// Just set the map visible...
					__geoview_JFrame.setVisible ( true );
				}
				else {	// No existing GeoView so create one...
					__geoview_JFrame = new GeoViewJFrame (
						this, null );
					// Add a GeoViewListener so TSTool can
					// handle selects from the map...
					__geoview_JFrame.getGeoViewJPanel().
					getGeoView().addGeoViewListener(this);
					// Add a window listener so TSTool can
					// listen for when the GeoView closes...
					__geoview_JFrame.addWindowListener (
						this );
					JGUIUtil.center ( __geoview_JFrame );
				}
			}
			catch ( Exception e ) {
				Message.printWarning ( 1, "TSTool",
				"Error displaying map interface." );
				__geoview_JFrame = null;
			}
		}
		else {	// Map is deselected.  Just set the map frame to not
			// visible...
			__geoview_JFrame.setVisible ( false );
		}
	}
	checkGUIState();
	}
	catch ( Exception e ) {
		Message.printWarning ( 2, "itemStateChanged", e );
	}
}

/**
Respond to KeyEvents.  Most single-key events are handled in keyReleased to
prevent multiple events.  Do track when the shift is pressed here.
*/
public void keyPressed ( KeyEvent event )
{	int code = event.getKeyCode();

	if ( code == event.VK_SHIFT ) {
		JGUIUtil.setShiftDown ( true );
	}
	else if ( code == event.VK_CONTROL ) {
		JGUIUtil.setControlDown ( true );
	}
}

/**
Respond to KeyEvents.
*/
public void keyReleased ( KeyEvent event )
{	int code = event.getKeyCode();

	if ( code == event.VK_ENTER ) {
		if ( event.getSource() == __commands_JList ) {
			// Same as the Edit...Command event...
			actionEditCommand ( true );
		}
	}
	else if ( code == event.VK_DELETE ) {
		// Clear a command...
		if ( event.getSource() == __commands_JList ) {
			clearCommands();
		}
	}
	checkGUIState();
}

public void keyTyped ( KeyEvent event )
{
}

/**
Handle mouse clicked event.
*/
public void mouseClicked ( MouseEvent event )
{	Object source = event.getSource();
	if ( source == __commands_JList ) {
		if ( event.getClickCount() == 2 ) {
			// Same as editing with error checks...
			// Edit the first selected item, unless a comment, in
			// which case all are edited...
			actionEditCommand ( true );
		}
	}
}

/**
Handle mouse entered event.
*/
public void mouseEntered ( MouseEvent event )
{
}

/**
Handle mouse exited event.
*/
public void mouseExited ( MouseEvent event )
{
}

/**
Handle mouse pressed event.
*/
public void mousePressed ( MouseEvent event )
{	int mods = event.getModifiers();
	Component c = event.getComponent();
	// Popup for time series list...
	if (	(c == __commands_JList) && (__commands_JListModel.size() > 0) &&
		((mods & MouseEvent.BUTTON3_MASK) != 0) ) {//&&
		//event.isPopupTrigger() ) {}
		Point pt = JGUIUtil.computeOptimalPosition (
			event.getPoint(), c, __Commands_JPopupMenu );
		__Commands_JPopupMenu.show ( c, pt.x, pt.y );
	}
	// Popup for commands...
	else if ( (c == __ts_JList) && (__ts_JListModel.size() > 0) &&
		((mods & MouseEvent.BUTTON3_MASK) != 0) ) {//&&
		//event.isPopupTrigger() ) {}
		Point pt = JGUIUtil.computeOptimalPosition (
			event.getPoint(), c, __Commands_JPopupMenu );
		__results_JPopupMenu.show ( c, pt.x, pt.y );
	}
	// Popup for input name...
	else if ( (c == __input_name_JComboBox) &&
		((mods & MouseEvent.BUTTON3_MASK) != 0) &&
		__selected_input_type.equals(__INPUT_TYPE_StateModB) ) {
		Point pt = JGUIUtil.computeOptimalPosition (
			event.getPoint(), c, __input_name_JPopupMenu );
		__input_name_JPopupMenu.removeAll();
		__input_name_JPopupMenu.add( new SimpleJMenuItem (
			__InputName_BrowseStateModB_String, this ) );
		__input_name_JPopupMenu.show ( c, pt.x, pt.y );
	}
	// Check the state of the "Copy Selected to Commands" button...
	else if ( c == __query_JWorksheet ) {
		if (	(__query_JWorksheet != null) &&
			(__query_JWorksheet.getSelectedRowCount() > 0) ) {
			JGUIUtil.setEnabled (
				__CopySelectedToCommands_JButton, true );
		}
		else {	JGUIUtil.setEnabled (
			__CopySelectedToCommands_JButton, false );
		}
		// Update the border to reflect selections...
		updateStatus();
	}
}

/**
Handle mouse released event.
*/
public void mouseReleased ( MouseEvent event )
{
}

/**
Open a connection to the ColoradoSMS database.  If running in batch mode, the
CDSS configuration file is used to determine ColoradoSMS server and database
name properties to use for the initial connection.  If no configuration file
exists, then a default connection is attempted.
@param startup If true, indicates that the database connection is being made
at startup.  This is the case, for example, when multiple HydroBase databases
may be available and there is no reason to automatically connect to one of them
for all users.
*/
private void openColoradoSMS ( boolean startup )
{	String routine = "TSTool_JFrame.openColoradoSMS";
	Message.printStatus ( 1, routine, "Opening ColoradoSMS connection..." );
	// REVISIT SAM 2005-10-18
	// Always connect, whether in batch mode or not.  Might need a way to
	// configure this.
	//if ( IOUtil.isBatch() || !__show_main ) {
		// Running in batch mode or without a main GUI so automatically
		// open HydroBase from the CDSS.cfg file information...
		// Get the input needed to process the file...
		String cfg = SatMonSys_Util.getConfigurationFile();
		PropList props = null;
		if ( IOUtil.fileExists(cfg) ) {
			// Use the configuration file to get ColoradoSMS
			// properties...
			try {	props = SatMonSys_Util.readConfiguration(cfg);
			}
			catch ( Exception e ) {
				Message.printWarning ( 1, routine,
				"Error reading ColoradoSMS configuration " +
				"file \""+ cfg +
				"\".  Using defaults for ColoradoSMS." );
				Message.printWarning ( 3, routine, e );
				props = null;
			}
		}
		// Override with any TSTool command-line arguments, in
		// particular the user login...
		String propval = tstool.getPropValue("ColoradoSMS.UserLogin" );
		if ( propval != null ) {
			props.set ( "ColoradoSMS.UserLogin", propval );
			Message.printStatus ( 1, routine,
			"Using batch login ColoradoSMS.UserLogin=\"" +
			propval + "\"" );
		}
		try {	// Now open the database...
			// This uses the guest login.  If properties were not
			// found, then default ColoradoSMS information will be
			// used.
			__smsdmi = new SatMonSysDMI ( props );
			__smsdmi.open();
		}
		catch ( Exception e ) {
			Message.printWarning ( 1, routine,
			"Error opening ColoradoSMS database.  " +
			"ColoradoSMS features will be disabled." );
			Message.printWarning ( 3, routine, e );
			__smsdmi = null;
		}
	//}
	// REVISIT SAM 2005
	// Currently don't support an interactive login.
	/*
	else {	// Running interactively so prompt the user to login...
		// Display the dialog to select the database.  This is a modal
		// dialog that will not allow anything else to occur until the
		// information is entered.  Use a PropList to pass information
		// because there are a lot of parameters and the list may change
		// in the future.
	
		PropList hb_props = new PropList ( "SelectHydroBase" );
		hb_props.set ( "ValidateLogin", "false" );
		hb_props.set ( "ShowWaterDivisions", "false" );

		// Pass in the previous HydroBaseDMI so that its information
		// can be displayed as the initial values...

		SelectHydroBaseJDialog selectHydroBaseJDialog = null;
		try {	// Let the dialog check HydroBase properties in the 
			// CDSS configuration file...
			selectHydroBaseJDialog =
			new SelectHydroBaseJDialog ( this, __hbdmi, hb_props );
			// After getting to here, the dialog has been closed.
			// The HydroBaseDMI from the dialog can be retrieved and
			// used...
			__hbdmi = selectHydroBaseJDialog.getHydroBaseDMI();
			if ( __hbdmi == null ) {
				Message.printWarning ( 1, routine,
				"HydroBase features will be disabled." );
			}
		}
		catch ( Exception e ) {
			Message.printWarning ( 1, routine,
			"Error opening HydroBase connection.  " +
			"HydroBase features will be disabled." );
			Message.printWarning ( 3, routine, e );
			__smsdmi = null;
		}
	}
	*/
	// Enable/disable ColoradoSMS features as necessary...
	checkColoradoSMSFeatures();
}

/**
Open a commands file and read into the list of commands.  A check is made to
see if the list contains anything and if it does the user is prompted as to
whether need to save the previous commands.
*/
private void openCommandsFile ()
{	String routine = "TSTool_JFrame.openCommandsFile";
	// See whether the old commands need to be cleared...
	if ( __commands_dirty ) {
		if ( __commands_file_name == null ) {
			// Have not been saved before...
			int x = ResponseJDialog.NO;
			if ( __commands_JListModel.size() > 0 ) {
				x = new ResponseJDialog ( this,
				IOUtil.getProgramName(),
				"Do you want to save the changes you made?",
				ResponseJDialog.YES| ResponseJDialog.NO|
				ResponseJDialog.CANCEL).response();
			}
			if ( x == ResponseJDialog.CANCEL ) {
				return;
			}
			else if ( x == ResponseJDialog.YES ) {
				// Prompt for the name and then save...
				writeCommandsFile ( __commands_file_name, true);
			}
		}
		else {	// A commands file exists...  Warn the user.  They can
			// save to the existing file name or can cancel and
			// File...Save As... to a different name.
			// Have not been saved before...
			int x = ResponseJDialog.NO;
			if ( __commands_JListModel.size() > 0 ) {
				x = new ResponseJDialog ( this,
				IOUtil.getProgramName(),
				"Do you want to save the changes you made to:\n"
				+ "\"" + __commands_file_name + "\"?",
				ResponseJDialog.YES| ResponseJDialog.NO|
				ResponseJDialog.CANCEL).response();
			}
			if ( x == ResponseJDialog.CANCEL ) {
				return;
			}
			else if ( x == ResponseJDialog.YES ) {
				writeCommandsFile ( __commands_file_name,false);
			}
			// Else if No will clear below before opening the other
			// file...
		}
	}

	// Get the file.  Do not clear the list until the file has been chosen
	// and is readable...

	JFileChooser fc = JFileChooserFactory.createJFileChooser (
					JGUIUtil.getLastFileDialogDirectory() );
	fc.setDialogTitle("Open " + IOUtil.getProgramName() + " Commands File");
	if ( __license_manager.getLicenseType().equalsIgnoreCase("CDSS") ) {
		// Ray Bennett favorite...
		fc.addChoosableFileFilter(
		new SimpleFileFilter("cmx", "TSTool Commands File") );
	}
	SimpleFileFilter sff =
		new SimpleFileFilter("TSTool", "TSTool Commands File");
	fc.addChoosableFileFilter(sff);
	fc.setFileFilter(sff);
	if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
		// If the user approves a selection do the following...
		String directory = fc.getSelectedFile().getParent();
		String filename = fc.getSelectedFile().getName(); 
		String path = fc.getSelectedFile().getPath();

		// REVISIT - is this necessary in Swing?
		// Set the "WorkingDir" property, which will NOT contain a
		// trailing separator...
		IOUtil.setProgramWorkingDir(directory);
		JGUIUtil.setLastFileDialogDirectory(directory);
		__props.set ("WorkingDir=" + IOUtil.getProgramWorkingDir());
		__initial_working_dir = __props.getValue ( "WorkingDir" );
		FileReader fr = null;
		BufferedReader br = null;
		try {	br = new BufferedReader( new FileReader(path) );
		}
		catch ( Exception e ) {
			// Error opening the file (should not happen but maybe
			// a read permissions problem)...
			Message.printWarning ( 1, routine,
			"Error opening file \"" + path + "\"" );
			Message.printWarning ( 2, routine, e );
			return;
		}
		// Successfully have a file so now go ahead and remove
		// the list contents and update the list...
		deleteCommands ();
		setCommandsFileName(path);
		String line;
		try {	__ignore_ItemEvent = true;
			__ignore_ListSelectionEvent = true;
			while ( true ) {
				line = br.readLine();
				if ( line == null ) {
					break;
				}
				// Add the command but do not check the GUI
				// state...
				addCommand ( line, false );
			}
			setCommandsDirty(false);			
			br.close();
		}
		catch ( Exception e ) {
			Message.printWarning (1, routine,
			"Error reading from file \"" + path + "\"");
			Message.printWarning (2, routine, e );
		}
		__ignore_ItemEvent = false;
		__ignore_ListSelectionEvent = false;
	}
	// New file has been opened or there was a cancel/error and the old
	// list remains.
	updateStatus ( true );
}

/**
Create new DIADvisorDMI instances with connections to the DIADvisor operational
and archive databases.
*/
private void openDIADvisor ()
{	String routine = "TSTool_JFrame.openDIADvisor";

	// Display the dialog to select the database.  This is a modal dialog
	// that will not allow anything else to occur until the information is
	// entered.  Use a PropList to pass information because there are a
	// lot of parameters and the list may change in the future.

	PropList props = new PropList ( "SelectDIADvisor" );
	props.set ( "ValidateLogin", "false" );

	// Pass in the previous DIADvisorDMI so that the information can be
	// displayed as the initial values...

	SelectDIADvisorDialog selectDIADvisorDialog = null;
	try {	selectDIADvisorDialog =
		new SelectDIADvisorDialog ( this, __DIADvisor_dmi,
					__DIADvisor_archive_dmi, props );

		// After getting to here, the dialog has been closed.  The
		// HydroBaseDMI from the dialog can be retrieved and used...

		__DIADvisor_dmi = selectDIADvisorDialog.getDIADvisorDMI();
		__DIADvisor_archive_dmi =
			selectDIADvisorDialog.getArchiveDIADvisorDMI();
		if ( __DIADvisor_dmi == null ) {
			Message.printWarning ( 1, routine,
			"Cannot open DIADvisor operational database.\n" +
			"DIADvisor features will be disabled." );
			__DIADvisor_archive_dmi = null;
		}
		else if ( __DIADvisor_archive_dmi == null ) {
			Message.printWarning ( 1, routine,
			"Cannot open DIADvisor archive database.\n" +
			"Archive data will not be available." );
		}
	}
	catch ( Exception e ) {
		Message.printWarning ( 1, routine,
		"DIADvisor features will be disabled." );
		Message.printWarning ( 2, routine, e );
		__DIADvisor_dmi = null;
		__DIADvisor_archive_dmi = null;
	}

	checkDIADvisorFeatures ();
}

/**
Open a connection to the HydroBase database.  If running in batch mode, the
CDSS configuration file is used to determine HydroBase server and database name
properties to use for the initial connection.  If no configuration file
exists, then a default connection is attempted.
@param startup If true, indicates that the database connection is being made
at startup.  This is the case, for example, when multiple HydroBase databases
may be available and there is no reason to automatically connect to one of them
for all users.
*/
private void openHydroBase ( boolean startup )
{	String routine = "TSTool_JFrame.openHydroBase";
	Message.printStatus ( 1, routine, "Opening HydroBase connection..." );
	if ( IOUtil.isBatch() || !__show_main ) {
		// Running in batch mode or without a main GUI so automatically
		// open HydroBase from the TSTool.cfg file information...
		// Get the input needed to process the file...
		String hbcfg = HydroBase_Util.getConfigurationFile();
		PropList props = null;
		if ( IOUtil.fileExists(hbcfg) ) {
			// Use the configuration file to get HydroBase
			// properties...
			try {	props = HydroBase_Util.readConfiguration(hbcfg);
			}
			catch ( Exception e ) {
				Message.printWarning ( 1, routine,
				"Error reading HydroBase configuration file \""+
				hbcfg + "\".  Using defaults for HydroBase." );
				Message.printWarning ( 3, routine, e );
				props = null;
			}
		}
		
		try {	// Now open the database...
			// This uses the guest login.  If properties were not
			// found, then default HydroBase information will be
			// used.
			__hbdmi = new HydroBaseDMI ( props );
			__hbdmi.open();
		}
		catch ( Exception e ) {
			Message.printWarning ( 1, routine,
			"Error opening HydroBase.  " +
			"HydroBase features will be disabled." );
			Message.printWarning ( 3, routine, e );
			__hbdmi = null;
		}
	}
	else {	// Running interactively so prompt the user to login...
		// Display the dialog to select the database.  This is a modal
		// dialog that will not allow anything else to occur until the
		// information is entered.  Use a PropList to pass information
		// because there are a lot of parameters and the list may change
		// in the future.
	
		PropList hb_props = new PropList ( "SelectHydroBase" );
		hb_props.set ( "ValidateLogin", "false" );
		hb_props.set ( "ShowWaterDivisions", "false" );

		// Pass in the previous HydroBaseDMI so that its information
		// can be displayed as the initial values...

		SelectHydroBaseJDialog selectHydroBaseJDialog = null;
		try {	// Let the dialog check HydroBase properties in the 
			// CDSS configuration file...
			selectHydroBaseJDialog =
			new SelectHydroBaseJDialog ( this, __hbdmi, hb_props );
			// After getting to here, the dialog has been closed.
			// The HydroBaseDMI from the dialog can be retrieved and
			// used...
			__hbdmi = selectHydroBaseJDialog.getHydroBaseDMI();
			if ( __hbdmi == null ) {
				Message.printWarning ( 1, routine,
				"HydroBase features will be disabled." );
			}
		}
		catch ( Exception e ) {
			Message.printWarning ( 1, routine,
			"Error opening HydroBase connection.  " +
			"HydroBase features will be disabled." );
			Message.printWarning ( 3, routine, e );
			__hbdmi = null;
		}
	}
	// Enable/disable HydroBase features as necessary...
	checkHydroBaseFeatures();
}

/**
Open a connection to NWSRFS FS5Files.
@param props NWSRFS FS5Files Properties read from a TSTool configuration file.
If null, the properties will be determined from the TSTool configuration file,
if available.  This will likely be the case at startup in systems where a
connection to NWSRFS FS5Files will always be made.  If not null, the connection
is likely being defined from a TSTool configuration file.
@param startup If true, indicates that the database connection is being made
at startup.  In this case, the NWSRFS_FS5Files input type is enabled but there
may not actually be information in the TSTool.cfg file.  This is the case, for
example, when multiple NWSRFS FS5Files databases may be available and there is
no reason to automatically connect to one of them for all users.
*/
private void openNWSRFSFS5Files ( PropList props, boolean startup )
{	String routine = "TSTool_JFrame.openNWSRFSFS5Files";
	// First close the existing connection...
	if ( __nwsrfs_dmi != null ) {
		try {	__nwsrfs_dmi.close();
		}
		catch ( Exception e ) {
			// Ignore - any reason not to?
		}
	}
	try {	String	UseAppsDefaults = null,
			InputName = null;
		int needed_prop_count = 0;		// For a connection
							// method, how many
							// needed props are
							// found

		// Get the database connect information...

		if ( props == null ) {
			UseAppsDefaults = tstool.getPropValue(
				"NWSRFSFS5Files.UseAppsDefaults");
		}
		else {	UseAppsDefaults = props.getValue(
				"NWSRFSFS5Files.UseAppsDefaults");
		}

		if ( props == null ) {
			InputName = tstool.getPropValue(
				"NWSRFSFS5Files.InputName");
		}
		else {	InputName = props.getValue(
				"NWSRFSFS5Files.InputName");
		}
		
		// If no configuration information is in the config file, do
		// not open a connection here...

		if (	(UseAppsDefaults != null) &&
			(UseAppsDefaults.equalsIgnoreCase("true") ||
			(UseAppsDefaults.equalsIgnoreCase("false") &&
			(InputName != null))) ) {
			JGUIUtil.setWaitCursor ( this, true );
			if ( UseAppsDefaults.equalsIgnoreCase("true") ) {
				Message.printStatus ( 1, routine,
				"Opening connection to NWSRFS FS5Files using " +
				"Apps Defaults..." );
				__nwsrfs_dmi = new NWSRFS_DMI ();
			}
			else {	Message.printStatus ( 1, routine,
				"Opening connection to NWSRFS FS5Files using " +
				"path \"" + InputName + "\"..." );
				__nwsrfs_dmi = new NWSRFS_DMI ( InputName );
			}
			// Now open the connection...
			__nwsrfs_dmi.open();
			// Add the FS5Files name to the list known to the
			// interface.  This will allow the user to pick this
			// in the GUI later without reopening...
			String path = __nwsrfs_dmi.getFS5FilesLocation();
			__input_name_NWSRFS_FS5Files.addElement ( path );
			// Check the GUI state to enable/disable NWSRFS
			// FS5Files properties...
			checkNWSRFSFS5FilesFeatures ();
			JGUIUtil.setWaitCursor ( this, false );
		}

	}
	catch ( Exception e ) {
		Message.printWarning ( 1, routine, 
		"Unable to open NWSRFS FS5Files database. ");
		Message.printWarning( 2, routine, e );
		// Set the DMI to null so that features will be turned on but
		// still allow the NWSRFS FS5Files input type to be enabled so
		// that it can be tried again.
		__nwsrfs_dmi = null;
		JGUIUtil.setWaitCursor ( this, false );
	}
}

/**
Open a connection to the RiversideDB database.
@param props RiversideDB Properties read from a RiverTrak configuration file.
If null, the properties will be determined from the TSTool configuration file,
if available.  This will likely be the case at startup in systems where a
connection to RiversideDB will always be made.  If not null, the connection is
likely being defined from a RiverTrak configuration file.
@param startup If true, indicates that the database connection is being made
at startup.  In this case, the RiversideDB input type is enabled but there may
not actually be information in the TSTool.cfg file.  This is the case, for
example, when multiple RiversideDB databases may be available and there is no
reason to automatically connect to one of them for all users.
*/
private void openRiversideDB ( PropList props, boolean startup )
{	String routine = "TSTool_JFrame.openRiversideDB";
	// First close the existing connection...
	if ( __rdmi != null ) {
		try {	__rdmi.close();
		}
		catch ( Exception e ) {
			// Ignore - any reason not to?
		}
	}
	try {	String	connect_method = null,
			database_engine = null,
			database_server = null,
			database_name = null,
			errors = "",
			system_login = null,
			system_password = null;
		int error_count = 0;
		int needed_prop_count = 0;		// For a connection
							// method, how many
							// needed props are
							// found

		// Get the database connect method (optional - will default)...

		if ( props == null ) {
			connect_method = tstool.getPropValue(
				"RiversideDB.JavaConnectMethod");
		}
		else {	// Newer...
			connect_method = props.getValue(
				"RiversideDB.JavaConnectMethod");
		}
		if ( connect_method == null ) {
			connect_method = "JDBCODBC";
		}

		// First get the database engine (required)...

		if ( props == null ) {
			database_engine = tstool.getPropValue(
				"RiversideDB.DatabaseEngine" );
		}
		else {	database_engine = props.getValue(
				"RiversideDB.DatabaseEngine");
		}
		if ( database_engine == null ) {
			errors +=
			"\nRiversideDB.DatabaseEngine is not defined.";
			++error_count;
		}
		else {	++needed_prop_count;
		}

		// Now get the properties for the connect method...

		if ( connect_method.equalsIgnoreCase ( "JDBCODBC" ) ) {
			// Database server name (required)...
			if ( props == null ) {
				database_server = tstool.getPropValue(
					"RiversideDB.JavaDatabaseServer" );
			}
			else {	database_server = props.getValue(
					"RiversideDB.JavaDatabaseServer");
			}
			if ( database_server == null ) {
				errors += "\nRiversideDB.JavaDatabaseServer" +
					" is not defined.";
				++error_count;
			}
			else {	++needed_prop_count;
			}
		}

		// The database name is used for both "ODBC" and "JDBCODBC"
		// connect methods (required)...

		if ( props == null ) {
			database_name = tstool.getPropValue(
				"RiversideDB.JavaDatabase" );
		}
		else {	database_name = props.getValue(
				"RiversideDB.JavaDatabase" );
		}
		if ( database_name == null ) {
			errors +=
			"\nRiversideDB.JavaDatabase is not defined.";
			++error_count;
		}
		else {	++needed_prop_count;
		}

		// Now get the system login and password to use
		// (optional - will default)...

		if ( props == null ) {
			// Newer...
			system_login = tstool.getPropValue(
				"RiversideDB.SystemLogin");
			if ( system_login == null ) {
				// Older...
				system_login = tstool.getPropValue(
					"RiversideDB.Login");
			}
		}
		else {	// Newer...
			system_login=props.getValue("RiversideDB.SystemLogin");
			if ( system_login == null ) {
				// Older...
				system_login = props.getValue (
					"RiversideDB.Login");
			}
		}
		if ( props == null ) {
			// Newer...
			system_password = tstool.getPropValue(
				"RiversideDB.SystemPassword");
			if ( system_password == null ) {
				// Older...
				system_password = tstool.getPropValue(
					"RiversideDB.Password");
			}
		}
		else {	// Newer...
			system_password = props.getValue(
				"RiversideDB.SystemPassword");
			if ( system_password == null ) {
				// Older...
				system_password = props.getValue(
					"RiversideDB.Password");
			}
		}

		// Check for configuration errors...

		if ( error_count > 0 ) {
			if (	!startup ||
				(startup && (needed_prop_count > 0) ) ) {
				// A startup condition where RiversideDB
				// properties have been given that are incorrect
				// and should be corrected... OR...
				// A startup condition where RiversideDB
				// properties are not given - no error because
				// we expect the user to specify connection
				// information later.
				errors += "\n\nError in RiversideDB " +
				"configuration properties.";
				errors +=
				"\nUnable to open RiversideDB database.";
				Message.printWarning ( 1, routine, errors );
			}
			__rdmi = null;
			return;
		}

		// Construct a new DMI...

		if ( connect_method.equalsIgnoreCase("ODBC") ) {
			__rdmi = new RiversideDB_DMI ( database_engine,
					database_name, system_login,
					system_password );
		}
		else {	__rdmi = new RiversideDB_DMI ( database_engine,
					database_server, database_name,
					-1, system_login, system_password );
		}

		// Now open the connection...
		__rdmi.open();
	}
	catch ( Exception e ) {
		Message.printWarning ( 1, routine, 
		"Unable to open RiversideDB database. ");
		Message.printWarning( 2, routine, e );
		// Set the DMI to null so that features will be turned on but
		// still allow the RiversideDB input type to be enabled so that
		// it can be tried again.
		__rdmi = null;
	}

	// Check the GUI state to enable/disable RiversideDB properties...

	checkRiversideDBFeatures ();

	int login = LoginJDialog.LOGIN_EXCEEDED_MAX;
	try {
		while (login != LoginJDialog.LOGIN_CANCELLED 
		    && login != LoginJDialog.LOGIN_OK) {
			login = new LoginJDialog(this, "Riverside Login", 
				__rdmi).response();
		}
	}
	catch (Exception e) {
		Message.printWarning(2, routine, 
			"Error opening login dialog.");
		Message.printWarning(2, routine, e);
		login = LoginJDialog.LOGIN_CANCELLED;
	}

	if (login == LoginJDialog.LOGIN_CANCELLED) {
		new ResponseJDialog(this, "Login Cancelled",
			"RiversideDB login was cancelled.  Some capabilities "
			+ "(such as\nsaving and editing TSProducts) will be "
			+ "disabled.  However,\nthe connection to the "
			+ "database was still established.", 
			ResponseJDialog.OK);
	}
}

/**
Paste the cut buffer after the selected item.  Maybe there is a better way to
do this using JList features.
*/
private void pasteCommandListCutBuffer ()
{	// Default selected to zero (empty list)...
	int last_selected = -1;
	int list_size = __commands_JListModel.size();

	// Get the list of selected items...
	int [] selected_indices = __commands_JList.getSelectedIndices();
	int selectedsize = 0;
	if ( selected_indices != null ) {
		selectedsize = selected_indices.length;
	}
	if ( selectedsize > 0 ) {
		last_selected = selected_indices[selectedsize - 1];
	}
	else if ( list_size != 0 ) {
		// Nothing selected so set to last item in list...
		last_selected = list_size - 1;
	}
	// Else, nothing in list so will insert at beginning

	// Transfer the cut buffer starting at one after the last selection...

	int buffersize = __commands_cut_buffer.size();

	String string = null;
	for ( int i = 0; i < buffersize; i++ ) {
		string = (String)__commands_cut_buffer.elementAt(i);
		__commands_JListModel.insertElementAt ( string,
			(last_selected + 1 + i) );
	}

	// Leave in the buffer so it can be pasted again.

	selected_indices = null;
	string = null;
	setCommandsDirty ( true );
	updateStatus();
}

/**
Print a status message to the status bar, etc.  This method is called by the
Message class for status messages.
@param level Status message level.
@param rtn Routine name.
@param message Message to print.
*/
public void printStatusMessages ( int level, String rtn, String message )
{	if ( level <= 1 ) {
		__message_JTextField.setText ( message );
		// REVISIT
		// This does not seem to be redrawing, not matter what is done!
		//__message_JTextField.validate ();
		//__message_JTextField.repaint ();
		//validate ();
		//invalidate ();
		//repaint ();
	}
}

/**
Method to read a TSProduct file and process the product.
@param show_view If true, the graph will be displayed on the screen.  If false,
an output file will be prompted for.
*/
private void processTSProductFile ( boolean view_gui )
throws Exception
{	JFileChooser fc = JFileChooserFactory.createJFileChooser (
				JGUIUtil.getLastFileDialogDirectory() );
	fc.setDialogTitle ( "Select TS Product File" );
	SimpleFileFilter sff =
		new SimpleFileFilter ( "tsp", "Time Series Product File" );
	fc.addChoosableFileFilter ( sff );
	fc.setFileFilter ( sff );
	if ( fc.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
		// Return if no file name is selected...
		return;
	}

	String path = fc.getSelectedFile().getPath();
	String directory = fc.getSelectedFile().getParent();
	JGUIUtil.setLastFileDialogDirectory ( directory );

	PropList override_props = new PropList ("TSTool");
	DateTime now = new DateTime ( DateTime.DATE_CURRENT );
	override_props.set ( "CurrentDateTime=", now.toString() );
	if ( view_gui ) {
		override_props.set ( "InitialView", "Graph" );
		override_props.set ( "PreviewOutput", "True" );
	}
	TSProcessor p = new TSProcessor ();
	// Set up the time series supplier...
	if ( __final_ts_engine != null ) {
		// Time series should be in memory so add the TSEngine as a
		// supplier.  This should result in quick supplying of in-memory
		// time series...
		p.addTSSupplier ( __final_ts_engine );
	}
	// Always add a new TSEngine that will be able to read time series that
	// are not currently in memory.  The main trick is to set the working
	// directory.  However the working directory will be set in memory in
	// IOUtil so just declare a TSEngine.  Don't pass any commands.
	//
	// REVISIT...
	// If no working directory has been set, we could set from the file
	// selection, but how to detect when to do this?
	TSEngine supplier_tsengine = new TSEngine ( __hbdmi, __rdmi,
					__DIADvisor_dmi,
					__DIADvisor_archive_dmi,
					__nwsrfs_dmi, __smsdmi,
					null, this );
	p.addTSSupplier ( supplier_tsengine );
	p.processProduct ( path, override_props );
}

/**
Quits the program with the correct exit status
@param status
 */
public void quitProgram( int status)
{
   tstool.quitProgram(status);
}


/**
Read the list of time series from a DateValue file and list in the GUI.
*/
public void readDateValueHeaders ()
throws IOException
{	String message, routine = "TSTool_JFrame.readDateValueHeaders";

	try {	JFileChooser fc = JFileChooserFactory.createJFileChooser (
			JGUIUtil.getLastFileDialogDirectory() );
		fc.setDialogTitle ( "Select DateValue Time Series File" );
		SimpleFileFilter dv_sff = new SimpleFileFilter( "dv",
			"DateValue Time Series");
		fc.addChoosableFileFilter(dv_sff);
		SimpleFileFilter sff = new SimpleFileFilter( "txt",
			"DateValue Time Series");
		fc.addChoosableFileFilter(sff);
		fc.setFileFilter(dv_sff);
		if ( fc.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
			// Cancelled...
			return;
		}
		String directory = fc.getSelectedFile().getParent();
		String filename = fc.getSelectedFile().getName(); 
		String path = fc.getSelectedFile().getPath();
		JGUIUtil.setLastFileDialogDirectory ( directory );

		Message.printStatus ( 1, routine,
		"Reading DateValue file \"" + path + "\"" );
		JGUIUtil.setWaitCursor ( this, true );
		Vector tslist = null;
		try {	tslist = DateValueTS.readTimeSeriesList (
			path, null, null, null, false );
		}
		catch ( Exception e ) {
			message = "Error reading DateValue file.";
			Message.printWarning ( 2, routine, message );
			JGUIUtil.setWaitCursor ( this, false );
			throw new IOException ( message );
		}
		if ( tslist == null ) {
			message = "Error reading DateValue file.";
			Message.printWarning ( 2, routine, message );
			JGUIUtil.setWaitCursor ( this, false );
			throw new IOException ( message );
		}
		int nts = 0;
		if ( tslist != null ) {
			nts = tslist.size();
		}
		TS ts = null;
		// There should not be any non-null time series so use the
		// Vector size...
		int size = tslist.size();
        	if ( size == 0 ) {
			Message.printStatus ( 1, routine,
			"No DateValue TS read." );
			clearQueryList ();
        	}
        	else {	Message.printStatus ( 1, routine, ""
			+ size + " DateValue TS read." );
			// Include the alias in the display...
			__query_TableModel = new TSTool_TS_TableModel (
				tslist, true );
			TSTool_TS_CellRenderer cr = new TSTool_TS_CellRenderer(
				(TSTool_TS_TableModel)__query_TableModel);
	
			__query_JWorksheet.setCellRenderer ( cr );
			__query_JWorksheet.setModel ( __query_TableModel );
			__query_JWorksheet.setColumnWidths (
			cr.getColumnWidths(), getGraphics() );

			JGUIUtil.setWaitCursor ( this, false );
        		updateStatus ();
		}
		ts = null;
		tslist = null;
	}
	catch ( Exception e ) {
		message = "Error reading DateValue file.";
		Message.printWarning ( 2, routine, message );
		Message.printWarning ( 2, routine, e );
		JGUIUtil.setWaitCursor ( this, false );
		throw new IOException ( message );
	}
}

/**
Read DIADvisor time series and list in the GUI.
*/
private void readDIADvisorHeaders()
{	String rtn = "TSTool_JFrame.readDIADvisorHeaders";
        JGUIUtil.setWaitCursor ( this, true );
        Message.printStatus ( 1, rtn, "Please wait... retrieving data");

	try {	clearQueryList ();

		String location = "";
		String data_type = StringUtil.getToken(
			__data_type_JComboBox.getSelected()," ",0,0).trim();
		String timestep = __time_step_JComboBox.getSelected().trim();

/* SAMX - need to figure out how to do where - or do we just have choices?
        	// add where clause(s)   
		if ( getWhereClauses() == false ) {
        		JGUIUtil.setWaitCursor ( this, false );
        		Message.printStatus ( 1, rtn, 
			"Query aborted - null where string");
			return;
		}
*/

		Vector results = null;
		
		try {	results = __DIADvisor_dmi.readSensorDefList();
		}
		catch ( Exception e ) {
			results = null;
			Message.printWarning ( 1, rtn, e );
		}

		int size = 0;
		if ( results != null ) {
			size = results.size();
		}
        	if ( (results == null) || (size == 0) ) {
			Message.printStatus ( 1, rtn,
			"Query complete.  No records returned." );
        	}
        	else {	Message.printStatus ( 1, rtn, "Query complete. "
			+ size + " records returned." );
        	}

		Vector tslist_data_Vector = new Vector(size*2);
		DIADvisor_SensorDef data;
		String start_date_time = "";
		String end_date_time = "";
		int interval_mult;
		String interval = __time_step_JComboBox.getSelected();
		String interval_base = "";
		String description = "";
		// This is either "DataValue" or "DataValue2"...
		String selected_group = StringUtil.getToken(
			__data_type_JComboBox.getSelected(), "-", 0, 0);
		String datatype = StringUtil.getToken (
			__data_type_JComboBox.getSelected(), "-", 0, 1);
		String sensor_type = "";	// Used to check for Rain
		String units = "";
		String rating_type = "";
		boolean is_datavalue1 = true;
		if ( datatype.endsWith("DataValue2") ) {
			is_datavalue1 = false;
		}
		for ( int i = 0; i < size; i++ ) {
			data = (DIADvisor_SensorDef)results.elementAt(i);  
			if ( !data.getGroup().equalsIgnoreCase(selected_group)){
				// Sensor is not in the requested group...
				continue;
			}
			rating_type = data.getRatingType();
			sensor_type = data.getType();

			// Process some fields that need special formatting...

			description = data.getDescription();

			// If DataValue1 - add all records + archive
			// if DateValue2, only have in operational/archive
			// database when rating != "NONE" and interval is
			// irregular (only in DataChron)
			if (	is_datavalue1 ||
				(!is_datavalue1 &&
				sensor_type.equalsIgnoreCase("RAIN") ||
				!rating_type.equalsIgnoreCase("NONE")) ) {
				if ( is_datavalue1 ) {
					units = data.getDisplayUnits();
				}
				else {	units = data.getDisplayUnits2();
				}
				tslist_data_Vector.addElement(
						data.getSensorID() + ";"
            					+ description + ";"
            					+ ";"
            					+selected_group+"-"+datatype+";"
            					+ timestep + ";"
            					+ ";"
            					+ units + ";"
                				+ start_date_time + ";" 
                				+ end_date_time + ";" 
						+ __INPUT_TYPE_DIADvisor );
			}
			// Add for archive database only if for DataValue or
			// DateValue2 and the interval is irregular...
			if (	(__DIADvisor_archive_dmi != null) &&
				(is_datavalue1 ||
				(!is_datavalue1 && 
				sensor_type.equalsIgnoreCase("RAIN") ||
				!rating_type.equalsIgnoreCase("NONE") &&
				interval.equals(__TIMESTEP_IRREGULAR))) ) {
				// Also add an archive time series...
				tslist_data_Vector.addElement(	
						data.getSensorID() + ";"
            					+ description + ";"
            					+ ";"
            					+selected_group+"-"+datatype+";"
            					+ timestep + ";"
            					+ "archive;"
            					+ units + ";"
                				+ start_date_time + ";" 
                				+ end_date_time + ";" 
						+ __INPUT_TYPE_DIADvisor );
			}
		}

		updateStatus();
		results = null;
        	JGUIUtil.setWaitCursor ( this, false );
	}
	catch ( Exception e ) {
		// Messages elsewhere but catch so we can get the cursor
		// back...
		Message.printWarning ( 2, rtn, e );
        	JGUIUtil.setWaitCursor ( this, false );
	}
}

/**
Query HydroBase time series and list in the GUI, using the current selections.
@param grlimits If a query is being executed from the map interface, then the
limits will be non-null.
@throws Exception if there is an error.
*/
private void readHydroBaseHeaders ( GRLimits grlimits )
throws Exception
{	String message, routine = "TSTool_JFrame.readHydroBaseHeaders";

        JGUIUtil.setWaitCursor ( this, true );
        Message.printStatus ( 1, routine, "Please wait... retrieving data");

	Vector tslist = null;
	int size = 0;
	try {	
		tslist = HydroBase_Util.readTimeSeriesHeaderObjects ( __hbdmi,
				__selected_data_type, __selected_time_step,
				(InputFilter_JPanel)
				__selected_input_filter_JPanel, grlimits );
		// Make sure that size is set...
		if ( tslist != null ) {
			size = tslist.size();
		}
		// Now display the data in the worksheet...
		if ( (tslist != null) && (size > 0) ) {
       			Message.printStatus ( 1, routine, "" + size +
			" HydroBase time series read.  Displaying data..." );
			if (	HydroBase_Util.
				isAgriculturalCASSCropStatsTimeSeriesDataType (
				__hbdmi, __selected_data_type) ||
				HydroBase_Util.
				isAgriculturalNASSCropStatsTimeSeriesDataType (
				__hbdmi, __selected_data_type ) ) {
				// Data from
				// agricultural_CASS_crop_statistics or
				// agricultural_NASS_crop_statistics...
				__query_TableModel = new
					TSTool_HydroBase_Ag_TableModel (
					__query_JWorksheet, tslist,
					__selected_data_type );
				TSTool_HydroBase_Ag_CellRenderer cr = new
					TSTool_HydroBase_Ag_CellRenderer(
					(TSTool_HydroBase_Ag_TableModel)
					__query_TableModel);
				__query_JWorksheet.setCellRenderer ( cr );
				__query_JWorksheet.setModel(__query_TableModel);
				__query_JWorksheet.setColumnWidths (
					cr.getColumnWidths(), getGraphics() );
			}
			else if(HydroBase_Util.
				isAgriculturalCASSLivestockStatsTimeSeriesDataType (
				__hbdmi, __selected_data_type) ) {
				// Data from CASS livestock stats...
				__query_TableModel = new
					TSTool_HydroBase_CASSLivestockStats_TableModel (
					__query_JWorksheet, tslist,
					__selected_data_type );
				TSTool_HydroBase_CASSLivestockStats_CellRenderer cr = new
					TSTool_HydroBase_CASSLivestockStats_CellRenderer(
					(TSTool_HydroBase_CASSLivestockStats_TableModel)
					__query_TableModel);
				__query_JWorksheet.setCellRenderer ( cr );
				__query_JWorksheet.setModel(__query_TableModel);
				__query_JWorksheet.setColumnWidths (
					cr.getColumnWidths(), getGraphics() );
			}
			else if(HydroBase_Util.isCUPopulationTimeSeriesDataType(
				__hbdmi, __selected_data_type) ) {
				// Data from CUPopulation...
				__query_TableModel = new
					TSTool_HydroBase_CUPopulation_TableModel (
					__query_JWorksheet, tslist,
					__selected_data_type );
				TSTool_HydroBase_CUPopulation_CellRenderer cr =
					new
					TSTool_HydroBase_CUPopulation_CellRenderer(
					(TSTool_HydroBase_CUPopulation_TableModel)
					__query_TableModel);
				__query_JWorksheet.setCellRenderer ( cr );
				__query_JWorksheet.setModel(__query_TableModel);
				__query_JWorksheet.setColumnWidths (
					cr.getColumnWidths(), getGraphics() );
			}
			else if(HydroBase_Util.isIrrigSummaryTimeSeriesDataType(
				__hbdmi, __selected_data_type ) ) {
				// Irrig summary TS...
				__query_TableModel = new
					TSTool_HydroBase_AgGIS_TableModel (
					__query_JWorksheet, tslist,
					__selected_data_type,
					StringUtil.atoi(__props.getValue(
					"HydroBase.WDIDLength")) );
				TSTool_HydroBase_AgGIS_CellRenderer cr = new
					TSTool_HydroBase_AgGIS_CellRenderer(
					(TSTool_HydroBase_AgGIS_TableModel)
					__query_TableModel);
				__query_JWorksheet.setCellRenderer ( cr );
				__query_JWorksheet.setModel(__query_TableModel);
				__query_JWorksheet.setColumnWidths (
					cr.getColumnWidths(), getGraphics() );
			}
			else if( HydroBase_Util.isWISTimeSeriesDataType (
				__hbdmi, __selected_data_type ) ) {
				// WIS TS...
				__query_TableModel = new
					TSTool_HydroBase_WIS_TableModel (
					__query_JWorksheet, tslist,
					__selected_data_type );
				TSTool_HydroBase_WIS_CellRenderer cr = new
					TSTool_HydroBase_WIS_CellRenderer(
					(TSTool_HydroBase_WIS_TableModel)
					__query_TableModel);
				__query_JWorksheet.setCellRenderer ( cr );
				__query_JWorksheet.setModel(__query_TableModel);
				__query_JWorksheet.setColumnWidths (
					cr.getColumnWidths(), getGraphics() );
			}
			// XJTSX
			else if (__selected_data_type.equalsIgnoreCase(
				"WellLevel")) {
			if (__selected_time_step.equalsIgnoreCase("Day")) {
				__query_TableModel = new
				TSTool_HydroBase_WellLevel_Day_TableModel (
					__query_JWorksheet, StringUtil.atoi(
					__props.getValue(
					"HydroBase.WDIDLength")), tslist );
				TSTool_HydroBase_WellLevel_Day_CellRenderer cr =
				new TSTool_HydroBase_WellLevel_Day_CellRenderer(
				(TSTool_HydroBase_WellLevel_Day_TableModel)
					__query_TableModel);
				__query_JWorksheet.setCellRenderer ( cr );
				__query_JWorksheet.setModel(__query_TableModel);
				// Turn off columns in the table model that do
				// not apply...
				__query_JWorksheet.setColumnWidths (
					cr.getColumnWidths(), getGraphics() );

			}
			else {
				// original table model
				__query_TableModel = new
					TSTool_HydroBase_TableModel (
					__query_JWorksheet, StringUtil.atoi(
					__props.getValue(
					"HydroBase.WDIDLength")), tslist );
				TSTool_HydroBase_CellRenderer cr =
					new TSTool_HydroBase_CellRenderer(
					(TSTool_HydroBase_TableModel)
					__query_TableModel);
				__query_JWorksheet.setCellRenderer ( cr );
				__query_JWorksheet.setModel(__query_TableModel);
				// Turn off columns in the table model that do
				// not apply...
				__query_JWorksheet.removeColumn (
					((TSTool_HydroBase_TableModel)
					__query_TableModel).COL_ABBREV );
				__query_JWorksheet.setColumnWidths (
					cr.getColumnWidths(), getGraphics() );
			}
			}
			else {	// Stations and structures...
				__query_TableModel = new
					TSTool_HydroBase_TableModel (
					__query_JWorksheet, StringUtil.atoi(
					__props.getValue(
					"HydroBase.WDIDLength")), tslist );
				TSTool_HydroBase_CellRenderer cr =
					new TSTool_HydroBase_CellRenderer(
					(TSTool_HydroBase_TableModel)
					__query_TableModel);
				__query_JWorksheet.setCellRenderer ( cr );
				__query_JWorksheet.setModel(__query_TableModel);
				// Turn off columns in the table model that do
				// not apply...
				if (	!(tslist.elementAt(0) instanceof
					HydroBase_StationGeolocMeasType) ) {
					__query_JWorksheet.removeColumn (
					((TSTool_HydroBase_TableModel)
					__query_TableModel).COL_ABBREV );
				}
				__query_JWorksheet.setColumnWidths (
					cr.getColumnWidths(), getGraphics() );
			}
		}
		else {	Message.printStatus ( 1, routine,
			"No HydroBase time series read." );
			clearQueryList ();
       		}

       		updateStatus ();
		JGUIUtil.setWaitCursor ( this, false );
	}
	catch ( Exception e ) {
		message = "Error reading time series list from HydroBase.";
		Message.printWarning ( 2, routine, message );
		Message.printWarning ( 2, routine, e );
		JGUIUtil.setWaitCursor ( this, false );
		throw new Exception ( message );
	}
}

/**
Read the license file.  If not found, the license manager will be null and
result in an invalid license error.  The license is read before the GUI is
initialized to allow the GUI to be configured according to the license type.
For example, the on-line help will be searched for differently if an RTi
general license or a CDSS license.
*/
private void readLicense ()
{	String	license_owner, license_type, license_count, license_expires,
		license_key;
	String	routine = "TSTool_JFrame.readLicense";
	String	warning = "";

	Message.printStatus ( 1, "TSTool.checkLicense", "Checking license" );
	license_owner = tstool.getPropValue ( "TSTool.LicenseOwner" );
	if ( license_owner == null ) {
		warning += "\nLicenseOwner is not specified in the TSTool " +
			"license information.";
	}
	license_type = tstool.getPropValue ( "TSTool.LicenseType" );
	if ( license_type == null ) {
		warning += "\nLicenseType is not specified in the TSTool " +
			"license information.";
	}
	license_count = tstool.getPropValue ( "TSTool.LicenseCount" );
	if ( license_count == null ) {
		warning += "\nLicenseCount is not specified in the TSTool " +
			"license information.";
	}
	license_expires = tstool.getPropValue ( "TSTool.LicenseExpires" );
	if ( license_expires == null ) {
		warning += "\nLicenseExpires is not specified in the TSTool " +
			"license information.";
	}
	license_key = tstool.getPropValue ( "TSTool.LicenseKey" );
	if ( license_key == null ) {
		warning += "\nLicenseKey is not specified in the TSTool " +
			"license information.";
	}
	if ( warning.length() > 0 ) {
		warning += "\nTSTool will not run.";
		Message.printWarning ( 1, routine, warning );
		__license_manager = null;
		return;
	}

	try {	// The following allows null fields so check above...
		__license_manager = new LicenseManager ( "TSTool",
						license_owner,
						license_type, license_count,
						license_expires, license_key );
	}
	catch ( Exception e ) {
		__license_manager = null;
	}
}

/**
Read the list of time series from a Mexico CSMN CATALOGO.TXT file and list in
the GUI.  Because the catalog file is rather long (e.g., 5000 lines) and does
not change, it is only read once.  If necessary, later change to prompt each
time.
*/
public void readMexicoCSMNHeaders ()
throws IOException
{	String message, routine = "TSTool_JFrame.readMexicoCSMNHeaders";

	try {	Vector allts = MexicoCsmnTS.getCatalogTSList();
		if ( allts == null ) {
			// Have not read the catalog yet so read it...
			JFileChooser fc =
				JFileChooserFactory.createJFileChooser (
				JGUIUtil.getLastFileDialogDirectory() );
			fc.setDialogTitle (
				"Select a Mexico CSMN CATALOGO.TXT File" );
			SimpleFileFilter sff = new SimpleFileFilter( "txt",
				"Mexico CSMN Catalogo.txt File");
			fc.addChoosableFileFilter(sff);
			fc.setFileFilter(sff);
			if (	fc.showOpenDialog(this) !=
				JFileChooser.APPROVE_OPTION ) {
				// Cancelled...
				return;
			}
			String directory = fc.getSelectedFile().getParent();
			String filename = fc.getSelectedFile().getName(); 
			String path = fc.getSelectedFile().getPath();
			JGUIUtil.setLastFileDialogDirectory ( directory );

			Message.printStatus ( 1, routine,
			"Reading Mexico CSMN catalog file \"" + path + "\"" );
			JGUIUtil.setWaitCursor ( this, true );
			Vector tslist = null;
			try {	allts = MexicoCsmnTS.readCatalogFile ( path );
			}
			catch ( Exception e ) {
				message = "Error reading Mexico CSMN catalog " +
					"file \"" + path + "\"";
				Message.printWarning ( 2, routine, message );
				Message.printWarning ( 2, routine, e );
				JGUIUtil.setWaitCursor ( this, false );
				throw new IOException ( message );
			}
		}
		if ( allts == null ) {
			message = "Error reading Mexico CSMN catalog " +
					"file - no time series.";
			Message.printWarning ( 2, routine, message );
			JGUIUtil.setWaitCursor ( this, false );
			throw new IOException ( message );
		}
		// Now limit the list of time series according to the where
		// criteria...
		// There should not be any non-null time series so use the
		// Vector size...
		int nallts = allts.size();
		Message.printStatus ( 1, routine,
		"Read " + nallts + " time series listings from catalog file." );
		TS ts;
		Vector tslist = null;
		// Limit the output to the matching information - there are 2
		// filter groups...
		InputFilter filter =
			__input_filter_MexicoCSMN_JPanel.getInputFilter(0);
		InputFilter station_filter = null;
		String where1 = filter.getWhereLabel();
		String input1 = filter.getInput(false).trim();
		filter = __input_filter_MexicoCSMN_JPanel.getInputFilter(1);
		String where2 = filter.getWhereLabel();
		String input2 = filter.getInput(false).trim();
		String station_operator = null, station = null, state = null;
		if ( where1.equals("Station Name") ) {
			station = input1;
			station_filter =
			__input_filter_MexicoCSMN_JPanel.getInputFilter(0);
			station_operator = 
			__input_filter_MexicoCSMN_JPanel.getOperator(0);
		}
		else if ( where2.equals("Station Name") ) {
			station = input2;
			station_filter =
			__input_filter_MexicoCSMN_JPanel.getInputFilter(1);
			station_operator = 
			__input_filter_MexicoCSMN_JPanel.getOperator(1);
		}
		if ( where1.equals("State Number") ) {
			state = input1;
		}
		else if ( where2.equals("State Number") ) {
			state = input2;
		}
		String prefix = null;	// State padded with zeroes
		if ( state != null ) {
			prefix = StringUtil.formatString(
			StringUtil.atoi( state),"%05d");
		}
		// Limit time series based on the station name...
		tslist = new Vector(100);
		int state_num;	// State number for specific time series.
		File f;		// Used to update file name for time series file
		for ( int i = 0; i < nallts; i++ ) {
			ts = (TS)allts.elementAt(i);
			if ( Message.isDebugOn ) {
			Message.printStatus ( 1, "",
				"station=" + station +
				" station_operator=" + station_operator +
				" state=" + state +
				" location=" + ts.getLocation() +
				" prefix=" + prefix  +
				" matchresult=" +
				station_filter.matches ( station,
				station_operator, true ) );
			}
			if (	((station == null) ||
				station_filter.matches ( ts.getDescription(),
				station_operator, true )) &&
				((state == null) ||
				ts.getLocation().startsWith(prefix)) ) {
				// OK to add the time series to the output
				// list (reset some information first)...
				ts.setDataType ( __selected_data_type );
				state_num = StringUtil.atoi(
					ts.getLocation().substring(0,5) );
				f = new File(ts.getIdentifier().getInputName());
				ts.getIdentifier().setInputName (
					f.getParent() + File.separator +
					MexicoCsmnTS.getStateAbbreviation(
					state_num) + "_" +
					__selected_data_type + ".CSV" );
				tslist.addElement ( ts );
			}
		}
		// There should not be any non-null time series so use the
		// Vector size...
		int size = tslist.size();
        	if ( size == 0 ) {
			Message.printStatus ( 1, routine,
			"No Mexico CSMN TS read." );
			clearQueryList ();
        	}
        	else {	Message.printStatus ( 1, routine, ""
			+ size + " Mexico CSMN TS read." );
			__query_TableModel = new TSTool_TS_TableModel (tslist );
			TSTool_TS_CellRenderer cr = new TSTool_TS_CellRenderer(
				(TSTool_TS_TableModel)__query_TableModel);
	
			__query_JWorksheet.setCellRenderer ( cr );
			__query_JWorksheet.setModel ( __query_TableModel );
			// Do not include the alias in the display...
			__query_JWorksheet.removeColumn (
			((TSTool_TS_TableModel)__query_TableModel).COL_ALIAS );
			__query_JWorksheet.setColumnWidths (
			cr.getColumnWidths(), getGraphics() );

        		updateStatus ();
		}
		JGUIUtil.setWaitCursor ( this, false );
		allts = null;
		tslist = null;
		ts = null;
	}
	catch ( Exception e ) {
		message = "Error reading Mexico CSMN catalog file.";
		Message.printWarning ( 2, routine, message );
		Message.printWarning ( 2, routine, e );
		JGUIUtil.setWaitCursor ( this, false );
		throw new IOException ( message );
	}
}

/**
Read the list of time series from a MODSIM file and list in the GUI.
*/
public void readMODSIMHeaders ()
throws IOException
{	String message, routine = "TSTool_JFrame.readMODSIMHeaders";

	try {	JFileChooser fc = JFileChooserFactory.createJFileChooser (
			JGUIUtil.getLastFileDialogDirectory() );
		fc.setDialogTitle ( "Select MODSIM Output File" );
		SimpleFileFilter sff = new SimpleFileFilter( "DEM",
			"Demand Time Series");
		fc.addChoosableFileFilter(sff);
		sff = new SimpleFileFilter( "FLO", "Flow Time Series");
		fc.addChoosableFileFilter(sff);
		sff = new SimpleFileFilter( "GW", "Groundwater Series");
		fc.addChoosableFileFilter(sff);
		sff = new SimpleFileFilter( "RES", "Reservoir Time Series");
		fc.addChoosableFileFilter(sff);
		if ( fc.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
			// Cancelled...
			return;
		}
		String directory = fc.getSelectedFile().getParent();
		String filename = fc.getSelectedFile().getName(); 
		String path = fc.getSelectedFile().getPath();
		JGUIUtil.setLastFileDialogDirectory ( directory );

		Message.printStatus ( 1, routine,
		"Reading MODSIM file \"" + path + "\"" );
		JGUIUtil.setWaitCursor ( this, true );
		Vector tslist = null;
		try {	tslist = ModsimTS.readTimeSeriesList (
			path, null, null, null, false );
		}
		catch ( Exception e ) {
			message = "Error reading MODSIM file.";
			Message.printWarning ( 2, routine, message );
			JGUIUtil.setWaitCursor ( this, false );
			throw new IOException ( message );
		}
		if ( tslist == null ) {
			message = "Error reading MODSIM file.";
			Message.printWarning ( 2, routine, message );
			JGUIUtil.setWaitCursor ( this, false );
			throw new IOException ( message );
		}
		int nts = 0;
		if ( tslist != null ) {
			nts = tslist.size();
		}
		TS ts = null;
		// There should not be any non-null time series so use the
		// Vector size...
		int size = tslist.size();
        	if ( size == 0 ) {
			Message.printStatus ( 1, routine,
			"No MODSIM TS read." );
			clearQueryList ();
        	}
        	else {	Message.printStatus ( 1, routine, ""
			+ size + " MODSIM TS read." );
			__query_TableModel = new TSTool_TS_TableModel (
				tslist, true );
			TSTool_TS_CellRenderer cr = new TSTool_TS_CellRenderer(
				(TSTool_TS_TableModel)__query_TableModel);
	
			__query_JWorksheet.setCellRenderer ( cr );
			__query_JWorksheet.setModel ( __query_TableModel );
			// Do not include the alias in the display...
			__query_JWorksheet.removeColumn (
			((TSTool_TS_TableModel)__query_TableModel).COL_ALIAS );
			__query_JWorksheet.setColumnWidths (
			cr.getColumnWidths(), getGraphics() );

			JGUIUtil.setWaitCursor ( this, false );
        		updateStatus ();
		}
		ts = null;
		tslist = null;
	}
	catch ( Exception e ) {
		message = "Error reading MODSIM file.";
		Message.printWarning ( 2, routine, message );
		Message.printWarning ( 2, routine, e );
		JGUIUtil.setWaitCursor ( this, false );
		throw new IOException ( message );
	}
}

/**
Read the list of time series from NWS CARD files and list in the GUI.
*/
public void readNWSCARDHeaders ()
throws IOException
{	String message, routine = "TSTool_JFrame.readNWSCARDHeaders";

	// REVISIT - allow multiple file selections...
	try {	JFileChooser fc = JFileChooserFactory.createJFileChooser (
			JGUIUtil.getLastFileDialogDirectory() );
		fc.setDialogTitle ( "Select NWS Card Time Series File" );

		/*
		SimpleFileFilter card_sff = new SimpleFileFilter( "card",
			"NWS Card Time Series");
		fc.addChoosableFileFilter(card_sff);
		SimpleFileFilter sff = new SimpleFileFilter( "txt",
			"NWS Card Time Series");
		fc.addChoosableFileFilter(sff);
		fc.setFileFilter(card_sff);
		*/

		FileFilter[] filters = NWSCardTS.getFileFilters();
		for (int i = 0; i < filters.length - 1; i++) {
			fc.addChoosableFileFilter(filters[i]);
		}
		fc.setFileFilter(filters[filters.length - 1]);
		
		if ( fc.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
			// Cancelled...
			return;
		}
		String directory = fc.getSelectedFile().getParent();
		String filename = fc.getSelectedFile().getName(); 
		String path = fc.getSelectedFile().getPath();
		JGUIUtil.setLastFileDialogDirectory ( directory );

		Message.printStatus ( 1, routine,
		"Reading NWS CARD file \"" + path + "\"" );
		JGUIUtil.setWaitCursor ( this, true );
		Vector tslist = null;
		try {	tslist = NWSCardTS.readTimeSeriesList (
				(TS)null,	// No requested time series
				path,
				(DateTime)null,	// Start
				(DateTime)null,	// End
				(String)null,	// Units
				false );	// No data, header only
		}
		catch ( Exception e ) {
			message = "Error reading NWS Card file.";
			Message.printWarning ( 2, routine, message );
			JGUIUtil.setWaitCursor ( this, false );
			clearQueryList ();
			throw new IOException ( message );
		}
		if ( tslist == null ) {
			message = "Error reading NWS CARD file.";
			Message.printWarning ( 2, routine, message );
			JGUIUtil.setWaitCursor ( this, false );
			throw new IOException ( message );
		}
		__query_TableModel = new TSTool_TS_TableModel ( tslist );
		TSTool_TS_CellRenderer cr = new TSTool_TS_CellRenderer(
			(TSTool_TS_TableModel)__query_TableModel);

		__query_JWorksheet.setCellRenderer ( cr );
		__query_JWorksheet.setModel ( __query_TableModel );
		__query_JWorksheet.removeColumn (
			((TSTool_TS_TableModel)__query_TableModel).COL_ALIAS );
		__query_JWorksheet.setColumnWidths (
			cr.getColumnWidths(), getGraphics() );

        	updateStatus ();
		JGUIUtil.setWaitCursor ( this, false );
	}
	catch ( Exception e ) {
		message = "Error reading NWS CARD file.";
		Message.printWarning ( 2, routine, message );
		Message.printWarning ( 2, routine, e );
		JGUIUtil.setWaitCursor ( this, false );
		throw new IOException ( message );
	}
}

/**
Read the list of time series from an NWSRFS ESPTraceEnsemble file and list in
the GUI.
*/
public void readNWSRFSESPTraceEnsembleHeaders ()
throws IOException
{	String message, routine =
		"TSTool_JFrame.readNWSRFSESPTraceEnsembleHeaders";

	try {	JFileChooser fc = JFileChooserFactory.createJFileChooser (
			JGUIUtil.getLastFileDialogDirectory() );
		fc.setDialogTitle ( "Select ESP Trace Ensemble File" );
		SimpleFileFilter csff = new SimpleFileFilter( "CS",
		"Conditional Simulation Trace File");
		fc.addChoosableFileFilter(csff);
		/* REVISIT - add later
		SimpleFileFilter sff = new SimpleFileFilter( "HS",
		"Historical Simulation Trace File");
		fc.addChoosableFileFilter(sff);
		*/
		fc.setFileFilter(csff);
		if ( fc.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
			// Cancelled...
			return;
		}
		String directory = fc.getSelectedFile().getParent();
		JGUIUtil.setLastFileDialogDirectory ( directory );
		String filename = fc.getSelectedFile().getName(); 
		String path = fc.getSelectedFile().getPath();
		Message.printStatus ( 1, routine,
		"Reading NWSRFS ESPTraceEnsemble file \"" + path + "\"" );
		JGUIUtil.setWaitCursor ( this, true );
		Vector tslist = null;
		NWSRFS_ESPTraceEnsemble ensemble = null;
		try {	ensemble = new NWSRFS_ESPTraceEnsemble ( path, false );
			// Get the array of time series...
			tslist = ensemble.getTimeSeriesVector ();
		}
		catch ( Exception e ) {
			message = "Error reading NWSRFS ESPTraceEnsemble file.";
			Message.printWarning ( 2, routine, message );
			Message.printWarning ( 2, routine, e );
			JGUIUtil.setWaitCursor ( this, false );
			throw new IOException ( message );
		}
		int size = 0;
		if ( tslist != null ) {
			size = tslist.size();
			__query_TableModel = new
				TSTool_ESPTraceEnsemble_TableModel ( ensemble );
			TSTool_ESPTraceEnsemble_CellRenderer cr =
				new TSTool_ESPTraceEnsemble_CellRenderer(
				(TSTool_ESPTraceEnsemble_TableModel)
				__query_TableModel);

			__query_JWorksheet.setCellRenderer ( cr );
			__query_JWorksheet.setModel ( __query_TableModel );
			__query_JWorksheet.setColumnWidths (
				cr.getColumnWidths(), getGraphics() );
		}
       		if ( (tslist == null) || (size == 0) ) {
			Message.printStatus ( 1, routine,
			"No NWSRFS ESPTraceEnsemble TS read." );
			clearQueryList ();
       		}
       		else {	Message.printStatus ( 1, routine, ""
			+ size + " NWSRFS ESPTraceEnsemble TS traces read." );
       		}

		JGUIUtil.setWaitCursor ( this, false );
       		updateStatus ();
	}
	catch ( Exception e ) {
		message = "Error reading NWSRFS ESPTraceEnsemble file.";
		Message.printWarning ( 2, routine, message );
		Message.printWarning ( 2, routine, e );
		JGUIUtil.setWaitCursor ( this, false );
		throw new IOException ( message );
	}
}

/**
Read the list of time series from NWSRFS FS5Files and list in the GUI.
*/
public void readNWSRFSFS5FilesHeaders ()
throws IOException
{	String message, routine = "TSTool_JFrame.readNWSRFSFS5FilesHeaders";

	try {	JGUIUtil.setWaitCursor ( this, true );
		Vector tslist = null;
		// Default is to return all IDs...
		String id_input = "*";
		// Get the ID from the input filter...
		Vector input_Vector = __input_filter_NWSRFS_FS5Files_JPanel.
			getInput ( "ID", true, null );
		int isize = input_Vector.size();
		if ( isize > 1 ) {
			Message.printWarning ( 1, routine,
			"Only one input filter for ID can be specified." );
			return;
		}
		if ( isize > 0 ) {
			// Use the first matching filter...
			id_input = (String)input_Vector.elementAt(0);
		}
		String datatype = StringUtil.getToken ( 
			__data_type_JComboBox.getSelected().trim(), " ",0, 0);
		// Parse the interval into the integer hour...
		int selected_interval = 0;
		try {	TimeInterval interval = TimeInterval.parseInterval (
				__selected_time_step);
			selected_interval = interval.getMultiplier();
		}
		catch ( Exception e ) {
			// Should never fail...
		}
		String selected_input_name=__input_name_JComboBox.getSelected();
		try {	String tsident_string = id_input + ".NWSRFS." +
				datatype + "." +
				__selected_time_step + "~NWSRFS_FS5Files~" +
				selected_input_name;
			Message.printStatus ( 2, routine,
			"Reading NWSRFS FS5Files time series for \"" +
			tsident_string + "\"..." );
			tslist = __nwsrfs_dmi.readTimeSeriesList (
				tsident_string, (DateTime)null,
				(DateTime)null, (String)null, false );
		}
		catch ( Exception e ) {
			message = "Error reading NWSRFS time series list.";
			Message.printWarning ( 2, routine, message );
			Message.printWarning ( 2, routine, e );
			JGUIUtil.setWaitCursor ( this, false );
			throw new IOException ( message );
		}
		
		int size = 0;
		TS ts = null;
		if ( tslist != null ) {
			size = tslist.size();

			// Now display in the table...

			__query_TableModel = new TSTool_TS_TableModel (
				tslist, true );
			TSTool_TS_CellRenderer cr = new TSTool_TS_CellRenderer(
				(TSTool_TS_TableModel)__query_TableModel);
	
			__query_JWorksheet.setCellRenderer ( cr );
			__query_JWorksheet.setModel ( __query_TableModel );
			// Do not include the alias in the display...
			__query_JWorksheet.removeColumn (
			((TSTool_TS_TableModel)__query_TableModel).COL_ALIAS );
			__query_JWorksheet.setColumnWidths (
			cr.getColumnWidths(), getGraphics() );
		}
       		if ( (tslist == null) || (size == 0) ) {
			Message.printStatus ( 1, routine,
			"No NWSRFS FS5Files time series read." );
			clearQueryList ();
       		}
       		else {	Message.printStatus ( 1, routine, ""
			+ size + " NWSRFS FS5Files time series read." );
       		}

		JGUIUtil.setWaitCursor ( this, false );
       		updateStatus ();
	}
	catch ( Exception e ) {
		JGUIUtil.setWaitCursor ( this, false );
		message = "Error reading NWSRFS FS5Files time series list.";
		Message.printWarning ( 2, routine, message );
		Message.printWarning ( 2, routine, e );
		throw new IOException ( message );
	}
}

/**
Read the pattern time series corresponding to the pattern file names.
Only read the headers.  Then the identifiers are saved for use in the
fillPattern_Dialog.
*/
public void readPatternTS ()
{	// Empty the list...
	__fill_pattern_ids.removeAllElements();
	Vector fill_pattern_ts = null;
	TS ts = null;
	for (	int ifile = 0; ifile < __fill_pattern_files.size();
		ifile++ ) {
		// REVISIT - need this to be more generic.  If a statemod
		// pattern file is specified, read it, else, allow for other
		// formats.  For now always read a StateMod file.
		//
		// Read the header but not the actual data since only a list of
		// identifiers is needed.
		fill_pattern_ts = StateMod_TS.readPatternTimeSeriesList (
			IOUtil.getPathUsingWorkingDir(
			(String)__fill_pattern_files.elementAt(ifile)), false );
		if ( fill_pattern_ts != null ) {
			int listsize = fill_pattern_ts.size();
			for ( int j = 0; j < listsize; j++ ) {
				ts = (TS)fill_pattern_ts.elementAt(j);
				if ( ts == null ) {
					continue;
				}
				__fill_pattern_ids.addElement (
					ts.getLocation() );
			}
		}
	}
	ts = null;
	fill_pattern_ts = null;
}

/**
Read RiversideDB time series (MeasType) and list in the GUI.
*/
private void readRiversideDBHeaders()
{	String rtn = "TSTool_JFrame.readRiversideDBHeaders";
        JGUIUtil.setWaitCursor ( this, true );
        Message.printStatus ( 1, rtn, "Please wait... retrieving data");

	// The headers are a Vector of HydroBase_
	try {	clearQueryList ();

		String location = "";
		String data_type = StringUtil.getToken(
			__data_type_JComboBox.getSelected()," ",0,0).trim();
		String timestep = __time_step_JComboBox.getSelected();
		if ( timestep == null ) {
			Message.printWarning ( 1, rtn,
			"No time series are available for timestep." );
			JGUIUtil.setWaitCursor ( this, false );
			return;
		}
		else {	timestep = timestep.trim();
		}
		String data_type_mod = "";
		if ( data_type.indexOf ( "-" ) >= 0 ) {
			data_type_mod = StringUtil.getToken ( data_type,
				"-", 0, 1 );
		}
		if ( !data_type_mod.equals("") ) {
			data_type_mod = "-" + data_type_mod;
		}

/* SAMX - need to figure out how to do where - or do we just have choices?
        	// add where clause(s)   
		if ( getWhereClauses() == false ) {
        		JGUIUtil.setWaitCursor ( this, false );
        		Message.printStatus ( 1, rtn, 
			"Query aborted - null where string");
			return;
		}
*/

		TSIdent ident = new TSIdent ( location + ".." + data_type +
					data_type_mod + "." + timestep + "." );
		Message.printStatus ( 1, "",
		"Datatype = \"" + ident.getType() + "\" main = \"" +
		ident.getMainType() + "\" sub = \"" + ident.getSubType() +"\"");

		Vector results = null;

		try {	results = __rdmi.readMeasTypeListForTSIdent (
				location + ".." + data_type + data_type_mod +
				"." + timestep + "." );
		}
		catch ( Exception e ) {
			results = null;
		}

		int size = 0;
		if ( results != null ) {
			size = results.size();
			// Does not work??
			//__query_TableModel.setNewData ( results );
			// Try brute force...
			__query_TableModel = new
				TSTool_RiversideDB_TableModel ( results );
			TSTool_RiversideDB_CellRenderer cr =
				new TSTool_RiversideDB_CellRenderer(
				(TSTool_RiversideDB_TableModel)
				__query_TableModel);

			__query_JWorksheet.setCellRenderer ( cr );
			__query_JWorksheet.setModel ( __query_TableModel );
			__query_JWorksheet.setColumnWidths (
				cr.getColumnWidths(), getGraphics() );
		}
        	if ( (results == null) || (size == 0) ) {
			Message.printStatus ( 1, rtn,
			"Query complete.  No records returned." );
        	}
        	else {	Message.printStatus ( 1, rtn, "Query complete. "
			+ size + " records returned." );
        	}

        	updateStatus ();

        	JGUIUtil.setWaitCursor ( this, false );
	}
	catch ( Exception e ) {
		// Messages elsewhere but catch so we can get the cursor
		// back...
		Message.printWarning ( 3, rtn, e );
        	JGUIUtil.setWaitCursor ( this, false );
	}
}

/**
Read the list of time series from RiverWare files and list in the GUI.
*/
public void readRiverWareHeaders ()
throws IOException
{	String message, routine = "TSTool_JFrame.readRiverWareHeaders";

	try {	JFileChooser fc = JFileChooserFactory.createJFileChooser (
			JGUIUtil.getLastFileDialogDirectory() );
		fc.setDialogTitle ( "Select RiverWare Time Series File" );
		SimpleFileFilter sff = new SimpleFileFilter( "dat",
			"RiverWare Time Series");
		fc.addChoosableFileFilter(sff);
		sff = new SimpleFileFilter( "rwts", "RiverWare Time Series");
		fc.addChoosableFileFilter(sff);
		sff = new SimpleFileFilter( "ts", "RiverWare Time Series");
		fc.addChoosableFileFilter(sff);
		if ( fc.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
			// Cancelled...
			return;
		}
		JGUIUtil.setLastFileDialogDirectory (
			fc.getSelectedFile().getParent() );
		String path = fc.getSelectedFile().getPath();

		Message.printStatus ( 1, routine,
		"Reading RiverWare file \"" + path + "\"" );
		JGUIUtil.setWaitCursor ( this, true );
		TS ts = RiverWareTS.readTimeSeries (
			path, null, null, null, false );
		if ( ts == null ) {
			message = "Error reading RiverWare file.";
			Message.printWarning ( 2, routine, message );
			JGUIUtil.setWaitCursor ( this, false );
			throw new IOException ( message );
		}
		Vector tslist = new Vector ( 1 );
		tslist.addElement ( ts );
		__query_TableModel = new TSTool_TS_TableModel ( tslist );
		TSTool_TS_CellRenderer cr = new TSTool_TS_CellRenderer(
			(TSTool_TS_TableModel)__query_TableModel);

		__query_JWorksheet.setCellRenderer ( cr );
		__query_JWorksheet.setModel ( __query_TableModel );
		__query_JWorksheet.removeColumn (
			((TSTool_TS_TableModel)__query_TableModel).COL_ALIAS );
		__query_JWorksheet.setColumnWidths (
			cr.getColumnWidths(), getGraphics() );

        	updateStatus ();
		JGUIUtil.setWaitCursor ( this, false );
	}
	catch ( Exception e ) {
		message = "Error reading RiverWare file.";
		Message.printWarning ( 2, routine, message );
		Message.printWarning ( 2, routine, e );
		JGUIUtil.setWaitCursor ( this, false );
		throw new IOException ( message );
	}
}

/**
Read the list of time series from a StateCU file and display the list in the
GUI.
@exception Exception if there is an error reading the file.
*/
public void readStateCUHeaders ()
throws Exception
{	String message, routine = "TSTool_JFrame.readStateCUHeaders";

	try {	Vector tslist = null;			// Time series to
							// display.
		TS ts = null;				// Single time series.
		int size = 0;				// Number of time
							// series.
		String path = __input_name_JComboBox.getSelected();
		Message.printStatus ( 1, routine,
		"Reading StateCU file \"" + path + "\"" );
		if (	(__input_name_FileFilter ==
			__input_name_StateCU_iwrrep_FileFilter) ||
			(__input_name_FileFilter ==
			__input_name_StateCU_wsl_FileFilter) ||
			(__input_name_FileFilter ==
			__input_name_StateCU_frost_FileFilter) ) {
			JGUIUtil.setWaitCursor ( this, true );
			tslist = StateCU_TS.readTimeSeriesList (
					path, null, null, null, false );
			if ( tslist != null ) {
				size = tslist.size();
				__query_TableModel = new
					TSTool_TS_TableModel ( tslist );
				TSTool_TS_CellRenderer cr =
					new TSTool_TS_CellRenderer(
					(TSTool_TS_TableModel)
					__query_TableModel);
	
				__query_JWorksheet.setCellRenderer ( cr );
				__query_JWorksheet.setModel(__query_TableModel);
				// Turn off columns in the table model that do
				// not apply...
				__query_JWorksheet.removeColumn (
					((TSTool_TS_TableModel)
					__query_TableModel).COL_ALIAS );
				__query_JWorksheet.removeColumn (
					((TSTool_TS_TableModel)
					__query_TableModel).COL_DATA_SOURCE );
				__query_JWorksheet.removeColumn (
					((TSTool_TS_TableModel)
					__query_TableModel).COL_SCENARIO);
				__query_JWorksheet.setColumnWidths (
					cr.getColumnWidths(), getGraphics() );
			}
		}
		else if ( __input_name_FileFilter ==
			__input_name_StateCU_cds_FileFilter ) {
			// Yearly crop patterns...
			JGUIUtil.setWaitCursor ( this, true );
			tslist = StateCU_CropPatternTS.readTimeSeriesList (
					path, null, null, null, false );
			if ( tslist != null ) {
				size = tslist.size();
				__query_TableModel = new
					TSTool_TS_TableModel ( tslist );
				TSTool_TS_CellRenderer cr =
					new TSTool_TS_CellRenderer(
					(TSTool_TS_TableModel)
					__query_TableModel);
	
				__query_JWorksheet.setCellRenderer ( cr );
				__query_JWorksheet.setModel(__query_TableModel);
				// Turn off columns in the table model that do
				// not apply...
				__query_JWorksheet.removeColumn (
					((TSTool_TS_TableModel)
					__query_TableModel).COL_ALIAS );
				__query_JWorksheet.removeColumn (
					((TSTool_TS_TableModel)
					__query_TableModel).COL_DATA_SOURCE );
				__query_JWorksheet.removeColumn (
					((TSTool_TS_TableModel)
					__query_TableModel).COL_SCENARIO);
				__query_JWorksheet.setColumnWidths (
					cr.getColumnWidths(), getGraphics() );
			}
		}
		else if ((__input_name_FileFilter ==
			__input_name_StateCU_ipy_FileFilter) ||
			(__input_name_FileFilter ==
			__input_name_StateCU_tsp_FileFilter) ) {
			// Yearly irrigation practice...
			JGUIUtil.setWaitCursor ( this, true );
			tslist = StateCU_IrrigationPracticeTS.
					readTimeSeriesList (
					path, null, null, null, false );
			if ( tslist != null ) {
				size = tslist.size();
				__query_TableModel = new
					TSTool_TS_TableModel ( tslist );
				TSTool_TS_CellRenderer cr =
					new TSTool_TS_CellRenderer(
					(TSTool_TS_TableModel)
					__query_TableModel);
	
				__query_JWorksheet.setCellRenderer ( cr );
				__query_JWorksheet.setModel(__query_TableModel);
				// Turn off columns in the table model that do
				// not apply...
				__query_JWorksheet.removeColumn (
					((TSTool_TS_TableModel)
					__query_TableModel).COL_ALIAS );
				__query_JWorksheet.removeColumn (
					((TSTool_TS_TableModel)
					__query_TableModel).COL_DATA_SOURCE );
				__query_JWorksheet.removeColumn (
					((TSTool_TS_TableModel)
					__query_TableModel).COL_SCENARIO);
				__query_JWorksheet.setColumnWidths (
					cr.getColumnWidths(), getGraphics() );
			}
		}
		else if ((__input_name_FileFilter ==
			__input_name_StateCU_ddc_FileFilter) ||
			(__input_name_FileFilter ==
			__input_name_StateCU_ddy_FileFilter) ||
			(__input_name_FileFilter ==
			__input_name_StateCU_ddh_FileFilter) ||
			(__input_name_FileFilter ==
			__input_name_StateCU_precip_FileFilter) ||
			(__input_name_FileFilter ==
			__input_name_StateCU_temp_FileFilter) ||
			(__input_name_FileFilter ==
			__input_name_StateCU_frost_FileFilter) ) {
			// Normal daily or monthly StateMod file...
			JGUIUtil.setWaitCursor ( this, true );
			if (	 __input_name_FileFilter ==
				__input_name_StateCU_frost_FileFilter ) {
				tslist = StateCU_TS.
					readTimeSeriesList (
					path, null, null, null, false );
			}
			else {	// Normal StateMod file...
				tslist = StateMod_TS.readTimeSeriesList (
					path, null, null, null, false );
			}
			// Change time series data source to StateCU since the
			// file is part of a StateCU data set...
			if ( tslist != null ) {
				size = tslist.size();
				for ( int i = 0; i < size; i++ ) {
					ts = (TS)tslist.elementAt(i);
					if ( ts == null ) {
						continue;
					}
					ts.getIdentifier().setSource("StateCU");
				}
				__query_TableModel = new
					TSTool_TS_TableModel ( tslist );
				TSTool_TS_CellRenderer cr =
					new TSTool_TS_CellRenderer(
					(TSTool_TS_TableModel)
					__query_TableModel);
	
				__query_JWorksheet.setCellRenderer ( cr );
				__query_JWorksheet.setModel(__query_TableModel);
				// Turn off columns in the table model that do
				// not apply...
				__query_JWorksheet.removeColumn (
					((TSTool_TS_TableModel)
					__query_TableModel).COL_ALIAS );
				__query_JWorksheet.removeColumn (
					((TSTool_TS_TableModel)
					__query_TableModel).COL_DATA_SOURCE );
				__query_JWorksheet.removeColumn (
					((TSTool_TS_TableModel)
					__query_TableModel).COL_DATA_TYPE );
				__query_JWorksheet.removeColumn (
					((TSTool_TS_TableModel)
					__query_TableModel).COL_SCENARIO);
				__query_JWorksheet.setColumnWidths (
					cr.getColumnWidths(), getGraphics() );
			}
		}
		else {	Message.printWarning ( 1, routine,
			"File format not recognized for \"" + path + "\"" );
		}
       		if ( (tslist == null) || (size == 0) ) {
			Message.printStatus (1, routine,"No StateCU TS read.");
			clearQueryList ();
       		}
       		else {	Message.printStatus ( 1, routine, ""
			+ size + " StateCU TS read." );
       		}

		JGUIUtil.setWaitCursor ( this, false );
       		updateStatus ();
	}
	catch ( Exception e ) {
		message = "Error reading StateCU file.";
		Message.printWarning ( 2, routine, message );
		Message.printWarning ( 2, routine, e );
		JGUIUtil.setWaitCursor ( this, false );
		throw new Exception ( message );
	}
}

/**
Read the list of time series from a StateMod file and display the list in the
GUI.
@exception Exception if there is an error reading the file.
*/
public void readStateModHeaders ()
throws Exception
{	String message, routine = "TSTool_JFrame.readStateModHeaders";

	try {	JFileChooser fc = JFileChooserFactory.createJFileChooser (
				JGUIUtil.getLastFileDialogDirectory() );
		// REVISIT - need to pick the file first and detect the time
		// step from the file, similar to the binary file.  For now,
		// key off the selected time step.
		if ( __selected_time_step.equals( __TIMESTEP_DAY)) {
			fc.setDialogTitle (
				"Select StateMod Daily Time Series File" );
			SimpleFileFilter sff = new SimpleFileFilter( "ddd",
				"StateMod Direct Diversion Demands (Daily)");
			fc.addChoosableFileFilter(sff);
			sff = new SimpleFileFilter( "ddy",
				"StateMod Historicial Diversions (Daily)");
			fc.addChoosableFileFilter(sff);
			sff = new SimpleFileFilter( "eoy",
				"StateMod Reservoir Storage (End of Day)");
			fc.addChoosableFileFilter(sff);
			sff = new SimpleFileFilter( "ifd",
				"StateMod Instream Flow Demands (Daily)");
			fc.addChoosableFileFilter(sff);
			sff = new SimpleFileFilter( "riy",
				"StateMod Base Streamflow (Daily)");
			fc.addChoosableFileFilter(sff);
			sff = new SimpleFileFilter( "riy",
				"StateMod Historicial Streamflow (Daily)");
			fc.addChoosableFileFilter(sff);
			SimpleFileFilter stm_sff = new SimpleFileFilter( "stm",
				"StateMod Time Series (Daily)");
			fc.addChoosableFileFilter(sff);
			sff = new SimpleFileFilter( "tad",
				"StateMod Reservoir Min/Max Targets (Daily)");
			fc.addChoosableFileFilter(sff);
			sff = new SimpleFileFilter( "wed",
				"StateMod Well Demands (Daily)");
			fc.addChoosableFileFilter(sff);
			sff = new SimpleFileFilter( "wey",
				"StateMod Historicial Well Pumping (Daily)");
			fc.addChoosableFileFilter(sff);
			fc.setFileFilter(stm_sff);
		}
		else {	fc.setDialogTitle (
				"Select StateMod Monthly Time Series File" );
			SimpleFileFilter sff = new SimpleFileFilter( "ddh",
				"StateMod Historicial Diversions (Monthly)");
			fc.addChoosableFileFilter(sff);
			sff = new SimpleFileFilter( "dda",
			"StateMod Direct Diversion Demands (Average Monthly)");
			fc.addChoosableFileFilter(sff);
			sff = new SimpleFileFilter( "ddm",
				"StateMod Direct Diversion Demands (Monthly)");
			fc.addChoosableFileFilter(sff);
			sff = new SimpleFileFilter( "ddo",
			"StateMod Direct Diversion Demands Override (Monthly)");
			fc.addChoosableFileFilter(sff);
			sff = new SimpleFileFilter( "eom",
				"StateMod Reservoir Storage (End of Month)");
			fc.addChoosableFileFilter(sff);
			sff = new SimpleFileFilter( "eva",
				"StateMod Evaporation (Monthly)");
			fc.addChoosableFileFilter(sff);
			sff = new SimpleFileFilter( "ifa",
			"StateMod Instream Flow Demands (Average Monthly)");
			fc.addChoosableFileFilter(sff);
			sff = new SimpleFileFilter( "ifm",
				"StateMod Instream Flow Demands (Monthly)");
			fc.addChoosableFileFilter(sff);
			sff = new SimpleFileFilter( "ddc",
				"StateMod/StateCU Irrigation Water" +
				" Requirement (Monthly)");
			fc.addChoosableFileFilter(sff);
			sff = new SimpleFileFilter( "iwr",
				"StateMod/StateCU Irrigation Water" +
				" Requirement (Monthly)");
			fc.addChoosableFileFilter(sff);
			sff = new SimpleFileFilter( "pre",
				"StateMod Precipitation (Monthly)");
			fc.addChoosableFileFilter(sff);
			sff = new SimpleFileFilter( "rih",
				"StateMod Historicial Streamflow (Monthly)");
			fc.addChoosableFileFilter(sff);
			sff = new SimpleFileFilter( "riy",
				"StateMod Base Streamflow (Monthly)");
			fc.addChoosableFileFilter(sff);
			sff = new SimpleFileFilter( "tar",
				"StateMod Reservoir Min/Max Targets (Monthly)");
			fc.addChoosableFileFilter(sff);
			SimpleFileFilter stm_sff = new SimpleFileFilter( "stm",
				"StateMod Time Series (Monthly)");
			fc.addChoosableFileFilter(stm_sff);
			sff = new SimpleFileFilter( "wem",
				"StateMod Well Demands (Monthly)");
			fc.addChoosableFileFilter(sff);
			sff = new SimpleFileFilter( "weh",
				"StateMod Historicial Well Pumping (Monthly)");
			fc.addChoosableFileFilter(sff);
			fc.setFileFilter(stm_sff);
		}
		if ( fc.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
			// Cancelled...
			return;
		}
		String directory = fc.getSelectedFile().getParent();
		JGUIUtil.setLastFileDialogDirectory ( directory );
		JGUIUtil.setLastFileDialogDirectory(directory);
		String filename = fc.getSelectedFile().getName(); 
		String path = fc.getSelectedFile().getPath();
		Message.printStatus ( 1, routine,
		"Reading StateMod file \"" + path + "\"" );
		TS ts = null;
		// Normal daily or monthly format file...
		Vector tslist = null;
		JGUIUtil.setWaitCursor ( this, true );
		if ( __selected_time_step.equals(__TIMESTEP_DAY) ) {
			// Only read the headers...
			tslist = StateMod_TS.readTimeSeriesList (
					path, null, null, null, false );
		}
		else {	// Only read the headers...
			tslist = StateMod_TS.readTimeSeriesList (
				path, null, null, null, false );
		}
		int size = 0;
		if ( tslist != null ) {
			size = tslist.size();
			__query_TableModel = new TSTool_TS_TableModel ( tslist);
			TSTool_TS_CellRenderer cr = new TSTool_TS_CellRenderer(
				(TSTool_TS_TableModel)__query_TableModel);
	
			__query_JWorksheet.setCellRenderer ( cr );
			__query_JWorksheet.setModel(__query_TableModel);
			// Turn off columns in the table model that do not
			// apply...
			__query_JWorksheet.removeColumn (
				((TSTool_TS_TableModel)
				__query_TableModel).COL_ALIAS );
			__query_JWorksheet.removeColumn (
				((TSTool_TS_TableModel)
				__query_TableModel).COL_DATA_SOURCE );
			__query_JWorksheet.removeColumn (
				((TSTool_TS_TableModel)
				__query_TableModel).COL_DATA_TYPE );
			__query_JWorksheet.removeColumn (
				((TSTool_TS_TableModel)
				__query_TableModel).COL_SCENARIO);
			__query_JWorksheet.setColumnWidths (
				cr.getColumnWidths(), getGraphics() );
		}
        	if ( (tslist == null) || (size == 0) ) {
			Message.printStatus ( 1, routine,
			"No StateMod TS read." );
			clearQueryList ();
        	}
        	else {	Message.printStatus ( 1, routine, ""
			+ size + " StateMod TS read." );
        	}

		JGUIUtil.setWaitCursor ( this, false );
        	updateStatus ();
	}
	catch ( Exception e ) {
		message = "Error reading StateMod file.";
		Message.printWarning ( 2, routine, message );
		JGUIUtil.setWaitCursor ( this, false );
		throw new Exception ( message );
	}
}

/**
Read the list of time series from a StateMod binary file and list in the GUI.
The binary file is taken from the selected item in the __input_name_JComboBox.
*/
public void readStateModBHeaders ()
throws IOException
{	String routine = "TSTool_JFrame.readStateModBHeaders";

	try {	String path = __input_name_JComboBox.getSelected();
		Message.printStatus ( 1, routine,
		"Reading StateMod binary output file \"" + path + "\"" );
		TS ts = null;
		Vector tslist = null;
		JGUIUtil.setWaitCursor ( this, true );
		StateMod_BTS bin = new StateMod_BTS ( path );
		tslist = bin.readTimeSeriesList (
			"*.*." + __selected_data_type + ".*.*",
			null, null, null, false );
		bin.close();
		int size = 0;
		if ( tslist != null ) {
			size = tslist.size();
			__query_TableModel = new
				TSTool_TS_TableModel ( tslist );
			TSTool_TS_CellRenderer cr =
				new TSTool_TS_CellRenderer(
				(TSTool_TS_TableModel)
				__query_TableModel);

			__query_JWorksheet.setCellRenderer ( cr );
			__query_JWorksheet.setModel ( __query_TableModel );
			// Turn off columns in the table model that do not
			// apply...
			__query_JWorksheet.removeColumn (
				((TSTool_TS_TableModel)
				__query_TableModel).COL_ALIAS );
			__query_JWorksheet.removeColumn (
				((TSTool_TS_TableModel)
				__query_TableModel).COL_ALIAS );
			__query_JWorksheet.removeColumn (
				((TSTool_TS_TableModel)
				__query_TableModel).COL_SCENARIO);
			__query_JWorksheet.removeColumn (
				((TSTool_TS_TableModel)
				__query_TableModel).COL_DATA_SOURCE );
			//__query_JWorksheet.removeColumn (
				//((TSTool_TS_TableModel)
				//__query_TableModel).COL_DATA_TYPE );
			__query_JWorksheet.setColumnWidths (
				cr.getColumnWidths(), getGraphics() );
		}
       		if ( (tslist == null) || (size == 0) ) {
			Message.printStatus ( 1, routine,
			"No StateMod binary TS were read." );
			clearQueryList ();
       		}
       		else {	Message.printStatus ( 1, routine, ""
			+ size + " StateMod binary TS were read." );
       		}

       		updateStatus ();
		tslist = null;
		ts = null;
		JGUIUtil.setWaitCursor ( this, false );
	}
	catch ( Exception e ) {
		String message = "Error reading StateMod binary file.";
		Message.printWarning ( 2, routine, message );
		Message.printWarning ( 2, routine, e );
		JGUIUtil.setWaitCursor ( this, false );
		throw new IOException ( message );
	}
}

/**
Read the list of time series from a USGS NWIS file and list in the GUI.
*/
public void readUsgsNwisHeaders ()
throws IOException
{	String message, routine = "TSTool_JFrame.readUsgsNwisHeaders";

	try {	JFileChooser fc = JFileChooserFactory.createJFileChooser (
			JGUIUtil.getLastFileDialogDirectory() );
		fc.setDialogTitle ( "Select USGS NWIS Daily Surface Flow File");
		SimpleFileFilter sff = new SimpleFileFilter( "txt",
			"USGS NWIS Daily Surface Flow File");
		fc.addChoosableFileFilter(sff);
		if ( fc.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
			// Cancelled...
			return;
		}
		JGUIUtil.setLastFileDialogDirectory (
			fc.getSelectedFile().getParent() );
		String path = fc.getSelectedFile().getPath();

		Message.printStatus ( 1, routine,
		"Reading USGS NWIS file \"" + path + "\"" );
		JGUIUtil.setWaitCursor ( this, true );
		TS ts = UsgsNwisTS.readTimeSeries(path, null, null, null,false);
		if ( ts == null ) {
			message = "Error reading USGS NWIS file \""+path + "\"";
			Message.printWarning ( 2, routine, message );
			JGUIUtil.setWaitCursor ( this, false );
			throw new IOException ( message );
		}
		Vector tslist = new Vector ( 1 );
		tslist.addElement ( ts );
		__query_TableModel = new TSTool_TS_TableModel ( tslist );
		TSTool_TS_CellRenderer cr = new TSTool_TS_CellRenderer(
			(TSTool_TS_TableModel)__query_TableModel);

		__query_JWorksheet.setCellRenderer ( cr );
		__query_JWorksheet.setModel ( __query_TableModel );
		__query_JWorksheet.removeColumn (
			((TSTool_TS_TableModel)__query_TableModel).COL_ALIAS );
		__query_JWorksheet.setColumnWidths (
			cr.getColumnWidths(), getGraphics() );

        	updateStatus ();
		JGUIUtil.setWaitCursor ( this, false );
	}
	catch ( Exception e ) {
		message = "Error reading USGS NWIS file.";
		Message.printWarning ( 2, routine, message );
		Message.printWarning ( 2, routine, e );
		JGUIUtil.setWaitCursor ( this, false );
		throw new IOException ( message );
	}
}

/**
Run the commands in the command list.  These time series are saved in
__final_ts_engine and are then available for export, analysis, or viewing.  This
method should only be called if there are commands in the command list.
@param run_all_commands If false, then only the selected commands are run.  If
true, then all commands are run.
@param create_output If true, then all write* methods are processed by the
TSEngine.  If false, only the time series in memory remain at the end and can
be viewed.  The former is suitable for batch files, both for the GUI.
*/
private void runCommands ( boolean run_all_commands, boolean create_output )
{	String rtn = "TSTool_JFrame.runCommands";
	Message.printStatus ( 1, rtn, "Running commands." );
	JGUIUtil.setWaitCursor ( this, true );
	// Clean up old TSEngine...
	clearTSList ();	// Sets __final_ts_engine to null.
	System.gc();
	// Now get a new TSEngine...
	Vector commands = getCommands ( run_all_commands );
	// Save the commands in case any output calls
	// IOUtil.printCreatorHeader...
	IOUtil.setProgramCommandList ( commands );
	__final_ts_engine = new TSEngine ( __hbdmi, __rdmi, __DIADvisor_dmi,
				__DIADvisor_archive_dmi, __nwsrfs_dmi,
				__smsdmi, commands, this, create_output );
	JGUIUtil.setWaitCursor ( this, false );
	int size = JGUIUtil.selectedSize ( __commands_JList );
	// Fill the time series list with the descriptions of the in-memory
	// time series...
	size = __final_ts_engine.getTimeSeriesSize();
	TS ts = null;
	String desc = null;
	String alias = null;
	BinaryTS binary_ts = null;
	int size_visible = 0;
	for ( int i = 0; i < size; i++ ) {
		if ( __final_ts_engine.isBinaryTSUsed() ) {
			try {	binary_ts = __final_ts_engine.getBinaryTS();
				desc = binary_ts.getDescription(i);
				if ( (desc == null) || (desc.length() == 0) ) {
					desc=	binary_ts.getIdentifier(i
						).getLocation();
				}
				addTimeSeries ( "" + (i + 1) + ") " +
				desc + " - " +
				binary_ts.getIdentifier(i) + " (" +
				binary_ts.getDate1() + " to " +
				binary_ts.getDate2() + ")" );
			}
			catch ( Exception e ) {
				addTimeSeries ( "" + (i + 1) +
				" Error getting Time Series" );
				continue;
			}
		}
		else {	try {	ts = __final_ts_engine.getTimeSeries(i);
			}
			catch ( Exception e ) {
				addTimeSeries ( "" + (i + 1) +
				" Error getting Time Series" );
				continue;
			}
			if ( ts == null ) {
				addTimeSeries("" +(i + 1)+" Null Time Series" );
				continue;
			}
			else {	desc = ts.getDescription();
				alias = ts.getAlias();
				if ( !alias.equals("") ) {
					alias = alias + " - ";
				}
				if ( (desc == null) || (desc.length() == 0) ) {
					desc = ts.getIdentifier().getLocation();
				}
				addTimeSeries ( "" + (i + 1) + ") " + alias +
				desc + " - " + ts.getIdentifier() +
				" (" + ts.getDate1() + " to " +
				ts.getDate2() + ")" );
			}
		}
		++size_visible;
	}
	// Default is to select all the time series...
	int [] selected = new int[size_visible];
	for ( int i = 0; i < size_visible; i++ ) {
		selected[i] = i;
	}
	__ts_JList.setSelectedIndices ( selected );
	updateStatus ();
	Message.printStatus ( 1, rtn, "Completed running commands.  Use "+
		"Results and Tools menus." );
}

/**
Run with a hidden main gui but process the specified commands file.
If also in batch mode because commands were specified on the command line, the
a true batch run will be made.
@param commands_file Commands file to process.
@exception Exception if there is an error processing the file, for example
no data to graph.
*/
private void runNoMainGUI ( String commands_file )
throws Exception
{	String routine = "TSTool_JFrame.runNoMainGUI";
	// Run similar to the Run...Commands menu
	TSEngine engine = null;
	// Process as if in batch mode...
	engine = new TSEngine ( __hbdmi, __rdmi, __DIADvisor_dmi,
				__DIADvisor_archive_dmi, __nwsrfs_dmi,
				__smsdmi );
	engine.addTSViewWindowListener ( this );
	engine.processCommands ( __hbdmi, commands_file, IOUtil.isBatch() );
	engine = null;
	Message.printStatus ( 1, routine,
	"Successfully processed commands file \"" + commands_file + "\"" );
}

/**
Run TSTool in server mode.  Currently this is done without the GUI.
Need to update this to use TSEngine for processing.
*/
public void runServer ()
{	// Loop until an "end" or "exit" command is encountered.  Read input
	// from standard input and execute commands.  Do not yet support full
	// TSEngine features but focus on TS Product processing...
	BufferedReader in =new BufferedReader(new InputStreamReader(System.in));
	PrintStream out = System.out;
	String string;
	while ( true ) {
		try {	string = in.readLine();
		}
		catch ( Exception e ) {
			break;
		}
		if ( string == null ) {
			// Should this ever happen?  Or will it happen because
			// of the buffering, etc.?
			continue;
		}
		string = string.trim();
		if ( string.length() == 0 ) {
			continue;
		}
		if (	string.equalsIgnoreCase("exit") ||
			string.equalsIgnoreCase("end") ) {
			out.println ( "stop 0" );
			closeClicked();
		}
		else {	out.println("error");
		}
	}
}

// REVISIT - need to enable/disable filters based on the list of time series
// that are selected.
/**
Save the current time series to the selected format.  The user picks the file
and the format, via the chooser filter.
*/
private void saveTimeSeries ()
{	JFileChooser fc = JFileChooserFactory.createJFileChooser(
		JGUIUtil.getLastFileDialogDirectory() );
	fc.setDialogTitle("Save Time Series (full period)");
	// Default, and always enabled...
	SimpleFileFilter datevalue_sff = null;
	datevalue_sff=new SimpleFileFilter( "dv", "DateValue Time Series File");
	fc.addChoosableFileFilter( datevalue_sff );
	fc.setFileFilter(datevalue_sff);
	// Add ESP Trace Ensemble only if enabled in the configuration... 
	SimpleFileFilter esp_cs_sff = null;
	if ( __source_NWSRFS_ESPTraceEnsemble_enabled ) {
		esp_cs_sff = new SimpleFileFilter(
			"CS","ESP Conditional Trace Ensemble File");
		fc.addChoosableFileFilter ( esp_cs_sff );
	}
	// Add NWS Card only if enabled in the configuration... 
//	SimpleFileFilter nwscard_sff = null;
	FileFilter[] nwscardFilters = null;
	if ( __source_NWSCard_enabled ) {
		nwscardFilters = NWSCardTS.getFileFilters();
		for (int i = 0; i < nwscardFilters.length - 1; i++) {
			fc.addChoosableFileFilter(nwscardFilters[i]);
		}
/*
		nwscard_sff = new SimpleFileFilter(
				"nwscard", "NWS Card Time Series File" );
		fc.addChoosableFileFilter( nwscard_sff );
*/
	}
	// Add RiverWare only if enabled in the configuration... 
	SimpleFileFilter riverware_sff = null;
	if ( __source_RiverWare_enabled ) {
		riverware_sff = new SimpleFileFilter(
				"txt", "RiverWare Time Series Format File" );
		fc.addChoosableFileFilter( riverware_sff );
	}
	// Add SHEF only if enabled in the configuration... 
	SimpleFileFilter shef_sff = null;
	if ( __source_SHEF_enabled ) {
		shef_sff = new SimpleFileFilter("shef", "SHEF .A Format File" );
		fc.addChoosableFileFilter( shef_sff );
	}
	// Add StateMod only if enabled in the configuration... 
	SimpleFileFilter statemod_sff = null;
	if ( __source_StateMod_enabled ) {
		statemod_sff = new SimpleFileFilter(
				"stm", "StateMod Time Series File" );
		fc.addChoosableFileFilter( statemod_sff );
	}
	// Add Summary always...
	SimpleFileFilter summary_sff=new SimpleFileFilter("txt","Summary File");
		fc.addChoosableFileFilter( summary_sff );
	if ( fc.showSaveDialog(this) != JFileChooser.APPROVE_OPTION ) {
		// Did not approve...
		return;
	}
	// If here, the file was selected for writing...
	String directory = fc.getSelectedFile().getParent();
	JGUIUtil.setLastFileDialogDirectory( directory );
	String path = fc.getSelectedFile().getPath();
	
	FileFilter ff = fc.getFileFilter();
	// REVISIT - need to do this cleaner and perhaps let users pick some
	// output options.  Do enforce the file extensions.
	if ( ff == datevalue_sff ) {
		export("-odatevalue", IOUtil.enforceFileExtension(path,"dv") );
	}
	/*
	else if ( ff == nwscard_sff ) {
		// Don't know for sure what extension...
		export("-onwscard", path );
	}
	*/
	else if ( ff == esp_cs_sff ) {
		export("-onwsrfsesptraceensemble",
		IOUtil.enforceFileExtension(path,"CS") );
	}
	else if ( ff == riverware_sff ) {
		// Don't know for sure what extension...
		export("-oriverware", path );
	}
	else if ( ff == shef_sff ) {
		// Don't know for sure what extension...
		export("-oshefa", path );
	}
	else if ( ff == statemod_sff ) {
		// Don't know for sure what extension...
		export("-ostatemod", path );
	}
	else if ( ff == summary_sff ) {
		export("-osummary", IOUtil.enforceFileExtension(path,"txt") );
	}
	else {
		for (int i = 0; i < nwscardFilters.length; i++) {
			if (ff == nwscardFilters[i]) {
				export("-onwscard", path);
				break;
			}
		}
	}
}

/**
Select the command and position the display at the command.
@param iline Command position (0+).
*/
private void selectCommand ( int iline )
{	__commands_JList.setSelectedIndex ( iline );
	updateStatus();
}

/**
Prompt for a NWSRFS FS5Files input name (FS5Files directory).  When selected,
update the choices.
@param reset_input_names If true, the input names will be repopulated with
values from __input_name_NWSRFS_FS5Files.
@exception Exception if there is an error.
*/
private void selectInputName_NWSRFS_FS5Files ( boolean reset_input_names )
throws Exception
{	String routine = "TSTool_JFrame.selectInputName_NWSRFS_FS5Files";
	// REVISIT SAM 2004-09-10 it does not seem like the following case ever
	// is true - need to decide whether to take out.
	if ( reset_input_names ) {
		// The NWSRFS_FS5Files input type has been selected as a change
		// from another type.  Repopululate the list if previous choices
		// exist...
		int size = __input_name_NWSRFS_FS5Files.size();
		// REVISIT - probably not needed...
		//__input_name_JComboBox.removeAll();
		// This does NOT include the leading browse, etc...
		__input_name_JComboBox.setData ( __input_name_NWSRFS_FS5Files );
	}
	// Check the item that is selected...
	String input_name = __input_name_JComboBox.getSelected();
	if ( (input_name == null) || input_name.equals(__BROWSE) ) {
		// Prompt for the name of an NWSRFS FS5Files directory...
		// Based on the file extension, set the data types and other
		// information...
		JFileChooser fc = JFileChooserFactory.createJFileChooser (
				JGUIUtil.getLastFileDialogDirectory() );
		fc.setDialogTitle("Select NWSRFS FS5Files Directory");
		fc.setFileSelectionMode (JFileChooser.DIRECTORIES_ONLY );
		if (fc.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
			// User cancelled - set the file name back to the
			// original and disable other choices...
			// Ignore programatic events on the combo boxes...
			__ignore_ItemEvent = true;
			if ( input_name != null ) {
				__input_name_JComboBox.select(null);
				__input_name_JComboBox.select(input_name);
			}
			__ignore_ItemEvent = false;
			return;
		}
		// User has chosen a directory...

		input_name = fc.getSelectedFile().getPath(); 
		JGUIUtil.setLastFileDialogDirectory (
			fc.getSelectedFile().getParent() );

		// Set the input name...

		__ignore_ItemEvent = true;
		if (	!JGUIUtil.isSimpleJComboBoxItem (__input_name_JComboBox,
				input_name, JGUIUtil.NONE, null, null ) ) {
			// Not already in so add after the browse string (files
			// are listed chronologically by select with most recent
			// at the top...
			if ( IOUtil.isUNIXMachine() ) {
				// Unix/Linux includes "Please Select...",
				// "Use Apps Defaults", and "Browse..."...
				__input_name_JComboBox.addAt ( input_name, 3 );
				__input_name_JComboBox.select ( 3 );
			}
			else {	// Windows includes only "Browse..."
				__input_name_JComboBox.addAt ( input_name, 1 );
				__input_name_JComboBox.select ( 1 );
			}
			// Insert so we have the list of files later...
			__input_name_NWSRFS_FS5Files.insertElementAt (
					input_name, 0 );
		}
		// Select the file in the input name because leaving it on
		// browse will disable the user's ability to reselect browse...
		__input_name_JComboBox.select ( null );
		__input_name_JComboBox.select ( input_name );
		// Now allow item events to occur as normal again...
		__ignore_ItemEvent = false;
	}

	__input_name_JComboBox.setEnabled ( true );

	// Open the NWSRFS_DMI instance...

	JGUIUtil.setWaitCursor ( this, true );
	try {	if ( input_name.equals(__USE_APPS_DEFAULTS) ) {
			Message.printStatus ( 1, routine,
			"Opening connection to NWSRFS FS5Files using Apps " +
			"Defaults..." );
			__nwsrfs_dmi = new NWSRFS_DMI();
		}
		else {	Message.printStatus ( 1, routine,
			"Opening connection to NWSRFS FS5Files using path \"" +
			input_name + "\"..." );
			__nwsrfs_dmi = new NWSRFS_DMI ( input_name );
		}
		__nwsrfs_dmi.open ();
		Message.printStatus ( 1, routine,
		"Opened connection to NWSRFS FS5Files." );
	}
	catch ( Exception e ) {
		Message.printWarning ( 1, routine,
		"Error opening NWSRFS FS5Files" );
		Message.printWarning ( 2, routine, e );
		__nwsrfs_dmi = null;
		JGUIUtil.setWaitCursor ( this, false );
		return;
		// REVISIT SAM 2004-09-08 need to remove from the input name
		// list if an error?
	}
	
	// Set the data types and time step based on what is available...

	inputNameChoiceClicked ();

	// Initialize with blank data vector...

	__query_TableModel = new TSTool_TS_TableModel(null);
	TSTool_TS_CellRenderer cr = new TSTool_TS_CellRenderer(
	(TSTool_TS_TableModel)__query_TableModel);
	__query_JWorksheet.setCellRenderer ( cr );
	__query_JWorksheet.setModel ( __query_TableModel );
	// Turn off columns in the table model that do not apply...
	__query_JWorksheet.removeColumn (
		((TSTool_TS_TableModel)__query_TableModel).COL_ALIAS );
	__query_JWorksheet.removeColumn (
		((TSTool_TS_TableModel)__query_TableModel).COL_SCENARIO);
	__query_JWorksheet.setColumnWidths ( cr.getColumnWidths() );
	JGUIUtil.setWaitCursor ( this, false );
}

/**
Prompt for a StateCU input name (one of several files).  When selected, update
the choices.
@param reset_input_names If true, the input names will be repopulated with
values from __input_name_StateCU.
@exception Exception if there is an error.
*/
private void selectInputName_StateCU ( boolean reset_input_names )
throws Exception
{	String routine = "TSTool_JFrame.selectInputName_StateCU";
	if ( reset_input_names ) {
		// The StateCU input type has been selected as a change from
		// another type.  Repopululate the list if previous choices
		// exist...
		int size = __input_name_StateCU.size();
		__input_name_JComboBox.setData ( __input_name_StateCU );
	}
	// Check the item that is selected...
	String input_name = __input_name_JComboBox.getSelected();
	if ( (input_name == null) || input_name.equals(__BROWSE) ) {
		// Prompt for the name of a StateCU file...
		// Based on the file extension, set the data types and other
		// information...
		JFileChooser fc = JFileChooserFactory.createJFileChooser (
				JGUIUtil.getLastFileDialogDirectory() );
		// Only handle specific files.  List alphabetically by the
		// description...
		fc.setAcceptAllFileFilterUsed ( false );
		fc.setDialogTitle ( "Select StateCU Input or Output File" );
		__input_name_StateCU_cds_FileFilter =new SimpleFileFilter("cds",
			"Crop Pattern (Yearly)");
			fc.addChoosableFileFilter(
			__input_name_StateCU_cds_FileFilter);
		__input_name_StateCU_ddh_FileFilter =new SimpleFileFilter("ddh",
			"Historical Direct Diversions - StateMod format " +
			"(Monthly)");
			fc.addChoosableFileFilter(
			__input_name_StateCU_ddh_FileFilter);
		__input_name_StateCU_ddy_FileFilter = new SimpleFileFilter(
			"ddy","Historical Direct Diversions - StateMod format "+
			"(Daily)");
			fc.addChoosableFileFilter(
			__input_name_StateCU_ddy_FileFilter);
		__input_name_StateCU_ipy_FileFilter = new SimpleFileFilter(
			"ipy", "Irrigation Practice (Yearly)");
			fc.addChoosableFileFilter(
			__input_name_StateCU_ipy_FileFilter);
		__input_name_StateCU_tsp_FileFilter = new SimpleFileFilter(
			"tsp",
			"Irrigation Practice - old TSP extension (Yearly)");
			fc.addChoosableFileFilter(
			__input_name_StateCU_tsp_FileFilter);
		__input_name_StateCU_ddc_FileFilter = new SimpleFileFilter(
			"ddc", "Irrigation Water Requirement - " +
			"StateMod format (Monthly)");
			fc.addChoosableFileFilter(
			__input_name_StateCU_ddc_FileFilter);
		__input_name_StateCU_iwr_FileFilter = new SimpleFileFilter(
			"iwr", "Irrigation Water Requirement - " +
			"StateMod format (Monthly)");
			fc.addChoosableFileFilter(
			__input_name_StateCU_iwr_FileFilter);
		__input_name_StateCU_iwrrep_FileFilter =
			new SimpleFileFilter("iwr",
			"Irrigation Water Requirement - StateCU report " +
			"(Monthly, Yearly)");
			fc.addChoosableFileFilter(
			__input_name_StateCU_iwrrep_FileFilter);
		__input_name_StateCU_frost_FileFilter = new SimpleFileFilter(
			"stm", "Frost Dates (Yearly)");
			fc.addChoosableFileFilter(
			__input_name_StateCU_frost_FileFilter);
		__input_name_StateCU_precip_FileFilter = new SimpleFileFilter(
			"stm", "Precipitation Time Series - StateMod format " +
			"(Monthly)");
			fc.addChoosableFileFilter(
			__input_name_StateCU_precip_FileFilter);
		__input_name_StateCU_temp_FileFilter=new SimpleFileFilter("stm",
			"Temperature Time Series - StateMod format " +
			"(Monthly)");
			fc.addChoosableFileFilter(
			__input_name_StateCU_temp_FileFilter);
		__input_name_StateCU_wsl_FileFilter =new SimpleFileFilter("wsl",
			"Water Supply Limited CU - StateCU report " +
			"(Monthly, Yearly)" );
			fc.addChoosableFileFilter(
			__input_name_StateCU_wsl_FileFilter);
		fc.setFileFilter(__input_name_StateCU_iwrrep_FileFilter);
		if (fc.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
			// User cancelled - set the file name back to the
			// original and disable other choices...
			if ( input_name != null ) {
				__ignore_ItemEvent = true;
				__input_name_JComboBox.select(null);
				if ( __input_name_StateCU_last != null ) {
					__input_name_JComboBox.select(
					__input_name_StateCU_last );
				}
				__ignore_ItemEvent = false;
			}
			return;
		}

		// User has chosen a file...

		input_name = fc.getSelectedFile().getPath(); 
		// Save as last selection...
		__input_name_StateCU_last = input_name;
		__input_name_FileFilter = fc.getFileFilter();
		JGUIUtil.setLastFileDialogDirectory (
			fc.getSelectedFile().getParent() );

		// Set the input name...

		__ignore_ItemEvent = true;
		if (	!JGUIUtil.isSimpleJComboBoxItem (__input_name_JComboBox,
				__BROWSE, JGUIUtil.NONE, null, null ) ) {
			// Not already in so add it at the beginning...
			__input_name_StateCU.addElement ( __BROWSE );
			__input_name_JComboBox.add ( __BROWSE );
		}
		if (	!JGUIUtil.isSimpleJComboBoxItem (__input_name_JComboBox,
				input_name, JGUIUtil.NONE, null, null ) ) {
			// Not already in so add after the browse string (files
			// are listed chronologically by select with most recent
			// at the top...
			if ( __input_name_JComboBox.getItemCount() > 1 ) {
				__input_name_JComboBox.addAt ( input_name, 1 );
				__input_name_StateCU.insertElementAt (
						input_name, 1 );
			}
			else {	__input_name_JComboBox.add ( input_name );
				__input_name_StateCU.addElement(input_name);
			}
		}
		__ignore_ItemEvent = false;
		// Select the file in the input name because leaving it on
		// browse will disable the user's ability to reselect browse...
		__input_name_JComboBox.select ( null );
		__input_name_JComboBox.select ( input_name );
	}

	__input_name_JComboBox.setEnabled ( true );

	// Set the data types and time step based on the file extension...

	__data_type_JComboBox.setEnabled ( true );
	__data_type_JComboBox.removeAll ();

	// For now, set the data type to auto.  Later, let users select the
	// data type to be selected.

	__data_type_JComboBox.removeAll ();
	__data_type_JComboBox.add ( __DATA_TYPE_AUTO );
	__data_type_JComboBox.select ( null );
	__data_type_JComboBox.select ( 0 );
	__data_type_JComboBox.setEnabled ( false );

	// Set time step appropriately...

	__time_step_JComboBox.removeAll ();
	__time_step_JComboBox.add ( __TIMESTEP_AUTO );
	__time_step_JComboBox.select ( null );
	__time_step_JComboBox.select ( 0 );
	__time_step_JComboBox.setEnabled ( false );

	// Initialize with blank data vector...

	__query_TableModel = new TSTool_TS_TableModel(null);
	TSTool_TS_CellRenderer cr = new TSTool_TS_CellRenderer(
	(TSTool_TS_TableModel)__query_TableModel);
	__query_JWorksheet.setCellRenderer ( cr );
	__query_JWorksheet.setModel ( __query_TableModel );
	// Turn off columns in the table model that do not apply...
	__query_JWorksheet.removeColumn (
		((TSTool_TS_TableModel)__query_TableModel).COL_ALIAS );
	__query_JWorksheet.removeColumn (
		((TSTool_TS_TableModel)__query_TableModel).COL_SCENARIO);
	__query_JWorksheet.removeColumn (
		((TSTool_TS_TableModel)__query_TableModel).COL_SEQUENCE);
	__query_JWorksheet.setColumnWidths ( cr.getColumnWidths() );
}

/**
Prompt for a StateModB input name (binary file name).  When selected, update
the choices.
@param reset_input_names If true, the input names will be repopulated with
values from __input_name_StateModB.
@exception Exception if there is an error.
*/
private void selectInputName_StateModB ( boolean reset_input_names )
throws Exception
{	String routine = "TSTool_JFrame.selectInputName_StateModB";
	if ( reset_input_names ) {
		// The StateModB input type has been selected as a change from
		// another type.  Repopululate the list if previous choices
		// exist...
		int size = __input_name_StateModB.size();
		// REVISIT - probably not needed...
		//__input_name_JComboBox.removeAll();
		__input_name_JComboBox.setData ( __input_name_StateModB );
	}
	// Check the item that is selected...
	String input_name = __input_name_JComboBox.getSelected();
	if ( (input_name == null) || input_name.equals(__BROWSE) ) {
		// Prompt for the name of a StateMod binary file...
		// Based on the file extension, set the data types and other
		// information...
		JFileChooser fc = JFileChooserFactory.createJFileChooser (
				JGUIUtil.getLastFileDialogDirectory() );
		fc.setDialogTitle("Select StateMod Binary Output File");
		SimpleFileFilter sff = new SimpleFileFilter("b43",
			"Diversion, Stream, Instream stations (Monthly)");
		fc.addChoosableFileFilter( sff );
		fc.addChoosableFileFilter( new SimpleFileFilter("b49",
			"Diversion, Stream, Instream stations (Daily)") );
		fc.addChoosableFileFilter( new SimpleFileFilter("b44",
			"Reservoir stations (Monthly)") );
		fc.addChoosableFileFilter( new SimpleFileFilter("b50",
			"Reservoir stations (Daily)") );
		fc.addChoosableFileFilter( new SimpleFileFilter("b42",
			"Well stations (Monthly)") );
		fc.addChoosableFileFilter( new SimpleFileFilter("b65",
			"Well stations (Daily)") );
		fc.setFileFilter(sff);
		// Only allow recognized extensions...
		fc.setAcceptAllFileFilterUsed ( false );
		if (fc.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
			// User cancelled - set the file name back to the
			// original and disable other choices...
			if ( input_name != null ) {
				__ignore_ItemEvent = true;
				__input_name_JComboBox.select(null);
				if ( __input_name_StateModB_last != null ) {
					__input_name_JComboBox.select (
					__input_name_StateModB_last );
				}
				__ignore_ItemEvent = false;
			}
			return;
		}
		// User has chosen a file...

		input_name = fc.getSelectedFile().getPath(); 
		// Save as last selection...
		__input_name_StateModB_last = input_name;
		JGUIUtil.setLastFileDialogDirectory (
			fc.getSelectedFile().getParent() );

		// Set the input name...

		__ignore_ItemEvent = true;
		if (	!JGUIUtil.isSimpleJComboBoxItem (__input_name_JComboBox,
				__BROWSE, JGUIUtil.NONE, null, null ) ) {
			// Not already in so add it at the beginning...
			__input_name_StateModB.addElement ( __BROWSE );
			__input_name_JComboBox.add ( __BROWSE );
		}
		if (	!JGUIUtil.isSimpleJComboBoxItem (__input_name_JComboBox,
				input_name, JGUIUtil.NONE, null, null ) ) {
			// Not already in so add after the browse string (files
			// are listed chronologically by select with most recent
			// at the top...
			if ( __input_name_JComboBox.getItemCount() > 1 ) {
				__input_name_JComboBox.addAt ( input_name, 1 );
				__input_name_StateModB.insertElementAt (
						input_name, 1 );
			}
			else {	__input_name_JComboBox.add ( input_name );
				__input_name_StateModB.addElement(input_name);
			}
		}
		__ignore_ItemEvent = false;
		// Select the file in the input name because leaving it on
		// browse will disable the user's ability to reselect browse...
		__input_name_JComboBox.select ( null );
		__input_name_JComboBox.select ( input_name );
	}

	__input_name_JComboBox.setEnabled ( true );

	// Set the data types and time step based on the file extension...

	__data_type_JComboBox.setEnabled ( true );
	__data_type_JComboBox.removeAll ();
	String extension = IOUtil.getFileExtension ( input_name );

	Vector data_types = null;
	int interval_base = TimeInterval.MONTH;	// Default
	int comp = StateMod_DataSet.COMP_UNKNOWN;
	if ( extension.equalsIgnoreCase("b42" ) ) {
		// Wells - monthly
		comp = StateMod_DataSet.COMP_WELL_STATIONS;
	}
	else if ( extension.equalsIgnoreCase("b43" ) ) {
		// Diversions, streamflow, instream - monthly
		// Use diversions below...
		comp = StateMod_DataSet.COMP_DIVERSION_STATIONS;
	}
	else if ( extension.equalsIgnoreCase("b44" ) ) {
		// Reservoirs - monthly
		comp = StateMod_DataSet.COMP_RESERVOIR_STATIONS;
	}
	else if ( extension.equalsIgnoreCase("b49" ) ) {
		// Diversions, streamflow, instream - daily
		// Use diversions below...
		interval_base = TimeInterval.DAY;
		comp = StateMod_DataSet.COMP_DIVERSION_STATIONS;
	}
	else if ( extension.equalsIgnoreCase("b50" ) ) {
		// Reservoir - daily
		interval_base = TimeInterval.DAY;
		comp = StateMod_DataSet.COMP_RESERVOIR_STATIONS;
	}
	else if ( extension.equalsIgnoreCase("b65" ) ) {
		// Well - daily
		interval_base = TimeInterval.DAY;
		comp = StateMod_DataSet.COMP_WELL_STATIONS;
	}
	// REVISIT SAM 2006-01-15
	// The following is not overly efficient because of a transition in
	// StateMod versions.  The version of the format is determined from the
	// file.  For older versions, this is used to return hard-coded
	// parameter lists.  For newer formats, the binary file is reopened and
	// the parameters are determined from the file.
	data_types = StateMod_Util.getTimeSeriesDataTypes (
			input_name,	// Name of binary file
			comp,	// Component from above, from file extension
			null,	// ID
			null,	// dataset
			StateMod_BTS.determineFileVersion(input_name),
			interval_base,
			false,	// Include input (only output here)
			false,	// Include input, estimated (only output here)
			true,	// Include output (what the binaries contain)
			false,	// Check availability
			true,	// Add group (if available)
			false );// add note

	// Fill data types choice...

	Message.printStatus ( 2, routine, "Setting StateModB data types..." );
	__data_type_JComboBox.setData ( data_types );
	Message.printStatus ( 2, routine,
		"Selecting the first StateModB data type..." );
	__data_type_JComboBox.select ( null );
	__data_type_JComboBox.select ( 0 );

	// Set time step appropriately...

	__time_step_JComboBox.removeAll ();
	if ( interval_base == TimeInterval.MONTH ) {
		__time_step_JComboBox.add ( __TIMESTEP_MONTH );
	}
	else if ( interval_base == TimeInterval.DAY ) {
		__time_step_JComboBox.add ( __TIMESTEP_DAY );
	}
	__time_step_JComboBox.setEnabled ( true ); // Enabled, but one visible
	__time_step_JComboBox.select ( null );
	__time_step_JComboBox.select ( 0 );

	// Initialize with blank data vector...

	__query_TableModel = new TSTool_TS_TableModel(null);
	TSTool_TS_CellRenderer cr = new TSTool_TS_CellRenderer(
	(TSTool_TS_TableModel)__query_TableModel);
	__query_JWorksheet.setCellRenderer ( cr );
	__query_JWorksheet.setModel ( __query_TableModel );
	// Turn off columns in the table model that do not apply...
	__query_JWorksheet.removeColumn (
		((TSTool_TS_TableModel)__query_TableModel).COL_ALIAS );
	__query_JWorksheet.removeColumn (
		((TSTool_TS_TableModel)__query_TableModel).COL_SCENARIO);
	__query_JWorksheet.removeColumn (
		((TSTool_TS_TableModel)__query_TableModel).COL_SEQUENCE);
	__query_JWorksheet.setColumnWidths ( cr.getColumnWidths() );
}

/**
Select on the map the features that are shown in the time series list in the
upper right of the main TSTool interface and zoom to the selected features.
@exception Exception if there is an error showing the related time series
features on the map.
*/
private void selectOnMap ()
throws Exception
{	String routine = "TSTool_JFrame.selectOnMap";
	int size = __query_JWorksheet.getRowCount();	// To size Vector
	Vector idlist = new Vector(size);
	Message.printStatus ( 1, routine,
		"Selecting and zooming to stations on map.  Please wait...");
	JGUIUtil.setWaitCursor(this, true);

	// Get the list of layers to select from, and the attributes to use...
	// First read the file with the lookup of time series to layer
	// information.

	// REVISIT SAM 2006-03-01
	// Currently read this each time because it is a small file and it
	// helps in development to not restart.  However, in the future, change
	// so that the file is read once.

	String filename = tstool.getPropValue ( "TSTool.MapLayerLookupFile" );
	if ( filename == null ) {
		Message.printWarning ( 1, routine,
		"The TSTool.MapLayerLookupFile is not defined - " +
		"cannot link map and time series." );
		return;
	}

	String full_filename = IOUtil.getPathUsingWorkingDir ( filename );
	if ( !IOUtil.fileExists(full_filename) ) {
		Message.printWarning ( 1, routine,
		"The map layer lookup file \"" + full_filename +
		"\" does not exist.  Cannot link map and time series." );
		return;
	}
	
	PropList props = new PropList ("");
	props.set ( "Delimiter=," );		// see existing prototype
	props.set ( "CommentLineIndicator=#" );	// New - skip lines that start
						// with this
	props.set ( "TrimStrings=True" );	// If true, trim strings after
						// reading.
	DataTable table = DataTable.parseFile ( full_filename, props );
	
	int tsize = 0;
	int TS_InputTypeCol_int = -1;
	int TS_DataTypeCol_int = -1;
	int TS_IntervalCol_int = -1;
	int Layer_NameCol_int = -1;
	int Layer_LocationCol_int = -1;
	int Layer_DataSourceCol_int = -1;
	int Layer_IntervalCol_int = -1;
	StringBuffer attributes = new StringBuffer();	// To put together a
							// list of attribute
							// names and values for
							// queries.
	if ( table != null ) {
		tsize = table.getNumberOfRecords();

		TS_InputTypeCol_int = table.getFieldIndex ( "TS_InputType" );
		TS_DataTypeCol_int = table.getFieldIndex ( "TS_DataType" );
		TS_IntervalCol_int = table.getFieldIndex ( "TS_Interval" );
		Layer_NameCol_int = table.getFieldIndex ( "Layer_Name" );
		Layer_LocationCol_int = table.getFieldIndex ( "Layer_Location");
		Layer_DataSourceCol_int = table.getFieldIndex (
			"Layer_DataSource");
		Message.printStatus ( 2, routine, "TimeSeriesLookup table for "+
		"map has columns...TS_InputType=" + TS_InputTypeCol_int +
		",TS_DataType=" + TS_DataTypeCol_int +
		",TS_Interval=" + TS_IntervalCol_int +
		",Layer_Name=" + Layer_NameCol_int +
		",Layer_Location=" + Layer_LocationCol_int +
		",Layer_DataSource=" + Layer_DataSourceCol_int );
	}

	Vector layerlist = new Vector();	// List of layers to match
						// features
	Vector mapidlist = new Vector();	// List of identifier attributes
						// in map data to match features
	String ts_inputtype, ts_datatype, ts_interval,
		layer_name, layer_location, layer_datasource = "",
		layer_interval = "";
						// TSTool input to match
						// against layers.
	TableRecord rec;
	if (	(TS_InputTypeCol_int >= 0) && (TS_DataTypeCol_int >= 0) &&
		(TS_IntervalCol_int >= 0) && (Layer_NameCol_int >= 0) &&
		(Layer_LocationCol_int >= 0) ) {
		// Have the necessary columns in the file to search for a
		// matching layer...
		for ( int i = 0; i < tsize; i++ ) {
			rec = table.getRecord(i);
			ts_inputtype = (String)rec.getFieldValue(
					TS_InputTypeCol_int);
			ts_datatype = (String)rec.getFieldValue(
					TS_DataTypeCol_int);
			ts_interval = (String)rec.getFieldValue(
					TS_IntervalCol_int);
			layer_name = (String)rec.getFieldValue(
					Layer_NameCol_int);
			layer_location = (String)rec.getFieldValue(
					Layer_LocationCol_int);
			if ( Layer_DataSourceCol_int >= 0 ) {
				// Also include the data source in the query
				layer_datasource = (String)rec.getFieldValue(
					Layer_DataSourceCol_int);
			}
			if ( Layer_IntervalCol_int >= 0 ) {
				// Also include the data interval in the query
				layer_interval = (String)rec.getFieldValue(
					Layer_IntervalCol_int);
			}
			// Required fields...
			if (!ts_inputtype.equalsIgnoreCase(
				__selected_input_type)){
				continue;
			}
			if ( !ts_datatype.equalsIgnoreCase(
				__selected_data_type)){
				continue;
			}
			if ( !ts_interval.equalsIgnoreCase(
				__selected_time_step)){
				continue;
			}
			// The layer matches the main input selections...
			// Save optional information that will be used for
			// record by record comparisons...
			if ( Layer_DataSourceCol_int >= 0 ) {
				layer_datasource = (String)rec.getFieldValue(
					Layer_DataSourceCol_int);
			}
			if ( Layer_IntervalCol_int >= 0 ) {
				layer_interval = (String)rec.getFieldValue(
					Layer_IntervalCol_int);
			}
			// Save the layer to search and attribute(s) to match...
			layerlist.addElement ( layer_name );
			attributes.setLength(0);
			attributes.append ( layer_location );
			if (	(Layer_DataSourceCol_int >= 0) &&
				(layer_datasource != null) &&
				!layer_datasource.equals("") ) {
				attributes.append ( "," + layer_datasource );
			}
			if (	(Layer_IntervalCol_int >= 0) &&
				(layer_interval != null) &&
				!layer_interval.equals("") ) {
				attributes.append ( "," + layer_interval );
			}
			mapidlist.addElement ( attributes.toString() );
		}
	}

	// Determine the list of features (by ID, and optionally data source
	// and interval) to select...
	// REVISIT SAM 2006-01-16
	// Need to make this more generic by using an interface to retrieve
	// important time series data?

	// Get the worksheet of interest...

	int row = -1, location_col = -1, datasource_col = -1, interval_col = -1;
	if ( __selected_input_type.equals ( __INPUT_TYPE_HydroBase )) {
		if ( __query_TableModel instanceof TSTool_TS_TableModel){
			TSTool_TS_TableModel model =
				(TSTool_TS_TableModel)__query_TableModel;
			location_col = model.COL_ID;
			datasource_col = model.COL_DATA_SOURCE;
			interval_col = model.COL_TIME_STEP;
		}
		else if ( __query_TableModel instanceof
			TSTool_HydroBase_TableModel){
			TSTool_HydroBase_TableModel model =
				(TSTool_HydroBase_TableModel)__query_TableModel;
			location_col = model.COL_ID;
			datasource_col = model.COL_DATA_SOURCE;
			interval_col = model.COL_TIME_STEP;
		}
	}

	// Get the selected rows or all if none are selected...

	int rows[] = __query_JWorksheet.getSelectedRows();

	boolean all = false;	// Only process selected rows
	if ( rows == null || rows.length == 0) {
		all = true;
		size = __query_JWorksheet.getRowCount();
	}
	else {	size = rows.length;
	}

	// Loop through either all rows or selected rows to get the identifiers
	// of interest...
	
	String id = null;
	for ( int i = 0; i < size; i++ ) {
		if (all) {
			// Process all rows...
			row = i;
		}
		else {	// Process only selected rows...
			row = rows[i];
		}
		
		// Get the station identifier from the table model row...

		if ( (row >= 0) && (location_col >= 0) ) {
			// The identifier is always matched...
			id = (String)__query_TableModel.getValueAt( row,
				location_col );
			if ( (id == null) || (id.length() <= 0) ) {
				continue;
			}
			attributes.setLength(0);
			attributes.append ( id );
			// Optional fields that if non-null should be used...
			if (	(Layer_DataSourceCol_int >= 0) &&
				(layer_datasource != null) &&
				!layer_datasource.equals("") ) {
				attributes.append ( "," + (String)
					__query_TableModel.getValueAt( row,
					datasource_col ) );
			}
			if (	(Layer_IntervalCol_int >= 0) &&
				(layer_interval != null) &&
				!layer_interval.equals("") ) {
				attributes.append ( "," + (String)
					__query_TableModel.getValueAt( row,
					interval_col ) );
			}
			// Add to the list to match...
			idlist.addElement ( attributes.toString() );
		}
	}

	// Select the features, specifying the layers and features of interest
	// corresponding to the currently selected input type, data type, and
	// interval.  Zoom to the selected shapes...
	PropList select_props = new PropList ( "search" );
	select_props.set ( "ZoomToSelected=True" );
	Message.printStatus ( 2, routine, "Selecting features..." );
	Message.printStatus ( 2, routine, "LayerList=" + layerlist );
	Message.printStatus ( 2, routine, "AttributeList=" + mapidlist );
	Message.printStatus ( 2, routine, "IDList=" + idlist );
	Vector matching_features =
		__geoview_JFrame.getGeoViewJPanel().selectLayerFeatures(
			layerlist,	// Layers to search
			mapidlist,	// Attributes to use to compare to the
					// identifiers
			idlist,		// Identifiers to find
			select_props );	// Properties to control search/select
	int matches = 0;
	if ( matching_features != null ) {
		matches = matching_features.size();
	}
	if (matches == 0) {
		Message.printWarning ( 1, routine,
		"No matching features were found in map data.\n" +
		"This may be because the location data are incomplete or\n" +
		"because no suitable data layers are being used.");
	}
	else if ( matches != idlist.size() ) {
		Message.printWarning ( 1, routine,
		"" + matches + " matching features out of " + idlist.size()+
		" records were found in map data.\n" +
		"This may be because of incomplete location data.");
	}
        //__statusJTextField.setText(
	//"Map is zoomed to selected stations.  Ready.");
	JGUIUtil.setWaitCursor(this, false);
	idlist = null;
}

/*
	else if ( __selected_input_type.equals ( __INPUT_TYPE_HydroBase )) {
		if ( __query_TableModel instanceof TSTool_HydroBase_TableModel){
			TSTool_HydroBase_TableModel model =
				(TSTool_HydroBase_TableModel)__query_TableModel;
			appendResultsToCommands ( 
				(String)__query_TableModel.getValueAt(
					row, model.COL_ID ),
				(String)__query_TableModel.getValueAt (
					row, model.COL_DATA_SOURCE),
				(String)__query_TableModel.getValueAt (
					row, model.COL_DATA_TYPE),
				(String)__query_TableModel.getValueAt (
					row, model.COL_TIME_STEP),
				"",	// No scenario
				null,	// No sequence number
				(String)__query_TableModel.getValueAt(
					row, model.COL_INPUT_TYPE),
				"", // No input name
				(String)__query_TableModel.getValueAt(
					row, model.COL_ID) + " - " +
					(String)__query_TableModel.getValueAt (
					row, model.COL_NAME),
				false );
		}
		else if (__query_TableModel instanceof
			TSTool_HydroBase_WellLevel_Day_TableModel) {
			TSTool_HydroBase_WellLevel_Day_TableModel model =
				(TSTool_HydroBase_WellLevel_Day_TableModel)
				__query_TableModel;
			appendResultsToCommands(
				(String)__query_TableModel.getValueAt(
					row, model.COL_ID ),
				(String)__query_TableModel.getValueAt (
					row, model.COL_DATA_SOURCE),
				(String)__query_TableModel.getValueAt (
					row, model.COL_DATA_TYPE),
				(String)__query_TableModel.getValueAt (
					row, model.COL_TIME_STEP),
				"",	// No scenario
				null,	// No sequence number
				(String)__query_TableModel.getValueAt(
					row, model.COL_INPUT_TYPE),
				"", // No input name
				(String)__query_TableModel.getValueAt(
					row, model.COL_ID) + " - " +
					(String)__query_TableModel.getValueAt (
					row, model.COL_NAME),
				false );			
		}
		else if ( __query_TableModel instanceof
			TSTool_HydroBase_Ag_TableModel){
			TSTool_HydroBase_Ag_TableModel model =
				(TSTool_HydroBase_Ag_TableModel)
				__query_TableModel;
			appendResultsToCommands ( 
				(String)__query_TableModel.getValueAt(
					row, model.COL_ID ),
				(String)__query_TableModel.getValueAt (
					row, model.COL_DATA_SOURCE),
				(String)__query_TableModel.getValueAt (
					row, model.COL_DATA_TYPE),
				(String)__query_TableModel.getValueAt (
					row, model.COL_TIME_STEP),
				"",	// No scenario
				null,	// No sequence number
				(String)__query_TableModel.getValueAt(
					row, model.COL_INPUT_TYPE),
				"", // No input name
				"", // No comment
				false );
		}
		else if ( __query_TableModel instanceof
			TSTool_HydroBase_AgGIS_TableModel){
			TSTool_HydroBase_AgGIS_TableModel model =
				(TSTool_HydroBase_AgGIS_TableModel)
				__query_TableModel;
			appendResultsToCommands ( 
				(String)__query_TableModel.getValueAt(
					row, model.COL_ID ),
				(String)__query_TableModel.getValueAt (
					row, model.COL_DATA_SOURCE),
				(String)__query_TableModel.getValueAt (
					row, model.COL_DATA_TYPE),
				(String)__query_TableModel.getValueAt (
					row, model.COL_TIME_STEP),
				"",	// No scenario
				null,	// No sequence number
				(String)__query_TableModel.getValueAt(
					row, model.COL_INPUT_TYPE),
				"", // No input name
				"", // No comment
				false );
		}
		else if ( __query_TableModel instanceof
			TSTool_HydroBase_WIS_TableModel){
			TSTool_HydroBase_WIS_TableModel model =
				(TSTool_HydroBase_WIS_TableModel)
				__query_TableModel;
			appendResultsToCommands ( 
				(String)__query_TableModel.getValueAt(
					row, model.COL_ID ),
				(String)__query_TableModel.getValueAt (
					row, model.COL_DATA_SOURCE),
				(String)__query_TableModel.getValueAt (
					row, model.COL_DATA_TYPE),
				(String)__query_TableModel.getValueAt (
					row, model.COL_TIME_STEP),
				(String)__query_TableModel.getValueAt (
					row, model.COL_SHEET_NAME),
				null,	// No sequence number
				(String)__query_TableModel.getValueAt(
					row, model.COL_INPUT_TYPE),
				"", // No input name
				"", // No comment
				false );
		}
	}
*/

/**
Set the commands file name.  This also will refresh any interface components
that display the commands file name.
@param commands_file_name Name of current commands file (can be null).
*/
private void setCommandsFileName ( String commands_file_name )
{	__commands_file_name = commands_file_name;
	// Update the title bar...
	updateStatus ();
}

/**
Get the selected time series from the commands list and save in the cut buffer.
*/
private void setCommandListCutBuffer ()
{	int size = 0;
	int [] selected_indices = __commands_JList.getSelectedIndices();
	if ( selected_indices != null ) {
		size = selected_indices.length;
	}
	if ( size == 0 ) {
		return;
	}

	// Allocate the cut_buffer...
	__commands_cut_buffer.removeAllElements();

	// Transfer to the cut buffer...
	for ( int i = 0; i < size; i++ ) {
		__commands_cut_buffer.addElement (
		__commands_JListModel.get(selected_indices[i]));
	}
}

/**
Indicate whether the commands have been modified.  The application title is
also updated to indicate this.
@param dirty Specify as true if the commands have been modified in some way.
*/
private void setCommandsDirty ( boolean dirty )
{	__commands_dirty = dirty;
	updateStatus ();
}

/**
Set the input filters based on the current settings.  This sets the appropriate
input filter visible since all input filters are created at startup.
*/
private void setInputFilters()
{	if (	__selected_input_type.equals(__INPUT_TYPE_HydroBase) &&
		(__input_filter_HydroBase_station_JPanel != null) &&
		(__input_filter_HydroBase_structure_JPanel != null) &&
		(__input_filter_HydroBase_structure_sfut_JPanel != null) ) {
		// Can only use the HydroBase filters if they were originally
		// set up (if HydroBase was originally available)...
		String [] hb_mt = HydroBase_Util.convertToHydroBaseMeasType(
				__selected_data_type, __selected_time_step );

		String meas_type = hb_mt[0];
		//String vax_field = hb_mt[1];
		//String time_step = hb_mt[2];
		if ( HydroBase_Util.isStationTimeSeriesDataType (
			__hbdmi, meas_type) ) {
			__selected_input_filter_JPanel =
				__input_filter_HydroBase_station_JPanel;
		}
		// Call this before the more general
		// isStructureTimeSeriesDataType() method...
		else if ( HydroBase_Util.isStructureSFUTTimeSeriesDataType (
			__hbdmi, meas_type) ) {
			__selected_input_filter_JPanel =
				__input_filter_HydroBase_structure_sfut_JPanel;
		}
		else if ( HydroBase_Util.isStructureTimeSeriesDataType (
			__hbdmi, meas_type) ) {
			__selected_input_filter_JPanel =
				__input_filter_HydroBase_structure_JPanel;
		}
		else if ((__input_filter_HydroBase_CASSCropStats_JPanel != null)
			&& HydroBase_Util.
			isAgriculturalCASSCropStatsTimeSeriesDataType (
			__hbdmi, __selected_data_type) ) {
			//Message.printStatus (2, "",
			//"Displaying CASS crop stats panel");
			__selected_input_filter_JPanel =
				__input_filter_HydroBase_CASSCropStats_JPanel;
		}
		else if ((__input_filter_HydroBase_CASSLivestockStats_JPanel
			!= null) && HydroBase_Util.
			isAgriculturalCASSLivestockStatsTimeSeriesDataType (
			__hbdmi, __selected_data_type) ) {
			//Message.printStatus (2, "",
			//"Displaying CASS livestock stats panel");
			__selected_input_filter_JPanel =
			__input_filter_HydroBase_CASSLivestockStats_JPanel;
		}
		else if ((__input_filter_HydroBase_CUPopulation_JPanel
			!= null) && HydroBase_Util.
			isCUPopulationTimeSeriesDataType (
			__hbdmi, __selected_data_type) ) {
			//Message.printStatus (2, "",
			//"Displaying CU population panel");
			__selected_input_filter_JPanel =
			__input_filter_HydroBase_CUPopulation_JPanel;
		}
		else if ( (__input_filter_HydroBase_NASS_JPanel != null) &&
			HydroBase_Util.
			isAgriculturalNASSCropStatsTimeSeriesDataType (
			__hbdmi, __selected_data_type) ) {
			//Message.printStatus (2, "",
			//"Displaying NASS agstats panel");
			__selected_input_filter_JPanel =
				__input_filter_HydroBase_NASS_JPanel;
		}
		else if ( (__input_filter_HydroBase_irrigts_JPanel != null) &&
			HydroBase_Util.isIrrigSummaryTimeSeriesDataType (
			__hbdmi, __selected_data_type) ) {
			__selected_input_filter_JPanel =
				__input_filter_HydroBase_irrigts_JPanel;
		}
		else if ((__input_filter_HydroBase_wells_JPanel != null) 
		    && HydroBase_Util.isGroundWaterWellTimeSeriesDataType(
		    __hbdmi, __selected_data_type)) {
			if (__selected_time_step.equals(__TIMESTEP_IRREGULAR)) {
				__selected_input_filter_JPanel =
					__input_filter_HydroBase_station_JPanel;
			}
			else {
				__selected_input_filter_JPanel =
					__input_filter_HydroBase_wells_JPanel;
			}
		}		
		else if ( (__input_filter_HydroBase_WIS_JPanel != null) &&
			HydroBase_Util.isWISTimeSeriesDataType (
			__hbdmi, __selected_data_type) ) {
			__selected_input_filter_JPanel =
				__input_filter_HydroBase_WIS_JPanel;
		}
		
		else {	// Generic input filter does not have anything...
			__selected_input_filter_JPanel =
				__input_filter_generic_JPanel;
		}
	}
	else if(__selected_input_type.equals(__INPUT_TYPE_MEXICO_CSMN) &&
		(__input_filter_MexicoCSMN_JPanel != null) ) {
		// Can only use the Mexico CSMN filters if they were originally
		// set up...
		__selected_input_filter_JPanel=__input_filter_MexicoCSMN_JPanel;
	}
	else if(__selected_input_type.equals(__INPUT_TYPE_NWSRFS_FS5Files) &&
		(__input_filter_NWSRFS_FS5Files_JPanel != null) ) {
		__selected_input_filter_JPanel =
		__input_filter_NWSRFS_FS5Files_JPanel;
	}
	else {	// Currently no other input types support filtering - this may
		// also be used if HydroBase input filters were not set up due
		// to a missing database connection...
		__selected_input_filter_JPanel = __input_filter_generic_JPanel;
	}
	// Now loop through the available input filter panels and set visible
	// the selected one...
	int size = __input_filter_JPanel_Vector.size();
	JPanel input_filter_JPanel;
	for ( int i = 0; i < size; i++ ) {
		input_filter_JPanel =
			(JPanel)__input_filter_JPanel_Vector.elementAt(i);
		if ( input_filter_JPanel == __selected_input_filter_JPanel ) {
			input_filter_JPanel.setVisible ( true );
		}
		else {	input_filter_JPanel.setVisible ( false );
		}
	}
}

/**
Set the __input_type_JComboBox contents based on the configuration information.
If an input type is a database, only add if a non-null connection is available.
Later, database connections may also be named, in which case more checks will
need to be done.
*/
private void setInputTypeChoices ()
{	//Message.printStatus ( 1, "", "SAMX - setting input type choices..." );
	// Ignore item events while manipulating.  This should prevent
	// unnecessary error-handling at startup.
	__ignore_ItemEvent = true;
	if ( __input_type_JComboBox.getItemCount() > 0 ) {
		__input_type_JComboBox.removeAll ();
	}
	if ( __source_DateValue_enabled ) {
		__input_type_JComboBox.add( __INPUT_TYPE_DateValue );
	}
	if (	__source_DIADvisor_enabled &&
		(__DIADvisor_dmi != null) &&
		(__DIADvisor_archive_dmi != null) ) {
		__input_type_JComboBox.add( __INPUT_TYPE_DIADvisor );
	}
	if ( __source_HydroBase_enabled ) {
		__input_type_JComboBox.add( __INPUT_TYPE_HydroBase );
	}
	if ( __source_MexicoCSMN_enabled ) {
		__input_type_JComboBox.add( __INPUT_TYPE_MEXICO_CSMN );
	}
	if ( __source_MODSIM_enabled ) {
		__input_type_JComboBox.add( __INPUT_TYPE_MODSIM );
	}
	if ( __source_NWSCard_enabled ) {
		__input_type_JComboBox.add( __INPUT_TYPE_NWSCARD );
	}
	if ( __source_NWSRFS_FS5Files_enabled ) {
		__input_type_JComboBox.add( __INPUT_TYPE_NWSRFS_FS5Files );
	}
	if ( __source_NWSRFS_ESPTraceEnsemble_enabled ) {
		__input_type_JComboBox.add(
		__INPUT_TYPE_NWSRFS_ESPTraceEnsemble );
	}
	if ( __source_RiversideDB_enabled && (__rdmi != null) ) {
		__input_type_JComboBox.add( __INPUT_TYPE_RiversideDB );
	}
	if ( __source_RiverWare_enabled ) {
		__input_type_JComboBox.add( __INPUT_TYPE_RiverWare );
	}
	// REVISIT - don't know how to parse SHEF yet...
	//if ( __source_SHEF_enabled ) {
		//__input_type_JComboBox.add( __INPUT_TYPE_SHEF );
	//}
	if ( __source_StateCU_enabled ) {
		__input_type_JComboBox.add( __INPUT_TYPE_StateCU );
	}
	if ( __source_StateMod_enabled ) {
		__input_type_JComboBox.add( __INPUT_TYPE_StateMod );
	}
	if ( __source_StateModB_enabled ) {
		__input_type_JComboBox.add( __INPUT_TYPE_StateModB );
	}
	if ( __source_USGSNWIS_enabled ) {
		__input_type_JComboBox.add( __INPUT_TYPE_USGSNWIS );
	}

	// Enable item events again so the events will cascade...

	__ignore_ItemEvent = false;

	if ( __source_HydroBase_enabled ) {
		// If enabled, select it because the users probably want it
		// as the choice...
		__input_type_JComboBox.select( null );
		__input_type_JComboBox.select( __INPUT_TYPE_HydroBase );
	}
	else {	// Select the first item in the list...
		__input_type_JComboBox.select ( null );
		__input_type_JComboBox.select ( 0 );
	}
}

/**
In the HBGUIApp base class - not sure why this is needed.
*/
public void setMenuItemState ( boolean state )
{
}

/**
Show and hide the main frame.
@param state true if showing the frame, false if hiding it.
*/
public synchronized void setVisible(boolean state)
{	if (state) {
		setLocation(50, 50);
	}
	super.setVisible(state);
}


public void setWaitCursor( boolean wait )
{
   JGUIUtil.setWaitCursor( this, wait);
}


/**
Show the Help About dialog.
*/
private void showHelpAbout ()
{	String license_type = __license_manager.getLicenseType();
	if ( license_type.equalsIgnoreCase("CDSS") ) {
		// CDSS installation...
		new HelpAboutJDialog ( this, "About TSTool",
		"TSTool - Time Series Tool\n" +
		"A component of CDSS\n" +
		IOUtil.getProgramVersion() + "\n" +
		"Copyright 1997-2006\n" +
		"Developed by Riverside Technology, inc.\n" +
		"Funded by:\n" +
		"Colorado Division of Water Resources\n" +
		"Colorado Water Conservation Board\n" +
		"Send comments about this interface to:\n" +
		"cdss@state.co.us (CDSS)\n" +
		"support@riverside.com (general)\n" );
	}
	else {	// An RTi installation...
		new HelpAboutJDialog ( this, "About TSTool",
		"TSTool - Time Series Tool\n" +
		IOUtil.getProgramVersion() + "\n" +
		"Copyright 1997-2006\n" +
		"Developed by Riverside Technology, inc.\n" +
		"Licensed to: " + __license_manager.getLicenseOwner() + "\n" +
		"Contact support at:\n" +
		"support@riverside.com\n" );
	}
}

/**
Fill in appropriate selections for the data type modifier choices based on
the currently selected input data source, data type and time step.
*/
private void timeStepChoiceClicked()
{	String rtn = "TSTool_JFrame.timeStepChoiceClicked";
	if (	(__input_type_JComboBox == null) ||
		(__data_type_JComboBox == null) ||
		(__time_step_JComboBox == null)  ) {
		// GUI still initializing...
		if ( Message.isDebugOn ) {
			Message.printDebug ( 1, rtn, "Time step has been " +
			"selected but the GUI is not initialized." );
		}
		return;
	}
	__selected_time_step = __time_step_JComboBox.getSelected();
	if ( __selected_time_step == null ) {
		// Apparently this happens when setData() or similar is called
		// on the JComboBox, and before select() is called.
		if ( Message.isDebugOn ) {
			Message.printDebug ( 1, rtn, "Time step has been " +
			"selected:  null (select is ignored)" );
		}
		return;
	}
	if ( Message.isDebugOn ) {
		Message.printStatus ( 2, rtn, "Time step has been " +
		"selected:  \"" + __selected_time_step + "\"" );
	}

	setInputFilters();
/*
	else if ( dsource.equals(__INPUT_TYPE_RiversideDB) ) {
		// Get the selected MeasType that match the choices so far
		// and add to the data type modifiers (sub-data type)...
		Vector v = null;
		String data_type = StringUtil.getToken(
			__data_type_JComboBox.getSelected(),
			" ",0,0).trim();
		try {	v = __rdmi.readMeasTypeListForTSIdent (
				".." + data_type + "." +
				__time_step_JComboBox.getSelected() + "." );
		}
		catch ( Exception e ) {
			v = null;
		}
		int size = 0;
		if ( v != null ) {
			size = v.size();
		}
		String subtype = "";
		RiversideDB_MeasType mt;
		if ( size > 0 ) {
			for ( int i = 0; i < size; i++ ) {
				mt = (RiversideDB_MeasType)v.elementAt(i);
				// Only add if not already listed.
				// Alternatively - add a "distinct" query
				subtype = mt.getSub_type();
				if ( subtype.equals("") ) {
					continue;
				}
				if (	!JGUIUtil.isSimpleJComboBoxItem(
					__data_type_mod_JComboBox,
					subtype, JGUIUtil.NONE, null, null)){
					__data_type_mod_JComboBox.add (
					subtype );
				}
			}
			if ( __data_type_mod_JComboBox.getItemCount() == 0 ) {
				__data_type_mod_JComboBox.add ( _MOD_NONE );
			}
			__data_type_mod_JComboBox.setEnabled ( true );
		}
		else {	__data_type_mod_JComboBox.setEnabled ( false );
			__data_type_mod_JComboBox.add ( _MOD_NONE );
		}
	}
*/
}

/**
Transfer all the time series from the query results to the command List.
*/
public void transferAllTSFromListToCommands ()
{	String routine = "TSTool_JFrame.transferAllTSFromListToCommands";

	int nrows = __query_TableModel.getRowCount();
	Message.printStatus ( 1, routine,
	"Transferring all time series to commands (" + nrows + " in list)..." );
	JGUIUtil.setWaitCursor ( this, true );
	int iend = nrows - 1;
	__ignore_ListSelectionEvent = true;	// To increase performance
						// during transfer...
	__ignore_ItemEvent = true;	// To increase performance
	for ( int i = 0; i < nrows; i++ ) {
		// Only force the GUI state to be updated if the last item.
		if ( i == iend ) {
			__ignore_ListSelectionEvent = false;
			__ignore_ItemEvent = false;
			transferOneTSFromListToCommand ( i, true );
		}
		else {	transferOneTSFromListToCommand ( i, false );
		}
	}
	__ignore_ListSelectionEvent = false;
	__ignore_ItemEvent = false;
	JGUIUtil.setWaitCursor ( this, false );

	Message.printStatus ( 1, routine, "Selected all time series." );
}

/**
Select a station from the query list and create a time series indentifier in the
command list.
@param row Row that is selected in the query list.  This method can be
called once for a single event or multiple times from transferAll().
@param check_gui_state If true, then the GUI state is checked after the
transfer.  This should only be set to true when transferring one command.  If
many are transferred, this slows down the GUI.
*/
private void transferOneTSFromListToCommand ( int row, boolean check_gui_state) 
{	boolean use_alias = false;
	if ( __selected_input_type.equals(__INPUT_TYPE_DateValue) ) {
		// The location (id), type, and time step uniquely identify the
		// time series, but the input_name is needed to find the data.
		// If an alias is specified, assume that it should be used
		// instead of the normal TSID.
		TSTool_TS_TableModel model =
			(TSTool_TS_TableModel)__query_TableModel;
		String alias = ((String)model.getValueAt (
			row, model.COL_ALIAS )).trim();
		if ( !alias.equals("") ) {
			// Alias is available so use it...
			use_alias = true;
		}
		// REVISIT SAM 2004-01-06 - for now don't use the alias to put
		// together the ID - there are issues to resolve in how
		// DateValueTS.readTimeSeries() handles the ID.
		use_alias = false;
		if ( use_alias ) {
			// Use the alias instead of the identifier...
			try {
			appendResultsToCommands ( alias,
			null,
			null,
			null,
			null,
			null,	// No sequence number
			(String)__query_TableModel.getValueAt(
				row, model.COL_INPUT_TYPE),
			(String)__query_TableModel.getValueAt(
				row, model.COL_INPUT_NAME), "",
			use_alias );
			}
			catch ( Exception e ) {
				Message.printWarning ( 2, "", e );
			}
		}
		else {	// Use full ID and all other fields...
			Message.printStatus ( 1, "",
			"Calling append (no alias)..." );
			appendResultsToCommands ( 
			(String)__query_TableModel.getValueAt(
				row, model.COL_ID ),
			(String)__query_TableModel.getValueAt (
				row, model.COL_DATA_SOURCE),
			(String)__query_TableModel.getValueAt (
				row, model.COL_DATA_TYPE),
			(String)__query_TableModel.getValueAt (
				row, model.COL_TIME_STEP),
			(String)__query_TableModel.getValueAt (
				row, model.COL_SCENARIO),
			(String)__query_TableModel.getValueAt (
				row, model.COL_SEQUENCE),
			(String)__query_TableModel.getValueAt(
				row, model.COL_INPUT_TYPE),
			(String)__query_TableModel.getValueAt(
				row, model.COL_INPUT_NAME), "",
			use_alias );
		}
	}
	else if ( __selected_input_type.equals ( __INPUT_TYPE_HydroBase )) {
		if ( __query_TableModel instanceof TSTool_HydroBase_TableModel){
			TSTool_HydroBase_TableModel model =
				(TSTool_HydroBase_TableModel)__query_TableModel;
			appendResultsToCommands ( 
				(String)__query_TableModel.getValueAt(
					row, model.COL_ID ),
				(String)__query_TableModel.getValueAt (
					row, model.COL_DATA_SOURCE),
				(String)__query_TableModel.getValueAt (
					row, model.COL_DATA_TYPE),
				(String)__query_TableModel.getValueAt (
					row, model.COL_TIME_STEP),
				"",	// No scenario
				null,	// No sequence number
				(String)__query_TableModel.getValueAt(
					row, model.COL_INPUT_TYPE),
				"", // No input name
				(String)__query_TableModel.getValueAt(
					row, model.COL_ID) + " - " +
					(String)__query_TableModel.getValueAt (
					row, model.COL_NAME),
				false );
		}
		else if (__query_TableModel instanceof
			TSTool_HydroBase_WellLevel_Day_TableModel) {
			TSTool_HydroBase_WellLevel_Day_TableModel model =
				(TSTool_HydroBase_WellLevel_Day_TableModel)
				__query_TableModel;
			appendResultsToCommands(
				(String)__query_TableModel.getValueAt(
					row, model.COL_ID ),
				(String)__query_TableModel.getValueAt (
					row, model.COL_DATA_SOURCE),
				(String)__query_TableModel.getValueAt (
					row, model.COL_DATA_TYPE),
				(String)__query_TableModel.getValueAt (
					row, model.COL_TIME_STEP),
				"",	// No scenario
				null,	// No sequence number
				(String)__query_TableModel.getValueAt(
					row, model.COL_INPUT_TYPE),
				"", // No input name
				(String)__query_TableModel.getValueAt(
					row, model.COL_ID) + " - " +
					(String)__query_TableModel.getValueAt (
					row, model.COL_NAME),
				false );			
		}
		else if ( __query_TableModel instanceof
			TSTool_HydroBase_Ag_TableModel){
			TSTool_HydroBase_Ag_TableModel model =
				(TSTool_HydroBase_Ag_TableModel)
				__query_TableModel;
			appendResultsToCommands ( 
				(String)__query_TableModel.getValueAt(
					row, model.COL_ID ),
				(String)__query_TableModel.getValueAt (
					row, model.COL_DATA_SOURCE),
				(String)__query_TableModel.getValueAt (
					row, model.COL_DATA_TYPE),
				(String)__query_TableModel.getValueAt (
					row, model.COL_TIME_STEP),
				"",	// No scenario
				null,	// No sequence number
				(String)__query_TableModel.getValueAt(
					row, model.COL_INPUT_TYPE),
				"", // No input name
				"", // No comment
				false );
		}
		else if ( __query_TableModel instanceof
			TSTool_HydroBase_AgGIS_TableModel){
			TSTool_HydroBase_AgGIS_TableModel model =
				(TSTool_HydroBase_AgGIS_TableModel)
				__query_TableModel;
			appendResultsToCommands ( 
				(String)__query_TableModel.getValueAt(
					row, model.COL_ID ),
				(String)__query_TableModel.getValueAt (
					row, model.COL_DATA_SOURCE),
				(String)__query_TableModel.getValueAt (
					row, model.COL_DATA_TYPE),
				(String)__query_TableModel.getValueAt (
					row, model.COL_TIME_STEP),
				"",	// No scenario
				null,	// No sequence number
				(String)__query_TableModel.getValueAt(
					row, model.COL_INPUT_TYPE),
				"", // No input name
				"", // No comment
				false );
		}
		else if ( __query_TableModel instanceof
			TSTool_HydroBase_CASSLivestockStats_TableModel){
			TSTool_HydroBase_CASSLivestockStats_TableModel model =
				(TSTool_HydroBase_CASSLivestockStats_TableModel)
				__query_TableModel;
			appendResultsToCommands ( 
				(String)__query_TableModel.getValueAt(
					row, model.COL_ID ),
				(String)__query_TableModel.getValueAt (
					row, model.COL_DATA_SOURCE),
				(String)__query_TableModel.getValueAt (
					row, model.COL_DATA_TYPE),
				(String)__query_TableModel.getValueAt (
					row, model.COL_TIME_STEP),
				"",	// No scenario
				null,	// No sequence number
				(String)__query_TableModel.getValueAt(
					row, model.COL_INPUT_TYPE),
				"", // No input name
				"", // No comment
				false );
		}
		else if ( __query_TableModel instanceof
			TSTool_HydroBase_CUPopulation_TableModel){
			TSTool_HydroBase_CUPopulation_TableModel model =
				(TSTool_HydroBase_CUPopulation_TableModel)
				__query_TableModel;
			appendResultsToCommands ( 
				(String)__query_TableModel.getValueAt(
					row, model.COL_ID ),
				(String)__query_TableModel.getValueAt (
					row, model.COL_DATA_SOURCE),
				(String)__query_TableModel.getValueAt (
					row, model.COL_DATA_TYPE),
				(String)__query_TableModel.getValueAt (
					row, model.COL_TIME_STEP),
				"",	// No scenario
				null,	// No sequence number
				(String)__query_TableModel.getValueAt(
					row, model.COL_INPUT_TYPE),
				"", // No input name
				"", // No comment
				false );
		}
		else if ( __query_TableModel instanceof
			TSTool_HydroBase_WIS_TableModel){
			TSTool_HydroBase_WIS_TableModel model =
				(TSTool_HydroBase_WIS_TableModel)
				__query_TableModel;
			appendResultsToCommands ( 
				(String)__query_TableModel.getValueAt(
					row, model.COL_ID ),
				(String)__query_TableModel.getValueAt (
					row, model.COL_DATA_SOURCE),
				(String)__query_TableModel.getValueAt (
					row, model.COL_DATA_TYPE),
				(String)__query_TableModel.getValueAt (
					row, model.COL_TIME_STEP),
				(String)__query_TableModel.getValueAt (
					row, model.COL_SHEET_NAME),
				null,	// No sequence number
				(String)__query_TableModel.getValueAt(
					row, model.COL_INPUT_TYPE),
				"", // No input name
				"", // No comment
				false );
		}
	}
	else if ( __selected_input_type.equals(
		__INPUT_TYPE_NWSRFS_ESPTraceEnsemble)) {
		// The location (id), type, and time step uniquely identify the
		// time series, but the input_name is needed to find the data.
		// If an alias is specified, assume that it should be used
		// instead of the normal TSID.
		TSTool_ESPTraceEnsemble_TableModel model =
			(TSTool_ESPTraceEnsemble_TableModel)__query_TableModel;
		String alias = ((String)__query_TableModel.getValueAt ( row,
			model.COL_ALIAS )).trim();
		boolean use_alias_in_id = false;
		if ( use_alias_in_id ) {
			// Need to figure out what to do here...
		}
		else {	// Use full ID and all other fields...
			appendResultsToCommands ( 
			(String)__query_TableModel.getValueAt(
				row, model.COL_ID ),
			(String)__query_TableModel.getValueAt (
				row, model.COL_DATA_SOURCE),
			(String)__query_TableModel.getValueAt (
				row, model.COL_DATA_TYPE),
			(String)__query_TableModel.getValueAt (
				row, model.COL_TIME_STEP),
			(String)__query_TableModel.getValueAt (
				row, model.COL_SCENARIO),
			(String)__query_TableModel.getValueAt (
				row, model.COL_SEQUENCE),
			(String)__query_TableModel.getValueAt(
				row, model.COL_INPUT_TYPE),
			(String)__query_TableModel.getValueAt(
				row, model.COL_INPUT_NAME), "",
			false );
		}
	}
	else if ( __selected_input_type.equals(__INPUT_TYPE_RiversideDB) ) {
		// The location (id), type, and time step uniquely
		// identify the time series, but the input_name is needed
		// to indicate the database.
		TSTool_RiversideDB_TableModel model =
			(TSTool_RiversideDB_TableModel)__query_TableModel;
		appendResultsToCommands ( 
		(String)__query_TableModel.getValueAt( row, model.COL_ID ),
		(String)__query_TableModel.getValueAt(row,
						model.COL_DATA_SOURCE),
		(String)__query_TableModel.getValueAt( row,model.COL_DATA_TYPE),
		(String)__query_TableModel.getValueAt( row,model.COL_TIME_STEP),
		(String)__query_TableModel.getValueAt( row, model.COL_SCENARIO),
		null,	// No sequence number
		(String)__query_TableModel.getValueAt(row,model.COL_INPUT_TYPE),
		"",
		"", false );
	}
	// The following input types use the generic TSTool_TS_TableModel with
	// no special considerations.  If the sequence number is non-blank in
	// the worksheet, it will be transferred.
	else if ( __selected_input_type.equals(__INPUT_TYPE_DIADvisor) ||
		__selected_input_type.equals(__INPUT_TYPE_MEXICO_CSMN) ||
		__selected_input_type.equals(__INPUT_TYPE_MODSIM) ||
		__selected_input_type.equals ( __INPUT_TYPE_NWSCARD ) ||
		__selected_input_type.equals ( __INPUT_TYPE_NWSRFS_FS5Files ) ||
		__selected_input_type.equals ( __INPUT_TYPE_RiverWare ) ||
		__selected_input_type.equals ( __INPUT_TYPE_StateCU ) ||
		__selected_input_type.equals ( __INPUT_TYPE_StateMod ) ||
		__selected_input_type.equals ( __INPUT_TYPE_StateModB ) ||
		__selected_input_type.equals(__INPUT_TYPE_USGSNWIS) ) {
		// The location (id), type, and time step uniquely
		// identify the time series...
		TSTool_TS_TableModel model =
			(TSTool_TS_TableModel)__query_TableModel;
		String seqnum = (String)__query_TableModel.getValueAt(
					row, model.COL_SEQUENCE );
		if ( seqnum.length() == 0 ) {
			seqnum = null;
		}
		appendResultsToCommands (
		(String)__query_TableModel.getValueAt (
					row, model.COL_ID ),
		(String)__query_TableModel.getValueAt(
					row, model.COL_DATA_SOURCE),
		(String)__query_TableModel.getValueAt(
					row, model.COL_DATA_TYPE),
		(String)__query_TableModel.getValueAt(
					row, model.COL_TIME_STEP ),
		(String)__query_TableModel.getValueAt(
					row, model.COL_SCENARIO ),
		seqnum,	// Optional sequence number
		(String)__query_TableModel.getValueAt(
					row, model.COL_INPUT_TYPE),
		(String)__query_TableModel.getValueAt(
					row, model.COL_INPUT_NAME),
		"", false );
	}
	// Check the GUI...
	if ( check_gui_state ) {
		checkGUIState();
	}
}

/**
Transfer selected time series from the query results to the command List.
*/
public void transferSelectedTSFromListToCommands ()
{	String routine = "TSTool_JFrame.transferSelectedTSFromListToCommands";

	int nrows = __query_JWorksheet.getSelectedRowCount();
	Message.printStatus ( 1, routine,
	"Transferring selected time series to commands (" + nrows +
	" in list)..." );
	int [] selected = __query_JWorksheet.getSelectedRows();
	int iend = nrows - 1;
	for ( int i = 0; i < nrows; i++ ) {
		// Only update the GUI state if transferring the last command...
		if ( i == iend ) {
			transferOneTSFromListToCommand ( selected[i], true ); 
		}
		else {	transferOneTSFromListToCommand ( selected[i], false ); 
		}
	}
	Message.printStatus ( 1, routine, "Transferred selected time series." );
}


/**
Update the command list with the result of a command edit.
The text that is selected is replaced with the command that is passed in.
This method should only be called from the editCommand() method.
@param original_command The original command Vector before any editing (null if
a new command).
@param edited_command Vector containing command after editing (currently
assumed to be in the first String since all commands are one line).
@param action __UPDATE_COMMAND or __INSERT_COMMAND.
*/
private void updateCommand (	Vector original_command,
				Vector edited_command, int action )
{	if (edited_command == null) {
		return;
	}

	int selectedIndices[] = __commands_JList.getSelectedIndices();
	int selectedSize = selectedIndices.length;	
	int size = edited_command.size();
	int index_to_view = -1;	// Index that should be visible after updates
	if ( action == __UPDATE_COMMAND ) {
		if (selectedSize > 0) {
			// Remove the selected items (going backwards so that
			// things don't get stepped on)...
			for (int i = (selectedSize - 1); i >= 0; i--) {
				__commands_JListModel.removeElementAt(
					selectedIndices[i]);
			}
			// Now add immediately after the first selected item...
			for (int i = 0; i < size; i++) {
				__commands_JListModel.insertElementAt (
					(String)edited_command.elementAt(i),
					(selectedIndices[0] + i));
			}
			index_to_view = selectedIndices[0] + size - 1;
		}
	}
	else if ( action == __INSERT_COMMAND ) {
		// Insert before the first selected item...
		if (selectedSize > 0) {
			for (int i = 0; i < size; i++) {
				__commands_JListModel.insertElementAt (
					(String)edited_command.elementAt(i),
					(selectedIndices[0] + i));
			}
			index_to_view = selectedIndices[0] + size - 1;
		}
		else {	// Insert at end of commands list.
			for (int i = 0; i < size; i++) {
				__commands_JListModel.addElement (
				(String)edited_command.elementAt(i));
			}
			index_to_view = __commands_JListModel.size() - 1;
		}
	}
	// Make sure that the list scrolls to the position that has been
	// updated...
	if ( index_to_view >= 0 ) {
		__commands_JList.ensureIndexIsVisible ( index_to_view );
	}
	// Clean up...
	selectedIndices = null;
	if ( !commandsAreEqual ( original_command, edited_command ) ) {
		setCommandsDirty(true);
	}
	checkGUIState ();
}

/**
Update the dynamic properties in __props by processing specific commands.  For
example, for a command that is about to be edited, determine the working
directory from previous settings and commands.  If the user has set the
working directory with a command, it will be recognized for the edit.
*/
private void updateDynamicProps ()
{	// Update the shared application properties to set the
	// "WorkingDir" property, which is used in the following
	// dialog...
	Vector needed_commands_Vector = new Vector();
	needed_commands_Vector.addElement ( "setWorkingDir" );
	Vector working_dir_commands_Vector =
		getCommandsAboveInsertPosition (
		needed_commands_Vector, true );
	// Always add the starting working directory to the top to
	// make sure an initial condition is set...
	working_dir_commands_Vector.insertElementAt (
		getInitialSetWorkingDirCommand(), 0 );
	TSEngine engine = new TSEngine ( __hbdmi, __rdmi, __DIADvisor_dmi,
				__DIADvisor_archive_dmi, __nwsrfs_dmi,
				__smsdmi );
	// REVISIT SAM 2004-09-07 need to make like StateDMi where only a
	// boolean is passed...
	try {	engine.processCommands ( __hbdmi,
		working_dir_commands_Vector, __props );
	}
	catch ( Exception e ) {
		// Ignore for now.
	}
	needed_commands_Vector = null;
	working_dir_commands_Vector = null;
	engine = null;
}

/**
Update the text fields at the bottom of the main interface.
@param level Message level.  If > 0 and the message is not null, call
Message.printStatus() to record a message.
@param routine Routine name used if Message.printStatus() is called.
@param message If not null, update the __message_JTextField to contain this
text.  If null, leave the contents as previously shown.  Specify "" to clear the
text.
@param status If not null, update the __status_JTextField to contain
this text.  If null, leave the contents as previously shown.  Specify "" to
clear the text.
*/
private void updateTextFields (	int level, String routine, String message,
				String status )
{	if ( (level > 0) && (message != null) ) {
		// Print a status message to the messaging system...
		Message.printStatus ( 1, routine, message );
	}
	if ( message != null ) {
		__message_JTextField.setText (message);
	}
	if ( status != null ) {
		__status_JTextField.setText (status);
	}
}

/**
Update the main status information when the list contents have changed.  This
method should be called after any change to the query, command, or time series
results list.
Interface tasks include:
<ol>
<li>	Set the title bar.
	If no commands file has been read, the title will be:
	"TSTool - no commands saved".
	If a commands file has been read but not modified, the title will be:
	"TSTool - "filename"".
	If a commands file has been read and modified, the title will be:
	"TSTool - "filename" (modified)".
	</li>
<li>	Call updateTextFields() to indicate the number of selected and total
	commands and set the general status to "Ready".
	</li>
<li>	Call checkGUIState() to reset menus, etc.  Note this should be called
	independently when the list appearance changes (selections, etc.).
	</li>
</ol>
@param check_gui_state If true, then the checkGUIState() method is also called,
which checks many interface settings.
*/
private void updateStatus ( boolean check_gui_state )
{	if ( __commands_file_name == null ) {
		setTitle ( "TSTool - no commands saved");
	}
	else {	if ( __commands_dirty ) {
			setTitle ( "TSTool - \"" + __commands_file_name +
			"\" (modified)");
		}
		else {	setTitle ( "TSTool - \"" + __commands_file_name + "\"");
		}
	}
	int selected_size = 0;
	if ( __query_results_JPanel != null ) {
		int size = 0;
		if ( __query_JWorksheet != null ) {
			try {	size=__query_JWorksheet.getRowCount();
				selected_size=__query_JWorksheet.
				getSelectedRowCount();
			}
			catch ( Exception e ) {
				// Absorb the exception in most cases - print if
				// developing to see if this issue can be
				// resolved.
				if ( Message.isDebugOn && IOUtil.testing()  ) {
					String routine =
						"TSTool_JFrame.updateStatus";
					Message.printWarning ( 3, routine,
					"For developers:  caught exception in "+
					"clearQueryList JWorksheet at setup." );
					Message.printWarning ( 3, routine, e );
				}
			}
		}
        	__query_results_JPanel.setBorder(
			BorderFactory.createTitledBorder (
			BorderFactory.createLineBorder(Color.black),
			"Time Series List (" + size +
			" time series, " + selected_size + " selected)") );
	}
	int selected_indices[] = __commands_JList.getSelectedIndices();
	selected_size = 0;
	if ( selected_indices != null ) {
		selected_size = selected_indices.length;
	}
	if ( __commands_JPanel != null ) {
        	__commands_JPanel.setBorder(
			BorderFactory.createTitledBorder (
			BorderFactory.createLineBorder(Color.black),
			"Commands (" + __commands_JListModel.size() +
			" commands, " + selected_size + " selected)") );
	}
	selected_indices = __ts_JList.getSelectedIndices();
	selected_size = 0;
	if ( selected_indices != null ) {
		selected_size = selected_indices.length;
	}
	if ( __ts_JPanel != null ) {
        	__ts_JPanel.setBorder(
			BorderFactory.createTitledBorder (
			BorderFactory.createLineBorder(Color.black),
			"Time Series Results (" +
			__ts_JListModel.size() + " time series, " +
			selected_size + " selected)") );
	}
	updateTextFields ( -1, "TSTool_JFrame.updateStatus", null,
				__STATUS_READY );
	checkGUIState ();
}

/**
Call updateStatus(true), which also checks the GUI state.
*/
private void updateStatus ()
{	updateStatus ( false );
}

/**
Handle ListSelectionListener events.
*/
public void valueChanged ( ListSelectionEvent e )
{	// For now just want to know when something changes so the GUI state
	// can be checked...
	// e.getSource() apparently does not return __commands_JList - it must
	// return a different component so don't check the object address...
	if ( __ignore_ListSelectionEvent ) {
		return;
	}
	updateStatus ();
}

public void windowActivated ( WindowEvent e )
{
}

/**
This class is listening for GeoViewFrame closing so it can gracefully handle.
*/
public void windowClosed ( WindowEvent e )
{	// Get the window that is being closed.  If it is the __geoview_JFrame,
	// then set the __geoview_JFrame instance to null.
	Component c = e.getComponent();
	if ( (__geoview_JFrame != null) && (c == __geoview_JFrame) ) {
		// GeoView...
		__geoview_JFrame = null;
		__View_MapInterface_JCheckBoxMenuItem.setSelected ( false );
	}
	else if ( !__show_main ) {
		// Running in hidden mode and the TSViewJFrame has closed so
		// close the application...
		closeClicked();
	}
}

/**
We care if the TSTool GUI is closing because we need to shut it down
gracefully.
*/
public void windowClosing ( WindowEvent e )
{	Component c = e.getComponent();
	if ( c.equals(this) ) {
		// This may cancel the close...
            	closeClicked();
	}
}

public void windowDeactivated ( WindowEvent e )
{
}

public void windowDeiconified ( WindowEvent e )
{
}

public void windowIconified ( WindowEvent e )
{
}

public void windowOpened ( WindowEvent e )
{
}

// REVISIT SAM 2006-03-02
// The JWorksheet_Listener may be deprecated or reworked in the future.  It is
// used in a limited capacity here to detect row select/deselect from the popup
// menu, so tha the status labels can be updated properly.

/**
Required by JWorksheet_Listener.
*/
public void worksheetDeselectAllRows(int timeframe)
{	if (timeframe == JWorksheet.POST_SELECTION_CHANGE) {
		updateStatus ( true );
	}
}

/**
Required by JWorksheet_Listener.
*/
public void worksheetRowAdded ( int row )
{
}

/**
Required by JWorksheet_Listener.
*/
public void worksheetRowDeleted ( int row )
{
}

/**
Required by JWorksheet_Listener.
*/
public void worksheetSelectAllRows(int timeframe)
{	if (timeframe == JWorksheet.POST_SELECTION_CHANGE) {
		updateStatus ( true );
	}
}

/**
Required by JWorksheet_Listener.
*/
public void worksheetSetRowCount ( int count )
{
}

/**
Write the current commands file list (all lines, whether selected or not) to
the specified file.  Do not prompt for header comments (and do not add).
@param prompt_for_file If true, prompt for the file name rather than using the
value that is passed.
@param file Commands file to write.
*/
private void writeCommandsFile ( String file, boolean prompt_for_file )
{	String directory = null;
	if ( prompt_for_file ) {
		JFileChooser fc = JFileChooserFactory.createJFileChooser(
			JGUIUtil.getLastFileDialogDirectory() );
		fc.setDialogTitle("Save Commands File");
		// Default name...
		File default_file = new File("commands.TSTool");
		fc.setSelectedFile ( default_file );
		fc.addChoosableFileFilter(
			new SimpleFileFilter("cmx", "TSTool Commands File") );
		SimpleFileFilter sff = 
			new SimpleFileFilter(
			"TSTool","TSTool Commands File");
		fc.setFileFilter(sff);
		if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
			directory = fc.getSelectedFile().getParent();
			String filename = fc.getSelectedFile().getName();
			file = fc.getSelectedFile().getPath();
			JGUIUtil.setLastFileDialogDirectory ( directory );
		}		
		else {	// Did not approve...
			return;
		}
	}
	// Now write the file...
	try {	PrintWriter out = new PrintWriter(new FileOutputStream(file));
		
		int size = __commands_JListModel.size();
	
		for (int i = 0; i < size; i++) {
			out.println((String)__commands_JListModel.get(i));
		}
	
		out.close();
		setCommandsDirty(false);
		setCommandsFileName ( file );

		if ( directory != null ) {
			// Set the "WorkingDir" property, which will NOT
			// contain a trailing separator...
			IOUtil.setProgramWorkingDir(directory);
			JGUIUtil.setLastFileDialogDirectory(directory);
			__props.set ("WorkingDir=" +
				IOUtil.getProgramWorkingDir());
			__initial_working_dir = __props.getValue("WorkingDir");
		}
	}
	catch ( Exception e ) {
		Message.printWarning (1, "writeCommandsFile",
			"Error writing file:\n\"" + file + "\"");
		// Leave the dirty flag the previous value.
	}
	// Update the status information...
	updateStatus();
}

// Put all test code below here....

/**
Method to run test code.
@exception Exception if there is an error.
*/
private void test ()
throws Exception
{	String routine = "TSTool_JFrame.test";
	int test_num = 9;
	Message.printStatus ( 2, routine, "Executing test " + test_num );
	if ( test_num == 0 ) {
		// Test reading and initializing SHEF data...

		// Initialize the data types and SHEF code from the NWS files...
		DataUnits.readNWSUnitsFile (
			"J:\\cdss\\develop\\apps\\tstool\\test\\NWSSystemFiles"+
			"\\DATAUNIT", true );
		DataType.readNWSDataTypeFile (
			"J:\\cdss\\develop\\apps\\tstool\\test\\NWSSystemFiles"+
			"\\DATATYPE" );
		DataType.readNWSDataTypeFile (
			"J:\\cdss\\develop\\apps\\tstool\\test\\NWSSystemFiles"+
			"\\DATATYPE2" );
		Vector datatypes = DataType.getDataTypesData();
		int size = datatypes.size();
		for ( int i = 0; i < size; i++ ) {
			Message.printDebug ( 1, "",
			"DataType[" + i + "]: " +
			((DataType)datatypes.elementAt(i)).toString() );
		}
		SHEFType.readNWSSHEFPPDBFile (
			"J:\\cdss\\develop\\apps\\tstool\\test\\NWSSystemFiles"+
			"\\SHEFPPDB", true );
		Vector sheftypes = SHEFType.getSHEFTypesData();
		size = sheftypes.size();
		for ( int i = 0; i < size; i++ ) {
			Message.printDebug ( 1, "",
			"SHEFType[" + i + "]: " +
			((SHEFType)sheftypes.elementAt(i)).toString() );
		}
		// Print out again after SHEF should be matched up...
		datatypes = DataType.getDataTypesData();
		size = datatypes.size();
		for ( int i = 0; i < size; i++ ) {
			Message.printDebug ( 1, "",
			"DataType[" + i + "]: " +
			((DataType)datatypes.elementAt(i)).toString() );
		}
		// Read test input...
		Vector tslist = DateValueTS.readTimeSeriesList (
			"J:\\cdss\\develop\\apps\\TSTool\\test\\" +
			"Data_SHEF\\TF24.mdk",
			(DateTime)null, (DateTime)null, (String)null,
			true );
		// Write the SHEF
		PropList outprops = new PropList ( "shef" );
		outprops.set ( "Duration=DH1200" );
		outprops.set ( "CreationDate=DC200311241319" );
		ShefATS.writeTimeSeriesList ( tslist, 
			"J:\\cdss\\develop\\apps\\TSTool\\test\\" +
			"Data_SHEF\\test.shef", (DateTime)null, (DateTime)null,
			(Vector)null, ShefATS.getPEForTimeSeries(tslist),
			(Vector)null, (Vector)null,
			outprops );
	}
	else if ( test_num == 1 ) {
		// Test reading multiple time series from a date value file
		// and creating an average time series...
		Vector tslist = DateValueTS.readTimeSeriesList (
			"J:\\cdss\\develop\\apps\\TSTool\\test\\" +
			"Data_EspTraceEnsemble\\CSCI.CSCI.SQIN.ACCUMULATOR",
			(DateTime)null, (DateTime)null, (String)null,
			true );
		int size = tslist.size() - 1;
		Vector tslist2 = new Vector(size);
		TS ts;
		for ( int i = 0; i < size; i++ ) {
			// Add time series that have ACFT units and "accum" in
			// the description, but not the
			// last time series, which is the average that we are
			// trying to test here.
			ts = (TS)tslist.elementAt(i);
			if (	ts.getDataUnits().equalsIgnoreCase("ACFT") &&
				(ts.getAlias().indexOf("Accum") >= 0) ) {
				tslist2.addElement ( ts );
			}
		}
		PropList props = new PropList ( "test" );
		props.set ( "TransferData=Sequentially" );
		TS newts = TSUtil.average ( tslist2, null, null, props );
		tslist2.addElement ( newts );
		// Display...
		/*
		PropList graphprops = new PropList ( "graph" );
		graphprops.set ( "InitialView", "Graph" );
		graphprops.set ( "TotalWidth", "600" );
		graphprops.set ( "TotalHeight", "400" );
		//graphprops.set ( "Title", "Summary" );
		graphprops.set ( "DisplayFont", "Courier" );
		graphprops.set ( "DisplaySize", "11" );
		graphprops.set ( "PrintFont", "Courier" );
		graphprops.set ( "PrintSize", "7" );
		graphprops.set ( "PageLength", "100" );
		new TSViewJFrame ( tslist2, graphprops );
		*/
		// Write a new DateValue file...
		DateValueTS.writeTimeSeriesList ( tslist2,
		"J:\\cdss\\develop\\apps\\TSTool\\test\\" +
		"Data_EspTraceEnsemble\\CSCI.CSCI.SQIN.ACCUMULATOR.out");
	}
	else if ( test_num == 2) {
		// Test converting to/from Julian day for a full range of dates.
		// This output is compared to the Fortran output.
		DateTime date_input = new DateTime(DateTime.PRECISION_HOUR),
			date_output;
		date_input.setYear(1900);	// Start same as "i" below.
		int jhour, jhour2;
		int inthr;
		int jday;
		int [] jul;
		int imax = 920000;
		for ( int i = 790000; i < imax; i++, date_input.addHour(1) ) {
			if ( (i%1000) == 0 ) {
				Message.printStatus ( 1, routine,
				"Processing iteration " + i + " of " + imax );
			}
			jhour = i;
			inthr = jhour%24;
			jday = (jhour - inthr)/24 + 1;
			date_output = NWSRFS_Util.mdyh1 ( jday, inthr );

			jul = NWSRFS_Util.julda (
				date_input.getMonth(), date_input.getDay(),
				date_input.getYear(), date_input.getHour() );
			jhour2 = (jul[0] - 1)*24 + jul[1];
			Message.printStatus ( 2, routine,
			"Hour " + i + " -> " + date_output +
			"  Date " + date_input + " -> " + jhour2 );
		}
	}
	else if ( test_num == 3 ) {
		// Test reading and initializing SHEF data...

		// Initialize the data types and SHEF code from the NWS files...
		DataUnits.readNWSUnitsFile (
			"J:\\cdss\\develop\\apps\\tstool\\test\\NWSSystemFiles"+
			"\\DATAUNIT", true );
		DataType.readNWSDataTypeFile (
			"J:\\cdss\\develop\\apps\\tstool\\test\\NWSSystemFiles"+
			"\\DATATYPE" );
		DataType.readNWSDataTypeFile (
			"J:\\cdss\\develop\\apps\\tstool\\test\\NWSSystemFiles"+
			"\\DATATYPE2" );
	}
	else if ( test_num == 4 ) {
		// Generate output to check a bad card file.
		DateTime date1 = new DateTime();
		date1.setYear ( 1986 );
		date1.setMonth ( 5 );
		date1.setDay ( 1 );
		DateTime date2 = new DateTime();
		date2.setYear ( 2003 );
		date2.setMonth ( 12 );
		date2.setDay ( 31 );
		int year;
		for ( DateTime date = new DateTime ( date1 );
			date.lessThanOrEqualTo(date2);
			date.addDay(1) ) {
			year = date.getYear();
			if ( year >= 2000 ) {
				year -= 2000;
			}
			else {	year -= 1900;
			}
			Message.printStatus ( 2, "",
			"04256500    " +
			StringUtil.formatString(date.getMonth(),"%02d") +
			StringUtil.formatString(year,"%02d") );
		}
	}
	else if ( test_num == 5 ) {
		// Test converting NWS MAP 6-hour time series to hydrologic
		// day.  This requires connecting to the NERFC files...
		// Query each time series corresponding to the MAP centroid
		// layer, using the AppJoinField as the ID part of the time
		// series identifier.
		String loc;	// Location to process
		String tsid;	// Time series identifier string.
		TS ts;		// 6-hour MAP time series
		DayTS dayts;	// 24-hour MAP time series
		DateTime date;	// Used to iterate through dates.
		DateTime date2;	// Last date/time in the 6-hour time series.
		int count;	// Count of values in a day (need 4).
		double sum;	// Sum of 6-hour values for day.
		double value;	// Value from the 6-hour time series.
		// REVISIT SAM 2004-10-28 TOM Replace the following to loop
		// through each feature in the MAP centroid layer...
		for ( int i = 0; i < 1; i++ ) {
		loc = "NYZ001";
		// REVISIT SAM 2004-10-28 This will only work on systems with
		// 6-hour MAP (which is most).  Additional work will need to
		// be done to support other systems.
		// REVISIT SAM 2004-10-28 - Tom replace the following with
		//tsid = loc + ".NWSRFS.MAP.6Hour~NWSRFS_FS5Files";
		tsid = loc + ".NWSRFS.MAP.6Hour~NWSRFS_FS5Files" +
			"~" + __nwsrfs_dmi.getFS5FilesLocation();
		try {	ts = __nwsrfs_dmi.readTimeSeries ( tsid,
				null, null, null, true );
		}
		catch ( Exception e ) {
			// Error getting the time series so don't process into
			// daily...
			Message.printWarning ( 2, routine,
			"Error reading time series for: \"" + tsid + "\"" );
			ts = null;
		}
		if ( ts == null ) {
			// No time series so don't process into daily...
			Message.printWarning ( 2, routine,
			"No time series read for: \"" + tsid + "\"" );
			// REVISIT Tom...
			// Somehow need to add -999 to the attribute table for
			// this location.  Otherwise, if the time series is not
			// null, do the following...
		}
		// Create a daily time series...
		dayts = new DayTS ();
		dayts.setIdentifier ( loc + ".NWSRFS.MAP.Day" );
		dayts.setDataUnits ( ts.getDataUnits() );
		dayts.setDataUnitsOriginal ( ts.getDataUnitsOriginal() );
		// Set the dates to include whatever the original data do - this
		// may result in some missing data at the ends -
		// REVISIT SAM 2004-10-28 need to decide whether to include
		// partial totals at the ends - does the processed DB always
		// have full days?
		date = new DateTime ( ts.getDate1() );
		date.setPrecision ( DateTime.PRECISION_DAY );
		dayts.setDate1 ( date );
		dayts.setDate1Original ( date );
		date = new DateTime ( ts.getDate2() );
		date.setPrecision ( DateTime.PRECISION_DAY );
		dayts.setDate2 ( date );
		dayts.setDate2Original ( date );
		dayts.allocateDataSpace();
		// Loop through the 6-hour time series and convert each
		// 18, 00, 06, 12 combination to a single 1-day sum,
		// corresponding to the hydrologic day.
		// REVISIT SAM 2004-10-28 at some point this may just call a
		// generic TSUtil.changeInterval() method, but that code is not
		// ready.
		count = 0;
		sum = 0.0;
		date2 = ts.getDate2();
		for (	date = new DateTime ( ts.getDate1());
			date.lessThanOrEqualTo(date2);
			date.addHour ( 6 ) ) {
			// Add to the sum...
			value = ts.getDataValue(date);
			// REVISIT SAM - Tom take out this status message
			// after testing out...
			Message.printStatus ( 2, "", " 6-hour " +
				date.toString() + " value=" + value );
			if ( !ts.isDataMissing(value) ) {
				// Not missing...
				sum += value;
				++count;
			}
			// else missing so don't add...
			if ( date.getHour() == 12 ) {
				if ( count == 4 ) {
					// Assign the sum to the daily time
					// series...
					// OK to use the same date since it will
					// be truncated and the hydrologic day
					// ending on YYYY-MM-DD HH is
					// YYYY-MM-DD...
					dayts.setDataValue ( date, sum );
					// REVISIT SAM - Tom take out this
					// status message after testing out...
					Message.printStatus ( 2, "", "24-hour "+
					date.toString() + " value=" + sum );
				}
				// Always reset the count and sum...
				count = 0;
				sum = 0.0;
			}
		}
		// REVISIT Tom...
		// Now add the daily values to the attribute table...
		} // End loop through centroid layer features
	}
	else if ( test_num == 7 ) {
		// Binary compare two files that are the same length...
		EndianRandomAccessFile f1 = new EndianRandomAccessFile (
			"\\cdss\\develop\\apps\\tstool\\test\\Data_DateValue\\TAMARIND.TAMARIND.RQIM.01.CS", "r", true );
		EndianRandomAccessFile f2 = new EndianRandomAccessFile (
			"\\cdss\\develop\\apps\\tstool\\test\\Data_DateValue\\TAMARIND.TAMARIND.RQIM.01.CS_OldCode", "r", true );
		byte b1, b2;
		long byte_count = 0L;
		try {	while ( true ) {
				b1 = f1.readByte();
				b2 = f2.readByte();
				++byte_count;
				if ( b1 != b2 ) {
					Message.printStatus ( 2, routine,
					"Byte [" + byte_count +
					"] not the same:  " + b1 + " " + b2 );
				}
			}
		}
		catch ( Exception e ) {
			Message.printStatus ( 2, routine,
			"End of file at byte " + byte_count );
		}
	}
	else if ( test_num == 8 ) {
		// This was used in the CDSS Flood DSS as a demonstration.
		// Create a shapefile for real-time gages showing the current
		// data value and percent of average for the date.
		// First get the map layer for the real-time gages...
		if ( __geoview_JFrame == null ) {
			Message.printWarning ( 1, routine,
			"The map must be displayed to run this test." );
			return;
		}
		Vector layerviews =
			__geoview_JFrame.getGeoViewJPanel().getLayerViews(null);
		int size = 0;
		if ( layerviews != null ) {
			size = layerviews.size();
		}
		GeoLayerView lv = null;
		DateTime now = new DateTime ( DateTime.PRECISION_MINUTE );
		now.setYear ( 2006 );
		now.setMonth ( 6 );
		now.setDay ( 14 );
		now.setHour ( 12 );
		now.setMinute ( 0 );
		DateTime today = new DateTime ( now );
		today.setPrecision ( DateTime.PRECISION_DAY );
		DateTime date = null;
		DateTime end = null;
		String abbrev, station_id;
		double value, value2, sum, percent, mean;
		int nvalues;
		TS ts, ts2;
		TSIdent tsident, tsident2;
		TableRecord rec;
		// Hard code for now to demonstrate...
		String analyzed_layer_name = "Streamflow Gages - Real-time";
		for ( int i = 0; i < size; i++ ) {
			lv = (GeoLayerView)layerviews.elementAt(i);
			if(lv.getName().equalsIgnoreCase(analyzed_layer_name)){
				break;
			}
			else {	lv = null;
			}
		}
		if ( lv == null ) {
			Message.printWarning ( 1, routine,
			"Unable to find layer to analyze \"" +
			analyzed_layer_name + "\"." );
			return;
		}
		GeoLayer lv_layer = lv.getLayer();

		// Create a new layer to contain the analyzed data.  The layer
		// needs shapes and an attribute table.

		GeoLayer layer = new GeoLayer(new PropList("Layer"));
		layer.setShapeType(GeoLayer.POINT);
		Vector shapes = new Vector();
		Vector lv_shapes = lv_layer.getShapes();
		DataTable lv_table = lv_layer.getAttributeTable();
		int lv_layer_size = 0;
		if ( lv_shapes != null ) {
			lv_layer_size = lv_shapes.size();
			Message.printStatus ( 2, routine,
			"There are " + lv_layer_size + " features to process.");
		}
		else {	Message.printWarning ( 1, routine,
			"No shapes in layer." );
			return;
		}
		GRShape shape = null;
		GRPoint pt;
		for ( int i = 0; i < lv_layer_size; i++ ) {
			// Copy the shape into the new layer...
			//Message.printStatus ( 2, routine,
			//"Processing shape [" + i + "]" );
			shape = (GRShape)lv_shapes.elementAt(i);
			if ( shape == null ) {
				// Null shape (ignore)...
				Message.printStatus ( 2, routine,
				"Shape [" + i + "] is null." );
				continue;
			}
			if ( shape.type == GRShape.UNKNOWN ) {
				continue;
			}
			//Message.printStatus ( 2, routine, "" + shape );
			pt = (GRPoint)shape;
			shapes.addElement ( new GRPoint(pt.x,pt.y) );
		}
		layer.setShapes ( shapes );

		// Create the attribute table...

		Vector fields = new Vector ();
		fields.addElement ( new TableField(
			TableField.DATA_TYPE_STRING, "Abbrev", 12 ) ); // 0
		fields.addElement ( new TableField(
			TableField.DATA_TYPE_STRING, "Station_id", 12 ) ); // 1
		fields.addElement ( new TableField(
			TableField.DATA_TYPE_STRING, "Name", 40 ) ); // 2
		fields.addElement ( new TableField(
			TableField.DATA_TYPE_DOUBLE, "Flow_cfs", 12 ) ); // 3
		fields.addElement ( new TableField(
			TableField.DATA_TYPE_STRING, "Date", 10 ) ); // 4
		fields.addElement ( new TableField(
			TableField.DATA_TYPE_DOUBLE, "Average", 10 ) ); // 5
		fields.addElement ( new TableField(
			TableField.DATA_TYPE_DOUBLE, "Perc. Ave", 14 ) ); //6
		fields.addElement ( new TableField(
			TableField.DATA_TYPE_DOUBLE, "UTMX", 13 ) ); //7
		fields.addElement ( new TableField(
			TableField.DATA_TYPE_DOUBLE, "UTMY", 13 ) ); //8
		int fields_size = fields.size();
		DataTable table = new DataTable ( fields );
		layer.setAttributeTable(table);

		// For each point in the layer, find the real-time time series
		// in memory and get its most recent value (for today).  Then
		// determine all values from the matching historical record and
		// compute an average flow.  Then calculate a percent of average
		// for the current value.  Add these items to the attribute
		// table of the layer.

		if ( __final_ts_engine == null ) {
			Message.printWarning ( 1, routine,
			"No time series available for test." );
			return;
		}
		Vector tslist = __final_ts_engine.getTimeSeriesList ( null );
		int tslist_size = 0;
		if ( tslist != null ) {
			tslist_size = tslist.size();
		}
		for ( int i = 0; i < lv_layer_size; i++ ) {
			shape = (GRShape)lv_shapes.elementAt(i);
			if ( 	(shape == null) ||
				(shape.type == GRShape.UNKNOWN) ) {
				// Null shape so ignore...
				continue;
			}
			// Copy the shape into the new layer and add a record
			// to the attribute table...
			pt = (GRPoint)lv_shapes.elementAt(i);
			rec = new TableRecord ( fields_size );
			abbrev = (String)lv_table.getFieldValue(i,"Abbrev");
			rec.addFieldValue ( abbrev );
			station_id =
				(String)lv_table.getFieldValue(i,"Station_id");
			Message.printStatus ( 2, routine,
			"Processing station abbrev=\"" + abbrev +
			"\" station_id=\"" + station_id + "\"" );
			rec.addFieldValue ( station_id );
			rec.addFieldValue ( lv_table.getFieldValue(i,"Name") );
			rec.addFieldValue ( new Double(-1.0) );	// Value CFS
			rec.addFieldValue ( "" );		// Date
			rec.addFieldValue ( new Double(-1.0) );	// Average
			rec.addFieldValue ( new Double(-1.0) );	// % of Average
			rec.addFieldValue ( new Double(pt.x) );	// UTMX
			rec.addFieldValue ( new Double(pt.y) );	// UTMY
			table.addRecord ( rec );	// May be updated below

			// Determine the most recent value of the time series...
			// First loop through the time series to find one that
			// matches the station.

			for ( int j = 0; j < tslist_size; j++ ) {
				ts = (TS)tslist.elementAt(j);
				tsident = ts.getIdentifier();
				if (	(tsident.getLocation().equalsIgnoreCase(
					abbrev) ||
					tsident.getLocation().equalsIgnoreCase(
					station_id)) &&
					tsident.getInterval().equalsIgnoreCase(
					"Irregular") ) {
					Message.printStatus ( 2, routine,
					"Found matching real-time time " +
					"series.");
					// have the matching station
					value = ts.getDataValue ( now );
					Message.printStatus ( 2, routine,
					"Data value on " + now + " is " +
					value );
					rec.setFieldValue (3,new Double(value));
					rec.setFieldValue (4,today.toString() );
					// Find the historical time series if
					// the current value is not missing...
					if ( !ts.isDataMissing(value) ) {
					for ( int k = 0; k < tslist_size; k++){
						ts2 = (TS)tslist.elementAt(k);
						tsident2 = ts2.getIdentifier();
						if (	(tsident2.getLocation().
							equalsIgnoreCase(
							abbrev) ||
							tsident2.getLocation().
							equalsIgnoreCase(
							station_id)) &&
							tsident2.getInterval().
							equalsIgnoreCase(
							"Day") ) {
							Message.printStatus ( 2,
							routine,
							"Found matching " +
							"historical time " +
							"series.");
							// Loop through entire
							// period to extract
							// daily values matching
							// today's date.
							nvalues = 0;
							sum = 0.0;
							date = new DateTime (
							ts2.getDate1() );
							date.setMonth(
							today.getMonth() );
							date.setDay(
							today.getDay() );
							end = ts2.getDate2();
							for ( ; date.
								lessThanOrEqualTo(
								end);
								date.addYear(1)){
								value2 =
								ts2.getDataValue(date);
								if ( ts2.isDataMissing(value2) ) {
									continue;
								}
								++nvalues;
								sum += value2;
							}
				
							// Compute the average.
							// REVISIT - need some
							// criteria for minimum
							// number of points.
							if ( nvalues > 10 ) {
								mean =
								sum/nvalues;
								percent =
								value/
								mean*100.0;
								rec.
								setFieldValue (
								5,new Double(
								mean));
								rec.
								setFieldValue (
								6,new Double(
								percent));
							}
							// No need to continue
							// looking for the
							// historical time
							// series...
							break;
						}
					}
					}
					// No need to continue looking for the
					// station...
					break;
				}
			} 
		}

		// Write the shapefile...

		layer.writeShapefile ( "F:\\home\\beware\\sam\\testflow",
			false, false, null );
		table.writeDelimitedFile ("F:\\home\\beware\\sam\\testflow.csv",
			",", true, null );
	}
	else if ( test_num == 9 ) {
		// Read a DateValue file and test creating a graph.
		// as the data file.
		// import RTi.TS.DateValueTS;
		// import RTi.Util.IO.PropList;
		// import RTi.GRTS.TSViewJFrame;
		TS ts = DateValueTS.readTimeSeries (
		"K:\\projects\\XR053_ArcGIS Engine Evaluation\\TimeSeriesExample\\example.dv" );
		Vector tslist = new Vector(1);
		tslist.addElement ( ts );
		PropList graphprops = new PropList ( "graph" );
		graphprops.set ( "TotalWidth", "600" );
		graphprops.set ( "TotalHeight", "400" );
		graphprops.set ( "InitialView", "Graph" );
		//graphprops.set ( "DisplayFont", "Courier" );
		//graphprops.set ( "DisplaySize", "11" );
		//graphprops.set ( "PrintFont", "Courier" );
		//graphprops.set ( "PrintSize", "7" );
		//graphprops.set ( "PageLength", "100" );
		TSViewJFrame view = new TSViewJFrame ( tslist, graphprops );
	}
}

} // end TSTool_JFrame class
