// TSTool_HydroBase - integration between TSTool UI and HydroBase database connection

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

package cdss.app.tstool.datastore.cdss.hydrobase;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPanel;

import DWR.DMI.HydroBaseDMI.HydroBaseDMI;
import DWR.DMI.HydroBaseDMI.HydroBaseDataStore;
import DWR.DMI.HydroBaseDMI.HydroBase_AgriculturalCASSCropStats;
import DWR.DMI.HydroBaseDMI.HydroBase_AgriculturalCASSLivestockStats;
import DWR.DMI.HydroBaseDMI.HydroBase_AgriculturalNASSCropStats;
import DWR.DMI.HydroBaseDMI.HydroBase_CUPopulation;
import DWR.DMI.HydroBaseDMI.HydroBase_GUI_AgriculturalCASSCropStats_InputFilter_JPanel;
import DWR.DMI.HydroBaseDMI.HydroBase_GUI_AgriculturalCASSLivestockStats_InputFilter_JPanel;
import DWR.DMI.HydroBaseDMI.HydroBase_GUI_AgriculturalNASSCropStats_InputFilter_JPanel;
import DWR.DMI.HydroBaseDMI.HydroBase_GUI_CUPopulation_InputFilter_JPanel;
import DWR.DMI.HydroBaseDMI.HydroBase_GUI_GroundWater_InputFilter_JPanel;
import DWR.DMI.HydroBaseDMI.HydroBase_GUI_StationGeolocMeasType_InputFilter_JPanel;
import DWR.DMI.HydroBaseDMI.HydroBase_GUI_StructureGeolocStructMeasType_InputFilter_JPanel;
import DWR.DMI.HydroBaseDMI.HydroBase_GUI_StructureIrrigSummaryTS_InputFilter_JPanel;
import DWR.DMI.HydroBaseDMI.HydroBase_GroundWaterWellsView;
import DWR.DMI.HydroBaseDMI.HydroBase_StationGeolocMeasType;
import DWR.DMI.HydroBaseDMI.HydroBase_StructureGeolocStructMeasType;
import DWR.DMI.HydroBaseDMI.HydroBase_StructureIrrigSummaryTS;
import DWR.DMI.HydroBaseDMI.HydroBase_Util;
import DWR.DMI.HydroBaseDMI.SelectHydroBaseJDialog;
import DWR.DMI.HydroBaseDMI.ui.TSTool_HydroBase_AgGIS_CellRenderer;
import DWR.DMI.HydroBaseDMI.ui.TSTool_HydroBase_AgGIS_TableModel;
import DWR.DMI.HydroBaseDMI.ui.TSTool_HydroBase_Ag_CASS_CellRenderer;
import DWR.DMI.HydroBaseDMI.ui.TSTool_HydroBase_Ag_CASS_TableModel;
import DWR.DMI.HydroBaseDMI.ui.TSTool_HydroBase_Ag_NASS_CellRenderer;
import DWR.DMI.HydroBaseDMI.ui.TSTool_HydroBase_Ag_NASS_TableModel;
import DWR.DMI.HydroBaseDMI.ui.TSTool_HydroBase_CASSLivestockStats_CellRenderer;
import DWR.DMI.HydroBaseDMI.ui.TSTool_HydroBase_CASSLivestockStats_TableModel;
import DWR.DMI.HydroBaseDMI.ui.TSTool_HydroBase_CUPopulation_CellRenderer;
import DWR.DMI.HydroBaseDMI.ui.TSTool_HydroBase_CUPopulation_TableModel;
import DWR.DMI.HydroBaseDMI.ui.TSTool_HydroBase_GroundWaterWellsView_TableModel;
import DWR.DMI.HydroBaseDMI.ui.TSTool_HydroBase_StationGeolocMeasType_CellRenderer;
import DWR.DMI.HydroBaseDMI.ui.TSTool_HydroBase_StationGeolocMeasType_TableModel;
import DWR.DMI.HydroBaseDMI.ui.TSTool_HydroBase_StructureGeolocStructMeasType_CellRenderer;
import DWR.DMI.HydroBaseDMI.ui.TSTool_HydroBase_StructureGeolocStructMeasType_TableModel;
import DWR.DMI.HydroBaseDMI.ui.TSTool_HydroBase_WellLevel_Day_CellRenderer;
import DWR.DMI.HydroBaseDMI.ui.TSTool_HydroBase_WellLevel_Day_TableModel;
import DWR.DMI.tstool.TSToolConstants;
import DWR.DMI.tstool.TSToolMain;
import DWR.DMI.tstool.TSTool_JFrame;
import RTi.DMI.DatabaseDataStore;
import RTi.GR.GRLimits;
import RTi.Util.GUI.InputFilter_JPanel;
import RTi.Util.GUI.JGUIUtil;
import RTi.Util.GUI.JWorksheet;
import RTi.Util.GUI.JWorksheet_AbstractRowTableModel;
import RTi.Util.GUI.JWorksheet_DefaultTableCellRenderer;
import RTi.Util.GUI.SimpleJComboBox;
import RTi.Util.IO.IOUtil;
import RTi.Util.IO.PropList;
import RTi.Util.Message.Message;
import RTi.Util.String.StringUtil;
import riverside.datastore.DataStore;

/**
 * This class provides integration between TSTool and HydroBase database.
 * These datastores are currently built into TSTool, rather than being a plugin.
 * Isolating the code simplifies maintenance.
 * The class is a singleton.  Use getInstance() to get the instance to call methods.
 */
public class TSTool_HydroBase {

	/**
	 * Singleton instance of this class.
	 * Use getInstance() to return the singleton.
	 */
	private static TSTool_HydroBase instance = null;
	
	/**
	 * Instance of the TSTool main UI.
	 */
	private TSTool_JFrame tstoolJFrame = null;

	/**
	 * Private constructor for lazy initialization.
	 */
	private TSTool_HydroBase ( TSTool_JFrame tstoolJFrame ) {
		this.tstoolJFrame = tstoolJFrame;
	}

	/**
	InputFilter_JPanel for HydroBase CASS agricultural crop statistics time series.
	*/
	private InputFilter_JPanel inputFilterHydroBaseCASSCropStats_JPanel = null;

	/**
	InputFilter_JPanel for HydroBase CASS agricultural livestock statistics time series.
	*/
	private InputFilter_JPanel inputFilterHydroBaseCASSLivestockStats_JPanel = null;

	/**
	InputFilter_JPanel for HydroBase CUPopulation time series.
	*/
	private InputFilter_JPanel inputFilterHydroBaseCUPopulation_JPanel = null;

	/**
	InputFilter_JPanel for HydroBase NASS agricultural crop statistics time series.
	*/
	private InputFilter_JPanel inputFilterHydroBaseNASS_JPanel = null;

	/**
	InputFilter_JPanel for HydroBase structure irrig_summary_ts time series.
	*/
	private InputFilter_JPanel inputFilterHydroBaseIrrigts_JPanel = null;

	/**
	InputFilter_JPanel for HydroBase station time series.
	*/
	private InputFilter_JPanel inputFilterHydroBaseStation_JPanel = null;

	/**
	InputFilter_JPanel for HydroBase structure time series - those that do not use SFUT.
	*/
	private InputFilter_JPanel inputFilterHydroBaseStructure_JPanel = null;

	/**
	InputFilter_JPanel for HydroBase structure time series - those that do use SFUT.
	*/
	private InputFilter_JPanel inputFilterHydroBaseStructureSfut_JPanel = null;

	/**
	HydroBaseDataStore for HydroBase input type, opened via TSTool.cfg information and the HydroBase select dialog,
	provided to the processor as the initial HydroBase DMI instance.
	When working with HydroBase if the UI has a HydroBase datastore selected, then the datastore will be used; otherwise,
	the legacy datastore will be used.
	TODO smalers 2012-09-10 Begin phasing out in favor of datastores but need to figure out how to get rid of login
	dialog and have best practices for modelers to configure datastores.
	*/
	private HydroBaseDataStore hbDataStoreLegacy = null;

	/**
	InputFilter_JPanel for groundwater wells data.
	*/
	private InputFilter_JPanel inputFilterHydroBaseWells_JPanel = null;

	/**
	Enable/disable the HydroBase input type features depending on whether a HydroBaseDMI connection has been made.
	*/
	public void checkHydroBaseFeatures ( JMenuItem File_Properties_HydroBase_JMenuItem ) {
	 	String routine = getClass().getSimpleName() + ".ui_CheckHydroBaseFeatures";
	 	HydroBaseDMI hbdmi = getHydroBaseDMILegacy();
	 	
	 	SimpleJComboBox inputType_JComboBox = this.tstoolJFrame.ui_GetInputTypeJComboBox();

	 	Message.printStatus(2, routine, "In check, connected=" + hbdmi.connected() + " isOpen=" + hbdmi.isOpen() );
	 	if ( (hbdmi != null) && hbdmi.isOpen() ) {
	 		Message.printStatus ( 2, routine, "HydroBase connection is available... adding its features to UI..." );
	 		if ( inputType_JComboBox != null ) {
	 			// Make sure that HydroBase is in the input type list.
	 			int count = inputType_JComboBox.getItemCount();
	 			boolean hbfound = false;
	 			for ( int i = 0; i < count; i++ ) {
	 				if ( inputType_JComboBox.getItem(i).equals(TSToolConstants.INPUT_TYPE_HydroBase) ) {
	 					hbfound = true;
	 					break;
	 				}
	 			}
	 			if ( !hbfound ) {
	 				// Repopulate the input types.
	 				this.tstoolJFrame.ui_SetInputTypeChoices();
	 			}
	 		}
	 		JGUIUtil.setEnabled (File_Properties_HydroBase_JMenuItem, true );
	 	}
	 	else {
	 		// Remove HydroBase from the input type list if necessary.
	 		Message.printStatus ( 2, routine,
	 				"HydroBase connection is not available... removing its features to UI..." );
	 		try {
	 			inputType_JComboBox.remove ( TSToolConstants.INPUT_TYPE_HydroBase);
	 		}
	 		catch ( Exception e ) {
	 			// Ignore - probably already removed.
	 		}
	 		JGUIUtil.setEnabled ( File_Properties_HydroBase_JMenuItem, false );
	 	}
	}

	/**
	 * Handle data type selection.
	 */
	public void dataTypeSelected ( DataStore selectedDataStore, String selectedDataType ) {
		SimpleJComboBox timeStep_JComboBox = this.tstoolJFrame.ui_GetTimeStepJComboBox();
		
	    HydroBaseDMI hbdmi = null;
	    if ( selectedDataStore != null ) {
	        HydroBaseDataStore ds = (HydroBaseDataStore)selectedDataStore;
	        hbdmi = (HydroBaseDMI)ds.getDMI();
	    }
	    else {
	        hbdmi = getHydroBaseDMILegacy();
	    }
	    List<String> time_steps = HydroBase_Util.getTimeSeriesTimeSteps (hbdmi,
	        selectedDataType,
			HydroBase_Util.DATA_TYPE_AGRICULTURE |
			HydroBase_Util.DATA_TYPE_DEMOGRAPHICS_ALL |
			HydroBase_Util.DATA_TYPE_HARDWARE |
			HydroBase_Util.DATA_TYPE_STATION_ALL |
			HydroBase_Util.DATA_TYPE_STRUCTURE_ALL );
		timeStep_JComboBox.setData ( time_steps );
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
		// If the data type is for a diversion or reservoir data type,
		// hide the abbreviation column in the table model.  Else show the column.
    }

	/**
	Return the legacy HydroBaseDataStore instance that is active for the UI,
	opened from the configuration file information or the HydroBase select dialog.
	@return the HydroBaseDataStore instance that is active for the UI.
	*/
	public HydroBaseDataStore getHydroBaseDataStoreLegacy () {
		return this.hbDataStoreLegacy;
	}

	/**
	Return the legacy HydroBaseDMI instance that is active for the UI.
	@return the legacy HydroBaseDMI instance that is active for the UI, or null if not selected.
	*/
	public HydroBaseDMI getHydroBaseDMILegacy () {
		HydroBaseDMI dmi = null;
		HydroBaseDataStore ds = getHydroBaseDataStoreLegacy();
		if ( ds != null ) {
			if ( ds.getDMI() != null ) {
				dmi = (HydroBaseDMI)ds.getDMI();
			}
		}
		return dmi;
	}
	
	/**
	 * Handle setting the input filter.
	 */
	public InputFilter_JPanel setInputFilterForSelections ( String selectedDataType, String selectedTimeStep,
		InputFilter_JPanel inputFilterGeneric_JPanel ) {
		String routine = getClass().getSimpleName() + ".setInputFilterForSelections";
        // Legacy HydroBaseDMI input type/name.
		// Can only use the HydroBase filters if they were originally set up (if HydroBase was originally available).
        // The following lookups are currently hard coded and not read from HydroBase.
		String [] hb_mt = HydroBase_Util.convertToHydroBaseMeasType( selectedDataType, selectedTimeStep );
		String meas_type = hb_mt[0];
		//String vax_field = hb_mt[1];
		//String time_step = hb_mt[2];
		InputFilter_JPanel selectedInputFilter_JPanel = null;
		if ( getHydroBaseDataStoreLegacy() != null ) {
    		Message.printStatus(2, routine, "isStationTimeSeriesDataType("+ selectedDataType
    			+ "," + selectedTimeStep + "," + meas_type +
    			")=" + HydroBase_Util.isStationTimeSeriesDataType ( getHydroBaseDMILegacy(), meas_type));
		}
		if ( getHydroBaseDataStoreLegacy() == null ) {
		    // Display a message in the input filter panel area that a database connection needs to be made.
		    selectedInputFilter_JPanel = this.tstoolJFrame.ui_GetInputFilterMessageJPanel (
		        "HydroBase connection is not available.\nUse File...Open...HydroBase.");
		}
		else if ( (this.inputFilterHydroBaseStation_JPanel != null) &&
		    HydroBase_Util.isStationTimeSeriesDataType ( getHydroBaseDMILegacy(), meas_type) ) {
			selectedInputFilter_JPanel = this.inputFilterHydroBaseStation_JPanel;
		}
		// Call this before the more general isStructureTimeSeriesDataType() method.
		else if ( (this.inputFilterHydroBaseStructureSfut_JPanel != null) &&
		    HydroBase_Util.isStructureSFUTTimeSeriesDataType ( getHydroBaseDMILegacy(), meas_type) ) {
			selectedInputFilter_JPanel = this.inputFilterHydroBaseStructureSfut_JPanel;
		}
		else if ( (this.inputFilterHydroBaseStructure_JPanel != null) &&
		    HydroBase_Util.isStructureTimeSeriesDataType ( getHydroBaseDMILegacy(), meas_type) ) {
			selectedInputFilter_JPanel = this.inputFilterHydroBaseStructure_JPanel;
		}
		else if ((this.inputFilterHydroBaseCASSCropStats_JPanel != null)
			&& HydroBase_Util.isAgriculturalCASSCropStatsTimeSeriesDataType ( getHydroBaseDMILegacy(), selectedDataType) ) {
			//Message.printStatus (2, "","Displaying CASS crop stats panel");
			selectedInputFilter_JPanel = this.inputFilterHydroBaseCASSCropStats_JPanel;
		}
		else if ((this.inputFilterHydroBaseCASSLivestockStats_JPanel != null) &&
		    HydroBase_Util.isAgriculturalCASSLivestockStatsTimeSeriesDataType ( getHydroBaseDMILegacy(), selectedDataType) ) {
			//Message.printStatus (2, "","Displaying CASS livestock stats panel");
			selectedInputFilter_JPanel = this.inputFilterHydroBaseCASSLivestockStats_JPanel;
		}
		else if ((this.inputFilterHydroBaseCUPopulation_JPanel != null) &&
		    HydroBase_Util.isCUPopulationTimeSeriesDataType ( getHydroBaseDMILegacy(), selectedDataType) ) {
			//Message.printStatus (2, "","Displaying CU population panel");
			selectedInputFilter_JPanel = this.inputFilterHydroBaseCUPopulation_JPanel;
		}
		else if ( (this.inputFilterHydroBaseNASS_JPanel != null) &&
			HydroBase_Util.isAgriculturalNASSCropStatsTimeSeriesDataType ( getHydroBaseDMILegacy(), selectedDataType) ) {
			//Message.printStatus (2, "","Displaying NASS agstats panel");
			selectedInputFilter_JPanel = this.inputFilterHydroBaseNASS_JPanel;
		}
		else if ( (this.inputFilterHydroBaseIrrigts_JPanel != null) &&
			HydroBase_Util.isIrrigSummaryTimeSeriesDataType ( getHydroBaseDMILegacy(), selectedDataType) ) {
			selectedInputFilter_JPanel = this.inputFilterHydroBaseIrrigts_JPanel;
		}
		else if ((this.inputFilterHydroBaseWells_JPanel != null)
		    && HydroBase_Util.isGroundWaterWellTimeSeriesDataType( getHydroBaseDMILegacy(), selectedDataType)) {
			if (selectedTimeStep.equals(TSToolConstants.TIMESTEP_IRREGULAR)) {
				selectedInputFilter_JPanel = this.inputFilterHydroBaseStation_JPanel;
			}
			else {
				selectedInputFilter_JPanel = this.inputFilterHydroBaseWells_JPanel;
			}
		}		
		else {
            // Generic input filter does not have anything.
			selectedInputFilter_JPanel = inputFilterGeneric_JPanel;
		}
		return selectedInputFilter_JPanel;
	}

	/**
	 * Get the singleton instance.
	 */
	public static TSTool_HydroBase getInstance ( TSTool_JFrame tstoolJFrame ) {
		if ( TSTool_HydroBase.instance == null ) {
			TSTool_HydroBase.instance = new TSTool_HydroBase( tstoolJFrame );
		}
		return TSTool_HydroBase.instance;
	}

	/**
	Query HydroBase time series and list in the GUI, using the current selections.
	@param grlimits If a query is being executed from the map interface, then the limits will be non-null.
	@throws Exception if there is an error.
	*/
	public void getTimeSeriesListClicked_ReadHydroBaseCatalog ( GRLimits grlimits, InputFilter_JPanel selectedInputFilter_JPanel, PropList tstoolProps )
	throws Exception {
		String message, routine = getClass().getSimpleName() + ".getTimeSeriesClicked_ReadHydroBaseCatalog";

		JGUIUtil.setWaitCursor ( this.tstoolJFrame, true );
		String selectedDataType = this.tstoolJFrame.ui_GetSelectedDataType();
		String selectedTimeStep = this.tstoolJFrame.ui_GetSelectedTimeStep();

		// Object type in list varies.
		int size = 0;
		DataStore dataStore = this.tstoolJFrame.ui_GetSelectedDataStore (); // Will be null if using HydroBase input type.
		HydroBaseDMI hbdmi = null;
		if ( dataStore == null ) {
			Message.printStatus ( 1, routine, "Please wait... retrieving data from HydroBase input type...");
			hbdmi = getHydroBaseDMILegacy(); // From input type - legacy.
		}
		else {
			Message.printStatus ( 1, routine, "Please wait... retrieving data from HydroBase datastore...");
			HydroBaseDataStore hydroBaseDataStore = (HydroBaseDataStore)dataStore;
			hbdmi = (HydroBaseDMI)hydroBaseDataStore.getDMI();
		}
		InputFilter_JPanel selectedInputFilterJPanel = selectedInputFilter_JPanel;
		// Convert TSTool conventions to HydroBase conventions.
		String [] hb_mt = HydroBase_Util.convertToHydroBaseMeasType ( selectedDataType, selectedTimeStep );
		String meas_type = hb_mt[0];
		String hbtime_step = hb_mt[2];
		try {
			// List the time series by major category:
			// - old code had this logic in HydroBase_Util and returned List (no generics)
			// - new code calls specific HydroBaseUtil code in order to cleanly implement generics
			// Specific objects are read from HydroBase based on the data type.
			if ( HydroBase_Util.isAgriculturalCASSCropStatsTimeSeriesDataType ( hbdmi, selectedDataType ) ) {
				// Data from agricultural_CASS_crop_statistics.
				List<HydroBase_AgriculturalCASSCropStats>dataList = hbdmi.readAgriculturalCASSCropStatsList (
						selectedInputFilterJPanel,
						null,		// county
						null,		// commodity
						null,		// practice
						null,		// date1
						null,		// date2,
						true );		// Distinct
				size = dataList.size();
				if ( size > 0 ) {
					Message.printStatus ( 1, routine, "" + size + " HydroBase CASS crop statistics time series were read.  Displaying data..." );
					JWorksheet query_JWorksheet = tstoolJFrame.ui_GetTimeSeriesCatalogWorksheet();
					JWorksheet_AbstractRowTableModel query_TableModel = new
							TSTool_HydroBase_Ag_CASS_TableModel ( query_JWorksheet, dataList, selectedDataType );
					this.tstoolJFrame.ui_SetTimeSeriesCatalogTableModel ( query_TableModel );
					TSTool_HydroBase_Ag_CASS_CellRenderer cr = new
							TSTool_HydroBase_Ag_CASS_CellRenderer((TSTool_HydroBase_Ag_CASS_TableModel)query_TableModel);
					query_JWorksheet.setCellRenderer ( cr );
					query_JWorksheet.setModel(query_TableModel);
					query_JWorksheet.setColumnWidths ( cr.getColumnWidths(), this.tstoolJFrame.getGraphics() );
				}
			}
			else if ( HydroBase_Util.isAgriculturalNASSCropStatsTimeSeriesDataType ( hbdmi, selectedDataType ) ) {
				// Data from agricultural_NASS_crop_statistics.
				List<HydroBase_AgriculturalNASSCropStats>dataList = hbdmi.readAgriculturalNASSCropStatsList (
						selectedInputFilterJPanel,
						null,		// county
						null,		// commodity
						null,		// date1
						null,		// date2,
						true );		// Distinct
				size = dataList.size();
				if ( size > 0 ) {
					Message.printStatus ( 1, routine, "" + size + " HydroBase NASS crop statistics time series were read.  Displaying data..." );
					JWorksheet query_JWorksheet = tstoolJFrame.ui_GetTimeSeriesCatalogWorksheet();
					JWorksheet_AbstractRowTableModel query_TableModel = new
							TSTool_HydroBase_Ag_NASS_TableModel ( query_JWorksheet, dataList, selectedDataType );
					this.tstoolJFrame.ui_SetTimeSeriesCatalogTableModel ( query_TableModel );
					TSTool_HydroBase_Ag_NASS_CellRenderer cr = new
							TSTool_HydroBase_Ag_NASS_CellRenderer((TSTool_HydroBase_Ag_NASS_TableModel)query_TableModel);
					query_JWorksheet.setCellRenderer ( cr );
					query_JWorksheet.setModel(query_TableModel);
					query_JWorksheet.setColumnWidths ( cr.getColumnWidths(), this.tstoolJFrame.getGraphics() );
				}
			}
			else if (HydroBase_Util.isAgriculturalCASSLivestockStatsTimeSeriesDataType ( hbdmi, selectedDataType) ) {
				// Data from CASS livestock stats.
				List<HydroBase_AgriculturalCASSLivestockStats> dataList = hbdmi.readAgriculturalCASSLivestockStatsList (
						selectedInputFilterJPanel,	// From input filter.
						null,		// county
						null,		// commodity
						null,		// type
						null,		// date1
						null,		// date2,
						true );		// Distinct
				size = dataList.size();
				if ( size > 0 ) {
					Message.printStatus ( 1, routine, "" + size + " HydroBase CASS livestock statistics time series were read.  Displaying data..." );
					JWorksheet query_JWorksheet = tstoolJFrame.ui_GetTimeSeriesCatalogWorksheet();
					JWorksheet_AbstractRowTableModel query_TableModel = new
							TSTool_HydroBase_CASSLivestockStats_TableModel ( query_JWorksheet, dataList, selectedDataType );
					this.tstoolJFrame.ui_SetTimeSeriesCatalogTableModel ( query_TableModel );
					TSTool_HydroBase_CASSLivestockStats_CellRenderer cr = new
							TSTool_HydroBase_CASSLivestockStats_CellRenderer(
									(TSTool_HydroBase_CASSLivestockStats_TableModel)query_TableModel);
					query_JWorksheet.setCellRenderer ( cr );
					query_JWorksheet.setModel(query_TableModel);
					query_JWorksheet.setColumnWidths ( cr.getColumnWidths(), this.tstoolJFrame.getGraphics() );
				}
			}
			else if ( HydroBase_Util.isCUPopulationTimeSeriesDataType( hbdmi, selectedDataType) ) {
				List<HydroBase_CUPopulation> dataList = hbdmi.readCUPopulationList (
						selectedInputFilterJPanel,	// From input filter.
						null,		// county
						null,		// commodity
						null,		// type
						null,		// date1
						null,		// date2,
						true );		// Distinct
				size = dataList.size();
				if ( size > 0 ) {
					Message.printStatus ( 1, routine, "" + size + " HydroBase human population time series were read.  Displaying data..." );
					// Data from CUPopulation.
					JWorksheet query_JWorksheet = tstoolJFrame.ui_GetTimeSeriesCatalogWorksheet();
					JWorksheet_AbstractRowTableModel query_TableModel = new
							TSTool_HydroBase_CUPopulation_TableModel ( query_JWorksheet, dataList, selectedDataType );
					this.tstoolJFrame.ui_SetTimeSeriesCatalogTableModel ( query_TableModel );
					TSTool_HydroBase_CUPopulation_CellRenderer cr =	new
							TSTool_HydroBase_CUPopulation_CellRenderer(
									(TSTool_HydroBase_CUPopulation_TableModel)query_TableModel);
					query_JWorksheet.setCellRenderer ( cr );
					query_JWorksheet.setModel(query_TableModel);
					query_JWorksheet.setColumnWidths ( cr.getColumnWidths(), this.tstoolJFrame.getGraphics() );
				}
			}
			else if ( HydroBase_Util.isIrrigSummaryTimeSeriesDataType( hbdmi, selectedDataType ) ) {
				List<HydroBase_StructureIrrigSummaryTS> dataList = HydroBase_Util.readStructureIrrigSummaryTSCatalogList(
						hbdmi,
						selectedInputFilterJPanel,
						null,	// orderby
						HydroBase_Util.MISSING_INT,	// structure_num
						HydroBase_Util.MISSING_INT,	// wd
						HydroBase_Util.MISSING_INT,	// id
						null,	// str_name
						null,	// landuse
						null,	// start
						null,	// end
						true);	// distinct
				size = dataList.size();
				if ( size > 0 ) {
					Message.printStatus ( 1, routine, "" + size + " HydroBase structure irrigation summary time series were read.  Displaying data..." );
					// Irrig summary TS.
					JWorksheet query_JWorksheet = tstoolJFrame.ui_GetTimeSeriesCatalogWorksheet();
					JWorksheet_AbstractRowTableModel query_TableModel = new
							TSTool_HydroBase_AgGIS_TableModel (	query_JWorksheet, dataList, selectedDataType,
									StringUtil.atoi(tstoolProps.getValue( "HydroBase.WDIDLength")) );
					this.tstoolJFrame.ui_SetTimeSeriesCatalogTableModel ( query_TableModel );
					TSTool_HydroBase_AgGIS_CellRenderer cr = new
							TSTool_HydroBase_AgGIS_CellRenderer( (TSTool_HydroBase_AgGIS_TableModel)query_TableModel);
					query_JWorksheet.setCellRenderer ( cr );
					query_JWorksheet.setModel(query_TableModel);
					query_JWorksheet.setColumnWidths ( cr.getColumnWidths(), this.tstoolJFrame.getGraphics() );
				}
			}	
			else if ( HydroBase_Util.isStationTimeSeriesDataType(hbdmi, meas_type) ) {
				List<HydroBase_StationGeolocMeasType> dataList = HydroBase_Util.readStationGeolocMeasTypeCatalogList(
						hbdmi, selectedInputFilterJPanel, selectedDataType, selectedTimeStep, grlimits );
				size = dataList.size();
				if ( size > 0 ) {
					Message.printStatus ( 1, routine, "" + size + " HydroBase station time series series were read.  Displaying data..." );
					JWorksheet_DefaultTableCellRenderer cr = null;
					JWorksheet query_JWorksheet = tstoolJFrame.ui_GetTimeSeriesCatalogWorksheet();
					JWorksheet_AbstractRowTableModel query_TableModel = new TSTool_HydroBase_StationGeolocMeasType_TableModel(
							query_JWorksheet, dataList );
					this.tstoolJFrame.ui_SetTimeSeriesCatalogTableModel ( query_TableModel );
					cr = new TSTool_HydroBase_StationGeolocMeasType_CellRenderer(
							(TSTool_HydroBase_StationGeolocMeasType_TableModel)query_TableModel);
					query_JWorksheet.setCellRenderer ( cr );
					query_JWorksheet.setModel(query_TableModel);
					query_JWorksheet.setColumnWidths ( cr.getColumnWidths(), this.tstoolJFrame.getGraphics() );
				}
			}
			else if ( HydroBase_Util.isStructureTimeSeriesDataType(hbdmi, meas_type) ) {
				List<HydroBase_StructureGeolocStructMeasType> dataList = hbdmi.readStructureGeolocStructMeasTypeCatalogList(
						selectedInputFilterJPanel, selectedDataType, selectedTimeStep);
				size = dataList.size();
				if ( size > 0 ) {
					Message.printStatus ( 1, routine, "" + size + " HydroBase structure time series were read.  Displaying list..." );
					JWorksheet_DefaultTableCellRenderer cr = null;
					JWorksheet query_JWorksheet = tstoolJFrame.ui_GetTimeSeriesCatalogWorksheet();
					JWorksheet_AbstractRowTableModel query_TableModel = new TSTool_HydroBase_StructureGeolocStructMeasType_TableModel (
							query_JWorksheet, StringUtil.atoi(tstoolProps.getValue( "HydroBase.WDIDLength")), dataList );
					this.tstoolJFrame.ui_SetTimeSeriesCatalogTableModel ( query_TableModel );
					cr = new TSTool_HydroBase_StructureGeolocStructMeasType_CellRenderer(
							(TSTool_HydroBase_StructureGeolocStructMeasType_TableModel)query_TableModel);
					query_JWorksheet.setCellRenderer ( cr );
					query_JWorksheet.setModel(query_TableModel);
					query_JWorksheet.setColumnWidths ( cr.getColumnWidths(), this.tstoolJFrame.getGraphics() );
				}
			}
			else if (selectedDataType.equalsIgnoreCase( "WellLevel") || selectedDataType.equalsIgnoreCase( "WellLevelElev")||
					selectedDataType.equalsIgnoreCase( "WellLevelDepth") ) {
				// Well level data.
				if (selectedTimeStep.equalsIgnoreCase("Day")) {
					List<HydroBase_GroundWaterWellsView> dataList =
							HydroBase_Util.readGroundWaterWellsViewTSCatalogList(hbdmi, selectedInputFilterJPanel, meas_type, hbtime_step);
					size = dataList.size();
					if ( size > 0 ) {
						JWorksheet query_JWorksheet = tstoolJFrame.ui_GetTimeSeriesCatalogWorksheet();
						JWorksheet_AbstractRowTableModel query_TableModel = new TSTool_HydroBase_WellLevel_Day_TableModel (
								query_JWorksheet, StringUtil.atoi( tstoolProps.getValue("HydroBase.WDIDLength")), dataList,
								"HydroBase" );
						this.tstoolJFrame.ui_SetTimeSeriesCatalogTableModel ( query_TableModel );
						TSTool_HydroBase_WellLevel_Day_CellRenderer cr =
								new TSTool_HydroBase_WellLevel_Day_CellRenderer(
										(TSTool_HydroBase_WellLevel_Day_TableModel)query_TableModel);
						query_JWorksheet.setCellRenderer ( cr );
						query_JWorksheet.setModel(query_TableModel);
						query_JWorksheet.setColumnWidths ( cr.getColumnWidths(), this.tstoolJFrame.getGraphics() );
					}
				}
				else {
					// Real-time well elevation as station.
					// TODO smalers 2019-06-02 evaluate if this is working (is it handled by the general station code?).
					List<HydroBase_StationGeolocMeasType> dataList = new ArrayList<>();
					size = dataList.size();
					if ( size > 0 ) {
						JWorksheet query_JWorksheet = tstoolJFrame.ui_GetTimeSeriesCatalogWorksheet();
						JWorksheet_AbstractRowTableModel query_TableModel =
							new TSTool_HydroBase_StationGeolocMeasType_TableModel ( query_JWorksheet, dataList );
						this.tstoolJFrame.ui_SetTimeSeriesCatalogTableModel ( query_TableModel );
						TSTool_HydroBase_StationGeolocMeasType_CellRenderer cr =
								new TSTool_HydroBase_StationGeolocMeasType_CellRenderer(
										(TSTool_HydroBase_StationGeolocMeasType_TableModel)query_TableModel);
						query_JWorksheet.setCellRenderer ( cr );
						query_JWorksheet.setModel(query_TableModel);
						// TODO smalers 2012-09-05 Verify that Abbrev should be on since station.
						// Turn off columns in the table model that do not apply.
						//__query_JWorksheet.removeColumn ( ((TSTool_HydroBase_StationGeolocMeasType_TableModel)__query_TableModel).COL_ABBREV );
						query_JWorksheet.setColumnWidths ( cr.getColumnWidths(), this.tstoolJFrame.getGraphics() );
					}
				}
			}
			if ( size == 0 ) {
				Message.printStatus ( 1, routine, "No HydroBase time series were read." );
				this.tstoolJFrame.queryResultsList_Clear ();
			}
			JGUIUtil.setWaitCursor ( this.tstoolJFrame, false );
		}
		catch ( Exception e ) {
			message = "Error reading time series list from HydroBase (" + e + ").";
			Message.printWarning ( 2, routine, message );
			Message.printWarning ( 2, routine, e );
			JGUIUtil.setWaitCursor ( this.tstoolJFrame, false );
			throw new Exception ( message );
		}
	}

	/**
	Initialize the HydroBase input filter (may be called at startup after login or File...Open HydroBase).
	@param dataStoreLegacy legacy HydroBaseDataStore that corresponds to the legacy configuration file
	or File...Open HydroBase.
	@param y y-coordinate in the filter panel to add the input filter
	*/
	public void initGUIInputFiltersLegacy ( HydroBaseDataStore dataStoreLegacy, int y ) {
		String routine = getClass().getSimpleName() + "initGUIInputFiltersLegacy";
		int buffer = 3;
		Insets insets = new Insets(0,buffer,0,0);

		// If the an instance of a panel is not null, remove it from the list and then recreate it.

		Message.printStatus ( 2, routine, "Initializing input filter(s) for legacy HydroBase DMI..." );

		List<InputFilter_JPanel> inputFilterJPanelList = this.tstoolJFrame.ui_GetInputFilterJPanelList();
		JPanel queryInput_JPanel = this.tstoolJFrame.ui_GetQueryInputJPanel();

		// Add input filters for stations.

		try {
			if ( inputFilterHydroBaseStation_JPanel != null ) {
				inputFilterJPanelList.remove (this.inputFilterHydroBaseStation_JPanel );
			}
			inputFilterHydroBaseStation_JPanel = new HydroBase_GUI_StationGeolocMeasType_InputFilter_JPanel( getHydroBaseDataStoreLegacy() );
			JGUIUtil.addComponent(queryInput_JPanel, this.inputFilterHydroBaseStation_JPanel,
					0, y, 3, 1, 1.0, 0.0, insets, GridBagConstraints.HORIZONTAL,
					GridBagConstraints.WEST );
			inputFilterHydroBaseStation_JPanel.setName ( "HydroBase.StationInputFilterPanel.Legacy");
			inputFilterJPanelList.add (this.inputFilterHydroBaseStation_JPanel );
		}
		catch ( Exception e ) {
			Message.printWarning ( 2, routine, "Unable to initialize input filter for HydroBase stations." );
			Message.printWarning ( 2, routine, e );
		}

		// Add input filters for structures - there is one panel for
		// "total" time series and one for water class time series that can be filtered by SFUT.

		try {
			if ( this.inputFilterHydroBaseStructure_JPanel != null ) {
				inputFilterJPanelList.remove ( this.inputFilterHydroBaseStructure_JPanel );
			}
			this.inputFilterHydroBaseStructure_JPanel = new
					HydroBase_GUI_StructureGeolocStructMeasType_InputFilter_JPanel( getHydroBaseDataStoreLegacy(), false );
			JGUIUtil.addComponent(queryInput_JPanel, this.inputFilterHydroBaseStructure_JPanel,
					0, y, 3, 1, 1.0, 0.0, insets, GridBagConstraints.HORIZONTAL,
					GridBagConstraints.WEST );
			this.inputFilterHydroBaseStructure_JPanel.setName ( "HydroBase.StructureInputFilterPanel.Legacy" );
			inputFilterJPanelList.add ( this.inputFilterHydroBaseStructure_JPanel );
		}
		catch ( Exception e ) {
			Message.printWarning ( 2, routine, "Unable to initialize input filter for HydroBase structures." );
			Message.printWarning ( 2, routine, e );
		}

		try {
			if ( this.inputFilterHydroBaseStructureSfut_JPanel != null ) {
				inputFilterJPanelList.remove ( this.inputFilterHydroBaseStructureSfut_JPanel);
			}
			this.inputFilterHydroBaseStructureSfut_JPanel = new
					HydroBase_GUI_StructureGeolocStructMeasType_InputFilter_JPanel( getHydroBaseDataStoreLegacy(), true );
			JGUIUtil.addComponent(queryInput_JPanel, this.inputFilterHydroBaseStructureSfut_JPanel,
					0, y, 3, 1, 1.0, 0.0, insets, GridBagConstraints.HORIZONTAL,
					GridBagConstraints.WEST );
			this.inputFilterHydroBaseStructureSfut_JPanel.setName("HydroBase.StructureSFUTInputFilterPanel.Legacy");
			inputFilterJPanelList.add ( this.inputFilterHydroBaseStructureSfut_JPanel);
		}
		catch ( Exception e ) {
			Message.printWarning ( 2, routine,
					"Unable to initialize input filter for HydroBase structures with SFUT." );
			Message.printWarning ( 2, routine, e );
		}

		// Add input filters for structure irrig_summary_ts.

		try {
			if ( this.inputFilterHydroBaseIrrigts_JPanel != null ) {
				inputFilterJPanelList.remove ( this.inputFilterHydroBaseIrrigts_JPanel );
			}
			inputFilterHydroBaseIrrigts_JPanel = new
					HydroBase_GUI_StructureIrrigSummaryTS_InputFilter_JPanel( getHydroBaseDataStoreLegacy() );
			JGUIUtil.addComponent(queryInput_JPanel, this.inputFilterHydroBaseIrrigts_JPanel,
					0, y, 3, 1, 1.0, 0.0, insets, GridBagConstraints.HORIZONTAL,
					GridBagConstraints.WEST );
			this.inputFilterHydroBaseIrrigts_JPanel.setName ( "HydroBase.IrrigationSummaryInputFilterPanel.Legacy");
			inputFilterJPanelList.add ( this.inputFilterHydroBaseIrrigts_JPanel );
		}
		catch ( Exception e ) {
			Message.printWarning ( 2, routine,
					"Unable to initialize input filter for HydroBase irrigation summary time series - old database?" );
			Message.printWarning ( 2, routine, e );
		}

		// Add input filters for CASS agricultural crop statistics, only available for newer databases.
		// For now, just catch an exception when not supported.

		try {
			if ( this.inputFilterHydroBaseCASSCropStats_JPanel != null ) {
				inputFilterJPanelList.remove ( this.inputFilterHydroBaseCASSCropStats_JPanel );
			}
			this.inputFilterHydroBaseCASSCropStats_JPanel = new
					HydroBase_GUI_AgriculturalCASSCropStats_InputFilter_JPanel ( getHydroBaseDataStoreLegacy() );
			JGUIUtil.addComponent(queryInput_JPanel, this.inputFilterHydroBaseCASSCropStats_JPanel,
					0, y, 3, 1, 1.0, 0.0, insets, GridBagConstraints.HORIZONTAL,
					GridBagConstraints.WEST );
			this.inputFilterHydroBaseCASSCropStats_JPanel.setName("HydroBase.CASSCropsInputFilterPanel.Legacy");
			inputFilterJPanelList.add ( this.inputFilterHydroBaseCASSCropStats_JPanel );
		}
		catch ( Exception e ) {
			// Agricultural_CASS_crop_stats probably not in HydroBase.
			Message.printWarning ( 2, routine,
					"Unable to initialize input filter for HydroBase CASS crop statistics - old database?" );
			Message.printWarning ( 2, routine, e );
		}

		// Add input filters for CASS agricultural livestock statistics, only available for newer databases.
		// For now, just catch an exception when not supported.

		try {
			if ( this.inputFilterHydroBaseCASSLivestockStats_JPanel != null ) {
				inputFilterJPanelList.remove ( this.inputFilterHydroBaseCASSLivestockStats_JPanel );
			}
			this.inputFilterHydroBaseCASSLivestockStats_JPanel = new
					HydroBase_GUI_AgriculturalCASSLivestockStats_InputFilter_JPanel ( getHydroBaseDataStoreLegacy() );
			JGUIUtil.addComponent(queryInput_JPanel, this.inputFilterHydroBaseCASSLivestockStats_JPanel,
					0, y, 3, 1, 1.0, 0.0, insets, GridBagConstraints.HORIZONTAL,
					GridBagConstraints.WEST );
			this.inputFilterHydroBaseCASSLivestockStats_JPanel.setName("HydroBase.CASSLivestockInputFilterPanel.Legacy");
			inputFilterJPanelList.add ( this.inputFilterHydroBaseCASSLivestockStats_JPanel );
		}
		catch ( Exception e ) {
			// Agricultural_CASS_livestock_stats probably not in HydroBase.
			Message.printWarning ( 2, routine,
					"Unable to initialize input filter for HydroBase CASS livestock statistics - old database?" );
			Message.printWarning ( 2, routine, e );
		}

		// Add input filters for CU population data, only available for newer databases.
		// For now, just catch an exception when not supported.

		try {
			if ( this.inputFilterHydroBaseCUPopulation_JPanel != null ) {
				inputFilterJPanelList.remove ( this.inputFilterHydroBaseCUPopulation_JPanel );
			}
			this.inputFilterHydroBaseCUPopulation_JPanel = new
					HydroBase_GUI_CUPopulation_InputFilter_JPanel( getHydroBaseDataStoreLegacy() );
			JGUIUtil.addComponent(queryInput_JPanel, this.inputFilterHydroBaseCUPopulation_JPanel,
					0, y, 3, 1, 1.0, 0.0, insets, GridBagConstraints.HORIZONTAL,
					GridBagConstraints.WEST );
			this.inputFilterHydroBaseCUPopulation_JPanel.setName("HydroBase.CUPopulationInputFilterPanel.Legacy");
			inputFilterJPanelList.add ( this.inputFilterHydroBaseCUPopulation_JPanel );
		}
		catch ( Exception e ) {
			// CUPopulation probably not in HydroBase.
			Message.printWarning ( 2, routine,
					"Unable to initialize input filter for HydroBase CU population data - old database?" );
			Message.printWarning ( 2, routine, e );
		}

		// Add input filters for NASS agricultural statistics, only available for newer databases.
		// For now, just catch an exception when not supported.

		try {
			if ( this.inputFilterHydroBaseNASS_JPanel != null ) {
				inputFilterJPanelList.remove ( this.inputFilterHydroBaseNASS_JPanel );
			}
			this.inputFilterHydroBaseNASS_JPanel = new
					HydroBase_GUI_AgriculturalNASSCropStats_InputFilter_JPanel ( getHydroBaseDataStoreLegacy() );
			JGUIUtil.addComponent(queryInput_JPanel, this.inputFilterHydroBaseNASS_JPanel,
					0, y, 3, 1, 1.0, 0.0, insets, GridBagConstraints.HORIZONTAL,
					GridBagConstraints.WEST );
			this.inputFilterHydroBaseNASS_JPanel.setName("HydroBase.NASSInputFilterPanel.Legacy");
			inputFilterJPanelList.add ( this.inputFilterHydroBaseNASS_JPanel );
		}
		catch ( Exception e ) {
			// Agricultural_NASS_crop_stats probably not in HydroBase.
			Message.printWarning ( 2, routine,
					"Unable to initialize input filter for HydroBase agricultural_NASS_crop_stats - old database?" );
			Message.printWarning ( 2, routine, e );
		}

		try {
			if ( this.inputFilterHydroBaseWells_JPanel != null ) {
				inputFilterJPanelList.remove( this.inputFilterHydroBaseWells_JPanel);
			}
			inputFilterHydroBaseWells_JPanel =
					new HydroBase_GUI_GroundWater_InputFilter_JPanel ( getHydroBaseDataStoreLegacy(), null, true);
			JGUIUtil.addComponent(queryInput_JPanel,this.inputFilterHydroBaseWells_JPanel,
					0, y, 3, 1, 1.0, 0.0, insets, GridBagConstraints.HORIZONTAL,
					GridBagConstraints.WEST);
			inputFilterHydroBaseWells_JPanel.setName("HydroBase.WellsInputFilterPanel.Legacy");
			inputFilterJPanelList.add( this.inputFilterHydroBaseWells_JPanel);
		}
		catch ( Exception e ) {
			// Agricultural_NASS_crop_stats probably not in HydroBase.
			Message.printWarning ( 2, routine,
					"Unable to initialize input filter for HydroBase wells - old database?" );
			Message.printWarning ( 2, routine, e );
		}
	}

	/**
	Initialize the HydroBase input filters for HydroBase datastores.
	@param dataStoreList HydroBase datastores
	*/
	public void initGUIInputFilters ( List<DataStore> dataStoreList, int y ) {
		String routine = getClass().getSimpleName() + ".ui_InitGUIInputFilters";
		int buffer = 3;
		Insets insets = new Insets(0,buffer,0,0);

		List<InputFilter_JPanel> inputFilterJPanelList = this.tstoolJFrame.ui_GetInputFilterJPanelList();
		JPanel queryInput_JPanel = this.tstoolJFrame.ui_GetQueryInputJPanel();

		Message.printStatus ( 2, routine, "Initializing input filter(s) for " + dataStoreList.size() + " HydroBase datastores." );
		for ( DataStore dataStore: dataStoreList ) {
			// Add input filters for stations.
			String dsName = dataStore.getName();
			try {
				HydroBase_GUI_StationGeolocMeasType_InputFilter_JPanel panel = new
						HydroBase_GUI_StationGeolocMeasType_InputFilter_JPanel( (HydroBaseDataStore)dataStore );
				JGUIUtil.addComponent(queryInput_JPanel, panel,
						0, y, 3, 1, 1.0, 0.0, insets, GridBagConstraints.HORIZONTAL,
						GridBagConstraints.WEST );
				panel.setName ( "HydroBase." + dsName + ".StationInputFilterPanel");
				inputFilterJPanelList.add (panel );
			}
			catch ( Exception e ) {
				Message.printWarning ( 2, routine, "Unable to initialize input filter for HydroBase stations for datastore \"" +
						dataStore.getName() + "\" (" + e + ")." );
				Message.printWarning ( 2, routine, e );
			}

			// Add input filters for structures - there is one panel for
			// "total" time series and one for water class time series that can be filtered by SFUT.

			try {
				HydroBase_GUI_StructureGeolocStructMeasType_InputFilter_JPanel panel = new
						HydroBase_GUI_StructureGeolocStructMeasType_InputFilter_JPanel( (HydroBaseDataStore)dataStore, false );
				JGUIUtil.addComponent(queryInput_JPanel, panel,
						0, y, 3, 1, 1.0, 0.0, insets, GridBagConstraints.HORIZONTAL,
						GridBagConstraints.WEST );
				panel.setName ( "HydroBase." + dsName + ".StructureInputFilterPanel" );
				inputFilterJPanelList.add ( panel );
			}
			catch ( Exception e ) {
				Message.printWarning ( 2, routine, "Unable to initialize input filter for HydroBase structures for datastore \"" +
						dataStore.getName() + "\" (" + e + ")." );
				Message.printWarning ( 2, routine, e );
			}

			try {
				HydroBase_GUI_StructureGeolocStructMeasType_InputFilter_JPanel panel = new
						HydroBase_GUI_StructureGeolocStructMeasType_InputFilter_JPanel( (HydroBaseDataStore)dataStore, true );
				JGUIUtil.addComponent(queryInput_JPanel, panel,
						0, y, 3, 1, 1.0, 0.0, insets, GridBagConstraints.HORIZONTAL,
						GridBagConstraints.WEST );
				panel.setName("HydroBase." + dsName + ".StructureSFUTInputFilterPanel");
				inputFilterJPanelList.add ( panel );
			}
			catch ( Exception e ) {
				Message.printWarning ( 2, routine,
						"Unable to initialize input filter for HydroBase structures with SFUT for datastore \"" +
								dataStore.getName() + "\" (" + e + ")." );
				Message.printWarning ( 2, routine, e );
			}

			// Add input filters for structure irrig_summary_ts.

			try {
				HydroBase_GUI_StructureIrrigSummaryTS_InputFilter_JPanel panel = new
						HydroBase_GUI_StructureIrrigSummaryTS_InputFilter_JPanel( (HydroBaseDataStore)dataStore );
				JGUIUtil.addComponent(queryInput_JPanel, panel,
						0, y, 3, 1, 1.0, 0.0, insets, GridBagConstraints.HORIZONTAL,
						GridBagConstraints.WEST );
				panel.setName ( "HydroBase." + dsName + ".IrrigationSummaryInputFilterPanel");
				inputFilterJPanelList.add ( panel );
			}
			catch ( Exception e ) {
				Message.printWarning ( 2, routine,
						"Unable to initialize input filter for HydroBase irrigation summary time series for datastore \"" +
								dataStore.getName() + "\" (" + e + ")." );
				Message.printWarning ( 2, routine, e );
			}

			// Add input filters for CASS agricultural crop statistics, only available for newer databases.
			// For now, just catch an exception when not supported.

			try {
				HydroBase_GUI_AgriculturalCASSCropStats_InputFilter_JPanel panel = new
						HydroBase_GUI_AgriculturalCASSCropStats_InputFilter_JPanel ( (HydroBaseDataStore)dataStore );
				JGUIUtil.addComponent(queryInput_JPanel, panel,
						0, y, 3, 1, 1.0, 0.0, insets, GridBagConstraints.HORIZONTAL,
						GridBagConstraints.WEST );
				panel.setName("HydroBase." + dsName + ".CASSCropsInputFilterPanel");
				inputFilterJPanelList.add ( panel );
			}
			catch ( Exception e ) {
				// Agricultural_CASS_crop_stats probably not in HydroBase.
				Message.printWarning ( 2, routine,
						"Unable to initialize input filter for HydroBase CASS crop statistics for datastore \"" +
								dataStore.getName() + "\" (" + e + ")." );
				Message.printWarning ( 2, routine, e );
			}

			// Add input filters for CASS agricultural livestock statistics, only available for newer databases.
			// For now, just catch an exception when not supported.

			try {
				HydroBase_GUI_AgriculturalCASSLivestockStats_InputFilter_JPanel panel = new
						HydroBase_GUI_AgriculturalCASSLivestockStats_InputFilter_JPanel ( (HydroBaseDataStore)dataStore );
				JGUIUtil.addComponent(queryInput_JPanel, panel,
						0, y, 3, 1, 1.0, 0.0, insets, GridBagConstraints.HORIZONTAL,
						GridBagConstraints.WEST );
				panel.setName("HydroBase." + dsName + ".CASSLivestockInputFilterPanel");
				inputFilterJPanelList.add ( panel );
			}
			catch ( Exception e ) {
				// Agricultural_CASS_livestock_stats probably not in HydroBase.
				Message.printWarning ( 2, routine,
						"Unable to initialize input filter for HydroBase CASS livestock statistics for datastore \"" +
								dataStore.getName() + "\" (" + e + ")." );
				Message.printWarning ( 2, routine, e );
			}

			// Add input filters for CU population data, only available for newer databases.
			// For now, just catch an exception when not supported.

			try {
				HydroBase_GUI_CUPopulation_InputFilter_JPanel panel = new
						HydroBase_GUI_CUPopulation_InputFilter_JPanel( (HydroBaseDataStore)dataStore );
				JGUIUtil.addComponent(queryInput_JPanel, panel,
						0, y, 3, 1, 1.0, 0.0, insets, GridBagConstraints.HORIZONTAL,
						GridBagConstraints.WEST );
				panel.setName("HydroBase." + dsName + ".CUPopulationInputFilterPanel");
				inputFilterJPanelList.add ( panel );
			}
			catch ( Exception e ) {
				// CUPopulation probably not in HydroBase.
				Message.printWarning ( 2, routine,
						"Unable to initialize input filter for HydroBase CU population datastore \"" +
								dataStore.getName() + "\" (" + e + ")." );
				Message.printWarning ( 2, routine, e );
			}

			// Add input filters for NASS agricultural statistics, only available for newer databases.
			// For now, just catch an exception when not supported.

			try {
				HydroBase_GUI_AgriculturalNASSCropStats_InputFilter_JPanel panel = new
						HydroBase_GUI_AgriculturalNASSCropStats_InputFilter_JPanel ( (HydroBaseDataStore)dataStore );
				JGUIUtil.addComponent(queryInput_JPanel, panel,
						0, y, 3, 1, 1.0, 0.0, insets, GridBagConstraints.HORIZONTAL,
						GridBagConstraints.WEST );
				panel.setName("HydroBase." + dsName + ".NASSInputFilterPanel");
				inputFilterJPanelList.add ( panel );
			}
			catch ( Exception e ) {
				// Agricultural_NASS_crop_stats probably not in HydroBase.
				Message.printWarning ( 2, routine,
						"Unable to initialize input filter for HydroBase agricultural_NASS_crop_stats for datastore \"" +
								dataStore.getName() + "\" (" + e + ")." );
				Message.printWarning ( 2, routine, e );
			}

			try {
				HydroBase_GUI_GroundWater_InputFilter_JPanel panel =
						new HydroBase_GUI_GroundWater_InputFilter_JPanel ( (HydroBaseDataStore)dataStore, null, true);
				JGUIUtil.addComponent(queryInput_JPanel, panel,
						0, y, 3, 1, 1.0, 0.0, insets, GridBagConstraints.HORIZONTAL,
						GridBagConstraints.WEST);
				panel.setName("HydroBase." + dsName + ".WellsInputFilterPanel");
				inputFilterJPanelList.add( panel );
			}
			catch ( Exception e ) {
				// Agricultural_NASS_crop_stats probably not in HydroBase.
				Message.printWarning ( 2, routine,
						"Unable to initialize input filter for HydroBase wells for datastore \"" +
								dataStore.getName() + "\" (" + e + ")." );
				Message.printWarning ( 2, routine, e );
			}
		}
	}

	/**
	TODO smalers 2010-09-13 Streamline this when HydroBase is converted to a datastore from input type/name.
	Open a connection to the HydroBase database.
	@param startup if true, then the connection is being made at software startup.
	In this case if AutoConnect=True in the configuration, the dialog will not be shown.
	*/
	public void openHydroBase ( boolean startup, JMenuItem File_Properties_HydroBase_JMenuItem ) {
	 	String routine = getClass().getSimpleName() + ".uiAction_OpenHydroBase";
	 	Message.printStatus ( 1, routine, "Opening HydroBase connection..." );

	 	// Running interactively so:
	 	// 1) If AutoConnect=True, automatically connect using default info
	 	// 2) If AutoConnect=False (default), prompt the user to select and login to HydroBase.
	 	// This is a modal dialog that will not allow anything else to occur until the information is entered.
	 	// Use a PropList to pass information because there are many parameters that may change in the future.

	 	// Determine whether the database connection should be automatically made.
	 	boolean AutoConnect_boolean = false; // Default is to use the login dialog.
	 	// Get the property from the TSTool configuration file because ultimately this will be by
	 	// user and the generic CDSS information will continue to be for all users.
	 	String AutoConnect = TSToolMain.getPropValue("HydroBase.AutoConnect");
	 	if ( (AutoConnect != null) && AutoConnect.equalsIgnoreCase("true") ) {
	 		AutoConnect_boolean = true;
	 	}

	 	HydroBaseDMI hbdmi = null; // DMI that is opened by the dialog or automatically - null if cancel or error.
	 	String error = ""; // Message for whether there was an unexpected error opening HydroBase.
	 	boolean usedDialog = false;
	 	if ( startup && AutoConnect_boolean ) {
	 		Message.printStatus ( 2, routine, "HydroBase.AutoConnect=True in TSTool.cfg configuration file so " +
	 				"autoconnecting to HydroBase with default connection information from CDSS.cfg." );
	 		hbdmi = TSToolMain.openHydroBase(this.tstoolJFrame.commandProcessor_GetCommandProcessor());
	 		// Further checks are made below for UI setup.
	 	}
	 	else {
	 		// Use the login dialog.
	 		PropList hb_props = new PropList ( "SelectHydroBase" );
	 		hb_props.set ( "ValidateLogin", "false" );
	 		hb_props.set ( "ShowWaterDivisions", "false" );

	 		// Pass in the previous HydroBaseDMI so that its information can be displayed as the initial values.

	 		SelectHydroBaseJDialog selectHydroBaseJDialog = null;
	 		usedDialog = true;

	 		try {
	 			// Let the dialog check HydroBase properties in the CDSS configuration file.
	 			HydroBaseDMI dmi = null;
	 			if ( getHydroBaseDMILegacy() != null ) {
	 				dmi = getHydroBaseDMILegacy();
	 			}
	 			selectHydroBaseJDialog = new SelectHydroBaseJDialog ( this.tstoolJFrame, dmi, hb_props );
	 			// After getting to here, the dialog has been closed.
	 			// The HydroBaseDMI from the dialog can be retrieved and used.
	 			hbdmi = selectHydroBaseJDialog.getHydroBaseDMI();
	 		}
	 		catch ( Exception e ) {
	 			error = "Error opening HydroBase connection.  ";
	 			Message.printWarning ( 3, routine, e );
	 			hbdmi = null;
	 		}
	 	}
	 	// If no HydroBase connection was opened, print an appropriate message.
	 	if ( hbdmi == null ) {
	 		if ( (getHydroBaseDataStoreLegacy() == null) && usedDialog ) {
	 			// No previous connection known to the UI - print this warning if the dialog was attempted.
	 			Message.printWarning ( 1, routine, error + "HydroBase features will be disabled." );
	 		}
	 		else if ( usedDialog ) {
	 			// Had a previous connection in the UI so continue to use it.
	 			Message.printWarning ( 1, routine, error + "The previous HydroBase connection will be used." );
	 		}
	 	}
	 	else if ( hbdmi == getHydroBaseDMILegacy() ) {
	 		// Same instance was returned as original (user canceled) - no need to do anything.
	 		// TODO smalers 2008-10-23 If this step is not ignored, the GUI removes HydroBase from the interface?
	 	}
	 	else {
	 		// New connection has been established.
	 		// Set the HydroBaseDMI for the GUI and command processor.
	 		Message.printStatus(2, routine,
	 				"Using new HydroBase connection in UI (refreshing HydroBase features) and processor." );
	 		this.tstoolJFrame.commandProcessor_SetHydroBaseDMI ( hbdmi );
	 		// Set the UI instance last because setting in the processor may close the old connection and
	 		// therefore close the one referenced by the UI.
	 		setHydroBaseDataStoreLegacy ( hbdmi );
	 		// Enable/disable HydroBase features as necessary.
	 		checkHydroBaseFeatures ( File_Properties_HydroBase_JMenuItem );
	 	}
	}

	/**
	Set up the query options because the HydroBase input type has been selected.
	*/
	public void selectDataStore ( HydroBaseDataStore selectedDataStore ) {
	    String routine = getClass().getSimpleName() + ".selectDataStore";
	    // Input name cleared and disabled.
	    this.tstoolJFrame.ui_SetInputNameVisible(false); // Not needed.

	    SimpleJComboBox dataType_JComboBox = this.tstoolJFrame.ui_GetDataTypeJComboBox();
	    SimpleJComboBox inputName_JComboBox = this.tstoolJFrame.ui_GetInputNameJComboBox();

	    inputName_JComboBox.removeAll();
	    inputName_JComboBox.setEnabled ( false );
	    // Data type - get the time series choices from the HydroBase_Util code.
	    dataType_JComboBox.setEnabled ( true );
	    dataType_JComboBox.removeAll ();
	    HydroBaseDMI dmi = (HydroBaseDMI)((DatabaseDataStore)selectedDataStore).getDMI();
	    List<String> data_types =
	    		HydroBase_Util.getTimeSeriesDataTypes (dmi,
	    				HydroBase_Util.DATA_TYPE_AGRICULTURE |
	    				HydroBase_Util.DATA_TYPE_DEMOGRAPHICS_ALL |
	    				HydroBase_Util.DATA_TYPE_HARDWARE |
	    				HydroBase_Util.DATA_TYPE_STATION_ALL |
	    				HydroBase_Util.DATA_TYPE_STRUCTURE_ALL,
	    				true ); // Add notes.
	    dataType_JComboBox.setData ( data_types );

	    // Select the default (this causes the other choices to be updated).

	    dataType_JComboBox.select( null );
	    dataType_JComboBox.select(HydroBase_Util.getDefaultTimeSeriesDataType(dmi, true ) );

	    // Initialize with blank data list.
	    // TODO smalers 2012-09-05 Initialize with correct table model - for now use stations because it will get reset
	    // when data are read (different table model might confuse user).

	    try {
	    	JWorksheet query_JWorksheet = tstoolJFrame.ui_GetTimeSeriesCatalogWorksheet();
	    	JWorksheet_AbstractRowTableModel query_TableModel = new TSTool_HydroBase_StationGeolocMeasType_TableModel( query_JWorksheet,
	    			//StringUtil.atoi(__props.getValue("HydroBase.WDIDLength")),
	    			null);
			this.tstoolJFrame.ui_SetTimeSeriesCatalogTableModel ( query_TableModel );
	    	TSTool_HydroBase_StationGeolocMeasType_CellRenderer cr =
	    			new TSTool_HydroBase_StationGeolocMeasType_CellRenderer(
	    					(TSTool_HydroBase_StationGeolocMeasType_TableModel)query_TableModel);
	    	query_JWorksheet.setCellRenderer ( cr );
	    	query_JWorksheet.setModel ( query_TableModel );
	    	query_JWorksheet.setColumnWidths (cr.getColumnWidths() );
	    }
	    catch ( Exception e ) {
	    	// Absorb the exception in most cases - print if developing to see if this issue can be resolved.
	    	if ( Message.isDebugOn && IOUtil.testing()  ) {
	    		Message.printWarning ( 3, routine,
	    				"For developers:  caught exception blanking HydroBase JWorksheet at setup." );
	    		Message.printWarning ( 3, routine, e );
	    	}
	    }
	}

	/**
	Set up the query options because the HydroBase input type has been selected.
	*/
	public void selectInputType () {
	    String routine = getClass().getSimpleName() + "selectInputType";
	    // Input name cleared and disabled.
	    this.tstoolJFrame.ui_SetInputNameVisible(false); // Not needed.


	    SimpleJComboBox dataType_JComboBox = this.tstoolJFrame.ui_GetDataTypeJComboBox();
	    SimpleJComboBox inputName_JComboBox = this.tstoolJFrame.ui_GetInputNameJComboBox();

	    inputName_JComboBox.removeAll();
	    inputName_JComboBox.setEnabled ( false );
	    // Data type - get the time series choices from the HydroBase_Util code.
	    dataType_JComboBox.setEnabled ( true );
	    dataType_JComboBox.removeAll ();
	    List<String> data_types =
	    		HydroBase_Util.getTimeSeriesDataTypes ( getHydroBaseDMILegacy(),
	    				HydroBase_Util.DATA_TYPE_AGRICULTURE |
	    				HydroBase_Util.DATA_TYPE_DEMOGRAPHICS_ALL |
	    				HydroBase_Util.DATA_TYPE_HARDWARE |
	    				HydroBase_Util.DATA_TYPE_STATION_ALL |
	    				HydroBase_Util.DATA_TYPE_STRUCTURE_ALL,
	    				true ); // Add notes.
	    dataType_JComboBox.setData ( data_types );

	    // Select the default (this causes the other choices to be updated).

	    dataType_JComboBox.select( null );
	    dataType_JComboBox.select(HydroBase_Util.getDefaultTimeSeriesDataType(getHydroBaseDMILegacy(), true ) );

	    // Initialize with blank data list.
	    // TODO smalers 2012-09-05 Initialize with correct table model - for now use stations because it will get reset
	    // when data are read (different table model might confuse user).

	    try {
			JWorksheet query_JWorksheet = tstoolJFrame.ui_GetTimeSeriesCatalogWorksheet();
	    	JWorksheet_AbstractRowTableModel query_TableModel = new TSTool_HydroBase_StationGeolocMeasType_TableModel(
	    			query_JWorksheet,
	    			//StringUtil.atoi(__props.getValue("HydroBase.WDIDLength")),
	    			null);
			this.tstoolJFrame.ui_SetTimeSeriesCatalogTableModel ( query_TableModel );
	    	TSTool_HydroBase_StationGeolocMeasType_CellRenderer cr =
	    			new TSTool_HydroBase_StationGeolocMeasType_CellRenderer(
	    					(TSTool_HydroBase_StationGeolocMeasType_TableModel)query_TableModel);
	    	query_JWorksheet.setCellRenderer ( cr );
	    	query_JWorksheet.setModel ( query_TableModel );
	    	query_JWorksheet.setColumnWidths (cr.getColumnWidths() );
	    }
	    catch ( Exception e ) {
	    	// Absorb the exception in most cases - print if developing to see if this issue can be resolved.
	    	if ( Message.isDebugOn && IOUtil.testing()  ) {
	    		Message.printWarning ( 3, routine,
	    				"For developers:  caught exception blanking HydroBase JWorksheet at setup." );
	    		Message.printWarning ( 3, routine, e );
	    	}
	    }
	}

	/**
	Set the HydroBaseDMI instance used by the GUI by encapsulating in a HydroBaseDataStore instance.
	This typically is the same as the command processor HydroBaseDMI; however,
	it is possible that OpenHydroBase() commands will be used and open up different HydroBase connections during processing.
	@param hbdmi the HydroBaseDMI instance used by the GUI.
	*/
	public void setHydroBaseDataStoreLegacy ( HydroBaseDMI hbdmi ) {
		if ( hbdmi == null ) {
			this.hbDataStoreLegacy = null;
		}
		else {
			this.hbDataStoreLegacy = new HydroBaseDataStore( "HydroBase", "State of Colorado HydroBase database", hbdmi, true );
		}
	}

	/**
	 * Transfer one time series catalog row to a command.
	 * @param row the time series catalog row to transfer (0+)
	 * @param useAlias whether to use an alias
	 */
    public int transferOneTimeSeriesCatalogRowToCommands ( int row, boolean useAlias, int insertOffset,
    	JWorksheet_AbstractRowTableModel query_TableModel, String selectedDataStoreName ) {
    	int numCommandsAdded = 0;
		if ( query_TableModel instanceof TSTool_HydroBase_StationGeolocMeasType_TableModel){
		    // Station time series.
		    TSTool_HydroBase_StationGeolocMeasType_TableModel model =
		        (TSTool_HydroBase_StationGeolocMeasType_TableModel)query_TableModel;
			numCommandsAdded = this.tstoolJFrame.queryResultsList_AppendTSIDToCommandList (
				(String)query_TableModel.getValueAt( row, model.COL_ID ),
				(String)query_TableModel.getValueAt ( row, model.COL_DATA_SOURCE),
				(String)query_TableModel.getValueAt ( row, model.COL_DATA_TYPE),
				(String)query_TableModel.getValueAt ( row, model.COL_TIME_STEP),
				"",	// No scenario.
				null, // No sequence number.
				(String)query_TableModel.getValueAt( row, model.COL_INPUT_TYPE),
				"", // No input name.
				(String)query_TableModel.getValueAt( row, model.COL_ID) + " - " +
				(String)query_TableModel.getValueAt (	row, model.COL_NAME),
				false, insertOffset );
		}
		else if ( query_TableModel instanceof TSTool_HydroBase_StructureGeolocStructMeasType_TableModel){
	        // Structure time series.
		    TSTool_HydroBase_StructureGeolocStructMeasType_TableModel model = (TSTool_HydroBase_StructureGeolocStructMeasType_TableModel)query_TableModel;
            numCommandsAdded = this.tstoolJFrame.queryResultsList_AppendTSIDToCommandList (
                (String)query_TableModel.getValueAt( row, model.COL_ID ),
                (String)query_TableModel.getValueAt ( row, model.COL_DATA_SOURCE),
                (String)query_TableModel.getValueAt ( row, model.COL_DATA_TYPE),
                (String)query_TableModel.getValueAt ( row, model.COL_TIME_STEP),
                "", // No scenario.
                null, // No sequence number.
                (String)query_TableModel.getValueAt( row, model.COL_INPUT_TYPE),
                "", // No input name.
                (String)query_TableModel.getValueAt( row, model.COL_ID) + " - " +
                (String)query_TableModel.getValueAt ( row, model.COL_NAME),
                false, insertOffset );
        }
        else if ( query_TableModel instanceof TSTool_HydroBase_GroundWaterWellsView_TableModel){
            // Structure time series.
            TSTool_HydroBase_GroundWaterWellsView_TableModel model = (TSTool_HydroBase_GroundWaterWellsView_TableModel)query_TableModel;
            numCommandsAdded = this.tstoolJFrame.queryResultsList_AppendTSIDToCommandList (
                (String)query_TableModel.getValueAt( row, model.COL_ID ),
                (String)query_TableModel.getValueAt ( row, model.COL_DATA_SOURCE),
                (String)query_TableModel.getValueAt ( row, model.COL_DATA_TYPE),
                (String)query_TableModel.getValueAt ( row, model.COL_TIME_STEP),
                "", // No scenario.
                null, // No sequence number.
                (String)query_TableModel.getValueAt( row, model.COL_INPUT_TYPE),
                "", // No input name.
                (String)query_TableModel.getValueAt( row, model.COL_ID) + " - " +
                (String)query_TableModel.getValueAt ( row, model.COL_NAME),
                false, insertOffset );
        }
		else if (query_TableModel instanceof TSTool_HydroBase_WellLevel_Day_TableModel) {
			TSTool_HydroBase_WellLevel_Day_TableModel model = (TSTool_HydroBase_WellLevel_Day_TableModel)query_TableModel;
			numCommandsAdded = this.tstoolJFrame.queryResultsList_AppendTSIDToCommandList(
				(String)query_TableModel.getValueAt( row, model.COL_ID ),
				(String)query_TableModel.getValueAt (	row, model.COL_DATA_SOURCE),
				(String)query_TableModel.getValueAt (	row, model.COL_DATA_TYPE),
				(String)query_TableModel.getValueAt (	row, model.COL_TIME_STEP),
				"",	// No scenario.
				null, // No sequence number.
				(String)query_TableModel.getValueAt( row, model.COL_INPUT_TYPE),
				"", // No input name.
				(String)query_TableModel.getValueAt( row, model.COL_ID) + " - " +
				(String)query_TableModel.getValueAt ( row, model.COL_NAME),
				false, insertOffset );			
		}
		else if ( query_TableModel instanceof	TSTool_HydroBase_Ag_CASS_TableModel ) {
			TSTool_HydroBase_Ag_CASS_TableModel model = (TSTool_HydroBase_Ag_CASS_TableModel)query_TableModel;
			numCommandsAdded = this.tstoolJFrame.queryResultsList_AppendTSIDToCommandList (
				(String)query_TableModel.getValueAt ( row, model.COL_ID ),
				(String)query_TableModel.getValueAt ( row, model.COL_DATA_SOURCE),
				(String)query_TableModel.getValueAt ( row, model.COL_DATA_TYPE),
				(String)query_TableModel.getValueAt (	row, model.COL_TIME_STEP),
				"",	// No scenario.
				null, // No sequence number.
				(String)query_TableModel.getValueAt( row, model.COL_INPUT_TYPE),
				"", // No input name.
				"", // No comment.
				false, insertOffset );
		}
		else if ( query_TableModel instanceof TSTool_HydroBase_AgGIS_TableModel ) {
			TSTool_HydroBase_AgGIS_TableModel model = (TSTool_HydroBase_AgGIS_TableModel)query_TableModel;
			numCommandsAdded = this.tstoolJFrame.queryResultsList_AppendTSIDToCommandList (
				(String)query_TableModel.getValueAt ( row, model.COL_ID ),
				(String)query_TableModel.getValueAt ( row, model.COL_DATA_SOURCE),
				(String)query_TableModel.getValueAt ( row, model.COL_DATA_TYPE),
				(String)query_TableModel.getValueAt ( row, model.COL_TIME_STEP),
				"",	// No scenario.
				null, // No sequence number.
				(String)query_TableModel.getValueAt( row, model.COL_INPUT_TYPE),
				"", // No input name.
				"", // No comment.
				false, insertOffset );
		}
		else if ( query_TableModel instanceof TSTool_HydroBase_CASSLivestockStats_TableModel ) {
			TSTool_HydroBase_CASSLivestockStats_TableModel model = (TSTool_HydroBase_CASSLivestockStats_TableModel)query_TableModel;
			numCommandsAdded = this.tstoolJFrame.queryResultsList_AppendTSIDToCommandList (
				(String)query_TableModel.getValueAt ( row, model.COL_ID ),
				(String)query_TableModel.getValueAt (	row, model.COL_DATA_SOURCE),
				(String)query_TableModel.getValueAt (	row, model.COL_DATA_TYPE),
				(String)query_TableModel.getValueAt (	row, model.COL_TIME_STEP),
				"",	// No scenario.
				null,	// No sequence number.
				(String)query_TableModel.getValueAt ( row, model.COL_INPUT_TYPE),
				"", // No input name.
				"", // No comment.
				false, insertOffset );
		}
		else if ( query_TableModel instanceof	TSTool_HydroBase_Ag_NASS_TableModel ) {
			TSTool_HydroBase_Ag_NASS_TableModel model = (TSTool_HydroBase_Ag_NASS_TableModel)query_TableModel;
			numCommandsAdded = this.tstoolJFrame.queryResultsList_AppendTSIDToCommandList (
				(String)query_TableModel.getValueAt ( row, model.COL_ID ),
				(String)query_TableModel.getValueAt ( row, model.COL_DATA_SOURCE),
				(String)query_TableModel.getValueAt ( row, model.COL_DATA_TYPE),
				(String)query_TableModel.getValueAt (	row, model.COL_TIME_STEP),
				"",	// No scenario.
				null, // No sequence number.
				(String)query_TableModel.getValueAt( row, model.COL_INPUT_TYPE),
				"", // No input name.
				"", // No comment.
				false, insertOffset );
		}
		else if ( query_TableModel instanceof TSTool_HydroBase_CUPopulation_TableModel ) {
			TSTool_HydroBase_CUPopulation_TableModel model = (TSTool_HydroBase_CUPopulation_TableModel)query_TableModel;
			numCommandsAdded = this.tstoolJFrame.queryResultsList_AppendTSIDToCommandList (
				(String)query_TableModel.getValueAt ( row, model.COL_ID ),
				(String)query_TableModel.getValueAt (	row, model.COL_DATA_SOURCE),
				(String)query_TableModel.getValueAt (	row, model.COL_DATA_TYPE),
				(String)query_TableModel.getValueAt ( row, model.COL_TIME_STEP),
				"",	// No scenario.
				null, // No sequence number.
				(String)query_TableModel.getValueAt( row, model.COL_INPUT_TYPE),
				"", // No input name.
				"", // No comment.
				false, insertOffset );
		}
    	return numCommandsAdded;
    }
}