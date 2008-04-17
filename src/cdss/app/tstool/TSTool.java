package cdss.app.tstool;

import javax.swing.JApplet;

import DWR.DMI.tstool.TSToolMain;

/**
Main (application startup) class for CDSS version of TSTool.  This class will start the TSTool GUI
or run the TSCommandProcessor in batch mode with a command file.
*/
public class TSTool extends JApplet
{

/**
Instantiates the application instance as an applet.
*/
public void init()
{	// The init() method is not static so must declare an instance.
    TSToolMain tstool = new TSToolMain();
    tstool.init();
}
    
/**
Start the main application instance.
@param args Command line arguments.
*/
public static void main ( String args[] )
{	TSToolMain.main(args);
}

}
