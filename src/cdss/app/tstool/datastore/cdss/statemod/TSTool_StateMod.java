// TSTool_StateMod - integration between TSTool UI and CDSS StateMod files

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

package cdss.app.tstool.datastore.cdss.statemod;

import java.util.List;

import javax.swing.JFileChooser;

import DWR.DMI.tstool.TSToolConstants;
import DWR.DMI.tstool.TSTool_JFrame;
import DWR.DMI.tstool.TSTool_TS_CellRenderer;
import DWR.DMI.tstool.TSTool_TS_TableModel;
import DWR.StateMod.StateMod_DiversionRight;
import DWR.StateMod.StateMod_GUIUtil;
import DWR.StateMod.StateMod_InstreamFlowRight;
import DWR.StateMod.StateMod_ReservoirRight;
import DWR.StateMod.StateMod_TS;
import DWR.StateMod.StateMod_TS_CellRenderer;
import DWR.StateMod.StateMod_TS_TableModel;
import DWR.StateMod.StateMod_Util;
import DWR.StateMod.StateMod_WellRight;
import RTi.TS.TS;
import RTi.Util.GUI.JFileChooserFactory;
import RTi.Util.GUI.JGUIUtil;
import RTi.Util.GUI.JWorksheet;
import RTi.Util.GUI.JWorksheet_AbstractRowTableModel;
import RTi.Util.GUI.SimpleJComboBox;
import RTi.Util.Message.Message;
import RTi.Util.Time.TimeInterval;

/**
 * This class provides integration between TSTool and the CDSS StateMod model files.
 * These datastores are currently built into TSTool, rather than being a plugin.
 * Isolating the code simplifies maintenance.
 * The class is a singleton.  Use getInstance() to get the instance to call methods.
 */
public class TSTool_StateMod {
	/**
	 * Singleton instance of this class.
	 * Use getInstance() to return the singleton.
	 */
	private static TSTool_StateMod instance = null;
	
	/**
	 * Instance of the TSTool main UI.
	 */
	private TSTool_JFrame tstoolJFrame = null;
	
	/**
	 * Private constructor for lazy initialization.
	 */
	private TSTool_StateMod ( TSTool_JFrame tstoolJFrame ) {
		this.tstoolJFrame = tstoolJFrame;
	}

	/**
	 * Handle the data type selection.
	 */
	public void dataTypeSelected () {
		SimpleJComboBox timeStep_JComboBox = this.tstoolJFrame.ui_GetTimeStepJComboBox();
		timeStep_JComboBox.removeAll ();
		timeStep_JComboBox.add ( TSToolConstants.TIMESTEP_DAY );
		timeStep_JComboBox.add ( TSToolConstants.TIMESTEP_MONTH );
		timeStep_JComboBox.select ( null );
		timeStep_JComboBox.select ( TSToolConstants.TIMESTEP_MONTH );
		timeStep_JComboBox.setEnabled ( true );
	}

	/**
	 * Get the singleton instance.
	 */
	public static TSTool_StateMod getInstance ( TSTool_JFrame tstoolJFrame ) {
		if ( TSTool_StateMod.instance == null ) {
			TSTool_StateMod.instance = new TSTool_StateMod( tstoolJFrame );
		}
		return TSTool_StateMod.instance;
	}

	/**
	Read the list of time series from a StateMod file and display the list in the GUI.
	@exception Exception if there is an error reading the file.
	*/
	public void getTimeSeriesListClicked_ReadStateModCatalog ()
	throws Exception {
		String message, routine = getClass().getSimpleName() + ".getTimeSeriesListClicked_ReadStateModCatalog";

		try {
			JFileChooser fc = JFileChooserFactory.createJFileChooser ( JGUIUtil.getLastFileDialogDirectory() );
			String selectedTimeStep = this.tstoolJFrame.ui_GetSelectedTimeStep();
			// TODO smalers 2007-05-14 Need to better handle picking files.
			// Need to pick the file first and detect the time step from the file, similar to the binary file.
			// For now, key off the selected time step.
			if ( selectedTimeStep.equals( TSToolConstants.TIMESTEP_DAY)) {
				fc.setDialogTitle (	"Select StateMod Daily Time Series File" );
				StateMod_GUIUtil.addTimeSeriesFilenameFilters(fc, TimeInterval.DAY, false);
			}
			else if ( selectedTimeStep.equals( TSToolConstants.TIMESTEP_MONTH)) {
				fc.setDialogTitle (	"Select StateMod Monthly Time Series File" );
				StateMod_GUIUtil.addTimeSeriesFilenameFilters(fc, TimeInterval.MONTH, false);
			}
			if ( fc.showOpenDialog(this.tstoolJFrame) != JFileChooser.APPROVE_OPTION) {
				// Canceled.
				return;
			}
			String directory = fc.getSelectedFile().getParent();
			JGUIUtil.setLastFileDialogDirectory(directory);
			String path = fc.getSelectedFile().getPath();
			Message.printStatus ( 1, routine, "Reading StateMod file \"" + path + "\"" );
			// Normal daily or monthly format file.
			List<TS> tslist = null;
			JGUIUtil.setWaitCursor ( this.tstoolJFrame, true );
			int interval_base = TimeInterval.MONTH;
			if ( selectedTimeStep.equals(TSToolConstants.TIMESTEP_DAY)) {
				interval_base = TimeInterval.DAY;
			}
			else if ( selectedTimeStep.equals(TSToolConstants.TIMESTEP_MONTH)) {
				interval_base = TimeInterval.MONTH;
			}
			/* TODO smalers 2007-05-16 Resolve later.  Use readStateMod() in the meantime.
			else if ( __selected_time_step.equals(__TIMESTEP_YEAR)) {
				interval_base = TimeInterval.YEAR;
			}
			 */
			// TODO smalers 2009-11-16 The well rights are not completely supported - can list but not read.
			// Use the ReadStateMod() command.
			if ( StateMod_DiversionRight.isDiversionRightFile(path) ) {
				// First read the diversion rights.
				List<StateMod_DiversionRight> ddr_List = StateMod_DiversionRight.readStateModFile ( path );
				// Convert the rights to time series (one per location)...
				tslist = StateMod_Util.createWaterRightTimeSeriesList (
						ddr_List,       // Raw water rights.
						interval_base,  // Time series interval.
						0,              // Summarize time series at location.
						-1,				// Don't consider parcel year.
						true,			// Create data set total.
						null,           // Time series start.
						null,           // Time series end.
						999999.0,	    // No special treatment of junior rights.
						null,
						null,
						false );         // Don't read data - only header.
				// Set the input name and input type, which are not set in the above call.
				int size = 0;
				if ( tslist != null ) {
					size = tslist.size();
				}
				for ( int i = 0; i < size; i++ ) {
					TS ts = tslist.get(i);
					ts.getIdentifier().setInputType("StateMod");
					ts.getIdentifier().setInputName(path);
				}
			}
			else if ( StateMod_InstreamFlowRight.isInstreamFlowRightFile(path) ) {
				// First read the instream flow rights.
				List<StateMod_InstreamFlowRight> ifr_List = StateMod_InstreamFlowRight.readStateModFile ( path );
				// Convert the rights to time series (one per location).
				tslist = StateMod_Util.createWaterRightTimeSeriesList (
						ifr_List,       // Raw water rights.
						interval_base,  // Time series interval.
						0,              // Summarize time series at location.
						-1,				// Don't consider parcel year.
						true,			// Create data set total.
						null,           // Time series start.
						null,           // Time series end.
						999999.0,	    // No special treatment of junior rights.
						null,
						null,
						false );        // Don't read data - only header.
				// Set the input name and input type, which are not set in the above call.
				int size = 0;
				if ( tslist != null ) {
					size = tslist.size();
				}
				for ( int i = 0; i < size; i++ ) {
					TS ts = tslist.get(i);
					ts.getIdentifier().setInputType("StateMod");
					ts.getIdentifier().setInputName(path);
				}
			}
			else if ( StateMod_ReservoirRight.isReservoirRightFile(path) ) {
				// First read the reservoir rights.
				List<StateMod_ReservoirRight> rer_List = StateMod_ReservoirRight.readStateModFile ( path );
				// Convert the rights to time series (one per location).
				tslist = StateMod_Util.createWaterRightTimeSeriesList (
						rer_List,       // Raw water rights.
						interval_base,  // Time series interval.
						0,              // Summarize time series at location.
						-1,				// Don't consider parcel year.
						true,			// Create data set total.
						null,           // Time series start.
						null,           // Time series end.
						999999.0,	    // No special treatment of junior rights.
						null,
						null,
						false );        // Don't read data - only header.
				// Set the input name and input type, which are not set in the above call.
				int size = 0;
				if ( tslist != null ) {
					size = tslist.size();
				}
				for ( int i = 0; i < size; i++ ) {
					TS ts = tslist.get(i);
					ts.getIdentifier().setInputType("StateMod");
					ts.getIdentifier().setInputName(path);
				}
			}
			else if ( StateMod_WellRight.isWellRightFile(path) ) {
				// First read the well rights.
				List<StateMod_WellRight> wer_List = StateMod_WellRight.readStateModFile ( path );
				// Convert the rights to time series (one per location).
				tslist = StateMod_Util.createWaterRightTimeSeriesList (
						wer_List,       // Raw water rights.
						interval_base,  // Time series interval.
						0,              // Summarize time series at location.
						-1,				// Don't consider parcel year.
						true,			// Create data set total.
						null,           // Time series start.
						null,           // Time series end.
						999999.0,	    // No special treatment of junior rights.
						null,
						null,
						false ); // Don't read data - only header.
				// Set the input name and input type, which are not set in the above call.
				int size = 0;
				if ( tslist != null ) {
					size = tslist.size();
				}
				for ( int i = 0; i < size; i++ ) {
					TS ts = tslist.get(i);
					ts.getIdentifier().setInputType("StateMod");
					ts.getIdentifier().setInputName(path);
				}
			}
			else if ( selectedTimeStep.equals(TSToolConstants.TIMESTEP_DAY) ) {
				// Daily, only read the headers.
				tslist = StateMod_TS.readTimeSeriesList ( path, null, null, null, false );
			}
			else {
				// Monthly, only read the headers.
				tslist = StateMod_TS.readTimeSeriesList ( path, null, null, null, false );
			}
			int size = 0;
			if ( tslist != null ) {
				size = tslist.size();
				if ( path.toUpperCase().endsWith("XOP") ) {
					// Use a table model that has columns more specific than the general table model.
					JWorksheet_AbstractRowTableModel query_TableModel = new StateMod_TS_TableModel ( tslist, "XOP" );
					this.tstoolJFrame.ui_SetTimeSeriesCatalogTableModel ( query_TableModel );
					StateMod_TS_CellRenderer cr = new StateMod_TS_CellRenderer( (StateMod_TS_TableModel)query_TableModel);

					JWorksheet query_JWorksheet = tstoolJFrame.ui_GetTimeSeriesCatalogWorksheet();
					query_JWorksheet.setCellRenderer ( cr );
					query_JWorksheet.setModel(query_TableModel);
				}
				else {
					// Use generic table model.
					JWorksheet_AbstractRowTableModel query_TableModel = new TSTool_TS_TableModel ( tslist);
					this.tstoolJFrame.ui_SetTimeSeriesCatalogTableModel ( query_TableModel );
					TSTool_TS_CellRenderer cr = new TSTool_TS_CellRenderer(	(TSTool_TS_TableModel)query_TableModel);

					JWorksheet query_JWorksheet = tstoolJFrame.ui_GetTimeSeriesCatalogWorksheet();
					query_JWorksheet.setCellRenderer ( cr );
					query_JWorksheet.setModel(query_TableModel);
					// Turn off columns in the table model that do not apply.
					query_JWorksheet.removeColumn ( ((TSTool_TS_TableModel)query_TableModel).COL_ALIAS );
					query_JWorksheet.removeColumn ( ((TSTool_TS_TableModel)query_TableModel).COL_DATA_SOURCE );
					query_JWorksheet.removeColumn ( ((TSTool_TS_TableModel)query_TableModel).COL_DATA_TYPE );
					query_JWorksheet.removeColumn ( ((TSTool_TS_TableModel)query_TableModel).COL_SCENARIO);
					query_JWorksheet.setColumnWidths ( cr.getColumnWidths(), this.tstoolJFrame.getGraphics() );
				}
			}
			if ( (tslist == null) || (size == 0) ) {
				Message.printStatus ( 1, routine, "No StateMod time series were read." );
				this.tstoolJFrame.queryResultsList_Clear ();
			}
			else {
				Message.printStatus ( 1, routine, "" + size + " StateMod time series read." );
			}

			JGUIUtil.setWaitCursor ( this.tstoolJFrame, false );
			this.tstoolJFrame.ui_UpdateStatus ( false );
		}
		catch ( Exception e ) {
			message = "Error reading StateMod file (" + e + ").";
			Message.printWarning ( 2, routine, message );
			JGUIUtil.setWaitCursor ( this.tstoolJFrame, false );
			throw new Exception ( message );
		}
	}

	/**
	Set up query choices because the StateMod input type has been selected.
	*/
	public void selectInputType ()
	throws Exception {
		// Disable all but the time step so the user can pick from appropriate files.
		this.tstoolJFrame.ui_SetInputNameVisible(false); // Not needed.
		SimpleJComboBox inputName_JComboBox = this.tstoolJFrame.ui_GetInputNameJComboBox();
		inputName_JComboBox.removeAll();
		inputName_JComboBox.setEnabled ( false );

		SimpleJComboBox dataType_JComboBox = this.tstoolJFrame.ui_GetDataTypeJComboBox();
		dataType_JComboBox.setEnabled ( false );
		dataType_JComboBox.removeAll ();
		dataType_JComboBox.add( TSToolConstants.DATA_TYPE_AUTO );
		dataType_JComboBox.select ( null );
		dataType_JComboBox.select ( 0 );

		// Initialize with blank data list.
		// The table model will be reset when actual data are read, for example, if an XOP file is picked.
		JWorksheet_AbstractRowTableModel query_TableModel = new TSTool_TS_TableModel(null);
		this.tstoolJFrame.ui_SetTimeSeriesCatalogTableModel ( query_TableModel );
		TSTool_TS_CellRenderer cr = new TSTool_TS_CellRenderer( (TSTool_TS_TableModel)query_TableModel);

		JWorksheet query_JWorksheet = tstoolJFrame.ui_GetTimeSeriesCatalogWorksheet();
		query_JWorksheet.setCellRenderer ( cr );
		query_JWorksheet.setModel ( query_TableModel );
		// Turn off columns in the table model that do not apply.
		query_JWorksheet.removeColumn (((TSTool_TS_TableModel)query_TableModel).COL_ALIAS );
		query_JWorksheet.removeColumn (((TSTool_TS_TableModel)query_TableModel).COL_DATA_SOURCE );
		query_JWorksheet.removeColumn (((TSTool_TS_TableModel)query_TableModel).COL_DATA_TYPE );
		query_JWorksheet.removeColumn (((TSTool_TS_TableModel)query_TableModel).COL_SCENARIO);
		query_JWorksheet.removeColumn (((TSTool_TS_TableModel)query_TableModel).COL_SEQUENCE );
		query_JWorksheet.setColumnWidths ( cr.getColumnWidths() );
	}

	/**
	 * Transfer one time series catalog row to a command, for daily time series.
	 * @param row the time series catalog row to transfer (0+)
	 * @param useAlias whether to use an alias
	 */
    public int transferOneTimeSeriesCatalogRowToCommands ( int row, boolean useAlias, int insertOffset ) {
	    // Most time series are still handled generically but XOP time series have additional columns in the table.
        int numCommandsAdded = 0;
		JWorksheet_AbstractRowTableModel query_TableModel = this.tstoolJFrame.ui_GetTimeSeriesCatalogTableModel();
	    if ( query_TableModel instanceof StateMod_TS_TableModel ) {
	        // The location (id), type, and time step uniquely identify the time series.
            StateMod_TS_TableModel model = (StateMod_TS_TableModel)query_TableModel;
            String scenario = null;
            String seqnum = null;
            numCommandsAdded = this.tstoolJFrame.queryResultsList_AppendTSIDToCommandList (
            (String)query_TableModel.getValueAt( row, model.COL_ID ),
            (String)query_TableModel.getValueAt( row, model.COL_DATA_SOURCE),
            (String)query_TableModel.getValueAt( row, model.COL_DATA_TYPE),
            (String)query_TableModel.getValueAt( row, model.COL_TIME_STEP ),
            scenario, seqnum, // Not used
            (String)query_TableModel.getValueAt( row, model.COL_INPUT_TYPE),
            (String)query_TableModel.getValueAt( row, model.COL_INPUT_NAME), "", false, insertOffset );
	    }
	    else {
	        // The location (id), type, and time step uniquely identify the time series.
	        TSTool_TS_TableModel model = (TSTool_TS_TableModel)query_TableModel;
	        String seqnum = (String)query_TableModel.getValueAt( row, model.COL_SEQUENCE );
	        if ( seqnum.length() == 0 ) {
	            seqnum = null;
	        }
	        numCommandsAdded = this.tstoolJFrame.queryResultsList_AppendTSIDToCommandList (
	        (String)query_TableModel.getValueAt( row, model.COL_ID ),
	        (String)query_TableModel.getValueAt( row, model.COL_DATA_SOURCE),
	        (String)query_TableModel.getValueAt( row, model.COL_DATA_TYPE),
	        (String)query_TableModel.getValueAt( row, model.COL_TIME_STEP ),
	        (String)query_TableModel.getValueAt( row, model.COL_SCENARIO ), seqnum, // Optional sequence number.
	        (String)query_TableModel.getValueAt( row, model.COL_INPUT_TYPE),
	        (String)query_TableModel.getValueAt( row, model.COL_INPUT_NAME), "", false, insertOffset );
	    }
	    return numCommandsAdded;
    }

}