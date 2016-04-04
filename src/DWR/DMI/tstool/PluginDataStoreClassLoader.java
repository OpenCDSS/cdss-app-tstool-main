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
 * Load plugin datastore classes.  This loader loads the datastore class as an entry point into
 * the plugin datastores integrated with TSTool.  Once the datastores have been loaded, other
 * classes will be loaded, such as low-level package to read/write the database.
 * @author sam
 *
 */
public class PluginDataStoreClassLoader extends URLClassLoader {
	/**
	 * Construct the class loader with a list of jar files that are candidates to load
	 */
	public PluginDataStoreClassLoader ( URL [] dataStoreJarList ) {
		// Using URLClassLoader for the base class should result in the parent (application) class
		// loader being used for for any other class loads, then this class.
		super ( dataStoreJarList );
	}
	
	/**
	 * Load the datastore classes found in the jar file.
	 */
	public List<Class> loadDataStoreClasses () throws ClassNotFoundException {
		String routine = "loadDataStoreClasses";
		// Get all of the URLs that were specified to the loader
		URL [] pluginClassURLs = getURLs();
		// Plugin datastore classes that are loaded
		List<Class> pluginDataStoreList = new ArrayList<Class>();
		// Loop through all of the URLs
		for ( int i = 0; i < pluginClassURLs.length; i++ ) {
			JarInputStream jarStream = null;
			try {
				// Open the META-INF/MANIFEST.MF file and get the property Datastore-Class, which is what needs to be loaded
				jarStream = new JarInputStream(pluginClassURLs[i].openStream());
				Manifest manifest = jarStream.getManifest();
				Attributes attributes = manifest.getMainAttributes();
				// TODO SAM 2016-04-03 may also need the datastore factory class
				String dataStoreClassToLoad = attributes.getValue("Datastore-Class");
				// The following will search the list of URLs that was provided to the constructor
				Message.printStatus(2, routine, "Trying to load datastore class \"" + dataStoreClassToLoad + "\"");
				// This class is an instance of URLClassLoader so can run the super-class loadClass()
				Class<?> loadedClass = loadClass(dataStoreClassToLoad);
				Message.printStatus(2, routine, "Loaded datastore class \"" + dataStoreClassToLoad + "\"");
				pluginDataStoreList.add(loadedClass);
			}
			catch ( IOException ioe ) {
				Message.printWarning(3,routine,"Error loading plugin datastore from \"" + pluginClassURLs[i] + "\"");
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
		return pluginDataStoreList;
	}
	
	/**
	 * Load the datastore factory classes found in the jar file.
	 */
	public List<Class> loadDataStoreFactoryClasses () throws ClassNotFoundException {
		String routine = "loadDataStoreFactoryClasses";
		// Get all of the URLs that were specified to the loader
		URL [] pluginClassURLs = getURLs();
		// Plugin datastore factory classes that are loaded
		List<Class> pluginDataStoreFactoryList = new ArrayList<Class>();
		// Loop through all of the URLs
		for ( int i = 0; i < pluginClassURLs.length; i++ ) {
			JarInputStream jarStream = null;
			try {
				// Open the META-INF/MANIFEST.MF file and get the property DatastoreFactory-Class, which is what needs to be loaded
				jarStream = new JarInputStream(pluginClassURLs[i].openStream());
				Manifest manifest = jarStream.getManifest();
				Attributes attributes = manifest.getMainAttributes();
				String dataStoreFactoryClassToLoad = attributes.getValue("DatastoreFactory-Class");
				// The following will search the list of URLs that was provided to the constructor
				Message.printStatus(2, routine, "Trying to load datastore factory class \"" + dataStoreFactoryClassToLoad + "\"");
				// This class is an instance of URLClassLoader so can run the super-class loadClass()
				Class<?> loadedClass = loadClass(dataStoreFactoryClassToLoad);
				Message.printStatus(2, routine, "Loaded datastore factory class \"" + dataStoreFactoryClassToLoad + "\"");
				pluginDataStoreFactoryList.add(loadedClass);
			}
			catch ( IOException ioe ) {
				Message.printWarning(3,routine,"Error loading plugin datastore factory from \"" + pluginClassURLs[i] + "\"");
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
		return pluginDataStoreFactoryList;
	}
}