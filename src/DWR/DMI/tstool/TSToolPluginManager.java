// TSToolInstallationManager - class to manage TSTool installations

/* NoticeStart

TSTool
TSTool is a part of Colorado's Decision Support Systems (CDSS)
Copyright (C) 1994-2025 Colorado Department of Natural Resources

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
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import RTi.Util.IO.IOUtil;
import RTi.Util.Message.Message;

/**
 * The TSToolPluginManager class is a singleton that can be created for a TSTool application session
 * to help manage TTool plugins, for example to determine the size of and remove old installations,
 * and to check check which versions are installed to help troubleshoot plugin version issues.
 */
public class TSToolPluginManager {

	/**
	 * Singleton instance.
	 */
	private static TSToolPluginManager instance = null;
	
	/**
	 * Private list of TSTool plugins.
	 */
	private List<TSToolPlugin> plugins = null;
	
	/**
	 * Create an instance of the singleton.
	 */
	private TSToolPluginManager () {
		this.plugins = new ArrayList<>();
	}

	/**
	 * Add a plugin version folder:
	 * <ol>
	 * <li>a version folder will be created at the same level as the folder (under the same parent)</li>
	 * <li>the current installation folder will be renamed to be under the new version folder</li>
	 * </ol>
	 * It is expected that the plugin is not loaded in memory.
	 * @param plugin the plugin instance to update
	 * @param problems a list to add problem messages
	 * @return true if the plugin folder was moved, false if not
	 */
	public boolean addPluginVersionFolder ( TSToolPlugin plugin, List<String> problems ) {
		// Make sure that a version is known.
		String version = plugin.getVersion();
		if ( (version == null) || version.isEmpty() ) {
			if ( problems != null ) {
				problems.add ( "Plugin version is unknown.");
			}
			return false;
		}
		// Create the version folder.
		File parentFolder = plugin.getPluginJarFolder().getParentFile();
		if ( !parentFolder.canWrite() ) {
			problems.add("Cannot create version folder (don't have write permissions): " + parentFolder.getAbsolutePath() );
			return false;
		}
		File versionFolder = new File(parentFolder.getAbsolutePath() + File.separator + plugin.getVersion());
		boolean folderCreated = versionFolder.mkdir();
		if ( !folderCreated ) {
			problems.add("Unable to create version folder: " + versionFolder.getAbsolutePath() );
			return false;
		}
		// Move the exiting folder to under the version folder.
		File newFolder = new File(parentFolder.getAbsolutePath() + File.separator + plugin.getVersion()
			+ File.separator + plugin.getPluginJarFolder().getName() );
		// Use the nio package.
		try {
			Files.move(plugin.getPluginJarFolder().toPath(), newFolder.toPath(), StandardCopyOption.REPLACE_EXISTING);
			
			// Update the plugin information.
			File newPluginJarFile = new File(newFolder.getAbsolutePath() + File.separator + plugin.getPluginJarFile().getName());
			plugin.setPluginJarFile(newPluginJarFile);
			return true;
		}
		catch ( Exception e ) {
			problems.add ( "Failed to move \"" + newFolder.getAbsolutePath() + "\" to \"" + newFolder.getAbsolutePath() + "\"." );
			problems.add ( "Exception: " + e );
			return false;
		}
	}
	
	/**
	 * Delete a plugin installation:
	 * <ol>
	 * <li>the plugin will be deleted from the file system</li>
	 * <li>the plugin will be removed from the in-memory list</li>
	 * <li>the plugin will not be shown in the plugin table</li>
	 * </ol>
	 * It is expected that the plugin is not loaded in memory.
	 * @return true if the plugin folder was deleted, false if not
	 */
	public boolean deletePlugin ( TSToolPlugin plugin, List<String> problems ) {
		boolean removed = IOUtil.deleteDirectory ( plugin.getPluginJarFolder() );
		if ( !removed ) {
			problems.add ( "Unable to remove plugin folder \"" + plugin.getPluginJarFolder() + "\".");
			return false;
		}
		else {
			// The folder was removed so remove from memory and refresh the table.
			this.plugins.remove ( plugin );
		}
		return removed;
	}
	
	/**
	 * Determine the best compatible version, by checking each plugin name to find a specific plugin version that:
	 * <ul>
	 * <li> is the most recent compatible</li>
	 * <li> if all compatibility is unknown, the most recent</li>
	 * <li> rely on the sorting that was done above</li>
	 * </ul>
	 */
	private void evaluateCompatibility () {
		String routine = getClass().getSimpleName() + ".evaluateCompatibility";
		// Get a list of all plugin names to check.
		List<String> pluginNames = new ArrayList<>();
		boolean doTest = false;
		for ( TSToolPlugin plugin : this.plugins ) {
			if ( doTest ) {
				plugin.setIsBestCompatibleWithTSTool(Boolean.TRUE);
			}
			String pluginName = plugin.getName();
			boolean found = false;
			for ( String pluginName0 : pluginNames ) {
				if ( pluginName0.equals(pluginName) ) {
					found = true;
					break;
				}
			}
			if ( !found ) {
				pluginNames.add(pluginName);
			}
		}
		
		if ( doTest ) {
			return;
		}
		// Loop through the plugin names to find the best compatible version.
		
		boolean sortNamesAscending = true;
		boolean sortVersionsAscending = false;
		for ( String pluginName : pluginNames ) {
			if ( Message.isDebugOn ) {
				Message.printStatus(2, routine, "Checking plugin \"" + pluginName + "\" for best compatible.");
			}
			// Get a list of the plugins for the name.
			List<TSToolPlugin> pluginsForName = new ArrayList<>();
			for ( TSToolPlugin plugin : this.plugins ) {
				if ( plugin.getName().equals(pluginName) ) {
					pluginsForName.add(plugin);
				}
			}

			// Sort again to make sure.
			Collections.sort(pluginsForName, new TSToolPluginComparator(sortNamesAscending, sortVersionsAscending) );
			
			// Loop through the plugins:
			// - if compatibility is unknown, assume it is OK
			// - over time this will be filled in as plugin jar manifest specifies information
			boolean bestFound = false;
			for ( TSToolPlugin plugin : pluginsForName ) {
				Boolean isCompatible = plugin.getIsCompatibleWithTSTool();
				if ( Message.isDebugOn ) {
					Message.printStatus(2, routine, "  Plugin \"" + pluginName + "\" version=\"" + plugin.getVersion() +
							"\" isCompatible=" + isCompatible);
				}
				if ( bestFound ) {
					// The best was already found so set to false for this plugin.
					plugin.setIsBestCompatibleWithTSTool(Boolean.FALSE);
					if ( Message.isDebugOn ) {
						Message.printStatus(2, routine, "    Not best compatible (because later version is best).");
					}
				}
				else {
					// Still looking for the best compatible.
					if ( (isCompatible != null) && !isCompatible ) {
						// Definitely not compatible so can't be the best.
						plugin.setIsBestCompatibleWithTSTool(Boolean.FALSE);
						if ( Message.isDebugOn ) {
							Message.printStatus(2, routine, "    Not best compatible (because not compatible).");
						}
					}
					else {
						// Compatibility unknown or compatible:
						// - set the best to true and break
						plugin.setIsBestCompatibleWithTSTool(Boolean.TRUE);
						if ( Message.isDebugOn ) {
							Message.printStatus(2, routine, "    Best compatible.");
						}
						bestFound = true;
					}
				}
			}
		}
	}

	/**
	 * Find the list of plugin jar files using new (TSTool 12.07.00 and later approach).
	 * Plugins may correspond to datastores or may provide one or more commands that are not related to a datastore.
	 * This simply finds candidate jar files in the software installation 'plugins' folder and
	 * the user files '.tstool/##/plugions' folder.
	 * The loadPlugin* methods then examine the jar files and use if appropriate.
	 * @param session TSTool session.
	 * @param listAll if true, list all the jar files in expected locations, including dependencies.
	 * If false, list only the jar files that are not in a "dep/" folder.
	 * @param pluginJarList list of full path to plugin jar file (will be appended to),
	 * output will be full paths
	 */
	public void findPluginJarFiles ( TSToolSession session, boolean listAll, List<String> pluginJarList ) {
		String routine = TSToolMain.class.getSimpleName() + ".findPluginJarFiles";
		
		// List of candidate jar files:
		// - initially, any jar files that are in the software installation or user files
		// - will be pared down to the final list of plugin jar files
		
		List<String> jarFileList = new ArrayList<>();

		// Get a list of candidate jar files in the user plugins folder:
		// - will take precedent over installation files so list first
		// - append to the list
		
		File userPluginsFolder = new File(session.getUserPluginsFolder());
		Message.printStatus(2, routine, "Finding plugin jar files in user plugins folder \"" + userPluginsFolder + "\"");
		try {
			getMatchingFilenamesInTree ( jarFileList, userPluginsFolder, ".*.jar" );
		}
		catch ( Exception e ) {
			// Should not happen.
		}

		// Get a list of jar files in the software installation plugins folder:
		// - append to the list
		File installPluginsFolder = new File(session.getInstallPluginsFolder());
		Message.printStatus(2, routine, "Finding plugin jar files in TSTool software installation plugins folder \"" + installPluginsFolder + "\"");
		try {
			getMatchingFilenamesInTree ( jarFileList, installPluginsFolder, ".*.jar" );
		}
		catch ( Exception e ) {
			// Should not happen.
		}
		
		// Loop through the Jar files and determine if they contain A TSTool plugin:
		// - exclude jar files that are in a 'dep/' folder because these will be dependencies
		// - the jar file 'META-INF/MANIFEST.MF' file will include
		
		// All plugin jar files, including multiple versions.
		if ( listAll ) {
		for ( String jarFile : jarFileList ) {
			jarFile = jarFile.replace("\\", "/");
			Message.printStatus(2, routine, "Checking \"" + jarFile + "\" for plugin (ignoring \"dep\" files).");
			if ( ! jarFile.contains("/dep/") ) {
				Message.printStatus(2, routine, "  Not a dependency, checking the jar file manifest.");
				// Not a dependency jar file:
				// - check the manifest attributes
				SortedMap<String,String> manifestMap = new TreeMap<>();
				try {
					// Get the Manifest file attributes.
					manifestMap = TSToolPlugin.readJarManifestMap ( jarFile );
					Message.printStatus(2, routine, "  Have " + manifestMap.size() + " manifest attributes.");
					
					// Check the attributes for whether a plugin:
					// - should have at least one of the standard attributes
					boolean found = false;
					if ( manifestMap.get("DataStore-Class") != null ) {
						Message.printStatus(2, routine, "  Found 'DataStore-Class' in the manifest.");
						found = true;
					}
					else if ( manifestMap.get("Command-Class1") != null ) {
						Message.printStatus(2, routine, "  Found 'Command-Class1' in the manifest.");
						found = true;
					}
					if ( found ) {
						// Have a plugin jar file:
						// - evaluate 
						pluginJarList.add(jarFile);
					}
				}
				catch ( IOException e ) {
					Message.printWarning(3, routine, "Error reading candidate jar file manifest.");
					Message.printWarning(3, routine, e);
				}
			}
			else {
				Message.printStatus(2, routine, "  Ignoring dependency.");
			}
		}
		}
		else {
			// Used by current TSTool during transition to new code:
			// - return all jar files
			// - will be checked for plugins later
			for ( String jarFile : jarFileList ) {
				pluginJarList.add(jarFile);
			}
		}
	}

	/**
	 * Return a list of all best compatible plugin jar files.
	 * @param includeDep if true, include dependency jar files, if false do not include
	 * @return the list of all best plugin jar files
	 */
	public List<File> getAllBestCompatiblePluginJarFiles ( boolean includeDep ) {
		List<File> jarFiles = new ArrayList<>();
		for ( TSToolPlugin plugin : this.plugins ) {
			if ( plugin.getIsBestCompatibleWithTSTool() ) {
				jarFiles.add(plugin.getPluginJarFile());
				if ( includeDep ) {
					jarFiles.addAll(plugin.getPluginDepList());
				}
			}
		}
		return jarFiles;
	}

	/**
	 * Return the plugin at the specified index (0+), or null if out of bounds.
	 * @return the plugin at the specified index (0+), or null if out of bounds
	 */
	public TSToolPlugin getPlugin ( int index ) {
		if ( (index < 0) || (index >= size()) ) {
			return null;
		}
		else {
			return this.plugins.get ( index );
		}
	}

	/**
	 * Return the all the plugins
	 * @return the list of all the plugins, guaranteed to be non-null but may be empty
	 */
	public List<TSToolPlugin> getPlugins () {
		if ( this.plugins == null ) {
			return new ArrayList<>();
		}
		else {
			return this.plugins;
		}
	}
	
	/**
	 * Return the singleton instance of the TSToolPluginManager.
	 * If it is the first call, all the plugin data will be read.
	 * @return the singleton instance of the TSToolPluginManager.
	 */
	public static TSToolPluginManager getInstance () {
		if ( instance == null ) {
			// Get a new instances.
			instance = new TSToolPluginManager ();
			// First time so read the data.
			instance.searchForPlugins ( TSToolSession.getInstance() );
		}
		return instance;
	}

	/**
	 * Return the singleton instance of the TSToolPluginManager.
	 * @param refreshData if true, refresh the data by checking for installations
	 * @return the singleton instance of the TSToolPluginManager.
	 */
	public static TSToolPluginManager getInstance ( boolean refreshData ) {
		if ( instance == null ) {
			// Get a new instances.
			instance = new TSToolPluginManager ();
		}
		if ( refreshData || (instance.plugins.size() == 0) ) {
			// Either have no data or want to refresh the data.
			instance.searchForPlugins ( TSToolSession.getInstance() );
		}
		return instance;
	}

	/**
	Visits all files and directories under the given directory and if
	the file matches the pattern it is added to the file list.
	All files that end with ".jar" will be added to the list.
	@param fileList List of jar files that are matched, to be appended to.
	@param path Folder in which to start searching for jar files.
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
	        // Add to list if jar file is valid.
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
	 * Determine the plugins by searching the computer.
	 * Candidate parent folders are searched for the following (backslash inserted to avoid issue with closing comment in javadoc):
	 * <ul>
	 * <li> Drive:/CDSS/TSTool-*\/plugins/</li>
	 * <li> Drive:/C/Users/.tstool/##/plugins/</li>
	 * </ul>
	 * The internal list of plugins is set.
	 * @param tstoolSession the TSTool session that is active
	 */
	private void searchForPlugins ( TSToolSession tstoolSession ) {
		String routine = getClass().getSimpleName() + ".searchForPlugins";
		List<TSToolPlugin> pluginList = new ArrayList<>();

		// Get the list of candidate plugin jar files:
		// - list all plugin jar files since this manager helps to manage

		List<String> pluginJarList = new ArrayList<>();
		
		// Find plugin jar files:
		// - this pass the first test of being a plugin but may not be compatible with the TSToolSession
		boolean listAll = true;
		findPluginJarFiles ( TSToolSession.getInstance(), listAll, pluginJarList );
		Message.printStatus ( 2, routine, "Have " + pluginJarList.size() + " plugin jar files from initial search." );
		
		// Loop through the plugin jar files.
		for ( String jarFile : pluginJarList ) {
			// Create a plugin instance:
			// - this will populate initial plugin information like folder locations, size, jar file manifest, etc.
			TSToolPlugin plugin = new TSToolPlugin ( new File(jarFile) );
			
			// Add the plugin to the list.
			pluginList.add(plugin);
		}
		
		// Sort the installations with newest first, based on the version, newest first.
		boolean sortNameAscending = true;
		boolean sortVersionAscending = false;
		Collections.sort(pluginList, new TSToolPluginComparator(sortNameAscending, sortVersionAscending) );
		this.plugins = pluginList;
		
		// Evaluate the compatibility of plugins with TSTool, based on version.
		evaluateCompatibility();
	}

	/**
	 * Return the size of the list of installations.
	 * @return the size of the list of installations
	 */
	public int size () {
		if ( this.plugins == null ) {
			return 0;
		}
		else {
			return this.plugins.size();
		}
	}
}