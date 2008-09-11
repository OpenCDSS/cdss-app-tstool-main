// TODO SAM 2004-08-29 Need to use HydroBase_Util preferredWDIDLength instead
// of carrying around a property - isolate all HydroBase properties to that
// package

package DWR.DMI.tstool;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
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
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;

import rti.tscommandprocessor.core.TSCommandFactory;
import rti.tscommandprocessor.core.TSCommandFileRunner;
import rti.tscommandprocessor.core.TSCommandProcessor;
import rti.tscommandprocessor.core.TSCommandProcessorListModel;
import rti.tscommandprocessor.core.TSCommandProcessorThreadRunner;
import rti.tscommandprocessor.core.TSCommandProcessorUtil;

import rti.tscommandprocessor.commands.hecdss.HecDssAPI;

import DWR.DMI.HydroBaseDMI.HydroBaseDMI;
import DWR.DMI.HydroBaseDMI.HydroBase_GUI_AgriculturalCASSCropStats_InputFilter_JPanel;
import DWR.DMI.HydroBaseDMI.HydroBase_GUI_AgriculturalCASSLivestockStats_InputFilter_JPanel;
import DWR.DMI.HydroBaseDMI.HydroBase_GUI_AgriculturalNASSCropStats_InputFilter_JPanel;
import DWR.DMI.HydroBaseDMI.HydroBase_GUI_CUPopulation_InputFilter_JPanel;
import DWR.DMI.HydroBaseDMI.HydroBase_GUI_GroundWater_InputFilter_JPanel;
import DWR.DMI.HydroBaseDMI.HydroBase_GUI_SheetNameWISFormat_InputFilter_JPanel;
import DWR.DMI.HydroBaseDMI.HydroBase_GUI_StationGeolocMeasType_InputFilter_JPanel;
import DWR.DMI.HydroBaseDMI.HydroBase_GUI_StructureGeolocStructMeasType_InputFilter_JPanel;
import DWR.DMI.HydroBaseDMI.HydroBase_GUI_StructureIrrigSummaryTS_InputFilter_JPanel;
import DWR.DMI.HydroBaseDMI.HydroBase_StationGeolocMeasType;
import DWR.DMI.HydroBaseDMI.HydroBase_Util;
import DWR.DMI.HydroBaseDMI.SelectHydroBaseJDialog;
import DWR.DMI.SatMonSysDMI.SatMonSysDMI;
import DWR.DMI.SatMonSysDMI.SatMonSys_Util;
import DWR.StateCU.StateCU_BTS;
import DWR.StateCU.StateCU_CropPatternTS;
import DWR.StateCU.StateCU_DataSet;
import DWR.StateCU.StateCU_IrrigationPracticeTS;
import DWR.StateCU.StateCU_TS;
import DWR.StateCU.StateCU_Util;
import DWR.StateMod.StateMod_BTS;
import DWR.StateMod.StateMod_DataSet;
import DWR.StateMod.StateMod_DiversionRight;
import DWR.StateMod.StateMod_InstreamFlowRight;
import DWR.StateMod.StateMod_ReservoirRight;
import DWR.StateMod.StateMod_TS;
import DWR.StateMod.StateMod_Util;
import DWR.StateMod.StateMod_WellRight;
import RTi.DMI.DMIUtil;
import RTi.DMI.DIADvisorDMI.DIADvisorDMI;
import RTi.DMI.DIADvisorDMI.DIADvisor_SensorDef;
import RTi.DMI.DIADvisorDMI.DIADvisor_SysConfig;
import RTi.DMI.DIADvisorDMI.SelectDIADvisorDialog;
import RTi.DMI.NWSRFS_DMI.NWSCardTS;
import RTi.DMI.NWSRFS_DMI.NWSRFS_ConvertJulianHour_JDialog;
import RTi.DMI.NWSRFS_DMI.NWSRFS_DMI;
import RTi.DMI.NWSRFS_DMI.NWSRFS_ESPTraceEnsemble;
import RTi.DMI.NWSRFS_DMI.NWSRFS_TS_InputFilter_JPanel;
import RTi.DMI.NWSRFS_DMI.NWSRFS_Util;
import RTi.DMI.RiversideDB_DMI.LoginJDialog;
import RTi.DMI.RiversideDB_DMI.RiversideDB_DMI;
import RTi.DMI.RiversideDB_DMI.RiversideDB_DataType;
import RTi.DMI.RiversideDB_DMI.RiversideDB_MeasType;
import RTi.DMI.RiversideDB_DMI.RiversideDB_TSProductManager_JFrame;
import RTi.GIS.GeoView.GeoLayer;
import RTi.GIS.GeoView.GeoLayerView;
import RTi.GIS.GeoView.GeoRecord;
import RTi.GIS.GeoView.GeoViewJFrame;
import RTi.GIS.GeoView.GeoViewListener;
import RTi.GR.GRLimits;
import RTi.GR.GRPoint;
import RTi.GR.GRShape;
import RTi.GRTS.TSProcessor;
import RTi.GRTS.TSPropertiesJFrame;
import RTi.GRTS.TSViewJFrame;
import RTi.TS.DateValueTS;
import RTi.TS.DayTS;
import RTi.TS.MexicoCsmnTS;
import RTi.TS.ModsimTS;
import RTi.TS.RiverWareTS;
import RTi.TS.ShefATS;
import RTi.TS.TS;
import RTi.TS.TSEnsemble;
import RTi.TS.TSIdent;
import RTi.TS.TSUtil;
import RTi.TS.UsgsNwisTS;
import RTi.Util.GUI.FindInJListJDialog;
import RTi.Util.GUI.HelpAboutJDialog;
import RTi.Util.GUI.InputFilter;
import RTi.Util.GUI.InputFilter_JPanel;
import RTi.Util.GUI.JFileChooserFactory;
import RTi.Util.GUI.JGUIUtil;
import RTi.Util.GUI.JScrollWorksheet;
import RTi.Util.GUI.JWorksheet;
import RTi.Util.GUI.JWorksheet_AbstractRowTableModel;
import RTi.Util.GUI.JWorksheet_Listener;
import RTi.Util.GUI.ReportJFrame;
import RTi.Util.GUI.ResponseJDialog;
import RTi.Util.GUI.SimpleBrowser;
import RTi.Util.GUI.SimpleFileFilter;
import RTi.Util.GUI.SimpleJButton;
import RTi.Util.GUI.SimpleJComboBox;
import RTi.Util.GUI.SimpleJMenuItem;
import RTi.Util.GUI.TextResponseJDialog;
import RTi.Util.IO.AnnotatedCommandJList;
import RTi.Util.IO.Command;
import RTi.Util.IO.CommandDiscoverable;
import RTi.Util.IO.CommandLogRecord;
import RTi.Util.IO.CommandPhaseType;
import RTi.Util.IO.CommandProcessor;
import RTi.Util.IO.CommandProcessorListener;
import RTi.Util.IO.CommandProcessorRequestResultsBean;
import RTi.Util.IO.CommandStatusProvider;
import RTi.Util.IO.CommandStatusType;
import RTi.Util.IO.CommandStatusUtil;
import RTi.Util.IO.DataType;
import RTi.Util.IO.DataUnits;
import RTi.Util.IO.EndianRandomAccessFile;
import RTi.Util.IO.FileGenerator;
import RTi.Util.IO.GenericCommand;
import RTi.Util.IO.HTMLViewer;
import RTi.Util.IO.IOUtil;
import RTi.Util.IO.LicenseManager;
import RTi.Util.IO.PrintJGUI;
import RTi.Util.IO.ProcessManager;
import RTi.Util.IO.Prop;
import RTi.Util.IO.PropList;
import RTi.Util.IO.SHEFType;
import RTi.Util.IO.UnknownCommandException;
import RTi.Util.Message.DiagnosticsJFrame;
import RTi.Util.Message.Message;
import RTi.Util.Message.MessageLogListener;
import RTi.Util.String.StringUtil;
import RTi.Util.Table.DataTable;
import RTi.Util.Table.DataTable_JFrame;
import RTi.Util.Table.TableField;
import RTi.Util.Table.TableRecord;
import RTi.Util.Time.DateTime;
import RTi.Util.Time.DateTimeBuilderJDialog;
import RTi.Util.Time.StopWatch;
import RTi.Util.Time.TimeInterval;

/**
JFrame to provide the application interface for TSTool.  This class provides the
following main functionality:
<ol>
<li>	Provide a left-to-right, top-to-bottom control of user interaction.</li>
<li>	Provide capability to interactively insert/delete/move/edit commands.</li>
<li>	Provide feedback on command initialization/discovery/run status.</li>
<li>	Execute a TSCommandProcessor to generate time series output and provide access to results.</li>
<li>	Interact with TSCommandProcessor via a list model (derived from AbstractListModel).</li>
</ol>
*/
public class TSTool_JFrame extends JFrame
implements
ActionListener,			// To handle menu selections in GUI
GeoViewListener,
ItemListener,			// To handle choice selections in GUI
JWorksheet_Listener,	// To handle query result interaction
KeyListener,			// To handle keyboard input in GUI
ListDataListener,		// To change the GUI state when commands list change
ListSelectionListener,
MessageLogListener,		// To handle interaction between log view and command list
MouseListener,
WindowListener,			// To handle window/app shutdown
CommandProcessorListener// To handle command processor progress updates
{

//================================
//  Data Members
//================================

/**
The license manager to verify the license, etc.  Use the license_set/getLicenseManager() methods
to access this data member.
*/
private LicenseManager __licenseManager = null;

/**
Support email when contacting RTi.
*/
private final String __RTiSupportEmail = "support@riverside.com";

/**
Map interface.
*/
private GeoViewJFrame __geoview_JFrame = null;

/**
Path to icons/graphics in classpath.
*/
private String __TOOL_ICON_PATH = "/DWR/DMI/tstool";

/**
Fixed-width font - courier
*/
private String __FIXED_WIDTH_FONT = "Courier";

/**
Command font - looks better than courier
*/
private String __COMMANDS_FONT = "Lucida Console";

//================================
// Query area...
//================================

/**
Available input types including enabled file and databases.
*/
private SimpleJComboBox	__input_type_JComboBox;

/**
Available input names for the input type (history of recent
choices and allow browse where necessary).
*/
private SimpleJComboBox __input_name_JComboBox;	

/**
Data type choice.
*/
private SimpleJComboBox __data_type_JComboBox;

/**
Time step for initial query.
*/
private SimpleJComboBox __time_step_JComboBox;

/**
FileFilter for current input name, checked with StateCU (and more being phased in).
Having as a data member allows selection of a filter in one place and a check
later (e.g., when the time series list is generated and displayed).
*/
private FileFilter __input_name_FileFilter = null;

/**
FileFilters used with StateCU input type.
__input_name_FileFilter will be set to one of these.
 */
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

/**
Vector of InputFilter_JPanel or JPanel (for input types
that do not support input filters).  One of these will
be visible at any time.
*/
private Vector __input_filter_JPanel_Vector = new Vector(5);

/**
The currently selected input filter JPanel, used to check
input and get the filter information for queries.
Not an InputFilter_JPanel because of the generic case.
*/
private JPanel __selected_input_filter_JPanel = null;

/**
InputFilter_JPanel for HydroBase CASS agricultural crop statistics time series.
*/
private InputFilter_JPanel __input_filter_HydroBase_CASSCropStats_JPanel = null;

/**
InputFilter_JPanel for HydroBase CASS agricultural livestock statistics
time series.
*/
private InputFilter_JPanel __input_filter_HydroBase_CASSLivestockStats_JPanel =	null;

/**
InputFilter_JPanel for HydroBase CUPopulation time series.
*/
private InputFilter_JPanel __input_filter_HydroBase_CUPopulation_JPanel = null;

/**
InputFilter_JPanel for HydroBase NASS agricultural crop statistics time series.
*/
private InputFilter_JPanel __input_filter_HydroBase_NASS_JPanel = null;

/**
InputFilter_JPanel for HydroBase structure irrig_summary_ts time series.
*/
private InputFilter_JPanel __input_filter_HydroBase_irrigts_JPanel = null;

/**
InputFilter_JPanel for HydroBase station time series.
*/
private InputFilter_JPanel __input_filter_HydroBase_station_JPanel = null;

/**
InputFilter_JPanel for HydroBase structure time series - those that do not
use SFUT.
*/
private InputFilter_JPanel __input_filter_HydroBase_structure_JPanel = null;

/**
InputFilter_JPanel for HydroBase structure time series - those that do use SFUT.
*/
private InputFilter_JPanel __input_filter_HydroBase_structure_sfut_JPanel = null;

/**
InputFilter_JPanel for HydroBase WIS time series.
*/
private InputFilter_JPanel __input_filter_HydroBase_WIS_JPanel = null;

/**
InputFilter_JPanel for Mexico CSMN stations time series.
*/
private InputFilter_JPanel __input_filter_MexicoCSMN_JPanel = null;

/**
InputFilter_JPanel for NWSRFS_FS5Files time series.
*/
private InputFilter_JPanel __input_filter_NWSRFS_FS5Files_JPanel = null;

/**
InputFilter_JPanel for groundwater wells data
*/
private InputFilter_JPanel __input_filter_HydroBase_wells_JPanel = null;

/**
JPanel for input types that do not support input filters.
*/
private InputFilter_JPanel __input_filter_generic_JPanel = null;

/**
Used to keep track of the layout position for the input filter panels because the
panel cannot be initialized until after database connections are made.
*/
private int __input_filter_y = 0;

/**
The HEC-DSS files that have been selected during the session, to allow switching
between input types but not losing the list of files.
*/
private Vector __input_name_HECDSS = new Vector();

/**
The last HEC-DSS file that was selected, to reset after cancelling a browse.
*/
private String __input_name_HECDSS_last = null;

/**
The NWSFFS FS5Files directories that have been selected during the
session, to allow switching between input types but not losing the list of files.
*/
private Vector		__input_name_NWSRFS_FS5Files = new Vector();

/**
The StateCU files that have been selected during the session, to allow switching
between input types but not losing the list of files. 
*/
private Vector		__input_name_StateCU = new Vector();

/**
The last StateCU file that was selected, to reset after cancelling a browse. 
*/
private String		__input_name_StateCU_last = null;

/**
The StateCUB files that have been selected during the session, to allow switching
between input types but not losing the list of files.
*/
private Vector __input_name_StateCUB = new Vector();

/**
The last StateCUB file that was selected, to reset after cancelling a browse.
*/
private String __input_name_StateCUB_last = null;

/**
The StateModB files that have been selected during the session, to allow switching
between input types but not losing the list of files.
*/
private Vector __input_name_StateModB = new Vector();

/**
The last StateModB file that was selected, to reset after cancelling a browse.
*/
private String __input_name_StateModB_last = null;

//TODO Evaluate usability - does not seem to work! - popup on popup
/**
Used with input name to allow a new input name to be specified.  SAM tried using
a "Browse..." choice but the problem is that an event will not be generated if the
item state does not change.
*/
private JPopupMenu __input_name_JPopupMenu = null;

/**
String to display in input name combo box to indicate that the NWSRFS_FS5Files
input type should use the current apps defaults to find the files to read.
 */
private final String __USE_APPS_DEFAULTS = "Use Apps Defaults";

/**
String to display in input name combo box to indicate that something should be
selected.  The selection will force an event to happen,
which will allow the proper settings.
*/
private final String __PLEASE_SELECT = "Please select...";

/**
String to display in input name combo box to browse for a new file.
*/
private final String __BROWSE = "Browse...";

/**
Get Time Series List button.
*/
private SimpleJButton __get_ts_list_JButton;

/**
Copy selected time series from list to commands (as time series identifiers).
*/
private SimpleJButton __CopySelectedToCommands_JButton;

/**
Copy all time series from list to commands (as time series identifiers).
*/
private SimpleJButton __CopyAllToCommands_JButton;

/**
Panel for input options.
*/
private JPanel __query_input_JPanel;

/**
Panel to hold results of time series header lists.
*/
private JPanel __query_results_JPanel;

/**
Panel for commands.
*/
private JPanel __commands_JPanel;

/**
Query results.
*/
private JWorksheet __query_JWorksheet;

/**
Table model for query results.
*/
private JWorksheet_AbstractRowTableModel __query_TableModel = null;

/**
Input type when "Get Time Series List" is selected.
*/
private String __selected_input_type = null;

/**
Input name displayed in the "Input Name: choice."
*/
private String __selected_input_name = null;

/**
Data type when "Get Time Series List" is selected - only containing
the data type used in time series identifiers.  The leading or trailing extra
information is removed after selection to simplify use.
*/
private String __selected_data_type = null;

/**
Full data type from the visible choice - this allows input types like HydroBase
to differentiate between stations and structures.
*/
private String __selected_data_type_full = null;

/**
Time step when "Get Time Series List" is selected.
*/
private String __selected_time_step = null;					

//================================
// Commands area...
//================================

/**
Annotated list to hold commands and display the command status.
*/
private AnnotatedCommandJList __commands_AnnotatedCommandJList;

/**
Commands JList, to support interaction such as selecting and popup menus.
This is a reference to the JList managed by AnnotatedList.
*/
//private JList __commands_JList;

/**
List model that maps the TSCommandProcessor Command data to the command JList.
*/
private TSCommandProcessorListModel __commands_JListModel;

/**
Run the selected commands.
*/
private SimpleJButton __Run_SelectedCommands_JButton;

/**
Run all the commands.
*/
private SimpleJButton __Run_AllCommands_JButton;

/**
Clear commands(s).
*/
private SimpleJButton __ClearCommands_JButton;
						
/**
The Vector of Command that is used with cut/copy/paste user actions.
*/
private Vector __commands_cut_buffer = new Vector(100,100);

/**
Paths to pattern files.
*/
private Vector __fill_pattern_files = 	new Vector (10,10);

/**
Time series with pattern data.
*/
private Vector __fill_pattern_ids = new Vector ( 10, 10 );

// TODO SAM 2007-11-02 Evaluate putting in the processor
/**
Indicates whether the commands have been edited without being saved.
This will trigger some changes in the UI, for example indicating that the commands
have been modified and need to be saved (or cancel) before exit.
*/
private boolean __commands_dirty = false;

/**
Name of the command file.	Don't set until selected by
the user (or on the command line).
*/
private String __command_file_name = null;	

//================================
// Results area...
//================================

/**
Tabbed panel to include all results.
*/
private JTabbedPane __results_JTabbedPane;

/**
Panel for TSEnsemble results.
*/
private JPanel __results_tsensembles_JPanel; 

/**
Final list showing in-memory time series ensemble results.
*/
private JList __results_tsensembles_JList;

/**
JList data model for final time series ensembles (basically a Vector of
time series associated with __results_tsensembles_JList).
*/
private DefaultListModel __results_tsensembles_JListModel;

/**
Popup menu for ensemble results.
*/
private JPopupMenu __results_tsensembles_JPopupMenu = null;

/**
Panel for TS results.
*/
private JPanel __results_ts_JPanel; 

/**
Final list showing in-memory time series results.
*/
private JList __results_ts_JList;

/**
JList data model for final time series (basically a Vector of
time series associated with __results_ts_JList).
*/
private DefaultListModel __results_ts_JListModel;	

/**
The command processor, which maintains a list of command objects, process
the data, and the time series results.  There is only one command processor
instance for a TSTool session and it is kept current with the application.
In the future, if threading is implemented, it may be possible to have, for
example, tabs for different command files, each with a TSCommandProcessor.
*/
private TSCommandProcessor __ts_processor = new TSCommandProcessor();

/**
Popup menu for time series results.
*/
private JPopupMenu __results_ts_JPopupMenu = null;

/**
List of results tables for viewing with an editor.
*/
private JList __results_tables_JList = null;

/**
JList data model for final time series (basically a Vector of
table identifiers associated with __results_tables_JList).
*/
private DefaultListModel __results_tables_JListModel;

/**
List of results output files for viewing with an editor.
*/
private JList __results_files_JList = null;

/**
JList data model for final time series (basically a Vector of
filenames associated with __results_files_JList).
*/
private DefaultListModel __results_files_JListModel;

//================================
// Status-area related...
//================================

/**
General status string to indicate that the GUI is ready for user input.
*/
private final String __STATUS_READY = "Ready";
/**
General status string to indicate that the user should wait for the GUI to finish a task.
*/
private final String __STATUS_BUSY = "Wait";
/**
General status string to indicate that command processing is being cancelled.
*/
private final String __STATUS_CANCELING = "Canceling";
/**
General status string to indicate that command processing has been cancelled.
*/
private final String __STATUS_CANCELLED = "Cancelled";

/**
Message area text field (e.g., "Processing commands...") - long and left-most.
*/
private JTextField __message_JTextField;

/**
Progress bar to show progress of running commands in processor.
*/
private JProgressBar __processor_JProgressBar;
/**
Progress bar to show progress of running a specific commands.
*/
private JProgressBar __command_JProgressBar;
/**
Status area text field (e.g., "READY", "WAIT") - small and right-most.
*/
private JTextField __status_JTextField;		

//================================
// General...
//================================

/**
TSTool application properties.  These control how the GUI behaves before
commands are run.  For example, if the output period is set in the
setOutputPeriod_Dialog, then the output period string properties can be
passed back to this main GUI.  Then, subsequent edit dialogs can
use the information to display appropriate properties or instructions.
It may be somewhat difficult to use this because the properties are singular but
properties can change during a run.
*/
private PropList __props;

/**
The initial working directory corresponding to a command file read/write or
File... Set Working Dir.  This is used when processing the list
of setWorkingDir() commands passed to command editors. Without the initial working
directory, relative changes in the working directory will
result in an inaccurate initial state.
*/
private String __initial_working_dir = "";

/**
The last directory selected with Run...Command File, to run an external commands file.
*/
private String __Dir_LastExternalCommandFileRun = null;

/**
The last directory selected with Run...Command File, to run an external commands file.
*/
private String __Dir_LastCommandFileOpened = null;

/**
Lets the checkGUIState() method avoid checking for many null components during startup.
*/
private boolean __gui_initialized = false;

/**
Use this to temporarily ignore item action performed events, necessary when
programatically modifying the contents of combo boxes.
*/
private boolean __ignore_ActionEvent = false;

// TODO SAM 2007-10-19 Evaluate whether still needed with new list model.
/**
Use this to temporarily ignore item listener events, necessary when
programatically modifying the contents of combo boxes.
*/
private boolean __ignore_ItemEvent = false;

/**
This is used in some cases to disable updates during bulk operations to
the commands list.
*/
private boolean __ignore_ListSelectionEvent = false;

/**
Indicates which data sources are enabled, initialized to defaults.
*/
private boolean	__source_ColoradoSMS_enabled = false,
		__source_DateValue_enabled = true,
		__source_DIADvisor_enabled = false,
		__source_HECDSS_enabled = true,
		__source_HydroBase_enabled = true,
		__source_MexicoCSMN_enabled = false,
		__source_MODSIM_enabled = true,
		__source_NDFD_enabled = true,
		__source_NWSCard_enabled = true,
		__source_NWSRFS_FS5Files_enabled = false,
		__source_NWSRFS_ESPTraceEnsemble_enabled = false,
		__source_RiversideDB_enabled = false,
		__source_RiverWare_enabled = true,
		__source_SHEF_enabled = true,
		__source_StateCU_enabled = true,
		__source_StateCUB_enabled = true,
		__source_StateMod_enabled = true,
		__source_StateModB_enabled = true,
		__source_USGSNWIS_enabled = true;

/**
Indicates if the main GUI should be shown.  This is normally true but if false
then only graph windows will be shown, and when closed the app will close.  This
is used when calling TSTool from other software (e.g., to provide graphing
capabilities to GIS applications).
*/
//private boolean __show_main = true;

/**
RiversideDB_DMI object for RiversideDB input type, opened via TSTool.cfg information
and the RiversideDB select dialog, provided to the processor as the initial
RiversideDB DMI instance.
*/
private RiversideDB_DMI __rdmi = null;

/**
DiadVisor_DMI objects for DiadVisor input type, opened via TSTool.cfg information
and the DiadVisor select dialog, provided to the processor as the initial
DiadVisor DMI instance.
*/
private DIADvisorDMI __DIADvisor_dmi = null;	// Operational DB
private DIADvisorDMI __DIADvisor_archive_dmi = null;	// Archive databases.

/**
HydroBaseDMI object for HydroBase input type, opened via TSTool.cfg information
and the HydroBase select dialog, provided to the processor as the initial
HydroBase DMI instance.
*/
private HydroBaseDMI __hbdmi = null;

/**
SatMonSysDMI object for ColoradoSMS input type, opened via TSTool.cfg information
and the HydroBase select dialog, provided to the processor as the initial
HydroBase DMI instance.
*/
private SatMonSysDMI __smsdmi = null;		

/**
NWSRFS_DMI object for NWSRFS_FS5Files input type, opened via TSTool.cfg information
and the NWSRFS FS5Files directory select dialog, provided to the processor as the initial
NWSRFSS DMI instance.
*/
private NWSRFS_DMI __nwsrfs_dmi = null;

//================================
// Main toolbar
//================================

private SimpleJButton
    __toolbarNew_JButton = null,
    __toolbarOpen_JButton = null,
    __toolbarSave_JButton = null;

//================================
// Menu items and strings, list in order of the menus...
//================================

private JPopupMenu
	__Commands_JPopupMenu;

private JMenuItem
	__CommandsPopup_Edit_CommandWithErrorChecking_JMenuItem,

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
	
	__CommandsPopup_CancelCommandProcessing_JMenuItem,

	__CommandsPopup_ConvertSelectedCommandsToComments_JMenuItem,
	__CommandsPopup_ConvertSelectedCommandsFromComments_JMenuItem,
	
	__CommandsPopup_ShowCommandStatus_JMenuItem;

// File menu...

private JMenu
	__File_JMenu = null,
	__File_Open_JMenu = null;
		private JMenuItem
		__File_Open_CommandFile_JMenuItem = null,
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
	__Edit_CommandFile_JMenuItem = null,
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
	__Commands_Create_CreateFromList_JMenuItem,
    __Commands_Create_ResequenceTimeSeriesData_JMenuItem,
	__Commands_Create_TS_ChangeInterval_JMenuItem,
	__Commands_Create_TS_Copy_JMenuItem,
	__Commands_Create_TS_Disaggregate_JMenuItem,
	__Commands_Create_TS_NewDayTSFromMonthAndDayTS_JMenuItem,
	__Commands_Create_TS_NewEndOfMonthTSFromDayTS_JMenuItem,
	__Commands_Create_TS_NewPatternTimeSeries_JMenuItem,
	__Commands_Create_TS_NewStatisticTimeSeries_JMenuItem,
	__Commands_Create_TS_NewStatisticYearTS_JMenuItem,
	__Commands_Create_TS_NewTimeSeries_JMenuItem,
	__Commands_Create_TS_Normalize_JMenuItem,
	__Commands_Create_TS_RelativeDiff_JMenuItem;

// Commands (Convert TSID to Read Command)...

JMenu
	__Commands_ConvertTSIDToReadCommand_JMenu = null;
JMenuItem
	__Commands_ConvertTSIDTo_ReadTimeSeries_JMenuItem = null,
	__Commands_ConvertTSIDTo_ReadDateValue_JMenuItem = null,
    __Commands_ConvertTSIDTo_ReadDelimitedFile_JMenuItem = null,
	__Commands_ConvertTSIDTo_ReadHydroBase_JMenuItem = null,
	__Commands_ConvertTSIDTo_ReadMODSIM_JMenuItem = null,
	__Commands_ConvertTSIDTo_ReadNwsCard_JMenuItem = null,
	__Commands_ConvertTSIDTo_ReadNWSRFSFS5Files_JMenuItem = null,
	__Commands_ConvertTSIDTo_ReadRiverWare_JMenuItem = null,
	__Commands_ConvertTSIDTo_ReadStateMod_JMenuItem = null,
	__Commands_ConvertTSIDTo_ReadStateModB_JMenuItem = null,
	__Commands_ConvertTSIDTo_ReadUsgsNwis_JMenuItem = null;

// Commands (Read Time Series)...

JMenu
	__Commands_ReadTimeSeries_JMenu = null;
JMenuItem
	//--
	// NOT TS Alias = commands...
	__Commands_Read_ReadDateValue_JMenuItem,
    __Commands_Read_ReadDelimitedFile_JMenuItem,
    __Commands_Read_ReadHECDSS_JMenuItem,
	__Commands_Read_ReadHydroBase_JMenuItem,
	__Commands_Read_ReadMODSIM_JMenuItem,
	__Commands_Read_ReadNwsCard_JMenuItem,
	__Commands_Read_ReadNWSRFSFS5Files_JMenuItem,
	__Commands_Read_ReadStateCU_JMenuItem,
	__Commands_Read_ReadStateCUB_JMenuItem,
	__Commands_Read_ReadStateMod_JMenuItem,
	__Commands_Read_ReadStateModB_JMenuItem,
	__Commands_Read_StateModMax_JMenuItem,
	// TS Alias = commands...
	__Commands_Read_TS_ReadDateValue_JMenuItem,
	__Commands_Read_TS_ReadHydroBase_JMenuItem,
	__Commands_Read_TS_ReadMODSIM_JMenuItem,
	__Commands_Read_TS_ReadNDFD_JMenuItem,
	__Commands_Read_TS_ReadNwsCard_JMenuItem,
	__Commands_Read_TS_ReadNWSRFSFS5Files_JMenuItem,
	__Commands_Read_TS_ReadRiverWare_JMenuItem,
	// FIXME SAM 2008-08-21 Enable when read but no need to show users now
	//__Commands_Read_TS_ReadStateMod_JMenuItem,
	//__Commands_Read_TS_ReadStateModB_JMenuItem,
	__Commands_Read_TS_ReadUsgsNwis_JMenuItem,

	__Commands_Read_SetIncludeMissingTS_JMenuItem,
	__Commands_Read_SetInputPeriod_JMenuItem;

	// Commands...Fill Time Series....
JMenu
	__Commands_FillTimeSeries_JMenu = null;
JMenuItem
	__Commands_Fill_FillConstant_JMenuItem,
	__Commands_Fill_FillDayTSFrom2MonthTSAnd1DayTS_JMenuItem,
	__Commands_Fill_FillFromTS_JMenuItem,
	__Commands_Fill_FillHistMonthAverage_JMenuItem,
	__Commands_Fill_FillHistYearAverage_JMenuItem,
	__Commands_Fill_FillInterpolate_JMenuItem,
	__Commands_Fill_FillMixedStation_JMenuItem,
	__Commands_Fill_FillMOVE1_JMenuItem,
	__Commands_Fill_FillMOVE2_JMenuItem,
	__Commands_Fill_FillPattern_JMenuItem,
	__Commands_Fill_FillProrate_JMenuItem,
	__Commands_Fill_FillRegression_JMenuItem,
	__Commands_Fill_FillRepeat_JMenuItem,
	__Commands_Fill_FillUsingDiversionComments_JMenuItem,

	__Commands_Fill_SetAutoExtendPeriod_JMenuItem,
	__Commands_Fill_SetAveragePeriod_JMenuItem,
	__Commands_Fill_SetIgnoreLEZero_JMenuItem,
	__Commands_Fill_SetPatternFile_JMenuItem;

	// Commands...Set Time Series....
JMenu
	__Commands_SetTimeSeries_JMenu = null;
JMenuItem
	//--
	__Commands_Set_ReplaceValue_JMenuItem,
	//--
	__Commands_Set_SetConstant_JMenuItem,
	__Commands_Set_SetDataValue_JMenuItem,
    __Commands_Set_SetFromTS_JMenuItem,
	__Commands_Set_SetMax_JMenuItem,
	__Commands_Set_SetToMin_JMenuItem,
    __Commands_Set_SetTimeSeriesProperty_JMenuItem;

// Commands...Manipulate Time Series....
JMenu
	__Commands_ManipulateTimeSeries_JMenu = null;
JMenuItem
	__Commands_Manipulate_Add_JMenuItem,
	__Commands_Manipulate_AddConstant_JMenuItem,
	__Commands_Manipulate_AdjustExtremes_JMenuItem,
	__Commands_Manipulate_ARMA_JMenuItem,
	__Commands_Manipulate_Blend_JMenuItem,
    __Commands_Manipulate_ChangePeriod_JMenuItem,
	__Commands_Manipulate_ConvertDataUnits_JMenuItem,
	__Commands_Manipulate_Cumulate_JMenuItem,
	__Commands_Manipulate_Divide_JMenuItem,
	__Commands_Manipulate_Free_JMenuItem,
	__Commands_Manipulate_Multiply_JMenuItem,
	__Commands_Manipulate_RunningAverage_JMenuItem,
	__Commands_Manipulate_Scale_JMenuItem,
	__Commands_Manipulate_ShiftTimeByInterval_JMenuItem,
	__Commands_Manipulate_Subtract_JMenuItem;

// Commands...Analyze Time Series....
JMenu
	__Commands_AnalyzeTimeSeries_JMenu = null;
JMenuItem
	__Commands_Analyze_AnalyzePattern_JMenuItem = null,
	__Commands_Analyze_CompareTimeSeries_JMenuItem = null,
	__Commands_Analyze_ComputeErrorTimeSeries_JMenuItem = null,

	__Commands_Analyze_NewDataTest_JMenuItem = null,
	__Commands_Analyze_ReadDataTestFromRiversideDB_JMenuItem = null,
	__Commands_Analyze_RunDataTests_JMenuItem = null,
	__Commands_Analyze_ProcessDataTestResults_JMenuItem = null;

// Commands...Models....
JMenu
	__Commands_Models_JMenu = null;
JMenuItem
	__Commands_Models_LagK_JMenuItem = null;

// Commands...Output Time Series....
JMenu
	__Commands_OutputTimeSeries_JMenu = null;
JMenuItem
	__Commands_Output_DeselectTimeSeries_JMenuItem,
	__Commands_Output_SetOutputDetailedHeaders_JMenuItem,
	__Commands_Output_SetOutputPeriod_JMenuItem,
	__Commands_Output_SetOutputYearType_JMenuItem,
	__Commands_Output_SelectTimeSeries_JMenuItem,
	__Commands_Output_SortTimeSeries_JMenuItem,
	__Commands_Output_WriteDateValue_JMenuItem,
	__Commands_Output_WriteNwsCard_JMenuItem,
	__Commands_Output_WriteRiverWare_JMenuItem,
    __Commands_Output_WriteSHEF_JMenuItem,
	__Commands_Output_WriteStateCU_JMenuItem,
	__Commands_Output_WriteStateMod_JMenuItem,
	__Commands_Output_WriteSummary_JMenuItem,

    __Commands_Output_ProcessTSProduct_JMenuItem;

//Commands...Ensemble Processing...

JMenu
    __Commands_Ensemble_JMenu = null;
JMenuItem
    __Commands_Ensemble_CreateEnsemble_JMenuItem,
    __Commands_Ensemble_CopyEnsemble_JMenuItem,
    __Commands_Ensemble_ReadNwsrfsEspTraceEnsemble_JMenuItem,
    __Commands_Ensemble_TS_NewStatisticTimeSeriesFromEnsemble_JMenuItem,
    __Commands_Ensemble_TS_WeightTraces_JMenuItem,
    __Commands_Ensemble_WriteNWSRFSESPTraceEnsemble_JMenuItem;

// Commands (Table)...

JMenu
    __Commands_Table_JMenu = null;
JMenuItem
    __Commands_Table_ReadTableFromDelimitedFile_JMenuItem;

// Commands (General)...

JMenu
	__Commands_General_Logging_JMenu = null;
JMenuItem
	__Commands_General_Logging_StartLog_JMenuItem = null,
	__Commands_General_Logging_SetDebugLevel_JMenuItem = null,
	__Commands_General_Logging_SetWarningLevel_JMenuItem = null;

JMenu
__Commands_General_CheckingResults_JMenu = null;
JMenuItem
__Commands_General_CheckingResults_OpenCheckFile_JMenuItem = null;

JMenu
    __Commands_General_Comments_JMenu = null;
JMenuItem
	__Commands_General_Comments_Comment_JMenuItem = null,
	__Commands_General_Comments_ReadOnlyComment_JMenuItem = null,
	__Commands_General_Comments_StartComment_JMenuItem = null,
	__Commands_General_Comments_EndComment_JMenuItem = null;

JMenu
    __Commands_General_FileHandling_JMenu = null;
JMenuItem
    __Commands_General_FileHandling_FTPGet_JMenuItem = null,
    __Commands_General_FileHandling_RemoveFile_JMenuItem = null;

JMenu
    __Commands_General_Running_JMenu = null;
JMenuItem
    __Commands_General_Running_SetProperty_JMenuItem = null,
    __Commands_General_Running_SetPropertyFromNwsrfsAppDefault_JMenuItem = null,
	__Commands_General_Running_RunCommands_JMenuItem = null,
	__Commands_General_Running_RunProgram_JMenuItem = null,
    __Commands_General_Running_RunPython_JMenuItem = null,
    __Commands_General_Running_Exit_JMenuItem = null,
    __Commands_General_Running_SetWorkingDir_JMenuItem = null;

//Commands (General - Test Processing)...
JMenu
    __Commands_General_TestProcessing_JMenu = null;
JMenuItem
    __Commands_General_TestProcessing_CompareFiles_JMenuItem = null,
    __Commands_General_TestProcessing_WriteProperty_JMenuItem = null,
    __Commands_General_TestProcessing_WriteTimeSeriesProperty_JMenuItem = null,
    //-- separator ---
    __Commands_General_TestProcessing_CreateRegressionTestCommandFile_JMenuItem = null,
    __Commands_General_TestProcessing_StartRegressionTestResultsReport_JMenuItem = null,
    //-- separator ---
    __Commands_General_TestProcessing_TestCommand_JMenuItem = null;

// Commands (HydroBase)...
JMenu
	__Commands_HydroBase_JMenu = null;
JMenuItem
	__Commands_HydroBase_OpenHydroBase_JMenuItem;

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
	__Run_CancelCommandProcessing_JMenuItem,
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
	
	__CommandsPopup_ShowCommandStatus_String = "Show Command Status (Success/Warning/Failure)",

	// File menu (order in GUI)...

	__File_String = "File",
		__File_Open_String = "Open",
			__File_Open_CommandFile_String = "Command File...", 
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
			__File_Print_Commands_ActionString = "File...Print...Commands...", 
			__File_Print_Commands_String = "Commands...", 
		__File_Properties_String = "Properties",
			__File_Properties_CommandsRun_String="Commands Run",
			__File_Properties_TSToolSession_String="TSTool Session",
			__File_Properties_ColoradoSMS_String = "ColoradoSMS",
			__File_Properties_DIADvisor_String = "DIADvisor",
			__File_Properties_HydroBase_String ="HydroBase",
			__File_Properties_NWSRFSFS5Files_String = "NWSRFS FS5 Files",
			__File_Properties_RiversideDB_String = "RiversideDB",
		__File_SetWorkingDirectory_String = "Set Working Directory...",
		__File_Exit_String = "Exit",

	// Edit menu (order in GUI)...

	__Edit_String = "Edit",
		__Edit_CutCommands_String = "Cut Command(s)",
		__Edit_CopyCommands_String = "Copy Command(s)",
		__Edit_PasteCommands_String = "Paste Command(s) (After Selected)",
		__Edit_DeleteCommands_String = "Delete Command(s)",
		__Edit_SelectAllCommands_String ="Select All Commands",
		__Edit_DeselectAllCommands_String = "Deselect All Commands",
		__Edit_CommandFile_String = "Command File...",
		__Edit_CommandWithErrorChecking_String = "Command...",
		__Edit_ConvertSelectedCommandsToComments_String = "Convert selected commands to # comments",
		__Edit_ConvertSelectedCommandsFromComments_String =	"Convert selected commands from # comments",

	// View menu (order in GUI)...

	__View_String = "View",
		__View_MapInterface_String = "Map",

	// Commands menu (order in GUI)...

	__Commands_String = "Commands",

	__Commands_ConvertTSIDToReadCommand_String = "Convert TS Identifier to Read Command",
	__Commands_ConvertTSIDTo_ReadTimeSeries_String = TAB + "Convert TS Identifier (X.X.X.X.X) to TS Alias = ReadTimeSeries()",
	__Commands_ConvertTSIDTo_ReadDateValue_String = TAB + "Convert TS Identifier (X.X.X.X.X) to TS Alias = ReadDateValue()",
    __Commands_ConvertTSIDTo_ReadDelimitedFile_String = TAB + "Convert TS Identifier (X.X.X.X.X) to TS Alias = ReadDelimitedFile()",
	__Commands_ConvertTSIDTo_ReadHydroBase_String = TAB + "Convert TS Identifier (X.X.X.X.X) to TS Alias = ReadHydroBase()",
	__Commands_ConvertTSIDTo_ReadMODSIM_String = TAB + "Convert TS Identifier (X.X.X.X.X) to TS Alias = ReadMODSIM()",
	__Commands_ConvertTSIDTo_ReadNwsCard_String = TAB + "Convert TS Identifier (X.X.X.X.X) to TS Alias = ReadNwsCard()",
	__Commands_ConvertTSIDTo_ReadNWSRFSFS5Files_String = TAB + "Convert TS Identifier (X.X.X.X.X) to TS Alias = ReadNWSRFSFS5Files()",
	__Commands_ConvertTSIDTo_ReadRiverWare_String = TAB + "Convert TS Identifier (X.X.X.X.X) to TS Alias = ReadRiverWare()",
	__Commands_ConvertTSIDTo_ReadStateMod_String = TAB + "Convert TS Identifier (X.X.X.X.X) to TS Alias = ReadStateMod()",
	__Commands_ConvertTSIDTo_ReadStateModB_String = TAB + "Convert TS Identifier (X.X.X.X.X) to TS Alias = ReadStateModB()",
	__Commands_ConvertTSIDTo_ReadUsgsNwis_String = TAB + "Convert TS Identifier (X.X.X.X.X) to TS Alias = ReadUsgsNwis()",

	__Commands_CreateTimeSeries_String = "Create Time Series",
	__Commands_Create_CreateFromList_String = TAB + "CreateFromList()...  <read 1(+) time series from a list of identifiers>",
    __Commands_Create_ResequenceTimeSeriesData_String = TAB + "ResequenceTimeSeriesData()...  <resequence years to create new scenarios>",
	__Commands_Create_TS_ChangeInterval_String = TAB + "TS Alias = ChangeInterval()...  <convert time series to one with a different interval (under development)>",
	__Commands_Create_TS_Copy_String = TAB + "TS Alias = Copy()...  <copy a time series>",
	__Commands_Create_TS_Disaggregate_String = TAB + "TS Alias = Disaggregate()...  <disaggregate longer interval to shorter>",
	__Commands_Create_TS_NewDayTSFromMonthAndDayTS_String = TAB + "TS Alias = NewDayTSFromMonthAndDayTS()...  <create daily time series from monthly total and daily pattern>",
	__Commands_Create_TS_NewEndOfMonthTSFromDayTS_String = TAB + "TS Alias = NewEndOfMonthTSFromDayTS()...  <convert daily data to end of month time series>",
	__Commands_Create_TS_NewPatternTimeSeries_String = TAB + "TS Alias = NewPatternTimeSeries()... <create and initialize a new pattern time series>",
	__Commands_Create_TS_NewStatisticTimeSeries_String = TAB + "TS Alias = NewStatisticTimeSeries()... <create a time series as a repeating statistic from another time series - EXPERIMENTAL>",
	__Commands_Create_TS_NewStatisticYearTS_String = TAB + "TS Alias = NewStatisticYearTS()... <create a year time series using a statistic from another time series>",
	__Commands_Create_TS_NewTimeSeries_String = TAB + "TS Alias = NewTimeSeries()... <create and initialize a new time series>",
	__Commands_Create_TS_Normalize_String = TAB + "TS Alias = Normalize()... <Normalize time series to unitless values>",
	__Commands_Create_TS_RelativeDiff_String = TAB + "TS Alias = RelativeDiff()... <relative difference of time series>",

	__Commands_Read_SetIncludeMissingTS_String = TAB + "SetIncludeMissingTS()... <create empty time series if no data>",
	__Commands_Read_SetInputPeriod_String = TAB + "SetInputPeriod()... <for reading data>",

	__Commands_ReadTimeSeries_String = "Read Time Series",
	__Commands_Read_ReadDateValue_String = TAB + "ReadDateValue()...  <read 1(+) time series from a DateValue file>",
    __Commands_Read_ReadDelimitedFile_String = TAB + "ReadDelimitedFile()...  <read 1(+) time series from a delimited file (under development)>",
    __Commands_Read_ReadHECDSS_String = TAB + "ReadHECDSS()...  <read 1(+) time series from a HEC-DSS database file>",
    __Commands_Read_ReadHydroBase_String = TAB + "ReadHydroBase()...  <read 1(+) time series from HydroBase>",
	__Commands_Read_ReadMODSIM_String = TAB + "ReadMODSIM()...  <read 1(+) time ries from a MODSIM output file>",
	__Commands_Read_ReadNwsCard_String = TAB + "ReadNwsCard()...  <read 1(+) time series from an NWS CARD file>",
	__Commands_Read_ReadNWSRFSFS5Files_String = TAB + "ReadNWSRFSFS5Files()...  <read 1(+) time series from an NWSRFS FS5 Files>",
	__Commands_Read_ReadStateCU_String = TAB + "ReadStateCU()...  <read 1(+) time series from a StateCU file>",
	__Commands_Read_ReadStateCUB_String = TAB + "ReadStateCUB()...  <read 1(+) time series from a StateCU binary output file>",
	__Commands_Read_ReadStateMod_String = TAB +	"ReadStateMod()...  <read 1(+) time series from a StateMod file>",
	__Commands_Read_ReadStateModB_String = TAB + "ReadStateModB()...  <read 1(+) time series from a StateMod binary output file>",
	__Commands_Read_StateModMax_String = TAB + "StateModMax()...  <generate 1(+) time series as Max() of TS in two StateMod files>",

	__Commands_Read_TS_ReadDateValue_String = TAB +	"TS Alias = ReadDateValue()...  <read 1 time series from a DateValue file>",
	__Commands_Read_TS_ReadHydroBase_String = TAB + "TS Alias = ReadHydroBase()...  <read 1 time series from HydroBase>",
	__Commands_Read_TS_ReadMODSIM_String = TAB + "TS Alias = ReadMODSIM()...  <read 1 time series from a MODSIM output file>",
	__Commands_Read_TS_ReadNDFD_String = TAB + "TS Alias = ReadNDFD()...  <read 1 time series from NDFD web service>",
	__Commands_Read_TS_ReadNwsCard_String = TAB + "TS Alias = ReadNwsCard()...  <read 1 time series from an NWS CARD file>",
	__Commands_Read_TS_ReadNWSRFSFS5Files_String = TAB + "TS Alias = ReadNWSRFSFS5Files()...  <read 1 time series from an NWSRFS FS5 Files>",
	__Commands_Read_TS_ReadRiverWare_String = TAB +	"TS Alias = ReadRiverWare()...  <read 1 time series from a RiverWare file>",
	__Commands_Read_TS_ReadStateMod_String = TAB + "TS Alias = ReadStateMod()...  <read 1 time series from a StateMod file>",
	__Commands_Read_TS_ReadStateModB_String = TAB + "TS Alias = ReadStateModB()...  <read 1 time series from a StateMod binary file>",
	__Commands_Read_TS_ReadUsgsNwis_String = TAB + "TS Alias = ReadUsgsNwis()...  <read 1 time series from a USGS NWIS file>",

	// Commands... Fill Time Series...

	__Commands_FillTimeSeries_String = "Fill Time Series Missing Data",
	__Commands_Fill_FillConstant_String = TAB + "FillConstant()...  <fill TS with constant>",
	__Commands_Fill_FillDayTSFrom2MonthTSAnd1DayTS_String = TAB + "FillDayTSFrom2MonthTSAnd1DayTS()...  <fill daily time series using D1 = D2*M1/M2>",
	__Commands_Fill_FillFromTS_String = TAB + "FillFromTS()...  <fill time series with values from another time series>",
	__Commands_Fill_FillHistMonthAverage_String = TAB +	"FillHistMonthAverage()...  <fill monthly TS using historic average>",
	__Commands_Fill_FillHistYearAverage_String = TAB + "FillHistYearAverage()...  <fill yearly TS using historic average>",
	__Commands_Fill_FillInterpolate_String = TAB + "FillInterpolate()...  <fill TS using interpolation>",
	__Commands_Fill_FillMixedStation_String = TAB + "FillMixedStation()...  <fill TS using mixed stations (under development)>",
	__Commands_Fill_FillMOVE1_String = TAB + "FillMOVE1()...  <fill TS using MOVE1 method>",
	__Commands_Fill_FillMOVE2_String = TAB + "FillMOVE2()...  <fill TS using MOVE2 method>",
	__Commands_Fill_FillPattern_String = TAB + "FillPattern()...  <fill TS using WET/DRY/AVG pattern>",
	__Commands_Fill_FillProrate_String = TAB + "FillProrate()...  <fill TS by prorating another time series>",
	__Commands_Fill_FillRegression_String = TAB + "FillRegression()...  <fill TS using regression>",
	__Commands_Fill_FillRepeat_String = TAB + "FillRepeat()...  <fill TS by repeating values>",
	__Commands_Fill_FillUsingDiversionComments_String = TAB + "FillUsingDiversionComments()... <use diversion comments as data  - HydroBase ONLY>",
	// TODO SAM - need to add later...
	//MENU_INTERMEDIATE_FILL_WEIGHTS_String = "Fill Using Weights...",

	__Commands_Fill_SetAutoExtendPeriod_String = TAB + "SetAutoExtendPeriod()... <for data filling and manipulation>",
	__Commands_Fill_SetAveragePeriod_String = TAB +	"SetAveragePeriod()... <for data filling>",
	__Commands_Fill_SetIgnoreLEZero_String = TAB + "SetIgnoreLEZero()... <ignore values <= 0 in historical averages>",
	__Commands_Fill_SetPatternFile_String = TAB + "SetPatternFile()... <for use with fillPattern() >",
	__Commands_SetTimeSeries_String = "Set Time Series Contents",
	__Commands_Set_ReplaceValue_String = TAB + "ReplaceValue()...  <replace value (range) with constant in TS>",
	__Commands_Set_SetConstant_String = TAB + "SetConstant()...  <set all values to constant in TS>",
	__Commands_Set_SetDataValue_String = TAB + "SetDataValue()...  <set a single data value in a TS>",
	__Commands_Set_SetFromTS_String = TAB + "SetFromTS()...  <set time series values from another time series>",
	__Commands_Set_SetMax_String = TAB + "SetMax()...  <set values to maximum of time series>",
	__Commands_Set_SetToMin_String = TAB + "SetToMin()...  <set values to minimum of time series>",
    __Commands_Set_SetTimeSeriesProperty_String = TAB + "SetTimeSeriesProperty()...  <set time series properties>",

	// Commands...Manipulate Time Series menu...

	__Commands_Manipulate_Add_String = TAB + "Add()...  <add one or more TS to another>",
	__Commands_Manipulate_AddConstant_String = TAB + "AddConstant()...  <add a constant value to a TS>",
	__Commands_Manipulate_AdjustExtremes_String = TAB + "AdjustExtremes()...  <adjust extreme values>",
	__Commands_Manipulate_ARMA_String = TAB + "ARMA()...  <lag/attenuate a time series using ARMA>",
	__Commands_Manipulate_Blend_String = TAB + "Blend()...  <blend one TS with another>",
    __Commands_Manipulate_ChangePeriod_String = TAB + "ChangePeriod()...  <change the period of record>",
	__Commands_Manipulate_ConvertDataUnits_String = TAB + "ConvertDataUnits()...  <convert data units>",
	__Commands_Manipulate_Cumulate_String = TAB + "Cumulate()...  <cumulate values over time>",
	__Commands_Manipulate_Divide_String = TAB +	"Divide()...  <divide one TS by another TS>",
	__Commands_Manipulate_Free_String = TAB + "Free()...  <free time series>", 
	__Commands_Manipulate_Multiply_String = TAB + "Multiply()...  <multiply one TS by another TS>",
	__Commands_Manipulate_RunningAverage_String = TAB +	"RunningAverage()...  <convert TS to running average>",
	__Commands_Manipulate_Scale_String = TAB + "Scale()...  <scale TS by a constant>",
	__Commands_Manipulate_ShiftTimeByInterval_String = TAB + "ShiftTimeByInterval()...  <shift TS by an even interval>",
	__Commands_Manipulate_Subtract_String = TAB + "Subtract()...  <subtract one or more TS from another>",

	// Commands...Output Series menu...

	__Commands_OutputTimeSeries_String = "Output Time Series",
	__Commands_Output_DeselectTimeSeries_String = TAB +	"DeselectTimeSeries()...  <deselect time series for output/processing>",
	__Commands_Output_SelectTimeSeries_String = TAB + "SelectTimeSeries()...  <select time series for output/processing>",
	__Commands_Output_SetOutputDetailedHeaders_String = TAB + "SetOutputDetailedHeaders()... <in summary reports>",
	__Commands_Output_SetOutputPeriod_String = TAB + "SetOutputPeriod()... <for output products>",
	__Commands_Output_SetOutputYearType_String = TAB + "SetOutputYearType()... <e.g., Water, Calendar>",
	__Commands_Output_SortTimeSeries_String = TAB +	"SortTimeSeries()...  <sort time series>",
	__Commands_Output_WriteDateValue_String = TAB +	"WriteDateValue()...  <write DateValue file>",
	__Commands_Output_WriteNwsCard_String = TAB + "WriteNwsCard()...  <write NWS Card file>",
	__Commands_Output_WriteRiverWare_String = TAB +	"WriteRiverWare()...  <write RiverWare file>",
    __Commands_Output_WriteSHEF_String = TAB + "WriteSHEF()...  <write SHEF (Standard Hydrologic Exchange Format) file (under development)>",
	__Commands_Output_WriteStateCU_String = TAB + "WriteStateCU()...  <write StateCU file>",
	__Commands_Output_WriteStateMod_String = TAB + "WriteStateMod()...  <write StateMod file>",
	__Commands_Output_WriteSummary_String = TAB + "WriteSummary()...  <write Summary file>",
    __Commands_Output_ProcessTSProduct_String = TAB + "ProcessTSProduct()...  <process a time series product file>",

	// Commands...Analyze Time Series...

	__Commands_AnalyzeTimeSeries_String = "Analyze Time Series",
	__Commands_Analyze_AnalyzePattern_String = TAB + "AnalyzePattern()... <determine pattern(s) for FillPattern() (under development)>",
	__Commands_Analyze_CompareTimeSeries_String = TAB + "CompareTimeSeries()... <find differences between time series>",
	__Commands_Analyze_ComputeErrorTimeSeries_String = TAB + "ComputeErrorTimeSeries()... <compute error between time series>",

	__Commands_Analyze_NewDataTest_String = TAB + "DataTest TestID = NewDataTest()... <create a new data test> (under development)",
	__Commands_Analyze_ReadDataTestFromRiversideDB_String = TAB + "ReadDataTestFromRiversideDB()... <read 1 data test from RiversideDB> (under development)",
	__Commands_Analyze_RunDataTests_String = TAB + "RunDataTests()... <run data tests to evaluate time series> (under development)",
	__Commands_Analyze_ProcessDataTestResults_String = TAB + "ProcessDataTestResults()... <process data test results> (under development)",

	// Commands...Models...

	__Commands_Models_String = "Models",
	__Commands_Models_LagK_String =	"TS Alias = LagK()... <lag and attenuate (route) (under development)>",
    
	// HydroBase commands...

	__Commands_HydroBase_String = "HydroBase",
	__Commands_HydroBase_OpenHydroBase_String = TAB + "OpenHydroBase()... <open HydroBase database connection>",

	// NDFD commands...

	__Commands_NDFD_String = "NDFD",
	__Commands_NDFD_openNDFD_String = TAB +	"OpenNDFD()... <open NDFD web site connection>",
    
    // Commands...Ensemble processing...
    
    __Commands_Ensemble_String = "Ensemble Processing",
    __Commands_Ensemble_CreateEnsemble_String = TAB + "CreateEnsemble()...  <convert 1 time series into an ensemble>",
    __Commands_Ensemble_CopyEnsemble_String = TAB + "CopyEnsemble()...  <create a copy of an ensemble>",
    __Commands_Ensemble_ReadNwsrfsEspTraceEnsemble_String = TAB + "ReadNwsrfsEspTraceEnsemble()...  <read 1(+) time series from an NWSRFS ESP trace ensemble file>",
    __Commands_Ensemble_TS_NewStatisticTimeSeriesFromEnsemble_String = TAB + "TS Alias = NewStatisticTimeSeriesFromEnsemble()... <create a time series as a statistic from an ensemble - EXPERIMENTAL>",
    __Commands_Ensemble_TS_WeightTraces_String = TAB + "TS Alias = WeightTraces()... <weight traces to create a new time series>",
    __Commands_Ensemble_WriteNWSRFSESPTraceEnsemble_String = TAB + "WriteNWSRFSESPTraceEnsemble()...  <write NWSRFS ESP trace ensemble file>",
    
    // Table Commands...

    __Commands_Table_String = "Table Processing",
    __Commands_Table_ReadTableFromDelimitedFile_String = TAB + "ReadTableFromDelimitedFile()... <read a table from a delimited file (under development)>",

	// General Commands...
    
    __Commands_General_CheckingResults_String = "General - Checking/Testing Results",
    __Commands_General_CheckingResults_OpenCheckFile_String = TAB + "OpenCheckFile()... <open check file>",

    __Commands_General_Comments_String = "General - Comments",
    __Commands_General_Comments_Comment_String = TAB + "# comment(s)...",
    __Commands_General_Comments_ReadOnlyComment_String = TAB + "#@readOnly <insert read-only comment>",
    __Commands_General_Comments_StartComment_String = TAB + "/*   <start comment>",
    __Commands_General_Comments_EndComment_String = TAB + "*/   <end comment>",
    
    __Commands_General_FileHandling_String = "General - File Handling",
    __Commands_General_FileHandling_FTPGet_String = TAB + "FTPGet()... <get file(s) using FTP>",
    __Commands_General_FileHandling_RemoveFile_String = TAB + "RemoveFile()... <remove file(s)>",
    
	__Commands_General_Logging_String = "General - Logging",
	__Commands_General_Logging_StartLog_String = TAB + "StartLog()... <(re)start the log file>",
	__Commands_General_Logging_SetDebugLevel_String = TAB +	"SetDebugLevel()... <set debug message level>",
	__Commands_General_Logging_SetWarningLevel_String = TAB + "SetWarningLevel()... <set debug message level>",

    __Commands_General_Running_String = "General - Running",
    __Commands_General_Running_SetProperty_String = TAB + "SetProperty()... <set a time series processor property>",
    __Commands_General_Running_SetPropertyFromNwsrfsAppDefault_String =
        TAB + "SetPropertyFromNwsrfsAppDefault()... <set a time series processor property from an NWSRFS App Default>",
	__Commands_General_Running_RunCommands_String = TAB + "RunCommands()... <run a command file>",
	__Commands_General_Running_RunProgram_String = TAB + "RunProgram()... <run external program>",
    __Commands_General_Running_RunPython_String = TAB + "RunPython()... <run a Python script>",
    __Commands_General_Running_Exit_String = TAB + "Exit()  <to end processing>",
    __Commands_General_Running_SetWorkingDir_String = TAB + "SetWorkingDir()... <set the working directory for relative paths>",
 
    __Commands_General_TestProcessing_String = "General - Test Processing",
    __Commands_General_TestProcessing_StartRegressionTestResultsReport_String = TAB + "StartRegressionTestResultsReport()... <for test results>",
	__Commands_General_TestProcessing_CompareFiles_String = TAB + "CompareFiles()... <compare files, to test software>",
	__Commands_General_TestProcessing_WriteProperty_String = TAB + "WriteProperty()... <write processor property, to test software>",
	__Commands_General_TestProcessing_WriteTimeSeriesProperty_String = TAB + "WriteTimeSeriesProperty()... <write time series property, to test software>",
	__Commands_General_TestProcessing_CreateRegressionTestCommandFile_String = TAB + "CreateRegressionTestCommandFile()... <to test software>",
    __Commands_General_TestProcessing_TestCommand_String = TAB + "TestCommand()... <to test software>",
    
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
	__Results_Graph_PredictedValue_String =	"Graph - Predicted Value (under development)",
	__Results_Graph_PredictedValueResidual_String =	"Graph - Predicted Value Residual (under development)",
	__Results_Graph_XYScatter_String = "Graph - XY-Scatter",

	__Results_Table_String = "Table",
	__Results_Report_Summary_String = "Report - Summary",
	__Results_TimeSeriesProperties_String = "Time Series Properties",

	// Only for popup Results menu...

	__Results_FindTimeSeries_String = "Find Time Series...",

	// Run menu (order in GUI)...

	__Run_AllCommandsCreateOutput_String = "All Commands (create all output)",
	__Run_AllCommandsIgnoreOutput_String = "All Commands (ignore output commands)",
	__Run_SelectedCommandsCreateOutput_String =	"Selected Commands (create all output)",
	__Run_SelectedCommandsIgnoreOutput_String =	"Selected Commands (ignore output commands)",
	__Run_CancelCommandProcessing_String = "Cancel Command Processing",
	__Run_CommandsFromFile_String = "Commands From File...",
	__Run_ProcessTSProductPreview_String = "Process TS Product File (preview)...",
	__Run_ProcessTSProductOutput_String = "Process TS Product File (create output)...",

	// Tools menu (order in GUI)...

	__Tools_String = "Tools",
		__Tools_Analysis_String = "Analysis",
			__Tools_Analysis_MixedStationAnalysis_String = "Mixed Station Analysis... (under development)",
		__Tools_Report_String = "Report",
			__Tools_Report_DataCoverageByYear_String = "Data Coverage by Year...",
			__Tools_Report_DataLimitsSummary_String = "Data Limits Summary...",
			__Tools_Report_MonthSummaryDailyMeans_String = "Month Summary (Daily Means)...",
			__Tools_Report_MonthSummaryDailyTotals_String =	"Month Summary (Daily Totals)...",
			__Tools_Report_YearToDateTotal_String =	"Year to Date Total... <Daily or real-time CFS Only!>",
		__Tools_NWSRFS_String = "NWSRFS",
			__Tools_NWSRFS_ConvertNWSRFSESPTraceEnsemble_String = "Convert NWSRFS ESP Trace Ensemble File to Text...",
			__Tools_NWSRFS_ConvertJulianHour_String = "Convert Julian Hour...",
		__Tools_RiversideDB_String = "RiversideDB",
			__Tools_RiversideDB_TSProductManager_String = "Manage Time Series Products in RiversideDB...",
		__Tools_SelectOnMap_String = "Select on Map",
		__Tools_Options_String = "Options",

	// Help menu (order in GUI)...

	__Help_String = "Help",
		__Help_AboutTSTool_String = "About TSTool",

	// Strings used in popup menu for other components...

	__InputName_BrowseHECDSS_String = "Browse for a HEC-DSS file...",
	__InputName_BrowseStateModB_String = "Browse for a StateMod binary file...",
	__InputName_BrowseStateCUB_String = "Browse for a StateCU binary file...",

	__DATA_TYPE_AUTO = "Auto",

	// Input types for the __input_type_JComboBox...

	//__INPUT_TYPE_ColoradoSMS = "ColoradoSMS",
	__INPUT_TYPE_DateValue = "DateValue",
	__INPUT_TYPE_DIADvisor = "DIADvisor",
	__INPUT_TYPE_HECDSS = "HEC-DSS",
	__INPUT_TYPE_HydroBase = "HydroBase",
	__INPUT_TYPE_MEXICO_CSMN = "MexicoCSMN",
	__INPUT_TYPE_MODSIM = "MODSIM",
	__INPUT_TYPE_NWSCARD = "NWSCARD",
	__INPUT_TYPE_NWSRFS_FS5Files = "NWSRFS_FS5Files",
	__INPUT_TYPE_NWSRFS_ESPTraceEnsemble = "NWSRFS_ESPTraceEnsemble",
	__INPUT_TYPE_RiversideDB = "RiversideDB",
	__INPUT_TYPE_RiverWare = "RiverWare",
	__INPUT_TYPE_StateCU = "StateCU",
	__INPUT_TYPE_StateCUB = "StateCUB",
	__INPUT_TYPE_StateMod = "StateMod",
	__INPUT_TYPE_StateModB = "StateModB",
	__INPUT_TYPE_USGSNWIS = "USGSNWIS",

	// Time steps for __time_step_JComboBox...

	__TIMESTEP_AUTO = "Auto",
	__TIMESTEP_MINUTE = "Minute",
	__TIMESTEP_HOUR = "Hour",
	__TIMESTEP_DAY = "Day",
	__TIMESTEP_MONTH = "Month",
	__TIMESTEP_IRREGULAR = "Irregular"; // Use for real-time where interval is not known

// Columns in the time series list.

private final static int
	//__DIADvisor_COL_ROW_COUNT	= 0,
	__DIADvisor_COL_ID = 1,	// Sensor ID
	__DIADvisor_COL_NAME = 2,	// Sensor Name
	__DIADvisor_COL_DATA_SOURCE = 3,	// blank
	__DIADvisor_COL_DATA_TYPE = 4,	// "Data Value" and "Data Value 2"
	__DIADvisor_COL_TIMESTEP = 5,
	__DIADvisor_COL_SCENARIO = 6,	// Blank or Archive
	__DIADvisor_COL_UNITS = 7,	// "Display Units" and "Display Units 2"
	__DIADvisor_COL_START = 8,
	__DIADvisor_COL_END = 9,
	__DIADvisor_COL_INPUT_TYPE = 10;	// DIADvisor

/**
TSTool_JFrame constructor.
@param command_file Name of the command file to load at initialization.
@param run_on_load If true, a successful load of a command file will be followed by
running the commands.  If false, the command file will be loaded but not automatically run.
*/
public TSTool_JFrame ( String command_file, boolean run_on_load )
{	super();
	StopWatch swMain = new StopWatch();
	swMain.start();
	String rtn = "TSTool_JFrame";
	
	// Let the Message class know the frame so that message dialogs can popu up.
	
	Message.setTopLevel ( this );
   
	// Set the initial working directory up front because it is used in the
	// command processor and edit dialogs.
    
	__props = new PropList("TSTool_JFrame");
	__props.set ("WorkingDir=" + IOUtil.getProgramWorkingDir());	
	ui_SetInitialWorkingDir (__props.getValue ( "WorkingDir" ));
 
	addWindowListener ( this );

	// Determine which data sources are available.  This controls the look
	// and feel of the GUI.  Alphabetize the checks.  Initialize default
	// properties for each area if necessary (all other properties are
	// defaulted after checking the individual input types)...

	// DateValue always enabled by default...

	String prop_value = null;
	__source_DateValue_enabled = true;

	// ColoradoSMS disabled by default...

	prop_value = TSToolMain.getPropValue ( "TSTool.ColoradoSMSEnabled" );
	if ( prop_value != null ) {
		if ( prop_value.equalsIgnoreCase("false") ) {
			__source_ColoradoSMS_enabled = false;
		}
		else if ( prop_value.equalsIgnoreCase("true") ) {
			__source_ColoradoSMS_enabled = true;
		}
	}

	// DIADvisor not enabled by default...

	prop_value = TSToolMain.getPropValue ( "TSTool.DIADvisorEnabled" );
	if ( prop_value != null ) {
		if ( prop_value.equalsIgnoreCase("false") ) {
			__source_DIADvisor_enabled = false;
		}
		else if ( prop_value.equalsIgnoreCase("true") ) {
			__source_DIADvisor_enabled = true;
		}
	}
	
    // HEC-DSS not enabled by default...

    prop_value = TSToolMain.getPropValue ( "TSTool.HEC-DSSEnabled" );
    if ( prop_value != null ) {
        if ( prop_value.equalsIgnoreCase("false") ) {
            __source_HECDSS_enabled = false;
        }
        else if ( prop_value.equalsIgnoreCase("true") ) {
            __source_HECDSS_enabled = true;
        }
    }

	// NWSRFS_ESPTraceEnsemble not enabled by default...

	prop_value = TSToolMain.getPropValue ( "TSTool.NWSRFSESPTraceEnsembleEnabled" );
	if ( prop_value != null ) {
		if ( prop_value.equalsIgnoreCase("false") ) {
			__source_NWSRFS_ESPTraceEnsemble_enabled = false;
		}
		else if ( prop_value.equalsIgnoreCase("true") ) {
			__source_NWSRFS_ESPTraceEnsemble_enabled = true;
		}
	}
	// Always disable if not available in the Jar file...
	if ( !IOUtil.classCanBeLoaded( "RTi.DMI.NWSRFS_DMI.NWSRFS_ESPTraceEnsemble") ) {
		__source_NWSRFS_ESPTraceEnsemble_enabled = false;
	}

	// NWSRFS_FS5Files not enabled by default...

	prop_value = TSToolMain.getPropValue ( "TSTool.NWSRFSFS5FilesEnabled" );
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
	prop_value = TSToolMain.getPropValue ( "TSTool.HydroBaseEnabled" );
	if ( prop_value == null ) {
		// Older...
		prop_value = TSToolMain.getPropValue ("TSTool.HydroBaseCOEnabled" );
	}
	if ( (prop_value != null) && prop_value.equalsIgnoreCase("false") ) {
		__source_HydroBase_enabled = false;
	}
	if ( __source_HydroBase_enabled ) {
		// Use newer notation...
		__props.set ( "TSTool.HydroBaseEnabled", "true" );
		prop_value = TSToolMain.getPropValue ( "HydroBase.WDIDLength" );
		if ( (prop_value != null) && StringUtil.isInteger(prop_value)){
			__props.set ( "HydroBase.WDIDLength", prop_value );
			// Also set in global location.
			HydroBase_Util.setPreferredWDIDLength (	StringUtil.atoi ( prop_value ) );
		}
		else {
		    // Default...
			__props.set ( "HydroBase.WDIDLength", "7" );
		}
		prop_value = TSToolMain.getPropValue ( "HydroBase.OdbcDsn" );
		if ( prop_value != null ) {
			__props.set ( "HydroBase.OdbcDsn", prop_value );
		}
	}
	else {
	    __props.set ( "TSTool.HydroBaseEnabled", "false" );
	}

	// Mexico CSMN disabled by default...

	prop_value = TSToolMain.getPropValue ( "TSTool.MexicoCSMNEnabled" );
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
	prop_value = TSToolMain.getPropValue ( "TSTool.MODSIMEnabled" );
	if ( (prop_value != null) && prop_value.equalsIgnoreCase("false") ) {
		__source_MODSIM_enabled = false;
	}

	// NDFD enabled for RTi, disabled for CDSS by default...

	prop_value = TSToolMain.getPropValue ( "TSTool.NDFDEnabled" );
	if ( (prop_value != null) && prop_value.equalsIgnoreCase("false") ) {
		__source_NDFD_enabled = false;
	}

	// NWS Card disabled by default...

	prop_value = TSToolMain.getPropValue ( "TSTool.NWSCardEnabled" );
	if ( (prop_value != null) && prop_value.equalsIgnoreCase("false") ) {
		__source_NWSCard_enabled = false;
	}

	// NWSRFS FS5Files disabled by default...

	prop_value = TSToolMain.getPropValue ( "TSTool.NWSRFSFS5FilesEnabled" );
	if ( (prop_value != null) && prop_value.equalsIgnoreCase("false") ) {
		__source_NWSRFS_FS5Files_enabled = false;
	}
	else if ( (prop_value != null) && prop_value.equalsIgnoreCase("true") ){
		__source_NWSRFS_FS5Files_enabled = true;
		prop_value = TSToolMain.getPropValue ( "NWSRFSFS5Files.UseAppsDefaults" );
		if ( prop_value != null ) {
			__props.set( "NWSRFSFS5Files.UseAppsDefaults",prop_value );
		}
		else {
		    // Default is to use files.  If someone has Apps
			// Defaults configured, this property can be changed.
			__props.set ( "NWSRFSFS5Files.UseAppsDefaults","false");
		}
		prop_value = TSToolMain.getPropValue ( "NWSRFSFS5Files.InputName" );
		if ( prop_value != null ) {
			__props.set( "NWSRFSFS5Files.InputName",prop_value );
		}
	}

	// RiversideDB disabled by default...

	prop_value = TSToolMain.getPropValue ( "TSTool.RiversideDBEnabled" );

	if ( prop_value != null ) {
		if ( prop_value.equalsIgnoreCase("false") ) {
			__source_RiversideDB_enabled = false;
		}
		else if ( prop_value.equalsIgnoreCase("true") ) {
			__source_RiversideDB_enabled = true;
		}
	}

	// RiverWare disabled by default...

	prop_value = TSToolMain.getPropValue ( "TSTool.RiverWareEnabled" );
	if ( prop_value != null ) {
		if ( prop_value.equalsIgnoreCase("false") ) {
			__source_RiverWare_enabled = false;
		}
		else if ( prop_value.equalsIgnoreCase("true") ) {
			__source_RiverWare_enabled = true;
		}
	}

	// SHEF disabled by default...

	prop_value = TSToolMain.getPropValue ( "TSTool.SHEFEnabled" );
	if ( prop_value != null ) {
		if ( prop_value.equalsIgnoreCase("false") ) {
			__source_SHEF_enabled = false;
		}
		else if ( prop_value.equalsIgnoreCase("true") ) {
			__source_SHEF_enabled = true;
		}
	}

	// StateMod enabled by default (no config file)...

	prop_value = TSToolMain.getPropValue ( "TSTool.StateModEnabled" );
	if ( (prop_value != null) && prop_value.equalsIgnoreCase("false") ) {
		__source_StateMod_enabled = false;
	}

	// StateCU enabled by default (no config file)...

	prop_value = TSToolMain.getPropValue ( "TSTool.StateCUEnabled" );
	if ( (prop_value != null) && prop_value.equalsIgnoreCase("false") ) {
		__source_StateCU_enabled = false;
	}
	
	// StateCUB enabled by default...

    prop_value = TSToolMain.getPropValue ( "TSTool.StateCUBEnabled" );
    if ( (prop_value != null) && prop_value.equalsIgnoreCase("false") ) {
        __source_StateCUB_enabled = false;
    }

	// StateModB enabled by default...

	prop_value = TSToolMain.getPropValue ( "TSTool.StateModBEnabled" );
	if ( (prop_value != null) && prop_value.equalsIgnoreCase("false") ) {
		__source_StateModB_enabled = false;
	}

	// USGSNWIS enabled by default...

	prop_value = TSToolMain.getPropValue ( "TSTool.USGSNWISEnabled" );
	if ( (prop_value != null) && prop_value.equalsIgnoreCase("false") ) {
		__source_USGSNWIS_enabled = false;
	}

	prop_value = null;

	// Values determined at run-time...

	__props.setHowSet ( Prop.SET_AS_RUNTIME_DEFAULT );
	__props.set ( "WorkingDir", IOUtil.getProgramWorkingDir() );
	Message.printStatus ( 1, "", "Working directory is " + __props.getValue ( "WorkingDir" ) );
	__props.setHowSet ( Prop.SET_AT_RUNTIME_BY_USER );

	// Read the license information and disable/enable input types based on the license...

	license_InitializeLicenseFromTSToolProperties ();
	ui_CheckInputTypesForLicense ( license_GetLicenseManager() );
	// If the license type is CDSS, the icon will be set to the CDSS icon.
	// Otherwise, the icon will be set to the RTi icon...
	TSToolMain.setIcon ( license_GetLicenseManager().getLicenseType() );

	// create the GUI ...
	StopWatch in = new StopWatch();
	in.start();
	ui_InitGUI ();
	in.stop();
	if ( Message.isDebugOn ) {
	    Message.printDebug(1, "", "JTS - InitGUI: " + in.getSeconds());
	}

	// Check the license.  Do this after GUI is initialized so that a dialog can be shown...

	license_CheckLicense ( license_GetLicenseManager() );

    // Get database connection information.  Force a login if the
	// database connection cannot be made.  The login is interactive and
	// is disabled if no main GUI is to be shown.

	StopWatch sms = new StopWatch();
	sms.start();
	if ( __source_ColoradoSMS_enabled ) {
		// Login to Colorado SMS database using information in the
		// CDSS.cfg file (pending Doug Stenzel input)...
		uiAction_OpenColoradoSMS ( true );
	}
	sms.stop();
	Message.printDebug(1, "", "JTS - OpenColoradoSMS: " + sms.getSeconds());
	swMain.stop();
	Message.printDebug(1, "", "JTS - TSTool_JFrame(): " + swMain.getSeconds());
	if ( __source_HydroBase_enabled ) {
		// Login to HydroBase using information in the TSTool configuration file...
		uiAction_OpenHydroBase ( true );
		// Force the choices to refresh...
		if ( __hbdmi != null ) {
			__input_type_JComboBox.select ( null );
			__input_type_JComboBox.select (__INPUT_TYPE_HydroBase);
		}
	}
	if ( __source_NWSRFS_FS5Files_enabled ) {
		// Open NWSRFS FS5Files using information in the TSTool configuration file...
		uiAction_OpenNWSRFSFS5Files ( null, true );
		// Force the choices to refresh...
		if ( __nwsrfs_dmi != null ) {
			/* TODO SAM 2004-09-10 DO NOT DO THIS BECAUSE IT THEN PROMPTS FOR a choice...
			__input_type_JComboBox.select ( null );
			__input_type_JComboBox.select (
				__INPUT_TYPE_NWSRFS_FS5Files );
			*/
		}
	}
	if ( __source_RiversideDB_enabled ) {
		// Login to the RiversideDB using information in the TSTool configuration file...
		uiAction_OpenRiversideDB ( null, true );
	}

	// Add GUI features that depend on the databases...
	// The appropriate panel will be set visible as input types and data
	// types are chosen.  The panels are currently only intialized once so
	// changing databases or enabling new databases after setup may require
	// some code changes.  Set the wait cursor because queries are done during setup.

	JGUIUtil.setWaitCursor ( this, true );
	try {
	    ui_InitGUIInputFilters ( __input_filter_y );
	}
	catch ( Exception e ) {
		Message.printWarning ( 3, rtn,
		"For developers:  caught exception initializing input filters at setup." );
		Message.printWarning ( 3, rtn, e );
	}
	// TODO SAM 2007-01-23 Evaluate use.
	// Force everything to refresh based on the current GUI layout.
	// Still evaluating this.
	this.invalidate ();
	JGUIUtil.setWaitCursor ( this, false );

	// Use the window listener

	ui_UpdateStatus ( true );

	// If running with a command file from the command line, we don't
	// normally want to see the main window so handle with a special case...

	// TSTool has been started with a command file so try to open and display
	// It should already be absolute
	if ( (command_file != null) && (command_file.length() > 0) ) {
	    ui_LoadCommandFile ( command_file, run_on_load );
	}
}

/**
Handle action events (menu and button actions).
@param event Event to handle.
*/
public void actionPerformed (ActionEvent event)
{	
	if ( ui_GetIgnoreActionEvent() ) {
		// Ignore ActionEvent for programmatic modification of data models.
		return;
	}
	try {
        // This will chain, calling several methods, so that each method does not get so large...

		uiAction_ActionPerformed1_MainActions(event);

		// Check the GUI state and disable buttons, etc., depending on the selections that are made...

		ui_UpdateStatus ( true );
	}
	catch ( Exception e ) {
		// Unexpected exception - user will likely see no result from their action.
		String routine = getClass().getName() + ".actionPerformed";
			Message.printWarning ( 2, routine, e );
		JGUIUtil.setWaitCursor ( this, false );
	}
}

/**
Indicate that a command has been cancelled.  The success/failure of the command
is not indicated (see CommandStatusProvider).
@param icommand The command index (0+).
@param ncommand The total number of commands to process
@param command The reference to the command that has been cancelled, either the
one that has just been processed, or potentially the next one, depending on when
the cancel was requested.
@param percent_complete If >= 0, the value can be used to indicate progress
running a list of commands (not the single command).  If less than zero, then
no estimate is given for the percent complete and calling code can make its
own determination (e.g., ((icommand + 1)/ncommand)*100).
@param message A short message describing the status (e.g., "Running command ..." ).
*/
public void commandCancelled ( int icommand, int ncommand, Command command,
		float percent_complete, String message )
{	String routine = "TSTool_JFrame.commandCancelled";
	
	// Refresh the results with what is available...
	//String command_string = command.toString();
	ui_UpdateStatusTextFields ( 1, routine,	"Cancelled command processing.",
			//"Cancelled: " + command_string,
				null, __STATUS_CANCELLED );
	uiAction_RunCommands_ShowResults ();
}

/**
Indicate that a command has completed.  The success/failure of the command
is not indicated (see CommandStatusProvider).
@param icommand The command index (0+).
@param ncommand The total number of commands to process
@param command The reference to the command that is starting to run,
provided to allow future interaction with the command.
@param percent_complete If >= 0, the value can be used to indicate progress
running a list of commands (not the single command).  If less than zero, then
no estimate is given for the percent complete and calling code can make its
own determination (e.g., ((icommand + 1)/ncommand)*100).
@param message A short message describing the status (e.g., "Running command ..." ).
*/
public void commandCompleted ( int icommand, int ncommand, Command command,
		float percent_complete, String message )
{	String routine = "TSTool_JFrame.commandCompleted";
	// Update the progress bar to indicate progress (1 to number of commands... completed).
	__processor_JProgressBar.setValue ( icommand + 1 );
	// For debugging...
	//Message.printStatus(2,getClass().getName()+".commandCompleted", "Setting processor progress bar to " + (icommand + 1));
	__command_JProgressBar.setValue ( __command_JProgressBar.getMaximum() );
	
	if ( (icommand + 1) == ncommand ) {
		// Last command has completed so refresh the time series results.
		// Only need to do if threaded because otherwise will handle synchronously
		// in the uiAction_RunCommands() method...
		String command_string = command.toString();
		ui_UpdateStatusTextFields ( 1, routine, null, "Processed: " + command_string,
				__STATUS_READY );
		if ( ui_Property_RunCommandProcessorInThread() ) {
			uiAction_RunCommands_ShowResults ();
		}
	}
}

/**
Determine whether commands are equal, for single-line commands.
@param original_command Original command as a string.
@param edited_command Edited command as a string.
*/
private boolean commandList_CommandsAreEqual (String original_command, String edited_command)
{	Vector original_command_Vector = new Vector(1);
	original_command_Vector.addElement ( original_command );
	Vector edited_command_Vector = new Vector(1);
	edited_command_Vector.addElement ( edited_command );
	return commandList_CommandsAreEqual(original_command_Vector,edited_command_Vector);
}

/**
Determine whether commands are equal.  To allow for multi-line commands, each
command is stored in a Vector (but typically only the first String is used.
@param original_command Original command as a Vector of String or Command.
@param edited_command Edited command as a Vector of String or Command.
*/
private boolean commandList_CommandsAreEqual(Vector original_command, Vector edited_command)
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
	Object original_Object, edited_Object;
	String original_String = null, edited_String = null;
	for ( int i = 0; i < original_size; i++ ) {
		original_Object = original_command.elementAt(i);
		edited_Object = edited_command.elementAt(i);
		if ( original_Object instanceof String ) {
			original_String = (String)original_Object;
		}
		else if ( original_Object instanceof Command ) {
			original_String = ((Command)original_Object).toString();
		}
		if ( edited_Object instanceof String ) {
			edited_String = (String)edited_Object;
		}
		else if ( edited_Object instanceof Command ) {
			edited_String = ((Command)edited_Object).toString();
		}
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
Return the command(s) that are currently selected in the final list.
If the command contains only comments, they are all returned.  If it contains
commands and time series identifiers, only the first command (or time series
identifier) is returned.
@return selected commands in final list or null if none are selected.
Also return null if more than one command is selected.
*/
/*
private Vector commandList_GetCommand ()
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
/ * FIXME when method reenabled.
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
* /

	return command;
}
*/

/**
Edit a command in the command list.
@param action the string containing the event's action value.  This is checked
for new commands.  When editing existing commands, command_Vector will contain
a list of Command class instances.  Normally only the first command will be edited as
a single-line command.  However, multiple # comment lines can be selected and edited at once.
@param command_Vector If an update, this contains the current Command instances
to edit.  If a new command, this is null and the action string will be consulted
to construct the appropriate command.  The only time that multiple commands will
be edited are when they are in a {# delimited comment block).
@param mode the action to take when editing the command (__INSERT_COMMAND for a
new command or __UPDATE_COMMAND for an existing command).
*/
private void commandList_EditCommand (	String action, Vector command_Vector, int mode )
{	String routine = getClass().getName() + ".editCommand";
	int dl = 1;		// Debug level
	
    // Make absolutely sure that warning level 1 messages are shown to the user in a dialog.
    // This may have been turned off in command processing.
    // Should not need this if set properly in the command processor.
    //Message.setPropValue ( "ShowWarningDialog=true" );
    
	// Indicate whether the commands are a block of # comments.
	// If so then need to use a special editor rather than typical one-line editors.
	boolean is_comment_block = false;
	// Indicate whether an exit command, in which case editing is not needed
	boolean is_exit = false;
	if ( mode == __UPDATE_COMMAND ) {
		is_comment_block = commandList_IsCommentBlock ( __ts_processor,
			command_Vector,
			true,	// All must be comments
			true );	// Comments must be contiguous
		is_exit = commandList_IsExitCommand ( command_Vector );
	}
	else {
		// New command, so look for comment actions.
		if ( action.equals(__Commands_General_Comments_Comment_String) ) {
			is_comment_block = true;
		}
		if ( action.equals(__Commands_General_Running_Exit_String) ) {
			is_exit = true;
		}
	}
	if ( is_comment_block ) {
		Message.printStatus(2, routine, "Command is a comment block.");
	}

	try {
        // Main try to help with troubleshooting, especially during
		// transition to new command structure.

	// First make sure we have a Command object to edit.  If an old-style command
	// then it will be stored in a GenericCommand.
	// The Command object is inserted in the processor in any case, to take advantage
	// of processor information (such as being able to get the time series identifiers
	// from previous commands.
	// If a new command is being inserted and a cancel occurs, the command will simply
	// be removed from the list.
	// If an existing command is being updated and a cancel occurs, the changes need to
	// be ignored.
	
	Command command_to_edit_original = null;	// Command being edited (original).
	Command command_to_edit = null;	// Command being edited (clone).
	if ( mode == __UPDATE_COMMAND ) {
		// Get the command from the processor...
		if ( is_comment_block ) {
			// Use the string-based editor dialog and then convert each
			// comment line into a command.  Don't do anything to the command list yet
		}
		else if ( is_exit ) {
			// Don't do anything.
		}
		else {
			// Get the original command...
			command_to_edit_original = (Command)command_Vector.elementAt(0);
			// Clone it so that the edit occurs on the copy...
			command_to_edit = (Command)command_to_edit_original.clone();
			Message.printStatus(2, routine, "Cloned command to edit: \"" + command_to_edit + "\"" );
			// Remove the original command...
			int pos = commandList_IndexOf ( command_to_edit_original );
			commandList_RemoveCommand ( command_to_edit_original );
			// Insert the copy during the edit...
			commandList_InsertCommandAt ( command_to_edit, pos );
			Message.printStatus(2, routine,
				"Will edit the copy and restore to the original if the edit is cancelled.");
		}
	}
	else if ( mode == __INSERT_COMMAND ) {
		if ( is_comment_block ) {
			// Don't do anything here.  New comments will be inserted in code below.
		}
		else {
			// New command so create a command as a placeholder for
			// editing (filled out during the editing).
			// Get everything before the ) in the command and then re-add the ").
			// TODO SAM 2007-08-31 Why is this done?
			// Need to handle:
			//	1) Traditional commands foo()
			//	2) Comments # blocks
			//  3) TS alias = foo()
			//  4) Time series identifiers.
			//  5) Don't allow edit of /* */ comments - just insert/delete
			String command_string = StringUtil.getToken(action,")",0,0)+ ")";
			if ( Message.isDebugOn ) {
				Message.printDebug ( dl, routine,
						"Using command factory to create new command for \"" + command_string + "\"" );
			}
		
			command_to_edit = commandList_NewCommand( command_string, true );
			Message.printStatus(2, routine, "Created new command to insert:  \"" + command_to_edit + "\"" );
        
			// Add it to the processor at the insert point of the edit (before the first selected command...
        
			commandList_InsertCommandBasedOnUI ( command_to_edit );
			Message.printStatus(2, routine, "Inserted command for editing.");
		}
	}

	// Second, edit the command, whether an update or an insert...

	boolean edit_completed = false;
	Vector new_comments = new Vector();	// Used if comments are edited.
	if ( is_comment_block ) {
		// Edit using the old-style editor...
		edit_completed = commandList_EditCommandOldStyleComments ( mode, action, command_Vector, new_comments );
	}
	else if ( is_exit ) {
		// No need to edit - just insert.
		edit_completed = true;
	}
	else {
	    // Editing a single one-line command...
        try {
    		if ( command_to_edit instanceof GenericCommand ) {
    			// Edit with the old style editors.
    			Message.printStatus(2, routine, "Editing GenericCommand with old-style editor.");
    			edit_completed = commandList_EditCommandOldStyle ( mode, action, command_Vector, (GenericCommand)command_to_edit );
    		}
    		else {
    			// Edit with the new style editors...
    			Message.printStatus(2, routine, "Editing Command with new-style editor.");
    			edit_completed = commandList_EditCommandNewStyle ( command_to_edit );
    		}
        }
        catch ( Exception e ) {
            Message.printWarning (1 , routine, "Unexpected error editing command - refer to log and report to software support." );
            Message.printWarning( 3, routine, e );
            edit_completed = false;
        }
	}
	
	// Third, make sure that the edits are to be saved.  If not, restore the original
	// copy (if an update) or discard the command (if a new insert).
    // If the command implements CommandDiscoverable, try to make the discovery run.

	if ( edit_completed ) {
		if ( mode == __INSERT_COMMAND ) {
			if ( is_comment_block ) {
				// Insert the comments at the insert point...
				commandList_InsertCommentsBasedOnUI ( new_comments );
			}
			else {
				// The command has already been inserted in the list.
				Message.printStatus(2, routine, "After insert, command is:  \"" + command_to_edit + "\"" );
                if ( command_to_edit instanceof CommandDiscoverable ) {
                    commandList_EditCommand_RunDiscovery ( command_to_edit );
                }
			}
			commandList_SetDirty(true);
		}
		else if ( mode == __UPDATE_COMMAND ) {
			// The command was updated.
			if ( is_comment_block ) {
				// Remove the commands that were selected and insert the new ones.
				commandList_ReplaceComments ( command_Vector, new_comments );
				if ( !commandList_CommandsAreEqual(command_Vector,new_comments)) {
					commandList_SetDirty(true);
				}
			}
			else {
				// The contents of the command will have been modified so there is no need to do anything more.
				Message.printStatus(2, routine, "After edit, command is:  \"" + command_to_edit + "\"" );
				if ( !command_to_edit_original.toString().equals(command_to_edit.toString())) {
					commandList_SetDirty(true);
				}
                if ( command_to_edit instanceof CommandDiscoverable ) {
                    commandList_EditCommand_RunDiscovery ( command_to_edit );
                }
			}
		}
	}
	else {
        // The edit was canceled.  If it was a new command being inserted, remove the command from the processor...
		if ( mode == __INSERT_COMMAND ) {
			if ( is_comment_block ) {
				// No comments were inserted at start of edit.  No need to do anything.
			}
			else {
				// A temporary new command was inserted so remove it.
				commandList_RemoveCommand(command_to_edit);
				Message.printStatus(2, routine, "Edit was cancelled.  Removing from command list." );
			}
		}
		else if ( mode == __UPDATE_COMMAND ) {
			if ( is_comment_block ) {
				// The original comments will remain.  No need to do anything.
			}
			else {
				// Else was an update so restore the original command...
			    Message.printStatus(2, routine, "Edit was cancelled.  Restoring pre-edit command." );
				int pos = commandList_IndexOf(command_to_edit);
				commandList_RemoveCommand(command_to_edit);
				commandList_InsertCommandAt(command_to_edit_original, pos);
			}
		}
	}
	
	// TODO SAM 2007-12-07 Evaluate whether to refresh the command list status?
    
    ui_ShowCurrentCommandListStatus();

	}
	catch ( Exception e2 ) {
		// TODO SAM 2005-05-18 Evaluate handling of unexpected error... 
		Message.printWarning(1, routine, "Unexpected error editing command." );
		Message.printWarning ( 3, routine, e2 );
	}
}

/**
Run discovery on the command. This will, for example, make available a list of time series
to be requested with the ObjectListProvider.getObjectList() method.
*/
private void commandList_EditCommand_RunDiscovery ( Command command_to_edit )
{   String routine = getClass().getName() + ".commandList_EditCommand_RunDiscovery";
    // Run the discovery...
    Message.printStatus(2, routine, "Running discovery mode on command:  \"" + command_to_edit + "\"" );
    try {
        ((CommandDiscoverable)command_to_edit).runCommandDiscovery(__ts_processor.indexOf(command_to_edit));
        // Redraw the status area
        ui_ShowCurrentCommandListStatus();
    }
    catch ( Exception e )
    {
        // For now ignore because edit-time input may not be complete...
        String message = "Unable to make discover run - may be OK if partial data.";
        Message.printStatus(2, routine, message);
    }
}

/**
Edit a new-style command, which has a custom editor.
@param Command command_to_edit The command to edit.
*/
private boolean commandList_EditCommandNewStyle ( Command command_to_edit )
{
	return command_to_edit.editCommand(this);
}

/**
Edit the command using old-style editors.  This is used for commands that have not
been migrated to the new design and hence they are only represented as GenericCommand
with the command string set.  This method ONLY edits the command.  It does nothing to
commit the edit back to the command list (the calling code will do that).
@param mode Mode of editing, whether updating or inserting.
@param action If not null, then the command is new and the action needs to be checked
to determine the command.
@param command_Vector Commands being edited as a Vector of Command, as passed from
the legacy code.
@param command_to_edit The Command instance to edit.
@return true if the command edits were committed, false if cancelled.
*/
private boolean commandList_EditCommandOldStyle (
		int mode, String action, Vector command_Vector, GenericCommand command_to_edit )
{	String routine = getClass().getName() + ".editCommandOldStyle";
	int dl = 1;	// Debug level
	// String version of the command...
	String command = "";	// Will be reset below
	Vector cv = null;
	if ( mode == __UPDATE_COMMAND ) {
		cv = new Vector(1);
		command = ((GenericCommand)command_Vector.elementAt(0)).getCommandString();
		cv.addElement ( command );
	}
	else {
	    // Inserting a new command.  Just pass empty data to be edited (will be checked for in editors).
		cv = new Vector(1);
		command = "";
		cv.addElement ( command );
	}
	String command_trimmed = command.trim();
	Vector edited_cv = null;
	// The following are listed in the order of the menus.
	if ( action.equals( __Commands_Create_CreateFromList_String)||
		(StringUtil.indexOfIgnoreCase(command,"createFromList(",0)>=0)||
		(StringUtil.indexOfIgnoreCase(command,"createFromList (",0) >= 0) ) {
		if ( Message.isDebugOn ) {
			Message.printDebug ( dl, routine, "Opening dialog for createFromList()" );
		}
		edited_cv = new createFromList_JDialog ( this, ui_GetPropertiesForOldStyleEditor ( command_to_edit ),
			cv, TSCommandProcessorUtil.getTSIdentifiersNoInputFromCommandsBeforeCommand(
					__ts_processor, command_to_edit), command_to_edit).getText();
	}
	else if ( action.equals( __Commands_Create_TS_Disaggregate_String)||
		(StringUtil.indexOfIgnoreCase(command,"disaggregate(",0) >= 0)||
		(StringUtil.indexOfIgnoreCase(command,"disaggregate (",0) >=0)){
		if ( Message.isDebugOn ) {
			Message.printDebug ( dl, routine, "Opening dialog for TS Alias = disaggregate()" );
		}
		edited_cv = new disaggregate_JDialog ( this, cv,
				TSCommandProcessorUtil.getTSIdentifiersNoInputFromCommandsBeforeCommand(
						__ts_processor, command_to_edit)).getText();
	}
	else if ( action.equals(
		__Commands_Create_TS_NewDayTSFromMonthAndDayTS_String)||
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
				TSCommandProcessorUtil.getTSIdentifiersNoInputFromCommandsBeforeCommand(
						__ts_processor, command_to_edit)).getText();
	}
	else if ( action.equals(
		__Commands_Create_TS_NewEndOfMonthTSFromDayTS_String)||
		(StringUtil.indexOfIgnoreCase(
		command,"NewEndOfMonthTSFromDayTS",0) >= 0) ) {
		if ( Message.isDebugOn ) {
			Message.printDebug ( dl, routine,
			"Opening dialog for TS Alias =" +
			"newEndOfMonthTSFromDayTS()" );
		}
		edited_cv = new newEndOfMonthTSFromDayTS_JDialog (this,cv,
			TSCommandProcessorUtil.getTSIdentifiersNoInputFromCommandsBeforeCommand(
					__ts_processor, command_to_edit)).getText();
	}
	else if ( action.equals( __Commands_Create_TS_Normalize_String)||
		(StringUtil.indexOfIgnoreCase(command,"normalize(",0) >= 0)||
		(StringUtil.indexOfIgnoreCase(command,"normalize (",0)>= 0)){
		if ( Message.isDebugOn ) {
			Message.printDebug ( dl, routine,
			"Opening dialog for TS Alias = normalize()" );
		}
		edited_cv = new normalize_JDialog ( this, cv,
				TSCommandProcessorUtil.getTSIdentifiersNoInputFromCommandsBeforeCommand(
						__ts_processor, command_to_edit)).getText();
	}
	else if ( action.equals( __Commands_Create_TS_RelativeDiff_String)||
		(StringUtil.indexOfIgnoreCase(
		command,"relativeDiff(",0)>=0)||
		(StringUtil.indexOfIgnoreCase(
		command,"relativeDiff (",0)>=0) ) {
		if ( Message.isDebugOn ) {
			Message.printDebug ( dl, routine,
			"Opening dialog for TS Alias = relativeDiff()" );
		}
		edited_cv = new relativeDiff_JDialog ( this, cv,
				TSCommandProcessorUtil.getTSIdentifiersNoInputFromCommandsBeforeCommand(
						__ts_processor, command_to_edit)).getText();
	}

	// Convert time series...

	else if (action.equals(__Commands_ConvertTSIDTo_ReadTimeSeries_String)||
		StringUtil.indexOfIgnoreCase(command,"readTimeSeries",0)>5 ) {
		// TS Alias = ...
		if ( Message.isDebugOn ) {
			Message.printDebug ( dl, routine,
			"Opening dialog for TS Alias = readTimeSeries()" );
		}
		edited_cv = new readTimeSeries_JDialog ( this, ui_GetPropertiesForOldStyleEditor ( command_to_edit ),
			cv, TSCommandProcessorUtil.getTSIdentifiersNoInputFromCommandsBeforeCommand(
					__ts_processor, command_to_edit)).getText();
	}

	// Read Time Series...

	else if ( action.equals( __Commands_Read_StateModMax_String)||
		StringUtil.startsWithIgnoreCase(command,"StateModMax") ) {
		edited_cv = new statemodMax_JDialog ( this, ui_GetPropertiesForOldStyleEditor ( command_to_edit ),
			cv, null, command_to_edit ).getText();
	}

	// Fill Time Series Data...

	else if ( action.equals(
		__Commands_Fill_FillDayTSFrom2MonthTSAnd1DayTS_String)||
		command.regionMatches(
			true,0,"fillDayTSFrom2MonthTSAnd1DayTS",0,30) ) {
		if ( Message.isDebugOn ) {
			Message.printDebug ( dl, routine,
			"Opening dialog for fillDayTSFrom2MonthTSAnd1DayTS()" );
		}
		edited_cv = new fillDayTSFrom2MonthTSAnd1DayTS_JDialog ( 
			this, cv, TSCommandProcessorUtil.getTSIdentifiersNoInputFromCommandsBeforeCommand(
					__ts_processor, command_to_edit)).getText();
	}
	else if ( action.equals(__Commands_Fill_FillPattern_String) ||
		command.regionMatches(true,0,"fillPattern",0,11) ) {
		if ( Message.isDebugOn ) {
			Message.printDebug ( dl, routine,
			"Opening dialog for fillPattern()" );
		}
		// Get the pattern time series from the previous commands...
		Vector needed_commands_Vector = new Vector();
		needed_commands_Vector.addElement ( "setPatternFile" );
		Vector found_commands_Vector =
			TSCommandProcessorUtil.getCommandsBeforeIndex(
			commandList_GetInsertPosition(),
			__ts_processor,
			needed_commands_Vector,
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
				found_commands_Vector.elementAt(ip).toString(),
				"() ", StringUtil.DELIM_SKIP_BLANKS|
				StringUtil.DELIM_ALLOW_STRINGS );
			if ( (tokens != null) && (tokens.size() == 2) ) {
				__fill_pattern_files.addElement ( ((String)tokens.elementAt(1)).trim() );
			}
		}
		// Now get a list of the patterns so that they can be used in the dialog to make selections...
		if ( __fill_pattern_ids.size() == 0 ) {
			// Read the pattern information so it can be passed to the dialog.
			readPatternTS ();
		}
		edited_cv = new fillPattern_JDialog ( this, ui_GetPropertiesForOldStyleEditor ( command_to_edit ),
			__fill_pattern_files, cv,
			TSCommandProcessorUtil.getTSIdentifiersNoInputFromCommandsBeforeCommand(
					__ts_processor, command_to_edit), __fill_pattern_ids ).getText();
	}
	else if (action.equals( __Commands_Fill_FillProrate_String)||
		command.regionMatches( true,0,"fillProrate",0,11)){
		if ( Message.isDebugOn ) {
			Message.printDebug ( dl, routine, "Opening dialog for fillProrate()" );
		}
		edited_cv = new fillProrate_JDialog ( this, cv,
				TSCommandProcessorUtil.getTSIdentifiersNoInputFromCommandsBeforeCommand(
						__ts_processor, command_to_edit)).getText();
	}
	else if ( action.equals( __Commands_Fill_SetPatternFile_String) ||
		command.regionMatches(true,0,"setPatternFile",0,14) ) {
		if ( Message.isDebugOn ) {
			Message.printDebug ( dl, routine,
			"Opening dialog for setPatternFile()" );
		}
		edited_cv = new setPatternFile_JDialog ( this, ui_GetPropertiesForOldStyleEditor ( command_to_edit ),
			cv, TSCommandProcessorUtil.getTSIdentifiersNoInputFromCommandsBeforeCommand(
					__ts_processor, command_to_edit), command_to_edit).getText();
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
	
	// Set time series contents...

	else if ( action.equals( __Commands_Set_SetDataValue_String)||
		command.regionMatches(true,0,"setDataValue",0,12) ) {
		if ( Message.isDebugOn ) {
			Message.printDebug ( dl, routine,
			"Opening dialog for setDataValue()" );
		}
		edited_cv = new setDataValue_JDialog ( this, cv,
				TSCommandProcessorUtil.getTSIdentifiersNoInputFromCommandsBeforeCommand(
						__ts_processor, command_to_edit)).getText();
	}
	else if ( action.equals( __Commands_Set_SetMax_String)||
		command.regionMatches(true,0,"setMax",0,6) ) {
		if ( Message.isDebugOn ) {
			Message.printDebug ( dl, routine,
			"Opening dialog for setMax()" );
		}
		edited_cv = new setMax_JDialog ( this, cv,
				TSCommandProcessorUtil.getTSIdentifiersNoInputFromCommandsBeforeCommand(
						__ts_processor, command_to_edit)).getText();
	}
	else if ( action.equals( __Commands_Set_SetToMin_String)||
			command.regionMatches(true,0,"setToMin",0,8) ) {
			if ( Message.isDebugOn ) {
				Message.printDebug ( dl, routine,
				"Opening dialog for setToMin()" );
			}
			edited_cv = new setToMin_JDialog ( this, cv,
					TSCommandProcessorUtil.getTSIdentifiersNoInputFromCommandsBeforeCommand(
							__ts_processor, command_to_edit)).getText();
		}

	// Manipulate time series...

	else if ( action.equals( __Commands_Manipulate_AdjustExtremes_String)||
		command.regionMatches(true,0,"adjustExtremes",0,14) ) {
		if ( Message.isDebugOn ) {
			Message.printDebug ( dl, routine,
			"Opening dialog for adjustExtremes()" );
		}
		edited_cv = new adjustExtremes_JDialog ( this, cv,
				TSCommandProcessorUtil.getTSIdentifiersNoInputFromCommandsBeforeCommand(
						__ts_processor, command_to_edit)).getText();
	}
	else if ( action.equals( __Commands_Manipulate_ARMA_String)||
		command.regionMatches(true,0,"ARMA",0,4) ) {
		if ( Message.isDebugOn ) {
			Message.printDebug ( dl, routine,
			"Opening dialog for ARMA()" );
		}
		edited_cv = new ARMA_JDialog ( this, cv,
				TSCommandProcessorUtil.getTSIdentifiersNoInputFromCommandsBeforeCommand(
						__ts_processor, command_to_edit)).getText();
	}
	else if ( action.equals(__Commands_Manipulate_Divide_String) ||
		command.regionMatches(true,0,"divide",0,6) ) {
		if ( Message.isDebugOn ) {
			Message.printDebug ( dl, routine,
			"Opening dialog for divide()" );
		}
		edited_cv = new divide_JDialog ( this, cv,
				TSCommandProcessorUtil.getTSIdentifiersNoInputFromCommandsBeforeCommand(
						__ts_processor, command_to_edit)).getText();
	}
	else if ( action.equals(__Commands_Manipulate_Multiply_String) ||
		command.regionMatches(true,0,"multiply",0,8) ) {
		if ( Message.isDebugOn ) {
			Message.printDebug ( dl, routine,
			"Opening dialog for multiply()" );
		}
		edited_cv = new multiply_JDialog ( this, cv,
				TSCommandProcessorUtil.getTSIdentifiersNoInputFromCommandsBeforeCommand(
						__ts_processor, command_to_edit)).getText();
	}

	// General...

    else if ( action.equals(__Commands_General_Comments_ReadOnlyComment_String) ) {
        // edited_cv is just a new comment...
        if ( Message.isDebugOn ) {
            Message.printDebug ( dl, routine, "Adding @readOnly comment." );
        }
        edited_cv = new Vector(1);
        edited_cv.addElement( "#@readOnly" );
    }
	else if ( action.equals(__Commands_General_Comments_StartComment_String) ) {
		// edited_cv is just a new comment...
		if ( Message.isDebugOn ) {
			Message.printDebug ( dl, routine, "Adding start of comment block." );
		}
		edited_cv = new Vector(1);
		edited_cv.addElement( "/*" );
	}
	else if ( action.equals(__Commands_General_Comments_EndComment_String) ) {
		// edited_cv is just a new comment...
		if ( Message.isDebugOn ) {
			Message.printDebug ( dl, routine, "Adding end of comment block." );
		}
		edited_cv = new Vector(1);
		edited_cv.addElement( "*/" );
	}
	else if ( action.equals(__Commands_General_Running_Exit_String) ) {
		// No need to edit.  Just return "exit".
		edited_cv = new Vector(1);
		edited_cv.addElement( "Exit" );
	}
	else {
		// A time series identifier or other command that for whatever reason
		// does not have a custom dialog...
		edited_cv = new commandString_JDialog ( this, cv,
				TSCommandProcessorUtil.getTSIdentifiersNoInputFromCommandsBeforeCommand(
						__ts_processor, command_to_edit)).getText();
	}
	
	if ( edited_cv == null ) {
		// The edit was cancelled...
		Message.printStatus(2, routine, "Edit was cancelled.");
		return false;
	}
	else {
		// The edit occurred so commit by resetting the command string in the
		// GenericCommand to the result of the edit.
		Message.printStatus ( 2, routine, "Old-style editor, command after edit = \"" + (String)edited_cv.elementAt(0) + "\"" );
		command_to_edit.setCommandString( (String)edited_cv.elementAt(0) );
		return true;
	}
}

/**
Edit comments using an old-style editor.
@param mode Mode of editing, whether updating or inserting.
@param action If not null, then the comments are new (insert).
@param command_Vector Comments being edited as a Vector of GenericCommand, as passed from
the legacy code.
@param new_comments The new comments as a Vector of String, to be inserted
into the command list.
@return true if the command edits were committed, false if cancelled.
*/
private boolean commandList_EditCommandOldStyleComments (
		int mode, String action,
		Vector command_Vector, Vector new_comments )
{	//else if ( action.equals(__Commands_General_Comment_String) ||
	//	command.startsWith("#") ) {
	Vector cv = new Vector();
	int size = 0;
	if ( command_Vector != null ) {
		size = command_Vector.size();
	}
	Command command = null;
	for ( int i = 0; i < size; i++ ) {
		command = (Command)command_Vector.elementAt(i);
		cv.addElement( command.toString() );
	}
	Vector edited_cv = new comment_JDialog ( this, cv, null ).getText();
	if ( edited_cv == null ) {
		return false;
	}
	else {
		// Transfer to the Vector that was passed in...
		int size2 = edited_cv.size();
		for ( int i = 0; i < size2; i++ ) {
			new_comments.addElement ( edited_cv.elementAt(i) );
		}
		return true;
	}
}

/**
Get the list of commands to process, as a Vector of Command, guaranteed
to be non-null but may be zero length.
@return the commands as a Vector of Command.
@param get_all If false, return those that are selected
unless none are selected, in which case all are returned.  If true, all are
returned, regardless of which are selected.
*/
private Vector commandList_GetCommands ( boolean get_all )
{	if ( __commands_JListModel.size() == 0 ) {
		return new Vector();
	}

	int [] selected = ui_GetCommandJList().getSelectedIndices();
	int selected_size = 0;
	if ( selected != null ) {
		selected_size = selected.length;
	}

	if ( (selected_size == 0) || get_all ) {
		// Nothing selected or want to get all, get all...
		selected_size = __commands_JListModel.size();
		Vector itemVector = new Vector(selected_size);
		for ( int i = 0; i < selected_size; i++ ) {
			itemVector.addElement ( __commands_JListModel.get(i) );
		}
		return itemVector;
	}
	else {	// Else something selected so get them...
		Vector itemVector = new Vector(selected_size);
		for ( int i = 0; i < selected_size; i++ ) {
			itemVector.addElement ( __commands_JListModel.get(selected[i]) );
		}
		return itemVector;
	}
}

/**
Get the list of commands to process.  If any are selected, only they will be
returned.  If none are selected, all will be returned.
@return the commands as a Vector of String.
*/
private Vector commandList_GetCommandsBasedOnUI ( )
{	return commandList_GetCommands ( false );
}

/**
Get the list of commands to process, as a Vector of String.
@return the commands as a Vector of String.
@param get_all If false, return those that are selected
unless none are selected, in which case all are returned.  If true, all are
returned, regardless of which are selected.
*/
private Vector commandList_GetCommandStrings ( boolean get_all )
{	// Get the Command list, will not be non-null
	Vector commands = commandList_GetCommands ( get_all );
	// Convert to String instances
	int size = commands.size();
	Vector strings = new Vector(size);
	for ( int i = 0; i < size; i++ ) {
		strings.addElement ( ((Command)commands.elementAt(i)).toString() );
	}
	return strings;
}

/**
Return the number of commands with failure as max severity.
*/
private int commandList_GetFailureCount()
{
	int size = __commands_JListModel.size();
	CommandStatusProvider command;
	int failure_count = 0;
	for ( int i = 0; i < size; i++ ) {
		command = (CommandStatusProvider)__commands_JListModel.get(i);
		if ( CommandStatusUtil.getHighestSeverity(command).equals(CommandStatusType.FAILURE) ) {
			++failure_count;
		}
	}
	return failure_count;
}

/**
Get the insert position for a command based on current selections.
If no commands are selected, the insert position is the end of the list (size),
and an add at the end should occur..
If commands are selected, the insert position is the index of the first selection.
*/
private int commandList_GetInsertPosition ()
{
	int selectedIndices[] = ui_GetCommandJList().getSelectedIndices();
	int selectedSize = selectedIndices.length;

	int insert_pos = 0;
	if (selectedSize > 0) {
		// Insert before the first selected item...
		insert_pos = selectedIndices[0];
	}
	else {	// Insert at end of commands list.
		insert_pos = __commands_JListModel.size();
	}
	return insert_pos;
}

/**
Return the number of commands with warnings as maximum severity.
*/
private int commandList_GetWarningCount()
{
	int size = __commands_JListModel.size();
	CommandStatusProvider command;
	int failure_count = 0;
	for ( int i = 0; i < size; i++ ) {
		command = (CommandStatusProvider)__commands_JListModel.get(i);
		if ( CommandStatusUtil.getHighestSeverity(command).equals(CommandStatusType.WARNING) ) {
			++failure_count;
		}
	}
	return failure_count;
}

/**
Return the index position of the command from the command list.
Currently this assumes that there is a one to one correspondence between
items in the list and commands in the processor.
@param command The Command instance to determine the position in the command list.
*/
private int commandList_IndexOf ( Command command )
{	return __ts_processor.indexOf(command);
}

/**
Insert a command at the indicated position.
@param command The Command to insert.
@param pos The index in the command list at which to insert.
*/
private void commandList_InsertCommandAt ( Command command, int pos )
{
	__ts_processor.insertCommandAt( command, pos );
	ui_GetCommandJList().ensureIndexIsVisible ( pos );
	// Since an insert, mark the commands list as dirty...
	//commandList_SetDirty(true);
	ui_UpdateStatus ( false );
}

/**
Insert a new command into the command list, utilizing the selected commands in the displayed
list to determine the insert position.  If any commands are selected in the GUI, the insert will
occur before the selection. If none are selected, the insert will occur at the end of the
list.  For example this can occur in the following cases:
<ol>
<li>	The user is interacting with the command list via command menus.</li>
<li>	Time series identifiers are being transferred to the commands area from
		the query results list.</li>
</ol>
The GUI should call this method WHENEVER a command is being inserted and
is coded to respond to changes in the data model.
@param inserted_command The command to insert.
*/
private void commandList_InsertCommandBasedOnUI ( Command inserted_command )
{	String routine = getClass().getName() + ".insertCommand";

	// Get the selected indices from the commands...
	int selectedIndices[] = ui_GetCommandJList().getSelectedIndices();
	int selectedSize = selectedIndices.length;

	int insert_pos = 0;
	if (selectedSize > 0) {
		// Insert before the first selected item...
		insert_pos = selectedIndices[0];
		__commands_JListModel.insertElementAt (	inserted_command, insert_pos );
		Message.printStatus(2, routine, "Inserting command \"" +
				inserted_command + "\" at [" + insert_pos + "]" );
	}
	else {	// Insert at end of commands list.
		__commands_JListModel.addElement ( inserted_command );
		insert_pos = __commands_JListModel.size() - 1;
	}
	// Make sure that the list scrolls to the position that has been
	// updated...
	if ( insert_pos >= 0 ) {
	    ui_GetCommandJList().ensureIndexIsVisible ( insert_pos );
	}
	// Since an insert, mark the commands list as dirty...
	//commandList_SetDirty(true);
}

/**
Insert comments into the command list, utilizing the selected commands in the displayed
list to determine the insert position.
@param new_comments The comments to insert, as a Vector of String.
*/
private void commandList_InsertCommentsBasedOnUI ( Vector new_comments )
{	String routine = getClass().getName() + ".commandList_InsertCommentsBasedOnUI";

	// Get the selected indices from the commands...
	int selectedIndices[] = ui_GetCommandJList().getSelectedIndices();
	int selectedSize = selectedIndices.length;
	
	int size = new_comments.size();

	int insert_pos = 0;
	Command inserted_command = null;	// New comment line as Command
	for ( int i = 0; i < size; i++ ) {
		inserted_command = commandList_NewCommand (
				(String)new_comments.elementAt(i), true );
		if (selectedSize > 0) {
			// Insert before the first selected item...
			int insert_pos0 = selectedIndices[0];
			insert_pos = insert_pos0 + i;
			__commands_JListModel.insertElementAt (	inserted_command, insert_pos );
			Message.printStatus(2, routine, "Inserting comment \"" +
				inserted_command + "\" at [" + insert_pos + "]" );
		}
		else {	// Insert at end of commands list.
			__commands_JListModel.addElement ( inserted_command );
			insert_pos = __commands_JListModel.size() - 1;
		}
	}
	// Make sure that the list scrolls to the position that has been
	// updated...
	if ( insert_pos >= 0 ) {
	    ui_GetCommandJList().ensureIndexIsVisible ( insert_pos );
	}
	// Since an insert, mark the commands list as dirty...
	//commandList_SetDirty(true);
}

/**
Determine whether a list of commands is a comment block.
@param processor The TSCommandProcessor that is processing the results,
used to check for positions of commands.
@param commands Vector of Command instances to check.
@param all_must_be_comments If true then all must be comment lines
for true to be returned.  If false, then only one must be a comment.
This allows a warning to be printed that
only a block of ALL comments can be edited at once.
@param must_be_contigous If true, then the comments must be contiguous
for true to be returned.  The GUI code should check this and disallow
comment edits if not contiguous.
*/
private boolean commandList_IsCommentBlock ( TSCommandProcessor processor,
		Vector commands,
		boolean all_must_be_comments,
		boolean must_be_contiguous )
{
	int size_commands = commands.size();
	boolean is_comment_block = true;
	boolean is_contiguous = true;
	// Loop through the commands to check...
	Command command = null;
	int comment_count = 0;
	int pos_prev = -1;
	for ( int i = 0; i < size_commands; i++ ) {
		command = (Command)commands.elementAt(i);
		if ( commandList_IsCommentLine(command.toString()) ) {
			++comment_count;
		}
		// Get the index position in the commands processor and
		// check for contiguousness.
		int pos = processor.indexOf(command);
		if ( (i > 0) && (pos != (pos_prev + 1)) ) {
			is_contiguous = false;
		}
		// Save the position for the next check for contiguity...
		pos_prev = pos;
	}
	if ( must_be_contiguous && !is_contiguous ) {
		is_comment_block = false;
	}
	if ( all_must_be_comments && (comment_count != size_commands) ) {
		is_comment_block = false;
	}
	return is_comment_block;
}

/**
Determine whether a command line is a comment that starts with #.
@param line String command line.
@return true if a comment line, false if not.
*/
private boolean commandList_IsCommentLine ( String line )
{	if ( line == null ) {
		// Blank line...
		return true;
	}
	if ( line.length() == 0 ) {
		return true;
	}
	if ( line.charAt(0) == '#' ) {
		return true;
	}
	return false;
}

/**
Determine whether a command line is an exit command.
@param command_Vector Vector of Command - only the first one will be checked.
@return true if an exit command, false if not.
*/
private boolean commandList_IsExitCommand ( Vector command_Vector )
{	if ( command_Vector == null ) {
		return false;
	}
	if ( command_Vector.size() < 1 ) {
		return false;
	}
	Command command = (Command)command_Vector.elementAt(0);
	if ( StringUtil.startsWithIgnoreCase(command.toString(),"exit") ) {
		return true;
	}
	return false;
}
/**
Create a new Command instance given a command string.  This may be called when
loading commands from a file or adding new commands while editing.
@param command_string Command as a string, to parse and create a Command instance.
@param create_generic_command_if_not_recognized Indicate if a generic command should
be created if not recognized.  For now this should generally be true, until all
commands are recognized by the TSCommandFactory.
*/
private Command commandList_NewCommand ( String command_string,
		boolean create_generic_command_if_not_recognized )
{	int dl = 1;
	String routine = getClass().getName() + ".newCommand";
	if ( Message.isDebugOn ) {
		Message.printDebug ( dl, routine,
		"Using command factory to create a new command for \"" + command_string + "\"" );
	}
	Command c = null;
	try {
		TSCommandFactory cf = new TSCommandFactory();
		c = cf.newCommand(command_string);
		Message.printStatus ( 2, routine, "Created command from factory for \"" + command_string + "\"");
	}
	catch ( UnknownCommandException e ) {
		// Processor does not know the command so create a GenericCommand.
		c = new GenericCommand();
		Message.printStatus ( 2, routine, "Created generic command for \"" + command_string + "\"");
	}
	// TODO SAM 2007-08-31 This is essentially validation.
	// Need to evaluate for old-style commands, impacts on error-handling.
	// New is command from the processor
	try {
	    c.initializeCommand ( command_string, __ts_processor, true );	// Full initialization
		if ( Message.isDebugOn ) {
			Message.printDebug ( dl, routine, "Initialized command for \"" + command_string + "\"" );
		}
	}
	catch ( Exception e ) {
		// Absorb the warning and make the user try to deal with it in the editor
		// dialog.  They can always cancel out.

		// TODO SAM 2005-05-09 Need to handle parse error.  Should the editor come
		// up with limited information?
		Message.printWarning ( 3, routine, "Unexpected error initializing command \"" + command_string + "\"." );
		Message.printWarning ( 3, routine, e );
	}
	return c;
}

/**
Remove all commands in the list.  The delete is done independent of what is
selected in the UI.  If UI selects are relevant, use
commandList_ClearBasedOnUI.
*/
private void commandList_RemoveAllCommands ()
{	// Do this so that the status only needs to be updated once...
	Message.printStatus(2,"commandList_RemoveAllCommands", "Calling list model removeAllElements.");
	__commands_JListModel.removeAllElements();
	commandList_SetDirty ( true );
	ui_UpdateStatus ( false );
}

/**
Remove the indicated command from the command list.
@param command The Command instance to remove from the command list.
*/
private void commandList_RemoveCommand ( Command command )
{	int pos = commandList_IndexOf ( command );
	__commands_JListModel.removeElementAt(pos);
	//commandList_SetDirty(true);
	ui_UpdateStatus ( false );
}

/*
Remove selected command list, using the data model.  If any items are selected,
then only those are selected.  If none are selected, then all are cleared, asking
the user to confirm.  Items from the command list (or all if none are selected).
Save what was cleared in the __command_cut_buffer Vector so that it can be used
with Paste.
*/
private void commandList_RemoveCommandsBasedOnUI ()
{	int size = 0;
	int [] selected_indices = ui_GetCommandJList().getSelectedIndices();
	if ( selected_indices != null ) {
		size = selected_indices.length;
	}
	// Uncomment for troubleshooting...
	//String routine = getClass().getName() + ".commandList_RemoveCommandsBasedOnUI";
	//Message.printStatus ( 2, routine, "There are " + size +
	//		" commands selected for remove.  If zero all will be removed." );
	if ( (size == __commands_JListModel.size()) || (size == 0) ) {
		int x = new ResponseJDialog ( this, "Delete Commands?",
			"Are you sure you want to delete ALL the commands?",
			ResponseJDialog.YES|ResponseJDialog.NO).response();
		if ( x == ResponseJDialog.NO ) {
			return;
		}
	}
	if ( size == 0 ) {
		// Nothing selected so remove all...
		__commands_JListModel.removeAllElements();
	}
	else {
	    // Need to remove from back of selected_indices so that removing
		// elements will not affect the index of items before that
		// index.  At some point need to add an undo feature.
		JGUIUtil.setWaitCursor ( this, true );
		ui_SetIgnoreItemEvent ( true );
		ui_SetIgnoreListSelectionEvent ( true );
		for ( int i = (size - 1); i >= 0; i-- ) {
			__commands_JListModel.removeElementAt (	selected_indices[i] );
		}
		ui_SetIgnoreItemEvent ( false );
		ui_SetIgnoreListSelectionEvent ( false );
		selected_indices = null;
		JGUIUtil.setWaitCursor ( this, false );
	}
	commandList_SetDirty ( true );
	results_TimeSeries_Clear();
	ui_UpdateStatus ( true );
}

/**
Replace a command with another.  This is used, for example, when converting commands
to/from comments.
@param old_command Old command to remove.
@param new_command New command to insert in its place.
*/
private void commandList_ReplaceCommand ( Command old_command, Command new_command )
{
	// Probably could get the index passed in from list operations but
	// do the lookup through the data model to be more independent.
	int pos_old = __ts_processor.indexOf(old_command);
	if ( pos_old < 0 ) {
		// Can't find the old command so return.
		return;
	}
	// Remove the old command...
	__ts_processor.removeCommandAt ( pos_old );
	// Insert the new command at the same position.  Handle the case that
	// it is now at the end of the list.
	if ( pos_old < __ts_processor.size() ) {
		// Have enough elements to add at the requested position...
		__ts_processor.insertCommandAt( new_command, pos_old );
	}
	else {
		// Add at the end...
		__ts_processor.addCommand ( new_command );
	}
	// Refresh the GUI...
	//commandList_SetDirty ( true );
	ui_UpdateStatus ( false );
}

/**
Replace a contiguous block of # comments with another block.
@param old_comments Vector of old comments (as Command) to remove.
@param new_comments Vector of new comments (as String) to insert in its place.
*/
private void commandList_ReplaceComments ( Vector old_comments, Vector new_comments )
{	//String routine = getClass().getName() + ".commandList_ReplaceComments";
	// Probably could get the index passed in from list operations but
	// do the lookup through the data model to be more independent.
	int pos_old = __ts_processor.indexOf((Command)old_comments.elementAt(0));
	if ( pos_old < 0 ) {
		// Can't find the old command so return.
		return;
	}
	// Remove the old commands.  They will shift so OK to keep removing at
	// the single index.
	int size = old_comments.size();
	for ( int i = 0; i < size; i++ ) {
		__ts_processor.removeCommandAt ( pos_old );
	}
	// Insert the new commands at the same position.  Handle the case that
	// it is now at the end of the list.
	int size_new = new_comments.size();
	if ( pos_old < __ts_processor.size() ) {
		// Have enough elements to add at the requested position...
		for ( int i = 0; i < size_new; i++ ) {
			Command new_command = commandList_NewCommand (
					(String)new_comments.elementAt(i), true );
			//Message.printStatus ( 2, routine, "Inserting " + new_command +
			//		" at " + (pos_old + 1));
			__ts_processor.insertCommandAt( new_command, (pos_old + i) );
		}
	}
	else {
		// Add at the end...
		for ( int i = 0; i < size_new; i++ ) {
			Command new_command = commandList_NewCommand (
					(String)new_comments.elementAt(i), true );
			//Message.printStatus ( 2, routine, "Adding " + new_command +
			//		" at end" );
			__ts_processor.addCommand ( new_command );
		}
	}
	// Refresh the GUI...
	//commandList_SetDirty ( true );
	ui_UpdateStatus ( false );
}

/**
Select the command and optionally position the view at the command.
@param iline Command position (0+).
@param ensure_visible If true, the list will be scrolled to the selected item.
This may be undesirable if selecting many items.
*/
private void commandList_SelectCommand ( int iline, boolean ensure_visible )
{	ui_GetCommandJList().setSelectedIndex ( iline );
	if ( ensure_visible ) {
		// Position the list to make the selected item visible...
	    ui_GetCommandJList().ensureIndexIsVisible(iline);
	}
	ui_UpdateStatus ( false);
}

/**
Set the command file name.  This also will refresh any interface components
that display the command file name.  It DOES NOT cause the commands to be
reloaded - it is a simple setter.
@param command_file_name Name of current command file (can be null).
*/
private void commandList_SetCommandFileName ( String command_file_name )
{	// Set the file name used in the TSTool UI...
	__command_file_name = command_file_name;
	// Also set the initial working directory for the processor as the
	// parent folder of the command file...
	File file = new File ( command_file_name );
	commandProcessor_SetInitialWorkingDir ( file.getParent() );
	// Update the title bar...
	ui_UpdateStatus ( false );
}

/**
Indicate whether the commands have been modified.  The application title is
also updated to indicate this.
@param dirty Specify as true if the commands have been modified in some way.
*/
private void commandList_SetDirty ( boolean dirty )
{	__commands_dirty = dirty;
	ui_UpdateStatus ( false );
	// TODO SAM 2007-08-31 Evaluate whether processor should have "dirty" property.
}

/**
Convert the specified commands to a Vector of String.  This may not be
the best place for this method but ok for now.
@param commands Vector of Command.
*/
private Vector commandList_ToStringVector ( Vector commands )
{	Vector strings = new Vector();
	int size = commands.size();
	for ( int i = 0; i < size; i++ ) {
		strings.addElement ( "" + commands.elementAt(i) );
	}
	return strings;
}

/**
Indicate the progress that is occurring within a command.  This may be a chained call
from a CommandProcessor that implements CommandListener to listen to a command.  This
level of monitoring is useful if more than one progress indicator is present in an
application UI.
@param istep The number of steps being executed in a command (0+).
@param nstep The total number of steps to process within a command.
@param command The reference to the command that is starting to run,
provided to allow future interaction with the command.
@param percent_complete If >= 0, the value can be used to indicate progress
running a single command (not the single command).  If less than zero, then
no estimate is given for the percent complete and calling code can make its
own determination (e.g., ((istep + 1)/nstep)*100).
@param message A short message describing the status (e.g., "Running command ..." ).
*/
public void commandProgress ( int istep, int nstep, Command command,
		float percent_complete, String message )
{	if ( istep == 0 ) {
		// Initialize the limits of the command progress bar.
		__command_JProgressBar.setMinimum ( 0 );
		__command_JProgressBar.setMaximum ( nstep );
	}
	// Set the current value...
	__command_JProgressBar.setValue ( istep + 1 );
}

// All of the following methods perform and interaction with the command processor,
// beyond basic command list insert/delete/update.

/**
Clear the time series results in the command processor.
*/
private void commandProcessor_ClearResults()
{
	// Clear the time series in the processor...
	__ts_processor.clearResults();
}

/**
Get the command processor AutoExtendPeriod.  This method is meant for simple
reporting.  Any errors in the processor should be detected during command
initialization and processing.
@return The global AutoExtendPeriod as a Boolean from the command processor or null if
not yet determined.
*/
private Boolean commandProcessor_GetAutoExtendPeriod()
{	if ( __ts_processor == null ) {
		return null;
	}
	Object o = null;
	try {	o = __ts_processor.getPropContents ( "AutoExtendPeriod" );
	}
	catch ( Exception e ) {
		return null;
	}
	if ( o == null ) {
		return null;
	}
	else { return (Boolean)o;
	}
}

/**
Return the command processor instance that is being used.  This method should be
called to avoid direct interaction with the processor data member.
@return the TSCommandProcessor instance that is being used.
*/
private TSCommandProcessor commandProcessor_GetCommandProcessor ()
{
    return __ts_processor;
}

/**
Get the command processor time series ensemble results for an index position.
Typically this corresponds to a user selecting the ensemble from the
results list, for further display.
@param pos Position (0+ for the time series).
@return The time series ensemble at the requested position in the results or null
if the processor is not available.
*/
private TSEnsemble commandProcessor_GetEnsembleAt( int pos )
{   String message, routine = "TSTool_JFrame.commandProcessorGetEnsembleAt";
    if ( __ts_processor == null ) {
        return null;
    }
    PropList request_params = new PropList ( "" );
    request_params.setUsingObject ( "Index", new Integer(pos) );
    CommandProcessorRequestResultsBean bean = null;
    try {
        bean = __ts_processor.processRequest( "GetEnsembleAt", request_params);
    }
    catch ( Exception e ) {
        message = "Error requesting GetEnsembleAt(Index=" + pos + ") from processor.";
        Message.printWarning(2, routine, message );
        Message.printWarning ( 3, routine, e );
    }
    PropList bean_PropList = bean.getResultsPropList();
    Object o_TSEnsemble = bean_PropList.getContents ( "TSEnsemble" );
    TSEnsemble tsensemble = null;
    if ( o_TSEnsemble == null ) {
        message = "Null TSEnsemble returned from processor for GetEnsembleAt(Index=\"" + pos + "\").";
        Message.printWarning ( 2, routine, message );
    }
    else {
        tsensemble = (TSEnsemble)o_TSEnsemble;
    }
    return tsensemble;
}

/**
Get the command processor table results list.
@return The table results list or null
if the processor is not available.
*/
private List commandProcessor_GetEnsembleResultsList()
{   String routine = "TSTool_JFrame.commandProcessorGetEnsembleResultsList";
    Object o = null;
    try {
        o = __ts_processor.getPropContents ( "EnsembleResultsList" );
    }
    catch ( Exception e ) {
        String message = "Error requesting EnsembleResultsList from processor.";
        Message.printWarning(2, routine, message );
    }
    if ( o == null ) {
        return null;
    }
    else {
        return (List)o;
    }
}

/**
Get the size of the command processor ensemble results list.
@return the size of the command processor ensemble results list.
*/
private int commandProcessor_GetEnsembleResultsListSize()
{
    List results = commandProcessor_GetEnsembleResultsList();
    if ( results == null ) {
        return 0;
    }
    return results.size();
}

/**
Get the command processor HydroBaseDMIList.  This method is meant for simple
reporting.  Any errors in the processor should be detected during command
initialization and processing.
@return The HydroBaseDMIList as a Vector from the command processor or null if
not yet determined or no connections.
*/
private Vector commandProcessor_GetHydroBaseDMIList()
{	if ( __ts_processor == null ) {
		return null;
	}
	Object o = null;
	try {	o = __ts_processor.getPropContents ( "HydroBaseDMIList" );
	}
	catch ( Exception e ) {
		return null;
	}
	if ( o == null ) {
		return null;
	}
	else { return (Vector)o;
	}
}

/**
Get the command processor IncludeMissingTS.  This method is meant for simple
reporting.  Any errors in the processor should be detected during command
initialization and processing.
@return The global IncludeMissingTS as a Boolean from the command processor or null if
not yet determined.
*/
private Boolean commandProcessor_GetIncludeMissingTS()
{	if ( __ts_processor == null ) {
		return null;
	}
	Object o = null;
	try {	o = __ts_processor.getPropContents ( "IncludeMissingTS" );
	}
	catch ( Exception e ) {
		return null;
	}
	if ( o == null ) {
		return null;
	}
	else { return (Boolean)o;
	}
}

/**
Get the command processor InputEnd.  This method is meant for simple
reporting.  Any errors in the processor should be detected during command
initialization and processing.
@return The global InputEnd as a DateTime from the command processor or null if
not yet determined.
*/
private DateTime commandProcessor_GetInputEnd()
{	if ( __ts_processor == null ) {
		return null;
	}
	Object o = null;
	try {	o = __ts_processor.getPropContents ( "InputEnd" );
	}
	catch ( Exception e ) {
		return null;
	}
	if ( o == null ) {
		return null;
	}
	else { return (DateTime)o;
	}
}

/**
Get the command processor InputStart.  This method is meant for simple
reporting.  Any errors in the processor should be detected during command
initialization and processing.
@return The global InputStart as a DateTime from the command processor or null if
not yet determined.
*/
private DateTime commandProcessor_GetInputStart()
{	if ( __ts_processor == null ) {
		return null;
	}
	Object o = null;
	try {	o = __ts_processor.getPropContents ( "InputStart" );
	}
	catch ( Exception e ) {
		return null;
	}
	if ( o == null ) {
		return null;
	}
	else { return (DateTime)o;
	}
}

/**
Get the command processor OutputEnd.  This method is meant for simple
reporting.  Any errors in the processor should be detected during command
initialization and processing.
@return The global OutputEnd as a DateTime from the command processor or null if
not yet determined.
*/
private DateTime commandProcessor_GetOutputEnd()
{	if ( __ts_processor == null ) {
		return null;
	}
	Object o = null;
	try {	o = __ts_processor.getPropContents ( "OutputEnd" );
	}
	catch ( Exception e ) {
		return null;
	}
	if ( o == null ) {
		return null;
	}
	else { return (DateTime)o;
	}
}

/**
Get the command processor OutputStart.  This method is meant for simple
reporting.  Any errors in the processor should be detected during command
initialization and processing.
@return The global OutputStart as a DateTime from the command processor or null if
not yet determined.
*/
private DateTime commandProcessor_GetOutputStart()
{	if ( __ts_processor == null ) {
		return null;
	}
	Object o = null;
	try {	o = __ts_processor.getPropContents ( "OutputStart" );
	}
	catch ( Exception e ) {
		return null;
	}
	if ( o == null ) {
		return null;
	}
	else { return (DateTime)o;
	}
}

/**
Get the command processor table results for a table identifier.
Typically this corresponds to a user selecting the time series from the
results list, for further display.
@param table_id Table identifier to display.
@return The matching table or null if not available from the processor.
*/
private DataTable commandProcessor_GetTable ( String table_id )
{   String message, routine = "TSTool_JFrame.commandProcessorGetTimeSeries";
    if ( __ts_processor == null ) {
        return null;
    }
    PropList request_params = new PropList ( "" );
    request_params.set ( "TableID", table_id );
    CommandProcessorRequestResultsBean bean = null;
    try { bean =
        __ts_processor.processRequest( "GetTable", request_params);
    }
    catch ( Exception e ) {
        message = "Error requesting GetTable(TableID=\"" + table_id + "\") from processor.";
        Message.printWarning(2, routine, message );
        Message.printWarning ( 3, routine, e );
    }
    PropList bean_PropList = bean.getResultsPropList();
    Object o_table = bean_PropList.getContents ( "Table" );
    DataTable table = null;
    if ( o_table == null ) {
        message = "Null table returned from processor for GetTable(TableID=\"" + table_id + "\").";
        Message.printWarning ( 2, routine, message );
    }
    else {
        table = (DataTable)o_table;
    }
    return table;
}

/**
Get the command processor table results list.
@return The table results list or null
if the processor is not available.
*/
private List commandProcessor_GetTableResultsList()
{   String routine = "TSTool_JFrame.commandProcessorGetTableResultsList";
    Object o = null;
    try {
        o = __ts_processor.getPropContents ( "TableResultsList" );
    }
    catch ( Exception e ) {
        String message = "Error requesting TableResultsList from processor.";
        Message.printWarning(2, routine, message );
    }
    if ( o == null ) {
        return null;
    }
    else {
        return (List)o;
    }
}

/**
Get the command processor time series results for an index position.
Typically this corresponds to a user selecting the time series from the
results list, for further display.
@param pos Position (0+ for the time series).
@return The time series at the requested position in the results or null
if the processor is not available.
*/
private TS commandProcessor_GetTimeSeries( int pos )
{	String message, routine = "TSTool_JFrame.commandProcessorGetTimeSeries";
	if ( __ts_processor == null ) {
		return null;
	}
	PropList request_params = new PropList ( "" );
	request_params.setUsingObject ( "Index", new Integer(pos) );
	CommandProcessorRequestResultsBean bean = null;
	try { bean =
		__ts_processor.processRequest( "GetTimeSeries", request_params);
	}
	catch ( Exception e ) {
		message = "Error requesting GetTimeSeries(Index=" + pos + ") from processor.";
		Message.printWarning(2, routine, message );
		Message.printWarning ( 3, routine, e );
	}
	PropList bean_PropList = bean.getResultsPropList();
	Object o_TS = bean_PropList.getContents ( "TS" );
	TS ts = null;
	if ( o_TS == null ) {
		message = "Null TS returned from processor for GetTimeSeries(Index=\"" + pos + "\").";
		Message.printWarning ( 2, routine, message );
	}
	else {	ts = (TS)o_TS;
	}
	return ts;
}

/**
Get the command processor time series results list.
@return The time series results list or null
if the processor is not available.
*/
private Vector commandProcessor_GetTimeSeriesResultsList()
{	String routine = "TSTool_JFrame.commandProcessorGetTimeSeriesResultsList";
	Object o = null;
	try { o = __ts_processor.getPropContents ( "TSResultsList" );
	}
	catch ( Exception e ) {
		String message = "Error requesting TSResultsList from processor.";
		Message.printWarning(2, routine, message );
	}
	if ( o == null ) {
		return null;
	}
	else {
		return (Vector)o;
	}
}

/**
Get the size of the command processor time series results list.
@return the size of the command processor time series results list.
*/
private int commandProcessor_GetTimeSeriesResultsListSize()
{
	Vector results = commandProcessor_GetTimeSeriesResultsList();
	if ( results == null ) {
		return 0;
	}
	return results.size();
}

/**
Get the working directory for a command (e.g., for editing).
*/
private String commandProcessor_GetWorkingDirForCommand ( Command command )
{	
	return TSCommandProcessorUtil.getWorkingDirForCommand( __ts_processor, command );
}

// TODO SAM 2008-01-05 Evaluate whether this should live in the processor.
/**
Process ensembles from the results list into a product (graph, etc).
@param indices Indices of the ensembles to process.
@param props Properties to control output (graph type).
*/
private void commandProcessor_ProcessEnsembleResultsList ( int [] indices, PropList props )
{   String routine = "TSTool_JFrame.commandProcessor_ProcessEnsembleResultsList";
    if ( indices == null ) {
        // No ensembles are selected so return.
        Message.printWarning( 1, "", "Select an ensemble to graph." );
        return;
    }
    // Translate the ensemble indices into time series indices
    // Get the list of time series results...
    Object o_tslist = null;
    try {
        o_tslist = __ts_processor.getPropContents ( "TSResultsList" );
    }
    catch ( Exception e ) {
        String message = "Error getting time series from processor.";
        Message.printWarning(1, routine, message );
        Message.printWarning(3, routine, e );
        return;
    }
    if ( o_tslist == null ) {
        String message = "No time series are available from processor - null list.";
        Message.printWarning(1, routine, message );
        return;
    }
    Vector tslist = (Vector)o_tslist;
    int tslist_size = tslist.size();
    if ( tslist_size == 0 ) {
        String message = "No time series are available from processor.";
        Message.printWarning(1, routine, message );
        return;
    }
    // Get the list of ensembles...
    Vector match_index_Vector = new Vector();   // List of matching time series indices
    for ( int i = 0; i < indices.length; i++ ) {
        PropList request_params = new PropList ( "" );
        // String is of format 1) EnsembleID - EnsembleName
        String EnsembleID = StringUtil.getToken((String)__results_tsensembles_JListModel.elementAt(indices[i]),
                " -",StringUtil.DELIM_SKIP_BLANKS,1);
        Message.printStatus ( 2, routine, "Getting processor ensemble results for \"" + EnsembleID + "\"" );
        request_params.setUsingObject ( "EnsembleID", EnsembleID );
        CommandProcessorRequestResultsBean bean = null;
        try {
            bean = __ts_processor.processRequest( "GetEnsemble", request_params );
        }
        catch ( Exception e ) {
            String message = "Error requesting GetEnsemble(EnsembleID=\"" + EnsembleID + "\") from processor.";
            Message.printWarning(2, routine, message );
            return;
        }
        PropList results_props = bean.getResultsPropList();
        Object o_ensemble = results_props.getContents ( "TSEnsemble" );
        if ( o_ensemble == null ) {
            String message = "Null ensemble requesting GetEnsemble(EnsembleID=\"" + EnsembleID + "\") from processor.";
            Message.printWarning(2, routine, message );
            return;
        }
        TSEnsemble ensemble = (TSEnsemble)o_ensemble;
        // Loop through the time series in the ensemble and add to the time series index list for matches
        int ensemble_size = ensemble.size();
        TS ens_ts, ts;
        for ( int iens = 0; iens < ensemble_size; iens++ ) {
            ens_ts = (TS)ensemble.get(iens);
            for ( int its = 0; its < tslist_size; its++ ) {
                ts = (TS)tslist.elementAt(its);
                if ( ens_ts == ts ) {
                    // Have a matching time series
                    match_index_Vector.addElement ( new Integer(its));
                }
            }
        }
    }
    // Convert the indices to an array
    int [] indices2 = new int[match_index_Vector.size()];
    for ( int i = 0; i < indices2.length; i++ ) {
        indices2[i] = ((Integer)match_index_Vector.elementAt(i)).intValue();
    }
    // Now display the list of time series
    PropList request_params = new PropList ( "" );
    request_params.setUsingObject ( "Indices", indices2 );
    request_params.setUsingObject ( "Properties", props );

    try { //bean =
        __ts_processor.processRequest( "ProcessTimeSeriesResultsList", request_params );
    }
    catch ( Exception e ) {
        String message = "Error requesting ProcessTimeSeriesResultsList(Indices=\"" + indices + "\"," +
        " Properties=\"" + props + "\") from processor.";
        Message.printWarning(2, routine, message );
    }
}

/**
Process time series from the results list into a product (graph, etc).
*/
private void commandProcessor_ProcessTimeSeriesResultsList ( int [] indices, PropList props )
{	String routine = "TSTool_JFrame.commandProcessorProcessTimeSeriesResultsList";
	PropList request_params = new PropList ( "" );
	request_params.setUsingObject ( "Indices", indices );
	request_params.setUsingObject ( "Properties", props );
	//CommandProcessorRequestResultsBean bean = null;
	try { //bean =
		__ts_processor.processRequest( "ProcessTimeSeriesResultsList", request_params );
	}
	catch ( Exception e ) {
		String message = "Error requesting ProcessTimeSeriesResultsList(Indices=\"" + indices + "\"," +
		" Properties=\"" + props + "\") from processor.";
		Message.printWarning(2, routine, message );
	}
}

/**
Read and load a command file into the processor.
@param path Absolute path to the command file to read.
@return the number of lines that are automatically changed during the read (1 if the size is
different after read).
@exception IOException if there is an error reading the command file.
*/
private int commandProcessor_ReadCommandFile ( String path )
throws IOException
{	String routine = "TSTool_JFrame.commandProcessor_ReadCommandFile";
    // Set the command file for use with output...
    // FIXME SAM 2008-09-04 Confirm no negative effects from taking this out
	//IOUtil.setProgramCommandFile ( path );
	__ts_processor.readCommandFile ( path,
			true,	// Create GenericCommand instances for unrecognized commands
			false );// Do not append to the current processor contents
    // Refresh the GUI list to show the status done in call to this method
	
	// TODO SAM 2008-05-11 Evaluate whether to move this to the readCommandFile() method.
	// If any lines in the file are different from the commands, mark the file as dirty.
	// Changes may automatically occur during the load because of automated updates to
	// commands.
	BufferedReader in = new BufferedReader ( new InputStreamReader(IOUtil.getInputStream ( path )) );
	Vector strings = new Vector();
	String line;
	while ( true ) {
	    line = in.readLine();
	    if ( line == null ) {
	        break;
	    }
	    strings.add ( line );
	}
	in.close();
	int size_orig = strings.size();
	if ( size_orig != __ts_processor.size() ) {
	    Message.printStatus( 2, routine, "Command list was modified during load (different length)." );
	    commandList_SetDirty ( true );
	    return 1;
	}
	// Go through all the commands.
	Command command = null;
	CommandStatusProvider csp = null;
	int numAutoChanges = 0;
	for ( int i = 0; i < size_orig; i++ ) {
	    line = (String)strings.get(i);
	    command = __ts_processor.get(i);
	    if ( !line.equals(command.toString()) ) {
	        Message.printStatus( 2, routine, "Command " + (i + 1) +
	                " was automatically updated during load (usually due to software update)." );
	        commandList_SetDirty ( true );
	        ++numAutoChanges;
	        if ( command instanceof CommandStatusProvider ) {
	            csp = (CommandStatusProvider)command;
	            // FIXME SAM 2008-05-11 This message gets clobbered by reinitialization before running
	            // Add a message that the command was updated during load.
	            csp.getCommandStatus().addToLog ( CommandPhaseType.INITIALIZATION,
	                    new CommandLogRecord(CommandStatusType.UNKNOWN,
	                            "Command was automatically updated during load (usually due to software update).",
	                            "Should not need to do anything." ) );
	        }
	    }
	}
	return numAutoChanges;
}

/**
Run the commands through the processor.  Currently this supplies the list of
Command instances to run because the user can select the commands in the
interface.  In the future the command processor may put together the list without
being passed from the GUI.
@param ts_processor The TSCommandProcessor to run.  Typically this will be the
main processor but it will be a temporary instance when processing the
working directory for edit dialogs.
@param commands The list of commands to process.
@param create_output Indicate whether output files should be created.  Doing so results
in some performance degredation.
*/
private void commandProcessor_RunCommands (
		TSCommandProcessor ts_processor,
		Vector commands,
		boolean create_output )
{	String routine = "TSTool_JFrame.commandProcessorRunCommands";
	PropList request_params = new PropList ( "" );
	request_params.setUsingObject ( "CommandList", commands );
	// FIXME SAM 2007-10-13 Remove when test out.  The initial directory needs to be set
	// when a command file is initialized.
	//request_params.setUsingObject ( "InitialWorkingDir", getInitialWorkingDir() );
	request_params.setUsingObject ( "CreateOutput", new Boolean(create_output) );
	Message.printStatus ( 2, routine, "Running commands in GUI thread.");
	try { 
		ts_processor.processRequest( "RunCommands", request_params );
	}
	catch ( Exception e ) {
		String message = "Error requesting RunCommands(CommandList=...) from processor.";
		Message.printWarning ( 2, routine, message );
		Message.printWarning ( 3,routine, e );
	}
}

/**
Run the commands through the processor.  Curently this supplies the list of
Command instances to run because the user can select the commands in the
interface.  In the future the command processor may put together the list without
being passed from the GUI.
*/
private void commandProcessor_RunCommandsThreaded ( Vector commands, boolean create_output )
{	String routine = "TSTool_JFrame.commandProcessor_RunCommandsThreaded";

	PropList request_params = new PropList ( "" );
	request_params.setUsingObject ( "CommandList", commands );
	request_params.setUsingObject ( "InitialWorkingDir", ui_GetInitialWorkingDir() );
	request_params.setUsingObject ( "CreateOutput", new Boolean(create_output) );
	try {
		TSCommandProcessorThreadRunner runner =
		new TSCommandProcessorThreadRunner ( __ts_processor, request_params );
		Message.printStatus ( 2, routine, "Running commands in separate thread.");
		Thread thread = new Thread ( runner );
		thread.start();
		// Do one update of the GUI to reflect the GUI running.  This will disable run
		// buttons, etc. until the current run is done.
		ui_CheckGUIState ();
		// At this point the GUI will get updated if any notification fires from the
		// processor.
	}
	catch ( Exception e ) {
		String message = "Error running command processor in thread.";
		Message.printWarning(2, routine, message );
		Message.printWarning (3,routine, e);
	}
}

/**
Set the command processor HydroBase instance that is opened via the GUI.
@param hbdmi Open HydroBaseDMI instance.
The input name is blank since it is the default HydroBaseDMI.
*/
private void commandProcessor_SetHydroBaseDMI( HydroBaseDMI hbdmi )
{	// Call the overloaded method that takes a processor as a parameter...
	commandProcessor_SetHydroBaseDMI( __ts_processor, hbdmi );
}

/**
Set the command processor HydroBase instance that is opened via the GUI.
This version is generally called by the overloaded version and when processing
an external command file.
@param processor The command processor to set the HydroBase DMI instance.
@param hbdmi Open HydroBaseDMI instance.
The input name is blank since it is the default HydroBaseDMI.
*/
private void commandProcessor_SetHydroBaseDMI( CommandProcessor processor, HydroBaseDMI hbdmi )
{	String message, routine = "TSTool_JFrame.setCommandProcessorHydroBaseDMI";
	if ( hbdmi == null ) {
		return;
	}
	PropList request_params = new PropList ( "" );
	request_params.setUsingObject ( "HydroBaseDMI", hbdmi );
	//CommandProcessorRequestResultsBean bean = null;
	try { //bean =
		processor.processRequest( "SetHydroBaseDMI", request_params );
	}
	catch ( Exception e ) {
		message = "Error requesting SetHydroBaseDMI(HydroBaseDMI=\"" + hbdmi + " from processor.";
		Message.printWarning(2, routine, message );
	}
}

/**
Set the command processor initial working directory.
@param dir Initial working directory.
*/
private void commandProcessor_SetInitialWorkingDir ( String InitialWorkingDir )
{
	try {
		__ts_processor.setPropContents( "InitialWorkingDir", InitialWorkingDir );
	}
	catch ( Exception e ) {
		String routine = getClass().getName() + ".commandProcessor_setInitialWorkingDir";
		String message = "Error setting InitialWorkingDir(\"" + InitialWorkingDir + "\") in processor.";
		Message.printWarning(2, routine, message );
	}
}

/**
Set the command processor NWSRFS FS5Files DMI instance that is opened via the GUI.
@param nwsrfs_dmi Open NWSRFS FS5Files instance.
*/
private void commandProcessor_SetNWSRFSFS5FilesDMI( NWSRFS_DMI nwsrfs_dmi )
{	commandProcessor_SetNWSRFSFS5FilesDMI( __ts_processor, nwsrfs_dmi );
}

/**
Set the command processor NWSRFS FS5Files DMI instance that is opened via the GUI.
Typically this method is only called when running an external command file.
@param processor the Processor that is being updated.
@param nwsrfs_dmi Open NWSRFS FS5Files instance.
*/
private void commandProcessor_SetNWSRFSFS5FilesDMI( TSCommandProcessor processor, NWSRFS_DMI nwsrfs_dmi )
{	String message, routine = "TSTool_JFrame.setCommandProcessorNWSRFSFS5FilesDMI";
	if ( nwsrfs_dmi == null ) {
		return;
	}
	PropList request_params = new PropList ( "" );
	request_params.setUsingObject ( "NWSRFSFS5FilesDMI", nwsrfs_dmi );
	//CommandProcessorRequestResultsBean bean = null;
	try { //bean =
		processor.processRequest( "SetNWSRFSFS5FilesDMI", request_params );
	}
	catch ( Exception e ) {
		message = "Error requesting SetNWSRFSFS5FilesDMI(NWSRFSFS5FilesDMI=\"" + nwsrfs_dmi + " from processor.";
		Message.printWarning(2, routine, message );
	}
}

/**
Set the command processor RiversideDB instance that is opened via the GUI.
This version is generally called by the overloaded version and when processing
an external command file.
@param processor The command processor to set the RiversideDB DMI instance.
@param rdmi Open RiversideDB_DMI instance.
The input name is blank since it is the default RiversideDB_DMI.
*/
private void commandProcessor_SetRiversideDB_DMI( RiversideDB_DMI rdmi )
{   String message, routine = "TSTool_JFrame.commandProcessor_SetRiversideDBDMI";
    if ( rdmi == null ) {
        return;
    }
    CommandProcessor processor = commandProcessor_GetCommandProcessor();
    PropList request_params = new PropList ( "" );
    request_params.setUsingObject ( "RiversideDB_DMI", rdmi );
    //CommandProcessorRequestResultsBean bean = null;
    try { //bean =
        processor.processRequest( "SetRiversideDB_DMI", request_params );
    }
    catch ( Exception e ) {
        message = "Error requesting SetRiversideDB_DMI(RiversideDB_DMI=\"" + rdmi + "\") from processor.";
        Message.printWarning(2, routine, message );
    }
}

/**
Indicate that a command has started running.
@param icommand The command index (0+).
@param ncommand The total number of commands to process
@param command The reference to the command that is starting to run,
provided to allow future interaction with the command.
@param percent_complete If >= 0, the value can be used to indicate progress
running a list of commands (not the single command).  If less than zero, then
no estimate is given for the percent complete and calling code can make its
own determination (e.g., ((icommand + 1)/ncommand)*100).
@param message A short message describing the status (e.g., "Running command ..." ).
*/
public void commandStarted ( int icommand, int ncommand, Command command,
		float percent_complete, String message )
{	String routine = "TSTool_JFrame.commandStarted";
	// commandCompleted updates the progress bar after each command.
	// For this method, only reset the bounds of the progress bar and
	// clear if the first command.
	String command_string = command.toString();
	ui_UpdateStatusTextFields ( 1, routine, "Processing \"" + command_string + "\"",
			null, __STATUS_BUSY );
	if ( icommand == 0 ) {
		__processor_JProgressBar.setMinimum ( 0 );
		__processor_JProgressBar.setMaximum ( ncommand );
		__processor_JProgressBar.setValue ( 0 );
		Message.printStatus(2, getClass().getName()+".commandStarted", "Setting processor progress bar limits to 0 to " + ncommand );
	}
	// Always set the value for the command progres so that it shows up
	// as zero.  The commandProgres() method will do a better job of setting
	// the limits and current status for a specific command.
	__command_JProgressBar.setMinimum ( 0 );
	__command_JProgressBar.setMaximum ( 100 );
	__command_JProgressBar.setValue ( 0 );
}

/**
Required by ListDataListener - receive notification when the contents of the
commands list have changed.
*/
public void contentsChanged ( ListDataEvent e )
{
	// The contents of the command list chagned so check the GUI state...
	ui_UpdateStatus ( true );	// true = also call checkGUIState();
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

// TODO SAM 2007-11-02 Review code.  Should this do the same as a select?
/**
Do nothing.  
@param devlimits Limits of select in device coordinates(pixels).
@param datalimits Limits of select in data coordinates.
@param selected Vector of selected GeoRecord.  Currently ignored.
*/
public void geoViewInfo(GRShape devlimits, GRShape datalimits,
Vector selected)
{
}

/**
Do nothing.
*/
public void geoViewInfo(GRPoint devlimits, GRPoint datalimits, 
Vector selected)
{
}

/**
Do nothing.
*/
public void geoViewInfo(GRLimits devlimits, GRLimits datalimits,
Vector selected)
{
}

/**
Handle the mouse motion event from another GeoView (likely a ReferenceGeoView).
Does nothing.
@param devpt Coordinates of mouse in device coordinates(pixels).
@param datapt Coordinates of mouse in data coordinates.
*/
public void geoViewMouseMotion(GRPoint devpt, GRPoint datapt)
{
}

// TODO SAM 2006-03-02 Evaluate code
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

	// TODO SAM 2006-03-02 Need to evaluate how to best read this file once.

	// Read the time series to layer lookup file...

	String filename = TSToolMain.getPropValue ( "TSTool.MapLayerLookupFile" );
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
	ui_SetIgnoreListSelectionEvent ( true );	// To increase performance
						// during transfer...
	ui_SetIgnoreItemEvent ( true );	// To increase performance
	int match_count = 0;
	int ngeo = 0;
	if ( selected != null ) {
		ngeo = selected.size();
	}
	// Loop through the selected features...
	GeoRecord georec;
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
				// TODO SAM 2006-03-02 Evaluate code
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

			// TODO SAM 2006-03-02 Add attributes other than strings
			// For now only deal with string attributes -
			// later need to handle other types.

			// Get the attribute to be checked for the ID,
			// based on the attribute name in the lookup
			// file...

			// TODO SAM 2006-03-02 Optimize code
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
				// TODO SAM 2006-03-02 Refactor/optimize
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
				// TIDI SAM 2006-03-02 Optimize/refactor
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
				// TODO SAM 2006-03-02 Evaluate usability
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
	ui_SetIgnoreListSelectionEvent ( false );
	ui_SetIgnoreItemEvent ( false );
	ui_UpdateStatus ( true );
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
		else {
		    // Get the command number from the first token...
			command_line = StringUtil.getToken(tag,",",0,0);
		}
	}
	else {
	    // Get the command number from the only tag...
		if ( StringUtil.isInteger(tag) ) {
			command_line = tag;
		}
	}
	if ( StringUtil.isInteger(command_line) ) {
		int iline = StringUtil.atoi(command_line) - 1;
		if ( (iline >= 0) && (iline < __commands_JListModel.size()) ) {
			// Clear previous selections...
		    ui_GetCommandJList().clearSelection();
			// Select the current tag...
			commandList_SelectCommand ( iline, true );
		}
	}
}

/**
Required by ListDataListener - receive notification when the contents of the
commands list have changed due to commands being added.
*/
public void intervalAdded ( ListDataEvent e )
{
	// The contents of the command list chagned so check the GUI state...
	ui_UpdateStatus ( true );	// true = also call checkGUIState();
}

/**
Required by ListDataListener - receive notification when the contents of the
commands list have changed due to commands being removed.
*/
public void intervalRemoved ( ListDataEvent e )
{
	// The contents of the command list chagned so check the GUI state...
	ui_UpdateStatus ( true );	// true = also call checkGUIState();
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

	if ( ui_GetIgnoreItemEvent() ) {
		// A programatic change to a list is occurring and we want to
		// ignore the event that will result...
		return;
	}

	Object	o = evt.getItemSelectable();

	// If any of the choices are changed, clear the main list so that the
	// choices and list are in agreement...

	try {
    	// List in the order of the GUI...
    
    	if ( (o == __input_type_JComboBox) && (evt.getStateChange() == ItemEvent.SELECTED) ) {
    		// New input type selected...
    		queryResultsList_Clear();
    		uiAction_InputTypeChoiceClicked();
    	}
    	else if((o == __input_name_JComboBox) && (evt.getStateChange() == ItemEvent.SELECTED) ) {
    		// New input name selected...
    		if ( __selected_input_type.equals( __INPUT_TYPE_NWSRFS_FS5Files) &&
    			!__input_name_JComboBox.getSelected().equals( __PLEASE_SELECT) ) {
    			// If the default "Please Select" is shown, the it is
    			// initialization - don't force a selection...
    			try {
    			    uiAction_SelectInputName_NWSRFS_FS5Files ( false );
    			}
    			catch ( Exception e ) {
    				Message.printWarning ( 1, routine, "Error opening NWSRFS FS5Files connection." );
    				Message.printWarning ( 2, routine, e );
    			}
    		}
            else if(__selected_input_type.equals(__INPUT_TYPE_HECDSS )) {
                uiAction_SelectInputName_HECDSS ( false );
            }
    		else if ( __selected_input_type.equals(__INPUT_TYPE_StateCU ) ){
    			uiAction_SelectInputName_StateCU ( false );
    		}
            else if(__selected_input_type.equals(__INPUT_TYPE_StateCUB )) {
                uiAction_SelectInputName_StateCUB ( false );
            }
    		else if(__selected_input_type.equals(__INPUT_TYPE_StateModB )) {
    			uiAction_SelectInputName_StateModB ( false );
    		}
    	}
    	else if ( o == __data_type_JComboBox && (evt.getStateChange() == ItemEvent.SELECTED) ) {
    		queryResultsList_Clear();
    		uiAction_DataTypeChoiceClicked();
    	}
    	else if ( o == __time_step_JComboBox && (evt.getStateChange() == ItemEvent.SELECTED) ) {
    		queryResultsList_Clear();
    		uiAction_TimeStepChoiceClicked();
    	}
        else if ( o == __View_MapInterface_JCheckBoxMenuItem ) {
    		if ( __View_MapInterface_JCheckBoxMenuItem.isSelected() ) {
    			// User wants the map to be displayed...
    			try {
    			    if ( __geoview_JFrame != null ) {
    					// Just set the map visible...
    					__geoview_JFrame.setVisible ( true );
    				}
    				else {
    				    // No existing GeoView so create one...
    					__geoview_JFrame = new GeoViewJFrame ( this, null );
    					// Add a GeoViewListener so TSTool can handle selects from the map...
    					__geoview_JFrame.getGeoViewJPanel().getGeoView().addGeoViewListener(this);
    					// Add a window listener so TSTool can listen for when the GeoView closes...
    					__geoview_JFrame.addWindowListener ( this );
    					JGUIUtil.center ( __geoview_JFrame );
    				}
    			}
    			catch ( Exception e ) {
    				Message.printWarning ( 1, "TSTool", "Error displaying map interface." );
    				__geoview_JFrame = null;
    			}
    		}
    		else {
                // Map is deselected.  Just set the map frame to not visible...
                if ( __geoview_JFrame != null ) {
                    __geoview_JFrame.setVisible ( false );
                }
    		}
    	}
    	ui_UpdateStatus ( true );
	}
	catch ( Exception e ) {
		// Unexpected exception...
		Message.printWarning ( 2, getClass().getName() + ".itemStateChanged", e );
	}
}

/**
Respond to KeyEvents.  Most single-key events are handled in keyReleased to
prevent multiple events.  Do track when the shift is pressed here.
*/
public void keyPressed ( KeyEvent event )
{	int code = event.getKeyCode();

	if ( code == KeyEvent.VK_SHIFT ) {
		JGUIUtil.setShiftDown ( true );
	}
	else if ( code == KeyEvent.VK_CONTROL ) {
		JGUIUtil.setControlDown ( true );
	}
}

/**
Respond to KeyEvents.
*/
public void keyReleased ( KeyEvent event )
{	int code = event.getKeyCode();

	if ( code == KeyEvent.VK_ENTER ) {
		if ( event.getSource() == ui_GetCommandJList() ) {
			// Same as the Edit...Command event...
			uiAction_EditCommand ();
		}
	}
	else if ( code == KeyEvent.VK_DELETE ) {
		// Clear a command...
		if ( event.getSource() == ui_GetCommandJList() ) {
			commandList_RemoveCommandsBasedOnUI();
		}
	}
	ui_UpdateStatus ( true );
}

public void keyTyped ( KeyEvent event )
{
}

/**
Check the license information to make sure that TSTool has a valid license.
If not print a warning and exit.  The data member __license_manager is created
for use elsewhere (e.g., in Help About).
@param licenseManager the license manager for the session.
*/
private void license_CheckLicense ( LicenseManager licenseManager )
{   String routine = "TSTool.checkLicense";
    try {
        if ( (licenseManager == null) || !licenseManager.isLicenseValid() ) {
            Message.printWarning ( 1, routine, "The license is invalid.  TSTool will exit." );
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            setVisible(false);
            dispose();
            Message.closeLogFile();
            System.exit(0);
        }
    }
    catch ( Exception e ) {
        Message.printWarning ( 1, routine, "Error checking the license.  TSTool will exit." );
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(false);
        dispose();
        Message.closeLogFile();
        System.exit(0);
    }
    // Demo version always warns at startup.
    if ( licenseManager.isLicenseDemo() ) {
        if ( licenseManager.isLicenseExpired() ) {
            Message.printWarning ( 1, routine, "The demonstration license expired on " +
                    licenseManager.getLicenseExpires() + ".  TSTool will exit.  Contact " +
                    __RTiSupportEmail + " to obtain a new license.");
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            setVisible(false);
            dispose();
            Message.closeLogFile();
            System.exit(0);
        }
        else {
            // Not expired yet but a demo so warn the user every time they run TSTool
            Message.printWarning ( 1, routine,
            "This is a demonstration version of TSTool and will expire on " + licenseManager.getLicenseExpires() +
            ".  Contact " + __RTiSupportEmail + " to obtain a full license.");
        }
    }
    else {
        // Not a demonstration license.
        // Warn if within 30 days of the expiration
        int daysToExpiration = licenseManager.getDaysToExpiration();
        if ( daysToExpiration >= 0 && (daysToExpiration < 30) ) {
            Message.printWarning ( 1, routine, "The license expires in " +
                    daysToExpiration + " days.  Contact " + __RTiSupportEmail + " to renew the license.");
        }
        // Do not allow running with expired license
        else if ( licenseManager.isLicenseExpired() ) {
            Message.printWarning ( 1, routine, "The license expired on " +
                    licenseManager.getLicenseExpires() + ".  TSTool will exit.  Contact " +
                    __RTiSupportEmail + " to renew the license.");
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            setVisible(false);
            dispose();
            Message.closeLogFile();
            System.exit(0);
        }
    }
}

/**
Get the license manager that is in effect for the session.
*/
private LicenseManager license_GetLicenseManager ()
{
    return __licenseManager;
}

/**
Read the license file.  If not found, the license manager will be null and
result in an invalid license error.  The license is read before the GUI is
initialized to allow the GUI to be configured according to the license type.
For example, the on-line help will be searched for differently if an RTi
general license or a CDSS license.
*/
private void license_InitializeLicenseFromTSToolProperties ()
{   String license_owner, license_type, license_count, license_expires, license_key;
    String routine = "TSTool_JFrame.readLicense";
    String warning = "";

    Message.printStatus ( 1, "TSTool.checkLicense", "Checking license" );
    license_owner = TSToolMain.getPropValue ( "TSTool.LicenseOwner" );
    if ( license_owner == null ) {
        warning += "\nLicenseOwner is not specified in the TSTool license information.";
    }
    license_type = TSToolMain.getPropValue ( "TSTool.LicenseType" );
    if ( license_type == null ) {
        warning += "\nLicenseType is not specified in the TSTool license information.";
    }
    license_count = TSToolMain.getPropValue ( "TSTool.LicenseCount" );
    if ( license_count == null ) {
        warning += "\nLicenseCount is not specified in the TSTool license information.";
    }
    license_expires = TSToolMain.getPropValue ( "TSTool.LicenseExpires" );
    if ( license_expires == null ) {
        warning += "\nLicenseExpires is not specified in the TSTool license information.";
    }
    license_key = TSToolMain.getPropValue ( "TSTool.LicenseKey" );
    if ( license_key == null ) {
        warning += "\nLicenseKey is not specified in the TSTool license information.";
    }
    if ( warning.length() > 0 ) {
        warning += "\nTSTool will not run.";
        Message.printWarning ( 1, routine, warning );
        license_SetLicenseManager ( null );
        return;
    }

    try {
        // The following allows null fields so check above...
        //Message.printStatus ( 2, routine, "Input to license manager is " + license_owner + " " + license_type +
        //       " " + license_count + " " + license_expires + " " + license_key );
        license_SetLicenseManager ( new LicenseManager ( "TSTool",
            license_owner, license_type, license_count, license_expires, license_key ) );
        //Message.printStatus ( 2, routine, "License manager after initialization:  " + license_GetLicenseManager() );
    }
    catch ( Exception e ) {
        license_SetLicenseManager ( null );
    }
}

/**
Indicate if the software install is for CDSS, in which case certain non-CDSS
features should be turned off.
@param licenseManager the license manager for the session.
*/
private boolean license_IsInstallCDSS( LicenseManager licenseManager )
{
    if ( licenseManager.getLicenseType().equalsIgnoreCase("CDSS") ) {
        return true;
    }
    else {
        return false;
    }
}

/**
Indicate if the software install is for RTi, in which case all features are
typically on (although there may also be checks to see which input types are
enabled - no reason to show HydroBase features if not enabled).
@param licenseManager the license manager for the session.
*/
private boolean license_IsInstallRTi(LicenseManager licenseManager)
{
    // The install type can normally be "Demo", "Site", etc. but the special value
    // "CDSS" is used for unlimited CDSS installs.  Therefore, if it is not a CDSS
    // type, it is an RTi type and therefore an RTi install.
    if ( !license_IsInstallCDSS( licenseManager) ) {
        return true;
    }
    else {
        return false;
    }
}

/**
Set the license manager for the session.
@param licenseManager license manager for the session.
*/
private void license_SetLicenseManager ( LicenseManager licenseManager )
{
    __licenseManager = licenseManager;
}

/**
Handle mouse clicked event.
*/
public void mouseClicked ( MouseEvent event )
{	Object source = event.getSource();
	if ( source == ui_GetCommandJList() ) {
		if ( event.getClickCount() == 2 ) {
			// Edit the first selected item, unless a comment, in which case all are edited...
			uiAction_EditCommand ();
		}
	}
}

/**
Handle mouse entered event.  Nothing is done.
*/
public void mouseEntered ( MouseEvent event )
{
}

/**
Handle mouse exited event.  Nothing is done.
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
    // Popup for commands...
	if ( (c == ui_GetCommandJList()) && (__commands_JListModel.size() > 0) &&
		((mods & MouseEvent.BUTTON3_MASK) != 0) ) {
		Point pt = JGUIUtil.computeOptimalPosition ( event.getPoint(), c, __Commands_JPopupMenu );
		__Commands_JPopupMenu.show ( c, pt.x, pt.y );
	}
    // Popup for time series results list...
	else if ( (c == __results_ts_JList) && (__results_ts_JListModel.size() > 0) &&
		((mods & MouseEvent.BUTTON3_MASK) != 0) ) {
		Point pt = JGUIUtil.computeOptimalPosition (event.getPoint(), c, __results_ts_JPopupMenu );
		__results_ts_JPopupMenu.show ( c, pt.x, pt.y );
	}
    // Popup for ensemble results list...
    else if ( (c == __results_tsensembles_JList) && (__results_tsensembles_JListModel.size() > 0) &&
        ((mods & MouseEvent.BUTTON3_MASK) != 0) ) {
        Point pt = JGUIUtil.computeOptimalPosition (event.getPoint(), c, __results_tsensembles_JPopupMenu );
        __results_tsensembles_JPopupMenu.show ( c, pt.x, pt.y );
    }
	// Popup for input name...
    else if ( (c == __input_name_JComboBox) && ((mods & MouseEvent.BUTTON3_MASK) != 0) &&
        __selected_input_type.equals(__INPUT_TYPE_HECDSS) ) {
        Point pt = JGUIUtil.computeOptimalPosition (event.getPoint(), c, __input_name_JPopupMenu );
        __input_name_JPopupMenu.removeAll();
        __input_name_JPopupMenu.add( new SimpleJMenuItem (__InputName_BrowseHECDSS_String, this ) );
        __input_name_JPopupMenu.show ( c, pt.x, pt.y );
    }
    else if ( (c == __input_name_JComboBox) && ((mods & MouseEvent.BUTTON3_MASK) != 0) &&
        __selected_input_type.equals(__INPUT_TYPE_StateCUB) ) {
        Point pt = JGUIUtil.computeOptimalPosition (event.getPoint(), c, __input_name_JPopupMenu );
        __input_name_JPopupMenu.removeAll();
        __input_name_JPopupMenu.add( new SimpleJMenuItem (__InputName_BrowseStateCUB_String, this ) );
        __input_name_JPopupMenu.show ( c, pt.x, pt.y );
    }
	else if ( (c == __input_name_JComboBox) && ((mods & MouseEvent.BUTTON3_MASK) != 0) &&
		__selected_input_type.equals(__INPUT_TYPE_StateModB) ) {
		Point pt = JGUIUtil.computeOptimalPosition (event.getPoint(), c, __input_name_JPopupMenu );
		__input_name_JPopupMenu.removeAll();
		__input_name_JPopupMenu.add( new SimpleJMenuItem (__InputName_BrowseStateModB_String, this ) );
		__input_name_JPopupMenu.show ( c, pt.x, pt.y );
	}
	// Check the state of the "Copy Selected to Commands" button...
	else if ( c == __query_JWorksheet ) {
		if ( (__query_JWorksheet != null) && (__query_JWorksheet.getSelectedRowCount() > 0) ) {
			JGUIUtil.setEnabled ( __CopySelectedToCommands_JButton, true );
		}
		else {
            JGUIUtil.setEnabled ( __CopySelectedToCommands_JButton, false );
		}
		// Update the border to reflect selections...
		ui_UpdateStatus ( false );
	}
}

/**
Handle mouse released event.  If the object is a results file, display the selected items.
*/
public void mouseReleased ( MouseEvent event )
{
}

/**
Print a status message to the status bar, etc.  This method is called by the
Message class for status messages.
@param level Status message level.
@param rtn Routine name.
@param message Message to print.
*/
/*
public void printStatusMessages ( int level, String rtn, String message )
{	if ( level <= 1 ) {
		__message_JTextField.setText ( message );
		// TODO SAM 2007-11-02 Probably not needed - check on threading issues
		// This does not seem to be redrawing, not matter what is done!
		//__message_JTextField.validate ();
		//__message_JTextField.repaint ();
		//validate ();
		//invalidate ();
		//repaint ();
	}
}*/

/**
Display the time series list query results in a string format.  These results are APPENDED to the list
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
private void queryResultsList_AppendTSIDToCommandList (	String location,
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
	int selected_indices[] = ui_GetCommandJList().getSelectedIndices();
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
	commandList_SetDirty ( true );
	ui_UpdateStatus ( false );
}

/**
Clear the query list (e.g., when a choice changes).
*/
private void queryResultsList_Clear ()
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

	ui_UpdateStatus ( false );
}

/**
Select a station from the query list and create a time series indentifier in the
command list.
@param row Row that is selected in the query list.  This method can be
called once for a single event or multiple times from transferAll().
@param update_status If true, then the GUI state is checked after the
transfer.  This should only be set to true when transferring one command.  If
many are transferred, this slows down the GUI.
*/
private void queryResultsList_TransferOneTSFromQueryResultsListToCommandList ( int row, boolean update_status ) 
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
		// TODO SAM 2004-01-06 - for now don't use the alias to put
		// together the ID - there are issues to resolve in how
		// DateValueTS.readTimeSeries() handles the ID.
		use_alias = false;
		if ( use_alias ) {
			// Use the alias instead of the identifier...
			try {
			queryResultsList_AppendTSIDToCommandList ( alias,
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
			queryResultsList_AppendTSIDToCommandList ( 
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
			queryResultsList_AppendTSIDToCommandList ( 
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
			queryResultsList_AppendTSIDToCommandList(
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
			queryResultsList_AppendTSIDToCommandList ( 
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
			queryResultsList_AppendTSIDToCommandList ( 
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
			queryResultsList_AppendTSIDToCommandList ( 
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
			queryResultsList_AppendTSIDToCommandList ( 
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
			queryResultsList_AppendTSIDToCommandList ( 
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
		//String alias = ((String)__query_TableModel.getValueAt ( row,
			//model.COL_ALIAS )).trim();
		boolean use_alias_in_id = false;
		if ( use_alias_in_id ) {
			// Need to figure out what to do here...
		}
		else {	// Use full ID and all other fields...
			queryResultsList_AppendTSIDToCommandList ( 
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
		queryResultsList_AppendTSIDToCommandList ( 
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
	    __selected_input_type.equals ( __INPUT_TYPE_HECDSS ) ||
		__selected_input_type.equals(__INPUT_TYPE_MEXICO_CSMN) ||
		__selected_input_type.equals(__INPUT_TYPE_MODSIM) ||
		__selected_input_type.equals ( __INPUT_TYPE_NWSCARD ) ||
		__selected_input_type.equals ( __INPUT_TYPE_NWSRFS_FS5Files ) ||
		__selected_input_type.equals ( __INPUT_TYPE_RiverWare ) ||
		__selected_input_type.equals ( __INPUT_TYPE_StateCU ) ||
		__selected_input_type.equals ( __INPUT_TYPE_StateCUB ) ||
		__selected_input_type.equals ( __INPUT_TYPE_StateMod ) ||
		__selected_input_type.equals ( __INPUT_TYPE_StateModB ) ||
		__selected_input_type.equals(__INPUT_TYPE_USGSNWIS) ) {
		// The location (id), type, and time step uniquely identify the time series...
		TSTool_TS_TableModel model = (TSTool_TS_TableModel)__query_TableModel;
		String seqnum = (String)__query_TableModel.getValueAt( row, model.COL_SEQUENCE );
		if ( seqnum.length() == 0 ) {
			seqnum = null;
		}
		queryResultsList_AppendTSIDToCommandList (
		(String)__query_TableModel.getValueAt (	row, model.COL_ID ),
		(String)__query_TableModel.getValueAt( row, model.COL_DATA_SOURCE),
		(String)__query_TableModel.getValueAt( row, model.COL_DATA_TYPE),
		(String)__query_TableModel.getValueAt( row, model.COL_TIME_STEP ),
		(String)__query_TableModel.getValueAt( row, model.COL_SCENARIO ), seqnum, // Optional sequence number
		(String)__query_TableModel.getValueAt( row, model.COL_INPUT_TYPE),
		(String)__query_TableModel.getValueAt( row, model.COL_INPUT_NAME), "", false );
	}
	// Check the GUI...
	if ( update_status ) {
		ui_UpdateStatus ( true );
	}
}

/**
Quits the program with the correct exit status
@param status
 */
/*
private void quitProgram( int status )
{	// Call the application startup to quit...
   tstool.quitProgram(status);
}
*/

/**
Run with a hidden main gui but process the specified command file.
If also in batch mode because commands were specified on the command line, the
a true batch run will be made.
@param command_file Command file to process.
@exception Exception if there is an error processing the file, for example
no data to graph.
*/
/* FIXME SAM 2007-08-20 Need to enable.
private void runNoMainGUI ( String command_file )
throws Exception
{	String routine = "TSTool_JFrame.runNoMainGUI";
	// Run similar to the Run...Commands menu
	TSEngine engine = null;
	// Process as if in batch mode...
	engine = new TSEngine ( __hbdmi, __rdmi, __DIADvisor_dmi,
				__DIADvisor_archive_dmi, __nwsrfs_dmi,
				__smsdmi );
	engine.addTSViewWindowListener ( this );
	engine.processCommands ( __hbdmi, command_file, IOUtil.isBatch() );
	engine = null;
	Message.printStatus ( 1, routine,
	"Successfully processed command file \"" + command_file + "\"" );
}
*/

//TODO SAM 2007-09-02 Need to isolate this code outside the GUI.
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
	for ( int ifile = 0; ifile < __fill_pattern_files.size(); ifile++ ) {
		// TODO - need this to be more generic.  If a statemod
		// pattern file is specified, read it, else, allow for other
		// formats.  For now always read a StateMod file.
		//
		// Read the header but not the actual data since only a list of identifiers is needed.
		fill_pattern_ts = StateMod_TS.readPatternTimeSeriesList (
			IOUtil.getPathUsingWorkingDir((String)__fill_pattern_files.elementAt(ifile)), false );
		if ( fill_pattern_ts != null ) {
			int listsize = fill_pattern_ts.size();
			for ( int j = 0; j < listsize; j++ ) {
				ts = (TS)fill_pattern_ts.elementAt(j);
				if ( ts == null ) {
					continue;
				}
				__fill_pattern_ids.addElement (	ts.getLocation() );
			}
		}
	}
	ts = null;
	fill_pattern_ts = null;
}

/**
Clear the results displays.
*/
private void results_Clear()
{
    results_Ensembles_Clear();
	results_TimeSeries_Clear();
    results_Tables_Clear();
	results_OutputFiles_Clear();
}

/**
Add a time series ensemble description at the end of the time series ensemble results list.
Note that this does not add the actual ensemble, only a description string.
The ensembles are still accessed by the positions in the list.
@param tsensemble_info Time series ensemble information to add at the end of the list.
*/
private void results_Ensembles_AddEnsembleToResults ( final String tsensemble_info )
{   __results_tsensembles_JListModel.addElement ( tsensemble_info );
}

/**
Clear the results ensemble List.  Updates to the label are also done.
*/
private void results_Ensembles_Clear()
{   // Clear the visible list of results...
    __results_tsensembles_JListModel.removeAllElements();
    ui_UpdateStatus ( false );
}

/**
Add the specified output file to the list of output files that can be selected for viewing.
Only files that exist are added.
@param file Output file generated by the processor.
*/
private void results_OutputFiles_AddOutputFile ( File file )
{	String filePathString = null;
    try {
        filePathString = file.getCanonicalPath();
    }
    catch ( IOException e ) {
        // Should not happen
        return;
    }
    if ( !IOUtil.fileExists(filePathString)) {
        // File does not exist so don't show in the list of output files
        return;
    }
    if ( JGUIUtil.indexOf(__results_files_JList, filePathString, false, true) < 0 ) {
            __results_files_JListModel.addElement( filePathString );
    }
}

/**
Clear the list of output files.  This is normally called before the commands are run.
*/
private void results_OutputFiles_Clear()
{
	__results_files_JListModel.removeAllElements();
    ui_UpdateStatus ( false );
}

/**
Add the specified table to the list of tables that can be selected for viewing.
@param table Table generated by the processor.
*/
private void results_Tables_AddTable ( DataTable table )
{   String tableid = table.getTableID();
    if ( JGUIUtil.indexOf(__results_tables_JList, tableid, false, true) < 0 ) {
            __results_tables_JListModel.addElement( tableid );
    }
}

/**
Clear the list of results tables.  This is normally called before the commands are run.
*/
private void results_Tables_Clear()
{
    __results_tables_JListModel.removeAllElements();
}

/**
Add a time series description at the end of the time series results list.
Note that this does not add the actual time series, only a description string.
The time series are still accessed by the positions in the list.
@param ts_info Time series information to add at the end of the list.
*/
private void results_TimeSeries_AddTimeSeriesToResults ( final String ts_info )
{	__results_ts_JListModel.addElement ( ts_info );
}

/**
Clear the final time series List.  Updates to the label are also done.
Also set the engine to null.
*/
private void results_TimeSeries_Clear()
{	// Clear the visible list of results...
	__results_ts_JListModel.removeAllElements();
	ui_UpdateStatus ( false );
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

/**
Enable/disable the ColoradoSMS input type features depending on whether a
ColoradoSMS connection has been made.
*/
private void ui_CheckColoradoSMSFeatures ()
{	if ( (__smsdmi != null) && __smsdmi.isOpen() ) {
		/* TODO SAM 2005-10-18 Currently time series features are not
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
		/* TODO SAM 2005-10-18 Currently time series cannot be
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
successful.  This method is called after a DIADvisor connection has been opened.
For example, it turns on DIADvisor commands.
*/
private void ui_CheckDIADvisorFeatures()
{	if ( (__DIADvisor_dmi != null) && (__DIADvisor_archive_dmi != null) && (__input_type_JComboBox != null) ) {
		// Make sure DIADvisor is in the data source list...
		int count = __input_type_JComboBox.getItemCount();
		boolean rfound = false;
		for ( int i = 0; i < count; i++ ) {
			if ( __input_type_JComboBox.getItem(i).equals(__INPUT_TYPE_DIADvisor) ) {
				rfound = true;
				break;
			}
		}
		if ( !rfound ) {
			// Repopulate the data sources...
			ui_SetInputTypeChoices();
		}
		JGUIUtil.setEnabled(__File_Properties_DIADvisor_JMenuItem,true);
	}
	else {
	    // Remove DIADvisor from the data source list if necessary...
		try {
		    __input_type_JComboBox.remove ( __INPUT_TYPE_DIADvisor);
		}
		catch ( Exception e ) {
			// Ignore - probably already removed...
		}
		JGUIUtil.setEnabled ( __File_Properties_DIADvisor_JMenuItem, false );
	}
}

/**
Check the state of the GUI and enable/disable features as necessary.  For
example, if no time series are selected, disable output options.
This is a more primitive call than ui_UpdateStatus(), which should
generally be used rather than directly calling this method.
*/
private void ui_CheckGUIState ()
{	// Early on the GUI may not be initialized and some of the components
	// seem to still be null...

	if ( !__gui_initialized ) {
		return;
	}

	boolean enabled = true;		// Used to enable/disable main menus based on submenus.

	// Get the needed list sizes...

	int query_list_size = 0;
	if ( __query_TableModel != null ) {
		query_list_size = __query_TableModel.getRowCount();
	}

	int command_list_size = 0;
	int selected_commands_size = 0;
	if ( __commands_JListModel != null ) {
		command_list_size = __commands_JListModel.size();
		selected_commands_size = JGUIUtil.selectedSize ( ui_GetCommandJList() );
	}

	int ts_list_size = 0;
	if ( __results_ts_JListModel != null ) {
		ts_list_size = __results_ts_JListModel.size();
	}

	// List in the order of the GUI.  Popup menu items are checked as needed mixed in below...

	// File menu...

	enabled = false;	// Use for File...Save menu.
	if ( command_list_size > 0 ) {
		// Have commands shown...
		if ( __commands_dirty  ) {
			JGUIUtil.setEnabled ( __File_Save_Commands_JMenuItem,true );
		}
		else {
            JGUIUtil.setEnabled ( __File_Save_Commands_JMenuItem,false );
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
	else {
        JGUIUtil.setEnabled ( __File_Save_TimeSeriesAs_JMenuItem,false);
	}
	JGUIUtil.setEnabled ( __File_Save_JMenu, enabled );

	if ( __smsdmi != null ) {
		JGUIUtil.setEnabled ( __File_Properties_ColoradoSMS_JMenuItem,true );
	}
	else {
        JGUIUtil.setEnabled ( __File_Properties_ColoradoSMS_JMenuItem,false );
	}
	if ( __DIADvisor_dmi != null ) {
		JGUIUtil.setEnabled ( __File_Properties_DIADvisor_JMenuItem,true );
	}
	else {
        JGUIUtil.setEnabled ( __File_Properties_DIADvisor_JMenuItem,false );
	}
	if ( __hbdmi != null ) {
		JGUIUtil.setEnabled ( __File_Properties_HydroBase_JMenuItem,true );
	}
	else {
        JGUIUtil.setEnabled ( __File_Properties_HydroBase_JMenuItem,false );
	}
	if ( __nwsrfs_dmi != null ) {
		JGUIUtil.setEnabled (__File_Properties_NWSRFSFS5Files_JMenuItem,true );
	}
	else {
        JGUIUtil.setEnabled (__File_Properties_NWSRFSFS5Files_JMenuItem,false );
	}
	if ( __rdmi != null ) {
		JGUIUtil.setEnabled ( __File_Properties_RiversideDB_JMenuItem,true );
	}
	else {
        JGUIUtil.setEnabled ( __File_Properties_RiversideDB_JMenuItem,false );
	}

	// Edit menu...

	if ( command_list_size > 0 ) {
		JGUIUtil.setEnabled ( __Edit_SelectAllCommands_JMenuItem, true);
		JGUIUtil.setEnabled ( __CommandsPopup_SelectAll_JMenuItem,true);
		JGUIUtil.setEnabled ( __Edit_DeselectAllCommands_JMenuItem,true);
		JGUIUtil.setEnabled ( __CommandsPopup_DeselectAll_JMenuItem,true);
	}
	else {	// No commands are shown.
		JGUIUtil.setEnabled ( __Edit_SelectAllCommands_JMenuItem,false);
		JGUIUtil.setEnabled ( __CommandsPopup_SelectAll_JMenuItem,false);
		JGUIUtil.setEnabled ( __Edit_DeselectAllCommands_JMenuItem,false);
		JGUIUtil.setEnabled ( __CommandsPopup_DeselectAll_JMenuItem,false);
	}
	if ( selected_commands_size > 0 ) {
		JGUIUtil.setEnabled ( __Edit_CutCommands_JMenuItem,true);
		JGUIUtil.setEnabled ( __CommandsPopup_Cut_JMenuItem,true);
		JGUIUtil.setEnabled ( __Edit_CopyCommands_JMenuItem,true);
		JGUIUtil.setEnabled ( __CommandsPopup_Copy_JMenuItem,true);
		JGUIUtil.setEnabled ( __Edit_DeleteCommands_JMenuItem,true);
		JGUIUtil.setEnabled ( __CommandsPopup_Delete_JMenuItem,true);
		JGUIUtil.setEnabled ( __Edit_CommandWithErrorChecking_JMenuItem, true );
		JGUIUtil.setEnabled ( __CommandsPopup_Edit_CommandWithErrorChecking_JMenuItem,true );
		JGUIUtil.setEnabled ( __Edit_ConvertSelectedCommandsToComments_JMenuItem,true);
		JGUIUtil.setEnabled ( __CommandsPopup_ConvertSelectedCommandsToComments_JMenuItem,true);
		JGUIUtil.setEnabled ( __Edit_ConvertSelectedCommandsFromComments_JMenuItem,true);
		JGUIUtil.setEnabled ( __CommandsPopup_ConvertSelectedCommandsFromComments_JMenuItem,true);
	}
	else {
		JGUIUtil.setEnabled ( __Edit_CutCommands_JMenuItem, false );
		JGUIUtil.setEnabled ( __CommandsPopup_Cut_JMenuItem, false );
		JGUIUtil.setEnabled ( __Edit_CopyCommands_JMenuItem, false );
		JGUIUtil.setEnabled ( __CommandsPopup_Copy_JMenuItem, false );
		JGUIUtil.setEnabled ( __Edit_DeleteCommands_JMenuItem, false );
		JGUIUtil.setEnabled ( __CommandsPopup_Delete_JMenuItem, false );
		JGUIUtil.setEnabled ( __Edit_CommandWithErrorChecking_JMenuItem, false );
		JGUIUtil.setEnabled ( __CommandsPopup_Edit_CommandWithErrorChecking_JMenuItem,false );
		JGUIUtil.setEnabled ( __Edit_ConvertSelectedCommandsToComments_JMenuItem,false);
		JGUIUtil.setEnabled ( __CommandsPopup_ConvertSelectedCommandsToComments_JMenuItem,false);
		JGUIUtil.setEnabled ( __Edit_ConvertSelectedCommandsFromComments_JMenuItem,false);
		JGUIUtil.setEnabled ( __CommandsPopup_ConvertSelectedCommandsFromComments_JMenuItem,false);
	}
	if ( __commands_cut_buffer.size() > 0 ) {
		// Paste button should be enabled...
		JGUIUtil.setEnabled ( __Edit_PasteCommands_JMenuItem, true );
		JGUIUtil.setEnabled ( __CommandsPopup_Paste_JMenuItem, true );
	}
	else {
		JGUIUtil.setEnabled ( __Edit_PasteCommands_JMenuItem, false );
		JGUIUtil.setEnabled ( __CommandsPopup_Paste_JMenuItem, false );
	}

	// View menu...

	// Commands menu...

	if ( command_list_size > 0 ) {
        JGUIUtil.setEnabled ( __Commands_Create_ResequenceTimeSeriesData_JMenuItem, true);
		JGUIUtil.setEnabled ( __Commands_Create_TS_ChangeInterval_JMenuItem, true);
		JGUIUtil.setEnabled ( __Commands_Create_TS_Copy_JMenuItem, true);
		JGUIUtil.setEnabled ( __Commands_Create_TS_Disaggregate_JMenuItem, true);
		JGUIUtil.setEnabled ( __Commands_Create_TS_NewDayTSFromMonthAndDayTS_JMenuItem,	true );
		JGUIUtil.setEnabled ( __Commands_Create_TS_NewEndOfMonthTSFromDayTS_JMenuItem,true);
		JGUIUtil.setEnabled ( __Commands_Create_TS_NewStatisticTimeSeries_JMenuItem,true);
		JGUIUtil.setEnabled ( __Commands_Create_TS_NewStatisticYearTS_JMenuItem,true);
		JGUIUtil.setEnabled ( __Commands_Create_TS_Normalize_JMenuItem, true);
		JGUIUtil.setEnabled ( __Commands_Create_TS_RelativeDiff_JMenuItem, true);

		JGUIUtil.setEnabled ( __Commands_Fill_FillConstant_JMenuItem, true);
		JGUIUtil.setEnabled ( __Commands_Fill_FillDayTSFrom2MonthTSAnd1DayTS_JMenuItem, true);
		JGUIUtil.setEnabled ( __Commands_Fill_FillFromTS_JMenuItem, true);
		JGUIUtil.setEnabled ( __Commands_Fill_FillHistMonthAverage_JMenuItem, true);
		JGUIUtil.setEnabled ( __Commands_Fill_FillHistYearAverage_JMenuItem, true);
		JGUIUtil.setEnabled ( __Commands_Fill_FillInterpolate_JMenuItem, true);
		// TODO SAM 2005-04-26 This fill method is not enabled - may not be needed.
		//JGUIUtil.setEnabled(__Commands_Fill_fillMOVE1_JMenuItem,true);
		JGUIUtil.setEnabled ( __Commands_Fill_FillMOVE2_JMenuItem, true);
		JGUIUtil.setEnabled ( __Commands_Fill_FillMixedStation_JMenuItem,true );
		JGUIUtil.setEnabled ( __Commands_Fill_FillPattern_JMenuItem,true);
		JGUIUtil.setEnabled ( __Commands_Fill_FillProrate_JMenuItem,true);
		JGUIUtil.setEnabled ( __Commands_Fill_FillRegression_JMenuItem,true);
		JGUIUtil.setEnabled ( __Commands_Fill_FillRepeat_JMenuItem,true);
		JGUIUtil.setEnabled ( __Commands_Fill_FillUsingDiversionComments_JMenuItem,	true);
		JGUIUtil.setEnabled ( __Commands_FillTimeSeries_JMenu, true );

		JGUIUtil.setEnabled ( __Commands_Set_ReplaceValue_JMenuItem,true);
		JGUIUtil.setEnabled ( __Commands_Set_SetConstant_JMenuItem, true);
		JGUIUtil.setEnabled ( __Commands_Set_SetDataValue_JMenuItem,true);
		JGUIUtil.setEnabled ( __Commands_Set_SetFromTS_JMenuItem, true);
		JGUIUtil.setEnabled ( __Commands_Set_SetMax_JMenuItem, true);
		JGUIUtil.setEnabled ( __Commands_Set_SetToMin_JMenuItem, true);
        JGUIUtil.setEnabled ( __Commands_Set_SetTimeSeriesProperty_JMenuItem, true );
		JGUIUtil.setEnabled ( __Commands_SetTimeSeries_JMenu, true );

		JGUIUtil.setEnabled ( __Commands_Manipulate_Add_JMenuItem, true);
		JGUIUtil.setEnabled ( __Commands_Manipulate_AddConstant_JMenuItem, true);
		JGUIUtil.setEnabled ( __Commands_Manipulate_AdjustExtremes_JMenuItem, true);
		JGUIUtil.setEnabled ( __Commands_Manipulate_ARMA_JMenuItem, true);
		JGUIUtil.setEnabled ( __Commands_Manipulate_Blend_JMenuItem,true);
        JGUIUtil.setEnabled ( __Commands_Manipulate_ChangePeriod_JMenuItem,true);
		JGUIUtil.setEnabled ( __Commands_Manipulate_ConvertDataUnits_JMenuItem,true);
		JGUIUtil.setEnabled ( __Commands_Manipulate_Cumulate_JMenuItem,true);
		JGUIUtil.setEnabled ( __Commands_Manipulate_Divide_JMenuItem,true);
		JGUIUtil.setEnabled ( __Commands_Manipulate_Free_JMenuItem,true);
		JGUIUtil.setEnabled ( __Commands_Manipulate_Multiply_JMenuItem,true);
		JGUIUtil.setEnabled ( __Commands_Manipulate_RunningAverage_JMenuItem, true);
		JGUIUtil.setEnabled ( __Commands_Manipulate_Scale_JMenuItem,true);
		JGUIUtil.setEnabled ( __Commands_Manipulate_ShiftTimeByInterval_JMenuItem,true);
		JGUIUtil.setEnabled ( __Commands_Manipulate_Subtract_JMenuItem, true);
		JGUIUtil.setEnabled ( __Commands_ManipulateTimeSeries_JMenu,true);

		JGUIUtil.setEnabled ( __Commands_Analyze_AnalyzePattern_JMenuItem, true);
		JGUIUtil.setEnabled ( __Commands_Analyze_CompareTimeSeries_JMenuItem, true);
		JGUIUtil.setEnabled ( __Commands_Analyze_ComputeErrorTimeSeries_JMenuItem, true);
		JGUIUtil.setEnabled ( __Commands_AnalyzeTimeSeries_JMenu, true);

		JGUIUtil.setEnabled ( __Commands_Models_JMenu, true);
        
		JGUIUtil.setEnabled ( __Commands_Output_SortTimeSeries_JMenuItem, true);
		JGUIUtil.setEnabled ( __Commands_Output_WriteDateValue_JMenuItem, true);
		JGUIUtil.setEnabled ( __Commands_Output_WriteNwsCard_JMenuItem,true);
		JGUIUtil.setEnabled ( __Commands_Output_WriteRiverWare_JMenuItem, true);
        JGUIUtil.setEnabled ( __Commands_Output_WriteSHEF_JMenuItem, true);
		JGUIUtil.setEnabled ( __Commands_Output_WriteStateCU_JMenuItem, true);
		JGUIUtil.setEnabled ( __Commands_Output_WriteStateMod_JMenuItem, true);
		JGUIUtil.setEnabled ( __Commands_Output_WriteSummary_JMenuItem, true);
		JGUIUtil.setEnabled ( __Commands_Output_DeselectTimeSeries_JMenuItem, true);
		JGUIUtil.setEnabled ( __Commands_Output_SelectTimeSeries_JMenuItem, true);
        
        JGUIUtil.setEnabled ( __Commands_Ensemble_CreateEnsemble_JMenuItem, true);
        JGUIUtil.setEnabled ( __Commands_Ensemble_CopyEnsemble_JMenuItem, true);
        JGUIUtil.setEnabled ( __Commands_Ensemble_TS_NewStatisticTimeSeriesFromEnsemble_JMenuItem,true);
        JGUIUtil.setEnabled ( __Commands_Ensemble_TS_WeightTraces_JMenuItem, true);
        JGUIUtil.setEnabled ( __Commands_Ensemble_WriteNWSRFSESPTraceEnsemble_JMenuItem,true);

		/* TODO - it is irritating to not be able to run commands
		  when external input changes (or during debugging)...
		if (	__commands_dirty ||
			(!__commands_dirty && (ts_list_size == 0)) ) {
			JGUIUtil.setEnabled ( __run_commands_JButton, true );
		}
		else {
            JGUIUtil.setEnabled ( __run_commands_JButton, false );
		}
		*/
		if ( selected_commands_size > 0 ) {
			JGUIUtil.setEnabled ( __Run_SelectedCommands_JButton, true );
		}
		else {
            JGUIUtil.setEnabled ( __Run_SelectedCommands_JButton,false );
		}
		JGUIUtil.setEnabled ( __Run_AllCommands_JButton, true );
		JGUIUtil.setEnabled ( __ClearCommands_JButton, true );
	}
	else {
        // No commands are shown.
        JGUIUtil.setEnabled ( __Commands_Create_ResequenceTimeSeriesData_JMenuItem, false);
		JGUIUtil.setEnabled ( __Commands_Create_TS_ChangeInterval_JMenuItem,false);
		JGUIUtil.setEnabled ( __Commands_Create_TS_Copy_JMenuItem, false);
		JGUIUtil.setEnabled ( __Commands_Create_TS_Disaggregate_JMenuItem, false);
		JGUIUtil.setEnabled ( __Commands_Create_TS_NewDayTSFromMonthAndDayTS_JMenuItem,false);
		JGUIUtil.setEnabled ( __Commands_Create_TS_NewEndOfMonthTSFromDayTS_JMenuItem,false);
		JGUIUtil.setEnabled ( __Commands_Create_TS_NewStatisticTimeSeries_JMenuItem,false );
		JGUIUtil.setEnabled ( __Commands_Create_TS_NewStatisticYearTS_JMenuItem,false );
		JGUIUtil.setEnabled ( __Commands_Create_TS_Normalize_JMenuItem, false);
		JGUIUtil.setEnabled ( __Commands_Create_TS_RelativeDiff_JMenuItem, false);

		JGUIUtil.setEnabled ( __Commands_Fill_FillConstant_JMenuItem, false);
		JGUIUtil.setEnabled ( __Commands_Fill_FillDayTSFrom2MonthTSAnd1DayTS_JMenuItem,	false);
		JGUIUtil.setEnabled ( __Commands_Fill_FillFromTS_JMenuItem,false);
		JGUIUtil.setEnabled ( __Commands_Fill_FillHistMonthAverage_JMenuItem,false);
		JGUIUtil.setEnabled ( __Commands_Fill_FillHistYearAverage_JMenuItem, false);
		JGUIUtil.setEnabled ( __Commands_Fill_FillInterpolate_JMenuItem, false);
		JGUIUtil.setEnabled ( __Commands_Fill_FillMixedStation_JMenuItem,false);
		JGUIUtil.setEnabled ( __Commands_Fill_FillMOVE1_JMenuItem, false);
		JGUIUtil.setEnabled ( __Commands_Fill_FillMOVE2_JMenuItem, false);
		JGUIUtil.setEnabled ( __Commands_Fill_FillPattern_JMenuItem, false);
		JGUIUtil.setEnabled ( __Commands_Fill_FillProrate_JMenuItem, false);
		JGUIUtil.setEnabled ( __Commands_Fill_FillRegression_JMenuItem, false);
		JGUIUtil.setEnabled ( __Commands_Fill_FillRepeat_JMenuItem, false);
		JGUIUtil.setEnabled ( __Commands_Fill_FillUsingDiversionComments_JMenuItem,	false);
		JGUIUtil.setEnabled ( __Commands_FillTimeSeries_JMenu, false );

		JGUIUtil.setEnabled ( __Commands_Set_ReplaceValue_JMenuItem, false);
		JGUIUtil.setEnabled ( __Commands_Set_SetDataValue_JMenuItem, false);
		JGUIUtil.setEnabled ( __Commands_Set_SetFromTS_JMenuItem, false);
		JGUIUtil.setEnabled ( __Commands_Set_SetMax_JMenuItem, false);
		JGUIUtil.setEnabled ( __Commands_Set_SetToMin_JMenuItem, false);
        JGUIUtil.setEnabled ( __Commands_Set_SetTimeSeriesProperty_JMenuItem, false );
		JGUIUtil.setEnabled ( __Commands_SetTimeSeries_JMenu, false );

		JGUIUtil.setEnabled ( __Commands_Manipulate_Add_JMenuItem,false);
		JGUIUtil.setEnabled ( __Commands_Manipulate_AddConstant_JMenuItem, false);
		JGUIUtil.setEnabled ( __Commands_Manipulate_AdjustExtremes_JMenuItem,false);
		JGUIUtil.setEnabled ( __Commands_Manipulate_ARMA_JMenuItem,false);
		JGUIUtil.setEnabled ( __Commands_Manipulate_Blend_JMenuItem, false);
        JGUIUtil.setEnabled ( __Commands_Manipulate_ChangePeriod_JMenuItem,false);
		JGUIUtil.setEnabled ( __Commands_Manipulate_ConvertDataUnits_JMenuItem,false);
		JGUIUtil.setEnabled ( __Commands_Manipulate_Cumulate_JMenuItem,false);
		JGUIUtil.setEnabled ( __Commands_Manipulate_Divide_JMenuItem,false);
		JGUIUtil.setEnabled ( __Commands_Manipulate_Free_JMenuItem,false);
		JGUIUtil.setEnabled ( __Commands_Manipulate_Multiply_JMenuItem,false);
		JGUIUtil.setEnabled ( __Commands_Manipulate_RunningAverage_JMenuItem, false);
		JGUIUtil.setEnabled ( __Commands_Manipulate_Scale_JMenuItem,false);
		JGUIUtil.setEnabled ( __Commands_Manipulate_ShiftTimeByInterval_JMenuItem,false);
		JGUIUtil.setEnabled ( __Commands_Manipulate_Subtract_JMenuItem, false);
		JGUIUtil.setEnabled ( __Commands_ManipulateTimeSeries_JMenu,false);

		JGUIUtil.setEnabled ( __Commands_Analyze_AnalyzePattern_JMenuItem, false);
		JGUIUtil.setEnabled ( __Commands_Analyze_CompareTimeSeries_JMenuItem, false);
		JGUIUtil.setEnabled ( __Commands_Analyze_ComputeErrorTimeSeries_JMenuItem, false);
		JGUIUtil.setEnabled ( __Commands_AnalyzeTimeSeries_JMenu, false);

		// TODO SAM 2005-07-11 For now enable because models can
		// create time series...
		//JGUIUtil.setEnabled ( __Commands_Models_JMenu, false );
        
		JGUIUtil.setEnabled ( __Commands_Output_SortTimeSeries_JMenuItem, false);
		JGUIUtil.setEnabled ( __Commands_Output_WriteDateValue_JMenuItem, false);
		JGUIUtil.setEnabled ( __Commands_Output_WriteNwsCard_JMenuItem,false);
		JGUIUtil.setEnabled ( __Commands_Output_WriteRiverWare_JMenuItem, false );
        JGUIUtil.setEnabled ( __Commands_Output_WriteSHEF_JMenuItem,false);
		JGUIUtil.setEnabled ( __Commands_Output_WriteStateCU_JMenuItem,false);
		JGUIUtil.setEnabled ( __Commands_Output_WriteStateMod_JMenuItem,false);
		JGUIUtil.setEnabled ( __Commands_Output_WriteSummary_JMenuItem,false);
		JGUIUtil.setEnabled ( __Commands_Output_DeselectTimeSeries_JMenuItem,false);
		JGUIUtil.setEnabled ( __Commands_Output_SelectTimeSeries_JMenuItem,false);
        
        JGUIUtil.setEnabled ( __Commands_Ensemble_CreateEnsemble_JMenuItem, false);
        JGUIUtil.setEnabled ( __Commands_Ensemble_CopyEnsemble_JMenuItem, false);
        JGUIUtil.setEnabled ( __Commands_Ensemble_TS_NewStatisticTimeSeriesFromEnsemble_JMenuItem,false );
        JGUIUtil.setEnabled ( __Commands_Ensemble_TS_WeightTraces_JMenuItem, false);
        JGUIUtil.setEnabled ( __Commands_Ensemble_WriteNWSRFSESPTraceEnsemble_JMenuItem,false);

		JGUIUtil.setEnabled ( __Run_SelectedCommands_JButton, false );
		JGUIUtil.setEnabled ( __Run_AllCommands_JButton, false );
		JGUIUtil.setEnabled ( __ClearCommands_JButton, false );
	}
	if ( selected_commands_size > 0 ) {
		JGUIUtil.setEnabled ( __Commands_ConvertTSIDTo_ReadTimeSeries_JMenuItem,true);
		JGUIUtil.setEnabled ( __Commands_ConvertTSIDToReadCommand_JMenu,true);
	}
	else {
		JGUIUtil.setEnabled ( __Commands_ConvertTSIDTo_ReadDateValue_JMenuItem,false);
		JGUIUtil.setEnabled ( __Commands_ConvertTSIDToReadCommand_JMenu,false);
	}

	// Run menu...
	
	ui_CheckGUIState_RunMenu ( command_list_size, selected_commands_size );

	// Results menu...

	if ( ts_list_size > 0 ) {
		JGUIUtil.setEnabled ( __Results_JMenu, true );
	}
	else {
        // DISABLE file and view options...
		JGUIUtil.setEnabled ( __Results_JMenu, false );
	}

	// Tools menu...

	if ( ts_list_size > 0 ) {
		JGUIUtil.setEnabled ( __Tools_Analysis_JMenu, true );
		JGUIUtil.setEnabled ( __Tools_Report_JMenu, true );
	}
	else {
        JGUIUtil.setEnabled ( __Tools_Analysis_JMenu, false );
		JGUIUtil.setEnabled ( __Tools_Report_JMenu, false );
	}
	if ( (query_list_size > 0) && (__geoview_JFrame != null) ) {
		JGUIUtil.setEnabled ( __Tools_SelectOnMap_JMenuItem, true );
	}
	else {
        JGUIUtil.setEnabled ( __Tools_SelectOnMap_JMenuItem, false );
	}

	// Enable/disable features related to the query list...

	if ( query_list_size > 0 ) {
		JGUIUtil.setEnabled ( __CopyAllToCommands_JButton, true );
	}
	else {
        JGUIUtil.setEnabled ( __CopyAllToCommands_JButton, false );
	}
	if (	(query_list_size > 0) && (__query_JWorksheet != null) &&
		(__query_JWorksheet.getSelectedRowCount() > 0) ) {
		JGUIUtil.setEnabled ( __CopySelectedToCommands_JButton, true );
	}
	else {
        JGUIUtil.setEnabled ( __CopySelectedToCommands_JButton, false );
	}

	// Disable all of the following menus until the dialogs can be enabled.
	// If not enabled below, the command may be phased out or merged with another command...

	// TODO - all of these are new features.  The intent is to convert
	// a TSID to a specific read command, as available in other menus,
	// simplifying the conversion from time series browsing, to command language.
	JGUIUtil.setEnabled ( __Commands_ConvertTSIDTo_ReadDateValue_JMenuItem,false);
    JGUIUtil.setEnabled ( __Commands_ConvertTSIDTo_ReadDelimitedFile_JMenuItem,false);
	JGUIUtil.setEnabled ( __Commands_ConvertTSIDTo_ReadHydroBase_JMenuItem,false);
	JGUIUtil.setEnabled ( __Commands_ConvertTSIDTo_ReadMODSIM_JMenuItem,false);
	JGUIUtil.setEnabled ( __Commands_ConvertTSIDTo_ReadNwsCard_JMenuItem,false);
	JGUIUtil.setEnabled ( __Commands_ConvertTSIDTo_ReadNWSRFSFS5Files_JMenuItem,false);
	JGUIUtil.setEnabled ( __Commands_ConvertTSIDTo_ReadRiverWare_JMenuItem,false);
	JGUIUtil.setEnabled ( __Commands_ConvertTSIDTo_ReadStateMod_JMenuItem,false);
	JGUIUtil.setEnabled ( __Commands_ConvertTSIDTo_ReadStateModB_JMenuItem,false);
	JGUIUtil.setEnabled ( __Commands_ConvertTSIDTo_ReadUsgsNwis_JMenuItem,false);

	// TODO SAM 2005-09-02 Proposed new commands
	//JGUIUtil.setEnabled(__Commands_Read_TS_ReadStateModB_JMenuItem,false);
	//JGUIUtil.setEnabled(__Commands_Read_TS_ReadStateMod_JMenuItem,false);

	// TODO Not available as a command yet - logic not coded...
	JGUIUtil.setEnabled(__Commands_Fill_FillMOVE1_JMenuItem,false);

	// TODO - can this be phased out?
	JGUIUtil.setEnabled(__Commands_Output_SetOutputDetailedHeaders_JMenuItem,false);
}

/**
Enable/disable Run menu items based on the state of the GUI.
*/
private void ui_CheckGUIState_RunMenu ( int command_list_size, int selected_commands_size )
{
	// Running, so allow cancel but not another run...
	boolean enable_run = false;
	if ( (__ts_processor != null) && __ts_processor.getIsRunning() ) {
		// Running, so allow cancel but not another run...
		enable_run = false;  // Handled below in second if
		JGUIUtil.setEnabled (__Run_CancelCommandProcessing_JMenuItem, true);
		JGUIUtil.setEnabled (__CommandsPopup_CancelCommandProcessing_JMenuItem,true);

	}
	else {
		// Not running, so disable cancel, but do allow run if
		// there are commands (see below)...
		enable_run = true;
		JGUIUtil.setEnabled (__Run_CancelCommandProcessing_JMenuItem, false);
		JGUIUtil.setEnabled (__CommandsPopup_CancelCommandProcessing_JMenuItem,false );
	}
	if ( command_list_size > 0 ) {
		/* TODO SAM 2007-11-01 Evaluate need
		if (	__commands_dirty ||
			(!__commands_dirty && (ts_list_size == 0)) ) {
			JGUIUtil.setEnabled (
				__run_commands_JButton, true );
		}
		else {	JGUIUtil.setEnabled ( __run_commands_JButton, false );
		}
		*/
		// Run all commands menus always enabled if not already running...
		JGUIUtil.setEnabled(__Run_AllCommandsCreateOutput_JMenuItem, enable_run);
		JGUIUtil.setEnabled(__CommandsPopup_Run_AllCommandsCreateOutput_JMenuItem,enable_run);
		JGUIUtil.setEnabled(__Run_AllCommandsIgnoreOutput_JMenuItem, enable_run);
		JGUIUtil.setEnabled(__CommandsPopup_Run_AllCommandsIgnoreOutput_JMenuItem,enable_run);
		JGUIUtil.setEnabled ( __Run_AllCommands_JButton, enable_run );
		if ( selected_commands_size > 0 ) {
			// Run selected commands menus...
			JGUIUtil.setEnabled(__Run_SelectedCommandsCreateOutput_JMenuItem, enable_run);
			JGUIUtil.setEnabled(__CommandsPopup_Run_SelectedCommandsCreateOutput_JMenuItem,enable_run);
			JGUIUtil.setEnabled(__Run_SelectedCommandsIgnoreOutput_JMenuItem, enable_run);
			JGUIUtil.setEnabled(__CommandsPopup_Run_SelectedCommandsIgnoreOutput_JMenuItem,enable_run);
			// Run buttons...
			JGUIUtil.setEnabled (__Run_SelectedCommands_JButton, enable_run );
		}
		else {
			// Run selected commands menus are false (because none selected)...
			JGUIUtil.setEnabled(__Run_SelectedCommandsCreateOutput_JMenuItem, false);
			JGUIUtil.setEnabled(__CommandsPopup_Run_SelectedCommandsCreateOutput_JMenuItem,false);
			JGUIUtil.setEnabled(__Run_SelectedCommandsIgnoreOutput_JMenuItem, false);
			JGUIUtil.setEnabled(__CommandsPopup_Run_SelectedCommandsIgnoreOutput_JMenuItem,false);
			JGUIUtil.setEnabled (__Run_SelectedCommands_JButton, false );
		}
		// Have commands so can clear...
		JGUIUtil.setEnabled ( __ClearCommands_JButton, true );
	}
	else {	// No commands in list...
		JGUIUtil.setEnabled (__Run_AllCommandsCreateOutput_JMenuItem,false);
		JGUIUtil.setEnabled (__CommandsPopup_Run_AllCommandsCreateOutput_JMenuItem,false);
		JGUIUtil.setEnabled (__Run_AllCommandsIgnoreOutput_JMenuItem,false);
		JGUIUtil.setEnabled (__CommandsPopup_Run_AllCommandsIgnoreOutput_JMenuItem,false);
		JGUIUtil.setEnabled (__Run_SelectedCommandsCreateOutput_JMenuItem,false);
		JGUIUtil.setEnabled (__CommandsPopup_Run_SelectedCommandsCreateOutput_JMenuItem,false);
		JGUIUtil.setEnabled (__Run_SelectedCommandsIgnoreOutput_JMenuItem,false);
		JGUIUtil.setEnabled (__CommandsPopup_Run_SelectedCommandsIgnoreOutput_JMenuItem,false);
		JGUIUtil.setEnabled ( __Run_SelectedCommands_JButton, false );
		JGUIUtil.setEnabled ( __Run_AllCommands_JButton, false );
		JGUIUtil.setEnabled ( __ClearCommands_JButton, false );
	}
}

/**
Enable/disable the HydroBase input type features depending on whether a
HydroBaseDMI connection has been made.
*/
private void ui_CheckHydroBaseFeatures ()
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
				ui_SetInputTypeChoices();
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
@param licenseManager license manager for the session.
*/
private void ui_CheckInputTypesForLicense ( LicenseManager licenseManager )
{	if ( licenseManager == null ) {
		return;
	}
	String routine = "TSTool_JFrame.checkInputTypesForLicense";
	if ( licenseManager.getLicenseType().equalsIgnoreCase("CDSS") ) {
		if ( !__source_StateCU_enabled ) {
			// Might not be in older config files...
			Message.printStatus ( 1, routine, "StateCU input type being enabled for CDSS." );
			__source_StateCU_enabled = true;
		}
		if ( !__source_StateMod_enabled ) {
			// Might not be in older config files...
			Message.printStatus ( 1, routine, "StateMod input type being enabled for CDSS." );
			__source_StateMod_enabled = true;
		}
		Message.printStatus ( 2, routine, "DIADvisor input type being disabled for CDSS." );
		__source_DIADvisor_enabled = false;
	    Message.printStatus ( 2, routine, "HEC-DSS input type being disabled for CDSS." );
	    __source_HECDSS_enabled = false;
		Message.printStatus ( 2, routine, "Mexico CSMN input type being disabled for CDSS." );
		__source_MexicoCSMN_enabled = false;
		Message.printStatus ( 2, routine, "MODSIM input type being disabled for CDSS." );
		__source_MODSIM_enabled = false;
		Message.printStatus ( 2, routine, "NDFD input type being disabled for CDSS." );
		__source_NDFD_enabled = false;
		Message.printStatus ( 2, routine, "NWSCard input type being disabled for CDSS." );
		__source_NWSCard_enabled = false;
		Message.printStatus ( 2, routine, "NWSRFS FS5Files input type being disabled for CDSS." );
		__source_NWSRFS_FS5Files_enabled = false;
		Message.printStatus ( 2, routine, "NWSRFS ESPTraceEnsemble input type being disabled for CDSS." );
		__source_NWSRFS_ESPTraceEnsemble_enabled = false;
		Message.printStatus ( 2, routine, "RiversideDB input type being disabled for CDSS." );
		__source_RiversideDB_enabled = false;
		Message.printStatus ( 2, routine, "SHEF input type being disabled for CDSS." );
		__source_SHEF_enabled = false;
	}
}

/**
Check the GUI for NWSRFS FS5Files features.
*/
private void ui_CheckNWSRFSFS5FilesFeatures ()
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
			ui_SetInputTypeChoices();
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
private void ui_CheckRiversideDBFeatures ()
{	if ( (__rdmi != null) && (__input_type_JComboBox != null) ) {
		// Make sure RiversideDB is in the data source list...
		int count = __input_type_JComboBox.getItemCount();
		boolean rfound = false;
		for ( int i = 0; i < count; i++ ) {
			if ( __input_type_JComboBox.getItem(i).equals(__INPUT_TYPE_RiversideDB) ) {
				rfound = true;
				break;
			}
		}
		if ( !rfound ) {
			// Repopulate the data sources...
			ui_SetInputTypeChoices();
		}
		if (__File_Properties_RiversideDB_JMenuItem != null ) {
			__File_Properties_RiversideDB_JMenuItem.setEnabled(true);
		}
	}
	else {
        // RiversideDB connection failed so remove RiversideDB from the data source list if necessary...
		try {
            __input_type_JComboBox.remove(__INPUT_TYPE_RiversideDB);
		}
		catch ( Exception e ) {
			// Ignore - probably already removed...
		}
		if ( __File_Properties_RiversideDB_JMenuItem != null ) {
			__File_Properties_RiversideDB_JMenuItem.setEnabled ( false );
		}
	}
}

/**
Return the command list component.  Do it this way because the command component
may evolve.
*/
private JList ui_GetCommandJList()
{
    return __commands_AnnotatedCommandJList.getJList();
}

/**
Return the directory for the last "File...Open Command File".
*/
private String ui_GetDir_LastCommandFileOpened()
{   String routine = "TSTool_JFrame.ui_GetDir_LastCommandFileOpened";
	if ( __Dir_LastCommandFileOpened != null ) {
	    Message.printStatus ( 2, routine,
	            "Returning last (non null) command file directory: " + __Dir_LastCommandFileOpened );
	    return __Dir_LastCommandFileOpened;
	}
	    
	// Try to get the generic dialog selection location...
	__Dir_LastCommandFileOpened = JGUIUtil.getLastFileDialogDirectory();
    if ( __Dir_LastCommandFileOpened != null ) {
        Message.printStatus ( 2, routine,
                "Returning last command file directory from last dialog selection: " + __Dir_LastCommandFileOpened );
        return __Dir_LastCommandFileOpened;
    }
	// This will check user.dir
	__Dir_LastCommandFileOpened = IOUtil.getProgramWorkingDir ();
	Message.printStatus ( 2, routine,
            "Returning last command file directory from working directory: " + __Dir_LastCommandFileOpened );
	return __Dir_LastCommandFileOpened;
}

/**
Return the last directory for "Run Command File", which runs an external command file
but does not show the results.
*/
private String ui_GetDir_LastExternalCommandFileRun()
{
    if ( __Dir_LastExternalCommandFileRun != null ) {
        return __Dir_LastExternalCommandFileRun;
    }
    // Try to get the generic dialog selection location...
	__Dir_LastExternalCommandFileRun = JGUIUtil.getLastFileDialogDirectory();
	if ( __Dir_LastExternalCommandFileRun == null ) {
	    // This will check user.dir
        __Dir_LastExternalCommandFileRun = IOUtil.getProgramWorkingDir ();
    }
	return __Dir_LastExternalCommandFileRun;
}

/**
Return the HydroBaseDMI instance that is active for the UI, opened from
the configuration file inforation or the HydroBase select dialog.
@return the HydroBaseDMI instance that is active for the UI.
*/
private HydroBaseDMI ui_GetHydroBaseDMI ()
{
	return __hbdmi;
}

/**
Return whether ActionEvents should be ignored.
*/
private boolean ui_GetIgnoreActionEvent()
{
    return __ignore_ActionEvent;
}

/**
Return whether ItemEvents should be ignored.
*/
private boolean ui_GetIgnoreItemEvent()
{
    return __ignore_ItemEvent;
}

/**
Return whether ListSelectionEvents should be ignored.
*/
private boolean ui_GetIgnoreListSelectionEvent()
{
    return __ignore_ListSelectionEvent;
}

//FIXME SAM 2007-11-01 Need to use /tmp etc for a startup home if not
//specified so the software install home is not used.
/**
Return the initial working directory, which will be the softare startup
home, or the location of new command files.
This directory is suitable for initializing a workflow processing run.
@return the initial working directory, which should always be non-null.
*/
private String ui_GetInitialWorkingDir ()
{
	return __initial_working_dir;
}

/**
Return the NWSRFSFS5Files instance that is active for the UI, opened from
the configuration file inforation or the NWSRFS FS5Files select dialog.
@return the NWSRFSDMI instance that is active for the UI.
*/
private NWSRFS_DMI ui_GetNWSRFSFS5FilesDMI ()
{
	return __nwsrfs_dmi;
}

/**
Get a PropList with properties needed for an old-style editor.  Mainly this is
the WorkingDir property, with a value determined from the initial working directory
and subsequent setWorkingDir() commands prior to the command being edited.
This is needed because old style editors get the information from a PropList that
is passed to the editor.
@param command_to_edit Command that is being edited.
@return the PropList containing a valid WorkingDir value, accurate at for the context
of the command being edited.
*/
private PropList ui_GetPropertiesForOldStyleEditor ( Command command_to_edit )
{
	PropList props = new PropList ( "" );
	props.set ( "WorkingDir", commandProcessor_GetWorkingDirForCommand ( command_to_edit ) );
	return props;
}

/**
Initialize the GUI.
@param show_main Indicates if the main interface should be shown.
*/
private void ui_InitGUI ( )
{	String routine = "TSTool_JFrame.initGUI";
	try {	// To catch layout problems...
	int y;

	try {
        JGUIUtil.setSystemLookAndFeel(true);
	}
	catch (Exception e) {
		Message.printWarning ( 2, routine, e );
	}

	// Need this even if no main GUI (why? secondary windows?)...

	JGUIUtil.setIcon(this, JGUIUtil.getIconImage());

	ui_InitGUIMenus ();
    ui_InitToolbar ();

	// Remainder of main window...

	// objects used throughout the GUI layout
	int buffer = 3;
	Insets insetsNLNR = new Insets(0,buffer,0,buffer);
	Insets insetsNNNR = new Insets(0,0,0,buffer);
	Insets insetsNLNN = new Insets(0,buffer,0,0);
    Insets insetsNLBR = new Insets(0,buffer,buffer,buffer);
	Insets insetsTLNR = new Insets(buffer,buffer,0,buffer);
	Insets insetsNNNN = new Insets(0,0,0,0);
    GridBagLayout gbl = new GridBagLayout();

	// Panel to hold the query components, added to the top of the main content pane...

    JPanel query_JPanel = new JPanel();
    query_JPanel.setLayout(new BorderLayout());
    getContentPane().add("North", query_JPanel);
        
	// --------------------------------------------------------------------
	// Query input components...
	// --------------------------------------------------------------------

	__query_input_JPanel = new JPanel();
	__query_input_JPanel.setLayout(gbl);
	__query_input_JPanel.setBorder(	BorderFactory.createTitledBorder (
		BorderFactory.createLineBorder(Color.black),"Input/Query Options" ));
	
    query_JPanel.add("West", __query_input_JPanel);
 
	y=0;
    JGUIUtil.addComponent(__query_input_JPanel, new JLabel("Input Type:"), 
		0, y, 1, 1, 0.0, 0.0, insetsNLNN, GridBagConstraints.NONE, GridBagConstraints.EAST);
        __input_type_JComboBox = new SimpleJComboBox(false);
        __input_type_JComboBox.setMaximumRowCount ( 15 );
        __input_type_JComboBox.setToolTipText (
		"<HTML>The input type is the file/database format being read.</HTML>" );
	__input_type_JComboBox.addItemListener( this );
        JGUIUtil.addComponent(__query_input_JPanel, __input_type_JComboBox, 
		1, y++, 2, 1, 1.0, 0.0, insetsNNNR, GridBagConstraints.NONE, GridBagConstraints.WEST);

    JGUIUtil.addComponent(__query_input_JPanel, new JLabel("Input Name:"), 
		0, y, 1, 1, 0.0, 0.0, insetsNLNN, GridBagConstraints.NONE, GridBagConstraints.EAST);
        __input_name_JComboBox = new SimpleJComboBox(false);
        __input_name_JComboBox.setToolTipText (
		"<HTML>The input name is the name of the file or database" +
		" being read.<BR>It will default or be promted for after " +
		"selecting the input type.</HTML>" );
	__input_name_JComboBox.addItemListener( this );
        JGUIUtil.addComponent(__query_input_JPanel, __input_name_JComboBox, 
		1, y++, 2, 1, 1.0, 0.0, insetsNNNR, GridBagConstraints.NONE, GridBagConstraints.WEST);

    JGUIUtil.addComponent(__query_input_JPanel, new JLabel("Data Type:"), 
		0, y, 1, 1, 0.0, 0.0, insetsNLNN, GridBagConstraints.NONE, GridBagConstraints.EAST);
	__data_type_JComboBox = new SimpleJComboBox(false);
        __data_type_JComboBox.setMaximumRowCount ( 20 );
	__data_type_JComboBox.setToolTipText (
		"<HTML>The data type is used to filter the list of time series.</HTML>" );
	__data_type_JComboBox.addItemListener( this );
    JGUIUtil.addComponent(__query_input_JPanel, __data_type_JComboBox, 
		1, y++, 2, 1, 1.0, 0.0, insetsNNNR, GridBagConstraints.NONE, GridBagConstraints.WEST);

    JGUIUtil.addComponent(__query_input_JPanel, new JLabel("Time Step:"), 
		0, y, 1, 1, 0.0, 0.0, insetsNLNN, GridBagConstraints.NONE, GridBagConstraints.EAST);
        __time_step_JComboBox = new SimpleJComboBox(false);
	__time_step_JComboBox.setToolTipText (
		"<HTML>The time step is used to filter the list of time series.</HTML>" );
	__time_step_JComboBox.addItemListener( this );
    JGUIUtil.addComponent(__query_input_JPanel, __time_step_JComboBox, 
		1, y++, 2, 1, 1.0, 0.0, insetsNNNR, GridBagConstraints.NONE, GridBagConstraints.WEST);

	// Save the position for the input filters, which will be added after database connections are made...

	__input_filter_y = y;
	++y;	// Increment for GUI features below.

	// Force the choices to assume some reasonable values...

	ui_SetInputTypeChoices();

    __get_ts_list_JButton = new SimpleJButton(BUTTON_TOP_GET_TIME_SERIES,this);
	__get_ts_list_JButton.setToolTipText (
		"<HTML>Get a list of time series but not the full time " +
		"series data.<BR>Time series can then be selected for processing.</HTML>" );
    JGUIUtil.addComponent(__query_input_JPanel, __get_ts_list_JButton, 
		2, y++, 1, 1, 0, 0, insetsTLNR, GridBagConstraints.NONE, GridBagConstraints.EAST);

	// --------------------------------------------------------------------
	// Query results components...
	// --------------------------------------------------------------------

	__query_results_JPanel = new JPanel();
    __query_results_JPanel.setLayout(gbl);
	__query_results_JPanel.setBorder(
		BorderFactory.createTitledBorder (BorderFactory.createLineBorder(Color.black),"Time Series List"));
    
    // Set the minimum size for the panel based on the default size
    // used for HydroBase, which seems to be an acceptable size.
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
    	// Listen for mouse events to enable the buttons in the Time Series area...
    	__query_JWorksheet.addMouseListener ( this );
    	__query_JWorksheet.addJWorksheetListener ( this );
        JGUIUtil.addComponent(__query_results_JPanel, sjw,
		0, y++, 3, 7, 1.0, 1.0, insetsNLBR, GridBagConstraints.BOTH, GridBagConstraints.WEST);
	}
	catch ( Exception e ) {
		// Absorb the exception in most cases - print if developing to see if this issue can be resolved.
		if ( Message.isDebugOn && IOUtil.testing()  ) {
			Message.printWarning ( 3, routine, "For developers:  caught exception initializing JWorksheet at setup." );
			Message.printWarning ( 3, routine, e );
		}
	}

	// Add the button to select all the time series...

	y = 7;
    __CopySelectedToCommands_JButton = new SimpleJButton( BUTTON_TOP_COPY_SELECTED_TO_COMMANDS,this);
	__CopySelectedToCommands_JButton.setToolTipText (
		"<HTML>Copy selected time series from above to the Commands " +
		"list,<BR>as time series identifiers.</HTML>" );
    JGUIUtil.addComponent ( __query_results_JPanel,	__CopySelectedToCommands_JButton, 
		1, y, 1, 1, 0.0, 0.0, insetsNNNR, GridBagConstraints.NONE, GridBagConstraints.EAST );
    __CopyAllToCommands_JButton = new SimpleJButton( BUTTON_TOP_COPY_ALL_TO_COMMANDS,this);
	__CopyAllToCommands_JButton.setToolTipText (
		"<HTML>Copy all time series from above to the Commands " +
		"list,<BR>as time series identifiers.</HTML>" );
    JGUIUtil.addComponent ( __query_results_JPanel,	__CopyAllToCommands_JButton, 
		2, y, 1, 1, 0.0, 0.0, insetsNNNR, GridBagConstraints.NONE, GridBagConstraints.EAST );
 
	// --------------------------------------------------------------------
	// Command components...
	// --------------------------------------------------------------------

	// Use a panel to include both the commands and time series panels and place in the center, to allow resizing.

	JPanel center_JPanel = new JPanel ();
    center_JPanel.setLayout(gbl);
    getContentPane().add("Center", center_JPanel);

    // Commands JPanel - 8 columns wide for grid bag layout
    __commands_JPanel = new JPanel();
    __commands_JPanel.setLayout(gbl);
	__commands_JPanel.setBorder(
		BorderFactory.createTitledBorder ( BorderFactory.createLineBorder(Color.black), "Commands" ));
	JGUIUtil.addComponent(center_JPanel, __commands_JPanel,
		0, 0, 1, 1, 1.0, 1.0, insetsNNNN, GridBagConstraints.BOTH, GridBagConstraints.CENTER);

	// Initialize the command processor to interact with the GUI.  The
	// initial working directory will be the software startup directory.
	__ts_processor = new TSCommandProcessor();
	ui_SetInitialWorkingDir( System.getProperty("user.dir") );
	commandProcessor_SetInitialWorkingDir ( ui_GetInitialWorkingDir() );
	// FIXME SAM 2007-08-28 Need to set a WindowListener for -nomaingui calls?
	//__ts_processor.setTSCommandProcessorUI ( this );
	__ts_processor.addCommandProcessorListener ( this );
	__commands_JListModel = new TSCommandProcessorListModel(__ts_processor);
	__commands_JListModel.addListDataListener ( this );

	__commands_AnnotatedCommandJList = new AnnotatedCommandJList ( __commands_JListModel );
	JList command_JList = __commands_AnnotatedCommandJList.getJList();
	// Set the font to fixed font so that similar command text lines up, especially for comments...
    // This looks like crap on Linux. Try using Lucida Console on windows and Linux to get some feedback.
    if ( IOUtil.isUNIXMachine() ) {
        command_JList.setFont ( new Font(__COMMANDS_FONT, Font.PLAIN, 12 ) );
    }
    else {
        // __commands_JList.setFont ( new Font("Courier", Font.PLAIN, 11 ) );
        command_JList.setFont ( new Font(__COMMANDS_FONT, Font.PLAIN, 12 ) );
    }
	// Handling the ellipsis is dealt with in the annotated list...
	
	// Listen for events on the list so the GUI can respond to selections.
	
	command_JList.addListSelectionListener ( this );
	command_JList.addKeyListener ( this );
	command_JList.addMouseListener ( this );
	
	JGUIUtil.addComponent(__commands_JPanel, __commands_AnnotatedCommandJList,
		0, 0, 8, 5, 1.0, 1.0, insetsNLNR, GridBagConstraints.BOTH, GridBagConstraints.WEST);
	
	// Popup menu for the commands list...
	
	ui_InitGUIMenus_CommandsPopup ();

	// Popup menu for the input name field...
	__input_name_JPopupMenu = new JPopupMenu("Input Name Actions");

	// Buttons that correspond to the command list 

	// Put on left because user is typically working in that area...
	__Run_SelectedCommands_JButton =
		new SimpleJButton(__Button_RunSelectedCommands_String,__Run_SelectedCommandsCreateOutput_String, this);
	__Run_SelectedCommands_JButton.setToolTipText (
		"<HTML>Run selected commands from above to generate time " +
		"series,<BR>which will be shown in the Time Series Results list below.</HTML>" );
	JGUIUtil.addComponent(__commands_JPanel, __Run_SelectedCommands_JButton,
		5, 5, 1, 1, 0.0, 0.0, insetsTLNR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	__Run_AllCommands_JButton =
		new SimpleJButton(__Button_RunAllCommands_String, __Run_AllCommandsCreateOutput_String, this);
	__Run_AllCommands_JButton.setToolTipText (
		"<HTML>Run all commands from above to generate time series," +
		"<BR>which will be shown in the Time Series Results list below.</HTML>" );
	JGUIUtil.addComponent(__commands_JPanel, __Run_AllCommands_JButton,
		6, 5, 1, 1, 0.0, 0.0, insetsTLNR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	// Put on right because we want it to be a decision to clear...
	__ClearCommands_JButton = new SimpleJButton(__Button_ClearCommands_String, this);
	__ClearCommands_JButton.setToolTipText (
		"<HTML>Clear selected commands from above.<BR>Clear all commands if none are selected.</HTML>" );
	JGUIUtil.addComponent(__commands_JPanel, __ClearCommands_JButton, 
		7, 5, 1, 1, 0.0, 0.0, insetsTLNR, GridBagConstraints.NONE, GridBagConstraints.EAST);

	// --------------------------------------------------------------------
	// Results components (listed alphabetically by label).
	// --------------------------------------------------------------------

    __results_JTabbedPane = new JTabbedPane ();
    __results_JTabbedPane.setBorder(
            BorderFactory.createTitledBorder ( BorderFactory.createLineBorder(Color.black), "Results" ));
    JGUIUtil.addComponent(center_JPanel, __results_JTabbedPane,
            0, 1, 1, 1, 1.0, 1.0, insetsNNNN, GridBagConstraints.BOTH, GridBagConstraints.CENTER);
    
    // Results: Ensembles...

    __results_tsensembles_JPanel = new JPanel();
    __results_tsensembles_JPanel.setLayout(gbl);
    /*
    __results_tsensembles_JPanel.setBorder(
        BorderFactory.createTitledBorder (
        BorderFactory.createLineBorder(Color.black),
        "Results: Ensembles of Time Series" ));
        */
    __results_tsensembles_JListModel = new DefaultListModel();
    __results_tsensembles_JList = new JList ( __results_tsensembles_JListModel );
    __results_tsensembles_JList.setSelectionMode ( ListSelectionModel.SINGLE_SELECTION );
    __results_tsensembles_JList.addKeyListener ( this );
    __results_tsensembles_JList.addListSelectionListener ( this );
    __results_tsensembles_JList.addMouseListener ( this );
    JScrollPane results_tsensembles_JScrollPane = new JScrollPane ( __results_tsensembles_JList );
    JGUIUtil.addComponent(__results_tsensembles_JPanel, results_tsensembles_JScrollPane, 
        0, 15, 8, 5, 1.0, 1.0, insetsNLNR, GridBagConstraints.BOTH, GridBagConstraints.WEST);
    __results_JTabbedPane.addTab ( "Ensembles", __results_tsensembles_JPanel );
    
    // Results: output files...

    JPanel results_files_JPanel = new JPanel();
    results_files_JPanel.setLayout(gbl);
    /*
    results_files_JPanel.setBorder(
        BorderFactory.createTitledBorder (
        BorderFactory.createLineBorder(Color.black),
        "Results: Output Files" ));
        */
    JGUIUtil.addComponent(center_JPanel, results_files_JPanel,
        0, 3, 1, 1, 1.0, 0.0, insetsNNNN, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER);
    JGUIUtil.addComponent(results_files_JPanel, new JLabel ("Output files:"),
        0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NONE, GridBagConstraints.EAST);
    __results_files_JListModel = new DefaultListModel();
    __results_files_JList = new JList ( __results_files_JListModel );
    __results_files_JList.setSelectionMode ( ListSelectionModel.SINGLE_SELECTION );
    __results_files_JList.addKeyListener ( this );
    __results_files_JList.addListSelectionListener ( this );
    __results_files_JList.addMouseListener ( this );
    JGUIUtil.addComponent(results_files_JPanel, new JScrollPane ( __results_files_JList ), 
        0, 15, 8, 5, 1.0, 1.0, insetsNLNR, GridBagConstraints.BOTH, GridBagConstraints.WEST);
    __results_JTabbedPane.addTab ( "Output Files", results_files_JPanel );
    
    // Results: tables...

    JPanel results_tables_JPanel = new JPanel();
    results_tables_JPanel.setLayout(gbl);
    /*
    results_tables_JPanel.setBorder(
        BorderFactory.createTitledBorder (
        BorderFactory.createLineBorder(Color.black),
        "Results: Tables" ));
        */
    //JGUIUtil.addComponent(center_JPanel, results_tables_JPanel,
    //    0, 2, 1, 1, 1.0, 0.0, insetsNNNN, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER);
    JGUIUtil.addComponent(results_tables_JPanel, new JLabel ("Tables:"),
        0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NONE, GridBagConstraints.EAST);
    __results_tables_JListModel = new DefaultListModel();
    __results_tables_JList = new JList ( __results_tables_JListModel );
    __results_tables_JList.setSelectionMode ( ListSelectionModel.SINGLE_SELECTION );
    __results_tables_JList.addKeyListener ( this );
    __results_tables_JList.addListSelectionListener ( this );
    __results_tables_JList.addMouseListener ( this );
    JGUIUtil.addComponent(results_tables_JPanel, new JScrollPane ( __results_tables_JList ), 
        0, 15, 8, 5, 1.0, 1.0, insetsNLNR, GridBagConstraints.BOTH, GridBagConstraints.WEST);
    __results_JTabbedPane.addTab ( "Tables", results_tables_JPanel );
    
	// Results: time series...

    __results_ts_JPanel = new JPanel();
    __results_ts_JPanel.setLayout(gbl);
    /*
	__results_ts_JPanel.setBorder(
		BorderFactory.createTitledBorder (
		BorderFactory.createLineBorder(Color.black),
		"Results: Time Series" ));
        */
	//JGUIUtil.addComponent(center_JPanel, __ts_JPanel,
	//	0, 1, 1, 1, 1.0, 1.0, insetsNNNN, GridBagConstraints.BOTH, GridBagConstraints.CENTER);

	__results_ts_JListModel = new DefaultListModel();
	__results_ts_JList = new JList ( __results_ts_JListModel );
	__results_ts_JList.addKeyListener ( this );
	__results_ts_JList.addListSelectionListener ( this );
	__results_ts_JList.addMouseListener ( this );
	JScrollPane results_ts_JScrollPane = new JScrollPane ( __results_ts_JList );
	JGUIUtil.addComponent(__results_ts_JPanel, results_ts_JScrollPane, 
		0, 15, 8, 5, 1.0, 1.0, insetsNLNR, GridBagConstraints.BOTH, GridBagConstraints.WEST);
    __results_JTabbedPane.addTab ( "Time Series", __results_ts_JPanel );
    
    // Default to time series selected since that is what most people have seen with previous
    // TSTool versions...
    
    __results_JTabbedPane.setSelectedComponent ( __results_ts_JPanel );
	
	// Popup menu for the time series results list...
	ui_InitGUIMenus_ResultsPopup ();

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
		0, 0, 5, 1, 1.0, 0.0, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
	__processor_JProgressBar = new JProgressBar ();
	__processor_JProgressBar.setToolTipText ( "Indicates progress in processing all commands.");
	__processor_JProgressBar.setStringPainted ( true );
	JGUIUtil.addComponent(bottom_JPanel, __processor_JProgressBar,
		5, 0, 2, 1, 0.0, 0.0, GridBagConstraints.NONE, GridBagConstraints.WEST);
	__command_JProgressBar = new JProgressBar ();
	__command_JProgressBar.setToolTipText ( "Indicates progress in processing the current command that is running.");
	__command_JProgressBar.setStringPainted ( true );
	JGUIUtil.addComponent(bottom_JPanel, __command_JProgressBar,
		7, 0, 2, 1, 0.0, 0.0, GridBagConstraints.NONE, GridBagConstraints.WEST);
	__status_JTextField = new JTextField ( 5 );
	__status_JTextField.setEditable(false);
	JGUIUtil.addComponent(bottom_JPanel, __status_JTextField,
		9, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NONE, GridBagConstraints.WEST);

	getContentPane().add ("South", bottom_JPanel);

	ui_UpdateStatusTextFields ( -1, "TSTool_JFrame.initGUI",
			null, "Open a command file or add new commands.",
			__STATUS_READY );
	ui_UpdateStatus ( false );
    pack();
	setSize ( 800, 600 );
	JGUIUtil.center ( this );
	// TODO SAM 2008-01-11 Need to evaluate whether server mode controls the GUI or only a command processor
	if ( !TSToolMain.isServer() ) {
       	setVisible ( true );
	}
	}
	catch ( Exception e ) {
		Message.printWarning ( 2, "", e );
	}
	__gui_initialized = true;
	// Select an input type to get the UI to a usable initial state.
	if ( __hbdmi != null ) {
		// Select HydroBase for CDSS use...
		__input_type_JComboBox.select( null );
		__input_type_JComboBox.select( __INPUT_TYPE_HydroBase );
	}
	else {
        __input_type_JComboBox.select( null );
		__input_type_JComboBox.select( __INPUT_TYPE_DateValue );
	}
}

// TODO - is this code also called when a new database connection is made
// dynamically or assume that the filters will not change?
/**
Initialize the input filters.  An input filter is defined and added for each
enabled input type but only one is set visible at a time.  Later, as an input
type is selected, the appropriate input filter is made visible.
@param y Layout position to add the input filters.
*/
private void ui_InitGUIInputFilters ( final int y )
{	// Define and add specific input filters...
    Runnable r = new Runnable() {
        public void run() {
        	String routine = "TSTool_JFrame.initGUIInputFilters";
        	int buffer = 3;
        	Insets insets = new Insets(0,buffer,0,0);
        	if ( __source_HydroBase_enabled && (__hbdmi != null) ) {
        		// Add input filters for stations...
        
        		try {
        		    __input_filter_HydroBase_station_JPanel = new
        			HydroBase_GUI_StationGeolocMeasType_InputFilter_JPanel(	__hbdmi);
                		JGUIUtil.addComponent(__query_input_JPanel,
        				__input_filter_HydroBase_station_JPanel,
        				0, y, 3, 1, 0.0, 0.0, insets, GridBagConstraints.HORIZONTAL,
        				GridBagConstraints.EAST );
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
        				0, y, 3, 1, 0.0, 0.0, insets, GridBagConstraints.HORIZONTAL,
        				GridBagConstraints.EAST );
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
        				0, y, 3, 1, 0.0, 0.0, insets, GridBagConstraints.HORIZONTAL,
        				GridBagConstraints.EAST );
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
        				0, y, 3, 1, 0.0, 0.0, insets, GridBagConstraints.HORIZONTAL,
        				GridBagConstraints.EAST );
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
        				0, y, 3, 1, 0.0, 0.0, insets, GridBagConstraints.HORIZONTAL,
        				GridBagConstraints.EAST );
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
        				0, y, 3, 1, 0.0, 0.0, insets, GridBagConstraints.HORIZONTAL,
        				GridBagConstraints.EAST );
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
        				0, y, 3, 1, 0.0, 0.0, insets, GridBagConstraints.HORIZONTAL,
        				GridBagConstraints.EAST );
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
        				0, y, 3, 1, 0.0, 0.0, insets, GridBagConstraints.HORIZONTAL,
        				GridBagConstraints.EAST );
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
        				0, y, 3, 1, 0.0, 0.0, insets, GridBagConstraints.HORIZONTAL,
        				GridBagConstraints.EAST );
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
        				0, y, 3, 1, 0.0, 0.0, insets, GridBagConstraints.HORIZONTAL,
        				GridBagConstraints.EAST);
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
        			0, y, 3, 1, 0.0, 0.0, insets, GridBagConstraints.HORIZONTAL,
        			GridBagConstraints.EAST );
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
        				0, y, 3, 1, 0.0, 0.0, insets, GridBagConstraints.HORIZONTAL,
        				GridBagConstraints.EAST );
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
        			0, y, 3, 1, 0.0, 0.0, insets, GridBagConstraints.HORIZONTAL,
        			GridBagConstraints.EAST );
        	__input_filter_generic_JPanel.setToolTipText (
        		"<HTML>The selected input type does not support" +
        			"<BR>input filters.</HTML>" );
        	__input_filter_JPanel_Vector.addElement (
        		__input_filter_generic_JPanel );
        	// The appropriate JPanel will be set visible later based on the input
        	// type that is selected.
        
        	// Because a component is added to the original GUI, need to refresh
        	// the GUI layout...
        
        	ui_SetInputFilters();
        	validate();
        	repaint();
        }
    };
    if ( SwingUtilities.isEventDispatchThread() )
    {
        r.run();
    }
    else 
    {
        SwingUtilities.invokeLater ( r );
    }
}

/**
Initialize the GUI menus.  Split out of the main initGUI method to keep the
method size under 64K.
*/
private void ui_InitGUIMenus ()
{	JMenuBar menu_bar = new JMenuBar();
	ui_InitGUIMenus_File ( menu_bar );
	ui_InitGUIMenus_Edit ( menu_bar );
	ui_InitGUIMenus_View ( menu_bar );
	ui_InitGUIMenus_Commands ( menu_bar );
	ui_InitGUIMenus_CommandsGeneral ();
	ui_InitGUIMenus_Run ( menu_bar );
	ui_InitGUIMenus_Results ( menu_bar );
	ui_InitGUIMenus_Tools ( menu_bar );
	ui_InitGUIMenus_Help ( menu_bar );
	setJMenuBar ( menu_bar );
}

/**
Initialize the GUI "Commands" menu.
*/
private void ui_InitGUIMenus_Commands ( JMenuBar menu_bar )
{	// "Commands...TS Create/Convert/Read Time Series"...

	menu_bar.add( __Commands_JMenu = new JMenu( __Commands_String, true ) );	

	__Commands_JMenu.add ( __Commands_CreateTimeSeries_JMenu=new JMenu(__Commands_CreateTimeSeries_String) );

	// Create...

	__Commands_CreateTimeSeries_JMenu.add( __Commands_Create_CreateFromList_JMenuItem =
		new SimpleJMenuItem(__Commands_Create_CreateFromList_String, this) );
    
    __Commands_CreateTimeSeries_JMenu.add( __Commands_Create_ResequenceTimeSeriesData_JMenuItem =
        new SimpleJMenuItem(__Commands_Create_ResequenceTimeSeriesData_String, this ) );

    __Commands_CreateTimeSeries_JMenu.addSeparator();
	__Commands_CreateTimeSeries_JMenu.add(__Commands_Create_TS_ChangeInterval_JMenuItem =
		new SimpleJMenuItem(__Commands_Create_TS_ChangeInterval_String, this) );

	__Commands_CreateTimeSeries_JMenu.add(__Commands_Create_TS_Copy_JMenuItem =
        new SimpleJMenuItem(__Commands_Create_TS_Copy_String, this) );

	__Commands_CreateTimeSeries_JMenu.add( __Commands_Create_TS_Disaggregate_JMenuItem =
		new SimpleJMenuItem(__Commands_Create_TS_Disaggregate_String, this) );

	__Commands_CreateTimeSeries_JMenu.add(__Commands_Create_TS_NewDayTSFromMonthAndDayTS_JMenuItem =
		new SimpleJMenuItem(__Commands_Create_TS_NewDayTSFromMonthAndDayTS_String, this) );

	__Commands_CreateTimeSeries_JMenu.add (__Commands_Create_TS_NewEndOfMonthTSFromDayTS_JMenuItem =
		new SimpleJMenuItem(__Commands_Create_TS_NewEndOfMonthTSFromDayTS_String, this ) );
	
	__Commands_CreateTimeSeries_JMenu.add (	__Commands_Create_TS_NewPatternTimeSeries_JMenuItem =
			new SimpleJMenuItem(__Commands_Create_TS_NewPatternTimeSeries_String, this ) );
	
	__Commands_CreateTimeSeries_JMenu.add (	__Commands_Create_TS_NewStatisticTimeSeries_JMenuItem =
			new SimpleJMenuItem(__Commands_Create_TS_NewStatisticTimeSeries_String, this ) );

	__Commands_CreateTimeSeries_JMenu.add (__Commands_Create_TS_NewStatisticYearTS_JMenuItem =
		new SimpleJMenuItem(__Commands_Create_TS_NewStatisticYearTS_String, this ) );

	__Commands_CreateTimeSeries_JMenu.add (__Commands_Create_TS_NewTimeSeries_JMenuItem =
		new SimpleJMenuItem(__Commands_Create_TS_NewTimeSeries_String, this ) );

	__Commands_CreateTimeSeries_JMenu.add(__Commands_Create_TS_Normalize_JMenuItem =
        new SimpleJMenuItem(__Commands_Create_TS_Normalize_String, this ) );

	__Commands_CreateTimeSeries_JMenu.add (	__Commands_Create_TS_RelativeDiff_JMenuItem =
		new SimpleJMenuItem( __Commands_Create_TS_RelativeDiff_String, this ) );

	// Convert...

	__Commands_JMenu.add ( __Commands_ConvertTSIDToReadCommand_JMenu=
		new JMenu(__Commands_ConvertTSIDToReadCommand_String) );

	__Commands_ConvertTSIDToReadCommand_JMenu.add (	__Commands_ConvertTSIDTo_ReadTimeSeries_JMenuItem =
			new SimpleJMenuItem(__Commands_ConvertTSIDTo_ReadTimeSeries_String, this ));

	if ( __source_DateValue_enabled ) {
		__Commands_ConvertTSIDToReadCommand_JMenu.add (	__Commands_ConvertTSIDTo_ReadDateValue_JMenuItem =
			new SimpleJMenuItem(__Commands_ConvertTSIDTo_ReadDateValue_String, this ));
	}
    //if ( __source_DelimitedFile_enabled ) {
    //    __Commands_ConvertTSIDToReadCommand_JMenu.add ( __Commands_ConvertTSIDTo_ReadDateValue_JMenuItem =
    //        new SimpleJMenuItem(__Commands_ConvertTSIDTo_ReadDateValue_String, this ));
    //}

	if ( __source_HydroBase_enabled ) {
		__Commands_ConvertTSIDToReadCommand_JMenu.add (	__Commands_ConvertTSIDTo_ReadHydroBase_JMenuItem =
			new SimpleJMenuItem(__Commands_ConvertTSIDTo_ReadHydroBase_String, this ) );
	}

	if ( __source_MODSIM_enabled ) {
		__Commands_ConvertTSIDToReadCommand_JMenu.add (	__Commands_ConvertTSIDTo_ReadMODSIM_JMenuItem =
			new SimpleJMenuItem(__Commands_ConvertTSIDTo_ReadMODSIM_String, this ) );
	}

	if ( __source_NWSCard_enabled ) {
		__Commands_ConvertTSIDToReadCommand_JMenu.add (	__Commands_ConvertTSIDTo_ReadNwsCard_JMenuItem =
			new SimpleJMenuItem(__Commands_ConvertTSIDTo_ReadNwsCard_String, this ) );
	}

	if ( __source_NWSRFS_FS5Files_enabled ) {
		__Commands_ConvertTSIDToReadCommand_JMenu.add (	__Commands_ConvertTSIDTo_ReadNWSRFSFS5Files_JMenuItem =
			new SimpleJMenuItem(__Commands_ConvertTSIDTo_ReadNWSRFSFS5Files_String,this ) );
	}

	if ( __source_RiverWare_enabled ) {
		__Commands_ConvertTSIDToReadCommand_JMenu.add (	__Commands_ConvertTSIDTo_ReadRiverWare_JMenuItem =
			new SimpleJMenuItem(__Commands_ConvertTSIDTo_ReadRiverWare_String, this ) );
	}

	if ( __source_StateMod_enabled ) {
		__Commands_ConvertTSIDToReadCommand_JMenu.add (	__Commands_ConvertTSIDTo_ReadStateMod_JMenuItem =
			new SimpleJMenuItem(__Commands_ConvertTSIDTo_ReadStateMod_String, this ) );
	}

	if ( __source_StateMod_enabled ) {
		__Commands_ConvertTSIDToReadCommand_JMenu.add (	__Commands_ConvertTSIDTo_ReadStateModB_JMenuItem =
			new SimpleJMenuItem(__Commands_ConvertTSIDTo_ReadStateModB_String, this ) );
	}

	if ( __source_StateMod_enabled ) {
		__Commands_ConvertTSIDToReadCommand_JMenu.add (	__Commands_ConvertTSIDTo_ReadUsgsNwis_JMenuItem =
			new SimpleJMenuItem(__Commands_ConvertTSIDTo_ReadUsgsNwis_String, this ) );
	}

	// Read...

	__Commands_JMenu.add ( __Commands_ReadTimeSeries_JMenu=	new JMenu(__Commands_ReadTimeSeries_String) );

	__Commands_ReadTimeSeries_JMenu.add (__Commands_Read_SetIncludeMissingTS_JMenuItem =
		new SimpleJMenuItem(__Commands_Read_SetIncludeMissingTS_String, this ) );

	__Commands_ReadTimeSeries_JMenu.add (__Commands_Read_SetInputPeriod_JMenuItem=
        new SimpleJMenuItem(__Commands_Read_SetInputPeriod_String, this ) );

	__Commands_ReadTimeSeries_JMenu.addSeparator ();

	if ( __source_DateValue_enabled ) {
		__Commands_ReadTimeSeries_JMenu.add(__Commands_Read_ReadDateValue_JMenuItem =
			new SimpleJMenuItem(__Commands_Read_ReadDateValue_String, this) );
	}
    
    //if ( __source_DateValue_enabled ) {
        __Commands_ReadTimeSeries_JMenu.add(__Commands_Read_ReadDelimitedFile_JMenuItem =
            new SimpleJMenuItem(__Commands_Read_ReadDelimitedFile_String, this) );
    //}
        
    if ( __source_HECDSS_enabled ) {
        __Commands_ReadTimeSeries_JMenu.add(__Commands_Read_ReadHECDSS_JMenuItem =
            new SimpleJMenuItem(__Commands_Read_ReadHECDSS_String, this) );
    }

	if ( __source_HydroBase_enabled ) {
		__Commands_ReadTimeSeries_JMenu.add(__Commands_Read_ReadHydroBase_JMenuItem =
			new SimpleJMenuItem(__Commands_Read_ReadHydroBase_String, this) );
	}

	if ( __source_MODSIM_enabled ) {
		__Commands_ReadTimeSeries_JMenu.add(__Commands_Read_ReadMODSIM_JMenuItem =
			new SimpleJMenuItem(__Commands_Read_ReadMODSIM_String, this) );
	}

	if ( __source_NWSCard_enabled ) {
		__Commands_ReadTimeSeries_JMenu.add(__Commands_Read_ReadNwsCard_JMenuItem =
			new SimpleJMenuItem(__Commands_Read_ReadNwsCard_String, this) );
	}

	/* TODO SAM 2004-09-11 need to enable
	if ( __source_NWSRFS_FS5Files_enabled ) {
		__Commands_ReadTimeSeries_JMenu.add(
			__Commands_Read_readNWSRFSFS5Files_JMenuItem =
			new SimpleJMenuItem(__Commands_Read_readNWSRFSFS5Files_String, this) );
	}
	*/

	if ( __source_StateCU_enabled ) {
		__Commands_ReadTimeSeries_JMenu.add(__Commands_Read_ReadStateCU_JMenuItem =
			new SimpleJMenuItem( __Commands_Read_ReadStateCU_String, this) );
	}
	
    if ( __source_StateCUB_enabled ) {
        __Commands_ReadTimeSeries_JMenu.add(__Commands_Read_ReadStateCUB_JMenuItem =
            new SimpleJMenuItem(__Commands_Read_ReadStateCUB_String, this) );
    }

	if ( __source_StateMod_enabled ) {
		__Commands_ReadTimeSeries_JMenu.add(__Commands_Read_ReadStateMod_JMenuItem =
			new SimpleJMenuItem(__Commands_Read_ReadStateMod_String, this) );
	}

	if ( __source_StateModB_enabled ) {
		__Commands_ReadTimeSeries_JMenu.add(__Commands_Read_ReadStateModB_JMenuItem =
			new SimpleJMenuItem(__Commands_Read_ReadStateModB_String, this) );
	}

	if ( __source_StateMod_enabled ) {
		__Commands_ReadTimeSeries_JMenu.addSeparator ();
		__Commands_ReadTimeSeries_JMenu.add(__Commands_Read_StateModMax_JMenuItem =
			new SimpleJMenuItem(__Commands_Read_StateModMax_String, this) );
	}

	__Commands_ReadTimeSeries_JMenu.addSeparator ();

	if ( __source_DateValue_enabled ) {
		__Commands_ReadTimeSeries_JMenu.add (__Commands_Read_TS_ReadDateValue_JMenuItem =
			new SimpleJMenuItem(__Commands_Read_TS_ReadDateValue_String, this) );
	}

	if ( __source_HydroBase_enabled ) {
		__Commands_ReadTimeSeries_JMenu.add (__Commands_Read_TS_ReadHydroBase_JMenuItem =
			new SimpleJMenuItem(__Commands_Read_TS_ReadHydroBase_String, this) );
	}

	if ( __source_MODSIM_enabled ) {
		__Commands_ReadTimeSeries_JMenu.add(__Commands_Read_TS_ReadMODSIM_JMenuItem =
			new SimpleJMenuItem(__Commands_Read_TS_ReadMODSIM_String, this) );
	}

	if ( __source_NDFD_enabled ) {
		__Commands_ReadTimeSeries_JMenu.add(__Commands_Read_TS_ReadNDFD_JMenuItem =
			new SimpleJMenuItem(__Commands_Read_TS_ReadNDFD_String, this) );
	}

	if ( __source_NWSCard_enabled ) {
		__Commands_ReadTimeSeries_JMenu.add(__Commands_Read_TS_ReadNwsCard_JMenuItem =
			new SimpleJMenuItem(__Commands_Read_TS_ReadNwsCard_String, this) );
	}

	if ( __source_NWSRFS_FS5Files_enabled ) {
		__Commands_ReadTimeSeries_JMenu.add(__Commands_Read_TS_ReadNWSRFSFS5Files_JMenuItem =
			new SimpleJMenuItem(__Commands_Read_TS_ReadNWSRFSFS5Files_String, this) );
	}

	if ( __source_RiverWare_enabled ) {
		__Commands_ReadTimeSeries_JMenu.add(__Commands_Read_TS_ReadRiverWare_JMenuItem =
			new SimpleJMenuItem(__Commands_Read_TS_ReadRiverWare_String, this) );
	}

	/* FIXME SAM 2008-08-21 Enable when functionality is added
	if ( __source_StateMod_enabled ) {
		__Commands_ReadTimeSeries_JMenu.add(__Commands_Read_TS_ReadStateMod_JMenuItem =
			new SimpleJMenuItem(__Commands_Read_TS_ReadStateMod_String, this) );
	}

	if ( __source_StateModB_enabled ) {
		__Commands_ReadTimeSeries_JMenu.add(__Commands_Read_TS_ReadStateModB_JMenuItem =
			new SimpleJMenuItem(__Commands_Read_TS_ReadStateModB_String, this) );
	}
	*/

	if ( __source_USGSNWIS_enabled ) {
		__Commands_ReadTimeSeries_JMenu.add(__Commands_Read_TS_ReadUsgsNwis_JMenuItem =
			new SimpleJMenuItem(__Commands_Read_TS_ReadUsgsNwis_String, this) );
	}

	// "Commands...Fill Time Series"...

	__Commands_JMenu.add ( __Commands_FillTimeSeries_JMenu=new JMenu(__Commands_FillTimeSeries_String));

	__Commands_FillTimeSeries_JMenu.add (__Commands_Fill_FillConstant_JMenuItem =
        new SimpleJMenuItem(__Commands_Fill_FillConstant_String, this ) );

	__Commands_FillTimeSeries_JMenu.add (__Commands_Fill_FillDayTSFrom2MonthTSAnd1DayTS_JMenuItem =
		new SimpleJMenuItem(__Commands_Fill_FillDayTSFrom2MonthTSAnd1DayTS_String,this) );

	__Commands_FillTimeSeries_JMenu.add (__Commands_Fill_FillFromTS_JMenuItem =
        new SimpleJMenuItem(__Commands_Fill_FillFromTS_String,this) );

	__Commands_FillTimeSeries_JMenu.add(__Commands_Fill_FillHistMonthAverage_JMenuItem =
		new SimpleJMenuItem( __Commands_Fill_FillHistMonthAverage_String, this ) );

	__Commands_FillTimeSeries_JMenu.add (__Commands_Fill_FillHistYearAverage_JMenuItem =
		new SimpleJMenuItem( __Commands_Fill_FillHistYearAverage_String, this ) );

	__Commands_FillTimeSeries_JMenu.add (__Commands_Fill_FillInterpolate_JMenuItem =
        new SimpleJMenuItem(__Commands_Fill_FillInterpolate_String, this ) );

	/* TODO SAM 2004-02-21 Comment out this menu - it may be added later.
	__Commands_FillTimeSeries_JMenu.add (
		__Commands_Fill_fillMOVE1_JMenuItem = new SimpleJMenuItem( 
		__Commands_Fill_fillMOVE1_String, this ) );
	__Commands_Fill_fillMOVE1_JMenuItem.setEnabled ( false );
	*/

	__Commands_FillTimeSeries_JMenu.add (__Commands_Fill_FillMixedStation_JMenuItem =
        new SimpleJMenuItem(__Commands_Fill_FillMixedStation_String, this ) );

	__Commands_FillTimeSeries_JMenu.add (__Commands_Fill_FillMOVE2_JMenuItem =
        new SimpleJMenuItem(__Commands_Fill_FillMOVE2_String, this ) );

	__Commands_FillTimeSeries_JMenu.add (__Commands_Fill_FillPattern_JMenuItem =
        new SimpleJMenuItem(__Commands_Fill_FillPattern_String, this ) );

	__Commands_FillTimeSeries_JMenu.add (__Commands_Fill_FillProrate_JMenuItem =
        new SimpleJMenuItem( __Commands_Fill_FillProrate_String, this ) );

	__Commands_FillTimeSeries_JMenu.add (__Commands_Fill_FillRegression_JMenuItem =
        new SimpleJMenuItem( __Commands_Fill_FillRegression_String, this ) );

	__Commands_FillTimeSeries_JMenu.add (__Commands_Fill_FillRepeat_JMenuItem =
        new SimpleJMenuItem( __Commands_Fill_FillRepeat_String, this ) );

	if ( __source_HydroBase_enabled ) {
		__Commands_FillTimeSeries_JMenu.add(__Commands_Fill_FillUsingDiversionComments_JMenuItem =
			new SimpleJMenuItem(__Commands_Fill_FillUsingDiversionComments_String,this ) );
	}

	/* FIXME DISABLE THIS FOR now - this was an RTi freebie and needs to be reworked
	_JmenuTSFillWeights = new SimpleJMenuItem(
		MENU_INTERMEDIATE_FILL_WEIGHTS,
		this );
	ts_Jmenu.add( _JmenuTSFillWeights );
	*/

	__Commands_FillTimeSeries_JMenu.addSeparator();

	__Commands_FillTimeSeries_JMenu.add (__Commands_Fill_SetAutoExtendPeriod_JMenuItem =
		new SimpleJMenuItem(__Commands_Fill_SetAutoExtendPeriod_String, this ) );

	__Commands_FillTimeSeries_JMenu.add (__Commands_Fill_SetAveragePeriod_JMenuItem =
        new SimpleJMenuItem(__Commands_Fill_SetAveragePeriod_String, this ) );

	__Commands_FillTimeSeries_JMenu.add(__Commands_Fill_SetIgnoreLEZero_JMenuItem =
        new SimpleJMenuItem(__Commands_Fill_SetIgnoreLEZero_String, this ) );

	__Commands_FillTimeSeries_JMenu.add (__Commands_Fill_SetPatternFile_JMenuItem=
        new SimpleJMenuItem(__Commands_Fill_SetPatternFile_String, this ) );

	// "Commands...Set Time Series"...

	__Commands_JMenu.add ( __Commands_SetTimeSeries_JMenu=new JMenu(__Commands_SetTimeSeries_String));

	__Commands_SetTimeSeries_JMenu.add (__Commands_Set_ReplaceValue_JMenuItem =
		new SimpleJMenuItem( __Commands_Set_ReplaceValue_String, this));
	__Commands_SetTimeSeries_JMenu.addSeparator();

	__Commands_SetTimeSeries_JMenu.add (__Commands_Set_SetConstant_JMenuItem =
		new SimpleJMenuItem( __Commands_Set_SetConstant_String, this ));

	__Commands_SetTimeSeries_JMenu.add (__Commands_Set_SetDataValue_JMenuItem =
        new SimpleJMenuItem(__Commands_Set_SetDataValue_String, this ) );

	__Commands_SetTimeSeries_JMenu.add (__Commands_Set_SetFromTS_JMenuItem =
        new SimpleJMenuItem(__Commands_Set_SetFromTS_String, this ) );

	__Commands_SetTimeSeries_JMenu.add (__Commands_Set_SetMax_JMenuItem =
        new SimpleJMenuItem(__Commands_Set_SetMax_String, this ) );
	
	__Commands_SetTimeSeries_JMenu.add (__Commands_Set_SetToMin_JMenuItem =
        new SimpleJMenuItem(__Commands_Set_SetToMin_String, this ) );
    
    __Commands_SetTimeSeries_JMenu.addSeparator ();
    __Commands_SetTimeSeries_JMenu.add ( __Commands_Set_SetTimeSeriesProperty_JMenuItem =
        new SimpleJMenuItem( __Commands_Set_SetTimeSeriesProperty_String, this ) );

	// "Commands...Manipulate Time Series"...

	__Commands_JMenu.add (__Commands_ManipulateTimeSeries_JMenu=new JMenu("Manipulate Time Series"));

	__Commands_ManipulateTimeSeries_JMenu.add (	__Commands_Manipulate_Add_JMenuItem =
        new SimpleJMenuItem( __Commands_Manipulate_Add_String,this) );

	__Commands_ManipulateTimeSeries_JMenu.add (	__Commands_Manipulate_AddConstant_JMenuItem=
        new SimpleJMenuItem( __Commands_Manipulate_AddConstant_String,this) );

	__Commands_ManipulateTimeSeries_JMenu.add(__Commands_Manipulate_AdjustExtremes_JMenuItem =
		new SimpleJMenuItem(__Commands_Manipulate_AdjustExtremes_String, this ) );

	__Commands_ManipulateTimeSeries_JMenu.add ( __Commands_Manipulate_ARMA_JMenuItem =
        new SimpleJMenuItem( __Commands_Manipulate_ARMA_String, this ) );

	__Commands_ManipulateTimeSeries_JMenu.add ( __Commands_Manipulate_Blend_JMenuItem =
        new SimpleJMenuItem( __Commands_Manipulate_Blend_String, this ) );
    
    __Commands_ManipulateTimeSeries_JMenu.add ( __Commands_Manipulate_ChangePeriod_JMenuItem =
        new SimpleJMenuItem( __Commands_Manipulate_ChangePeriod_String, this ) );

	__Commands_ManipulateTimeSeries_JMenu.add ( __Commands_Manipulate_ConvertDataUnits_JMenuItem =
		new SimpleJMenuItem( __Commands_Manipulate_ConvertDataUnits_String, this ) );

	__Commands_ManipulateTimeSeries_JMenu.add (	__Commands_Manipulate_Cumulate_JMenuItem =
        new SimpleJMenuItem(__Commands_Manipulate_Cumulate_String, this ) );

	__Commands_ManipulateTimeSeries_JMenu.add (	__Commands_Manipulate_Divide_JMenuItem =
        new SimpleJMenuItem( __Commands_Manipulate_Divide_String, this ) );

	__Commands_ManipulateTimeSeries_JMenu.add (	__Commands_Manipulate_Free_JMenuItem =
        new SimpleJMenuItem(__Commands_Manipulate_Free_String, this ) );

	__Commands_ManipulateTimeSeries_JMenu.add ( __Commands_Manipulate_Multiply_JMenuItem =
        new SimpleJMenuItem( __Commands_Manipulate_Multiply_String, this ) );

	__Commands_ManipulateTimeSeries_JMenu.add (	__Commands_Manipulate_RunningAverage_JMenuItem =
		new SimpleJMenuItem(__Commands_Manipulate_RunningAverage_String, this ) );

	__Commands_ManipulateTimeSeries_JMenu.add ( __Commands_Manipulate_Scale_JMenuItem =
        new SimpleJMenuItem(__Commands_Manipulate_Scale_String, this ) );

	__Commands_ManipulateTimeSeries_JMenu.add (	__Commands_Manipulate_ShiftTimeByInterval_JMenuItem=
		new SimpleJMenuItem(__Commands_Manipulate_ShiftTimeByInterval_String, this ) );

	__Commands_ManipulateTimeSeries_JMenu.add (	__Commands_Manipulate_Subtract_JMenuItem =
        new SimpleJMenuItem(__Commands_Manipulate_Subtract_String, this ) );

	// "Commands...Analyze Time Series"...

	__Commands_JMenu.add ( __Commands_AnalyzeTimeSeries_JMenu= new JMenu(__Commands_AnalyzeTimeSeries_String) );
	__Commands_AnalyzeTimeSeries_JMenu.add ( __Commands_Analyze_AnalyzePattern_JMenuItem =
        new SimpleJMenuItem(__Commands_Analyze_AnalyzePattern_String, this ) );

	__Commands_AnalyzeTimeSeries_JMenu.add (__Commands_Analyze_CompareTimeSeries_JMenuItem =
		new SimpleJMenuItem(__Commands_Analyze_CompareTimeSeries_String, this ) );
	
	__Commands_AnalyzeTimeSeries_JMenu.addSeparator ();
	__Commands_AnalyzeTimeSeries_JMenu.add (__Commands_Analyze_ComputeErrorTimeSeries_JMenuItem =
        new SimpleJMenuItem(__Commands_Analyze_ComputeErrorTimeSeries_String, this ) );

	/* FIXME SAM 2008-02-04 Enable after talking with Ian about new data tests and working on result validation.
	__Commands_AnalyzeTimeSeries_JMenu.addSeparator ();
	__Commands_AnalyzeTimeSeries_JMenu.add (__Commands_Analyze_NewDataTest_JMenuItem =
		new SimpleJMenuItem(__Commands_Analyze_NewDataTest_String, this ) );
	__Commands_AnalyzeTimeSeries_JMenu.add (__Commands_Analyze_ReadDataTestFromRiversideDB_JMenuItem =
		new SimpleJMenuItem(__Commands_Analyze_ReadDataTestFromRiversideDB_String, this ) );
	__Commands_AnalyzeTimeSeries_JMenu.add (__Commands_Analyze_RunDataTests_JMenuItem =
		new SimpleJMenuItem(__Commands_Analyze_RunDataTests_String, this ) );
	__Commands_AnalyzeTimeSeries_JMenu.add (__Commands_Analyze_ProcessDataTestResults_JMenuItem =
		new SimpleJMenuItem(__Commands_Analyze_ProcessDataTestResults_String, this ) );
    */

	// "Commands...Models"...

	__Commands_JMenu.add ( __Commands_Models_JMenu = new JMenu(__Commands_Models_String) );
	__Commands_Models_JMenu.add ( __Commands_Models_LagK_JMenuItem =
        new SimpleJMenuItem( __Commands_Models_LagK_String, this ) );

	// "Commands...Output Time Series"...

	__Commands_JMenu.add ( __Commands_OutputTimeSeries_JMenu=new JMenu(__Commands_OutputTimeSeries_String) );

	__Commands_OutputTimeSeries_JMenu.add (	__Commands_Output_DeselectTimeSeries_JMenuItem =
		new SimpleJMenuItem(__Commands_Output_DeselectTimeSeries_String, this ) );
	__Commands_OutputTimeSeries_JMenu.add (	__Commands_Output_SelectTimeSeries_JMenuItem =
		new SimpleJMenuItem(__Commands_Output_SelectTimeSeries_String, this ) );
	__Commands_OutputTimeSeries_JMenu.add (	__Commands_Output_SetOutputDetailedHeaders_JMenuItem =
		new SimpleJMenuItem(__Commands_Output_SetOutputDetailedHeaders_String, this ) );
	__Commands_OutputTimeSeries_JMenu.add(	__Commands_Output_SetOutputPeriod_JMenuItem =
		new SimpleJMenuItem(__Commands_Output_SetOutputPeriod_String, this ) );
	__Commands_OutputTimeSeries_JMenu.add (	__Commands_Output_SetOutputYearType_JMenuItem =
		new SimpleJMenuItem(__Commands_Output_SetOutputYearType_String, this ) );

	__Commands_OutputTimeSeries_JMenu.addSeparator ();

	__Commands_OutputTimeSeries_JMenu.add (	__Commands_Output_SortTimeSeries_JMenuItem =
		new SimpleJMenuItem(__Commands_Output_SortTimeSeries_String, this ) );

	__Commands_OutputTimeSeries_JMenu.addSeparator ();
	__Commands_OutputTimeSeries_JMenu.add (	__Commands_Output_WriteDateValue_JMenuItem =
		new SimpleJMenuItem(__Commands_Output_WriteDateValue_String, this ) );

	if ( __source_NWSCard_enabled ) {
		__Commands_OutputTimeSeries_JMenu.add ( __Commands_Output_WriteNwsCard_JMenuItem =
			new SimpleJMenuItem( __Commands_Output_WriteNwsCard_String, this ) );
	}

	if ( __source_RiverWare_enabled ) {
		__Commands_OutputTimeSeries_JMenu.add( __Commands_Output_WriteRiverWare_JMenuItem =
			new SimpleJMenuItem(__Commands_Output_WriteRiverWare_String, this ) );
	}
    
    if ( __source_SHEF_enabled ) {
        __Commands_OutputTimeSeries_JMenu.add ( __Commands_Output_WriteSHEF_JMenuItem =
            new SimpleJMenuItem(  __Commands_Output_WriteSHEF_String, this ) );
    }

	if ( __source_StateCU_enabled ) {
		__Commands_OutputTimeSeries_JMenu.add ( __Commands_Output_WriteStateCU_JMenuItem =
            new SimpleJMenuItem( __Commands_Output_WriteStateCU_String, this ) );
	}

	if ( __source_StateMod_enabled ) {
		__Commands_OutputTimeSeries_JMenu.add ( __Commands_Output_WriteStateMod_JMenuItem =
            new SimpleJMenuItem(__Commands_Output_WriteStateMod_String, this ) );
	}

	__Commands_OutputTimeSeries_JMenu.add (	__Commands_Output_WriteSummary_JMenuItem =
        new SimpleJMenuItem( __Commands_Output_WriteSummary_String, this ) );

	__Commands_OutputTimeSeries_JMenu.addSeparator ();

	__Commands_OutputTimeSeries_JMenu.add ( __Commands_Output_ProcessTSProduct_JMenuItem =
		new SimpleJMenuItem( __Commands_Output_ProcessTSProduct_String, this ) );
}

/**
Initialize the GUI "Commands...General".
*/
private void ui_InitGUIMenus_CommandsGeneral ()
{	if ( __source_HydroBase_enabled ) {
		__Commands_JMenu.addSeparator();
		__Commands_JMenu.add( __Commands_HydroBase_JMenu = new JMenu( __Commands_HydroBase_String, true ) );	
		__Commands_HydroBase_JMenu.add (__Commands_HydroBase_OpenHydroBase_JMenuItem =
			new SimpleJMenuItem(__Commands_HydroBase_OpenHydroBase_String, this ) );
	}
    /* FIXME SAM 2007-12-23 Need to re-enable when there is time
	if ( __source_NDFD_enabled ) {
		__Commands_JMenu.addSeparator();
		__Commands_JMenu.add( __Commands_NDFD_JMenu =new JMenu( __Commands_NDFD_String, true ) );	
		__Commands_NDFD_JMenu.add (	__Commands_NDFD_openNDFD_JMenuItem =
			new SimpleJMenuItem(__Commands_NDFD_openNDFD_String, this ) );
	}
    */

    // "Commands...Ensemble processing"...
    
    __Commands_JMenu.addSeparator();
    __Commands_JMenu.add ( __Commands_Ensemble_JMenu = new JMenu(__Commands_Ensemble_String) );
    __Commands_Ensemble_JMenu.add( __Commands_Ensemble_CreateEnsemble_JMenuItem =
        new SimpleJMenuItem( __Commands_Ensemble_CreateEnsemble_String, this) );
    __Commands_Ensemble_JMenu.add( __Commands_Ensemble_CopyEnsemble_JMenuItem =
        new SimpleJMenuItem( __Commands_Ensemble_CopyEnsemble_String, this) );
    
    if ( __source_NWSRFS_ESPTraceEnsemble_enabled ) {
        __Commands_Ensemble_JMenu.add( __Commands_Ensemble_ReadNwsrfsEspTraceEnsemble_JMenuItem =
        new SimpleJMenuItem( __Commands_Ensemble_ReadNwsrfsEspTraceEnsemble_String, this) );
    }
    __Commands_Ensemble_JMenu.add ( __Commands_Ensemble_TS_NewStatisticTimeSeriesFromEnsemble_JMenuItem =
        new SimpleJMenuItem( __Commands_Ensemble_TS_NewStatisticTimeSeriesFromEnsemble_String, this ) );
    
    __Commands_Ensemble_JMenu.add ( __Commands_Ensemble_TS_WeightTraces_JMenuItem =
        new SimpleJMenuItem(__Commands_Ensemble_TS_WeightTraces_String, this ) );
    if ( __source_NWSRFS_ESPTraceEnsemble_enabled ) {
        __Commands_Ensemble_JMenu.add ( __Commands_Ensemble_WriteNWSRFSESPTraceEnsemble_JMenuItem=
            new SimpleJMenuItem(__Commands_Ensemble_WriteNWSRFSESPTraceEnsemble_String,this ));
    }
    
    __Commands_JMenu.addSeparator();
    __Commands_JMenu.add( __Commands_Table_JMenu = new JMenu( __Commands_Table_String, true ) );
    __Commands_Table_JMenu.add( __Commands_Table_ReadTableFromDelimitedFile_JMenuItem =
        new SimpleJMenuItem( __Commands_Table_ReadTableFromDelimitedFile_String, this ) );

	__Commands_JMenu.addSeparator();   // Separate general commands from others
    
    __Commands_JMenu.add( __Commands_General_CheckingResults_JMenu = new JMenu( __Commands_General_CheckingResults_String, true ) );
    __Commands_General_CheckingResults_JMenu.add ( __Commands_General_CheckingResults_OpenCheckFile_JMenuItem =
        new SimpleJMenuItem( __Commands_General_CheckingResults_OpenCheckFile_String, this ) );
	
    __Commands_JMenu.add( __Commands_General_Comments_JMenu = new JMenu( __Commands_General_Comments_String, true ) );
    __Commands_General_Comments_JMenu.add ( __Commands_General_Comments_Comment_JMenuItem =
        new SimpleJMenuItem( __Commands_General_Comments_Comment_String, this ) );
    __Commands_General_Comments_JMenu.add (__Commands_General_Comments_ReadOnlyComment_JMenuItem =
        new SimpleJMenuItem( __Commands_General_Comments_ReadOnlyComment_String, this ) );
    __Commands_General_Comments_JMenu.add (__Commands_General_Comments_StartComment_JMenuItem =
        new SimpleJMenuItem( __Commands_General_Comments_StartComment_String, this ) );
    __Commands_General_Comments_JMenu.add( __Commands_General_Comments_EndComment_JMenuItem =
        new SimpleJMenuItem(__Commands_General_Comments_EndComment_String, this ) );
    
    __Commands_JMenu.add( __Commands_General_FileHandling_JMenu = new JMenu( __Commands_General_FileHandling_String, true ) );
    __Commands_General_FileHandling_JMenu.add ( __Commands_General_FileHandling_FTPGet_JMenuItem =
        new SimpleJMenuItem( __Commands_General_FileHandling_FTPGet_String, this ) );
    __Commands_General_FileHandling_JMenu.add ( __Commands_General_FileHandling_RemoveFile_JMenuItem =
        new SimpleJMenuItem( __Commands_General_FileHandling_RemoveFile_String, this ) );
    
	__Commands_JMenu.add( __Commands_General_Logging_JMenu = new JMenu( __Commands_General_Logging_String, true ) );	
	__Commands_General_Logging_JMenu.add(__Commands_General_Logging_StartLog_JMenuItem =
        new SimpleJMenuItem(__Commands_General_Logging_StartLog_String, this ) );
	__Commands_General_Logging_JMenu.add (__Commands_General_Logging_SetDebugLevel_JMenuItem =
        new SimpleJMenuItem(__Commands_General_Logging_SetDebugLevel_String, this ) );
	__Commands_General_Logging_JMenu.add ( __Commands_General_Logging_SetWarningLevel_JMenuItem =
		new SimpleJMenuItem(__Commands_General_Logging_SetWarningLevel_String, this ) );

    __Commands_JMenu.add( __Commands_General_Running_JMenu = new JMenu( __Commands_General_Running_String, true ) );
    __Commands_General_Running_JMenu.add (__Commands_General_Running_SetProperty_JMenuItem =
        new SimpleJMenuItem( __Commands_General_Running_SetProperty_String,this));
    if ( __source_NWSRFS_FS5Files_enabled ) {
        __Commands_General_Running_JMenu.add (__Commands_General_Running_SetPropertyFromNwsrfsAppDefault_JMenuItem =
        new SimpleJMenuItem( __Commands_General_Running_SetPropertyFromNwsrfsAppDefault_String,this));
    }
    __Commands_General_Running_JMenu.addSeparator();
    __Commands_General_Running_JMenu.add (__Commands_General_Running_RunCommands_JMenuItem =
        new SimpleJMenuItem( __Commands_General_Running_RunCommands_String,this));
    __Commands_General_Running_JMenu.add ( __Commands_General_Running_RunProgram_JMenuItem =
        new SimpleJMenuItem(__Commands_General_Running_RunProgram_String,this));
    __Commands_General_Running_JMenu.add ( __Commands_General_Running_RunPython_JMenuItem =
        new SimpleJMenuItem(__Commands_General_Running_RunPython_String,this));
    __Commands_General_Running_JMenu.addSeparator();
    __Commands_General_Running_JMenu.add ( __Commands_General_Running_Exit_JMenuItem =
        new SimpleJMenuItem(__Commands_General_Running_Exit_String, this ) );
	__Commands_General_Running_JMenu.add(__Commands_General_Running_SetWorkingDir_JMenuItem =
        new SimpleJMenuItem( __Commands_General_Running_SetWorkingDir_String, this ) );

    __Commands_JMenu.add( __Commands_General_TestProcessing_JMenu = new JMenu( __Commands_General_TestProcessing_String, true ) );
    __Commands_General_TestProcessing_JMenu.add ( __Commands_General_TestProcessing_WriteProperty_JMenuItem =
        new SimpleJMenuItem(__Commands_General_TestProcessing_WriteProperty_String, this ) );
    __Commands_General_TestProcessing_JMenu.add ( __Commands_General_TestProcessing_WriteTimeSeriesProperty_JMenuItem =
        new SimpleJMenuItem(__Commands_General_TestProcessing_WriteTimeSeriesProperty_String, this ) );
    __Commands_General_TestProcessing_JMenu.add ( __Commands_General_TestProcessing_CompareFiles_JMenuItem =
        new SimpleJMenuItem(__Commands_General_TestProcessing_CompareFiles_String, this ) );
    __Commands_General_TestProcessing_JMenu.addSeparator();
    __Commands_General_TestProcessing_JMenu.add ( __Commands_General_TestProcessing_CreateRegressionTestCommandFile_JMenuItem =
        new SimpleJMenuItem( __Commands_General_TestProcessing_CreateRegressionTestCommandFile_String, this ) );
    __Commands_General_TestProcessing_JMenu.add (__Commands_General_TestProcessing_StartRegressionTestResultsReport_JMenuItem =
        new SimpleJMenuItem( __Commands_General_TestProcessing_StartRegressionTestResultsReport_String,this));
    __Commands_General_TestProcessing_JMenu.add ( __Commands_General_TestProcessing_TestCommand_JMenuItem =
        new SimpleJMenuItem( __Commands_General_TestProcessing_TestCommand_String, this ) );
}

/**
Define the popup menu for the commands area.  In some cases the words that are
shown are different from the corresponding menu because the popup mixes submenus
from different menus and also popup menus are typically more abbreviated.
*/
private void ui_InitGUIMenus_CommandsPopup ()
{	// Pop-up menu to manipulate commands...
	__Commands_JPopupMenu = new JPopupMenu("Command Actions");
	__Commands_JPopupMenu.add( __CommandsPopup_ShowCommandStatus_JMenuItem =
		new SimpleJMenuItem ( __CommandsPopup_ShowCommandStatus_String,	__CommandsPopup_ShowCommandStatus_String, this ) );
	__Commands_JPopupMenu.addSeparator();
	__Commands_JPopupMenu.add( __CommandsPopup_Edit_CommandWithErrorChecking_JMenuItem =
		new SimpleJMenuItem(__Edit_String, __Edit_CommandWithErrorChecking_String, this ) );
	__Commands_JPopupMenu.addSeparator();
	__Commands_JPopupMenu.add( __CommandsPopup_Cut_JMenuItem =
        new SimpleJMenuItem( "Cut", __Edit_CutCommands_String, this ) );
	__Commands_JPopupMenu.add( __CommandsPopup_Copy_JMenuItem =
        new SimpleJMenuItem ( "Copy", __Edit_CopyCommands_String,this));
	__Commands_JPopupMenu.add( __CommandsPopup_Paste_JMenuItem=
        new SimpleJMenuItem("Paste (After Selected)", __Edit_PasteCommands_String,this));
	__Commands_JPopupMenu.addSeparator();
	__Commands_JPopupMenu.add(__CommandsPopup_Delete_JMenuItem=
		new SimpleJMenuItem( __Edit_DeleteCommands_String, __Edit_DeleteCommands_String, this ) );
	__Commands_JPopupMenu.addSeparator();
	__Commands_JPopupMenu.add( __CommandsPopup_FindCommands_JMenuItem =
		new SimpleJMenuItem(__CommandsPopup_FindCommands_String, this));
	__Commands_JPopupMenu.add( __CommandsPopup_SelectAll_JMenuItem =
		new SimpleJMenuItem ( __Edit_SelectAllCommands_String, __Edit_SelectAllCommands_String, this ) );
	__Commands_JPopupMenu.add( __CommandsPopup_DeselectAll_JMenuItem =
		new SimpleJMenuItem( __Edit_DeselectAllCommands_String, __Edit_DeselectAllCommands_String, this ) );
	__Commands_JPopupMenu.add( __CommandsPopup_ConvertSelectedCommandsToComments_JMenuItem =
		new SimpleJMenuItem ( __Edit_ConvertSelectedCommandsToComments_String, this ) );
	__Commands_JPopupMenu.add( __CommandsPopup_ConvertSelectedCommandsFromComments_JMenuItem =
		new SimpleJMenuItem ( __Edit_ConvertSelectedCommandsFromComments_String, this ) );
	__Commands_JPopupMenu.addSeparator();
	__Commands_JPopupMenu.add( __CommandsPopup_Run_AllCommandsCreateOutput_JMenuItem =
		new SimpleJMenuItem ( "Run " + __Run_AllCommandsCreateOutput_String, __Run_AllCommandsCreateOutput_String, this ) );
	__Commands_JPopupMenu.add( __CommandsPopup_Run_AllCommandsIgnoreOutput_JMenuItem =
		new SimpleJMenuItem ( "Run " + __Run_AllCommandsIgnoreOutput_String,__Run_AllCommandsIgnoreOutput_String, this ) );
	__Commands_JPopupMenu.add( __CommandsPopup_Run_SelectedCommandsCreateOutput_JMenuItem =
		new SimpleJMenuItem ( "Run " + __Run_SelectedCommandsCreateOutput_String,__Run_SelectedCommandsCreateOutput_String, this ) );
	__Commands_JPopupMenu.add( __CommandsPopup_Run_SelectedCommandsIgnoreOutput_JMenuItem =
		new SimpleJMenuItem ( "Run " + __Run_SelectedCommandsIgnoreOutput_String, __Run_SelectedCommandsIgnoreOutput_String, this ) );
}

/**
Initialize the GUI "Edit" menu.
*/
private void ui_InitGUIMenus_Edit ( JMenuBar menu_bar )
{	JMenu __Edit_JMenu = new JMenu( "Edit", true);
	menu_bar.add( __Edit_JMenu );	

	__Edit_JMenu.add( __Edit_CutCommands_JMenuItem = new SimpleJMenuItem( __Edit_CutCommands_String, this ) );
	__Edit_CutCommands_JMenuItem.setEnabled ( false );

	__Edit_JMenu.add( __Edit_CopyCommands_JMenuItem = new SimpleJMenuItem( __Edit_CopyCommands_String, this ) );
	__Edit_CopyCommands_JMenuItem.setEnabled ( false );

	__Edit_JMenu.add( __Edit_PasteCommands_JMenuItem = new SimpleJMenuItem( __Edit_PasteCommands_String, this ) );
	__Edit_PasteCommands_JMenuItem.setEnabled ( false );

	__Edit_JMenu.addSeparator( );

	__Edit_JMenu.add( __Edit_DeleteCommands_JMenuItem = new SimpleJMenuItem(__Edit_DeleteCommands_String, this) );
	__Edit_DeleteCommands_JMenuItem.setEnabled ( false );

	__Edit_JMenu.addSeparator( );

	__Edit_JMenu.add(
		__Edit_SelectAllCommands_JMenuItem = new SimpleJMenuItem( __Edit_SelectAllCommands_String, this ) );
	__Edit_JMenu.add(
		__Edit_DeselectAllCommands_JMenuItem = new SimpleJMenuItem( __Edit_DeselectAllCommands_String, this ) );

	__Edit_JMenu.addSeparator( );

	__Edit_JMenu.add( __Edit_CommandFile_JMenuItem = new SimpleJMenuItem (__Edit_CommandFile_String, this ) );
	__Edit_JMenu.add(
		__Edit_CommandWithErrorChecking_JMenuItem = new SimpleJMenuItem(__Edit_CommandWithErrorChecking_String, this ) );
	__Edit_JMenu.addSeparator( );
	__Edit_JMenu.add(__Edit_ConvertSelectedCommandsToComments_JMenuItem=
		new SimpleJMenuItem (__Edit_ConvertSelectedCommandsToComments_String, this ) );
	__Edit_JMenu.add( __Edit_ConvertSelectedCommandsFromComments_JMenuItem =
		new SimpleJMenuItem (__Edit_ConvertSelectedCommandsFromComments_String, this ) );
}

/**
Initialize the GUI "File" menu.
*/
private void ui_InitGUIMenus_File ( JMenuBar menu_bar )
{	__File_JMenu = new JMenu( __File_String, true );
	menu_bar.add( __File_JMenu );

	__File_JMenu.add( __File_Open_JMenu=new JMenu(__File_Open_String,true));

	__File_Open_JMenu.add( __File_Open_CommandFile_JMenuItem =
		new SimpleJMenuItem( __File_Open_CommandFile_String, this ) );

	boolean separator_added = false;
	if ( __source_DIADvisor_enabled ) {
		if ( !separator_added ) {
			__File_Open_JMenu.addSeparator( );
			separator_added = true;
		}
		__File_Open_JMenu.add (	__File_Open_DIADvisor_JMenuItem =
            new SimpleJMenuItem(__File_Open_DIADvisor_String, this ) );
	}
	if ( __source_HydroBase_enabled ) {
		if ( !separator_added ) {
			__File_Open_JMenu.addSeparator( );
			separator_added = true;
		}
		__File_Open_JMenu.add (
			__File_Open_HydroBase_JMenuItem = new SimpleJMenuItem( __File_Open_HydroBase_String, this ) );
	}

	if ( __source_RiversideDB_enabled ) {
		if ( !separator_added ) {
			__File_Open_JMenu.addSeparator( );
			separator_added = true;
		}
		__File_Open_JMenu.add ( __File_Open_RiversideDB_JMenuItem =
			new SimpleJMenuItem(__File_Open_RiversideDB_String, this ) );
	}

	__File_JMenu.add( __File_Save_JMenu=new JMenu(__File_Save_String,true));
	//__File_Save_Commands_JMenuItem = new SimpleJMenuItem(
		//__File_Save_Commands_String,__File_Save_Commands_ActionString,this );
	__File_Save_JMenu.add ( __File_Save_Commands_JMenuItem = new SimpleJMenuItem( __File_Save_Commands_String, this ) );
	__File_Save_JMenu.add ( __File_Save_CommandsAs_JMenuItem = new SimpleJMenuItem( __File_Save_CommandsAs_String, this ) );

	__File_Save_JMenu.add (	__File_Save_TimeSeriesAs_JMenuItem =
        new SimpleJMenuItem(__File_Save_TimeSeriesAs_String, this ) );

	__File_JMenu.add( __File_Print_JMenu=new JMenu(__File_Print_String,true));
	__File_Print_JMenu.add ( __File_Print_Commands_JMenuItem =
		new SimpleJMenuItem( __File_Print_Commands_String,__File_Print_Commands_ActionString, this ) );

	__File_JMenu.addSeparator( );

	__File_JMenu.add( __File_Properties_JMenu=new JMenu(__File_Properties_String,true));
	if ( __source_DIADvisor_enabled ) {
		__File_Properties_JMenu.add (__File_Properties_DIADvisor_JMenuItem =
			new SimpleJMenuItem(__File_Properties_DIADvisor_String, this ) );
	}

	__File_Properties_JMenu.add(__File_Properties_CommandsRun_JMenuItem =
        new SimpleJMenuItem( __File_Properties_CommandsRun_String, this ) );
	__File_Properties_JMenu.add( __File_Properties_TSToolSession_JMenuItem =
        new SimpleJMenuItem( __File_Properties_TSToolSession_String, this ) );

	boolean seperator_added = false;

	if ( __source_ColoradoSMS_enabled ) {
		if ( !seperator_added ) {
			__File_Properties_JMenu.addSeparator ();
			seperator_added = true;
		}
		__File_Properties_JMenu.add ( __File_Properties_ColoradoSMS_JMenuItem =
			new SimpleJMenuItem( __File_Properties_ColoradoSMS_String, this ) );
	}
	if ( __source_HydroBase_enabled ) {
		if ( !seperator_added ) {
			__File_Properties_JMenu.addSeparator ();
			seperator_added = true;
		}
		__File_Properties_JMenu.add ( __File_Properties_HydroBase_JMenuItem =
			new SimpleJMenuItem( __File_Properties_HydroBase_String, this ) );
	}

	if ( __source_NWSRFS_FS5Files_enabled ) {
		if ( !seperator_added ) {
			__File_Properties_JMenu.addSeparator ();
			seperator_added = true;
		}
		__File_Properties_JMenu.add ( __File_Properties_NWSRFSFS5Files_JMenuItem =
			new SimpleJMenuItem ( __File_Properties_NWSRFSFS5Files_String, this ) );
		__File_Properties_NWSRFSFS5Files_JMenuItem.setEnabled ( false );
	}

	if ( __source_RiversideDB_enabled ) {
		if ( !seperator_added ) {
			__File_Properties_JMenu.addSeparator ();
			seperator_added = true;
		}
		__File_Properties_JMenu.add ( __File_Properties_RiversideDB_JMenuItem =
			new SimpleJMenuItem ( __File_Properties_RiversideDB_String, this ) );
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
			__File_JMenu.add ( new SimpleJMenuItem( "Test", "Test",	this ) );
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
private void ui_InitGUIMenus_Help ( JMenuBar menu_bar )
{	__Help_JMenu = new JMenu ( __Help_String );
	menu_bar.add ( __Help_JMenu );
	// TODO - not implemented by Java?
	//menu_bar.setHelpMenu ( _help_JMenu );
	__Help_JMenu.add ( new SimpleJMenuItem(__Help_AboutTSTool_String,this));
	/* TODO SAM 2004-05-24 Help index features are not working as well now that
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
private void ui_InitGUIMenus_Results ( JMenuBar menu_bar )
{	menu_bar.add( __Results_JMenu = new JMenu( "Results", true ) );	

	/* TODO SAM 2004-08-04 Add later?
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

	/* TODO SAM 2004-08-04 Add later?
	__Results_JMenu.add( __Results_Graph_DoubleMass_JMenuItem =
		new SimpleJMenuItem( __Results_Graph_DoubleMass_String, this));
	__Results_Graph_DoubleMass_JMenuItem.setEnabled ( false );
	*/

	__Results_JMenu.add( __Results_Graph_Duration_JMenuItem =
		new SimpleJMenuItem( __Results_Graph_Duration_String, this ));

	__Results_JMenu.add( __Results_Graph_Line_JMenuItem=
        new SimpleJMenuItem( __Results_Graph_Line_String, this ) );

	__Results_JMenu.add( __Results_Graph_LineLogY_JMenuItem =
        new SimpleJMenuItem( __Results_Graph_LineLogY_String, this ) );

	/* SAMX Not enabled
	_view_graph_percent_exceed_JMenuItem = new SimpleJMenuItem(
	"Percent Exceedance Curve", "GraphPercentExceed", this );
	_Results_JMenu.add( _view_graph_percent_exceed_JMenuItem );
	_view_graph_percent_exceed.setEnabled ( false );
	*/

	__Results_JMenu.add( __Results_Graph_PeriodOfRecord_JMenuItem =
        new SimpleJMenuItem(__Results_Graph_PeriodOfRecord_String, this ) );

	__Results_JMenu.add(__Results_Graph_Point_JMenuItem=
        new SimpleJMenuItem( __Results_Graph_Point_String, this ) );

	__Results_JMenu.add(__Results_Graph_PredictedValue_JMenuItem =
        new SimpleJMenuItem(__Results_Graph_PredictedValue_String, this ) );

	__Results_JMenu.add(__Results_Graph_PredictedValueResidual_JMenuItem =
		new SimpleJMenuItem(__Results_Graph_PredictedValueResidual_String, this ) );

	__Results_JMenu.add(__Results_Graph_XYScatter_JMenuItem =
        new SimpleJMenuItem(__Results_Graph_XYScatter_String, this ) );

	__Results_JMenu.addSeparator();
	__Results_JMenu.add( __Results_Table_JMenuItem = new SimpleJMenuItem( __Results_Table_String, this ) );

	__Results_JMenu.addSeparator();
	__Results_JMenu.add(__Results_Report_Summary_JMenuItem =
        new SimpleJMenuItem( __Results_Report_Summary_String, this ) );

	__Results_JMenu.addSeparator();
	__Results_JMenu.add(__Results_TimeSeriesProperties_JMenuItem =
        new SimpleJMenuItem( __Results_TimeSeriesProperties_String, this ) );
}
   
/**
Define the popup menus for results.
*/
private void ui_InitGUIMenus_ResultsPopup ()
{	__results_ts_JPopupMenu = new JPopupMenu("TS Results Actions");
	__results_ts_JPopupMenu.add( new SimpleJMenuItem ( __Results_Graph_BarsLeft_String, this ) );
	__results_ts_JPopupMenu.add( new SimpleJMenuItem ( __Results_Graph_BarsCenter_String,this));
	__results_ts_JPopupMenu.add( new SimpleJMenuItem ( __Results_Graph_BarsRight_String, this ));
	__results_ts_JPopupMenu.add( new SimpleJMenuItem ( __Results_Graph_Duration_String, this ));
	__results_ts_JPopupMenu.add( new SimpleJMenuItem ( __Results_Graph_Line_String, this ) );
	__results_ts_JPopupMenu.add( new SimpleJMenuItem ( __Results_Graph_LineLogY_String, this ) );
	__results_ts_JPopupMenu.add( new SimpleJMenuItem ( __Results_Graph_PeriodOfRecord_String, this ) );
	__results_ts_JPopupMenu.add( new SimpleJMenuItem ( __Results_Graph_Point_String, this ) );
	__results_ts_JPopupMenu.add( new SimpleJMenuItem ( __Results_Graph_PredictedValue_String, this));
	__results_ts_JPopupMenu.add( new SimpleJMenuItem ( __Results_Graph_PredictedValueResidual_String, this));
	__results_ts_JPopupMenu.add( new SimpleJMenuItem ( __Results_Graph_XYScatter_String, this));
	__results_ts_JPopupMenu.addSeparator();
	__results_ts_JPopupMenu.add( new SimpleJMenuItem ( __Results_Table_String, this ) );
	__results_ts_JPopupMenu.addSeparator();
	__results_ts_JPopupMenu.add( new SimpleJMenuItem ( __Results_Report_Summary_String, this ) );
	__results_ts_JPopupMenu.addSeparator();
	__results_ts_JPopupMenu.add( new SimpleJMenuItem ( __Results_FindTimeSeries_String, this ) );
	__results_ts_JPopupMenu.add( new SimpleJMenuItem ( BUTTON_TS_SELECT_ALL, this ) );
	__results_ts_JPopupMenu.add( new SimpleJMenuItem ( BUTTON_TS_DESELECT_ALL, this ) );
	__results_ts_JPopupMenu.addSeparator();
	__results_ts_JPopupMenu.add( new SimpleJMenuItem ( __Results_TimeSeriesProperties_String, this ) );
    
    ActionListener_ResultsEnsembles ens_l = new ActionListener_ResultsEnsembles();
    __results_tsensembles_JPopupMenu = new JPopupMenu("Ensembles Results Actions");
    /* TODO SAM 2008-01-05 Evaluate what to enable, maybe add exceedance plot, etc.
    __results_ts_JPopupMenu.add( new SimpleJMenuItem (  __Results_Graph_BarsLeft_String, this ) );
    __results_ts_JPopupMenu.add( new SimpleJMenuItem (  __Results_Graph_BarsCenter_String,this));
    __results_ts_JPopupMenu.add( new SimpleJMenuItem (  __Results_Graph_BarsRight_String, this ));
    __results_ts_JPopupMenu.add( new SimpleJMenuItem (  __Results_Graph_Duration_String, this ));
    */
    __results_tsensembles_JPopupMenu.add( new SimpleJMenuItem ( __Results_Graph_Line_String, ens_l ) );
    /*
    __results_ts_JPopupMenu.add( new SimpleJMenuItem (  __Results_Graph_LineLogY_String, this ) );
    __results_ts_JPopupMenu.add( new SimpleJMenuItem (  __Results_Graph_PeriodOfRecord_String, this ) );
    __results_ts_JPopupMenu.add( new SimpleJMenuItem (  __Results_Graph_Point_String, this ) );
    __results_ts_JPopupMenu.add( new SimpleJMenuItem (  __Results_Graph_PredictedValue_String, this));
    __results_ts_JPopupMenu.add( new SimpleJMenuItem (  __Results_Graph_PredictedValueResidual_String, this));
    __results_ts_JPopupMenu.add( new SimpleJMenuItem (  __Results_Graph_XYScatter_String, this));
    __results_ts_JPopupMenu.addSeparator();
    __results_ts_JPopupMenu.add( new SimpleJMenuItem (  __Results_Table_String, this ) );
    __results_ts_JPopupMenu.addSeparator();
    __results_ts_JPopupMenu.add( new SimpleJMenuItem (  __Results_Report_Summary_String, this ) );
    __results_ts_JPopupMenu.addSeparator();
    __results_ts_JPopupMenu.add( new SimpleJMenuItem (  __Results_FindTimeSeries_String, this ) );
    __results_ts_JPopupMenu.add( new SimpleJMenuItem (  BUTTON_TS_SELECT_ALL, this ) );
    __results_ts_JPopupMenu.add( new SimpleJMenuItem (  BUTTON_TS_DESELECT_ALL, this ) );
    __results_ts_JPopupMenu.addSeparator();
    __results_ts_JPopupMenu.add( new SimpleJMenuItem (  __Results_TimeSeriesProperties_String, this ) );
    */
}

/**
Initialize the GUI "Run" menu.
*/
private void ui_InitGUIMenus_Run ( JMenuBar menu_bar )
{	__Run_JMenu = new JMenu( "Run", true);
	menu_bar.add ( __Run_JMenu );	
	__Run_JMenu.add( __Run_AllCommandsCreateOutput_JMenuItem =
		new SimpleJMenuItem(__Run_AllCommandsCreateOutput_String,this));
	__Run_JMenu.add( __Run_AllCommandsIgnoreOutput_JMenuItem =
		new SimpleJMenuItem(__Run_AllCommandsIgnoreOutput_String,this));
	__Run_JMenu.add( __Run_SelectedCommandsCreateOutput_JMenuItem =
		new SimpleJMenuItem(__Run_SelectedCommandsCreateOutput_String,this));
	__Run_JMenu.add( __Run_SelectedCommandsIgnoreOutput_JMenuItem =
		new SimpleJMenuItem(__Run_SelectedCommandsIgnoreOutput_String,this));
	__Run_JMenu.add ( __Run_CancelCommandProcessing_JMenuItem =
		new SimpleJMenuItem(__Run_CancelCommandProcessing_String,this));
	__Run_JMenu.addSeparator();
	__Run_JMenu.add(__Run_CommandsFromFile_JMenuItem =
        new SimpleJMenuItem ( __Run_CommandsFromFile_String, this ));
	__Run_JMenu.addSeparator();
	__Run_JMenu.add ( __Run_ProcessTSProductPreview_JMenuItem =
		new SimpleJMenuItem(__Run_ProcessTSProductPreview_String,this));
	__Run_JMenu.add ( __Run_ProcessTSProductOutput_JMenuItem =
		new SimpleJMenuItem(__Run_ProcessTSProductOutput_String,this));
}

/**
Initialize the GUI "Tools" menu.
*/
private void ui_InitGUIMenus_Tools ( JMenuBar menu_bar )
{	__Tools_JMenu = new JMenu();
	menu_bar.add( __Tools_JMenu = new JMenu( __Tools_String, true ) );	

	__Tools_JMenu.add ( __Tools_Analysis_JMenu =new JMenu(__Tools_Analysis_String, true ) );

	__Tools_Analysis_JMenu.add(	__Tools_Analysis_MixedStationAnalysis_JMenuItem =
		new SimpleJMenuItem(__Tools_Analysis_MixedStationAnalysis_String, this ) );

	__Tools_JMenu.add ( __Tools_Report_JMenu = new JMenu(__Tools_Report_String, true ) );

	__Tools_Report_JMenu.add(__Tools_Report_DataCoverageByYear_JMenuItem=
        new SimpleJMenuItem(__Tools_Report_DataCoverageByYear_String, this ) );

	__Tools_Report_JMenu.add (__Tools_Report_DataLimitsSummary_JMenuItem =
        new SimpleJMenuItem(__Tools_Report_DataLimitsSummary_String, this ) );

	__Tools_Report_JMenu.add (__Tools_Report_MonthSummaryDailyMeans_JMenuItem =
		new SimpleJMenuItem(__Tools_Report_MonthSummaryDailyMeans_String, this ) );

	__Tools_Report_JMenu.add (__Tools_Report_MonthSummaryDailyTotals_JMenuItem =
		new SimpleJMenuItem(__Tools_Report_MonthSummaryDailyTotals_String, this ) );

	__Tools_Report_JMenu.add (__Tools_Report_YearToDateTotal_JMenuItem =
        new SimpleJMenuItem( __Tools_Report_YearToDateTotal_String, this ) );

	if ( __source_NWSRFS_FS5Files_enabled || __source_NWSCard_enabled ||
		__source_NWSRFS_ESPTraceEnsemble_enabled ) {
		// Add NWSRFS-related tools (check all above because on
		// non-UNIX system only card type may be enabled)...
		__Tools_JMenu.addSeparator ();
		__Tools_JMenu.add ( __Tools_NWSRFS_JMenu = new JMenu(__Tools_NWSRFS_String, true ) );

		__Tools_NWSRFS_JMenu.add( __Tools_NWSRFS_ConvertNWSRFSESPTraceEnsemble_JMenuItem=
			new SimpleJMenuItem(__Tools_NWSRFS_ConvertNWSRFSESPTraceEnsemble_String,this ) );
		__Tools_NWSRFS_JMenu.add( __Tools_NWSRFS_ConvertJulianHour_JMenuItem=
			new SimpleJMenuItem(__Tools_NWSRFS_ConvertJulianHour_String, this ) );
	}

	if ( __source_RiversideDB_enabled ) {
		// Add RiversideDB-related tools...
		__Tools_JMenu.addSeparator ();
		__Tools_JMenu.add ( __Tools_RiversideDB_JMenu =	new JMenu(__Tools_RiversideDB_String, true ) );

		__Tools_RiversideDB_JMenu.add( __Tools_RiversideDB_TSProductManager_JMenuItem=
			new SimpleJMenuItem( __Tools_RiversideDB_TSProductManager_String, this ) );
	}

	// Options menu...

	__Tools_JMenu.addSeparator ();
	__Tools_JMenu.add ( __Tools_SelectOnMap_JMenuItem=new SimpleJMenuItem( __Tools_SelectOnMap_String, this));

	__Tools_JMenu.addSeparator ();
	__Tools_JMenu.add ( __Tools_Options_JMenuItem=new SimpleJMenuItem( __Tools_Options_String, this ) );

	// Create the diagnostics GUI, specifying the key for the help
	// button.  Events are handled from within the diagnostics GUI.
	__Tools_JMenu.addSeparator ();
	DiagnosticsJFrame diagnostics_JFrame = new DiagnosticsJFrame ();
	// TODO SAM 2005-04-05 some day need to enable on-line help. ("TSTool.PreferencesMenu");
	diagnostics_JFrame.attachMainMenu ( __Tools_JMenu );
	Message.addMessageLogListener ( this );
}

/**
Initialize the View menu.
*/
private void ui_InitGUIMenus_View ( JMenuBar menuBar )
{	JMenu viewJMenu = new JMenu (__View_String, true);
	menuBar.add (viewJMenu);

	viewJMenu.add ( __View_MapInterface_JCheckBoxMenuItem =	new JCheckBoxMenuItem(__View_MapInterface_String) );
	__View_MapInterface_JCheckBoxMenuItem.setState ( false );
	__View_MapInterface_JCheckBoxMenuItem.addItemListener ( this );
}

/**
Initialize the toolbar.
*/
private void ui_InitToolbar ()
{
    JToolBar toolbar = new JToolBar("TSTool Control Buttons");

    Insets none = new Insets(0, 0, 0, 0);
    URL url = null;
    /* TODO SAM 2007-12-04 Enable when decide how to handle blank file better - /tmp, etc.
    url = this.getClass().getResource( __TOOL_ICON_PATH + "/icon_newFile.gif" );
    String New_CommandFile_String = "New command file";
    if (url != null) {
        __toolbarNew_JButton = new SimpleJButton(new ImageIcon(url),
            New_CommandFile_String,
            New_CommandFile_String,
            none, false, this);
    }
 
    if ( __toolbarNew_JButton != null ) {
        // Might be null if no files are found.
        __toolbarNew_JButton.setToolTipText ( "<HTML>" + New_CommandFile_String + "</HTML>" );
        toolbar.add(__toolbarNew_JButton);
    }
    */

    url = this.getClass().getResource( __TOOL_ICON_PATH + "/icon_openFile.gif");
    String Open_CommandFile_String = "Open command file";
    if (url != null) {
        __toolbarOpen_JButton = new SimpleJButton(new ImageIcon(url),
            Open_CommandFile_String,
            Open_CommandFile_String,
            none, false, this);
    }

    if ( __toolbarOpen_JButton != null ) {
        // Might be null if no files are found.
        __toolbarOpen_JButton.setToolTipText ( "<HTML>" + Open_CommandFile_String + "/HTML>" );
        toolbar.add(__toolbarOpen_JButton);
    }

    url = this.getClass().getResource( __TOOL_ICON_PATH + "/icon_saveFile.gif");
    String Save_CommandFile_String = "Save command file";
    if (url != null) {
        __toolbarSave_JButton = new SimpleJButton(new ImageIcon(url),
            Save_CommandFile_String,
            Save_CommandFile_String,
            none, false, this);

    }

    if ( __toolbarSave_JButton != null ) {
        // Might be null if no files are found.
        __toolbarSave_JButton.setToolTipText ( "<HTML>" + Save_CommandFile_String + "/HTML>" );
        toolbar.add(__toolbarSave_JButton);
    }
    // FIXME SAM 2007-12-04 Add when some of the other layout changes - adding split pane to separate
    // commands and results, etc.  Right now this conflicts with the query panel.
    //getContentPane().add ("North", toolbar);
}

/**
Load a command file and display in the command list.
@param command_file Full path to command file to load.
@param run_on_load If true, the commands will be run after loading.
*/
private void ui_LoadCommandFile ( String command_file, boolean run_on_load )
{   String routine = "TSTool_JFrame.ui_LoadCommandFile";
    int numAutoChanges = 0; // Number of lines automatically changed during load
    try {
        numAutoChanges = commandProcessor_ReadCommandFile ( command_file );
        // Repaint the list to reflect the status of the commands...
        ui_ShowCurrentCommandListStatus();
    }
    catch ( FileNotFoundException e ) {
        Message.printWarning ( 1, routine, "Command file \"" + command_file + "\" does not exist." );
        Message.printWarning ( 3, routine, e );
        // Previous contents will remain.
        return;
    }
    catch ( IOException e ) {
        Message.printWarning ( 1, routine, "Error reading command file \"" + command_file +
                "\".  List of commands may be incomplete." );
        Message.printWarning ( 3, routine, e );
        // Previous contents will remain.
        return;
    }
    catch ( Exception e ) {
        // FIXME SAM 2007-10-09 Perhaps should revert to previous
        // data model contents?  For now allow partical contents to be
        // displayed.
        //
        // Error opening the file (should not happen but maybe a read permissions problem)...
        Message.printWarning ( 1, routine,
        "Unexpected error reading command file \"" + command_file +
        "\".  Displaying commands that could be read." );
        Message.printWarning ( 3, routine, e );
    }
    // If successful the TSCommandProcessor, as the data model, will
    // have fired actions to make the JList update.
    commandList_SetCommandFileName(command_file);
    if ( numAutoChanges == 0 ) {
        commandList_SetDirty(false);
    }
    // Clear the old results...
    results_Clear();
    // If requested, run the commands.
    if ( run_on_load ) {
        // Run all commands and create output.
        uiAction_RunCommands ( true, true );
    }
}

/**
Indicate whether running commands should occur in a thread.
The default if not specified is true.
@return true if the commands should be run in a thread, false if not.
*/
private boolean ui_Property_RunCommandProcessorInThread()
{
	String RunCommandProcessorInThread_String =
	    __props.getValue ( TSTool_Options_JDialog.TSTool_RunCommandProcessorInThread );
	if ( (RunCommandProcessorInThread_String != null) &&
			RunCommandProcessorInThread_String.equalsIgnoreCase("False") ) {
		return false;
	}
	else {
		// Default.
		return true;
	}
}

/**
Set the directory for the last "File...Open Command File".
@param Dir_LastCommandFileOpened Directory for last command file opened.
*/
private void ui_SetDir_LastCommandFileOpened ( String Dir_LastCommandFileOpened )
{
	__Dir_LastCommandFileOpened = Dir_LastCommandFileOpened;
	// Also set the last directory opened by a dialog...
	JGUIUtil.setLastFileDialogDirectory(Dir_LastCommandFileOpened);
}

/**
Set the last directory for "Run Command File", which runs an external command file
but does not show the results.
@param Dir_LastExternalCommandFileRun Directory for last command file ope
*/
private void ui_SetDir_LastExternalCommandFileRun ( String Dir_LastExternalCommandFileRun )
{
	__Dir_LastExternalCommandFileRun = Dir_LastExternalCommandFileRun;
	// Also set the last directory opened by a dialog...
	JGUIUtil.setLastFileDialogDirectory(Dir_LastExternalCommandFileRun);
}

/**
Set whether ActionEvents should be ignored (or not).  In general they should
not be ignored but in some cases when programatically modifying data models
the spurious events do not need to trigger other actions.
@param ignore whether to ignore ActionEvents.
*/
private void ui_SetIgnoreActionEvent ( boolean ignore )
{
	__ignore_ActionEvent = ignore;
}

/**
Set whether ItemEvents should be ignored (or not).  In general they should
not be ignored but in some cases when programatically modifying data models
the spurious events do not need to trigger other actions.
@param ignore whether to ignore ActionEvents.
*/
private void ui_SetIgnoreItemEvent ( boolean ignore )
{
	__ignore_ItemEvent = ignore;
}

/**
Set whether ListSelectionEvents should be ignored (or not).  In general they should
not be ignored but in some cases when programatically modifying data models
the spurious events do not need to trigger other actions.
@param ignore whether to ignore ActionEvents.
*/
private void ui_SetIgnoreListSelectionEvent ( boolean ignore )
{
	__ignore_ListSelectionEvent = ignore;
}

/**
Set the initial working directory, which will be the software startup home or
the location where the command file has been read/saved.
@param initial_working_dir The initial working directory (should be non-null).
*/
private void ui_SetInitialWorkingDir ( String initial_working_dir )
{	String routine = getClass().getName() + ".ui_SetInitialWorkingDir";
	Message.printStatus(2, routine, "Setting the initial working directory to \"" +
			initial_working_dir + "\"" );
	__initial_working_dir = initial_working_dir;
	// Also set in the processor...
	commandProcessor_SetInitialWorkingDir ( initial_working_dir );
}

/**
Set the input filters based on the current settings.  This sets the appropriate
input filter visible since all input filters are created at startup.
*/
private void ui_SetInputFilters()
{	if (	__selected_input_type.equals(__INPUT_TYPE_HydroBase) &&
		(__input_filter_HydroBase_station_JPanel != null) &&
		(__input_filter_HydroBase_structure_JPanel != null) &&
		(__input_filter_HydroBase_structure_sfut_JPanel != null) ) {
		// Can only use the HydroBase filters if they were originally
		// set up (if HydroBase was originally available)...
		String [] hb_mt = HydroBase_Util.convertToHydroBaseMeasType( __selected_data_type, __selected_time_step );

		String meas_type = hb_mt[0];
		//String vax_field = hb_mt[1];
		//String time_step = hb_mt[2];
		Message.printStatus(2, "", "isStationTimeSeriesDataType("+ __selected_data_type
				+ "," + __selected_time_step + "," + meas_type +
				")=" + HydroBase_Util.isStationTimeSeriesDataType (__hbdmi, meas_type));
		if ( HydroBase_Util.isStationTimeSeriesDataType ( __hbdmi, meas_type) ) {
			__selected_input_filter_JPanel = __input_filter_HydroBase_station_JPanel;
		}
		// Call this before the more general isStructureTimeSeriesDataType() method...
		else if ( HydroBase_Util.isStructureSFUTTimeSeriesDataType ( __hbdmi, meas_type) ) {
			__selected_input_filter_JPanel = __input_filter_HydroBase_structure_sfut_JPanel;
		}
		else if ( HydroBase_Util.isStructureTimeSeriesDataType ( __hbdmi, meas_type) ) {
			__selected_input_filter_JPanel = __input_filter_HydroBase_structure_JPanel;
		}
		else if ((__input_filter_HydroBase_CASSCropStats_JPanel != null)
			&& HydroBase_Util.isAgriculturalCASSCropStatsTimeSeriesDataType (	__hbdmi, __selected_data_type) ) {
			//Message.printStatus (2, "","Displaying CASS crop stats panel");
			__selected_input_filter_JPanel = __input_filter_HydroBase_CASSCropStats_JPanel;
		}
		else if ((__input_filter_HydroBase_CASSLivestockStats_JPanel != null) && HydroBase_Util.
			isAgriculturalCASSLivestockStatsTimeSeriesDataType ( __hbdmi, __selected_data_type) ) {
			//Message.printStatus (2, "","Displaying CASS livestock stats panel");
			__selected_input_filter_JPanel = __input_filter_HydroBase_CASSLivestockStats_JPanel;
		}
		else if ((__input_filter_HydroBase_CUPopulation_JPanel != null) && HydroBase_Util.
			isCUPopulationTimeSeriesDataType ( __hbdmi, __selected_data_type) ) {
			//Message.printStatus (2, "","Displaying CU population panel");
			__selected_input_filter_JPanel = __input_filter_HydroBase_CUPopulation_JPanel;
		}
		else if ( (__input_filter_HydroBase_NASS_JPanel != null) &&
			HydroBase_Util.isAgriculturalNASSCropStatsTimeSeriesDataType (	__hbdmi, __selected_data_type) ) {
			//Message.printStatus (2, "","Displaying NASS agstats panel");
			__selected_input_filter_JPanel = __input_filter_HydroBase_NASS_JPanel;
		}
		else if ( (__input_filter_HydroBase_irrigts_JPanel != null) &&
			HydroBase_Util.isIrrigSummaryTimeSeriesDataType ( __hbdmi, __selected_data_type) ) {
			__selected_input_filter_JPanel = __input_filter_HydroBase_irrigts_JPanel;
		}
		else if ((__input_filter_HydroBase_wells_JPanel != null) 
		    && HydroBase_Util.isGroundWaterWellTimeSeriesDataType( __hbdmi, __selected_data_type)) {
			if (__selected_time_step.equals(__TIMESTEP_IRREGULAR)) {
				__selected_input_filter_JPanel = __input_filter_HydroBase_station_JPanel;
			}
			else {
				__selected_input_filter_JPanel = __input_filter_HydroBase_wells_JPanel;
			}
		}		
		else if ( (__input_filter_HydroBase_WIS_JPanel != null) &&
			HydroBase_Util.isWISTimeSeriesDataType ( __hbdmi, __selected_data_type) ) {
			__selected_input_filter_JPanel = __input_filter_HydroBase_WIS_JPanel;
		}
		
		else {
            // Generic input filter does not have anything...
			__selected_input_filter_JPanel = __input_filter_generic_JPanel;
		}
	}
	else if(__selected_input_type.equals(__INPUT_TYPE_MEXICO_CSMN) &&
		(__input_filter_MexicoCSMN_JPanel != null) ) {
		// Can only use the Mexico CSMN filters if they were originally set up...
		__selected_input_filter_JPanel=__input_filter_MexicoCSMN_JPanel;
	}
	else if(__selected_input_type.equals(__INPUT_TYPE_NWSRFS_FS5Files) &&
		(__input_filter_NWSRFS_FS5Files_JPanel != null) ) {
		__selected_input_filter_JPanel =
		__input_filter_NWSRFS_FS5Files_JPanel;
	}
	else {
        // Currently no other input types support filtering - this may also be used if HydroBase input
        // filters were not set up due to a missing database connection...
		__selected_input_filter_JPanel = __input_filter_generic_JPanel;
	}
	// Now loop through the available input filter panels and set visible the selected one...
	int size = __input_filter_JPanel_Vector.size();
	JPanel input_filter_JPanel;
	for ( int i = 0; i < size; i++ ) {
		input_filter_JPanel = (JPanel)__input_filter_JPanel_Vector.elementAt(i);
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
private void ui_SetInputTypeChoices ()
{	//Message.printStatus ( 1, "", "SAMX - setting input type choices..." );
	// Ignore item events while manipulating.  This should prevent
	// unnecessary error-handling at startup.
	ui_SetIgnoreItemEvent ( true );
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
    if ( __source_HECDSS_enabled ) {
        __input_type_JComboBox.add( __INPUT_TYPE_HECDSS );
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
	// TODO - don't know how to parse SHEF yet...
	//if ( __source_SHEF_enabled ) {
		//__input_type_JComboBox.add( __INPUT_TYPE_SHEF );
	//}
	if ( __source_StateCU_enabled ) {
		__input_type_JComboBox.add( __INPUT_TYPE_StateCU );
	}
    if ( __source_StateCUB_enabled ) {
        __input_type_JComboBox.add( __INPUT_TYPE_StateCUB );
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

	ui_SetIgnoreItemEvent ( false );

	if ( __source_HydroBase_enabled ) {
		// If enabled, select it because the users probably want it as the choice...
		__input_type_JComboBox.select( null );
		__input_type_JComboBox.select( __INPUT_TYPE_HydroBase );
	}
	else {
        // Select the first item in the list...
		__input_type_JComboBox.select ( null );
		__input_type_JComboBox.select ( 0 );
	}
}

/**
Update the command list to show the current status.  This is called after all commands
have been processed in run mode(), when a command has been edited(), and when loading
commands from a file.
*/
private void ui_ShowCurrentCommandListStatus ()
{
    __commands_AnnotatedCommandJList.repaint();
}

/**
Update the main status information when the list contents have changed.  This
method should be called after any change to the query, command, or time series
results list.
Interface tasks include:
<ol>
<li>	Set the title bar.
	If no command file has been read, the title will be:
	"TSTool - no commands saved".
	If a command file has been read but not modified, the title will be:
	"TSTool - "filename"".
	If a command file has been read and modified, the title will be:
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
private void ui_UpdateStatus ( boolean check_gui_state )
{	// Title bar (command file)...
	
	if ( __command_file_name == null ) {
		setTitle ( "TSTool - no commands saved");
	}
	else {
        if ( __commands_dirty ) {
			setTitle ( "TSTool - \"" + __command_file_name + "\" (modified)");
		}
		else {
            setTitle ( "TSTool - \"" + __command_file_name + "\"");
		}
	}
	
	// Query results...
	
	int selected_size = 0;
	if ( __query_results_JPanel != null ) {
		int size = 0;
		if ( __query_JWorksheet != null ) {
			try {
                size=__query_JWorksheet.getRowCount();
				selected_size=__query_JWorksheet.getSelectedRowCount();
			}
			catch ( Exception e ) {
				// Absorb the exception in most cases - print if
				// developing to see if this issue can be
				// resolved.
				if ( Message.isDebugOn && IOUtil.testing()  ) {
					String routine = "TSTool_JFrame.updateStatus";
					Message.printWarning ( 3, routine,
					"For developers:  caught exception in clearQueryList JWorksheet at setup." );
					Message.printWarning ( 3, routine, e );
				}
			}
		}
        __query_results_JPanel.setBorder(
		BorderFactory.createTitledBorder (
		BorderFactory.createLineBorder(Color.black),
		"Time Series List (" + size + " time series, " + selected_size + " selected)") );
	}
	
	// Commands....
	
	int selected_indices[] = ui_GetCommandJList().getSelectedIndices();
	selected_size = 0;
	if ( selected_indices != null ) {
		selected_size = selected_indices.length;
	}
	if ( __commands_JPanel != null ) {
        	__commands_JPanel.setBorder(
			BorderFactory.createTitledBorder (
			BorderFactory.createLineBorder(Color.black),
			"Commands (" + __commands_JListModel.size() +
			" commands, " + selected_size + " selected, " +
			commandList_GetFailureCount() + " with failures, " +
			commandList_GetWarningCount() + " with warnings)") );
	}
	
	// Results...
	
	selected_indices = __results_ts_JList.getSelectedIndices();
	selected_size = 0;
	if ( selected_indices != null ) {
		selected_size = selected_indices.length;
	}
	if ( __results_ts_JPanel != null ) {
        	__results_ts_JPanel.setBorder(
			BorderFactory.createTitledBorder (
			BorderFactory.createLineBorder(Color.black),
			//"Results: Time Series (" +	
            "" + __results_ts_JListModel.size() + " time series, " + selected_size + " selected") );
	}
	// TODO SAM 2007-08-31 Evaluate call here - probably should call elsewhere
	//ui_UpdateStatusTextFields ( -1, "TSTool_JFrame.updateStatus", null,
	//			__STATUS_READY );
	ui_CheckGUIState ();
}

/**
Update the text fields at the bottom of the main interface.  This does NOT update
all text fields like the number of commands, etc.
@param level Message level.  If > 0 and the message is not null, call
Message.printStatus() to record a message.
@param routine Routine name used if Message.printStatus() is called.
@param command_panel_status 
@param message If not null, update the __message_JTextField to contain this
text.  If null, leave the contents as previously shown.  Specify "" to clear the
text.
@param status If not null, update the __status_JTextField to contain
this text.  If null, leave the contents as previously shown.  Specify "" to
clear the text.
*/
private void ui_UpdateStatusTextFields ( int level, String routine,
		String command_panel_status, String message, String status )
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
Handle a group of action, for the 
@param event Event to handle.
*/
private void uiAction_ActionPerformed1_MainActions (ActionEvent event)
throws Exception
{	String command = event.getActionCommand();
	Object o = event.getSource();

	// Next list menus or commands...
	if (command.equals(BUTTON_TOP_GET_TIME_SERIES) ) {
		uiAction_GetTimeSeriesListClicked();
	}
	else if ( o == __CopySelectedToCommands_JButton ) {
		// Transfer from the time series list to the commands...
		uiAction_TransferSelectedQueryResultsToCommandList();
	}
	else if ( o == __CopyAllToCommands_JButton ) {
		// Transfer from the time series list to the commands...
		uiAction_TransferAllQueryResultsToCommandList();
	}
	else if (command.equals(__Button_RunSelectedCommands_String) ) {
		// Can be a button or a menu?
		uiAction_RunCommands ( false, true );
	}
	else if (command.equals(__Button_RunAllCommands_String) ) {
		// Can be a button or a menu?
		uiAction_RunCommands ( true, true );
	}
	else if (command.equals(__Button_ClearCommands_String) ) {
		commandList_RemoveCommandsBasedOnUI();
	}
	else if (command.equals(BUTTON_TS_SELECT_ALL) ) {
		JGUIUtil.selectAll ( __results_ts_JList );
	}
	else if (command.equals(BUTTON_TS_DESELECT_ALL) ) {
		__results_ts_JList.clearSelection();
	}
	else {
        // Chain to the next method...
		uiAction_ActionPerformed2_FileMenu(event);
	}
}

/**
Handle a group of actions for the File menu.
@param event Event to handle.
*/
private void uiAction_ActionPerformed2_FileMenu (ActionEvent event)
throws Exception
{	Object o = event.getSource();
	String command = event.getActionCommand();
	String rtn = "FileMenu";

	// File Menu actions...

	if ( o == __File_Open_CommandFile_JMenuItem ) {
		try {
            uiAction_OpenCommandFile();
		}
		catch ( Exception e ) {
			Message.printWarning ( 1, rtn, "Error reading command file" );
			Message.printWarning( 3, "", e);
		}
	}
	else if ( command.equals ( __File_Open_DIADvisor_String )) {
		// Open a connections to the DIADvisor operational and archive
		// databases - currently assumed to be MS Access. 
		uiAction_OpenDIADvisor ();
		// Force the choices to refresh...
		if ( (__DIADvisor_dmi != null) && (__DIADvisor_archive_dmi != null) ) {
			__input_type_JComboBox.select ( null );
			__input_type_JComboBox.select ( __INPUT_TYPE_DIADvisor);
		}
	}
	else if ( command.equals ( __File_Open_HydroBase_String )) {
		uiAction_OpenHydroBase ( false );
		// Force the choices to refresh...
		if ( __hbdmi != null ) {
			__input_type_JComboBox.select ( null );
			__input_type_JComboBox.select (__INPUT_TYPE_HydroBase);
		}
	}
	else if ( command.equals ( __File_Open_RiversideDB_String )) {
		// Read a RiverTrak config file, get the RiversideDB properties,
		// and open the database...
		JFileChooser fc = JFileChooserFactory.createJFileChooser( JGUIUtil.getLastFileDialogDirectory() );
		fc.setDialogTitle( "Select a RiverTrak or TSTool Configuration File" );
		SimpleFileFilter sff = new SimpleFileFilter ( "cfg", "RiverTrak/TSTool Configuration File" );
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
		uiAction_OpenRiversideDB ( rprops, false );
		// Force the choices to refresh...
		__input_type_JComboBox.select ( null );
		__input_type_JComboBox.select ( __INPUT_TYPE_RiversideDB );
	}
	else if ( o == __File_Save_Commands_JMenuItem ) {
		try {
            if ( __command_file_name != null ) {
				// Use the existing name...
                int x = ResponseJDialog.OK;
                if ( __ts_processor.getReadOnly() ) {
                    x = new ResponseJDialog ( this, IOUtil.getProgramName(),
                        "The commands are marked read-only.\n" +
                        "Press Cancel and save to a new name if desired.  Press OK to update the read-only file.",
                        ResponseJDialog.OK|ResponseJDialog.CANCEL).response();
                }
                if ( x == ResponseJDialog.OK ) {
                    uiAction_WriteCommandFile ( __command_file_name,false);
                }
			}
			else {
                // No command file has been saved - prompt for the name...
				uiAction_WriteCommandFile ( __command_file_name, true);
			}
		}
		catch ( Exception e ) {
			Message.printWarning ( 1, rtn, "Error writing command file." );
		}
	}
	else if ( o == __File_Save_CommandsAs_JMenuItem ) {
		try {
            // Prompt for the name...
			uiAction_WriteCommandFile ( __command_file_name, true);
		}
		catch ( Exception e ) {
			Message.printWarning ( 1, rtn, "Error writing command file." );
		}
	}
    else if ( command.equals(__File_Save_TimeSeriesAs_String) ) {
		// Can save in a number of formats.  Allow the user to pick using a file chooser...
		uiAction_SaveTimeSeries ();
	}
	else if (command.equals(__File_Print_Commands_ActionString) ) {
		// Get all commands as strings for printing
		try {
            PrintJGUI.print ( this, commandList_GetCommandStrings(true), null, 10 );
		}
		catch ( Exception e ) {
			Message.printWarning ( 1, rtn, "Error printing commands." );
		}
	}
	else if ( command.equals(__File_Properties_CommandsRun_String) ) {
		// Simple text display of last commands run data from TSEngine.
		uiAction_ShowProperties_CommandsRun();
	}
    else if ( command.equals(__File_Properties_TSToolSession_String) ) {
		// Simple text display of session data, including last
		// command file that was read.  Put here where in the past
		// information was shown in labels.  Now need the label space
		// for other information.
		PropList reportProp = new PropList ("ReportJFrame.props");
		// Too big (make this big when we have more stuff)...
		//reportProp.set ( "TotalWidth", "750" );
		//reportProp.set ( "TotalHeight", "550" );
		reportProp.set ( "TotalWidth", "600" );
		reportProp.set ( "TotalHeight", "300" );
		reportProp.set ( "DisplayFont", __FIXED_WIDTH_FONT );
		reportProp.set ( "DisplaySize", "11" );
		reportProp.set ( "PrintFont", __FIXED_WIDTH_FONT );
		reportProp.set ( "PrintSize", "7" );
		reportProp.set ( "Title", "TSTool Session Properties" );
		Vector v = new Vector ( 4 );
		v.addElement ( "TSTool Session Properties" );
		v.addElement ( "" );
		if ( __command_file_name == null ) {
			v.addElement ( "No command file has been read or saved." );
		}
		else {
            v.addElement ( "Last command file read/saved: \"" + __command_file_name + "\"" );
		}
		v.addElement ( "Current working directory = " + IOUtil.getProgramWorkingDir() );
		v.addElement ( "Run commands in thread = " + ui_Property_RunCommandProcessorInThread() );
		// List open database information...
		if ( __source_ColoradoSMS_enabled ) {
			v.addElement ( "" );
			if ( __smsdmi == null ) {
				v.addElement ( "GUI ColoradoSMS connection not defined.");
			}
			else {
                v.addElement ( "GUI ColoradoSMS connection information:" );
				StringUtil.addListToStringList ( v,	StringUtil.toVector( __smsdmi.getVersionComments() ) );
			}
		}
		if ( __source_HydroBase_enabled ) {
			v.addElement ( "" );
			if ( __hbdmi == null ) {
				v.addElement ( "GUI HydroBase connection not defined.");
			}
			else {
                v.addElement ( "GUI HydroBase connection information:" );
				StringUtil.addListToStringList ( v, StringUtil.toVector( __hbdmi.getVersionComments() ) );
			}
		}
		// List enabled data types...
		v.addElement ( "Input types and whether enabled:" );
		v.addElement ( "" );
		if ( __source_HydroBase_enabled ) {
			v.addElement ( "ColoradoSMS input type is enabled" );
		}
		else {
            v.addElement ( "ColoradoSMS input type is not enabled");
		}
		if ( __source_DateValue_enabled ) {
			v.addElement ( "DateValue input type is enabled" );
		}
		else {
            v.addElement ( "DateValue input type is not enabled" );
		}
		if ( __source_DIADvisor_enabled ) {
			v.addElement ( "DIADvisor input type is enabled" );
		}
		else {
            v.addElement ( "DIADvisor input type is not enabled" );
		}
        if ( __source_HECDSS_enabled ) {
            v.addElement ( "HEC-DSS input type is enabled" );
        }
        else {
            v.addElement ( "HEC-DSS input type is not enabled");
        }
		if ( __source_HydroBase_enabled ) {
			v.addElement ( "HydroBase input type is enabled" );
		}
		else {
            v.addElement ( "HydroBase input type is not enabled" );
		}
		if ( __source_MexicoCSMN_enabled ) {
			v.addElement ( "MexicoCSMN input type is enabled" );
		}
		else {
            v.addElement ( "MexicoCSMN input type is not enabled" );
		}
		if ( __source_MODSIM_enabled ) {
			v.addElement ( "MODSIM input type is enabled" );
		}
		else {
            v.addElement ( "MODSIM input type is not enabled" );
		}
		if ( __source_NDFD_enabled  ) {
			v.addElement ( "NDFD input type is enabled" );
		}
		else {
            v.addElement ( "NDFD input type is not enabled" );
		}
		if ( __source_NWSCard_enabled  ) {
			v.addElement ( "NWSCARD input type is enabled" );
		}
		else {
            v.addElement ( "NWSCARD input type is not enabled" );
		}
		if ( __source_NWSRFS_FS5Files_enabled ) {
			v.addElement ( "NWSRFS FS5Files input type is enabled");
		}
		else {
            v.addElement ( "NWSRFS FS5Files input type is not enabled" );
		}
		if ( __source_NWSRFS_ESPTraceEnsemble_enabled ) {
			v.addElement ( "NWSRFS_ESPTraceEnsemble input type is enabled" );
		}
		else {
            v.addElement ( "NWSRFS_ESPTraceEnsemble input type is not enabled" );
		}
		if ( __source_RiversideDB_enabled ) {
			v.addElement ( "RiversideDB input type is enabled" );
		}
		else {
            v.addElement ( "RiversideDB input type is not enabled");
		}
		if ( __source_RiverWare_enabled ) {
			v.addElement ( "RiverWare input type is enabled" );
		}
		else {
            v.addElement ( "RiverWare input type is not enabled");
		}
		if ( __source_SHEF_enabled ) {
			v.addElement ( "SHEF input type is enabled" );
		}
		else {
            v.addElement ( "SHEF input type is not enabled");
		}
		if ( __source_StateCU_enabled ) {
			v.addElement ( "StateCU input type is enabled" );
		}
		else {
            v.addElement ( "StateCU input type is not enabled");
		}
        if ( __source_StateCUB_enabled ) {
            v.addElement ( "StateCUB input type is enabled" );
        }
        else {
            v.addElement ( "StateCUB input type is not enabled");
        }
		if ( __source_StateMod_enabled ) {
			v.addElement ( "StateMod input type is enabled" );
		}
		else {
            v.addElement ( "StateMod input type is not enabled");
		}
		if ( __source_StateModB_enabled ) {
			v.addElement ( "StateModB input type is enabled" );
		}
		else {
            v.addElement ( "StateModB input type is not enabled");
		}
		if ( __source_USGSNWIS_enabled ) {
			v.addElement ( "USGSNWIS input type is enabled" );
		}
		else {
            v.addElement ( "USGSNWIS input type is not enabled");
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
		reportProp.set ( "DisplayFont", __FIXED_WIDTH_FONT );
		reportProp.set ( "DisplaySize", "11" );
		reportProp.set ( "PrintFont", __FIXED_WIDTH_FONT );
		reportProp.set ( "PrintSize", "7" );
		reportProp.set ( "Title", "Colorado SMS Properties" );
		Vector v = null;
		if ( __hbdmi == null ) {
			v.addElement ( "Colorado SMS Properties" );
			v.addElement ( "" );
			v.addElement("No Colorado SMS database is available." );
		}
		else {
            v = __smsdmi.getDatabaseProperties();
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
		reportProp.set ( "DisplayFont", __FIXED_WIDTH_FONT );
		reportProp.set ( "DisplaySize", "11" );
		reportProp.set ( "PrintFont", __FIXED_WIDTH_FONT );
		reportProp.set ( "PrintSize", "7" );
		reportProp.set ( "Title", "DIADvisor Properties" );
		Vector v = new Vector();
		v.addElement ( "DIADvisor Operational Database:" );
		v.addElement ( "" );
		StringUtil.addListToStringList ( v, __DIADvisor_dmi.getDatabaseProperties ( 3 ) );
		v.addElement ( "" );
		v.addElement ( "DIADvisor Archive Database:" );
		v.addElement ( "" );
		StringUtil.addListToStringList ( v, __DIADvisor_archive_dmi.getDatabaseProperties ( 3 ) );
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
		reportProp.set ( "DisplayFont", __FIXED_WIDTH_FONT );
		reportProp.set ( "DisplaySize", "11" );
		reportProp.set ( "PrintFont", __FIXED_WIDTH_FONT );
		reportProp.set ( "PrintSize", "7" );
		reportProp.set ( "Title", "HydroBase Properties" );
		Vector v = null;
		if ( __hbdmi == null ) {
			v.addElement ( "HydroBase Properties" );
			v.addElement ( "" );
			v.addElement ( "No HydroBase database is available." );
		}
		else {
            v = __hbdmi.getDatabaseProperties();
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
		reportProp.set ( "DisplayFont", __FIXED_WIDTH_FONT );
		reportProp.set ( "DisplaySize", "11" );
		reportProp.set ( "PrintFont", __FIXED_WIDTH_FONT );
		reportProp.set ( "PrintSize", "7" );
		reportProp.set ( "Title", "NWSRFS FS5Files Properties" );
		Vector v = null;
		if ( __nwsrfs_dmi == null ) {
			v = new Vector ( 1 );
			v.addElement ( "The NWSRFS FS5Files connection is not open." );
		}
		else {
            v = __nwsrfs_dmi.getDatabaseProperties ( 3 );
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
		reportProp.set ( "DisplayFont", __FIXED_WIDTH_FONT );
		reportProp.set ( "DisplaySize", "11" );
		reportProp.set ( "PrintFont", __FIXED_WIDTH_FONT );
		reportProp.set ( "PrintSize", "7" );
		reportProp.set ( "Title", "RiversideDB Properties" );
		Vector v = __rdmi.getDatabaseProperties ( 3 );
		new ReportJFrame ( v, reportProp );
		// Clean up...
		v = null;
		reportProp = null;
	}
    else if ( o == __File_SetWorkingDirectory_JMenuItem ) {
		JFileChooser fc = JFileChooserFactory.createJFileChooser ( JGUIUtil.getLastFileDialogDirectory() );
		fc.setDialogTitle( "Set the Working Directory (normally only use if a commands file was not opened or saved)");
		fc.setFileSelectionMode (JFileChooser.DIRECTORIES_ONLY );
		if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			String directory = fc.getSelectedFile().getPath();
			IOUtil.setProgramWorkingDir(directory);
			// TODO - is this needed with Swing?
			// Reset to make sure the ending delimiter is removed...
			__props.set("WorkingDir",IOUtil.getProgramWorkingDir());
			ui_SetInitialWorkingDir (__props.getValue ("WorkingDir"));
			JGUIUtil.setLastFileDialogDirectory(directory);
		}
	}
    else if (command.equals(__Run_ProcessTSProductPreview_String) ) {
		try {
            uiAction_ProcessTSProductFile ( true );
		}
		catch ( Exception te ) {
			Message.printWarning ( 1, "", "Error processing TSProduct file." );
			Message.printWarning ( 2, "", te );
		}
	}
    else if ( command.equals(__File_Exit_String) ) {
		uiAction_FileExitClicked();
    }
	else if (command.equals("Test") ) {
		// Test code...
		try {
            uiAction_Test();
		}
		catch ( Exception te ) {
			Message.printWarning ( 1, "", "Error running test" );
			Message.printWarning ( 2, "", te );
		}
	}
    else {
    	// Chain to the next action handler...
    	uiAction_ActionPerformed3_EditMenu ( event );
    }
}

/**
Handle a group of actions for the Edit menu.
@param event Event to handle.
*/
private void uiAction_ActionPerformed3_EditMenu (ActionEvent event)
throws Exception
{	String command = event.getActionCommand();

	// Edit menu actions (in order of menu)...

    if ( command.equals(__Edit_CutCommands_String) ) {
		// Need to think whether this should work for the time series
		// list or only the commands.  Copy to the buffer...
		uiAction_CopyFromCommandListToCutBuffer( true );
	}
    else if ( command.equals(__Edit_CopyCommands_String) ) {
		// Copy to the buffer...
		uiAction_CopyFromCommandListToCutBuffer( false );
	}
    else if ( command.equals(__Edit_PasteCommands_String) ) {
		// Copy the buffer to the final list...
		uiAction_PasteFromCutBufferToCommandList();
	}
    else if ( command.equals(__Edit_DeleteCommands_String) ) {
		// The commands WILL NOT remain in the cut buffer.  Now clear the list of selected commands...
		commandList_RemoveCommandsBasedOnUI();
	}
    else if ( command.equals(__Edit_SelectAllCommands_String) ) {
    	uiAction_SelectAllCommands();
	}
    else if ( command.equals(__Edit_DeselectAllCommands_String) ) {
		uiAction_DeselectAllCommands();
	}
    else if ( command.equals(__Edit_CommandFile_String) ) {
    	uiAction_EditCommandFile();
	}
	else if ( command.equals(__Edit_CommandWithErrorChecking_String) ) {
		// Edit the first selected item, unless a comment, in which case all are edited...
		uiAction_EditCommand ();
	}
	else if (command.equals(
		__Edit_ConvertSelectedCommandsToComments_String) ) {
		uiAction_ConvertCommandsToComments ( true );
	}
	else if (command.equals(
		__Edit_ConvertSelectedCommandsFromComments_String) ) {
		uiAction_ConvertCommandsToComments ( false );
	}
	// Popup only...
	else if (command.equals(__CommandsPopup_FindCommands_String) ) {
		new FindInJListJDialog(this,ui_GetCommandJList(),"Find Command(s)");
		ui_UpdateStatus ( true );
	}
	else if (command.equals(__CommandsPopup_ShowCommandStatus_String) ) {
		uiAction_ShowCommandStatus();
	}
	else {
		// Chain to next set of actions...
		uiAction_ActionPerformed4_RunMenu ( event );
	}
}

/**
Handle a group of actions for the Run menu.
@param event Event to handle.
*/
private void uiAction_ActionPerformed4_RunMenu (ActionEvent event)
throws Exception
{	String command = event.getActionCommand();
	String routine = getClass().getName() + ".uiAction_ActionPerformed4_RunMenu";

	// Run menu (order in menu)...

    if ( command.equals(__Run_AllCommandsCreateOutput_String) ) {
		// Process time series and create all output from write*
		// commands...
        uiAction_RunCommands ( true, true );
	}
    else if ( command.equals(__Run_AllCommandsIgnoreOutput_String) ) {
		// Process time series but ignore write* commands...
        uiAction_RunCommands ( true, false );
	}
    else if ( command.equals(__Run_SelectedCommandsCreateOutput_String) ) {
		// Process selected commands and create all output from write*
		// commands...
        uiAction_RunCommands ( false, true );
	}
    else if ( command.equals(__Run_SelectedCommandsIgnoreOutput_String) ) {
		// Process selected commands but ignore write* commands...
    	uiAction_RunCommands ( false, false );
	}
	else if (command.equals(__Run_CancelCommandProcessing_String) ) {
		// Cancel the current processor.  This may take awhile to occur.
		ui_UpdateStatusTextFields ( 1, routine, "Processing is bing cancelled...", null, __STATUS_CANCELING );
        ui_UpdateStatus ( true );
        __ts_processor.setCancelProcessingRequested ( true );
	}
    else if ( command.equals(__Run_CommandsFromFile_String) ) {
		// Get the name of the file to run and then execute a TSCommandProcessor as if in batch mode...
    	uiAction_RunCommandFile ();
	}
    else if (command.equals(__Run_ProcessTSProductOutput_String) ) {
		// Test code...
		try {
            uiAction_ProcessTSProductFile ( false );
		}
		catch ( Exception te ) {
			Message.printWarning ( 1, "", "Error processing TSProduct file." );
			Message.printWarning ( 2, "", te );
		}
	}
	else {	// Chain to the next method...
		uiAction_ActionPerformed5_CommandsCreateMenu(event);
	}
}

/**
Handle a group of actions for the Commands...Create... menu.
@param event Event to handle.
*/
private void uiAction_ActionPerformed5_CommandsCreateMenu (ActionEvent event)
throws Exception
{	String command = event.getActionCommand();

	// TODO SAM 2006-05-02
	// Why is all of this needed?  Should rely on the command factory for
	// commands.  Evaluate this when all commands are in the factory.

	if (command.equals( __Commands_Create_CreateFromList_String)){
		commandList_EditCommand ( __Commands_Create_CreateFromList_String, null, __INSERT_COMMAND );
	}
    else if (command.equals( __Commands_Create_ResequenceTimeSeriesData_String) ) {
        commandList_EditCommand ( __Commands_Create_ResequenceTimeSeriesData_String, null, __INSERT_COMMAND );
    }

	// Convert TS Identifier to read command...

	/* FIXME SAM need to enable
	else if ( o == __Commands_ConvertTSIDTo_readTimeSeries_JMenuItem ) {
		commandList_EditCommand ( __Commands_ConvertTSIDTo_readTimeSeries_String, getCommand(), __UPDATE_COMMAND );
	}
	else if ( o == __Commands_ConvertTSIDTo_readDateValue_JMenuItem ) {
		commandList_EditCommand ( __Commands_ConvertTSIDTo_readDateValue_String, getCommand(), __UPDATE_COMMAND );
	}
    else if ( o == __Commands_ConvertTSIDTo_readDateDelimitedFile_JMenuItem ) {
        commandList_EditCommand ( __Commands_ConvertTSIDTo_readDelimitedFile_String, getCommand(), __UPDATE_COMMAND );
    }
	else if ( o == __Commands_ConvertTSIDTo_ReadHydroBase_JMenuItem ) {
		commandList_EditCommand ( __Commands_ConvertTSIDTo_ReadHydroBase_String, getCommand(), __UPDATE_COMMAND );
	}
	else if ( o == __Commands_ConvertTSIDTo_readMODSIM_JMenuItem ) {
		commandList_EditCommand ( __Commands_ConvertTSIDTo_readMODSIM_String, getCommand(), __UPDATE_COMMAND );
	}
	else if ( o == __Commands_ConvertTSIDTo_ReadNwsCard_JMenuItem ) {
		commandList_EditCommand ( __Commands_ConvertTSIDTo_ReadNwsCard_String, getCommand(), __UPDATE_COMMAND );
	}
	else if ( o == __Commands_ConvertTSIDTo_readRiverWare_JMenuItem ) {
		commandList_EditCommand ( __Commands_ConvertTSIDTo_readRiverWare_String, getCommand(), __UPDATE_COMMAND );
	}
	else if ( o == __Commands_ConvertTSIDTo_ReadStateMod_JMenuItem ) {
		commandList_EditCommand ( __Commands_ConvertTSIDTo_ReadStateMod_String, getCommand(), __UPDATE_COMMAND );
	}
	else if ( o == __Commands_ConvertTSIDTo_ReadStateModB_JMenuItem ) {
		commandList_EditCommand ( __Commands_ConvertTSIDTo_ReadStateModB_String, getCommand(), __UPDATE_COMMAND );
	}
	else if ( o == __Commands_ConvertTSIDTo_readUsgsNwis_JMenuItem ) {
		commandList_EditCommand ( __Commands_ConvertTSIDTo_readUsgsNwis_String, getCommand(), __UPDATE_COMMAND );
	}
	*/
	else if (command.equals( __Commands_Create_TS_ChangeInterval_String)){
		commandList_EditCommand ( __Commands_Create_TS_ChangeInterval_String, null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Create_TS_Copy_String)){
		commandList_EditCommand ( __Commands_Create_TS_Copy_String, null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Create_TS_Disaggregate_String)){
		commandList_EditCommand ( __Commands_Create_TS_Disaggregate_String,	null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Create_TS_NewDayTSFromMonthAndDayTS_String)){
		commandList_EditCommand ( __Commands_Create_TS_NewDayTSFromMonthAndDayTS_String, null, __INSERT_COMMAND );
	}
	else if (command.equals(__Commands_Create_TS_NewEndOfMonthTSFromDayTS_String) ) {
		commandList_EditCommand ( __Commands_Create_TS_NewEndOfMonthTSFromDayTS_String, null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Create_TS_NewPatternTimeSeries_String)){
		commandList_EditCommand ( __Commands_Create_TS_NewPatternTimeSeries_String,	null, __INSERT_COMMAND );
	}
	else if (command.equals(__Commands_Create_TS_NewStatisticTimeSeries_String)){
			commandList_EditCommand ( __Commands_Create_TS_NewStatisticTimeSeries_String, null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Create_TS_NewStatisticYearTS_String)){
		commandList_EditCommand ( __Commands_Create_TS_NewStatisticYearTS_String, null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Create_TS_NewTimeSeries_String)){
		commandList_EditCommand ( __Commands_Create_TS_NewTimeSeries_String, null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Create_TS_Normalize_String)){
		commandList_EditCommand ( __Commands_Create_TS_Normalize_String, null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Create_TS_RelativeDiff_String)){
		commandList_EditCommand ( __Commands_Create_TS_RelativeDiff_String,	null, __INSERT_COMMAND );
	}
	else {
		// Chain to next actions...
		uiAction_ActionPerformed6_CommandsReadMenu ( event );
	}
}

/**
Handle a group of actions for the Commands...Read... menu.
@param event Event to handle.
*/
private void uiAction_ActionPerformed6_CommandsReadMenu (ActionEvent event)
throws Exception
{	String command = event.getActionCommand();

	// Read Time Series...

	if (command.equals( __Commands_Read_ReadDateValue_String)){
		commandList_EditCommand ( __Commands_Read_ReadDateValue_String, null, __INSERT_COMMAND );
	}
    else if (command.equals( __Commands_Read_ReadDelimitedFile_String)){
        commandList_EditCommand ( __Commands_Read_ReadDelimitedFile_String, null, __INSERT_COMMAND );
    }
    else if (command.equals( __Commands_Read_ReadHECDSS_String)){
        commandList_EditCommand ( __Commands_Read_ReadHECDSS_String, null, __INSERT_COMMAND );
    }
	else if (command.equals( __Commands_Read_ReadHydroBase_String)){
		commandList_EditCommand ( __Commands_Read_ReadHydroBase_String,	null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Read_ReadMODSIM_String)){
		commandList_EditCommand ( __Commands_Read_ReadMODSIM_String, null, __INSERT_COMMAND );
	}
	else if (command.equals(__Commands_Read_ReadNwsCard_String)){
		commandList_EditCommand ( __Commands_Read_ReadNwsCard_String, null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Read_ReadNWSRFSFS5Files_String)){
		commandList_EditCommand ( __Commands_Read_ReadNWSRFSFS5Files_String, null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Read_ReadStateCU_String)){
		commandList_EditCommand ( __Commands_Read_ReadStateCU_String, null, __INSERT_COMMAND );
	}
    else if (command.equals( __Commands_Read_ReadStateCUB_String)){
        commandList_EditCommand ( __Commands_Read_ReadStateCUB_String, null, __INSERT_COMMAND );
    }
	else if (command.equals( __Commands_Read_ReadStateModB_String)){
		commandList_EditCommand ( __Commands_Read_ReadStateModB_String,	null, __INSERT_COMMAND );
	}
	// Put after longer versions...
	else if (command.equals( __Commands_Read_ReadStateMod_String)){
		commandList_EditCommand ( __Commands_Read_ReadStateMod_String, null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Read_StateModMax_String)){
		commandList_EditCommand ( __Commands_Read_StateModMax_String, null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Read_TS_ReadDateValue_String)){
		commandList_EditCommand ( __Commands_Read_TS_ReadDateValue_String, null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Read_TS_ReadHydroBase_String)){
		commandList_EditCommand ( __Commands_Read_TS_ReadHydroBase_String, null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Read_TS_ReadMODSIM_String)){
		commandList_EditCommand ( __Commands_Read_TS_ReadMODSIM_String, null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Read_TS_ReadNDFD_String)){
		commandList_EditCommand ( __Commands_Read_TS_ReadNDFD_String, null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Read_TS_ReadNwsCard_String)){
		commandList_EditCommand ( __Commands_Read_TS_ReadNwsCard_String, null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Read_TS_ReadNWSRFSFS5Files_String)){
		commandList_EditCommand ( __Commands_Read_TS_ReadNWSRFSFS5Files_String,	null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Read_TS_ReadRiverWare_String)){
		commandList_EditCommand ( __Commands_Read_TS_ReadRiverWare_String, null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Read_TS_ReadStateModB_String)){
		commandList_EditCommand ( __Commands_Read_TS_ReadStateModB_String, null, __INSERT_COMMAND );
	}
	// Put after longer versions...
	else if (command.equals( __Commands_Read_TS_ReadStateMod_String)){
		commandList_EditCommand ( __Commands_Read_TS_ReadStateMod_String, null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Read_TS_ReadUsgsNwis_String)){
		commandList_EditCommand ( __Commands_Read_TS_ReadUsgsNwis_String, null, __INSERT_COMMAND );
	}
	else if (command.equals(__Commands_Read_SetIncludeMissingTS_String)){
		commandList_EditCommand ( __Commands_Read_SetIncludeMissingTS_String, null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Read_SetInputPeriod_String) ) {
		commandList_EditCommand ( __Commands_Read_SetInputPeriod_String, null, __INSERT_COMMAND );
	}
	else {
		// Chain to next menus
		uiAction_ActionPerformed7_CommandsFillMenu ( event );
	}
}

/**
Handle a group of actions for the Commands...Fill... menu.
@param event Event to handle.
*/
private void uiAction_ActionPerformed7_CommandsFillMenu (ActionEvent event)
throws Exception
{	String command = event.getActionCommand();

    if (command.equals( __Commands_Fill_FillConstant_String)){
		commandList_EditCommand ( __Commands_Fill_FillConstant_String, null, __INSERT_COMMAND );
	}
	else if (command.equals(__Commands_Fill_FillDayTSFrom2MonthTSAnd1DayTS_String)){
		commandList_EditCommand ( __Commands_Fill_FillDayTSFrom2MonthTSAnd1DayTS_String, null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Fill_FillFromTS_String)){
		commandList_EditCommand ( __Commands_Fill_FillFromTS_String, null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Fill_FillHistMonthAverage_String)){
		commandList_EditCommand ( __Commands_Fill_FillHistMonthAverage_String, null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Fill_FillHistYearAverage_String)){
		commandList_EditCommand ( __Commands_Fill_FillHistYearAverage_String, null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Fill_FillInterpolate_String)){
		commandList_EditCommand ( __Commands_Fill_FillInterpolate_String, null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Fill_FillMixedStation_String)){
		commandList_EditCommand ( __Commands_Fill_FillMixedStation_String, null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Fill_FillMOVE1_String)){
		commandList_EditCommand ( __Commands_Fill_FillMOVE1_String,	null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Fill_FillMOVE2_String)){
		commandList_EditCommand ( __Commands_Fill_FillMOVE2_String,	null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Fill_FillPattern_String) ) {
		commandList_EditCommand ( __Commands_Fill_FillPattern_String, null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Fill_FillProrate_String) ) {
		commandList_EditCommand ( __Commands_Fill_FillProrate_String, null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Fill_FillRegression_String) ) {
		commandList_EditCommand ( __Commands_Fill_FillRegression_String, null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Fill_FillRepeat_String) ) {
		commandList_EditCommand ( __Commands_Fill_FillRepeat_String, null, __INSERT_COMMAND );
	}
	else if(command.equals(	__Commands_Fill_FillUsingDiversionComments_String)){
		commandList_EditCommand (__Commands_Fill_FillUsingDiversionComments_String, null, __INSERT_COMMAND );
	}
	else if (command.equals(__Commands_Fill_SetAutoExtendPeriod_String)){
		commandList_EditCommand ( __Commands_Fill_SetAutoExtendPeriod_String, null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Fill_SetAveragePeriod_String) ) {
		commandList_EditCommand ( __Commands_Fill_SetAveragePeriod_String, null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Fill_SetIgnoreLEZero_String) ) {
		commandList_EditCommand ( __Commands_Fill_SetIgnoreLEZero_String, null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Fill_SetPatternFile_String) ) {
		commandList_EditCommand ( __Commands_Fill_SetPatternFile_String, null, __INSERT_COMMAND );
	}
	else {
		// Chain to other menus
		uiAction_ActionPerformed8_CommandsSetMenu ( event );
	}
}

/**
Handle a group of actions for the Commands...Set... menu.
@param event Event to handle.
*/
private void uiAction_ActionPerformed8_CommandsSetMenu (ActionEvent event)
throws Exception
{	String command = event.getActionCommand();

	if (command.equals( __Commands_Set_ReplaceValue_String)){
		commandList_EditCommand ( __Commands_Set_ReplaceValue_String, null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Set_SetConstant_String)){
		commandList_EditCommand ( __Commands_Set_SetConstant_String, null, __INSERT_COMMAND );
    }
	else if (command.equals( __Commands_Set_SetDataValue_String)){
		commandList_EditCommand ( __Commands_Set_SetDataValue_String, null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Set_SetFromTS_String)){
		commandList_EditCommand ( __Commands_Set_SetFromTS_String, null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Set_SetMax_String)){
		commandList_EditCommand ( __Commands_Set_SetMax_String, null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Set_SetToMin_String)){
		commandList_EditCommand ( __Commands_Set_SetToMin_String, null, __INSERT_COMMAND );
	}
    else if (command.equals( __Commands_Set_SetTimeSeriesProperty_String)){
        commandList_EditCommand ( __Commands_Set_SetTimeSeriesProperty_String, null, __INSERT_COMMAND );
    }
	else {
		uiAction_ActionPerformed9_CommandsManipulateMenu ( event );
	}
}

/**
Handle a group of actions for the Commands...Manipulate... menu.
@param event Event to handle.
*/
private void uiAction_ActionPerformed9_CommandsManipulateMenu (ActionEvent event)
throws Exception
{	String command = event.getActionCommand();

	if (command.equals( __Commands_Manipulate_Add_String)){
		commandList_EditCommand ( __Commands_Manipulate_Add_String,	null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Manipulate_AddConstant_String)){
		commandList_EditCommand ( __Commands_Manipulate_AddConstant_String, null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Manipulate_AdjustExtremes_String)){
		commandList_EditCommand ( __Commands_Manipulate_AdjustExtremes_String, null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Manipulate_ARMA_String)){
		commandList_EditCommand ( __Commands_Manipulate_ARMA_String, null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Manipulate_Blend_String)){
		commandList_EditCommand ( __Commands_Manipulate_Blend_String, null, __INSERT_COMMAND );
	}
    else if (command.equals(__Commands_Manipulate_ChangePeriod_String)){
        commandList_EditCommand ( __Commands_Manipulate_ChangePeriod_String, null, __INSERT_COMMAND );
    }
	else if (command.equals(__Commands_Manipulate_ConvertDataUnits_String)){
		commandList_EditCommand ( __Commands_Manipulate_ConvertDataUnits_String, null, __INSERT_COMMAND );
	}
	else if (command.equals(__Commands_Manipulate_Cumulate_String) ) {
		commandList_EditCommand ( __Commands_Manipulate_Cumulate_String, null, __INSERT_COMMAND );
	}
	else if (command.equals(__Commands_Manipulate_Divide_String) ) {
		commandList_EditCommand ( __Commands_Manipulate_Divide_String, null, __INSERT_COMMAND );
	}
	else if (command.equals(__Commands_Manipulate_Free_String) ) {
		commandList_EditCommand ( __Commands_Manipulate_Free_String, null, __INSERT_COMMAND );
	}
	else if (command.equals(__Commands_Manipulate_Multiply_String) ) {
		commandList_EditCommand ( __Commands_Manipulate_Multiply_String, null, __INSERT_COMMAND );
	}
	else if (command.equals(__Commands_Manipulate_RunningAverage_String) ) {
		commandList_EditCommand ( __Commands_Manipulate_RunningAverage_String, null, __INSERT_COMMAND );
	}
	else if (command.equals(__Commands_Manipulate_Scale_String) ) {
		commandList_EditCommand ( __Commands_Manipulate_Scale_String, null, __INSERT_COMMAND );
	}
	else if (command.equals(__Commands_Manipulate_ShiftTimeByInterval_String) ) {
		commandList_EditCommand ( __Commands_Manipulate_ShiftTimeByInterval_String, null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Manipulate_Subtract_String)){
		commandList_EditCommand ( __Commands_Manipulate_Subtract_String, null, __INSERT_COMMAND );
	}
	else {
		// Chain to next actions
		uiAction_ActionPerformed10_CommandsAnalyzeMenu ( event );
	}
}

/**
Handle a group of actions for the Commands...Analyze... menu.
@param event Event to handle.
*/
private void uiAction_ActionPerformed10_CommandsAnalyzeMenu (ActionEvent event)
throws Exception
{	String command = event.getActionCommand();

	if (command.equals( __Commands_Analyze_AnalyzePattern_String)){
		commandList_EditCommand ( __Commands_Analyze_AnalyzePattern_String,	null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Analyze_CompareTimeSeries_String)){
		commandList_EditCommand ( __Commands_Analyze_CompareTimeSeries_String, null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Analyze_ComputeErrorTimeSeries_String)){
        commandList_EditCommand ( __Commands_Analyze_ComputeErrorTimeSeries_String, null, __INSERT_COMMAND );
    }
	else if (command.equals( __Commands_Analyze_NewDataTest_String)){
		commandList_EditCommand ( __Commands_Analyze_NewDataTest_String, null, __INSERT_COMMAND );
	}
	else if (command.equals(__Commands_Analyze_ReadDataTestFromRiversideDB_String)){
		commandList_EditCommand ( __Commands_Analyze_ReadDataTestFromRiversideDB_String, null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Analyze_RunDataTests_String)){
		commandList_EditCommand ( __Commands_Analyze_RunDataTests_String, null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Analyze_ProcessDataTestResults_String)){
		commandList_EditCommand ( __Commands_Analyze_ProcessDataTestResults_String, null, __INSERT_COMMAND );
	}
	else {
		// Chain to other actions
		uiAction_ActionPerformed11_CommandsModelsMenu ( event );
	}
}

/**
Handle a group of actions for the Commands...Models... menu.
@param event Event to handle.
*/
private void uiAction_ActionPerformed11_CommandsModelsMenu (ActionEvent event)
throws Exception
{	String command = event.getActionCommand();

	if (command.equals( __Commands_Models_LagK_String)){
		commandList_EditCommand ( __Commands_Models_LagK_String, null, __INSERT_COMMAND );
	}
	else {
		// Chain to other actions
		uiAction_ActionPerformed12_CommandsEnsembleMenu ( event );
	}
}

/**
Handle a group of actions for the Commands...Ensemble... menu.
@param event Event to handle.
*/
private void uiAction_ActionPerformed12_CommandsEnsembleMenu (ActionEvent event)
throws Exception
{   String command = event.getActionCommand();

    if (command.equals( __Commands_Ensemble_CreateEnsemble_String)){
        commandList_EditCommand ( __Commands_Ensemble_CreateEnsemble_String, null, __INSERT_COMMAND );
    }
    if (command.equals( __Commands_Ensemble_CopyEnsemble_String)){
        commandList_EditCommand ( __Commands_Ensemble_CopyEnsemble_String, null, __INSERT_COMMAND );
    }
    else if (command.equals( __Commands_Ensemble_ReadNwsrfsEspTraceEnsemble_String)){
        commandList_EditCommand ( __Commands_Ensemble_ReadNwsrfsEspTraceEnsemble_String, null, __INSERT_COMMAND );
    }
    else if (command.equals(__Commands_Ensemble_TS_NewStatisticTimeSeriesFromEnsemble_String)){
        commandList_EditCommand ( __Commands_Ensemble_TS_NewStatisticTimeSeriesFromEnsemble_String, null, __INSERT_COMMAND );
    }
    else if (command.equals( __Commands_Ensemble_TS_WeightTraces_String)){
        commandList_EditCommand ( __Commands_Ensemble_TS_WeightTraces_String, null, __INSERT_COMMAND );
    }
    else if(command.equals( __Commands_Ensemble_WriteNWSRFSESPTraceEnsemble_String)){
        commandList_EditCommand ( __Commands_Ensemble_WriteNWSRFSESPTraceEnsemble_String, null, __INSERT_COMMAND );
    }
    else {
        // Chain to other actions
        uiAction_ActionPerformed13_CommandsOutputMenu ( event );
    }
}

/**
Handle a group of actions for the Commands...Output... menu.
@param event Event to handle.
*/
private void uiAction_ActionPerformed13_CommandsOutputMenu (ActionEvent event)
throws Exception
{	String command = event.getActionCommand();

	if (command.equals( __Commands_Output_DeselectTimeSeries_String)){
		commandList_EditCommand ( __Commands_Output_DeselectTimeSeries_String, null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Output_SelectTimeSeries_String)){
		commandList_EditCommand ( __Commands_Output_SelectTimeSeries_String, null, __INSERT_COMMAND );
	}
	else if (command.equals(
		__Commands_Output_SetOutputDetailedHeaders_String) ) {
		commandList_EditCommand (__Commands_Output_SetOutputDetailedHeaders_String, null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Output_SetOutputPeriod_String) ) {
		commandList_EditCommand ( __Commands_Output_SetOutputPeriod_String, null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Output_SetOutputYearType_String) ){
		commandList_EditCommand ( __Commands_Output_SetOutputYearType_String, null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Output_SortTimeSeries_String)){
		commandList_EditCommand ( __Commands_Output_SortTimeSeries_String, null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Output_WriteDateValue_String)){
		commandList_EditCommand ( __Commands_Output_WriteDateValue_String, null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Output_WriteNwsCard_String)){
		commandList_EditCommand ( __Commands_Output_WriteNwsCard_String, null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Output_WriteRiverWare_String)){
		commandList_EditCommand ( __Commands_Output_WriteRiverWare_String, null, __INSERT_COMMAND );
	}
    else if (command.equals( __Commands_Output_WriteSHEF_String)){
        commandList_EditCommand ( __Commands_Output_WriteSHEF_String, null, __INSERT_COMMAND );
    }
	else if (command.equals( __Commands_Output_WriteStateCU_String)){
		commandList_EditCommand ( __Commands_Output_WriteStateCU_String, null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Output_WriteStateMod_String)){
		commandList_EditCommand ( __Commands_Output_WriteStateMod_String, null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Output_WriteSummary_String)){
		commandList_EditCommand ( __Commands_Output_WriteSummary_String, null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_Output_ProcessTSProduct_String)){
		commandList_EditCommand ( __Commands_Output_ProcessTSProduct_String, null, __INSERT_COMMAND );
	}
	else {	// Chain to next list of commands...
		uiAction_ActionPerformed14_CommandsGeneralMenu ( event );
	}
}

/**
Handle a group of actions for the Commands...General... menu.
Also include a couple of special open commands for input types.
@param event Event to handle.
*/
private void uiAction_ActionPerformed14_CommandsGeneralMenu (ActionEvent event)
throws Exception
{	String command = event.getActionCommand();

	// HydroBase commands...

	if (command.equals(__Commands_HydroBase_OpenHydroBase_String)){
		commandList_EditCommand ( __Commands_HydroBase_OpenHydroBase_String, null, __INSERT_COMMAND );
	}

	// NDFD commands...

	else if (command.equals(__Commands_NDFD_openNDFD_String)){
		commandList_EditCommand ( __Commands_NDFD_openNDFD_String, null, __INSERT_COMMAND );
	}
    
    // Table commands...
    
    else if (command.equals( __Commands_Table_ReadTableFromDelimitedFile_String) ) {
        commandList_EditCommand ( __Commands_Table_ReadTableFromDelimitedFile_String, null, __INSERT_COMMAND );
    }

	// General commands...

	else if (command.equals( __Commands_General_Logging_StartLog_String) ) {
		commandList_EditCommand ( __Commands_General_Logging_StartLog_String, null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_General_Logging_SetDebugLevel_String) ) {
		commandList_EditCommand ( __Commands_General_Logging_SetDebugLevel_String, null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_General_Logging_SetWarningLevel_String) ) {
		commandList_EditCommand ( __Commands_General_Logging_SetWarningLevel_String, null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_General_Running_SetWorkingDir_String) ) {
		commandList_EditCommand ( __Commands_General_Running_SetWorkingDir_String, null, __INSERT_COMMAND );
	}
    else if (command.equals(__Commands_General_CheckingResults_OpenCheckFile_String) ) {
        commandList_EditCommand ( __Commands_General_CheckingResults_OpenCheckFile_String, null, __INSERT_COMMAND );
    }
	else if (command.equals(__Commands_General_Comments_Comment_String) ) {
		commandList_EditCommand ( __Commands_General_Comments_Comment_String, null, __INSERT_COMMAND );
	}
    else if (command.equals(__Commands_General_Comments_ReadOnlyComment_String) ) {
        commandList_EditCommand ( __Commands_General_Comments_ReadOnlyComment_String, null, __INSERT_COMMAND );
    }
	else if (command.equals(__Commands_General_Comments_StartComment_String) ) {
		commandList_EditCommand ( __Commands_General_Comments_StartComment_String, null, __INSERT_COMMAND );
	}
	else if (command.equals(__Commands_General_Comments_EndComment_String) ) {
		commandList_EditCommand ( __Commands_General_Comments_EndComment_String,	null, __INSERT_COMMAND );
	}
	else if (command.equals(__Commands_General_Running_Exit_String) ) {
		commandList_EditCommand ( __Commands_General_Running_Exit_String, null, __INSERT_COMMAND );
	}
    else if (command.equals( __Commands_General_TestProcessing_StartRegressionTestResultsReport_String) ) {
        commandList_EditCommand ( __Commands_General_TestProcessing_StartRegressionTestResultsReport_String, null, __INSERT_COMMAND );
    }
    else if (command.equals( __Commands_General_Running_SetProperty_String) ) {
        commandList_EditCommand ( __Commands_General_Running_SetProperty_String, null, __INSERT_COMMAND );
    }
    else if (command.equals( __Commands_General_Running_SetPropertyFromNwsrfsAppDefault_String) ) {
        commandList_EditCommand ( __Commands_General_Running_SetPropertyFromNwsrfsAppDefault_String, null, __INSERT_COMMAND );
    }
	else if (command.equals( __Commands_General_Running_RunCommands_String) ) {
		commandList_EditCommand ( __Commands_General_Running_RunCommands_String, null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_General_Running_RunProgram_String) ) {
		commandList_EditCommand ( __Commands_General_Running_RunProgram_String, null, __INSERT_COMMAND );
	}
    else if (command.equals( __Commands_General_Running_RunPython_String) ) {
        commandList_EditCommand ( __Commands_General_Running_RunPython_String, null, __INSERT_COMMAND );
    }
    else if (command.equals( __Commands_General_FileHandling_FTPGet_String)){
        commandList_EditCommand ( __Commands_General_FileHandling_FTPGet_String, null, __INSERT_COMMAND );
    }
    else if (command.equals( __Commands_General_FileHandling_RemoveFile_String)){
        commandList_EditCommand ( __Commands_General_FileHandling_RemoveFile_String, null, __INSERT_COMMAND );
    }
	else if (command.equals( __Commands_General_TestProcessing_CompareFiles_String)){
		commandList_EditCommand ( __Commands_General_TestProcessing_CompareFiles_String, null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_General_TestProcessing_WriteProperty_String)){
		commandList_EditCommand ( __Commands_General_TestProcessing_WriteProperty_String, null, __INSERT_COMMAND );
	}
    else if (command.equals( __Commands_General_TestProcessing_WriteTimeSeriesProperty_String)){
        commandList_EditCommand ( __Commands_General_TestProcessing_WriteTimeSeriesProperty_String, null, __INSERT_COMMAND );
    }
	else if (command.equals( __Commands_General_TestProcessing_TestCommand_String) ) {
		commandList_EditCommand ( __Commands_General_TestProcessing_TestCommand_String, null, __INSERT_COMMAND );
	}
	else if (command.equals( __Commands_General_TestProcessing_CreateRegressionTestCommandFile_String) ) {
		commandList_EditCommand ( __Commands_General_TestProcessing_CreateRegressionTestCommandFile_String, null, __INSERT_COMMAND );
	}
	else {
		// Chain to other actions...
		uiAction_ActionPerformed15_ResultsMenu ( event );
	}
}

/**
Handle a group of actions for the View menu.
@param event Event to handle.
*/
private void uiAction_ActionPerformed15_ResultsMenu (ActionEvent event)
throws Exception
{	String command = event.getActionCommand();

    if ( command.equals(__Results_Graph_AnnualTraces_String) ) {
		// Only do if data are selected...
		String response = new TextResponseJDialog ( 
			this, "Graph Annual Traces", "Enter the 4-digit year to use as a reference.",
			ResponseJDialog.OK|ResponseJDialog.CANCEL ).response();
		if ( response == null ) {
			return;
		}
		uiAction_GraphTimeSeriesResults("-oannual_traces_graph " + StringUtil.atoi(response) );
	}
    else if ( command.equals(__Results_Graph_BarsLeft_String) ) {
    	uiAction_GraphTimeSeriesResults("-obar_graph", "BarsLeftOfDate");
	}
    else if ( command.equals(__Results_Graph_BarsCenter_String) ) {
    	uiAction_GraphTimeSeriesResults("-obar_graph", "BarsCenteredOnDate");
	}
    else if ( command.equals(__Results_Graph_BarsRight_String) ) {
    	uiAction_GraphTimeSeriesResults("-obar_graph", "BarsRightOfDate");
	}
    else if ( command.equals(__Results_Graph_DoubleMass_String) ) {
    	uiAction_GraphTimeSeriesResults("-odoublemassgraph");
	}
    else if ( command.equals(__Results_Graph_Duration_String) ) {
		// Only do if data are selected...
    	uiAction_GraphTimeSeriesResults("-oduration_graph " );
	}
    else if ( command.equals(__Results_Graph_Line_String) ) {
    	uiAction_GraphTimeSeriesResults("-olinegraph");
	}
    else if ( command.equals(__Results_Graph_LineLogY_String) ) {
        uiAction_GraphTimeSeriesResults("-olinelogygraph");
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
		uiAction_GraphTimeSeriesResults("-oporgraph");
	}
    else if ( command.equals(__Results_Graph_Point_String) ) {
    	uiAction_GraphTimeSeriesResults("-opointgraph");
	}
    else if ( command.equals(__Results_Graph_PredictedValue_String) ) {
    	uiAction_GraphTimeSeriesResults("-oPredictedValue_graph" );
	}
    else if (command.equals(__Results_Graph_PredictedValueResidual_String)){
    	uiAction_GraphTimeSeriesResults("-oPredictedValueResidual_graph" );
	}
    else if ( command.equals(__Results_Graph_XYScatter_String) ) {
    	uiAction_GraphTimeSeriesResults("-oxyscatter_graph" );
	}
    else if ( command.equals(__Results_Table_String) ) {
		// For now handle similar to the summary report, forcing a preview...
    	uiAction_ExportTimeSeriesResults("-otable", "-preview" );
	}
    else if ( command.equals(__Results_Report_Summary_String) ) {
		// SAM.  Let the ReportJFrame prompt for this.  This is
		// different from the StateMod output in that when a summary is
		// exported in the GUI, the user views first and then saves to disk.
		uiAction_ExportTimeSeriesResults("-osummary", "-preview" );
	}

	// Only on View pop-up...

	else if (command.equals(__Results_FindTimeSeries_String) ) {
		new FindInJListJDialog ( this, false, __results_ts_JList, "Find Time Series" );
	}
    else if ( command.equals(__Results_TimeSeriesProperties_String) ) {
		// Get the first time series selected in the view window...
		if ( __ts_processor != null ) {
			int pos = 0;
			if ( JGUIUtil.selectedSize(__results_ts_JList) == 0 ) {
				pos = 0;
			}
			else {
                pos = JGUIUtil.selectedIndex(__results_ts_JList,0);
			}
			// Now display the properties...
			if ( pos >= 0 ) {
				new TSPropertiesJFrame ( this, commandProcessor_GetTimeSeries(pos) );
			}
		}
	}
    else {
    	// Chain to next actions...
    	uiAction_ActionPerformed16_ToolsMenu ( event );
    }
}

/**
Handle a group of actions for the Tools menu.
@param event Event to handle.
*/
private void uiAction_ActionPerformed16_ToolsMenu (ActionEvent event)
throws Exception
{	Object o = event.getSource();
	String command = event.getActionCommand();
	String routine = "ToolsMenu";

	if ( o == __Tools_Analysis_MixedStationAnalysis_JMenuItem ) {
		// Create the dialog using the available time series...
		/* FIXME SAM 2007-08-09 need to keep main commands processor
		 separate from this instance.  Determine how TSEngine is used.
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
		*/
	}
	else if (command.equals(__Tools_Report_DataCoverageByYear_String) ) {
		// Use graph TS because it does not take an output file...
		uiAction_GraphTimeSeriesResults ( "-odata_coverage_report " );
	}
	else if (command.equals(__Tools_Report_DataLimitsSummary_String) ) {
		// Use graph TS because it does not take an output file...
		uiAction_GraphTimeSeriesResults ( "-odata_limits_report " );
	}
	else if (command.equals(__Tools_Report_MonthSummaryDailyMeans_String)) {
		// Use graph TS because it does not take an output file...
		uiAction_GraphTimeSeriesResults ( "-omonth_mean_summary_report " );
	}
	else if (command.equals(__Tools_Report_MonthSummaryDailyTotals_String)){
		// Use graph TS because it does not take an output file...
		uiAction_GraphTimeSeriesResults ( "-omonth_total_summary_report " );
	}
	else if (command.equals(__Tools_Report_YearToDateTotal_String) ) {
		// Set the averaging end date...
		PropList props = new PropList( "DateBuilder properties" );
		props.set ( "DatePrecision", "Day" );
		props.set ( "DateFormat", "US" );
		props.set ( "EnableYear", "false" );
		props.set ( "DateLabel", "Enter Ending Date for Annual Total:" );
		props.set ( "Title", "Enter Ending Date for Annual Total" );
		DateTime to = new DateTime ( DateTime.DATE_CURRENT | DateTime.PRECISION_DAY );
		try {
            JTextField tmpJTextField = new JTextField();
			new DateTimeBuilderJDialog ( this, tmpJTextField, to, props );
			to = DateTime.parse ( tmpJTextField.getText() ); 
		}
		catch ( Exception ex ) {
			Message.printWarning ( 1, "TSTool_JFrame", "There was an error processing the date for the report." );
		}
		// Use graph TS because it does not take an output file...
		uiAction_GraphTimeSeriesResults ( "-oyear_to_date_report ",	to.toString(DateTime.FORMAT_MM_SLASH_DD) );
	}
	else if ( o == __Tools_NWSRFS_ConvertNWSRFSESPTraceEnsemble_JMenuItem ){
		// Prompt for the ESP file...
		JFileChooser fc = JFileChooserFactory.createJFileChooser ( JGUIUtil.getLastFileDialogDirectory() );
		fc.setDialogTitle ( "Select NWSRFS ESP Trace Ensemble File" );
		SimpleFileFilter sff = new SimpleFileFilter ( "CS",	"Conditional Simulation Ensemble File" );
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
		sff = new SimpleFileFilter ( "txt","NWSRFS ESP Ensemble File as Text" );
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
			this, "Specify Output Units", "Specify the output units for the file (blank to default).",
			ResponseJDialog.OK| ResponseJDialog.CANCEL ).response();
		if ( units == null ) {
			return;
		}
		units = units.trim();
		// Convert...
		try {
            NWSRFS_ESPTraceEnsemble.convertESPTraceEnsembleToText (
			esp_file, IOUtil.enforceFileExtension(out_file,"txt"), units );
		}
		catch ( Exception e ) {
			Message.printWarning ( 1, "TSTool_JFrame",
			"There was an error converting the ESP trace ensemble file to text.  Partial results may be available." );
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
		try {
            uiAction_SelectOnMap ();
		}
		catch ( Exception e ) {
			JGUIUtil.setWaitCursor ( this, false );
			Message.printWarning ( 1, routine, "Unable to select locations on map." );
			Message.printWarning ( 3, routine, e );
		}
	}
	else if ( o == __Tools_Options_JMenuItem ) {
		new TSTool_Options_JDialog ( this, __props );
		// Reset as necessary...
		if ( __query_TableModel instanceof TSTool_HydroBase_TableModel){
			((TSTool_HydroBase_TableModel)__query_TableModel).
			setWDIDLength(StringUtil.atoi( __props.getValue("HydroBase.WDIDLength")));
		}
		else if ( __query_TableModel instanceof TSTool_HydroBase_WellLevel_Day_TableModel){
			((TSTool_HydroBase_WellLevel_Day_TableModel)
			__query_TableModel).setWDIDLength(StringUtil.atoi(__props.getValue("HydroBase.WDIDLength")));
		}		
	}
	else {
		// Chain to remaining actions...
		uiAction_ActionPerformed17_HelpMenu ( event );
	}
}

/**
Handle a group of actions for the Tools menu.
@param event Event to handle.
*/
private void uiAction_ActionPerformed17_HelpMenu (ActionEvent event)
throws Exception
{	//Object o = event.getSource();
	String command = event.getActionCommand();

	// Help menu (order of GUI)...

	if ( command.equals ( __Help_AboutTSTool_String )) {
		uiAction_ShowHelpAbout ( license_GetLicenseManager() );
	}
}

/**
Convert selected commands to/from comments.  When converting to commands:
Each Command instance is retrieved, its command string is taken from the Command, and
a new GenericCommand is create, and the original Command is replaced in the list.
When converting from commands, the GenericCommand is retrieved, a new Command instance
is created, the original Command is replaced.
@param to_comment If true, convert commands to comments, if false, from
comments.
*/
private void uiAction_ConvertCommandsToComments ( boolean to_comment )
{	int selected_indexes[] = ui_GetCommandJList().getSelectedIndices();
	int selected_size = JGUIUtil.selectedSize ( ui_GetCommandJList() );
	String old_command_string = null;
	Command old_command = null;
	Command new_command = null;
	// It is OK to loop through each item below.  Even though the items in
	// the data model will change, if a command is replaced each time, the
	// indices will still be relevant.
	for ( int i = 0; i < selected_size; i++ ) {
		old_command = (Command)__commands_JListModel.get(selected_indexes[i]);
		old_command_string = (String)old_command.toString();
		if ( to_comment ) {
			// Replace the current command with a new string that has the comment character...
			new_command = commandList_NewCommand(
					"# " + old_command_string,	// New command as comment
					true );	// Create the command even if not recognized.
			commandList_ReplaceCommand ( old_command, new_command );
		}
		else {	// Remove comment...
			if ( old_command_string.startsWith("#") ) {
				new_command = commandList_NewCommand(
					old_command_string.substring(1).trim(),	// New command as comment
					true );	// Create the command even if not recognized.
				commandList_ReplaceCommand ( old_command, new_command );
			}
		}
	}
	// Mark the commands as dirty...
	if ( selected_size > 0 ) {
		commandList_SetDirty ( true );
	}
}

/**
Get the selected commands from the commands list, clone a copy, and save in the cut buffer.
The commands can then be pasted into the command list with uiAction_PasteFromCutBufferToCommandList.
@param remove_original If true, then this is a Cut operation and the original
commands should be removed from the list.  If false, a copy is made but the original
commands will remain in the list.
*/
private void uiAction_CopyFromCommandListToCutBuffer ( boolean remove_original )
{	int size = 0;
	int [] selected_indices = ui_GetCommandJList().getSelectedIndices();
	if ( selected_indices != null ) {
		size = selected_indices.length;
	}
	if ( size == 0 ) {
		return;
	}

	// Clear what may previously have been in the cut buffer...
	__commands_cut_buffer.removeAllElements();

	// Transfer Command instances to the cut buffer...
	Command command = null;	// Command instance to process
	for ( int i = 0; i < size; i++ ) {
		command = (Command)__commands_JListModel.get(selected_indices[i]);
		__commands_cut_buffer.addElement ( (Command)command.clone() );
	}
	
	if ( remove_original ) {
		// If removing, delete the selected commands from the list...
		commandList_RemoveCommandsBasedOnUI();
	}
}

/**
This function fills in appropriate selections for the time step and choices.
This also calls timestepChoiceClicked() to set the data type modifier.
*/
private void uiAction_DataTypeChoiceClicked()
{	String rtn = "TSTool_JFrame.dataTypeChoiceClicked";
	if ( (__input_type_JComboBox == null) || (__data_type_JComboBox == null) ) {
		if ( Message.isDebugOn ) {
			Message.printDebug ( 1, rtn, "Data type has been selected but GUI is not initialized." );
		}
		return;	// Not done initializing.
	}
	__selected_data_type_full = __data_type_JComboBox.getSelected();
	if ( __selected_data_type_full == null ) {
		// Apparently this happens when setData() or similar is called
		// on the JComboBox, and before select() is called.
		if ( Message.isDebugOn ) {
			Message.printDebug ( 1, rtn, "Data type has been selected:  null (select is ignored)" );
		}
		return;
	}
	// For some input types, data types have additional label-only information...
	if ( __selected_data_type_full.indexOf('-') >= 0 ) {
		if ( __selected_input_type.equals(__INPUT_TYPE_HydroBase) ||
		        __selected_input_type.equals(__INPUT_TYPE_StateCUB) ||
			__selected_input_type.equals(__INPUT_TYPE_StateModB)) {
			// Data type group is first and data type second...
			__selected_data_type = StringUtil.getToken(	__selected_data_type_full, "-", 0, 1).trim();
		}
		else {
            // Data type is first and explanation second...
			__selected_data_type = StringUtil.getToken(	__selected_data_type_full, "-", 0, 0).trim();
		}
	}
	else {
        __selected_data_type = __selected_data_type_full;
	}
	if ( Message.isDebugOn ) {
		Message.printDebug ( 1, rtn, "Data type has been selected:  \"" + __selected_data_type + "\"" );
	}

	// Set the appropriate settings for the current data input type and data type...

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
		if ( __selected_data_type.endsWith("DataValue") && group.equalsIgnoreCase("Rain") ) {
			__time_step_JComboBox.add ( __TIMESTEP_DAY );
			__time_step_JComboBox.add ( __TIMESTEP_HOUR );
		}
		__time_step_JComboBox.add ( __TIMESTEP_IRREGULAR );
		if ( __selected_data_type.endsWith("DataValue") && group.equalsIgnoreCase("Rain") ) {
			try {
                DIADvisor_SysConfig config = __DIADvisor_dmi.readSysConfig();
				__time_step_JComboBox.add ( "" + config.getInterval() +	__TIMESTEP_MINUTE );
			}
			catch ( Exception e ) {
				Message.printWarning ( 2, rtn, "Could not determine DIADvisor interval for time step choice." );
				Message.printWarning ( 2, rtn, e );
			}
		}
		__time_step_JComboBox.select ( null );
		__time_step_JComboBox.select ( 0 );
	}
    else if ( __selected_input_type.equals(__INPUT_TYPE_HECDSS) ) {
        // HEC-DSS database file - the time step is set when the
        // input name is selected so do nothing here.
    }
	else if ( __selected_input_type.equals(__INPUT_TYPE_HydroBase) ) {
		Vector time_steps =	HydroBase_Util.getTimeSeriesTimeSteps (__hbdmi,
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
		if ( JGUIUtil.isSimpleJComboBoxItem(__time_step_JComboBox,"Month", JGUIUtil.NONE, null, null ) ) {
			__time_step_JComboBox.select ( "Month" );
		}
		else {	// Select the first item...
			try {
                __time_step_JComboBox.select ( 0 );
			}
			catch ( Exception e ) {
				// For cases when for some reason no choice is available.
				__time_step_JComboBox.setEnabled ( false );
			}
		}
		// If the data type is for a diversion or reservoir data type,
		// hide the abbreviation column in the table model.  Else show the column.
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
		Vector time_steps = NWSRFS_Util.getDataTypeIntervals ( __nwsrfs_dmi, __selected_data_type );
		__time_step_JComboBox.setData ( time_steps );
		__time_step_JComboBox.select ( null );
		__time_step_JComboBox.setEnabled ( true );
		// Select 6Hour as the default if available...
		if ( JGUIUtil.isSimpleJComboBoxItem(__time_step_JComboBox,"6Hour", JGUIUtil.NONE, null, null ) ) {
			__time_step_JComboBox.select ( "6Hour" );
		}
		else {
            // Select the first item...
			try {
                __time_step_JComboBox.select ( 0 );
			}
			catch ( Exception e ) {
				// For cases when for some reason no choice is available.
				__time_step_JComboBox.setEnabled ( false );
			}
		}
	}
	else if ( __selected_input_type.equals( __INPUT_TYPE_NWSRFS_ESPTraceEnsemble) ){
		// ESP Trace Ensemble file...
		__time_step_JComboBox.removeAll ();
		__time_step_JComboBox.add ( __TIMESTEP_AUTO );
		__time_step_JComboBox.select ( null );
		__time_step_JComboBox.select ( __TIMESTEP_AUTO );
		__time_step_JComboBox.setEnabled ( false );
	}
	else if ( __selected_input_type.equals(__INPUT_TYPE_RiversideDB) ) {
		// Time steps are determined from the database based on the data type that is selected...
		String data_type = StringUtil.getToken(__data_type_JComboBox.getSelected()," ",0,0).trim();
		Vector v = null;
		try {
            v = __rdmi.readMeasTypeListForTSIdent (	".." + data_type + ".." );
		}
		catch ( Exception e ) {
			Message.printWarning(2, rtn, "Error getting time steps from RiversideDB.");
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
				// Only add if not already listed. Alternatively - add a "distinct" query
				time_step_base = mt.getTime_step_base();
				time_step_mult = mt.getTime_step_mult();
				if ( time_step_base.equalsIgnoreCase( "IRREGULAR") || DMIUtil.isMissing(time_step_mult) ) {
					timestep = mt.getTime_step_base();
				}
				else {
                    timestep = "" + mt.getTime_step_mult() + mt.getTime_step_base();
				}
				if ( !JGUIUtil.isSimpleJComboBoxItem(__time_step_JComboBox, timestep, JGUIUtil.NONE, null, null)){
					__time_step_JComboBox.add(timestep);
				}
			}
			__time_step_JComboBox.select ( null );
			__time_step_JComboBox.select ( 0 );
			__time_step_JComboBox.setEnabled ( true );
		}
		else {
            __time_step_JComboBox.setEnabled ( false );
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
    else if ( __selected_input_type.equals(__INPUT_TYPE_StateCUB) ) {
        // StateCU binary output file - the time step is set when the
        // input name is selected so do nothing here.
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

	ui_SetInputFilters();
}

/**
Deselect all commands in the commands list.  This occurs in response to a user
selecting a menu choice.
*/
private void uiAction_DeselectAllCommands()
{	ui_GetCommandJList().clearSelection();
	// TODO SAM 2007-08-31 Should add list seletion listener to handle updateStatus call.
	ui_UpdateStatus ( false );
}

/**
Carry out the edit command action, triggered by:
<ol>
<li>	Popup menu edit of a command on the command list.</li>
<li>	Pressing the enter key on the command list.</li>
<li>	Double-clicking on a command in the command list.</li>
</ol>
This method will call the uiAction_EditCommand() method with a list of
commands that were selected to be edited.  Multiple commands may be edited if
a block of # delimited comments.
*/
private void uiAction_EditCommand ()
{	int selected_size = 0;
	int [] selected = ui_GetCommandJList().getSelectedIndices();
	if ( selected != null ) {
		selected_size = selected.length;
	}
	if ( selected_size > 0 ) {
		Command command = (Command)__commands_JListModel.get(selected[0]);
		Vector v = null;
		if ( command.toString().startsWith("#") ) {
			// Allow multiple lines to be edited in a comment...
			// This is handled in the called method, which brings up a multi-line editor for comments.
            // Only edit the contiguous # block. The first one is a # but stop adding when lines no longer
			// start with #
			v = new Vector ( selected_size );
			for ( int i = 0; i < selected_size; i++ ) {
				command = (Command)__commands_JListModel.get(selected[i]);
				if ( !command.toString().startsWith("#")) {
					break;
				}
				// Else add command to the list.
				v.addElement ( command );
			}
		}
		else {
            // Commands are one line...
			v = new Vector ( 1 );
			v.addElement ( command );
		}
		commandList_EditCommand ( "", v, __UPDATE_COMMAND ); // No action event from menus
	}
}

/**
Handle Edit...Command File, which uses an external editor to edit the file.
*/
private void uiAction_EditCommandFile ()
{	String routine = getClass().getName() + ".uiAction_EditCommandFile";
	// Get the name of the file to edit and then use notepad...
	//
	// Instantiate a file dialog object with no default...
	//
	JFileChooser fc = JFileChooserFactory.createJFileChooser (ui_GetDir_LastCommandFileOpened() );
	fc.setDialogTitle ( "Select File" );
	SimpleFileFilter sff = new SimpleFileFilter ( "TSTool", "TSTool Command File" );
	fc.addChoosableFileFilter ( sff );
	fc.setFileFilter ( sff );
	if ( fc.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
		// Return if no file name is selected...
		return;
	}
	String path = fc.getSelectedFile().getPath();
	ui_SetDir_LastCommandFileOpened ( fc.getSelectedFile().getParent() );
	String [] command_array = new String[2];
	if ( IOUtil.isUNIXMachine() ) {
		command_array[0] = "nedit";
	}
	else {
        command_array[0] = "notepad";
	}
	if ( path != null ) {
		try {
            command_array[1] = path;
			ProcessManager p = new ProcessManager( command_array );
			Thread thread = new Thread ( p );
			thread.start ();
		}
		catch ( Exception e2 ) {
			Message.printWarning ( 1, routine, "Unable to edit file \"" + path + "\"" );
		}
	}
}

/**
Export time series to a file.   Assume that if this is called, the state of the
GUI is such that time series are available from the TSEngine.
@param format Format to export, as a tstool command line option.
@param filename Name of file to save as, as a tstool command line option (e.g.,
"-o filename").  If previewing output, this will be "-preview".
*/
private void uiAction_ExportTimeSeriesResults ( String format, String filename )
{	String routine = getClass().getName() + ".uiAction_ExportTimeSeriesResults";
	if ( Message.isDebugOn ) {
		Message.printDebug ( 1, routine, "In export" );
	}

	// Get the time series list

	PropList props = new PropList ( "SaveAsProps" );
	props.set ( "OutputFormat=" + format );
	props.set ( "OutputFile=" + filename );
	// Final list is selected...
	 if ( __ts_processor != null ) {
		try {
            int selected_ts = JGUIUtil.selectedSize(__results_ts_JList);
			if ( selected_ts == 0 ) {
				commandProcessor_ProcessTimeSeriesResultsList(null,props);
			}
			else {
                commandProcessor_ProcessTimeSeriesResultsList (	__results_ts_JList.getSelectedIndices(), props );
			}
		}
		catch ( Exception e ) {
			Message.printWarning ( 1, routine, "Unable to save time series." );
		}
	}
}

/**
Handle "File...Exit" and X actions.
*/
private void uiAction_FileExitClicked ()
{	// If the commands are dirty, see if they want to save them...
	// This code is also in openCommandFile - might be able to remove
	// copy once all actions are implemented...
	int x = ResponseJDialog.YES;	// Default for batch mode
	if ( !TSToolMain.isServer() && !IOUtil.isBatch() ) {
		if ( __commands_dirty ) {
			if ( __command_file_name == null ) {
				// Have not been saved before...
				x = ResponseJDialog.NO;
				if ( __commands_JListModel.size() > 0 ) {
	                if ( __ts_processor.getReadOnly() ) {
	                    x = new ResponseJDialog ( this, IOUtil.getProgramName(),
	                            "The command file is marked read-only.\n" +
	                            "Press Cancel and then save to a new name if desired." +
	                            "Press YES to update the read-only file.",
	                            ResponseJDialog.YES|ResponseJDialog.NO|ResponseJDialog.CANCEL).response();
	                }
	                else {
	                     x = new ResponseJDialog ( this,	IOUtil.getProgramName(),
                             "Do you want to save the changes you made?",
                             ResponseJDialog.YES|ResponseJDialog.NO|ResponseJDialog.CANCEL).response();
	                }
	            }
				if ( x == ResponseJDialog.CANCEL ) {
					setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
					return;
				}
				else if ( x == ResponseJDialog.YES ) {
					// Prompt for the name and then save...
					uiAction_WriteCommandFile (	__command_file_name, true);
				}
			}
			else {
                // A command file exists...  Warn the user. They can save to the existing file name or
				// can cancel and File...Save As... to a different name.  Have not been saved before...
				x = ResponseJDialog.NO;
				if ( __commands_JListModel.size() > 0 ) {
				    if ( __ts_processor.getReadOnly() ) {
                        x = new ResponseJDialog ( this, IOUtil.getProgramName(),
                                "The command file is marked read-only.  Changes cannot be saved.\n" +
                                "Press Cancel and then save to a new name if desired.",
                                ResponseJDialog.OK|ResponseJDialog.CANCEL).response();
                    }
                    else {
                        x = new ResponseJDialog ( this,
                                IOUtil.getProgramName(), "Do you want to save the changes you made to\n\""
                                + __command_file_name + "\"?",
                                ResponseJDialog.YES| ResponseJDialog.NO|ResponseJDialog.CANCEL).response();
                    }
				}
				if ( x == ResponseJDialog.CANCEL ) {
					setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
					return;
				}
				else if ( x == ResponseJDialog.YES ) {
					uiAction_WriteCommandFile (	__command_file_name, false );
				}
				// Else if No will just exit below...
			}
		}
		// Now make sure the user wants to exit - they might have a lot of data processed...
		x = new ResponseJDialog (this, "Exit TSTool", "Are you sure you want to exit TSTool?",
			ResponseJDialog.YES| ResponseJDialog.NO).response();
	}
	if ( x == ResponseJDialog.YES ) {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(false);
		try {
            dispose();
		}
		catch ( Exception e ) {
			// Why is this a problem?
		}
		Message.closeLogFile();
		System.exit(0);
	}
	else {
        // Cancel...
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
	}
}

/**
Respond to "Get Time Series List" being clicked.
*/
private void uiAction_GetTimeSeriesListClicked()
{	String message, routine = getClass().getName() + ".getTimeSeriesListClicked";
	Message.printStatus ( 1, routine, "Getting time series list from " + __selected_input_type + " input type..." );

	// Verify that the input filters have valid data...

	if ( __selected_input_filter_JPanel != __input_filter_generic_JPanel ) {
		if ( !((InputFilter_JPanel)__selected_input_filter_JPanel).
			checkInput(true) ) {
			// An input error was detected so don't get the time series...
			return;
		}
	}

	// Read the time series list and display in the Time Series List
	// area.  Return if an error occurs because the message at the bottom
	// should only be printed if successful.

	if ( __selected_input_type.equals (__INPUT_TYPE_DateValue)) {
		try {
            uiAction_GetTimeSeriesListClicked_ReadDateValueHeaders ();
		}
		catch ( Exception e ) {
			message = "Error reading DateValue file.  Cannot display time series list.";
			Message.printWarning ( 1, routine, message );
			Message.printWarning ( 2, routine, e );
			return;
		}
	}
	else if ( __selected_input_type.equals (__INPUT_TYPE_DIADvisor)) {
		try {
            uiAction_GetTimeSeriesListClicked_ReadDIADvisorHeaders(); 
		}
		catch ( Exception e ) {
			message = "Error reading DIADvisor.  Cannot display time series list.";
			Message.printWarning ( 1, routine, message );
			Message.printWarning ( 2, routine, e );
			return;
		}
	}
    else if ( __selected_input_type.equals (__INPUT_TYPE_HECDSS)) {
        try {
            uiAction_GetTimeSeriesListClicked_ReadHECDSSHeaders ();
        }
        catch ( Exception e ) {
            message = "Error reading HEC-DSS file.  Cannot display time series list.";
            Message.printWarning ( 1, routine, message );
            Message.printWarning ( 2, routine, e );
            return;
        }
    }
	else if ( __selected_input_type.equals (__INPUT_TYPE_HydroBase)) {
		try {
            uiAction_GetTimeSeriesListClicked_ReadHydroBaseHeaders ( null ); 
		}
		catch ( Exception e ) {
			message = "Error reading HydroBase.  Cannot display time series list.";
			Message.printWarning ( 1, routine, message );
			Message.printWarning ( 2, routine, e );
			return;
		}
	}
	else if ( __selected_input_type.equals (__INPUT_TYPE_MEXICO_CSMN)) {
		try {
            uiAction_GetTimeSeriesListClicked_ReadMexicoCSMNHeaders ();
		}
		catch ( Exception e ) {
			message =
			"Error reading Mexico CSMN catalog file.  Cannot display time series list.";
			Message.printWarning ( 1, routine, message );
			Message.printWarning ( 2, routine, e );
			return;
		}
	}
	else if ( __selected_input_type.equals (__INPUT_TYPE_MODSIM)) {
		try {
            uiAction_GetTimeSeriesListClicked_ReadMODSIMHeaders ();
		}
		catch ( Exception e ) {
			message = "Error reading MODSIM file.  Cannot display time series list.";
			Message.printWarning ( 1, routine, message );
			Message.printWarning ( 2, routine, e );
			return;
		}
	}
	else if ( __selected_input_type.equals (__INPUT_TYPE_NWSCARD)) {
		try {
            uiAction_GetTimeSeriesListClicked_ReadNWSCARDHeaders ();
		}
		catch ( Exception e ) {
			message = "Error reading NWS CARD file.  Cannot display time series list.";
			Message.printWarning ( 1, routine, message );
			Message.printWarning ( 2, routine, e );
			return;
		}
	}
	else if ( __selected_input_type.equals(	__INPUT_TYPE_NWSRFS_ESPTraceEnsemble)) {
		try {
            uiAction_GetTimeSeriesListClicked_ReadNwsrfsEspTraceEnsembleHeaders ();
		}
		catch ( Exception e ) {
			message = "Error reading NWSRFS_ESPTraceEnsemble file.  Cannot display time series list.";
			Message.printWarning ( 1, routine, message );
			Message.printWarning ( 2, routine, e );
			return;
		}
	}
	else if ( __selected_input_type.equals (__INPUT_TYPE_NWSRFS_FS5Files)) {
		try {
            // This reads the time series headers and displays the results in the list...
			uiAction_GetTimeSeriesListClicked_ReadNWSRFSFS5FilesHeaders ();
		}
		catch ( Exception e ) {
			message = "Error reading NWSRFS FS5Files time series list.  Cannot display time series list.";
			Message.printWarning ( 1, routine, message );
			Message.printWarning ( 2, routine, e );
			return;
		}
	}
	else if ( __selected_input_type.equals (__INPUT_TYPE_RiversideDB)) {
		try {
            uiAction_GetTimeSeriesListClicked_ReadRiversideDBHeaders(); 
		}
		catch ( Exception e ) {
			message = "Error reading RiversideDB.  Cannot display time series list.";
			Message.printWarning ( 1, routine, message );
			Message.printWarning ( 2, routine, e );
			return;
		}
	}
	else if ( __selected_input_type.equals (__INPUT_TYPE_RiverWare)) {
		try {
            uiAction_GetTimeSeriesListClicked_ReadRiverWareHeaders ();
		}
		catch ( Exception e ) {
			message = "Error reading RiverWare file.  Cannot display time series list.";
			Message.printWarning ( 1, routine, message );
			Message.printWarning ( 2, routine, e );
			return;
		}
	}
	else if ( __selected_input_type.equals (__INPUT_TYPE_StateCU)) {
		try {
            uiAction_GetTimeSeriesListClicked_ReadStateCUHeaders ();
		}
		catch ( Exception e ) {
			message = "Error reading StateCU file.  Cannot display time series list.";
			Message.printWarning ( 1, routine, message );
			Message.printWarning ( 2, routine, e );
			return;
		}
	}
   else if ( __selected_input_type.equals (__INPUT_TYPE_StateCUB)) {
        try {
            uiAction_GetTimeSeriesListClicked_ReadStateCUBHeaders ();
        }
        catch ( Exception e ) {
            message = "Error reading StateCU binary file.  Cannot display time series list.";
            Message.printWarning ( 1, routine, message );
            Message.printWarning ( 2, routine, e );
            return;
        }
    }
	else if ( __selected_input_type.equals (__INPUT_TYPE_StateMod)) {
		try {
            uiAction_GetTimeSeriesListClicked_ReadStateModHeaders ();
		}
		catch ( Exception e ) {
			message = "Error reading StateMod file.  Cannot display time series list.";
			Message.printWarning ( 1, routine, message );
			Message.printWarning ( 2, routine, e );
			return;
		}
	}
	else if ( __selected_input_type.equals (__INPUT_TYPE_StateModB)) {
		try {
            uiAction_GetTimeSeriesListClicked_ReadStateModBHeaders ();
		}
		catch ( Exception e ) {
			message = "Error reading StateMod binary file.  Cannot display time series list.";
			Message.printWarning ( 1, routine, message );
			Message.printWarning ( 2, routine, e );
			return;
		}
	}
	else if ( __selected_input_type.equals (__INPUT_TYPE_USGSNWIS)) {
		try {
            uiAction_GetTimeSeriesListClicked_ReadUsgsNwisHeaders ();
		}
		catch ( Exception e ) {
			message = "Error reading USGS NWIS file.  Cannot display time series list.";
			Message.printWarning ( 1, routine, message );
			Message.printWarning ( 2, routine, e );
			return;
		}
	}
	Message.printStatus ( 1, routine,
	"Time series list from " + __selected_input_type + " input type are listed in Time Series List area." );
}

/**
Read the list of time series from a DateValue file and list in the GUI.
*/
private void uiAction_GetTimeSeriesListClicked_ReadDateValueHeaders ()
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
		// There should not be any non-null time series so use the
		// Vector size...
		int size = tslist.size();
        	if ( size == 0 ) {
			Message.printStatus ( 1, routine, "No DateValue TS read." );
			queryResultsList_Clear ();
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
        	ui_UpdateStatus ( false );
		}
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
private void uiAction_GetTimeSeriesListClicked_ReadDIADvisorHeaders()
{	String rtn = "TSTool_JFrame.readDIADvisorHeaders";
        JGUIUtil.setWaitCursor ( this, true );
        Message.printStatus ( 1, rtn, "Please wait... retrieving data");

	try {	queryResultsList_Clear ();

		//String location = "";
		//String data_type = StringUtil.getToken(
			//__data_type_JComboBox.getSelected()," ",0,0).trim();
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
		String interval = __time_step_JComboBox.getSelected();
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

		ui_UpdateStatus ( false );
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
Read the list of time series from a HEC-DSS database file and list in the GUI.
The filename is taken from the selected item in the __input_name_JComboBox.
*/
private void uiAction_GetTimeSeriesListClicked_ReadHECDSSHeaders ()
throws IOException
{   String routine = "TSTool_JFrame.readHECDSSHeaders";

    try {
        String path = __input_name_JComboBox.getSelected();
        Message.printStatus ( 1, routine, "Reading HEC-DSS file \"" + path + "\"" );
        Vector tslist = null;
        JGUIUtil.setWaitCursor ( this, true );
        // TODO SAM 2008-09-03 Enable searchable fields
        tslist = HecDssAPI.readTimeSeriesList ( "*.*.*.*.*~HEC-DSS~" + path, null, null, null, false );
        int size = 0;
        if ( tslist != null ) {
            size = tslist.size();
            __query_TableModel = new TSTool_TS_TableModel ( tslist );
            TSTool_TS_CellRenderer cr = new TSTool_TS_CellRenderer( (TSTool_TS_TableModel)__query_TableModel);

            __query_JWorksheet.setCellRenderer ( cr );
            __query_JWorksheet.setModel ( __query_TableModel );
            // Turn off columns in the table model that do not apply...
            __query_JWorksheet.removeColumn (((TSTool_TS_TableModel)__query_TableModel).COL_ALIAS );
            __query_JWorksheet.removeColumn (((TSTool_TS_TableModel)__query_TableModel).COL_ALIAS );
            //__query_JWorksheet.removeColumn (((TSTool_TS_TableModel)__query_TableModel).COL_SCENARIO);
            __query_JWorksheet.removeColumn (((TSTool_TS_TableModel)__query_TableModel).COL_DATA_SOURCE );
            //__query_JWorksheet.removeColumn (((TSTool_TS_TableModel)__query_TableModel).COL_DATA_TYPE );
            __query_JWorksheet.setColumnWidths ( cr.getColumnWidths(), getGraphics() );
        }
        if ( (tslist == null) || (size == 0) ) {
            Message.printStatus ( 1, routine, "No HEC-DSS time series were read." );
            queryResultsList_Clear ();
        }
        else {
            Message.printStatus ( 1, routine, "" + size + " HEC-DSS time series were read." );
        }

        ui_UpdateStatus ( false );
        tslist = null;
        JGUIUtil.setWaitCursor ( this, false );
    }
    catch ( Exception e ) {
        String message = "Error reading HEC-DSS file.";
        Message.printWarning ( 2, routine, message );
        Message.printWarning ( 2, routine, e );
        JGUIUtil.setWaitCursor ( this, false );
        throw new IOException ( message );
    }
}

/**
Query HydroBase time series and list in the GUI, using the current selections.
@param grlimits If a query is being executed from the map interface, then the
limits will be non-null.
@throws Exception if there is an error.
*/
private void uiAction_GetTimeSeriesListClicked_ReadHydroBaseHeaders ( GRLimits grlimits )
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
			queryResultsList_Clear ();
       		}
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
Read the list of time series from a Mexico CSMN CATALOGO.TXT file and list in
the GUI.  Because the catalog file is rather long (e.g., 5000 lines) and does
not change, it is only read once.  If necessary, later change to prompt each
time.
*/
private void uiAction_GetTimeSeriesListClicked_ReadMexicoCSMNHeaders ()
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
			String path = fc.getSelectedFile().getPath();
			JGUIUtil.setLastFileDialogDirectory ( directory );

			Message.printStatus ( 1, routine,
			"Reading Mexico CSMN catalog file \"" + path + "\"" );
			JGUIUtil.setWaitCursor ( this, true );
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
			queryResultsList_Clear ();
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

        	ui_UpdateStatus ( false );
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
private void uiAction_GetTimeSeriesListClicked_ReadMODSIMHeaders ()
throws IOException
{	String message, routine = getClass().getName() + ".readMODSIMHeaders";

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
		// There should not be any non-null time series so use the
		// Vector size...
		int size = tslist.size();
        	if ( size == 0 ) {
			Message.printStatus ( 1, routine, "No MODSIM TS read." );
			queryResultsList_Clear ();
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
        	ui_UpdateStatus ( false );
		}
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
private void uiAction_GetTimeSeriesListClicked_ReadNWSCARDHeaders ()
throws IOException
{	String message, routine = "TSTool_JFrame.uiAction_GetTimeSeriesListClicked_ReadNWSCARDHeaders";

	// TODO - need to allow multiple file selections...
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
			queryResultsList_Clear ();
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

        ui_UpdateStatus ( false );
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
private void uiAction_GetTimeSeriesListClicked_ReadNwsrfsEspTraceEnsembleHeaders ()
throws IOException
{	String message, routine = "TSTool_JFrame.ReadNwsrfsEspTraceEnsembleHeaders";

	try {
        JFileChooser fc = JFileChooserFactory.createJFileChooser ( JGUIUtil.getLastFileDialogDirectory() );
		fc.setDialogTitle ( "Select ESP Trace Ensemble File" );
		SimpleFileFilter csff = new SimpleFileFilter( "CS", "Conditional Simulation Trace File");
		fc.addChoosableFileFilter(csff);
		/* TODO - add later
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
		String path = fc.getSelectedFile().getPath();
		Message.printStatus ( 1, routine, "Reading NWSRFS ESPTraceEnsemble file \"" + path + "\"" );
		JGUIUtil.setWaitCursor ( this, true );
		Vector tslist = null;
		NWSRFS_ESPTraceEnsemble ensemble = null;
		try {
            ensemble = new NWSRFS_ESPTraceEnsemble ( path, false );
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
			__query_TableModel = new TSTool_ESPTraceEnsemble_TableModel ( ensemble );
			TSTool_ESPTraceEnsemble_CellRenderer cr =
				new TSTool_ESPTraceEnsemble_CellRenderer( (TSTool_ESPTraceEnsemble_TableModel)__query_TableModel);

			__query_JWorksheet.setCellRenderer ( cr );
			__query_JWorksheet.setModel ( __query_TableModel );
			__query_JWorksheet.setColumnWidths ( cr.getColumnWidths(), getGraphics() );
		}
       	if ( (tslist == null) || (size == 0) ) {
			Message.printStatus ( 1, routine, "No NWSRFS ESPTraceEnsemble TS read." );
			queryResultsList_Clear ();
       	}
       	else {
            Message.printStatus ( 1, routine, "" + size + " NWSRFS ESPTraceEnsemble TS traces read." );
       	}

		JGUIUtil.setWaitCursor ( this, false );
       	ui_UpdateStatus ( false );
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
private void uiAction_GetTimeSeriesListClicked_ReadNWSRFSFS5FilesHeaders ()
throws IOException
{	String message, routine = "TSTool_JFrame.readNWSRFSFS5FilesHeaders";

	try {
        JGUIUtil.setWaitCursor ( this, true );
		Vector tslist = null;
		// Default is to return all IDs...
		String id_input = "*";
		// Get the ID from the input filter...
		Vector input_Vector = __input_filter_NWSRFS_FS5Files_JPanel.getInput ( "ID", true, null );
		int isize = input_Vector.size();
		if ( isize > 1 ) {
			Message.printWarning ( 1, routine, "Only one input filter for ID can be specified." );
			return;
		}
		if ( isize > 0 ) {
			// Use the first matching filter...
			id_input = (String)input_Vector.elementAt(0);
		}
		String datatype = StringUtil.getToken ( __data_type_JComboBox.getSelected().trim(), " ",0, 0);
		// Parse the interval into the integer hour...
		String selected_input_name=__input_name_JComboBox.getSelected();
		try {
		    String tsident_string = id_input + ".NWSRFS." + datatype + "." +
				__selected_time_step + "~NWSRFS_FS5Files~" + selected_input_name;
			Message.printStatus ( 2, routine, "Reading NWSRFS FS5Files time series for \"" + tsident_string + "\"..." );
			tslist = __nwsrfs_dmi.readTimeSeriesList ( tsident_string, (DateTime)null,
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
		if ( tslist != null ) {
			size = tslist.size();

			// Now display in the table...

			__query_TableModel = new TSTool_TS_TableModel (	tslist, true );
			TSTool_TS_CellRenderer cr = new TSTool_TS_CellRenderer( (TSTool_TS_TableModel)__query_TableModel);
	
			__query_JWorksheet.setCellRenderer ( cr );
			__query_JWorksheet.setModel ( __query_TableModel );
			// Do not include the alias in the display...
			__query_JWorksheet.removeColumn (((TSTool_TS_TableModel)__query_TableModel).COL_ALIAS );
			__query_JWorksheet.setColumnWidths (cr.getColumnWidths(), getGraphics() );
		}
       	if ( (tslist == null) || (size == 0) ) {
			Message.printStatus ( 1, routine, "No NWSRFS FS5Files time series read." );
			queryResultsList_Clear ();
       	}
       	else {
            Message.printStatus ( 1, routine, "" + size + " NWSRFS FS5Files time series read." );
       	}

		JGUIUtil.setWaitCursor ( this, false );
       	ui_UpdateStatus ( false );
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
Read RiversideDB time series (MeasType) and list in the GUI.
*/
private void uiAction_GetTimeSeriesListClicked_ReadRiversideDBHeaders()
{	String rtn = "TSTool_JFrame.readRiversideDBHeaders";
    JGUIUtil.setWaitCursor ( this, true );
    Message.printStatus ( 1, rtn, "Please wait... retrieving data");

	// The headers are a Vector of HydroBase_
	try {
        queryResultsList_Clear ();

		String location = "";
		String data_type = StringUtil.getToken(	__data_type_JComboBox.getSelected()," ",0,0).trim();
		String timestep = __time_step_JComboBox.getSelected();
		if ( timestep == null ) {
			Message.printWarning ( 1, rtn, "No time series are available for timestep." );
			JGUIUtil.setWaitCursor ( this, false );
			return;
		}
		else {
            timestep = timestep.trim();
		}
		String data_type_mod = "";
		if ( data_type.indexOf ( "-" ) >= 0 ) {
			data_type_mod = StringUtil.getToken ( data_type,"-", 0, 1 );
		}
		if ( !data_type_mod.equals("") ) {
			data_type_mod = "-" + data_type_mod;
		}

		/* TODO - need to figure out how to do where - or do we just have choices?
        	// add where clause(s)   
		if ( getWhereClauses() == false ) {
        		JGUIUtil.setWaitCursor ( this, false );
        		Message.printStatus ( 1, rtn, 
			"Query aborted - null where string");
			return;
		}
		*/

		TSIdent ident = new TSIdent ( location + ".." + data_type +	data_type_mod + "." + timestep + "." );
		Message.printStatus ( 1, "",
		"Datatype = \"" + ident.getType() + "\" main = \"" +
		ident.getMainType() + "\" sub = \"" + ident.getSubType() +"\"");

		Vector results = null;

		try {
            results = __rdmi.readMeasTypeListForTSIdent (
				location + ".." + data_type + data_type_mod + "." + timestep + "." );
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
			__query_TableModel = new TSTool_RiversideDB_TableModel ( results );
			TSTool_RiversideDB_CellRenderer cr =
				new TSTool_RiversideDB_CellRenderer( (TSTool_RiversideDB_TableModel)__query_TableModel);

			__query_JWorksheet.setCellRenderer ( cr );
			__query_JWorksheet.setModel ( __query_TableModel );
			__query_JWorksheet.setColumnWidths ( cr.getColumnWidths(), getGraphics() );
		}
        if ( (results == null) || (size == 0) ) {
			Message.printStatus ( 1, rtn,"Query complete.  No records returned." );        	}
        	else {	Message.printStatus ( 1, rtn, "Query complete. " + size + " records returned." );
        }
        ui_UpdateStatus ( false );

        JGUIUtil.setWaitCursor ( this, false );
	}
	catch ( Exception e ) {
		// Messages elsewhere but catch so we can get the cursor back...
		Message.printWarning ( 3, rtn, e );
        JGUIUtil.setWaitCursor ( this, false );
	}
}

/**
Read the list of time series from RiverWare files and list in the GUI.
*/
private void uiAction_GetTimeSeriesListClicked_ReadRiverWareHeaders ()
throws IOException
{	String message, routine = "TSTool_JFrame.readRiverWareHeaders";

	try {
        JFileChooser fc = JFileChooserFactory.createJFileChooser ( JGUIUtil.getLastFileDialogDirectory() );
		fc.setDialogTitle ( "Select RiverWare Time Series File" );
		SimpleFileFilter sff = new SimpleFileFilter( "dat",	"RiverWare Time Series");
		fc.addChoosableFileFilter(sff);
		sff = new SimpleFileFilter( "rwts", "RiverWare Time Series");
		fc.addChoosableFileFilter(sff);
		sff = new SimpleFileFilter( "ts", "RiverWare Time Series");
		fc.addChoosableFileFilter(sff);
		if ( fc.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
			// Cancelled...
			return;
		}
		JGUIUtil.setLastFileDialogDirectory ( fc.getSelectedFile().getParent() );
		String path = fc.getSelectedFile().getPath();

		Message.printStatus ( 1, routine, "Reading RiverWare file \"" + path + "\"" );
		JGUIUtil.setWaitCursor ( this, true );
		TS ts = RiverWareTS.readTimeSeries ( path, null, null, null, false );
		if ( ts == null ) {
			message = "Error reading RiverWare file.";
			Message.printWarning ( 2, routine, message );
			JGUIUtil.setWaitCursor ( this, false );
			throw new IOException ( message );
		}
		Vector tslist = new Vector ( 1 );
		tslist.addElement ( ts );
		__query_TableModel = new TSTool_TS_TableModel ( tslist );
		TSTool_TS_CellRenderer cr = new TSTool_TS_CellRenderer( (TSTool_TS_TableModel)__query_TableModel);

		__query_JWorksheet.setCellRenderer ( cr );
		__query_JWorksheet.setModel ( __query_TableModel );
		__query_JWorksheet.removeColumn (((TSTool_TS_TableModel)__query_TableModel).COL_ALIAS );
		__query_JWorksheet.setColumnWidths ( cr.getColumnWidths(), getGraphics() );

        ui_UpdateStatus ( false );
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
Read the list of time series from a StateCU file and display the list in the GUI.
@exception Exception if there is an error reading the file.
*/
private void uiAction_GetTimeSeriesListClicked_ReadStateCUHeaders ()
throws Exception
{	String message, routine = "TSTool_JFrame.readStateCUHeaders";

	try {
        Vector tslist = null; // Time series to display.
		TS ts = null; // Single time series.
		int size = 0; // Number of time series.
		String path = __input_name_JComboBox.getSelected();
		Message.printStatus ( 1, routine, "Reading StateCU file \"" + path + "\"" );
		if (	(__input_name_FileFilter ==	__input_name_StateCU_iwrrep_FileFilter) ||
			(__input_name_FileFilter ==	__input_name_StateCU_wsl_FileFilter) ||
			(__input_name_FileFilter ==	__input_name_StateCU_frost_FileFilter) ) {
			JGUIUtil.setWaitCursor ( this, true );
			tslist = StateCU_TS.readTimeSeriesList ( path, null, null, null, false );
			if ( tslist != null ) {
				size = tslist.size();
				__query_TableModel = new TSTool_TS_TableModel ( tslist );
				TSTool_TS_CellRenderer cr =	new TSTool_TS_CellRenderer(	(TSTool_TS_TableModel)__query_TableModel);
	
				__query_JWorksheet.setCellRenderer ( cr );
				__query_JWorksheet.setModel(__query_TableModel);
				// Turn off columns in the table model that do not apply...
				__query_JWorksheet.removeColumn (((TSTool_TS_TableModel)__query_TableModel).COL_ALIAS );
				__query_JWorksheet.removeColumn (((TSTool_TS_TableModel)__query_TableModel).COL_DATA_SOURCE );
				__query_JWorksheet.removeColumn (((TSTool_TS_TableModel)__query_TableModel).COL_SCENARIO);
				__query_JWorksheet.setColumnWidths (cr.getColumnWidths(), getGraphics() );
			}
		}
		else if ( __input_name_FileFilter == __input_name_StateCU_cds_FileFilter ) {
			// Yearly crop patterns...
			JGUIUtil.setWaitCursor ( this, true );
			tslist = StateCU_CropPatternTS.readTimeSeriesList (	path, null, null, null, false );
			if ( tslist != null ) {
				size = tslist.size();
				__query_TableModel = new TSTool_TS_TableModel ( tslist );
				TSTool_TS_CellRenderer cr = new TSTool_TS_CellRenderer(	(TSTool_TS_TableModel)__query_TableModel);
	
				__query_JWorksheet.setCellRenderer ( cr );
				__query_JWorksheet.setModel(__query_TableModel);
				// Turn off columns in the table model that do not apply...
				__query_JWorksheet.removeColumn (((TSTool_TS_TableModel)__query_TableModel).COL_ALIAS );
				__query_JWorksheet.removeColumn (((TSTool_TS_TableModel)__query_TableModel).COL_DATA_SOURCE );
				__query_JWorksheet.removeColumn (((TSTool_TS_TableModel)__query_TableModel).COL_SCENARIO);
				__query_JWorksheet.setColumnWidths (cr.getColumnWidths(), getGraphics() );
			}
		}
		else if ((__input_name_FileFilter == __input_name_StateCU_ipy_FileFilter) ||
			(__input_name_FileFilter ==	__input_name_StateCU_tsp_FileFilter) ) {
			// Yearly irrigation practice...
			JGUIUtil.setWaitCursor ( this, true );
			tslist = StateCU_IrrigationPracticeTS.readTimeSeriesList ( path, null, null, null, false );
			if ( tslist != null ) {
				size = tslist.size();
				__query_TableModel = new TSTool_TS_TableModel ( tslist );
				TSTool_TS_CellRenderer cr =	new TSTool_TS_CellRenderer(	(TSTool_TS_TableModel)__query_TableModel);
	
				__query_JWorksheet.setCellRenderer ( cr );
				__query_JWorksheet.setModel(__query_TableModel);
				// Turn off columns in the table model that do not apply...
				__query_JWorksheet.removeColumn (((TSTool_TS_TableModel)__query_TableModel).COL_ALIAS );
				__query_JWorksheet.removeColumn (((TSTool_TS_TableModel)__query_TableModel).COL_DATA_SOURCE );
				__query_JWorksheet.removeColumn (((TSTool_TS_TableModel)__query_TableModel).COL_SCENARIO);
				__query_JWorksheet.setColumnWidths (cr.getColumnWidths(), getGraphics() );
			}
		}
		else if ((__input_name_FileFilter == __input_name_StateCU_ddc_FileFilter) ||
			(__input_name_FileFilter == __input_name_StateCU_ddy_FileFilter) ||
			(__input_name_FileFilter == __input_name_StateCU_ddh_FileFilter) ||
			(__input_name_FileFilter ==	__input_name_StateCU_precip_FileFilter) ||
			(__input_name_FileFilter ==	__input_name_StateCU_temp_FileFilter) ||
			(__input_name_FileFilter ==	__input_name_StateCU_frost_FileFilter) ) {
			// Normal daily or monthly StateMod file...
			JGUIUtil.setWaitCursor ( this, true );
			if ( __input_name_FileFilter == __input_name_StateCU_frost_FileFilter ) {
				tslist = StateCU_TS.readTimeSeriesList (path, null, null, null, false );
			}
			else {
                // Normal StateMod file...
				tslist = StateMod_TS.readTimeSeriesList ( path, null, null, null, false );
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
				__query_TableModel = new TSTool_TS_TableModel ( tslist );
				TSTool_TS_CellRenderer cr =	new TSTool_TS_CellRenderer(	(TSTool_TS_TableModel)__query_TableModel);
	
				__query_JWorksheet.setCellRenderer ( cr );
				__query_JWorksheet.setModel(__query_TableModel);
				// Turn off columns in the table model that do not apply...
				__query_JWorksheet.removeColumn (((TSTool_TS_TableModel)__query_TableModel).COL_ALIAS );
				__query_JWorksheet.removeColumn (((TSTool_TS_TableModel)__query_TableModel).COL_DATA_SOURCE );
				__query_JWorksheet.removeColumn (((TSTool_TS_TableModel)__query_TableModel).COL_DATA_TYPE );
				__query_JWorksheet.removeColumn (((TSTool_TS_TableModel)__query_TableModel).COL_SCENARIO);
				__query_JWorksheet.setColumnWidths (cr.getColumnWidths(), getGraphics() );
			}
		}
		else {
            Message.printWarning ( 1, routine, "File format not recognized for \"" + path + "\"" );
		}
        if ( (tslist == null) || (size == 0) ) {
			Message.printStatus (1, routine,"No StateCU time series read.");
			queryResultsList_Clear ();
        }
       	else {
            Message.printStatus ( 1, routine, "" + size + " StateCU TS read." );
        }

		JGUIUtil.setWaitCursor ( this, false );
       	ui_UpdateStatus ( false );
	}
	catch ( Exception e ) {
		message = "Unexpected error reading StateCU file.";
		Message.printWarning ( 2, routine, message );
		Message.printWarning ( 2, routine, e );
		JGUIUtil.setWaitCursor ( this, false );
		throw new Exception ( message );
	}
}

/**
Read the list of time series from a StateCU binary file and list in the GUI.
The binary file is taken from the selected item in the __input_name_JComboBox.
*/
private void uiAction_GetTimeSeriesListClicked_ReadStateCUBHeaders ()
throws IOException
{   String routine = "TSTool_JFrame.readStateCUdBHeaders";

    try {
        String path = __input_name_JComboBox.getSelected();
        Message.printStatus ( 1, routine, "Reading StateCU binary output file \"" + path + "\"" );
        Vector tslist = null;
        JGUIUtil.setWaitCursor ( this, true );
        StateCU_BTS bin = new StateCU_BTS ( path );
        tslist = bin.readTimeSeriesList ( "*.*." + __selected_data_type + ".*.*", null, null, null, false );
        bin.close();
        int size = 0;
        if ( tslist != null ) {
            size = tslist.size();
            __query_TableModel = new TSTool_TS_TableModel ( tslist );
            TSTool_TS_CellRenderer cr = new TSTool_TS_CellRenderer( (TSTool_TS_TableModel)__query_TableModel);

            __query_JWorksheet.setCellRenderer ( cr );
            __query_JWorksheet.setModel ( __query_TableModel );
            // Turn off columns in the table model that do not apply...
            __query_JWorksheet.removeColumn (((TSTool_TS_TableModel)__query_TableModel).COL_ALIAS );
            __query_JWorksheet.removeColumn (((TSTool_TS_TableModel)__query_TableModel).COL_ALIAS );
            __query_JWorksheet.removeColumn (((TSTool_TS_TableModel)__query_TableModel).COL_SCENARIO);
            __query_JWorksheet.removeColumn (((TSTool_TS_TableModel)__query_TableModel).COL_DATA_SOURCE );
            //__query_JWorksheet.removeColumn (((TSTool_TS_TableModel)__query_TableModel).COL_DATA_TYPE );
            __query_JWorksheet.setColumnWidths ( cr.getColumnWidths(), getGraphics() );
        }
        if ( (tslist == null) || (size == 0) ) {
            Message.printStatus ( 1, routine, "No StateCU binary time series were read." );
            queryResultsList_Clear ();
        }
        else {
            Message.printStatus ( 1, routine, "" + size + " StateCU binary TS were read." );
        }

        ui_UpdateStatus ( false );
        tslist = null;
        JGUIUtil.setWaitCursor ( this, false );
    }
    catch ( Exception e ) {
        String message = "Error reading StateCU binary file.";
        Message.printWarning ( 2, routine, message );
        Message.printWarning ( 2, routine, e );
        JGUIUtil.setWaitCursor ( this, false );
        throw new IOException ( message );
    }
}

/**
Read the list of time series from a StateMod file and display the list in the GUI.
@exception Exception if there is an error reading the file.
*/
private void uiAction_GetTimeSeriesListClicked_ReadStateModHeaders ()
throws Exception
{	String message, routine = "TSTool_JFrame.readStateModHeaders";

	try {
        JFileChooser fc = JFileChooserFactory.createJFileChooser ( JGUIUtil.getLastFileDialogDirectory() );
		// TODO SAM 2007-05-14 Need to better handle picking files.
		// Need to pick the file first and detect the time step from the file, similar to the binary file.
        // For now, key off the selected time step.
		SimpleFileFilter ddr_filter = null;
		SimpleFileFilter ifr_filter = null;
		SimpleFileFilter rer_filter = null;
		SimpleFileFilter wer_filter = null;
		if ( __selected_time_step.equals( __TIMESTEP_DAY)) {
			fc.setDialogTitle (	"Select StateMod Daily Time Series File" );
			SimpleFileFilter sff = new SimpleFileFilter( "ddd",	"StateMod Direct Diversion Demands (Daily)");
			fc.addChoosableFileFilter(sff);
			ddr_filter = new SimpleFileFilter( "ddr", "StateMod Diversion Rights (Daily)");
			fc.addChoosableFileFilter(ddr_filter);
			sff = new SimpleFileFilter( "ddy", "StateMod Historicial Diversions (Daily)");
			fc.addChoosableFileFilter(sff);
			sff = new SimpleFileFilter( "eoy", "StateMod Reservoir Storage (End of Day)");
			fc.addChoosableFileFilter(sff);
			ifr_filter = new SimpleFileFilter( "ifd", "StateMod Instream Flow Demands (Daily)");
			fc.addChoosableFileFilter(ifr_filter);
			sff = new SimpleFileFilter( "ifr", "StateMod Instream Flow Rights (Daily)");
			fc.addChoosableFileFilter(ifr_filter);
			rer_filter = new SimpleFileFilter( "rer", "StateMod Reservoir Rights (Daily)");
			fc.addChoosableFileFilter(rer_filter);
			sff = new SimpleFileFilter( "riy", "StateMod Base Streamflow (Daily)");
			fc.addChoosableFileFilter(sff);
			sff = new SimpleFileFilter( "riy", "StateMod Historicial Streamflow (Daily)");
			fc.addChoosableFileFilter(sff);
			SimpleFileFilter stm_sff = new SimpleFileFilter( "stm", "StateMod Time Series (Daily)");
			fc.addChoosableFileFilter(sff);
			sff = new SimpleFileFilter( "tad", "StateMod Reservoir Min/Max Targets (Daily)");
			fc.addChoosableFileFilter(sff);
			sff = new SimpleFileFilter( "wed", "StateMod Well Demands (Daily)");
			fc.addChoosableFileFilter(sff);
			wer_filter = new SimpleFileFilter( "wer", "StateMod Well Rights (Daily)");
			fc.addChoosableFileFilter(wer_filter);
			sff = new SimpleFileFilter( "wey", "StateMod Historicial Well Pumping (Daily)");
			fc.addChoosableFileFilter(sff);
			fc.setFileFilter(stm_sff);
		}
		else if ( __selected_time_step.equals( __TIMESTEP_MONTH)) {
			fc.setDialogTitle (	"Select StateMod Monthly Time Series File" );
			SimpleFileFilter sff = new SimpleFileFilter( "ddh",	"StateMod Historicial Diversions (Monthly)");
			fc.addChoosableFileFilter(sff);
			sff = new SimpleFileFilter( "dda", "StateMod Direct Diversion Demands (Average Monthly)");
			fc.addChoosableFileFilter(sff);
			sff = new SimpleFileFilter( "ddm", "StateMod Direct Diversion Demands (Monthly)");
			fc.addChoosableFileFilter(sff);
			sff = new SimpleFileFilter( "ddo", "StateMod Direct Diversion Demands Override (Monthly)");
			fc.addChoosableFileFilter(sff);
			ddr_filter = new SimpleFileFilter( "ddr", "StateMod Diversion Rights (Monthly)");
			fc.addChoosableFileFilter(ddr_filter);
			sff = new SimpleFileFilter( "eom", "StateMod Reservoir Storage (End of Month)");
			fc.addChoosableFileFilter(sff);
			sff = new SimpleFileFilter( "eva", "StateMod Evaporation (Monthly)");
			fc.addChoosableFileFilter(sff);
			sff = new SimpleFileFilter( "ifa", "StateMod Instream Flow Demands (Average Monthly)");
			fc.addChoosableFileFilter(sff);
			sff = new SimpleFileFilter( "ifm", "StateMod Instream Flow Demands (Monthly)");
			fc.addChoosableFileFilter(sff);
			sff = new SimpleFileFilter( "ifr", "StateMod Instream Flow Rights (Monthly)");
			fc.addChoosableFileFilter(ifr_filter);
			sff = new SimpleFileFilter( "ddc", "StateMod/StateCU Irrigation Water Requirement (Monthly)");
			fc.addChoosableFileFilter(sff);
			sff = new SimpleFileFilter( "iwr", "StateMod/StateCU Irrigation Water Requirement (Monthly)");
			fc.addChoosableFileFilter(sff);
			sff = new SimpleFileFilter( "pre", "StateMod Precipitation (Monthly)");
			fc.addChoosableFileFilter(sff);
			sff = new SimpleFileFilter( "rih", "StateMod Historicial Streamflow (Monthly)");
			fc.addChoosableFileFilter(sff);
			sff = new SimpleFileFilter( "riy", "StateMod Base Streamflow (Monthly)");
			fc.addChoosableFileFilter(sff);
			sff = new SimpleFileFilter( "tar", "StateMod Reservoir Min/Max Targets (Monthly)");
			fc.addChoosableFileFilter(sff);
			rer_filter = new SimpleFileFilter( "rer", "StateMod Reservoir Rights (Monthly)");
			fc.addChoosableFileFilter(rer_filter);
			SimpleFileFilter stm_sff = new SimpleFileFilter( "stm",	"StateMod Time Series (Monthly)");
			fc.addChoosableFileFilter(stm_sff);
			sff = new SimpleFileFilter( "wem", "StateMod Well Demands (Monthly)");
			fc.addChoosableFileFilter(sff);
			wer_filter = new SimpleFileFilter( "wer", "StateMod Well Rights (Monthly)");
			fc.addChoosableFileFilter(wer_filter);
			sff = new SimpleFileFilter( "weh", "StateMod Historicial Well Pumping (Monthly)");
			fc.addChoosableFileFilter(sff);
			fc.setFileFilter(stm_sff);
		}
		if ( fc.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
			// Cancelled...
			return;
		}
		String directory = fc.getSelectedFile().getParent();
		JGUIUtil.setLastFileDialogDirectory(directory);
		String path = fc.getSelectedFile().getPath();
		Message.printStatus ( 1, routine, "Reading StateMod file \"" + path + "\"" );
		// Normal daily or monthly format file...
		Vector tslist = null;
		JGUIUtil.setWaitCursor ( this, true );
		FileFilter ff = fc.getFileFilter();
		int interval_base = TimeInterval.MONTH;
		if ( __selected_time_step.equals(__TIMESTEP_DAY)) {
			interval_base = TimeInterval.DAY;
		}
		else if ( __selected_time_step.equals(__TIMESTEP_MONTH)) {
			interval_base = TimeInterval.MONTH;
		}
		/* TODO SAM 2007-05-16 Resolve later.  Use readStateMod() in the meantime.
		else if ( __selected_time_step.equals(__TIMESTEP_YEAR)) {
			interval_base = TimeInterval.YEAR;
		}
		*/
		if ( ff == ddr_filter ) {
			// First read the diversion rights...
			Vector ddr_Vector = StateMod_DiversionRight.readStateModFile ( path );
			// Convert the rights to time series (one per location)...
			tslist = StateMod_Util.createWaterRightTimeSeriesList (
					ddr_Vector,        // raw water rights
					interval_base,  // time series interval
					0,              // summarize time series at location
					-1,				// Don't consider parcel year
					true,			// Create data set total
					null,             // time series start
					null,             // time series end
					999999.0,	// No special treatment of junior rights
					null,
					null,
					false );          // don't read data - only header
		}
		else if ( ff == ifr_filter ) {
			// First read the instream flow rights...
			Vector ifr_Vector = StateMod_InstreamFlowRight.readStateModFile ( path );
			// Convert the rights to time series (one per location)...
			tslist = StateMod_Util.createWaterRightTimeSeriesList (
					ifr_Vector,        // raw water rights
					interval_base,  // time series interval
					0,              // summarize time series at location
					-1,				// Don't consider parcel year
					true,			// Create data set total
					null,              // time series start
					null,              // time series end
					999999.0,	// No special treatment of junior rights
					null,
					null,
					false );           // don't read data - only header
		}
		else if ( ff == rer_filter ) {
			// First read the reservoir rights...
			Vector rer_Vector = StateMod_ReservoirRight.readStateModFile ( path );
			// Convert the rights to time series (one per location)...
			tslist = StateMod_Util.createWaterRightTimeSeriesList (
					rer_Vector,        // raw water rights
					interval_base,  // time series interval
					0,              // summarize time series at location
					-1,				// Don't consider parcel year
					true,			// Create data set total
					null,              // time series start
					null,              // time series end
					999999.0,	// No special treatment of junior rights
					null,
					null,
					false );           // don't read data - only header
		}
		else if ( ff == wer_filter ) {
			// First read the well rights...
			Vector wer_Vector = StateMod_WellRight.readStateModFile ( path );
			// Convert the rights to time series (one per location)...
			tslist = StateMod_Util.createWaterRightTimeSeriesList (
					wer_Vector,        // raw water rights
					interval_base,  // time series interval
					0,              // summarize time series at location
					-1,				// Don't consider parcel year
					true,			// Create data set total
					null,              // time series start
					null,              // time series end
					999999.0,	// No special treatment of junior rights
					null,
					null,
					false );           // don't read data - only header
		}
		else if ( __selected_time_step.equals(__TIMESTEP_DAY) ) {
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
			TSTool_TS_CellRenderer cr = new TSTool_TS_CellRenderer(	(TSTool_TS_TableModel)__query_TableModel);
	
			__query_JWorksheet.setCellRenderer ( cr );
			__query_JWorksheet.setModel(__query_TableModel);
			// Turn off columns in the table model that do not apply...
			__query_JWorksheet.removeColumn ( ((TSTool_TS_TableModel)__query_TableModel).COL_ALIAS );
			__query_JWorksheet.removeColumn ( ((TSTool_TS_TableModel)__query_TableModel).COL_DATA_SOURCE );
			__query_JWorksheet.removeColumn ( ((TSTool_TS_TableModel)__query_TableModel).COL_DATA_TYPE );
			__query_JWorksheet.removeColumn ( ((TSTool_TS_TableModel)__query_TableModel).COL_SCENARIO);
			__query_JWorksheet.setColumnWidths ( cr.getColumnWidths(), getGraphics() );
		}
        if ( (tslist == null) || (size == 0) ) {
			Message.printStatus ( 1, routine, "No StateMod time series read." );
			queryResultsList_Clear ();
        }
        else {
            Message.printStatus ( 1, routine, "" + size + " StateMod time series read." );
        }

		JGUIUtil.setWaitCursor ( this, false );
        ui_UpdateStatus ( false );
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
private void uiAction_GetTimeSeriesListClicked_ReadStateModBHeaders ()
throws IOException
{	String routine = "TSTool_JFrame.readStateModBHeaders";

	try {
        String path = __input_name_JComboBox.getSelected();
		Message.printStatus ( 1, routine, "Reading StateMod binary output file \"" + path + "\"" );
		Vector tslist = null;
		JGUIUtil.setWaitCursor ( this, true );
		StateMod_BTS bin = new StateMod_BTS ( path );
		tslist = bin.readTimeSeriesList ( "*.*." + __selected_data_type + ".*.*", null, null, null, false );
		bin.close();
		int size = 0;
		if ( tslist != null ) {
			size = tslist.size();
			__query_TableModel = new TSTool_TS_TableModel ( tslist );
			TSTool_TS_CellRenderer cr =	new TSTool_TS_CellRenderer(	(TSTool_TS_TableModel)__query_TableModel);

			__query_JWorksheet.setCellRenderer ( cr );
			__query_JWorksheet.setModel ( __query_TableModel );
			// Turn off columns in the table model that do not apply...
			__query_JWorksheet.removeColumn (((TSTool_TS_TableModel)__query_TableModel).COL_ALIAS );
			__query_JWorksheet.removeColumn (((TSTool_TS_TableModel)__query_TableModel).COL_ALIAS );
			__query_JWorksheet.removeColumn (((TSTool_TS_TableModel)__query_TableModel).COL_SCENARIO);
			__query_JWorksheet.removeColumn (((TSTool_TS_TableModel)__query_TableModel).COL_DATA_SOURCE );
			//__query_JWorksheet.removeColumn (((TSTool_TS_TableModel)__query_TableModel).COL_DATA_TYPE );
			__query_JWorksheet.setColumnWidths ( cr.getColumnWidths(), getGraphics() );
		}
       	if ( (tslist == null) || (size == 0) ) {
			Message.printStatus ( 1, routine, "No StateMod binary time series were read." );
			queryResultsList_Clear ();
       	}
       	else {
            Message.printStatus ( 1, routine, "" + size + " StateMod binary TS were read." );
      	}

       	ui_UpdateStatus ( false );
		tslist = null;
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
private void uiAction_GetTimeSeriesListClicked_ReadUsgsNwisHeaders ()
throws IOException
{	String message, routine = "TSTool_JFrame.readUsgsNwisHeaders";

	try {
        JFileChooser fc = JFileChooserFactory.createJFileChooser ( JGUIUtil.getLastFileDialogDirectory() );
		fc.setDialogTitle ( "Select USGS NWIS Daily Surface Flow File");
		SimpleFileFilter sff = new SimpleFileFilter( "txt", "USGS NWIS Daily Surface Flow File");
		fc.addChoosableFileFilter(sff);
		if ( fc.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
			// Cancelled...
			return;
		}
		JGUIUtil.setLastFileDialogDirectory ( fc.getSelectedFile().getParent() );
		String path = fc.getSelectedFile().getPath();

		Message.printStatus ( 1, routine, "Reading USGS NWIS file \"" + path + "\"" );
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
		TSTool_TS_CellRenderer cr = new TSTool_TS_CellRenderer(	(TSTool_TS_TableModel)__query_TableModel);

		__query_JWorksheet.setCellRenderer ( cr );
		__query_JWorksheet.setModel ( __query_TableModel );
		__query_JWorksheet.removeColumn (((TSTool_TS_TableModel)__query_TableModel).COL_ALIAS );
		__query_JWorksheet.setColumnWidths ( cr.getColumnWidths(), getGraphics() );

        ui_UpdateStatus ( false );
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
Graph ensemble results.
@param graph_type Type of graph (same as tstool command line arguments).
*/
private void uiAction_GraphEnsembleResults ( String graph_type ) 
{   String routine = getClass().getName() + ".uiAction_GraphEnsembleResults";

    try {
        // Change the wait cursor here in the main GUI to indicate that the graph is being processed.
        JGUIUtil.setWaitCursor ( this, true );
        PropList props = new PropList ( "GraphProps" );
        props.set ( "OutputFormat=" + graph_type );
        // Final list is selected...
        if ( __ts_processor != null ) {
            try {
                int selected_tsensembles = JGUIUtil.selectedSize(__results_tsensembles_JList);
                if ( selected_tsensembles == 0 ) {
                    commandProcessor_ProcessEnsembleResultsList ( null, props );
                }
                else {
                    commandProcessor_ProcessEnsembleResultsList ( __results_tsensembles_JList.getSelectedIndices(), props );
                }
            }
            catch ( Exception e ) {
                Message.printWarning ( 1, routine, "Unable to graph time series from ensemble results." );
                Message.printWarning ( 3, routine, e );
            }
        }
        JGUIUtil.setWaitCursor ( this, false );
    }
    catch ( Exception e ) {
        JGUIUtil.setWaitCursor ( this, false );
    }
}

/**
Graph time series that are in the final list.
@param graph_type Type of graph to create.
*/
private void uiAction_GraphTimeSeriesResults ( String type )
{	uiAction_GraphTimeSeriesResults ( type, null );
}

/**
Graph time series that are in the final list.
@param graph_type Type of graph (same as tstool command line arguments).
*/
private void uiAction_GraphTimeSeriesResults ( String graph_type, String params ) 
{	String routine = getClass().getName() + ".uiAction_GraphTimeSeriesResults";

	try {
		// Change the wait cursor here in the main GUI to indicate that the graph is being processed.
		JGUIUtil.setWaitCursor ( this, true );
		PropList props = new PropList ( "GraphProps" );
		props.set ( "OutputFormat=" + graph_type );
		if ( params != null ) {
			props.set ( "Parameters=" + params );
		}
		// Final list is selected...
		if ( __ts_processor != null ) {
			try {
                int selected_ts=JGUIUtil.selectedSize(__results_ts_JList);
				if ( selected_ts == 0 ) {
					commandProcessor_ProcessTimeSeriesResultsList (	null, props );
				}
				else {
                    commandProcessor_ProcessTimeSeriesResultsList (	__results_ts_JList.getSelectedIndices(), props );
				}
			}
			catch ( Exception e ) {
				Message.printWarning ( 1, routine, "Unable to graph time series." );
                Message.printWarning ( 3, routine, e );
			}
		}
		JGUIUtil.setWaitCursor ( this, false );
	}
	catch ( Exception e ) {
		JGUIUtil.setWaitCursor ( this, false );
	}
}

/**
Reset the query options choices based on the selected input name.  Other
method calls are cascaded to fully reset the choices.
*/
private void uiAction_InputNameChoiceClicked()
{	String routine = getClass().getName() + ".inputNameChoiceClicked";
	if ( __input_name_JComboBox == null ) {
		return;
	}

	__selected_input_name = __input_name_JComboBox.getSelected();

	if ( Message.isDebugOn ) {
		Message.printDebug ( 1, routine, "Input name has been selected:  \"" + __selected_input_name + "\"" );
	}

	// List alphabetically...
	try {
    	if ( __selected_input_type.equals ( __INPUT_TYPE_NWSRFS_FS5Files ) ) {
    		// Reset the data types...
    		__data_type_JComboBox.setEnabled ( true );
    		__data_type_JComboBox.removeAll ();
    		// TODO SAM 2004-09-01 need to find a way to not re-read the data types file.
    		Vector data_types = NWSRFS_Util.getTimeSeriesDataTypes ( __nwsrfs_dmi, true ); // Include description
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
shows/hides columns in the query results multilist to be appropriate for the data input source.
*/
private void uiAction_InputTypeChoiceClicked()
{	String routine = "TSTool_JFrame.inputTypeChoiceClicked";
	if ( __input_type_JComboBox == null ) {
		if ( Message.isDebugOn ) {
			Message.printDebug ( 1, routine, "Input type has been selected but GUI is not yet initialized.");
		}
		return;	// Not done initializing.
	}
	__selected_input_type = __input_type_JComboBox.getSelected();
	if ( __selected_input_type == null ) {
		// Apparently this happens when setData() or similar is called
		// on the JComboBox, and before select() is called.
		if ( Message.isDebugOn ) {
			Message.printDebug ( 1, routine, "Input type has been selected:  null (select is ignored)" );
		}
		return;
	}

	if ( Message.isDebugOn ) {
		Message.printDebug ( 1, routine, "Input type has been selected:  \"" + __selected_input_type + "\"" );
	}

	// List alphabetically...
	try {
	if ( __selected_input_type.equals ( __INPUT_TYPE_DateValue ) ) {
		// Most information is determined from the file so set the other choices to be inactive...
		__input_name_JComboBox.removeAll();
		__input_name_JComboBox.setEnabled ( false );

		__data_type_JComboBox.setEnabled ( false );
		__data_type_JComboBox.removeAll ();
		__data_type_JComboBox.add( __DATA_TYPE_AUTO );

		// Initialize with blank data vector...

		__query_TableModel = new TSTool_TS_TableModel ( null, true );
		TSTool_TS_CellRenderer cr = new TSTool_TS_CellRenderer(	(TSTool_TS_TableModel)__query_TableModel);
		// TODO Seems to be null at startup?
		if ( __query_JWorksheet != null ) {
			try {
                __query_JWorksheet.setCellRenderer ( cr );
				__query_JWorksheet.setModel ( __query_TableModel );
				__query_JWorksheet.setColumnWidths ( cr.getColumnWidths() );
			}
			catch ( Exception e ) {
				// Absorb the exception in most cases - print if developing to see if this issue can be resolved.
				if ( Message.isDebugOn && IOUtil.testing()  ) {
					Message.printWarning ( 3, routine,
                            "For developers:  caught exception in clearQueryList JWorksheet at setup." );
					Message.printWarning ( 3, routine, e );
				}
			}
		}
	}
	else if ( __selected_input_type.equals ( __INPUT_TYPE_DIADvisor ) ) {
		__data_type_JComboBox.setEnabled ( true );
		__data_type_JComboBox.removeAll ();
		// Get the data types from the GroupDef table.  Because two values may be available, append "-DataValue" and
		// "-DataValue2" to each group.
		Vector sensordef_Vector = null;
		try {
		    sensordef_Vector = __DIADvisor_dmi.readSensorDefListForDistinctGroup();
		}
		catch ( Exception e ) {
			Message.printWarning ( 2, routine, e );
			Message.printWarning ( 2, routine, __DIADvisor_dmi.getLastSQLString() );
		}
		// For now always list a DataValue and DataValue2, even though
		// DataValue2 may not be available - need to get more info from DIAD to know how to limit...

		int size = 0;
		if ( sensordef_Vector != null ) {
			size = sensordef_Vector.size();
		}
		DIADvisor_SensorDef sensordef = null;
		for ( int i = 0; i < size; i++ ) {
			sensordef = (DIADvisor_SensorDef)sensordef_Vector.elementAt(i);
			__data_type_JComboBox.add( sensordef.getGroup() + "-DataValue" );
			__data_type_JComboBox.add( sensordef.getGroup() + "-DataValue2" );
		}

		// Default to first in the list...
		__data_type_JComboBox.select( null );
		__data_type_JComboBox.select( 0 );

		/* TODO - need to put these in input filters...
		__where_JComboBox.setEnabled ( true );
		__where_JComboBox.removeAll ();
		// TODO Need to decide what to put here...
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
		/* TODO
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
    else if ( __selected_input_type.equals ( __INPUT_TYPE_HECDSS ) ) {
        // Prompt for a HEC-DSS file and update choices...
        uiAction_SelectInputName_HECDSS ( true );
    }
	else if ( __selected_input_type.equals ( __INPUT_TYPE_HydroBase )) {
		// Input name cleared and disabled...
		__input_name_JComboBox.removeAll();
		__input_name_JComboBox.setEnabled ( false );
		// Data type - get the time series choices from the HydroBase_Util code...
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

		// Select the default (this causes the other choices to be updated)...

		__data_type_JComboBox.select( null );
		__data_type_JComboBox.select(HydroBase_Util.getDefaultTimeSeriesDataType(__hbdmi, true ) );

		// Initialize with blank data vector...

		try {
		    __query_TableModel = new TSTool_HydroBase_TableModel(
			__query_JWorksheet, StringUtil.atoi(__props.getValue("HydroBase.WDIDLength")), null);
			TSTool_HydroBase_CellRenderer cr =
			new TSTool_HydroBase_CellRenderer((TSTool_HydroBase_TableModel)__query_TableModel);
			__query_JWorksheet.setCellRenderer ( cr );
			__query_JWorksheet.setModel ( __query_TableModel );
			__query_JWorksheet.setColumnWidths (cr.getColumnWidths() );
		}
		catch ( Exception e ) {
			// Absorb the exception in most cases - print if
			// developing to see if this issue can be resolved.
			if ( Message.isDebugOn && IOUtil.testing()  ) {
				Message.printWarning ( 3, routine,
				"For developers:  caught exception blanking HydroBase JWorksheet at setup." );
				Message.printWarning ( 3, routine, e );
		}
	}
	}
	else if ( __selected_input_type.equals ( __INPUT_TYPE_MEXICO_CSMN ) ) {
		// Most information is determined from the file but let the user limit the time series that will be liste...
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
		TSTool_TS_CellRenderer cr = new TSTool_TS_CellRenderer(	(TSTool_TS_TableModel)__query_TableModel);
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
		TSTool_TS_CellRenderer cr = new TSTool_TS_CellRenderer((TSTool_TS_TableModel)__query_TableModel);
		__query_JWorksheet.setCellRenderer ( cr );
		__query_JWorksheet.setModel ( __query_TableModel );
		// Remove columns that are not appropriate...
		__query_JWorksheet.removeColumn ( ((TSTool_TS_TableModel)__query_TableModel).COL_SEQUENCE );
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
		TSTool_TS_CellRenderer cr = new TSTool_TS_CellRenderer(	(TSTool_TS_TableModel)__query_TableModel);
		__query_JWorksheet.setCellRenderer ( cr );
		__query_JWorksheet.setModel ( __query_TableModel );
		// Remove columns that are not appropriate...
		__query_JWorksheet.removeColumn ( ((TSTool_TS_TableModel)__query_TableModel).COL_SEQUENCE );
		__query_JWorksheet.setColumnWidths ( cr.getColumnWidths() );
	}
	else if ( __selected_input_type.equals ( __INPUT_TYPE_NWSRFS_FS5Files)){
		// Update the input name and let the user choose Apps Defaults or pick a directory...
		ui_SetIgnoreItemEvent ( true );		// Do this to prevent item event cascade
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
			__input_name_JComboBox.add ( (String)__input_name_NWSRFS_FS5Files.elementAt(i) );
        }
		// Try to select the current DMI (it should always work if the logic is correct)...

		boolean choice_ok = false;	// Needed to force data types to cascade correctly...
		if ( __nwsrfs_dmi != null ) {
			String path = __nwsrfs_dmi.getFS5FilesLocation();
			try {
                __input_name_JComboBox.select ( path );
				choice_ok = true;
			}
			catch ( Exception e ) {
				Message.printWarning ( 2, routine,
                        "Unable to select current NWSRFS FS5Files \"" +	path + "\" in input names." );
			}
		}
		__input_name_JComboBox.setEnabled ( true );

		// Enable when an input name is picked...

		__data_type_JComboBox.setEnabled ( false );
		__data_type_JComboBox.removeAll ();

		// Enable when a data type is picked...

		__time_step_JComboBox.setEnabled ( false );
		__time_step_JComboBox.removeAll ();

		ui_SetIgnoreItemEvent ( false );		// Item events OK again

		if ( (__input_name_JComboBox.getItemCount() == 1) && (__input_name_JComboBox.getSelected().equals(__BROWSE))){
			// Only Browse is in the list so if the user picks, the
			// component does not generate an event because the choice has not changed.
			try {
                uiAction_SelectInputName_NWSRFS_FS5Files ( false );
			}
			catch ( Exception e ) {
				Message.printWarning ( 1, routine, "Error opening NWSRFS FS5Files connection." );
				Message.printWarning ( 2, routine, e );
			}
		}
		else if ( choice_ok ) {
			// The current open DMI was able to be selected above. Need to force the data types to cascade...
			uiAction_InputNameChoiceClicked();
		}

		// Display the NWSRFS input filters...
		ui_SetInputFilters();

		// Initialize with blank data vector...

		__query_TableModel = new TSTool_TS_TableModel(null);
		TSTool_TS_CellRenderer cr = new TSTool_TS_CellRenderer( (TSTool_TS_TableModel)__query_TableModel);
		__query_JWorksheet.setCellRenderer ( cr );
		__query_JWorksheet.setModel ( __query_TableModel );
		// Remove columns that are not appropriate...
		__query_JWorksheet.removeColumn ( ((TSTool_TS_TableModel)__query_TableModel).COL_SEQUENCE );
		__query_JWorksheet.setColumnWidths ( cr.getColumnWidths() );
	}
	else if(__selected_input_type.equals ( __INPUT_TYPE_NWSRFS_ESPTraceEnsemble)) {
		// Most information is determined from the file...
		__input_name_JComboBox.removeAll();
		__input_name_JComboBox.setEnabled ( false );

		__data_type_JComboBox.setEnabled ( false );
		__data_type_JComboBox.removeAll ();
		__data_type_JComboBox.add( __DATA_TYPE_AUTO );

		// Initialize with blank data vector...

		__query_TableModel = new TSTool_TS_TableModel ( null, true );
		TSTool_TS_CellRenderer cr = new TSTool_TS_CellRenderer( (TSTool_TS_TableModel)__query_TableModel);
		__query_JWorksheet.setCellRenderer ( cr );
		__query_JWorksheet.setModel ( __query_TableModel );
		__query_JWorksheet.setColumnWidths ( cr.getColumnWidths() );
	}
	else if ( __selected_input_type.equals ( __INPUT_TYPE_RiversideDB ) ) {
		__data_type_JComboBox.setEnabled ( true );
		__data_type_JComboBox.removeAll ();
		Vector mts = null;
		Vector dts = null;
		try {
            mts = __rdmi.readMeasTypeListForDistinctData_type();
			dts = __rdmi.readDataTypeList();
		}
		catch ( Exception e ) {
			Message.printWarning ( 2, routine, e );
			Message.printWarning ( 2, routine, __rdmi.getLastSQLString() );
			mts = null;
		}
		int size = 0;
		if ( mts != null ) {
			size = mts.size();
		}
		if ( size > 0 ) {
			RiversideDB_MeasType mt = null;
			int pos;
			String data_type;
			for ( int i = 0; i < size; i++ ) {
				mt = (RiversideDB_MeasType)mts.elementAt(i);
				pos = RiversideDB_DataType.indexOf (dts, mt.getData_type() );
				if ( pos < 0 ) {
					__data_type_JComboBox.add(mt.getData_type() );
				}
				else {
                    data_type = mt.getData_type() +	" - " + ((RiversideDB_DataType)dts.elementAt(pos)).getDescription();
					if ( data_type.length() > 30 ) {
						__data_type_JComboBox.add( data_type.substring(0,30) + "..." );
					}
					else {
                        __data_type_JComboBox.add( data_type );
					}
				}
			}
			__data_type_JComboBox.select ( null );
			__data_type_JComboBox.select ( 0 );
		}

		// Default to first in the list...
		//__data_type_JComboBox.select( 0 );

		/* TODO
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
			new TSTool_RiversideDB_CellRenderer((TSTool_RiversideDB_TableModel)__query_TableModel);
		__query_JWorksheet.setCellRenderer ( cr );
		__query_JWorksheet.setModel ( __query_TableModel );
		// Remove columns that are not appropriate...
		//__query_JWorksheet.removeColumn (((TSTool_RiversideDB_TableModel)__query_TableModel).COL_SEQUENCE );
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
		TSTool_TS_CellRenderer cr = new TSTool_TS_CellRenderer( (TSTool_TS_TableModel)__query_TableModel);
		__query_JWorksheet.setCellRenderer ( cr );
		__query_JWorksheet.setModel ( __query_TableModel );
		// Remove columns that are not appropriate...
		__query_JWorksheet.removeColumn ( ((TSTool_TS_TableModel)__query_TableModel).COL_SEQUENCE );
		__query_JWorksheet.setColumnWidths ( cr.getColumnWidths() );
	}
	else if ( __selected_input_type.equals ( __INPUT_TYPE_StateCU ) ) {
		// Prompt for a StateCU file and update choices...
		uiAction_SelectInputName_StateCU ( true );
	}
    else if ( __selected_input_type.equals ( __INPUT_TYPE_StateCUB ) ) {
        // Prompt for a StateCU binary file and update choices...
        uiAction_SelectInputName_StateCUB ( true );
    }
	else if ( __selected_input_type.equals ( __INPUT_TYPE_StateMod ) ) {
		// We disable all but the time step so the user can pick from appropriate files...
		__input_name_JComboBox.removeAll();
		__input_name_JComboBox.setEnabled ( false );

		__data_type_JComboBox.setEnabled ( false );
		__data_type_JComboBox.removeAll ();
		__data_type_JComboBox.add( __DATA_TYPE_AUTO );
		__data_type_JComboBox.select ( null );
		__data_type_JComboBox.select ( 0 );

		// Initialize with blank data vector...

		__query_TableModel = new TSTool_TS_TableModel(null);
		TSTool_TS_CellRenderer cr = new TSTool_TS_CellRenderer( (TSTool_TS_TableModel)__query_TableModel);
		__query_JWorksheet.setCellRenderer ( cr );
		__query_JWorksheet.setModel ( __query_TableModel );
		// Turn off columns in the table model that do not apply...
		__query_JWorksheet.removeColumn (((TSTool_TS_TableModel)__query_TableModel).COL_ALIAS );
		__query_JWorksheet.removeColumn (((TSTool_TS_TableModel)__query_TableModel).COL_DATA_SOURCE );
		__query_JWorksheet.removeColumn (((TSTool_TS_TableModel)__query_TableModel).COL_DATA_TYPE );
		__query_JWorksheet.removeColumn (((TSTool_TS_TableModel)__query_TableModel).COL_SCENARIO);
		__query_JWorksheet.removeColumn (((TSTool_TS_TableModel)__query_TableModel).COL_SEQUENCE );
		__query_JWorksheet.setColumnWidths ( cr.getColumnWidths() );
	}
	else if ( __selected_input_type.equals ( __INPUT_TYPE_StateModB ) ) {
		// Prompt for a StateMod binary file and update choices...
		uiAction_SelectInputName_StateModB ( true );
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
		TSTool_TS_CellRenderer cr = new TSTool_TS_CellRenderer((TSTool_TS_TableModel)__query_TableModel);
		__query_JWorksheet.setCellRenderer ( cr );
		__query_JWorksheet.setModel ( __query_TableModel );
		// Turn off columns in the table model that do not apply...
		__query_JWorksheet.removeColumn (((TSTool_TS_TableModel)__query_TableModel).COL_ALIAS );
		__query_JWorksheet.removeColumn (((TSTool_TS_TableModel)__query_TableModel).COL_DATA_SOURCE );
		__query_JWorksheet.removeColumn (((TSTool_TS_TableModel)__query_TableModel).COL_DATA_TYPE );
		__query_JWorksheet.removeColumn (((TSTool_TS_TableModel)__query_TableModel).COL_SCENARIO);
		__query_JWorksheet.removeColumn ( ((TSTool_TS_TableModel)__query_TableModel).COL_SEQUENCE );
		__query_JWorksheet.setColumnWidths ( cr.getColumnWidths() );
	}
	else {	// Not a recognized input source...
		Message.printWarning ( 1, routine,"\"" + __selected_input_type + "\" is not a recognized input source." );
		return;
	}

	}
	catch ( Exception e ) {
		Message.printWarning ( 2, routine, e );
	}

	routine = null;
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
private void uiAction_OpenColoradoSMS ( boolean startup )
{	String routine = "TSTool_JFrame.openColoradoSMS";
	Message.printStatus ( 1, routine, "Opening ColoradoSMS connection..." );
	// TODO SAM 2005-10-18
	// Always connect, whether in batch mode or not.  Might need a way to
	// configure this.
	//if ( IOUtil.isBatch() || !__show_main ) {
		// Running in batch mode or without a main GUI so automatically
		// open HydroBase from the CDSS.cfg file information...
		// Get the input needed to process the file...
		String cfg = SatMonSys_Util.getConfigurationFile();
		PropList props = null;
		if ( IOUtil.fileExists(cfg) ) {
			// Use the configuration file to get ColoradoSMS properties...
			try {
                props = SatMonSys_Util.readConfiguration(cfg);
			}
			catch ( Exception e ) {
				Message.printWarning ( 1, routine, "Error reading ColoradoSMS configuration " + "file \""+ cfg +
				"\".  Using defaults for ColoradoSMS." );
				Message.printWarning ( 3, routine, e );
				props = null;
			}
		}
		// Override with any TSTool command-line arguments, in particular the user login...
		String propval = TSToolMain.getPropValue("ColoradoSMS.UserLogin" );
		if ( propval != null ) {
			props.set ( "ColoradoSMS.UserLogin", propval );
			Message.printStatus ( 1, routine, "Using batch login ColoradoSMS.UserLogin=\"" + propval + "\"" );
		}
		try {
            // Now open the database...
			// This uses the guest login.  If properties were not found,
            // then default ColoradoSMS information will be used.
			__smsdmi = new SatMonSysDMI ( props );
			__smsdmi.open();
		}
		catch ( Exception e ) {
			Message.printWarning ( 1, routine,
                    "Error opening ColoradoSMS database.  ColoradoSMS features will be disabled." );
			Message.printWarning ( 3, routine, e );
			__smsdmi = null;
		}
	//}
	// TODO SAM 2005 Currently don't support an interactive login.
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
	ui_CheckColoradoSMSFeatures();
}

/**
Open a command file and read into the list of commands.  A check is made to
see if the list contains anything and if it does the user is prompted as to
whether need to save the previous commands.
*/
private void uiAction_OpenCommandFile ()
{	String routine = getClass().getName() + ".openCommandFile";
	// See whether the old commands need to be cleared...
	if ( __commands_dirty ) {
		if ( __command_file_name == null ) {
			// Have not been saved before.
		    // Always allow save, even if read-only comment is set (since first save).
			int x = ResponseJDialog.NO;
			if ( __commands_JListModel.size() > 0 ) {
				x = new ResponseJDialog ( this,	IOUtil.getProgramName(),
				"Do you want to save the changes you made?",
				ResponseJDialog.YES| ResponseJDialog.NO|ResponseJDialog.CANCEL).response();
			}
			if ( x == ResponseJDialog.CANCEL ) {
				return;
			}
			else if ( x == ResponseJDialog.YES ) {
				// Prompt for the name and then save...
				uiAction_WriteCommandFile ( __command_file_name, true);
			}
		}
		else {
			// A command file exists...  Warn the user.  They can
			// save to the existing file name or can cancel and
			// File...Save As... to a different name.
			// Have not been saved before...
			int x = ResponseJDialog.NO;
			if ( __commands_JListModel.size() > 0 ) {
			    if ( __ts_processor.getReadOnly() ) {
                    x = new ResponseJDialog ( this, IOUtil.getProgramName(),
                        "Do you want to save the changes you made to:\n"
                        + "\"" + __command_file_name + "\"?\n\n" +
                        "The commands are marked read-only.\n" +
                        "Press Yes to update the read-only file before opening a new file.\n" +
                        "Press No to discard edits before opening a new file.\n" +
                        "Press Cancel and then save to a new name if desired.\n",
                        ResponseJDialog.YES|ResponseJDialog.NO|ResponseJDialog.CANCEL).response();
			    }
			    else {
    				x = new ResponseJDialog ( this,	IOUtil.getProgramName(),
    				"Do you want to save the changes you made to:\n"
    				+ "\"" + __command_file_name + "\"?",
    				ResponseJDialog.YES| ResponseJDialog.NO|ResponseJDialog.CANCEL).response();
			    }
			}
			if ( x == ResponseJDialog.CANCEL ) {
				return;
			}
			else if ( x == ResponseJDialog.YES ) {
				uiAction_WriteCommandFile ( __command_file_name,false);
			}
			// Else if No or OK will clear below before opening the other file...
		}
	}

	// Get the file.  Do not clear the list until the file has been chosen and is readable...

	String initial_dir = ui_GetDir_LastCommandFileOpened();
	Message.printStatus ( 2, routine, "Initial directory for browsing:  \"" + initial_dir + "\"" );
	JFileChooser fc = JFileChooserFactory.createJFileChooser ( initial_dir );
	fc.setDialogTitle("Open " + IOUtil.getProgramName() + " Command File");
	SimpleFileFilter sff = new SimpleFileFilter("TSTool", "TSTool Command File");
	fc.addChoosableFileFilter(sff);
	fc.setFileFilter(sff);
	if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
		// If the user approves a selection do the following...
		String directory = fc.getSelectedFile().getParent();
		String path = fc.getSelectedFile().getPath();

		// TODO - is this necessary in Swing?
		// Set the "WorkingDir" property, which will NOT contain a trailing separator...
		IOUtil.setProgramWorkingDir(directory);
		ui_SetDir_LastCommandFileOpened(directory);
		__props.set ("WorkingDir=" + IOUtil.getProgramWorkingDir());
		ui_SetInitialWorkingDir ( __props.getValue ( "WorkingDir" ) );
		Message.printStatus(2, routine, "Working directory from command file is \"" +
			IOUtil.getProgramWorkingDir() );
		// Load but do not automatically run.
		ui_LoadCommandFile ( path, false );
	}
	// New file has been opened or there was a cancel/error and the old list remains.
	Message.printStatus ( 2, routine, "Done reading commands.  Calling ui_UpdateStatus...");
	ui_UpdateStatus ( true );
	Message.printStatus ( 2, routine, "Back from update status." );
}

/**
Create new DIADvisorDMI instances with connections to the DIADvisor operational
and archive databases.  This is done in response to a user request.
*/
private void uiAction_OpenDIADvisor ()
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
	try {
        selectDIADvisorDialog = new SelectDIADvisorDialog ( this, __DIADvisor_dmi, __DIADvisor_archive_dmi, props );

		// After getting to here, the dialog has been closed.  The
		// HydroBaseDMI from the dialog can be retrieved and used...

		__DIADvisor_dmi = selectDIADvisorDialog.getDIADvisorDMI();
		__DIADvisor_archive_dmi = selectDIADvisorDialog.getArchiveDIADvisorDMI();
		if ( __DIADvisor_dmi == null ) {
			Message.printWarning ( 1, routine,
			"Cannot open DIADvisor operational database.\nDIADvisor features will be disabled." );
			__DIADvisor_archive_dmi = null;
		}
		else if ( __DIADvisor_archive_dmi == null ) {
			Message.printWarning ( 1, routine,
			"Cannot open DIADvisor archive database.\nArchive data will not be available." );
		}
	}
	catch ( Exception e ) {
		Message.printWarning ( 1, routine, "DIADvisor features will be disabled." );
		Message.printWarning ( 2, routine, e );
		__DIADvisor_dmi = null;
		__DIADvisor_archive_dmi = null;
	}

	ui_CheckDIADvisorFeatures ();
}

/**
Open a connection to the HydroBase database.
@param startup If true, indicates that the database connection is being made
at startup.  This is the case, for example, when multiple HydroBase databases
may be available and there is no reason to automatically connect to one of them
for all users.
*/
private void uiAction_OpenHydroBase ( boolean startup )
{	String routine = "TSTool_JFrame.uiAction_OpenHydroBase";
	Message.printStatus ( 1, routine, "Opening HydroBase connection..." );

    // Running interactively so prompt the user to select and login to HydroBase.
	// This is a modal dialog that will not allow anything else to occur until the
	// information is entered.  Use a PropList to pass information because there are many
	// parameters that may change in the future.

	PropList hb_props = new PropList ( "SelectHydroBase" );
	hb_props.set ( "ValidateLogin", "false" );
	hb_props.set ( "ShowWaterDivisions", "false" );

	// Pass in the previous HydroBaseDMI so that its information
	// can be displayed as the initial values...

	SelectHydroBaseJDialog selectHydroBaseJDialog = null;
	try {
        // Let the dialog check HydroBase properties in the CDSS configuration file...
		selectHydroBaseJDialog = new SelectHydroBaseJDialog ( this, __hbdmi, hb_props );
		// After getting to here, the dialog has been closed.
		// The HydroBaseDMI from the dialog can be retrieved and used...
		__hbdmi = selectHydroBaseJDialog.getHydroBaseDMI();
		if ( __hbdmi == null ) {
			Message.printWarning ( 1, routine, "HydroBase features will be disabled." );
		}
	}
	catch ( Exception e ) {
		Message.printWarning ( 1, routine,
                "Error opening HydroBase connection.  HydroBase features will be disabled." );
		Message.printWarning ( 3, routine, e );
		__hbdmi = null;
	}

	// Set the HydroBaseDMI for the command processor...
	commandProcessor_SetHydroBaseDMI ( __hbdmi );
	// Enable/disable HydroBase features as necessary...
	ui_CheckHydroBaseFeatures();
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
private void uiAction_OpenNWSRFSFS5Files ( PropList props, boolean startup )
{	String routine = "TSTool_JFrame.openNWSRFSFS5Files";
	// First close the existing connection...
	if ( __nwsrfs_dmi != null ) {
		try {
            __nwsrfs_dmi.close();
		}
		catch ( Exception e ) {
			// Ignore - any reason not to?
		}
	}
	try {
        String	UseAppsDefaults = null,
		InputName = null;

		// Get the database connect information...

		if ( props == null ) {
			UseAppsDefaults = TSToolMain.getPropValue("NWSRFSFS5Files.UseAppsDefaults");
		}
		else {
            UseAppsDefaults = props.getValue( "NWSRFSFS5Files.UseAppsDefaults");
		}

		if ( props == null ) {
			InputName = TSToolMain.getPropValue("NWSRFSFS5Files.InputName");
		}
		else {
            InputName = props.getValue(	"NWSRFSFS5Files.InputName");
		}
		
		// If no configuration information is in the config file, do
		// not open a connection here...

		if ( (UseAppsDefaults != null) && (UseAppsDefaults.equalsIgnoreCase("true") ||
			(UseAppsDefaults.equalsIgnoreCase("false") && (InputName != null))) ) {
			JGUIUtil.setWaitCursor ( this, true );
			if ( UseAppsDefaults.equalsIgnoreCase("true") ) {
				Message.printStatus ( 1, routine,
				"Opening connection to NWSRFS FS5Files using Apps Defaults..." );
				__nwsrfs_dmi = new NWSRFS_DMI ();
			}
			else {
                Message.printStatus ( 1, routine,
				"Opening connection to NWSRFS FS5Files using path \"" + InputName + "\"..." );
				__nwsrfs_dmi = new NWSRFS_DMI ( InputName );
			}
			// Now open the connection...
			__nwsrfs_dmi.open();
			// Add the FS5Files name to the list known to the
			// interface.  This will allow the user to pick this
			// in the GUI later without reopening...
			String path = __nwsrfs_dmi.getFS5FilesLocation();
			__input_name_NWSRFS_FS5Files.addElement ( path );
			// For now there is no way for the user to set a separate
			// input name so set the same as the FS5 Files location...
			// Is automatically set.
			//__nwsrfs_dmi.setInputName ( path );
			// Set the NWSRFSDMI for the command processor...
			commandProcessor_SetNWSRFSFS5FilesDMI ( __nwsrfs_dmi );
			// Check the GUI state to enable/disable NWSRFS FS5Files properties...
			ui_CheckNWSRFSFS5FilesFeatures ();
			JGUIUtil.setWaitCursor ( this, false );
		}

	}
	catch ( Exception e ) {
		Message.printWarning ( 1, routine, "Unable to open NWSRFS FS5Files database.");
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
private void uiAction_OpenRiversideDB ( PropList props, boolean startup )
{	String routine = "TSTool_JFrame.openRiversideDB";
	// First close the existing connection...
	if ( __rdmi != null ) {
		try {
            __rdmi.close();
		}
		catch ( Exception e ) {
			// Ignore - any reason not to?
		}
	}
	try {
        String	connect_method = null,
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
			connect_method = TSToolMain.getPropValue("RiversideDB.JavaConnectMethod");
		}
		else {	// Newer...
			connect_method = props.getValue("RiversideDB.JavaConnectMethod");
		}
		if ( connect_method == null ) {
			connect_method = "JDBCODBC";
		}

		// First get the database engine (required)...

		if ( props == null ) {
			database_engine = TSToolMain.getPropValue("RiversideDB.DatabaseEngine" );
		}
		else {
            database_engine = props.getValue("RiversideDB.DatabaseEngine");
		}
		if ( database_engine == null ) {
			errors += "\nRiversideDB.DatabaseEngine is not defined.";
			++error_count;
		}
		else {
            ++needed_prop_count;
		}

		// Now get the properties for the connect method...

		if ( connect_method.equalsIgnoreCase ( "JDBCODBC" ) ) {
			// Database server name (required)...
			if ( props == null ) {
				database_server = TSToolMain.getPropValue( "RiversideDB.JavaDatabaseServer" );
			}
			else {
                database_server = props.getValue( "RiversideDB.JavaDatabaseServer");
			}
			if ( database_server == null ) {
				errors += "\nRiversideDB.JavaDatabaseServer is not defined.";
				++error_count;
			}
			else {
                ++needed_prop_count;
			}
		}

		// The database name is used for both "ODBC" and "JDBCODBC"
		// connect methods (required)...

		if ( props == null ) {
			database_name = TSToolMain.getPropValue("RiversideDB.JavaDatabase" );
		}
		else {
            database_name = props.getValue(	"RiversideDB.JavaDatabase" );
		}
		if ( database_name == null ) {
			errors += "\nRiversideDB.JavaDatabase is not defined.";
			++error_count;
		}
		else {
            ++needed_prop_count;
		}

		// Now get the system login and password to use
		// (optional - will default)...

		if ( props == null ) {
			// Newer...
			system_login = TSToolMain.getPropValue(	"RiversideDB.SystemLogin");
			if ( system_login == null ) {
				// Older...
				system_login = TSToolMain.getPropValue("RiversideDB.Login");
			}
		}
		else {	// Newer...
			system_login=props.getValue("RiversideDB.SystemLogin");
			if ( system_login == null ) {
				// Older...
				system_login = props.getValue (	"RiversideDB.Login");
			}
		}
		if ( props == null ) {
			// Newer...
			system_password = TSToolMain.getPropValue("RiversideDB.SystemPassword");
			if ( system_password == null ) {
				// Older...
				system_password = TSToolMain.getPropValue("RiversideDB.Password");
			}
		}
		else {	// Newer...
			system_password = props.getValue("RiversideDB.SystemPassword");
			if ( system_password == null ) {
				// Older...
				system_password = props.getValue("RiversideDB.Password");
			}
		}

		// Check for configuration errors...

		if ( error_count > 0 ) {
			if ( !startup || (startup && (needed_prop_count > 0) ) ) {
				// A startup condition where RiversideDB
				// properties have been given that are incorrect
				// and should be corrected... OR...
				// A startup condition where RiversideDB
				// properties are not given - no error because
				// we expect the user to specify connection
				// information later.
				errors += "\n\nError in RiversideDB configuration properties.";
				errors += "\nUnable to open RiversideDB database.";
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
		else {
            __rdmi = new RiversideDB_DMI ( database_engine,
					database_server, database_name,
					-1, system_login, system_password );
		}

		// Now open the connection...
		__rdmi.open();
	}
	catch ( Exception e ) {
		Message.printWarning ( 1, routine, "Unable to open RiversideDB database. ");
		Message.printWarning( 2, routine, e );
		// Set the DMI to null so that features will be turned on but
		// still allow the RiversideDB input type to be enabled so that
		// it can be tried again.
		__rdmi = null;
	}

	// Check the GUI state to enable/disable RiversideDB properties...

	ui_CheckRiversideDBFeatures ();

	int login = LoginJDialog.LOGIN_EXCEEDED_MAX;
	try {
		while (login != LoginJDialog.LOGIN_CANCELLED 
		    && login != LoginJDialog.LOGIN_OK) {
			login = new LoginJDialog(this, "Riverside Login", 
				__rdmi).response();
		}
	}
	catch (Exception e) {
		Message.printWarning(2, routine, "Error opening login dialog.");
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
    
    // Set the HydroBaseDMI for the command processor...
    commandProcessor_SetRiversideDB_DMI ( __rdmi );
    // Enable/disable HydroBase features as necessary...
    ui_CheckRiversideDBFeatures();
}

/**
Paste the cut buffer containing Command instances that were previously cut or
copied, inserting after the selected item.
*/
private void uiAction_PasteFromCutBufferToCommandList ()
{	// Default selected to zero (empty list)...
	int last_selected = -1;
	int list_size = __commands_JListModel.size();

	// Get the list of selected items...
	int [] selected_indices = ui_GetCommandJList().getSelectedIndices();
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

	Command command = null;
	for ( int i = 0; i < buffersize; i++ ) {
		command = (Command)__commands_cut_buffer.elementAt(i);
		commandList_InsertCommandAt ( command, (last_selected + 1 + i) );
	}

	// Leave in the buffer so it can be pasted again.

	commandList_SetDirty ( true );
	ui_UpdateStatus ( true );
}

/**
Method to read a TSProduct file and process the product.
@param show_view If true, the graph will be displayed on the screen.  If false,
an output file will be prompted for.
*/
private void uiAction_ProcessTSProductFile ( boolean view_gui )
throws Exception
{	JFileChooser fc = JFileChooserFactory.createJFileChooser ( JGUIUtil.getLastFileDialogDirectory() );
	fc.setDialogTitle ( "Select TS Product File" );
	SimpleFileFilter sff = new SimpleFileFilter ( "tsp", "Time Series Product File" );
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
	if ( __ts_processor != null ) {
		// Time series should be in memory so add the TSCommandProcessor as a
		// supplier.  This should result in quick supplying of in-memory time series...
		p.addTSSupplier ( __ts_processor );
	}
	// Always add a new TSEngine that will be able to read time series that
	// are not currently in memory.  The main trick is to set the working
	// directory.  However the working directory will be set in memory in
	// IOUtil so just declare a TSEngine.  Don't pass any commands.
	//
	// If no working directory has been set, we could set from the file
	// selection, but how to detect when to do this?
	/* TODO SAM 2007-08-20 Evaluate whether needed
	 * Won't the above read the time series if necessary?
	TSEngine supplier_tsengine = new TSEngine ( __hbdmi, __rdmi,
					__DIADvisor_dmi,
					__DIADvisor_archive_dmi,
					__nwsrfs_dmi, __smsdmi,
					null, this );

	p.addTSSupplier ( supplier_tsengine );
		*/
	
	// Now process the product...
	
	p.processProduct ( path, override_props );
}

/**
Run the commands in the command list.  These time series are saved in
__ts_processor and are then available for export, analysis, or viewing.  This
method should only be called if there are commands in the command list.
@param run_all_commands If false, then only the selected commands are run.  If
true, then all commands are run.
@param create_output If true, then all write* methods are processed by the
TSEngine.  If false, only the time series in memory remain at the end and can
be viewed.  The former is suitable for batch files, both for the GUI.
*/
private void uiAction_RunCommands ( boolean run_all_commands, boolean create_output )
{	String routine = "TSTool_JFrame.uiAction_RunCommands";
	ui_UpdateStatusTextFields ( 1, routine, null, "Running commands...", __STATUS_BUSY);
	results_Clear ();
	System.gc();
	// Get commands to run (all or selected)...
	Vector commands = commandList_GetCommands ( run_all_commands );
	// The limits of the command progress bar are handled in commandStarted().
	// Save the commands in case any output calls IOUtil.printCreatorHeader...
	// FIXME SAM 2008-09-04 Confirm no negative effects from taking this out
	//IOUtil.setProgramCommandList ( commandList_ToStringVector(commands) );
	// Run the commands in the processor instance.
	if ( ui_Property_RunCommandProcessorInThread() ) {
		// Run the commands in a thread.
		commandProcessor_RunCommandsThreaded ( commands, create_output );
		// List of time series is displayed when CommandProcessorListener
		// detects that the last command is complete.
	}
	else {
		// Run the commands in the current thread (the GUI will be unresponsive during this time).
		JGUIUtil.setWaitCursor ( this, true );
		commandProcessor_RunCommands ( __ts_processor, commands, create_output );
		// Display the list of time series...
		uiAction_RunCommands_ShowResults ();
		JGUIUtil.setWaitCursor ( this, false );
	}
}

/**
Display the results of the run, time series and output files.
This is handled as a "pinch point" in hand-off from the processor and the UI, to try
to gracefully handle displaying output.
*/
private void uiAction_RunCommands_ShowResults()
{
	// This method may be called from a thread different than the Swing thread.  To
	// avoid bad behavior in GUI components (like the results list having big gaps),
	// use the following to queue up GUI actions on the Swing thread.
	
	Runnable r = new Runnable() {
		public void run() {
            // Close the regression results report if it is open (have to do here because
            // layers of recursion can occur when running a commands file)...
            TSCommandProcessorUtil.closeRegressionTestReportFile();
			results_Clear();
			uiAction_RunCommands_ShowResultsTimeSeries(); // JList
            uiAction_RunCommands_ShowResultsTables(); // JComboBox
			uiAction_RunCommands_ShowResultsOutputFiles(); // JComboBox
            uiAction_RunCommands_ShowResultsEnsembles(); // JList
            
            // Repaint the list to reflect the status of the commands...
            ui_ShowCurrentCommandListStatus ();
		}
	};
	if ( SwingUtilities.isEventDispatchThread() )
	{
		r.run();
	}
	else 
	{
		SwingUtilities.invokeLater ( r );
	}
}

/**
Display the ensembles from the command processor in the results list.
*/
private void uiAction_RunCommands_ShowResultsEnsembles ()
{   String routine = "TSTool_JFrame.uiAction_RunCommands_ShowResultsEnsembles";
    //Message.printStatus ( 2, "uiAction_RunCommands_ShowResultsEnsembles", "Entering method.");

    // Fill the ensemble list with the descriptions of the in-memory time series...
    int size = commandProcessor_GetEnsembleResultsListSize();
    Message.printStatus ( 2, routine, "Adding " + size + " ensembles to results." );
    TSEnsemble tsensemble = null;
    for ( int i = 0; i < size; i++ ) {
        try {
            tsensemble = commandProcessor_GetEnsembleAt(i);
        }
        catch ( Exception e ) {
            results_Ensembles_AddEnsembleToResults ( "" + (i + 1) + " - Error getting ensemble from processor." );
            Message.printWarning ( 3, routine, e );
            continue;
        }
        if ( tsensemble == null ) {
            results_Ensembles_AddEnsembleToResults ( "" + (i + 1)+ " - Null ensemble from processor." );
            continue;
        }
        else {
            // Have actual data to display...
            String id = tsensemble.getEnsembleID();
            String name = tsensemble.getEnsembleName();
            results_Ensembles_AddEnsembleToResults ( "" + (i + 1) + ") " + id + " - " + name );
            //+ " - " + ts.getIdentifier() + " (" + ts.getDate1() + " to " + ts.getDate2() + ")" );
        }
    }
    ui_UpdateStatus ( false );

    ui_UpdateStatusTextFields ( 1, routine, null, "Completed running commands.  Use Results and Tools menus.",
            __STATUS_READY );
    // Make sure that the user is not waiting on the wait cursor....
    //JGUIUtil.setWaitCursor ( this, false );
    
    //Message.printStatus ( 2, "uiAction_RunCommands_ShowResultsTimeSeries", "Leaving method.");
}

/**
Display the list of output files from the commands.
*/
private void uiAction_RunCommands_ShowResultsOutputFiles()
{	// Loop through the commands.  For any that implement the FileGenerator interface,
	// get the output file names and add to the list.
	// Only add a file if not already in the list (so append does not
	//Message.printStatus ( 2, "uiAction_RunCommands_ShowResultsOutputFiles", "Entering method.");
	int size = __commands_JListModel.size();
	Command command;
	ui_SetIgnoreActionEvent(true);
	for ( int i = 0; i < size; i++ ) {
		command = (Command)__commands_JListModel.get(i);
		if ( command instanceof FileGenerator ) {
			List list = ((FileGenerator)command).getGeneratedFileList();
			if ( list != null ) {
				int size2 = list.size();
				for ( int ifile = 0; ifile < size2; ifile++ ) {
					results_OutputFiles_AddOutputFile ( (File)list.get(ifile));
				}
			}
		}
	}
	ui_SetIgnoreActionEvent(false);
	//Message.printStatus ( 2, "uiAction_RunCommands_ShowResultsOutputFiles", "Leaving method.");
}

/**
Display the table results.
*/
private void uiAction_RunCommands_ShowResultsTables()
{   // Get the list of tables from the processor.
    //Message.printStatus ( 2, "uiAction_RunCommands_ShowResultsTables", "Entering method.");
    // Get the list of table identifiers from the processor.
    List table_List = commandProcessor_GetTableResultsList();
    int size = 0;
    if ( table_List != null ) {
        size = table_List.size();
    }
    ui_SetIgnoreActionEvent(true);
    DataTable table;
    for ( int i = 0; i < size; i++ ) {
        table = (DataTable)table_List.get(i);
        results_Tables_AddTable ( table );
    }
    ui_SetIgnoreActionEvent(false);
    //Message.printStatus ( 2, "uiAction_RunCommands_ShowResultsTables", "Leaving method.");
}

/**
Display the time series from the command processor in the results list.
*/
private void uiAction_RunCommands_ShowResultsTimeSeries ()
{	String routine = "TSTool_JFrame.uiAction_RunCommands_ShowResultsTimeSeries";
	//Message.printStatus ( 2, "uiAction_RunCommands_ShowResultsTimeSeries", "Entering method.");

	// Fill the time series list with the descriptions of the in-memory time series...
	int size = commandProcessor_GetTimeSeriesResultsListSize();
	Message.printStatus ( 2, routine, "Adding " + size + " time series to results." );
	TS ts = null;
	String desc = null;
	String alias = null;
	boolean [] selected_boolean = new boolean[size];   // Size to results list
	for ( int i = 0; i < size; i++ ) {
		selected_boolean[i] = false;
		try {
            ts = commandProcessor_GetTimeSeries(i);
		}
		catch ( Exception e ) {
			results_TimeSeries_AddTimeSeriesToResults ( "" + (i + 1) +
			") - Error getting time series from processor." );
			Message.printWarning ( 3, routine, e );
			continue;
		}
		if ( ts == null ) {
			results_TimeSeries_AddTimeSeriesToResults ( "" + (i + 1)+
			") - Null time series from processor." );
			continue;
		}
		else {
			// Have actual data to display...
			desc = ts.getDescription();
			alias = ts.getAlias();
			if ( !alias.equals("") ) {
				alias = alias + " - ";
			}
			if ( (desc == null) || (desc.length() == 0) ) {
				desc = ts.getIdentifier().getLocation();
			}
			results_TimeSeries_AddTimeSeriesToResults ( "" + (i + 1) + ") " + alias +
			desc + " - " + ts.getIdentifier() +	" (" + ts.getDate1() + " to " +	ts.getDate2() + ")" );
			// Determine whether the time series was programmatically selected in the commands...
			selected_boolean[i] = ts.isSelected();
		}
	}
	// If no time series are selected programatically, then visually select all.
	// If any are selected, then visually select only the ones that are
	// selected.  First determine the number that have been selected programatically.
	int num_selected = 0;
	for ( int i = 0; i < size; i++ ) {
		if ( selected_boolean[i] ) {
			++num_selected;
		}
	}
	// Now create the list of indices to use for visually selecting...
	int [] selected = null;
	if ( num_selected == 0 ) {
		// All need to be selected in output
		selected = new int[size];	// Whether visually selected
		for ( int i = 0; i < size; i++ ) {
			selected[i] = i;
		}
	}
	else {
        // Select the time series of interest.
		selected = new int[num_selected];	// Whether visually selected
		int selectedCount = 0;
		for ( int i = 0; i < size; i++ ) {
			if ( selected_boolean[i] ) {
				selected[selectedCount++] = i;
			}
		}
	}
	// Now actually select the time series in the visual output...
	__results_ts_JList.setSelectedIndices ( selected );
	ui_UpdateStatus ( false );
	// Repaint the list to reflect the status of the commands...
    ui_ShowCurrentCommandListStatus ();
	ui_UpdateStatusTextFields ( 1, routine, null, "Completed running commands.  Use Results and Tools menus.",
			__STATUS_READY );
	// Make sure that the user is not waiting on the wait cursor....
	//JGUIUtil.setWaitCursor ( this, false );
	
	//Message.printStatus ( 2, "uiAction_RunCommands_ShowResultsTimeSeries", "Leaving method.");
}

/**
Run a command file, independent of what is shown in the UI.
*/
private void uiAction_RunCommandFile ()
{	String routine = getClass().getName() + ".uiAction_RunCommandFile";
	// Select from the directory where the previous selection occurred.
	JFileChooser fc = JFileChooserFactory.createJFileChooser ( ui_GetDir_LastExternalCommandFileRun() );
	fc.setDialogTitle("Select " + IOUtil.getProgramName() + " Command File to Run");
	SimpleFileFilter sff = new SimpleFileFilter("TSTool", "TSTool Command File");
	fc.addChoosableFileFilter(sff);
	fc.setFileFilter(sff);
	if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
		// If the user approves a selection do the following...
		String directory = fc.getSelectedFile().getParent();
		String path = fc.getSelectedFile().getPath();
		ui_SetDir_LastExternalCommandFileRun(directory);
		// TODO SAM 2007-11-02 Put in thread - but need to figure out how to show completion.
		try { TSCommandFileRunner runner = new TSCommandFileRunner();
			JGUIUtil.setWaitCursor ( this, true );
			// Read the file
			runner.readCommandFile ( path );
			// Set the DMI information...
			commandProcessor_SetHydroBaseDMI ( runner.getProcessor(), ui_GetHydroBaseDMI() );
			commandProcessor_SetNWSRFSFS5FilesDMI ( runner.getProcessor(), ui_GetNWSRFSFS5FilesDMI() );
			// Now run the command file
			Message.printStatus ( 1, routine, "Running external command file \"" + path + "\"" );
			runner.runCommands();
			Message.printStatus ( 1, routine, "Successfully processed command file \"" + path + "\"" );
			JGUIUtil.setWaitCursor ( this, false );
		}
		catch ( Exception e ) {
			JGUIUtil.setWaitCursor ( this, false );
			Message.printWarning ( 1, routine, "Error running command file \"" + path + "\"" );
			Message.printWarning ( 2, routine, e );
			JGUIUtil.setWaitCursor ( this, false );
		}
	}
}

//TODO SAM 2007-08-31 - need to enable/disable filters based on the list of time series
//that are selected.
/**
Save the current time series to the selected format.  The user picks the file
and the format, via the chooser filter.
*/
private void uiAction_SaveTimeSeries ()
{	JFileChooser fc = JFileChooserFactory.createJFileChooser( JGUIUtil.getLastFileDialogDirectory() );
	fc.setDialogTitle("Save Time Series (full period)");
	// Default, and always enabled...
	SimpleFileFilter datevalue_sff = null;
	datevalue_sff=new SimpleFileFilter( "dv", "DateValue Time Series File");
	fc.addChoosableFileFilter( datevalue_sff );
	fc.setFileFilter(datevalue_sff);
	// Add ESP Trace Ensemble only if enabled in the configuration... 
	SimpleFileFilter esp_cs_sff = null;
	if ( __source_NWSRFS_ESPTraceEnsemble_enabled ) {
		esp_cs_sff = new SimpleFileFilter( "CS","ESP Conditional Trace Ensemble File");
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
		nwscard_sff = new SimpleFileFilter( "nwscard", "NWS Card Time Series File" );
		fc.addChoosableFileFilter( nwscard_sff );
*/
	}
	// Add RiverWare only if enabled in the configuration... 
	SimpleFileFilter riverware_sff = null;
	if ( __source_RiverWare_enabled ) {
		riverware_sff = new SimpleFileFilter("txt", "RiverWare Time Series Format File" );
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
		statemod_sff = new SimpleFileFilter("stm", "StateMod Time Series File" );
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
	// TODO - need to do this cleaner and perhaps let users pick some
	// output options.  Do enforce the file extensions.
	if ( ff == datevalue_sff ) {
		uiAction_ExportTimeSeriesResults("-odatevalue", IOUtil.enforceFileExtension(path,"dv") );
	}
	/*
	else if ( ff == nwscard_sff ) {
		// Don't know for sure what extension...
		export("-onwscard", path );
	}
	*/
	else if ( ff == esp_cs_sff ) {
		uiAction_ExportTimeSeriesResults("-onwsrfsesptraceensemble",
		IOUtil.enforceFileExtension(path,"CS") );
	}
	else if ( ff == riverware_sff ) {
		// Don't know for sure what extension...
		uiAction_ExportTimeSeriesResults("-oriverware", path );
	}
	else if ( ff == shef_sff ) {
		// Don't know for sure what extension...
		uiAction_ExportTimeSeriesResults("-oshefa", path );
	}
	else if ( ff == statemod_sff ) {
		// Don't know for sure what extension...
		uiAction_ExportTimeSeriesResults("-ostatemod", path );
	}
	else if ( ff == summary_sff ) {
		uiAction_ExportTimeSeriesResults("-osummary", IOUtil.enforceFileExtension(path,"txt") );
	}
	else {
		for (int i = 0; i < nwscardFilters.length; i++) {
			if (ff == nwscardFilters[i]) {
				uiAction_ExportTimeSeriesResults("-onwscard", path);
				break;
			}
		}
	}
}

/**
Select all commands in the commands list.  This occurs in response to a user
selecting a menu choice.
*/
private void uiAction_SelectAllCommands()
{	JGUIUtil.selectAll(ui_GetCommandJList());
	ui_UpdateStatus ( true );
}

/**
Prompt for a HECDSS input name (binary file name).  When selected, update the choices.
@param reset_input_names If true, the input names will be repopulated with
values from __input_name_HECDSS.
@exception Exception if there is an error.
*/
private void uiAction_SelectInputName_HECDSS ( boolean reset_input_names )
throws Exception
{   String routine = "TSTool_JFrame.selectInputName_HECDSS";
    if ( reset_input_names ) {
        // The HEC-DSS input type has been selected as a change from
        // another type.  Repopululate the list if previous choices exist...
        // TODO - probably not needed...
        //__input_name_JComboBox.removeAll();
        __input_name_JComboBox.setData ( __input_name_HECDSS );
    }
    // Check the item that is selected...
    String input_name = __input_name_JComboBox.getSelected();
    if ( (input_name == null) || input_name.equals(__BROWSE) ) {
        // Prompt for the name of a StateCU binary file...
        // Based on the file extension, set the data types and other information...
        JFileChooser fc = JFileChooserFactory.createJFileChooser ( JGUIUtil.getLastFileDialogDirectory() );
        fc.setDialogTitle("Select HEC-DSS Database File");
        SimpleFileFilter sff = new SimpleFileFilter("dss","HEC-DSS Database File");
        fc.addChoosableFileFilter( sff );
        // Only allow recognized extensions...
        fc.setAcceptAllFileFilterUsed ( false );
        if (fc.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
            // User cancelled - set the file name back to the original and disable other choices...
            if ( input_name != null ) {
                ui_SetIgnoreItemEvent ( true );
                __input_name_JComboBox.select(null);
                if ( __input_name_StateCUB_last != null ) {
                    __input_name_JComboBox.select ( __input_name_HECDSS_last );
                }
                ui_SetIgnoreItemEvent ( false );
            }
            return;
        }
        // User has chosen a file...

        input_name = fc.getSelectedFile().getPath(); 
        // Save as last selection...
        __input_name_HECDSS_last = input_name;
        JGUIUtil.setLastFileDialogDirectory (fc.getSelectedFile().getParent() );

        // Set the input name...

        ui_SetIgnoreItemEvent ( true );
        if ( !JGUIUtil.isSimpleJComboBoxItem (__input_name_JComboBox,__BROWSE, JGUIUtil.NONE, null, null ) ) {
            // Not already in so add it at the beginning...
            __input_name_HECDSS.addElement ( __BROWSE );
            __input_name_JComboBox.add ( __BROWSE );
        }
        if ( !JGUIUtil.isSimpleJComboBoxItem (__input_name_JComboBox,input_name, JGUIUtil.NONE, null, null ) ) {
            // Not already in so add after the browse string (files
            // are listed chronologically by select with most recent at the top...
            if ( __input_name_JComboBox.getItemCount() > 1 ) {
                __input_name_JComboBox.addAt ( input_name, 1 );
                __input_name_StateCUB.insertElementAt ( input_name, 1 );
            }
            else {
                __input_name_JComboBox.add ( input_name );
                __input_name_StateCUB.addElement(input_name);
            }
        }
        ui_SetIgnoreItemEvent ( false );
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

    Vector data_types = new Vector();
    int interval_base = TimeInterval.MONTH; // Default
    /* FIXME
    // TODO SAM 2006-01-15
    // The following is not overly efficient because of a transition in
    // StateMod versions.  The version of the format is determined from the
    // file.  For older versions, this is used to return hard-coded
    // parameter lists.  For newer formats, the binary file is reopened and
    // the parameters are determined from the file.
    data_types = StateCU_Util.getTimeSeriesDataTypes (
            input_name, // Name of binary file
            comp,   // Component from above, from file extension
            null,   // ID
            null,   // dataset
            StateCU_BTS.determineFileVersion(input_name),
            interval_base,
            false,  // Include input (only output here)
            false,  // Include input, estimated (only output here)
            true,   // Include output (what the binaries contain)
            false,  // Check availability
            true,   // Add group (if available)
            false );// add note
            */

    // Fill data types choice...

    Message.printStatus ( 2, routine, "Setting HEC-DSS data types..." );
    __data_type_JComboBox.setData ( data_types );
    Message.printStatus ( 2, routine, "Selecting the first HEC-DSS data type..." );
    __data_type_JComboBox.select ( null );
    if ( data_types.size() > 0 ) {
        __data_type_JComboBox.select ( 0 );
    }

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
    TSTool_TS_CellRenderer cr = new TSTool_TS_CellRenderer((TSTool_TS_TableModel)__query_TableModel);
    __query_JWorksheet.setCellRenderer ( cr );
    __query_JWorksheet.setModel ( __query_TableModel );
    // Turn off columns in the table model that do not apply...
    __query_JWorksheet.removeColumn (((TSTool_TS_TableModel)__query_TableModel).COL_ALIAS );
    __query_JWorksheet.removeColumn (((TSTool_TS_TableModel)__query_TableModel).COL_SCENARIO);
    __query_JWorksheet.removeColumn (((TSTool_TS_TableModel)__query_TableModel).COL_SEQUENCE);
    __query_JWorksheet.setColumnWidths ( cr.getColumnWidths() );
}

/**
Prompt for a NWSRFS FS5Files input name (FS5Files directory).  When selected,
update the choices.
@param reset_input_names If true, the input names will be repopulated with
values from __input_name_NWSRFS_FS5Files.
@exception Exception if there is an error.
*/
private void uiAction_SelectInputName_NWSRFS_FS5Files ( boolean reset_input_names )
throws Exception
{	String routine = "TSTool_JFrame.selectInputName_NWSRFS_FS5Files";
	// TODO SAM 2004-09-10 it does not seem like the following case ever
	// is true - need to decide whether to take out.
	if ( reset_input_names ) {
		// The NWSRFS_FS5Files input type has been selected as a change
		// from another type.  Repopululate the list if previous choices
		// exist...
		//int size = __input_name_NWSRFS_FS5Files.size();
		// TODO - probably not needed...
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
			ui_SetIgnoreItemEvent ( true );
			if ( input_name != null ) {
				__input_name_JComboBox.select(null);
				__input_name_JComboBox.select(input_name);
			}
			ui_SetIgnoreItemEvent ( false );
			return;
		}
		// User has chosen a directory...

		input_name = fc.getSelectedFile().getPath(); 
		JGUIUtil.setLastFileDialogDirectory (fc.getSelectedFile().getParent() );

		// Set the input name...

		ui_SetIgnoreItemEvent ( true );
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
		ui_SetIgnoreItemEvent ( false );
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
		// TODO SAM 2004-09-08 need to remove from the input name
		// list if an error?
	}
	
	// Set the data types and time step based on what is available...

	uiAction_InputNameChoiceClicked ();

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
private void uiAction_SelectInputName_StateCU ( boolean reset_input_names )
throws Exception
{	if ( reset_input_names ) {
		// The StateCU input type has been selected as a change from
		// another type.  Repopululate the list if previous choices
		// exist...
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
				ui_SetIgnoreItemEvent ( true );
				__input_name_JComboBox.select(null);
				if ( __input_name_StateCU_last != null ) {
					__input_name_JComboBox.select(
					__input_name_StateCU_last );
				}
				ui_SetIgnoreItemEvent ( false );
			}
			return;
		}

		// User has chosen a file...

		input_name = fc.getSelectedFile().getPath(); 
		// Save as last selection...
		__input_name_StateCU_last = input_name;
		__input_name_FileFilter = fc.getFileFilter();
		JGUIUtil.setLastFileDialogDirectory (fc.getSelectedFile().getParent() );

		// Set the input name...

		ui_SetIgnoreItemEvent ( true );
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
		ui_SetIgnoreItemEvent ( false );
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
Prompt for a StateCUB input name (binary file name).  When selected, update the choices.
@param reset_input_names If true, the input names will be repopulated with
values from __input_name_StateModB.
@exception Exception if there is an error.
*/
private void uiAction_SelectInputName_StateCUB ( boolean reset_input_names )
throws Exception
{   String routine = "TSTool_JFrame.selectInputName_StateCUB";
    if ( reset_input_names ) {
        // The StateCUB input type has been selected as a change from
        // another type.  Repopululate the list if previous choices exist...
        // TODO - probably not needed...
        //__input_name_JComboBox.removeAll();
        __input_name_JComboBox.setData ( __input_name_StateCUB );
    }
    // Check the item that is selected...
    String input_name = __input_name_JComboBox.getSelected();
    if ( (input_name == null) || input_name.equals(__BROWSE) ) {
        // Prompt for the name of a StateCU binary file...
        // Based on the file extension, set the data types and other information...
        JFileChooser fc = JFileChooserFactory.createJFileChooser ( JGUIUtil.getLastFileDialogDirectory() );
        fc.setDialogTitle("Select StateCU Binary Output File");
        SimpleFileFilter sff = new SimpleFileFilter("bd1","CU (Monthly)");
        fc.addChoosableFileFilter( sff );
        //fc.addChoosableFileFilter( new SimpleFileFilter("b49","Diversion, Stream, Instream stations (Daily)") );
        fc.setFileFilter(sff);
        // Only allow recognized extensions...
        fc.setAcceptAllFileFilterUsed ( false );
        if (fc.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
            // User cancelled - set the file name back to the original and disable other choices...
            if ( input_name != null ) {
                ui_SetIgnoreItemEvent ( true );
                __input_name_JComboBox.select(null);
                if ( __input_name_StateCUB_last != null ) {
                    __input_name_JComboBox.select ( __input_name_StateCUB_last );
                }
                ui_SetIgnoreItemEvent ( false );
            }
            return;
        }
        // User has chosen a file...

        input_name = fc.getSelectedFile().getPath(); 
        // Save as last selection...
        __input_name_StateCUB_last = input_name;
        JGUIUtil.setLastFileDialogDirectory (fc.getSelectedFile().getParent() );

        // Set the input name...

        ui_SetIgnoreItemEvent ( true );
        if ( !JGUIUtil.isSimpleJComboBoxItem (__input_name_JComboBox,__BROWSE, JGUIUtil.NONE, null, null ) ) {
            // Not already in so add it at the beginning...
            __input_name_StateCUB.addElement ( __BROWSE );
            __input_name_JComboBox.add ( __BROWSE );
        }
        if ( !JGUIUtil.isSimpleJComboBoxItem (__input_name_JComboBox,input_name, JGUIUtil.NONE, null, null ) ) {
            // Not already in so add after the browse string (files
            // are listed chronologically by select with most recent at the top...
            if ( __input_name_JComboBox.getItemCount() > 1 ) {
                __input_name_JComboBox.addAt ( input_name, 1 );
                __input_name_StateCUB.insertElementAt ( input_name, 1 );
            }
            else {
                __input_name_JComboBox.add ( input_name );
                __input_name_StateCUB.addElement(input_name);
            }
        }
        ui_SetIgnoreItemEvent ( false );
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
    int interval_base = TimeInterval.MONTH; // Default
    int comp = StateCU_DataSet.COMP_UNKNOWN;
    if ( extension.equalsIgnoreCase("bd1" ) ) {
        // CU Locations
        comp = StateCU_DataSet.COMP_CU_LOCATIONS;
    }
     // TODO SAM 2006-01-15
    // The following is not overly efficient because of a transition in
    // StateMod versions.  The version of the format is determined from the
    // file.  For older versions, this is used to return hard-coded
    // parameter lists.  For newer formats, the binary file is reopened and
    // the parameters are determined from the file.
    data_types = StateCU_Util.getTimeSeriesDataTypes (
            input_name, // Name of binary file
            comp,   // Component from above, from file extension
            null,   // ID
            null,   // dataset
            StateCU_BTS.determineFileVersion(input_name),
            interval_base,
            false,  // Include input (only output here)
            false,  // Include input, estimated (only output here)
            true,   // Include output (what the binaries contain)
            false,  // Check availability
            true,   // Add group (if available)
            false );// add note

    // Fill data types choice...

    Message.printStatus ( 2, routine, "Setting StateCUB data types..." );
    __data_type_JComboBox.setData ( data_types );
    Message.printStatus ( 2, routine, "Selecting the first StateCUB data type..." );
    __data_type_JComboBox.select ( null );
    if ( data_types.size() > 0 ) {
        __data_type_JComboBox.select ( 0 );
    }

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
    TSTool_TS_CellRenderer cr = new TSTool_TS_CellRenderer((TSTool_TS_TableModel)__query_TableModel);
    __query_JWorksheet.setCellRenderer ( cr );
    __query_JWorksheet.setModel ( __query_TableModel );
    // Turn off columns in the table model that do not apply...
    __query_JWorksheet.removeColumn (((TSTool_TS_TableModel)__query_TableModel).COL_ALIAS );
    __query_JWorksheet.removeColumn (((TSTool_TS_TableModel)__query_TableModel).COL_SCENARIO);
    __query_JWorksheet.removeColumn (((TSTool_TS_TableModel)__query_TableModel).COL_SEQUENCE);
    __query_JWorksheet.setColumnWidths ( cr.getColumnWidths() );
}

/**
Prompt for a StateModB input name (binary file name).  When selected, update the choices.
@param reset_input_names If true, the input names will be repopulated with
values from __input_name_StateModB.
@exception Exception if there is an error.
*/
private void uiAction_SelectInputName_StateModB ( boolean reset_input_names )
throws Exception
{	String routine = "TSTool_JFrame.selectInputName_StateModB";
	if ( reset_input_names ) {
		// The StateModB input type has been selected as a change from
		// another type.  Repopululate the list if previous choices exist...
		// TODO - probably not needed...
		//__input_name_JComboBox.removeAll();
		__input_name_JComboBox.setData ( __input_name_StateModB );
	}
	// Check the item that is selected...
	String input_name = __input_name_JComboBox.getSelected();
	if ( (input_name == null) || input_name.equals(__BROWSE) ) {
		// Prompt for the name of a StateMod binary file...
		// Based on the file extension, set the data types and other information...
		JFileChooser fc = JFileChooserFactory.createJFileChooser ( JGUIUtil.getLastFileDialogDirectory() );
		fc.setDialogTitle("Select StateMod Binary Output File");
		SimpleFileFilter sff = new SimpleFileFilter("b43","Diversion, Stream, Instream stations (Monthly)");
		fc.addChoosableFileFilter( sff );
		fc.addChoosableFileFilter( new SimpleFileFilter("b49","Diversion, Stream, Instream stations (Daily)") );
		fc.addChoosableFileFilter( new SimpleFileFilter("b44","Reservoir stations (Monthly)") );
		fc.addChoosableFileFilter( new SimpleFileFilter("b50","Reservoir stations (Daily)") );
		fc.addChoosableFileFilter( new SimpleFileFilter("b42","Well stations (Monthly)") );
		fc.addChoosableFileFilter( new SimpleFileFilter("b65","Well stations (Daily)") );
		fc.setFileFilter(sff);
		// Only allow recognized extensions...
		fc.setAcceptAllFileFilterUsed ( false );
		if (fc.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
			// User cancelled - set the file name back to the original and disable other choices...
			if ( input_name != null ) {
				ui_SetIgnoreItemEvent ( true );
				__input_name_JComboBox.select(null);
				if ( __input_name_StateModB_last != null ) {
					__input_name_JComboBox.select (	__input_name_StateModB_last );
				}
				ui_SetIgnoreItemEvent ( false );
			}
			return;
		}
		// User has chosen a file...

		input_name = fc.getSelectedFile().getPath(); 
		// Save as last selection...
		__input_name_StateModB_last = input_name;
		JGUIUtil.setLastFileDialogDirectory (fc.getSelectedFile().getParent() );

		// Set the input name...

		ui_SetIgnoreItemEvent ( true );
		if ( !JGUIUtil.isSimpleJComboBoxItem (__input_name_JComboBox,__BROWSE, JGUIUtil.NONE, null, null ) ) {
			// Not already in so add it at the beginning...
			__input_name_StateModB.addElement ( __BROWSE );
			__input_name_JComboBox.add ( __BROWSE );
		}
		if ( !JGUIUtil.isSimpleJComboBoxItem (__input_name_JComboBox,input_name, JGUIUtil.NONE, null, null ) ) {
			// Not already in so add after the browse string (files
			// are listed chronologically by select with most recent at the top...
			if ( __input_name_JComboBox.getItemCount() > 1 ) {
				__input_name_JComboBox.addAt ( input_name, 1 );
				__input_name_StateModB.insertElementAt ( input_name, 1 );
			}
			else {
                __input_name_JComboBox.add ( input_name );
				__input_name_StateModB.addElement(input_name);
			}
		}
		ui_SetIgnoreItemEvent ( false );
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
		// Diversions, streamflow, instream - monthly Use diversions below...
		comp = StateMod_DataSet.COMP_DIVERSION_STATIONS;
	}
	else if ( extension.equalsIgnoreCase("b44" ) ) {
		// Reservoirs - monthly
		comp = StateMod_DataSet.COMP_RESERVOIR_STATIONS;
	}
	else if ( extension.equalsIgnoreCase("b49" ) ) {
		// Diversions, streamflow, instream - daily. Use diversions below...
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
	// TODO SAM 2006-01-15
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
	Message.printStatus ( 2, routine, "Selecting the first StateModB data type..." );
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
	TSTool_TS_CellRenderer cr = new TSTool_TS_CellRenderer((TSTool_TS_TableModel)__query_TableModel);
	__query_JWorksheet.setCellRenderer ( cr );
	__query_JWorksheet.setModel ( __query_TableModel );
	// Turn off columns in the table model that do not apply...
	__query_JWorksheet.removeColumn (((TSTool_TS_TableModel)__query_TableModel).COL_ALIAS );
	__query_JWorksheet.removeColumn (((TSTool_TS_TableModel)__query_TableModel).COL_SCENARIO);
	__query_JWorksheet.removeColumn (((TSTool_TS_TableModel)__query_TableModel).COL_SEQUENCE);
	__query_JWorksheet.setColumnWidths ( cr.getColumnWidths() );
}

/**
Select on the map the features that are shown in the time series list in the
upper right of the main TSTool interface and zoom to the selected features.
@exception Exception if there is an error showing the related time series
features on the map.
*/
private void uiAction_SelectOnMap ()
throws Exception
{	String routine = "TSTool_JFrame.selectOnMap";
	int size = __query_JWorksheet.getRowCount();	// To size Vector
	Vector idlist = new Vector(size);
	Message.printStatus ( 1, routine, "Selecting and zooming to stations on map.  Please wait...");
	JGUIUtil.setWaitCursor(this, true);

	// Get the list of layers to select from, and the attributes to use...
	// First read the file with the lookup of time series to layer information.

	// TODO SAM 2006-03-01
	// Currently read this each time because it is a small file and it
	// helps in development to not restart.  However, in the future, change
	// so that the file is read once.

	String filename = TSToolMain.getPropValue ( "TSTool.MapLayerLookupFile" );
	if ( filename == null ) {
		Message.printWarning ( 1, routine, "The TSTool.MapLayerLookupFile is not defined - cannot link map and time series." );
		return;
	}

	String full_filename = IOUtil.getPathUsingWorkingDir ( filename );
	if ( !IOUtil.fileExists(full_filename) ) {
		Message.printWarning ( 1, routine,
		"The map layer lookup file \"" + full_filename + "\" does not exist.  Cannot link map and time series." );
		return;
	}
	
	PropList props = new PropList ("");
	props.set ( "Delimiter=," );		// see existing prototype
	props.set ( "CommentLineIndicator=#" );	// New - skip lines that start with this
	props.set ( "TrimStrings=True" );	// If true, trim strings after reading.
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
		Layer_DataSourceCol_int = table.getFieldIndex (	"Layer_DataSource");
		Message.printStatus ( 2, routine, "TimeSeriesLookup table for "+
		"map has columns...TS_InputType=" + TS_InputTypeCol_int +
		",TS_DataType=" + TS_DataTypeCol_int +
		",TS_Interval=" + TS_IntervalCol_int +
		",Layer_Name=" + Layer_NameCol_int +
		",Layer_Location=" + Layer_LocationCol_int +
		",Layer_DataSource=" + Layer_DataSourceCol_int );
	}

	Vector layerlist = new Vector();	// List of layers to match features
	Vector mapidlist = new Vector();	// List of identifier attributes in map data to match features
	String ts_inputtype, ts_datatype, ts_interval,
		layer_name, layer_location, layer_datasource = "",
		layer_interval = ""; // TSTool input to match against layers.
	TableRecord rec;
	if (	(TS_InputTypeCol_int >= 0) && (TS_DataTypeCol_int >= 0) &&
		(TS_IntervalCol_int >= 0) && (Layer_NameCol_int >= 0) &&
		(Layer_LocationCol_int >= 0) ) {
		// Have the necessary columns in the file to search for a matching layer...
		for ( int i = 0; i < tsize; i++ ) {
			rec = table.getRecord(i);
			ts_inputtype = (String)rec.getFieldValue(TS_InputTypeCol_int);
			ts_datatype = (String)rec.getFieldValue(TS_DataTypeCol_int);
			ts_interval = (String)rec.getFieldValue(TS_IntervalCol_int);
			layer_name = (String)rec.getFieldValue(Layer_NameCol_int);
			layer_location = (String)rec.getFieldValue(Layer_LocationCol_int);
			if ( Layer_DataSourceCol_int >= 0 ) {
				// Also include the data source in the query
				layer_datasource = (String)rec.getFieldValue(Layer_DataSourceCol_int);
			}
			if ( Layer_IntervalCol_int >= 0 ) {
				// Also include the data interval in the query
				layer_interval = (String)rec.getFieldValue(	Layer_IntervalCol_int);
			}
			// Required fields...
			if (!ts_inputtype.equalsIgnoreCase(	__selected_input_type)){
				continue;
			}
			if ( !ts_datatype.equalsIgnoreCase(	__selected_data_type)){
				continue;
			}
			if ( !ts_interval.equalsIgnoreCase(	__selected_time_step)){
				continue;
			}
			// The layer matches the main input selections...
			// Save optional information that will be used for record by record comparisons...
			if ( Layer_DataSourceCol_int >= 0 ) {
				layer_datasource = (String)rec.getFieldValue(Layer_DataSourceCol_int);
			}
			if ( Layer_IntervalCol_int >= 0 ) {
				layer_interval = (String)rec.getFieldValue(	Layer_IntervalCol_int);
			}
			// Save the layer to search and attribute(s) to match...
			layerlist.addElement ( layer_name );
			attributes.setLength(0);
			attributes.append ( layer_location );
			if ( (Layer_DataSourceCol_int >= 0) && (layer_datasource != null) && !layer_datasource.equals("") ) {
				attributes.append ( "," + layer_datasource );
			}
			if ( (Layer_IntervalCol_int >= 0) && (layer_interval != null) && !layer_interval.equals("") ) {
				attributes.append ( "," + layer_interval );
			}
			mapidlist.addElement ( attributes.toString() );
		}
	}

	// Determine the list of features (by ID, and optionally data source and interval) to select...
	// TODO SAM 2006-01-16
	// Need to make this more generic by using an interface to retrieve important time series data?

	// Get the worksheet of interest...

	int row = -1, location_col = -1, datasource_col = -1, interval_col = -1;
	if ( __selected_input_type.equals ( __INPUT_TYPE_HydroBase )) {
		if ( __query_TableModel instanceof TSTool_TS_TableModel){
			TSTool_TS_TableModel model = (TSTool_TS_TableModel)__query_TableModel;
			location_col = model.COL_ID;
			datasource_col = model.COL_DATA_SOURCE;
			interval_col = model.COL_TIME_STEP;
		}
		else if ( __query_TableModel instanceof
			TSTool_HydroBase_TableModel){
			TSTool_HydroBase_TableModel model =	(TSTool_HydroBase_TableModel)__query_TableModel;
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
	else {
        size = rows.length;
	}

	// Loop through either all rows or selected rows to get the identifiers of interest...
	
	String id = null;
	for ( int i = 0; i < size; i++ ) {
		if (all) {
			// Process all rows...
			row = i;
		}
		else {
            // Process only selected rows...
			row = rows[i];
		}
		
		// Get the station identifier from the table model row...

		if ( (row >= 0) && (location_col >= 0) ) {
			// The identifier is always matched...
			id = (String)__query_TableModel.getValueAt( row, location_col );
			if ( (id == null) || (id.length() <= 0) ) {
				continue;
			}
			attributes.setLength(0);
			attributes.append ( id );
			// Optional fields that if non-null should be used...
			if ( (Layer_DataSourceCol_int >= 0) && (layer_datasource != null) && !layer_datasource.equals("") ) {
				attributes.append ( "," + (String)__query_TableModel.getValueAt( row, datasource_col ) );
			}
			if ( (Layer_IntervalCol_int >= 0) && (layer_interval != null) && !layer_interval.equals("") ) {
				attributes.append ( "," + (String)__query_TableModel.getValueAt( row,interval_col ) );
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
			mapidlist,	// Attributes to use to compare to the identifiers
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
Display the status of the selected command(s).
*/
private void uiAction_ShowCommandStatus()
{
  SwingUtilities.invokeLater(new Runnable() {
    public void run() {
      try {
        String status = uiAction_ShowCommandStatus_GetCommandsStatus();

        HTMLViewer hTMLViewer = new HTMLViewer();
        hTMLViewer.setTitle ( "TSTool - Command Status" );
        hTMLViewer.setHTML(status);
        hTMLViewer.setSize(700,400);
        hTMLViewer.setVisible(true);
      }
      catch(Throwable t){
        Message.printWarning(1, "uiAction_ShowCommandStatus", "Problem showing Command status");
        String routine = "TSTool_JFrame.showCommandStatus";
        Message.printWarning(2, routine, t);
      }
    }
  });
  }

/**
 * Gets Commands status in HTML - this is currently only a helper for
 * the above method.  Rename if it will be used generically.
 * @return
 */
private String uiAction_ShowCommandStatus_GetCommandsStatus()
{
  Vector commands = commandList_GetCommandsBasedOnUI();
  String html = CommandStatusUtil.getHTMLStatusReport(commands);	
  return html;
}

/**
Show the Help About dialog in response to a user selecting a menu.
*/
private void uiAction_ShowHelpAbout ( LicenseManager licenseManager )
{   String license_type = licenseManager.getLicenseType();
    if ( license_type.equalsIgnoreCase("CDSS") ) {
        // CDSS installation...
        new HelpAboutJDialog ( this, "About TSTool",
        "TSTool - Time Series Tool\n" +
        "A component of CDSS\n" +
        IOUtil.getProgramVersion() + "\n" +
        "Copyright 1997-2008\n" +
        "Developed by Riverside Technology, inc.\n" +
        "Funded by:\n" +
        "Colorado Division of Water Resources\n" +
        "Colorado Water Conservation Board\n" +
        "Send comments about this interface to:\n" +
        "cdss@state.co.us (CDSS)\n" +
        __RTiSupportEmail + " (general)\n" );
    }
    else {
        // A non-CDSS (RTi customer) installation...
        new HelpAboutJDialog ( this, "About TSTool",
        "TSTool - Time Series Tool\n" +
        IOUtil.getProgramVersion() + "\n" +
        "Copyright 1997-2008\n" +
        "Developed by Riverside Technology, inc.\n" +
        "Licensed to: " + licenseManager.getLicenseOwner() + "\n" +
        "License type: " + licenseManager.getLicenseType() + "\n" +
        "License expires: " + licenseManager.getLicenseExpires() + "\n" +
        "Contact support at:  " +
        __RTiSupportEmail + "\n" );
    }
}

/**
Show the properties for the currnet commands run (processor).
*/
private void uiAction_ShowProperties_CommandsRun ()
{
	PropList reportProp = new PropList ("ReportJFrame.props");
	// Too big (make this big when we have more stuff)...
	//reportProp.set ( "TotalWidth", "750" );
	//reportProp.set ( "TotalHeight", "550" );
	reportProp.set ( "TotalWidth", "600" );
	reportProp.set ( "TotalHeight", "300" );
	reportProp.set ( "DisplayFont", __FIXED_WIDTH_FONT );
	reportProp.set ( "DisplaySize", "11" );
	reportProp.set ( "PrintFont", __FIXED_WIDTH_FONT );
	reportProp.set ( "PrintSize", "7" );
	reportProp.set ( "Title", "TSTool Commands Run Properties" );
	Vector v = new Vector ( 4 );
	v.addElement ( "Properties from the last commands run." );
	v.addElement ( "Note that property values are as of the time the window is opened." );
	v.addElement ( "Properties are set as defaults initially and change value when a command is processed." );
	v.addElement ( "" );
	
	String s1 = "", s2 = "";
	// Initial and current working directory...
	v.addElement ( "Initial working directory:  " + __ts_processor.getInitialWorkingDir() );
	try {
	    v.addElement ( "Current working directory:  " + __ts_processor.getPropContents("WorkingDir") );
	}
	catch ( Exception e ) {
	    v.addElement ( "Current working directory:  Unknown" );
	}
	// Whether running and cancel requested...
	v.addElement ( "Are commands running:  " + __ts_processor.getIsRunning() );
	v.addElement ( "Has cancel been requested (and is pending):  " + __ts_processor.getCancelProcessingRequested() );
	// Input period...
	DateTime date1 = commandProcessor_GetInputStart();
	if ( date1 == null ) {
		s1 = "NOT SPECIFIED (use all available data)";
	}
	else {
        s1 = date1.toString();
	}
	DateTime date2 = commandProcessor_GetInputEnd();
	if ( date2 == null ) {
		s2 = "NOT SPECIFIED (use all available data)";
	}
	else {	s2 = date2.toString();
	}
	v.addElement ( "Input (query/read) start: " + s1 );
	v.addElement ( "Input (query/read) end:   " + s2 );
	// Output period...
	date1 = commandProcessor_GetOutputStart();
	if ( date1 == null ) {
		s1 = "NOT SPECIFIED (output all available data)";
	}
	else {
        s1 = date1.toString();
	}
	date2 = commandProcessor_GetOutputEnd();
	if ( date2 == null ) {
		s2 = "NOT SPECIFIED (output all available data)";
	}
	else {
        s2 = date2.toString();
	}
	v.addElement ( "Output period start: " + s1 );
	v.addElement ( "Output period end:   " + s2 );
	// Auto-extend period...
	v.addElement ( "Automatically extend period to output period during read: " +
		commandProcessor_GetAutoExtendPeriod () );
	// Include missing TS automatically...
	v.addElement ( "Include missing TS automatically: " + commandProcessor_GetIncludeMissingTS() );
	if ( __source_HydroBase_enabled ) {
		v.addElement ( "" );
		Object o2 = commandProcessor_GetHydroBaseDMIList();
		if ( (o2 == null) || (((Vector)o2).size() == 0) ) {
			v.addElement ( "No HydroBase connections are open for the command processor." );
		}
		else { Vector dmis = (Vector)o2;
			int size = dmis.size();
			for ( int i = 0; i < size; i++ ) {
				v.addElement ( "Command processor HydroBase connection information:" );
				try {
					StringUtil.addListToStringList ( v,
						StringUtil.toVector( ((HydroBaseDMI)dmis.elementAt(i)).getVersionComments() ) );
				}
				catch ( Exception e ) {
					// Ignore for now.
				}
			}
		}
	}
	new ReportJFrame ( v, reportProp );
	// Clean up...
	v = null;
	reportProp = null;
}

/**
Show an output file using the appropriate display software/editor.
@param selected Path to selected output file.
*/
private void uiAction_ShowResultsOutputFile ( String selected )
{   String routine = getClass().getName() + ".uiAction_ShowOutputFile";
    if ( selected == null ) {
        // May be the result of some UI event...
        return;
    }
    // Display the selected file...
    if ( !( new File( selected ).isAbsolute() ) ) {
        selected = IOUtil.getPathUsingWorkingDir( selected );
    }
    if ( selected.toUpperCase().endsWith(".HTML")) {
        // Run the simple RTi browser to display the check file
        // this browser enables HTML navigation features for viewing the check file
        try {
            new SimpleBrowser( selected ).setVisible(true);
        } 
        catch ( MalformedURLException e ) {
            Message.printWarning(2, routine,"Couldn't find file or url: " + selected );
        }
        catch (IOException e) {
            Message.printWarning( 2, routine,"Failed to open browser to view: " + selected );
            Message.printWarning( 3, routine, e );
        }
    }
    else {
        // Display a simple text file (will show up in courier fixed width
        // font, which looks better than the HTML browser).
        try {
            if ( IOUtil.isUNIXMachine() ) {
                // Use a built in viewer (may be slow)...
                PropList reportProp = new PropList ("Output File");
                reportProp.set ( "TotalWidth", "800" );
                reportProp.set ( "TotalHeight", "600" );
                reportProp.set ( "DisplayFont", __FIXED_WIDTH_FONT );
                reportProp.set ( "DisplaySize", "11" );
                reportProp.set ( "PrintFont", __FIXED_WIDTH_FONT );
                reportProp.set ( "PrintSize", "7" );
                reportProp.set ( "Title", selected );
                reportProp.set ( "URL", selected );
                new ReportJFrame ( null, reportProp );
            }
            else {
                // Rely on Notepad on Windows...
                String [] command_array = new String[2];
                command_array[0] = "notepad";
                command_array[1] = IOUtil.getPathUsingWorkingDir(selected);
                ProcessManager p = new ProcessManager ( command_array );
                Thread t = new Thread ( p );
                t.start();
            }
        }
        catch (Exception e2) {
            Message.printWarning (1, routine, "Unable to view file \"" + selected + "\"" );
            Message.printWarning ( 3, routine, e2 );
        }
    }
}

/**
Show a table using the built in display component.
@param selected Identifier for the table to display.
*/
private void uiAction_ShowResultsTable ( String selected )
{   String routine = getClass().getName() + ".uiAction_ShowResultsTable";
    if ( selected == null ) {
        // May be the result of some UI event...
        return;
    }
    // Display the table...
    try {
        DataTable table = commandProcessor_GetTable ( selected );
        if ( table == null ) {
            Message.printWarning (1, routine,
                "Unable to get table \"" + selected + "\" from processor to view." );  
        }
        new DataTable_JFrame ( "Table \"" + selected + "\"", table );
    }
    catch (Exception e2) {
        Message.printWarning (1, routine, "Unable to view table \"" + selected + "\"" );
        Message.printWarning ( 3, routine, e2 );
    }
}

/**
Method to run test code.  This is usually accessible only when running with the
-test command line option.
@exception Exception if there is an error.
*/
private void uiAction_Test ()
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
		graphprops.set ( "DisplayFont", __FIXED_WIDTH_FONT );
		graphprops.set ( "DisplaySize", "11" );
		graphprops.set ( "PrintFont", __FIXED_WIDTH_FONT );
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
			        date_input.getMonth(), date_input.getDay(), date_input.getYear(), date_input.getHour() );
			jhour2 = (jul[0] - 1)*24 + jul[1];
			Message.printStatus ( 2, routine,
			        "Hour " + i + " -> " + date_output + "  Date " + date_input + " -> " + jhour2 );
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
		// TODO SAM 2004-10-28 TOM Replace the following to loop
		// through each feature in the MAP centroid layer...
		for ( int i = 0; i < 1; i++ ) {
		loc = "NYZ001";
		// TODO SAM 2004-10-28 This will only work on systems with
		// 6-hour MAP (which is most).  Additional work will need to
		// be done to support other systems.
		// TODO SAM 2004-10-28 - Tom replace the following with
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
			// TODO Tom...
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
		// TODO SAM 2004-10-28 need to decide whether to include
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
		// TODO SAM 2004-10-28 at some point this may just call a
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
			// TODO SAM - Tom take out this status message
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
					// TODO SAM - Tom take out this
					// status message after testing out...
					Message.printStatus ( 2, "", "24-hour "+
					date.toString() + " value=" + sum );
				}
				// Always reset the count and sum...
				count = 0;
				sum = 0.0;
			}
		}
		// TODO Tom...
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

		if ( __ts_processor == null ) {
			Message.printWarning ( 1, routine,
			"No time series available for test." );
			return;
		}
		Vector tslist = commandProcessor_GetTimeSeriesResultsList();
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
							// TODO - need some
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
		//graphprops.set ( "DisplayFont", __FIXED_WIDTH_FONT );
		//graphprops.set ( "DisplaySize", "11" );
		//graphprops.set ( "PrintFont", __FIXED_WIDTH_FONT );
		//graphprops.set ( "PrintSize", "7" );
		//graphprops.set ( "PageLength", "100" );
		new TSViewJFrame ( tslist, graphprops );
	}
}

/**
Fill in appropriate selections for the data type modifier choices based on
the currently selected input data source, data type and time step.
*/
private void uiAction_TimeStepChoiceClicked()
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

	ui_SetInputFilters();
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
private void uiAction_TransferAllQueryResultsToCommandList()
{	String routine = "TSTool_JFrame.transferAllTSFromListToCommands";

	int nrows = __query_TableModel.getRowCount();
	Message.printStatus ( 1, routine,
	"Transferring all time series to commands (" + nrows + " in list)..." );
	JGUIUtil.setWaitCursor ( this, true );
	int iend = nrows - 1;
	ui_SetIgnoreListSelectionEvent ( true );	// To increase performance
						// during transfer...
	ui_SetIgnoreItemEvent ( true );	// To increase performance
	for ( int i = 0; i < nrows; i++ ) {
		// Only force the GUI state to be updated if the last item.
		if ( i == iend ) {
			__ignore_ListSelectionEvent = false;
			__ignore_ItemEvent = false;
			queryResultsList_TransferOneTSFromQueryResultsListToCommandList ( i, true );
		}
		else {	queryResultsList_TransferOneTSFromQueryResultsListToCommandList ( i, false );
		}
	}
	ui_SetIgnoreListSelectionEvent ( false );
	ui_SetIgnoreItemEvent ( false );
	JGUIUtil.setWaitCursor ( this, false );

	Message.printStatus ( 1, routine, "Selected all time series." );
}

/**
Transfer selected time series from the query results to the command List.
*/
private void uiAction_TransferSelectedQueryResultsToCommandList ()
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
			queryResultsList_TransferOneTSFromQueryResultsListToCommandList ( selected[i], true ); 
		}
		else {	queryResultsList_TransferOneTSFromQueryResultsListToCommandList ( selected[i], false ); 
		}
	}
	Message.printStatus ( 1, routine, "Transferred selected time series." );
}

/**
Write the current command file list (all lines, whether selected or not) to
the specified file.  Do not prompt for header comments (and do not add).
@param prompt_for_file If true, prompt for the file name rather than using the
value that is passed.  An extension of .TSTool is enforced.
@param file Command file to write.
*/
private void uiAction_WriteCommandFile ( String file, boolean prompt_for_file )
{	String directory = null;
	if ( prompt_for_file ) {
		JFileChooser fc = JFileChooserFactory.createJFileChooser(ui_GetDir_LastCommandFileOpened() );
		fc.setDialogTitle("Save Command File");
		// Default name...
		File default_file = new File("commands.TSTool");
		fc.setSelectedFile ( default_file );
		SimpleFileFilter sff = new SimpleFileFilter("TSTool","TSTool Command File");
		fc.setFileFilter(sff);
		if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
			directory = fc.getSelectedFile().getParent();
			file = fc.getSelectedFile().getPath();
			IOUtil.enforceFileExtension ( file, "TSTool" );
			ui_SetDir_LastCommandFileOpened( directory );
		}		
		else {
		    // Did not approve...
			return;
		}
	}
	// Now write the file...
	try {
	    PrintWriter out = new PrintWriter(new FileOutputStream(file));
		int size = __commands_JListModel.size();
		for (int i = 0; i < size; i++) {
			out.println(((Command)__commands_JListModel.get(i)).toString());
		}
	
		out.close();
		commandList_SetDirty(false);
		commandList_SetCommandFileName ( file );

		if ( directory != null ) {
			// Set the "WorkingDir" property, which will NOT
			// contain a trailing separator...
			IOUtil.setProgramWorkingDir(directory);
			ui_SetDir_LastCommandFileOpened(directory);
			__props.set ("WorkingDir=" + IOUtil.getProgramWorkingDir());
			ui_SetInitialWorkingDir (__props.getValue("WorkingDir"));
		}
	}
	catch ( Exception e ) {
		Message.printWarning (1, "writeCommandFile", "Error writing file:\n\"" + file + "\"");
		// Leave the dirty flag the previous value.
	}
	// Update the status information...
	ui_UpdateStatus ( false );
}

/**
Handle ListSelectionListener events.
*/
public void valueChanged ( ListSelectionEvent e )
{	// For now just want to know when something changes so the GUI state can be checked...
	// e.getSource() apparently does not return __commands_JList - it must
	// return a different component so don't check the object address...
	if ( __ignore_ListSelectionEvent ) {
		return;
	}
    Object component = e.getSource();
    if ( component == __results_tsensembles_JList ) {
        //Message.printStatus(2, "", "Ensemble selected in list");
    }
    else if ( component == __results_files_JList ) {
        if ( !e.getValueIsAdjusting() ) {
            // User is done adjusting selection so do the display...
            ListSelectionModel lsm = __results_files_JList.getSelectionModel();
            int minIndex = lsm.getMinSelectionIndex();
            int maxIndex = lsm.getMaxSelectionIndex();
            for (int i = minIndex; i <= maxIndex; i++) {
                if (lsm.isSelectedIndex(i)) {
                    uiAction_ShowResultsOutputFile( (String)__results_files_JListModel.elementAt(i) );
                }
            }
        }
    }
    else if ( component == __results_tables_JList ) {
        if ( !e.getValueIsAdjusting() ) {
            // User is done adjusting selection so do the display...
            ListSelectionModel lsm = __results_tables_JList.getSelectionModel();
            int minIndex = lsm.getMinSelectionIndex();
            int maxIndex = lsm.getMaxSelectionIndex();
            for (int i = minIndex; i <= maxIndex; i++) {
                if (lsm.isSelectedIndex(i)) {
                    uiAction_ShowResultsTable( (String)__results_tables_JListModel.elementAt(i) );
                }
            }
        }
    }
	ui_UpdateStatus ( false );
}

/**
Needed for WindowListener interface.  Currently does nothing.
*/
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
		// GeoView window is closing...
		__geoview_JFrame = null;
		__View_MapInterface_JCheckBoxMenuItem.setSelected ( false );
	}
	/* FIXME SAM 2008-01-11 Need to test how -nomaingui mode is working with recent changes and then delete this code
	else if ( !__show_main ) {
		// Running in hidden mode and the TSViewJFrame has closed so close the application...
		uiAction_FileExitClicked ();
	}
	*/
}

/**
Need to monitor TSTool GUI is closing to shut it down gracefully.
*/
public void windowClosing ( WindowEvent e )
{	Component c = e.getComponent();
	if ( c.equals(this) ) {
		// This may cancel the close if the user decides not to exit...
        uiAction_FileExitClicked();
	}
}

/**
Needed for WindowListener interface.  Currently does nothing.
*/
public void windowDeactivated ( WindowEvent e )
{
}

/**
Needed for WindowListener interface.  Currently does nothing.
*/
public void windowDeiconified ( WindowEvent e )
{
}

/**
Needed for WindowListener interface.  Currently does nothing.
*/
public void windowIconified ( WindowEvent e )
{
}

/**
Needed for WindowListener interface.  Currently does nothing.
*/
public void windowOpened ( WindowEvent e )
{
}

// TODO SAM 2006-03-02 - Evaluate need
// The JWorksheet_Listener may be deprecated or reworked in the future.  It is
// used in a limited capacity here to detect row select/deselect from the popup
// menu, so tha the status labels can be updated properly.

/**
Required by JWorksheet_Listener.
*/
public void worksheetDeselectAllRows(int timeframe)
{	if (timeframe == JWorksheet.POST_SELECTION_CHANGE) {
		ui_UpdateStatus ( true );
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
		ui_UpdateStatus ( true );
	}
}

/**
Required by JWorksheet_Listener.
*/
public void worksheetSetRowCount ( int count )
{
}

/**
Internal class to handle action events from ensemble results list.
*/
private class ActionListener_ResultsEnsembles implements ActionListener
{
    /**
    Handle a group of actions for the View menu.
    @param event Event to handle.
    */
    public void actionPerformed (ActionEvent event)
    {   String command = event.getActionCommand();

        if ( command.equals(__Results_Graph_Line_String) ) {
            uiAction_GraphEnsembleResults("-olinegraph");
        }
    }
}

}
