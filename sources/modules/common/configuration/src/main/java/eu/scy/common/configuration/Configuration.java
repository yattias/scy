package eu.scy.common.configuration;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import org.apache.log4j.Logger;


/**
 * @author bollen
 * @author giemza
 */
public class Configuration {
	
	private static Configuration instance = null;
	private final static Logger logger = Logger.getLogger(Configuration.class.getName());
	private Properties props;
	
	private Configuration() {
		try {
			logger.info("initialising default configuration");
			props = getDefaultProperties();
			logger.info("loading configuration from file");
			URL url = Configuration.class.getResource("scyconfig.properties");
			logger.info("configuration file found in " + url.toString());
			props.load(url.openStream());
            logger.info("loaded "+props.size()+" keys");
            logger.info("parsing system properties for configuration");
            Properties sysprops = System.getProperties();
            int counter = 0;
            for (Object key : sysprops.keySet()) {
                logger.info("PROP: " + key.toString());
                if (key.toString().startsWith("scyconfig.")) {
                	counter++;
                    String keyString = key.toString();
                    keyString = keyString.substring("scyconfig.".length());
                    props.setProperty(keyString, sysprops.getProperty(key.toString()));
                }
            }
            if(counter > 0) {
            	logger.info("loaded " + counter + " keys from system properties");
            } else {
            	logger.info("no keys loaded from system properties");
            }
            // check for old configuration items
            if(props.getProperty("communication.client.event.join.session") != null) {
            	System.err.println("Old property values found. You should only have one scyconfig.properties in your classpath!");
            	System.exit(-1);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	private Properties getDefaultProperties() {
		Properties props = new Properties();
		props.setProperty("sqlspaces.server.host", "scy.collide.info");
		props.setProperty("sqlspaces.server.port", "2525");
		props.setProperty("openfire.host", "scy.collide.info");
		props.setProperty("openfire.conference", "conference.scy.collide.info");
		props.setProperty("openfire.client.port", "5222");
		props.setProperty("openfire.externalcomponent.port", "5275");
		props.setProperty("openfire.externalcomponent.secretkey", "java");
		props.setProperty("scyhub.name", "scyhub");
        props.setProperty("sail.db.name", "sail_database");
        props.setProperty("sail.db.host", "localhost");
        props.setProperty("sail.db.username", "root");
        props.setProperty("sail.db.password", "");
		return props;
	}
	
	public static synchronized Configuration getInstance() {
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
	
	public String getOpenFireConference() {
		return props.getProperty("openfire.conference");
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

    // ### SAIL
    public String getSailDBHost() {
        return props.getProperty("sail.db.host");
    }

    // ### SAIL
    public String getSailDBName() {
        return props.getProperty("sail.db.name");
    }

    public String getSailDBUserName() {
        return props.getProperty("sail.db.username");
    }

    public String getSailDBPassword() {
        return props.getProperty("sail.db.password");
    }
}