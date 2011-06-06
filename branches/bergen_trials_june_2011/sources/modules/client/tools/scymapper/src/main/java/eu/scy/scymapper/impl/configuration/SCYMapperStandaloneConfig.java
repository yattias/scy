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
        NOHELP,
        VOLUNTARY,
        CONTINUOUS;

    }

    private final static Logger logger = Logger.getLogger(SCYMapperStandaloneConfig.class);

    private static SCYMapperStandaloneConfig instance = null;

    private static Properties config;

//    private static final String LOCAL_CONFIG_FILE_PATH = "src/main/resources/eu/scy/scymapper/";
    private static final String LOCAL_CONFIG_FILE_PATH = "";


    public static final String LOCAL_CONFIG_FILE = "scymapper.properties";

    public static final String KEY_HELP_MODE = "HelpMode";

    private static final Help DEFAULT_HELP_MODE = Help.VOLUNTARY;

    private static final String KEY_CON_HELP_WAIT_TIME = "ContinuousHelpWaitTime";

    private static final int DEFAULT_CON_HELP_WAIT_TIME = 180;

    private static final String KEY_CON_HELP_INTERVAL = "ContinuousHelpInterval";

    private static final int DEFAULT_CON_HELP_INTERVAL = 30;

    private static final String KEY_CON_HELP_WAIT_CONCEPT_NR = "ContinuousHelpWaitConceptNumber";

    private static final int DEFAULT_CON_HELP_WAIT_CONCEPT_NR = 5;

    private static final String KEY_RELATIONS = "Relations";

    private static final String KEY_LEXICON = "Lexicon";

    private static final String KEY_SQLSPACES_HOST = "sqlspaces.host";

    private static final String DEFAULT_SQLSPACES_HOST = "127.0.0.1";

    private static final String KEY_SQLSPACES_PORT = "sqlspaces.port";

    private static final int DEFAULT_SQLSPACES_PORT = 2525;

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

        props.setProperty(KEY_CON_HELP_WAIT_TIME, String.valueOf(DEFAULT_CON_HELP_WAIT_TIME));

        props.setProperty(KEY_CON_HELP_INTERVAL, String.valueOf(DEFAULT_CON_HELP_INTERVAL));

        props.setProperty(KEY_CON_HELP_WAIT_CONCEPT_NR, String.valueOf(DEFAULT_CON_HELP_WAIT_CONCEPT_NR));

        props.setProperty(KEY_SQLSPACES_HOST, DEFAULT_SQLSPACES_HOST);

        props.setProperty(KEY_SQLSPACES_PORT, String.valueOf(DEFAULT_SQLSPACES_PORT));
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

    public void setHelpMode(Help h) {
        config.setProperty(KEY_HELP_MODE, h.toString());
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

    public List<String> getLexicon() {
        try {
        	String lexiconEntries = config.getProperty(KEY_LEXICON);
        	if(lexiconEntries == null) {
        		return null;
        	}

            String[] entries = lexiconEntries.split(",");
            List<String> entriesAsList = new ArrayList<String>();
            for (String string : entries) {
                entriesAsList.add(string.trim());

            }
            return entriesAsList;

        } catch (IllegalArgumentException iae) {
            return null;
        }
    }

    public int getContinuousHelpWaitTime() {
        try {
            return Integer.parseInt(config.getProperty(KEY_CON_HELP_WAIT_TIME));
        } catch (NumberFormatException e) {
            return DEFAULT_CON_HELP_WAIT_TIME;
        }
    }

    public long getContinuousHelpInterval() {
        try {
            return Long.parseLong(config.getProperty(KEY_CON_HELP_INTERVAL));
        } catch (NumberFormatException e) {
            return DEFAULT_CON_HELP_INTERVAL;
        }
    }

    public int getContinuousHelpWaitConceptNr() {
        try {
            return Integer.parseInt(config.getProperty(KEY_CON_HELP_WAIT_CONCEPT_NR));
        } catch (NumberFormatException e) {
            return DEFAULT_CON_HELP_WAIT_CONCEPT_NR;
        }
    }

    public String getSQLSpacesHost() {
    	return config.getProperty(KEY_SQLSPACES_HOST);
    }

    public int getSQLSpacesPort() {
    	try {
    		return Integer.parseInt(config.getProperty(KEY_SQLSPACES_PORT));
    	} catch (NumberFormatException e) {
    		return DEFAULT_SQLSPACES_PORT;
    	}
    }

    // Only for testing purposes
    public static void main(String[] args) throws CloneNotSupportedException {
        SCYMapperStandaloneConfig config = new SCYMapperStandaloneConfig();
        logger.debug(config.getHelpMode());
        logger.debug(config.getContinuousHelpWaitTime());
        List<String> relations = config.getRelations();
        logger.debug("Relations:");
        for (String string : relations) {
            logger.debug("/" + string + "/");
        }
    }
}
