package eu.scy.listeners;

import eu.scy.common.configuration.Configuration;
import eu.scy.communications.datasync.properties.CommunicationProperties;
import eu.scy.scyhub.SCYHubComponent;

import javax.servlet.ServletContextListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContext;

import org.jivesoftware.whack.ExternalComponentManager;
import org.xmpp.component.ComponentException;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.ContextLoader;

import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 11.aug.2009
 * Time: 12:23:15
 * To change this template use File | Settings | File Templates.
 */
public class SCYHubConnector implements ServletContextListener {

    private Logger log = Logger.getLogger("SCYHubConnector.class");

    private Configuration props = Configuration.getInstance();

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        XmlWebApplicationContext ctx = (XmlWebApplicationContext) servletContextEvent.getServletContext().getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
        if (ctx == null) {
            ctx = initializeContext(servletContextEvent.getServletContext());
        }

        log.info("** ** ** *************************");
        log.info("** ** ** CONNECTING TO THE SCY-HUB");
        log.info("** ** ** *************************");
        final ExternalComponentManager manager = new ExternalComponentManager(props.getDatasyncExternalComponentHost(), props.getDatasyncExternalComponentPort());
        manager.setSecretKey(props.getDatasyncMessageHub(), props.getDatasyncExternalComponentSecretKey());
        manager.setMultipleAllowed("scyhub", true);

        try {
            SCYHubComponent scyHubComponent = (SCYHubComponent) ctx.getBean("SCYHubComponent");
            initializeScyHubComponent(scyHubComponent);
            log.info("MESSAGE: " + props.getDatasyncMessageHub());
            manager.addComponent(props.getDatasyncMessageHub(), scyHubComponent);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private XmlWebApplicationContext initializeContext(ServletContext context) {
        XmlWebApplicationContext ctx;
        ctx = new XmlWebApplicationContext();
        String configLocations = context.getInitParameter(ContextLoader.CONFIG_LOCATION_PARAM);
        String[] files = configLocations.split(",");
        for (int i = 0; i < files.length; i++) {
            files[i] = files[i].trim();
        }
        if (log.isLoggable(Level.FINEST)) {
            log.finest("Spring context files: " + configLocations);
        }

        ctx.setConfigLocations(files);
        ctx.setServletContext(context);
        ctx.refresh();
        return ctx;
    }

    private void initializeScyHubComponent(SCYHubComponent scyHubComponent) {
        //To change body of created methods use File | Settings | File Templates.
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
