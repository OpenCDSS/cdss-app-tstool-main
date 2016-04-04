package DWR.DMI.tstool;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import RTi.Util.IO.IOUtil;
import RTi.Util.IO.PropList;
import RTi.Util.Message.Message;

/**
Class to maintain TSTool session information such as the history of command files opened.
*/
public class TSToolSession
{

/**
Global value that indicates if the file is being written.
Need to handle because if the file is being modified at the same time exceptions will be thrown.
*/
private boolean historyBeingWritten = false;

/**
Construct the session instance.
*/
public TSToolSession ()
{
}

/**
Create the datastore folder if necessary.
@return true if datastore folder exists and is writeable, false otherwise.
*/
public boolean createDatastoreFolder () {
	String datastoreFolder = getDatastoreFolder();
	// Do not allow datastore folder to be created under Linux root but allow TSTool to run
	if ( datastoreFolder.equals("/") ) {
		return false;
	}
	File f = new File(datastoreFolder);
	if ( !f.exists() ) {
		try {
			f.mkdirs();
		}
		catch ( SecurityException e ) {
			return false;
		}
	}
	else {
		// Make sure it is writeable
		if ( !f.canWrite() ) {
			return false;
		}
	}
	return true;
}

/**
Create the log folder if necessary.
@return true if log folder exists and is writeable, false otherwise.
*/
public boolean createLogFolder () {
	String logFolder = getLogFolder();
	// Do not allow log file to be created under Linux root but allow TSTool to run
	if ( logFolder.equals("/") ) {
		return false;
	}
	File f = new File(logFolder);
	if ( !f.exists() ) {
		try {
			f.mkdirs();
		}
		catch ( SecurityException e ) {
			return false;
		}
	}
	else {
		// Make sure it is writeable
		if ( !f.canWrite() ) {
			return false;
		}
	}
	return true;
}

/**
Create a new system configuration file in user files.
This is used when transitioning from TSTool earlier than 11.09.00 to version later.
@return true if the file was created, false for all other cases.
*/
public boolean createConfigFile ( )
{
	if ( getUserFolder().equals("/") ) {
		// Don't allow files to be created under root on Linux
		return false;
	}

	// Create the configuration folder if necessary
	File f = new File(getConfigFile());
	File folder = f.getParentFile();
	if ( !folder.exists() ) {
		if ( !folder.mkdirs() ) {
			// Unable to make folder
			return false;
		}
	}
	try {
		String nl = System.getProperty("line.separator");
		StringBuilder sb = new StringBuilder ( "# TSTool configuration file containing user settings, shared between TSTool versions" + nl );
		sb.append("# This file indicates which datastore software features should be enabled." + nl );
		sb.append("# Disabling datastore types that are not used can improve TSTool performance and simplifies the user interface." + nl );
		sb.append("# Refer to the TSTool.cfg file under the software installation folder for global configuration properties." + nl );
		sb.append("# User settings in this file will override the installation settings." + nl );
		sb.append(nl);
		// Include a line for HydroBase since it often needs to be disabled on computers where HydroBase is not used
		sb.append("HydroBaseEnabled = true" + nl );
		IOUtil.writeFile ( f.getPath(), sb.toString() );
		return true;
	}
	catch ( Exception e ) {
		return false;
	}
}

/**
Create the system folder if necessary.
@return true if system folder exists and is writeable, false otherwise.
*/
public boolean createSystemFolder () {
	String systemFolder = getSystemFolder();
	// Do not allow system folder to be created under Linux root but allow TSTool to run
	if ( systemFolder.equals("/") ) {
		return false;
	}
	File f = new File(systemFolder);
	if ( !f.exists() ) {
		try {
			f.mkdirs();
		}
		catch ( SecurityException e ) {
			return false;
		}
	}
	else {
		// Make sure it is writeable
		if ( !f.canWrite() ) {
			return false;
		}
	}
	return true;
}

/**
Return the value of the requested property from the user's TSTool configuration file.
This reads the configuration file each time to ensure synchronization.
@param propName property name
@return the value of the property or null if file or property is not found
*/
public String getConfigPropValue ( String propName )
{
	String configFile = getConfigFile();
	File f = new File(configFile);
	if ( !f.exists() || !f.canRead() ) {
		return null;
	}
	PropList props = new PropList("TSToolUserConfig");
	props.setPersistentName(configFile);
	try {
		props.readPersistent();
		return props.getValue(propName);
	}
	catch ( Exception e ) {
		return null;
	}
}

/**
Return the name of the user's TSTool configuration file.
*/
public String getConfigFile ()
{
	String logFile = getSystemFolder() + File.separator + "TSTool.cfg";
	//Message.printStatus(1,"","Config file is \"" + logFolder + "\"");
	return logFile;
}

/**
Return the name of the datastore configuration folder.
*/
public String getDatastoreFolder ()
{
	String datastoreFolder = getUserFolder() + File.separator + "datastore";
	//Message.printStatus(1,"","Datastore folder is \"" + datastoreFolder + "\"");
	return datastoreFolder;
}

/**
Return the name of the TSTool history file.
*/
public String getHistoryFile ()
{
	String historyFile = System.getProperty("user.home") + File.separator + ".tstool" + File.separator + "command-file-history.txt";
	//Message.printStatus(1,"","History file \"" + historyFile + "\"");
	return historyFile;
}

/**
Return the name of the log file for the user.
*/
public String getLogFile ()
{
	String logFile = getLogFolder() + File.separator + "TSTool_" + System.getProperty("user.name") + ".log";
	//Message.printStatus(1,"","Log folder is \"" + logFolder + "\"");
	return logFile;
}

/**
Return the name of the log file folder.
*/
public String getLogFolder ()
{
	String logFolder = getUserFolder() + File.separator + "log";
	//Message.printStatus(1,"","Log folder is \"" + logFolder + "\"");
	return logFolder;
}

/**
Return the name of the system folder.
*/
public String getSystemFolder ()
{
	String systemFolder = getUserFolder() + File.separator + "system";
	//Message.printStatus(1,"","System folder is \"" + systemFolder + "\"");
	return systemFolder;
}

/**
Return the name of the TSTool user folder for the operating system, for example:
<ul>
<li>	Windows:  C:\Users\UserName\.tstool</li>
<li>	Linux: /home/UserName/.tstool</li>
</ul>
*/
public String getUserFolder ()
{
	String userFolder = System.getProperty("user.home") + File.separator + ".tstool";
	//Message.printStatus(1,"","User folder is \"" + userFolder + "\"");
	return userFolder;
}

/**
Push a new command file onto the history.  This reads the history, updates it, and writes it.
This is done because if multiple TSTool sessions are running they, will share the history.
@param commandFile full path to command file that has been opened
*/
public void pushHistory ( String commandFile )
{
	// Read the history file from the .tstool-history file
	List<String> history = readHistory();
	// Add in the first position so it will show up first in the File...Open... menu
	history.add(0, commandFile);
	// Process from the back so that old duplicates are removed and recent access is always at the top of the list
	// TODO SAM 2014-12-17 use a TSTool configuration file property to set cap
	int max = 100;
	String old;
	for ( int i = history.size() - 1; i >= 1; i-- ) {
		old = history.get(i);
		if ( i >= max ) {
			// Trim the history to the maximum
			history.remove(i);
		}
		else if ( old.equals(commandFile) || old.equals("") || old.startsWith("#")) {
			// Ignore comments, blank lines and duplicate to most recent access
			history.remove(i--);
		}
	}
	//Message.printStatus(2,"", "History length is " + history.size());
	// Write the updated history
	writeHistory(history);
}

/**
Read the history of command files that have been opened.
@return list of command files recently opened, newest first
*/
public List<String> readHistory()
{	//String routine = getClass().getSimpleName() + ".readHistory";
	try {
		List<String> history = IOUtil.fileToStringList(getHistoryFile());
		// Remove comment lines
		for ( int i = (history.size() - 1); i >= 0; i-- ) {
			String f = history.get(i);
			if ( f.startsWith("#") ) {
				history.remove(i);
			}
		}
		return history;
	}
	catch ( Exception e ) {
		// For now just swallow exception - may be because the history folder does not exist
		//Message.printWarning(3,routine,e);
		return new ArrayList<String>();
	}
}

/**
Write the history of commands files that have been opened.
*/
private void writeHistory ( List<String> history )
{
	String nl = System.getProperty("line.separator");
	StringBuilder sb = new StringBuilder ( "# TSTool command file history, most recent at top, shared between TSTool instances" );
	
	if ( getUserFolder().equals("/") ) {
		// Don't allow files to be created under root on Linux
		return;
	}

	long ms = System.currentTimeMillis();
	while ( this.historyBeingWritten ) {
		// Need to wait until another operation finishes writing
		// But don't wait longer than a couple of seconds before moving on
		if ( (System.currentTimeMillis() - ms) > 2000 ) {
			break;
		}
	}
	// Now can continue
	try {
	
		for ( String s : history ) {
			sb.append(nl + s);
		}
		// Create the history folder if necessary
		File f = new File(getHistoryFile());
		File folder = f.getParentFile();
		if ( !folder.exists() ) {
			if ( !folder.mkdirs() ) {
				// Unable to make folder
				return;
			}
		}
		try {
			//Message.printStatus(1, "", "Writing history: " + sb );
			IOUtil.writeFile ( f.getPath(), sb.toString() );
		}
		catch ( Exception e ) {
			// Absorb exception for now
		}
	}
	finally {
		// Make sure to do the following so don't lock up
		this.historyBeingWritten = false;
	}
}

}