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
	
	public String getTestString() {
		return props.getProperty("communication.test.property");
	}
	
	public String get(String key) {
		return props.getProperty(key);
	}

	public String getDatasyncMessageHub() {
		return props.getProperty("communication.datasync.messageHub");
	}

	public String getDatasyncExternalComponentSecretKey() {
		return props.getProperty("communication.datasync.server.externalComponent.secretKey");
	}

	public String getDatasyncExternalComponentHost() {
		return props.getProperty("communication.datasync.server.externalComponent.host");
	}

	public int getDatasyncExternalComponentPort() {
		return Integer.parseInt(props.getProperty("communication.datasync.server.externalComponent.port"));
	}

	public String getClientEventCreateData() {
		return props.getProperty("communication.client.event.create.data");
	}

	public String getClientEventCreateSession() {
		return props.getProperty("communication.client.event.create.session");
	}

	public String getClientEventGetSessions() {
		return props.getProperty("communication.client.event.get.sessions");		
	}

	public String getClientEventSynchronize() {
		return props.getProperty("communication.client.event.synchronize");
	}

	public String getClientEventJoinSession() {
		return props.getProperty("communication.client.event.join.session");
	}

	public int getSqlSpacesServerPort() {
		return Integer.parseInt(props.getProperty("communication.sqlspaces.server.port"));
	}

	public String getSqlSpacesServerHost() {
		return props.getProperty("communication.sqlspaces.server.host");
	}

	public long getDatasyncMessageDefaultExpiration() {	
		return Long.parseLong(props.getProperty("communication.datasync.message.default.expiration"));
	}

	public String getDatasyncServerHost() {
		return props.getProperty("communication.datasync.server.host");
	}

	public String getDatasyncMessageHubAddress() {
		return getDatasyncMessageHub() + "." + getDatasyncServerHost();
	}

	public int getDatasyncServerPort() {
		return Integer.parseInt(props.getProperty("communication.datasync.server.port"));
	}

	public String getSqlSpacesServerSpaceDatasync() {
		return props.getProperty("communication.sqlspaces.server.space.datasync");
	}

    public String getEventQuery() {
        return props.getProperty("communication.client.event.query");
    }
	
}