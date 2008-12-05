package DWR.DMI.tstool;

import java.util.List;

import DWR.DMI.HydroBaseDMI.HydroBase_WaterDistrict;
import DWR.DMI.HydroBaseDMI.HydroBase_GroundWaterWellsView;

import RTi.DMI.DMIUtil;
import RTi.Util.GUI.JWorksheet;
import RTi.Util.GUI.JWorksheet_AbstractRowTableModel;

/**
This class is a table model for time series header information for HydroBase
daily well level time series.  By default the sheet will contain row and column numbers.
*/
public class TSTool_HydroBase_WellLevel_Day_TableModel
extends JWorksheet_AbstractRowTableModel
{

/**
Number of columns in the table model, including the row number.
*/
private final int __COLUMNS = 18;

public final int COL_ID = 		0;
public final int COL_NAME = 		1;
public final int COL_DATA_SOURCE = 	2;
public final int COL_DATA_TYPE = 	3;
public final int COL_TIME_STEP = 	4;
public final int COL_UNITS = 		5;
public final int COL_START = 		6;
public final int COL_END = 		7;
public final int COL_MEAS_COUNT = 	8;
public final int COL_DIV = 		9;
public final int COL_DIST = 		10;
public final int COL_COUNTY = 		11;
public final int COL_STATE = 		12;
public final int COL_HUC = 		13;
public final int COL_BASIN = 		14;
public final int COL_DSS_AQUIFER1 =	15;
public final int COL_DSS_AQUIFER2 = 	16;
public final int COL_INPUT_TYPE = 	17;

private int __wdid_length = 7; // The length to use when formatting WDIDs in IDs.

/**
Constructor.  This builds the model for displaying the given HydroBase time series data.
@param worksheet the JWorksheet that displays the data from the table model.
@param wdid_length Total length to use when formatting WDIDs.
@param data the Vector of HydroBase_GroundWaterWellsView objects to display in 
the table (null is allowed - see setData()).
@throws Exception if an invalid results passed in.
*/
public TSTool_HydroBase_WellLevel_Day_TableModel (JWorksheet worksheet, int wdid_length, List data )
throws Exception
{	__wdid_length = wdid_length;
	if ( data == null ) {
		_rows = 0;
	}
	else {
	    _rows = data.size();
	}
	_data = data;
}

/**
From AbstractTableModel.  Returns the class of the data stored in a given column.
@param columnIndex the column for which to return the data class.
*/
public Class getColumnClass (int columnIndex) {
	switch (columnIndex) {
		default:			return String.class;
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
		case COL_ID:			return "ID";
		case COL_NAME:			return "Name/Description";
		case COL_DATA_SOURCE:		return "Data Source";
		case COL_DATA_TYPE:		return "Data Type";
		case COL_TIME_STEP:		return "Time Step";
		case COL_UNITS:			return "Units";
		case COL_START:			return "Start";
		case COL_END:			return "End";
		case COL_MEAS_COUNT:		return "Meas. Count";
		case COL_DIV:			return "Div.";
		case COL_DIST:			return "Dist.";
		case COL_COUNTY:		return "County";
		case COL_STATE:			return "State";
		case COL_HUC:			return "HUC";
		case COL_BASIN:			return "Basin";
		case COL_DSS_AQUIFER1:		return "DSS Aquifer 1";
		case COL_DSS_AQUIFER2:		return "DSS Aquifer 2";
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

	int i;	// Use for integer data.

	HydroBase_GroundWaterWellsView wv = (HydroBase_GroundWaterWellsView) _data.get(row);

	switch (col) {
		// case 0 handled above.
		case COL_ID:		
					if ( wv.getIdentifier().length() > 0 ) {
						// Well with a different identifier to display.
						return
						wv.getIdentifier();
					}
					else {
					    // A structure other than wells...
						return HydroBase_WaterDistrict.formWDID (__wdid_length, wv.getWD(), wv.getID() );
					}
		case COL_NAME: 		return wv.getWell_name();
		case COL_DATA_SOURCE:	return wv.getData_source();
		case COL_DATA_TYPE:	// TSTool translates to values from the TSTool interface...
					return "WellLevel";
		case COL_TIME_STEP:	// TSTool translates HydroBase values to nicer values...
					return wv.getTime_step();
		case COL_UNITS: // The units are not in HydroBase.meas_type but are set by TSTool...
					return wv.getData_units();
		case COL_START: //return new Integer(wv.getStart_year() );
					i = wv.getStart_year();
					if ( DMIUtil.isMissing(i) ) {
						return "";
					}
					else {
					    return "" + i;
					}
		case COL_END: //return new Integer (wv.getEnd_year() );
					i = wv.getEnd_year();
					if ( DMIUtil.isMissing(i) ) {
						return "";
					}
					else {
					    return "" + i;
					}
		case COL_MEAS_COUNT: i = wv.getMeas_count();
					if ( DMIUtil.isMissing(i) ) {
						return "";
					}
					else {
					    return "" + i;
					}
		case COL_DIV: //return new Integer ( wv.getDiv() );
					i = wv.getDiv();
					if ( DMIUtil.isMissing(i) ) {
						return "";
					}
					else {
					    return "" + i;
					}
		case COL_DIST: //return new Integer ( wv.getWD() );
					i = wv.getWD();
					if ( DMIUtil.isMissing(i) ) {
						return "";
					}
					else {
					    return "" + i;
					}
		case COL_COUNTY:	return wv.getCounty();
		case COL_STATE:		return wv.getST();
		case COL_HUC:		return wv.getHUC();
		case COL_BASIN:		return wv.getBasin();
		case COL_DSS_AQUIFER1:	return wv.getDSS_aquifer1();
		case COL_DSS_AQUIFER2:	return wv.getDSS_aquifer2();
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
	widths[COL_ID] = 12;		// ID
	widths[COL_NAME] = 20;		// Name/Description
	widths[COL_DATA_SOURCE] = 10;	// Data Source
	widths[COL_DATA_TYPE] = 15;	// Data Type
	widths[COL_TIME_STEP] = 8;	// Time Step
	widths[COL_UNITS] = 8;		// Units
	widths[COL_START] = 10;		// Start
	widths[COL_END] = 10;		// End
	widths[COL_MEAS_COUNT] = 8;	// Meas. count
	widths[COL_DIV] = 5;		// Division
	widths[COL_DIST] = 5;		// District
	widths[COL_COUNTY] = 8;		// County
	widths[COL_STATE] = 3;		// State
	widths[COL_HUC] = 8;		// HUC
	widths[COL_BASIN] = 20;	// Basin
	widths[COL_DSS_AQUIFER1] = 10;	// DSS Aquifer 1
	widths[COL_DSS_AQUIFER2] = 10;	// DSS Aquifer 2
	widths[COL_INPUT_TYPE] = 12;	// Input Type
	return widths;
}

/**
Set the width of WDIDs, which controls formatting of the ID column for structures.
@param wdid_length WDID length for formatting the ID.
*/
public void setWDIDLength ( int wdid_length )
{	__wdid_length = wdid_length;
}

}