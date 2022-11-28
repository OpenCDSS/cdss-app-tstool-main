// TSTool_Generic - integration between TSTool UI and generic database datastore

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

/**
 * This class provides integration between TSTool and * generic database datastores.
 * These datastores are currently built into TSTool, rather than being a plugin.
 * Isolating the code simplifies maintenance.
 * The class is a singleton.  Use getInstance() to get the instance to call methods.
 */
package cdss.app.tstool.datastore.generic;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import DWR.DMI.tstool.TSTool_JFrame;
import RTi.TS.TSIdent;
import RTi.Util.GUI.InputFilter_JPanel;
import RTi.Util.GUI.JGUIUtil;
import RTi.Util.GUI.JWorksheet;
import RTi.Util.GUI.JWorksheet_AbstractRowTableModel;
import RTi.Util.GUI.SimpleJComboBox;
import RTi.Util.Message.Message;
import riverside.datastore.DataStore;
import riverside.datastore.GenericDatabaseDataStore;
import riverside.datastore.GenericDatabaseDataStore_TS_CellRenderer;
import riverside.datastore.GenericDatabaseDataStore_TS_TableModel;
import riverside.datastore.GenericDatabaseDataStore_TimeSeries_InputFilter_JPanel;
import riverside.datastore.TimeSeriesMeta;

public class TSTool_Generic {

	/**
	 * Singleton instance of this class.
	 * Use getInstance() to return the singleton.
	 */
	private static TSTool_Generic instance = null;
	
	/**
	 * Instance of the TSTool main UI.
	 */
	private TSTool_JFrame tstoolJFrame = null;

	/**
	 * Private constructor for lazy initialization.
	 */
	private TSTool_Generic ( TSTool_JFrame tstoolJFrame ) {
		this.tstoolJFrame = tstoolJFrame;
	}

	/**
	 * Get the singleton instance.
	 */
	public static TSTool_Generic getInstance ( TSTool_JFrame tstoolJFrame ) {
		if ( TSTool_Generic.instance == null ) {
			TSTool_Generic.instance = new TSTool_Generic( tstoolJFrame );
		}
		return TSTool_Generic.instance;
	}

	/**
	Read GenericDatabaseDatastore time series and list in the GUI.
	*/
	public void getTimeSeriesListClicked_ReadGenericDatabaseDataStoreCatalog ( InputFilter_JPanel selectedInputFilter_JPanel, DataStore selectedDataStore ) {
	   String rtn = getClass().getSimpleName() + ".getTimeSeriesListClicked_ReadGenericDatabaseDataStoreCatalog";
	   JGUIUtil.setWaitCursor ( this.tstoolJFrame, true );
	   Message.printStatus ( 1, rtn, "Please wait... retrieving data");

	   SimpleJComboBox dataType_JComboBox = this.tstoolJFrame.ui_GetDataTypeJComboBox();
	   SimpleJComboBox timeStep_JComboBox = this.tstoolJFrame.ui_GetTimeStepJComboBox();

	   // The headers are a list of TimeSeriesMeta.
	   try {
		   GenericDatabaseDataStore ds = (GenericDatabaseDataStore)selectedDataStore;
		   //DMI dmi = (DMI)ds.getDMI();
		   this.tstoolJFrame.queryResultsList_Clear ();
		   String dataType = dataType_JComboBox.getSelected(); // Object type - common data type.
		   String timeStep = timeStep_JComboBox.getSelected();
		   if ( timeStep == null ) {
			   Message.printWarning ( 1, rtn, "No time series are available for timestep." );
			   JGUIUtil.setWaitCursor ( this.tstoolJFrame, false );
			   return;
		   }
		   else {
			   timeStep = timeStep.trim();
		   }

		   List<TimeSeriesMeta> results = null;
		   // Data type is shown with name so only use the first part of the choice.
		   try {
			   results = ds.readTimeSeriesMetaList(dataType, timeStep,
					   (GenericDatabaseDataStore_TimeSeries_InputFilter_JPanel)selectedInputFilter_JPanel);
		   }
		   catch ( Exception e ) {
			   results = null;
		   }

		   int size = 0;
		   if ( results != null ) {
			   size = results.size();
			   // TODO Does not work??
			   //query_TableModel.setNewData ( results );
			   // Try brute force.
			   JWorksheet_AbstractRowTableModel query_TableModel = new GenericDatabaseDataStore_TS_TableModel ( results, ds );
			   this.tstoolJFrame.ui_SetTimeSeriesCatalogTableModel ( query_TableModel );
			   GenericDatabaseDataStore_TS_CellRenderer cr =
					   new GenericDatabaseDataStore_TS_CellRenderer( (GenericDatabaseDataStore_TS_TableModel)query_TableModel);

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
	Initialize the GenericDataBaseDataStore input filter (may be called at startup).
	@param dataStoreList the list of datastores for which input filter panels are to be added.
	@param y the position in the input panel that the filter should be added
	*/
	public void initGUIInputFilters ( List<DataStore> dataStoreList, int y ) {
		String routine = getClass().getSimpleName() + "initGUIInputFilters";
		Message.printStatus ( 2, routine, "Initializing input filter(s) for " + dataStoreList.size() +
				" GenericDatabaseDataStore datastores." );
		String selectedDataType = this.tstoolJFrame.ui_GetSelectedDataType();
		String selectedTimeStep = this.tstoolJFrame.ui_GetSelectedTimeStep();
		List<InputFilter_JPanel> inputFilterJPanelList = this.tstoolJFrame.ui_GetInputFilterJPanelList();
		JPanel queryInput_JPanel = this.tstoolJFrame.ui_GetQueryInputJPanel();
		for ( DataStore dataStore: dataStoreList ) {
			try {
				// Only handle database datastores (not web datastores.
				if ( dataStore instanceof GenericDatabaseDataStore ) {
					// Only add an input filter if the datastore properies are defined to indicate time series data.
					GenericDatabaseDataStore ds = (GenericDatabaseDataStore)dataStore;
					if ( ds.hasTimeSeriesInterface(true) ) {
						// Try to find an existing input filter panel for the same name.
						JPanel ifp = this.tstoolJFrame.ui_GetInputFilterPanelForDataStoreName ( dataStore.getName(), selectedDataType, selectedTimeStep );
						// If the previous instance is not null, remove it from the list.
						if ( ifp != null ) {
							inputFilterJPanelList.remove ( ifp );
						}
						// Create a new panel.
						GenericDatabaseDataStore_TimeSeries_InputFilter_JPanel newIfp =
								new GenericDatabaseDataStore_TimeSeries_InputFilter_JPanel((GenericDatabaseDataStore)dataStore, 3);

						// Add the new panel to the layout and set in the global data.
						int buffer = 3;
						Insets insets = new Insets(0,buffer,0,0);
						JGUIUtil.addComponent(queryInput_JPanel, newIfp,
								0, y, 3, 1, 1.0, 0.0, insets, GridBagConstraints.HORIZONTAL,
								GridBagConstraints.WEST );
						newIfp.setName("GenericDatabaseDataStore." + dataStore.getName());
						inputFilterJPanelList.add ( newIfp );
					}
				}
			}
			catch ( Exception e ) {
				Message.printWarning ( 2, routine,
						"Unable to initialize input filter for GenericDatabaseDataStore time series " +
								"for datastore \"" + dataStore.getName() + "\" (" + e + ")." );
				Message.printWarning ( 2, routine, e );
			}
		}
	}

	/**
	Refresh the query choices for the currently selected generic database datastore.
	*/
	public void selectDataStore ( GenericDatabaseDataStore ds )
	throws Exception {
		String routine = getClass().getSimpleName() + "selectDataStore";
		
		SimpleJComboBox dataType_JComboBox = this.tstoolJFrame.ui_GetDataTypeJComboBox();
		SimpleJComboBox timeStep_JComboBox = this.tstoolJFrame.ui_GetTimeStepJComboBox();

		this.tstoolJFrame.ui_SetInputNameVisible(false); // Not needed for HDB.
		dataType_JComboBox.removeAll ();
		// Get the list of valid object/data types from the database.
		List<String> dataTypes = new ArrayList<>();
		try {
			dataTypes = ds.getTimeSeriesMetaDataTypeList ( false, null, null, null, null, null );
			// Also add "*" to get all types, for example, when location type is selected.
			dataTypes.add ( "*" );
		}
		catch ( Exception e ) {
			Message.printWarning(3, routine, e);
		}
		dataType_JComboBox.setData ( dataTypes );
		if ( dataType_JComboBox.getItemCount() > 0 ) {
			dataType_JComboBox.select(0);
		}
		dataType_JComboBox.setEnabled ( true );

		// Get the list of timesteps that are valid for the data type.
		// Need to trigger a select to populate the input filters.
		timeStep_JComboBox.removeAll ();
		List<String> timeSteps = new ArrayList<>();
		try {
			if ( dataTypes.size() > 0 ) {
				timeSteps = ds.readTimeSeriesMetaIntervalList ( null, null, null, dataTypes.get(0), null );
			}
		}
		catch ( Exception e ) {
			Message.printWarning(3, routine, e);
		}
		timeStep_JComboBox.setData(timeSteps);
		if ( timeStep_JComboBox.getItemCount() > 0 ) {
			timeStep_JComboBox.select ( 0 );
		}
		timeStep_JComboBox.setEnabled ( true );

		// Initialize results with null list.
		JWorksheet_AbstractRowTableModel query_TableModel = new GenericDatabaseDataStore_TS_TableModel( null, ds );
	    this.tstoolJFrame.ui_SetTimeSeriesCatalogTableModel ( query_TableModel );
		GenericDatabaseDataStore_TS_CellRenderer cr =
				new GenericDatabaseDataStore_TS_CellRenderer((GenericDatabaseDataStore_TS_TableModel)query_TableModel);

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
    public int transferOneTimeSeriesCatalogRowToCommands ( int row, boolean useAlias, int insertOffset ) {
        // TODO smalers 2013-08-27 Try this model but need to deal with location type.
		JWorksheet_AbstractRowTableModel query_TableModel = this.tstoolJFrame.ui_GetTimeSeriesCatalogTableModel();
        GenericDatabaseDataStore_TS_TableModel model = (GenericDatabaseDataStore_TS_TableModel)query_TableModel;
        String locType = (String)query_TableModel.getValueAt( row, model.COL_LOC_TYPE );
        if ( !locType.equals("") ) {
            // Add the separator.
            locType += TSIdent.LOC_TYPE_SEPARATOR;
        }
        int numCommandsAdded = this.tstoolJFrame.queryResultsList_AppendTSIDToCommandList (
            locType + (String)query_TableModel.getValueAt( row, model.COL_ID ),
            (String)query_TableModel.getValueAt ( row, model.COL_DATA_SOURCE),
            (String)query_TableModel.getValueAt ( row, model.COL_DATA_TYPE),
            (String)query_TableModel.getValueAt ( row, model.COL_TIME_STEP),
            (String)query_TableModel.getValueAt ( row, model.COL_SCENARIO),
            null, // No sequence number.
            (String)query_TableModel.getValueAt( row, model.COL_INPUT_TYPE),
            "", // No input name.
            "",
            false, insertOffset );
        return numCommandsAdded;
    }

}