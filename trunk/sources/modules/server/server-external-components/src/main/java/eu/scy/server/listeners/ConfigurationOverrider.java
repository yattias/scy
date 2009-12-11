package eu.scy.server.listeners;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 10.des.2009
 * Time: 12:13:08
 * To change this template use File | Settings | File Templates.
 */
public class ConfigurationOverrider implements ServletContextListener {

    private static final Logger logger = Logger.getLogger(ConfigurationOverrider.class.getName());

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        try {
            logger.info("------------------------------------------------------------------");
            logger.info("--- OVERRIDING CONFIGURATION! WISH ME THE BEST!");
            logger.info("------------------------------------------------------------------");

            URL url = ConfigurationOverrider.class.getResource("serverconfig.properties");
            logger.info("configuration file found in " + url.toString());
            Properties props = new Properties();
            props.load(url.openStream());
            logger.info("loaded " + props.size() + " keys");
            logger.info("--------------------------");
            logger.info("--------------------------");
            logger.info("--------------------------");
            logger.info("--------------------------");
            logger.info("--------------------------");
            logger.info("--------------------------");
            //Properties props = System.getProperties();
            int counter = 0;
            for (Object key : props.keySet()) {
                if (key.toString().startsWith("scyconfig.")) {
                    counter++;
                    String keyString = key.toString();
                    keyString = keyString.substring("scyconfig.".length());
                    String prop = props.getProperty(key.toString());
                    logger.info("PROP=====>" + prop);
                    if (prop != null && !prop.equals("null")) {
                        if (prop.indexOf("$") >= 0) {
                            //hehe - totally freakin hack man!
                        } else {
                            System.setProperty((String) key, prop);
                            logger.info("OVERRIDDEN PROPERTY: " + key + ":::" + prop);
                        }


                    }

                    //System.setProperty(key.toString(), props.getProperty(key.toString()));

                } else {
                    logger.info("NOT USING KEY: " + key.toString());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
