package eu.scy.scyhub;

import java.util.Date;

import org.apache.log4j.Logger;
import org.jivesoftware.openfire.pubsub.PendingSubscriptionsCommand;
import org.xmpp.component.Component;
import org.xmpp.component.ComponentException;
import org.xmpp.component.ComponentManager;
import org.xmpp.component.ComponentManagerFactory;
import org.xmpp.packet.JID;
import org.xmpp.packet.Message;
import org.xmpp.packet.Packet;
import org.xmpp.packet.PacketExtension;

import eu.scy.communications.message.ISyncMessage;
import eu.scy.communications.message.impl.DataSyncPacketExtension;
import eu.scy.communications.message.impl.SyncMessage;
import eu.scy.datasync.CommunicationProperties;
import eu.scy.datasync.api.DataSyncException;
import eu.scy.datasync.api.DataSyncUnkownEventException;
import eu.scy.datasync.api.IDataSyncModule;
import eu.scy.datasync.api.event.IDataSyncEvent;
import eu.scy.datasync.api.event.IDataSyncListener;
import eu.scy.datasync.api.session.IDataSyncSession;
import eu.scy.datasync.impl.factory.DataSyncModuleFactory;

/**
 * SCYHub is the central communicator, processes packets and routes them to the correct server side module
 *
 * @author anthonjp
 */
public class SCYHubComponent implements Component {
    
    private static final Logger logger = Logger.getLogger(SCYHubComponent.class.getName());
    
    private IDataSyncModule dataSyncModule;
    CommunicationProperties props = new CommunicationProperties();
    
    
    public String getName() {
        return "SCY HUB";
    }
    
    public String getDescription() {
        return "SCY Hub Component";
    }
    
    /**
     * process the packet and route the it to the correct place 
     */
    public void processPacket(Packet packet) {
        logger.debug("Received package:\n" + packet.toXML());
        // Only process Message packets
        if (packet instanceof Message) {
            logger.debug("Packet is a org.xmpp.packet.Message");
            // Get the requested station to obtain it's weather information
            Message message = (Message) packet;
            PacketExtension packetExtension = (PacketExtension) message.getExtension(DataSyncPacketExtension.ELEMENT_NAME, DataSyncPacketExtension.NAMESPACE);
            
            if (packetExtension != null) {
                //found a datasync extension, yay!
                logger.debug("Packet contains a DataSyncPacketExtension");
                DataSyncPacketExtension dspe = DataSyncPacketExtension.convertFromXmppPacketExtension(packetExtension);
                logger.debug("dspe: " + dspe.toXML());
                try {
                    dataSyncModule.processSyncMessage(dspe.toPojo());
                } catch (DataSyncUnkownEventException e) {
                    logger.debug("An event we did not anticipate " + e);
                    //TODO: alert tool by sending a message
                    e.printStackTrace();
                }
            } else {
                logger.debug("Packet didn't contain a DataSyncPacketExtension");
            }
        }
    }
    
    
    public void initialize(JID jid, ComponentManager componentManager) {
        logger.debug("SCYHubComponent.initialize()");
        initModules();
    }
    
    
    private void initModules() {
        //data sync
        try {
            dataSyncModule = DataSyncModuleFactory.getDataSyncModule(DataSyncModuleFactory.LOCAL_STYLE);
            // add listner in order to get callbacks on stuff that's happening
            dataSyncModule.addDataSyncListener(new IDataSyncListener(){
                
                @Override
                public void handleDataSyncEvent(IDataSyncEvent event) {
                    // ComponentManagerFactory.getComponentManager().sendPacket(this, packet)
                    ISyncMessage syncMessage = event.getSyncMessage();
                    if (syncMessage.getFrom() != null) {            
                        Message reply = new Message();
                        //FIXME: these props dont make sense to use here, but work for test purposes
                        reply.setTo("nutpaduser@" + props.datasyncServerHost);
                        reply.setFrom("scyhub." + props.datasyncServerHost);
                        reply.setBody("hey");
                        try {
                            ComponentManagerFactory.getComponentManager().sendPacket(SCYHubComponent.this, reply);
                        } catch (ComponentException e) {
                            ComponentManagerFactory.getComponentManager().getLog().error(e);
                        }
                    }                     
                }
            });
        } catch (DataSyncException exeption) {
            logger.error("Something was messed up during creation of dataSyncModule: ");
            exeption.printStackTrace();
        }        
    }
    
    
    public void start() {
        logger.debug("SCYHubComponent.start()");
    }
    
    
    public void shutdown() {
        logger.debug("SCYHubComponent.shutdown()");
    }
}
