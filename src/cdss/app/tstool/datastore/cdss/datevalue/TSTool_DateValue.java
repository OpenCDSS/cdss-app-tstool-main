// TSTool_DateValue - integration between TSTool UI and DateValue files

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

package cdss.app.tstool.datastore.cdss.datevalue;

import java.io.IOException;
import java.util.List;

import javax.swing.JFileChooser;

import DWR.DMI.tstool.TSToolConstants;
import DWR.DMI.tstool.TSTool_JFrame;
import DWR.DMI.tstool.TSTool_TS_CellRenderer;
import DWR.DMI.tstool.TSTool_TS_TableModel;
import RTi.TS.DateValueTS;
import RTi.TS.TS;
import RTi.Util.GUI.JFileChooserFactory;
import RTi.Util.GUI.JGUIUtil;
import RTi.Util.GUI.JWorksheet;
import RTi.Util.GUI.JWorksheet_AbstractRowTableModel;
import RTi.Util.GUI.SimpleFileFilter;
import RTi.Util.GUI.SimpleJComboBox;
import RTi.Util.IO.IOUtil;
import RTi.Util.Message.Message;

/**
 * This class provides integration between TSTool and DateValue files.
 * These datastores are currently built into TSTool, rather than being a plugin.
 * Isolating the code simplifies maintenance.
 * The class is a singleton.  Use getInstance() to get the instance to call methods.
 */
public class TSTool_DateValue {

	/**
	 * Singleton instance of this class.
	 * Use getInstance() to return the singleton.
	 */
	private static TSTool_DateValue instance = null;
	
	/**
	 * Instance of the TSTool main UI.
	 */
	private TSTool_JFrame tstoolJFrame = null;

	/**
	 * Private constructor for lazy initialization.
	 */
	private TSTool_DateValue ( TSTool_JFrame tstoolJFrame ) {
		this.tstoolJFrame = tstoolJFrame;
	}
	
	/**
	 * Handle data type selection.
	 */
	public void dataTypeSelected () {
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
	public static TSTool_DateValue getInstance ( TSTool_JFrame tstoolJFrame ) {
		if ( TSTool_DateValue.instance == null ) {
			TSTool_DateValue.instance = new TSTool_DateValue( tstoolJFrame );
		}
		return TSTool_DateValue.instance;
	}

	/**
	Read the list of time series from a DateValue file and list in the GUI.
	*/
	public void getTimeSeriesListClicked_ReadDateValueCatalog ()
	throws IOException {
		String message, routine = getClass().getSimpleName() + ".getTimeSeriesListClicked_ReadDateValueCatalog";

		try {
			JFileChooser fc = JFileChooserFactory.createJFileChooser ( JGUIUtil.getLastFileDialogDirectory() );
			fc.setDialogTitle ( "Select DateValue Time Series File" );
			SimpleFileFilter dv_sff = new SimpleFileFilter( "dv", "DateValue Time Series");
			fc.addChoosableFileFilter(dv_sff);
			SimpleFileFilter sff = new SimpleFileFilter( "txt", "DateValue Time Series");
			fc.addChoosableFileFilter(sff);
			fc.setFileFilter(dv_sff);
			if ( fc.showOpenDialog(this.tstoolJFrame) != JFileChooser.APPROVE_OPTION) {
				// Canceled.
				return;
			}
			String directory = fc.getSelectedFile().getParent();
			String path = fc.getSelectedFile().getPath();
			JGUIUtil.setLastFileDialogDirectory ( directory );

			Message.printStatus ( 1, routine, "Reading DateValue file \"" + path + "\"" );
			JGUIUtil.setWaitCursor ( this.tstoolJFrame, true );
			List<TS> tslist = null;
			try {
				tslist = DateValueTS.readTimeSeriesList ( path, null, null, null, false );
			}
			catch ( Exception e ) {
				message = "Error reading DateValue file.";
				Message.printWarning ( 2, routine, message );
				JGUIUtil.setWaitCursor ( this.tstoolJFrame, false );
				throw new IOException ( message );
			}
			if ( tslist == null ) {
				message = "Error reading DateValue file.";
				Message.printWarning ( 2, routine, message );
				JGUIUtil.setWaitCursor ( this.tstoolJFrame, false );
				throw new IOException ( message );
			}
			// There should not be any non-null time series so use the list size.
			int size = tslist.size();
			if ( size == 0 ) {
				Message.printStatus ( 1, routine, "No DateValue TS read." );
				this.tstoolJFrame.queryResultsList_Clear ();
			}
			else {
				Message.printStatus ( 1, routine, "" + size + " DateValue TS read." );
				// Include the alias in the display.
	    		JWorksheet_AbstractRowTableModel query_TableModel = new TSTool_TS_TableModel ( tslist, true );
	    		this.tstoolJFrame.ui_SetTimeSeriesCatalogTableModel ( query_TableModel );
				TSTool_TS_CellRenderer cr = new TSTool_TS_CellRenderer(	(TSTool_TS_TableModel)query_TableModel);

	    		JWorksheet query_JWorksheet = tstoolJFrame.ui_GetTimeSeriesCatalogWorksheet();
				query_JWorksheet.setCellRenderer ( cr );
				query_JWorksheet.setModel ( query_TableModel );
				query_JWorksheet.setColumnWidths (cr.getColumnWidths(), this.tstoolJFrame.getGraphics() );

				JGUIUtil.setWaitCursor ( this.tstoolJFrame, false );
				this.tstoolJFrame.ui_UpdateStatus ( false );
			}
			tslist = null;
		}
		catch ( Exception e ) {
			message = "Error reading DateValue file.";
			Message.printWarning ( 2, routine, message );
			Message.printWarning ( 2, routine, e );
			JGUIUtil.setWaitCursor ( this.tstoolJFrame, false );
			throw new IOException ( message );
		}
	}

	/**
	Set the query options because the DateValue input type has been selected.
	*/
	public void selectInputType ()
	throws Exception {
	    String routine = getClass().getSimpleName() + ".uiAction_SelectInputName_DateValue";
	    // Most information is determined from the file so set the other choices to be inactive.
	    this.tstoolJFrame.ui_SetInputNameVisible(false); // Not used
	    
	    SimpleJComboBox inputName_JComboBox = this.tstoolJFrame.ui_GetInputNameJComboBox();
	    SimpleJComboBox dataType_JComboBox = this.tstoolJFrame.ui_GetDataTypeJComboBox();

	    inputName_JComboBox.removeAll();
	    inputName_JComboBox.setEnabled ( false );

	    dataType_JComboBox.setEnabled ( false );
	    dataType_JComboBox.removeAll ();
	    dataType_JComboBox.add( TSToolConstants.DATA_TYPE_AUTO );

	    // Initialize with blank data list.

		JWorksheet_AbstractRowTableModel query_TableModel = new TSTool_TS_TableModel ( null, true );
   		this.tstoolJFrame.ui_SetTimeSeriesCatalogTableModel ( query_TableModel );
	    TSTool_TS_CellRenderer cr = new TSTool_TS_CellRenderer( (TSTool_TS_TableModel)query_TableModel);

	    // TODO Seems to be null at startup?
   		JWorksheet query_JWorksheet = tstoolJFrame.ui_GetTimeSeriesCatalogWorksheet();
	    if ( query_JWorksheet != null ) {
	    	try {
	    		query_JWorksheet.setCellRenderer ( cr );
	    		query_JWorksheet.setModel ( query_TableModel );
	    		query_JWorksheet.setColumnWidths ( cr.getColumnWidths() );
	    	}
	    	catch ( Exception e ) {
	    		// Absorb the exception in most cases - print if developing to see if this issue can be resolved.
	    		if ( Message.isDebugOn && IOUtil.testing()  ) {
	    			Message.printWarning ( 3, routine,
    					"For developers:  caught exception in clearQueryList JWorksheet at setup." );
	    			Message.printWarning ( 3, routine, e );
	    		}
	    	}
	    }
	}

	/**
	 * Transfer one time series catalog row to a command, for daily time series.
	 * @param row the time series catalog row to transfer (0+)
	 * @param useAlias whether to use an alias
	 */
    public int transferOneTimeSeriesCatalogRowToCommands ( int row, boolean useAlias, int insertOffset ) {
		// The location (id), type, and time step uniquely identify the time series,
    	// but the input_name is needed to find the data.
		// If an alias is specified, assume that it should be used instead of the normal TSID.
		JWorksheet_AbstractRowTableModel query_TableModel = this.tstoolJFrame.ui_GetTimeSeriesCatalogTableModel();
		TSTool_TS_TableModel model = (TSTool_TS_TableModel)query_TableModel;
		String alias = ((String)model.getValueAt ( row, model.COL_ALIAS )).trim();
		if ( !alias.equals("") ) {
			// Alias is available so use it.
			useAlias = true;
		}
		// TODO smalers 2004-01-06 - for now don't use the alias to put
		// together the ID - there are issues to resolve in how
		// DateValueTS.readTimeSeries() handles the ID.
		useAlias = false;
		int numCommandsAdded = 0;
		if ( useAlias ) {
			// Use the alias instead of the identifier.
		    try {
		        numCommandsAdded = this.tstoolJFrame.queryResultsList_AppendTSIDToCommandList ( alias,
    			null,
    			null,
    			null,
    			null,
    			null, // No sequence number.
    			(String)query_TableModel.getValueAt( row, model.COL_INPUT_TYPE),
    			(String)query_TableModel.getValueAt( row, model.COL_INPUT_NAME), "", useAlias, insertOffset );
		        return numCommandsAdded;
			}
			catch ( Exception e ) {
				Message.printWarning ( 3, "", e );
			}
		}
		else {
		    // Use full ID and all other fields.
			Message.printStatus ( 1, "", "Calling append (no alias)..." );
			numCommandsAdded = this.tstoolJFrame.queryResultsList_AppendTSIDToCommandList (
			(String)query_TableModel.getValueAt ( row, model.COL_ID ),
			(String)query_TableModel.getValueAt ( row, model.COL_DATA_SOURCE),
			(String)query_TableModel.getValueAt ( row, model.COL_DATA_TYPE),
			(String)query_TableModel.getValueAt ( row, model.COL_TIME_STEP),
			(String)query_TableModel.getValueAt ( row, model.COL_SCENARIO),
			(String)query_TableModel.getValueAt ( row, model.COL_SEQUENCE),
			(String)query_TableModel.getValueAt ( row, model.COL_INPUT_TYPE),
			(String)query_TableModel.getValueAt ( row, model.COL_INPUT_NAME), "",
			useAlias, insertOffset );
		}
		return numCommandsAdded;
    }
}
