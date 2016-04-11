package DWR.DMI.tstool;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;

import RTi.Util.Message.Message;

/**
 * Load plugin command classes.  This loader loads the command class as an entry point into
 * the plugin commands integrated with TSTool.  Once the commands have been loaded, other
 * classes will be loaded, such as the editor dialog and all the underlying classes that
 * are needed to run the command, such as database interface.
 * See:  http://docs.oracle.com/javase/tutorial/ext/basics/load.html
 * @author sam
 *
 */
public class PluginCommandClassLoader extends URLClassLoader {
	/**
	 * Construct the class loader with a list of jar files that are candidates to load
	 */
	public PluginCommandClassLoader ( URL [] commandJarList ) {
		// Using URLClassLoader for the base class should result in the parent (application) class
		// loader being used for for any other class loads, then this class.
		super ( commandJarList );
	}
	
	/**
	 * Load the command classes found in the jar file.
	 */
	public List<Class> loadCommandClasses () throws ClassNotFoundException {
		String routine = "loadCommandClasses";
		// Get all of the URLs that were specified to the loader
		URL [] pluginClassURLs = getURLs();
		// Plugin command classes that are loaded
		List<Class> pluginCommandList = new ArrayList<Class>();
		// Loop through all of the URLs
		for ( int i = 0; i < pluginClassURLs.length; i++ ) {
			JarInputStream jarStream = null;
			try {
				// Open the META-INF/MANIFEST.MF file and get the property Command-Class, which is what needs to be loaded
				jarStream = new JarInputStream(pluginClassURLs[i].openStream());
				Manifest manifest = jarStream.getManifest();
				Attributes attributes = manifest.getMainAttributes();
				String commandClassToLoad = attributes.getValue("Command-Class");
				if ( commandClassToLoad == null ) {
					Message.printWarning(3, routine,
						"Cannot find Command-Class property in jar file MANIFEST.  Cannot load command class \"" + commandClassToLoad + "\"");
				}
				else {
					// The following will search the list of URLs that was provided to the constructor
					Message.printStatus(2, routine, "Trying to load command class \"" + commandClassToLoad + "\"");
					// This class is an instance of URLClassLoader so can run the super-class loadClass()
					Class<?> loadedClass = loadClass(commandClassToLoad);
					Message.printStatus(2, routine, "Loaded command class \"" + commandClassToLoad + "\"");
					pluginCommandList.add(loadedClass);
				}
			}
			catch ( IOException ioe ) {
				Message.printWarning(3,routine,"Error loading plugin command from \"" + pluginClassURLs[i] + "\"");
			}
			finally {
				if ( jarStream != null ) {
					try {
						jarStream.close();
					}
					catch ( IOException e ) {
						// Ignore - should not happen
					}
				}
			}
		}
		return pluginCommandList;
	}
}