// ----------------------------------------------------------------------------
// relativeDiff_JDialog - editor for relativeDiff()
// ----------------------------------------------------------------------------
// Copyright:	See the COPYRIGHT file.
// ----------------------------------------------------------------------------
// History: 
//
// 20 Feb 2001	Steven A. Malers, RTi	Initial version.
// 2002-04-24	SAM, RTi		Clean up dialog.
// 2003-12-16	SAM, RTi		Update to Swing.
// 2004-08-02	SAM, RTi		Fix bugs where data fields are not
//					setting properly when editing an
//					existing command.
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

public class relativeDiff_JDialog extends JDialog
implements ActionListener, ItemListener, KeyListener, WindowListener
{

private String __DIVISORTS1 = "DivideByTS1";
private String __DIVISORTS2 = "DivideByTS2";

private SimpleJButton	__cancel_JButton = null,// Cancel Button
			__ok_JButton = null;	// Ok Button
private JFrame		__parent_JFrame = null;	// parent JFrame
private Vector		__command_Vector = null; // Command as Vector of String
private JTextField	__command_JTextField=null;// Command as JTextField
private JTextField	__alias_JTextField = null;// Field for time series alias
private SimpleJComboBox	__ts1_JComboBox = null;	// Time series available to
						// operate on.
private SimpleJComboBox	__ts2_JComboBox = null;	// Time series available to
						// operate on.
private SimpleJComboBox	__divisor_JComboBox =null;// Indicates which time series
						// is divisor.
private Vector		__tsids = null;		// Time series identifiers
						// to check against selected
						// alias
private boolean		__error_wait = false;	// Is there an error that we
						// are waiting to be cleared up
						// or Cancel?
private boolean		__first_time = true;

/**
relativeDiff_JDialog constructor.
@param parent JFrame class instantiating this class.
@param command Command to parse.
@param tsids Time series identifiers for existing time series.
*/
public relativeDiff_JDialog ( JFrame parent, Vector command, Vector tsids )
{	super(parent, true);
	initialize ( parent, "Edit relativeDiff() Command", command, tsids );
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
	// See if alias is already in the time series identifiers list.  If so
	// print a warning...
	if ( StringUtil.indexOf(__tsids,alias) >= 0 ) {
		warning +=
			"\nTime series alias \"" + alias +
			"\" is already used above.\n" +
			"Choose a different value or Cancel";
	}
	if ( warning.length() > 0 ) {
		Message.printWarning ( 1, "relativeDiff_JDialog", warning );
		__error_wait = true;
	}
}

/**
Free memory for garbage collection.
*/
protected void finalize ()
throws Throwable
{	__alias_JTextField = null;
	__cancel_JButton = null;
	__command_JTextField = null;
	__command_Vector = null;
	__ts1_JComboBox = null;
	__ts2_JComboBox = null;
	__divisor_JComboBox = null;
	__ok_JButton = null;
	__parent_JFrame = null;
	__tsids = null;
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
@param command Vector of String containing command.
@param tsids Time series identifiers to compare alias to.
*/
private void initialize ( JFrame parent, String title, Vector command,
			Vector tsids )
{	__parent_JFrame = parent;
	__command_Vector = command;
	__tsids = tsids;

	addWindowListener( this );

        Insets insetsTLBR = new Insets(2,2,2,2);

	JPanel main_JPanel = new JPanel();
	main_JPanel.setLayout( new GridBagLayout() );
	GridBagConstraints gbc = new GridBagConstraints();
	getContentPane().add ( "North", main_JPanel );
	int y = 0;

        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Create a new unitless time series with data values:" ),
		0, y, 7, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"    TS = (TS1 - TS2)/Divisor"), 
		0, ++y, 7, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"where the Divisor is either TS1 or TS2."), 
		0, ++y, 7, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel("Attempting to divide by" +
		" zero sets the data point to the missing value."), 
		0, ++y, 7, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel("TS Time Series Alias:"),
		0, ++y, 1, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.EAST);
	__alias_JTextField = new JTextField ( "" );
	__alias_JTextField.addKeyListener ( this );
        JGUIUtil.addComponent(main_JPanel, __alias_JTextField,
		1, y, 6, 1, 1, 0, insetsTLBR, gbc.HORIZONTAL, gbc.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel("Time Series 1 (TS1):"),
		0, ++y, 1, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.EAST);
	__ts1_JComboBox = new SimpleJComboBox ( false );
	int size = 0;
	if ( tsids != null ) {
		size = tsids.size();
	}
	if ( size == 0 ) {
		Message.printWarning ( 1, "relativeDiff_JDialog.initialize",
		"You must define time series before inserting a " +
		"relativeDiff() command." );
		response ( 0 );
	}
	__ts1_JComboBox.setData ( tsids );
	__ts1_JComboBox.addItemListener ( this );
        JGUIUtil.addComponent(main_JPanel, __ts1_JComboBox,
		1, y, 6, 1, 1, 0, insetsTLBR, gbc.HORIZONTAL, gbc.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel("Time Series 2 (TS2):"),
		0, ++y, 1, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.EAST);
	__ts2_JComboBox = new SimpleJComboBox ( false );
	size = 0;
	if ( tsids != null ) {
		size = tsids.size();
	}
	if ( size == 0 ) {
		Message.printWarning ( 1, "relativeDiff_JDialog.initialize",
		"You must define time series before inserting a " +
		"relativeDiff() command." );
		response ( 0 );
	}
	__ts2_JComboBox.setData ( tsids );
	__ts2_JComboBox.addItemListener ( this );
        JGUIUtil.addComponent(main_JPanel, __ts2_JComboBox,
		1, y, 6, 1, 1, 0, insetsTLBR, gbc.HORIZONTAL, gbc.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel("Divisor:"),
		0, ++y, 1, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.EAST);
	__divisor_JComboBox = new SimpleJComboBox ( false );
	__divisor_JComboBox.addItem ( "DivideByTS1" );
	__divisor_JComboBox.addItem ( "DivideByTS2" );
	__divisor_JComboBox.addItemListener ( this );
        JGUIUtil.addComponent(main_JPanel, __divisor_JComboBox,
		1, y, 2, 1, 1, 0, insetsTLBR, gbc.NONE, gbc.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel ( "Command:"),
		0, ++y, 1, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.EAST);
	__command_JTextField = new JTextField (50);
	__command_JTextField.setEditable ( false );
	JGUIUtil.addComponent(main_JPanel, __command_JTextField,
		1, y, 6, 1, 1, 0, insetsTLBR, gbc.HORIZONTAL, gbc.WEST);

	// Refresh the contents...
	refresh();

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
{	refresh();
}

/**
Respond to KeyEvents.
*/
public void keyPressed ( KeyEvent event )
{	int code = event.getKeyCode();

	if ( code == event.VK_ENTER ) {
		refresh();
		checkInput();
		if ( !__error_wait ) {
			response ( 1 );
		}
	}
}

public void keyReleased ( KeyEvent event )
{	// Only refresh if the text is not just a "-" or ".".  Otherwise, we'll
	// get a warning about it not being a number...

	refresh();
}

public void keyTyped ( KeyEvent event ) {;}

/**
Refresh the command from the other text field contents.
*/
private void refresh ()
{	String alias = "";
	String ts1 = "";
	String ts2 = "";
	String divisor = "";
	__error_wait = false;
	if ( __first_time ) {
		__first_time = false;
		if ( ((String)__command_Vector.elementAt(0)).indexOf('=')>=0){
			// Parse an existing command
			// Assume TS X = relativeDiff(tsident)...
			Vector v = StringUtil.breakStringList (
			(String)__command_Vector.elementAt(0), "=",
			StringUtil.DELIM_SKIP_BLANKS );
			if ( (v != null) && (v.size() == 2) ) {
				// First field has format "TS X"
				alias = ((String)v.elementAt(0)).trim();
				alias = alias.substring(2).trim();
				__alias_JTextField.setText( alias );
				// Second field after the = has 
				// relativeDiff(tsident,...)
				v = StringUtil.breakStringList ( 
					((String)v.elementAt(1)).trim(),
					"( ),", StringUtil.DELIM_SKIP_BLANKS );
				ts1 = ((String)v.elementAt(1)).trim();
				ts2 = ((String)v.elementAt(2)).trim();
				divisor = ((String)v.elementAt(3)).trim();
			}
			// Now select the item in the list.  If not a match,
			// print a warning.
			if (	JGUIUtil.isSimpleJComboBoxItem( __ts1_JComboBox,
				ts1, JGUIUtil.NONE, null, null ) ) {
				__ts1_JComboBox.select ( ts1 );
			}
			else {	Message.printWarning ( 1,
				"relativeDiff_JDialog.refresh", "Existing " +
				"relativeDiff() references a non-existent\n"+
				"TS1 time series \"" + ts1 + "\".  Select a\n" +
				"different time series or Cancel." );
			}
			if (	JGUIUtil.isSimpleJComboBoxItem( __ts2_JComboBox,
				ts2, JGUIUtil.NONE, null, null ) ) {
				__ts2_JComboBox.select ( ts2 );
			}
			else {	Message.printWarning ( 1,
				"relativeDiff_JDialog.refresh", "Existing " +
				"relativeDiff() references a non-existent\n"+
				"TS2 time series \"" + ts2 + "\".  Select a\n" +
				"different time series or Cancel." );
			}
			if (	JGUIUtil.isSimpleJComboBoxItem(
				__divisor_JComboBox,
				divisor, JGUIUtil.NONE, null, null ) ) {
				__divisor_JComboBox.select ( divisor );
			}
			else {	Message.printWarning ( 1,
				"relativeDiff_JDialog.refresh", "Existing " +
				"relativeDiff() divisor parameter \""+ divisor+
				"\" is invalid\".\nSelect a" +
				"different value or Cancel." );
			}
		}
	}
	// Regardless, reset the command from the fields...
	alias = __alias_JTextField.getText().trim();
	ts1 = __ts1_JComboBox.getSelected();
	ts2 = __ts2_JComboBox.getSelected();
	divisor = __divisor_JComboBox.getSelected();
	if ( (alias == null) || (alias.trim().length() == 0) ) {
		return;
	}
	__command_JTextField.setText("TS " + alias + " = relativeDiff(" +
			ts1 + "," + ts2 + "," + divisor + ")" );
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

public void windowActivated( WindowEvent evt ){;}
public void windowClosed( WindowEvent evt ){;}
public void windowDeactivated( WindowEvent evt ){;}
public void windowDeiconified( WindowEvent evt ){;}
public void windowIconified( WindowEvent evt ){;}
public void windowOpened( WindowEvent evt ){;}

} // end relativeDiff_JDialog
