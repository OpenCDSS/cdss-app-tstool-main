// ----------------------------------------------------------------------------
// addConstant_JDialog - editor for addConstant()
// ----------------------------------------------------------------------------
// Copyright:	See the COPYRIGHT file.
// ----------------------------------------------------------------------------
// History: 
//
// 2002-08-26	Steven A. Malers, RTi	Initial version - copy and modify
//					scale_Dialog().
// 2003-12-03	SAM, RTi		Convert to Swing.
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
import RTi.Util.Message.Message;
import RTi.Util.String.StringUtil;
import RTi.Util.Time.DateTime;

public class addConstant_JDialog extends JDialog
implements ActionListener, ItemListener, KeyListener, WindowListener
{
private SimpleJButton	__cancel_JButton = null,// Cancel Button
			__ok_JButton = null;	// Ok Button
private JFrame		__parent_JFrame = null;	// parent JFrame GUI class
private Vector		__command_Vector = null; // Command as Vector of String
private JTextField	__command_JTextField=null;// Command as JTextField
private SimpleJComboBox	__alias_JComboBox = null;// Field for time series alias
private JTextField	__add_JTextField = null;// Field for added value
private JTextField	__analysis_period_start_JTextField = null;
private JTextField	__analysis_period_end_JTextField = null;
						// Fields for analysis period
private boolean		__error_wait = false;	// Is there an error that we
						// are waiting to be cleared up
						// or Cancel?
private boolean		__first_time = true;

/**
addConstant_JDialog constructor.
@param parent JFrame class instantiating this class.
@param command Command to parse.
@param tsids Time series identifiers for time series available to add.
*/
public addConstant_JDialog ( JFrame parent, Vector command, Vector tsids )
{	super(parent, true);
	initialize ( parent, "Edit addConstant() Command", command, tsids );
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
{	String add = __add_JTextField.getText().trim();
	String analysis_period_start =
		__analysis_period_start_JTextField.getText().trim();
	String analysis_period_end =
		__analysis_period_end_JTextField.getText().trim();
	String warning = "";

	if ( !StringUtil.isDouble(add) ) {
		warning +=
			"\nAdd value \"" + add + "\" is not a number.\n"+
			"Specify a number or Cancel.";
			__error_wait = true;
	}
	if (	!analysis_period_start.equals("*") &&
		!analysis_period_start.equalsIgnoreCase("OutputStart")){
		try {	DateTime startdate =
				DateTime.parse(analysis_period_start);
		}
		catch ( Exception e ) {
			warning +=
				"\nDate \"" + analysis_period_start +
				"\" is not a valid date.\n"+
				"Specify a date, *, OutputStart," +
				" or press Cancel.";
				__error_wait = true;
		}
	}
	if (	!analysis_period_end.equals("*") &&
		!analysis_period_end.equalsIgnoreCase("OutputEnd") ) {
		try {	DateTime enddate = DateTime.parse( analysis_period_end);
		}
		catch ( Exception e ) {
			warning +=
				"\nDate \"" + analysis_period_end +
				"\" is not a valid date.\n"+
				"Specify a date, *, OutputEnd," +
				"or press Cancel.";
			__error_wait = true;
		}
	}

	if ( warning.length() > 0 ) {
		__error_wait = true;
		Message.printWarning ( 1, "addConstant_JDialog", warning );
	}
}

/**
Free memory for garbage collection.
*/
protected void finalize ()
throws Throwable
{	__alias_JComboBox = null;
	__cancel_JButton = null;
	__command_JTextField = null;
	__command_Vector = null;
	__ok_JButton = null;
	__parent_JFrame = null;
	__add_JTextField = null;
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
		"Add a constant to a time series' data values." ), 
		0, y, 7, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Specify dates with precision appropriate for the data, " +
		"use * for all available data, OutputStart, or OutputEnd." ),
		0, ++y, 7, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel ( "Time Series:" ), 
		0, ++y, 1, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.EAST);
	__alias_JComboBox = new SimpleJComboBox ( false );
	int size = 0;
	if ( tsids != null ) {
		size = tsids.size();
	}
	if ( size == 0 ) {
		Message.printWarning ( 1, "addConstant_JDialog.initialize",
		"You must define time series before inserting a addConstant() "+
		"command." );
		response ( 0 );
	}
	__alias_JComboBox.setData ( tsids );
	// Always allow a "*" to let all time series be filled...
	__alias_JComboBox.add ( "*" );
	__alias_JComboBox.addItemListener ( this );
        JGUIUtil.addComponent(main_JPanel, __alias_JComboBox,
		1, y, 6, 1, 1, 0, insetsTLBR, gbc.HORIZONTAL, gbc.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel ( "Add Value:" ), 
		0, ++y, 1, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.EAST);
	__add_JTextField = new JTextField ( 10 );
	__add_JTextField.addKeyListener ( this );
        JGUIUtil.addComponent(main_JPanel, __add_JTextField,
		1, y, 2, 1, 1, 0, insetsTLBR, gbc.NONE, gbc.WEST);

	JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Analysis Period:" ), 
		0, ++y, 1, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.EAST);
	__analysis_period_start_JTextField = new JTextField ( "*", 15 );
	__analysis_period_start_JTextField.addKeyListener ( this );
	JGUIUtil.addComponent(main_JPanel, __analysis_period_start_JTextField,
		1, y, 2, 1, 1, 0, insetsTLBR, gbc.HORIZONTAL, gbc.WEST);
	JGUIUtil.addComponent(main_JPanel, new JLabel ( "to" ), 
		3, y, 2, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.CENTER);
	__analysis_period_end_JTextField = new JTextField ( "*", 15 );
	__analysis_period_end_JTextField.addKeyListener ( this );
	JGUIUtil.addComponent(main_JPanel, __analysis_period_end_JTextField,
		5, y, 2, 1, 1, 0, insetsTLBR, gbc.HORIZONTAL, gbc.WEST);

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

	if ( o.equals(__alias_JComboBox) ) {
		refresh();
	}
}

/**
Respond to KeyEvents.
*/
public void keyPressed ( KeyEvent event )
{	int code = event.getKeyCode();

	if ( code == event.VK_ENTER ) {
		refresh ();
		checkInput ();
		if ( !__error_wait ) {
			response ( 1 );
		}
	}
}

public void keyReleased ( KeyEvent event )
{	// Only refresh if the text is not just a "-" or ".".  Otherwise, we'll
	// get a warning about it not being a number...
	String add = __add_JTextField.getText();
	if ( add == null ) {
		add = "";
	}
	else {	add = add.trim();
	}
	if ( !add.equals("-") && !add.equals(".") ) {
		refresh();
	}
}

public void keyTyped ( KeyEvent event ) {;}

/**
Refresh the command from the other text field contents.
*/
private void refresh ()
{	String alias = "";
	String add = "";
	String analysis_period_start = "";
	String analysis_period_end = "";
	__error_wait = false;
	if ( __first_time ) {
		__first_time = false;
		// Parse the incoming string and fill the fields...
		Vector v = StringUtil.breakStringList (
			((String)__command_Vector.elementAt(0)).trim(),"(),",
			StringUtil.DELIM_SKIP_BLANKS );
		if ( (v != null) && (v.size() >= 3) ) {
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
				"addConstant_JDialog.refresh",
				"Existing addConstant() references a " +
				"non-existent\n"+
				"time series \"" + alias + "\".  Select a\n" +
				"different time series or Cancel." );
			}
			// Third field has add value...
			add = ((String)v.elementAt(2)).trim();
			__add_JTextField.setText( add );
			// Fourth and fifth fields optionally have analysis
			// period...
			if ( v.size() >= 4 ) {
				analysis_period_start =
					((String)v.elementAt(3)).trim();
				__analysis_period_start_JTextField.setText(
					analysis_period_start );
			}
			if ( v.size() >= 5 ) {
				analysis_period_end =
					((String)v.elementAt(4)).trim();
				__analysis_period_end_JTextField.setText(
					analysis_period_end );
			}
		}
		else {	// First time and new command that is blank.
			__analysis_period_start_JTextField.setText("*");
			__analysis_period_end_JTextField.setText("*");
		}
	}
	// Regardless, reset the command from the fields...
	alias = __alias_JComboBox.getSelected();
	add = __add_JTextField.getText().trim();
	analysis_period_start=
		__analysis_period_start_JTextField.getText().trim();
	analysis_period_end = __analysis_period_end_JTextField.getText().trim();
	__command_JTextField.setText("addConstant(" + alias + "," + add + "," +
		analysis_period_start + "," + analysis_period_end + ")" );
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

} // end addConstant_JDialog
