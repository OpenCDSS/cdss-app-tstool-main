package DWR.DMI.tstool;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.Enumeration;
import java.util.Vector;


import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.ListModel;

import RTi.Util.IO.CommandPhaseType;
import RTi.Util.IO.CommandStatus;
import RTi.Util.IO.CommandStatusProvider;
import RTi.Util.IO.CommandStatusType;

/**
 * Provides an overview of Problems associated with the
 * MessageModel. 
 * <p>
 * When a gutter marker is clicked, the associated
 * index in the list will be scrolled to and selected.
 */
public class OverviewGutter extends JComponent
{
  //private final static int MARKER_NONE = 0;
  private final static int MARKER_WARNING = 1;
  private final static int MARKER_FAILURE = 2;

  private final ListModel _dataModel;

  /**
 * @uml.property  name="_list"
 * @uml.associationEnd  multiplicity="(1 1)"
 */
private final JList        _list;

  /**
   * Creates an overview gutter for the specified data model & JList.
   * <p>
   * Problems associated with data model items are indicated visually by
   * colored markers drawn in the gutter.
   * 
   * @param dataModel
   * @param list
   */
  public OverviewGutter(ListModel dataModel, JList list)
    {
      _dataModel = dataModel;
      _list = list;

      // Detect mouse clicks on markers & ensure associated JList item is visible
      addMouseListener(new MouseAdapter()
      {
        public void mouseClicked(MouseEvent me)
          {
            int index = findError(me.getPoint());
            if (index >= 0)
              {
                _list.ensureIndexIsVisible(index);
                _list.setSelectedIndex(index);
              }
          }
      });
      
      // Display marker text when pointer is over a marker
      addMouseMotionListener(new MouseMotionAdapter()
        {
          public void mouseMoved(MouseEvent e) 
            {
              String markerText = "";
            int index = findError(e.getPoint());
            if( index > -1 && index < _dataModel.getSize())
              {
                markerText = getMarkerText(index);
              }
           ((JComponent)e.getComponent()).setToolTipText(markerText);
        }}
          );
      
      // listen to the model and update
      /*
      dataModel.addChangeListener(new ChangeListener()
      {
        public void stateChanged(ChangeEvent e)
          {
            repaint();
          }
      });
      */

      // really only the width matters
      setPreferredSize(new Dimension(12, 16));
      setToolTipText("");
    }

  /**
   * Returns the index of the error in the model from the specified 
   * position.
   * 
   * @param p point
   * @return model index
   */
  private int findError(Point p)
    {
      ErrorBarIterator it = new ErrorBarIterator();
      int idx = 0;
      while (it.y < getHeight())
        {
          if (p.y >= it.y && p.y < it.y + it.barHeight)
            {
              return idx;
            }
          it.next();
          idx++;
        }
      return -1;
    }

  protected void paintComponent(Graphics g)
    {
      super.paintComponent(g);
//      if (_list.getHeight() <= _list.getParent().getHeight())
//        {
//          return;
//        }
      if (_list.getModel().getSize() == 0) return;
      
      ErrorBarIterator it = new ErrorBarIterator();
     // int barHeight = (int) Math.max(1, it.barHeight);
      Color color = null;
      for (int i = 0; i < _dataModel.getSize(); i++)
        {
         // int level = Util.getMaximumLevel(_dataModel.getMessages(i));
         //
          int level = getMarker(i);
         // Color color = Util.getColor(level);
          //TODO set color
          if (level == 1){ color = Color.yellow;}
          if (level == 2){ color = Color.red;}
            
          if (color != null)
            {
              // System.out.println("index: " + i + " level: " + level);
              g.setColor(color);
              g.fillRect(2, (int)it.y,  getWidth()-4, 4);
              g.setColor(Color.BLACK);
              g.drawRect(2, (int) it.y, getWidth() - 4, 4);
            }
          it.next();
          }
    }
  
 /**
  * Returns text to be displayed in tool tip.
  * <p>
  * Called by ToolTipMgr to get tool tip text
  */
  public String getToolTipText(MouseEvent event)
    {
	   if (_dataModel.getSize() != 0)
	   {
      int index = findError(event.getPoint());
      
      try
      {
      CommandStatusProvider csp = (CommandStatusProvider)_dataModel.getElementAt(index);
      return CommandStatusProviderUtil.getCommandLogHTML(csp);
      }
      catch (ClassCastException e)
      {
       System.out.println("Item #:" + index +1 + " does not implement ComandStatusProvider interface"
    		   + "\n  item.toString(): " + _dataModel.getElementAt(index).toString()
    		   + "\n\n" + e);
       return new String("NotACommandStatusProvider");
      }
	   }
	   else
	   {
		   return new String("");
	   }
    }
    
  /**
   * 0 none
   * 1 warning
   * 2 error
   * @param index
   * @return
   */
  private int getMarker(int index)
    {
      int markerIndex= 0;
      CommandStatusProvider csp;

      Object o = _dataModel.getElementAt(index);

      if (o instanceof CommandStatusProvider)
        {
          csp = (CommandStatusProvider)o;
          markerIndex = CommandStatusProviderUtil.getHighestSeverity(csp);
        }
      return markerIndex;
    }

  private String getMarkerText(int index)
  {
    CommandStatusProvider csp;
    String markerText ="";
    Object o = _dataModel.getElementAt(index);

    if (o instanceof CommandStatusProvider)
      {
        csp = (CommandStatusProvider)o;
        CommandStatusProviderUtil.getCommandLogHTML(csp);
      }
    return markerText;
  }
  
  
  /**
   * Encapsulate layout logic.
   * 
   * @todo allow customization of minimum and maximum bar size.
   */
  class ErrorBarIterator
  {
    double barHeight;

    int    idx;

    double y;

    ErrorBarIterator()
      {
        int height = getHeight();
//        int rowHeight = (int) _list.getCellBounds(0, 0).getHeight();
//        int errorHeight = _dataModel.getSize() * rowHeight;
//        barHeight = rowHeight
//            * Math.min(height / (double) errorHeight, 1.);
        //TODO: dre -revisit
	if (_dataModel.getSize() > 0)
	{
        barHeight = height/_dataModel.getSize();
	}
	    else 
	{
	  barHeight = 10;
	}
      }

    void next()
      {
        y += barHeight;
      }
  } // eof class ErrorBarIterator
      
}

