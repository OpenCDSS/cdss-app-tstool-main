// ----------------------------------------------------------------------------
// readStateCU_JDialog - editor for readStateCUB()
// ----------------------------------------------------------------------------
// Copyright:	See the COPYRIGHT file.
// ----------------------------------------------------------------------------
// History: 
//
// 2004-07-11	Steven A. Malers, RTi	Initial version (copy and modify
//					readStateModB_JDialog).
// 2005-06-09	SAM, RTi		Update to read CDS and IPY files.
// 2007-02-26	SAM, RTi		Clean up code based on Eclipse feedback.
// 2007-04-09	SAM, RTi		Add AutoAdjust to help automatically handle
//					new crop names that may not work with TSIDs.
//					Change the command from a text field to text area.
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
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
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

public class readStateCU_JDialog extends JDialog
implements ActionListener, ItemListener, KeyListener, WindowListener
{
	
private final String __False = "False";
private final String __True = "True";

private SimpleJButton	__browse_JButton = null,// File browse button
			__cancel_JButton = null,// Cancel Button
			__ok_JButton = null,	// Ok Button
			__path_JButton = null;	// Convert between relative and
						// absolute paths.
private Vector		__command_Vector = null;// Command as Vector of String
private JTextArea	__command_JTextArea=null;// Command as TextArea
private String		__working_dir = null;	// Working directory.
private JTextField	__InputFile_JTextField = null;// Field for input file. 
private JTextField	__TSID_JTextField = null;// Field for time series
						// identifier
private JTextField	__NewScenario_JTextField = null;// Field for new scenario
private SimpleJComboBox	__AutoAdjust_JComboBox = null;  // For development to
						// deal with non-standard issues in data (e.g., crop
						// names that include "."
private boolean		__error_wait = false;	// Is there an error that we
						// are waiting to be cleared up
						// or Cancel?
private boolean		__first_time = true;

/**
readStateCU_JDialog constructor.
@param parent Frame class instantiating this class.
@param app_PropList Properties from the application.
@param command Time series command to parse.
@param tsids Time series identifiers for available time series.
*/
public readStateCU_JDialog (	JFrame parent, PropList app_PropList,
				Vector command, Vector tsids )
{	super(parent, true);
	initialize ( parent, "Edit readStateCU() Command", app_PropList,
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
		fc.setDialogTitle( "Select StateMod Time Series File");
		// REVISIT - maybe need to list all recognized StateCU file
		// extensions for data sets.
		SimpleFileFilter cds_sff = 
			new SimpleFileFilter("cds",
			"Crop Pattern Time Series (Yearly)");
		fc.addChoosableFileFilter(cds_sff);
		SimpleFileFilter ipy_sff = 
			new SimpleFileFilter("ipy",
			"Irrigation Practice Time Series (Yearly)");
		fc.addChoosableFileFilter(ipy_sff);
		SimpleFileFilter iwr_sff = 
			new SimpleFileFilter("iwr",
			"Irrigation Water Requirement - StateCU report " +
			"(Monthly, Yearly)");
		fc.addChoosableFileFilter(iwr_sff);
		SimpleFileFilter wsl_sff = 
			new SimpleFileFilter("wsl",
			"Water Supply Limited CU - StateCU report " +
			"(Monthly, Yearly)");
		fc.addChoosableFileFilter(wsl_sff);
		fc.setFileFilter ( cds_sff );
		
		if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			String directory = fc.getSelectedFile().getParent();
			String filename = fc.getSelectedFile().getName(); 
			String path = fc.getSelectedFile().getPath(); 
	
			if (filename == null || filename.equals("")) {
				return;
			}
	
			if (path != null) {
				__InputFile_JTextField.setText(path );
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
			__InputFile_JTextField.setText (
			IOUtil.toAbsolutePath(__working_dir,
			__InputFile_JTextField.getText() ) );
		}
		else if ( __path_JButton.getText().equals(
			"Remove Working Directory") ) {
			try {	__InputFile_JTextField.setText (
				IOUtil.toRelativePath ( __working_dir,
				__InputFile_JTextField.getText() ) );
			}
			catch ( Exception e ) {
				Message.printWarning ( 1,
				"readStateCU_JDialog",
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
{	String InputFile = __InputFile_JTextField.getText().trim();
	String NewScenario = __NewScenario_JTextField.getText().trim();
	String warning = "";
	__error_wait = false;
	// Adjust the working directory that was passed in by the specified
	// directory.  If the directory does not exist, warn the user...
	try {	String adjusted_path = IOUtil.adjustPath ( __working_dir,
			InputFile );
		File f = new File ( adjusted_path );
		if ( !f.exists() ) {
			warning += "\nThe file does not exist:\n" +
			"    " + adjusted_path + "\n" +
		  	"Correct or Cancel.";
		}
		f = null;
	}
	catch ( Exception e ) {
		warning += "\nThe working directory:\n" +
		"    \"" + __working_dir + "\"\ncannot be adjusted using:\n" +
		"    \"" + InputFile + "\".\n" +
		"Correct the file or Cancel.";
	}
	if ( NewScenario.indexOf(" ") > 0 ) {
		warning += "\nThe NewScenario cannot contain a space.";
	}
	if ( warning.length() > 0 ) {
		Message.printWarning ( 1, "readStateCU_JDialog", warning );
		__error_wait = true;
	}
}

/**
Free memory for garbage collection.
*/
protected void finalize ()
throws Throwable
{	__browse_JButton = null;
	__cancel_JButton = null;
	__command_JTextArea = null;
	__InputFile_JTextField = null;
	__TSID_JTextField = null;
	__NewScenario_JTextField = null;
	__AutoAdjust_JComboBox = null;
	__command_Vector = null;
	__ok_JButton = null;
	__path_JButton = null;
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
@param parent Frame class instantiating this class.
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

	// Main panel...

	JPanel main_JPanel = new JPanel();
	main_JPanel.setLayout( new GridBagLayout() );
	getContentPane().add ( "North", main_JPanel );
	int y = 0;

        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Read 1+ (or all) time series from one of the following " +
		"file types, using data in the file to assign the identifier:"),
		0, y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"     Crop pattern time series file (StateCU input file)." ),
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"     Irrigation practice time series file (input)." ),
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"     StateCU IWR or WSL report file (output)." ),
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
		"The time series identifier pattern, if specified, will" +
		" filter the read ONLY for IWR and WSL files;" ),
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
    JGUIUtil.addComponent(main_JPanel, new JLabel (
		"specify blank to read all or use * wildcards to match a" +
		" time series identifier." ),
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
    JGUIUtil.addComponent(main_JPanel, new JLabel (
		"For example, to read all monthly IWR time series for" +
		" locations starting with ABC, specify:" ),
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
    JGUIUtil.addComponent(main_JPanel, new JLabel ( "  ABC.*.IWR.Month"),
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
    JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Location can be X, X*, or *.  Data type and interval can " +
		"be * or combinations as follows:"),
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
    JGUIUtil.addComponent(main_JPanel, new JLabel (
		"   CropArea-AllCrops        Year" ),
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
    JGUIUtil.addComponent(main_JPanel, new JLabel (
		"   IWR or WSL               Month, Year" ),
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
    JGUIUtil.addComponent(main_JPanel, new JLabel (
		"   IWR_Depth or WSL_Depth   Month, Year" ),
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);

    JGUIUtil.addComponent(main_JPanel, new JLabel (
		"StateCU file to read:" ),
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	__InputFile_JTextField = new JTextField ( 50 );
	__InputFile_JTextField.addKeyListener ( this );
    JGUIUtil.addComponent(main_JPanel, __InputFile_JTextField,
		1, y, 5, 1, 1, 0, insetsTLBR, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
	__browse_JButton = new SimpleJButton ( "Browse", this );
    JGUIUtil.addComponent(main_JPanel, __browse_JButton,
		6, y, 1, 1, 1, 0, insetsTLBR, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER);

    JGUIUtil.addComponent(main_JPanel, new JLabel ( "Time series ID:" ), 
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	__TSID_JTextField = new JTextField ( 10 );
	__TSID_JTextField.addKeyListener ( this );
    JGUIUtil.addComponent(main_JPanel, __TSID_JTextField,
		1, y, 1, 1, 1, 0, insetsTLBR, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
    JGUIUtil.addComponent(main_JPanel, new JLabel (
	"Loc.Source.Type.Interval"),
	3, y, 4, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);

    JGUIUtil.addComponent(main_JPanel, new JLabel ( "New scenario:" ), 
    	0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
    __NewScenario_JTextField = new JTextField ( 10 );
    __NewScenario_JTextField.addKeyListener ( this );
    JGUIUtil.addComponent(main_JPanel, __NewScenario_JTextField,
     	1, y, 1, 1, 1, 0, insetsTLBR, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
    JGUIUtil.addComponent(main_JPanel, new JLabel (
       	"New scenario to assign (useful when comparing)."),
       	3, y, 4, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);


    JGUIUtil.addComponent(main_JPanel, new JLabel ( "Automatically adjust:" ), 
   		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
   	__AutoAdjust_JComboBox = new SimpleJComboBox ( false );
   	__AutoAdjust_JComboBox.addItem ( "" );
   	__AutoAdjust_JComboBox.addItem ( __False );
   	__AutoAdjust_JComboBox.addItem ( __True );
   	__AutoAdjust_JComboBox.addItemListener ( this );
    JGUIUtil.addComponent(main_JPanel, __AutoAdjust_JComboBox,
   		1, y, 2, 1, 1, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
   	JGUIUtil.addComponent(main_JPanel, new JLabel (
       	"Convert data type with \".\" to \"-\"."),
       	3, y, 4, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
       	
    JGUIUtil.addComponent(main_JPanel, new JLabel ( "Command:"),
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
    __command_JTextArea = new JTextArea ( 5, 55 );
    __command_JTextArea.setLineWrap ( true );
    __command_JTextArea.setWrapStyleWord ( true );
    __command_JTextArea.setEditable ( false );
	JGUIUtil.addComponent(main_JPanel, new JScrollPane(__command_JTextArea),
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
					"Remove Working Directory",this);
		button_JPanel.add ( __path_JButton );
	}
	__cancel_JButton = new SimpleJButton("Cancel", this);
	button_JPanel.add ( __cancel_JButton );
	__ok_JButton = new SimpleJButton("OK", this);
	button_JPanel.add ( __ok_JButton );

	if ( title != null ) {
		setTitle ( title );
	}
	// Dialogs do not need to be resizable...
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
{	Object o = e.getItemSelectable();

	if (	(o == __AutoAdjust_JComboBox) &&
		(e.getStateChange() == ItemEvent.SELECTED) ) {
		refresh();
	}
}

/**
Respond to KeyEvents.
*/
public void keyPressed ( KeyEvent event )
{	int code = event.getKeyCode();

	if ( code == KeyEvent.VK_ENTER ) {
		refresh ();
		checkInput ();
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
Refresh the command from the other text field contents.
*/
private void refresh ()
{	String routine = "readStateCU_JDialog.refresh";
	__error_wait = false;
	String InputFile="";
	String TSID="";
	String NewScenario="";
	String AutoAdjust = "";
	if ( __first_time ) {
		__first_time = false;
		// Parse the incoming string and fill the fields...
		Vector v = StringUtil.breakStringList (
			((String)__command_Vector.elementAt(0)).trim(),"()",
			StringUtil.DELIM_SKIP_BLANKS );
		PropList props = null;
		if ( (v != null) && (v.size() > 1) ) {
			props = PropList.parse (
				(String)v.elementAt(1), routine, "," );
		}
		else {	props = new PropList ( routine );
		}
		InputFile = props.getValue ( "InputFile" );
		TSID = props.getValue ( "TSID" );
		NewScenario = props.getValue ( "NewScenario" );
		AutoAdjust = props.getValue ( "AutoAdjust" );
		if ( InputFile != null ) {
			__InputFile_JTextField.setText (InputFile);
		}
		if ( TSID != null ) {
			__TSID_JTextField.setText (TSID);
		}
		if ( NewScenario != null ) {
			__NewScenario_JTextField.setText (NewScenario);
		}
		if (	JGUIUtil.isSimpleJComboBoxItem(
				__AutoAdjust_JComboBox,
				AutoAdjust, JGUIUtil.NONE, null, null ) ) {
				__AutoAdjust_JComboBox.select ( AutoAdjust);
		}
		else {	if (	(AutoAdjust == null) ||
					AutoAdjust.equals("") ) {
					// New command...select the default...
					__AutoAdjust_JComboBox.select ( 0 );
				}
				else {	Message.printWarning ( 1,
						routine,
						"Existing readStateCU() references an " +
						"invalid\n"+ "AutoAdjust parameter \"" +
						AutoAdjust + "\".  Correct or Cancel." );
				}
		}
	}
	// Regardless, reset the command from the fields...
	InputFile = __InputFile_JTextField.getText().trim();
	TSID = __TSID_JTextField.getText().trim();
	NewScenario = __NewScenario_JTextField.getText().trim();
	AutoAdjust = __AutoAdjust_JComboBox.getSelected();
	StringBuffer b = new StringBuffer ();
	if ( InputFile.length() > 0 ) {
		b.append ( "InputFile=\"" + InputFile + "\"" );
	}
	if ( TSID.length() > 0 ) {
		if ( b.length() > 0 ) {
			b.append ( "," );
		}
		b.append ( "TSID=\"" + TSID + "\"" );
	}
	if ( NewScenario.length() > 0 ) {
		if ( b.length() > 0 ) {
			b.append ( "," );
		}
		b.append ( "NewScenario=\"" + NewScenario + "\"" );
	}
	if ( AutoAdjust.length() > 0 ) {
		if ( b.length() > 0 ) {
			b.append ( "," );
		}
		b.append ( "AutoAdjust=\"" + AutoAdjust + "\"" );
	}
	__command_JTextArea.setText("readStateCU(" + b.toString() + ")" );
	__command_Vector.removeAllElements();
	__command_Vector.addElement ( __command_JTextArea.getText() );
	// Check the path and determine what the label on the path button should
	// be...
	if ( __path_JButton != null ) {
		__path_JButton.setEnabled ( true );
		File f = new File ( InputFile );
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

} // end readStateCU_JDialog
