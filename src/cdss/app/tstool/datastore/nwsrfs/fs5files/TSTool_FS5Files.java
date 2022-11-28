// TSTool_FS5Files - integration between TSTool UI and NWSRFS FS5Files files

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

package cdss.app.tstool.datastore.nwsrfs.fs5files;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import DWR.DMI.tstool.TSToolConstants;
import DWR.DMI.tstool.TSToolMain;
import DWR.DMI.tstool.TSTool_JFrame;
import DWR.DMI.tstool.TSTool_TS_CellRenderer;
import DWR.DMI.tstool.TSTool_TS_TableModel;
import RTi.DMI.NWSRFS_DMI.NWSRFS_DMI;
import RTi.DMI.NWSRFS_DMI.NWSRFS_TS_InputFilter_JPanel;
import RTi.DMI.NWSRFS_DMI.NWSRFS_Util;
import RTi.TS.TS;
import RTi.Util.GUI.InputFilter_JPanel;
import RTi.Util.GUI.JFileChooserFactory;
import RTi.Util.GUI.JGUIUtil;
import RTi.Util.GUI.JWorksheet;
import RTi.Util.GUI.JWorksheet_AbstractRowTableModel;
import RTi.Util.GUI.SimpleJComboBox;
import RTi.Util.IO.IOUtil;
import RTi.Util.IO.PropList;
import RTi.Util.Message.Message;
import RTi.Util.String.StringUtil;
import RTi.Util.Time.DateTime;

/**
 * This class provides integration between TSTool and
 * National Weather Service River Forecast System (NWSRFS) FS5Files files.
 * These datastores are currently built into TSTool, rather than being a plugin.
 * Isolating the code simplifies maintenance.
 * The class is a singleton.  Use getInstance() to get the instance to call methods.
 */
public class TSTool_FS5Files {

	/**
	 * Singleton instance of this class.
	 * Use getInstance() to return the singleton.
	 */
	private static TSTool_FS5Files instance = null;
	
	/**
	 * Instance of the TSTool main UI.
	 */
	private TSTool_JFrame tstoolJFrame = null;

	/**
	InputFilter_JPanel for NWSRFS_FS5Files time series.
	*/
	private InputFilter_JPanel inputFilterNWSRFSFS5Files_JPanel = null;

	/**
	The NWSFFS FS5Files directories that have been selected during the session,
	to allow switching between input types but not losing the list of files.
	*/
	private List<String> input_name_NWSRFS_FS5Files = new ArrayList<>();

	/**
	NWSRFS_DMI object for NWSRFS_FS5Files input type,
	opened via TSTool.cfg information and the NWSRFS FS5Files directory select dialog,
	provided to the processor as the initial NWSRFSS DMI instance.
	*/
	private NWSRFS_DMI nwsrfs_dmi = null;

	/**
	 * Private constructor for lazy initialization.
	 */
	private TSTool_FS5Files ( TSTool_JFrame tstoolJFrame ) {
		this.tstoolJFrame = tstoolJFrame;
	}

	/**
	Check the GUI for NWSRFS FS5Files features.
	*/
	public void checkNwsrfsFS5FilesFeatures ( JMenuItem File_Properties_NWSRFSFS5Files_JMenuItem ) {
		SimpleJComboBox inputType_JComboBox = this.tstoolJFrame.ui_GetInputTypeJComboBox();
		if ( (this.nwsrfs_dmi != null) && (inputType_JComboBox != null) ) {
			// Make sure NWSRFS FS5Files is in the data source list.
			int count = inputType_JComboBox.getItemCount();
			boolean rfound = false;
			for ( int i = 0; i < count; i++ ) {
				if ( inputType_JComboBox.getItem(i).equals(TSToolConstants.INPUT_TYPE_NWSRFS_FS5Files) ) {
					rfound = true;
					break;
				}
			}
			if ( !rfound ) {
				// Repopulate the data sources.
				this.tstoolJFrame.ui_SetInputTypeChoices();
			}
			if (File_Properties_NWSRFSFS5Files_JMenuItem != null ) {
				File_Properties_NWSRFSFS5Files_JMenuItem.setEnabled(true);
			}
		}
		else {
			// NWSRFS FS5Files connection failed, but do not remove from
			// input types because a new connection currently can be established from the interactive.
			/*
		try {
		    __inputType_JComboBox.remove(__INPUT_TYPE_NWSRFS_FS5FILES);
		}
		catch ( Exception e ) {
			// Ignore - probably already removed.
		}
			 */
			if ( File_Properties_NWSRFSFS5Files_JMenuItem != null ) {
				File_Properties_NWSRFSFS5Files_JMenuItem.setEnabled ( false );
			}
		}
	}

	/**
	 * Handle data type selection.
	 */
	public void dataTypeSelected ( String selectedDataType ) {
		// Time steps are determined from the system.
		SimpleJComboBox timeStep_JComboBox = this.tstoolJFrame.ui_GetTimeStepJComboBox();
	    List<String> time_steps = NWSRFS_Util.getDataTypeIntervals ( this.nwsrfs_dmi, selectedDataType );
		timeStep_JComboBox.setData ( time_steps );
		timeStep_JComboBox.select ( null );
		timeStep_JComboBox.setEnabled ( true );
		// Select 6Hour as the default if available.
		if ( JGUIUtil.isSimpleJComboBoxItem(timeStep_JComboBox,"6Hour", JGUIUtil.NONE, null, null ) ) {
			timeStep_JComboBox.select ( "6Hour" );
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
	public static TSTool_FS5Files getInstance ( TSTool_JFrame tstoolJFrame ) {
		if ( TSTool_FS5Files.instance == null ) {
			TSTool_FS5Files.instance = new TSTool_FS5Files( tstoolJFrame );
		}
		return TSTool_FS5Files.instance;
	}

	/**
	Return the NWSRFSFS5Files instance that is active for the UI,
	opened from the configuration file information or the NWSRFS FS5Files select dialog.
	@return the NWSRFSDMI instance that is active for the UI.
	 */
	public NWSRFS_DMI getNwsrfsFS5FilesDMI () {
		return this.nwsrfs_dmi;
	}
	
	/**
	Read the list of time series from NWSRFS FS5Files and list in the GUI.
	 */
	public void getTimeSeriesListClicked_ReadNwsrfsFS5FilesCatalog ()
	throws IOException {
	 	String message, routine = getClass().getSimpleName() + ".getTimeSeriesListClicked_ReadNwsrfsFS5FilesCatalog";

	 	SimpleJComboBox dataType_JComboBox = this.tstoolJFrame.ui_GetDataTypeJComboBox();
	 	SimpleJComboBox inputName_JComboBox = this.tstoolJFrame.ui_GetInputNameJComboBox();
	 	try {
	 		JGUIUtil.setWaitCursor ( this.tstoolJFrame, true );
	 		List<TS> tslist = null;
	 		// Default is to return all IDs.
	 		String id_input = "*";
	 		// Get the ID from the input filter.
	 		List<String> input_List = this.inputFilterNWSRFSFS5Files_JPanel.getInput ( "ID", null, true, null );
	 		int isize = input_List.size();
	 		if ( isize > 1 ) {
	 			Message.printWarning ( 1, routine, "Only one input filter for ID can be specified." );
	 			return;
	 		}
	 		if ( isize > 0 ) {
	 			// Use the first matching filter.
	 			id_input = (String)input_List.get(0);
	 		}
	 		String datatype = StringUtil.getToken ( dataType_JComboBox.getSelected().trim(), " ",0, 0);
	 		String selectedTimeStep = this.tstoolJFrame.ui_GetSelectedTimeStep();
	 		// Parse the interval into the integer hour.
	 		String selected_input_name = inputName_JComboBox.getSelected();
	 		try {
	 			String tsident_string = id_input + ".NWSRFS." + datatype + "." +
	 					selectedTimeStep + "~NWSRFS_FS5Files~" + selected_input_name;
	 			Message.printStatus ( 2, routine, "Reading NWSRFS FS5Files time series for \"" + tsident_string + "\"..." );
	 			tslist = this.nwsrfs_dmi.readTimeSeriesList ( tsident_string, (DateTime)null,
	 					(DateTime)null, (String)null, false );
	 		}
	 		catch ( Exception e ) {
	 			message = "Error reading NWSRFS time series list.";
	 			Message.printWarning ( 2, routine, message );
	 			Message.printWarning ( 2, routine, e );
	 			JGUIUtil.setWaitCursor ( this.tstoolJFrame, false );
	 			throw new IOException ( message );
	 		}

	 		int size = 0;
	 		if ( tslist != null ) {
	 			size = tslist.size();

	 			// Now display in the table.

	 			JWorksheet_AbstractRowTableModel query_TableModel = new TSTool_TS_TableModel (	tslist, true );
	 			this.tstoolJFrame.ui_SetTimeSeriesCatalogTableModel ( query_TableModel );
	 			TSTool_TS_CellRenderer cr = new TSTool_TS_CellRenderer( (TSTool_TS_TableModel)query_TableModel);

	 			JWorksheet query_JWorksheet = tstoolJFrame.ui_GetTimeSeriesCatalogWorksheet();
	 			query_JWorksheet.setCellRenderer ( cr );
	 			query_JWorksheet.setModel ( query_TableModel );
	 			// Do not include the alias in the display.
	 			query_JWorksheet.removeColumn (((TSTool_TS_TableModel)query_TableModel).COL_ALIAS );
	 			query_JWorksheet.setColumnWidths (cr.getColumnWidths(), this.tstoolJFrame.getGraphics() );
	 		}
	 		if ( (tslist == null) || (size == 0) ) {
	 			Message.printStatus ( 1, routine, "No NWSRFS FS5Files time series read." );
	 			this.tstoolJFrame.queryResultsList_Clear ();
	 		}
	 		else {
	 			Message.printStatus ( 1, routine, "" + size + " NWSRFS FS5Files time series read." );
	 		}

	 		JGUIUtil.setWaitCursor ( this.tstoolJFrame, false );
	 		this.tstoolJFrame.ui_UpdateStatus ( false );
	 	}
	 	catch ( Exception e ) {
	 		JGUIUtil.setWaitCursor ( this.tstoolJFrame, false );
	 		message = "Error reading NWSRFS FS5Files time series list.";
	 		Message.printWarning ( 2, routine, message );
	 		Message.printWarning ( 2, routine, e );
	 		throw new IOException ( message );
	 	}
	}

	/**
	 * Initialize the input filters.
	 */
    public void initGUIInputFiltersLegacy ( JPanel queryInput_JPanel, List<InputFilter_JPanel> inputFilterJPanelList, Insets insets, int y )
    throws Exception {
    	this.inputFilterNWSRFSFS5Files_JPanel = new NWSRFS_TS_InputFilter_JPanel ();
    	JGUIUtil.addComponent(queryInput_JPanel, this.inputFilterNWSRFSFS5Files_JPanel,
    			0, y, 3, 1, 1.0, 0.0, insets, GridBagConstraints.HORIZONTAL,
    			GridBagConstraints.WEST );
    	this.inputFilterNWSRFSFS5Files_JPanel.setName ( "NWSRFSFS5Files.InputFilterPanel" );
    	inputFilterJPanelList.add ( this.inputFilterNWSRFSFS5Files_JPanel );
    }
    
    /**
     * Handle input name selection.
     */
    public void inputNameSelected () {
    	SimpleJComboBox dataType_JComboBox = this.tstoolJFrame.ui_GetDataTypeJComboBox();
   		// Reset the data types.
   		dataType_JComboBox.setEnabled ( true );
   		dataType_JComboBox.removeAll ();
   		// TODO smalers 2004-09-01 need to find a way to not re-read the data types file.
   		List<String> dataTypes = NWSRFS_Util.getTimeSeriesDataTypes ( getNwsrfsFS5FilesDMI(), true ); // Include description.
   		dataType_JComboBox.setData ( dataTypes );
   		dataType_JComboBox.select ( null );
   		dataType_JComboBox.select ( 0 );
    }

	/**
	Open a connection to NWSRFS FS5Files.
	@param props NWSRFS FS5Files Properties read from a TSTool configuration file.
	If null, the properties will be determined from the TSTool configuration file, if available.
	This will likely be the case at startup in systems where a connection to NWSRFS FS5Files will always be made.
	If not null, the connection is likely being defined from a TSTool configuration file.
	@param startup If true, indicates that the database connection is being made at startup.
	In this case, the NWSRFS_FS5Files input type is enabled but there
	may not actually be information in the TSTool.cfg file.
	This is the case, for example, when multiple NWSRFS FS5Files databases may be available and there is
	no reason to automatically connect to one of them for all users.
	*/
	public void openNwsrfsFS5Files ( PropList props, boolean startup, JMenuItem File_Properties_NWSRFSFS5Files_JMenuItem ) {
		String routine = getClass().getSimpleName() + ".openNwsrfsFS5Files";
		// First close the existing connection.
		if ( this.nwsrfs_dmi != null ) {
			try {
				this.nwsrfs_dmi.close();
			}
			catch ( Exception e ) {
				// Ignore - any reason not to?
			}
		}
		try {
			String	UseAppsDefaults = null,
					InputName = null;

			// Get the database connect information.

			if ( props == null ) {
				UseAppsDefaults = TSToolMain.getPropValue("NWSRFSFS5Files.UseAppsDefaults");
			}
			else {
				UseAppsDefaults = props.getValue( "NWSRFSFS5Files.UseAppsDefaults");
			}

			if ( props == null ) {
				InputName = TSToolMain.getPropValue("NWSRFSFS5Files.InputName");
			}
			else {
				InputName = props.getValue(	"NWSRFSFS5Files.InputName");
			}

			// If no configuration information is in the configuration file, do not open a connection here.

			if ( (UseAppsDefaults != null) && (UseAppsDefaults.equalsIgnoreCase("true") ||
					(UseAppsDefaults.equalsIgnoreCase("false") && (InputName != null))) ) {
				JGUIUtil.setWaitCursor ( this.tstoolJFrame, true );
				if ( UseAppsDefaults.equalsIgnoreCase("true") ) {
					Message.printStatus ( 1, routine,
							"Opening connection to NWSRFS FS5Files using Apps Defaults..." );
					this.nwsrfs_dmi = new NWSRFS_DMI ();
				}
				else {
					Message.printStatus ( 1, routine,
							"Opening connection to NWSRFS FS5Files using path \"" + InputName + "\"..." );
					this.nwsrfs_dmi = new NWSRFS_DMI ( InputName );
				}
				// Now open the connection.
				this.nwsrfs_dmi.open();
				// Add the FS5Files name to the list known to the interface.
				// This will allow the user to pick this in the GUI later without reopening.
				String path = this.nwsrfs_dmi.getFS5FilesLocation();
				this.input_name_NWSRFS_FS5Files.add ( path );
				// For now there is no way for the user to set a separate input name so set the same as the FS5 Files location.
				// Is automatically set.
				//__nwsrfs_dmi.setInputName ( path );
				// Set the NWSRFSDMI for the command processor.
				this.tstoolJFrame.commandProcessor_SetNWSRFSFS5FilesDMI ( this.nwsrfs_dmi );
				// Check the GUI state to enable/disable NWSRFS FS5Files properties.
				checkNwsrfsFS5FilesFeatures ( File_Properties_NWSRFSFS5Files_JMenuItem );
				JGUIUtil.setWaitCursor ( this.tstoolJFrame, false );
			}

		}
		catch ( Exception e ) {
			Message.printWarning ( 1, routine, "Unable to open NWSRFS FS5Files database.");
			Message.printWarning( 2, routine, e );
			// Set the DMI to null so that features will be turned on but // still allow the NWSRFS FS5Files
			// input type to be enabled so that it can be tried again.
			this.nwsrfs_dmi = null;
			JGUIUtil.setWaitCursor ( this.tstoolJFrame, false );
		}
	}
	
	/**
	 * Select the input filter panel for FS5Files selection.
	 */
	public InputFilter_JPanel setInputFilterForSelections () {
		if ( this.inputFilterNWSRFSFS5Files_JPanel != null ) {
			return this.inputFilterNWSRFSFS5Files_JPanel;
		}
		else {
			return null;
		}
	}

	/**
	Set up the query choices because an NWSRFS FS5files directory has been selected.
	Prompt for a NWSRFS FS5Files input name (FS5Files directory).  When selected, update the choices.
	@param reset_input_names If true, the input names will be repopulated with
	values from __input_name_NWSRFS_FS5Files.
	@exception Exception if there is an error.
	*/
	public void selectInputName ( boolean reset_input_names )
	throws Exception {
		String routine = getClass().getSimpleName() + ".selectInputName";

		SimpleJComboBox inputName_JComboBox = this.tstoolJFrame.ui_GetInputNameJComboBox();

		// TODO smalers 2004-09-10 it does not seem like the following case ever
		// is true - need to decide whether to take out.
		if ( reset_input_names ) {
			// The NWSRFS_FS5Files input type has been selected as a change from another type.
			// Repopulate the list if previous choices exist.
			//int size = __input_name_NWSRFS_FS5Files.size();
			// TODO - probably not needed.
			//__input_name_JComboBox.removeAll();
			// This does NOT include the leading browse, etc.
			inputName_JComboBox.setData ( this.input_name_NWSRFS_FS5Files );
		}
		// Check the item that is selected.
		String inputName = inputName_JComboBox.getSelected();
		if ( (inputName == null) || inputName.equals(TSToolConstants.BROWSE) ) {
			// Prompt for the name of an NWSRFS FS5Files directory.
			// Based on the file extension, set the data types and other information.
			JFileChooser fc = JFileChooserFactory.createJFileChooser ( JGUIUtil.getLastFileDialogDirectory() );
			fc.setDialogTitle("Select NWSRFS FS5Files Directory");
			fc.setFileSelectionMode (JFileChooser.DIRECTORIES_ONLY );
			if (fc.showOpenDialog(this.tstoolJFrame) != JFileChooser.APPROVE_OPTION) {
				// User canceled - set the file name back to the original and disable other choices.
				// Ignore programmatic events on the combo boxes.
				this.tstoolJFrame.ui_SetIgnoreItemEvent ( true );
				if ( inputName != null ) {
					inputName_JComboBox.select(null);
					inputName_JComboBox.select(inputName);
				}
				this.tstoolJFrame.ui_SetIgnoreItemEvent ( false );
				return;
			}
			// User has chosen a directory.

			inputName = fc.getSelectedFile().getPath();
			JGUIUtil.setLastFileDialogDirectory (fc.getSelectedFile().getParent() );

			// Set the input name.

			this.tstoolJFrame.ui_SetIgnoreItemEvent ( true );
			if ( !JGUIUtil.isSimpleJComboBoxItem (inputName_JComboBox, inputName, JGUIUtil.NONE, null, null ) ) {
				// Not already in so add after the browse string
				// (files are listed chronologically by select with most recent at the top).
				if ( IOUtil.isUNIXMachine() ) {
					// Unix/Linux includes "Please Select...",
					// "Use Apps Defaults", and "Browse..."...
					inputName_JComboBox.addAt ( inputName, 3 );
					inputName_JComboBox.select ( 3 );
				}
				else {
					// Windows includes only "Browse..."
					inputName_JComboBox.addAt ( inputName, 1 );
					inputName_JComboBox.select ( 1 );
				}
				// Insert so have the list of files later.
				this.input_name_NWSRFS_FS5Files.add ( 0, inputName );
			}
			// Select the file in the input name because leaving it on browse will disable the user's ability to reselect browse.
			inputName_JComboBox.select ( null );
			inputName_JComboBox.select ( inputName );
			// Now allow item events to occur as normal again.
			this.tstoolJFrame.ui_SetIgnoreItemEvent ( false );
		}

		inputName_JComboBox.setEnabled ( true );

		// Open the NWSRFS_DMI instance.

		JGUIUtil.setWaitCursor ( this.tstoolJFrame, true );
		try {
			if ( inputName.equals(TSToolConstants.USE_APPS_DEFAULTS) ) {
				Message.printStatus ( 1, routine, "Opening connection to NWSRFS FS5Files using Apps Defaults..." );
				this.nwsrfs_dmi = new NWSRFS_DMI();
			}
			else {
				Message.printStatus ( 1, routine, "Opening connection to NWSRFS FS5Files using path \"" +
						inputName + "\"..." );
				this.nwsrfs_dmi = new NWSRFS_DMI ( inputName );
			}
			this.nwsrfs_dmi.open ();
			Message.printStatus ( 1, routine, "Opened connection to NWSRFS FS5Files." );
		}
		catch ( Exception e ) {
			Message.printWarning ( 1, routine, "Error opening NWSRFS FS5Files (" + e + ")." );
			Message.printWarning ( 2, routine, e );
			this.nwsrfs_dmi = null;
			JGUIUtil.setWaitCursor ( this.tstoolJFrame, false );
			return;
			// TODO smalers 2004-09-08 need to remove from the input name list if an error?
		}

		// Set the data types and time step based on what is available.

		this.tstoolJFrame.uiAction_InputNameChoiceClicked ( null );

		// Initialize with blank data list.

   		JWorksheet_AbstractRowTableModel query_TableModel = new TSTool_TS_TableModel(null);
		this.tstoolJFrame.ui_SetTimeSeriesCatalogTableModel ( query_TableModel );
		TSTool_TS_CellRenderer cr = new TSTool_TS_CellRenderer( (TSTool_TS_TableModel)query_TableModel);

   		JWorksheet query_JWorksheet = tstoolJFrame.ui_GetTimeSeriesCatalogWorksheet();
		query_JWorksheet.setCellRenderer ( cr );
		query_JWorksheet.setModel ( query_TableModel );
		// Turn off columns in the table model that do not apply.
		query_JWorksheet.removeColumn (((TSTool_TS_TableModel)query_TableModel).COL_ALIAS );
		query_JWorksheet.removeColumn (((TSTool_TS_TableModel)query_TableModel).COL_SCENARIO);
		query_JWorksheet.setColumnWidths ( cr.getColumnWidths() );
		JGUIUtil.setWaitCursor ( this.tstoolJFrame, false );
	}

	/**
	Set up query choices because the NWSRFS FS5Files input type was selected.
	*/
	public void selectInputType ()
	throws Exception {
		String routine = getClass().getSimpleName() + ".selectInputType";
		// Update the input name and let the user choose Apps Defaults or pick a directory.
		this.tstoolJFrame.ui_SetIgnoreItemEvent ( true ); // Do this to prevent item event cascade.
		this.tstoolJFrame.ui_SetInputNameVisible(true); // Lists files.

		SimpleJComboBox dataType_JComboBox = this.tstoolJFrame.ui_GetDataTypeJComboBox();
		SimpleJComboBox inputName_JComboBox = this.tstoolJFrame.ui_GetInputNameJComboBox();
		SimpleJComboBox timeStep_JComboBox = this.tstoolJFrame.ui_GetTimeStepJComboBox();

		inputName_JComboBox.removeAll();
		if ( IOUtil.isUNIXMachine() ) {
			inputName_JComboBox.add ( TSToolConstants.PLEASE_SELECT );
			inputName_JComboBox.add ( TSToolConstants.USE_APPS_DEFAULTS );
			inputName_JComboBox.select ( TSToolConstants.PLEASE_SELECT );
		}
		inputName_JComboBox.add ( TSToolConstants.BROWSE );
		// If previous choices were selected, show them.
		int size = this.input_name_NWSRFS_FS5Files.size();
		for ( int i = 0; i < size; i++ ) {
			inputName_JComboBox.add ( this.input_name_NWSRFS_FS5Files.get(i) );
		}
		// Try to select the current DMI (it should always work if the logic is correct).

		boolean choice_ok = false;  // Needed to force data types to cascade correctly.
		if ( this.nwsrfs_dmi != null ) {
			String path = this.nwsrfs_dmi.getFS5FilesLocation();
			try {
				inputName_JComboBox.select ( path );
				choice_ok = true;
			}
			catch ( Exception e ) {
				Message.printWarning ( 2, routine, "Unable to select current NWSRFS FS5Files \"" + path + "\" in input names." );
			}
		}
		inputName_JComboBox.setEnabled ( true );

		// Enable when an input name is picked.

		dataType_JComboBox.setEnabled ( false );
		dataType_JComboBox.removeAll ();

		// Enable when a data type is picked.

		timeStep_JComboBox.setEnabled ( false );
		timeStep_JComboBox.removeAll ();

		this.tstoolJFrame.ui_SetIgnoreItemEvent ( false );  // Item events OK again.

		if ( (inputName_JComboBox.getItemCount() == 1) && (inputName_JComboBox.getSelected().equals(TSToolConstants.BROWSE))){
			// Only Browse is in the list so if the user picks,
			// the component does not generate an event because the choice has not changed.
			try {
				selectInputName ( false );
			}
			catch ( Exception e ) {
				Message.printWarning ( 1, routine, "Error opening NWSRFS FS5Files connection." );
				Message.printWarning ( 2, routine, e );
			}
		}
		else if ( choice_ok ) {
			// The current open DMI was able to be selected above. Need to force the data types to cascade.
			this.tstoolJFrame.uiAction_InputNameChoiceClicked( null );
		}

		// Display the NWSRFS input filters.
		this.tstoolJFrame.ui_SetInputFilterForSelections();

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
