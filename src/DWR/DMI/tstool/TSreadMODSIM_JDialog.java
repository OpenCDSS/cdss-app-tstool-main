// ----------------------------------------------------------------------------
// TSreadMODSIM_JDialog - editor for TS x = readMODSIM()
// ----------------------------------------------------------------------------
// Copyright:	See the COPYRIGHT file.
// ----------------------------------------------------------------------------
// History: 
//
// 2002-06-13	Steven A. Malers, RTi	Initial version (copy and modify
//					TSreadDateValue_Dialog).
// 2002-06-14	SAM, RTi		Update to allow specifying a node
//					name and data type.
// 2003-12-07	SAM, RTi		Update to Swing.
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

import rti.tscommandprocessor.core.TSCommandProcessor;
import rti.tscommandprocessor.core.TSCommandProcessorUtil;

import java.io.File;
import java.util.Vector;

import RTi.TS.ModsimTS;
import RTi.TS.TSIdent;

import RTi.Util.GUI.JFileChooserFactory;
import RTi.Util.GUI.JGUIUtil;
import RTi.Util.GUI.SimpleFileFilter;
import RTi.Util.GUI.SimpleJButton;
import RTi.Util.GUI.SimpleJComboBox;
import RTi.Util.IO.Command;
import RTi.Util.IO.IOUtil;
import RTi.Util.IO.PropList;
import RTi.Util.Message.Message;
import RTi.Util.String.StringUtil;
import RTi.Util.Time.DateTime;

/**
The TSreadMODSIM_JDialog edits the TS x = readMODSIM() command.
If the "WorkingDir" application property is defined, then the dialog will allow
the input file path to be truncated to a relative path.
*/
public class TSreadMODSIM_JDialog extends JDialog
implements ActionListener, ItemListener, KeyListener, WindowListener
{
private SimpleJButton	__browse_JButton = null,// File browse button
			__path_JButton = null,	// Convert between relative and
						// absolute path.
			__cancel_JButton = null,// Cancel Button
			__ok_JButton = null;	// Ok Button
private Vector		__command_Vector = null;// Command as Vector of String
private Command __Command = null;   // FIXME SAM 2007-12-01 change to __command when Command class is implemented
private String		__working_dir = null;	// Working directory.
private JTextField	__alias_JTextField = null,// Alias for time series.
			__nodename_JTextField,	// Node name to read.
			__datatype_JTextField,	// Node data type to read.
			__analysis_period_start_JTextField,
			__analysis_period_end_JTextField,
						// Text fields for analysis
						// period.
			__command_JTextField = null,
						// Command as JTextField
			__file_JTextField = null, // Field for time series
						// identifier
			__units_JTextField = null;// Units to convert to at read
private SimpleJComboBox	__datatype_JComboBox;	// Node data type choice.
private boolean		__error_wait = false;	// Is there an error that we
						// are waiting to be cleared up
						// or Cancel?
private boolean		__first_time = true;

/**
TSreadMODSIM_JDialog constructor.
@param parent JFrame class instantiating this class.
@param app_PropList Properties from the application.
@param command Command to parse.
@param tsids Time series identifiers for available time series - ignored.
*/
public TSreadMODSIM_JDialog (	JFrame parent, PropList app_PropList,
				Vector command, Vector tsids, Command command_class )
{	super(parent, true);
	initialize ( parent, "Edit TS Alias = ReadMODSIM() Command",
		app_PropList, command, tsids, command_class );
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
		fc.setDialogTitle( "Select MODSIM Output Time Series File");
		SimpleFileFilter
		sff = new SimpleFileFilter("FLO",
			"MODSIM flow link output");
		fc.addChoosableFileFilter(sff);
		sff = new SimpleFileFilter("RES",
			"MODSIM reservoir output");
		fc.addChoosableFileFilter(sff);
		sff = new SimpleFileFilter("DEM",
			"MODSIM demand output");
		fc.addChoosableFileFilter(sff);
		sff = new SimpleFileFilter("GW",
			"MODSIM groundwater output");
		fc.addChoosableFileFilter(sff);
		
		if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			String filename = fc.getSelectedFile().getName(); 
			String path = fc.getSelectedFile().getPath(); 
	
			if (filename == null || filename.equals("")) {
				return;
			}
	
			if (path != null) {
				__file_JTextField.setText(path );
				JGUIUtil.setLastFileDialogDirectory(
					last_directory_selected );
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
				"readMODSIM_JDialog",
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
	String nodename = __nodename_JTextField.getText().trim();
	String routine = "TSreadMODSIM_JDialog.checkInput";
	String datatype = __datatype_JTextField.getText().trim();
	String analysis_period_start = __analysis_period_start_JTextField.getText().trim();
	String analysis_period_end = __analysis_period_end_JTextField.getText().trim();
	String warning = "";
	// Adjust the working directory that was passed in by the specified
	// directory.  If the directory does not exist, warn the user...
	try {	String adjusted_path = IOUtil.verifyPathForOS(IOUtil.adjustPath ( __working_dir, file));
		File f = new File ( adjusted_path );
		if ( !f.exists() ) {
			warning +=
			"The MODSIM file does not exist:\n" +
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
	if ( alias.equals("") ) {
		warning +=
			"\nAlias must be set, or press Cancel";
	}
	if ( nodename.equals("") ) {
		warning +=
			"Node/Link name must be set, or press Cancel";
	}
	if ( datatype.equals("") ) {
		warning +=
			"Data type must be set, or press Cancel";
	}
	DateTime startdate = null, enddate = null;
	if (	!analysis_period_start.equals("*") &&
		!analysis_period_start.equalsIgnoreCase("OutputStart") &&
		!analysis_period_start.equalsIgnoreCase("OutputEnd") &&
		!analysis_period_start.equalsIgnoreCase("QueryStart") &&
		!analysis_period_start.equalsIgnoreCase("QueryEnd") ) {
		try {	startdate = DateTime.parse(
				analysis_period_start );
		}
		catch ( Exception e ) {
			warning +=
				"\nStart date \"" + analysis_period_start +
				"\" is not a valid date.\n"+
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
				"\nEnd date \"" + analysis_period_end +
				"\" is not a valid date.\n"+
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
			Vector command, Vector tsids, Command command_class )
{	__command_Vector = command;
    __Command = command_class;
    __working_dir = TSCommandProcessorUtil.getWorkingDirForCommand ( (TSCommandProcessor)__Command.getCommandProcessor(), __Command );

	addWindowListener( this );

        Insets insetsTLBR = new Insets(2,2,2,2);

	JPanel main_JPanel = new JPanel();
	main_JPanel.setLayout( new GridBagLayout() );
	getContentPane().add ( "North", main_JPanel );
	int y = 0;

        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Read a single time series from a MODSIM format file."),
		0, y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
       	JGUIUtil.addComponent(main_JPanel, new JLabel (
		"The node/link name must be consistent with information in the"+
		" MODSIM file."),
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
       	JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Data types are determined from the file extension" +
		" but others can be specified."),
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Specify a full path or relative path (relative to working " +
		"directory) for a MODSIM file to read." ), 
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
       	JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Specifying the period will limit data that are available " +
		"for fill commands but can increase performance." ), 
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
       	JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Specifying units causes conversion during the read " +
		"(currently under development)."),
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
       	JGUIUtil.addComponent(main_JPanel, new JLabel (
		"If not specified, the period defaults to the query period."),
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);

	if ( __working_dir != null ) {
        	JGUIUtil.addComponent(main_JPanel, new JLabel (
		"The working directory is: " + __working_dir ), 
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
	}

        JGUIUtil.addComponent(main_JPanel, new JLabel ( "Time Series Alias:"),
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	__alias_JTextField = new JTextField ( 30 );
	__alias_JTextField.addKeyListener ( this );
	JGUIUtil.addComponent(main_JPanel, __alias_JTextField,
		1, y, 3, 1, 1, 0, insetsTLBR, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"MODSIM File to Read:" ), 
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	__file_JTextField = new JTextField ( 50 );
	__file_JTextField.addKeyListener ( this );
        JGUIUtil.addComponent(main_JPanel, __file_JTextField,
		1, y, 5, 1, 1, 0, insetsTLBR, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
	__browse_JButton = new SimpleJButton ( "Browse", "Browse", this );
        JGUIUtil.addComponent(main_JPanel, __browse_JButton,
		6, y, 1, 1, 1, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.CENTER);

        JGUIUtil.addComponent(main_JPanel,new JLabel("Node/Link Name to Read:"),
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	__nodename_JTextField = new JTextField ( "", 30 );
	__nodename_JTextField.addKeyListener ( this );
	JGUIUtil.addComponent(main_JPanel, __nodename_JTextField,
		1, y, 3, 1, 1, 0, insetsTLBR, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel("Data Type to Read:"),
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	__datatype_JTextField = new JTextField ( "", 30 );
	__datatype_JTextField.addKeyListener ( this );
	JGUIUtil.addComponent(main_JPanel, __datatype_JTextField,
		1, y, 3, 1, 1, 0, insetsTLBR, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
	__datatype_JComboBox = new SimpleJComboBox ( false );
	__datatype_JComboBox.addItem ( "Unavailable" );
	__datatype_JComboBox.addItemListener ( this );
	JGUIUtil.addComponent(main_JPanel, __datatype_JComboBox,
		4, y, 2, 1, 1, 0, insetsTLBR, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel("Units to convert to:"),
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	__units_JTextField = new JTextField ( "*", 10 );
	__units_JTextField.addKeyListener ( this );
	__units_JTextField.setEnabled ( false );
	JGUIUtil.addComponent(main_JPanel, __units_JTextField,
		1, y, 3, 1, 1, 0, insetsTLBR, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);

	JGUIUtil.addComponent(main_JPanel, new JLabel ( "Period to Read:" ), 
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	__analysis_period_start_JTextField = new JTextField ( "*", 15 );
	__analysis_period_start_JTextField.addKeyListener ( this );
	JGUIUtil.addComponent(main_JPanel, __analysis_period_start_JTextField,
		1, y, 2, 1, 1, 0, insetsTLBR, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
	JGUIUtil.addComponent(main_JPanel, new JLabel ( "to" ), 
		3, y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.CENTER);
	__analysis_period_end_JTextField = new JTextField ( "*", 15 );
	__analysis_period_end_JTextField.addKeyListener ( this );
	JGUIUtil.addComponent(main_JPanel, __analysis_period_end_JTextField,
		4, y, 2, 1, 1, 0, insetsTLBR, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel ( "Command:"),
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	__command_JTextField = new JTextField ( 60 );
	__command_JTextField.setEditable ( false );
	JGUIUtil.addComponent(main_JPanel, __command_JTextField,
		1, y, 6, 1, 1, 0, insetsTLBR, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);

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
{	// Only one choice...
	__datatype_JTextField.setText(__datatype_JComboBox.getSelected());
}

/**
Respond to KeyEvents.
*/
public void keyPressed ( KeyEvent event )
{	int code = event.getKeyCode();

	// Don't want key input to refresh dates unless enter or tab...
	if (	(event.getSource() == __analysis_period_start_JTextField) ||
		(event.getSource() == __analysis_period_end_JTextField) ) {
		if ( (code == KeyEvent.VK_ENTER) || (code == KeyEvent.VK_TAB) ) {
			refresh ();
		}
	}
	else if ((event.getSource() == __file_JTextField) &&
		((code == KeyEvent.VK_ENTER) || (code == KeyEvent.VK_TAB)) ) {
		updateDataTypeChoices();
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
	String nodename = "";
	String datatype = "";
	__error_wait = false;
	String analysis_period_start = "*";
	String analysis_period_end = "*";
	if ( __first_time ) {
		__first_time = false;
		if ( ((String)__command_Vector.elementAt(0)).indexOf('=')>=0){
			// Parse an existing command
			// Assume TS X =readMODSIM(file,TSID,units,start,end)
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
					updateDataTypeChoices ();
				}
				if ( (v != null) && (v.size() >= 3) ) {
					// Third field is the TSID to read.
					// Currently ignored.
					TSIdent tsident = null;
					try {	tsident =new TSIdent(((String)v.
							elementAt(2)).trim());
						__nodename_JTextField.setText(
						tsident.getLocation() );
						datatype = tsident.getType();
					}
					catch ( Exception e ) {
					}
					__datatype_JTextField.setText(datatype);
					if (	JGUIUtil.isSimpleJComboBoxItem(
						__datatype_JComboBox, alias,
						JGUIUtil.NONE, null, null ) ) {
						// Not a requirement that they
						// match...
						__datatype_JComboBox.select (
						datatype );
					}
				}
				if ( (v != null) && (v.size() >= 4) ) {
					// Fourth field is the requested units.
					// Currently ignored.
				}
				if ( (v != null) && (v.size() >= 5) ) {
					// Fifth field is file...
					analysis_period_start =
					((String)v.elementAt(4)).trim();
					__analysis_period_start_JTextField.
					setText ( analysis_period_start );
				}
				if ( (v != null) && (v.size() >= 6) ) {
					analysis_period_end =
					((String)v.elementAt(5)).trim();
					__analysis_period_end_JTextField.
					setText ( analysis_period_end );
				}
			}
		}
	}
	// Regardless, reset the command from the fields...
	alias = __alias_JTextField.getText().trim();
	file = __file_JTextField.getText().trim();
	nodename = __nodename_JTextField.getText().trim();
	datatype = __datatype_JTextField.getText().trim();
	analysis_period_start=
		__analysis_period_start_JTextField.getText().trim();
	analysis_period_end = __analysis_period_end_JTextField.getText().trim();
	if ( alias.equals("") ) {
		__command_JTextField.setText("");
	}
	else {	__command_JTextField.setText("TS " + alias +
		" = ReadMODSIM(\"" + file + "\",\"" + nodename + ".." +
		datatype + "..\",*," +
		analysis_period_start + "," + analysis_period_end +")"); 
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
Update the data type choices based on the file extension.
*/
private void updateDataTypeChoices()
{	__datatype_JComboBox.removeAll();
	String [] available_datatypes = ModsimTS.getAvailableDataTypes(
		IOUtil.getPathUsingWorkingDir(
		__file_JTextField.getText().trim()), false );
	if ( available_datatypes == null ) {
		__datatype_JComboBox.add ( "Unavailable" );
	}
	else {	__datatype_JComboBox.setData (
			StringUtil.toVector(available_datatypes) );
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

} // end TSreadMODSIM_JDialog
