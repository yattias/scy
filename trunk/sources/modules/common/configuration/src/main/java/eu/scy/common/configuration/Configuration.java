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
    private Integer scyServerPort;

    /*
    Will pick up all properties that starts with scyconfig or sqlspaces. If the property starts with scyconofig, the prefix will be removed (not if it starts with sqlspaces - to make it perfectly confusing
     */
    private Configuration() {
        StringBuilder builder = new StringBuilder();
        try {
            builder.append("initialising default configuration\n");
            props = getDefaultProperties();
            builder.append("loading configuration from file\n");
            URL url = Configuration.class.getResource("scyconfig.properties");
            builder.append("configuration file found in " + url.toString() + "\n");
            props.load(url.openStream());
            builder.append("loaded " + props.size() + " keys\n");
            builder.append("parsing system properties for configuration\n");
            Properties sysprops = System.getProperties();
            int counter = 0;
            for (Object key : sysprops.keySet()) {
                builder.append("PROP: " + key.toString() + " ----> '" + sysprops.get(key) + "'\n");
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
                builder.append("loaded " + counter + " keys from system properties\n");
            } else {
                builder.append("no keys loaded from system properties\n");
            }
            logger.info(builder.toString());
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
        props.setProperty("auth.cr.context", "webapp/UserCredentials.html");
        props.setProperty("auth.cr.server", "scy.collide.info");
        props.setProperty("auth.cr.port", "8080");
        props.setProperty("filestreamer.port", "8080");
        props.setProperty("roolo.server", "scy.collide.info");
        props.setProperty("roolo.context", "extcomp");
        props.setProperty("roolo.port", "8080");
        props.setProperty("sail.db.name", "sail_database");
        props.setProperty("sail.db.host", "localhost");
        props.setProperty("sail.db.username", "root");
        props.setProperty("sail.db.password", "");
        props.setProperty("studentplanningtool.service.url", "");
        props.setProperty("eportfolio.protocol", "http");
        props.setProperty("eportfolio.server", "scy.collide.info");
        props.setProperty("eportfolio.port", "8080");
        props.setProperty("eportfolio.context", "/webapp/app/eportfolio/");
        props.setProperty("feedback.protocol", "http");
        props.setProperty("feedback.server", "scy.collide.info");
        props.setProperty("feedback.port", "8080");
        props.setProperty("feedback.context", "/webapp/app/feedback/");
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

    public void setScyServerPort(Integer scyServerPort) {
        this.scyServerPort = scyServerPort;
    }

    private boolean isScyServerPortDefined() {
        return scyServerPort != null && scyServerPort > 0;
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
        if (isScyServerPortDefined()) {
            return "" + scyServerPort;
        }
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

    // ### RoOLO (-> for remoting, like from elo-save-webservice)
    public String getRooloServer() {
        if (isScyServerHostDefined()) {
            return scyServerHost;
        }
        return props.getProperty("roolo.server");
    }

    public String getRooloContext() {
        return props.getProperty("roolo.context");
    }

    public String getRooloPort() {
        if (isScyServerPortDefined()) {
            return "" + scyServerPort;
        }
        return props.getProperty("roolo.port");
    }

    public void setRooloServer(String server) {
        props.setProperty("roolo.server", server);
    }

    public void setRooloContext(String context) {
        props.setProperty("roolo.context", context);
    }

    public void setRooloPort(String port) {
        props.setProperty("roolo.port", port);
    }

    public String getEportfolioProtocol() {
        return props.getProperty("eportfolio.protocol");
    }

    public String getEportfolioServer() {
        if (isScyServerHostDefined()) {
            return scyServerHost;
        }
        return props.getProperty("eportfolio.server");
    }

    public String getEportfolioPort() {
        if (isScyServerPortDefined()) {
            return "" + scyServerPort;
        }
        return props.getProperty("eportfolio.port");
    }

    public String getEportfolioContext() {
        return props.getProperty("eportfolio.context");
    }

    public String getFeedbackProtocol() {
        return props.getProperty("feedback.protocol");
    }

    public String getFeedbackServer() {
        if (isScyServerHostDefined()) {
            return scyServerHost;
        }
        return props.getProperty("feedback.server");
    }

    public String getFeedbackPort() {
        if (isScyServerPortDefined()) {
            return "" + scyServerPort;
        }
        return props.getProperty("feedback.port");
    }

    public String getFeedbackContext() {
        return props.getProperty("feedback.context");
    }

    public String getChallengeResponseServiceURL() {
        String context = props.getProperty("auth.cr.context");
        String server = null;
        String port = null;
        if (isScyServerHostDefined()) {
            server = scyServerHost;
        } else {
            server = props.getProperty("auth.cr.server");
        }
        if (isScyServerPortDefined()) {
            port = scyServerPort.toString();
        } else {
            port = props.getProperty("auth.cr.port");
        }
        if (context != null && server != null && port != null) {
            StringBuffer str = new StringBuffer();
            str.append(server);
            str.append(":");
            str.append(port);
            str.append("/");
            str.append(context);
            return str.toString();
        }
        return null;
    }
}
