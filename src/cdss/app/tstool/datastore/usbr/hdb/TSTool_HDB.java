// TSTool_HDB - integration between TSTool UI and Reclamation HDB database

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

package cdss.app.tstool.datastore.usbr.hdb;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JPanel;

import DWR.DMI.tstool.TSToolConstants;
import DWR.DMI.tstool.TSTool_JFrame;
import RTi.DMI.DMI;
import RTi.Util.GUI.InputFilter_JPanel;
import RTi.Util.GUI.JGUIUtil;
import RTi.Util.GUI.JWorksheet;
import RTi.Util.GUI.JWorksheet_AbstractRowTableModel;
import RTi.Util.GUI.SimpleJComboBox;
import RTi.Util.Message.Message;
import riverside.datastore.DataStore;
import rti.tscommandprocessor.commands.reclamationhdb.ReclamationHDBDataStore;
import rti.tscommandprocessor.commands.reclamationhdb.ReclamationHDBDataStoreFactory;
import rti.tscommandprocessor.commands.reclamationhdb.ReclamationHDB_DMI;
import rti.tscommandprocessor.commands.reclamationhdb.ReclamationHDB_SiteTimeSeriesMetadata;
import rti.tscommandprocessor.commands.reclamationhdb.ReclamationHDB_TimeSeries_InputFilter_JPanel;
import rti.tscommandprocessor.commands.reclamationhdb.ui.TSTool_ReclamationHDB_CellRenderer;
import rti.tscommandprocessor.commands.reclamationhdb.ui.TSTool_ReclamationHDB_TableModel;
import rti.tscommandprocessor.core.TSCommandProcessor;

/**
 * This class provides integration between TSTool and Reclamation HDB database.
 * These datastores are currently built into TSTool, rather than being a plugin.
 * Isolating the code simplifies maintenance.
 * The class is a singleton.  Use getInstance() to get the instance to call methods.
 */
public class TSTool_HDB {
	/**
	 * Singleton instance of this class.
	 * Use getInstance() to return the singleton.
	 */
	private static TSTool_HDB instance = null;
	
	/**
	 * Instance of the TSTool main UI.
	 */
	private TSTool_JFrame tstoolJFrame = null;

	/**
	 * Private constructor for lazy initialization.
	 */
	private TSTool_HDB ( TSTool_JFrame tstoolJFrame ) {
		this.tstoolJFrame = tstoolJFrame;
	}

	/**
	 * Get the singleton instance.
	 */
	public static TSTool_HDB getInstance ( TSTool_JFrame tstoolJFrame ) {
		if ( TSTool_HDB.instance == null ) {
			TSTool_HDB.instance = new TSTool_HDB( tstoolJFrame );
		}
		return TSTool_HDB.instance;
	}

	/**
	Read ReclamationHDB time series and list in the GUI.
	*/
	public void getTimeSeriesListClicked_ReadReclamationHDBCatalog() {
		String rtn = getClass().getSimpleName() + ".getTimeSeriesListClicked_ReadReclamationHDBCatalog";
		JGUIUtil.setWaitCursor ( this.tstoolJFrame, true );
		Message.printStatus ( 1, rtn, "Please wait... retrieving data");
		
		SimpleJComboBox dataType_JComboBox = this.tstoolJFrame.ui_GetDataTypeJComboBox();
		SimpleJComboBox timeStep_JComboBox = this.tstoolJFrame.ui_GetTimeStepJComboBox();
		InputFilter_JPanel selectedInputFilter_JPanel = this.tstoolJFrame.ui_GetSelectedInputFilterJPanel();

		DataStore dataStore = this.tstoolJFrame.ui_GetSelectedDataStore ();
		// The headers are a list of ReclamationHDB_SiteTimeSeriesMetadata.
		try {
			ReclamationHDBDataStore ds = (ReclamationHDBDataStore)dataStore;
			// Check the connection in case the connection timed out.
			ds.checkDatabaseConnection();
			ReclamationHDB_DMI dmi = (ReclamationHDB_DMI)ds.getDMI();
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

			List<ReclamationHDB_SiteTimeSeriesMetadata> results = null;
			if ( timeStep.equals("*") ) {
				// Read the time series for each of the major intervals and then concatenate the results.
				String [] timeSteps = {
						TSToolConstants.TIMESTEP_HOUR,
						TSToolConstants.TIMESTEP_DAY,
						TSToolConstants.TIMESTEP_MONTH,
						TSToolConstants.TIMESTEP_YEAR,
						TSToolConstants.TIMESTEP_IRREGULAR
				};
				results = new ArrayList<>();
				List<ReclamationHDB_SiteTimeSeriesMetadata> results2 = null;
				for ( int i = 0; i < timeSteps.length; i++ ) {
					try {
						results2 = dmi.readSiteTimeSeriesMetadataList(dataType, timeSteps[i], selectedInputFilter_JPanel);
						if ( results2 != null ) {
							for ( ReclamationHDB_SiteTimeSeriesMetadata result : results2 ) {
								results.add ( result );
							}
						}
					}
					catch ( Exception e ) {
						// Just skip the timestep.
					}
				}
				if ( results.size() == 0 ) {
					results = null; // Handle warning below.
				}
			}
			else {
				// Data type is shown with name so only use the first part of the choice.
				try {
					results = dmi.readSiteTimeSeriesMetadataList(dataType, timeStep, selectedInputFilter_JPanel);
				}
				catch ( Exception e ) {
					results = null;
				}
			}

			int size = 0;
			if ( results != null ) {
				size = results.size();
				// TODO Does not work??
				//__query_TableModel.setNewData ( results );
				// Try brute force.
				JWorksheet_AbstractRowTableModel query_TableModel = new TSTool_ReclamationHDB_TableModel ( ds, results );
				this.tstoolJFrame.ui_SetTimeSeriesCatalogTableModel ( query_TableModel );
				TSTool_ReclamationHDB_CellRenderer cr = new TSTool_ReclamationHDB_CellRenderer( (TSTool_ReclamationHDB_TableModel)query_TableModel);

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
	Initialize the Reclamation HDB input filter (may be called at startup).
	@param dataStoreList the list of datastores for which input filter panels are to be added.
	@param y the position in the input panel that the filter should be added
	*/
	public void initGUIInputFilters ( List<DataStore> dataStoreList, int y ) {
	    String routine = getClass().getSimpleName() + "initGUIInputFilters";
	    Message.printStatus ( 2, routine, "Initializing input filter(s) for " + dataStoreList.size() +
	    		" ReclamationHDB datastores." );

	    String selectedDataType = this.tstoolJFrame.ui_GetSelectedDataType();
	    String selectedTimeStep = this.tstoolJFrame.ui_GetSelectedTimeStep();
	    JPanel queryInput_JPanel = this.tstoolJFrame.ui_GetQueryInputJPanel();
	    List<InputFilter_JPanel> inputFilterJPanelList = this.tstoolJFrame.ui_GetInputFilterJPanelList(); 

	    boolean repaint = false;
	    for ( DataStore dataStore: dataStoreList ) {
	    	try {
	    		// Try to find an existing input filter panel for the same name.
	    		// The following will return null if the first time setup.
	    		JPanel ifp = this.tstoolJFrame.ui_GetInputFilterPanelForDataStoreName (
    				dataStore.getName(), selectedDataType, selectedTimeStep );
	    		if ( ifp != null ) {
	    			ReclamationHDB_TimeSeries_InputFilter_JPanel rifp = (ReclamationHDB_TimeSeries_InputFilter_JPanel)ifp;
	    			// Get the DMI instances to check.
	    			DMI dmi = ((ReclamationHDBDataStore)dataStore).getDMI();
	    			if ( (dataStore == rifp.getDataStore()) && (dmi == rifp.getDataStore().getDMI()) ) {
	    				// If the found input filter panel datastore and DMI is the same as the iterator instance it has not changed - leave it.
	    				Message.printStatus(2, routine, "New datastore is same as previous - not resetting filter ReclamationHDB panel.");
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
	    				String oldName = "ReclamationHDB.InputFilterPanel." + dataStore.getName();
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
	    		// In here, create a new panel because datastore and/or DMI have changed and query results may also be different for filters.
	    		ReclamationHDB_TimeSeries_InputFilter_JPanel newIfp =
	    				new ReclamationHDB_TimeSeries_InputFilter_JPanel((ReclamationHDBDataStore)dataStore, 3);

	    		// Add the new panel to the layout and set in the global data.
	    		int buffer = 3;
	    		Insets insets = new Insets(0,buffer,0,0);
	    		JGUIUtil.addComponent(queryInput_JPanel, newIfp,
	    				0, y, 3, 1, 1.0, 0.0, insets, GridBagConstraints.HORIZONTAL,
	    				GridBagConstraints.WEST );
	    		newIfp.setName("ReclamationHDB.InputFilterPanel." + dataStore.getName());
	    		inputFilterJPanelList.add ( newIfp );
	    		if ( repaint ) {
	    			queryInput_JPanel.validate();
	    			queryInput_JPanel.repaint();
	    		}
	    	}
	    	catch ( Exception e ) {
	    		Message.printWarning ( 2, routine,
	    			"Unable to initialize input filter for Reclamation HDB time series for datastore \"" +
	    				dataStore.getName() + "\" (" + e + ")." );
	    		Message.printWarning ( 2, routine, e );
	    	}
	    }
	}
	
	/**
	 * Open the database by prompting for the login.
	 */
	public void openDatabase () throws Exception {
		String routine = getClass().getSimpleName() + ".openDatabase";
		// This is used only to re-login to an existing data store.
		// The datastore with server, database, etc. must be configured in a datastore configuration file.
		ReclamationHDBDataStoreFactory factory = new ReclamationHDBDataStoreFactory();
		// Get the list of ReclamationHDB datastores that are open and send to the dialog.
		// The user will be able to pick one and then re-login.  The updated datastore will be returned.
		TSCommandProcessor processor = this.tstoolJFrame.commandProcessor_GetCommandProcessor();
		List<DataStore> dslist = processor.getDataStoresByType(ReclamationHDBDataStore.class, false); // Returns all, even non-active.
		DataStore ds = factory.openDataStoreConnectionUI(dslist, this.tstoolJFrame);
		ReclamationHDBDataStore newrds = (ReclamationHDBDataStore)ds;
		DMI newdmi = newrds.getDMI();
		if ( (newdmi != null) && newdmi.isOpen() ) {
			// New datastore login was successful.
			// Get the old matching datastore, if any, in order to close the old DMI.
			// TODO smalers 2015-03-22 Setting the processor property may do this.
			TSCommandProcessor tsProcessor = this.tstoolJFrame.commandProcessor_GetCommandProcessor();
			ReclamationHDBDataStore oldds = (ReclamationHDBDataStore)tsProcessor.getDataStoreForName ( newrds.getName(), ReclamationHDBDataStore.class );
			DMI olddmi = oldds.getDMI();
			if ( (oldds == newrds) && (olddmi == newdmi) ) {
				// Don't need to do anything - likely a cancel out of the login with no change.
				Message.printStatus(2,routine,"New ReclamationHDB datastore is same as previous - leaving as is.");
			}
			else {
				Message.printStatus(2,routine,"New ReclamationHDB datastore is different, resetting in processor and updating filter panel.");
				// Get the currently selected datastore.
				String selectedDataStoreName = this.tstoolJFrame.ui_GetDataTypeJComboBox().getSelected();
				if ( olddmi.isOpen() ) {
					try {
						olddmi.close();
					}
					catch ( Exception e ) {
						// May be a timeout, in which case just continue.
					}
				}
				// Once re-opened, reset in the command processor and refresh the UI components that depend on the datastore.
				tsProcessor.setPropContents("DataStore", newrds);
				// Reset the input panel in the user interface.
				List<DataStore> dsList = new ArrayList<>();
				dsList.add(ds);
				initGUIInputFilters(dsList, this.tstoolJFrame.ui_GetInputFilterY());
				// Reset the filters if the current selection is the one that was updated.
				if ( selectedDataStoreName.equalsIgnoreCase(newrds.getName()) ) {
					Message.printStatus(2,routine,"Setting the input filters for current selections because new Reclamation HDB datastore is selected.");
					this.tstoolJFrame.ui_SetInputFilterForSelections();
					//__queryInput_JPanel.repaint(); // See if this helps - without the filters don't always redraw.
				}
			}
		}
	}

	/**
	Refresh the query choices for the currently selected ReclamationHDB datastore.
	*/
	public void selectDataStore ( ReclamationHDBDataStore selectedDataStore )
	throws Exception {
	    //String routine = getClass().getSimpleName() + "uiAction_SelectDataStore_ReclamationHDB";
		// Get the DMI instance for the matching datastore.
		ReclamationHDBDataStore ds = (ReclamationHDBDataStore)selectedDataStore;

		SimpleJComboBox dataType_JComboBox = this.tstoolJFrame.ui_GetDataTypeJComboBox();
		SimpleJComboBox timeStep_JComboBox = this.tstoolJFrame.ui_GetTimeStepJComboBox();

		// Check the connection in case the connection timed out.
		ds.checkDatabaseConnection();
		ReclamationHDB_DMI dmi = (ReclamationHDB_DMI)ds.getDMI();
		this.tstoolJFrame.ui_SetInputNameVisible(false); // Not needed for HDB.
		dataType_JComboBox.removeAll ();
		// Get the list of valid object/data types from the database.
		List<String> dataTypes = dmi.getObjectDataTypes ( true );
		// Add a wildcard option to get all data types.
		dataTypes.add(0,"*");
		dataType_JComboBox.setData ( dataTypes );
		dataType_JComboBox.setEnabled ( true );

		// Get the list of timesteps that are valid for the data type.
		// Need to trigger a select to populate the input filters.
		timeStep_JComboBox.removeAll ();
		timeStep_JComboBox.add ( TSToolConstants.TIMESTEP_HOUR );
		timeStep_JComboBox.add ( "2" + TSToolConstants.TIMESTEP_HOUR );
		timeStep_JComboBox.add ( "3" + TSToolConstants.TIMESTEP_HOUR );
		timeStep_JComboBox.add ( "4" + TSToolConstants.TIMESTEP_HOUR );
		timeStep_JComboBox.add ( "6" + TSToolConstants.TIMESTEP_HOUR );
		timeStep_JComboBox.add ( "12" + TSToolConstants.TIMESTEP_HOUR );
		timeStep_JComboBox.add ( "24" + TSToolConstants.TIMESTEP_HOUR );
		timeStep_JComboBox.add ( TSToolConstants.TIMESTEP_DAY );
		timeStep_JComboBox.add ( TSToolConstants.TIMESTEP_MONTH );
		timeStep_JComboBox.add ( TSToolConstants.TIMESTEP_YEAR );
		timeStep_JComboBox.add ( TSToolConstants.TIMESTEP_IRREGULAR ); // Instantaneous handled as irregular.
		timeStep_JComboBox.add ( "*" ); // This will query hour, day, month, year, and irregular in sequence and concatenate the lists.
		timeStep_JComboBox.setMaximumRowCount(timeStep_JComboBox.getItemCount());
		// FIXME smalers 2010-10-26 Could handle WY as YEAR, but need to think about it to be consistent with TSTool in general.
		timeStep_JComboBox.select ( TSToolConstants.TIMESTEP_MONTH );
		timeStep_JComboBox.setEnabled ( true );

		// Initialize with blank data list.
		JWorksheet_AbstractRowTableModel query_TableModel = new TSTool_ReclamationHDB_TableModel( ds, null);
		this.tstoolJFrame.ui_SetTimeSeriesCatalogTableModel ( query_TableModel );
		TSTool_ReclamationHDB_CellRenderer cr = new TSTool_ReclamationHDB_CellRenderer((TSTool_ReclamationHDB_TableModel)query_TableModel);

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
        // The location (id), type, and time step uniquely identify the time series,
		// but the input_name is needed to indicate the database.
		JWorksheet_AbstractRowTableModel query_TableModel = this.tstoolJFrame.ui_GetTimeSeriesCatalogTableModel();
        TSTool_ReclamationHDB_TableModel model = (TSTool_ReclamationHDB_TableModel)query_TableModel;
        ReclamationHDBDataStore ds = (ReclamationHDBDataStore)this.tstoolJFrame.ui_GetSelectedDataStore();
        // Check the connection in case timeouts, etc.
        ds.checkDatabaseConnection();
        ReclamationHDB_DMI dmi = (ReclamationHDB_DMI)ds.getDMI();
        // Format the TSID using the older format that uses common names, but these are not guaranteed unique.
        String tsType = (String)query_TableModel.getValueAt( row, model.COL_TYPE_REAL_MODEL);
        int numCommandsAdded = 0;
        if ( dmi.getTSIDStyleSDI() ) {
            // Format the TSID using the newer SDI and MRI style.
            String loc, scenario = "";
            String siteCommonName = (String)query_TableModel.getValueAt( row, model.COL_SITE_COMMON_NAME );
            siteCommonName = siteCommonName.replace('.', ' ').replace('-',' ');
            if ( tsType.equalsIgnoreCase("Model") ) {
                loc = "" + (Integer)query_TableModel.getValueAt( row, model.COL_SITE_DATATYPE_ID ) + "-" +
                    (Integer)query_TableModel.getValueAt( row, model.COL_MODEL_RUN_ID );
                String modelName = (String)query_TableModel.getValueAt( row, model.COL_MODEL_NAME );
                modelName = modelName.replace('.', ' ').replace('-',' ');
                String modelRunName = (String)query_TableModel.getValueAt( row, model.COL_MODEL_RUN_NAME );
                modelRunName = modelRunName.replace('.', ' ').replace('-',' ');
                String hydrologicIndicator = (String)query_TableModel.getValueAt( row, model.COL_MODEL_HYDROLOGIC_INDICATOR );
                hydrologicIndicator = hydrologicIndicator.replace('.', ' ').replace('-',' ');
                String modelRunDate = "" + (Date)query_TableModel.getValueAt( row, model.COL_MODEL_RUN_DATE );
                // Trim off the hundredths of a second since that interferes with the TSID conventions.
                // It always appears to be ".0", also remove seconds :00 at end.
                int pos = modelRunDate.indexOf(".");
                if ( pos > 0 ) {
                    modelRunDate = modelRunDate.substring(0,pos - 3);
                }
                // The following should uniquely identify a model time series (in addition to other TSID parts).
                scenario = siteCommonName + "-" + modelName + "-" + modelRunName + "-" + hydrologicIndicator + "-" + modelRunDate;
            }
            else {
                loc = "" + (Integer)query_TableModel.getValueAt( row, model.COL_SITE_DATATYPE_ID );
                scenario = siteCommonName;
            }
            // Use data type common name as FYI and make sure no periods are in it because they will interfere with TSID syntax.
            String dataType = ((String)query_TableModel.getValueAt( row, model.COL_DATA_TYPE_COMMON_NAME)).replace("."," ");
            numCommandsAdded = this.tstoolJFrame.queryResultsList_AppendTSIDToCommandList (
                (String)query_TableModel.getValueAt( row, model.COL_OBJECT_TYPE_NAME ) + ":" + loc,
                (String)query_TableModel.getValueAt( row, model.COL_DATA_SOURCE),
                dataType,
                (String)query_TableModel.getValueAt( row, model.COL_TIME_STEP),
                scenario,
                null, // No sequence number.
                (String)query_TableModel.getValueAt( row,model.COL_DATASTORE_NAME),
                "",
                "", false, insertOffset );
        }
        else {
            String scenario = "";
            if ( tsType.equalsIgnoreCase("Model") ) {
                String modelRunDate = "" + (Date)query_TableModel.getValueAt( row, model.COL_MODEL_RUN_DATE );
                // Trim off the hundredths of a second since that interferes with the TSID conventions.
                // It always appears to be ".0".
                int pos = modelRunDate.indexOf(".");
                if ( pos > 0 ) {
                    modelRunDate = modelRunDate.substring(0,pos);
                }
                // Replace "." with "?" in the model information so as to not conflict with TSID conventions - will switch again later.
                String modelName = (String)query_TableModel.getValueAt( row, model.COL_MODEL_NAME );
                modelName = modelName.replace('.', '?');
                String modelRunName = (String)query_TableModel.getValueAt( row, model.COL_MODEL_RUN_NAME );
                modelRunName = modelRunName.replace('.', '?');
                String hydrologicIndicator = (String)query_TableModel.getValueAt( row, model.COL_MODEL_HYDROLOGIC_INDICATOR );
                hydrologicIndicator = hydrologicIndicator.replace('.', '?');
                // The following should uniquely identify a model time series (in addition to other TSID parts).
                scenario = modelName + "-" + modelRunName + "-" + hydrologicIndicator + "-" + modelRunDate;
            }
            String loc = (String)query_TableModel.getValueAt( row, model.COL_SITE_COMMON_NAME );
            String dataType = (String)query_TableModel.getValueAt( row, model.COL_DATA_TYPE_COMMON_NAME);
            numCommandsAdded = this.tstoolJFrame.queryResultsList_AppendTSIDToCommandList (
            //(String)__query_TableModel.getValueAt( row, model.COL_SUBJECT_TYPE ) + ":" +
            tsType + ":" + loc.replace('.','?'), // Replace period because it will interfere with TSID.
            (String)query_TableModel.getValueAt( row, model.COL_DATA_SOURCE),
            dataType,
            (String)query_TableModel.getValueAt( row, model.COL_TIME_STEP),
            scenario,
            null, // No sequence number.
            (String)query_TableModel.getValueAt( row,model.COL_DATASTORE_NAME),
            "",
            "", false, insertOffset );
        }
        return numCommandsAdded;
    }
}