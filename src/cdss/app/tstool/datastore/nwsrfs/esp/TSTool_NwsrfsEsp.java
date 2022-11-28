// TSTool_Esp - integration between TSTool UI and NWSRFS ESP trace ensemble files

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

package cdss.app.tstool.datastore.nwsrfs.esp;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.JFileChooser;

import DWR.DMI.tstool.TSToolConstants;
import DWR.DMI.tstool.TSTool_JFrame;
import DWR.DMI.tstool.TSTool_TS_CellRenderer;
import DWR.DMI.tstool.TSTool_TS_TableModel;
import RTi.DMI.NWSRFS_DMI.NWSRFS_ESPTraceEnsemble;
import RTi.TS.TS;
import RTi.Util.GUI.JFileChooserFactory;
import RTi.Util.GUI.JGUIUtil;
import RTi.Util.GUI.JWorksheet;
import RTi.Util.GUI.JWorksheet_AbstractRowTableModel;
import RTi.Util.GUI.ResponseJDialog;
import RTi.Util.GUI.SimpleFileFilter;
import RTi.Util.GUI.SimpleJComboBox;
import RTi.Util.GUI.TextResponseJDialog;
import RTi.Util.IO.IOUtil;
import RTi.Util.Message.Message;
import rti.tscommandprocessor.commands.nwsrfs.ui.TSTool_ESPTraceEnsemble_CellRenderer;
import rti.tscommandprocessor.commands.nwsrfs.ui.TSTool_ESPTraceEnsemble_TableModel;

/**
 * This class provides integration between TSTool and
 * National Weather Service River Forecast System (NWSRFS) ESP Trace Ensemble files.
 * These datastores are currently built into TSTool, rather than being a plugin.
 * Isolating the code simplifies maintenance.
 * The class is a singleton.  Use getInstance() to get the instance to call methods.
 */
public class TSTool_NwsrfsEsp {

	/**
	 * Singleton instance of this class.
	 * Use getInstance() to return the singleton.
	 */
	private static TSTool_NwsrfsEsp instance = null;
	
	/**
	 * Instance of the TSTool main UI.
	 */
	private TSTool_JFrame tstoolJFrame = null;

	/**
	 * Private constructor for lazy initialization.
	 */
	private TSTool_NwsrfsEsp ( TSTool_JFrame tstoolJFrame ) {
		this.tstoolJFrame = tstoolJFrame;
	}
	
	/**
	 * Convert ESP trace ensemble to other form.
	 */
	public void convertTraceFile () {
		// Prompt for the ESP file.
		JFileChooser fc = JFileChooserFactory.createJFileChooser ( JGUIUtil.getLastFileDialogDirectory() );
		fc.setDialogTitle ( "Select NWSRFS ESP Trace Ensemble File" );
		SimpleFileFilter sff = new SimpleFileFilter ( "CS",	"Conditional Simulation Ensemble File" );
		fc.addChoosableFileFilter ( sff );
		fc.setFileFilter ( sff );
		if ( fc.showOpenDialog(this.tstoolJFrame) != JFileChooser.APPROVE_OPTION) {
			// Return if no file name is selected.
			return;
		}
		String esp_file = fc.getSelectedFile().getPath();
		String directory = fc.getSelectedFile().getParent();
		// Prompt for the output file.
		fc = JFileChooserFactory.createJFileChooser ( directory );
		fc.setDialogTitle ( "Select Text Output File" );
		sff = new SimpleFileFilter ( "txt","NWSRFS ESP Ensemble File as Text" );
		fc.addChoosableFileFilter ( sff );
		fc.setSelectedFile ( new File(esp_file + ".txt") );
		fc.setFileFilter ( sff );
		if ( fc.showSaveDialog(this.tstoolJFrame) != JFileChooser.APPROVE_OPTION) {
			// Return if no file name is selected.
			return;
		}
		directory = fc.getSelectedFile().getParent();
		JGUIUtil.setLastFileDialogDirectory ( directory );
		String out_file = fc.getSelectedFile().getPath();
		String units = new TextResponseJDialog (
			this.tstoolJFrame, "Specify Output Units", "Specify the output units for the file (blank to default).",
			ResponseJDialog.OK| ResponseJDialog.CANCEL ).response();
		if ( units == null ) {
			return;
		}
		units = units.trim();
		// Convert.
		try {
            NWSRFS_ESPTraceEnsemble.convertESPTraceEnsembleToText (
			esp_file, IOUtil.enforceFileExtension(out_file,"txt"), units );
		}
		catch ( Exception e ) {
			Message.printWarning ( 1, "TSTool_JFrame",
			"There was an error converting the ESP trace ensemble file to text.  Partial results may be available." );
		}
	}
	
	/**
	 * Handle data type selection.
	 */
	public void dataTypeSelected () {
		// ESP Trace Ensemble file.
		SimpleJComboBox timeStep_JComboBox = this.tstoolJFrame.ui_GetTimeStepJComboBox();
		timeStep_JComboBox.removeAll ();
		timeStep_JComboBox.add ( TSToolConstants.TIMESTEP_AUTO );
		timeStep_JComboBox.select ( null );
		timeStep_JComboBox.select ( TSToolConstants.TIMESTEP_AUTO );
		timeStep_JComboBox.setEnabled ( false );
	}

	/**
	 * Get the singleton instance.
	 */
	public static TSTool_NwsrfsEsp getInstance ( TSTool_JFrame tstoolJFrame ) {
		if ( TSTool_NwsrfsEsp.instance == null ) {
			TSTool_NwsrfsEsp.instance = new TSTool_NwsrfsEsp( tstoolJFrame );
		}
		return TSTool_NwsrfsEsp.instance;
	}

	/**
	Read the list of time series from an NWSRFS ESPTraceEnsemble file and list in the GUI.
	*/
	public void getTimeSeriesListClicked_ReadNwsrfsEspTraceEnsembleCatalog ()
	throws IOException {
		String message, routine = getClass().getSimpleName() + ".getTimeSeriesListClicked_ReadNwsrfsEspTraceEnsembleCatalog";

		try {
			JFileChooser fc = JFileChooserFactory.createJFileChooser ( JGUIUtil.getLastFileDialogDirectory() );
			fc.setDialogTitle ( "Select ESP Trace Ensemble File" );
			SimpleFileFilter csff = new SimpleFileFilter( "CS", "Conditional Simulation Trace File");
			fc.addChoosableFileFilter(csff);
			/* TODO - add later
		SimpleFileFilter sff = new SimpleFileFilter( "HS",
		"Historical Simulation Trace File");
		fc.addChoosableFileFilter(sff);
			 */
			fc.setFileFilter(csff);
			if ( fc.showOpenDialog(this.tstoolJFrame) != JFileChooser.APPROVE_OPTION) {
				// Canceled.
				return;
			}
			String directory = fc.getSelectedFile().getParent();
			JGUIUtil.setLastFileDialogDirectory ( directory );
			String path = fc.getSelectedFile().getPath();
			Message.printStatus ( 1, routine, "Reading NWSRFS ESPTraceEnsemble file \"" + path + "\"" );
			JGUIUtil.setWaitCursor ( this.tstoolJFrame, true );
			List<TS> tslist = null;
			NWSRFS_ESPTraceEnsemble ensemble = null;
			try {
				ensemble = new NWSRFS_ESPTraceEnsemble ( path, false );
				// Get the array of time series.
				tslist = ensemble.getTimeSeriesList ();
			}
			catch ( Exception e ) {
				message = "Error reading NWSRFS ESPTraceEnsemble file.";
				Message.printWarning ( 2, routine, message );
				Message.printWarning ( 2, routine, e );
				JGUIUtil.setWaitCursor ( this.tstoolJFrame, false );
				throw new IOException ( message );
			}
			int size = 0;
			if ( tslist != null ) {
				size = tslist.size();
				JWorksheet_AbstractRowTableModel query_TableModel = new TSTool_ESPTraceEnsemble_TableModel ( ensemble );
				this.tstoolJFrame.ui_SetTimeSeriesCatalogTableModel ( query_TableModel );
				TSTool_ESPTraceEnsemble_CellRenderer cr =
						new TSTool_ESPTraceEnsemble_CellRenderer( (TSTool_ESPTraceEnsemble_TableModel)query_TableModel);

				JWorksheet query_JWorksheet = tstoolJFrame.ui_GetTimeSeriesCatalogWorksheet();
				query_JWorksheet.setCellRenderer ( cr );
				query_JWorksheet.setModel ( query_TableModel );
				query_JWorksheet.setColumnWidths ( cr.getColumnWidths(), this.tstoolJFrame.getGraphics() );
			}
			if ( (tslist == null) || (size == 0) ) {
				Message.printStatus ( 1, routine, "No NWSRFS ESPTraceEnsemble TS read." );
				this.tstoolJFrame.queryResultsList_Clear ();
			}
			else {
				Message.printStatus ( 1, routine, "" + size + " NWSRFS ESPTraceEnsemble TS traces read." );
			}

			JGUIUtil.setWaitCursor ( this.tstoolJFrame, false );
			this.tstoolJFrame.ui_UpdateStatus ( false );
		}
		catch ( Exception e ) {
			message = "Error reading NWSRFS ESPTraceEnsemble file.";
			Message.printWarning ( 2, routine, message );
			Message.printWarning ( 2, routine, e );
			JGUIUtil.setWaitCursor ( this.tstoolJFrame, false );
			throw new IOException ( message );
		}
	}

	/**
	Set up query choices because NWSRFS ESP trace ensemble input type was selected.
	*/
	public void selectInputType ()
	throws Exception {
		SimpleJComboBox dataType_JComboBox = this.tstoolJFrame.ui_GetDataTypeJComboBox();
		SimpleJComboBox inputName_JComboBox = this.tstoolJFrame.ui_GetInputNameJComboBox();

		// Most information is determined from the file.
		this.tstoolJFrame.ui_SetInputNameVisible(false); // Not needed.
		inputName_JComboBox.removeAll();
		inputName_JComboBox.setEnabled ( false );

		dataType_JComboBox.setEnabled ( false );
		dataType_JComboBox.removeAll ();
		dataType_JComboBox.add( TSToolConstants.DATA_TYPE_AUTO );

		// Initialize with blank data list.

		JWorksheet_AbstractRowTableModel query_TableModel = new TSTool_TS_TableModel ( null, true );
		this.tstoolJFrame.ui_SetTimeSeriesCatalogTableModel ( query_TableModel );
		TSTool_TS_CellRenderer cr = new TSTool_TS_CellRenderer( (TSTool_TS_TableModel)query_TableModel);

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
    public int transferOneTimeSeriesCatalogRowToCommands ( int row, boolean useAlias, int insertOffset,
		JWorksheet_AbstractRowTableModel query_TableModel ) {
		// The location (id), type, and time step uniquely identify the time series,
		// but the input_name is needed to find the data.
		// If an alias is specified, assume that it should be used instead of the normal TSID.
		TSTool_ESPTraceEnsemble_TableModel model = (TSTool_ESPTraceEnsemble_TableModel)query_TableModel;
		//String alias = ((String)__query_TableModel.getValueAt ( row, model.COL_ALIAS )).trim();
		boolean useAliasInId = false;
		int numCommandsAdded = 0;
		if ( useAliasInId ) {
			// Need to figure out what to do here.
		}
		else {
		    // Use full ID and all other fields.
		    numCommandsAdded = this.tstoolJFrame.queryResultsList_AppendTSIDToCommandList (
			(String)query_TableModel.getValueAt ( row, model.COL_ID ),
			(String)query_TableModel.getValueAt ( row, model.COL_DATA_SOURCE),
			(String)query_TableModel.getValueAt ( row, model.COL_DATA_TYPE),
			(String)query_TableModel.getValueAt ( row, model.COL_TIME_STEP),
			(String)query_TableModel.getValueAt ( row, model.COL_SCENARIO),
			(String)query_TableModel.getValueAt ( row, model.COL_SEQUENCE),
			(String)query_TableModel.getValueAt ( row, model.COL_INPUT_TYPE),
			(String)query_TableModel.getValueAt ( row, model.COL_INPUT_NAME), "",
			false, insertOffset );
		}
		return numCommandsAdded;
    }

}