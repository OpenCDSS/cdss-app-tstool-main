package DWR.DMI.tstool;

import java.util.List;

import RTi.DMI.DMIUtil;
import RTi.DMI.RiversideDB_DMI.RiversideDB_MeasTypeMeasLocGeoloc;
import RTi.Util.GUI.JWorksheet_AbstractRowTableModel;
import RTi.Util.String.StringUtil;

/**
This class is a table model for time series header information for RiversideDB
time series.  By default the sheet will contain row and column numbers.
*/
public class TSTool_RiversideDB_TableModel extends JWorksheet_AbstractRowTableModel
{

/**
Number of columns in the table model.
*/
private final int __COLUMNS = 19;

// MeasType
public final int COL_ID = 0;
public final int COL_NAME = 1; // time series description
public final int COL_DATA_SOURCE = 2;
public final int COL_DATA_TYPE = 3;
public final int COL_SUB_TYPE = 4;
public final int COL_TIME_STEP = 5;
public final int COL_SCENARIO = 6;
public final int COL_UNITS = 7;
public final int COL_START = 8;
public final int COL_END = 9;
// MeasLoc
public final int COL_STATION_NAME = 10; // station name
// Geoloc
public final int COL_COUNTY = 11;
public final int COL_STATE = 12;
public final int COL_LATITUDE = 13;
public final int COL_LONGITUDE = 14;
public final int COL_X = 15;
public final int COL_Y = 16;
public final int COL_ELEVATION = 17;
// TSID input type
public final int COL_INPUT_TYPE	= 18;

/**
Constructor.  This builds the model for displaying the given RiversideDB time series data.
@param data the Vector of RiversideDB_MeasType that will be displayed in the
table (null is allowed - see setData()).
@throws Exception if an invalid results passed in.
*/
public TSTool_RiversideDB_TableModel ( List data )
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
From AbstractTableModel.  Returns the class of the data stored in a given column.
@param columnIndex the column for which to return the data class.
*/
public Class getColumnClass (int columnIndex) {
	switch (columnIndex) {
		case COL_ID: return String.class;
		case COL_NAME: return String.class;
		case COL_DATA_SOURCE: return String.class;
		case COL_DATA_TYPE: return String.class;
		case COL_SUB_TYPE: return String.class;
		case COL_TIME_STEP: return String.class;
		case COL_SCENARIO: return String.class;
		case COL_UNITS: return String.class;
		case COL_START: return String.class;
		case COL_END: return String.class;
		case COL_STATION_NAME: return String.class;
	    case COL_COUNTY: return String.class;
	    case COL_STATE: return String.class;
	    /* TODO SAM 2010-03-23 Need to figure out why justification does not work
		case COL_LATITUDE: return Double.class;
		case COL_LONGITUDE: return Double.class;
		case COL_X: return Double.class;
		case COL_Y: return Double.class;
		case COL_ELEVATION: return Double.class;
		*/
        case COL_LATITUDE: return String.class;
        case COL_LONGITUDE: return String.class;
        case COL_X: return String.class;
        case COL_Y: return String.class;
        case COL_ELEVATION: return String.class;
        case COL_INPUT_TYPE: return String.class;
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
		case COL_ID: return "ID";
		case COL_NAME: return "Name/Description";
		case COL_DATA_SOURCE: return "Data Source";
		case COL_DATA_TYPE: return "Data Type";
		case COL_SUB_TYPE: return "Subtype";
		case COL_TIME_STEP: return "Time Step";
		case COL_SCENARIO: return "Scenario";
		case COL_UNITS: return "Units";
		case COL_START: return "Start";
		case COL_END: return "End";
		case COL_STATION_NAME: return "Station Name";
		case COL_COUNTY: return "County";
        case COL_STATE: return "State";
        case COL_LATITUDE: return "Latitude";
        case COL_LONGITUDE: return "Longitude";
        case COL_X: return "X";
        case COL_Y: return "Y";
        case COL_ELEVATION: return "Elevation";
		case COL_INPUT_TYPE: return "Input Type";
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
	/* TODO SAM 2010-03-23 Need to figure out why justification does not work - use strings for now
	    case COL_LATITUDE:
	        return "%10.6f";
	    case COL_LONGITUDE:
            return "%11.6f";
	    case COL_X:
            return "%14.6f";
	    case COL_Y:
            return "%14.6f";
	    case COL_ELEVATION:
            return "%9.3f";
            */
        case COL_LATITUDE:
            return "%10.10s";
        case COL_LONGITUDE:
            return "%11.11s";
        case COL_X:
            return "%14.14s";
        case COL_Y:
            return "%14.14s";
        case COL_ELEVATION:
            return "%9.9s";
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

	RiversideDB_MeasTypeMeasLocGeoloc mt = (RiversideDB_MeasTypeMeasLocGeoloc)_data.get(row);
	switch (col) {
		// case 0 handled above.
		case COL_ID:
		    return mt.getIdentifier();
		case COL_NAME:
		    String description = mt.getDescription();
			if ( description.equals("") ) {
			    // Use the station name 
				description = mt.getMeasLoc_name();
			}
			return description;
		case COL_DATA_SOURCE:
			return mt.getSource_abbrev();
		case COL_DATA_TYPE:
			return mt.getData_type();
	     case COL_SUB_TYPE:
            return mt.getSub_type();
		case COL_TIME_STEP:
			int interval_mult = (int)mt.getTime_step_mult();
			String interval_base = mt.getTime_step_base();
			String timestep = "";
			if ( DMIUtil.isMissing(interval_mult) || StringUtil.startsWithIgnoreCase( interval_base,"IRR") ) {
				timestep = mt.getTime_step_base();
			}
			else {
			    timestep = "" + interval_mult + interval_base;
			}
			return timestep;
		case COL_SCENARIO:
			return mt.getScenario();
		case COL_UNITS:
		    return mt.getUnits_abbrev();
		case COL_START:
		    return "";	// Not yet available
		case COL_END:
		    return "";	// Not yet available
		case COL_STATION_NAME:
            return mt.getMeasLoc_name();
		case COL_COUNTY:
		    return mt.getCounty();
		case COL_STATE:
		    return mt.getState();
		    /* TODO SAM 2010-03-23 Need to figure out why justification does not work
        case COL_LATITUDE:
            double latitude = mt.getLatitude();
            if ( DMIUtil.isMissing(latitude) ) {
                return null;
            }
            else {
                return new Double(latitude);
            }
        case COL_LONGITUDE:
            double longitude = mt.getLongitude();
            if ( DMIUtil.isMissing(longitude)) {
                return null;
            }
            else {
                return new Double(longitude);
            }
        case COL_X:
            double x = mt.getX();
            if ( DMIUtil.isMissing(x)) {
                return null;
            }
            else {
                return new Double(x);
            }
        case COL_Y:
            double y = mt.getY();
            if ( DMIUtil.isMissing(y)) {
                return null;
            }
            else {
                return new Double(y);
            }
        case COL_ELEVATION:
            double elevation = mt.getElevation();
            if ( DMIUtil.isMissing(elevation)) {
                return null;
            }
            else {
                return new Double(elevation);
            }
            */
        case COL_LATITUDE:
            double latitude = mt.getLatitude();
            if ( DMIUtil.isMissing(latitude) ) {
                return "";
            }
            else {
                return StringUtil.formatString(latitude,"%10.6f");
            }
        case COL_LONGITUDE:
            double longitude = mt.getLongitude();
            if ( DMIUtil.isMissing(longitude)) {
                return "";
            }
            else {
                return StringUtil.formatString(longitude,"%11.6f");
            }
        case COL_X:
            double x = mt.getX();
            if ( DMIUtil.isMissing(x)) {
                return null;
            }
            else {
                return StringUtil.formatString(x,"%14.6f");
            }
        case COL_Y:
            double y = mt.getY();
            if ( DMIUtil.isMissing(y)) {
                return null;
            }
            else {
                return StringUtil.formatString(y,"%14.6f");
            }
        case COL_ELEVATION:
            double elevation = mt.getElevation();
            if ( DMIUtil.isMissing(elevation)) {
                return null;
            }
            else {
                return StringUtil.formatString(elevation,"%9.3f");
            }		    
		case COL_INPUT_TYPE:
			return "RiversideDB";
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
	widths[COL_NAME] = 20;
	widths[COL_DATA_SOURCE] = 11;
	widths[COL_DATA_TYPE] = 8;
	widths[COL_SUB_TYPE] = 8;
	widths[COL_TIME_STEP] = 8;
	widths[COL_SCENARIO] = 8;
	widths[COL_UNITS] = 8;
	widths[COL_START] = 10;
	widths[COL_END] = 10;
	widths[COL_STATION_NAME] = 20;
	widths[COL_COUNTY] = 8;
	widths[COL_STATE] = 6;
    widths[COL_LATITUDE] = 10;
    widths[COL_LONGITUDE] = 10;
    widths[COL_X] = 10;
    widths[COL_Y] = 10;
    widths[COL_ELEVATION] = 10;
	widths[COL_INPUT_TYPE] = 12;
	return widths;
}

}