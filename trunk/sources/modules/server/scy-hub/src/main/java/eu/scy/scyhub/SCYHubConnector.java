package eu.scy.scyhub;

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
import org.springframework.beans.BeanUtils;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.ApplicationContext;

import java.util.logging.Logger;
import java.util.logging.Level;


/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 04.sep.2009
 * Time: 13:34:56
 * To change this template use File | Settings | File Templates.
 */
public class SCYHubConnector implements ServletContextListener {

    private Logger log = Logger.getLogger("SCYHubConnector.class");

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ApplicationContext ctx = initializeContext(servletContextEvent.getServletContext());

        log.info("** ** ** *************************");
        log.info("** ** ** CONNECTING TO THE SCY-HUB");
        log.info("** ** ** *************************");
        final ExternalComponentManager manager = new ExternalComponentManager(Configuration.getInstance().getDatasyncExternalComponentHost(), Configuration.getInstance().getDatasyncExternalComponentPort());
        manager.setSecretKey(Configuration.getInstance().getDatasyncMessageHub(), Configuration.getInstance().getDatasyncExternalComponentSecretKey());
        manager.setMultipleAllowed("scyhub", true);

        try {
            SCYHubComponent scyHubComponent = (SCYHubComponent) ctx.getBean("SCYHubComponent");
            initializeScyHubComponent(scyHubComponent);
            log.info("MESSAGE: " + Configuration.getInstance().getDatasyncMessageHub());
            manager.addComponent(Configuration.getInstance().getDatasyncMessageHub(), scyHubComponent);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private ApplicationContext initializeContext(ServletContext context) {
        log.info("-------------- > > INITIALIZING SPRING CONTEXT FROM SPRING CONFIG CLASS...");
        try {
            ConfigurableApplicationContext applicationContext = null;
            //SpringConfiguration springConfig = (SpringConfiguration) BeanUtils.instantiateClass(Class.forName("eu.scy.webapp.spring.impl.SpringConfigurationImpl"));
            //applicationContext = new ClassPathXmlApplicationContext(springConfig.getRootApplicationContextConfigLocations());
            log.info("--------------  > > CONTEXT INITIALIZED!");
            throw new RuntimeException("HENRIK FIX THIS ONE!!");

            //return applicationContext;
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        throw new RuntimeException("Cannot initialize spring context!");
    }

    private void initializeScyHubComponent(SCYHubComponent scyHubComponent) {
        //To change body of created methods use File | Settings | File Templates.
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}