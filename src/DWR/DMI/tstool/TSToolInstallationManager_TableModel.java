// TSToolInstallationManager_TableModel - table model for displaying TSToolInstallationManager data in a JWorksheet

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
public class TSToolInstallationManager_TableModel
extends JWorksheet_AbstractRowTableModel<TSToolInstallation> {

	/**
	The TSToolInstalltionManager displayed in the worksheet.
	*/
	private TSToolInstallationManager installationManager = null;

	/**
	Number of columns in the table model (with the alias).
	*/
	private int COLUMNS = 8;

	/**
	Absolute column indices, for column lookups.
	*/
	public final int COL_VERSION = 0;
	public final int COL_VERSION_DATE = 1;
	public final int COL_INSTALLATION_FOLDER = 2;
	public final int COL_INSTALLION_SIZE = 3;
	public final int COL_LAST_RUN = 4;
	public final int COL_LAST_MODIFIED = 5;
	public final int COL_USER_FOLDER = 6;
	public final int COL_USER_FOLDER_SIZE = 7;

	/**
	Constructor.
	@param installationManager the TSToolInstallationManager to show in the worksheet.
	@throws NullPointerException if the dataTable is null.
	*/
	public TSToolInstallationManager_TableModel ( TSToolInstallationManager installationManager )
	throws Exception {
	    if ( installationManager == null ) {
	        _rows = 0;
	    }
	    else {
	        _rows = installationManager.size();
	    }
	    this.installationManager = installationManager;
	    _data = installationManager.getAll();
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
	    	case COL_VERSION: return String.class;
	    	case COL_VERSION_DATE: return String.class;
	    	case COL_INSTALLATION_FOLDER: return String.class;
	    	case COL_INSTALLION_SIZE: return Long.class;
	    	case COL_LAST_RUN: return DateTime.class;
	    	case COL_LAST_MODIFIED: return DateTime.class;
	    	case COL_USER_FOLDER: return String.class;
	    	case COL_USER_FOLDER_SIZE: return Long.class;
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
	    	case COL_VERSION: return "Version";
	    	case COL_VERSION_DATE: return "Version Date";
	    	case COL_INSTALLATION_FOLDER: return "Installation Folder";
	    	case COL_INSTALLION_SIZE: return "Installation Size";
	    	case COL_LAST_RUN: return "Last Run Time";
	    	case COL_LAST_MODIFIED: return "Last Modified Time";
	    	case COL_USER_FOLDER: return "User Folder";
	    	case COL_USER_FOLDER_SIZE: return "User Folder Size";
	        default: return "";
	    }
	}

	/**
	Return tool tips for the columns.
	@return tool tips for the columns.
	*/
	public String[] getColumnToolTips() {
	    String [] tooltips = new String[this.COLUMNS];
    	tooltips[COL_VERSION] = "TSTool version";
    	tooltips[COL_VERSION_DATE] = "TSTool version date (for TSTool 15+).";
    	tooltips[COL_INSTALLATION_FOLDER] = "Installation folder";
    	tooltips[COL_INSTALLION_SIZE] = "Installation size (bytes), includes files in the software installation folder";
    	tooltips[COL_LAST_RUN] = "Last run time (from startup log file and user log file for matching TSTool version)";
    	tooltips[COL_LAST_MODIFIED] = "Last modified time (from software 'bin' folder)";
    	tooltips[COL_USER_FOLDER] = "User folder, which matches the TSTool major version";
    	tooltips[COL_USER_FOLDER_SIZE] = "User folder size (bytes), includes all files for TSTool major version";
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
	    	case COL_USER_FOLDER_SIZE: return "%d";
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

	    TSToolInstallation installation = this.installationManager.get(row);
	    if ( installation == null ) {
	        return null;
	    }
	    File installationFolder = installation.getInstallationFolder();
	    File userFolder = installation.getUserFolder();
	    switch (col) {
	    	case COL_VERSION: return installation.getVersion();
	    	case COL_VERSION_DATE: return installation.getVersionDate();
	    	case COL_INSTALLATION_FOLDER:
	    		if ( installationFolder == null ) {
	    			return null;
	    		}
	    		else {
	    			return installationFolder.getAbsolutePath();
	    		}
	    	case COL_INSTALLION_SIZE:
	    		if ( installationFolder == null ) {
	    			return null;
	    		}
	    		else {
	    			if ( installation.getInstallationFolderSize(false) < 0 ) {
	    				return null;
	    			}
	    			else {
	    				return Long.valueOf(installation.getInstallationFolderSize(false));
	    			}
	    		}
	    	case COL_LAST_RUN:
   				return installation.getSoftwareLastRunTime();
	    	case COL_LAST_MODIFIED:
   				return installation.getSoftwareLastModifiedTime();
	    	case COL_USER_FOLDER:
	    		if ( userFolder == null ) {
	    			return null;
	    		}
	    		else {
	    			return userFolder.getAbsolutePath();
	    		}
	    	case COL_USER_FOLDER_SIZE:
	    		if ( userFolder == null ) {
	    			return null;
	    		}
	    		else {
	    			if ( installation.getUserFolderSize(false) < 0 ) {
	    				return null;
	    			}
	    			else {
	    				return Long.valueOf(installation.getUserFolderSize(false));
	    			}
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
    	widths[COL_INSTALLATION_FOLDER] = 30;
    	widths[COL_INSTALLION_SIZE] = 8;
    	widths[COL_LAST_RUN] = 13;
	   	widths[COL_LAST_MODIFIED] = 13;
    	widths[COL_USER_FOLDER] = 30;
    	widths[COL_USER_FOLDER_SIZE] = 8;
	    return widths;
	}

}