// TSToolPlugin - class to manage TSTool plugins

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
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import RTi.Util.IO.BasicFolderAttributes;
import RTi.Util.IO.IOUtil;
import RTi.Util.IO.ProcessManager;
import RTi.Util.Message.Message;
import RTi.Util.String.StringUtil;
import RTi.Util.Time.DateTime;

/**
 * The TSToolPlugin class corresponds to a TSTool plugin installation,
 * which can exist in the following folders:
 * <ul>
 * <li> TSTool software installation 'plugins' folder</li>
 * <li> User's '.tstool/##/plugins' folder</li>
 * </ul>
 * 
 * The folder structure in the 'plugins' folder can be one of the following,
 * which is used for both the TSTool installation and user files.
 * 
 * As of TSTool 15, the plugins folder organization is similar to the following,
 * which allows multiple plugins to exist and be managed:
 * 
 * <pre>
 *    plugins/owf-tstool-aws-plugin/
 *      1.5.7/
 *        owf-tstool-aws-plugin-1.5.7.jar
 *        dep/
 *       
 * </pre>
 * 
 * Prior to TSTool 15, the plugins folder organization is similar to the following,
 * which DOES NOT allow multiple versions.
 * 
 * <pre>
 *    plugins/owf-tstool-aws-plugin/
 *      owf-tstool-aws-plugin-1.5.7.jar
 *      dep/
 * </pre>
 * 
 * The plugins that are managed allow checking the software version for plugin compatibility.
 * The TSToolPluginManager_JFrame can be used to display and handle installations.
 * Information fields are meant to display information but are not used internally for any functionality.
 */
public class TSToolPlugin {

	/**
	 * The location of the plugin installation,
	 * either with TSTool software files or user files.
	 */
	private TSToolPluginLocationType pluginLocationType = null;
	
	/**
	 * TSTool plugin main installation folder, for example,
	 * the following, which is directly under the 'plugins' folder.
	 * For TSTool before version 15, this will include a single active plugin,
	 * for example:
	 *   C:/Users/user/.tstool/##/plugins/owf-tstool-aws-plugin
	 */
	private File mainInstallationFolder = null;

	/**
	 * TSTool main installation folder size:
	 * - may include files for multiple versions for TSTool greater than version 15
	 * - initialize to negative to trigger refresh for the first call
	 */
	private long mainInstallationFolderSize = -999;

	/**
	 * TSTool plugin version installation folder. for example,
	 * the following, which is directly above the plugin's 'jar' file.
	 * For TSTool before version 15, this will include a single active plugin,
	 * for example:
	 *   C:/Users/user/.tstool/##/plugins/owf-tstool-aws-plugin/1.2.3
	 *   
	 * For TSTool before 15, this folder will be same as the main plugin folder.
	 */
	private File versionInstallationFolder = null;

	/**
	 * TSTool version installation folder size:
	 * - initialize to negative to trigger refresh for the first call
	 */
	private long versionInstallationFolderSize = -999;
	
	/**
	 * Most recent modification time for a plugin's version files.
	 */
	private DateTime versionInstallationLastModifiedTime = null;

	/**
	 * TSTool plugin's semantic version.
	 */
	private String version = "";

	/**
	 * TSTool plugin's version date.
	 */
	private String versionDate = "";

	/**
	 * Create an instance of the plugin.
	 * The version is determined from the version folder
	 * @param versionInstallationFolder the plugin's version installation folder
	 */
	public TSToolPlugin ( File versionInstallationFolder ) {
		this.versionInstallationFolder = versionInstallationFolder;
		// Determine the main installation folder.
		setVersionFromFolder ( mainInstallationFolder );
		this.mainInstallationFolderSize = -999;
		this.versionInstallationFolderSize = -999;
		// Set the main folder based on the version folder.
		setMainFolder ();
	}

	/**
	 * Create an instance of the plugin.
	 * @param versionInstallationFolder the plugin's vesion installation folder
	 * @param version the plugin version
	 */
	public TSToolPlugin ( File versionInstallationFolder, String version ) {
		this.versionInstallationFolder =  versionInstallationFolder;
		this.version = version;
	}

	/**
	 * Return the plugin main installation folder.
	 * @return the plugin main installation folder
	 */
	public File getMainInstallationFolder () {
		return this.mainInstallationFolder;
	}

	/**
	 * Return the main installation folder size.
	 * The size is determined when called by getting the size of all files in the installation folder.
	 * @param refresh if true, refresh the size from the file system
	 * @return the installation folder size, or negative number if not known
	 */
	public long getMainnstallationFolderSize ( boolean refresh ) {
		if ( refresh || (this.mainInstallationFolderSize < 0) ) {
			refreshMainInstallationFolderAttributes ();
		}
		return this.mainInstallationFolderSize;
	}

	/**
	 * Determine the plugin version from a log file.  **TODO - Need to refactor.**
	 * @param logFile the log file to examine
	 * @return the plugin version from the log file, or null if could not determine
	 */
	private String getLogFileVersion ( File logFile ) {
		String version = null;
		try {
			List<String> fileLines = IOUtil.fileToStringList(logFile.getAbsolutePath(), 50);
			for ( String line : fileLines ) {
				if ( line.startsWith("#") && (line.contains("program:") ) ) {
					// Found a line like:
					//   # program:      TSTool 14.10.1 (2025-02-12)
					int pos = line.indexOf("TSTool ");
					if ( (pos > 0) && (pos < (line.length() - 8) ) ) {
						int pos2 = line.indexOf(" ", (pos + 7));
						if ( pos2 > 0 ) {
							version = line.substring((pos + 7),pos2).trim();
							break;
						}
					}
				}
			}
		}
		catch ( Exception e ) {
			version = null;
		}
		
		return version;
	}

	/**
	 * Return the plugin location type.
	 * @return the plugin location type
	 */
	public TSToolPluginLocationType getLocationType () {
		return this.pluginLocationType;
	}

	/**
	 * Return the version installation last modification time.
	 * @return the plugin's version files last modification time
	 */
	public DateTime getVersionInstallationLastModifiedTime () {
		return this.versionInstallationLastModifiedTime;
	}

	/**
	 * Return the plugin version.
	 * @return the plugin version
	 */
	public String getVersion () {
		return this.version;
	}

	/**
	 * Return the plugin version date.
	 * @return the plugin version date
	 */
	public String getVersionDate () {
		return this.versionDate;
	}

	/**
	 * Return the plugin version installation folder.
	 * @return the plugin version installation folder
	 */
	public File getVersionInstallationFolder () {
		return this.versionInstallationFolder;
	}

	/**
	 * Return the version installation folder size.
	 * The size is determined when called by getting the size of all files in the version installation folder.
	 * @param refresh if true, refresh the size from the file system
	 * @return the version installation folder size, or negative number if not known
	 */
	public long getVersionInstallationFolderSize ( boolean refresh ) {
		if ( refresh || (this.versionInstallationFolderSize < 0) ) {
			refreshVersionInstallationFolderAttributes ();
		}
		return this.versionInstallationFolderSize;
	}

	/**
	 * Refresh the main installation folder attributes such as for all of the files.
	 * @param refresh if true, refresh the size from the file system
	 */
	private void refreshMainInstallationFolderAttributes () {
		try {
			// Get folder attributes for the entire installation folder:
			// - used to get the size
			List<String> includePatterns = new ArrayList<>();
			List<String> excludePatterns = new ArrayList<>();
			BasicFolderAttributes folderAttrib = IOUtil.getFolderAttributes ( this.mainInstallationFolder, true, includePatterns, excludePatterns );
			this.mainInstallationFolderSize = folderAttrib.getSize();
		}
		catch ( IOException e ) {
			// Log the error.
			String routine = getClass().getSimpleName() + ".refreshMainInstallationFolderAttributes";
			Message.printWarning(3,routine,e);
		}
		try {
			// Get the folder attributes for the 'bin' folder:
			// - used to get the software last modified time
			File binFolder = new File ( this.mainInstallationFolder.getAbsolutePath() + File.separator, "bin");
			List<String> includePatterns = new ArrayList<>();
			List<String> excludePatterns = new ArrayList<>();
			BasicFolderAttributes folderAttrib = IOUtil.getFolderAttributes ( binFolder, true, includePatterns, excludePatterns );
			FileTime time = folderAttrib.getFilesMaxLastModifiedTime();
			if ( time != null ) {
				//this.mainInstallationLastModifiedTime = new DateTime(time.toInstant(), 0, null);
			}
		}
		catch ( IOException e ) {
			// Log the error.
			String routine = getClass().getSimpleName() + ".refreshMainInstallationFolderAttributes";
			Message.printWarning(3,routine,e);
		}
	}

	/**
	 * Refresh the version installation folder attributes such as for all of the files.
	 * @param refresh if true, refresh the size from the file system
	 */
	private void refreshVersionInstallationFolderAttributes () {

	}

	/**
	 * Set the user folder based on the major version.
	 * This method should be called from the constructor.
	 */
	private void setMainFolder () {
		if ( (this.version == null) || !this.version.contains(".") ) {
			return;
		}
		else {
			// Version contains a period so the major version is the first part.
			int pos = this.version.indexOf(".");
			String majorVersion = this.version.substring(0,pos);
			if ( IOUtil.isUNIXMachine() ) {
				// Linux.
				String home = System.getenv("HOME");
				//this.userFolder = new File(home + "/.tstool/" + majorVersion);
			}
			else {
				// Windows.
				String home = System.getenv("USERPROFILE");
				//this.userFolder = new File(home + "\\.tstool\\" + majorVersion);
			}
		}
	}

	/**
	 * Set the semantic version from the installation folder,
	 * and the version date from running the executable (works for version 15 and before then is null).
	 * @param installationFolder the TSTool installation folder, expected to end in a semantic version
	 * (e.g., TSTool-14.10.0)
	 */
	private void setVersionFromFolder ( File installationFolder ) {
		// Parse the TSTool version from the installation folder.
		String folderName = installationFolder.getName();
		int pos = folderName.indexOf("-");
		if ( (pos < 0) || ((pos + 1) >= folderName.length()) ) {
			this.version = "";
		}
		else {
			// Get the version as 14.10.0, for example.
			this.version = folderName.substring(pos + 1);
		}
		
		// Get the version date by running the program.
		String executable = installationFolder.getAbsolutePath() + File.separator + "bin" + File.separator;
		if ( IOUtil.isUNIXMachine() ) {
			executable += "TSTool.exe";
		}
		else {
			executable += "tstool";
		}
		
		if ( (this.version != null) && !this.version.isEmpty() && StringUtil.compareSemanticVersions(this.version, ">", "15", 1)) {
			// Only TSTool installer > 15.0.0 has --version-date command parameter.
			ProcessManager pm = new ProcessManager(executable + " --version-date");
			pm.run();
			List<String> output = pm.getOutputList();
			if ( output.size() > 0 ) {
				this.versionDate = output.get(0).trim();
			}
		}
	}

}