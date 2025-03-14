// TSToolPluginManager_JFrame - frame for displaying a table with TSTool plugins

/* NoticeStart

TSTool
TSTool is a part of Colorado's Decision Support Systems (CDSS)
Copyright (C) 1994-2025 Colorado Department of Natural Resources

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

package DWR.DMI.tstool;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import RTi.Util.GUI.JGUIUtil;
import RTi.Util.GUI.ResponseJDialog;
import RTi.Util.GUI.SimpleJButton;
import RTi.Util.Message.Message;

/**
This class is the frame in which the panel displaying TSToolPlugin data in a worksheet is displayed.
*/
@SuppressWarnings("serial")
public class TSToolPluginManager_JFrame extends JFrame implements ActionListener
{

	/**
	The TSToolPluginManager that is being displayed.
	*/
	private TSToolPluginManager pluginManager = null;

	/**
	The panel containing the worksheet that will be displayed in the frame.
	*/
	private TSToolPluginManager_JPanel pluginManagerPanel = null;

	/**
 	* Parent JFrame, used to center the table window.
 	*/
	private JFrame parent = null;

	/**
	 * Button for "Add Version Folder".
	 */
	private SimpleJButton addVersionFolder_JButton = null;

	/**
	 * Button for "Close".
	 */
	private SimpleJButton close_JButton = null;

	/**
	 * Button for "Delete".
	 */
	private SimpleJButton delete_JButton = null;

	/**
	Message text fields.
	*/
	private JTextField messageJTextField = null;

	/**
 	* Status text field.
 	*/
	private JTextField statusJTextField = null;

	/**
	Constructor.
	@param title the title to put on the frame.
	@param pluginManager the pluginManager to display
	@throws Exception if table is null.
	*/
	public TSToolPluginManager_JFrame ( String title, TSToolPluginManager pluginManager )
	throws Exception {
		// Call the overloaded method with no parent.
		this ( null, title, pluginManager );
	}

	/**
	Constructor.
	@param parent parent JFrame, used to center the window on the parent.
	@param title the title to put on the frame.
	@param pluginManager the TSToolPluginManager to display
	@throws Exception if table is null.
	*/
	public TSToolPluginManager_JFrame ( JFrame parent, String title, TSToolPluginManager pluginManager )
	throws Exception {
		JGUIUtil.setIcon ( this, JGUIUtil.getIconImage() );
		if ( title == null ) {
			if ( (JGUIUtil.getAppNameForWindows() == null) || JGUIUtil.getAppNameForWindows().equals("") ) {
				setTitle ( "TSTool Install Manager" );
			}
			else {
            	setTitle( JGUIUtil.getAppNameForWindows() +	" - Installation Manager" );
			}
		}
		else {
        	if ( (JGUIUtil.getAppNameForWindows() == null) || JGUIUtil.getAppNameForWindows().equals("") ) {
				setTitle ( title );
			}
			else {
            	setTitle( JGUIUtil.getAppNameForWindows() +	" - " + title );
			}
		}
		this.parent = parent;
		this.pluginManager = pluginManager;
		
		// Listen for worksheet selections:
		// - TODO smalers 2025-03-11 need to implement something so buttons can be enabled only for selected rows
		//this.pluginManagerPanel.addSelectionListener ( this );

		setupUI();
	}

	/**
	 * Add version folder for the selected plugins.
	 */
	private void addVersionFolder () {
		int[] selectedRows = this.pluginManagerPanel.getWorksheetSelectedRows();
		if ( selectedRows.length == 0 ) {
			// Should not happen.
			new ResponseJDialog ( this, "Add Version Folder",
				"Select one plugin to add a version folder.",
				ResponseJDialog.OK).response();
			return;
		}
		else if ( selectedRows.length > 1 ) {
			new ResponseJDialog ( this, "Add Version Folder",
				"Only one plugin can be updated at a time.\n"
				+ "Make sure that only one plugin row is selected in the table.",
				ResponseJDialog.OK).response();
			return;
		}
		else {
			// Have a single plugin selected.
			StringBuilder b = new StringBuilder();
			TSToolPlugin plugin = this.pluginManagerPanel.getPlugin(selectedRows[0]);
			String pluginInfo = "  " + plugin.getName() + " " + plugin.getVersion() + "\n";
			Boolean isBest = plugin.getIsBestCompatibleWithTSTool();
			if ( (isBest != null) && isBest ) {
				// The plugin is the best compatible with TSTool so it will have been loaded into memory.
				new ResponseJDialog ( this, "Confirm Add Version Folder",
					"The selected plugin's files cannot be moved because it is currently being used by TSTool:\n" +
					pluginInfo +
					"Leave it as is, wait until a newer version is installed, or manually delete the plugin installation folder when TSTool is not running." + b.toString(),
					ResponseJDialog.OK).response();
			}
			else {
				// OK to move the plugin.
				int x = new ResponseJDialog ( this, "Confirm Add Version Folder",
					"Are you sure that you want to add a version folder for the selected plugin?\n"
					+ pluginInfo,
					ResponseJDialog.OK|ResponseJDialog.CANCEL).response();
				if ( x == ResponseJDialog.OK ) {
					// Add the version folder for the plugin.
					List<String> problems = new ArrayList<>();
					boolean added = this.pluginManager.addPluginVersionFolder(plugin, problems);
					if ( !added ) {
						StringBuilder b2 = new StringBuilder("Error adding the version folder:\n");
						for ( String problem : problems ) {
							b2.append( "  " + problem + "\n");
						}
						b2.append("Check the files on the computer");
						new ResponseJDialog ( this, "Add Version Folder Failed",
							b2.toString(),
							ResponseJDialog.OK).response();
					}
				}
			}
		}
	}
	
	/**
	 * Check the UI state.
	 * TODO smalers 2025-03-11 need to enable this if can figure out how to detect JWorksheet selections.
	 */
	private void checkUiState () {
		int[] selectedRows = this.pluginManagerPanel.getWorksheetSelectedRows();
		/*
		if ( selectedRows.length > 0 ) {
			// Have at least one selected row.
			this.addVersionFolder_JButton.setEnabled(true);
			this.delete_JButton.setEnabled(true);
		}
		else {
			this.addVersionFolder_JButton.setEnabled(false);
			this.delete_JButton.setEnabled(false);
		}
		*/
	}

	/**
	 * Close the UI.
	 */
	private void close () {
		setVisible ( false );
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		dispose();
	}

	/**
	 * Delete the selected plugins.
	 */
	private void delete () {
		int[] selectedRows = this.pluginManagerPanel.getWorksheetSelectedRows();
		if ( selectedRows.length == 0 ) {
			// Should not happen.
			new ResponseJDialog ( this, "Delete plugin",
				"Select one plugin to delete.",
				ResponseJDialog.OK).response();
			return;
		}
		else if ( selectedRows.length > 1 ) {
			// Should not happen.
			new ResponseJDialog ( this, "Delete plugin",
				"Only a single plugin can be deleted at a time.\n"
				+ "Make sure that only one plugin row is selected in the table.",
				ResponseJDialog.OK).response();
			return;
		}
		else {
			StringBuilder b = new StringBuilder();
			TSToolPlugin plugin = this.pluginManagerPanel.getPlugin(selectedRows[0]);
			String pluginInfo = "  " + plugin.getName() + " " + plugin.getVersion() + "\n";
			Boolean isBest = plugin.getIsBestCompatibleWithTSTool();
			if ( (isBest != null) && isBest ) {
				// The plugin is the best compatible with TSTool so it will have been loaded into memory.
				new ResponseJDialog ( this, "Confirm Plugin Delete",
					"The selected plugin cannot be deleted because it is currently being used by TSTool:\n" +
					pluginInfo +
					"Leave it as is or manually delete the plugin installation folder when TSTool is not running." + b.toString(),
					ResponseJDialog.OK).response();
			}
			else {
				int x = new ResponseJDialog ( this, "Confirm Plugin Delete",
					"Are you sure that you want to deleted the selected plugin (it can be reinstalled later if needed)?\n" + b.toString(),
					ResponseJDialog.OK|ResponseJDialog.CANCEL).response();
				if ( x == ResponseJDialog.OK ) {
					// Delete the selected plugin.
					List<String> problems = new ArrayList<>();
					boolean deleted = this.pluginManager.deletePlugin(plugin, problems);
					if ( !deleted ) {
						StringBuilder b2 = new StringBuilder("Error deleting the plugin:\n");
						for ( String problem : problems ) {
							b2.append( "  " + problem + "\n");
						}
						b2.append("Check the files on the computer");
						new ResponseJDialog ( this, "Delete Plugin Failed",
							b2.toString(),
							ResponseJDialog.OK).response();
					}
				}
			}
		}
	}

	/**
	Responds to ActionEvents.
	@param event ActionEvent object
	*/
	public void actionPerformed ( ActionEvent event ) {
		Object o = event.getSource();
	
	    if ( o == addVersionFolder_JButton ) {
			addVersionFolder ();
		}
		else if ( o == delete_JButton ) {
			delete ();
		}
		else if ( o == close_JButton ) {
			close ();
		}
	}

	/**
	Sets the status bar's message and status text fields.
	@param message the value to put into the message text field.
	@param status the value to put into the status text field.
	*/
	public void setMessageStatus ( String message, String status ) {
		if ( message != null ) {
			this.messageJTextField.setText(message);
		}
		if ( status != null ) {
			this.statusJTextField.setText(status);
		}
	}

	/**
	Sets up the UI.
	*/
	private void setupUI()
	throws Exception {
		// Add the table and buttons to the center of the UI.
		JPanel main_JPanel = new JPanel();
		main_JPanel.setLayout(new GridBagLayout());
		this.pluginManagerPanel = new TSToolPluginManager_JPanel(this, this.pluginManager);
		
		int y = -1;
		JGUIUtil.addComponent(main_JPanel, this.pluginManagerPanel,
			0, ++y, 1, 1, 1, 1,
			GridBagConstraints.BOTH, GridBagConstraints.CENTER);
		
		// 
		getContentPane().add("Center", main_JPanel);

		// Add a button panel.
		Insets insetsTLBR = new Insets ( 1, 3, 1, 3 ); // Space around component.
		JPanel button_JPanel = new JPanel();
		button_JPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
       	JGUIUtil.addComponent(main_JPanel, button_JPanel,
			0, ++y, 1, 1, 1, 0, insetsTLBR, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER);

		this.addVersionFolder_JButton = new SimpleJButton("Add Version Folder", this);
		this.addVersionFolder_JButton.setToolTipText("Add version folder(s) for the selected plugin(s)");
		button_JPanel.add (this.addVersionFolder_JButton);

		this.delete_JButton = new SimpleJButton("Delete", this);
		button_JPanel.add (this.delete_JButton);
		this.delete_JButton.setToolTipText("Delete the selected plugins (will be confirmed)");

		this.close_JButton = new SimpleJButton("Close", this);
		this.close_JButton.setToolTipText("Close this window");
		button_JPanel.add (this.close_JButton);

		// Add the status bar to the bottom of the UI.
		JPanel statusBarPanel = new JPanel();
		statusBarPanel.setLayout(new GridBagLayout());

		this.messageJTextField = new JTextField(20);
		this.messageJTextField.setEditable(false);
		this.statusJTextField = new JTextField(10);
		this.statusJTextField.setEditable(false);

		JGUIUtil.addComponent(statusBarPanel, this.messageJTextField,
			0, 0, 1, 1, 1, 1,
			GridBagConstraints.BOTH, GridBagConstraints.WEST);
		JGUIUtil.addComponent(statusBarPanel, this.statusJTextField,
			1, 0, 1, 1, 0, 0,
			GridBagConstraints.NONE, GridBagConstraints.WEST);
		getContentPane().add("South", statusBarPanel);

		// Display the number of rows and columns.
		int count = this.pluginManagerPanel.getWorksheetRowCount();
		String plural = "s";
		if ( count == 1 ) {
			plural = "";
		}
		int countCol = this.pluginManagerPanel.getWorksheetColumnCount();
		String pluralCol = "s";
    	if ( countCol == 1 ) {
        	pluralCol = "";
    	}
		setMessageStatus("Displaying " + count + " row" + plural + ", " + countCol + " column" + pluralCol + ".", "Ready");

		// Set the UI size and set visible.
		setSize(1000, 400);
		if ( this.parent == null ) {
			JGUIUtil.center(this);
		}
		else {
			JGUIUtil.center(this, this.parent);
		}

		// Set the UI visible.
		setVisible(true);

		// Set the worksheet columns widths after sizing has been done.
		this.pluginManagerPanel.setWorksheetColumnWidths();
	}

}