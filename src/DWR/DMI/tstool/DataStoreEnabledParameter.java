package DWR.DMI.tstool;

import java.util.ArrayList;
import java.util.List;

import RTi.Util.Message.Message;

/**
 * This class stores information from the --enable-datastores and --disable-datastores command parameters,
 * which specify what datastores should be enabled at startup, and checks the information.
 * This class saves a single command line parameter's information.
 */
public class DataStoreEnabledParameter {

	/**
	 * Original command parameter.
	 */
	private String parameter = null;

	/**
	 * Whether the datastores are enabled (--enable-datastores) or disabled (--disable-datastores).
	 */
	boolean isEnabled = true;

	/**
	 * Datastore patterns from the command parameter.
	 */
	private List<String> datastores = new ArrayList<>();

	/**
	 * Construct a parameter object.
	 * @param parameter command line parameter
	 * (--disable-datatores=name1,name2 or --enable-datastores=name1,name2, where name can use * for wildcard)
	 */
	public DataStoreEnabledParameter ( String parameter ) {
		String routine = getClass().getSimpleName();
		// Save in case want for message later.
		this.parameter = parameter;
		boolean goodSyntax = true;
		if ( parameter.startsWith("--disable-datastores") ) {
			Message.printStatus ( 2, routine, "Added datastore disabled parameter: " + parameter );
			this.isEnabled = false;
		}
		else if ( parameter.startsWith("--enable-datastores") ) {
			Message.printStatus ( 2, routine, "Added datastore enabled parameter: " + parameter );
			this.isEnabled = true;
		}
		else {
			Message.printStatus ( 2, routine, "Datastore parameter is invalid: " + parameter );
			goodSyntax = false;
		}

		if ( goodSyntax ) {
			int pos = parameter.indexOf("=");
			if ( pos > 0 ) {
				// Split the datastores by a comma.
				String [] parts = parameter.substring(pos + 1).split(",");
				// Add the datastore names to the list.
				for ( String part : parts ) {
					// Add the datastore patterns as Java regular expression.
					String pattern = part.trim().replace("*", ".*");
					this.datastores.add(pattern);
					Message.printStatus ( 2, routine, "  Added datastore pattern \"" + pattern + "\"." );
				}
			}
		}
	}

	/**
	 * Return whether the command parameter enables or disables datastores.
	 * @return whether the command parameter enables or disables datastores
	 */
	public boolean getIsEnabled () {
		return this.isEnabled;
	}

	/**
	 * Return the command line parameter.
	 * @return the command line parameter
	 */
	public String getParameter() {
		return this.parameter;
	}

	/**
	 * Determine whether a datastore is matched by the command parameter.
	 * @param datastoreToMatch datastore to match
	 * @return true if the datastore matches those for the command parameter
	 */
	public boolean matches ( String datastoreToMatch ) {
		String routine = getClass().getSimpleName() + ".matches";
		for ( String datastore : this.datastores ) {
			Message.printStatus(2, routine, "Comparing \"" + datastoreToMatch + "\" with \"" + datastore + "\".");
			if ( datastoreToMatch.matches(datastore) ) {
				// Matched a datastore from the command parameter.
				return true;
			}
		}
		return false;
	}
}