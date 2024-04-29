// TSToolMain - Main (application startup) class for TSTool.

/* NoticeStart

TSTool
TSTool is a part of Colorado's Decision Support Systems (CDSS)
Copyright (C) 1994-2024 Colorado Department of Natural Resources

TSTool is free software:  you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

TSTool is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

You should have received a copy of the GNU General Public License
    along with TSTool.  If not, see <https://www.gnu.org/licenses/>.

NoticeEnd */

package DWR.DMI.tstool;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.HttpURLConnection;
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
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;

import javax.swing.JFrame;

import org.restlet.data.Parameter;

import com.sun.net.httpserver.HttpServer;

import riverside.datastore.DataStore;
import riverside.datastore.DataStoreConnectionUIProvider;
import riverside.datastore.DataStoreFactory;
import riverside.datastore.DataStoreSubstitute;
import rti.app.tstoolrestlet.TSToolServer;
import rti.tscommandprocessor.core.TSCommandFileRunner;
import rti.tscommandprocessor.core.TSCommandProcessor;
import DWR.DMI.HydroBaseDMI.HydroBaseDMI;
import DWR.DMI.HydroBaseDMI.HydroBase_Util;
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
Main (application startup) class for TSTool.
This class will start the TSTool GUI or run the TSCommandProcessor in batch mode with a command file.
The methods in this file are called by the startup TSTool and CDSS versions of TSTool.
*/
public class TSToolMain
{

	/**
	 * The program name.
	 */
	public static final String PROGRAM_NAME = "TSTool";

/**
 * Semantic version, see:  https://semver.org/
 * - previously did not use period after third part (14.0.0dev) but have started using period (14.0.0.dev1).
 * - otherwise, there can be problems with the string being interpreted as hex code by installer tools
 * - as of version 14, do not pad version parts with zeros
 */
public static final String PROGRAM_VERSION = "14.9.5 (2024-04-29)";

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
Timeout when running in batch mode.
TSTool will exit if processing has not finished (usually because of web service hang-up, etc.)
*/
private static int __batchTimeoutSeconds = 0;

/**
Path to the configuration file.
This cannot be defaulted until the -home command line parameter is processed.
*/
private static String __configFile = "";

/**
List of application properties to control the software from the configuration file and passed in on the command line.
*/
private static PropList __tstool_props = null;

/**
 * List of processor properties, which will be set for each command file run.
 * Properties are parsed from command line Property==Value parameters.
 */
private static PropList processorProps = new PropList("ProcessorProps");

/**
Indicates whether TSTool is running in batch server mode (look for command files in hot folder).
*/
private static boolean __isBatchServer = false;

/**
Indicates whether TSTool is running in HTTP server mode (requires command files to match
REST endpoints and URL parameters will translate to ${Property}).
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
Running discovery is a performance hit, especially for very large command files that are generated from templates,
and is generally unnecessary in batch mode.
*/
private static boolean __runDiscoveryOnLoad = true;

/**
Indicates whether the main GUI is shown, for cases where TSTool is run in in batch mode,
with only the plot window shown.  If running in interactive mode the GUI is always shown.
*/
private static boolean __showMainGUI = true;

/**
 * Datastore substitute list, which allows datastore names in command files to remain but use a different datastore,
 * particularly useful when pointing to different web service URL or database server for testing.
 */
private static List<DataStoreSubstitute> datastoreSubstituteList = new ArrayList<>();

/**
 * Datastores to enable/disable, used to optimize session performance, for example when run in batch mode.
 */
private static DataStoreEnabledChecker dataStoreEnabledChecker = new DataStoreEnabledChecker();

/**
 * Count of message about --disable-datastores and --enable-datastores not being specified.
 */
private static int disableDatastoresMessageCount = 0;

/**
Indicates whether the --nomaingui command line argument is set.
This is used instead of just the above to know for sure the combination of command line parameters.
*/
private static boolean __noMainGUIArgSpecified = false;

/**
Command file being processed when run in batch mode with --commands File.
*/
private static String __commandFile = null;

/**
 * Modification string for the title, used to customize TSTool.
 */
private static String titleMod = "";

/**
 * Find the list of plugin jar files using new (TSTool 12.07.00 and later approach).
 * This simply finds candidate jar files in the expected locations.
 * The loadPluginDataStores*, etc. methods then examine the jar files and use if appropriate.
 * @param session TSTool session.
 * @param pluginJarList List of full path to plugin jar file.
 */
private static void findPluginDataStoreJarFilesNew ( TSToolSession session, List<String> pluginJarList ) {
	String routine = TSToolMain.class.getSimpleName() + ".findDataStorePluginJarFilesNew";
	// Get a list of jar files in the user plugins folder (will take precedent over installation files).
	File userPluginsFolder = new File(session.getUserPluginsFolder());
	Message.printStatus(2, routine, "Finding plugin datastore jar files in plugins folder \"" + userPluginsFolder + "\"");
	try {
		getMatchingFilenamesInTree ( pluginJarList, userPluginsFolder, ".*.jar" );
	}
	catch ( Exception e ) {
		// Should not happen.
	}
	// Get a list of jar files in the installation plugins folder.
	File installPluginsFolder = new File(session.getInstallPluginsFolder());
	Message.printStatus(2, routine, "Finding plugin datastore jar files in plugins folder \"" + installPluginsFolder + "\"");
	try {
		getMatchingFilenamesInTree ( pluginJarList, installPluginsFolder, ".*.jar" );
	}
	catch ( Exception e ) {
		// Should not happen.
	}
}

/**
 * Find the list of plugin jar files using old (pre-TSTool 12.06.00 approach).
 * @param session TSTool session.
 * @param pluginJarList List of full path to plugin jar file.
 */
private static void findPluginDataStoreJarFilesOld ( TSToolSession session, List<String> pluginJarList ) {
	String routine = TSToolMain.class.getSimpleName() + ".findDatastorePluginJarFilesOld";
	final String [] pluginHomeFolders = {
		// InstallHome/plugin-datastore
		__tstoolInstallHome + File.separator + "plugin-datastore",
		// .tstool/plugin-datastore/DatastoreName/bin/Datastore-version.jar
		session.getUserTstoolFolder() + File.separator + "plugin-datastore"
	};
	for ( int iPluginHome = 0; iPluginHome < 2; iPluginHome++ ) {
		// First get a list of candidate URLs, using path to jar file.
		String pluginDir = pluginHomeFolders[iPluginHome];
		// TODO SAM 2016-04-03 it may be desirable to include sub-folders under bin for third-party software
		// in which case ** will need to be used somehow in the glob pattern.
		String glob = pluginDir + File.separator + "*" + File.separator + "bin" + File.separator + "*.jar";
		// For glob backslashes need to be escaped (this should not impact Linux).
		glob = glob.replace ("\\","\\\\");
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
			// The following walks the tree path under the specified folder.
			// The full path is returned during walking (not just under the starting folder).
			Path pluginDirPath = Paths.get(pluginDir);
			Message.printStatus(2,routine, "Trying to find plugin datastores using pluginDirPath \"" + pluginDirPath + "\" and glob pattern \"" + glob + "\"" );
			Files.walkFileTree(pluginDirPath, matcherVisitor);
		}
		catch ( IOException e ) {
			Message.printWarning(3,routine,"Error getting jar file list for plugin datastore(s) (" + e + ")" );
			// Return empty list of datastore plugin classes.
			return;
		}
	}
}

/**
Return the batch server hot folder.
@return the batch server hot folder
*/
public static String getBatchServerHotFolder() {
	return __batchServerHotFolder;
}

/**
Return the command file that is being processed, or null if not being run in batch mode.
@return the path to the command file to run.
*/
private static int getBatchTimeout () {
	return __batchTimeoutSeconds;
}

/**
Return the command file that is being processed, or null if not being run in batch mode.
@return the path to the command file to run.
*/
private static String getCommandFile () {
	return __commandFile;
}

/**
Return the name of the configuration file for the session.  This file will be determined
from the -home command line parameter during command line parsing (default) and can be
specified with -config File on the command line (typically used to test different configurations).
@return the full path to the configuration file.
*/
public static String getConfigFile () {
    return __configFile;
}

/**
Return the JFrame for the main TSTool GUI.
@return the JFrame instance for use with low-level code that needs to pop up dialogs, etc.
*/
public static JFrame getJFrame () {
	return (JFrame)__tstool_JFrame;
}

/**
 * Return the TSTool major version or zero if an issue (should not happen).
 * @return the major TSTool version
 */
private static int getMajorVersion () {
    int majorVersion = 0;
    try {
    	System.err.println("program version: " + IOUtil.getProgramVersion());
    	majorVersion = Integer.parseInt(IOUtil.getProgramVersion().split("\\.")[0].trim());
    	System.err.println("Major version: " + majorVersion);
    }
    catch ( Exception e ) {
    	Message.printWarning(1,"TSTool", "Error getting TSTool major version number (" + e + ")." );
    }
    return majorVersion;
}

/**
Visits all files and directories under the given directory and if
the file matches the pattern it is added to the file list.
All commands file that end with ".jar" will be added to the list.
@param fileList List of command files that are matched, to be appended to.
@param path Folder in which to start searching for command files.
@param pattern Pattern to match when searching files, for example "*.jar".
@throws IOException
 */
private static void getMatchingFilenamesInTree ( List<String> fileList, File path, String pattern )
throws IOException {
    //String routine = TSTool.class.getSimpleName() + ".getMatchingFilenamesInTree";
    if (path.isDirectory()) {
        String[] children = path.list();
        for (int i = 0; i < children.length; i++) {
        	// Recursively call with full path using the directory and child name.
        	getMatchingFilenamesInTree(fileList,new File(path,children[i]), pattern);
        }
    }
    else {
        // Add to list if command file is valid.
        String pathName = path.getName();
    	//Message.printStatus(2, routine, "Checking path \"" + pathName + "\" against \"" + pattern + "\"" );
    	// Do comparison on file name without directory.
        if( pathName.matches( pattern ) ) {
        	//Message.printStatus(2, "", "File matched: \"" + path + "\".");
        	fileList.add(path.toString());
        }
    }
}

/**
Return a TSTool property.  The properties are defined in the TSTool configuration file.
@param propertyExp name of property to look up as a Java regular expression.
@return the value(s) for a TSTool configuration property, or null if a properties file does not exist.
Return null if the property is not found (or if no configuration file exists for TSTool).
*/
public static List<Prop> getProps ( String propertyExp ) {
    if ( __tstool_props == null ) {
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
public static String getPropValue ( String property ) {
	if ( __tstool_props == null ) {
		return null;
	}
	return __tstool_props.getValue ( property );
}

/**
Indicate whether the commands should be run after loaded into the GUI
(when a command file is specified on the command line).
@return true if the commands should automatically be run after loading.
*/
public static boolean getRunOnLoad () {
    return __run_commands_on_load;
}

/**
Initialize important data and set message levels for application after startup.
*/
private static void initializeLoggingLevelsAfterLogOpened () {
	// Initialize message levels.
    // FIXME SAM 2008-01-11 Need to have initialize2() reset message levels to not show on the console.
	Message.setDebugLevel ( Message.TERM_OUTPUT, 0 );
	Message.setDebugLevel ( Message.LOG_OUTPUT, 0 );
	Message.setStatusLevel ( Message.TERM_OUTPUT, 0 );
	Message.setStatusLevel ( Message.LOG_OUTPUT, 2 );
	Message.setWarningLevel ( Message.TERM_OUTPUT, 0 );
	Message.setWarningLevel ( Message.LOG_OUTPUT, 3 );

	// Indicate that message levels should be shown in messages, to allow
	// for a filter when displaying messages.

	Message.setPropValue ( "ShowMessageLevel=true" );
	Message.setPropValue ( "ShowMessageTag=true" );
}

/**
Initialize important data and set message levels for console startup.
*/
private static void initializeLoggingLevelsBeforeLogOpened () {
    // Initialize message levels.
    // FIXME SAM 2008-01-11 Need to have initialize2() reset message levels to not show on the console.
    Message.setDebugLevel ( Message.TERM_OUTPUT, 0 );
    Message.setDebugLevel ( Message.LOG_OUTPUT, 0 );
    Message.setStatusLevel ( Message.TERM_OUTPUT, 2 );
    Message.setStatusLevel ( Message.LOG_OUTPUT, 2 );
    Message.setWarningLevel ( Message.TERM_OUTPUT, 2 );
    Message.setWarningLevel ( Message.LOG_OUTPUT, 3 );

    // Indicate that message levels should be shown in messages, to allow
    // for a filter when displaying messages.

    Message.setPropValue ( "ShowMessageLevel=true" );
    Message.setPropValue ( "ShowMessageTag=true" );
}

/**
Initialize important data relative to the installation home.
*/
private static void initializeAfterHomeIsKnown () {
	String routine = TSToolMain.class.getSimpleName() + ".initializeAfterHomeIsKnown";

	// Initialize the system data.

	String units_file = __tstoolInstallHome + File.separator + "system" + File.separator + "DATAUNIT";

	Message.printStatus ( 2, routine, "Reading the units file \"" +	units_file + "\"" );
	try {
        DataUnits.readUnitsFile( units_file );
        Message.printStatus ( 2, routine, "  Read " + DataUnits.getUnitsData().size() + " units definitions." );
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
public static boolean isBatchServer() {
	return __isBatchServer;
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
Load plugin datastores.  Plugin files can exist in three locations:
<ul>
<li> UserHomeFolder/.tstool/12/plugins or subfolder (prototyped in TSTool 12.07.00) - preferred for latest design</li>
<li> TSToolInstallFolder/plugin-datastore/DataStoreName/bin/DataStore-version.jar -
will ensure that it continues to work (prototyped in TSTool 12.06.00 and earlier)</li>
<li> UserHomeFolder/.tstool/plugin-datastore/DataStoreName/bin/DataStore-version.jar - old folder convention</li>
</ul>
@param session TSTool session, which provides user and environment information.
@param pluginDataStoreList empty list of plugin datastores, will be populated by this method.
@param pluginDataStoreFactoryList empty list of plugin datastore factories, will be populated by this method.
@param pluginCommandList empty list of plugin commands, will be populated by this method.
*/
private static void loadPluginDataStores(TSToolSession session,
	@SuppressWarnings("rawtypes") List<Class> pluginDataStoreList,
	@SuppressWarnings("rawtypes") List<Class> pluginDataStoreFactoryList,
	@SuppressWarnings("rawtypes") List<Class> pluginCommandList ) {
	final String routine = TSToolMain.class.getSimpleName() + ".loadPluginDataStores";
	// Find jar files that contain datastores.
	// First use the old approach (TSTool 12.06.00 and earlier).
	final List<String> pluginJarListOld = new ArrayList<>();
	Message.printStatus(2, routine, "Start loading plugin datastores and commands using the old approach...");
	findPluginDataStoreJarFilesOld ( session, pluginJarListOld );
	loadPluginDataStoresOld("", session, pluginJarListOld, pluginDataStoreList, pluginDataStoreFactoryList, pluginCommandList );
	Message.printStatus(2, routine, "...end loading plugin datastores and commands using the old approach.");
	// Next use the new approach (TSTool 12.07.00 and later).
	final List<String> pluginJarListNew = new ArrayList<>();
	Message.printStatus(2, routine, "Start loading plugin datastores and commands using the new approach...");
	findPluginDataStoreJarFilesNew ( session, pluginJarListNew );
	loadPluginDataStoresNew(session, pluginJarListNew, pluginDataStoreList, pluginDataStoreFactoryList, pluginCommandList );
	Message.printStatus(2, routine, "...end loading plugin datastores and commands using the new approach.");

	// Globally save the folders for plugins:
	// - this allows those folders to be added to the classpath in cases where a separate Java program is run later,
	//   such as the S3 file browser run with the TSTool AWS plugin
	// - also save the "dep" sub-folder if it exists
	// - check both the old and new lists
	// - only add files and associated "dep" folders once
	List<String> pluginClasspathList = new ArrayList<>();
	List<String> checkList = null;
	for ( int i = 0; i < 2; i++ ) {
		if ( i == 0 ) {
			checkList = pluginJarListOld;
		}
		else {
			checkList = pluginJarListNew;
		}
		for ( String jarFile : checkList ) {
			File f = new File(jarFile);
			if ( f.exists() ) {
				// The jar file exists:
				// - add to the list if it is not already in the list
				if ( !StringUtil.isInList(pluginClasspathList, jarFile ) ) {
					pluginClasspathList.add(jarFile);
					continue;
				}
				// Also add the "dep" folder used with plugins if it exists.
				String folder = f.getParent() + File.separator + "dep";
				f = new File(folder);
				if ( f.exists() ) {
					if ( !StringUtil.isInList(pluginClasspathList, f.getAbsolutePath() ) ) {
						// Add the folder with * wildcard to match all files in "dep".
						pluginClasspathList.add(f.getAbsolutePath() + File.separator + "*");
					}
				}
			}
		}
	}
	if ( pluginClasspathList.size() > 0 ) {
		// Set the application plugin classpath list.
		IOUtil.setApplicationPluginClasspath ( pluginClasspathList );
	}
}

/**
 * Load plugin datastore classes using new (TSTool 12.07.00 and later approach).
 * Currently uses the old approach because at this point the list of candidate jar files is processed.
 * @param session
 * @param pluginJarList
 * @param pluginDataStoreList
 * @param pluginDataStoreFactoryList
 * @param pluginCommandList empty list of plugin commands, will be populated by this method.
 */
private static void loadPluginDataStoresNew(TSToolSession session, List<String> pluginJarList,
	@SuppressWarnings("rawtypes") List<Class> pluginDataStoreList,
	@SuppressWarnings("rawtypes") List<Class> pluginDataStoreFactoryList,
	@SuppressWarnings("rawtypes") List<Class> pluginCommandList ) {
	// Try using the old logic for now.
	loadPluginDataStoresOld( "New", session, pluginJarList, pluginDataStoreList, pluginDataStoreFactoryList, pluginCommandList );
}

/**
 * Load plugin datastore classes using old (TSTool 12.06.00 and earlier approach).
 * @param session
 * @param pluginJarList
 * @param pluginDataStoreList
 * @param pluginDataStoreFactoryList
 * @param pluginCommandList empty list of plugin commands, will be populated by this method.
 */
private static void loadPluginDataStoresOld(String messagePrefix, TSToolSession session, List<String> pluginJarList,
	@SuppressWarnings("rawtypes") List<Class> pluginDataStoreList,
	@SuppressWarnings("rawtypes") List<Class> pluginDataStoreFactoryList,
	@SuppressWarnings("rawtypes") List<Class> pluginCommandList ) {
	if ( messagePrefix.isEmpty() ) {
		messagePrefix = "Old";
		// Use for messages so it is clear whether processing old or new datastore configurations.
	}
	String routine = TSToolMain.class.getSimpleName() + ".loadPluginDataStores" + messagePrefix;
	// Create a separate class loader for each plugin to maintain separation.
	// From this point forward the jar file path does not care if in the user folder or TSTool installation folder.
	Message.printStatus(2, routine, "Trying to load plugin datastores from " + pluginJarList.size() + " candidate jar files.");
	for ( String pluginJar : pluginJarList ) {
		// TODO figure out if only the top level datastore Jar file should be included.
		if ( Message.isDebugOn ) {
			Message.printStatus(2, routine, "Trying to load plugin datastores from \"" + pluginJar + "\"");
		}
		URL [] dataStoreJarURLs = new URL[2];
		try {
			// Convert the file system filename to URL using forward slashes.
			dataStoreJarURLs[0] = new URL("file:///" + pluginJar.replace("\\", "/"));
			// Also add all the jar files in the "/dep" folder:
			// - tried to figure out how to use MANIFEST-MF Class-Path property but seemed confusing
			//   so just add all jar files that are found
			File f = new File(pluginJar);
			List<File> depJarFiles = IOUtil.getFilesMatchingPattern(f.getParent() + File.separator + "dep", "jar", false);
			URL [] dataStoreJarURLs2 = new URL[1 + depJarFiles.size()];
			dataStoreJarURLs2[0] = dataStoreJarURLs[0];
			int i = 1;
			for ( File depJarFile : depJarFiles ) {
				dataStoreJarURLs2[i++] = new URL("file:///" + depJarFile.getAbsolutePath().replace("\\", "/"));
			}
			dataStoreJarURLs = dataStoreJarURLs2;
		}
		catch ( MalformedURLException e ) {
			Message.printWarning(3,routine,"Error creating URL for datastore plugin jar file \"" + pluginJar + "\" (" + e + ") - skipping plugin" );
			continue;
		}
		// Create a class loader specific to the datastore:
		// - this expects the datastore to be in the jar file with class name XXXXXDataStore
		// - TODO smalers 2020-07-26 using different class loaders for datastore and command classes causes an issue later
		//PluginDataStoreClassLoader pcl = new PluginDataStoreClassLoader ( dataStoreJarURLs );
		PluginDataStoreClassLoader pcl = null;
		boolean useChildClassLoader = false;
		// Create a class loader for plugins.
		pcl = new PluginDataStoreClassLoader ( dataStoreJarURLs, TSToolMain.class.getClassLoader(), useChildClassLoader );
		@SuppressWarnings("rawtypes")
		List<Class> pluginDataStoreList1 = null;
		@SuppressWarnings("rawtypes")
		List<Class> pluginDataStoreFactoryList1 = null;
		// Load datastore classes.
		try {
			pluginDataStoreList1 = pcl.loadDataStoreClasses();
		}
		catch ( ClassNotFoundException e ) {
			Message.printWarning(2,routine,"Error loading datastore plugin classes (" + e + ")." );
			Message.printWarning(2,routine,e);
		}
		// Load datastore factory classes.
		try {
			pluginDataStoreFactoryList1 = pcl.loadDataStoreFactoryClasses();
		}
		catch ( ClassNotFoundException e ) {
			Message.printWarning(2,routine,"Error loading datastore factory plugin classes (" + e + ")." );
			Message.printWarning(2,routine,e);
		}
		// For now require that one datastore class and one datastore class factory are loaded for each datastore.
		// Add the plugin datastore class to the list.
		if ( pluginDataStoreList1 == null ) {
			Message.printWarning(2,routine,"Null datastore for plugin Jar \"" + pluginJar + "\" - skipping plugin datastore." );
		}
		else {
			if ( pluginDataStoreList1.size() != 1 ) {
				// This may be due to dependencies so don't print to the log file.
				if ( Message.isDebugOn ) {
					Message.printWarning(2,routine,"Datastore plugin list size (" + pluginDataStoreList1.size() +
						") is not size of 1 for Jar \"" + pluginJar + "\" - skipping plugin." );
				}
			}
			else {
				// Add to the list to be known to TSTool.
				pluginDataStoreList.addAll(pluginDataStoreList1);
			}
		}
		// Add the plugin datastore factory class to the list.
		if ( pluginDataStoreFactoryList1 == null ) {
			Message.printWarning(2,routine,"Null datastore factory for plugin Jar \"" + pluginJar + "\" - skipping plugin datastore factory." );
		}
		else {
			if ( pluginDataStoreFactoryList1.size() != 1 ) {
				// Probably a dependency jar so don't print to the log file.
				if ( Message.isDebugOn ) {
					Message.printWarning(2,routine,"Datastore plugin factory list size (" + pluginDataStoreFactoryList1.size() +
						") is not size of 1 for Jar \"" + pluginJar + "\" - skipping plugin." );
				}
			}
			else {
				// Add to the list to be known to TSTool.
				pluginDataStoreFactoryList.addAll(pluginDataStoreFactoryList1);
			}
		}

		// Add the plugin command class to the list.
		// Use a class loader to load the class file.
		@SuppressWarnings("rawtypes")
		List<Class> pluginCommandList1 = null;
		// Load command classes.
		try {
			// TODO smalers 2020-08-03 for now use method in the datastore class loader.
			//PluginCommandClassLoader pcl2 = new PluginCommandClassLoader ( dataStoreJarURLs );
			//pluginCommandList1 = pcl2.loadCommandClasses();
			pluginCommandList1 = pcl.loadCommandClasses();
		}
		catch ( ClassNotFoundException e ) {
			Message.printWarning(2,routine,"Error loading command plugin classes (" + e + ")." );
			Message.printWarning(2,routine,e);
		}
		finally {
			/* FIXME SAM 2016-04-03 Try not closing class loader because it is needed for other classes in the plugin.
		 	* The compiler may show as a warning as a memory leak but it needs to be around throughout the runtime.
			try {
				pcl.close();
			}
			catch ( IOException e ) {
				// For now swallow - not sure what else to do.
			}
			*/
		}
		if ( pluginCommandList1 == null ) {
			Message.printWarning(2,routine,"Null plugin command list for plugin Jar \"" + pluginJar + "\" - skipping plugin commands." );
		}
		else {
			// Add to the list to be known to TSTool:
			// - multiple commands can be associated with a plugin jar file
			// - other plugins may also add to the list
			if ( pluginCommandList1.size() > 0 ) {
				Message.printStatus(2,routine,"Plugin command list for plugin Jar \"" + pluginJar + "\" includes " +
					pluginCommandList1.size() + " commands." );
			}
			pluginCommandList.addAll(pluginCommandList1);
		}
	}
}

/**
Start the main application instance.
@param args Command line arguments.
*/
public static void main ( String args[] ) {
	String routine = TSToolMain.class.getSimpleName() + ".main";

	try {
	// Main try.

	// Turn on POI (Excel integration) logging, used for troubleshooting:
	// - see:  https://poi.apache.org/components/logging.html
	// - TODO smalers 2019-10-07 Need to enable in limited way to troubleshoot
	//System.setProperty("org.apache.poi.util.POILogger", "org.apache.poi.util.CommonsLogger" );

	// TSTool session properties are a singleton
	IOUtil.setProgramData ( PROGRAM_NAME, PROGRAM_VERSION, args ); // Do first, needed by session to find local files, plugins, etc.
	JGUIUtil.setAppNameForWindows("TSTool");
	//System.err.println("Program version: " + IOUtil.getProgramVersion());
	//System.err.println("Program major version: " + getMajorVersion());

	// Initialize logging levels before the log file is opened:
	// - this only sets the levels and will result in messages to the console
	initializeLoggingLevelsBeforeLogOpened();

	// The first time the following is called the major version is saved in the session.
	// Subsequent calls without the version will use the saved version.
	TSToolSession session = TSToolSession.getInstance(getMajorVersion());

	setWorkingDirInitial ();

	// Set up handler for GUI event queue, for exceptions that may otherwise get swallowed by a JRE launcher.
	new MessageEventQueue();

	// Initialize the logging levels after the lot file is opened:
	// - note that messages will not be printed to the log file until the log file is opened below
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

	// Set the application icon to the CDSS logo by default.
    // Do not do this in pure batch mode because it is not needed and may cause problems with X-Windows on UNIX.
	// Do need to load it when --nomaingui is used because the windows that are shown will need to look nice with the icon.

	Message.printStatus ( 2, routine, "isBatch=" + IOUtil.isBatch() + " --nomaingui specified = " + __noMainGUIArgSpecified );
	Message.printStatus ( 2, routine, "Is TSTool running in headless mode = " + GraphicsEnvironment.isHeadless() );

	if ( !IOUtil.isBatch() || __noMainGUIArgSpecified || !isBatchServer() ) {
	    // Not "pure" batch so need to have the icon initialized.
	    try {
	        setIcon ( "CDSS" );
	    }
	    catch ( Exception e ) {
	        // FIXME SAM 2008-08-29 Why doesn't the above work on Linux in batch mode to avoid trying?
	        Message.printWarning( 2, routine, "Error setting icon graphic." );
	        Message.printWarning( 3, routine, e );
	    }
	}

	// Read the data units.

	initializeAfterHomeIsKnown ();

	Message.printStatus ( 1, routine, "Setup completed.  showmain = " + __showMainGUI + " isbatch=" + IOUtil.isBatch() );

	// Load plugin datastore classes.

	@SuppressWarnings("rawtypes")
	List<Class> pluginDataStoreClasses = new ArrayList<>();
	@SuppressWarnings("rawtypes")
	List<Class> pluginDataStoreFactoryClasses = new ArrayList<>();
	List<Class> pluginCommandClasses = new ArrayList<>();
	try {
		loadPluginDataStores(session, pluginDataStoreClasses, pluginDataStoreFactoryClasses, pluginCommandClasses);
	}
	catch ( Throwable e ) {
		Message.printWarning ( 1, routine, "Error loading plugin datastores.  See log file for details." );
		Message.printWarning ( 1, routine, e );
	}
	Message.printStatus(2, routine, "Loaded " + pluginDataStoreClasses.size() + " plugin datastore classes for all datastore jars.");
	Message.printStatus(2, routine, "Loaded " + pluginDataStoreFactoryClasses.size() + " plugin datastore factory classes for all datastore jars.");
	Message.printStatus(2, routine, "Loaded " + pluginCommandClasses.size() + " plugin command classes for all datastore jars.");

	// Load plugin command classes:
	// - TODO smalers 2020-07-25 - these are now determined when loading datastore plugins, above

	// @SuppressWarnings("rawtypes")
	// List<Class> pluginCommandClasses = loadPluginCommands(session);

	// Run TSTool in the run mode indicated by command line parameters.

	if ( IOUtil.isBatch() ) {
		trackUsage ( "batch" );
		// Running like "tstool --commandfile" (possibly with --nomaingui).
		TSCommandFileRunner runner = new TSCommandFileRunner(processorProps, pluginCommandClasses);
		// If the global timeout is set, start a thread that will time out when the batch run is complete.
		startTimeoutThread ( getBatchTimeout());
	    // Open the HydroBase connection if the configuration file specifies the information.
		// Do this before reading the command file because commands may try to run discovery during load.
        openHydroBase ( runner.getProcessor() );
        // Open datastores in a generic way if the configuration file specifies the information.
        // Do this before reading the command file because commands may try to run discovery during load.
        openDataStoresAtStartup ( session, runner.getProcessor(), pluginDataStoreClasses, pluginDataStoreFactoryClasses, true );
        // Set datastore substitutes, used later when requesting datastores.
        runner.getProcessor().setDatastoreSubstituteList(datastoreSubstituteList);
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
		// If running with --nomaingui, then plot windows should be displayed and when closed cause the
		// run to end - this should be used with external applications that use TSTool as a plotting tool.
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
                // No special handling of windows since --nomaingui was not not specified.  Just exit.
                quitProgram ( 0 );
            }
            else {
                // Not showing the main GUI.  Exit here if there are no plot windows - otherwise will hang.
            	// If windows are found, let the GUI WindowListener handle when to close the application.
                Frame [] frameArray = Frame.getFrames();
                int size = 0;
                if ( frameArray != null ) {
                    size = frameArray.length;
                }
                boolean openWindowFound = false;    // Are any windows open?  If no, exit.
                // Check for windows that could be open as part of visualization,
                // including the graph, summary, and table windows.
                // If any are visible, then need to wait until the user closes all.
                // Then WindowClosing will be called since no more windows are shown.
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
                // If no open window was found quit.  Otherwise let the TSToolBatchWindowListener handle the close.
                if ( !openWindowFound ) {
                    Message.printStatus(2,routine, "No open, visible windows detected.  Exiting.");
                    quitProgram ( 0 );
                }
            }
		}
		catch ( Exception e ) {
			// Some type of error.
			Message.printWarning ( 1, routine, "Error running command file \"" + getCommandFile() + "\"." );
			Message.printWarning ( 1, routine, e );
			quitProgram ( 1 );
		}
	}
	else if ( isBatchServer() ) {
		String batchServerHotFolder0 = getBatchServerHotFolder();
		trackUsage ( "batchserver" );
		Message.printStatus ( 1, routine, "Starting in batch server mode with hot folder \"" + batchServerHotFolder0 + "\"" );
		// TODO SAM 2016-02-09 For now keep code here but may make more modular.
		// Create a runner that will be re-used.
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
		// Create a processor using initial command line properties for the processor.
		TSCommandFileRunner runner = new TSCommandFileRunner(processorProps, pluginCommandClasses);
		// Open the HydroBase connection if the configuration file specifies the information.
		// Do this before reading the command file because commands may try to run discovery during load.
        openHydroBase ( runner.getProcessor() );
        // Open datastores in a generic way if the configuration file specifies the information.
        // Do this before reading the command file because commands may try to run discovery during load.
        openDataStoresAtStartup ( session, runner.getProcessor(), pluginDataStoreClasses, pluginDataStoreFactoryClasses, true );
        File f = null;
		String commandFileFull = "";
	    boolean runDiscoveryOnLoad = false;
	    boolean doStop = false;
	    long sleep = 50;
        while ( true ) {
        	Thread.sleep(sleep); // Do this to keep the loop from eating up a lot of CPU.
        	// Wait for configured wait time between processing.
        	// Look for files in the batch server hot folder.
        	List<File> files = IOUtil.getFilesMatchingPattern(batchServerHotFolder0, "*", true);
        	//Message.printStatus(1,routine,"Have " + files.size() + " files in hot folder.");
        	// TODO SAM 2016-02-08 need to sort so oldest file processed first.
        	// Also perhaps need to check for a command like "End()" to make sure file is complete from copy into hot folder.
        	for ( int i = 0; i < files.size(); i++ ) {
        		f = files.get(i);
        		// Make sure the file exists and is readable.
        		Message.printStatus(1,routine,"Processing command file \"" + f.getAbsolutePath() + "\".");
        		if ( f.exists() && f.canRead() ) {
    				commandFileFull = f.getAbsolutePath();
        			String filename = f.getName();
        			// Special actions based on command file name.
        			if ( filename.equalsIgnoreCase("stop") ) {
        				doStop = true;
        				break;
        			}
        			if ( !commandFileFull.toUpperCase().endsWith(".TSTOOL") ) {
        				// Not a command file so don't process.
        				continue;
        			}
        			// Open the command file.
        			try {
        			    Message.printStatus( 1, routine, "Running command file in batch server mode:  \"" + commandFileFull + "\"" );
        				runner.readCommandFile ( commandFileFull, runDiscoveryOnLoad );
        			}
        			catch ( Exception e ) {
        				Message.printWarning ( 1, routine, "Error reading command file \"" + commandFileFull + "\".  Unable to run commands." );
        				Message.printWarning ( 1, routine, e );
        				continue;
        			}
        			// Run the command file.
        			try {
        			    // The following will throw an exception if there are any errors running.
        	            runner.runCommands();
        			}
        			catch ( Exception e ) {
        				Message.printWarning ( 1, routine, "Error running command file \"" + commandFileFull + "\"." );
        				Message.printWarning ( 1, routine, e );
        			}
        			// Remove the command file.
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
		// Do something simple for now to test.
		trackUsage ( "httpserver" );
		int port = 8000;
		HttpServer server = HttpServer.create(new InetSocketAddress(port),0);
		String root = "/tstool";
		server.createContext(root, new UrlHandler());
		server.setExecutor(null);
		server.start();
	}
	else if ( isRestServer() ) {
		// Run in server mode using REST API.
		trackUsage ( "restservelet" );
		runRestletServer();
	}
	else {
		// Run the UI:
		// - the processor for the UI is created in the called code
		Message.printStatus ( 2, routine, "Starting TSTool UI..." );
		trackUsage ( "ui" );
		try {
            __tstool_JFrame = new TSTool_JFrame (
            	session,
            	getCommandFile(),
            	getRunOnLoad(),
            	pluginDataStoreClasses,
            	pluginDataStoreFactoryClasses,
            	pluginCommandClasses,
            	processorProps,
            	datastoreSubstituteList,
            	titleMod);
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
This is called by openDataStoresAtStartup, which processes all the datastores.
Open a single datastore given its configuration properties.
The datastore is also added to the processor (if the open fails then the datastore should set status=1).
The TSTool configuration file properties are checked here to ensure that the datastore type is enabled.
Otherwise, opening datastores takes time and impacts performance.
@param session TSTool session, which provides user and environment information
@param dataStoreProps datastore configuration properties recognized by the datastore factory "create" method.
@param processor time series command processor that will use/manage the datastore
@param pluginDataStoreClassList list of plugin datastore classes to be loaded dynamically
@param isBatch indicate whether running in batch mode - if true, do not open datastores with login of "prompt"
*/
protected static DataStore openDataStore ( TSToolSession session, PropList dataStoreProps,
	TSCommandProcessor processor, @SuppressWarnings("rawtypes") List<Class> pluginDataStoreClassList,
	@SuppressWarnings("rawtypes") List<Class> pluginDataStoreFactoryClassList, boolean isBatch )
throws ClassNotFoundException, IllegalAccessException, InstantiationException, Exception {
    String routine = TSToolMain.class.getSimpleName() + ".openDataStore";
    // Open the datastore depending on the type.
    String dataStoreType = dataStoreProps.getValue("Type");
    String dataStoreName = dataStoreProps.getValue("Name");
    String dataStoreConfigFile = dataStoreProps.getValue("DataStoreConfigFile");
    Message.printStatus(2,routine,"DataStoreConfigFile=\"" + dataStoreConfigFile + "\"");
    // For now hard-code this here.
    // TODO SAM 2010-09-01 Make this more elegant.
    String packagePath = ""; // Make sure to include trailing period below.
    // TODO SAM 2016-03-25 Need to figure out how software feature set can be generically disabled without coding name here.
    // Similar checks are done in the TSTool UI to enable/disable UI features.
    String propValue = null; // From software installation configuration.
    String userPropValue = null; // From user configuration.
	//Class pluginDataStoreClass = null; // Will be used if a plugin.
    @SuppressWarnings("rawtypes")
	Class pluginDataStoreFactoryClass = null; // Will be used if a plugin.

    // Check the command like parameters for enabling and disabling datastores
    // by using the dataStoreEnabledChecker.
    if ( dataStoreEnabledChecker.size() > 0 ) {
    	// Have --disable-datastores and/or --enable-datastores command line parameters.
    	if ( dataStoreEnabledChecker.isDataStoreEnabled ( dataStoreName ) ) {
			Message.printStatus(2, routine, "Datastore \"" + dataStoreName +
				"\" is enabled after evaluating " + dataStoreEnabledChecker.size() +
				" --disable-datastores and --enable-datastores command parameters.  Will check configuration file." );
    	}
    	else {
    		// Datastore is disabled so don't need to do further checking.
			Message.printStatus(2, routine, "Datastore \"" + dataStoreName +
				"\" is disabled after evaluating --disable-datastores and --enable-datastores command parameters." );
    		return null;
    	}
    }
    else {
    	if ( disableDatastoresMessageCount == 0 ) {
    		Message.printStatus(2, routine,
			  		"Command line parameters --disable-datastores and --enable-datastores were not specified." );
    		++disableDatastoresMessageCount;
    	}
    }

    if ( dataStoreType.equalsIgnoreCase("ColoradoHydroBaseRestDataStore") ) {
        propValue = getPropValue("TSTool.ColoradoHydroBaseRestEnabled");
    	userPropValue = session.getUserConfigPropValue ( "ColoradoHydroBaseRestEnabled" );
    	if ( (userPropValue != null) && !userPropValue.isEmpty() ) {
    		propValue = userPropValue;
    	}
        if ( (propValue != null) && propValue.equalsIgnoreCase("True") ) {
        	// HydroBase REST web services are enabled.
            packagePath = "cdss.dmi.hydrobase.rest.";
        }
        else if ( (propValue == null) || propValue.isEmpty() ) {
        	Message.printStatus(2,routine,"ColoradoHydroBaseRestEnabled system property is null or empty.");
        }
        else {
        	Message.printStatus(2,routine,"ColoradoHydroBaseRestEnabled system property is false.");
        }
    }
    else if ( dataStoreType.equalsIgnoreCase("GenericDatabaseDataStore") ) {
        // No need to check whether enabled or not since a generic connection.
        // Specific configuration files will indicate if enabled.
    	// TODO SAM 2016-02-19 Need to move to more appropriate path.
        packagePath = "riverside.datastore.";
    }
    else if ( dataStoreType.equalsIgnoreCase("HydroBaseDataStore") ) {
        propValue = getPropValue("TSTool.HydroBaseEnabled");
    	userPropValue = session.getUserConfigPropValue ( "HydroBaseEnabled" );
    	if ( (userPropValue != null) && !userPropValue.isEmpty() ) {
    		propValue = userPropValue;
    	}
        if ( (propValue != null) && propValue.equalsIgnoreCase("True") ) {
            packagePath = "DWR.DMI.HydroBaseDMI.";
        }
    }
    else if ( dataStoreType.equalsIgnoreCase("NrcsAwdbDataStore") ) {
        propValue = getPropValue("TSTool.NrcsAwdbEnabled");
    	userPropValue = session.getUserConfigPropValue ( "NrcsAwdbEnabled" );
    	if ( (userPropValue != null) && !userPropValue.isEmpty() ) {
    		propValue = userPropValue;
    	}
        if ( (propValue != null) && propValue.equalsIgnoreCase("True") ) {
            packagePath = "rti.tscommandprocessor.commands.nrcs.awdb.";
        }
    }
    else if ( dataStoreType.equalsIgnoreCase("RccAcisDataStore") ) {
        propValue = getPropValue("TSTool.RCCACISEnabled");
    	userPropValue = session.getUserConfigPropValue ( "RCCACISEnabled" );
    	if ( (userPropValue != null) && !userPropValue.isEmpty() ) {
    		propValue = userPropValue;
    	}
        if ( (propValue != null) && propValue.equalsIgnoreCase("True") ) {
            packagePath = "rti.tscommandprocessor.commands.rccacis.";
        }
    }
    else if ( dataStoreType.equalsIgnoreCase("ReclamationHDBDataStore") ) {
        propValue = getPropValue("TSTool.ReclamationHDBEnabled");
    	userPropValue = session.getUserConfigPropValue ( "ReclamationHDBEnabled" );
    	if ( (userPropValue != null) && !userPropValue.isEmpty() ) {
    		propValue = userPropValue;
    	}
        if ( (propValue != null) && propValue.equalsIgnoreCase("True") ) {
            packagePath = "rti.tscommandprocessor.commands.reclamationhdb.";
        }
    }
    else if ( dataStoreType.equalsIgnoreCase("ReclamationPiscesDataStore") ) {
        propValue = getPropValue("TSTool.ReclamationPiscesEnabled");
    	userPropValue = session.getUserConfigPropValue ( "ReclamationPiscesEnabled" );
    	if ( (userPropValue != null) && !userPropValue.isEmpty() ) {
    		propValue = userPropValue;
    	}
        if ( (propValue != null) && propValue.equalsIgnoreCase("True") ) {
            packagePath = "rti.tscommandprocessor.commands.reclamationpisces.";
        }
    }
    else if ( dataStoreType.equalsIgnoreCase("UsgsNwisDailyDataStore") ) {
        propValue = getPropValue("TSTool.UsgsNwisDailyEnabled");
    	userPropValue = session.getUserConfigPropValue ( "UsgsNwisDailyDataStore" );
    	if ( (userPropValue != null) && !userPropValue.isEmpty() ) {
    		propValue = userPropValue;
    	}
        if ( (propValue != null) && propValue.equalsIgnoreCase("True") ) {
            packagePath = "rti.tscommandprocessor.commands.usgs.nwis.daily.";
        }
    }
    else if ( dataStoreType.equalsIgnoreCase("UsgsNwisGroundwaterDataStore") ) {
        propValue = getPropValue("TSTool.UsgsNwisGroundwaterEnabled");
    	userPropValue = session.getUserConfigPropValue ( "UsgsNwisGroundwaterEnabled" );
    	if ( (userPropValue != null) && !userPropValue.isEmpty() ) {
    		propValue = userPropValue;
    	}
        if ( (propValue != null) && propValue.equalsIgnoreCase("True") ) {
            packagePath = "rti.tscommandprocessor.commands.usgs.nwis.groundwater.";
        }
    }
    else if ( dataStoreType.equalsIgnoreCase("UsgsNwisInstantaneousDataStore") ) {
        propValue = getPropValue("TSTool.UsgsNwisInstantaneousEnabled");
    	userPropValue = session.getUserConfigPropValue ( "UsgsNwisInstantaneousEnabled" );
    	if ( (userPropValue != null) && !userPropValue.isEmpty() ) {
    		propValue = userPropValue;
    	}
        if ( (propValue != null) && propValue.equalsIgnoreCase("True") ) {
            packagePath = "rti.tscommandprocessor.commands.usgs.nwis.instantaneous.";
        }
    }
    else if ( dataStoreType.equalsIgnoreCase("WaterOneFlowDataStore") ) {
        propValue = getPropValue("TSTool.WaterOneFlowEnabled");
    	userPropValue = session.getUserConfigPropValue ( "WaterOneFlowEnabled" );
    	if ( (userPropValue != null) && !userPropValue.isEmpty() ) {
    		propValue = userPropValue;
    	}
        if ( (propValue != null) && propValue.equalsIgnoreCase("True") ) {
            packagePath = "rti.tscommandprocessor.commands.wateroneflow.ws.";
        }
    }
    else {
    	// Try to load plugin by matching the datastore type with the datastore class name.
    	boolean loaded = false;
		// Loop through the datastore classes to find which has a name that matches the datastore type in configuration file.
    	String dataStoreFactory = dataStoreType + "Factory";
		for ( @SuppressWarnings("rawtypes") Class c : pluginDataStoreFactoryClassList ) {
			String nameFromClass = c.getSimpleName(); // Something like DatabaseXDataStoreFactory.
			if ( Message.isDebugOn ) {
				Message.printStatus(2, routine, "Checking plugin datastore config file \"" +
					dataStoreConfigFile + "\" for configuration datastore type \"" + nameFromClass + "\"");
			}
			if ( nameFromClass.equals(dataStoreFactory) ) {
				// Construction will occur by the datastore factory below but need the package name.
				packagePath = c.getPackage().toString() + "."; // Actually have problems with this.
				pluginDataStoreFactoryClass = c; // Use directly below.
				loaded = true;
        		break;
			}
		}
    	if ( !loaded ) {
    		Message.printStatus(2, routine, "Checking plugin datastores for \"" + dataStoreType +
    			"\".  Did not find datastore factory \"" + dataStoreFactory + "\".");
	        throw new InvalidParameterException("Datastore type \"" + dataStoreType +
	            "\" did not have factory class in plugins - cannot initialize datastore connection." );
    	}
    }
    if ( !packagePath.isEmpty() ) {
        boolean isEnabled = true; // Default is datastore is enabled.
        // Also check the command like parameters for enabling and disabling datastores.
        /*
        isEnabled = dataStoreEnabledChecker.isDataStoreEnabled ( dataStoreName );
        Message.printStatus(2, routine,
        	"After checking enabled/disabled datastores from command line, datastore \"" + dataStoreName
        	+ "\" is: " + isEnabled);
        if ( !isEnabled ) {
            // Datastore is disabled.  Do not even attempt to load.  This will minimize in-memory resource use.
            Message.printStatus(2, routine, "Created datastore type \"" + dataStoreType + "\", name \"" +
                dataStoreProps.getValue("Name") + "\" is disabled.  Not opening." );
            return null;
        }
        */
        // Datastore is OK to load:
        // - check the datastore configuration file's "Enabled" property and if "True", continue with the load
        // - otherwise don't attempt to load the datastore
        propValue = dataStoreProps.getValue("Enabled");
        if ( (propValue != null) && propValue.equalsIgnoreCase("False") ) {
        	// The datastore is disabled so don't load the plugin
        	isEnabled = false;
        }
        if ( isEnabled ) {
            // Datastore is enabled so construct the instance.
            StopWatch sw = new StopWatch();
            sw.start();
            String className = packagePath + dataStoreType + "Factory";
            DataStoreFactory factory = null;
            if ( pluginDataStoreFactoryClass != null ) {
            	// This works for plugins.
	            try {
		    		@SuppressWarnings("unchecked")
					Constructor<?> constructor = pluginDataStoreFactoryClass.getConstructor();
		    		Object dataStoreFactory = constructor.newInstance();
		    		// The object must be a DataStore if it follows implementation requirements.
		    		factory = (DataStoreFactory)dataStoreFactory;
		    	}
		    	catch ( NoSuchMethodException e ) {
		    		Message.printWarning(2,routine,"Error getting constructor for plugin datastore class \"" + className + "\" (" + e + ")");
		    	}
		    	catch ( IllegalAccessException e ) {
		    		Message.printWarning(2,routine,"Error creating instance of command for plugin datastore class \"" + className + "\" (" + e + ")");
		    	}
		    	catch ( InstantiationException e ) {
		    		Message.printWarning(2,routine,"Error creating instance of command for plugin datastore class \"" + className + "\" (" + e + ")");
		    	}
		    	catch ( InvocationTargetException e ) {
		    		Message.printWarning(2,routine,"Error creating instance of command for plugin datastore class \"" + className + "\" (" + e + ")");
		    	}
            }
            else {
            	// Plugin datastore class was not determined above.
                Message.printStatus(2, routine, "Getting class for name \"" + className + "\"" );
                @SuppressWarnings("rawtypes")
				Class clazz = Class.forName( className );
                Message.printStatus(2, routine, "Creating instance of class \"" + className + "\"" );
            	factory = (DataStoreFactory)clazz.newInstance();
            }
        	// Check for a login of "prompt".
        	String systemLogin = dataStoreProps.getValue("SystemLogin");
        	String systemPassword = dataStoreProps.getValue("SystemPassword");
            if ( ((systemLogin != null) && systemLogin.equalsIgnoreCase("prompt")) ||
            	((systemPassword != null) && systemPassword.equalsIgnoreCase("prompt"))	) {
                // If in batch mode, skip.
            	if ( isBatch ) {
                    Message.printStatus(2, routine, "Skipping datastore \"" + dataStoreType + "\", name \"" +
                        dataStoreProps.getValue("Name") + "\" because in batch mode.  Will prompt for login when not in batch mode." );
                    return null;
            	}
            	else {
            		// Not in batch mode - open the datastore using a prompt initiated from the TSTool UI.
            		if ( factory instanceof DataStoreConnectionUIProvider ) {
        	            // Create the datastore instance using the properties in the configuration file
            			// supplemented by login/password from interactive input.
        	        	// Add to the processor even if it does not successfully open so that UI can show.
        	        	// TODO SAM 2015-02-15 Need to update each factory to handle partial opens.
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
	        	            // Add the datastore to the processor.
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
	            // Create the datastore instance using the properties in the configuration file.
	        	// Add to the processor even if it does not successfully open so that UI can show.
	        	// TODO SAM 2015-02-15 Need to update each factory to handle partial opens.
	            DataStore dataStore = factory.create(dataStoreProps);
	            // Add the datastore to the processor.
	            processor.setPropContents ( "DataStore", dataStore );
	            Message.printStatus(2, routine, "DataStore properties are: " + dataStore.getProperties().toString(","));
	            sw.stop();
	            Message.printStatus(2, routine, "Opening datastore type \"" + dataStoreType + "\", name \"" +
	                dataStore.getName() + "\" took " + sw.getMilliseconds() + " ms" );
	            return dataStore;
            }
        }
        else {
        	// Datastore is not enabled.
    		Message.printStatus(2, routine, "Datastore \"" + dataStoreName + "\" has Enabled=False in configuration file.  Skipping.");
        	return null;
        }
    }
    else {
    	// No package path to load datastore.
   		Message.printWarning(2, routine, "No package path found for datastore \"" + dataStoreName + "\".  Skipping.");
   		Message.printWarning(2, routine, "Check that the configuration file specifies a built-in or plug-in datastore type: " +
   			"\"" + dataStoreConfigFile + "\"." );
        return null;
    }
}

/**
Open the datastores (e.g., database and web service connection(s)) using datastore configuration files.
This method can be called from the UI code to automatically establish database startup database connections.
@param session TSTool session, which provides user and environment information.
@param processor Command processor that will have datastores opened.
@param pluginDataStoreClassList List of plugin datastore classes to be loaded dynamically.
@param isBatch Is the software running in batch mode?  If in batch mode do not open up datastores
that have a login of "prompt".
*/
protected static void openDataStoresAtStartup ( TSToolSession session, TSCommandProcessor processor,
	@SuppressWarnings("rawtypes") List<Class> pluginDataStoreClassList,
	@SuppressWarnings("rawtypes") List<Class> pluginDataStoreFactoryClassList, boolean isBatch ) {
    String routine = TSToolMain.class.getSimpleName() + ".openDataStoresAtStartup";

    // Allow multiple database connections via the new convention using datastore configuration files.
    // The following code processes all datastores.

    // Plugin datastore classes will have been loaded by this time.

	// List configuration files in the 'datastores' folder of the software installation.
	List<String> dataStoreConfigFiles = new ArrayList<String>();
	// First list the cfg files
	String installDatastoresFolder = session.getInstallDatastoresFolder();
	List<File> installConfigFiles = IOUtil.getFilesMatchingPattern(installDatastoresFolder, "cfg", true);
	Message.printStatus(2, routine, "Found " + installConfigFiles.size() +
		" installation datastore *.cfg files in \"" + installDatastoresFolder );
	// Convert to String
	for ( File f : installConfigFiles ) {
		dataStoreConfigFiles.add(f.getAbsolutePath());
	}

    // Also get names of datastore configuration files from configuration files in user's home folder .tstool/N/datastores.
    if ( session.createUserDatastoresFolder(true) ) {
	    String datastoreFolder = session.getUserDatastoresFolder();
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
	    String [] dfs = f.list(ff); // Returns files without leading path.
	    if ( dfs != null ) {
	    	for ( int i = 0; i < dfs.length; i++ ) {
	    		String datastoreFile = datastoreFolder + File.separator + dfs[i];
	    		dataStoreConfigFiles.add(datastoreFile);
	    		Message.printStatus(2, routine, "Found user datastore configuration file: " + datastoreFile );
	    	}
	    }
    }
    // Now open the datastores for found configuration files:
    // - loop backwards since user files were added last
    // - if a duplicate is found, use the user version first
    int nDataStores = dataStoreConfigFiles.size();
    // Datastore names that have been opened, so as to avoid reopening.  User datastores are opened first.
    List<DataStore> openDataStoreList = new ArrayList<>(); // Datastores that have been opened, to avoid re-opening.
    Message.printStatus(2, routine, "Trying to open " + dataStoreConfigFiles.size() + " datastores (first user, then installation configuration files)." );
    for ( int iDataStore = nDataStores - 1; iDataStore >= 0; iDataStore-- ) {
    	String dataStoreFile = dataStoreConfigFiles.get(iDataStore);
        Message.printStatus ( 2, routine, "Start opening datastore using properties in \"" + dataStoreFile + "\".");
        // Read the properties from the configuration file.
        PropList dataStoreProps = new PropList("");
        String dataStoreFileFull = dataStoreFile;
        if ( !IOUtil.isAbsolute(dataStoreFile)) {
            dataStoreFileFull = __tstoolInstallHome + File.separator + "system" + File.separator + dataStoreFile;
        }
        if ( !IOUtil.fileExists(dataStoreFileFull) ) {
            Message.printWarning(3, routine, "  Datastore configuration file \"" + dataStoreFileFull +
                "\" does not exist - not opening datastore." );
        }
        else {
            dataStoreProps.setPersistentName(dataStoreFileFull);
            String dataStoreClassName = "";
            try {
                // Get the properties from the file.
                dataStoreProps.readPersistent();
                // Also assign the configuration file path property to facilitate file processing later
                // (e.g., to locate related files referenced in the configuration file, such as lists of data
                // that are not available from web services).  This is also used for View / Datastores in the UI.
                dataStoreProps.set("DataStoreConfigFile",dataStoreFileFull);
                String dataStoreName = dataStoreProps.getValue("Name");
                String dataStoreType = dataStoreProps.getValue("Type");
                // If the datastore type is no longer supported, skip because it can slow down startup:
                // - need more time to fully remove the code
                if ( dataStoreType.equalsIgnoreCase("ColoradoWaterHBGuestDataStore") ||
                	dataStoreType.equalsIgnoreCase("ColoradoWaterSMSDataStore")) {
               		Message.printStatus(2,routine,"  Datastore \"" + dataStoreName +
               			"\" type \"" + dataStoreType + "\" is obsolete.  Skipping." );
                	continue;
                }
                // See if the datastore name matches one that is already open and if so, ignore it:
                // - ignore because user datastores are opened first
                // - name must match, and must be enabled
                boolean dataStoreAlreadyOpened = false;
                for ( DataStore openDataStore : openDataStoreList ) {
                	String prop = openDataStore.getProperty("Enabled");
                	boolean isEnabled = false;
                	if ( (prop != null) && prop.equalsIgnoreCase("true") ) {
                		isEnabled = true;
                	}
                	if ( openDataStore.getName().equalsIgnoreCase(dataStoreName) && isEnabled ) {
                		// Found a matching datastore.
                		Message.printStatus ( 2, routine, "  Datastore \"" + dataStoreName +
                			"\" matches previous enabled datastore (user datastores are used before system datastores).  Skipping.");
                		dataStoreAlreadyOpened = true;
                		break;
                	}
                }
                if ( dataStoreAlreadyOpened ) {
                	continue;
                }
                DataStore dataStore = openDataStore ( session, dataStoreProps, processor, pluginDataStoreClassList, pluginDataStoreFactoryClassList, isBatch );
                // Save the datastore name so duplicates are not opened.
                if ( dataStore != null ) {
                	// DataStore will be null if disabled or a serious error occurred opening.
                	openDataStoreList.add(dataStore);
                }
                Message.printStatus ( 2, routine, "  Done opening datastore using properties in \"" + dataStoreFile + "\".");
            }
            catch ( ClassNotFoundException e ) {
                Message.printWarning (2,routine, "  Datastore class \"" + dataStoreClassName +
                    "\" is not in the class path - report to software support (" + e + ")." );
                Message.printWarning(2, routine, e);
            }
            catch( InstantiationException e ) {
                Message.printWarning (2,routine, "  Error instantiating datastore for class \"" + dataStoreClassName +
                    "\" - report to software support (" + e + ")." );
                Message.printWarning(2, routine, e);
            }
            catch( IllegalAccessException e ) {
                Message.printWarning (2,routine, "  Datastore for class \"" + dataStoreClassName +
                    "\" needs a no-argument constructor - report to software support (" + e + ")." );
                Message.printWarning(2, routine, e);
            }
            catch ( Exception e ) {
                Message.printWarning (2,routine,"  Error reading datastore configuration file \"" +
                    dataStoreFileFull + "\" - not opening datastore (" + e + ")." );
                Message.printWarning(2, routine, e);
            }
        }
    }

    // TODO SAM 2010-09-01 Transition HydroBase and other datastores here.
}

// TODO SAM 2010-02-03 Evaluate whether non-null HydroBaseDMI return is OK or whether
// should rely on exceptions.
/**
Open the HydroBase connection using the CDSS configuration file information,
when running in batch mode or when auto-connecting in the GUI.
The CDSS configuration file is used to determine
HydroBase server and database name properties to use for the initial connection.
If no configuration file exists, then a default connection is attempted.
@param processor the command processor that needs the (default) HydroBase connection.
@return opened HydroBaseDMI if the connection was made, or null if a problem.
*/
public static HydroBaseDMI openHydroBase ( TSCommandProcessor processor ) {
    String routine = TSToolMain.class.getSimpleName() + ".openHydroBase";
    boolean HydroBase_enabled = false;  // Whether HydroBaseEnabled = true in TSTool configuration file.
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
        // Running in batch mode or without a main GUI so automatically open HydroBase from the CDSS.cfg file information.
        // Get the input needed to process the file.
        String hbcfg = HydroBase_Util.getConfigurationFile();
        PropList props = null;

        if ( IOUtil.fileExists(hbcfg) ) {
            // Use the configuration file to get HydroBase properties.
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
            // Now open the database. This uses the guest login.
        	// If properties were not found, then default HydroBase information will be used.
            HydroBaseDMI hbdmi = new HydroBaseDMI ( props );
            String dbname = props.getValue("DefaultDatabaseName");
            if ( dbname == null ) {
            	dbname = props.getValue("HydroBase.DefaultDatabaseName");
            }
            if ( (dbname != null) && dbname.equalsIgnoreCase("HydroBase") ) {
            	// If the HydroBase name is generic "HydroBase", don't open the database because it will fail
        	   	// because the database name needs to be something like HydroBase_CO_20180529.
            	Message.printStatus(2, routine, "DefaultHydroBaseName=" + dbname );
            	Message.printStatus(2, routine, "Not attempting to open database because generic name will fail in batch mode.");
            	Message.printStatus(2, routine, "Recommend configuring a HydroBase datastore to use in batch mode.");
            }
            else {
            	// A more specific database name is specified, so try to use it.
            	hbdmi.open();
            	List<HydroBaseDMI> hbdmi_Vector = new ArrayList<HydroBaseDMI>(1);
            	hbdmi_Vector.add ( hbdmi );
            	processor.setPropContents ( "HydroBaseDMIList", hbdmi_Vector );
            	Message.printStatus(2, routine, "Successfully opened HydroBase connection." );
            	return hbdmi;
            }
        }
        catch ( Exception e ) {
            Message.printWarning ( 1, routine, "Error opening HydroBase.  HydroBase input type features will be disabled." );
            Message.printWarning ( 1, routine, "Equivalent datastore features may be enabled if defined." );
            Message.printWarning ( 3, routine, e );
            return null;
        }
    }
    return null; // Probably will not get here.
}

/**
Open the log file.  This should be done as soon as the application home
directory is known so that remaining information can be captured in the log file.
*/
private static void openLogFile ( TSToolSession session ) {
	String routine = TSToolMain.class.getSimpleName() + ".openLogFile";
	String user = IOUtil.getProgramUser();

	String logFile = null;
	// Default as of 2016-02-18 is to open the log file as $home/.tstool/NN/logs/TSTool_user.log file unless specified on command line.
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
                // Do it again so it goes into the log file.
                Message.printStatus ( 1, routine, "Log file name from -logFile: " + logFile );
			}
			catch (Exception e) {
				Message.printWarning ( 1, routine, "Error opening log file \"" + logFile + "\"");
			}
		}
	}
	else {
		// Get the log file name from the session object...under user home folder.
		if ( session.createUserLogsFolder(true) ) {
			// Log folder already exists or was created, so OK to use.
			logFile = session.getUserLogFile();
			Message.printStatus ( 1, routine, "Log file name from TSTool default: " + logFile );
			try {
                Message.openLogFile ( logFile );
                // Also log for troubleshooting.
                Message.printStatus ( 1, routine, "Log file name from TSTool default: " + logFile );
			}
			catch (Exception e) {
				Message.printWarning ( 1, routine, "Error opening log file \"" + logFile + "\"");
			}
		}
		else {
			Message.printWarning ( 2, routine, "Unable to create/open TSTool log folder \"" + session.getUserLogsFolder() + "\".  Not opening log file.");
		}
	}
    boolean oldWay = false;
    if ( oldWay ) {
    	// TODO SAM 2016-02-19 Remove this if the above logic works for the log file.
	    if ( (__tstoolInstallHome == null) || (__tstoolInstallHome.length() == 0) || (__tstoolInstallHome.charAt(0) == '.')) {
			Message.printWarning ( 2, routine, "Home directory is not defined.  Not opening log file.");
		}
		else {
			// Home folder was specified so create log file in that folder logs folder.
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

/**
Parse command line arguments.
@param session TSToolSession instance to track user and other data
@param args Command line arguments.
*/
public static void parseArgs ( TSToolSession session, String[] args )
throws Exception {
	String routine = TSToolMain.class.getSimpleName() + ".parseArgs", message;
	int pos = 0; // Position in a string.

    // Allow setting of -home via system property "tstool.home".
    // This can be supplied by passing the -Dtstool.home=HOME option to the JVM.
    // The following inserts the passed values into the front of the 'args' array to
	// make sure that the install home can be considered by following parameters.
    if (System.getProperty("tstool.home") != null) {
        String[] extArgs = new String[args.length + 2];
        System.arraycopy(args, 0, extArgs, 2, args.length);
        extArgs[0] = "-home";
        extArgs[1] = System.getProperty("tstool.home");
        args = extArgs;
    }

    // String that will be replaced with a space in command parameters:
    // - used to handle file/path parameters with spaces
    // - for now only implement for parameters that are known to have issue with spaces
    String spaceReplacement = null;
    // Iterate through the command line parameters.
	for ( int i = 0; i < args.length; i++ ) {
		//Message.printStatus(1, routine, "Parsing arg: " + args[i]);
		//System.err.println("Parsing arg: " + args[i]);
		if ( args[i].equalsIgnoreCase("-batchServer") || args[i].equalsIgnoreCase("--batchServer") ) {
			Message.printStatus ( 1, routine, "Will start TSTool in batch server mode." );
			__isBatchServer = true;
		}
		else if ( args[i].equalsIgnoreCase("-batchServerHotFolder") || args[i].equalsIgnoreCase("--batchServerHotFolder") ) {
		    // Batch server hot folder name.
			if ( (i + 1) == args.length ) {
				message = "No argument provided to '-batchServerHotFolder'";
				Message.printWarning(1,routine, message);
				throw new Exception(message);
			}
			i++;
			__batchServerHotFolder = args[i];
		}
		else if ( args[i].equalsIgnoreCase("-commands") || args[i].equalsIgnoreCase("--commands") ) {
		    // Command file name.
			if ((i + 1)== args.length) {
				message = "No argument provided to '" + args[i] + "'";
				Message.printWarning(1,routine, message);
				throw new Exception(message);
			}
			i++;
			String commandFile = parseArgsCheckSpaceReplacement(args[i], spaceReplacement);
			// Setup the application using the command file and indicate running in batch mode.
			setupUsingCommandFile ( commandFile, true );
		}
		else if ( args[i].equalsIgnoreCase("-batchTimeout") || args[i].equalsIgnoreCase("--batchTimeout") ) {
		    // Batch timeout in seconds.
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
		else if (args[i].equalsIgnoreCase("-config") || args[i].equalsIgnoreCase("--config")) {
		    // Configuration file name.
		    // TODO SAM 2011-12-07 Need to allow properties like ${UserHome} to read the configuration file from
		    // a users' home folder, even if TSTool is installed in a central location
            if ( (i + 1) == args.length ) {
            	message = "No argument provided to '-config'";
                Message.printWarning(1,routine,message);
                throw new Exception(message);
            }
            i++;
			String configFile = parseArgsCheckSpaceReplacement(args[i], spaceReplacement);
            setConfigFile ( IOUtil.verifyPathForOS(IOUtil.getPathUsingWorkingDir(configFile)) );
            Message.printStatus(1 , routine, "Using configuration file from command line: \"" + getConfigFile() + "\"" );
            // Read the configuration file specified here.  If not specified, the defaults read immediately
            // after -home is parsed will be reset if in both files.
            readConfigFile(getConfigFile());
	    }
		else if ( args[i].regionMatches(true,0,"-d",0,2) || args[i].regionMatches(true, 0, "--debug", 0,7)) {
			// Set debug information.
			if ( (i + 1) == args.length ) {
				// No argument.  Turn terminal and log file debug on to level 1.
				Message.isDebugOn = true;
				Message.setDebugLevel ( Message.TERM_OUTPUT, 1);
				Message.setDebugLevel ( Message.LOG_OUTPUT, 1);
			}
			i++;
			if ( ((i + 1) == args.length) && (args[i].indexOf(",") >= 0) ) {
				// Comma, set screen and file debug to different levels.
				String token = StringUtil.getToken(args[i],",",0,0);
				if ( StringUtil.isInteger(token) ) {
					Message.isDebugOn = true;
					Message.setDebugLevel (	Message.TERM_OUTPUT, StringUtil.atoi(token) );
				}
				token = StringUtil.getToken(args[i],",",0,1);
				if ( StringUtil.isInteger(token) ) {
					Message.isDebugOn = true;
					Message.setDebugLevel (	Message.LOG_OUTPUT, StringUtil.atoi(token) );
				}
			}
			else if ( (i + 1) == args.length ) {
			    // No comma.  Turn screen and log file debug on to the requested level.
				if ( StringUtil.isInteger(args[i]) ) {
					Message.isDebugOn = true;
					Message.setDebugLevel (	Message.TERM_OUTPUT, StringUtil.atoi(args[i]) );
					Message.setDebugLevel ( Message.LOG_OUTPUT,	StringUtil.atoi(args[i]) );
				}
			}
		}
		else if ( args[i].toLowerCase().startsWith("--datastore-substitute") ) {
			// Used to substitute one datastore name for another, used in testing when need to point at a different
			// datastore without changing command files:
			// - example: --substitute-datastore=DatastoreNameToUse,DatastoreNameInCommands
			//   where DatastoreNameToUse is the original (actual) and DatastoreNameInCommands is a substitute that
			//   is in commands
			pos = args[i].indexOf("=");
			int pos2 = args[i].indexOf(",");
			if ( (pos >= 0) && (pos2 >= 0) ) {
				// Have well-formed parameter.
				String [] parts = args[i].substring(pos + 1).split(",");
				String datastoreToUse = parts[0].trim();
				String datastoreInCommands = parts[1].trim();
				Message.printStatus ( 1, routine, "Will use datastore \"" + datastoreToUse + "\" for \"" +
					datastoreInCommands + "\" in commands that use datastores." );
				datastoreSubstituteList.add(new DataStoreSubstitute(datastoreToUse, datastoreInCommands));
			}
			else {
				Message.printWarning(1, routine, "Bad parameter \"" + args[i] +
					"\", should be: --datastore-substitute=DatastoreNameToUse,DatastoreNameInCommands");
			}
		}
		else if ( args[i].toLowerCase().startsWith("--disable-datastores") ) {
			// Used to disable datastores when running in batch mode:
			// - example: --disable-datastores=datastore1,datastore2
			pos = args[i].indexOf("=");
			if ( pos >= 0 ) {
				// Have well-formed parameter.
				dataStoreEnabledChecker.addParameter(args[i]);
			}
			else {
				Message.printWarning(1, routine, "Bad parameter \"" + args[i] +
					"\", should be: --disable-datastores=datastore1,datastore2");
			}
		}
		else if ( args[i].toLowerCase().startsWith("--enable-datastores") ) {
			// Used to enable datastores when running in batch mode:
			// - example: --enable-datastores=datastore1,datastore2
			pos = args[i].indexOf("=");
			if ( pos >= 0 ) {
				// Have well-formed parameter.
				dataStoreEnabledChecker.addParameter(args[i]);
			}
			else {
				Message.printWarning(1, routine, "Bad parameter \"" + args[i] +
					"\", should be: --enable-datastores=datastore1,datastore2");
			}
		}
		else if ( args[i].equalsIgnoreCase("-h") || args[i].equalsIgnoreCase("-help") || args[i].equalsIgnoreCase("--help") ) {
			// Print usage.
			printUsage();
			quitProgram(0);
		}
		else if ( args[i].equalsIgnoreCase("-home") || args[i].equalsIgnoreCase("--home") ) {
		    // Should be specified in batch file or script that runs TSTool, or in properties for a executable launcher.
			// Therefore this should be processed before any user command line
	        // parameters and the log file should open up before much else is done.
			if ((i + 1)== args.length) {
				message = "No argument provided to '-home'";
				Message.printWarning(1,routine,message);
				throw new Exception(message);
			}
			i++;
			String homeFolder = parseArgsCheckSpaceReplacement(args[i], spaceReplacement);

            //Changed __home since old way wasn't supporting relative paths __home = args[i];
            __tstoolInstallHome = (new File(homeFolder)).getCanonicalPath().toString();

			// Open the log file so that remaining messages will be seen in the log file.
			openLogFile(session);
			Message.printStatus ( 1, routine, "TSTool install folder from -home command line parameter is \"" +
			    __tstoolInstallHome + "\"" );
			// The default configuration file location is relative to the install home.
			// This works as long as the -home argument is first in the command line.
			setConfigFile ( __tstoolInstallHome + File.separator + "system" + File.separator + "TSTool.cfg" );
			// Don't call setProgramWorkingDir or setLastFileDialogDirectory this since we set to user.dir at startup.
			//IOUtil.setProgramWorkingDir(__home);
			//JGUIUtil.setLastFileDialogDirectory(__home);
			IOUtil.setApplicationHomeDir(__tstoolInstallHome);
			// TODO SAM 2016-02-22:
			// - see http://fahdshariff.blogspot.be/2011/08/changing-java-library-path-at-runtime.html
			// - cannot change the path at runtime
			// - trying some solutions when loading HEC-DSS libraries in static code and will remove following if it works
			String javaLibraryPath = System.getProperty ( "java.library.path" );
			Message.printStatus( 2, routine, "java.library.path at startup: \"" + javaLibraryPath + "\"" );
	        // Read the configuration file to get default TSTool properties,
            // so that later command-line parameters can override them.
			// If any other command line arguments are -config, then skip the following because a user-specified
			// command file will be read instead.
			boolean userSpecifiedConfig = false;
			for ( int iarg2 = 0; iarg2 < args.length; iarg2++ ) {
			    if ( args[iarg2].equalsIgnoreCase("-config") || args[iarg2].equalsIgnoreCase("--config") ) {
			        userSpecifiedConfig = true;
			        break;
			    }
			}
			if ( !userSpecifiedConfig ) {
			    Message.printStatus(1 , routine, "No user configuration file.  Using installation configuration file: \"" + getConfigFile() + "\"" );
			    readConfigFile(getConfigFile());
			}
		}
		else if ( args[i].equalsIgnoreCase("-httpServer") || args[i].equalsIgnoreCase("--httpServer") ) {
			Message.printStatus ( 1, routine, "Will start TSTool in HTTP server mode." );
			__isHttpServer = true;
		}
		else if ( args[i].equalsIgnoreCase("-logFile") || args[i].equalsIgnoreCase("--logFile") ) {
		    // Specify the log file.
			if ((i + 1)== args.length) {
				message = "No argument provided to '-logFile'";
				Message.printWarning(1,routine,message);
				throw new Exception(message);
			}
			i++;
			String logFile = parseArgsCheckSpaceReplacement(args[i], spaceReplacement);
			__logFileFromCommandLine = logFile;
		}
        else if ( args[i].equalsIgnoreCase("-nodiscovery") || args[i].equalsIgnoreCase("--nodiscovery") ) {
            // Don't run commands in discovery mode on initial load (should only be used in large batch runs).
            Message.printStatus ( 1, routine, "Will process command file without main GUI (plot windows only)." );
            __runDiscoveryOnLoad = false;
        }
		else if ( args[i].equalsIgnoreCase("-nomaingui") || args[i].equalsIgnoreCase("--nomaingui") ) {
			// User specified or specified by a script/system call to the normal TSTool script/launcher.
			// Don't make the main GUI visible...
			Message.printStatus ( 1, routine, "Will process command file without main GUI (plot windows only)." );
			__showMainGUI = false;
			__noMainGUIArgSpecified = true;
		}
        else if ( args[i].equalsIgnoreCase("-runcommandsonload") || args[i].equalsIgnoreCase("--runcommandsonload") ) {
        	// User specified or specified by a script/system call to the normal TSTool script/launcher.
            Message.printStatus ( 1, routine, "Will run commands on load." );
            __run_commands_on_load = true;
        }
		else if ( args[i].equalsIgnoreCase("-server") || args[i].equalsIgnoreCase("--server") ) {
			// User specified or specified by a script/system call to the normal TSTool script/launcher.
			Message.printStatus ( 1, routine, "Will start TSTool in restlet server mode." );
			__isRestletServer = true;
		}
		else if ( args[i].toLowerCase().startsWith("--space-replacement") ) {
			// Used to substitute spaces in command parameters with a sequence, for example multiple underscores.
			// datastore without changing command files.
			// --substitute-datastore=oldDatastore,newDatastore
			pos = args[i].indexOf("=");
			if ( pos >= 0 ) {
				// Have well-formed parameter.
				spaceReplacement = args[i].substring(pos+1).trim();
				System.out.println("Will substitute a space for \"" + spaceReplacement + "\" in command parameters.");
				Message.printStatus ( 1, routine, "Will substitute a space for \"" + spaceReplacement + "\" in command parameters." );
			}
			else {
				Message.printWarning(1, routine, "Bad parameter \"" + args[i] + "\", should be: --space-replacement=string");
			}
		}
		else if ( args[i].equalsIgnoreCase("-test") || args[i].equalsIgnoreCase("--test") ) {
			// User specified (generally by developers).
			IOUtil.testing(true);
			Message.printStatus ( 1, routine, "Running in test mode." );
		}
		else if ( args[i].toLowerCase().startsWith("--ui-titlemod") ) {
			// Provide information to include in the title to help differentiate TSTool sessions that may be running.
			pos = args[i].indexOf("=");
			if ( (pos >= 0) && (args[i].length() > (pos + 1)) ) {
				titleMod = args[i].substring(pos+1).trim();
			}
			else {
				Message.printWarning(1, routine, "Bad parameter \"" + args[i] + "\", should be: --ui-titlemod=text");
			}
		}
		else if ( args[i].equalsIgnoreCase("-verbose") || args[i].equalsIgnoreCase("--verbose") ) {
			// Used to run verbose mode to see which classes are loaded by Java:
			// - this is handled in the 'tstool' bash script so ignore here
		}
		else if ( args[i].equalsIgnoreCase("-v") || args[i].equalsIgnoreCase("-version") || args[i].equalsIgnoreCase("--version") ) {
			// Print version.
			printVersion();
			quitProgram(0);
		}
		else if ( args[i].indexOf("==") > 0 ) {
			// User specified command processor property:
			// - check before single equal sign
			// - these properties will be initialized in the processor when running a command file
			// - a command line argument of the form:  Property==Value
			// - for example, specify a web service root URL for testing
			String arg = parseArgsCheckSpaceReplacement(args[i], spaceReplacement);
		    pos = arg.indexOf("==");
			String propname = arg.substring(0,pos);
			String propval = arg.substring(pos+2);
			Prop prop = new Prop ( propname, propval );
			System.err.println("Setting processor startup parameter " + propname + "==\"" + propval + "\"" );
			Message.printStatus ( 1, routine, "Setting processor startup  parameter " + propname + "==\"" + propval + "\"" );
			prop.setHowSet ( Prop.SET_AT_RUNTIME_BY_USER );
			processorProps.set ( prop );
			/* TODO smalers 2021-08-02 Thought I needed this but found a way to pass parameters without globals:
			 * - remove when tested out
			// Also set in a global application list that can be accessed in deep code such as RunCommands command.
			PropList appProps = IOUtil.getPropListManager().getPropList("TSTool.CommandLine");
			if ( appProps == null ) {
				appProps = new PropList("TSTool.CommandLine");
				appProps.set(prop);
				IOUtil.getPropListManager().addList(appProps, true);
			}
			*/
		}
		else if ( args[i].indexOf("=") > 0 ) {
			// User specified TSTool application property:
			// - specified by a script/system call to the normal TSTool script/launcher
			// - a command line argument of the form:  Property=Value
			// - for example, specify a database login for batch mode
		    // - the properties can be interpreted by the GUI or other code
			String arg = parseArgsCheckSpaceReplacement(args[i], spaceReplacement);
		    pos = arg.indexOf("=");
			String propname = arg.substring(0,pos);
			String propval = arg.substring(pos+1);
			Prop prop = new Prop ( propname, propval );
			System.err.println( "Setting application startup parameter " + propname + "=\"" + propval + "\"" );
			Message.printStatus ( 1, routine, "Setting application startup parameter " + propname + "=\"" + propval + "\"" );
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
		    // by a double-click on a file with a *.TSTool or *.tstool extension.
			// In this case the GUI will start and load the command file.
			String commandFile = parseArgsCheckSpaceReplacement(args[i], spaceReplacement);
			// Setup the application using the command file and indicate NOT running in batch mode.
		    setupUsingCommandFile ( commandFile, false );
		}
	}
}

/**
 * Utility method to check for space replacement string.
 * For example use --space-replacement=___ to replace command parameter substring '___' with a space ' '.
 * This is used to work around command parameters that are difficult to protect with quotes, etc.
 * @param arg command parameter argument to process
 * @param spaceReplacement if not null, a string that will be replaced with a single space
 */
private static String parseArgsCheckSpaceReplacement(String arg, String spaceReplacement) {
	if ( spaceReplacement == null ) {
		// Return the original string.
		return arg;
	}
	else {
		return arg.replace(spaceReplacement, " ");
	}
}

/**
Print the program usage to the log file.
*/
public static void printUsage ( ) {
	String nl = System.getProperty ( "line.separator" );
	String routine = TSToolMain.class.getSimpleName() + ".printUsage";
	int len = PROGRAM_NAME.length();
	String format = "%" + len + "." + len + "s";
	String blanks = String.format(format,"");
	String usage =  nl +
	"Usage:  " + PROGRAM_NAME + " [options] [[--commands CommandFile] | CommandFile]" + nl + nl +
	"TSTool displays, analyzes, and manipulates time series." + nl+
	"" + nl+
	PROGRAM_NAME + " --commands CommandFile               Runs the commands in batch mode and exits." + nl+
	PROGRAM_NAME + " --commands CommandFile --nomaingui   Runs the commands in batch mode, displays product windows" + nl +
	blanks + "                                      (no main window), and exists when the window(s) are closed." + nl+
	PROGRAM_NAME + " CommandFile                          Opens the TSTool UI and loads the command file (but does not run it)" + nl +
	blanks + "                                      (this may be used when a *.tstool file is selected in the desktop)." + nl+
	"" + nl+
	"See the TSTool documentation for more information." + nl + nl;
	System.err.println ( usage );
	Message.printStatus ( 1, routine, usage );
}

/**
Print the program version and exit the program.
*/
public static void printVersion ( ) {
	String nl = System.getProperty ( "line.separator" );
	System.err.println (  nl + PROGRAM_NAME + " version: " + PROGRAM_VERSION + nl + nl +
	"TSTool is a part of Colorado's Decision Support Systems (CDSS)\n" +
	"Copyright (C) 1997-2024 Colorado Department of Natural Resources\n" +
    "\n" +
	"TSTool is free software:  you can redistribute it and/or modify\n" +
	"    it under the terms of the GNU General Public License as published by\n" +
	"    the Free Software Foundation, either version 3 of the License, or\n" +
	"    (at your option) any later version.\n" +
	"\n" +
	"TSTool is distributed in the hope that it will be useful,\n" +
	"    but WITHOUT ANY WARRANTY; without even the implied warranty of\n" +
	"    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the\n" +
	"    GNU General Public License for more details.\n" +
	"\n" +
	"You should have received a copy of the GNU General Public License\n" +
	"    along with TSTool.  If not, see <https://www.gnu.org/licenses/>.\n" );
	quitProgram (0);
}

/**
Clean up and quit the program.
@param status Program exit status.
*/
public static void quitProgram ( int status ) {
	String routine = TSToolMain.class.getSimpleName() + ".quitProgram";

	Message.printStatus ( 1, routine, "Exiting with status " + status + "." );

	System.err.print( "STOP " + status + "\n" );
	Message.closeLogFile ();
	System.exit ( status );
}

/**
Read the configuration file.  This should be done as soon as the application home is known.
TODO SAM 2015-01-07 need to store configuration information in a generic "session" object to be developed.
@param configFile Name of the configuration file.
*/
private static void readConfigFile ( String configFile ) {
	String routine = TSToolMain.class.getSimpleName() + ".readConfigFile";
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
                	// Also set global properties that are used more generically.
                	IOUtil.setProp("DiffProgram", prop.getValue());
                }
                else if ( prop.getKey().equalsIgnoreCase("TSTool.DiffProgram.Linux") ) {
                	// Also set global properties that are used more generically.
                	IOUtil.setProp("DiffProgram.Linux", prop.getValue());
                }
                else if ( prop.getKey().equalsIgnoreCase("TSTool.DiffProgram.Windows") ) {
                	// Also set global properties that are used more generically.
                	IOUtil.setProp("DiffProgram.Windows", prop.getValue());
                }
            }
            // Override with user properties:
            // - allow property name to be prefixed with 'TSTool.' or not.
            TSToolSession session = TSToolSession.getInstance();
            String userPropValue = session.getUserConfigPropValue ( "TSTool.DiffProgram" );
            if ( (userPropValue == null) || userPropValue.isEmpty() ) {
            	userPropValue = session.getUserConfigPropValue ( "DiffProgram" );
            }
            if ( (userPropValue != null) && !userPropValue.isEmpty() ) {
               	IOUtil.setProp("DiffProgram", userPropValue );
            }
            userPropValue = session.getUserConfigPropValue ( "TSTool.DiffProgram.Linux" );
            if ( (userPropValue == null) || userPropValue.isEmpty() ) {
            	userPropValue = session.getUserConfigPropValue ( "DiffProgram.Linux" );
            }
            if ( (userPropValue != null) && !userPropValue.isEmpty() ) {
               	IOUtil.setProp("DiffProgram.Linux", userPropValue );
            }
            userPropValue = session.getUserConfigPropValue ( "TSTool.DiffProgram.Windows" );
            if ( (userPropValue == null) || userPropValue.isEmpty() ) {
            	userPropValue = session.getUserConfigPropValue ( "DiffProgram.Windows" );
            }
            if ( (userPropValue != null) && !userPropValue.isEmpty() ) {
               	IOUtil.setProp("DiffProgram.Windows", userPropValue );
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
private static void runRestletServer () {
    String routine = TSToolMain.class.getSimpleName() + ".runRestletServer()";
    try {
        int port = -1; // Default.
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
--commands File parameter.  This indicates that a batch run should be done,
with no main TSTool GUI, although windows may display for graphical products.
*/
private static void setCommandFile ( String configFile ) {
	__commandFile = configFile;
}

/**
Set the configuration file that is being used with TSTool.
If a relative path is given, then the file is made into an absolute path by using the working directory.
Typically an absolute path is provided when the -home command line parameter is parsed at startup,
and a relative path may be provided if -config is specified on the command line.
@param configFile Configuration file.
*/
private static void setConfigFile ( String configFile ) {
    __configFile = configFile;
}

/**
Set the icon for the application.  This will be used for all windows.
@param iconType The icon will be used by searching for
TSToolXXXIcon32.gif (where XXX=iconType), both using a path for the main application.
*/
public static void setIcon ( String iconType ) {
	// First try loading the icon from the JAR file or class path.
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
Setup the application using the specific command file,
which either came in on the command line with "tstool --commands CommandFile" (run batch and exit)
or "tstool CommandFile" (run UI and load the command file).
@param commandFileArg Command file from the command line argument (no processing on the argument before this call).
@param isBatch Indicates if the command file was specified with "--commands CommandFile",
indicating that a batch run is requested.
*/
private static void setupUsingCommandFile ( String commandFileArg, boolean isBatch ) {
    String routine = TSToolMain.class.getSimpleName() + ".setupUsingCommandFile";

    // Make sure that the command file is an absolute path because it indicates the working
    // directory for all other processing.
    String userDir = System.getProperty("user.dir");
    Message.printStatus (1, routine, "Startup (user.dir) directory is \"" + userDir + "\"");
    String commandFileCanonical = null; // Does not need to be absolute.
    File commandFileFile = new File(commandFileArg);
    File commandFileFullFile = null;
    String commandFileFull = null;
    try {
        commandFileCanonical = commandFileFile.getCanonicalPath();
        Message.printStatus( 1, routine, "Canonical path for command file is \"" + commandFileCanonical + "\"" );
    }
    catch ( Exception e ) {
        String message = "Unable to determine canonical path for \"" + commandFileArg + "\"." +
        "Check that the file exists and read permissions are granted.  Not using command file.";
        Message.printWarning ( 1, routine, message );
        System.err.println ( message );

        return;
    }

    // Get the absolute path to the command file.
    // TODO SAM 2008-01-11 Shouldn't a canonical path always be absolute?
    File commandFileCanonicalFile = new File ( commandFileCanonical );
    if ( commandFileCanonicalFile.isAbsolute() ) {
        commandFileFull = commandFileCanonical;
    }
    else {
        // Append the command file to the user directory and set the working directory to the resulting directory.
        commandFileFull = userDir + File.separator + commandFileFull;
    }

    // Save the full path to the command file so it can be processed when the GUI initializes.
    // TODO SAM 2007-09-09 Evaluate phasing global command file out - needs to be handled in the command processor.
    Message.printStatus ( 1, routine, "Command file is \"" + commandFileFull + "\"" );
    // FIXME SAM 2008-09-04 Confirm no negative effects from taking this out.
    //IOUtil.setProgramCommandFile ( command_file_full );
    setCommandFile ( commandFileFull );

    setWorkingDirUsingCommandFile ( commandFileFull );

    commandFileFullFile = new File ( commandFileFull );
    if ( !commandFileFullFile.exists() ) {
        String message = "Command file \"" + commandFileFull + "\" does not exist.";
        Message.printWarning(1, routine, message );
        System.err.println ( message );
        if ( isBatch ) {
             // Exit because there is nothing to do.
            quitProgram ( 1 );
        }
        else {
            // In GUI mode, go ahead and start up but there will be more warnings about no file to read.
        }
    }

    // Indicate whether running in batch mode:
    // - if so TSTool will run and exit
    IOUtil.isBatch ( isBatch );
}

/**
Set the working directory as the system "user.dir" property.
*/
private static void setWorkingDirInitial() {
    String routine = TSToolMain.class.getSimpleName() + ".setWorkingDirInitial";
    String working_dir = System.getProperty("user.dir");
    IOUtil.setProgramWorkingDir ( working_dir );
    // Set the dialog because if the running in batch mode and interaction with the graph occurs,
    // this default for dialogs should be the home of the command file.
    JGUIUtil.setLastFileDialogDirectory( working_dir );
    String message = "Setting working directory to user directory \"" + working_dir +"\".";
    Message.printStatus ( 1, routine, message );
    System.err.println(message);
}

/**
Set the working directory as the parent of the command file.
@param commandFileFull the full (absolute) path to the command file
*/
private static void setWorkingDirUsingCommandFile ( String commandFileFull ) {
    File commandFileFull_File = new File ( commandFileFull );
    String workingDir = commandFileFull_File.getParent();
    IOUtil.setProgramWorkingDir ( workingDir );
    // Set the dialog because if the running in batch mode and interaction with the graph occurs,
    // this default for dialogs should be the home of the command file.
    JGUIUtil.setLastFileDialogDirectory( workingDir );
    // Print at level 1 because the log file is not yet initialized.
    String message = "Setting working directory to command file folder \"" + workingDir + ".\"";
    //Message.printStatus ( 1, routine, message );
    System.err.println(message);
}

// TODO SAM 2015-12-14 This does not seem to work as intended:
// - the Excecutor service itself is not a thread and so does not return immediately
// - what will happen if -nomaingui is used with a thread timeout
// - probably need timeout on start-up but not once graph is displayed
/**
Start the timeout thread, which will exit TSTool if it is not finished within the timeout.
This is needed in cases where something in the code hangs and TSTool never exits.
Ideally those situations can be handled with granular timeouts but that is sometimes not possible
due to limitations in the packages that are called.
@param timeoutSeconds number of seconds before timing out (ignore if <= 0)
*/
private static void startTimeoutThread ( int timeoutSeconds ) {
	String routine = TSToolMain.class.getSimpleName() + ".startTimeoutThread";
	if ( timeoutSeconds <= 0 ) {
		// No need to start timeout thread.
		return;
	}
	ExecutorService executor = Executors.newSingleThreadExecutor();
	Future<String> future = executor.submit(new SleepTask(timeoutSeconds));
	try {
		Message.printStatus(2, routine, "Starting thread to time-out TSTool if not done after " + timeoutSeconds + " seconds.");
		// Actually the task that is run above should sleep the number of seconds so specifying the seconds below is redundant.
		future.get(timeoutSeconds,TimeUnit.SECONDS);
		Message.printStatus(2, routine, "Exiting TSTool after waiting " + timeoutSeconds + " seconds.");
		quitProgram(1); // TODO SAM 2015-02-14 need to evaluate a standard set of exit codes.
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
                // Put in some protection against injection by checking for keywords other than SELECT.
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

/**
 * Do a GET request on the OpenCDSS website to cause Google Analytics to track a TSTool usage.
 * TODO smalers 2023-03-30 This needs more work.
 * See:  https://stackoverflow.com/questions/75890133/google-analytics-4-for-desktop-java-application
 * @param runMode the TSTool run mode, to understand how TSTool is being called
 */
private static void trackUsage ( String runMode ) {
	String routine = TSToolMain.class.getSimpleName() + ".trackUsage";

	boolean doTracking = false;
	if ( !doTracking ) {
		// TODO smalers 2023-03-30 figure out a good solution for tracking.
		return;
	}

	// Operating system.
	String os = "unknown";
	if ( IOUtil.isUNIXMachine() ) {
		// This is not totally right but lump them together.
		os = "linux";
	}
	else {
		os = "windows";
	}

	// Get the version (e.g., 1.2.3).
	String [] versionParts = TSToolMain.PROGRAM_VERSION.split(" ");
	String version = versionParts[0].trim();

	String usageUrl = "https://opencdss.state.co.us/tstool/" + version + "/usage/index.html?version=" + version + "&os=" + os;

	// Do a get to use Google Analytics:
	// - use a short timeout so that this does not adversely impact TSTool startup time
	// - set to follow redirects but only temporarily
	boolean oldfollowRedirects = false; // Will be set in the try to the current value.
	StopWatch sw = new StopWatch();
	sw.start();
	try {
		URL url = new URL(usageUrl);
		// Save so can set back to the default when done.
		oldfollowRedirects = HttpURLConnection.getFollowRedirects();
		HttpURLConnection con = (HttpURLConnection)url.openConnection();
		// Follow redirects in case the documentation hosting location moves.
		HttpURLConnection.setFollowRedirects(true);
		int timeoutMs = 100;
		con.setConnectTimeout(timeoutMs);
		con.setReadTimeout(timeoutMs);
		// Open the connection.
		con.connect();
		int responseCode = con.getResponseCode();
		if ( responseCode != 200 ) {
			Message.printWarning(3,routine,"Error (" + responseCode
				+ ") getting tracking page. Unable to update TSTool tracking. Assume that network is locked.");
		}
		else {
			// Assume that the GET worked.
		}
	}
	catch ( MalformedURLException e ) {
		Message.printWarning(2, "", "Unable to display documentation at \"" + usageUrl + "\" - malformed URL." );
	}
	catch ( IOException e ) {
		Message.printWarning(2, "", "Unable to display documentation at \"" + usageUrl + "\" - IOException (" + e + ")." );
	}
	catch ( Exception e ) {
		Message.printWarning(2, "", "Unable to display documentation at \"" + usageUrl + "\" - Exception (" + e + ")." );
	}
	finally {
		// Reset back to the normal default
		HttpURLConnection.setFollowRedirects(oldfollowRedirects);
	}
	sw.stop();
	Message.printStatus(2, routine, "Took " + sw.getMilliseconds() + "ms to update tracking.");
}

}

/**
Class used to run a timeout task, which is optional to end TSTool when running in batch mode.
*/
class SleepTask implements Callable<String> {
	/**
	Number of seconds the task should sleep before timing out.
	*/
	private int sleepSeconds = 0;

	/**
	Construct with the timeout.
	*/
	public SleepTask ( int sleepSeconds ) {
		this.sleepSeconds = sleepSeconds;
	}

	/**
	Callable task.
	*/
	@Override
	public String call () throws Exception {
		Thread.sleep(this.sleepSeconds*1000); // Task will wait the timeout and then return.
		return "Slept " + sleepSeconds;
	}
}