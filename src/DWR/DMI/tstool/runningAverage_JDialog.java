// ----------------------------------------------------------------------------
// runningAverage_JDialog - editor for runningAverage()
// ----------------------------------------------------------------------------
// Copyright:	See the COPYRIGHT file.
// ----------------------------------------------------------------------------
// History: 
//
// 30 Dec 2000	Steven A. Malers, RTi	Initial version (copy and modify
//					scale_Dialog).
// 2002-04-23	SAM, RTi		Clean up dialog.
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

public class runningAverage_JDialog extends JDialog
implements ActionListener, ItemListener, KeyListener, WindowListener
{
private String __AVERAGE_TYPE_CENTERED = "Centered";
private String __AVERAGE_TYPE_NYEAR = "N-Year";

private SimpleJButton	__cancel_JButton = null,// Cancel Button
			__ok_JButton = null;	// Ok Button
private Vector		__command_Vector = null;// Command as Vector of String
private JTextField	__command_JTextField=null;// Command as JTextField
private SimpleJComboBox	__alias_JComboBox = null;// Field for time series alias
private SimpleJComboBox	__average_type_JComboBox = null;
						// Field for averaging type
private JTextField	__bracket_JTextField = null;
						// Field for averaging bracket
private JLabel		__bracket_JLabel = null;// Label for bracket
private boolean		__error_wait = false;	// Is there an error that we
						// are waiting to be cleared up
						// or Cancel?
private boolean		__first_time = true;

/**
runningAverage_JDialog constructor.
@param parent JFrame class instantiating this class.
@param command Command to parse.
@param tsids Time series identifiers for time series available to convert.
*/
public runningAverage_JDialog ( JFrame parent, Vector command, Vector tsids )
{	super(parent, true);
	initialize ( parent, "Edit RunningAverage() Command", command, tsids );
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
{	String bracket = __bracket_JTextField.getText().trim();
	String warning = "";
	if ( !StringUtil.isInteger(bracket) ) {
		warning +=
		"\nNumber of intervals \"" + bracket +
		"\" is not an integer.\n"+
		"Specify an integer or Cancel.";
	}
	if ( warning.length() > 0 ) {
		__error_wait = true;
		Message.printWarning ( 1, "add_JDialog", warning );
	}
}

/**
Free memory for garbage collection.
*/
protected void finalize ()
throws Throwable
{	__alias_JComboBox = null;
	__average_type_JComboBox = null;
	__bracket_JLabel = null;
	__bracket_JTextField = null;
	__cancel_JButton = null;
	__command_JTextField = null;
	__command_Vector = null;
	__ok_JButton = null;
	__AVERAGE_TYPE_CENTERED = null;
	__AVERAGE_TYPE_NYEAR = null;
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
		"Convert a time series to a running average." +
		"  Units, data type, etc. are not changed."), 
		0, y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Time Series to Convert:" ), 
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	__alias_JComboBox = new SimpleJComboBox ( false );
	int size = 0;
	if ( tsids != null ) {
		size = tsids.size();
	}
	if ( size == 0 ) {
		tsids = new Vector();
	}
	__alias_JComboBox.setData ( tsids );
	// Always allow a "*" to let all time series be filled...
	__alias_JComboBox.addItem ( "*" );
	__alias_JComboBox.addItemListener ( this );
        JGUIUtil.addComponent(main_JPanel, __alias_JComboBox,
		1, y, 6, 1, 1, 0, insetsTLBR, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel ( "Type of Average:" ), 
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	__average_type_JComboBox = new SimpleJComboBox ( false );
	__average_type_JComboBox.addItem ( __AVERAGE_TYPE_CENTERED );
	__average_type_JComboBox.addItem ( __AVERAGE_TYPE_NYEAR );
	__average_type_JComboBox.addItemListener ( this );
        JGUIUtil.addComponent(main_JPanel, __average_type_JComboBox,
		1, y, 2, 1, 1, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);

	__bracket_JLabel = new JLabel (
		"Number of Intervals on Each Side:" );
        JGUIUtil.addComponent(main_JPanel, __bracket_JLabel,
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	__bracket_JTextField = new JTextField ( 10 );
	__bracket_JTextField.addKeyListener ( this );
        JGUIUtil.addComponent(main_JPanel, __bracket_JTextField,
		1, y, 2, 1, 1, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel ( "Command:" ), 
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	__command_JTextField = new JTextField ( 50 );
	__command_JTextField.setEditable ( false );
	JGUIUtil.addComponent(main_JPanel, __command_JTextField,
		1, y, 6, 1, 1, 0, insetsTLBR, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);

	// Refresh the contents...
	refresh ();

	// South Panel: North
	JPanel button_Panel = new JPanel();
	button_Panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JGUIUtil.addComponent(main_JPanel, button_Panel, 
		0, ++y, 8, 1, 1, 0, insetsTLBR, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER);

	__cancel_JButton = new SimpleJButton("Cancel", this);
	button_Panel.add ( __cancel_JButton );
	__ok_JButton = new SimpleJButton("OK", this);
	button_Panel.add ( __ok_JButton );

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
{	Object o = e.getItemSelectable();

	if (	(o == __average_type_JComboBox) &&
		(e.getStateChange() == ItemEvent.SELECTED) ) {
		// Reset the bracket label...
		if (	__average_type_JComboBox.getSelectedItem().equals(
			__AVERAGE_TYPE_CENTERED ) ) {
			__bracket_JLabel.setText ( 
			"Number of Intervals on Each Side:" );
		}
		else {	__bracket_JLabel.setText ( 
			"Number of Years to Average:" );
		}
	}
	refresh();
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
runningAverage(TSID,Centered,3)
</pre>
*/
private void refresh ()
{	String alias = "";
	String average_type = "";
	String bracket = "";
	__error_wait = false;
	if ( __first_time ) {
		__first_time = false;
		// Parse the incoming string and fill the fields...
		Vector v = StringUtil.breakStringList (
			((String)__command_Vector.elementAt(0)).trim(),"() ,",
			StringUtil.DELIM_SKIP_BLANKS );
		if ( (v != null) && (v.size() == 4) ) {
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
				"runningAverage_JDialog.refresh",
				"Existing command references a " +
				"non-existent\n"+
				"time series \"" + alias + "\".  Select a\n" +
				"different time series or Cancel." );
			}
			// Third field has averaging type...
			average_type = ((String)v.elementAt(2)).trim();
			if (	JGUIUtil.isSimpleJComboBoxItem(
				__average_type_JComboBox,
				average_type, JGUIUtil.NONE, null, null ) ) {
				__average_type_JComboBox.select ( average_type);
				// Set the label...
				if ( average_type.equals(__AVERAGE_TYPE_NYEAR)){
					// Reset label to other than the
					// default...
					__bracket_JLabel.setText ( 
					"Number of Years to Average:" );
				}
			}
			else {	Message.printWarning ( 1,
				"runningAverage_JDialog.refresh",
				"Existing command references a " +
				"non-existent\n"+ "average type \"" +
				average_type + "\".  Select a\n" +
				"different average type or Cancel." );
			}
			// Fourth field has bracket...
			bracket = ((String)v.elementAt(3)).trim();
			__bracket_JTextField.setText( bracket );
		}
		else {	if ( __alias_JComboBox.getItemCount() > 0 ) {
				__alias_JComboBox.select ( 0 );
			}
			__average_type_JComboBox.select(0);
		}
	}
	// Regardless, reset the command from the fields...
	alias = __alias_JComboBox.getSelected();
	average_type = __average_type_JComboBox.getSelected();
	bracket = __bracket_JTextField.getText().trim();
	if ( (alias == null) || (alias.trim().length() == 0) ) {
		return;
	}
	if ( (bracket == null) || (bracket.trim().length() == 0) ) {
		return;
	}
	__command_JTextField.setText("RunningAverage(" + alias + "," +
	average_type + "," + bracket + ")" );
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

} // end runningAverage_JDialog
