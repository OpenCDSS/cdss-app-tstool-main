// ----------------------------------------------------------------------------
// add_JDialog - editor for add()
// ----------------------------------------------------------------------------
// Copyright:	See the COPYRIGHT file.
// ----------------------------------------------------------------------------
// History: 
//
// 02 Jan 2000	Steven A. Malers, RTi	Initial version.
// 15 Jan 2001	SAM, RTi		Add PopUp menu to allow time series to
//					be converted to a temporary.
// 22 Feb 2001	SAM, RTi		Add option to handle missing data.  This
//					also allows new and old commands to
//					be differentiated.
// 2002-04-23	SAM, RTi		Clean up.
// 2003-12-15	SAM, RTi		Update to Swing.
// 2004-01-29	SAM, RTi		Allow * as the time series to add.
// 2004-07-11	SAM, RTi		* Convert to new free-format parameter
//					  notation.
//					* Add TSList property to indicate how
//					  to get time series.
// 2004-08-02	SAM, RTi		The HandleMissingHow parameter was not
//					getting output in the command.
// 2004-11-12	SAM, RTi		Fix typo in notes.
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

import java.util.Vector;

import RTi.Util.GUI.JGUIUtil;
import RTi.Util.GUI.SimpleJComboBox;
import RTi.Util.GUI.SimpleJMenuItem;
import RTi.Util.GUI.SimpleJButton;
import RTi.Util.IO.PropList;
import RTi.Util.Message.Message;
import RTi.Util.String.StringUtil;

public class add_JDialog extends JDialog
implements ActionListener, ItemListener, KeyListener, ListSelectionListener,
MouseListener, WindowListener
{
// Use with TSList parameter
private final String __SelectedTS = "SelectedTS";
private final String __SpecifiedTS = "SpecifiedTS";
private final String __AllTS = "AllTS";

private String __CHANGE_TO_TEMPTS = "Change to TEMPTS";
private String __REMOVE_TEMPTS_FLAG = "Remove TEMPTS flag";

private String __IGNORE_MISSING = "IgnoreMissing";
private String __SET_MISSING_IF_OTHER_MISSING = "SetMissingIfOtherMissing";
private String __SET_MISSING_IF_ANY_MISSING = "SetMissingIfAnyMissing";

private SimpleJButton	__cancel_JButton = null,// Cancel Button
			__ok_JButton = null;	// Ok Button
private Vector		__command_Vector = null; // Command as Vector of String
private JTextField	__command_JTextField=null;// Command as JTextField
private SimpleJComboBox	__TSID_JComboBox = null;// Field for time series ID
private SimpleJComboBox	__TSList_JComboBox = null;
						// Indicate how to get time
						// series list.
private DefaultListModel __AddTSID_JListModel = null;
private JList		__AddTSID_JList= null;
						// Field for independent time
						// series identifiers
private SimpleJComboBox	__HandleMissingHow_JComboBox = null;
						// Indicates how to handle
						// missing data.
private JPopupMenu	__ts_JPopupMenu;	// Popup to convert a normal
						// time series to a temporary.
private boolean		__error_wait = false;
private boolean		__first_time = true;
private String		__command = null;	// Command to edit.

/**
add_JDialog constructor.
@param parent JFrame class instantiating this class.
@param command Command to parse.
@param is_add If true, then an add() command is being edited.  Otherwise,
a subtract() command.
@param tsids Time series identifiers for time series that can be used.
*/
public add_JDialog ( JFrame parent, Vector command, boolean is_add, Vector tsids )
{	super(parent, true);
	initialize ( parent, command, is_add, tsids );
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
		JGUIUtil.addStringToSelected(__AddTSID_JList,"TEMPTS ");
		refresh();
	}
	else if ( s.equals(__REMOVE_TEMPTS_FLAG) ) {
		// If selected time series start with the string "TEMPTS" and
		// there is more than one token, remove the leading TEMPTS...
		JGUIUtil.removeStringFromSelected(
			__AddTSID_JList,"TEMPTS ");
		refresh();
	}
}

/**
Check the user input for errors and set __error_wait accordingly.
*/
private void checkInput ()
{	String TSID = __TSID_JComboBox.getSelected();
	String TSList = __TSList_JComboBox.getSelected();
	String warning = "";
	if (	TSList.equalsIgnoreCase ( __SpecifiedTS) &&
		(JGUIUtil.indexOf(__AddTSID_JList, TSID, true, true) >= 0) ) {
		if ( __command.equalsIgnoreCase("add") ) {
			warning +=
			"\nTime series to receive sum \"" + TSID +
			"\" is the same.\nas a time series to be added.\n" +
			"Correct or Cancel.";
		}
		else {	warning +=
			"\nTime series to receive difference \"" + TSID +
			"\" is the same.\nas a time series to be " +
			"subtracted.\n" +
			"Correct or Cancel.";
		}
	}
	if ( !TSList.equalsIgnoreCase ( __SpecifiedTS) ) {
		int [] selected = __AddTSID_JList.getSelectedIndices();
		if ( (selected != null) && (selected.length > 0) ) {
			warning +=
			"\nTS List of \"" + TSList +
			"\" does not require list selections.  Check input.";
		}
	}
	if ( warning.length() > 0 ) {
		__error_wait = true;
		Message.printWarning ( 1, __command + "JDialog.checkInput",
			warning );
	}
}

/**
Free memory for garbage collection.
*/
protected void finalize ()
throws Throwable
{	__TSID_JComboBox = null;
	__TSList_JComboBox = null;
	__cancel_JButton = null;
	__command_JTextField = null;
	__command_Vector = null;
	__AddTSID_JList = null;
	__AddTSID_JListModel = null;
	__HandleMissingHow_JComboBox = null;
	__ok_JButton = null;
	__ts_JPopupMenu = null;
	super.finalize ();
}

/**
Return the text for the command.
@return the text for the command or null if there is a problem with the command.
*/
public Vector getText ()
{	if (	(__command_Vector == null) ||
		(__command_Vector.size() == 0) ||
		((String)__command_Vector.elementAt(0)).equals("") ) {
		return null;
	}
	return __command_Vector;
}

/**
Instantiates the GUI components.
@param parent JFrame class instantiating this class.
@param command Vector of String containing the command.
@param is_add If true, then an add() command is being edited.  Otherwise,
a subtract() command.
@param tsids Time series identifiers for time series that can be used.
*/
private void initialize ( JFrame parent, Vector command, boolean is_add, Vector tsids )
{	__command_Vector = command;
	__command = ((String)command.elementAt(0)).trim();
	if ( is_add ) {
		__command = "Add";
	}
	else {	__command = "Subtract";
	}
	String title = "Edit " + __command + "() Command";

	addWindowListener( this );

        Insets insetsTLBR = new Insets(2,2,2,2);

	JPanel main_JPanel = new JPanel();
	main_JPanel.setLayout( new GridBagLayout() );
	getContentPane().add ( "North", main_JPanel );
	int y = 0;

	if ( __command.equalsIgnoreCase("add") ) {
        	JGUIUtil.addComponent(main_JPanel,
		new JLabel ( "Add one or more time series to a time series." +
		"  The receiving time series is modified."),
		0, y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
	}
	else if ( __command.equalsIgnoreCase("subtract") ) {
        	JGUIUtil.addComponent(main_JPanel,
		new JLabel ( "Subtract one or more time series from a time " +
		"series.  The receiving time series is modified."),
		0, y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
	}
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"The time series to be " + __command +
		"ed can be selected using the TS list parameter:"),
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"  " + __AllTS + " - " + __command +
		" all previous time series."),
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"  " + __SelectedTS + " - " + __command +
		" time series selected with selectTimeSeries() commands"),
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"  " + __SpecifiedTS + " - " + __command +
		" time series selected from the list below (* will " +
		__command + " all previous time series)"),
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
	if ( __command.equalsIgnoreCase("add") ) {
        	JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Right-click on \"Time Series to Add\" to convert to" +
		" TEMPTS."),
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
	}
	else if ( __command.equalsIgnoreCase("subtract") ) {
        	JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Right-click on \"Time Series to Subtract\" to convert to" +
		" TEMPTS."),
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
	}

        JGUIUtil.addComponent(main_JPanel,
		new JLabel ( "Time series to receive results:"),
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	__TSID_JComboBox = new SimpleJComboBox ( false );
	int size = 0;
	if ( tsids != null ) {
		size = tsids.size();
	}
	if ( size == 0 ) {
		Message.printWarning ( 1, __command + "_JDialog.initialize",
		"You must define time series before inserting the " +
		__command + "() command." );
		response ( 0 );
	}
	__TSID_JComboBox.setData ( tsids );
	__TSID_JComboBox.addItemListener ( this );
        JGUIUtil.addComponent(main_JPanel, __TSID_JComboBox,
		1, y, 6, 1, 1, 0, insetsTLBR, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel ("TS list:"),
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	Vector tslist_Vector = new Vector();
	tslist_Vector.addElement ( __AllTS );
	tslist_Vector.addElement ( __SelectedTS );
	tslist_Vector.addElement ( __SpecifiedTS );
	__TSList_JComboBox = new SimpleJComboBox(false);
	__TSList_JComboBox.setData ( tslist_Vector );
	__TSList_JComboBox.addItemListener (this);
	JGUIUtil.addComponent(main_JPanel, __TSList_JComboBox,
		1, y, 2, 1, 1, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"How to get the time series to " + __command + "."),
		3, y, 3, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST );

        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Time series to " + __command + ":" ), 
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	__AddTSID_JListModel = new DefaultListModel();
	__AddTSID_JListModel.addElement ( "*" );
	for ( int i = 0; i < size; i++ ) {
		__AddTSID_JListModel.addElement(
		(String)tsids.elementAt(i));
	}
	__AddTSID_JList = new JList ( __AddTSID_JListModel );
	__AddTSID_JList.addListSelectionListener ( this );
	__AddTSID_JList.addKeyListener ( this );
	__AddTSID_JList.addMouseListener ( this );
	__AddTSID_JList.clearSelection();
	DefaultListSelectionModel sm = new DefaultListSelectionModel();
	sm.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION );
	__AddTSID_JList.setSelectionModel ( sm );
        JGUIUtil.addComponent(main_JPanel, new JScrollPane(
		__AddTSID_JList),
		1, y, 6, 1, 1, 0, insetsTLBR, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST );

        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Handle missing data how?:" ), 
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	__HandleMissingHow_JComboBox = new SimpleJComboBox ( false );
	__HandleMissingHow_JComboBox.addItem ( __IGNORE_MISSING );
	__HandleMissingHow_JComboBox.addItem ( __SET_MISSING_IF_ANY_MISSING );
	__HandleMissingHow_JComboBox.addItem ( __SET_MISSING_IF_OTHER_MISSING );
	__HandleMissingHow_JComboBox.addItemListener ( this );
        JGUIUtil.addComponent(main_JPanel, __HandleMissingHow_JComboBox,
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
{	refresh();
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
Refresh the command from the other text field contents:
<pre>
Old:
add(Alias,MissingFlag,TSID1,TSID2,...)
add(Alias,TSID1,TSID2,...)

New:

add(TSID="X",HandleMissingHow=X,TSList="X",SubtractTSID="X,X,...")
subtract(TSID="X",HandleMissingHow=X,TSList="X",SubtractTSID="X,X,...")
</pre>
*/
private void refresh ()
{	String routine = __command + "_JDialog.refresh";
	String TSID = "";
	String HandleMissingHow = "";
	String AddTSID = "";	// Time series to add (or subtract)
	String TSList = "";	// How to get list of time series
	__error_wait = false;
	String command_string = ((String)__command_Vector.elementAt(0)).trim();
	if ( __first_time ) {
		__first_time = false;
		if ( (command_string.length() > 0) && command_string.indexOf('=') < 0 ) {
			// Old syntax...
			int first_to_add = 2;	// Index of first time series
						// to add.
			TSList = __SpecifiedTS;
			// Parse the incoming string and fill the fields...
			Vector v = StringUtil.breakStringList (
				command_string,"(),",
				StringUtil.DELIM_SKIP_BLANKS );
			if ( (v != null) && (v.size() >= 3) ) {
				TSID = ((String)v.elementAt(1)).trim();
				HandleMissingHow =
					((String)v.elementAt(2)).trim();
				if (	HandleMissingHow.equalsIgnoreCase(
					__IGNORE_MISSING) ||
					HandleMissingHow.equalsIgnoreCase(
					__SET_MISSING_IF_OTHER_MISSING) ||
					HandleMissingHow.equalsIgnoreCase(
					__SET_MISSING_IF_ANY_MISSING) ) {
					// New style syntax.
					first_to_add = 3;
				}
				else {	// Old style syntax.
					first_to_add = 2;
					// Default...
					HandleMissingHow = __IGNORE_MISSING;
				}
			}
			StringBuffer buffer = new StringBuffer();
			int size = v.size();
			for ( int i = first_to_add; i < size; i++ ) {
				if ( i != first_to_add ) {
					buffer.append ( "," );
				}
				buffer.append ( (String)v.elementAt(i) );
			}
			AddTSID=buffer.toString();
			Message.printStatus ( 1, "",
			"AddTSID=\"" + AddTSID + "\"" );
		}
		else {	// New syntax...
			Vector v = StringUtil.breakStringList (
			((String)__command_Vector.elementAt(0)).trim(),"()",0 );
			PropList props = null;
			if (	(v != null) && (v.size() > 1) &&
				(((String)v.elementAt(1)).indexOf("=") > 0) ) {
				props = PropList.parse (
					(String)v.elementAt(1), routine, "," );
			}
			if ( props == null ) {
				props = new PropList ( routine );
			}
			TSID = props.getValue ( "TSID" );
			TSList = props.getValue ( "TSList" );
			if ( __command.equalsIgnoreCase("add") ) {
				AddTSID = props.getValue ( "AddTSID" );
			}
			else {	AddTSID = props.getValue ( "SubtractTSID" );
			}
			HandleMissingHow = props.getValue ( "HandleMissingHow");
		}
		if ( (TSID == null) || (TSID.length() == 0) ) {
			// Select default...
			__TSID_JComboBox.select ( 0 );
		}
		else {	if (	JGUIUtil.isSimpleJComboBoxItem(
				__TSID_JComboBox,
				TSID, JGUIUtil.NONE, null, null ) ) {
				__TSID_JComboBox.select ( TSID );
			}
			else {	Message.printWarning ( 1, routine,
				"Existing command " +
				"references an invalid\nTSID value \"" +
				TSID +
				"\".  Select a different value or Cancel.");
				__error_wait = true;
			}
		}
		if ( TSList == null ) {
			// Select default...
			__TSList_JComboBox.select ( 0 );
		}
		else {	if (	JGUIUtil.isSimpleJComboBoxItem(
				__TSList_JComboBox,
				TSList, JGUIUtil.NONE, null, null ) ) {
				__TSList_JComboBox.select ( TSList );
			}
			else {	Message.printWarning ( 1, routine,
				"Existing command " +
				"references an invalid\nTSList value \"" +
				TSList +
				"\".  Select a different value or Cancel.");
				__error_wait = true;
			}
		}
		if ( HandleMissingHow == null ) {
			// Select default...
			__HandleMissingHow_JComboBox.select ( 0 );
		}
		else {	if (	JGUIUtil.isSimpleJComboBoxItem(
				__HandleMissingHow_JComboBox,
				HandleMissingHow, JGUIUtil.NONE, null, null )) {
				__HandleMissingHow_JComboBox.select (
				HandleMissingHow );
			}
			else {	Message.printWarning ( 1, routine,
				"Existing command " +
				"references an invalid\n" +
				"HandleMissingHow value \"" + HandleMissingHow +
				"\".  Select a different value or Cancel.");
				__error_wait = true;
			}
		}
		// Check all the items in the list and highlight the
		// ones that match the command being edited...
		if (	(TSList != null) &&
			TSList.equalsIgnoreCase(__SpecifiedTS) &&
			(AddTSID != null) ) {
			Vector v = StringUtil.breakStringList (
				AddTSID, ",", StringUtil.DELIM_SKIP_BLANKS );
			int size = v.size();
			String temp = null;
			int pos = 0;
			boolean found_ts = false;
			Vector selected = new Vector();
			String independent = "";
			for ( int i = 0; i < size; i++ ) {
				independent = (String)v.elementAt(i);
				found_ts = false;
				if (	(pos = JGUIUtil.indexOf(
					__AddTSID_JList,
					independent, false, true))>= 0 ) {
					// Select it because it is in the
					// command and the list...
					selected.addElement ( "" + pos );
					found_ts = true;
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
							__AddTSID_JList,
							temp,false,true);
						if ( (pos >=0) ) {
							temp = "TEMPTS " + temp;
							__AddTSID_JListModel
							.setElementAt(temp,pos);
							selected.addElement (
							"" + pos );
							found_ts = true;
						}
					}
					if ( !found_ts ) {
						// Probably not in the original
						// list so add to the bottom.
						// The TEMPTS is already at the
						// front of the independent TS..
						__AddTSID_JListModel.
						addElement( independent);
						JGUIUtil.select (
							__AddTSID_JList,
							independent, true );
						selected.addElement ( "" +
							size );
					}
				}
				else {	Message.printWarning ( 1, routine,
					"Existing " +
					"command references a non-existent\n"+
					"time series \"" + independent +
					"\".  Select a\n" +
					"different time series or Cancel." );
				}
			}
			// Select the matched time series...
			if ( selected.size() > 0  ) {
				int [] iselected = new int[selected.size()];
				for ( int is = 0; is < iselected.length; is++ ){
					iselected[is] = StringUtil.atoi (
					(String)selected.elementAt(is));
				}
				__AddTSID_JList.setSelectedIndices(
					iselected );
			}
		}
	}
	// Regardless, reset the command from the fields...
	TSID = __TSID_JComboBox.getSelected();
	TSList = __TSList_JComboBox.getSelected();
	HandleMissingHow = __HandleMissingHow_JComboBox.getSelected();
	if (	TSList.equalsIgnoreCase(__AllTS) ||
		TSList.equalsIgnoreCase(__SelectedTS) ) {
		// Don't need...
		AddTSID = "";
	}
	else if ( TSList.equalsIgnoreCase(__SpecifiedTS) ) {
		// Format from the selected identifiers...
		AddTSID = "";
		if ( JGUIUtil.selectedSize(__AddTSID_JList) > 0 ) {
			// Get the selected and format...
			int selected[] = __AddTSID_JList.getSelectedIndices();
			int size = JGUIUtil.selectedSize(__AddTSID_JList);
			StringBuffer buffer = new StringBuffer();
			for ( int i = 0; i < size; i++ ) {
				if ( i > 0 ) {
					buffer.append ( ",");
				}
				buffer.append ( __AddTSID_JListModel.elementAt(
							selected[i]) );
			}
			AddTSID = buffer.toString();
		}
	}
	StringBuffer b = new StringBuffer ();
	if ( TSID.length() > 0 ) {
		b.append ( "TSID=\"" + TSID + "\"" );
	}
	if ( TSList.length() > 0 ) {
		if ( b.length() > 0 ) {
			b.append ( "," );
		}
		b.append ( "TSList=\"" + TSList + "\"" );
	}
	if ( AddTSID.length() > 0 ) {
		if ( b.length() > 0 ) {
			b.append ( "," );
		}
		if ( __command.equalsIgnoreCase("add") ) {
			b.append ( "AddTSID=\"" + AddTSID + "\"" );
		}
		else {	b.append ( "SubtractTSID=\"" + AddTSID + "\"" );
		}
	}
	if ( HandleMissingHow.length() > 0 ) {
		if ( b.length() > 0 ) {
			b.append ( "," );
		}
		b.append ( "HandleMissingHow=" + HandleMissingHow );
	}
	__command_JTextField.setText( __command + "(" + b.toString() + ")" );
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

} // end add_JDialog
