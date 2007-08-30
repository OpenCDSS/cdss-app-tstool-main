
package DWR.DMI.tstool;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import RTi.Util.IO.CommandStatusProvider;

/**
 * Provides a JList with visual problem markers.
 * 
 * @author dre
 */

public class AnnotatedList extends JPanel
{
  private static final long serialVersionUID = 1L;
  /** GUI component displaying problem overview of entire list*/
  private OverviewGutter _gutter;
  /** Extended JList */
  private JList _jList;
  /** JScrollPane containing JList */
  private JScrollPane _jScrollPane; 
  
  ListModel _dataModel;
  
  public AnnotatedList()
  {
	  initialize();
  }
  /**
   * Creates a component for viewing a list with line numbers &
   * markers.
   * 
   * The compound component consists of a JScrollPane containing
   * a JList, ProblemGutter & OverviewGutter.
   * 
   * Markers can only be displayed for data model items implementing the
   * <code>CommandStatusProvider</code> interface.
   * 
   * @param dataModel - data model to be displayed
   * @param msgModel
   */
  public AnnotatedList(ListModel dataModel)
  {
	  _dataModel = dataModel;
	  initialize();
	  _jList.setModel(dataModel);
	  
	  dataModel.addListDataListener(new MyListDataListener());
  }
  private void initialize()
  {
   setLayout(new BorderLayout());
   // Override JList's getToolTipText method to return tooltip
    _jList = new JList()
    {
      static final long serialVersionUID = 1L;

      /**
       * Returns text to be displayed in tool tip.
       * <p>
       * Called by ToolTipMgr to get tool tip text
       */
       public String getToolTipText(MouseEvent event)
       {
   	     if (getModel().getSize() != 0)
    	   {
         // Get item index
         int index = locationToIndex(event.getPoint());

         // Get item

         Object item = getModel().getElementAt(index);
          return CommandStatusProviderUtil.getCommandLogHTML(item);
         }
         else
         {
        	 return new String("");
         }
       }
    };
  
    _jList.setToolTipText("myTip");
    // Use a Border that displays markers
    //_jList.setBorder( new LineNumberBorder(_jList, this));
    
    _jScrollPane = new JScrollPane(_jList);
    add(_jScrollPane, BorderLayout.CENTER);
    //
    _jScrollPane.getViewport().addChangeListener(
      new ChangeListener() 
      {
        public void stateChanged(ChangeEvent e)
        {
        e.getSource();
        _jList.repaint();
        }
      });
 
    ProblemGutter ng = new ProblemGutter(_jList, _jScrollPane);
    _jScrollPane.setRowHeaderView(ng);
    
    _gutter = new OverviewGutter(_dataModel, _jList);
    add(_gutter, BorderLayout.EAST);
  }
  
  /**
   * Returns JList
   * @return
   */
 public JList getJList()
 {
	 return _jList;
 }
} // AnnotatedList
class MyListDataListener implements ListDataListener {
    public void contentsChanged(ListDataEvent e) {
        System.out.println("contentsChanged: " + e.getIndex0() +
	           ", " + e.getIndex1() + e.getIndex0()); 

    }
    public void intervalAdded(ListDataEvent e) {
        System.out.println("intervalAdded: " + e.getIndex0() +
	           ", " + e.getIndex1());
        Object o =((ListModel)e.getSource()).getElementAt(e.getIndex0());
        if (o instanceof CommandStatusProvider)
        	System.out.println("   CommandStatusProvider");
        
    }
    public void intervalRemoved(ListDataEvent e) {
        System.out.println("intervalRemoved: " + e.getIndex0() +
	           ", " + e.getIndex1());
    }
}