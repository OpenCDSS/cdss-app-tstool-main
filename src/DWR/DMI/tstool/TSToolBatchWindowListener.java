package DWR.DMI.tstool;

import java.awt.Component;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import RTi.GRTS.TSViewJFrame;

/**
 * This class listens for WindowEvents and is used when TSTool is run in batch mode.
 * Its only purpose is to gracefully handle close-down of the application when no
 * main GUI is used and a plot window is closed (application needs to stay open to show
 * the plot but close completely when the plot window is closed).
 * @author sam
 *
 */
public class TSToolBatchWindowListener implements WindowListener
{
    /**
     * Constructor.
     */
    public TSToolBatchWindowListener ()
    {
    }
    
    /**
    Needed for WindowListener interface.  Currently does nothing.
    */
    public void windowActivated ( WindowEvent e )
    {
    }

    /**
    This class is listening for GeoViewFrame closing so it can gracefully handle.
    */
    public void windowClosed ( WindowEvent e )
    {   Component c = e.getComponent();
        if ( c instanceof TSViewJFrame ) {
            // Running in hidden mode and the TSViewJFrame has closed so close the application...
            TSToolMain.quitProgram(0);
        }
    }

    /**
    Need to monitor TSTool GUI is closing to shut it down gracefully.
    */
    public void windowClosing ( WindowEvent e )
    {
    }

    /**
    Needed for WindowListener interface.  Currently does nothing.
    */
    public void windowDeactivated ( WindowEvent e )
    {
    }

    /**
    Needed for WindowListener interface.  Currently does nothing.
    */
    public void windowDeiconified ( WindowEvent e )
    {
    }

    /**
    Needed for WindowListener interface.  Currently does nothing.
    */
    public void windowIconified ( WindowEvent e )
    {
    }

    /**
    Needed for WindowListener interface.  Currently does nothing.
    */
    public void windowOpened ( WindowEvent e )
    {
    }
}
