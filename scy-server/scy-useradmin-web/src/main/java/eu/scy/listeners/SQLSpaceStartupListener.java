package eu.scy.listeners;

import org.apache.log4j.Logger;

import javax.servlet.ServletContextListener;
import javax.servlet.ServletContextEvent;

import info.collide.sqlspaces.server.Server;
import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.TupleSpaceException;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 09.des.2008
 * Time: 11:35:13
 * To change this template use File | Settings | File Templates.
 */
public class SQLSpaceStartupListener implements ServletContextListener {
    private Logger log = Logger.getLogger(ColemoStartupListener.class);



    public void contextInitialized(final ServletContextEvent servletContextEvent) {

        new Thread() {
            public void run() {
                log.info("Initializing SQL SPACES SERVER");
                Server.startServer();
                
                try {
                    TupleSpace ts = new TupleSpace();
                } catch (TupleSpaceException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }

            }
        }.start();
    }

    public void contextDestroyed(ServletContextEvent sce) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
