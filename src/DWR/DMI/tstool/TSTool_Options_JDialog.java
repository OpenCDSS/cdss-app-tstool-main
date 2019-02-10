// TSTool_Options_JDialog - Editor for TSTool configuration options.

/* NoticeStart

TSTool
TSTool is a part of Colorado's Decision Support Systems (CDSS)
Copyright (C) 1994-2019 Colorado Department of Natural Resources

TSTool is free software:  you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    TSTool is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with TSTool.  If not, see <https://www.gnu.org/licenses/>.

NoticeEnd */

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
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

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
@SuppressWarnings("serial")
public class TSTool_Options_JDialog extends JDialog
implements ActionListener, KeyListener, WindowListener
{
	
public static final String TSTool_RunCommandProcessorInThread = "TSTool.RunCommandProcessorInThread";

private final String __False = "False";
private final String __True = "True";
	
private SimpleJButton __cancel_JButton = null;
private SimpleJButton __ok_JButton = null;
private JCheckBox __General_RunThreaded_JCheckBox = null;
/**
Checkbox for installation configuration file datastore enable/disable state.
*/
private JCheckBox [] __datastoreInstallEnabled_JCheckBox = null;
/**
Installation configuration file datastore enable/disable state when first read from configuration file.
*/
private boolean [] __datastoreEnabledInstallOriginalState = null;
/**
Checkbox for user configuration file datastore enable/disable state.
*/
private JCheckBox [] __datastoreUserEnabled_JCheckBox = null;
/**
User configuration file datastore enable/disable state when first read from configuration file.
*/
private boolean [] __datastoreEnabledUserOriginalState = null;
/**
Is there an error waiting to be resolved before closing dialog?
*/
private boolean __error_wait = false;
/**
PropList from TSTool installation configuration file, with configuration properties.
Currently this has limited use and only applies to the installation configuration file.
*/
private PropList __appPropList = null;
/**
TSTool session information, to locate user configuration files.
*/
private TSToolSession __session = null;
/**
Path to TSTool.cfg for installation location.
*/
private String __configFilePathInstall = "";

/**
TSTool_Options_JDialog constructor.
@param parent JFrame class instantiating this class.
@param session the TSTool session, which provides information about user files.
@param configFilePathInstall path to the installation TSTool configuration file.
@param appPropList Properties from the application - a subset of all props that are specifically handled.
*/
public TSTool_Options_JDialog ( JFrame parent, TSToolSession session, String configFilePathInstall, PropList appPropList )
{	super(parent, true);
	__session = session;
	__appPropList = appPropList;
	__configFilePathInstall = configFilePathInstall;
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
Get the configuration file properties that indicate whether a datastore or input type is enabled.
*/
private PropList getDatastoreEnabledProperties ( PropList configFilePropList )
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
Instantiates the UI components.
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
	int yDsInstall = 0;

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

	// Panel for enabled datastores and input types, from software installation...
	
    JPanel enabledDatastoresInstall_JPanel = new JPanel();
    enabledDatastoresInstall_JPanel.setLayout ( gbl );
    main_JTabbedPane.addTab ( "Datastores and Input Types (Installation)", enabledDatastoresInstall_JPanel );
    
    yDsInstall = -1;
    JGUIUtil.addComponent(enabledDatastoresInstall_JPanel,
        new JLabel("Specify the configuration file properties to enable/disable datastores and input types.  Restart TSTool to see changes."),
        0, ++yDsInstall, 10, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
    JGUIUtil.addComponent(enabledDatastoresInstall_JPanel,
        new JLabel("Refer to TSTool datastore and input type documentation appendices for configuration information."),
        0, ++yDsInstall, 10, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
    JGUIUtil.addComponent(enabledDatastoresInstall_JPanel,
        new JLabel("<html><b>These settings are for the TSTool installation settings.  Also refer to TSTool user settings."),
        0, ++yDsInstall, 10, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
    
    // The __appPropList only contains a few special properties.  Because the input type enabling is
    // handled by updating the configuration file, read the current configuration file here to get the current list
    // of properties.
    
    File configFileInstall = new File(__configFilePathInstall);
    if ( !configFileInstall.canRead() ) {
        JGUIUtil.addComponent(enabledDatastoresInstall_JPanel,
            new JLabel("Configuration file (installation) is not readable: " + __configFilePathInstall),
            0, ++yDsInstall, 10, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(enabledDatastoresInstall_JPanel,new JSeparator(SwingConstants.HORIZONTAL),
            0, ++yDsInstall, 10, 1, 0, 0, insetsTLBR, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
    }
    else if ( !configFileInstall.canWrite() ) {
        JGUIUtil.addComponent(enabledDatastoresInstall_JPanel,
            new JLabel("Configuration file (installation) is not writeable (contact system support or change TSTool user settings): " + __configFilePathInstall),
            0, ++yDsInstall, 10, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(enabledDatastoresInstall_JPanel,new JSeparator(SwingConstants.HORIZONTAL),
            0, ++yDsInstall, 10, 1, 0, 0, insetsTLBR, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
    }
    else {
        JGUIUtil.addComponent(enabledDatastoresInstall_JPanel,
            new JLabel("Configuration file (installation): " + __configFilePathInstall),
            0, ++yDsInstall, 10, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(enabledDatastoresInstall_JPanel,new JSeparator(SwingConstants.HORIZONTAL),
            0, ++yDsInstall, 10, 1, 0, 0, insetsTLBR, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
        PropList configPropList = new PropList("");
        configPropList.setPersistentName(__configFilePathInstall);
        try {
            configPropList.readPersistent();
            PropList inputTypeProps = getDatastoreEnabledProperties ( configPropList );
            __datastoreInstallEnabled_JCheckBox = new JCheckBox[inputTypeProps.size()];
            __datastoreEnabledInstallOriginalState = new boolean[inputTypeProps.size()];
            int x = 0;
            for ( int i = 0; i < inputTypeProps.size(); i++ ) {
                if ( i == inputTypeProps.size()/2 ) {
                    // Start a second column
                    x = 1;
                    yDsInstall -= inputTypeProps.size()/2;
                }
                Prop prop = inputTypeProps.elementAt(i);
                String label = prop.getKey();
                int pos = label.indexOf("."); // Because properties start with TSTool.
                if ( pos >= 0 ) {
                    label = label.substring(pos+1);
                }
                __datastoreEnabledInstallOriginalState[i] = false;
                if ( prop.getValue().equalsIgnoreCase("true")) {
                    __datastoreEnabledInstallOriginalState[i] = true;
                }
                __datastoreInstallEnabled_JCheckBox[i] = new JCheckBox(label,__datastoreEnabledInstallOriginalState[i]);
                JGUIUtil.addComponent(enabledDatastoresInstall_JPanel,
                    __datastoreInstallEnabled_JCheckBox[i],
                    x, ++yDsInstall, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
            }
        }
        catch ( Exception e ) {
            JGUIUtil.addComponent(enabledDatastoresInstall_JPanel,
                new JLabel("Error reading installation configuration file \"" + __configFilePathInstall + "\" (" + e + ")."),
                0, ++yDsInstall, 10, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        }
    }
    
	// Panel for enabled datastores and input types, from user folder...
	
    JPanel enabledDatastoresUser_JPanel = new JPanel();
    enabledDatastoresUser_JPanel.setLayout ( gbl );
    main_JTabbedPane.addTab ( "Datastores and Input Types (User)", enabledDatastoresUser_JPanel );
    
    yDsInstall = -1;
    JGUIUtil.addComponent(enabledDatastoresUser_JPanel,
        new JLabel("Specify the configuration file properties to enable/disable datastores and input types.  Restart TSTool to see changes."),
        0, ++yDsInstall, 10, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
    JGUIUtil.addComponent(enabledDatastoresUser_JPanel,
        new JLabel("Refer to TSTool datastore and input type documentation appendices for configuration information."),
        0, ++yDsInstall, 10, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
    JGUIUtil.addComponent(enabledDatastoresUser_JPanel,
        new JLabel("<html><b>These settings are for the TSTool user settings.  Also refer to TSTool installation settings.</b></html>"),
        0, ++yDsInstall, 10, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
    JGUIUtil.addComponent(enabledDatastoresUser_JPanel,
        new JLabel("<html><b>User settings will override the installation settings.</b></html>"),
        0, ++yDsInstall, 10, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
    JGUIUtil.addComponent(enabledDatastoresUser_JPanel,
        new JLabel("<html><b>You may need to edit the \"" + __session.getConfigFile() + "\" file to add properties.</b></html>"),
        0, ++yDsInstall, 10, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
    JGUIUtil.addComponent(enabledDatastoresUser_JPanel,
        new JLabel("<html><b>Press OK to initialize a new user configuration file.</b></html>"),
        0, ++yDsInstall, 10, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
    
    File configFileUser = new File(__session.getConfigFile());
    if ( !configFileUser.canRead() ) {
        JGUIUtil.addComponent(enabledDatastoresUser_JPanel,
            new JLabel("Configuration file (user) is not readable: " + __session.getConfigFile()),
            0, ++yDsInstall, 10, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(enabledDatastoresUser_JPanel,new JSeparator(SwingConstants.HORIZONTAL),
            0, ++yDsInstall, 10, 1, 0, 0, insetsTLBR, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
    }
    else if ( !configFileUser.canWrite() ) {
        JGUIUtil.addComponent(enabledDatastoresUser_JPanel,
            new JLabel("Configuration file (user) is not writeable (contact TSTool support because this should not happen): " + __session.getConfigFile()),
            0, ++yDsInstall, 10, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(enabledDatastoresUser_JPanel,new JSeparator(SwingConstants.HORIZONTAL),
            0, ++yDsInstall, 10, 1, 0, 0, insetsTLBR, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
    }
    else {
        JGUIUtil.addComponent(enabledDatastoresUser_JPanel,
            new JLabel("Configuration file (user): " + __session.getConfigFile()),
            0, ++yDsInstall, 10, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(enabledDatastoresUser_JPanel,new JSeparator(SwingConstants.HORIZONTAL),
            0, ++yDsInstall, 10, 1, 0, 0, insetsTLBR, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
        PropList configPropList = new PropList("");
        configPropList.setPersistentName(__session.getConfigFile());
        try {
            configPropList.readPersistent();
            PropList inputTypeProps = getDatastoreEnabledProperties ( configPropList );
            __datastoreUserEnabled_JCheckBox = new JCheckBox[inputTypeProps.size()];
            __datastoreEnabledUserOriginalState = new boolean[inputTypeProps.size()];
            int x = 0;
            for ( int i = 0; i < inputTypeProps.size(); i++ ) {
                if ( i == inputTypeProps.size()/2 ) {
                    // Start a second column
                    x = 1;
                    yDsInstall -= inputTypeProps.size()/2;
                }
                Prop prop = inputTypeProps.elementAt(i);
                String label = prop.getKey();
                int pos = label.indexOf("."); // Because properties start with TSTool.
                if ( pos >= 0 ) {
                    label = label.substring(pos+1);
                }
                __datastoreEnabledUserOriginalState[i] = false;
                if ( prop.getValue().equalsIgnoreCase("true")) {
                    __datastoreEnabledUserOriginalState[i] = true;
                }
                __datastoreUserEnabled_JCheckBox[i] = new JCheckBox(label,__datastoreEnabledUserOriginalState[i]);
                JGUIUtil.addComponent(enabledDatastoresUser_JPanel,
                    __datastoreUserEnabled_JCheckBox[i],
                    x, ++yDsInstall, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
            }
        }
        catch ( Exception e ) {
            JGUIUtil.addComponent(enabledDatastoresUser_JPanel,
                new JLabel("Error reading user configuration file \"" + __session.getConfigFile() + "\" (" + e + ")."),
                0, ++yDsInstall, 10, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
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
   
    // Select the Enabled Input Types because they are more generally of interest to users
    
    main_JTabbedPane.setSelectedComponent(enabledDatastoresInstall_JPanel);

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
		// Save the input type enabled values to the TSTool configuration files, for installation and user versions
		updateConfigurationFile ( __configFilePathInstall, __datastoreInstallEnabled_JCheckBox, __datastoreEnabledInstallOriginalState );
		// If necessary, initialize the user configuration file
		File f = new File(__session.getConfigFile());
		if ( f.exists() || __session.createConfigFile() ) {
			// File exists so update the file OR it did not exist but was created so update the file
			updateConfigurationFile ( __session.getConfigFile(), __datastoreUserEnabled_JCheckBox, __datastoreEnabledUserOriginalState );
		}
		// Return the updated properties...
		return __appPropList;
	}
}

// TODO SAM 2011-05-22 Need a package to update the configuration file
/**
Update the values in the configuration file.  So as to not change the comments
in the file, specific strings are processed.
@param configurationFile full path to TSTool.cfg configuration file, can be installation or user version.
*/
private void updateConfigurationFile ( String configurationFile, JCheckBox [] datastoreEnabled_JCheckBox, boolean [] datastoreEnabledOriginalState )
{   String routine = getClass().getSimpleName() + ".updateConfigurationFile";
    // Figure out if any configuration property values were changed by the user
    if ( datastoreEnabled_JCheckBox == null ) {
        // User did not have permission.
        return;
    }
    int changeCount = 0;
    for ( int i = 0; i < datastoreEnabled_JCheckBox.length; i++ ) {
        boolean isSelected = datastoreEnabled_JCheckBox[i].isSelected();
        if ( isSelected != datastoreEnabledOriginalState[i] ) {
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
                for ( int i = 0; i < datastoreEnabled_JCheckBox.length; i++ ) {
                    if ( datastoreEnabled_JCheckBox[i].getText().equalsIgnoreCase(propName) ) {
                        // Matched the label so set the property if value has changed
                        if ( datastoreEnabledOriginalState[i] != datastoreEnabled_JCheckBox[i].isSelected() ) {
                            String newString = propName + " = " + datastoreEnabled_JCheckBox[i].isSelected();
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
