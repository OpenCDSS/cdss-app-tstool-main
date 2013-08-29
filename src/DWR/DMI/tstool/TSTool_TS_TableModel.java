package DWR.DMI.tstool;

import java.util.List;

import RTi.TS.TS;
import RTi.Util.GUI.JWorksheet_AbstractRowTableModel;

/**
This class is a table model for time series header information for TS instances.
By default the sheet will contain row and column numbers.
The alias can be treated as a hidden column.
This class may eventually be moved to the RTi.TS package.
*/
public class TSTool_TS_TableModel extends JWorksheet_AbstractRowTableModel
{

/**
Number of columns in the table model (with the alias).
*/
private int __COLUMNS = 13;

/**
Absolute column indices, for column lookups (includes the alias).
*/
public final int COL_ID = 0;
public final int COL_ALIAS = 1;
public final int COL_NAME = 2;
public final int COL_DATA_SOURCE= 3;
public final int COL_DATA_TYPE = 4;
public final int COL_TIME_STEP = 5;
public final int COL_SCENARIO = 6;
public final int COL_SEQUENCE = 7;
public final int COL_UNITS = 8;
public final int COL_START = 9;
public final int COL_END = 10;
public final int COL_INPUT_TYPE	= 11;
public final int COL_INPUT_NAME	= 12;

/**
Constructor.  This builds the model for displaying the given time series data.
@param data the list of TS that will be displayed in the table (null is allowed).
@throws Exception if an invalid results passed in.
*/
public TSTool_TS_TableModel ( List data )
throws Exception
{	this ( data, false );
}

/**
Constructor.  This builds the model for displaying the given time series data.
@param data the Vector of TS that will be displayed in the table (null is allowed).
@param include_alias If true, an alias column will be included after the
location column.  The JWorksheet.removeColumn ( COL_ALIAS ) method should be called.
@throws Exception if an invalid results passed in.
*/
public TSTool_TS_TableModel ( List data, boolean include_alias )
throws Exception
{	if ( data == null ) {
		_rows = 0;
	}
	else {
	    _rows = data.size();
	}
	_data = data;
}

/**
From AbstractTableModel.  Returns the class of the data stored in a given
column.  All values are treated as strings.
@param columnIndex the column for which to return the data class.
*/
public Class getColumnClass (int columnIndex) {
	switch (columnIndex) {
		case COL_ID: return String.class;
		case COL_ALIAS: return String.class;
		case COL_NAME: return String.class;
		case COL_DATA_SOURCE: return String.class;
		case COL_DATA_TYPE: return String.class;
		case COL_TIME_STEP: return String.class;
		case COL_SCENARIO: return String.class;
		case COL_SEQUENCE: return String.class;
		case COL_UNITS: return String.class;
		case COL_START: return String.class;
		case COL_END: return String.class;
		case COL_INPUT_TYPE: return String.class;
		case COL_INPUT_NAME: return String.class;
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
		case COL_ID: return "\nID";
		case COL_ALIAS: return "\nAlias";
		case COL_NAME: return "Name/\nDescription";
		case COL_DATA_SOURCE: return "Data\nSource";
		case COL_DATA_TYPE: return "Data\nType";
		case COL_TIME_STEP: return "Time\nStep";
		case COL_SCENARIO: return "\nScenario";
		case COL_SEQUENCE: return "Sequence\nNumber";
		case COL_UNITS: return "\nUnits";
		case COL_START: return "\nStart";
		case COL_END: return "\nEnd";
		case COL_INPUT_TYPE: return "Input\nType";
		case COL_INPUT_NAME: return "Input\nName";
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
		default:	return "%s";
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

	TS ts = (TS)_data.get(row);
	if ( ts == null ) {
		return "";
	}
	switch (col) {
		case COL_ID: return ts.getIdentifier().getLocation();
		case COL_ALIAS: return ts.getAlias();
		case COL_NAME: return ts.getDescription();
		case COL_DATA_SOURCE: return ts.getIdentifier().getSource();
		case COL_DATA_TYPE: return ts.getDataType();
		case COL_TIME_STEP: return ts.getIdentifier().getInterval();
		case COL_SCENARIO: return ts.getIdentifier().getScenario();
		case COL_SEQUENCE:
		    if ( ts.getSequenceNumber() < 0 ) {
				return "";
			}
			else {
			    return "" + ts.getSequenceNumber();
			}
		case COL_UNITS:
		    return ts.getDataUnits();
		case COL_START:
		    return ts.getDate1();
		case COL_END:
		    return ts.getDate2();
		case COL_INPUT_TYPE:
		    return ts.getIdentifier().getInputType();
		case COL_INPUT_NAME:
		    return ts.getIdentifier().getInputName();
		default:
		    return "";
	}
}

/**
Returns an array containing the column widths (in number of characters).
@return an integer array containing the widths for each field.
*/
public int[] getColumnWidths() {
	int[] widths = new int[__COLUMNS];
	widths[COL_ID] = 12;
	widths[COL_ALIAS] = 12;
	widths[COL_NAME] = 20;
	widths[COL_DATA_SOURCE] = 10;
	widths[COL_DATA_TYPE] = 8;
	widths[COL_TIME_STEP] = 8;
	widths[COL_SCENARIO] = 8;
	widths[COL_SEQUENCE] = 8;
	widths[COL_UNITS] = 8;
	widths[COL_START] = 10;
	widths[COL_END] = 10;
	widths[COL_INPUT_TYPE] = 12;
	widths[COL_INPUT_NAME] = 20;
	return widths;
}

}