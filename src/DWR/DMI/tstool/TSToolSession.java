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
Construct the instance.
*/
public TSToolSession ()
{
}

/**
Return the name of the TSTool history file.
*/
public String getHistoryFile ()
{
	String historyFile = System.getProperty("user.home") + File.separator + ".tstool\\command-file-history.txt";
	Message.printStatus(1,"","History file \"" + historyFile + "\"");
	return historyFile;
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
	// Add in the first position
	history.add(0, commandFile);
	// Remove any duplicates and over the maximum
	// Process from the back so that old duplicates are removed and recent access is always at the top of the list
	// TODO SAM 2014-12-17 use a TSTool configuration file property to set cap
	int max = 100;
	for ( int i = history.size() - 1; i >= 1; i-- ) {
		if ( i >= max ) {
			history.remove(i);
		}
		else if ( history.get(i).equals(commandFile) || history.get(i).equals("") ||
			history.get(i).startsWith("#")) {
			history.remove(i);
		}
	}
	Message.printStatus(1,"", "History length is " + history.size());
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
		// For now just swallow exception
		Message.printWarning(3,routine,e);
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
			if ( !folder.mkdir() ) {
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