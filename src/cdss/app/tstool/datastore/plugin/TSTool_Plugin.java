// TSTool_Plugin - integration between TSTool UI and plugin datastores

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

package cdss.app.tstool.datastore.plugin;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

import DWR.DMI.tstool.TSToolConstants;
import DWR.DMI.tstool.TSToolMenus;
import DWR.DMI.tstool.TSTool_JFrame;
import RTi.DMI.DatabaseDataStore;
import RTi.TS.TSIdent;
import RTi.Util.GUI.InputFilter_JPanel;
import RTi.Util.GUI.JGUIUtil;
import RTi.Util.GUI.JWorksheet;
import RTi.Util.GUI.JWorksheet_AbstractExcelCellRenderer;
import RTi.Util.GUI.JWorksheet_AbstractRowTableModel;
import RTi.Util.GUI.SimpleJComboBox;
import RTi.Util.GUI.SimpleJMenuItem;
import RTi.Util.Message.Message;
import riverside.datastore.DataStore;
import riverside.datastore.PluginDataStore;

/**
 * This class provides integration between TSTool and * plugin datastores.
 * Isolating the code simplifies maintenance.
 * The class is a singleton.  Use getInstance() to get the instance to call methods.
 */
public class TSTool_Plugin {

	/**
	 * Singleton instance of this class.
	 * Use getInstance() to return the singleton.
	 */
	private static TSTool_Plugin instance = null;
	
	/**
	 * Instance of the TSTool main UI.
	 */
	private TSTool_JFrame tstoolJFrame = null;

	/**
	 * Private constructor for lazy initialization.
	 */
	private TSTool_Plugin ( TSTool_JFrame tstoolJFrame ) {
		this.tstoolJFrame = tstoolJFrame;
	}

	/**
	List of plugin datastore classes, which allow third-party datastores to be opened and used for data.
	*/
	private List<Class> pluginDataStoreClassList = new ArrayList<>();

	/**
	List of plugin datastore factory classes, which allow third-party datastores to be opened and used for data.
	*/
	private List<Class> pluginDataStoreFactoryClassList = new ArrayList<>();

	/**
	List of plugin command classes, which allow third-party commands to be recognized and run.
	*/
	private List<Class> pluginCommandClassList = new ArrayList<>();
	
	/**
	 * Handle data type selection.
	 */
	public void dataTypeSelected ( PluginDataStore pluginDataStore, String selectedDataType ) {
    	List<String> timeStepChoices = pluginDataStore.getTimeSeriesDataIntervalStrings(selectedDataType);
    	SimpleJComboBox timeStep_JComboBox = this.tstoolJFrame.ui_GetTimeStepJComboBox();
    	timeStep_JComboBox.removeAll ();
    	timeStep_JComboBox.setData (timeStepChoices);
        timeStep_JComboBox.select ( null );
        timeStep_JComboBox.select ( 0 );
	}

	/**
	 * Get the singleton instance.
	 */
	public static TSTool_Plugin getInstance ( TSTool_JFrame tstoolJFrame ) {
		if ( TSTool_Plugin.instance == null ) {
			TSTool_Plugin.instance = new TSTool_Plugin( tstoolJFrame );
		}
		return TSTool_Plugin.instance;
	}

	/**
	 * Get the plugin datastore class list.
	 */
	public List<Class> getPluginDataStoreClassList () {
		return this.pluginDataStoreClassList;
	}

	/**
	 * Get the plugin datastore factory class list.
	 */
	public List<Class> getPluginDataStoreFactoryClassList () {
		return this.pluginDataStoreFactoryClassList;
	}

	/**
	 * Get the plugin command class list.
	 */
	public List<Class> getPluginCommandClassList () {
		return this.pluginCommandClassList;
	}

	/**
	Read plugin datastore time series and list in the GUI.
	*/
	public void getTimeSeriesListClicked_ReadPluginTimeSeriesCatalog ( InputFilter_JPanel selectedInputFilter_JPanel ) {
		String rtn = getClass().getSimpleName() + "getTimeSeriesListClicked_ReadPluginTimeSeriesCatalog";
		JGUIUtil.setWaitCursor ( this.tstoolJFrame, true );
		Message.printStatus ( 1, rtn, "Please wait... retrieving data");
		
		SimpleJComboBox dataType_JComboBox = this.tstoolJFrame.ui_GetDataTypeJComboBox();
		SimpleJComboBox timeStep_JComboBox = this.tstoolJFrame.ui_GetTimeStepJComboBox();

		DataStore dataStore = this.tstoolJFrame.ui_GetSelectedDataStore ();
		PluginDataStore pds = null;
		if ( dataStore instanceof PluginDataStore ) {
			pds = (PluginDataStore)dataStore;
		}
		// The headers are a list of objects controlled by the plugin datastore.
		try {
			if ( dataStore instanceof DatabaseDataStore ) {
				// Check the connection in case the connection timed out.
				DatabaseDataStore dbds = (DatabaseDataStore)dataStore;
				dbds.checkDatabaseConnection();
			}
			this.tstoolJFrame.queryResultsList_Clear ();

			String dataType = dataType_JComboBox.getSelected(); // May be "datatype" or "datatype - note", but generically can't know here.
			String timeStep = timeStep_JComboBox.getSelected(); // May be "interval" or "interval - note", but generically can't know here.

			List<Object> results = null;
			if ( pds != null ) {
				// Data type is shown without name so use full choice.
				try {
					JWorksheet_AbstractRowTableModel query_TableModel = pds.createTimeSeriesListTableModel(dataType,timeStep,selectedInputFilter_JPanel);
					this.tstoolJFrame.ui_SetTimeSeriesCatalogTableModel ( query_TableModel );
					@SuppressWarnings("unchecked")
					List<Object> results0 = query_TableModel.getData();
					results = results0;
					if ( results != null ) {
						// TODO Does not work??
						//__query_TableModel.setNewData ( results );
						// Try brute force.
						JWorksheet_AbstractExcelCellRenderer cr = pds.getTimeSeriesListCellRenderer(query_TableModel);

						JWorksheet query_JWorksheet = tstoolJFrame.ui_GetTimeSeriesCatalogWorksheet();
						query_JWorksheet.setCellRenderer ( cr );
						query_JWorksheet.setModel ( query_TableModel );
						query_JWorksheet.setColumnWidths ( cr.getColumnWidths(), this.tstoolJFrame.getGraphics() );
					}
				}
				catch ( Exception e ) {
					Message.printWarning ( 1, rtn, "Error querying time series list (" + e + ")." );
					Message.printWarning(2, rtn, e);
					results = null;
				}
			}

			int size = 0;
			if ( results == null ) {
				Message.printStatus ( 1, rtn, "Query complete.  No records returned." );
			}
			else {
				size = results.size();
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
	Initialize the GUI "Commands(Plugin)" menu.
	*/
	public void initGUIMenus_Commands ( JMenuBar menu_bar ) {
		// "Commands(Plugin)".

		menu_bar.add( TSToolMenus.Commands_Plugin_JMenu = new JMenu( TSToolConstants.Commands_Plugin_String, true ) );
		TSToolMenus.Commands_Plugin_JMenu.setToolTipText("Insert command into commands list (above first selected command, or at end).");

		//__Commands_Plugin_JMenu.add( __Commands_Plugin_ReadTimeSeries_JMenu = new JMenu( TSToolConstants.Commands_Plugin_ReadTimeSeries_String, true ) );
		//menu_bar.add( __Commands_JMenu = new JMenu( TSToolConstants.Commands_String, true ) );

		// Loop through the plugin command classes and add menus.

		for ( Class c : this.pluginCommandClassList ) {
			// For now use the class name to determine the menu.
			String name = c.getSimpleName();
			int pos = name.indexOf("_Command");
			if ( pos > 0 ) {
				// TODO smalers 2016-04-02 Need a way to get a short command description.
				TSToolMenus.Commands_Plugin_JMenu.add ( new SimpleJMenuItem(name.substring(0,pos) + "()...", this.tstoolJFrame ) );
			}
		}

		//__Commands_JMenu.add ( __Commands_SelectTimeSeries_JMenu = new JMenu(TSToolConstants.Commands_SelectTimeSeries_String) );
		//__Commands_SelectTimeSeries_JMenu.setToolTipText("Select time series for processing (use with TSList=SelectedTS).");
		//__Commands_SelectTimeSeries_JMenu.add ( __Commands_Select_DeselectTimeSeries_JMenuItem =
		//    new SimpleJMenuItem(TSToolConstants.Commands_Select_DeselectTimeSeries_String, this ) );
	}
	
	/**
	 * Initialize the GUI input filters.
	 * @param dataStoreList
	 * @param y
	 */
	public void initGUIInputFilters ( PluginDataStore pds,
		JPanel queryInput_JPanel, List<InputFilter_JPanel> inputFilterJPanelList, Insets insets, int y ) {
		String routine = getClass().getSimpleName() + ".initGUIInputFilters";
        DataStore ds = (DataStore)pds;
        Message.printStatus(2,routine,"Adding input filter for plugin datastore \"" + ds.getName() + "\"..." );
		InputFilter_JPanel ifp = pds.createTimeSeriesListInputFilterPanel();
        // Add the new panel to the layout and set in the global data.
        JGUIUtil.addComponent(queryInput_JPanel, ifp,
            0, y, 3, 1, 1.0, 0.0, insets, GridBagConstraints.HORIZONTAL,
            GridBagConstraints.WEST );
        // TODO smalers 2016-04-16 might need more care setting this name.
        // However, class name, plus datastore name should be unique.
        ifp.setName(pds.getClass().getSimpleName() + "." + ds.getName() + ".InputFilterPanel");
        inputFilterJPanelList.add ( ifp );
        Message.printStatus(2,routine,"...added input filter for plugin datastore \"" + ds.getName() + "\"" );
	}

	/**
	Refresh the query choices for the currently selected plugin datastore.
	This will only be called if a plugin but pass as the base class DataStore to access normal information.
	*/
	public void selectDataStore ( DataStore selectedDataStore )
	throws Exception {
		//String routine = getClass().getSimpleName() + "uiAction_SelectDataStore_Plugin";

		SimpleJComboBox dataType_JComboBox = this.tstoolJFrame.ui_GetDataTypeJComboBox();
		SimpleJComboBox timeStep_JComboBox = this.tstoolJFrame.ui_GetTimeStepJComboBox();

		// Check the connection in case the connection timed out.
		DatabaseDataStore dbds = null;
		if ( selectedDataStore instanceof DatabaseDataStore ) {
			dbds = (DatabaseDataStore)selectedDataStore;
			dbds.checkDatabaseConnection();
		}
		PluginDataStore pds = null;
		if ( selectedDataStore instanceof PluginDataStore ) {
			pds = (PluginDataStore)selectedDataStore;
		}
		if ( pds == null ) {
			// This method should not have been called.
			return;
		}
		this.tstoolJFrame.ui_SetInputNameVisible(false); // Not needed for datastores (used for files).
		dataType_JComboBox.removeAll ();
		// Get the list of valid object/data types from the database.
		List<String> dataTypeStrings = pds.getTimeSeriesDataTypeStrings(null);
		dataType_JComboBox.setData ( dataTypeStrings );
		dataType_JComboBox.select ( 0 );
		dataType_JComboBox.setEnabled ( true );
		// Set the initial timestep as "*" and refresh the list once a data type selection is made.
		timeStep_JComboBox.removeAll ();
		List<String> intervalStrings = pds.getTimeSeriesDataIntervalStrings(dataType_JComboBox.getSelected());
		if ( intervalStrings.size() > 0 ) {
			timeStep_JComboBox.setData(intervalStrings);
			timeStep_JComboBox.setEnabled ( true );
			timeStep_JComboBox.select ( 0 );
		}
		else {
			// Don't have timesteps to choose from, probably an error of some kind.
			timeStep_JComboBox.setEnabled ( false );
		}

		// Initialize with blank data list.
		List<Object> emptyData = new ArrayList<>();
		JWorksheet_AbstractRowTableModel query_TableModel = pds.getTimeSeriesListTableModel(emptyData);
		JWorksheet_AbstractExcelCellRenderer cr = pds.getTimeSeriesListCellRenderer(query_TableModel);

		JWorksheet query_JWorksheet = tstoolJFrame.ui_GetTimeSeriesCatalogWorksheet();
		query_JWorksheet.setCellRenderer ( cr );
		query_JWorksheet.setModel ( query_TableModel );
		query_JWorksheet.setColumnWidths ( cr.getColumnWidths() );
	}
	
	/**
	 * Set the plugin datastore class list.
	 */
	public void setPluginDataStoreClassList ( List<Class> pluginDataStoreClassList) {
		this.pluginDataStoreClassList = pluginDataStoreClassList;
	}

	/**
	 * Set the plugin datastore factory class list.
	 */
	public void setPluginDataStoreFactoryClassList ( List<Class> pluginDataStoreFactoryClassList ) {
		this.pluginDataStoreFactoryClassList = pluginDataStoreFactoryClassList;
	}

	/**
	 * Set the plugin command class list.
	 */
	public void setPluginCommandClassList ( List<Class> pluginCommandClassList ) {
		this.pluginCommandClassList = pluginCommandClassList;
	}

	/**
	 * Transfer one time series catalog row to a command, for daily time series.
	 * @param row the time series catalog row to transfer (0+)
	 * @param useAlias whether to use an alias
	 */
    public int transferOneTimeSeriesCatalogRowToCommands ( int row, boolean useAlias, int insertOffset,
    	JWorksheet_AbstractRowTableModel query_TableModel, DataStore selectedDataStore ) {
    	// The time series identifier parts are retrieved from the datastore.
    	PluginDataStore pds = (PluginDataStore)selectedDataStore;
    	TSIdent tsident = pds.getTimeSeriesIdentifierFromTableModel(query_TableModel,row);
        String comment = "";
        int numCommandsAdded = this.tstoolJFrame.queryResultsList_AppendTSIDToCommandList (
            tsident.getLocation(),
            tsident.getSource(),
            tsident.getType(),
            tsident.getInterval(),
            tsident.getScenario(),
            tsident.getSequenceID(),
            selectedDataStore.getName(),
            tsident.getInputName(),
            comment,
            useAlias, insertOffset );
        return numCommandsAdded;
    }

}