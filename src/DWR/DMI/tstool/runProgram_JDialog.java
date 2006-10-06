// ----------------------------------------------------------------------------
// runProgram_JDialog - editor for runProgram()
// ----------------------------------------------------------------------------
// Copyright:	See the COPYRIGHT file.
// ----------------------------------------------------------------------------
// History: 
//
// 22 Aug 2001	Steven A. Malers, RTi	Initial version (copy and modify
//					setMissingDataValue_Dialog).
//					Might need to add pause also to let
//					OS finish with files.
// 2002-04-08	SAM, RTi		Clean up dialog.
// 2003-12-03	SAM, RTi		Update to Swing.
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

public class runProgram_JDialog extends JDialog
implements ActionListener, KeyListener, WindowListener
{
private SimpleJButton	__cancel_JButton = null,// Cancel Button
			__ok_JButton = null;	// Ok Button
private JFrame		__parent_JFrame = null;	// parent Frame GUI class
private Vector		__command_Vector = null;// Command as Vector of String
private JTextField	__command_JTextField=null;// Command as TextField
private JTextField	__program_JTextField = null,
			__timeout_JTextField = null;
private boolean		__error_wait = false;	// Is there an error that we
						// are waiting to be cleared up
						// or Cancel?
private boolean		__first_time = true;

/**
runProgram_JDialog constructor.
@param parent JFrame class instantiating this class.
@param command Command to parse.
@param tsids Time series identifiers for available time series.
*/
public runProgram_JDialog ( JFrame parent, Vector command, Vector tsids )
{	super(parent, true);
	initialize ( parent, "Edit runProgram() Command", command, tsids );
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
Check the user input for errors and set __error_wait accordingly.
*/
private void checkInput ()
{	String program = __program_JTextField.getText().trim();
	String timeout = __timeout_JTextField.getText().trim();
	String routine = "runProgram_JDialog.checkInput";

	String warning = "";

	if ( program.length() == 0 ) {
		warning +=
		"\nThe program \"" + program + "\" must be specified.";
		__error_wait = true;
	}
	if ( !StringUtil.isDouble(timeout) ) {
		warning +=
		"\nTimeout value \"" + timeout + "\" is not a number.\n"+
		"Specify a number or Cancel.";
		__error_wait = true;
	}
	if ( warning.length() > 0 ) {
		__error_wait = true;
		Message.printWarning ( 1, "runProgram_JDialog", warning );
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
	__program_JTextField = null;
	__timeout_JTextField = null;
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
@param tsids Time series identifiers from
TSEngine.getTSIdentifiersFromCommands() - ignored.
*/
private void initialize ( JFrame parent, String title, Vector command,
			Vector tsids )
{	__parent_JFrame = parent;
	__command_Vector = command;

	addWindowListener( this );

        Insets insetsTLBR = new Insets(2,2,2,2);
        Insets insetsMin = new Insets(0,2,0,2);

	// Main panel...

	JPanel main_JPanel = new JPanel();
	main_JPanel.setLayout( new GridBagLayout() );
	GridBagConstraints gbc = new GridBagConstraints();
	getContentPane().add ( "North", main_JPanel );
	int y = 0;

        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"TSTool can run other programs as part of a command file."),
		0, y, 7, 1, 1, 0, insetsMin, gbc.HORIZONTAL, gbc.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Commands must use a full path or TSTool must be started " +
		"from directory to run in"),
		0, ++y, 7, 1, 1, 0, insetsMin, gbc.HORIZONTAL, gbc.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
 		"(setWorkingDir() has no effect)."), 
		0, ++y, 7, 1, 1, 0, insetsMin, gbc.HORIZONTAL, gbc.WEST);
	if ( System.getProperty("os.name").equalsIgnoreCase("Windows NT") ) {
        	JGUIUtil.addComponent(main_JPanel, new JLabel ( "Programs can "+
		"have maximum of 9 command-line arguments."), 
		0, ++y, 7, 1, 1, 0, insetsMin, gbc.HORIZONTAL, gbc.WEST);
	}
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"The timeout should be specified in seconds (value of 0" +
		" means no timeout."),
		0, ++y, 7, 1, 1, 0, insetsMin, gbc.HORIZONTAL, gbc.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel ( "External Command:" ), 
		0, ++y, 1, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.EAST);
	__program_JTextField = new JTextField ( 50 );
	__program_JTextField.setText( "");
	__program_JTextField.addKeyListener ( this );
        JGUIUtil.addComponent(main_JPanel, __program_JTextField,
		1, y, 6, 1, 1, 0, insetsTLBR, gbc.HORIZONTAL, gbc.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel ( "Timeout:" ), 
		0, ++y, 1, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.EAST);
	__timeout_JTextField = new JTextField ( "0", 55 );
	__timeout_JTextField.addKeyListener ( this );
        JGUIUtil.addComponent(main_JPanel, __timeout_JTextField,
		1, y, 2, 1, 1, 0, insetsTLBR, gbc.NONE, gbc.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel ( "Command:"),
		0, ++y, 1, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.EAST);
	__command_JTextField = new JTextField ( 30 );
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
{	// Only refresh if the text is not just a ".".  Otherwise, we'll
	// get a warning about it not being a number...
	Object o = event.getComponent();
	if ( o.equals(__timeout_JTextField) ) {
		String val = __timeout_JTextField.getText();
		if ( val == null ) {
			val = "";
		}
		else {	val = val.trim();
		}
		if ( !val.equals(".") ) {
			refresh();
		}
		val = null;
	}
	o = null;
}

public void keyTyped ( KeyEvent event ) {;}

/**
Refresh the command from the other text field contents.
*/
private void refresh ()
{	String program = "";
	String timeoutval = "";
	__error_wait = false;
	if ( __first_time ) {
		__first_time = false;
		// Parse the incoming string and fill the fields...
		Vector v = StringUtil.breakStringList (
			((String)__command_Vector.elementAt(0)).trim(),
			"() ,", StringUtil.DELIM_SKIP_BLANKS|
			StringUtil.DELIM_ALLOW_STRINGS );
		if ( (v != null) && (v.size() >= 2) ) {
			program = ((String)v.elementAt(1)).trim();
			__program_JTextField.setText( program );
			if ( v.size() > 2 ) {
				// Third field has timeout
				timeoutval = ((String)v.elementAt(2)).trim();
				__timeout_JTextField.setText( timeoutval );
			}
		}
	}
	// Regardless, reset the command from the fields...
	program = __program_JTextField.getText();
	timeoutval = __timeout_JTextField.getText();
	__command_JTextField.setText(
		"runProgram(\""+ program + "\","+timeoutval+")");
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

} // end runProgram_JDialog
