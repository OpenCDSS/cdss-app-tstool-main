// EnsembleGraph_JDialog - Dialog to select/confirm options for Ensemble Graph, to hand off to normal graph components.

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
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import RTi.Util.GUI.JGUIUtil;
import RTi.Util.GUI.SimpleJButton;
import RTi.Util.GUI.SimpleJComboBox;
import RTi.Util.IO.PropList;
import RTi.Util.Time.YearType;

/**
Dialog to select/confirm options for Ensemble Graph, to hand off to normal graph components.
*/
@SuppressWarnings("serial")
public class EnsembleGraph_JDialog extends JDialog
implements ActionListener, ItemListener, KeyListener, WindowListener
{
	
private SimpleJButton cancel_JButton = null;
private SimpleJButton ok_JButton = null;

private JTextField referenceDate_JTextField = null;
private SimpleJComboBox outputYearType_JComboBox = null;
private JTextField statistics_JTextField = null;
private SimpleJComboBox graphTemplates_JComboBox = null;

private boolean ok = false; // Has the users pressed OK to close the dialog.

/**
Is there an error waiting to be resolved before closing dialog?
*/
private boolean errorWait = false;

/**
TSTool session information, to locate user configuration files.
*/
private TSToolSession session = null;

/**
 * Remember the settings for the dialog, so they can default when opened 2nd+ time.
 */
private static String savedOutputYearType = null;

/**
 * Remember the settings for the dialog, so they can default when opened 2nd+ time.
 */
private static String savedReferenceDate = null;

/**
 * Remember the settings for the dialog, so they can default when opened 2nd+ time.
 */
private static String savedStatistics = null;

/**
 * Remember the settings for the dialog, so they can default when opened 2nd+ time.
 */
private static String savedGraphTemplate = null;

/**
 * Whether the dialog is for time series (true) or ensemble (false) results.
 */
private boolean isDialogForTS = true;

/**
 * Prefix for UI state properties.
 */
private String propertyPrefix = "Results.TS.";

/**
TSTool_Options_JDialog constructor.
@param parent JFrame class instantiating this class.
@param session the TSTool session, which provides information about user files.
@param isDialogForTS if true then the dialog is being shown for time series results,
if false, the dialog is being shown for ensemble results.
@param configFilePathInstall path to the installation TSTool configuration file.
@param appPropList Properties from the application - a subset of all props that are specifically handled.
*/
public EnsembleGraph_JDialog ( TSTool_JFrame parent, TSToolSession session,
	boolean isDialogForTS, PropList appPropList )
{	super(parent, true);
	this.session = session;
	this.isDialogForTS = isDialogForTS;
	if ( isDialogForTS ) {
		this.propertyPrefix = "Results.TimeSeries.";
	}
	else {
		this.propertyPrefix = "Results.Ensembles.";
	}
	// TODO sam 2017-04-09 Evaluate whether need given that TSTool session properties are supported
	//this.appPropList = appPropList;
	initialize ();
}

/**
Responds to ActionEvents.
@param event ActionEvent object
*/
public void actionPerformed( ActionEvent event )
{	Object o = event.getSource();

	if ( o == cancel_JButton ) {
		response ( false );
	}
	else if ( o == ok_JButton ) {
		checkInput();
		if ( !errorWait ) {
			response ( true );
		}
	}
}

/**
Check the input.  If errors exist, warn the user and set the __error_wait flag
to true.  This should be called before response() is allowed to complete.
*/
private void checkInput ()
{	// TODO sam 2017-04-10 need to implement checks for statistics, reference date
}

/**
 * Get the output year type.
 */
public String getOutputYearType () {
	return outputYearType_JComboBox.getSelected();
}

/**
 * Get the reference date/time.
 */
public String getReferenceDate () {
	return referenceDate_JTextField.getText().trim();
}

/**
 * Get the statistics.
 */
public String getStatistics () {
	return statistics_JTextField.getText().trim();
}

/**
 * Get the template file.
 */
public String getGraphTemplate () {
	return graphTemplates_JComboBox.getSelected().trim();
}

/**
Instantiates the UI components.
*/
private void initialize ()
{	addWindowListener( this );

    Insets insetsTLBR = new Insets(2,2,2,2);
    
    TSToolSession session = TSToolSession.getInstance();

	GridBagLayout gbl = new GridBagLayout();
	JPanel main_JPanel = new JPanel();
	main_JPanel.setLayout( gbl );
	getContentPane().add ( "North", main_JPanel );
	int y = -1;

	if ( this.isDialogForTS ) {
	    JGUIUtil.addComponent(main_JPanel, new JLabel ( "Specify properties to control converting the time series to an ensemble before creating the graph."),
	        0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
	    JGUIUtil.addComponent(main_JPanel, new JLabel (
	    	"The time series can optionally be used to compute statistics similar to the NewStatisticTimeSeriesFromEnsemble() command,"),
	        0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
	    JGUIUtil.addComponent(main_JPanel, new JLabel ( "with statistics specified as Mean, Median, Max, Min, etc. (comma-separated)."),
	        0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
	    JGUIUtil.addComponent(main_JPanel, new JLabel ( "The template file, if specified, provides properties for the graph, such as title and legend format."),
	        0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
	    JGUIUtil.addComponent(main_JPanel, new JLabel ( "If the template file specifies a TemplateProcessCommandFile property, "
	    	+ "then commands are used to preprocss the single input time series before display."),
	        0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
	    JGUIUtil.addComponent(main_JPanel, new JLabel ( "For example, preprocessing can replace the need to specify statistics here."),
	        0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
	    JGUIUtil.addComponent(main_JPanel, new JLabel ( "See also the \"Graph with template\" button in the lower-right corner of the results area,"
	    	+ " which can be used for single-click graphing."),
		    0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
	}
	else {
	    JGUIUtil.addComponent(main_JPanel, new JLabel ( "Specify properties to control processing the ensemble before creating the graph."),
	        0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
	    JGUIUtil.addComponent(main_JPanel, new JLabel (
	    	"The time series in the ensemble can optionally be used to compute statistics similar to the NewStatisticTimeSeriesFromEnsemble() command,"),
	        0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
	    JGUIUtil.addComponent(main_JPanel, new JLabel ( "with statistics specified as Mean, Median, Max, Min, etc. (comma-separated)."),
	        0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
	    JGUIUtil.addComponent(main_JPanel, new JLabel ( "The template file, if specified, provides properties for the graph, such as title and legend format."),
	        0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
	    JGUIUtil.addComponent(main_JPanel, new JLabel ( "If the template file specifies a TemplateProcessCommandFile property, "
	    	+ "then commands are used to preprocss the ensemble time series before display."),
	        0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
	    JGUIUtil.addComponent(main_JPanel, new JLabel ( "For example, preprocessing can replace the need to specify statistics here."),
	        0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
	    JGUIUtil.addComponent(main_JPanel, new JLabel ( "See also the \"Graph with template\" button in the lower-right corner of the results area,"
	    	+ " which can be used for single-click graphing."),
		    0, ++y, 7, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
	}

    JGUIUtil.addComponent(main_JPanel, new JSeparator (SwingConstants.HORIZONTAL),
        0, ++y, 7, 1, 1, 0, insetsTLBR, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER);

    if ( this.isDialogForTS ) { // Only for time series since used in the TS->Ensemble process
	    JGUIUtil.addComponent(main_JPanel, new JLabel ( "Output year type:" ), 
			0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
		outputYearType_JComboBox = new SimpleJComboBox ( false );
		// Only include types that have been tested for all output.  More specific types may be included in
		// some commands where local handling is enabled.
		outputYearType_JComboBox.add ( "" );
	    outputYearType_JComboBox.add ( "" + YearType.CALENDAR );
	    outputYearType_JComboBox.add ( "" + YearType.NOV_TO_OCT );
	    outputYearType_JComboBox.add ( "" + YearType.WATER );
	    if ( savedOutputYearType == null ) {
	    	// See if the year type was in the TSTool state
	    	savedOutputYearType = session.getUIStateProperty(this.propertyPrefix + "EnsembleGraph.OutputYearType");
	    }
	    if ( savedOutputYearType != null ) {
	    	try {
	    		outputYearType_JComboBox.select(savedOutputYearType);
	    	}
	    	catch ( Exception e ) {
	    		// OK, default
	    	}
	    }
		outputYearType_JComboBox.addItemListener ( this );
	        JGUIUtil.addComponent(main_JPanel, outputYearType_JComboBox,
			1, y, 2, 1, 1, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
	    JGUIUtil.addComponent(main_JPanel, new JLabel ( "Optional - output year type for ensemble (default=" + YearType.CALENDAR + ")."),
	        3, y, 2, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
    }

    if ( this.isDialogForTS ) { // Only for time series since used in the TS->Ensemble process
	    JGUIUtil.addComponent(main_JPanel, new JLabel (	"Reference date:" ), 
			0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
		referenceDate_JTextField = new JTextField ( 20 );
		referenceDate_JTextField.setToolTipText("Date at which to shift all ensemble traces to align start, YYYY-MM-DD for daily data.");
	    if ( savedReferenceDate == null ) {
	    	// See if the reference date was in the TSTool state
	    	savedReferenceDate = session.getUIStateProperty(this.propertyPrefix + "EnsembleGraph.ReferenceDate");
	    }
	    if ( savedReferenceDate != null ) {
	    	referenceDate_JTextField.setText(savedReferenceDate);
	    }
		referenceDate_JTextField.addKeyListener(this);
		JGUIUtil.addComponent(main_JPanel, referenceDate_JTextField,
			1, y, 2, 1, 1, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
	    JGUIUtil.addComponent(main_JPanel, new JLabel( "Optional (default=starting day for year type, for current date)."), 
	        3, y, 4, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
    }

    // Option to calculate statistics is available for time series and ensemble processing
    JGUIUtil.addComponent(main_JPanel, new JLabel (	"Statistics:" ), 
		0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	statistics_JTextField = new JTextField ( 20 );
	statistics_JTextField.setToolTipText("Statistics to compute as time series, comma-separated:  Mean, Median, Max, Min.");
    if ( savedStatistics == null ) {
    	// See if the statistics property was in the TSTool state
    	savedStatistics = session.getUIStateProperty(this.propertyPrefix + "EnsembleGraph.Statistics");
    }
    if ( savedStatistics != null ) {
    	statistics_JTextField.setText(savedStatistics);
    }
	statistics_JTextField.addKeyListener(this);
	JGUIUtil.addComponent(main_JPanel, statistics_JTextField,
		1, y, 2, 1, 1, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
    JGUIUtil.addComponent(main_JPanel, new JLabel( "Optional (default=list of statistics to compute)."), 
        3, y, 4, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);

    // Option to to use graph template is available for time series and ensemble processing
    JGUIUtil.addComponent(main_JPanel, new JLabel ( "Graph template:" ), 
    	0, ++y, 1, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.EAST);
	graphTemplates_JComboBox = new SimpleJComboBox(false);
	List<File> templateFileList = this.session.getGraphTemplateFileList();
	List<String> templateFileList2 = new ArrayList<String>();
	templateFileList2.add(""); // Default is not to use a template
	for ( File f : templateFileList ) {
		templateFileList2.add(f.getName());
	}
	graphTemplates_JComboBox.setMaximumRowCount ( 20 );
	graphTemplates_JComboBox.setData(templateFileList2);
    if ( savedGraphTemplate == null ) {
    	// See if the graph template was in the TSTool state
    	savedGraphTemplate = session.getUIStateProperty(this.propertyPrefix + "EnsembleGraph.GraphTemplate");
    }
    if ( savedGraphTemplate != null ) {
    	try {
    		graphTemplates_JComboBox.select(savedGraphTemplate);
    	}
    	catch ( Exception e ) {
    		// OK, maybe template files renamed manually
    	}
    }
	graphTemplates_JComboBox.addItemListener( this );
	JGUIUtil.addComponent(main_JPanel, graphTemplates_JComboBox, 
		1, y, 2, 1, 1, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
    JGUIUtil.addComponent(main_JPanel, new JLabel( "Optional (default=use default graph properties)."), 
        3, y, 4, 1, 0, 0, insetsTLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);

    JGUIUtil.addComponent(main_JPanel, new JSeparator (SwingConstants.HORIZONTAL),
        0, ++y, 7, 1, 1, 0, insetsTLBR, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER);

	// South Panel: North
	JPanel button_JPanel = new JPanel();
	button_JPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
	getContentPane().add ( "South", button_JPanel );
    //JGUIUtil.addComponent(main_JPanel, button_JPanel,
	//	0, ++y, 1, 1, 1, 0, insetsTLBR, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER);

	cancel_JButton = new SimpleJButton("Cancel", this);
	button_JPanel.add ( cancel_JButton );
	ok_JButton = new SimpleJButton("OK", this);
	button_JPanel.add ( ok_JButton );

	setTitle ( JGUIUtil.getAppNameForWindows() + " - Ensemble Graph Properties" );
	setResizable ( false );
    pack();
    JGUIUtil.center( this );
    super.setVisible( true );
}

/**
Handle ItemEvent events.
@param e ItemEvent to handle.
*/
public void itemStateChanged ( ItemEvent e )
{	//refresh();
}

/**
Respond to KeyEvents.
*/
public void keyPressed ( KeyEvent event )
{	int code = event.getKeyCode();

	if ( code == KeyEvent.VK_ENTER ) {
		checkInput();
		if ( !errorWait ) {
			response ( true );
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
Indicate if the user pressed OK (cancel otherwise).
@return true if the edits were committed, false if the user canceled.
*/
public boolean ok ()
{   return this.ok;
}

/**
Close and return to parent UI component.
*/
public void response ( boolean ok )
{	this.ok = ok;
	if ( ok ) {
		// Save the choices that were selected
		if ( this.isDialogForTS ) {
			savedOutputYearType = outputYearType_JComboBox.getSelected();
			savedReferenceDate = referenceDate_JTextField.getText();
		}
		savedStatistics = statistics_JTextField.getText();
		savedGraphTemplate = graphTemplates_JComboBox.getSelected();
		// Also save in TSTool session
		// TODO sam 2017-04-09 decide whether to only rely on TSTool session
		TSToolSession session = TSToolSession.getInstance();
		if ( this.isDialogForTS ) {
			// The following are only used for TS processing
			session.setUIStateProperty(this.propertyPrefix + "EnsembleGraph.OutputYearType",savedOutputYearType);
			session.setUIStateProperty(this.propertyPrefix + "EnsembleGraph.ReferenceDate",savedReferenceDate);
		}
		session.setUIStateProperty(this.propertyPrefix + "EnsembleGraph.Statistics",savedStatistics);
		session.setUIStateProperty(this.propertyPrefix + "EnsembleGraph.GraphTemplate",savedGraphTemplate);
	}
	setVisible( false );
	dispose();
}

/**
Responds to WindowEvents.
@param event WindowEvent object
*/
public void windowClosing( WindowEvent event )
{	response ( false );
}

public void windowActivated( WindowEvent evt ){;}
public void windowClosed( WindowEvent evt ){;}
public void windowDeactivated( WindowEvent evt ){;}
public void windowDeiconified( WindowEvent evt ){;}
public void windowIconified( WindowEvent evt ){;}
public void windowOpened( WindowEvent evt ){;}

}
