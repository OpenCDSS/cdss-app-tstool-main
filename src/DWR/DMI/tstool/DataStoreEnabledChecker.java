package DWR.DMI.tstool;

import java.util.ArrayList;
import java.util.List;

import RTi.Util.Message.Message;

/**
 * This class stores information from the --enable-datastores and --disable-datastores command parameters,
 * which specify what datastores should be enabled at startup, and checks the information.
 * The command line parameters are consulted before opening every datastore. 
 */
public class DataStoreEnabledChecker {

	/**
	 * List of datastore enabled/disabled command parameters.
	 */
	private List<DataStoreEnabledParameter> parameters = new ArrayList<>();

	/**
	 * Construct a checker.
	 */
	public DataStoreEnabledChecker () {
	}
	
	/**
	 * Add a command line parameter
	 * @param parameter the command line parameter to add
	 */
	public void addParameter ( String parameter ) {
		// The following will result in the datastore being enabled or disabled
		// based on the command parameter.
		this.parameters.add ( new DataStoreEnabledParameter(parameter) );
	}
	
	/**
	 * Return the size of the checker, which is the number of --disable-datastores and --enable-datastores
	 * command parameters that were specified.
	 * @return the size of the checker
	 */
	public int size () {
		return this.parameters.size();
	}
	
	/**
	 * Determine whether a datastore is enabled for TSTool startup.
	 * The combination of command line parameters is evaluated in sequence.
	 * @param datastore datastore name to check, from a datastore configuration file
	 * @return true if the command parameters indicate that the datastore should be loaded
	 */
	boolean isDataStoreEnabled ( String datastore ) {
		String routine = getClass().getSimpleName() + ".isDataStoreEnabled";
		// Default is enabled (Datastore configuration file Enabled is missing)
		// which will result in the datastore configuration file Enabled to be checked.
		boolean isEnabled = true;
		if ( Message.isDebugOn ) {
			Message.printStatus(2, routine, "Checking whether datastore \"" + datastore + "\" is enabled." );
			Message.printStatus(2, routine, "  Starting with enabled = true.");
		}
		for ( DataStoreEnabledParameter parameter : this.parameters ) {
			if ( parameter.matches(datastore) ) {
				// The datastore matches a saved parameter's datastore:
				// - get the enabled/disabled state
				isEnabled = parameter.getIsEnabled();
				if ( Message.isDebugOn ) {
					Message.printStatus(2, routine, "  Changed enabled to " + isEnabled );
				}
			}
		}
		// Return the overall evaluation of whether the datastore should be considered enabled or disabled.
		return isEnabled;
	}
}