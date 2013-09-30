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
private final int __COLUMNS = 35;

public final int COL_TYPE = 0;
public final int COL_SITE_COMMON_NAME = 1;
public final int COL_SITE_NAME = 2;
public final int COL_SITE_ID = 3;
public final int COL_SITE_DATATYPE_ID = 4;
public final int COL_SITE_DESCRIPTION = 5;
public final int COL_DATA_SOURCE = 6;
public final int COL_DATA_TYPE = 7;
public final int COL_TIME_STEP = 8;
public final int COL_MODEL_NAME = 9;
public final int COL_MODEL_RUN_NAME = 10;
public final int COL_MODEL_HYDROLOGIC_INDICATOR = 11;
public final int COL_MODEL_RUN_DATE = 12;
public final int COL_MODEL_RUN_ID = 13;
public final int COL_UNITS = 14;
public final int COL_START = 15; // This is actually the min start date/time
public final int COL_END = 16; // This is actually the max start date/time
public final int COL_OBJECT_TYPE_ID = 17;
public final int COL_OBJECT_TYPE_NAME = 18;
public final int COL_OBJECT_TYPE_TAG = 19;
public final int COL_PHYSICAL_QUANTITY_NAME = 20;
public final int COL_SITE_DB_SITE_CODE = 21;
public final int COL_SITE_NWS_CODE = 22;
public final int COL_SITE_SCS_ID = 23;
public final int COL_SITE_SHEF_CODE = 24;
public final int COL_SITE_USGS_ID = 25;
public final int COL_SITE_STATE = 26;
public final int COL_SITE_BASIN = 27;
public final int COL_SITE_HUC = 28;
public final int COL_SITE_SEGMENT_NO = 29;
public final int COL_SITE_RIVER_MILE = 30;
public final int COL_SITE_LATITUDE = 31;
public final int COL_SITE_LONGITUDE = 32;
public final int COL_SITE_ELEVATION = 33;
public final int COL_DATASTORE_NAME	= 34;

/**
Data store name corresponding to data store used to retrieve the data.
*/
String __dataStoreName = null;

/**
Time-step for all time series.
*/
String __timeStep = null;

/**
Constructor.  This builds the model for displaying the given Reclamation HDB time series data.
@param dataStore the data store for the data
@param timeStep time-step for all time series
@param data the list of BNDSS_DataMetaData that will be displayed in the
table (null is allowed - see setData()).
@throws Exception if an invalid results passed in.
*/
public TSTool_ReclamationHDB_TableModel ( ReclamationHDBDataStore dataStore, String timeStep, List data )
throws Exception
{	if ( data == null ) {
		_rows = 0;
	}
	else {
	    _rows = data.size();
	}
    __dataStoreName = dataStore.getName();
    __timeStep = timeStep;
	_data = data;
}

/**
From AbstractTableModel.  Returns the class of the data stored in a given column.
@param columnIndex the column for which to return the data class.
*/
public Class getColumnClass (int columnIndex) {
	switch (columnIndex) {
	    case COL_TYPE: return String.class;
	    case COL_SITE_COMMON_NAME: return String.class;
		case COL_SITE_NAME: return String.class;
        case COL_SITE_ID: return Integer.class;
        case COL_SITE_DATATYPE_ID: return Integer.class;
		case COL_SITE_DESCRIPTION: return String.class;
		case COL_DATA_SOURCE: return String.class;
		case COL_DATA_TYPE: return String.class;
		case COL_TIME_STEP: return String.class;
		case COL_MODEL_NAME: return String.class;
	    case COL_MODEL_RUN_NAME: return String.class;
	    case COL_MODEL_HYDROLOGIC_INDICATOR: return String.class;
	    case COL_MODEL_RUN_DATE: return String.class;
	    case COL_MODEL_RUN_ID: return Integer.class;
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
	    case COL_TYPE: return "Real/Model";
	    case COL_SITE_COMMON_NAME: return "Site Common Name";
		case COL_SITE_NAME: return "Site Name";
	    case COL_SITE_ID: return "Site ID";
	    case COL_SITE_DATATYPE_ID: return "Site Datatype ID";
	    case COL_SITE_DESCRIPTION: return "Site Description";
		case COL_DATA_SOURCE: return "Data Source";
		case COL_DATA_TYPE: return "Data Type";
		case COL_TIME_STEP: return "Time Step";
	    case COL_MODEL_NAME: return "Model";
		case COL_MODEL_RUN_NAME: return "Model Run";
		case COL_MODEL_HYDROLOGIC_INDICATOR: return "Model Hydrologic Indicator";
		case COL_MODEL_RUN_DATE: return "Model Run Date";
		case COL_MODEL_RUN_ID: return "Model Run ID";
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
        case COL_SITE_DATATYPE_ID: return "%d";
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
        case COL_TYPE:
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
		case COL_DATA_TYPE:
			return data.getDataTypeCommonName();
		case COL_TIME_STEP:
			return __timeStep;
		case COL_MODEL_NAME:
		    return data.getModelName();
        case COL_MODEL_RUN_NAME:
            return data.getModelRunName();
        case COL_MODEL_HYDROLOGIC_INDICATOR:
            return data.getHydrologicIndicator();
        case COL_MODEL_RUN_DATE:
            return data.getModelRunDate();
        case COL_MODEL_RUN_ID:
            return data.getModelRunID();
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
	widths[COL_TYPE] = 7;
    widths[COL_SITE_COMMON_NAME] = 20;
	widths[COL_SITE_NAME] = 20;
    widths[COL_SITE_ID] = 4;
    widths[COL_SITE_DATATYPE_ID] = 10;
    widths[COL_SITE_DESCRIPTION] = 20;
	widths[COL_DATA_SOURCE] = 11;
	widths[COL_DATA_TYPE] = 14;
	widths[COL_TIME_STEP] = 8;
	widths[COL_MODEL_NAME] = 12;
	widths[COL_MODEL_RUN_NAME] = 12;
	widths[COL_MODEL_HYDROLOGIC_INDICATOR] = 18;
	widths[COL_MODEL_RUN_DATE] = 12;
	widths[COL_MODEL_RUN_ID] = 10;
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