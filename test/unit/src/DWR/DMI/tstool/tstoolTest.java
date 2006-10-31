//  2006-10-30  KAT, RTi    Added this test class to be able to
//                          setup a testing framework for TSTool
//                          Tests that are more applicable should
//                          be added in the future
//                  


package DWR.DMI.tstool;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.framework.Test;
import DWR.DMI.tstool.tstool;

public class tstoolTest extends TestCase {

    public tstoolTest(String testname)
    {
        super(testname);
    }

    public tstoolTest()
    {
        
    }

    // Set up some properties and to test
    protected void setUp()
    {
       
    }
    
    /*******************************************************************
    This method just tests the PrintVersion method of the tstool main 
    class.    
    ********************************************************************/
    public void testisServer()
    {
       boolean exp_val = false;
       assertEquals(false, tstool.isServer());
    }
    
    public void testgetJFrame()
    {
       tstool.getJFrame();
    }
    
    public void testgetPropValue()
    {
       assertNull(tstool.getPropValue("blah"));  
    }
    
    // Quick Unit test suite
    public static Test suite()
    {
        TestSuite suite = new TestSuite();
        suite.addTest((new tstoolTest("testisServer")));
        suite.addTest((new tstoolTest("testgetJFrame")));
        suite.addTest((new tstoolTest("testgetPropValue")));
        return suite;
    }
    
}

