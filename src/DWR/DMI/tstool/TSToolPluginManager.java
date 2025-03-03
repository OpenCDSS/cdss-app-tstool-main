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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
	 * Return the plugin at the specified index (0+), or null if out of bounds.
	 * @return the plugin at the specified index (0+), or null if out of bounds
	 */
	public TSToolPlugin get ( int index ) {
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
	public List<TSToolPlugin> getAll () {
		if ( this.plugins == null ) {
			return new ArrayList<>();
		}
		else {
			return this.plugins;
		}
	}
	
	/**
	 * Return the singleton instance of the TSToolPluginManager.
	 * @param refreshData if true, refresh the data by checking for installations
	 * @return the singleton instance of the TSToolPluginManager.
	 */
	public static TSToolPluginManager getInstance ( boolean refreshData ) {
		if ( instance == null ) {
			instance = new TSToolPluginManager ();
		}
		if ( refreshData ) {
			instance.plugins = instance.searchForPlugins();
		}
		return instance;
	}
	
	/**
	 * Determine the plugins by searching the computer.
	 * Candidate parent folders are searched for the following (backslash inserted to avoid issue with closing comment in javadoc):
	 * <ul>
	 * <li> Drive:/CDSS/TSTool-*\/plugins/</li>
	 * <li> Drive:/C/Users/.tstool/##/plugins/</li>
	 * </ul>
	 */
	private List<TSToolPlugin> searchForPlugins () {
		List<String> candidateParents = new ArrayList<>();
		List<TSToolPlugin> pluginList = new ArrayList<>();
		if ( IOUtil.isUNIXMachine() ) {
			// Linux:
			// - system installation is /opt
			// - user installation is ${home}
			candidateParents.add("/opt");
			String home = System.getenv("HOME");
			if ( (home != null) && !home.isEmpty() ) {
				candidateParents.add(home);
			}
		}
		else {
			// Windows:
			// - search for TSTool software installation plugins
			// - search for user file plugins
			/*
			for ( char drive = 'A'; drive <= 'Z'; drive++ ) {
				// Check for a top-level folder.
				File cdssFolder = new File(drive + ":\\CDSS");
				if ( cdssFolder.exists() ) {
					// CDSS folder in a drive exists.
					// Get all folders under the 'CDSS' folder.
					boolean listRecursive = false;
					boolean listFiles = false;
					boolean listFolders = true;
					List<String> includePatterns = new ArrayList<>();
					includePatterns.add("TSTool-.*");
					List<String> excludePatterns = new ArrayList<>();
					List<File> tstoolFolders = null;
					try {
						tstoolFolders = IOUtil.getFiles(cdssFolder, listRecursive, listFiles, listFolders, includePatterns, excludePatterns);
					}
					catch ( IOException e ) {
						// Problem listing the files.
						String routine = getClass().getSimpleName() + ".searchForPlugins";
						Message.printWarning(3, routine, e);
					}
					// Create candidate files using the TSTool folders and expected software files.
					List<String> tstoolFiles = new ArrayList<>();
					for ( File tstoolFolder : tstoolFolders ) {
						// Check for the executable program:
						// - only add one file so that the files and folder align, so the matching folder can be set below.
						tstoolFiles.add ( tstoolFolder.getAbsolutePath() + File.separator + "bin" + File.separator + "TSTool.exe" );
					}
					for ( int i = 0; i < tstoolFiles.size(); i++ ) {
						String tstoolFile = tstoolFiles.get(i);
						File file = new File(tstoolFile);
						if ( file.exists() ) {
							// Found an installation:
							// - only add if it has not already been added
							File installationFolder = tstoolFolders.get(i);
							boolean found = false;
							for ( TSToolPlugin plugin0 : pluginList ) {
								if ( plugin0.getVersionInstallationFolder().equals(installationFolder)) {
									found = true;
									break;
								}
							}
							if ( !found ) {
								TSToolPlugin installation = new TSToolPlugin ( installationFolder );
								pluginList.add(installation);
							}
						} // Candidate file exists.
					} // Candidate files on the folder to search for.
				} // CDSS folder on the drive exists.
			} // Windows drives.
			*/
			
			// User plugins.
			
		} // Windows.

		// Sort the installations with newest first, based on the version, newest first.
		Collections.sort(pluginList, new TSToolPluginComparator(false) );

		return pluginList;
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