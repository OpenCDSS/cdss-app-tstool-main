// TSTool_NwsrfsCard - integration between TSTool UI and NWSRFS Card files

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

package cdss.app.tstool.datastore.nwsrfs.card;

import java.io.IOException;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import DWR.DMI.tstool.TSToolConstants;
import DWR.DMI.tstool.TSTool_JFrame;
import DWR.DMI.tstool.TSTool_TS_CellRenderer;
import DWR.DMI.tstool.TSTool_TS_TableModel;
import RTi.DMI.NWSRFS_DMI.NWSCardTS;
import RTi.TS.TS;
import RTi.Util.GUI.JFileChooserFactory;
import RTi.Util.GUI.JGUIUtil;
import RTi.Util.GUI.JWorksheet;
import RTi.Util.GUI.JWorksheet_AbstractRowTableModel;
import RTi.Util.GUI.SimpleJComboBox;
import RTi.Util.Message.Message;
import RTi.Util.Time.DateTime;

/**
 * This class provides integration between TSTool and
 * National Weather Service River Forecast System (NWSRFS) Card files.
 * These datastores are currently built into TSTool, rather than being a plugin.
 * Isolating the code simplifies maintenance.
 * The class is a singleton.  Use getInstance() to get the instance to call methods.
 */
public class TSTool_NwsrfsCard {

	/**
	 * Singleton instance of this class.
	 * Use getInstance() to return the singleton.
	 */
	private static TSTool_NwsrfsCard instance = null;
	
	/**
	 * Instance of the TSTool main UI.
	 */
	private TSTool_JFrame tstoolJFrame = null;

	/**
	 * Private constructor for lazy initialization.
	 */
	private TSTool_NwsrfsCard ( TSTool_JFrame tstoolJFrame ) {
		this.tstoolJFrame = tstoolJFrame;
	}
	
	/**
	 * Handle data type selection.
	 */
	public void dataTypeSelected () {
		SimpleJComboBox timeStep_JComboBox = this.tstoolJFrame.ui_GetTimeStepJComboBox();
		timeStep_JComboBox.removeAll ();
		timeStep_JComboBox.add ( TSToolConstants.TIMESTEP_HOUR );
		timeStep_JComboBox.select ( null );
		timeStep_JComboBox.select ( TSToolConstants.TIMESTEP_HOUR );
		timeStep_JComboBox.setEnabled ( false );
	}

	/**
	 * Get the singleton instance.
	 */
	public static TSTool_NwsrfsCard getInstance ( TSTool_JFrame tstoolJFrame ) {
		if ( TSTool_NwsrfsCard.instance == null ) {
			TSTool_NwsrfsCard.instance = new TSTool_NwsrfsCard( tstoolJFrame );
		}
		return TSTool_NwsrfsCard.instance;
	}

	/**
	Read the list of time series from NWS CARD files and list in the GUI.
	*/
	public void getTimeSeriesListClicked_ReadNwsCardCatalog ()
	throws IOException {
		String message, routine = getClass().getSimpleName() + ".getTimeSeriesListClicked_ReadNwsCardCatalog";

		// TODO - need to allow multiple file selections.
		try {
			JFileChooser fc = JFileChooserFactory.createJFileChooser ( JGUIUtil.getLastFileDialogDirectory() );
			fc.setDialogTitle ( "Select NWS Card Time Series File" );

			/*
			SimpleFileFilter card_sff = new SimpleFileFilter( "card", "NWS Card Time Series");
			fc.addChoosableFileFilter(card_sff);
			SimpleFileFilter sff = new SimpleFileFilter( "txt", "NWS Card Time Series");
			fc.addChoosableFileFilter(sff);
			fc.setFileFilter(card_sff);
			*/

			FileFilter[] filters = NWSCardTS.getFileFilters();
			for (int i = 0; i < filters.length - 1; i++) {
				fc.addChoosableFileFilter(filters[i]);
			}
			fc.setFileFilter(filters[filters.length - 1]);

			if ( fc.showOpenDialog(this.tstoolJFrame) != JFileChooser.APPROVE_OPTION) {
				// Canceled.
				return;
			}
			String directory = fc.getSelectedFile().getParent();
			String path = fc.getSelectedFile().getPath();
			JGUIUtil.setLastFileDialogDirectory ( directory );

			Message.printStatus ( 1, routine, "Reading NWS CARD file \"" + path + "\"" );
			JGUIUtil.setWaitCursor ( this.tstoolJFrame, true );
			List<TS> tslist = null;
			try {
				tslist = NWSCardTS.readTimeSeriesList (
						(TS)null,	// No requested time series.
						path,
						(DateTime)null,	// Start
						(DateTime)null,	// End
						(String)null,	// Units
						false );	// No data, header only.
			}
			catch ( Exception e ) {
				message = "Error reading NWS Card file.";
				Message.printWarning ( 2, routine, message );
				JGUIUtil.setWaitCursor ( this.tstoolJFrame, false );
				this.tstoolJFrame.queryResultsList_Clear ();
				throw new IOException ( message );
			}
			if ( tslist == null ) {
				message = "Error reading NWS CARD file.";
				Message.printWarning ( 2, routine, message );
				JGUIUtil.setWaitCursor ( this.tstoolJFrame, false );
				throw new IOException ( message );
			}
			JWorksheet_AbstractRowTableModel query_TableModel = new TSTool_TS_TableModel ( tslist );
			this.tstoolJFrame.ui_SetTimeSeriesCatalogTableModel ( query_TableModel );
			TSTool_TS_CellRenderer cr = new TSTool_TS_CellRenderer( (TSTool_TS_TableModel)query_TableModel);

			JWorksheet query_JWorksheet = tstoolJFrame.ui_GetTimeSeriesCatalogWorksheet();
			query_JWorksheet.setCellRenderer ( cr );
			query_JWorksheet.setModel ( query_TableModel );
			query_JWorksheet.removeColumn ( ((TSTool_TS_TableModel)query_TableModel).COL_ALIAS );
			query_JWorksheet.setColumnWidths ( cr.getColumnWidths(), this.tstoolJFrame.getGraphics() );

			this.tstoolJFrame.ui_UpdateStatus ( false );
			JGUIUtil.setWaitCursor ( this.tstoolJFrame, false );
		}
		catch ( Exception e ) {
			message = "Error reading NWS CARD file.";
			Message.printWarning ( 2, routine, message );
			Message.printWarning ( 2, routine, e );
			JGUIUtil.setWaitCursor ( this.tstoolJFrame, false );
			throw new IOException ( message );
		}
	}

	/**
	Set up query choices because NWS Card input type has been selected.
	*/
	public void selectInputType ()
	throws Exception {
		// Most information is determined from the file.
		this.tstoolJFrame.ui_SetInputNameVisible(false); // Not needed.

		SimpleJComboBox dataType_JComboBox = this.tstoolJFrame.ui_GetDataTypeJComboBox();
		SimpleJComboBox inputName_JComboBox = this.tstoolJFrame.ui_GetInputNameJComboBox();

		inputName_JComboBox.removeAll();
		inputName_JComboBox.setEnabled ( false );

		dataType_JComboBox.setEnabled ( false );
		dataType_JComboBox.removeAll();
		dataType_JComboBox.add( TSToolConstants.DATA_TYPE_AUTO );

		// Initialize with blank data list.

		JWorksheet_AbstractRowTableModel query_TableModel = new TSTool_TS_TableModel(null);
		this.tstoolJFrame.ui_SetTimeSeriesCatalogTableModel ( query_TableModel );
		TSTool_TS_CellRenderer cr = new TSTool_TS_CellRenderer( (TSTool_TS_TableModel)query_TableModel);

		JWorksheet query_JWorksheet = tstoolJFrame.ui_GetTimeSeriesCatalogWorksheet();
		query_JWorksheet.setCellRenderer ( cr );
		query_JWorksheet.setModel ( query_TableModel );
		// Remove columns that are not appropriate.
		query_JWorksheet.removeColumn ( ((TSTool_TS_TableModel)query_TableModel).COL_SEQUENCE );
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