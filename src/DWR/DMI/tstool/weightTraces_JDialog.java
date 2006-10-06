// ----------------------------------------------------------------------------
// weightTraces_JDialog - editor for weightTraces()
// ----------------------------------------------------------------------------
// Copyright:	See the COPYRIGHT file.
// ----------------------------------------------------------------------------
// History: 
//
// 21 Mar 2001	Steven A. Malers, RTi	Initial version.  Copy normalize_Dialog
//					and modify.
// 2002-04-24	SAM, RTi		Clean up dialog.
// 2003-12-16	SAM, RTi		Update to Swing.
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
import javax.swing.JTextField;

import RTi.Util.GUI.JGUIUtil;
import RTi.Util.GUI.SimpleJButton;
import RTi.Util.GUI.SimpleJComboBox;
import RTi.Util.Message.Message;
import RTi.Util.String.StringUtil;

public class weightTraces_JDialog extends JDialog
implements ActionListener, ItemListener, KeyListener, WindowListener
{

private String __ABSOLUTE_WEIGHTS = "AbsoluteWeights";
//private String __NORMALIZED_WEIGHTS = "NormalizedWeights";

private SimpleJButton	__cancel_JButton = null,// Cancel Button
			__ok_JButton = null;	// Ok Button
private JFrame		__parent_JFrame = null;	// parent JFrame
private Vector		__command_Vector = null;// Command as Vector of String
private JTextField	__command_JTextField=null;// Command as JTextField
private JTextField	__alias_JTextField = null;// Field for time series alias
private SimpleJComboBox	__independent_JComboBox = null;
						// Time series available to
						// operate on.
private SimpleJComboBox	__weight_JComboBox = null;// Indicates how weights are
						// specified
private JTextArea	__trace_JTextArea = null; // Field for traces
private JTextArea	__weight_JTextArea = null;// Weights for traces.
						// results.
private Vector		__tsids = null;		// Time series identifiers
						// to check against selected
						// alias
private boolean		__error_wait = false;	// Is there an error that we
						// are waiting to be cleared up
						// or Cancel?
private boolean		__first_time = true;

/**
weightTraces_JDialog constructor.
@param parent JFrame class instantiating this class.
@param command Command to parse.
@param tsids Time series identifiers for existing time series.
*/
public weightTraces_JDialog ( JFrame parent, Vector command, Vector tsids )
{	super(parent, true);
	initialize ( parent, "Edit weightTraces() Command", command, tsids );
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
{	// Nothing to check since the choice only allows valid values.
	String alias = __alias_JTextField.getText().trim();
	Vector traces = JGUIUtil.toVector(__trace_JTextArea); 
	Vector weights = JGUIUtil.toVector(__weight_JTextArea); 
	String warning = "";
	if ( alias.length() == 0 ) {
		warning +=
			"\nAn alias must be specified.";
	}
	if ( traces.size() != weights.size() ) {
		warning +=
			"\nYou must specify the same number of traces" +
			" and weights.";
	}
	int size = traces.size();
	for ( int i = 0; i < size; i++ ) {
		if ( !StringUtil.isDouble((String)traces.elementAt(i)) ) {
			warning +=
				"\nTrace number \"" + traces.elementAt(i) +
				"\" is not a number.\n"+
				"Specify a number or Cancel.";
		}
	}
	size = weights.size();
	for ( int i = 0; i < size; i++ ) {
		if ( !StringUtil.isDouble((String)weights.elementAt(i)) ) {
			warning +=
				"\nWeight value \"" + weights.elementAt(i) +
				"\" is not a number.\n"+
				"Specify a number or Cancel.";
		}
	}
	if ( warning.length() > 0 ) {
		Message.printWarning ( 1, "weightTraces_JDialog", warning );
		__error_wait = true;
	}
}


/**
Free memory for garbage collection.
*/
protected void finalize ()
throws Throwable
{	__alias_JTextField = null;
	__cancel_JButton = null;
	__command_JTextField = null;
	__command_Vector = null;
	__independent_JComboBox = null;
	__weight_JComboBox = null;
	__trace_JTextArea = null;
	__weight_JTextArea = null;
	__ok_JButton = null;
	__parent_JFrame = null;
	__tsids = null;
	super.finalize ();
}

/**
Return the text for the command.
@return the text for the command or null if there is a problem with the
command.
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
@param tsids Time series identifiers to compare alias to.
*/
private void initialize ( JFrame parent, String title, Vector command,
			Vector tsids )
{	__parent_JFrame = parent;
	__command_Vector = command;
	__tsids = tsids;

	addWindowListener( this );

        Insets insetsTLBR = new Insets(2,2,2,2);

	JPanel main_JPanel = new JPanel();
	main_JPanel.setLayout( new GridBagLayout() );
	GridBagConstraints gbc = new GridBagConstraints();
	getContentPane().add ( "North", main_JPanel );
	int y = 0;

        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Create a new time series by weighting traces.  The result" +
		" is identified by its alias."),
		0, y, 7, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Enter trace years and weights one value per line."),
		0, ++y, 7, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel ( "Time Series Alias:" ), 
		0, ++y, 1, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.EAST);
	__alias_JTextField = new JTextField ();
	__alias_JTextField.addKeyListener ( this );
        JGUIUtil.addComponent(main_JPanel, __alias_JTextField,
		1, y, 6, 1, 1, 0, insetsTLBR, gbc.HORIZONTAL, gbc.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Trace Identifier:" ), 
		0, ++y, 1, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.EAST );
	__independent_JComboBox = new SimpleJComboBox ( false );
	int size = 0;
	if ( tsids != null ) {
		size = tsids.size();
	}
	if ( size == 0 ) {
		Message.printWarning ( 1, "weightTraces_JDialog.initialize",
		"You must define time series traces before inserting a " +
		"weightTraces() command." );
		response ( 0 );
	}
	__independent_JComboBox.setData ( tsids );
	__independent_JComboBox.addItemListener ( this );
        JGUIUtil.addComponent(main_JPanel, __independent_JComboBox,
		1, y, 6, 1, 1, 0, insetsTLBR, gbc.HORIZONTAL, gbc.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel(
		"Weights Specified How:"),
		0, ++y, 1, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.EAST);
	__weight_JComboBox = new SimpleJComboBox ( false );
	__weight_JComboBox.addItem ( __ABSOLUTE_WEIGHTS );
	//__weight_JComboBox.addItem ( __NORMALIZED_WEIGHTS );
	__weight_JComboBox.addItemListener ( this );
        JGUIUtil.addComponent(main_JPanel, __weight_JComboBox,
		1, y, 2, 1, 1, 0, insetsTLBR, gbc.NONE, gbc.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel( "Trace (year):"),
		0, ++y, 1, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.EAST);
	__trace_JTextArea = new JTextArea ( 5, 10 );
	__trace_JTextArea.addKeyListener ( this );
        JGUIUtil.addComponent(main_JPanel, new JScrollPane(__trace_JTextArea),
		1, y, 2, 1, 1, 0, insetsTLBR, gbc.HORIZONTAL, gbc.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel( "Weight:"),
		3, y, 1, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.EAST);
	__weight_JTextArea = new JTextArea ( 5, 10 );
	__weight_JTextArea.addKeyListener ( this );
        JGUIUtil.addComponent(main_JPanel, new JScrollPane(__weight_JTextArea),
		4, y, 2, 1, 1, 0, insetsTLBR, gbc.HORIZONTAL, gbc.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel ( "Command:"),
		0, ++y, 1, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.EAST);
	__command_JTextField = new JTextField ( 60 );
	__command_JTextField.setEditable ( false );
	JGUIUtil.addComponent(main_JPanel, __command_JTextField,
		1, y, 6, 1, 1, 0, insetsTLBR, gbc.HORIZONTAL, gbc.WEST);

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
{	refresh();
}

public void keyReleased ( KeyEvent event )
{	refresh();
}

public void keyTyped ( KeyEvent event ) {;}

/**
Refresh the command from the other text field contents.
*/
private void refresh ()
{	String alias = "";
	String independent = "";
	String weight_type = "";
	Vector weights = new Vector();
	Vector traces = new Vector();
	__error_wait = false;
	if ( __first_time ) {
		__first_time = false;
		// Parse an existing command
		// Assume TS X =
		// weightTraces(tsident,weightType,trace,wt,trace,wt)...
		Vector v = StringUtil.breakStringList (
		(String)__command_Vector.elementAt(0), "=",
		StringUtil.DELIM_SKIP_BLANKS );
		if ( (v != null) && (v.size() == 2) ) {
			// First field has format "TS X"
			alias = ((String)v.elementAt(0)).trim();
			alias = alias.substring(2).trim();
			__alias_JTextField.setText( alias );
			v = StringUtil.breakStringList (
				((String)v.elementAt(1)).trim(),
				"( ),", StringUtil.DELIM_SKIP_BLANKS );
			int size = 0;
			if ( v != null ) {
				size = v.size();
			}
			if ( size >= 3 ) {
				independent = ((String)v.elementAt(1)).trim();
				// Now select the item in the list.  If not a
				// match, print a warning.
				if (	JGUIUtil.isSimpleJComboBoxItem(
					__independent_JComboBox, independent,
					JGUIUtil.NONE, null, null ) ) {
					__independent_JComboBox.select (
					independent );
				}
				else {	Message.printWarning ( 1,
					"weightTraces_JDialog.refresh",
					"Existing weightTraces() references a "+
					"non-existent\ntime series \"" +
					independent + "\".  Select a\n" +
					"different time series or Cancel." );
				}
				weight_type = ((String)v.elementAt(2)).trim();
				if (	JGUIUtil.isSimpleJComboBoxItem(
					__weight_JComboBox, weight_type,
					JGUIUtil.NONE, null, null ) ) {
					__weight_JComboBox.select (weight_type);
				}
				else {	Message.printWarning ( 1,
					"weightTraces_JDialog.refresh",
					"Existing weightTraces() references an "
					+ "invalid\nweight type \"" +
					weight_type + "\".  Select a\n"+
					"different type or Cancel." );
				}
			}
			// Process each trace and weight...
			for ( int i = 3; i < size; i++ ) {
				if ( (i%2) != 0 ) {
					traces.addElement(
					((String)v.elementAt(i)).trim());
				}
				else {	weights.addElement(
					((String)v.elementAt(i)).trim());
				}
			}
			size = traces.size();
			for ( int i = 0; i < size; i++ ) {
				if ( i != 0 ) {
					__trace_JTextArea.append ( "\n" );
				}
				__trace_JTextArea.append (
				(String)traces.elementAt(i) );
			}
			size = weights.size();
			for ( int i = 0; i < size; i++ ) {
				if ( i != 0 ) {
					__weight_JTextArea.append ( "\n" );
				}
				__weight_JTextArea.append (
				(String)weights.elementAt(i) );
			}
		}
	}
	// Regardless, reset the command from the fields...
	alias = __alias_JTextField.getText().trim();
	independent = __independent_JComboBox.getSelected();
	weight_type = __weight_JComboBox.getSelected();
	traces = JGUIUtil.toVector(__trace_JTextArea); 
	weights = JGUIUtil.toVector(__weight_JTextArea); 
	if ( (alias == null) || (alias.trim().length() == 0) ) {
		return;
	}
	if ( (independent == null) || (independent.trim().length() == 0) ) {
		return;
	}
	StringBuffer buffer = new StringBuffer (
			"TS " + alias + " = weightTraces(" +
			independent + "," + weight_type );
	int size = traces.size();
	if ( weights.size() == traces.size() ) {
		for ( int i = 0; i < size; i++ ) {
			buffer.append ( "," + traces.elementAt(i) + "," +
			weights.elementAt(i) );
		}
	}
	__command_JTextField.setText( buffer.toString() + ")" );
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

} // end weightTraces_JDialog
