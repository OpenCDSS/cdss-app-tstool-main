package rti.app.tstoolrestlet;

import java.util.Collection;

import org.restlet.Application;
import org.restlet.Component;
import org.restlet.Context;
import org.restlet.Restlet;
import org.restlet.Router;
import org.restlet.data.Parameter;
import org.restlet.data.Protocol;

import RTi.Util.Message.Message;

/**
TSTool restlet server application.
*/
public class TSToolServer extends Application
{

/**
The application component, which contains properties for the application.
*/
private Component __component = null;

/**
Create a new TSTool restlet server application.
@param port the port to use for the service (if null use 8182).
*/
public TSToolServer()
throws Exception
{
    // Initialization is done in startServer()
}

/**
Create the root restlet to receive all calls.
*/
@Override
public Restlet createRoot()
{   String routine = getClass().getName() + ".createRoot";
    // Create a router to handle the URIs - the context allows access to system resources initialized
    // from the parent (main) application
    Router router = new Router(getContext());
    // Template that allows using {name} syntax with a data model
    //Template template = null;
    
    Message.printStatus(2,routine,"Initialized restlet.");

    //router.attach("/static",new Directory(getContext(), new File("www").toURI().toString()));
    /*
    // simple routes
    router.attach("/hello", new HelloWorldRestlet(getContext()));
    // always put shortest routes first
    router.attach("/hello/{who}", new HelloWorldRestlet(getContext()));
    // notice difference between restlet and resource (one is instance, the other is a class)
    router.attach("/helloresource", HelloWorldResource.class);
    router.attach("/helloresource/{who}", HelloWorldResource.class);
    router.attach("/stuff", ListingResource.class);
    router.attach("/stuff/{type}", ListingResource.class);
    router.attach("/stuff/{type}/{id}", ListingResource.class);
    router.attach("/testform", FormResource.class);
    router.attach("/upload", UploadResource.class);
    Guard guard = new Guard(getContext(), "HOOHA Realm", new ArrayList<String>(Arrays.asList("localhost:8182")),"jjaahh");
    guard.getSecrets().put("BILLY", "FOOBAR".toCharArray());
    guard.setNext(ProtectedResource.class);
    router.attach("/protected", guard);
    router.attach("/notprotected", ProtectedResource.class);
    router.attach("/logout", ProtectedResource.class);


    // optional attribute route
    template = router.attach("/optional/{id}/{option}", OptionalArgumentResource.class).getTemplate();
    template.getVariables().put("option", new Variable(Variable.TYPE_ALPHA,"",false,false));
    */
    // Fall through to other resources
    //router.attachDefault(NotFoundResource.class );
    return router;
}

/**
Start the server.
@param port Port used for the service (1882 by default if specified <= 0).
@param params List of parameters passed in from the main application
(via command line, configuration file, etc.)
@throws Exception
 */
public void startServer( int port, Collection<Parameter> params ) throws Exception
{
    if ( __component == null ) {
        Component component = new Component();
        if ( port <= 0 ) {
            port = 8182; // Default
        }
        // Indicate the protocols that the server will handle
        component.getServers().add(Protocol.HTTP, port );
        component.getClients().add(Protocol.FILE);
        TSToolServer server = this;
        // Indicate to the component (this server) how to get to system resources (via main application)
        Context ctx = component.getContext().createChildContext();
        // Add parameters that may have been passed from the main application
        ctx.getParameters().addAll(params);
        server.setContext(ctx);
        component.getDefaultHost().attach(server);
        // Start the server
        component.start();
        // Only assign after start is successful
        this.__component = component;
    }
}

/**
Stop the server.
@throws Exception
*/
public void stopServer() throws Exception
{
    if ( __component != null) {
        __component.stop();
    }
}

}