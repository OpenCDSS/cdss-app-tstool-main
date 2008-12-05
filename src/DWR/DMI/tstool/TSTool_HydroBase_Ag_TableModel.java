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

import DWR.DMI.HydroBaseDMI.HydroBase_AgriculturalCASSCropStats;
import DWR.DMI.HydroBaseDMI.HydroBase_AgriculturalNASSCropStats;
import DWR.DMI.HydroBaseDMI.HydroBase_Agstats;

import RTi.Util.GUI.JWorksheet;
import RTi.Util.GUI.JWorksheet_AbstractRowTableModel;

/**
This class is a table model for time series header information for HydroBase agricultural statistics time series.
By default the sheet will contain row and column numbers.
*/
public class TSTool_HydroBase_Ag_TableModel
extends JWorksheet_AbstractRowTableModel
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

private final int __RECORD_TYPE_UNKNOWN = 0;	// Used with __record_type
private final int __RECORD_TYPE_AGRICULTURAL_CASS_CROP_STATS = 1;
private final int __RECORD_TYPE_AGSTATS = 2;
private final int __RECORD_TYPE_AGRICULTURAL_NASS_CROP_STATS = 3;

private int __record_type = __RECORD_TYPE_UNKNOWN;	// Type of records being listed, for use with getValueAt().
private String __data_type_prefix = "";	// Prefix for data type.

/**
Constructor.  This builds the model for displaying the given HydroBase Ag time series data.
@param data the list of HydroBase_Agstats, HydroBase_AgriculturalCASSCropStats, or HydroBase_AgriculturalNASSCropStats
that will be displayed in the table (null is allowed - see setData()).
@param data_type_prefix The data type prefix for the data type (e.g., "CropAreaHarvested", "CropAreaPlanted").
@throws Exception if an invalid results passed in.
*/
public TSTool_HydroBase_Ag_TableModel ( JWorksheet worksheet, List data, String data_type_prefix )
throws Exception
{	if ( data == null ) {
		_rows = 0;
	}
	else {	_rows = data.size();
		// Figure out what types of data are listed...
		if ( _rows > 0 ) {
			Object o = (Object)data.get(0);
			if ( o instanceof HydroBase_Agstats ) {
				__record_type = __RECORD_TYPE_AGSTATS;
			}
			else if ( o instanceof
				HydroBase_AgriculturalCASSCropStats ) {
				__record_type = __RECORD_TYPE_AGRICULTURAL_CASS_CROP_STATS;
			}
			else if ( o instanceof
				HydroBase_AgriculturalNASSCropStats ) {
				__record_type = __RECORD_TYPE_AGRICULTURAL_NASS_CROP_STATS;
			}
		}
	}
	_data = data;
	__data_type_prefix = data_type_prefix;
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
	if ( __record_type == __RECORD_TYPE_AGRICULTURAL_CASS_CROP_STATS ) {
		switch (columnIndex) {
			case COL_ID:		return "ID\n(County)";
			case COL_DATA_SOURCE:	return "Data\nSource";
			case COL_DATA_TYPE:	return "Data Type\n" + "(" + __data_type_prefix + "-Commodity_Practice)";
			case COL_TIME_STEP:	return "Time\nStep";
			case COL_UNITS:		return "Units";
			case COL_START:		return "Start";
			case COL_END:		return "End";
			case COL_STATE:		return "State";
			case COL_INPUT_TYPE:	return "Input Type";
			default:		return "";
		}
	}
	else if ( __record_type == __RECORD_TYPE_AGRICULTURAL_NASS_CROP_STATS){
		switch (columnIndex) {
			case COL_ID:		return "ID\n(County)";
			case COL_DATA_SOURCE:	return "Data\nSource";
			case COL_DATA_TYPE:	return "Data Type\n" + "(CropArea-Commodity)";
			case COL_TIME_STEP:	return "Time\nStep";
			case COL_UNITS:		return "Units";
			case COL_START:		return "Start";
			case COL_END:		return "End";
			case COL_STATE:		return "State";
			case COL_INPUT_TYPE:	return "Input Type";
			default:		return "";
		}
	}
	else {	return "";
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

	if ( __record_type == __RECORD_TYPE_AGRICULTURAL_CASS_CROP_STATS ) {
		HydroBase_AgriculturalCASSCropStats ag = (HydroBase_AgriculturalCASSCropStats)_data.get(row);
		switch (col) {
			// case 0 handled above.
			case COL_ID:		return ag.getCounty();
			case COL_DATA_SOURCE:	// Station also has source but want the meas_type source.
						return "CASS";
			case COL_DATA_TYPE:	// TSTool translates to values from the TSTool interface...
						return __data_type_prefix +"-" + ag.getCommodity() + "_"+ ag.getPractice();
			case COL_TIME_STEP:	// TSTool translates HydroBase values to nicer values...
						return "Year";
			case COL_UNITS:		return "ACRE";
			case COL_START:		// For now ignore since full TS is not available...
						return "";
			case COL_END:		// For now ignore since full TS is not available...
						return "";
			case COL_STATE:		return ag.getSt();
			case COL_INPUT_TYPE:	return "HydroBase";
			default:		return "";
		}
	}
	else if ( __record_type == __RECORD_TYPE_AGRICULTURAL_NASS_CROP_STATS){
		HydroBase_AgriculturalNASSCropStats ag = (HydroBase_AgriculturalNASSCropStats)_data.get(row);
		switch (col) {
			// case 0 handled above.
			case COL_ID:		return ag.getCounty();
			case COL_DATA_SOURCE:	// Station also has source but we want the meas_type source.
						return "NASS";
			case COL_DATA_TYPE:	// TSTool translates to values from the TSTool interface...
						return __data_type_prefix +"-" + ag.getCommodity();
			case COL_TIME_STEP:	// TSTool translates HydroBase values to nicer values...
						return "Year";
			case COL_UNITS:		return "ACRE";
			case COL_START:		// For now ignore since full TS is not available...
						return "";
			case COL_END:		// For now ignore since full TS is not available...
						return "";
			case COL_STATE:		return ag.getSt();
			case COL_INPUT_TYPE:	return "HydroBase";
			default:		return "";
		}
	}
	else if ( __record_type == __RECORD_TYPE_AGSTATS ) {
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
	else {	return "";
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