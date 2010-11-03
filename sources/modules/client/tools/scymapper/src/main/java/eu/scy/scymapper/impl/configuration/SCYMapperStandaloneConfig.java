package eu.scy.scymapper.impl.configuration;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

public class SCYMapperStandaloneConfig {

	public enum Help {
		NOHELP, VOLUNTARY, CONTINUOUS;

	}

	private final static Logger logger = Logger
			.getLogger(SCYMapperStandaloneConfig.class);

	private static SCYMapperStandaloneConfig instance = null;

	// private static final String LOCAL_CONFIG_FILE_PATH =
	// "src/main/java/eu/scy/scymapper/";
	private static final String LOCAL_CONFIG_FILE_PATH = "";

	private static final String LOCAL_CONFIG_FILE = "scymapper.properties";

	private static final String KEY_HELP_MODE = "HelpMode";

	private static final String KEY_CONTINUOUS_HELP_WAIT_TIME = "ContinuousHelpWaitTime";

	private static final String KEY_CONTINUOUS_HELP_INTERVAL = "ContinuousHelpInterval";

	private static Properties config;

	private static final Help DEFAULT_HELP_MODE = Help.CONTINUOUS;

	private static final int DEFAULT_CON_HELP_WAIT_TIME = 180;

	private static final int DEFAULT_CON_HELP_INTERVAL = 30;

	private static final String KEY_RELATIONS = "Relations";

	private SCYMapperStandaloneConfig() {
		config = new Properties();
		loadDefaultConfig(config);

		File file = new File(LOCAL_CONFIG_FILE_PATH + LOCAL_CONFIG_FILE);
		if (file.canRead()) {
			// Use config file if possible
			Reader reader = null;
			try {
				reader = new FileReader(file);
				config.load(reader);
			} catch (IOException e) {
				// TODO
			} finally {
				try {
					reader.close();
				} catch (IOException e) {
					// TODO
				}
			}
		}
	};

	private static void loadDefaultConfig(Properties props) {

		props.setProperty(KEY_HELP_MODE, DEFAULT_HELP_MODE.name());

		props.setProperty(KEY_CONTINUOUS_HELP_WAIT_TIME,
				String.valueOf(DEFAULT_CON_HELP_WAIT_TIME));
	}

	public static SCYMapperStandaloneConfig getInstance() {
		if (instance == null) {
			instance = new SCYMapperStandaloneConfig();
		}
		return instance;
	}

	public Help getHelpMode() {
		try {
			return Help.valueOf(config.getProperty(KEY_HELP_MODE));
		} catch (IllegalArgumentException e) {
			return DEFAULT_HELP_MODE;
		}
	}

	public List<String> getRelations() {
		try {
			String[] relations = config.getProperty(KEY_RELATIONS).split(",");
			List<String> relationsAsList = new ArrayList<String>();
			for (String string : relations) {
				relationsAsList.add(string.trim());

			}
			return relationsAsList;

		} catch (IllegalArgumentException iae) {
			return null;
		}
	}

	public void setHelpMode(Help h) {
		config.setProperty(KEY_HELP_MODE, h.toString());
	}

	public int getContinuousHelpWaitTime() {
		try {
			return Integer.parseInt(config
					.getProperty(KEY_CONTINUOUS_HELP_WAIT_TIME));
		} catch (NumberFormatException e) {
			return DEFAULT_CON_HELP_WAIT_TIME;
		}
	}


	public long getContinuousHelpInterval() {
	    try {
	        return Long.parseLong(config
	                .getProperty(KEY_CONTINUOUS_HELP_INTERVAL));
	    } catch (NumberFormatException e) {
	        return DEFAULT_CON_HELP_INTERVAL;
	    }
	}
	
	public void setContinuousHelpWaitTime(int time) {
		config.setProperty(KEY_CONTINUOUS_HELP_WAIT_TIME,
				Integer.toString(time));
	}

	// Only for testing purposes
	public static void main(String[] args) throws CloneNotSupportedException {
		SCYMapperStandaloneConfig config = new SCYMapperStandaloneConfig();
		System.out.println(config.getHelpMode());
		System.out.println(config.getContinuousHelpWaitTime());
		List<String> relations = config.getRelations();
		System.out.println("Relations:");
		for (String string : relations) {
			System.out.println("/"+string+"/");
		}
	}

}
