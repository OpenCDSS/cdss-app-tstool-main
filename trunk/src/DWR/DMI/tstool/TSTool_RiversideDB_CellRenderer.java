// ----------------------------------------------------------------------------
// TSTool_RiversideDB_CellRenderer - class to render cells for RiversideDB TS
//					list
// ----------------------------------------------------------------------------
// Copyright:   See the COPYRIGHT file
// ----------------------------------------------------------------------------
// History:
//
// 2003-06-16	Steven A. Malers, RTi	Initial version.
// 2007-02-26	SAM, RTi		Clean up code based on Eclipse feedback.
// ----------------------------------------------------------------------------

package DWR.DMI.tstool;

import RTi.Util.GUI.JWorksheet_DefaultTableCellRenderer;

/**
This class is used to render cells for TSTool_RiversideDB_TableModel data.
*/
public class TSTool_RiversideDB_CellRenderer
extends JWorksheet_DefaultTableCellRenderer {

TSTool_RiversideDB_TableModel __table_model = null;	// Table model to
							// render

/**
Constructor.
@param table_model The TSTool_RiversideDB_TableModel to render.
*/
public TSTool_RiversideDB_CellRenderer (
				TSTool_RiversideDB_TableModel table_model )
{	__table_model = table_model;
}

/**
Returns the format for a given column.
@param column the colum for which to return the format.
@return the column format as used by StringUtil.formatString().
*/
public String getFormat(int column) {
	return __table_model.getFormat(column);	
}

/**
Returns the widths of the columns in the table.
@return an integer array of the widths of the columns in the table.
*/
public int[] getColumnWidths() {
	return __table_model.getColumnWidths();
}

}
