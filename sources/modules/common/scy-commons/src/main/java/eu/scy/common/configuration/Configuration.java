package eu.scy.common.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;


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
			logger.debug("initialising eu.scy.common.configuration...");
			InputStream stream = Configuration.class.getResourceAsStream("scyconfig.properties");
			props.load(stream);
            logger.debug("...loaded "+props.size()+" keys.");
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
	
}