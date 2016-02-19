package DWR.DMI.tstool;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import RTi.Util.IO.IOUtil;
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
Create the datastores folder if necessary.
@return true if datastores folder exists and is writeable, false otherwise.
*/
public boolean createDatastoreFolder () {
	String datastoresFolder = getDatastoreFolder();
	// Do not allow datastores folder to be created under Linux root but allow TSTool to run
	if ( datastoresFolder.equals("/") ) {
		return false;
	}
	File f = new File(datastoresFolder);
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
Return the name of the log file folder.
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
Return the name of the user folder for the operating system.
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
{	String routine = getClass().getSimpleName() + ".readHistory";
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