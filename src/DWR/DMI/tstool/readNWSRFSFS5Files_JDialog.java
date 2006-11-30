// ----------------------------------------------------------------------------
// readNWSRFSFS5Files_JDialog - editor for TS x = readNWSRFSFS5Files() and
//					readNWSRFSFS5Files().
// ----------------------------------------------------------------------------
// Copyright:	See the COPYRIGHT file.
// ----------------------------------------------------------------------------
// History: 
//
// 2004-09-11	Steven A. Malers, RTi	Initial version (copy and modify
//					readHydroBase_JDialog).
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
import java.io.File;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import RTi.DMI.NWSRFS_DMI.NWSRFS_DMI;
import RTi.DMI.NWSRFS_DMI.NWSRFS_TS_InputFilter_JPanel;

import RTi.TS.TSIdent;

import RTi.Util.GUI.InputFilter;
import RTi.Util.GUI.InputFilter_JPanel;
import RTi.Util.GUI.JGUIUtil;
import RTi.Util.GUI.SimpleFileFilter;
import RTi.Util.GUI.SimpleJButton;
import RTi.Util.IO.IOUtil;
import RTi.Util.IO.PropList;
import RTi.Util.Message.Message;
import RTi.Util.String.StringUtil;
import RTi.Util.Time.DateTime;
import RTi.Util.Time.TimeInterval;

/**
The readNWSRFSFS5Files_JDialog edits the TS x = readNWSRFSFS5Files() and
readNWSRFSFS5Files commands.
*/
public class readNWSRFSFS5Files_JDialog extends JDialog
implements ActionListener, ItemListener, KeyListener, WindowListener
{
private SimpleJButton	__cancel_JButton = null,// Cancel Button
			__ok_JButton = null;	// Ok Button
private JFrame		__parent_JFrame = null;	// parent JFrame GUI class
private Vector		__command_Vector = null;// Command as Vector of String
private JTextField	__Alias_JTextField = null,// Alias for time series.
			__location_JTextField,	// Location part of TSID
			__datasource_JTextField,// Data source part of TSID
			__DataType_JTextField,	// Data type part of TSID
			__Interval_JTextField,	// Interval part of TSID
			__TSID_JTextField,	// Full TSID
			__QueryStart_JTextField,
			__QueryEnd_JTextField,	// Text fields for query
						// period.
			__Units_JTextField,	// Units to return
			__command_JTextField = null;
private Vector __input_filter_JPanel_Vector = new Vector();
						// Command as TextField
private InputFilter_JPanel __input_filter_NWSRFS_FS5Files_JPanel = null;
						// InputFilter_JPanel for
						// NWSRFS FS5Files time series.
private NWSRFS_DMI	__nwsrfs_dmi = null;	// NWSRFS DMI to do queries.
private boolean		__error_wait = false;	// Is there an error that we
						// are waiting to be cleared up
						// or Cancel?
private boolean		__first_time = true;
private boolean		__read_one = true;	// Indicates if one time series
						// is being read (TX X =...) or
						// multiple time series.

/**
readNWSRFSFS5Files_JDialog constructor.
@param parent JFrame class instantiating this class.
@param app_PropList Properties from the application.
@param command Time series command to parse.
@param read_one If true, then the TX X = command is being processed.  Otherwise,
1+ time series can be read.
@param nwsrfs_dmi NWSRFS_DMI to do queries.
*/
public readNWSRFSFS5Files_JDialog(	JFrame parent, PropList app_PropList,
					Vector command, boolean read_one,
					NWSRFS_DMI nwsrfs_dmi )
{	super(parent, true);
	initialize ( parent, app_PropList, command, read_one, nwsrfs_dmi );
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
		checkInput ();
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
{	String routine = "readNWSRFSFS5Files_JDialog.checkInput";
	String DataType = __DataType_JTextField.getText().trim();
	String Interval = __Interval_JTextField.getText().trim();
	String QueryStart = __QueryStart_JTextField.getText().trim();
	String QueryEnd = __QueryEnd_JTextField.getText().trim();
	String warning = "";
	if ( __read_one ) {
		String Alias = __Alias_JTextField.getText().trim();
		if ( Alias.equals("") ) {
			warning += "\nAlias must be set, or press Cancel";
		}
		String tsid_location = __location_JTextField.getText().trim();
		if ( tsid_location.length() == 0 ) {
			warning += "\nThe location must be specified.";
		}
		String DataSource = __datasource_JTextField.getText().trim();
		if ( DataSource.length() == 0 ) {
			warning += "\nThe data source must be specified.";
		}
	}
	if ( DataType.length() == 0 ) {
		warning += "\nThe data type must be specified.";
	}
	if ( Interval.length() == 0 ) {
		warning += "\nThe data interval must be specified.";
	}
	else {	try { TimeInterval.parseInterval (Interval);
		}
		catch ( Exception e ) {
			warning += "\nThe data interval \"" +
				Interval + "\" is invalid";
		}
	}
	DateTime startdate = null, enddate = null;
	if (	!QueryStart.equals("") &&
		!QueryStart.equalsIgnoreCase("OutputStart") &&
		!QueryStart.equalsIgnoreCase("OutputEnd") &&
		!QueryStart.equalsIgnoreCase("QueryStart") &&
		!QueryStart.equalsIgnoreCase("QueryEnd") ) {
		try {	startdate = DateTime.parse(
				QueryStart);
		}
		catch ( Exception e ) {
			warning +=
				"\nQuery start date/time \"" + QueryStart +
				"\" is not a valid date/time.\n"+
				"Specify a date or press Cancel.";
		}
	}
	if (	!QueryEnd.equals("") &&
		!QueryEnd.equalsIgnoreCase("OutputStart") &&
		!QueryEnd.equalsIgnoreCase("OutputEnd") &&
		!QueryEnd.equalsIgnoreCase("QueryStart") &&
		!QueryEnd.equalsIgnoreCase("QueryEnd") ) {
		try {	enddate = DateTime.parse(QueryEnd);
		}
		catch ( Exception e ) {
			warning +=
				"\nQuery end date/time \"" + QueryEnd +
				"\" is not a valid date/time.\n"+
				"Specify a date, or press Cancel.";
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
{	__Alias_JTextField = null;
	__cancel_JButton = null;
	__command_JTextField = null;
	__command_Vector = null;
	__ok_JButton = null;
	__parent_JFrame = null;
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
@param app_PropList Properties from application.
@param command Vector of String containing the time series command, which
should have a time series identifier and optionally comments.
@param read_one If true, then the TX X = command is being processed.  Otherwise,
1+ time series can be read.
@param nwsrfs_dmi NWSRFS_DMI to do queries.
*/
private void initialize ( JFrame parent, PropList app_PropList,
			Vector command, boolean read_one, NWSRFS_DMI nwsrfs_dmi)
{	String routine = "readNWSRFSFS5Files_JDialog.initialize";
	__parent_JFrame = parent;
	__command_Vector = command;
	__read_one = read_one;
	__nwsrfs_dmi = nwsrfs_dmi;
	String title = "";

	if ( __read_one ) {
		title = "Edit TS x = readNWSRFSFS5Files() Command";
	}
	else {	title = "Edit readNWSRFSFS5Files() Command";
	}

	try {	// REVISIT SAM 2004-09-11 put in until dialog is fully enabled
	addWindowListener( this );

        Insets insetsTLBR = new Insets(2,2,2,2);

	JPanel main_JPanel = new JPanel();
	main_JPanel.setLayout( new GridBagLayout() );
	GridBagConstraints gbc = new GridBagConstraints();
	getContentPane().add ( "North", main_JPanel );
	int y = 0;

	if ( __read_one ) {
        	JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Read a single time series from the NWSRFS FS5Files."),
		0, y, 7, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.WEST);
	}
	else {	JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Read one or more time series from the NWSRFS FS5Files."),
		0, y, 7, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.WEST);
       		JGUIUtil.addComponent(main_JPanel, new JLabel (
		"The data type and interval must be selected.  Constrain the " +
		"query using the \"where\" clauses, if necessary." ), 
		0, ++y, 7, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.WEST);
       		JGUIUtil.addComponent(main_JPanel, new JLabel (
		"This command is fully enabled only for structure time " +
		"series."),
		0, ++y, 7, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.WEST);
	}
       	JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Refer to the NWSRFS FS5Files Input Type documentation for " +
		"possible values." ), 
		0, ++y, 7, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.WEST);
       	JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Specifying the period will limit data that are available " +
		"for fill commands but can increase performance." ), 
		0, ++y, 7, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.WEST);
       	JGUIUtil.addComponent(main_JPanel, new JLabel (
		"If not specified, the period defaults to the query period."),
		0, ++y, 7, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.WEST);

	if ( __read_one ) {
        	JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Time series alias:"),
		0, ++y, 1, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.EAST);
		__Alias_JTextField = new JTextField ( 30 );
		__Alias_JTextField.addKeyListener ( this );
		JGUIUtil.addComponent(main_JPanel, __Alias_JTextField,
		1, y, 3, 1, 1, 0, insetsTLBR, gbc.HORIZONTAL, gbc.WEST);

        	JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Location:"),
		0, ++y, 1, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.EAST);
		__location_JTextField = new JTextField ( "" );
		__location_JTextField.addKeyListener ( this );
        	JGUIUtil.addComponent(main_JPanel, __location_JTextField,
		1, y, 2, 1, 1, 0, insetsTLBR, gbc.HORIZONTAL, gbc.WEST);
        	JGUIUtil.addComponent(main_JPanel, new JLabel (
		"For example: station or area ID."),
		3, y, 2, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.WEST);

        	JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Data source:"),
		0, ++y, 1, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.EAST);
		__datasource_JTextField = new JTextField ( "NWSRFS" );
		__datasource_JTextField.setEditable ( false );
        	JGUIUtil.addComponent(main_JPanel, __datasource_JTextField,
		1, y, 2, 1, 1, 0, insetsTLBR, gbc.HORIZONTAL, gbc.WEST);
	}

        JGUIUtil.addComponent(main_JPanel, new JLabel ( "Data type:"),
		0, ++y, 1, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.EAST);
	__DataType_JTextField = new JTextField ( "" );
	__DataType_JTextField.addKeyListener ( this );
        JGUIUtil.addComponent(main_JPanel, __DataType_JTextField,
		1, y, 2, 1, 1, 0, insetsTLBR, gbc.HORIZONTAL, gbc.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"For example: MAP, QIN."),
		3, y, 2, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel ("Data interval:"),
		0, ++y, 1, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.EAST);
	__Interval_JTextField = new JTextField ( "" );
	__Interval_JTextField.addKeyListener ( this );
        JGUIUtil.addComponent(main_JPanel, __Interval_JTextField,
		1, y, 2, 1, 1, 0, insetsTLBR, gbc.HORIZONTAL, gbc.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"For example: 6Hour, 24Hour."),
		3, y, 2, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.WEST);

	if ( !__read_one ) {
		int buffer = 3;
		Insets insets = new Insets(0,buffer,0,0);

		try {	// NWSRFS FS5Files time series...

			PropList filter_props = new PropList ( "" );
			filter_props.set ( "NumFilterGroups=6" );
			__input_filter_NWSRFS_FS5Files_JPanel = new
				NWSRFS_TS_InputFilter_JPanel ();
       			JGUIUtil.addComponent(main_JPanel,
				__input_filter_NWSRFS_FS5Files_JPanel,
				0, ++y, 7, 1, 0.0, 0.0, insets, gbc.HORIZONTAL,
				gbc.WEST );
			__input_filter_JPanel_Vector.addElement (
				__input_filter_NWSRFS_FS5Files_JPanel);
			__input_filter_NWSRFS_FS5Files_JPanel.
				addEventListeners ( this );
		}
		catch ( Exception e ) {
			Message.printWarning ( 2, routine,
			"Unable to initialize input filter for NWSRFS " +
			" FS5Files." );
			Message.printWarning ( 2, routine, e );
		}
	}

	if ( __read_one ) {
        	JGUIUtil.addComponent(main_JPanel, new JLabel ( "TSID (full):"),
		0, ++y, 1, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.EAST);
		__TSID_JTextField = new JTextField ( "" );
		__TSID_JTextField.setEditable ( false );
		__TSID_JTextField.addKeyListener ( this );
        	JGUIUtil.addComponent(main_JPanel, __TSID_JTextField,
		1, y, 6, 1, 1, 0, insetsTLBR, gbc.HORIZONTAL, gbc.WEST);
	}

	JGUIUtil.addComponent(main_JPanel, new JLabel ( "Period to read:" ), 
		0, ++y, 1, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.EAST);
	__QueryStart_JTextField = new JTextField ( 15 );
	__QueryStart_JTextField.addKeyListener ( this );
	JGUIUtil.addComponent(main_JPanel, __QueryStart_JTextField,
		1, y, 2, 1, 1, 0, insetsTLBR, gbc.HORIZONTAL, gbc.WEST);
	JGUIUtil.addComponent(main_JPanel, new JLabel ( "to" ), 
		3, y, 1, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.CENTER);
	__QueryEnd_JTextField = new JTextField ( 15 );
	__QueryEnd_JTextField.addKeyListener ( this );
	JGUIUtil.addComponent(main_JPanel, __QueryEnd_JTextField,
		4, y, 2, 1, 1, 0, insetsTLBR, gbc.HORIZONTAL, gbc.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel ("Units:"),
		0, ++y, 1, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.EAST);
	__Units_JTextField = new JTextField ( "" );
	__Units_JTextField.addKeyListener ( this );
        JGUIUtil.addComponent(main_JPanel, __Units_JTextField,
		1, y, 2, 1, 1, 0, insetsTLBR, gbc.HORIZONTAL, gbc.WEST);
        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Indicate return units (e.g., CFS if database is CMS)."),
		3, y, 2, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.WEST);

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

	__cancel_JButton = new SimpleJButton( "Cancel", this);
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
	catch ( Exception e ) {
		Message.printWarning ( 2, "", e );
	}
}

/**
Process ItemEvents.
*/
public void itemStateChanged ( ItemEvent event )
{	refresh();
}

/**
Respond to KeyEvents.
*/
public void keyPressed ( KeyEvent event )
{	refresh();
}

/**
Need this to properly capture key events, especially deletes.
*/
public void keyReleased ( KeyEvent event )
{	refresh();	
}

public void keyTyped ( KeyEvent event )
{
}

/**
Refresh the command from the other text field contents:
<pre>
TS X = readNWSRFSFS5Files(TSID="X",QueryStart="X",QueryEnd="X")
</pre>
*/
private void refresh ()
{	String routine = "readNWSRFSFS5Files.refresh";
	String Alias = "";
	__error_wait = false;
	String TSID = "";
	String DataType = "";
	String Interval = "";
	String Where1 = "";
	String filter_delim = ";";
	String QueryStart = "";
	String QueryEnd = "";
	String Units = "";
	String command = ((String)__command_Vector.elementAt(0)).trim();
	String substring;
	if ( __first_time ) {
		__first_time = false;
		if ( ((String)__command_Vector.elementAt(0)).indexOf('=')>=0){
			// Because the parameters contain =, find the first =
			// to break the assignment...
			int pos = -1;	// Will be incremented to zero
					// for !__read_one
			if ( __read_one ) {
				pos = command.indexOf('=');
				substring = command.substring(0,pos).trim();
				Vector v = StringUtil.breakStringList (
					substring, " ",
					StringUtil.DELIM_SKIP_BLANKS ); 
				// First field has format "TS X"
				Alias = ((String)v.elementAt(1)).trim();
				__Alias_JTextField.setText ( Alias );
			}
			// Parse the parameters, include the command...
			substring = command.substring(pos + 1).trim();
			Vector v = StringUtil.breakStringList (
				substring,"()",
				StringUtil.DELIM_SKIP_BLANKS );
			PropList props = null;
			if (	(v != null) && (v.size() > 1) &&
				(substring.indexOf("=") > 0) ) {
				props = PropList.parse(
					(String)v.elementAt(1), routine, ",");
			}
			if ( props == null ) {
				props = new PropList ( routine );
			}
			TSID = props.getValue ( "TSID" );
			DataType = props.getValue ( "DataType" );
			Interval = props.getValue ( "Interval" );
			QueryStart = props.getValue ( "QueryStart" );
			QueryEnd = props.getValue ( "QueryEnd" );
			Units = props.getValue ( "Units" );
			if ( !__read_one ) {
				if ( DataType != null ) {
					__DataType_JTextField.setText(DataType);
				}
				if ( Interval != null ) {
					__Interval_JTextField.setText(Interval);
				}
			}
			if ( TSID != null ) {
				try {	TSIdent tsident = new TSIdent ( TSID );
					if ( __location_JTextField != null ) {
						__location_JTextField.setText (
						tsident.getLocation() );
					}
					if ( __datasource_JTextField != null ) {
						__datasource_JTextField.setText(
						tsident.getSource() );
					}
					__DataType_JTextField.setText (
						tsident.getType() );
					__Interval_JTextField.setText (
						tsident.getInterval() );
				}
				catch ( Exception e ) {
					// For now do nothing.
				}
			}
			if ( !__read_one ) {
			InputFilter_JPanel filter_panel =
				__input_filter_NWSRFS_FS5Files_JPanel;
			int nfg = filter_panel.getNumFilterGroups();
			String where;
			InputFilter filter;
			for ( int ifg = 0; ifg < nfg; ifg ++ ) {
				filter = (InputFilter)
				__input_filter_NWSRFS_FS5Files_JPanel.
				getInputFilter(ifg);
				where = props.getValue ( "Where" + (ifg + 1) );
				if ( where != null ) {
					// Set the filter...
					try {	filter_panel.setInputFilter (
						ifg, where, filter_delim );
					}
					catch ( Exception e ) {
						Message.printWarning ( 1,
						routine,
						"Error setting where " +
						"information using \"" + where
						+ "\"" );
						Message.printWarning ( 2,
						routine, e );
					}
				}
			}
			} // end !__read_one
			if ( QueryStart != null ) {
				__QueryStart_JTextField.setText ( QueryStart );
			}
			if ( QueryEnd != null ) {
				__QueryEnd_JTextField.setText ( QueryEnd );
			}
			if ( Units != null ) {
				__Units_JTextField.setText ( Units );
			}
		}
	}
	// Regardless, reset the command from the fields...
	if ( __read_one ) {
		Alias = __Alias_JTextField.getText().trim();
		if ( __nwsrfs_dmi.openedWithAppsDefaults() ) {
			TSID =	__location_JTextField.getText().trim() + "." +
			__datasource_JTextField.getText().trim() + "." +
			__DataType_JTextField.getText().trim() + "." +
			__Interval_JTextField.getText().trim() +
			"~NWSRFS_FS5Files";
		}
		else {	TSID =	__location_JTextField.getText().trim() + "." +
			__datasource_JTextField.getText().trim() + "." +
			__DataType_JTextField.getText().trim() + "." +
			__Interval_JTextField.getText().trim() +
			"~NWSRFS_FS5Files~" +__nwsrfs_dmi.getFS5FilesLocation();
		}
	}
	else {	DataType = __DataType_JTextField.getText().trim();
		Interval = __Interval_JTextField.getText().trim();
	}
	QueryStart = __QueryStart_JTextField.getText().trim();
	QueryEnd = __QueryEnd_JTextField.getText().trim();
	Units = __Units_JTextField.getText().trim();
	StringBuffer b = new StringBuffer ();
	if ( __read_one ) {
		if ( TSID.length() > 0 ) {
			b.append ( "TSID=\"" + TSID + "\"" );
		}
	}
	if ( !__read_one && (DataType != null) && (DataType.length() > 0) ) {
		if ( b.length() > 0 ) {
			b.append ( "," );
		}
		b.append ( "DataType=\"" + DataType + "\"" );
	}
	if ( !__read_one && (Interval != null) && (Interval.length() > 0) ) {
		if ( b.length() > 0 ) {
			b.append ( "," );
		}
		b.append ( "Interval=\"" + Interval + "\"" );
	}
	if ( !__read_one ) {
		// Add the where clause...
		InputFilter_JPanel filter_panel =
			__input_filter_NWSRFS_FS5Files_JPanel;
		int nfg = filter_panel.getNumFilterGroups();
		InputFilter filter;
		String where;
		String delim = ";";	// To separate input filter parts
		for ( int ifg = 0; ifg < nfg; ifg ++ ) {
			filter = (InputFilter)
			__input_filter_NWSRFS_FS5Files_JPanel.
			getInputFilter(ifg);
			where = filter_panel.toString(ifg,delim).trim();
			// Make sure there is a field that is being checked in
			// a where clause...
			if (	(where.length() > 0) &&
				!where.startsWith(delim) ) {
				if ( b.length() > 0 ) {
					b.append ( "," );
				}
				b.append ( "Where" + (ifg + 1) +
					"=\"" + where + "\"" );
			}
		}
	}
	if ( QueryStart.length() > 0 ) {
		if ( b.length() > 0 ) {
			b.append ( "," );
		}
		b.append ( "QueryStart=\"" + QueryStart + "\"" );
	}
	if ( QueryEnd.length() > 0 ) {
		if ( b.length() > 0 ) {
			b.append ( "," );
		}
		b.append ( "QueryEnd=\"" + QueryEnd + "\"" );
	}
	if ( Units.length() > 0 ) {
		if ( b.length() > 0 ) {
			b.append ( "," );
		}
		b.append ( "Units=\"" + Units + "\"" );
	}
	if ( __read_one ) {
		__TSID_JTextField.setText ( TSID );
		__command_JTextField.setText("TS " + Alias +
		" = readNWSRFSFS5Files(" + b.toString() + ")" );
	}
	else {	__command_JTextField.setText( "readNWSRFSFS5Files(" +
		b.toString() + ")" );
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

} // end readNWSRFSFS5Files_JDialog