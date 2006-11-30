// ----------------------------------------------------------------------------
// TSTool_Options_JDialog - tabbed-panel options dialog for TSTool
// ----------------------------------------------------------------------------
// Copyright:	See the COPYRIGHT file.
// ----------------------------------------------------------------------------
// History: 
//
// 2004-01-29	Steven A. Malers, RTi	Initial version (copy and modify
//					readDateValue).
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
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import java.io.File;
import java.util.Vector;

import RTi.Util.GUI.JFileChooserFactory;
import RTi.Util.GUI.JGUIUtil;
import RTi.Util.GUI.SimpleFileFilter;
import RTi.Util.GUI.SimpleJButton;
import RTi.Util.IO.IOUtil;
import RTi.Util.IO.PropList;
import RTi.Util.Message.Message;
import RTi.Util.String.StringUtil;

public class TSTool_Options_JDialog extends JDialog
implements ActionListener, KeyListener, WindowListener
{
private SimpleJButton	__cancel_JButton = null,// Cancel JButton
			__ok_JButton = null;	// Ok JButton
private JFrame		__parent_JFrame = null;	// parent JFrame GUI class
private JTextField	__HydroBase_wdid_length_JTextField = null;
						// WDID length
private String		__working_dir = null;	// Working directory.
private boolean		__error_wait = false;	// Is there an error that we
						// are waiting to be cleared up
						// or Cancel?
private PropList	__app_PropList = null;	// PropList from TSTool, used
						// to initialize data and
						// returned modified properties.
private boolean		__HydroBase_enabled = true;
						// Indicates whether HydroBase
						// input type is enabled in
						// TSTool.

/**
TSTool_Options_JDialog constructor.
@param parent JFrame class instantiating this class.
@param app_PropList Properties from the application.
*/
public TSTool_Options_JDialog ( JFrame parent, PropList app_PropList )
{	super(parent, true);
	__parent_JFrame = parent;
	__app_PropList = app_PropList;
	String prop_value = __app_PropList.getValue ("TSTool.HydroBaseEnabled");
	__HydroBase_enabled = false;
	if ( (prop_value != null) && prop_value.equalsIgnoreCase("true") ) {
		__HydroBase_enabled = true;
	}
	initialize ();
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
{	String routine = "TSTool_Options_JDialog.checkInput";
	String warning = "";
	String wdid_length =__HydroBase_wdid_length_JTextField.getText().trim();
	if ( __HydroBase_enabled ) {
		if (	!StringUtil.isInteger(wdid_length) ||
			(StringUtil.atoi(wdid_length) < 6) ) {
			warning +=
			"\nThe HydroBase WDID length is not an integer >= 6";
		}
	}
	if ( warning.length() > 0 ) {
		Message.printWarning ( 1, routine, warning );
		__error_wait = true;
	}
}

/**
Free memory for garbage collection.
@exception Throwable if there is an error.
*/
protected void finalize ()
throws Throwable
{	__cancel_JButton = null;
	__HydroBase_wdid_length_JTextField = null;
	__ok_JButton = null;
	__parent_JFrame = null;
	__working_dir = null;
	super.finalize ();
}

/**
Instantiates the GUI components.
*/
private void initialize ()
{	__working_dir = __app_PropList.getValue ( "WorkingDir" );

	addWindowListener( this );

        Insets insetsTLBR = new Insets(2,2,2,2);

	JTabbedPane main_JTabbedPane = new JTabbedPane();
	GridBagLayout gbl = new GridBagLayout();
	GridBagConstraints gbc = new GridBagConstraints();
	JPanel main_JPanel = new JPanel();
	main_JPanel.setLayout( gbl );
        JGUIUtil.addComponent(main_JPanel, main_JTabbedPane,
		0, 0, 1, 1, 1, 1, insetsTLBR, gbc.BOTH, gbc.CENTER);
	getContentPane().add ( "North", main_JPanel );
	int y = 0;

	// General tab...
	JPanel general_JPanel = new JPanel();
	general_JPanel.setLayout ( gbl );
        JGUIUtil.addComponent(general_JPanel, new JLabel (
		"General properties are being enabled."),
		0, 0, 1, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.WEST);
	main_JTabbedPane.addTab ( "General", general_JPanel );

	// HydroBase tab...

	if ( __HydroBase_enabled ) {
		JPanel HydroBase_JPanel = new JPanel();
		HydroBase_JPanel.setLayout ( gbl );
		y = 0;
        	JGUIUtil.addComponent(HydroBase_JPanel,
			new JLabel ( "WDID Length:"),
			0, y, 1, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.EAST);
		String wdid_length = __app_PropList.getValue (
			"HydroBase.WDIDLength" );
		if ( wdid_length == null ) {
			wdid_length = "7";
		}
		__HydroBase_wdid_length_JTextField =
			new JTextField ( wdid_length, 5 );
		JGUIUtil.addComponent(HydroBase_JPanel,
			__HydroBase_wdid_length_JTextField,
			1, y, 1, 1, 0, 0, insetsTLBR, gbc.NONE, gbc.WEST);
		main_JTabbedPane.addTab ( "HydroBase", HydroBase_JPanel );
	}

	// South Panel: North
	JPanel button_JPanel = new JPanel();
	button_JPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JGUIUtil.addComponent(main_JPanel, button_JPanel, 
		0, 1, 1, 1, 1, 0, insetsTLBR, gbc.HORIZONTAL, gbc.CENTER);

	__cancel_JButton = new SimpleJButton("Cancel", this);
	button_JPanel.add ( __cancel_JButton );
	__ok_JButton = new SimpleJButton("OK", this);
	button_JPanel.add ( __ok_JButton );

	setTitle ( JGUIUtil.getAppNameForWindows() + " - Options" );
	setResizable ( true );
        pack();
        JGUIUtil.center( this );
        super.setVisible( true );
}

/**
Respond to KeyEvents.
*/
public void keyPressed ( KeyEvent event )
{	int code = event.getKeyCode();

	if ( code == event.VK_ENTER ) {
		checkInput();
		if ( !__error_wait ) {
			response ( 1 );
		}
	}
}

public void keyReleased ( KeyEvent event )
{	
}

public void keyTyped ( KeyEvent event )
{	
}

/**
Return the properties as a PropList.
@return returns the command text or null if no command.
*/
public PropList response ( int status )
{	setVisible( false );
	dispose();
	if ( status == 0 ) {
		// Cancel...
		return null;
	}
	else {	// Update the properties in the PropList...
		if ( __HydroBase_enabled ) {
			__app_PropList.set ( "HydroBase.WDIDLength",
			__HydroBase_wdid_length_JTextField.getText().trim() );
		}
		// Return the updated properties...
		return __app_PropList;
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

} // end TSTool_Options_JDialog