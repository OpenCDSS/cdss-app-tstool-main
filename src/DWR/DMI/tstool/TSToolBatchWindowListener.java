// TSToolBatchWindowListener - class that listens for WindowEvents and is used when TSTool is run in batch mode

/* NoticeStart

TSTool
TSTool is a part of Colorado's Decision Support Systems (CDSS)
Copyright (C) 1994-2023 Colorado Department of Natural Resources

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

import java.awt.Component;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import RTi.GRTS.TSViewJFrame;

/**
 * This class listens for WindowEvents and is used when TSTool is run in batch mode.
 * Its only purpose is to gracefully handle close-down of the application when no
 * main GUI is used and a plot window is closed (application needs to stay open to show
 * the plot but close completely when the plot window is closed).
 */
public class TSToolBatchWindowListener implements WindowListener {
    /**
     * Constructor.
     */
    public TSToolBatchWindowListener () {
    }

    /**
    Needed for WindowListener interface.  Currently does nothing.
    */
    public void windowActivated ( WindowEvent e ) {
    }

    /**
    This class is listening for GeoViewFrame closing so it can gracefully handle.
    */
    public void windowClosed ( WindowEvent e ) {
       Component c = e.getComponent();
        if ( c instanceof TSViewJFrame ) {
            // Running in hidden mode and the TSViewJFrame has closed so close the application.
            TSToolMain.quitProgram(0);
        }
    }

    /**
    Need to monitor TSTool GUI is closing to shut it down gracefully.
    */
    public void windowClosing ( WindowEvent e ) {
    }

    /**
    Needed for WindowListener interface.  Currently does nothing.
    */
    public void windowDeactivated ( WindowEvent e ) {
    }

    /**
    Needed for WindowListener interface.  Currently does nothing.
    */
    public void windowDeiconified ( WindowEvent e ) {
    }

    /**
    Needed for WindowListener interface.  Currently does nothing.
    */
    public void windowIconified ( WindowEvent e ) {
    }

    /**
    Needed for WindowListener interface.  Currently does nothing.
    */
    public void windowOpened ( WindowEvent e ) {
    }
}