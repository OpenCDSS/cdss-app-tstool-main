// NotFoundResource - Simple resource to indicate that a URI is not found.

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

import org.restlet.Context;
import org.restlet.data.MediaType;  
import org.restlet.data.Request;  
import org.restlet.data.Response;  
import org.restlet.resource.Representation;  
import org.restlet.resource.Resource;  
import org.restlet.resource.ResourceException;  
import org.restlet.resource.StringRepresentation;  
import org.restlet.resource.Variant;  
  
/** 
Simple resource to indicate that a URI is not found.
*/  
public class NotFoundResource extends Resource {  
  
    public NotFoundResource(Context context, Request request, Response response) {  
        super(context, request, response);  
  
        // This representation has only one type of representation.  
        getVariants().add(new Variant(MediaType.TEXT_PLAIN));  
    }  
  
    /** 
     * Returns a full representation for a given variant. 
     */  
    @Override  
    public Representation represent(Variant variant) throws ResourceException {  
        Representation representation = new StringRepresentation("Not found.", MediaType.TEXT_PLAIN);  
        return representation;  
    }  
}
