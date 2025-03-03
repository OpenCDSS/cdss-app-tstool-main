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

import RTi.Util.GUI.JWorksheet_AbstractRowTableModel;
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
	private int COLUMNS = 7;

	/**
	Absolute column indices, for column lookups.
	*/
	public final int COL_LOCATION_TYPE = 0;
	public final int COL_VERSION = 1;
	public final int COL_VERSION_DATE = 2;
	public final int COL_VERSION_INSTALLATION_FOLDER = 3;
	public final int COL_VERSION_INSTALLION_SIZE = 4;
	public final int COL_VERSION_LAST_MODIFIED = 5;
	public final int COL_MAIN_INSTALLATION_FOLDER = 6;

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
	    _data = pluginManager.getAll();
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
	    	case COL_LOCATION_TYPE: return String.class;
	    	case COL_VERSION: return String.class;
	    	case COL_VERSION_DATE: return String.class;
	    	case COL_VERSION_INSTALLATION_FOLDER: return String.class;
	    	case COL_VERSION_INSTALLION_SIZE: return Long.class;
	    	case COL_VERSION_LAST_MODIFIED: return DateTime.class;
	    	case COL_MAIN_INSTALLATION_FOLDER: return String.class;
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
	    	case COL_LOCATION_TYPE: return "Installation Location";
	    	case COL_VERSION: return "Version";
	    	case COL_VERSION_DATE: return "Version Date";
	    	case COL_VERSION_INSTALLATION_FOLDER: return "Installation Folder";
	    	case COL_VERSION_INSTALLION_SIZE: return "Installation Size";
	    	case COL_VERSION_LAST_MODIFIED: return "Last Modified Time";
	    	case COL_MAIN_INSTALLATION_FOLDER: return "Main Installation Folder";
	        default: return "";
	    }
	}

	/**
	Return tool tips for the columns.
	@return tool tips for the columns.
	*/
	public String[] getColumnToolTips() {
	    String [] tooltips = new String[this.COLUMNS];
    	tooltips[COL_LOCATION_TYPE] = "Plugin installation location";
    	tooltips[COL_VERSION] = "Plugin version";
    	tooltips[COL_VERSION_DATE] = "Plugin version date.";
    	tooltips[COL_VERSION_INSTALLATION_FOLDER] = "Plugin installation folder";
    	tooltips[COL_VERSION_INSTALLION_SIZE] = "Plugin installation size (bytes), includes files in the plugin installation folder";
    	tooltips[COL_VERSION_LAST_MODIFIED] = "Last modified time (from plugin software folder)";
    	tooltips[COL_MAIN_INSTALLATION_FOLDER] = "Plugin main installation folder (above the version installation folder)";
	    return tooltips;
	}

	/**
	Returns the format to display the specified column.
	@param column column for which to return the format.
	@return the format (as used by StringUtil.formatString()).
	*/
	public String getFormat ( int column ) {
	    switch (column) {
	    	case COL_VERSION_INSTALLION_SIZE: return "%d";
	        default: return "%s";
	    }
	}

	/**
	From AbstractTableMode.  Returns the number of rows of data in the table.
	@rReturn the number of rows of data in the FileManager.
	*/
	public int getRowCount() {
	    return _rows;
	}

	/**
	From AbstractTableMode.  Returns the data that should be placed in the JTable at the given row and column.
	@param row the row (0+) for which to return data.
	@param col the absolute column (0+) for which to return data.
	@return the data that should be placed in the JTable at the given row and column.
	*/
	public Object getValueAt ( int row, int col ) {
	    // Make sure the row numbers are never sorted.

	    if (_sortOrder != null) {
	        row = _sortOrder[row];
	    }

	    TSToolPlugin plugin = this.pluginManager.get(row);
	    if ( plugin == null ) {
	        return null;
	    }
	    File versionInstallationFolder = plugin.getVersionInstallationFolder();
	    File mainInstallationFolder = plugin.getMainInstallationFolder();
	    switch (col) {
	    	case COL_LOCATION_TYPE: return plugin.getLocationType().toString();
	    	case COL_VERSION: return plugin.getVersion();
	    	case COL_VERSION_DATE: return plugin.getVersionDate();
	    	case COL_VERSION_INSTALLATION_FOLDER:
	    		if ( versionInstallationFolder == null ) {
	    			return null;
	    		}
	    		else {
	    			return versionInstallationFolder.getAbsolutePath();
	    		}
	    	case COL_VERSION_INSTALLION_SIZE:
	    		if ( versionInstallationFolder == null ) {
	    			return null;
	    		}
	    		else {
	    			if ( plugin.getVersionInstallationFolderSize(false) < 0 ) {
	    				return null;
	    			}
	    			else {
	    				return Long.valueOf(plugin.getVersionInstallationFolderSize(false));
	    			}
	    		}
	    	case COL_VERSION_LAST_MODIFIED:
   				return plugin.getVersionInstallationLastModifiedTime();
	    	case COL_MAIN_INSTALLATION_FOLDER:
	    		if ( mainInstallationFolder == null ) {
	    			return null;
	    		}
	    		else {
	    			return mainInstallationFolder.getAbsolutePath();
	    		}
	        default: return null;
	    }
	}

	/**
	Returns an array containing the column widths (in number of characters).
	@return an integer array containing the widths for each field.
	*/
	public int[] getColumnWidths() {
	    int[] widths = new int[this.COLUMNS];
    	widths[COL_VERSION] = 8;
    	widths[COL_VERSION_DATE] = 8;
    	widths[COL_VERSION_INSTALLATION_FOLDER] = 30;
    	widths[COL_VERSION_INSTALLION_SIZE] = 8;
	   	widths[COL_VERSION_LAST_MODIFIED] = 13;
    	widths[COL_MAIN_INSTALLATION_FOLDER] = 30;
	    return widths;
	}

}