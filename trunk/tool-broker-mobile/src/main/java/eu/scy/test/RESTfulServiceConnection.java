package eu.scy.test;

import javax.microedition.io.HttpConnection;
import javax.microedition.io.Connector;
import java.io.IOException;

/**
 * Created: 03.feb.2009 15:10:52
 *
 * This adds session support to a http connection
 *
 * @author Bjørge Næss
 */
public class RESTfulServiceConnection {
	private RESTfulServiceConnection instance;
	private final String SERVICE_URL = null;
	private String server_cookie;
	private HttpConnection httpConnection;

	private RESTfulServiceConnection() {

	}

	public RESTfulServiceConnection getInstance() {
		if (instance == null) instance = new RESTfulServiceConnection();
		return instance;
	}
	private HttpConnection openConnection() throws IOException {
		httpConnection = (HttpConnection) Connector.open(SERVICE_URL);
		if (server_cookie == null) server_cookie = httpConnection.getHeaderField("set-cookie");
		else httpConnection.setRequestProperty("cookie", server_cookie);
		return httpConnection;
	}
	public HttpConnection Request(String serviceName, String[] args) {
		return null;
	}
}
