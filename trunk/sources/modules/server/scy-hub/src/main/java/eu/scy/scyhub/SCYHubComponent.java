package eu.scy.scyhub;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.dom4j.Element;
import org.jdom.JDOMException;
import org.xmpp.component.Component;
import org.xmpp.component.ComponentException;
import org.xmpp.component.ComponentManager;
import org.xmpp.component.ComponentManagerFactory;
import org.xmpp.packet.JID;
import org.xmpp.packet.Message;
import org.xmpp.packet.Packet;
import org.xmpp.packet.PacketExtension;

import eu.scy.actionlogging.api.IAction;
import eu.scy.actionlogging.api.IActionProcessModule;
import eu.scy.actionlogging.logger.Action;
import eu.scy.actionlogging.packetextension.ActionLoggingExtension;
import eu.scy.actionlogging.receiver.ActionProcessModule;
import eu.scy.communications.datasync.event.IDataSyncEvent;
import eu.scy.communications.datasync.event.IDataSyncListener;
import eu.scy.communications.datasync.properties.CommunicationProperties;
import eu.scy.communications.packet.extension.datasync.DataSyncPacketExtension;
import eu.scy.datasync.api.DataSyncException;
import eu.scy.datasync.api.IDataSyncModule;
import eu.scy.datasync.impl.factory.DataSyncModuleFactory;

/**
 * SCYHub is the central communicator, processes packets and routes them to the correct server side module
 *
 * @author anthonjp
 */
public class SCYHubComponent implements Component {
    
    private static final Logger logger = Logger.getLogger(SCYHubComponent.class.getName());
    
    private IDataSyncModule dataSyncModule;
    private IActionProcessModule actionProcessModule;
    private static CommunicationProperties communicationProps = new CommunicationProperties();
    
    
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
            
            PacketExtension packetExtension = message.getExtension(DataSyncPacketExtension.ELEMENT_NAME, DataSyncPacketExtension.NAMESPACE);
            if (packetExtension != null &&  packetExtension instanceof DataSyncPacketExtension ) {
                //found a datasync extension, yay!
                try {
                    // pass syncMessage to DataSyncModule for storing
                    DataSyncPacketExtension dse = ((DataSyncPacketExtension)packetExtension);
                    if( dse.getEvent().equals(communicationProps.clientEventCreateData)) {
                        dataSyncModule.create(dse.toPojo());
                    } else if (dse.getEvent().equals(communicationProps.clientEventCreateSession) ) {
                        dataSyncModule.createSession(dse.toPojo());
                    } else if( dse.getEvent().equals(communicationProps.clientEventGetSessions) ) {
                        dataSyncModule.getSessions(dse.toPojo());
                    } else if( dse.getEvent().equals(communicationProps.clientEventSynchronize) ) {
                        dataSyncModule.synchronizeClientState(dse.toPojo());
                    } else if( dse.getEvent().equals(communicationProps.clientEventJoinSession)) {
                        dataSyncModule.joinSession(dse.toPojo());
                    }
                } catch (DataSyncException e) {
                    e.printStackTrace();
                }// try
            }
            
            packetExtension = message.getExtension(ActionLoggingExtension.ELEMENT_NAME, ActionLoggingExtension.NAMESPACE);
            if(packetExtension != null && packetExtension instanceof ActionLoggingExtension) {
            	ActionLoggingExtension ale = (ActionLoggingExtension) packetExtension;
            	logger.debug("Received packet with ActionLoggingExtension");
            	Element el = (Element) ale.getElement().content().get(0);
            	logger.debug(el.asXML());
            	try {
                    IAction action = new Action(el.asXML());
                    actionProcessModule.create(action);
                } catch (JDOMException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            
            logger.debug("Packet didn't contain any PacketExtension");
        }// if 
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
        //register extensions
        DataSyncPacketExtension dsp = new DataSyncPacketExtension();
        ActionLoggingExtension ale = new ActionLoggingExtension();
        //data sync
        try {
            dataSyncModule = DataSyncModuleFactory.getDataSyncModule(DataSyncModuleFactory.LOCAL_STYLE);
            // add listner in order to get callbacks on stuff that's happening
            dataSyncModule.addDataSyncListener(new IDataSyncListener(){
                
                @Override
                public void handleDataSyncEvent(IDataSyncEvent event) {
                    //send a reply
                    Message reply = new Message();
                    if (event.getSyncMessage().getFrom() != null) {
                        reply.setTo(event.getSyncMessage().getFrom());
                    } else {
                        //TODO: throw exception here?
                        logger.error("Empty from field. Not good.");
                    }
                    reply.setFrom(SCYHubComponent.this.getName() + "." +communicationProps.datasyncExternalComponentHost);
                    reply.setType(Message.Type.normal);
                    reply.setBody("scyhub cares for you");
                   
                    DataSyncPacketExtension d = new DataSyncPacketExtension(event.getSyncMessage());
                    reply.addExtension(d);
                    try {
                        ComponentManagerFactory.getComponentManager().sendPacket(SCYHubComponent.this, reply);
                    } catch (ComponentException e) {
                        e.printStackTrace();
                    }
                }                     
            });
        } catch (DataSyncException exeption) {
            logger.error("Something was messed up during creation of dataSyncModule: ");
            exeption.printStackTrace();
        }
        // action processing
        actionProcessModule = new ActionProcessModule(communicationProps.sqlSpacesServerHost, communicationProps.sqlSpacesServerPort);
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
    
}
