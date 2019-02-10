// TSTool_UsgsNwisDaily_TableModel - table model for USGS NWIS site/time series header information for TS instances.

/* NoticeStart

TSTool
TSTool is a part of Colorado's Decision Support Systems (CDSS)
Copyright (C) 1994-2019 Colorado Department of Natural Resources

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

import java.util.List;

import rti.tscommandprocessor.commands.usgs.nwis.daily.UsgsNwisDailyDataStore;
import rti.tscommandprocessor.commands.usgs.nwis.daily.UsgsNwisSiteTimeSeriesMetadata;

import RTi.Util.GUI.JWorksheet_AbstractRowTableModel;

/**
This class is a table model for USGS NWIS site/time series header information for TS instances.
By default the sheet will contain row and column numbers.
The alias can be treated as a hidden column.
This class may eventually be moved to the RTi.TS package.
*/
public class TSTool_UsgsNwisDaily_TableModel extends JWorksheet_AbstractRowTableModel
{
    
/**
Data store associated with the data.
*/
private UsgsNwisDailyDataStore __dataStore = null;

/**
Number of columns in the table model.
*/
private int __COLUMNS = 14;

/**
Absolute column indices, for column lookups (includes the alias).
*/
public final int COL_ID = 0;
public final int COL_NAME = 1;
public final int COL_DATA_SOURCE= 2;
public final int COL_PARAMETER_NUM = 3;
public final int COL_PARAMETER_NAME = 4;
public final int COL_STATISTIC_NUM = 5;
public final int COL_STATISTIC_NAME = 6;
public final int COL_TIME_STEP = 7;
public final int COL_UNITS = 8;
public final int COL_START = 9;
public final int COL_END = 10;
public final int COL_LATITUDE = 11;
public final int COL_LONGITUDE = 12;
public final int COL_DATA_STORE_NAME = 13;

/**
Constructor.  This builds the model for displaying the given time series data.
@param data the list of TS that will be displayed in the table (null is allowed).
@throws Exception if an invalid results passed in.
*/
public TSTool_UsgsNwisDaily_TableModel ( UsgsNwisDailyDataStore dataStore, List data )
throws Exception
{	if ( data == null ) {
		_rows = 0;
	}
	else {
	    _rows = data.size();
	}
	_data = data;
	__dataStore = dataStore;
}

/**
From AbstractTableModel.  Returns the class of the data stored in a given
column.  All values are treated as strings.
@param columnIndex the column for which to return the data class.
*/
public Class getColumnClass (int columnIndex) {
	switch (columnIndex) {
		case COL_ID: return String.class;
		case COL_NAME: return String.class;
		case COL_DATA_SOURCE: return String.class;
		case COL_PARAMETER_NUM: return String.class;
		case COL_PARAMETER_NAME: return String.class;
		case COL_STATISTIC_NUM: return String.class;
		case COL_STATISTIC_NAME: return String.class;
		case COL_TIME_STEP: return String.class;
		case COL_UNITS: return String.class;
		case COL_START: return String.class;
		case COL_END: return String.class;
	    case COL_LATITUDE: return String.class;
	    case COL_LONGITUDE: return String.class;
		case COL_DATA_STORE_NAME: return String.class;
		default: return String.class;
	}
}

/**
From AbstractTableMode.  Returns the number of columns of data.
@return the number of columns of data.
*/
public int getColumnCount() {
	return __COLUMNS;
}

/**
From AbstractTableMode.  Returns the name of the column at the given position.
@return the name of the column at the given position.
*/
public String getColumnName(int columnIndex) {
	switch (columnIndex) {
		case COL_ID: return "Site Number\n(ID)";
		case COL_NAME: return "Name/\nDescription";
		case COL_DATA_SOURCE: return "Data\nSource";
		case COL_PARAMETER_NUM: return "Parameter\nNumber";
		case COL_PARAMETER_NAME: return "Parameter\nName";
	    case COL_STATISTIC_NUM: return "Statistic\nNumber";
	    case COL_STATISTIC_NAME: return "Statistic\nName";
		case COL_TIME_STEP: return "Time\nStep";
		case COL_UNITS: return "\nUnits";
		case COL_START: return "\nStart";
		case COL_END: return "\nEnd";
	    case COL_LONGITUDE: return "Longitude\n(Dec. Deg.)";
	    case COL_LATITUDE: return "Latitude\n(Dec. Deg.)";
		case COL_DATA_STORE_NAME: return "\nData Store";
		default: return "";
	}
}

/**
Returns the format to display the specified column.
@param column column for which to return the format.
@return the format (as used by StringUtil.formatString()).
*/
public String getFormat ( int column ) {
	switch (column) {
    	case COL_LONGITUDE: return "%.6f";
        case COL_LATITUDE: return "%.6f";
		default: return "%s";
	}
}

/**
From AbstractTableMode.  Returns the number of rows of data in the table.
*/
public int getRowCount() {
	return _rows;
}

/**
From AbstractTableMode.  Returns the data that should be placed in the JTable at the given row and column.
@param row the row for which to return data.
@param col the absolute column for which to return data.
@return the data that should be placed in the JTable at the given row and column.
*/
public Object getValueAt(int row, int col)
{	// make sure the row numbers are never sorted ...

	if (_sortOrder != null) {
		row = _sortOrder[row];
	}

	UsgsNwisSiteTimeSeriesMetadata ts = (UsgsNwisSiteTimeSeriesMetadata)_data.get(row);
	if ( ts == null ) {
		return "";
	}
	switch (col) {
		case COL_ID: return ts.getSiteNum();
		case COL_NAME: return ts.getName();
		case COL_DATA_SOURCE: return ts.getAgencyCode();
		case COL_PARAMETER_NUM:
		    if ( ts.getParameter() == null ) {
		        return "";
		    }
		    else {
		        return ts.getParameter().getCode();
		    }
	    case COL_PARAMETER_NAME:
	        if ( ts.getParameter() == null ) {
	            return "";
	        }
	        else {
	            return ts.getParameter().getName();
	        }
	    case COL_STATISTIC_NUM:
	        if ( ts.getStatistic() == null ) {
	            return "";
	        }
	        else {
	            return ts.getStatistic().getCode();
	        }
	    case COL_STATISTIC_NAME:
	        if ( ts.getStatistic() == null ) {
	            return "";
	        }
	        else {
	            return ts.getStatistic().getName();
	        }
		case COL_TIME_STEP: return ts.getInterval();
		case COL_UNITS: return ts.getUnits();
		case COL_START: return ts.getStart();
		case COL_END: return ts.getEnd();
	    case COL_LONGITUDE:
	        if ( Double.isNaN(ts.getLongitude()) ) {
	            return null;
	        }
	        else {
	            return ts.getLongitude();
	        }
	    case COL_LATITUDE:
	        if ( Double.isNaN(ts.getLatitude()) ) {
	            return null;
	        }
	        else {
	            return ts.getLatitude();
	        }
		case COL_DATA_STORE_NAME:
		    if ( __dataStore == null ) {
		        return "";
		    }
		    else {
		        return __dataStore.getName();
		    }
		default: return "";
	}
}

/**
Returns an array containing the column widths (in number of characters).
@return an integer array containing the widths for each field.
*/
public int[] getColumnWidths() {
	int[] widths = new int[__COLUMNS];
	widths[COL_ID] = 12;
	widths[COL_NAME] = 20;
	widths[COL_DATA_SOURCE] = 10;
	widths[COL_PARAMETER_NUM] = 8;
	widths[COL_PARAMETER_NAME] = 8;
	widths[COL_STATISTIC_NUM] = 8;
	widths[COL_STATISTIC_NAME] = 8;
	widths[COL_TIME_STEP] = 8;
	widths[COL_LATITUDE] = 8;
	widths[COL_LONGITUDE] = 8;
	widths[COL_UNITS] = 8;
	widths[COL_START] = 10;
	widths[COL_END] = 10;
	widths[COL_DATA_STORE_NAME] = 12;
	return widths;
}

}
