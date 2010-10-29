package DWR.DMI.tstool;

import java.util.List;

import rti.tscommandprocessor.commands.bndss.BNDSSSubjectType;
import rti.tscommandprocessor.commands.bndss.BNDSS_DataMetaData;
import rti.tscommandprocessor.commands.bndss.ColoradoBNDSSDataStore;

import RTi.Util.GUI.JWorksheet_AbstractRowTableModel;

/**
This class is a table model for time series header information for Colorado BNDSS
time series.  By default the sheet will contain row and column numbers.
*/
public class TSTool_ColoradoBNDSS_TableModel extends JWorksheet_AbstractRowTableModel
{

/**
Number of columns in the table model.
*/
private final int __COLUMNS = 14;

// MeasType
public final int COL_SUBJECT_TYPE = 0; // See BNDSSSubjectType enumeration
public final int COL_SUBJECT_ID = 1; // subjectID, same as name (need to resolve length)
public final int COL_SUBJECT_NAME = 2; // subject name
public final int COL_DATA_SOURCE = 3;
public final int COL_DATA_TYPE = 4;
public final int COL_SUB_TYPE = 5;
public final int COL_METHOD = 6;
public final int COL_SUB_METHOD = 7;
public final int COL_TIME_STEP = 8;
public final int COL_SCENARIO = 9;
public final int COL_UNITS = 10;
public final int COL_START = 11;
public final int COL_END = 12;
//public final int COL_LATITUDE = 13;
//public final int COL_LONGITUDE = 14;
//public final int COL_X = 15;
//public final int COL_Y = 16;
// TSID input type
public final int COL_DATASTORE_NAME	= 13;

/**
Data store name corresponding to data store used to retrieve the data.
*/
String __dataStoreName = null;

/**
Constructor.  This builds the model for displaying the given Colorado BNDSS time series data.
@param dataStore the data store for the data
@param data the list of BNDSS_DataMetaData that will be displayed in the
table (null is allowed - see setData()).
@throws Exception if an invalid results passed in.
*/
public TSTool_ColoradoBNDSS_TableModel ( ColoradoBNDSSDataStore dataStore, List data )
throws Exception
{	if ( data == null ) {
		_rows = 0;
	}
	else {
	    _rows = data.size();
	}
    __dataStoreName = dataStore.getName();
	_data = data;
}

/**
From AbstractTableModel.  Returns the class of the data stored in a given column.
@param columnIndex the column for which to return the data class.
*/
public Class getColumnClass (int columnIndex) {
	switch (columnIndex) {
	    case COL_SUBJECT_TYPE: return String.class;
		case COL_SUBJECT_ID: return String.class;
		case COL_SUBJECT_NAME: return String.class;
		case COL_DATA_SOURCE: return String.class;
		case COL_DATA_TYPE: return String.class;
		case COL_SUB_TYPE: return String.class;
	    case COL_METHOD: return String.class;
	    case COL_SUB_METHOD: return String.class;
		case COL_TIME_STEP: return String.class;
		case COL_SCENARIO: return String.class;
		case COL_UNITS: return String.class;
		case COL_START: return String.class;
		case COL_END: return String.class;
        case COL_DATASTORE_NAME: return String.class;
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
        case COL_SUBJECT_TYPE: return "Subject";
		case COL_SUBJECT_ID: return "Subject ID";
		case COL_SUBJECT_NAME: return "Subject Name";
		case COL_DATA_SOURCE: return "Data Source";
		case COL_DATA_TYPE: return "Data Type (main)";
		case COL_SUB_TYPE: return "Data Type (sub)";
	    case COL_METHOD: return "Method (main)";
	    case COL_SUB_METHOD: return "Method (sub)";
		case COL_TIME_STEP: return "Time Step";
		case COL_SCENARIO: return "Scenario";
		case COL_UNITS: return "Units";
		case COL_START: return "Start";
		case COL_END: return "End";
		case COL_DATASTORE_NAME: return "Data Store";
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
		default:
		    return "%s"; // All else are strings.
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
@param col the column for which to return data.
@return the data that should be placed in the JTable at the given row and column.
*/
public Object getValueAt(int row, int col) {
	// make sure the row numbers are never sorted ...
	if (_sortOrder != null) {
		row = _sortOrder[row];
	}

	BNDSS_DataMetaData data = (BNDSS_DataMetaData)_data.get(row);
	switch (col) {
        case COL_SUBJECT_TYPE:
            return data.getSubject();
		case COL_SUBJECT_ID:
		    if ( data.getSubject().equalsIgnoreCase(""+BNDSSSubjectType.COUNTY)) {
		        return "" + data.getName();
		    }
		    else {
		        // TODO SAM 2010-04-29 Need to use human-readable name when available in DB
		        return "" + data.getSubjectID();
		    }
		case COL_SUBJECT_NAME:
		    return data.getName();
		case COL_DATA_SOURCE:
			return data.getSource();
		case COL_DATA_TYPE:
			return data.getDataType();
	    case COL_SUB_TYPE:
            return data.getSubType();
        case COL_METHOD:
            return data.getMethod();
        case COL_SUB_METHOD:
            return data.getSubMethod();
		case COL_TIME_STEP:
			return "Year";
		case COL_SCENARIO:
			return data.getScenario();
		case COL_UNITS:
		    return data.getUnits();
		case COL_START:
		    return "";	// Not yet available
		case COL_END:
		    return "";	// Not yet available
		case COL_DATASTORE_NAME:
			return __dataStoreName;
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
	widths[COL_SUBJECT_TYPE] = 8;
	widths[COL_SUBJECT_ID] = 12;
	widths[COL_SUBJECT_NAME] = 20;
	widths[COL_DATA_SOURCE] = 11;
	widths[COL_DATA_TYPE] = 11;
	widths[COL_SUB_TYPE] = 10;
	widths[COL_METHOD] = 10;
	widths[COL_SUB_METHOD] = 9;
	widths[COL_TIME_STEP] = 8;
	widths[COL_SCENARIO] = 8;
	widths[COL_UNITS] = 8;
	widths[COL_START] = 10;
	widths[COL_END] = 10;
	widths[COL_DATASTORE_NAME] = 12;
	return widths;
}

}