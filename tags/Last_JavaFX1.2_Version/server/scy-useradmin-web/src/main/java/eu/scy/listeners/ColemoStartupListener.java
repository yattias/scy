package eu.scy.listeners;

//import eu.scy.colemo.server.network.Server;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 10.nov.2008
 * Time: 08:36:08
 */
public class ColemoStartupListener implements ServletContextListener {

    private Logger log = Logger.getLogger("ColemoStartupListener.class");

    public void contextInitialized(final ServletContextEvent servletContextEvent) {

        new Thread() {
            public void run() {
                log.info("Initializing COLEMO SERVER");
                WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContextEvent.getServletContext());
//                Server colemoServer = (Server) wac.getBean("colemoserver");
//                colemoServer.run();
//                log.info("COLEMO SERVER STARTED");
            }

        }.start();
    }

    public void contextDestroyed(ServletContextEvent sce) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
