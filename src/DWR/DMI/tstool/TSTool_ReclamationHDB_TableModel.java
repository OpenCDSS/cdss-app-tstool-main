// TSTool_ReclamationHDB_TableModel - table model for time series header information for Reclamation HDB time series.

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

import rti.tscommandprocessor.commands.reclamationhdb.ReclamationHDBDataStore;
import rti.tscommandprocessor.commands.reclamationhdb.ReclamationHDB_SiteTimeSeriesMetadata;

import RTi.Util.GUI.JWorksheet_AbstractRowTableModel;

/**
This class is a table model for time series header information for Reclamation HDB
time series.  By default the sheet will contain row and column numbers.
*/
public class TSTool_ReclamationHDB_TableModel extends JWorksheet_AbstractRowTableModel
{

/**
Number of columns in the table model.
*/
private final int __COLUMNS = 41;

// The following order was updated according to Reclamation input on 2014-04-06
public final int COL_TYPE_REAL_MODEL = 0;
public final int COL_SITE_COMMON_NAME = 1;
public final int COL_SITE_NAME = 2;
public final int COL_SITE_ID = 3;
public final int COL_DATA_TYPE_COMMON_NAME = 4;
public final int COL_DATA_TYPE_ID = 5;
public final int COL_SITE_DATATYPE_ID = 6;
public final int COL_TIME_STEP = 7;
public final int COL_OBJECT_TYPE_NAME = 8;
public final int COL_SITE_DESCRIPTION = 9;
public final int COL_DATA_SOURCE = 10;
public final int COL_DATA_TYPE_NAME = 11;
// COL_MODEL* are only used with model time series
public final int COL_MODEL_NAME = 12;
public final int COL_MODEL_ID = 13;
public final int COL_MODEL_RUN_NAME = 14;
public final int COL_MODEL_HYDROLOGIC_INDICATOR = 15;
public final int COL_MODEL_RUN_DATE = 16;
public final int COL_MODEL_RUN_ID = 17;
// COL_ENSEMBLE* are only used with ensemble traces (time series in an ensemble)
// The following are in REF_ENSEMBLE
//public final int COL_ENSEMBLE_ID = ;
//public final int COL_ENSEMBLE_NAME = ;
public final int COL_ENSEMBLE_NAME = 18;
public final int COL_ENSEMBLE_AGENCY_NAME = 19;
public final int COL_ENSEMBLE_TRACE_DOMAIN = 20;
// The following are in REF_ENSEMBLE_TRACE
//public final int COL_ENSEMBLE_TRACE_ID = ;
//public final int COL_ENSEMBLE_TRACE_NUMERIC = ;
//public final int COL_ENSEMBLE_TRACE_NAME = ;
public final int COL_UNITS = 21;
public final int COL_START = 22; // This is actually the min start date/time
public final int COL_END = 23; // This is actually the max start date/time
public final int COL_OBJECT_TYPE_ID = 24;
public final int COL_OBJECT_TYPE_TAG = 25;
public final int COL_PHYSICAL_QUANTITY_NAME = 26;
public final int COL_SITE_DB_SITE_CODE = 27;
public final int COL_SITE_NWS_CODE = 28;
public final int COL_SITE_SCS_ID = 29;
public final int COL_SITE_SHEF_CODE = 30;
public final int COL_SITE_USGS_ID = 31;
public final int COL_SITE_STATE = 32;
public final int COL_SITE_BASIN = 33;
public final int COL_SITE_HUC = 34;
public final int COL_SITE_SEGMENT_NO = 35;
public final int COL_SITE_RIVER_MILE = 36;
public final int COL_SITE_LATITUDE = 37;
public final int COL_SITE_LONGITUDE = 38;
public final int COL_SITE_ELEVATION = 39;
public final int COL_DATASTORE_NAME	= 40;

/**
Data store name corresponding to data store used to retrieve the data.
*/
String __dataStoreName = null;

/**
Constructor.  This builds the model for displaying the given Reclamation HDB time series data.
@param dataStore the data store for the data
@param timeStep time-step for all time series
@param data the list of BNDSS_DataMetaData that will be displayed in the
table (null is allowed - see setData()).
@throws Exception if an invalid results passed in.
*/
public TSTool_ReclamationHDB_TableModel ( ReclamationHDBDataStore dataStore, List data )
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
	    case COL_TYPE_REAL_MODEL: return String.class;
	    case COL_SITE_COMMON_NAME: return String.class;
		case COL_SITE_NAME: return String.class;
        case COL_SITE_ID: return Integer.class;
        case COL_SITE_DATATYPE_ID: return Integer.class;
		case COL_SITE_DESCRIPTION: return String.class;
		case COL_DATA_SOURCE: return String.class;
		case COL_DATA_TYPE_COMMON_NAME: return String.class;
		case COL_DATA_TYPE_ID: return Integer.class;
		case COL_DATA_TYPE_NAME: return String.class;
		case COL_TIME_STEP: return String.class;
		case COL_MODEL_NAME: return String.class;
		case COL_MODEL_ID: return Integer.class;
	    case COL_MODEL_RUN_NAME: return String.class;
	    case COL_MODEL_HYDROLOGIC_INDICATOR: return String.class;
	    case COL_MODEL_RUN_DATE: return String.class;
	    case COL_MODEL_RUN_ID: return Integer.class;
	    case COL_ENSEMBLE_NAME: return String.class;
	    case COL_ENSEMBLE_AGENCY_NAME: return String.class;
	    case COL_ENSEMBLE_TRACE_DOMAIN: return String.class;
		case COL_UNITS: return String.class;
		case COL_START: return String.class;
		case COL_END: return String.class;
		case COL_OBJECT_TYPE_ID: return Integer.class;
		case COL_OBJECT_TYPE_NAME: return String.class;
		case COL_OBJECT_TYPE_TAG: return String.class;
		case COL_PHYSICAL_QUANTITY_NAME: return String.class;
		case COL_SITE_DB_SITE_CODE: return String.class;
		case COL_SITE_NWS_CODE: return String.class;
		case COL_SITE_SCS_ID: return String.class;
		case COL_SITE_SHEF_CODE: return String.class;
		case COL_SITE_USGS_ID: return String.class;
		case COL_SITE_STATE: return String.class;
		case COL_SITE_BASIN: return Integer.class;
		case COL_SITE_HUC: return Integer.class;
		case COL_SITE_SEGMENT_NO: return Integer.class;
		case COL_SITE_RIVER_MILE: return Float.class;
		case COL_SITE_LATITUDE: return Double.class;
		case COL_SITE_LONGITUDE: return Double.class;
		case COL_SITE_ELEVATION: return Float.class;
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
	    case COL_TYPE_REAL_MODEL: return "Real/Model";
	    case COL_SITE_COMMON_NAME: return "Site Common Name";
		case COL_SITE_NAME: return "Site Name";
	    case COL_SITE_ID: return "Site ID";
	    case COL_SITE_DATATYPE_ID: return "Site Datatype ID";
	    case COL_SITE_DESCRIPTION: return "Site Description";
		case COL_DATA_SOURCE: return "Data Source";
		case COL_DATA_TYPE_COMMON_NAME: return "Data Type Common Name";
		case COL_DATA_TYPE_ID: return "Data Type ID";
		case COL_DATA_TYPE_NAME: return "Data Type Name";
		case COL_TIME_STEP: return "Time Step";
	    case COL_MODEL_NAME: return "Model Name";
	    case COL_MODEL_ID: return "Model ID";
		case COL_MODEL_RUN_NAME: return "Model Run";
		case COL_MODEL_HYDROLOGIC_INDICATOR: return "Model Hydrologic Indicator";
		case COL_MODEL_RUN_DATE: return "Model Run Date";
		case COL_MODEL_RUN_ID: return "Model Run ID";
		case COL_ENSEMBLE_NAME: return "Ensemble Name";
		case COL_ENSEMBLE_AGENCY_NAME: return "Ensemble Agency";
        case COL_ENSEMBLE_TRACE_DOMAIN: return "Ensemble Trace Domain";
		case COL_UNITS: return "Units";
		case COL_START: return "Start";
		case COL_END: return "End";
        case COL_OBJECT_TYPE_ID: return "Object Type ID";
        case COL_OBJECT_TYPE_NAME: return "Object Type Name";
        case COL_OBJECT_TYPE_TAG: return "Object Type Tag";
        case COL_PHYSICAL_QUANTITY_NAME: return "Physical Quantity Name";
        case COL_SITE_DB_SITE_CODE: return "DB Site Code";
        case COL_SITE_NWS_CODE: return "NWS Code";
        case COL_SITE_SCS_ID: return "SCS ID";
        case COL_SITE_SHEF_CODE: return "SHEF Code";
        case COL_SITE_USGS_ID: return "USGS ID";
	    case COL_SITE_STATE: return "State";
	    case COL_SITE_BASIN: return "BASIN";
        case COL_SITE_HUC: return "HUC";
        case COL_SITE_SEGMENT_NO: return "Segment Number";
        case COL_SITE_RIVER_MILE: return "River Mile";
	    case COL_SITE_LATITUDE: return "Latitude";
	    case COL_SITE_LONGITUDE: return "Longitude";
	    case COL_SITE_ELEVATION: return "Elevation";
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
        case COL_SITE_ID: return "%d";
        case COL_DATA_TYPE_ID: return "%d";
        case COL_SITE_DATATYPE_ID: return "%d";
        case COL_MODEL_ID: return "%d";
        case COL_MODEL_RUN_ID: return "%d";
        case COL_SITE_RIVER_MILE: return "%.6f";
        case COL_SITE_LATITUDE: return "%.6f";
        case COL_SITE_LONGITUDE: return "%.6f";
        case COL_SITE_ELEVATION: return "%.2f";
		default: return "%s"; // All else are strings.
	}
}

/**
From AbstractTableMode.  Returns the number of rows of data in the table.
*/
public int getRowCount() {
	return _rows;
}

/**
Returns an array containing the column widths (in number of characters).
@return an integer array containing the widths for each field.
*/
public String[] getColumnToolTips() {
    String[] toolTips = new String[__COLUMNS];
    toolTips[COL_TYPE_REAL_MODEL] = "<html>Indicates whether the time series is Real (observed) or Model (simulated)<br>" +
        "<html>This controls whether the HDB R_* or M_* time series tables are read.<br>" +
        "<html>Time series property can be accessed with ${ts:REAL_MODEL_TYPE}</html>";
    toolTips[COL_SITE_COMMON_NAME] = "<html>Site common name corresponding to database HDB_SITE.SITE_COMMON_NAME.<br>" +
        "<html>Time series property can be accessed with ${ts:SITE_COMMON_NAME}</html>";
    toolTips[COL_SITE_NAME] = "<html>Site name corresponding to database HDB_SITE.SITE_NAME.<br>" +
        "<html>Time series property can be accessed with ${ts:SITE_NAME}</html>";
    toolTips[COL_SITE_ID] = "<html>Unique site ID corresponding to database HDB_SITE.SITE_ID.<br>" +
        "<html>Time series property can be accessed with ${ts:SITE_ID}</html>";
    toolTips[COL_SITE_DATATYPE_ID] = "<html>Site/data type unique ID corresponding to database HDB_SITE_DATATYPE.SITE_DATATYPE_ID.<br>" +
        "<html>Time series property can be accessed with ${ts:SITE_DATATYPE_ID}</html>";
    toolTips[COL_SITE_DESCRIPTION] = "<html>Site descreption corresponding to database HDB_SITE.DESCRIPTION.<br>" +
        "<html>Time series property can be accessed with ${ts:DESCRIPTION}</html>";
    toolTips[COL_DATA_SOURCE] = "<html>Data source used in time series identifiers.<br>" +
        "<html>Time series data source is always HDB</html>";
    toolTips[COL_DATA_TYPE_COMMON_NAME] = "<html>Data type common name corresponding to database HDB_DATATYPE.DATATYPE_COMMON_NAME.<br>" +
        "<html>Time series property can be accessed with ${ts:DATA_TYPE_COMMON_NAME}</html>";
    toolTips[COL_DATA_TYPE_ID] = "<html>Unique data type ID corresponding to database HDB_DATATYPE.DATATYPE_ID.<br>" +
        "<html>Time series property can be accessed with ${ts:DATA_TYPE_ID}</html>";
    toolTips[COL_DATA_TYPE_NAME] = "<html>Data type name corresponding to database HDB_DATATYPE.DATATYPE_NAME.<br>" +
        "<html>Time series property can be accessed with ${ts:DATA_TYPE_NAME}</html>";
    toolTips[COL_TIME_STEP] = "<html>TSTool time step (interval) used to match HDB time series table.<br>" +
        "<html>Time series property can be indicated with %L</html>";
    toolTips[COL_MODEL_NAME] = "<html>Model name used with model time series, corresponding to database HDB_MODEL.MODEL_NAME.<br>" +
        "<html>Time series property can be accessed with ${ts:MODEL_NAME}</html>";
    toolTips[COL_MODEL_ID] = "<html>Unique model ID corresponding to database HDB_MODEL.MODEL_ID.<br>" +
        "<html>Time series property can be accessed with ${ts:MODEL_ID}</html>";
    toolTips[COL_MODEL_RUN_NAME] = "<html>Model run name corresponding to database REF_MODEL_RUN.MODEL_RUN_NAME.<br>" +
        "<html>Time series property can be accessed with ${ts:MODEL_RUN_NAME}</html>";
    toolTips[COL_MODEL_HYDROLOGIC_INDICATOR] = "<html>Hydrologic indicator corresponding to database REF_MODEL_RUN.HYDROLOGIC_INDICATOR.<br>" +
        "<html>Time series property can be accessed with ${ts:HYDROLOGIC_INDICATOR}</html>";
    toolTips[COL_MODEL_RUN_DATE] = "<html>Model run date corresponding to database REF_MODEL_RUN..<br>" +
        "<html>Time series property can be accessed with ${ts:MODEL_RUN_DATE}</html>";
    toolTips[COL_MODEL_RUN_ID] = "<html>Unique model run ID corresponding to database REF_MODEL_RUN.MODEL_RUN_ID.<br>" +
        "<html>Time series property can be accessed with ${ts:MODEL_RUN_ID}</html>";
    toolTips[COL_ENSEMBLE_NAME] = "<html>Ensemble name corresponding to database REF_ENSEMBLE.ESEMBLE_NAME.<br>" +
        "<html>Time series property can be accessed with ${ts:ENSEMBLE_NAME}</html>";
    toolTips[COL_ENSEMBLE_AGENCY_NAME] = "<html>Ensemble agency name corresponding to database REF_ENSEMBLE.AGEN_NAME->HDB_AGEN.AGEN_NAME.<br>" +
        "<html>Time series property can be accessed with ${ts:ENSEMBLE_AGEN_NAME}</html>";
    toolTips[COL_ENSEMBLE_TRACE_DOMAIN] = "<html>Ensemble trace domain corresponding to database REF_ENSEMBLE.TRACE_DOMAIN.<br>" +
        "<html>Time series property can be accessed with ${ts:ENSEMBLE_TRACE_DOMAIN}</html>";
    toolTips[COL_UNITS] = "<html>Time series data units corresponding to database HDB_DATATYPE.UNIT_ID->HDB_UNIT.UNIT_COMMON_NAME.<br>" +
        "<html>Time series property can be accessed with ${ts:UNIT_COMMON_NAME}</html>";
    toolTips[COL_START] = "<html>Time series start date/time corresponding to MIN(time series date/time record).</html>";
    toolTips[COL_END] = "<html>Time series end date/time corresponding to MAX(time series date/time record).</html>";
    toolTips[COL_OBJECT_TYPE_ID] = "<html>Object type corresponding to dataase HDB_SITE.OBJECTYPEID.<br>" +
        "<html>Time series property can be accessed with ${ts:OBJECT_TYPE_ID}</html>";
    toolTips[COL_OBJECT_TYPE_NAME] = "<html>Object type name corresponding to database HDB_OBJECT_TYPE.OBJECTTYPE_NAME.<br>" +
        "<html>Time series property can be accessed with ${ts:OBJECT_TYPE_NAME}</html>";
    toolTips[COL_OBJECT_TYPE_TAG] = "<html>Object type tag corresponding to database HDB_OBJECT_TYPE.OBJECTTYPE_TAG.<br>" +
        "<html>Time series property can be accessed with ${ts:OBJECT_TYPE_TAG}</html>";
    toolTips[COL_PHYSICAL_QUANTITY_NAME] = "<html>Physical quantity name corresponding to database HDB_DATATYPE.PHYSICAL_QUANTITY_NAME.<br>" +
        "<html>Time series property can be accessed with ${ts:PHYSICAL_QUANTITY_NAME}</html>";
    toolTips[COL_SITE_DB_SITE_CODE] = "<html>Database site code corresponding to database HDB_SITE.DB_SITE_CODE.<br>" +
        "<html>Time series property can be accessed with ${ts:DB_SITE_CODE}</html>";
    toolTips[COL_SITE_NWS_CODE] = "<html>NWS code corresponding to database HDB_SITE.NWS_CODE.<br>" +
        "<html>Time series property can be accessed with ${ts:NWS_CODE}</html>";
    toolTips[COL_SITE_SCS_ID] = "<html>SCS site ID corresponding to database HDB_SITE.SCS_ID.<br>" +
        "<html>Time series property can be accessed with ${ts:SCS_ID}</html>";
    toolTips[COL_SITE_SHEF_CODE] = "<html>SHEF code corresponding to database HDB_SITE.SHEF_CODE.<br>" +
        "<html>Time series property can be accessed with ${ts:SHEF_CODE}</html>";
    toolTips[COL_SITE_USGS_ID] = "<html>USGS ID corresponding to database HDB_SITE.USGS_ID.<br>" +
        "<html>Time series property can be accessed with ${ts:USGS_ID}</html>";
    toolTips[COL_SITE_STATE] = "<html>Site state corresponding to database HDB_SITE.STATE_ID->HDB_STATE.STATE_CODE.<br>" +
        "<html>Time series property can be accessed with ${ts:STATE_CODE}</html>";
    toolTips[COL_SITE_BASIN] = "<html>Site basin corresponding to database HDB_SITE.BASIN_ID-> Always blank.<br>" +
        "<html>Time series property can be accessed with ${ts:BASIN_ID}</html>";
    toolTips[COL_SITE_HUC] = "<html>Site HUC corresponding to database HDB_SITE.HYDROLOGIC_UNIT.<br>" +
        "<html>Time series property can be accessed with ${ts:HUC}</html>";
    toolTips[COL_SITE_SEGMENT_NO] = "<html>Site segment number corresponding to database HDB_SITE.SEGMENT_NO.<br>" +
        "<html>Time series property can be accessed with ${ts:SEGMENT_NO}</html>";
    toolTips[COL_SITE_RIVER_MILE] = "<html>Site river mile corresponding to database HDB_SITE.RIVER_MILE.<br>" +
        "<html>Time series property can be accessed with ${ts:RIVER_MILE}</html>";
    toolTips[COL_SITE_LATITUDE] = "<html>Site latitude corresponding to database HDB_SITE.LAT.<br>" +
        "<html>Time series property can be accessed with ${ts:LATITUDE}</html>";
    toolTips[COL_SITE_LONGITUDE] = "<html>Site longitude corresponding to database HDB_SITE.LONGI.<br>" +
        "<html>Time series property can be accessed with ${ts:LONGITUDE}</html>";
    toolTips[COL_SITE_ELEVATION] = "<html>Site elevation corresponding to database HDB_SITE.ELEVATION.<br>" +
        "<html>Time series property can be accessed with ${ts:ELEVATION}</html>";
    toolTips[COL_DATASTORE_NAME] = "<html>Datastore name.</html>";
    return toolTips;
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

	ReclamationHDB_SiteTimeSeriesMetadata data = (ReclamationHDB_SiteTimeSeriesMetadata)_data.get(row);
	switch (col) {
        case COL_TYPE_REAL_MODEL:
            return data.getRealModelType();
        case COL_SITE_COMMON_NAME:
            return data.getSiteCommonName();
		case COL_SITE_NAME:
		    return data.getSiteName();
        case COL_SITE_ID:
            return data.getSiteID();
        case COL_SITE_DATATYPE_ID:
            return data.getSiteDataTypeID();
        case COL_SITE_DESCRIPTION:
            return data.getDescription();
		case COL_DATA_SOURCE:
			return "HDB"; // TODO SAM 2010-10-28 Need to evaluate how to handle different data sources - they are in value records
		case COL_DATA_TYPE_COMMON_NAME:
			return data.getDataTypeCommonName();
	    case COL_DATA_TYPE_ID:
            return data.getDataTypeID();
		case COL_DATA_TYPE_NAME:
            return data.getDataTypeName();
		case COL_TIME_STEP:
			return data.getDataInterval();
		case COL_MODEL_NAME:
		    return data.getModelName();
		case COL_MODEL_ID:
            return data.getModelID();
        case COL_MODEL_RUN_NAME:
            return data.getModelRunName();
        case COL_MODEL_HYDROLOGIC_INDICATOR:
            return data.getHydrologicIndicator();
        case COL_MODEL_RUN_DATE:
            return data.getModelRunDate();
        case COL_MODEL_RUN_ID:
            return data.getModelRunID();
        case COL_ENSEMBLE_NAME:
            return data.getEnsembleName();
        case COL_ENSEMBLE_AGENCY_NAME:
            return data.getEnsembleAgenName();
        case COL_ENSEMBLE_TRACE_DOMAIN:
            return data.getEnsembleTraceDomain();
		case COL_UNITS:
		    return data.getUnitCommonName();
		case COL_START:
		    return "" + data.getStartDateTimeMin();
		case COL_END:
		    return "" + data.getStartDateTimeMax();
        case COL_OBJECT_TYPE_ID:
            return data.getObjectTypeID();
        case COL_OBJECT_TYPE_NAME:
            return data.getObjectTypeName();
        case COL_OBJECT_TYPE_TAG:
            return data.getObjectTypeTag();
        case COL_PHYSICAL_QUANTITY_NAME:
            return data.getPhysicalQuantityName();
        case COL_SITE_DB_SITE_CODE:
            return data.getDbSiteCode();
        case COL_SITE_NWS_CODE:
            return data.getNwsCode();
        case COL_SITE_SCS_ID:
            return data.getScsID();
        case COL_SITE_SHEF_CODE:
            return data.getShefCode();
        case COL_SITE_USGS_ID:
            return data.getUsgsID();
        case COL_SITE_STATE:
            return data.getStateCode();
        case COL_SITE_BASIN:
            return data.getBasinID();
        case COL_SITE_HUC:
            return data.getHuc();
        case COL_SITE_SEGMENT_NO:
            return data.getSegmentNo();
        case COL_SITE_RIVER_MILE:
            return data.getRiverMile();
        case COL_SITE_LATITUDE:
            return data.getLatitude();
        case COL_SITE_LONGITUDE:
            return data.getLongitude();
        case COL_SITE_ELEVATION:
            return data.getElevation();
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
	widths[COL_TYPE_REAL_MODEL] = 7;
    widths[COL_SITE_COMMON_NAME] = 20;
	widths[COL_SITE_NAME] = 20;
    widths[COL_SITE_ID] = 4;
    widths[COL_SITE_DATATYPE_ID] = 10;
    widths[COL_SITE_DESCRIPTION] = 20;
	widths[COL_DATA_SOURCE] = 11;
	widths[COL_DATA_TYPE_COMMON_NAME] = 17;
	widths[COL_DATA_TYPE_ID] = 8;
	widths[COL_DATA_TYPE_NAME] = 14;
	widths[COL_TIME_STEP] = 8;
	widths[COL_MODEL_NAME] = 12;
	widths[COL_MODEL_ID] = 12;
	widths[COL_MODEL_RUN_NAME] = 12;
	widths[COL_MODEL_HYDROLOGIC_INDICATOR] = 18;
	widths[COL_MODEL_RUN_DATE] = 12;
	widths[COL_MODEL_RUN_ID] = 10;
	widths[COL_ENSEMBLE_NAME] = 12;
	widths[COL_ENSEMBLE_AGENCY_NAME] = 12;
    widths[COL_ENSEMBLE_TRACE_DOMAIN] = 16;
	widths[COL_UNITS] = 8;
	widths[COL_START] = 10;
	widths[COL_END] = 10;
    widths[COL_OBJECT_TYPE_ID] = 10;
    widths[COL_OBJECT_TYPE_NAME] = 12;
    widths[COL_OBJECT_TYPE_TAG] = 10;
    widths[COL_PHYSICAL_QUANTITY_NAME] = 16;
    widths[COL_SITE_DB_SITE_CODE] = 8;
    widths[COL_SITE_NWS_CODE] = 8;
    widths[COL_SITE_SCS_ID] = 6;
    widths[COL_SITE_SHEF_CODE] = 8;
    widths[COL_SITE_USGS_ID] = 8;
    widths[COL_SITE_STATE] = 4;
    widths[COL_SITE_BASIN] = 4;
    widths[COL_SITE_HUC] = 8;
    widths[COL_SITE_SEGMENT_NO] = 12;
    widths[COL_SITE_RIVER_MILE] = 8;
    widths[COL_SITE_LATITUDE] = 9;
    widths[COL_SITE_LONGITUDE] = 9;
    widths[COL_SITE_ELEVATION] = 6;
	widths[COL_DATASTORE_NAME] = 12;
	return widths;
}

}
