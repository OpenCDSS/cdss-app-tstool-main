// ----------------------------------------------------------------------------
// setConstant_JDialog - editor for setConstant()
// ----------------------------------------------------------------------------
// Copyright:	See the COPYRIGHT file.
// ----------------------------------------------------------------------------
// History: 
//
// 16 Jan 2001	Steven A. Malers, RTi	Initial version.
// 2002-04-16	SAM, RTi		Clean up interface.
// 2003-12-07	SAM, RTi		Update to Swing.
// 2004-08-12	SAM, RTi		Change to free-format parameters and
//					allow single or monthly constant values.
//					Also allow the set period to be set.
// ----------------------------------------------------------------------------

package DWR.DMI.tstool;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Vector;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import RTi.Util.GUI.JGUIUtil;
import RTi.Util.GUI.SimpleJButton;
import RTi.Util.GUI.SimpleJComboBox;
import RTi.Util.IO.PropList;
import RTi.Util.Message.Message;
import RTi.Util.String.StringUtil;
import RTi.Util.Time.DateTime;

public class setConstant_JDialog extends JDialog
implements ActionListener, ItemListener, KeyListener, WindowListener
{
private SimpleJButton	__cancel_JButton = null,// Cancel Button
			__ok_JButton = null;	// Ok Button
private JFrame		__parent_JFrame = null;	// parent JFrame
private Vector		__command_Vector = null; // Command as Vector of String
private JTextField	__command_JTextField=null;// Command as JTextField
private SimpleJComboBox	__TSID_JComboBox = null;// Field for TSID
private JTextField	__ConstantValue_JTextField = null;
						// Constant value to apply.
private JTextField	__MonthValues_JTextField = null;
						// Monthly value to apply.
private JTextField	__SetStart_JTextField = null;
						// Start date/time for set
private JTextField	__SetEnd_JTextField = null;
						// End date/time for set
private boolean		__error_wait = false;	// Is there an error that we
						// are waiting to be cleared up
						// or Cancel?
private boolean		__first_time = true;

/**
setConstant_JDialog constructor.
@param parent JFrame class instantiating this class.
@param command Command to parse.
@param tsids Time series identifiers for time series available to modify.
*/
public setConstant_JDialog ( JFrame parent, Vector command, Vector tsids )
{	super(parent, true);
	initialize ( parent, "Edit setConstant() Command", command, tsids );
}

/**
Responds to ActionEvents.
@param event ActionEvent object
*/
public void actionPerformed( ActionEvent event )
{	Object o = event.getSource();

	if ( o == __cancel_JButton ) {
		response ( 0 );
	}
	else if ( o == __ok_JButton ) {
		refresh ();
		checkInput();
		if ( !__error_wait ) {
			response ( 1 );
		}
	}
}

/**
Check the input.  If errors exist, warn the user and set the __error_wait flag
to true.  This should be called before response() is allowed to complete.
*/
private void checkInput ()
{	// Nothing to check since the choice only allows valid values.
	String ConstantValue = __ConstantValue_JTextField.getText().trim();
	String MonthValues = "";
	if ( __MonthValues_JTextField != null ) {
		MonthValues = __MonthValues_JTextField.getText().trim();
	}
	String warning = "";
	String routine = "setConstant_JDialog.checkInput";
	if (	(ConstantValue.length() > 0) &&
		!StringUtil.isDouble(ConstantValue) ) {
		warning +=
			"\nConstant \"" + ConstantValue+"\" is not a number.\n"+
			"Specify a number or Cancel.";
	}
	if ( MonthValues.length() > 0 ) {
		Vector v = StringUtil.breakStringList ( MonthValues,",", 0 );
		if ( (v == null) || (v.size() != 12) ) {
			warning += "\n12 monthly values must be specified." +
				"  Correct or Cancel.";
		}
		else {	String val;
			for ( int i = 0; i < 12; i++ ) {
				val = ((String)v.elementAt(i)).trim();
				if ( !StringUtil.isDouble(val) ) {
					warning += "\nMonthly value \"" + val +
						"\" is not a number." +
					"  Correct or Cancel.";
				}
			}
		}
	}
	if ( (ConstantValue.length() == 0) && (MonthValues.length() == 0) ) {
		warning +=
		"\nChoose a single value or monthly values, but not both.";
	}
	if ( (ConstantValue.length() > 0) && (MonthValues.length() > 0) ) {
		warning +=
		"\nChoose a single value or monthly values, but not both.";
	}
	String SetStart = __SetStart_JTextField.getText().trim();
	String SetEnd = __SetEnd_JTextField.getText().trim();
	if (!SetStart.equals("")) {
		try {	DateTime datetime1 = DateTime.parse(SetStart);
			if ( datetime1 == null ) {
				throw new Exception ("bad date");
			}
		}
		catch (Exception e) {
			warning += "\nStart date/time \"" + SetStart +
			"\" is not a valid date/time.\n"+
			"Specify a valid date/time or Cancel.";
		}
	}
	if (!SetEnd.equals("")) {
		try {	DateTime datetime2 = DateTime.parse(SetEnd);
			if ( datetime2 == null ) {
				throw new Exception ("bad date");
			}
		}
		catch (Exception e) {
			warning += "\nEnd date/time \"" +
			SetEnd + "\" is not a valid date/time.\n"+
			"Specify a valid date/time or Cancel.";
		}
	}
	if ( warning.length() > 0 ) {
		Message.printWarning ( 1, routine, warning );
		__error_wait = true;
	}
}

/**
Free memory for garbage collection.
*/
protected void finalize ()
throws Throwable
{	__TSID_JComboBox = null;
	__cancel_JButton = null;
	__command_JTextField = null;
	__command_Vector = null;
	__ok_JButton = null;
	__parent_JFrame = null;
	__ConstantValue_JTextField = null;
	__MonthValues_JTextField = null;
	__SetStart_JTextField = null;
	__SetEnd_JTextField = null;
	super.finalize ();
}

/**
Return the text for the command.
@return the text for the command or null if there is a problem with the command.
*/
public Vector getText ()
{	if (	(__command_Vector.size() == 0) ||
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
{	__parent_JFrame = parent;
	__command_Vector = command;

	addWindowListener( this );

        Insets insetsTLBR = new Insets(2,2,2,2);

	// Main panel...

	JPanel main_JPanel = new JPanel();
	main_JPanel.setLayout( new GridBagLayout() );
	GridBagConstraints gbc = new GridBagConstraints();
	getContentPane().add ( "North", main_JPanel );
	int y = 0;

        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Set time series data values to a single or monthly (Jan - Dec)"
		+" constant values." ), 
		0, y, 7, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"If the time series data interval is month or smaller, " +
		"constant values for each month can be specified." ), 
		0, ++y, 7, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"In this case, each date/time that matches a month will have " +
		"its corresponding value set." ), 
		0, ++y, 7, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Time series identifier/alias:" ), 
		0, ++y, 1, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.EAST);
	__TSID_JComboBox = new SimpleJComboBox ( false );
	int size = 0;
	if ( tsids != null ) {
		size = tsids.size();
	}
	if ( size == 0 ) {
		Message.printWarning ( 1, "setConstant_JDialog.initialize",
		"You must define time series before inserting a setConstant() "
		+ "command." );
		response ( 0 );
	}
	__TSID_JComboBox.setData ( tsids );
	// Always allow a "*" to let all time series be set...
	__TSID_JComboBox.add ( "*" );
	__TSID_JComboBox.addItemListener ( this );
        JGUIUtil.addComponent(main_JPanel, __TSID_JComboBox,
		1, y, 6, 1, 1, 0, insetsTLBR, gbc.HORIZONTAL, gbc.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel ( "Constant value:" ), 
		0, ++y, 1, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.EAST);
	__ConstantValue_JTextField = new JTextField ( 10 );
	__ConstantValue_JTextField.addKeyListener ( this );
        JGUIUtil.addComponent(main_JPanel, __ConstantValue_JTextField,
		1, y, 6, 1, 1, 0, insetsTLBR, gbc.NONE, gbc.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Use for all intervals.."),
		3, y, 3, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.WEST );

        JGUIUtil.addComponent(main_JPanel, new JLabel ( "Monthly values:" ), 
		0, ++y, 1, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.EAST);
	__MonthValues_JTextField = new JTextField ( 20 );
	__MonthValues_JTextField.addKeyListener ( this );
        JGUIUtil.addComponent(main_JPanel, __MonthValues_JTextField,
		1, y, 6, 1, 1, 0, insetsTLBR, gbc.NONE, gbc.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Monthly values, separated by commas."),
		3, y, 3, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.WEST );

        JGUIUtil.addComponent(main_JPanel, new JLabel ("Set start:"), 
		0, ++y, 1, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.EAST);
	__SetStart_JTextField = new JTextField (20);
	__SetStart_JTextField.addKeyListener (this);
        JGUIUtil.addComponent(main_JPanel, __SetStart_JTextField,
		1, y, 2, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Set start (optional).  Default is all."),
		3, y, 3, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.WEST );

        JGUIUtil.addComponent(main_JPanel, new JLabel ( "Set End:"), 
		0, ++y, 2, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.EAST);
	__SetEnd_JTextField = new JTextField (20);
	__SetEnd_JTextField.addKeyListener (this);
        JGUIUtil.addComponent(main_JPanel, __SetEnd_JTextField,
		1, y, 6, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Set end (optional).  Default is all."),
		3, y, 3, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.WEST );

        JGUIUtil.addComponent(main_JPanel, new JLabel ( "Command:" ), 
		0, ++y, 1, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.EAST);
	__command_JTextField = new JTextField ( 50 );
	__command_JTextField.setEditable ( false );
	JGUIUtil.addComponent(main_JPanel, __command_JTextField,
		1, y, 6, 1, 1, 0, insetsTLBR, gbc.HORIZONTAL, gbc.WEST );

	// Refresh the contents...
	refresh ();

	// South Panel: North
	JPanel button_JPanel = new JPanel();
	button_JPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JGUIUtil.addComponent(main_JPanel, button_JPanel, 
		0, ++y, 8, 1, 1, 0, insetsTLBR, gbc.HORIZONTAL, gbc.CENTER);

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

	if ( o.equals(__TSID_JComboBox) ) {
		refresh();
	}
}

/**
Respond to KeyEvents.
*/
public void keyPressed ( KeyEvent event )
{	int code = event.getKeyCode();

	if ( code == event.VK_ENTER ) {
		refresh ();
		checkInput();
		if ( !__error_wait ) {
			response ( 1 );
		}
	}
}

public void keyReleased ( KeyEvent event )
{	// Only refresh if the text is not just a "-" or ".".  Otherwise, we'll
	// get a warning about it not being a number...
	String constant = __ConstantValue_JTextField.getText();
	if ( constant == null ) {
		constant = "";
	}
	else {	constant = constant.trim();
	}
	if ( !constant.equals("-") && !constant.equals(".") ) {
		refresh();
	}
}

public void keyTyped ( KeyEvent event ) {;}

/**
Refresh the command from the other text field contents.
*/
private void refresh ()
{	String routine = "setConstant_JDialog.refresh";
	String TSID = "";
	String ConstantValue = "";
	String MonthValues = "";
	String SetStart = "";
	String SetEnd = "";
	__error_wait = false;
	String command_string = ((String)__command_Vector.elementAt(0)).trim();
	if ( __first_time ) {
		__first_time = false;
		if (	(command_string.length() > 0) &&
			command_string.indexOf('=') < 0 ) {
			// Old syntax...
			// Parse the incoming string and fill the fields...
			Vector v = StringUtil.breakStringList ( ((String)
				__command_Vector.elementAt(0)).trim(),"() ,",
				StringUtil.DELIM_SKIP_BLANKS );
			if ( (v != null) && (v.size() == 3) ) {
				// Second field is identifier...
				TSID = ((String)v.elementAt(1)).trim();
				// Third field has constant...
				ConstantValue = ((String)v.elementAt(2)).trim();
			}
		}
		else {	// New syntax...
			Vector v = StringUtil.breakStringList (
			((String)__command_Vector.elementAt(0)).trim(),"()",
			StringUtil.DELIM_SKIP_BLANKS );
			PropList props = null;
			if (	(v != null) && (v.size() > 1) &&
				(((String)v.elementAt(1)).indexOf("=") > 0) ) {
				props = PropList.parse (
					(String)v.elementAt(1), routine, "," );
			}
			if ( props == null ) {
				props = new PropList ( routine );
			}
			TSID = props.getValue ( "TSID" );
			ConstantValue = props.getValue ( "ConstantValue" );
			MonthValues = props.getValue ( "MonthValues" );
			SetStart = props.getValue ( "SetStart" );
			SetEnd = props.getValue ( "SetEnd" );
		}

		// Now select the item in the list.  If not a match, print a
		// warning.
		if ( (TSID == null) || (TSID.length() == 0) ) {
			// Select default...
			__TSID_JComboBox.select ( 0 );
		}
		else {	if (	JGUIUtil.isSimpleJComboBoxItem(
				__TSID_JComboBox, TSID,
				JGUIUtil.NONE, null, null ) ) {
				__TSID_JComboBox.select ( TSID );
			}
			else {	Message.printWarning ( 1, routine,
				"Existing setConstant() references a " +
				"non-existent\n"+
				"time series \"" + TSID + "\".  Select a\n" +
				"different time series or Cancel." );
			}
		}
		if ( ConstantValue != null ) {
			__ConstantValue_JTextField.setText( ConstantValue );
		}
		if ( MonthValues != null ) {
			__MonthValues_JTextField.setText( MonthValues );
		}
		if ( SetStart != null ) {
			__SetStart_JTextField.setText( SetStart );
		}
		if ( SetEnd != null ) {
			__SetEnd_JTextField.setText( SetEnd );
		}
	}
	// Regardless, reset the command from the fields...
	TSID = __TSID_JComboBox.getSelected().trim();
	ConstantValue = __ConstantValue_JTextField.getText().trim();
	MonthValues = __MonthValues_JTextField.getText().trim();
	SetStart = __SetStart_JTextField.getText().trim();
	SetEnd = __SetEnd_JTextField.getText().trim();
	StringBuffer b = new StringBuffer ();
	if ( TSID.length() > 0 ) {
		b.append ( "TSID=\"" + TSID + "\"" );
	}
	if ( ConstantValue.length() > 0 ) {
		if ( b.length() > 0 ) {
			b.append ( "," );
		}
		b.append ( "ConstantValue=" + ConstantValue );
	}
	if ( MonthValues.length() > 0 ) {
		if ( b.length() > 0 ) {
			b.append ( "," );
		}
		b.append ( "MonthValues=\"" + MonthValues + "\"" );
	}
	if ( SetStart.length() > 0 ) {
		if ( b.length() > 0 ) {
			b.append ( "," );
		}
		b.append ( "SetStart=\"" + SetStart + "\"" );
	}
	if ( SetEnd.length() > 0 ) {
		if ( b.length() > 0 ) {
			b.append ( "," );
		}
		b.append ( "SetEnd=\"" + SetEnd + "\"" );
	}
	__command_JTextField.setText("setConstant(" + b.toString() + ")" );
	__command_Vector.removeAllElements();
	__command_Vector.addElement ( __command_JTextField.getText() );
}

/**
Return the time series command as a Vector of String.
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

} // end setConstant_JDialog
