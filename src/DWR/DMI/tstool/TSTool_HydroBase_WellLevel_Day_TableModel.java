// TSTool_HydroBase_WellLevel_Day_TableModel - table model for time series header information for HydroBase
// daily well level time series with information stored in HydroBase_GroundWaterWellsView objects.

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
This class is a table model for time series header information for HydroBase
daily well level time series with information stored in HydroBase_GroundWaterWellsView objects.
By default the sheet will contain row and column numbers.
*/
@SuppressWarnings("serial")
public class TSTool_HydroBase_WellLevel_Day_TableModel
extends JWorksheet_AbstractRowTableModel<HydroBase_GroundWaterWellsView>
{

/**
Number of columns in the table model, including the row number.
*/
private final int __COLUMNS = 63;

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
public final int COL_MD = 15;
public final int COL_DSS_AQUIFER1 =	16;
public final int COL_DSS_AQUIFER2 = 17;
public final int COL_DSS_AQUIFER_COMMENT = 18;
public final int COL_AQUIFER1 = 19;
public final int COL_AQUIFER2 = 20;
public final int COL_AQUIFER_COMMENT = 21;
public final int COL_LONG = 22;
public final int COL_LAT = 23;
public final int COL_UTM_X = 24;
public final int COL_UTM_Y = 25;
public final int COL_SPOTTER_VERSION = 26;
public final int COL_LOC_ACCURACY = 27;
public final int COL_PM = 28;
public final int COL_TS = 29;
public final int COL_TDIR = 30;
public final int COL_TSA = 31;
public final int COL_RNG = 32;
public final int COL_RDIR = 33;
public final int COL_RNGA = 34;
public final int COL_SEC = 35;
public final int COL_SECA = 36;
public final int COL_Q160 = 37;
public final int COL_Q40 = 38;
public final int COL_Q10 = 39;
public final int COL_COORDSNS = 40;
public final int COL_COORDSNS_DIR = 41;
public final int COL_COORDSEW = 42;
public final int COL_COORDSWE_DIR = 43;
public final int COL_ELEV = 44;
public final int COL_ELEV_ACCURACY = 45;
public final int COL_WELL_DEPTH = 46;
public final int COL_TPERF = 47;
public final int COL_BPERF = 48;
public final int COL_YIELD = 49;
public final int COL_BEDROCK_ELEV = 50;
public final int COL_SAT_1965 = 51;
public final int COL_REMARKS1 = 52;
public final int COL_REMARKS2 = 53;
// HydroBase data that might be useful but which may be all missing
public final int COL_DATA_SOURCE_IDENTIFIER = 54;
public final int COL_IDENTIFIER = 55;
public final int COL_RECEIPT = 56;
public final int COL_PERMITNO = 57;
public final int COL_PERMITSUF = 58;
public final int COL_PERMITRPL = 59;
public final int COL_LOCNUM = 60;
public final int COL_SITE_ID = 61;
public final int COL_INPUT_TYPE = 62;

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
public TSTool_HydroBase_WellLevel_Day_TableModel (JWorksheet worksheet, int wdid_length,
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
	__inputType = inputType;
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
		case COL_MD: return "MD";
		case COL_DSS_AQUIFER1: return "DSS Aquifer 1";
		case COL_DSS_AQUIFER2: return "DSS Aquifer 2";
		case COL_DSS_AQUIFER_COMMENT: return "DSS Aquifer Comment";
		case COL_AQUIFER1: return "Aquifer 1";
		case COL_AQUIFER2: return "Aquifer 2";
		case COL_AQUIFER_COMMENT: return "Aquifer Comment";
        case COL_LONG: return "Longtitude";
        case COL_LAT: return "Latitude";
        case COL_UTM_X: return "UTM X";
        case COL_UTM_Y: return "UTM Y";
        case COL_SPOTTER_VERSION: return "Spotter Version";
        case COL_LOC_ACCURACY: return "Loc Acc";
        case COL_PM: return "PM";
        case COL_TS: return "TS";
        case COL_TDIR: return "TSDir";
        case COL_TSA: return "TSA";
        case COL_RNG: return "Rng";
        case COL_RDIR: return "RngDir";
        case COL_RNGA: return "RngA";
        case COL_SEC: return "Sec";
        case COL_SECA: return "SecA";
        case COL_Q160: return "Q160";
        case COL_Q40: return "Q40";
        case COL_Q10: return "Q10";
        case COL_COORDSNS: return "CoordsNS";
        case COL_COORDSNS_DIR: return "CoordsNS Dir";
        case COL_COORDSEW: return "CoordsEW";
        case COL_COORDSWE_DIR: return "CoordsEW Dir";
        case COL_ELEV: return "Elevation";
        case COL_ELEV_ACCURACY: return "Elev Acc";
        case COL_WELL_DEPTH: return "Depth";
        case COL_TPERF: return "Tperf";
        case COL_BPERF: return "Bperf";
        case COL_YIELD: return "Yield";
        case COL_BEDROCK_ELEV: return "Bedrock Elev";
        case COL_SAT_1965: return "Sat 1965";
        case COL_REMARKS1: return "Remarks 1";
        case COL_REMARKS2: return "Remarks 2";
        case COL_DATA_SOURCE_IDENTIFIER: return "Data Source Identifier";
        case COL_IDENTIFIER: return "Identifier";
        case COL_RECEIPT: return "Receipt";
        case COL_PERMITNO: return "Permit No";
        case COL_PERMITSUF: return "Permit Suffix";
        case COL_PERMITRPL: return "Permit Repl";
        case COL_LOCNUM: return "USBR Locnum";
        case COL_SITE_ID: return "USGS Site ID";
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
    toolTips[COL_ID] = "Unique well identifier, typically the data source's primary identifier " +
    	"(data source impacts identifier format)";
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
    toolTips[COL_MD] = "Management district code";
    toolTips[COL_DSS_AQUIFER1] = "Decision support system (DSS) aquifer 1";
    toolTips[COL_DSS_AQUIFER2] = "Decision support system (DSS) aquifer 2";
    toolTips[COL_DSS_AQUIFER_COMMENT] = "Decision support system (DSS) aquifer comment";
    toolTips[COL_AQUIFER1] = "Aquifer 1";
    toolTips[COL_AQUIFER2] = "Aquifer 2";
    toolTips[COL_AQUIFER_COMMENT] = "Aquifer comment";
    toolTips[COL_LONG] = "Longitude decimal degrees";
    toolTips[COL_LAT] = "Latitude decimal degrees";
    toolTips[COL_UTM_X] = "UTM X, meters";
    toolTips[COL_UTM_Y] = "UTM Y, meters";
    toolTips[COL_SPOTTER_VERSION] = "Spotter version for coordinates";
    toolTips[COL_LOC_ACCURACY] = "Accuracy of UTM coordinates";
    toolTips[COL_PM] = "Principal meridian";
    toolTips[COL_TS] = "Township number";
    toolTips[COL_TDIR] = "Township direction";
    toolTips[COL_TSA] = "Half township indicator";
    toolTips[COL_RNG] = "Range number";
    toolTips[COL_RDIR] = "Range direction";
    toolTips[COL_RNGA] = "Half range indicator";
    toolTips[COL_SEC] = "Section number";
    toolTips[COL_SECA] = "Upper section indicator";
    toolTips[COL_Q160] = "160 acre quarter section";
    toolTips[COL_Q40] = "40 acre quarter section";
    toolTips[COL_Q10] = "10 acre quarter section";
    toolTips[COL_COORDSNS] = "Distance from north/south section line (feet)";
    toolTips[COL_COORDSNS_DIR] = "Direction of measurement from north/south section line";
    toolTips[COL_COORDSEW] = "Distance from east/west section line (feet)";
    toolTips[COL_COORDSWE_DIR] = "Direction of measurement from east/west section line";
    toolTips[COL_ELEV] = "Surface elevation (feet)";
    toolTips[COL_ELEV_ACCURACY] = "Accuracy of surface elevation";
    toolTips[COL_WELL_DEPTH] = "Completed depth of well (feet)";
    toolTips[COL_TPERF] = "Depth to first perforated casing (feet)";
    toolTips[COL_BPERF] = "Depth to base of last perforated casing (feet)";
    toolTips[COL_YIELD] = "Actual pumping rate (gpm)";
    toolTips[COL_BEDROCK_ELEV] = "Elevation of bedrock (feet)";
    toolTips[COL_SAT_1965] = "Well's physical characteristics included in Senate Bill 5 modeling effort";
    toolTips[COL_REMARKS1] = "Generic remarks";
    toolTips[COL_REMARKS2] = "Generic remarks";
    toolTips[COL_DATA_SOURCE_IDENTIFIER] = "Unique identifier used by data source to identify well";
    toolTips[COL_IDENTIFIER] = "Other identifier for the measurement type, typically the primary data source's unique identifier";
    toolTips[COL_RECEIPT] = "Unique well application (permit) identifier, generated by cash register";
    toolTips[COL_PERMITNO] = "Well permit number";
    toolTips[COL_PERMITSUF] = "Well suffix code";
    toolTips[COL_PERMITRPL] = "Well replacement code ('A' for exempt, 'R' for non-exempt)";
    toolTips[COL_LOCNUM] = "USBR location identifier string based on the well's PLSS location";
    toolTips[COL_SITE_ID] = "USGS site identifier";
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

	HydroBase_GroundWaterWellsView wv = _data.get(row);

	switch (col) {
		// case 0 handled above.
		case COL_ID:		
			if ( wv.getIdentifier().length() > 0 ) {
				// Well with a different identifier to display.
				return wv.getIdentifier();
			}
			else if ( (wv.getWD() > 0) && (wv.getID() > 0) ) {
			    return HydroBase_WaterDistrict.formWDID (__wdid_length, wv.getWD(), wv.getID() );
			}
			else {
			    // A structure other than wells...
				return wv.formatLatLongID();
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
			if ( DMIUtil.isMissing(i) || HydroBase_Util.isMissing(i)) {
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
		case COL_COUNTY:
		    return wv.getCounty();
		case COL_STATE:
		    return wv.getST();
		case COL_HUC:
		    return wv.getHUC();
		case COL_BASIN:
		    return wv.getBasin();
		case COL_MD:
            return wv.getMD();
		case COL_DSS_AQUIFER1:
		    return wv.getDSS_aquifer1();
		case COL_DSS_AQUIFER2:
		    return wv.getDSS_aquifer2();
		case COL_DSS_AQUIFER_COMMENT:
            return wv.getDSS_aquifer_comment();
        case COL_AQUIFER1:
            return wv.getAquifer1();
        case COL_AQUIFER2:
            return wv.getAquifer2();
        case COL_AQUIFER_COMMENT:
            return wv.getAquifer_comment();
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
        case COL_SPOTTER_VERSION:
            return wv.getSpotter_version();
        case COL_LOC_ACCURACY:
            i = wv.getLoc_accuracy();
            if ( DMIUtil.isMissing(i) || HydroBase_Util.isMissing(i)) {
                return "";
            }
            else {
                return "" + i;
            }
        case COL_PM:
            return wv.getPM();
        case COL_TS:
            i = wv.getTS();
            if ( DMIUtil.isMissing(i) || HydroBase_Util.isMissing(i)) {
                return "";
            }
            else {
                return "" + i;
            }
        case COL_TDIR:
            return wv.getTdir();
        case COL_TSA:
            return wv.getTsa();
        case COL_RNG:
            i = wv.getRng();
            if ( DMIUtil.isMissing(i) || HydroBase_Util.isMissing(i)) {
                return "";
            }
            else {
                return "" + i;
            }
        case COL_RDIR:
            return wv.getRdir();
        case COL_RNGA:
            return wv.getRnga();
        case COL_SEC:
            i = wv.getSec();
            if ( DMIUtil.isMissing(i) || HydroBase_Util.isMissing(i)) {
                return "";
            }
            else {
                return "" + i;
            }
        case COL_SECA:
            return wv.getSeca();
        case COL_Q160:
            return wv.getQ160();
        case COL_Q40:
            return wv.getQ40();
        case COL_Q10:
            return wv.getQ10();
        case COL_COORDSNS:
            i = wv.getCoordsns();
            if ( DMIUtil.isMissing(i) || HydroBase_Util.isMissing(i)) {
                return "";
            }
            else {
                return "" + i;
            }
        case COL_COORDSNS_DIR:
            return wv.getCoordsns_dir();
        case COL_COORDSEW:
            i = wv.getCoordsew();
            if ( DMIUtil.isMissing(i) || HydroBase_Util.isMissing(i)) {
                return "";
            }
            else {
                return "" + i;
            }
        case COL_COORDSWE_DIR:
            return wv.getCoordsew_dir();
        case COL_ELEV:
            d = wv.getElev();
            if ( DMIUtil.isMissing(d) || HydroBase_Util.isMissing(d)) {
                return "";
            }
            else {
                return "" + StringUtil.formatString(d,"%.0f");
            }
        case COL_ELEV_ACCURACY:
            d = wv.getElev_accuracy();
            if ( DMIUtil.isMissing(d) || HydroBase_Util.isMissing(d)) {
                return "";
            }
            else {
                return "" + StringUtil.formatString(d,"%.0f");
            }
        case COL_WELL_DEPTH:
            i = wv.getWell_depth();
            if ( DMIUtil.isMissing(i) || HydroBase_Util.isMissing(i)) {
                return "";
            }
            else {
                return "" + i;
            }
        case COL_TPERF:
            i = wv.getTperf();
            if ( DMIUtil.isMissing(i) || HydroBase_Util.isMissing(i)) {
                return "";
            }
            else {
                return "" + i;
            }
        case COL_BPERF:
            i = wv.getBperf();
            if ( DMIUtil.isMissing(i) || HydroBase_Util.isMissing(i)) {
                return "";
            }
            else {
                return "" + i;
            }
        case COL_YIELD:
            d = wv.getYield();
            if ( DMIUtil.isMissing(d) || HydroBase_Util.isMissing(d)) {
                return "";
            }
            else {
                return "" + StringUtil.formatString(d,"%.2f");
            }
        case COL_BEDROCK_ELEV:
            d = wv.getBedrock_elev();
            if ( DMIUtil.isMissing(d) || HydroBase_Util.isMissing(d)) {
                return "";
            }
            else {
                return "" + StringUtil.formatString(d,"%.0f");
            }
        case COL_SAT_1965:
            d = wv.getSat_1965();
            if ( DMIUtil.isMissing(d) || HydroBase_Util.isMissing(d)) {
                return "";
            }
            else {
                return "" + StringUtil.formatString(d,"%.0f");
            }
        case COL_REMARKS1:
            return wv.getRemarks1();
        case COL_REMARKS2:
            return wv.getRemarks2();
        case COL_DATA_SOURCE_IDENTIFIER:
            return wv.getData_source_id();
        case COL_IDENTIFIER:
            return wv.getIdentifier();
        case COL_RECEIPT:
            return wv.getReceipt();
        case COL_PERMITNO:
            i = wv.getPermitno();
            if ( DMIUtil.isMissing(i) || HydroBase_Util.isMissing(i)) {
                return "";
            }
            else {
                return "" + i;
            }
        case COL_PERMITSUF:
            return wv.getPermitsuf();
        case COL_PERMITRPL:
            return wv.getPermitrpl();
        case COL_LOCNUM:
            return wv.getLoc_num();
        case COL_SITE_ID:
            return wv.getSite_id();
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
	widths[COL_MD] = 2;
	widths[COL_DSS_AQUIFER1] = 9; // DSS Aquifer 1
	widths[COL_DSS_AQUIFER2] = 9; // DSS Aquifer 2
	widths[COL_DSS_AQUIFER_COMMENT] = 15;
	widths[COL_AQUIFER1] = 6;
	widths[COL_AQUIFER2] = 6;
	widths[COL_AQUIFER_COMMENT] = 11;
    widths[COL_LONG] = 8;
    widths[COL_LAT] = 8;
    widths[COL_UTM_X] = 8;
    widths[COL_UTM_Y] = 8;
    widths[COL_SPOTTER_VERSION] = 10;
    widths[COL_LOC_ACCURACY] = 5;
    widths[COL_PM] = 2;
    widths[COL_TS] = 2;
    widths[COL_TDIR] = 4;
    widths[COL_TSA] = 3;
    widths[COL_RNG] = 3;
    widths[COL_RDIR] = 4;
    widths[COL_RNGA] = 4;
    widths[COL_SEC] = 2;
    widths[COL_SECA] = 4;
    widths[COL_Q160] = 3;
    widths[COL_Q40] = 3;
    widths[COL_Q10] = 3;
    widths[COL_COORDSNS] = 6;
    widths[COL_COORDSNS_DIR] = 9;
    widths[COL_COORDSEW] = 7;
    widths[COL_COORDSWE_DIR] = 9;
    widths[COL_ELEV] = 5;
    widths[COL_ELEV_ACCURACY] = 5;
    widths[COL_WELL_DEPTH] = 5;
    widths[COL_TPERF] = 5;
    widths[COL_BPERF] = 5;
    widths[COL_YIELD] = 5;
    widths[COL_BEDROCK_ELEV] = 8;
    widths[COL_SAT_1965] = 5;
    widths[COL_REMARKS1] = 10;
    widths[COL_REMARKS2] = 10;
    widths[COL_DATA_SOURCE_IDENTIFIER] = 15;
    widths[COL_IDENTIFIER] = 10;
    widths[COL_RECEIPT] = 8;
    widths[COL_PERMITNO] = 8;
    widths[COL_PERMITSUF] = 8;
    widths[COL_PERMITRPL] = 7;
    widths[COL_LOCNUM] = 12;
    widths[COL_SITE_ID] = 12;
	widths[COL_INPUT_TYPE] = 12;
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