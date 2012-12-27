package eu.scy.scyhub;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import eu.scy.core.openfire.ScyAuthorRuntimeModule;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.xmpp.component.Component;
import org.xmpp.component.ComponentManager;
import org.xmpp.packet.JID;
import org.xmpp.packet.Message;
import org.xmpp.packet.Packet;

import eu.scy.actionlogging.server.ActionProcessModule;
import eu.scy.server.datasync.DataSyncModule;
import eu.scy.server.notification.Notificator;

/**
 * SCYHub is the central communicator, processes packets and routes them to the correct server side module
 *
 * @author giemza
 * @author anthonjp
 */
public class SCYHubComponent implements Component, ApplicationContextAware {
    
    private static final Logger logger = Logger.getLogger(SCYHubComponent.class);

    private List<SCYHubModule> modules;

    private ApplicationContext applicationContext;

    private ExecutorService executorService;

    public SCYHubComponent() {
        logger.info("INITIALIZING SCY HUB COMPONENT!");
        modules = new ArrayList<SCYHubModule>();
        executorService = Executors.newFixedThreadPool(25);
    }

    /**
     * Gets the name
     * 
     * @return
     */
    public String getName() {
        return "scyhub";
    }
    
    /**
     * gets the component description
     * 
     * @return 
     */
    public String getDescription() {
        return "SCY Hub Component";
    }
    
    /**
     * process the packet and route the it to the correct place 
     */
    public void processPacket(Packet packet) {
    	logger.debug("Received packet" + packet);
        // Only process Message packets
        if (packet instanceof Message) {
           final  Message message = (Message) packet;
            
            for (final SCYHubModule module : modules) {
				if(module.accept(message)) {
                    if (!executorService.isShutdown()) {
                        executorService.execute(new Runnable() {
                            @Override
                            public void run() {
                                module.process(message);
                            }
                        });
                    }
				}
			}
        } else {
        	logger.debug("Not a message so we skip it!");
        }
    }
    
    /**
     * initialize
     */
    public void initialize(JID jid, ComponentManager componentManager) {
        //initModules();
    }
    
    /**
     * Init the modules
     */
    private void initModules() {
        modules.add((SCYHubModule) getBean("dataSyncModule"));
        modules.add((SCYHubModule) getBean("actionProcessModule"));
        modules.add((SCYHubModule) getBean("notificator"));
        //modules.add((SCYHubModule) getBean("scyAuthorRuntimeModule"));
    }
    
    
    /**
     * Starts the component
     */
    public void start() {
        logger.debug("SCYHubComponent.start()");
    }
    
    /**
     * Shuts this baby down
     */
    public void shutdown() {
        logger.debug("SCYHubComponent.shutdown()");
        executorService.shutdown();
        for(SCYHubModule module : modules) {
        	module.shutdown();
        }
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    private Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }
}
