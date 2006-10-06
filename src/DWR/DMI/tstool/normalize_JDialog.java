// ----------------------------------------------------------------------------
// normalize_JDialog - editor for normalize()
// ----------------------------------------------------------------------------
// Copyright:	See the COPYRIGHT file.
// ----------------------------------------------------------------------------
// History: 
//
// 20 Feb 2001	Steven A. Malers, RTi	Initial version.
// 2002-04-24	SAM, RTi		Clean up dialog.
// 2003-12-16	SAM, RTi		Update to Swing.
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

public class normalize_JDialog extends JDialog
implements ActionListener, ItemListener, KeyListener, WindowListener
{

private String __MINVAL_TS = "MinFromTS";
private String __MINVAL_ZERO = "MinZero";

private SimpleJButton	__cancel_JButton = null,// Cancel Button
			__ok_JButton = null;	// Ok Button
private JFrame		__parent_JFrame = null;	// parent JFrame
private Vector		__command_Vector = null; // Command as Vector of String
private JTextField	__command_JTextField=null;// Command as TextField
private JTextField	__alias_JTextField = null;// Field for time series alias
private SimpleJComboBox	__independent_JComboBox = null;
						// Time series available to
						// operate on.
private SimpleJComboBox	__minval_JComboBox = null;// Indicates whether minimum
						// value should be zero or
						// taken from time series.
private JTextField	__tsident_JTextField = null;
						// Field for time series
						// identifier
private JTextField	__minval_JTextField = null;
						// Minimum value in normalized
						// results.
private JTextField	__maxval_JTextField = null;
						// Maximum value in normalized
						// results.
private Vector		__tsids = null;		// Time series identifiers
						// to check against selected
						// alias
private boolean		__error_wait = false;	// Is there an error that we
						// are waiting to be cleared up
						// or Cancel?
private boolean		__first_time = true;

/**
normalize_JDialog constructor.
@param parent JFrame class instantiating this class.
@param command Command to parse.
@param tsids Time series identifiers for existing time series.
*/
public normalize_JDialog ( JFrame parent, Vector command, Vector tsids )
{	super(parent, true);
	initialize ( parent, "Edit normalize() Command", command, tsids );
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
	String alias = __alias_JTextField.getText().trim();
	String minval = __minval_JTextField.getText().trim();
	String maxval = __maxval_JTextField.getText().trim();
	String warning = "";
	if ( alias.length() == 0 ) {
		warning +=
		"\nThe alias must be specified.";
	}
	if ( !StringUtil.isDouble(minval) ) {
		warning +=
		"\nMinimum value \"" + minval + "\" is not a number.\n"+
		"Specify a number or Cancel.";
	}
	if ( !StringUtil.isDouble(maxval) ) {
		warning +=
		"\nMaximum value \"" + maxval + "\" is not a number.\n"+
		"Specify a number or Cancel.";
	}
	if ( warning.length() > 0 ) {
		__error_wait = true;
		Message.printWarning ( 1, "normalize_JDialog", warning );
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
	__independent_JComboBox = null;
	__maxval_JTextField = null;
	__minval_JComboBox = null;
	__minval_JTextField = null;
	__ok_JButton = null;
	__parent_JFrame = null;
	__tsident_JTextField = null;
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
@param command Vector of String containing the command.
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
		"Create a new time series by normalizing the data from a" +
		" time series."),
		0, y, 7, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Use the alias to reference the new time series." +
		"  Data units are not changed."),
		0, ++y, 7, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel ( "Time Series Alias:" ), 
		0, ++y, 1, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.EAST);
	__alias_JTextField = new JTextField ( 20 );
	__alias_JTextField.addKeyListener ( this );
        JGUIUtil.addComponent(main_JPanel, __alias_JTextField,
		1, y, 2, 1, 1, 0, insetsTLBR, gbc.NONE, gbc.WEST);

        JGUIUtil.addComponent(main_JPanel,
		new JLabel("Time Series to Normalize:"),
		0, ++y, 1, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.EAST);
	__independent_JComboBox = new SimpleJComboBox ( false );
	int size = 0;
	if ( tsids != null ) {
		size = tsids.size();
	}
	if ( size == 0 ) {
		Message.printWarning ( 1, "normalize_JDialog.initialize",
		"You must define time series before inserting a normalize() " +
		"command." );
		response ( 0 );
	}
	__independent_JComboBox.setData ( tsids );
	__independent_JComboBox.addItemListener ( this );
        JGUIUtil.addComponent(main_JPanel, __independent_JComboBox,
		1, y, 6, 1, 1, 0, insetsTLBR, gbc.HORIZONTAL, gbc.WEST);

        JGUIUtil.addComponent(main_JPanel,new JLabel ("Minimum Output Value:" ),
		0, ++y, 1, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.EAST);
	__minval_JTextField = new JTextField ( "0.0", 10 );
	JGUIUtil.addComponent(main_JPanel, __minval_JTextField,
		1, y, 2, 1, 1, 0, insetsTLBR, gbc.NONE, gbc.WEST);
	__minval_JTextField.addKeyListener ( this );

        JGUIUtil.addComponent(main_JPanel, new JLabel("Maximum Output Value:"), 
		0, ++y, 1, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.EAST);
	__maxval_JTextField = new JTextField ( "1.0", 10 );
	JGUIUtil.addComponent(main_JPanel, __maxval_JTextField,
		1, y, 2, 1, 1, 0, insetsTLBR, gbc.NONE, gbc.WEST);
	__maxval_JTextField.addKeyListener ( this );

        JGUIUtil.addComponent(main_JPanel, new JLabel(
		"Minimum Data Value to Process:"),
		0, ++y, 1, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.EAST);
	__minval_JComboBox = new SimpleJComboBox ( false );
	__minval_JComboBox.addItem ( __MINVAL_TS );
	__minval_JComboBox.addItem ( __MINVAL_ZERO );
	__minval_JComboBox.addItemListener ( this );
        JGUIUtil.addComponent(main_JPanel, __minval_JComboBox,
		1, y, 2, 1, 1, 0, insetsTLBR, gbc.NONE, gbc.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel ( "Command:"),
		0, ++y, 1, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.EAST);
	__command_JTextField = new JTextField (50);
	__command_JTextField.setEditable ( false );
	JGUIUtil.addComponent(main_JPanel, __command_JTextField,
		1, y, 6, 1, 1, 0, insetsTLBR, gbc.HORIZONTAL, gbc.WEST );

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

	String val = null;
	if ( event.getComponent() == __minval_JTextField ) {
		val = __minval_JTextField.getText();
	}
	else {	val = __maxval_JTextField.getText();
	}
	if ( val == null ) {
		val = "";
	}
	else {	val = val.trim();
	}
	if ( !val.equals("-") && !val.equals(".") ) {
		refresh();
	}
}

public void keyTyped ( KeyEvent event ) {;}

/**
Refresh the command from the other text field contents.
*/
private void refresh ()
{	String alias = "";
	String tsident = "";
	String maxval = "";
	String minval = "";
	String minval_type = "";
	__error_wait = false;
	if ( __first_time ) {
		__first_time = false;
		if ( ((String)__command_Vector.elementAt(0)).indexOf('=')>=0){
			// Parse an existing command
			// Assume TS X = normalize(tsident)...
			Vector v = StringUtil.breakStringList (
			(String)__command_Vector.elementAt(0), "=",
			StringUtil.DELIM_SKIP_BLANKS );
			if ( (v != null) && (v.size() == 2) ) {
				// First field has format "TS X"
				alias = ((String)v.elementAt(0)).trim();
				alias = alias.substring(2).trim();
				__alias_JTextField.setText( alias );
				// Second field has tsident
				// normalize(tsident)
				tsident = ((String)v.elementAt(1)).trim();
				v = StringUtil.breakStringList ( tsident,
					"( ),", StringUtil.DELIM_SKIP_BLANKS );
				tsident = ((String)v.elementAt(1)).trim();
				minval_type = ((String)v.elementAt(2)).trim();
				minval = ((String)v.elementAt(3)).trim();
				maxval = ((String)v.elementAt(4)).trim();
			}
			// Now select the item in the list.  If not a match,
			// print a warning.
			if (	JGUIUtil.isSimpleJComboBoxItem(
				__independent_JComboBox, tsident,
				JGUIUtil.NONE, null, null ) ) {
				__independent_JComboBox.select ( alias );
			}
			else {	Message.printWarning ( 1,
				"normalize_JDialog.refresh",
				"Existing normalize() references a " +
				"non-existent\n"+
				"time series \"" + tsident + "\".  Select a\n" +
				"different time series or Cancel." );
			}
			if (	JGUIUtil.isSimpleJComboBoxItem(
				__minval_JComboBox, minval_type,
				JGUIUtil.NONE, null, null ) ) {
				__minval_JComboBox.select ( minval_type );
			}
			else {	Message.printWarning ( 1,
				"normalize_JDialog.refresh", "Existing " +
				"normalize() references an invalid\n"+
				"minimum value choice \"" + minval_type +
				"\".  Select a\n" +
				"different choice or Cancel." );
			}
			// Third and fourth fields have min and max values...
			__minval_JTextField.setText ( minval );
			__maxval_JTextField.setText ( maxval );
		}
	}
	// Regardless, reset the command from the fields...
	alias = __alias_JTextField.getText().trim();
	tsident = __independent_JComboBox.getSelected();
	minval = __minval_JTextField.getText().trim();
	maxval = __maxval_JTextField.getText().trim();
	minval_type = __minval_JComboBox.getSelected();
	if ( (alias == null) || (alias.trim().length() == 0) ) {
		return;
	}
	__command_JTextField.setText("TS " + alias + " = normalize(" +
			tsident + "," + minval_type + "," + minval + "," +
			maxval + ")" );
	// See if alias is already in the time series identifiers list.  If so
	// print a warning...
	if ( StringUtil.indexOf(__tsids,alias) >= 0 ) {
		Message.printWarning ( 1, "normalize_JDialog.refresh",
		"Time series alias \"" + alias + "\" is already used above.\n" +
		"Choose a different value or Cancel" );
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

} // end normalize_JDialog
