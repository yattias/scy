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
 * @author Bjørge Næss
 */
public class SCYWebservice {
	public static void main(String[] args) throws IOException {

		final String baseUri = "http://localhost:9998/";
        final Map<String, String> initParams = new HashMap<String, String>();

        initParams.put("com.sun.jersey.config.property.resourceConfigClass", "com.sun.jersey.api.core.PackagesResourceConfig");

		// This is where 
        initParams.put("com.sun.jersey.config.property.packages", "eu.scy.ws.example.resources");

        System.out.println("Starting grizzly...");
        SelectorThread threadSelector = GrizzlyWebContainerFactory.create(baseUri, initParams);
        System.out.println(String.format("Jersey app started with WADL available at %sapplication.wadl\nTry out %selo/1\nHit enter to stop it...", baseUri, baseUri));
        System.in.read();
        threadSelector.stopEndpoint();
        System.exit(0);
	}
}
