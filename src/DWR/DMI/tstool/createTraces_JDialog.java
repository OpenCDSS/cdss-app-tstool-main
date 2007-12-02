// ----------------------------------------------------------------------------
// createTraces_JDialog - editor for createTraces()
// ----------------------------------------------------------------------------
// Copyright:	See the COPYRIGHT file.
// ----------------------------------------------------------------------------
// History: 
//
// 20 Mar 2001	Steven A. Malers, RTi	Initial version.  Copy add_Dialog and
//					modify.
// 2002-04-23	SAM, RTi		Clean up dialog.
// 2003-12-16	SAM, RTi		Update to Swing.
// 2004-02-22	SAM, RTi		Change independent TS list to single
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

import javax.swing.DefaultListModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import java.util.Vector;

import RTi.Util.GUI.JGUIUtil;
import RTi.Util.GUI.SimpleJComboBox;
import RTi.Util.GUI.SimpleJMenuItem;
import RTi.Util.GUI.SimpleJButton;
import RTi.Util.Message.Message;
import RTi.Util.String.StringUtil;
import RTi.Util.Time.DateTime;
import RTi.Util.Time.TimeInterval;

public class createTraces_JDialog extends JDialog
implements ActionListener, ItemListener, KeyListener, ListSelectionListener,
MouseListener, WindowListener
{
private String __CHANGE_TO_TEMPTS = "Change to TEMPTS";
private String __REMOVE_TEMPTS_FLAG = "Remove TEMPTS flag";

private String __SHIFT_NONE = "NoShift";
private String __SHIFT_TO_REFERENCE = "ShiftToReference";

private SimpleJButton	__cancel_JButton = null,// Cancel Button
			__ok_JButton = null;	// Ok Button
private Vector		__command_Vector = null;// Command as Vector of String
private JTextField	__command_JTextField=null;// Command as JTextField
private SimpleJComboBox	__shift_JComboBox = null;// Indicates how to handle
						// shift.
private JTextField	__reference_JTextField = null;
						// Reference date.
private JTextField	__period_JTextField=null; // Total period length.
private DefaultListModel __independent_JListModel = null;
private JList		__independent_JList =null;// Field for independent time
						// series identifiers
private JPopupMenu	__ts_JPopupMenu;	// Popup to convert a normal
						// time series to a temporary.
private boolean		__error_wait = false;
private boolean		__first_time = true;

/**
createTraces_JDialog constructor.
@param parent JFrame class instantiating this class.
@param command Command to parse.
@param tsids Time series identifiers for time series that can be used.
*/
public createTraces_JDialog ( JFrame parent, Vector command, Vector tsids )
{	super(parent, true);
	initialize ( parent, "Edit CreateTraces() Command", command, tsids );
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
{	// TODO SAM
	//String independent = (String)__independent_JList.getSelectedValue ();
	String reference = __reference_JTextField.getText().trim();
	String shift_type = __shift_JComboBox.getSelected();
	String period = __period_JTextField.getText().trim();
	String warning = "";
	if ( !reference.equals("") ) {
		try {	DateTime.parse ( reference );
		}
		catch ( Exception e ) {
			warning +=
				"\nThe reference date/time \"" +
				reference + "\" is invalid.\n" +
				"Specify a valid date/time or Cancel.";
		}
	}
	// Check the period length...
	try {	TimeInterval.parseInterval ( period );
	}
	catch ( Exception e ) {
		warning +=
			"\nThe shift type \"" + shift_type +"\" is invalid.\n" +
			"Specify a valid type or Cancel.";
	}
	if ( warning.length() > 0 ) {
		__error_wait = true;
		Message.printWarning ( 1, "createTraces_JDialog", warning );
	}
}

/**
Free memory for garbage collection.
*/
protected void finalize ()
throws Throwable
{	__cancel_JButton = null;
	__command_JTextField = null;
	__reference_JTextField = null;
	__period_JTextField = null;
	__command_Vector = null;
	__independent_JList = null;
	__independent_JListModel = null;
	__shift_JComboBox = null;
	__ok_JButton = null;
	__ts_JPopupMenu = null;
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
		"Convert a time series to a sequence of traces (typically one trace per year)."),
		0, y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Each trace will start on the reference date and will be as long as specified."),
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Each trace will have the properties of the original time series with unique sequence numbers."),
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Specify the reference date using standard date formats to a precision appropriate for the data."),
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"If shifted, each trace will start on the reference date (use to overlay plots)."),
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);

        JGUIUtil.addComponent(main_JPanel,
		new JLabel ( "Time series from which to create traces:"),
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	int size = 0;
	if ( tsids != null ) {
		size = tsids.size();
	}
	String e = "";
	if ( (command != null) && (command.size() > 0) ) {
		e = (String)command.elementAt(0);
	}
	if ( (size == 0) && (e.indexOf("TEMPTS") < 0) ) {
		// No time series to choose or the existing command does not
		// use a TEMPTS.
		Message.printWarning ( 1, "createTraces_JDialog.initialize",
		"You must define time series before inserting a CreateTraces() command." );
		response ( 0 );
	}
	__independent_JListModel = new DefaultListModel();
	for ( int i = 0; i < size; i++ ) {
		__independent_JListModel.addElement((String)tsids.elementAt(i));
	}
	__independent_JList = new JList ( __independent_JListModel );
	__independent_JList.setSelectionMode ( ListSelectionModel.SINGLE_SELECTION );
	__independent_JList.addListSelectionListener ( this );
	__independent_JList.addKeyListener ( this );
	__independent_JList.addMouseListener ( this );
        JGUIUtil.addComponent(main_JPanel, new JScrollPane(__independent_JList),
		1, y, 6, 1, 1, 0, insetsTLBR, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST );

        JGUIUtil.addComponent(main_JPanel,
		new JLabel ( "Trace Length (blank=1Year):" ), 
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	__period_JTextField = new JTextField ( "1Year", 10 );
	__period_JTextField.addKeyListener ( this );
	JGUIUtil.addComponent(main_JPanel, __period_JTextField,
		1, y, 2, 1, 1, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Reference Date (blank=Jan 1):" ), 
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	__reference_JTextField = new JTextField ( 10 );
	__reference_JTextField.addKeyListener(this);
	JGUIUtil.addComponent(main_JPanel, __reference_JTextField,
		1, y, 2, 1, 1, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Shift Data How?:" ), 
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	__shift_JComboBox = new SimpleJComboBox ( false );
	__shift_JComboBox.addItem ( __SHIFT_NONE );
	__shift_JComboBox.addItem ( __SHIFT_TO_REFERENCE );
	__shift_JComboBox.addItemListener ( this );
        JGUIUtil.addComponent(main_JPanel, __shift_JComboBox,
		1, y, 2, 1, 1, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel ( "Command:" ),
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	__command_JTextField = new JTextField ( 55 );
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
	__ts_JPopupMenu.add( new SimpleJMenuItem ( __CHANGE_TO_TEMPTS, this ) );
	__ts_JPopupMenu.add( new SimpleJMenuItem(__REMOVE_TEMPTS_FLAG, this ) );

	// Visualize it...

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
createTraces(Alias,Length,ReferenceDate,ShiftType)
</pre>
*/
private void refresh ()
{	String independent = "";
	String shift_type = "";
	String reference = "";
	String period = "";
	__error_wait = false;
	if (	__first_time ) {
		__first_time = false;
		// Parse the incoming string and fill the fields...
		// Trim spaces later and do not ignore blanks since some fields
		// are optional...
		Vector v = StringUtil.breakStringList (
			((String)__command_Vector.elementAt(0)).trim(),"(),",
			0 );
		// Parse out the command values...
		if ( (v != null) && (v.size() >= 3) ) {
			independent = ((String)v.elementAt(1)).trim();
			// Check all the items in the list and highlight the
			// ones that match the command being edited...
			String temp = null;
			boolean found_ts = false;
			int pos = 0;
			if (	JGUIUtil.indexOf( __independent_JList,
				independent, false, true ) >= 0 ) {
				// Select it because it is in the
				// command and the list...
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
						__independent_JListModel
						.setElementAt(temp,pos);
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
					__independent_JListModel.addElement(independent);
					JGUIUtil.select (
						__independent_JList,
						independent, true );
				}
			}
			else {	Message.printWarning ( 1,
				"createTraces_JDialog.refresh", "Existing " +
				"createTraces() references a non-existent\n"+
				"time series \"" + independent +
				"\".  Select a\n" +
				"different time series or Cancel." );
			}

			period = StringUtil.unpad(
					((String)v.elementAt(2)).trim(), " \t",
					StringUtil.PAD_FRONT_MIDDLE_BACK );
			__period_JTextField.setText ( period );
		}
		if ( (v != null) && (v.size() >= 5) ) {
			reference = ((String)v.elementAt(3)).trim();
			shift_type = ((String)v.elementAt(4)).trim();
			// Check the reference date...
			if ( !reference.equals("") ) {
				__reference_JTextField.setText ( reference );
			}
			// Check the shift type...
			if (	JGUIUtil.isSimpleJComboBoxItem(
				__shift_JComboBox, shift_type,
				JGUIUtil.NONE, null, null ) ) {
				__shift_JComboBox.select ( shift_type );
			}
			else {	Message.printWarning ( 1,
				"createTraces_JDialog.refresh", "Existing " +
				"CreateTraces() references an invalid\n"+
				"shift type \"" + shift_type +
				"\".  Select a\n" +
				"different type or Cancel." );
			}
		}
		else {	// First time and a new command that is blank.
			// To prevent a warning, select the first item in the
			// alias and the second in the independent, if more than
			// one in the list...
			if ( __independent_JListModel.size() > 1 ) {
				__independent_JList.setSelectedIndex(1);
			}
			else if ( __independent_JListModel.size() > 0 ) {
				__independent_JList.setSelectedIndex(0);
			}
		}
	}
	// Regardless, reset the command from the fields...
	independent = (String)__independent_JList.getSelectedValue ();
	shift_type = __shift_JComboBox.getSelected();
	reference = __reference_JTextField.getText().trim();
	period = StringUtil.unpad ( __period_JTextField.getText().trim(), " \t",
					StringUtil.PAD_FRONT_MIDDLE_BACK );
	__period_JTextField.setText ( period );
	if ( (independent == null) || (independent.trim().length() == 0) ) {
		return;
	}
	if ( JGUIUtil.selectedSize(__independent_JList) == 0 ) {
		return;
	}
	if ( (shift_type == null) || (shift_type.trim().length() == 0) ) {
		return;
	}
	__command_JTextField.setText( "CreateTraces(" + independent + "," +
			period + "," + reference + "," + shift_type + ")" );
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

public void windowActivated( WindowEvent evt ){;}
public void windowClosed( WindowEvent evt ){;}
public void windowDeactivated( WindowEvent evt ){;}
public void windowDeiconified( WindowEvent evt ){;}
public void windowIconified( WindowEvent evt ){;}
public void windowOpened( WindowEvent evt ){;}

} // end createTraces_JDialog
