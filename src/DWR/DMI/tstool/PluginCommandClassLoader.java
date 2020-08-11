// PluginCommandClassLoader - Load plugin command classes.

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
		String routine = getClass().getSimpleName() + ".loadCommandClasses";
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
				for ( int iCommand = 1 ; ; ++iCommand ) {
					String commandClassToLoad = attributes.getValue("Command-Class" + iCommand);
					if ( commandClassToLoad == null ) {
						// No more command classes so break out of the loop
						Message.printStatus(2, routine,  "Read " + (iCommand - 1) + " MANIFEST entries for command classes.");
						break;
					}
					else {
						// The following will search the list of URLs that was provided to the constructor
						Message.printStatus(2, routine,  "Found MANIFEST entry for command class: " +
							commandClassToLoad.substring(0,(commandClassToLoad.length() - (("" + iCommand).length() - 1)) ));
						Message.printStatus(2, routine, "Trying to load command class \"" + commandClassToLoad + "\"");
						// This class is an instance of URLClassLoader so can run the super-class loadClass()
						Class<?> loadedClass = loadClass(commandClassToLoad);
						Message.printStatus(2, routine, "Loaded command class \"" + commandClassToLoad + "\"");
						pluginCommandList.add(loadedClass);
					}
				}
			}
			catch ( IOException ioe ) {
				Message.printWarning(3,routine,"Error loading plugin commands from \"" + pluginClassURLs[i] + "\"");
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