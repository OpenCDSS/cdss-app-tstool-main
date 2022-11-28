// TSTool_UsgsNwis - integration between TSTool UI and USGS NWIS web service datastore

/* NoticeStart

TSTool
TSTool is a part of Colorado's Decision Support Systems (CDSS)
Copyright (C) 1994-2022 Colorado Department of Natural Resources

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

package cdss.app.tstool.datastore.usgs.nwis;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JPanel;

import DWR.DMI.tstool.TSToolConstants;
import DWR.DMI.tstool.TSTool_JFrame;
import DWR.DMI.tstool.TSTool_TS_CellRenderer;
import DWR.DMI.tstool.TSTool_TS_TableModel;
import RTi.TS.TS;
import RTi.TS.UsgsNwisRdbTS;
import RTi.Util.GUI.JFileChooserFactory;
import RTi.Util.GUI.JGUIUtil;
import RTi.Util.GUI.JWorksheet;
import RTi.Util.GUI.JWorksheet_AbstractRowTableModel;
import RTi.Util.GUI.SimpleFileFilter;
import RTi.Util.GUI.SimpleJComboBox;
import RTi.Util.Message.Message;
import riverside.datastore.DataStore;
import rti.tscommandprocessor.commands.usgs.nwis.daily.UsgsNwisDailyDataStore;
import rti.tscommandprocessor.commands.usgs.nwis.daily.UsgsNwisDaily_TimeSeries_InputFilter_JPanel;
import rti.tscommandprocessor.commands.usgs.nwis.daily.UsgsNwisSiteTimeSeriesMetadata;
import rti.tscommandprocessor.commands.usgs.nwis.daily.ui.TSTool_UsgsNwisDaily_CellRenderer;
import rti.tscommandprocessor.commands.usgs.nwis.daily.ui.TSTool_UsgsNwisDaily_TableModel;
import rti.tscommandprocessor.commands.usgs.nwis.groundwater.UsgsNwisGroundwaterDataStore;
import rti.tscommandprocessor.commands.usgs.nwis.groundwater.UsgsNwisGroundwater_TimeSeries_InputFilter_JPanel;
import rti.tscommandprocessor.commands.usgs.nwis.groundwater.ui.TSTool_UsgsNwisGroundwater_CellRenderer;
import rti.tscommandprocessor.commands.usgs.nwis.groundwater.ui.TSTool_UsgsNwisGroundwater_TableModel;
import rti.tscommandprocessor.commands.usgs.nwis.instantaneous.UsgsNwisInstantaneousDataStore;
import rti.tscommandprocessor.commands.usgs.nwis.instantaneous.UsgsNwisInstantaneous_TimeSeries_InputFilter_JPanel;
import rti.tscommandprocessor.commands.usgs.nwis.instantaneous.ui.TSTool_UsgsNwisInstantaneous_CellRenderer;
import rti.tscommandprocessor.commands.usgs.nwis.instantaneous.ui.TSTool_UsgsNwisInstantaneous_TableModel;

/**
 * This class provides integration between TSTool and the USGS NWIS web service datastores.
 * These datastores are currently built into TSTool, rather than being a plugin.
 * Isolating the code simplifies maintenance.
 * The class is a singleton.  Use getInstance() to get the instance to call methods.
 */
public class TSTool_UsgsNwis {
	
	/**
	 * Singleton instance of this class.
	 * Use getInstance() to return the singleton.
	 */
	private static TSTool_UsgsNwis instance = null;
	
	/**
	 * Instance of the TSTool main UI.
	 */
	private TSTool_JFrame tstoolJFrame = null;
	
	/**
	 * Private constructor for lazy initialization.
	 */
	private TSTool_UsgsNwis ( TSTool_JFrame tstoolJFrame ) {
		this.tstoolJFrame = tstoolJFrame;
	}

	/**
	Refresh the query choices for the currently selected USGS NWIS daily datastore.
	*/
	public void dataStoreSelected_Daily ( UsgsNwisDailyDataStore selectedDataStore )
			throws Exception
	{   //String routine = getClass().getSimpleName() + "uiAction_SelectDataStore_UsgsNwis";
		UsgsNwisDailyDataStore dataStore = (UsgsNwisDailyDataStore)selectedDataStore;
		this.tstoolJFrame.ui_SetInputNameVisible(false); // Not needed for datastores.
		// Get the list of valid object/data types from the datastore.
		List<String> dataTypes = dataStore.getParameterStrings ( true );

		// Populate the list of available data types and select the first.
		SimpleJComboBox dataType_JComboBox = tstoolJFrame.ui_GetDataTypeJComboBox();
		dataType_JComboBox.setEnabled ( true );
		dataType_JComboBox.removeAll ();
		dataType_JComboBox.setData ( dataTypes );
		dataType_JComboBox.select ( null );
		dataType_JComboBox.select ( 0 );

		// Initialize the time series list with blank data list.
   		JWorksheet_AbstractRowTableModel query_TableModel = new TSTool_UsgsNwisDaily_TableModel( dataStore, null);
   		this.tstoolJFrame.ui_SetTimeSeriesCatalogTableModel ( query_TableModel );
		TSTool_UsgsNwisDaily_CellRenderer cr = new TSTool_UsgsNwisDaily_CellRenderer((TSTool_UsgsNwisDaily_TableModel)query_TableModel);
   		JWorksheet query_JWorksheet = tstoolJFrame.ui_GetTimeSeriesCatalogWorksheet();
		query_JWorksheet.setCellRenderer ( cr );
		query_JWorksheet.setModel ( query_TableModel );
		query_JWorksheet.setColumnWidths ( cr.getColumnWidths() );
	}

	/**
	Refresh the query choices for the currently selected USGS NWIS groundwater datastore.
	*/
	public void dataStoreSelected_Groundwater ( UsgsNwisGroundwaterDataStore selectedDataStore )
			throws Exception
	{   //String routine = getClass().getSimpleName() + "uiAction_SelectDataStore_UsgsNwis";
		UsgsNwisGroundwaterDataStore dataStore = (UsgsNwisGroundwaterDataStore)selectedDataStore;
		this.tstoolJFrame.ui_SetInputNameVisible(false); // Not needed for datastores.
		// Get the list of valid object/data types from the datastore.
		List<String> dataTypes = dataStore.getParameterStrings ( true );

		// Populate the list of available data types and select the first.
		SimpleJComboBox dataType_JComboBox = tstoolJFrame.ui_GetDataTypeJComboBox();
		dataType_JComboBox.setEnabled ( true );
		dataType_JComboBox.removeAll ();
		dataType_JComboBox.setData ( dataTypes );
		dataType_JComboBox.select ( null );
		dataType_JComboBox.select ( 0 );

		// Initialize the time series list with blank data list.
   		JWorksheet_AbstractRowTableModel query_TableModel = new TSTool_UsgsNwisGroundwater_TableModel( dataStore, null);
   		this.tstoolJFrame.ui_SetTimeSeriesCatalogTableModel ( query_TableModel );
		TSTool_UsgsNwisGroundwater_CellRenderer cr = new TSTool_UsgsNwisGroundwater_CellRenderer((TSTool_UsgsNwisGroundwater_TableModel)query_TableModel);
   		JWorksheet query_JWorksheet = tstoolJFrame.ui_GetTimeSeriesCatalogWorksheet();
		query_JWorksheet.setCellRenderer ( cr );
		query_JWorksheet.setModel ( query_TableModel );
		query_JWorksheet.setColumnWidths ( cr.getColumnWidths() );
	}

	/**
	Refresh the query choices for the currently selected USGS NWIS instantaneous datastore.
	*/
	public void dataStoreSelected_Instantaneous ( UsgsNwisInstantaneousDataStore selectedDataStore )
			throws Exception
	{   //String routine = getClass().getSimpleName() + "uiAction_SelectDataStore_UsgsNwis";
		UsgsNwisInstantaneousDataStore dataStore = (UsgsNwisInstantaneousDataStore)selectedDataStore;
		this.tstoolJFrame.ui_SetInputNameVisible(false); // Not needed for datastores.
		// Get the list of valid object/data types from the datastore.
		List<String> dataTypes = dataStore.getParameterStrings ( true );

		// Populate the list of available data types and select the first.
		SimpleJComboBox dataType_JComboBox = tstoolJFrame.ui_GetDataTypeJComboBox();
		dataType_JComboBox.setEnabled ( true );
		dataType_JComboBox.removeAll ();
		dataType_JComboBox.setData ( dataTypes );
		dataType_JComboBox.select ( null );
		dataType_JComboBox.select ( 0 );

		// Initialize the time series list with blank data list.
   		JWorksheet_AbstractRowTableModel query_TableModel = new TSTool_UsgsNwisInstantaneous_TableModel( dataStore, null);
   		this.tstoolJFrame.ui_SetTimeSeriesCatalogTableModel ( query_TableModel );
		TSTool_UsgsNwisInstantaneous_CellRenderer cr = new TSTool_UsgsNwisInstantaneous_CellRenderer((TSTool_UsgsNwisInstantaneous_TableModel)query_TableModel);
   		JWorksheet query_JWorksheet = tstoolJFrame.ui_GetTimeSeriesCatalogWorksheet();
		query_JWorksheet.setCellRenderer ( cr );
		query_JWorksheet.setModel ( query_TableModel );
		query_JWorksheet.setColumnWidths ( cr.getColumnWidths() );
	}
	
	/**
	 * Handle data type selection for daily datastore.
	 * @param dataStore selected datastore
	 * @param selectedDataType selected data type
	 */
	public void dataTypeSelected_Daily ( UsgsNwisDailyDataStore dataStore, String selectedDataType ) {
        // Set intervals for the data type and trigger a select to populate the input filters.
        SimpleJComboBox timeStep_JComboBox = this.tstoolJFrame.ui_GetDataTypeJComboBox();
        timeStep_JComboBox.removeAll ();
        timeStep_JComboBox.setEnabled ( true );
        timeStep_JComboBox.setData ( dataStore.getDataIntervalStringsForDataType(selectedDataType));
        timeStep_JComboBox.select ( null );
        timeStep_JComboBox.select ( 0 );
	}

	/**
	 * Handle data type selection for groundwater datastore.
	 * @param dataStore selected datastore
	 * @param selectedDataType selected data type
	 */
	public void dataTypeSelected_Groundwater ( UsgsNwisGroundwaterDataStore dataStore, String selectedDataType ) {
        // Set intervals for the data type and trigger a select to populate the input filters.
        SimpleJComboBox timeStep_JComboBox = this.tstoolJFrame.ui_GetDataTypeJComboBox();
        timeStep_JComboBox.removeAll ();
        timeStep_JComboBox.setEnabled ( true );
        timeStep_JComboBox.setData ( dataStore.getDataIntervalStringsForDataType(selectedDataType));
        timeStep_JComboBox.select ( null );
        timeStep_JComboBox.select ( 0 );
	}

	/**
	 * Handle data type selection for instantaneous datastore.
	 * @param dataStore selected datastore
	 * @param selectedDataType selected data type
	 */
	public void dataTypeSelected_Instantaneous ( UsgsNwisInstantaneousDataStore dataStore, String selectedDataType ) {
        // Set intervals for the data type and trigger a select to populate the input filters.
        SimpleJComboBox timeStep_JComboBox = this.tstoolJFrame.ui_GetDataTypeJComboBox();
        timeStep_JComboBox.removeAll ();
        timeStep_JComboBox.setEnabled ( true );
        timeStep_JComboBox.setData ( dataStore.getDataIntervalStringsForDataType(selectedDataType));
        timeStep_JComboBox.select ( null );
        timeStep_JComboBox.select ( 0 );
    }

	/**
	 * Handle data type selection for an RDB file.
	 */
	public void dataTypeSelected_Rdb () {
        SimpleJComboBox timeStep_JComboBox = this.tstoolJFrame.ui_GetDataTypeJComboBox();
		timeStep_JComboBox.removeAll ();
		timeStep_JComboBox.add ( TSToolConstants.TIMESTEP_AUTO );
		timeStep_JComboBox.select ( null );
		timeStep_JComboBox.select ( TSToolConstants.TIMESTEP_AUTO );
		timeStep_JComboBox.setEnabled ( false );
	}

	/**
	 * Get the singleton instance.
	 */
	public static TSTool_UsgsNwis getInstance ( TSTool_JFrame tstoolJFrame ) {
		if ( TSTool_UsgsNwis.instance == null ) {
			TSTool_UsgsNwis.instance = new TSTool_UsgsNwis( tstoolJFrame );
		}
		return TSTool_UsgsNwis.instance;
	}

	/**
	Read USGS NWIS daily values web service time series and list in the GUI.
	*/
	public void getTimeSeriesListClicked_ReadUsgsNwisDailyCatalog () {
	    String rtn = getClass().getSimpleName() + ".getTimeSeriesListClicked_ReadUsgsNwisDailyCatalog";
	    JGUIUtil.setWaitCursor ( tstoolJFrame, true );
	    Message.printStatus ( 1, rtn, "Please wait... retrieving data.");

	    DataStore dataStore = tstoolJFrame.ui_GetSelectedDataStore ();
	    // The headers are a list of UsgsNwisTimeSeriesMetadata.
	    try {
	    	UsgsNwisDailyDataStore usgsNwisDailyDataStore = (UsgsNwisDailyDataStore)dataStore;
	    	tstoolJFrame.queryResultsList_Clear ();

	    	String dataType = tstoolJFrame.ui_GetSelectedDataType();
	    	String timeStep = tstoolJFrame.ui_GetSelectedTimeStep();
	    	if ( timeStep == null ) {
	    		Message.printWarning ( 1, rtn, "No time series are available for timestep." );
	    		JGUIUtil.setWaitCursor ( tstoolJFrame, false );
	    		return;
	    	}
	    	else {
	    		timeStep = timeStep.trim();
	    	}

	    	List<UsgsNwisSiteTimeSeriesMetadata> results = null;
	    	// Data type is shown with name so only use the first part of the choice.
	    	try {
	    		results = usgsNwisDailyDataStore.readSiteTimeSeriesMetadataList(dataType, timeStep, tstoolJFrame.ui_GetSelectedInputFilterJPanel());
	    	}
	    	catch ( Exception e ) {
	    		Message.printWarning(1, rtn, "Error getting time series list from USGS NWIS (" + e + ").");
	    		Message.printWarning(3, rtn, e );
	    		results = null;
	    	}

	    	int size = 0;
	    	if ( results != null ) {
	    		size = results.size();
	    		// TODO Does not work??
	    		//__query_TableModel.setNewData ( results );
	    		// Try brute force.
	    		JWorksheet_AbstractRowTableModel query_TableModel = new TSTool_UsgsNwisDaily_TableModel ( usgsNwisDailyDataStore, results );
	    		this.tstoolJFrame.ui_SetTimeSeriesCatalogTableModel ( query_TableModel );
	    		TSTool_UsgsNwisDaily_CellRenderer cr = new TSTool_UsgsNwisDaily_CellRenderer( (TSTool_UsgsNwisDaily_TableModel)query_TableModel);

	    		JWorksheet query_JWorksheet = tstoolJFrame.ui_GetTimeSeriesCatalogWorksheet();
	    		query_JWorksheet.setCellRenderer ( cr );
	    		query_JWorksheet.setModel ( query_TableModel );
	    		query_JWorksheet.setColumnWidths ( cr.getColumnWidths(), tstoolJFrame.getGraphics() );
	    	}
	    	if ( (results == null) || (size == 0) ) {
	    		Message.printStatus ( 1, rtn, "Query complete.  No records returned." );
	    	}
	    	else {
	    		Message.printStatus ( 1, rtn, "Query complete. " + size + " records returned." );
	    	}
	    	tstoolJFrame.ui_UpdateStatus ( false );
	    	JGUIUtil.setWaitCursor ( tstoolJFrame, false );
	    }
	    catch ( Exception e ) {
	    	// Messages elsewhere but catch so we can get the cursor back.
	    	Message.printWarning ( 3, rtn, e );
	    	JGUIUtil.setWaitCursor ( tstoolJFrame, false );
	    }
	}

	/**
	Read USGS NWIS groundwater web service time series and list in the GUI.
	*/
	public void getTimeSeriesListClicked_ReadUsgsNwisGroundwaterCatalog () {
	    String rtn = getClass().getSimpleName() + ".getTimeSeriesListClicked_ReadUsgsNwisGroundwaterCatalog";
	    JGUIUtil.setWaitCursor ( tstoolJFrame, true );
	    Message.printStatus ( 1, rtn, "Please wait... retrieving data.");

	    DataStore dataStore = tstoolJFrame.ui_GetSelectedDataStore ();
	    // The headers are a list of UsgsNwisTimeSeriesMetadata.
	    try {
	    	UsgsNwisGroundwaterDataStore usgsNwisGroundwaterDataStore = (UsgsNwisGroundwaterDataStore)dataStore;
	    	tstoolJFrame.queryResultsList_Clear ();

	    	String dataType = tstoolJFrame.ui_GetSelectedDataType();
	    	String timeStep = tstoolJFrame.ui_GetSelectedTimeStep();
	    	if ( timeStep == null ) {
	    		Message.printWarning ( 1, rtn, "No time series are available for timestep." );
	    		JGUIUtil.setWaitCursor ( tstoolJFrame, false );
	    		return;
	    	}
	    	else {
	    		timeStep = timeStep.trim();
	    	}

	    	List<UsgsNwisSiteTimeSeriesMetadata> results = null;
	    	// Data type is shown with name so only use the first part of the choice.
	    	try {
	    		results = usgsNwisGroundwaterDataStore.readSiteTimeSeriesMetadataList(dataType, timeStep, tstoolJFrame.ui_GetSelectedInputFilterJPanel());
	    	}
	    	catch ( Exception e ) {
	    		Message.printWarning(1, rtn, "Error getting time series list from USGS NWIS groundwater (" + e + ").");
	    		Message.printWarning(3, rtn, e );
	    		results = null;
	    	}

	    	int size = 0;
	    	if ( results != null ) {
	    		size = results.size();
	    		// TODO Does not work??
	    		//__query_TableModel.setNewData ( results );
	    		// Try brute force...
	    		JWorksheet_AbstractRowTableModel query_TableModel =
	    			new TSTool_UsgsNwisGroundwater_TableModel ( usgsNwisGroundwaterDataStore, results );
	    		this.tstoolJFrame.ui_SetTimeSeriesCatalogTableModel ( query_TableModel );
	    		TSTool_UsgsNwisGroundwater_CellRenderer cr =
	    				new TSTool_UsgsNwisGroundwater_CellRenderer( (TSTool_UsgsNwisGroundwater_TableModel)query_TableModel);

	    		JWorksheet query_JWorksheet = tstoolJFrame.ui_GetTimeSeriesCatalogWorksheet();
	    		query_JWorksheet.setCellRenderer ( cr );
	    		query_JWorksheet.setModel ( query_TableModel );
	    		query_JWorksheet.setColumnWidths ( cr.getColumnWidths(), tstoolJFrame.getGraphics() );
	    	}
	    	if ( (results == null) || (size == 0) ) {
	    		Message.printStatus ( 1, rtn, "Query complete.  No records returned." );
	    	}
	    	else {
	    		Message.printStatus ( 1, rtn, "Query complete. " + size + " records returned." );
	    	}
	    	tstoolJFrame.ui_UpdateStatus ( false );
	    	JGUIUtil.setWaitCursor ( tstoolJFrame, false );
	    }
	    catch ( Exception e ) {
	    	// Messages elsewhere but catch so we can get the cursor back.
	    	Message.printWarning ( 3, rtn, e );
	    	JGUIUtil.setWaitCursor ( tstoolJFrame, false );
	    }
	}

	/**
	Read USGS NWIS instantaneous values web service time series and list in the GUI.
	*/
	public void getTimeSeriesListClicked_ReadUsgsNwisInstantaneousCatalog () {
	    String rtn = getClass().getSimpleName() + "getTimeSeriesListClicked_ReadUsgsNwisInstantaneousCatalog";
	    JGUIUtil.setWaitCursor ( tstoolJFrame, true );
	    Message.printStatus ( 1, rtn, "Please wait... retrieving data.");

	    DataStore dataStore = tstoolJFrame.ui_GetSelectedDataStore ();
	    // The headers are a list of UsgsNwisTimeSeriesMetadata.
	    try {
	    	UsgsNwisInstantaneousDataStore usgsNwisInstantaneousDataStore = (UsgsNwisInstantaneousDataStore)dataStore;
	    	tstoolJFrame.queryResultsList_Clear ();

	    	String dataType = tstoolJFrame.ui_GetSelectedDataType();
	    	String timeStep = tstoolJFrame.ui_GetSelectedTimeStep();
	    	if ( timeStep == null ) {
	    		Message.printWarning ( 1, rtn, "No time series are available for timestep." );
	    		JGUIUtil.setWaitCursor ( tstoolJFrame, false );
	    		return;
	    	}
	    	else {
	    		timeStep = timeStep.trim();
	    	}

	    	List<UsgsNwisSiteTimeSeriesMetadata> results = null;
	    	// Data type is shown with name so only use the first part of the choice.
	    	try {
	    		results = usgsNwisInstantaneousDataStore.readSiteTimeSeriesMetadataList(dataType, timeStep, tstoolJFrame.ui_GetSelectedInputFilterJPanel());
	    	}
	    	catch ( Exception e ) {
	    		Message.printWarning(1, rtn, "Error getting time series list from USGS NWIS (" + e + ").");
	    		Message.printWarning(3, rtn, e );
	    		results = null;
	    	}

	    	int size = 0;
	    	if ( results != null ) {
	    		size = results.size();
	    		// TODO Does not work??
	    		//__query_TableModel.setNewData ( results );
	    		// Try brute force.
	    		JWorksheet_AbstractRowTableModel query_TableModel =
	    			new TSTool_UsgsNwisInstantaneous_TableModel ( usgsNwisInstantaneousDataStore, results );
	    		this.tstoolJFrame.ui_SetTimeSeriesCatalogTableModel ( query_TableModel );
	    		TSTool_UsgsNwisInstantaneous_CellRenderer cr =
	    			new TSTool_UsgsNwisInstantaneous_CellRenderer( (TSTool_UsgsNwisInstantaneous_TableModel)query_TableModel);

	    		JWorksheet query_JWorksheet = tstoolJFrame.ui_GetTimeSeriesCatalogWorksheet();
	    		query_JWorksheet.setCellRenderer ( cr );
	    		query_JWorksheet.setModel ( query_TableModel );
	    		query_JWorksheet.setColumnWidths ( cr.getColumnWidths(), tstoolJFrame.getGraphics() );
	    	}
	    	if ( (results == null) || (size == 0) ) {
	    		Message.printStatus ( 1, rtn, "Query complete.  No records returned." );
	    	}
	    	else {
	    		Message.printStatus ( 1, rtn, "Query complete. " + size + " records returned." );
	    	}
	    	tstoolJFrame.ui_UpdateStatus ( false );
	    	JGUIUtil.setWaitCursor ( tstoolJFrame, false );
	    }
	    catch ( Exception e ) {
	    	// Messages elsewhere but catch so we can get the cursor back.
	    	Message.printWarning ( 3, rtn, e );
	    	JGUIUtil.setWaitCursor ( tstoolJFrame, false );
	    }
	}

	/**
	Read the list of time series from a USGS NWIS RDB file and list in the GUI.
	*/
	public void getTimeSeriesListClicked_ReadUsgsNwisRdbCatalog ()
	throws IOException {
		String message, routine = getClass().getSimpleName() + ".readUsgsNwisRdbCatalog";

		try {
			JFileChooser fc = JFileChooserFactory.createJFileChooser ( JGUIUtil.getLastFileDialogDirectory() );
			fc.setDialogTitle ( "Select USGS NWIS RDB File");
			SimpleFileFilter sff = new SimpleFileFilter( "txt", "USGS NWIS RDB File");
			fc.addChoosableFileFilter(sff);
			if ( fc.showOpenDialog(tstoolJFrame) != JFileChooser.APPROVE_OPTION) {
				// Canceled.
				return;
			}
			JGUIUtil.setLastFileDialogDirectory ( fc.getSelectedFile().getParent() );
			String path = fc.getSelectedFile().getPath();

			Message.printStatus ( 1, routine, "Reading USGS NWIS RDB file \"" + path + "\"." );
			JGUIUtil.setWaitCursor ( tstoolJFrame, true );
			TS ts = UsgsNwisRdbTS.readTimeSeries(path, null, null, null, null, null, null,false);
			if ( ts == null ) {
				message = "Error reading USGS NWIS RDB file \"" + path + "\".";
				Message.printWarning ( 2, routine, message );
				JGUIUtil.setWaitCursor ( tstoolJFrame, false );
				throw new IOException ( message );
			}
			List<TS> tslist = new ArrayList<>( 1 );
			tslist.add ( ts );
    		JWorksheet_AbstractRowTableModel query_TableModel = new TSTool_TS_TableModel ( tslist );
    		this.tstoolJFrame.ui_SetTimeSeriesCatalogTableModel ( query_TableModel );
			TSTool_TS_CellRenderer cr = new TSTool_TS_CellRenderer(	(TSTool_TS_TableModel)query_TableModel);

    		JWorksheet query_JWorksheet = tstoolJFrame.ui_GetTimeSeriesCatalogWorksheet();
			query_JWorksheet.setCellRenderer ( cr );
			query_JWorksheet.setModel ( query_TableModel );
			query_JWorksheet.removeColumn (((TSTool_TS_TableModel)query_TableModel).COL_ALIAS );
			query_JWorksheet.setColumnWidths ( cr.getColumnWidths(), tstoolJFrame.getGraphics() );

			tstoolJFrame.ui_UpdateStatus ( false );
			JGUIUtil.setWaitCursor ( tstoolJFrame, false );
		}
		catch ( Exception e ) {
			message = "Error reading USGS NWIS RDB file.";
			Message.printWarning ( 2, routine, message );
			Message.printWarning ( 2, routine, e );
			JGUIUtil.setWaitCursor ( tstoolJFrame, false );
			throw new IOException ( message );
		}
	}

	/**
	Initialize the USGS NWIS input filter (may be called at startup).
	@param dataStoreList the list of datastores for which input filter panels are to be added.
	@param y the position in the input panel that the filter should be added
	 */
	public void initGUIInputFiltersUsgsNwisDaily ( List<DataStore> dataStoreList, int y ) {
	    String routine = getClass().getSimpleName() + ".ui_InitGUIInputFiltersUsgsNwisDaily";
	    Message.printStatus ( 2, routine, "Initializing input filter(s) for " + dataStoreList.size() +
	    		" UsgsNwisDaily datastores." );
	    String selectedDataType = this.tstoolJFrame.ui_GetSelectedDataType();
	    String selectedTimeStep = this.tstoolJFrame.ui_GetSelectedTimeStep();
	    for ( DataStore dataStore: dataStoreList ) {
	    	try {
	    		// Try to find an existing input filter panel for the same name.
	    		JPanel ifp = this.tstoolJFrame.ui_GetInputFilterPanelForDataStoreName (
	    				dataStore.getName(), selectedDataType, selectedTimeStep );
	    		// If the previous instance is not null, remove it from the list.
	    		if ( ifp != null ) {
	    			this.tstoolJFrame.ui_GetInputFilterJPanelList().remove ( ifp );
	    		}
	    		// Create a new panel.
	    		UsgsNwisDaily_TimeSeries_InputFilter_JPanel newIfp =
	    				new UsgsNwisDaily_TimeSeries_InputFilter_JPanel((UsgsNwisDailyDataStore)dataStore, 3);

	    		// Add the new panel to the layout and set in the global data.
	    		int buffer = 3;
	    		Insets insets = new Insets(0,buffer,0,0);
	    		JGUIUtil.addComponent(this.tstoolJFrame.ui_GetQueryInputJPanel(), newIfp,
	    				0, y, 3, 1, 1.0, 0.0, insets, GridBagConstraints.HORIZONTAL,
	    				GridBagConstraints.WEST );
	    		newIfp.setName("UsgsNwisDaily.InputFilterPanel");
	    		this.tstoolJFrame.ui_GetInputFilterJPanelList().add ( newIfp );
	    	}
	    	catch ( Exception e ) {
	    		Message.printWarning ( 2, routine,
	    				"Unable to initialize input filter for USGS NWIS daily time series for datastore \"" +
	    						dataStore.getName() + "\" (" + e + ")." );
	    		Message.printWarning ( 2, routine, e );
	    	}
	    }
	}

	/**
	Initialize the USGS NWIS groundwater datastore input filter (may be called at startup).
	@param dataStoreList the list of datastores for which input filter panels are to be added.
	@param y the position in the input panel that the filter should be added
	*/
	public void initGUIInputFiltersUsgsNwisGroundwater ( List<DataStore> dataStoreList, int y ) {
	    String routine = getClass().getSimpleName() + ".ui_InitGUIInputFiltersUsgsNwisGroundwater";
	    Message.printStatus ( 2, routine, "Initializing input filter(s) for " + dataStoreList.size() +
			" UsgsNwisGroundwater datastores." );
	    String selectedDataType = this.tstoolJFrame.ui_GetSelectedDataType();
	    String selectedTimeStep = this.tstoolJFrame.ui_GetSelectedTimeStep();
	    for ( DataStore dataStore: dataStoreList ) {
	    	try {
	    		// Try to find an existing input filter panel for the same name.
	    		JPanel ifp = this.tstoolJFrame.ui_GetInputFilterPanelForDataStoreName (
	    				dataStore.getName(), selectedDataType, selectedTimeStep );
	    		// If the previous instance is not null, remove it from the list.
	    		if ( ifp != null ) {
	    			this.tstoolJFrame.ui_GetInputFilterJPanelList().remove ( ifp );
	    		}
	    		// Create a new panel.
	    		UsgsNwisGroundwater_TimeSeries_InputFilter_JPanel newIfp =
	    				new UsgsNwisGroundwater_TimeSeries_InputFilter_JPanel((UsgsNwisGroundwaterDataStore)dataStore, 3);

	    		// Add the new panel to the layout and set in the global data.
	    		int buffer = 3;
	    		Insets insets = new Insets(0,buffer,0,0);
	    		JGUIUtil.addComponent(this.tstoolJFrame.ui_GetQueryInputJPanel(), newIfp,
	    				0, y, 3, 1, 1.0, 0.0, insets, GridBagConstraints.HORIZONTAL,
	    				GridBagConstraints.WEST );
	    		newIfp.setName("UsgsNwisGroundwater.InputFilterPanel");
	    		this.tstoolJFrame.ui_GetInputFilterJPanelList().add ( newIfp );
	    	}
	    	catch ( Exception e ) {
	    		Message.printWarning ( 2, routine,
	    				"Unable to initialize input filter for USGS NWIS groundwater time series for datastore \"" +
	    						dataStore.getName() + "\" (" + e + ")." );
	    		Message.printWarning ( 2, routine, e );
	    	}
	    }
	}

	/**
	Initialize the USGS NWIS instantaneous values input filter (may be called at startup).
	@param dataStoreList the list of datastores for which input filter panels are to be added.
	@param y the position in the input panel that the filter should be added
	 */
	public void initGUIInputFiltersUsgsNwisInstantaneous ( List<DataStore> dataStoreList, int y ) {
	    String routine = getClass().getSimpleName() + ".ui_InitGUIInputFiltersUsgsNwisInstantaneous";
	    Message.printStatus ( 2, routine, "Initializing input filter(s) for " + dataStoreList.size() +
	    		" UsgsNwisInstantaneous datastores." );
	    String selectedDataType = this.tstoolJFrame.ui_GetSelectedDataType();
	    String selectedTimeStep = this.tstoolJFrame.ui_GetSelectedTimeStep();
	    for ( DataStore dataStore: dataStoreList ) {
	    	try {
	    		// Try to find an existing input filter panel for the same name.
	    		JPanel ifp = this.tstoolJFrame.ui_GetInputFilterPanelForDataStoreName (
	    				dataStore.getName(), selectedDataType, selectedTimeStep );
	    		// If the previous instance is not null, remove it from the list.
	    		if ( ifp != null ) {
	    			this.tstoolJFrame.ui_GetInputFilterJPanelList().remove ( ifp );
	    		}
	    		// Create a new panel.
	    		UsgsNwisInstantaneous_TimeSeries_InputFilter_JPanel newIfp =
	    				new UsgsNwisInstantaneous_TimeSeries_InputFilter_JPanel((UsgsNwisInstantaneousDataStore)dataStore, 3);

	    		// Add the new panel to the layout and set in the global data.
	    		int buffer = 3;
	    		Insets insets = new Insets(0,buffer,0,0);
	    		JGUIUtil.addComponent(this.tstoolJFrame.ui_GetQueryInputJPanel(), newIfp,
	    				0, y, 3, 1, 1.0, 0.0, insets, GridBagConstraints.HORIZONTAL,
	    				GridBagConstraints.WEST );
	    		newIfp.setName("UsgsNwisInstantaneous.InputFilterPanel");
	    		this.tstoolJFrame.ui_GetInputFilterJPanelList().add ( newIfp );
	    	}
	    	catch ( Exception e ) {
	    		Message.printWarning ( 2, routine,
	    				"Unable to initialize input filter for USGS NWIS instantaneous time series for datastore \"" +
	    						dataStore.getName() + "\" (" + e + ")." );
	    		Message.printWarning ( 2, routine, e );
	    	}
	    }
	}

	/**
	Set up query choices because the USGS NWIS input type has been selected.
	*/
	public void inputTypeSelected ()
	throws Exception {
		// Most information is determined from the file.
		tstoolJFrame.ui_SetInputNameVisible(false); // Not needed.
		SimpleJComboBox inputName_JComboBox = tstoolJFrame.ui_GetInputNameJComboBox();
		inputName_JComboBox.removeAll();
		inputName_JComboBox.setEnabled ( false );

		SimpleJComboBox dataType_JComboBox = tstoolJFrame.ui_GetDataTypeJComboBox();
		dataType_JComboBox.setEnabled ( false );
		dataType_JComboBox.removeAll ();
		dataType_JComboBox.add( TSToolConstants.DATA_TYPE_AUTO );

		// Initialize with blank data list.

		JWorksheet_AbstractRowTableModel query_TableModel = new TSTool_TS_TableModel(null);
   		this.tstoolJFrame.ui_SetTimeSeriesCatalogTableModel ( query_TableModel );
		TSTool_TS_CellRenderer cr = new TSTool_TS_CellRenderer((TSTool_TS_TableModel)query_TableModel);

		JWorksheet query_JWorksheet = tstoolJFrame.ui_GetTimeSeriesCatalogWorksheet();
		query_JWorksheet.setCellRenderer ( cr );
		query_JWorksheet.setModel ( query_TableModel );
		// Turn off columns in the table model that do not apply.
		query_JWorksheet.removeColumn (((TSTool_TS_TableModel)query_TableModel).COL_ALIAS );
		query_JWorksheet.removeColumn (((TSTool_TS_TableModel)query_TableModel).COL_DATA_SOURCE );
		query_JWorksheet.removeColumn (((TSTool_TS_TableModel)query_TableModel).COL_DATA_TYPE );
		query_JWorksheet.removeColumn (((TSTool_TS_TableModel)query_TableModel).COL_SCENARIO);
		query_JWorksheet.removeColumn ( ((TSTool_TS_TableModel)query_TableModel).COL_SEQUENCE );
		query_JWorksheet.setColumnWidths ( cr.getColumnWidths() );
	}

	/**
	 * Transfer one time series catalog row to a command, for daily time series.
	 * @param row the time series catalog row to transfer (0+)
	 * @param useAlias whether to use an alias
	 */
    public int transferOneTimeSeriesCatalogRowToCommands_Daily ( int row, boolean useAlias, int insertOffset ) {
        // The location (id), type, and time step uniquely identify the time series,
    	// but the input_name is needed to indicate the database.
		JWorksheet_AbstractRowTableModel query_TableModel = this.tstoolJFrame.ui_GetTimeSeriesCatalogTableModel();
        TSTool_UsgsNwisDaily_TableModel model = (TSTool_UsgsNwisDaily_TableModel)query_TableModel;
        if (model.getSortOrder() != null) {
            row = model.getSortOrder()[row];
        }
        UsgsNwisSiteTimeSeriesMetadata ts = (UsgsNwisSiteTimeSeriesMetadata)model.getData().get(row);
        int numCommandsAdded = this.tstoolJFrame.queryResultsList_AppendTSIDToCommandList (
            (String)query_TableModel.getValueAt ( row, model.COL_ID),
            (String)query_TableModel.getValueAt ( row, model.COL_DATA_SOURCE),
            ts.formatDataTypeForTSID(),
            (String)query_TableModel.getValueAt ( row, model.COL_TIME_STEP),
            null, // No scenario.
            null, // No sequence number.
            (String)query_TableModel.getValueAt ( row, model.COL_DATA_STORE_NAME),
            "", "",
            useAlias, insertOffset );
        return numCommandsAdded;
    }

	/**
	 * Transfer one time series catalog row to a command, for groundwater time series.
	 * @param row the time series catalog row to transfer (0+)
	 * @param useAlias whether to use an alias
	 */
    public int transferOneTimeSeriesCatalogRowToCommands_Groundwater ( int row, boolean useAlias, int insertOffset ) {
        // The location (id), type, and time step uniquely identify the time series,
    	// but the input_name is needed to indicate the database.
		JWorksheet_AbstractRowTableModel query_TableModel = this.tstoolJFrame.ui_GetTimeSeriesCatalogTableModel();
        TSTool_UsgsNwisGroundwater_TableModel model = (TSTool_UsgsNwisGroundwater_TableModel)query_TableModel;
        if (model.getSortOrder() != null) {
            row = model.getSortOrder()[row];
        }
        UsgsNwisSiteTimeSeriesMetadata ts = (UsgsNwisSiteTimeSeriesMetadata)model.getData().get(row);
        int numCommandsAdded = this.tstoolJFrame.queryResultsList_AppendTSIDToCommandList (
            (String)query_TableModel.getValueAt ( row, model.COL_ID),
            (String)query_TableModel.getValueAt ( row, model.COL_DATA_SOURCE),
            ts.formatDataTypeForTSID(),
            (String)query_TableModel.getValueAt ( row, model.COL_TIME_STEP),
            null, // No scenario.
            null, // No sequence number.
            (String)query_TableModel.getValueAt ( row, model.COL_DATA_STORE_NAME),
            "", "",
            useAlias, insertOffset );
        return numCommandsAdded;
    }

	/**
	 * Transfer one time series catalog row to a command, for groundwater time series.
	 * @param row the time series catalog row to transfer (0+)
	 * @param useAlias whether to use an alias
	 */
    public int transferOneTimeSeriesCatalogRowToCommands_Instantaneous ( int row, boolean useAlias, int insertOffset ) {
        // The location (id), type, and time step uniquely identify the time series,
    	// but the input_name is needed to indicate the database.
		JWorksheet_AbstractRowTableModel query_TableModel = this.tstoolJFrame.ui_GetTimeSeriesCatalogTableModel();
        TSTool_UsgsNwisInstantaneous_TableModel model = (TSTool_UsgsNwisInstantaneous_TableModel)query_TableModel;
        if (model.getSortOrder() != null) {
            row = model.getSortOrder()[row];
        }
        UsgsNwisSiteTimeSeriesMetadata ts = (UsgsNwisSiteTimeSeriesMetadata)model.getData().get(row);
        int numCommandsAdded = this.tstoolJFrame.queryResultsList_AppendTSIDToCommandList (
            (String)query_TableModel.getValueAt ( row, model.COL_ID),
            (String)query_TableModel.getValueAt ( row, model.COL_DATA_SOURCE),
            ts.formatDataTypeForTSID(),
            (String)query_TableModel.getValueAt ( row, model.COL_TIME_STEP),
            null, // No scenario.
            null, // No sequence number.
            (String)query_TableModel.getValueAt ( row, model.COL_DATA_STORE_NAME),
            "", "",
            useAlias, insertOffset );
        return numCommandsAdded;
    }

	/**
	 * Transfer one time series catalog row to a command, for RDB time series.
	 * @param row the time series catalog row to transfer (0+)
	 * @param useAlias whether to use an alias
	 */
    public int transferOneTimeSeriesCatalogRowToCommands_Rdb ( int row, boolean useAlias, int insertOffset ) {
		// The location (id), type, and time step uniquely identify the time series.
		JWorksheet_AbstractRowTableModel query_TableModel = this.tstoolJFrame.ui_GetTimeSeriesCatalogTableModel();
		TSTool_TS_TableModel model = (TSTool_TS_TableModel)query_TableModel;
		String seqnum = (String)query_TableModel.getValueAt( row, model.COL_SEQUENCE );
		if ( seqnum.length() == 0 ) {
			seqnum = null;
		}
		int numCommandsAdded = this.tstoolJFrame.queryResultsList_AppendTSIDToCommandList (
		(String)query_TableModel.getValueAt( row, model.COL_ID ),
		(String)query_TableModel.getValueAt( row, model.COL_DATA_SOURCE),
		(String)query_TableModel.getValueAt( row, model.COL_DATA_TYPE),
		(String)query_TableModel.getValueAt( row, model.COL_TIME_STEP ),
		(String)query_TableModel.getValueAt( row, model.COL_SCENARIO ), seqnum, // Optional sequence number.
		(String)query_TableModel.getValueAt( row, model.COL_INPUT_TYPE),
		(String)query_TableModel.getValueAt( row, model.COL_INPUT_NAME), "", false, insertOffset );
		return numCommandsAdded;
    }

}