// TSTool_HydroBase_WIS_TableModel - table model for time series header information for HydroBase
// WIS time series, which are identified both by sheet name and row identifier.  By

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

import DWR.DMI.HydroBaseDMI.HydroBase_WISSheetNameWISFormat;

import RTi.Util.GUI.JWorksheet;
import RTi.Util.GUI.JWorksheet_AbstractRowTableModel;

/**
This class is a table model for time series header information for HydroBase
WIS time series, which are identified both by sheet name and row identifier.  By
default, the sheet will contain row and column numbers.
*/
public class TSTool_HydroBase_WIS_TableModel
extends JWorksheet_AbstractRowTableModel
{

/**
Number of columns in the table model, including the row number.
*/
private final int __COLUMNS = 9;

public final int COL_ID = 0;			// Row identifier
public final int COL_DATA_SOURCE = 1;		// DWR
public final int COL_DATA_TYPE = 2;		// WISDiversion, etc.
public final int COL_TIME_STEP = 3;		// Day
public final int COL_UNITS = 4;			// CFS
public final int COL_START = 5;			// Blank
public final int COL_END = 6;			// Blank
public final int COL_SHEET_NAME = 7;		// Sheet name
public final int COL_INPUT_TYPE = 8;		// HydroBase

private String __data_type;	// From TSTool, constant for all records

/**
Constructor.  This builds the model for displaying the given HydroBase WIS time series data.
@param data the Vector of HydroBase_WISSheetNameRowFormat.
that will be displayed in the table (null is allowed - see setData()).
@param data_type Data type to be used with all records.
@throws Exception if an invalid results passed in.
*/
public TSTool_HydroBase_WIS_TableModel ( JWorksheet worksheet, List data, String data_type )
throws Exception
{	if ( data == null ) {
		_rows = 0;
	}
	else {
	    _rows = data.size();
	}
	_data = data;
	__data_type = data_type;
}

/**
From AbstractTableModel.  Returns the class of the data stored in a given column.
@param columnIndex the column for which to return the data class.
*/
public Class getColumnClass (int columnIndex) {
	switch (columnIndex) {
		default: return String.class;
	}
}

/**
From AbstractTableModel.  Returns the number of columns of data.
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
		case COL_ID:		return "ID";
		case COL_DATA_SOURCE:	return "Data\nSource";
		case COL_DATA_TYPE:	return "Data Type\n";
		case COL_TIME_STEP:	return "Time\nStep";
		case COL_UNITS:		return "Units";
		case COL_START:		return "Start";
		case COL_END:		return "End";
		case COL_SHEET_NAME:	return "Sheet Name";
		case COL_INPUT_TYPE:	return "Input Type";
		default:		return "";
	}
}

/**
Returns the format to display the specified column.
@param column column for which to return the format.
@return the format (as used by StringUtil.formatString()).
*/
public String getFormat ( int column ) {
	switch (column) {
		default: return "%s";	// All are strings.
	}
}

/**
From AbstractTableMode.  Returns the number of rows of data in the table.
*/
public int getRowCount() {
	return _rows;
}

/**
From AbstractTableModel.  Returns the data that should be placed in the JTable at the given row and column.
@param row the row for which to return data.
@param col the column for which to return data.
@return the data that should be placed in the JTable at the given row and column.
*/
public Object getValueAt(int row, int col)
{	// make sure the row numbers are never sorted ...
	if (_sortOrder != null) {
		row = _sortOrder[row];
	}

	HydroBase_WISSheetNameWISFormat wis = (HydroBase_WISSheetNameWISFormat)_data.get(row);
	switch (col) {
		case COL_ID:		return wis.getIdentifier();
		case COL_DATA_SOURCE:	// Not stored in the database...
					return "DWR";
		case COL_DATA_TYPE:	// Use the data type passed in from TSTool...
					return __data_type;
		case COL_TIME_STEP:	return "Day";
		case COL_UNITS:		return "CFS";
		case COL_START:		// For now ignore since difficult to determine if WIS format changes...
					return "";
		case COL_END:		// See previous comment...
					return "";
		case COL_SHEET_NAME:	return wis.getSheet_name();
		case COL_INPUT_TYPE:	return "HydroBase";
		default:		return "";
	}
}

/**
Returns an array containing the column widths (in number of characters).
@return an integer array containing the widths for each field.
*/
public int[] getColumnWidths() {
	int[] widths = new int[__COLUMNS];
	widths[COL_ID] = 12;
	widths[COL_DATA_SOURCE] = 5;
	widths[COL_DATA_TYPE] = 12;
	widths[COL_TIME_STEP] = 4;
	widths[COL_UNITS] = 4;
	widths[COL_START] = 4;
	widths[COL_END] = 4;
	widths[COL_SHEET_NAME] = 12;
	widths[COL_INPUT_TYPE] = 8;
	return widths;
}

}
