// ----------------------------------------------------------------------------
// readTimeSeries_JDialog - editor for time series expression
// ----------------------------------------------------------------------------
// Copyright:	See the COPYRIGHT file.
// ----------------------------------------------------------------------------
// History: 
//
// 30 Nov 2000	Steven A. Malers, RTi	Initial version.
// 2002-04-16	SAM, RTi		Clean up interface.  Add instructions.
// 2002-04-26	SAM, RTi		Add double quotes around the time series
//					identifier to handle paths with spaces.
//					Add a button to add/remove the working
//					directory from identifiers that have
//					paths.
// 2002-05-08	SAM, RTi		Fix so that the working directory button
//					is only shown if the time series
//					identifier includes an input name.
// 2004-02-22	SAM, RTi		* Update to Swing.
//					* Remove working directory logic - the
//					  dialog simply adds the alias.
//					* Update to always enforce the new
//					  TS X = readTimeSeries() command
//					  syntax.  There is another dialog for
//					  editing TSIDs as a text field.
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
import RTi.Util.IO.PropList;
import RTi.Util.Message.Message;
import RTi.Util.String.StringUtil;

public class readTimeSeries_JDialog extends JDialog
implements ActionListener, KeyListener, WindowListener
{
private SimpleJButton	__cancel_JButton = null,// Cancel Button
			__ok_JButton = null;	// Ok Button
private Vector		__command_Vector = null; // Command as Vector of String
private JTextField	__command_JTextField=null;// Command as TextField
private JTextField	__alias_JTextField = null;// Field for time series alias
private JTextField	__tsident_JTextField=null;// Field for time series
						// identifier
private Vector		__tsids = null;		// Time series identifiers
						// to check against selected
						// alias
private boolean		__first_time = true;
private boolean		__error_wait = false;

/**
readTimeSeries_JDialog constructor.
@param parent JFrame class instantiating this class.
@param app_PropList Properties from the application.
@param command Time series command to parse.
@param tsids Time series identifiers for existing time series.
*/
public readTimeSeries_JDialog (	JFrame parent, PropList app_PropList,
				Vector command, Vector tsids )
{	super(parent, true);
	initialize ( parent, "Edit readTimeSeries() Command", app_PropList,
		command, tsids );
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
{	String alias = __alias_JTextField.getText().trim();
	String warning = "";
	__error_wait = false;
	// See if alias is already in the time series identifiers list.  If so
	// print a warning...
	if ( StringUtil.indexOf(__tsids,alias) >= 0 ) {
		warning += "\nTime series alias \"" + alias +
			"\" is already used in previous commands.\n" +
			"Choose a different alias or Cancel.";
	}
	if ( warning.length() > 0 ) {
		Message.printWarning ( 1, "readTimeSeries_JDialog", warning );
		__error_wait = true;
	}
}

/**
Free memory for garbage collection.
@exception Throwable if there is an error.
*/
protected void finalize ()
throws Throwable
{	__alias_JTextField = null;
	__cancel_JButton = null;
	__command_JTextField = null;
	__command_Vector = null;
	__ok_JButton = null;
	__tsident_JTextField = null;
	__tsids = null;
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
@param app_PropList Properties from the application.
@param command Vector of String containing the time series command, which
should have a time series identifier and optionally comments.
@param tsids Time series identifiers to compare alias to.
*/
private void initialize ( JFrame parent, String title, PropList app_PropList,
			Vector command, Vector tsids )
{	__command_Vector = command;
	__tsids = tsids;

try {
	addWindowListener( this );

        Insets insetsTLBR = new Insets(2,2,2,2);

	JPanel main_JPanel = new JPanel();
	main_JPanel.setLayout( new GridBagLayout() );
	getContentPane().add ( "North", main_JPanel );
	int y = 0;

        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"The readTimeSeries() command is a general read command."),
		0, y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Its main purpose is to assign an alias to a time series, " +
		"which is more convenient to use than the long identifier."),
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Read commands for specific input types may offer more " +
		"options."),
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"The alias should be descriptive and should not " +
		" contain spaces, periods, or parentheses."),
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel ( "Time series alias:" ), 
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	__alias_JTextField = new JTextField ( "", 60 );
	__alias_JTextField.addKeyListener ( this );
        JGUIUtil.addComponent(main_JPanel, __alias_JTextField,
		1, y, 6, 1, 1, 0, insetsTLBR, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);

        JGUIUtil.addComponent(main_JPanel,new JLabel("Time series identifier:"),
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	__tsident_JTextField = new JTextField (
		(String)__command_Vector.elementAt(0), 60 );
	__tsident_JTextField.setEditable ( false );
	__tsident_JTextField.addKeyListener ( this );
        JGUIUtil.addComponent(main_JPanel, __tsident_JTextField,
		1, y, 6, 1, 1, 0, insetsTLBR, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST );

        JGUIUtil.addComponent(main_JPanel, new JLabel ( "Command:" ), 
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	__command_JTextField = new JTextField (60);
	__command_JTextField.setEditable ( false );
	JGUIUtil.addComponent(main_JPanel, __command_JTextField,
		1, y, 6, 1, 1, 0, insetsTLBR, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);

	// Refresh the contents...
	refresh();

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
	refresh();	// Sets the __path_JButton status
        super.setVisible( true );
}
catch ( Exception e ) {
	Message.printWarning ( 1, "", e );
}
}

/**
Respond to KeyEvents.
*/
public void keyPressed ( KeyEvent event )
{	int code = event.getKeyCode();

	if ( code == KeyEvent.VK_ENTER ) {
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
{	String alias = "";
	String tsident = "";
	if ( __first_time ) {
		__first_time = false;
		// Parse the incoming string and fill the fields...
		String e = (String)__command_Vector.elementAt(0);
		if ( e.indexOf('=') == -1 ) {
			// Assume a time series identifier only...
			tsident = e;
		}
		else {	// Assume TS X = readTimeSeries(tsident)...
			Vector v = StringUtil.breakStringList (
			(String)__command_Vector.elementAt(0), "=",
			StringUtil.DELIM_SKIP_BLANKS );
			if ( (v != null) && (v.size() == 2) ) {
				// First field has format "TS X"
				alias = ((String)v.elementAt(0)).trim();
				alias = alias.substring(2).trim();
				__alias_JTextField.setText( alias );
				// Second field has tsident
				// readTimeSeries(tsident)
				tsident = ((String)v.elementAt(1)).trim();
				// Trim off the TSID to remove spaces on the
				// end...
				v = StringUtil.breakStringList ( tsident,
					"()", StringUtil.DELIM_ALLOW_STRINGS|
					StringUtil.DELIM_SKIP_BLANKS );
				tsident = ((String)v.elementAt(1)).trim();
				__tsident_JTextField.setText ( tsident );
			}
		}
	}
	// Regardless, reset the command from the fields...
	alias = __alias_JTextField.getText().trim();
	tsident = __tsident_JTextField.getText().trim();
	__command_JTextField.setText("TS " + alias +
		" = readTimeSeries(\"" + tsident + "\")" );
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
	else {	// OK (return contents)...
		refresh();
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

} // end readTimeSeries_JDialog
