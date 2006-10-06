// ----------------------------------------------------------------------------
// TSreadUsgsNwis_JDialog - editor for readUsgsNwis()
// ----------------------------------------------------------------------------
// Copyright:	See the COPYRIGHT file.
// ----------------------------------------------------------------------------
// History: 
//
// 2002-03-29	Steven A. Malers, RTi	Initial version (copy and modify
//					readStateMod).
// 2002-04-28	SAM, RTi		Fix bug where entering dates causes
//					warnings every key stroke.
// 2003-12-07	SAM, RTi		Update to Swing.
// 2004-02-17	SAM, RTi		Fix bug where directory from file
//					selection was not getting set as the
//					last dialog directory in JGUIUtil.
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

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import RTi.Util.GUI.JGUIUtil;
import RTi.Util.GUI.SimpleFileFilter;
import RTi.Util.GUI.SimpleJButton;
import RTi.Util.IO.IOUtil;
import RTi.Util.IO.PropList;
import RTi.Util.Message.Message;
import RTi.Util.String.StringUtil;
import RTi.Util.Time.DateTime;

/**
The TSreadUsgsNwis_JDialog edits the TS X = readUsgsNwis() command.
If the "WorkingDir" application property is defined, then the dialog will allow
the input file path to be truncated to a relative path.
*/
public class TSreadUsgsNwis_JDialog extends JDialog
implements ActionListener, KeyListener, WindowListener
{
private SimpleJButton	__browse_JButton = null,// File browse button
			__path_JButton = null,	// Convert between relative and
						// absolute path.
			__cancel_JButton = null,// Cancel Button
			__ok_JButton = null;	// Ok Button
private JFrame		__parent_JFrame = null;	// parent JFrame
private Vector		__command_Vector = null;// Command as Vector of String
private String		__working_dir = null;	// Working directory.
private JTextField	__alias_JTextField = null,// Alias for time series.
			__analysis_period_start_JTextField,
			__analysis_period_end_JTextField,
						// Text fields for analysis
						// period.
			__command_JTextField = null,
						// Command as JTextField
			__file_JTextField = null; // Field for time series
						// identifier
private boolean		__error_wait = false;	// Is there an error that we
						// are waiting to be cleared up
						// or Cancel?
private boolean		__first_time = true;

/**
TSreadUsgsNwis_JDialog constructor.
@param parent JFrame class instantiating this class.
@param app_PropList Properties from the application.
@param command Command to parse.
@param tsids Time series identifiers for available time series.
*/
public TSreadUsgsNwis_JDialog (	JFrame parent, PropList app_PropList,
				Vector command, Vector tsids )
{	super(parent, true);
	initialize ( parent, "Edit TS X = readUsgsNwis() Command",
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
		JFileChooser fc = new JFileChooser();
		fc.setDialogTitle( "Select USGS NWIS Time Series File");
		SimpleFileFilter
		sff = new SimpleFileFilter("txt",
			"USGS NWIS Daily Time Series File");
		fc.addChoosableFileFilter(sff);
		
		String last_directory_selected =
			JGUIUtil.getLastFileDialogDirectory();
		if ( last_directory_selected != null ) {
			fc.setCurrentDirectory(
				new File(last_directory_selected));
		}
		else {	fc.setCurrentDirectory(new File(__working_dir));
		}
		if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			String directory = fc.getSelectedFile().getParent();
			String filename = fc.getSelectedFile().getName(); 
			String path = fc.getSelectedFile().getPath(); 
	
			if (filename == null || filename.equals("")) {
				return;
			}
	
			String fs = System.getProperty ("file.separator");
			if (path != null) {
				__file_JTextField.setText(path );
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
		checkInput ();
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
				"TSreadUsgsNwis_JDialog",
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
	String alias = __alias_JTextField.getText().trim();
	String routine = "TSreadUsgsNwis_JDialog.checkInput";
	String analysis_period_start =
		__analysis_period_start_JTextField.getText().trim();
	String analysis_period_end =
		__analysis_period_end_JTextField.getText().trim();
	String warning = "";
	// Adjust the working directory that was passed in by the specified
	// directory.  If the directory does not exist, warn the user...
	try {	String adjusted_path = IOUtil.adjustPath ( __working_dir, file);
		File f = new File ( adjusted_path );
		if ( !f.exists() ) {
			warning +=
			"The USGS NWIS file does not exist:\n" +
			"    " + adjusted_path + "\n" +
		  	"Correct or Cancel.";
		}
		f = null;
	}
	catch ( Exception e ) {
		warning +=
		"\nThe working directory:\n" +
		"    \"" + __working_dir + "\"\ncannot be adjusted using:\n" +
		"    \"" + file + "\".\n" +
		"Correct the file or Cancel.";
	}
	DateTime startdate = null, enddate = null;
	if (	!analysis_period_start.equals("*") &&
		!analysis_period_start.equalsIgnoreCase("OutputStart") &&
		!analysis_period_start.equalsIgnoreCase("OutputEnd") &&
		!analysis_period_start.equalsIgnoreCase("QueryStart") &&
		!analysis_period_start.equalsIgnoreCase("QueryEnd") ) {
		try {	startdate = DateTime.parse(
				analysis_period_start);
		}
		catch ( Exception e ) {
			warning +=
				"\nStart date/time \"" + analysis_period_start +
				"\" is not a valid date/time.\n"+
				"Specify a date, *, or press Cancel.";
		}
	}
	if (	!analysis_period_end.equals("*") &&
		!analysis_period_end.equalsIgnoreCase("OutputStart") &&
		!analysis_period_end.equalsIgnoreCase("OutputEnd") &&
		!analysis_period_end.equalsIgnoreCase("QueryStart") &&
		!analysis_period_end.equalsIgnoreCase("QueryEnd") ) {
		try {	enddate = DateTime.parse(analysis_period_end);
		}
		catch ( Exception e ) {
			warning +=
				"\nEnd date/time \"" + analysis_period_end +
				"\" is not a valid date/time.\n"+
				"Specify a date, *, or press Cancel.";
		}
	}
	if ( (startdate != null) && (enddate != null) ) {
		if ( startdate.getPrecision() != enddate.getPrecision() ) {
			warning +=
				"\nStart and end date/time have different" +
				" precision";
		}
		if ( startdate.greaterThan(enddate) ) {
			warning +=
				"\nStart date/time is later than the end " +
				"date/time.";
		}
	}
	if ( alias.equals("") ) {
		warning +=
		"\nAlias must be set, or press Cancel";
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
{	__alias_JTextField = null;
	__browse_JButton = null;
	__cancel_JButton = null;
	__command_JTextField = null;
	__command_Vector = null;
	__file_JTextField = null;
	__ok_JButton = null;
	__parent_JFrame = null;
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
@param parent JFrame class instantiating this class.
@param title Dialog title.
@param app_PropList Properties from application.
@param command Vector of String containing the command.
@param tsids Time series identifiers - ignored.
*/
private void initialize ( JFrame parent, String title, PropList app_PropList,
			Vector command, Vector tsids )
{	__parent_JFrame = parent;
	__command_Vector = command;
	__working_dir = app_PropList.getValue ( "WorkingDir" );

try {
	addWindowListener( this );

        Insets insetsTLBR = new Insets(2,2,2,2);

	JPanel main_JPanel = new JPanel();
	main_JPanel.setLayout( new GridBagLayout() );
	GridBagConstraints gbc = new GridBagConstraints();
	getContentPane().add ( "North", main_JPanel );
	int y = 0;

        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Read a single time series from a USGS NWIS format file (" +
		"currently only daily streamflow is supported)." ), 
		0, y, 7, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Specify a full path or relative path (relative to working " +
		"directory) for a USGS NWIS file to read." ), 
		0, ++y, 7, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.WEST);
       	JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Specifying the period will limit data that are available " +
		"for fill commands but can increase performance." ), 
		0, ++y, 7, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.WEST);
       	JGUIUtil.addComponent(main_JPanel, new JLabel (
		"If not specified, the period defaults to the query period."),
		0, ++y, 7, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.WEST);

	if ( __working_dir != null ) {
        	JGUIUtil.addComponent(main_JPanel, new JLabel (
		"The working directory is: " + __working_dir ), 
		0, ++y, 7, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.WEST);
	}

        JGUIUtil.addComponent(main_JPanel, new JLabel ( "Time Series Alias:"),
		0, ++y, 1, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.EAST);
	__alias_JTextField = new JTextField ( 30 );
	__alias_JTextField.addKeyListener ( this );
	JGUIUtil.addComponent(main_JPanel, __alias_JTextField,
		1, y, 3, 1, 1, 0, insetsTLBR, gbc.HORIZONTAL, gbc.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"USGS NWIS File to Read:" ), 
		0, ++y, 1, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.EAST);
	__file_JTextField = new JTextField ( 50 );
	__file_JTextField.addKeyListener ( this );
        JGUIUtil.addComponent(main_JPanel, __file_JTextField,
		1, y, 5, 1, 1, 0, insetsTLBR, gbc.HORIZONTAL, gbc.WEST);
	__browse_JButton = new SimpleJButton ( "Browse", this );
        JGUIUtil.addComponent(main_JPanel, __browse_JButton,
		6, y, 1, 1, 1, 0, insetsTLBR, gbc.HORIZONTAL, gbc.CENTER);

	JGUIUtil.addComponent(main_JPanel, new JLabel ( "Period to Read:" ), 
		0, ++y, 1, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.EAST);
	__analysis_period_start_JTextField = new JTextField ( "*", 15 );
	__analysis_period_start_JTextField.addKeyListener ( this );
	JGUIUtil.addComponent(main_JPanel, __analysis_period_start_JTextField,
		1, y, 2, 1, 1, 0, insetsTLBR, gbc.HORIZONTAL, gbc.WEST);
	JGUIUtil.addComponent(main_JPanel, new JLabel ( "to" ), 
		3, y, 1, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.CENTER);
	__analysis_period_end_JTextField = new JTextField ( "*", 15 );
	__analysis_period_end_JTextField.addKeyListener ( this );
	JGUIUtil.addComponent(main_JPanel, __analysis_period_end_JTextField,
		4, y, 2, 1, 1, 0, insetsTLBR, gbc.HORIZONTAL, gbc.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel ( "Command:"),
		0, ++y, 1, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.EAST);
	__command_JTextField = new JTextField ( 60 );
	__command_JTextField.setEditable ( false );
	JGUIUtil.addComponent(main_JPanel, __command_JTextField,
		1, y, 6, 1, 1, 0, insetsTLBR, gbc.HORIZONTAL, gbc.WEST);

	// Refresh the contents...
	refresh ();

	// South Panel: North
	JPanel button_JPanel = new JPanel();
	button_JPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JGUIUtil.addComponent(main_JPanel, button_JPanel, 
		0, ++y, 8, 1, 1, 0, insetsTLBR, gbc.HORIZONTAL, gbc.CENTER);

	if ( __working_dir != null ) {
		// Add the button to allow conversion to/from relative
		// path...
		__path_JButton = new SimpleJButton(
			"Remove Working Directory", this);
		button_JPanel.add ( __path_JButton );
	}
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
	refresh();	// Sets the __path_JButton status
        super.setVisible( true );
}
catch  ( Exception e ) {
	Message.printWarning ( 2, "", e );
}
}

/**
Respond to KeyEvents.
*/
public void keyPressed ( KeyEvent event )
{	int code = event.getKeyCode();

	// Don't want key input to refresh dates unless enter or tab...
	if (	(event.getSource() == __analysis_period_start_JTextField) ||
		(event.getSource() == __analysis_period_end_JTextField) ) {
		if ( (code == event.VK_ENTER) || (code == event.VK_TAB) ) {
			refresh ();
		}
	}
	else {	refresh();
	}
}

/**
Need this to properly capture key events, especially deletes.
*/
public void keyReleased ( KeyEvent event )
{	if (	(event.getSource() == __analysis_period_start_JTextField) ||
		(event.getSource() == __analysis_period_end_JTextField) ) {
		return;
	}
	else {	refresh();
	}
}

public void keyTyped ( KeyEvent event )
{
}

/**
Refresh the command from the other text field contents.
*/
private void refresh ()
{	String alias = "";
	String file = "";
	__error_wait = false;
	String analysis_period_start = "*";
	String analysis_period_end = "*";
	if ( __first_time ) {
		__first_time = false;
		if ( ((String)__command_Vector.elementAt(0)).indexOf('=')>=0){
			// Parse an existing command
			// Assume TS X = readUsgsNwis(file,start,end)
			Vector v = StringUtil.breakStringList (
			((String)__command_Vector.elementAt(0)).trim(),"=",
			StringUtil.DELIM_SKIP_BLANKS);
			if ( (v != null) && (v.size() == 2) ) {
				// First field has format "TS X"
				alias = ((String)v.elementAt(0)).trim();
				alias = alias.substring(2).trim();
				__alias_JTextField.setText ( alias );
				// Don's skip blanks to parse right side of
				// equals sign...
				v = StringUtil.breakStringList ( ((String)
					v.elementAt(1)).trim(),
					"(),", StringUtil.DELIM_ALLOW_STRINGS );
				if ( (v != null) && (v.size() >= 2) ) {
					// Second field is file...
					file = ((String)v.elementAt(1)).trim();
					__file_JTextField.setText ( file );
				}
				if ( (v != null) && (v.size() >= 3) ) {
					// Third field is file...
					analysis_period_start =
					((String)v.elementAt(2)).trim();
					__analysis_period_start_JTextField.
					setText ( analysis_period_start );
				}
				if ( (v != null) && (v.size() >= 4) ) {
					analysis_period_end =
					((String)v.elementAt(3)).trim();
					__analysis_period_end_JTextField.
					setText ( analysis_period_end );
				}
			}
		}
	}
	// Regardless, reset the command from the fields...
	alias = __alias_JTextField.getText().trim();
	file = __file_JTextField.getText().trim();
	analysis_period_start=
		__analysis_period_start_JTextField.getText().trim();
	analysis_period_end = __analysis_period_end_JTextField.getText().trim();
	if ( (file == null) || (file.length() == 0) ) {
		if ( __path_JButton != null ) {
			__path_JButton.setEnabled ( false );
		}
		return;
	}
	if ( alias.equals("") ) {
		__command_JTextField.setText("");
	}
	else {	__command_JTextField.setText("TS " + alias +
		" = readUsgsNwis(\"" + file +
		"\"," + analysis_period_start + "," + analysis_period_end +")"); 
	}
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

} // end TSreadUsgsNwis_JDialog
