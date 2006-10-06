// ----------------------------------------------------------------------------
// TSTool_RiversideDB_TableModel - Table Model for a Vector of
//					RiversideDB_MeasType to be
//					displayed as time series headers
// ----------------------------------------------------------------------------
// Copyright:   See the COPYRIGHT file
// ----------------------------------------------------------------------------
// History:
//
// 2003-06-16	Steven A. Malers, RTi	Initial version.  Copy some HydroBase
//					code and modify.
// 2003-11-26	SAM, RTi		Define column positions as final ints,
//					for use in other code if necessary.
// 2004-01-22	SAM, RTi		Update to new JWorksheet row heading
//					standard.
// ----------------------------------------------------------------------------

package DWR.DMI.tstool;

import java.util.Date;
import java.util.Vector;

import RTi.DMI.DMIUtil;
import RTi.DMI.RiversideDB_DMI.RiversideDB_MeasType;
import RTi.Util.GUI.JWorksheet_AbstractRowTableModel;
import RTi.Util.String.StringUtil;

/**
This class is a table model for time series header information for RiversideDB
time series.  By default the sheet will contain row and column numbers.
*/
public class TSTool_RiversideDB_TableModel
extends JWorksheet_AbstractRowTableModel
{

/**
Number of columns in the table model.
*/
private final int __COLUMNS = 12;

public final int COL_ID = 0;
public final int COL_NAME = 1;
public final int COL_DATA_SOURCE= 2;
public final int COL_DATA_TYPE = 3;
public final int COL_TIME_STEP = 4;
public final int COL_SCENARIO = 5;
public final int COL_UNITS = 6;
public final int COL_START = 7;
public final int COL_END = 8;
public final int COL_COUNTY = 9;
public final int COL_STATE = 10;
public final int COL_INPUT_TYPE	= 11;

/**
Constructor.  This builds the model for displaying the given RiversideDB time
series data.
@param data the Vector of RiversideDB_MeasType that will be displayed in the
table (null is allowed - see setData()).
@throws Exception if an invalid results passed in.
*/
public TSTool_RiversideDB_TableModel ( Vector data )
throws Exception
{	if ( data == null ) {
		_rows = 0;
	}
	else {	_rows = data.size();
	}
	_data = data;
}

/**
From AbstractTableModel.  Returns the class of the data stored in a given
column.
@param columnIndex the column for which to return the data class.
*/
public Class getColumnClass (int columnIndex) {
	switch (columnIndex) {
		case COL_ID:		return String.class;
		case COL_NAME:		return String.class;
		case COL_DATA_SOURCE:	return String.class;
		case COL_DATA_TYPE:	return String.class;
		case COL_TIME_STEP:	return String.class;
		case COL_SCENARIO:	return String.class;
		case COL_UNITS:		return String.class;
		case COL_START:		return String.class;
		case COL_END:		return String.class;
		case COL_COUNTY:	return String.class;
		case COL_STATE:		return String.class;
		case COL_INPUT_TYPE:	return String.class;
		default:		return String.class;
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
		case COL_ID:		return "ID";
		case COL_NAME:		return "Name/Description";
		case COL_DATA_SOURCE:	return "Data Source";
		case COL_DATA_TYPE:	return "Data Type";
		case COL_TIME_STEP:	return "Time Step";
		case COL_SCENARIO:	return "Scenario";
		case COL_UNITS:		return "Units";
		case COL_START:		return "Start";
		case COL_END:		return "End";
		case COL_COUNTY:	return "County";
		case COL_STATE:		return "State";
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
From AbstractTableMode.  Returns the data that should be placed in the JTable
at the given row and column.
@param row the row for which to return data.
@param col the column for which to return data.
@return the data that should be placed in the JTable at the given row and col.
*/
public Object getValueAt(int row, int col) {
	// make sure the row numbers are never sorted ...
	if (_sortOrder != null) {
		row = _sortOrder[row];
	}

	RiversideDB_MeasType mt = (RiversideDB_MeasType)_data.elementAt(row);
	switch (col) {
		// case 0 handled above.
		case COL_ID:	return mt.getIdentifier();
		case COL_NAME: 	String description = mt.getDescription();
				if ( description.equals("") ) {
					description = mt.getMeasLoc_name();
				}
				return description;
		case COL_DATA_SOURCE:
				return mt.getSource_abbrev();
		case COL_DATA_TYPE:
			 	String subtype = "";
				if ( !mt.getSub_type().equals("") ) {
					subtype = "-" + mt.getSub_type();
				}
				return mt.getData_type() + subtype;
			
		case COL_TIME_STEP:
				int interval_mult = (int)mt.getTime_step_mult();
				String interval_base = mt.getTime_step_base();
				String timestep = "";
				if (	DMIUtil.isMissing(interval_mult) ||
					StringUtil.startsWithIgnoreCase(
					interval_base,"IRR") ) {
					timestep = mt.getTime_step_base();
				}
				else {	timestep = "" + interval_mult +
						interval_base;
				}
				return timestep;
		case COL_SCENARIO:
				return mt.getScenario();
		case COL_UNITS:	return mt.getUnits_abbrev();
		case COL_START:	return "";	// Not yet available
		case COL_END:	return "";	// Not yet available
		case COL_COUNTY:return "";	// Not yet available
		case COL_STATE:	return "";	// Not yet available
		case COL_INPUT_TYPE:
				return "RiversideDB";
		default:	return "";
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
	widths[COL_DATA_TYPE] = 8;
	widths[COL_TIME_STEP] = 8;
	widths[COL_SCENARIO] = 8;
	widths[COL_UNITS] = 8;
	widths[COL_START] = 10;
	widths[COL_END] = 10;
	widths[COL_COUNTY] = 0;
	widths[COL_STATE] = 0;
	widths[COL_INPUT_TYPE] = 12;
	return widths;
}

} // End TSTool_RiversideDB_TableModel
