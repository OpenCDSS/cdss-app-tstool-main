// ----------------------------------------------------------------------------
// fillInterpolate_JDialog - editor for fillInterpolate()
// ----------------------------------------------------------------------------
// Copyright:	See the COPYRIGHT file.
// ----------------------------------------------------------------------------
// History: 
//
// 25 Feb 2001	Steven A. Malers, RTi	Initial version (copy and modify
//					runningAverage_Dialog).
// 2002-04-23	SAM, RTi		Clean up dialog.
// 2003-12-02	SAM, RTi		Update to Swing.
// 2004-02-17	SAM, RTi		Add clarifying notes for user.
// 2007-02-26	SAM, RTi		Clean up code based on Eclipse feedback.
// 2007-03-01	SAM, RTi		Put quotes around TSID to allow for spaces.
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
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

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

public class fillInterpolate_JDialog extends JDialog
implements ActionListener, ItemListener, KeyListener, WindowListener
{
private final String __INTERPOLATION_TYPE_LINEAR = "Linear";

private SimpleJButton	__cancel_JButton = null,// Cancel Button
			__ok_JButton = null;	// Ok Button
private Vector		__command_Vector = null;// Command as Vector of String
private JTextField	__command_JTextField=null;// Command as JTextField
private SimpleJComboBox	__alias_JComboBox = null;// Field for time series alias
private SimpleJComboBox	__interpolate_type_JComboBox = null;
						// Field for averaging type
// TODO SAM 2007-07-11 Enable FillFlag
//private JTextField	__FillFlag_JTextField =null; // Flag to label filled data.
private JTextField	__maxint_JTextField =null;// Field for averaging bracket
private boolean		__error_wait = false;	// Is there an error that we
						// are waiting to be cleared up
						// or Cancel?
private boolean		__first_time = true;

/**
fillInterpolate_JDialog constructor.
@param parent JFrame class instantiating this class.
@param command Command to parse.
@param tsids Time series identifiers for time series available to convert.
*/
public fillInterpolate_JDialog ( JFrame parent, Vector command, Vector tsids )
{	super(parent, true);
	initialize ( parent, "Edit fillInterpolate() Command", command, tsids );
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
{	String maxint = __maxint_JTextField.getText().trim();
	//TODO SAM
	//String interpolate_type = __interpolate_type_JComboBox.getSelected();
	//String FillFlag = __FillFlag_JTextField.getText().trim();	
	String warning = "";

	if ( !StringUtil.isInteger(maxint) ) {
		warning +=
			"\nMaximum intervals \"" + maxint +
			"\" is not an integer.\n"+
			"Specify an integer or Cancel.";
		__error_wait = true;
	}
	else if ( StringUtil.isInteger(maxint) &&
			(StringUtil.atoi(maxint) < 0)) {
		warning +=
			"\nMaximum intervals \"" + maxint +"\" must be >= 0.\n"+
			"Specify a positive integer or Cancel.";
		__error_wait = true;
	}
	/*
	if ( (FillFlag != null) && (FillFlag.length() > 1) ) {
		warning +=
			"\nThe fill flag \"" + FillFlag +
			"\" should be a single character.";
	}
	*/

	if ( warning.length() > 0 ) {
		__error_wait = true;
		Message.printWarning ( 1, "fillInterpolate_JDialog", warning );
	}
}

/**
Free memory for garbage collection.
*/
protected void finalize ()
throws Throwable
{	__alias_JComboBox = null;
	__interpolate_type_JComboBox = null;
	__maxint_JTextField = null;
	//__FillFlag_JTextField = null;
	__cancel_JButton = null;
	__command_JTextField = null;
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
		"Fill time series missing values by interpolating between" +
		" non-missing values." ), 
		0, y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"If the maximum intervals to fill is 0, then interpolation" +
		" will be used regardless of the data gap." ), 
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Time Series to Fill:" ), 
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	__alias_JComboBox = new SimpleJComboBox ( false );
	int size = 0;
	if ( tsids != null ) {
		size = tsids.size();
	}
	if ( size == 0 ) {
		Message.printWarning ( 1, "fillInterpolate_JDialog.initialize",
		"You must define time series before inserting a " +
		"fillInterpolate() command." );
		response ( 0 );
	}
	__alias_JComboBox.setData ( tsids );
	// Always allow a "*" to let all time series be filled...
	__alias_JComboBox.add ( "*" );
	__alias_JComboBox.addItemListener ( this );
        JGUIUtil.addComponent(main_JPanel, __alias_JComboBox,
		1, y, 6, 1, 1, 0, insetsTLBR, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);

        JGUIUtil.addComponent(main_JPanel,new JLabel(
		"Maximum Intervals to Fill:"),
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	__maxint_JTextField = new JTextField ( "0", 10 );
	__maxint_JTextField.addKeyListener ( this );
        JGUIUtil.addComponent(main_JPanel, __maxint_JTextField,
		1, y, 2, 1, 1, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Type of Interpolation:"), 
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	__interpolate_type_JComboBox = new SimpleJComboBox ( false );
	__interpolate_type_JComboBox.addItem ( __INTERPOLATION_TYPE_LINEAR );
	__interpolate_type_JComboBox.addItemListener ( this );
        JGUIUtil.addComponent(main_JPanel, __interpolate_type_JComboBox,
		1, y, 2, 1, 1, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
       
       /*
    JGUIUtil.addComponent(main_JPanel,new JLabel( "Fill flag:"),
    	0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
    __FillFlag_JTextField = new JTextField ( "", 10 );
    __FillFlag_JTextField.addKeyListener ( this );
    JGUIUtil.addComponent(main_JPanel, __FillFlag_JTextField,
    	1, y, 2, 1, 1, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
    JGUIUtil.addComponent(main_JPanel, new JLabel (
    	"Optional one-character flag to mark filled data."),
    	3, y, 3, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST );
*/

    JGUIUtil.addComponent(main_JPanel, new JLabel ( "Command:" ), 
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	__command_JTextField = new JTextField ( 55 );
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

	if ( title != null ) {
		setTitle ( title );
	}
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
fillInterpolate(TSID,MaxIntervals,Method)
</pre>
*/
private void refresh ()
{	String alias = "";
	String interpolate_type = "";
	String maxint = "";
	//String FillFlag = "";
	__error_wait = false;
	if ( __first_time ) {
		__first_time = false;
		// Parse the incoming string and fill the fields...
		Vector v = StringUtil.breakStringList (
			((String)__command_Vector.elementAt(0)).trim(),"() ,",
			StringUtil.DELIM_SKIP_BLANKS|StringUtil.DELIM_ALLOW_STRINGS );
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
				"fillInterpolate_JDialog.refresh",
				"Existing fillInterpolate() references a " +
				"non-existent\n"+
				"time series \"" + alias + "\".  Select a\n" +
				"different time series or Cancel." );
			}
			// Third field has max intervals to fill...
			maxint = ((String)v.elementAt(2)).trim();
			__maxint_JTextField.setText( maxint );
			// Fourth field has interpolate type...
			interpolate_type = ((String)v.elementAt(3)).trim();
			if (	JGUIUtil.isSimpleJComboBoxItem(
				__interpolate_type_JComboBox,
				interpolate_type, JGUIUtil.NONE, null, null )){
				__interpolate_type_JComboBox.select (
				interpolate_type );
			}
			else {	Message.printWarning ( 1,
				"fillInterpolate_JDialog.refresh",
				"Existing fillInterpolate() references a " +
				"non-existent\n"+
				"interpolation type \"" + interpolate_type +
				"\".  Select a\n" +
				"different interpolation type or Cancel." );
			}
		}
	}
	// Regardless, reset the command from the fields...
	alias = __alias_JComboBox.getSelected();
	maxint = __maxint_JTextField.getText().trim();
	interpolate_type = __interpolate_type_JComboBox.getSelected();
	__command_JTextField.setText("fillInterpolate(\"" + alias + "\"," +
	maxint + "," + interpolate_type + ")" );
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

} // end fillInterpolate_JDialog
