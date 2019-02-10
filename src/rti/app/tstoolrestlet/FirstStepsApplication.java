// FirstStepsApplication - 

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

package rti.app.tstoolrestlet;

import org.restlet.Application;  
import org.restlet.Restlet;  
import org.restlet.Router;  
  
public class FirstStepsApplication extends Application {  
  
    /** 
     * Creates a root Restlet that will receive all incoming calls. 
     */  
    @Override  
    public Restlet createRoot() {  
        // Create a router Restlet that routes each call to a  
        // new instance of HelloWorldResource.  
        Router router = new Router(getContext());  
  
        // Defines only one route  
        router.attachDefault(HelloWorldResource.class);  
  
        return router;  
    }  
}
