// TSToolPluginComparator - compare plugin versions for sorting

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

import java.util.Comparator;

import RTi.Util.String.StringUtil;

/**
 * Comparator for Collections.sort to sort TSTool plugins by version.
 */
public class TSToolPluginComparator implements Comparator<TSToolPlugin> {

	/**
	 * Whether to sort the name ascending (default is true, alphabetical).
	 */
	private boolean sortNameAscending = true;

	/**
	 * Whether to sort the version ascending (default is false, newest on top).
	 */
	private boolean sortVersionAscending = false;
	
	/**
	 * Constructor.
	 * Default sorting is name ascending (alphabetical) with newest version first (descending).
	 */
	public TSToolPluginComparator () {
	}

	/**
	 * Constructor.
	 * @param sortNameAscending if true, sort the name ascending; if false, sort descending
	 * @param softVersionAscending if true, sort the version ascending; if false, sort descending
	 */
	public TSToolPluginComparator ( boolean sortNameAscending, boolean sortVersionAscending ) {
		this.sortNameAscending = sortNameAscending;
		this.sortVersionAscending = sortVersionAscending;
	}

	/**
	 * Compare two TSTool plugins using the semantic version.
	 * If pluginA is < pluginB, return -1.
	 * If pluginA = pluginB, return 0.
	 * If pluginA is > pluginB, return 1
	 * @param pluginA the first TSTool plugin to compare
	 * @param pluginB the second TSTool plugin to compare
	 */
	public int compare ( TSToolPlugin pluginA, TSToolPlugin pluginB ) {
		// First sort by plugin name.
		String nameA = pluginA.getName();
		String nameB = pluginB.getName();
		int result = nameA.compareTo(nameB);
		if ( result == 0 ) {
			// Then sort by plugin version in the same name.
			String versionA = pluginA.getVersion();
			String versionB = pluginB.getVersion();
			result = 0;
			// First do an ascending comparison.
			if ( StringUtil.compareSemanticVersions(versionA, "<", versionB, -1 ) ) {
				result = -1;
			}
			else if ( StringUtil.compareSemanticVersions(versionA, ">", versionB, -1 ) ) {
				result = 1;
			}
			else {
				result = 0;
			}
			if ( ! this.sortVersionAscending ) {
				// Reverse the order.
				result *= -1;
			}
		}
		else {
			if ( ! this.sortNameAscending ) {
				// Reverse the order.
				result *= -1;
			}
		}

		return result;
	}
}