package eu.scy.common.configuration;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * @author bollen
 */
public class Configuration {
	
	private static Configuration instance = null;
	private final static Logger logger = Logger.getLogger(Configuration.class.getName());
	private Properties props;
	
	private Configuration() {
		props = new Properties();
		try {
			logger.log(Level.INFO, "initialising eu.scy.common.configuration.Configuration...");
			InputStream stream = Configuration.class.getResourceAsStream("scyconfig.properties");
			props.load(stream);
            logger.log(Level.INFO, "...loaded "+props.size()+" keys.");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public static Configuration getInstance() {
		if (instance == null) {
			instance = new Configuration();
		}
		return instance;
	}
	
	public String get(String key) {
		return props.getProperty(key);
	}

	// ### OpenFireServer
	public String getOpenFireHost() {
		return props.getProperty("openfire.host");
	}

	public int getOpenFirePort() {
		return Integer.parseInt(props.getProperty("openfire.client.port"));
	}
	
	// ### External Component
	public int getOpenFireExternalComponentPort() {
		return Integer.parseInt(props.getProperty("openfire.externalcomponent.port"));
	}
	
	public String getOpenFireExternalComponentSecretKey() {
		return props.getProperty("openfire.externalcomponent.secretkey");
	}
	
	// ### SCYHub
	public String getSCYHubName() {
		return props.getProperty("scyhub.name");
	}

	// ### SQLSpaces
	public int getSQLSpacesServerPort() {
		return Integer.parseInt(props.getProperty("sqlspaces.server.port"));
	}

	public String getSQLSpacesServerHost() {
		return props.getProperty("sqlspaces.server.host");
	}
}