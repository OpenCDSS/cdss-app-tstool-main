package DWR.DMI.tstool;

import RTi.Util.GUI.JWorksheet_AbstractExcelCellRenderer;
//import RTi.Util.GUI.JWorksheet_DefaultTableCellRenderer;

/**
This class is used to render cells for TSTool_ColoradoBNDSS_TableModel data.
*/
public class TSTool_ColoradoBNDSS_CellRenderer extends JWorksheet_AbstractExcelCellRenderer {
//JWorksheet_DefaultTableCellRenderer {

TSTool_ColoradoBNDSS_TableModel __table_model = null;	// Table model to render

/**
Constructor.
@param table_model The TSTool_RiversideDB_TableModel to render.
*/
public TSTool_ColoradoBNDSS_CellRenderer ( TSTool_ColoradoBNDSS_TableModel table_model )
{	__table_model = table_model;
}

/**
Returns the format for a given column.
@param column the column for which to return the format.
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