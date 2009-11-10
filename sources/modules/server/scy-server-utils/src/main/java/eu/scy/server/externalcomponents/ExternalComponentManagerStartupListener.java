package eu.scy.server.externalcomponents;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 10.nov.2009
 * Time: 05:50:11
 * To change this template use File | Settings | File Templates.
 */
public class ExternalComponentManagerStartupListener implements ServletContextListener {

    private Logger log = Logger.getLogger("ExternalComponentManagerStartupListener.class");

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        log.info("****************************************************************************");
        log.info("****************** STARTING EXTERNAL COMPONENTS ****************************");
        log.info("****************************************************************************");
        System.out.println("here1");
        WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContextEvent.getServletContext());
        System.out.println("here2   ");
        if (wac == null) {
            System.out.println("WAC IS NULL");
            log.warning("WAC IS NULL!!");
        }

        try {
            System.out.println("here2.1");
            ExternalComponentManager manager = (ExternalComponentManager) wac.getBean("externalComponentManager");
            System.out.println("here3");
            if (manager == null) {
                System.out.println("MANAGER IS NULL");
                log.warning("MANAGER IS NULL!!!");
            }
            System.out.println("here4");
            manager.startupExternalComponents();
        } catch (Exception e) {
            e.printStackTrace();
        }
//                Server colemoServer = (Server) wac.getBean("colemoserver");
//                colemoServer.run();
//                log.info("COLEMO SERVER STARTED");

        log.info("****************************************************************************");
        log.info("****************** DONE STARTING EXTERNAL COMPONENTS ***********************");
        log.info("****************************************************************************");

    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
