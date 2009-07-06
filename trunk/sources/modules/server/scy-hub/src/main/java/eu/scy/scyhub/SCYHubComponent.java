package eu.scy.scyhub;

import org.apache.log4j.Logger;
import org.xmpp.component.Component;
import org.xmpp.component.ComponentException;
import org.xmpp.component.ComponentManager;
import org.xmpp.component.ComponentManagerFactory;
import org.xmpp.packet.JID;
import org.xmpp.packet.Message;
import org.xmpp.packet.Packet;

import eu.scy.communications.datasync.event.IDataSyncEvent;
import eu.scy.communications.datasync.event.IDataSyncListener;
import eu.scy.communications.datasync.properties.CommunicationProperties;
import eu.scy.communications.datasync.session.IDataSyncSession;
import eu.scy.communications.message.ISyncMessage;
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
    
    private IDataSyncSession dataSyncSession;
    private IDataSyncModule dataSyncModule;
    private static CommunicationProperties communicationProps = new CommunicationProperties();
    
    
    /**
     * Gets the name
     * 
     * @return
     */
    public String getName() {
        return "SCY HUB";
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
        logger.debug("Received package:\n" + packet.toXML());
        // Only process Message packets
        if (packet instanceof Message) {
            logger.debug("Packet is a org.xmpp.packet.Message");
            // Get the requested station to obtain it's weather information
            Message message = (Message) packet;
            DataSyncPacketExtension packetExtension = (DataSyncPacketExtension) message.getExtension(DataSyncPacketExtension.ELEMENT_NAME, DataSyncPacketExtension.NAMESPACE);
            
            if ( packetExtension instanceof DataSyncPacketExtension ) {
                //found a datasync extension, yay!
                logger.debug("Packet contains a DataSyncPacketExtension");
                try {
                    // pass syncMessage to DataSyncModule for storing
                    DataSyncPacketExtension dse = ((DataSyncPacketExtension)packetExtension);
                    if( dse.getEvent().equals(communicationProps.clientEventCreateData)) {
                        dataSyncModule.create(dse.toPojo());
                    } else if (dse.getEvent().equals(communicationProps.clientEventCreateSession) ) {
                        dataSyncModule.createSession(dse.toPojo());
                    } else if( dse.getEvent().equals(communicationProps.clientEventGetSessions) ) {
                        dataSyncModule.getSessions(dse.toPojo());
                    }// if
                } catch (DataSyncException e1) {
                    e1.printStackTrace();
                }// try
            } else {
                logger.debug("Packet didn't contain a DataSyncPacketExtension");
            }// if
        }// if 
    }
    
    /**
     * initialize
     */
    public void initialize(JID jid, ComponentManager componentManager) {
        logger.debug("SCYHubComponent.initialize()");
        initModules();
    }
    
    /**
     * Init the modules
     */
    private void initModules() {
        //register extensions
        DataSyncPacketExtension dsp = new DataSyncPacketExtension();
        //data sync
        try {
            dataSyncModule = DataSyncModuleFactory.getDataSyncModule(DataSyncModuleFactory.LOCAL_STYLE);
            // add listner in order to get callbacks on stuff that's happening
            dataSyncModule.addDataSyncListener(new IDataSyncListener(){
                
                @Override
                public void handleDataSyncEvent(IDataSyncEvent event) {
                    // ComponentManagerFactory.getComponentManager().sendPacket(this, packet)
                    ISyncMessage syncMessage = event.getSyncMessage();
                        
                        //send a reply
                        Message reply = new Message();
                        //FIXME
                        reply.setTo("obama@" + communicationProps.datasyncServerHost);
                        reply.setFrom(ComponentManagerFactory.getComponentManager().getServerName());
                        reply.setType(Message.Type.normal);
                        reply.setBody("scyhub is watching you");
                       
                        DataSyncPacketExtension d = new DataSyncPacketExtension(event.getSyncMessage());
                        reply.addExtension(d);
                        try {
                            ComponentManagerFactory.getComponentManager().sendPacket(SCYHubComponent.this, reply);
                        } catch (ComponentException e) {
                            e.printStackTrace();
                        }
                        logger.debug("SCYHubComponent.initModules()");
                    }                     
            });
        } catch (DataSyncException exeption) {
            logger.error("Something was messed up during creation of dataSyncModule: ");
            exeption.printStackTrace();
        }        
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
