package eu.scy.listeners;

import eu.scy.colemo.server.network.Server;

import javax.servlet.ServletContextListener;
import javax.servlet.ServletContextEvent;

import org.apache.log4j.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 10.nov.2008
 * Time: 08:36:08
 * To change this template use File | Settings | File Templates.
 */
public class ColemoStartupListener implements ServletContextListener {

    private Logger log = Logger.getLogger(ColemoStartupListener.class);

    public void contextInitialized(ServletContextEvent sce) {

        new Thread() {

            public void run() {
                log.info("Initializing COLEMO SERVER");
                Server colemoServer = new Server();
                colemoServer.run();
                log.info("COLEMO SERVER STARTED");

            }

        }.start();



    }

    public void contextDestroyed(ServletContextEvent sce) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
