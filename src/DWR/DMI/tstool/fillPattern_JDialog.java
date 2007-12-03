// ----------------------------------------------------------------------------
// fillPattern_JDialog - editor for fillPattern()
// ----------------------------------------------------------------------------
// Copyright:	See the COPYRIGHT file.
// ----------------------------------------------------------------------------
// History: 
//
// 25 Feb 2001	Steven A. Malers, RTi	Initial version (copy and modify
//					runningAverage_Dialog).
// 2002-04-05	SAM, RTi		Cosmetic cleanup.
// 2003-12-11	SAM, RTi		Update to Swing.
// 2005-04-25	SAM, RTi		Update to support free-format
//					parameters.
// 2005-04-27	SAM, RTi		Change TSList options to be more
//					specific to allow using first, last,
//					or all matching TSID.
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
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import RTi.Util.GUI.JGUIUtil;
import RTi.Util.GUI.SimpleJButton;
import RTi.Util.GUI.SimpleJComboBox;
import RTi.Util.IO.PropList;
import RTi.Util.Message.Message;
import RTi.Util.String.StringUtil;

public class fillPattern_JDialog extends JDialog
implements ActionListener, ItemListener, KeyListener, WindowListener
{

private final String __AllTS = "AllTS";
private final String __AllMatchingTSID = "AllMatchingTSID";
private final String __LastMatchingTSID = "LastMatchingTSID";
private final String __SelectedTS = "SelectedTS";

private SimpleJButton	__cancel_JButton = null,// Cancel Button
			__ok_JButton = null;	// Ok Button
private Vector		__command_Vector = null;// Command as Vector of String
private JTextArea	__command_JTextArea=null;// Command as TextArea
private Vector		__pattern_file_Vector;	// Vector of fill pattern files.
private String		__working_dir = null;	// Application working
						// directory.
private SimpleJComboBox	__TSID_JComboBox = null;// Field for time series ID
private SimpleJComboBox	__TSList_JComboBox = null;
						// Indicate how to get time
						// series list.
private SimpleJComboBox	__PatternID_JComboBox = null;// Field for averaging type
private boolean		__error_wait = false;	// Is there an error that we
						// are waiting to be cleared up
						// or Cancel?
private boolean		__first_time = true;

/**
fillPattern_JDialog constructor.
@param parent JFrame class instantiating this class.
@param app_PropList Properties from the application.
@param pattern_file_Vector list of pattern files.
@param command Command to parse.
@param tsids Time series identifiers for time series available to convert.
*/
public fillPattern_JDialog (	JFrame parent, PropList app_PropList,
				Vector pattern_file_Vector, Vector command,
				Vector tsids, Vector patterns )
{	super(parent, true);
	initialize (	parent, "Edit FillPattern() Command", app_PropList,
			pattern_file_Vector, command, tsids, patterns );
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
	else if ( (__TSList_JComboBox != null) && (o == __TSList_JComboBox) ) {
		checkGUIState();
		refresh ();
	}
}

/**
Check the state of the dialog, disabling/enabling components as appropriate.
*/
private void checkGUIState()
{	// If "AllMatchingTSID" or "LastMatchingTSID", enable the list.
	// Otherwise, clear and disable...
	String selected = __TSList_JComboBox.getSelected();
	if (	selected.equals(__AllMatchingTSID) ||
		selected.equals(__LastMatchingTSID) ) {
		__TSID_JComboBox.setEnabled(true);
	}
	else {	__TSID_JComboBox.setEnabled(false);
	}
}

/**
Check the input.  If errors exist, warn the user and set the __error_wait flag
to true.  This should be called before response() is allowed to complete.
*/
private void checkInput ()
{	// Nothing to check since the choice only allows valid values.
	/* REVISIT SAM 2005-04-25 can the following happen??
	if ( (alias == null) || (alias.trim().length() == 0) ) {
		return;
	}
	if ( (pattern == null) || (pattern .trim().length() == 0) ) {
		return;
	}
	*/
}

/**
Free memory for garbage collection.
*/
protected void finalize ()
throws Throwable
{	__TSID_JComboBox = null;
	__TSList_JComboBox = null;
	__PatternID_JComboBox = null;
	__cancel_JButton = null;
	__command_JTextArea = null;
	__command_Vector = null;
	__ok_JButton = null;
	__working_dir = null;
	__pattern_file_Vector = null;
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
@param app_PropList Properties from the application.
@param pattern_file_Vector list of pattern files.
@param command Vector of String containing the command.
@param tsids Time series identifiers - ignored.
@param patterns Patterns from the pattern files (reference gages).
*/
private void initialize (	JFrame parent, String title,
				PropList app_PropList,
				Vector pattern_file_Vector, Vector command,
				Vector tsids, Vector patterns )
{	__command_Vector = command;
	__working_dir = app_PropList.getValue ( "WorkingDir" );
	__pattern_file_Vector = pattern_file_Vector;

	addWindowListener( this );

        Insets insetsTLBR = new Insets(2,2,2,2);

	// Main panel...

	JPanel main_JPanel = new JPanel();
	main_JPanel.setLayout( new GridBagLayout() );
	getContentPane().add ( "North", main_JPanel );
	int y = 0;

        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Monthly time series can be filled using historic average" +
		" patterns (e.g., WET, DRY, AVG climate patterns)." ), 
		0, y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Patterns are defined with setPatternFile() command(s).  " +
		"The following pattern file(s) are defined:" ), 
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
	if (	(__pattern_file_Vector == null) ||
		(__pattern_file_Vector.size() == 0) ) {
		JGUIUtil.addComponent(main_JPanel, new JLabel (
		"     None Defined" ), 
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
	}
	else {	int size = __pattern_file_Vector.size();
		for ( int i = 0; i < size; i++ ) {
			JGUIUtil.addComponent(main_JPanel, new JLabel (
			"     " + (String)__pattern_file_Vector.elementAt(i) ), 
			0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
		}
        	JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Pattern file paths are absolute or are relative to the " +
		"working directory."),
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
		if ( __working_dir != null ) {
        		JGUIUtil.addComponent(main_JPanel, new JLabel (
			"The working directory is: " + __working_dir ), 
			0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
		}
	}
       	JGUIUtil.addComponent(main_JPanel, new JLabel (
		"The time series to process are indicated using the TS list."),
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
       	JGUIUtil.addComponent(main_JPanel, new JLabel (
		"If TS list is \"AllMatchingTSID\" or \"LastMatchingTSID\", "+
		"pick a single time series, " +
		"or enter a wildcard time series identifier pattern."),
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel ("TS list:"),
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	Vector tslist_Vector = new Vector();
	tslist_Vector.addElement ( __AllTS );
	tslist_Vector.addElement ( __AllMatchingTSID );
	tslist_Vector.addElement ( __LastMatchingTSID );
	tslist_Vector.addElement ( __SelectedTS );
	__TSList_JComboBox = new SimpleJComboBox(false);
	__TSList_JComboBox.setData ( tslist_Vector );
	__TSList_JComboBox.select ( 0 );
	__TSList_JComboBox.addActionListener (this);
	JGUIUtil.addComponent(main_JPanel, __TSList_JComboBox,
		1, y, 2, 1, 1, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"How to get the time series to fill."),
		3, y, 3, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST );

        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Identifier (TSID) to match:" ), 
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);

	// Allow edits...
	__TSID_JComboBox = new SimpleJComboBox ( true );
	int size = 0;
	if ( tsids != null ) {
		size = tsids.size();
	}
	if ( size == 0 ) {
		tsids = new Vector();
	}
	__TSID_JComboBox.setData ( tsids );
	// Always allow a "*" to let all time series be filled...
	__TSID_JComboBox.addItem ( "*" );
	__TSID_JComboBox.addItemListener ( this );
	__TSID_JComboBox.addKeyListener ( this );
        JGUIUtil.addComponent(main_JPanel, __TSID_JComboBox,
		1, y, 6, 1, 1, 0, insetsTLBR, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel ( "Fill pattern ID:"), 
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	__PatternID_JComboBox = new SimpleJComboBox ( false );
	size = 0;
	if ( patterns != null ) {
		size = patterns.size();
	}
	if ( size == 0 ) {
		Message.printWarning ( 1, "fillPattern_JDialog.initialize",
		"You must define pattern files with setPatternFile()\nbefore " +
		"inserting a fillPattern() command." );
		response ( 0 );
	}
	__PatternID_JComboBox.setData ( patterns );
	__PatternID_JComboBox.addItemListener ( this );
        JGUIUtil.addComponent(main_JPanel, __PatternID_JComboBox,
		1, y, 2, 1, 1, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Pattern ID used for filling."),
		3, y, 3, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST );

        JGUIUtil.addComponent(main_JPanel, new JLabel ( "Command:" ), 
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	__command_JTextArea = new JTextArea ( 4, 50 );
	__command_JTextArea.setLineWrap ( true );
	__command_JTextArea.setWrapStyleWord ( true );
	__command_JTextArea.setEditable ( false );
	JGUIUtil.addComponent(main_JPanel, new JScrollPane(__command_JTextArea),
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
Refresh the command from the other text field contents.  The command is
of the form:
<pre>
New:

fillPattern(TSID="X",TSList=X,PatternID="X")

Old:

fillPattern(TSID,Pattern)
</pre>
*/
private void refresh ()
{	String routine = "fillPattern_JDialog.refresh";
	String TSID = "";
	String TSList = "";
	String PatternID = "";
	__error_wait = false;
	String command_string = ((String)__command_Vector.elementAt(0)).trim();
	if ( __first_time ) {
		__first_time = false;
		if (	(command_string.length() > 0) &&
			command_string.indexOf('=') < 0 ) {
			// Old syntax...
			// Parse the incoming string and fill the fields...
			Vector v = StringUtil.breakStringList (
				command_string,"(), ",
				StringUtil.DELIM_SKIP_BLANKS );
			if ( (v != null) && (v.size() == 3) ) {
				// Second field is identifier...
				TSID = ((String)v.elementAt(1)).trim();
				// Third field has pattern...
				PatternID = ((String)v.elementAt(2)).trim();
			}
			if ( TSID.indexOf("*") >= 0 ) {
				TSList = __AllMatchingTSID;	// * or 29*
			}
			else {	TSList = __LastMatchingTSID;	// explicit TSID
			}
		}
		else {	// New syntax...
			Vector v = StringUtil.breakStringList (
			((String)__command_Vector.elementAt(0)).trim(),"()",
			StringUtil.DELIM_SKIP_BLANKS );
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
			PatternID = props.getValue ( "PatternID" );
			TSList = props.getValue ( "TSList" );
		}
		// Do the checks...
		// Now select the item in the list.  If not a match, print a
		// warning.
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
		if (	JGUIUtil.isSimpleJComboBoxItem( __TSID_JComboBox, TSID,
				JGUIUtil.NONE, null, null ) ) {
				__TSID_JComboBox.select ( TSID );
		}
		else {	/* TODO SAM 2005-04-26 disable since this may
			prohibit advanced users.
			Message.printWarning ( 1,
				"fillPattern_JDialog.refresh",
				"Existing fillPattern() references a " +
				non-existent\n"+
				"time series \"" + TSID + "\".  Select a\n" +
				"different time series or Cancel." );
			*/
			// Automatically add to the list after the first "*"...
			__TSID_JComboBox.insertItemAt ( TSID, 1 );
			// Select...
			__TSID_JComboBox.select ( TSID );
		}
		// Check the GUI state to make sure that components are
		// enabled as expected (mainly enable/disable the TSID).  If
		// disabled, the TSID will not be added as a parameter below.
		checkGUIState();
		if ( !__TSID_JComboBox.isEnabled() ) {
			// Not needed because some other method of specifying
			// the time series is being used...
			TSID = null;
		}
		if (	JGUIUtil.isSimpleJComboBoxItem(
			__PatternID_JComboBox,
			PatternID, JGUIUtil.NONE, null, null ) ) {
			__PatternID_JComboBox.select ( PatternID );
		}
		else {
            /* FIXME SAM 2007-12-02 Evaluate code use
            Message.printWarning ( 1,
			"fillPattern_JDialog.refresh",
			"Existing command references a non-existent\n"+
			"pattern ID \"" + PatternID + "\".  Select a\n" +
			"different pattern or Cancel." );
            */
		}
	}
	// Regardless, reset the command from the fields...
	TSList = __TSList_JComboBox.getSelected();
	TSID = __TSID_JComboBox.getSelected();
	if ( !__TSID_JComboBox.isEnabled() ) {
		// Don't include if not needed...
		TSID = "";
	}
	PatternID = __PatternID_JComboBox.getSelected();
	StringBuffer b = new StringBuffer ();
	if ( TSList.length() > 0 ) {
		b.append ( "TSList=" + TSList );
	}
	if ( TSID.length() > 0 ) {
		if ( b.length() > 0 ) {
			b.append ( "," );
		}
		b.append ( "TSID=\"" + TSID + "\"" );
	}
	if ( PatternID.length() > 0 ) {
		if ( b.length() > 0 ) {
			b.append ( "," );
		}
		b.append ( "PatternID=\"" + PatternID + "\"" );
	}
	__command_JTextArea.setText("FillPattern(" + b.toString() + ")");
	__command_Vector.removeAllElements();
	__command_Vector.addElement ( __command_JTextArea.getText() );
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

} // end fillPattern_JDialog
