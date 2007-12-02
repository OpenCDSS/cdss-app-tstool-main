// ----------------------------------------------------------------------------
// selectTimeSeries_JDialog - editor for selectTimeSeries() and
//				deselectTimeSeries()
// ----------------------------------------------------------------------------
// Copyright:	See the COPYRIGHT file.
// ----------------------------------------------------------------------------
// History: 
//
// 2004-03-15	Steven A. Malers, RTi	Initial version (copy and modify
//					openHydroBase_JDialog).
// 2004-05-28	SAM, RTi		Add "Pos" property to get list by
//					position.
// 2004-07-11	SAM, RTi		Add SelectAllFirst and DeselectAllFirst
//					properties, to eliminate need to do
//					deselect all or select all with a
//					separate command.
// 2005-07-17	SAM, RTi		Update to allow position and pattern to
//					be used together.
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
import RTi.Util.IO.PropList;
import RTi.Util.Message.Message;
import RTi.Util.String.StringUtil;

public class selectTimeSeries_JDialog extends JDialog
implements ActionListener, ItemListener, KeyListener, WindowListener
{

// For use with DeselectAllFirst and SelectAllFirst...

private final String __False = "False";
private final String __True = "True";

private SimpleJButton	__cancel_JButton = null,	// Cancel Button
			__ok_JButton = null;		// Ok Button
private Vector		__command_Vector = null;	// Command as Vector of
							// String
private JTextField	__command_JTextField = null;	// Command as JTextField
private JTextField	__TSID_JTextField=null;		// Field for TSID to
							// match
private JTextField	__Pos_JTextField=null;		// Field for TS
							// positions
private SimpleJComboBox	__SelectAllFirst_JComboBox = null;
							// Indicates whether
							// all time series
							// should be selected
							// first,
							// when deselecting.
private SimpleJComboBox	__DeselectAllFirst_JComboBox = null;
							// Indicates whether
							// all time series
							// should be deselected
							// first,
							// when selecting.

private boolean		__error_wait = false;
private boolean		__first_time = true;

private boolean		__select = true;		// If true, then the
							// selectTimeSeries()
							// command is used.  If
							// false the
							// deselectTimeSeries()
							// command is used.
private String		__command = "SelectTimeSeries";	// Command being edited

/**
selectTimeSeries_JDialog constructor.
@param parent JFrame class instantiating this class.
@param command Command to parse.
@param tsids Time series identifiers for time series available to convert -
unused.
@param select If true then the selectTimeSeries() command is being edited.  If
false, deselectTimeSeries() is being edited.
*/
public selectTimeSeries_JDialog ( JFrame parent, Vector command, Vector tsids,
					boolean select )
{	super(parent, true);
	initialize ( parent, command, tsids, select );
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
{	String TSID = __TSID_JTextField.getText().trim();
	String Pos = __Pos_JTextField.getText().trim();
	String routine = __command + ".checkInput";
	String warning = "";
	__error_wait = false;

	if ( (TSID.length() == 0) && (Pos.length() == 0) ) {
		warning +=
			"\nA TSID pattern or position must be specified.\n"+
			"Correct or Cancel.";
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
{	__TSID_JTextField = null;
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
@param tsids Time series identifiers - unused.
*/
private void initialize ( JFrame parent, Vector command,
			Vector tsids, boolean select )
{	__command_Vector = command;
	__select = select;
	if ( __select ) {
		__command = "SelectTimeSeries";
	}
	else {	__command = "DeselectTimeSeries";
	}

	addWindowListener( this );

        Insets insetsTLBR = new Insets(2,2,2,2);

	// Main panel...

	JPanel main_JPanel = new JPanel();
	main_JPanel.setLayout( new GridBagLayout() );
	getContentPane().add ( "North", main_JPanel );
	int y = 0;

	if ( select ) {
        	JGUIUtil.addComponent(main_JPanel, new JLabel (
		"This command selects time series, similar to how time " +
		"series are interactively selected."),
		0, y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        	JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Selected time series may then be used by other commands."),
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        	JGUIUtil.addComponent(main_JPanel, new JLabel (
		"For example, output commands may allow selected time series" +
		" to be output, rather than default to all time series."),
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
	}
	else {	JGUIUtil.addComponent(main_JPanel, new JLabel (
		"This command deselects time series.  This is most often used" +
		" before a selectTimeSeries() command." ),
		0, y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
	}
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Time series can be matched by one of the following methods " +
		"(use only or a combination):"),
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"1. Use a time series identifier (TSID) pattern:"),
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"    The dot-delimited time series identifier parts are " +
		"Location.DataSource.DataType.Interval.Scenario"),
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"    The pattern used to select/deselect time series will be " +
		"matched against aliases and identifiers."),
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"    Use * to match all time series."),
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"    Use A* to match all time series with alias or location" +
		" starting with A."),
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"    Use *.*.XXXXX.*.* to match all time series with a data " +
		"type XXXXX."),
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"2. Specify in-memory time series positions:"),
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"    The first time series created is position 1."),
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"    Separate numbers by a comma.  Specify a range, for " +
		"example, as 1-3."),
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Time series alias/identifier:" ),
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	__TSID_JTextField = new JTextField ( "", 20 );
	__TSID_JTextField.addKeyListener ( this );
        JGUIUtil.addComponent(main_JPanel, __TSID_JTextField,
		1, y, 1, 1, 1, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Specify a TSID pattern." ),
		2, y, 3, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Time series position(s):" ),
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	__Pos_JTextField = new JTextField ( "", 8 );
	__Pos_JTextField.addKeyListener ( this );
        JGUIUtil.addComponent(main_JPanel, __Pos_JTextField,
		1, y, 1, 1, 1, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"For example, 1,2,7-8." ),
		2, y, 3, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);

	Vector select_all_first = new Vector ( 3 );
	select_all_first.addElement ( "" );
	select_all_first.addElement ( __False );
	select_all_first.addElement ( __True );
	if ( __select ) {
        	JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Deselect all first?:" ),
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
		__DeselectAllFirst_JComboBox = new SimpleJComboBox ( true );
		__DeselectAllFirst_JComboBox.setData ( select_all_first );
		__DeselectAllFirst_JComboBox.addItemListener ( this );
        	JGUIUtil.addComponent(main_JPanel, __DeselectAllFirst_JComboBox,
		1, y, 1, 1, 1, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        	JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Eliminates need for separate deselect."),
		3, y, 4, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
	}
	else {	JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Select all first?:" ),
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
		__SelectAllFirst_JComboBox = new SimpleJComboBox ( true );
		__SelectAllFirst_JComboBox.setData ( select_all_first );
		__SelectAllFirst_JComboBox.addItemListener ( this );
        	JGUIUtil.addComponent(main_JPanel, __SelectAllFirst_JComboBox,
		1, y, 1, 1, 1, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        	JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Eliminates need for separate select."),
		3, y, 4, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
	}

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

	setTitle ( "Edit " + __command + "() Command" );

	// Dialogs do not need to be resizable...
	setResizable ( true );
        pack();
        JGUIUtil.center( this );
        super.setVisible( true );
}

/**
Handle ItemEvent events.
@param e ItemEvent to handle.
*/
public void itemStateChanged ( ItemEvent event )
{	if ( event.getStateChange() != ItemEvent.SELECTED ) {
		return;
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
selectTimeSeries(TSID="x",Pos="x",DeselectAllFirst=X)
deselectTimeSeries(TSID="x",Pos="x",SelectAllFirst=X)
</pre>
*/
private void refresh ()
{	String routine = __command + ".refresh";
	String TSID = "";
	String Pos = "";
	String DeselectAllFirst = "";
	String SelectAllFirst = "";
	if ( __first_time ) {
		__first_time = false;
		__error_wait = false;
		// Parse the incoming string and fill the fields...
		Vector v = StringUtil.breakStringList (
			((String)__command_Vector.elementAt(0)).trim(),"()",
			StringUtil.DELIM_SKIP_BLANKS );
		PropList props = null;
		if ( (v != null) && (v.size() > 1) ) {
			props = PropList.parse (
				(String)v.elementAt(1), routine, "," );
		}
		else {	props = new PropList ( "selectTimeSeries" );
		}
		TSID = props.getValue ( "TSID" );
		Pos = props.getValue ( "Pos" );
		DeselectAllFirst = props.getValue ( "DeselectAllFirst" );
		SelectAllFirst = props.getValue ( "SelectAllFirst" );
		if ( TSID != null ) {
			__TSID_JTextField.setText ( TSID );
		}
		if ( Pos != null ) {
			__Pos_JTextField.setText ( Pos );
		}
		if ( __DeselectAllFirst_JComboBox != null ) {
		if ( DeselectAllFirst == null ) {
			// Select blank...
			__DeselectAllFirst_JComboBox.select ( 0 );
		}
		else {	if (	JGUIUtil.isSimpleJComboBoxItem(
				__DeselectAllFirst_JComboBox,
				DeselectAllFirst, JGUIUtil.NONE, null, null ) ){
				__DeselectAllFirst_JComboBox.select (
				DeselectAllFirst );
			}
			else {	Message.printWarning ( 1, routine,
				"Existing " + __command + "() references an " +
				"invalid\nDeselectAllFirst \"" + DeselectAllFirst +
				"\".  Select a different value or Cancel.");
				__error_wait = true;
			}
		}
		}
		if ( __SelectAllFirst_JComboBox != null ) {
		if ( SelectAllFirst == null ) {
			// Select blank...
			__SelectAllFirst_JComboBox.select ( 0 );
		}
		else {	if (	JGUIUtil.isSimpleJComboBoxItem(
				__SelectAllFirst_JComboBox,
				SelectAllFirst, JGUIUtil.NONE, null, null ) ){
				__SelectAllFirst_JComboBox.select (
				SelectAllFirst );
			}
			else {	Message.printWarning ( 1, routine,
				"Existing " + __command + "() references an " +
				"invalid\nSelectAllFirst \"" + SelectAllFirst +
				"\".  Select a different value or Cancel.");
				__error_wait = true;
			}
		}
		}
	}
	// Regardless, reset the command from the fields...
	TSID = __TSID_JTextField.getText().trim();
	Pos = __Pos_JTextField.getText().trim();
	if ( __SelectAllFirst_JComboBox != null ) {
		SelectAllFirst = __SelectAllFirst_JComboBox.getSelected();
	}
	if ( __DeselectAllFirst_JComboBox != null ) {
		DeselectAllFirst = __DeselectAllFirst_JComboBox.getSelected();
	}
	StringBuffer b = new StringBuffer ();
	if ( TSID.length() > 0 ) {
		b.append ( "TSID=\"" + TSID + "\"" );
	}
	if ( Pos.length() > 0 ) {
		if ( b.length() > 0 ) {
			b.append ( "," );
		}
		b.append ( "Pos=\"" + Pos + "\"" );
	}
	if ( (DeselectAllFirst != null) && (DeselectAllFirst.length() > 0) ) {
		if ( b.length() > 0 ) {
			b.append ( "," );
		}
		b.append ( "DeselectAllFirst=\"" + DeselectAllFirst + "\"" );
	}
	if ( (SelectAllFirst != null) && (SelectAllFirst.length() > 0) ) {
		if ( b.length() > 0 ) {
			b.append ( "," );
		}
		b.append ( "SelectAllFirst=" + SelectAllFirst );
	}
	__command_JTextField.setText(__command + "(" + b.toString() + ")" );
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

} // end selectTimeSeries_JDialog
