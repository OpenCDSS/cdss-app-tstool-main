// ----------------------------------------------------------------------------
// writeDateValue_JDialog - editor for writeDateValue()
// ----------------------------------------------------------------------------
// Copyright:	See the COPYRIGHT file.
// ----------------------------------------------------------------------------
// History: 
//
// 18 Jan 2001	Steven A. Malers, RTi	Initial version (copy and modify scale).
// 2002-03-30	SAM, RTi		Update to display the working directory.
// 2002-04-23	SAM, RTi		Update to allow browse.
// 2003-11-18	SAM, RTi		Update to Swing.
// 2004-02-17	SAM, RTi		Fix bug where directory from file
//					selection was not getting set as the
//					last dialog directory in JGUIUtil.
// 2004-03-15	SAM, RTi		* Convert to free-format parameters.
//					* Add OutputStart, OutputEnd, TSList
//					  parameter.
// 2004-04-01	SAM, RTi		Fix problem with backward compatibility
//					to older syntax.
// 2004-07-12	SAM, RTi		Minor cleanup.
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

import javax.swing.JFileChooser;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.io.File;
import java.util.Vector;

import RTi.Util.GUI.JFileChooserFactory;
import RTi.Util.GUI.JGUIUtil;
import RTi.Util.GUI.SimpleFileFilter;
import RTi.Util.GUI.SimpleJButton;
import RTi.Util.GUI.SimpleJComboBox;
import RTi.Util.IO.IOUtil;
import RTi.Util.IO.PropList;
import RTi.Util.Message.Message;
import RTi.Util.String.StringUtil;
import RTi.Util.Time.DateTime;

public class writeDateValue_JDialog extends JDialog
implements ActionListener, KeyListener, ItemListener, WindowListener
{

// Use with TSList parameter
private final String __SelectedTS = "SelectedTS";
private final String __AllTS = "AllTS";

private SimpleJButton	__cancel_JButton = null,// Cancel Button
			__browse_JButton = null,// Browse Button
			__ok_JButton = null,	// Ok Button
			__path_JButton = null;	// Button to add/remove path
private Vector		__command_Vector = null;// Command as Vector of String
private String		__working_dir = null;	// Working directory.
private JTextField	__command_JTextField=null;// Command as TextField
private JTextField	__OutputFile_JTextField = null; // Field for time series
						// identifier
private JTextField	__OutputStart_JTextField = null;
private JTextField	__OutputEnd_JTextField = null;
private SimpleJComboBox	__TSList_JComboBox = null;
private boolean		__error_wait = false;	// Is there an error that we
						// are waiting to be cleared up
						// or Cancel?
private boolean		__first_time = true;

/**
writeDateValue_JDialog constructor.
@param parent JFrame class instantiating this class.
@param app_PropList Properties from the application.
@param command Time series command to parse.
@param tsids Time series identifiers for available time series.
*/
public writeDateValue_JDialog (	JFrame parent, PropList app_PropList,
				Vector command, Vector tsids )
{	// Call the full version with no title and ok Button

	super(parent, true);
	initialize ( parent, "Edit writeDateValue() Command",
		app_PropList, command, tsids );
}

/**
Responds to ActionEvents.
@param event ActionEvent object
*/
public void actionPerformed( ActionEvent event )
{	Object o = event.getSource();

	if ( o == __browse_JButton ) {
		String last_directory_selected =
			JGUIUtil.getLastFileDialogDirectory();
		JFileChooser fc = null;
		if ( last_directory_selected != null ) {
			fc = JFileChooserFactory.createJFileChooser(
				last_directory_selected );
		}
		else {	fc = JFileChooserFactory.createJFileChooser(
				__working_dir );
		}
		fc.setDialogTitle("Select DateValue Time Series File to Write");
		SimpleFileFilter sff = new SimpleFileFilter("txt",
			"DateValue Time Series File");
		fc.addChoosableFileFilter(sff);
		sff = new SimpleFileFilter("dv",
			"DateValue Time Series File");
		fc.addChoosableFileFilter(sff);
		
		if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
			String directory = fc.getSelectedFile().getParent();
			String filename = fc.getSelectedFile().getName(); 
			String path = fc.getSelectedFile().getPath(); 
	
			if (filename == null || filename.equals("")) {
				return;
			}
	
			if (path != null) {
				__OutputFile_JTextField.setText(path );
				JGUIUtil.setLastFileDialogDirectory(directory );
				refresh();
			}
		}
	}
	else if ( o == __cancel_JButton ) {
		response ( 0 );
	}
	else if ( o == __ok_JButton ) {
		refresh ();
		checkInput();
		if ( !__error_wait ) {
			response ( 1 );
		}
	}
	else if ( o == __path_JButton ) {
		if (	__path_JButton.getText().equals(
			"Add Working Directory") ) {
			__OutputFile_JTextField.setText (
			IOUtil.toAbsolutePath(__working_dir,
			__OutputFile_JTextField.getText() ) );
		}
		else if ( __path_JButton.getText().equals(
			"Remove Working Directory") ) {
			try {	__OutputFile_JTextField.setText (
				IOUtil.toRelativePath ( __working_dir,
				__OutputFile_JTextField.getText() ) );
			}
			catch ( Exception e ) {
				Message.printWarning ( 1,
				"writeDateValue_JDialog",
				"Error converting file to relative path." );
			}
		}
		refresh ();
	}
}

/**
Check the input.  If errors exist, warn the user and set the __error_wait flag
to true.  This should be called before response() is allowed to complete.
*/
private void checkInput ()
{	String file = __OutputFile_JTextField.getText().trim();

	String warning = "";

	// Adjust the working directory that was passed in by the specified
	// directory.  If the directory does not exist, warn the user...
	try {	String adjusted_path = IOUtil.adjustPath ( __working_dir, file);
		File f = new File ( adjusted_path );
		File f2 = new File ( f.getParent() );
		if ( !f2.exists() ) {
			warning +=
			"\nThe DateValue file parent directory does " +
			"not exist:\n    " + adjusted_path + "\n" +
		  	"Correct or Cancel.";
		}
		f = null;
		f2 = null;
	}
	catch ( Exception e ) {
		warning +=
			"\nThe working directory:\n" +
			"    \"" + __working_dir +
			"\"\ncannot be adjusted using:\n" +
			"    \"" + file + "\".\n" +
		"Correct the file or Cancel.";
	}
	String OutputStart = __OutputStart_JTextField.getText().trim();
	String OutputEnd = __OutputEnd_JTextField.getText().trim();
	if (!OutputStart.equals("")) {
		try {	DateTime datetime1 = DateTime.parse(OutputStart);
			if ( datetime1 == null ) {
				throw new Exception ("bad date");
			}
		}
		catch (Exception e) {
			warning += "\nStart date/time \"" + OutputStart +
			"\" is not a valid date/time.\n"+
			"Specify a valid date/time or Cancel.";
		}
	}
	if (!OutputEnd.equals("")) {
		try {	DateTime datetime2 = DateTime.parse(OutputEnd);
			if ( datetime2 == null ) {
				throw new Exception ("bad date");
			}
		}
		catch (Exception e) {
			warning += "\nEnd date/time \"" +
			OutputEnd + "\" is not a valid date/time.\n"+
			"Specify a valid date/time or Cancel.";
		}
	}
	if ( warning.length() > 0 ) {
		__error_wait = true;
		Message.printWarning ( 1, "writeDateValue_JDialog", warning );
	}
}

/**
Free memory for garbage collection.
*/
protected void finalize ()
throws Throwable
{	__cancel_JButton = null;
	__command_JTextField = null;
	__OutputFile_JTextField = null;
	__OutputStart_JTextField = null;
	__OutputEnd_JTextField = null;
	__TSList_JComboBox = null;
	__command_Vector = null;
	__ok_JButton = null;
	__path_JButton = null;
	__browse_JButton = null;
	__working_dir = null;
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
@param parent Frame class instantiating this class.
@param title Dialog title.
@param app_PropList Properties from the application.
@param command Vector of String containing the time series command, which
should have a time series identifier and optionally comments.
@param tsids Time series identifiers from
TSEngine.getTSIdentifiersFromCommands().
*/
private void initialize (	JFrame parent, String title,
				PropList app_PropList, Vector command,
				Vector tsids )
{	__command_Vector = command;
	__working_dir = app_PropList.getValue ( "WorkingDir" );

	addWindowListener( this );

        Insets insetsTLBR = new Insets(1,2,1,2);

	JPanel main_JPanel = new JPanel();
	main_JPanel.setLayout( new GridBagLayout() );
	getContentPane().add ( "North", main_JPanel );
	int y = 0;

        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Write in-memory time series to a DateValue format file," +
		" which can be specified using a full or" ),
		0, y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"relative path (relative to the working directory)."),
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
	if ( __working_dir != null ) {
        	JGUIUtil.addComponent(main_JPanel, new JLabel (
		"The working directory is: " + __working_dir ), 
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
	}
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"The Browse button can be used to select an existing file " +
		"to overwrite (or edit the file name after selection)."),
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel ("Enter date/times to a "+
		"precision appropriate for output time series."),
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"DateValue File to Write:" ), 
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	__OutputFile_JTextField = new JTextField ( 50 );
	__OutputFile_JTextField.addKeyListener ( this );
        JGUIUtil.addComponent(main_JPanel, __OutputFile_JTextField,
		1, y, 5, 1, 1, 0, insetsTLBR, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
	__browse_JButton = new SimpleJButton ( "Browse", this );
        JGUIUtil.addComponent(main_JPanel, __browse_JButton,
		6, y, 1, 1, 1, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.CENTER);

        JGUIUtil.addComponent(main_JPanel, new JLabel ("Output Start:"), 
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	__OutputStart_JTextField = new JTextField (20);
	__OutputStart_JTextField.addKeyListener (this);
        JGUIUtil.addComponent(main_JPanel, __OutputStart_JTextField,
		1, y, 2, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Overrides the global output start."),
		3, y, 3, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST );

        JGUIUtil.addComponent(main_JPanel, new JLabel ( "Output End:"), 
		0, ++y, 2, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	__OutputEnd_JTextField = new JTextField (20);
	__OutputEnd_JTextField.addKeyListener (this);
        JGUIUtil.addComponent(main_JPanel, __OutputEnd_JTextField,
		1, y, 6, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Overrides the global output end."),
		3, y, 3, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST );

        JGUIUtil.addComponent(main_JPanel, new JLabel ("TS list:"),
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	Vector tslist_Vector = new Vector();
	tslist_Vector.addElement ( "" );
	tslist_Vector.addElement ( __SelectedTS );
	tslist_Vector.addElement ( __AllTS );
	__TSList_JComboBox = new SimpleJComboBox(false);
	__TSList_JComboBox.setData ( tslist_Vector );
	__TSList_JComboBox.addItemListener (this);
	JGUIUtil.addComponent(main_JPanel, __TSList_JComboBox,
		1, y, 2, 1, 1, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Indicates the time series to output."),
		3, y, 3, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST );

        JGUIUtil.addComponent(main_JPanel, new JLabel ( "Command:"),
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

	if ( __working_dir != null ) {
		// Add the button to allow conversion to/from relative
		// path...
		__path_JButton = new SimpleJButton( "Remove Working Directory",
			"Remove Working Directory", this);
		button_JPanel.add ( __path_JButton );
	}
	__cancel_JButton = new SimpleJButton("Cancel", "Cancel", this);
	button_JPanel.add ( __cancel_JButton );
	__ok_JButton = new SimpleJButton("OK", "OK", this);
	button_JPanel.add ( __ok_JButton );

	if ( title != null ) {
		setTitle ( title );
	}
	setResizable ( true );
        pack();
        JGUIUtil.center( this );
	refresh();	// Sets the __path_JButton status
        super.setVisible( true );
}

/**
Handle ItemEvent events.
@param e ItemEvent to handle.
*/
public void itemStateChanged (ItemEvent e) {
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

public void keyTyped ( KeyEvent event )
{	refresh();
}

/**
Refresh the command from the other text field contents.  The command is of the
form:
<pre>
writeDateValue(OutputFile="X",OutputStart="X",OutputEnd="X",TSList="")
</pre>
*/
private void refresh ()
{	String routine = "writeDateValue_JDialog.refresh";
	String OutputFile = "";
	String OutputStart = "";
	String OutputEnd = "";
	String TSList = "";
	__error_wait = false;
	if ( __first_time ) {
		__first_time = false;
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
		OutputFile = props.getValue ( "OutputFile" );
		OutputStart = props.getValue ( "OutputStart" );
		OutputEnd = props.getValue ( "OutputEnd" );
		TSList = props.getValue ( "TSList" );
		// Backward compatibility...
		String token = "";
		if ( (v != null) && (v.size() > 1) ) {
			token = (String)v.elementAt(1);
			token = StringUtil.remove(token,"\"");
		}
		if ( (OutputFile == null) && (token.indexOf("=") < 0) ) {
			OutputFile = token;
		}
		if ( OutputFile != null ) {
			__OutputFile_JTextField.setText (OutputFile);
		}
		if ( OutputStart != null ) {
			__OutputStart_JTextField.setText (OutputStart);
		}
		if ( OutputEnd != null ) {
			__OutputEnd_JTextField.setText (OutputEnd);
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
	}
	// Regardless, reset the command from the fields...
	OutputFile = __OutputFile_JTextField.getText().trim();
	OutputStart = __OutputStart_JTextField.getText().trim();
	OutputEnd = __OutputEnd_JTextField.getText().trim();
	TSList = __TSList_JComboBox.getSelected();
	StringBuffer b = new StringBuffer ();
	if ( OutputFile.length() > 0 ) {
		b.append ( "OutputFile=\"" + OutputFile + "\"" );
	}
	if ( OutputStart.length() > 0 ) {
		if ( b.length() > 0 ) {
			b.append ( "," );
		}
		b.append ( "OutputStart=\"" + OutputStart + "\"" );
	}
	if ( OutputEnd.length() > 0 ) {
		if ( b.length() > 0 ) {
			b.append ( "," );
		}
		b.append ( "OutputEnd=\"" + OutputEnd + "\"" );
	}
	if ( TSList.length() > 0 ) {
		if ( b.length() > 0 ) {
			b.append ( "," );
		}
		b.append ( "TSList=\"" + TSList + "\"" );
	}
	__command_JTextField.setText("writeDateValue(" + b.toString() + ")" );
	__command_Vector.removeAllElements();
	__command_Vector.addElement ( __command_JTextField.getText() );
	// Check the path and determine what the label on the path button should
	// be...
	if ( (OutputFile == null) || (OutputFile.length() == 0) ) {
		if ( __path_JButton != null ) {
			__path_JButton.setEnabled ( false );
		}
	}
	if ( __path_JButton != null ) {
		__path_JButton.setEnabled ( true );
		File f = new File ( OutputFile );
		if ( f.isAbsolute() ) {
			__path_JButton.setText ( "Remove Working Directory" );
		}
		else {	__path_JButton.setText ( "Add Working Directory" );
		}
	}
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

} // end writeDateValue_JDialog
