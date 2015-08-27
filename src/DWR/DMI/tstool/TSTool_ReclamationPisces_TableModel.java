package DWR.DMI.tstool;

import java.util.List;

import rti.tscommandprocessor.commands.reclamationpisces.ReclamationPiscesDMI;
import rti.tscommandprocessor.commands.reclamationpisces.ReclamationPiscesDataStore;
import rti.tscommandprocessor.commands.reclamationpisces.ReclamationPisces_SiteCatalogSeriesCatalog;
import RTi.Util.GUI.JWorksheet_AbstractRowTableModel;

/**
This class is a table model for time series header information for Reclamation Pisces
time series.  By default the sheet will contain row and column numbers.
*/
public class TSTool_ReclamationPisces_TableModel extends JWorksheet_AbstractRowTableModel
{

/**
Number of columns in the table model.
*/
private final int COLUMNS = 30;

//Karl Tarbet said not to display the following (2015-08-26)...
//public final int COL_SERIES_ID = 25; 
//public final int COL_SERIES_PARENTID = 26;
//public final int COL_SERIES_ISFOLDER = 27;
//public final int COL_SERIES_SORTORDER = 28;
//public final int COL_SERIES_NOTES = 33;
//public final int COL_SERIES_ENABLED = 34;
public final int COL_SITE_ID = 0;
public final int COL_SITE_DESCRIPTION = 1;
public final int COL_SERIES_NAME = 2;
public final int COL_SERIES_SERVER = 3;
public final int COL_SERIES_PARAMETER = 4;
public final int COL_SERIES_TIMEINTERVAL = 5;
public final int COL_SERIES_UNITS = 6;
public final int COL_SERIES_START = 7;
public final int COL_SERIES_END = 8;
public final int COL_SITE_STATE = 9;
public final int COL_SITE_AGENCY_REGION = 10;
public final int COL_SITE_LONGITUDE = 11;
public final int COL_SITE_LATITUDE = 12;
public final int COL_SITE_HORIZONTAL_DATUM = 13;
public final int COL_SITE_ELEVATION = 14;
public final int COL_SITE_VERTICAL_DATUM = 15;
public final int COL_SITE_ELEVATION_METHOD = 16;
public final int COL_SITE_VERTICAL_ACCURACY = 17;
public final int COL_SITE_TIMEZONE = 18;
public final int COL_SITE_TIMEZONE_OFFSET = 19;
public final int COL_SITE_INSTALL = 20;
public final int COL_SITE_ACTIVE_FLAG = 21;
public final int COL_SITE_TYPE = 22;
public final int COL_SITE_RESPONSIBILITY = 23;
public final int COL_SERIES_PROVIDER = 24;
public final int COL_SERIES_ICONNAME = 25;
public final int COL_SERIES_TABLENAME = 26;
public final int COL_SERIES_CONNECTION_STRING = 27;
public final int COL_SERIES_EXPRESSION = 28;
public final int COL_DATASTORE_NAME	= 29;

/**
Datastore corresponding to datastore used to retrieve the data.
*/
ReclamationPiscesDataStore datastore = null;

/**
Constructor.  This builds the model for displaying the given Reclamation Pisces time series data.
@param dataStore the data store for the data
@param timeStep time-step for all time series
@param data the list of ??? that will be displayed in the
table (null is allowed - see setData()).
@throws Exception if an invalid results passed in.
*/
public TSTool_ReclamationPisces_TableModel ( ReclamationPiscesDataStore dataStore, List data )
throws Exception
{	if ( data == null ) {
		_rows = 0;
	}
	else {
	    _rows = data.size();
	}
    this.datastore = dataStore;
	_data = data;
}

/**
From AbstractTableModel.  Returns the class of the data stored in a given column.
@param columnIndex the column for which to return the data class.
*/
public Class getColumnClass (int columnIndex) {
	switch (columnIndex) {
		case COL_SITE_LATITUDE: return Double.class;
		case COL_SITE_LONGITUDE: return Double.class;
		case COL_SITE_ELEVATION: return Double.class;
		case COL_SITE_VERTICAL_ACCURACY: return Double.class;
		default: return String.class;
	}
}

/**
From AbstractTableMode.  Returns the number of columns of data.
@return the number of columns of data.
*/
public int getColumnCount() {
	return this.COLUMNS;
}

/**
From AbstractTableMode.  Returns the name of the column at the given position.
@return the name of the column at the given position.
*/
public String getColumnName(int columnIndex) {
	switch (columnIndex) {
		case COL_SITE_ID: return "Site ID";
		case COL_SITE_DESCRIPTION: return "Site Description";
		case COL_SERIES_NAME: return "Series Name";
		case COL_SERIES_SERVER: return "Server";
		case COL_SERIES_PARAMETER: return "Parameter (Data Type)";
		case COL_SERIES_TIMEINTERVAL: return "Interval";
		case COL_SERIES_UNITS: return "Units";
		case COL_SERIES_START: return "Start";
		case COL_SERIES_END: return "End";
		case COL_SITE_STATE: return "State";
		case COL_SITE_AGENCY_REGION: return "Region";
		case COL_SITE_LONGITUDE: return "Longitude";
		case COL_SITE_LATITUDE: return "Latitude";
		case COL_SITE_HORIZONTAL_DATUM: return "H Datum";
		case COL_SITE_ELEVATION: return "Elevation";
		case COL_SITE_VERTICAL_DATUM: return "V Datum";
		case COL_SITE_ELEVATION_METHOD: return "E Method";
		case COL_SITE_VERTICAL_ACCURACY: return "V Accuracy";
		case COL_SITE_TIMEZONE: return "Time Zone";
		case COL_SITE_TIMEZONE_OFFSET: return "Time Zone Offset";
		case COL_SITE_INSTALL: return "Site Install";
		case COL_SITE_ACTIVE_FLAG: return "Site Active Flag";
		case COL_SITE_TYPE: return "Site Type";
		case COL_SITE_RESPONSIBILITY: return "Site Responsibility";
		case COL_SERIES_PROVIDER: return "Series Provider";
		case COL_SERIES_ICONNAME: return "Series Icon Name";
		case COL_SERIES_TABLENAME: return "Series Table Name";
		case COL_SERIES_CONNECTION_STRING: return "Series Connection String";
		case COL_SERIES_EXPRESSION: return "Series Expression";
		case COL_DATASTORE_NAME: return "Datastore";
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
		case COL_SITE_LATITUDE: return "%.6f";
		case COL_SITE_LONGITUDE: return "%.6f";
		case COL_SITE_ELEVATION: return "%.2f";
		case COL_SITE_VERTICAL_ACCURACY: return "%.2f";
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
    String[] toolTips = new String[this.COLUMNS];
    toolTips[COL_SITE_ID] = "Site/location identifier";
    toolTips[COL_SITE_DESCRIPTION] = "Description of the site/location";
    toolTips[COL_SERIES_NAME] = "Display name in Pisces for equations that reference the series";
    toolTips[COL_SERIES_SERVER] = "Data server that provides the site/series data";
    toolTips[COL_SERIES_PARAMETER] = "Short data parameter (data type) name";
    toolTips[COL_SERIES_TIMEINTERVAL] = "Data reporting interval:  Hourly, Daily, Weekly, Monthly, Yearly, Irregular";
    toolTips[COL_SERIES_UNITS] = "Data units of measure";
    toolTips[COL_SERIES_START] = "Starting date/time for the series";
    toolTips[COL_SERIES_END] = "Ending date/time for the series";
    toolTips[COL_SITE_STATE] = "State abbreviation";
    toolTips[COL_SITE_AGENCY_REGION] = "Reclamation agency region";
    toolTips[COL_SITE_LONGITUDE] = "Site latitude, decimal degrees";
    toolTips[COL_SITE_LATITUDE] = "Site longitude, decimal degrees";
    toolTips[COL_SITE_HORIZONTAL_DATUM] = "??";
    toolTips[COL_SITE_ELEVATION] = "??";
    toolTips[COL_SITE_VERTICAL_DATUM] = "??";
    toolTips[COL_SITE_ELEVATION_METHOD] = "??";
    toolTips[COL_SITE_VERTICAL_ACCURACY] = "??";
    toolTips[COL_SITE_TIMEZONE] = "??";
    toolTips[COL_SITE_TIMEZONE_OFFSET] = "??";
    toolTips[COL_SITE_INSTALL] = "Date that site was installed";
    toolTips[COL_SITE_ACTIVE_FLAG] = "??";
    toolTips[COL_SITE_TYPE] = "Location type (e.g., reservoir)";
    toolTips[COL_SITE_RESPONSIBILITY] = "??";
    toolTips[COL_SERIES_PROVIDER] = "Name of class derived from Reclamation.TimeSeries.Series (or Series)";
    toolTips[COL_SERIES_ICONNAME] = "Used to render an icon based on the source of the data";
    toolTips[COL_SERIES_TABLENAME] = "Name of Pisces data table for time series";
    toolTips[COL_SERIES_CONNECTION_STRING] = "Provider-specific connection information such as path to Excel file, sheet name, or specific parameter code";
    toolTips[COL_SERIES_EXPRESSION] = "Equation expression for computed series";
    toolTips[COL_DATASTORE_NAME] = "Datastore";
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

	ReclamationPisces_SiteCatalogSeriesCatalog data = (ReclamationPisces_SiteCatalogSeriesCatalog)_data.get(row);
	switch (col) {
		case COL_SITE_ID: return data.getSiteID();
		case COL_SITE_DESCRIPTION: return data.getDescription();
		case COL_SERIES_NAME: return data.getName();
		case COL_SERIES_SERVER: return data.getServer();
		case COL_SERIES_PARAMETER: return data.getParameter();
		case COL_SERIES_TIMEINTERVAL:
			ReclamationPiscesDMI dmi = (ReclamationPiscesDMI)this.datastore.getDMI();
			return dmi.getTSIDIntervalFromPiscesInterval(data.getTimeInterval());
		case COL_SERIES_UNITS: return data.getUnits();
		case COL_SERIES_START: return "";
		case COL_SERIES_END: return "";
		case COL_SITE_STATE: return data.getState();
		case COL_SITE_AGENCY_REGION: return data.getAgencyRegion();
		case COL_SITE_LONGITUDE: return data.getLongitude();
		case COL_SITE_LATITUDE: return data.getLatitude();
		case COL_SITE_HORIZONTAL_DATUM: return data.getHorizontalDatum();
		case COL_SITE_ELEVATION: return data.getElevation();
		case COL_SITE_VERTICAL_DATUM: return data.getVerticalDatum();
		case COL_SITE_ELEVATION_METHOD: return data.getElevationMethod();
		case COL_SITE_VERTICAL_ACCURACY: return data.getVerticalAccuracy();
		case COL_SITE_TIMEZONE: return data.getTimeZone();
		case COL_SITE_TIMEZONE_OFFSET: return data.getTimeZoneOffset();
		case COL_SITE_INSTALL: return data.getInstall();
		case COL_SITE_ACTIVE_FLAG: return data.getActiveFlag();
		case COL_SITE_TYPE: return data.getType();
		case COL_SITE_RESPONSIBILITY: return data.getResponsibility();
		case COL_SERIES_PROVIDER: return data.getProvider();
		case COL_SERIES_ICONNAME: return data.getIconName();
		case COL_SERIES_TABLENAME: return data.getTableName();
		case COL_SERIES_CONNECTION_STRING: return data.getConnectionString();
		case COL_SERIES_EXPRESSION: return data.getExpression();
		case COL_DATASTORE_NAME: return this.datastore.getName();
		default: return "";
	}
}

/**
Returns an array containing the column widths (in number of characters).
@return an integer array containing the widths for each field.
*/
public int[] getColumnWidths() {
	int[] widths = new int[this.COLUMNS];
    widths[COL_SITE_ID] = 10;
    widths[COL_SITE_DESCRIPTION] = 25;
    widths[COL_SERIES_NAME] = 15;
    widths[COL_SERIES_SERVER] = 10;
    widths[COL_SERIES_PARAMETER] = 25;
    widths[COL_SERIES_TIMEINTERVAL] = 6;
    widths[COL_SERIES_UNITS] = 8;
    widths[COL_SERIES_START] = 3;
    widths[COL_SERIES_END] = 3;
    widths[COL_SITE_STATE] = 3;
    widths[COL_SITE_AGENCY_REGION] = 10;
    widths[COL_SITE_LONGITUDE] = 7;
    widths[COL_SITE_LATITUDE] = 7;
    widths[COL_SITE_HORIZONTAL_DATUM] = 6;
    widths[COL_SITE_ELEVATION] = 6;
    widths[COL_SITE_VERTICAL_DATUM] = 6;
    widths[COL_SITE_ELEVATION_METHOD] = 6;
    widths[COL_SITE_VERTICAL_ACCURACY] = 7;
    widths[COL_SITE_TIMEZONE] = 7;
    widths[COL_SITE_TIMEZONE_OFFSET] = 11;
    widths[COL_SITE_INSTALL] = 7;
    widths[COL_SITE_ACTIVE_FLAG] = 11;
    widths[COL_SITE_TYPE] = 7;
    widths[COL_SERIES_PROVIDER] = 13;
    widths[COL_SITE_RESPONSIBILITY] = 13;
    widths[COL_SERIES_ICONNAME] = 12;
    widths[COL_SERIES_TABLENAME] = 20;
    widths[COL_SERIES_CONNECTION_STRING] = 20;
    widths[COL_SERIES_EXPRESSION] = 20;
    widths[COL_DATASTORE_NAME] = 15;
	return widths;
}

}