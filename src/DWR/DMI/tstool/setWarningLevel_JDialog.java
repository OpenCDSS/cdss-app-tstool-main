// ----------------------------------------------------------------------------
// setWarningLevel_JDialog - editor for setWarningLevel()
// ----------------------------------------------------------------------------
// Copyright:	See the COPYRIGHT file.
// ----------------------------------------------------------------------------
// History: 
//
// 27 Mar 2001	Steven A. Malers, RTi	Initial version (copy and modify
//					setDebugLevel_Dialog).
// 2002-04-08	SAM, RTi		Clean up interface and add comments.
// 2003-12-06	SAM, RTi		Update to Swing.
// ----------------------------------------------------------------------------

package DWR.DMI.tstool;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import RTi.Util.Message.Message;
import RTi.Util.String.StringUtil;

public class setWarningLevel_JDialog extends JDialog
implements ActionListener, KeyListener, WindowListener
{
private SimpleJButton	__cancel_JButton = null,// Cancel Button
			__ok_JButton = null;	// Ok Button
private JFrame		__parent_JFrame = null;	// parent JFrame class
private Vector		__command_Vector = null; // Command as Vector of String
private JTextField	__command_JTextField=null;// Command as JTextField
private JTextField	__log_JTextField = null,// Fields for warning levels
			__screen_JTextField;
private boolean		__error_wait = false;	// Is there an error that we
						// are waiting to be cleared up
						// or Cancel?
private boolean		__first_time = true;

/**
setWarningLevel_JDialog constructor.
@param parent JFrame class instantiating this class.
@param command Command to parse.
@param tsids Time series identifiers for time series available. - ignored
*/
public setWarningLevel_JDialog ( JFrame parent, Vector command, Vector tsids )
{ 	super(parent, true);
	initialize ( parent, "Edit setWarningLevel() Command", command, tsids );
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
{	String screen = __screen_JTextField.getText().trim();
	String log = __log_JTextField.getText().trim();
	String routine = "setWarningLevel.checkInput";
	String warning = "";

	if ( !StringUtil.isInteger(screen) ) {
		warning +=
			"\nThe screen message level \"" + screen +
			"\" is not an integer.\n"+
			"Specify an integer or Cancel.";
	}
	if ( !StringUtil.isInteger(log) ) {
		warning +=
			"\nThe log file message level \"" + log +
			"\" is not an integer.\n"+
			"Specify an integer or Cancel.";
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
	__command_JTextField = null;
	__command_Vector = null;
	__log_JTextField = null;
	__screen_JTextField = null;
	__ok_JButton = null;
	__parent_JFrame = null;
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
@param tsids Time series identifiers - ignored.
*/
private void initialize ( JFrame parent, String title, Vector command,
			Vector tsids )
{	__parent_JFrame = parent;
	__command_Vector = command;

	addWindowListener( this );

        Insets insetsNONE = new Insets(1,1,1,1);
        Insets insetsTLBR = new Insets(2,2,2,2);

	// Main panel...

	JPanel main_JPanel = new JPanel();
	main_JPanel.setLayout( new GridBagLayout() );
	GridBagConstraints gbc = new GridBagConstraints();
	getContentPane().add ( "North", main_JPanel );
	int y = 0;

        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Setting the warning level to a higher number prints more" +
		" warning information."),
		0, y, 7, 1, 0, 0, insetsNONE, gbc.NONE, gbc.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Warning information is used for troubleshooting." ),
		0, ++y, 7, 1, 0, 0, insetsNONE, gbc.NONE, gbc.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Messages can be printed to the screen and/or log file." ), 
		0, ++y, 7, 1, 0, 0, insetsNONE, gbc.NONE, gbc.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Warning levels can be increased before and decreased after" ), 
		0, ++y, 7, 1, 0, 0, insetsNONE, gbc.NONE, gbc.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"specific commands to debug the commands." ), 
		0, ++y, 7, 1, 0, 0, insetsNONE, gbc.NONE, gbc.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel("Screen warning level:"), 
		0, ++y, 1, 1, 0, 0, insetsNONE, gbc.NONE, gbc.EAST);
	__screen_JTextField = new JTextField ( 4 );
	__screen_JTextField.setText( "1");
	__screen_JTextField.addKeyListener ( this );
        JGUIUtil.addComponent(main_JPanel, __screen_JTextField,
		1, y, 1, 1, 1, 0, insetsNONE, gbc.NONE, gbc.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Log file warning level:" ), 
		0, ++y, 1, 1, 0, 0, insetsNONE, gbc.NONE, gbc.EAST);
	__log_JTextField = new JTextField ( 30 );
	__log_JTextField.addKeyListener ( this );
        JGUIUtil.addComponent(main_JPanel, __log_JTextField,
		1, y, 1, 1, 1, 0, insetsNONE, gbc.NONE, gbc.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel ( "Command:"),
		0, ++y, 1, 1, 0, 0, insetsNONE, gbc.NONE, gbc.WEST);
	__command_JTextField = new JTextField ( 30 );
	__command_JTextField.setEditable ( false );
	JGUIUtil.addComponent(main_JPanel, __command_JTextField,
		1, y, 6, 1, 1, 0, insetsNONE, gbc.HORIZONTAL, gbc.CENTER);

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
Refresh the command from the other text field contents.
*/
private void refresh ()
{	String screen = "1";
	String log = "2";
	__error_wait = false;
	if ( __first_time ) {
		__first_time = false;
		Vector tokens = null;
		String command = ((String)__command_Vector.elementAt(0)).trim();
		// Parse the incoming string and fill the fields...
		if ( command.regionMatches(true,0, "setWarningLevel", 0,13) ) {
			// New style...
			tokens = StringUtil.breakStringList ( command,
				" (,)", StringUtil.DELIM_SKIP_BLANKS );
		}
		else if ( command.length() >= 2 ) {
			// Old style -d#... or -d #...
			if (	(command.length() == 2) ||
				((command.length() > 2) &&
				(command.charAt(2) == ' ')) ) {
				// Parse the whole thing...
				tokens = StringUtil.breakStringList ( command,
					" ,", StringUtil.DELIM_SKIP_BLANKS );
			}
			else {	// Parse from the 3rd character on and then
				// insert a dummy command...
				tokens = StringUtil.breakStringList (
					command.substring(2),
					" ,", StringUtil.DELIM_SKIP_BLANKS );
				tokens.insertElementAt("-d",0);
			}
		}
		// Now the same token structure...
		int size = 0;
		if ( tokens != null ) {
			size = tokens.size();
		}
		if ( size == 1 ) {
			// Set warning level to 1...
			screen = "1";
			log = "1";
		}
		else if ( size == 2 ) {
			// Set warning level to same value for all...
			screen = ((String)tokens.elementAt(1)).trim();
			log = screen;
		}
		else if ( size == 3 ) {
			// Set warning level to different values for log and
			// display...
			screen = ((String)tokens.elementAt(1)).trim();
			log = ((String)tokens.elementAt(2)).trim();
		}
		__screen_JTextField.setText( screen );
		__log_JTextField.setText( log );
	}
	// Regardless, reset the command from the fields...
	screen = __screen_JTextField.getText().trim();
	log = __log_JTextField.getText().trim();
	if ( (log.length() > 0) && (screen.length() > 0) ) {
		__command_JTextField.setText("setWarningLevel(" + screen +
		"," + log + ")" );
	}
	else if ( (log.length() == 0) && (screen.length() == 0) ) {
		__command_JTextField.setText("setWarningLevel()");
	}
	else {	// For now don't do anything fancy but this may cause
		// problems...
		__command_JTextField.setText("setWarningLevel(" + screen +
		"," + log + ")" );
	}
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

} // end setWarningLevel_JDialog
