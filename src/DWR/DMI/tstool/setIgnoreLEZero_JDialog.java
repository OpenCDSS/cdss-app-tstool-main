// ----------------------------------------------------------------------------
// setIgnoreLEZero_JDialog - editor for setIgnoreLEZero()
// ----------------------------------------------------------------------------
// Copyright:	See the COPYRIGHT file.
// ----------------------------------------------------------------------------
// History: 
//
// 17 Jan 2001	Steven A. Malers, RTi	Initial version (copy and modify
//					setUseDiversionComments_Dialog).
// 2002-03-31	SAM, RTi		Minor cleanup.
// 2003-12-06	SAM, RTi		Update to Swing.
// 2004-02-17	SAM, RTi		Looks like the previous work never got
//					done so do it now.
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

public class setIgnoreLEZero_JDialog extends JDialog
implements ActionListener, ItemListener, KeyListener, WindowListener
{
private final String __TOGGLE_TRUE = "True";
private final String __TOGGLE_FALSE = "False";

private SimpleJButton	__cancel_JButton = null,// Cancel Button
			__ok_JButton = null;	// Ok Button
private Vector		__command_Vector = null;// Command as Vector of String
private JTextField	__command_JTextField = null;
						// Command as JTextField
private SimpleJComboBox	__toggle_JComboBox = null;
						// Field for true/false
private boolean		__error_wait = false;
private boolean		__first_time = true;

/**
setIgnoreLEZero_JDialog constructor.
@param parent JFrame class instantiating this class.
@param command Time series command to parse.
@param tsids Time series identifiers for time series available to convert.
*/
public setIgnoreLEZero_JDialog ( JFrame parent, Vector command, Vector tsids )
{	super(parent, true);
	initialize ( parent, "Edit setIgnoreLEZero() Command", command,
		tsids );
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
}

/**
Free memory for garbage collection.
*/
protected void finalize ()
throws Throwable
{	__cancel_JButton = null;
	__command_JTextField = null;
	__command_Vector = null;
	__ok_JButton = null;
	__toggle_JComboBox = null;
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
@param parent Frame class instantiating this class.
@param title Dialog title.
@param command Vector of String containing the time series command, which
should have a time series identifier and optionally comments.
@param tsids Time series identifiers from
TSEngine.getTSIdentifiersFromCommands().
*/
private void initialize ( JFrame parent, String title, Vector command,
			Vector tsids )
{	__command_Vector = command;

	addWindowListener( this );

        Insets insetsTLBR = new Insets(2,2,2,2);

	// Main panel...

	JPanel main_JPanel = new JPanel();
	main_JPanel.setLayout( new GridBagLayout() );
	getContentPane().add ( "North", main_JPanel );
	int y = 0;

        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Setting the option to True results in values <= 0 being"  +
		" treated as missing" ),
		0, y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"when computing historical averages." ),
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"The default if setIgnoreLEZero() is not used is False, in " +
		"which case all non-missing data are averaged." ),
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"This command should be specified before time series read" +
		" commands because averages" ),
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"are computed immediately after reading time series." ),
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Ignore <= 0 computing averages?:" ), 
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	__toggle_JComboBox = new SimpleJComboBox ( false );
	__toggle_JComboBox.add ( __TOGGLE_TRUE );
	__toggle_JComboBox.add ( __TOGGLE_FALSE );
	__toggle_JComboBox.addItemListener ( this );
        JGUIUtil.addComponent(main_JPanel, __toggle_JComboBox,
		1, y, 1, 1, 1, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);

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
{	refresh();
}

public void keyTyped ( KeyEvent event ) {;}

/**
Refresh the command from the other text field contents.  The command is
of the form:
<pre>
setIgnoreLEZero(true)
</pre>
*/
private void refresh ()
{	String toggle = "";
	if ( __first_time ) {
		__first_time = false;
		__error_wait = false;
		// Parse the incoming string and fill the fields...
		Vector v = StringUtil.breakStringList (
			((String)__command_Vector.elementAt(0)).trim(),"() ,",
			StringUtil.DELIM_SKIP_BLANKS );
		if ( (v != null) && (v.size() == 1) ) {
			String first = (String)v.elementAt(0);
			if ( first.equalsIgnoreCase("-ignorelezero") ) {
				toggle = __TOGGLE_TRUE;
			}
			else {	toggle = __TOGGLE_FALSE;
			}
			__toggle_JComboBox.select ( toggle );
			first = null;
			// Should only call this if the original option was
			// detected as -ignorelezero
		}
		if ( (v != null) && (v.size() == 2) ) {
			// Second field is year type...
			toggle = ((String)v.elementAt(1)).trim();
			if (	JGUIUtil.isSimpleJComboBoxItem(
				__toggle_JComboBox,
				toggle, JGUIUtil.NONE, null, null ) ) {
				__toggle_JComboBox.select ( toggle );
			}
			else {	Message.printWarning ( 1,
				"setIgnoreLEZero_JDialog.refresh",
				"Existing setIgnoreLEZero() uses an " +
				"invalid\nflag value \"" + toggle +
				"\".  Select a different value or Cancel." );
				__error_wait = true;
			}
		}
	}
	// Regardless, reset the command from the fields...
	toggle = __toggle_JComboBox.getSelected();
	if (	JGUIUtil.isSimpleJComboBoxItem( __toggle_JComboBox,
		toggle, JGUIUtil.NONE, null, null ) ) {
		__error_wait = false;
	}
	else {	__error_wait = true;
	}
	__command_JTextField.setText("setIgnoreLEZero(" + toggle +")");
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

} // end setIgnoreLEZero_JDialog
