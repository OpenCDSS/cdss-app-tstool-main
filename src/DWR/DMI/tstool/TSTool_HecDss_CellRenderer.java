package DWR.DMI.tstool;

import RTi.Util.GUI.JWorksheet_DefaultTableCellRenderer;

/**
This class is used to render cells for TSTool_HecDss_TableModel data.
*/
@SuppressWarnings("serial")
public class TSTool_HecDss_CellRenderer extends JWorksheet_DefaultTableCellRenderer{

TSTool_HecDss_TableModel __tableModel = null;	// Table model to render

/**
Constructor.
@param table_model The TSTool_HecDSS_TableModel to render.
*/
public TSTool_HecDss_CellRenderer ( TSTool_HecDss_TableModel table_model )
{	__tableModel = table_model;
}

/**
Returns the format for a given column.
@param column the column for which to return the format.
@return the column format as used by StringUtil.formatString().
*/
public String getFormat(int column) {
	return __tableModel.getFormat(column);	
}

/**
Returns the widths of the columns in the table.
@return an integer array of the widths of the columns in the table.
*/
public int[] getColumnWidths() {
	return __tableModel.getColumnWidths();
}

}