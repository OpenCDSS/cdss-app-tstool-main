// PluginDataStoreClassLoader - Load plugin datastore classes.

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
import java.lang.reflect.Method;
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
 * Java class loaders, including URLClassLoader, first ask to load classes from their parent
 * class loader, which will not find dependencies specific to this class loader.
 * Therefore, try to load plugin dependencies in this classloader first.
 * See:  https://dzone.com/articles/java-classloader-handling.
 * There can be issue with multiple class loaders.
 * - see:  https://medium.com/jrtapsell/java-classloaders-ec56b8f9c2bd
 * @author sam
 *
 */
public class PluginDataStoreClassLoader extends URLClassLoader {
	
	private ChildClassLoader childClassLoader = null;
	
	/**
	 * TODO smalers 2020-07-26 this is new code to try to overcome class cast exception for same calls in different loaders..
	 * Construct the class loader with a list of jar files that are candidates to load,
	 * and specify a parent class loader.
	 * @param dataStoreJarList list of jar files to load
	 * @param prerent class loader
	 * @param useChildClassLoader if true, use the child class loader before giving the parent class loader a chance to load a class;
	 * if false, let the parent load the class first, which is the typical default.
	 */
	public PluginDataStoreClassLoader ( URL [] dataStoreJarList, ClassLoader parent, boolean useChildClassLoader ) {
		// Using URLClassLoader for the base class should result in the parent (application) class
		// loader being used for for any other class loads, then this class.
		// This won't work for new classes that are not in the parent classpath (manifest)
		super ( dataStoreJarList, parent );
		String routine = getClass().getSimpleName() + ".PluginDataStoreClassLoader";
		// The following is used to allow classes referenced by the plugin class loader to be loaded
		// here rather than the parent class loader
		Message.printStatus(2, routine, "Jar file list (classpath) size is " + dataStoreJarList.length );
		for ( int i = 0; i < dataStoreJarList.length; i++ ) {
			Message.printStatus(2, routine, "dataStoreJarList[" + i + "]=" + dataStoreJarList[i] );
		}
		Message.printStatus(2, routine, "In constructor, creating ChildClassLoader");
		if ( useChildClassLoader ) {
			this.childClassLoader = new ChildClassLoader(dataStoreJarList, new DetectClass(this.getParent()));
		}
	}

	/**
	 * Construct the class loader with a list of jar files that are candidates to load
	 */
	public PluginDataStoreClassLoader ( URL [] dataStoreJarList ) {
		// Using URLClassLoader for the base class should result in the parent (application) class
		// loader being used for for any other class loads, then this class.
		// This won't work for new classes that are not in the parent classpath (manifest)
		super ( dataStoreJarList );
		String routine = getClass().getSimpleName() + ".PluginDataStoreClassLoader";
		// The following is used to allow classes referenced by the plugin class loader to be loaded
		// here rather than the parent class loader
		Message.printStatus(2, routine, "Jar file list (classpath) size is " + dataStoreJarList.length );
		for ( int i = 0; i < dataStoreJarList.length; i++ ) {
			Message.printStatus(2, routine, "dataStoreJarList[" + i + "]=" + dataStoreJarList[i] );
		}
		Message.printStatus(2, routine, "In constructor, creating ChildClassLoader");
		this.childClassLoader = new ChildClassLoader(dataStoreJarList, new DetectClass(this.getParent()));
	}
	
	/**
	 * Load the command classes found in the jar file.
	 * TODO smalers 2020-7-26 copied the code here because using different class loaders was causing class cast exception
	 */
	public List<Class> loadCommandClasses () throws ClassNotFoundException {
		String routine = getClass().getSimpleName() + ".loadCommandClasses";
		// Get all of the URLs that were specified to the loader
		URL [] pluginClassURLs = getURLs();
		// Plugin command classes that are loaded
		List<Class> pluginCommandList = new ArrayList<>();
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
	
	/**
	 * Load the datastore classes found in the jar file.
	 * Multiple datastores may be in one jar file, but for practical reasons probably only one should be included
	 * to avoid confusion.
	 */
	@SuppressWarnings("rawtypes")
	public List<Class> loadDataStoreClasses () throws ClassNotFoundException {
		String routine = getClass().getSimpleName() + ".loadDataStoreClasses";
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
				// Try finding "Datastore-Class" in the main attributes, which was used in TSTool 12.06.00 and earlier
				Attributes attributes = manifest.getMainAttributes();
				// TODO SAM 2016-04-03 may also need the datastore factory class
				String dataStoreClassToLoad = attributes.getValue("Datastore-Class");
				if ( dataStoreClassToLoad == null ) {
					Message.printWarning(3, routine,
						"Cannot find Datastore-Class property in jar file MANIFEST main section.  Cannot load datastore class \"" + dataStoreClassToLoad + "\"");
					// TODO smalers 2018-09-18 figure out how to load from Name: section,
					// but would need to know how to request a name of interest
				}
				else {
					// The following will search the list of URLs that was provided to the constructor
					Message.printStatus(2, routine, "Trying to load datastore class \"" + dataStoreClassToLoad + "\"");
					// This class is an instance of URLClassLoader so can run the super-class loadClass()
					Class<?> loadedClass = loadClass(dataStoreClassToLoad);
					Message.printStatus(2, routine, "Loaded datastore class \"" + dataStoreClassToLoad + "\"");
					pluginDataStoreList.add(loadedClass);
				}
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
	 * Multiple datastore factories may be in one jar file, but for practical reasons probably only one should be included
	 * to avoid confusion.
	 */
	@SuppressWarnings("rawtypes")
	public List<Class> loadDataStoreFactoryClasses () throws ClassNotFoundException {
		String routine = getClass().getSimpleName() + ".loadDataStoreFactoryClasses";
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
				String dataStoreFactoryClassToLoad = attributes.getValue("DataStoreFactory-Class");
				if ( dataStoreFactoryClassToLoad == null ) {
					// Try old spelling - if null then have an issue
					dataStoreFactoryClassToLoad = attributes.getValue("DatastoreFactory-Class");
				}
				// If additional jar files are located in the path, they may be supporting packages rather than DataStore files
				if ( dataStoreFactoryClassToLoad == null ) {
					Message.printWarning(3, routine, "No DataStoreFactory-Class attribute in MANIFEST.MF for \"" +
						pluginClassURLs[i] + "\"" );
				}
				else {
					// The following will search the list of URLs that was provided to the constructor
					Message.printStatus(2, routine, "Trying to load datastore factory class \"" + dataStoreFactoryClassToLoad + "\"");
					// This class is an instance of URLClassLoader so can run the super-class loadClass()
					Class<?> loadedClass = loadClass(dataStoreFactoryClassToLoad);
					Message.printStatus(2, routine, "Loaded datastore factory class \"" + dataStoreFactoryClassToLoad + "\"");
					pluginDataStoreFactoryList.add(loadedClass);
				}
				
				// Also try to load plugin commands since these are now bundled in the same jar file as a plugin datastore.
				// - the MANIFEST line is similar to:  Command-Class1: trilynx.novastar5.ws.tstool.plugin.commands.ReadNovaStar_Command
				
				for ( int iCommand = 1; ; iCommand++ ) {
					String commandClassToLoad = attributes.getValue("Command-Class" + iCommand);
					if ( commandClassToLoad == null ) {
						// No more command classes so break out of the loop
						Message.printStatus(2, routine,  "Read " + (iCommand - 1) + " MANIFEST entries for command classes.");
						break;
					}
					else {
						// Found a command class
						Message.printStatus(2, routine,  "Found MANIFEST entry for command class: " +
							commandClassToLoad.substring(0,(commandClassToLoad.length() - (("" + iCommand).length() - 1)) ));
					}
				}
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
	
	// This method was added to support dependency loading
	/**
	 * Override the ClassLoader.loadClass() method, thereby allowing control of class loading before the parent class loader
	 * attempts to load the class.
	 * @param className the binary name of the class (e.g., java.lang.String, javax.swing.JSpinner$DefaultEditor - see explanation in ClassLoader javadoc).
	 * @param resolve if true, then resolve the class (normally the case?)
	 */
    @Override
    protected synchronized Class<?> loadClass(String className, boolean resolve) throws ClassNotFoundException {
      	if ( this.childClassLoader == null ) {
       		// Load classes as normal.
          	return super.loadClass(className, resolve);
       	}
       	else {
       		try {
       			// Load classes using the child class loader first, then the parent.
       			Message.printStatus(2, "PluginDataStoreClassLoader", "Calling childClassLoader.findClass(" + className + ").");
           		return childClassLoader.findClass(className);
       		}
        	catch( ClassNotFoundException e ) {
        		// Unable to use child class loader so use the parent class.
            	return super.loadClass(className, resolve);
        	}
        }
    }

	// This private class was added to support dependency loading
    private static class ChildClassLoader extends URLClassLoader {
        private DetectClass realParent;
        public ChildClassLoader( URL[] urls, DetectClass realParent ) {
            super(urls, null);
            this.realParent = realParent;
        }
        
        @Override
        public Class<?> findClass(String className) throws ClassNotFoundException
        {
            try {
            	Message.printStatus(2, "ChildClassLoader.findClass", "Calling super.findLoadedClass(" + className + ").");
            	Class<?> loaded = super.findLoadedClass(className);
                if ( loaded != null ) {
                	Message.printStatus(2, "ChildClassLoader.findClass", "Class is already loaded: " + className );
                    return loaded;
                }
                Message.printStatus(2, "ChildClassLoader.findClass", "Calling super.findClass(" + className + ").");
                return super.findClass(className);
            }
            catch( ClassNotFoundException e ) {
                return realParent.loadClass(className);
            }
        }
    }

	// This private class was added to support dependency loading
	/**
	 * Private internal class to ensure that child class loader is used before parent,
	 * therefore classes in local jars will be found first.
	 */
	private static class DetectClass extends ClassLoader {
		public DetectClass(ClassLoader parent) {
			super ( parent );
		}
		
		@Override
		public Class<?> findClass(String className) throws ClassNotFoundException {
			Message.printStatus(2, "DetectClass.findClass", "Calling super.findClass(" + className + ").");
			return super.findClass(className);
		}
	}
}
