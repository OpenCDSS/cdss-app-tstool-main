// ----------------------------------------------------------------------------
// newDayTSFromMonthAndDayTS_JDialog - editor for newDayTSFromMonthAndDayTS()
// ----------------------------------------------------------------------------
// Copyright:	See the COPYRIGHT file.
// ----------------------------------------------------------------------------
// History: 
//
// 17 Jan 2001	Steven A. Malers, RTi	Initial version.
// 2002-04-24	SAM, RTi		Clean up dialog.
// 2003-12-16	SAM, RTi		Update to Swing.
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

public class newDayTSFromMonthAndDayTS_JDialog extends JDialog
implements ActionListener, ItemListener, KeyListener, WindowListener
{

private SimpleJButton	__cancel_JButton = null,// Cancel Button
			__ok_JButton = null;	// Ok Button
private Vector		__command_Vector = null;// Command as Vector of String
private JTextField	__command_JTextField=null;// Command as JTextField
private JTextField	__alias0_JTextField =null;// Field for time series alias
private JTextField	__alias_JTextField = null;// Field for time series alias
private SimpleJComboBox	__monthts_JComboBox = null;// Field for independent time
						// series identifier
private SimpleJComboBox	__dayts_JComboBox = null;// Field for independent time
						// series identifier
private boolean		__error_wait = false;
private boolean		__first_time = true;

/**
newDayTSFromMonthAndDayTS constructor.
@param parent JFrame class instantiating this class.
@param command Command to parse.
@param tsids Time series identifiers for time series that can be used.
*/
public newDayTSFromMonthAndDayTS_JDialog ( JFrame parent, Vector command,
					Vector tsids )
{	super(parent, true);
	initialize ( parent, "Edit newDayTSFromMonthAndDayTS() Command",
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
	if ( alias.length() == 0 ) {
		warning +=
			"\nThe alias must be specified.";
	}
	if ( warning.length() > 0 ) {
		Message.printWarning ( 1, "newDayTSFromMonthAndDayTS", warning);
		__error_wait = true;
	}
}

/**
Free memory for garbage collection.
*/
protected void finalize ()
throws Throwable
{	__alias0_JTextField = null;
	__alias_JTextField = null;
	__cancel_JButton = null;
	__command_JTextField = null;
	__command_Vector = null;
	__monthts_JComboBox = null;
	__dayts_JComboBox = null;
	__ok_JButton = null;
	super.finalize ();
}

/**
Return the text for the command.
@return the text for the command or null if there is a problem with the
command.
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
@param tsids Time series identifiers for time series that can be used.
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
		"Create a daily time series by distributing a monthly total" +
		" using a daily pattern." ), 
		0, y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Currently this command is implemented only to convert from" +
		" acre-feet to CFS." ), 
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Time Series Alias:" ), 
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	__alias0_JTextField = new JTextField ( 30 );
	JGUIUtil.addComponent(main_JPanel, __alias0_JTextField,
		1, y, 6, 1, 1, 0, insetsTLBR, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
	__alias0_JTextField.addKeyListener(this);

        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"New Time Series Identifier:" ), 
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	__alias_JTextField = new JTextField ( 30 );
	JGUIUtil.addComponent(main_JPanel, __alias_JTextField,
		1, y, 6, 1, 1, 0, insetsTLBR, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
	__alias_JTextField.addKeyListener(this);

        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Monthly Time Series for Total:"), 
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	__monthts_JComboBox = new SimpleJComboBox ( false );
	int size = 0;
	if ( tsids != null ) {
		size = tsids.size();
	}
	if ( size == 0 ) {
		Message.printWarning ( 1,"newDayTSFromMonthAndDayTS.initialize",
		"You must define time series before inserting a " +
		"newDayTSFromMonthAndDayTS() command." );
		response ( 0 );
	}
	__monthts_JComboBox.setData ( tsids );
	__monthts_JComboBox.addItemListener ( this );
        JGUIUtil.addComponent(main_JPanel, __monthts_JComboBox,
		1, y, 6, 1, 1, 0, insetsTLBR, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Daily Time Series for Distribution:" ), 
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	__dayts_JComboBox = new SimpleJComboBox ( false );
	__dayts_JComboBox.setData ( tsids );
	__dayts_JComboBox.addItemListener ( this );
        JGUIUtil.addComponent(main_JPanel, __dayts_JComboBox,
		1, y, 6, 1, 1, 0, insetsTLBR, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel ( "Command:" ), 
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	__command_JTextField = new JTextField ( 50 );
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
        super.setVisible( true );
}

/**
Handle ItemEvent events.
@param e ItemEvent to handle.
*/
public void itemStateChanged ( ItemEvent e )
{	// Any change needs to refresh the command...
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
Refresh the command from the other text field contents.
The command is like:
<pre>
newDayTSFromMonthAndDayTS(Alias,MonthTSID,DayTSID)
</pre>
*/
private void refresh ()
{	String alias = "";
	String alias0 = "";
	String monthts = "";
	String dayts = "";
	__error_wait = false;
	if ( __first_time ) {
		__first_time = false;
		// Parse the incoming string and fill the fields...
		Vector v = StringUtil.breakStringList (
			((String)__command_Vector.elementAt(0)).trim(),
			"=() ,", StringUtil.DELIM_SKIP_BLANKS );
		// Parse out the command values...
		if ( (v != null) && (v.size() >= 6) ) {
			// New syntax...
			alias0 = ((String)v.elementAt(1)).trim();
			__alias0_JTextField.setText ( alias0 );
			alias = ((String)v.elementAt(3)).trim();
			__alias_JTextField.setText ( alias );
			monthts = ((String)v.elementAt(4)).trim();
			dayts = ((String)v.elementAt(5)).trim();
			if (	JGUIUtil.isSimpleJComboBoxItem(
				__monthts_JComboBox, monthts,
				JGUIUtil.NONE, null, null ) ) {
				__monthts_JComboBox.select ( monthts );
			}
			else {	Message.printWarning ( 1,
				"newDayTSFromMonthAndDayTS.refresh","Existing "+
				"newDayTSFromMonthAndDayTS() references a " +
				" non-existent\n"+
				"monthly time series \"" + monthts +
				"\".  Select a\n" +
				"different time series or Cancel." );
			}
			if (	JGUIUtil.isSimpleJComboBoxItem(
				__dayts_JComboBox, dayts,
				JGUIUtil.NONE, null, null ) ) {
				__dayts_JComboBox.select ( dayts );
			}
			else {	Message.printWarning ( 1,
				"newDayTSFromMonthAndDayTS.refresh","Existing "+
				"newDayTSFromMonthAndDayTS() references a " +
				"non-existent\n"+
				"daily time series \"" + dayts +
				"\".  Select a\n" +
				"different time series or Cancel." );
			}
		}
		else {	// First time and a new command that is blank.
			// To prevent a warning, select the first item in the
			// lists...
			__monthts_JComboBox.select(0);
			__dayts_JComboBox.select(0);
		}
	}
	// Regardless, reset the command from the fields...
	alias = __alias_JTextField.getText().trim();
	alias0 = __alias0_JTextField.getText().trim();
	monthts = __monthts_JComboBox.getSelected();
	dayts = __dayts_JComboBox.getSelected();
	if ( (alias0 == null) || (alias0.trim().length() == 0) ) {
		return;
	}
	if ( (alias == null) || (alias.trim().length() == 0) ) {
		return;
	}
	if ( (monthts == null) || (monthts.trim().length() == 0) ) {
		return;
	}
	if ( (dayts == null) || (dayts.trim().length() == 0) ) {
		return;
	}
	__command_JTextField.setText("TS " + alias0 +
		" = newDayTSFromMonthAndDayTS(" + alias + "," +
		monthts + "," + dayts + ")" );
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

} // end newDayTSFromMonthAndDayTS
