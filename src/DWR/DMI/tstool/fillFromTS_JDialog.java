// ----------------------------------------------------------------------------
// fillFromTS_JDialog - editor for fillFromTS()
// ----------------------------------------------------------------------------
// Copyright:	See the COPYRIGHT file.
// ----------------------------------------------------------------------------
// History: 
//
// 2002-03-30	Steven A. Malers, RTi	Initial version.  Copy and modify
//					fillMOVE2_Dialog().
// 2003-12-10	SAM, RTi		Update to Swing.
// 2004-02-22	SAM, RTi		Change independent list to single
//					selection.
// 2007-02-26	SAM, RTi		Clean up code based on Eclipse feedback.
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
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import RTi.Util.GUI.JGUIUtil;
import RTi.Util.GUI.SimpleJButton;
import RTi.Util.GUI.SimpleJComboBox;
import RTi.Util.GUI.SimpleJMenuItem;
import RTi.Util.Message.Message;
import RTi.Util.String.StringUtil;
import RTi.Util.Time.DateTime;

public class fillFromTS_JDialog extends JDialog
implements ActionListener, ItemListener, KeyListener, ListSelectionListener,
MouseListener, WindowListener
{

private String __CHANGE_TO_TEMPTS = "Change to TEMPTS";
private String __REMOVE_TEMPTS_FLAG = "Remove TEMPTS flag";

private SimpleJButton	__cancel_JButton = null,// Cancel Button
			__ok_JButton = null;	// Ok Button
private Vector		__command_Vector = null; // Command as Vector of String
private JTextField	__analysis_period_start_JTextField,
			__analysis_period_end_JTextField,
						// Text fields for analysis
						// period.
			__command_JTextField = null;
						// Command as JTextField
private SimpleJComboBox	__alias_JComboBox = null;// Field for time series alias
private JList		__independent_JList = null;
						// Field for independent time
						// series identifier
private DefaultListModel __independent_JListModel = null;
private JPopupMenu	__ts_JPopupMenu;	// Popup to convert a normal
						// time series to a temporary.
private boolean		__error_wait = false;
private boolean		__first_time = true;

/**
fillFromTS_JDialog constructor.
@param parent JFrame class instantiating this class.
@param command Command to parse.
@param tsids Time series identifiers for time series that can be used.
*/
public fillFromTS_JDialog ( JFrame parent, Vector command, Vector tsids )
{	super(parent, true);
	initialize ( parent, "Edit fillFromTS() Command", command, tsids );
}

/**
Responds to ActionEvents.
@param event ActionEvent object
*/
public void actionPerformed( ActionEvent event )
{	String s = event.getActionCommand();
	Object o = event.getSource();

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
	else if ( s.equals(__CHANGE_TO_TEMPTS) ) {
		// Make sure that all selected time series start with the
		// string "TEMPTS" but don't duplicate it...
		JGUIUtil.addStringToSelected(__independent_JList,"TEMPTS ");
		refresh();
	}
	else if ( s.equals(__REMOVE_TEMPTS_FLAG) ) {
		// If selected time series start with the string "TEMPTS" and
		// there is more than one token, remove the leading TEMPTS...
		JGUIUtil.removeStringFromSelected(
			__independent_JList,"TEMPTS ");
		refresh();
	}
}

/**
Check the user input for errors and set __error_wait accordingly.
*/
private void checkInput ()
{	String alias = __alias_JComboBox.getSelected();
	String independent = (String)__independent_JList.getSelectedValue ();
	String analysis_period_start =
		__analysis_period_start_JTextField.getText().trim();
	String analysis_period_end =
		__analysis_period_end_JTextField.getText().trim();
	String warning = "";

	if ( alias.equalsIgnoreCase(independent) ) {
		warning += "\nThe time series to fill \"" + alias +
			"\" is the same.\n"+
			"as the independent time series \"" +
			independent + "\".\n" + "Correct or Cancel.";
	}
	if (	!analysis_period_start.equals("*") &&
		!analysis_period_start.equalsIgnoreCase("OutputStart") ) {
		try {	DateTime.parse(analysis_period_start);
		}
		catch ( Exception e ) {
			warning += 
				"\nThe start date/time \"" +
				analysis_period_start +
				"\" is not a valid date/time.\n"+
				"Specify a date, *, OutputStart, or Cancel.";
		}
	}
	if (	!analysis_period_end.equals("*") &&
		!analysis_period_end.equalsIgnoreCase("OutputEnd") ) {
		try {	DateTime.parse(analysis_period_end);
		}
		catch ( Exception e ) {
			warning +=
				"The end date/time \"" + analysis_period_end +
				"\" is not a valid date/time.\n"+
				"Specify a date, *, OutputEnd, or Cancel.";
		}
	}
	if ( warning.length() > 0 ) {
		__error_wait = true;
		Message.printWarning ( 1, "fillFromTS_JDialog", warning );
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
	__independent_JList = null;
	__ok_JButton = null;
	__ts_JPopupMenu = null;
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
@param tsids Time series identifiers for time series that can be used.
*/
private void initialize (	JFrame parent, String title, Vector command,
				Vector tsids )
{	__command_Vector = command;

	addWindowListener( this );

	Insets insetsTLBR = new Insets(2,2,2,2);

	// Main panel...

try {
	JPanel main_JPanel = new JPanel();
	main_JPanel.setLayout( new GridBagLayout() );
	getContentPane().add ( "North", main_JPanel );
	int y = 0;

	// Main contents...

        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"fillFromTS() copies data values from the independent time" +
		" series to replace missing values in the"),
		0, y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"dependent time series.  Only data in the analysis period " +
		"will be checked."),
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Use a setOutputPeriod() command if the dependent time series" +
		" period will be extended." ), 
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Specify dates with precision appropriate for the data, " +
		"use * for all available data, OutputStart, or OutputEnd." ),
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Time Series to Fill (dependent):" ), 
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);

	__alias_JComboBox = new SimpleJComboBox ( false );
	int size = 0;
	if ( tsids != null ) {
		size = tsids.size();
	}
	if ( size == 0 ) {
		Message.printWarning ( 1, "fillFromTS_JDialog.initialize",
		"You must define time series before inserting a fillFromTS() "+
		"command." );
		response ( 0 );
	}
	__alias_JComboBox.setData ( tsids );
	__alias_JComboBox.addItemListener ( this );
        JGUIUtil.addComponent(main_JPanel, __alias_JComboBox,
		1, y, 6, 1, 1, 0, insetsTLBR, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Independent Time Series:" ), 
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	__independent_JListModel = new DefaultListModel();
	__independent_JList = new JList ( __independent_JListModel );
	__independent_JList.setSelectionMode (
		ListSelectionModel.SINGLE_SELECTION );
	for ( int i = 0; i < size; i++ ) {
		__independent_JListModel.addElement((String)tsids.elementAt(i));
	} 
	__independent_JList.addListSelectionListener ( this );
	__independent_JList.addKeyListener ( this );
	__independent_JList.addMouseListener ( this );
        JGUIUtil.addComponent(main_JPanel, new JScrollPane(__independent_JList),
		1, y, 6, 1, 1, 0, insetsTLBR, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);

	JGUIUtil.addComponent(main_JPanel, new JLabel ( "Analysis Period:" ), 
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	__analysis_period_start_JTextField = new JTextField ( "*", 15 );
	__analysis_period_start_JTextField.addKeyListener ( this );
	JGUIUtil.addComponent(main_JPanel, __analysis_period_start_JTextField,
		1, y, 2, 1, 1, 0, insetsTLBR, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
	JGUIUtil.addComponent(main_JPanel, new JLabel ( "to" ), 
		3, y, 2, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.CENTER);
	__analysis_period_end_JTextField = new JTextField ( "*", 15 );
	__analysis_period_end_JTextField.addKeyListener ( this );
	JGUIUtil.addComponent(main_JPanel, __analysis_period_end_JTextField,
		5, y, 2, 1, 1, 0, insetsTLBR, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel ( "Command:" ), 
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);

	__command_JTextField = new JTextField ( 60 );
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

	// Add the PopupMenu...

	// Pop-up menu to manipulate time series...
	__ts_JPopupMenu = new JPopupMenu("TS Actions");
	__ts_JPopupMenu.add( new SimpleJMenuItem (
		__CHANGE_TO_TEMPTS, __CHANGE_TO_TEMPTS, this ) );
	__ts_JPopupMenu.add( new SimpleJMenuItem (
		__REMOVE_TEMPTS_FLAG, __REMOVE_TEMPTS_FLAG, this ) );

	if ( title != null ) {
		setTitle ( title );
	}
	setResizable ( true );
        pack();
        JGUIUtil.center( this );
        super.setVisible( true );
}
catch ( Exception e ) {
	Message.printWarning ( 2, "", e );
}
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
		// Only check the format of dates if enter is pressed...
		refresh ();
		checkInput();
		if ( !__error_wait ) {
			response ( 1 );
		}
	}
}

public void keyReleased ( KeyEvent event )
{	//refresh();
}

public void keyTyped ( KeyEvent event )
{
}

/**
Handle mouse clicked event.
*/
public void mouseClicked ( MouseEvent event )
{
}

/**
Handle mouse entered event.
*/
public void mouseEntered ( MouseEvent event )
{
}

/**
Handle mouse exited event.
*/
public void mouseExited ( MouseEvent event )
{
}

/**
Handle mouse pressed event.
*/
public void mousePressed ( MouseEvent event )
{	int mods = event.getModifiers();
	if ( (mods & MouseEvent.BUTTON3_MASK) != 0 ) {
		__ts_JPopupMenu.show (
		event.getComponent(), event.getX(), event.getY() );
	}
}

/**
Handle mouse released event.
*/
public void mouseReleased ( MouseEvent event )
{
}

/**
Refresh the command from the other text field contents.
The command is like:
<pre>
fillFromTS(Dependent,Independent,AnalysisStart,AnalysisEnd)
</pre>
*/
private void refresh ()
{	String alias = "";
	String independent = "";
	String analysis_period_start = "";
	String analysis_period_end = "";
	__error_wait = false;
	boolean found_ts = false;
	String command = "fillFromTS";
	String temp = null;
	int pos = 0;
	if ( __first_time ) {
		__first_time = false;
		// Parse the incoming string and fill the fields...
		Vector v = StringUtil.breakStringList (
			((String)__command_Vector.elementAt(0)).trim(),"(),",
			StringUtil.DELIM_SKIP_BLANKS );
		if ( (v != null) && (v.size() > 0) ) {
			command = ((String)v.elementAt(0)).trim();
		}
		// Parse out the command values...
		if ( (v != null) && (v.size() >= 5) ) {
			// New syntax...
			alias = ((String)v.elementAt(1)).trim();
			independent = ((String)v.elementAt(2)).trim();
			analysis_period_start = ((String)v.elementAt(3)).trim();
			analysis_period_end = ((String)v.elementAt(4)).trim();
			if (	JGUIUtil.isSimpleJComboBoxItem(
				__alias_JComboBox, alias,
				JGUIUtil.NONE, null, null ) ) {
				__alias_JComboBox.select ( alias );
			}
			else {	Message.printWarning ( 1,
				"fillFromTS_JDialog.refresh", "Existing " +
				command + "() references a non-existent\n"+
				"time series \"" + alias + "\".  Select a\n" +
				"different time series or Cancel." );
			}
			if (	JGUIUtil.indexOf( __independent_JList,
				independent, false, true) >= 0 ) {
				// Select it...
				JGUIUtil.select ( __independent_JList,
					independent, true );
			}
			else if ( independent.regionMatches(
				true,0,"TEMPTS",0,6) ) {
				// The time series is a TSTEMP so look
				// for the rest of the time series in
				// the list.  If it exists, convert to
				// TSTEMP to match the command.  If not
				// add to the list as a TSTEMP to match
				// the command.
				temp =	StringUtil.getToken(
					independent, " ",
					StringUtil.DELIM_SKIP_BLANKS,1);
				if ( temp != null ) {
					temp = temp.trim();
					pos =	JGUIUtil.indexOf(
						__independent_JList,
						temp,false,true);
					if ( (pos >=0) ) {
						temp = "TEMPTS " + temp;
						__independent_JListModel.
						setElementAt( temp, pos );
						JGUIUtil.select (
						__independent_JList,
						temp, true );
						found_ts = true;
					}
				}
				if ( !found_ts ) {
					// Probably not in the original
					// list so add to the bottom.
					// The TEMPTS is already at the
					// front of the independent TS..
					__independent_JListModel.addElement (
							independent);
					JGUIUtil.select (
						__independent_JList,
						independent, true );
				}
			}
			else {	Message.printWarning ( 1,
				"fillFromTS_JDialog.refresh", "Existing " +
				command + "() references a non-existent\n"+
				"time series \"" + independent +
				"\".  Select a\n" +
				"different time series or Cancel." );
			}
			__analysis_period_start_JTextField.setText (
				analysis_period_start );
			__analysis_period_end_JTextField.setText (
				analysis_period_end );
		}
		else {	// First time and a new command that is blank.
			// To prevent a warning, select the first item in the
			// alias and the second in the independent, if more than
			// one in the list...
			if ( __alias_JComboBox.getItemCount() > 0 ) {
				__alias_JComboBox.select ( 0 );
			}
			if ( __independent_JListModel.size() > 1 ) {
				__independent_JList.setSelectedIndex(1);
			}
			else if ( __independent_JListModel.size() > 0 ) {
				__independent_JList.setSelectedIndex(0);
			}
			__analysis_period_start_JTextField.setText ( "*" );
			__analysis_period_end_JTextField.setText ( "*" );
		}
	}
	// Regardless, reset the command from the fields...
	alias = __alias_JComboBox.getSelected();
	independent = (String)__independent_JList.getSelectedValue();
	analysis_period_start=
		__analysis_period_start_JTextField.getText().trim();
	analysis_period_end = __analysis_period_end_JTextField.getText().trim();
	if ( (alias == null) || (alias.trim().length() == 0) ) {
		return;
	}
	if ( (independent == null) || (independent.trim().length() == 0) ) {
		return;
	}
	__command_JTextField.setText(command+"(" + alias + "," +
		independent + "," + analysis_period_start + "," +
		analysis_period_end + ")" );
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
Handle ListSelectionListener events.
*/
public void valueChanged ( ListSelectionEvent e )
{	refresh ();
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

} // end fillFromTS_JDialog
