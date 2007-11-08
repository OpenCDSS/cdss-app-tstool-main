// ----------------------------------------------------------------------------
// ARMA_JDialog - editor for ARMA()
// ----------------------------------------------------------------------------
// Copyright:	See the COPYRIGHT file.
// ----------------------------------------------------------------------------
// History: 
//
// 2002-04-17	Steven A. Malers, RTi	Copy shiftTimeByInterval() and modify
//					for ARMA.
// 2002-04-19	SAM, RTi		Add ARMA interval.
// 2002-05-01	SAM, RTi		Change 1.0 total check to use formatted
//					strings to avoid precision problems.
// 2003-12-15	SAM, RTi		Update to Swing.
// 2004-08-02	SAM, RTi		Fix bug where checkInput() was crashing
//					because of different number of
//					coefficients.
// 2005-06-29	SAM, RTi		Correct typo in "coefients".
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
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
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
import RTi.Util.Time.TimeInterval;

public class ARMA_JDialog extends JDialog
implements ActionListener, ItemListener, KeyListener, WindowListener
{
private SimpleJButton	__cancel_JButton = null,// Cancel Button
			__ok_JButton = null;	// Ok Button
private Vector		__command_Vector = null; // Command as Vector of String
private JTextField	__command_JTextField=null;// Command as JTextField
private SimpleJComboBox	__alias_JComboBox = null;// Field for time series alias
private JTextField	__a_JTextField = null;	// Field for a values.
private JTextField	__b_JTextField = null;	// Field for b values.
private	JTextField	__ARMA_interval_JTextField = null;
						// Interval for ARMA
						// coefficients.
private boolean		__error_wait = false;	// Is there an error that we
						// are waiting to be cleared up
						// or Cancel?
private boolean		__first_time = true;

/**
ARMA_JDialog constructor.
@param parent JFrame class instantiating this class.
@param command Command to parse.
@param tsids Time series identifiers for time series available to modify.
*/
public ARMA_JDialog ( JFrame parent, Vector command, Vector tsids )
{	super(parent, true);
	initialize ( parent, "Edit ARMA() Command", command, tsids );
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
{	// Check the data...
	String ARMA_interval =__ARMA_interval_JTextField.getText().trim();
	String warning = "";

	// Check to see that the interval is valid...

	try {	TimeInterval.parseInterval(ARMA_interval);
	}
	catch ( Exception e ) {
		warning +=
			"\nInterval used to develop ARMA coefficients is" +
			" not valid:\n" +
			"    " + ARMA_interval + "\n" +
			"Correct or Cancel.";
	}
	// Make sure coeffients sum to 1.0...
	double total = 0.0;
	Vector a_Vector = StringUtil.breakStringList (
		__a_JTextField.getText().trim(), ", ",
		StringUtil.DELIM_SKIP_BLANKS );
	int a_size = 0;
	if ( a_Vector != null ) {
		a_size = a_Vector.size();
	}
	for ( int i = 0; i < a_size; i++ ) {
		total += StringUtil.atod (
			((String)a_Vector.elementAt(i)).trim() );
	}
	Vector b_Vector = StringUtil.breakStringList (
			__b_JTextField.getText().trim(), ", ",
			StringUtil.DELIM_SKIP_BLANKS );
	int b_size = 0;
	if ( b_Vector != null ) {
		b_size = b_Vector.size();
	}
	if ( b_size == 0 ) {
		warning +=
		"\nARMA b-coefficient information is not defined.\n" +
			"Correct or Cancel.";
	}
	for ( int i = 0; i < b_size; i++ ) {
		total += StringUtil.atod (
			((String)b_Vector.elementAt(i)).trim() );
	}

	String total_String = StringUtil.formatString(total,"%.6f");
	if ( !total_String.equals("1.000000") ) {
		warning +=
			"\nTotal of a and b coefficients (" +
			StringUtil.formatString(total,"%.6f") +
			") should equal 1.0.";
	}
	String token;
	for ( int i = 0; i < a_size; i++ ) {
		token = ((String)a_Vector.elementAt(i)).trim();
		if ( token.equals("") ) {
			continue;
		}
		if (	token.equals("-") || token.equals(".") ||
			token.equals("-.") ) {
			continue;
		}
		if ( !StringUtil.isDouble(token) ) {
			warning +=
				"\na-coefficent \"" + token +
				"\" is not a number.";
		}
	}
	for ( int i = 0; i < b_size; i++ ) {
		token = ((String)b_Vector.elementAt(i)).trim();
		if ( token.equals("") ) {
			continue;
		}
		if (	token.equals("-") || token.equals(".") ||
			token.equals("-.") ) {
			continue;
		}
		if ( !StringUtil.isDouble(token) ) {
			warning +=
				"\nb-coefficent \"" + token +
				"\" is not a number.\n";
		}
	}
	if ( warning.length() > 0 ) {
		__error_wait = true;
		Message.printWarning ( 1, "ARMA_JDialog", warning );
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
	__a_JTextField = null;
	__b_JTextField = null;
	__ARMA_interval_JTextField = null;
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
@param tsids Time series identifiers from
TSEngine.getTSIdentifiersFromCommands().
*/
private void initialize ( JFrame parent, String title, Vector command,
			Vector tsids )
{	__command_Vector = command;

	addWindowListener( this );

        Insets insetsTLBR = new Insets(2,2,2,2);

	// Main panel...

	JPanel main_JPanel = new JPanel();
	main_JPanel.setLayout( new GridBagLayout() );
	getContentPane().add ( "North", main_JPanel );
	int y = 0;

        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Lag and attenuate a time series using the ARMA " +
		"(AutoRegressive Moving Average) method." ),
		0, y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"The adjusted output time series O is computed from the" +
		" original input I using:"),
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"O[t] = a_1*O[t-1] + a_2*O[t-2] + ... + a_p*O[t-p] + " +
		"b_0*I[t] + b_1*I[t-1] + ... + b_q*I[t-q]" ),
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"where t = time, p = number of outflows to consider, and" +
		" q = number of inflows to consider"),
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"ARMA a and b coefficients must be computed externally and" +
		" should sum to 1.0." ),
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"The values for p and q will be " +
		"determined from the number of coefficients."),
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Specify the interval used to compute ARMA coefficients as " +
		"1Day, 6Hour, 2Hour, etc."),
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Currently the ARMA interval must be <= the time series " +
		"interval."),
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"The resulting value is set to missing if one or more" +
		" input values are missing"),
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"(typically only filled data should be used).  The period" +
		" will not automatically be extended."),
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Time Series to Process:" ), 
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	__alias_JComboBox = new SimpleJComboBox ( false );
	int size = 0;
	if ( tsids != null ) {
		size = tsids.size();
	}
	if ( size == 0 ) {
		tsids = new Vector();
	}
	__alias_JComboBox.setData ( tsids );
	// Always allow a "*" to let all time series be set...
	__alias_JComboBox.addItem ( "*" );
	__alias_JComboBox.addItemListener ( this );
        JGUIUtil.addComponent(main_JPanel, __alias_JComboBox,
		1, y, 6, 1, 1, 0, insetsTLBR, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel ( "\"a\" coefficients:" ), 
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	__a_JTextField = new JTextField ( 50 );
	__a_JTextField.addKeyListener ( this );
        JGUIUtil.addComponent(main_JPanel, __a_JTextField,
		1, y, 6, 1, 1, 0, insetsTLBR, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel ( "\"b\" coefficients:" ), 
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	__b_JTextField = new JTextField ( 50 );
	__b_JTextField.addKeyListener ( this );
        JGUIUtil.addComponent(main_JPanel, __b_JTextField,
		1, y, 6, 1, 1, 0, insetsTLBR, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel ( "ARMA Interval:" ), 
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	__ARMA_interval_JTextField = new JTextField ( 10 );
	__ARMA_interval_JTextField.addKeyListener ( this );
        JGUIUtil.addComponent(main_JPanel, __ARMA_interval_JTextField,
		1, y, 2, 1, 1, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel ( "Command:" ), 
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	__command_JTextField = new JTextField ( 50 );
	__command_JTextField.setEditable ( false );
	JGUIUtil.addComponent(main_JPanel, __command_JTextField,
		1, y, 6, 1, 1, 0, insetsTLBR, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST );

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

public void keyTyped ( KeyEvent event )
{
}

/**
Refresh the command from the other text field contents.
*/
private void refresh ()
{	String alias = "";
	String a = "";
	String b = "";
	String ARMA_interval = "";
	int n_a = 0;
	int n_b = 0;
	boolean in_p = false;
	boolean in_q = false;
	__error_wait = false;
	String token;
	if ( __first_time ) {
		__first_time = false;
		// Parse the incoming string and fill the fields...
		Vector v = StringUtil.breakStringList (
			((String)__command_Vector.elementAt(0)).trim(),"() ,",
			StringUtil.DELIM_SKIP_BLANKS );
		if ( (v != null) && (v.size() >= 4) ) {
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
				"ARMA_JDialog.refresh",
				"Existing ARMA() references a " +
				"non-existent\n"+
				"time series \"" + alias + "\".  Select a\n" +
				"different time series or Cancel." );
			}
			ARMA_interval = ((String)v.elementAt(2)).trim();
			// 4rd+ field has pN,a1,a2,qN,b0,b1...
			a = "";
			b = "";
			for ( int i = 3; i < v.size(); i++ ) {
				token = ((String)v.elementAt(i)).trim();
				if ( token.regionMatches(true,0,"p",0,1) ) {
					in_p = true;
					in_q = false;
					continue;
				}
				else if ( token.regionMatches(true,0,"q",0,1) ){
					in_p = false;
					in_q = true;
					continue;
				}
				if ( in_p ) {
					if ( n_a != 0 ) {
						a += ",";
					}
					a += token;
					++n_a;
				}
				else if ( in_q ) {
					if ( n_b != 0 ) {
						b += ",";
					}
					b += token;
					++n_b;
				}
			}
			__a_JTextField.setText ( a );
			__b_JTextField.setText ( b );
			__ARMA_interval_JTextField.setText ( ARMA_interval );
		}
	}
	// Regardless, reset the command from the fields...
	alias = __alias_JComboBox.getSelected();
	a = __a_JTextField.getText().trim();
	b = __b_JTextField.getText().trim();
	ARMA_interval = __ARMA_interval_JTextField.getText().trim();
	if ( (alias == null) || (alias.trim().length() == 0) ) {
		return;
	}
	if ( (ARMA_interval == null) || (ARMA_interval.trim().length() == 0) ) {
		return;
	}
	n_a = 0;
	n_b = 0;
	Vector a_Vector = StringUtil.breakStringList ( a, ", ",
			StringUtil.DELIM_SKIP_BLANKS );
	if ( a_Vector != null ) {
		n_a = a_Vector.size();
	}
	Vector b_Vector = StringUtil.breakStringList ( b, ", ",
			StringUtil.DELIM_SKIP_BLANKS );
	if ( b_Vector != null ) {
		n_b = b_Vector.size();
	}
	if ( n_b == 0 ) {
		__command_JTextField.setText("");
	}
	else {	__command_JTextField.setText("ARMA(" + alias + "," +
		ARMA_interval + ",p" +
		 n_a + "," + a + ",q" + (n_b - 1) + "," + b + ")" );
	}
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

} // end ARMA_JDialog
