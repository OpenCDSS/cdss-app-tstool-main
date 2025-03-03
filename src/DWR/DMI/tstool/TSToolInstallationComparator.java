// TSToolInstallationComparator - compare TSTool versions for sorting

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
 * Comparator for Collections.sort to sort TSTool installations by version.
 */
public class TSToolInstallationComparator implements Comparator<TSToolInstallation> {

	/**
	 * Whether to sort ascending (default is true).
	 */
	private boolean doAscending = true;
	
	/**
	 * Constructor.
	 * Default sorting is ascending with version with newest last.
	 */
	public TSToolInstallationComparator () {
		this.doAscending = true;
	}

	/**
	 * Constructor.
	 * @param doAscending if true, sort ascending; if false, sort descending
	 */
	public TSToolInstallationComparator ( boolean doAscending ) {
		this.doAscending = doAscending;
	}

	/**
	 * Compare two TSTool installations using the semantic version.
	 * If installationA is < installationB, return -1.
	 * If installationA = installationB, return 0.
	 * If installationA is > installationB, return 1
	 * @param installationA the first TSTool installation to compare
	 * @param installationB the second TSTool installation to compare
	 */
	public int compare ( TSToolInstallation installationA, TSToolInstallation installationB ) {
		String versionA = installationA.getVersion();
		String versionB = installationB.getVersion();
		int result = 0;
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
		if ( ! this.doAscending ) {
			// Reverse the order.
			result *= -1;
		}

		return result;
	}
}