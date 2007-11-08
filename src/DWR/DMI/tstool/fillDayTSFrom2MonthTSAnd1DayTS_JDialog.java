// ----------------------------------------------------------------------------
// fillDayTSFrom2MonthTSAnd1DayTS_JDialog - editor for
//					fillDayTSFrom2MonthTSAnd1DayTS()
// ----------------------------------------------------------------------------
// Copyright:	See the COPYRIGHT file.
// ----------------------------------------------------------------------------
// History: 
//
// 25 Feb 2001	Steven A. Malers, RTi	Copy newDayTSFromMonthAndDayTS_Dialog
//					and update.
// 2002-04-23	SAM, RTi		Clean up dialog.
// 2003-12-11	SAM, RTi		Update to Swing.
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

public class fillDayTSFrom2MonthTSAnd1DayTS_JDialog extends JDialog
implements ActionListener, ItemListener, KeyListener, WindowListener
{

private SimpleJButton	__cancel_JButton = null,// Cancel Button
			__ok_JButton = null;	// Ok Button
private Vector		__command_Vector = null;// Command as Vector of String
private JTextField	__command_JTextField=null;// Command as JTextField
private SimpleJComboBox	__alias_JComboBox = null;// Field for time series alias
private SimpleJComboBox	__monthts1_JComboBox = null;// Field for related monthly
						// time series.
private SimpleJComboBox	__dayts2_JComboBox = null;// Field for independent time
						// series identifier
private SimpleJComboBox	__monthts2_JComboBox = null;// Field for independent
						// time series identifier
private boolean		__error_wait = false;
private boolean		__first_time = true;

/**
fillDayTSFrom2MonthTSAnd1DayTS_JDialog constructor.
@param parent JFrame class instantiating this class.
@param command Command to parse.
@param tsids Time series identifiers for time series that can be used.
*/
public fillDayTSFrom2MonthTSAnd1DayTS_JDialog (	JFrame parent, Vector command,
						Vector tsids )
{	super(parent, true);
	initialize ( parent, "Edit fillDayTSFrom2MonthTSAnd1DayTS() Command",
		command, tsids );
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
}


/**
Free memory for garbage collection.
*/
protected void finalize ()
throws Throwable
{	__alias_JComboBox = null;
	__dayts2_JComboBox = null;
	__monthts1_JComboBox = null;
	__monthts2_JComboBox = null;
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
		"Fill a daily time series using the relationship " +
		"D1[i] = D2[i]*M1[i]/M2[i]." ), 
		0, y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.BOTH, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"The monthly values give an average estimate of the volume " +
		"ratio and d2 provides an estimate of the daily pattern." ),
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.BOTH, GridBagConstraints.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Daily Time Series to Fill (D1):" ), 
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	__alias_JComboBox = new SimpleJComboBox ();
	int size = 0;
	if ( tsids != null ) {
		size = tsids.size();
	}
	if ( size == 0 ) {
		Message.printWarning ( 1,
		"fillDayTSFrom2MonthTSAnd1DayTS.initialize",
		"You must define time series before inserting a " +
		"fillDayTSFrom2MonthTSAnd1DayTS() command." );
		response ( 0 );
	}
	__alias_JComboBox.setData ( tsids );
	__alias_JComboBox.addItemListener ( this );
        JGUIUtil.addComponent(main_JPanel, __alias_JComboBox,
		1, y, 6, 1, 1, 0, insetsTLBR, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Associated Monthly Time Series (M1):"), 
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	__monthts1_JComboBox = new SimpleJComboBox ( false );
	__monthts1_JComboBox.setData ( tsids );
	__monthts1_JComboBox.addItemListener ( this );
        JGUIUtil.addComponent(main_JPanel, __monthts1_JComboBox,
		1, y, 6, 1, 1, 0, insetsTLBR, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Monthly Time Series for Total (M2):"), 
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	__monthts2_JComboBox = new SimpleJComboBox ( false );
	__monthts2_JComboBox.setData ( tsids );
	__monthts2_JComboBox.addItemListener ( this );
        JGUIUtil.addComponent(main_JPanel, __monthts2_JComboBox,
		1, y, 6, 1, 1, 0, insetsTLBR, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Daily Time Series for Distribution (D2):" ), 
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	__dayts2_JComboBox = new SimpleJComboBox ( false );
	__dayts2_JComboBox.setData ( tsids );
	__dayts2_JComboBox.addItemListener ( this );
        JGUIUtil.addComponent(main_JPanel, __dayts2_JComboBox,
		1, y, 6, 1, 1, 0, insetsTLBR, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);

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
Refresh the command from the other text field contents.
The command is like:
<pre>
fillDayTSFrom2MonthTSAnd1DayTS(Alias,MonthTSID,DayTSID)
</pre>
*/
private void refresh ()
{	String alias = "";
	String monthts1 = "";
	String monthts2 = "";
	String dayts2 = "";
	__error_wait = false;
	if ( __first_time ) {
		__first_time = false;
		// Parse the incoming string and fill the fields...
		Vector v = StringUtil.breakStringList (
			((String)__command_Vector.elementAt(0)).trim(),
			"=() ,", StringUtil.DELIM_SKIP_BLANKS );
		// Parse out the command values...
		if ( (v != null) && (v.size() >= 5) ) {
			// New syntax...
			alias = ((String)v.elementAt(1)).trim();
			monthts1 = ((String)v.elementAt(2)).trim();
			monthts2 = ((String)v.elementAt(3)).trim();
			dayts2 = ((String)v.elementAt(4)).trim();
			if (	JGUIUtil.isSimpleJComboBoxItem(
				__alias_JComboBox,
				alias, JGUIUtil.NONE, null, null ) ) {
				__alias_JComboBox.select ( alias );
			}
			else {	Message.printWarning ( 1,
				"fillDayTSFrom2MonthTSAnd1DayTS.refresh",
				"Existing fillDayTSFrom2MonthTSAnd1DayTS() " +
				"references a non-existent\n"+
				"D1 time series \"" + alias +
				"\".  Select a\n" +
				"different time series or Cancel." );
			}
			if (	JGUIUtil.isSimpleJComboBoxItem(
				__monthts1_JComboBox,
				monthts1, JGUIUtil.NONE, null, null ) ) {
				__monthts1_JComboBox.select ( monthts1 );
			}
			else {	Message.printWarning ( 1,
				"fillDayTSFrom2MonthTSAnd1DayTS.refresh",
				"Existing fillDayTSFrom2MonthTSAnd1DayTS() " +
				"references a non-existent\n"+
				"M1 time series \"" + monthts1 +
				"\".  Select a\n" +
				"different time series or Cancel." );
			}
			if (	JGUIUtil.isSimpleJComboBoxItem(
				__monthts2_JComboBox,
				monthts2, JGUIUtil.NONE, null, null ) ) {
				__monthts2_JComboBox.select ( monthts2 );
			}
			else {	Message.printWarning ( 1,
				"fillDayTSFrom2MonthTSAnd1DayTS.refresh",
				"Existing fillDayTSFrom2MonthTSAnd1DayTS() " +
				"references a non-existent\n"+
				"M2 time series \"" + monthts2 +
				"\".  Select a\n" +
				"different time series or Cancel." );
			}
			if (	JGUIUtil.isSimpleJComboBoxItem(
				__dayts2_JComboBox, dayts2,
				JGUIUtil.NONE, null, null ) ) {
				__dayts2_JComboBox.select ( dayts2 );
			}
			else {	Message.printWarning ( 1,
				"fillDayTSFrom2MonthTSAnd1DayTS.refresh",
				"Existing fillDayTSFrom2MonthTSAnd1DayTS() " +
				"references a non-existent\n"+
				"D2 time series \"" + dayts2 +"\".  Select a\n"+
				"different time series or Cancel." );
			}
		}
		else {	// First time and a new command that is blank.
			// To prevent a warning, select the first item in the
			// lists...
			__alias_JComboBox.select(0);
			__monthts1_JComboBox.select(0);
			__monthts2_JComboBox.select(0);
			__dayts2_JComboBox.select(0);
		}
	}
	// Regardless, reset the command from the fields...
	alias = __alias_JComboBox.getSelected();
	monthts1 = __monthts1_JComboBox.getSelected();
	monthts2 = __monthts2_JComboBox.getSelected();
	dayts2 = __dayts2_JComboBox.getSelected();
	if ( (alias == null) || (alias.trim().length() == 0) ) {
		return;
	}
	if ( (monthts1 == null) || (monthts1.trim().length() == 0) ) {
		return;
	}
	if ( (monthts2 == null) || (monthts2.trim().length() == 0) ) {
		return;
	}
	if ( (dayts2 == null) || (dayts2.trim().length() == 0) ) {
		return;
	}
	__command_JTextField.setText("fillDayTSFrom2MonthTSAnd1DayTS(" +
		alias + "," + monthts1 + "," + monthts2 + "," + dayts2 + ")" );
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

} // end fillDayTSFrom2MonthTSAnd1DayTS_JDialog
