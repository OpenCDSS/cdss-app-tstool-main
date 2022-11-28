// TSTool_RiverWare - integration between TSTool UI and RiverWare files

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

package cdss.app.tstool.datastore.usbr.riverware;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;

import DWR.DMI.tstool.TSToolConstants;
import DWR.DMI.tstool.TSTool_JFrame;
import DWR.DMI.tstool.TSTool_TS_CellRenderer;
import DWR.DMI.tstool.TSTool_TS_TableModel;
import RTi.TS.RiverWareTS;
import RTi.TS.TS;
import RTi.Util.GUI.JFileChooserFactory;
import RTi.Util.GUI.JGUIUtil;
import RTi.Util.GUI.JWorksheet;
import RTi.Util.GUI.JWorksheet_AbstractRowTableModel;
import RTi.Util.GUI.SimpleFileFilter;
import RTi.Util.GUI.SimpleJComboBox;
import RTi.Util.Message.Message;

/**
 * This class provides integration between TSTool and RiverWare files.
 * These files are currently built into TSTool, rather than being a plugin.
 * Isolating the code simplifies maintenance.
 * The class is a singleton.  Use getInstance() to get the instance to call methods.
 */
public class TSTool_RiverWare {
	/**
	 * Singleton instance of this class.
	 * Use getInstance() to return the singleton.
	 */
	private static TSTool_RiverWare instance = null;
	
	/**
	 * Instance of the TSTool main UI.
	 */
	private TSTool_JFrame tstoolJFrame = null;
	
	/**
	 * Private constructor for lazy initialization.
	 */
	private TSTool_RiverWare ( TSTool_JFrame tstoolJFrame ) {
		this.tstoolJFrame = tstoolJFrame;
	}

	/**
	 * Handle data type selection for daily datastore.
	 * @param dataStore selected datastore
	 * @param selectedDataType selected data type
	 */
	public void dataTypeSelected () {
		// RiverWare file.
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
	public static TSTool_RiverWare getInstance ( TSTool_JFrame tstoolJFrame ) {
		if ( TSTool_RiverWare.instance == null ) {
			TSTool_RiverWare.instance = new TSTool_RiverWare( tstoolJFrame );
		}
		return TSTool_RiverWare.instance;
	}

	/**
	Read the list of time series from RiverWare files and list in the GUI.
	*/
	public void getTimeSeriesListClicked_ReadRiverWareCatalog ()
	throws IOException {
	 	String message, routine = getClass().getSimpleName() + ".getTimeSeriesListClicked_ReadRiverWareCatalog";

	 	try {
	 		JFileChooser fc = JFileChooserFactory.createJFileChooser ( JGUIUtil.getLastFileDialogDirectory() );
	 		fc.setDialogTitle ( "Select RiverWare Time Series File" );
	 		SimpleFileFilter sff = new SimpleFileFilter( "dat",	"RiverWare Time Series");
	 		fc.addChoosableFileFilter(sff);
	 		sff = new SimpleFileFilter( "rwts", "RiverWare Time Series");
	 		fc.addChoosableFileFilter(sff);
	 		sff = new SimpleFileFilter( "ts", "RiverWare Time Series");
	 		fc.addChoosableFileFilter(sff);
	 		if ( fc.showOpenDialog(this.tstoolJFrame) != JFileChooser.APPROVE_OPTION) {
	 			// Canceled.
	 			return;
	 		}
	 		JGUIUtil.setLastFileDialogDirectory ( fc.getSelectedFile().getParent() );
	 		String path = fc.getSelectedFile().getPath();

	 		Message.printStatus ( 1, routine, "Reading RiverWare file \"" + path + "\"" );
	 		JGUIUtil.setWaitCursor ( this.tstoolJFrame, true );
	 		TS ts = RiverWareTS.readTimeSeries ( path, null, null, null, false );
	 		if ( ts == null ) {
	 			message = "Error reading RiverWare file.";
	 			Message.printWarning ( 2, routine, message );
	 			JGUIUtil.setWaitCursor ( this.tstoolJFrame, false );
	 			throw new IOException ( message );
	 		}
	 		List<TS>tslist = new ArrayList<>( 1 );
	 		tslist.add ( ts );
    		JWorksheet_AbstractRowTableModel query_TableModel = new TSTool_TS_TableModel ( tslist );
    		this.tstoolJFrame.ui_SetTimeSeriesCatalogTableModel ( query_TableModel );
	 		TSTool_TS_CellRenderer cr = new TSTool_TS_CellRenderer( (TSTool_TS_TableModel)query_TableModel);

    		JWorksheet query_JWorksheet = tstoolJFrame.ui_GetTimeSeriesCatalogWorksheet();
	 		query_JWorksheet.setCellRenderer ( cr );
	 		query_JWorksheet.setModel ( query_TableModel );
	 		query_JWorksheet.removeColumn (((TSTool_TS_TableModel)query_TableModel).COL_ALIAS );
	 		query_JWorksheet.setColumnWidths ( cr.getColumnWidths(), this.tstoolJFrame.getGraphics() );

	 		this.tstoolJFrame.ui_UpdateStatus ( false );
	 		JGUIUtil.setWaitCursor ( this.tstoolJFrame, false );
	 	}
	 	catch ( Exception e ) {
	 		message = "Error reading RiverWare file.";
	 		Message.printWarning ( 2, routine, message );
	 		Message.printWarning ( 3, routine, e );
	 		JGUIUtil.setWaitCursor ( this.tstoolJFrame, false );
	 		throw new IOException ( message );
	 	}
	}

	/**
	Set up query choices because the Riverware input type has been selected.
	*/
	public void selectInputType ()
	throws Exception {
		// Most information is determined from the file.
		this.tstoolJFrame.ui_SetInputNameVisible(false); // Not needed.
		SimpleJComboBox inputName_JComboBox = this.tstoolJFrame.ui_GetInputNameJComboBox();
		inputName_JComboBox.removeAll();
		inputName_JComboBox.setEnabled ( false );

		SimpleJComboBox dataType_JComboBox = this.tstoolJFrame.ui_GetDataTypeJComboBox();
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