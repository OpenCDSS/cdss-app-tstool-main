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
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import RTi.Util.GUI.JGUIUtil;
import RTi.Util.GUI.SimpleJButton;
import RTi.Util.IO.IOUtil;
import RTi.Util.IO.Prop;
import RTi.Util.IO.PropList;
import RTi.Util.Message.Message;
import RTi.Util.String.StringUtil;

/**
Editor for TSTool configuration options.
*/
public class TSTool_Options_JDialog extends JDialog
implements ActionListener, KeyListener, WindowListener
{
	
public static final String TSTool_RunCommandProcessorInThread = "TSTool.RunCommandProcessorInThread";

private final String __False = "False";
private final String __True = "True";
	
private SimpleJButton __cancel_JButton = null;
private SimpleJButton __ok_JButton = null;
private JCheckBox __General_RunThreaded_JCheckBox = null;
private JCheckBox [] __inputTypeEnabled_JCheckBox = null;
private boolean [] __inputTypeEnabledOriginalState = null;
private boolean __error_wait = false; // Is there an error waiting to be cleared up?

private PropList __appPropList = null; // PropList from TSTool, with configuration properties.
private String __configFilePath = "";

/**
TSTool_Options_JDialog constructor.
@param parent JFrame class instantiating this class.
@param appPropList Properties from the application - a subset of all props that are specifically handled.
*/
public TSTool_Options_JDialog ( JFrame parent, String configFilePath, PropList appPropList )
{	super(parent, true);
	__appPropList = appPropList;
	__configFilePath = configFilePath;
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
{	
}

/**
Free memory for garbage collection.
@exception Throwable if there is an error.
*/
protected void finalize ()
throws Throwable
{	__cancel_JButton = null;
	__ok_JButton = null;
	super.finalize ();
}

/**
Get the input type properties from configuration file properties.
*/
private PropList getInputTypeProperties ( PropList configFilePropList )
{
    PropList inputTypeProps = new PropList("");
    for ( int i = 0; i < configFilePropList.size(); i++ ) {
        Prop prop = configFilePropList.elementAt(i);
        if ( prop.getKey().toUpperCase().endsWith("ENABLED") ) {
            inputTypeProps.set(prop);
        }
    }
    return inputTypeProps;
}

/**
Instantiates the GUI components.
*/
private void initialize ()
{	addWindowListener( this );

    Insets insetsTLBR = new Insets(2,2,2,2);

	JTabbedPane main_JTabbedPane = new JTabbedPane();
	GridBagLayout gbl = new GridBagLayout();
	JPanel main_JPanel = new JPanel();
	main_JPanel.setLayout( gbl );
        JGUIUtil.addComponent(main_JPanel, main_JTabbedPane,
		0, 0, 1, 1, 1, 1, insetsTLBR, GridBagConstraints.BOTH, GridBagConstraints.CENTER);
	getContentPane().add ( "North", main_JPanel );
	int y = 0;

	// Developer tab...
	JPanel developer_JPanel = new JPanel();
	developer_JPanel.setLayout ( gbl );
    __General_RunThreaded_JCheckBox = new JCheckBox ();
    boolean RunCommandProcessorInThread_boolean = true;		// Default
	String RunCommandProcessorInThread_String = __appPropList.getValue ( TSTool_RunCommandProcessorInThread );
    if ( (RunCommandProcessorInThread_String != null) &&
    		RunCommandProcessorInThread_String.equalsIgnoreCase(__False) ) {
    	RunCommandProcessorInThread_boolean = false;
    }
    __General_RunThreaded_JCheckBox.setText("Run commands in thread (allows cancel) (under development).");
	__General_RunThreaded_JCheckBox.setSelected(RunCommandProcessorInThread_boolean);
	JGUIUtil.addComponent(developer_JPanel, __General_RunThreaded_JCheckBox,
		0, 0, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
	main_JTabbedPane.addTab ( "Developer", developer_JPanel );

	// Panel for enabled input types...
	
    JPanel enabledInputTypes_JPanel = new JPanel();
    enabledInputTypes_JPanel.setLayout ( gbl );
    
    y = 0;
    JGUIUtil.addComponent(enabledInputTypes_JPanel,
        new JLabel("Specify the configuration file properties to turn on input types.  Restart TSTool to see changes."),
        0, y, 10, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
    JGUIUtil.addComponent(enabledInputTypes_JPanel,
        new JLabel("Refer to TSTool input type and data store documentation appendices for configuration information."),
        0, ++y, 10, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
    
    // The __appPropList only contains a few special properties.  Because the input type enabling is
    // handled by updating the configuration file, read the current configuration file here to get the current list
    // of properties.
    
    File configFile = new File(__configFilePath);
    if ( !configFile.canRead() ) {
        JGUIUtil.addComponent(enabledInputTypes_JPanel,
            new JLabel("Configuration file is not readable: " + __configFilePath),
            0, ++y, 10, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
    }
    else if ( !configFile.canWrite() ) {
        JGUIUtil.addComponent(enabledInputTypes_JPanel,
            new JLabel("Configuration file is not writeable: " + __configFilePath),
            0, ++y, 10, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
    }
    else {
        JGUIUtil.addComponent(enabledInputTypes_JPanel,
            new JLabel("Configuration file: " + __configFilePath),
            0, ++y, 10, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        PropList configPropList = new PropList("");
        configPropList.setPersistentName(__configFilePath);
        try {
            configPropList.readPersistent();
            PropList inputTypeProps = getInputTypeProperties ( configPropList );
            __inputTypeEnabled_JCheckBox = new JCheckBox[inputTypeProps.size()];
            __inputTypeEnabledOriginalState = new boolean[inputTypeProps.size()];
            int x = 0;
            for ( int i = 0; i < inputTypeProps.size(); i++ ) {
                if ( i == inputTypeProps.size()/2 ) {
                    // Start a second column
                    x = 1;
                    y -= inputTypeProps.size()/2;
                }
                Prop prop = inputTypeProps.elementAt(i);
                String label = prop.getKey();
                int pos = label.indexOf("."); // Because properties start with TSTool.
                if ( pos >= 0 ) {
                    label = label.substring(pos+1);
                }
                __inputTypeEnabledOriginalState[i] = false;
                if ( prop.getValue().equalsIgnoreCase("true")) {
                    __inputTypeEnabledOriginalState[i] = true;
                }
                __inputTypeEnabled_JCheckBox[i] = new JCheckBox(label,__inputTypeEnabledOriginalState[i]);
                JGUIUtil.addComponent(enabledInputTypes_JPanel,
                    __inputTypeEnabled_JCheckBox[i],
                    x, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
            }
        }
        catch ( Exception e ) {
            JGUIUtil.addComponent(enabledInputTypes_JPanel,
                new JLabel("Error reading configuration file (" + e + ")."),
                0, ++y, 10, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        }
    }
    
    /*
    __General_RunThreaded_JCheckBox = new JCheckBox ();
    boolean RunCommandProcessorInThread_boolean = true;     // Default
    String RunCommandProcessorInThread_String = __appPropList.getValue ( TSTool_RunCommandProcessorInThread );
    if ( (RunCommandProcessorInThread_String != null) &&
            RunCommandProcessorInThread_String.equalsIgnoreCase(__False) ) {
        RunCommandProcessorInThread_boolean = false;
    }
    __General_RunThreaded_JCheckBox.setText("Run commands in thread (allows cancel) (under development).");
    __General_RunThreaded_JCheckBox.setSelected(RunCommandProcessorInThread_boolean);

    JGUIUtil.addComponent(developer_JPanel,
            __General_RunThreaded_JCheckBox,
            0, 0, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
                    */
    main_JTabbedPane.addTab ( "Enabled Input Types", enabledInputTypes_JPanel );
    
    // Select the Enabled Input Types because they are more generally of interest to users
    
    main_JTabbedPane.setSelectedComponent(enabledInputTypes_JPanel);

	// South Panel: North
	JPanel button_JPanel = new JPanel();
	button_JPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JGUIUtil.addComponent(main_JPanel, button_JPanel, 
		0, 1, 1, 1, 1, 0, insetsTLBR, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER);

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

	if ( code == KeyEvent.VK_ENTER ) {
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
	else {
	    // Update the properties in the PropList...
		if ( __General_RunThreaded_JCheckBox.isSelected() ) {
			__appPropList.set ( TSTool_RunCommandProcessorInThread, __True);
		}
		else {
			__appPropList.set ( TSTool_RunCommandProcessorInThread, __False);
		}
		// Save the input type enabled values to the properties file
		updateConfigurationFile ( __configFilePath );
		// Return the updated properties...
		return __appPropList;
	}
}

// TODO SAM 2011-05-22 Need a package to update the configuration file
/**
Update the values in the configuration file.  So as to not change the comments
in the file, specific strings are processed.
*/
private void updateConfigurationFile ( String configurationFile )
{   String routine = "updateConfigurationFile";
    // Figure out if any configuration property values where changed by the user
    if ( __inputTypeEnabled_JCheckBox == null ) {
        // User did not have permission.
        return;
    }
    int changeCount = 0;
    for ( int i = 0; i < __inputTypeEnabled_JCheckBox.length; i++ ) {
        boolean isSelected = __inputTypeEnabled_JCheckBox[i].isSelected();
        if ( isSelected != __inputTypeEnabledOriginalState[i] ) {
            ++changeCount;
        }
    }
    if ( changeCount == 0 ) {
        // No need to update configuration file
        return;
    }
    // Read the configuration file into a string list
    List<String> stringList = null;
    try {
        stringList = IOUtil.fileToStringList(configurationFile);
    }
    catch ( Exception e ) {
        Message.printWarning(1, routine, "Error reading current configuration file \"" +
            configurationFile + "\" - cannot update (" + e + ")." );
        return;
    }
    // Modify the strings
    int pos;
    String s;
    for ( int iString = 0; iString < stringList.size(); ++iString) {
        // First figure out if it is one of the InputTypeEnabled properties
        s = stringList.get(iString);
        pos = s.indexOf("=");
        if ( pos > 0 ) {
            String[] parts = s.split("=");
            if ( parts.length == 2 ) {
                String propName = parts[0].trim();
                // Find the matching checkbox
                for ( int i = 0; i < __inputTypeEnabled_JCheckBox.length; i++ ) {
                    if ( __inputTypeEnabled_JCheckBox[i].getText().equalsIgnoreCase(propName) ) {
                        // Matched the label so set the property if value has changed
                        if ( __inputTypeEnabledOriginalState[i] != __inputTypeEnabled_JCheckBox[i].isSelected() ) {
                            String newString = propName + " = " + __inputTypeEnabled_JCheckBox[i].isSelected();
                            stringList.set(iString, newString);
                        }
                        // No need to continue searching
                        break;
                    }
                }
            }
        }
    }
    // Now write out the file
    try {
        IOUtil.writeFile(configurationFile,StringUtil.toString(stringList, System.getProperty("line.separator")));
    }
    catch ( Exception e ) {
        Message.printWarning(1, routine, "Error writing updated configuration file \"" +
            configurationFile + "\" - cannot update (" + e + ")." );
        return;
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

}