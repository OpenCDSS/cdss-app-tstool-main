// TSToolInstallation - class to manage TSTool installations

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
 * The TSToolInstallation class corresponds to a TSTool installation.
 * The installations that are managed allow checking the software version for plugin compatibility.
 * The TSToolInstallationManager_JFrame can be used to display and handle installations.
 * Information fields are meant to display information but are not used internally for any functionality.
 */
public class TSToolInstallation {

	/**
	 * TSTool installation folder.
	 */
	private File installationFolder = null;

	/**
	 * TSTool installation folder size:
	 * - initialize to negative to trigger refresh for the first call
	 */
	private long installationFolderSize = -999;
	
	/**
	 * Most recent modification time on software files in the 'bin' folder.
	 */
	private DateTime softwareLastModifiedTime = null;

	/**
	 * Last run date based on modification time of log files.
	 */
	private DateTime softwareLastRunTime = null;

	/**
	 * TSTool user folder for the TSTool major version, for example:
	 *    C:\\Users\\user\\.tstool\\14
	 */
	private File userFolder = null;

	/**
	 * TSTool user folder size:
	 * - initialize to negative to trigger refresh for the first call
	 */
	private long userFolderSize = -999;

	/**
	 * TSTool semantic version.
	 */
	private String version = "";

	/**
	 * TSTool version date.
	 */
	private String versionDate = "";

	/**
	 * Create an instance of the installation.
	 * The version is determined from the end of the installation folder
	 * @param installationFolder the installation folder
	 */
	public TSToolInstallation ( File installationFolder ) {
		this.installationFolder = installationFolder;
		setVersionFromFolder ( installationFolder );
		this.installationFolderSize = -999;
		this.userFolderSize = -999;
		// Set the user folder based on the version.
		setUserFolder ();
	}

	/**
	 * Create an instance of the installation.
	 * @param installationFolder the installation folder
	 * @param version the TSTool version
	 */
	public TSToolInstallation ( File installationFolder, String version ) {
		this.installationFolder = installationFolder;
		this.version = version;
	}

	/**
	 * Return the TSTool installation folder.
	 * @return the TSTool installation folder
	 */
	public File getInstallationFolder () {
		return this.installationFolder;
	}

	/**
	 * Return the installation folder size.
	 * The size is determined when called by getting the size of all files in the installation folder.
	 * @param refresh if true, refresh the size from the file system
	 * @return the installation folder size, or negative number if not known
	 */
	public long getInstallationFolderSize ( boolean refresh ) {
		if ( refresh || (this.installationFolderSize < 0) ) {
			refreshInstallationFolderAttributes ();
		}
		return this.installationFolderSize;
	}

	/**
	 * Determine the TSTool version from a log file.
	 * @param logFile the log file to examine
	 * @return the TSTool version from the log file, or null if could not determine
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
	 * Return the software last modification time.
	 * @return the TSTool last modification time, based on the 'bin' files
	 */
	public DateTime getSoftwareLastModifiedTime () {
		return this.softwareLastModifiedTime;
	}

	/**
	 * Return the software last run time.
	 * @return the TSTool last run time, based on the installation and user 'log' files
	 */
	public DateTime getSoftwareLastRunTime () {
		return this.softwareLastRunTime;
	}

	/**
	 * Return the user folder.
	 * @return the user folder
	 */
	public File getUserFolder () {
		return this.userFolder;
	}

	/**
	 * Return the user folder size.
	 * The size is determined when called by getting the size of all files in the installation folder.
	 * @param refresh if true, refresh the size from the file system
	 * @return the user folder size, or negative number if not known
	 */
	public long getUserFolderSize ( boolean refresh ) {
		if ( refresh || (this.userFolderSize < 0) ) {
			refreshUserFolderAttributes ();
		}
		return this.userFolderSize;
	}

	/**
	 * Return the TSTool version.
	 * @return the TSTool version
	 */
	public String getVersion () {
		return this.version;
	}

	/**
	 * Return the TSTool version date.
	 * @return the TSTool version date
	 */
	public String getVersionDate () {
		return this.versionDate;
	}

	/**
	 * Refresh the installation folder attributes such as for all of the files.
	 * @param refresh if true, refresh the size from the file system
	 */
	private void refreshInstallationFolderAttributes () {
		try {
			// Get folder attributes for the entire installation folder:
			// - used to get the size
			List<String> includePatterns = new ArrayList<>();
			List<String> excludePatterns = new ArrayList<>();
			BasicFolderAttributes folderAttrib = IOUtil.getFolderAttributes ( this.installationFolder, true, includePatterns, excludePatterns );
			this.installationFolderSize = folderAttrib.getSize();
		}
		catch ( IOException e ) {
			// Log the error.
			String routine = getClass().getSimpleName() + ".refreshInstallationFolderAttributes";
			Message.printWarning(3,routine,e);
		}
		try {
			// Get the folder attributes for the 'bin' folder:
			// - used to get the software last modified time
			File binFolder = new File ( this.installationFolder.getAbsolutePath() + File.separator, "bin");
			List<String> includePatterns = new ArrayList<>();
			List<String> excludePatterns = new ArrayList<>();
			BasicFolderAttributes folderAttrib = IOUtil.getFolderAttributes ( binFolder, true, includePatterns, excludePatterns );
			FileTime time = folderAttrib.getFilesMaxLastModifiedTime();
			if ( time != null ) {
				this.softwareLastModifiedTime = new DateTime(time.toInstant(), 0, null);
			}
		}
		catch ( IOException e ) {
			// Log the error.
			String routine = getClass().getSimpleName() + ".refreshInstallationFolderAttributes";
			Message.printWarning(3,routine,e);
		}
		try {
			// Get the folder attributes for the startup 'logs' folder:
			// - used to get the software last run time
			// - installation log files may not be writeable
			// - user log files will be touched after the startup, should be writeable
			File logsFolder = new File ( this.installationFolder.getAbsolutePath() + File.separator, "logs");
			if ( logsFolder.exists() ) {
				// Set the initial run time based on the log folder date:
				// - necessary because the user log file may be a later minor version
				this.softwareLastRunTime = new DateTime(Instant.ofEpochMilli(logsFolder.lastModified()), 0, null);
			}
			List<String> includePatterns = new ArrayList<>();
			List<String> excludePatterns = new ArrayList<>();
			BasicFolderAttributes folderAttrib = IOUtil.getFolderAttributes ( logsFolder, true, includePatterns, excludePatterns );
			FileTime time = folderAttrib.getFilesMaxLastModifiedTime();
			if ( time != null ) {
				// Always set here because if refreshed don't want the old value:
				// - may reset below based on user log files
				DateTime dt = new DateTime(time.toInstant(), 0, null);
				if ( (this.softwareLastRunTime == null) || dt.greaterThan(this.softwareLastRunTime) ) {
					this.softwareLastRunTime = dt;
				}
			}
			// Check the user log files:
			// - only check log files that include a string that matches the specific version
			// - otherwise, all log files for the major version are considered
			// - the version string in the log file is something like:
			//      Status[1]: Opened log file "C:\Users\sam\.tstool\14\logs\TSTool_sam.log".  Previous messages not in file.
			//      File generated by...
			//      # program:      TSTool 14.10.1 (2025-02-12)
			logsFolder = new File ( this.userFolder.getAbsolutePath() + File.separator, "logs");
			//folderAttrib = IOUtil.getFolderAttributes ( logsFolder, true, includePatterns, excludePatterns );
			boolean listRecursive = true;
			boolean listFiles = true;
			boolean listFolders = false;
			List<File> logFiles = IOUtil.getFiles(logsFolder, listRecursive, listFiles, listFolders, includePatterns, excludePatterns);
			for ( File logFile : logFiles ) {
				// Make sure that the log file matches the specific installation version.
				String logVersion = getLogFileVersion(logFile);
				//Message.printStatus(2, "xxx", "Log file \"" + logFile.getAbsolutePath() + "\" version is \"" + logVersion + "\"");
				if ( (logVersion != null) && logVersion.equals(getVersion()) ) {
					// The log file matches the version.
					Long unixTime = logFile.lastModified();
					// Convert milliseconds to seconds.
					Instant logTime = Instant.ofEpochMilli(unixTime);
					DateTime dt = new DateTime(logTime, 0, null);
					if ( (this.softwareLastRunTime == null) || dt.greaterThan(this.softwareLastRunTime) ) {
						this.softwareLastRunTime = dt;
					}
				}
			}
		}
		catch ( IOException e ) {
			// Log the error.
			String routine = getClass().getSimpleName() + ".refreshInstallationFolderAttributes";
			Message.printWarning(3,routine,e);
		}
	}

	/**
	 * Refresh the user folder attributes such as for all of the files.
	 * @param refresh if true, refresh the user file size and other attributes from the file system
	 */
	private void refreshUserFolderAttributes () {
		// Refresh the size of the user folder.
		try {
			List<String> includePatterns = new ArrayList<>();
			List<String> excludePatterns = new ArrayList<>();
			BasicFolderAttributes folderAttrib = IOUtil.getFolderAttributes ( this.userFolder, true, includePatterns, excludePatterns );
			this.userFolderSize = folderAttrib.getSize();
		}
		catch ( IOException e ) {
			// Log the error.
			String routine = getClass().getSimpleName() + ".refreshUserFolderAttributes";
			Message.printWarning(3,routine,e);
		}
	}
	
	/**
	 * Set the user folder based on the major version.
	 * This method should be called from the constructor.
	 */
	private void setUserFolder () {
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
				this.userFolder = new File(home + "/.tstool/" + majorVersion);
			}
			else {
				// Windows.
				String home = System.getenv("USERPROFILE");
				this.userFolder = new File(home + "\\.tstool\\" + majorVersion);
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