// ----------------------------------------------------------------------------
// setWorkingDir_JDialog - editor for setWorkingDir()
// ----------------------------------------------------------------------------
// Copyright:	See the COPYRIGHT file.
// ----------------------------------------------------------------------------
// History: 
//
// 28 Feb 2001	Steven A. Malers, RTi	Initial version (copy and modify
//					runningAverage_Dialog).
// 31 Aug 2001	SAM, RTi		Add comments at top and add Browse
//					button.
// 2002-03-29	SAM, RTi		Minor cleanup.
// 2002-04-05	SAM, RTi		More minor cleanup to make the dialog
//					look better.
// 2003-11-06	SAM, RTi		* Update to Swing.
//					* Pass in the current working directory
//					  to allow phasing in of relative shifts
//					  in the working directory.
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
import RTi.Util.GUI.SimpleJButton;
import RTi.Util.GUI.SimpleJComboBox;
import RTi.Util.IO.IOUtil;
import RTi.Util.IO.PropList;
import RTi.Util.Message.Message;
import RTi.Util.String.StringUtil;

public class setWorkingDir_JDialog extends JDialog
implements ActionListener, ItemListener, KeyListener, WindowListener
{
private final String GUI_ONLY = "GUIOnly";
private final String GUI_AND_BATCH = "GUIAndBatch";

private SimpleJButton	__browse_JButton = null,// directory browse button
			__cancel_JButton = null,// Cancel Button
			__ok_JButton = null;	// Ok Button
private JFrame		__parent_JFrame = null;	// parent JFrame
private Vector		__command_Vector = null; // Command as Vector of String
private String		__working_dir = null;	// Working directory.
private JTextField	__command_JTextField = null;
						// Command as JTextField
private SimpleJComboBox	__type_JComboBox = null;// Field for GUI or GUI & batch
private JTextField	__dir_JTextField = null;// Field for working directory
private boolean		__error_wait = false;	// Is there an error that we
						// are waiting to be cleared up
						// or Cancel?
private boolean		__first_time = true;

/**
setWorkingDir_JDialog constructor.
@param parent JFrame class instantiating this class.
@param app_PropList Properties from the application.
@param command Command to parse.
@param tsids Time series identifiers for time series available to convert -
ignored.
*/
public setWorkingDir_JDialog (	JFrame parent, PropList app_PropList,
				Vector command, Vector tsids )
{	super(parent, true);
	initialize ( parent, "Edit setWorkingDir() Command",
		app_PropList, command, tsids );
}

/**
Responds to ActionEvents.
@param event ActionEvent object
*/
public void actionPerformed( ActionEvent event )
{	Object o = event.getSource();

	if ( o == __browse_JButton ) {
		// Browse for the file to read...
		JFileChooser fc = JFileChooserFactory.createJFileChooser(
			JGUIUtil.getLastFileDialogDirectory() );
		fc.setDialogTitle("Select the Working Directory" );
		fc.setFileSelectionMode ( JFileChooser.DIRECTORIES_ONLY );
		if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			String directory = fc.getSelectedFile().getPath();
			if (directory != null) {
				JGUIUtil.setLastFileDialogDirectory(directory);
			}
			__dir_JTextField.setText (directory);
			refresh ();
		}
	}

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
{	String dir = __dir_JTextField.getText().trim();
	String routine = "setWorkingDir_JDialog.checkInput";

	String warning = "";

	// Adjust the working directory that was passed in by the specified
	// directory.  If the directory does not exist, warn the user...
	try {	String adjusted_path = IOUtil.adjustPath ( __working_dir, dir);
		File f = new File ( adjusted_path );
		if ( !f.exists() ) {
			warning +=
			"\nThe working directory does " +
			"not exist:\n    " + adjusted_path + "\n" +
		  	"Correct or Cancel.";
		}
		if ( !f.isDirectory() ) {
			warning +=
			"\nWorking directory \"" + adjusted_path +
			"\" is not a directory.  Correct or Cancel.";
		}
		f = null;
	}
	catch ( Exception e ) {
		warning +=
			"\nThe working directory:\n" +
			"    \"" + __working_dir +
			"\"\ncannot be adjusted using:\n" +
			"    \"" + dir + "\".\n" +
		"Correct the file or Cancel.";
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
{	__browse_JButton = null;
	__type_JComboBox = null;
	__dir_JTextField = null;
	__browse_JButton = null;
	__cancel_JButton = null;
	__command_JTextField = null;
	__command_Vector = null;
	__ok_JButton = null;
	__parent_JFrame = null;
	__working_dir = null;
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
@param app_PropList Properies from the application.
@param command Vector of String containing the command.
@param tsids Time series identifiers - ignored.
*/
private void initialize (	JFrame parent, String title,
				PropList app_PropList, Vector command,
				Vector tsids )
{	__parent_JFrame = parent;
	__command_Vector = command;
	__working_dir = app_PropList.getValue ( "WorkingDir" );

	addWindowListener( this );

        Insets insetsTLBR = new Insets(0,2,0,2);

	JPanel main_JPanel = new JPanel();
	main_JPanel.setLayout( new GridBagLayout() );
	GridBagConstraints gbc = new GridBagConstraints();
	getContentPane().add ( "North", main_JPanel );
	int y = 0;

        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Set the working directory to precede relative paths in " +
		"commands.  If browsing for files while editing other "),
		0, y, 8, 1, 0, 0, insetsTLBR, gbc.BOTH, gbc.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"commands, you may need to edit the selected paths to be " +
		"relative to the working directory.  If applied" ),
		0, ++y, 8, 1, 0, 0, insetsTLBR, gbc.BOTH, gbc.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"to the GUI only, then running TSTool in batch mode will use" +
		" the starting directory as the working directory." ),
		0, ++y, 8, 1, 0, 0, insetsTLBR, gbc.BOTH, gbc.WEST);
	if ( __working_dir != null ) {
        	JGUIUtil.addComponent(main_JPanel, new JLabel (
		"The working directory is currently: " + __working_dir ), 
		0, ++y, 7, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.WEST);
	}

        JGUIUtil.addComponent(main_JPanel, new JLabel ("Working Directory:"),
		0, ++y, 1, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.EAST);
	__dir_JTextField = new JTextField ( 55 );
	__dir_JTextField.addKeyListener ( this );
        JGUIUtil.addComponent(main_JPanel, __dir_JTextField,
		1, y, 6, 1, 1, 0, insetsTLBR, gbc.HORIZONTAL, gbc.WEST);
	__browse_JButton = new SimpleJButton ( "Browse", this );
        JGUIUtil.addComponent(main_JPanel, __browse_JButton,
		7, y, 1, 1, 1, 0, insetsTLBR, gbc.HORIZONTAL, gbc.CENTER);

        JGUIUtil.addComponent(main_JPanel, new JLabel ( "Apply to:" ), 
		0, ++y, 1, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.EAST);
	__type_JComboBox = new SimpleJComboBox ();
	__type_JComboBox.add ( GUI_ONLY );
	__type_JComboBox.add ( GUI_AND_BATCH );
	__type_JComboBox.addItemListener ( this );
        JGUIUtil.addComponent(main_JPanel, __type_JComboBox,
		1, y, 1, 1, 1, 0, insetsTLBR, gbc.NONE, gbc.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel ( "Command:" ), 
		0, ++y, 1, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.EAST);
	__command_JTextField = new JTextField ( 60 );
	__command_JTextField.setEditable ( false );
	JGUIUtil.addComponent(main_JPanel, __command_JTextField,
		1, y, 7, 1, 1, 0, insetsTLBR, gbc.HORIZONTAL, gbc.CENTER);

	// Refresh the contents...
	refresh ();

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
{	int code = event.getKeyCode();

	if ( (code == event.VK_ENTER) || (code == event.VK_TAB) ) {
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
{	
}

public void keyTyped ( KeyEvent event ) {;}

/**
Refresh the command from the other text field contents.  The command is
of the form:
<pre>
setWorkingDir("directory",Type)
</pre>
*/
private void refresh ()
{	String type = "";
	String dir = "";
	__error_wait = false;
	if ( __first_time ) {
		__first_time = false;
		// Parse the incoming string and fill the fields...
		Vector v = StringUtil.breakStringList (
			((String)__command_Vector.elementAt(0)).trim(),"() ,",
			StringUtil.DELIM_SKIP_BLANKS |
			StringUtil.DELIM_ALLOW_STRINGS );
		if ( (v != null) && (v.size() == 3) ) {
			// Second field is directory...
			dir = ((String)v.elementAt(1)).trim();
			__dir_JTextField.setText ( dir );
			// Third field has type...
			type = ((String)v.elementAt(2)).trim();
			if (	JGUIUtil.isSimpleJComboBoxItem(__type_JComboBox,
				type, JGUIUtil.NONE, null, null ) ) {
				__type_JComboBox.select ( type );
			}
			else {	Message.printWarning ( 1,
				"setWorkingDir_JDialog.refresh",
				"Existing setWorkingDir() references a " +
				"non-existent\n"+
				"apply-to type \"" + type + "\".  Select a\n" +
				"different choice or Cancel." );
			}
		}
	}
	// Regardless, reset the command from the fields...
	dir = __dir_JTextField.getText();
	type = __type_JComboBox.getSelected();
	if ( (dir == null) || (dir.trim().length() == 0) ) {
		return;
	}
	if ( (type == null) || (type.trim().length() == 0) ) {
		return;
	}
	__command_JTextField.setText("setWorkingDir(\""+dir + "\"," + type+")");
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

} // end setWorkingDir_JDialog
