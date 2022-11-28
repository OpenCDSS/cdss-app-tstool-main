// TSTool_Modsim - integration between TSTool UI and MODSIM model files

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

package cdss.app.tstool.datastore.csu.modsim;

import java.io.IOException;
import java.util.List;

import javax.swing.JFileChooser;

import DWR.DMI.tstool.TSToolConstants;
import DWR.DMI.tstool.TSTool_JFrame;
import DWR.DMI.tstool.TSTool_TS_CellRenderer;
import DWR.DMI.tstool.TSTool_TS_TableModel;
import RTi.TS.ModsimTS;
import RTi.Util.GUI.JFileChooserFactory;
import RTi.Util.GUI.JGUIUtil;
import RTi.Util.GUI.JWorksheet;
import RTi.Util.GUI.JWorksheet_AbstractRowTableModel;
import RTi.Util.GUI.SimpleFileFilter;
import RTi.Util.GUI.SimpleJComboBox;
import RTi.Util.Message.Message;

/**
 * This class provides integration between TSTool and MODSIM model files.
 * These datastores are currently built into TSTool, rather than being a plugin.
 * Isolating the code simplifies maintenance.
 * The class is a singleton.  Use getInstance() to get the instance to call methods.
 */
public class TSTool_Modsim {

	/**
	 * Singleton instance of this class.
	 * Use getInstance() to return the singleton.
	 */
	private static TSTool_Modsim instance = null;
	
	/**
	 * Instance of the TSTool main UI.
	 */
	private TSTool_JFrame tstoolJFrame = null;

	/**
	 * Private constructor for lazy initialization.
	 */
	private TSTool_Modsim ( TSTool_JFrame tstoolJFrame ) {
		this.tstoolJFrame = tstoolJFrame;
	}

	/**
	 * Handle data type selection.
	 */
	public void dataTypeSelected () {
		// MODSIM file.
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
	public static TSTool_Modsim getInstance ( TSTool_JFrame tstoolJFrame ) {
		if ( TSTool_Modsim.instance == null ) {
			TSTool_Modsim.instance = new TSTool_Modsim( tstoolJFrame );
		}
		return TSTool_Modsim.instance;
	}

	/**
	Read the list of time series from a MODSIM file and list in the GUI.
	*/
	public void getTimeSeriesListClicked_ReadModsimCatalog ()
	throws IOException {
	 	String message, routine = getClass().getSimpleName() + ".getTimeSeriesListClicked_ReadModsimCatalog";

	 	try {
	 		JFileChooser fc = JFileChooserFactory.createJFileChooser ( JGUIUtil.getLastFileDialogDirectory() );
	 		fc.setDialogTitle ( "Select MODSIM Output File" );
	 		SimpleFileFilter sff = new SimpleFileFilter( "DEM", "Demand Time Series");
	 		fc.addChoosableFileFilter(sff);
	 		sff = new SimpleFileFilter( "FLO", "Flow Time Series");
	 		fc.addChoosableFileFilter(sff);
	 		sff = new SimpleFileFilter( "GW", "Groundwater Series");
	 		fc.addChoosableFileFilter(sff);
	 		sff = new SimpleFileFilter( "RES", "Reservoir Time Series");
	 		fc.addChoosableFileFilter(sff);
	 		if ( fc.showOpenDialog(this.tstoolJFrame) != JFileChooser.APPROVE_OPTION) {
	 			// Canceled.
	 			return;
	 		}
	 		String directory = fc.getSelectedFile().getParent();
	 		String path = fc.getSelectedFile().getPath();
	 		JGUIUtil.setLastFileDialogDirectory ( directory );

	 		Message.printStatus ( 1, routine, "Reading MODSIM file \"" + path + "\"" );
	 		JGUIUtil.setWaitCursor ( this.tstoolJFrame, true );
	 		List tslist = null;
	 		try {
	 			tslist = ModsimTS.readTimeSeriesList ( path, null, null, null, false );
	 		}
	 		catch ( Exception e ) {
	 			message = "Error reading MODSIM file.";
	 			Message.printWarning ( 2, routine, message );
	 			JGUIUtil.setWaitCursor ( this.tstoolJFrame, false );
	 			throw new IOException ( message );
	 		}
	 		if ( tslist == null ) {
	 			message = "Error reading MODSIM file.";
	 			Message.printWarning ( 2, routine, message );
	 			JGUIUtil.setWaitCursor ( this.tstoolJFrame, false );
	 			throw new IOException ( message );
	 		}
	 		// There should not be any non-null time series so use the list size.
	 		int size = tslist.size();
	 		if ( size == 0 ) {
	 			Message.printStatus ( 1, routine, "No MODSIM TS read." );
	 			this.tstoolJFrame.queryResultsList_Clear ();
	 		}
	 		else {
	 			Message.printStatus ( 1, routine, "" + size + " MODSIM TS read." );
	 			JWorksheet_AbstractRowTableModel query_TableModel = new TSTool_TS_TableModel ( tslist, true );
	 			this.tstoolJFrame.ui_SetTimeSeriesCatalogTableModel ( query_TableModel );
	 			TSTool_TS_CellRenderer cr = new TSTool_TS_CellRenderer( (TSTool_TS_TableModel)query_TableModel);

	 			JWorksheet query_JWorksheet = tstoolJFrame.ui_GetTimeSeriesCatalogWorksheet();
	 			query_JWorksheet.setCellRenderer ( cr );
	 			query_JWorksheet.setModel ( query_TableModel );
	 			// Do not include the alias in the display.
	 			query_JWorksheet.removeColumn ( ((TSTool_TS_TableModel)query_TableModel).COL_ALIAS );
	 			query_JWorksheet.setColumnWidths ( cr.getColumnWidths(), this.tstoolJFrame.getGraphics() );

	 			JGUIUtil.setWaitCursor ( this.tstoolJFrame, false );
	 			this.tstoolJFrame.ui_UpdateStatus ( false );
	 		}
	 		tslist = null;
	 	}
	 	catch ( Exception e ) {
	 		message = "Error reading MODSIM file.";
	 		Message.printWarning ( 2, routine, message );
	 		Message.printWarning ( 2, routine, e );
	 		JGUIUtil.setWaitCursor ( this.tstoolJFrame, false );
	 		throw new IOException ( message );
	 	}
	}

	/**
	Set up the query options because the MODSIM input type has been selected
	*/
	public void selectInputType ()
	throws Exception {
	    this.tstoolJFrame.ui_SetInputNameVisible(false); // Not needed.
	    
	    SimpleJComboBox dataType_JComboBox = this.tstoolJFrame.ui_GetDataTypeJComboBox();
	    
	    // Most information is determined from the file.
	    dataType_JComboBox.setEnabled ( false );
	    dataType_JComboBox.removeAll ();
	    dataType_JComboBox.add( TSToolConstants.DATA_TYPE_AUTO );

	    // Initialize with blank data list.

		JWorksheet_AbstractRowTableModel query_TableModel = new TSTool_TS_TableModel(null);
		this.tstoolJFrame.ui_SetTimeSeriesCatalogTableModel ( query_TableModel );
	    TSTool_TS_CellRenderer cr = new TSTool_TS_CellRenderer((TSTool_TS_TableModel)query_TableModel);

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