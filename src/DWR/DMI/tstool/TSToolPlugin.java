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
import java.net.URL;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.jar.Attributes;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;

import RTi.Util.IO.BasicFolderAttributes;
import RTi.Util.IO.IOUtil;
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
	 * Plugin name from jar file manifest 'TSTool-Plugin', for example 'owf-tstool-aws-plugin' from the following:
	 *   C:/Users/user/.tstool/##/plugins/owf-tstool-aws-plugin/1.5.7 (TSTool 15.x)
	 */
	private String nameFromJarManifest = "";

	/**
	 * Plugin name from jar file name
	 */
	private String nameFromJarFile = "";
	
	/**
	 * The location of the plugin installation,
	 * either with TSTool software files or user files.
	 */
	private TSToolPluginLocationType pluginLocationType = TSToolPluginLocationType.UNKNOWN;
	
	/**
	 * The Jar file for the plugin, for example:
	 *   C:/Users/user/.tstool/##/plugins/owf-tstool-aws-plugin/owf-tstool-aws-plugin-1.5.7.jar (before TSTool 15.x)
	 *   C:/Users/user/.tstool/##/plugins/owf-tstool-aws-plugin/1.5.7/owf-tstool-aws-plugin-1.5.7.jar (TSTool 15.x)
	 */
	private File pluginJarFile = null;

	/**
	 * The Jar file 'META-INF/MANIFEST.MF' attributes as a map.
	 * Use a sorted map so to facilitate use in output.
	 */
	private SortedMap<String,String> pluginJarFileManifestMap = new TreeMap<>();

	/**
	 * TSTool 'plugins/plugin-name/version/dep' folder for this plugin (may be system or user plugin),
	 * which contains dependency jar files, * for example:
	 *   C:/Users/user/.tstool/##/plugins/owf-tstool-aws-plugin/dep (before TSTool 15.x)
	 *   C:/Users/user/.tstool/##/plugins/owf-tstool-aws-plugin/1.5.7/dep (TSTool 15.x)
	 */
	private File pluginDepFolder = null;

	/**
	 * Dependency items in the plugin dependency folder, useful to know if the plugin has any dependencies.
	 */
	private List<File> pluginDepList = new ArrayList<>();

	/**
	 * Plugin installation folder size:
	 * - initialize to null
	 */
	private Long installationSize = null;
	
	/**
	 * Most recent modification time for a plugin's version files.
	 */
	private DateTime installationLastModifiedTime = null;

	/**
	 * TSTool plugin's semantic version:
	 * - from the installation folder
	 */
	private String versionFromFolder= "";

	/**
	 * TSTool plugin's semantic version:
	 * - from the plugin jar file, for example semantic version before the file extension
	 *   C:/Users/user/.tstool/##/plugins/owf-tstool-aws-plugin/1.5.7/owf-tstool-aws-plugin-1.5.7.jar
	 */
	private String versionFromJarFile = "";

	/**
	 * TSTool plugin's semantic version:
	 * - from the MANIFEST.MF file in the jar file 
	 */
	private String versionFromJarManifest = "";

	/**
	 * TSTool plugin's version date:
	 * - enable later?
	 */
	private String versionDate = "";
	
	/**
	 * Whether compatible with current TSTool.
	 */
	private Boolean isCompatibleWithTSTool = null;

	/**
	 * Whether compatible with current TSTool and the best plugin version.
	 */
	private Boolean isBestCompatibleWithTSTool = null;

	/**
	 * Create an instance of the plugin from the plugin jar file path.
	 * The version is determined from the version folder
	 * @param versionInstallationFolder the plugin's version installation folder
	 */
	public TSToolPlugin ( File pluginJarFile ) {
		// Set the plugin Jar file, which will cause a set of all the related data.
		setPluginJarFile ( pluginJarFile );
	}

	/**
	 * Return whether the plugin is the best compatible plugin for the current TSTool version.
	 * @return true if the plugin is compatible with the current TSTool version,
	 * false if not, an null if unable to determine
	 */
	public Boolean getIsBestCompatibleWithTSTool () {
		return this.isBestCompatibleWithTSTool;
	}

	/**
	 * Return whether the plugin is compatible for the current TSTool version.
	 * @return true if the plugin is compatible with the current TSTool version,
	 * false if not, an null if unable to determine
	 */
	public Boolean getIsCompatibleWithTSTool () {
		return this.isCompatibleWithTSTool;
	}

	/**
	 * Return the version installation last modification time.
	 * @return the plugin's version files last modification time
	 */
	public DateTime getInstallationLastModifiedTime () {
		return this.installationLastModifiedTime;
	}
	
	/**
	 * Return the plugin installation folder size.
	 * The size is determined when called by getting the size of all files in the installation folder.
	 * @param refresh if true, refresh the size from the file system
	 * @return the installation folder size, or negative number if not known
	 */
	public Long getInstallationSize ( boolean refresh ) {
		if ( refresh || (this.installationSize == null) ) {
			refreshInstallationFolderAttributes ();
		}
		return this.installationSize;
	}

	/**
	 * Return the plugin jar file manifest attributes as a CSV, useful for including in a table.
	 * @return the plugin jar file manifest attributes as a CSV
	 */
	public String getJarFileManifestCsv () {
		StringBuilder b = new StringBuilder();
		for ( Map.Entry<String,String> entry : this.pluginJarFileManifestMap.entrySet() ) {
			if ( b.length() > 0 ) {
				b.append(",");
			}
			b.append(entry.getKey() + "=" + entry.getValue() );
		}
		return b.toString();
	}

	/**
	 * Return the plugin location type.
	 * @return the plugin location type
	 */
	public TSToolPluginLocationType getLocationType () {
		return this.pluginLocationType;
	}

	/**
	 * Return the plugin name that identifies the plugin product (e.g., 'owf-tstool-aws-plugin',
	 * determined from the jar manifext 'Plugin-Name' attribute and then from jar file name.
	 * @return the plugin name
	 */
	public String getName () {
		if ( (this.nameFromJarManifest != null) && !this.nameFromJarManifest.isEmpty() ) {
			return this.nameFromJarManifest;
		}
		else if ( (this.nameFromJarFile != null) && !this.nameFromJarFile.isEmpty() ) {
			return this.nameFromJarFile;
		}
		else {
			return "";
		}
	}

	/**
	 * Return the plugin dependency jar file list.
	 * @return the plugin dependency jar file list.
	 */
	public List<File> getPluginDepList() {
		return this.pluginDepList;
	}

	/**
	 * Return the plugin jar file as a File.
	 * @return the plugin jar file, or null if not set.
	 */
	public File getPluginJarFile () {
		return this.pluginJarFile;
	}

	/**
	 * Return the plugin jar file folder as a File.
	 * @return the plugin jar file folder.
	 */
	public File getPluginJarFolder () {
		if ( this.pluginJarFile == null ) {
			return null;
		}
		else {
			return this.pluginJarFile.getParentFile();
		}
	}

	/**
	 * Return the TSTool version requirements from the manifest 'TSTool-Version' property.
	 * @return the TSTool version requirements, or an empty string if not known
	 */
	public String getTSToolVersionRequirements () {
		String req = this.pluginJarFileManifestMap.get("TSTool-Version");
		if ( (req == null) || req.isEmpty() ) {
			return "";
		}
		else {
			return req;
		}
	}
	
	/**
	 * Return whether the installation folder parent matches the version.
	 * @return true if the plugin jar file is in a version folder, false if not, and null if unknown
	 */
	public Boolean getUsesVersionFolder () {
		File parent = getPluginJarFolder();
		if ( parent == null ) {
			return null;
		}
		else {
			if ( parent.getName().equals(getVersion()) ) {
				return Boolean.TRUE;
			}
			else {
				return Boolean.FALSE;
			}
		}
	}
	
	/**
	 * Return the plugin version as the manifest 'Plugin-Version' property, the version from the jar file, or the installation folder.
	 * @return the plugin version, or an empty string if not known
	 */
	public String getVersion () {
		if ( (this.versionFromJarManifest != null) && !this.versionFromJarManifest.isEmpty() ) {
			return this.versionFromJarManifest;
		}
		else if ( (this.versionFromJarFile != null) && !this.versionFromJarFile.isEmpty() ) {
			return this.versionFromJarFile;
		}
		else if ( (this.versionFromFolder != null) && !this.versionFromFolder.isEmpty() ) {
			return this.versionFromFolder;
		}
		else {
			return "";
		}
	}

	/**
	 * Return the plugin version date.
	 * @return the plugin version date
	 */
	public String getVersionDate () {
		return this.versionDate;
	}

	/**
	 * Return the plugin version for the installation folder.
	 * @return the plugin version for the installation folder.
	 */
	public String getVersionFromFolder () {
		return this.versionFromFolder;
	}

	/**
	 * Return the plugin version for the jar MANIFEST.MF file.
	 * @return the plugin version for the jar MANIFEST.MF file
	 */
	public String getVersionFromJarManifest () {
		return this.versionFromJarManifest;
	}

	/**
	 * Read the manifest properties for a 'jar' file.
	 * @param jarFile path to a 'jar' file of interest
	 * @return a map of jar file MANIFEST.MF file attributes, sorted by key
	 */
	public static SortedMap<String,String> readJarManifestMap ( String jarFile ) throws IOException {
		String routine = TSToolPlugin.class.getSimpleName() + ".readJarManifest";
		JarInputStream jarStream = null;
		SortedMap<String,String> manifestMap = new TreeMap<>();
		// Get the Jar file URL:
		// - on Windows will be like "file:///C:/path/...
		// - on Linux will be like "file:///path/...
		URL jarFileUrl = new URL("file:///" + jarFile.replace("\\", "/"));
		try {
			// Open the META-INF/MANIFEST.MF file and get the property Command-Class, which is what needs to be loaded.
			jarStream = new JarInputStream(jarFileUrl.openStream());
			Manifest manifest = jarStream.getManifest();
			Attributes attributes = manifest.getMainAttributes();
			for ( Map.Entry<Object,Object> entry : attributes.entrySet() ) {
				Object key = entry.getKey();
				Object value = entry.getValue();
				if ( Message.isDebugOn ) {
					Message.printStatus(2, routine, "Checking manifest " + key + "=" + value);
				}
				if ( key != null ) {
					String skey = "" + key;
					if ( !skey.startsWith("Comment") ) {
						// Not a comment to add to the list of attributes.
						manifestMap.put(skey, "" + value);
					}
				}
			}
		}
		catch ( Exception e ) {
			Message.printWarning(3, routine, "Error reading manifest.");
			Message.printWarning(3, routine, e);
		}
		finally {
			if ( jarStream != null ) {
				try {
					jarStream.close();
				}
				catch ( IOException e ) {
					// Ignore - should not happen.
				}
			}
		}
		return manifestMap;
	}

	/**
	 * Refresh the installation folder attributes including installation size and modification time for all of the files.
	 * The installation folder size and last modified time are set
	 * @param refresh if true, refresh the size from the file system
	 */
	private void refreshInstallationFolderAttributes () {
		try {
			// Get folder attributes for the entire installation folder:
			// - used to get the size
			List<String> includePatterns = new ArrayList<>();
			List<String> excludePatterns = new ArrayList<>();
			BasicFolderAttributes folderAttrib = IOUtil.getFolderAttributes ( getPluginJarFolder(), true, includePatterns, excludePatterns );
			this.installationSize = folderAttrib.getSize();
			FileTime time = folderAttrib.getFilesMaxLastModifiedTime();
			if ( time != null ) {
				this.installationLastModifiedTime = new DateTime(time.toInstant(), 0, null);
			}
		}
		catch ( IOException e ) {
			// Log the error.
			String routine = getClass().getSimpleName() + ".refreshInstallationFolderAttributes";
			Message.printWarning(3,routine,e);
		}
	}
	
	/**
	 * Set whether the plugin is best for TSTool compatibility.
	 * @param isBest whether the plugin is best for TSTool compatibility (OK to set to null if unknown).
	 */
	public void setIsBestCompatibleWithTSTool ( Boolean isBest ) {
		this.isBestCompatibleWithTSTool = isBest;
	}

	/**
	 * Set the name from the jar file, for example 'owf-tstool-aws-plugin' in the following:
	 *   C:/Users/user/.tstool/##/plugins/owf-tstool-aws-plugin/owf-tstool-aws-plugin-1.5.7.jar
	 * This should be called from the constructor after the version is set.
	 */
	private void setNameFromJarFile () {
		// Parse the TSTool version from the installation folder:
		// - first remove the extension
		String jarFile = this.getPluginJarFile().getName().replace(".jar", "");
		// Remove the version.
		jarFile = jarFile.replace(this.versionFromJarFile, "");
		// Remove trailing dash or underscore.
		if ( jarFile.endsWith("-") ) {
			jarFile = jarFile.substring(0,(jarFile.length() - 1));
		}
		if ( jarFile.endsWith("_") ) {
			jarFile = jarFile.substring(0,(jarFile.length() - 1));
		}
		this.nameFromJarFile = jarFile;
	}
	
	/**
	 * Set the plugin jar file.
	 * All related data are (re)set.
	 * @param pluginJarFile the plugin jar file
	 */
	public void setPluginJarFile ( File pluginJarFile ) {
		String routine = getClass().getSimpleName() + ".setPluginJarFile";
		this.pluginJarFile = pluginJarFile;

		// Determine the plugin location type.
		// Get the user plugins folder using forward slashes.
		String installPluginsFolder = TSToolSession.getInstance().getInstallPluginsFolder().replace("\\", "/");
		String userPluginsFolder = TSToolSession.getInstance().getUserPluginsFolder().replace("\\", "/");
		// The plugin installation location is either in system location or user files.
		this.pluginLocationType = TSToolPluginLocationType.UNKNOWN;
		if ( pluginJarFile.getAbsolutePath().replace("\\", "/").startsWith(userPluginsFolder) ) {
			// Plugin location is user files.
			this.pluginLocationType = TSToolPluginLocationType.USER_FILES;
		}
		else if ( pluginJarFile.getAbsolutePath().replace("\\", "/").startsWith(installPluginsFolder) ) {
			this.pluginLocationType = TSToolPluginLocationType.TSTOOL_FILES;
		}
		
		// Read the manifest from the jar file.
		try {
			this.pluginJarFileManifestMap = readJarManifestMap ( pluginJarFile.getAbsolutePath() );
		}
		catch ( IOException e ) {
		}
		
		// Set data from the manifest data.
		if ( this.pluginJarFileManifestMap.get("Plugin-Name") != null ) {
			// Set the plugin name from the manifest 'Plugin-Name' attribute.
			this.nameFromJarManifest = this.pluginJarFileManifestMap.get("Plugin-Name");
		}
		if ( this.pluginJarFileManifestMap.get("Plugin-Version") != null ) {
			// Set the plugin version from the manifest 'Plugin-Version' attribute.
			this.versionFromJarManifest = this.pluginJarFileManifestMap.get("Plugin-Version");
		}
		
		// Set the version from the plugin jar file name.
		setVersionFromJarFile ();
		
		// Set the name from the plugin jar file name.
		setNameFromJarFile ();
		
		// Get dependency files.
		if ( this.pluginJarFileManifestMap.get("Class-Path") != null ) {
			// Get the dependency files.
			String depPath = this.getPluginJarFolder() + File.separator + this.pluginJarFileManifestMap.get("Class-Path");
			if ( depPath.endsWith("/") ) {
				// Remove the trailing /.
				depPath = depPath.substring(0,depPath.length() - 1);
			}
			this.pluginDepFolder = new File(depPath);
			boolean listRecursive = true;
			boolean listFiles = true;
			boolean listFolders = false;
			List<String> includePatterns = new ArrayList<>();
			includePatterns.add(".*\\.jar");
			List<String> excludePatterns = null;
			try {
				this.pluginDepList = IOUtil.getFiles(this.pluginDepFolder, listRecursive, listFiles, listFolders, includePatterns, excludePatterns);
				Message.printStatus(2, routine, "Dependency folder \"" + this.pluginDepFolder.getAbsolutePath() +
					"\" has " + this.pluginDepList.size() + " jar files.");
			}
			catch ( Exception e ) {
				Message.printWarning(3, routine, "Error getting dependency list.");
				Message.printWarning(3, routine, e);
			}
		}
		else {
			Message.printStatus(2, routine, "Plugin for \"" + this.pluginJarFile.getAbsolutePath() + "\" has no dependency folder defined." );
		}

		// Determine whether this plugin is compatible with the current TSTool:
		// - currently only handle simple '> 1.2.3' and '>= 1.2.3' criteria
		// - split the operator and required version

		String requiredVersion = getTSToolVersionRequirements();
		if ( requiredVersion != null ) {
			requiredVersion = requiredVersion.trim(); // Just to make sure there is no surrounding whitespace.
			int pos = -1;
			for ( int i = 1; i <= 0; i++ ) {
				pos = requiredVersion.indexOf("" + i);
				if ( pos >- 0 ) {
					// Found a version.
					break;
				}
			}
			if ( pos >= 0 ) {
				String requiredVersion2 = requiredVersion.substring(pos).trim();
				String operator = "=="; // Default if no operator is provided.
				if ( pos > 0 ) {
					// An operator was specified.
					operator = requiredVersion.substring(0,pos).trim();
				}
				if ( StringUtil.compareSemanticVersions(IOUtil.getProgramVersion(), operator, requiredVersion2, -1) ) {
					this.isBestCompatibleWithTSTool = Boolean.TRUE;
				}
				else {
					this.isBestCompatibleWithTSTool = Boolean.FALSE;
				}
			}
		}
		else {
			// Can't check the version so set to null:
			// - newest installed version will be assumed to be compatible
			this.isCompatibleWithTSTool = null;
		}
	}

	/**
	 * Set the semantic version from the jar file, for example 1.5.7 from the following example:
	 *   C:/Users/user/.tstool/##/plugins/owf-tstool-aws-plugin/owf-tstool-aws-plugin-1.5.7.jar
	 * This should be called from the constructor.
	 */
	private void setVersionFromJarFile () {
		// Parse the TSTool version from the installation folder:
		// - first remove the extension
		String jarFile = this.getPluginJarFile().getName().replace(".jar", "");
		// Next, find the position of a number:
		// - this assumes that a number only shows up in the version part of the filename
		int pos = -1;
		for ( int i = 0; i < jarFile.length(); i++ ) {
			char c = jarFile.charAt(i);
			if ( (c >= '0') && (c <= '9') ) {
				pos = i;
				break;
			}
		}
		if ( pos > 0 ) {
			this.versionFromJarFile = jarFile.substring(pos);
		}
	}

}