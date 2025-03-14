// TSToolPluginManager_TableModel - table model for displaying TSToolPluginManager data in a JWorksheet

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
import java.util.ArrayList;
import java.util.Collections;

import RTi.Util.GUI.JWorksheet_AbstractRowTableModel;
import RTi.Util.Message.Message;
import RTi.Util.Time.DateTime;

/**
Table model for displaying data table data in a JWorksheet.
*/
@SuppressWarnings("serial")
public class TSToolPluginManager_TableModel
extends JWorksheet_AbstractRowTableModel<TSToolPlugin> {

	/**
	The TSToolInstalltionManager displayed in the worksheet.
	*/
	private TSToolPluginManager pluginManager = null;

	/**
	Number of columns in the table model (with the alias).
	*/
	private int COLUMNS = 13;

	/**
	Absolute column indices, for column lookups.
	*/
	public final int COL_NAME = 0;
	public final int COL_LOCATION_TYPE = 1;
	public final int COL_PLUGIN_JAR_FILE = 2;
	public final int COL_VERSION = 3;
	public final int COL_USES_VERSION_FOLDER = 4;
	//public final int COL_VERSION_DATE = 4;
	public final int COL_TSTOOL_VERSION = 5;
	public final int COL_COMPATIBLE_WITH_TSTOOL= 6;
	public final int COL_BEST_COMPATIBLE_WITH_TSTOOL= 7;
	public final int COL_INSTALLATION_FOLDER = 8;
	public final int COL_INSTALLION_SIZE = 9;
	public final int COL_LAST_MODIFIED = 10;
	public final int COL_DEP_COUNT = 11;
	public final int COL_JAR_MANIFEST = 12;

	/**
	Constructor.
	@param pluginManager the TSToolPluginManager to show in the worksheet.
	@throws NullPointerException if the dataTable is null.
	*/
	public TSToolPluginManager_TableModel ( TSToolPluginManager pluginManager )
	throws Exception {
	    if ( pluginManager == null ) {
	        _rows = 0;
	    }
	    else {
	        _rows = pluginManager.size();
	    }
	    this.pluginManager = pluginManager;
	    // Make a copy of the data to make sure the UI is not sorting differently.
	    _data = new ArrayList<TSToolPlugin>();
	    _data.addAll(pluginManager.getPlugins());
	    boolean sortNameAscending = true;
	    boolean sortVersionAscending = false;
		Collections.sort(_data, new TSToolPluginComparator(sortNameAscending, sortVersionAscending) );
	}

	/**
	From AbstractTableModel.  Returns the class of the data stored in a given column.
	All values are treated as strings.
	@param columnIndex the column for which to return the data class.
	@return the column class
	*/
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Class getColumnClass ( int columnIndex ) {
	    switch (columnIndex) {
	    	case COL_NAME: return String.class;
	    	case COL_LOCATION_TYPE: return String.class;
	    	case COL_PLUGIN_JAR_FILE: return String.class;
	    	case COL_VERSION: return String.class;
	    	case COL_USES_VERSION_FOLDER: return Boolean.class;
	    	//case COL_VERSION_DATE: return String.class;
	    	case COL_TSTOOL_VERSION: return String.class;
	    	case COL_COMPATIBLE_WITH_TSTOOL: return Boolean.class;
	    	case COL_BEST_COMPATIBLE_WITH_TSTOOL: return Boolean.class;
	    	case COL_INSTALLATION_FOLDER: return String.class;
	    	case COL_INSTALLION_SIZE: return Long.class;
	    	case COL_LAST_MODIFIED: return DateTime.class;
	    	case COL_DEP_COUNT: return Integer.class;
	    	case COL_JAR_MANIFEST: return String.class;
	        default: return String.class;
	    }
	}

	/**
	From AbstractTableMode.  Returns the number of columns of data.
	@return the number of columns of data.
	*/
	public int getColumnCount() {
	    return this.COLUMNS;
	}

	/**
	From AbstractTableMode.  Returns the name of the column at the given position.
	@return the name of the column at the given position.
	*/
	public String getColumnName ( int columnIndex ) {
	    switch (columnIndex) {
	    	case COL_NAME: return "Name";
	    	case COL_LOCATION_TYPE: return "Installation Location";
	    	case COL_PLUGIN_JAR_FILE: return "Plugin Jar File";
	    	case COL_VERSION: return "Version";
	    	case COL_USES_VERSION_FOLDER: return "Uses Version Folder";
	    	//case COL_VERSION_DATE: return "Version Date";
	    	case COL_TSTOOL_VERSION: return "TSTool Version Requirement";
	    	case COL_COMPATIBLE_WITH_TSTOOL: return "Compatible with TSTool";
	    	case COL_BEST_COMPATIBLE_WITH_TSTOOL: return "Best Compatible with TSTool";
	    	case COL_INSTALLATION_FOLDER: return "Installation Folder";
	    	case COL_INSTALLION_SIZE: return "Installation Size";
	    	case COL_LAST_MODIFIED: return "Last Modified Time";
	    	case COL_DEP_COUNT: return "Dependencies";
	    	case COL_JAR_MANIFEST: return "Jar Manifest";
	        default: return "";
	    }
	}

	/**
	Return tool tips for the columns.
	@return tool tips for the columns.
	*/
	public String[] getColumnToolTips() {
	    String [] tooltips = new String[this.COLUMNS];
    	tooltips[COL_NAME] = "Plugin (product) name";
    	tooltips[COL_LOCATION_TYPE] = "Plugin installation location";
    	tooltips[COL_PLUGIN_JAR_FILE] = "Path to the plugin jar file";
    	tooltips[COL_VERSION] = "Plugin version";
    	tooltips[COL_USES_VERSION_FOLDER] = "Whether the plugin is installed in a folder matching the version";
    	//tooltips[COL_VERSION_DATE] = "Plugin version date";
    	tooltips[COL_TSTOOL_VERSION] = "TSTool version requirement(s)";
    	tooltips[COL_COMPATIBLE_WITH_TSTOOL] = "Compatible with TSTool?";
    	tooltips[COL_BEST_COMPATIBLE_WITH_TSTOOL] = "Best version of the plugin that is compatible with TSTool?";
    	tooltips[COL_INSTALLATION_FOLDER] = "Plugin installation folder";
    	tooltips[COL_INSTALLION_SIZE] = "Plugin installation size (bytes), includes files in the plugin installation folder";
    	tooltips[COL_LAST_MODIFIED] = "Last modified time (from plugin software folder)";
    	tooltips[COL_DEP_COUNT] = "Dependency jar counti (from dep/ folder)";
    	tooltips[COL_JAR_MANIFEST] = "Plugin jar file manifest attributes";
	    return tooltips;
	}

	/**
	Returns the format to display the specified column.
	@param column column for which to return the format.
	@return the format (as used by StringUtil.formatString()).
	*/
	public String getFormat ( int column ) {
	    switch (column) {
	    	case COL_INSTALLION_SIZE: return "%d";
	        default: return "%s";
	    }
	}

	/**
	From AbstractTableMode.  Returns the number of rows of data in the table.
	@return the number of rows of data in the FileManager.
	*/
	public int getRowCount() {
	    return _rows;
	}

	/**
	Returns the plugin for the requested row.
	@param row the row (0+) for which to return data.
	@return the plugin for the row.
	*/
	public TSToolPlugin getPlugin ( int row ) {
	    if ( _sortOrder != null ) {
	        row = _sortOrder[row];
	    }
	    return this.pluginManager.getPlugin(row);
	}

	/**
	From AbstractTableMode.  Returns the data that should be placed in the JTable at the given row and column.
	@param row the row (0+) for which to return data.
	@param col the absolute column (0+) for which to return data.
	@return the data that should be placed in the JTable at the given row and column.
	*/
	public Object getValueAt ( int row, int col ) {
	    // Make sure the row numbers are never sorted.

	    if ( _sortOrder != null ) {
	        row = _sortOrder[row];
	    }

	    TSToolPlugin plugin = this.pluginManager.getPlugin(row);
	    if ( plugin == null ) {
	        return null;
	    }
	    switch (col) {
	    	case COL_NAME:
	    		return plugin.getName();
	    	case COL_LOCATION_TYPE:
	    		return plugin.getLocationType().toString();
	    	case COL_PLUGIN_JAR_FILE:
	    		if ( plugin.getPluginJarFile() == null ) {
	    			return null;
	    		}
	    		else {
	    			return plugin.getPluginJarFile().getAbsolutePath();
	    		}
	    	case COL_VERSION:
	    		return plugin.getVersion();
	    	case COL_USES_VERSION_FOLDER:
	    		return plugin.getUsesVersionFolder();
	    	//case COL_VERSION_DATE:
	    	//	return plugin.getVersionDate();
	    	case COL_TSTOOL_VERSION:
	    		return plugin.getTSToolVersionRequirements();
	    	case COL_COMPATIBLE_WITH_TSTOOL:
	    		return plugin.getIsCompatibleWithTSTool();
	    	case COL_BEST_COMPATIBLE_WITH_TSTOOL:
	    		Message.printStatus(2, "TableModel", "Plugin \"" + plugin.getName() + "\" version=" +
	    			plugin.getVersion() + " isBest=" + plugin.getIsBestCompatibleWithTSTool() );
	    		return plugin.getIsBestCompatibleWithTSTool();
	    	case COL_INSTALLATION_FOLDER:
	    		File installationFolder = plugin.getPluginJarFolder();
	    		if ( installationFolder == null ) {
	    			return null;
	    		}
	    		else {
	    			return installationFolder.getAbsolutePath();
	    		}
	    	case COL_INSTALLION_SIZE:
	    		return plugin.getInstallationSize(false);
	    	case COL_LAST_MODIFIED:
   				return plugin.getInstallationLastModifiedTime();
	    	case COL_DEP_COUNT:
	    		return plugin.getPluginDepList().size();
	    	case COL_JAR_MANIFEST:
	    		return plugin.getJarFileManifestCsv ();
	        default:
	        	return null;
	    }
	}

	/**
	Returns an array containing the column widths (in number of characters).
	@return an integer array containing the widths for each field.
	*/
	public int[] getColumnWidths() {
	    int[] widths = new int[this.COLUMNS];
    	widths[COL_NAME] = 20;
    	widths[COL_LOCATION_TYPE] = 7;
    	widths[COL_PLUGIN_JAR_FILE] = 70;
    	widths[COL_VERSION] = 8;
    	widths[COL_USES_VERSION_FOLDER] = 8;
    	//widths[COL_VERSION_DATE] = 8;
    	widths[COL_TSTOOL_VERSION] = 12;
    	widths[COL_COMPATIBLE_WITH_TSTOOL] = 10;
    	widths[COL_BEST_COMPATIBLE_WITH_TSTOOL] = 10;
    	widths[COL_INSTALLATION_FOLDER] = 45;
    	widths[COL_INSTALLION_SIZE] = 8;
	   	widths[COL_LAST_MODIFIED] = 13;
    	widths[COL_DEP_COUNT] = 10;
    	widths[COL_JAR_MANIFEST] = 45;
	    return widths;
	}

}