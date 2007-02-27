// ----------------------------------------------------------------------------
// adjustExtremes_JDialog - editor for adjustExtremes()
// ----------------------------------------------------------------------------
// Copyright:	See the COPYRIGHT file.
// ----------------------------------------------------------------------------
// History: 
//
// 2002-05-27	Steven A. Malers, RTi	Initial version.  Copy and modify
//					scale().
// 2003-12-15	SAM, RTi		Update to Swing.
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

public class adjustExtremes_JDialog extends JDialog
implements ActionListener, ItemListener, KeyListener, WindowListener
{
private SimpleJButton	__cancel_JButton = null,// Cancel Button
			__ok_JButton = null;	// Ok Button
private Vector		__command_Vector = null; // Command as Vector of String
private JTextField	__command_JTextField=null;// Command as TextField
private SimpleJComboBox	__alias_JComboBox = null; // Field for time series alias
private SimpleJComboBox	__method_JComboBox = null;// Method to adjust.
private SimpleJComboBox	__extreme_JComboBox = null;// Whether max or min values
						// are being adjusted
private JTextField	__extreme_JTextField=null;// Field for extreme value
						// cutoff.
private JTextField	__max_intervals_JTextField = null;
						// Field for max intervals
private JTextField	__analysis_period_start_JTextField = null;
private JTextField	__analysis_period_end_JTextField = null;
						// Fields for analysis period
private boolean		__error_wait = false;	// Is there an error that we
						// are waiting to be cleared up
						// or Cancel?
private boolean		__first_time = true;

/**
adjustExtremes_JDialog constructor.
@param parent JFrame class instantiating this class.
@param command Command to parse.
@param tsids Time series identifiers for time series available for command.
*/
public adjustExtremes_JDialog ( JFrame parent, Vector command, Vector tsids )
{	super(parent, true);
	initialize ( parent, "Edit adjustExtremes() Command", command, tsids );
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
{	// Check the data...
	String extreme_value = __extreme_JTextField.getText().trim();
	String max_intervals = __max_intervals_JTextField.getText().trim();
	String analysis_period_start =
		__analysis_period_start_JTextField.getText();
	String analysis_period_end = __analysis_period_end_JTextField.getText();
	String warning = "";
	if ( !StringUtil.isDouble(extreme_value) ) {
		warning +=
			"\nExtreme value \"" + extreme_value +
			"\" is not a number.\n"+
			"Specify a number or Cancel.";
	}
	if ( !StringUtil.isInteger(max_intervals) ) {
		warning +=
			"\nMaximum intervals \"" + max_intervals +
			"\" is not a number.\n"+
			"Specify a number or Cancel.";
	}
	if (	!analysis_period_start.equals("*") &&
		!analysis_period_start.equalsIgnoreCase("OutputStart") ) {
		try {	DateTime.parse(analysis_period_start);
		}
		catch ( Exception e ) {
			warning += 
				"\nThe start date/time \"" +
				analysis_period_start +
				"\" is not a valid date/time.\n"+
				"Specify a date, *, OutputStart, or Cancel.";
		}
	}
	if (	!analysis_period_end.equals("*") &&
		!analysis_period_end.equalsIgnoreCase("OutputEnd") ) {
		try {	DateTime.parse( analysis_period_end);
		}
		catch ( Exception e ) {
			warning +=
				"\nThe end date/time \"" + analysis_period_end +
				"\" is not a valid date/time.\n"+
				"Specify a date, *, OutputEnd, or Cancel.";
		}
	}
	if ( warning.length() > 0 ) {
		__error_wait = true;
		Message.printWarning ( 1, "scale_JDialog", warning );
	}
}

/**
Free memory for garbage collection.
*/
protected void finalize ()
throws Throwable
{	__alias_JComboBox = null;
	__method_JComboBox = null;
	__extreme_JComboBox = null;
	__cancel_JButton = null;
	__command_JTextField = null;
	__command_Vector = null;
	__ok_JButton = null;
	__extreme_JTextField = null;
	__max_intervals_JTextField = null;
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
{	__command_Vector = command;

	addWindowListener( this );

        Insets insetsTLBR = new Insets(2,2,2,2);

	JPanel main_JPanel = new JPanel();
	main_JPanel.setLayout( new GridBagLayout() );
	getContentPane().add ( "North", main_JPanel );
	int y = 0;

        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Adjust the extreme values by considering values to each side" +
		" of extreme values."),
		0, y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"If the Extreme to Adjust is" +
		" AdjustMinimum, values < Extreme Value are adjusted." ),
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"If the Extreme to Adjust is" +
		" AdjustMaximum, values > Extreme Value are adjusted." ),
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"The Average Adjust Method replaces the extreme and values on" +
		" each side of the extreme"),
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"by the average of all values, preserving the total." ),
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
/* SAX
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"The WeightedAverage Adjust Method replaces the extreme and" +
		" values on each side of the extreme"),
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"by the average of all values, preserving the total." ),
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
*/
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"The Maximum Intervals field indicates how many intervals on" +
		" each side of the extreme can be modified."),
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Specify dates with precision appropriate for the data, " +
		"use * for all available data, OutputStart, or OutputEnd." ),
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel ("Time Series to Adjust:"),
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	__alias_JComboBox = new SimpleJComboBox ( false );
	int size = 0;
	if ( tsids != null ) {
		size = tsids.size();
	}
	if ( size == 0 ) {
		Message.printWarning ( 1, "adjustExtremes_JDialog.initialize",
		"You must define time series before inserting an " +
		"adjustExtremes() command." );
		response ( 0 );
	}
	__alias_JComboBox.setData ( tsids );
	// Always allow a "*" to let all time series be filled...
	__alias_JComboBox.addItem ( "*" );
	__alias_JComboBox.addItemListener ( this );
        JGUIUtil.addComponent(main_JPanel, __alias_JComboBox,
		1, y, 6, 1, 1, 0, insetsTLBR, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel ( "Adjust Method:" ),
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	__method_JComboBox = new SimpleJComboBox ( false );
	__method_JComboBox.addItem ( "Average" );
	//__method_JComboBox.addItem ( "WeightedAverage" );
	__method_JComboBox.select ( "Average" );
        JGUIUtil.addComponent(main_JPanel, __method_JComboBox,
		1, y, 2, 1, 1, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel ( "Extreme to Adjust:" ),
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	__extreme_JComboBox = new SimpleJComboBox ( false );
	__extreme_JComboBox.addItem ( "AdjustMinimum" );
	__extreme_JComboBox.addItem ( "AdjustMaximum" );
	__extreme_JComboBox.select ( "AdjustMinimum" );
        JGUIUtil.addComponent(main_JPanel, __extreme_JComboBox,
		1, y, 2, 1, 1, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel ( "Extreme Value:" ),
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	__extreme_JTextField = new JTextField ( "0", 10 );
	__extreme_JTextField.addKeyListener ( this );
        JGUIUtil.addComponent(main_JPanel, __extreme_JTextField,
		1, y, 2, 1, 1, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel ( "Maximum Intervals:" ),
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	__max_intervals_JTextField = new JTextField ( "0", 10 );
	__max_intervals_JTextField.addKeyListener ( this );
        JGUIUtil.addComponent(main_JPanel, __max_intervals_JTextField,
		1, y, 2, 1, 1, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);

	JGUIUtil.addComponent(main_JPanel, new JLabel ( "Analysis Period:" ), 
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	__analysis_period_start_JTextField = new JTextField ( "*", 15 );
	__analysis_period_start_JTextField.addKeyListener ( this );
	JGUIUtil.addComponent(main_JPanel, __analysis_period_start_JTextField,
		1, y, 2, 1, 1, 0, insetsTLBR, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
	JGUIUtil.addComponent(main_JPanel, new JLabel ( "to" ), 
		3, y, 2, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.CENTER);
	__analysis_period_end_JTextField = new JTextField ( "*", 15 );
	__analysis_period_end_JTextField.addKeyListener ( this );
	JGUIUtil.addComponent(main_JPanel, __analysis_period_end_JTextField,
		5, y, 2, 1, 1, 0, insetsTLBR, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel ( "Command:" ),
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	__command_JTextField = new JTextField ( 55 );
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
{	refresh();
}

/**
Respond to KeyEvents.
*/
public void keyPressed ( KeyEvent event )
{	int code = event.getKeyCode();

	if ( code == KeyEvent.VK_ENTER ) {
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
	String extreme_value = __extreme_JTextField.getText();
	if ( extreme_value == null ) {
		extreme_value = "";
	}
	else {	extreme_value = extreme_value.trim();
	}
	if ( !extreme_value.equals("-") && !extreme_value.equals(".") ) {
		refresh();
	}
}

public void keyTyped ( KeyEvent event )
{
}

/**
Refresh the command from the other text field contents.
*/
private void refresh ()
{	String alias = "";
	String method = "";
	String extreme_flag = "";
	String extreme_value = "";
	String max_intervals = "";
	String analysis_period_start = "";
	String analysis_period_end = "";
	__error_wait = false;
	if ( __first_time ) {
		__first_time = false;
		// Parse the incoming string and fill the fields...
		Vector v = StringUtil.breakStringList (
			((String)__command_Vector.elementAt(0)).trim(),"(),",
			StringUtil.DELIM_SKIP_BLANKS );
		if ( (v != null) && (v.size() >= 8) ) {
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
				"adjustExtremes_JDialog.refresh",
				"Existing adjustExtremes() references a " +
				"non-existent\n"+
				"time series \"" + alias + "\".  Select a\n" +
				"different time series or Cancel." );
			}
			// Third field has method...
			method = ((String)v.elementAt(2)).trim();
			if (	JGUIUtil.isSimpleJComboBoxItem(
				__method_JComboBox, method,
				JGUIUtil.NONE, null, null ) ) {
				__method_JComboBox.select ( method );
			}
			else {	Message.printWarning ( 1,
				"adjustExtremes_JDialog.refresh",
				"Existing adjustExtremes() uses an " +
				"unrecognized\n"+
				"adjust method \"" + method + "\".  Select a\n"+
				"recognized method." );
			}
			// Fourth field has extreme to adjust...
			extreme_flag = ((String)v.elementAt(3)).trim();
			if (	JGUIUtil.isSimpleJComboBoxItem(
				__extreme_JComboBox,
				extreme_flag, JGUIUtil.NONE, null, null ) ) {
				__extreme_JComboBox.select ( method );
			}
			else {	Message.printWarning ( 1,
				"adjustExtremes_JDialog.refresh",
				"Existing adjustExtremes() uses an " +
				"unrecognized\n"+
				"extreme to adjust \"" + extreme_flag +
				"\".  Select a recognized extreme to adjust." );
			}
			// Fifth field has extreme value...
			extreme_value = ((String)v.elementAt(4)).trim();
			__extreme_JTextField.setText( extreme_value );
			// Sixth field has maximum intervals...
			max_intervals = ((String)v.elementAt(5)).trim();
			__max_intervals_JTextField.setText( max_intervals );
			// Seventh and eighth fields have analysis period...
			analysis_period_start = ((String)v.elementAt(6)).trim();
			__analysis_period_start_JTextField.setText(
				analysis_period_start );
			analysis_period_end = ((String)v.elementAt(7)).trim();
			__analysis_period_end_JTextField.setText(
				analysis_period_end );
		}
		else {	// First time and new command that is blank.
			__analysis_period_start_JTextField.setText("*");
			__analysis_period_end_JTextField.setText("*");
		}
	}
	// Regardless, reset the command from the fields...
	alias = __alias_JComboBox.getSelected();
	method = __method_JComboBox.getSelected();
	extreme_flag = __extreme_JComboBox.getSelected();
	extreme_value = __extreme_JTextField.getText().trim();
	max_intervals = __max_intervals_JTextField.getText().trim();
	analysis_period_start=
		__analysis_period_start_JTextField.getText().trim();
	analysis_period_end = __analysis_period_end_JTextField.getText().trim();
	if ( (alias == null) || (alias.trim().length() == 0) ) {
		return;
	}
	if ( (extreme_value == null) || (extreme_value.trim().length() == 0) ) {
		return;
	}
	__command_JTextField.setText("adjustExtremes(" + alias + "," + method +
		"," + extreme_flag + "," + extreme_value + "," +
		max_intervals + "," +
		analysis_period_start + "," + analysis_period_end + ")" );
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

public void windowActivated( WindowEvent evt )
{
}

public void windowClosed( WindowEvent evt )
{
}

public void windowDeactivated( WindowEvent evt )
{
}

public void windowDeiconified( WindowEvent evt )
{
}

public void windowIconified( WindowEvent evt )
{
}

public void windowOpened( WindowEvent evt )
{
}

} // end adjustExtremes_JDialog
