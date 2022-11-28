// TSTool_StateModB - integration between TSTool UI and StateMod model binary file

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
 * This class provides integration between TSTool and StateMod model binary files.
 * These datastores are currently built into TSTool, rather than being a plugin.
 * Isolating the code simplifies maintenance.
 * The class is a singleton.  Use getInstance() to get the instance to call methods.
 */
package cdss.app.tstool.datastore.cdss.statemodb;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;

import DWR.DMI.tstool.TSToolConstants;
import DWR.DMI.tstool.TSTool_JFrame;
import DWR.DMI.tstool.TSTool_TS_CellRenderer;
import DWR.DMI.tstool.TSTool_TS_TableModel;
import DWR.StateMod.StateMod_BTS;
import DWR.StateMod.StateMod_DataSet;
import DWR.StateMod.StateMod_Util;
import RTi.TS.TS;
import RTi.Util.GUI.JFileChooserFactory;
import RTi.Util.GUI.JGUIUtil;
import RTi.Util.GUI.JWorksheet;
import RTi.Util.GUI.JWorksheet_AbstractRowTableModel;
import RTi.Util.GUI.SimpleFileFilter;
import RTi.Util.GUI.SimpleJComboBox;
import RTi.Util.IO.IOUtil;
import RTi.Util.Message.Message;
import RTi.Util.Time.TimeInterval;

public class TSTool_StateModB {

	/**
	 * Singleton instance of this class.
	 * Use getInstance() to return the singleton.
	 */
	private static TSTool_StateModB instance = null;
	
	/**
	 * Instance of the TSTool main UI.
	 */
	private TSTool_JFrame tstoolJFrame = null;

	/**
	The StateModB files that have been selected during the session,
	to allow switching between input types but not losing the list of files.
	*/
	private List<String> inputNameStateModB = new ArrayList<>();

	/**
	The last StateModB file that was selected, to reset after canceling a browse.
	*/
	private String inputNameStateModBLast = null;
	
	/**
	 * Private constructor for lazy initialization.
	 */
	private TSTool_StateModB ( TSTool_JFrame tstoolJFrame ) {
		this.tstoolJFrame = tstoolJFrame;
	}

	/**
	 * Get the singleton instance.
	 */
	public static TSTool_StateModB getInstance ( TSTool_JFrame tstoolJFrame ) {
		if ( TSTool_StateModB.instance == null ) {
			TSTool_StateModB.instance = new TSTool_StateModB( tstoolJFrame );
		}
		return TSTool_StateModB.instance;
	}

	/**
	Read the list of time series from a StateMod binary file and list in the GUI.
	The binary file is taken from the selected item in the __input_name_JComboBox.
	*/
	public void getTimeSeriesListClicked_ReadStateModBCatalog ()
	throws IOException {
		String routine = getClass().getSimpleName() + ".uiAction_GetTimeSeriesListClicked_ReadStateModBHeaders";

		String selectedDataType = this.tstoolJFrame.ui_GetSelectedDataType();
		try {
			String path = this.tstoolJFrame.ui_GetSelectedInputName();
			Message.printStatus ( 1, routine, "Reading StateMod binary output file \"" + path + "\"" );
			List<TS> tslist = null;
			JGUIUtil.setWaitCursor ( this.tstoolJFrame, true );
			StateMod_BTS bin = new StateMod_BTS ( path );
			tslist = bin.readTimeSeriesList ( "*.*." + selectedDataType + ".*.*", null, null, null, false );
			bin.close();
			int size = 0;
			if ( tslist != null ) {
				size = tslist.size();
				JWorksheet_AbstractRowTableModel query_TableModel = new TSTool_TS_TableModel ( tslist );
				this.tstoolJFrame.ui_SetTimeSeriesCatalogTableModel ( query_TableModel );
				TSTool_TS_CellRenderer cr =	new TSTool_TS_CellRenderer(	(TSTool_TS_TableModel)query_TableModel);

				JWorksheet query_JWorksheet = tstoolJFrame.ui_GetTimeSeriesCatalogWorksheet();
				query_JWorksheet.setCellRenderer ( cr );
				query_JWorksheet.setModel ( query_TableModel );
				// Turn off columns in the table model that do not apply.
				query_JWorksheet.removeColumn (((TSTool_TS_TableModel)query_TableModel).COL_ALIAS );
				query_JWorksheet.removeColumn (((TSTool_TS_TableModel)query_TableModel).COL_ALIAS );
				query_JWorksheet.removeColumn (((TSTool_TS_TableModel)query_TableModel).COL_SCENARIO);
				query_JWorksheet.removeColumn (((TSTool_TS_TableModel)query_TableModel).COL_DATA_SOURCE );
				//__query_JWorksheet.removeColumn (((TSTool_TS_TableModel)__query_TableModel).COL_DATA_TYPE );
				query_JWorksheet.setColumnWidths ( cr.getColumnWidths(), this.tstoolJFrame.getGraphics() );
			}
			if ( (tslist == null) || (size == 0) ) {
				Message.printStatus ( 1, routine, "No StateMod binary time series were read." );
				this.tstoolJFrame.queryResultsList_Clear ();
			}
			else {
				Message.printStatus ( 1, routine, "" + size + " StateMod binary TS were read." );
			}

			this.tstoolJFrame.ui_UpdateStatus ( false );
			tslist = null;
			JGUIUtil.setWaitCursor ( this.tstoolJFrame, false );
		}
		catch ( Exception e ) {
			String message = "Error reading StateMod binary file.";
			Message.printWarning ( 2, routine, message );
			Message.printWarning ( 2, routine, e );
			JGUIUtil.setWaitCursor ( this.tstoolJFrame, false );
			throw new IOException ( message );
		}
	}

	/**
	Prompt for a StateModB input name (binary file name).  When selected, update the choices.
	@param resetInputNames If true, the input names will be repopulated with values from __input_name_StateModB.
	@exception Exception if there is an error.
	 */
	public void selectInputType ( boolean resetInputNames )
	throws Exception {
		String routine = getClass().getSimpleName() + "selectInputType";
		this.tstoolJFrame.ui_SetInputNameVisible(true); // Lists files.
		SimpleJComboBox inputName_JComboBox = this.tstoolJFrame.ui_GetInputNameJComboBox();
		SimpleJComboBox dataType_JComboBox = this.tstoolJFrame.ui_GetDataTypeJComboBox();
		SimpleJComboBox timeStep_JComboBox = this.tstoolJFrame.ui_GetTimeStepJComboBox();
		if ( resetInputNames ) {
			// The StateModB input type has been selected as a change from another type.
			// Repopulate the list if previous choices exist.
			// TODO smalers - probably not needed.
			//__input_name_JComboBox.removeAll();
			inputName_JComboBox.setData ( this.inputNameStateModB );
		}
		// Check the item that is selected.
		String inputName = inputName_JComboBox.getSelected();
		if ( (inputName == null) || inputName.equals(TSToolConstants.BROWSE) ) {
			// Prompt for the name of a StateMod binary file.
			// Based on the file extension, set the data types and other information.
			JFileChooser fc = JFileChooserFactory.createJFileChooser ( JGUIUtil.getLastFileDialogDirectory() );
			fc.setDialogTitle("Select StateMod Binary Output File");
			SimpleFileFilter sff = new SimpleFileFilter("b43","Diversion, Stream, Instream stations (Monthly)");
			fc.addChoosableFileFilter( sff );
			fc.addChoosableFileFilter( new SimpleFileFilter("b49","Diversion, Stream, Instream stations (Daily)") );
			fc.addChoosableFileFilter( new SimpleFileFilter("b44","Reservoir stations (Monthly)") );
			fc.addChoosableFileFilter( new SimpleFileFilter("b50","Reservoir stations (Daily)") );
			fc.addChoosableFileFilter( new SimpleFileFilter("b42","Well stations (Monthly)") );
			fc.addChoosableFileFilter( new SimpleFileFilter("b65","Well stations (Daily)") );
			fc.setFileFilter(sff);
			// Only allow recognized extensions.
			fc.setAcceptAllFileFilterUsed ( false );
			if (fc.showOpenDialog(this.tstoolJFrame) != JFileChooser.APPROVE_OPTION) {
				// User canceled - set the file name back to the original and disable other choices.
				if ( inputName != null ) {
					this.tstoolJFrame.ui_SetIgnoreItemEvent ( true );
					inputName_JComboBox.select(null);
					if ( this.inputNameStateModBLast != null ) {
						inputName_JComboBox.select ( this.inputNameStateModBLast );
					}
					this.tstoolJFrame.ui_SetIgnoreItemEvent ( false );
				}
				return;
			}
			// User has chosen a file.

			inputName = fc.getSelectedFile().getPath();
			// Save as last selection.
			this.inputNameStateModBLast = inputName;
			JGUIUtil.setLastFileDialogDirectory (fc.getSelectedFile().getParent() );

			// Set the input name.

			this.tstoolJFrame.ui_SetIgnoreItemEvent ( true );
			if ( !JGUIUtil.isSimpleJComboBoxItem (inputName_JComboBox,TSToolConstants.BROWSE, JGUIUtil.NONE, null, null ) ) {
				// Not already in so add it at the beginning.
				this.inputNameStateModB.add ( TSToolConstants.BROWSE );
				inputName_JComboBox.add ( TSToolConstants.BROWSE );
			}
			if ( !JGUIUtil.isSimpleJComboBoxItem (inputName_JComboBox,inputName, JGUIUtil.NONE, null, null ) ) {
				// Not already in so add after the browse string
				// (files are listed chronologically by select with most recent at the top).
				if ( inputName_JComboBox.getItemCount() > 1 ) {
					inputName_JComboBox.addAt ( inputName, 1 );
					this.inputNameStateModB.add ( 1, inputName );
				}
				else {
					inputName_JComboBox.add ( inputName );
					this.inputNameStateModB.add(inputName);
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
		String extension = IOUtil.getFileExtension ( inputName );

		List<String> dataTypes = null;
		int interval_base = TimeInterval.MONTH;	// Default.
		int comp = StateMod_DataSet.COMP_UNKNOWN;
		if ( extension.equalsIgnoreCase("b42" ) ) {
			// Wells - monthly.
			comp = StateMod_DataSet.COMP_WELL_STATIONS;
		}
		else if ( extension.equalsIgnoreCase("b43" ) ) {
			// Diversions, streamflow, instream - monthly Use diversions below.
			comp = StateMod_DataSet.COMP_DIVERSION_STATIONS;
		}
		else if ( extension.equalsIgnoreCase("b44" ) ) {
			// Reservoirs - monthly
			comp = StateMod_DataSet.COMP_RESERVOIR_STATIONS;
		}
		else if ( extension.equalsIgnoreCase("b49" ) ) {
			// Diversions, streamflow, instream - daily. Use diversions below.
			interval_base = TimeInterval.DAY;
			comp = StateMod_DataSet.COMP_DIVERSION_STATIONS;
		}
		else if ( extension.equalsIgnoreCase("b50" ) ) {
			// Reservoir - daily
			interval_base = TimeInterval.DAY;
			comp = StateMod_DataSet.COMP_RESERVOIR_STATIONS;
		}
		else if ( extension.equalsIgnoreCase("b65" ) ) {
			// Well - daily
			interval_base = TimeInterval.DAY;
			comp = StateMod_DataSet.COMP_WELL_STATIONS;
		}
		// TODO smalers 2006-01-15  The following is not overly efficient because of a transition in StateMod versions.
		// The version of the format is determined from the file.
		// For older versions, this is used to return hard-coded parameter lists.
		// For newer formats, the binary file is reopened and the parameters are determined from the file.
		dataTypes = StateMod_Util.getTimeSeriesDataTypes (
				inputName,	// Name of binary file.
				comp,	// Component from above, from file extension.
				null,	// ID.
				null,	// Dataset.
				StateMod_BTS.determineFileVersion(inputName),
				interval_base,
				false,	// Include input (only output here).
				false,	// Include input, estimated (only output here).
				true,	// Include output (what the binaries contain).
				false,	// Check availability.
				true,	// Add group (if available).
				false );// Add note.

		// Fill data types choice.

		Message.printStatus ( 2, routine, "Setting StateModB data types (" + dataTypes.size() + " types)..." );
		dataType_JComboBox.setData ( dataTypes );
		Message.printStatus ( 2, routine, "Selecting the first StateModB data type..." );
		dataType_JComboBox.select ( null );
		dataType_JComboBox.select ( 0 );

		// Set time step appropriately.

		timeStep_JComboBox.removeAll ();
		if ( interval_base == TimeInterval.MONTH ) {
			timeStep_JComboBox.add ( TSToolConstants.TIMESTEP_MONTH );
		}
		else if ( interval_base == TimeInterval.DAY ) {
			timeStep_JComboBox.add ( TSToolConstants.TIMESTEP_DAY );
		}
		timeStep_JComboBox.setEnabled ( true ); // Enabled, but one visible.
		timeStep_JComboBox.select ( null );
		timeStep_JComboBox.select ( 0 );

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
	 * Transfer one time series catalog row to a command, for daily time series.
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