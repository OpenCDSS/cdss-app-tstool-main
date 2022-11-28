// TSTool_HecDss - integration between TSTool UI and US Army Corps HEC-DSS files

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
package cdss.app.tstool.datastore.usace.hecdss;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;

import DWR.DMI.tstool.TSToolConstants;
import DWR.DMI.tstool.TSTool_JFrame;
import RTi.TS.TS;
import RTi.Util.GUI.InputFilter_JPanel;
import RTi.Util.GUI.JFileChooserFactory;
import RTi.Util.GUI.JGUIUtil;
import RTi.Util.GUI.JWorksheet;
import RTi.Util.GUI.JWorksheet_AbstractRowTableModel;
import RTi.Util.GUI.SimpleFileFilter;
import RTi.Util.GUI.SimpleJComboBox;
import RTi.Util.Message.Message;
import RTi.Util.String.StringUtil;
import rti.tscommandprocessor.commands.hecdss.HecDssAPI;
import rti.tscommandprocessor.commands.hecdss.HecDssTSInputFilter_JPanel;
import rti.tscommandprocessor.commands.hecdss.ui.TSTool_HecDss_CellRenderer;
import rti.tscommandprocessor.commands.hecdss.ui.TSTool_HecDss_TableModel;

/**
 * This class provides integration between TSTool and the US Army Corps of Engineers HEC-DSS files.
 * This file datastore is currently built into TSTool, rather than being a plugin.
 * Isolating the code simplifies maintenance.
 * The class is a singleton.  Use getInstance() to get the instance to call methods.
 */
public class TSTool_HecDss {

	/**
	 * Singleton instance of this class.
	 * Use getInstance() to return the singleton.
	 */
	private static TSTool_HecDss instance = null;
	
	/**
	 * Instance of the TSTool main UI.
	 */
	private TSTool_JFrame tstoolJFrame = null;
	
	/**
	InputFilter_JPanel for HEC-DSS time series.
	*/
	private InputFilter_JPanel __inputFilterHecDss_JPanel = null;

	/**
	The HEC-DSS files that have been selected during the session,
	to allow switching between input types but not losing the list of files.  These are the full file strings.
	*/
	private List<String> __inputNameHecDssList = new ArrayList<>();

	/**
	The HEC-DSS files that have been selected during the session,
	to allow switching between input types but not losing the list of files.
	These are the abbreviated file strings, so as to not take up too much space in the interface.
	*/
	private List<String> __inputNameHecDssVisibleList = new ArrayList<>();

	/**
	The last HEC-DSS file that was selected, to reset after canceling a browse.
	*/
	private String __inputNameHecDssVisibleLast = null;

	/**
	 * Return the HEC-DLL input filter JPanel.
	 */
    public InputFilter_JPanel getInputFilterJPanel () {
	    return this.__inputFilterHecDss_JPanel;
    }

	/**
	 * Private constructor for lazy initialization.
	 */
	private TSTool_HecDss ( TSTool_JFrame tstoolJFrame ) {
		this.tstoolJFrame = tstoolJFrame;
	}

	/**
	 * Get the singleton instance.
	 */
	public static TSTool_HecDss getInstance ( TSTool_JFrame tstoolJFrame ) {
		if ( TSTool_HecDss.instance == null ) {
			TSTool_HecDss.instance = new TSTool_HecDss( tstoolJFrame );
		}
		return TSTool_HecDss.instance;
	}

	/**
	Read the list of time series from a HEC-DSS database file and list in the GUI.
	The filename is taken from the selected item in the __inputName_JComboBox.
	 */
	public void getTimeSeriesListClicked_ReadHecDssCatalog ()
	throws IOException {
		String routine = getClass().getSimpleName() + ".getTimeSeriesListClicked_ReadHecDssCatalog";

		try {
			// Visible path might be abbreviated so look up in list that has full paths.
			String inputNameSelectedVisible = this.tstoolJFrame.ui_GetSelectedInputName();
			int listIndex = StringUtil.indexOf(__inputNameHecDssVisibleList,inputNameSelectedVisible);
			Message.printStatus( 2, routine, "Visible item is in position " + listIndex );
			String inputNameSelected = (String)__inputNameHecDssList.get(listIndex);
			Message.printStatus( 2, routine, "File corresponding to visible item is \"" + inputNameSelected + "\"" );
			//String id_input = "*";
			// Get the ID from the input filter.
			String aPartReq = "*";
			String bPartReq = "*";
			String cPartReq = "*";
			// D part is not selectable in the main GUI but can use SetInputPeriod() command to limit read for time series.
			String ePartReq = "*";
			String fPartReq = "*";
			// Try to get filter choices for all the parts.
			List<String> inputList = __inputFilterHecDss_JPanel.getInput ( HecDssTSInputFilter_JPanel.A_PART_LABEL, null, true, null );
			if ( inputList.size() > 0 ) {
				// Use the first matching filter.
				aPartReq = (String)inputList.get(0);
			}
			inputList = __inputFilterHecDss_JPanel.getInput ( HecDssTSInputFilter_JPanel.B_PART_LABEL, null, true, null );
			if ( inputList.size() > 0 ) {
				// Use the first matching filter.
				bPartReq = (String)inputList.get(0);
			}
			inputList = __inputFilterHecDss_JPanel.getInput ( HecDssTSInputFilter_JPanel.C_PART_LABEL, null, true, null );
			if ( inputList.size() > 0 ) {
				// Use the first matching filter.
				cPartReq = (String)inputList.get(0);
			}
			inputList = __inputFilterHecDss_JPanel.getInput ( HecDssTSInputFilter_JPanel.E_PART_LABEL, null, true, null );
			if ( inputList.size() > 0 ) {
				// Use the first matching filter.
				ePartReq = (String)inputList.get(0);
			}
			inputList = __inputFilterHecDss_JPanel.getInput ( HecDssTSInputFilter_JPanel.F_PART_LABEL, null, true, null );
			if ( inputList.size() > 0 ) {
				// Use the first matching filter.
				fPartReq = (String)inputList.get(0);
			}
			String cPartReq2 = "";
			// TODO smalers 2022-10-21 the following is needed to avoid ** but may be able to clean up better.
			if ( cPartReq.equals("*") ) {
				cPartReq2 = "*";
			}
			else {
				cPartReq2 = "*" + cPartReq;
			}
			// Use the : to separate the parts.
			String tsidPattern = aPartReq + ":" + bPartReq + ".HEC-DSS." + cPartReq2 + "." + ePartReq + "." + fPartReq +
					"~HEC-DSS~" + inputNameSelected;
			Message.printStatus ( 1, routine, "Reading HEC-DSS file \"" + inputNameSelected + "\" for TSID pattern \"" + tsidPattern + "\"" );
			List<TS> tslist = null;
			JGUIUtil.setWaitCursor ( this.tstoolJFrame, true );
			// TODO smalers 2008-09-03 Enable searchable fields.
			boolean readData = false;
			boolean closeAfterRead = false;
			tslist = HecDssAPI.readTimeSeriesList ( new File(inputNameSelected), tsidPattern, null, null, null,
					readData, closeAfterRead );
			int size = 0;
			if ( tslist != null ) {
				size = tslist.size();
				JWorksheet_AbstractRowTableModel query_TableModel = new TSTool_HecDss_TableModel ( tslist );
				this.tstoolJFrame.ui_SetTimeSeriesCatalogTableModel ( query_TableModel );
				TSTool_HecDss_CellRenderer cr = new TSTool_HecDss_CellRenderer( (TSTool_HecDss_TableModel)query_TableModel);

				JWorksheet query_JWorksheet = tstoolJFrame.ui_GetTimeSeriesCatalogWorksheet();
				query_JWorksheet.setCellRenderer ( cr );
				query_JWorksheet.setModel ( query_TableModel );
				// Turn off columns in the table model that do not apply.
				query_JWorksheet.removeColumn (((TSTool_HecDss_TableModel)query_TableModel).COL_SEQUENCE);
				query_JWorksheet.setColumnWidths ( cr.getColumnWidths(), this.tstoolJFrame.getGraphics() );
			}
			if ( (tslist == null) || (size == 0) ) {
				Message.printStatus ( 1, routine, "No HEC-DSS time series were read." );
				this.tstoolJFrame.queryResultsList_Clear ();
			}
			else {
				Message.printStatus ( 1, routine, "" + size + " HEC-DSS time series were read." );
			}

			this.tstoolJFrame.ui_UpdateStatus ( false );
			tslist = null;
			JGUIUtil.setWaitCursor ( this.tstoolJFrame, false );
		}
		catch ( Exception e ) {
			String message = "Error reading HEC-DSS file.";
			Message.printWarning ( 2, routine, message );
			Message.printWarning ( 2, routine, e );
			JGUIUtil.setWaitCursor ( this.tstoolJFrame, false );
			throw new IOException ( message );
		}
	}

	/**
	 * Initialize the GUI input filter for HEC-DSS.
	 * @param insets borders for around component
	 * @param y layout position
	 */
    public void initGUIInputFilters ( Insets insets, int y) {
    	String routine = getClass().getSimpleName() + ".initGUIInputFilters";
        try {
            // Open with 5 lists to show all HEC-DSS parts except D.
            this.__inputFilterHecDss_JPanel = new HecDssTSInputFilter_JPanel ( 5 );
            this.__inputFilterHecDss_JPanel.setName ( "HEC-DSS" );
            // For debugging.
            //__inputFilterHecDss_JPanel.setBackground(Color.red);
            JGUIUtil.addComponent(this.tstoolJFrame.ui_GetQueryInputJPanel(), this.__inputFilterHecDss_JPanel,
                0, y, 3, 1, 1.0, 0.0, insets, GridBagConstraints.HORIZONTAL,
                GridBagConstraints.WEST );
            this.tstoolJFrame.ui_GetInputFilterJPanelList().add ( this.__inputFilterHecDss_JPanel );
        }
        catch ( Throwable e ) {
            Message.printWarning ( 3, routine, "Unable to initialize input filter for HEC-DSS file.");
            Message.printWarning ( 3, routine, e );
        }
    }

    /**
     * Handle input name selection.
     */
    public void inputNameSelected ( String selectedInputNameVisible ) {
    	String routine = getClass().getSimpleName() + ".inputNameSelected";
        // Get the position of the visible name.
        int listIndex = StringUtil.indexOf(__inputNameHecDssVisibleList,selectedInputNameVisible);
        Message.printStatus( 2, routine, "Visible item is in position " + listIndex );
        String selectedInputName = __inputNameHecDssList.get(listIndex);
        Message.printStatus( 2, routine, "File corresponding to visible item is \"" + selectedInputName + "\"" );
        // A new file was selected so clear the part choices and regenerate the lists.
        Message.printStatus( 2, routine, "  Setting the input filter to use this file." );
        Message.printStatus( 2, routine, "    Visible name:            " + selectedInputNameVisible );
        Message.printStatus( 2, routine, "    Invisible (actual) name: " + selectedInputName);
        ((HecDssTSInputFilter_JPanel)__inputFilterHecDss_JPanel).setHecDssFile(new File(selectedInputName));
        Message.printStatus( 2, routine, "    Refreshing the input filter choices for the selected file." );
        ((HecDssTSInputFilter_JPanel)__inputFilterHecDss_JPanel).refreshChoices();
        Message.printStatus( 2, routine, "    Back from refreshing the input filter choices for the selected file." );
    }

    /**
	Set up query choices because a HECDSS file name has been selected.
	Prompt for a HECDSS input name (binary file name).  When selected, update the choices.
	@param resetInputNames If true, the input names will be repopulated with values from __inputNameHecDssListVisible.
	@exception Exception if there is an error.
	@return false if cancel, true if a file is selected
     */
    public boolean selectInputName ( boolean resetInputNames )
    		throws Exception {
    	String routine = getClass().getSimpleName() + ".selectInputName";
    	if ( resetInputNames ) {
    		// The  input type has been selected as a change from another type.
    		// Repopulate the list if previous choices exist:
    		// - will either select a different file (not in the list) below
    		//   or cancel, in which case the initial selection is reset
    		this.tstoolJFrame.ui_GetInputNameJComboBox().setData ( __inputNameHecDssVisibleList );
    	}
    	// Check the item that is selected.
    	SimpleJComboBox inputName_JComboBox = this.tstoolJFrame.ui_GetInputNameJComboBox();
    	String inputName = inputName_JComboBox.getSelected();
    	String inputNameVisible = null;
    	if ( (inputName == null) || inputName.equals(TSToolConstants.BROWSE) ) {
    		// First HEC-DSS file is being selected (nothing in the choices) or have selected "Browse":
    		// - prompt for the name of a HEC-DSS file.
    		Message.printStatus ( 2, routine, "Prompting for HEC-DSS input file..." );

    		// Based on the file extension, set the data types and other information.
    		JFileChooser fc = JFileChooserFactory.createJFileChooser ( JGUIUtil.getLastFileDialogDirectory() );
    		// Request focus because otherwise focus may still be on another component.
    		fc.requestFocusInWindow();
    		fc.setDialogTitle("Select a HEC-DSS Database File");
    		SimpleFileFilter sff = new SimpleFileFilter("dss","HEC-DSS Database File");
    		fc.addChoosableFileFilter( sff );
    		// Only allow recognized extensions.
    		fc.setAcceptAllFileFilterUsed ( false );
    		if (fc.showOpenDialog(this.tstoolJFrame) != JFileChooser.APPROVE_OPTION) {
    			// User canceled - set the file name back to the original and disable other choices.
    			if ( inputName != null ) {
    				this.tstoolJFrame.ui_SetIgnoreItemEvent ( true );
    				inputName_JComboBox.select(null);
    				if ( __inputNameHecDssVisibleLast != null ) {
    					// Reselect the previous selection without regenerating a select event.
    					inputName_JComboBox.select ( __inputNameHecDssVisibleLast );
    				}
    				this.tstoolJFrame.ui_SetIgnoreItemEvent ( false );
    			}
    			// Cancelled so don't process the file.
    			return false;
    		}
    		// User has chosen a file.

    		// Get the name of the actual file selection.
    		inputName = fc.getSelectedFile().getPath();
    		Message.printStatus ( 2, routine, "Input file from file selector: " + inputName );
    		// Save as last selection, using the shorter visible path.
    		//__inputNameHecDssLast = inputName; // Not needed?
    		// Create a filename that is visible but may contain ... when the filename is long,
    		// otherwise, very long filenames cause layout issues.
    		inputNameVisible = this.tstoolJFrame.ui_CreateAbbreviatedVisibleFilename(inputName);
    		__inputNameHecDssVisibleLast = inputNameVisible;
    		JGUIUtil.setLastFileDialogDirectory (fc.getSelectedFile().getParent() );

    		// Set the input name.

    		this.tstoolJFrame.ui_SetIgnoreItemEvent ( true );
    		if ( !JGUIUtil.isSimpleJComboBoxItem (inputName_JComboBox, TSToolConstants.BROWSE, JGUIUtil.NONE, null, null ) ) {
    			// The "Browse" choice is not already in the visible list so add it at the beginning
    			// for subsequent selections.
    			// Also add to the invisible file list to keep the indices the same.
    			this.__inputNameHecDssVisibleList.add ( TSToolConstants.BROWSE );
    			this.__inputNameHecDssList.add ( TSToolConstants.BROWSE );
    			inputName_JComboBox.add ( TSToolConstants.BROWSE );
    		}
    		if ( !JGUIUtil.isSimpleJComboBoxItem (inputName_JComboBox,inputNameVisible, JGUIUtil.NONE, null, null ) ) {
    			// The selected file is not already in the list of input names so add after the browse string
    			// (files are listed chronologically by select with most recent at the top).
    			if ( inputName_JComboBox.getItemCount() > 1 ) {
    				inputName_JComboBox.addAt ( inputNameVisible, 1 );
    				this.__inputNameHecDssVisibleList.add ( 1, inputNameVisible );
    				this.__inputNameHecDssList.add ( 1, inputName );
    			}
    			else {
    				inputName_JComboBox.add ( inputNameVisible );
    				this.__inputNameHecDssVisibleList.add(inputNameVisible);
    				this.__inputNameHecDssList.add(inputName);
    			}
    		}
    		this.tstoolJFrame.ui_SetIgnoreItemEvent ( false );

    		// Select the file in the input name:
    		// - otherwise, leaving it on browse will disable the user's ability to reselect browse.
    		// - disable events so that logic can continue below
    		Message.printStatus ( 2, routine, "Selecting the visible input file without generating an event: " );
    		Message.printStatus ( 2, routine, "  " + inputName );
    		this.tstoolJFrame.ui_SetIgnoreItemEvent ( true );
    		inputName_JComboBox.select ( null );
    		inputName_JComboBox.select ( inputNameVisible );
    		this.tstoolJFrame.ui_SetIgnoreItemEvent ( false );
    	}
    	else {
    		// Picking an existing file:
    		// - need to look up the invisible (actual) file name from the visible file name.
    		inputNameVisible = inputName;
    		// Figure out the position in the list.
    		int foundChoice = -1;
    		for ( int i = 0; i < this.__inputNameHecDssVisibleList.size(); i++ ) {
    			String choice = this.__inputNameHecDssVisibleList.get(i);
    			if ( choice.equals(inputNameVisible) ) {
    				foundChoice = i;
    				break;
    			}
    		}
    		if ( foundChoice < 0 ) {
    			// Should not happen.
    			return false;
    		}
    		inputName = this.__inputNameHecDssList.get(foundChoice);
    		__inputNameHecDssVisibleLast = inputNameVisible;
    	}

    	// Make sure the input name is enabled in case it was deselected elsewhere.
    	inputName_JComboBox.setEnabled ( true );

    	// Set the data types and time step to tell the user to use filters.

    	List<String> dataTypes = new ArrayList<>();
    	dataTypes.add(TSToolConstants.DATA_TYPE_USE_FILTERS);

    	// Fill data types choice (set to an empty list since data types are in the input filters).
    	SimpleJComboBox dataType_JComboBox = this.tstoolJFrame.ui_GetDataTypeJComboBox();
    	this.tstoolJFrame.ui_SetIgnoreItemEvent ( true );
    	dataType_JComboBox.setEnabled ( false );
    	dataType_JComboBox.removeAll ();
    	Message.printStatus ( 2, routine, "Setting HEC-DSS data types without generating event..." );
    	dataType_JComboBox.setData ( dataTypes );
    	dataType_JComboBox.select ( null );
    	//if ( dataTypes.size() > 0 ) {
    	dataType_JComboBox.select ( 0 );
    	//}
    	this.tstoolJFrame.ui_SetIgnoreItemEvent ( false );

    	// Set time step appropriately.

    	SimpleJComboBox timeStep_JComboBox = this.tstoolJFrame.ui_GetTimeStepJComboBox();
    	this.tstoolJFrame.ui_SetIgnoreItemEvent ( true );
    	timeStep_JComboBox.removeAll ();
    	timeStep_JComboBox.add(TSToolConstants.DATA_TYPE_USE_FILTERS);
    	//int intervalBase = TimeInterval.MONTH; // Default
    	//if ( intervalBase == TimeInterval.MONTH ) {
    	//    __timeStep_JComboBox.add ( __TIMESTEP_MONTH );
    	//}
    	//else if ( intervalBase == TimeInterval.DAY ) {
    	//    __timeStep_JComboBox.add ( __TIMESTEP_DAY );
    	//}
    	//__timeStep_JComboBox.setEnabled ( true ); // Enabled, but one visible.
    	timeStep_JComboBox.select ( null );
    	timeStep_JComboBox.select ( 0 );
    	timeStep_JComboBox.setEnabled ( false ); // Disabled and empty.
    	this.tstoolJFrame.ui_SetIgnoreItemEvent ( false );

    	// Show the input filters that are appropriate for data time and timestep choices:
    	// - must be called because normally it is triggered from other events
    	// - for HEC-DSS it is the input type that controls
    	this.tstoolJFrame.ui_SetInputFilterForSelections();

    	// Refresh other choices.  Input filter choices.
    	// A new file was selected so clear the part choices and regenerate the lists.
    	Message.printStatus( 2, routine, "  Setting the input filter to use this file:" );
    	Message.printStatus( 2, routine, "    Visible name:            " + inputNameVisible );
    	Message.printStatus( 2, routine, "    Invisible (actual) name: " + inputName);
    	((HecDssTSInputFilter_JPanel)__inputFilterHecDss_JPanel).setHecDssFile(new File(inputName));
    	Message.printStatus( 2, routine, "    Refreshing the input filter choices for the selected file." );
    	((HecDssTSInputFilter_JPanel)__inputFilterHecDss_JPanel).refreshChoices();
    	Message.printStatus( 2, routine, "    Back from refreshing the input filter choices for the selected file." );

    	// Initialize the time series with a blank data list.

		JWorksheet_AbstractRowTableModel query_TableModel = new TSTool_HecDss_TableModel(null);
		this.tstoolJFrame.ui_SetTimeSeriesCatalogTableModel ( query_TableModel );
    	TSTool_HecDss_CellRenderer cr = new TSTool_HecDss_CellRenderer((TSTool_HecDss_TableModel)query_TableModel);

   		JWorksheet query_JWorksheet = tstoolJFrame.ui_GetTimeSeriesCatalogWorksheet();
    	query_JWorksheet.setCellRenderer ( cr );
    	query_JWorksheet.setModel ( query_TableModel );
    	// Turn off columns in the table model that do not apply.
    	query_JWorksheet.removeColumn (((TSTool_HecDss_TableModel)query_TableModel).COL_SEQUENCE);
    	query_JWorksheet.setColumnWidths ( cr.getColumnWidths() );

    	// File was selected.
    	return true;
    }

    /**
	Set up the query choices because the HEC DSS input type has been selected.
    */
    public void selectInputType ()
    throws Exception {
        this.tstoolJFrame.ui_SetInputNameVisible(true); // Lists HEC-DSS (.dss) files using a shortened name if necessary.
        // Prompt for a HEC-DSS:
        boolean fileSelected = selectInputName ( true );

        if ( fileSelected ) {
        	// TODO smalers 2022-10-21 these are set in the uiAction_SelectInputName_HECDSS() method.
        	// Empty and disable the data type and time step because an input filter with choices from the file is used.
        	//__dataType_JComboBox.setEnabled ( false );
        	//__dataType_JComboBox.removeAll ();
        	//__timeStep_JComboBox.setEnabled ( false );
        	//__timeStep_JComboBox.removeAll ();

        	// Initialize the time series list with blank data list.
        	JWorksheet_AbstractRowTableModel query_TableModel = new TSTool_HecDss_TableModel ( null, false );
        	this.tstoolJFrame.ui_SetTimeSeriesCatalogTableModel ( query_TableModel );
        	TSTool_HecDss_CellRenderer cr = new TSTool_HecDss_CellRenderer( (TSTool_HecDss_TableModel)query_TableModel);

        	JWorksheet query_JWorksheet = tstoolJFrame.ui_GetTimeSeriesCatalogWorksheet();
        	query_JWorksheet.setCellRenderer ( cr );
        	query_JWorksheet.setModel ( query_TableModel );
        	query_JWorksheet.setColumnWidths ( cr.getColumnWidths() );
        }
    }

	/**
	 * Transfer one time series catalog row to a command.
	 * @param row the time series catalog row to transfer (0+)
	 * @param useAlias whether to use an alias
	 */
    public int transferOneTimeSeriesCatalogRowToCommands ( int row, boolean useAlias, int insertOffset ) {
	    // TODO smalers 2009-01-13 Evaluate whether to remove some columns.
	    // Currently essentially the same as the generic model but have custom headings.
        // The location (id), type, and time step uniquely identify the time series.
		JWorksheet_AbstractRowTableModel query_TableModel = this.tstoolJFrame.ui_GetTimeSeriesCatalogTableModel();
        TSTool_HecDss_TableModel model = (TSTool_HecDss_TableModel)query_TableModel;
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