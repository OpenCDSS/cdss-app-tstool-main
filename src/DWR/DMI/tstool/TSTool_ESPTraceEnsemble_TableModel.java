// TSTool_ESPTraceEnsemble_TableModel - table model for NWSRFS_ESPTraceEnsemble header information for TS instances.

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

import RTi.DMI.NWSRFS_DMI.NWSRFS_ESPTraceEnsemble;
import RTi.TS.TS;
import RTi.Util.GUI.JWorksheet_AbstractRowTableModel;

/**
This class is a table model for NWSRFS_ESPTraceEnsemble header information for
TS instances.  By default the sheet will contain row and column numbers.
*/
public class TSTool_ESPTraceEnsemble_TableModel extends JWorksheet_AbstractRowTableModel
{

/**
Number of columns in the table model (without the alias).
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
Local reference to NWSRFS_ESPTraceEnsemble.
*/
//TODO SAM evaluate if needed
//private NWSRFS_ESPTraceEnsemble __ensemble = null;

/**
Constructor.  This builds the model for displaying the given time series data.
@param ensemble the NWSRFS_ESPTraceEnsemble that will be displayed in the table (null is allowed).
@throws Exception if an invalid results passed in.
*/
public TSTool_ESPTraceEnsemble_TableModel ( NWSRFS_ESPTraceEnsemble ensemble )
throws Exception
{	this ( ensemble, true );
}

/**
Constructor.  This builds the model for displaying the given time series data.
@param ensemble the NWSRFS_ESPTraceEnsemble that will be displayed in the table (null is allowed).
@param include_alias If true, an alias column will be included after the location column.
@throws Exception if an invalid results passed in.
*/
public TSTool_ESPTraceEnsemble_TableModel (	NWSRFS_ESPTraceEnsemble ensemble, boolean include_alias )
throws Exception
{	//TODO SAM
	//__ensemble = ensemble;
	if ( (ensemble == null) || (ensemble.getTimeSeriesList() == null) ) {
		_rows = 0;
	}
	else {
	    _rows = ensemble.getTimeSeriesList().size();
	}
	// TODO - should _data be set to "ensemble"?
	_data = ensemble.getTimeSeriesList();
}

/**
From AbstractTableModel.  Returns the class of the data stored in a given column.
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
		case COL_SEQUENCE: return Integer.class;
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
		case COL_ID: return "ID\n(Location)";
		case COL_ALIAS: return "Alias";
		case COL_NAME: return "Name/Description";
		case COL_DATA_SOURCE: return "Data\nSource";
		case COL_DATA_TYPE: return "Data\nType";
		case COL_TIME_STEP: return "Time\nStep";
		case COL_SCENARIO: return "Scenario";
		case COL_SEQUENCE: return "Trace #\n(Year)";
		case COL_UNITS: return "Units";
		case COL_START: return "Start";
		case COL_END: return "End";
		case COL_INPUT_TYPE: return "Input Type";
		case COL_INPUT_NAME: return "Input File";
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
		default: return "%s"; // All are strings.
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
@return the data that should be placed in the JTable at the given row and column.
*/
public Object getValueAt(int row, int col) {
	// make sure the row numbers are never sorted ...
	if (_sortOrder != null) {
		row = _sortOrder[row];
	}

	TS ts = (TS)_data.get(row);
	switch (col) {
		case COL_ID: return ts.getIdentifier().getLocation();
		case COL_ALIAS: return ts.getAlias();
		case COL_NAME: return ts.getDescription();
		case COL_DATA_SOURCE: return ts.getIdentifier().getSource();
		case COL_DATA_TYPE: return ts.getDataType();
		case COL_TIME_STEP: return ts.getIdentifier().getInterval();
		case COL_SCENARIO: return ts.getIdentifier().getScenario();
		case COL_SEQUENCE: return ts.getSequenceID();
		case COL_UNITS: return ts.getDataUnits();
		case COL_START: return ts.getDate1();
		case COL_END: return ts.getDate2();
		case COL_INPUT_TYPE: return ts.getIdentifier().getInputType();
		case COL_INPUT_NAME: return ts.getIdentifier().getInputName();
		default: return "";
	}
}

/**
Returns an array containing the column widths (in number of characters).
@return an integer array containing the widths for each field.
*/
public int[] getColumnWidths() {
	int[] widths = new int[__COLUMNS];
	widths[COL_ID] = 8;
	widths[COL_ALIAS] = 12;
	widths[COL_NAME] = 16;
	widths[COL_DATA_SOURCE] = 5;
	widths[COL_DATA_TYPE] = 3;
	widths[COL_TIME_STEP] = 3;
	widths[COL_SCENARIO] = 8;
	widths[COL_SEQUENCE] = 5;
	widths[COL_UNITS] = 3;
	widths[COL_START] = 11;
	widths[COL_END] = 11;
	widths[COL_INPUT_TYPE] = 15;
	widths[COL_INPUT_NAME] = 20;
	return widths;
}

}
