// TSToolSession - Class to maintain TSTool session information such as the history of command files opened.

/* NoticeStart

TSTool
TSTool is a part of Colorado's Decision Support Systems (CDSS)
Copyright (C) 1994-2023 Colorado Department of Natural Resources

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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import RTi.Util.IO.IOUtil;
import RTi.Util.IO.PropList;
import RTi.Util.Message.Message;

/**
Class to maintain TSTool session information such as the history of command files opened.
Class to maintain TSTool session information such as the history of command files opened.
A singleton instance should be retrieve using the getInstance() method.
*/
public class TSToolSession
{

/**
Global value that indicates if the command file history is being written.
Need to handle because if the file is being modified at the same time exceptions will be thrown.
*/
private boolean historyBeingWritten = false;

/**
Global value that indicates if the UI state file is being written.
Need to handle because if the file is being modified at the same time exceptions will be thrown.
*/
private boolean uiStateBeingWritten = false;

/**
 * List of properties for the UI state, such as last selections in wizards, choices, etc.
 */
private PropList uiStateProps = new PropList("ui-state");

/**
 * Private singleton instance.
 * Instance is created in getInstance().
 */
private static TSToolSession instance = null;

/**
 * Major software version, used for folder below .tstool/, for example .tstool/13.
 * This is initialized as a parameter to the constructor.
 */
private int majorVersion = 0; // 0 will be an obvious error if a folder is created.

/**
Private constructor for the session instance.
@param majorVersion the major version of TSTool, necessary because user files are organized by TSTool version.
*/
private TSToolSession ( int majorVersion ) {
	// Read UI state properties so they are available for interaction.
	// They will be written when TSTool closes, and at other intermediate points, as appropriate,
	// by calling writeUIState().
	this.majorVersion = majorVersion;
	readUIState();
}

/**
 * Check that the history file exists.
 * If it does not, copy from the previous version(s) of TSTool if it exists.
 * @return true if the history file exists, false if an issue creating/finding the file.
 */
private boolean checkHistoryFile() {
	String routine = getClass().getSimpleName() + ".checkHistoryFile";
	String historyFile = getHistoryFile();
	//Message.printStatus(2, routine, "History file is \"" + historyFile + "\"");
	File f = new File(historyFile);
	// Folder should match the major version.
	File folder = f.getParentFile();
	if ( !folder.exists() ) {
		// Create the folder for the history file.
		Message.printStatus(2,routine,"Creating folder for history file \"" + folder + "\"" );
		if ( !folder.mkdirs() ) {
			// Unable to make folder.
			Message.printWarning(2,routine,"Error creating folder for history file \"" + folder + "\"" );
			return false;
		}
	}
	// If here the folder for the history file exists so can check for the file.
	// If the file does not exist, see if one exists in a previous version of the software and copy it:
	// - this is OK because currently the format has not changed since the original
	for ( int iVersion = getMajorVersion() - 1; iVersion >= 12; iVersion-- ) {
		if ( !f.exists() ) {
			// The history file for the current version does not exist so check for an old version:
			// - only go back a couple of major versions because the format may change
			String historyFileOld = getHistoryFile(iVersion);
			File f2 = new File(historyFileOld);
			if ( f2.exists() ) {
				// Old history file exists so can copy.
				Path original = Paths.get(historyFileOld);
				Path copy = Paths.get(historyFile);
				try {
					Message.printStatus(2,routine, "Copying history file:");
					Message.printStatus(2,routine, "  from: " + original);
					Message.printStatus(2,routine, "    to: " + copy);
					Files.copy(original, copy);
					break;
				}
				catch ( IOException e ) {
					Message.printWarning(2,routine,"Error copying old history file:");
					Message.printStatus(2,routine, "  from: " + original);
					Message.printStatus(2,routine, "    to: " + copy);
				}
			}
		}
	}
	// If the file still does not exist, create an empty file.
	if ( !f.exists() ) {
		// The following will get overwritten by writeHistory()
		StringBuilder sb = new StringBuilder ( "# TSTool command file history, most recent at top, shared between similar TSTool major version" );
		try {
			IOUtil.writeFile ( f.getPath(), sb.toString() );
			Message.printStatus(2,routine,"Create empty history file \"" + f + "\"" );
		}
		catch ( IOException e ) {
			// For now absorb.
			Message.printWarning(2,routine,"Error creating empty history file \"" + f + "\"" );
			return false;
		}
	}
	return true;
}

/**
 * Check that the UI state file exists.
 * If it does not, copy from the previous version(s) of TSTool if it exists.
 * @return true if the UI state file exists, false if an issue creating/finding the file.
 */
private boolean checkUIStateFile() {
	String routine = getClass().getSimpleName() + ".checkUIStateFile";
	String uiStateFile = getUIStateFile();
	Message.printStatus(2, routine, "UI state file is \"" + uiStateFile + "\"");
	File f = new File(uiStateFile);
	File folder = f.getParentFile();
	if ( !folder.exists() ) {
		// Create the folders for the UI state file.
		Message.printStatus(2,routine,"Creating folder for user state file \"" + folder + "\"" );
		if ( !folder.mkdirs() ) {
			// Unable to make folder.
			Message.printWarning(2,routine,"Error creating folder for UI state file \"" + folder + "\"" );
			return false;
		}
	}
	// If here the folder for the UI state file exists so can check for the file.
	// If the file does not exist, see if one exists in a previous version of the software and copy it:
	// - this is OK because currently the format has not changed since the original
	for ( int iVersion = getMajorVersion() - 1; iVersion >= 12; iVersion-- ) {
		if ( !f.exists() ) {
			// The UI state file does not exist so check for an old version.
			String uiStateFileOld = getUIStateFile(iVersion);
			File f2 = new File(uiStateFileOld);
			if ( f2.exists() ) {
				Path original = Paths.get(uiStateFileOld);
				Path copy = Paths.get(uiStateFile);
				try {
					Message.printStatus(2,routine, "Copying UI state file:");
					Message.printStatus(2,routine, "  from: " + original);
					Message.printStatus(2,routine, "    to: " + copy);
					Files.copy(original, copy);
					break;
				}
				catch ( IOException e ) {
					Message.printWarning(2,routine,"Error copying old UI state file:");
					Message.printStatus(2,routine, "  from: " + original);
					Message.printStatus(2,routine, "    to: " + copy);
				}
			}
		}
	}
	// If the file still does not exist, create an empty file.
	if ( !f.exists() ) {
		// The following will get overwritten by writeHistory().
		StringBuilder sb = new StringBuilder ( "# TSTool UI state" );
		try {
			IOUtil.writeFile ( f.getPath(), sb.toString() );
		}
		catch ( IOException e ) {
			// For now absorb.
			return false;
		}
	}
	return true;
}

/**
Create a new TSTool configuration file in user files.
This is used when transitioning from TSTool earlier than 11.09.00 to version later.
@return true if the file was created, false for all other cases.
*/
public boolean createUserConfigFile ( ) {
	String userTstoolFolder = getUserTstoolFolder();
	if ( userTstoolFolder.equals("/") || userTstoolFolder.equals("/root") ) {
		// Don't allow files to be created under root on Linux.
		Message.printWarning(3, "TSToolSession.createUserConfigFile",
			"Unable to create user files in '" + userTstoolFolder +
			"' (root) folder - need to run TSTool as a non-root user.");
		return false;
	}

	// Create the configuration folder if necessary.
	File f = new File(getUserConfigFile());
	File folder = f.getParentFile();
	if ( !folder.exists() ) {
		if ( !folder.mkdirs() ) {
			// Unable to make folder.
			return false;
		}
	}
	try {
		String nl = System.getProperty("line.separator");
		StringBuilder sb = new StringBuilder ( "# TSTool configuration file containing user settings, shared between TSTool versions" + nl );
		sb.append("# This file indicates which datastore software features should be enabled." + nl );
		sb.append("# Disabling datastore types that are not used can improve TSTool performance and simplifies the user interface." + nl );
		sb.append("# Refer to the TSTool.cfg file under the software installation folder for global configuration properties." + nl );
		sb.append("# User settings in this file will override the installation settings." + nl );
		sb.append(nl);
		// Include a line for HydroBase since it often needs to be disabled on computers where HydroBase is not used.
		sb.append("HydroBaseEnabled = true" + nl );
		IOUtil.writeFile ( f.getPath(), sb.toString() );
		return true;
	}
	catch ( Exception e ) {
		return false;
	}
}

/**
Create the user's datastores folder if necessary.
This folder is handled separately because more specific logic may need to be implemented.
@return true if datastores folder exists and is writable, false otherwise.
*/
public boolean createUserDatastoresFolder ( boolean copyPreviousVersion ) {
	String routine = getClass().getSimpleName() + ".createUserDatastoresFolder";
	String datastoresFolder = getUserDatastoresFolder();
	// Do not allow the datastores folder to be created under Linux root but allow TSTool to run.
	if ( datastoresFolder.equals("/") || datastoresFolder.equals("/root") ) {
		Message.printWarning(3, routine,
			"Unable to create user datastore files in '" + datastoresFolder +
			"' (root) folder - need to run TSTool as a non-root user.");
		return false;
	}
	File f = new File(datastoresFolder);
	if ( !f.exists() ) {
		Message.printStatus(2, routine, "Creating user datastores folder: " + datastoresFolder );
		if ( copyPreviousVersion ) {
			// Copy the datastore files.
			for ( int iVersion = getMajorVersion() - 1; iVersion >= 13; iVersion-- ) {
				if ( !f.exists() ) {
					// The UI state file does not exist so check for an old version:
					// - version 13 was the first version that implemented current user folders
					String datastoresFolderOld = getUserDatastoresFolder(iVersion);
					File f2 = new File(datastoresFolderOld);
					if ( f2.exists() ) {
						Path original = Paths.get(datastoresFolderOld);
						Path copy = Paths.get(datastoresFolder);
						try {
							Message.printStatus(2,routine, "Copying datastores folder:");
							Message.printStatus(2,routine, "  from: " + original);
							Message.printStatus(2,routine, "    to: " + copy);
							List<String> problems = new ArrayList<>();
							IOUtil.copyDirectory(original.toString(), copy.toString(), problems);
							if ( problems.size() > 0 ) {
								Message.printWarning(2,routine,"Errors copying datastores files from previous version:");
								for ( String problem : problems ) {
									Message.printWarning(2,routine,"  " + problem);
								}
							}
							break;
						}
						catch ( IOException e ) {
							Message.printWarning(2,routine,"Error copying datastores folder:");
							Message.printStatus(2,routine, "  from: " + original);
							Message.printStatus(2,routine, "    to: " + copy);
						}
					}
				}
			}
		}
		// File was not created:
		// - just create an empty new folder
		if ( !f.exists() ) {
			try {
				Message.printStatus(2, routine, "Creating empty user datastores folder (files must be copied later): " + datastoresFolder );
				f.mkdirs();
			}
			catch ( SecurityException e ) {
				Message.printWarning(3, routine, "Error creating user datastores folder: " + datastoresFolder);
				return false;
			}
		}
	}
	// Make sure it is writable.
	if ( !f.canWrite() ) {
		return false;
	}
	else {
		return true;
	}
}

/**
Create the user's graph templates folder if necessary.
This folder is handled separately because more specific logic may need to be implemented.
@return true if 'template-graphs' folder exists and is writable, false otherwise.
*/
public boolean createUserGraphTemplatesFolder ( boolean copyPreviousVersion ) {
	String routine = getClass().getSimpleName() + ".createUserGraphTemplatesFolder";
	String templatesFolder = getUserGraphTemplatesFolder();
	// Do not allow the folder to be created under Linux root but allow TSTool to run.
	if ( templatesFolder.equals("/") || templatesFolder.equals("/root") ) {
		Message.printWarning(3, routine,
			"Unable to create user graph template files in '" + templatesFolder +
			"' (root) folder - need to run TSTool as a non-root user.");
		return false;
	}
	File f = new File(templatesFolder);
	if ( !f.exists() ) {
		Message.printStatus(2, routine, "Creating user graph templates folder: " + templatesFolder );
		if ( copyPreviousVersion ) {
			// Copy the graph template files.
			for ( int iVersion = getMajorVersion() - 1; iVersion >= 13; iVersion-- ) {
				if ( !f.exists() ) {
					// The UI state file does not exist so check for an old version:
					// - version 13 was the first version that implemented current user folders
					String templatesFolderOld = getUserGraphTemplatesFolder(iVersion);
					File f2 = new File(templatesFolderOld);
					if ( f2.exists() ) {
						Path original = Paths.get(templatesFolderOld);
						Path copy = Paths.get(templatesFolder);
						try {
							Message.printStatus(2,routine, "Copying graph templates folder:");
							Message.printStatus(2,routine, "  from: " + original);
							Message.printStatus(2,routine, "    to: " + copy);
							List<String> problems = new ArrayList<>();
							IOUtil.copyDirectory(original.toString(), copy.toString(), problems);
							if ( problems.size() > 0 ) {
								Message.printWarning(2,routine,"Errors copying graph templates files from previous version:");
								for ( String problem : problems ) {
									Message.printWarning(2,routine,"  " + problem);
								}
							}
							break;
						}
						catch ( IOException e ) {
							Message.printWarning(2,routine,"Error copying graph template folder:");
							Message.printStatus(2,routine, "  from: " + original);
							Message.printStatus(2,routine, "    to: " + copy);
						}
					}
				}
			}
		}
		// Folder was not created:
		// - just create an empty new folder
		if ( !f.exists() ) {
			try {
				Message.printStatus(2, routine, "Creating empty user graph templates folder (files must be copied later): " + templatesFolder );
				f.mkdirs();
			}
			catch ( SecurityException e ) {
				Message.printWarning(3, routine, "Error creating user graph templates folder: " + templatesFolder);
				return false;
			}
		}
	}
	// Make sure it is writable.
	if ( !f.canWrite() ) {
		return false;
	}
	else {
		return true;
	}
}

/**
Create the "logs" folder if necessary.
This folder is handled separately because more specific logic may need to be implemented.
@return true if "logs" folder exists and is writable, false otherwise.
*/
public boolean createUserLogsFolder ( boolean copyPreviousVersion ) {
	String routine = getClass().getSimpleName() + ".createUserLogsFolder";
	String logsFolder = getUserLogsFolder();
	// Do not allow log file to be created under Linux root but allow TSTool to run.
	if ( logsFolder.equals("/") || logsFolder.equals("/root") ) {
		Message.printWarning(3, routine,
			"Unable to create user log files in '" + logsFolder +
			"' (root) folder - need to run TSTool as a non-root user.");
		return false;
	}
	File f = new File(logsFolder);
	if ( !f.exists() ) {
		try {
			Message.printStatus(2, routine, "Creating user logs folder: " + logsFolder );
			f.mkdirs();
		}
		catch ( SecurityException e ) {
			Message.printWarning(3, routine, "Error creating user logs folder: " + logsFolder);
			return false;
		}
	}
	else {
		// Make sure it is writable.
		if ( !f.canWrite() ) {
			return false;
		}
	}
	return true;
}

/**
Create the user's "plugins" folder if necessary.
This folder is handled separately because more specific logic may need to be implemented.
@return true if "plugins" folder exists and is writable, false otherwise.
*/
public boolean createUserPluginsFolder ( boolean copyPreviousVersion ) {
	String routine = getClass().getSimpleName() + ".createUserPluginsFolder";
	String pluginsFolder = getUserPluginsFolder();
	// Do not allow log file to be created under Linux root but allow TSTool to run.
	if ( pluginsFolder.equals("/") || pluginsFolder.equals("/root") ) {
		Message.printWarning(3, routine,
			"Unable to create user plugin files in '" + pluginsFolder +
			"' (root) folder - need to run TSTool as a non-root user.");
		return false;
	}
	File f = new File(pluginsFolder);
	if ( !f.exists() ) {
		Message.printStatus(2, routine, "Creating user plugins folder: " + pluginsFolder );
		if ( copyPreviousVersion ) {
			// Copy the plugin files.
			for ( int iVersion = getMajorVersion() - 1; iVersion >= 13; iVersion-- ) {
				if ( !f.exists() ) {
					// The UI state file does not exist so check for an old version.
					String pluginsFolderOld = getUserPluginsFolder(iVersion);
					File f2 = new File(pluginsFolderOld);
					if ( f2.exists() ) {
						Path original = Paths.get(pluginsFolderOld);
						Path copy = Paths.get(pluginsFolder);
						try {
							Message.printStatus(2,routine, "Copying plugins folder:");
							Message.printStatus(2,routine, "  from: " + original);
							Message.printStatus(2,routine, "    to: " + copy);
							List<String> problems = new ArrayList<>();
							IOUtil.copyDirectory(original.toString(), copy.toString(), problems);
							if ( problems.size() > 0 ) {
								Message.printWarning(2,routine,"Errors copying plugins files from previous version:");
								for ( String problem : problems ) {
									Message.printWarning(2,routine,"  " + problem);
								}
							}
							break;
						}
						catch ( IOException e ) {
							Message.printWarning(2,routine,"Error copying plugins folder:");
							Message.printStatus(2,routine, "  from: " + original);
							Message.printStatus(2,routine, "    to: " + copy);
						}
					}
				}
			}
		}
		// File was not created:
		// - just create an empty new folder
		if ( !f.exists() ) {
			try {
				Message.printStatus(2, routine, "Creating empty user plugins folder (files must be copied later): " + pluginsFolder );
				f.mkdirs();
			}
			catch ( SecurityException e ) {
				Message.printWarning(3, routine, "Error creating user plugins folder: " + pluginsFolder);
				return false;
			}
		}
	}
	// Make sure it is writable.
	if ( !f.canWrite() ) {
		return false;
	}
	else {
		return true;
	}
}

/**
Create the user system folder if necessary.
This folder is handled separately because more specific logic may need to be implemented.
@return true if system folder exists and is writable, false otherwise.
*/
public boolean createUserSystemFolder ( boolean copyPreviousVersion ) {
	String routine = getClass().getSimpleName() + ".createUserSystemFolder";
	String systemFolder = getUserSystemFolder();
	// Do not allow system folder to be created under Linux root but allow TSTool to run.
	if ( systemFolder.equals("/") || systemFolder.equals("/root") ) {
		Message.printWarning(3, routine,
			"Unable to create user system files in '" + systemFolder +
			"' (root) folder - need to run TSTool as a non-root user.");
		return false;
	}
	File f = new File(systemFolder);
	if ( !f.exists() ) {
		Message.printStatus(2, routine, "Creating user system folder: " + systemFolder );
		if ( copyPreviousVersion ) {
			// Copy the system files.
			for ( int iVersion = getMajorVersion() - 1; iVersion >= 13; iVersion-- ) {
				if ( !f.exists() ) {
					// The UI state file does not exist so check for an old version.
					String systemFolderOld = getUserSystemFolder(iVersion);
					File f2 = new File(systemFolderOld);
					if ( f2.exists() ) {
						Path original = Paths.get(systemFolderOld);
						Path copy = Paths.get(systemFolder);
						try {
							Message.printStatus(2,routine, "Copying system folder:");
							Message.printStatus(2,routine, "  from: " + original);
							Message.printStatus(2,routine, "    to: " + copy);
							List<String> problems = new ArrayList<>();
							IOUtil.copyDirectory(original.toString(), copy.toString(), problems);
							if ( problems.size() > 0 ) {
								Message.printWarning(2,routine,"Errors copying system files from previous version:");
								for ( String problem : problems ) {
									Message.printWarning(2,routine,"  " + problem);
								}
							}
							break;
						}
						catch ( IOException e ) {
							Message.printWarning(2,routine,"Error copying system folder:");
							Message.printStatus(2,routine, "  from: " + original);
							Message.printStatus(2,routine, "    to: " + copy);
						}
					}
				}
			}
		}
		// File was not created:
		// - just create an empty new folder
		if ( !f.exists() ) {
			try {
				Message.printStatus(2, routine, "Creating empty user system folder (files must be copied later): " + systemFolder );
				f.mkdirs();
			}
			catch ( SecurityException e ) {
				Message.printWarning(3, routine, "Error creating user system folder: " + systemFolder);
				return false;
			}
		}
	}
	// Make sure it is writable.
	if ( !f.canWrite() ) {
		return false;
	}
	else {
		return true;
	}
}

// TODO smalers 2021-08-26 need to use the getUserGraphTemplatesFolder method,
// but need to test with Reclamation.
/**
 * Return the the File for the graph template file, for example.
 * <pre>
 * Windows: C:/Users/user/.tstool/13/template-graphs/tspFilename
 * Linux:   $HOME/user/.tstool/13/template-graphs/tspFilename
 * </pre>
 * The user's file location for templates is prepended to the specific file.
 * The folder is plural for TSTool 13 and later, consistent with plurals for "datastores", "plugins", etc..
 * @param tspFilename a *.tsp file, without leading path, one of the items from getGraphTemplateFileList().
 */
public File getGraphTemplateFile ( String tspFilename ) {
	int majorVersion = getMajorVersion();
	if ( majorVersion <= 12 ) {
		return new File(getUserTstoolFolder() + File.separator + "template-graph" + File.separator + tspFilename );
	}
	else {
		// For version 13, migrate to folder with "s":
		// - however, if not found, support the version 12 name
		File templateGraph = new File(getMajorVersionFolder() + File.separator + "template-graphs" + File.separator + tspFilename );
		if ( templateGraph.exists() ) {
			return templateGraph;
		}
		else {
			// Check the version 12 folder:
			// - directly under ./tstool and singular folder
			return new File(getUserTstoolFolder() + File.separator + "template-graph" + File.separator + tspFilename );
		}
	}
}

// TODO smalers 2021-08-26 need to use the getUserGraphTemplatesFolder method,
// but need to test with Reclamation.
/**
Return the list of graph template files.
Template *.tsp files should be in ".tstool/NN/template-graph" (version 13) or
".tstool/template-graph" (version 12).
*/
public List<File> getGraphTemplateFileList ()
{
	String graphTemplateFolder;
	int majorVersion = getMajorVersion();
	if ( majorVersion <= 12 ) {
	    graphTemplateFolder = getUserTstoolFolder() + File.separator + "template-graph";
	    return IOUtil.getFilesMatchingPattern(graphTemplateFolder, "tsp", false);
	}
	else {
	    graphTemplateFolder = getMajorVersionFolder() + File.separator + "template-graphs";
	    List<File> fileList = IOUtil.getFilesMatchingPattern(graphTemplateFolder, "tsp", false);
	    // Also support version 12 folder name for compatibility:
	    // - directly under ./tstool and singular folder
	    graphTemplateFolder = getUserTstoolFolder() + File.separator + "template-graph";
	    List<File> fileList12 = IOUtil.getFilesMatchingPattern(graphTemplateFolder, "tsp", false);
	    fileList.addAll(fileList12);
	    return fileList;
	}
}

/**
Return the name of the TSTool history file.
This exists under the TSTool version folder, used with TSTool 13.x and later.
*/
public String getHistoryFile () {
	return getHistoryFile ( getMajorVersion() );
}

/**
Return the name of the TSTool history file for a TSTool major version.
*/
public String getHistoryFile ( int majorVersion ) {
	String historyFile = "";
	if ( majorVersion <= 12 ) {
		// History file exists in: ./tstool
		historyFile = getUserTstoolFolder() + File.separator + "command-file-history.txt";
	}
	else {
		// History file exists in, for example:  ./tstool/13/
		historyFile = getUserTstoolFolder() + File.separator + majorVersion + File.separator + "command-file-history.txt";
	}
	//Message.printStatus(1,"","History file \"" + historyFile + "\"");
	return historyFile;
}

/**
Return the folder where TSTool is installed, for example:
<ul>
<li>	Windows:  C:\CDSS\TSTool-Version</li>
<li>	Linux: /opt/TSTool-Version</li>
</ul>
*/
public String getInstallFolder () {
	String installFolder = IOUtil.getApplicationHomeDir();
	return installFolder;
}

/**
Return the folder for TSTool datastore configuration files, in software install folder:
*/
public String getInstallDatastoresFolder () {
	String installFolder = getInstallFolder();
	return installFolder + File.separator + "datastores";
}

/**
Return the folder for TSTool plugin files, a folder named "plugins".
*/
public String getInstallPluginsFolder () {
	String installFolder = getInstallFolder();
	return installFolder + File.separator + "plugins";
}

/**
 * Return the singleton instance of the TSToolSession.
 * This version must be called after the overloaded version that specifies the major version.
 * Otherwise, 0 is set as the major version.
 */
public static TSToolSession getInstance() {
	if ( instance == null ) {
		instance = new TSToolSession( 0 );
	}
	// Else instance is non-null and will be returned:
	// - the following won't do anything if already initialized
	boolean copyPreviousVersion = true;
	instance.initializeUserFiles(instance.getMajorVersion(), copyPreviousVersion);
	return instance;
}

/**
 * Return the singleton instance of the TSToolSession.
 * @param majorVersion the major version of TSTool, necessary because user files are organized by TSTool version.
 */
public static TSToolSession getInstance( int majorVersion ) {
	if ( instance == null ) {
		instance = new TSToolSession( majorVersion );
	}
	// Else instance is non-null and will be returned.
	boolean copyPreviousVersion = true;
	// The following won't do anything if already initialized.
	instance.initializeUserFiles(instance.getMajorVersion(), copyPreviousVersion );
	return instance;
}

/**
 * Return the major software version, used for top-level user files.
 * @return the software major version, used for top-level user files
 */
public int getMajorVersion () {
	return this.majorVersion;
}

/**
Return the folder to the major version:
<ul>
<li>	Windows:  C:\Users\UserName\.tstool\12</li>
<li>	Linux: /home/UserName/.tstool/12</li>
</ul>
*/
public String getMajorVersionFolder () {
	return getMajorVersionFolder(getMajorVersion());
}

/**
Return the folder to the major version:
<ul>
<li>	Windows:  C:\Users\UserName\.tstool\12</li>
<li>	Linux: /home/UserName/.tstool/12</li>
</ul>
*/
public String getMajorVersionFolder (int majorVersion) {
	String majorVersionFolder = getUserTstoolFolder() + File.separator + majorVersion;
	//Message.printStatus(1,"","Major version folder is \"" + majorVersionFolder + "\"");
	return majorVersionFolder;
}

/**
Return the name of the TSTool UI state file.
*/
public String getUIStateFile () {
	return getUIStateFile(getMajorVersion());
}

/**
Return the name of the TSTool UI state file for a TSTool major version.
@param majorVersion the major TSTool version of interest.
*/
public String getUIStateFile ( int majorVersion ) {
	String uiStateFile = "";
	if ( majorVersion <= 12 ) {
		// UI state file exists in, for example:  ./tstool/
		uiStateFile = getUserTstoolFolder() + File.separator + "ui-state.txt";
	}
	else {
		// History file exists in, for example:  ./tstool/13/
		uiStateFile = getUserTstoolFolder() + File.separator + majorVersion + File.separator + "ui-state.txt";
	}
	//Message.printStatus(1,"","UI state file \"" + uiStateFile + "\"");
	return uiStateFile;
}

/**
 * Return a UI state property, as a string.
 * @param propertyName name of property being requested.
 */
public String getUIStateProperty ( String propertyName ) {
	return this.uiStateProps.getValue(propertyName);
}

/**
Return the name of the user's TSTool configuration file.
*/
public String getUserConfigFile () {
	String logFile = getUserSystemFolder() + File.separator + "TSTool.cfg";
	//Message.printStatus(1,"","Config file is \"" + logFolder + "\"");
	return logFile;
}

/**
Return the value of the requested property from the user's TSTool configuration file.
This reads the configuration file each time to ensure synchronization.
@param propName property name
@return the value of the property or null if file or property is not found
*/
public String getUserConfigPropValue ( String propName ) {
	String configFile = getUserConfigFile();
	File f = new File(configFile);
	if ( !f.exists() || !f.canRead() ) {
		return null;
	}
	PropList props = new PropList("TSToolUserConfig");
	props.setPersistentName(configFile);
	try {
		props.readPersistent();
		return props.getValue(propName);
	}
	catch ( Exception e ) {
		return null;
	}
}

/**
Return the name of the user's datastore configuration folder.
@return the "datastores" folder path (no trailing /).
*/
public String getUserDatastoresFolder () {
	return getUserDatastoresFolder ( getMajorVersion() );
}

/**
Return the name of the user's datastore configuration folder.
@return the "datastores" folder path (no trailing /).
*/
public String getUserDatastoresFolder ( int majorVersion ) {
	// 12.06.00 and earlier (not under version folder and singular).
	//String datastoreFolder = getUserFolder() + File.separator + "datastore";
	// 12.07.00 and later (under version folder and plural, which seems more appropriate).
	String datastoresFolder = getMajorVersionFolder(majorVersion) + File.separator + "datastores";
	//Message.printStatus(1,"","Datastores folder is \"" + datastoreFolder + "\"");
	return datastoresFolder;
}

/**
Return the name of the user's graph templates folder.
@return the "template-graphs" folder path (no trailing /).
*/
public String getUserGraphTemplatesFolder () {
	return getUserGraphTemplatesFolder ( getMajorVersion() );
}

/**
Return the name of the user's graph templates folder.
@return the "template-graphs" folder path (no trailing /).
*/
public String getUserGraphTemplatesFolder ( int majorVersion ) {
	if ( majorVersion <= 12 ) {
		// Version 12 folder:
		// - under main .tstool folder
		// - singular
		return getUserTstoolFolder() + File.separator + "template-graph";
	}
	else {
		// Version 13+
		// - in folder for major version
		// - plural "s" similar to other folders
		return getMajorVersionFolder(majorVersion) + File.separator + "template-graphs";
	}
}

/**
Return the name of the log file for the user.
*/
public String getUserLogFile () {
	String logFile = getUserLogsFolder() + File.separator + "TSTool_" + System.getProperty("user.name") + ".log";
	//Message.printStatus(1,"","Log folder is \"" + logFolder + "\"");
	return logFile;
}

/**
Return the name of the user's log file folder.
@return the "logs" folder path (no trailing /).
*/
public String getUserLogsFolder () {
	int majorVersion = getMajorVersion();
	String logsFolder = "";
	if ( majorVersion <= 12 ) {
		// 12.06.00 and earlier (not under version folder and singular).
		logsFolder = getUserTstoolFolder() + File.separator + "log";
	}
	else {
		// 12.07.00 and later (under version folder and plural, which seems more appropriate).
		// 12.07.00 was never released so can check this change as version 13.
		logsFolder = getMajorVersionFolder() + File.separator + "logs";
	}
	//Message.printStatus(1,"","Log folder is \"" + logFolder + "\"");
	return logsFolder;
}

/**
Return the name of the user's plugins configuration folder.
@return the "plugins" folder path (no trailing /).
*/
public String getUserPluginsFolder () {
	return getUserPluginsFolder(getMajorVersion());
}

/**
Return the name of the user's plugins configuration folder.
@param majorVersion the major software version
@return the "plugins" folder path (no trailing /).
*/
public String getUserPluginsFolder ( int majorVersion ) {
	// 12.06.00 and earlier was split into plugin-command and plugin-datastore.
	// 12.07.00 and later (under version folder and plural, which seems more appropriate).
	String pluginsFolder = getMajorVersionFolder(majorVersion) + File.separator + "plugins";
	//Message.printStatus(1,"","Plugins folder is \"" + pluginsFolder + "\"");
	return pluginsFolder;
}

/**
Return the name of the user's system folder.
@return the "system" folder path (no trailing /).
*/
public String getUserSystemFolder () {
	return getUserSystemFolder(getMajorVersion());
}

/**
Return the name of the user's system folder.
@return the "system" folder path (no trailing /).
*/
public String getUserSystemFolder ( int majorVersion ) {
	// 12.06.00 and earlier (not under version folder).
	//String systemFolder = getUserFolder() + File.separator + "system";
	// 12.07.00 and later (under version folder).
	String systemFolder = getMajorVersionFolder(majorVersion) + File.separator + "system";
	//Message.printStatus(1,"","System folder is \"" + systemFolder + "\"");
	return systemFolder;
}

/**
Return the name of the TSTool user folder for the operating system, for example:
<ul>
<li>	Windows:  C:\Users\UserName\.tstool</li>
<li>	Linux: /home/UserName/.tstool</li>
</ul>
*/
public String getUserTstoolFolder () {
	String userFolder = System.getProperty("user.home") + File.separator + ".tstool";
	//Message.printStatus(1,"","User folder is \"" + userFolder + "\"");
	return userFolder;
}

/**
 * Initialize user files.
 * This method should be called at application startup to make sure that user files are created.
 * TSTool 12.06.00 and earlier used the following folder structure, using Windows as example:
 *
 * <pre>
 * C:/Users/user/
 *   .tstool/
 *      datastore/
 *        *.cfg
 *      log/
 *        TSTool_user.log
 *      plugin-command/
 *        SomeCommand/
 *          bin/
 *            SomeCommand-Version.jar
 *          doc/
 *            SomeCommand.html
 *      plugin-datastore/
 *        SomeDatastore/
 *          bin/
 *            SomeDataStore.jar
 * </pre>
 *
 * The above has proven to be problematic for a number of reasons including:
 * 1) strict folder structure is prone to errors (flexible drop-in for jar files is better),
 * 2) TSTool version evolution is prone to breaking
 *
 * Therefore the following alternative is now being implemented in TSTool 12.07.00:
 *
 * <pre>
 * C:/Users/user/
 *   .tstool/
 *      12/
 *        datastores/
 *          somedatastore/ (folder is optional)
 *            somedatastore.cfg
 *        logs/
 *          TSTool-user.log
 *        plugins/
 *          someplugin/ (folder is optional)
 *            someplugin.jar
 *            supporting files
 *      13/
 *        ...
 *      14/
 *        ...
 * </pre>
 *
 * Conventions will be used to manage files but users will be able to organize as they prefer.
 * The jar files can contain datastores and commands in the same jar file so as to minimize duplicate deployment of code.
 * The use of a version folder is a compromise: users will need to use migration tools
 * to import previous version datastore configurations, etc.,
 * but the version folder allows different major versions of TSTool to remain functional if major design changes occur.
 * @param majorVersion the major TSTool version,
 * a parameter to allow calling multiple times for different TSTool versions if necessary.
 * @param copyPreviousVersion if true, copy the nearest previous version's files,
 * if false only create folders
 * @return true if the files were initialized, false for all other cases.
 */
public boolean initializeUserFiles ( int majorVersion, boolean copyPreviousVersion ) {
	String routine = getClass().getSimpleName() + ".initializeUserFiles";
	String userFolder = getUserTstoolFolder();
	if ( userFolder.equals("/") || userFolder.equals("/root") ) {
		// Don't allow files to be created under root on Linux.
		Message.printWarning(3, routine, "Unable to create user files in '" + userFolder +
			"' (root) folder - need to run TSTool as a non-root user.");
		return false;
	}
	// Create the version folder if it does not exist.
	String versionFolder = userFolder + File.separator + majorVersion;
	File f = new File(versionFolder);
	if ( !f.exists() ) {
		Message.printStatus(2, routine, "It looks like this is a new TSTool major version installation.");
		Message.printStatus(2, routine, "Will attempt to copy previous major version's configuration files.");
		try {
			Message.printStatus(2, routine, "Creating TSTool user files version folder \"" + versionFolder + "\".");
			f.mkdirs();
		}
		catch ( SecurityException e ) {
			Message.printWarning(3, routine, "Could not create TSTool user files version folder \"" + versionFolder + "\" (" + e + ").");
			return false;
		}
	}
	else {
		// Make sure it is writeable.
		if ( !f.canWrite() ) {
			Message.printWarning(3, routine, "TSTool user files version folder \"" + versionFolder + "\" is not writeable.");
			return false;
		}
	}
	// Initialize user files under the version folder.
	checkHistoryFile();
	checkUIStateFile();
	createUserDatastoresFolder(copyPreviousVersion);
	createUserGraphTemplatesFolder(copyPreviousVersion);
	createUserLogsFolder(copyPreviousVersion);
	createUserPluginsFolder(copyPreviousVersion);
	createUserSystemFolder(copyPreviousVersion);
	return true;
}

/**
Push a new command file onto the history.  This reads the history, updates it, and writes it.
This is done because if multiple TSTool sessions are running they, will share the history.
@param commandFile full path to command file that has been opened
*/
public void pushHistory ( String commandFile ) {
	// Read the history file from the .tstool-history file.
	List<String> history = readHistory();
	// Add in the first position so it will show up first in the File...Open... menu.
	history.add(0, commandFile);
	// Process from the back so that old duplicates are removed and recent access is always at the top of the list.
	// TODO SAM 2014-12-17 use a TSTool configuration file property to set cap.
	int max = 100;
	String old;
	for ( int i = history.size() - 1; i >= 1; i-- ) {
		old = history.get(i);
		if ( i >= max ) {
			// Trim the history to the maximum
			history.remove(i);
		}
		else if ( old.equals(commandFile) || old.equals("") || old.startsWith("#")) {
			// Ignore comments, blank lines and duplicate to most recent access.
			history.remove(i--);
		}
	}
	//Message.printStatus(2,"", "History length is " + history.size());
	// Write the updated history.
	writeHistory(history);
}

/**
Read the history of command files that have been opened.
@return list of command files recently opened, newest first
*/
public List<String> readHistory() {
	//String routine = getClass().getSimpleName() + ".readHistory";
	try {
		// If the history file does not exist or is zero size,
		// automatically copy from the previous version of TSTool if it exists.
		if ( !checkHistoryFile() ) {
			// History file could not be verified/created so return empty list.
			return new ArrayList<String>();
		}
		List<String> history = IOUtil.fileToStringList(getHistoryFile());
		// Remove comment lines.
		for ( int i = (history.size() - 1); i >= 0; i-- ) {
			String f = history.get(i);
			if ( f.startsWith("#") ) {
				history.remove(i);
			}
		}
		return history;
	}
	catch ( Exception e ) {
		// For now just swallow exception - may be because the history folder does not exist.
		//Message.printWarning(3,routine,e);
		return new ArrayList<String>();
	}
}

/**
Read the UI state.
The UI state is saved as simple property=value text file in the .tstool/ui-state.txt file.
Properties are saved in the uiStateProps PropList internally.
*/
public void readUIState() {
	//String routine = getClass().getSimpleName() + ".readUIState";
	// Check that the UI state file exists in the expected location.
	checkUIStateFile();
	try {
		this.uiStateProps = new PropList("ui-state");
		this.uiStateProps.setPersistentName(getUIStateFile());
		this.uiStateProps.readPersistent();
	}
	catch ( Exception e ) {
		// For now just swallow exception - may be because the UI state file has not been created the first time.
		//Message.printWarning(3,routine,e);
	}
}

/**
 * Set a UI state property.
 * @propertyName name of the state property.
 * @propertyValue value of the property as a string.
 */
public void setUIStateProperty ( String propertyName, String propertyValue ) {
	this.uiStateProps.set(propertyName,propertyValue);
}

/**
Write the history of commands files that have been opened.
*/
private void writeHistory ( List<String> history ) {
	String nl = System.getProperty("line.separator");
	StringBuilder sb = new StringBuilder ( "# TSTool command file history, most recent at top, shared between similar TSTool major version" );

	String userTstoolFolder = getUserTstoolFolder();
	if ( userTstoolFolder.equals("/") || userTstoolFolder.equals("/root") ) {
		// Don't allow files to be created under root on Linux.
		Message.printWarning(3, "TSToolSession.writeHistory",
			"Unable to create user system files in '" + userTstoolFolder +
			"' (root) folder - need to run TSTool as a non-root user.");
		return;
	}

	long ms = System.currentTimeMillis();
	while ( this.historyBeingWritten ) {
		// Need to wait until another operation finishes writing,
		// gut don't wait longer than a couple of seconds before moving on.
		if ( (System.currentTimeMillis() - ms) > 2000 ) {
			break;
		}
	}
	// Now can continue.
	try {

		for ( String s : history ) {
			sb.append(nl + s);
		}
		// If the history file does not exist or is zero size,
		// automatically copy from the previous version of TSTool if it exists.
		if ( !checkHistoryFile() ) {
			return;
		}
		try {
			//Message.printStatus(1, "", "Writing history: " + sb );
			File f = new File(getHistoryFile());
			IOUtil.writeFile ( f.getPath(), sb.toString() );
		}
		catch ( Exception e ) {
			// Absorb exception for now.
		}
	}
	finally {
		// Make sure to do the following so don't lock up.
		this.historyBeingWritten = false;
	}
}

/**
Write the UI state properties.
*/
public void writeUIState () {
	String userTstoolFolder = getUserTstoolFolder();
	if ( userTstoolFolder.equals("/") || userTstoolFolder.equals("/root") ) {
		// Don't allow files to be created under root on Linux.
		Message.printWarning(3, "TSToolSession.writeHistory",
			"Unable to create TSTool UI state file in '" + userTstoolFolder +
			"' (root) folder - need to run TSTool as a non-root user.");
		return;
	}

	// Make sure the UI state file exists.
	checkUIStateFile();

	long ms = System.currentTimeMillis();
	while ( this.uiStateBeingWritten ) {
		// Need to wait until another operation finishes writing,
		// but don't wait longer than a couple of seconds before moving on.
		if ( (System.currentTimeMillis() - ms) > 2000 ) {
			break;
		}
	}
	// Now can continue.
	this.uiStateBeingWritten = true;
	try {
		try {
			//Message.printStatus(1, "", "Writing UI state" );
			this.uiStateProps.writePersistent();
		}
		catch ( Exception e ) {
			// Absorb exception for now.
		}
	}
	finally {
		// Make sure to do the following so don't lock up.
		this.uiStateBeingWritten = false;
	}
}

}