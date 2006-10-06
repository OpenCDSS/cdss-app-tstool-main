// ----------------------------------------------------------------------------
// fillRepeat_JDialog - editor for fillRepeat()
// ----------------------------------------------------------------------------
// Copyright:	See the COPYRIGHT file.
// ----------------------------------------------------------------------------
// History: 
//
// 2002-04-21	Steven A. Malers, RTi	Initial version (copy and modify
//					fillUsingDiversionComments_JDialog).
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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.util.Vector;

import RTi.Util.GUI.JGUIUtil;
import RTi.Util.GUI.SimpleJComboBox;
import RTi.Util.GUI.SimpleJButton;
import RTi.Util.IO.PropList;
import RTi.Util.Message.Message;
import RTi.Util.String.StringUtil;
import RTi.Util.Time.DateTime;

public class fillRepeat_JDialog extends JDialog
implements ActionListener, ItemListener, KeyListener, WindowListener
{
private String __FORWARD = "Forward";
private String __BACKWARD = "Backward";

private SimpleJButton	__cancel_JButton = null,// Cancel Button
			__ok_JButton = null;	// Ok Button
private JFrame		__parent_JFrame = null;	// parent JFrame
private Vector		__command_Vector = null;// Command as Vector of String
private JTextField	__command_JTextField=null;// Command as JTextField
private JTextField	__MaxIntervals_JTextField=null;
						// Max intervals to fill
private SimpleJComboBox	__TSID_JComboBox = null;// Field for time series alias
private SimpleJComboBox	__FillDirection_JComboBox = null;
						// Field for fill direction
private JTextField	__FillStart_JTextField =null;
						// Field for fill start
private JTextField	__FillEnd_JTextField =null;
						// Field for fill end
private boolean		__error_wait = false;	// Is there an error that we
						// are waiting to be cleared up
						// or Cancel?
private boolean		__first_time = true;

/**
fillRepeat_JDialog constructor.
@param parent JFrame class instantiating this class.
@param command Command to parse.
@param tsids Time series identifiers for time series available to fill.
*/
public fillRepeat_JDialog ( JFrame parent, Vector command, Vector tsids )
{	super(parent, true);
	initialize ( parent, "Edit fillRepeat() Command", command, tsids );
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
{	String TSID = __TSID_JComboBox.getSelected();
	String FillStart = __FillStart_JTextField.getText().trim();
	String FillEnd = __FillEnd_JTextField.getText().trim();
	String MaxIntervals = __MaxIntervals_JTextField.getText().trim();
	String warning = "";
	DateTime fillstart = null, fillend = null;
	__error_wait = false;

	try {	if ( FillStart.length() > 0 ) {
			fillstart = DateTime.parse ( FillStart );
		}
	}
	catch ( Exception e ) {
		warning +=
			"\nThe fill start date/time \"" + FillStart +
			"\" is not a valid date/time.  " +
			"Specify a date/time or Cancel.";
	}
	try {	if ( FillEnd.length() > 0 ) {
			fillend = DateTime.parse ( FillEnd );
		}
	}
	catch ( Exception e ) {
		warning +=
			"\nThe fill end date/time \"" + FillEnd +
			"\" is not a valid date/time.  "+
			"Specify a date/time or Cancel.";
	}
	if (	(MaxIntervals.length() > 0) &&
		!StringUtil.isInteger(MaxIntervals) ) {
		warning +=
			"\nThe maximum intervals \"" + MaxIntervals +
			"\" is not a number.  Correct or Cancel.";
	}
	if ( warning.length() > 0 ) {
		__error_wait = true;
		Message.printWarning ( 1, "fillRepeat_JDialog", warning );
	}
}

/**
Free memory for garbage collection.
*/
protected void finalize ()
throws Throwable
{	__TSID_JComboBox = null;
	__FillDirection_JComboBox = null;
	__FillStart_JTextField = null;
	__FillEnd_JTextField = null;
	__MaxIntervals_JTextField = null;
	__cancel_JButton = null;
	__command_JTextField = null;
	__command_Vector = null;
	__ok_JButton = null;
	__parent_JFrame = null;
	super.finalize ();
}

/**
Return the text for the command.
@return the text for the command or null if there is a problem with the
command.
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
@param tsids Time series identifiers.
*/
private void initialize ( JFrame parent, String title, Vector command,
			Vector tsids )
{	__parent_JFrame = parent;
	__command_Vector = command;

	addWindowListener( this );

        Insets insetsTLBR = new Insets(2,2,2,2);

	JPanel main_JPanel = new JPanel();
	main_JPanel.setLayout( new GridBagLayout() );
	GridBagConstraints gbc = new GridBagConstraints();
	getContentPane().add ( "North", main_JPanel );
	int y = 0;

        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"This command fills missing data in the time series by" +
		" repeating non-missing values." ), 
		0, y, 7, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"The fill direction can be forward or backward."),
		0, ++y, 7, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Specify the maximum intervals as blank or zero to fill " +
		"any gap."),
		0, ++y, 7, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"The fill start and end, if specified, will limit the period" +
		" that is filled." ), 
		0, ++y, 7, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Use standard date/time formats appropriate for the date/time "+
		"precision of the time series."),
		0, ++y, 7, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Time series to fill:" ), 
		0, ++y, 1, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.EAST);
	__TSID_JComboBox = new SimpleJComboBox ( false );
	int size = 0;
	if ( tsids != null ) {
		size = tsids.size();
	}
	if ( size == 0 ) {
		Message.printWarning ( 1,
		"fillRepeat_JDialog.initialize",
		"You must define time series before inserting a " +
		"fillInterpolate() command." );
		response ( 0 );
	}
	__TSID_JComboBox.setData ( tsids );
	// Always allow a "*" to let all time series be filled...
	__TSID_JComboBox.add ( "*" );
	__TSID_JComboBox.addItemListener ( this );
        JGUIUtil.addComponent(main_JPanel, __TSID_JComboBox,
		1, y, 6, 1, 1, 0, insetsTLBR, gbc.HORIZONTAL, gbc.WEST);

        JGUIUtil.addComponent(main_JPanel,new JLabel(
		"Fill start date/time:"),
		0, ++y, 1, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.EAST);
	__FillStart_JTextField = new JTextField ( "", 10 );
	__FillStart_JTextField.addKeyListener ( this );
        JGUIUtil.addComponent(main_JPanel, __FillStart_JTextField,
		1, y, 2, 1, 1, 0, insetsTLBR, gbc.NONE, gbc.WEST);

        JGUIUtil.addComponent(main_JPanel,new JLabel(
		"Fill end date/time:"),
		0, ++y, 1, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.EAST);
	__FillEnd_JTextField = new JTextField ( "", 10 );
	__FillEnd_JTextField.addKeyListener ( this );
        JGUIUtil.addComponent(main_JPanel, __FillEnd_JTextField,
		1, y, 2, 1, 1, 0, insetsTLBR, gbc.NONE, gbc.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Fill direction:"), 
		0, ++y, 1, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.EAST);
	__FillDirection_JComboBox = new SimpleJComboBox ( false );
	__FillDirection_JComboBox.addItem ( __BACKWARD );
	__FillDirection_JComboBox.addItem ( __FORWARD );
	__FillDirection_JComboBox.select ( __FORWARD );
	__FillDirection_JComboBox.addItemListener ( this );
        JGUIUtil.addComponent(main_JPanel, __FillDirection_JComboBox,
		1, y, 2, 1, 1, 0, insetsTLBR, gbc.NONE, gbc.WEST);

        JGUIUtil.addComponent(main_JPanel,new JLabel(
		"Max. intervals:"),
		0, ++y, 1, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.EAST);
	__MaxIntervals_JTextField = new JTextField ( "", 10 );
	__MaxIntervals_JTextField.addKeyListener ( this );
        JGUIUtil.addComponent(main_JPanel, __MaxIntervals_JTextField,
		1, y, 2, 1, 1, 0, insetsTLBR, gbc.NONE, gbc.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel ( "Command:" ), 
		0, ++y, 1, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.EAST);
	__command_JTextField = new JTextField ( 55 );
	__command_JTextField.setEditable ( false );
	JGUIUtil.addComponent(main_JPanel, __command_JTextField,
		1, y, 6, 1, 1, 0, insetsTLBR, gbc.HORIZONTAL, gbc.WEST);

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

	if ( code == event.VK_ENTER ) {
		refresh ();
		checkInput();
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
fillRepeat(TSID=xxx,FillStart=xxx,FillEnd=xxx,FillDirection=xxx,MaxIntervals=xx)
</pre>
*/
private void refresh()
{	String TSID = "";
	String FillStart = "";
	String FillEnd = "";
	String FillDirection = "";
	String MaxIntervals = "";
	if ( __first_time ) {
		__first_time = false;
		__error_wait = false;
		// Parse the incoming string and fill the fields...
		Vector v = StringUtil.breakStringList (
			((String)__command_Vector.elementAt(0)).trim(),"()",
			StringUtil.DELIM_SKIP_BLANKS );
		PropList props = null;
		if ( (v != null) && (v.size() > 1) ) {
			props = PropList.parse (
				(String)v.elementAt(1),
				"fillRepeat", "," );
		}
		else {	props = new PropList ( "fillRepeat" );
		}
		TSID = props.getValue ( "TSID" );
		FillStart = props.getValue ( "FillStart" );
		FillEnd = props.getValue ( "FillEnd" );
		FillDirection = props.getValue ( "FillDirection" );
		MaxIntervals = props.getValue ( "MaxIntervals" );
		// Now select the information...
		if ( TSID == null ) {
			// Select all...
			__TSID_JComboBox.select ( "*" );
		}
		else {	if (	JGUIUtil.isSimpleJComboBoxItem(__TSID_JComboBox,
				TSID, JGUIUtil.NONE, null, null ) ) {
				__TSID_JComboBox.select ( TSID );
			}
			else {	Message.printWarning ( 1,
				"fillRepeat_JDialog.refresh",
				"Existing fillRepeat() " +
				"references an invalid\nTSID\"" + TSID +
				"\".  Select a different time " +
				"series or Cancel.");
				__error_wait = true;
			}
		}
		if ( FillStart != null ) {
			__FillStart_JTextField.setText ( FillStart );
		}
		if ( FillEnd != null ) {
			__FillEnd_JTextField.setText ( FillEnd );
		}
		if ( FillDirection == null ) {
			// Select default...
			__FillDirection_JComboBox.select ( __FORWARD );
		}
		else {	if (	JGUIUtil.isSimpleJComboBoxItem(
				__FillDirection_JComboBox,
				FillDirection, JGUIUtil.NONE, null, null ) ) {
				__FillDirection_JComboBox.select(FillDirection);
			}
			else {	Message.printWarning ( 1,
				"fillRepeat_JDialog.refresh",
				"Existing fillRepeat() " +
				"references an invalid\nrun mode \"" +
				FillDirection +
				"\".  Select a different run mode or Cancel.");
				__error_wait = true;
			}
		}
		if ( MaxIntervals != null ) {
			__MaxIntervals_JTextField.setText ( MaxIntervals );
		}
	}
	// Regardless, reset the command from the fields...
	TSID = __TSID_JComboBox.getSelected();
	FillStart = __FillStart_JTextField.getText().trim();
	FillEnd = __FillEnd_JTextField.getText().trim();
	FillDirection = __FillDirection_JComboBox.getSelected();
	MaxIntervals = __MaxIntervals_JTextField.getText().trim();
	StringBuffer b = new StringBuffer ();
	if ( TSID.length() > 0 ) {
		b.append ( "TSID=\"" + TSID + "\"" );
	}
	if ( FillStart.length() > 0 ) {
		if ( b.length() > 0 ) {
			b.append ( "," );
		}
		b.append ( "FillStart=\"" + FillStart + "\"" );
	}
	if ( FillEnd.length() > 0 ) {
		if ( b.length() > 0 ) {
			b.append ( "," );
		}
		b.append ( "FillEnd=\"" + FillEnd + "\"" );
	}
	if ( FillDirection.length() > 0 ) {
		if ( b.length() > 0 ) {
			b.append ( "," );
		}
		b.append ( "FillDirection=" + FillDirection );
	}
	if ( MaxIntervals.length() > 0 ) {
		if ( b.length() > 0 ) {
			b.append ( "," );
		}
		b.append ( "MaxIntervals=" + MaxIntervals );
	}
	__command_JTextField.setText("fillRepeat(" +
		b.toString() + ")" );
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

} // end fillRepeat_JDialog
