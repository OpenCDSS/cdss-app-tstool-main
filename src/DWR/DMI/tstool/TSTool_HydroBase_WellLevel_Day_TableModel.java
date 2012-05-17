package DWR.DMI.tstool;

import java.util.List;

import DWR.DMI.HydroBaseDMI.HydroBase_WaterDistrict;
import DWR.DMI.HydroBaseDMI.HydroBase_GroundWaterWellsView;

import RTi.DMI.DMIUtil;
import RTi.Util.GUI.JWorksheet;
import RTi.Util.GUI.JWorksheet_AbstractRowTableModel;
import RTi.Util.String.StringUtil;

/**
This class is a table model for time series header information for HydroBase
daily well level time series with information stored in HydroBase_GroundWaterWellsView objects.
By default the sheet will contain row and column numbers.
*/
public class TSTool_HydroBase_WellLevel_Day_TableModel
extends JWorksheet_AbstractRowTableModel
{

/**
Number of columns in the table model, including the row number.
*/
private final int __COLUMNS = 22;

public final int COL_ID = 0;
public final int COL_NAME = 1;
public final int COL_DATA_SOURCE = 2;
public final int COL_DATA_TYPE = 3;
public final int COL_TIME_STEP = 4;
public final int COL_UNITS = 5;
public final int COL_START = 6;
public final int COL_END = 7;
public final int COL_MEAS_COUNT = 8;
public final int COL_DIV = 9;
public final int COL_DIST = 10;
public final int COL_COUNTY = 11;
public final int COL_STATE = 12;
public final int COL_HUC = 13;
public final int COL_BASIN = 14;
public final int COL_DSS_AQUIFER1 =	15;
public final int COL_DSS_AQUIFER2 = 16;
public final int COL_LONG = 17;
public final int COL_LAT = 18;
public final int COL_UTM_X = 19;
public final int COL_UTM_Y = 20;
public final int COL_INPUT_TYPE = 21;

private int __wdid_length = 7; // The length to use when formatting WDIDs in IDs.

String __inputType = "";

/**
Constructor.  This builds the model for displaying the given HydroBase time series data.
@param worksheet the JWorksheet that displays the data from the table model.
@param wdid_length Total length to use when formatting WDIDs.
@param data the list of HydroBase_GroundWaterWellsView objects to display in 
the table (null is allowed - see setData()).
@param inputType the input type for the TSID, "HydroBase" or a data store name
@throws Exception if an invalid results passed in.
*/
public TSTool_HydroBase_WellLevel_Day_TableModel (JWorksheet worksheet, int wdid_length, List data,
    String inputType )
throws Exception
{	__wdid_length = wdid_length;
	if ( data == null ) {
		_rows = 0;
	}
	else {
	    _rows = data.size();
	}
	_data = data;
	__inputType = inputType;
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
		case COL_TIME_STEP: return "Time Step";
		case COL_UNITS: return "Units";
		case COL_START: return "Start";
		case COL_END: return "End";
		case COL_MEAS_COUNT: return "Meas. Count";
		case COL_DIV: return "Div.";
		case COL_DIST: return "Dist.";
		case COL_COUNTY: return "County";
		case COL_STATE: return "State";
		case COL_HUC: return "HUC";
		case COL_BASIN: return "Basin";
		case COL_DSS_AQUIFER1: return "DSS Aquifer 1";
		case COL_DSS_AQUIFER2: return "DSS Aquifer 2";
        case COL_LONG: return "Longtitude";
        case COL_LAT: return "Latitude";
        case COL_UTM_X: return "UTM X";
        case COL_UTM_Y: return "UTM Y";
		case COL_INPUT_TYPE: return "Input Type";
		default: return "";
	}
}

/**
Returns column tooltips.
@return column tooltips
@param columnIndex column index (0+, using COL_*)
*/
public String [] getColumnToolTips ()
{
    String [] toolTips = new String[__COLUMNS];
    toolTips[COL_ID] = "Unique well identifier, typically the data source's primary identifier";
    toolTips[COL_NAME] = "Well name";
    toolTips[COL_DATA_SOURCE] = "Current source of the data, organization abbreviation";
    toolTips[COL_DATA_TYPE] = "Data type used by software, typically consistent with HydroBase";
    toolTips[COL_TIME_STEP] = "Time step consistent with software";
    toolTips[COL_UNITS] = "Data units";
    toolTips[COL_START] = "Start of time series data (time series data records may be more precise)";
    toolTips[COL_END] = "End of time series data (time series data records may be more precise)";
    toolTips[COL_MEAS_COUNT] = "Number of observations";
    toolTips[COL_DIV] = "Water division";
    toolTips[COL_DIST] = "Water district";
    toolTips[COL_COUNTY] = "County";
    toolTips[COL_STATE] = "State";
    toolTips[COL_HUC] = "Hydrlogic unit code (HUC)";
    toolTips[COL_BASIN] = "Groundwater basin";
    toolTips[COL_DSS_AQUIFER1] = "Decision support system (DSS) Aquifer 1";
    toolTips[COL_DSS_AQUIFER2] = "Decision support system (DSS) Aquifer 2";
    toolTips[COL_LONG] = "Longitude decimal degrees";
    toolTips[COL_LAT] = "Latitude decimal degrees";
    toolTips[COL_UTM_X] = "UTM X, meters";
    toolTips[COL_UTM_Y] = "UTM Y, meters";
    toolTips[COL_INPUT_TYPE] = "Data store name";
    return toolTips;
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

	int i; // Use for integer data.
	double d; // Use for double data.

	HydroBase_GroundWaterWellsView wv = (HydroBase_GroundWaterWellsView) _data.get(row);

	switch (col) {
		// case 0 handled above.
		case COL_ID:		
			if ( wv.getIdentifier().length() > 0 ) {
				// Well with a different identifier to display.
				return wv.getIdentifier();
			}
			else {
			    // A structure other than wells...
				return HydroBase_WaterDistrict.formWDID (__wdid_length, wv.getWD(), wv.getID() );
			}
		case COL_NAME:
		    return wv.getWell_name();
		case COL_DATA_SOURCE:
		    return wv.getData_source();
		case COL_DATA_TYPE:
			return wv.getMeas_type(); // May be WellLevel, WellLevelElev, WellLevelDepth, etc.
		case COL_TIME_STEP:	// TSTool translates HydroBase values to nicer values...
			return wv.getTime_step();
		case COL_UNITS: // The units are not in HydroBase.meas_type but are set by TSTool...
			return wv.getData_units();
		case COL_START:
		    //return new Integer(wv.getStart_year() );
			i = wv.getStart_year();
			if ( DMIUtil.isMissing(i) ) {
				return "";
			}
			else {
			    return "" + i;
			}
		case COL_END:
		    //return new Integer (wv.getEnd_year() );
			i = wv.getEnd_year();
			if ( DMIUtil.isMissing(i) ) {
				return "";
			}
			else {
			    return "" + i;
			}
		case COL_MEAS_COUNT:
		    i = wv.getMeas_count();
			if ( DMIUtil.isMissing(i) ) {
				return "";
			}
			else {
			    return "" + i;
			}
		case COL_DIV:
		    //return new Integer ( wv.getDiv() );
			i = wv.getDiv();
			if ( DMIUtil.isMissing(i) ) {
				return "";
			}
			else {
			    return "" + i;
			}
		case COL_DIST:
		    //return new Integer ( wv.getWD() );
			i = wv.getWD();
			if ( DMIUtil.isMissing(i) ) {
				return "";
			}
			else {
			    return "" + i;
			}
		case COL_COUNTY:
		    return wv.getCounty();
		case COL_STATE:
		    return wv.getST();
		case COL_HUC:
		    return wv.getHUC();
		case COL_BASIN:
		    return wv.getBasin();
		case COL_DSS_AQUIFER1:
		    return wv.getDSS_aquifer1();
		case COL_DSS_AQUIFER2:
		    return wv.getDSS_aquifer2();
        case COL_LONG:
            d = wv.getLongdecdeg();
            if ( DMIUtil.isMissing(d) ) {
                return "";
            }
            else {
                return "" + StringUtil.formatString(d,"%.6f");
            }
        case COL_LAT:
            d = wv.getLatdecdeg();
            if ( DMIUtil.isMissing(d) ) {
                return "";
            }
            else {
                return "" + StringUtil.formatString(d,"%.6f");
            }
        case COL_UTM_X:
            d = wv.getUtm_x();
            if ( DMIUtil.isMissing(d) ) {
                return "";
            }
            else {
                return "" + StringUtil.formatString(d,"%.3f");
            }
        case COL_UTM_Y:
            d = wv.getUtm_y();
            if ( DMIUtil.isMissing(d) ) {
                return "";
            }
            else {
                return "" + StringUtil.formatString(d,"%.3f");
            }
		case COL_INPUT_TYPE:
		    return __inputType;
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
	widths[COL_ID] = 12; // ID
	widths[COL_NAME] = 20; // Name/Description
	widths[COL_DATA_SOURCE] = 10; // Data Source
	widths[COL_DATA_TYPE] = 15; // Data Type
	widths[COL_TIME_STEP] = 8; // Time Step
	widths[COL_UNITS] = 8; // Units
	widths[COL_START] = 10; // Start
	widths[COL_END] = 10; // End
	widths[COL_MEAS_COUNT] = 8; // Meas. count
	widths[COL_DIV] = 5; // Division
	widths[COL_DIST] = 5; // District
	widths[COL_COUNTY] = 8; // County
	widths[COL_STATE] = 3; // State
	widths[COL_HUC] = 8; // HUC
	widths[COL_BASIN] = 20; // Basin
	widths[COL_DSS_AQUIFER1] = 10; // DSS Aquifer 1
	widths[COL_DSS_AQUIFER2] = 10; // DSS Aquifer 2
    widths[COL_LONG] = 8;
    widths[COL_LAT] = 8;
    widths[COL_UTM_X] = 8;
    widths[COL_UTM_Y] = 8;
	widths[COL_INPUT_TYPE] = 12; // Input Type
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