// TSToolPluginManager_CellRenderer - class for rendering TSToolPluginManager data in a JWorksheet.

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

import java.awt.Component;

import java.util.Date;

import javax.swing.JTable;
import javax.swing.SwingConstants;

import RTi.Util.GUI.JWorksheet;
import RTi.Util.GUI.JWorksheet_AbstractExcelCellRenderer;

import RTi.Util.String.StringUtil;

/**
This class is a cell renderer for cells in TSToolPluginManager JWorksheets.
*/
@SuppressWarnings("serial")
public class TSToolPluginManager_CellRenderer
extends JWorksheet_AbstractExcelCellRenderer {

	/**
	The table model for which this class will render cells.
	*/
	private TSToolPluginManager_TableModel tableModel = null;

	/**
	Constructor.
	@param tableModel the tableModel for which to render cells.
	*/
	public TSToolPluginManager_CellRenderer ( TSToolPluginManager_TableModel tableModel ) {
		this.tableModel = tableModel;
	}

	/**
	Renders a cell for the worksheet.
	@param table the JWorksheet for which a cell will be renderer.
	@param value the value in the cell.
	@param isSelected whether the cell is selected or not.
	@param hasFocus whether the cell has the input focus or not.
	@param row the row in the worksheet where the cell is located.
	@param column the column in the worksheet where the cell is located.
	@return the rendered cell.
	*/
	public Component getTableCellRendererComponent ( JTable table, Object value,
	boolean isSelected, boolean hasFocus, int row, int column ) {
		String str = "";
 		if ( value != null ) {
 			// The following will use DateTime.toString(), which will result in ISO formatting for DateTime objects.
			str = value.toString();
		}

		int abscolumn = ((JWorksheet)table).getAbsoluteColumn(column);

		String format = getFormat(abscolumn);
		//Message.printStatus(2,"","Format for value " + value + " is " + format);

		int justification = SwingConstants.LEFT;

		if (value instanceof Integer) {
			justification = SwingConstants.RIGHT;
			str = StringUtil.formatString(value, format);
		}
		else if (value instanceof Double) {
			justification = SwingConstants.RIGHT;
			if ( !((Double)value).isNaN() ) {
		    	str = StringUtil.formatString(value, format);
			}
		}
		else if (value instanceof Date) {
			justification = SwingConstants.LEFT;
			// FYI: str has been set above with str = value.toString().
		}
		else if (value instanceof String) {
			justification = SwingConstants.LEFT;
			str = StringUtil.formatString(value, format);
		}
		else if (value instanceof Float) {
			justification = SwingConstants.RIGHT;
	    	if ( !((Float)value).isNaN() ) {
	        	str = StringUtil.formatString(value, format);
	    	}
		}
		else {
			justification = SwingConstants.LEFT;
		}

		str = str.trim();

		// Call the DefaultTableCellRenderer's version of this method so that all the cell highlighting is handled properly.
		super.getTableCellRendererComponent(table, str, isSelected, hasFocus, row, column);

		int tableAlignment = ((JWorksheet)table).getColumnAlignment(abscolumn);
		if (tableAlignment != JWorksheet.DEFAULT) {
			justification = tableAlignment;
		}

		setHorizontalAlignment(justification);
		setFont(((JWorksheet)table).getCellFont());

		return this;
	}

	/**
	Returns the data format for the given column.
	@param column the column for which to return the data format.
	@return the data format for the given column.
	*/
	public String getFormat ( int column ) {
		return this.tableModel.getFormat ( column );
	}

	/**
	Returns the widths the columns should be set to.
	@return the widths the columns should be set to.
	*/
	public int[] getColumnWidths() {
		return this.tableModel.getColumnWidths();
	}

}