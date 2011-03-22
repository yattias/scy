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
    private String scyServerHost;

    /*
    Will pick up all properties that starts with scyconfig or sqlspaces. If the property starts with scyconofig, the prefix will be removed (not if it starts with sqlspaces - to make it perfectly confusing
     */
    private Configuration() {
        try {
            logger.info("initialising default configuration");
            props = getDefaultProperties();
            logger.info("loading configuration from file");
            URL url = Configuration.class.getResource("scyconfig.properties");
            logger.info("configuration file found in " + url.toString());
            props.load(url.openStream());
            logger.info("loaded " + props.size() + " keys");
            logger.info("parsing system properties for configuration");
            Properties sysprops = System.getProperties();
            int counter = 0;
            for (Object key : sysprops.keySet()) {
                logger.info("PROP: " + key.toString() + " ----> '" + sysprops.get(key) + "'");
                if (key.toString().startsWith("scyconfig.")) {
                    counter++;
                    String keyString = key.toString();
                    keyString = keyString.substring("scyconfig.".length());
                    props.setProperty(keyString, sysprops.getProperty(key.toString()));
                } else if (key.toString().startsWith("sqlspaces")) {
                    counter++;
                    String keyString = key.toString();
                    props.setProperty(keyString, sysprops.getProperty(keyString.toString()));
                }
            }
            if (counter > 0) {
                logger.info("loaded " + counter + " keys from system properties");
            } else {
                logger.info("no keys loaded from system properties");
            }
            // check for old configuration items
            if (props.getProperty("communication.client.event.join.session") != null) {
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
        props.setProperty("filestreamer.server", "scy.collide.info");
        props.setProperty("filestreamer.context", "/webapp/common/filestreamer.html");
        props.setProperty("filestreamer.port", "8080");
        props.setProperty("sail.db.name", "sail_database");
        props.setProperty("sail.db.host", "localhost");
        props.setProperty("sail.db.username", "root");
        props.setProperty("sail.db.password", "");
        props.setProperty("studentplanningtool.service.url", "");
        return props;
    }

    public static synchronized Configuration getInstance() {
        if (instance == null) {
            instance = new Configuration();
        }
        return instance;
    }

    public void setScyServerHost(String scyServerHost) {
        this.scyServerHost = scyServerHost;
    }

    private boolean isScyServerHostDefined() {
        return scyServerHost != null && scyServerHost.length() > 0;
    }

    public String get(String key) {
        return props.getProperty(key);
    }

    // ### OpenFireServer
    public String getOpenFireHost() {
        if (isScyServerHostDefined()) {
            return scyServerHost;
        }
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
        if (isScyServerHostDefined()) {
            return scyServerHost;
        }
        return props.getProperty("sqlspaces.server.host");
    }

    public String getSQLSpacesDBHost() {
        return props.getProperty("sqlspaces.db.mysql.host");
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

    public String getStudentPlanningToolUrl() {
        return props.getProperty("studentplanningtool.service.url");
    }

    // ### Filestreamer (-> profile pictures)
    public String getFilestreamerServer() {
        if (isScyServerHostDefined()) {
            return scyServerHost;
        }
        return props.getProperty("filestreamer.server");
    }

    public String getFilestreamerContext() {
        return props.getProperty("filestreamer.context");
    }

    public String getFilestreamerPort() {
        return props.getProperty("filestreamer.port");
    }

    public void setFilestreamerServer(String server) {
        props.setProperty("filestreamer.server", server);
    }

    public void setFilestreamerContext(String context) {
        props.setProperty("filestreamer.context", context);
    }

    public void setFilestreamerPort(String port) {
        props.setProperty("filestreamer.port", port);
    }
}