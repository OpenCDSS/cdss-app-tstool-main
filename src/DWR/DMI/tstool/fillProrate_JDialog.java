// ----------------------------------------------------------------------------
// fillProrate_JDialog - editor for fillProrate()
// ----------------------------------------------------------------------------
// Copyright:	See the COPYRIGHT file.
// ----------------------------------------------------------------------------
// History: 
//
// 2002-04-21	Steven A. Malers, RTi	Initial version (copy and modify
//					fillRepeat_JDialog).
// 2005-07-17	SAM, RTi		* Add CalculateFactorHow, AnalysisStart,
//					  AnalysisEnd, FillFlag.
//					* Convert command area to a text area.
// 2005-08-01	SAM, RTi		* Change InitialValue to use an
//					  editable JComboBox and allow a number
//					  NearestForward, or NearestBackward.
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
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import RTi.Util.GUI.JGUIUtil;
import RTi.Util.GUI.SimpleJButton;
import RTi.Util.GUI.SimpleJComboBox;
import RTi.Util.IO.PropList;
import RTi.Util.Message.Message;
import RTi.Util.String.StringUtil;
import RTi.Util.Time.DateTime;

public class fillProrate_JDialog extends JDialog
implements ActionListener, ItemListener, KeyListener, ListSelectionListener,
MouseListener, WindowListener
{
private String __FORWARD = "Forward";
private String __BACKWARD = "Backward";

private String __AnalyzeAverage = "AnalyzeAverage";
private String __NearestPoint = "NearestPoint";

private String __NearestBackward = "NearestBackward";
private String __NearestForward = "NearestForward";

private String __CHANGE_TO_TEMPTS = "Change to TEMPTS";
private String __REMOVE_TEMPTS_FLAG = "Remove TEMPTS flag";

private SimpleJButton	__cancel_JButton = null,// Cancel Button
			__ok_JButton = null;	// Ok Button
private Vector		__command_Vector = null;// Command as Vector of String
private JTextArea	__command_JTextArea=null;// Command as JTextField
private SimpleJComboBox	__InitialValue_JComboBox = null;
						// How to get initial value for
						// the ratio.
private SimpleJComboBox	__TSID_JComboBox = null;
						// Field for time series alias
private JList		__IndependentTSID_JList = null;
						// Field for independent time
						// series identifier
private DefaultListModel __IndependentTSID_JListModel = null;
private JPopupMenu	__ts_JPopupMenu;	// Popup to convert a normal
						// time series to a temporary.
private SimpleJComboBox	__FillDirection_JComboBox = null;
						// Field for fill direction.
private JTextField	__FillStart_JTextField =null;
						// Field for fill start
private JTextField	__FillEnd_JTextField =null;
						// Field for fill end
private SimpleJComboBox	__CalculateFactorHow_JComboBox = null;
						// How factor is determined
private JTextField	__AnalysisStart_JTextField =null;
						// Field for fill start
private JTextField	__AnalysisEnd_JTextField =null;
						// Field for fill end
private JTextField	__FillFlag_JTextField =null;
						// Flag to label filled data.
private boolean		__error_wait = false;	// Is there an error that we
						// are waiting to be cleared up
						// or Cancel?
private boolean		__first_time = true;

/**
fillProrate_JDialog constructor.
@param parent JFrame class instantiating this class.
@param command Command to parse.
@param tsids Time series identifiers for time series available to fill.
*/
public fillProrate_JDialog ( JFrame parent, Vector command, Vector tsids )
{	super(parent, true);
	initialize ( parent, "Edit fillProrate() Command", command, tsids );
}

/**
Responds to ActionEvents.
@param event ActionEvent object
*/
public void actionPerformed( ActionEvent event )
{	Object o = event.getSource();
	String s = event.getActionCommand();

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
		JGUIUtil.addStringToSelected(__IndependentTSID_JList,"TEMPTS ");
		refresh();
	}
	else if ( s.equals(__REMOVE_TEMPTS_FLAG) ) {
		// If selected time series start with the string "TEMPTS" and
		// there is more than one token, remove the leading TEMPTS...
		JGUIUtil.removeStringFromSelected(
			__IndependentTSID_JList,"TEMPTS ");
		refresh();
	}
	else {	// Choices...
		refresh();
	}
}

/**
Check the user input for errors and set __error_wait accordingly.
*/
private void checkInput ()
{	String TSID = __TSID_JComboBox.getSelected();
	String IndependentTSID =
		(String)__IndependentTSID_JList.getSelectedValue();
	if ( IndependentTSID == null ) {
		IndependentTSID = "";
	}
	String FillStart = __FillStart_JTextField.getText().trim();
	String FillEnd = __FillEnd_JTextField.getText().trim();
	String AnalysisStart = __AnalysisStart_JTextField.getText().trim();
	String AnalysisEnd = __AnalysisEnd_JTextField.getText().trim();
	String InitialValue = __InitialValue_JComboBox.getSelected().trim();
	String FillFlag = __FillFlag_JTextField.getText().trim();
	String warning = "";
	__error_wait = false;

	if ( TSID.equalsIgnoreCase(IndependentTSID) ) {
		warning += "\nThe time series to fill \"" + TSID +
			"\" is the same.\n"+
			"as the independent time series \"" +
			IndependentTSID + "\".\n" + "Correct or Cancel.";
	}
	if ( IndependentTSID.length() == 0 ) {
		warning += "\nAn independent time series must be selected.\n" +
				"Correct or Cancel.";
	}
	if ( FillStart.length() > 0 ) {
		try {	DateTime.parse ( FillStart );
		}
		catch ( Exception e ) {
			warning +=
				"\nThe fill start date/time \"" + FillStart +
				"\" is not a valid date/time.  " +
				"Specify a date/time or Cancel.";
		}
	}
	if ( FillEnd.length() > 0 ) {
		try {	DateTime.parse ( FillEnd );
		}
		catch ( Exception e ) {
			warning +=
			"\nThe fill end date/time \"" + FillEnd +
			"\" is not a valid date/time.  "+
			"Specify a date/time or Cancel.";
		}
	}
	if ( AnalysisStart.length() > 0 ) {
		try {	DateTime.parse ( AnalysisStart );
		}
		catch ( Exception e ) {
			warning +=
				"\nThe Analysis start date/time \"" +
				AnalysisStart +
				"\" is not a valid date/time.  " +
				"Specify a date/time or Cancel.";
		}
	}
	if ( AnalysisEnd.length() > 0 ) {
		try {	DateTime.parse ( AnalysisEnd );
		}
		catch ( Exception e ) {
			warning +=
			"\nThe Analysis end date/time \"" + AnalysisEnd +
			"\" is not a valid date/time.  "+
			"Specify a date/time or Cancel.";
		}
	}
	if (	(InitialValue.length() > 0) &&
		!InitialValue.equalsIgnoreCase(__NearestBackward) &&
		!InitialValue.equalsIgnoreCase(__NearestForward) && 
		!StringUtil.isDouble(InitialValue) ) {
		warning +=
			"\nThe initial value \"" + InitialValue +
			"\" must be a number,\n" +
			"NearestBackward, or NearestForward.  " +
			"Correct or Cancel.";
	}
	if ( (FillFlag != null) && (FillFlag.length() > 1) ) {
		warning +=
			"\nThe fill flag \"" + FillFlag +
			"\" should be a single character.";
	}
	if ( warning.length() > 0 ) {
		__error_wait = true;
		Message.printWarning ( 1, "fillProrate_JDialog", warning );
	}
}

/**
Free memory for garbage collection.
*/
protected void finalize ()
throws Throwable
{	__TSID_JComboBox = null;
	__IndependentTSID_JList = null;
	__FillDirection_JComboBox = null;
	__CalculateFactorHow_JComboBox = null;
	__FillStart_JTextField = null;
	__FillEnd_JTextField = null;
	__AnalysisStart_JTextField = null;
	__AnalysisEnd_JTextField = null;
	__InitialValue_JComboBox = null;
	__cancel_JButton = null;
	__command_JTextArea = null;
	__command_Vector = null;
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
@param tsids Time series identifiers.
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
		"This command fills missing data in a time series by" +
		" prorating values from an independent time series." ), 
		0, y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"The proration factor is calculated in one of the following " +
		"ways (indicated by \"Calculate factor how?\"):" ),
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"1)  Recompute the factor at each point where a non-missing " +
		"value is found in both time series (NearestPoint)." ),
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"     The initial value in the filled time series is used to " +
		"compute the initial factor, for missing data at the " +
		"end of the fill period."),
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"2)  Recompute the factor as the dependent time series "
		+ "value divided by the average of independent values " +
		"(AnalysisAverage)." ),
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"The factor (ratio) is TS/IndependentTS and the filled value "+
		"is calculated as factor*IndependentTS." ),
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"If the independent time series data value is zero, the factor "
		+"remains as previously calculated to avoid division by zero."),
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"The independent time series is not itself filled if * is " +
		"specified for the time series to fill."),
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"The fill start and end, if specified, will limit the period" +
		" that is filled." ), 
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Use standard date/time formats appropriate for the date/time "+
		"precision of the time series."),
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel ("Time series to fill:"), 
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	__TSID_JComboBox = new SimpleJComboBox ( false );
	int size = 0;
	if ( tsids != null ) {
		size = tsids.size();
	}
	if ( size == 0 ) {
		tsids = new Vector();
	}
	__TSID_JComboBox.setData ( tsids );
	// Always allow a "*" to let all time series be filled...
	__TSID_JComboBox.add ( "*" );
	__TSID_JComboBox.addActionListener ( this );
        JGUIUtil.addComponent(main_JPanel, __TSID_JComboBox,
		1, y, 6, 1, 1, 0, insetsTLBR, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Independent time series:" ), 
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	__IndependentTSID_JListModel = new DefaultListModel();
	for ( int i = 0; i < size; i++ ) {
		__IndependentTSID_JListModel.addElement(
			(String)tsids.elementAt(i));
	} 
	__IndependentTSID_JList = new JList ( __IndependentTSID_JListModel );
	__IndependentTSID_JList.setSelectionMode (
		ListSelectionModel.SINGLE_SELECTION );
	__IndependentTSID_JList.addListSelectionListener ( this );
	__IndependentTSID_JList.addKeyListener ( this );
	__IndependentTSID_JList.addMouseListener ( this );
        JGUIUtil.addComponent(main_JPanel, new JScrollPane(
		__IndependentTSID_JList),
		1, y, 6, 1, 1, 0, insetsTLBR, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);

        JGUIUtil.addComponent(main_JPanel,new JLabel(
		"Fill start date/time:"),
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	__FillStart_JTextField = new JTextField ( "", 10 );
	__FillStart_JTextField.addKeyListener ( this );
        JGUIUtil.addComponent(main_JPanel, __FillStart_JTextField,
		1, y, 2, 1, 1, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Start date/time for filling (blank=fill all)."),
		3, y, 3, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST );

        JGUIUtil.addComponent(main_JPanel,new JLabel(
		"Fill end date/time:"),
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	__FillEnd_JTextField = new JTextField ( "", 10 );
	__FillEnd_JTextField.addKeyListener ( this );
        JGUIUtil.addComponent(main_JPanel, __FillEnd_JTextField,
		1, y, 2, 1, 1, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"End date/time for filling (blank=fill all)."),
		3, y, 3, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST );

        JGUIUtil.addComponent(main_JPanel,new JLabel( "Fill flag:"),
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	__FillFlag_JTextField = new JTextField ( "", 10 );
	__FillFlag_JTextField.addKeyListener ( this );
        JGUIUtil.addComponent(main_JPanel, __FillFlag_JTextField,
		1, y, 2, 1, 1, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Optional one-character flag to mark filled data."),
		3, y, 3, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST );

        JGUIUtil.addComponent(main_JPanel, new JLabel ( "Fill direction:"), 
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	__FillDirection_JComboBox = new SimpleJComboBox ( false );
	__FillDirection_JComboBox.addItem ( __BACKWARD );
	__FillDirection_JComboBox.addItem ( __FORWARD );
	__FillDirection_JComboBox.select ( __FORWARD );
	__FillDirection_JComboBox.addActionListener ( this );
        JGUIUtil.addComponent(main_JPanel, __FillDirection_JComboBox,
		1, y, 2, 1, 1, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Direction to traverse data when filling."),
		3, y, 3, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST );

        JGUIUtil.addComponent(main_JPanel, new JLabel("Calculate factor how?:"),
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	__CalculateFactorHow_JComboBox = new SimpleJComboBox ( false );
	__CalculateFactorHow_JComboBox.addItem ( "" );
	__CalculateFactorHow_JComboBox.addItem ( __AnalyzeAverage );
	__CalculateFactorHow_JComboBox.addItem ( __NearestPoint );
	//__CalculateFactorHow_JComboBox.select ( __Constant );
	__CalculateFactorHow_JComboBox.addActionListener ( this );
        JGUIUtil.addComponent(main_JPanel, __CalculateFactorHow_JComboBox,
		1, y, 2, 1, 1, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"How will factor be calculated? (default=NearestPoint)"),
		3, y, 3, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST );

        JGUIUtil.addComponent(main_JPanel,new JLabel(
		"Analysis start date/time:"),
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	__AnalysisStart_JTextField = new JTextField ( "", 10 );
	__AnalysisStart_JTextField.addKeyListener ( this );
        JGUIUtil.addComponent(main_JPanel, __AnalysisStart_JTextField,
		1, y, 2, 1, 1, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Analysis start date/time, for AnalyzeAverage."),
		3, y, 3, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST );

        JGUIUtil.addComponent(main_JPanel,new JLabel(
		"Analysis end date/time:"),
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	__AnalysisEnd_JTextField = new JTextField ( "", 10 );
	__AnalysisEnd_JTextField.addKeyListener ( this );
        JGUIUtil.addComponent(main_JPanel, __AnalysisEnd_JTextField,
		1, y, 2, 1, 1, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Analysis end date/time, for AnalyzeAverage."),
		3, y, 3, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST );

        JGUIUtil.addComponent(main_JPanel,new JLabel( "Initial value:"),
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	__InitialValue_JComboBox = new SimpleJComboBox ( true );
	Vector InitialValue_Vector = new Vector  ( 3 );
	InitialValue_Vector.addElement ( "" );
	InitialValue_Vector.addElement ( __NearestBackward );
	InitialValue_Vector.addElement ( __NearestForward );
	__InitialValue_JComboBox.setData ( InitialValue_Vector );
	__InitialValue_JComboBox.addKeyListener ( this );
	__InitialValue_JComboBox.addActionListener ( this );
        JGUIUtil.addComponent(main_JPanel, __InitialValue_JComboBox,
		1, y, 2, 1, 1, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Initial value in time series for missing end-points."),
		3, y, 3, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST );

        JGUIUtil.addComponent(main_JPanel, new JLabel ( "Command:" ), 
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	__command_JTextArea = new JTextArea ( 5, 55 );
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
	else {	refresh();
	}
}

public void keyReleased ( KeyEvent event )
{	//refresh();
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
Refresh the command from the other text field contents.  The command is
of the form:
<pre>
fillProrate(TSID="x",IndependentTSID="x",FillStart="x",FillEnd="x",
FillDirection=x,InitialValue=x)
</pre>
*/
private void refresh()
{	String TSID = "";
	String IndependentTSID = "";
	String FillStart = "";
	String FillEnd = "";
	String FillDirection = "";
	String CalculateFactorHow = "";
	String AnalysisStart = "";
	String AnalysisEnd = "";
	String InitialValue = "";
	String FillFlag = "";
	if ( __first_time ) {
		__first_time = false;
		__error_wait = false;
		// Parse the incoming string and fill the fields...
		Vector v = StringUtil.breakStringList (
			((String)__command_Vector.elementAt(0)).trim(),"()",
			StringUtil.DELIM_SKIP_BLANKS |
			StringUtil.DELIM_ALLOW_STRINGS );
		PropList props = null;
		if ( (v != null) && (v.size() > 1) ) {
			props = PropList.parse (
				(String)v.elementAt(1),
				"fillProrate", "," );
		}
		else {	props = new PropList ( "fillProrate" );
		}
		TSID = props.getValue ( "TSID" );
		IndependentTSID = props.getValue ( "IndependentTSID" );
		if ( IndependentTSID == null ) {
			return;
		}
		FillStart = props.getValue ( "FillStart" );
		FillEnd = props.getValue ( "FillEnd" );
		FillDirection = props.getValue ( "FillDirection" );
		CalculateFactorHow = props.getValue ( "CalculateFactorHow" );
		AnalysisStart = props.getValue ( "AnalysisStart" );
		AnalysisEnd = props.getValue ( "AnalysisEnd" );
		InitialValue = props.getValue ( "InitialValue" );
		FillFlag = props.getValue ( "FillFlag" );
		// Now select the information...
		if ( TSID == null ) {
			// Select all...
			__TSID_JComboBox.select ( "*" );
		}
		else {	if (	JGUIUtil.isSimpleJComboBoxItem(__TSID_JComboBox,
				TSID, JGUIUtil.NONE, null, null ) ) {
				__TSID_JComboBox.select ( TSID );
			}
			else {	Message.printWarning ( 1,
				"fillProrate_JDialog.refresh",
				"Existing fillProrate() " +
				"references an invalid\nTSID \"" + TSID +
				"\".  Select a different time " +
				"series or Cancel.");
				__error_wait = true;
			}
		}
		if (	JGUIUtil.indexOf( __IndependentTSID_JList,
			IndependentTSID, false, true) >= 0 ) {
			// Select it...
			JGUIUtil.select ( __IndependentTSID_JList,
				IndependentTSID, true );
		}
		else if ( IndependentTSID.regionMatches(true,0,"TEMPTS",0,6) ) {
			// The time series is a TSTEMP so look for the rest of
			// the time series in the list.  If it exists, convert
			// to TSTEMP to match the command.  If not add to the
			// list as a TSTEMP to match the command.
			String temp =
				StringUtil.getToken( IndependentTSID, " ",
				StringUtil.DELIM_SKIP_BLANKS,1);
			boolean found_ts = false;
			if ( temp != null ) {
				temp = temp.trim();
				int pos = JGUIUtil.indexOf(
					__IndependentTSID_JList,
					temp,false,true);
				if ( (pos >=0) ) {
					temp = "TEMPTS " + temp;
					__IndependentTSID_JListModel.
					setElementAt( temp, pos );
					JGUIUtil.select (
					__IndependentTSID_JList,
					temp, true );
					found_ts = true;
				}
			}
			if ( !found_ts ) {
				// Probably not in the original list so add to
				// the bottom.  The TEMPTS is already at the
				// front of the independent TS..
				__IndependentTSID_JListModel.addElement (
						IndependentTSID);
				JGUIUtil.select (
					__IndependentTSID_JList,
					IndependentTSID, true );
			}
		}
		else {	Message.printWarning ( 1,
			"fillProrate_JDialog.refresh", "Existing " +
			"fillProrate() references a non-existent\n"+
			"time series \"" + IndependentTSID +
			"\".  Select a\n" +
			"different time series or Cancel." );
		}
		if ( FillStart != null ) {
			__FillStart_JTextField.setText ( FillStart );
		}
		if ( FillEnd != null ) {
			__FillEnd_JTextField.setText ( FillEnd );
		}
		if ( FillDirection == null ) {
			// Select default...
			__FillDirection_JComboBox.select ( __FORWARD );
		}
		else {	if (	JGUIUtil.isSimpleJComboBoxItem(
				__FillDirection_JComboBox,
				FillDirection, JGUIUtil.NONE, null, null ) ) {
				__FillDirection_JComboBox.select(FillDirection);
			}
			else {	Message.printWarning ( 1,
				"fillProrate_JDialog.refresh",
				"Existing fillProrate() " +
				"references an invalid\nfill direction \"" +
				FillDirection + "\".  Select a different " +
				"fill direction or Cancel.");
				__error_wait = true;
			}
		}
		if ( CalculateFactorHow == null ) {
			// Select default...
			__CalculateFactorHow_JComboBox.select ( __FORWARD );
		}
		else {	if (	JGUIUtil.isSimpleJComboBoxItem(
				__CalculateFactorHow_JComboBox,
				CalculateFactorHow, JGUIUtil.NONE, null, null)){
				__CalculateFactorHow_JComboBox.select(
				CalculateFactorHow);
			}
			else {	Message.printWarning ( 1,
				"fillProrate_JDialog.refresh",
				"Existing fillProrate() " +
				"references an invalid\nCalculateFactorHow \"" +
				CalculateFactorHow +
				"\".  Select a different value or Cancel.");
				__error_wait = true;
			}
		}
		if ( AnalysisStart != null ) {
			__AnalysisStart_JTextField.setText ( AnalysisStart );
		}
		if ( AnalysisEnd != null ) {
			__AnalysisEnd_JTextField.setText ( AnalysisEnd );
		}
		if ( InitialValue == null ) {
			// Select default...
			__InitialValue_JComboBox.select ( 0 );
		}
		else {	if (	JGUIUtil.isSimpleJComboBoxItem(
				__InitialValue_JComboBox,
				InitialValue, JGUIUtil.NONE, null, null)){
				__InitialValue_JComboBox.select(
				InitialValue);
			}
			else if ( StringUtil.isDouble(InitialValue) ) {
				__InitialValue_JComboBox.setText (
					InitialValue );
			}
			else {	Message.printWarning ( 1,
				"fillProrate_JDialog.refresh",
				"Existing fillProrate() " +
				"references an invalid\nInitialValue \"" +
				InitialValue +
				"\".  Select a different value or Cancel.");
				__error_wait = true;
			}
		}
		if ( FillFlag != null ) {
			__FillFlag_JTextField.setText ( FillFlag );
		}
	}
	// Regardless, reset the command from the fields...
	TSID = __TSID_JComboBox.getSelected();
	IndependentTSID = (String)__IndependentTSID_JList.getSelectedValue();
	FillStart = __FillStart_JTextField.getText().trim();
	FillEnd = __FillEnd_JTextField.getText().trim();
	FillDirection = __FillDirection_JComboBox.getSelected();
	CalculateFactorHow= __CalculateFactorHow_JComboBox.getSelected();
	AnalysisStart = __AnalysisStart_JTextField.getText().trim();
	AnalysisEnd = __AnalysisEnd_JTextField.getText().trim();
	InitialValue = __InitialValue_JComboBox.getSelected().trim();
	FillFlag = __FillFlag_JTextField.getText().trim();
	StringBuffer b = new StringBuffer ();
	if ( TSID.length() > 0 ) {
		b.append ( "TSID=\"" + TSID + "\"" );
	}
	if ( (IndependentTSID != null) && (IndependentTSID.length() > 0) ) {
		if ( b.length() > 0 ) {
			b.append ( "," );
		}
		b.append ( "IndependentTSID=\"" + IndependentTSID + "\"" );
	}
	if ( FillStart.length() > 0 ) {
		if ( b.length() > 0 ) {
			b.append ( "," );
		}
		b.append ( "FillStart=\"" + FillStart + "\"" );
	}
	if ( FillEnd.length() > 0 ) {
		if ( b.length() > 0 ) {
			b.append ( "," );
		}
		b.append ( "FillEnd=\"" + FillEnd + "\"" );
	}
	if ( FillDirection.length() > 0 ) {
		if ( b.length() > 0 ) {
			b.append ( "," );
		}
		b.append ( "FillDirection=" + FillDirection );
	}
	if ( CalculateFactorHow.length() > 0 ) {
		if ( b.length() > 0 ) {
			b.append ( "," );
		}
		b.append ( "CalculateFactorHow=" + CalculateFactorHow );
	}
	if ( AnalysisStart.length() > 0 ) {
		if ( b.length() > 0 ) {
			b.append ( "," );
		}
		b.append ( "AnalysisStart=\"" + AnalysisStart + "\"" );
	}
	if ( AnalysisEnd.length() > 0 ) {
		if ( b.length() > 0 ) {
			b.append ( "," );
		}
		b.append ( "AnalysisEnd=\"" + AnalysisEnd + "\"" );
	}
	if ( InitialValue.length() > 0 ) {
		if ( b.length() > 0 ) {
			b.append ( "," );
		}
		b.append ( "InitialValue=" + InitialValue );
	}
	if ( FillFlag.length() > 0 ) {
		if ( b.length() > 0 ) {
			b.append ( "," );
		}
		b.append ( "FillFlag=\"" + FillFlag + "\"" );
	}
	__command_JTextArea.setText("fillProrate(" +
		b.toString() + ")" );
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

} // end fillProrate_JDialog
