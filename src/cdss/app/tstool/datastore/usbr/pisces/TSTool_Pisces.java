// TSTool_Pisces - integration between TSTool UI and Reclamation Pisces database datastore

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

package cdss.app.tstool.datastore.usbr.pisces;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import DWR.DMI.tstool.TSTool_JFrame;
import RTi.DMI.DMI;
import RTi.Util.GUI.InputFilter_JPanel;
import RTi.Util.GUI.JGUIUtil;
import RTi.Util.GUI.JWorksheet;
import RTi.Util.GUI.JWorksheet_AbstractRowTableModel;
import RTi.Util.GUI.SimpleJComboBox;
import RTi.Util.Message.Message;
import RTi.Util.Time.TimeInterval;
import riverside.datastore.DataStore;
import rti.tscommandprocessor.commands.reclamationpisces.ReclamationPiscesDMI;
import rti.tscommandprocessor.commands.reclamationpisces.ReclamationPiscesDataStore;
import rti.tscommandprocessor.commands.reclamationpisces.ReclamationPisces_Ref_Parameter;
import rti.tscommandprocessor.commands.reclamationpisces.ReclamationPisces_SiteCatalogSeriesCatalog;
import rti.tscommandprocessor.commands.reclamationpisces.ReclamationPisces_TimeSeries_InputFilter_JPanel;
import rti.tscommandprocessor.commands.reclamationpisces.ui.TSTool_ReclamationPisces_CellRenderer;
import rti.tscommandprocessor.commands.reclamationpisces.ui.TSTool_ReclamationPisces_TableModel;

/**
 * This class provides integration between TSTool and the US Bureau of Reclamation (USBR) Pisces database datastore.
 * This datastore is currently built into TSTool, rather than being a plugin.
 * Isolating the code simplifies maintenance.
 * The class is a singleton.  Use getInstance() to get the instance to call methods.
 */
public class TSTool_Pisces {

	/**
	 * Singleton instance of this class.
	 * Use getInstance() to return the singleton.
	 */
	private static TSTool_Pisces instance = null;
	
	/**
	 * Instance of the TSTool main UI.
	 */
	private TSTool_JFrame tstoolJFrame = null;
	
	/**
	 * Private constructor for lazy initialization.
	 */
	private TSTool_Pisces ( TSTool_JFrame tstoolJFrame ) {
		this.tstoolJFrame = tstoolJFrame;
	}

	/**
	 * Handle data type selection for daily datastore.
	 * @param dataStore selected datastore
	 * @param selectedDataType selected data type
	 */
	public void dataTypeSelected ( ReclamationPiscesDataStore dataStore, String selectedDataType ) {
        // Set intervals for the data type and trigger a select to populate the input filters.
    	ReclamationPiscesDMI dmi = (ReclamationPiscesDMI)dataStore.getDMI();
    	
    	SimpleJComboBox timeStep_JComboBox = this.tstoolJFrame.ui_GetTimeStepJComboBox();
        timeStep_JComboBox.removeAll ();
        timeStep_JComboBox.setEnabled ( true );
        timeStep_JComboBox.setData ( dmi.getDataIntervalStringsForParameter(selectedDataType));
        timeStep_JComboBox.select ( null );
        if ( timeStep_JComboBox.getItemCount() > 0 ) {
        	timeStep_JComboBox.select ( 0 );
        }
	}

	/**
	 * Get the singleton instance.
	 */
	public static TSTool_Pisces getInstance ( TSTool_JFrame tstoolJFrame ) {
		if ( TSTool_Pisces.instance == null ) {
			TSTool_Pisces.instance = new TSTool_Pisces( tstoolJFrame );
		}
		return TSTool_Pisces.instance;
	}

	/**
	Initialize the Reclamation Pisces input filter (may be called at startup).
	@param dataStoreList the list of datastores for which input filter panels are to be added.
	@param y the position in the input panel that the filter should be added
	*/
	public void initGUIInputFilters ( List<DataStore> dataStoreList, int y ) {
	    String routine = getClass().getSimpleName() + "initGUIInputFilters";
	    Message.printStatus ( 2, routine, "Initializing input filter(s) for " + dataStoreList.size() + " ReclamationPisces datastores." );
	    String selectedDataType = this.tstoolJFrame.ui_GetSelectedDataType();
	    String selectedTimeStep = this.tstoolJFrame.ui_GetSelectedTimeStep();
	    boolean repaint = false;
	    JPanel queryInput_JPanel = this.tstoolJFrame.ui_GetQueryInputJPanel();
	    List<InputFilter_JPanel> inputFilterJPanelList = this.tstoolJFrame.ui_GetInputFilterJPanelList();
	    for ( DataStore dataStore: dataStoreList ) {
	    	try {
	    		// Try to find an existing input filter panel for the same name.
	    		// The following will return null if the first time setup.
	    		JPanel ifp = this.tstoolJFrame.ui_GetInputFilterPanelForDataStoreName (
	    				dataStore.getName(), selectedDataType, selectedTimeStep );
	    		if ( ifp != null ) {
	    			ReclamationPisces_TimeSeries_InputFilter_JPanel rifp = (ReclamationPisces_TimeSeries_InputFilter_JPanel)ifp;
	    			// Get the DMI instances to check.
	    			DMI dmi = ((ReclamationPiscesDataStore)dataStore).getDMI();
	    			if ( (dataStore == rifp.getDataStore()) && (dmi == rifp.getDataStore().getDMI()) ) {
	    				// If the found input filter panel datastore and DMI is the same as the iterator instance it has not changed - leave it.
	    				Message.printStatus(2, routine, "New datastore is same as previous - not resetting filter ReclamationPisces panel.");
	    				continue;
	    			}
	    			else {
	    				// Else if the previous instance is not null and is new, remove the old one from the list because it has been replaced.
	    				Message.printStatus(2, routine, "New datastore name is same as previous but datastore differs - removing old filter panel to replace.");
	    				Message.printStatus(2, routine, "Filter panel list size before removing=" + inputFilterJPanelList.size() );
	    				inputFilterJPanelList.remove ( ifp );
	    				Message.printStatus(2, routine, "Filter panel list size after removing=" + inputFilterJPanelList.size() );
	    				// Also remove from the component.
	    				Component [] comps = queryInput_JPanel.getComponents();
	    				Message.printStatus(2, routine, "Panel component list size before removing=" + comps.length );
	    				String oldName = "ReclamationPisces.InputFilterPanel." + dataStore.getName();
	    				for ( int i = 0; i < comps.length; i++ ) {
	    					Message.printStatus(2, routine, "Comparing panel component [" + i + "] \"" + comps[i].getName() + "\" with old name \"" + oldName + "\"" );
	    					if ( (comps[i].getName() != null) && comps[i].getName().equalsIgnoreCase(oldName) ) {
	    						Message.printStatus(2, routine, "Removing panel component [" + i + "] \"" + comps[i].getName() + "\"" );
	    						queryInput_JPanel.remove(comps[i]);
	    						repaint = true;
	    						break;
	    					}
	    				}
	    				Message.printStatus(2, routine, "Panel component list size after removing=" + queryInput_JPanel.getComponents().length );
	    			}
	    		}
	    		else {
	    			Message.printStatus(2, routine, "No previous datastore found with name \"" + dataStore.getName() + "\"");
	    		}
	    		// If here, create a new panel because datastore and/or DMI have changed and query results may also be different for filters.
	    		ReclamationPisces_TimeSeries_InputFilter_JPanel newIfp =
	    				new ReclamationPisces_TimeSeries_InputFilter_JPanel((ReclamationPiscesDataStore)dataStore, 4);

	    		// Add the new panel to the layout and set in the global data.
	    		int buffer = 3;
	    		Insets insets = new Insets(0,buffer,0,0);
	    		JGUIUtil.addComponent(queryInput_JPanel, newIfp,
	    				0, y, 3, 1, 1.0, 0.0, insets, GridBagConstraints.HORIZONTAL,
	    				GridBagConstraints.WEST );
	    		newIfp.setName("ReclamationPisces.InputFilterPanel." + dataStore.getName());
	    		inputFilterJPanelList.add ( newIfp );
	    		if ( repaint ) {
	    			queryInput_JPanel.validate();
	    			queryInput_JPanel.repaint();
	    		}
	    	}
	    	catch ( Exception e ) {
	    		Message.printWarning ( 2, routine,
	    				"Unable to initialize input filter for Reclamation Pisces time series " +
	    						"for datastore \"" + dataStore.getName() + "\" (" + e + ")." );
	    		Message.printWarning ( 2, routine, e );
	    	}
	    }
	}

	/**
	Read ReclamationPisces time series and list in the GUI.
	*/
	public void getTimeSeriesListClicked_ReadReclamationPiscesCatalog() {
	    String rtn = getClass().getSimpleName() + ".getTimeSeriesListClicked_ReadReclamationPiscesCatalog";
	    JGUIUtil.setWaitCursor ( this.tstoolJFrame, true );
	    Message.printStatus ( 1, rtn, "Please wait... retrieving data");

	    DataStore dataStore = this.tstoolJFrame.ui_GetSelectedDataStore ();
	    // The headers are a list Pisces Metadata.
	    try {
	    	ReclamationPiscesDataStore ds = (ReclamationPiscesDataStore)dataStore;
	    	ReclamationPiscesDMI dmi = (ReclamationPiscesDMI)ds.getDMI();
	    	// Check the connection in case the connection timed out.
	    	ds.checkDatabaseConnection();
	    	this.tstoolJFrame.queryResultsList_Clear ();

	    	String dataType = this.tstoolJFrame.ui_GetSelectedDataType(); // Parameter
	    	String timeStep = this.tstoolJFrame.ui_GetSelectedTimeStep();
	    	if ( timeStep == null ) {
	    		Message.printWarning ( 1, rtn, "No time series are available for timestep." );
	    		JGUIUtil.setWaitCursor ( this.tstoolJFrame, false );
	    		return;
	    	}
	    	else {
	    		timeStep = timeStep.trim();
	    	}

	    	List<ReclamationPisces_SiteCatalogSeriesCatalog> results = null;
	    	// Data type is shown without name so use full choice.
	    	try {
	    		results = dmi.readSiteCatalogSeriesCatalogList(null, null, dataType, timeStep,
	    				this.tstoolJFrame.ui_GetSelectedInputFilterJPanel());
	    	}
	    	catch ( Exception e ) {
	    		results = null;
	    	}

	    	int size = 0;
	    	if ( results != null ) {
	    		size = results.size();
	    		// TODO Does not work??
	    		//__query_TableModel.setNewData ( results );
	    		// Try brute force.
	    		JWorksheet_AbstractRowTableModel query_TableModel = new TSTool_ReclamationPisces_TableModel ( ds, results );
	    		this.tstoolJFrame.ui_SetTimeSeriesCatalogTableModel ( query_TableModel );
	    		TSTool_ReclamationPisces_CellRenderer cr =
	    				new TSTool_ReclamationPisces_CellRenderer( (TSTool_ReclamationPisces_TableModel)query_TableModel);

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
	Refresh the query choices for the currently selected ReclamationPisces datastore.
	*/
	public void selectDataStore ( ReclamationPiscesDataStore selectedDataStore )
			throws Exception {
		//String routine = getClass().getSimpleName() + "uiAction_SelectDataStore_ReclamationPisces";
		// Get the DMI instance for the matching datastore.
		ReclamationPiscesDataStore ds = (ReclamationPiscesDataStore)selectedDataStore;
		ReclamationPiscesDMI dmi = (ReclamationPiscesDMI)ds.getDMI();
		// Check the connection in case the connection timed out.
		ds.checkDatabaseConnection();
		this.tstoolJFrame.ui_SetInputNameVisible(false); // Not needed for Pisces.
		
		SimpleJComboBox dataType_JComboBox = this.tstoolJFrame.ui_GetDataTypeJComboBox();
		dataType_JComboBox.removeAll ();
		// Get the list of valid object/data types from the database.
		List<ReclamationPisces_Ref_Parameter> parameters = dmi.getParameterList();
		// Add a wildcard option to get all data types.
		List<String> dataTypes = new ArrayList<>();
		dataTypes.add(0,"*");
		for ( ReclamationPisces_Ref_Parameter p : parameters ) {
			dataTypes.add(p.getID());
		}
		dataType_JComboBox.setData ( dataTypes );
		dataType_JComboBox.select ( 0 );
		dataType_JComboBox.setEnabled ( true );

		// Set the initial timestep as "*" and refresh the list once a data type selection is made.
		SimpleJComboBox timeStep_JComboBox = this.tstoolJFrame.ui_GetTimeStepJComboBox();
		timeStep_JComboBox.removeAll ();
		timeStep_JComboBox.add ( "*" );
		timeStep_JComboBox.add ( TimeInterval.getName(TimeInterval.HOUR, 0));
		timeStep_JComboBox.add ( TimeInterval.getName(TimeInterval.DAY, 0));
		// TODO smalers 2015-08-26 Add week when enabled in TSTool.
		//__timeStep_JComboBox.add ( TimeInterval.getName(TimeInterval.WEEK, 0));
		timeStep_JComboBox.add ( TimeInterval.getName(TimeInterval.MONTH, 0));
		timeStep_JComboBox.add ( TimeInterval.getName(TimeInterval.YEAR, 0));
		timeStep_JComboBox.add ( TimeInterval.getName(TimeInterval.IRREGULAR, 0));
		timeStep_JComboBox.select ( "*" );
		timeStep_JComboBox.setEnabled ( true );

		// Initialize with blank data list.
		JWorksheet_AbstractRowTableModel query_TableModel = new TSTool_ReclamationPisces_TableModel( ds, null);
   		this.tstoolJFrame.ui_SetTimeSeriesCatalogTableModel ( query_TableModel );
		TSTool_ReclamationPisces_CellRenderer cr =
			new TSTool_ReclamationPisces_CellRenderer((TSTool_ReclamationPisces_TableModel)query_TableModel);
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
        // identify the time series, but the input name is needed to indicate the datastore.
		JWorksheet_AbstractRowTableModel query_TableModel = this.tstoolJFrame.ui_GetTimeSeriesCatalogTableModel();
        TSTool_ReclamationPisces_TableModel model = (TSTool_ReclamationPisces_TableModel)query_TableModel;

        ReclamationPiscesDataStore ds = (ReclamationPiscesDataStore)this.tstoolJFrame.ui_GetSelectedDataStore();
        // Check the connection in case timeouts, etc.
        ds.checkDatabaseConnection();
        // Format the TSID using the older format that uses common names, but these are not guaranteed unique.
        String loc = (String)query_TableModel.getValueAt( row, model.COL_SITE_ID );
        String dataSource = (String)query_TableModel.getValueAt( row, model.COL_SERIES_SERVER);
        String dataType = (String)query_TableModel.getValueAt( row, model.COL_SERIES_PARAMETER);
        String interval = (String)query_TableModel.getValueAt( row, model.COL_SERIES_TIMEINTERVAL);
        String scenario = "";
        int numCommandsAdded = this.tstoolJFrame.queryResultsList_AppendTSIDToCommandList (
	        loc,
	        dataSource,
	        dataType,
	        interval,
	        scenario,
	        null, // No sequence number.
	        (String)query_TableModel.getValueAt( row, model.COL_DATASTORE_NAME),
	        "",
	        "", false, insertOffset );
        return numCommandsAdded;
    }

}