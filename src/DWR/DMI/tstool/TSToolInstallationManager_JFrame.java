// TSToolInstallationManager_JFrame - frame for displaying a table with TSTool installations

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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import RTi.Util.GUI.JGUIUtil;

/**
This class is the frame in which the panel displaying TSToolInstallation data in a worksheet is displayed.
*/
@SuppressWarnings("serial")
public class TSToolInstallationManager_JFrame extends JFrame
{

	/**
	The TSToolInstallationManager that is being displayed.
	*/
	private TSToolInstallationManager installationManager = null;

	/**
	The panel containing the worksheet that will be displayed in the frame.
	*/
	private TSToolInstallationManager_JPanel installationManagerPanel = null;

	/**
 	* Parent JFrame, used to center the table window.
 	*/
	private JFrame parent = null;

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
	@param installatinManager the TSToolInstallationManager to display
	@throws Exception if table is null.
	*/
	public TSToolInstallationManager_JFrame ( String title, TSToolInstallationManager installationManager )
	throws Exception {
		// Call the overloaded method with no parent.
		this ( null, title, installationManager );
	}

	/**
	Constructor.
	@param parent parent JFrame, used to center the window on the parent.
	@param title the title to put on the frame.
	@param installationManager the installationManager to display
	@throws Exception if table is null.
	*/
	public TSToolInstallationManager_JFrame ( JFrame parent, String title, TSToolInstallationManager installationManager )
	throws Exception {
		JGUIUtil.setIcon ( this, JGUIUtil.getIconImage() );
		if ( title == null ) {
			if ( (JGUIUtil.getAppNameForWindows() == null) || JGUIUtil.getAppNameForWindows().equals("") ) {
				setTitle ( "TSTool Installation Manager" );
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
		this.installationManager = installationManager;

		setupUI();
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
		this.installationManagerPanel = new TSToolInstallationManager_JPanel(this, this.installationManager);

		getContentPane().add("Center", this.installationManagerPanel);

		JPanel statusBar = new JPanel();
		statusBar.setLayout(new GridBagLayout());

		this.messageJTextField = new JTextField(20);
		this.messageJTextField.setEditable(false);
		this.statusJTextField = new JTextField(10);
		this.statusJTextField.setEditable(false);

		JGUIUtil.addComponent(statusBar, this.messageJTextField,
			0, 0, 1, 1, 1, 1,
			GridBagConstraints.BOTH, GridBagConstraints.WEST);
		JGUIUtil.addComponent(statusBar, this.statusJTextField,
			1, 0, 1, 1, 0, 0,
			GridBagConstraints.NONE, GridBagConstraints.WEST);
		getContentPane().add("South", statusBar);

		setSize(600, 400);
		if ( this.parent == null ) {
			JGUIUtil.center(this);
		}
		else {
			JGUIUtil.center(this, this.parent);
		}

		int count = this.installationManagerPanel.getWorksheetRowCount();
		String plural = "s";
		if ( count == 1 ) {
			plural = "";
		}
		int countCol = this.installationManagerPanel.getWorksheetColumnCount();
		String pluralCol = "s";
    	if ( countCol == 1 ) {
        	pluralCol = "";
    	}

		setMessageStatus("Displaying " + count + " row" + plural + ", " + countCol + " column" + pluralCol + ".", "Ready");

		setVisible(true);

	this.installationManagerPanel.setWorksheetColumnWidths();
}

}