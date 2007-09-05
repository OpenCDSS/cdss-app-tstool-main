// ----------------------------------------------------------------------------
// createFromList_JDialog - editor for createFromList()
// ----------------------------------------------------------------------------
// Copyright:	See the COPYRIGHT file.
// ----------------------------------------------------------------------------
// History: 
//
// 20 Feb 2001	Steven A. Malers, RTi	Initial version.
// 31 Aug 2001	SAM, RTi		Add additional comments at top and
//					source place-holder for later use.
//					Enable the browse button.
// 2002-04-05	SAM, RTi		Rework interface to be cleaner.  Add
//					ability to add/remove working directory.
// 2002-05-08	SAM, RTi		Fix so path button is available even if
//					command is not totally specified.
// 2003-12-15	SAM, RTi		* Update to Swing.
//					* Enable the data source.
// 2004-02-17	SAM, RTi		Fix bug where directory from file
//					selection was not getting set as the
//					last dialog directory in JGUIUtil.
// 2004-07-21	SAM, RTi		Update to be more generic and use free
//					format parameter lists.
// 2007-02-26	SAM, RTi		Clean up code based on Eclipse feedback.
// 2007-03-12	SAM, RTi		Quick fix to add DefaultUnits to address
//					problem with a later add() command.
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
import RTi.Util.GUI.SimpleFileFilter;
import RTi.Util.GUI.SimpleJButton;
import RTi.Util.GUI.SimpleJComboBox;
import RTi.Util.IO.IOUtil;
import RTi.Util.IO.PropList;
import RTi.Util.Message.Message;
import RTi.Util.String.StringUtil;
import RTi.Util.Time.TimeInterval;

public class createFromList_JDialog extends JDialog
implements ActionListener, ItemListener, KeyListener, WindowListener
{

private final String __DefaultMissingTS = "DefaultMissingTS";
private final String __IgnoreMissingTS = "IgnoreMissingTS";

private SimpleJButton	__browse_JButton = null,// Browse for file.
			__cancel_JButton = null,// Cancel Button
			__ok_JButton = null,	// Ok Button
			__path_JButton = null;	// Convert between relative and
						// absolute paths
private Vector		__command_Vector = null;// Command as Vector of String
private String		__working_dir = null;	// Working directory.
private JTextField	__command_JTextField=null;
						// Command as TextField
private JTextField	__ListFile_JTextField = null;
						// Field for file name
private JTextField	__ID_JTextField = null;	// IDs to process file
private SimpleJComboBox	__IDCol_JComboBox = null;
						// ID column in file
// TODO SAM
// private SimpleJComboBox	__Delim_JComboBox = null;
						// Delimiter for file
private JTextField	__DataSource_JTextField = null;
						// Field for time series
						// data source.
private JTextField	__DataType_JTextField = null;
						// Field for time series
						// data type.
private JTextField	__Interval_JTextField = null;
						// Data Interval.
private JTextField	__Scenario_JTextField = null;
						// Scenario.
private JTextField	__InputType_JTextField = null;
						// Input type.
private JTextField	__InputName_JTextField = null;
						// Input name.
private JTextField	__Delim_JTextField = null;
						// Delimiter character(s).
private SimpleJComboBox	__HandleMissingTSHow_JComboBox = null;
						// Time series available to
						// operate on.
private JTextField	__DefaultUnits_JTextField = null;
						// Default units when blank time series is created.
private boolean		__error_wait = false;	// Is there an error that we
						// are waiting to be cleared up
						// or Cancel?
private boolean		__first_time = true;

/**
createFromList_JDialog constructor.
@param parent JFrame class instantiating this class.
@param app_PropList Properties from the application.
@param command Command to parse.
@param tsids Time series identifiers for existing time series.
*/
public createFromList_JDialog (	JFrame parent, PropList app_PropList,
				Vector command, Vector tsids )
{	super ( parent, true );
	initialize (	parent, "Edit createFromList() Command", app_PropList,
			command, tsids );
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
		fc.setDialogTitle( "Select List File");
		SimpleFileFilter sff = new SimpleFileFilter("txt", "List File");
		fc.addChoosableFileFilter(sff);
		sff = new SimpleFileFilter("csv", "List File");
		fc.addChoosableFileFilter(sff);
		
		if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			String directory = fc.getSelectedFile().getParent();
			String filename = fc.getSelectedFile().getName(); 
			String path = fc.getSelectedFile().getPath(); 
	
			if (filename == null || filename.equals("")) {
				return;
			}
	
			if (path != null) {
				__ListFile_JTextField.setText(path );
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
		checkInput();
		if ( !__error_wait ) {
			response ( 1 );
		}
	}
	else if ( o == __path_JButton ) {
		if (	__path_JButton.getText().equals(
			"Add Working Directory") ) {
			__ListFile_JTextField.setText (
			IOUtil.toAbsolutePath(__working_dir,
			__ListFile_JTextField.getText() ) );
		}
		else if ( __path_JButton.getText().equals(
			"Remove Working Directory") ) {
			try {	__ListFile_JTextField.setText (
				IOUtil.toRelativePath ( __working_dir,
				__ListFile_JTextField.getText() ) );
			}
			catch ( Exception e ) {
				Message.printWarning ( 1,
				"createFromList_JDialog",
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
{	String ListFile = __ListFile_JTextField.getText().trim();
	// TODO SAM
	//String InputType = __InputType_JTextField.getText().trim();
	String Interval = __Interval_JTextField.getText().trim();
	String routine = "createFromList_JDialog.checkInput";
	String warning = "";
	// Adjust the working directory that was passed in by the specified
	// directory.  If the directory does not exist, warn the user...
	if ( ListFile.length() == 0 ) {
		warning +=
		"The list file must be specified.";
	}
	else {
	try {	String adjusted_path = IOUtil.adjustPath ( __working_dir,
			ListFile);
		File f = new File ( adjusted_path );
		if ( !f.exists() && !f.isFile() ) {
			warning +=
			"The list file does not exist:\n" +
			"    " + adjusted_path + "\n" +
		  	"Correct or Cancel.";
		}
		f = null;
	}
	catch ( Exception e ) {
		warning +=
		"\nThe working directory:\n" +
		"    \"" + __working_dir + "\"\ncannot be adjusted using:\n" +
		"    \"" + ListFile + "\".\n" +
		"Correct the file or Cancel.";
	}
	}
	try {	TimeInterval.parseInterval ( Interval );
	}
	catch ( Exception e ) {
		warning += 
			"\nThe Interval \"" + Interval +
			"\" is not recognized.\n"+
			"Specify a valid Interval (e.g., Day, Month, Year).";
	}
	if ( Interval.length() == 0 ) {
		warning +=
		"The interval must be specified.";
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
{	__ListFile_JTextField = null;
	__cancel_JButton = null;
	__command_JTextField = null;
	__command_Vector = null;
	__HandleMissingTSHow_JComboBox = null;
	__DataType_JTextField = null;
	__Interval_JTextField = null;
	__DefaultUnits_JTextField = null;
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
{	if (	(__command_Vector == null) || (__command_Vector.size() == 0) ||
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
@param tsids Time series identifiers to compare alias to.
*/
private void initialize (	JFrame parent, String title,
				PropList app_PropList, Vector command,
				Vector tsids )
{	__command_Vector = command;
	__working_dir = app_PropList.getValue ( "WorkingDir" );

	addWindowListener( this );

        Insets insetsTLBR = new Insets(0,2,0,2);

	// Main panel...

	JPanel main_JPanel = new JPanel();
	main_JPanel.setLayout( new GridBagLayout() );
	getContentPane().add ( "North", main_JPanel );
	int y = 0;

        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Create a list of time series from a list of identifiers " +
		"in a file." ),
		0, y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"The information specified below is used with the identifiers" +
		" to create time series identifiers,"),
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"which are then used to read the time series.  The " +
		"identifiers are of the form:"),
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"  ID.DataSource.DataType.Interval.Scenario~InputType~" +
		"InputName"),
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"This command is useful for automating time series creation " +
		"where there is a one-to-one relationship between data."),
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"The list file can contain comment lines starting with #." ), 
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"It is recommended that the path to the file be specified " +
		"using a relative path."),
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
	if ( __working_dir != null ) {
        	JGUIUtil.addComponent(main_JPanel, new JLabel (
		"The working directory is: " + __working_dir ), 
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
	}

        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"List file to read:" ), 
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	__ListFile_JTextField = new JTextField ( 50 );
	__ListFile_JTextField.addKeyListener ( this );
        JGUIUtil.addComponent(main_JPanel, __ListFile_JTextField,
		1, y, 5, 1, 1, 0, insetsTLBR, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
	__browse_JButton = new SimpleJButton ( "Browse", this );
        JGUIUtil.addComponent(main_JPanel, __browse_JButton,
		6, y, 1, 1, 1, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.CENTER);

        JGUIUtil.addComponent(main_JPanel, new JLabel ( "ID column:" ), 
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	__IDCol_JComboBox = new SimpleJComboBox ( false );
	Vector IDCol_Vector = new Vector ( 100 );
	for ( int i = 1; i < 101; i++ ) {
		IDCol_Vector.addElement ( "" + i );
	}
	__IDCol_JComboBox.setData ( IDCol_Vector );
	__IDCol_JComboBox.addItemListener ( this );
        JGUIUtil.addComponent(main_JPanel, __IDCol_JComboBox,
		1, y, 2, 1, 1, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Indicate the ID column in the list file."),
		3, y, 3, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST );

        JGUIUtil.addComponent(main_JPanel, new JLabel ( "Delim:" ), 
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	__Delim_JTextField = new JTextField ( "", 20 );
	__Delim_JTextField.addKeyListener ( this );
        JGUIUtil.addComponent(main_JPanel, __Delim_JTextField,
		1, y, 2, 1, 1, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Delimiter(s) between data columns (default is \" ,\")."),
		3, y, 3, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST );

        JGUIUtil.addComponent(main_JPanel, new JLabel ( "ID:" ), 
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	__ID_JTextField = new JTextField ( "", 20 );
	__ID_JTextField.addKeyListener ( this );
        JGUIUtil.addComponent(main_JPanel, __ID_JTextField,
		1, y, 2, 1, 1, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"IDs to use (default is all or use X* to filter)."),
		3, y, 3, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST );

        JGUIUtil.addComponent(main_JPanel, new JLabel ( "Data source:" ), 
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	__DataSource_JTextField = new JTextField ( "", 20 );
	__DataSource_JTextField.addKeyListener ( this );
        JGUIUtil.addComponent(main_JPanel, __DataSource_JTextField,
		1, y, 2, 1, 1, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"May be optional or required for input type."),
		3, y, 3, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST );

        JGUIUtil.addComponent(main_JPanel, new JLabel ( "Data type:" ), 
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	__DataType_JTextField = new JTextField ( "", 20 );
	__DataType_JTextField.addKeyListener ( this );
        JGUIUtil.addComponent(main_JPanel, __DataType_JTextField,
		1, y, 2, 1, 1, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST );
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"As available for the input type."),
		3, y, 3, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST );

        JGUIUtil.addComponent(main_JPanel, new JLabel ( "Data interval:" ), 
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	__Interval_JTextField = new JTextField ( "", 20 );
	__Interval_JTextField.addKeyListener ( this );
        JGUIUtil.addComponent(main_JPanel, __Interval_JTextField,
		1, y, 2, 1, 1, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"NMinute, NHour, Day, Month, Year, or Irregular."),
		3, y, 3, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST );

        JGUIUtil.addComponent(main_JPanel, new JLabel ( "Scenario:" ), 
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	__Scenario_JTextField = new JTextField ( "", 20 );
	__Scenario_JTextField.addKeyListener ( this );
        JGUIUtil.addComponent(main_JPanel, __Scenario_JTextField,
		1, y, 2, 1, 1, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Optional."),
		3, y, 3, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST );

        JGUIUtil.addComponent(main_JPanel, new JLabel ( "Input type:" ), 
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	__InputType_JTextField = new JTextField ( "", 20 );
	__InputType_JTextField.addKeyListener ( this );
        JGUIUtil.addComponent(main_JPanel, __InputType_JTextField,
		1, y, 2, 1, 1, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Needed to identify format of input."),
		3, y, 3, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST );

        JGUIUtil.addComponent(main_JPanel, new JLabel ( "Input name:" ), 
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	__InputName_JTextField = new JTextField ( "", 20 );
	__InputName_JTextField.addKeyListener ( this );
        JGUIUtil.addComponent(main_JPanel, __InputName_JTextField,
		1, y, 2, 1, 1, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Specify for input types that require a file, etc."),
		3, y, 3, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST );

        JGUIUtil.addComponent(main_JPanel,new JLabel("Handle missing TS how?:"),
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	__HandleMissingTSHow_JComboBox = new SimpleJComboBox ( false );
	__HandleMissingTSHow_JComboBox.addItem ( __DefaultMissingTS );
	__HandleMissingTSHow_JComboBox.addItem ( __IgnoreMissingTS );
	__HandleMissingTSHow_JComboBox.addItemListener ( this );
        JGUIUtil.addComponent(main_JPanel, __HandleMissingTSHow_JComboBox,
		1, y, 1, 1, 1, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Should empty TS be created (requires output period)?"),
		3, y, 3, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST );

        JGUIUtil.addComponent(main_JPanel, new JLabel ( "Default units:" ), 
        		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
        __DefaultUnits_JTextField = new JTextField ( "", 20 );
        __DefaultUnits_JTextField.addKeyListener ( this );
        JGUIUtil.addComponent(main_JPanel, __DefaultUnits_JTextField,
        1, y, 2, 1, 1, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
        "Default units when missing time series are created (default is from InputType)."),
        3, y, 3, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST );
        
        JGUIUtil.addComponent(main_JPanel, new JLabel ( "Command:"),
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	__command_JTextField = new JTextField ( 55 );
	__command_JTextField.setEditable ( false );
	JGUIUtil.addComponent(main_JPanel, __command_JTextField,
		1, y, 6, 1, 1, 0, insetsTLBR, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST );

	// Refresh the contents...
	refresh();

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
		refresh();
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
{	String routine = "createFromList_JDialog.refrsh";
	String ListFile = null;
	String IDCol = null;
	String Delim = null;
	String ID = null;
	String DataSource = null;
	String DataType = null;
	String Interval = null;
	String Scenario = null;
	String InputType = null;
	String InputName = null;
	String HandleMissingTSHow = null;
	String DefaultUnits = null;
	__error_wait = false;
	String command_string = ((String)__command_Vector.elementAt(0)).trim();
	if ( __first_time ) {
		__first_time = false;
		if (	(command_string.length() > 0) &&
			command_string.indexOf('=') < 0 ) {
			// Old syntax...
			Vector v = StringUtil.breakStringList (
				(String)__command_Vector.elementAt(0), "(), \t",
				StringUtil.DELIM_SKIP_BLANKS|
				StringUtil.DELIM_ALLOW_STRINGS );
			if ( (v != null) && (v.size() == 6) ) {
				ListFile = ((String)v.elementAt(1)).trim();
				DataSource = ((String)v.elementAt(2)).trim();
				DataType = ((String)v.elementAt(3)).trim();
				Interval = ((String)v.elementAt(4)).trim();
				HandleMissingTSHow =
				((String)v.elementAt(5)).trim();
			}
		}
		else {	// New syntax...
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
			ListFile = props.getValue ( "ListFile" );
			IDCol = props.getValue ( "IDCol" );
			Delim = props.getValue ( "Delim" );
			ID = props.getValue ( "ID" );
			DataSource = props.getValue ( "DataSource" );
			DataType = props.getValue ( "DataType" );
			Interval = props.getValue ( "Interval" );
			Scenario = props.getValue ( "Scenario" );
			InputType = props.getValue ( "InputType" );
			InputName = props.getValue ( "InputName" );
			HandleMissingTSHow=props.getValue("HandleMissingTSHow");
			DefaultUnits=props.getValue("DefaultUnits");
		}
		// Now select the item in the list.  If not a match,
		// print a warning.
		if ( ListFile != null ) {
			__ListFile_JTextField.setText ( ListFile );
		}
		if ( __IDCol_JComboBox != null ) {
			if ( IDCol == null ) {
				// Select default...
				__IDCol_JComboBox.select ( 0 );
			}
			else {	if (	JGUIUtil.isSimpleJComboBoxItem(
					__IDCol_JComboBox, IDCol,
					JGUIUtil.NONE, null, null ) ){
					__IDCol_JComboBox.select ( IDCol );
				}
				else {	Message.printWarning ( 1, routine,
					"Existing createFromList() " +
					"references an invalid\n"+
					"IDCol \"" + IDCol +
					"\".  Select a\ndifferent value or " +
					"Cancel." );
				}
			}
		}
		if ( ID != null ) {
			__ID_JTextField.setText ( ID );
		}
		if ( DataSource != null ) {
			__DataSource_JTextField.setText ( DataSource );
		}
		if ( DataType != null ) {
			__DataType_JTextField.setText ( DataType );
		}
		if ( Interval != null ) {
			__Interval_JTextField.setText ( Interval );
		}
		if ( Scenario != null ) {
			__Scenario_JTextField.setText ( Scenario );
		}
		if ( InputType != null ) {
			__InputType_JTextField.setText ( InputType );
		}
		if ( InputName != null ) {
			__InputName_JTextField.setText ( InputName );
		}
		if ( __HandleMissingTSHow_JComboBox != null ) {
			if ( HandleMissingTSHow == null ) {
				// Select default...
				__HandleMissingTSHow_JComboBox.select (
				__IgnoreMissingTS );
			}
			else {	if (	JGUIUtil.isSimpleJComboBoxItem(
					__HandleMissingTSHow_JComboBox,
					HandleMissingTSHow, JGUIUtil.NONE, null,
					null ) ) {
					__HandleMissingTSHow_JComboBox.select (
					HandleMissingTSHow );
				}
				else {	Message.printWarning ( 1, routine,
					"Existing createFromList() references" +
					" an invalid\n"+
					"HandleMissingTSHow \"" +
					HandleMissingTSHow +
					"\".  Select a\ndifferent value " +
					"or Cancel." );
				}
			}
		}
		if ( DefaultUnits != null ) {
			__DefaultUnits_JTextField.setText ( ID );
		}
	}
	// Regardless, reset the command from the fields...
	ListFile = __ListFile_JTextField.getText().trim();
	if ( __IDCol_JComboBox != null ) {
		IDCol = __IDCol_JComboBox.getSelected();
	}
	// Do not trim the delimiter!  Otherwise spaces will be lost.
	Delim = __Delim_JTextField.getText();
	ID = __ID_JTextField.getText().trim();
	DataSource = __DataSource_JTextField.getText().trim();
	DataType = __DataType_JTextField.getText().trim();
	Interval = __Interval_JTextField.getText().trim();
	Scenario = __Scenario_JTextField.getText().trim();
	InputType = __InputType_JTextField.getText().trim();
	InputName = __InputName_JTextField.getText().trim();
	if ( __HandleMissingTSHow_JComboBox != null ) {
		HandleMissingTSHow =
		__HandleMissingTSHow_JComboBox.getSelected();
	}
	DefaultUnits = __DefaultUnits_JTextField.getText().trim();
	if ( (ListFile == null) || (ListFile.trim().length() == 0) ) {
		if ( __path_JButton != null ) {
			__path_JButton.setEnabled ( false );
		}
	}
	StringBuffer b = new StringBuffer ();
	if ( ListFile.length() > 0 ) {
		b.append ( "ListFile=\"" + ListFile + "\"" );
	}
	if ( IDCol.length() > 0 ) {
		if ( b.length() > 0 ) {
			b.append ( "," );
		}
		b.append ( "IDCol=" + IDCol );
	}
	if ( Delim.length() > 0 ) {
		if ( b.length() > 0 ) {
			b.append ( "," );
		}
		b.append ( "Delim=\"" + Delim + "\"" );
	}
	if ( ID.length() > 0 ) {
		if ( b.length() > 0 ) {
			b.append ( "," );
		}
		b.append ( "ID=\"" + ID + "\"" );
	}
	if ( DataSource.length() > 0 ) {
		if ( b.length() > 0 ) {
			b.append ( "," );
		}
		b.append ( "DataSource=" + DataSource );
	}
	if ( DataType.length() > 0 ) {
		if ( b.length() > 0 ) {
			b.append ( "," );
		}
		b.append ( "DataType=\"" + DataType + "\"" );
	}
	if ( Interval.length() > 0 ) {
		if ( b.length() > 0 ) {
			b.append ( "," );
		}
		b.append ( "Interval=" + Interval );
	}
	if ( Scenario.length() > 0 ) {
		if ( b.length() > 0 ) {
			b.append ( "," );
		}
		b.append ( "Scenario=\"" + Scenario + "\"" );
	}
	if ( InputType.length() > 0 ) {
		if ( b.length() > 0 ) {
			b.append ( "," );
		}
		b.append ( "InputType=" + InputType );
	}
	if ( InputName.length() > 0 ) {
		if ( b.length() > 0 ) {
			b.append ( "," );
		}
		b.append ( "InputName=\"" + InputName + "\"" );
	}
	if ( HandleMissingTSHow.length() > 0 ) {
		if ( b.length() > 0 ) {
			b.append ( "," );
		}
		b.append ( "HandleMissingTSHow=" + HandleMissingTSHow );
	}
	if ( DefaultUnits.length() > 0 ) {
		if ( b.length() > 0 ) {
			b.append ( "," );
		}
		b.append ( "DefaultUnits=\"" + DefaultUnits + "\"");
	}
	__command_JTextField.setText("createFromList(" + b.toString() + ")" );
	// Check the path and determine what the label on the path button should
	// be...
	if ( __path_JButton != null ) {
		__path_JButton.setEnabled ( true );
		File f = new File ( ListFile );
		if ( f.isAbsolute() ) {
			__path_JButton.setText ( "Remove Working Directory" );
		}
		else {	__path_JButton.setText ( "Add Working Directory" );
		}
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

} // end createFromList_JDialog
