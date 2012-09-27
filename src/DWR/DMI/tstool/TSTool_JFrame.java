// TODO SAM 2004-08-29 Need to use HydroBase_Util preferredWDIDLength instead
// of carrying around a property - isolate all HydroBase properties to that
// package

package DWR.DMI.tstool;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
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
import java.net.URL;
import java.util.Date;
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

import riverside.datastore.DataStore;
import riverside.datastore.DataStores_JFrame;
import rti.tscommandprocessor.core.TSCommandFactory;
import rti.tscommandprocessor.core.TSCommandFileRunner;
import rti.tscommandprocessor.core.TSCommandProcessor;
import rti.tscommandprocessor.core.TSCommandProcessorListModel;
import rti.tscommandprocessor.core.TSCommandProcessorThreadRunner;
import rti.tscommandprocessor.core.TSCommandProcessorUtil;
import rti.tscommandprocessor.core.TimeSeriesTreeView;
import rti.tscommandprocessor.core.TimeSeriesTreeView_JTree;
import rti.tscommandprocessor.core.TimeSeriesView;

import rti.tscommandprocessor.commands.hecdss.HecDssAPI;
import rti.tscommandprocessor.commands.hecdss.HecDssTSInputFilter_JPanel;
import rti.tscommandprocessor.commands.rccacis.RccAcisDataStore;
import rti.tscommandprocessor.commands.rccacis.RccAcisStationTimeSeriesMetadata;
import rti.tscommandprocessor.commands.rccacis.RccAcis_TimeSeries_InputFilter_JPanel;
import rti.tscommandprocessor.commands.reclamationhdb.ReclamationHDBDataStore;
import rti.tscommandprocessor.commands.reclamationhdb.ReclamationHDB_DMI;
import rti.tscommandprocessor.commands.reclamationhdb.ReclamationHDB_SiteTimeSeriesMetadata;
import rti.tscommandprocessor.commands.reclamationhdb.ReclamationHDB_TimeSeries_InputFilter_JPanel;
import rti.tscommandprocessor.commands.ts.FillMixedStation_JDialog;
import rti.tscommandprocessor.commands.ts.FillPrincipalComponentAnalysis_JDialog;
import rti.tscommandprocessor.commands.ts.TSID_Command;
import rti.tscommandprocessor.commands.usgs.nwis.daily.UsgsNwisDailyDataStore;
import rti.tscommandprocessor.commands.usgs.nwis.daily.UsgsNwisDaily_TimeSeries_InputFilter_JPanel;
import rti.tscommandprocessor.commands.usgs.nwis.daily.UsgsNwisSiteTimeSeriesMetadata;
import rti.tscommandprocessor.commands.util.Comment_Command;
import rti.tscommandprocessor.commands.util.Comment_JDialog;
import rti.tscommandprocessor.commands.util.Exit_Command;
import us.co.state.dwr.hbguest.ColoradoWaterHBGuestService;
import us.co.state.dwr.hbguest.ColoradoWaterHBGuest_GUI_StationGeolocMeasType_InputFilter_JPanel;
import us.co.state.dwr.hbguest.ColoradoWaterHBGuest_GUI_StructureGeolocMeasType_InputFilter_JPanel;
import us.co.state.dwr.hbguest.ColoradoWaterHBGuest_GUI_GroundWaterWellsMeasType_InputFilter_JPanel;
import us.co.state.dwr.hbguest.datastore.ColoradoWaterHBGuestDataStore;
import us.co.state.dwr.sms.ColoradoWaterSMS;
import us.co.state.dwr.sms.ColoradoWaterSMSAPI;
import us.co.state.dwr.sms.datastore.ColoradoWaterSMSDataStore;

import DWR.DMI.HydroBaseDMI.HydroBaseDMI;
import DWR.DMI.HydroBaseDMI.HydroBaseDataStore;
import DWR.DMI.HydroBaseDMI.HydroBase_GUI_AgriculturalCASSCropStats_InputFilter_JPanel;
import DWR.DMI.HydroBaseDMI.HydroBase_GUI_AgriculturalCASSLivestockStats_InputFilter_JPanel;
import DWR.DMI.HydroBaseDMI.HydroBase_GUI_AgriculturalNASSCropStats_InputFilter_JPanel;
import DWR.DMI.HydroBaseDMI.HydroBase_GUI_CUPopulation_InputFilter_JPanel;
import DWR.DMI.HydroBaseDMI.HydroBase_GUI_GroundWater_InputFilter_JPanel;
import DWR.DMI.HydroBaseDMI.HydroBase_GUI_SheetNameWISFormat_InputFilter_JPanel;
import DWR.DMI.HydroBaseDMI.HydroBase_GUI_StationGeolocMeasType_InputFilter_JPanel;
import DWR.DMI.HydroBaseDMI.HydroBase_GUI_StructureGeolocStructMeasType_InputFilter_JPanel;
import DWR.DMI.HydroBaseDMI.HydroBase_GUI_StructureIrrigSummaryTS_InputFilter_JPanel;
import DWR.DMI.HydroBaseDMI.HydroBase_GroundWaterWellsView;
import DWR.DMI.HydroBaseDMI.HydroBase_StationGeolocMeasType;
import DWR.DMI.HydroBaseDMI.HydroBase_StructureGeolocStructMeasType;
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
import DWR.StateMod.StateMod_GUIUtil;
import DWR.StateMod.StateMod_InstreamFlowRight;
import DWR.StateMod.StateMod_ReservoirRight;
import DWR.StateMod.StateMod_TS;
import DWR.StateMod.StateMod_Util;
import DWR.StateMod.StateMod_WellRight;
import RTi.DMI.DMIUtil;
import RTi.DMI.DatabaseDataStore;
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
import RTi.DMI.RiversideDB_DMI.RiversideDBDataStore;
import RTi.DMI.RiversideDB_DMI.RiversideDB_DMI;
import RTi.DMI.RiversideDB_DMI.RiversideDB_DataType;
import RTi.DMI.RiversideDB_DMI.RiversideDB_MeasType;
import RTi.DMI.RiversideDB_DMI.RiversideDB_MeasTypeMeasLocGeoloc;
import RTi.DMI.RiversideDB_DMI.RiversideDB_MeasTypeMeasLocGeoloc_InputFilter_JPanel;
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
import RTi.GRTS.TSProductDMI;
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
import RTi.TS.UsgsNwisRdbTS;
import RTi.Util.GUI.FindInJListJDialog;
import RTi.Util.GUI.HelpAboutJDialog;
import RTi.Util.GUI.InputFilter;
import RTi.Util.GUI.InputFilter_JPanel;
import RTi.Util.GUI.JFileChooserFactory;
import RTi.Util.GUI.JGUIUtil;
import RTi.Util.GUI.JScrollWorksheet;
import RTi.Util.GUI.JWorksheet;
import RTi.Util.GUI.JWorksheet_AbstractRowTableModel;
import RTi.Util.GUI.JWorksheet_DefaultTableCellRenderer;
import RTi.Util.GUI.JWorksheet_Listener;
import RTi.Util.GUI.ReportJFrame;
import RTi.Util.GUI.ResponseJDialog;
import RTi.Util.GUI.SimpleFileFilter;
import RTi.Util.GUI.SimpleJButton;
import RTi.Util.GUI.SimpleJComboBox;
import RTi.Util.GUI.SimpleJMenuItem;
import RTi.Util.GUI.TextResponseJDialog;
import RTi.Util.IO.AbstractCommand;
import RTi.Util.IO.AnnotatedCommandJList;
import RTi.Util.IO.Command;
import RTi.Util.IO.CommandDiscoverable;
import RTi.Util.IO.CommandListUI;
import RTi.Util.IO.CommandLogRecord;
import RTi.Util.IO.CommandLog_CellRenderer;
import RTi.Util.IO.CommandLog_TableModel;
import RTi.Util.IO.CommandPhaseType;
import RTi.Util.IO.CommandProcessor;
import RTi.Util.IO.CommandProcessorListener;
import RTi.Util.IO.CommandProcessorRequestResultsBean;
import RTi.Util.IO.CommandProgressListener;
import RTi.Util.IO.CommandSavesMultipleVersions;
import RTi.Util.IO.CommandStatusProvider;
import RTi.Util.IO.CommandStatusType;
import RTi.Util.IO.CommandStatusUtil;
import RTi.Util.IO.DataType;
import RTi.Util.IO.DataUnits;
import RTi.Util.IO.DataUnits_JFrame;
import RTi.Util.IO.EndianRandomAccessFile;
import RTi.Util.IO.FileGenerator;
import RTi.Util.IO.HTMLViewer;
import RTi.Util.IO.IOUtil;
import RTi.Util.IO.LicenseManager;
import RTi.Util.IO.TextPrinterJob;
import RTi.Util.IO.ProcessManager;
import RTi.Util.IO.Prop;
import RTi.Util.IO.PropList;
import RTi.Util.IO.SHEFType;
import RTi.Util.IO.UnknownCommand;
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
ActionListener, // To handle menu selections in GUI
CommandListUI, // To integrate this UI with command tools
GeoViewListener, // To handle map interaction (not well developed)
ItemListener, // To handle choice selections in GUI
JWorksheet_Listener, // To handle query result interaction
KeyListener, // To handle keyboard input in GUI
ListDataListener, // To change the GUI state when commands list change
ListSelectionListener,
MessageLogListener, // To handle interaction between log view and command list
MouseListener,
WindowListener, // To handle window/app shutdown, in particular if called in headless mode and TSView controls
CommandProcessorListener, // To handle command processor progress updates (when commands start/finish)
CommandProgressListener // To update the status based on progress within a command
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
Path to icons/graphics in class path.
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
Label for data stores, necessary because label will be set not visible if no data stores.
*/
private JLabel __dataStore_JLabel = null;

/**
Available data stores.
*/
private SimpleJComboBox __dataStore_JComboBox = null;

/**
Available input types including enabled file and databases.
*/
private SimpleJComboBox	__input_type_JComboBox = null;

/**
Input name choice label.  This may be set (in)visible depending on selected data store or input type.
*/
private JLabel __inputName_JLabel = null;

/**
Available input names for the input type (history of recent choices and allow browse where necessary).
*/
private SimpleJComboBox __inputName_JComboBox = null;	

/**
Data type choice.
*/
private SimpleJComboBox __dataType_JComboBox = null;

/**
Time step for initial query.
*/
private SimpleJComboBox __timeStep_JComboBox = null;

/**
FileFilter for current input name, checked with StateCU (and more being phased in).
Having as a data member allows selection of a filter in one place and a check
later (e.g., when the time series list is generated and displayed).
*/
private FileFilter __inputName_FileFilter = null;

/**
FileFilters used with StateCU input type.  __inputName_FileFilter will be set to one of these.
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
List of InputFilter_JPanel or JPanel (for input types that do not support input filters).
One of these will be visible at any time to provide query filter capability.  Each input type or data store
can have 1+ input filter panels, based on the data type and interval.
TODO SAM 2010-09-02 Evaluate whether a generic blank InputFilter_JPanel can be implemented to help
make the code more elegant.
*/
private List<JPanel> __inputFilterJPanelList = new Vector();

/**
The currently selected input filter JPanel, used to check input and get the filter information for queries.
Not an InputFilter_JPanel because of the generic case where a simple JPanel may need to be inserted.
*/
private JPanel __selectedInputFilter_JPanel = null;

/**
InputFilter_JPanel for ColoradoWaterHBGuest station time series.
*/
private InputFilter_JPanel __inputFilterColoradoWaterHBGuestStationGeolocMeasType_JPanel = null;

/**
InputFilter_JPanel for ColoradoWaterHBGuest structure time series.
*/
private InputFilter_JPanel __inputFilterColoradoWaterHBGuestStructureGeolocMeasType_JPanel = null;

/**
InputFilter_JPanel for ColoradoWaterHBGuest groundwater well time series.
*/
private InputFilter_JPanel __inputFilterColoradoWaterHBGuestGroundWaterWellsMeasType_JPanel = null;

/**
InputFilter_JPanel for HEC-DSS time series.
*/
private InputFilter_JPanel __inputFilterHecDss_JPanel = null;

/**
InputFilter_JPanel for HydroBase CASS agricultural crop statistics time series.
*/
private InputFilter_JPanel __inputFilterHydroBaseCASSCropStats_JPanel = null;

/**
InputFilter_JPanel for HydroBase CASS agricultural livestock statistics time series.
*/
private InputFilter_JPanel __inputFilterHydroBaseCASSLivestockStats_JPanel = null;

/**
InputFilter_JPanel for HydroBase CUPopulation time series.
*/
private InputFilter_JPanel __inputFilterHydroBaseCUPopulation_JPanel = null;

/**
InputFilter_JPanel for HydroBase NASS agricultural crop statistics time series.
*/
private InputFilter_JPanel __inputFilterHydroBaseNASS_JPanel = null;

/**
InputFilter_JPanel for HydroBase structure irrig_summary_ts time series.
*/
private InputFilter_JPanel __inputFilterHydroBaseIrrigts_JPanel = null;

/**
InputFilter_JPanel for HydroBase station time series.
*/
private InputFilter_JPanel __inputFilterHydroBaseStation_JPanel = null;

/**
InputFilter_JPanel for HydroBase structure time series - those that do not use SFUT.
*/
private InputFilter_JPanel __inputFilterHydroBaseStructure_JPanel = null;

/**
InputFilter_JPanel for HydroBase structure time series - those that do use SFUT.
*/
private InputFilter_JPanel __inputFilterHydroBaseStructureSfut_JPanel = null;

/**
InputFilter_JPanel for groundwater wells data.
*/
private InputFilter_JPanel __inputFilterHydroBaseWells_JPanel = null;

/**
InputFilter_JPanel for HydroBase WIS time series.
*/
private InputFilter_JPanel __inputFilterHydroBaseWIS_JPanel = null;

/**
InputFilter_JPanel for Mexico CSMN stations time series.
*/
private InputFilter_JPanel __inputFilterMexicoCSMN_JPanel = null;

/**
InputFilter_JPanel for NWSRFS_FS5Files time series.
*/
private InputFilter_JPanel __inputFilterNWSRFSFS5Files_JPanel = null;

/**
JPanel for input types that do not support input filters.
*/
private InputFilter_JPanel __inputFilterGeneric_JPanel = null;

/**
JPanel for message when input input filters are disabled (e.g., input type is selected but database connection
is not available.
*/
private InputFilter_JPanel __inputFilterMessage_JPanel = null;

/**
Used to keep track of the layout position for the input filter panels because the
panel cannot be initialized until after database connections are made.
*/
private int __inputFilterY = 0;

/**
The HEC-DSS files that have been selected during the session, to allow switching
between input types but not losing the list of files.  These are the full file strings.
*/
private List<String> __inputNameHecDssList = new Vector();

/**
The HEC-DSS files that have been selected during the session, to allow switching
between input types but not losing the list of files.  These are the abbreviated file strings, so as to not
take up too much space in the interface.
*/
private List<String> __inputNameHecDssVisibleList = new Vector();

/**
The last HEC-DSS file that was selected, to reset after canceling a browse.
*/
private String __inputNameHecDssVisibleLast = null;

/**
The NWSFFS FS5Files directories that have been selected during the
session, to allow switching between input types but not losing the list of files.
*/
private List<String> __input_name_NWSRFS_FS5Files = new Vector();

/**
The StateCU files that have been selected during the session, to allow switching
between input types but not losing the list of files. 
*/
private List<String> __inputNameStateCU = new Vector();

/**
The last StateCU file that was selected, to reset after canceling a browse. 
*/
private String __inputNameStateCULast = null;

/**
The StateCUB files that have been selected during the session, to allow switching
between input types but not losing the list of files.
*/
private List<String> __input_name_StateCUB = new Vector();

/**
The last StateCUB file that was selected, to reset after canceling a browse.
*/
private String __input_name_StateCUB_last = null;

/**
The StateModB files that have been selected during the session, to allow switching
between input types but not losing the list of files.
*/
private List<String> __input_name_StateModB = new Vector();

/**
The last StateModB file that was selected, to reset after canceling a browse.
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
private JPanel __queryInput_JPanel;

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

//================================
// Commands area...
//================================

/**
Annotated list to hold commands and display the command status.
*/
private AnnotatedCommandJList __commands_AnnotatedCommandJList;

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
The list of Command that is used with cut/copy/paste user actions.
*/
private List<Command> __commandsCutBuffer = new Vector(100,100);

// TODO SAM 2007-11-02 Evaluate putting in the processor
/**
Indicates whether the commands have been edited without being saved.
This will trigger some changes in the UI, for example indicating that the commands
have been modified and need to be saved (or cancel) before exit.
*/
private boolean __commandsDirty = false;

/**
Name of the command file.  Don't set until selected by the user (or on the command line).
*/
private String __commandFileName = null;	

//================================
// Results area...
//================================

/**
Tabbed pane to include all results.
*/
private JTabbedPane __results_JTabbedPane;

/**
Panel for TSEnsemble results.
*/
private JPanel __resultsTSEnsembles_JPanel; 

/**
Final list showing in-memory time series ensemble results.
*/
private JList __resultsTSEnsembles_JList;

/**
JList data model for final time series ensembles (basically a list of
time series associated with __results_tsensembles_JList).
*/
private DefaultListModel __resultsTSEnsembles_JListModel;

/**
Popup menu for ensemble results.
*/
private JPopupMenu __resultsTSEnsembles_JPopupMenu = null;

/**
Worksheet that contains a list of problems from processing.
*/
private JWorksheet __resultsProblems_JWorksheet = null;

/**
Panel for TS results.
*/
private JPanel __resultsTS_JPanel; 

/**
Final list showing in-memory time series results.
*/
private JList __resultsTS_JList;

/**
JList data model for final time series (basically a list of time series associated with __results_ts_JList).
*/
private DefaultListModel __resultsTS_JListModel;	

/**
The command processor, which maintains a list of command objects, process
the data, and the time series results.  There is only one command processor
instance for a TSTool session and it is kept current with the application.
TODO SAM 2009-03-06 In the future, if threading is implemented, it may be possible to have, for
example, tabs for different command files, each with a TSCommandProcessor.
*/
private TSCommandProcessor __tsProcessor = new TSCommandProcessor();

/**
Popup menu for time series results.
*/
private JPopupMenu __resultsTS_JPopupMenu = null;

/**
List of results tables for viewing with an editor.
*/
private JList __resultsTables_JList = null;

/**
JList data model for final time series (basically a Vector of
table identifiers associated with __results_tables_JList).
*/
private DefaultListModel __resultsTables_JListModel;

/**
List of results output files for viewing with an editor.
*/
private JList __resultsOutputFiles_JList = null;

/**
JList data model for final time series (basically a Vector of
filenames associated with __resultsOutputFiles_JList).
*/
private DefaultListModel __resultsOutputFiles_JListModel;

/**
Panel for results views.
*/
private JPanel __resultsViews_JPanel;

/**
Tabbed pane for views within the view results.
*/
private JTabbedPane __resultsViews_JTabbedPane;

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
General status string to indicate that command processing is being canceled.
*/
private final String __STATUS_CANCELING = "Canceling";
/**
General status string to indicate that command processing has been canceled.
*/
private final String __STATUS_CANCELED = "Canceled";

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

// TODO SAM 2011-05-18 It may be OK to phase these out in favor of processor properties (like the
// working directory) in particular since only a select few properties are handled here
/**
A subset of TSTool application configuration properties, which are initialized from the configuration file
and are then updated in the TSTool environment.
*/
private PropList __props;

/**
The initial working directory corresponding to a command file read/write or File... Set Working Dir.
This is used when processing the list of setWorkingDir() commands passed to command editors.
Without the initial working directory, relative changes in the working directory will
result in an inaccurate initial state.
*/
private String __initialWorkingDir = "";

/**
The last directory selected with Run...Command File, to run an external command file.
*/
private String __Dir_LastExternalCommandFileRun = null;

/**
The last directory selected when loading a command file.
*/
private String __Dir_LastCommandFileOpened = null;

/**
Lets the checkGUIState() method avoid checking for many null components during startup.
*/
private boolean __guiInitialized = false;

/**
Use this to temporarily ignore item action performed events, necessary when
programatically modifying the contents of combo boxes.
*/
private boolean __ignoreActionEvent = false;

// TODO SAM 2007-10-19 Evaluate whether still needed with new list model.
/**
Use this to temporarily ignore item listener events, necessary when
programatically modifying the contents of combo boxes.
*/
private boolean __ignoreItemEvent = false;

/**
This is used in some cases to disable updates during bulk operations to the commands list.
*/
private boolean __ignoreListSelectionEvent = false;

/**
Indicates which data sources are enabled.  Defaults are actually definitively set when
the configuration file properties are evaluated.
*/
private boolean
    __source_ColoradoBNDSS_enabled = false,
    __source_ColoradoSMS_enabled = false,
    __source_ColoradoWaterHBGuest_enabled = true, // By default - allow all to access web service
    __source_ColoradoWaterSMS_enabled = true, // By default - allow all to access web service
	__source_DateValue_enabled = true,
	__source_DIADvisor_enabled = false,
	__source_HECDSS_enabled = true,
	__source_HydroBase_enabled = true,
	__source_MexicoCSMN_enabled = false,
	__source_MODSIM_enabled = true,
	__source_NWSCard_enabled = true,
	__source_NWSRFS_FS5Files_enabled = false,
	__source_NWSRFS_ESPTraceEnsemble_enabled = false,
	__source_RCCACIS_enabled = false,
	__source_ReclamationHDB_enabled = false,
	__source_RiversideDB_enabled = false,
	__source_RiverWare_enabled = true,
	__source_SHEF_enabled = true,
	__source_StateCU_enabled = true,
	__source_StateCUB_enabled = true,
	__source_StateMod_enabled = true,
	__source_StateModB_enabled = true,
	__source_UsgsNwisDaily_enabled = true,
	__source_UsgsNwisRdb_enabled = true,
	__source_WaterML_enabled = true,
	__source_WaterOneFlow_enabled = true;

/**
Indicates if the main GUI should be shown.  This is normally true but if false
then only graph windows will be shown, and when closed the app will close.  This
is used when calling TSTool from other software (e.g., to provide graphing
capabilities to GIS applications).
*/
//private boolean __show_main = true;

/**
DiadVisor_DMI objects for DiadVisor input type, opened via TSTool.cfg information
and the DiadVisor select dialog, provided to the processor as the initial DiadVisor DMI instance.
*/
private DIADvisorDMI __DIADvisor_dmi = null;	// Operational DB
private DIADvisorDMI __DIADvisor_archive_dmi = null;	// Archive databases.

/**
HydroBaseDataStore for HydroBase input type, opened via TSTool.cfg information
and the HydroBase select dialog, provided to the processor as the initial HydroBase DMI instance.
TODO SAM 2012-09-10 Begin phasing out in favor of datastores but need to figure out how to get rid of login
dialog and have best practices for modelers to configure datastores.
*/
private HydroBaseDataStore __hbDataStore = null;

/**
SatMonSysDMI object for ColoradoSMS input type, opened via TSTool.cfg information
and the HydroBase select dialog, provided to the processor as the initial HydroBase DMI instance.
*/
private SatMonSysDMI __smsdmi = null;		

/**
NWSRFS_DMI object for NWSRFS_FS5Files input type, opened via TSTool.cfg information
and the NWSRFS FS5Files directory select dialog, provided to the processor as the initial NWSRFSS DMI instance.
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
    __CommandsPopup_ShowCommandStatus_JMenuItem,
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
    __CommandsPopup_ConvertTSIDTo_ReadTimeSeries_JMenuItem,
    __CommandsPopup_ConvertTSIDTo_ReadCommand_JMenuItem;

// File menu...

private JMenu
	__File_JMenu = null,
	__File_New_JMenu = null;
        private JMenuItem
        __File_New_CommandFile_JMenuItem = null;
	JMenu __File_Open_JMenu = null;
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
		__File_Save_CommandsAsVersion9_JMenuItem = null,
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
	__Edit_CommandWithErrorChecking_JMenuItem = null,
	// --
	__Edit_ConvertSelectedCommandsToComments_JMenuItem = null,
	__Edit_ConvertSelectedCommandsFromComments_JMenuItem = null,
    __Edit_ConvertTSIDTo_ReadTimeSeries_JMenuItem = null,
    __Edit_ConvertTSIDTo_ReadCommand_JMenuItem = null;

// View menu...

private JMenu
	__View_JMenu = null;
private JMenuItem
    __View_DataStores_JMenuItem = null,
    __View_DataUnits_JMenuItem = null;
private JCheckBoxMenuItem
	__View_MapInterface_JCheckBoxMenuItem = null;
private JMenuItem
    __View_CloseAllViewWindows_JMenuItem = null;

// Commands (Create Time Series)...

JMenu
	__Commands_JMenu = null,
	__Commands_CreateTimeSeries_JMenu = null;
JMenuItem
    __Commands_Create_NewPatternTimeSeries_JMenuItem,
    __Commands_Create_NewTimeSeries_JMenuItem,
	__Commands_Create_ChangeInterval_JMenuItem,
	__Commands_Create_Copy_JMenuItem,
	__Commands_Create_Delta_JMenuItem,
	__Commands_Create_Disaggregate_JMenuItem,
	__Commands_Create_LookupTimeSeriesFromTable_JMenuItem,
	__Commands_Create_NewDayTSFromMonthAndDayTS_JMenuItem,
	__Commands_Create_NewEndOfMonthTSFromDayTS_JMenuItem,
	__Commands_Create_Normalize_JMenuItem,
	__Commands_Create_RelativeDiff_JMenuItem,
    __Commands_Create_ResequenceTimeSeriesData_JMenuItem,
    __Commands_Create_RunningStatisticTimeSeries_JMenuItem,
	__Commands_Create_NewStatisticTimeSeries_JMenuItem,
	__Commands_Create_NewStatisticYearTS_JMenuItem;

// Commands (Read Time Series)...

JMenu
	__Commands_ReadTimeSeries_JMenu = null;
JMenuItem
    __Commands_Read_SetIncludeMissingTS_JMenuItem,
    __Commands_Read_SetInputPeriod_JMenuItem,
	//--
    __Commands_Read_CreateFromList_JMenuItem,
	__Commands_Read_ReadDateValue_JMenuItem,
    __Commands_Read_ReadDelimitedFile_JMenuItem,
    __Commands_Read_ReadHecDss_JMenuItem,
	__Commands_Read_ReadHydroBase_JMenuItem,
	__Commands_Read_ReadColoradoBNDSS_JMenuItem,
	__Commands_Read_ReadMODSIM_JMenuItem,
	__Commands_Read_ReadNwsCard_JMenuItem,
	__Commands_Read_ReadNwsrfsFS5Files_JMenuItem,
	__Commands_Read_ReadRccAcis_JMenuItem,
	__Commands_Read_ReadReclamationHDB_JMenuItem,
	__Commands_Read_ReadRiversideDB_JMenuItem,
	__Commands_Read_ReadRiverWare_JMenuItem,
	__Commands_Read_ReadStateCU_JMenuItem,
	__Commands_Read_ReadStateCUB_JMenuItem,
	__Commands_Read_ReadStateMod_JMenuItem,
	__Commands_Read_ReadStateModB_JMenuItem,
	__Commands_Read_ReadTimeSeries_JMenuItem,
	__Commands_Read_ReadUsgsNwisDaily_JMenuItem,
	__Commands_Read_ReadUsgsNwisRdb_JMenuItem,
	__Commands_Read_ReadWaterML_JMenuItem,
	__Commands_Read_ReadWaterOneFlow_JMenuItem,
	__Commands_Read_StateModMax_JMenuItem;

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
    __Commands_Fill_ReadPatternFile_JMenuItem,
	__Commands_Fill_FillProrate_JMenuItem,
	__Commands_Fill_FillRegression_JMenuItem,
	__Commands_Fill_FillRepeat_JMenuItem,
	__Commands_Fill_FillUsingDiversionComments_JMenuItem,

	__Commands_Fill_SetAutoExtendPeriod_JMenuItem,
	__Commands_Fill_SetAveragePeriod_JMenuItem,
	__Commands_Fill_SetIgnoreLEZero_JMenuItem;

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
	__Commands_Set_SetToMax_JMenuItem,
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
	__Commands_Analyze_CalculateTimeSeriesStatistic_JMenuItem = null,
	__Commands_Analyze_CompareTimeSeries_JMenuItem = null,
	__Commands_Analyze_ComputeErrorTimeSeries_JMenuItem = null;

// Commands...Models....
JMenu
	__Commands_Models_Routing_JMenu = null;
JMenuItem
	__Commands_Models_Routing_LagK_JMenuItem = null,
	__Commands_Models_Routing_VariableLagK_JMenuItem = null;

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
	__Commands_Output_WriteHecDss_JMenuItem,
	__Commands_Output_WriteNwsCard_JMenuItem,
	__Commands_Output_WriteReclamationHDB_JMenuItem,
	__Commands_Output_WriteRiversideDB_JMenuItem,
	__Commands_Output_WriteRiverWare_JMenuItem,
    __Commands_Output_WriteSHEF_JMenuItem,
	__Commands_Output_WriteStateCU_JMenuItem,
	__Commands_Output_WriteStateMod_JMenuItem,
	__Commands_Output_WriteSummary_JMenuItem,
	__Commands_Output_WriteWaterML_JMenuItem,

    __Commands_Output_ProcessTSProduct_JMenuItem;

JMenu
    __Commands_Check_CheckTimeSeries_JMenu = null;
JMenuItem
    __Commands_Check_CheckingResults_CheckTimeSeries_JMenuItem = null,
    __Commands_Check_CheckingResults_CheckTimeSeriesStatistic_JMenuItem = null,
    __Commands_Check_CheckingResults_WriteCheckFile_JMenuItem = null;

//Commands...Ensemble Processing...

JMenu
    __Commands_Ensemble_JMenu = null;
JMenuItem
    __Commands_Ensemble_CreateEnsembleFromOneTimeSeries_JMenuItem,
    __Commands_Ensemble_CopyEnsemble_JMenuItem,
    __Commands_Ensemble_NewEnsemble_JMenuItem,
    __Commands_Ensemble_ReadNwsrfsEspTraceEnsemble_JMenuItem,
    __Commands_Ensemble_InsertTimeSeriesIntoEnsemble_JMenuItem,
    __Commands_Ensemble_NewStatisticEnsemble_JMenuItem,
    __Commands_Ensemble_NewStatisticTimeSeriesFromEnsemble_JMenuItem,
    __Commands_Ensemble_WeightTraces_JMenuItem,
    __Commands_Ensemble_WriteNwsrfsEspTraceEnsemble_JMenuItem;

// Commands (Table)...

JMenu
    __Commands_Table_JMenu = null;
JMenuItem
    __Commands_Table_NewTable_JMenuItem,
    __Commands_Table_CopyTable_JMenuItem,
    __Commands_Table_ReadTableFromDataStore_JMenuItem,
    __Commands_Table_ReadTableFromDelimitedFile_JMenuItem,
    __Commands_Table_ReadTableFromDBF_JMenuItem,
    __Commands_Table_TimeSeriesToTable_JMenuItem,
    __Commands_Table_ManipulateTableString_JMenuItem,
    __Commands_Table_TableMath_JMenuItem,
    __Commands_Table_TableTimeSeriesMath_JMenuItem,
    __Commands_Table_SetTimeSeriesPropertiesFromTable_JMenuItem,
    __Commands_Table_CopyTimeSeriesPropertiesToTable_JMenuItem,
    __Commands_Table_CompareTables_JMenuItem,
    __Commands_Table_WriteTableToDelimitedFile_JMenuItem,
    __Commands_Table_WriteTableToHTML_JMenuItem;

// Commands (Template Processing)...

JMenu
    __Commands_Template_JMenu = null;
JMenuItem
    __Commands_Template_ExpandTemplateFile_JMenuItem;

//Commands (View Processing)...

JMenu
    __Commands_View_JMenu = null;
JMenuItem
    __Commands_View_NewTreeView_JMenuItem;

// Commands (General)...

JMenu
	__Commands_General_Logging_JMenu = null;
JMenuItem
	__Commands_General_Logging_StartLog_JMenuItem = null,
	__Commands_General_Logging_SetDebugLevel_JMenuItem = null,
	__Commands_General_Logging_SetWarningLevel_JMenuItem = null;

JMenu
    __Commands_General_Comments_JMenu = null;
JMenuItem
	__Commands_General_Comments_Comment_JMenuItem = null,
	__Commands_General_Comments_StartComment_JMenuItem = null,
	__Commands_General_Comments_EndComment_JMenuItem = null,
	__Commands_General_Comments_ReadOnlyComment_JMenuItem = null,
	__Commands_General_Comments_ExpectedStatusFailureComment_JMenuItem = null,
	__Commands_General_Comments_ExpectedStatusWarningComment_JMenuItem = null;

JMenu
    __Commands_General_FileHandling_JMenu = null;
JMenuItem
    __Commands_General_FileHandling_FTPGet_JMenuItem = null,
    __Commands_General_FileHandling_WebGet_JMenuItem = null,
    __Commands_General_FileHandling_AppendFile_JMenuItem = null,
    __Commands_General_FileHandling_RemoveFile_JMenuItem = null,
    __Commands_General_FileHandling_PrintTextFile_JMenuItem = null;

JMenu
    __Commands_General_Running_JMenu = null;
JMenuItem
    __Commands_General_Running_ReadPropertiesFromFile_JMenuItem = null,
    __Commands_General_Running_SetProperty_JMenuItem = null,
    __Commands_General_Running_SetPropertyFromNwsrfsAppDefault_JMenuItem = null,
    __Commands_General_Running_FormatDateTimeProperty_JMenuItem = null,
    __Commands_General_Running_WritePropertiesToFile_JMenuItem = null,
	__Commands_General_Running_RunCommands_JMenuItem = null,
	__Commands_General_Running_RunProgram_JMenuItem = null,
    __Commands_General_Running_RunPython_JMenuItem = null,
    __Commands_General_Running_RunDSSUTL_JMenuItem = null,
    __Commands_General_Running_Exit_JMenuItem = null,
    __Commands_General_Running_SetWorkingDir_JMenuItem = null;

//Commands (General - Test Processing)...
JMenu
    __Commands_General_TestProcessing_JMenu = null;
JMenuItem
    __Commands_General_TestProcessing_CompareFiles_JMenuItem = null,
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

// Run...

private JMenu
	__Run_JMenu = null;
private JMenuItem
	__Run_AllCommandsCreateOutput_JMenuItem,
	__Run_AllCommandsIgnoreOutput_JMenuItem,
	__Run_SelectedCommandsCreateOutput_JMenuItem,
	__Run_SelectedCommandsIgnoreOutput_JMenuItem,
	__Run_CancelCommandProcessing_JMenuItem,
	__Run_CancelAllCommandProcesses_JMenuItem,
	__Run_CommandsFromFile_JMenuItem,
	__Run_ProcessTSProductPreview_JMenuItem,
	__Run_ProcessTSProductOutput_JMenuItem;

// Results...

private JMenu
	__Results_JMenu = null,
	__Results_Graph_JMenu = null;
private JMenuItem
	__Results_Graph_AnnualTraces_JMenuItem = null,
	__Results_Graph_Area_JMenuItem = null,
	__Results_Graph_AreaStacked_JMenuItem = null,
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
	__Tools_Analysis_PrincipalComponentAnalysis_JMenuItem = null,
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
	__Help_AboutTSTool_JMenuItem = null,
	__Help_ViewDocumentation_JMenuItem = null,
	__Help_ViewTrainingMaterials_JMenuItem = null,
	__Help_ImportConfiguration_JMenuItem = null;

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
	   __File_New_String = "New",
           __File_New_CommandFile_String = "Command File", 
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
			__File_Save_CommandsAsVersion9_String = "Commands As (Version 9 Syntax)...", 
			__File_Save_TimeSeriesAs_String = "Time Series As...", 
		__File_Print_String = "Print",
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
		__Edit_CommandWithErrorChecking_String = "Command...",
		__Edit_ConvertSelectedCommandsToComments_String = "Convert selected commands to # comments",
		__Edit_ConvertSelectedCommandsFromComments_String =	"Convert selected commands from # comments",
		__Edit_ConvertTSIDTo_ReadTimeSeries_String = "Convert TSID command to general ReadTimeSeries() command",
		__Edit_ConvertTSIDTo_ReadCommand_String = "Convert TSID command to specific Read...() command",

	// View menu (order in GUI)...

	__View_String = "View",
		__View_DataStores_String = "Data Stores",
		__View_DataUnits_String = "Data Units",
	    __View_Map_String = "Map",
	    __View_CloseAllViewWindows_String = "Close All View Windows",

	// Commands menu (order in GUI)...

	__Commands_String = "Commands",

	__Commands_CreateTimeSeries_String = "Create Time Series",
	__Commands_Create_NewPatternTimeSeries_String = TAB + "NewPatternTimeSeries()... <create a time series with repeating data values>",
    __Commands_Create_NewTimeSeries_String = TAB + "NewTimeSeries()... <create and initialize a new time series>",
	__Commands_Create_CreateFromList_String = TAB + "CreateFromList()... <read 1+ time series using a list of identifiers>",
	__Commands_Create_ChangeInterval_String = TAB + "ChangeInterval()... <create time series with new interval (timestep)>",
	__Commands_Create_Copy_String = TAB + "Copy()... <copy a time series>",
	__Commands_Create_Delta_String = TAB + "Delta()... <create new time series as delta between values>",
	__Commands_Create_Disaggregate_String = TAB + "Disaggregate()... <disaggregate longer interval to shorter>",
	__Commands_Create_LookupTimeSeriesFromTable_String = TAB + "LookupTimeSeriesFromTable()... <create a time series using a lookup table>",
	__Commands_Create_NewDayTSFromMonthAndDayTS_String = TAB + "NewDayTSFromMonthAndDayTS()... <create daily time series from monthly total and daily pattern>",
	__Commands_Create_NewEndOfMonthTSFromDayTS_String = TAB + "NewEndOfMonthTSFromDayTS()... <convert daily data to end of month time series>",
	__Commands_Create_Normalize_String = TAB + "Normalize()... <Normalize time series to unitless values>",
	__Commands_Create_RelativeDiff_String = TAB + "RelativeDiff()... <relative difference of time series>",
    __Commands_Create_ResequenceTimeSeriesData_String = TAB + "ResequenceTimeSeriesData()... <resequence years to create new scenarios>",
    __Commands_Create_RunningStatisticTimeSeries_String = TAB + "RunningStatisticTimeSeries()... <create a statistic time series from a running sample>",
	__Commands_Create_NewStatisticTimeSeries_String = TAB + "NewStatisticTimeSeries()... <create a time series as repeating statistics from a time series>",
	__Commands_Create_NewStatisticYearTS_String = TAB + "NewStatisticYearTS()... <create a year time series using a statistic from a time series>",

	__Commands_Read_SetIncludeMissingTS_String = TAB + "SetIncludeMissingTS()... <create empty time series if no data>",
	__Commands_Read_SetInputPeriod_String = TAB + "SetInputPeriod()... <for reading data>",

	__Commands_ReadTimeSeries_String = "Read Time Series",
    __Commands_Read_ReadColoradoBNDSS_String = TAB + "ReadColoradoBNDSS()... <read 1+ time series from Colorado's BNDSS database>",
	__Commands_Read_ReadDateValue_String = TAB + "ReadDateValue()... <read 1+ time series from a DateValue file>",
    __Commands_Read_ReadDelimitedFile_String = TAB + "ReadDelimitedFile()... <read 1+ time series from a delimited file (under development)>",
    __Commands_Read_ReadHecDss_String = TAB + "ReadHecDss()... <read 1+ time series from a HEC-DSS database file>",
    __Commands_Read_ReadHydroBase_String = TAB + "ReadHydroBase()... <read 1+ time series from HydroBase>",
	__Commands_Read_ReadMODSIM_String = TAB + "ReadMODSIM()... <read 1+ time ries from a MODSIM output file>",
	__Commands_Read_ReadNwsCard_String = TAB + "ReadNwsCard()... <read 1+ time series from an NWS CARD file>",
	__Commands_Read_ReadNwsrfsFS5Files_String = TAB + "ReadNwsrfsFS5Files()... <read 1 time series from NWSRFS FS5 files>",
	__Commands_Read_ReadRccAcis_String = TAB + "ReadRccAcis()... <read 1+ time series from the RCC ACIS web service>",
	__Commands_Read_ReadReclamationHDB_String = TAB + "ReadReclamationHDB()... <read 1+ time series a Reclamation HDB database>",
	__Commands_Read_ReadRiversideDB_String = TAB + "ReadRiversideDB()... <read 1+ time series from a RiversideDB database>",
	__Commands_Read_ReadRiverWare_String = TAB + "ReadRiverWare()... <read 1 time series from a RiverWare file>",
	__Commands_Read_ReadStateCU_String = TAB + "ReadStateCU()... <read 1+ time series from a StateCU file>",
	__Commands_Read_ReadStateCUB_String = TAB + "ReadStateCUB()... <read 1+ time series from a StateCU binary output file>",
	__Commands_Read_ReadStateMod_String = TAB +	"ReadStateMod()... <read 1+ time series from a StateMod file>",
	__Commands_Read_ReadStateModB_String = TAB + "ReadStateModB()... <read 1+ time series from a StateMod binary output file>",
	__Commands_Read_ReadTimeSeries_String = TAB + "ReadTimeSeries()... <read 1 time series given a full TSID>",
    __Commands_Read_ReadUsgsNwisDaily_String = TAB + "ReadUsgsNwisDaily()... <read 1+ time series from USGS NWIS daily value web service>",
	__Commands_Read_ReadUsgsNwisRdb_String = TAB + "ReadUsgsNwisRdb()... <read 1 time series from a USGS NWIS RDB file>",
    __Commands_Read_ReadWaterML_String = TAB + "ReadWaterML()... <read 1+ time series from a WaterML file>",
    __Commands_Read_ReadWaterOneFlow_String = TAB + "ReadWaterOneFlow()... <read 1+ time series from a WaterOneFlow web service>",
	__Commands_Read_StateModMax_String = TAB + "StateModMax()... <generate 1+ time series as Max() of TS in two StateMod files>",

	// Commands... Fill Time Series...

	__Commands_FillTimeSeries_String = "Fill Time Series Missing Data",
	__Commands_Fill_FillConstant_String = TAB + "FillConstant()... <fill TS with constant>",
	__Commands_Fill_FillDayTSFrom2MonthTSAnd1DayTS_String = TAB + "FillDayTSFrom2MonthTSAnd1DayTS()... <fill daily time series using D1 = D2*M1/M2>",
	__Commands_Fill_FillFromTS_String = TAB + "FillFromTS()... <fill time series with values from another time series>",
	__Commands_Fill_FillHistMonthAverage_String = TAB +	"FillHistMonthAverage()... <fill monthly TS using historic average>",
	__Commands_Fill_FillHistYearAverage_String = TAB + "FillHistYearAverage()... <fill yearly TS using historic average>",
	__Commands_Fill_FillInterpolate_String = TAB + "FillInterpolate()... <fill TS using interpolation>",
	__Commands_Fill_FillMixedStation_String = TAB + "FillMixedStation()... <fill TS using mixed stations (under development)>",
	__Commands_Fill_FillMOVE1_String = TAB + "FillMOVE1()... <fill TS using MOVE1 method>",
	__Commands_Fill_FillMOVE2_String = TAB + "FillMOVE2()... <fill TS using MOVE2 method>",
	__Commands_Fill_FillPattern_String = TAB + "FillPattern()... <fill TS using WET/DRY/AVG pattern>",
	__Commands_Fill_ReadPatternFile_String = TAB + "  ReadPatternFile()... <for use with FillPattern() >",
	__Commands_Fill_FillProrate_String = TAB + "FillProrate()... <fill TS by prorating another time series>",
	__Commands_Fill_FillRegression_String = TAB + "FillRegression()... <fill TS using regression>",
	__Commands_Fill_FillRepeat_String = TAB + "FillRepeat()... <fill TS by repeating values>",
	__Commands_Fill_FillUsingDiversionComments_String = TAB + "FillUsingDiversionComments()... <use diversion comments as data  - HydroBase ONLY>",

	__Commands_Fill_SetAutoExtendPeriod_String = TAB + "SetAutoExtendPeriod()... <for data filling and manipulation>",
	__Commands_Fill_SetAveragePeriod_String = TAB +	"SetAveragePeriod()... <for data filling>",
	__Commands_Fill_SetIgnoreLEZero_String = TAB + "SetIgnoreLEZero()... <ignore values <= 0 in historical averages>",
	__Commands_SetTimeSeries_String = "Set Time Series Contents",
	__Commands_Set_ReplaceValue_String = TAB + "ReplaceValue()... <replace value (range) with constant in TS>",
	__Commands_Set_SetConstant_String = TAB + "SetConstant()... <set all values to constant in TS>",
	__Commands_Set_SetDataValue_String = TAB + "SetDataValue()... <set a single data value in a TS>",
	__Commands_Set_SetFromTS_String = TAB + "SetFromTS()... <set time series values from another time series>",
	__Commands_Set_SetToMax_String = TAB + "SetToMax()... <set values to maximum of time series>",
	__Commands_Set_SetToMin_String = TAB + "SetToMin()... <set values to minimum of time series>",
    __Commands_Set_SetTimeSeriesProperty_String = TAB + "SetTimeSeriesProperty()... <set time series properties>",

	// Commands...Manipulate Time Series menu...

	__Commands_Manipulate_Add_String = TAB + "Add()... <add one or more TS to another>",
	__Commands_Manipulate_AddConstant_String = TAB + "AddConstant()... <add a constant value to a TS>",
	__Commands_Manipulate_AdjustExtremes_String = TAB + "AdjustExtremes()... <adjust extreme values>",
	__Commands_Manipulate_ARMA_String = TAB + "ARMA()... <lag/attenuate a time series using ARMA>",
	__Commands_Manipulate_Blend_String = TAB + "Blend()... <blend one TS with another>",
    __Commands_Manipulate_ChangePeriod_String = TAB + "ChangePeriod()... <change the period of record>",
	__Commands_Manipulate_ConvertDataUnits_String = TAB + "ConvertDataUnits()... <convert data units>",
	__Commands_Manipulate_Cumulate_String = TAB + "Cumulate()... <cumulate values over time>",
	__Commands_Manipulate_Divide_String = TAB +	"Divide()... <divide one TS by another TS>",
	__Commands_Manipulate_Free_String = TAB + "Free()... <free time series>", 
	__Commands_Manipulate_Multiply_String = TAB + "Multiply()... <multiply one TS by another TS>",
	__Commands_Manipulate_RunningAverage_String = TAB +	"RunningAverage()... <convert TS to running average>",
	__Commands_Manipulate_Scale_String = TAB + "Scale()... <scale TS by a constant>",
	__Commands_Manipulate_ShiftTimeByInterval_String = TAB + "ShiftTimeByInterval()... <shift TS by an even interval>",
	__Commands_Manipulate_Subtract_String = TAB + "Subtract()... <subtract one or more TS from another>",

	// Commands...Output Series menu...

	__Commands_OutputTimeSeries_String = "Output Time Series",
	__Commands_Output_DeselectTimeSeries_String = TAB +	"DeselectTimeSeries()... <deselect time series for output/processing>",
	__Commands_Output_SelectTimeSeries_String = TAB + "SelectTimeSeries()... <select time series for output/processing>",
	__Commands_Output_SetOutputDetailedHeaders_String = TAB + "SetOutputDetailedHeaders()... <in summary reports>",
	__Commands_Output_SetOutputPeriod_String = TAB + "SetOutputPeriod()... <for output products>",
	__Commands_Output_SetOutputYearType_String = TAB + "SetOutputYearType()... <e.g., Calendar and others>",
	__Commands_Output_SortTimeSeries_String = TAB +	"SortTimeSeries()... <sort time series>",
	__Commands_Output_WriteDateValue_String = TAB +	"WriteDateValue()... <write time series to DateValue file>",
	__Commands_Output_WriteHecDss_String = TAB + "WriteHecDss()... <write time series to HEC-DSS file>",
	__Commands_Output_WriteNwsCard_String = TAB + "WriteNwsCard()... <write time series to NWS Card file>",
	__Commands_Output_WriteReclamationHDB_String = TAB + "WriteReclamationHDB()... <write time series to a Reclamation HDB database>",
	__Commands_Output_WriteRiversideDB_String = TAB + "WriteRiversideDB()... <write time series to RiversideDB database>",
	__Commands_Output_WriteRiverWare_String = TAB +	"WriteRiverWare()... <write time series to RiverWare file>",
    __Commands_Output_WriteSHEF_String = TAB + "WriteSHEF()... <write time series to SHEF file (under development)>",
	__Commands_Output_WriteStateCU_String = TAB + "WriteStateCU()... <write time series to StateCU file>",
	__Commands_Output_WriteStateMod_String = TAB + "WriteStateMod()... <write time series to StateMod file>",
	__Commands_Output_WriteSummary_String = TAB + "WriteSummary()... <write time series to Summary file>",
	__Commands_Output_WriteWaterML_String = TAB + "WriteWaterML()... <write time series to WaterML file>",
    __Commands_Output_ProcessTSProduct_String = TAB + "ProcessTSProduct()... <process a time series product file>",

    // Commands...Check Time Series...
    
    __Commands_Check_CheckingResults_String = "Check Time Series",
    __Commands_Check_CheckingResults_CheckTimeSeries_String = TAB + "CheckTimeSeries()... <check time series data values>",
    __Commands_Check_CheckingResults_CheckTimeSeriesStatistic_String = TAB + "CheckTimeSeriesStatistic()... <check time series statistic>",
    __Commands_Check_CheckingResults_WriteCheckFile_String = TAB + "WriteCheckFile()... <write check file>",
    
	// Commands...Analyze Time Series...

	__Commands_AnalyzeTimeSeries_String = "Analyze Time Series",
	__Commands_Analyze_CalculateTimeSeriesStatistic_String = TAB + "CalculateTimeSeriesStatistic()... <determine statistic for time series>",
	__Commands_Analyze_AnalyzePattern_String = TAB + "AnalyzePattern()... <determine pattern(s) for FillPattern()>",
	__Commands_Analyze_CompareTimeSeries_String = TAB + "CompareTimeSeries()... <find differences between time series>",
	__Commands_Analyze_ComputeErrorTimeSeries_String = TAB + "ComputeErrorTimeSeries()... <compute error between time series>",

	// Commands...Models...

	__Commands_Models_Routing_String = "Models - Routing",
	__Commands_Models_Routing_LagK_String = "LagK()... <lag and attenuate (route)>",
	__Commands_Models_Routing_VariableLagK_String = "VariableLagK()... <lag and attenuate (route)>",
    
	// HydroBase commands...

	__Commands_HydroBase_String = "HydroBase",
	__Commands_HydroBase_OpenHydroBase_String = TAB + "OpenHydroBase()... <open HydroBase database connection - PHASING OUT>",

    // Commands...Ensemble processing...
    
    __Commands_Ensemble_String = "Ensemble Processing",
    __Commands_Ensemble_CreateEnsembleFromOneTimeSeries_String =
        TAB + "CreateEnsembleFromOneTimeSeries()... <convert 1 time series into an ensemble>",
    __Commands_Ensemble_CopyEnsemble_String = TAB + "CopyEnsemble()... <create a copy of an ensemble>",
    __Commands_Ensemble_NewEnsemble_String = TAB + "NewEnsemble()... <create a new ensemble from 0+ time series>",
    __Commands_Ensemble_ReadNwsrfsEspTraceEnsemble_String = TAB + "ReadNwsrfsEspTraceEnsemble()... <read 1(+) time series from an NWSRFS ESP trace ensemble file>",
    __Commands_Ensemble_InsertTimeSeriesIntoEnsemble_String = TAB + "InsertTimeSeriesIntoEnsemble()... <insert 1+ time series into an ensemble>",
    __Commands_Ensemble_NewStatisticEnsemble_String = TAB + "NewStatisticEnsemble()... <create an ensemble of statistic time series>",
    __Commands_Ensemble_NewStatisticTimeSeriesFromEnsemble_String = TAB + "NewStatisticTimeSeriesFromEnsemble()... <create a time series as a statistic from an ensemble>",
    __Commands_Ensemble_WeightTraces_String = TAB + "WeightTraces()... <weight traces to create a new time series>",
    __Commands_Ensemble_WriteNwsrfsEspTraceEnsemble_String = TAB + "WriteNwsrfsEspTraceEnsemble()... <write NWSRFS ESP trace ensemble file>",
    
    // Table Commands...

    __Commands_Table_String = "Table Processing",
    __Commands_Table_NewTable_String = TAB + "NewTable()... <create a new empty table>",
    __Commands_Table_CopyTable_String = TAB + "CopyTable()... <create a new table as a full/partial copy of another>",
    __Commands_Table_ReadTableFromDataStore_String = TAB + "ReadTableFromDataStore()... <read a table from a database data store>",
    __Commands_Table_ReadTableFromDelimitedFile_String = TAB + "ReadTableFromDelimitedFile()... <read a table from a delimited file>",
    __Commands_Table_ReadTableFromDBF_String = TAB + "ReadTableFromDBF()... <read a table from a dBASE file>",
    __Commands_Table_TimeSeriesToTable_String = TAB + "TimeSeriesToTable()... <copy time series to a table>",
    __Commands_Table_ManipulateTableString_String = TAB + "ManipulateTableString()... <perform simple manipulation on table strings>",
    __Commands_Table_TableMath_String = TAB + "TableMath()... <perform simple math on table columns>",
    __Commands_Table_TableTimeSeriesMath_String = TAB + "TableTimeSeriesMath()... <perform simple math on table columns and time series>",
    __Commands_Table_SetTimeSeriesPropertiesFromTable_String =
        TAB + "SetTimeSeriesPropertiesFromTable()... <set time series properties from table>",
    __Commands_Table_CopyTimeSeriesPropertiesToTable_String =
        TAB + "CopyTimeSeriesPropertiesToTable()... <copy time series properties to table>",
    __Commands_Table_CompareTables_String = TAB + "CompareTables()... <compare two tables (indicate differences)>",
    __Commands_Table_WriteTableToDelimitedFile_String = TAB + "WriteTableToDelimitedFile()... <write a table to a delimited file>",
    __Commands_Table_WriteTableToHTML_String = TAB + "WriteTableToHTML()... <write a table to an HTML file>",

    // Template Commands...

    __Commands_Template_String = "Template Processing",
    __Commands_Template_ExpandTemplateFile_String = TAB + "ExpandTemplateFile()... <expand a template to the full file>",
    
    // View Commands...

    __Commands_View_String = "View Processing",
    __Commands_View_NewTreeView_String = TAB + "NewTreeView()... <create a tree view for time series results>",
    
	// General Commands...

    __Commands_General_Comments_String = "General - Comments",
    __Commands_General_Comments_Comment_String = TAB + "# comment(s) <insert 1+ comments, each starting with #>",
    __Commands_General_Comments_StartComment_String = TAB + "/* <start multi-line comment section>",
    __Commands_General_Comments_EndComment_String = TAB + "*/ <end multi-line comment section>",
    __Commands_General_Comments_ReadOnlyComment_String = TAB + "#@readOnly <insert read-only comment to protect command file>",
    __Commands_General_Comments_ExpectedStatusFailureComment_String = TAB + "#@expectedStatus Failure <used to test commands>",
    __Commands_General_Comments_ExpectedStatusWarningComment_String = TAB + "#@expectedStatus Warning <used to test commands>",
    
    __Commands_General_FileHandling_String = "General - File Handling",
    __Commands_General_FileHandling_FTPGet_String = TAB + "FTPGet()... <get file(s) using FTP>",
    __Commands_General_FileHandling_WebGet_String = TAB + "WebGet()... <get file(s) from the web>",
    __Commands_General_FileHandling_AppendFile_String = TAB + "AppendFile()... <append file(s)>",
    __Commands_General_FileHandling_RemoveFile_String = TAB + "RemoveFile()... <remove file(s)>",
    __Commands_General_FileHandling_PrintTextFile_String = TAB + "PrintTextFile()... <print a text file>",
    
	__Commands_General_Logging_String = "General - Logging",
	__Commands_General_Logging_StartLog_String = TAB + "StartLog()... <(re)start the log file>",
	__Commands_General_Logging_SetDebugLevel_String = TAB +	"SetDebugLevel()... <set debug message level>",
	__Commands_General_Logging_SetWarningLevel_String = TAB + "SetWarningLevel()... <set debug message level>",

    __Commands_General_Running_String = "General - Running and Properties",
    __Commands_General_Running_ReadPropertiesFromFile_String = TAB + "ReadPropertiesFromFile()... <read processor properties from file>",
    __Commands_General_Running_SetProperty_String = TAB + "SetProperty()... <set a processor property>",
    __Commands_General_Running_SetPropertyFromNwsrfsAppDefault_String =
        TAB + "SetPropertyFromNwsrfsAppDefault()... <set a processor property from an NWSRFS App Default>",
    __Commands_General_Running_FormatDateTimeProperty_String = TAB + "FormatDateTimeProperty()... <format date/time property as string property>",
    __Commands_General_Running_WritePropertiesToFile_String = TAB + "WritePropertiesToFile()... <write processor properties to file>",
    __Commands_General_Running_RunCommands_String = TAB + "RunCommands()... <run a command file>",
	__Commands_General_Running_RunProgram_String = TAB + "RunProgram()... <run an external program>",
    __Commands_General_Running_RunPython_String = TAB + "RunPython()... <run a Python script>",
    __Commands_General_Running_RunDSSUTL_String = TAB + "RunDSSUTL()... <run the HEC DSSUTL program>",
    __Commands_General_Running_Exit_String = TAB + "Exit() <to end processing>",
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
	__Results_Graph_Area_String = "Graph - Area",
	__Results_Graph_AreaStacked_String = "Graph - Area (stacked)",
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
	
	__Results_Ensemble_Graph_Line_String = "Graph - Line",
	__Results_Ensemble_Table_String = "Table",
	__Results_Ensemble_Properties_String = "Ensemble Properties",

	__Results_Table_String = "Table",
	__Results_Report_Summary_Html_String = "Report - Summary (HTML)",
	__Results_Report_Summary_Text_String = "Report - Summary (Text)",
	__Results_TimeSeriesProperties_String = "Time Series Properties",

	// Only for popup Results menu...

	__Results_FindTimeSeries_String = "Find Time Series...",

	// Run menu (order in GUI)...

	__Run_AllCommandsCreateOutput_String = "All Commands (create all output)",
	__Run_AllCommandsIgnoreOutput_String = "All Commands (ignore output commands)",
	__Run_SelectedCommandsCreateOutput_String =	"Selected Commands (create all output)",
	__Run_SelectedCommandsIgnoreOutput_String =	"Selected Commands (ignore output commands)",
	__Run_CancelCommandProcessing_String = "Cancel Command Processing",
	__Run_CancelAllCommandProcesses_String = "Cancel All Command Processes",
	__Run_CommandsFromFile_String = "Commands From File...",
	__Run_ProcessTSProductPreview_String = "Process TS Product File (preview)...",
	__Run_ProcessTSProductOutput_String = "Process TS Product File (create output)...",

	// Tools menu (order in GUI)...

	__Tools_String = "Tools",
		__Tools_Analysis_String = "Analysis",
			__Tools_Analysis_MixedStationAnalysis_String = "Mixed Station Analysis... (under development)",
			__Tools_Analysis_PrincipalComponentAnalysis_String = "Principal Component Analysis... (under development)",
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
		__Tools_Options_String = "Options...",

	// Help menu (order in GUI)...

	__Help_String = "Help",
		__Help_AboutTSTool_String = "About TSTool",
		__Help_ViewDocumentation_String = "View Documentation",
		__Help_ViewTrainingMaterials_String = "View Training Materials",
		__Help_ImportConfiguration_String = "Import Configuration...",

	// Strings used in popup menu for other components...

	__InputName_BrowseHECDSS_String = "Browse for a HEC-DSS file...",
	__InputName_BrowseStateModB_String = "Browse for a StateMod binary file...",
	__InputName_BrowseStateCUB_String = "Browse for a StateCU binary file...",

	__DATA_TYPE_AUTO = "Auto",

	// Input types for the __input_type_JComboBox...
	// Data stores are NOT listed here; consequently, the following are files or databases
	// that have not been converted to data stores

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
	__INPUT_TYPE_RiverWare = "RiverWare",
	__INPUT_TYPE_StateCU = "StateCU",
	__INPUT_TYPE_StateCUB = "StateCUB",
	__INPUT_TYPE_StateMod = "StateMod",
	__INPUT_TYPE_StateModB = "StateModB",
	__INPUT_TYPE_UsgsNwisRdb = "UsgsNwisRdb",
	__INPUT_TYPE_WaterML = "WaterML",

	// Time steps for __time_step_JComboBox...

	__TIMESTEP_AUTO = "Auto",
	__TIMESTEP_MINUTE = "Minute",
	__TIMESTEP_HOUR = "Hour",
	__TIMESTEP_DAY = "Day",
	__TIMESTEP_MONTH = "Month",
	__TIMESTEP_YEAR = "Year",
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
	
	// Read the license information...
	license_InitializeLicenseFromTSToolProperties ();

	// Initialize the input types and data stores based on the configuration
	ui_EnableInputTypesForConfiguration ();
	
	// Disable input types based on the license (regardless of what is in the configuration file)...
    ui_CheckInputTypesForLicense ( license_GetLicenseManager() );

	// Values determined at run-time...

	__props.setHowSet ( Prop.SET_AS_RUNTIME_DEFAULT );
	__props.set ( "WorkingDir", IOUtil.getProgramWorkingDir() );
	Message.printStatus ( 1, "", "Working directory is " + __props.getValue ( "WorkingDir" ) );
	__props.setHowSet ( Prop.SET_AT_RUNTIME_BY_USER );

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
		// Login to Colorado SMS database using information in the CDSS.cfg file...
		uiAction_OpenColoradoSMS ( true );
	}
	sms.stop();
	Message.printDebug(1, rtn, "JTS - OpenColoradoSMS: " + sms.getSeconds());
	swMain.stop();
	Message.printDebug(1, rtn, "JTS - TSTool_JFrame(): " + swMain.getSeconds());
	
	// Show the HydroBase login dialog only if a CDSS license.  For RTi, force the user to
	// use File...Open HydroBase.  Or, configure HydroBase information in the TSTool configuration file.
	// FIXME SAM 2008-10-02 Need to confirm that information can be put in the file
	if ( __source_HydroBase_enabled ) { //&& license_IsInstallCDSS(__licenseManager) ) {
		// Login to HydroBase using information in the TSTool configuration file...
		uiAction_OpenHydroBase ( true );
		// Force the choices to refresh...
		if ( ui_GetHydroBaseDMI() != null ) {
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
			__input_type_JComboBox.select ( __INPUT_TYPE_NWSRFS_FS5Files );
			*/
		}
	}

	// Open remaining data stores.
	// TODO SAM 2010-09-03 migrate more input types to data stores
	try {
	    TSToolMain.openDataStoresAtStartup(__tsProcessor);
	}
	catch ( Exception e ) {
	    Message.printStatus ( 1, rtn, "Error opening data stores (" + e + ")." );
	}
	
	// Populate the data store choices in the UI.
	
	ui_DataStoreList_Populate ();
	
	// Add GUI features that depend on the databases...
	// The appropriate panel will be set visible as input types and data
	// types are chosen.  The panels are currently only initialized once so
	// changing databases or enabling new databases after setup may require
	// some code changes if not implemented already.
	// Set the wait cursor because queries are done during setup.

	JGUIUtil.setWaitCursor ( this, true );
	try {
	    ui_InitGUIInputFilters ( ui_GetInputFilterY() );
	}
	catch ( Exception e ) {
		Message.printWarning ( 3, rtn, "For developers:  caught exception initializing input filters at setup." );
		Message.printWarning ( 3, rtn, e );
	}
	// TODO SAM 2007-01-23 Evaluate use.
	// Force everything to refresh based on the current GUI layout.  Still evaluating this.
	this.invalidate ();
	JGUIUtil.setWaitCursor ( this, false );

	// Use the window listener

	ui_UpdateStatus ( true );

	// If running with a command file from the command line, we don't
	// normally want to see the main window so handle with a special case...

	// TSTool has been started with a command file so try to open and display.  It should already be absolute.
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

		uiAction_ActionPerformed01_MainActions(event);

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
Indicate that a command has been canceled.  The success/failure of the command
is not indicated (see CommandStatusProvider).
@param icommand The command index (0+).
@param ncommand The total number of commands to process
@param command The reference to the command that has been canceled, either the
one that has just been processed, or potentially the next one, depending on when the cancel was requested.
@param percent_complete If >= 0, the value can be used to indicate progress
running a list of commands (not the single command).  If less than zero, then
no estimate is given for the percent complete and calling code can make its
own determination (e.g., ((icommand + 1)/ncommand)*100).
@param message A short message describing the status (e.g., "Running command ..." ).
*/
public void commandCanceled ( int icommand, int ncommand, Command command, float percent_complete, String message )
{	String routine = "TSTool_JFrame.commandCanceled";
	
	// Refresh the results with what is available...
	//String command_string = command.toString();
	ui_UpdateStatusTextFields ( 1, routine,	"Canceled command processing.",
			//"Canceled: " + command_string,
				null, __STATUS_CANCELED );
	uiAction_RunCommands_ShowResults ();
}

/**
Indicate that a command has completed.  The success/failure of the command is not indicated (see CommandStatusProvider).
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
public void commandCompleted ( int icommand, int ncommand, Command command, float percent_complete, String message )
{	String routine = "TSTool_JFrame.commandCompleted";
	// Update the progress bar to indicate progress (1 to number of commands... completed).
	__processor_JProgressBar.setValue ( icommand + 1 );
	// For debugging...
	//Message.printStatus(2,getClass().getName()+".commandCompleted", "Setting processor progress bar to " + (icommand + 1));
	__command_JProgressBar.setValue ( __command_JProgressBar.getMaximum() );
	// Set the tooltip text for the progress bar to indicate the numbers
	String tip = "Completed command " + (icommand + 1) + " of " + ncommand;
    __processor_JProgressBar.setToolTipText ( tip );
	
	if ( ((icommand + 1) == ncommand) || command instanceof Exit_Command ) {
		// Last command has completed (or Exit() command) so refresh the time series results.
		// Only need to do if threaded because otherwise will handle synchronously
		// in the uiAction_RunCommands() method...
		String command_string = command.toString();
		ui_UpdateStatusTextFields ( 1, routine, null, "Processed: " + command_string, __STATUS_READY );
		if ( ui_Property_RunCommandProcessorInThread() ) {
			uiAction_RunCommands_ShowResults ();
		}
	}
}

/**
Determine whether commands are equal.  To allow for multi-line commands, each
command is stored in a list (but typically only the first String is used.
@param original_command Original command as a list of String or Command.
@param edited_command Edited command as a list of String or Command.
*/
private boolean commandList_CommandsAreEqual(List original_command, List edited_command)
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
		original_Object = original_command.get(i);
		edited_Object = edited_command.get(i);
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
		if ( (original_String == null) && (edited_String != null) ) {
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
commands and time series identifiers, only the first command (or time series identifier) is returned.
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
@param commandsToEdit If an update, this contains the current Command instances
to edit.  If a new command, this is null and the action string will be consulted
to construct the appropriate command.  The only time that multiple commands will
be edited are when they are in a {# delimited comment block).
@param mode the action to take when editing the command (INSERT for a
new command or UPDATE for an existing command).
*/
private void commandList_EditCommand ( String action, List<Command> commandsToEdit, CommandEditType mode )
{	String routine = getClass().getName() + ".editCommand";
	int dl = 1; // Debug level
	
    // Make absolutely sure that warning level 1 messages are shown to the user in a dialog.
    // This may have been turned off in command processing.
    // Should not need this if set properly in the command processor.
    //Message.setPropValue ( "ShowWarningDialog=true" );
	
	// FIXME SAM 2011-02-19 Need to do a better job positioning the cursor in the list after edit
	
	if ( mode == CommandEditType.CONVERT ) {
	    // Special case for converting TSID commands to Read() commands...
	    StringBuffer b = new StringBuffer();
	    Command command;
	    boolean requireSpecific = false; // OK to use ReadTimeSeries()
	    if ( !action.equals(__Edit_ConvertTSIDTo_ReadTimeSeries_String)) {
	        requireSpecific = true; // Need to have specific read command
	    }
	    for ( int iCommand = 0; iCommand < commandsToEdit.size(); iCommand++ ) {
	        // Should only operate on TSID, otherwise ignore
	        command = commandsToEdit.get(iCommand);
	        if ( command instanceof TSID_Command ) {
	            // Create a TSIdent from the command
	            try {
	                Command newCommand = TSCommandProcessorUtil.convertTSIDToReadCommand (
	                    __tsProcessor, command.toString(), requireSpecific );
	                // Replace the current command with the new command...
	                int insertPos = commandList_IndexOf(command);
	                commandList_RemoveCommand(command);
	                commandList_InsertCommandAt(newCommand,insertPos);
	                // Run discovery mode on the command...
	                if ( newCommand instanceof CommandDiscoverable ) {
	                    commandList_EditCommand_RunDiscovery ( newCommand );
	                    // TODO SAM 2011-03-21 Should following commands run discovery - could be slow
	                }
	            }
	            catch ( Exception e ) {
	                if ( b.length() != 0 ) {
	                    b.append ( ", " );
	                }
	                b.append ( "\"" + command.toString() + "\" (" + e + ")" );
	            }
	        }
	    }
	    if ( b.length() > 0 ) {
	        b.insert(0, "There were errors converting TSID to Read...() commands:  " );
	        Message.printWarning(1,routine,b.toString());
	    }
	    ui_ShowCurrentCommandListStatus();
	    return;
	}
    
	// Indicate whether the commands are a block of # comments.
	// If so then need to use a special editor rather than typical one-line editors.
	boolean isCommentBlock = false;
	if ( mode == CommandEditType.UPDATE ) {
		isCommentBlock = commandList_IsCommentBlock ( __tsProcessor,
			commandsToEdit,
			true,	// All must be comments
			true );	// Comments must be contiguous
	}
	else {
		// New command, so look for comment actions.
		if ( action.equals(__Commands_General_Comments_Comment_String) ||
		    action.equals(__Commands_General_Comments_ReadOnlyComment_String) ||
            action.equals(__Commands_General_Comments_ExpectedStatusFailureComment_String) ||
            action.equals(__Commands_General_Comments_ExpectedStatusWarningComment_String) ) {
			isCommentBlock = true;
		}
	}
	if ( isCommentBlock ) {
		Message.printStatus(2, routine, "Command is a comment block.");
	}

	try {
        // Main try to help with troubleshooting, especially during
		// transition to new command structure.

	// First make sure we have a Command object to edit.  If an old-style command
	// then it will be stored in a GenericCommand.
	// The Command object is inserted in the processor in any case, to take advantage
	// of processor information (such as being able to get the time series identifiers from previous commands.
	// If a new command is being inserted and a cancel occurs, the command will simply be removed from the list.
	// If an existing command is being updated and a cancel occurs, the changes need to be ignored.
	
	Command commandToEditOriginal = null;	// Command being edited (original).
	Command commandToEdit = null;	// Command being edited (clone).
	if ( mode == CommandEditType.UPDATE ) {
		// Get the command from the processor...
		if ( isCommentBlock ) {
			// Use the string-based editor dialog and then convert each
			// comment line into a command.  Don't do anything to the command list yet
		}
		else {
			// Get the original command...
			commandToEditOriginal = commandsToEdit.get(0);
			// Clone it so that the edit occurs on the copy...
			commandToEdit = (Command)commandToEditOriginal.clone();
			Message.printStatus(2, routine, "Cloned command to edit: \"" + commandToEdit + "\"" );
			// Remove the original command...
			int pos = commandList_IndexOf ( commandToEditOriginal );
			commandList_RemoveCommand ( commandToEditOriginal );
			// Insert the copy during the edit...
			commandList_InsertCommandAt ( commandToEdit, pos );
			Message.printStatus(2, routine,
				"Will edit the copy and restore to the original if the edit is canceled.");
		}
	}
	else if ( mode == CommandEditType.INSERT ) {
		if ( isCommentBlock ) {
			// Don't do anything here.  New comments will be inserted in code below.
		}
		else {
			// New command so create a command as a place-holder for editing (filled out during the editing).
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
		
			commandToEdit = commandList_NewCommand( command_string, true );
			Message.printStatus(2, routine, "Created new command to insert:  \"" + commandToEdit + "\"" );
        
			// Add it to the processor at the insert point of the edit (before the first selected command...
        
			commandList_InsertCommandBasedOnUI ( commandToEdit );
			Message.printStatus(2, routine, "Inserted command for editing.");
		}
	}

	// Second, edit the command, whether an update or an insert...

	boolean edit_completed = false;
	List<String> new_comments = new Vector();	// Used if comments are edited.
	if ( isCommentBlock ) {
		// Edit using the old-style editor...
		edit_completed = commandList_EditCommandOldStyleComments ( mode, action, commandsToEdit, new_comments );
	}
	else {
	    // Editing a single one-line command...
        try {
   			// Edit with the new style editors...
   			Message.printStatus(2, routine, "Editing Command with new-style editor.");
   			edit_completed = commandList_EditCommandNewStyle ( commandToEdit );
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
		if ( mode == CommandEditType.INSERT ) {
			if ( isCommentBlock ) {
				// Insert the comments at the insert point...
				commandList_InsertCommentsBasedOnUI ( new_comments );
			}
			else {
				// The command has already been inserted in the list.
				Message.printStatus(2, routine, "After insert, command is:  \"" + commandToEdit + "\"" );
                // Connect the command to the UI to handle progress when the command is run.
                // TODO SAM 2009-03-23 Evaluate whether to define and interface rather than rely on
                // AbstractCommand here.
                if ( commandToEdit instanceof AbstractCommand ) {
                    ((AbstractCommand)commandToEdit).addCommandProgressListener ( this );
                }
                if ( commandToEdit instanceof CommandDiscoverable ) {
                    commandList_EditCommand_RunDiscovery ( commandToEdit );
                    // TODO SAM 2011-03-21 Should following commands run discovery - could be slow
                }
			}
			commandList_SetDirty(true);
		}
		else if ( mode == CommandEditType.UPDATE ) {
			// The command was updated.
			if ( isCommentBlock ) {
				// Remove the commands that were selected and insert the new ones.
				commandList_ReplaceComments ( commandsToEdit, new_comments );
				if ( !commandList_CommandsAreEqual(commandsToEdit,new_comments)) {
					commandList_SetDirty(true);
				}
			}
			else {
				// The contents of the command will have been modified so there is no need to do anything more.
				Message.printStatus(2, routine, "After edit, command is:  \"" + commandToEdit + "\"" );
				if ( !commandToEditOriginal.toString().equals(commandToEdit.toString())) {
					commandList_SetDirty(true);
				}
                if ( commandToEdit instanceof CommandDiscoverable ) {
                    commandList_EditCommand_RunDiscovery ( commandToEdit );
                    // TODO SAM 2011-03-21 Should following commands run discovery - could be slow
                }
			}
		}
	}
	else {
        // The edit was canceled.  If it was a new command being inserted, remove the command from the processor...
		if ( mode == CommandEditType.INSERT ) {
			if ( isCommentBlock ) {
				// No comments were inserted at start of edit.  No need to do anything.
			}
			else {
				// A temporary new command was inserted so remove it.
				commandList_RemoveCommand(commandToEdit);
				Message.printStatus(2, routine, "Edit was canceled.  Removing from command list." );
			}
		}
		else if ( mode == CommandEditType.UPDATE ) {
			if ( isCommentBlock ) {
				// The original comments will remain.  No need to do anything.
			}
			else {
				// Else was an update so restore the original command...
			    Message.printStatus(2, routine, "Edit was canceled.  Restoring pre-edit command." );
				int pos = commandList_IndexOf(commandToEdit);
				commandList_RemoveCommand(commandToEdit);
				commandList_InsertCommandAt(commandToEditOriginal, pos);
			}
		}
	}
	
	// TODO SAM 2007-12-07 Evaluate whether to refresh the command list status?
    
    ui_ShowCurrentCommandListStatus();

	}
	catch ( Exception e2 ) {
		// TODO SAM 2005-05-18 Evaluate handling of unexpected error... 
		Message.printWarning(1, routine, "Unexpected error editing command (" + e2 + ")." );
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
        JGUIUtil.setWaitCursor ( this, true );
        ((CommandDiscoverable)command_to_edit).runCommandDiscovery(__tsProcessor.indexOf(command_to_edit));
        // Redraw the status area
        ui_ShowCurrentCommandListStatus();
    }
    catch ( Exception e ) {
        // TODO SAM 2011-02-17 Need to show warning to user?  With current design, code should have complete input.
        // For now ignore because edit-time input may not be complete...
        String message = "Unable to make discover run - may be OK if partial data.";
        Message.printStatus(2, routine, message);
        Message.printWarning(3, routine, e);
    }
    finally {
        JGUIUtil.setWaitCursor ( this, false );
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
Edit comments using an old-style editor.
@param mode Mode of editing, whether updating or inserting.
@param action If not null, then the comments are new (insert).
@param command_Vector Comments being edited as a Vector of GenericCommand, as passed from the legacy code.
@param new_comments The new comments as a list of String, to be inserted into the command list.
@return true if the command edits were committed, false if canceled.
*/
private boolean commandList_EditCommandOldStyleComments (
	CommandEditType mode, String action, List<Command> command_Vector, List<String> new_comments )
{	//else if ( action.equals(__Commands_General_Comment_String) ||
	//	command.startsWith("#") ) {
    List<String> cv = new Vector();
	int size = 0;
	if ( command_Vector != null ) {
		size = command_Vector.size();
	}
	Command command = null;
	for ( int i = 0; i < size; i++ ) {
		command = command_Vector.get(i);
		cv.add( command.toString() );
	}
	List<String> edited_cv = new Comment_JDialog ( this, cv ).getText();
	if ( edited_cv == null ) {
		return false;
	}
	else {
		// Transfer to the list that was passed in...
		int size2 = edited_cv.size();
		for ( int i = 0; i < size2; i++ ) {
			new_comments.add ( edited_cv.get(i) );
		}
		return true;
	}
}

/**
Get the list of commands to process, as a list of Command, guaranteed
to be non-null but may be zero length.
@return the commands as a list of Command.
@param get_all If false, return those that are selected in the command list, unless none are selected,
in which case all are returned.  If true, all are returned, regardless of which are selected.
*/
private List<Command> commandList_GetCommands ( boolean get_all )
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
		List<Command> itemVector = new Vector(selected_size);
		for ( int i = 0; i < selected_size; i++ ) {
			itemVector.add ( (Command)__commands_JListModel.get(i) );
		}
		return itemVector;
	}
	else {
	    // Else something selected so get them...
	    List<Command> itemVector = new Vector(selected_size);
		for ( int i = 0; i < selected_size; i++ ) {
			itemVector.add ( (Command)__commands_JListModel.get(selected[i]) );
		}
		return itemVector;
	}
}

/**
Get the list of commands to process.  If any are selected, only they will be
returned.  If none are selected, all will be returned.
@return the commands as a list of String.
*/
private List<Command> commandList_GetCommandsBasedOnUI ( )
{	return commandList_GetCommands ( false );
}

/**
Get the list of commands to process, as a Vector of String.
@return the commands as a list of String.
@param get_all If false, return those that are selected
unless none are selected, in which case all are returned.  If true, all are
returned, regardless of which are selected.
*/
private List<String> commandList_GetCommandStrings ( boolean get_all )
{	// Get the Command list, will not be non-null
    List<Command> commands = commandList_GetCommands ( get_all );
	// Convert to String instances
	int size = commands.size();
	List<String> strings = new Vector(size);
	for ( int i = 0; i < size; i++ ) {
		strings.add ( "" + commands.get(i) );
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
{	return __tsProcessor.indexOf(command);
}

/**
Insert a command at the indicated position.
@param command The Command to insert.
@param pos The index in the command list at which to insert.
*/
private void commandList_InsertCommandAt ( Command command, int pos )
{
	__tsProcessor.insertCommandAt( command, pos );
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
		Message.printStatus(2, routine, "Inserting command \"" + inserted_command + "\" at [" + insert_pos + "]" );
	}
	else {
	    // Insert at end of commands list.
		__commands_JListModel.addElement ( inserted_command );
		insert_pos = __commands_JListModel.size() - 1;
	}
	// Make sure that the list scrolls to the position that has been updated...
	if ( insert_pos >= 0 ) {
	    ui_GetCommandJList().ensureIndexIsVisible ( insert_pos );
	}
	// Since an insert, mark the commands list as dirty...
	//commandList_SetDirty(true);
}

/**
Insert comments into the command list, utilizing the selected commands in the displayed
list to determine the insert position.
@param new_comments The comments to insert, as a list of String.
*/
private void commandList_InsertCommentsBasedOnUI ( List<String> new_comments )
{	String routine = getClass().getName() + ".commandList_InsertCommentsBasedOnUI";

	// Get the selected indices from the commands...
	int selectedIndices[] = ui_GetCommandJList().getSelectedIndices();
	int selectedSize = selectedIndices.length;
	
	int size = new_comments.size();

	int insert_pos = 0;
	Command inserted_command = null;	// New comment line as Command
	for ( int i = 0; i < size; i++ ) {
		inserted_command = commandList_NewCommand ( new_comments.get(i), true );
		// Check the command parameters to trigger an OK status - otherwise
		// the UI will decorate the command to indicate status unknown
		try {
		    inserted_command.checkCommandParameters(null, "", 3);
		}
		catch ( Exception e ) {
		    // Should not happen.
		}
		if (selectedSize > 0) {
			// Insert before the first selected item...
			int insert_pos0 = selectedIndices[0];
			insert_pos = insert_pos0 + i;
			__commands_JListModel.insertElementAt (	inserted_command, insert_pos );
			Message.printStatus(2, routine, "Inserting comment \"" +
				inserted_command + "\" at [" + insert_pos + "]" );
		}
		else {
		    // Insert at end of command list.
			__commands_JListModel.addElement ( inserted_command );
			insert_pos = __commands_JListModel.size() - 1;
		}
	}
	// Make sure that the list scrolls to the position that has been updated...
	if ( insert_pos >= 0 ) {
	    ui_GetCommandJList().ensureIndexIsVisible ( insert_pos );
	}
	// Since an insert, mark the command list as dirty...
	//commandList_SetDirty(true);
}

/**
Determine whether a list of commands is a comment block consisting of multiple # comments.
@param processor The TSCommandProcessor that is processing the results,
used to check for positions of commands.
@param commands Vector of Command instances to check.
@param allMustBeComments If true then all must be comment lines
for true to be returned.  If false, then only one must be a comment.
This allows a warning to be printed that only a block of ALL comments can be edited at once.
@param must_be_contigous If true, then the comments must be contiguous
for true to be returned.  The GUI code should check this and disallow comment edits if not contiguous.
*/
private boolean commandList_IsCommentBlock ( TSCommandProcessor processor,
        List<Command> commands, boolean allMustBeComments, boolean mustBeContiguous )
{
	int size_commands = commands.size();
	boolean is_comment_block = true;
	boolean is_contiguous = true;
	// Loop through the commands to check...
	Command command = null;
	int comment_count = 0;
	int pos_prev = -1;
	for ( int i = 0; i < size_commands; i++ ) {
		command = commands.get(i);
		if ( command instanceof Comment_Command ) {
			++comment_count;
		}
		// Get the index position in the commands processor and check for contiguousness.
		int pos = processor.indexOf(command);
		if ( (i > 0) && (pos != (pos_prev + 1)) ) {
			is_contiguous = false;
		}
		// Save the position for the next check for contiguity...
		pos_prev = pos;
	}
	if ( mustBeContiguous && !is_contiguous ) {
		is_comment_block = false;
	}
	if ( allMustBeComments && (comment_count != size_commands) ) {
		is_comment_block = false;
	}
	return is_comment_block;
}

/**
Create a new Command instance given a command string.  This may be called when
loading commands from a file or adding new commands while editing.
@param commandString Command as a string, to parse and create a Command instance.
@param createUnknownCommandIfNotRecognized Indicate if a generic command should
be created if not recognized.  For now this should generally be true, until all
commands are recognized by the TSCommandFactory.
*/
private Command commandList_NewCommand ( String commandString, boolean createUnknownCommandIfNotRecognized )
{	int dl = 1;
	String routine = getClass().getName() + ".newCommand";
	if ( Message.isDebugOn ) {
		Message.printDebug ( dl, routine,
		"Using command factory to create a new command for \"" + commandString + "\"" );
	}
	Command c = null;
	try {
		TSCommandFactory cf = new TSCommandFactory();
		c = cf.newCommand(commandString);
		Message.printStatus ( 2, routine, "Created command from factory for \"" + commandString + "\"");
	}
	catch ( UnknownCommandException e ) {
		// Processor does not know the command so create an UnknownCommand.
		c = new UnknownCommand();
		Message.printStatus ( 2, routine, "Created unknown command for \"" + commandString + "\"");
	}
	// TODO SAM 2007-08-31 This is essentially validation.
	// Need to evaluate for old-style commands, impacts on error-handling.
	// New is command from the processor
	try {
	    c.initializeCommand ( commandString, __tsProcessor, true );	// Full initialization
		if ( Message.isDebugOn ) {
			Message.printDebug ( dl, routine, "Initialized command for \"" + commandString + "\"" );
		}
	}
	catch ( Exception e ) {
		// Absorb the warning and make the user try to deal with it in the editor
		// dialog.  They can always cancel out.

		// TODO SAM 2005-05-09 Need to handle parse error.  Should the editor come
		// up with limited information?
		Message.printWarning ( 3, routine, "Unexpected error initializing command \"" + commandString + "\"." );
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
Replace a command with another.  This is used, for example, when converting commands to/from comments.
@param old_command Old command to remove.
@param new_command New command to insert in its place.
*/
private void commandList_ReplaceCommand ( Command old_command, Command new_command )
{
	// Probably could get the index passed in from list operations but
	// do the lookup through the data model to be more independent.
	int pos_old = __tsProcessor.indexOf(old_command);
	if ( pos_old < 0 ) {
		// Can't find the old command so return.
		return;
	}
	// Remove the old command...
	__tsProcessor.removeCommandAt ( pos_old );
	// Insert the new command at the same position.  Handle the case that
	// it is now at the end of the list.
	if ( pos_old < __tsProcessor.size() ) {
		// Have enough elements to add at the requested position...
		__tsProcessor.insertCommandAt( new_command, pos_old );
	}
	else {
		// Add at the end...
		__tsProcessor.addCommand ( new_command );
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
private void commandList_ReplaceComments ( List<Command> old_comments, List<String> new_comments )
{	//String routine = getClass().getName() + ".commandList_ReplaceComments";
	// Probably could get the index passed in from list operations but
	// do the lookup through the data model to be more independent.
	int pos_old = __tsProcessor.indexOf((Command)old_comments.get(0));
	if ( pos_old < 0 ) {
		// Can't find the old command so return.
		return;
	}
	// Remove the old commands.  They will shift so OK to keep removing at the single index.
	int size = old_comments.size();
	for ( int i = 0; i < size; i++ ) {
		__tsProcessor.removeCommandAt ( pos_old );
	}
	// Insert the new commands at the same position.  Handle the case that
	// it is now at the end of the list.
	int size_new = new_comments.size();
	if ( pos_old < __tsProcessor.size() ) {
		// Have enough elements to add at the requested position...
		for ( int i = 0; i < size_new; i++ ) {
			Command new_command = commandList_NewCommand ( new_comments.get(i), true );
			// Check the command parameters to trigger an OK status - otherwise
	        // the UI will decorate the command to indicate status unknown
	        try {
	            new_command.checkCommandParameters(null, "", 3);
	        }
	        catch ( Exception e ) {
	            // Should not happen.
	        }
			//Message.printStatus ( 2, routine, "Inserting " + new_command + " at " + (pos_old + 1));
			__tsProcessor.insertCommandAt( new_command, (pos_old + i) );
		}
	}
	else {
		// Add at the end...
		for ( int i = 0; i < size_new; i++ ) {
			Command new_command = commandList_NewCommand ( new_comments.get(i), true );
			//Message.printStatus ( 2, routine, "Adding " + new_command + " at end" );
			__tsProcessor.addCommand ( new_command );
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
that display the command file name.  It DOES NOT cause the commands to be reloaded - it is a simple setter.
@param command_file_name Name of current command file (can be null).
*/
private void commandList_SetCommandFileName ( String commandFileName )
{	// Set the file name used in the TSTool UI...
	__commandFileName = commandFileName;
	// Also set the initial working directory for the processor as the parent folder of the command file...
    if ( commandFileName != null ) {
        File file = new File ( commandFileName );
        commandProcessor_SetInitialWorkingDir ( file.getParent(), true );
    }
	// Update the title bar...
	ui_UpdateStatus ( false );
}

/**
Indicate whether the commands have been modified.  The application title is also updated to indicate this.
@param dirty Specify as true if the commands have been modified in some way.
*/
private void commandList_SetDirty ( boolean dirty )
{	__commandsDirty = dirty;
	ui_UpdateStatus ( false );
	// TODO SAM 2007-08-31 Evaluate whether processor should have "dirty" property.
}

/**
Indicate the progress that is occurring within a command.  This may be a chained call
from a CommandProcessor that implements CommandListener to listen to a command.  This
level of monitoring is useful if more than one progress indicator is present in an application UI.
@param istep The number of steps being executed in a command (0+).
@param nstep The total number of steps to process within a command.
@param command The reference to the command that is running.
@param percentComplete If >= 0, the value can be used to indicate progress
running a single command (not the single command).  If less than zero, then
no estimate is given for the percent complete and calling code can make its
own determination (e.g., ((istep + 1)/nstep)*100).
@param message A short message describing the status (e.g., "Running command ..." ).
*/
public void commandProgress ( int istep, int nstep, Command command, float percentComplete, String message )
{	if ( istep == 0 ) {
		// Initialize the limits of the command progress bar.
		__command_JProgressBar.setMinimum ( 0 );
		__command_JProgressBar.setMaximum ( nstep );
	}
	// Set the current value...
    if ( percentComplete > 0.0 ) {
        // Calling code is providing the percent complete
        __command_JProgressBar.setValue ( (int)(nstep*percentComplete/100.0) );
    }
    else {
        // Calling code wants percentage to be computed here
        __command_JProgressBar.setValue ( istep + 1 );
    }
}

// All of the following methods perform and interaction with the command processor,
// beyond basic command list insert/delete/update.

/**
Kill all processes that are still running for RunProgram(), RunDSSUTL(), and RunPython() commands.
This may be needed because a process is hung (e.g., waiting for input).
*/
private void commandProcessor_CancelAllCommandProcesses ()
{   String routine = getClass().getName() + ".commandProcessor_CancelAllCommandProcesses";
    Message.printStatus(2, routine, "Killing all processes started by commands..." );
    TSCommandProcessorUtil.killCommandProcesses(__tsProcessor.getCommands());
}

/**
Get the command processor AutoExtendPeriod.  This method is meant for simple
reporting.  Any errors in the processor should be detected during command initialization and processing.
@return The global AutoExtendPeriod as a Boolean from the command processor or null if not yet determined.
*/
private Boolean commandProcessor_GetAutoExtendPeriod()
{	if ( __tsProcessor == null ) {
		return null;
	}
	Object o = null;
	try {
	    o = __tsProcessor.getPropContents ( "AutoExtendPeriod" );
	}
	catch ( Exception e ) {
		return null;
	}
	if ( o == null ) {
		return null;
	}
	else {
	    return (Boolean)o;
	}
}

/**
Return the command processor instance that is being used.  This method should be
called to avoid direct interaction with the processor data member.
@return the TSCommandProcessor instance that is being used.
*/
private TSCommandProcessor commandProcessor_GetCommandProcessor ()
{
    return __tsProcessor;
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
    if ( __tsProcessor == null ) {
        return null;
    }
    PropList request_params = new PropList ( "" );
    request_params.setUsingObject ( "Index", new Integer(pos) );
    CommandProcessorRequestResultsBean bean = null;
    try {
        bean = __tsProcessor.processRequest( "GetEnsembleAt", request_params);
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
        o = __tsProcessor.getPropContents ( "EnsembleResultsList" );
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
reporting.  Any errors in the processor should be detected during command initialization and processing.
@return The HydroBaseDMIList as a Vector from the command processor or null if
not yet determined or no connections.
*/
private List<HydroBaseDMI> commandProcessor_GetHydroBaseDMIList()
{	if ( __tsProcessor == null ) {
		return null;
	}
	Object o = null;
	try {
	    o = __tsProcessor.getPropContents ( "HydroBaseDMIList" );
	}
	catch ( Exception e ) {
		return null;
	}
	if ( o == null ) {
		return null;
	}
	else {
	    return (List<HydroBaseDMI>)o;
	}
}

/**
Get the command processor IncludeMissingTS.  This method is meant for simple
reporting.  Any errors in the processor should be detected during command initialization and processing.
@return The global IncludeMissingTS as a Boolean from the command processor or null if not yet determined.
*/
private Boolean commandProcessor_GetIncludeMissingTS()
{	if ( __tsProcessor == null ) {
		return null;
	}
	Object o = null;
	try {
	    o = __tsProcessor.getPropContents ( "IncludeMissingTS" );
	}
	catch ( Exception e ) {
		return null;
	}
	if ( o == null ) {
		return null;
	}
	else {
	    return (Boolean)o;
	}
}

/**
Get the command processor InputEnd.  This method is meant for simple
reporting.  Any errors in the processor should be detected during command initialization and processing.
@return The global InputEnd as a DateTime from the command processor or null if not yet determined.
*/
private DateTime commandProcessor_GetInputEnd()
{	if ( __tsProcessor == null ) {
		return null;
	}
	Object o = null;
	try {
	    o = __tsProcessor.getPropContents ( "InputEnd" );
	}
	catch ( Exception e ) {
		return null;
	}
	if ( o == null ) {
		return null;
	}
	else {
	    return (DateTime)o;
	}
}

/**
Get the command processor InputStart.  This method is meant for simple
reporting.  Any errors in the processor should be detected during command initialization and processing.
@return The global InputStart as a DateTime from the command processor or null if not yet determined.
*/
private DateTime commandProcessor_GetInputStart()
{	if ( __tsProcessor == null ) {
		return null;
	}
	Object o = null;
	try {
	    o = __tsProcessor.getPropContents ( "InputStart" );
	}
	catch ( Exception e ) {
		return null;
	}
	if ( o == null ) {
		return null;
	}
	else {
	    return (DateTime)o;
	}
}

/**
Get the command processor OutputEnd.  This method is meant for simple
reporting.  Any errors in the processor should be detected during command initialization and processing.
@return The global OutputEnd as a DateTime from the command processor or null if not yet determined.
*/
private DateTime commandProcessor_GetOutputEnd()
{	if ( __tsProcessor == null ) {
		return null;
	}
	Object o = null;
	try {
	    o = __tsProcessor.getPropContents ( "OutputEnd" );
	}
	catch ( Exception e ) {
		return null;
	}
	if ( o == null ) {
		return null;
	}
	else {
	    return (DateTime)o;
	}
}

/**
Get the command processor OutputStart.  This method is meant for simple
reporting.  Any errors in the processor should be detected during command
initialization and processing.
@return The global OutputStart as a DateTime from the command processor or null if not yet determined.
*/
private DateTime commandProcessor_GetOutputStart()
{	if ( __tsProcessor == null ) {
		return null;
	}
	Object o = null;
	try {
	    o = __tsProcessor.getPropContents ( "OutputStart" );
	}
	catch ( Exception e ) {
		return null;
	}
	if ( o == null ) {
		return null;
	}
	else {
	    return (DateTime)o;
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
    if ( __tsProcessor == null ) {
        return null;
    }
    PropList request_params = new PropList ( "" );
    request_params.set ( "TableID", table_id );
    CommandProcessorRequestResultsBean bean = null;
    try { bean =
        __tsProcessor.processRequest( "GetTable", request_params);
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
private List<DataTable> commandProcessor_GetTableResultsList()
{   String routine = "TSTool_JFrame.commandProcessorGetTableResultsList";
    Object o = null;
    try {
        o = __tsProcessor.getPropContents ( "TableResultsList" );
    }
    catch ( Exception e ) {
        String message = "Error requesting TableResultsList from processor.";
        Message.printWarning(2, routine, message );
    }
    if ( o == null ) {
        return null;
    }
    else {
        return (List<DataTable>)o;
    }
}

/**
Get the command processor time series results for an index position.
Typically this corresponds to a user selecting the time series from the
results list, for further display.
@param pos Position (0+ for the time series).
@return The time series at the requested position in the results or null if the processor is not available.
*/
private TS commandProcessor_GetTimeSeries( int pos )
{	String message, routine = "TSTool_JFrame.commandProcessorGetTimeSeries";
	if ( __tsProcessor == null ) {
		return null;
	}
	PropList request_params = new PropList ( "" );
	request_params.setUsingObject ( "Index", new Integer(pos) );
	CommandProcessorRequestResultsBean bean = null;
	try { bean =
		__tsProcessor.processRequest( "GetTimeSeries", request_params);
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
private List commandProcessor_GetTimeSeriesResultsList()
{	String routine = "TSTool_JFrame.commandProcessorGetTimeSeriesResultsList";
	Object o = null;
	try {
	    o = __tsProcessor.getPropContents ( "TSResultsList" );
	}
	catch ( Exception e ) {
		String message = "Error requesting TSResultsList from processor.";
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
Get the size of the command processor time series results list.
@return the size of the command processor time series results list.
*/
private int commandProcessor_GetTimeSeriesResultsListSize()
{
    List results = commandProcessor_GetTimeSeriesResultsList();
	if ( results == null ) {
		return 0;
	}
	return results.size();
}

/**
Get the command processor time series view results list.
@return The time series view results list or null if the processor is not available.
*/
private List<TimeSeriesView> commandProcessor_GetTimeSeriesViewResultsList()
{   String routine = "TSTool_JFrame.commandProcessor_GetTimeSeriesViewResultsList";
    Object o = null;
    try {
        o = __tsProcessor.getPropContents ( "TimeSeriesViewResultsList" );
    }
    catch ( Exception e ) {
        String message = "Error requesting TimeSeriesViewResultsList from processor.";
        Message.printWarning(2, routine, message );
    }
    if ( o == null ) {
        return null;
    }
    else {
        return (List<TimeSeriesView>)o;
    }
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
        o_tslist = __tsProcessor.getPropContents ( "TSResultsList" );
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
    List tslist = (List)o_tslist;
    int tslist_size = tslist.size();
    if ( tslist_size == 0 ) {
        String message = "No time series are available from processor.";
        Message.printWarning(1, routine, message );
        return;
    }
    // Get the list of ensembles...
    List match_index_Vector = new Vector();   // List of matching time series indices
    for ( int i = 0; i < indices.length; i++ ) {
        PropList request_params = new PropList ( "" );
        // String is of format 1) EnsembleID - EnsembleName where Ensemble ID can contain dashes but generally not
        // dashes and spaces
        String fullLabel = (String)__resultsTSEnsembles_JListModel.elementAt(indices[i]);
        int posParen = fullLabel.indexOf(")");
        int posDash = fullLabel.indexOf(" -");
        String EnsembleID = fullLabel.substring(posParen + 1, posDash).trim();
        Message.printStatus ( 2, routine, "Getting processor ensemble results for \"" + EnsembleID + "\"" );
        request_params.setUsingObject ( "EnsembleID", EnsembleID );
        CommandProcessorRequestResultsBean bean = null;
        try {
            bean = __tsProcessor.processRequest( "GetEnsemble", request_params );
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
                ts = (TS)tslist.get(its);
                if ( ens_ts == ts ) {
                    // Have a matching time series
                    match_index_Vector.add ( new Integer(its));
                }
            }
        }
    }
    // Convert the indices to an array
    int [] indices2 = new int[match_index_Vector.size()];
    for ( int i = 0; i < indices2.length; i++ ) {
        indices2[i] = ((Integer)match_index_Vector.get(i)).intValue();
    }
    // Now display the list of time series
    PropList request_params = new PropList ( "" );
    request_params.setUsingObject ( "Indices", indices2 );
    request_params.setUsingObject ( "Properties", props );

    try { //bean =
        __tsProcessor.processRequest( "ProcessTimeSeriesResultsList", request_params );
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
		__tsProcessor.processRequest( "ProcessTimeSeriesResultsList", request_params );
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
	__tsProcessor.readCommandFile ( path,
			true,	// Create UnknownCommand instances for unrecognized commands
			false );// Do not append to the current processor contents
    // Refresh the GUI list to show the status done in call to this method
	
	// TODO SAM 2008-05-11 Evaluate whether to move this to the readCommandFile() method.
	// If any lines in the file are different from the commands, mark the file as dirty.
	// Changes may automatically occur during the load because of automated updates to commands.
	BufferedReader in = new BufferedReader ( new InputStreamReader(IOUtil.getInputStream ( path )) );
	List strings = new Vector();
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
	if ( size_orig != __tsProcessor.size() ) {
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
	    command = __tsProcessor.get(i);
	    if ( !line.equals(command.toString()) ) {
	        Message.printStatus( 2, routine, "Command " + (i + 1) +
	            " was automatically updated during load (usually due to software update)." );
	        commandList_SetDirty ( true );
	        ++numAutoChanges;
	        if ( command instanceof CommandStatusProvider ) {
	            csp = (CommandStatusProvider)command;
	            // FIXME SAM 2008-05-11 This message gets clobbered by re-initialization before running
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
interface.  In the future the command processor may put together the list without being passed from the GUI.
@param commands List of commands to run.
@param createOutput whether to create output (slower) or skip those commands.
*/
private void commandProcessor_RunCommandsThreaded ( List commands, boolean createOutput )
{	String routine = "TSTool_JFrame.commandProcessor_RunCommandsThreaded";

	PropList requestParams = new PropList ( "" );
	requestParams.setUsingObject ( "CommandList", commands );
	requestParams.setUsingObject ( "InitialWorkingDir", ui_GetInitialWorkingDir() );
	requestParams.setUsingObject ( "CreateOutput", new Boolean(createOutput) );
	try {
		TSCommandProcessorThreadRunner runner = new TSCommandProcessorThreadRunner ( __tsProcessor, requestParams );
		Message.printStatus ( 2, routine, "Running commands in separate thread.");
		Thread thread = new Thread ( runner );
		thread.start();
		// Do one update of the GUI to reflect the GUI running.  This will disable run
		// buttons, etc. until the current run is done.
		ui_CheckGUIState ();
		// At this point the GUI will get updated if any notification fires from the processor.
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
	commandProcessor_SetHydroBaseDMI( __tsProcessor, hbdmi );
}

/**
Set the command processor HydroBase instance that is opened via the GUI.
This version is generally called by the overloaded version and when processing an external command file.
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
@param setWorkingDir if true, also set the working directory to the same value
*/
private void commandProcessor_SetInitialWorkingDir ( String InitialWorkingDir, boolean setWorkingDir )
{   String routine = getClass().getName() + ".commandProcessor_setInitialWorkingDir";
	try {
		__tsProcessor.setPropContents( "InitialWorkingDir", InitialWorkingDir );
	}
	catch ( Exception e ) {
		String message = "Error setting InitialWorkingDir(\"" + InitialWorkingDir + "\") in processor.";
		Message.printWarning(2, routine, message );
	}
	if ( setWorkingDir ) {
	    try {
	        __tsProcessor.setPropContents( "WorkingDir", InitialWorkingDir );
	    }
	    catch ( Exception e ) {
	        String message = "Error setting WorkingDir(\"" + InitialWorkingDir + "\") in processor.";
	        Message.printWarning(2, routine, message );
	    }
	}
}

/**
Set the command processor NWSRFS FS5Files DMI instance that is opened via the GUI.
@param nwsrfs_dmi Open NWSRFS FS5Files instance.
*/
private void commandProcessor_SetNWSRFSFS5FilesDMI( NWSRFS_DMI nwsrfs_dmi )
{	commandProcessor_SetNWSRFSFS5FilesDMI( __tsProcessor, nwsrfs_dmi );
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
Indicate that a command has started running.
@param icommand The command index (0+) in the list of commands being run (see ncommand)
@param ncommand The total number of commands to process (will be selected number if running selected)
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
	String tip = "Running command " + (icommand + 1) + " of " + ncommand;
	// If RunCommands() is being run, then also put the input file name to make it easier to understand progress
	String additionalInfo = "";
	String commandName = command.getCommandName();
	if ( commandName.equalsIgnoreCase("RunCommands") ) {
	    additionalInfo = command.getCommandParameters().getValue("InputFile");
	    if ( additionalInfo.length() > 30 ) {
	        additionalInfo = additionalInfo.substring(0,30);
	    }
	    additionalInfo += "...";
	}
	ui_UpdateStatusTextFields ( 1, routine, null, tip + ": \"" + commandName + "(..." +
	    additionalInfo + ")\"", __STATUS_BUSY );
	if ( icommand == 0 ) {
		__processor_JProgressBar.setMinimum ( 0 );
		__processor_JProgressBar.setMaximum ( ncommand );
		__processor_JProgressBar.setValue ( 0 );
		Message.printStatus(2, getClass().getName()+".commandStarted", "Setting processor progress bar limits to 0 to " + ncommand );
	}
	// Set the tooltip text for the progress bar to indicate the numbers
	__processor_JProgressBar.setToolTipText ( tip );
	// Always set the value for the command progress so that it shows up
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
public void geoViewInfo(GRShape devlimits, GRShape datalimits, List selected)
{
}

/**
Do nothing.
*/
public void geoViewInfo(GRPoint devlimits, GRPoint datalimits, List selected)
{
}

/**
Do nothing.
*/
public void geoViewInfo(GRLimits devlimits, GRLimits datalimits, List selected)
{
}

/**
Handle the mouse motion event from another GeoView (likely a ReferenceGeoView).  Does nothing.
@param devpt Coordinates of mouse in device coordinates(pixels).
@param datapt Coordinates of mouse in data coordinates.
*/
public void geoViewMouseMotion(GRPoint devpt, GRPoint datapt)
{
}

// TODO SAM 2006-03-02 Evaluate code
// Should the select use the coordinates?  Not all time series have this information available
/**
If a selection is made from the map, use the attributes specified in the lookup
table file to match the attributes in the layer with time series in the query
list.  The coordinates of the select ARE NOT used in the selection.
@param devlimits Limits of select in device coordinates(pixels).
@param datalimits Limits of select in data coordinates.
@param selected Vector of selected GeoRecord.  This is used to match the
attributes for the selected feature with the time series query list.
*/
public void geoViewSelect (	GRShape devlimits, GRShape datalimits, List selected, boolean append )
{	String routine = "TSTool_JFrame.geoViewSelect";

	try {
	    // Main try, for development and troubleshooting
    	Message.printStatus ( 1, routine, "Selecting time series that match map selection." );
    
    	// Select from the time series query list matching the attributes in the selected layer.
    
    	// TODO SAM 2006-03-02 Need to evaluate how to best read this file once.
    
    	// Read the time series to layer lookup file...
    
    	String filename = TSToolMain.getPropValue ( "TSTool.MapLayerLookupFile" );
    	if ( filename == null ) {
    		Message.printWarning ( 1, routine,
    		"The TSTool.MapLayerLookupFile is not defined - cannot link map and time series." );
    		return;
    	}
    
    	String full_filename = IOUtil.getPathUsingWorkingDir ( filename );
    	if ( !IOUtil.fileExists(full_filename) ) {
    		Message.printWarning ( 1, routine, "The map layer lookup file \"" + full_filename +
    		"\" does not exist.  Cannot link map and time series." );
    		return;
    	}
    	
    	PropList props = new PropList ("");
    	props.set ( "Delimiter=," );		// see existing prototype
    	props.set ( "CommentLineIndicator=#" );	// New - skip lines that start with this
    	props.set ( "TrimStrings=True" );	// If true, trim strings after reading.
    	DataTable table = null;
    	try {
    	    table = DataTable.parseFile ( full_filename, props );
    	}
    	catch ( Exception e ) {
    		Message.printWarning ( 1, routine, "Error reading the map layer lookup file \"" + full_filename +
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
    			Message.printWarning ( 1, routine, "Error finding TS_InputType column in lookup file \"" +
    			full_filename + "\".  Cannot link map and time series." );
    			Message.printWarning ( 3, routine, e );
    			JGUIUtil.setWaitCursor ( this, false);
    			return;
    		}
    		try {
    		    TS_DataTypeCol_int = table.getFieldIndex("TS_DataType");
    		}
    		catch ( Exception e ) {
    			Message.printWarning ( 1, routine, "Error finding TS_InputType column in lookup file \"" +
    			full_filename + "\".  Cannot link map and time series." );
    			Message.printWarning ( 3, routine, e );
    			JGUIUtil.setWaitCursor ( this, false);
    			return;
    		}
    		try {
    		    TS_IntervalCol_int = table.getFieldIndex("TS_Interval");
    		}
    		catch ( Exception e ) {
    			Message.printWarning ( 1, routine, "Error finding TS_Interval column in lookup file \"" +
    			full_filename + "\".  Cannot link map and time series." );
    			Message.printWarning ( 3, routine, e );
    			JGUIUtil.setWaitCursor ( this, false);
    			return;
    		}
    		try {
    		    Layer_NameCol_int = table.getFieldIndex ( "Layer_Name");
    		}
    		catch ( Exception e ) {
    			Message.printWarning ( 1, routine, "Error finding Layer_Name column in lookup file \"" +
    			full_filename + "\".  Cannot link map and time series." );
    			Message.printWarning ( 3, routine, e );
    			JGUIUtil.setWaitCursor ( this, false);
    			return;
    		}
    		try {
    		    Layer_LocationCol_int = table.getFieldIndex ("Layer_Location");
    		}
    		catch ( Exception e ) {
    			Message.printWarning ( 1, routine, "Error finding Layer_Location column in lookup file \""+
    			full_filename + "\".  Cannot link map and time series." );
    			Message.printWarning ( 3, routine, e );
    			JGUIUtil.setWaitCursor ( this, false);
    			return;
    		}
    		try {
    		    Layer_DataSourceCol_int = table.getFieldIndex ("Layer_DataSource");
    		}
    		catch ( Exception e ) {
    			// Non-fatal...
    			Message.printWarning ( 3, routine, "Error finding TS_InputType column in lookup file \"" +
    			full_filename + "\".  Data source will not be considered in lookups." );
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
    	// if they want).  In this way multiple selections can be made from the map.
    
    	int nrows = __query_TableModel.getRowCount();
    	Message.printStatus ( 1, routine, "Selecting query list time series based on map selections..." );
    	JGUIUtil.setWaitCursor ( this, true );
    	ui_SetIgnoreListSelectionEvent ( true ); // To increase performance during transfer...
    	ui_SetIgnoreItemEvent ( true );	// To increase performance
    	int match_count = 0;
    	int ngeo = 0;
    	if ( selected != null ) {
    		ngeo = selected.size();
    	}
    	// Loop through the selected features...
    	GeoRecord georec;
    	GeoLayerView geolayerview = null;
    	TableRecord rec = null; // Lookup table record.
    	String geolayer_name = null;
    	TSTool_TS_TableModel generic_tm = null;
    	String ts_inputtype = null; // The TS input type from lookup file
    	String ts_datatype = null; // The TS data type from lookup file
    	String ts_interval = null; // The TS interval from lookup file
    	String layer_name = null; // The layer name from lookup file
    	String layer_location = null; // The layer location attribute from the lookup file
    	int layer_location_field_int =0; // Column index in attribute table for georecord location
    	String layer_datasource = null; // The layer data source attribute from the lookup file
    	int layer_datasource_field_int =0; // Column index in attribute table for georecord data source
    	String georec_location = null; // The georec location value.
    	String georec_datasource = null; // The georec data source value.
    	String tslist_id = null; // The time series location ID from the time series list.
    	String tslist_datasource = null; // The time series data source from the time series list.
    	String tslist_datatype = null; // The time series data type from the time series list.
    	String tslist_interval = null; // The time series interval from the time series list.
    	for ( int igeo = 0; igeo < ngeo; igeo++ ) {
    		// Get the GeoRecord that was selected...
    		georec = (GeoRecord)selected.get(igeo);
    		// Get the name of the layer that corresponds to the GeoRecord,
    		// which is used to locate the record in the lookup file...
    		geolayerview = georec.getLayerView();
    		geolayer_name = geolayerview.getName();
    		// Find layer that matches the record, using the layer name in
    		// the lookup table.  More than one visible layer may be
    		// searched.  The data interval is also used to find a layer to match.
    		for ( int ilook = 0; ilook < tsize; ilook++ ) {
    			Message.printStatus ( 2, routine, "Searching lookup file for the layer named \"" + geolayer_name + "\"" );
    			try {
    			    rec = table.getRecord(ilook);
    				ts_inputtype = (String)rec.getFieldValue(TS_InputTypeCol_int);
    				ts_datatype = (String)rec.getFieldValue(TS_DataTypeCol_int);
    				ts_interval = (String)rec.getFieldValue(TS_IntervalCol_int);
    				layer_name = (String)rec.getFieldValue(Layer_NameCol_int);
    				layer_location = (String)rec.getFieldValue(Layer_LocationCol_int);
    				layer_datasource = (String)rec.getFieldValue(Layer_DataSourceCol_int);
    				// TODO SAM 2006-03-02 Evaluate code
    				// Add layer_interval if such an attribute exists, and use this in addition to the
    				// layer name to find an appropriate layer in the lookup table.
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
    				Message.printWarning ( 3, routine, "Unable to get data from lookup.  Ignoring record." );
    				Message.printWarning ( 3, routine, e );
    				continue;
    			}
    			if ( !geolayer_name.equalsIgnoreCase( layer_name) ) {
    				// The lookup record layer name does not match the selected GeoRecord layer name.
    				continue;
    			}
    
    			// If here then a layer was found so match the selected features with time series in the list.
    
    			Message.printStatus ( 2, routine, "Matching layer \"" + layer_name +
    			"\" features with time series using attribute \"" + layer_location + "\"." );
    
    			try {
    			    layer_location_field_int = georec.getLayer().getAttributeTable().getFieldIndex( layer_location );
    				Message.printStatus ( 2, routine, "Attribute \"" + layer_location +
    				"\" is field [" + layer_location_field_int+"]");
    			}
    			catch ( Exception e ) {
    				Message.printWarning ( 1, routine, "Layer attribute column \"" + layer_location +
    				    "\" is not found.  Cannot link time series and map." );
    				Message.printWarning ( 3, routine, e );
    				JGUIUtil.setWaitCursor ( this, false);
    				return;
    			}
    			if ( Layer_DataSourceCol_int >= 0 ) {
    				try {
    				    layer_datasource = (String)rec.getFieldValue(Layer_DataSourceCol_int);
    					try {
    					    layer_datasource_field_int =
    						georec.getLayer().getAttributeTable().getFieldIndex(layer_datasource);
    						Message.printStatus ( 2,routine, "Attribute \"" + layer_datasource +
    						"\" is field [" + layer_datasource_field_int+"]");
    					}
    					catch ( Exception e ) {
    						Message.printWarning ( 3, routine, "Data source attribute column \"" +
    						layer_datasource + "\" is not found.  Ignoring data source." );
    						Message.printWarning ( 3, routine, e );
    						JGUIUtil.setWaitCursor ( this, false);
    						layer_datasource = null;
    					}
    				}
    				catch ( Exception e ) {
    					Message.printWarning ( 3, routine, "Unable to determine data data source from layer " +
    					"attribute table.  Ignoring data source." );
    					// Ignore for now.
    					layer_datasource = null;
    				}
    			}
    
    			// TODO SAM 2006-03-02 Add attributes other than strings
    			// For now only deal with string attributes - later need to handle other types.
    
    			// Get the attribute to be checked for the ID,
    			// based on the attribute name in the lookup file...
    
    			// TODO SAM 2006-03-02 Optimize code
    			// Need to implement DbaseDataTable.getTableRecord to
    			// handle on the fly reading to make this a little more directly...
    
    			try {
    			    georec_location = (String)georec.getLayer().getAttributeTable().getFieldValue(
    				georec.getShape().index, layer_location_field_int );
    				Message.printStatus ( 2, routine, "Trying to find time series with location ID \""
    				+ georec_location + "\"..." );
    			}
    			catch ( Exception e ) {
    				Message.printWarning ( 3, routine,
    				"Cannot get value for \"" + layer_location + "\" attribute.  Skipping record." );
    				// Ignore for now.
    				Message.printWarning ( 3, routine, e );
    				continue;
    			}
    			// Get the attribute to be checked for the data source,
    			// based on the attribute name in the lookup file...
    			if ( (Layer_DataSourceCol_int >= 0) && (layer_datasource != null) ) {
        			try {
        			    georec_datasource = (String)georec.getLayer().getAttributeTable().getFieldValue(
        				georec.getShape().index, layer_datasource_field_int );
        				Message.printStatus ( 2, routine, "Trying to find time series with data source \""
        				+ georec_datasource + "\"..." );
        			}
        			catch ( Exception e ) {
        				Message.printWarning ( 3, routine, "Cannot get value for \"" + layer_datasource +
        				"\" attribute.  Skipping record." );
        				// Ignore for now.
        				Message.printWarning ( 3, routine, e );
        				continue;
        			}
    			}
    			// Now use the TS fields in the lookup table to match time series that are listed in the query
    			// results.  This is done brute force by searching everything in the table model...
    			for ( int its = 0; its < nrows; its++ ) {
    				// Get the attributes from the time series query list table model.
    				// TODO SAM 2006-03-02 Refactor/optimize
    				// Probably what is needed here is a generic interface on time series table models to
    				// have methods that return TSIdent or TSID parts.  For now do it brute force.
    				if ( __query_TableModel instanceof TSTool_TS_TableModel ) {
    					generic_tm = (TSTool_TS_TableModel)__query_TableModel;
    					tslist_id = (String)generic_tm.getValueAt(its, generic_tm.COL_ID );
    					tslist_datasource = (String)generic_tm.getValueAt(its,generic_tm.COL_DATA_SOURCE);
    					tslist_datatype = (String)generic_tm.getValueAt(its, generic_tm.COL_DATA_TYPE );
    					tslist_interval = (String)generic_tm.getValueAt(its, generic_tm.COL_TIME_STEP );
    				}
    				else if(__query_TableModel instanceof TSTool_HydroBase_StationGeolocMeasType_TableModel ) {
    				    TSTool_HydroBase_StationGeolocMeasType_TableModel model =
    				        (TSTool_HydroBase_StationGeolocMeasType_TableModel)__query_TableModel;
    					tslist_id = (String)model.getValueAt(its, model.COL_ID );
    					tslist_datasource =(String)model.getValueAt(its,model.COL_DATA_SOURCE);
    					tslist_datatype = (String)model.getValueAt(its,model.COL_DATA_TYPE);
    					tslist_interval = (String)model.getValueAt(its,model.COL_TIME_STEP);
    				}
                    else if(__query_TableModel instanceof TSTool_HydroBase_StructureGeolocStructMeasType_TableModel ) {
                        TSTool_HydroBase_StructureGeolocStructMeasType_TableModel model =
                            (TSTool_HydroBase_StructureGeolocStructMeasType_TableModel)__query_TableModel;
                        tslist_id = (String)model.getValueAt(its, model.COL_ID );
                        tslist_datasource =(String)model.getValueAt(its,model.COL_DATA_SOURCE);
                        tslist_datatype = (String)model.getValueAt(its,model.COL_DATA_TYPE);
                        tslist_interval = (String)model.getValueAt(its,model.COL_TIME_STEP);
                    }
                    else if(__query_TableModel instanceof TSTool_HydroBase_GroundWaterWellsView_TableModel ) {
                        TSTool_HydroBase_GroundWaterWellsView_TableModel model =
                            (TSTool_HydroBase_GroundWaterWellsView_TableModel)__query_TableModel;
                        tslist_id = (String)model.getValueAt(its, model.COL_ID );
                        tslist_datasource =(String)model.getValueAt(its,model.COL_DATA_SOURCE);
                        tslist_datatype = (String)model.getValueAt(its,model.COL_DATA_TYPE);
                        tslist_interval = (String)model.getValueAt(its,model.COL_TIME_STEP);
                    }
    				// Check the attributes against that in the map.  Use some of the look interval
    				// information for data type and interval, unless that is supplied in a layer attribute.
    				// TODO SAM 2006-03-02 Optimize/refactor
    				// Currently check the ID and get the data type and interval from the lookup file.  Later
    				// can get the interval from the layer attribute data.
    				if ( !georec_location.equalsIgnoreCase(tslist_id) ) {
    					// The ID in the selected feature does not match the time series ID in the list...
    					continue;
    				}
    				if ( (georec_datasource != null) && !georec_datasource.equalsIgnoreCase(tslist_datasource) ) {
    					// The data source in the selected feature does not match the timeseries ID in the list...
    					continue;
    				}
    				if ( !ts_datatype.equalsIgnoreCase(tslist_datatype) ) {
    					// The data type in lookup file for the selected layer does not match the
    					// time series data type in the list...
    					continue;
    				}
    				if ( !ts_interval.equalsIgnoreCase(tslist_interval) ) {
    					// The data interval in lookup file for the selected layer does not match the
    					// time series data interval in the list...
    					continue;
    				}
    				// The checked attributes match so select the time series and increment the count (do not
    				// deselect first)...
    				Message.printStatus ( 2, routine, "Selecting time seris [" + its + "]" );
    				__query_JWorksheet.selectRow ( its, false );
    				// TODO SAM 2006-03-02 Evaluate usability - should the worksheet automatically scroll to
    				// the last select?
    				++match_count;
    			}
    		}
    	}
    	if ( match_count != ngeo ) {
    		Message.printWarning ( 1, routine, "The number of matched time series (" + match_count +
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
		Message.printWarning ( 1, routine, "Unable to link map and TSTool.  See log file." );
		Message.printWarning ( 3, routine, e );
		JGUIUtil.setWaitCursor ( this, false );
	}
}

public void geoViewSelect(GRPoint devlimits, GRPoint datalimits, List selected, boolean append) {
	geoViewSelect((GRShape)devlimits, (GRShape)datalimits, selected, append);
}

public void geoViewSelect(GRLimits devlimits, GRLimits datalimits, List selected, boolean append) {
	geoViewSelect((GRShape)devlimits, (GRShape)datalimits, selected, append);
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
where "command" is the command number (1+) and "count" is an optional count of warnings for the command.
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
Insert a command in the command list, depending on the state of the UI (e.g., insert before highlighted
commands).  This method is called by command editors that are also stand-alone tools, when transferring
commands to the main command processor.
@param command single command to insert
*/
public void insertCommand ( Command command )
{   // Chain to the private method
    commandList_InsertCommandBasedOnUI ( command );
}

/**
Insert multiple commands in the command list, depending on the state of the UI (e.g., insert before highlighted
commands).  This method is called by command editors that are also stand-alone tools, when transferring
commands to the main command processor.
@param command single command to insert
*/
/* FIXME SAM 2009-06-12 Enable when needed
public void insertCommands ( List<Command> commands )
{
    // Chain to the private method
    commandList_InsertCommandsBasedOnUI ( commands );
}
*/

/**
Required by ListDataListener - receive notification when the contents of the
commands list have changed due to commands being added.
*/
public void intervalAdded ( ListDataEvent e )
{
	// The contents of the command list changed so check the GUI state...
	ui_UpdateStatus ( true );	// true = also call checkGUIState();
}

/**
Required by ListDataListener - receive notification when the contents of the
commands list have changed due to commands being removed.
*/
public void intervalRemoved ( ListDataEvent e )
{
	// The contents of the command list changed so check the GUI state...
	ui_UpdateStatus ( true );	// true = also call checkGUIState();
}

/**
Respond to ItemEvents.  If in the final list, the behavior is similar to Microsoft tools:
<ol>
<li>	Click selects on item.</li>
<li>	Shift-click selects forward or backward to nearest selection.</li>
<li>	Control-click toggles one item.</li>
</ol>
*/
public void itemStateChanged ( ItemEvent evt )
{	String routine="TSTool_JFrame.itemStateChanged";

	if ( !__guiInitialized ) {
		// Some objects are probably still null so ignore the event...
		return;
	}

	if ( ui_GetIgnoreItemEvent() ) {
		// A programatic change to a list is occurring and we want to ignore the event that will result...
		return;
	}

	Object o = evt.getItemSelectable();
	String selectedInputType = ui_GetSelectedInputType();

	// If any of the choices are changed, clear the main list so that the choices and list are in agreement...

	try {
    	// List in the order of the GUI...
    
        if ( (o == __dataStore_JComboBox) && (evt.getStateChange() == ItemEvent.SELECTED) ) {
            // New data store selected...
            uiAction_DataStoreChoiceClicked();
        }
        else if ( (o == __input_type_JComboBox) && (evt.getStateChange() == ItemEvent.SELECTED) ) {
    		// New input type selected...
    		queryResultsList_Clear();
    		uiAction_InputTypeChoiceClicked(null); // null indicates data store selection is not driving action
    	}
    	else if((o == __inputName_JComboBox) && (evt.getStateChange() == ItemEvent.SELECTED) ) {
    		// New input name selected...
    		if ( selectedInputType.equals( __INPUT_TYPE_NWSRFS_FS5Files) &&
    			!__inputName_JComboBox.getSelected().equals( __PLEASE_SELECT) ) {
    			// If the default "Please Select" is shown, the it is initialization - don't force a selection...
    			try {
    			    uiAction_SelectInputName_NWSRFS_FS5Files ( false );
    			}
    			catch ( Exception e ) {
    				Message.printWarning ( 1, routine, "Error opening NWSRFS FS5Files connection." );
    				Message.printWarning ( 2, routine, e );
    			}
    		}
            else if(selectedInputType.equals(__INPUT_TYPE_HECDSS )) {
                uiAction_SelectInputName_HECDSS ( false );
            }
    		else if ( selectedInputType.equals(__INPUT_TYPE_StateCU ) ){
    			uiAction_SelectInputType_StateCU ( false );
    		}
            else if(selectedInputType.equals(__INPUT_TYPE_StateCUB )) {
                uiAction_SelectInputType_StateCUB ( false );
            }
    		else if(selectedInputType.equals(__INPUT_TYPE_StateModB )) {
    			uiAction_SelectInputType_StateModB ( false );
    		}
    	}
    	else if ( o == __dataType_JComboBox && (evt.getStateChange() == ItemEvent.SELECTED) ) {
    		queryResultsList_Clear();
    		uiAction_DataTypeChoiceClicked();
    	}
    	else if ( o == __timeStep_JComboBox && (evt.getStateChange() == ItemEvent.SELECTED) ) {
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
                    licenseManager.getLicenseExpires() + ".  TSTool features will be disabled.  Contact " +
                    __RTiSupportEmail + " to obtain a new license.");
            // Refresh the menus - everything will be disabled but Help, which will allow the license/configuration
            // to be updated.
            ui_CheckGUIState();
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
                    licenseManager.getLicenseExpires() + ".  TSTool features will be disabled.  Contact " +
                    __RTiSupportEmail + " to renew the license.");
            // Refresh the menus - everything will be disabled but Help, which will allow the license/configuration
            // to be updated.
            ui_CheckGUIState();
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

    Message.printStatus ( 1, routine, "Checking license" );
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
        Message.printWarning ( 3, routine, e );
        license_SetLicenseManager ( null );
    }
}

/**
Indicate if the license is expired.
@param licenseManager the license manager for the session.
*/
private boolean license_IsLicenseExpired( LicenseManager licenseManager )
{
    return licenseManager.isLicenseExpired();
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
Merge a new configuration file with the old configuration file.  It is assumed that the current configuration file
is the most complete, likely being distributed with a demo or new version of the software.  Therefore the merge will
accomplish the following cases:
<ol>
<li>    If the user is merging with a complete old configuration file, the old license and settings will be
    applied to the new software.  New settings will still be in effect.</li>
<li>    If the user is applying a new license, for example to a demo installation, then only the license
    properties will be applied.<li>
</ol>
@param currentConfigFile Current configuration file that was distributed with the current software.
@param configFileToMerge The configuration file to merge with the current configuration file.
@exception IOException if there is an error writing the file.
*/
private void license_MergeConfigFiles ( File currentConfigFile, File configFileToMerge )
throws IOException
{   String routine = "TSTool_JFrame.license_MergeConfigFiles";
    int updatedPropCount = 0;
    // Read the current configuration...
    PropList currentProps = new PropList ( "current" );
    currentProps.setPersistentName ( currentConfigFile.getPath() );
    try {
        // Read and save literals as properties so that comments can be output similar to the original file.
        currentProps.readPersistent( true, true );
    }
    catch ( Exception e ) {
        Message.printWarning ( 1, routine,
            "Error reading current configuration file - cannot merge configuration properties." );
        return;
    }
    // Read the configuration to merge
    PropList configToMergeProps = new PropList ( "current" );
    configToMergeProps.setPersistentName ( configFileToMerge.getPath() );
    try {
        configToMergeProps.readPersistent();
    }
    catch ( Exception e ) {
        Message.printWarning ( 1, routine,
            "Error reading configuration file to merge - cannot merge configuration properties." );
        return;
    }
    // Loop through he properties in the current file and reset to those in the file to merge
    List props = currentProps.getList();
    int size = props.size();
    for ( int i = 0; i < size; i++ ) {
        // Get out of the current list
        Prop prop = (Prop)props.get(i);
        String currentPropName = prop.getKey();
        String currentPropValue = prop.getValue();
        // See if there is a match in the properties to be merged
        String matchPropValue = configToMergeProps.getValue(currentPropName);
        if ( (matchPropValue != null) && !currentPropValue.equals(matchPropValue) ) {
            // Property names match and values are different so reset in the current properties
            prop.setValue ( matchPropValue );
            ++updatedPropCount;
        }
    }
    if ( updatedPropCount > 0 ) {
        // Had at least one match so rewrite the property file to the same name.
        currentProps.writePersistent();
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
	String selectedInputType = ui_GetSelectedInputType();
    // Popup for commands...
	if ( (c == ui_GetCommandJList()) && (__commands_JListModel.size() > 0) &&
		((mods & MouseEvent.BUTTON3_MASK) != 0) ) {
		Point pt = JGUIUtil.computeOptimalPosition ( event.getPoint(), c, __Commands_JPopupMenu );
		__Commands_JPopupMenu.show ( c, pt.x, pt.y );
	}
    // Popup for time series results list...
	else if ( (c == __resultsTS_JList) && (__resultsTS_JListModel.size() > 0) &&
		((mods & MouseEvent.BUTTON3_MASK) != 0) ) {
		Point pt = JGUIUtil.computeOptimalPosition (event.getPoint(), c, __resultsTS_JPopupMenu );
		__resultsTS_JPopupMenu.show ( c, pt.x, pt.y );
	}
    // Popup for ensemble results list...
    else if ( (c == __resultsTSEnsembles_JList) && (__resultsTSEnsembles_JListModel.size() > 0) &&
        ((mods & MouseEvent.BUTTON3_MASK) != 0) ) {
        Point pt = JGUIUtil.computeOptimalPosition (event.getPoint(), c, __resultsTSEnsembles_JPopupMenu );
        __resultsTSEnsembles_JPopupMenu.show ( c, pt.x, pt.y );
    }
	// Popup for input name...
    else if ( (c == __inputName_JComboBox) && ((mods & MouseEvent.BUTTON3_MASK) != 0) &&
        selectedInputType.equals(__INPUT_TYPE_HECDSS) ) {
        Point pt = JGUIUtil.computeOptimalPosition (event.getPoint(), c, __input_name_JPopupMenu );
        __input_name_JPopupMenu.removeAll();
        __input_name_JPopupMenu.add( new SimpleJMenuItem (__InputName_BrowseHECDSS_String, this ) );
        __input_name_JPopupMenu.show ( c, pt.x, pt.y );
    }
    else if ( (c == __inputName_JComboBox) && ((mods & MouseEvent.BUTTON3_MASK) != 0) &&
        selectedInputType.equals(__INPUT_TYPE_StateCUB) ) {
        Point pt = JGUIUtil.computeOptimalPosition (event.getPoint(), c, __input_name_JPopupMenu );
        __input_name_JPopupMenu.removeAll();
        __input_name_JPopupMenu.add( new SimpleJMenuItem (__InputName_BrowseStateCUB_String, this ) );
        __input_name_JPopupMenu.show ( c, pt.x, pt.y );
    }
	else if ( (c == __inputName_JComboBox) && ((mods & MouseEvent.BUTTON3_MASK) != 0) &&
		selectedInputType.equals(__INPUT_TYPE_StateModB) ) {
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
Display the time series list query results in a string format.  These results are APPENDED to the list
of strings already found in the __commands_JList.  
@param location Location part of TSIdent.
@param source Source part of TSIdent.
@param type Data type part of TSIdent.
@param interval Interval part of TSIdent.
@param scenario Scenario part of TSIdent (null or empty for no scenario).
@param sequence_number Sequence number part of TSIdent (null or empty for no sequence number).
@param input_type The type of input (e.g., DateValue, NWSCard, USGSNWIS).
@param input_name The input location for the data (a database connection or file name).
@param comment Comment for time series (null or empty for no comment).
@param use_alias If true, then the location contains the alias and the other
TSID fields are ignored when putting together the TSID command.
@param insertOffset the position to offset the insert (used to ensure that inserting multiple
time series results in the same order in the commands, rather than first in last out appearance
that could occur when inserting after a selected command).
@return the number of commands inserted (may be 2 if a comment is inserted).  This is only used when
commands are selected and prevents the insert from occurring in reverse order.
*/
private int queryResultsList_AppendTSIDToCommandList ( String location,
					String source,
					String type,
					String interval,
					String scenario,
					String sequence_number,
					String input_type,
					String input_name,
					String comment,
					boolean use_alias,
					int insertOffset )
{	// Add after the last selected item or at the end if nothing is selected.
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
	    // If this is part of a block of inserts, the insertOffset is also added to ensure that the
	    // insert occurs at the end of the block (incremental).  Otherwise the insert is first in last
	    // out and reversed the order of the block.
		insert_pos = selected_indices[selected_size - 1] + 1 + insertOffset;
	}
	else {
	    // Insert at the end...
		insert_pos = __commands_JListModel.size();
	}
	
	int i = 0;	// Leave this in case we add looping.
	int offset = 0;	// Handles cases where a comment is inserted prior to the command.
	int numberInserted = 0;
	if ( (comment != null) && !comment.equals("") ) {
	    // Insert a comment prior to the command, for example to include the station description.
	    Command commentCommand = commandList_NewCommand ( "# " + comment, true );
	    // Run the comment to set the status from unknown to success
        try {
            commentCommand.checkCommandParameters(null, "", 3);
        }
        catch ( Exception e ) {
            // Should not happen.
        }
		__commands_JListModel.insertElementAt ( commentCommand, (insert_pos + i*2));
		++numberInserted;
		offset = 1;
	}
	String tsident_string = null;
	if ( use_alias ) {
		// Just use the short alias...
		tsident_string = location + input;
	}
	else {
	    // Use a full time series identifier...
		tsident_string = location + "." + source + "." + type + "." +
			interval + scenario2 + sequence_number2 + input;
	}
	if ( Message.isDebugOn ) {
		Message.printDebug ( 1, "", "Inserting \"" + tsident_string +
		"\" at " + (insert_pos + i*2 + offset) );
	}
	//__commands_JListModel.insertElementAt ( tsident_string, (insert_pos + i*2 + offset));
	Command tsid_command = commandList_NewCommand ( tsident_string, true );
	__commands_JListModel.insertElementAt ( tsid_command, (insert_pos + i*2 + offset));
    ++numberInserted;
	//commandList_SetDirty ( true );
	//ui_UpdateStatus ( false );
    if ( tsid_command instanceof CommandDiscoverable ) {
        // Run discovery on the command to do initial validation and provide data to other commands
        commandList_EditCommand_RunDiscovery ( tsid_command );
        // TODO SAM 2011-03-21 Should following commands run discovery - could be slow
    }
    ui_ShowCurrentCommandListStatus();
    commandList_SetDirty ( true );
    return numberInserted;
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
       	try {
       	    __query_JWorksheet.clear();       
		}
		catch ( Exception e ) {
			// Absorb the exception in most cases - print if
			// developing to see if this issue can be resolved.
			if ( Message.isDebugOn && IOUtil.testing()  ) {
				String routine = "TSTool_JFrame.clearQueryList";
				Message.printWarning ( 3, routine,
				"For developers:  caught exception in clearQueryList JWorksheet at setup." );
				Message.printWarning ( 3, routine, e );
			}
		}
	}

	// Set the data lists to empty and redraw the label (check for null
	// because it seems to be an issue at startup)...

	ui_UpdateStatus ( false );
}

/**
Select a station from the query list and create a time series identifier in the command list.
@param row Row that is selected in the query list.  This method can be
called once for a single event or multiple times from transferAll().
@param update_status If true, then the GUI state is checked after the
transfer.  This should only be set to true when transferring one command.  If
many are transferred, this slows down the GUI.
@param insertOffset used when processing multiple time series - ensures that inserts after a selected
command occur in order and not first in last out.
@return the number of commands that are added (1 or maybe 2 if a comment is inserted before the command).
*/
private int queryResultsList_TransferOneTSFromQueryResultsListToCommandList (
    int row, boolean update_status, int insertOffset ) 
{	boolean use_alias = false;
    int numCommandsAdded = 0; // Used when inserting blocks of time series
    String selectedInputType = ui_GetSelectedInputType();
    DataStore selectedDataStore = ui_GetSelectedDataStore(); 
    if ( (selectedDataStore != null) && (selectedDataStore instanceof ColoradoWaterHBGuestDataStore) ) {
        if ( __query_TableModel instanceof TSTool_HydroBase_WellLevel_Day_TableModel ) {
            TSTool_HydroBase_WellLevel_Day_TableModel model = (TSTool_HydroBase_WellLevel_Day_TableModel)__query_TableModel;
            numCommandsAdded = queryResultsList_AppendTSIDToCommandList ( 
                (String)__query_TableModel.getValueAt( row, model.COL_ID ),
                (String)__query_TableModel.getValueAt ( row, model.COL_DATA_SOURCE),
                (String)__query_TableModel.getValueAt ( row, model.COL_DATA_TYPE),
                (String)__query_TableModel.getValueAt ( row, model.COL_TIME_STEP),
                "", // No scenario
                null, // No sequence number
                (String)__query_TableModel.getValueAt( row, model.COL_INPUT_TYPE),
                "", // No input name
                (String)__query_TableModel.getValueAt( row, model.COL_ID) + " - " +
                (String)__query_TableModel.getValueAt ( row, model.COL_NAME),
                false, insertOffset );
        }
        else if ( __query_TableModel instanceof TSTool_HydroBase_StationGeolocMeasType_TableModel ) {
            TSTool_HydroBase_StationGeolocMeasType_TableModel model =
                (TSTool_HydroBase_StationGeolocMeasType_TableModel)__query_TableModel;
            numCommandsAdded = queryResultsList_AppendTSIDToCommandList ( 
                (String)__query_TableModel.getValueAt( row, model.COL_ID ),
                (String)__query_TableModel.getValueAt ( row, model.COL_DATA_SOURCE),
                (String)__query_TableModel.getValueAt ( row, model.COL_DATA_TYPE),
                (String)__query_TableModel.getValueAt ( row, model.COL_TIME_STEP),
                "", // No scenario
                null, // No sequence number
                (String)__query_TableModel.getValueAt( row, model.COL_INPUT_TYPE),
                "", // No input name
                (String)__query_TableModel.getValueAt( row, model.COL_ID) + " - " +
                (String)__query_TableModel.getValueAt ( row, model.COL_NAME),
                false, insertOffset );
        }
        else if ( __query_TableModel instanceof TSTool_HydroBase_StructureGeolocStructMeasType_TableModel ) {
            TSTool_HydroBase_StructureGeolocStructMeasType_TableModel model =
                (TSTool_HydroBase_StructureGeolocStructMeasType_TableModel)__query_TableModel;
            numCommandsAdded = queryResultsList_AppendTSIDToCommandList ( 
                (String)__query_TableModel.getValueAt( row, model.COL_ID ),
                (String)__query_TableModel.getValueAt ( row, model.COL_DATA_SOURCE),
                (String)__query_TableModel.getValueAt ( row, model.COL_DATA_TYPE),
                (String)__query_TableModel.getValueAt ( row, model.COL_TIME_STEP),
                "", // No scenario
                null, // No sequence number
                (String)__query_TableModel.getValueAt( row, model.COL_INPUT_TYPE),
                "", // No input name
                (String)__query_TableModel.getValueAt( row, model.COL_ID) + " - " +
                (String)__query_TableModel.getValueAt ( row, model.COL_NAME),
                false, insertOffset );
        }
        else if ( __query_TableModel instanceof TSTool_HydroBase_GroundWaterWellsView_TableModel ) {
            TSTool_HydroBase_GroundWaterWellsView_TableModel model =
                (TSTool_HydroBase_GroundWaterWellsView_TableModel)__query_TableModel;
            numCommandsAdded = queryResultsList_AppendTSIDToCommandList ( 
                (String)__query_TableModel.getValueAt( row, model.COL_ID ),
                (String)__query_TableModel.getValueAt ( row, model.COL_DATA_SOURCE),
                (String)__query_TableModel.getValueAt ( row, model.COL_DATA_TYPE),
                (String)__query_TableModel.getValueAt ( row, model.COL_TIME_STEP),
                "", // No scenario
                null, // No sequence number
                (String)__query_TableModel.getValueAt( row, model.COL_INPUT_TYPE),
                "", // No input name
                (String)__query_TableModel.getValueAt( row, model.COL_ID) + " - " +
                (String)__query_TableModel.getValueAt ( row, model.COL_NAME),
                false, insertOffset );
        }
    }
    else if ( (selectedDataStore != null) && (selectedDataStore instanceof ColoradoWaterSMSDataStore) ) {
        // The location (id), type, and time step uniquely
        // identify the time series, but the input_name is needed to indicate the database.
        TSTool_HydroBase_StationGeolocMeasType_TableModel model =
            (TSTool_HydroBase_StationGeolocMeasType_TableModel)__query_TableModel;
        numCommandsAdded = queryResultsList_AppendTSIDToCommandList ( 
            (String)__query_TableModel.getValueAt( row, model.COL_ABBREV ),
            (String)__query_TableModel.getValueAt ( row, model.COL_DATA_SOURCE),
            (String)__query_TableModel.getValueAt ( row, model.COL_DATA_TYPE),
            (String)__query_TableModel.getValueAt ( row, model.COL_TIME_STEP),
            "", // No scenario
            null, // No sequence number
            (String)__query_TableModel.getValueAt( row, model.COL_INPUT_TYPE),
            "", // No input name
            (String)__query_TableModel.getValueAt( row, model.COL_ABBREV) + " - " +
            (String)__query_TableModel.getValueAt ( row, model.COL_NAME),
            false, insertOffset );
    }
    else if ( selectedInputType.equals(__INPUT_TYPE_DateValue) ) {
		// The location (id), type, and time step uniquely identify the
		// time series, but the input_name is needed to find the data.
		// If an alias is specified, assume that it should be used instead of the normal TSID.
		TSTool_TS_TableModel model = (TSTool_TS_TableModel)__query_TableModel;
		String alias = ((String)model.getValueAt ( row, model.COL_ALIAS )).trim();
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
		        numCommandsAdded = queryResultsList_AppendTSIDToCommandList ( alias,
    			null,
    			null,
    			null,
    			null,
    			null,	// No sequence number
    			(String)__query_TableModel.getValueAt( row, model.COL_INPUT_TYPE),
    			(String)__query_TableModel.getValueAt( row, model.COL_INPUT_NAME), "", use_alias, insertOffset );
			}
			catch ( Exception e ) {
				Message.printWarning ( 3, "", e );
			}
		}
		else {
		    // Use full ID and all other fields...
			Message.printStatus ( 1, "", "Calling append (no alias)..." );
			numCommandsAdded = queryResultsList_AppendTSIDToCommandList ( 
			(String)__query_TableModel.getValueAt ( row, model.COL_ID ),
			(String)__query_TableModel.getValueAt ( row, model.COL_DATA_SOURCE),
			(String)__query_TableModel.getValueAt ( row, model.COL_DATA_TYPE),
			(String)__query_TableModel.getValueAt ( row, model.COL_TIME_STEP),
			(String)__query_TableModel.getValueAt ( row, model.COL_SCENARIO),
			(String)__query_TableModel.getValueAt ( row, model.COL_SEQUENCE),
			(String)__query_TableModel.getValueAt ( row, model.COL_INPUT_TYPE),
			(String)__query_TableModel.getValueAt ( row, model.COL_INPUT_NAME), "",
			use_alias, insertOffset );
		}
	}
	else if (  selectedInputType.equals ( __INPUT_TYPE_HECDSS ) ) {
	    // TODO SAM 2009-01-13 Evaluate whether to remove some columns
	    // Currently essentially the same as the generic model but have custom headings
        // The location (id), type, and time step uniquely identify the time series...
        TSTool_HecDss_TableModel model = (TSTool_HecDss_TableModel)__query_TableModel;
        String seqnum = (String)__query_TableModel.getValueAt( row, model.COL_SEQUENCE );
        if ( seqnum.length() == 0 ) {
            seqnum = null;
        }
        numCommandsAdded = queryResultsList_AppendTSIDToCommandList (
        (String)__query_TableModel.getValueAt( row, model.COL_ID ),
        (String)__query_TableModel.getValueAt( row, model.COL_DATA_SOURCE),
        (String)__query_TableModel.getValueAt( row, model.COL_DATA_TYPE),
        (String)__query_TableModel.getValueAt( row, model.COL_TIME_STEP ),
        (String)__query_TableModel.getValueAt( row, model.COL_SCENARIO ), seqnum, // Optional sequence number
        (String)__query_TableModel.getValueAt( row, model.COL_INPUT_TYPE),
        (String)__query_TableModel.getValueAt( row, model.COL_INPUT_NAME), "", false, insertOffset );
    }
	else if ( selectedInputType.equals ( __INPUT_TYPE_HydroBase )) {
		if ( __query_TableModel instanceof TSTool_HydroBase_StationGeolocMeasType_TableModel){
		    // Station time series
		    TSTool_HydroBase_StationGeolocMeasType_TableModel model =
		        (TSTool_HydroBase_StationGeolocMeasType_TableModel)__query_TableModel;
			numCommandsAdded = queryResultsList_AppendTSIDToCommandList ( 
				(String)__query_TableModel.getValueAt( row, model.COL_ID ),
				(String)__query_TableModel.getValueAt ( row, model.COL_DATA_SOURCE),
				(String)__query_TableModel.getValueAt ( row, model.COL_DATA_TYPE),
				(String)__query_TableModel.getValueAt ( row, model.COL_TIME_STEP),
				"",	// No scenario
				null,	// No sequence number
				(String)__query_TableModel.getValueAt( row, model.COL_INPUT_TYPE),
				"", // No input name
				(String)__query_TableModel.getValueAt( row, model.COL_ID) + " - " +
				(String)__query_TableModel.getValueAt (	row, model.COL_NAME),
				false, insertOffset );
		}
		else if ( __query_TableModel instanceof TSTool_HydroBase_StructureGeolocStructMeasType_TableModel){
	        // Structure time series
		    TSTool_HydroBase_StructureGeolocStructMeasType_TableModel model =
	            (TSTool_HydroBase_StructureGeolocStructMeasType_TableModel)__query_TableModel;
            numCommandsAdded = queryResultsList_AppendTSIDToCommandList ( 
                (String)__query_TableModel.getValueAt( row, model.COL_ID ),
                (String)__query_TableModel.getValueAt ( row, model.COL_DATA_SOURCE),
                (String)__query_TableModel.getValueAt ( row, model.COL_DATA_TYPE),
                (String)__query_TableModel.getValueAt ( row, model.COL_TIME_STEP),
                "", // No scenario
                null,   // No sequence number
                (String)__query_TableModel.getValueAt( row, model.COL_INPUT_TYPE),
                "", // No input name
                (String)__query_TableModel.getValueAt( row, model.COL_ID) + " - " +
                (String)__query_TableModel.getValueAt ( row, model.COL_NAME),
                false, insertOffset );
        }
        else if ( __query_TableModel instanceof TSTool_HydroBase_GroundWaterWellsView_TableModel){
            // Structure time series
            TSTool_HydroBase_GroundWaterWellsView_TableModel model =
                (TSTool_HydroBase_GroundWaterWellsView_TableModel)__query_TableModel;
            numCommandsAdded = queryResultsList_AppendTSIDToCommandList ( 
                (String)__query_TableModel.getValueAt( row, model.COL_ID ),
                (String)__query_TableModel.getValueAt ( row, model.COL_DATA_SOURCE),
                (String)__query_TableModel.getValueAt ( row, model.COL_DATA_TYPE),
                (String)__query_TableModel.getValueAt ( row, model.COL_TIME_STEP),
                "", // No scenario
                null,   // No sequence number
                (String)__query_TableModel.getValueAt( row, model.COL_INPUT_TYPE),
                "", // No input name
                (String)__query_TableModel.getValueAt( row, model.COL_ID) + " - " +
                (String)__query_TableModel.getValueAt ( row, model.COL_NAME),
                false, insertOffset );
        }
		else if (__query_TableModel instanceof TSTool_HydroBase_WellLevel_Day_TableModel) {
			TSTool_HydroBase_WellLevel_Day_TableModel model =
			    (TSTool_HydroBase_WellLevel_Day_TableModel)__query_TableModel;
			numCommandsAdded = queryResultsList_AppendTSIDToCommandList(
				(String)__query_TableModel.getValueAt( row, model.COL_ID ),
				(String)__query_TableModel.getValueAt (	row, model.COL_DATA_SOURCE),
				(String)__query_TableModel.getValueAt (	row, model.COL_DATA_TYPE),
				(String)__query_TableModel.getValueAt (	row, model.COL_TIME_STEP),
				"",	// No scenario
				null,	// No sequence number
				(String)__query_TableModel.getValueAt( row, model.COL_INPUT_TYPE),
				"", // No input name
				(String)__query_TableModel.getValueAt( row, model.COL_ID) + " - " +
				(String)__query_TableModel.getValueAt ( row, model.COL_NAME),
				false, insertOffset );			
		}
		else if ( __query_TableModel instanceof	TSTool_HydroBase_Ag_TableModel){
			TSTool_HydroBase_Ag_TableModel model = (TSTool_HydroBase_Ag_TableModel)__query_TableModel;
			numCommandsAdded = queryResultsList_AppendTSIDToCommandList ( 
				(String)__query_TableModel.getValueAt ( row, model.COL_ID ),
				(String)__query_TableModel.getValueAt ( row, model.COL_DATA_SOURCE),
				(String)__query_TableModel.getValueAt ( row, model.COL_DATA_TYPE),
				(String)__query_TableModel.getValueAt (	row, model.COL_TIME_STEP),
				"",	// No scenario
				null,	// No sequence number
				(String)__query_TableModel.getValueAt( row, model.COL_INPUT_TYPE),
				"", // No input name
				"", // No comment
				false, insertOffset );
		}
		else if ( __query_TableModel instanceof	TSTool_HydroBase_AgGIS_TableModel){
			TSTool_HydroBase_AgGIS_TableModel model = (TSTool_HydroBase_AgGIS_TableModel)__query_TableModel;
			numCommandsAdded = queryResultsList_AppendTSIDToCommandList ( 
				(String)__query_TableModel.getValueAt ( row, model.COL_ID ),
				(String)__query_TableModel.getValueAt ( row, model.COL_DATA_SOURCE),
				(String)__query_TableModel.getValueAt ( row, model.COL_DATA_TYPE),
				(String)__query_TableModel.getValueAt ( row, model.COL_TIME_STEP),
				"",	// No scenario
				null,	// No sequence number
				(String)__query_TableModel.getValueAt( row, model.COL_INPUT_TYPE),
				"", // No input name
				"", // No comment
				false, insertOffset );
		}
		else if ( __query_TableModel instanceof TSTool_HydroBase_CASSLivestockStats_TableModel){
			TSTool_HydroBase_CASSLivestockStats_TableModel model =
				(TSTool_HydroBase_CASSLivestockStats_TableModel)__query_TableModel;
			numCommandsAdded = queryResultsList_AppendTSIDToCommandList ( 
				(String)__query_TableModel.getValueAt ( row, model.COL_ID ),
				(String)__query_TableModel.getValueAt (	row, model.COL_DATA_SOURCE),
				(String)__query_TableModel.getValueAt (	row, model.COL_DATA_TYPE),
				(String)__query_TableModel.getValueAt (	row, model.COL_TIME_STEP),
				"",	// No scenario
				null,	// No sequence number
				(String)__query_TableModel.getValueAt ( row, model.COL_INPUT_TYPE),
				"", // No input name
				"", // No comment
				false, insertOffset );
		}
		else if ( __query_TableModel instanceof TSTool_HydroBase_CUPopulation_TableModel){
			TSTool_HydroBase_CUPopulation_TableModel model =
				(TSTool_HydroBase_CUPopulation_TableModel)__query_TableModel;
			numCommandsAdded = queryResultsList_AppendTSIDToCommandList ( 
				(String)__query_TableModel.getValueAt ( row, model.COL_ID ),
				(String)__query_TableModel.getValueAt (	row, model.COL_DATA_SOURCE),
				(String)__query_TableModel.getValueAt (	row, model.COL_DATA_TYPE),
				(String)__query_TableModel.getValueAt ( row, model.COL_TIME_STEP),
				"",	// No scenario
				null,	// No sequence number
				(String)__query_TableModel.getValueAt( row, model.COL_INPUT_TYPE),
				"", // No input name
				"", // No comment
				false, insertOffset );
		}
		else if ( __query_TableModel instanceof TSTool_HydroBase_WIS_TableModel){
			TSTool_HydroBase_WIS_TableModel model =	(TSTool_HydroBase_WIS_TableModel)__query_TableModel;
			numCommandsAdded = queryResultsList_AppendTSIDToCommandList ( 
				(String)__query_TableModel.getValueAt ( row, model.COL_ID ),
				(String)__query_TableModel.getValueAt ( row, model.COL_DATA_SOURCE),
				(String)__query_TableModel.getValueAt (	row, model.COL_DATA_TYPE),
				(String)__query_TableModel.getValueAt ( row, model.COL_TIME_STEP),
				(String)__query_TableModel.getValueAt ( row, model.COL_SHEET_NAME),
				null,	// No sequence number
				(String)__query_TableModel.getValueAt ( row, model.COL_INPUT_TYPE),
				"", // No input name
				"", // No comment
				false, insertOffset );
		}
	}
	else if ( selectedInputType.equals( __INPUT_TYPE_NWSRFS_ESPTraceEnsemble)) {
		// The location (id), type, and time step uniquely identify the
		// time series, but the input_name is needed to find the data.
		// If an alias is specified, assume that it should be used
		// instead of the normal TSID.
		TSTool_ESPTraceEnsemble_TableModel model = (TSTool_ESPTraceEnsemble_TableModel)__query_TableModel;
		//String alias = ((String)__query_TableModel.getValueAt ( row, model.COL_ALIAS )).trim();
		boolean use_alias_in_id = false;
		if ( use_alias_in_id ) {
			// Need to figure out what to do here...
		}
		else {
		    // Use full ID and all other fields...
		    numCommandsAdded = queryResultsList_AppendTSIDToCommandList ( 
			(String)__query_TableModel.getValueAt ( row, model.COL_ID ),
			(String)__query_TableModel.getValueAt ( row, model.COL_DATA_SOURCE),
			(String)__query_TableModel.getValueAt ( row, model.COL_DATA_TYPE),
			(String)__query_TableModel.getValueAt ( row, model.COL_TIME_STEP),
			(String)__query_TableModel.getValueAt ( row, model.COL_SCENARIO),
			(String)__query_TableModel.getValueAt ( row, model.COL_SEQUENCE),
			(String)__query_TableModel.getValueAt ( row, model.COL_INPUT_TYPE),
			(String)__query_TableModel.getValueAt ( row, model.COL_INPUT_NAME), "",
			false, insertOffset );
		}
	}
    else if ( (selectedDataStore != null) && (selectedDataStore instanceof RccAcisDataStore) ) {
        // The location (id), type, and time step uniquely
        // identify the time series, but the input_name is needed to indicate the database.
        RccAcisDataStore rccAcisDataStore = (RccAcisDataStore)selectedDataStore;
        TSTool_RccAcis_TableModel model = (TSTool_RccAcis_TableModel)__query_TableModel;
        if (model.getSortOrder() != null) {
            row = model.getSortOrder()[row];
        }
        RccAcisStationTimeSeriesMetadata ts = (RccAcisStationTimeSeriesMetadata)model.getData().get(row);
        String dataType = "" + __query_TableModel.getValueAt ( row, model.COL_DATA_TYPE_ELEM);
        if ( rccAcisDataStore.getAPIVersion() == 1 ) {
            dataType = "" + __query_TableModel.getValueAt ( row, model.COL_DATA_TYPE_MAJOR);
        }
        numCommandsAdded = queryResultsList_AppendTSIDToCommandList ( 
            ts.getIDPreferred(true),
            "ACIS", //(String)__query_TableModel.getValueAt ( row, model.COL_DATA_SOURCE),
            //(String)__query_TableModel.getValueAt ( row, model.COL_DATA_TYPE_MAJOR),
            dataType,
            (String)__query_TableModel.getValueAt ( row, model.COL_TIME_STEP),
            null, // No scenario
            null, // No sequence number
            (String)__query_TableModel.getValueAt ( row, model.COL_DATA_STORE_NAME),
            "", "",
            use_alias, insertOffset );
    }
	else if ( (selectedDataStore != null) && (selectedDataStore instanceof ReclamationHDBDataStore) ) {
        // The location (id), type, and time step uniquely
        // identify the time series, but the input_name is needed to indicate the database.
        TSTool_ReclamationHDB_TableModel model = (TSTool_ReclamationHDB_TableModel)__query_TableModel;
        String tsType = (String)__query_TableModel.getValueAt( row, model.COL_TYPE);
        String scenario = "";
        if ( tsType.equalsIgnoreCase("Model") ) {
            String modelRunDate = "" + (Date)__query_TableModel.getValueAt( row, model.COL_MODEL_RUN_DATE );
            // Trim off the hundredths of a second since that interferes with the TSID conventions.  It always
            // appears to be ".0"
            int pos = modelRunDate.indexOf(".");
            if ( pos > 0 ) {
                modelRunDate = modelRunDate.substring(0,pos);
            }
            // Replace "." with "?" in the model information so as to not conflict with TSID conventions - will
            // switch again later.
            String modelName = (String)__query_TableModel.getValueAt( row, model.COL_MODEL_NAME );
            modelName = modelName.replace('.', '?');
            String modelRunName = (String)__query_TableModel.getValueAt( row, model.COL_MODEL_RUN_NAME );
            modelRunName = modelRunName.replace('.', '?');
            String hydrologicIndicator = (String)__query_TableModel.getValueAt( row, model.COL_MODEL_HYDROLOGIC_INDICATOR );
            hydrologicIndicator = hydrologicIndicator.replace('.', '?');
            // The following should uniquely identify a model time series (in addition to other TSID parts)
            scenario = modelName + "-" + modelRunName + "-" + hydrologicIndicator + "-" + modelRunDate;
        }
        String loc = (String)__query_TableModel.getValueAt( row, model.COL_SITE_COMMON_NAME );
        String dataType = (String)__query_TableModel.getValueAt( row, model.COL_DATA_TYPE);
        numCommandsAdded = queryResultsList_AppendTSIDToCommandList (
        //(String)__query_TableModel.getValueAt( row, model.COL_SUBJECT_TYPE ) + ":" +
        tsType + ":" + loc.replace('.','?'), // Replace period because it will interfere with TSID
        (String)__query_TableModel.getValueAt( row, model.COL_DATA_SOURCE),
        dataType,
        (String)__query_TableModel.getValueAt( row, model.COL_TIME_STEP),
        scenario,
        null,   // No sequence number
        (String)__query_TableModel.getValueAt( row,model.COL_DATASTORE_NAME),
        "",
        "", false, insertOffset );
    }
	else if ( (selectedDataStore != null) && (selectedDataStore instanceof RiversideDBDataStore) ) {
		// The location (id), type, and time step uniquely
		// identify the time series, but the input_name is needed to indicate the database.
		TSTool_RiversideDB_TableModel model = (TSTool_RiversideDB_TableModel)__query_TableModel;
		// TSID data type is formed from separate database values
		String dataType = (String)__query_TableModel.getValueAt( row, model.COL_DATA_TYPE);
		String subType = (String)__query_TableModel.getValueAt( row, model.COL_SUB_TYPE);
		if ( subType.length() > 0 ) {
		    dataType = dataType + "-" + subType;
		}
		String sequenceNumber = (String)__query_TableModel.getValueAt( row, model.COL_SEQUENCE);
		numCommandsAdded = queryResultsList_AppendTSIDToCommandList (
		(String)__query_TableModel.getValueAt( row, model.COL_ID ),
		(String)__query_TableModel.getValueAt( row,	model.COL_DATA_SOURCE),
		dataType,
		(String)__query_TableModel.getValueAt( row, model.COL_TIME_STEP),
		(String)__query_TableModel.getValueAt( row, model.COL_SCENARIO),
		sequenceNumber,
		(String)__query_TableModel.getValueAt( row,model.COL_DATASTORE_NAME),
		"",
		"", false, insertOffset );
	}
    else if ( (selectedDataStore != null) && (selectedDataStore instanceof UsgsNwisDailyDataStore) ) {
        // The location (id), type, and time step uniquely
        // identify the time series, but the input_name is needed to indicate the database.
        TSTool_UsgsNwisDaily_TableModel model = (TSTool_UsgsNwisDaily_TableModel)__query_TableModel;
        if (model.getSortOrder() != null) {
            row = model.getSortOrder()[row];
        }
        UsgsNwisSiteTimeSeriesMetadata ts = (UsgsNwisSiteTimeSeriesMetadata)model.getData().get(row);
        numCommandsAdded = queryResultsList_AppendTSIDToCommandList ( 
            (String)__query_TableModel.getValueAt ( row, model.COL_ID),
            (String)__query_TableModel.getValueAt ( row, model.COL_DATA_SOURCE),
            ts.formatDataTypeForTSID(),
            (String)__query_TableModel.getValueAt ( row, model.COL_TIME_STEP),
            null, // No scenario
            null, // No sequence number
            (String)__query_TableModel.getValueAt ( row, model.COL_DATA_STORE_NAME),
            "", "",
            use_alias, insertOffset );
    }
	// The following input types use the generic TSTool_TS_TableModel with
	// no special considerations.  If the sequence number is non-blank in
	// the worksheet, it will be transferred.
	else if (
	    selectedInputType.equals(__INPUT_TYPE_DIADvisor) ||
	    selectedInputType.equals ( __INPUT_TYPE_HECDSS ) ||
		selectedInputType.equals(__INPUT_TYPE_MEXICO_CSMN) ||
		selectedInputType.equals(__INPUT_TYPE_MODSIM) ||
		selectedInputType.equals ( __INPUT_TYPE_NWSCARD ) ||
		selectedInputType.equals ( __INPUT_TYPE_NWSRFS_FS5Files ) ||
		selectedInputType.equals ( __INPUT_TYPE_RiverWare ) ||
		selectedInputType.equals ( __INPUT_TYPE_StateCU ) ||
		selectedInputType.equals ( __INPUT_TYPE_StateCUB ) ||
		selectedInputType.equals ( __INPUT_TYPE_StateMod ) ||
		selectedInputType.equals ( __INPUT_TYPE_StateModB ) ||
		selectedInputType.equals(__INPUT_TYPE_UsgsNwisRdb) ) {
		// The location (id), type, and time step uniquely identify the time series...
		TSTool_TS_TableModel model = (TSTool_TS_TableModel)__query_TableModel;
		String seqnum = (String)__query_TableModel.getValueAt( row, model.COL_SEQUENCE );
		if ( seqnum.length() == 0 ) {
			seqnum = null;
		}
		numCommandsAdded = queryResultsList_AppendTSIDToCommandList (
		(String)__query_TableModel.getValueAt( row, model.COL_ID ),
		(String)__query_TableModel.getValueAt( row, model.COL_DATA_SOURCE),
		(String)__query_TableModel.getValueAt( row, model.COL_DATA_TYPE),
		(String)__query_TableModel.getValueAt( row, model.COL_TIME_STEP ),
		(String)__query_TableModel.getValueAt( row, model.COL_SCENARIO ), seqnum, // Optional sequence number
		(String)__query_TableModel.getValueAt( row, model.COL_INPUT_TYPE),
		(String)__query_TableModel.getValueAt( row, model.COL_INPUT_NAME), "", false, insertOffset );
	}
	else {
	    String routine = getClass().getName() +
	    ".queryResultsList_TransferOneTSFromQueryResultsListToCommandList";
	    Message.printWarning(1, routine, "Transfer from query list to commands has not been implemented for \"" +
            selectedInputType + "\" input type." );
	    numCommandsAdded = 0;
	}
	// Check the GUI...
	if ( update_status ) {
		ui_UpdateStatus ( true );
	}
	return numCommandsAdded;
}

/**
Clear the results displays.
*/
private void results_Clear()
{
    results_Ensembles_Clear();
    results_OutputFiles_Clear();
	results_TimeSeries_Clear();
    results_Tables_Clear();
    results_Views_Clear();
}

/**
Add a time series ensemble description at the end of the time series ensemble results list.
Note that this does not add the actual ensemble, only a description string.
The ensembles are still accessed by the positions in the list.
@param tsensemble_info Time series ensemble information to add at the end of the list.
*/
private void results_Ensembles_AddEnsembleToResults ( final String tsensemble_info )
{   __resultsTSEnsembles_JListModel.addElement ( tsensemble_info );
}

/**
Clear the results ensemble List.  Updates to the label are also done.
*/
private void results_Ensembles_Clear()
{   // Clear the visible list of results...
    __resultsTSEnsembles_JListModel.removeAllElements();
    ui_UpdateStatus ( false );
}

/**
Get the ensemble identifier from a displayed ensemble item, which is in the format
"N) EnsembleID - Ensemble Name".
*/
private TSEnsemble results_Ensembles_GetEnsembleID ( String displayItem )
{   String routine = getClass().getName() + ".results_Ensembles_GetEnsembleID";
    String message;
    // Get the "N) EnsembleID" string...
    // This could still be an issue if the ensemble ID has " - " in the name, but at least it is less
    // likely than just "-"
    int pos = displayItem.indexOf( " - " );
    String s = displayItem.substring(0,pos).trim();
    // Now get the ensemble ID part of the string...
    pos = displayItem.indexOf( ")" );
    String ensembleID = s.substring(pos + 1).trim();
    PropList requestParams = new PropList ( "" );
    requestParams.set ( "EnsembleID", ensembleID );
    CommandProcessorRequestResultsBean bean = null;
    try {
        bean = __tsProcessor.processRequest( "GetEnsemble", requestParams);
    }
    catch ( Exception e ) {
        message = "Error requesting GetEnsemble(EnsembleID=\"" + ensembleID + "\".";
        Message.printWarning(3, routine, message );
        return null;
    }
    PropList bean_PropList = bean.getResultsPropList();
    Object o_tsEnsemble = bean_PropList.getContents ( "TSEnsemble" );
    if ( o_tsEnsemble == null ) {
        message = "Null TSEnsemble requesting GetEnsemble(EnsembleID=\"" + ensembleID + "\".";
        Message.printWarning(3, routine, message );
        return null;
    }
    TSEnsemble ensemble = (TSEnsemble)o_tsEnsemble;
    return ensemble;
}

// TODO SAM 2009-05-08 Evaluate sorting the files - maybe need to put in a worksheet so they can be sorted
/**
Add the specified output file to the list of output files that can be selected for viewing.
Only files that exist are added.  Files are added in the order of processing.
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
    if ( JGUIUtil.indexOf(__resultsOutputFiles_JList, filePathString, false, true) < 0 ) {
        __resultsOutputFiles_JListModel.addElement( filePathString );
    }
}

/**
Clear the list of output files.  This is normally called before the commands are run.
*/
private void results_OutputFiles_Clear()
{
	__resultsOutputFiles_JListModel.removeAllElements();
    ui_UpdateStatus ( false );
}

/**
Add the specified table to the list of tables that can be selected for viewing.
@param table Table generated by the processor.
*/
private void results_Tables_AddTable ( DataTable table )
{   String tableid = table.getTableID();
    if ( JGUIUtil.indexOf(__resultsTables_JList, tableid, false, true) < 0 ) {
         __resultsTables_JListModel.addElement( tableid );
    }
}

/**
Clear the list of results tables.  This is normally called immediately before the commands are run.
*/
private void results_Tables_Clear()
{
    __resultsTables_JListModel.removeAllElements();
}

/**
Add a time series description at the end of the time series results list.
Note that this does not add the actual time series, only a description string.
The time series are still accessed by the positions in the list.
@param ts_info Time series information to add at the end of the list.
*/
private void results_TimeSeries_AddTimeSeriesToResults ( final String ts_info )
{	__resultsTS_JListModel.addElement ( ts_info );
}

/**
Clear the final time series List.  Updates to the label are also done.
Also set the engine to null.
*/
private void results_TimeSeries_Clear()
{	// Clear the visible list of results...
	__resultsTS_JListModel.removeAllElements();
	ui_UpdateStatus ( false );
}

/**
Add the specified view to the list of views that can be selected for viewing.
@param view time series view generated by the processor.
*/
private void results_Views_AddView ( TimeSeriesView view )
{   String viewid = view.getViewID();
    // The tabbed pane is cleared before running commands so adding a view should be as simple
    // as adding a new panel to the tabbed pane
    if ( view instanceof TimeSeriesTreeView ) {
        // See the StateMod_DataSet_JTree for a more complex tree - this is pretty simple for now
        TimeSeriesTreeView treeView = (TimeSeriesTreeView)view;
        //__resultsViews_JTabbedPane.add(viewid,new JScrollPane(new JTree(treeView.getRootNode())));
        TimeSeriesTreeView_JTree tree = new TimeSeriesTreeView_JTree(treeView.getRootNode());
        __resultsViews_JTabbedPane.add(viewid,new JScrollPane(tree));
    }
    else {
        __resultsViews_JTabbedPane.add ( new JLabel("View is not implemented.") );
    }
}

/**
Clear the list of results views.  This is normally called immediately before the commands are run.
*/
private void results_Views_Clear()
{
    __resultsViews_JTabbedPane.removeAll();
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
		/* TODO SAM 2005-10-18 Currently time series features are not available...
		if ( __input_type_JComboBox != null ) {
			// Make sure HydroBase is in the input type list...
			int count = __input_type_JComboBox.getItemCount();
			boolean hbfound = false;
			for ( int i = 0; i < count; i++ ) {
				if ( __input_type_JComboBox.getItem(i).equals(__INPUT_TYPE_ColoradoSMS) ) {
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
		JGUIUtil.setEnabled ( __File_Properties_ColoradoSMS_JMenuItem, true );
	}
	else {
	    // Remove ColoradoSMS from the data source list if necessary...
		/* TODO SAM 2005-10-18 Currently time series cannot be queried
		try {
		    __input_type_JComboBox.remove ( __INPUT_TYPE_HydroBase);
		}
		catch ( Exception e ) {
			// Ignore - probably already removed...
		}
		*/
		JGUIUtil.setEnabled ( __File_Properties_ColoradoSMS_JMenuItem, false );
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
{	// Early on the GUI may not be initialized and some of the components seem to still be null...
	if ( !__guiInitialized ) {
		return;
	}
	
	// For the case where the license has expired, disable top-level menus except File...Exit and Help and return
	if ( license_IsLicenseExpired(license_GetLicenseManager()) ) {
	    ui_CheckGUIState_LicenseExpired();
	    return;
	}

	boolean enabled = true;		// Used to enable/disable main menus based on submenus.

	// Get the needed list sizes...

	int queryListSize = 0;
	if ( __query_TableModel != null ) {
		queryListSize = __query_TableModel.getRowCount();
	}

	int commandListSize = 0;
	int selectedCommandsSize = 0;
	if ( __commands_JListModel != null ) {
		commandListSize = __commands_JListModel.size();
		selectedCommandsSize = JGUIUtil.selectedSize ( ui_GetCommandJList() );
	}

	int tsListSize = 0;
	if ( __resultsTS_JListModel != null ) {
		tsListSize = __resultsTS_JListModel.size();
	}
	
	int dataStoreListSize = 0;
	if ( __tsProcessor != null ) {
	    dataStoreListSize = __tsProcessor.getDataStores().size();
	}
	
	// If no data stores are available, don't even show the data store choices - this should hopefully
	// minimize confusion between data stores and input type/name selections
	
	if ( dataStoreListSize == 0 ) {
	    __dataStore_JLabel.setVisible(false);
	    __dataStore_JComboBox.setVisible(false);
	}
	else {
	    __dataStore_JLabel.setVisible(true);
	    __dataStore_JComboBox.setVisible(true);
	}

	// List menus in the order of the GUI.  Popup menu items are checked as needed mixed in below...

	// File menu...

	enabled = false;
	if ( commandListSize > 0 ) {
		// Have commands shown...
		if ( __commandsDirty  ) {
			JGUIUtil.setEnabled ( __File_Save_Commands_JMenuItem,true );
		}
		else {
            JGUIUtil.setEnabled ( __File_Save_Commands_JMenuItem,false );
		}
		JGUIUtil.setEnabled ( __File_Save_CommandsAs_JMenuItem, true );
		JGUIUtil.setEnabled ( __File_Save_CommandsAsVersion9_JMenuItem, true );
		JGUIUtil.setEnabled ( __File_Print_Commands_JMenuItem, true );
		JGUIUtil.setEnabled ( __File_Print_JMenu, true );
		enabled = true;
	}
	else {
	    // No commands are shown.
		JGUIUtil.setEnabled ( __File_Save_Commands_JMenuItem, false );
		JGUIUtil.setEnabled ( __File_Save_CommandsAs_JMenuItem, false );
		JGUIUtil.setEnabled ( __File_Save_CommandsAsVersion9_JMenuItem, false );
	    JGUIUtil.setEnabled ( __File_Print_Commands_JMenuItem, false );
		JGUIUtil.setEnabled ( __File_Print_JMenu, false );
	}
	if ( tsListSize > 0 ) {
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
	if ( ui_GetHydroBaseDMI() != null ) {
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
	if ( __tsProcessor.getDataStoresByType(RiversideDBDataStore.class).size() > 0 ) {
		JGUIUtil.setEnabled ( __File_Properties_RiversideDB_JMenuItem,true );
	}
	else {
        JGUIUtil.setEnabled ( __File_Properties_RiversideDB_JMenuItem,false );
	}

	// Edit menu...

	enabled = false;
	if ( commandListSize > 0 ) {
	    enabled = true;
	}
	JGUIUtil.setEnabled ( __Edit_SelectAllCommands_JMenuItem, enabled);
	JGUIUtil.setEnabled ( __CommandsPopup_SelectAll_JMenuItem,enabled);
	JGUIUtil.setEnabled ( __Edit_DeselectAllCommands_JMenuItem,enabled);
	JGUIUtil.setEnabled ( __CommandsPopup_DeselectAll_JMenuItem,enabled);
	
	enabled = false;
	if ( selectedCommandsSize > 0 ) {
	    enabled = true;
	}
	JGUIUtil.setEnabled ( __Edit_CutCommands_JMenuItem,enabled);
	JGUIUtil.setEnabled ( __CommandsPopup_Cut_JMenuItem,enabled);
	JGUIUtil.setEnabled ( __Edit_CopyCommands_JMenuItem,enabled);
	JGUIUtil.setEnabled ( __CommandsPopup_Copy_JMenuItem,enabled);
	JGUIUtil.setEnabled ( __Edit_DeleteCommands_JMenuItem,enabled);
	JGUIUtil.setEnabled ( __CommandsPopup_Delete_JMenuItem,enabled);
	JGUIUtil.setEnabled ( __Edit_CommandWithErrorChecking_JMenuItem, enabled );
	JGUIUtil.setEnabled ( __CommandsPopup_Edit_CommandWithErrorChecking_JMenuItem,enabled );
	JGUIUtil.setEnabled ( __Edit_ConvertSelectedCommandsToComments_JMenuItem,enabled);
	JGUIUtil.setEnabled ( __CommandsPopup_ConvertSelectedCommandsToComments_JMenuItem,enabled);
	JGUIUtil.setEnabled ( __Edit_ConvertSelectedCommandsFromComments_JMenuItem,enabled);
	JGUIUtil.setEnabled ( __CommandsPopup_ConvertSelectedCommandsFromComments_JMenuItem,enabled);
    JGUIUtil.setEnabled ( __Edit_ConvertTSIDTo_ReadTimeSeries_JMenuItem,enabled);
    JGUIUtil.setEnabled ( __CommandsPopup_ConvertTSIDTo_ReadTimeSeries_JMenuItem,enabled);
    JGUIUtil.setEnabled ( __Edit_ConvertTSIDTo_ReadCommand_JMenuItem,enabled);
    JGUIUtil.setEnabled ( __CommandsPopup_ConvertTSIDTo_ReadCommand_JMenuItem,enabled);
	
	if ( __commandsCutBuffer.size() > 0 ) {
		// Paste button should be enabled...
		JGUIUtil.setEnabled ( __Edit_PasteCommands_JMenuItem, true );
		JGUIUtil.setEnabled ( __CommandsPopup_Paste_JMenuItem, true );
	}
	else {
		JGUIUtil.setEnabled ( __Edit_PasteCommands_JMenuItem, false );
		JGUIUtil.setEnabled ( __CommandsPopup_Paste_JMenuItem, false );
	}

	// View menu...
	
	if ( dataStoreListSize > 0 ) {
	    JGUIUtil.setEnabled ( __View_DataStores_JMenuItem, true );
	}
	else {
	    JGUIUtil.setEnabled ( __View_DataStores_JMenuItem, false );
	}
    if ( TSViewJFrame.getTSViewWindowManager().getWindowCount() > 0 ) {
        JGUIUtil.setEnabled ( __View_CloseAllViewWindows_JMenuItem, true );
    }
    else {
        JGUIUtil.setEnabled ( __View_CloseAllViewWindows_JMenuItem, false );
    }

	// Commands menu...

	enabled = false;
	if ( commandListSize > 0 ) {
	    // Some commands are available so enable commands that operate on other time series
	    enabled = true;
	}
    JGUIUtil.setEnabled ( __Commands_Create_Delta_JMenuItem, enabled);
    JGUIUtil.setEnabled ( __Commands_Create_ResequenceTimeSeriesData_JMenuItem, enabled);
	JGUIUtil.setEnabled ( __Commands_Create_ChangeInterval_JMenuItem, enabled);
	JGUIUtil.setEnabled ( __Commands_Create_Copy_JMenuItem, enabled);
	JGUIUtil.setEnabled ( __Commands_Create_Disaggregate_JMenuItem, enabled);
	JGUIUtil.setEnabled ( __Commands_Create_LookupTimeSeriesFromTable_JMenuItem, enabled);
	JGUIUtil.setEnabled ( __Commands_Create_NewDayTSFromMonthAndDayTS_JMenuItem,	enabled );
	JGUIUtil.setEnabled ( __Commands_Create_NewEndOfMonthTSFromDayTS_JMenuItem,enabled);
	JGUIUtil.setEnabled ( __Commands_Create_Normalize_JMenuItem, enabled);
	JGUIUtil.setEnabled ( __Commands_Create_RelativeDiff_JMenuItem, enabled);
    JGUIUtil.setEnabled ( __Commands_Create_NewStatisticTimeSeries_JMenuItem,enabled);
    JGUIUtil.setEnabled ( __Commands_Create_NewStatisticYearTS_JMenuItem,enabled);
    JGUIUtil.setEnabled ( __Commands_Create_RunningStatisticTimeSeries_JMenuItem, enabled);

	JGUIUtil.setEnabled ( __Commands_Fill_FillConstant_JMenuItem, enabled);
	JGUIUtil.setEnabled ( __Commands_Fill_FillDayTSFrom2MonthTSAnd1DayTS_JMenuItem, enabled);
	JGUIUtil.setEnabled ( __Commands_Fill_FillFromTS_JMenuItem, enabled);
	JGUIUtil.setEnabled ( __Commands_Fill_FillHistMonthAverage_JMenuItem, enabled);
	JGUIUtil.setEnabled ( __Commands_Fill_FillHistYearAverage_JMenuItem, enabled);
	JGUIUtil.setEnabled ( __Commands_Fill_FillInterpolate_JMenuItem, enabled);
	// TODO SAM 2005-04-26 This fill method is not enabled - may not be needed.
	//JGUIUtil.setEnabled(__Commands_Fill_fillMOVE1_JMenuItem,enabled);
	JGUIUtil.setEnabled ( __Commands_Fill_FillMOVE2_JMenuItem, enabled);
	JGUIUtil.setEnabled ( __Commands_Fill_FillMixedStation_JMenuItem,enabled );
	JGUIUtil.setEnabled ( __Commands_Fill_FillPattern_JMenuItem,enabled);
	JGUIUtil.setEnabled ( __Commands_Fill_FillProrate_JMenuItem,enabled);
	JGUIUtil.setEnabled ( __Commands_Fill_FillRegression_JMenuItem,enabled);
	JGUIUtil.setEnabled ( __Commands_Fill_FillRepeat_JMenuItem,enabled);
	JGUIUtil.setEnabled ( __Commands_Fill_FillUsingDiversionComments_JMenuItem,	enabled);
	JGUIUtil.setEnabled ( __Commands_FillTimeSeries_JMenu, enabled );

	JGUIUtil.setEnabled ( __Commands_Set_ReplaceValue_JMenuItem,enabled);
	JGUIUtil.setEnabled ( __Commands_Set_SetConstant_JMenuItem, enabled);
	JGUIUtil.setEnabled ( __Commands_Set_SetDataValue_JMenuItem,enabled);
	JGUIUtil.setEnabled ( __Commands_Set_SetFromTS_JMenuItem, enabled);
	JGUIUtil.setEnabled ( __Commands_Set_SetToMax_JMenuItem, enabled);
	JGUIUtil.setEnabled ( __Commands_Set_SetToMin_JMenuItem, enabled);
    JGUIUtil.setEnabled ( __Commands_Set_SetTimeSeriesProperty_JMenuItem, enabled );
	JGUIUtil.setEnabled ( __Commands_SetTimeSeries_JMenu, enabled );

	JGUIUtil.setEnabled ( __Commands_Manipulate_Add_JMenuItem, enabled);
	JGUIUtil.setEnabled ( __Commands_Manipulate_AddConstant_JMenuItem, enabled);
	JGUIUtil.setEnabled ( __Commands_Manipulate_AdjustExtremes_JMenuItem, enabled);
	JGUIUtil.setEnabled ( __Commands_Manipulate_ARMA_JMenuItem, enabled);
	JGUIUtil.setEnabled ( __Commands_Manipulate_Blend_JMenuItem,enabled);
    JGUIUtil.setEnabled ( __Commands_Manipulate_ChangePeriod_JMenuItem,enabled);
	JGUIUtil.setEnabled ( __Commands_Manipulate_ConvertDataUnits_JMenuItem,enabled);
	JGUIUtil.setEnabled ( __Commands_Manipulate_Cumulate_JMenuItem,enabled);
	JGUIUtil.setEnabled ( __Commands_Manipulate_Divide_JMenuItem,enabled);
	JGUIUtil.setEnabled ( __Commands_Manipulate_Free_JMenuItem,enabled);
	JGUIUtil.setEnabled ( __Commands_Manipulate_Multiply_JMenuItem,enabled);
	JGUIUtil.setEnabled ( __Commands_Manipulate_RunningAverage_JMenuItem, enabled);
	JGUIUtil.setEnabled ( __Commands_Manipulate_Scale_JMenuItem,enabled);
	JGUIUtil.setEnabled ( __Commands_Manipulate_ShiftTimeByInterval_JMenuItem,enabled);
	JGUIUtil.setEnabled ( __Commands_Manipulate_Subtract_JMenuItem, enabled);
	JGUIUtil.setEnabled ( __Commands_ManipulateTimeSeries_JMenu,enabled);

	JGUIUtil.setEnabled ( __Commands_Analyze_AnalyzePattern_JMenuItem, enabled);
    JGUIUtil.setEnabled ( __Commands_Analyze_CalculateTimeSeriesStatistic_JMenuItem, enabled);
	JGUIUtil.setEnabled ( __Commands_Analyze_CompareTimeSeries_JMenuItem, enabled);
	JGUIUtil.setEnabled ( __Commands_Analyze_ComputeErrorTimeSeries_JMenuItem, enabled);
	JGUIUtil.setEnabled ( __Commands_AnalyzeTimeSeries_JMenu, enabled);

	JGUIUtil.setEnabled ( __Commands_Models_Routing_JMenu, enabled);
    
	JGUIUtil.setEnabled ( __Commands_Output_SortTimeSeries_JMenuItem, enabled);
	JGUIUtil.setEnabled ( __Commands_Output_WriteDateValue_JMenuItem, enabled);
    JGUIUtil.setEnabled ( __Commands_Output_WriteHecDss_JMenuItem,enabled);
	JGUIUtil.setEnabled ( __Commands_Output_WriteNwsCard_JMenuItem,enabled);
	if ( __tsProcessor.getDataStoresByType(ReclamationHDBDataStore.class).size() > 0 ) {
	    JGUIUtil.setEnabled ( __Commands_Output_WriteReclamationHDB_JMenuItem, true );
	}
	else {
	    JGUIUtil.setEnabled ( __Commands_Output_WriteReclamationHDB_JMenuItem, false );
	}
    if ( __tsProcessor.getDataStoresByType(RiversideDBDataStore.class).size() > 0 ) {
        JGUIUtil.setEnabled ( __Commands_Output_WriteRiversideDB_JMenuItem, true );
    }
    else {
        JGUIUtil.setEnabled ( __Commands_Output_WriteRiversideDB_JMenuItem, false );
    }
	JGUIUtil.setEnabled ( __Commands_Output_WriteRiverWare_JMenuItem, enabled);
    JGUIUtil.setEnabled ( __Commands_Output_WriteSHEF_JMenuItem, enabled);
	JGUIUtil.setEnabled ( __Commands_Output_WriteStateCU_JMenuItem, enabled);
	JGUIUtil.setEnabled ( __Commands_Output_WriteStateMod_JMenuItem, enabled);
	JGUIUtil.setEnabled ( __Commands_Output_WriteSummary_JMenuItem, enabled);
	JGUIUtil.setEnabled ( __Commands_Output_WriteWaterML_JMenuItem, enabled);
	JGUIUtil.setEnabled ( __Commands_Output_DeselectTimeSeries_JMenuItem, enabled);
	JGUIUtil.setEnabled ( __Commands_Output_SelectTimeSeries_JMenuItem, enabled);
    
    JGUIUtil.setEnabled ( __Commands_Ensemble_CreateEnsembleFromOneTimeSeries_JMenuItem, enabled);
    JGUIUtil.setEnabled ( __Commands_Ensemble_CopyEnsemble_JMenuItem, enabled);
    JGUIUtil.setEnabled ( __Commands_Ensemble_InsertTimeSeriesIntoEnsemble_JMenuItem, enabled);
    JGUIUtil.setEnabled ( __Commands_Ensemble_NewStatisticEnsemble_JMenuItem,enabled);
    JGUIUtil.setEnabled ( __Commands_Ensemble_NewStatisticTimeSeriesFromEnsemble_JMenuItem,enabled);
    JGUIUtil.setEnabled ( __Commands_Ensemble_WeightTraces_JMenuItem, enabled);
    JGUIUtil.setEnabled ( __Commands_Ensemble_WriteNwsrfsEspTraceEnsemble_JMenuItem,enabled);
    
    JGUIUtil.setEnabled ( __Commands_Check_CheckingResults_CheckTimeSeries_JMenuItem,enabled);
    JGUIUtil.setEnabled ( __Commands_Check_CheckingResults_CheckTimeSeriesStatistic_JMenuItem,enabled);

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
	if ( selectedCommandsSize > 0 ) {
		JGUIUtil.setEnabled ( __Run_SelectedCommands_JButton, true );
	}
	else {
        JGUIUtil.setEnabled ( __Run_SelectedCommands_JButton,false );
	}
	JGUIUtil.setEnabled ( __Run_AllCommands_JButton, true );
	JGUIUtil.setEnabled ( __ClearCommands_JButton, true );
	
	enabled = false;
	if ( selectedCommandsSize > 0 ) {
	    enabled = true;
	}
	JGUIUtil.setEnabled ( __CommandsPopup_ConvertTSIDTo_ReadTimeSeries_JMenuItem,enabled);
	JGUIUtil.setEnabled ( __CommandsPopup_ConvertTSIDTo_ReadCommand_JMenuItem,enabled);

	// Run menu...
	
	ui_CheckGUIState_RunMenu ( commandListSize, selectedCommandsSize );

	// Results menu...

	if ( tsListSize > 0 ) {
		JGUIUtil.setEnabled ( __Results_JMenu, true );
	}
	else {
        // DISABLE file and view options...
		JGUIUtil.setEnabled ( __Results_JMenu, false );
	}

	// Tools menu...

	if ( tsListSize > 0 ) {
		JGUIUtil.setEnabled ( __Tools_Analysis_JMenu, true );
		JGUIUtil.setEnabled ( __Tools_Report_JMenu, true );
	}
	else {
        JGUIUtil.setEnabled ( __Tools_Analysis_JMenu, false );
		JGUIUtil.setEnabled ( __Tools_Report_JMenu, false );
	}
	if ( (queryListSize > 0) && (__geoview_JFrame != null) ) {
		JGUIUtil.setEnabled ( __Tools_SelectOnMap_JMenuItem, true );
	}
	else {
        JGUIUtil.setEnabled ( __Tools_SelectOnMap_JMenuItem, false );
	}

	// Enable/disable features related to the query list...

	if ( queryListSize > 0 ) {
		JGUIUtil.setEnabled ( __CopyAllToCommands_JButton, true );
	}
	else {
        JGUIUtil.setEnabled ( __CopyAllToCommands_JButton, false );
	}
	if ( (queryListSize > 0) && (__query_JWorksheet != null) &&
		(__query_JWorksheet.getSelectedRowCount() > 0) ) {
		JGUIUtil.setEnabled ( __CopySelectedToCommands_JButton, true );
	}
	else {
        JGUIUtil.setEnabled ( __CopySelectedToCommands_JButton, false );
	}

	// TODO Not available as a command yet - logic not coded...
	JGUIUtil.setEnabled(__Commands_Fill_FillMOVE1_JMenuItem,false);

	// TODO - can this be phased out?
	JGUIUtil.setEnabled(__Commands_Output_SetOutputDetailedHeaders_JMenuItem,false);
}

/**
Disable GUI features based on an expired license.
*/
private void ui_CheckGUIState_LicenseExpired () {
    Runnable r = new Runnable() {
        public void run() {
            JGUIUtil.setEnabled ( __File_New_JMenu, false );
            JGUIUtil.setEnabled ( __File_Open_JMenu, false );
            JGUIUtil.setEnabled ( __File_Save_JMenu, false );
            JGUIUtil.setEnabled ( __File_Print_JMenu, false );
            JGUIUtil.setEnabled ( __File_Properties_JMenu, false );
            JGUIUtil.setEnabled ( __File_SetWorkingDirectory_JMenuItem, false );
            JGUIUtil.setEnabled ( __Edit_JMenu, false );
            JGUIUtil.setEnabled ( __View_JMenu, false );
            JGUIUtil.setEnabled ( __Commands_JMenu, false );
            JGUIUtil.setEnabled ( __Run_JMenu, false );
            JGUIUtil.setEnabled ( __Results_JMenu, false );
            JGUIUtil.setEnabled ( __Tools_JMenu, false );
            // Buttons
            JGUIUtil.setEnabled ( __get_ts_list_JButton, false );
            JGUIUtil.setEnabled ( __CopyAllToCommands_JButton, false );
            JGUIUtil.setEnabled ( __CopySelectedToCommands_JButton, false );
            JGUIUtil.setEnabled ( __Run_SelectedCommands_JButton, false );
            JGUIUtil.setEnabled ( __Run_AllCommands_JButton, false );
            JGUIUtil.setEnabled ( __ClearCommands_JButton, false );
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
Enable/disable Run menu items based on the state of the GUI.
*/
private void ui_CheckGUIState_RunMenu ( int command_list_size, int selected_commands_size )
{
	// Running, so allow cancel but not another run...
	boolean enable_run = false;
	if ( (__tsProcessor != null) && __tsProcessor.getIsRunning() ) {
		// Running, so allow cancel but not another run...
		enable_run = false;  // Handled below in second if
		JGUIUtil.setEnabled (__Run_CancelCommandProcessing_JMenuItem, true);
		JGUIUtil.setEnabled (__CommandsPopup_CancelCommandProcessing_JMenuItem,true);

	}
	else {
		// Not running, so disable cancel, but do allow run if there are commands (see below)...
		enable_run = true;
		JGUIUtil.setEnabled (__Run_CancelCommandProcessing_JMenuItem, false);
		JGUIUtil.setEnabled (__CommandsPopup_CancelCommandProcessing_JMenuItem,false );
	}
	if ( command_list_size > 0 ) {
		/* TODO SAM 2007-11-01 Evaluate need
		if ( __commands_dirty || (__commands_dirty && (ts_list_size == 0)) ) {
			JGUIUtil.setEnabled (__run_commands_JButton, true );
		}
		else {
		    JGUIUtil.setEnabled ( __run_commands_JButton, false );
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
	else {
	    // No commands in list...
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
Enable/disable the HydroBase input type features depending on whether a HydroBaseDMI connection has been made.
*/
private void ui_CheckHydroBaseFeatures ()
{	String routine = getClass().getName() + ".ui_CheckHydroBaseFeatures";
    HydroBaseDMI hbdmi = ui_GetHydroBaseDMI();
    Message.printStatus(2, routine, "In check, connected=" + hbdmi.connected() + " isOpen=" + hbdmi.isOpen() );
    if ( (hbdmi != null) && hbdmi.isOpen() ) {
        Message.printStatus ( 2, routine, "HydroBase connection is available... adding its features to UI..." );
		if ( __input_type_JComboBox != null ) {
			// Make sure HydroBase is in the input type list...
			int count = __input_type_JComboBox.getItemCount();
			boolean hbfound = false;
			for ( int i = 0; i < count; i++ ) {
				if ( __input_type_JComboBox.getItem(i).equals(__INPUT_TYPE_HydroBase) ) {
					hbfound = true;
					break;
				}
			}
			if ( !hbfound ) {
				// Repopulate the input types...
				ui_SetInputTypeChoices();
			}
		}
		JGUIUtil.setEnabled (__File_Properties_HydroBase_JMenuItem, true );
	}
	else {
	    // Remove HydroBase from the input type list if necessary...
	    Message.printStatus ( 2, routine,
	        "HydroBase connection is not available... removing its features to UI..." );
		try {
		    __input_type_JComboBox.remove ( __INPUT_TYPE_HydroBase);
		}
		catch ( Exception e ) {
			// Ignore - probably already removed...
		}
		JGUIUtil.setEnabled ( __File_Properties_HydroBase_JMenuItem, false );
	}
}

// TODO SAM 2011-03-21 It would be great to have this go away and rely only on the
// configuration file properties and plug-in jars
/**
Disable input types depending on the license.  For example, if the license type
is "CDSS", then RTi-developed input types like RiversideDB and DIADvisor are
disabled.  For the most part, input types should all be enabled and only
CDSS turns input types off in this method.
@param licenseManager license manager for the session.
*/
private void ui_CheckInputTypesForLicense ( LicenseManager licenseManager )
{	if ( licenseManager == null ) {
		return;
	}
	String routine = "TSTool_JFrame.checkInputTypesForLicense";
	if ( licenseManager.getLicenseType().equalsIgnoreCase("CDSS") ) {
	    // These generally are not going to be used with CDSS
		//Message.printStatus ( 2, routine, "DIADvisor input type being disabled for CDSS." );
		//__source_DIADvisor_enabled = false;
		//Message.printStatus ( 2, routine, "Mexico CSMN input type being disabled for CDSS." );
		//__source_MexicoCSMN_enabled = false;
		// TODO SAM 2008-10-02 Evaluate whether to permanently make this change in defaults
		// Allow MODSIM to be turned on for CDSS since it appears to be a model that might be used
		// in some parts of the State.
		//Message.printStatus ( 2, routine, "MODSIM input type being disabled for CDSS." );
		//__source_MODSIM_enabled = false;
		//Message.printStatus ( 2, routine, "RiversideDB data stores being disabled for CDSS." );
		//__source_RiversideDB_enabled = false;
		//Message.printStatus ( 2, routine, "SHEF input type being disabled for CDSS." );
		//__source_SHEF_enabled = false;
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
			if ( __input_type_JComboBox.getItem(i).equals(__INPUT_TYPE_NWSRFS_FS5Files) ) {
				rfound = true;
				break;
			}
		}
		if ( !rfound ) {
			// Repopulate the data sources...
			ui_SetInputTypeChoices();
		}
		if (__File_Properties_NWSRFSFS5Files_JMenuItem != null ) {
			__File_Properties_NWSRFSFS5Files_JMenuItem.setEnabled(true);
		}
	}
	else {
	    // NWSRFS FS5Files connection failed, but do not remove from
		// input types because a new connection currently can be established from the interactive.
		/*
		try {
		    __input_type_JComboBox.remove(__INPUT_TYPE_NWSRFS_FS5FILES);
		}
		catch ( Exception e ) {
			// Ignore - probably already removed...
		}
		*/
		if (__File_Properties_NWSRFSFS5Files_JMenuItem != null ) {
			__File_Properties_NWSRFSFS5Files_JMenuItem.setEnabled ( false );
		}
	}
}

/**
Take a full filename string and if it is too long, create an abbreviated filename that fits into the GUI better.
The front and the rear of the file are retained, with ... in the middle.
@param fullFilename the full filename string to check.
@return the abbreviated filename, if longer than the maximum allowed for display purposes.
*/
private String ui_CreateAbbreviatedVisibleFilename(String fullFilename)
{   int maxLengthAllowed = 60;
    if ( fullFilename.length() < maxLengthAllowed ) {
        return fullFilename;
    }
    else {
        // First try - remove the middle characters, retaining the ends without checking path breaks
        int numCharsToRemove = fullFilename.length() - maxLengthAllowed;
        int numCharsToRetainEachEnd = (fullFilename.length() - numCharsToRemove)/2;
        return fullFilename.substring(0,numCharsToRetainEachEnd) + "..." +
            fullFilename.substring(fullFilename.length() - numCharsToRetainEachEnd);
    }
}

/**
Populate the data store list from available processor data stores.
*/
private void ui_DataStoreList_Populate ()
{
    __dataStore_JComboBox.removeAll();
    List<String> dataStoreNameList = new Vector();
    dataStoreNameList.add ( "" ); // Blank when picking input type and name separately
    List<DataStore> dataStoreList = __tsProcessor.getDataStores();
    for ( DataStore dataStore : dataStoreList ) {
        if ( dataStore.getName().equalsIgnoreCase("UsgsNwisDaily") ) {
            // For now disable in the main browser
            continue;
        }
        dataStoreNameList.add ( dataStore.getName() );
    }
    __dataStore_JComboBox.setData(dataStoreNameList);
    // Select the blank
    __dataStore_JComboBox.select("");
}

/**
Enable the input and data store types based on the TSTool configuration.  Features will
 */
private void ui_EnableInputTypesForConfiguration ()
{
    // Determine which data sources are available.  This controls the look
    // and feel of the GUI.  Alphabetize the checks.  A default value is set for the case where
    // the configuration file may not contain a property, and then a check of the string value
    // is done to set to a value other than the default

    String propValue = null;
    
    // Colorado BNDSS disabled by default (since only used in CDSS)...

    __source_ColoradoBNDSS_enabled = false;
    propValue = TSToolMain.getPropValue ( "TSTool.ColoradoBNDSSEnabled" );
    if ( (propValue != null) && propValue.equalsIgnoreCase("true") ) {
        __source_ColoradoBNDSS_enabled = true;
    }
    
    // ColoradoSMS disabled by default (used in CDSS, requires direct SQL Server access)...

    __source_ColoradoSMS_enabled = false;
    propValue = TSToolMain.getPropValue ( "TSTool.ColoradoSMSEnabled" );
    if ( (propValue != null) && propValue.equalsIgnoreCase("true") ) {
        __source_ColoradoSMS_enabled = true;
    }
    
    // State of Colorado HBGuest web service disabled by default
    // (can be slow at startup due to input filter initialization) but can turn on...

    __source_ColoradoWaterHBGuest_enabled = false;
    propValue = TSToolMain.getPropValue ( "TSTool.ColoradoWaterHBGuestEnabled" );
    if ( (propValue != null) && propValue.equalsIgnoreCase("true") ) {
        __source_ColoradoWaterHBGuest_enabled = true;
    }
    
    // State of Colorado Water SMS web service enabled by default
    // (fast startup because no input filter initialization)...

    __source_ColoradoWaterSMS_enabled = true;
    propValue = TSToolMain.getPropValue ( "TSTool.ColoradoWaterSMSEnabled" );
    if ( (propValue != null) && propValue.equalsIgnoreCase("false") ) {
        __source_ColoradoWaterSMS_enabled = false;
    }
    
    // DateValue enabled by default...

    __source_DateValue_enabled = true;
    propValue = TSToolMain.getPropValue ( "TSTool.DateValueEnabled" );
    if ( (propValue != null) && propValue.equalsIgnoreCase("false") ) {
        __source_DateValue_enabled = false;
    }

    // DIADvisor disabled by default (only useful for users with DIADvisor)...

    __source_DIADvisor_enabled = false;
    propValue = TSToolMain.getPropValue ( "TSTool.DIADvisorEnabled" );
    if ( (propValue != null) && propValue.equalsIgnoreCase("true") ) {
        __source_DIADvisor_enabled = true;
    }
    
    // HEC-DSS enabled by default on Windows and always off on UNIX because DLLs are for Windows.
    // TODO SAM 2009-04-14 Emailed Bill Charley and waiting on answer for support of other OS.

    if ( IOUtil.isUNIXMachine() ) {
        // Disable on UNIX
        __source_HECDSS_enabled = false;
    }
    else {
        __source_HECDSS_enabled = true;
        propValue = TSToolMain.getPropValue ( "TSTool.HEC-DSSEnabled" );
        if ( (propValue != null)&& propValue.equalsIgnoreCase("false") ) {
            __source_HECDSS_enabled = false;
        }
    }
    
    // State of Colorado HydroBase disabled by default (users must have a local HydroBase)...

    __source_HydroBase_enabled = false;
    // Newer...
    propValue = TSToolMain.getPropValue ( "TSTool.HydroBaseEnabled" );
    if ( propValue == null ) {
        // Older...
        propValue = TSToolMain.getPropValue ("TSTool.HydroBaseCOEnabled" );
    }
    if ( (propValue != null) && propValue.equalsIgnoreCase("true") ) {
        __source_HydroBase_enabled = true;
    }
    else if ( propValue == null ) {
        // No property defined.  Make sure to turn on for CDSS installation (false property above will
        // have disabled).
        if ( license_GetLicenseManager().getLicenseType().equalsIgnoreCase("CDSS") ) {
            __source_HydroBase_enabled = true;
        }
    }
    if ( __source_HydroBase_enabled ) {
        // Use newer notation...
        __props.set ( "TSTool.HydroBaseEnabled", "true" );
        propValue = TSToolMain.getPropValue ( "HydroBase.WDIDLength" );
        if ( (propValue != null) && StringUtil.isInteger(propValue)){
            __props.set ( "HydroBase.WDIDLength", propValue );
            // Also set in global location.
            HydroBase_Util.setPreferredWDIDLength ( StringUtil.atoi ( propValue ) );
        }
        else {
            // Default...
            __props.set ( "HydroBase.WDIDLength", "7" );
        }
        propValue = TSToolMain.getPropValue ( "HydroBase.OdbcDsn" );
        if ( propValue != null ) {
            __props.set ( "HydroBase.OdbcDsn", propValue );
        }
    }
    else {
        __props.set ( "TSTool.HydroBaseEnabled", "false" );
    }

    // Mexico CSMN disabled by default...

    __source_MexicoCSMN_enabled = false;
    propValue = TSToolMain.getPropValue ( "TSTool.MexicoCSMNEnabled" );
    if ( (propValue != null) && propValue.equalsIgnoreCase("true") ) {
        __source_MexicoCSMN_enabled = true;
    }

    // MODSIM always enabled by default...

    __source_MODSIM_enabled = true;
    propValue = TSToolMain.getPropValue ( "TSTool.MODSIMEnabled" );
    if ( (propValue != null) && propValue.equalsIgnoreCase("false") ) {
        __source_MODSIM_enabled = false;
    }
    
    // NWS Card enabled by default...

    __source_NWSCard_enabled = true;
    propValue = TSToolMain.getPropValue ( "TSTool.NWSCardEnabled" );
    if ( (propValue != null) && propValue.equalsIgnoreCase("false") ) {
        __source_NWSCard_enabled = false;
    }

    // NWSRFS_ESPTraceEnsemble disabled by default...

    __source_NWSRFS_ESPTraceEnsemble_enabled = false;
    propValue = TSToolMain.getPropValue ( "TSTool.NWSRFSESPTraceEnsembleEnabled" );
    if ( (propValue != null) && propValue.equalsIgnoreCase("true") ) {
        __source_NWSRFS_ESPTraceEnsemble_enabled = true;
    }
    // Always disable if not available in the Jar file...
    if ( !IOUtil.classCanBeLoaded( "RTi.DMI.NWSRFS_DMI.NWSRFS_ESPTraceEnsemble") ) {
        __source_NWSRFS_ESPTraceEnsemble_enabled = false;
    }

    // NWSRFS FS5Files disabled by default...

    __source_NWSRFS_FS5Files_enabled = false;
    propValue = TSToolMain.getPropValue ( "TSTool.NWSRFSFS5FilesEnabled" );
    if ( (propValue != null) && propValue.equalsIgnoreCase("true") ){
        __source_NWSRFS_FS5Files_enabled = true;
        propValue = TSToolMain.getPropValue ( "NWSRFSFS5Files.UseAppsDefaults" );
        if ( propValue != null ) {
            __props.set( "NWSRFSFS5Files.UseAppsDefaults",propValue );
        }
        else {
            // Default is to use files.  If someone has Apps
            // Defaults configured, this property can be changed.
            __props.set ( "NWSRFSFS5Files.UseAppsDefaults","false");
        }
        propValue = TSToolMain.getPropValue ( "NWSRFSFS5Files.InputName" );
        if ( propValue != null ) {
            __props.set( "NWSRFSFS5Files.InputName",propValue );
        }
    }
    
    // RCC ACIS disabled by default but distributed configuration file has enabled by default...

    __source_RCCACIS_enabled = false;
    propValue = TSToolMain.getPropValue ( "TSTool.RCCACISEnabled" );
    if ( (propValue != null) && propValue.equalsIgnoreCase("true") ) {
        __source_RCCACIS_enabled = true;
    }
    
    // Reclamation HDB disabled by default (not many users would have)...

    __source_ReclamationHDB_enabled = false;
    propValue = TSToolMain.getPropValue ( "TSTool.ReclamationHDBEnabled" );
    if ( (propValue != null) && propValue.equalsIgnoreCase("true") ) {
        __source_ReclamationHDB_enabled = true;
    }

    // RiversideDB disabled by default...

    __source_RiversideDB_enabled = false;
    propValue = TSToolMain.getPropValue ( "TSTool.RiversideDBEnabled" );
    if ( (propValue != null) && propValue.equalsIgnoreCase("true") ) {
        __source_RiversideDB_enabled = true;
    }

    // RiverWare enabled by default...

    __source_RiverWare_enabled = true;
    propValue = TSToolMain.getPropValue ( "TSTool.RiverWareEnabled" );
    if ( (propValue != null) && propValue.equalsIgnoreCase("false") ) {
        __source_RiverWare_enabled = false;
    }

    // SHEF disabled by default...

    __source_SHEF_enabled = false;
    propValue = TSToolMain.getPropValue ( "TSTool.SHEFEnabled" );
    if ( (propValue != null) && propValue.equalsIgnoreCase("true") ) {
        __source_SHEF_enabled = true;
    }

    // StateCU enabled by default...

    __source_StateCU_enabled = true;
    propValue = TSToolMain.getPropValue ( "TSTool.StateCUEnabled" );
    if ( (propValue != null) && propValue.equalsIgnoreCase("false") ) {
        __source_StateCU_enabled = false;
    }
    
    // StateCUB enabled by default...

    __source_StateCUB_enabled = true;
    propValue = TSToolMain.getPropValue ( "TSTool.StateCUBEnabled" );
    if ( (propValue != null) && propValue.equalsIgnoreCase("false") ) {
        __source_StateCUB_enabled = false;
    }

    // StateMod enabled by default...

    __source_StateMod_enabled = true;
    propValue = TSToolMain.getPropValue ( "TSTool.StateModEnabled" );
    if ( (propValue != null) && propValue.equalsIgnoreCase("false") ) {
        __source_StateMod_enabled = false;
    }

    // StateModB enabled by default...

    __source_StateModB_enabled = true;
    propValue = TSToolMain.getPropValue ( "TSTool.StateModBEnabled" );
    if ( (propValue != null) && propValue.equalsIgnoreCase("false") ) {
        __source_StateModB_enabled = false;
    }
    
    // UsgsNwisDaily enabled by default...

    __source_UsgsNwisDaily_enabled = true;
    propValue = TSToolMain.getPropValue ( "TSTool.UsgsNwisDailyEnabled" );
    if ( (propValue != null) && propValue.equalsIgnoreCase("false") ) {
        __source_UsgsNwisDaily_enabled = false;
    }

    // UsgsNwisRdb enabled by default...

    __source_UsgsNwisRdb_enabled = true;
    // New...
    propValue = TSToolMain.getPropValue ( "TSTool.UsgsNwisRdbEnabled" );
    // Legacy...
    if ( propValue == null ) {
        propValue = TSToolMain.getPropValue ( "TSTool.USGSNWISEnabled" );
    }
    if ( (propValue != null) && propValue.equalsIgnoreCase("false") ) {
        __source_UsgsNwisRdb_enabled = false;
    }
    
    // WaterML enabled by default...

    __source_WaterML_enabled = true;
    propValue = TSToolMain.getPropValue ( "TSTool.WaterMLEnabled" );
    if ( (propValue != null) && propValue.equalsIgnoreCase("false") ) {
        __source_WaterML_enabled = false;
    }
    
    // WaterOneFlow enabled by default...

    __source_WaterOneFlow_enabled = true;
    propValue = TSToolMain.getPropValue ( "TSTool.WaterOneFlowEnabled" );
    if ( (propValue != null) && propValue.equalsIgnoreCase("false") ) {
        __source_WaterOneFlow_enabled = false;
    }
}

/**
Return the command list component.  Do it this way because the command component may evolve.
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
Return the HydroBaseDataStore instance that is active for the UI, opened from
the configuration file information or the HydroBase select dialog.
@return the HydroBaseDataStore instance that is active for the UI.
*/
private HydroBaseDataStore ui_GetHydroBaseDataStore ()
{
	return __hbDataStore;
}

/**
Return the HydroBaseDMI instance that is active for the UI, opened from
the configuration file information or the HydroBase select dialog.
@return the HydroBaseDMI instance that is active for the UI, or null if not used.
*/
private HydroBaseDMI ui_GetHydroBaseDMI ()
{   HydroBaseDMI dmi = null;
    HydroBaseDataStore ds = ui_GetHydroBaseDataStore();
    if ( ds != null ) {
        if ( ds.getDMI() != null ) {
            dmi = (HydroBaseDMI)ds.getDMI();
        }
    }
    return dmi;
}

/**
Return whether ActionEvents should be ignored.
*/
private boolean ui_GetIgnoreActionEvent()
{
    return __ignoreActionEvent;
}

/**
Return whether ItemEvents should be ignored.
*/
private boolean ui_GetIgnoreItemEvent()
{
    return __ignoreItemEvent;
}

/**
Return whether ListSelectionEvents should be ignored.
*/
private boolean ui_GetIgnoreListSelectionEvent()
{
    return __ignoreListSelectionEvent;
}

/**
Return the Y layout coordinate used with the input filter panel.  This is saved
because input types can be opened interactively and need to be added to the same location in the layout.
*/
private int ui_GetInputFilterY()
{
    return __inputFilterY;
}

/**
Return the message input filter panel, with the indicated message.
This is used, for example, when displaying a message that a HydroBase database connection is unavailable,
rather than just showing the generic input filter that provides no information.
 */
private InputFilter_JPanel ui_GetInputFilterMessageJPanel ( String text )
{
    __inputFilterMessage_JPanel.setText ( text );
    return __inputFilterMessage_JPanel;
}

/**
Return the input filter panel for the specified data store name.  By design, this method should only be called
when working with data stores.  Eventually all the "database" input types will
be handled (including binary files and relational databases).
@param dataStoreName name of data store to match
@param dataType the selected data type
@return the input filter panel that matches the data store name, or null if not found
*/
private JPanel ui_GetInputFilterPanelForDataStoreName ( String dataStoreName, String dataType )
{
    // This is a bit brute force because the name is embedded in the data store but is not
    // a data member of the input panel
    // Alphabetize by "instanceof" argument
    for ( JPanel panel : __inputFilterJPanelList ) {
        if ( (panel instanceof ColoradoWaterHBGuest_GUI_StationGeolocMeasType_InputFilter_JPanel) ) {
            // This type of filter uses a DataStore
            ColoradoWaterHBGuestDataStore dataStore =
                ((ColoradoWaterHBGuest_GUI_StationGeolocMeasType_InputFilter_JPanel)panel).getColoradoWaterHBGuestDataStore();
            if ( dataStore.getName().equalsIgnoreCase(dataStoreName) ) {
                // Have a match in the data store name so return the panel
                return panel;
            }
        }
        else if ( (panel instanceof ColoradoWaterHBGuest_GUI_StructureGeolocMeasType_InputFilter_JPanel) &&
            (dataType.equalsIgnoreCase("DivTotal") || dataType.equalsIgnoreCase("IDivTotal") ||
            dataType.equalsIgnoreCase("RelTotal") || dataType.equalsIgnoreCase("IRelTotal")) ) {
            // This type of filter uses a DataStore
            ColoradoWaterHBGuestDataStore dataStore =
                ((ColoradoWaterHBGuest_GUI_StructureGeolocMeasType_InputFilter_JPanel)panel).getColoradoWaterHBGuestDataStore();
            if ( dataStore.getName().equalsIgnoreCase(dataStoreName) ) {
                // Have a match in the data store name so return the panel
                return panel;
            }
        }
        else if ( (panel instanceof ColoradoWaterHBGuest_GUI_GroundWaterWellsMeasType_InputFilter_JPanel) &&
            (dataType.equalsIgnoreCase("WellLevelElev") || dataType.equalsIgnoreCase("WellLevelDepth")) ) {
            // This type of filter uses a DataStore
            ColoradoWaterHBGuestDataStore dataStore =
                ((ColoradoWaterHBGuest_GUI_GroundWaterWellsMeasType_InputFilter_JPanel)panel).getColoradoWaterHBGuestDataStore();
            if ( dataStore.getName().equalsIgnoreCase(dataStoreName) ) {
                // Have a match in the data store name so return the panel
                return panel;
            }
        }
        // TODO SAM 2012-05-03 - May add input filter later
        // No input filter panel is used for ColoradoWaterSMS - only the data type is populated
        //
        else if ( panel instanceof RccAcis_TimeSeries_InputFilter_JPanel ) {
            // This type of filter uses a DataStore
            DataStore dataStore = ((RccAcis_TimeSeries_InputFilter_JPanel)panel).getDataStore();
            if ( dataStore.getName().equalsIgnoreCase(dataStoreName) ) {
                // Have a match in the data store name so return the panel
                return panel;
            }
        }
        else if ( panel instanceof ReclamationHDB_TimeSeries_InputFilter_JPanel ) {
            // This type of filter uses a DataStore
            DataStore dataStore = ((ReclamationHDB_TimeSeries_InputFilter_JPanel)panel).getDataStore();
            if ( dataStore.getName().equalsIgnoreCase(dataStoreName) ) {
                // Have a match in the data store name so return the panel
                return panel;
            }
        }
        else if ( panel instanceof RiversideDB_MeasTypeMeasLocGeoloc_InputFilter_JPanel ) {
            // This type of filter uses a DataStore
            DataStore dataStore = ((RiversideDB_MeasTypeMeasLocGeoloc_InputFilter_JPanel)panel).getDataStore();
            if ( dataStore.getName().equalsIgnoreCase(dataStoreName) ) {
                // Have a match in the data store name so return the panel
                return panel;
            }
        }
        else if ( panel instanceof UsgsNwisDaily_TimeSeries_InputFilter_JPanel ) {
            // This type of filter uses a DataStore
            DataStore dataStore = ((UsgsNwisDaily_TimeSeries_InputFilter_JPanel)panel).getDataStore();
            if ( dataStore.getName().equalsIgnoreCase(dataStoreName) ) {
                // Have a match in the data store name so return the panel
                return panel;
            }
        }
    }
    return null;
}

//FIXME SAM 2007-11-01 Need to use /tmp etc for a startup home if not
//specified so the software install home is not used.
/**
Return the initial working directory, which will be the software startup
home, or the location of new command files.
This directory is suitable for initializing a workflow processing run.
@return the initial working directory, which should always be non-null.
*/
private String ui_GetInitialWorkingDir ()
{
	return __initialWorkingDir;
}

/**
Return the NWSRFSFS5Files instance that is active for the UI, opened from
the configuration file information or the NWSRFS FS5Files select dialog.
@return the NWSRFSDMI instance that is active for the UI.
*/
private NWSRFS_DMI ui_GetNWSRFSFS5FilesDMI ()
{
	return __nwsrfs_dmi;
}

/**
Return the data store for the selected data store name.
@return the data store for the selected data store name, or null if the selected name is blank (or for some
reason is not in the processor - should not happen if data are being kept consistent).
*/
private DataStore ui_GetSelectedDataStore ()
{
    // TODO SAM 2010-09-02 How to ensure that name is unique until data stores are handled consistently?
    String dataStoreName = __dataStore_JComboBox.getSelected();
    Message.printStatus(2, "ui_GetSelectedDataStore", "Getting data store for selected name \"" +
        dataStoreName + "\"" );
    if ( (dataStoreName == null) || dataStoreName.equals("") ) {
        // No need to request from processor
        return null;
    }
    return __tsProcessor.getDataStoreForName ( dataStoreName, null );
}

/**
Return the selected data type as a short string.  For some input types, data types have additional
label-only information because the types are grouped in the GUI due to the list length.
StateCUB has a lot of types but currently they are not grouped (AND THEY MAY INCLUDE "-" so
don't do anything special).
*/
private String ui_GetSelectedDataType ()
{
    // First set the default for no special handling of data type in GUI.  This applies to many of
    // the data types, including DateValue, HEC-DSS, MODSIM, NWSCard, NWSRFS ESP trace ensemble,
    // RiverWare, StateCU, StateCUB, StateMod, StateModB (New - just use data types in files), USGS NWIS
    DataStore selectedDataStore = ui_GetSelectedDataStore();
    String selectedInputType = ui_GetSelectedInputType(); // OK if null or blank
    String selectedDataTypeFull = ui_GetSelectedDataTypeFull();  
    String selectedDataType = selectedDataTypeFull;
    // The displayed data type may not be the actual due to some annotation.
    // Check some specific input types and strip off the labels.
    if (
        ((selectedDataStore != null) && (selectedDataStore instanceof ColoradoWaterHBGuestDataStore)) ||
        selectedInputType.equals(__INPUT_TYPE_HydroBase) || selectedInputType.equals(__INPUT_TYPE_StateModB) ) {
        // Data type group is first and data type second.
        // HydroBase:  "Climate - Precip"
        // StateModB (old, group the types to help user):  "Water Supply - Total_Supply"
        if ( selectedDataTypeFull.indexOf('-') >= 0 ) {
            selectedDataType = StringUtil.getToken( selectedDataTypeFull, "-", 0, 1).trim();
            int pos = selectedDataType.indexOf(' ');
            if ( pos >= 0 ) {
                // Special case "Well - WellLevel (phasing out)"
                selectedDataType = selectedDataType.substring(0,pos).trim();
            }
        }
    }
    else if ( selectedInputType.equals(__INPUT_TYPE_MEXICO_CSMN) ||
        selectedInputType.equals(__INPUT_TYPE_NWSRFS_FS5Files)) {
        // Data type is first and explanation second, for example...
        // Mexico CSMN:   "EV - Evaporation"
        // NWSRFS FS5 Files: "MAP - Mean Areal Precipitation"
        if ( selectedDataTypeFull.indexOf('-') >= 0 ) {
            selectedDataType = StringUtil.getToken( selectedDataTypeFull, "-", 0, 0).trim();
        }
    }
    return selectedDataType;
}

/**
Return the selected data type full string, which may include a note.
*/
private String ui_GetSelectedDataTypeFull ()
{
    return __dataType_JComboBox.getSelected();
}

/**
Return the selected input name.
*/
private String ui_GetSelectedInputName()
{
    return __inputName_JComboBox.getSelected();
}

/**
Return the selected input type.  Use this to ensure that operations are based on the current setting.
*/
private String ui_GetSelectedInputType()
{
    return __input_type_JComboBox.getSelected();
}

/**
Return the selected time step.
*/
private String ui_GetSelectedTimeStep()
{
    return __timeStep_JComboBox.getSelected();
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

	__queryInput_JPanel = new JPanel();
	__queryInput_JPanel.setLayout(gbl);
	ui_SetInputPanelTitle (null, Color.black );
	
    query_JPanel.add("West", __queryInput_JPanel);
 
	y=0;
	__dataStore_JLabel = new JLabel("Data store:");
    JGUIUtil.addComponent(__queryInput_JPanel, __dataStore_JLabel, 
        0, y, 1, 1, 0.0, 0.0, insetsNLNN, GridBagConstraints.NONE, GridBagConstraints.EAST);
    __dataStore_JComboBox = new SimpleJComboBox(false);
    __dataStore_JComboBox.setMaximumRowCount ( 20 );
    String tooltip = "<html>Configured database and web service data stores - select a data store OR select input type and name below.</html>";
    __dataStore_JLabel.setToolTipText(tooltip);
    __dataStore_JComboBox.setToolTipText ( tooltip );
    __dataStore_JComboBox.addItemListener( this );
        JGUIUtil.addComponent(__queryInput_JPanel, __dataStore_JComboBox, 
        1, y++, 2, 1, 1.0, 0.0, insetsNNNR, GridBagConstraints.NONE, GridBagConstraints.WEST);

    JLabel label = new JLabel("Input type:");
    JGUIUtil.addComponent(__queryInput_JPanel, label, 
		0, y, 1, 1, 0.0, 0.0, insetsNLNN, GridBagConstraints.NONE, GridBagConstraints.EAST);
    __input_type_JComboBox = new SimpleJComboBox(false);
    __input_type_JComboBox.setMaximumRowCount ( 20 );
    tooltip = "<html>The input type is the file/database format being read - select an input type OR select a data store above.</html>";
    label.setToolTipText ( tooltip );
    __input_type_JComboBox.setToolTipText ( tooltip );
	__input_type_JComboBox.addItemListener( this );
        JGUIUtil.addComponent(__queryInput_JPanel, __input_type_JComboBox, 
		1, y++, 2, 1, 1.0, 0.0, insetsNNNR, GridBagConstraints.NONE, GridBagConstraints.WEST);

    __inputName_JLabel = new JLabel("Input name:" );
    JGUIUtil.addComponent(__queryInput_JPanel, __inputName_JLabel, 
		0, y, 1, 1, 0.0, 0.0, insetsNLNN, GridBagConstraints.NONE, GridBagConstraints.EAST);
    __inputName_JComboBox = new SimpleJComboBox(false);
    __inputName_JComboBox.setMaximumRowCount ( 20 );
    // Set a blank entry to work with data store handling
    __inputName_JComboBox.add ( "" );
    tooltip = "<html>The input name is the name of the file or database" +
    " being read.<br>It will default or be prompted for after selecting the input type.</html>";
    label.setToolTipText ( tooltip );
    __inputName_JComboBox.setToolTipText ( tooltip );
	__inputName_JComboBox.addItemListener( this );
        JGUIUtil.addComponent(__queryInput_JPanel, __inputName_JComboBox, 
		1, y++, 2, 1, 1.0, 0.0, insetsNNNR, GridBagConstraints.NONE, GridBagConstraints.WEST);

    label = new JLabel("Data type:");
    JGUIUtil.addComponent(__queryInput_JPanel, label, 
		0, y, 1, 1, 0.0, 0.0, insetsNLNN, GridBagConstraints.NONE, GridBagConstraints.EAST);
	__dataType_JComboBox = new SimpleJComboBox(false);
    __dataType_JComboBox.setMaximumRowCount ( 20 );
    tooltip = "<html>The data type is used to filter the list of time series.</html>";
    label.setToolTipText ( tooltip );
	__dataType_JComboBox.setToolTipText ( tooltip );
	__dataType_JComboBox.addItemListener( this );
    JGUIUtil.addComponent(__queryInput_JPanel, __dataType_JComboBox, 
		1, y++, 2, 1, 1.0, 0.0, insetsNNNR, GridBagConstraints.NONE, GridBagConstraints.WEST);

    label = new JLabel("Time step:");
    JGUIUtil.addComponent(__queryInput_JPanel, label, 
		0, y, 1, 1, 0.0, 0.0, insetsNLNN, GridBagConstraints.NONE, GridBagConstraints.EAST);
    __timeStep_JComboBox = new SimpleJComboBox(false);
    tooltip = "<html>The time step is used to filter the list of time series.</html>";
    label.setToolTipText ( tooltip );
	__timeStep_JComboBox.setToolTipText ( tooltip );
	__timeStep_JComboBox.addItemListener( this );
    JGUIUtil.addComponent(__queryInput_JPanel, __timeStep_JComboBox, 
		1, y++, 2, 1, 1.0, 0.0, insetsNNNR, GridBagConstraints.NONE, GridBagConstraints.WEST);

	// Save the position for the input filters, which will be added after database connections are made...

	__inputFilterY = y;
	++y;	// Increment for GUI features below.

	// Force the choices to assume some reasonable values...

	ui_SetInputTypeChoices();

    __get_ts_list_JButton = new SimpleJButton(BUTTON_TOP_GET_TIME_SERIES,this);
	__get_ts_list_JButton.setToolTipText (
		"<html>Get a list of time series but not the full time " +
		"series data.<br>Time series can then be selected for processing.</html>" );
    JGUIUtil.addComponent(__queryInput_JPanel, __get_ts_list_JButton, 
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
		"<html>Copy selected time series from above to the Commands list as time series identifiers.<br/>" +
		"Insert AFTER last selected command or at end if no commands are selected.</html>" );
    JGUIUtil.addComponent ( __query_results_JPanel,	__CopySelectedToCommands_JButton, 
		1, y, 1, 1, 0.0, 0.0, insetsNNNR, GridBagConstraints.NONE, GridBagConstraints.EAST );
    __CopyAllToCommands_JButton = new SimpleJButton( BUTTON_TOP_COPY_ALL_TO_COMMANDS,this);
	__CopyAllToCommands_JButton.setToolTipText (
		"<html>Copy all time series from above to the Commands list as time series identifiers.<br />" +
		"Insert AFTER last selected command or at end if no commands are selected.</html>" );
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
	__tsProcessor = new TSCommandProcessor();
	ui_SetInitialWorkingDir( System.getProperty("user.dir") );
	commandProcessor_SetInitialWorkingDir ( ui_GetInitialWorkingDir(), true );
	// FIXME SAM 2007-08-28 Need to set a WindowListener for -nomaingui calls?
	//__ts_processor.setTSCommandProcessorUI ( this );
	__tsProcessor.addCommandProcessorListener ( this );
	__commands_JListModel = new TSCommandProcessorListModel(__tsProcessor);
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
		"<html>Run selected commands from above to generate time " +
		"series,<br>which will be shown in the Results - Time Series tab below.</html>" );
	JGUIUtil.addComponent(__commands_JPanel, __Run_SelectedCommands_JButton,
		5, 5, 1, 1, 0.0, 0.0, insetsTLNR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	__Run_AllCommands_JButton =
		new SimpleJButton(__Button_RunAllCommands_String, __Run_AllCommandsCreateOutput_String, this);
	__Run_AllCommands_JButton.setToolTipText (
		"<html>Run all commands from above to generate time series," +
		"<br>which will be shown in the Results - Time Series tab below.</html>" );
	JGUIUtil.addComponent(__commands_JPanel, __Run_AllCommands_JButton,
		6, 5, 1, 1, 0.0, 0.0, insetsTLNR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	// Put on right because we want it to be a decision to clear...
	__ClearCommands_JButton = new SimpleJButton(__Button_ClearCommands_String, this);
	__ClearCommands_JButton.setToolTipText (
		"<html>Clear selected commands from above.<br>Clear all commands if none are selected.</html>" );
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

    __resultsTSEnsembles_JPanel = new JPanel();
    __resultsTSEnsembles_JPanel.setLayout(gbl);
    /*
    __results_tsensembles_JPanel.setBorder(
        BorderFactory.createTitledBorder (
        BorderFactory.createLineBorder(Color.black),
        "Results: Ensembles of Time Series" ));
        */
    __resultsTSEnsembles_JListModel = new DefaultListModel();
    __resultsTSEnsembles_JList = new JList ( __resultsTSEnsembles_JListModel );
    __resultsTSEnsembles_JList.setSelectionMode ( ListSelectionModel.SINGLE_SELECTION );
    __resultsTSEnsembles_JList.addKeyListener ( this );
    __resultsTSEnsembles_JList.addListSelectionListener ( this );
    __resultsTSEnsembles_JList.addMouseListener ( this );
    JScrollPane results_tsensembles_JScrollPane = new JScrollPane ( __resultsTSEnsembles_JList );
    JGUIUtil.addComponent(__resultsTSEnsembles_JPanel, results_tsensembles_JScrollPane, 
        0, 15, 8, 5, 1.0, 1.0, insetsNLNR, GridBagConstraints.BOTH, GridBagConstraints.WEST);
    __results_JTabbedPane.addTab ( "Ensembles", __resultsTSEnsembles_JPanel );
    
    // Results: output files...

    JPanel results_files_JPanel = new JPanel();
    results_files_JPanel.setLayout(gbl);
    JGUIUtil.addComponent(center_JPanel, results_files_JPanel,
        0, 3, 1, 1, 1.0, 0.0, insetsNNNN, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER);
    JGUIUtil.addComponent(results_files_JPanel, new JLabel ("Output files:"),
        0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NONE, GridBagConstraints.EAST);
    __resultsOutputFiles_JListModel = new DefaultListModel();
    __resultsOutputFiles_JList = new JList ( __resultsOutputFiles_JListModel );
    __resultsOutputFiles_JList.setSelectionMode ( ListSelectionModel.SINGLE_SELECTION );
    __resultsOutputFiles_JList.addKeyListener ( this );
    __resultsOutputFiles_JList.addListSelectionListener ( this );
    __resultsOutputFiles_JList.addMouseListener ( this );
    JGUIUtil.addComponent(results_files_JPanel, new JScrollPane ( __resultsOutputFiles_JList ), 
        0, 15, 8, 5, 1.0, 1.0, insetsNLNR, GridBagConstraints.BOTH, GridBagConstraints.WEST);
    __results_JTabbedPane.addTab ( "Output Files", results_files_JPanel );
    
    // Results - problems...

    JPanel results_problems_JPanel = new JPanel();
    results_problems_JPanel.setLayout(gbl);
    CommandLog_TableModel tableModel = null;
    try {
        tableModel = new CommandLog_TableModel(new Vector());
    }
    catch ( Exception e ) {
        // Should not happen but log
        Message.printWarning ( 3, routine, e );
        Message.printWarning(3, routine, "Error creating table model for problem display.");
        throw new RuntimeException ( e );
    }
    CommandLog_CellRenderer cellRenderer = new CommandLog_CellRenderer(tableModel);
    PropList commandStatusProps = new PropList ( "Problems" );
    commandStatusProps.add("JWorksheet.ShowRowHeader=true");
    commandStatusProps.add("JWorksheet.AllowCopy=true");
    // Initialize with null table model since no initial data
    JScrollWorksheet sjw = new JScrollWorksheet ( cellRenderer, tableModel, commandStatusProps );
    __resultsProblems_JWorksheet = sjw.getJWorksheet ();
    __resultsProblems_JWorksheet.setColumnWidths (cellRenderer.getColumnWidths(), getGraphics() );
    __resultsProblems_JWorksheet.setPreferredScrollableViewportSize(null);
    // Listen for mouse events to ??...
    //__problems_JWorksheet.addMouseListener ( this );
    //__problems_JWorksheet.addJWorksheetListener ( this );
    JGUIUtil.addComponent(results_problems_JPanel, sjw, 
        0, 0, 8, 5, 1.0, 1.0, insetsNLNR, GridBagConstraints.BOTH, GridBagConstraints.WEST);
    __results_JTabbedPane.addTab ( "Problems", results_problems_JPanel );
    
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
    __resultsTables_JListModel = new DefaultListModel();
    __resultsTables_JList = new JList ( __resultsTables_JListModel );
    __resultsTables_JList.setSelectionMode ( ListSelectionModel.SINGLE_SELECTION );
    __resultsTables_JList.addKeyListener ( this );
    __resultsTables_JList.addListSelectionListener ( this );
    __resultsTables_JList.addMouseListener ( this );
    JGUIUtil.addComponent(results_tables_JPanel, new JScrollPane ( __resultsTables_JList ), 
        0, 15, 8, 5, 1.0, 1.0, insetsNLNR, GridBagConstraints.BOTH, GridBagConstraints.WEST);
    __results_JTabbedPane.addTab ( "Tables", results_tables_JPanel );
    
	// Results: time series...

    __resultsTS_JPanel = new JPanel();
    __resultsTS_JPanel.setLayout(gbl);
    /*
	__results_ts_JPanel.setBorder(
		BorderFactory.createTitledBorder (
		BorderFactory.createLineBorder(Color.black),
		"Results: Time Series" ));
        */
	//JGUIUtil.addComponent(center_JPanel, __ts_JPanel,
	//	0, 1, 1, 1, 1.0, 1.0, insetsNNNN, GridBagConstraints.BOTH, GridBagConstraints.CENTER);

	__resultsTS_JListModel = new DefaultListModel();
	__resultsTS_JList = new JList ( __resultsTS_JListModel );
	__resultsTS_JList.addKeyListener ( this );
	__resultsTS_JList.addListSelectionListener ( this );
	__resultsTS_JList.addMouseListener ( this );
	JScrollPane results_ts_JScrollPane = new JScrollPane ( __resultsTS_JList );
	JGUIUtil.addComponent(__resultsTS_JPanel, results_ts_JScrollPane, 
		0, 15, 8, 5, 1.0, 1.0, insetsNLNR, GridBagConstraints.BOTH, GridBagConstraints.WEST);
    __results_JTabbedPane.addTab ( "Time Series", __resultsTS_JPanel );
    
    // Results: views...

    __resultsViews_JPanel = new JPanel();
    __resultsViews_JPanel.setLayout(gbl);
    JGUIUtil.addComponent(center_JPanel, __resultsViews_JPanel,
        0, 3, 1, 1, 1.0, 0.0, insetsNNNN, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER);
    __resultsViews_JTabbedPane = new JTabbedPane();
    JGUIUtil.addComponent(__resultsViews_JPanel, __resultsViews_JTabbedPane, 
        0, 0, 1, 1, 1.0, 1.0, insetsNLNR, GridBagConstraints.BOTH, GridBagConstraints.WEST);
    __results_JTabbedPane.addTab ( "Views", __resultsViews_JPanel );
    
    // Default to time series selected since that is what most people have seen with previous
    // TSTool versions...
    
    __results_JTabbedPane.setSelectedComponent ( __resultsTS_JPanel );
	
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
	setSize ( 900, 750 );
	JGUIUtil.center ( this );
	// TODO SAM 2008-01-11 Need to evaluate whether server mode controls the GUI or only a command processor
	if ( !TSToolMain.isRestletServer() ) {
       	setVisible ( true );
	}
	}
	catch ( Exception e ) {
	    Message.printWarning ( 2, routine, "Error initializing UI (" + e + ")." );
		Message.printWarning ( 2, routine, e );
	}
	__guiInitialized = true;
	// Select an input type to get the UI to a usable initial state.
	if ( ui_GetHydroBaseDataStore() != null ) {
		// Select HydroBase for CDSS use...
		__input_type_JComboBox.select( null );
		__input_type_JComboBox.select( __INPUT_TYPE_HydroBase );
	}
	else {
        __input_type_JComboBox.select( null );
		__input_type_JComboBox.select( __INPUT_TYPE_DateValue );
	}
}

/**
Initialize the input filters.  An input filter is defined and added for each
enabled input type but only one is set visible at a time.  Later, as an input
type is selected, the appropriate input filter is made visible.
This method is called at GUI startup.  Individual helper methods can be called as necessary to reset the
filters for a specific input time (e.g., when File...Open...HydroBase is used).
@param y layout position to add the input filters.
*/
private void ui_InitGUIInputFilters ( final int y )
{	// Define and add specific input filters...
    Message.printStatus ( 2, "ui_InitGUIInputFilters", "Initializing input filters." );
    Runnable r = new Runnable() {
        public void run() {
        	String routine = "TSTool_JFrame.ui_InitGUIInputFilters";
            ui_SetInputPanelTitle ("Initializing Input/Query Options...", Color.red );
        	int buffer = 3;
        	Insets insets = new Insets(0,buffer,0,0);
        	// Remove all the current input filters...
        	for ( int i = 0; i < __inputFilterJPanelList.size(); i++ ) {
                __queryInput_JPanel.remove( (Component)__inputFilterJPanelList.get(i));
        	}
        	__inputFilterJPanelList.clear();
        	// Now add the input filters for input types that are enabled, all on top of each other
            if ( __source_ColoradoWaterHBGuest_enabled &&
                (__tsProcessor.getDataStoresByType(ColoradoWaterHBGuestDataStore.class).size() > 0) ) {
                try {
                    ui_InitGUIInputFiltersColoradoWaterHBGuest(
                        __tsProcessor.getDataStoresByType(ColoradoWaterHBGuestDataStore.class), y );
                }
                catch ( Throwable e ) {
                    // This may happen if the web service cannot initialize.
                    Message.printWarning(1, routine,
                        "Error initializing ColoradoWaterHBGuest input filters (" + e + ").");
                    Message.printWarning(3, routine, e);
                }
            }
            if ( __source_HECDSS_enabled ) {
                // Add input filters for HEC-DSS files.
                try {
                    // Open with 5 lists to show all HEC-DSS parts except D
                    __inputFilterHecDss_JPanel = new HecDssTSInputFilter_JPanel ( 5 );
                    // For debugging...
                    //__inputFilterHecDss_JPanel.setBackground(Color.red);
                    JGUIUtil.addComponent(__queryInput_JPanel, __inputFilterHecDss_JPanel,
                        0, y, 3, 1, 1.0, 0.0, insets, GridBagConstraints.HORIZONTAL,
                        GridBagConstraints.WEST );
                    __inputFilterJPanelList.add ( __inputFilterHecDss_JPanel );
                }
                catch ( Throwable e ) {
                    Message.printWarning ( 3, routine, "Unable to initialize input filter for HEC-DSS file.");
                    Message.printWarning ( 3, routine, e );
                }
            }
        	if ( __source_HydroBase_enabled && (ui_GetHydroBaseDataStore() != null) ) {
        	    try {
        	        ui_InitGUIInputFiltersHydroBase(ui_GetHydroBaseDataStore(), y );
        	    }
                catch ( Throwable e ) {
                    // This may happen if the web service static code cannot initialize.  Just catch
                    // and let a blank panel be used for input filters.
                    Message.printWarning(3, routine, "Error initializing HydroBase input filters (" + e + ").");
                    Message.printWarning(3, routine, e);
                }
        	}
            if ( __source_HydroBase_enabled && (__tsProcessor.getDataStoresByType(HydroBaseDataStore.class).size() > 0) ) {
                try {
                    ui_InitGUIInputFiltersHydroBase(__tsProcessor.getDataStoresByType(HydroBaseDataStore.class), y );
                }
                catch ( Throwable e ) {
                    // This may happen if the database is unavailable or inconsistent with expected design.
                    Message.printWarning(3, routine, "Error initializing HydroBase data store input filters (" + e + ").");
                    Message.printWarning(3, routine, e);
                }
            }
        	if ( __source_MexicoCSMN_enabled ) {
                List<InputFilter> input_filters = null;
                InputFilter filter = null;
        		// Add input filters using text fields...
        		// Later may put this code in the MexicoCSMN package since it may be used by other interfaces...
        		input_filters = new Vector(2);
        		List<String> statenum_Vector = new Vector (32);
        		statenum_Vector.add ( "01 - Aguascalientes" );
        		statenum_Vector.add ( "02 - Baja California" );
        		statenum_Vector.add ( "03 - Baja California Sur" );
        		statenum_Vector.add ( "04 - Campeche" );
        		statenum_Vector.add ( "05 - Coahuila" );
        		statenum_Vector.add ( "06 - Colima" );
        		statenum_Vector.add ( "07 - Chiapas" );
        		statenum_Vector.add ( "08 - Chihuahua" );
        		statenum_Vector.add ( "09 - Distrito Federal" );
        		statenum_Vector.add ( "10 - Durango" );
        		statenum_Vector.add ( "11 - Guanajuato" );
        		statenum_Vector.add ( "12 - Guerrero" );
        		statenum_Vector.add ( "13 - Hidalgo" );
        		statenum_Vector.add ( "14 - Jalisco" );
        		statenum_Vector.add ( "15 - Mexico" );
        		statenum_Vector.add ( "16 - Michoacan" );
        		statenum_Vector.add ( "17 - Morelos" );
        		statenum_Vector.add ( "18 - Nayarit" );
        		statenum_Vector.add ( "19 - Nuevo Leon" );
        		statenum_Vector.add ( "20 - Oaxaca" );
        		statenum_Vector.add ( "21 - Puebla" );
        		statenum_Vector.add ( "22 - Queretaro" );
        		statenum_Vector.add ( "23 - Quintana Roo" );
        		statenum_Vector.add ( "24 - San Luis Potosi" );
        		statenum_Vector.add ( "25 - Sinaloa" );
        		statenum_Vector.add ( "26 - Sonora" );
        		statenum_Vector.add ( "27 - Tabasco" );
        		statenum_Vector.add ( "28 - Tamaulipas" );
        		statenum_Vector.add ( "29 - Tlaxcala" );
        		statenum_Vector.add ( "30 - Veracruz" );
        		statenum_Vector.add ( "31 - Yucatan" );
        		statenum_Vector.add ( "32 - Zacatecas" );
        		input_filters.add ( new InputFilter (
        			"", "",
        			StringUtil.TYPE_STRING,
        			null, null, true ) );	// Blank to disable filter
        		input_filters.add ( new InputFilter ( "Station Name", "Station Name",
        			StringUtil.TYPE_STRING,
        			null, null, true ) );
        		filter = new InputFilter ( "State Number", "Station Number",
        			StringUtil.TYPE_INTEGER,
        			statenum_Vector, statenum_Vector, true );
        		filter.setTokenInfo("-",0);
        		input_filters.add ( filter );
                JGUIUtil.addComponent(__queryInput_JPanel, __inputFilterMexicoCSMN_JPanel =
        			new InputFilter_JPanel ( input_filters, 2, -1 ), 
        			0, y, 3, 1, 1.0, 0.0, insets, GridBagConstraints.HORIZONTAL,
        			GridBagConstraints.WEST );
        		__inputFilterMexicoCSMN_JPanel.setToolTipText (
        			"<html>Mexico CSMN queries can be filtered<br>based on station data.</html>" );
        		__inputFilterMexicoCSMN_JPanel.setName ( "MexicoCSMN.InputFilterPanel" );
        		__inputFilterJPanelList.add ( __inputFilterMexicoCSMN_JPanel );
        	}
         	if ( __source_NWSRFS_FS5Files_enabled ) {
        		// Add input filters for NWSRFS FS5 files.
        		try {
        		    __inputFilterNWSRFSFS5Files_JPanel = new NWSRFS_TS_InputFilter_JPanel ();
                		JGUIUtil.addComponent(__queryInput_JPanel, __inputFilterNWSRFSFS5Files_JPanel,
        				0, y, 3, 1, 1.0, 0.0, insets, GridBagConstraints.HORIZONTAL,
        				GridBagConstraints.WEST );
                	__inputFilterNWSRFSFS5Files_JPanel.setName ( "NWSRFSFS5Files.InputFilterPanel" );
        			__inputFilterJPanelList.add ( __inputFilterNWSRFSFS5Files_JPanel );
        		}
        		catch ( Throwable e ) {
        			Message.printWarning ( 2, routine, "Unable to initialize input filter for NWSRFS FS5Files.");
        			Message.printWarning ( 2, routine, e );
        		}
        	}
            if ( __source_RCCACIS_enabled && (__tsProcessor.getDataStoresByType(RccAcisDataStore.class).size() > 0) ) {
                try {
                    ui_InitGUIInputFiltersRccAcis(__tsProcessor.getDataStoresByType(RccAcisDataStore.class), y );
                }
                catch ( Throwable e ) {
                    // This may happen if the database is unavailable or inconsistent with expected design.
                    Message.printWarning(3, routine, "Error initializing RCC ACIS data store input filters (" + e + ").");
                    Message.printWarning(3, routine, e);
                }
            }
            if ( __source_ReclamationHDB_enabled && (__tsProcessor.getDataStoresByType(ReclamationHDBDataStore.class).size() > 0) ) {
                try {
                    ui_InitGUIInputFiltersReclamationHDB(__tsProcessor.getDataStoresByType(ReclamationHDBDataStore.class), y );
                }
                catch ( Throwable e ) {
                    // This may happen if the database is unavailable or inconsistent with expected design.
                    Message.printWarning(3, routine, "Error initializing Reclamation HDB data store input filters (" + e + ").");
                    Message.printWarning(3, routine, e);
                }
            }
            if ( __source_RiversideDB_enabled && (__tsProcessor.getDataStoresByType(RiversideDBDataStore.class).size() > 0) ) {
                try {
                    ui_InitGUIInputFiltersRiversideDB(__tsProcessor.getDataStoresByType(RiversideDBDataStore.class), y );
                }
                catch ( Throwable e ) {
                    // This may happen if the database is unavailable or inconsistent with expected design.
                    Message.printWarning(3, routine, "Error initializing RiversideDB data store input filters (" + e + ").");
                    Message.printWarning(3, routine, e);
                }
            }
            if ( __source_UsgsNwisDaily_enabled && (__tsProcessor.getDataStoresByType(UsgsNwisDailyDataStore.class).size() > 0) ) {
                try {
                    ui_InitGUIInputFiltersUsgsNwisDaily(
                        __tsProcessor.getDataStoresByType(UsgsNwisDailyDataStore.class), y );
                }
                catch ( Throwable e ) {
                    // This may happen if the database is unavailable or inconsistent with expected design.
                    Message.printWarning(3, routine, "Error initializing USGS NWIS daily data store input filters (" + e + ").");
                    Message.printWarning(3, routine, e);
                }
            }
        
        	// Always add a generic input filter JPanel that is shared by input
        	// types that do not have filter capabilities and when database connections are not set up...
        
           	JGUIUtil.addComponent(__queryInput_JPanel, __inputFilterGeneric_JPanel = new InputFilter_JPanel (),
    			0, y, 3, 1, 1.0, 0.0, insets, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST );
        	__inputFilterGeneric_JPanel.setToolTipText ( "The selected input type does not support input filters." );
        	__inputFilterGeneric_JPanel.setName( "GenericDefault.InputFilterPanel" );
        	__inputFilterJPanelList.add ( __inputFilterGeneric_JPanel );
        	
            // Always add a message input filter JPanel that can be used to display a message, such as
        	// an instruction that a database connection is not available...
        
            JGUIUtil.addComponent(__queryInput_JPanel, __inputFilterMessage_JPanel = new InputFilter_JPanel (""),
                0, y, 3, 1, 1.0, 0.0, insets, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST );
            __inputFilterMessage_JPanel.setName ( "Message.InputFilterPanel" );
            __inputFilterJPanelList.add ( __inputFilterMessage_JPanel );
            
        	// The appropriate JPanel will be set visible later based on the input type that is selected.
        	// Because a component is added to the original GUI, need to refresh the GUI layout...
        	ui_SetInputFilters();
            ui_SetInputPanelTitle (null, Color.black );
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
Initialize the ColoradoWaterHBGuest input filter.
*/
private void ui_InitGUIInputFiltersColoradoWaterHBGuest ( List<DataStore> dataStoreList, int y )
{   String routine = getClass().getName() + ".ui_InitGUIInputFiltersColoradoWaterHBGuest";
    int buffer = 3;
    Insets insets = new Insets(0,buffer,0,0);
    
    Message.printStatus(2, routine, "Initializing ColoradoWaterHBGuest input filters.");
    String selectedDataType = ui_GetSelectedDataType();
    for ( DataStore dataStore: dataStoreList ) {
        while ( true ) {
            // Try to find existing input filter panels for the same name (may be more than one
            // given that have stations, structures, etc.)...
            JPanel ifp = ui_GetInputFilterPanelForDataStoreName ( dataStore.getName(), selectedDataType );
            // If the previous instance is not null, remove it from the list...
            if ( ifp != null ) {
                __inputFilterJPanelList.remove ( ifp );
            }
            else {
                // No more filters matching the data store
                break;
            }
        }
        ColoradoWaterHBGuestDataStore cwds = (ColoradoWaterHBGuestDataStore)dataStore;
        
        // If the an instance of a panel is not null, remove it from the list and then recreate it.
    
        // Add input filters for stations, for historical data (since ColoradoWaterSMS provides access
        // to real-time data)...
        
        try {
            if ( __inputFilterColoradoWaterHBGuestStationGeolocMeasType_JPanel != null ) {
                __inputFilterJPanelList.remove ( __inputFilterColoradoWaterHBGuestStationGeolocMeasType_JPanel );
            }
            __inputFilterColoradoWaterHBGuestStationGeolocMeasType_JPanel = new
                ColoradoWaterHBGuest_GUI_StationGeolocMeasType_InputFilter_JPanel( cwds );
            JGUIUtil.addComponent(__queryInput_JPanel, __inputFilterColoradoWaterHBGuestStationGeolocMeasType_JPanel,
                0, y, 3, 1, 1.0, 0.0, insets, GridBagConstraints.HORIZONTAL,
                GridBagConstraints.WEST );
            __inputFilterColoradoWaterHBGuestStationGeolocMeasType_JPanel.setName ( "ColoradoWaterHBGuest.StationInputFilterPanel");
            __inputFilterJPanelList.add ( __inputFilterColoradoWaterHBGuestStationGeolocMeasType_JPanel );
        }
        catch ( Exception e ) {
            Message.printWarning ( 2, routine, "Unable to initialize input filter for HydroBase stations." );
            Message.printWarning ( 2, routine, e );
        }
        
        // Add input filters for structures - there is one panel for
        // "total" time series and one for water class time series that can be filtered by SFUT...
        
        try {
            if ( __inputFilterColoradoWaterHBGuestStructureGeolocMeasType_JPanel != null ) {
                __inputFilterJPanelList.remove ( __inputFilterColoradoWaterHBGuestStructureGeolocMeasType_JPanel );
            }
            __inputFilterColoradoWaterHBGuestStructureGeolocMeasType_JPanel = new
                ColoradoWaterHBGuest_GUI_StructureGeolocMeasType_InputFilter_JPanel( cwds, false );
            JGUIUtil.addComponent(__queryInput_JPanel, __inputFilterColoradoWaterHBGuestStructureGeolocMeasType_JPanel,
                0, y, 3, 1, 1.0, 0.0, insets, GridBagConstraints.HORIZONTAL,
                GridBagConstraints.WEST );
            __inputFilterColoradoWaterHBGuestStructureGeolocMeasType_JPanel.setName ( "ColoradoWaterHBGuest.StructureInputFilterPanel");
            __inputFilterJPanelList.add ( __inputFilterColoradoWaterHBGuestStructureGeolocMeasType_JPanel );
        }
        catch ( Exception e ) {
            Message.printWarning ( 2, routine, "Unable to initialize input filter for ColoradoWaterHBGuest structures." );
            Message.printWarning ( 3, routine, e );
        }
        
        /*
        try {
            if ( __inputFilterHydroBaseStructureSfut_JPanel != null ) {
                __inputFilterJPanelList.remove ( __inputFilterHydroBaseStructureSfut_JPanel);
            }
            __inputFilterHydroBaseStructureSfut_JPanel = new
            HydroBase_GUI_StructureGeolocStructMeasType_InputFilter_JPanel( __hbdmi, true );
            JGUIUtil.addComponent(__queryInput_JPanel, __inputFilterHydroBaseStructureSfut_JPanel,
                0, y, 3, 1, 1.0, 0.0, insets, GridBagConstraints.HORIZONTAL,
                GridBagConstraints.WEST );
            __inputFilterJPanelList.add ( __inputFilterHydroBaseStructureSfut_JPanel);
        }
        catch ( Exception e ) {
            Message.printWarning ( 2, routine,
            "Unable to initialize input filter for HydroBase structures with SFUT." );
            Message.printWarning ( 2, routine, e );
        }
        */
        
        // Add input filters for structure irrig_summary_ts,
        
        /*
        try {
            if ( __inputFilterHydroBaseIrrigts_JPanel != null ) {
                __inputFilterJPanelList.remove ( __inputFilterHydroBaseIrrigts_JPanel );
            }
            __inputFilterHydroBaseIrrigts_JPanel = new
            HydroBase_GUI_StructureIrrigSummaryTS_InputFilter_JPanel( __hbdmi );
            JGUIUtil.addComponent(__queryInput_JPanel, __inputFilterHydroBaseIrrigts_JPanel,
                0, y, 3, 1, 1.0, 0.0, insets, GridBagConstraints.HORIZONTAL,
                GridBagConstraints.WEST );
            __inputFilterJPanelList.add ( __inputFilterHydroBaseIrrigts_JPanel );
        }
        catch ( Exception e ) {
            Message.printWarning ( 2, routine,
            "Unable to initialize input filter for HydroBase irrigation summary time series - old database?" );
            Message.printWarning ( 2, routine, e );
        }
        */
        
        // Add input filters for CASS agricultural crop statistics,
        // only available for newer databases.  For now, just catch an exception when not supported.
        
        /*
        try {
            if ( __inputFilterHydroBaseCASSCropStats_JPanel != null ) {
                __inputFilterJPanelList.remove ( __inputFilterHydroBaseCASSCropStats_JPanel );
            }
            __inputFilterHydroBaseCASSCropStats_JPanel = new
            HydroBase_GUI_AgriculturalCASSCropStats_InputFilter_JPanel ( __hbdmi );
            JGUIUtil.addComponent(__queryInput_JPanel, __inputFilterHydroBaseCASSCropStats_JPanel,
                0, y, 3, 1, 1.0, 0.0, insets, GridBagConstraints.HORIZONTAL,
                GridBagConstraints.WEST );
            __inputFilterJPanelList.add ( __inputFilterHydroBaseCASSCropStats_JPanel );
        }
        catch ( Exception e ) {
            // Agricultural_CASS_crop_stats probably not in HydroBase...
            Message.printWarning ( 2, routine,
            "Unable to initialize input filter for HydroBase CASS crop statistics - old database?" );
            Message.printWarning ( 2, routine, e );
        }
        */
        
        // Add input filters for CASS agricultural livestock statistics,
        // only available for newer databases.  For now, just catch an
        // exception when not supported.
        
        /*
        try {
            if ( __inputFilterHydroBaseCASSLivestockStats_JPanel != null ) {
                __inputFilterJPanelList.remove ( __inputFilterHydroBaseCASSLivestockStats_JPanel );
            }
            __inputFilterHydroBaseCASSLivestockStats_JPanel = new
            HydroBase_GUI_AgriculturalCASSLivestockStats_InputFilter_JPanel ( __hbdmi );
            JGUIUtil.addComponent(__queryInput_JPanel, __inputFilterHydroBaseCASSLivestockStats_JPanel,
                0, y, 3, 1, 1.0, 0.0, insets, GridBagConstraints.HORIZONTAL,
                GridBagConstraints.WEST );
            __inputFilterJPanelList.add ( __inputFilterHydroBaseCASSLivestockStats_JPanel );
        }
        catch ( Exception e ) {
            // Agricultural_CASS_livestock_stats probably not in HydroBase...
            Message.printWarning ( 2, routine,
            "Unable to initialize input filter for HydroBase CASS livestock statistics - old database?" );
            Message.printWarning ( 2, routine, e );
        }
        */
        
        // Add input filters for CU population data, only available for
        // newer databases.  For now, just catch an exception when not supported.
        
        /*
        try {
            if ( __inputFilterHydroBaseCUPopulation_JPanel != null ) {
                __inputFilterJPanelList.remove ( __inputFilterHydroBaseCUPopulation_JPanel );
            }
            __inputFilterHydroBaseCUPopulation_JPanel = new
            HydroBase_GUI_CUPopulation_InputFilter_JPanel( __hbdmi);
            JGUIUtil.addComponent(__queryInput_JPanel, __inputFilterHydroBaseCUPopulation_JPanel,
                0, y, 3, 1, 1.0, 0.0, insets, GridBagConstraints.HORIZONTAL,
                GridBagConstraints.WEST );
            __inputFilterJPanelList.add ( __inputFilterHydroBaseCUPopulation_JPanel );
        }
        catch ( Exception e ) {
            // CUPopulation probably not in HydroBase...
            Message.printWarning ( 2, routine,
            "Unable to initialize input filter for HydroBase CU population data - old database?" );
            Message.printWarning ( 2, routine, e );
        }
        */
        
        // Add input filters for NASS agricultural statistics, only
        // available for newer databases.  For now, just catch an exception when not supported.
        
        /*
        try {
            if ( __inputFilterHydroBaseNASS_JPanel != null ) {
                __inputFilterJPanelList.remove ( __inputFilterHydroBaseNASS_JPanel );
            }
            __inputFilterHydroBaseNASS_JPanel = new
            HydroBase_GUI_AgriculturalNASSCropStats_InputFilter_JPanel ( __hbdmi );
            JGUIUtil.addComponent(__queryInput_JPanel, __inputFilterHydroBaseNASS_JPanel,
                0, y, 3, 1, 1.0, 0.0, insets, GridBagConstraints.HORIZONTAL,
                GridBagConstraints.WEST );
            __inputFilterJPanelList.add ( __inputFilterHydroBaseNASS_JPanel );
        }
        catch ( Exception e ) {
            // Agricultural_NASS_crop_stats probably not in HydroBase...
            Message.printWarning ( 2, routine,
            "Unable to initialize input filter for HydroBase agricultural_NASS_crop_stats - old database?" );
            Message.printWarning ( 2, routine, e );
        }
        */
        
        // Add input filters for WIS.  For now, just catch an exception when not supported.
        
        /*
        try {
            if ( __inputFilterHydroBaseWIS_JPanel != null ) {
                __inputFilterJPanelList.remove ( __inputFilterHydroBaseWIS_JPanel );
            }
            __inputFilterHydroBaseWIS_JPanel = new
            HydroBase_GUI_SheetNameWISFormat_InputFilter_JPanel ( __hbdmi );
            JGUIUtil.addComponent(__queryInput_JPanel, __inputFilterHydroBaseWIS_JPanel,
                0, y, 3, 1, 1.0, 0.0, insets, GridBagConstraints.HORIZONTAL,
                GridBagConstraints.WEST );
            __inputFilterJPanelList.add ( __inputFilterHydroBaseWIS_JPanel );
        }
        catch ( Exception e ) {
            // WIS tables probably not in HydroBase...
            Message.printWarning ( 2, routine,
            "Unable to initialize input filter for HydroBase WIS - data tables not in database?" );
            Message.printWarning ( 2, routine, e );
        }
        */
        
        // Groundwater wells
        
        try {
            if ( __inputFilterColoradoWaterHBGuestGroundWaterWellsMeasType_JPanel != null ) {
                __inputFilterJPanelList.remove ( __inputFilterColoradoWaterHBGuestGroundWaterWellsMeasType_JPanel );
            }
            __inputFilterColoradoWaterHBGuestGroundWaterWellsMeasType_JPanel = new
                ColoradoWaterHBGuest_GUI_GroundWaterWellsMeasType_InputFilter_JPanel( cwds );
            JGUIUtil.addComponent(__queryInput_JPanel,
                __inputFilterColoradoWaterHBGuestGroundWaterWellsMeasType_JPanel,
                0, y, 3, 1, 1.0, 0.0, insets, GridBagConstraints.HORIZONTAL,
                GridBagConstraints.WEST );
            __inputFilterColoradoWaterHBGuestGroundWaterWellsMeasType_JPanel.setName (
                "ColoradoWaterHBGuest.GroundWaterWellsInputFilterPanel");
            __inputFilterJPanelList.add ( __inputFilterColoradoWaterHBGuestGroundWaterWellsMeasType_JPanel );
        }
        catch ( Exception e ) {
            Message.printWarning ( 2, routine, "Unable to initialize input filter for ColoradoWaterHBGuest structures." );
            Message.printWarning ( 3, routine, e );
        }
    }
}

/**
Initialize the HydroBase input filter (may be called at startup after login or File...Open HydroBase).
*/
private void ui_InitGUIInputFiltersHydroBase ( HydroBaseDataStore dataStore, int y )
{   String routine = getClass().getName() + ".ui_InitGUIInputFiltersHydroBase";
    int buffer = 3;
    Insets insets = new Insets(0,buffer,0,0);
    
    // If the an instance of a panel is not null, remove it from the list and then recreate it.

    // Add input filters for stations...
    
    try {
        if ( __inputFilterHydroBaseStation_JPanel != null ) {
            __inputFilterJPanelList.remove (__inputFilterHydroBaseStation_JPanel );
        }
        __inputFilterHydroBaseStation_JPanel = new
        HydroBase_GUI_StationGeolocMeasType_InputFilter_JPanel( ui_GetHydroBaseDataStore() );
        JGUIUtil.addComponent(__queryInput_JPanel, __inputFilterHydroBaseStation_JPanel,
            0, y, 3, 1, 1.0, 0.0, insets, GridBagConstraints.HORIZONTAL,
            GridBagConstraints.WEST );
        __inputFilterHydroBaseStation_JPanel.setName ( "HydroBase.StationInputFilterPanel");
        __inputFilterJPanelList.add (__inputFilterHydroBaseStation_JPanel );
    }
    catch ( Exception e ) {
        Message.printWarning ( 2, routine, "Unable to initialize input filter for HydroBase stations." );
        Message.printWarning ( 2, routine, e );
    }
    
    // Add input filters for structures - there is one panel for
    // "total" time series and one for water class time series that can be filtered by SFUT...
    
    try {
        if ( __inputFilterHydroBaseStructure_JPanel != null ) {
            __inputFilterJPanelList.remove ( __inputFilterHydroBaseStructure_JPanel );
        }
        __inputFilterHydroBaseStructure_JPanel = new
            HydroBase_GUI_StructureGeolocStructMeasType_InputFilter_JPanel( ui_GetHydroBaseDataStore(), false );
        JGUIUtil.addComponent(__queryInput_JPanel, __inputFilterHydroBaseStructure_JPanel,
            0, y, 3, 1, 1.0, 0.0, insets, GridBagConstraints.HORIZONTAL,
            GridBagConstraints.WEST );
        __inputFilterHydroBaseStructure_JPanel.setName ( "HydroBase.StructureInputFilterPanel" );
        __inputFilterJPanelList.add ( __inputFilterHydroBaseStructure_JPanel );
    }
    catch ( Exception e ) {
        Message.printWarning ( 2, routine, "Unable to initialize input filter for HydroBase structures." );
        Message.printWarning ( 2, routine, e );
    }
    
    try {
        if ( __inputFilterHydroBaseStructureSfut_JPanel != null ) {
            __inputFilterJPanelList.remove ( __inputFilterHydroBaseStructureSfut_JPanel);
        }
        __inputFilterHydroBaseStructureSfut_JPanel = new
        HydroBase_GUI_StructureGeolocStructMeasType_InputFilter_JPanel( ui_GetHydroBaseDataStore(), true );
        JGUIUtil.addComponent(__queryInput_JPanel, __inputFilterHydroBaseStructureSfut_JPanel,
            0, y, 3, 1, 1.0, 0.0, insets, GridBagConstraints.HORIZONTAL,
            GridBagConstraints.WEST );
        __inputFilterHydroBaseStructureSfut_JPanel.setName("HydroBase.StructureSFUTInputFilterPanel");
        __inputFilterJPanelList.add ( __inputFilterHydroBaseStructureSfut_JPanel);
    }
    catch ( Exception e ) {
        Message.printWarning ( 2, routine,
        "Unable to initialize input filter for HydroBase structures with SFUT." );
        Message.printWarning ( 2, routine, e );
    }
    
    // Add input filters for structure irrig_summary_ts,
    
    try {
        if ( __inputFilterHydroBaseIrrigts_JPanel != null ) {
            __inputFilterJPanelList.remove ( __inputFilterHydroBaseIrrigts_JPanel );
        }
        __inputFilterHydroBaseIrrigts_JPanel = new
        HydroBase_GUI_StructureIrrigSummaryTS_InputFilter_JPanel( ui_GetHydroBaseDataStore() );
        JGUIUtil.addComponent(__queryInput_JPanel, __inputFilterHydroBaseIrrigts_JPanel,
            0, y, 3, 1, 1.0, 0.0, insets, GridBagConstraints.HORIZONTAL,
            GridBagConstraints.WEST );
        __inputFilterHydroBaseIrrigts_JPanel.setName ( "HydroBase.IrrigationSummaryInputFilterPanel");
        __inputFilterJPanelList.add ( __inputFilterHydroBaseIrrigts_JPanel );
    }
    catch ( Exception e ) {
        Message.printWarning ( 2, routine,
        "Unable to initialize input filter for HydroBase irrigation summary time series - old database?" );
        Message.printWarning ( 2, routine, e );
    }
    
    // Add input filters for CASS agricultural crop statistics,
    // only available for newer databases.  For now, just catch an exception when not supported.
    
    try {
        if ( __inputFilterHydroBaseCASSCropStats_JPanel != null ) {
            __inputFilterJPanelList.remove ( __inputFilterHydroBaseCASSCropStats_JPanel );
        }
        __inputFilterHydroBaseCASSCropStats_JPanel = new
        HydroBase_GUI_AgriculturalCASSCropStats_InputFilter_JPanel ( ui_GetHydroBaseDataStore() );
        JGUIUtil.addComponent(__queryInput_JPanel, __inputFilterHydroBaseCASSCropStats_JPanel,
            0, y, 3, 1, 1.0, 0.0, insets, GridBagConstraints.HORIZONTAL,
            GridBagConstraints.WEST );
        __inputFilterHydroBaseCASSCropStats_JPanel.setName("HydroBase.CASSCropsInputFilterPanel");
        __inputFilterJPanelList.add ( __inputFilterHydroBaseCASSCropStats_JPanel );
    }
    catch ( Exception e ) {
        // Agricultural_CASS_crop_stats probably not in HydroBase...
        Message.printWarning ( 2, routine,
        "Unable to initialize input filter for HydroBase CASS crop statistics - old database?" );
        Message.printWarning ( 2, routine, e );
    }
    
    // Add input filters for CASS agricultural livestock statistics,
    // only available for newer databases.  For now, just catch an
    // exception when not supported.
    
    try {
        if ( __inputFilterHydroBaseCASSLivestockStats_JPanel != null ) {
            __inputFilterJPanelList.remove ( __inputFilterHydroBaseCASSLivestockStats_JPanel );
        }
        __inputFilterHydroBaseCASSLivestockStats_JPanel = new
        HydroBase_GUI_AgriculturalCASSLivestockStats_InputFilter_JPanel ( ui_GetHydroBaseDataStore() );
        JGUIUtil.addComponent(__queryInput_JPanel, __inputFilterHydroBaseCASSLivestockStats_JPanel,
            0, y, 3, 1, 1.0, 0.0, insets, GridBagConstraints.HORIZONTAL,
            GridBagConstraints.WEST );
        __inputFilterHydroBaseCASSLivestockStats_JPanel.setName("HydroBase.CASSLivestockInputFilterPanel");
        __inputFilterJPanelList.add ( __inputFilterHydroBaseCASSLivestockStats_JPanel );
    }
    catch ( Exception e ) {
        // Agricultural_CASS_livestock_stats probably not in HydroBase...
        Message.printWarning ( 2, routine,
        "Unable to initialize input filter for HydroBase CASS livestock statistics - old database?" );
        Message.printWarning ( 2, routine, e );
    }
    
    // Add input filters for CU population data, only available for
    // newer databases.  For now, just catch an exception when not supported.
    
    try {
        if ( __inputFilterHydroBaseCUPopulation_JPanel != null ) {
            __inputFilterJPanelList.remove ( __inputFilterHydroBaseCUPopulation_JPanel );
        }
        __inputFilterHydroBaseCUPopulation_JPanel = new
        HydroBase_GUI_CUPopulation_InputFilter_JPanel( ui_GetHydroBaseDataStore() );
        JGUIUtil.addComponent(__queryInput_JPanel, __inputFilterHydroBaseCUPopulation_JPanel,
            0, y, 3, 1, 1.0, 0.0, insets, GridBagConstraints.HORIZONTAL,
            GridBagConstraints.WEST );
        __inputFilterHydroBaseCUPopulation_JPanel.setName("HydroBase.CUPopulationInputFilterPanel");
        __inputFilterJPanelList.add ( __inputFilterHydroBaseCUPopulation_JPanel );
    }
    catch ( Exception e ) {
        // CUPopulation probably not in HydroBase...
        Message.printWarning ( 2, routine,
        "Unable to initialize input filter for HydroBase CU population data - old database?" );
        Message.printWarning ( 2, routine, e );
    }
    
    // Add input filters for NASS agricultural statistics, only
    // available for newer databases.  For now, just catch an exception when not supported.
    
    try {
        if ( __inputFilterHydroBaseNASS_JPanel != null ) {
            __inputFilterJPanelList.remove ( __inputFilterHydroBaseNASS_JPanel );
        }
        __inputFilterHydroBaseNASS_JPanel = new
        HydroBase_GUI_AgriculturalNASSCropStats_InputFilter_JPanel ( ui_GetHydroBaseDataStore() );
        JGUIUtil.addComponent(__queryInput_JPanel, __inputFilterHydroBaseNASS_JPanel,
            0, y, 3, 1, 1.0, 0.0, insets, GridBagConstraints.HORIZONTAL,
            GridBagConstraints.WEST );
        __inputFilterHydroBaseNASS_JPanel.setName("HydroBase.NASSInputFilterPanel");
        __inputFilterJPanelList.add ( __inputFilterHydroBaseNASS_JPanel );
    }
    catch ( Exception e ) {
        // Agricultural_NASS_crop_stats probably not in HydroBase...
        Message.printWarning ( 2, routine,
        "Unable to initialize input filter for HydroBase agricultural_NASS_crop_stats - old database?" );
        Message.printWarning ( 2, routine, e );
    }
    
    // Add input filters for WIS.  For now, just catch an exception when not supported.
    
    try {
        if ( __inputFilterHydroBaseWIS_JPanel != null ) {
            __inputFilterJPanelList.remove ( __inputFilterHydroBaseWIS_JPanel );
        }
        __inputFilterHydroBaseWIS_JPanel = new
        HydroBase_GUI_SheetNameWISFormat_InputFilter_JPanel ( ui_GetHydroBaseDataStore() );
        JGUIUtil.addComponent(__queryInput_JPanel, __inputFilterHydroBaseWIS_JPanel,
            0, y, 3, 1, 1.0, 0.0, insets, GridBagConstraints.HORIZONTAL,
            GridBagConstraints.WEST );
        __inputFilterHydroBaseWIS_JPanel.setName("HydroBase.WISInputFilterPanel");
        __inputFilterJPanelList.add ( __inputFilterHydroBaseWIS_JPanel );
    }
    catch ( Exception e ) {
        // WIS tables probably not in HydroBase...
        Message.printWarning ( 2, routine,
        "Unable to initialize input filter for HydroBase WIS - data tables not in database?" );
        Message.printWarning ( 2, routine, e );
    }
    
    try {
        if ( __inputFilterHydroBaseWells_JPanel != null ) {
            __inputFilterJPanelList.remove( __inputFilterHydroBaseWells_JPanel);
        }
        __inputFilterHydroBaseWells_JPanel =
            new HydroBase_GUI_GroundWater_InputFilter_JPanel ( ui_GetHydroBaseDataStore(), null, true);
        JGUIUtil.addComponent(__queryInput_JPanel,__inputFilterHydroBaseWells_JPanel,
            0, y, 3, 1, 1.0, 0.0, insets, GridBagConstraints.HORIZONTAL,
            GridBagConstraints.WEST);
        __inputFilterHydroBaseWells_JPanel.setName("HydroBase.WellsInputFilterPanel");
        __inputFilterJPanelList.add( __inputFilterHydroBaseWells_JPanel);
    }
    catch ( Exception e ) {
        // Agricultural_NASS_crop_stats probably not in HydroBase...
        Message.printWarning ( 2, routine,
        "Unable to initialize input filter for HydroBase wells - old database?" );
        Message.printWarning ( 2, routine, e );
    }
}

/**
Initialize the HydroBase input filters for HydroBase datastores.
@param dataStoreList HydroBase datastores
*/
private void ui_InitGUIInputFiltersHydroBase ( List<DataStore> dataStoreList, int y )
{   String routine = getClass().getName() + ".ui_InitGUIInputFiltersHydroBase";
    int buffer = 3;
    Insets insets = new Insets(0,buffer,0,0);
    
    Message.printStatus ( 2, routine, "Initializing input filter(s) for " + dataStoreList.size() +
        " HydroBase data stores." );
    for ( DataStore dataStore: dataStoreList ) {
        try {
            // Try to find an existing input filter panel for the same name...
            String selectedDataType = ui_GetSelectedDataType();
            JPanel ifp = ui_GetInputFilterPanelForDataStoreName ( dataStore.getName(), selectedDataType );
            // If the previous instance is not null, remove it from the list...
            if ( ifp != null ) {
                __inputFilterJPanelList.remove ( ifp );
            }
        }
        catch ( Exception e ) {
            Message.printWarning ( 2, routine,
                "Unable to remove old (conflicting) input filter for HydroBase data store \"" +
                    dataStore.getName() + "\" (" + e + ")." );
            Message.printWarning ( 3, routine, e );
        }

        // Add input filters for stations...
        
        try {
            if ( __inputFilterHydroBaseStation_JPanel != null ) {
                __inputFilterJPanelList.remove (__inputFilterHydroBaseStation_JPanel );
            }
            __inputFilterHydroBaseStation_JPanel = new
            HydroBase_GUI_StationGeolocMeasType_InputFilter_JPanel( (HydroBaseDataStore)dataStore );
            JGUIUtil.addComponent(__queryInput_JPanel, __inputFilterHydroBaseStation_JPanel,
                0, y, 3, 1, 1.0, 0.0, insets, GridBagConstraints.HORIZONTAL,
                GridBagConstraints.WEST );
            __inputFilterHydroBaseStation_JPanel.setName ( "HydroBase.StationInputFilterPanel");
            __inputFilterJPanelList.add (__inputFilterHydroBaseStation_JPanel );
        }
        catch ( Exception e ) {
            Message.printWarning ( 2, routine, "Unable to initialize input filter for HydroBase stations for data store \"" +
                dataStore.getName() + "\" (" + e + ")." );
            Message.printWarning ( 2, routine, e );
        }
        
        // Add input filters for structures - there is one panel for
        // "total" time series and one for water class time series that can be filtered by SFUT...
        
        try {
            if ( __inputFilterHydroBaseStructure_JPanel != null ) {
                __inputFilterJPanelList.remove ( __inputFilterHydroBaseStructure_JPanel );
            }
            __inputFilterHydroBaseStructure_JPanel = new
                HydroBase_GUI_StructureGeolocStructMeasType_InputFilter_JPanel( (HydroBaseDataStore)dataStore, false );
            JGUIUtil.addComponent(__queryInput_JPanel, __inputFilterHydroBaseStructure_JPanel,
                0, y, 3, 1, 1.0, 0.0, insets, GridBagConstraints.HORIZONTAL,
                GridBagConstraints.WEST );
            __inputFilterHydroBaseStructure_JPanel.setName ( "HydroBase.StructureInputFilterPanel" );
            __inputFilterJPanelList.add ( __inputFilterHydroBaseStructure_JPanel );
        }
        catch ( Exception e ) {
            Message.printWarning ( 2, routine, "Unable to initialize input filter for HydroBase structures for data store \"" +
                dataStore.getName() + "\" (" + e + ")." );
            Message.printWarning ( 2, routine, e );
        }
        
        try {
            if ( __inputFilterHydroBaseStructureSfut_JPanel != null ) {
                __inputFilterJPanelList.remove ( __inputFilterHydroBaseStructureSfut_JPanel);
            }
            __inputFilterHydroBaseStructureSfut_JPanel = new
            HydroBase_GUI_StructureGeolocStructMeasType_InputFilter_JPanel( (HydroBaseDataStore)dataStore, true );
            JGUIUtil.addComponent(__queryInput_JPanel, __inputFilterHydroBaseStructureSfut_JPanel,
                0, y, 3, 1, 1.0, 0.0, insets, GridBagConstraints.HORIZONTAL,
                GridBagConstraints.WEST );
            __inputFilterHydroBaseStructureSfut_JPanel.setName("HydroBase.StructureSFUTInputFilterPanel");
            __inputFilterJPanelList.add ( __inputFilterHydroBaseStructureSfut_JPanel);
        }
        catch ( Exception e ) {
            Message.printWarning ( 2, routine,
                    "Unable to initialize input filter for HydroBase structures with SFUT for data store \"" +
                dataStore.getName() + "\" (" + e + ")." );
            Message.printWarning ( 2, routine, e );
        }
        
        // Add input filters for structure irrig_summary_ts,
        
        try {
            if ( __inputFilterHydroBaseIrrigts_JPanel != null ) {
                __inputFilterJPanelList.remove ( __inputFilterHydroBaseIrrigts_JPanel );
            }
            __inputFilterHydroBaseIrrigts_JPanel = new
            HydroBase_GUI_StructureIrrigSummaryTS_InputFilter_JPanel( (HydroBaseDataStore)dataStore );
            JGUIUtil.addComponent(__queryInput_JPanel, __inputFilterHydroBaseIrrigts_JPanel,
                0, y, 3, 1, 1.0, 0.0, insets, GridBagConstraints.HORIZONTAL,
                GridBagConstraints.WEST );
            __inputFilterHydroBaseIrrigts_JPanel.setName ( "HydroBase.IrrigationSummaryInputFilterPanel");
            __inputFilterJPanelList.add ( __inputFilterHydroBaseIrrigts_JPanel );
        }
        catch ( Exception e ) {
            Message.printWarning ( 2, routine,
                "Unable to initialize input filter for HydroBase irrigation summary time series for data store \"" +
                dataStore.getName() + "\" (" + e + ")." );
            Message.printWarning ( 2, routine, e );
        }
        
        // Add input filters for CASS agricultural crop statistics,
        // only available for newer databases.  For now, just catch an exception when not supported.
        
        try {
            if ( __inputFilterHydroBaseCASSCropStats_JPanel != null ) {
                __inputFilterJPanelList.remove ( __inputFilterHydroBaseCASSCropStats_JPanel );
            }
            __inputFilterHydroBaseCASSCropStats_JPanel = new
            HydroBase_GUI_AgriculturalCASSCropStats_InputFilter_JPanel ( (HydroBaseDataStore)dataStore );
            JGUIUtil.addComponent(__queryInput_JPanel, __inputFilterHydroBaseCASSCropStats_JPanel,
                0, y, 3, 1, 1.0, 0.0, insets, GridBagConstraints.HORIZONTAL,
                GridBagConstraints.WEST );
            __inputFilterHydroBaseCASSCropStats_JPanel.setName("HydroBase.CASSCropsInputFilterPanel");
            __inputFilterJPanelList.add ( __inputFilterHydroBaseCASSCropStats_JPanel );
        }
        catch ( Exception e ) {
            // Agricultural_CASS_crop_stats probably not in HydroBase...
            Message.printWarning ( 2, routine,
                "Unable to initialize input filter for HydroBase CASS crop statistics for datastore \"" +
                dataStore.getName() + "\" (" + e + ")." );
            Message.printWarning ( 2, routine, e );
        }
        
        // Add input filters for CASS agricultural livestock statistics,
        // only available for newer databases.  For now, just catch an
        // exception when not supported.
        
        try {
            if ( __inputFilterHydroBaseCASSLivestockStats_JPanel != null ) {
                __inputFilterJPanelList.remove ( __inputFilterHydroBaseCASSLivestockStats_JPanel );
            }
            __inputFilterHydroBaseCASSLivestockStats_JPanel = new
            HydroBase_GUI_AgriculturalCASSLivestockStats_InputFilter_JPanel ( (HydroBaseDataStore)dataStore );
            JGUIUtil.addComponent(__queryInput_JPanel, __inputFilterHydroBaseCASSLivestockStats_JPanel,
                0, y, 3, 1, 1.0, 0.0, insets, GridBagConstraints.HORIZONTAL,
                GridBagConstraints.WEST );
            __inputFilterHydroBaseCASSLivestockStats_JPanel.setName("HydroBase.CASSLivestockInputFilterPanel");
            __inputFilterJPanelList.add ( __inputFilterHydroBaseCASSLivestockStats_JPanel );
        }
        catch ( Exception e ) {
            // Agricultural_CASS_livestock_stats probably not in HydroBase...
            Message.printWarning ( 2, routine,
                "Unable to initialize input filter for HydroBase CASS livestock statistics for datastore \"" +
                dataStore.getName() + "\" (" + e + ")." );
            Message.printWarning ( 2, routine, e );
        }
        
        // Add input filters for CU population data, only available for
        // newer databases.  For now, just catch an exception when not supported.
        
        try {
            if ( __inputFilterHydroBaseCUPopulation_JPanel != null ) {
                __inputFilterJPanelList.remove ( __inputFilterHydroBaseCUPopulation_JPanel );
            }
            __inputFilterHydroBaseCUPopulation_JPanel = new
            HydroBase_GUI_CUPopulation_InputFilter_JPanel( (HydroBaseDataStore)dataStore );
            JGUIUtil.addComponent(__queryInput_JPanel, __inputFilterHydroBaseCUPopulation_JPanel,
                0, y, 3, 1, 1.0, 0.0, insets, GridBagConstraints.HORIZONTAL,
                GridBagConstraints.WEST );
            __inputFilterHydroBaseCUPopulation_JPanel.setName("HydroBase.CUPopulationInputFilterPanel");
            __inputFilterJPanelList.add ( __inputFilterHydroBaseCUPopulation_JPanel );
        }
        catch ( Exception e ) {
            // CUPopulation probably not in HydroBase...
            Message.printWarning ( 2, routine,
                "Unable to initialize input filter for HydroBase CU population datastore \"" +
                dataStore.getName() + "\" (" + e + ")." );
            Message.printWarning ( 2, routine, e );
        }
        
        // Add input filters for NASS agricultural statistics, only
        // available for newer databases.  For now, just catch an exception when not supported.
        
        try {
            if ( __inputFilterHydroBaseNASS_JPanel != null ) {
                __inputFilterJPanelList.remove ( __inputFilterHydroBaseNASS_JPanel );
            }
            __inputFilterHydroBaseNASS_JPanel = new
            HydroBase_GUI_AgriculturalNASSCropStats_InputFilter_JPanel ( (HydroBaseDataStore)dataStore );
            JGUIUtil.addComponent(__queryInput_JPanel, __inputFilterHydroBaseNASS_JPanel,
                0, y, 3, 1, 1.0, 0.0, insets, GridBagConstraints.HORIZONTAL,
                GridBagConstraints.WEST );
            __inputFilterHydroBaseNASS_JPanel.setName("HydroBase.NASSInputFilterPanel");
            __inputFilterJPanelList.add ( __inputFilterHydroBaseNASS_JPanel );
        }
        catch ( Exception e ) {
            // Agricultural_NASS_crop_stats probably not in HydroBase...
            Message.printWarning ( 2, routine,
                "Unable to initialize input filter for HydroBase agricultural_NASS_crop_stats for datastore \"" +
                dataStore.getName() + "\" (" + e + ")." );
            Message.printWarning ( 2, routine, e );
        }
        
        // Add input filters for WIS.  For now, just catch an exception when not supported.
        
        try {
            if ( __inputFilterHydroBaseWIS_JPanel != null ) {
                __inputFilterJPanelList.remove ( __inputFilterHydroBaseWIS_JPanel );
            }
            __inputFilterHydroBaseWIS_JPanel = new
            HydroBase_GUI_SheetNameWISFormat_InputFilter_JPanel ( (HydroBaseDataStore)dataStore );
            JGUIUtil.addComponent(__queryInput_JPanel, __inputFilterHydroBaseWIS_JPanel,
                0, y, 3, 1, 1.0, 0.0, insets, GridBagConstraints.HORIZONTAL,
                GridBagConstraints.WEST );
            __inputFilterHydroBaseWIS_JPanel.setName("HydroBase.WISInputFilterPanel");
            __inputFilterJPanelList.add ( __inputFilterHydroBaseWIS_JPanel );
        }
        catch ( Exception e ) {
            // WIS tables probably not in HydroBase...
            Message.printWarning ( 2, routine,
                "Unable to initialize input filter for HydroBase WIS for datastore \"" +
                dataStore.getName() + "\" (" + e + ")." );
            Message.printWarning ( 2, routine, e );
        }
        
        try {
            if ( __inputFilterHydroBaseWells_JPanel != null ) {
                __inputFilterJPanelList.remove( __inputFilterHydroBaseWells_JPanel);
            }
            __inputFilterHydroBaseWells_JPanel =
                new HydroBase_GUI_GroundWater_InputFilter_JPanel ( (HydroBaseDataStore)dataStore, null, true);
            JGUIUtil.addComponent(__queryInput_JPanel,__inputFilterHydroBaseWells_JPanel,
                0, y, 3, 1, 1.0, 0.0, insets, GridBagConstraints.HORIZONTAL,
                GridBagConstraints.WEST);
            __inputFilterHydroBaseWells_JPanel.setName("HydroBase.WellsInputFilterPanel");
            __inputFilterJPanelList.add( __inputFilterHydroBaseWells_JPanel);
        }
        catch ( Exception e ) {
            // Agricultural_NASS_crop_stats probably not in HydroBase...
            Message.printWarning ( 2, routine,
                "Unable to initialize input filter for HydroBase wells for datastore \"" +
                dataStore.getName() + "\" (" + e + ")." );
            Message.printWarning ( 2, routine, e );
        }
    }
}

/**
Initialize the RCC ACIS input filter (may be called at startup).
@param dataStoreList the list of data stores for which input filter panels are to be added.
@param y the position in the input panel that the filter should be added
*/
private void ui_InitGUIInputFiltersRccAcis ( List<DataStore> dataStoreList, int y )
{   String routine = getClass().getName() + ".ui_InitGUIInputFiltersRccAcis";
    Message.printStatus ( 2, routine, "Initializing input filter(s) for " + dataStoreList.size() +
        " RCC ACIS data stores." );
    for ( DataStore dataStore: dataStoreList ) {
        try {
            // Try to find an existing input filter panel for the same name...
            String selectedDataType = ui_GetSelectedDataType();
            JPanel ifp = ui_GetInputFilterPanelForDataStoreName ( dataStore.getName(), selectedDataType );
            // If the previous instance is not null, remove it from the list...
            if ( ifp != null ) {
                __inputFilterJPanelList.remove ( ifp );
            }
            // Create a new panel...
            RccAcis_TimeSeries_InputFilter_JPanel newIfp =
                new RccAcis_TimeSeries_InputFilter_JPanel((RccAcisDataStore)dataStore, 3);
    
            // Add the new panel to the layout and set in the global data...
            int buffer = 3;
            Insets insets = new Insets(0,buffer,0,0);
            JGUIUtil.addComponent(__queryInput_JPanel, newIfp,
                0, y, 3, 1, 1.0, 0.0, insets, GridBagConstraints.HORIZONTAL,
                GridBagConstraints.WEST );
            newIfp.setName("RccAcis.InputFilterPanel");
            __inputFilterJPanelList.add ( newIfp );
        }
        catch ( Exception e ) {
            Message.printWarning ( 2, routine,
                "Unable to initialize input filter for RCC ACIS time series " +
                "for data store \"" + dataStore.getName() + "\" (" + e + ")." );
            Message.printWarning ( 2, routine, e );
        }
    }
}

/**
Initialize the Reclamation HDB input filter (may be called at startup).
@param dataStoreList the list of data stores for which input filter panels are to be added.
@param y the position in the input panel that the filter should be added
*/
private void ui_InitGUIInputFiltersReclamationHDB ( List<DataStore> dataStoreList, int y )
{   String routine = getClass().getName() + ".ui_InitGUIInputFiltersReclamationHDB";
    Message.printStatus ( 2, routine, "Initializing input filter(s) for " + dataStoreList.size() +
        " ReclamationHDB data stores." );
    String selectedDataType = ui_GetSelectedDataType();
    for ( DataStore dataStore: dataStoreList ) {
        try {
            // Try to find an existing input filter panel for the same name...
            JPanel ifp = ui_GetInputFilterPanelForDataStoreName ( dataStore.getName(), selectedDataType );
            // If the previous instance is not null, remove it from the list...
            if ( ifp != null ) {
                __inputFilterJPanelList.remove ( ifp );
            }
            // Create a new panel...
            ReclamationHDB_TimeSeries_InputFilter_JPanel newIfp =
                new ReclamationHDB_TimeSeries_InputFilter_JPanel((ReclamationHDBDataStore)dataStore, 3);
    
            // Add the new panel to the layout and set in the global data...
            int buffer = 3;
            Insets insets = new Insets(0,buffer,0,0);
            JGUIUtil.addComponent(__queryInput_JPanel, newIfp,
                0, y, 3, 1, 1.0, 0.0, insets, GridBagConstraints.HORIZONTAL,
                GridBagConstraints.WEST );
            newIfp.setName("ReclamationHDB.InputFilterPanel");
            __inputFilterJPanelList.add ( newIfp );
        }
        catch ( Exception e ) {
            Message.printWarning ( 2, routine,
                "Unable to initialize input filter for Reclamation HDB time series " +
                "for data store \"" + dataStore.getName() + "\" (" + e + ")." );
            Message.printWarning ( 2, routine, e );
        }
    }
}

/**
Initialize the RiversideDB input filter (may be called at startup after login or File...Open RiversideDB).
@param dataStoreList the list of data stores for which input filter panels are to be added.
@param y the position in the input panel that the filter should be added
*/
private void ui_InitGUIInputFiltersRiversideDB ( List<DataStore> dataStoreList, int y )
{   String routine = getClass().getName() + ".ui_InitGUIInputFiltersRiversideDB";
    Message.printStatus ( 2, routine, "Initializing input filter(s) for " + dataStoreList.size() +
        " RiversideDB data stores." );
    for ( DataStore dataStore: dataStoreList ) {
        try {
            // Try to find an existing input filter panel for the same name...
            String selectedDataType = ui_GetSelectedDataType();
            JPanel ifp = ui_GetInputFilterPanelForDataStoreName ( dataStore.getName(), selectedDataType );
            // If the previous instance is not null, remove it from the list...
            if ( ifp != null ) {
                __inputFilterJPanelList.remove ( ifp );
            }
            // Create a new panel...
            RiversideDB_MeasTypeMeasLocGeoloc_InputFilter_JPanel newIfp =
                new RiversideDB_MeasTypeMeasLocGeoloc_InputFilter_JPanel((RiversideDBDataStore)dataStore,6);
    
            // Add the new panel to the layout and set in the global data...
            int buffer = 3;
            Insets insets = new Insets(0,buffer,0,0);
            JGUIUtil.addComponent(__queryInput_JPanel, newIfp,
                0, y, 3, 1, 1.0, 0.0, insets, GridBagConstraints.HORIZONTAL,
                GridBagConstraints.WEST );
            // The name is used to set visible later
            newIfp.setName("RiversideDB.InputFilterPanel");
            __inputFilterJPanelList.add ( newIfp );
        }
        catch ( Exception e ) {
            Message.printWarning ( 2, routine,
                "Unable to initialize input filter for RiversideDB time series (MeasType/MeasLoc/Geoloc) " +
                "for data store \"" + dataStore.getName() + "\" (" + e + ")." );
            Message.printWarning ( 3, routine, e );
        }
    }
}

/**
Initialize the USGS NWIS input filter (may be called at startup).
@param dataStoreList the list of data stores for which input filter panels are to be added.
@param y the position in the input panel that the filter should be added
*/
private void ui_InitGUIInputFiltersUsgsNwisDaily ( List<DataStore> dataStoreList, int y )
{   String routine = getClass().getName() + ".ui_InitGUIInputFiltersUsgsNwisDaily";
    Message.printStatus ( 2, routine, "Initializing input filter(s) for " + dataStoreList.size() +
        " UsgsNwisDaily data stores." );
    String selectedDataType = ui_GetSelectedDataType();
    for ( DataStore dataStore: dataStoreList ) {
        try {
            // Try to find an existing input filter panel for the same name...
            JPanel ifp = ui_GetInputFilterPanelForDataStoreName ( dataStore.getName(), selectedDataType );
            // If the previous instance is not null, remove it from the list...
            if ( ifp != null ) {
                __inputFilterJPanelList.remove ( ifp );
            }
            // Create a new panel...
            UsgsNwisDaily_TimeSeries_InputFilter_JPanel newIfp =
                new UsgsNwisDaily_TimeSeries_InputFilter_JPanel((UsgsNwisDailyDataStore)dataStore, 3);
    
            // Add the new panel to the layout and set in the global data...
            int buffer = 3;
            Insets insets = new Insets(0,buffer,0,0);
            JGUIUtil.addComponent(__queryInput_JPanel, newIfp,
                0, y, 3, 1, 1.0, 0.0, insets, GridBagConstraints.HORIZONTAL,
                GridBagConstraints.WEST );
            newIfp.setName("UsgsNwisDaily.InputFilterPanel");
            __inputFilterJPanelList.add ( newIfp );
        }
        catch ( Exception e ) {
            Message.printWarning ( 2, routine,
                "Unable to initialize input filter for USGS NWIS daily time series for data store \"" +
                dataStore.getName() + "\" (" + e + ")." );
            Message.printWarning ( 2, routine, e );
        }
    }
}

/**
Initialize the GUI menus.
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
	__Commands_JMenu.setToolTipText("Insert command into commands list (above first selected command, or at end).");

	__Commands_JMenu.add ( __Commands_CreateTimeSeries_JMenu = new JMenu(__Commands_CreateTimeSeries_String) );
	__Commands_CreateTimeSeries_JMenu.setToolTipText("Create time series from supplied values or other time series.");

	// Create (break into logical groups)...
	// Commands that create new simple data (maybe add functions or random)...
	   
    __Commands_CreateTimeSeries_JMenu.add ( __Commands_Create_NewPatternTimeSeries_JMenuItem =
       new SimpleJMenuItem(__Commands_Create_NewPatternTimeSeries_String, this ) );
    
    __Commands_CreateTimeSeries_JMenu.add (__Commands_Create_NewTimeSeries_JMenuItem =
        new SimpleJMenuItem(__Commands_Create_NewTimeSeries_String, this ) );
    
    __Commands_CreateTimeSeries_JMenu.addSeparator();
	// Commands that perform other processing and create new time series...
    __Commands_CreateTimeSeries_JMenu.add(__Commands_Create_ChangeInterval_JMenuItem =
        new SimpleJMenuItem(__Commands_Create_ChangeInterval_String, this) );
    
    __Commands_CreateTimeSeries_JMenu.add(__Commands_Create_Copy_JMenuItem =
        new SimpleJMenuItem(__Commands_Create_Copy_String, this) );
    
    __Commands_CreateTimeSeries_JMenu.add( __Commands_Create_Delta_JMenuItem =
        new SimpleJMenuItem(__Commands_Create_Delta_String, this) );
    
    __Commands_CreateTimeSeries_JMenu.add( __Commands_Create_Disaggregate_JMenuItem =
        new SimpleJMenuItem(__Commands_Create_Disaggregate_String, this) );
    
    __Commands_CreateTimeSeries_JMenu.add( __Commands_Create_LookupTimeSeriesFromTable_JMenuItem =
        new SimpleJMenuItem(__Commands_Create_LookupTimeSeriesFromTable_String, this) );
    
    __Commands_CreateTimeSeries_JMenu.add(__Commands_Create_NewDayTSFromMonthAndDayTS_JMenuItem =
        new SimpleJMenuItem(__Commands_Create_NewDayTSFromMonthAndDayTS_String, this) );

    __Commands_CreateTimeSeries_JMenu.add (__Commands_Create_NewEndOfMonthTSFromDayTS_JMenuItem =
        new SimpleJMenuItem(__Commands_Create_NewEndOfMonthTSFromDayTS_String, this ) );
    
    __Commands_CreateTimeSeries_JMenu.add(__Commands_Create_Normalize_JMenuItem =
        new SimpleJMenuItem(__Commands_Create_Normalize_String, this ) );

    __Commands_CreateTimeSeries_JMenu.add ( __Commands_Create_RelativeDiff_JMenuItem =
        new SimpleJMenuItem( __Commands_Create_RelativeDiff_String, this ) );
    
    __Commands_CreateTimeSeries_JMenu.add( __Commands_Create_ResequenceTimeSeriesData_JMenuItem =
        new SimpleJMenuItem(__Commands_Create_ResequenceTimeSeriesData_String, this ) );
    
    // Statistic commands...
    __Commands_CreateTimeSeries_JMenu.addSeparator();
	__Commands_CreateTimeSeries_JMenu.add (	__Commands_Create_NewStatisticTimeSeries_JMenuItem =
		new SimpleJMenuItem(__Commands_Create_NewStatisticTimeSeries_String, this ) );
	__Commands_CreateTimeSeries_JMenu.add (__Commands_Create_NewStatisticYearTS_JMenuItem =
		new SimpleJMenuItem(__Commands_Create_NewStatisticYearTS_String, this ) );
    __Commands_CreateTimeSeries_JMenu.add ( __Commands_Create_RunningStatisticTimeSeries_JMenuItem =
        new SimpleJMenuItem(__Commands_Create_RunningStatisticTimeSeries_String, this ) );

	// Read...

	__Commands_JMenu.add ( __Commands_ReadTimeSeries_JMenu=	new JMenu(__Commands_ReadTimeSeries_String) );
	__Commands_ReadTimeSeries_JMenu.setToolTipText("Read time series from files, databases, and the internet.");

	__Commands_ReadTimeSeries_JMenu.add (__Commands_Read_SetIncludeMissingTS_JMenuItem =
		new SimpleJMenuItem(__Commands_Read_SetIncludeMissingTS_String, this ) );

	__Commands_ReadTimeSeries_JMenu.add (__Commands_Read_SetInputPeriod_JMenuItem=
        new SimpleJMenuItem(__Commands_Read_SetInputPeriod_String, this ) );

	__Commands_ReadTimeSeries_JMenu.addSeparator ();
	
	__Commands_ReadTimeSeries_JMenu.add( __Commands_Read_CreateFromList_JMenuItem =
        new SimpleJMenuItem(__Commands_Create_CreateFromList_String, this) );
   
    __Commands_ReadTimeSeries_JMenu.addSeparator ();
	
    if ( __source_ColoradoBNDSS_enabled ) {
        __Commands_ReadTimeSeries_JMenu.add(__Commands_Read_ReadColoradoBNDSS_JMenuItem =
            new SimpleJMenuItem(__Commands_Read_ReadColoradoBNDSS_String, this) );
    }
	if ( __source_DateValue_enabled ) {
		__Commands_ReadTimeSeries_JMenu.add(__Commands_Read_ReadDateValue_JMenuItem =
			new SimpleJMenuItem(__Commands_Read_ReadDateValue_String, this) );
	}
    __Commands_ReadTimeSeries_JMenu.add(__Commands_Read_ReadDelimitedFile_JMenuItem =
        new SimpleJMenuItem(__Commands_Read_ReadDelimitedFile_String, this) );
    if ( __source_HECDSS_enabled ) {
        __Commands_ReadTimeSeries_JMenu.add(__Commands_Read_ReadHecDss_JMenuItem =
            new SimpleJMenuItem(__Commands_Read_ReadHecDss_String, this) );
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
    if ( __source_NWSRFS_FS5Files_enabled ) {
        __Commands_ReadTimeSeries_JMenu.add(__Commands_Read_ReadNwsrfsFS5Files_JMenuItem =
            new SimpleJMenuItem(__Commands_Read_ReadNwsrfsFS5Files_String, this) );
    }
    if ( __source_RCCACIS_enabled ) {
        __Commands_ReadTimeSeries_JMenu.add(__Commands_Read_ReadRccAcis_JMenuItem =
            new SimpleJMenuItem(__Commands_Read_ReadRccAcis_String, this) );
    }
    if ( __source_ReclamationHDB_enabled ) {
        __Commands_ReadTimeSeries_JMenu.add(__Commands_Read_ReadReclamationHDB_JMenuItem =
            new SimpleJMenuItem(__Commands_Read_ReadReclamationHDB_String, this) );
    }
    if ( __source_RiversideDB_enabled ) {
        __Commands_ReadTimeSeries_JMenu.add(__Commands_Read_ReadRiversideDB_JMenuItem =
            new SimpleJMenuItem(__Commands_Read_ReadRiversideDB_String, this) );
    }
    if ( __source_RiverWare_enabled ) {
        __Commands_ReadTimeSeries_JMenu.add(__Commands_Read_ReadRiverWare_JMenuItem =
            new SimpleJMenuItem(__Commands_Read_ReadRiverWare_String, this) );
    }
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
    __Commands_ReadTimeSeries_JMenu.add(__Commands_Read_ReadTimeSeries_JMenuItem =
        new SimpleJMenuItem(__Commands_Read_ReadTimeSeries_String, this) );
    if ( __source_UsgsNwisDaily_enabled ) {
        __Commands_ReadTimeSeries_JMenu.add(__Commands_Read_ReadUsgsNwisDaily_JMenuItem =
            new SimpleJMenuItem(__Commands_Read_ReadUsgsNwisDaily_String, this) );
    }
    if ( __source_UsgsNwisRdb_enabled ) {
        __Commands_ReadTimeSeries_JMenu.add(__Commands_Read_ReadUsgsNwisRdb_JMenuItem =
            new SimpleJMenuItem(__Commands_Read_ReadUsgsNwisRdb_String, this) );
    }
    if ( __source_WaterML_enabled ) {
        __Commands_ReadTimeSeries_JMenu.add(__Commands_Read_ReadWaterML_JMenuItem =
            new SimpleJMenuItem(__Commands_Read_ReadWaterML_String, this) );
    }
    if ( __source_WaterOneFlow_enabled ) {
        __Commands_ReadTimeSeries_JMenu.add(__Commands_Read_ReadWaterOneFlow_JMenuItem =
            new SimpleJMenuItem(__Commands_Read_ReadWaterOneFlow_String, this) );
    }
	if ( __source_StateMod_enabled ) {
		__Commands_ReadTimeSeries_JMenu.addSeparator ();
		__Commands_ReadTimeSeries_JMenu.add(__Commands_Read_StateModMax_JMenuItem =
			new SimpleJMenuItem(__Commands_Read_StateModMax_String, this) );
	}

	// "Commands...Fill Time Series"...

	__Commands_JMenu.add ( __Commands_FillTimeSeries_JMenu = new JMenu(__Commands_FillTimeSeries_String));
	__Commands_FillTimeSeries_JMenu.setToolTipText("Fill gaps in time series data.");

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

	__Commands_FillTimeSeries_JMenu.add (__Commands_Fill_FillMixedStation_JMenuItem =
        new SimpleJMenuItem(__Commands_Fill_FillMixedStation_String, this ) );

	__Commands_FillTimeSeries_JMenu.add (__Commands_Fill_FillMOVE2_JMenuItem =
        new SimpleJMenuItem(__Commands_Fill_FillMOVE2_String, this ) );

	__Commands_FillTimeSeries_JMenu.add (__Commands_Fill_FillPattern_JMenuItem =
        new SimpleJMenuItem(__Commands_Fill_FillPattern_String, this ) );
	
    __Commands_FillTimeSeries_JMenu.add (__Commands_Fill_ReadPatternFile_JMenuItem=
        new SimpleJMenuItem(__Commands_Fill_ReadPatternFile_String, this ) );

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

	__Commands_FillTimeSeries_JMenu.addSeparator();

	__Commands_FillTimeSeries_JMenu.add (__Commands_Fill_SetAutoExtendPeriod_JMenuItem =
		new SimpleJMenuItem(__Commands_Fill_SetAutoExtendPeriod_String, this ) );

	__Commands_FillTimeSeries_JMenu.add (__Commands_Fill_SetAveragePeriod_JMenuItem =
        new SimpleJMenuItem(__Commands_Fill_SetAveragePeriod_String, this ) );

	__Commands_FillTimeSeries_JMenu.add(__Commands_Fill_SetIgnoreLEZero_JMenuItem =
        new SimpleJMenuItem(__Commands_Fill_SetIgnoreLEZero_String, this ) );

	// "Commands...Set Time Series"...

	__Commands_JMenu.add ( __Commands_SetTimeSeries_JMenu=new JMenu(__Commands_SetTimeSeries_String));
	__Commands_SetTimeSeries_JMenu.setToolTipText("Set time series properties and data values.");

	__Commands_SetTimeSeries_JMenu.add (__Commands_Set_ReplaceValue_JMenuItem =
		new SimpleJMenuItem( __Commands_Set_ReplaceValue_String, this));
	__Commands_SetTimeSeries_JMenu.addSeparator();

	__Commands_SetTimeSeries_JMenu.add (__Commands_Set_SetConstant_JMenuItem =
		new SimpleJMenuItem( __Commands_Set_SetConstant_String, this ));

	__Commands_SetTimeSeries_JMenu.add (__Commands_Set_SetDataValue_JMenuItem =
        new SimpleJMenuItem(__Commands_Set_SetDataValue_String, this ) );

	__Commands_SetTimeSeries_JMenu.add (__Commands_Set_SetFromTS_JMenuItem =
        new SimpleJMenuItem(__Commands_Set_SetFromTS_String, this ) );

	__Commands_SetTimeSeries_JMenu.add (__Commands_Set_SetToMax_JMenuItem =
        new SimpleJMenuItem(__Commands_Set_SetToMax_String, this ) );
	
	__Commands_SetTimeSeries_JMenu.add (__Commands_Set_SetToMin_JMenuItem =
        new SimpleJMenuItem(__Commands_Set_SetToMin_String, this ) );
    
    __Commands_SetTimeSeries_JMenu.addSeparator ();
    __Commands_SetTimeSeries_JMenu.add ( __Commands_Set_SetTimeSeriesProperty_JMenuItem =
        new SimpleJMenuItem( __Commands_Set_SetTimeSeriesProperty_String, this ) );

	// "Commands...Manipulate Time Series"...

	__Commands_JMenu.add (__Commands_ManipulateTimeSeries_JMenu=new JMenu("Manipulate Time Series"));
	__Commands_ManipulateTimeSeries_JMenu.setToolTipText("Manipulate time series data values.");

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
	
	__Commands_ManipulateTimeSeries_JMenu.addSeparator();
    __Commands_ManipulateTimeSeries_JMenu.add ( __Commands_Manipulate_Free_JMenuItem =
        new SimpleJMenuItem(__Commands_Manipulate_Free_String, this ) );

	// "Commands...Analyze Time Series"...

	__Commands_JMenu.add ( __Commands_AnalyzeTimeSeries_JMenu= new JMenu(__Commands_AnalyzeTimeSeries_String) );
	__Commands_AnalyzeTimeSeries_JMenu.setToolTipText("Analyze time series data values.");
	__Commands_AnalyzeTimeSeries_JMenu.add ( __Commands_Analyze_AnalyzePattern_JMenuItem =
        new SimpleJMenuItem(__Commands_Analyze_AnalyzePattern_String, this ) );
    __Commands_AnalyzeTimeSeries_JMenu.add ( __Commands_Analyze_CalculateTimeSeriesStatistic_JMenuItem =
        new SimpleJMenuItem(__Commands_Analyze_CalculateTimeSeriesStatistic_String, this ) );
	__Commands_AnalyzeTimeSeries_JMenu.add (__Commands_Analyze_CompareTimeSeries_JMenuItem =
		new SimpleJMenuItem(__Commands_Analyze_CompareTimeSeries_String, this ) );
	
	__Commands_AnalyzeTimeSeries_JMenu.addSeparator ();
	__Commands_AnalyzeTimeSeries_JMenu.add (__Commands_Analyze_ComputeErrorTimeSeries_JMenuItem =
        new SimpleJMenuItem(__Commands_Analyze_ComputeErrorTimeSeries_String, this ) );

	// "Commands...Models"...

	__Commands_JMenu.add ( __Commands_Models_Routing_JMenu = new JMenu(__Commands_Models_Routing_String) );
	__Commands_Models_Routing_JMenu.setToolTipText("Route time series (lag and attenuate over time).");
	__Commands_Models_Routing_JMenu.add ( __Commands_Models_Routing_LagK_JMenuItem =
        new SimpleJMenuItem( __Commands_Models_Routing_LagK_String, this ) );
   __Commands_Models_Routing_JMenu.add ( __Commands_Models_Routing_VariableLagK_JMenuItem =
        new SimpleJMenuItem( __Commands_Models_Routing_VariableLagK_String, this ) );

	// "Commands...Output Time Series"...

	__Commands_JMenu.add ( __Commands_OutputTimeSeries_JMenu=new JMenu(__Commands_OutputTimeSeries_String) );
	__Commands_OutputTimeSeries_JMenu.setToolTipText("Write time series to files and databases.");

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

    if ( __source_HECDSS_enabled ) {
        __Commands_OutputTimeSeries_JMenu.add ( __Commands_Output_WriteHecDss_JMenuItem =
            new SimpleJMenuItem( __Commands_Output_WriteHecDss_String, this ) );
    }
	
	if ( __source_NWSCard_enabled ) {
		__Commands_OutputTimeSeries_JMenu.add ( __Commands_Output_WriteNwsCard_JMenuItem =
			new SimpleJMenuItem( __Commands_Output_WriteNwsCard_String, this ) );
	}
	
    if ( __source_ReclamationHDB_enabled ) {
        __Commands_OutputTimeSeries_JMenu.add( __Commands_Output_WriteReclamationHDB_JMenuItem =
            new SimpleJMenuItem(__Commands_Output_WriteReclamationHDB_String, this ) );
    }
    
    if ( __source_RiversideDB_enabled ) {
        __Commands_OutputTimeSeries_JMenu.add( __Commands_Output_WriteRiversideDB_JMenuItem =
            new SimpleJMenuItem(__Commands_Output_WriteRiversideDB_String, this ) );
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
	
    if ( __source_WaterML_enabled ) {
        __Commands_OutputTimeSeries_JMenu.add ( __Commands_Output_WriteWaterML_JMenuItem =
            new SimpleJMenuItem(__Commands_Output_WriteWaterML_String, this ) );
    }

	__Commands_OutputTimeSeries_JMenu.addSeparator ();

	__Commands_OutputTimeSeries_JMenu.add ( __Commands_Output_ProcessTSProduct_JMenuItem =
		new SimpleJMenuItem( __Commands_Output_ProcessTSProduct_String, this ) );
	
    __Commands_JMenu.add( __Commands_Check_CheckTimeSeries_JMenu = new JMenu( __Commands_Check_CheckingResults_String, true ) );
    __Commands_Check_CheckTimeSeries_JMenu.setToolTipText("Check time series against criteria.");
    __Commands_Check_CheckTimeSeries_JMenu.add ( __Commands_Check_CheckingResults_CheckTimeSeries_JMenuItem =
        new SimpleJMenuItem( __Commands_Check_CheckingResults_CheckTimeSeries_String, this ) );
    __Commands_Check_CheckTimeSeries_JMenu.add ( __Commands_Check_CheckingResults_CheckTimeSeriesStatistic_JMenuItem =
        new SimpleJMenuItem( __Commands_Check_CheckingResults_CheckTimeSeriesStatistic_String, this ) );
    __Commands_Check_CheckTimeSeries_JMenu.add ( __Commands_Check_CheckingResults_WriteCheckFile_JMenuItem =
        new SimpleJMenuItem( __Commands_Check_CheckingResults_WriteCheckFile_String, this ) );
}

/**
Initialize the GUI "Commands...General".
*/
private void ui_InitGUIMenus_CommandsGeneral ()
{	__Commands_JMenu.addSeparator(); // Results in double separator
    if ( __source_HydroBase_enabled ) {
		__Commands_JMenu.addSeparator();
		__Commands_JMenu.add( __Commands_HydroBase_JMenu = new JMenu( __Commands_HydroBase_String, true ) );
		__Commands_HydroBase_JMenu.setToolTipText("Additional HydroBase database commands.");
		__Commands_HydroBase_JMenu.add (__Commands_HydroBase_OpenHydroBase_JMenuItem =
			new SimpleJMenuItem(__Commands_HydroBase_OpenHydroBase_String, this ) );
	}

    // "Commands...Ensemble processing"...
    
    __Commands_JMenu.addSeparator();
    __Commands_JMenu.add ( __Commands_Ensemble_JMenu = new JMenu(__Commands_Ensemble_String) );
    __Commands_Ensemble_JMenu.setToolTipText("Process ensembles (groups of related time series).");
    __Commands_Ensemble_JMenu.add( __Commands_Ensemble_CreateEnsembleFromOneTimeSeries_JMenuItem =
        new SimpleJMenuItem( __Commands_Ensemble_CreateEnsembleFromOneTimeSeries_String, this) );
    __Commands_Ensemble_JMenu.add( __Commands_Ensemble_CopyEnsemble_JMenuItem =
        new SimpleJMenuItem( __Commands_Ensemble_CopyEnsemble_String, this) );
    __Commands_Ensemble_JMenu.add( __Commands_Ensemble_NewEnsemble_JMenuItem =
        new SimpleJMenuItem( __Commands_Ensemble_NewEnsemble_String, this) );
    
    if ( __source_NWSRFS_ESPTraceEnsemble_enabled ) {
        __Commands_Ensemble_JMenu.add( __Commands_Ensemble_ReadNwsrfsEspTraceEnsemble_JMenuItem =
        new SimpleJMenuItem( __Commands_Ensemble_ReadNwsrfsEspTraceEnsemble_String, this) );
    }
    
    __Commands_Ensemble_JMenu.addSeparator();
    __Commands_Ensemble_JMenu.add( __Commands_Ensemble_InsertTimeSeriesIntoEnsemble_JMenuItem =
        new SimpleJMenuItem( __Commands_Ensemble_InsertTimeSeriesIntoEnsemble_String, this) );

    __Commands_Ensemble_JMenu.addSeparator();
    __Commands_Ensemble_JMenu.add ( __Commands_Ensemble_NewStatisticEnsemble_JMenuItem =
        new SimpleJMenuItem( __Commands_Ensemble_NewStatisticEnsemble_String, this ) );
    __Commands_Ensemble_JMenu.add ( __Commands_Ensemble_NewStatisticTimeSeriesFromEnsemble_JMenuItem =
        new SimpleJMenuItem( __Commands_Ensemble_NewStatisticTimeSeriesFromEnsemble_String, this ) );
    
    __Commands_Ensemble_JMenu.add ( __Commands_Ensemble_WeightTraces_JMenuItem =
        new SimpleJMenuItem(__Commands_Ensemble_WeightTraces_String, this ) );
    if ( __source_NWSRFS_ESPTraceEnsemble_enabled ) {
        __Commands_Ensemble_JMenu.addSeparator();
        __Commands_Ensemble_JMenu.add ( __Commands_Ensemble_WriteNwsrfsEspTraceEnsemble_JMenuItem=
            new SimpleJMenuItem(__Commands_Ensemble_WriteNwsrfsEspTraceEnsemble_String,this ));
    }
    
    // Commands...Table processing...
    
    __Commands_JMenu.addSeparator();
    __Commands_JMenu.add( __Commands_Table_JMenu = new JMenu( __Commands_Table_String, true ) );
    __Commands_Table_JMenu.setToolTipText("Process tables (columns of data).");
    __Commands_Table_JMenu.add( __Commands_Table_NewTable_JMenuItem =
        new SimpleJMenuItem( __Commands_Table_NewTable_String, this ) );
    __Commands_Table_JMenu.add( __Commands_Table_CopyTable_JMenuItem =
        new SimpleJMenuItem( __Commands_Table_CopyTable_String, this ) );
    __Commands_Table_JMenu.add( __Commands_Table_ReadTableFromDataStore_JMenuItem =
        new SimpleJMenuItem( __Commands_Table_ReadTableFromDataStore_String, this ) );
    __Commands_Table_JMenu.add( __Commands_Table_ReadTableFromDelimitedFile_JMenuItem =
        new SimpleJMenuItem( __Commands_Table_ReadTableFromDelimitedFile_String, this ) );
    __Commands_Table_JMenu.add( __Commands_Table_ReadTableFromDBF_JMenuItem =
        new SimpleJMenuItem( __Commands_Table_ReadTableFromDBF_String, this ) );
    __Commands_Table_JMenu.add( __Commands_Table_TimeSeriesToTable_JMenuItem =
        new SimpleJMenuItem( __Commands_Table_TimeSeriesToTable_String, this ) );
    __Commands_Table_JMenu.addSeparator();
    __Commands_Table_JMenu.add( __Commands_Table_ManipulateTableString_JMenuItem =
        new SimpleJMenuItem( __Commands_Table_ManipulateTableString_String, this ) );
    __Commands_Table_JMenu.add( __Commands_Table_TableMath_JMenuItem =
        new SimpleJMenuItem( __Commands_Table_TableMath_String, this ) );
    __Commands_Table_JMenu.add( __Commands_Table_TableTimeSeriesMath_JMenuItem =
        new SimpleJMenuItem( __Commands_Table_TableTimeSeriesMath_String, this ) );
    __Commands_Table_JMenu.addSeparator();
    __Commands_Table_JMenu.add( __Commands_Table_SetTimeSeriesPropertiesFromTable_JMenuItem =
        new SimpleJMenuItem( __Commands_Table_SetTimeSeriesPropertiesFromTable_String, this ) );
    __Commands_Table_JMenu.add( __Commands_Table_CopyTimeSeriesPropertiesToTable_JMenuItem =
        new SimpleJMenuItem( __Commands_Table_CopyTimeSeriesPropertiesToTable_String, this ) );
    __Commands_Table_JMenu.addSeparator();
    __Commands_Table_JMenu.add( __Commands_Table_CompareTables_JMenuItem =
        new SimpleJMenuItem( __Commands_Table_CompareTables_String, this ) );
    __Commands_Table_JMenu.add( __Commands_Table_WriteTableToDelimitedFile_JMenuItem =
        new SimpleJMenuItem( __Commands_Table_WriteTableToDelimitedFile_String, this ) );
    __Commands_Table_JMenu.add( __Commands_Table_WriteTableToHTML_JMenuItem =
        new SimpleJMenuItem( __Commands_Table_WriteTableToHTML_String, this ) );
    
    // Commands...Template processing...
    
    __Commands_JMenu.addSeparator();
    __Commands_JMenu.add( __Commands_Template_JMenu = new JMenu( __Commands_Template_String, true ) );
    __Commands_Template_JMenu.setToolTipText("Process templates (to handle dynamic logic and data).");
    __Commands_Template_JMenu.add( __Commands_Template_ExpandTemplateFile_JMenuItem =
        new SimpleJMenuItem( __Commands_Template_ExpandTemplateFile_String, this ) );
    
    // Commands...View processing...
    
    __Commands_JMenu.addSeparator();
    __Commands_JMenu.add( __Commands_View_JMenu = new JMenu( __Commands_View_String, true ) );
    __Commands_View_JMenu.setToolTipText("Process views (representations of results).");
    __Commands_View_JMenu.add( __Commands_View_NewTreeView_JMenuItem =
        new SimpleJMenuItem( __Commands_View_NewTreeView_String, this ) );
    
    // Commands...General...

	__Commands_JMenu.addSeparator(); // Separate general commands from others
	__Commands_JMenu.addSeparator(); // Results in double separator
	
    __Commands_JMenu.add( __Commands_General_Comments_JMenu = new JMenu( __Commands_General_Comments_String, true ) );
    __Commands_General_Comments_JMenu.setToolTipText("Insert comments.");
    __Commands_General_Comments_JMenu.add ( __Commands_General_Comments_Comment_JMenuItem =
        new SimpleJMenuItem( __Commands_General_Comments_Comment_String, this ) );
    __Commands_General_Comments_JMenu.add (__Commands_General_Comments_StartComment_JMenuItem =
        new SimpleJMenuItem( __Commands_General_Comments_StartComment_String, this ) );
    __Commands_General_Comments_JMenu.add( __Commands_General_Comments_EndComment_JMenuItem =
        new SimpleJMenuItem(__Commands_General_Comments_EndComment_String, this ) );
    __Commands_General_Comments_JMenu.addSeparator();
    __Commands_General_Comments_JMenu.add (__Commands_General_Comments_ReadOnlyComment_JMenuItem =
        new SimpleJMenuItem( __Commands_General_Comments_ReadOnlyComment_String, this ) );
    __Commands_General_Comments_JMenu.add (__Commands_General_Comments_ExpectedStatusFailureComment_JMenuItem =
        new SimpleJMenuItem( __Commands_General_Comments_ExpectedStatusFailureComment_String, this ) );
    __Commands_General_Comments_JMenu.add (__Commands_General_Comments_ExpectedStatusWarningComment_JMenuItem =
        new SimpleJMenuItem( __Commands_General_Comments_ExpectedStatusWarningComment_String, this ) );
    
    __Commands_JMenu.add( __Commands_General_FileHandling_JMenu = new JMenu( __Commands_General_FileHandling_String, true ) );
    __Commands_General_FileHandling_JMenu.setToolTipText("Manipulate files.");
    __Commands_General_FileHandling_JMenu.add ( __Commands_General_FileHandling_FTPGet_JMenuItem =
        new SimpleJMenuItem( __Commands_General_FileHandling_FTPGet_String, this ) );
    __Commands_General_FileHandling_JMenu.add ( __Commands_General_FileHandling_WebGet_JMenuItem =
        new SimpleJMenuItem( __Commands_General_FileHandling_WebGet_String, this ) );
    __Commands_General_FileHandling_JMenu.addSeparator();
    __Commands_General_FileHandling_JMenu.add ( __Commands_General_FileHandling_AppendFile_JMenuItem =
        new SimpleJMenuItem( __Commands_General_FileHandling_AppendFile_String, this ) );
    __Commands_General_FileHandling_JMenu.add ( __Commands_General_FileHandling_RemoveFile_JMenuItem =
        new SimpleJMenuItem( __Commands_General_FileHandling_RemoveFile_String, this ) );
    __Commands_General_FileHandling_JMenu.addSeparator();
    __Commands_General_FileHandling_JMenu.add ( __Commands_General_FileHandling_PrintTextFile_JMenuItem =
        new SimpleJMenuItem( __Commands_General_FileHandling_PrintTextFile_String, this ) );
    
	__Commands_JMenu.add( __Commands_General_Logging_JMenu = new JMenu( __Commands_General_Logging_String, true ) );	
	__Commands_General_Logging_JMenu.setToolTipText("Control logging (tracking).");
	__Commands_General_Logging_JMenu.add(__Commands_General_Logging_StartLog_JMenuItem =
        new SimpleJMenuItem(__Commands_General_Logging_StartLog_String, this ) );
	__Commands_General_Logging_JMenu.add (__Commands_General_Logging_SetDebugLevel_JMenuItem =
        new SimpleJMenuItem(__Commands_General_Logging_SetDebugLevel_String, this ) );
	__Commands_General_Logging_JMenu.add ( __Commands_General_Logging_SetWarningLevel_JMenuItem =
		new SimpleJMenuItem(__Commands_General_Logging_SetWarningLevel_String, this ) );

    __Commands_JMenu.add( __Commands_General_Running_JMenu = new JMenu( __Commands_General_Running_String, true ) );
    __Commands_General_Running_JMenu.setToolTipText("Run external programs, command files, Python.");
    __Commands_General_Running_JMenu.add ( __Commands_General_Running_ReadPropertiesFromFile_JMenuItem =
        new SimpleJMenuItem(__Commands_General_Running_ReadPropertiesFromFile_String, this ) );
    __Commands_General_Running_JMenu.add (__Commands_General_Running_SetProperty_JMenuItem =
        new SimpleJMenuItem( __Commands_General_Running_SetProperty_String,this));
    if ( __source_NWSRFS_FS5Files_enabled ) {
        __Commands_General_Running_JMenu.add (__Commands_General_Running_SetPropertyFromNwsrfsAppDefault_JMenuItem =
        new SimpleJMenuItem( __Commands_General_Running_SetPropertyFromNwsrfsAppDefault_String,this));
    }
    __Commands_General_Running_JMenu.add (__Commands_General_Running_FormatDateTimeProperty_JMenuItem =
        new SimpleJMenuItem( __Commands_General_Running_FormatDateTimeProperty_String,this));
    __Commands_General_Running_JMenu.add ( __Commands_General_Running_WritePropertiesToFile_JMenuItem =
        new SimpleJMenuItem(__Commands_General_Running_WritePropertiesToFile_String, this ) );
    __Commands_General_Running_JMenu.addSeparator();
    __Commands_General_Running_JMenu.add (__Commands_General_Running_RunCommands_JMenuItem =
        new SimpleJMenuItem( __Commands_General_Running_RunCommands_String,this));
    __Commands_General_Running_JMenu.add ( __Commands_General_Running_RunProgram_JMenuItem =
        new SimpleJMenuItem(__Commands_General_Running_RunProgram_String,this));
    __Commands_General_Running_JMenu.add ( __Commands_General_Running_RunPython_JMenuItem =
        new SimpleJMenuItem(__Commands_General_Running_RunPython_String,this));
    __Commands_General_Running_JMenu.add ( __Commands_General_Running_RunDSSUTL_JMenuItem =
        new SimpleJMenuItem(__Commands_General_Running_RunDSSUTL_String,this));
    __Commands_General_Running_JMenu.addSeparator();
    __Commands_General_Running_JMenu.add ( __Commands_General_Running_Exit_JMenuItem =
        new SimpleJMenuItem(__Commands_General_Running_Exit_String, this ) );
	__Commands_General_Running_JMenu.add(__Commands_General_Running_SetWorkingDir_JMenuItem =
        new SimpleJMenuItem( __Commands_General_Running_SetWorkingDir_String, this ) );

    __Commands_JMenu.add( __Commands_General_TestProcessing_JMenu = new JMenu( __Commands_General_TestProcessing_String, true ) );
    __Commands_General_TestProcessing_JMenu.setToolTipText("Test the software and processes.");
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
	__Commands_JPopupMenu.addSeparator();
	__Commands_JPopupMenu.add( __CommandsPopup_ConvertSelectedCommandsToComments_JMenuItem =
		new SimpleJMenuItem ( __Edit_ConvertSelectedCommandsToComments_String, this ) );
	__Commands_JPopupMenu.add( __CommandsPopup_ConvertSelectedCommandsFromComments_JMenuItem =
		new SimpleJMenuItem ( __Edit_ConvertSelectedCommandsFromComments_String, this ) );
    __Commands_JPopupMenu.add( __CommandsPopup_ConvertTSIDTo_ReadTimeSeries_JMenuItem =
        new SimpleJMenuItem ( __Edit_ConvertTSIDTo_ReadTimeSeries_String, this ) );
    __Commands_JPopupMenu.add( __CommandsPopup_ConvertTSIDTo_ReadCommand_JMenuItem =
        new SimpleJMenuItem ( __Edit_ConvertTSIDTo_ReadCommand_String, this ) );
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

	__Edit_JMenu.add(
	    __Edit_CommandWithErrorChecking_JMenuItem = new SimpleJMenuItem(__Edit_CommandWithErrorChecking_String, this ) );

	__Edit_JMenu.addSeparator( );
	__Edit_JMenu.add(__Edit_ConvertSelectedCommandsToComments_JMenuItem=
		new SimpleJMenuItem (__Edit_ConvertSelectedCommandsToComments_String, this ) );
	__Edit_JMenu.add( __Edit_ConvertSelectedCommandsFromComments_JMenuItem =
		new SimpleJMenuItem (__Edit_ConvertSelectedCommandsFromComments_String, this ) );
    __Edit_JMenu.add(__Edit_ConvertTSIDTo_ReadTimeSeries_JMenuItem=
        new SimpleJMenuItem (__Edit_ConvertTSIDTo_ReadTimeSeries_String, this ) );
    __Edit_JMenu.add( __Edit_ConvertTSIDTo_ReadCommand_JMenuItem =
        new SimpleJMenuItem (__Edit_ConvertTSIDTo_ReadCommand_String, this ) );
}

/**
Initialize the GUI "File" menu.
*/
private void ui_InitGUIMenus_File ( JMenuBar menu_bar )
{	__File_JMenu = new JMenu( __File_String, true );
	menu_bar.add( __File_JMenu );
	
	__File_JMenu.add( __File_New_JMenu=new JMenu(__File_New_String,true));
    __File_New_JMenu.add( __File_New_CommandFile_JMenuItem =
        new SimpleJMenuItem( __File_New_CommandFile_String, this ) );

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
	__File_Save_JMenu.add ( __File_Save_CommandsAsVersion9_JMenuItem =
	   new SimpleJMenuItem( __File_Save_CommandsAsVersion9_String, this ) );
	__File_Save_CommandsAsVersion9_JMenuItem.setToolTipText ( "Save old TS Alias = Command(...) syntax" );
	__File_Save_JMenu.addSeparator();
	__File_Save_JMenu.add (	__File_Save_TimeSeriesAs_JMenuItem =
        new SimpleJMenuItem(__File_Save_TimeSeriesAs_String, this ) );

	__File_JMenu.add( __File_Print_JMenu=new JMenu(__File_Print_String,true));
    __File_Print_JMenu.add ( __File_Print_Commands_JMenuItem =
        new SimpleJMenuItem( __File_Print_Commands_String,__File_Print_Commands_String, this ) );

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
	__Help_JMenu.add ( __Help_AboutTSTool_JMenuItem = new SimpleJMenuItem(__Help_AboutTSTool_String,this));
	__Help_JMenu.addSeparator();
    __Help_JMenu.add ( __Help_ViewDocumentation_JMenuItem = new SimpleJMenuItem(__Help_ViewDocumentation_String,this));
    __Help_JMenu.add ( __Help_ViewTrainingMaterials_JMenuItem = new SimpleJMenuItem(__Help_ViewTrainingMaterials_String,this));
    __Help_JMenu.addSeparator();
    __Help_JMenu.add ( __Help_ImportConfiguration_JMenuItem = new SimpleJMenuItem(__Help_ImportConfiguration_String,this));
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

    __Results_JMenu.add( __Results_Graph_Area_JMenuItem =
        new SimpleJMenuItem( __Results_Graph_Area_String, this ) );
    
    __Results_JMenu.add( __Results_Graph_AreaStacked_JMenuItem =
        new SimpleJMenuItem( __Results_Graph_AreaStacked_String, this ) );

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
        new SimpleJMenuItem( __Results_Report_Summary_Html_String, this ) );
	__Results_JMenu.add(__Results_Report_Summary_JMenuItem =
        new SimpleJMenuItem( __Results_Report_Summary_Text_String, this ) );

	__Results_JMenu.addSeparator();
	__Results_JMenu.add(__Results_TimeSeriesProperties_JMenuItem =
        new SimpleJMenuItem( __Results_TimeSeriesProperties_String, this ) );
}
   
/**
Define the popup menus for results.
*/
private void ui_InitGUIMenus_ResultsPopup ()
{	__resultsTS_JPopupMenu = new JPopupMenu("TS Results Actions");
    __resultsTS_JPopupMenu.add( new SimpleJMenuItem ( __Results_Graph_Area_String, this ) );
    __resultsTS_JPopupMenu.add( new SimpleJMenuItem ( __Results_Graph_AreaStacked_String, this ) );
	__resultsTS_JPopupMenu.add( new SimpleJMenuItem ( __Results_Graph_BarsLeft_String, this ) );
	__resultsTS_JPopupMenu.add( new SimpleJMenuItem ( __Results_Graph_BarsCenter_String,this));
	__resultsTS_JPopupMenu.add( new SimpleJMenuItem ( __Results_Graph_BarsRight_String, this ));
	__resultsTS_JPopupMenu.add( new SimpleJMenuItem ( __Results_Graph_Duration_String, this ));
	__resultsTS_JPopupMenu.add( new SimpleJMenuItem ( __Results_Graph_Line_String, this ) );
	__resultsTS_JPopupMenu.add( new SimpleJMenuItem ( __Results_Graph_LineLogY_String, this ) );
	__resultsTS_JPopupMenu.add( new SimpleJMenuItem ( __Results_Graph_PeriodOfRecord_String, this ) );
	__resultsTS_JPopupMenu.add( new SimpleJMenuItem ( __Results_Graph_Point_String, this ) );
	__resultsTS_JPopupMenu.add( new SimpleJMenuItem ( __Results_Graph_PredictedValue_String, this));
	__resultsTS_JPopupMenu.add( new SimpleJMenuItem ( __Results_Graph_PredictedValueResidual_String, this));
	__resultsTS_JPopupMenu.add( new SimpleJMenuItem ( __Results_Graph_XYScatter_String, this));
	__resultsTS_JPopupMenu.addSeparator();
	__resultsTS_JPopupMenu.add( new SimpleJMenuItem ( __Results_Table_String, this ) );
	__resultsTS_JPopupMenu.addSeparator();
	__resultsTS_JPopupMenu.add( new SimpleJMenuItem ( __Results_Report_Summary_Html_String, this ) );
	__resultsTS_JPopupMenu.add( new SimpleJMenuItem ( __Results_Report_Summary_Text_String, this ) );
	__resultsTS_JPopupMenu.addSeparator();
	__resultsTS_JPopupMenu.add( new SimpleJMenuItem ( __Results_FindTimeSeries_String, this ) );
	__resultsTS_JPopupMenu.add( new SimpleJMenuItem ( BUTTON_TS_SELECT_ALL, this ) );
	__resultsTS_JPopupMenu.add( new SimpleJMenuItem ( BUTTON_TS_DESELECT_ALL, this ) );
	__resultsTS_JPopupMenu.addSeparator();
	__resultsTS_JPopupMenu.add( new SimpleJMenuItem ( __Results_TimeSeriesProperties_String, this ) );
    
    ActionListener_ResultsEnsembles ens_l = new ActionListener_ResultsEnsembles();
    __resultsTSEnsembles_JPopupMenu = new JPopupMenu("Ensembles Results Actions");
    /* TODO SAM 2008-01-05 Evaluate what to enable, maybe add exceedance plot, etc.
    __results_ts_JPopupMenu.add( new SimpleJMenuItem (  __Results_Graph_Area_String, this ) );
    __results_ts_JPopupMenu.add( new SimpleJMenuItem (  __Results_Graph_AreaStacked_String, this ) );
    __results_ts_JPopupMenu.add( new SimpleJMenuItem (  __Results_Graph_BarsLeft_String, this ) );
    __results_ts_JPopupMenu.add( new SimpleJMenuItem (  __Results_Graph_BarsCenter_String,this));
    __results_ts_JPopupMenu.add( new SimpleJMenuItem (  __Results_Graph_BarsRight_String, this ));
    __results_ts_JPopupMenu.add( new SimpleJMenuItem (  __Results_Graph_Duration_String, this ));
    */
    __resultsTSEnsembles_JPopupMenu.add( new SimpleJMenuItem ( __Results_Ensemble_Graph_Line_String, ens_l ) );
    __resultsTSEnsembles_JPopupMenu.add( new SimpleJMenuItem ( __Results_Ensemble_Table_String, ens_l ) );
    __resultsTSEnsembles_JPopupMenu.add( new SimpleJMenuItem ( __Results_Ensemble_Properties_String, ens_l ) );
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
	/* TODO SAM 2009-04-10 Evaluate whether to allow this - the problem is that although the command processor
	 * is run on a thread, the individual commands are not and therefore if a process hangs, it is hung in the
	 * processor and the Process instances cannot be retrieved
    __Run_JMenu.add ( __Run_CancelAllCommandProcesses_JMenuItem =
        new SimpleJMenuItem(__Run_CancelAllCommandProcesses_String,this));
             */
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

	__Tools_Analysis_JMenu.add(	__Tools_Analysis_PrincipalComponentAnalysis_JMenuItem =
		new SimpleJMenuItem(__Tools_Analysis_PrincipalComponentAnalysis_String, this ) );

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
{	__View_JMenu = new JMenu (__View_String, true);
	menuBar.add (__View_JMenu);

    __View_JMenu.add ( __View_DataStores_JMenuItem=new SimpleJMenuItem( __View_DataStores_String, this));
	__View_JMenu.add ( __View_DataUnits_JMenuItem=new SimpleJMenuItem( __View_DataUnits_String, this));
    __View_JMenu.add ( __View_MapInterface_JCheckBoxMenuItem =  new JCheckBoxMenuItem(__View_Map_String) );
    __View_MapInterface_JCheckBoxMenuItem.setState ( false );
    __View_MapInterface_JCheckBoxMenuItem.addItemListener ( this );
    __View_JMenu.addSeparator();
    __View_JMenu.add ( __View_CloseAllViewWindows_JMenuItem=new SimpleJMenuItem( __View_CloseAllViewWindows_String, this));
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
        __toolbarNew_JButton.setToolTipText ( "<html>" + New_CommandFile_String + "</html>" );
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
        __toolbarOpen_JButton.setToolTipText ( "<html>" + Open_CommandFile_String + "/html>" );
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
        __toolbarSave_JButton.setToolTipText ( "<html>" + Save_CommandFile_String + "/html>" );
        toolbar.add(__toolbarSave_JButton);
    }
    // FIXME SAM 2007-12-04 Add when some of the other layout changes - adding split pane to separate
    // commands and results, etc.  Right now this conflicts with the query panel.
    //getContentPane().add ("North", toolbar);
}

/**
Load a command file and display in the command list.
@param commandFile Full path to command file to load.
@param runOnLoad If true, the commands will be run after loading.
*/
private void ui_LoadCommandFile ( String commandFile, boolean runOnLoad )
{   String routine = "TSTool_JFrame.ui_LoadCommandFile";
    int numAutoChanges = 0; // Number of lines automatically changed during load
    try {
        numAutoChanges = commandProcessor_ReadCommandFile ( commandFile );
        // Add progress listeners to the commands
        for ( Command command : __tsProcessor.getCommands() ) {
            // Connect the command to the UI to handle progress when the command is run.
            // TODO SAM 2009-03-23 Evaluate whether to define and interface rather than rely on
            // AbstractCommand here - too much overhead as is when only some commands implement.
            if ( command instanceof AbstractCommand ) {
                ((AbstractCommand)command).addCommandProgressListener ( this );
            }
        }
        // Repaint the list to reflect the status of the commands...
        ui_ShowCurrentCommandListStatus();
    }
    catch ( FileNotFoundException e ) {
        Message.printWarning ( 1, routine, "Command file \"" + commandFile + "\" does not exist." );
        Message.printWarning ( 3, routine, e );
        // Previous contents will remain.
        return;
    }
    catch ( IOException e ) {
        Message.printWarning ( 1, routine, "Error reading command file \"" + commandFile +
            "\".  List of commands may be incomplete." );
        Message.printWarning ( 3, routine, e );
        // Previous contents will remain.
        return;
    }
    catch ( Exception e ) {
        // FIXME SAM 2007-10-09 Perhaps should revert to previous
        // data model contents?  For now allow partical contents to be displayed.
        //
        // Error opening the file (should not happen but maybe a read permissions problem)...
        Message.printWarning ( 1, routine,
        "Unexpected error reading command file \"" + commandFile +
        "\".  Displaying commands that could be read." );
        Message.printWarning ( 3, routine, e );
    }
    // If successful the TSCommandProcessor, as the data model, will
    // have fired actions to make the JList update.
    commandList_SetCommandFileName(commandFile);
    if ( numAutoChanges == 0 ) {
        commandList_SetDirty(false);
    }
    // Clear the old results...
    results_Clear();
    ui_UpdateStatusTextFields ( 2, null, null, "Use the Run menu/buttons to run the commands.", __STATUS_READY );
    __processor_JProgressBar.setValue ( 0 );
    __command_JProgressBar.setValue ( 0 );
    // If requested, run the commands.
    if ( runOnLoad ) {
        // Run all commands and create output.
        uiAction_RunCommands ( true, true );
        // This will update the status text fields
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
Set the HydroBaseDMI instance used by the GUI by encapsulating in a HydroBaseDataStore instance.
This typically is the same as the command processor HydroBaseDMI;
however, it is possible that OpenHydroBase() commands will be used and open up different HydroBase
connections during processing.
@param hbdmi the HydroBaseDMI instance used by the GUI.
*/
private void ui_SetHydroBaseDataStore ( HydroBaseDMI hbdmi )
{
    if ( hbdmi == null ) {
        __hbDataStore = null;
    }
    else {
        __hbDataStore = new HydroBaseDataStore( "HydroBase", "State of Colorado HydroBase database", hbdmi );
    }
}

/**
Set whether ActionEvents should be ignored (or not).  In general they should
not be ignored but in some cases when programatically modifying data models
the spurious events do not need to trigger other actions.
@param ignore whether to ignore ActionEvents.
*/
private void ui_SetIgnoreActionEvent ( boolean ignore )
{
	__ignoreActionEvent = ignore;
}

/**
Set whether ItemEvents should be ignored (or not).  In general they should
not be ignored but in some cases when programatically modifying data models
the spurious events do not need to trigger other actions.
@param ignore whether to ignore ActionEvents.
*/
private void ui_SetIgnoreItemEvent ( boolean ignore )
{
	__ignoreItemEvent = ignore;
}

/**
Set whether ListSelectionEvents should be ignored (or not).  In general they should
not be ignored but in some cases when programatically modifying data models
the spurious events do not need to trigger other actions.
@param ignore whether to ignore ActionEvents.
*/
private void ui_SetIgnoreListSelectionEvent ( boolean ignore )
{
	__ignoreListSelectionEvent = ignore;
}

/**
Set the initial working directory, which will be the software startup home or
the location where the command file has been read/saved.
@param initialWorkingDir The initial working directory (should be non-null).
*/
private void ui_SetInitialWorkingDir ( String initialWorkingDir )
{	String routine = getClass().getName() + ".ui_SetInitialWorkingDir";
	Message.printStatus(2, routine, "Setting the initial working directory to \"" + initialWorkingDir + "\"" );
	__initialWorkingDir = initialWorkingDir;
	// Also set in the processor...
	commandProcessor_SetInitialWorkingDir ( initialWorkingDir, true );
}

/**
Set the input filters based on the current settings.  This sets the appropriate
input filter visible since all input filters are created at startup or when a data store is opened.
*/
private void ui_SetInputFilters()
{	String routine = getClass().getName() + ".ui_SetInputFilters";
    String selectedDataStoreName = null;
    DataStore selectedDataStore = ui_GetSelectedDataStore();
    if ( selectedDataStore != null ) {
        selectedDataStoreName = selectedDataStore.getName();
    }
    JPanel selectedInputFilter_JPanel = null; // If not set at end, will use generic panel
    String selectedInputType = ui_GetSelectedInputType();
    String selectedDataType = ui_GetSelectedDataType();
    String selectedTimeStep = ui_GetSelectedTimeStep();
    Message.printStatus(2, routine, "Setting input filter based on selected data store \"" +
        selectedDataStoreName + "\", input type \"" + selectedInputType +
        "\", and data type \"" + selectedDataType + "\"" );
    try {
    if ( selectedDataStore != null ) {
        // This handles input filters associated with data stores
        selectedInputFilter_JPanel =
            ui_GetInputFilterPanelForDataStoreName(selectedDataStore.getName(), selectedDataType);
    }
    else if(selectedInputType.equals(__INPUT_TYPE_HECDSS) && (__inputFilterHecDss_JPanel != null) ) {
        selectedInputFilter_JPanel = __inputFilterHecDss_JPanel;
    }
    else if ( selectedInputType.equals(__INPUT_TYPE_HydroBase) ) {
		// Can only use the HydroBase filters if they were originally
		// set up (if HydroBase was originally available).
        // The following lookups are currently hard coded and not read from HydroBase
		String [] hb_mt = HydroBase_Util.convertToHydroBaseMeasType( selectedDataType, selectedTimeStep );
		String meas_type = hb_mt[0];
		//String vax_field = hb_mt[1];
		//String time_step = hb_mt[2];
		if ( ui_GetHydroBaseDataStore() != null ) {
    		Message.printStatus(2, routine, "isStationTimeSeriesDataType("+ selectedDataType
    			+ "," + selectedTimeStep + "," + meas_type +
    			")=" + HydroBase_Util.isStationTimeSeriesDataType ( ui_GetHydroBaseDMI(), meas_type));
		}
		if ( ui_GetHydroBaseDataStore() == null ) {
		    // Display a message in the input filter panel area that a database connection needs to be made.
		    selectedInputFilter_JPanel = ui_GetInputFilterMessageJPanel (
		        "HydroBase connection is not available.\nUse File...Open...HydroBase.");
		}
		else if ( (__inputFilterHydroBaseStation_JPanel != null) &&
		    HydroBase_Util.isStationTimeSeriesDataType ( ui_GetHydroBaseDMI(), meas_type) ) {
			selectedInputFilter_JPanel = __inputFilterHydroBaseStation_JPanel;
		}
		// Call this before the more general isStructureTimeSeriesDataType() method...
		else if ( (__inputFilterHydroBaseStructureSfut_JPanel != null) &&
		    HydroBase_Util.isStructureSFUTTimeSeriesDataType ( ui_GetHydroBaseDMI(), meas_type) ) {
			selectedInputFilter_JPanel = __inputFilterHydroBaseStructureSfut_JPanel;
		}
		else if ( (__inputFilterHydroBaseStructure_JPanel != null) &&
		    HydroBase_Util.isStructureTimeSeriesDataType ( ui_GetHydroBaseDMI(), meas_type) ) {
			selectedInputFilter_JPanel = __inputFilterHydroBaseStructure_JPanel;
		}
		else if ((__inputFilterHydroBaseCASSCropStats_JPanel != null)
			&& HydroBase_Util.isAgriculturalCASSCropStatsTimeSeriesDataType ( ui_GetHydroBaseDMI(), selectedDataType) ) {
			//Message.printStatus (2, "","Displaying CASS crop stats panel");
			selectedInputFilter_JPanel = __inputFilterHydroBaseCASSCropStats_JPanel;
		}
		else if ((__inputFilterHydroBaseCASSLivestockStats_JPanel != null) &&
		    HydroBase_Util.isAgriculturalCASSLivestockStatsTimeSeriesDataType ( ui_GetHydroBaseDMI(), selectedDataType) ) {
			//Message.printStatus (2, "","Displaying CASS livestock stats panel");
			selectedInputFilter_JPanel = __inputFilterHydroBaseCASSLivestockStats_JPanel;
		}
		else if ((__inputFilterHydroBaseCUPopulation_JPanel != null) &&
		    HydroBase_Util.isCUPopulationTimeSeriesDataType ( ui_GetHydroBaseDMI(), selectedDataType) ) {
			//Message.printStatus (2, "","Displaying CU population panel");
			selectedInputFilter_JPanel = __inputFilterHydroBaseCUPopulation_JPanel;
		}
		else if ( (__inputFilterHydroBaseNASS_JPanel != null) &&
			HydroBase_Util.isAgriculturalNASSCropStatsTimeSeriesDataType ( ui_GetHydroBaseDMI(), selectedDataType) ) {
			//Message.printStatus (2, "","Displaying NASS agstats panel");
			selectedInputFilter_JPanel = __inputFilterHydroBaseNASS_JPanel;
		}
		else if ( (__inputFilterHydroBaseIrrigts_JPanel != null) &&
			HydroBase_Util.isIrrigSummaryTimeSeriesDataType ( ui_GetHydroBaseDMI(), selectedDataType) ) {
			selectedInputFilter_JPanel = __inputFilterHydroBaseIrrigts_JPanel;
		}
		else if ((__inputFilterHydroBaseWells_JPanel != null) 
		    && HydroBase_Util.isGroundWaterWellTimeSeriesDataType( ui_GetHydroBaseDMI(), selectedDataType)) {
			if (selectedTimeStep.equals(__TIMESTEP_IRREGULAR)) {
				selectedInputFilter_JPanel = __inputFilterHydroBaseStation_JPanel;
			}
			else {
				selectedInputFilter_JPanel = __inputFilterHydroBaseWells_JPanel;
			}
		}		
		else if ( (__inputFilterHydroBaseWIS_JPanel != null) &&
			HydroBase_Util.isWISTimeSeriesDataType ( ui_GetHydroBaseDMI(), selectedDataType) ) {
			selectedInputFilter_JPanel = __inputFilterHydroBaseWIS_JPanel;
		}
		else {
            // Generic input filter does not have anything...
			selectedInputFilter_JPanel = __inputFilterGeneric_JPanel;
		}
	}
	else if(selectedInputType.equals(__INPUT_TYPE_MEXICO_CSMN) &&
		(__inputFilterMexicoCSMN_JPanel != null) ) {
		// Can only use the Mexico CSMN filters if they were originally set up...
		selectedInputFilter_JPanel=__inputFilterMexicoCSMN_JPanel;
	}
	else if(selectedInputType.equals(__INPUT_TYPE_NWSRFS_FS5Files) &&
		(__inputFilterNWSRFSFS5Files_JPanel != null) ) {
		selectedInputFilter_JPanel = __inputFilterNWSRFSFS5Files_JPanel;
	}
	else {
        // Currently no other input types support filtering - this may also be used if HydroBase input
        // filters were not set up due to a missing database connection...
		selectedInputFilter_JPanel = __inputFilterGeneric_JPanel;
	}
    if ( selectedInputFilter_JPanel == null ) {
        if ( selectedDataStore != null ) {
            Message.printStatus(2, routine,
                "Unable to determine input panel to use for data store \"" + selectedDataStoreName +
                "\".  Using blank panel." );
        }
        else {
            Message.printStatus(2, routine,
                "Unable to determine input panel to use for input type \"" + selectedInputType +
                "\".  Using blank panel." ); 
        }
        selectedInputFilter_JPanel = __inputFilterGeneric_JPanel;
    }
    }
    catch ( Throwable e ) {
        Message.printStatus(2, routine,
            "Error selecting the input panel for input type\"" + selectedInputType +
            "\".  Using blank panel." );
        Message.printWarning(3,routine,e);
        selectedInputFilter_JPanel = __inputFilterGeneric_JPanel;
    }
    __selectedInputFilter_JPanel = selectedInputFilter_JPanel;
	// Now loop through the available input filter panels and set visible the selected one...
	for ( JPanel input_filter_JPanel: __inputFilterJPanelList ) {
		if ( input_filter_JPanel == __selectedInputFilter_JPanel ) {
			input_filter_JPanel.setVisible ( true );
			Message.printStatus(2, routine, "Set input filter panel \"" + input_filter_JPanel.getName() +
			    "\" visible TRUE." );
		}
		else {
		    input_filter_JPanel.setVisible ( false );
		    Message.printStatus(2, routine, "Set input filter panel \"" + input_filter_JPanel.getName() +
            "\" visible FALSE." );
		}
	}
}

/**
Set the "Input name" label and choices visible.  This is called when a data store or input type is selected
because input name is only used by some.
*/
private void ui_SetInputNameVisible(boolean isVisible )
{
    __inputName_JLabel.setVisible(isVisible);
    __inputName_JComboBox.setVisible(isVisible);
}

/**
Set the title of the input panel.
@param title the title for the input/query panel.  If null, use the default of "Input/Query Options..." with
a black border.
@param color color of the line border.
*/
private void ui_SetInputPanelTitle ( String title, Color color )
{
    if ( title == null ) {
        title = "Input/Query Options";
        color = Color.black;
    }
    __queryInput_JPanel.setBorder( BorderFactory.createTitledBorder (
        BorderFactory.createLineBorder(color),title ));
}

/**
Set the __input_type_JComboBox contents based on the configuration information.
If an input type is a database, only add if a non-null connection is available.
Later, database connections may also be named, in which case more checks will need to be done.
*/
private void ui_SetInputTypeChoices ()
{	//Message.printStatus ( 1, "", "SAMX - setting input type choices..." );
	// Ignore item events while manipulating.  This should prevent
	// unnecessary error-handling at startup.
	ui_SetIgnoreItemEvent ( true );
	if ( __input_type_JComboBox.getItemCount() > 0 ) {
		__input_type_JComboBox.removeAll ();
	}
	// Add a blank choice to allow working with data stores
	__input_type_JComboBox.add ( "" );
	if ( __source_DateValue_enabled ) {
		__input_type_JComboBox.add( __INPUT_TYPE_DateValue );
	}
	if ( __source_DIADvisor_enabled && (__DIADvisor_dmi != null) && (__DIADvisor_archive_dmi != null) ) {
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
	if ( __source_UsgsNwisRdb_enabled ) {
		__input_type_JComboBox.add( __INPUT_TYPE_UsgsNwisRdb );
	}

	// Enable item events again so the events will cascade...

	ui_SetIgnoreItemEvent ( false );

	if ( __source_HydroBase_enabled && (ui_GetHydroBaseDataStore() != null) ) {
		// If enabled and available, select it because the users probably want it as the choice...
		__input_type_JComboBox.select( null );
		__input_type_JComboBox.select( __INPUT_TYPE_HydroBase );
	}
	else {
        // Select the DateValue format, which is generic...
		__input_type_JComboBox.select ( null );
		__input_type_JComboBox.select ( __INPUT_TYPE_DateValue );
	}
}

/**
Set the message text field.
*/
private void ui_SetMessageText ( String message )
{
    __message_JTextField.setText (message);
}

/**
Update the command list to show the current status.  This is called after all commands
have been processed in run mode(), when a command has been edited(), and when loading commands from a file.
*/
private void ui_ShowCurrentCommandListStatus ()
{
    __commands_AnnotatedCommandJList.repaint();
}

/**
Update the main status information when the list contents have changed.  This
method should be called after any change to the query, command, or time series results list.
Interface tasks include:
<ol>
<li>	Set the title bar.
	If no command file has been read, the title will be:  "TSTool - no commands saved".
	If a command file has been read but not modified, the title will be:  "TSTool - "filename"".
	If a command file has been read and modified, the title will be: "TSTool - "filename" (modified)".
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
	
	if ( __commandFileName == null ) {
		setTitle ( "TSTool - no commands saved");
	}
	else {
        if ( __commandsDirty ) {
			setTitle ( "TSTool - \"" + __commandFileName + "\" (modified)");
		}
		else {
            setTitle ( "TSTool - \"" + __commandFileName + "\"");
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
				// Absorb the exception in most cases - print if developing to see if this issue can be			// resolved.
				if ( Message.isDebugOn && IOUtil.testing()  ) {
					String routine = "TSTool_JFrame.updateStatus";
					Message.printWarning ( 3, routine,
					"For developers:  caught exception in clearQueryList JWorksheet at setup." );
					Message.printWarning ( 3, routine, e );
				}
			}
		}
        __query_results_JPanel.setBorder( BorderFactory.createTitledBorder (
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
    	__commands_JPanel.setBorder( BorderFactory.createTitledBorder (
		BorderFactory.createLineBorder(Color.black),
		"Commands (" + __commands_JListModel.size() + " commands, " + selected_size + " selected, " +
		commandList_GetFailureCount() + " with failures, " + commandList_GetWarningCount() + " with warnings)") );
	}
	
	// Results (currently only update title for time series tab...
	
	selected_indices = __resultsTS_JList.getSelectedIndices();
	selected_size = 0;
	if ( selected_indices != null ) {
		selected_size = selected_indices.length;
	}
	if ( __resultsTS_JPanel != null ) {
       	__resultsTS_JPanel.setBorder(BorderFactory.createTitledBorder (
		BorderFactory.createLineBorder(Color.black),
		//"Results: Time Series (" +	
        "" + __resultsTS_JListModel.size() + " time series, " + selected_size + " selected") );
	}
	// TODO SAM 2007-08-31 Evaluate call here - probably should call elsewhere
	//ui_UpdateStatusTextFields ( -1, "TSTool_JFrame.updateStatus", null,
	//			__STATUS_READY );
	// FIXME SAM 2008-11-11 Why is this called no matter what is passed in for the parameter?
	ui_CheckGUIState ();
}

/**
Update the text fields at the bottom of the main interface.  This does NOT update
all text fields like the number of commands, etc.
@param level Message level.  If > 0 and the message is not null, call
Message.printStatus() to record a message.
@param routine Routine name used if Message.printStatus() is called.
@param commandPanelStatus 
@param message If not null, update the __message_JTextField to contain this
text.  If null, leave the contents as previously shown.  Specify "" to clear the text.
@param status If not null, update the __status_JTextField to contain
this text.  If null, leave the contents as previously shown.  Specify "" to clear the text.
*/
private void ui_UpdateStatusTextFields ( int level, String routine,
		String commandPanelStatus, String message, String status )
{	if ( (level > 0) && (message != null) ) {
		// Print a status message to the messaging system...
		Message.printStatus ( 1, routine, message );
	}
	if ( message != null ) {
		ui_SetMessageText ( message );
	}
	if ( status != null ) {
		__status_JTextField.setText (status);
	}
}

/**
Handle a group of action, for the 
@param event Event to handle.
*/
private void uiAction_ActionPerformed01_MainActions (ActionEvent event)
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
		JGUIUtil.selectAll ( __resultsTS_JList );
	}
	else if (command.equals(BUTTON_TS_DESELECT_ALL) ) {
		__resultsTS_JList.clearSelection();
	}
	else {
        // Chain to the next method...
		uiAction_ActionPerformed02_FileMenu(event);
	}
}

/**
Handle a group of actions for the File menu.
@param event Event to handle.
*/
private void uiAction_ActionPerformed02_FileMenu (ActionEvent event)
throws Exception
{	Object o = event.getSource();
	String command = event.getActionCommand();
	String rtn = "FileMenu";

	// File Menu actions...

    if ( o == __File_New_CommandFile_JMenuItem ) {
        uiAction_NewCommandFile();
    }
	else if ( o == __File_Open_CommandFile_JMenuItem ) {
		try {
            uiAction_OpenCommandFile();
		}
		catch ( Exception e ) {
			Message.printWarning ( 1, rtn, "Error reading command file (" + e + ")." );
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
		uiAction_OpenHydroBase ( false ); // False means not opening at startup
		// Update the HydroBase input filters
		if ( ui_GetHydroBaseDataStore() != null ) {
		    ui_InitGUIInputFiltersHydroBase ( ui_GetHydroBaseDataStore(), __inputFilterY );
		}
		// Force the choices to refresh...
		if ( ui_GetHydroBaseDataStore() != null ) {
			__input_type_JComboBox.select ( null );
			__input_type_JComboBox.select (__INPUT_TYPE_HydroBase);
		}
	}
	else if ( command.equals ( __File_Open_RiversideDB_String )) {
	    // FIXME SAM 2010-09-07 Need to enable in some form
		// Read a RiverTrak config file, get the RiversideDB properties, and open the database...
		JFileChooser fc = JFileChooserFactory.createJFileChooser( JGUIUtil.getLastFileDialogDirectory() );
		fc.setDialogTitle( "Select a RiversideDB Configuration File" );
		SimpleFileFilter sff = new SimpleFileFilter ( "cfg", "RiversideDB Data Store Configuration File" );
        fc.addChoosableFileFilter ( sff );
		sff = new SimpleFileFilter ( "cfg", "RiverTrak/TSTool Configuration File" );
		fc.addChoosableFileFilter ( sff );
		fc.setFileFilter ( sff );
		if ( fc.showOpenDialog(this) != JFileChooser.APPROVE_OPTION ) {
			return;
		}
		// Don't save the directory because it is probably a one-off selection and won't be picked again.
		String path = fc.getSelectedFile().getPath();
		DataStore dataStore = uiAction_OpenRiversideDB ( path );
		if ( dataStore != null ) {
            // Now update the input filters for the open data store list (only pass in the one item so that
            // existing input filters are not impacted
            List<DataStore> dataStoreList = new Vector();
            dataStoreList.add ( dataStore );
            ui_InitGUIInputFiltersRiversideDB ( dataStoreList, ui_GetInputFilterY() );
            // Add the data store name to the choices and select the choice, which will cause other events
            __dataStore_JComboBox.add(dataStore.getName());
            __dataStore_JComboBox.select(dataStore.getName());
		}
	}
	else if ( o == __File_Save_Commands_JMenuItem ) {
		try {
            if ( __commandFileName != null ) {
				// Use the existing name...
                int x = ResponseJDialog.OK;
                if ( __tsProcessor.getReadOnly() ) {
                    x = new ResponseJDialog ( this, IOUtil.getProgramName(),
                        "The commands are marked read-only.\n" +
                        "Press Cancel and save to a new name if desired.  Press OK to update the read-only file.",
                        ResponseJDialog.OK|ResponseJDialog.CANCEL).response();
                }
                if ( x == ResponseJDialog.OK ) {
                    // Save most recent version...
                    uiAction_WriteCommandFile ( __commandFileName, false, false );
                }
			}
			else {
                // No command file has been saved - prompt for the name...
				uiAction_WriteCommandFile ( __commandFileName, true, false );
			}
		}
		catch ( Exception e ) {
			Message.printWarning ( 1, rtn, "Error writing command file (" + e + ")." );
		}
	}
	else if ( o == __File_Save_CommandsAs_JMenuItem ) {
		try {
            // Prompt for the name...
			uiAction_WriteCommandFile ( __commandFileName, true, false );
		}
		catch ( Exception e ) {
			Message.printWarning ( 1, rtn, "Error writing command file (" + e + ")." );
		}
	}
    else if ( o == __File_Save_CommandsAsVersion9_JMenuItem ) {
        try {
            // Prompt for the name...
            uiAction_WriteCommandFile ( __commandFileName, true, true );
        }
        catch ( Exception e ) {
            Message.printWarning ( 1, rtn, "Error writing command file in version 9 format (" + e + ")." );
        }
    }
    else if ( command.equals(__File_Save_TimeSeriesAs_String) ) {
		// Can save in a number of formats.  Allow the user to pick using a file chooser...
		uiAction_SaveTimeSeries ();
	}
    else if (command.equals(__File_Print_Commands_String) ) {
        // Get all commands as strings for printing
        try {
            new TextPrinterJob ( commandList_GetCommandStrings(true), "TSTool Commands",
                null, // printer name
                "na-letter", // paper size
                null, // paper source
                "Landscape", // page orientation
                .75, // left margin
                .75, // right
                .6, // top
                .6, // bottom
                0, // lines per page - let called code determine
                null, // header
                null, // footer
                true, // show line count
                true, // show page count
                null, // print all pages
                false, // double-sided
                null, // print file
                true ); // show print configuration dialog
        }
        catch ( Exception e ) {
            Message.printWarning ( 1, rtn, "Error printing commands (" + e + ").");
            Message.printWarning ( 3, rtn, e );
        }
    }
	else if ( command.equals(__File_Properties_CommandsRun_String) ) {
		// Simple text display of last commands run data from TSEngine.
		uiAction_ShowProperties_CommandsRun();
	}
    else if ( command.equals(__File_Properties_TSToolSession_String) ) {
        uiAction_ShowProperties_TSToolSession( ui_GetHydroBaseDataStore() );
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
		List v = null;
		if ( __smsdmi == null ) {
		    v = new Vector(3);
			v.add ( "Colorado SMS Properties" );
			v.add ( "" );
			v.add("No Colorado SMS database is available." );
		}
		else {
            v = __smsdmi.getDatabaseProperties();
		}
		new ReportJFrame ( v, reportProp );
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
		List<String> v = new Vector();
		v.add ( "DIADvisor Operational Database:" );
		v.add ( "" );
		StringUtil.addListToStringList ( v, __DIADvisor_dmi.getDatabaseProperties ( 3 ) );
		v.add ( "" );
		v.add ( "DIADvisor Archive Database:" );
		v.add ( "" );
		StringUtil.addListToStringList ( v, __DIADvisor_archive_dmi.getDatabaseProperties ( 3 ) );
		new ReportJFrame ( v, reportProp );
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
		List<String> v = null;
		if ( ui_GetHydroBaseDMI() == null ) {
		    v = new Vector(3);
			v.add ( "HydroBase Properties" );
			v.add ( "" );
			v.add ( "No HydroBase database is available." );
		}
		else {
            v = ui_GetHydroBaseDMI().getDatabaseProperties();
		}
		new ReportJFrame ( v, reportProp );
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
		List<String> v = null;
		if ( __nwsrfs_dmi == null ) {
			v = new Vector ( 1 );
			v.add ( "The NWSRFS FS5Files connection is not open." );
		}
		else {
            v = __nwsrfs_dmi.getDatabaseProperties ( 3 );
		}
		new ReportJFrame ( v, reportProp );
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
		reportProp.set ( "Title", "RiversideDB Data Store Properties" );
		List<DataStore> dataStoreList = __tsProcessor.getDataStoresByType(RiversideDBDataStore.class);
		List<String> v = new Vector();
        v.add ( "RiversideDB Data Store Properties" );
        v.add ( "" );
		for ( DataStore dataStore : dataStoreList ) {
		    v.add ( "Data store name:  " + dataStore.getName() );
		    v.add ( "Data store description:  " + dataStore.getDescription() );
		    v.add ( "" );
		    RiversideDB_DMI rdmi = (RiversideDB_DMI)((RiversideDBDataStore)dataStore).getDMI();
		    v.addAll ( rdmi.getDatabaseProperties ( 3 ) );
		}
		new ReportJFrame ( v, reportProp );
	}
    else if ( o == __File_SetWorkingDirectory_JMenuItem ) {
		JFileChooser fc = JFileChooserFactory.createJFileChooser ( JGUIUtil.getLastFileDialogDirectory() );
		fc.setDialogTitle( "Set the Working Directory (normally only use if a command file was not opened or saved)");
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
			Message.printWarning ( 1, "", "Error processing TSProduct file (" + te + ")." );
			Message.printWarning ( 3, "", te );
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
			Message.printWarning ( 3, "", te );
		}
	}
    else {
    	// Chain to the next action handler...
    	uiAction_ActionPerformed03_EditMenu ( event );
    }
}

/**
Handle a group of actions for the Edit menu.
@param event Event to handle.
*/
private void uiAction_ActionPerformed03_EditMenu (ActionEvent event)
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
	else if ( command.equals(__Edit_CommandWithErrorChecking_String) ) {
		// Edit the first selected item, unless a comment, in which case all are edited...
		uiAction_EditCommand ();
	}
	else if (command.equals(__Edit_ConvertSelectedCommandsToComments_String) ) {
		uiAction_ConvertCommandsToComments ( true );
	}
	else if (command.equals(__Edit_ConvertSelectedCommandsFromComments_String) ) {
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
		uiAction_ActionPerformed04_ViewMenu ( event );
	}
}

/**
Handle a group of actions for the Run menu.
@param event Event to handle.
*/
private void uiAction_ActionPerformed04_ViewMenu (ActionEvent event)
throws Exception
{   String command = event.getActionCommand();
    //String routine = getClass().getName() + ".uiAction_ActionPerformed3b_ViewMenu";

    if ( command.equals(__View_DataUnits_String) ) {
        // Show the data units
        uiAction_ShowDataUnits();
    }
    else if ( command.equals(__View_DataStores_String) ) {
        // Show the data stores
        uiAction_ShowDataStores();
    }
    else if ( command.equals(__View_CloseAllViewWindows_String) ) {
        // Show the data stores
        TSViewJFrame.getTSViewWindowManager().closeAll();
    }
    else {
        // Chain to next set of actions...
        uiAction_ActionPerformed05_CommandsCreateMenu(event);
    }
}

/**
Handle a group of actions for the Commands...Create... menu.
@param event Event to handle.
*/
private void uiAction_ActionPerformed05_CommandsCreateMenu (ActionEvent event)
throws Exception
{	String command = event.getActionCommand();

	// TODO SAM 2006-05-02 Why is all of this needed?  Should rely on the command factory for
	// commands.  Evaluate this when all commands are in the factory.

	if (command.equals( __Commands_Create_CreateFromList_String)){
		commandList_EditCommand ( __Commands_Create_CreateFromList_String, null, CommandEditType.INSERT );
	}
	else if (command.equals( __Commands_Create_Delta_String)){
        commandList_EditCommand ( __Commands_Create_Delta_String, null, CommandEditType.INSERT );
    }
    else if (command.equals( __Commands_Create_ResequenceTimeSeriesData_String) ) {
        commandList_EditCommand ( __Commands_Create_ResequenceTimeSeriesData_String, null, CommandEditType.INSERT );
    }
    else if (command.equals(__Commands_Create_RunningStatisticTimeSeries_String)){
        commandList_EditCommand ( __Commands_Create_RunningStatisticTimeSeries_String, null, CommandEditType.INSERT );
    }

	// Convert TS Identifier to read command...

	else if ( command.equals(__Edit_ConvertTSIDTo_ReadTimeSeries_String) ) {
		commandList_EditCommand ( __Edit_ConvertTSIDTo_ReadTimeSeries_String,
		    commandList_GetCommandsBasedOnUI(), CommandEditType.CONVERT );
	}
    else if ( command.equals(__Edit_ConvertTSIDTo_ReadCommand_String) ) {
        commandList_EditCommand ( __Edit_ConvertTSIDTo_ReadCommand_String,
            commandList_GetCommandsBasedOnUI(), CommandEditType.CONVERT );
    }
	/*
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
	else if (command.equals( __Commands_Create_ChangeInterval_String)){
		commandList_EditCommand ( __Commands_Create_ChangeInterval_String, null, CommandEditType.INSERT );
	}
	else if (command.equals( __Commands_Create_Copy_String)){
		commandList_EditCommand ( __Commands_Create_Copy_String, null, CommandEditType.INSERT );
	}
	else if (command.equals( __Commands_Create_Disaggregate_String)){
		commandList_EditCommand ( __Commands_Create_Disaggregate_String, null, CommandEditType.INSERT );
	}
    else if (command.equals( __Commands_Create_LookupTimeSeriesFromTable_String)){
        commandList_EditCommand ( __Commands_Create_LookupTimeSeriesFromTable_String, null, CommandEditType.INSERT );
    }
	else if (command.equals( __Commands_Create_NewDayTSFromMonthAndDayTS_String)){
		commandList_EditCommand ( __Commands_Create_NewDayTSFromMonthAndDayTS_String, null, CommandEditType.INSERT );
	}
	else if (command.equals(__Commands_Create_NewEndOfMonthTSFromDayTS_String) ) {
		commandList_EditCommand ( __Commands_Create_NewEndOfMonthTSFromDayTS_String, null, CommandEditType.INSERT );
	}
	else if (command.equals( __Commands_Create_NewPatternTimeSeries_String)){
		commandList_EditCommand ( __Commands_Create_NewPatternTimeSeries_String, null, CommandEditType.INSERT );
	}
	else if (command.equals(__Commands_Create_NewStatisticTimeSeries_String)){
		commandList_EditCommand ( __Commands_Create_NewStatisticTimeSeries_String, null, CommandEditType.INSERT );
	}
	else if (command.equals( __Commands_Create_NewStatisticYearTS_String)){
		commandList_EditCommand ( __Commands_Create_NewStatisticYearTS_String, null, CommandEditType.INSERT );
	}
	else if (command.equals( __Commands_Create_NewTimeSeries_String)){
		commandList_EditCommand ( __Commands_Create_NewTimeSeries_String, null, CommandEditType.INSERT );
	}
	else if (command.equals( __Commands_Create_Normalize_String)){
		commandList_EditCommand ( __Commands_Create_Normalize_String, null, CommandEditType.INSERT );
	}
	else if (command.equals( __Commands_Create_RelativeDiff_String)){
		commandList_EditCommand ( __Commands_Create_RelativeDiff_String, null, CommandEditType.INSERT );
	}
	else {
		// Chain to next actions...
		uiAction_ActionPerformed06_CommandsReadMenu ( event );
	}
}

/**
Handle a group of actions for the Commands...Read... menu.
@param event Event to handle.
*/
private void uiAction_ActionPerformed06_CommandsReadMenu (ActionEvent event)
throws Exception
{	String command = event.getActionCommand();

	// Read Time Series...

    if (command.equals(__Commands_Read_SetIncludeMissingTS_String)){
        commandList_EditCommand ( __Commands_Read_SetIncludeMissingTS_String, null, CommandEditType.INSERT );
    }
    else if (command.equals( __Commands_Read_SetInputPeriod_String) ) {
        commandList_EditCommand ( __Commands_Read_SetInputPeriod_String, null, CommandEditType.INSERT );
    }
    else if (command.equals( __Commands_Read_ReadColoradoBNDSS_String)){
        commandList_EditCommand ( __Commands_Read_ReadColoradoBNDSS_String, null, CommandEditType.INSERT );
    }
    else if (command.equals( __Commands_Read_ReadDateValue_String)){
		commandList_EditCommand ( __Commands_Read_ReadDateValue_String, null, CommandEditType.INSERT );
	}
    else if (command.equals( __Commands_Read_ReadDelimitedFile_String)){
        commandList_EditCommand ( __Commands_Read_ReadDelimitedFile_String, null, CommandEditType.INSERT );
    }
    else if (command.equals( __Commands_Read_ReadHecDss_String)){
        commandList_EditCommand ( __Commands_Read_ReadHecDss_String, null, CommandEditType.INSERT );
    }
	else if (command.equals( __Commands_Read_ReadHydroBase_String)){
		commandList_EditCommand ( __Commands_Read_ReadHydroBase_String,	null, CommandEditType.INSERT );
	}
	else if (command.equals( __Commands_Read_ReadMODSIM_String)){
		commandList_EditCommand ( __Commands_Read_ReadMODSIM_String, null, CommandEditType.INSERT );
	}
	else if (command.equals(__Commands_Read_ReadNwsCard_String)){
		commandList_EditCommand ( __Commands_Read_ReadNwsCard_String, null, CommandEditType.INSERT );
	}
    else if (command.equals( __Commands_Read_ReadRccAcis_String)){
        commandList_EditCommand ( __Commands_Read_ReadRccAcis_String, null, CommandEditType.INSERT );
    }
	else if (command.equals( __Commands_Read_ReadReclamationHDB_String)){
        commandList_EditCommand ( __Commands_Read_ReadReclamationHDB_String, null, CommandEditType.INSERT );
    }
    else if (command.equals( __Commands_Read_ReadRiversideDB_String)){
        commandList_EditCommand ( __Commands_Read_ReadRiversideDB_String, null, CommandEditType.INSERT );
    }
	else if (command.equals( __Commands_Read_ReadStateCU_String)){
		commandList_EditCommand ( __Commands_Read_ReadStateCU_String, null, CommandEditType.INSERT );
	}
    else if (command.equals( __Commands_Read_ReadStateCUB_String)){
        commandList_EditCommand ( __Commands_Read_ReadStateCUB_String, null, CommandEditType.INSERT );
    }
	else if (command.equals( __Commands_Read_ReadStateModB_String)){
		commandList_EditCommand ( __Commands_Read_ReadStateModB_String,	null, CommandEditType.INSERT );
	}
	// Put after longer versions...
	else if (command.equals( __Commands_Read_ReadStateMod_String)){
		commandList_EditCommand ( __Commands_Read_ReadStateMod_String, null, CommandEditType.INSERT );
	}
	else if (command.equals( __Commands_Read_ReadNwsrfsFS5Files_String)){
		commandList_EditCommand ( __Commands_Read_ReadNwsrfsFS5Files_String, null, CommandEditType.INSERT );
	}
	else if (command.equals( __Commands_Read_ReadRiverWare_String)){
		commandList_EditCommand ( __Commands_Read_ReadRiverWare_String, null, CommandEditType.INSERT );
	}
    else if (command.equals( __Commands_Read_ReadTimeSeries_String)){
        commandList_EditCommand ( __Commands_Read_ReadTimeSeries_String, null, CommandEditType.INSERT );
    }
    else if (command.equals( __Commands_Read_ReadUsgsNwisDaily_String)){
        commandList_EditCommand ( __Commands_Read_ReadUsgsNwisDaily_String, null, CommandEditType.INSERT );
    }
	else if (command.equals( __Commands_Read_ReadUsgsNwisRdb_String)){
		commandList_EditCommand ( __Commands_Read_ReadUsgsNwisRdb_String, null, CommandEditType.INSERT );
	}
    else if (command.equals( __Commands_Read_ReadWaterML_String)){
        commandList_EditCommand ( __Commands_Read_ReadWaterML_String, null, CommandEditType.INSERT );
    }
    else if (command.equals( __Commands_Read_ReadWaterOneFlow_String)){
        commandList_EditCommand ( __Commands_Read_ReadWaterOneFlow_String, null, CommandEditType.INSERT );
    }
    else if (command.equals( __Commands_Read_StateModMax_String)){
        commandList_EditCommand ( __Commands_Read_StateModMax_String, null, CommandEditType.INSERT );
    }
	else {
		// Chain to next menus
		uiAction_ActionPerformed07_CommandsFillMenu ( event );
	}
}

/**
Handle a group of actions for the Commands...Fill... menu.
@param event Event to handle.
*/
private void uiAction_ActionPerformed07_CommandsFillMenu (ActionEvent event)
throws Exception
{	String command = event.getActionCommand();

    if (command.equals( __Commands_Fill_FillConstant_String)){
		commandList_EditCommand ( __Commands_Fill_FillConstant_String, null, CommandEditType.INSERT );
	}
	else if (command.equals(__Commands_Fill_FillDayTSFrom2MonthTSAnd1DayTS_String)){
		commandList_EditCommand ( __Commands_Fill_FillDayTSFrom2MonthTSAnd1DayTS_String, null, CommandEditType.INSERT );
	}
	else if (command.equals( __Commands_Fill_FillFromTS_String)){
		commandList_EditCommand ( __Commands_Fill_FillFromTS_String, null, CommandEditType.INSERT );
	}
	else if (command.equals( __Commands_Fill_FillHistMonthAverage_String)){
		commandList_EditCommand ( __Commands_Fill_FillHistMonthAverage_String, null, CommandEditType.INSERT );
	}
	else if (command.equals( __Commands_Fill_FillHistYearAverage_String)){
		commandList_EditCommand ( __Commands_Fill_FillHistYearAverage_String, null, CommandEditType.INSERT );
	}
	else if (command.equals( __Commands_Fill_FillInterpolate_String)){
		commandList_EditCommand ( __Commands_Fill_FillInterpolate_String, null, CommandEditType.INSERT );
	}
	else if (command.equals( __Commands_Fill_FillMixedStation_String)){
		commandList_EditCommand ( __Commands_Fill_FillMixedStation_String, null, CommandEditType.INSERT );
	}
	else if (command.equals( __Commands_Fill_FillMOVE1_String)){
		commandList_EditCommand ( __Commands_Fill_FillMOVE1_String,	null, CommandEditType.INSERT );
	}
	else if (command.equals( __Commands_Fill_FillMOVE2_String)){
		commandList_EditCommand ( __Commands_Fill_FillMOVE2_String,	null, CommandEditType.INSERT );
	}
	else if (command.equals( __Commands_Fill_FillPattern_String) ) {
		commandList_EditCommand ( __Commands_Fill_FillPattern_String, null, CommandEditType.INSERT );
	}
    else if (command.equals( __Commands_Fill_ReadPatternFile_String) ) {
        commandList_EditCommand ( __Commands_Fill_ReadPatternFile_String, null, CommandEditType.INSERT );
    }
	else if (command.equals( __Commands_Fill_FillProrate_String) ) {
		commandList_EditCommand ( __Commands_Fill_FillProrate_String, null, CommandEditType.INSERT );
	}
	else if (command.equals( __Commands_Fill_FillRegression_String) ) {
		commandList_EditCommand ( __Commands_Fill_FillRegression_String, null, CommandEditType.INSERT );
	}
	else if (command.equals( __Commands_Fill_FillRepeat_String) ) {
		commandList_EditCommand ( __Commands_Fill_FillRepeat_String, null, CommandEditType.INSERT );
	}
	else if(command.equals(	__Commands_Fill_FillUsingDiversionComments_String)){
		commandList_EditCommand (__Commands_Fill_FillUsingDiversionComments_String, null, CommandEditType.INSERT );
	}
	else if (command.equals(__Commands_Fill_SetAutoExtendPeriod_String)){
		commandList_EditCommand ( __Commands_Fill_SetAutoExtendPeriod_String, null, CommandEditType.INSERT );
	}
	else if (command.equals( __Commands_Fill_SetAveragePeriod_String) ) {
		commandList_EditCommand ( __Commands_Fill_SetAveragePeriod_String, null, CommandEditType.INSERT );
	}
	else if (command.equals( __Commands_Fill_SetIgnoreLEZero_String) ) {
		commandList_EditCommand ( __Commands_Fill_SetIgnoreLEZero_String, null, CommandEditType.INSERT );
	}
	else {
		// Chain to other menus
		uiAction_ActionPerformed08_CommandsSetMenu ( event );
	}
}

/**
Handle a group of actions for the Commands...Set... menu.
@param event Event to handle.
*/
private void uiAction_ActionPerformed08_CommandsSetMenu (ActionEvent event)
throws Exception
{	String command = event.getActionCommand();

	if (command.equals( __Commands_Set_ReplaceValue_String)){
		commandList_EditCommand ( __Commands_Set_ReplaceValue_String, null, CommandEditType.INSERT );
	}
	else if (command.equals( __Commands_Set_SetConstant_String)){
		commandList_EditCommand ( __Commands_Set_SetConstant_String, null, CommandEditType.INSERT );
    }
	else if (command.equals( __Commands_Set_SetDataValue_String)){
		commandList_EditCommand ( __Commands_Set_SetDataValue_String, null, CommandEditType.INSERT );
	}
	else if (command.equals( __Commands_Set_SetFromTS_String)){
		commandList_EditCommand ( __Commands_Set_SetFromTS_String, null, CommandEditType.INSERT );
	}
	else if (command.equals( __Commands_Set_SetToMax_String)){
		commandList_EditCommand ( __Commands_Set_SetToMax_String, null, CommandEditType.INSERT );
	}
	else if (command.equals( __Commands_Set_SetToMin_String)){
		commandList_EditCommand ( __Commands_Set_SetToMin_String, null, CommandEditType.INSERT );
	}
    else if (command.equals( __Commands_Set_SetTimeSeriesProperty_String)){
        commandList_EditCommand ( __Commands_Set_SetTimeSeriesProperty_String, null, CommandEditType.INSERT );
    }
	else {
		uiAction_ActionPerformed09_CommandsManipulateMenu ( event );
	}
}

/**
Handle a group of actions for the Commands...Manipulate... menu.
@param event Event to handle.
*/
private void uiAction_ActionPerformed09_CommandsManipulateMenu (ActionEvent event)
throws Exception
{	String command = event.getActionCommand();

	if (command.equals( __Commands_Manipulate_Add_String)){
		commandList_EditCommand ( __Commands_Manipulate_Add_String,	null, CommandEditType.INSERT );
	}
	else if (command.equals( __Commands_Manipulate_AddConstant_String)){
		commandList_EditCommand ( __Commands_Manipulate_AddConstant_String, null, CommandEditType.INSERT );
	}
	else if (command.equals( __Commands_Manipulate_AdjustExtremes_String)){
		commandList_EditCommand ( __Commands_Manipulate_AdjustExtremes_String, null, CommandEditType.INSERT );
	}
	else if (command.equals( __Commands_Manipulate_ARMA_String)){
		commandList_EditCommand ( __Commands_Manipulate_ARMA_String, null, CommandEditType.INSERT );
	}
	else if (command.equals( __Commands_Manipulate_Blend_String)){
		commandList_EditCommand ( __Commands_Manipulate_Blend_String, null, CommandEditType.INSERT );
	}
    else if (command.equals(__Commands_Manipulate_ChangePeriod_String)){
        commandList_EditCommand ( __Commands_Manipulate_ChangePeriod_String, null, CommandEditType.INSERT );
    }
	else if (command.equals(__Commands_Manipulate_ConvertDataUnits_String)){
		commandList_EditCommand ( __Commands_Manipulate_ConvertDataUnits_String, null, CommandEditType.INSERT );
	}
	else if (command.equals(__Commands_Manipulate_Cumulate_String) ) {
		commandList_EditCommand ( __Commands_Manipulate_Cumulate_String, null, CommandEditType.INSERT );
	}
	else if (command.equals(__Commands_Manipulate_Divide_String) ) {
		commandList_EditCommand ( __Commands_Manipulate_Divide_String, null, CommandEditType.INSERT );
	}
	else if (command.equals(__Commands_Manipulate_Free_String) ) {
		commandList_EditCommand ( __Commands_Manipulate_Free_String, null, CommandEditType.INSERT );
	}
	else if (command.equals(__Commands_Manipulate_Multiply_String) ) {
		commandList_EditCommand ( __Commands_Manipulate_Multiply_String, null, CommandEditType.INSERT );
	}
	else if (command.equals(__Commands_Manipulate_RunningAverage_String) ) {
		commandList_EditCommand ( __Commands_Manipulate_RunningAverage_String, null, CommandEditType.INSERT );
	}
	else if (command.equals(__Commands_Manipulate_Scale_String) ) {
		commandList_EditCommand ( __Commands_Manipulate_Scale_String, null, CommandEditType.INSERT );
	}
	else if (command.equals(__Commands_Manipulate_ShiftTimeByInterval_String) ) {
		commandList_EditCommand ( __Commands_Manipulate_ShiftTimeByInterval_String, null, CommandEditType.INSERT );
	}
	else if (command.equals( __Commands_Manipulate_Subtract_String)){
		commandList_EditCommand ( __Commands_Manipulate_Subtract_String, null, CommandEditType.INSERT );
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
		commandList_EditCommand ( __Commands_Analyze_AnalyzePattern_String,	null, CommandEditType.INSERT );
	}
    else if (command.equals( __Commands_Analyze_CalculateTimeSeriesStatistic_String)){
        commandList_EditCommand ( __Commands_Analyze_CalculateTimeSeriesStatistic_String, null, CommandEditType.INSERT );
    }
	else if (command.equals( __Commands_Analyze_CompareTimeSeries_String)){
		commandList_EditCommand ( __Commands_Analyze_CompareTimeSeries_String, null, CommandEditType.INSERT );
	}
	else if (command.equals( __Commands_Analyze_ComputeErrorTimeSeries_String)){
        commandList_EditCommand ( __Commands_Analyze_ComputeErrorTimeSeries_String, null, CommandEditType.INSERT );
    }
	else {
		// Chain to other actions
		uiAction_ActionPerformed11_CommandsModelsMenu ( event );
	}
}

/**
Handle a group of actions for the Commands...Models*... menus.
@param event Event to handle.
*/
private void uiAction_ActionPerformed11_CommandsModelsMenu (ActionEvent event)
throws Exception
{	String command = event.getActionCommand();

	if (command.equals( __Commands_Models_Routing_LagK_String)){
		commandList_EditCommand ( __Commands_Models_Routing_LagK_String, null, CommandEditType.INSERT );
	}
	else if (command.equals( __Commands_Models_Routing_VariableLagK_String)){
        commandList_EditCommand ( __Commands_Models_Routing_VariableLagK_String, null, CommandEditType.INSERT );
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

    if (command.equals( __Commands_Ensemble_CreateEnsembleFromOneTimeSeries_String)){
        commandList_EditCommand ( __Commands_Ensemble_CreateEnsembleFromOneTimeSeries_String, null, CommandEditType.INSERT );
    }
    else if (command.equals( __Commands_Ensemble_CopyEnsemble_String)){
        commandList_EditCommand ( __Commands_Ensemble_CopyEnsemble_String, null, CommandEditType.INSERT );
    }
    else if (command.equals( __Commands_Ensemble_NewEnsemble_String)){
        commandList_EditCommand ( __Commands_Ensemble_NewEnsemble_String, null, CommandEditType.INSERT );
    }
    else if (command.equals( __Commands_Ensemble_ReadNwsrfsEspTraceEnsemble_String)){
        commandList_EditCommand ( __Commands_Ensemble_ReadNwsrfsEspTraceEnsemble_String, null, CommandEditType.INSERT );
    }
    else if (command.equals( __Commands_Ensemble_InsertTimeSeriesIntoEnsemble_String)){
        commandList_EditCommand ( __Commands_Ensemble_InsertTimeSeriesIntoEnsemble_String, null, CommandEditType.INSERT );
    }
    else if (command.equals(__Commands_Ensemble_NewStatisticEnsemble_String)){
        commandList_EditCommand ( __Commands_Ensemble_NewStatisticEnsemble_String, null, CommandEditType.INSERT );
    }
    else if (command.equals(__Commands_Ensemble_NewStatisticTimeSeriesFromEnsemble_String)){
        commandList_EditCommand ( __Commands_Ensemble_NewStatisticTimeSeriesFromEnsemble_String, null, CommandEditType.INSERT );
    }
    else if (command.equals( __Commands_Ensemble_WeightTraces_String)){
        commandList_EditCommand ( __Commands_Ensemble_WeightTraces_String, null, CommandEditType.INSERT );
    }
    else if(command.equals( __Commands_Ensemble_WriteNwsrfsEspTraceEnsemble_String)){
        commandList_EditCommand ( __Commands_Ensemble_WriteNwsrfsEspTraceEnsemble_String, null, CommandEditType.INSERT );
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
		commandList_EditCommand ( __Commands_Output_DeselectTimeSeries_String, null, CommandEditType.INSERT );
	}
	else if (command.equals( __Commands_Output_SelectTimeSeries_String)){
		commandList_EditCommand ( __Commands_Output_SelectTimeSeries_String, null, CommandEditType.INSERT );
	}
	else if (command.equals(
		__Commands_Output_SetOutputDetailedHeaders_String) ) {
		commandList_EditCommand (__Commands_Output_SetOutputDetailedHeaders_String, null, CommandEditType.INSERT );
	}
	else if (command.equals( __Commands_Output_SetOutputPeriod_String) ) {
		commandList_EditCommand ( __Commands_Output_SetOutputPeriod_String, null, CommandEditType.INSERT );
	}
	else if (command.equals( __Commands_Output_SetOutputYearType_String) ){
		commandList_EditCommand ( __Commands_Output_SetOutputYearType_String, null, CommandEditType.INSERT );
	}
	else if (command.equals( __Commands_Output_SortTimeSeries_String)){
		commandList_EditCommand ( __Commands_Output_SortTimeSeries_String, null, CommandEditType.INSERT );
	}
	else if (command.equals( __Commands_Output_WriteDateValue_String)){
		commandList_EditCommand ( __Commands_Output_WriteDateValue_String, null, CommandEditType.INSERT );
	}
    else if (command.equals( __Commands_Output_WriteHecDss_String)){
        commandList_EditCommand ( __Commands_Output_WriteHecDss_String, null, CommandEditType.INSERT );
    }
	else if (command.equals( __Commands_Output_WriteNwsCard_String)){
		commandList_EditCommand ( __Commands_Output_WriteNwsCard_String, null, CommandEditType.INSERT );
	}
    else if (command.equals( __Commands_Output_WriteReclamationHDB_String)){
        commandList_EditCommand ( __Commands_Output_WriteReclamationHDB_String, null, CommandEditType.INSERT );
    }
    else if (command.equals( __Commands_Output_WriteRiversideDB_String)){
        commandList_EditCommand ( __Commands_Output_WriteRiversideDB_String, null, CommandEditType.INSERT );
    }
	else if (command.equals( __Commands_Output_WriteRiverWare_String)){
		commandList_EditCommand ( __Commands_Output_WriteRiverWare_String, null, CommandEditType.INSERT );
	}
    else if (command.equals( __Commands_Output_WriteSHEF_String)){
        commandList_EditCommand ( __Commands_Output_WriteSHEF_String, null, CommandEditType.INSERT );
    }
	else if (command.equals( __Commands_Output_WriteStateCU_String)){
		commandList_EditCommand ( __Commands_Output_WriteStateCU_String, null, CommandEditType.INSERT );
	}
	else if (command.equals( __Commands_Output_WriteStateMod_String)){
		commandList_EditCommand ( __Commands_Output_WriteStateMod_String, null, CommandEditType.INSERT );
	}
	else if (command.equals( __Commands_Output_WriteSummary_String)){
		commandList_EditCommand ( __Commands_Output_WriteSummary_String, null, CommandEditType.INSERT );
	}
    else if (command.equals( __Commands_Output_WriteWaterML_String)){
        commandList_EditCommand ( __Commands_Output_WriteWaterML_String, null, CommandEditType.INSERT );
    }
	else if (command.equals( __Commands_Output_ProcessTSProduct_String)){
		commandList_EditCommand ( __Commands_Output_ProcessTSProduct_String, null, CommandEditType.INSERT );
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
		commandList_EditCommand ( __Commands_HydroBase_OpenHydroBase_String, null, CommandEditType.INSERT );
	}

    // Table commands...
    
    else if (command.equals( __Commands_Table_NewTable_String) ) {
        commandList_EditCommand ( __Commands_Table_NewTable_String, null, CommandEditType.INSERT );
    }
    else if (command.equals( __Commands_Table_CopyTable_String) ) {
        commandList_EditCommand ( __Commands_Table_CopyTable_String, null, CommandEditType.INSERT );
    }
    else if (command.equals( __Commands_Table_ReadTableFromDataStore_String) ) {
        commandList_EditCommand ( __Commands_Table_ReadTableFromDataStore_String, null, CommandEditType.INSERT );
    }
    else if (command.equals( __Commands_Table_ReadTableFromDelimitedFile_String) ) {
        commandList_EditCommand ( __Commands_Table_ReadTableFromDelimitedFile_String, null, CommandEditType.INSERT );
    }
    else if (command.equals( __Commands_Table_ReadTableFromDBF_String) ) {
        commandList_EditCommand ( __Commands_Table_ReadTableFromDBF_String, null, CommandEditType.INSERT );
    }
    else if (command.equals( __Commands_Table_SetTimeSeriesPropertiesFromTable_String) ) {
        commandList_EditCommand ( __Commands_Table_SetTimeSeriesPropertiesFromTable_String, null, CommandEditType.INSERT );
    }
    else if (command.equals( __Commands_Table_CopyTimeSeriesPropertiesToTable_String) ) {
        commandList_EditCommand ( __Commands_Table_CopyTimeSeriesPropertiesToTable_String, null, CommandEditType.INSERT );
    }
    else if (command.equals( __Commands_Table_TimeSeriesToTable_String) ) {
        commandList_EditCommand ( __Commands_Table_TimeSeriesToTable_String, null, CommandEditType.INSERT );
    }
    else if (command.equals( __Commands_Table_ManipulateTableString_String) ) {
        commandList_EditCommand ( __Commands_Table_ManipulateTableString_String, null, CommandEditType.INSERT );
    }
    else if (command.equals( __Commands_Table_TableMath_String) ) {
        commandList_EditCommand ( __Commands_Table_TableMath_String, null, CommandEditType.INSERT );
    }
    else if (command.equals( __Commands_Table_TableTimeSeriesMath_String) ) {
        commandList_EditCommand ( __Commands_Table_TableTimeSeriesMath_String, null, CommandEditType.INSERT );
    }
    else if (command.equals( __Commands_Table_CompareTables_String) ) {
        commandList_EditCommand ( __Commands_Table_CompareTables_String, null, CommandEditType.INSERT );
    }
    else if (command.equals( __Commands_Table_WriteTableToDelimitedFile_String) ) {
        commandList_EditCommand ( __Commands_Table_WriteTableToDelimitedFile_String, null, CommandEditType.INSERT );
    }
    else if (command.equals( __Commands_Table_WriteTableToHTML_String) ) {
        commandList_EditCommand ( __Commands_Table_WriteTableToHTML_String, null, CommandEditType.INSERT );
    }
	
	// Template commands...

    else if (command.equals( __Commands_Template_ExpandTemplateFile_String) ) {
        commandList_EditCommand ( __Commands_Template_ExpandTemplateFile_String, null, CommandEditType.INSERT );
    }
	
	// View commands...

    else if (command.equals( __Commands_View_NewTreeView_String) ) {
        commandList_EditCommand ( __Commands_View_NewTreeView_String, null, CommandEditType.INSERT );
    }
	
	// General commands...

	else if (command.equals( __Commands_General_Logging_StartLog_String) ) {
		commandList_EditCommand ( __Commands_General_Logging_StartLog_String, null, CommandEditType.INSERT );
	}
	else if (command.equals( __Commands_General_Logging_SetDebugLevel_String) ) {
		commandList_EditCommand ( __Commands_General_Logging_SetDebugLevel_String, null, CommandEditType.INSERT );
	}
	else if (command.equals( __Commands_General_Logging_SetWarningLevel_String) ) {
		commandList_EditCommand ( __Commands_General_Logging_SetWarningLevel_String, null, CommandEditType.INSERT );
	}
	else if (command.equals( __Commands_General_Running_SetWorkingDir_String) ) {
		commandList_EditCommand ( __Commands_General_Running_SetWorkingDir_String, null, CommandEditType.INSERT );
	}
    else if (command.equals(__Commands_Check_CheckingResults_CheckTimeSeries_String) ) {
        commandList_EditCommand ( __Commands_Check_CheckingResults_CheckTimeSeries_String, null, CommandEditType.INSERT );
    }
    else if (command.equals(__Commands_Check_CheckingResults_CheckTimeSeriesStatistic_String) ) {
        commandList_EditCommand ( __Commands_Check_CheckingResults_CheckTimeSeriesStatistic_String, null, CommandEditType.INSERT );
    }
    else if (command.equals(__Commands_Check_CheckingResults_WriteCheckFile_String) ) {
        commandList_EditCommand ( __Commands_Check_CheckingResults_WriteCheckFile_String, null, CommandEditType.INSERT );
    }
	else if (command.equals(__Commands_General_Comments_Comment_String) ) {
		commandList_EditCommand ( __Commands_General_Comments_Comment_String, null, CommandEditType.INSERT );
	}
    else if (command.equals(__Commands_General_Comments_ExpectedStatusFailureComment_String) ) {
        // Most inserts let the editor format the command.  However, in this case the specific
        // comment needs to be supplied.  Otherwise, the comment will be blank or the string from
        // the menu, which has too much verbage.
        List<Command> comments = new Vector(1);
        comments.add ( commandList_NewCommand("#@expectedStatus Failure",true) );
        commandList_EditCommand ( __Commands_General_Comments_ExpectedStatusFailureComment_String, comments, CommandEditType.INSERT );
    }
    else if (command.equals(__Commands_General_Comments_ExpectedStatusWarningComment_String) ) {
        // Most inserts let the editor format the command.  However, in this case the specific
        // comment needs to be supplied.  Otherwise, the comment will be blank or the string from
        // the menu, which has too much verbage.
        List<Command> comments = new Vector(1);
        comments.add ( commandList_NewCommand("#@expectedStatus Warning",true) );
        commandList_EditCommand ( __Commands_General_Comments_ExpectedStatusWarningComment_String, comments, CommandEditType.INSERT );
    }
    else if (command.equals(__Commands_General_Comments_ReadOnlyComment_String) ) {
        // Most inserts let the editor format the command.  However, in this case the specific
        // comment needs to be supplied.  Otherwise, the comment will be blank or the string from
        // the menu, which has too much verbage.
        List<Command> comments = new Vector(1);
        comments.add ( commandList_NewCommand("#@readOnly",true) );
        commandList_EditCommand ( __Commands_General_Comments_ReadOnlyComment_String, comments, CommandEditType.INSERT );
    }
	else if (command.equals(__Commands_General_Comments_StartComment_String) ) {
		commandList_EditCommand ( __Commands_General_Comments_StartComment_String, null, CommandEditType.INSERT );
	}
	else if (command.equals(__Commands_General_Comments_EndComment_String) ) {
		commandList_EditCommand ( __Commands_General_Comments_EndComment_String,	null, CommandEditType.INSERT );
	}
	else if (command.equals(__Commands_General_Running_Exit_String) ) {
		commandList_EditCommand ( __Commands_General_Running_Exit_String, null, CommandEditType.INSERT );
	}
    else if (command.equals( __Commands_General_TestProcessing_StartRegressionTestResultsReport_String) ) {
        commandList_EditCommand ( __Commands_General_TestProcessing_StartRegressionTestResultsReport_String, null, CommandEditType.INSERT );
    }
    else if (command.equals( __Commands_General_Running_ReadPropertiesFromFile_String)){
        commandList_EditCommand ( __Commands_General_Running_ReadPropertiesFromFile_String, null, CommandEditType.INSERT );
    }
    else if (command.equals( __Commands_General_Running_SetProperty_String) ) {
        commandList_EditCommand ( __Commands_General_Running_SetProperty_String, null, CommandEditType.INSERT );
    }
    else if (command.equals( __Commands_General_Running_SetPropertyFromNwsrfsAppDefault_String) ) {
        commandList_EditCommand ( __Commands_General_Running_SetPropertyFromNwsrfsAppDefault_String, null, CommandEditType.INSERT );
    }
    else if (command.equals( __Commands_General_Running_FormatDateTimeProperty_String) ) {
        commandList_EditCommand ( __Commands_General_Running_FormatDateTimeProperty_String, null, CommandEditType.INSERT );
    }
    else if (command.equals( __Commands_General_Running_WritePropertiesToFile_String)){
        commandList_EditCommand ( __Commands_General_Running_WritePropertiesToFile_String, null, CommandEditType.INSERT );
    }
	else if (command.equals( __Commands_General_Running_RunCommands_String) ) {
		commandList_EditCommand ( __Commands_General_Running_RunCommands_String, null, CommandEditType.INSERT );
	}
	else if (command.equals( __Commands_General_Running_RunProgram_String) ) {
		commandList_EditCommand ( __Commands_General_Running_RunProgram_String, null, CommandEditType.INSERT );
	}
    else if (command.equals( __Commands_General_Running_RunPython_String) ) {
        commandList_EditCommand ( __Commands_General_Running_RunPython_String, null, CommandEditType.INSERT );
    }
    else if (command.equals( __Commands_General_Running_RunDSSUTL_String) ) {
        commandList_EditCommand ( __Commands_General_Running_RunDSSUTL_String, null, CommandEditType.INSERT );
    }
    else if (command.equals( __Commands_General_Running_Exit_String) ) {
        commandList_EditCommand ( __Commands_General_Running_Exit_String, null, CommandEditType.INSERT );
    }
    else if (command.equals( __Commands_General_FileHandling_FTPGet_String)){
        commandList_EditCommand ( __Commands_General_FileHandling_FTPGet_String, null, CommandEditType.INSERT );
    }
    else if (command.equals( __Commands_General_FileHandling_WebGet_String)){
        commandList_EditCommand ( __Commands_General_FileHandling_WebGet_String, null, CommandEditType.INSERT );
    }
    else if (command.equals( __Commands_General_FileHandling_AppendFile_String)){
        commandList_EditCommand ( __Commands_General_FileHandling_AppendFile_String, null, CommandEditType.INSERT );
    }
    else if (command.equals( __Commands_General_FileHandling_RemoveFile_String)){
        commandList_EditCommand ( __Commands_General_FileHandling_RemoveFile_String, null, CommandEditType.INSERT );
    }
    else if (command.equals( __Commands_General_FileHandling_PrintTextFile_String)){
        commandList_EditCommand ( __Commands_General_FileHandling_PrintTextFile_String, null, CommandEditType.INSERT );
    }
	else if (command.equals( __Commands_General_TestProcessing_CompareFiles_String)){
		commandList_EditCommand ( __Commands_General_TestProcessing_CompareFiles_String, null, CommandEditType.INSERT );
	}
	else if (command.equals( __Commands_General_TestProcessing_WriteProperty_String)){
		commandList_EditCommand ( __Commands_General_TestProcessing_WriteProperty_String, null, CommandEditType.INSERT );
	}
    else if (command.equals( __Commands_General_TestProcessing_WriteTimeSeriesProperty_String)){
        commandList_EditCommand ( __Commands_General_TestProcessing_WriteTimeSeriesProperty_String, null, CommandEditType.INSERT );
    }
	else if (command.equals( __Commands_General_TestProcessing_TestCommand_String) ) {
		commandList_EditCommand ( __Commands_General_TestProcessing_TestCommand_String, null, CommandEditType.INSERT );
	}
	else if (command.equals( __Commands_General_TestProcessing_CreateRegressionTestCommandFile_String) ) {
		commandList_EditCommand ( __Commands_General_TestProcessing_CreateRegressionTestCommandFile_String, null, CommandEditType.INSERT );
	}
	else {
		// Chain to other actions...
		uiAction_ActionPerformed15_RunMenu ( event );
	}
}

/**
Handle a group of actions for the Run menu.
@param event Event to handle.
*/
private void uiAction_ActionPerformed15_RunMenu (ActionEvent event)
throws Exception
{   String command = event.getActionCommand();
    String routine = getClass().getName() + ".uiAction_ActionPerformed4_RunMenu";

    // Run menu (order in menu)...

    if ( command.equals(__Run_AllCommandsCreateOutput_String) ) {
        // Process time series and create all output from Write* commands...
        uiAction_RunCommands ( true, true );
    }
    else if ( command.equals(__Run_AllCommandsIgnoreOutput_String) ) {
        // Process time series but ignore write* commands...
        uiAction_RunCommands ( true, false );
    }
    else if ( command.equals(__Run_SelectedCommandsCreateOutput_String) ) {
        // Process selected commands and create all output from Write* commands...
        uiAction_RunCommands ( false, true );
    }
    else if ( command.equals(__Run_SelectedCommandsIgnoreOutput_String) ) {
        // Process selected commands but ignore write* commands...
        uiAction_RunCommands ( false, false );
    }
    else if (command.equals(__Run_CancelCommandProcessing_String) ) {
        // Cancel the current processor.  This may take awhile to occur.
        ui_UpdateStatusTextFields ( 1, routine, "Processing is being canceled...", null, __STATUS_CANCELING );
        ui_UpdateStatus ( true );
        __tsProcessor.setCancelProcessingRequested ( true );
    }
    else if (command.equals(__Run_CancelAllCommandProcesses_String) ) {
        // Cancel all processes being run by commands - to fix hung processes
        commandProcessor_CancelAllCommandProcesses ();
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
    else {
        // Chain to the next method...
        uiAction_ActionPerformed16_ResultsMenu ( event );
    }
}

/**
Handle a group of actions for the View menu.
@param event Event to handle.
*/
private void uiAction_ActionPerformed16_ResultsMenu (ActionEvent event)
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
    else if ( command.equals(__Results_Graph_Area_String) ) {
        uiAction_GraphTimeSeriesResults("-oarea_graph");
    }
    else if ( command.equals(__Results_Graph_AreaStacked_String) ) {
        uiAction_GraphTimeSeriesResults("-oarea_stacked_graph");
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
    else if ( command.equals(__Results_Report_Summary_Html_String) ) {
        // SAM.  Let the ReportJFrame prompt for this.  This is
        // different from the StateMod output in that when a summary is
        // exported in the GUI, the user views first and then saves to disk.
        uiAction_ExportTimeSeriesResults("-osummaryhtml", "-preview" );
    }
    else if ( command.equals(__Results_Report_Summary_Text_String) ) {
		// SAM.  Let the ReportJFrame prompt for this.  This is
		// different from the StateMod output in that when a summary is
		// exported in the GUI, the user views first and then saves to disk.
		uiAction_ExportTimeSeriesResults("-osummary", "-preview" );
	}

	// Only on View pop-up...

	else if (command.equals(__Results_FindTimeSeries_String) ) {
		new FindInJListJDialog ( this, false, __resultsTS_JList, "Find Time Series" );
	}
    else if ( command.equals(__Results_TimeSeriesProperties_String) ) {
		// Get the first time series selected in the view window...
		if ( __tsProcessor != null ) {
			int pos = 0;
			if ( JGUIUtil.selectedSize(__resultsTS_JList) == 0 ) {
				pos = 0;
			}
			else {
                pos = JGUIUtil.selectedIndex(__resultsTS_JList,0);
			}
			// Now display the properties...
			if ( pos >= 0 ) {
				new TSPropertiesJFrame ( this, commandProcessor_GetTimeSeries(pos) );
			}
		}
	}
    else {
    	// Chain to next actions...
    	uiAction_ActionPerformed17_ToolsMenu ( event );
    }
}

/**
Handle a group of actions for the Tools menu.
@param event Event to handle.
*/
private void uiAction_ActionPerformed17_ToolsMenu (ActionEvent event)
throws Exception
{	Object o = event.getSource();
	String command = event.getActionCommand();
	String routine = "ToolsMenu";

	if ( o == __Tools_Analysis_MixedStationAnalysis_JMenuItem ) {
		// Create the dialog using the available time series results (accessed by the procesor)...
		try {
			new FillMixedStation_JDialog ( this, commandProcessor_GetCommandProcessor(), this );
		}
		catch ( Exception e ) {
			Message.printWarning ( 1, routine, "Error in Mixed Station Analysis tool (" + e + ")." );
			Message.printWarning ( 3, routine, e );
		}
	}
    else if ( o == __Tools_Analysis_PrincipalComponentAnalysis_JMenuItem ) {
		// Create the dialog using the available time series...
		try {
			new FillPrincipalComponentAnalysis_JDialog ( this, commandProcessor_GetCommandProcessor(), this );
		}
		catch ( Exception e ) {
			Message.printWarning ( 1, routine, "Error in Principal Component Analysis tool (" + e + ")." );
			Message.printWarning ( 3, routine, e );
		}
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
	    List<TSProductDMI> rdmiList = new Vector();
	    List<DataStore> dataStores = __tsProcessor.getDataStoresByType(RiversideDBDataStore.class);
	    for ( DataStore dataStore : dataStores ) {
    		rdmiList.add ( (RiversideDB_DMI)((DatabaseDataStore)dataStore).getDMI() );
	    }
		new RiversideDB_TSProductManager_JFrame ( rdmiList, null );
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
		new TSTool_Options_JDialog ( this, TSToolMain.getConfigFile(), __props );
		// Reset as necessary...
		if ( __query_TableModel instanceof TSTool_HydroBase_StructureGeolocStructMeasType_TableModel){
			((TSTool_HydroBase_StructureGeolocStructMeasType_TableModel)__query_TableModel).setWDIDLength(
			    StringUtil.atoi( __props.getValue("HydroBase.WDIDLength")));
		}
		else if ( __query_TableModel instanceof TSTool_HydroBase_GroundWaterWellsView_TableModel){
            ((TSTool_HydroBase_GroundWaterWellsView_TableModel)__query_TableModel).setWDIDLength(
                StringUtil.atoi( __props.getValue("HydroBase.WDIDLength")));
        }
		else if ( __query_TableModel instanceof TSTool_HydroBase_WellLevel_Day_TableModel){
			((TSTool_HydroBase_WellLevel_Day_TableModel)
			__query_TableModel).setWDIDLength(StringUtil.atoi(__props.getValue("HydroBase.WDIDLength")));
		}		
	}
	else {
		// Chain to remaining actions...
		uiAction_ActionPerformed18_HelpMenu ( event );
	}
}

/**
Handle a group of actions for the Tools menu.
@param event Event to handle.
*/
private void uiAction_ActionPerformed18_HelpMenu (ActionEvent event)
throws Exception
{	//Object o = event.getSource();
	String command = event.getActionCommand();

	// Help menu (order of GUI)...

	if ( command.equals ( __Help_AboutTSTool_String )) {
		uiAction_ShowHelpAbout ( license_GetLicenseManager() );
	}
	else if ( command.equals ( __Help_ViewDocumentation_String )) {
        uiAction_ViewDocumentation ();
    }
    else if ( command.equals ( __Help_ViewTrainingMaterials_String )) {
        uiAction_ViewTrainingMaterials ();
    }
	else if ( command.equals ( __Help_ImportConfiguration_String )) {
        uiAction_ImportConfiguration ( IOUtil.getApplicationHomeDir() + File.separator + "system" +
            File.separator + "TSTool.cfg");
    }
}

/**
Convert selected commands to/from comments.  When converting to commands:
Each Command instance is retrieved, its command string is taken from the Command, and
a new GenericCommand is create, and the original Command is replaced in the list.
When converting from commands, the GenericCommand is retrieved, a new Command instance
is created, the original Command is replaced.
@param to_comment If true, convert commands to comments, if false, from comments.
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
			// Check the status so that it will have a valid status and not "unknown", which triggers
			// a decorator to be displayed (no reason to show decorator for comment)
	        try {
	            new_command.checkCommandParameters(null, "", 3);
	        }
	        catch ( Exception e ) {
	            // Should not happen.
	        }
			commandList_ReplaceCommand ( old_command, new_command );
		}
		else {
		    // Remove comment...
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
	__commandsCutBuffer.clear();

	// Transfer Command instances to the cut buffer...
	Command command = null;	// Command instance to process
	for ( int i = 0; i < size; i++ ) {
		command = (Command)__commands_JListModel.get(selected_indices[i]);
		__commandsCutBuffer.add ( (Command)command.clone() );
	}
	
	if ( remove_original ) {
		// If removing, delete the selected commands from the list...
		commandList_RemoveCommandsBasedOnUI();
	}
}

/**
The data store choice has been clicked so process the event.
The only entry point to this method is if the user actually clicks on the choice.
In this case, the input type/name choices will be set to blank because the user has
made a decision to work with a data store.  If they subsequently choose to work with an input type, then
they would select an input and the data store choice would be blanked.
*/
private void uiAction_DataStoreChoiceClicked()
{   String routine = getClass().getName() + ".uiAction_DataStoreChoiceClicked";
    if ( __dataStore_JComboBox == null ) {
        if ( Message.isDebugOn ) {
            Message.printDebug ( 1, routine, "Data store has been selected but GUI is not yet initialized.");
        }
        return; // Not done initializing.
    }
    String selectedDataStoreName = __dataStore_JComboBox.getSelected();
    Message.printStatus(2, routine, "Selected data store \"" + selectedDataStoreName + "\"." );
    if ( selectedDataStoreName.equals("") ) {
        // Selected blank for some reason - do nothing
        return;
    }
    DataStore selectedDataStore = ui_GetSelectedDataStore();
    // This will select blank input type and name so that the focus is on the selected data store...
    uiAction_InputTypeChoiceClicked(selectedDataStore);
    // Now fully initialize the input/query information based on the data store
    try {
        if ( selectedDataStore instanceof ColoradoWaterHBGuestDataStore ) {
            uiAction_SelectDataStore_ColoradoWaterHBGuest ( (ColoradoWaterHBGuestDataStore)selectedDataStore );
        }
        else if ( selectedDataStore instanceof ColoradoWaterSMSDataStore ) {
            uiAction_SelectDataStore_ColoradoWaterSMS ( (ColoradoWaterSMSDataStore)selectedDataStore );
        }
        else if ( selectedDataStore instanceof HydroBaseDataStore ) {
            uiAction_SelectDataStore_HydroBase ( (HydroBaseDataStore)selectedDataStore );
        }
        else if ( selectedDataStore instanceof RccAcisDataStore ) {
            uiAction_SelectDataStore_RccAcis ( (RccAcisDataStore)selectedDataStore );
        }
        else if ( selectedDataStore instanceof ReclamationHDBDataStore ) {
            uiAction_SelectDataStore_ReclamationHDB ( (ReclamationHDBDataStore)selectedDataStore );
        }
        else if ( selectedDataStore instanceof RiversideDBDataStore ) {
            uiAction_SelectDataStore_RiversideDB ( (RiversideDBDataStore)selectedDataStore );
        }
        else if ( selectedDataStore instanceof UsgsNwisDailyDataStore ) {
            uiAction_SelectDataStore_UsgsNwisDaily ( (UsgsNwisDailyDataStore)selectedDataStore );
        }
    }
    catch ( Exception e ) {
        Message.printWarning( 2, routine, "Error selecting data store \"" + selectedDataStore.getName() + "\"" );
        Message.printWarning ( 3, routine, e );
    }
}

/**
This function fills in appropriate selections for the time step and choices.
This will result in a call to timestepChoiceClicked(), which will display input filters if appropriate.
*/
private void uiAction_DataTypeChoiceClicked()
{	String rtn = "TSTool_JFrame.uiAction_DataTypeChoiceClicked";
	if ( (__input_type_JComboBox == null) || (__dataType_JComboBox == null) ) {
		if ( Message.isDebugOn ) {
			Message.printDebug ( 1, rtn, "Data type has been selected but GUI is not initialized." );
		}
		return;	// Not done initializing.
	}
	String selectedInputType = ui_GetSelectedInputType();
	String selectedDataType = ui_GetSelectedDataType ();
	DataStore selectedDataStore = ui_GetSelectedDataStore();
	if ( selectedDataType == null ) {
		// Apparently this happens when setData() or similar is called
		// on the JComboBox, and before select() is called.
		if ( Message.isDebugOn ) {
			Message.printDebug ( 1, rtn, "Data type has been selected:  null (select is ignored)" );
		}
		return;
	}

	if ( Message.isDebugOn ) {
		Message.printDebug ( 1, rtn, "Data type has been selected for read:  \"" + selectedDataType + "\"" );
	}

	// Set the appropriate settings for the current data input type and data type...

    if ( (selectedDataStore != null) && (selectedDataStore instanceof ColoradoWaterHBGuestDataStore) ) {
	    ColoradoWaterHBGuestDataStore cwds = (ColoradoWaterHBGuestDataStore)selectedDataStore;
        List<String> time_steps = cwds.getColoradoWaterHBGuestService().getTimeSeriesTimeSteps (
            selectedDataType,
            //HydroBase_Util.DATA_TYPE_AGRICULTURE |
            //HydroBase_Util.DATA_TYPE_DEMOGRAPHICS_ALL |
            //HydroBase_Util.DATA_TYPE_HARDWARE |
            //HydroBase_Util.DATA_TYPE_STATION_ALL |
            HydroBase_Util.DATA_TYPE_STRUCTURE_ALL //|
            //HydroBase_Util.DATA_TYPE_WIS
            );
        __timeStep_JComboBox.setData ( time_steps );
        __timeStep_JComboBox.select ( null );
        __timeStep_JComboBox.setEnabled ( true );
        // Select monthly as the default if available...
        if ( JGUIUtil.isSimpleJComboBoxItem(__timeStep_JComboBox,"Month", JGUIUtil.NONE, null, null ) ) {
            __timeStep_JComboBox.select ( "Month" );
        }
        else {
            // Select the first item...
            try {
                __timeStep_JComboBox.select ( 0 );
            }
            catch ( Exception e ) {
                // For cases when for some reason no choice is available.
                __timeStep_JComboBox.setEnabled ( false );
            }
        }
        // TODO SAM 2012-05-03 This is an old comment but need to do as it says
        // If the data type is for a diversion or reservoir data type,
        // hide the abbreviation column in the table model.  Else show the column.
    }
    else if ( selectedInputType.equals(__INPUT_TYPE_DateValue) ) {
		// DateValue file...
		__timeStep_JComboBox.removeAll ();
		__timeStep_JComboBox.add ( __TIMESTEP_AUTO );
		__timeStep_JComboBox.select ( null );
		__timeStep_JComboBox.select ( __TIMESTEP_AUTO );
		__timeStep_JComboBox.setEnabled ( false );
	}
	else if ( selectedInputType.equals(__INPUT_TYPE_DIADvisor) ) {
		// The time steps are always available in both the operational
		// and archive database.  However, "DataValue2" is only
		// available in the operational database.  Also, regular
		// interval data seem to only be available for Rain group.
		String group =StringUtil.getToken(selectedDataType,"-",0,0);
		__timeStep_JComboBox.removeAll ();
		__timeStep_JComboBox.setEnabled ( true );
		if ( selectedDataType.endsWith("DataValue") && group.equalsIgnoreCase("Rain") ) {
			__timeStep_JComboBox.add ( __TIMESTEP_DAY );
			__timeStep_JComboBox.add ( __TIMESTEP_HOUR );
		}
		__timeStep_JComboBox.add ( __TIMESTEP_IRREGULAR );
		if ( selectedDataType.endsWith("DataValue") && group.equalsIgnoreCase("Rain") ) {
			try {
                DIADvisor_SysConfig config = __DIADvisor_dmi.readSysConfig();
				__timeStep_JComboBox.add ( "" + config.getInterval() +	__TIMESTEP_MINUTE );
			}
			catch ( Exception e ) {
				Message.printWarning ( 2, rtn, "Could not determine DIADvisor interval for time step choice." );
				Message.printWarning ( 2, rtn, e );
			}
		}
		__timeStep_JComboBox.select ( null );
		__timeStep_JComboBox.select ( 0 );
	}
    else if ( selectedInputType.equals(__INPUT_TYPE_HECDSS) ) {
        // HEC-DSS database file - the time step is set when the
        // input name is selected so do nothing here.
    }
	else if ( selectedInputType.equals(__INPUT_TYPE_HydroBase) ) {
	    List<String> time_steps = HydroBase_Util.getTimeSeriesTimeSteps (ui_GetHydroBaseDMI(),
	        selectedDataType,
			HydroBase_Util.DATA_TYPE_AGRICULTURE |
			HydroBase_Util.DATA_TYPE_DEMOGRAPHICS_ALL |
			HydroBase_Util.DATA_TYPE_HARDWARE |
			HydroBase_Util.DATA_TYPE_STATION_ALL |
			HydroBase_Util.DATA_TYPE_STRUCTURE_ALL |
			HydroBase_Util.DATA_TYPE_WIS );
		__timeStep_JComboBox.setData ( time_steps );
		__timeStep_JComboBox.select ( null );
		__timeStep_JComboBox.setEnabled ( true );
		// Select monthly as the default if available...
		if ( JGUIUtil.isSimpleJComboBoxItem(__timeStep_JComboBox,"Month", JGUIUtil.NONE, null, null ) ) {
			__timeStep_JComboBox.select ( "Month" );
		}
		else {
		    // Select the first item...
			try {
                __timeStep_JComboBox.select ( 0 );
			}
			catch ( Exception e ) {
				// For cases when for some reason no choice is available.
				__timeStep_JComboBox.setEnabled ( false );
			}
		}
		// If the data type is for a diversion or reservoir data type,
		// hide the abbreviation column in the table model.  Else show the column.
	}
	else if ( selectedInputType.equals(__INPUT_TYPE_MEXICO_CSMN) ) {
		// Mexico CSMN file...
		__timeStep_JComboBox.removeAll ();
		__timeStep_JComboBox.add ( __TIMESTEP_DAY );
		__timeStep_JComboBox.select ( null );
		__timeStep_JComboBox.select ( __TIMESTEP_DAY );
		__timeStep_JComboBox.setEnabled ( true );
	}
	else if ( selectedInputType.equals(__INPUT_TYPE_MODSIM) ) {
		// MODSIM file...
		__timeStep_JComboBox.removeAll ();
		__timeStep_JComboBox.add ( __TIMESTEP_AUTO );
		__timeStep_JComboBox.select ( null );
		__timeStep_JComboBox.select ( __TIMESTEP_AUTO );
		__timeStep_JComboBox.setEnabled ( false );
	}
	else if ( selectedInputType.equals(__INPUT_TYPE_NWSCARD) ) {
		// NWSCard file...
		__timeStep_JComboBox.removeAll ();
		__timeStep_JComboBox.add ( __TIMESTEP_HOUR );
		__timeStep_JComboBox.select ( null );
		__timeStep_JComboBox.select ( __TIMESTEP_HOUR );
		__timeStep_JComboBox.setEnabled ( false );
	}
	else if ( selectedInputType.equals(__INPUT_TYPE_NWSRFS_FS5Files) ) {
		// Time steps are determined from the system...
	    List<String> time_steps = NWSRFS_Util.getDataTypeIntervals ( __nwsrfs_dmi, selectedDataType );
		__timeStep_JComboBox.setData ( time_steps );
		__timeStep_JComboBox.select ( null );
		__timeStep_JComboBox.setEnabled ( true );
		// Select 6Hour as the default if available...
		if ( JGUIUtil.isSimpleJComboBoxItem(__timeStep_JComboBox,"6Hour", JGUIUtil.NONE, null, null ) ) {
			__timeStep_JComboBox.select ( "6Hour" );
		}
		else {
            // Select the first item...
			try {
                __timeStep_JComboBox.select ( 0 );
			}
			catch ( Exception e ) {
				// For cases when for some reason no choice is available.
				__timeStep_JComboBox.setEnabled ( false );
			}
		}
	}
	else if ( selectedInputType.equals( __INPUT_TYPE_NWSRFS_ESPTraceEnsemble) ){
		// ESP Trace Ensemble file...
		__timeStep_JComboBox.removeAll ();
		__timeStep_JComboBox.add ( __TIMESTEP_AUTO );
		__timeStep_JComboBox.select ( null );
		__timeStep_JComboBox.select ( __TIMESTEP_AUTO );
		__timeStep_JComboBox.setEnabled ( false );
	}
    else if ( (selectedDataStore != null) && (selectedDataStore instanceof RccAcisDataStore)) {
        // Set intervals for the data type and trigger a select to populate the input filters
        RccAcisDataStore dataStore = (RccAcisDataStore)selectedDataStore;
        __timeStep_JComboBox.removeAll ();
        __timeStep_JComboBox.setEnabled ( true );
        __timeStep_JComboBox.setData ( dataStore.getDataIntervalStringsForDataType(ui_GetSelectedDataType()));
        __timeStep_JComboBox.select ( null );
        __timeStep_JComboBox.select ( 0 );
    }
    else if ( (selectedDataStore != null) && (selectedDataStore instanceof ReclamationHDBDataStore)) {
        // Time steps are determined from the database based on the data type that is selected...
        /** FIXME SAM 2010-10-19 Need to enable
        String data_type = StringUtil.getToken(__dataType_JComboBox.getSelected()," ",0,0).trim();
        List<RiversideDB_MeasType> v = null;
        DataStore dataStore = ui_GetSelectedDataStore();
        RiversideDB_DMI rdmi = (RiversideDB_DMI)((DatabaseDataStore)dataStore).getDMI();
        try {
            v = rdmi.readMeasTypeListForTSIdent ( ".." + data_type + ".." );
        }
        catch ( Exception e ) {
            Message.printWarning(2, rtn, "Error getting time steps from RiversideDB \"" +
                selectedDataStore.getName() + ".");
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
        __timeStep_JComboBox.removeAll ();
        if ( size > 0 ) {
            for ( int i = 0; i < size; i++ ) {
                mt = v.get(i);
                // Only add if not already listed. Alternatively - add a "distinct" query
                time_step_base = mt.getTime_step_base();
                time_step_mult = mt.getTime_step_mult();
                if ( time_step_base.equalsIgnoreCase( "IRREGULAR") || DMIUtil.isMissing(time_step_mult) ) {
                    timestep = mt.getTime_step_base();
                }
                else {
                    timestep = "" + mt.getTime_step_mult() + mt.getTime_step_base();
                }
                if ( !JGUIUtil.isSimpleJComboBoxItem(__timeStep_JComboBox, timestep, JGUIUtil.NONE, null, null)){
                    __timeStep_JComboBox.add(timestep);
                }
            }
            __timeStep_JComboBox.select ( null );
            __timeStep_JComboBox.select ( 0 );
            __timeStep_JComboBox.setEnabled ( true );
        }
        else {
            __timeStep_JComboBox.setEnabled ( false );
        }
        */
    }
	else if ( (selectedDataStore != null) && (selectedDataStore instanceof RiversideDBDataStore)) {
		// Time steps are determined from the database based on the data type that is selected...
		String data_type = StringUtil.getToken(__dataType_JComboBox.getSelected()," ",0,0).trim();
		List<RiversideDB_MeasType> v = null;
		DataStore dataStore = ui_GetSelectedDataStore();
		RiversideDB_DMI rdmi = (RiversideDB_DMI)((DatabaseDataStore)dataStore).getDMI();
		try {
            v = rdmi.readMeasTypeListForTSIdent ( ".." + data_type + ".." );
		}
		catch ( Exception e ) {
			Message.printWarning(2, rtn, "Error getting time steps from RiversideDB \"" +
			    selectedDataStore.getName() + ".");
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
		__timeStep_JComboBox.removeAll ();
		if ( size > 0 ) {
			for ( int i = 0; i < size; i++ ) {
				mt = v.get(i);
				// Only add if not already listed. Alternatively - add a "distinct" query
				time_step_base = mt.getTime_step_base();
				time_step_mult = mt.getTime_step_mult();
				if ( time_step_base.equalsIgnoreCase( "IRREGULAR") || DMIUtil.isMissing(time_step_mult) ) {
					timestep = mt.getTime_step_base();
				}
				else {
                    timestep = "" + mt.getTime_step_mult() + mt.getTime_step_base();
				}
				if ( !JGUIUtil.isSimpleJComboBoxItem(__timeStep_JComboBox, timestep, JGUIUtil.NONE, null, null)){
					__timeStep_JComboBox.add(timestep);
				}
			}
			__timeStep_JComboBox.select ( null );
			__timeStep_JComboBox.select ( 0 );
			__timeStep_JComboBox.setEnabled ( true );
		}
		else {
            __timeStep_JComboBox.setEnabled ( false );
		}
	}
	else if ( selectedInputType.equals(__INPUT_TYPE_RiverWare) ) {
		// RiverWare file...
		__timeStep_JComboBox.removeAll ();
		__timeStep_JComboBox.add ( __TIMESTEP_AUTO );
		__timeStep_JComboBox.select ( null );
		__timeStep_JComboBox.select ( __TIMESTEP_AUTO );
		__timeStep_JComboBox.setEnabled ( false );
	}
	else if ( selectedInputType.equals(__INPUT_TYPE_StateCU) ) {
		// StateCU files...
		__timeStep_JComboBox.removeAll ();
		__timeStep_JComboBox.add ( __TIMESTEP_AUTO );
		__timeStep_JComboBox.setEnabled ( false );
	}
    else if ( selectedInputType.equals(__INPUT_TYPE_StateCUB) ) {
        // StateCU binary output file - the time step is set when the
        // input name is selected so do nothing here.
    }
	else if ( selectedInputType.equals(__INPUT_TYPE_StateMod) ) {
		__timeStep_JComboBox.removeAll ();
		__timeStep_JComboBox.add ( __TIMESTEP_DAY );
		__timeStep_JComboBox.add ( __TIMESTEP_MONTH );
		__timeStep_JComboBox.select ( null );
		__timeStep_JComboBox.select ( __TIMESTEP_MONTH );
		__timeStep_JComboBox.setEnabled ( true );
	}
	else if ( selectedInputType.equals(__INPUT_TYPE_StateModB) ) {
		// StateMod binary output file - the time step is set when the
		// input name is selected so do nothing here.
	}
	else if ( selectedInputType.equals(__INPUT_TYPE_UsgsNwisRdb) ) {
		// USGS NWIS file...
		__timeStep_JComboBox.removeAll ();
		__timeStep_JComboBox.add ( __TIMESTEP_AUTO );
		__timeStep_JComboBox.select ( null );
		__timeStep_JComboBox.select ( __TIMESTEP_AUTO );
		__timeStep_JComboBox.setEnabled ( false );
	}
    else if ( (selectedDataStore != null) && (selectedDataStore instanceof UsgsNwisDailyDataStore)) {
        // Set intervals for the data type and trigger a select to populate the input filters
        UsgsNwisDailyDataStore dataStore = (UsgsNwisDailyDataStore)selectedDataStore;
        __timeStep_JComboBox.removeAll ();
        __timeStep_JComboBox.setEnabled ( true );
        __timeStep_JComboBox.setData ( dataStore.getDataIntervalStringsForDataType(ui_GetSelectedDataType()));
        __timeStep_JComboBox.select ( null );
        __timeStep_JComboBox.select ( 0 );
    }

	// Set the filter where clauses based on the data type changing (this only triggers a reset of the
    // input filter for some input types/data stores, like HydroBase, which has different input filters
    // for different data types)...

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
		List v = null;
		if ( command instanceof Comment_Command ) {
			// Allow multiple lines to be edited in a comment...
			// This is handled in the called method, which brings up a multi-line editor for comments.
            // Only edit the contiguous # block. The first one is a # but stop adding when lines no longer
			// start with #
			v = new Vector ( selected_size );
			for ( int i = 0; i < selected_size; i++ ) {
				command = (Command)__commands_JListModel.get(selected[i]);
				if ( !(command instanceof Comment_Command) ) {
					break;
				}
				// Else add command to the list.
				v.add ( command );
			}
		}
		else {
            // Commands are one line...
			v = new Vector ( 1 );
			v.add ( command );
		}
		commandList_EditCommand ( "", v, CommandEditType.UPDATE ); // No action event from menus
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
	 if ( __tsProcessor != null ) {
		try {
            int selected_ts = JGUIUtil.selectedSize(__resultsTS_JList);
			if ( selected_ts == 0 ) {
				commandProcessor_ProcessTimeSeriesResultsList(null,props);
			}
			else {
                commandProcessor_ProcessTimeSeriesResultsList (	__resultsTS_JList.getSelectedIndices(), props );
			}
		}
		catch ( Exception e ) {
			Message.printWarning ( 1, routine, "Unable to save time series (" + e + ")." );
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
	if ( !TSToolMain.isRestletServer() && !IOUtil.isBatch() ) {
		if ( __commandsDirty ) {
			if ( __commandFileName == null ) {
				// Have not been saved before...
				x = ResponseJDialog.NO;
				if ( __commands_JListModel.size() > 0 ) {
	                if ( __tsProcessor.getReadOnly() ) {
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
					uiAction_WriteCommandFile (	__commandFileName, true, false );
				}
			}
			else {
                // A command file exists...  Warn the user. They can save to the existing file name or
				// can cancel and File...Save As... to a different name.  Have not been saved before...
				x = ResponseJDialog.NO;
				if ( __commands_JListModel.size() > 0 ) {
				    if ( __tsProcessor.getReadOnly() ) {
                        x = new ResponseJDialog ( this, IOUtil.getProgramName(),
                                "The command file is marked read-only.  Changes cannot be saved.\n" +
                                "Press Cancel and then save to a new name if desired.",
                                ResponseJDialog.OK|ResponseJDialog.CANCEL).response();
                    }
                    else {
                        x = new ResponseJDialog ( this,
                                IOUtil.getProgramName(), "Do you want to save the changes made to\n\""
                                + __commandFileName + "\"?",
                                ResponseJDialog.YES| ResponseJDialog.NO|ResponseJDialog.CANCEL).response();
                    }
				}
				if ( x == ResponseJDialog.CANCEL ) {
					setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
					return;
				}
				else if ( x == ResponseJDialog.YES ) {
					uiAction_WriteCommandFile (	__commandFileName, false, false );
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
		// Close global data connections - need to figure out where to put this to work for batch mode also.
		__tsProcessor.closeDataConnections(true);
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
    String selectedInputType = ui_GetSelectedInputType();
    DataStore selectedDataStore = ui_GetSelectedDataStore();
	Message.printStatus ( 1, routine, "Getting time series list from " + selectedInputType + " input type..." );

	// Verify that the input filters have valid data...

	if ( __selectedInputFilter_JPanel != __inputFilterGeneric_JPanel ) {
		if ( !((InputFilter_JPanel)__selectedInputFilter_JPanel).checkInput(true) ) {
			// An input error was detected so don't get the time series...
			return;
		}
	}

	// Read the time series list and display in the Time Series List
	// area.  Return if an error occurs because the message at the bottom
	// should only be printed if successful.

    if ( (selectedDataStore != null) && (selectedDataStore instanceof ColoradoWaterHBGuestDataStore) ) {
        try {
            uiAction_GetTimeSeriesListClicked_ReadColoradoWaterHBGuestHeaders ();
        }
        catch ( Exception e ) {
            message = "Error reading ColoradoWaterHBGuest web service - cannot display time series list (" + e + ").";
            Message.printWarning ( 1, routine, message );
            Message.printWarning ( 3, routine, e );
            return;
        }
    }
	else if ( (selectedDataStore != null) && (selectedDataStore instanceof ColoradoWaterSMSDataStore) ) {
        try {
            uiAction_GetTimeSeriesListClicked_ReadColoradoWaterSMSHeaders ();
        }
        catch ( Exception e ) {
            message = "Error reading ColoradoWaterSMS web service - cannot display time series list (" + e + ").";
            Message.printWarning ( 1, routine, message );
            Message.printWarning ( 3, routine, e );
            return;
        }
    }
	else if ( selectedInputType.equals (__INPUT_TYPE_DateValue)) {
		try {
            uiAction_GetTimeSeriesListClicked_ReadDateValueHeaders ();
		}
		catch ( Exception e ) {
			message = "Error reading DateValue file - cannot display time series list (" + e + ").";
			Message.printWarning ( 1, routine, message );
			Message.printWarning ( 3, routine, e );
			return;
		}
	}
	else if ( selectedInputType.equals (__INPUT_TYPE_DIADvisor)) {
		try {
            uiAction_GetTimeSeriesListClicked_ReadDIADvisorHeaders(); 
		}
		catch ( Exception e ) {
			message = "Error reading DIADvisor - cannot display time series list (" + e + ").";
			Message.printWarning ( 1, routine, message );
			Message.printWarning ( 3, routine, e );
			return;
		}
	}
    else if ( selectedInputType.equals (__INPUT_TYPE_HECDSS)) {
        try {
            uiAction_GetTimeSeriesListClicked_ReadHECDSSHeaders ();
        }
        catch ( Exception e ) {
            message = "Error reading HEC-DSS file - cannot display time series list (" + e + ").";
            Message.printWarning ( 1, routine, message );
            Message.printWarning ( 3, routine, e );
            return;
        }
    }
	else if ( selectedInputType.equals (__INPUT_TYPE_HydroBase)) {
		try {
		    GRLimits limits = null;
            uiAction_GetTimeSeriesListClicked_ReadHydroBaseHeaders ( limits ); 
		}
		catch ( Exception e ) {
			message = "Error reading HydroBase - cannot display time series list (" + e + ").";
			Message.printWarning ( 1, routine, message );
			Message.printWarning ( 3, routine, e );
			return;
		}
	}
    else if ( (selectedDataStore != null) && (selectedDataStore instanceof HydroBaseDataStore) ) {
        try {
            GRLimits limits = null;
            uiAction_GetTimeSeriesListClicked_ReadHydroBaseHeaders( limits ); 
        }
        catch ( Exception e ) {
            message = "Error reading time series from HydroBase \"" + selectedDataStore.getName() +
                "\" - cannot display time series list (" + e + ").";
            Message.printWarning ( 1, routine, message );
            Message.printWarning ( 3, routine, e );
            return;
        }
    }
	else if ( selectedInputType.equals (__INPUT_TYPE_MEXICO_CSMN)) {
		try {
            uiAction_GetTimeSeriesListClicked_ReadMexicoCSMNHeaders ();
		}
		catch ( Exception e ) {
			message =
			"Error reading Mexico CSMN catalog file - cannot display time series list (" + e + ").";
			Message.printWarning ( 1, routine, message );
			Message.printWarning ( 3, routine, e );
			return;
		}
	}
	else if ( selectedInputType.equals (__INPUT_TYPE_MODSIM)) {
		try {
            uiAction_GetTimeSeriesListClicked_ReadMODSIMHeaders ();
		}
		catch ( Exception e ) {
			message = "Error reading MODSIM file - cannot display time series list (" + e + ").";
			Message.printWarning ( 1, routine, message );
			Message.printWarning ( 3, routine, e );
			return;
		}
	}
	else if ( selectedInputType.equals (__INPUT_TYPE_NWSCARD)) {
		try {
            uiAction_GetTimeSeriesListClicked_ReadNWSCARDHeaders ();
		}
		catch ( Exception e ) {
			message = "Error reading NWS CARD file - cannot display time series list )" + e + ").";
			Message.printWarning ( 1, routine, message );
			Message.printWarning ( 3, routine, e );
			return;
		}
	}
	else if ( selectedInputType.equals( __INPUT_TYPE_NWSRFS_ESPTraceEnsemble)) {
		try {
            uiAction_GetTimeSeriesListClicked_ReadNwsrfsEspTraceEnsembleHeaders ();
		}
		catch ( Exception e ) {
			message = "Error reading NWSRFS_ESPTraceEnsemble file - cannot display time series list (" + e + ").";
			Message.printWarning ( 1, routine, message );
			Message.printWarning ( 3, routine, e );
			return;
		}
	}
	else if ( selectedInputType.equals (__INPUT_TYPE_NWSRFS_FS5Files)) {
		try {
            // This reads the time series headers and displays the results in the list...
			uiAction_GetTimeSeriesListClicked_ReadNWSRFSFS5FilesHeaders ();
		}
		catch ( Exception e ) {
			message = "Error reading NWSRFS FS5Files time series list - cannot display time series list (" + e + ").";
			Message.printWarning ( 1, routine, message );
			Message.printWarning ( 3, routine, e );
			return;
		}
	}
    else if ( (selectedDataStore != null) && (selectedDataStore instanceof RccAcisDataStore) ) {
        try {
            uiAction_GetTimeSeriesListClicked_ReadRccAcisHeaders(); 
        }
        catch ( Exception e ) {
            message = "Error reading RCC ACIS - cannot display time series list (" + e + ").";
            Message.printWarning ( 1, routine, message );
            Message.printWarning ( 3, routine, e );
            return;
        }
    }
	else if ( (selectedDataStore != null) && (selectedDataStore instanceof ReclamationHDBDataStore) ) {
        try {
            uiAction_GetTimeSeriesListClicked_ReadReclamationHDBHeaders(); 
        }
        catch ( Exception e ) {
            message = "Error reading ReclamationHDB - cannot display time series list (" + e + ").";
            Message.printWarning ( 1, routine, message );
            Message.printWarning ( 3, routine, e );
            return;
        }
    }
	else if ( (selectedDataStore != null) && (selectedDataStore instanceof RiversideDBDataStore) ) {
		try {
            uiAction_GetTimeSeriesListClicked_ReadRiversideDBHeaders(); 
		}
		catch ( Exception e ) {
			message = "Error reading time series from RiversideDB \"" + selectedDataStore.getName() +
			    "\" - cannot display time series list (" + e + ").";
			Message.printWarning ( 1, routine, message );
			Message.printWarning ( 3, routine, e );
			return;
		}
	}
    else if ( (selectedDataStore != null) && (selectedDataStore instanceof UsgsNwisDailyDataStore) ) {
        try {
            uiAction_GetTimeSeriesListClicked_ReadUsgsNwisDailyHeaders(); 
        }
        catch ( Exception e ) {
            message = "Error reading USGS NWIS - cannot display time series list (" + e + ").";
            Message.printWarning ( 1, routine, message );
            Message.printWarning ( 3, routine, e );
            return;
        }
    }
	else if ( selectedInputType.equals (__INPUT_TYPE_RiverWare)) {
		try {
            uiAction_GetTimeSeriesListClicked_ReadRiverWareHeaders ();
		}
		catch ( Exception e ) {
			message = "Error reading RiverWare file - cannot display time series list (" + e + ").";
			Message.printWarning ( 1, routine, message );
			Message.printWarning ( 3, routine, e );
			return;
		}
	}
	else if ( selectedInputType.equals (__INPUT_TYPE_StateCU)) {
		try {
            uiAction_GetTimeSeriesListClicked_ReadStateCUHeaders ();
		}
		catch ( Exception e ) {
			message = "Error reading StateCU file - cannot display time series list (" + e + ").";
			Message.printWarning ( 1, routine, message );
			Message.printWarning ( 3, routine, e );
			return;
		}
	}
   else if ( selectedInputType.equals (__INPUT_TYPE_StateCUB)) {
        try {
            uiAction_GetTimeSeriesListClicked_ReadStateCUBHeaders ();
        }
        catch ( Exception e ) {
            message = "Error reading StateCU binary file - cannot display time series list (" + e + ").";
            Message.printWarning ( 1, routine, message );
            Message.printWarning ( 3, routine, e );
            return;
        }
    }
	else if ( selectedInputType.equals (__INPUT_TYPE_StateMod)) {
		try {
            uiAction_GetTimeSeriesListClicked_ReadStateModHeaders ();
		}
		catch ( Exception e ) {
			message = "Error reading StateMod file - cannot display time series list (" + e + ").";
			Message.printWarning ( 1, routine, message );
			Message.printWarning ( 3, routine, e );
			return;
		}
	}
	else if ( selectedInputType.equals (__INPUT_TYPE_StateModB)) {
		try {
            uiAction_GetTimeSeriesListClicked_ReadStateModBHeaders ();
		}
		catch ( Exception e ) {
			message = "Error reading StateMod binary file - cannot display time series list (" + e + ").";
			Message.printWarning ( 1, routine, message );
			Message.printWarning ( 3, routine, e );
			return;
		}
	}
	else if ( selectedInputType.equals (__INPUT_TYPE_UsgsNwisRdb)) {
		try {
            uiAction_GetTimeSeriesListClicked_ReadUsgsNwisRdbHeaders ();
		}
		catch ( Exception e ) {
			message = "Error reading USGS NWIS file - cannot display time series list (" + e + ").";
			Message.printWarning ( 1, routine, message );
			Message.printWarning ( 3, routine, e );
			return;
		}
	}
	else {
	    Message.printWarning(1,routine,
	        "Getting time series list for input type \"" + selectedInputType + "\" is not implemented." );
	}
    if ( selectedDataStore != null ) {
    	Message.printStatus ( 1, routine,
    	    "Time series list from input type \"" + selectedDataStore.getName() +
    	    "\" are listed in Time Series List area." );
    }
    else {
        Message.printStatus ( 1, routine,
            "Time series list from input type \"" + selectedInputType + "\" are listed in Time Series List area." ); 
    }
}

/**
Read ColoradoWaterHBGuest time series via web service and list in the GUI.
*/
private void uiAction_GetTimeSeriesListClicked_ReadColoradoWaterHBGuestHeaders()
{   String routine = "TSTool_JFrame.uiAction_GetTimeSeriesListClicked_ReadColoradoWaterHBGuestHeaders";
    JGUIUtil.setWaitCursor ( this, true );
    Message.printStatus ( 1, routine, "Please wait... retrieving data");

    List tslist; // List contents vary depending on data type
    int size = 0;
    try {
        DataStore dataStore = ui_GetSelectedDataStore ();
        ColoradoWaterHBGuestDataStore cwds = (ColoradoWaterHBGuestDataStore)dataStore;
        // Get the subject from the where filters.  If not set, warn and don't query
        List<String> inputDivision =
            ((InputFilter_JPanel)__selectedInputFilter_JPanel).getInput("Division", null, false, null );
        List<String> inputDistrict =
            ((InputFilter_JPanel)__selectedInputFilter_JPanel).getInput("District", null, false, null );
        if ( (inputDistrict.size() + inputDivision.size()) == 0 ) {
            Message.printWarning ( 1, routine,
                "You must specify a district or division as a Where in the input filter." );
            JGUIUtil.setWaitCursor ( this, false );
            return;
        }
        queryResultsList_Clear ();

        String selectedDataType = ui_GetSelectedDataType();
        String selectedTimeStep = ui_GetSelectedTimeStep();

        Message.printStatus ( 2, "", "Datatype = \"" + selectedDataType + "\" timestep = \"" + selectedTimeStep + "\"" );

        ColoradoWaterHBGuestService service = cwds.getColoradoWaterHBGuestService();
        tslist = service.getTimeSeriesHeaderObjects ( selectedDataType, selectedTimeStep,
            (InputFilter_JPanel)__selectedInputFilter_JPanel );
        // Make sure that size is set...
        if ( tslist != null ) {
            size = tslist.size();
        }
        // Now display the data in the worksheet...
        if ( (tslist != null) && (size > 0) ) {
            Message.printStatus ( 1, routine, "" + size + " ColoradoWaterHBGuest time series read for data type \"" +
                selectedDataType + "\" and timestep \"" + selectedTimeStep + "\".  Displaying data..." );
            /* Not implemented
            if ( HydroBase_Util.isAgriculturalCASSCropStatsTimeSeriesDataType ( __hbdmi, __selected_data_type) ||
                HydroBase_Util.isAgriculturalNASSCropStatsTimeSeriesDataType ( __hbdmi, __selected_data_type ) ) {
                // Data from agricultural_CASS_crop_statistics or agricultural_NASS_crop_statistics...
                __query_TableModel = new
                    TSTool_HydroBase_Ag_TableModel ( __query_JWorksheet, tslist, __selected_data_type );
                TSTool_HydroBase_Ag_CellRenderer cr = new
                    TSTool_HydroBase_Ag_CellRenderer((TSTool_HydroBase_Ag_TableModel)__query_TableModel);
                __query_JWorksheet.setCellRenderer ( cr );
                __query_JWorksheet.setModel(__query_TableModel);
                __query_JWorksheet.setColumnWidths ( cr.getColumnWidths(), getGraphics() );
            }
            */
            /* Not implemented
            else if(HydroBase_Util.isAgriculturalCASSLivestockStatsTimeSeriesDataType ( __hbdmi, __selected_data_type) ) {
                // Data from CASS livestock stats...
                __query_TableModel = new
                    TSTool_HydroBase_CASSLivestockStats_TableModel ( __query_JWorksheet, tslist, __selected_data_type );
                TSTool_HydroBase_CASSLivestockStats_CellRenderer cr = new
                    TSTool_HydroBase_CASSLivestockStats_CellRenderer(
                    (TSTool_HydroBase_CASSLivestockStats_TableModel)__query_TableModel);
                __query_JWorksheet.setCellRenderer ( cr );
                __query_JWorksheet.setModel(__query_TableModel);
                __query_JWorksheet.setColumnWidths ( cr.getColumnWidths(), getGraphics() );
            }
            */
            /* Not implemented
            else if(HydroBase_Util.isCUPopulationTimeSeriesDataType( __hbdmi, __selected_data_type) ) {
                // Data from CUPopulation...
                __query_TableModel = new
                    TSTool_HydroBase_CUPopulation_TableModel ( __query_JWorksheet, tslist, __selected_data_type );
                TSTool_HydroBase_CUPopulation_CellRenderer cr = new
                    TSTool_HydroBase_CUPopulation_CellRenderer(
                    (TSTool_HydroBase_CUPopulation_TableModel)__query_TableModel);
                __query_JWorksheet.setCellRenderer ( cr );
                __query_JWorksheet.setModel(__query_TableModel);
                __query_JWorksheet.setColumnWidths ( cr.getColumnWidths(), getGraphics() );
            }
            */
            /* Not implemented
            else if(HydroBase_Util.isIrrigSummaryTimeSeriesDataType( __hbdmi, __selected_data_type ) ) {
                // Irrig summary TS...
                __query_TableModel = new
                    TSTool_HydroBase_AgGIS_TableModel ( __query_JWorksheet, tslist, __selected_data_type,
                    StringUtil.atoi(__props.getValue( "HydroBase.WDIDLength")) );
                TSTool_HydroBase_AgGIS_CellRenderer cr = new
                    TSTool_HydroBase_AgGIS_CellRenderer( (TSTool_HydroBase_AgGIS_TableModel)__query_TableModel);
                __query_JWorksheet.setCellRenderer ( cr );
                __query_JWorksheet.setModel(__query_TableModel);
                __query_JWorksheet.setColumnWidths ( cr.getColumnWidths(), getGraphics() );
            }
            */
            /* Not implemented
            else if( HydroBase_Util.isWISTimeSeriesDataType ( __hbdmi, __selected_data_type ) ) {
                // WIS TS...
                __query_TableModel = new
                    TSTool_HydroBase_WIS_TableModel ( __query_JWorksheet, tslist, __selected_data_type );
                TSTool_HydroBase_WIS_CellRenderer cr = new
                    TSTool_HydroBase_WIS_CellRenderer( (TSTool_HydroBase_WIS_TableModel)__query_TableModel);
                __query_JWorksheet.setCellRenderer ( cr );
                __query_JWorksheet.setModel(__query_TableModel);
                __query_JWorksheet.setColumnWidths ( cr.getColumnWidths(), getGraphics() );
            }
            else */
            if ( selectedDataType.equalsIgnoreCase( "WellLevelElev") ||
                selectedDataType.equalsIgnoreCase( "WellLevelDepth")) {
                if (selectedTimeStep.equalsIgnoreCase("Day")) {
                    __query_TableModel = new TSTool_HydroBase_WellLevel_Day_TableModel (
                        __query_JWorksheet, StringUtil.atoi( __props.getValue("HydroBase.WDIDLength")), tslist,
                        dataStore.getName());
                    TSTool_HydroBase_WellLevel_Day_CellRenderer cr =
                        new TSTool_HydroBase_WellLevel_Day_CellRenderer(
                        (TSTool_HydroBase_WellLevel_Day_TableModel)__query_TableModel);
                    __query_JWorksheet.setCellRenderer ( cr );
                    __query_JWorksheet.setModel(__query_TableModel);
                    __query_JWorksheet.setColumnWidths ( cr.getColumnWidths(), getGraphics() );
    
                }
                else {
                    // Real-time stations table model
                    /*
                    __query_TableModel = new TSTool_HydroBase_TableModel ( __query_JWorksheet, StringUtil.atoi(
                        __props.getValue( "HydroBase.WDIDLength")), tslist );
                    TSTool_HydroBase_CellRenderer cr =
                        new TSTool_HydroBase_CellRenderer( (TSTool_HydroBase_TableModel)__query_TableModel);
                    __query_JWorksheet.setCellRenderer ( cr );
                    __query_JWorksheet.setModel(__query_TableModel);
                    // Turn off columns in the table model that do not apply...
                    __query_JWorksheet.removeColumn ( ((TSTool_HydroBase_TableModel)__query_TableModel).COL_ABBREV );
                    __query_JWorksheet.setColumnWidths ( cr.getColumnWidths(), getGraphics() );
                    */
                }
            }
            else {
                // Stations and structures.  Figure out from returned data which table model needs to be used.
                // At least one object will have been returned based on checks above
                Object o = (Object)tslist.get(0);
                JWorksheet_DefaultTableCellRenderer cr = null;
                if ( o instanceof HydroBase_StationGeolocMeasType ) {
                    __query_TableModel = new TSTool_HydroBase_StationGeolocMeasType_TableModel(
                        __query_JWorksheet, tslist );
                    cr = new TSTool_HydroBase_StationGeolocMeasType_CellRenderer(
                        (TSTool_HydroBase_StationGeolocMeasType_TableModel)__query_TableModel);
                }
                else if ( o instanceof HydroBase_StructureGeolocStructMeasType ) {
                    __query_TableModel = new TSTool_HydroBase_StructureGeolocStructMeasType_TableModel (
                        __query_JWorksheet, StringUtil.atoi(__props.getValue( "HydroBase.WDIDLength")), tslist );
                    cr = new TSTool_HydroBase_StructureGeolocStructMeasType_CellRenderer(
                        (TSTool_HydroBase_StructureGeolocStructMeasType_TableModel)__query_TableModel);
                }
                else if (o instanceof HydroBase_GroundWaterWellsView) {
                    __query_TableModel = new TSTool_HydroBase_GroundWaterWellsView_TableModel (
                        __query_JWorksheet, StringUtil.atoi(__props.getValue( "HydroBase.WDIDLength")), tslist );
                    cr = new TSTool_HydroBase_GroundWaterWellsView_CellRenderer(
                        (TSTool_HydroBase_GroundWaterWellsView_TableModel)__query_TableModel);
                }
                __query_JWorksheet.setCellRenderer ( cr );
                __query_JWorksheet.setModel(__query_TableModel);
                __query_JWorksheet.setColumnWidths ( cr.getColumnWidths(), getGraphics() );
            }
        }
        else {
            Message.printStatus ( 1, routine, "No ColoradoWaterHBGuest time series read." );
            queryResultsList_Clear ();
        }
        JGUIUtil.setWaitCursor ( this, false );
    }
    catch ( Exception e ) {
        // Messages elsewhere but catch so we can get the cursor back...
        Message.printWarning ( 3, routine, e );
        JGUIUtil.setWaitCursor ( this, false );
    }
}

/**
Read ColoradoWaterSWS time series via web service and list in the GUI.
*/
private void uiAction_GetTimeSeriesListClicked_ReadColoradoWaterSMSHeaders()
{   String routine = "TSTool_JFrame.uiAction_GetTimeSeriesListClicked_ReadColoradoWaterSMSHeaders";
    JGUIUtil.setWaitCursor ( this, true );
    Message.printStatus ( 1, routine, "Please wait... retrieving data");

    // The headers are a list of HydroBase_
    try {
        DataStore dataStore = ui_GetSelectedDataStore ();
        ColoradoWaterSMSDataStore cwds = (ColoradoWaterSMSDataStore)dataStore;
        String selectedInputType = ui_GetSelectedInputType();
        queryResultsList_Clear ();

        String location = "";
        String dataType = __dataType_JComboBox.getSelected().trim();
        String timestep = __timeStep_JComboBox.getSelected().trim();

        Message.printStatus ( 2, "", "Datatype = \"" + dataType + "\" timestep = \"" + timestep + "\"" );

        List<HydroBase_StationGeolocMeasType> tslist = null;
        try {
            ColoradoWaterSMS service = cwds.getColoradoWaterSMS();
            // FIXME SAM 2009-11-20 Need to enable input filters for wd, div, abbrev
            int wd = 0; // <= 0 means get all
            int div = 0; // <= 0 meand get all
            String abbrev = "";//null; // Get all
            String stationName = null; // Get all
            String dataProvider = null; // Get all
            tslist = ColoradoWaterSMSAPI.readTimeSeriesHeaderObjects (
                service, wd, div, abbrev, stationName, dataProvider, dataType, timestep,
                null, null, false ); // Don't specify dates and don't request data
        }
        catch ( Exception e ) {
            Message.printWarning(3, routine, "Error reading time series list." );
            Message.printWarning(3,routine,e);
            tslist = null;
        }

        int size = 0;
        if ( (tslist != null) && (tslist.size() > 0) ) {
            size = tslist.size();
            // Does not work??
            //__query_TableModel.setNewData ( results );
            // Try brute force...
            /* If objects are time series
            __query_TableModel = new TSTool_TS_TableModel ( results );
            TSTool_TS_CellRenderer cr = new TSTool_TS_CellRenderer( (TSTool_TS_TableModel)__query_TableModel);

            __query_JWorksheet.setCellRenderer ( cr );
            __query_JWorksheet.setModel ( __query_TableModel );
            __query_JWorksheet.setColumnWidths ( cr.getColumnWidths(), getGraphics() );
            */
            // TODO SAM 2012-09-05 Should only be stations so no need to handle structures?
            // Stations and structures...
            __query_TableModel = new TSTool_HydroBase_StationGeolocMeasType_TableModel (
                __query_JWorksheet, tslist, dataStore.getName() );
            TSTool_HydroBase_StationGeolocMeasType_CellRenderer cr =
                new TSTool_HydroBase_StationGeolocMeasType_CellRenderer(
                    (TSTool_HydroBase_StationGeolocMeasType_TableModel)__query_TableModel);
            __query_JWorksheet.setCellRenderer ( cr );
            __query_JWorksheet.setModel(__query_TableModel);
            // Turn off columns in the table model that do not apply...
            // TODO SAM 2012-09-05 Should ID be allowed to cross-reference USGS and other external IDs? 
            __query_JWorksheet.removeColumn ( ((TSTool_HydroBase_StationGeolocMeasType_TableModel)__query_TableModel).COL_ID );
            __query_JWorksheet.setColumnWidths ( cr.getColumnWidths(), getGraphics() );
        }
        if ( (tslist == null) || (size == 0) ) {
            Message.printStatus ( 1, routine,"Query complete.  No records returned." );
        }
        else {
            Message.printStatus ( 1, routine, "Query complete. " + size + " records returned." );
        }
        ui_UpdateStatus ( false );

        JGUIUtil.setWaitCursor ( this, false );
    }
    catch ( Exception e ) {
        // Messages elsewhere but catch so we can get the cursor back...
        Message.printWarning ( 3, routine, e );
        JGUIUtil.setWaitCursor ( this, false );
    }
}

/**
Read the list of time series from a DateValue file and list in the GUI.
*/
private void uiAction_GetTimeSeriesListClicked_ReadDateValueHeaders ()
throws IOException
{	String message, routine = "TSTool_JFrame.readDateValueHeaders";

	try {
	    JFileChooser fc = JFileChooserFactory.createJFileChooser ( JGUIUtil.getLastFileDialogDirectory() );
		fc.setDialogTitle ( "Select DateValue Time Series File" );
		SimpleFileFilter dv_sff = new SimpleFileFilter( "dv", "DateValue Time Series");
		fc.addChoosableFileFilter(dv_sff);
		SimpleFileFilter sff = new SimpleFileFilter( "txt", "DateValue Time Series");
		fc.addChoosableFileFilter(sff);
		fc.setFileFilter(dv_sff);
		if ( fc.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
			// Canceled...
			return;
		}
		String directory = fc.getSelectedFile().getParent();
		String path = fc.getSelectedFile().getPath();
		JGUIUtil.setLastFileDialogDirectory ( directory );

		Message.printStatus ( 1, routine, "Reading DateValue file \"" + path + "\"" );
		JGUIUtil.setWaitCursor ( this, true );
		List tslist = null;
		try {
		    tslist = DateValueTS.readTimeSeriesList ( path, null, null, null, false );
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
		// There should not be any non-null time series so use the Vector size...
		int size = tslist.size();
        if ( size == 0 ) {
			Message.printStatus ( 1, routine, "No DateValue TS read." );
			queryResultsList_Clear ();
        }
        else {
            Message.printStatus ( 1, routine, "" + size + " DateValue TS read." );
			// Include the alias in the display...
			__query_TableModel = new TSTool_TS_TableModel (	tslist, true );
			TSTool_TS_CellRenderer cr = new TSTool_TS_CellRenderer(	(TSTool_TS_TableModel)__query_TableModel);
	
			__query_JWorksheet.setCellRenderer ( cr );
			__query_JWorksheet.setModel ( __query_TableModel );
			__query_JWorksheet.setColumnWidths (cr.getColumnWidths(), getGraphics() );

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

	try {
	    queryResultsList_Clear ();

		//String location = "";
		//String data_type = StringUtil.getToken(__data_type_JComboBox.getSelected()," ",0,0).trim();
		String timestep = __timeStep_JComboBox.getSelected().trim();

/* TODO SAM - need to figure out how to do where - or do we just have choices?
        	// add where clause(s)   
		if ( getWhereClauses() == false ) {
        		JGUIUtil.setWaitCursor ( this, false );
        		Message.printStatus ( 1, rtn, "Query aborted - null where string");
			return;
		}
*/

		List results = null;
		
		try {
		    results = __DIADvisor_dmi.readSensorDefList();
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
			Message.printStatus ( 1, rtn, "Query complete.  No records returned." );
        }
        else {
            Message.printStatus ( 1, rtn, "Query complete. " + size + " records returned." );
        }

        List tslist_data_Vector = new Vector(size*2);
		DIADvisor_SensorDef data;
		String start_date_time = "";
		String end_date_time = "";
		String interval = __timeStep_JComboBox.getSelected();
		String description = "";
		// This is either "DataValue" or "DataValue2"...
		String selected_group = StringUtil.getToken( __dataType_JComboBox.getSelected(), "-", 0, 0);
		String datatype = StringUtil.getToken ( __dataType_JComboBox.getSelected(), "-", 0, 1);
		String sensor_type = "";	// Used to check for Rain
		String units = "";
		String rating_type = "";
		boolean is_datavalue1 = true;
		if ( datatype.endsWith("DataValue2") ) {
			is_datavalue1 = false;
		}
		for ( int i = 0; i < size; i++ ) {
			data = (DIADvisor_SensorDef)results.get(i);  
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
			if ( is_datavalue1 || (!is_datavalue1 && sensor_type.equalsIgnoreCase("RAIN") ||
				!rating_type.equalsIgnoreCase("NONE")) ) {
				if ( is_datavalue1 ) {
					units = data.getDisplayUnits();
				}
				else {
				    units = data.getDisplayUnits2();
				}
				tslist_data_Vector.add(
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
			if ( (__DIADvisor_archive_dmi != null) && (is_datavalue1 ||
				(!is_datavalue1 && sensor_type.equalsIgnoreCase("RAIN") ||
				!rating_type.equalsIgnoreCase("NONE") && interval.equals(__TIMESTEP_IRREGULAR))) ) {
				// Also add an archive time series...
				tslist_data_Vector.add(	
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
		// Messages elsewhere but catch so we can get the cursor back...
		Message.printWarning ( 2, rtn, e );
        JGUIUtil.setWaitCursor ( this, false );
	}
}

/**
Read the list of time series from a HEC-DSS database file and list in the GUI.
The filename is taken from the selected item in the __inputName_JComboBox.
*/
private void uiAction_GetTimeSeriesListClicked_ReadHECDSSHeaders ()
throws IOException
{   String routine = "TSTool_JFrame.readHECDSSHeaders";

    try {
        // Visible path might be abbreviated so look up in list that has full paths
        String inputNameSelectedVisible = __inputName_JComboBox.getSelected();
        int listIndex = StringUtil.indexOf(__inputNameHecDssVisibleList,inputNameSelectedVisible);
        Message.printStatus( 2, routine, "Visible item is in position " + listIndex );
        String inputNameSelected = (String)__inputNameHecDssList.get(listIndex);
        Message.printStatus( 2, routine, "File corresponding to visible item is \"" + inputNameSelected + "\"" );
        String id_input = "*";
        // Get the ID from the input filter...
        String aPartReq = "*";
        String bPartReq = "*";
        String cPartReq = "*";
        // D part is not selectable in main gui but can use SetInputPeriod() command to limit read for time series
        String ePartReq = "*";
        String fPartReq = "*";
        // Try to get filter choices for all the parts
        List inputList = __inputFilterHecDss_JPanel.getInput ( "A part", null, true, null );
        if ( inputList.size() > 0 ) {
            // Use the first matching filter...
            aPartReq = (String)inputList.get(0);
        }
        inputList = __inputFilterHecDss_JPanel.getInput ( "B part", null, true, null );
        if ( inputList.size() > 0 ) {
            // Use the first matching filter...
            bPartReq = (String)inputList.get(0);
        }
        inputList = __inputFilterHecDss_JPanel.getInput ( "C part", null, true, null );
        if ( inputList.size() > 0 ) {
            // Use the first matching filter...
            cPartReq = (String)inputList.get(0);
        }
        inputList = __inputFilterHecDss_JPanel.getInput ( "E part", null, true, null );
        if ( inputList.size() > 0 ) {
            // Use the first matching filter...
            ePartReq = (String)inputList.get(0);
        }
        inputList = __inputFilterHecDss_JPanel.getInput ( "F part", null, true, null );
        if ( inputList.size() > 0 ) {
            // Use the first matching filter...
            fPartReq = (String)inputList.get(0);
        }
        // Use the : to separate the parts.
        String tsidPattern = aPartReq + ":" + bPartReq + "." + "*" + cPartReq + "." + ePartReq + "." + fPartReq +
            "~HEC-DSS~" + inputNameSelected;
        Message.printStatus ( 1, routine, "Reading HEC-DSS file \"" + inputNameSelected + "\" for TSID pattern \"" + tsidPattern + "\"" );
        List tslist = null;
        JGUIUtil.setWaitCursor ( this, true );
        // TODO SAM 2008-09-03 Enable searchable fields
        tslist = HecDssAPI.readTimeSeriesList ( new File(inputNameSelected), tsidPattern, null, null, null, false );
        int size = 0;
        if ( tslist != null ) {
            size = tslist.size();
            __query_TableModel = new TSTool_HecDss_TableModel ( tslist );
            TSTool_HecDss_CellRenderer cr = new TSTool_HecDss_CellRenderer( (TSTool_HecDss_TableModel)__query_TableModel);

            __query_JWorksheet.setCellRenderer ( cr );
            __query_JWorksheet.setModel ( __query_TableModel );
            // Turn off columns in the table model that do not apply...
            __query_JWorksheet.removeColumn (((TSTool_HecDss_TableModel)__query_TableModel).COL_SEQUENCE);
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
@param grlimits If a query is being executed from the map interface, then the limits will be non-null.
@throws Exception if there is an error.
*/
private void uiAction_GetTimeSeriesListClicked_ReadHydroBaseHeaders ( GRLimits grlimits )
throws Exception
{	String message, routine = "TSTool_JFrame.readHydroBaseHeaders";

    JGUIUtil.setWaitCursor ( this, true );
    Message.printStatus ( 1, routine, "Please wait... retrieving data from HydroBase datastore...");
    String selectedDataType = ui_GetSelectedDataType();
    String selectedTimeStep = ui_GetSelectedTimeStep();

    // Object type in list varies
    List tslist = null;
	int size = 0;
	DataStore dataStore = ui_GetSelectedDataStore (); // Will be null if using HydroBase input type
	HydroBaseDMI hbdmi = null;
	if ( dataStore == null ) {
	    hbdmi = ui_GetHydroBaseDMI(); // from input type - legacy
	}
	else {
        HydroBaseDataStore hydroBaseDataStore = (HydroBaseDataStore)dataStore;
        hbdmi = (HydroBaseDMI)hydroBaseDataStore.getDMI();
	}
	try {	
		tslist = HydroBase_Util.readTimeSeriesHeaderObjects ( hbdmi, selectedDataType, selectedTimeStep,
			(InputFilter_JPanel)__selectedInputFilter_JPanel, grlimits );
		// Make sure that size is set...
		if ( tslist != null ) {
			size = tslist.size();
		}
		// Now display the data in the worksheet...
		if ( (tslist != null) && (size > 0) ) {
       		Message.printStatus ( 1, routine, "" + size + " HydroBase time series read.  Displaying data..." );
			if ( HydroBase_Util.isAgriculturalCASSCropStatsTimeSeriesDataType ( hbdmi, selectedDataType) ||
				HydroBase_Util.isAgriculturalNASSCropStatsTimeSeriesDataType ( hbdmi, selectedDataType ) ) {
				// Data from agricultural_CASS_crop_statistics or agricultural_NASS_crop_statistics...
				__query_TableModel = new
					TSTool_HydroBase_Ag_TableModel ( __query_JWorksheet, tslist, selectedDataType );
				TSTool_HydroBase_Ag_CellRenderer cr = new
					TSTool_HydroBase_Ag_CellRenderer((TSTool_HydroBase_Ag_TableModel)__query_TableModel);
				__query_JWorksheet.setCellRenderer ( cr );
				__query_JWorksheet.setModel(__query_TableModel);
				__query_JWorksheet.setColumnWidths ( cr.getColumnWidths(), getGraphics() );
			}
			else if(HydroBase_Util.isAgriculturalCASSLivestockStatsTimeSeriesDataType ( hbdmi, selectedDataType) ) {
				// Data from CASS livestock stats...
				__query_TableModel = new
					TSTool_HydroBase_CASSLivestockStats_TableModel ( __query_JWorksheet, tslist, selectedDataType );
				TSTool_HydroBase_CASSLivestockStats_CellRenderer cr = new
					TSTool_HydroBase_CASSLivestockStats_CellRenderer(
					(TSTool_HydroBase_CASSLivestockStats_TableModel)__query_TableModel);
				__query_JWorksheet.setCellRenderer ( cr );
				__query_JWorksheet.setModel(__query_TableModel);
				__query_JWorksheet.setColumnWidths ( cr.getColumnWidths(), getGraphics() );
			}
			else if(HydroBase_Util.isCUPopulationTimeSeriesDataType( hbdmi, selectedDataType) ) {
				// Data from CUPopulation...
				__query_TableModel = new
					TSTool_HydroBase_CUPopulation_TableModel ( __query_JWorksheet, tslist, selectedDataType );
				TSTool_HydroBase_CUPopulation_CellRenderer cr =	new
					TSTool_HydroBase_CUPopulation_CellRenderer(
					(TSTool_HydroBase_CUPopulation_TableModel)__query_TableModel);
				__query_JWorksheet.setCellRenderer ( cr );
				__query_JWorksheet.setModel(__query_TableModel);
				__query_JWorksheet.setColumnWidths ( cr.getColumnWidths(), getGraphics() );
			}
			else if(HydroBase_Util.isIrrigSummaryTimeSeriesDataType( hbdmi, selectedDataType ) ) {
				// Irrig summary TS...
				__query_TableModel = new
					TSTool_HydroBase_AgGIS_TableModel (	__query_JWorksheet, tslist, selectedDataType,
					StringUtil.atoi(__props.getValue( "HydroBase.WDIDLength")) );
				TSTool_HydroBase_AgGIS_CellRenderer cr = new
					TSTool_HydroBase_AgGIS_CellRenderer( (TSTool_HydroBase_AgGIS_TableModel)__query_TableModel);
				__query_JWorksheet.setCellRenderer ( cr );
				__query_JWorksheet.setModel(__query_TableModel);
				__query_JWorksheet.setColumnWidths ( cr.getColumnWidths(), getGraphics() );
			}
			else if( HydroBase_Util.isWISTimeSeriesDataType ( hbdmi, selectedDataType ) ) {
				// WIS TS...
				__query_TableModel = new
					TSTool_HydroBase_WIS_TableModel ( __query_JWorksheet, tslist, selectedDataType );
				TSTool_HydroBase_WIS_CellRenderer cr = new
					TSTool_HydroBase_WIS_CellRenderer( (TSTool_HydroBase_WIS_TableModel)__query_TableModel);
				__query_JWorksheet.setCellRenderer ( cr );
				__query_JWorksheet.setModel(__query_TableModel);
				__query_JWorksheet.setColumnWidths ( cr.getColumnWidths(), getGraphics() );
			}
			else if (selectedDataType.equalsIgnoreCase( "WellLevel") || selectedDataType.equalsIgnoreCase( "WellLevelElev")||
			    selectedDataType.equalsIgnoreCase( "WellLevelDepth") ) {
    			if (selectedTimeStep.equalsIgnoreCase("Day")) {
    			    // Well level data...
    				__query_TableModel = new TSTool_HydroBase_WellLevel_Day_TableModel (
    				__query_JWorksheet, StringUtil.atoi( __props.getValue("HydroBase.WDIDLength")), tslist,
    				"HydroBase" );
    				TSTool_HydroBase_WellLevel_Day_CellRenderer cr =
    				new TSTool_HydroBase_WellLevel_Day_CellRenderer(
    				(TSTool_HydroBase_WellLevel_Day_TableModel)__query_TableModel);
    				__query_JWorksheet.setCellRenderer ( cr );
    				__query_JWorksheet.setModel(__query_TableModel);
    				__query_JWorksheet.setColumnWidths ( cr.getColumnWidths(), getGraphics() );
    			}
    			else {
    				// Real-time well elevation as station
    				__query_TableModel = new TSTool_HydroBase_StationGeolocMeasType_TableModel (
    				    __query_JWorksheet, tslist );
    				TSTool_HydroBase_StationGeolocMeasType_CellRenderer cr =
    					new TSTool_HydroBase_StationGeolocMeasType_CellRenderer(
    					    (TSTool_HydroBase_StationGeolocMeasType_TableModel)__query_TableModel);
    				__query_JWorksheet.setCellRenderer ( cr );
    				__query_JWorksheet.setModel(__query_TableModel);
    				// TODO SAM 2012-09-05 Verify that Abbrev should be on since station
    				// Turn off columns in the table model that do not apply...
    				//__query_JWorksheet.removeColumn ( ((TSTool_HydroBase_StationGeolocMeasType_TableModel)__query_TableModel).COL_ABBREV );
    				__query_JWorksheet.setColumnWidths ( cr.getColumnWidths(), getGraphics() );
    			}
			}
			else {
			    // Stations and structures.  Figure out from returned data which table model needs to be used.
			    // At least one object will have been returned based on checks above
			    Object o = (Object)tslist.get(0);
			    JWorksheet_DefaultTableCellRenderer cr = null;
	            if ( o instanceof HydroBase_StationGeolocMeasType ) {
	                __query_TableModel = new TSTool_HydroBase_StationGeolocMeasType_TableModel(
	                    __query_JWorksheet, tslist );
                    cr = new TSTool_HydroBase_StationGeolocMeasType_CellRenderer(
                        (TSTool_HydroBase_StationGeolocMeasType_TableModel)__query_TableModel);
	            }
	            else if ( o instanceof HydroBase_StructureGeolocStructMeasType ) {
	                __query_TableModel = new TSTool_HydroBase_StructureGeolocStructMeasType_TableModel (
	                    __query_JWorksheet, StringUtil.atoi(__props.getValue( "HydroBase.WDIDLength")), tslist );
                    cr = new TSTool_HydroBase_StructureGeolocStructMeasType_CellRenderer(
                        (TSTool_HydroBase_StructureGeolocStructMeasType_TableModel)__query_TableModel);
	            }
	            else if (o instanceof HydroBase_GroundWaterWellsView) {
	                __query_TableModel = new TSTool_HydroBase_GroundWaterWellsView_TableModel (
	                    __query_JWorksheet, StringUtil.atoi(__props.getValue( "HydroBase.WDIDLength")), tslist );
                    cr = new TSTool_HydroBase_GroundWaterWellsView_CellRenderer(
                        (TSTool_HydroBase_GroundWaterWellsView_TableModel)__query_TableModel);
	            }
				__query_JWorksheet.setCellRenderer ( cr );
				__query_JWorksheet.setModel(__query_TableModel);
				__query_JWorksheet.setColumnWidths ( cr.getColumnWidths(), getGraphics() );
			}
		}
		else {
		    Message.printStatus ( 1, routine, "No HydroBase time series read." );
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
not change, it is only read once.  If necessary, later change to prompt each time.
*/
private void uiAction_GetTimeSeriesListClicked_ReadMexicoCSMNHeaders ()
throws IOException
{	String message, routine = "TSTool_JFrame.readMexicoCSMNHeaders";

    String selectedDataType = ui_GetSelectedDataType();
	try {
	    List allts = MexicoCsmnTS.getCatalogTSList();
		if ( allts == null ) {
			// Have not read the catalog yet so read it...
			JFileChooser fc = JFileChooserFactory.createJFileChooser ( JGUIUtil.getLastFileDialogDirectory() );
			fc.setDialogTitle ( "Select a Mexico CSMN CATALOGO.TXT File" );
			SimpleFileFilter sff = new SimpleFileFilter( "txt", "Mexico CSMN Catalogo.txt File");
			fc.addChoosableFileFilter(sff);
			fc.setFileFilter(sff);
			if ( fc.showOpenDialog(this) != JFileChooser.APPROVE_OPTION ) {
				// Canceled...
				return;
			}
			String directory = fc.getSelectedFile().getParent();
			String path = fc.getSelectedFile().getPath();
			JGUIUtil.setLastFileDialogDirectory ( directory );

			Message.printStatus ( 1, routine, "Reading Mexico CSMN catalog file \"" + path + "\"" );
			JGUIUtil.setWaitCursor ( this, true );
			try {
			    allts = MexicoCsmnTS.readCatalogFile ( path );
			}
			catch ( Exception e ) {
				message = "Error reading Mexico CSMN catalog file \"" + path + "\"";
				Message.printWarning ( 2, routine, message );
				Message.printWarning ( 2, routine, e );
				JGUIUtil.setWaitCursor ( this, false );
				throw new IOException ( message );
			}
		}
		if ( allts == null ) {
			message = "Error reading Mexico CSMN catalog file - no time series.";
			Message.printWarning ( 2, routine, message );
			JGUIUtil.setWaitCursor ( this, false );
			throw new IOException ( message );
		}
		// Now limit the list of time series according to the where criteria...
		// There should not be any non-null time series so use the Vector size...
		int nallts = allts.size();
		Message.printStatus ( 1, routine, "Read " + nallts + " time series listings from catalog file." );
		TS ts;
		List tslist = null;
		// Limit the output to the matching information - there are 2 filter groups...
		InputFilter filter = __inputFilterMexicoCSMN_JPanel.getInputFilter(0);
		InputFilter station_filter = null;
		String where1 = filter.getWhereLabel();
		String input1 = filter.getInput(false).trim();
		filter = __inputFilterMexicoCSMN_JPanel.getInputFilter(1);
		String where2 = filter.getWhereLabel();
		String input2 = filter.getInput(false).trim();
		String station_operator = null, station = null, state = null;
		if ( where1.equals("Station Name") ) {
			station = input1;
			station_filter = __inputFilterMexicoCSMN_JPanel.getInputFilter(0);
			station_operator = __inputFilterMexicoCSMN_JPanel.getOperator(0);
		}
		else if ( where2.equals("Station Name") ) {
			station = input2;
			station_filter = __inputFilterMexicoCSMN_JPanel.getInputFilter(1);
			station_operator = __inputFilterMexicoCSMN_JPanel.getOperator(1);
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
			ts = (TS)allts.get(i);
			if ( Message.isDebugOn ) {
    			Message.printStatus ( 2, "",
    				"station=" + station +
    				" station_operator=" + station_operator +
    				" state=" + state +
    				" location=" + ts.getLocation() +
    				" prefix=" + prefix  +
    				" matchresult=" +
    				station_filter.matches ( station,
    				station_operator, true ) );
			}
			if ( ((station == null) || station_filter.matches ( ts.getDescription(),
				station_operator, true )) && ((state == null) || ts.getLocation().startsWith(prefix)) ) {
				// OK to add the time series to the output list (reset some information first)...
				ts.setDataType ( selectedDataType );
				state_num = StringUtil.atoi( ts.getLocation().substring(0,5) );
				f = new File(ts.getIdentifier().getInputName());
				ts.getIdentifier().setInputName ( f.getParent() + File.separator +
					MexicoCsmnTS.getStateAbbreviation( state_num) + "_" + selectedDataType + ".CSV" );
				tslist.add ( ts );
			}
		}
		// There should not be any non-null time series so use the Vector size...
		int size = tslist.size();
        if ( size == 0 ) {
			Message.printStatus ( 1, routine, "No Mexico CSMN TS read." );
			queryResultsList_Clear ();
        }
        else {
            Message.printStatus ( 1, routine, "" + size + " Mexico CSMN TS read." );
			__query_TableModel = new TSTool_TS_TableModel (tslist );
			TSTool_TS_CellRenderer cr = new TSTool_TS_CellRenderer(	(TSTool_TS_TableModel)__query_TableModel);
	
			__query_JWorksheet.setCellRenderer ( cr );
			__query_JWorksheet.setModel ( __query_TableModel );
			// Do not include the alias in the display...
			__query_JWorksheet.removeColumn ( ((TSTool_TS_TableModel)__query_TableModel).COL_ALIAS );
			__query_JWorksheet.setColumnWidths ( cr.getColumnWidths(), getGraphics() );

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

	try {
	    JFileChooser fc = JFileChooserFactory.createJFileChooser ( JGUIUtil.getLastFileDialogDirectory() );
		fc.setDialogTitle ( "Select MODSIM Output File" );
		SimpleFileFilter sff = new SimpleFileFilter( "DEM", "Demand Time Series");
		fc.addChoosableFileFilter(sff);
		sff = new SimpleFileFilter( "FLO", "Flow Time Series");
		fc.addChoosableFileFilter(sff);
		sff = new SimpleFileFilter( "GW", "Groundwater Series");
		fc.addChoosableFileFilter(sff);
		sff = new SimpleFileFilter( "RES", "Reservoir Time Series");
		fc.addChoosableFileFilter(sff);
		if ( fc.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
			// Canceled...
			return;
		}
		String directory = fc.getSelectedFile().getParent();
		String path = fc.getSelectedFile().getPath();
		JGUIUtil.setLastFileDialogDirectory ( directory );

		Message.printStatus ( 1, routine, "Reading MODSIM file \"" + path + "\"" );
		JGUIUtil.setWaitCursor ( this, true );
		List tslist = null;
		try {
		    tslist = ModsimTS.readTimeSeriesList ( path, null, null, null, false );
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
		// There should not be any non-null time series so use the Vector size...
		int size = tslist.size();
        if ( size == 0 ) {
			Message.printStatus ( 1, routine, "No MODSIM TS read." );
			queryResultsList_Clear ();
        }
        else {
            Message.printStatus ( 1, routine, "" + size + " MODSIM TS read." );
			__query_TableModel = new TSTool_TS_TableModel ( tslist, true );
			TSTool_TS_CellRenderer cr = new TSTool_TS_CellRenderer( (TSTool_TS_TableModel)__query_TableModel);
	
			__query_JWorksheet.setCellRenderer ( cr );
			__query_JWorksheet.setModel ( __query_TableModel );
			// Do not include the alias in the display...
			__query_JWorksheet.removeColumn ( ((TSTool_TS_TableModel)__query_TableModel).COL_ALIAS );
			__query_JWorksheet.setColumnWidths ( cr.getColumnWidths(), getGraphics() );

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
	try {
	    JFileChooser fc = JFileChooserFactory.createJFileChooser ( JGUIUtil.getLastFileDialogDirectory() );
		fc.setDialogTitle ( "Select NWS Card Time Series File" );

		/*
		SimpleFileFilter card_sff = new SimpleFileFilter( "card", "NWS Card Time Series");
		fc.addChoosableFileFilter(card_sff);
		SimpleFileFilter sff = new SimpleFileFilter( "txt", "NWS Card Time Series");
		fc.addChoosableFileFilter(sff);
		fc.setFileFilter(card_sff);
		*/

		FileFilter[] filters = NWSCardTS.getFileFilters();
		for (int i = 0; i < filters.length - 1; i++) {
			fc.addChoosableFileFilter(filters[i]);
		}
		fc.setFileFilter(filters[filters.length - 1]);
		
		if ( fc.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
			// Canceled...
			return;
		}
		String directory = fc.getSelectedFile().getParent();
		String path = fc.getSelectedFile().getPath();
		JGUIUtil.setLastFileDialogDirectory ( directory );

		Message.printStatus ( 1, routine, "Reading NWS CARD file \"" + path + "\"" );
		JGUIUtil.setWaitCursor ( this, true );
		List tslist = null;
		try {
		    tslist = NWSCardTS.readTimeSeriesList (
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
		TSTool_TS_CellRenderer cr = new TSTool_TS_CellRenderer( (TSTool_TS_TableModel)__query_TableModel);

		__query_JWorksheet.setCellRenderer ( cr );
		__query_JWorksheet.setModel ( __query_TableModel );
		__query_JWorksheet.removeColumn ( ((TSTool_TS_TableModel)__query_TableModel).COL_ALIAS );
		__query_JWorksheet.setColumnWidths ( cr.getColumnWidths(), getGraphics() );

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
Read the list of time series from an NWSRFS ESPTraceEnsemble file and list in the GUI.
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
			// Canceled...
			return;
		}
		String directory = fc.getSelectedFile().getParent();
		JGUIUtil.setLastFileDialogDirectory ( directory );
		String path = fc.getSelectedFile().getPath();
		Message.printStatus ( 1, routine, "Reading NWSRFS ESPTraceEnsemble file \"" + path + "\"" );
		JGUIUtil.setWaitCursor ( this, true );
		List tslist = null;
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
        List tslist = null;
		// Default is to return all IDs...
		String id_input = "*";
		// Get the ID from the input filter...
		List<String> input_Vector = __inputFilterNWSRFSFS5Files_JPanel.getInput ( "ID", null, true, null );
		int isize = input_Vector.size();
		if ( isize > 1 ) {
			Message.printWarning ( 1, routine, "Only one input filter for ID can be specified." );
			return;
		}
		if ( isize > 0 ) {
			// Use the first matching filter...
			id_input = (String)input_Vector.get(0);
		}
		String datatype = StringUtil.getToken ( __dataType_JComboBox.getSelected().trim(), " ",0, 0);
		String selectedTimeStep = ui_GetSelectedTimeStep();
		// Parse the interval into the integer hour...
		String selected_input_name=__inputName_JComboBox.getSelected();
		try {
		    String tsident_string = id_input + ".NWSRFS." + datatype + "." +
				selectedTimeStep + "~NWSRFS_FS5Files~" + selected_input_name;
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
Read RCC ACIS time series and list in the GUI.
*/
private void uiAction_GetTimeSeriesListClicked_ReadRccAcisHeaders()
{   String rtn = "TSTool_JFrame.uiAction_GetTimeSeriesListClicked_ReadRccAcisHeaders";
    JGUIUtil.setWaitCursor ( this, true );
    Message.printStatus ( 1, rtn, "Please wait... retrieving data");

    DataStore dataStore = ui_GetSelectedDataStore ();
    // The headers are a list of RccAcisTimeSeriesMetadata
    try {
        RccAcisDataStore rccAcisDataStore = (RccAcisDataStore)dataStore;
        queryResultsList_Clear ();

        String dataType = ui_GetSelectedDataType();
        String timeStep = ui_GetSelectedTimeStep();
        if ( timeStep == null ) {
            Message.printWarning ( 1, rtn, "No time series are available for timestep." );
            JGUIUtil.setWaitCursor ( this, false );
            return;
        }
        else {
            timeStep = timeStep.trim();
        }

        List<RccAcisStationTimeSeriesMetadata> results = null;
        // Data type is shown with name so only use the first part of the choice
        try {
            results = rccAcisDataStore.readStationTimeSeriesMetadataList(dataType, timeStep,
                (InputFilter_JPanel)__selectedInputFilter_JPanel);
        }
        catch ( Exception e ) {
            Message.printWarning(1, rtn, "Error getting time series list from ACIS (" + e + ").");
            Message.printWarning(3, rtn, e );
            results = null;
        }

        int size = 0;
        if ( results != null ) {
            size = results.size();
            // TODO Does not work??
            //__query_TableModel.setNewData ( results );
            // Try brute force...
            __query_TableModel = new TSTool_RccAcis_TableModel ( rccAcisDataStore, results );
            TSTool_RccAcis_CellRenderer cr =
                new TSTool_RccAcis_CellRenderer( (TSTool_RccAcis_TableModel)__query_TableModel);

            __query_JWorksheet.setCellRenderer ( cr );
            __query_JWorksheet.setModel ( __query_TableModel );
            __query_JWorksheet.setColumnWidths ( cr.getColumnWidths(), getGraphics() );
        }
        if ( (results == null) || (size == 0) ) {
            Message.printStatus ( 1, rtn, "Query complete.  No records returned." );
        }
        else {
            Message.printStatus ( 1, rtn, "Query complete. " + size + " records returned." );
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
Read ReclamationHDB time series and list in the GUI.
*/
private void uiAction_GetTimeSeriesListClicked_ReadReclamationHDBHeaders()
{   String rtn = "TSTool_JFrame.uiAction_GetTimeSeriesListClicked_ReadReclamationHDBHeaders";
    JGUIUtil.setWaitCursor ( this, true );
    Message.printStatus ( 1, rtn, "Please wait... retrieving data");

    DataStore dataStore = ui_GetSelectedDataStore ();
    // The headers are a list of ReclamationHDB_SiteTimeSeriesMetadata
    try {
        ReclamationHDBDataStore hdbDataStore = (ReclamationHDBDataStore)dataStore;
        ReclamationHDB_DMI dmi = (ReclamationHDB_DMI)hdbDataStore.getDMI();
        queryResultsList_Clear ();

        String dataType = __dataType_JComboBox.getSelected(); // Object type - common data type
        String timeStep = __timeStep_JComboBox.getSelected();
        if ( timeStep == null ) {
            Message.printWarning ( 1, rtn, "No time series are available for timestep." );
            JGUIUtil.setWaitCursor ( this, false );
            return;
        }
        else {
            timeStep = timeStep.trim();
        }

        List<ReclamationHDB_SiteTimeSeriesMetadata> results = null;
        // Data type is shown with name so only use the first part of the choice
        try {
            results = dmi.readSiteTimeSeriesMetadataList(dataType, timeStep,
                    (InputFilter_JPanel)__selectedInputFilter_JPanel);
        }
        catch ( Exception e ) {
            results = null;
        }

        int size = 0;
        if ( results != null ) {
            size = results.size();
            // TODO Does not work??
            //__query_TableModel.setNewData ( results );
            // Try brute force...
            __query_TableModel = new TSTool_ReclamationHDB_TableModel ( hdbDataStore, timeStep, results );
            TSTool_ReclamationHDB_CellRenderer cr =
                new TSTool_ReclamationHDB_CellRenderer( (TSTool_ReclamationHDB_TableModel)__query_TableModel);

            __query_JWorksheet.setCellRenderer ( cr );
            __query_JWorksheet.setModel ( __query_TableModel );
            __query_JWorksheet.setColumnWidths ( cr.getColumnWidths(), getGraphics() );
        }
        if ( (results == null) || (size == 0) ) {
            Message.printStatus ( 1, rtn, "Query complete.  No records returned." );
        }
        else {
            Message.printStatus ( 1, rtn, "Query complete. " + size + " records returned." );
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
Read RiversideDB time series (MeasType) and list in the GUI.
*/
private void uiAction_GetTimeSeriesListClicked_ReadRiversideDBHeaders()
{	String rtn = "TSTool_JFrame.uiAction_GetTimeSeriesListClicked_ReadRiversideDBHeaders";
    JGUIUtil.setWaitCursor ( this, true );
    Message.printStatus ( 1, rtn, "Please wait... retrieving data");

    DataStore dataStore = ui_GetSelectedDataStore ();
	// The headers are a list of RiversideDB_MeasTypeMeasLocGeoloc
	try {
	    RiversideDBDataStore riversideDBDataStore = (RiversideDBDataStore)dataStore;
	    RiversideDB_DMI rdmi = (RiversideDB_DMI)riversideDBDataStore.getDMI();
        queryResultsList_Clear ();

		String dataType = StringUtil.getToken(	__dataType_JComboBox.getSelected()," ",0,0).trim();
		String timeStep = __timeStep_JComboBox.getSelected();
		if ( timeStep == null ) {
			Message.printWarning ( 1, rtn, "No time series are available for timestep \"" + timeStep + "\"" );
			JGUIUtil.setWaitCursor ( this, false );
			return;
		}
		else {
            timeStep = timeStep.trim();
		}

		List<RiversideDB_MeasTypeMeasLocGeoloc> results = null;
		// Data type is shown with name so only use the first part of the choice
		try {
		    results = rdmi.readMeasTypeMeasLocGeolocList(dataType, timeStep,
		            (InputFilter_JPanel)__selectedInputFilter_JPanel);
		}
		catch ( Exception e ) {
		    Message.printWarning(3,rtn,e);
			results = null;
		}

		int size = 0;
		if ( results != null ) {
			size = results.size();
			// TODO Does not work??
			//__query_TableModel.setNewData ( results );
			// Try brute force...
			__query_TableModel = new TSTool_RiversideDB_TableModel ( riversideDBDataStore, results );
			TSTool_RiversideDB_CellRenderer cr =
				new TSTool_RiversideDB_CellRenderer( (TSTool_RiversideDB_TableModel)__query_TableModel);

			__query_JWorksheet.setCellRenderer ( cr );
			__query_JWorksheet.setModel ( __query_TableModel );
	        // Remove columns that are not appropriate...
			if ( !((RiversideDB_DMI)riversideDBDataStore.getDMI()).getMeasTypeHasSequenceNum() ) {
                __query_JWorksheet.removeColumn (((TSTool_RiversideDB_TableModel)__query_TableModel).COL_SEQUENCE );
			}
    		__query_JWorksheet.setColumnWidths ( cr.getColumnWidths(), getGraphics() );
		}
        if ( (results == null) || (size == 0) ) {
			Message.printStatus ( 1, rtn, "Query complete.  No records returned." );
		}
        else {
            Message.printStatus ( 1, rtn, "Query complete. " + size + " records returned." );
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
			// Canceled...
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
		List tslist = new Vector ( 1 );
		tslist.add ( ts );
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
		Message.printWarning ( 3, routine, e );
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
	    List tslist = null; // Time series to display.
		TS ts = null; // Single time series.
		int size = 0; // Number of time series.
		String path = __inputName_JComboBox.getSelected();
		Message.printStatus ( 1, routine, "Reading StateCU file \"" + path + "\"" );
		if ( (__inputName_FileFilter ==	__input_name_StateCU_iwrrep_FileFilter) ||
			(__inputName_FileFilter ==	__input_name_StateCU_wsl_FileFilter) ||
			(__inputName_FileFilter ==	__input_name_StateCU_frost_FileFilter) ) {
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
		else if ( __inputName_FileFilter == __input_name_StateCU_cds_FileFilter ) {
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
		else if ((__inputName_FileFilter == __input_name_StateCU_ipy_FileFilter) ||
			(__inputName_FileFilter ==	__input_name_StateCU_tsp_FileFilter) ) {
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
		else if ((__inputName_FileFilter == __input_name_StateCU_ddc_FileFilter) ||
			(__inputName_FileFilter == __input_name_StateCU_ddy_FileFilter) ||
			(__inputName_FileFilter == __input_name_StateCU_ddh_FileFilter) ||
			(__inputName_FileFilter ==	__input_name_StateCU_precip_FileFilter) ||
			(__inputName_FileFilter ==	__input_name_StateCU_temp_FileFilter) ||
			(__inputName_FileFilter ==	__input_name_StateCU_frost_FileFilter) ) {
			// Normal daily or monthly StateMod file...
			JGUIUtil.setWaitCursor ( this, true );
			if ( __inputName_FileFilter == __input_name_StateCU_frost_FileFilter ) {
				tslist = StateCU_TS.readTimeSeriesList (path, null, null, null, false );
			}
			else {
                // Normal StateMod file...
				tslist = StateMod_TS.readTimeSeriesList ( path, null, null, null, false );
			}
			// Change time series data source to StateCU since the file is part of a StateCU data set...
			if ( tslist != null ) {
				size = tslist.size();
				for ( int i = 0; i < size; i++ ) {
					ts = (TS)tslist.get(i);
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

    String selectedDataType = ui_GetSelectedDataType();
    try {
        String path = __inputName_JComboBox.getSelected();
        Message.printStatus ( 1, routine, "Reading StateCU binary output file \"" + path + "\"" );
        List tslist = null;
        JGUIUtil.setWaitCursor ( this, true );
        StateCU_BTS bin = new StateCU_BTS ( path );
        tslist = bin.readTimeSeriesList ( "*.*." + selectedDataType + ".*.*", null, null, null, false );
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
        String selectedTimeStep = ui_GetSelectedTimeStep();
		// TODO SAM 2007-05-14 Need to better handle picking files.
		// Need to pick the file first and detect the time step from the file, similar to the binary file.
        // For now, key off the selected time step.
		if ( selectedTimeStep.equals( __TIMESTEP_DAY)) {
			fc.setDialogTitle (	"Select StateMod Daily Time Series File" );
			StateMod_GUIUtil.addTimeSeriesFilenameFilters(fc, TimeInterval.DAY, false);
		}
		else if ( selectedTimeStep.equals( __TIMESTEP_MONTH)) {
			fc.setDialogTitle (	"Select StateMod Monthly Time Series File" );
			StateMod_GUIUtil.addTimeSeriesFilenameFilters(fc, TimeInterval.MONTH, false);
		}
		if ( fc.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
			// Canceled...
			return;
		}
		String directory = fc.getSelectedFile().getParent();
		JGUIUtil.setLastFileDialogDirectory(directory);
		String path = fc.getSelectedFile().getPath();
		Message.printStatus ( 1, routine, "Reading StateMod file \"" + path + "\"" );
		// Normal daily or monthly format file...
		List<TS> tslist = null;
		JGUIUtil.setWaitCursor ( this, true );
		int interval_base = TimeInterval.MONTH;
		if ( selectedTimeStep.equals(__TIMESTEP_DAY)) {
			interval_base = TimeInterval.DAY;
		}
		else if ( selectedTimeStep.equals(__TIMESTEP_MONTH)) {
			interval_base = TimeInterval.MONTH;
		}
		/* TODO SAM 2007-05-16 Resolve later.  Use readStateMod() in the meantime.
		else if ( __selected_time_step.equals(__TIMESTEP_YEAR)) {
			interval_base = TimeInterval.YEAR;
		}
		*/
		// TODO SAM 2009-11-16 The well rights are not completely supported - can list but not read.
		// Use the ReadStateMod() command.
		if ( StateMod_DiversionRight.isDiversionRightFile(path) ) {
			// First read the diversion rights...
		    List ddr_Vector = StateMod_DiversionRight.readStateModFile ( path );
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
	        // Set the input name and input type, which are not set in the above call
            int size = 0;
            if ( tslist != null ) {
                size = tslist.size();
            }
            for ( int i = 0; i < size; i++ ) {
                TS ts = tslist.get(i);
                ts.getIdentifier().setInputType("StateMod");
                ts.getIdentifier().setInputName(path);
            }
		}
		else if ( StateMod_InstreamFlowRight.isInstreamFlowRightFile(path) ) {
			// First read the instream flow rights...
		    List ifr_Vector = StateMod_InstreamFlowRight.readStateModFile ( path );
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
	        // Set the input name and input type, which are not set in the above call
            int size = 0;
            if ( tslist != null ) {
                size = tslist.size();
            }
            for ( int i = 0; i < size; i++ ) {
                TS ts = tslist.get(i);
                ts.getIdentifier().setInputType("StateMod");
                ts.getIdentifier().setInputName(path);
            }
		}
		else if ( StateMod_ReservoirRight.isReservoirRightFile(path) ) {
			// First read the reservoir rights...
		    List rer_Vector = StateMod_ReservoirRight.readStateModFile ( path );
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
	        // Set the input name and input type, which are not set in the above call
            int size = 0;
            if ( tslist != null ) {
                size = tslist.size();
            }
            for ( int i = 0; i < size; i++ ) {
                TS ts = tslist.get(i);
                ts.getIdentifier().setInputType("StateMod");
                ts.getIdentifier().setInputName(path);
            }
		}
		else if ( StateMod_WellRight.isWellRightFile(path) ) {
			// First read the well rights...
		    List wer_Vector = StateMod_WellRight.readStateModFile ( path );
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
					false ); // don't read data - only header
			// Set the input name and input type, which are not set in the above call
			int size = 0;
			if ( tslist != null ) {
			    size = tslist.size();
			}
			for ( int i = 0; i < size; i++ ) {
			    TS ts = tslist.get(i);
			    ts.getIdentifier().setInputType("StateMod");
			    ts.getIdentifier().setInputName(path);
			}
		}
		else if ( selectedTimeStep.equals(__TIMESTEP_DAY) ) {
			// Daily, only read the headers...
			tslist = StateMod_TS.readTimeSeriesList ( path, null, null, null, false );
		}
		else {
		    // Monthly, only read the headers...
			tslist = StateMod_TS.readTimeSeriesList ( path, null, null, null, false );
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
			Message.printStatus ( 1, routine, "No StateMod time series were read." );
			queryResultsList_Clear ();
        }
        else {
            Message.printStatus ( 1, routine, "" + size + " StateMod time series read." );
        }

		JGUIUtil.setWaitCursor ( this, false );
        ui_UpdateStatus ( false );
	}
	catch ( Exception e ) {
		message = "Error reading StateMod file (" + e + ").";
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
{	String routine = "TSTool_JFrame.uiAction_GetTimeSeriesListClicked_ReadStateModBHeaders";

    String selectedDataType = ui_GetSelectedDataType();
	try {
        String path = __inputName_JComboBox.getSelected();
		Message.printStatus ( 1, routine, "Reading StateMod binary output file \"" + path + "\"" );
		List tslist = null;
		JGUIUtil.setWaitCursor ( this, true );
		StateMod_BTS bin = new StateMod_BTS ( path );
		tslist = bin.readTimeSeriesList ( "*.*." + selectedDataType + ".*.*", null, null, null, false );
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
Read USGS NWIS web service time series and list in the GUI.
*/
private void uiAction_GetTimeSeriesListClicked_ReadUsgsNwisDailyHeaders()
{   String rtn = "TSTool_JFrame.uiAction_GetTimeSeriesListClicked_ReadUsgsNwisDailyHeaders";
    JGUIUtil.setWaitCursor ( this, true );
    Message.printStatus ( 1, rtn, "Please wait... retrieving data");

    DataStore dataStore = ui_GetSelectedDataStore ();
    // The headers are a list of UsgsNwisTimeSeriesMetadata
    try {
        UsgsNwisDailyDataStore usgsNwisDailyDataStore = (UsgsNwisDailyDataStore)dataStore;
        queryResultsList_Clear ();

        String dataType = ui_GetSelectedDataType();
        String timeStep = ui_GetSelectedTimeStep();
        if ( timeStep == null ) {
            Message.printWarning ( 1, rtn, "No time series are available for timestep." );
            JGUIUtil.setWaitCursor ( this, false );
            return;
        }
        else {
            timeStep = timeStep.trim();
        }

        List<UsgsNwisSiteTimeSeriesMetadata> results = null;
        // Data type is shown with name so only use the first part of the choice
        try {
            results = usgsNwisDailyDataStore.readSiteTimeSeriesMetadataList(dataType, timeStep,
                (InputFilter_JPanel)__selectedInputFilter_JPanel);
        }
        catch ( Exception e ) {
            Message.printWarning(1, rtn, "Error getting time series list from USGS NWIS (" + e + ").");
            Message.printWarning(3, rtn, e );
            results = null;
        }

        int size = 0;
        if ( results != null ) {
            size = results.size();
            // TODO Does not work??
            //__query_TableModel.setNewData ( results );
            // Try brute force...
            __query_TableModel = new TSTool_UsgsNwisDaily_TableModel ( usgsNwisDailyDataStore, results );
            TSTool_UsgsNwisDaily_CellRenderer cr =
                new TSTool_UsgsNwisDaily_CellRenderer( (TSTool_UsgsNwisDaily_TableModel)__query_TableModel);

            __query_JWorksheet.setCellRenderer ( cr );
            __query_JWorksheet.setModel ( __query_TableModel );
            __query_JWorksheet.setColumnWidths ( cr.getColumnWidths(), getGraphics() );
        }
        if ( (results == null) || (size == 0) ) {
            Message.printStatus ( 1, rtn, "Query complete.  No records returned." );
        }
        else {
            Message.printStatus ( 1, rtn, "Query complete. " + size + " records returned." );
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
Read the list of time series from a USGS NWIS RDB file and list in the GUI.
*/
private void uiAction_GetTimeSeriesListClicked_ReadUsgsNwisRdbHeaders ()
throws IOException
{	String message, routine = "TSTool_JFrame.readUsgsNwisRdbHeaders";

	try {
        JFileChooser fc = JFileChooserFactory.createJFileChooser ( JGUIUtil.getLastFileDialogDirectory() );
		fc.setDialogTitle ( "Select USGS NWIS RDB File");
		SimpleFileFilter sff = new SimpleFileFilter( "txt", "USGS NWIS RDB File");
		fc.addChoosableFileFilter(sff);
		if ( fc.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
			// Canceled...
			return;
		}
		JGUIUtil.setLastFileDialogDirectory ( fc.getSelectedFile().getParent() );
		String path = fc.getSelectedFile().getPath();

		Message.printStatus ( 1, routine, "Reading USGS NWIS RDB file \"" + path + "\"" );
		JGUIUtil.setWaitCursor ( this, true );
		TS ts = UsgsNwisRdbTS.readTimeSeries(path, null, null, null,false);
		if ( ts == null ) {
			message = "Error reading USGS NWIS RDB file \""+path + "\"";
			Message.printWarning ( 2, routine, message );
			JGUIUtil.setWaitCursor ( this, false );
			throw new IOException ( message );
		}
		List<TS> tslist = new Vector ( 1 );
		tslist.add ( ts );
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
		message = "Error reading USGS NWIS RDB file.";
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
        if ( __tsProcessor != null ) {
            try {
                int selected_tsensembles = JGUIUtil.selectedSize(__resultsTSEnsembles_JList);
                if ( selected_tsensembles == 0 ) {
                    commandProcessor_ProcessEnsembleResultsList ( null, props );
                }
                else {
                    commandProcessor_ProcessEnsembleResultsList ( __resultsTSEnsembles_JList.getSelectedIndices(), props );
                }
            }
            catch ( Exception e ) {
                Message.printWarning ( 1, routine, "Unable to graph time series from ensemble results." );
                Message.printWarning ( 3, routine, e );
            }
        }
    }
    finally {
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
		if ( __tsProcessor != null ) {
			try {
                int selected_ts=JGUIUtil.selectedSize(__resultsTS_JList);
				if ( selected_ts == 0 ) {
					commandProcessor_ProcessTimeSeriesResultsList (	null, props );
				}
				else {
                    commandProcessor_ProcessTimeSeriesResultsList (	__resultsTS_JList.getSelectedIndices(), props );
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
Import a configuration file and merge with the existing configuration file.
@param configFilePath Path to the current configuration file.
*/
private void uiAction_ImportConfiguration ( String configFilePath )
{   String routine = "TSTool_JFrame.uiAction_ImportConfiguration";
    // Print an explanation before continuing
    int x = ResponseJDialog.NO;
    if ( license_IsInstallRTi(license_GetLicenseManager()) ) {
        // Mention the license below...
        x = new ResponseJDialog ( this, IOUtil.getProgramName(),
        "The current TSTool configuration information will be updated as follows:\n\n" +
        "1) You will select a configuration file to merge (e.g., containing new license information).\n" +
        "2) The properties in the current configuration file will be updated to matching properties in the selected file.\n" +
        "3) You will need to restart the software in order for the new information to take effect.\n\n" +
        "Continue?",
        ResponseJDialog.YES| ResponseJDialog.NO).response();
    }
    else {
        // Don't mention the license
        x = new ResponseJDialog ( this, IOUtil.getProgramName(),
            "The current TSTool configuration information will be updated as follows:\n\n" +
            "1) You will select a configuration file to merge (e.g., containing old preferences).\n" +
            "2) The properties in the current configuration file will be updated to matching properties in the selected file.\n" +
            "3) You will need to restart the software in order for the new information to take effect.\n\n" +
            "Continue?",
            ResponseJDialog.YES| ResponseJDialog.NO).response();
    }
    if ( x == ResponseJDialog.NO ) {
        return;
    }
    // Else continue the process.
    // Pick the configuration file.
    File currentConfigFile = new File ( configFilePath );
    JFileChooser fc = JFileChooserFactory.createJFileChooser ( currentConfigFile.getParent() );
    fc.setDialogTitle("Select New " + IOUtil.getProgramName() + " Configuration File");
    SimpleFileFilter sff = new SimpleFileFilter("cfg", "TSTool Configuration File");
    fc.addChoosableFileFilter(sff);
    fc.setFileFilter(sff);
    if (fc.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
        return;
    }
    // Else, continue and do the merge
    try {
        license_MergeConfigFiles ( currentConfigFile, new File(fc.getSelectedFile().getPath()));
        x = new ResponseJDialog ( this, IOUtil.getProgramName(),
            "You must restart the software in order for the new configuration information to take effect.",
            ResponseJDialog.OK).response();
    }
    catch ( Exception e ) {
        Message.printWarning( 1, routine, "There was an error updating the configuration file.  Check permissions." );
        Message.printWarning(3, routine, e);
    }
}

/**
Reset the query options choices based on the selected input name.  Other
method calls are cascaded to fully reset the choices.
@param selectedDataStore if not null, then the blank input name is selected; if null, then the input name
selection is processed
*/
private void uiAction_InputNameChoiceClicked(DataStore selectedDataStore)
{	String routine = getClass().getName() + "uiAction_InputNameChoiceClicked";
	if ( __inputName_JComboBox == null ) {
		return;
	}
	// Default is for selected name and visible name to be the same, but phase in abbreviated visible names for
	// some input types below.
	String selectedInputName = ui_GetSelectedInputName();
	String selectedInputNameVisible = selectedInputName;
	
    if ( selectedDataStore != null ) {
        Message.printStatus(2, routine, "Blanking out input type because data store \"" +
            selectedDataStore.getName() + "\" has been selected." );
        // Set the input name to blank, adding the blank item if necessary
        if ( __inputName_JComboBox.getItemCount() == 0 ) {
            __inputName_JComboBox.add("");
        }
        else if ( !__inputName_JComboBox.getItemAt(0).equals("") ) {
            __inputName_JComboBox.addAt("",0);
        }
        ui_SetIgnoreItemEvent(true);
        __inputName_JComboBox.select("");
        ui_SetIgnoreItemEvent(false);
        return;
    }

	if ( Message.isDebugOn ) {
		Message.printDebug ( 1, routine, "Input name has been selected:  \"" + selectedInputName + "\"" );
	}

	// List alphabetically...
	try {
	    String selectedInputType = ui_GetSelectedInputType();
        if ( selectedInputType.equals ( __INPUT_TYPE_HECDSS ) ) {
            // Get the position of the visible name
            int listIndex = StringUtil.indexOf(__inputNameHecDssVisibleList,selectedInputNameVisible);
            Message.printStatus( 2, routine, "Visible item is in position " + listIndex );
            selectedInputName = __inputNameHecDssList.get(listIndex);
            Message.printStatus( 2, routine, "File corresponding to visible item is \"" + selectedInputName + "\"" );
            // A new file was slected so clear the part choices and regenerate the lists...
            ((HecDssTSInputFilter_JPanel)__inputFilterHecDss_JPanel).setHecDssFile(new File(selectedInputName));
            ((HecDssTSInputFilter_JPanel)__inputFilterHecDss_JPanel).refreshChoices(true);
        }
        else if ( selectedInputType.equals ( __INPUT_TYPE_NWSRFS_FS5Files ) ) {
    		// Reset the data types...
    		__dataType_JComboBox.setEnabled ( true );
    		__dataType_JComboBox.removeAll ();
    		// TODO SAM 2004-09-01 need to find a way to not re-read the data types file.
    		List dataTypes = NWSRFS_Util.getTimeSeriesDataTypes ( __nwsrfs_dmi, true ); // Include description
    		__dataType_JComboBox.setData ( dataTypes );
    		__dataType_JComboBox.select ( null );
    		__dataType_JComboBox.select ( 0 );
    	}
	}
	catch ( Exception e ) {
		Message.printWarning ( 3, routine, e );
	}
}

/**
Reset the query options choices based on the selected input type.  Other
method calls are cascaded to fully reset the choices.  This method also
shows/hides columns in the query results multilist to be appropriate for the data input source.
@param selectedDataStore if not null, then the input type choice is being cascaded through from
a data store selection and just needs to futher cascade to the input type, setting both to blank.
If null, then input type and name are fully processed, ignoring the data store.
*/
private void uiAction_InputTypeChoiceClicked ( DataStore selectedDataStore )
{	String routine = "TSTool_JFrame.inputTypeChoiceClicked";
	if ( __input_type_JComboBox == null ) {
		if ( Message.isDebugOn ) {
			Message.printDebug ( 1, routine, "Input type has been selected but GUI is not yet initialized.");
		}
		return;	// Not done initializing.
	}
	if ( selectedDataStore != null ) {
	    Message.printStatus(2, routine, "Blanking out input type because data store \"" +
	        selectedDataStore.getName() + "\" has been selected." );
	    // Set the input type to blank, adding the blank item if necessary
        if ( __input_type_JComboBox.getItemCount() == 0 ) {
            __input_type_JComboBox.add("");
        }
        else if ( !__input_type_JComboBox.getItemAt(0).equals("") ) {
            __input_type_JComboBox.addAt("",0);
        }
        ui_SetIgnoreItemEvent(true);
        __input_type_JComboBox.select("");
        ui_SetIgnoreItemEvent(false);
	    // Now chain to selecting the input name - this will blank the input name selection
	    uiAction_InputNameChoiceClicked(selectedDataStore);
	    return;
	}
	// Get the selected input type
    String selectedInputType = ui_GetSelectedInputType();
	if ( selectedInputType == null ) {
		// Apparently this happens when setData() or similar is called
	    // on the JComboBox, and before select() is called.
		if ( Message.isDebugOn ) {
			Message.printDebug ( 1, routine, "Input type has been selected:  null (select is ignored)" );
		}
		return;
	}
	// If here, make sure to blank out the selection on the data store so that the user is not confused by
	// seeing both
	ui_SetIgnoreItemEvent(true);
	__dataStore_JComboBox.select ( "" );
	ui_SetIgnoreItemEvent(false);

	if ( Message.isDebugOn ) {
		Message.printDebug ( 1, routine, "Input type has been selected:  \"" + selectedInputType + "\"" );
	}

	// List alphabetically...
	try {
	    if ( selectedInputType.equals ( __INPUT_TYPE_DateValue ) ) {
    	    uiAction_SelectInputType_DateValue ();
    	}
    	else if ( selectedInputType.equals ( __INPUT_TYPE_DIADvisor ) ) {
    	    uiAction_SelectInputType_DIADvisor ();
    	}
        else if ( selectedInputType.equals ( __INPUT_TYPE_HECDSS ) ) {
            uiAction_SelectInputType_HECDSS ();
        }
    	else if ( selectedInputType.equals ( __INPUT_TYPE_HydroBase )) {
    	    uiAction_SelectInputType_HydroBase();
    	}
    	else if ( selectedInputType.equals ( __INPUT_TYPE_MEXICO_CSMN ) ) {
    	    uiAction_SelectInputType_MexicoCSMN();
    	}
    	else if ( selectedInputType.equals ( __INPUT_TYPE_MODSIM ) ) {
    	    uiAction_SelectInputType_MODSIM();
    	}
    	else if ( selectedInputType.equals ( __INPUT_TYPE_NWSCARD ) ) {
    	    uiAction_SelectInputType_NwsCard();
    	}
    	else if ( selectedInputType.equals ( __INPUT_TYPE_NWSRFS_FS5Files)) {
    	    uiAction_SelectInputType_NwsrfsFs5files();
    	}
    	else if(selectedInputType.equals ( __INPUT_TYPE_NWSRFS_ESPTraceEnsemble)) {
    	    uiAction_SelectInputType_NwsrfsEspTraceEnsemble();
    	}
    	else if ( selectedInputType.equals ( __INPUT_TYPE_RiverWare ) ) {
    	    uiAction_SelectInputType_RiverWare();
    	}
    	else if ( selectedInputType.equals ( __INPUT_TYPE_StateCU ) ) {
    		// Prompt for a StateCU file and update choices...
    		uiAction_SelectInputType_StateCU ( true );
    	}
        else if ( selectedInputType.equals ( __INPUT_TYPE_StateCUB ) ) {
            // Prompt for a StateCU binary file and update choices...
            uiAction_SelectInputType_StateCUB ( true );
        }
    	else if ( selectedInputType.equals ( __INPUT_TYPE_StateMod ) ) {
    	    uiAction_SelectInputType_StateMod ();
    	}
    	else if ( selectedInputType.equals ( __INPUT_TYPE_StateModB ) ) {
    		// Prompt for a StateMod binary file and update choices...
    		uiAction_SelectInputType_StateModB ( true );
    	}
    	else if ( selectedInputType.equals ( __INPUT_TYPE_UsgsNwisRdb ) ) {
    	    uiAction_SelectInputType_UsgsNwis();
    	}
    	else {
    	    // Not a recognized input source...
    		Message.printWarning ( 1, routine,"\"" + selectedInputType + "\" is not a recognized input type." );
    		return;
    	}
	}
	catch ( Exception e ) {
		Message.printWarning ( 1, routine, "Error setting up choices for " + selectedInputType +
		    " input type (" + e + ")." );
		Message.printWarning ( 3, routine, e );
	}
}

/**
Create a new command file.  If any existing commands have been modified the
user has the option of saving.  Then, the existing commands are cleared and the
command file name is reset to null.
*/
private void uiAction_NewCommandFile ()
{   // See whether the old commands need to be saved/cleared...
    if ( !uiAction_OpenCommandFile_CheckForSavingCommands() ) {
        return;
    }

    // Clear the commands reset the name to null...

    commandList_RemoveAllCommands ();
    commandList_SetDirty ( false ); // deleteCommands() sets to true but
                    // since we are clearing the name, the commands are not dirty
    commandList_SetCommandFileName ( null );
    // Clear the old results...
    results_Clear();
    ui_UpdateStatusTextFields ( 2, null, null, "Use Commands menu to insert commands", __STATUS_READY );
    __processor_JProgressBar.setValue ( 0 );
    __command_JProgressBar.setValue ( 0 );
}

/**
Open a connection to the ColoradoSMS database.  If running in batch mode, the
CDSS configuration file is used to determine ColoradoSMS server and database
name properties to use for the initial connection.  If no configuration file
exists, then a default connection is attempted.
@param startup If true, indicates that the database connection is being made
at startup.  This is the case, for example, when multiple HydroBase databases
may be available and there is no reason to automatically connect to one of them for all users.
*/
private void uiAction_OpenColoradoSMS ( boolean startup )
{	String routine = "TSTool_JFrame.openColoradoSMS";
	Message.printStatus ( 1, routine, "Opening ColoradoSMS connection..." );
	// TODO SAM 2005-10-18
	// Always connect, whether in batch mode or not.  Might need a way to configure this.
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
	// See whether the old commands need to be saved/cleared...
    if ( !uiAction_OpenCommandFile_CheckForSavingCommands() ) {
        return;
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
		Message.printStatus(2, routine,
		    "Working directory (and initial working directory) from command file is \"" +
		    IOUtil.getProgramWorkingDir() );
		// Load but do not automatically run.
		ui_LoadCommandFile ( path, false );
	}
	// New file has been opened or there was a cancel/error and the old list remains.
	//Message.printStatus ( 2, routine, "Done reading commands.  Calling ui_UpdateStatus...");
	ui_UpdateStatus ( true );
	//Message.printStatus ( 2, routine, "Back from update status." );
}

/**
Check whether existing commands need to be saved (called when loading or creating a new command file).
@return true if the operation (open or new) should continue, or false if user has canceled.
*/
private boolean uiAction_OpenCommandFile_CheckForSavingCommands()
{
    if ( !__commandsDirty ) {
        // No need to do anything so return true that it is OK to continue with operation
        return true;
    }
    if ( __commandFileName == null ) {
        // Have not been saved before.
        // Always allow save, even if read-only comment is set (since first save).
        int x = ResponseJDialog.NO;
        if ( __commands_JListModel.size() > 0 ) {
            x = new ResponseJDialog ( this, IOUtil.getProgramName(),
            "Do you want to save the changes you made?",
            ResponseJDialog.YES| ResponseJDialog.NO|ResponseJDialog.CANCEL).response();
        }
        if ( x == ResponseJDialog.CANCEL ) {
            return false;
        }
        else if ( x == ResponseJDialog.YES ) {
            // Prompt for the name and then save...
            uiAction_WriteCommandFile ( __commandFileName, true, false );
        }
    }
    else {
        // A command file exists...  Warn the user.  They can save to the existing file name or can cancel and
        // File...Save As... to a different name.
        int x = ResponseJDialog.NO;
        if ( __commands_JListModel.size() > 0 ) {
            if ( __tsProcessor.getReadOnly() ) {
                x = new ResponseJDialog ( this, IOUtil.getProgramName(),
                    "Do you want to save the changes made to:\n"
                    + "\"" + __commandFileName + "\"?\n\n" +
                    "The commands are marked read-only.\n" +
                    "Press Yes to update the read-only file before opening a new file.\n" +
                    "Press No to discard edits before opening a new file.\n" +
                    "Press Cancel and then save to a new name if desired.\n",
                    ResponseJDialog.YES|ResponseJDialog.NO|ResponseJDialog.CANCEL).response();
            }
            else {
                x = new ResponseJDialog ( this, IOUtil.getProgramName(),
                "Do you want to save the changes made to:\n"
                + "\"" + __commandFileName + "\"?",
                ResponseJDialog.YES| ResponseJDialog.NO|ResponseJDialog.CANCEL).response();
            }
        }
        if ( x == ResponseJDialog.CANCEL ) {
            return false;
        }
        else if ( x == ResponseJDialog.YES ) {
            uiAction_WriteCommandFile ( __commandFileName, false, false );
        }
        // Else if No or OK will clear before opening the other file...
    }
    return true;
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
TODO SAM 2010-09-13 Streamline this when HydroBase is converted to a data store from input type/name
Open a connection to the HydroBase database.
@param startup if true, then the connection is being made at software startup.  In this case
if AutoConnect=True in the configuration, the dialog will not be shown.
*/
private void uiAction_OpenHydroBase ( boolean startup )
{	String routine = "TSTool_JFrame.uiAction_OpenHydroBase";
	Message.printStatus ( 1, routine, "Opening HydroBase connection..." );

    // Running interactively so:
	// 1) If AutoConnect=True, automatically connect using default info
	// 2) If AutoConnect=False (default), prompt the user to select and login to HydroBase.
	// This is a modal dialog that will not allow anything else to occur until the
	// information is entered.  Use a PropList to pass information because there are many
	// parameters that may change in the future.
	
	// Determine whether the database connection should be automatically made...
	boolean AutoConnect_boolean = false; // Default is to use the login dialog
	// Get the property from the TSTool configuration file because ultimately this will be by
	// user and the generic CDSS information will continue to be for all users
	String AutoConnect = TSToolMain.getPropValue("HydroBase.AutoConnect");
	if ( (AutoConnect != null) && AutoConnect.equalsIgnoreCase("true") ) {
	    AutoConnect_boolean = true;
	}

	HydroBaseDMI hbdmi = null; // DMI that is opened by the dialog or automatically - null if cancel or error
    String error = ""; // Message for whether there was an unexpected error opening HydroBase.
    boolean usedDialog = false;
	if ( startup && AutoConnect_boolean ) {
	    Message.printStatus ( 2, routine, "HydroBase.AutoConnect=True in TSTool.cfg configuration file so " +
	    	"autoconnecting to HydroBase with default connection information from CDSS.cfg." );
	    hbdmi = TSToolMain.openHydroBase(__tsProcessor);
	    // Further checks are made below for UI setup
	}
	else if ( (startup && license_IsInstallCDSS(__licenseManager)) || !startup ) {
	    // Use the login dialog
    	PropList hb_props = new PropList ( "SelectHydroBase" );
    	hb_props.set ( "ValidateLogin", "false" );
    	hb_props.set ( "ShowWaterDivisions", "false" );
    
    	// Pass in the previous HydroBaseDMI so that its information
    	// can be displayed as the initial values...
    
    	SelectHydroBaseJDialog selectHydroBaseJDialog = null;
    	usedDialog = true;
    	
    	try {
            // Let the dialog check HydroBase properties in the CDSS configuration file...
    	    HydroBaseDMI dmi = null;
    	    if ( ui_GetHydroBaseDMI() != null ) {
    	        dmi = ui_GetHydroBaseDMI();
    	    }
    		selectHydroBaseJDialog = new SelectHydroBaseJDialog ( this, dmi, hb_props );
    		// After getting to here, the dialog has been closed.
    		// The HydroBaseDMI from the dialog can be retrieved and used...
    		hbdmi = selectHydroBaseJDialog.getHydroBaseDMI();
    	}
    	catch ( Exception e ) {
    		error = "Error opening HydroBase connection.  ";
    		Message.printWarning ( 3, routine, e );
    		hbdmi = null;
    	}
	}
    // If no HydroBase connection was opened, print an appropriate message...
    if ( hbdmi == null ) {
        if ( (ui_GetHydroBaseDataStore() == null) && usedDialog ) {
            // No previous connection known to the UI - print this warning if the dialog was attempted
            Message.printWarning ( 1, routine, error + "HydroBase features will be disabled." );
        }
        else if ( usedDialog ) {
            // Had a previous connection in the UI so continue to use it
            Message.printWarning ( 1, routine, error + "The previous HydroBase connection will be used." );
        }
    }
	else if ( hbdmi == ui_GetHydroBaseDMI() ) {
	    // Same instance was returned as original (user canceled) - no need to do anything
	    // TODO SAM 2008-10-23 If this step is not ignored, the GUI removes HydroBase from the interface?
	}
	else {
	    // New connection has been established
    	// Set the HydroBaseDMI for the GUI and command processor...
	    Message.printStatus(2, routine,
	        "Using new HydroBase connection in UI (refreshing HydroBase features) and processor." );
    	commandProcessor_SetHydroBaseDMI ( hbdmi );
    	// Set the UI instance last because setting in the processor may close the old connection and
    	// therefore close the one referenced by the UI...
    	ui_SetHydroBaseDataStore ( hbdmi );
    	// Enable/disable HydroBase features as necessary...
    	ui_CheckHydroBaseFeatures();
	}
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
			__input_name_NWSRFS_FS5Files.add ( path );
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
Open a connection to the RiversideDB database based on information in a configuration file.
Currently this ONLY works on the newer configuration file, not TSTool or RiverTrak configurations files.
@param configFile configuration file containing RiversideDB connection information
@param startup If true, indicates that the database connection is being made
at startup.  In this case, the RiversideDB input type is enabled but there may
not actually be information in the TSTool.cfg file.  This is the case, for
example, when multiple RiversideDB databases may be available and there is no
reason to automatically connect to one of them for all users.
*/
private DataStore uiAction_OpenRiversideDB ( String configFile )
throws Exception
{
    // Read into a PropList...
    PropList rprops = new PropList("");
    rprops.setPersistentName ( configFile );
    rprops.readPersistent ();
    return TSToolMain.openDataStore( rprops, __tsProcessor );
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

	int buffersize = __commandsCutBuffer.size();

	Command command = null;
	for ( int i = 0; i < buffersize; i++ ) {
		command = (Command)__commandsCutBuffer.get(i);
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
	if ( __tsProcessor != null ) {
		// Time series should be in memory so add the TSCommandProcessor as a
		// supplier.  This should result in quick supplying of in-memory time series...
		p.addTSSupplier ( __tsProcessor );
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
Show properties for the selected ensemble(s).
*/
private void uiAction_ResultsEnsembleProperties ()
{   String routine = getClass().getName() + ".uiAction_ResultsEnsembleProperties";
    String message;
    // Get the selected ensembles
    int selected [] = __resultsTSEnsembles_JList.getSelectedIndices();
    if ( (selected == null) || (selected.length == 0) ) {
        // Nothing is selected to process all
        int size = __resultsTSEnsembles_JListModel.size();
        selected = new int[size];
        for ( int i = 0; i < size; i++ ) {
            selected[i] = i;
        }
    }
    // Get the list of time series from the processor, to check whether time series are shared
    List<TS> tslist = null;
    int tsResultsSize = -1;
    try {
        tslist = (List<TS>)__tsProcessor.getPropContents("TSResultsList");
        tsResultsSize = tslist.size();
    }
    catch ( Exception e ) {
        // Should not happen
        message = "Error getting time series list from processor:";
        Message.printWarning ( 3, routine, message );
        Message.printWarning ( 3, routine, e );
    }
    // Get the properties from the ensembles, including included time series properties
    String ensembleDisplay;
    String ensembleID;
    TSEnsemble ensemble;
    List<String> v = new Vector ();
    v.add ( "Ensemble Properties" );
    TS ts;
    for ( int i = 0; i < selected.length; i++ ) {
        // Get the ensemble ID from the list...
        ensembleDisplay = (String)__resultsTSEnsembles_JListModel.get(selected[i]);
        ensemble = results_Ensembles_GetEnsembleID ( ensembleDisplay );
        if ( ensemble == null ) {
            continue;
        }
        ensembleID = ensemble.getEnsembleID();
        if ( ensemble == null ) {
            v.add ( "" );
            v.add ( "EnsembleID:  " + ensembleID );
            v.add ( "  Error getting ensemble properties from processor.");
        }
        else {
            // Format the output and append to what is displayed
            v.add ( "" );
            v.add ( "EnsembleID:  " + ensembleID );
            v.add ( "Ensemble name:  " + ensemble.getEnsembleName() );
            int ensembleSize = ensemble.size();
            v.add ( "Number of time series:  " + ensembleSize );
            for ( int its = 0; its < ensembleSize; its++ ) {
                ts = ensemble.get(its);
                v.add ( "  " + StringUtil.formatString( (its + 1), "%4d") + " Time series identifier:  " +
                    ts.getIdentifierString() );
                v.add ( "        Period:  " + ts.getDate1() + " - " + ts.getDate2() );
                // Determine if the time series exists in the main time series list
                boolean found = false;
                int foundPos = -1;
                if ( tslist == null ) {
                    // Did not have the main time series list for some reason.
                    v.add ( "        Shared with main time series list:  UNKNOWN (no main list available)" );
                }
                else {
                    // Loop through the main time series list and try to match the time series reference,
                    // which will differ if the time series was copied.
                    TS tsr;
                    for ( int irts = 0; irts < tsResultsSize; irts++ ) {
                        tsr = tslist.get(irts);
                        // Compare by reference
                        if ( tsr == ts ) {
                            found = true;
                            foundPos = irts;
                            break;
                        }
                    }
                    if ( found ) {
                        v.add ( "        Shared with main time series list:  YES, time series " + (foundPos + 1) +
                        " (was not copied to ensemble)" );
                    }
                    else {
                        v.add ( "        Shared with main time series list:  NO (was copied to ensemble)" );
                    }
                }
            }
        }
    }
    PropList reportProp = new PropList ("ReportJFrame.props");
    // Too big (make this big when we have more stuff)...
    //reportProp.set ( "TotalWidth", "750" );
    //reportProp.set ( "TotalHeight", "550" );
    reportProp.set ( "TotalWidth", "800" );
    reportProp.set ( "TotalHeight", "500" );
    reportProp.set ( "DisplayFont", __FIXED_WIDTH_FONT );
    reportProp.set ( "DisplaySize", "11" );
    reportProp.set ( "PrintFont", __FIXED_WIDTH_FONT );
    reportProp.set ( "PrintSize", "7" );
    reportProp.set ( "Title", "Ensemble Properties" );
    new ReportJFrame ( v, reportProp );
}

/**
Run the commands in the command list.  These time series are saved in
__ts_processor and are then available for export, analysis, or viewing.  This
method should only be called if there are commands in the command list.
@param runAllCommands If false, then only the selected commands are run.  If
true, then all commands are run.
@param createOutput If true, then all write* methods are processed by the
TSEngine.  If false, only the time series in memory remain at the end and can
be viewed.  The former is suitable for batch files, both for the GUI.
*/
private void uiAction_RunCommands ( boolean runAllCommands, boolean createOutput )
{	String routine = "TSTool_JFrame.uiAction_RunCommands";
	ui_UpdateStatusTextFields ( 1, routine, null, "Running commands...", __STATUS_BUSY);
	results_Clear ();
	System.gc();
	// Get commands to run (all or selected)...
	List commands = commandList_GetCommands ( runAllCommands );
	// The limits of the command progress bar are handled in commandStarted().
	// Run the commands in a thread.
	commandProcessor_RunCommandsThreaded ( commands, createOutput );
	// List of time series is displayed when CommandProcessorListener
	// detects that the last command is complete.
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
            // layers of recursion can occur when running a command file)...
            TSCommandProcessorUtil.closeRegressionTestReportFile();
			results_Clear();
            uiAction_RunCommands_ShowResultsEnsembles();
            uiAction_RunCommands_ShowResultsOutputFiles();
            uiAction_RunCommands_ShowResultsProblems();
            uiAction_RunCommands_ShowResultsTables();
			uiAction_RunCommands_ShowResultsTimeSeries();
			uiAction_RunCommands_ShowResultsViews();
            
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
	// Only add a file if not already in the list
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
Display the list of problems from the commands.
*/
private void uiAction_RunCommands_ShowResultsProblems()
{   String routine = getClass().getName() + ".uiAction_RunCommands_ShowResultsProblems";
    //Message.printStatus ( 2, routine, "Entering method.");
    try {
        // Get all of the command log messages from all commands for run phase...
        List commands = __tsProcessor.getCommands();
        List logRecordList = CommandStatusUtil.getLogRecordList ( commands, null );//CommandPhaseType.RUN );
        Message.printStatus( 2, routine, "There were " + logRecordList.size() +
            " problems processing " + commands.size() + " commands.");
        // Create a new table model for the problem list.
        // TODO SAM 2009-03-01 Evaluate whether should just update data in existing table model (performance?)
        CommandLog_TableModel tableModel = new CommandLog_TableModel ( logRecordList );
        CommandLog_CellRenderer cellRenderer = new CommandLog_CellRenderer( tableModel );
        __resultsProblems_JWorksheet.setCellRenderer ( cellRenderer );
        __resultsProblems_JWorksheet.setModel ( tableModel );
        __resultsProblems_JWorksheet.setColumnWidths ( cellRenderer.getColumnWidths() );
        ui_SetIgnoreActionEvent(false);
    }
    catch ( Exception e ) {
        Message.printWarning( 3 , routine, e);
        Message.printWarning ( 1, routine, "Unexpected error displaying problems from run (" + e +
            ") - contact support.");
    }
    //Message.printStatus ( 2, "uiAction_RunCommands_ShowProblems", "Leaving method.");
}

/**
Display the table results.
*/
private void uiAction_RunCommands_ShowResultsTables()
{   // Get the list of tables from the processor.
    //Message.printStatus ( 2, "uiAction_RunCommands_ShowResultsTables", "Entering method.");
    // Get the list of table identifiers from the processor.
    List<DataTable> table_List = commandProcessor_GetTableResultsList();
    int size = 0;
    if ( table_List != null ) {
        size = table_List.size();
    }
    ui_SetIgnoreActionEvent(true);
    DataTable table;
    for ( int i = 0; i < size; i++ ) {
        table = table_List.get(i);
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
	DateTime date1, date2;
	boolean [] selected_boolean = new boolean[size];   // Size to results list
	// HTML brackets to deal with data issues - is red OK or is that a problem for people that are color-blind?
	String htmlStart = "<html><span style=\"color:red;font-weight:bold\">", htmlStart2;
	String htmlEnd = "</span></html>", htmlEnd2;
	for ( int i = 0; i < size; i++ ) {
		selected_boolean[i] = false;
		try {
            ts = commandProcessor_GetTimeSeries(i);
		}
		catch ( Exception e ) {
			results_TimeSeries_AddTimeSeriesToResults ( htmlStart + (i + 1) +
			    ") - Error getting time series from processor." + htmlEnd );
			Message.printWarning ( 3, routine, e );
			continue;
		}
		if ( ts == null ) {
			results_TimeSeries_AddTimeSeriesToResults ( htmlStart + (i + 1) +
			    ") - Null time series from processor." + htmlEnd );
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
			date1 = ts.getDate1();
			date2 = ts.getDate2();
			String dateString = null;
			htmlStart2 = "";
			htmlEnd2 = "";
			if ( (date1 == null) || (date2 == null) ) {
			    dateString = " (NO DATA)";
			    htmlStart2 = htmlStart;
			    htmlEnd2 = htmlEnd;
			}
			else if ( !ts.hasData() ) {
			    dateString = " (" + ts.getDate1() + " to " + ts.getDate2() + " NO DATA)";
	            htmlStart2 = htmlStart;
	            htmlEnd2 = htmlEnd;
			}
			else {
			    dateString = " (" + ts.getDate1() + " to " + ts.getDate2() + ")";
			}
			results_TimeSeries_AddTimeSeriesToResults ( htmlStart2 + (i + 1) + ") " + alias +
			    desc + " - " + ts.getIdentifier() +	dateString + htmlEnd2 );
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
	__resultsTS_JList.setSelectedIndices ( selected );
	ui_UpdateStatus ( false );
	ui_UpdateStatusTextFields ( 1, routine, null, "Completed running commands.  Use Results and Tools menus.",
			__STATUS_READY );
	// Make sure that the user is not waiting on the wait cursor....
	//JGUIUtil.setWaitCursor ( this, false );
	
	// TODO SAM 2010-08-09 Evaluate whether this fixes the problem and evaluate the approach
	// Redraw the list because sometimes it gets out of sync with the UI
	__resultsTS_JList.invalidate();
	
	//Message.printStatus ( 2, "uiAction_RunCommands_ShowResultsTimeSeries", "Leaving method.");
}

/**
Display the table results.
*/
private void uiAction_RunCommands_ShowResultsViews()
{   // Get the list of views from the processor.
    //Message.printStatus ( 2, "uiAction_RunCommands_ShowResultsTables", "Entering method.");
    // Get the list of table identifiers from the processor.
    List<TimeSeriesView> view_List = commandProcessor_GetTimeSeriesViewResultsList();
    int size = 0;
    if ( view_List != null ) {
        size = view_List.size();
    }
    TimeSeriesView view;
    for ( int i = 0; i < size; i++ ) {
        view = view_List.get(i);
        results_Views_AddView ( view );
    }
    //Message.printStatus ( 2, "uiAction_RunCommands_ShowResultsTables", "Leaving method.");
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
			HydroBaseDMI hbdmi = null;
			if ( ui_GetHydroBaseDataStore() != null ) {
			    hbdmi = (HydroBaseDMI)ui_GetHydroBaseDataStore().getDMI();
			}
			commandProcessor_SetHydroBaseDMI ( runner.getProcessor(), hbdmi );
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

//TODO SAM 2007-08-31 - need to enable/disable filters based on the list of time series that are selected.
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
	FileFilter[] nwscardFilters = null;
	if ( __source_NWSCard_enabled ) {
		nwscardFilters = NWSCardTS.getFileFilters();
		for (int i = 0; i < nwscardFilters.length - 1; i++) {
			fc.addChoosableFileFilter(nwscardFilters[i]);
		}
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
Select all commands in the commands list.  This occurs in response to a user selecting a menu choice.
*/
private void uiAction_SelectAllCommands()
{	JGUIUtil.selectAll(ui_GetCommandJList());
	ui_UpdateStatus ( true );
}

/**
Refresh the query choices for a ColoradoWaterHBGuest web service.
*/
private void uiAction_SelectDataStore_ColoradoWaterHBGuest ( ColoradoWaterHBGuestDataStore selectedDataStore)
throws Exception
{   //String routine = getClass().getName() + "uiAction_SelectInputName_ColoradoWaterHBGuest";
    // Input name is not currently used...
    ui_SetInputNameVisible(false); // Not needed
    __inputName_JComboBox.removeAll ();
    __inputName_JComboBox.setEnabled ( false );
    String selectedInputType = ui_GetSelectedInputType();
    // Get the distinct list of data types (HBGuest variables) for all stations...
    __dataType_JComboBox.setEnabled ( true );
    __dataType_JComboBox.removeAll ();
    __dataType_JComboBox.removeAll();
    ColoradoWaterHBGuestService service = selectedDataStore.getColoradoWaterHBGuestService();
    List<String> dataTypes = service.getTimeSeriesDataTypes (
        HydroBase_Util.DATA_TYPE_AGRICULTURE |
        //HydroBase_Util.DATA_TYPE_DEMOGRAPHICS_ALL |
        //HydroBase_Util.DATA_TYPE_HARDWARE |
        // Comment out stations until performance is figured out
        HydroBase_Util.DATA_TYPE_STATION_ALL |
        HydroBase_Util.DATA_TYPE_STRUCTURE_ALL, // |
        //HydroBase_Util.DATA_TYPE_WIS,
        true ); // Add notes
    __dataType_JComboBox.setData ( dataTypes );
    // Select the default (this causes the other choices to be updated)...
    // TODO SAM 2010-07-21 Default to Streamflow once implemented, like HydroBase
    __dataType_JComboBox.select( null );
    __dataType_JComboBox.select ( "Diversion - DivTotal" );
    
    // Initialize with blank DivTotal data - will be reset when a query occurs
    __query_TableModel = new TSTool_HydroBase_StructureGeolocStructMeasType_TableModel(
        __query_JWorksheet, StringUtil.atoi(__props.getValue("HydroBase.WDIDLength")), null, selectedInputType);
    TSTool_HydroBase_StructureGeolocStructMeasType_CellRenderer cr =
        new TSTool_HydroBase_StructureGeolocStructMeasType_CellRenderer(
            (TSTool_HydroBase_StructureGeolocStructMeasType_TableModel)__query_TableModel);
    __query_JWorksheet.setCellRenderer ( cr );
    __query_JWorksheet.setModel ( __query_TableModel );
    // Remove columns that are not appropriate...
    __query_JWorksheet.setColumnWidths ( cr.getColumnWidths() );
}

/**
Refresh the query choices for the currently selected ColoradoWaterSMS data store.
*/
private void uiAction_SelectDataStore_ColoradoWaterSMS ( ColoradoWaterSMSDataStore selectedDataStore )
throws Exception
{   //String routine = getClass().getName() + "uiAction_SelectInputName_ColoradoWaterSMS";
    ui_SetInputNameVisible(false); // Not needed for data stores
    __inputName_JComboBox.removeAll ();
    __inputName_JComboBox.setEnabled ( false );
    String selectedInputType = ui_GetSelectedInputType();
    // Get the distinct list of data types (SMS variables) for all stations...
    __dataType_JComboBox.setEnabled ( true );
    __dataType_JComboBox.removeAll ();
    ColoradoWaterSMS service = selectedDataStore.getColoradoWaterSMS();
    List<String> dataTypes = ColoradoWaterSMSAPI.readDistinctStationVariableList ( service, true );
    __dataType_JComboBox.removeAll();
    for ( String dataType : dataTypes  ) {
        __dataType_JComboBox.add ( dataType );
    }
    __dataType_JComboBox.add ( "*" ); // Allow users to list all available data
    __dataType_JComboBox.select ( null );
    __dataType_JComboBox.select ( 0 );
    
    // Timestep is irregular (for real-time), and hour and day aggregations
    __timeStep_JComboBox.setEnabled ( true );
    __timeStep_JComboBox.removeAll ();
    __timeStep_JComboBox.add ( __TIMESTEP_IRREGULAR );
    __timeStep_JComboBox.add ( __TIMESTEP_HOUR );
    __timeStep_JComboBox.add ( __TIMESTEP_DAY );
    __timeStep_JComboBox.select ( 0 );

    // Initialize with blank data vector...

    //__query_TableModel = new TSTool_TS_TableModel(null);
    //TSTool_TS_CellRenderer cr = new TSTool_TS_CellRenderer((TSTool_TS_TableModel)__query_TableModel);
    __query_TableModel = new TSTool_HydroBase_StationGeolocMeasType_TableModel(
        __query_JWorksheet, null, selectedInputType);
    TSTool_HydroBase_StationGeolocMeasType_CellRenderer cr =
        new TSTool_HydroBase_StationGeolocMeasType_CellRenderer(
            (TSTool_HydroBase_StationGeolocMeasType_TableModel)__query_TableModel);
    __query_JWorksheet.setCellRenderer ( cr );
    __query_JWorksheet.setModel ( __query_TableModel );
    // Remove columns that are not appropriate...
    __query_JWorksheet.setColumnWidths ( cr.getColumnWidths() );
}

/**
Set up the query options because the HydroBase input type has been selected.
*/
private void uiAction_SelectDataStore_HydroBase ( HydroBaseDataStore selectedDataStore )
{   String routine = getClass().getName() + ".uiAction_SelectDataStore_HydroBase";
    // Input name cleared and disabled...
    ui_SetInputNameVisible(false); // Not needed
    __inputName_JComboBox.removeAll();
    __inputName_JComboBox.setEnabled ( false );
    // Data type - get the time series choices from the HydroBase_Util code...
    __dataType_JComboBox.setEnabled ( true );
    __dataType_JComboBox.removeAll ();
    HydroBaseDMI dmi = (HydroBaseDMI)((DatabaseDataStore)selectedDataStore).getDMI();
    List<String> data_types =
        HydroBase_Util.getTimeSeriesDataTypes (dmi,
        HydroBase_Util.DATA_TYPE_AGRICULTURE |
        HydroBase_Util.DATA_TYPE_DEMOGRAPHICS_ALL |
        HydroBase_Util.DATA_TYPE_HARDWARE |
        HydroBase_Util.DATA_TYPE_STATION_ALL |
        HydroBase_Util.DATA_TYPE_STRUCTURE_ALL |
        HydroBase_Util.DATA_TYPE_WIS,
        true ); // Add notes
    __dataType_JComboBox.setData ( data_types );

    // Select the default (this causes the other choices to be updated)...

    __dataType_JComboBox.select( null );
    __dataType_JComboBox.select(HydroBase_Util.getDefaultTimeSeriesDataType(dmi, true ) );

    // Initialize with blank data list
    // TODO SAM 2012-09-05 Initialize with correct table model - for now use stations because it will get reset
    // when data are read (different table model might confuse user)

    try {
        __query_TableModel = new TSTool_HydroBase_StationGeolocMeasType_TableModel(
            __query_JWorksheet,
            //StringUtil.atoi(__props.getValue("HydroBase.WDIDLength")),
            null);
        TSTool_HydroBase_StationGeolocMeasType_CellRenderer cr =
            new TSTool_HydroBase_StationGeolocMeasType_CellRenderer(
                (TSTool_HydroBase_StationGeolocMeasType_TableModel)__query_TableModel);
        __query_JWorksheet.setCellRenderer ( cr );
        __query_JWorksheet.setModel ( __query_TableModel );
        __query_JWorksheet.setColumnWidths (cr.getColumnWidths() );
    }
    catch ( Exception e ) {
        // Absorb the exception in most cases - print if developing to see if this issue can be resolved.
        if ( Message.isDebugOn && IOUtil.testing()  ) {
            Message.printWarning ( 3, routine,
            "For developers:  caught exception blanking HydroBase JWorksheet at setup." );
            Message.printWarning ( 3, routine, e );
        }
    }
}

/**
Refresh the query choices for the currently selected RCC ACIS store.
*/
private void uiAction_SelectDataStore_RccAcis ( RccAcisDataStore selectedDataStore )
throws Exception
{   //String routine = getClass().getName() + "uiAction_SelectDataStore_RccAcis";
    RccAcisDataStore dataStore = (RccAcisDataStore)selectedDataStore;
    ui_SetInputNameVisible(false); // Not needed for data stores
    // Get the list of valid object/data types from the data store
    List<String> dataTypes = dataStore.getDataTypeStrings ( true, true );
    
    // Populate the list of available data types and select the first
    __dataType_JComboBox.setEnabled ( true );
    __dataType_JComboBox.removeAll ();
    __dataType_JComboBox.setData ( dataTypes );
    __dataType_JComboBox.select ( null );
    __dataType_JComboBox.select ( 0 );
    
    // Initialize the time series list with blank data list...
    __query_TableModel = new TSTool_RccAcis_TableModel( dataStore, null);
    TSTool_RccAcis_CellRenderer cr = new TSTool_RccAcis_CellRenderer((TSTool_RccAcis_TableModel)__query_TableModel);
    __query_JWorksheet.setCellRenderer ( cr );
    __query_JWorksheet.setModel ( __query_TableModel );
    __query_JWorksheet.setColumnWidths ( cr.getColumnWidths() );
}

/**
Refresh the query choices for the currently selected ReclamationHDB data store.
*/
private void uiAction_SelectDataStore_ReclamationHDB ( ReclamationHDBDataStore selectedDataStore )
throws Exception
{   //String routine = getClass().getName() + "uiAction_SelectDataStore_ReclamationHDB";
    // Get the DMI instances for the matching data store
    ReclamationHDB_DMI dmi = (ReclamationHDB_DMI)((DatabaseDataStore)selectedDataStore).getDMI();
    ui_SetInputNameVisible(false); // Not needed for HDB
    __dataType_JComboBox.removeAll ();
    // Get the list of valid object/data types from the database
    List<String> dataTypes = dmi.getObjectDataTypes ( true );
    // Add a wildcard option to get all data types
    dataTypes.add(0,"*");
    __dataType_JComboBox.setData ( dataTypes );
    __dataType_JComboBox.setEnabled ( true );
    
    // Get the list of timesteps that are valid for the data type
    // Need to trigger a select to populate the input filters
    __timeStep_JComboBox.removeAll ();
    __timeStep_JComboBox.add ( __TIMESTEP_HOUR );
    __timeStep_JComboBox.add ( __TIMESTEP_DAY );
    __timeStep_JComboBox.add ( __TIMESTEP_MONTH );
    __timeStep_JComboBox.add ( __TIMESTEP_YEAR );
    __timeStep_JComboBox.add ( __TIMESTEP_IRREGULAR ); // Instantaneous handled as irregular
    // FIXME SAM 2010-10-26 Could handle WY as YEAR, but need to think about it to be consistent with TSTool in general
    __timeStep_JComboBox.select ( __TIMESTEP_MONTH );
    __timeStep_JComboBox.setEnabled ( true );
 
    // Initialize with blank data vector...
    __query_TableModel = new TSTool_ReclamationHDB_TableModel( (ReclamationHDBDataStore)selectedDataStore, null, null);
    TSTool_ReclamationHDB_CellRenderer cr =
        new TSTool_ReclamationHDB_CellRenderer((TSTool_ReclamationHDB_TableModel)__query_TableModel);
    __query_JWorksheet.setCellRenderer ( cr );
    __query_JWorksheet.setModel ( __query_TableModel );
    __query_JWorksheet.setColumnWidths ( cr.getColumnWidths() );
}

/**
Refresh the query choices for the currently selected RiversideDB data store.
*/
private void uiAction_SelectDataStore_RiversideDB ( RiversideDBDataStore selectedDataStore )
throws Exception
{   String routine = getClass().getName() + "uiAction_SelectDataStore_RiversideDB";
    // Get the DMI instances for the matching data store
    RiversideDB_DMI rdmi = (RiversideDB_DMI)((DatabaseDataStore)selectedDataStore).getDMI();
    ui_SetInputNameVisible(false); // Not needed
    __dataType_JComboBox.setEnabled ( true );
    __dataType_JComboBox.removeAll ();
    List<RiversideDB_MeasType> mts = null;
    List<RiversideDB_DataType> dts = null;
    try {
        mts = rdmi.readMeasTypeListForDistinctData_type();
        dts = rdmi.readDataTypeList();
    }
    catch ( Exception e ) {
        Message.printWarning ( 1, routine, "Error getting time series choices (" + e + ")." );
        Message.printWarning ( 3, routine, e );
        Message.printWarning ( 3, routine, rdmi.getLastSQLString() );
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
            mt = mts.get(i);
            pos = RiversideDB_DataType.indexOf (dts, mt.getData_type() );
            if ( pos < 0 ) {
                __dataType_JComboBox.add(mt.getData_type() );
            }
            else {
                data_type = mt.getData_type() + " - " + dts.get(pos).getDescription();
                if ( data_type.length() > 30 ) {
                    __dataType_JComboBox.add( data_type.substring(0,30) + "..." );
                }
                else {
                    __dataType_JComboBox.add( data_type );
                }
            }
        }
        __dataType_JComboBox.select ( null );
        __dataType_JComboBox.select ( 0 );
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

    __query_TableModel = new TSTool_RiversideDB_TableModel(selectedDataStore,null);
    TSTool_RiversideDB_CellRenderer cr =
        new TSTool_RiversideDB_CellRenderer((TSTool_RiversideDB_TableModel)__query_TableModel);
    __query_JWorksheet.setCellRenderer ( cr );
    __query_JWorksheet.setModel ( __query_TableModel );
    // Remove columns that are not appropriate...
    //__query_JWorksheet.removeColumn (((TSTool_RiversideDB_TableModel)__query_TableModel).COL_SEQUENCE );
    __query_JWorksheet.setColumnWidths ( cr.getColumnWidths() );
}

/**
Refresh the query choices for the currently selected USGS NWIS daily data store.
*/
private void uiAction_SelectDataStore_UsgsNwisDaily ( UsgsNwisDailyDataStore selectedDataStore )
throws Exception
{   //String routine = getClass().getName() + "uiAction_SelectDataStore_UsgsNwis";
    UsgsNwisDailyDataStore dataStore = (UsgsNwisDailyDataStore)selectedDataStore;
    ui_SetInputNameVisible(false); // Not needed for data stores
    // Get the list of valid object/data types from the data store
    List<String> dataTypes = dataStore.getParameterStrings ( true );
    
    // Populate the list of available data types and select the first
    __dataType_JComboBox.setEnabled ( true );
    __dataType_JComboBox.removeAll ();
    __dataType_JComboBox.setData ( dataTypes );
    __dataType_JComboBox.select ( null );
    __dataType_JComboBox.select ( 0 );
    
    // Initialize the time series list with blank data list...
    __query_TableModel = new TSTool_UsgsNwisDaily_TableModel( dataStore, null);
    TSTool_UsgsNwisDaily_CellRenderer cr = new TSTool_UsgsNwisDaily_CellRenderer((TSTool_UsgsNwisDaily_TableModel)__query_TableModel);
    __query_JWorksheet.setCellRenderer ( cr );
    __query_JWorksheet.setModel ( __query_TableModel );
    __query_JWorksheet.setColumnWidths ( cr.getColumnWidths() );
}

/**
Set up query choices because a HECDSS file name has been selected.
Prompt for a HECDSS input name (binary file name).  When selected, update the choices.
@param resetInputNames If true, the input names will be repopulated with values from __inputNameHecDssListVisible.
@exception Exception if there is an error.
*/
private void uiAction_SelectInputName_HECDSS ( boolean resetInputNames )
throws Exception
{   String routine = "TSTool_JFrame.uiAction_SelectInputName_HECDSS";
    if ( resetInputNames ) {
        // The  input type has been selected as a change from
        // another type.  Repopululate the list if previous choices exist...
        __inputName_JComboBox.setData ( __inputNameHecDssVisibleList );
    }
    // Check the item that is selected...
    String inputName = __inputName_JComboBox.getSelected();
    if ( (inputName == null) || inputName.equals(__BROWSE) ) {
        // Prompt for the name of a HEC-DSS file...
        // Based on the file extension, set the data types and other information...
        JFileChooser fc = JFileChooserFactory.createJFileChooser ( JGUIUtil.getLastFileDialogDirectory() );
        fc.setDialogTitle("Select HEC-DSS Database File");
        SimpleFileFilter sff = new SimpleFileFilter("dss","HEC-DSS Database File");
        fc.addChoosableFileFilter( sff );
        // Only allow recognized extensions...
        fc.setAcceptAllFileFilterUsed ( false );
        if (fc.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
            // User canceled - set the file name back to the original and disable other choices...
            if ( inputName != null ) {
                ui_SetIgnoreItemEvent ( true );
                __inputName_JComboBox.select(null);
                if ( __inputNameHecDssVisibleLast != null ) {
                    __inputName_JComboBox.select ( __inputNameHecDssVisibleLast );
                }
                ui_SetIgnoreItemEvent ( false );
            }
            return;
        }
        // User has chosen a file...

        inputName = fc.getSelectedFile().getPath(); 
        // Save as last selection, using the shorter visible path...
        //__inputNameHecDssLast = inputName; // Not needed?
        String inputNameVisible = ui_CreateAbbreviatedVisibleFilename(inputName);
        __inputNameHecDssVisibleLast = inputNameVisible;
        JGUIUtil.setLastFileDialogDirectory (fc.getSelectedFile().getParent() );

        // Set the input name...

        ui_SetIgnoreItemEvent ( true );
        if ( !JGUIUtil.isSimpleJComboBoxItem (__inputName_JComboBox,__BROWSE, JGUIUtil.NONE, null, null ) ) {
            // The "Browse" choice is not already in the visible list so add it at the beginning.  Also add to
            // the invisible file list to keep the indices the same.
            __inputNameHecDssVisibleList.add ( __BROWSE );
            __inputNameHecDssList.add ( __BROWSE );
            __inputName_JComboBox.add ( __BROWSE );
        }
        if ( !JGUIUtil.isSimpleJComboBoxItem (__inputName_JComboBox,inputName, JGUIUtil.NONE, null, null ) ) {
            // The selected file is not already in so add after the browse string (files
            // are listed chronologically by select with most recent at the top)...
            if ( __inputName_JComboBox.getItemCount() > 1 ) {
                __inputName_JComboBox.addAt ( inputNameVisible, 1 );
                __inputNameHecDssVisibleList.add ( 1, inputNameVisible );
                __inputNameHecDssList.add ( 1, inputName );
            }
            else {
                __inputName_JComboBox.add ( inputNameVisible );
                __inputNameHecDssVisibleList.add(inputNameVisible);
                __inputNameHecDssList.add(inputName);
            }
        }
        ui_SetIgnoreItemEvent ( false );
        // Select the file in the input name because leaving it on
        // browse will disable the user's ability to reselect browse...
        __inputName_JComboBox.select ( null );
        __inputName_JComboBox.select ( inputNameVisible );
    }

    __inputName_JComboBox.setEnabled ( true );

    // Set the data types and time step based on the file extension...

    __dataType_JComboBox.setEnabled ( true );
    __dataType_JComboBox.removeAll ();

    List dataTypes = new Vector();
    int intervalBase = TimeInterval.MONTH; // Default

    // Fill data types choice...

    Message.printStatus ( 2, routine, "Setting HEC-DSS data types..." );
    __dataType_JComboBox.setData ( dataTypes );
    Message.printStatus ( 2, routine, "Selecting the first HEC-DSS data type..." );
    __dataType_JComboBox.select ( null );
    if ( dataTypes.size() > 0 ) {
        __dataType_JComboBox.select ( 0 );
    }

    // Set time step appropriately...

    __timeStep_JComboBox.removeAll ();
    if ( intervalBase == TimeInterval.MONTH ) {
        __timeStep_JComboBox.add ( __TIMESTEP_MONTH );
    }
    else if ( intervalBase == TimeInterval.DAY ) {
        __timeStep_JComboBox.add ( __TIMESTEP_DAY );
    }
    __timeStep_JComboBox.setEnabled ( true ); // Enabled, but one visible
    __timeStep_JComboBox.select ( null );
    __timeStep_JComboBox.select ( 0 );

    // Initialize with blank data vector...

    __query_TableModel = new TSTool_HecDss_TableModel(null);
    TSTool_HecDss_CellRenderer cr = new TSTool_HecDss_CellRenderer((TSTool_HecDss_TableModel)__query_TableModel);
    __query_JWorksheet.setCellRenderer ( cr );
    __query_JWorksheet.setModel ( __query_TableModel );
    // Turn off columns in the table model that do not apply...
    __query_JWorksheet.removeColumn (((TSTool_HecDss_TableModel)__query_TableModel).COL_SEQUENCE);
    __query_JWorksheet.setColumnWidths ( cr.getColumnWidths() );
}

/**
Set up the query choices because an NWSRFS FS5files directory has been selected.
Prompt for a NWSRFS FS5Files input name (FS5Files directory).  When selected, update the choices.
@param reset_input_names If true, the input names will be repopulated with
values from __input_name_NWSRFS_FS5Files.
@exception Exception if there is an error.
*/
private void uiAction_SelectInputName_NWSRFS_FS5Files ( boolean reset_input_names )
throws Exception
{   String routine = "TSTool_JFrame.selectInputName_NWSRFS_FS5Files";
    // TODO SAM 2004-09-10 it does not seem like the following case ever
    // is true - need to decide whether to take out.
    if ( reset_input_names ) {
        // The NWSRFS_FS5Files input type has been selected as a change
        // from another type.  Repopululate the list if previous choices exist...
        //int size = __input_name_NWSRFS_FS5Files.size();
        // TODO - probably not needed...
        //__input_name_JComboBox.removeAll();
        // This does NOT include the leading browse, etc...
        __inputName_JComboBox.setData ( __input_name_NWSRFS_FS5Files );
    }
    // Check the item that is selected...
    String inputName = __inputName_JComboBox.getSelected();
    if ( (inputName == null) || inputName.equals(__BROWSE) ) {
        // Prompt for the name of an NWSRFS FS5Files directory...
        // Based on the file extension, set the data types and other information...
        JFileChooser fc = JFileChooserFactory.createJFileChooser ( JGUIUtil.getLastFileDialogDirectory() );
        fc.setDialogTitle("Select NWSRFS FS5Files Directory");
        fc.setFileSelectionMode (JFileChooser.DIRECTORIES_ONLY );
        if (fc.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
            // User canceled - set the file name back to the original and disable other choices...
            // Ignore programatic events on the combo boxes...
            ui_SetIgnoreItemEvent ( true );
            if ( inputName != null ) {
                __inputName_JComboBox.select(null);
                __inputName_JComboBox.select(inputName);
            }
            ui_SetIgnoreItemEvent ( false );
            return;
        }
        // User has chosen a directory...

        inputName = fc.getSelectedFile().getPath(); 
        JGUIUtil.setLastFileDialogDirectory (fc.getSelectedFile().getParent() );

        // Set the input name...

        ui_SetIgnoreItemEvent ( true );
        if ( !JGUIUtil.isSimpleJComboBoxItem (__inputName_JComboBox, inputName, JGUIUtil.NONE, null, null ) ) {
            // Not already in so add after the browse string (files
            // are listed chronologically by select with most recent at the top...
            if ( IOUtil.isUNIXMachine() ) {
                // Unix/Linux includes "Please Select...",
                // "Use Apps Defaults", and "Browse..."...
                __inputName_JComboBox.addAt ( inputName, 3 );
                __inputName_JComboBox.select ( 3 );
            }
            else {
                // Windows includes only "Browse..."
                __inputName_JComboBox.addAt ( inputName, 1 );
                __inputName_JComboBox.select ( 1 );
            }
            // Insert so we have the list of files later...
            __input_name_NWSRFS_FS5Files.add ( 0, inputName );
        }
        // Select the file in the input name because leaving it on
        // browse will disable the user's ability to reselect browse...
        __inputName_JComboBox.select ( null );
        __inputName_JComboBox.select ( inputName );
        // Now allow item events to occur as normal again...
        ui_SetIgnoreItemEvent ( false );
    }

    __inputName_JComboBox.setEnabled ( true );

    // Open the NWSRFS_DMI instance...

    JGUIUtil.setWaitCursor ( this, true );
    try {
        if ( inputName.equals(__USE_APPS_DEFAULTS) ) {
            Message.printStatus ( 1, routine, "Opening connection to NWSRFS FS5Files using Apps Defaults..." );
            __nwsrfs_dmi = new NWSRFS_DMI();
        }
        else {
            Message.printStatus ( 1, routine, "Opening connection to NWSRFS FS5Files using path \"" +
            inputName + "\"..." );
            __nwsrfs_dmi = new NWSRFS_DMI ( inputName );
        }
        __nwsrfs_dmi.open ();
        Message.printStatus ( 1, routine, "Opened connection to NWSRFS FS5Files." );
    }
    catch ( Exception e ) {
        Message.printWarning ( 1, routine, "Error opening NWSRFS FS5Files (" + e + ")." );
        Message.printWarning ( 2, routine, e );
        __nwsrfs_dmi = null;
        JGUIUtil.setWaitCursor ( this, false );
        return;
        // TODO SAM 2004-09-08 need to remove from the input name list if an error?
    }
    
    // Set the data types and time step based on what is available...

    uiAction_InputNameChoiceClicked ( null );

    // Initialize with blank data vector...

    __query_TableModel = new TSTool_TS_TableModel(null);
    TSTool_TS_CellRenderer cr = new TSTool_TS_CellRenderer( (TSTool_TS_TableModel)__query_TableModel);
    __query_JWorksheet.setCellRenderer ( cr );
    __query_JWorksheet.setModel ( __query_TableModel );
    // Turn off columns in the table model that do not apply...
    __query_JWorksheet.removeColumn (((TSTool_TS_TableModel)__query_TableModel).COL_ALIAS );
    __query_JWorksheet.removeColumn (((TSTool_TS_TableModel)__query_TableModel).COL_SCENARIO);
    __query_JWorksheet.setColumnWidths ( cr.getColumnWidths() );
    JGUIUtil.setWaitCursor ( this, false );
}

/**
Set the query options because the DateValue input type has been selected.
*/
private void uiAction_SelectInputType_DateValue ()
throws Exception
{   String routine = getClass().getName() + ".uiAction_SelectInputName_DateValue";
    // Most information is determined from the file so set the other choices to be inactive...
    ui_SetInputNameVisible(false); // Not used
    __inputName_JComboBox.removeAll();
    __inputName_JComboBox.setEnabled ( false );

    __dataType_JComboBox.setEnabled ( false );
    __dataType_JComboBox.removeAll ();
    __dataType_JComboBox.add( __DATA_TYPE_AUTO );

    // Initialize with blank data vector...

    __query_TableModel = new TSTool_TS_TableModel ( null, true );
    TSTool_TS_CellRenderer cr = new TSTool_TS_CellRenderer( (TSTool_TS_TableModel)__query_TableModel);
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

/**
Set the query options because the DIADvisor input type has been selected.
*/
private void uiAction_SelectInputType_DIADvisor ()
{   String routine = getClass().getName() + ".uiAction_SelectInputName_DIADvisor";
    ui_SetInputNameVisible(false); // Not needed
    __dataType_JComboBox.setEnabled ( true );
    __dataType_JComboBox.removeAll ();
    // Get the data types from the GroupDef table.  Because two values may be available, append "-DataValue" and
    // "-DataValue2" to each group.
    List sensordef_Vector = null;
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
        sensordef = (DIADvisor_SensorDef)sensordef_Vector.get(i);
        __dataType_JComboBox.add( sensordef.getGroup() + "-DataValue" );
        __dataType_JComboBox.add( sensordef.getGroup() + "-DataValue2" );
    }

    // Default to first in the list...
    __dataType_JComboBox.select( null );
    __dataType_JComboBox.select( 0 );

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
    heading_array[__DIADvisor_COL_ID] = "ID";
    width_array[__DIADvisor_COL_ID] =  "75";
    heading_array[__DIADvisor_COL_NAME] = "Name/Description";
    width_array[__DIADvisor_COL_NAME] = "120";
    heading_array[__DIADvisor_COL_DATA_SOURCE] = "Data Source";
    width_array[__DIADvisor_COL_DATA_SOURCE] = "110";
    heading_array[__DIADvisor_COL_DATA_TYPE] = "Data Type";
    width_array[__DIADvisor_COL_DATA_TYPE] = "110";
    heading_array[__DIADvisor_COL_TIMESTEP] = "Time Step";
    width_array[__DIADvisor_COL_TIMESTEP] = "70";
    heading_array[__DIADvisor_COL_SCENARIO] = "Scenario";
    width_array[__DIADvisor_COL_SCENARIO] = "70";
    heading_array[__DIADvisor_COL_UNITS] = "Units";
    width_array[__DIADvisor_COL_UNITS] = "70";
    heading_array[__DIADvisor_COL_START] = "Start";
    width_array[__DIADvisor_COL_START] = "50";
    heading_array[__DIADvisor_COL_END] = "End";
    width_array[__DIADvisor_COL_END] = "50";
    heading_array[__DIADvisor_COL_INPUT_TYPE]= "Input Type";
    width_array[__DIADvisor_COL_INPUT_TYPE] = "85";
    // set MultiList column headings, widths and justifications
    /* TODO
        try {   _queryList_MultiList.clear();
        _queryList_MultiList.redraw();
        _queryList_MultiList.setHeadings( heading_array );
                _queryList_MultiList.setColumnSizes( width_array );
                //_list.setColumnAlignments( justify_array );
        }
        catch ( Exception e ) {
                Message.printWarning( 2, routine, e );
        }
     */
}

/**
Set up the query choices because the HEC DSS input type has been selected.
*/
private void uiAction_SelectInputType_HECDSS ()
throws Exception
{   ui_SetInputNameVisible(true); // Lists files
    // Prompt for a HEC-DSS file and update choices...
    uiAction_SelectInputName_HECDSS ( true );
    // Empty and disable the data type and time step because an input filter with choices from the file is used.
    __dataType_JComboBox.setEnabled ( false );
    __dataType_JComboBox.removeAll ();
    __timeStep_JComboBox.setEnabled ( false );
    __timeStep_JComboBox.removeAll ();
    // Initialize with blank data vector...

    __query_TableModel = new TSTool_HecDss_TableModel ( null, true );
    TSTool_HecDss_CellRenderer cr = new TSTool_HecDss_CellRenderer( (TSTool_HecDss_TableModel)__query_TableModel);
    __query_JWorksheet.setCellRenderer ( cr );
    __query_JWorksheet.setModel ( __query_TableModel );
    __query_JWorksheet.setColumnWidths ( cr.getColumnWidths() );
}

/**
Set up the query options because the HydroBase input type has been selected.
*/
private void uiAction_SelectInputType_HydroBase ()
{   String routine = getClass().getName() + ".uiAction_SelectInputName_HydroBase";
    // Input name cleared and disabled...
    ui_SetInputNameVisible(false); // Not needed
    __inputName_JComboBox.removeAll();
    __inputName_JComboBox.setEnabled ( false );
    // Data type - get the time series choices from the HydroBase_Util code...
    __dataType_JComboBox.setEnabled ( true );
    __dataType_JComboBox.removeAll ();
    List<String> data_types =
        HydroBase_Util.getTimeSeriesDataTypes ( ui_GetHydroBaseDMI(),
        HydroBase_Util.DATA_TYPE_AGRICULTURE |
        HydroBase_Util.DATA_TYPE_DEMOGRAPHICS_ALL |
        HydroBase_Util.DATA_TYPE_HARDWARE |
        HydroBase_Util.DATA_TYPE_STATION_ALL |
        HydroBase_Util.DATA_TYPE_STRUCTURE_ALL |
        HydroBase_Util.DATA_TYPE_WIS,
        true ); // Add notes
    __dataType_JComboBox.setData ( data_types );

    // Select the default (this causes the other choices to be updated)...

    __dataType_JComboBox.select( null );
    __dataType_JComboBox.select(HydroBase_Util.getDefaultTimeSeriesDataType(ui_GetHydroBaseDMI(), true ) );

    // Initialize with blank data list
    // TODO SAM 2012-09-05 Initialize with correct table model - for now use stations because it will get reset
    // when data are read (different table model might confuse user)

    try {
        __query_TableModel = new TSTool_HydroBase_StationGeolocMeasType_TableModel(
            __query_JWorksheet,
            //StringUtil.atoi(__props.getValue("HydroBase.WDIDLength")),
            null);
        TSTool_HydroBase_StationGeolocMeasType_CellRenderer cr =
            new TSTool_HydroBase_StationGeolocMeasType_CellRenderer(
                (TSTool_HydroBase_StationGeolocMeasType_TableModel)__query_TableModel);
        __query_JWorksheet.setCellRenderer ( cr );
        __query_JWorksheet.setModel ( __query_TableModel );
        __query_JWorksheet.setColumnWidths (cr.getColumnWidths() );
    }
    catch ( Exception e ) {
        // Absorb the exception in most cases - print if developing to see if this issue can be resolved.
        if ( Message.isDebugOn && IOUtil.testing()  ) {
            Message.printWarning ( 3, routine,
            "For developers:  caught exception blanking HydroBase JWorksheet at setup." );
            Message.printWarning ( 3, routine, e );
        }
    }
}

/**
Set up query choices because the MEXICO CSMN input type has been selected.
*/
private void uiAction_SelectInputType_MexicoCSMN ()
throws Exception
{
    // Most information is determined from the file but let the user limit the time series that will be liste...
    ui_SetInputNameVisible(false); // Not needed
    __inputName_JComboBox.removeAll();
    __inputName_JComboBox.setEnabled ( false );

    __dataType_JComboBox.removeAll ();
    __dataType_JComboBox.add ( "EV - Evaporation" );
    __dataType_JComboBox.add ( "PP - Precipitation" );
    __dataType_JComboBox.setEnabled ( true );

    // Set the default, which cascades other settings...

    __dataType_JComboBox.select ( 0 );

    // Initialize with blank data vector...

    __query_TableModel = new TSTool_TS_TableModel(null);
    TSTool_TS_CellRenderer cr = new TSTool_TS_CellRenderer( (TSTool_TS_TableModel)__query_TableModel);
    __query_JWorksheet.setCellRenderer ( cr );
    __query_JWorksheet.setModel ( __query_TableModel );
    __query_JWorksheet.setColumnWidths ( cr.getColumnWidths() );
}

/**
Set up the query options because the MODSIM input type has been selected
*/
private void uiAction_SelectInputType_MODSIM ()
throws Exception
{   ui_SetInputNameVisible(false); // Not needed
    // Most information is determined from the file...
    __dataType_JComboBox.setEnabled ( false );
    __dataType_JComboBox.removeAll ();
    __dataType_JComboBox.add( __DATA_TYPE_AUTO );

    // Initialize with blank data vector...

    __query_TableModel = new TSTool_TS_TableModel(null);
    TSTool_TS_CellRenderer cr = new TSTool_TS_CellRenderer((TSTool_TS_TableModel)__query_TableModel);
    __query_JWorksheet.setCellRenderer ( cr );
    __query_JWorksheet.setModel ( __query_TableModel );
    // Remove columns that are not appropriate...
    __query_JWorksheet.removeColumn ( ((TSTool_TS_TableModel)__query_TableModel).COL_SEQUENCE );
    __query_JWorksheet.setColumnWidths ( cr.getColumnWidths() );
}

/**
Set up query choices because NWS Card input type has been selected.
*/
private void uiAction_SelectInputType_NwsCard ()
throws Exception
{
    // Most information is determined from the file...
    ui_SetInputNameVisible(false); // Not needed
    __inputName_JComboBox.removeAll();
    __inputName_JComboBox.setEnabled ( false );

    __dataType_JComboBox.setEnabled ( false );
    __dataType_JComboBox.removeAll();
    __dataType_JComboBox.add( __DATA_TYPE_AUTO );

    // Initialize with blank data vector...

    __query_TableModel = new TSTool_TS_TableModel(null);
    TSTool_TS_CellRenderer cr = new TSTool_TS_CellRenderer( (TSTool_TS_TableModel)__query_TableModel);
    __query_JWorksheet.setCellRenderer ( cr );
    __query_JWorksheet.setModel ( __query_TableModel );
    // Remove columns that are not appropriate...
    __query_JWorksheet.removeColumn ( ((TSTool_TS_TableModel)__query_TableModel).COL_SEQUENCE );
    __query_JWorksheet.setColumnWidths ( cr.getColumnWidths() );
}

/**
Set up query choices becase NWSRFS ESP trace ensemble input type was seleted.
*/
private void uiAction_SelectInputType_NwsrfsEspTraceEnsemble ()
throws Exception
{
    // Most information is determined from the file...
    ui_SetInputNameVisible(false); // Not needed
    __inputName_JComboBox.removeAll();
    __inputName_JComboBox.setEnabled ( false );

    __dataType_JComboBox.setEnabled ( false );
    __dataType_JComboBox.removeAll ();
    __dataType_JComboBox.add( __DATA_TYPE_AUTO );

    // Initialize with blank data vector...

    __query_TableModel = new TSTool_TS_TableModel ( null, true );
    TSTool_TS_CellRenderer cr = new TSTool_TS_CellRenderer( (TSTool_TS_TableModel)__query_TableModel);
    __query_JWorksheet.setCellRenderer ( cr );
    __query_JWorksheet.setModel ( __query_TableModel );
    __query_JWorksheet.setColumnWidths ( cr.getColumnWidths() );
}

/**
Set up query choices because the NWSRFS FS5Files input type was selected.
*/
private void uiAction_SelectInputType_NwsrfsFs5files ()
throws Exception
{   String routine = getClass().getName() + ".uiAction_SelectInputType_NwsrfsFs5files";
    // Update the input name and let the user choose Apps Defaults or pick a directory...
    ui_SetIgnoreItemEvent ( true );     // Do this to prevent item event cascade
    ui_SetInputNameVisible(true); // Lists files
    __inputName_JComboBox.removeAll();
    if ( IOUtil.isUNIXMachine() ) {
        __inputName_JComboBox.add ( __PLEASE_SELECT );
        __inputName_JComboBox.add ( __USE_APPS_DEFAULTS );
        __inputName_JComboBox.select ( __PLEASE_SELECT );
    }
    __inputName_JComboBox.add ( __BROWSE );
    // If previous choices were selected, show them...
    int size = __input_name_NWSRFS_FS5Files.size();
    for ( int i = 0; i < size; i++ ) {
        __inputName_JComboBox.add ( (String)__input_name_NWSRFS_FS5Files.get(i) );
    }
    // Try to select the current DMI (it should always work if the logic is correct)...

    boolean choice_ok = false;  // Needed to force data types to cascade correctly...
    if ( __nwsrfs_dmi != null ) {
        String path = __nwsrfs_dmi.getFS5FilesLocation();
        try {
            __inputName_JComboBox.select ( path );
            choice_ok = true;
        }
        catch ( Exception e ) {
            Message.printWarning ( 2, routine,
                    "Unable to select current NWSRFS FS5Files \"" + path + "\" in input names." );
        }
    }
    __inputName_JComboBox.setEnabled ( true );

    // Enable when an input name is picked...

    __dataType_JComboBox.setEnabled ( false );
    __dataType_JComboBox.removeAll ();

    // Enable when a data type is picked...

    __timeStep_JComboBox.setEnabled ( false );
    __timeStep_JComboBox.removeAll ();

    ui_SetIgnoreItemEvent ( false );        // Item events OK again

    if ( (__inputName_JComboBox.getItemCount() == 1) && (__inputName_JComboBox.getSelected().equals(__BROWSE))){
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
        uiAction_InputNameChoiceClicked( null );
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

/**
Set up query choices because the Riverware input type has been selected.
*/
private void uiAction_SelectInputType_RiverWare ()
throws Exception
{
    // Most information is determined from the file...
    ui_SetInputNameVisible(false); // Not needed
    __inputName_JComboBox.removeAll();
    __inputName_JComboBox.setEnabled ( false );

    __dataType_JComboBox.setEnabled ( false );
    __dataType_JComboBox.removeAll();
    __dataType_JComboBox.add( __DATA_TYPE_AUTO );

    // Initialize with blank data vector...

    __query_TableModel = new TSTool_TS_TableModel(null);
    TSTool_TS_CellRenderer cr = new TSTool_TS_CellRenderer( (TSTool_TS_TableModel)__query_TableModel);
    __query_JWorksheet.setCellRenderer ( cr );
    __query_JWorksheet.setModel ( __query_TableModel );
    // Remove columns that are not appropriate...
    __query_JWorksheet.removeColumn ( ((TSTool_TS_TableModel)__query_TableModel).COL_SEQUENCE );
    __query_JWorksheet.setColumnWidths ( cr.getColumnWidths() );
}

/**
Prompt for a StateCU input name (one of several files).  When selected, update the choices.
@param reset_input_names If true, the input names will be repopulated with values from __input_name_StateCU.
@exception Exception if there is an error.
*/
private void uiAction_SelectInputType_StateCU ( boolean reset_input_names )
throws Exception
{	ui_SetInputNameVisible(true); // Lists files
    if ( reset_input_names ) {
		// The StateCU input type has been selected as a change from another type.
        // Repopululate the list if previous choices exist...
		__inputName_JComboBox.setData ( __inputNameStateCU );
	}
	// Check the item that is selected...
	String inputName = __inputName_JComboBox.getSelected();
	if ( (inputName == null) || inputName.equals(__BROWSE) ) {
		// Prompt for the name of a StateCU file...
		// Based on the file extension, set the data types and other information...
		JFileChooser fc = JFileChooserFactory.createJFileChooser ( JGUIUtil.getLastFileDialogDirectory() );
		// Only handle specific files.  List alphabetically by the description...
		fc.setAcceptAllFileFilterUsed ( false );
		fc.setDialogTitle ( "Select StateCU Input or Output File" );
		__input_name_StateCU_cds_FileFilter =new SimpleFileFilter("cds","Crop Pattern (Yearly)");
		fc.addChoosableFileFilter(__input_name_StateCU_cds_FileFilter);
		__input_name_StateCU_ddh_FileFilter =new SimpleFileFilter("ddh",
			"Historical Direct Diversions - StateMod format (Monthly)");
		fc.addChoosableFileFilter(__input_name_StateCU_ddh_FileFilter);
		__input_name_StateCU_ddy_FileFilter = new SimpleFileFilter(
			"ddy","Historical Direct Diversions - StateMod format (Daily)");
		fc.addChoosableFileFilter(__input_name_StateCU_ddy_FileFilter);
		__input_name_StateCU_ipy_FileFilter = new SimpleFileFilter("ipy", "Irrigation Practice (Yearly)");
			fc.addChoosableFileFilter(__input_name_StateCU_ipy_FileFilter);
		__input_name_StateCU_tsp_FileFilter = new SimpleFileFilter("tsp",
			"Irrigation Practice - old TSP extension (Yearly)");
		fc.addChoosableFileFilter(__input_name_StateCU_tsp_FileFilter);
		__input_name_StateCU_ddc_FileFilter = new SimpleFileFilter(
			"ddc", "Irrigation Water Requirement - StateMod format (Monthly)");
		fc.addChoosableFileFilter(__input_name_StateCU_ddc_FileFilter);
		__input_name_StateCU_iwr_FileFilter = new SimpleFileFilter(
			"iwr", "Irrigation Water Requirement - StateMod format (Monthly)");
		fc.addChoosableFileFilter(__input_name_StateCU_iwr_FileFilter);
		__input_name_StateCU_iwrrep_FileFilter =
			new SimpleFileFilter("iwr",	"Irrigation Water Requirement - StateCU report (Monthly, Yearly)");
		fc.addChoosableFileFilter(__input_name_StateCU_iwrrep_FileFilter);
		__input_name_StateCU_frost_FileFilter = new SimpleFileFilter("stm", "Frost Dates (Yearly)");
		fc.addChoosableFileFilter(__input_name_StateCU_frost_FileFilter);
		__input_name_StateCU_precip_FileFilter = new SimpleFileFilter(
			"stm", "Precipitation Time Series - StateMod format (Monthly)");
		fc.addChoosableFileFilter(__input_name_StateCU_precip_FileFilter);
		__input_name_StateCU_temp_FileFilter=new SimpleFileFilter("stm",
			"Temperature Time Series - StateMod format (Monthly)");
		fc.addChoosableFileFilter(__input_name_StateCU_temp_FileFilter);
		__input_name_StateCU_wsl_FileFilter =new SimpleFileFilter("wsl",
			"Water Supply Limited CU - StateCU report (Monthly, Yearly)" );
		fc.addChoosableFileFilter(__input_name_StateCU_wsl_FileFilter);
		fc.setFileFilter(__input_name_StateCU_iwrrep_FileFilter);
		if (fc.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
			// User canceled - set the file name back to the original and disable other choices...
			if ( inputName != null ) {
				ui_SetIgnoreItemEvent ( true );
				__inputName_JComboBox.select(null);
				if ( __inputNameStateCULast != null ) {
					__inputName_JComboBox.select(__inputNameStateCULast );
				}
				ui_SetIgnoreItemEvent ( false );
			}
			return;
		}

		// User has chosen a file...

		inputName = fc.getSelectedFile().getPath(); 
		// Save as last selection...
		__inputNameStateCULast = inputName;
		__inputName_FileFilter = fc.getFileFilter();
		JGUIUtil.setLastFileDialogDirectory (fc.getSelectedFile().getParent() );

		// Set the input name...

		ui_SetIgnoreItemEvent ( true );
		if ( !JGUIUtil.isSimpleJComboBoxItem (__inputName_JComboBox, __BROWSE, JGUIUtil.NONE, null, null ) ) {
			// Not already in so add it at the beginning...
			__inputNameStateCU.add ( __BROWSE );
			__inputName_JComboBox.add ( __BROWSE );
		}
		if ( !JGUIUtil.isSimpleJComboBoxItem (__inputName_JComboBox, inputName, JGUIUtil.NONE, null, null ) ) {
			// Not already in so add after the browse string (files
			// are listed chronologically by select with most recent at the top...
			if ( __inputName_JComboBox.getItemCount() > 1 ) {
				__inputName_JComboBox.addAt ( inputName, 1 );
				__inputNameStateCU.add ( 1, inputName );
			}
			else {
			    __inputName_JComboBox.add ( inputName );
				__inputNameStateCU.add(inputName);
			}
		}
		ui_SetIgnoreItemEvent ( false );
		// Select the file in the input name because leaving it on
		// browse will disable the user's ability to reselect browse...
		__inputName_JComboBox.select ( null );
		__inputName_JComboBox.select ( inputName );
	}

	__inputName_JComboBox.setEnabled ( true );

	// Set the data types and time step based on the file extension...

	__dataType_JComboBox.setEnabled ( true );
	__dataType_JComboBox.removeAll ();

	// For now, set the data type to auto.  Later, let users select the data type to be selected.

	__dataType_JComboBox.removeAll ();
	__dataType_JComboBox.add ( __DATA_TYPE_AUTO );
	__dataType_JComboBox.select ( null );
	__dataType_JComboBox.select ( 0 );
	__dataType_JComboBox.setEnabled ( false );

	// Set time step appropriately...

	__timeStep_JComboBox.removeAll ();
	__timeStep_JComboBox.add ( __TIMESTEP_AUTO );
	__timeStep_JComboBox.select ( null );
	__timeStep_JComboBox.select ( 0 );
	__timeStep_JComboBox.setEnabled ( false );

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
Prompt for a StateCUB input name (binary file name).  When selected, update the choices.
@param reset_input_names If true, the input names will be repopulated with
values from __input_name_StateModB.
@exception Exception if there is an error.
*/
private void uiAction_SelectInputType_StateCUB ( boolean reset_input_names )
throws Exception
{   String routine = "TSTool_JFrame.selectInputName_StateCUB";
    ui_SetInputNameVisible(true); // Lists files
    if ( reset_input_names ) {
        // The StateCUB input type has been selected as a change from
        // another type.  Repopululate the list if previous choices exist...
        // TODO - probably not needed...
        //__input_name_JComboBox.removeAll();
        __inputName_JComboBox.setData ( __input_name_StateCUB );
    }
    // Check the item that is selected...
    String input_name = __inputName_JComboBox.getSelected();
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
            // User canceled - set the file name back to the original and disable other choices...
            if ( input_name != null ) {
                ui_SetIgnoreItemEvent ( true );
                __inputName_JComboBox.select(null);
                if ( __input_name_StateCUB_last != null ) {
                    __inputName_JComboBox.select ( __input_name_StateCUB_last );
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
        if ( !JGUIUtil.isSimpleJComboBoxItem (__inputName_JComboBox,__BROWSE, JGUIUtil.NONE, null, null ) ) {
            // Not already in so add it at the beginning...
            __input_name_StateCUB.add ( __BROWSE );
            __inputName_JComboBox.add ( __BROWSE );
        }
        if ( !JGUIUtil.isSimpleJComboBoxItem (__inputName_JComboBox,input_name, JGUIUtil.NONE, null, null ) ) {
            // Not already in so add after the browse string (files
            // are listed chronologically by select with most recent at the top...
            if ( __inputName_JComboBox.getItemCount() > 1 ) {
                __inputName_JComboBox.addAt ( input_name, 1 );
                __input_name_StateCUB.add ( 1, input_name );
            }
            else {
                __inputName_JComboBox.add ( input_name );
                __input_name_StateCUB.add(input_name);
            }
        }
        ui_SetIgnoreItemEvent ( false );
        // Select the file in the input name because leaving it on
        // browse will disable the user's ability to reselect browse...
        __inputName_JComboBox.select ( null );
        __inputName_JComboBox.select ( input_name );
    }

    __inputName_JComboBox.setEnabled ( true );

    // Set the data types and time step based on the file extension...

    __dataType_JComboBox.setEnabled ( true );
    __dataType_JComboBox.removeAll ();
    String extension = IOUtil.getFileExtension ( input_name );

    List data_types = null;
    int interval_base = TimeInterval.MONTH; // Default
    int comp = StateCU_DataSet.COMP_UNKNOWN;
    if ( extension.equalsIgnoreCase("bd1" ) ) {
        // CU Locations
        comp = StateCU_DataSet.COMP_CU_LOCATIONS;
    }
    // TODO SAM 2006-01-15 The following is not overly efficient because of a transition in StateMod versions.
    // The version of the format is determined from the file.  For older versions, this is used to
    // return hard-coded parameter lists.  For newer formats, the binary file is reopened and
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
    __dataType_JComboBox.setData ( data_types );
    Message.printStatus ( 2, routine, "Selecting the first StateCUB data type..." );
    __dataType_JComboBox.select ( null );
    if ( data_types.size() > 0 ) {
        __dataType_JComboBox.select ( 0 );
    }

    // Set time step appropriately...

    __timeStep_JComboBox.removeAll ();
    if ( interval_base == TimeInterval.MONTH ) {
        __timeStep_JComboBox.add ( __TIMESTEP_MONTH );
    }
    else if ( interval_base == TimeInterval.DAY ) {
        __timeStep_JComboBox.add ( __TIMESTEP_DAY );
    }
    __timeStep_JComboBox.setEnabled ( true ); // Enabled, but one visible
    __timeStep_JComboBox.select ( null );
    __timeStep_JComboBox.select ( 0 );

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
Set up query choices because the StateMod input type has been selected.
*/
private void uiAction_SelectInputType_StateMod ()
throws Exception
{
    // We disable all but the time step so the user can pick from appropriate files...
    ui_SetInputNameVisible(false); // Not needed
    __inputName_JComboBox.removeAll();
    __inputName_JComboBox.setEnabled ( false );

    __dataType_JComboBox.setEnabled ( false );
    __dataType_JComboBox.removeAll ();
    __dataType_JComboBox.add( __DATA_TYPE_AUTO );
    __dataType_JComboBox.select ( null );
    __dataType_JComboBox.select ( 0 );

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

/**
Prompt for a StateModB input name (binary file name).  When selected, update the choices.
@param resetInputNames If true, the input names will be repopulated with values from __input_name_StateModB.
@exception Exception if there is an error.
*/
private void uiAction_SelectInputType_StateModB ( boolean resetInputNames )
throws Exception
{	String routine = "TSTool_JFrame.selectInputName_StateModB";
    ui_SetInputNameVisible(true); // Lists files
	if ( resetInputNames ) {
		// The StateModB input type has been selected as a change from
		// another type.  Repopululate the list if previous choices exist...
		// TODO - probably not needed...
		//__input_name_JComboBox.removeAll();
		__inputName_JComboBox.setData ( __input_name_StateModB );
	}
	// Check the item that is selected...
	String inputName = __inputName_JComboBox.getSelected();
	if ( (inputName == null) || inputName.equals(__BROWSE) ) {
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
			// User canceled - set the file name back to the original and disable other choices...
			if ( inputName != null ) {
				ui_SetIgnoreItemEvent ( true );
				__inputName_JComboBox.select(null);
				if ( __input_name_StateModB_last != null ) {
					__inputName_JComboBox.select (	__input_name_StateModB_last );
				}
				ui_SetIgnoreItemEvent ( false );
			}
			return;
		}
		// User has chosen a file...

		inputName = fc.getSelectedFile().getPath(); 
		// Save as last selection...
		__input_name_StateModB_last = inputName;
		JGUIUtil.setLastFileDialogDirectory (fc.getSelectedFile().getParent() );

		// Set the input name...

		ui_SetIgnoreItemEvent ( true );
		if ( !JGUIUtil.isSimpleJComboBoxItem (__inputName_JComboBox,__BROWSE, JGUIUtil.NONE, null, null ) ) {
			// Not already in so add it at the beginning...
			__input_name_StateModB.add ( __BROWSE );
			__inputName_JComboBox.add ( __BROWSE );
		}
		if ( !JGUIUtil.isSimpleJComboBoxItem (__inputName_JComboBox,inputName, JGUIUtil.NONE, null, null ) ) {
			// Not already in so add after the browse string (files
			// are listed chronologically by select with most recent at the top...
			if ( __inputName_JComboBox.getItemCount() > 1 ) {
				__inputName_JComboBox.addAt ( inputName, 1 );
				__input_name_StateModB.add ( 1, inputName );
			}
			else {
                __inputName_JComboBox.add ( inputName );
				__input_name_StateModB.add(inputName);
			}
		}
		ui_SetIgnoreItemEvent ( false );
		// Select the file in the input name because leaving it on
		// browse will disable the user's ability to reselect browse...
		__inputName_JComboBox.select ( null );
		__inputName_JComboBox.select ( inputName );
	}

	__inputName_JComboBox.setEnabled ( true );

	// Set the data types and time step based on the file extension...

	__dataType_JComboBox.setEnabled ( true );
	__dataType_JComboBox.removeAll ();
	String extension = IOUtil.getFileExtension ( inputName );

	List dataTypes = null;
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
	// TODO SAM 2006-01-15  The following is not overly efficient because of a transition in StateMod versions.
	// The version of the format is determined from the file.  For older versions, this is used to
	// return hard-coded parameter lists.  For newer formats, the binary file is reopened and
	// the parameters are determined from the file.
	dataTypes = StateMod_Util.getTimeSeriesDataTypes (
			inputName,	// Name of binary file
			comp,	// Component from above, from file extension
			null,	// ID
			null,	// dataset
			StateMod_BTS.determineFileVersion(inputName),
			interval_base,
			false,	// Include input (only output here)
			false,	// Include input, estimated (only output here)
			true,	// Include output (what the binaries contain)
			false,	// Check availability
			true,	// Add group (if available)
			false );// add note

	// Fill data types choice...

	Message.printStatus ( 2, routine, "Setting StateModB data types (" + dataTypes.size() + " types)..." );
	__dataType_JComboBox.setData ( dataTypes );
	Message.printStatus ( 2, routine, "Selecting the first StateModB data type..." );
	__dataType_JComboBox.select ( null );
	__dataType_JComboBox.select ( 0 );

	// Set time step appropriately...

	__timeStep_JComboBox.removeAll ();
	if ( interval_base == TimeInterval.MONTH ) {
		__timeStep_JComboBox.add ( __TIMESTEP_MONTH );
	}
	else if ( interval_base == TimeInterval.DAY ) {
		__timeStep_JComboBox.add ( __TIMESTEP_DAY );
	}
	__timeStep_JComboBox.setEnabled ( true ); // Enabled, but one visible
	__timeStep_JComboBox.select ( null );
	__timeStep_JComboBox.select ( 0 );

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
Set up query choices because the USGS NWIS input type has been selected.
*/
private void uiAction_SelectInputType_UsgsNwis ()
throws Exception
{
    // Most information is determined from the file...
    ui_SetInputNameVisible(false); // Not needed
    __inputName_JComboBox.removeAll();
    __inputName_JComboBox.setEnabled ( false );

    __dataType_JComboBox.setEnabled ( false );
    __dataType_JComboBox.removeAll ();
    __dataType_JComboBox.add( __DATA_TYPE_AUTO );

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

/**
Select on the map the features that are shown in the time series list in the
upper right of the main TSTool interface and zoom to the selected features.
@exception Exception if there is an error showing the related time series features on the map.
*/
private void uiAction_SelectOnMap ()
throws Exception
{	String routine = "TSTool_JFrame.selectOnMap";
	int size = __query_JWorksheet.getRowCount();	// To size Vector
	List idlist = new Vector(size);
	Message.printStatus ( 1, routine, "Selecting and zooming to stations on map.  Please wait...");
	JGUIUtil.setWaitCursor(this, true);
	
	String selectedInputType = ui_GetSelectedInputType();
	String selectedDataType = ui_GetSelectedDataType();
	String selectedTimeStep = ui_GetSelectedTimeStep();

	// Get the list of layers to select from, and the attributes to use...
	// First read the file with the lookup of time series to layer information.

	// TODO SAM 2006-03-01
	// Currently read this each time because it is a small file and it
	// helps in development to not restart.  However, in the future, change so that the file is read once.

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

	List layerlist = new Vector();	// List of layers to match features
	List mapidlist = new Vector();	// List of identifier attributes in map data to match features
	String ts_inputtype, ts_datatype, ts_interval,
		layer_name, layer_location, layer_datasource = "",
		layer_interval = ""; // TSTool input to match against layers.
	TableRecord rec;
	if ( (TS_InputTypeCol_int >= 0) && (TS_DataTypeCol_int >= 0) &&
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
			if (!ts_inputtype.equalsIgnoreCase(	selectedInputType)){
				continue;
			}
			if ( !ts_datatype.equalsIgnoreCase(	selectedDataType)){
				continue;
			}
			if ( !ts_interval.equalsIgnoreCase(	selectedTimeStep)){
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
			layerlist.add ( layer_name );
			attributes.setLength(0);
			attributes.append ( layer_location );
			if ( (Layer_DataSourceCol_int >= 0) && (layer_datasource != null) && !layer_datasource.equals("") ) {
				attributes.append ( "," + layer_datasource );
			}
			if ( (Layer_IntervalCol_int >= 0) && (layer_interval != null) && !layer_interval.equals("") ) {
				attributes.append ( "," + layer_interval );
			}
			mapidlist.add ( attributes.toString() );
		}
	}

	// Determine the list of features (by ID, and optionally data source and interval) to select...
	// TODO SAM 2006-01-16
	// Need to make this more generic by using an interface to retrieve important time series data?

	// Get the worksheet of interest...

	int row = -1, location_col = -1, datasource_col = -1, interval_col = -1;
	if ( selectedInputType.equals ( __INPUT_TYPE_HydroBase )) {
		if ( __query_TableModel instanceof TSTool_TS_TableModel){
			TSTool_TS_TableModel model = (TSTool_TS_TableModel)__query_TableModel;
			location_col = model.COL_ID;
			datasource_col = model.COL_DATA_SOURCE;
			interval_col = model.COL_TIME_STEP;
		}
		else if ( __query_TableModel instanceof TSTool_HydroBase_StationGeolocMeasType_TableModel){
		    TSTool_HydroBase_StationGeolocMeasType_TableModel model =
		        (TSTool_HydroBase_StationGeolocMeasType_TableModel)__query_TableModel;
			location_col = model.COL_ID;
			datasource_col = model.COL_DATA_SOURCE;
			interval_col = model.COL_TIME_STEP;
		}
        else if ( __query_TableModel instanceof TSTool_HydroBase_StructureGeolocStructMeasType_TableModel){
            TSTool_HydroBase_StructureGeolocStructMeasType_TableModel model =
                (TSTool_HydroBase_StructureGeolocStructMeasType_TableModel)__query_TableModel;
            location_col = model.COL_ID;
            datasource_col = model.COL_DATA_SOURCE;
            interval_col = model.COL_TIME_STEP;
        }
        else if ( __query_TableModel instanceof TSTool_HydroBase_GroundWaterWellsView_TableModel){
            TSTool_HydroBase_GroundWaterWellsView_TableModel model =
                (TSTool_HydroBase_GroundWaterWellsView_TableModel)__query_TableModel;
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
			idlist.add ( attributes.toString() );
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
	List matching_features = __geoview_JFrame.getGeoViewJPanel().selectLayerFeatures(
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
        Message.printWarning(1, "uiAction_ShowCommandStatus", "Problem showing Command status (" + t + ").");
        String routine = "TSTool_JFrame.showCommandStatus";
        Message.printWarning(3, routine, t);
      }
    }
  });
  }

/**
Gets Commands status in html - this is currently only a helper for
the above method.  Rename if it will be used generically.
@return command status in HTML format
*/
private String uiAction_ShowCommandStatus_GetCommandsStatus()
{
    List<Command> commands = commandList_GetCommandsBasedOnUI();
    return CommandStatusUtil.getHTMLStatusReport(commands);
}

/**
Show the data stores.
*/
private void uiAction_ShowDataStores ()
{   String routine = getClass().getName() + "uiAction_ShowDataStores";
    try {
        new DataStores_JFrame ( "Data Stores", __tsProcessor.getDataStores() );
    }
    catch ( Exception e ) {
        Message.printWarning ( 1, routine, "Error displaying data stores (" + e + ")." );
    }
}

/**
Show the data units.
*/
private void uiAction_ShowDataUnits ()
{   String routine = getClass().getName() + "uiAction_ShowDataUnits";
    try {
        new DataUnits_JFrame ( "Data Units", DataUnits.getUnitsData() );
    }
    catch ( Exception e ) {
        Message.printWarning ( 1, routine, "Error displaying data units (" + e + ")." );
    }
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
        "Copyright 1997-2012\n" +
        "Developed by Riverside Technology, inc.\n" +
        "Funded by:\n" +
        "Colorado Division of Water Resources\n" +
        "Colorado Water Conservation Board\n" +
        "Send comments about this interface to:\n" +
        "cdss@state.co.us (CDSS)\n" +
        __RTiSupportEmail + " (general)\n", true );
    }
    else {
        // A non-CDSS (RTi customer) installation...
        new HelpAboutJDialog ( this, "About TSTool",
        "TSTool - Time Series Tool\n" +
        IOUtil.getProgramVersion() + "\n" +
        "Copyright 1997-2012\n" +
        "Developed by Riverside Technology, inc.\n" +
        "Licensed to: " + licenseManager.getLicenseOwner() + "\n" +
        "License type: " + licenseManager.getLicenseType() + "\n" +
        "License expires: " + licenseManager.getLicenseExpires() + "\n" +
        "Contact support at:  " + __RTiSupportEmail + "\n", true );
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
	reportProp.set ( "TotalWidth", "800" );
	reportProp.set ( "TotalHeight", "500" );
	reportProp.set ( "DisplayFont", __FIXED_WIDTH_FONT );
	reportProp.set ( "DisplaySize", "11" );
	reportProp.set ( "PrintFont", __FIXED_WIDTH_FONT );
	reportProp.set ( "PrintSize", "7" );
	reportProp.set ( "Title", "TSTool Commands Run Properties" );
	List v = new Vector ( 4 );
	v.add ( "Properties from the last commands run." );
	v.add ( "Note that property values are as of the time the window is opened." );
	v.add ( "Properties are set as defaults initially and change value when a command is processed." );
	v.add ( "" );
	
	String s1 = "", s2 = "";
	// Initial and current working directory...
	v.add ( "Working directory (from user.dir system property):  " + System.getProperty ( "user.dir" ) );
	v.add ( "Initial working directory (internal to TSTool):  " + __tsProcessor.getInitialWorkingDir() );
	try {
	    v.add ( "Current working directory ${WorkingDir}:  " + __tsProcessor.getPropContents("WorkingDir") );
	}
	catch ( Exception e ) {
	    v.add ( "Current working directory ${WorkingDir}:  Unknown" );
	}
    try {
        v.add ( "Software install directory ${InstallDir}:  " + __tsProcessor.getPropContents("InstallDir") );
    }
    catch ( Exception e ) {
        v.add ( "Software install directory ${InstallDir}:  Unknown" );
    }
	// Whether running and cancel requested...
	v.add ( "Are commands running:  " + __tsProcessor.getIsRunning() );
	v.add ( "Has cancel been requested (and is pending):  " + __tsProcessor.getCancelProcessingRequested() );
	v.add ( "Total number of failure/warning messages from run:  " +
	    CommandStatusUtil.getLogRecordListFromCommands(__tsProcessor.getCommands(), CommandPhaseType.RUN ).size());

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
	else {
	    s2 = date2.toString();
	}
	v.add ( "Input (query/read) start: " + s1 );
	v.add ( "Input (query/read) end:   " + s2 );
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
	v.add ( "Output period start: " + s1 );
	v.add ( "Output period end:   " + s2 );
	// Auto-extend period...
	v.add ( "Automatically extend period to output period during read: " +
		commandProcessor_GetAutoExtendPeriod () );
	// Include missing TS automatically...
	v.add ( "Include missing TS automatically: " + commandProcessor_GetIncludeMissingTS() );
	if ( __source_HydroBase_enabled ) {
		v.add ( "" );
		Object o2 = commandProcessor_GetHydroBaseDMIList();
		if ( (o2 == null) || (((List)o2).size() == 0) ) {
			v.add ( "No HydroBase connections are open for the command processor." );
		}
		else {
		    List dmis = (List)o2;
			int size = dmis.size();
			for ( int i = 0; i < size; i++ ) {
				v.add ( "Command processor HydroBase connection information:" );
				try {
					StringUtil.addListToStringList ( v,
						StringUtil.toList( ((HydroBaseDMI)dmis.get(i)).getVersionComments() ) );
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
Show properties from the TSTool session.
*/
private void uiAction_ShowProperties_TSToolSession ( HydroBaseDataStore dataStore )
{   HydroBaseDMI hbdmi = null;
    if ( dataStore != null ) {
        hbdmi = (HydroBaseDMI)dataStore.getDMI();
    }
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
    List<String> v = new Vector<String>( 4 );
    v.add ( "TSTool Session Properties" );
    v.add ( "" );
    if ( __commandFileName == null ) {
        v.add ( "No command file has been read or saved." );
    }
    else {
        v.add ( "Last command file read/saved: \"" + __commandFileName + "\"" );
    }
    v.add ( "Working directory (from user.dir system property):" + System.getProperty ( "user.dir" ) );
    v.add ( "Current working directory (internal to TSTool) = " + IOUtil.getProgramWorkingDir() );
    v.add ( "Run commands in thread (internal to TSTool) = " + ui_Property_RunCommandProcessorInThread() );
    // List open database information...
    if ( __source_ColoradoSMS_enabled ) {
        v.add ( "" );
        if ( __smsdmi == null ) {
            v.add ( "GUI ColoradoSMS connection not defined.");
        }
        else {
            v.add ( "GUI ColoradoSMS connection information:" );
            StringUtil.addListToStringList ( v, StringUtil.toList( __smsdmi.getVersionComments() ) );
        }
    }
    if ( __source_HydroBase_enabled ) {
        v.add ( "" );
        if ( ui_GetHydroBaseDataStore() == null ) {
            v.add ( "GUI HydroBase connection not defined.");
        }
        else {
            v.add ( "GUI HydroBase connection information:" );
            try {
                StringUtil.addListToStringList ( v, StringUtil.toList( hbdmi.getVersionComments() ) );
            }
            catch ( Exception e ) {
                v.add ( "Error getting HydroBase connection information (" + e + ")." );
            }
        }
    }
    // List enabled data types...
    v.add ( "" );
    v.add ( "Input types and whether enabled:" );
    v.add ( "" );
    if ( __source_ColoradoBNDSS_enabled ) {
        v.add ( "ColoradoBNDSS data store is enabled" );
    }
    else {
        v.add ( "ColoradoBNDSS data store is not enabled");
    }
    if ( __source_ColoradoSMS_enabled ) {
        v.add ( "ColoradoSMS input type is enabled" );
    }
    else {
        v.add ( "ColoradoSMS input type is not enabled");
    }
    if ( __source_DateValue_enabled ) {
        v.add ( "DateValue input type is enabled" );
    }
    else {
        v.add ( "DateValue input type is not enabled" );
    }
    if ( __source_DIADvisor_enabled ) {
        v.add ( "DIADvisor input type is enabled" );
    }
    else {
        v.add ( "DIADvisor input type is not enabled" );
    }
    if ( __source_HECDSS_enabled ) {
        v.add ( "HEC-DSS input type is enabled" );
    }
    else {
        v.add ( "HEC-DSS input type is not enabled");
    }
    if ( __source_HydroBase_enabled ) {
        v.add ( "HydroBase input type is enabled" );
    }
    else {
        v.add ( "HydroBase input type is not enabled" );
    }
    if ( __source_MexicoCSMN_enabled ) {
        v.add ( "MexicoCSMN input type is enabled" );
    }
    else {
        v.add ( "MexicoCSMN input type is not enabled" );
    }
    if ( __source_MODSIM_enabled ) {
        v.add ( "MODSIM input type is enabled" );
    }
    else {
        v.add ( "MODSIM input type is not enabled" );
    }
    if ( __source_NWSCard_enabled  ) {
        v.add ( "NWSCARD input type is enabled" );
    }
    else {
        v.add ( "NWSCARD input type is not enabled" );
    }
    if ( __source_NWSRFS_FS5Files_enabled ) {
        v.add ( "NWSRFS FS5Files input type is enabled");
    }
    else {
        v.add ( "NWSRFS FS5Files input type is not enabled" );
    }
    if ( __source_NWSRFS_ESPTraceEnsemble_enabled ) {
        v.add ( "NWSRFS_ESPTraceEnsemble input type is enabled" );
    }
    else {
        v.add ( "NWSRFS_ESPTraceEnsemble input type is not enabled" );
    }
    if ( __source_RCCACIS_enabled ) {
        v.add ( "RCC ACIS data store is enabled" );
    }
    else {
        v.add ( "RCC ACIS data store is not enabled");
    }
    if ( __source_ReclamationHDB_enabled ) {
        v.add ( "ReclamationHDB data store is enabled" );
    }
    else {
        v.add ( "ReclamationHDB data store is not enabled");
    }
    if ( __source_RiversideDB_enabled ) {
        v.add ( "RiversideDB data store is enabled" );
    }
    else {
        v.add ( "RiversideDB data store is not enabled");
    }
    if ( __source_RiverWare_enabled ) {
        v.add ( "RiverWare input type is enabled" );
    }
    else {
        v.add ( "RiverWare input type is not enabled");
    }
    if ( __source_SHEF_enabled ) {
        v.add ( "SHEF input type is enabled" );
    }
    else {
        v.add ( "SHEF input type is not enabled");
    }
    if ( __source_StateCU_enabled ) {
        v.add ( "StateCU input type is enabled" );
    }
    else {
        v.add ( "StateCU input type is not enabled");
    }
    if ( __source_StateCUB_enabled ) {
        v.add ( "StateCUB input type is enabled" );
    }
    else {
        v.add ( "StateCUB input type is not enabled");
    }
    if ( __source_StateMod_enabled ) {
        v.add ( "StateMod input type is enabled" );
    }
    else {
        v.add ( "StateMod input type is not enabled");
    }
    if ( __source_StateModB_enabled ) {
        v.add ( "StateModB input type is enabled" );
    }
    else {
        v.add ( "StateModB input type is not enabled");
    }
    if ( __source_UsgsNwisRdb_enabled ) {
        v.add ( "UsgsNwisRdb input type is enabled" );
    }
    else {
        v.add ( "UsgsNwisRdb input type is not enabled");
    }
    
    v.add ( "" );
    v.add ( "Environment properties:" );
    v.add ( "" );
    v.add ( "Software installation directory = \"" + IOUtil.getApplicationHomeDir() + "\"" );
    v.add ( "Global working directory = \"" + IOUtil.getProgramWorkingDir() + "\"" );
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
{   String routine = getClass().getName() + ".uiAction_ShowResultsOutputFile";
    if ( selected == null ) {
        // May be the result of some UI event...
        return;
    }
    // Display the selected file...
    if ( !( new File( selected ).isAbsolute() ) ) {
        selected = IOUtil.getPathUsingWorkingDir( selected );
    }
    // First try the application that is configured...
    // TODO SAM 2011-03-31 What if a TSTool command file has been expanded?
    try {
        Desktop desktop = Desktop.getDesktop();
        desktop.open ( new File(selected) );
    }
    catch ( Exception e ) {
        // Else display as text (will show up in courier fixed width
        // font, which looks better than the html browser).
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
            Message.printWarning (1, routine, "Unable to view file \"" + selected + "\" (" + e2 + ")." );
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
Method to run test code.  This is usually accessible only when running with the -test command line option.
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
			"J:\\cdss\\develop\\apps\\tstool\\test\\NWSSystemFiles\\DATAUNIT", true );
		DataType.readNWSDataTypeFile (
			"J:\\cdss\\develop\\apps\\tstool\\test\\NWSSystemFiles\\DATATYPE" );
		DataType.readNWSDataTypeFile (
			"J:\\cdss\\develop\\apps\\tstool\\test\\NWSSystemFiles\\DATATYPE2" );
		List datatypes = DataType.getDataTypesData();
		int size = datatypes.size();
		for ( int i = 0; i < size; i++ ) {
			Message.printDebug ( 1, "", "DataType[" + i + "]: " + ((DataType)datatypes.get(i)).toString() );
		}
		SHEFType.readNWSSHEFPPDBFile (
			"J:\\cdss\\develop\\apps\\tstool\\test\\NWSSystemFiles\\SHEFPPDB", true );
		List sheftypes = SHEFType.getSHEFTypesData();
		size = sheftypes.size();
		for ( int i = 0; i < size; i++ ) {
			Message.printDebug ( 1, "", "SHEFType[" + i + "]: " + ((SHEFType)sheftypes.get(i)).toString() );
		}
		// Print out again after SHEF should be matched up...
		datatypes = DataType.getDataTypesData();
		size = datatypes.size();
		for ( int i = 0; i < size; i++ ) {
			Message.printDebug ( 1, "", "DataType[" + i + "]: " + ((DataType)datatypes.get(i)).toString() );
		}
		// Read test input...
		List tslist = DateValueTS.readTimeSeriesList ( "J:\\cdss\\develop\\apps\\TSTool\\test\\Data_SHEF\\TF24.mdk",
			(DateTime)null, (DateTime)null, (String)null, true );
		// Write the SHEF
		ShefATS.writeTimeSeriesList ( tslist, 
			"J:\\cdss\\develop\\apps\\TSTool\\test\\Data_SHEF\\test.shef", false, (DateTime)null, (DateTime)null,
			(List)null, ShefATS.getPEForTimeSeries(tslist), (List)null, (List)null,
			null, null, "DC200311241319", "DH1200", 24, -1);
	}
	else if ( test_num == 1 ) {
		// Test reading multiple time series from a date value file
		// and creating an average time series...
	    List tslist = DateValueTS.readTimeSeriesList (
			"J:\\cdss\\develop\\apps\\TSTool\\test\\Data_EspTraceEnsemble\\CSCI.CSCI.SQIN.ACCUMULATOR",
			(DateTime)null, (DateTime)null, (String)null, true );
		int size = tslist.size() - 1;
		List tslist2 = new Vector(size);
		TS ts;
		for ( int i = 0; i < size; i++ ) {
			// Add time series that have ACFT units and "accum" in the description, but not the
			// last time series, which is the average that we are trying to test here.
			ts = (TS)tslist.get(i);
			if ( ts.getDataUnits().equalsIgnoreCase("ACFT") && (ts.getAlias().indexOf("Accum") >= 0) ) {
				tslist2.add ( ts );
			}
		}
		PropList props = new PropList ( "test" );
		props.set ( "TransferData=Sequentially" );
		TS newts = TSUtil.average ( tslist2, null, null, props );
		tslist2.add ( newts );
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
		List layerviews = __geoview_JFrame.getGeoViewJPanel().getLayerViews(null);
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
			lv = (GeoLayerView)layerviews.get(i);
			if(lv.getName().equalsIgnoreCase(analyzed_layer_name)){
				break;
			}
			else {
			    lv = null;
			}
		}
		if ( lv == null ) {
			Message.printWarning ( 1, routine, "Unable to find layer to analyze \"" + analyzed_layer_name + "\"." );
			return;
		}
		GeoLayer lv_layer = lv.getLayer();

		// Create a new layer to contain the analyzed data.  The layer needs shapes and an attribute table.

		GeoLayer layer = new GeoLayer(new PropList("Layer"));
		layer.setShapeType(GeoLayer.POINT);
		List shapes = new Vector();
		List lv_shapes = lv_layer.getShapes();
		DataTable lv_table = lv_layer.getAttributeTable();
		int lv_layer_size = 0;
		if ( lv_shapes != null ) {
			lv_layer_size = lv_shapes.size();
			Message.printStatus ( 2, routine, "There are " + lv_layer_size + " features to process.");
		}
		else {
		    Message.printWarning ( 1, routine, "No shapes in layer." );
			return;
		}
		GRShape shape = null;
		GRPoint pt;
		for ( int i = 0; i < lv_layer_size; i++ ) {
			// Copy the shape into the new layer...
			//Message.printStatus ( 2, routine,"Processing shape [" + i + "]" );
			shape = (GRShape)lv_shapes.get(i);
			if ( shape == null ) {
				// Null shape (ignore)...
				Message.printStatus ( 2, routine, "Shape [" + i + "] is null." );
				continue;
			}
			if ( shape.type == GRShape.UNKNOWN ) {
				continue;
			}
			//Message.printStatus ( 2, routine, "" + shape );
			pt = (GRPoint)shape;
			shapes.add ( new GRPoint(pt.x,pt.y) );
		}
		layer.setShapes ( shapes );

		// Create the attribute table...

		List fields = new Vector ();
		fields.add ( new TableField(	TableField.DATA_TYPE_STRING, "Abbrev", 12 ) ); // 0
		fields.add ( new TableField( TableField.DATA_TYPE_STRING, "Station_id", 12 ) ); // 1
		fields.add ( new TableField(	TableField.DATA_TYPE_STRING, "Name", 40 ) ); // 2
		fields.add ( new TableField(	TableField.DATA_TYPE_DOUBLE, "Flow_cfs", 12 ) ); // 3
		fields.add ( new TableField(	TableField.DATA_TYPE_STRING, "Date", 10 ) ); // 4
		fields.add ( new TableField(	TableField.DATA_TYPE_DOUBLE, "Average", 10 ) ); // 5
		fields.add ( new TableField(	TableField.DATA_TYPE_DOUBLE, "Perc. Ave", 14 ) ); //6
		fields.add ( new TableField(	TableField.DATA_TYPE_DOUBLE, "UTMX", 13 ) ); //7
		fields.add ( new TableField(	TableField.DATA_TYPE_DOUBLE, "UTMY", 13 ) ); //8
		int fields_size = fields.size();
		DataTable table = new DataTable ( fields );
		layer.setAttributeTable(table);

		// For each point in the layer, find the real-time time series
		// in memory and get its most recent value (for today).  Then
		// determine all values from the matching historical record and
		// compute an average flow.  Then calculate a percent of average
		// for the current value.  Add these items to the attribute table of the layer.

		if ( __tsProcessor == null ) {
			Message.printWarning ( 1, routine, "No time series available for test." );
			return;
		}
		List tslist = commandProcessor_GetTimeSeriesResultsList();
		int tslist_size = 0;
		if ( tslist != null ) {
			tslist_size = tslist.size();
		}
		for ( int i = 0; i < lv_layer_size; i++ ) {
			shape = (GRShape)lv_shapes.get(i);
			if ( (shape == null) || (shape.type == GRShape.UNKNOWN) ) {
				// Null shape so ignore...
				continue;
			}
			// Copy the shape into the new layer and add a record to the attribute table...
			pt = (GRPoint)lv_shapes.get(i);
			rec = new TableRecord ( fields_size );
			abbrev = (String)lv_table.getFieldValue(i,"Abbrev");
			rec.addFieldValue ( abbrev );
			station_id = (String)lv_table.getFieldValue(i,"Station_id");
			Message.printStatus ( 2, routine, "Processing station abbrev=\"" + abbrev +
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
			// First loop through the time series to find one that matches the station.

			for ( int j = 0; j < tslist_size; j++ ) {
				ts = (TS)tslist.get(j);
				tsident = ts.getIdentifier();
				if ( (tsident.getLocation().equalsIgnoreCase(abbrev) ||
					tsident.getLocation().equalsIgnoreCase(station_id)) &&
					tsident.getInterval().equalsIgnoreCase("Irregular") ) {
					Message.printStatus ( 2, routine, "Found matching real-time time series.");
					// have the matching station
					value = ts.getDataValue ( now );
					Message.printStatus ( 2, routine, "Data value on " + now + " is " +	value );
					rec.setFieldValue (3,new Double(value));
					rec.setFieldValue (4,today.toString() );
					// Find the historical time series if the current value is not missing...
					if ( !ts.isDataMissing(value) ) {
					for ( int k = 0; k < tslist_size; k++){
						ts2 = (TS)tslist.get(k);
						tsident2 = ts2.getIdentifier();
						if ( (tsident2.getLocation().equalsIgnoreCase(abbrev) ||
							tsident2.getLocation().equalsIgnoreCase(station_id)) &&
							tsident2.getInterval().equalsIgnoreCase("Day") ) {
							Message.printStatus ( 2, routine, "Found matching historical time series.");
							// Loop through entire period to extract daily values matching today's date.
							nvalues = 0;
							sum = 0.0;
							date = new DateTime (ts2.getDate1() );
							date.setMonth( today.getMonth() );
							date.setDay( today.getDay() );
							end = ts2.getDate2();
							for ( ; date.lessThanOrEqualTo(end); date.addYear(1)){
								value2 = ts2.getDataValue(date);
								if ( ts2.isDataMissing(value2) ) {
									continue;
								}
								++nvalues;
								sum += value2;
							}
				
							// Compute the average.
							// TODO - need some criteria for minimum number of points.
							if ( nvalues > 10 ) {
								mean = sum/nvalues;
								percent = value/mean*100.0;
								rec.setFieldValue ( 5,new Double(mean));
								rec.setFieldValue (	6,new Double(percent));
							}
							// No need to continue looking for the historical time series...
							break;
						}
					}
					}
					// No need to continue looking for the station...
					break;
				}
			} 
		}

		// Write the shapefile...

		layer.writeShapefile ( "F:\\home\\beware\\sam\\testflow", false, false, null );
		table.writeDelimitedFile ("F:\\home\\beware\\sam\\testflow.csv", ",", true, null );
	}
	else if ( test_num == 9 ) {
		// Read a DateValue file and test creating a graph.
		// as the data file.
		// import RTi.TS.DateValueTS;
		// import RTi.Util.IO.PropList;
		// import RTi.GRTS.TSViewJFrame;
		TS ts = DateValueTS.readTimeSeries (
		"K:\\projects\\XR053_ArcGIS Engine Evaluation\\TimeSeriesExample\\example.dv" );
		List tslist = new Vector(1);
		tslist.add ( ts );
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
	if ( (__input_type_JComboBox == null) || (__dataType_JComboBox == null) || (__timeStep_JComboBox == null) ) {
		// GUI still initializing...
		if ( Message.isDebugOn ) {
			Message.printDebug ( 1, rtn, "Time step has been selected but the GUI is not initialized." );
		}
		return;
	}
	String selectedTimeStep = ui_GetSelectedTimeStep();
	if ( selectedTimeStep == null ) {
		// Apparently this happens when setData() or similar is called
		// on the JComboBox, and before select() is called.
		if ( Message.isDebugOn ) {
			Message.printDebug ( 1, rtn, "Time step has been selected:  null (select is ignored)" );
		}
		return;
	}
	if ( Message.isDebugOn ) {
		Message.printStatus ( 2, rtn, "Time step has been selected:  \"" + selectedTimeStep + "\"" );
	}
	// Show the input filters that are appropriate for data time and timestep choices
	ui_SetInputFilters();
}

/**
Transfer all the time series from the query results to the command List.
*/
private void uiAction_TransferAllQueryResultsToCommandList()
{	String routine = "TSTool_JFrame.uiAction_TransferAllQueryResultsToCommandList";

	int nrows = __query_TableModel.getRowCount();
	Message.printStatus ( 1, routine, "Transferring all time series to commands (" + nrows + " in list)..." );
	JGUIUtil.setWaitCursor ( this, true );
	int iend = nrows - 1;
	ui_SetIgnoreListSelectionEvent ( true ); // To increase performance during transfer...
	ui_SetIgnoreItemEvent ( true );	// To increase performance
	int numInserted = 0;
	for ( int i = 0; i < nrows; i++ ) {
		// Only force the GUI state to be updated if the last item.
		if ( i == iend ) {
			ui_SetIgnoreListSelectionEvent ( false );
			__ignoreItemEvent = false;
			numInserted += queryResultsList_TransferOneTSFromQueryResultsListToCommandList ( i, true, numInserted );
		}
		else {
		    numInserted += queryResultsList_TransferOneTSFromQueryResultsListToCommandList ( i, false, numInserted );
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
{	String routine = "TSTool_JFrame.uiAction_TransferSelectedQueryResultsToCommandList";

	int nrows = __query_JWorksheet.getSelectedRowCount();
	Message.printStatus ( 1, routine, "Transferring selected time series to commands (" + nrows + " in list)..." );
	int [] selected = __query_JWorksheet.getSelectedRows();
	int iend = nrows - 1;
	int numInserted = 0;
	for ( int i = 0; i < nrows; i++ ) {
		// Only update the GUI state if transferring the last command...
		if ( i == iend ) {
			numInserted += queryResultsList_TransferOneTSFromQueryResultsListToCommandList (
			    selected[i], true, numInserted ); 
		}
		else {
		    numInserted += queryResultsList_TransferOneTSFromQueryResultsListToCommandList (
		        selected[i], false, numInserted ); 
		}
	}
	Message.printStatus ( 1, routine, "Transferred selected time series." );
}

/**
View the documentation by displaying using the desktop application.
*/
private void uiAction_ViewDocumentation ()
{   String routine = getClass().getName() + ".uiAction_ViewDocumentation";
    // The location of the documentation is relative to the application home
    String docFileName = IOUtil.getApplicationHomeDir() + "/doc/UserManual/TSTool.pdf";
    // Convert for the operating system
    docFileName = IOUtil.verifyPathForOS(docFileName, true);
    // Now display using the default application for the file extension
    Message.printStatus(2, routine, "Opening documentation \"" + docFileName + "\"" );
    try {
        Desktop desktop = Desktop.getDesktop();
        desktop.open ( new File(docFileName) );
    }
    catch ( Exception e ) {
        Message.printWarning(1, "", "Unable to display documentation at \"" + docFileName + "\" (" + e + ")." );
    }
}

/**
View the training materials by displaying in file browser.
*/
private void uiAction_ViewTrainingMaterials ()
{   String routine = getClass().getName() + ".uiAction_ViewTrainingMaterials";
    // The location of the documentation is relative to the application home
    String trainingFolderName = IOUtil.getApplicationHomeDir() + "/doc/Training";
    // Convert for the operating system
    trainingFolderName = IOUtil.verifyPathForOS(trainingFolderName, true);
    // Now display using the default application for the file extension
    Message.printStatus(2, routine, "Opening training material folder \"" + trainingFolderName + "\"" );
    try {
        Desktop desktop = Desktop.getDesktop();
        desktop.open ( new File(trainingFolderName) );
    }
    catch ( Exception e ) {
        Message.printWarning(1, "", "Unable to display training materials at \"" +
            trainingFolderName + "\" (" + e + ")." );
    }
}

/**
Write the current command file list (all lines, whether selected or not) to
the specified file.  Do not prompt for header comments (and do not add).
@param promptForFile If true, prompt for the file name rather than using the
value that is passed.  An extension of .TSTool is enforced.
@param file Command file to write.
@param saveVersion9 whether to save the file considering the software version (this is generally
only needed when pre-10 TSTool version is saved, which will retain "TS Alias = " command notation
*/
private void uiAction_WriteCommandFile ( String file, boolean promptForFile, boolean saveVersion9 )
{	String routine = "TSTool_JFrame.uiAction_WriteCommandFile";
    String directory = null;
	if ( promptForFile ) {
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
		Command command;
		for (int i = 0; i < size; i++) {
		    command = (Command)__commands_JListModel.get(i);
		    if ( saveVersion9 && command instanceof CommandSavesMultipleVersions ) {
		        // TODO SAM This is a work-around to transition from the "TS Alias = " notation to
		        // Command(Alias=...), which was introduced in version 10
		        CommandSavesMultipleVersions versionCommand = (CommandSavesMultipleVersions)command;
		        out.println(versionCommand.toString(command.getCommandParameters(),9));
		    }
		    else {
		        out.println(command.toString());
		    }
		}
	
		out.close();
		commandList_SetDirty(false);
		commandList_SetCommandFileName ( file );

		if ( directory != null ) {
			// Set the "WorkingDir" property, which will NOT contain a trailing separator...
			IOUtil.setProgramWorkingDir(directory);
			ui_SetDir_LastCommandFileOpened(directory);
			__props.set ("WorkingDir=" + IOUtil.getProgramWorkingDir());
			ui_SetInitialWorkingDir (__props.getValue("WorkingDir"));
		}
	}
	catch ( Exception e ) {
		Message.printWarning (1, routine, "Error writing command file \"" + file + "\" (" + e + ").");
		// Leave the dirty flag the previous value.
	}
	// Update the status information...
	ui_UpdateStatus ( false );
}

/**
Handle ListSelectionListener events.
*/
public void valueChanged ( ListSelectionEvent e )
{	// e.getSource() apparently does not return __commands_JList - it must
	// return a different component so don't check the object address...
	if ( ui_GetIgnoreListSelectionEvent() ) {
		return;
	}
    Object component = e.getSource();
    if ( component == __resultsTSEnsembles_JList ) {
        //Message.printStatus(2, "", "Ensemble selected in list");
    }
    else if ( component == __resultsOutputFiles_JList ) {
        if ( !e.getValueIsAdjusting() ) {
            // User is done adjusting selection so do the display...
            ListSelectionModel lsm = __resultsOutputFiles_JList.getSelectionModel();
            int minIndex = lsm.getMinSelectionIndex();
            int maxIndex = lsm.getMaxSelectionIndex();
            for (int i = minIndex; i <= maxIndex; i++) {
                if (lsm.isSelectedIndex(i)) {
                    uiAction_ShowResultsOutputFile( (String)__resultsOutputFiles_JListModel.elementAt(i) );
                }
            }
        }
    }
    else if ( component == __resultsTables_JList ) {
        if ( !e.getValueIsAdjusting() ) {
            // User is done adjusting selection so do the display...
            ListSelectionModel lsm = __resultsTables_JList.getSelectionModel();
            int minIndex = lsm.getMinSelectionIndex();
            int maxIndex = lsm.getMaxSelectionIndex();
            for (int i = minIndex; i <= maxIndex; i++) {
                if (lsm.isSelectedIndex(i)) {
                    uiAction_ShowResultsTable( (String)__resultsTables_JListModel.elementAt(i) );
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
    Handle a group of actions for the ensemble popup menu.
    @param event Event to handle.
    */
    public void actionPerformed (ActionEvent event)
    {   String command = event.getActionCommand();

        if ( command.equals(__Results_Ensemble_Graph_Line_String) ) {
            uiAction_GraphEnsembleResults("-olinegraph");
        }
        else if ( command.equals(__Results_Ensemble_Table_String) ) {
            uiAction_GraphEnsembleResults("-otable");
        }
        else if ( command.equals(__Results_Ensemble_Properties_String) ) {
            uiAction_ResultsEnsembleProperties();
        }
    }
}

}
