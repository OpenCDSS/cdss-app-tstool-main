// ----------------------------------------------------------------------------
// comment_JDialog - editor for comments
// ----------------------------------------------------------------------------
// Copyright:	See the COPYRIGHT file.
// ----------------------------------------------------------------------------
// History: 
//
// 19 Dec 2000	Steven A. Malers, RTi	Initial version.
// 2002-03-30	SAM, RTi		Add instructions to help user.
// 2003-12-03	SAM, RTi		Update to Swing.
// 2004-02-22	SAM, RTi		* Fix bug where new comments were
//					  resulting in an extra line.
//					* Fix bug where cancel was returning
//					  non-null comments.
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

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import java.util.Vector;

import RTi.Util.GUI.JGUIUtil;
import RTi.Util.GUI.SimpleJButton;
import RTi.Util.String.StringUtil;

public class comment_JDialog extends JDialog
implements ActionListener, KeyListener, WindowListener
{
private SimpleJButton	__cancel_JButton = null,// Cancel Button
			__ok_JButton = null;	// Ok Button
private Vector		__command_Vector = null;// Command(s) as Vector of
						// String
private JTextArea	__command_JTextArea = null;// Command as JTextArea
private boolean		__first_time = true;

/**
comment_JDialog constructor.
@param parent JFrame class instantiating this class.
@param command Command to parse.
@param tsids Time series identifiers for time series available (ignored).
*/
public comment_JDialog ( JFrame parent, Vector command, Vector tsids )
{	super(parent, true);
	initialize ( parent, "Edit # Comments", command, tsids );
}

/**
Responds to ActionEvents.
@param event ActionEvent object
*/
public void actionPerformed( ActionEvent event )
{	Object o = event.getSource();

	if ( o == __cancel_JButton ) {
		__command_Vector = null;
		response ( 0 );
	}
	else if ( o == __ok_JButton ) {
		refresh();
		response ( 1 );
	}
}

/**
Convert text area to comments.
*/
private void checkComments ()
{	// Reset the command from the fields...
	__command_Vector = JGUIUtil.toVector( __command_JTextArea );
	// Make sure there is a # character at the front of each line...
	int size = 0;
	if ( __command_Vector != null ) {
		size = __command_Vector.size();
	}
	String s = null;
	for ( int i = 0; i < size; i++ ) {
		s = ((String)__command_Vector.elementAt(i)).trim();
		if ( !s.startsWith("#") ) {
			// Replace with a new string that has the comment
			// character...
			__command_Vector.removeElementAt(i);
			__command_Vector.insertElementAt(("# " + s), i);
		}
	}
}

/**
Free memory for garbage collection.
*/
protected void finalize ()
throws Throwable
{	__cancel_JButton = null;
	__command_JTextArea = null;
	__command_Vector = null;
	__ok_JButton = null;
	super.finalize ();
}

/**
Return the text for the command.
@return the text for the command or null if there is a problem with the command.
*/
public Vector getText ()
{	if ( __command_Vector == null ) {
		// Indicates a cancel...
		return null;
	}
	// Else, OK to process the comments...
	checkComments ();
	if (	(__command_Vector == null) ||
		(__command_Vector.size() == 0) ||
		((String)__command_Vector.elementAt(0)).equals("") ) {
		__command_Vector = null;
	}
	return __command_Vector;
}

/**
Instantiates the GUI components.
@param parent JFrame class instantiating this class.
@param title Dialog title.
@param command Vector of String containing the command.
@param tsids Time series identifiers from
TSEngine.getTSIdentifiersFromCommands() - ignored.
*/
private void initialize ( JFrame parent, String title, Vector command,
			Vector tsids )
{	__command_Vector = command;

	addWindowListener( this );

        Insets insetsTLBR = new Insets(7,2,7,2);

	// Main panel...

	JPanel main_JPanel = new JPanel();
	main_JPanel.setLayout( new GridBagLayout() );
	getContentPane().add ( "North", main_JPanel );
	int y = 0;

	// Main contents...

	// Now add the buttons...

        JGUIUtil.addComponent(main_JPanel, new JLabel (
		"Enter one or more comments (leading # will be added" +
		" automatically if not shown)." ), 
		0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);

        JGUIUtil.addComponent(main_JPanel, new JLabel ( "Comments:" ), 
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);

	__command_JTextArea = new JTextArea ( 10, 80 );
	__command_JTextArea.setEditable ( true );
	JGUIUtil.addComponent(main_JPanel, new JScrollPane(__command_JTextArea),
		1, y, 6, 1, 1, 1, insetsTLBR, GridBagConstraints.BOTH, GridBagConstraints.CENTER);

	// Refresh the contents...
	refresh ();

	// South Panel: North
	JPanel button_JPanel = new JPanel();
	button_JPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JGUIUtil.addComponent(main_JPanel, button_JPanel, 
		0, ++y, 8, 1, 1, 0, insetsTLBR, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER);

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
        super.setVisible( true );
}

/**
Respond to KeyEvents.
*/
public void keyPressed ( KeyEvent event )
{	// Want user to press OK to continue (not enter because newlines
	// may be allowed).
}

public void keyReleased ( KeyEvent event )
{	// Nothing to do	
}

public void keyTyped ( KeyEvent event )
{
}

/**
Refresh the command from the other text field contents.
*/
private void refresh ()
{	if ( __first_time ) {
		__first_time = false;
		// Fill the text area with the command text.  If a Vector with
		// one blank string, don't do the following because it results
		// in a blank line and the user must back up to edit the blank.
		if (	(__command_Vector != null) &&
			(__command_Vector.size() > 0) &&
			(((String)__command_Vector.elementAt(0)).length() > 0)){
			String text = StringUtil.toString(__command_Vector,
				System.getProperty("line.separator") );
			if ( text.length() > 0 ) {
				__command_JTextArea.setText ( text );
			}
		}
	}
}

/**
Return the time series command as a Vector of String.
@param status 0 to cancel, 1 is OK.
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
		checkComments ();
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

} // end comment_JDialog
