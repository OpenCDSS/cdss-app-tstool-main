// ----------------------------------------------------------------------------
// disaggregate_JDialog - editor for disaggregate()
// ----------------------------------------------------------------------------
// Copyright:	See the COPYRIGHT file.
// ----------------------------------------------------------------------------
// History: 
//
// 2002-04-15	Steven A. Malers, RTi	Initial version.  Copy
//					changeInterval_Dialog and modify.
// 2002-05-08	SAM, RTi		Add "SameValue" method.
// 2003-12-16	SAM, RTi		Update to Swing.
// 2004-02-22	SAM, RTi		Change original time series list to
//					single selection.
// ----------------------------------------------------------------------------

package DWR.DMI.tstool;

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
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
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

import RTi.Util.GUI.JGUIUtil;
import RTi.Util.GUI.SimpleJButton;
import RTi.Util.GUI.SimpleJComboBox;
import RTi.Util.GUI.SimpleJMenuItem;
import RTi.Util.Message.Message;
import RTi.Util.String.StringUtil;
import RTi.Util.Time.DateTime;
import RTi.Util.Time.TimeInterval;

public class disaggregate_JDialog extends JDialog
implements ActionListener, ItemListener, KeyListener, ListSelectionListener,
MouseListener, WindowListener
{
private String __CHANGE_TO_TEMPTS = "Change to TEMPTS";
private String __REMOVE_TEMPTS_FLAG = "Remove TEMPTS flag";

private String __METHOD_ORMSBEE = "Ormsbee";
private String __METHOD_SAME_VALUE = "SameValue";

private SimpleJButton	__cancel_JButton = null,// Cancel Button
			__ok_JButton = null;	// Ok Button
private JFrame		__parent_JFrame = null;	// parent JFrame
private Vector		__command_Vector = null; // Command as Vector of String
private JTextField	__alias_JTextField = null;// Alias for new time series
private JTextField	__command_JTextField=null;// Command as JTextField
private SimpleJComboBox	__method_JComboBox = null;// Disaggregation method.
private JTextField	__interval_JTextField = null; // New interval.
private JTextField	__datatype_JTextField = null; // New data type.
private JTextField	__units_JTextField = null;// New data units.
private DefaultListModel __independent_JListModel = null;
private JList		__independent_JList =null;// Field for independent time
						// series identifiers
private JPopupMenu	__ts_JPopupMenu	= null;	// Popup to convert a normal
						// time series to a temporary.
private boolean		__error_wait = false;
private boolean		__first_time = true;

/**
disaggregate_JDialog constructor.
@param parent JFrame class instantiating this class.
@param command Command to parse.
@param tsids Time series identifiers for time series that can be used.
*/
public disaggregate_JDialog ( JFrame parent, Vector command, Vector tsids )
{	super(parent, true);
	initialize ( parent, "Edit disaggregate() Command", command, tsids );
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
{	String alias = __alias_JTextField.getText().trim();
	String interval = __interval_JTextField.getText().trim();
	String warning = "";
	String independent = (String)__independent_JList.getSelectedValue ();
	if ( alias.length() == 0 ) {
		warning +=
			"\nThe alias must be specified.";
	}
	if ( alias.equalsIgnoreCase(independent)) {
		warning +=
			"\nTime series to receive disaggregate \"" + alias +
			"\" is the same\nas the time series being " +
			"disaggregated.\nCorrect or Cancel.";
	}
	try {	TimeInterval.parseInterval(interval);
	}
	catch ( Exception e ) {
		warning +=
			"\nThe interval \"" + interval + "\" is not valid.";
	}
	if ( warning.length() > 0 ) {
		__error_wait = true;
		Message.printWarning ( 1, "disaggregate_JDialog", warning );
	}
}

/**
Free memory for garbage collection.
*/
protected void finalize ()
throws Throwable
{	__cancel_JButton = null;
	__alias_JTextField = null;
	__command_JTextField = null;
	__command_Vector = null;
	__independent_JList = null;
	__independent_JListModel = null;
	__interval_JTextField = null;
	__datatype_JTextField = null;
	__units_JTextField = null;
	__method_JComboBox = null;
	__ok_JButton = null;
	__parent_JFrame = null;
	__ts_JPopupMenu = null;
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
@param tsids Time series identifiers for time series that can be used.
*/
private void initialize ( JFrame parent, String title, Vector command,
		Vector tsids )
{	__parent_JFrame = parent;
	__command_Vector = command;

	addWindowListener( this );

        Insets insetsTLBR = new Insets(2,2,2,2);

	// Main panel...

	JPanel main_JPanel = new JPanel();
	main_JPanel.setLayout( new GridBagLayout() );
	GridBagConstraints gbc = new GridBagConstraints();
	getContentPane().add ( "North", main_JPanel );
	int y = 0;

        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Disaggregation converts a longer-interval time series" +
		" to a shorter interval."),
		0, y, 7, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Specify the new interal as Nbase, where base is Minute, " +
		"Hour, Day (ommitting N assumes a value of 1)."),
		0, ++y, 7, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"The new interval must be evenly divisible into the old " +
		" interval."),
		0, ++y, 7, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Specify the new data type as * to use the same data type" +
		" as the original time series."),
		0, ++y, 7, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Specify the new data units as * to use the same units" +
		" as the original time series."),
		0, ++y, 7, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"The resulting identifier will use the new interval (" +
		"and data type if specified)."),
		0, ++y, 7, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"The Ormsbee method is only enabled to disaggregate 1Day to " +
		"6Hour time series."),
		0, ++y, 7, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"The SameValue method can disaggregate Year->Month, Month->Day"+
		" Day->NHour, Hour->NMinute."),
		0, ++y, 7, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel ( "Time Series Alias:" ), 
		0, ++y, 1, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.EAST);
	__alias_JTextField = new JTextField ( "" );
	__alias_JTextField.addKeyListener ( this );
        JGUIUtil.addComponent(main_JPanel, __alias_JTextField,
		1, y, 6, 1, 1, 0, insetsTLBR, gbc.HORIZONTAL, gbc.WEST);

        JGUIUtil.addComponent(main_JPanel,
		new JLabel ( "Original Time Series:"),
		0, ++y, 1, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.EAST);

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
		Message.printWarning ( 1, "disaggregate_JDialog.initialize",
		"You must define time series before inserting a" +
		" disaggregate() command." );
		response ( 0 );
	}
	__independent_JListModel = new DefaultListModel();
	for ( int i = 0; i < size; i++ ) {
		__independent_JListModel.addElement((String)tsids.elementAt(i));
	}
	__independent_JList = new JList ( __independent_JListModel );
	__independent_JList.setSelectionMode (
		ListSelectionModel.SINGLE_SELECTION );
	__independent_JList.addListSelectionListener ( this );
	__independent_JList.addKeyListener ( this );
	__independent_JList.addMouseListener ( this );
        JGUIUtil.addComponent(main_JPanel, new JScrollPane(__independent_JList),
		1, y, 6, 1, 1, 0, insetsTLBR, gbc.HORIZONTAL, gbc.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel ( "Method:" ), 
		0, ++y, 1, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.EAST);
	__method_JComboBox = new SimpleJComboBox ( false );
	__method_JComboBox.addItem ( __METHOD_ORMSBEE );
	__method_JComboBox.addItem ( __METHOD_SAME_VALUE );
	__method_JComboBox.select ( 1 );
	__method_JComboBox.addItemListener ( this );
        JGUIUtil.addComponent(main_JPanel, __method_JComboBox,
		1, y, 2, 1, 1, 0, insetsTLBR, gbc.NONE, gbc.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel ( "New Interval:" ), 
		0, ++y, 1, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.EAST);
	__interval_JTextField = new JTextField ( "", 20);
	__interval_JTextField.addKeyListener ( this );
        JGUIUtil.addComponent(main_JPanel, __interval_JTextField,
		1, y, 2, 1, 1, 0, insetsTLBR, gbc.NONE, gbc.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel ( "New Data Type:" ), 
		0, ++y, 1, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.EAST);
	__datatype_JTextField = new JTextField ( "*", 20);
	__datatype_JTextField.addKeyListener ( this );
        JGUIUtil.addComponent(main_JPanel, __datatype_JTextField,
		1, y, 2, 1, 1, 0, insetsTLBR, gbc.NONE, gbc.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel ( "New Data Units:" ), 
		0, ++y, 1, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.EAST);
	__units_JTextField = new JTextField ( "*", 20);
	__units_JTextField.addKeyListener ( this );
        JGUIUtil.addComponent(main_JPanel, __units_JTextField,
		1, y, 2, 1, 1, 0, insetsTLBR, gbc.NONE, gbc.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel ( "Command:" ), 
		0, ++y, 1, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.EAST);
	__command_JTextField = new JTextField ( 60 );
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

	// Add the PopupMenu...

	// Pop-up menu to manipulate time series...
	__ts_JPopupMenu = new JPopupMenu("TS Actions");
	__ts_JPopupMenu.add( new SimpleJMenuItem ( __CHANGE_TO_TEMPTS, this ) );
	__ts_JPopupMenu.add( new SimpleJMenuItem ( __REMOVE_TEMPTS_FLAG, this));

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

	if ( code == event.VK_ENTER ) {
		refresh ();
		checkInput();
		if ( !__error_wait ) {
			response ( 1 );
		}
	}
}

public void keyReleased ( KeyEvent event )
{	int code = event.getKeyCode();
	refresh();
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
disaggregate(Alias,Method,NewInterval,NewDataType,NewUnits)
</pre>
*/
private void refresh ()
{	String alias = "";
	String independent = "";
	String interval = "";
	String method = "";
	String datatype = "";
	String units = "";
	__error_wait = false;
	if (	__first_time ) {
		__first_time = false;
		// Parse the incoming string and fill the fields...
		// Trim spaces later and do not ignore blanks since some fields
		// are optional...
		Vector v = StringUtil.breakStringList (
			((String)__command_Vector.elementAt(0)).trim(),"=(),",
			0 );
		// First get the alias...
		if ( (v != null) && (v.size() >= 1) ) {
			// Get the alias...
			alias = StringUtil.getToken((String)v.elementAt(0),
				" \t", StringUtil.DELIM_SKIP_BLANKS,1 );
			if ( alias == null ) {
				Message.printWarning ( 1,
				"disaggregate_JDialog.refresh",
				"Existing disaggregate() does not specify" +
				" the time series alias." );
				alias = "";
			}
			__alias_JTextField.setText ( alias );
		}
		// Now get the remaining parameters...
		if ( (v != null) && (v.size() >= 4) ) {
			independent = ((String)v.elementAt(2)).trim();
			// Check all the items in the list and highlight the
			// ones that match the command being edited...
			int size = v.size();
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
					__independent_JListModel.addElement(
							independent);
					JGUIUtil.select (
						__independent_JList,
						independent, true );
				}
			}
			else {	Message.printWarning ( 1,
				"disaggregate_JDialog.refresh", "Existing " +
				"disaggregate() references a non-existent\n"+
				"time series \"" + independent +
				"\".  Select a\n" +
				"different time series or Cancel." );
			}
			// Method...
			method = ((String)v.elementAt(3)).trim();
			if (	JGUIUtil.isSimpleJComboBoxItem(
				__method_JComboBox,
				method, JGUIUtil.NONE, null, null ) ) {
				__method_JComboBox.select ( method );
			}
			else {	Message.printWarning ( 1,
				"disaggregate_JDialog.refresh", "Existing " +
				"disaggregate() references an invalid\n"+
				"method \"" + method + "\".  Select a\n" +
				"different method or Cancel." );
			}
			// New data interval...
			interval = ((String)v.elementAt(4)).trim();
			// Check the interval
			/* For later...
			TSInterval tsi = TSInterval.parseInterval ( period );
			if ( tsi.getBase() == 0 ) {
				Message.printWarning ( 1,
				"disaggregate_JDialog.refresh", "Existing " +
				"disaggregate() references an invalid\n"+
				"shift type \"" + shift_type +
				"\".  Select a\n" +
				"different type or Cancel." );
			}
			tsi = null;
			*/
			__interval_JTextField.setText ( interval );
			// New data type...
			datatype = ((String)v.elementAt(5)).trim();
			__datatype_JTextField.setText(datatype);
			// New units...
			units = ((String)v.elementAt(6)).trim();
			__units_JTextField.setText(units);
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
	alias = __alias_JTextField.getText().trim();
	independent = (String)__independent_JList.getSelectedValue();
	method = __method_JComboBox.getSelected();
	interval = __interval_JTextField.getText().trim();
	datatype = __datatype_JTextField.getText().trim();
	units = __units_JTextField.getText().trim();
	if ( (alias == null) || (alias.trim().length() == 0) ) {
		return;
	}
	if ( (independent == null) || (independent.trim().length() == 0) ) {
		return;
	}
	if ( (interval == null) || (interval.trim().length() == 0) ) {
		return;
	}
	__command_JTextField.setText( "TS " + alias + " = disaggregate(" +
			independent + "," + method + "," + interval +
			"," + datatype + "," + units + ")" );
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

} // end disaggregate_JDialog
