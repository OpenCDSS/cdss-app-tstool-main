// ----------------------------------------------------------------------------
// free_JDialog - editor for free()
// ----------------------------------------------------------------------------
// Copyright:	See the COPYRIGHT file.
// ----------------------------------------------------------------------------
// History: 
//
// 19 Dec 2000	Steven A. Malers, RTi	Initial version.
// 2002-04-23	SAM, RTi		Clean up dialog.
// 2003-12-02	SAM, RTi		Update to Swing.
// 2004-07-11	SAM, RTi		* Add support for wildcards.
//					* Change so dialog shows parameters -
//					  transition to new syntax.
// 2004-08-03	SAM, RTi		Fix typo in comments.
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

public class free_JDialog extends JDialog
implements ActionListener, ItemListener, KeyListener, WindowListener
{
private SimpleJButton	__cancel_JButton = null,// Cancel Button
			__ok_JButton = null;	// Ok Button
private Vector		__command_Vector = null; // Command as Vector of String
private JTextField	__command_JTextField=null;// Command as TextField
private SimpleJComboBox	__TSID_JComboBox = null; // Field for time series
						// identifier or alias
private boolean		__first_time = true;
private boolean		__error_wait = false;

/**
free_JDialog constructor.
@param parent JFrame class instantiating this class.
@param command Ccommand to parse.
@param tsids Time series identifiers for time series available to free.
*/
public free_JDialog ( JFrame parent, Vector command, Vector tsids )
{	super(parent, true);
	initialize ( parent, "Edit free() Command", command, tsids );
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
{	//TODO SAM
	//String TSID = __TSID_JComboBox.getSelected();
	String warning = "";

	if ( warning.length() > 0 ) {
		__error_wait = true;
		Message.printWarning ( 1, "free_JDialog", warning );
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
		"Free time series.  This is useful because some commands "+
		"operate on all available commands,"),
		0, y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"and therefore inappropriate time series may need to be " +
		"removed."),
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Select the time series to free from the list or enter " +
		"a time series identifier or alias."),
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Identifiers follow the pattern:"),
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"  Location.Source.DataType.Interval.Scenario"),
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Examples of wildcard use are shown below:"),
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"* - matches all time series"),
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"ABC* - matches locations starting with ABC"),
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"ABC*.*.TYPE.MONTH - matches locations starting with ABC, with"+
		" data type TYPE and interval MONTH."),
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"See also the selectTimeSeries() and deselectTimeSeries() " +
		"commands."),
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel ( "Time series to free:"),
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	// Allow edits so that wildcards can be entered.
	__TSID_JComboBox = new SimpleJComboBox ( true );
	__TSID_JComboBox.setData ( tsids );
	// Always allow a "*" to let all time series be freed...
	__TSID_JComboBox.add( "*" );
	__TSID_JComboBox.addItemListener ( this );
	__TSID_JComboBox.addKeyListener ( this );
        JGUIUtil.addComponent(main_JPanel, __TSID_JComboBox,
		1, y, 6, 1, 1, 0, insetsTLBR, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);

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
	// Dialogs do not need to be resizable...
	setResizable ( true );
        pack();
        JGUIUtil.center( this );
        super.setVisible( true );
}

/**
Handle ItemEvent events.
@param event ItemEvent to handle.
*/
public void itemStateChanged ( ItemEvent event )
{	Object o = event.getItemSelectable();

	if ( o.equals(__TSID_JComboBox) ) {
		if ( event.getStateChange() != ItemEvent.SELECTED ) {
			return;
		}
		refresh();
	}
}

/**
Respond to KeyEvents.
*/
public void keyPressed ( KeyEvent event )
{	int code = event.getKeyCode();

	refresh ();
	if ( code == KeyEvent.VK_ENTER ) {
		checkInput();
		if ( !__error_wait ) {
			response ( 1 );
		}
	}
}

public void keyReleased ( KeyEvent event )
{	// Nothing to do	
}

public void keyTyped ( KeyEvent event )
{
}

/**
Refresh the command from the other text field contents:
<pre>
free(TSID="X")
</pre>
*/
private void refresh ()
{	String TSID = "";
	String routine = "free_JDialog.refresh";
	String command = ((String)__command_Vector.elementAt(0)).trim();
	if ( __first_time ) {
		__first_time = false;
		// Parse the incoming string and fill the fields...
		if ( command.indexOf('=') < 0 ) {
			Vector v = StringUtil.breakStringList (
				command,"() ,", StringUtil.DELIM_SKIP_BLANKS );
			if ( (v != null) && (v.size() == 2) ) {
				// Second field is identifier...
				TSID = ((String)v.elementAt(1)).trim();
			}
		}
		else {	// New style...
			// Parse the incoming string and fill the fields...
			Vector v = StringUtil.breakStringList (
				command,"()", StringUtil.DELIM_SKIP_BLANKS );
			PropList props = null;
			if ( (v != null) && (v.size() > 1) ) {
				props = PropList.parse (
					(String)v.elementAt(1), routine, "," );
			}
			else {	props = new PropList ( "selectTimeSeries" );
			}
			TSID = props.getValue ( "TSID" );
		}
		if ( __TSID_JComboBox != null ) {
		if ( TSID == null ) {
			// Select default...
			__TSID_JComboBox.select ( 0 );
		}
		else {	if (	JGUIUtil.isSimpleJComboBoxItem(
				__TSID_JComboBox, TSID,
				JGUIUtil.NONE, null, null) ) {
				__TSID_JComboBox.select ( TSID );
			}
			else {	Message.printWarning ( 2, routine,
				"Existing free() references an unrecognized\n" +
				"TSID value \"" + TSID + "\".\nAllowing the " +
				" value because it contains wildcards or " +
				"may be the result from a bulk read.");
				__TSID_JComboBox.setText ( TSID );
				/* REVISIT SAM 2004-07-11 - see how the above
				works
				else {	// print a warning...
					Message.printWarning ( 1, routine,
					"Existing free() references a non-existent\n"+
					"time series \"" + TSID + "\".  Select a\n" +
					"different time series or Cancel." );
				}
				*/
			}
		}
		}
	}
	// Regardless, reset the command from the fields...
	if ( __TSID_JComboBox != null ) {
		TSID = __TSID_JComboBox.getSelected();
	}
	StringBuffer b = new StringBuffer ();
	if ( TSID.length() > 0 ) {
		b.append ( "TSID=\"" + TSID + "\"" );
	}
	__command_JTextField.setText("free(" + b.toString() + ")" );
	__command_Vector.removeAllElements();
	__command_Vector.addElement ( __command_JTextField.getText() );
}

/**
Return the time series command as a Vector of String.
@param status 0 to cancel, 1 is OK.
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

} // end free_JDialog
