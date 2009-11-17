package eu.scy.scyhub;

import org.apache.log4j.Logger;
import org.xmpp.component.Component;
import org.xmpp.component.ComponentException;
import org.xmpp.component.ComponentManager;
import org.xmpp.component.ComponentManagerFactory;
import org.xmpp.packet.JID;
import org.xmpp.packet.Message;
import org.xmpp.packet.Packet;
import org.xmpp.packet.PacketExtension;

import eu.scy.actionlogging.Action;
import eu.scy.actionlogging.ActionPacketTransformer;
import eu.scy.actionlogging.api.IActionLogger;
import eu.scy.actionlogging.api.IActionProcessModule;
import eu.scy.actionlogging.server.ActionProcessModule;
import eu.scy.common.configuration.Configuration;
import eu.scy.commons.whack.WhacketExtension;
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
    private Configuration conf = Configuration.getInstance();
    private IDataSyncModule dataSyncModule;
    private IActionProcessModule actionProcessModule;

    private IActionLogger actionLogger;

    public SCYHubComponent() {
        logger.info("INITIALIZING SCY HUB COMPONENT!");
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

            PacketExtension packetExtension = message.getExtension(DataSyncPacketExtension.ELEMENT_NAME, DataSyncPacketExtension.NAMESPACE);
            if (packetExtension != null && packetExtension instanceof DataSyncPacketExtension) {
                //found a datasync extension, yay!
                try {
                    // pass syncMessage to DataSyncModule for storing
                    DataSyncPacketExtension dse = ((DataSyncPacketExtension) packetExtension);
                    if (dse.getEvent().equals(conf.getClientEventCreateData())) {
                        dataSyncModule.create(dse.toPojo());
                    } else if (dse.getEvent().equals(conf.getClientEventCreateSession())) {
                        dataSyncModule.createSession(dse.toPojo());
                    } else if (dse.getEvent().equals(conf.getClientEventGetSessions())) {
                        dataSyncModule.getSessions(dse.toPojo());
                    } else if (dse.getEvent().equals(conf.getClientEventSynchronize())) {
                        dataSyncModule.synchronizeClientState(dse.toPojo());
                    } else if (dse.getEvent().equals(conf.getClientEventJoinSession())) {
                        dataSyncModule.joinSession(dse.toPojo());
                    }
                } catch (DataSyncException e) {
                    e.printStackTrace();
                }// try
            }

            ActionPacketTransformer transformer = new ActionPacketTransformer();
            packetExtension = message.getExtension(transformer.getElementname(), transformer.getNamespace());
            if (packetExtension != null && packetExtension instanceof WhacketExtension) {
                WhacketExtension we = (WhacketExtension) packetExtension;
                logger.debug("Received packet with ActionLoggingExtension");
                Action action = (Action) we.getPojo();
                logger.debug("Received action from " + action.getUser());
                actionProcessModule.create(action);
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
        DataSyncPacketExtension.registerExtension();
        WhacketExtension.registerExtension(new ActionPacketTransformer());


        //data sync
        try {
            logger.info("CREATING NEW DATASYNC MODULE, CURRENT IS: " + dataSyncModule);
            if (dataSyncModule == null) {
                dataSyncModule = DataSyncModuleFactory.getDataSyncModule(DataSyncModuleFactory.LOCAL_STYLE);
            }

            // add listner in order to get callbacks on stuff that's happening
            dataSyncModule.addDataSyncListener(new IDataSyncListener() {

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
                    reply.setFrom(SCYHubComponent.this.getName() + "." + conf.getDatasyncExternalComponentHost());
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
        } catch (Exception exeption) {
            logger.error("Something was messed up during creation of dataSyncModule: ");
            exeption.printStackTrace();
        }
        // action processing
        actionProcessModule = new ActionProcessModule(conf.getSqlSpacesServerHost(), conf.getSqlSpacesServerPort());
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

    public IDataSyncModule getDataSyncModule() {
        return dataSyncModule;
    }

    public void setDataSyncModule(IDataSyncModule dataSyncModule) {
        this.dataSyncModule = dataSyncModule;
    }
}
