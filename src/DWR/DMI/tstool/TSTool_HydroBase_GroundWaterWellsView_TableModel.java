// TSTool_HydroBase_GroundWaterWellsView_TableModel - table model for time series header
// information for HydroBase well time series that are not stored with structures.

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

import DWR.DMI.HydroBaseDMI.HydroBase_WaterDistrict;
import DWR.DMI.HydroBaseDMI.HydroBase_GroundWaterWellsView;
import DWR.DMI.HydroBaseDMI.HydroBase_Util;
import RTi.DMI.DMIUtil;
import RTi.Util.GUI.JWorksheet;
import RTi.Util.GUI.JWorksheet_AbstractRowTableModel;
import RTi.Util.String.StringUtil;

/**
public class TSTool_HydroBase_GroundWaterWellsView_TableModel
This class is a table model for time series header information for HydroBase well time series that are not stored
with structures.
By default the sheet will contain row and column numbers.
*/
@SuppressWarnings("serial")
public class TSTool_HydroBase_GroundWaterWellsView_TableModel
extends JWorksheet_AbstractRowTableModel<HydroBase_GroundWaterWellsView>
{

/**
Number of columns in the table model, including the row number.
*/
private final int __COLUMNS = 19;

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
public final int COL_LONG = 14;
public final int COL_LAT = 15;
public final int COL_UTM_X = 16;
public final int COL_UTM_Y = 17;
public final int COL_INPUT_TYPE = 18;

private int __wdid_length = 7; // The length to use when formatting WDIDs in IDs.

/**
Input type for time series identifier (default to "HydroBase" but can be set to allow class to be used
with other State-related data such as web services).
*/
private String __inputType = "HydroBase";

/**
Constructor.  This builds the model for displaying the given HydroBase time series data.
The input type defaults to "HydroBase".
@param worksheet the JWorksheet that displays the data from the table model.
@param wdid_length Total length to use when formatting WDIDs.
@param data the list of HydroBase_StationGeolocMeasType or HydroBase_StructureGeolocStructMeasType
that will be displayed in the table (null is allowed - see setData()).
@inputName input name for time series (default if not specified is "HydroBase").
@throws Exception if an invalid results passed in.
*/
public TSTool_HydroBase_GroundWaterWellsView_TableModel ( JWorksheet worksheet, int wdid_length,
	List<HydroBase_GroundWaterWellsView> data )
throws Exception
{
    this ( worksheet, wdid_length, data, null );
}

/**
Constructor.  This builds the model for displaying the given HydroBase time series data.
@param worksheet the JWorksheet that displays the data from the table model.
@param wdid_length Total length to use when formatting WDIDs.
@param data the list of HydroBase_StationGeolocMeasType or HydroBase_StructureGeolocStructMeasType
that will be displayed in the table (null is allowed - see setData()).
@inputType input type for time series (default if null or blank is "HydroBase").
@throws Exception if an invalid results passed in.
*/
public TSTool_HydroBase_GroundWaterWellsView_TableModel ( JWorksheet worksheet, int wdid_length,
	List<HydroBase_GroundWaterWellsView> data, String inputType )
throws Exception
{	__wdid_length = wdid_length;
	if ( data == null ) {
		_rows = 0;
	}
	else {
	    _rows = data.size();
	}
	_data = data;
	if ( (inputType != null) && !inputType.equals("") ) {
	    __inputType = inputType;
	}
}

/**
From AbstractTableModel.  Returns the class of the data stored in a given column.
@param columnIndex the column for which to return the data class.
*/
public Class<?> getColumnClass (int columnIndex) {
	switch (columnIndex) {
		// FIXME - can't seem to handle missing...
		//case COL_START:		return Integer.class;
		//case COL_END:			return Integer.class;
		//case COL_DIV:			return Integer.class;
		//case COL_DIST:		return Integer.class;
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
        case COL_LONG: return "Longtitude";
        case COL_LAT: return "Latitude";
		case COL_UTM_X: return "UTM X";
		case COL_UTM_Y: return "UTM Y";
		case COL_INPUT_TYPE: return "Data Store/Input Type";
		default: return "";
	}
}

/**
Returns an array containing the column widths (in number of characters).
@return an integer array containing the widths for each field.
*/
public String[] getColumnToolTips() {
    String[] tips = new String[__COLUMNS];
    tips[COL_ID] = "Well identifier from primary data provider";
    tips[COL_NAME] = "Well name";
    tips[COL_DATA_SOURCE] = "Organization/agency abbreviation";
    tips[COL_DATA_TYPE] = "Data type";
    tips[COL_TIME_STEP] = "Time step";
    tips[COL_UNITS] = "Data units";
    tips[COL_START] = "Starting date/time of available data";
    tips[COL_END] = "Ending date/time of available data";
    tips[COL_MEAS_COUNT] = "Count of available measurements";
    tips[COL_DIV] = "Water division";
    tips[COL_DIST] = "Water district";
    tips[COL_COUNTY] = "County name";
    tips[COL_STATE] = "State abbreviation";
    tips[COL_HUC] = "Hydrologic Unit Code";
    tips[COL_LONG] = "Longitude decimal degrees";
    tips[COL_LAT] = "Latitude decimal degrees";
    tips[COL_UTM_X] = "UTM X, meters";
    tips[COL_UTM_Y] = "UTM Y, meters";
    tips[COL_INPUT_TYPE] = "Input type or data store name";
    return tips;
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
    widths[COL_DATA_TYPE] = 15;
    widths[COL_TIME_STEP] = 8;
    widths[COL_UNITS] = 8;
    widths[COL_START] = 10;
    widths[COL_END] = 10;
    widths[COL_MEAS_COUNT] = 8;
    widths[COL_DIV] = 5;
    widths[COL_DIST] = 5;
    widths[COL_COUNTY] = 8;
    widths[COL_STATE] = 3;
    widths[COL_HUC] = 8;
    widths[COL_LONG] = 8;
    widths[COL_LAT] = 8;
    widths[COL_UTM_X] = 8;
    widths[COL_UTM_Y] = 8;
    widths[COL_INPUT_TYPE] = 15;
    return widths;
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
{	// If sorted, get the position in the data from the displayed row.
	if (_sortOrder != null) {
		row = _sortOrder[row];
	}

	int i; // Use for integer data.
	double d; // Use for double data

	HydroBase_GroundWaterWellsView wv = _data.get(row);

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
		case COL_NAME: return wv.getWell_name();
		case COL_DATA_SOURCE: return wv.getData_source();
		case COL_DATA_TYPE:
		    // TSTool translates to values from the TSTool interface...
			return "WellLevel";
		case COL_TIME_STEP:
		    // TSTool translates HydroBase values to nicer values...
			return wv.getTime_step();
		case COL_UNITS:
		    // The units are not in HydroBase.meas_type but are set by TSTool...
			return wv.getData_units();
		case COL_START:
		    //return new Integer(wv.getStart_year() );
			i = wv.getStart_year();
			if ( DMIUtil.isMissing(i) || HydroBase_Util.isMissing(i) ) {
				return "";
			}
			else {
			    return "" + i;
			}
		case COL_END:
		    //return new Integer (wv.getEnd_year() );
			i = wv.getEnd_year();
			if ( DMIUtil.isMissing(i) || HydroBase_Util.isMissing(i)) {
				return "";
			}
			else {
			    return "" + i;
			}
		case COL_MEAS_COUNT:
		    i = wv.getMeas_count();
			if ( DMIUtil.isMissing(i) || HydroBase_Util.isMissing(i)) {
				return "";
			}
			else {
			    return "" + i;
			}
		case COL_DIV:
		    //return new Integer ( wv.getDiv() );
			i = wv.getDiv();
			if ( DMIUtil.isMissing(i) || HydroBase_Util.isMissing(i)) {
				return "";
			}
			else {
			    return "" + i;
			}
		case COL_DIST:
		    //return new Integer ( wv.getWD() );
			i = wv.getWD();
			if ( DMIUtil.isMissing(i) || HydroBase_Util.isMissing(i)) {
				return "";
			}
			else {
			    return "" + i;
			}
		case COL_COUNTY: return wv.getCounty();
		case COL_STATE: return wv.getST();
		case COL_HUC: return wv.getHUC();
        case COL_LONG:
            d = wv.getLongdecdeg();
            if ( DMIUtil.isMissing(d) || HydroBase_Util.isMissing(d)) {
                return "";
            }
            else {
                return "" + StringUtil.formatString(d,"%.6f");
            }
        case COL_LAT:
            d = wv.getLatdecdeg();
            if ( DMIUtil.isMissing(d) || HydroBase_Util.isMissing(d)) {
                return "";
            }
            else {
                return "" + StringUtil.formatString(d,"%.6f");
            }
        case COL_UTM_X:
            d = wv.getUtm_x();
            if ( DMIUtil.isMissing(d) || HydroBase_Util.isMissing(d)) {
                return "";
            }
            else {
                return "" + StringUtil.formatString(d,"%.3f");
            }
        case COL_UTM_Y:
            d = wv.getUtm_y();
            if ( DMIUtil.isMissing(d) || HydroBase_Util.isMissing(d)) {
                return "";
            }
            else {
                return "" + StringUtil.formatString(d,"%.3f");
            }
		case COL_INPUT_TYPE: return __inputType;
		default: return "";
	}
}

/**
Set the input type (default is "HydroBase" but need to change when the table model is used for
multiple purposes.
*/
public void setInputType ( String inputType )
{
    __inputType = inputType;
}

/**
Set the width of WDIDs, which controls formatting of the ID column for structures.
@param wdid_length WDID length for formatting the ID.
*/
public void setWDIDLength ( int wdid_length )
{	__wdid_length = wdid_length;
}

}
