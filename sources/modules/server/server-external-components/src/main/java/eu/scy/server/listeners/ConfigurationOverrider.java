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
            URL url = ConfigurationOverrider.class.getResource("serverconfig.properties");
            Properties props = new Properties();
            props.load(url.openStream());
            int counter = 0;
            for (Object key : props.keySet()) {
                if (key.toString().startsWith("scyconfig.")) {
                    counter++;
                    String keyString = key.toString();
                    keyString = keyString.substring("scyconfig.".length());
                    String prop = props.getProperty(key.toString());
                    if (prop != null && !prop.equals("null")) {
                        if (prop.indexOf("$") >= 0) {
                            //hehe - totally freakin hack man!
                        } else {
                            System.setProperty((String) key, prop);
                            logger.fine("==> OVERRIDDEN PROPERTY: " + key + ":::" + prop);
                        }


                    }
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
