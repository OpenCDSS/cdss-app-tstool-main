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

package cdss.app.tstool.datastore.rcc.acis;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.List;

import javax.swing.JPanel;

import DWR.DMI.tstool.TSTool_JFrame;
import RTi.Util.GUI.InputFilter_JPanel;
import RTi.Util.GUI.JGUIUtil;
import RTi.Util.GUI.JWorksheet;
import RTi.Util.GUI.JWorksheet_AbstractRowTableModel;
import RTi.Util.GUI.SimpleJComboBox;
import RTi.Util.Message.Message;
import riverside.datastore.DataStore;
import rti.tscommandprocessor.commands.rccacis.RccAcisDataStore;
import rti.tscommandprocessor.commands.rccacis.RccAcisStationTimeSeriesMetadata;
import rti.tscommandprocessor.commands.rccacis.RccAcis_TimeSeries_InputFilter_JPanel;
import rti.tscommandprocessor.commands.rccacis.ui.TSTool_RccAcis_CellRenderer;
import rti.tscommandprocessor.commands.rccacis.ui.TSTool_RccAcis_TableModel;

/**
 * This class provides integration between TSTool and the
 * Regional Climate Center (RCC) Applied Climate Information System (ACIS) web service datastore.
 * This datastore is currently built into TSTool, rather than being a plugin.
 * Isolating the code simplifies maintenance.
 * The class is a singleton.  Use getInstance() to get the instance to call methods.
 */
public class TSTool_RccAcis {

	/**
	 * Singleton instance of this class.
	 * Use getInstance() to return the singleton.
	 */
	private static TSTool_RccAcis instance = null;
	
	/**
	 * Instance of the TSTool main UI.
	 */
	private TSTool_JFrame tstoolJFrame = null;
	
	/**
	 * Private constructor for lazy initialization.
	 */
	private TSTool_RccAcis ( TSTool_JFrame tstoolJFrame ) {
		this.tstoolJFrame = tstoolJFrame;
	}
	
	/**
	 * Handle the data type selection.
	 */
	public void dataTypeSelected () {
        // Set intervals for the data type and trigger a select to populate the input filters.
        RccAcisDataStore dataStore = (RccAcisDataStore)this.tstoolJFrame.ui_GetSelectedDataStore();
        SimpleJComboBox timeStep_JComboBox = this.tstoolJFrame.ui_GetTimeStepJComboBox();
        timeStep_JComboBox.removeAll ();
        timeStep_JComboBox.setEnabled ( true );
        timeStep_JComboBox.setData ( dataStore.getDataIntervalStringsForDataType(this.tstoolJFrame.ui_GetSelectedDataType()));
        timeStep_JComboBox.select ( null );
        timeStep_JComboBox.select ( 0 );
	}

	/**
	 * Get the singleton instance.
	 */
	public static TSTool_RccAcis getInstance ( TSTool_JFrame tstoolJFrame ) {
		if ( TSTool_RccAcis.instance == null ) {
			TSTool_RccAcis.instance = new TSTool_RccAcis( tstoolJFrame );
		}
		return TSTool_RccAcis.instance;
	}

	/**
	Initialize the RCC ACIS input filter (may be called at startup).
	@param dataStoreList the list of datastores for which input filter panels are to be added.
	@param y the position in the input panel that the filter should be added
	*/
	public void initGUIInputFilters ( List<DataStore> dataStoreList, int y ) {
	    String routine = getClass().getSimpleName() + ".initGUIInputFilters";
	    Message.printStatus ( 2, routine, "Initializing input filter(s) for " + dataStoreList.size() + " RCC ACIS datastores." );
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
	    		RccAcis_TimeSeries_InputFilter_JPanel newIfp =
	    				new RccAcis_TimeSeries_InputFilter_JPanel((RccAcisDataStore)dataStore, 3);

	    		// Add the new panel to the layout and set in the global data.
	    		int buffer = 3;
	    		Insets insets = new Insets(0,buffer,0,0);
	    		JGUIUtil.addComponent(this.tstoolJFrame.ui_GetQueryInputJPanel(), newIfp,
	    				0, y, 3, 1, 1.0, 0.0, insets, GridBagConstraints.HORIZONTAL,
	    				GridBagConstraints.WEST );
	    		newIfp.setName("RccAcis.InputFilterPanel");
	    		this.tstoolJFrame.ui_GetInputFilterJPanelList().add ( newIfp );
	    	}
	    	catch ( Exception e ) {
	    		Message.printWarning ( 2, routine,
	    				"Unable to initialize input filter for RCC ACIS time series " +
	    						"for datastore \"" + dataStore.getName() + "\" (" + e + ")." );
	    		Message.printWarning ( 2, routine, e );
	    	}
	    }
	}

	/**
	Read RCC ACIS time series and list in the GUI.
	*/
	public void getTimeSeriesListClicked_ReadRccAcisCatalog( InputFilter_JPanel selectedInputFilter_JPanel) {
	    String rtn = getClass().getSimpleName() + ".getTimeSeriesListClicked_ReadRccAcisCatalog";
	    JGUIUtil.setWaitCursor ( this.tstoolJFrame, true );
	    Message.printStatus ( 1, rtn, "Please wait... retrieving data");

	    DataStore dataStore = this.tstoolJFrame.ui_GetSelectedDataStore ();
	    // The headers are a list of RccAcisTimeSeriesMetadata.
	    try {
	    	RccAcisDataStore rccAcisDataStore = (RccAcisDataStore)dataStore;
	    	this.tstoolJFrame.queryResultsList_Clear ();

	    	String dataType = this.tstoolJFrame.ui_GetSelectedDataType();
	    	String timeStep = this.tstoolJFrame.ui_GetSelectedTimeStep();
	    	if ( timeStep == null ) {
	    		Message.printWarning ( 1, rtn, "No time series are available for timestep." );
	    		JGUIUtil.setWaitCursor ( this.tstoolJFrame, false );
	    		return;
	    	}
	    	else {
	    		timeStep = timeStep.trim();
	    	}

	    	List<RccAcisStationTimeSeriesMetadata> results = null;
	    	// Data type is shown with name so only use the first part of the choice.
	    	try {
	    		results = rccAcisDataStore.readStationTimeSeriesMetadataList(dataType, timeStep, selectedInputFilter_JPanel);
	    	}
	    	catch ( Exception e ) {
	    		Message.printWarning(1, rtn, "Error getting time series list from ACIS (" + e + ").");
	    		Message.printWarning(3, rtn, e );
	    		results = null;
	    	}

	    	int size = 0;
	    	if ( results != null ) {
	    		size = results.size();
	    		// TODO Does not work??
	    		//__query_TableModel.setNewData ( results );
	    		// Try brute force.
	    		JWorksheet_AbstractRowTableModel query_TableModel = new TSTool_RccAcis_TableModel ( rccAcisDataStore, results );
	    		this.tstoolJFrame.ui_SetTimeSeriesCatalogTableModel ( query_TableModel );
	    		TSTool_RccAcis_CellRenderer cr = new TSTool_RccAcis_CellRenderer( (TSTool_RccAcis_TableModel)query_TableModel);

	    		JWorksheet query_JWorksheet = tstoolJFrame.ui_GetTimeSeriesCatalogWorksheet();
	    		query_JWorksheet.setCellRenderer ( cr );
	    		query_JWorksheet.setModel ( query_TableModel );
	    		query_JWorksheet.setColumnWidths ( cr.getColumnWidths(), this.tstoolJFrame.getGraphics() );
	    	}
	    	if ( (results == null) || (size == 0) ) {
	    		Message.printStatus ( 1, rtn, "Query complete.  No records returned." );
	    	}
	    	else {
	    		Message.printStatus ( 1, rtn, "Query complete. " + size + " records returned." );
	    	}
	    	this.tstoolJFrame.ui_UpdateStatus ( false );
	    	JGUIUtil.setWaitCursor ( this.tstoolJFrame, false );
	    }
	    catch ( Exception e ) {
	    	// Messages elsewhere but catch so we can get the cursor back.
	    	Message.printWarning ( 3, rtn, e );
	    	JGUIUtil.setWaitCursor ( this.tstoolJFrame, false );
	    }
	}

	/**
	Refresh the query choices for the currently selected RCC ACIS store.
	*/
	public void selectDataStore ( RccAcisDataStore selectedDataStore )
	throws Exception {
	    //String routine = getClass().getSimpleName() + "uiAction_SelectDataStore_RccAcis";
		RccAcisDataStore dataStore = (RccAcisDataStore)selectedDataStore;
		this.tstoolJFrame.ui_SetInputNameVisible(false); // Not needed for datastores.
		// Get the list of valid object/data types from the datastore.
		List<String> dataTypes = dataStore.getDataTypeStrings ( true, true );

		// Populate the list of available data types and select the first.
		SimpleJComboBox dataType_JComboBox = this.tstoolJFrame.ui_GetDataTypeJComboBox();
		dataType_JComboBox.setEnabled ( true );
		dataType_JComboBox.removeAll ();
		dataType_JComboBox.setData ( dataTypes );
		dataType_JComboBox.select ( null );
		dataType_JComboBox.select ( 0 );

		// Initialize the time series list with blank data list.
		JWorksheet_AbstractRowTableModel query_TableModel = new TSTool_RccAcis_TableModel( dataStore, null);
   		this.tstoolJFrame.ui_SetTimeSeriesCatalogTableModel ( query_TableModel );
		TSTool_RccAcis_CellRenderer cr = new TSTool_RccAcis_CellRenderer((TSTool_RccAcis_TableModel)query_TableModel);

   		JWorksheet query_JWorksheet = tstoolJFrame.ui_GetTimeSeriesCatalogWorksheet();
		query_JWorksheet.setCellRenderer ( cr );
		query_JWorksheet.setModel ( query_TableModel );
		query_JWorksheet.setColumnWidths ( cr.getColumnWidths() );
	}

	/**
	 * Transfer one time series catalog row to a command, for daily time series.
	 * @param row the time series catalog row to transfer (0+)
	 * @param useAlias whether to use an alias
	 */
    public int transferOneTimeSeriesCatalogRowToCommands_Daily ( int row, boolean useAlias, int insertOffset ) {
        // The location (id), type, and time step uniquely
        // identify the time series, but the input_name is needed to indicate the database.
        RccAcisDataStore rccAcisDataStore = (RccAcisDataStore)this.tstoolJFrame.ui_GetSelectedDataStore();
		JWorksheet_AbstractRowTableModel query_TableModel = this.tstoolJFrame.ui_GetTimeSeriesCatalogTableModel();
        TSTool_RccAcis_TableModel model = (TSTool_RccAcis_TableModel)query_TableModel;
        if (model.getSortOrder() != null) {
            row = model.getSortOrder()[row];
        }
        RccAcisStationTimeSeriesMetadata ts = (RccAcisStationTimeSeriesMetadata)model.getData().get(row);
        String dataType = "" + query_TableModel.getValueAt ( row, model.COL_DATA_TYPE_ELEM);
        if ( rccAcisDataStore.getAPIVersion() == 1 ) {
            dataType = "" + query_TableModel.getValueAt ( row, model.COL_DATA_TYPE_MAJOR);
        }
        int numCommandsAdded = this.tstoolJFrame.queryResultsList_AppendTSIDToCommandList (
            ts.getIDPreferred(true),
            "ACIS", //(String)__query_TableModel.getValueAt ( row, model.COL_DATA_SOURCE),
            //(String)__query_TableModel.getValueAt ( row, model.COL_DATA_TYPE_MAJOR),
            dataType,
            (String)query_TableModel.getValueAt ( row, model.COL_TIME_STEP),
            null, // No scenario.
            null, // No sequence number.
            (String)query_TableModel.getValueAt ( row, model.COL_DATA_STORE_NAME),
            "", "",
            useAlias, insertOffset );
        return numCommandsAdded;
    }

}