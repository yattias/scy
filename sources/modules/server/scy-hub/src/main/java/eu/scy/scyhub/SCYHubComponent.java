package eu.scy.scyhub;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.xmpp.component.Component;
import org.xmpp.component.ComponentManager;
import org.xmpp.packet.JID;
import org.xmpp.packet.Message;
import org.xmpp.packet.Packet;
import org.xmpp.packet.PacketExtension;

import eu.scy.actionlogging.Action;
import eu.scy.actionlogging.ActionPacketTransformer;
import eu.scy.actionlogging.api.IActionLogger;
import eu.scy.actionlogging.api.IActionProcessModule;
import eu.scy.common.packetextension.SCYPacketTransformer;
import eu.scy.commons.whack.WhacketExtension;
import eu.scy.server.datasync.DataSyncModule;

/**
 * SCYHub is the central communicator, processes packets and routes them to the correct server side module
 *
 * @author anthonjp
 */
public class SCYHubComponent implements Component {
    
    private static final Logger logger = Logger.getLogger(SCYHubComponent.class.getName());
    private IActionProcessModule actionProcessModule;

    private IActionLogger actionLogger;
    
    private List<SCYHubModule> modules;

    public SCYHubComponent() {
        logger.info("INITIALIZING SCY HUB COMPONENT!");
        modules = new ArrayList<SCYHubModule>();
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
            // Get the requested station to obtain it's weather information
            Message message = (Message) packet;
            
            for (SCYHubModule module : modules) {
				if(module.accept(message)) {
					module.process(message);
				}
			}
            
            
            SCYPacketTransformer transformer = new ActionPacketTransformer();
            PacketExtension packetExtension = message.getExtension(transformer.getElementname(), transformer.getNamespace());
            if(packetExtension != null && packetExtension instanceof WhacketExtension) {
            	WhacketExtension we = (WhacketExtension) packetExtension;
            	logger.debug("Received packet with ActionLoggingExtension");
                Action action = (Action) we.getPojo();
                logger.debug("Received action from " + action.getUser());
                actionProcessModule.create(action);
            }
            
        } else {
        	logger.debug("Packet didn't contain any PacketExtension");
        }
    }
    
    /**
     * initialize
     */
    public void initialize(JID jid, ComponentManager componentManager) {
        initModules();
    }
    
    /**
     * Init the modules
     */
    private void initModules() {
    	
    	modules.add(new DataSyncModule(this));
    	
    	
        // action processing
//        actionProcessModule = new ActionProcessModule(conf.getSqlSpacesServerHost(), conf.getSqlSpacesServerPort());
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
    }

    public IActionLogger getActionLogger() {
        return actionLogger;
    }

    public void setActionLogger(IActionLogger actionLogger) {
        this.actionLogger = actionLogger;
    }
}
