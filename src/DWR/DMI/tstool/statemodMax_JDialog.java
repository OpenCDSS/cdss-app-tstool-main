// ----------------------------------------------------------------------------
// statemodMax_JDialog - editor for statemodMax()
// ----------------------------------------------------------------------------
// Copyright:	See the COPYRIGHT file.
// ----------------------------------------------------------------------------
// History: 
//
// 15 Mar 2001	Steven A. Malers, RTi	Initial version (copy and modify
//					readStateMod).
// 2002-04-23	SAM, RTi		Clean up dialog.
// 2004-02-17	SAM, RTi		Update to Swing.
// 2007-02-26	SAM, RTi		Clean up code based on Eclipse feedback.
// ----------------------------------------------------------------------------

package DWR.DMI.tstool;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.Vector;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import RTi.Util.GUI.JFileChooserFactory;
import RTi.Util.GUI.JGUIUtil;
import RTi.Util.GUI.SimpleFileFilter;
import RTi.Util.GUI.SimpleJButton;
import RTi.Util.IO.IOUtil;
import RTi.Util.IO.PropList;
import RTi.Util.Message.Message;
import RTi.Util.String.StringUtil;

public class statemodMax_JDialog extends JDialog
implements ActionListener, KeyListener, WindowListener
{
private SimpleJButton	__browse1_JButton = null,	// File browse button
			__browse2_JButton = null,	// Second file browse
							// button
			__cancel_JButton = null,	// Cancel Button
			__ok_JButton = null,		// Ok Button
			__path1_JButton = null,		// Buttons to convert
							// relative/
			__path2_JButton = null;		// absolute path.
private Vector		__command_Vector = null;	// Command as Vector of
							// String
private JTextField	__command_JTextField=null;	// Command as JTextField
private JTextField	__file1_JTextField = null;	// Field for first file
							// to read.
private String		__working_dir = null;		// Working directory.
private JTextField	__file2_JTextField = null;	// Field for second file
							// to read.
private boolean		__error_wait = false;	// Is there an error that we
						// are waiting to be cleared up
						// or Cancel?
private boolean		__first_time = true;

/**
statemodMax_JDialog constructor.
@param parent JFrame class instantiating this class.
@param app_PropList Properties from the application.
@param command Time series command to parse.
@param tsids Time series identifiers for available time series.
*/
public statemodMax_JDialog (	JFrame parent, PropList app_PropList,
				Vector command, Vector tsids )
{	super(parent, true);
	initialize ( parent, "Edit statemodMax() Command", app_PropList,
		command, tsids );
}

/**
Responds to ActionEvents.
@param event ActionEvent object
*/
public void actionPerformed( ActionEvent event )
{	Object o = event.getSource();

	if ( o == __browse1_JButton ) {
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
		fc.setDialogTitle( "Select StateMod Time Series File (File 1)");
		// REVISIT - maybe need to list all recognized StateMod file
		// extensions for data sets.
		SimpleFileFilter sff = new SimpleFileFilter("stm",
			"StateMod Time Series File");
		fc.addChoosableFileFilter(sff);
		
		if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			String directory = fc.getSelectedFile().getParent();
			String filename = fc.getSelectedFile().getName(); 
			String path = fc.getSelectedFile().getPath(); 
	
			if (filename == null || filename.equals("")) {
				return;
			}
	
			if (path != null) {
				__file1_JTextField.setText(path );
				JGUIUtil.setLastFileDialogDirectory(directory );
				refresh();
			}
		}
	}
	else if ( o == __browse2_JButton ) {
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
		fc.setDialogTitle( "Select StateMod Time Series File (File 2)");
		// REVISIT - maybe need to list all recognized StateMod file
		// extensions for data sets.
		SimpleFileFilter sff = new SimpleFileFilter("stm",
			"StateMod Time Series File");
		fc.addChoosableFileFilter(sff);
		
		if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			String directory = fc.getSelectedFile().getParent();
			String filename = fc.getSelectedFile().getName(); 
			String path = fc.getSelectedFile().getPath(); 
	
			if (filename == null || filename.equals("")) {
				return;
			}
	
			if (path != null) {
				__file2_JTextField.setText(path );
				JGUIUtil.setLastFileDialogDirectory(directory);
				refresh();
			}
		}
	}
	else if ( o == __cancel_JButton ) {
		response ( 0 );
	}
	else if ( o == __ok_JButton ) {
		refresh ();
		if ( !__error_wait ) {
			response ( 1 );
		}
	}
	else if ( o == __path1_JButton ) {
		if (	__path1_JButton.getText().equals(
			"Add Working Directory (File 1)") ) {
			__file1_JTextField.setText (
			IOUtil.toAbsolutePath(__working_dir,
			__file1_JTextField.getText() ) );
		}
		else if ( __path1_JButton.getText().equals(
			"Remove Working Directory (File 1)") ) {
			try {	__file1_JTextField.setText (
				IOUtil.toRelativePath ( __working_dir,
				__file1_JTextField.getText() ) );
			}
			catch ( Exception e ) {
				Message.printWarning ( 1,"readStateMod_JDialog",
				"Error converting file to relative path." );
			}
		}
		refresh ();
	}
	else if ( o == __path2_JButton ) {
		if (	__path2_JButton.getText().equals(
			"Add Working Directory (File 2)") ) {
			__file2_JTextField.setText (
			IOUtil.toAbsolutePath(__working_dir,
			__file2_JTextField.getText() ) );
		}
		else if ( __path2_JButton.getText().equals(
			"Remove Working Directory (File 2)") ) {
			try {	__file2_JTextField.setText (
				IOUtil.toRelativePath ( __working_dir,
				__file2_JTextField.getText() ) );
			}
			catch ( Exception e ) {
				Message.printWarning ( 1,"readStateMod_JDialog",
				"Error converting file to relative path." );
			}
		}
		refresh ();
	}
}

/**
Check the input.  If errors exist, warn the user and set the __errorWait flag
to true.  This should be called before response() is allowed to complete.
*/
private void checkInput ()
{	String file1 = __file1_JTextField.getText();
	String routine = "readStateMod_JDialog.checkInput";
	String warning = "";
	// Adjust the working directory that was passed in by the specified
	// directory.  If the directory does not exist, warn the user...
	try {	String adjusted_path = IOUtil.adjustPath (__working_dir, file1);
		File f = new File ( adjusted_path );
		if ( !f.exists() ) {
			warning +=
			"\nStateMod file 1 does not exist:\n" +
			"    " + adjusted_path + "\n" +
		  	"Correct or Cancel.";
		}
		f = null;
	}
	catch ( Exception e ) {
		warning +=
		"\nThe working directory:\n" +
		"    \"" + __working_dir + "\"\ncannot be adjusted using:\n" +
		"    \"" + file1 + "\".\n" +
		"Correct the file or Cancel.";
	}
	try {	String adjusted_path = IOUtil.adjustPath (__working_dir, file1);
		File f = new File ( adjusted_path );
		if ( !f.exists() ) {
			warning +=
			"\nStateMod file 2 does not exist:\n" +
			"    " + adjusted_path + "\n" +
		  	"Correct or Cancel.";
			__error_wait = true;
		}
		f = null;
	}
	catch ( Exception e ) {
		warning +=
		"\nThe working directory:\n" +
		"    \"" + __working_dir + "\"\ncannot be adjusted using:\n" +
		"    \"" + file1 + "\".\n" +
		"Correct the file or Cancel.";
	}
	if ( warning.length() > 0 ) {
		Message.printWarning ( 1, routine, warning );
		__error_wait = true;
	}
}

/**
Free memory for garbage collection.
*/
protected void finalize ()
throws Throwable
{	__browse1_JButton = null;
	__browse2_JButton = null;
	__cancel_JButton = null;
	__command_JTextField = null;
	__file1_JTextField = null;
	__file2_JTextField = null;
	__command_Vector = null;
	__ok_JButton = null;
	__path1_JButton = null;
	__path2_JButton = null;
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
@param app_PropList Properties from the application.
@param command Vector of String containing the time series command, which
should have a time series identifier and optionally comments.
@param tsids Time series identifiers from
TSEngine.getTSIdentifiersFromCommands().
*/
private void initialize ( JFrame parent, String title, PropList app_PropList,
			Vector command, Vector tsids )
{	__command_Vector = command;
	__working_dir = app_PropList.getValue ( "WorkingDir" );

	addWindowListener( this );

        Insets insetsTLBR = new Insets(2,2,2,2);

	JPanel main_JPanel = new JPanel();
	main_JPanel.setLayout( new GridBagLayout() );
	getContentPane().add ( "North", main_JPanel );
	int y = 0;

        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Create a list of time series where each time series" +
		" contains the maximum values for matching identifiers."),
		0, y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Time series identifiers are set using information in the " +
		"StateMod files."),
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Typically all results are then written with" +
		" other commands."),
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"This command is useful when computing StateMod demands as the"+
		" maximum of historical diversions and IWR/efficiency."),
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Specify a full or relative path (relative to working " +
		"directory)." ), 
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
	if ( __working_dir != null ) {
        	JGUIUtil.addComponent(main_JPanel, new JLabel (
		"The working directory is: " + __working_dir ), 
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
	}

        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"First StateMod File to Read:" ), 
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	__file1_JTextField = new JTextField ( 50 );
	__file1_JTextField.addKeyListener ( this );
        JGUIUtil.addComponent(main_JPanel, __file1_JTextField,
		1, y, 5, 1, 1, 0, insetsTLBR, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
	__browse1_JButton = new SimpleJButton ( "Browse", this );
        JGUIUtil.addComponent(main_JPanel, __browse1_JButton,
		6, y, 1, 1, 1, 0, insetsTLBR, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER);

        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Second StateMod File to Read:" ), 
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	__file2_JTextField = new JTextField ( 50 );
	__file2_JTextField.addKeyListener ( this );
        JGUIUtil.addComponent(main_JPanel, __file2_JTextField,
		1, y, 5, 1, 1, 0, insetsTLBR, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
	__browse2_JButton = new SimpleJButton ( "Browse", this );
        JGUIUtil.addComponent(main_JPanel, __browse2_JButton,
		6, y, 1, 1, 1, 0, insetsTLBR, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER);

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
		__path1_JButton = new SimpleJButton(
			"Remove Working Directory (File 1)", this);
		button_JPanel.add ( __path1_JButton );
		__path2_JButton = new SimpleJButton(
			"Remove Working Directory (File 2)", this);
		button_JPanel.add ( __path2_JButton );
	}
	__cancel_JButton = new SimpleJButton("Cancel", this);
	button_JPanel.add ( __cancel_JButton );
	__ok_JButton = new SimpleJButton( "OK", this);
	button_JPanel.add ( __ok_JButton );

	if ( title != null ) {
		setTitle ( title );
	}
        pack();
        JGUIUtil.center( this );
	refresh();	// Sets the _path_Button status
        super.setVisible( true );
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
*/
private void refresh ()
{	String file1 = "";
	String file2 = "";
	__error_wait = false;
	if ( __first_time ) {
		__first_time = false;
		// Parse the incoming string and fill the fields...
		Vector v = StringUtil.breakStringList (
			((String)__command_Vector.elementAt(0)).trim(),"() ,",
			StringUtil.DELIM_SKIP_BLANKS |
			StringUtil.DELIM_ALLOW_STRINGS );
		if ( (v != null) && (v.size() == 3) ) {
			// Second field is file...
			file1 = ((String)v.elementAt(1)).trim();
			__file1_JTextField.setText ( file1 );
			// Third field is file 2...
			file2 = ((String)v.elementAt(2)).trim();
			__file2_JTextField.setText ( file2 );
		}
	}
	// Regardless, reset the command from the fields...
	file1 = __file1_JTextField.getText();
	boolean do_return = false;
	if ( (file1 == null) || (file1.trim().length() == 0) ) {
		if ( __path1_JButton != null ) {
			__path1_JButton.setEnabled ( false );
		}
		do_return = true;
	}
	file2 = __file2_JTextField.getText();
	if ( (file2 == null) || (file2.trim().length() == 0) ) {
		if ( __path2_JButton != null ) {
			__path2_JButton.setEnabled ( false );
		}
		do_return = true;
	}
	if ( do_return ) {
		return;
	}
	__command_JTextField.setText("statemodMax(\"" + file1 + "\",\"" +
			file2 + "\")" );
	__command_Vector.removeAllElements();
	__command_Vector.addElement ( __command_JTextField.getText() );
	// Check the path and determine what the label on the path button should
	// be...
	if ( __path1_JButton != null ) {
		__path1_JButton.setEnabled ( true );
		File f = new File ( file1 );
		if ( f.isAbsolute() ) {
			__path1_JButton.setText (
			"Remove Working Directory (File 1)" );
		}
		else {	__path1_JButton.setText (
			"Add Working Directory (File 1)" );
		}
	}
	if ( __path2_JButton != null ) {
		__path2_JButton.setEnabled ( true );
		File f = new File ( file2 );
		if ( f.isAbsolute() ) {
			__path2_JButton.setText (
			"Remove Working Directory (File 2)" );
		}
		else {	__path2_JButton.setText (
			"Add Working Directory (File 2)" );
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

} // end statemodMax_JDialog
