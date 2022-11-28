// TSTool_HydroBaseRest - integration between TSTool UI and HydroBase REST web services

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

package cdss.app.tstool.datastore.cdss.hydrobaserest;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.List;

import javax.swing.JPanel;

import DWR.DMI.tstool.TSTool_JFrame;
import RTi.TS.TSIdent;
import RTi.TS.TimeSeriesIdentifierProvider;
import RTi.Util.GUI.InputFilter_JPanel;
import RTi.Util.GUI.JGUIUtil;
import RTi.Util.GUI.JWorksheet;
import RTi.Util.GUI.JWorksheet_AbstractRowTableModel;
import RTi.Util.GUI.SimpleJComboBox;
import RTi.Util.IO.PropList;
import RTi.Util.Message.Message;
import RTi.Util.String.StringUtil;
import cdss.dmi.hydrobase.rest.ColoradoHydroBaseRestDataStore;
import cdss.dmi.hydrobase.rest.dao.ClimateStationDataType;
import cdss.dmi.hydrobase.rest.dao.DiversionWaterClass;
import cdss.dmi.hydrobase.rest.dao.SurfaceWaterStationDataType;
import cdss.dmi.hydrobase.rest.dao.TelemetryStationDataType;
import cdss.dmi.hydrobase.rest.dao.WaterLevelsWell;
import cdss.dmi.hydrobase.rest.ui.ColoradoHydroBaseRest_ClimateStation_CellRenderer;
import cdss.dmi.hydrobase.rest.ui.ColoradoHydroBaseRest_ClimateStation_InputFilter_JPanel;
import cdss.dmi.hydrobase.rest.ui.ColoradoHydroBaseRest_ClimateStation_TableModel;
import cdss.dmi.hydrobase.rest.ui.ColoradoHydroBaseRest_Structure_CellRenderer;
import cdss.dmi.hydrobase.rest.ui.ColoradoHydroBaseRest_Structure_InputFilter_JPanel;
import cdss.dmi.hydrobase.rest.ui.ColoradoHydroBaseRest_Structure_TableModel;
import cdss.dmi.hydrobase.rest.ui.ColoradoHydroBaseRest_SurfaceWaterStation_CellRenderer;
import cdss.dmi.hydrobase.rest.ui.ColoradoHydroBaseRest_SurfaceWaterStation_InputFilter_JPanel;
import cdss.dmi.hydrobase.rest.ui.ColoradoHydroBaseRest_SurfaceWaterStation_TableModel;
import cdss.dmi.hydrobase.rest.ui.ColoradoHydroBaseRest_TelemetryStation_CellRenderer;
import cdss.dmi.hydrobase.rest.ui.ColoradoHydroBaseRest_TelemetryStation_InputFilter_JPanel;
import cdss.dmi.hydrobase.rest.ui.ColoradoHydroBaseRest_TelemetryStation_TableModel;
import cdss.dmi.hydrobase.rest.ui.ColoradoHydroBaseRest_WaterClass_CellRenderer;
import cdss.dmi.hydrobase.rest.ui.ColoradoHydroBaseRest_WaterClass_TableModel;
import cdss.dmi.hydrobase.rest.ui.ColoradoHydroBaseRest_Well_CellRenderer;
import cdss.dmi.hydrobase.rest.ui.ColoradoHydroBaseRest_Well_InputFilter_JPanel;
import cdss.dmi.hydrobase.rest.ui.ColoradoHydroBaseRest_Well_TableModel;
import riverside.datastore.DataStore;

/**
 * This class provides integration between TSTool and HydroBase REST web services.
 * These datastores are currently built into TSTool, rather than being a plugin.
 * Isolating the code simplifies maintenance.
 * The class is a singleton.  Use getInstance() to get the instance to call methods.
 */
public class TSTool_HydroBaseRest {

	/**
	 * Singleton instance of this class.
	 * Use getInstance() to return the singleton.
	 */
	private static TSTool_HydroBaseRest instance = null;
	
	/**
	 * Instance of the TSTool main UI.
	 */
	private TSTool_JFrame tstoolJFrame = null;

	/**
	 * Private constructor for lazy initialization.
	 */
	private TSTool_HydroBaseRest ( TSTool_JFrame tstoolJFrame ) {
		this.tstoolJFrame = tstoolJFrame;
	}

	/**
	Refresh the query choices for a ColoradoHydroBaseRest web service.
	*/
	public void dataStoreSelected ( ColoradoHydroBaseRestDataStore selectedDataStore, PropList tstoolProps )
	throws Exception {
	    //String routine = getClass().getSimpleName() + "uiAction_SelectInputName_ColoradoHydroBaseRest";
		// Input name is not currently used.

		SimpleJComboBox dataType_JComboBox = this.tstoolJFrame.ui_GetDataTypeJComboBox();
		SimpleJComboBox inputName_JComboBox = this.tstoolJFrame.ui_GetInputNameJComboBox();

		this.tstoolJFrame.ui_SetInputNameVisible(false); // Not needed.
		inputName_JComboBox.removeAll ();
		inputName_JComboBox.setEnabled ( false );
		String selectedInputType = this.tstoolJFrame.ui_GetSelectedInputType();
		// Get the distinct list of data types for all stations.
		dataType_JComboBox.setEnabled ( true );
		dataType_JComboBox.removeAll ();
		dataType_JComboBox.removeAll();
		boolean includeDataTypeGroups = true;
		boolean includeWildcards = true;
		List<String> dataTypes = selectedDataStore.getTimeSeriesDataTypes ( includeDataTypeGroups, includeWildcards );
		dataType_JComboBox.setData ( dataTypes );
		// Select the default (this causes the other choices to be updated).
		// TODO smalers 2010-07-21 Default to Streamflow once implemented, like HydroBase.
		dataType_JComboBox.select( null );
		try {
			dataType_JComboBox.select ( "Structure - DivTotal" );
		}
		catch ( Exception e ) {
			// Ignore for now.
		}

		// TODO smalers 2018-06-20 need to use table model for data type, not just default to structure.
		// Initialize with blank DivTotal data - will be reset when a query occurs.
		JWorksheet query_JWorksheet = tstoolJFrame.ui_GetTimeSeriesCatalogWorksheet();
		JWorksheet_AbstractRowTableModel query_TableModel = new ColoradoHydroBaseRest_Structure_TableModel(
				query_JWorksheet, StringUtil.atoi(tstoolProps.getValue("HydroBase.WDIDLength")), null, selectedInputType);
		this.tstoolJFrame.ui_SetTimeSeriesCatalogTableModel ( query_TableModel );
		ColoradoHydroBaseRest_Structure_CellRenderer cr =
				new ColoradoHydroBaseRest_Structure_CellRenderer((ColoradoHydroBaseRest_Structure_TableModel)query_TableModel);
		query_JWorksheet.setCellRenderer ( cr );
		query_JWorksheet.setModel ( query_TableModel );
		// Remove columns that are not appropriate.
		query_JWorksheet.setColumnWidths ( cr.getColumnWidths() );
	}
	
	/**
	 * Handle data type selection.
	 */
	public void dataTypeSelected ( DataStore selectedDataStore, String selectedDataType ) {
    	ColoradoHydroBaseRestDataStore ds = (ColoradoHydroBaseRestDataStore)selectedDataStore;
        List<String> timeSteps = ds.getTimeSeriesTimeSteps (selectedDataType);
        SimpleJComboBox timeStep_JComboBox = this.tstoolJFrame.ui_GetTimeStepJComboBox();
        timeStep_JComboBox.setData ( timeSteps );
        timeStep_JComboBox.select ( null );
        timeStep_JComboBox.setEnabled ( true );
        // Select monthly as the default if available.
        if ( JGUIUtil.isSimpleJComboBoxItem(timeStep_JComboBox,"Month", JGUIUtil.NONE, null, null ) ) {
            timeStep_JComboBox.select ( "Month" );
        }
        else {
            // Select the first item.
            try {
                timeStep_JComboBox.select ( 0 );
            }
            catch ( Exception e ) {
                // For cases when for some reason no choice is available.
                timeStep_JComboBox.setEnabled ( false );
            }
        }
	}

	/**
	 * Get the singleton instance.
	 */
	public static TSTool_HydroBaseRest getInstance ( TSTool_JFrame tstoolJFrame ) {
		if ( TSTool_HydroBaseRest.instance == null ) {
			TSTool_HydroBaseRest.instance = new TSTool_HydroBaseRest( tstoolJFrame );
		}
		return TSTool_HydroBaseRest.instance;
	}

	/**
	Read ColoradoHydroBaseRest time series via web service and list in the GUI.
	*/
	public void getTimeSeriesListClicked_ReadColoradoHydroBaseRestCatalog ( InputFilter_JPanel selectedInputFilter_JPanel) {
		String routine = getClass().getSimpleName() + ".getTimeSeriesListClicked_ReadColoradoHydroBaseRestCatalog";
		JGUIUtil.setWaitCursor ( this.tstoolJFrame, true );
		Message.printStatus ( 1, routine, "Please wait... retrieving data");

		String selectedDataType = this.tstoolJFrame.ui_GetSelectedDataType();
		String selectedTimeStep = this.tstoolJFrame.ui_GetSelectedTimeStep();

		// selectedDataType is "Group - DataType"
		Message.printStatus ( 2, "", "Datatype = \"" + selectedDataType + "\" timestep = \"" + selectedTimeStep + "\"" );

		int size = 0;
		try {
			DataStore dataStore = this.tstoolJFrame.ui_GetSelectedDataStore ();
			ColoradoHydroBaseRestDataStore ds = (ColoradoHydroBaseRestDataStore)dataStore;
			InputFilter_JPanel filterPanel = selectedInputFilter_JPanel;
			// Get the subject from the where filters.  If not set, warn and don't query.
			/* TODO smalers evaluate whether to keep this constraint to prevent slow/big queries.
        List<String> inputDivision = __selectedInputFilter_JPanel.getInput("Division", null, false, null );
        List<String> inputDistrict = __selectedInputFilter_JPanel.getInput("District", null, false, null );
        if ( (inputDistrict.size() + inputDivision.size()) == 0 ) {
            Message.printWarning ( 1, routine,
                "You must specify a district or division as a Where in the input filter." );
            JGUIUtil.setWaitCursor ( this, false );
            return;
        }
			 */
			this.tstoolJFrame.queryResultsList_Clear ();

			Message.printStatus(2, routine, "Selected filter panel is: " + filterPanel );

			//ColoradoHydroBaseRestDataStoreHelper helper = new ColoradoHydroBaseRestDataStoreHelper();
			if ( filterPanel instanceof ColoradoHydroBaseRest_ClimateStation_InputFilter_JPanel ) {
				List<ClimateStationDataType> tslist = ds.getClimateStationTimeSeriesCatalog(selectedDataType, selectedTimeStep, (ColoradoHydroBaseRest_ClimateStation_InputFilter_JPanel)filterPanel );
				// Make sure that size is set.
				if ( tslist != null ) {
					size = tslist.size();
				}
				// Now display the data in the worksheet.
				if ( size > 0 ) {
					Message.printStatus ( 1, routine, "" + size + " ColoradoHydroBaseRest climate station time series read for data type \"" +
							selectedDataType + "\" and timestep \"" + selectedTimeStep + "\".  Displaying data..." );
					JWorksheet query_JWorksheet = tstoolJFrame.ui_GetTimeSeriesCatalogWorksheet();
					JWorksheet_AbstractRowTableModel query_TableModel =
						new ColoradoHydroBaseRest_ClimateStation_TableModel(query_JWorksheet, tslist, selectedTimeStep, dataStore.getName() );
					this.tstoolJFrame.ui_SetTimeSeriesCatalogTableModel ( query_TableModel );
					ColoradoHydroBaseRest_ClimateStation_CellRenderer cr = new ColoradoHydroBaseRest_ClimateStation_CellRenderer(
							(ColoradoHydroBaseRest_ClimateStation_TableModel)query_TableModel);
					query_JWorksheet.setCellRenderer ( cr );
					query_JWorksheet.setModel(query_TableModel);
					query_JWorksheet.setColumnWidths ( cr.getColumnWidths(), this.tstoolJFrame.getGraphics() );
				}
				else {
					Message.printStatus ( 1, routine, "No ColoradoHydroBaseRest climate station time series read." );
					this.tstoolJFrame.queryResultsList_Clear ();
				}
			}
			else if ( filterPanel instanceof ColoradoHydroBaseRest_Structure_InputFilter_JPanel ) {
				List<DiversionWaterClass> tslist = ds.getWaterClassesTimeSeriesCatalog(selectedDataType, selectedTimeStep, (ColoradoHydroBaseRest_Structure_InputFilter_JPanel)filterPanel );
				// Make sure that size is set.
				if ( tslist != null ) {
					size = tslist.size();
				}
				// Now display the data in the worksheet.
				if ( size > 0 ) {
					Message.printStatus ( 1, routine, "" + size + " ColoradoHydroBaseRest structure time series read for data type \"" +
							selectedDataType + "\" and timestep \"" + selectedTimeStep + "\".  Displaying data..." );
					JWorksheet query_JWorksheet = tstoolJFrame.ui_GetTimeSeriesCatalogWorksheet();
					JWorksheet_AbstractRowTableModel query_TableModel =
						new ColoradoHydroBaseRest_WaterClass_TableModel(ds, query_JWorksheet, tslist, dataStore.getName() );
					this.tstoolJFrame.ui_SetTimeSeriesCatalogTableModel ( query_TableModel );
					ColoradoHydroBaseRest_WaterClass_CellRenderer cr = new ColoradoHydroBaseRest_WaterClass_CellRenderer(
							(ColoradoHydroBaseRest_WaterClass_TableModel)query_TableModel);
					query_JWorksheet.setCellRenderer ( cr );
					query_JWorksheet.setModel(query_TableModel);
					query_JWorksheet.setColumnWidths ( cr.getColumnWidths(), this.tstoolJFrame.getGraphics() );
				}
				else {
					Message.printStatus ( 1, routine, "No ColoradoHydroBaseRest structure time series read." );
					this.tstoolJFrame.queryResultsList_Clear ();
				}
			}
			else if ( filterPanel instanceof ColoradoHydroBaseRest_SurfaceWaterStation_InputFilter_JPanel ) {
				List<SurfaceWaterStationDataType> tslist = ds.getSurfaceWaterStationTimeSeriesCatalog(selectedDataType, selectedTimeStep, (ColoradoHydroBaseRest_SurfaceWaterStation_InputFilter_JPanel)filterPanel );
				// Make sure that size is set.
				if ( tslist != null ) {
					size = tslist.size();
				}
				// Now display the data in the worksheet.
				if ( size > 0 ) {
					Message.printStatus ( 1, routine, "" + size + " ColoradoHydroBaseRest surface water station time series read for data type \"" +
							selectedDataType + "\" and timestep \"" + selectedTimeStep + "\".  Displaying data..." );
					JWorksheet query_JWorksheet = tstoolJFrame.ui_GetTimeSeriesCatalogWorksheet();
					JWorksheet_AbstractRowTableModel query_TableModel =
						new ColoradoHydroBaseRest_SurfaceWaterStation_TableModel(query_JWorksheet, tslist, selectedTimeStep, dataStore.getName() );
					this.tstoolJFrame.ui_SetTimeSeriesCatalogTableModel ( query_TableModel );
					ColoradoHydroBaseRest_SurfaceWaterStation_CellRenderer cr = new ColoradoHydroBaseRest_SurfaceWaterStation_CellRenderer(
							(ColoradoHydroBaseRest_SurfaceWaterStation_TableModel)query_TableModel);
					query_JWorksheet.setCellRenderer ( cr );
					query_JWorksheet.setModel(query_TableModel);
					query_JWorksheet.setColumnWidths ( cr.getColumnWidths(), this.tstoolJFrame.getGraphics() );
				}
				else {
					Message.printStatus ( 1, routine, "No ColoradoHydroBaseRest surface water station time series read." );
					this.tstoolJFrame.queryResultsList_Clear ();
				}
			}
			else if ( filterPanel instanceof ColoradoHydroBaseRest_TelemetryStation_InputFilter_JPanel ) {
				List<TelemetryStationDataType> tslist = ds.getTelemetryStationTimeSeriesCatalog(selectedDataType, selectedTimeStep, (ColoradoHydroBaseRest_TelemetryStation_InputFilter_JPanel)filterPanel );
				// Make sure that size is set.
				if ( tslist != null ) {
					size = tslist.size();
				}
				// Now display the data in the worksheet.
				if ( size > 0 ) {
					Message.printStatus ( 1, routine, "" + size + " ColoradoHydroBaseRest telemetry station time series read for data type \"" +
							selectedDataType + "\" and timestep \"" + selectedTimeStep + "\".  Displaying data..." );
					JWorksheet query_JWorksheet = tstoolJFrame.ui_GetTimeSeriesCatalogWorksheet();
					JWorksheet_AbstractRowTableModel query_TableModel =
						new ColoradoHydroBaseRest_TelemetryStation_TableModel(query_JWorksheet, tslist, dataStore.getName() );
					ColoradoHydroBaseRest_TelemetryStation_CellRenderer cr = new ColoradoHydroBaseRest_TelemetryStation_CellRenderer(
							(ColoradoHydroBaseRest_TelemetryStation_TableModel)query_TableModel);
					query_JWorksheet.setCellRenderer ( cr );
					query_JWorksheet.setModel(query_TableModel);
					query_JWorksheet.setColumnWidths ( cr.getColumnWidths(), this.tstoolJFrame.getGraphics() );
				}
				else {
					Message.printStatus ( 1, routine, "No ColoradoHydroBaseRest telemetry station time series read." );
					this.tstoolJFrame.queryResultsList_Clear ();
				}
			}
			else if ( filterPanel instanceof ColoradoHydroBaseRest_Well_InputFilter_JPanel ) {
				List<WaterLevelsWell> tslist = ds.getWellTimeSeriesCatalog(selectedDataType, selectedTimeStep, (ColoradoHydroBaseRest_Well_InputFilter_JPanel)filterPanel );
				// Make sure that size is set.
				if ( tslist != null ) {
					size = tslist.size();
				}
				// Now display the data in the worksheet.
				if ( size > 0 ) {
					Message.printStatus ( 1, routine, "" + size + " ColoradoHydroBaseRest well time series read for data type \"" +
							selectedDataType + "\" and timestep \"" + selectedTimeStep + "\".  Displaying data..." );
					JWorksheet query_JWorksheet = tstoolJFrame.ui_GetTimeSeriesCatalogWorksheet();
					JWorksheet_AbstractRowTableModel query_TableModel =
						new ColoradoHydroBaseRest_Well_TableModel(query_JWorksheet, tslist, dataStore.getName() );
					this.tstoolJFrame.ui_SetTimeSeriesCatalogTableModel ( query_TableModel );
					ColoradoHydroBaseRest_Well_CellRenderer cr = new ColoradoHydroBaseRest_Well_CellRenderer(
							(ColoradoHydroBaseRest_Well_TableModel)query_TableModel);
					query_JWorksheet.setCellRenderer ( cr );
					query_JWorksheet.setModel(query_TableModel);
					query_JWorksheet.setColumnWidths ( cr.getColumnWidths(), this.tstoolJFrame.getGraphics() );
				}
				else {
					Message.printStatus ( 1, routine, "No ColoradoHydroBaseRest well time series read." );
					this.tstoolJFrame.queryResultsList_Clear ();
				}
			}
			JGUIUtil.setWaitCursor ( this.tstoolJFrame, false );
		}
		catch ( Exception e ) {
			// Messages elsewhere but catch so we can get the cursor back.
			Message.printWarning ( 3, routine, e );
			JGUIUtil.setWaitCursor ( this.tstoolJFrame, false );
		}
	}

	/**
	Initialize the ColoradoHydroBaseRest input filters, one filter panel for stations, structures,
	telemetry stations, and wells.
	*/
	public void initGUIInputFilters ( List<DataStore> dataStoreList, int y ) {
		String routine = getClass().getSimpleName() + ".initGUIInputFilters";
		int buffer = 3;
		Insets insets = new Insets(0,buffer,0,0);
		
		JPanel queryInput_JPanel = this.tstoolJFrame.ui_GetQueryInputJPanel();
		List<InputFilter_JPanel> inputFilterJPanelList = this.tstoolJFrame.ui_GetInputFilterJPanelList();

		Message.printStatus(2, routine, "Initializing ColoradoHydroBaseRest input filters.");
		String selectedDataType = this.tstoolJFrame.ui_GetSelectedDataType();
		String selectedTimeStep = this.tstoolJFrame.ui_GetSelectedTimeStep();
		for ( DataStore dataStore: dataStoreList ) {
			while ( true ) {
				// Try to find existing input filter panels for the same name (may be more than one
				// given that have stations, structures, etc.).
				JPanel ifp = this.tstoolJFrame.ui_GetInputFilterPanelForDataStoreName ( dataStore.getName(), selectedDataType, selectedTimeStep );
				// If the previous instance is not null, remove it from the list.
				if ( ifp != null ) {
					Message.printStatus(2, routine, "Removing matched ColoradoHydroBaseRest input filter \"" + ifp.getName() + ".");
					inputFilterJPanelList.remove ( ifp );
				}
				else {
					// No more filters matching the datastore.
					break;
				}
			}
			ColoradoHydroBaseRestDataStore ds = (ColoradoHydroBaseRestDataStore)dataStore;

			// If the an instance of a panel is not null, remove it from the list and then recreate it.

			// Add input filters for climate stations, for historical data.
			try {
				ColoradoHydroBaseRest_ClimateStation_InputFilter_JPanel inputFilter_ColoradoHydroBaseRest_ClimateStation_JPanel = new ColoradoHydroBaseRest_ClimateStation_InputFilter_JPanel( ds );
				JGUIUtil.addComponent(queryInput_JPanel, inputFilter_ColoradoHydroBaseRest_ClimateStation_JPanel,
						0, y, 3, 1, 1.0, 0.0, insets, GridBagConstraints.HORIZONTAL,
						GridBagConstraints.WEST );
				inputFilter_ColoradoHydroBaseRest_ClimateStation_JPanel.setName ( "ColoradoHydroBaseRest." + dataStore.getName() + ".ClimateStationInputFilterPanel" );
				inputFilterJPanelList.add ( inputFilter_ColoradoHydroBaseRest_ClimateStation_JPanel );
			}
			catch ( Exception e ) {
				Message.printWarning ( 2, routine, "Unable to initialize input filter for ColoradoHydroBaseRest climate stations for data type \""
						+ selectedDataType + "\" and time step \"" + selectedTimeStep + "\"" );
				Message.printWarning ( 3, routine, e );
			}

			// Add input filters for structures, including DivTotal, RelTotal, and reservoir measurements.
			try {
				ColoradoHydroBaseRest_Structure_InputFilter_JPanel inputFilter_ColoradoHydroBaseRest_Structure_JPanel = new
						ColoradoHydroBaseRest_Structure_InputFilter_JPanel( ds, false );
				JGUIUtil.addComponent(queryInput_JPanel, inputFilter_ColoradoHydroBaseRest_Structure_JPanel,
						0, y, 3, 1, 1.0, 0.0, insets, GridBagConstraints.HORIZONTAL,
						GridBagConstraints.WEST );
				inputFilter_ColoradoHydroBaseRest_Structure_JPanel.setName ( "ColoradoHydroBaseRest." + dataStore.getName() + ".StructureInputFilterPanel" );
				inputFilterJPanelList.add ( inputFilter_ColoradoHydroBaseRest_Structure_JPanel );
			}
			catch ( Exception e ) {
				Message.printWarning ( 2, routine, "Unable to initialize input filter for ColoradoHydroBaseRest structures for data type \""
						+ selectedDataType + "\" and time step \"" + selectedTimeStep + "\"" );
				Message.printWarning ( 3, routine, e );
			}

			// Add input filters for surface water stations, for historical data.
			try {
				ColoradoHydroBaseRest_SurfaceWaterStation_InputFilter_JPanel inputFilter_ColoradoHydroBaseRest_SurfaceWaterStation_JPanel =
						new ColoradoHydroBaseRest_SurfaceWaterStation_InputFilter_JPanel( ds );
				JGUIUtil.addComponent(queryInput_JPanel, inputFilter_ColoradoHydroBaseRest_SurfaceWaterStation_JPanel,
						0, y, 3, 1, 1.0, 0.0, insets, GridBagConstraints.HORIZONTAL,
						GridBagConstraints.WEST );
				inputFilter_ColoradoHydroBaseRest_SurfaceWaterStation_JPanel.setName ( "ColoradoHydroBaseRest." + dataStore.getName() + ".SurfaceWaterStationInputFilterPanel" );
				inputFilterJPanelList.add ( inputFilter_ColoradoHydroBaseRest_SurfaceWaterStation_JPanel );
			}
			catch ( Exception e ) {
				Message.printWarning ( 2, routine, "Unable to initialize input filter for ColoradoHydroBaseRest surface water stations for data type \""
						+ selectedDataType + "\" and time step \"" + selectedTimeStep + "\"" );
				Message.printWarning ( 3, routine, e );
			}

			// Add input filters for telemetry stations, for real-time data.
			try {
				ColoradoHydroBaseRest_TelemetryStation_InputFilter_JPanel inputFilter_ColoradoHydroBaseRest_TelemetryStation_JPanel =
						new ColoradoHydroBaseRest_TelemetryStation_InputFilter_JPanel( ds );
				JGUIUtil.addComponent(queryInput_JPanel, inputFilter_ColoradoHydroBaseRest_TelemetryStation_JPanel,
						0, y, 3, 1, 1.0, 0.0, insets, GridBagConstraints.HORIZONTAL,
						GridBagConstraints.WEST );
				inputFilter_ColoradoHydroBaseRest_TelemetryStation_JPanel.setName ( "ColoradoHydroBaseRest." + dataStore.getName() + ".TelemetryStationInputFilterPanel" );
				inputFilterJPanelList.add ( inputFilter_ColoradoHydroBaseRest_TelemetryStation_JPanel );
			}
			catch ( Exception e ) {
				Message.printWarning ( 2, routine, "Unable to initialize input filter for ColoradoHydroBaseRest telemetry stations for data type \""
						+ selectedDataType + "\" and time step \"" + selectedTimeStep + "\"" );
				Message.printWarning ( 3, routine, e );
			}

			// Well water levels (pumping is under diversion records).
			try {
				ColoradoHydroBaseRest_Well_InputFilter_JPanel inputFilter_ColoradoHydroBaseRest_Well_JPanel = new ColoradoHydroBaseRest_Well_InputFilter_JPanel( ds );
				JGUIUtil.addComponent(queryInput_JPanel, inputFilter_ColoradoHydroBaseRest_Well_JPanel,
						0, y, 3, 1, 1.0, 0.0, insets, GridBagConstraints.HORIZONTAL,
						GridBagConstraints.WEST );
				inputFilter_ColoradoHydroBaseRest_Well_JPanel.setName ( "ColoradoHydroBaseRest." + dataStore.getName() + ".WellInputFilterPanel" );
				inputFilterJPanelList.add ( inputFilter_ColoradoHydroBaseRest_Well_JPanel );
			}
			catch ( Exception e ) {
				Message.printWarning ( 2, routine, "Unable to initialize input filter for ColoradoHydroBaseRest wells for data type \""
						+ selectedDataType + "\" and time step \"" + selectedTimeStep + "\"" );
				Message.printWarning ( 3, routine, e );
			}
		}
	}

	/**
	Refresh the query choices for a ColoradoHydroBaseRest web service.
	*/
	public void selectDataStore ( ColoradoHydroBaseRestDataStore selectedDataStore, PropList tstoolProps )
	throws Exception {
	    //String routine = getClass().getSimpleName() + "selectDataStore";
		// Input name is not currently used.

		SimpleJComboBox dataType_JComboBox = this.tstoolJFrame.ui_GetDataTypeJComboBox();
		SimpleJComboBox inputName_JComboBox = this.tstoolJFrame.ui_GetInputNameJComboBox();

		this.tstoolJFrame.ui_SetInputNameVisible(false); // Not needed.
		inputName_JComboBox.removeAll ();
		inputName_JComboBox.setEnabled ( false );
		String selectedInputType = this.tstoolJFrame.ui_GetSelectedInputType();
		// Get the distinct list of data types for all stations.
		dataType_JComboBox.setEnabled ( true );
		dataType_JComboBox.removeAll ();
		dataType_JComboBox.removeAll();
		boolean includeDataTypeGroups = true;
		boolean includeWildcards = true;
		List<String> dataTypes = selectedDataStore.getTimeSeriesDataTypes ( includeDataTypeGroups, includeWildcards );
		dataType_JComboBox.setData ( dataTypes );
		// Select the default (this causes the other choices to be updated).
		// TODO smalers 2010-07-21 Default to Streamflow once implemented, like HydroBase.
		dataType_JComboBox.select( null );
		try {
			dataType_JComboBox.select ( "Structure - DivTotal" );
		}
		catch ( Exception e ) {
			// Ignore for now.
		}

		// TODO smalers 2018-06-20 need to use table model for data type, not just default to structure.
		// Initialize with blank DivTotal data - will be reset when a query occurs.
		JWorksheet query_JWorksheet = tstoolJFrame.ui_GetTimeSeriesCatalogWorksheet();
		JWorksheet_AbstractRowTableModel query_TableModel = new ColoradoHydroBaseRest_Structure_TableModel(
				query_JWorksheet, StringUtil.atoi(tstoolProps.getValue("HydroBase.WDIDLength")), null, selectedInputType);
		this.tstoolJFrame.ui_SetTimeSeriesCatalogTableModel ( query_TableModel );
		ColoradoHydroBaseRest_Structure_CellRenderer cr =
				new ColoradoHydroBaseRest_Structure_CellRenderer( (ColoradoHydroBaseRest_Structure_TableModel)query_TableModel);
		query_JWorksheet.setCellRenderer ( cr );
		query_JWorksheet.setModel ( query_TableModel );
		// Remove columns that are not appropriate.
		query_JWorksheet.setColumnWidths ( cr.getColumnWidths() );
	}

	/**
	 * Transfer one time series catalog row to a command.
	 * @param row the time series catalog row to transfer (0+)
	 * @param useAlias whether to use an alias
	 */
    public int transferOneTimeSeriesCatalogRowToCommands ( int row, boolean useAlias, int insertOffset,
    	JWorksheet_AbstractRowTableModel query_TableModel, String selectedDataStoreName ) {
    	String routine = getClass().getSimpleName() + ".transferOneTimeSeriesCatalogRow";
    	int numCommandsAdded = 0;
        if ( query_TableModel instanceof ColoradoHydroBaseRest_ClimateStation_TableModel         // Climate historical stations.
        	|| query_TableModel instanceof ColoradoHydroBaseRest_SurfaceWaterStation_TableModel  // Surface water historical stations.
        	|| query_TableModel instanceof ColoradoHydroBaseRest_TelemetryStation_TableModel     // Stations with real-time data.
        	|| query_TableModel instanceof ColoradoHydroBaseRest_WaterClass_TableModel           // Structures with DivTotal, WaterClass, etc. types.
        	|| query_TableModel instanceof ColoradoHydroBaseRest_Well_TableModel                 // Wells with groundwater levels.
        	) {
        	TimeSeriesIdentifierProvider tsidProvider = (TimeSeriesIdentifierProvider)query_TableModel;
        	TSIdent tsident = null;
        	try {
        		tsident = tsidProvider.getTimeSeriesIdentifier(row);
        	}
        	catch ( Exception e ) {
        		Message.printWarning(3, routine, e);
        		return 0;
        	}
        	if ( tsident != null ) {
	            numCommandsAdded = this.tstoolJFrame.queryResultsList_AppendTSIDToCommandList (
	                tsident.getLocation(),
	                tsident.getSource(),
	                tsident.getType(),
	                tsident.getInterval(),
	                tsident.getScenario(),
	                tsident.getSequenceID(),
	                selectedDataStoreName,
	                tsident.getInputName(),
	                tsident.getComment(),
	                useAlias, insertOffset );
        	}
        }
    	return numCommandsAdded;
	}
}