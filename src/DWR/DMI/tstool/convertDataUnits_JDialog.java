// ----------------------------------------------------------------------------
// convertDataUnits_JDialog - editor for convertDataUnits()
// ----------------------------------------------------------------------------
// Copyright:	See the COPYRIGHT file.
// ----------------------------------------------------------------------------
// History: 
//
// 2003-03-24	Steven A. Malers, RTi	Initial version (copy and modify
//					cumulate_Dialog).
// 2003-12-04	SAM, RTi		Update to Swing.
// 2007-02-26	SAM, RTi		Clean up code based on Eclipse feedback.
// ----------------------------------------------------------------------------

package DWR.DMI.tstool;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Vector;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import RTi.Util.GUI.JGUIUtil;
import RTi.Util.GUI.SimpleJButton;
import RTi.Util.GUI.SimpleJComboBox;
import RTi.Util.IO.DataDimension;
import RTi.Util.IO.DataUnits;
import RTi.Util.Message.Message;
import RTi.Util.String.StringUtil;

public class convertDataUnits_JDialog extends JDialog
implements ActionListener, ItemListener, KeyListener, WindowListener
{
private SimpleJButton	__cancel_JButton = null,// Cancel Button
			__ok_JButton = null;	// Ok Button
private Vector		__command_Vector = null;// Command as Vector of String
private JTextField	__command_JTextField=null;// Command as JTextField
private SimpleJComboBox	__alias_JComboBox = null;// Field for time series alias
private SimpleJComboBox	__dimension_JComboBox=null; // Field for data dimensions
private SimpleJComboBox	__units_JComboBox=null;	// Field for data units within
						// a dimension 
private boolean		__error_wait = false;	// Is there an error that we
						// are waiting to be cleared up
						// or Cancel?
private boolean		__first_time = true;

/**
convertDataUnits_JDialog constructor.
@param parent JFrame class instantiating this class.
@param command Command to parse.
@param tsids Time series identifiers for time series available to convert.
*/
public convertDataUnits_JDialog ( JFrame parent, Vector command, Vector tsids )
{	super(parent, true);
	initialize ( parent, "Edit convertDataUnits() Command", command, tsids);
}

/**
Responds to ActionEvents.
@param event ActionEvent object
*/
public void actionPerformed( ActionEvent event )
{	Object o = event.getSource();

	if ( o == __cancel_JButton ) {
		__command_Vector = null;
		response ( 0 );
	}
	else if ( o == __ok_JButton ) {
		refresh();
		checkInput();
		if ( !__error_wait ) {
			response ( 1 );
		}
	}
}

/**
Check the user input for errors and set __error_wait accordingly.
*/
private void checkInput ()
{	String warning = "";

	if ( warning.length() > 0 ) {
		__error_wait = true;
		Message.printWarning ( 1, "convertDataUnits_JDialog", warning );
	}
}

/**
Free memory for garbage collection.
*/
protected void finalize ()
throws Throwable
{	__alias_JComboBox = null;
	__dimension_JComboBox = null;
	__units_JComboBox = null;
	__cancel_JButton = null;
	__command_JTextField = null;
	__command_Vector = null;
	__ok_JButton = null;
	super.finalize ();
}

/**
Return the text for the command.
@return the text for the command or null if there is a problem with the command.
*/
public Vector getText ()
{	if (	(__command_Vector == null) ||(__command_Vector.size() == 0) ||
		((String)__command_Vector.elementAt(0)).equals("") ) {
		return null;
	}
	return __command_Vector;
}

/**
Instantiates the GUI components.
@param parent JFrame class instantiating this class.
@param title Dialog title.
@param command Vector of String containing the command.
@param tsids Time series identifiers from
TSEngine.getTSIdentifiersFromCommands().
*/
private void initialize ( JFrame parent, String title, Vector command,
			Vector tsids )
{	__command_Vector = command;

	addWindowListener( this );

        Insets insetsTLBR = new Insets(0,2,0,2);

	// Main panel...

	JPanel main_JPanel = new JPanel();
	main_JPanel.setLayout( new GridBagLayout() );
	getContentPane().add ( "North", main_JPanel );
	int y = 0;

        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"The units of the selected time series will be converted"+
		" to the new data units." ), 
		0, y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"The old and new data units must have the same dimension." ), 
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"However, the dimension is not checked until time series are" +
		" actually processed." ), 
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Time Series to Convert:" ), 
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	__alias_JComboBox = new SimpleJComboBox ( false );
	int size = 0;
	if ( tsids != null ) {
		size = tsids.size();
	}
	if ( size == 0 ) {
		tsids = new Vector();
	}
	__alias_JComboBox.setData ( tsids );
	// Always allow a "*" to let all time series be filled...
	__alias_JComboBox.add ( "*" );
	__alias_JComboBox.addItemListener ( this );
        JGUIUtil.addComponent(main_JPanel, __alias_JComboBox,
		1, y, 6, 1, 1, 0, insetsTLBR, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Dimension:" ), 
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	__dimension_JComboBox = new SimpleJComboBox ( false );
	Vector dimension_data_Vector0 = DataDimension.getDimensionData();
	Vector dimension_data_Vector = null;
	size = 0;
	if ( dimension_data_Vector0 != null ) {
		size = dimension_data_Vector0.size();
		Message.printStatus ( 1, "", "Number of dimension: " + size );
		dimension_data_Vector = new Vector(size);
		DataDimension dim;
		for ( int i = 0; i < size; i++ ) {
			dim =(DataDimension)dimension_data_Vector0.elementAt(i);
			if ( dim.getAbbreviation().length() > 0 ) {
				dimension_data_Vector.addElement (
				dim.getAbbreviation() + " - " +
				dim.getLongName() );
			}
		}
		dimension_data_Vector =
		StringUtil.sortStringList(dimension_data_Vector);
	}
	else {
		Message.printStatus ( 1, "", "Number of dimension (null): "+0);
	}
	size = 0;
	if ( dimension_data_Vector != null ) {
		size = dimension_data_Vector.size();
	}
	for ( int i = 0; i < size; i++ ) {
		if ( ((String)dimension_data_Vector.elementAt(i)).length() > 0){
			__dimension_JComboBox.add (
			(String)dimension_data_Vector.elementAt(i) );
		}
	}
	if ( size > 0 ) {
		__dimension_JComboBox.select ( 0 );
	}
	__dimension_JComboBox.addItemListener ( this );
        JGUIUtil.addComponent(main_JPanel, __dimension_JComboBox,
		1, y, 2, 1, 1, 0, insetsTLBR, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"New Data Units:" ), 
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	__units_JComboBox = new SimpleJComboBox ( false );
	__units_JComboBox.addItemListener ( this );
        JGUIUtil.addComponent(main_JPanel, __units_JComboBox,
		1, y, 2, 1, 1, 0, insetsTLBR, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel ( "Command:" ), 
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	__command_JTextField = new JTextField ( 50 );
	__command_JTextField.setEditable ( false );
	JGUIUtil.addComponent(main_JPanel, __command_JTextField,
		1, y, 6, 1, 1, 0, insetsTLBR, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);

	// Refresh the contents...
	refresh ();

	// South Panel: North
	JPanel button_JPanel = new JPanel();
	button_JPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JGUIUtil.addComponent(main_JPanel, button_JPanel, 
		0, ++y, 8, 1, 1, 0, insetsTLBR, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER);

	__cancel_JButton = new SimpleJButton("Cancel", this);
	button_JPanel.add ( __cancel_JButton );
	__ok_JButton = new SimpleJButton("OK", this);
	button_JPanel.add ( __ok_JButton );

	if ( title != null ) {
		setTitle ( title );
	}
	setResizable ( true );
        pack();
        JGUIUtil.center( this );
        super.setVisible( true );
}

/**
Handle ItemEvent events.
@param e ItemEvent to handle.
*/
public void itemStateChanged ( ItemEvent e )
{	Object o = e.getItemSelectable();
	if ( o == __dimension_JComboBox ) {
		// Refresh the units...
		refreshUnits ();
	}
	else {	refresh();
	}
}

/**
Respond to KeyEvents.
*/
public void keyPressed ( KeyEvent event )
{	int code = event.getKeyCode();

	if ( code == KeyEvent.VK_ENTER ) {
		refresh ();
		checkInput ();
		if ( !__error_wait ) {
			response ( 1 );
		}
	}
}

public void keyReleased ( KeyEvent event )
{	refresh();
}

public void keyTyped ( KeyEvent event ) {;}

/**
Refresh the command from the other text field contents.  The command is
of the form:
<pre>
convertDataUnits(TSID,NewUnits)
</pre>
*/
private void refresh ()
{	String alias = "";
	String new_units = "";
	__error_wait = false;
	if ( __first_time ) {
		__first_time = false;
		// Parse the incoming string and fill the fields...
		Vector v = StringUtil.breakStringList (
			((String)__command_Vector.elementAt(0)).trim(),"() ,",
			StringUtil.DELIM_SKIP_BLANKS );
		if ( (v != null) && (v.size() == 3) ) {
			// Second field is identifier...
			alias = ((String)v.elementAt(1)).trim();
			// Now select the item in the list.  If not a match,
			// print a warning.
			if (	JGUIUtil.isSimpleJComboBoxItem(
				__alias_JComboBox, alias,
				JGUIUtil.NONE, null, null ) ) {
				__alias_JComboBox.select ( alias );
			}
			else {	Message.printWarning ( 1,
				"convertDataUnits_JDialog.refresh",
				"Existing convertDataUnits() references a " +
				"non-existent\n"+
				"time series \"" + alias + "\".  Select a\n" +
				"different time series or Cancel." );
			}
			// Third field has new data units.  However, need to
			// figure out the dimension of the units and set the
			// dimension choice as well as the units choice...
			new_units = ((String)v.elementAt(2)).trim();
			DataUnits dataunits = null;
			try {	dataunits = DataUnits.lookupUnits ( new_units);
			}
			catch ( Exception e ) {
				Message.printWarning ( 1,
				"convertDataUnits_JDialog.refresh",
				"Existing convertDataUnits() " +
				"references unrecognized\n"+
				"data units \"" + new_units +
				"\".  Select recognized data " +
				"units or Cancel.");
				Message.printWarning ( 2,"",e);
				__error_wait = true;
			}
			if ( !__error_wait ) {
				try {	// First select the dimension...
					JGUIUtil.selectTokenMatches (
					__dimension_JComboBox,
					true, " ", 0, 0,
					dataunits.getDimension(
					).getAbbreviation(), null );
				}
				catch ( Exception e ) {
					Message.printWarning ( 1,
					"convertDataUnits_JDialog.refresh",
					"Existing convertDataUnits() " +
					"references units with unrecognized\n"+
					"data dimension \"" +
					dataunits.getDimension(
					).getAbbreviation() +
					"\".  Select recognized data units " +
					"or Cancel.");
					Message.printWarning ( 2, "", e );
					__error_wait = true;
				}
			}
			if ( !__error_wait ) {
				try {	// Now select the units...
					refreshUnits();
					JGUIUtil.selectTokenMatches (
					__units_JComboBox,
					true, " ", 0, 0, new_units, null );
				}
				catch ( Exception e ) {
					Message.printWarning ( 1,
					"convertDataUnits_JDialog.refresh",
					"Existing convertDataUnits() " +
					"references unrecognized\n"+
					"data units \"" + new_units +
					"\".  Select recognized data " +
					"units or Cancel.");
					Message.printWarning ( 2,"",e);
					__error_wait = true;
				}
			}
		}
		else {	refreshUnits ();
		}
	}
	// Regardless, reset the command from the fields...
	alias = __alias_JComboBox.getSelected();
	new_units =StringUtil.getToken(__units_JComboBox.getSelected()," ",0,0);
	__command_JTextField.setText("convertDataUnits(" + alias + "," +
	new_units + ")" );
	__command_Vector.removeAllElements();
	__command_Vector.addElement ( __command_JTextField.getText() );
}

/**
Refresh the data units based on the dimension.
*/
private void refreshUnits ()
{	String dimension = __dimension_JComboBox.getSelected();
	Vector units_Vector = DataUnits.lookupUnitsForDimension (
		null, StringUtil.getToken(dimension," ",0,0) );
	int size = 0;
	if ( units_Vector != null ) {
		size = units_Vector.size();
	}
	__units_JComboBox.removeAll ();
	DataUnits units = null;
	Vector units_sorted_Vector = new Vector();
	for ( int i = 0; i < size; i++ ) {
		units = (DataUnits)units_Vector.elementAt(i);
		units_sorted_Vector.addElement ( units.getAbbreviation() +
			" - " + units.getLongName() );
	}
	units_sorted_Vector = StringUtil.sortStringList ( units_sorted_Vector );
	__units_JComboBox.setData ( units_sorted_Vector );
}

/**
Return the command as a Vector of String.
@return returns the command text or null if no command.
*/
public Vector response ( int status )
{	setVisible( false );
	dispose();
	if ( status == 0 ) {
		// Cancel...
		__command_Vector = null;
		return null;
	}
	else {	refresh();
		if (	(__command_Vector.size() == 0) ||
			((String)__command_Vector.elementAt(0)).equals("") ) {
			return null;
		}
		return __command_Vector;
	}
}

/**
Responds to WindowEvents.
@param event WindowEvent object
*/
public void windowClosing( WindowEvent event )
{	response ( 0 );
}

public void windowActivated( WindowEvent evt ){;}
public void windowClosed( WindowEvent evt ){;}
public void windowDeactivated( WindowEvent evt ){;}
public void windowDeiconified( WindowEvent evt ){;}
public void windowIconified( WindowEvent evt ){;}
public void windowOpened( WindowEvent evt ){;}

} // end convertDataUnits_JDialog
