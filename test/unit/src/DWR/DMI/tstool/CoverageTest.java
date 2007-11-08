package DWR.DMI.tstool;

import java.io.File;
import java.util.ArrayList;

import junit.framework.TestCase;

public class CoverageTest extends TestCase 
{
	ArrayList tests;

	protected void setUp() throws Exception 
	{
//		tests = new ArrayList();
//		visitAllFiles(new File("test\\regression\\commands"));
	}

	public void testCoverage()
	{
		//	make sure to run in batch mode
//		String home = "test\\operational\\CDSS";
//	    IOUtil.isBatch(true);
//	    IOUtil.setProgramWorkingDir(home);
//	    IOUtil.setApplicationHomeDir(home);
//	    JGUIUtil.setLastFileDialogDirectory(home);
//	       
//		for ( int i = 0; i < tests.size(); i++ )
//		{
//			System.out.println( i + ": " + tests.get(i).toString() );
//			tstool.main( new String [] { "-home", home, "-commands",
//				tests.get(i).toString().trim() } );
//		}
	}
	
	public void visitAllFiles(File dir) {
		
		if (dir.isDirectory()) 
		{
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) 
			{
				visitAllFiles(new File(dir, children[i]));
			}
		}
		else 
		{
			//add to list
			if(dir.toString().endsWith(".TSTool"))
				tests.add(dir.toString());
		}
	}	
	
}
