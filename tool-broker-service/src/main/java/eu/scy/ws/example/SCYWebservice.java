package eu.scy.ws.example;

import com.sun.grizzly.http.SelectorThread;
import com.sun.jersey.api.container.grizzly.GrizzlyWebContainerFactory;

import java.util.HashMap;
import java.util.Map;
import java.io.IOException;

/**
 * Created: 04.feb.2009 16:21:36
 *
 *  This starts the web-service in a Grizzly Container 
 * @author Bj�rge N�ss
 */
public class SCYWebservice {
	public static void main(String[] args) throws IOException {

		// Where to publish the ws
		final String baseUri = "http://localhost:9998/";

		final Map<String, String> initParams = new HashMap<String, String>();

		// Configure jersey to read resources from the package
        initParams.put("com.sun.jersey.config.property.resourceConfigClass", "com.sun.jersey.api.core.PackagesResourceConfig");

		// This is the package where the service-classes live
        initParams.put("com.sun.jersey.config.property.packages", "eu.scy.ws.example.resources");

        System.out.println("Starting grizzly...");

		// Using Grizzly as servlet container for our ws
        SelectorThread threadSelector = GrizzlyWebContainerFactory.create(baseUri, initParams);
        System.out.println(String.format("Jersey app started with WADL available at %sapplication.wadl\nTry out %selo/1\nHit enter to stop it...", baseUri, baseUri));
        System.in.read();
        threadSelector.stopEndpoint();
        System.exit(0);
	}
}
