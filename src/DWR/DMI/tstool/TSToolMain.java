package DWR.DMI.tstool;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.awt.Frame;

import javax.swing.JApplet;
import javax.swing.JFrame;

import org.restlet.data.Parameter;

import com.sun.net.httpserver.HttpServer;

import riverside.datastore.DataStore;
import riverside.datastore.DataStoreConnectionUIProvider;
import riverside.datastore.DataStoreFactory;
import rti.app.tstoolrestlet.TSToolServer;
import rti.tscommandprocessor.core.TSCommandFileRunner;
import rti.tscommandprocessor.core.TSCommandProcessor;
import DWR.DMI.HydroBaseDMI.HydroBaseDMI;
import DWR.DMI.HydroBaseDMI.HydroBase_Util;
import RTi.DMI.RiversideDB_DMI.RiversideDB_DMI;
import RTi.DMI.RiversideDB_DMI.RiversideDBDataStore;
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
import RTi.Util.Time.StopWatch;

/**
Main (application startup) class for TSTool.  This class will start the TSTool GUI
or run the TSCommandProcessor in batch mode with a command file.  The methods in
this file are called by the startup TSTool and CDSS versions of TSTool.
*/
public class TSToolMain extends JApplet
{
public static final String PROGRAM_NAME = "TSTool";
public static final String PROGRAM_VERSION = "11.12.04beta (2016-09-17)";

/**
Main GUI instance, used when running interactively.
*/
private static TSTool_JFrame __tstool_JFrame;

/**
Home directory for system install.
As of 2016-02-18 this is NOT used for default log file and datastore configuration.
*/
private static String __tstoolInstallHome = null;

/**
Path to the batch server hot folder.
*/
private static String __batchServerHotFolder = "";

/**
Timeout when running in batch mode.  TSTool will exit if processing has not finished
(usually because of web service hang-up, etc.)
*/
private static int __batchTimeoutSeconds = 0;

/**
Path to the configuration file.  This cannot be defaulted until the -home command line parameter is processed.
*/
private static String __configFile = "";

/**
List of properties to control the software from the configuration file and passed in on the command line.
*/
private static PropList __tstool_props = null;

/**
Indicates whether TSTool is running in batch server mode (look for command files in hot folder).
*/
private static boolean __isBatchServer = false;

/**
Indicates whether TSTool is running in HTTP server mode (requires command files to match REST endpoints and URL parmeters will translate to ${Property}).
*/
private static boolean __isHttpServer = false;

/**
Indicates whether TSTool is running in server mode using restlet.
*/
private static boolean __isRestletServer = false;

/**
Log file from the command line.  Parent folder must exist to create.
*/
private static String __logFileFromCommandLine = null;

/**
Indicates whether the command file should run after loading, when used in GUI mode.
*/
private static boolean __run_commands_on_load = false;

/**
Indicates whether commands should have discovery run when commands are loaded.
Running discovery is a performance hit, especially for very large command files that are generated from
templates, and is generally unnecessary in batch mode.
*/
private static boolean __runDiscoveryOnLoad = true;

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
Return the batch server hot folder.
@return the batch server hot folder
*/
public static String getBatchServerHotFolder()
{	return __batchServerHotFolder;
}

/**
Return the command file that is being processed, or null if not being run in batch mode.
@return the path to the command file to run.
*/
private static int getBatchTimeout ()
{
	return __batchTimeoutSeconds;
}

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
public static String getConfigFile ()
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
@param propertyExp name of property to look up as a Java regular expression.
@return the value(s) for a TSTool configuration property, or null if a properties file does not exist.
Return null if the property is not found (or if no configuration file exists for TSTool).
*/
public static List<Prop> getProps ( String propertyExp )
{   if ( __tstool_props == null ) {
        return null;
    }
    return __tstool_props.getPropsMatchingRegExp(propertyExp);
}

/**
Return the value of a TSTool property.  The properties are defined in the TSTool configuration file.
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

	TSToolSession session = new TSToolSession();
	IOUtil.setApplet ( this );
	IOUtil.setProgramData ( PROGRAM_NAME, PROGRAM_VERSION, null );
	// Set up handler for GUI event queue, for exceptions that may otherwise get swallowed by a JRE launcher
    new MessageEventQueue();
    try {
        parseArgs( session, this );
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
	String commandFile = null;
	List<Class> pluginDataStoreClasses = new ArrayList<Class>();
	List<Class> pluginDataStoreFactoryClasses = new ArrayList<Class>();
	List<Class> pluginCommandClasses = new ArrayList<Class>();
	__tstool_JFrame = new TSTool_JFrame ( session, commandFile, false,
		pluginDataStoreClasses, pluginDataStoreFactoryClasses, pluginCommandClasses );
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

	String units_file = __tstoolInstallHome + File.separator + "system" + File.separator + "DATAUNIT";

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
Indicate whether TSTool is running in batch server mode.  This feature is under development.
@return true if running in batch server mode.
*/
public static boolean isBatchServer()
{	return __isBatchServer;
}

/**
Indicate whether TSTool is running in HTTP server mode.  This feature is under development.
@return true if running in server mode.
*/
public static boolean isHttpServer()
{	return __isHttpServer;
}

/**
Indicate whether TSTool is running in REST API server mode.  This feature is under development.
@return true if running in server mode.
*/
public static boolean isRestServer()
{	return __isRestletServer;
}

/**
Load plugin datastores.
@param session TSTool session, which provides user and environment information.
*/
private static List<Class>[] loadPluginDataStores(TSToolSession session)
{
	final String routine = "loadPluginDataStores";
	List<Class> pluginDataStoreList = new ArrayList<Class>();
	List<Class> pluginDataStoreFactoryList = new ArrayList<Class>();
	// First get a list of candidate URLs, which will be in the TSTool user files under, for example
	// .tstool/plugin-datastore/DatastoreName/bin/Datastore-version.jar
	String userPluginDir = session.getUserFolder() + File.separator + "plugin-datastore";
	// TODO SAM 2016-04-03 it may be desirable to include sub-folders under bin for third-party software
	// in which case ** will need to be used somehow in the glob pattern
	String glob = userPluginDir + File.separator + "*" + File.separator + "bin" + File.separator + "*.jar";
	// For glob backslashes need to be escaped (this should not impact Linux)
	glob = glob.replace ("\\","\\\\");
	final List<String> pluginJarList = new ArrayList<String>();
	final PathMatcher pathMatcher = FileSystems.getDefault().getPathMatcher("glob:"+glob);
	FileVisitor<Path> matcherVisitor = new SimpleFileVisitor<Path>() {
		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attribs) throws IOException {
			Message.printStatus(2,routine, "Checking path \"" + file + "\" for match" );
			if ( pathMatcher.matches(file)) {
				Message.printStatus(2,routine,"Found jar file for plugin datastore: " + file);
				pluginJarList.add(""+file);
			}
			return FileVisitResult.CONTINUE;
		}
		
		@Override
		public FileVisitResult visitFileFailed(Path file,IOException exc) throws IOException {
			return FileVisitResult.CONTINUE;
		}
	};
	try {
		// The following walks the tree path under the specified folder
		// The full path is returned during walking (not just under the starting folder)
		Path userPluginDirPath = Paths.get(userPluginDir);
		Message.printStatus(2,routine, "Trying to find plugin datastores using userDirPath \"" + userPluginDirPath + "\" and glob pattern \"" + glob + "\"" );
		Files.walkFileTree(userPluginDirPath, matcherVisitor);
	}
	catch ( IOException e ) {
		Message.printWarning(3,routine,"Error getting jar file list for plugin datastore(s) (" + e + ")" );
		// Return empty list of datastore plugin classes
		List<Class> [] array = new List[2];
		array[0] = pluginDataStoreList;
		array[1] = pluginDataStoreFactoryList;
		return array;
	}
	// Convert found jar files into an array of URL used by the class loader
	URL [] dataStoreJarURLs = new URL[pluginJarList.size()];
	int jarCount = 0;
	for ( String pluginJar : pluginJarList ) {
		try {
			// Convert the file system filename to URL using forward slashes
			dataStoreJarURLs[jarCount] = new URL("file:///" + pluginJar.replace("\\", "/"));
			++jarCount; // Only increment if successful
		}
		catch ( MalformedURLException e ) {
			Message.printWarning(3,routine,"Error creating URL for datastore plugin jar file \"" + pluginJar + "\" (" + e + ")" );
		}
	}
	// Create a class loader for datastores.  This expects datastores to be in the jar file with class name XXXXXDataStore.
	if ( jarCount != pluginJarList.size() ) {
		// Resize the array
		URL [] dataStoreJarURLs2 = new URL[jarCount];
		System.arraycopy(dataStoreJarURLs,0,dataStoreJarURLs2,0,jarCount);
		dataStoreJarURLs = dataStoreJarURLs2;
	}
	PluginDataStoreClassLoader pcl = new PluginDataStoreClassLoader ( dataStoreJarURLs );
	try {
		pluginDataStoreList = pcl.loadDataStoreClasses();
	}
	catch ( ClassNotFoundException e ) {
		Message.printWarning(2,routine,"Error loading datastore plugin classes (" + e + ")." );
		Message.printWarning(2,routine,e);
	}
	try {
		pluginDataStoreFactoryList = pcl.loadDataStoreFactoryClasses();
	}
	catch ( ClassNotFoundException e ) {
		Message.printWarning(2,routine,"Error loading datastore factory plugin classes (" + e + ")." );
		Message.printWarning(2,routine,e);
	}
	finally {
		/* FIXME SAM 2016-04-03 Try not closing class loader because it is needed for other classes in the plugin
		 * The compiler may show as a warning as a memory leak but it needs to be around throughout the runtime
		try {
			pcl.close();
		}
		catch ( IOException e ) {
			// For now swallow - not sure what else to do
		}
		*/
	}
	List<Class> [] array = new List[2];
	array[0] = pluginDataStoreList;
	array[1] = pluginDataStoreFactoryList;
	return array;
}

/**
Load plugin commands.
@param session TSTool session, which provides user and environment information.
*/
private static List<Class> loadPluginCommands(TSToolSession session)
{	final String routine = "loadPluginCommands";
	List<Class> pluginCommandList = new ArrayList<Class>();
	// First get a list of candidate URLs, which will be in the TSTool user files under, for example:
	// .tstool/plugin-command/CommandName/bin/CommandName-version.jar
	// See:  http://stackoverflow.com/questions/9148528/how-do-i-use-directory-globbing-in-jdk7
	String userPluginDir = session.getUserFolder() + File.separator + "plugin-command";
	// TODO SAM 2016-04-03 it may be desirable to include sub-folders under bin for third-party software
	// in which case ** will need to be used somehow in the glob pattern
	String glob = userPluginDir + File.separator + "*" + File.separator + "bin" + File.separator + "*.jar";
	// For glob backslashes need to be escaped (this should not impact Linux)
	glob = glob.replace ("\\","\\\\");
	final List<String> pluginJarList = new ArrayList<String>();
	final PathMatcher pathMatcher = FileSystems.getDefault().getPathMatcher("glob:"+glob);
	FileVisitor<Path> matcherVisitor = new SimpleFileVisitor<Path>() {
		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attribs) throws IOException {
			Message.printStatus(2,routine, "Checking path \"" + file + "\" for match" );
			if ( pathMatcher.matches(file)) {
				Message.printStatus(2,routine,"Found jar file for plugin command: " + file);
				pluginJarList.add(""+file);
			}
			return FileVisitResult.CONTINUE;
		}
		
		@Override
		public FileVisitResult visitFileFailed(Path file,IOException exc) throws IOException {
			return FileVisitResult.CONTINUE;
		}
	};
	try {
		// The following walks the tree path under the specified folder
		// The full path is returned during walking (not just under the starting folder)
		Path userPluginDirPath = Paths.get(userPluginDir);
		Message.printStatus(2,routine, "Trying to find plugin commands using userDirPath \"" + userPluginDirPath + "\" and glob pattern \"" + glob + "\"" );
		Files.walkFileTree(userPluginDirPath, matcherVisitor);
	}
	catch ( IOException e ) {
		Message.printWarning(3,routine,"Error getting jar file list for plugin command(s) (" + e + ")" );
		// Return empty list of command plugin classes
		return pluginCommandList;
	}
	// Convert found jar files into an array of URL used by the class loader
	URL [] commandJarURLs = new URL[pluginJarList.size()];
	int jarCount = 0;
	for ( String pluginJar : pluginJarList ) {
		try {
			// Convert the file system filename to URL using forward slashes
			commandJarURLs[jarCount] = new URL("file:///" + pluginJar.replace("\\", "/"));
			++jarCount; // Only increment if successful
		}
		catch ( MalformedURLException e ) {
			Message.printWarning(3,routine,"Error creating URL for plugin command jar file \"" + pluginJar + "\" (" + e + ")" );
		}
	}
	// Create a class loader for commands.  This expects commands to be in the jar file with class name CommandName.
	if ( jarCount != pluginJarList.size() ) {
		// Resize the array
		URL [] commandJarURLs2 = new URL[jarCount];
		System.arraycopy(commandJarURLs,0,commandJarURLs2,0,jarCount);
		commandJarURLs = commandJarURLs2;
	}
	PluginCommandClassLoader pcl = new PluginCommandClassLoader ( commandJarURLs );
	try {
		pluginCommandList = pcl.loadCommandClasses();
	}
	catch ( ClassNotFoundException e ) {
		Message.printWarning(2,routine,"Error loading command plugin classes (" + e + ")." );
		Message.printWarning(2,routine,e);
	}
	finally {
		/* FIXME SAM 2016-04-03 Try not closing class loader because it is needed for other classes in the plugin
		 * The compiler may show as a warning as a memory leak but it needs to be around throughout the runtime
		try {
			pcl.close();
		}
		catch ( IOException e ) {
			// For now swallow - not sure what else to do
		}
		*/
	}
	return pluginCommandList;
}

/**
Start the main application instance.
@param args Command line arguments.
*/
public static void main ( String args[] )
{	String routine = "TSToolMain.main";

	try {
	// Main try...

	TSToolSession session = new TSToolSession();
	initializeLoggingLevelsBeforeLogOpened();
	setWorkingDirInitial ();
	IOUtil.setProgramData ( PROGRAM_NAME, PROGRAM_VERSION, args );
	JGUIUtil.setAppNameForWindows("TSTool");
	
	// Set up handler for GUI event queue, for exceptions that may otherwise get swallowed by a JRE launcher
	new MessageEventQueue();

	// Note that messages will not be printed to the log file until the log file is opened below.

	initializeLoggingLevelsAfterLogOpened();

	try {
        parseArgs ( session, args );
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

	Message.printStatus( 2, routine, "isBatch=" + IOUtil.isBatch() + " -nomaingui specified = " + __noMainGUIArgSpecified );
	if ( !IOUtil.isBatch() || __noMainGUIArgSpecified || !isBatchServer() ) {
	    // Not "pure" batch so need to have the icon initialized
	    try {
	        setIcon ( "CDSS" );
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

	Message.printStatus ( 1, routine, "Setup completed.  showmain = " + __showMainGUI + " isbatch=" + IOUtil.isBatch() );

	// Load plugin datastore classes
	
	List<Class>[] array = loadPluginDataStores(session);
	List<Class> pluginDataStoreClasses = array[0];
	List<Class> pluginDataStoreFactoryClasses = array[1];
	
	// Load plugin command classes
	
	List<Class> pluginCommandClasses = loadPluginCommands(session);
	
	// Run TSTool in the run mode indicated by command line parameters
	
	if ( IOUtil.isBatch() ) {
		// Running like "tstool -commands file" (possibly with -nomaingui)
		TSCommandFileRunner runner = new TSCommandFileRunner();
		// If the global timeout is set, start a thread that will time out when the batch run is complete.
		startTimeoutThread ( getBatchTimeout());
	    // Open the HydroBase connection if the configuration file specifies the information.  Do this before
		// reading the command file because commands may try to run discovery during load.
        openHydroBase ( runner.getProcessor() );
        // Open datastores in a generic way if the configuration file specifies the information.  Do this before
        // reading the command file because commands may try to run discovery during load.
        openDataStoresAtStartup ( session, runner.getProcessor(), pluginDataStoreClasses, pluginDataStoreFactoryClasses, true );
		try {
		    String commandFileFull = getCommandFile();
		    Message.printStatus( 1, routine, "Running command file in batch mode:  \"" + commandFileFull + "\"" );
			runner.readCommandFile ( commandFileFull, __runDiscoveryOnLoad );
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
	else if ( isBatchServer() ) {
		String batchServerHotFolder0 = getBatchServerHotFolder();
		Message.printStatus ( 1, routine, "Starting in batch server mode with hot folder \"" + batchServerHotFolder0 + "\"" );
		// TODO SAM 2016-02-09 For now keep code here but may make more modular
		// Create a runner that will be re-used
		if ( batchServerHotFolder0.isEmpty() ) {
			Message.printWarning ( 1, routine, "No batch server hot folder specified - use -batchServerHotFolder command line parameter." );
			quitProgram ( 1 );
		}
		File batchServerHotFolder = new File(batchServerHotFolder0);
		if ( !batchServerHotFolder.exists() ) {
			Message.printWarning ( 1, routine, "Batch server hot folder \"" + batchServerHotFolder + "\" does not exist." );
			quitProgram ( 1 );
		}
		if ( !batchServerHotFolder.canRead() ) {
			Message.printWarning ( 1, routine, "Can't read batch server hot folder \"" + batchServerHotFolder + "\"." );
			quitProgram ( 1 );
		}
		TSCommandFileRunner runner = new TSCommandFileRunner();
		// Open the HydroBase connection if the configuration file specifies the information.  Do this before
		// reading the command file because commands may try to run discovery during load.
        openHydroBase ( runner.getProcessor() );
        // Open datastores in a generic way if the configuration file specifies the information.  Do this before
        // reading the command file because commands may try to run discovery during load.
        openDataStoresAtStartup ( session, runner.getProcessor(), pluginDataStoreClasses, pluginDataStoreFactoryClasses, true );
        File f = null;
		String commandFileFull = "";
	    boolean runDiscoveryOnLoad = false;
	    boolean doStop = false;
	    long sleep = 50;
        while ( true ) {
        	Thread.sleep(sleep); // Do this to keep the loop from eating up a lot of CPU
        	// Wait for configured wait time between processing
        	// Look for files in the batch server hot folder
        	List<File> files = IOUtil.getFilesMatchingPattern(batchServerHotFolder0, "*", true);
        	//Message.printStatus(1,routine,"Have " + files.size() + " files in hot folder.");
        	// TODO SAM 2016-02-08 need to sort so oldest file processed first.
        	// Also perhaps need to check for a command like "End()" to make sure file is complete from copy into hot folder
        	for ( int i = 0; i < files.size(); i++ ) {
        		f = files.get(i);
        		// Make sure the file exists and is readable
        		Message.printStatus(1,routine,"Processing command file \"" + f.getAbsolutePath() + "\".");
        		if ( f.exists() && f.canRead() ) {
    				commandFileFull = f.getAbsolutePath();
        			String filename = f.getName();
        			// Special actions based on command file name
        			if ( filename.equalsIgnoreCase("stop") ) {
        				doStop = true;
        				break;
        			}
        			if ( !commandFileFull.toUpperCase().endsWith(".TSTOOL") ) {
        				// Not a command file so don't process
        				continue;
        			}
        			// Open the command file...
        			try {
        			    Message.printStatus( 1, routine, "Running command file in batch server mode:  \"" + commandFileFull + "\"" );
        				runner.readCommandFile ( commandFileFull, runDiscoveryOnLoad );
        			}
        			catch ( Exception e ) {
        				Message.printWarning ( 1, routine, "Error reading command file \"" + commandFileFull + "\".  Unable to run commands." );
        				Message.printWarning ( 1, routine, e );
        				continue;
        			}
        			// Run the command file..
        			try {
        			    // The following will throw an exception if there are any errors running.
        	            runner.runCommands();
        			}
        			catch ( Exception e ) {
        				Message.printWarning ( 1, routine, "Error running command file \"" + commandFileFull + "\"." );
        				Message.printWarning ( 1, routine, e );
        			}
        			// Remove the command file
        			f.delete();
        		}
        	}
        	if ( doStop ) {
        		// TODO SAM 2016-02-08 is it necessary to deal with windows/frames?
        		Message.printStatus ( 1, routine, "Exiting batch server because file named \"stop\" was found in hot folder." );
        		f.delete();
        		quitProgram ( 0 );
        	}
        }
	}
	else if ( isHttpServer() ) {
		// See:  http://stackoverflow.com/questions/3732109/simple-http-server-in-java-using-only-java-se-api
		// Do something simple for now to test
		int port = 8000;
		HttpServer server = HttpServer.create(new InetSocketAddress(port),0);
		String root = "/tstool";
		server.createContext(root, new UrlHandler());
		server.setExecutor(null);
		server.start();
	}
	else if ( isRestServer() ) {
		// Run in server mode using REST API
		runRestletServer();
	}
	else {
		// Run the GUI...
		Message.printStatus ( 2, routine, "Starting TSTool GUI..." );
		try {
            __tstool_JFrame = new TSTool_JFrame ( session, getCommandFile(), getRunOnLoad(),
            	pluginDataStoreClasses, pluginDataStoreFactoryClasses, pluginCommandClasses );
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
Open a datastore given its configuration properties.  The datastore is also added to the processor
(if the open fails then the datastore should set status=1).
The TSTool configuration file properties are checked here to ensure that the datastore type is enabled.
Otherwise, opening datastores takes time and impacts performance.
@param session TSTool session, which provides user and environment information
@param dataStoreProps datastore configuration properties recognized by the datastore factory "create" method.
@param processor time series command processor that will use/manage the datastore
@param pluginDataStoreClassList list of plugin datastore classes to be loaded dynamically
@param isBatch indicate whether running in batch mode - if true, do not open datastores with login of "prompt"
*/
protected static DataStore openDataStore ( TSToolSession session, PropList dataStoreProps,
	TSCommandProcessor processor, List<Class> pluginDataStoreClassList, List<Class> pluginDataStoreFactoryClassList, boolean isBatch )
throws ClassNotFoundException, IllegalAccessException, InstantiationException, Exception
{   String routine = "TSToolMain.openDataStore";
    // Open the datastore depending on the type
    String dataStoreType = dataStoreProps.getValue("Type");
    String dataStoreConfigFile = dataStoreProps.getValue("DataStoreConfigFile");
    Message.printStatus(2,routine,"DataStoreConfigFile="+dataStoreConfigFile);
    // For now hard-code this here
    // TODO SAM 2010-09-01 Make this more elegant
    String packagePath = ""; // Make sure to include trailing period below
    // TODO SAM 2016-03-25 Need to figure out how software feature set can be generically disabled without coding name here
    // Similar checks are done in the TSTool UI to enable/disable UI features
    String propValue = null; // From software installation configuration
    String userPropValue = null; // From user configuration
    Class pluginDataStoreClass = null; // Will be used if a plugin
    Class pluginDataStoreFactoryClass = null; // Will be used if a plugin
    if ( dataStoreType.equalsIgnoreCase("ColoradoWaterHBGuestDataStore") ) {
        propValue = getPropValue("TSTool.ColoradoWaterHBGuestEnabled");
    	userPropValue = session.getConfigPropValue ( "ColoradoWaterHBGuestEnabled" );
    	if ( (userPropValue != null) && !userPropValue.isEmpty() ) {
    		propValue = userPropValue;
    	}
        if ( (propValue != null) && propValue.equalsIgnoreCase("True") ) {
            packagePath = "us.co.state.dwr.hbguest.datastore.";
        }
    }
    else if ( dataStoreType.equalsIgnoreCase("ColoradoWaterSMSDataStore") ) {
        propValue = getPropValue("TSTool.ColoradoWaterSMSEnabled");
    	userPropValue = session.getConfigPropValue ( "ColoradoWaterSMSEnabled" );
    	if ( (userPropValue != null) && !userPropValue.isEmpty() ) {
    		propValue = userPropValue;
    	}
        if ( (propValue != null) && propValue.equalsIgnoreCase("True") ) {
            packagePath = "us.co.state.dwr.sms.datastore.";
        }
    }
    else if ( dataStoreType.equalsIgnoreCase("GenericDatabaseDataStore") ) {
        // No need to check whether enabled or not since a generic connection
        // Specific configuration files will indicate if enabled
    	// TODO SAM 2016-02-19 Need to move to more appropriate path - don't confuse with RiversideDBDataStore
        packagePath = "riverside.datastore.";
    }
    else if ( dataStoreType.equalsIgnoreCase("HydroBaseDataStore") ) {
        propValue = getPropValue("TSTool.HydroBaseEnabled");
    	userPropValue = session.getConfigPropValue ( "HydroBaseEnabled" );
    	if ( (userPropValue != null) && !userPropValue.isEmpty() ) {
    		propValue = userPropValue;
    	}
        if ( (propValue != null) && propValue.equalsIgnoreCase("True") ) {
            packagePath = "DWR.DMI.HydroBaseDMI.";
        }
    }
    else if ( dataStoreType.equalsIgnoreCase("NrcsAwdbDataStore") ) {
        propValue = getPropValue("TSTool.NrcsAwdbEnabled");
    	userPropValue = session.getConfigPropValue ( "NrcsAwdbEnabled" );
    	if ( (userPropValue != null) && !userPropValue.isEmpty() ) {
    		propValue = userPropValue;
    	}
        if ( (propValue != null) && propValue.equalsIgnoreCase("True") ) {
            packagePath = "rti.tscommandprocessor.commands.nrcs.awdb.";
        }
    }
    else if ( dataStoreType.equalsIgnoreCase("RccAcisDataStore") ) {
        propValue = getPropValue("TSTool.RCCACISEnabled");
    	userPropValue = session.getConfigPropValue ( "RCCACISEnabled" );
    	if ( (userPropValue != null) && !userPropValue.isEmpty() ) {
    		propValue = userPropValue;
    	}
        if ( (propValue != null) && propValue.equalsIgnoreCase("True") ) {
            packagePath = "rti.tscommandprocessor.commands.rccacis.";
        }
    }
    else if ( dataStoreType.equalsIgnoreCase("ReclamationHDBDataStore") ) {
        propValue = getPropValue("TSTool.ReclamationHDBEnabled");
    	userPropValue = session.getConfigPropValue ( "ReclamationHDBEnabled" );
    	if ( (userPropValue != null) && !userPropValue.isEmpty() ) {
    		propValue = userPropValue;
    	}
        if ( (propValue != null) && propValue.equalsIgnoreCase("True") ) {
            packagePath = "rti.tscommandprocessor.commands.reclamationhdb.";
        }
    }
    else if ( dataStoreType.equalsIgnoreCase("ReclamationPiscesDataStore") ) {
        propValue = getPropValue("TSTool.ReclamationPiscesEnabled");
    	userPropValue = session.getConfigPropValue ( "ReclamationPiscesEnabled" );
    	if ( (userPropValue != null) && !userPropValue.isEmpty() ) {
    		propValue = userPropValue;
    	}
        if ( (propValue != null) && propValue.equalsIgnoreCase("True") ) {
            packagePath = "rti.tscommandprocessor.commands.reclamationpisces.";
        }
    }
    else if ( dataStoreType.equalsIgnoreCase("RiversideDBDataStore") ) {
        propValue = getPropValue("TSTool.RiversideDBEnabled");
    	userPropValue = session.getConfigPropValue ( "RiversideDBEnabled" );
    	if ( (userPropValue != null) && !userPropValue.isEmpty() ) {
    		propValue = userPropValue;
    	}
        if ( (propValue != null) && propValue.equalsIgnoreCase("True") ) {
            packagePath = "RTi.DMI.RiversideDB_DMI.";
        }
    }
    else if ( dataStoreType.equalsIgnoreCase("UsgsNwisDailyDataStore") ) {
        propValue = getPropValue("TSTool.UsgsNwisDailyEnabled");
    	userPropValue = session.getConfigPropValue ( "UsgsNwisDailyDataStore" );
    	if ( (userPropValue != null) && !userPropValue.isEmpty() ) {
    		propValue = userPropValue;
    	}
        if ( (propValue != null) && propValue.equalsIgnoreCase("True") ) {
            packagePath = "rti.tscommandprocessor.commands.usgs.nwis.daily.";
        }
    }
    else if ( dataStoreType.equalsIgnoreCase("UsgsNwisGroundwaterDataStore") ) {
        propValue = getPropValue("TSTool.UsgsNwisGroundwaterEnabled");
    	userPropValue = session.getConfigPropValue ( "UsgsNwisGroundwaterEnabled" );
    	if ( (userPropValue != null) && !userPropValue.isEmpty() ) {
    		propValue = userPropValue;
    	}
        if ( (propValue != null) && propValue.equalsIgnoreCase("True") ) {
            packagePath = "rti.tscommandprocessor.commands.usgs.nwis.groundwater.";
        }
    }
    else if ( dataStoreType.equalsIgnoreCase("UsgsNwisInstantaneousDataStore") ) {
        propValue = getPropValue("TSTool.UsgsNwisInstantaneousEnabled");
    	userPropValue = session.getConfigPropValue ( "UsgsNwisInstantaneousEnabled" );
    	if ( (userPropValue != null) && !userPropValue.isEmpty() ) {
    		propValue = userPropValue;
    	}
        if ( (propValue != null) && propValue.equalsIgnoreCase("True") ) {
            packagePath = "rti.tscommandprocessor.commands.usgs.nwis.instantaneous.";
        }
    }
    else if ( dataStoreType.equalsIgnoreCase("WaterOneFlowDataStore") ) {
        propValue = getPropValue("TSTool.WaterOneFlowEnabled");
    	userPropValue = session.getConfigPropValue ( "WaterOneFlowEnabled" );
    	if ( (userPropValue != null) && !userPropValue.isEmpty() ) {
    		propValue = userPropValue;
    	}
        if ( (propValue != null) && propValue.equalsIgnoreCase("True") ) {
            packagePath = "rti.tscommandprocessor.commands.wateroneflow.ws.";
        }
    }
    else {
    	// Try to load plugin by matching the datastore type with the datastore class name
    	boolean loaded = false;
		// Loop through the datastore classes to find which has a name that matches the datatore type in configuration file 
		for ( Class c : pluginDataStoreFactoryClassList ) {
			String nameFromClass = c.getSimpleName(); // Something like DatabaseXDataStoreFactory
			if ( nameFromClass.equals(dataStoreType+"Factory") ) {
				// Construction will occur by the datastore factory below but need the package name
				packagePath = c.getPackage().toString() + "."; // Actually have problems with this
				pluginDataStoreFactoryClass = c; // Use directly below
				loaded = true;
        		break;
			}
		}
    	if ( !loaded ) {
	        throw new InvalidParameterException("Datastore type \"" + dataStoreType +
	            "\" is not recognized - cannot initialize datastore connection." );
    	}
    }
    if ( !packagePath.equals("") ) {
        propValue = dataStoreProps.getValue("Enabled");
        if ( (propValue != null) && propValue.equalsIgnoreCase("False") ) {
            // Datastore is disabled.  Do not even attempt to load.  This will minimize in-memory resource use.
            Message.printStatus(2, routine, "Created datastore \"" + dataStoreType + "\", name \"" +
                dataStoreProps.getValue("Name") + "\" is disabled.  Not opening." );
            return null;
        }
        else {
            // Datastore is enabled
            StopWatch sw = new StopWatch();
            sw.start();
            String className = packagePath + dataStoreType + "Factory";
            DataStoreFactory factory = null;
            if ( pluginDataStoreFactoryClass != null ) {
            	// This works for plugins
	            try {
		    		Constructor<?> constructor = pluginDataStoreFactoryClass.getConstructor();
		    		Object dataStoreFactory = constructor.newInstance();
		    		// The object must be a DataStore if it follows implementation requirements
		    		factory = (DataStoreFactory)dataStoreFactory;
		    	}
		    	catch ( NoSuchMethodException e ) {
		    		Message.printWarning(2,routine,"Error getting constructor for plugin command class \"" + className + "\" (" + e + ")");
		    	}
		    	catch ( IllegalAccessException e ) {
		    		Message.printWarning(2,routine,"Error creating instance of command for plugin command class \"" + className + "\" (" + e + ")");
		    	}
		    	catch ( InstantiationException e ) {
		    		Message.printWarning(2,routine,"Error creating instance of command for plugin command class \"" + className + "\" (" + e + ")");
		    	}
		    	catch ( InvocationTargetException e ) {
		    		Message.printWarning(2,routine,"Error creating instance of command for plugin command class \"" + className + "\" (" + e + ")");
		    	}
            }
            else {
                Message.printStatus(2, routine, "Getting class for name \"" + className + "\"" );
                Class clazz = Class.forName( className );
                Message.printStatus(2, routine, "Creating instance of class \"" + className + "\"" );
            	factory = (DataStoreFactory)clazz.newInstance();
            }
        	// Check for a login of "prompt"
        	String systemLogin = dataStoreProps.getValue("SystemLogin");
        	String systemPassword = dataStoreProps.getValue("SystemPassword");
            if ( ((systemLogin != null) && systemLogin.equalsIgnoreCase("prompt")) ||
            	((systemPassword != null) && systemPassword.equalsIgnoreCase("prompt"))	) {
                // If in batch mode, skip
            	if ( isBatch ) {
                    Message.printStatus(2, routine, "Skipping datastore \"" + dataStoreType + "\", name \"" +
                        dataStoreProps.getValue("Name") + "\" because in batch mode.  Will prompt for login when not in batch mode." );
                    return null;
            	}
            	else {
            		// Not in batch mode - open the datastore using a prompt initiated from the TSTool UI
            		if ( factory instanceof DataStoreConnectionUIProvider ) {
        	            // Create the datastore instance using the properties in the configuration file
            			// supplemented by login/password from interactive input
        	        	// Add to the processor even if it does not successfully open so that UI can show
        	        	// TODO SAM 2015-02-15 Need to update each factory to handle partial opens
            			Message.printStatus(2, routine, "Opening datastore \"" + dataStoreType + "\", name \"" +
                            dataStoreProps.getValue("Name") + "\" via prompt from TSTool GUI." );
            			DataStoreConnectionUIProvider uip = (DataStoreConnectionUIProvider)factory;
        	            DataStore dataStore = uip.openDataStoreConnectionUI(dataStoreProps,getJFrame());
        	            sw.stop();
        	            if ( dataStore == null ) {
        	            	Message.printStatus(2, routine, "Datastore \"" + dataStoreProps.getValue("Name") +
        	            		"\" is null after opening from TSTool GUI - this is unexpected." );
        	            }
        	            else {
	        	            // Add the datastore to the processor
	        	            processor.setPropContents ( "DataStore", dataStore );
	        	            Message.printStatus(2, routine, "Opening datastore type \"" + dataStoreType + "\", name \"" +
	        	                dataStore.getName() + "\" took " + sw.getMilliseconds() + " ms" );
        	            }
        	            return dataStore;
            		}
                	else {
        	            Message.printStatus(2, routine, "Not opening datastore type \"" + dataStoreType + "\", name \"" +
        	            	dataStoreProps.getValue("Name") + "\" because prompt is requested but datastore does not offer interface." );
                		return null;
                	}
            	}
            }
            else {
	            // Create the datastore instance using the properties in the configuration file
	        	// Add to the processor even if it does not successfully open so that UI can show
	        	// TODO SAM 2015-02-15 Need to update each factory to handle partial opens
	            DataStore dataStore = factory.create(dataStoreProps);
	            // Add the datastore to the processor
	            processor.setPropContents ( "DataStore", dataStore );
	            Message.printStatus(2, routine, "DataStore properties are: " + dataStore.getProperties().toString(","));
	            sw.stop();
	            Message.printStatus(2, routine, "Opening datastore type \"" + dataStoreType + "\", name \"" +
	                dataStore.getName() + "\" took " + sw.getMilliseconds() + " ms" );
	            return dataStore;
            }
        }
    }
    else {
        return null;
    }
}

/**
Open the datastores (e.g., RiversideDB connection(s)) using the TSTool configuration file information and set
in the command processor.  The configuration file is used to determine the database server and
name properties to use for the initial connection(s).  This method can be called from the UI code
to automatically establish database startup database connections.
In the UI, the user may subsequently open new connections (File...Open...RiversideDB) in which case the
openRiversideDB() method will be called directly (no need to deal with the main TSTool configuration file).
@param session TSTool session, which provides user and environment information
@param processor command processor that will have datastores opened
@param pluginDataStoreClassList list of plugin datastore classes to be loaded dynamically
@param isBatch is the software running in batch mode?  If in batch mode do not open up datastores
that have a login of "prompt".
*/
protected static void openDataStoresAtStartup ( TSToolSession session, TSCommandProcessor processor,
	List<Class> pluginDataStoreClassList, List<Class> pluginDataStoreFactoryClassList, boolean isBatch )
{   String routine = "TSToolMain.openDataStoresAtStartup";
    String configFile = getConfigFile();

    // Use the configuration file to get RiversideDB properties...
    Message.printStatus(2, routine, "TSTool configuration file \"" + configFile +
    "\" is being used to open datastores at startup." );
    
    // Legacy properties allowed on RiversideDB to be configured from the main TSTool.cfg file
    // Support this approach to open a default "RiversideDB" datastore name
    String name = "RiversideDB";
    String description = "Default RiversideDB";
    String databaseEngine = getPropValue ( "RiversideDB.DatabaseEngine" );
    String databaseServer = getPropValue ( "RiversideDB.DatabaseServer" );
    String databaseName = getPropValue ( "RiversideDB.Database" );
    // FIXME SAM 2009-06-17 Need to evaluate encryption of password.
    String systemLogin = getPropValue ( "RiversideDB.SystemLogin" ); // OK if null
    String systemPassword = getPropValue ( "RiversideDB.SystemPassword" ); // OK if null
    if ( (databaseEngine == null) && (databaseServer == null) && (databaseName == null) ) {
        // Most likely because using new datastore conventions
        // Open below with the generic datastore code
    }
    else {
        // Have some data so check for the individual properties
        boolean RiversideDB_enabled = false;  // Whether RiversideDBEnabled = true in TSTool config file
        String propval = __tstool_props.getValue ( "TSTool.RiversideDBEnabled");
        if ( (propval != null) && propval.equalsIgnoreCase("true") ) {
            RiversideDB_enabled = true;
        }
        if ( RiversideDB_enabled ) {
            String warning = "";
            if ( (databaseEngine == null) || databaseEngine.equals("") ) {
                warning +=
                    "\nNo RiversideDB.DatabaseEngine property defined in TSTool configuration file \"" + configFile +
                    "\".  Cannot open RiversideDB connection for batch run.";
            }
            if ( (databaseServer == null) || databaseServer.equals("") ) {
                warning +=
                    "\nNo RiversideDB.DatabaseServer property defined in TSTool configuration file \"" + configFile +
                    "\".  Cannot open RiversideDB connection for batch run.";
            }
            if ( (databaseName == null) || databaseName.equals("") ) {
                warning +=
                    "\nNo RiversideDB.Database property defined in TSTool configuration file \"" + configFile +
                    "\".  Cannot open RiversideDB connection for batch run.";
            }
            if ( warning.length() > 0 ) {
                Message.printWarning ( 1, routine, warning );
            }
            else {
                Message.printStatus(2, routine, "Opening RiversideDB \"" + name + "\"." );
                openRiversideDB ( processor, name, description,
                    databaseEngine, databaseServer, databaseName, systemLogin, systemPassword );
            }
        }
    }
    
    // Also allow multiple database connections via the new convention using datastore configuration files
    // The following code processes all datastores, although RiversideDB is the first implementation using
    // this approach.
    
    // Plugin datastore classes will have been loaded by this time
    
    // TODO SAM 2010-09-01 DataStore:*ConfigFile does not work
    List<Prop> dataStoreMainProps = getProps ( "DataStore:*" );
    List<String> dataStoreConfigFiles = new ArrayList<String>();
    if ( dataStoreMainProps == null ) {
        Message.printStatus(2, routine, "No configuration properties matching DataStore:*.ConfigFile" );
    }
    else {
        Message.printStatus(2, routine, "Got " + dataStoreMainProps.size() + " DataStore:*.ConfigFile properties.");
        for ( Prop prop: dataStoreMainProps ) {
            // Only want .ConfigFile properties
            if ( !StringUtil.endsWithIgnoreCase(prop.getKey(),".ConfigFile") ) {
                continue;
            }
            // Get the filename that defines the datastore - absolute or relative to the system folder
            String dataStoreFile = prop.getValue();
            dataStoreConfigFiles.add(dataStoreFile);
        }
    }
    // Also get names of datastore configuration files from configuration files in user's home folder .tstool/datastore
    if ( session.createDatastoreFolder() ) {
	    String datastoreFolder = session.getDatastoreFolder();
	    File f = new File(datastoreFolder);
	    FilenameFilter ff = new FilenameFilter() {
	    	public boolean accept(File dir, String name) {
	    		if ( name.toLowerCase().endsWith(".cfg") ) {
	    			return true;
	    		}
	    		else {
	    			return false;
	    		}
	    	}
	    };
	    String [] dfs = f.list(ff); // Returns files without leading path
	    if ( dfs != null ) {
	    	for ( int i = 0; i < dfs.length; i++ ) {
	    		dataStoreConfigFiles.add(datastoreFolder + File.separator + dfs[i]);
	    	}
	    }
    }
    // Now open the datastores for found configuration files
    for ( String dataStoreFile : dataStoreConfigFiles ) {
        Message.printStatus ( 2, routine, "Opening datastore using properties in \"" + dataStoreFile + "\".");
        // Read the properties from the configuration file
        PropList dataStoreProps = new PropList("");
        String dataStoreFileFull = dataStoreFile;
        if ( !IOUtil.isAbsolute(dataStoreFile)) {
            dataStoreFileFull = __tstoolInstallHome + File.separator + "system" + File.separator + dataStoreFile;
        }
        if ( !IOUtil.fileExists(dataStoreFileFull) ) {
            Message.printWarning(3, routine, "Datastore configuration file \"" + dataStoreFileFull +
                "\" does not exist - not opening datastore." );
        }
        else {
            dataStoreProps.setPersistentName(dataStoreFileFull);
            String dataStoreClassName = "";
            try {
                // Get the properties from the file
                dataStoreProps.readPersistent();
                // Also assign the configuration file path property to facilitate file processing later
                // (e.g., to locate related files referenced in the configuration file, such as lists of data
                // that are not available from web services)
                dataStoreProps.set("DataStoreConfigFile",dataStoreFileFull);
                openDataStore ( session, dataStoreProps, processor, pluginDataStoreClassList, pluginDataStoreFactoryClassList, isBatch );
            }
            catch ( ClassNotFoundException e ) {
                Message.printWarning (2,routine, "Datastore class \"" + dataStoreClassName +
                    "\" is not in the class path - report to software support (" + e + ")." );
                Message.printWarning(2, routine, e);
            }
            catch( InstantiationException e ) {
                Message.printWarning (2,routine, "Error instantiating datastore for class \"" + dataStoreClassName +
                    "\" - report to software support (" + e + ")." );
                Message.printWarning(2, routine, e);
            }
            catch( IllegalAccessException e ) {
                Message.printWarning (2,routine, "Datastore for class \"" + dataStoreClassName +
                    "\" needs a no-argument constructor - report to software support (" + e + ")." );
                Message.printWarning(2, routine, e);
            }
            catch ( Exception e ) {
                Message.printWarning (2,routine,"Error reading datastore configuration file \"" +
                    dataStoreFileFull + "\" - not opening datastore (" + e + ")." );
                Message.printWarning(2, routine, e);
            }
        }
    }
    
    // TODO SAM 2010-09-01 Transition HydroBase and other datastores here
}

// TODO SAM 2010-02-03 Evaluate whether non-null HydroBaseDMI return is OK or whether
// should rely on exceptions.
/**
Open the HydroBase connection using the CDSS configuration file information, when running
in batch mode or when auto-connecting in the GUI.  The CDSS configuration file is used to determine
HydroBase server and database name properties to use for the initial connection.  If no configuration file
exists, then a default connection is attempted.
@param processor the command processor that needs the (default) HydroBase connection.
@return opened HydroBaseDMI if the connection was made, or null if a problem.
*/
public static HydroBaseDMI openHydroBase ( TSCommandProcessor processor )
{   String routine = "TSToolMain.openHydroBase";
    boolean HydroBase_enabled = false;  // Whether HydroBaseEnabled = true in TSTool configuration file
    String propval = __tstool_props.getValue ( "TSTool.HydroBaseEnabled");
    if ( (propval != null) && propval.equalsIgnoreCase("true") ) {
        HydroBase_enabled = true;
    }
    if ( !HydroBase_enabled ) {
        Message.printStatus ( 2, routine, "HydroBase is not enabled in TSTool configuation so not opening connection." );
        return null; 
    }
    // If using GUI, this code is called from the TSTool_JFrame but need to check the property
    // below to make the connection occur.
    boolean autoConnect = false;
    propval = __tstool_props.getValue ( "HydroBase.AutoConnect");
    if ( (propval != null) && propval.equalsIgnoreCase("true") ) {
        autoConnect = true;
    }
    if ( IOUtil.isBatch() || autoConnect ) {
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
            List<HydroBaseDMI> hbdmi_Vector = new Vector(1);
            hbdmi_Vector.add ( hbdmi );
            processor.setPropContents ( "HydroBaseDMIList", hbdmi_Vector );
            Message.printStatus(2, routine, "Successfully opened HydroBase connection." );
            return hbdmi;
        }
        catch ( Exception e ) {
            Message.printWarning ( 1, routine, "Error opening HydroBase.  HydroBase features will be disabled." );
            Message.printWarning ( 3, routine, e );
            return null;
        }
    }
    return null; // Probably will not get here
}

/**
Open a single RiversideDB connection using the specified information.  This method will add the connection
to the list of datastores used by the processor.  This method is used with the legacy information stored
in the TSTool.cfg file.  New conventions use a datastore configuration file.
*/
protected static void openRiversideDB ( TSCommandProcessor processor, String name, String description,
    String databaseEngine, String databaseServer, String databaseName, String systemLogin, String systemPassword )
{   String routine = "TSToolMain.openRiversideDB";
    try {
        // Now open the database...
        // This uses the guest login.  If properties were not found,
        // then default HydroBase information will be used.
        RiversideDB_DMI rdmi = new RiversideDB_DMI ( databaseEngine, databaseServer, databaseName,
            -1, // Don't use the port number - use the database name instead
            systemLogin, // OK if null - use read-only guest
            systemPassword ); // OK if null - use read-only guest
        rdmi.open();
        DataStore dataStore = new RiversideDBDataStore(name, description, rdmi);
        // This adds a datastore to the processor
        processor.setPropContents ( "DataStore", dataStore );
    }
    catch ( Exception e ) {
        Message.printWarning ( 1, routine, "Error opening RiversideDB.  RiversideDB features will be disabled for \"" +
            name + "\" (" + e + ")." );
        Message.printWarning ( 3, routine, e );
    }
}

/**
Open the log file.  This should be done as soon as the application home
directory is known so that remaining information can be captured in the log file.
*/
private static void openLogFile ( TSToolSession session )
{	String routine = "TSToolMain.openLogFile";
	String user = IOUtil.getProgramUser();

	String logFile = null;
	if ( IOUtil.isApplet() ) {
		Message.printWarning ( 2, routine, "Running as applet - no TSTool log file opened." );
	}
	else {
		// Default as of 2016-02-18 is to open the log file as $home/.tstool/logs/TSTool_user.log file unless specified on command line
		if ( __logFileFromCommandLine != null ) {
			File f = new File(__logFileFromCommandLine);
			if ( !f.getParentFile().exists() ) {
				Message.printWarning ( 1, routine, "Error opening log file \"" + __logFileFromCommandLine +
					"\" - log file parent folder does not exist.");
			}
			else {
				logFile = __logFileFromCommandLine;
				Message.printStatus ( 1, routine, "Log file name from -logFile: " + logFile );
				try {
	                Message.openLogFile ( logFile );
	                // Do it again so it goes into the log file
	                Message.printStatus ( 1, routine, "Log file name from -logFile: " + logFile );
				}
				catch (Exception e) {
					Message.printWarning ( 1, routine, "Error opening log file \"" + logFile + "\"");
				}
			}
		}
		else {
			// Get the log file name from the session object...under user home folder
			if ( session.createLogFolder() ) {
				// Log folder already exists or was created, so OK to use
				logFile = session.getLogFile();
				Message.printStatus ( 1, routine, "Log file name from TSTool default: " + logFile );
				try {
	                Message.openLogFile ( logFile );
	                // Also log for troubleshooting
	                Message.printStatus ( 1, routine, "Log file name from TSTool default: " + logFile );
				}
				catch (Exception e) {
					Message.printWarning ( 1, routine, "Error opening log file \"" + logFile + "\"");
				}
			}
			else {
				Message.printWarning ( 2, routine, "Unable to create/open TSTool log folder \"" + session.getLogFolder() + "\".  Not opening log file.");
			}
		}
	    boolean oldWay = false;
	    if ( oldWay ) {
	    	// TODO SAM 2016-02-19 Remove this if the above logic works for the log file
		    if ( (__tstoolInstallHome == null) || (__tstoolInstallHome.length() == 0) || (__tstoolInstallHome.charAt(0) == '.')) {
				Message.printWarning ( 2, routine, "Home directory is not defined.  Not opening log file.");
			}
			else {
				// Home folder was specified so create log file in that folder logs folder
				String logfile = null;
	            if ( (user == null) || user.trim().equals("")) {
					logfile = __tstoolInstallHome + File.separator + "logs" + File.separator + "TSTool.log";
				}
				else {
	                logfile = __tstoolInstallHome + File.separator + "logs" + File.separator + "TSTool_" +	user + ".log";
				}
				Message.printStatus ( 1, routine, "Log file name: " + logfile );
				if ( logfile != null ) {
					try {
		                Message.openLogFile ( logfile );
					}
					catch (Exception e) {
						Message.printWarning ( 1, routine, "Error opening log file \"" + logfile + "\"");
					}
				}
			}
	    }
	}
}

/**
Parse command line arguments.
@param args Command line arguments.
*/
public static void parseArgs ( TSToolSession session, String[] args )
throws Exception
{	String routine = "TSToolMain.parseArgs", message;
	int pos = 0; // Position in a string.

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
		if (args[i].equalsIgnoreCase("-batchServer")) {
			Message.printStatus ( 1, routine, "Will start TSTool in batch server mode." );
			__isBatchServer = true;
		}
		else if (args[i].equalsIgnoreCase("-batchServerHotFolder")) {
		    // Batch server hot folder name
			if ((i + 1)== args.length) {
				message = "No argument provided to '-batchServerHotFolder'";
				Message.printWarning(1,routine, message);
				throw new Exception(message);
			}
			i++;
			__batchServerHotFolder = args[i];
		}
		else if (args[i].equalsIgnoreCase("-commands")) {
		    // Command file name
			if ((i + 1)== args.length) {
				message = "No argument provided to '-commands'";
				Message.printWarning(1,routine, message);
				throw new Exception(message);
			}
			i++;
			setupUsingCommandFile ( args[i], true );
		}
		else if (args[i].equalsIgnoreCase("-batchTimeout")) {
		    // Batch timeout in seconds
			if ((i + 1)== args.length) {
				message = "No argument provided to '-batchTimeout'";
				Message.printWarning(1,routine,message);
				throw new Exception(message);
			}
			else {
				try {
					__batchTimeoutSeconds = Integer.parseInt(args[i + 1]);
				}
				catch ( NumberFormatException e ) {
					message = "-batchTimeout argument \"" + args[i + 1] + " is not an integer.";
					Message.printWarning(1,routine,message);
					throw new Exception(message);
				}
			}
			i++;
		}
		else if (args[i].equalsIgnoreCase("-config")) {
		    // Configuration file name
		    // TODO SAM 2011-12-07 Need to allow properties like ${UserHome} to read the configuration file from
		    // a users' home folder, even if TSTool is installed in a central location
            if ((i + 1)== args.length) {
            	message = "No argument provided to '-config'";
                Message.printWarning(1,routine,message);
                throw new Exception(message);
            }
            i++;
            setConfigFile ( IOUtil.verifyPathForOS(IOUtil.getPathUsingWorkingDir(args[i])) );
            Message.printStatus(1 , routine, "Using configuration file from command line: \"" + getConfigFile() + "\"" );
            // Read the configuration file specified here.  If not specified, the defaults read immediately
            // after -home is parsed will be reset if in both files.
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
				message = "No argument provided to '-home'";
				Message.printWarning(1,routine,message);
				throw new Exception(message);
			}
			i++;
           
            //Changed __home since old way wasn't supporting relative paths __home = args[i];
            __tstoolInstallHome = (new File(args[i])).getCanonicalPath().toString();
           
			// Open the log file so that remaining messages will be seen in the log file...
			openLogFile(session);
			Message.printStatus ( 1, routine, "TSTool install folder from -home command line parameter is \"" +
			    __tstoolInstallHome + "\"" );
			// The default configuration file location is relative to the install home.  This works
			// as long as the -home argument is first in the command line.
			setConfigFile ( __tstoolInstallHome + File.separator + "system" + File.separator + "TSTool.cfg" );
			// Don't call setProgramWorkingDir or setLastFileDialogDirectory this since we set to user.dir at startup
			//IOUtil.setProgramWorkingDir(__home);
			//JGUIUtil.setLastFileDialogDirectory(__home);
			IOUtil.setApplicationHomeDir(__tstoolInstallHome);
			// TODO SAM 2016-02-22 See http://fahdshariff.blogspot.be/2011/08/changing-java-library-path-at-runtime.html
			// - cannot change the path at runtime
			// - trying some solutions when loading HEC-DSS libraries in static code and will remove following if it works
			String javaLibraryPath = System.getProperty ( "java.library.path" );
			Message.printStatus( 2, routine, "java.library.path at startup: \"" + System.getProperty("java.library.path") + "\"" );
	        // Read the configuration file to get default TSTool properties,
            // so that later command-line parameters can override them.
			// If any other command line arguments are -config, then skip the following because a user-specified
			// command file will be read instead
			boolean userSpecifiedConfig = false;
			for ( int iarg2 = 0; iarg2 < args.length; iarg2++ ) {
			    if ( args[iarg2].equalsIgnoreCase("-config") ) {
			        userSpecifiedConfig = true;
			        break;
			    }
			}
			if ( !userSpecifiedConfig ) {
			    Message.printStatus(1 , routine, "Using default configuration file: \"" + getConfigFile() + "\"" );
			    readConfigFile(getConfigFile());
			}
		}
		else if (args[i].equalsIgnoreCase("-httpServer")) {
			Message.printStatus ( 1, routine, "Will start TSTool in HTTP server mode." );
			__isHttpServer = true;
		}
		else if (args[i].equalsIgnoreCase("-logFile")) {
		    // Specify the log file.
			if ((i + 1)== args.length) {
				message = "No argument provided to '-logFile'";
				Message.printWarning(1,routine,message);
				throw new Exception(message);
			}
			i++;
			__logFileFromCommandLine = args[i];
		}
        else if (args[i].equalsIgnoreCase("-nodiscovery")) {
            // Don't run commands in discovery mode on initial load (should only be used in large batch runs)...
            Message.printStatus ( 1, routine, "Will process command file without main GUI (plot windows only)." );
            __runDiscoveryOnLoad = false;
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
			Message.printStatus ( 1, routine, "Will start TSTool in restlet server mode." );
			__isRestletServer = true;
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
		    // Assume that a command file has been specified on the command line - normally this is triggered
		    // by a double-click on a file with a *.TSTool extension.  In this case the GUI will start and
		    // load the command file.
		    setupUsingCommandFile ( args[i], false );
		}
	}
}

// TODO - need to make these work as expected.
/**
Parse the command-line arguments for the applet, determined from the applet data.
@param a JApplet for this application.
*/
public static void parseArgs ( TSToolSession session, JApplet a )
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
    parseArgs ( session, (String [])args.toArray() );
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
	"See the TSTool documentation for more information." + nl + nl;
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
TODO SAM 2015-01-07 need to store configuration information in a generic "session" object to be developed.
@param configFile Name of the configuration file.
*/
private static void readConfigFile ( String configFile )
{	String routine = "TSToolMain.readConfigFile";
    Message.printStatus ( 2, routine, "Reading TSTool configuration information from \"" + configFile + "\"." );
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
                if ( prop.getKey().equalsIgnoreCase("TSTool.DiffProgram") ) {
                	// Also set global properties that are used more generically
                	IOUtil.setProp("DiffProgram", prop.getValue());
                }
            }
		}
		catch ( Exception e ) {
			Message.printWarning ( 1, routine,
			"Error reading TSTool configuration file \"" + configFile + "\".  TSTool may not start (" + e + ")." );
			Message.printWarning ( 1, routine, e );
		}
	}
	else {
	    Message.printWarning ( 1, routine,
	        "TSTool configuration file \"" + configFile + "\" is not readable.  TSTool may not start." );
	}
}

/**
Run TSTool in restlet server mode.
*/
private static void runRestletServer ()
{   String routine = "TSToolMain.runRestletServer()";
    try {
        int port = -1; // Default
        TSToolServer server = new TSToolServer();
        server.startServer ( port, new ArrayList<Parameter>() );
    }
    catch (Exception e) {  
        Message.printWarning ( 1, routine, "Error starting restlet application (" + e + ")." );
        Message.printWarning ( 3, routine, e );
        quitProgram ( 1 );
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
at startup, and a relative path may be provided if -config is specified on the command line.
@param configFile Configuration file.
*/
private static void setConfigFile ( String configFile )
{
    __configFile = configFile;
}

/**
Set the icon for the application.  This will be used for all windows.
@param iconType The icon will be used by searching for
TSToolXXXIcon32.gif (where XXX=iconType), both using a path for the main application.
*/
public static void setIcon ( String iconType )
{	// First try loading the icon from the JAR file or class path...
	String iconFile = "TSTool" + iconType + "Icon32.gif";
	String iconPath ="";
	try {
        // The icon files live in the main application folder in the classpath.
        iconPath = "DWR/DMI/tstool/" + iconFile;
		JGUIUtil.setIconImage( iconPath );
	}
	catch ( Exception e ) {
		Message.printStatus ( 2, "", "TSTool icon \"" + iconPath +	"\" does not exist in classpath." );
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
{   String routine = "TSToolMain.setWorkingDirInitial";
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
@param commandFileFull the full (absolute) path to the command file
*/
private static void setWorkingDirUsingCommandFile ( String commandFileFull )
{   File commandFileFull_File = new File ( commandFileFull );
    String workingDir = commandFileFull_File.getParent();
    IOUtil.setProgramWorkingDir ( workingDir );
    // Set the dialog because if the running in batch mode and interaction with the graph
    // occurs, this default for dialogs should be the home of the command file.
    JGUIUtil.setLastFileDialogDirectory( workingDir );
    // Print at level 1 because the log file is not yet initialized.
    String message = "Setting working directory to command file folder \"" + workingDir + ".\"";
    //Message.printStatus ( 1, routine, message );
    System.out.println(message);
}

// TODO SAM 2015-12-14 This does not seem to work as intended
// -The Excecutor service itself is not a thread and so does not return immediately
// -What will happen if -nomaingui is used with a thread timeout
// -Probably need timeout on start-up but not once graph is displayed
/**
Start the timeout thread, which will exit TSTool if it is not finished within the timeout.
This is needed in cases where something in the code hangs and TSTool never exits.
Ideally those situations can be handled with granular timeouts but that is sometimes not possible
due to limitations in the packages that are called.
@param timeoutSeconds number of seconds before timing out (ignore if <= 0)
*/
private static void startTimeoutThread ( int timeoutSeconds )
{	String routine = "startTimeoutThread";
	if ( timeoutSeconds <= 0 ) {
		// No need to start timeout thread
		return;
	}
	ExecutorService executor = Executors.newSingleThreadExecutor();
	Future<String> future = executor.submit(new SleepTask(timeoutSeconds));
	try {
		Message.printStatus(2, routine, "Starting thread to time-out TSTool if not done after " + timeoutSeconds + " seconds.");
		// Actually the task that is run above should sleep the number of seconds so specifying the seconds below is redundant
		future.get(timeoutSeconds,TimeUnit.SECONDS);
		Message.printStatus(2, routine, "Exiting TSTool after waiting " + timeoutSeconds + " seconds.");
		quitProgram(1); // TODO SAM 2015-02-14 need to evaluate a standard set of exit codes
	}
	catch ( TimeoutException e ) {
		Message.printWarning(2, routine, e );
	}
	catch ( InterruptedException e ) {
		Message.printWarning(2, routine, e );
	}
	catch ( ExecutionException e ) {
		Message.printWarning(2, routine, e );
	}
	finally {
		// Shutdown the tasks managed by executor.
		executor.shutdownNow();
	}
	
	/*
    Runnable r = new Runnable() {
        public void run() {
            try {
                // Put in some protection against injection by checking for keywords other than SELECT
                Thread.sleep(freqms);
            }
            catch ( Exception e ) {
                // OK since don't care about results but output to troubleshoot (typical user won't see).
                Message.printWarning(3, "keepAlive.run", e);
            }
        }
    };
    */
}

}

/**
Class used to run a timeout task, which is optional to end TSTool when running in batch mode.
*/
class SleepTask implements Callable<String>
{
	/**
	Number of seconds the task should sleep before timing out.
	*/
	private int sleepSeconds = 0;
	
	/**
	Construct with the timeout.
	*/
	public SleepTask ( int sleepSeconds )
	{
		this.sleepSeconds = sleepSeconds;
	}
	
	/**
	Callable task.
	*/
	@Override
	public String call () throws Exception {
		Thread.sleep(this.sleepSeconds*1000); // Task will wait the timeout and then return
		return "Slept " + sleepSeconds;
	}
}