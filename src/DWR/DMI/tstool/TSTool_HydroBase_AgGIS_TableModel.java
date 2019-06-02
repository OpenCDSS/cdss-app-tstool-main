// TSTool_HydroBase_AgGIS_TableModel -  table model for time series header information for HydroBase
// agricultural time series (irrigation summary).

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

import DWR.DMI.HydroBaseDMI.HydroBase_StructureIrrigSummaryTS;
import DWR.DMI.HydroBaseDMI.HydroBase_WaterDistrict;

import RTi.Util.GUI.JWorksheet;
import RTi.Util.GUI.JWorksheet_AbstractRowTableModel;

/**
This class is a table model for time series header information for HydroBase
agricultural time series (irrigation summary).  By default the
sheet will contain row and column numbers.
*/
@SuppressWarnings("serial")
public class TSTool_HydroBase_AgGIS_TableModel
extends JWorksheet_AbstractRowTableModel<HydroBase_StructureIrrigSummaryTS>
{

/**
Number of columns in the table model, including the row number.
*/
private final int __COLUMNS = 11;

public final int COL_ID = 0;			// Structure ID
public final int COL_NAME = 1;			// Structure name
public final int COL_DATA_SOURCE = 2;		// CDSSGIS
public final int COL_DATA_TYPE = 3;
public final int COL_TIME_STEP = 4;
public final int COL_UNITS = 5;
public final int COL_START = 6;
public final int COL_END = 7;
public final int COL_DIST = 8;
public final int COL_DIV = 9;
public final int COL_INPUT_TYPE = 10;

String __data_type = ""; // Data type to display - can be one of a number of fields.
private int __wdid_length = 7; // The length to use when formatting WDIDs in IDs.

/**
Constructor.  This builds the model for displaying the given HydroBase irrigation summary time series data.
@param data the Vector of 
HydroBase_StructureIrrigSummaryTS that will be displayed in the table (null is allowed - see setData()).
@param data_type Data type to list - can be any of a number of fields in the data records.
@param wdid_length The length to format WDIDs.
@throws Exception if an invalid results passed in.
*/
public TSTool_HydroBase_AgGIS_TableModel ( JWorksheet worksheet, List<HydroBase_StructureIrrigSummaryTS> data, String data_type, int wdid_length )
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
public Class<?> getColumnClass (int columnIndex) {
	switch (columnIndex) {
		default:	return String.class;
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
		case COL_ID:			return "ID";
		case COL_NAME:			return "Name";
		case COL_DATA_SOURCE:		return "Data\nSource";
		case COL_DATA_TYPE:		return "Data Type";
		case COL_TIME_STEP:		return "Time\nStep";
		case COL_UNITS:			return "Units";
		case COL_START:			return "Start";
		case COL_END:			return "End";
		case COL_DIST:			return "Dist.";
		case COL_DIV:			return "Div.";
		case COL_INPUT_TYPE:		return "Input Type";
		default:			return "";
	}
}

/**
Returns the format to display the specified column.
@param column column for which to return the format.
@return the format (as used by StringUtil.formatString()).
*/
public String getFormat ( int column ) {
	switch (column) {
		default:		return "%s";	// All are strings.
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

	HydroBase_StructureIrrigSummaryTS ag = _data.get(row);
	switch (col) {
		// case 0 handled above.
		case COL_ID:		return HydroBase_WaterDistrict.formWDID(__wdid_length, ag.getWD(),ag.getID());
		case COL_NAME: 		return ag.getStr_name();
		case COL_DATA_SOURCE:	// Station also has source but want the meas_type source.
					return "CDSSGIS";
		case COL_DATA_TYPE:	// TSTool translates to values from the TSTool interface...
					return __data_type + "-" + ag.getLand_use();
		case COL_TIME_STEP:	// TSTool translates HydroBase values to nicer values...
					return "Year";
		case COL_UNITS:		// For now ignore...
					return "ACFT";
		case COL_START:		// For now ignore...
					return "";
		case COL_END:		// For now ignore...
					return "";
		case COL_DIST:		return "" + ag.getWD();
		case COL_DIV:		return "" + ag.getDiv();
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
	widths[COL_ID] = 6;
	widths[COL_NAME] = 15;
	widths[COL_DATA_SOURCE] = 6;
	widths[COL_DATA_TYPE] = 24;
	widths[COL_TIME_STEP] = 4;
	widths[COL_UNITS] = 4;
	widths[COL_START] = 4;
	widths[COL_END] = 4;
	widths[COL_DIST] = 4;
	widths[COL_DIV] = 4;
	widths[COL_INPUT_TYPE] = 12;
	return widths;
}

}
