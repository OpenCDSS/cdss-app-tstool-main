// ----------------------------------------------------------------------------
// setOutputPeriod_JDialog - editor for setOutputPeriod()
// ----------------------------------------------------------------------------
// Copyright:	See the COPYRIGHT file.
// ----------------------------------------------------------------------------
// History: 
//
// 17 Jan 2001	Steven A. Malers, RTi	Initial version (copy
//					setConstantBefore_Dialog and modify).
// 27 Aug 2001	SAM, RTi		Change so that spaces are allowed in
//					dates when parsing.  Expand the
//					comments for higher precision examples.
// 2002-03-29	SAM, RTi		Update so that it is clear that the
//					output is ONLY used for output.  There
//					is now a setQueryPeriod() command for
//					the query period.  Allow * dates to
//					reset back to default.
// 2003-12-01	SAM, RTi		Update to Swing.
// 2004-02-03	SAM, RTi		Update comments to indicate when it is
//					best to use the command.
// 2007-02-26	SAM, RTi		Clean up code based on Eclipse feedback.
// ----------------------------------------------------------------------------

package DWR.DMI.tstool;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import RTi.Util.Message.Message;
import RTi.Util.String.StringUtil;
import RTi.Util.Time.DateTime;

public class setOutputPeriod_JDialog extends JDialog
implements ActionListener, KeyListener, WindowListener
{
private SimpleJButton	__cancel_JButton = null,// Cancel Button
			__ok_JButton = null;	// Ok Button
private Vector		__command_Vector = null; // Command as Vector of String
private JTextField	__date1_JTextField = null,// Dates for period.
			__date2_JTextField = null,// Dates for period.
			__command_JTextField = null;
						// Command as JTextField
private boolean		__error_wait = false;	// Is there an error that we
						// are waiting to be cleared up
						// or Cancel?
private boolean		__first_time = true;

/**
setOutputPeriod_JDialog constructor.
@param parent JFrame class instantiating this class.
@param command Command to parse.
@param tsids Time series identifiers for time series available to modify
(unused).
*/
public setOutputPeriod_JDialog ( JFrame parent, Vector command, Vector tsids )
{	super(parent, true);
	initialize ( parent, "Edit setOutputPeriod() Command", command, tsids );
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
{	String date1_string = __date1_JTextField.getText().trim();
	String date2_string = __date2_JTextField.getText().trim();
	DateTime date1 = null;
	DateTime date2 = null;
	String routine = "setOutputPeriod.checkInput";
	String warning = "";

	// Make sure that the dates can be parsed...
	try {	date1 = DateTime.parse ( date1_string );
	}
	catch ( Exception e ) {
		warning +=
			"\nThe start date/time \"" + date1_string +
			"\" is not a valid date/time.\n"+
			"Specify a date or Cancel.";
	}
	try {	date2 = DateTime.parse ( date2_string );
	}
	catch ( Exception e ) {
		warning +=
			"\nThe end date/time \"" + date2_string +
			"\" is not a valid date/time.\n"+
			"Specify a date or Cancel.";
	}
	if ( (date1 != null) && (date2 != null) ) {
		if ( date1.greaterThan(date2) ) {
			warning +=
			"\nThe start date/time is greater than " +
			"the end date/time.";
		}
		if ( date1.getPrecision() != date2.getPrecision() ) {
			warning +=
			"\nThe precision of the start and end are different.";
		}
	}
	if ( warning.length() > 0 ) {
		__error_wait = true;
		Message.printWarning ( 1, routine, warning );
	}
}

/**
Free memory for garbage collection.
*/
protected void finalize ()
throws Throwable
{	__cancel_JButton = null;
	__date1_JTextField = null;
	__date2_JTextField = null;
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
TSEngine.getTSIdentifiersFromCommands() - ignored.
*/
private void initialize ( JFrame parent, String title, Vector command,
			Vector tsids )
{	__command_Vector = command;

	addWindowListener( this );

        Insets insetsTLBR = new Insets(7,2,7,2);
        Insets insetsMin = new Insets(0,2,0,2);

	// Main panel...

	JPanel main_JPanel = new JPanel();
	main_JPanel.setLayout( new GridBagLayout() );
	getContentPane().add ( "North", main_JPanel );
	int y = 0;

	// Main contents...

        JGUIUtil.addComponent(main_JPanel, new JLabel ( "The output period" +
		" is used ONLY for output products (e.g., files)."),
		0, y, 6, 1, 0, 0, insetsMin, GridBagConstraints.NONE, GridBagConstraints.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"The time series period" +
		" after reading will be extended to the output period if " +
		"necessary."),
		0, ++y, 6, 1, 0, 0, insetsMin, GridBagConstraints.NONE, GridBagConstraints.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Use a setOutputPeriod() "+
		"command to guarantee longer periods if filling data."),
		0, ++y, 6, 1, 0, 0, insetsMin, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Specify the command at the top of commands when filling a"+
		" specific period."),
		0, ++y, 6, 1, 0, 0, insetsMin, GridBagConstraints.NONE, GridBagConstraints.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel ( "Enter dates to a " +
		"precision appropriate for output time series.  For example:"),
		0, ++y, 6, 1, 0, 0, insetsMin, GridBagConstraints.NONE, GridBagConstraints.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"    Year data:   YYYY"),
		0, ++y, 6, 1, 0, 0, insetsMin, GridBagConstraints.NONE, GridBagConstraints.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"    Month data:   MM/YYYY or YYYY-MM"),
		0, ++y, 6, 1, 0, 0, insetsMin, GridBagConstraints.NONE, GridBagConstraints.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"    Day data:     MM/DD/YYYY or YYYY-MM-DD"),
		0, ++y, 6, 1, 0, 0, insetsMin, GridBagConstraints.NONE, GridBagConstraints.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"    Hour data:    MM/DD/YYYY HH or YYYY-MM-DD HH"),
		0, ++y, 6, 1, 0, 0, insetsMin, GridBagConstraints.NONE, GridBagConstraints.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"    Minute data:  MM/DD/YYYY HH:mm or YYYY-MM-DD HH:mm"),
		0, ++y, 6, 1, 0, 0, insetsMin, GridBagConstraints.NONE, GridBagConstraints.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Enter * to output all available data (default if " +
		"setOutputPeriod() is not used)."), 
		0, ++y, 6, 1, 0, 0, insetsMin, GridBagConstraints.NONE, GridBagConstraints.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel ( "Output Period Start:"),
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	__date1_JTextField = new JTextField ( 20 );
	__date1_JTextField.addKeyListener ( this );
        JGUIUtil.addComponent(main_JPanel, __date1_JTextField,
		1, y, 2, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Output Period End:" ), 
		0, ++y, 2, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	__date2_JTextField = new JTextField ( 20 );
	__date2_JTextField.addKeyListener ( this );
        JGUIUtil.addComponent(main_JPanel, __date2_JTextField,
		1, y, 6, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel ( "Command:"),
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
	// Dialogs do not need to be resizable...
	setResizable ( true );
        pack();
        JGUIUtil.center( this );
        super.setVisible( true );
}

/**
Respond to KeyEvents.
*/
public void keyPressed ( KeyEvent event )
{	
}

public void keyReleased ( KeyEvent event )
{	refresh();	
}

public void keyTyped ( KeyEvent event ) {;}

/**
Refresh the command from the other text field contents.
*/
private void refresh ()
{	String date1 = "";
	String date2 = "";
	__error_wait = false;
	if ( __first_time ) {
		__first_time = false;
		// Parse the incoming string and fill the fields...
		Vector v = StringUtil.breakStringList (
			((String)__command_Vector.elementAt(0)).trim(),"(),",
			StringUtil.DELIM_SKIP_BLANKS );
		if ( (v != null) && (v.size() == 3) ) {
			// Second field has start date...
			date1 = ((String)v.elementAt(1)).trim();
			__date1_JTextField.setText( date1 );
			// Third field has date...
			date2 = ((String)v.elementAt(2)).trim();
			__date2_JTextField.setText( date2 );
		}
	}
	// Regardless, reset the command from the fields...
	date1 = __date1_JTextField.getText();
	date2 = __date2_JTextField.getText();
	if ( date1.equals("") ) {
		__error_wait = true;
		return;
	}
	__command_JTextField.setText("setOutputPeriod("+date1+"," + date2+")" );
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

} // end setOutputPeriod_JDialog
