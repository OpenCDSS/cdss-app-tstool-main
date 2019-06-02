// TSTool_HydroBase_Ag_TableModel - Table Model for a List of HydroBase_AgriculturalCASSCropStats or
// HydroBase_Agstats

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

// ----------------------------------------------------------------------------
// TSTool_HydroBase_Ag_TableModel - Table Model for a Vector of
//					HydroBase_AgriculturalCASSCropStats or
//					HydroBase_Agstats (currently not
//					complete).
// ----------------------------------------------------------------------------
// Copyright:   See the COPYRIGHT file
// ----------------------------------------------------------------------------
// History:
//
// 2003-11-25	Steven A. Malers, RTi	Initial version. Copy and modify
//					TSTool_HydroBase_TableModel class.
// 2004-01-22	SAM, RTi		Update for new JWorksheet row heading
//					standard.
// 2004-02-08	SAM, RTi		Update to use
//					HydroBase_AgriculturalCropStats.
//					Remove irrigated ts summary.
// 2004-02-20	SAM, RTi		Update to use
//					HydroBase_AgriculturalCASSCropStats.
//					Add HydroBase_AgriculturalNASSCropStats.
// 2007-02-26	SAM, RTi		Clean up code based on Eclipse feedback.
// ----------------------------------------------------------------------------

package DWR.DMI.tstool;

import java.util.List;

import DWR.DMI.HydroBaseDMI.HydroBase_Agstats;

import RTi.Util.GUI.JWorksheet;
import RTi.Util.GUI.JWorksheet_AbstractRowTableModel;

/**
This class is a table model for time series header information for HydroBase agricultural statistics time series.
By default the sheet will contain row and column numbers.
*/
@SuppressWarnings("serial")
public class TSTool_HydroBase_Ag_AgStats_TableModel
extends JWorksheet_AbstractRowTableModel<HydroBase_Agstats>
{

/**
Number of columns in the table model, including the row number.
*/
private final int __COLUMNS = 9;

public final int COL_ID = 0;			// County
public final int COL_DATA_SOURCE = 1;		// CASS
public final int COL_DATA_TYPE = 2;		// Combination of commodity and practice
public final int COL_TIME_STEP = 3;		// Year
public final int COL_UNITS = 4;			// Acre
public final int COL_START = 5;
public final int COL_END = 6;
public final int COL_STATE = 7;			// State
public final int COL_INPUT_TYPE = 8;		// HydroBase

//private String __data_type_prefix = "";	// Prefix for data type.

/**
Constructor.  This builds the model for displaying the given HydroBase Ag time series data.
@param data the list of HydroBase_Agstats, HydroBase_AgriculturalCASSCropStats, or HydroBase_AgriculturalNASSCropStats
that will be displayed in the table (null is allowed - see setData()).
@param data_type_prefix The data type prefix for the data type (e.g., "CropAreaHarvested", "CropAreaPlanted").
@throws Exception if an invalid results passed in.
*/
public TSTool_HydroBase_Ag_AgStats_TableModel ( JWorksheet worksheet, List<HydroBase_Agstats> data, String data_type_prefix )
throws Exception
{	if ( data == null ) {
		_rows = 0;
	}
	else {
		_rows = data.size();
	}
	_data = data;
	//__data_type_prefix = data_type_prefix;
}

/**
From AbstractTableModel.  Returns the class of the data stored in a given column.
@param columnIndex the column for which to return the data class.
*/
public Class<?> getColumnClass (int columnIndex) {
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

// TODO smalers need to implement, although maybe AgStats are not used
/**
From AbstractTableMode.  Returns the name of the column at the given position.
@return the name of the column at the given position.
*/
public String getColumnName(int columnIndex) {
	return "";
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

	HydroBase_Agstats ag = (HydroBase_Agstats)_data.get(row);
	switch (col) {
		// case 0 handled above.
		case COL_ID:		return ag.getCounty();
		case COL_DATA_SOURCE:	// Station also has source but we want the meas_type source.
					return "CASS";
		case COL_DATA_TYPE:	// TSTool translates to values from the TSTool interface...
					return "AgStats-" +ag.getType();
		case COL_TIME_STEP:	// TSTool translates HydroBase values to nicer values...
					return "Year";
		case COL_UNITS:		// For now ignore...
					return "";
		case COL_START:		// For now ignore...
					return "";
		case COL_END:		// For now ignore...
					return "";
		case COL_STATE:		// For now ignore...
					return "";
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
	widths[COL_DATA_TYPE] = 30;
	widths[COL_TIME_STEP] = 4;
	widths[COL_UNITS] = 4;
	widths[COL_START] = 4;
	widths[COL_END] = 4;
	widths[COL_STATE] = 4;
	widths[COL_INPUT_TYPE] = 8;
	return widths;
}

}