// TSToolInstallationManager_JPanel - panel for displaying a worksheet containing TSToolInstallationManager data.

/* NoticeStart

TSTool
TSToolis a part of Colorado's Decision Support Systems (CDSS)
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

import RTi.Util.GUI.JGUIUtil;
import RTi.Util.GUI.JScrollWorksheet;
import RTi.Util.GUI.JWorksheet;

import RTi.Util.IO.PropList;

import RTi.Util.Message.Message;

/**
Panel to contain the JWorksheet that displays TSToolInstallationManager data.
*/
@SuppressWarnings("serial")
public class TSToolInstallationManager_JPanel extends JPanel {

	/**
	The TSToolInstallationManager to display in the worksheet.
	*/
	private TSToolInstallationManager installationManager = null;

	/**
	The parent frame containing this panel.
	*/
	private JFrame parent = null;

	/**
	Column widths for the worksheet's fields.
	*/
	private int[] widths = null;

	/**
	The worksheet to display the data.
	*/
	private JWorksheet worksheet = null;

	/**
	Properties for how the worksheet should display.
	*/
	private PropList props = null;

	/**
	Constructor.  Set up the worksheet with a default set of properties:
	<ul>
	<li>JWorksheet.ShowPopupMenu=true</li>
	<li>JWorksheet.SelectionMode=SingleRowSelection</li>
	<li>JWorksheet.AllowCopy=true</li>
	</ul>
	To display with other properties, use the other constructor.
	@param parent the JFrame in which this panel is displayed.
	@param installationManager the TSToolInstallationManager to display in the panel.
	@throws NullPointerException if any of the parameters are null.
	*/
	public TSToolInstallationManager_JPanel ( JFrame parent, TSToolInstallationManager installationManager )
	throws Exception {
		if ( (parent == null) || (installationManager == null) ) {
			throw new NullPointerException();
		}

		this.parent = parent;
		this.installationManager = installationManager;

		this.props = new PropList("TSToolInstallationManager_JPanel.JWorksheet");
		this.props.add("JWorksheet.ShowPopupMenu=true");
		this.props.add("JWorksheet.SelectionMode=ExcelSelection");
		this.props.add("JWorksheet.AllowCopy=true");

		setupUI();
	}

	/**
	Constructor.
	@param parent the JFrame in which this panel is displayed.
	@param installationManager the TSToolInstallationManager to display in the panel.
	@param props the Properties to use to define the worksheet's characteristics.
	@throws NullPointerException if any of the parameters are null.
	*/
	public TSToolInstallationManager_JPanel ( TSToolInstallationManager_JFrame parent, TSToolInstallationManager installationManager, PropList props )
	throws Exception {
		if ( (parent == null) || (installationManager == null) || (props == null) ) {
			throw new NullPointerException();
		}

		this.parent = parent;
		this.installationManager = installationManager;
		this.props = props;

		setupUI();
	}

	/**
	Returns the number of columns in the worksheet.
	@return the number of columns in the worksheet.
	*/
	public int getWorksheetColumnCount() {
    	if ( this.worksheet == null ) {
        	return 0;
    	}
    	return this.worksheet.getColumnCount();
	}

	/**
	Returns the number of rows in the worksheet.
	@return the number of rows in the worksheet.
	*/
	public int getWorksheetRowCount() {
		if ( this.worksheet == null ) {
			return 0;
		}
		return  this.worksheet.getRowCount();
	}

	/**
	Sets up the UI.
	*/
	private void setupUI()
	throws Exception {
		String routine = getClass().getSimpleName() + ".setupUI";
		setLayout ( new GridBagLayout() );
	
		JScrollWorksheet jsw = null;
		try {
			TSToolInstallationManager_TableModel tm = new TSToolInstallationManager_TableModel ( this.installationManager );
			TSToolInstallationManager_CellRenderer cr = new TSToolInstallationManager_CellRenderer(tm);
	
			jsw = new JScrollWorksheet(cr, tm, this.props);
			this.worksheet = jsw.getJWorksheet();
			this.widths = cr.getColumnWidths();
		}
		catch (Exception e) {
			Message.printWarning(2, routine, e);
			jsw = new JScrollWorksheet(0, 0, this.props);
			this.worksheet = jsw.getJWorksheet();
		}
		this.worksheet.setPreferredScrollableViewportSize(null);
		this.worksheet.setHourglassJFrame(this.parent);
		//__worksheet.addMouseListener(this);
		//__worksheet.addKeyListener(this);
	
		JGUIUtil.addComponent(this, jsw,
			0, 0, 1, 1, 1, 1,
			GridBagConstraints.BOTH, GridBagConstraints.CENTER);
	}

	/**
	Sets the worksheet's column widths.
	Use this when the defaults are not correct.
	This should be called after the frame in which the panel is found has called setVisible(true).
	@param colWidths column widths for each column
	*/
	public void setWorksheetColumnWidths ( int [] colWidths ) {
		this.worksheet.setColumnWidths(colWidths);
	}

	/**
	Sets the worksheet's column widths using internal defaults.
	This should be called after the frame in which the panel is found has called setVisible(true).
	*/
	public void setWorksheetColumnWidths() {
		if ( this.worksheet != null ) {
			this.worksheet.calculateColumnWidths();
			if ( this.widths != null ) {
				// There are cases where the column widths are very large.
				// May need to put the check here to guard against because UI may freeze.
				// For now changed ResultSetToDataTableFactory code to handle better at front end.
				/*
				for ( int i = 0; i < __widths.length; i++ ) {
					if ( __widths[i] > 5000 ) {
						Message.printStatus(2, "", "Column width [" + i + "] \"" + __table.getFieldName(i) + "\" = " + __widths[i] + " resetting to 5000");
						__widths[i] = 5000;
					}
				}
					*/
				this.worksheet.setColumnWidths ( this.widths );
			}
		}
	}

}