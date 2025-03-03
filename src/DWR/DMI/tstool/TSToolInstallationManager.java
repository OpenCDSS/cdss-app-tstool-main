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
 * The TSToolInstallManager class is a singleton that can be created for a TSTool application session
 * to help manage TSTool installations, for example to determine the size of and remove old installations,
 * and to check which versions are installed to help troubleshoot plugin version issues.
 */
public class TSToolInstallationManager {

	/**
	 * Singleton instance.
	 */
	private static TSToolInstallationManager instance = null;
	
	/**
	 * Private list of TSTool installations.
	 */
	private List<TSToolInstallation> installations = null;
	
	/**
	 * Create an instance of the singleton.
	 */
	private TSToolInstallationManager () {
		this.installations = new ArrayList<>();
	}

	/**
	 * Return the installation at the specified index (0+), or null if out of bounds.
	 * @return the installation at the specified index (0+), or null if out of bounds
	 */
	public TSToolInstallation get ( int index ) {
		if ( (index < 0) || (index >= size()) ) {
			return null;
		}
		else {
			return this.installations.get ( index );
		}
	}

	/**
	 * Return the all the installations
	 * @return the list of all the installations, guaranteed to be non-null but may be empty
	 */
	public List<TSToolInstallation> getAll () {
		if ( this.installations == null ) {
			return new ArrayList<>();
		}
		else {
			return this.installations;
		}
	}
	
	/**
	 * Return the singleton instance of the TSToolInstallationManager.
	 * @param refreshData if true, refresh the data by checking for installations
	 * @return the singleton instance of the TSToolInstallationManager.
	 */
	public static TSToolInstallationManager getInstance ( boolean refreshData ) {
		if ( instance == null ) {
			instance = new TSToolInstallationManager ();
		}
		if ( refreshData ) {
			instance.installations = instance.searchForInstallations();
		}
		return instance;
	}
	
	/**
	 * Determine the installations by searching the computer.
	 * Candidate parent folders are searched for the following (backslash inserted to avoid issue with closing comment in javadoc):
	 * <ul>
	 * <li> Drive:/CDSS/TSTool-*\/bin/TSTool.exe</li>
	 * </ul>
	 */
	private List<TSToolInstallation> searchForInstallations () {
		List<String> candidateParents = new ArrayList<>();
		List<TSToolInstallation> installationList = new ArrayList<>();
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
			// Windows.
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
						String routine = getClass().getSimpleName() + ".searchForInstallations";
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
							for ( TSToolInstallation install0 : installationList ) {
								if ( install0.getInstallationFolder().equals(installationFolder)) {
									found = true;
									break;
								}
							}
							if ( !found ) {
								TSToolInstallation installation = new TSToolInstallation ( installationFolder );
								installationList.add(installation);
							}
						} // Candidate file exists.
					} // Candidate files on the folder to search for.
				} // CDSS folder on the drive exists.
			} // Windows drives.
		} // Windows.

		// Sort the installations with newest first, based on the version, newest first.
		Collections.sort(installationList, new TSToolInstallationComparator(false) );

		return installationList;
	}

	/**
	 * Return the size of the list of installations.
	 * @return the size of the list of installations
	 */
	public int size () {
		if ( this.installations == null ) {
			return 0;
		}
		else {
			return this.installations.size();
		}
	}
}