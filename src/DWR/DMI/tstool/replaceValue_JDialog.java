// ----------------------------------------------------------------------------
// replaceValue_JDialog - editor for replaceValue()
// ----------------------------------------------------------------------------
// Copyright:	See the COPYRIGHT file.
// ----------------------------------------------------------------------------
// History: 
//
// 27 Aug 2001	Steven A. Malers, RTi	Initial version.  Copy
//					setConstantBefore_Dialog and modify.
// 2002-04-23	SAM, RTi		Clean up dialog.
// 2003-12-07	SAM, RTi		Update to Swing.
// 2007-02-26	SAM, RTi		Clean up code based on Eclipse feedback.
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
import RTi.Util.Message.Message;
import RTi.Util.String.StringUtil;
import RTi.Util.Time.DateTime;

public class replaceValue_JDialog extends JDialog
implements ActionListener, ItemListener, KeyListener, WindowListener
{
private SimpleJButton	__cancel_JButton = null,// Cancel Button
			__ok_JButton = null;	// Ok Button
private Vector		__command_Vector = null; // Command as Vector of String
private JTextField	__date1_JTextField = null,// First date to apply set.
			__date2_JTextField = null,// Last date to apply set.
			__command_JTextField=null;// Command as JTextField
private SimpleJComboBox	__alias_JComboBox = null;// Field for time series alias
private JTextField	__minvalue_JTextField = null,
			__maxvalue_JTextField = null,
			__constant_JTextField = null;
private boolean		__error_wait = false;	// Is there an error that we
						// are waiting to be cleared up
						// or Cancel?
private boolean		__first_time = true;

/**
replaceValue_JDialog constructor.
@param parent JFrame class instantiating this class.
@param command Command to parse.
@param tsids Time series identifiers for time series available to modify.
*/
public replaceValue_JDialog ( JFrame parent, Vector command, Vector tsids )
{	super(parent, true);
	initialize ( parent, "Edit ReplaceValue() Command", command, tsids );
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
{	String routine = "replaceValue_JDialog.checkInput";
	String constant = __constant_JTextField.getText().trim();
	String minvalue = __minvalue_JTextField.getText().trim();
	String maxvalue = __maxvalue_JTextField.getText().trim();
	String date1 = __date1_JTextField.getText().trim();
	String date2 = __date2_JTextField.getText().trim();
	String warning = "";

	if ( !StringUtil.isDouble(constant) ) {
		warning +=
			"\nReplacement constant \"" + constant +
			"\" is not a number.\n"+
			"Specify a number or Cancel.";
	}
	if ( (minvalue.length() > 0) && !StringUtil.isDouble(minvalue) ) {
		warning +=
			"Minimum value \"" + minvalue + "\" is not a number.\n"+
			"Specify a number or Cancel.";
	}
	if ( (maxvalue.length() > 0) && !StringUtil.isDouble(maxvalue) ) {
		warning +=
			"Maximum value \"" + maxvalue + "\" is not a number.\n"+
			"Specify a number or Cancel.";
	}
	DateTime startdate = null, enddate = null;
	if (	!date1.equals("*") &&
		!date1.equalsIgnoreCase("OutputStart") &&
		!date1.equalsIgnoreCase("OutputEnd") &&
		!date1.equalsIgnoreCase("QueryStart") &&
		!date1.equalsIgnoreCase("QueryEnd") ) {
		try {	startdate = DateTime.parse( date1 );
		}
		catch ( Exception e ) {
			warning +=
				"\nStart date/time \"" + date1 +
				"\" is not a valid date/time.\n"+
				"Specify a date, *, or press Cancel.";
		}
	}
	if (	!date2.equals("*") &&
		!date2.equalsIgnoreCase("OutputStart") &&
		!date2.equalsIgnoreCase("OutputEnd") &&
		!date2.equalsIgnoreCase("QueryStart") &&
		!date2.equalsIgnoreCase("QueryEnd") ) {
		try {	enddate = DateTime.parse(date2);
		}
		catch ( Exception e ) {
			warning +=
				"\nEnd date/time \"" + date2 +
				"\" is not a valid date/time.\n"+
				"Specify a date, *, or press Cancel.";
		}
	}
	if ( (startdate != null) && (enddate != null) ) {
		if ( startdate.getPrecision() != enddate.getPrecision() ) {
			warning +=
				"\nStart and end date/time have different" +
				" precision";
		}
		if ( startdate.greaterThan(enddate) ) {
			warning +=
				"\nStart date/time is later than the end " +
				"date/time.";
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
{	__alias_JComboBox = null;
	__cancel_JButton = null;
	__date1_JTextField = null;
	__date2_JTextField = null;
	__command_JTextField = null;
	__command_Vector = null;
	__ok_JButton = null;
	__constant_JTextField = null;
	__minvalue_JTextField = null;
	__maxvalue_JTextField = null;
	super.finalize ();
}

/**
Return the text for the command.
@return the text for the command or null if there is a problem with the
command.
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

        Insets insetsTLBR = new Insets(2,2,2,2);
        Insets insetsMin = insetsTLBR;	//new Insets(0,2,0,2);

	JPanel main_JPanel = new JPanel();
	main_JPanel.setLayout( new GridBagLayout() );
	getContentPane().add ( "North", main_JPanel );
	int y = 0;

        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Replace a range of data values with a constant."),
		0, y, 7, 1, 0, 0, insetsMin, GridBagConstraints.BOTH, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Leave the maximum value blank if not used." ),
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Specify dates with precision appropriate for the data, " +
		"use * for all available data, OutputStart, or OutputEnd." ),
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Time Series to Edit:" ), 
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
	// Always allow a "*" to let all time series be set...
	__alias_JComboBox.add( "*" );
	__alias_JComboBox.addItemListener ( this );
        JGUIUtil.addComponent(main_JPanel, __alias_JComboBox,
		1, y, 6, 1, 1, 0, insetsTLBR, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel("Min. Val. to Replace:"),
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	__minvalue_JTextField = new JTextField ( 10 );
	__minvalue_JTextField.addKeyListener ( this );
        JGUIUtil.addComponent(main_JPanel, __minvalue_JTextField,
		1, y, 2, 1, 1, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST );

        JGUIUtil.addComponent(main_JPanel, new JLabel(
	"Max. Val. to Replace (optional):"),
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	__maxvalue_JTextField = new JTextField ( 10 );
	__maxvalue_JTextField.addKeyListener ( this );
        JGUIUtil.addComponent(main_JPanel, __maxvalue_JTextField,
		1, y, 2, 1, 1, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Constant value to replace with:"), 
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	__constant_JTextField = new JTextField ( 10 );
	__constant_JTextField.addKeyListener ( this );
        JGUIUtil.addComponent(main_JPanel, __constant_JTextField,
		1, y, 2, 1, 1, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);

	JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Analysis Period:" ), 
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	__date1_JTextField = new JTextField ( "*", 15 );
	__date1_JTextField.addKeyListener ( this );
	JGUIUtil.addComponent(main_JPanel, __date1_JTextField,
		1, y, 2, 1, 1, 0, insetsTLBR, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
	JGUIUtil.addComponent(main_JPanel, new JLabel ( "to" ), 
		3, y, 2, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.CENTER);
	__date2_JTextField = new JTextField ( "*", 15 );
	__date2_JTextField.addKeyListener ( this );
	JGUIUtil.addComponent(main_JPanel, __date2_JTextField,
		5, y, 2, 1, 1, 0, insetsTLBR, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel ( "Command:" ), 
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	__command_JTextField = new JTextField ( 30 );
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

	if ( o.equals(__alias_JComboBox) ) {
		refresh();
	}
}

/**
Respond to KeyEvents.
*/
public void keyPressed ( KeyEvent event )
{	int code = event.getKeyCode();

	if ( code == KeyEvent.VK_ENTER ) {
		refresh ();
		// Only return from dialog if specifying the constant...
		if ( event.getComponent().equals(__constant_JTextField) ) {
			checkInput();
			if ( !__error_wait ) {
				response ( 1 );
			}
		}
		// Else in date and need to check it.
	}
}

public void keyReleased ( KeyEvent event )
{	if ( event.getComponent().equals(__constant_JTextField) ) {
		// Only refresh if the text is not just a "-" or ".".
		// Otherwise, we'll get a warning about it not being a number...
		String constant = __constant_JTextField.getText();
		if ( constant == null ) {
			constant = "";
		}
		else {	constant = constant.trim();
		}
		if ( !constant.equals("-") && !constant.equals(".") ) {
			refresh();
		}
	}
	else if ( event.getComponent().equals(__minvalue_JTextField) ) {
		// Only refresh if the text is not just a "-" or ".".
		// Otherwise, we'll get a warning about it not being a number...
		String minvalue = __minvalue_JTextField.getText();
		if ( minvalue == null ) {
			minvalue = "";
		}
		else {	minvalue = minvalue.trim();
		}
		if ( !minvalue.equals("-") && !minvalue.equals(".") ) {
			refresh();
		}
	}
	else if ( event.getComponent().equals(__maxvalue_JTextField) ) {
		// Only refresh if the text is not just a "-" or ".".
		// Otherwise, we'll get a warning about it not being a number...
		String maxvalue = __maxvalue_JTextField.getText();
		if ( maxvalue == null ) {
			maxvalue = "";
		}
		else {	maxvalue = maxvalue.trim();
		}
		if ( !maxvalue.equals("-") && !maxvalue.equals(".") ) {
			refresh();
		}
	}
}

public void keyTyped ( KeyEvent event ) {;}

/**
Refresh the command from the other text field contents.
*/
private void refresh ()
{	String alias = "";
	String constant = "";
	String minvalue = "";
	String maxvalue = "";
	String date1 = "";
	String date2 = "";
	__error_wait = false;
	if ( __first_time ) {
		__first_time = false;
		// Parse the incoming string and fill the fields...
		// Do not skip blanks because the minimum value may be blank...
		Vector v = StringUtil.breakStringList (
			((String)__command_Vector.elementAt(0)).trim(),"(),",0);
		if ( (v != null) && (v.size() == 7) ) {
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
				"replaceValue_JDialog.refresh",
				"Existing command references a " +
				"non-existent\n"+
				"time series \"" + alias + "\".  Select a\n" +
				"different time series or Cancel." );
			}
			// Third field has min value...
			minvalue = ((String)v.elementAt(2)).trim();
			__minvalue_JTextField.setText( minvalue );
			// Fourth field has max value...
			maxvalue = ((String)v.elementAt(3)).trim();
			__maxvalue_JTextField.setText( maxvalue );
			// Fifth field has constant...
			constant = ((String)v.elementAt(4)).trim();
			__constant_JTextField.setText( constant );
			// Sixth field has first date...
			date1 = ((String)v.elementAt(5)).trim();
			__date1_JTextField.setText( date1 );
			// Seventh field has last date...
			date2 = ((String)v.elementAt(6)).trim();
			__date2_JTextField.setText( date2 );
		}
	}
	// Regardless, reset the command from the fields...
	alias = __alias_JComboBox.getSelected();
	constant = __constant_JTextField.getText().trim();
	minvalue = __minvalue_JTextField.getText().trim();
	maxvalue = __maxvalue_JTextField.getText().trim();
	date1 = __date1_JTextField.getText().trim();
	date2 = __date2_JTextField.getText().trim();
	// Don't automatically fill any fields since the next edit may be
	// more complicated...
	__command_JTextField.setText("ReplaceValue("
	+ alias + "," + minvalue + "," + maxvalue + "," +
	constant + "," + date1 + "," + date2 + ")" );
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

} // end replaceValue_JDialog
