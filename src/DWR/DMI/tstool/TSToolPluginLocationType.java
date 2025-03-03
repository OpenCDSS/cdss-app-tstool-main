// TSToolPluginLocationType - whether a TSTool plugin is installed in TSTool software or user files

/* NoticeStart

TSTool
TSTool is a part of Colorado's Decision Support Systems (CDSS)
Copyright (C) 1994-2025 Colorado Department of Natural Resources

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

/**
TSTool plugin location type, which indicates whether a plugin is installed
in the TSTool software files or user files.
*/
public enum TSToolPluginLocationType {

	/**
	 * Location of the plugin files is with TSTool installation files.
	 */
	TSTOOL_FILES ("TSToolFiles"),

	/**
	* Location of the plugin files is in the user's files.
	*/
	USER_FILES ("UserFiles");

	/**
	 * The name that should be displayed when location type is used in UIs and output.
	 */
	private final String displayName;

	/**
	 * Construct an enumeration value.
	 * @param displayName name that should be displayed in choices, etc.
	 */
	private TSToolPluginLocationType(String displayName) {
	    this.displayName = displayName;
	}

	/**
	 * Return the display name for the enumeration.
	 * This is usually the same as the value but using appropriate mixed case.
	 * @return the display name.
	 */
	@Override
	public String toString() {
	    return displayName;
	}

	/**
	 * Return the enumeration value given a string name (case-independent).
	 * @param name the enumeration name to match
	 * @return the enumeration value given a string name (case-independent), or null if not matched.
	 */
	public static TSToolPluginLocationType valueOfIgnoreCase(String name) {
	    if ( name == null ) {
	        return null;
	    }
	    TSToolPluginLocationType [] values = values();
	    for ( TSToolPluginLocationType t : values ) {
	        if ( name.equalsIgnoreCase(t.toString()) ) {
	            return t;
	        }
	    }
	    return null;
	}

}