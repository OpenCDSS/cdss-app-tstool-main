package DWR.DMI.tstool;

import java.util.Enumeration;
import java.util.Vector;

import RTi.Util.IO.CommandPhaseType;
import RTi.Util.IO.CommandStatus;
import RTi.Util.IO.CommandStatusProvider;
import RTi.Util.IO.CommandStatusType;
import RTi.Util.IO.CommandStatusUtil;

/**
 * Provides convenience methods for working with Command Status.
 * 
 * @author dre
 */
public class CommandStatusProviderUtil
{
  public static String getCommandLogHTML(Object o)
  {
    if ( o instanceof CommandStatusProvider)
      {
        CommandStatusProvider csp = (CommandStatusProvider)o;
        String toolTip = CommandStatusProviderUtil.getCommandLogHTML(csp);
        return toolTip;
      }
    else
      return null;
  }
  
  /**
   * Returns the command log records ready for display as html.
   * 
   * @param csp command status provider
   * @return concatenated log records as text
   */
  public static String getCommandLogHTML(CommandStatusProvider csp)
    {
      return HTMLUtil.text2html(getCommandLogText(csp));
    }
  
  
  /**
   * Returns the command log records ready for display as text
   * @param csp command status provider
   * 
   * @return concatenated log records as text
   */
  public static String getCommandLogText(CommandStatusProvider csp)
    {
      String markerText ="";

      if (csp != null)
        {
          CommandStatus cs = csp.getCommandStatus();

          if (cs != null)
            {
//              if (CommandStatusType.WARNING.equals(
//                  cs.getCommandStatus(CommandPhaseType.INITIALIZATION))
//              )
//                {
//                  Vector v = cs.getCommandLog(CommandPhaseType.INITIALIZATION);
//                  Enumeration e = v.elements();
//                  while (e.hasMoreElements())
//                    {
//                      markerText = markerText + e.nextElement().toString();
//                    }
//                }
              Vector v = cs.getCommandLog(CommandPhaseType.INITIALIZATION);
            Enumeration e = v.elements();
            while (e.hasMoreElements())
              {
                markerText = markerText + e.nextElement().toString();
              }
            v = cs.getCommandLog(CommandPhaseType.DISCOVERY);
            e = v.elements();
            while (e.hasMoreElements())
              {
                markerText = markerText + e.nextElement().toString();
              }
            v = cs.getCommandLog(CommandPhaseType.RUN);
            e = v.elements();
            while (e.hasMoreElements())
              {
                markerText = markerText + e.nextElement().toString();
              }
            }

        }
      return markerText;

    } // eof getCommandLogText
  
 
  /**
   * Returns the highest status severity of all phases.
   * 
   * @param csp command status provider @see CommandStatusType
   * @return highest severity 
   * <ol> <li> 0 - UNKNOWN</li>
   * <ol> <li> 1 - WARNING</li>
   * <ol> <li> 2 - FAILURE</li>
   * @see CommandStatusType
   */
  public static int getHighestSeverity(CommandStatusProvider csp)
    {
      CommandStatus cs = csp.getCommandStatus();
      int status = CommandStatusType.UNKNOWN.getPriority();
      
      if (cs != null)
        {
          int phaseStatus = cs.getCommandStatus(CommandPhaseType.INITIALIZATION).getPriority();
          if ( phaseStatus> status)
            {
              status = phaseStatus;
            }
          phaseStatus = cs.getCommandStatus(CommandPhaseType.DISCOVERY).getPriority();
          if ( phaseStatus> status)
            {
              status = phaseStatus;
            }
          phaseStatus = cs.getCommandStatus(CommandPhaseType.RUN).getPriority();
          if ( phaseStatus> status)
            {
              status = phaseStatus;
            }
        }
      return status;
    } // eof getHighestSeverity()
}

