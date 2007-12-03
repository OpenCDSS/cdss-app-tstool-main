// ----------------------------------------------------------------------------
// writeStateCU_JDialog - editor for writeStateCU()
// ----------------------------------------------------------------------------
// Copyright:	See the COPYRIGHT file.
// ----------------------------------------------------------------------------
// History: 
//
// 2004-02-13	Steven A. Malers, RTi	Initial version (copy and modify
//					writeStateMod_JDialog).
// 2004-02-17	SAM, RTi		Fix bug where directory from file
//					selection was not getting set as the
//					last dialog directory in JGUIUtil.
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

import javax.swing.JFileChooser;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import rti.tscommandprocessor.core.TSCommandProcessor;
import rti.tscommandprocessor.core.TSCommandProcessorUtil;

import java.io.File;
import java.util.Vector;

import RTi.Util.GUI.JFileChooserFactory;
import RTi.Util.GUI.JGUIUtil;
import RTi.Util.GUI.SimpleFileFilter;
import RTi.Util.GUI.SimpleJButton;
import RTi.Util.IO.Command;
import RTi.Util.IO.IOUtil;
import RTi.Util.IO.PropList;
import RTi.Util.Message.Message;
import RTi.Util.String.StringUtil;

public class writeStateCU_JDialog extends JDialog
implements ActionListener, KeyListener, WindowListener
{
private SimpleJButton	__browse_JButton = null,// Button to browse for file
			__cancel_JButton = null,// Cancel Button
			__ok_JButton = null,	// Ok Button
			__path_JButton = null;	// Convert between relative and
						// absolute paths
private Vector		__command_Vector = null; // Command as Vector of String
private Command __Command = null;   // FIXME SAM 2007-12-01 change to __command when Command class is implemented
private JTextField	__command_JTextField=null;// Command as JTextField
private String		__working_dir = null;	// Working directory.
private JTextField	__file_JTextField = null;// Field for time series
						// identifier
private boolean		__error_wait = false;	// Is there an error that we
						// are waiting to be cleared up
						// or Cancel?
private boolean		__first_time = true;

/**
writeStateCU_JDialog constructor.
@param parent JFrame class instantiating this class.
@param app_PropList Properties from the application.
@param command Command to parse.
@param tsids Time series identifiers for available time series.
*/
public writeStateCU_JDialog ( JFrame parent, PropList app_PropList, Vector command, Vector tsids, Command command_class )
{	super(parent, true);
	initialize ( parent, "Edit WriteStateCU() Command", app_PropList, command, tsids, command_class );
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
		fc.setDialogTitle("Select StateCU Time Series File to Write");
		SimpleFileFilter sff = new SimpleFileFilter("stm",
			"StateCU Frost Dates File");
		fc.addChoosableFileFilter(sff);
		
		if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
			String directory = fc.getSelectedFile().getParent();
			String filename = fc.getSelectedFile().getName(); 
			String path = fc.getSelectedFile().getPath(); 
	
			if (filename == null || filename.equals("")) {
				return;
			}
	
			if (path != null) {
				__file_JTextField.setText(path );
				JGUIUtil.setLastFileDialogDirectory( directory);
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
			__file_JTextField.setText (
			IOUtil.toAbsolutePath(__working_dir,
			__file_JTextField.getText() ) );
		}
		else if ( __path_JButton.getText().equals(
			"Remove Working Directory") ) {
			try {	__file_JTextField.setText (
				IOUtil.toRelativePath ( __working_dir,
				__file_JTextField.getText() ) );
			}
			catch ( Exception e ) {
				Message.printWarning ( 1,
				"writeStateCU_JDialog",
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
{	String file = __file_JTextField.getText().trim();
	String warning = "";
	// Adjust the working directory that was passed in by the specified
	// directory.  If the directory does not exist, warn the user...
	try {	String adjusted_path = IOUtil.adjustPath ( __working_dir, file);
		File f = new File ( adjusted_path );
		File f2 = new File ( f.getParent() );
		if ( !f2.exists() ) {
			warning +=
				"\nThe StateCU file parent directory does " +
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
	if ( warning.length() > 0 ) {
		__error_wait = true;
		Message.printWarning ( 1, "writeStateCU_JDialog", warning );
	}
}

/**
Free memory for garbage collection.
*/
protected void finalize ()
throws Throwable
{	__cancel_JButton = null;
	__command_JTextField = null;
	__file_JTextField = null;
	__command_Vector = null;
	__browse_JButton = null;
	__ok_JButton = null;
	__path_JButton = null;
	__working_dir = null;
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
@param command Vector of String containing the command.
@param tsids Time series identifiers from
TSEngine.getTSIdentifiersFromCommands().
*/
private void initialize ( JFrame parent, String title, PropList app_PropList,
			Vector command, Vector tsids, Command command_class )
{	__command_Vector = command;
    __Command = command_class;
	__working_dir = TSCommandProcessorUtil.getWorkingDirForCommand ( (TSCommandProcessor)__Command.getCommandProcessor(), __Command );

	addWindowListener( this );

        Insets insetsTLBR = new Insets(2,2,2,2);

	// Main panel...

	JPanel main_JPanel = new JPanel();
	main_JPanel.setLayout( new GridBagLayout() );
	getContentPane().add ( "North", main_JPanel );
	int y = 0;

        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"THIS COMMAND CURRENTLY ONLY WRITES StateCU FROST DATE FILES."),
		0, y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Write in-memory frost date time series to a StateCU " +
		"format file," +
		" which can be specified using a full or" ),
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
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
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Time series with data types FrostDateL28S, FrostDateL32S," +
		" FrostDateF32F, and FrostDateF28F are processed." ),
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"The time series location is used to find a time series of " +
		"each data type, for output." ),
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"StateCU File to Write:" ), 
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST );
	__file_JTextField = new JTextField ( 50 );
	__file_JTextField.addKeyListener ( this );
        JGUIUtil.addComponent(main_JPanel, __file_JTextField,
		1, y, 5, 1, 1, 0, insetsTLBR, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST );
	__browse_JButton = new SimpleJButton ( "Browse", this );
        JGUIUtil.addComponent(main_JPanel, __browse_JButton,
		6, y, 1, 1, 1, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.CENTER);

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
		__path_JButton = new SimpleJButton(
			"Remove Working Directory", this);
		button_JPanel.add ( __path_JButton );
	}
	__cancel_JButton = new SimpleJButton( "Cancel", this);
	button_JPanel.add ( __cancel_JButton );
	__ok_JButton = new SimpleJButton( "OK", this);
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
{	String file = "";
	__error_wait = false;
	if ( __first_time ) {
		__first_time = false;
		// Parse the incoming string and fill the fields...
		Vector v = StringUtil.breakStringList (
			((String)__command_Vector.elementAt(0)).trim(),"() ,",
			StringUtil.DELIM_SKIP_BLANKS |
			StringUtil.DELIM_ALLOW_STRINGS );
		if ( (v != null) && (v.size() >= 2) ) {
			// Second field is file...
			file = ((String)v.elementAt(1)).trim();
			__file_JTextField.setText ( file );
		}
	}
	// Regardless, reset the command from the fields...
	file = __file_JTextField.getText();
	if ( (file == null) || (file.trim().length() == 0) ) {
		if ( __path_JButton != null ) {
			__path_JButton.setEnabled ( false );
		}
		return;
	}
	__command_JTextField.setText("writeStateCU(\"" + file + "\")" );
	__command_Vector.removeAllElements();
	__command_Vector.addElement ( __command_JTextField.getText() );
	// Check the path and determine what the label on the path button should
	// be...
	if ( __path_JButton != null ) {
		__path_JButton.setEnabled ( true );
		File f = new File ( file );
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

} // end writeStateCU_JDialog
