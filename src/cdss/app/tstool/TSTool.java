// TSTool - Main (application startup) class for CDSS version of TSTool.

/* NoticeStart

TSTool
TSTool is a part of Colorado's Decision Support Systems (CDSS)
Copyright (C) 1994-2022 Colorado Department of Natural Resources

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

package cdss.app.tstool;

import DWR.DMI.tstool.TSToolMain;

/**
Main (application startup) class for CDSS version of TSTool.
This class will starts the TSTool user interface (UI) or runs the TSCommandProcessor in batch mode with a command file.
*/
public class TSTool
{

/**
Start the main application instance.
@param args Command line arguments.
*/
public static void main ( String args[] )
{	TSToolMain.main(args);
}

}