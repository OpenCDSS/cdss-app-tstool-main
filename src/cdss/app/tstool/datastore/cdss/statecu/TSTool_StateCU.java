// TSTool_StateCU - integration between TSTool UI and StateCU model files

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

package cdss.app.tstool.datastore.cdss.statecu;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import DWR.DMI.tstool.TSToolConstants;
import DWR.DMI.tstool.TSTool_JFrame;
import DWR.DMI.tstool.TSTool_TS_CellRenderer;
import DWR.DMI.tstool.TSTool_TS_TableModel;
import DWR.StateCU.StateCU_CropPatternTS;
import DWR.StateCU.StateCU_IrrigationPracticeTS;
import DWR.StateCU.StateCU_TS;
import DWR.StateMod.StateMod_TS;
import RTi.TS.TS;
import RTi.Util.GUI.JFileChooserFactory;
import RTi.Util.GUI.JGUIUtil;
import RTi.Util.GUI.JWorksheet;
import RTi.Util.GUI.JWorksheet_AbstractRowTableModel;
import RTi.Util.GUI.SimpleFileFilter;
import RTi.Util.GUI.SimpleJComboBox;
import RTi.Util.Message.Message;

/**
 * This class provides integration between TSTool and StateCU model files.
 * These datastores are currently built into TSTool, rather than being a plugin.
 * Isolating the code simplifies maintenance.
 * The class is a singleton.  Use getInstance() to get the instance to call methods.
 */
public class TSTool_StateCU {

	/**
	 * Singleton instance of this class.
	 * Use getInstance() to return the singleton.
	 */
	private static TSTool_StateCU instance = null;
	
	/**
	 * Instance of the TSTool main UI.
	 */
	private TSTool_JFrame tstoolJFrame = null;

	/**
	The StateCU files that have been selected during the session,
	to allow switching between input types but not losing the list of files.
	*/
	private List<String> inputNameStateCU = new ArrayList<>();

	/**
	The last StateCU file that was selected, to reset after canceling a browse.
	*/
	private String inputNameStateCULast = null;

	/**
	FileFilter for current input name.
	Having as a data member allows selection of a filter in one place and a check later
	(e.g., when the time series list is generated and displayed).
	*/
	private FileFilter inputName_FileFilter = null;

	/**
	FileFilters used with StateCU input type. this.inputName_FileFilter will be set to one of these.
	*/
	private SimpleFileFilter
	input_name_StateCU_ddy_FileFilter = null,
	input_name_StateCU_ddc_FileFilter = null,
	input_name_StateCU_ddh_FileFilter = null,
	input_name_StateCU_iwrrep_FileFilter = null,
	input_name_StateCU_iwr_FileFilter = null,
	input_name_StateCU_precip_FileFilter = null,
	input_name_StateCU_temp_FileFilter = null,
	input_name_StateCU_cds_FileFilter = null,
	input_name_StateCU_frost_FileFilter = null,
	input_name_StateCU_ipy_FileFilter = null,
	input_name_StateCU_tsp_FileFilter = null,
	input_name_StateCU_wsl_FileFilter = null;

	/**
	 * Private constructor for lazy initialization.
	 */
	private TSTool_StateCU ( TSTool_JFrame tstoolJFrame ) {
		this.tstoolJFrame = tstoolJFrame;
	}

	/**
	 * Handle data type selection
	 */
	public void dataTypeSelected () {
		SimpleJComboBox timeStep_JComboBox = this.tstoolJFrame.ui_GetTimeStepJComboBox();
		timeStep_JComboBox.removeAll ();
		timeStep_JComboBox.add ( TSToolConstants.TIMESTEP_AUTO );
		timeStep_JComboBox.setEnabled ( false );
	}

	/**
	 * Get the singleton instance.
	 */
	public static TSTool_StateCU getInstance ( TSTool_JFrame tstoolJFrame ) {
		if ( TSTool_StateCU.instance == null ) {
			TSTool_StateCU.instance = new TSTool_StateCU( tstoolJFrame );
		}
		return TSTool_StateCU.instance;
	}

	/**
	Read the list of time series from a StateCU file and display the list in the GUI.
	@exception Exception if there is an error reading the file.
	*/
	public void getTimeSeriesListClicked_ReadStateCUCatalog ()
	throws Exception {
	 	String message, routine = getClass().getSimpleName() + ".readStateCUHeaders";

	 	SimpleJComboBox inputName_JComboBox = this.tstoolJFrame.ui_GetInputNameJComboBox();

	 	try {
	 		List<TS> tslist = null; // Time series to display.
	 		TS ts = null; // Single time series.
	 		int size = 0; // Number of time series.
	 		String path = inputName_JComboBox.getSelected();
	 		Message.printStatus ( 1, routine, "Reading StateCU file \"" + path + "\"" );
	 		if ( (inputName_FileFilter == this.input_name_StateCU_iwrrep_FileFilter) ||
	 				(inputName_FileFilter == this.input_name_StateCU_wsl_FileFilter) ||
	 				(inputName_FileFilter == this.input_name_StateCU_frost_FileFilter) ) {
	 			JGUIUtil.setWaitCursor ( this.tstoolJFrame, true );
	 			tslist = StateCU_TS.readTimeSeriesList ( path, null, null, null, false );
	 			if ( tslist != null ) {
	 				size = tslist.size();
	 				JWorksheet_AbstractRowTableModel query_TableModel = new TSTool_TS_TableModel ( tslist );
					this.tstoolJFrame.ui_SetTimeSeriesCatalogTableModel ( query_TableModel );
	 				TSTool_TS_CellRenderer cr =	new TSTool_TS_CellRenderer(	(TSTool_TS_TableModel)query_TableModel);

	 				JWorksheet query_JWorksheet = tstoolJFrame.ui_GetTimeSeriesCatalogWorksheet();
	 				query_JWorksheet.setCellRenderer ( cr );
	 				query_JWorksheet.setModel(query_TableModel);
	 				// Turn off columns in the table model that do not apply.
	 				query_JWorksheet.removeColumn (((TSTool_TS_TableModel)query_TableModel).COL_ALIAS );
	 				query_JWorksheet.removeColumn (((TSTool_TS_TableModel)query_TableModel).COL_DATA_SOURCE );
	 				query_JWorksheet.removeColumn (((TSTool_TS_TableModel)query_TableModel).COL_SCENARIO);
	 				query_JWorksheet.setColumnWidths (cr.getColumnWidths(), this.tstoolJFrame.getGraphics() );
	 			}
	 		}
	 		else if ( inputName_FileFilter == this.input_name_StateCU_cds_FileFilter ) {
	 			// Yearly crop patterns.
	 			JGUIUtil.setWaitCursor ( this.tstoolJFrame, true );
	 			tslist = StateCU_CropPatternTS.readTimeSeriesList (	path, null, null, null, false );
	 			if ( tslist != null ) {
	 				size = tslist.size();
	 				JWorksheet_AbstractRowTableModel query_TableModel = new TSTool_TS_TableModel ( tslist );
					this.tstoolJFrame.ui_SetTimeSeriesCatalogTableModel ( query_TableModel );
	 				TSTool_TS_CellRenderer cr = new TSTool_TS_CellRenderer(	(TSTool_TS_TableModel)query_TableModel);

	 				JWorksheet query_JWorksheet = tstoolJFrame.ui_GetTimeSeriesCatalogWorksheet();
	 				query_JWorksheet.setCellRenderer ( cr );
	 				query_JWorksheet.setModel(query_TableModel);
	 				// Turn off columns in the table model that do not apply.
	 				query_JWorksheet.removeColumn (((TSTool_TS_TableModel)query_TableModel).COL_ALIAS );
	 				query_JWorksheet.removeColumn (((TSTool_TS_TableModel)query_TableModel).COL_DATA_SOURCE );
	 				query_JWorksheet.removeColumn (((TSTool_TS_TableModel)query_TableModel).COL_SCENARIO);
	 				query_JWorksheet.setColumnWidths (cr.getColumnWidths(), this.tstoolJFrame.getGraphics() );
	 			}
	 		}
	 		else if ((inputName_FileFilter == this.input_name_StateCU_ipy_FileFilter) ||
	 				(inputName_FileFilter ==	this.input_name_StateCU_tsp_FileFilter) ) {
	 			// Yearly irrigation practice.
	 			JGUIUtil.setWaitCursor ( this.tstoolJFrame, true );
	 			tslist = StateCU_IrrigationPracticeTS.readTimeSeriesList ( path, null, null, null, false );
	 			if ( tslist != null ) {
	 				size = tslist.size();
	 				JWorksheet_AbstractRowTableModel query_TableModel = new TSTool_TS_TableModel ( tslist );
					this.tstoolJFrame.ui_SetTimeSeriesCatalogTableModel ( query_TableModel );
	 				TSTool_TS_CellRenderer cr =	new TSTool_TS_CellRenderer(	(TSTool_TS_TableModel)query_TableModel);

	 				JWorksheet query_JWorksheet = tstoolJFrame.ui_GetTimeSeriesCatalogWorksheet();
	 				query_JWorksheet.setCellRenderer ( cr );
	 				query_JWorksheet.setModel(query_TableModel);
	 				// Turn off columns in the table model that do not apply.
	 				query_JWorksheet.removeColumn (((TSTool_TS_TableModel)query_TableModel).COL_ALIAS );
	 				query_JWorksheet.removeColumn (((TSTool_TS_TableModel)query_TableModel).COL_DATA_SOURCE );
	 				query_JWorksheet.removeColumn (((TSTool_TS_TableModel)query_TableModel).COL_SCENARIO);
	 				query_JWorksheet.setColumnWidths (cr.getColumnWidths(), this.tstoolJFrame.getGraphics() );
	 			}
	 		}
	 		else if ((inputName_FileFilter == this.input_name_StateCU_ddc_FileFilter) ||
	 			(inputName_FileFilter == this.input_name_StateCU_ddy_FileFilter) ||
	 			(inputName_FileFilter == this.input_name_StateCU_ddh_FileFilter) ||
	 			(inputName_FileFilter == this.input_name_StateCU_precip_FileFilter) ||
	 			(inputName_FileFilter == this.input_name_StateCU_temp_FileFilter) ||
	 			(inputName_FileFilter == this.input_name_StateCU_frost_FileFilter) ) {
	 			// Normal daily or monthly StateMod file.
	 			JGUIUtil.setWaitCursor ( this.tstoolJFrame, true );
	 			if ( inputName_FileFilter == this.input_name_StateCU_frost_FileFilter ) {
	 				tslist = StateCU_TS.readTimeSeriesList (path, null, null, null, false );
	 			}
	 			else {
	 				// Normal StateMod file.
	 				tslist = StateMod_TS.readTimeSeriesList ( path, null, null, null, false );
	 			}
	 			// Change time series data source to StateCU since the file is part of a StateCU data set.
	 			if ( tslist != null ) {
	 				size = tslist.size();
	 				for ( int i = 0; i < size; i++ ) {
	 					ts = (TS)tslist.get(i);
	 					if ( ts == null ) {
	 						continue;
	 					}
	 					ts.getIdentifier().setSource("StateCU");
	 				}
	 				JWorksheet_AbstractRowTableModel query_TableModel = new TSTool_TS_TableModel ( tslist );
					this.tstoolJFrame.ui_SetTimeSeriesCatalogTableModel ( query_TableModel );
	 				TSTool_TS_CellRenderer cr =	new TSTool_TS_CellRenderer(	(TSTool_TS_TableModel)query_TableModel);

	 				JWorksheet query_JWorksheet = tstoolJFrame.ui_GetTimeSeriesCatalogWorksheet();
	 				query_JWorksheet.setCellRenderer ( cr );
	 				query_JWorksheet.setModel(query_TableModel);
	 				// Turn off columns in the table model that do not apply.
	 				query_JWorksheet.removeColumn (((TSTool_TS_TableModel)query_TableModel).COL_ALIAS );
	 				query_JWorksheet.removeColumn (((TSTool_TS_TableModel)query_TableModel).COL_DATA_SOURCE );
	 				query_JWorksheet.removeColumn (((TSTool_TS_TableModel)query_TableModel).COL_DATA_TYPE );
	 				query_JWorksheet.removeColumn (((TSTool_TS_TableModel)query_TableModel).COL_SCENARIO);
	 				query_JWorksheet.setColumnWidths (cr.getColumnWidths(), this.tstoolJFrame.getGraphics() );
	 			}
	 		}
	 		else {
	 			Message.printWarning ( 1, routine, "File format not recognized for \"" + path + "\"" );
	 		}
	 		if ( (tslist == null) || (size == 0) ) {
	 			Message.printStatus (1, routine,"No StateCU time series read.");
	 			this.tstoolJFrame.queryResultsList_Clear ();
	 		}
	 		else {
	 			Message.printStatus ( 1, routine, "" + size + " StateCU TS read." );
	 		}

	 		JGUIUtil.setWaitCursor ( this.tstoolJFrame, false );
	 		this.tstoolJFrame.ui_UpdateStatus ( false );
	 	}
	 	catch ( Exception e ) {
	 		message = "Unexpected error reading StateCU file.";
	 		Message.printWarning ( 2, routine, message );
	 		Message.printWarning ( 2, routine, e );
	 		JGUIUtil.setWaitCursor ( this.tstoolJFrame, false );
	 		throw new Exception ( message );
	 	}
	}

	/**
	Prompt for a StateCU input name (one of several files).  When selected, update the choices.
	@param reset_input_names If true, the input names will be repopulated with values from __input_name_StateCU.
	@exception Exception if there is an error.
	*/
	public void selectInputType ( boolean reset_input_names )
	throws Exception {
		this.tstoolJFrame.ui_SetInputNameVisible(true); // Lists files.
		SimpleJComboBox inputName_JComboBox = this.tstoolJFrame.ui_GetInputNameJComboBox();
		SimpleJComboBox dataType_JComboBox = this.tstoolJFrame.ui_GetDataTypeJComboBox();
		SimpleJComboBox timeStep_JComboBox = this.tstoolJFrame.ui_GetTimeStepJComboBox();

		if ( reset_input_names ) {
			// The StateCU input type has been selected as a change from another type.
			// Repopululate the list if previous choices exist.
			inputName_JComboBox.setData ( this.inputNameStateCU );
		}
		// Check the item that is selected.
		String inputName = inputName_JComboBox.getSelected();
		if ( (inputName == null) || inputName.equals(TSToolConstants.BROWSE) ) {
			// Prompt for the name of a StateCU file.
			// Based on the file extension, set the data types and other information.
			JFileChooser fc = JFileChooserFactory.createJFileChooser ( JGUIUtil.getLastFileDialogDirectory() );
			// Only handle specific files.  List alphabetically by the description.
			fc.setAcceptAllFileFilterUsed ( false );
			fc.setDialogTitle ( "Select StateCU Input or Output File" );
			this.input_name_StateCU_cds_FileFilter = new SimpleFileFilter("cds","Crop Pattern (Yearly)");
			fc.addChoosableFileFilter(this.input_name_StateCU_cds_FileFilter);
			this.input_name_StateCU_ddh_FileFilter =new SimpleFileFilter("ddh",
				"Historical Direct Diversions - StateMod format (Monthly)");
			fc.addChoosableFileFilter(this.input_name_StateCU_ddh_FileFilter);
			this.input_name_StateCU_ddy_FileFilter = new SimpleFileFilter(
				"ddy","Historical Direct Diversions - StateMod format (Daily)");
			fc.addChoosableFileFilter(this.input_name_StateCU_ddy_FileFilter);
			this.input_name_StateCU_ipy_FileFilter = new SimpleFileFilter("ipy", "Irrigation Practice (Yearly)");
			fc.addChoosableFileFilter(this.input_name_StateCU_ipy_FileFilter);
			this.input_name_StateCU_tsp_FileFilter = new SimpleFileFilter("tsp",
				"Irrigation Practice - old TSP extension (Yearly)");
			fc.addChoosableFileFilter(this.input_name_StateCU_tsp_FileFilter);
			this.input_name_StateCU_ddc_FileFilter = new SimpleFileFilter(
				"ddc", "Irrigation Water Requirement - StateMod format (Monthly)");
			fc.addChoosableFileFilter(this.input_name_StateCU_ddc_FileFilter);
			this.input_name_StateCU_iwr_FileFilter = new SimpleFileFilter(
				"iwr", "Irrigation Water Requirement - StateMod format (Monthly)");
			fc.addChoosableFileFilter(this.input_name_StateCU_iwr_FileFilter);
			this.input_name_StateCU_iwrrep_FileFilter =
				new SimpleFileFilter("iwr",	"Irrigation Water Requirement - StateCU report (Monthly, Yearly)");
			fc.addChoosableFileFilter(this.input_name_StateCU_iwrrep_FileFilter);
			this.input_name_StateCU_frost_FileFilter = new SimpleFileFilter("stm", "Frost Dates (Yearly)");
			fc.addChoosableFileFilter(this.input_name_StateCU_frost_FileFilter);
			this.input_name_StateCU_precip_FileFilter = new SimpleFileFilter(
				"stm", "Precipitation Time Series - StateMod format (Monthly)");
			fc.addChoosableFileFilter(this.input_name_StateCU_precip_FileFilter);
			this.input_name_StateCU_temp_FileFilter= new SimpleFileFilter("stm",
				"Temperature Time Series - StateMod format (Monthly)");
			fc.addChoosableFileFilter(this.input_name_StateCU_temp_FileFilter);
			this.input_name_StateCU_wsl_FileFilter = new SimpleFileFilter("wsl",
				"Water Supply Limited CU - StateCU report (Monthly, Yearly)" );
			fc.addChoosableFileFilter(this.input_name_StateCU_wsl_FileFilter);
			fc.setFileFilter(this.input_name_StateCU_iwrrep_FileFilter);
			if (fc.showOpenDialog(this.tstoolJFrame) != JFileChooser.APPROVE_OPTION) {
				// User canceled - set the file name back to the original and disable other choices.
				if ( inputName != null ) {
					this.tstoolJFrame.ui_SetIgnoreItemEvent ( true );
					inputName_JComboBox.select(null);
					if ( inputNameStateCULast != null ) {
						inputName_JComboBox.select(this.inputNameStateCULast );
					}
					this.tstoolJFrame.ui_SetIgnoreItemEvent ( false );
				}
				return;
			}

			// User has chosen a file.

			inputName = fc.getSelectedFile().getPath();
			// Save as last selection.
			this.inputNameStateCULast = inputName;
			this.inputName_FileFilter = fc.getFileFilter();
			JGUIUtil.setLastFileDialogDirectory (fc.getSelectedFile().getParent() );

			// Set the input name.

			this.tstoolJFrame.ui_SetIgnoreItemEvent ( true );
			if ( !JGUIUtil.isSimpleJComboBoxItem (inputName_JComboBox, TSToolConstants.BROWSE, JGUIUtil.NONE, null, null ) ) {
				// Not already in so add it at the beginning.
				this.inputNameStateCU.add ( TSToolConstants.BROWSE );
				inputName_JComboBox.add ( TSToolConstants.BROWSE );
			}
			if ( !JGUIUtil.isSimpleJComboBoxItem (inputName_JComboBox, inputName, JGUIUtil.NONE, null, null ) ) {
				// Not already in so add after the browse string
				// (files are listed chronologically by select with most recent at the top).
				if ( inputName_JComboBox.getItemCount() > 1 ) {
					inputName_JComboBox.addAt ( inputName, 1 );
					this.inputNameStateCU.add ( 1, inputName );
				}
				else {
					inputName_JComboBox.add ( inputName );
					this.inputNameStateCU.add(inputName);
				}
			}
			this.tstoolJFrame.ui_SetIgnoreItemEvent ( false );
			// Select the file in the input name because leaving it on browse will disable the user's ability to reselect browse.
			inputName_JComboBox.select ( null );
			inputName_JComboBox.select ( inputName );
		}

		inputName_JComboBox.setEnabled ( true );

		// Set the data types and time step based on the file extension.

		dataType_JComboBox.setEnabled ( true );
		dataType_JComboBox.removeAll ();

		// For now, set the data type to auto.  Later, let users select the data type to be selected.

		dataType_JComboBox.removeAll ();
		dataType_JComboBox.add ( TSToolConstants.DATA_TYPE_AUTO );
		dataType_JComboBox.select ( null );
		dataType_JComboBox.select ( 0 );
		dataType_JComboBox.setEnabled ( false );

		// Set time step appropriately.

		timeStep_JComboBox.removeAll ();
		timeStep_JComboBox.add ( TSToolConstants.TIMESTEP_AUTO );
		timeStep_JComboBox.select ( null );
		timeStep_JComboBox.select ( 0 );
		timeStep_JComboBox.setEnabled ( false );

		// Initialize with blank data list.

   		JWorksheet_AbstractRowTableModel query_TableModel = new TSTool_TS_TableModel(null);
		this.tstoolJFrame.ui_SetTimeSeriesCatalogTableModel ( query_TableModel );
		TSTool_TS_CellRenderer cr = new TSTool_TS_CellRenderer((TSTool_TS_TableModel)query_TableModel);

   		JWorksheet query_JWorksheet = tstoolJFrame.ui_GetTimeSeriesCatalogWorksheet();
		query_JWorksheet.setCellRenderer ( cr );
		query_JWorksheet.setModel ( query_TableModel );
		// Turn off columns in the table model that do not apply.
		query_JWorksheet.removeColumn (((TSTool_TS_TableModel)query_TableModel).COL_ALIAS );
		query_JWorksheet.removeColumn (((TSTool_TS_TableModel)query_TableModel).COL_SCENARIO);
		query_JWorksheet.removeColumn (((TSTool_TS_TableModel)query_TableModel).COL_SEQUENCE);
		query_JWorksheet.setColumnWidths ( cr.getColumnWidths() );
	}

	/**
	 * Transfer one time series catalog row to a command.
	 * @param row the time series catalog row to transfer (0+)
	 * @param useAlias whether to use an alias
	 */
    public int transferOneTimeSeriesCatalogRowToCommands ( int row, boolean useAlias, int insertOffset ) {
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