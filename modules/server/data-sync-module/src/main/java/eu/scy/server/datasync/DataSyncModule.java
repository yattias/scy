/**
 * 
 */
package eu.scy.server.datasync;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Callback;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import info.collide.sqlspaces.commons.User;
import info.collide.sqlspaces.commons.Callback.Command;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.ProviderManager;
import org.xmpp.component.Component;
import org.xmpp.packet.Message;
import org.xmpp.packet.Packet;

import eu.scy.common.configuration.Configuration;
import eu.scy.common.datasync.SyncAction;
import eu.scy.common.datasync.SyncActionPacketTransformer;
import eu.scy.common.message.DataSyncMessagePacketTransformer;
import eu.scy.common.message.SyncMessage;
import eu.scy.common.message.SyncMessage.Event;
import eu.scy.common.message.SyncMessage.Response;
import eu.scy.common.message.SyncMessage.Type;
import eu.scy.common.packetextension.SCYPacketTransformer;
import eu.scy.common.smack.SmacketExtension;
import eu.scy.common.smack.SmacketExtensionProvider;
import eu.scy.commons.whack.WhacketExtension;
import eu.scy.scyhub.SCYHubModule;

/**
 * @author giemza
 * 
 */
public class DataSyncModule extends SCYHubModule {

    private static final Logger logger = Logger.getLogger(DataSyncModule.class.getName());

    // sessionid -> sessionbridge
    private Map<String, DataSyncSessionBridge> bridges;

    private XMPPConnection connection;

    private TupleSpace commandSpace;

    private String COMMAND_SPACE = "command";

    /**
     * @param scyhub
     */
    public DataSyncModule(Component scyhub) {
        super(scyhub, new DataSyncMessagePacketTransformer());

        bridges = new HashMap<String, DataSyncSessionBridge>();

        try {
            ConnectionConfiguration cc = new ConnectionConfiguration(Configuration.getInstance().getOpenFireHost(), Configuration.getInstance().getOpenFirePort());
            connection = new XMPPConnection(cc);
            connection.connect();
            connection.login("datasynclistener", "datasync");
            connection.addConnectionListener(new DataSyncConnectionListener(connection));

            final SyncActionPacketTransformer sapt = new SyncActionPacketTransformer();

            // add extenison provider
            SmacketExtensionProvider.registerExtension(sapt);

            ProviderManager providerManager = ProviderManager.getInstance();
            providerManager.addExtensionProvider(sapt.getElementname(), sapt.getNamespace(), new SmacketExtensionProvider());

            DataSyncPacketFilterListener packetFilterListener = new DataSyncPacketFilterListener(bridges, new SyncActionPacketTransformer());
            connection.addPacketListener(packetFilterListener, packetFilterListener);

            String host = Configuration.getInstance().getSQLSpacesServerHost();
            int port = Configuration.getInstance().getSQLSpacesServerPort();

            try {
                commandSpace = new TupleSpace(new User(DataSyncModule.class.getSimpleName()), host, port, false, false, COMMAND_SPACE);
                final Tuple tupleTemplate = new Tuple(String.class, "datasync", Field.createWildCardField());
                commandSpace.eventRegister(Command.WRITE, tupleTemplate, new Callback() {

                    @Override
                    public void call(final Command cmd, final int seqnum, final Tuple afterTuple, final Tuple beforeTuple) {
                        final String id = afterTuple.getField(0).getValue().toString();
                        final String service = afterTuple.getField(2).getValue().toString();
                        if (service.equals("create_session")) {
                            logger.debug("Got a create mucsession request with id " + id);
                            final SyncMessage msg = createSession();
                            final String mucId = msg.getMessage();
                            logger.debug("Answering id " + id + " with mucid " + mucId);
                            try {
                                commandSpace.write(new Tuple(id, mucId));
                            } catch (TupleSpaceException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, false);
            } catch (TupleSpaceException e) {
                e.printStackTrace();
            }

            logger.debug("Notificator initialised on " + host + ":" + port);

        } catch (XMPPException e) {
            e.printStackTrace();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see eu.scy.scyhub.SCYHubModule#process(org.xmpp.packet.Packet)
     */
    @Override
    protected void process(Packet packet, WhacketExtension extension) {
        logger.debug("Received packet with DataSyncCommandExtension");
        Object pojo = extension.getPojo();

        if (pojo instanceof SyncMessage) {
            SyncMessage command = (SyncMessage) pojo;
            logger.debug("Command: " + command.getEvent() + " " + command.getUserId() + " " + command.getToolId());

            SyncMessage response = createSession();

            // we create the response message, add the extension and send it
            // with the same id to the client
            Message responseMessage = new Message();

            responseMessage.addExtension(new WhacketExtension(transformer.getElementname(), transformer.getNamespace(), response));
            responseMessage.setTo(packet.getFrom());
            responseMessage.setFrom(Configuration.getInstance().getSCYHubName() + "." + Configuration.getInstance().getOpenFireHost());
            responseMessage.setID(packet.getID());

            send(responseMessage);
        }
    }

    private SyncMessage createSession() {
        String sessionId = UUID.randomUUID().toString() + "@syncsessions." + Configuration.getInstance().getOpenFireHost();

        // create a session logger with the random id
        DataSyncSessionBridge dssl = new DataSyncSessionBridge(sessionId);
        bridges.put(sessionId, dssl);

        // response to client
        SyncMessage response = new SyncMessage(Type.answer);
        try {
            // first check if connection is still alive
            if (!connection.isConnected()) {
                connection.connect();
            }
            // try to connect the logger
            dssl.connect(connection);

            // if everything is okay we return success
            response.setEvent(Event.create);
            response.setResponse(Response.success);
            response.setMessage(sessionId);
        } catch (Exception e) {
            logger.error("Error while creating DataSyncSessionLogger for session " + sessionId, e);
            // if not we send a failure
            response.setEvent(Event.create);
            response.setResponse(Response.failure);
        }
        return response;
    }

    private class DataSyncPacketFilterListener implements PacketFilter, PacketListener {

        private Map<String, DataSyncSessionBridge> bridges;

        private SCYPacketTransformer transformer;

        public DataSyncPacketFilterListener(Map<String, DataSyncSessionBridge> bridges, SCYPacketTransformer transformer) {
            this.bridges = bridges;
            this.transformer = transformer;
        }

        @Override
        public boolean accept(org.jivesoftware.smack.packet.Packet packet) {
            return packet.getExtension(transformer.getElementname(), transformer.getNamespace()) != null;
        }

        @Override
        public void processPacket(org.jivesoftware.smack.packet.Packet packet) {
            PacketExtension packetExtension = packet.getExtension(transformer.getElementname(), transformer.getNamespace());
            if (packetExtension != null && packetExtension instanceof SmacketExtension) {
                SmacketExtension extension = (SmacketExtension) packetExtension;

                SyncAction action = (SyncAction) extension.getTransformer().getObject();
                DataSyncSessionBridge bridge = bridges.get(action.getSessionId());

                if (bridge != null) {
                    bridge.process(action);
                }
            }
        }

    }

    @Override
    public void shutdown() {
        for (DataSyncSessionBridge bridge : bridges.values()) {
            bridge.shutdown();
        }
        connection.disconnect();
    }

    class DataSyncConnectionListener implements ConnectionListener {

        private XMPPConnection connection;

        public DataSyncConnectionListener(XMPPConnection connection) {
            this.connection = connection;
        }

        @Override
        public void connectionClosed() {}

        @Override
        public void connectionClosedOnError(Exception e) {
            logger.debug("XMPPConnection closed. Reconnecting...");
            try {
                connection.connect();
                logger.debug("Reconncetion successful!");
            } catch (XMPPException ex) {
                logger.debug("Could not reconnect", ex);
            }
        }

        @Override
        public void reconnectingIn(int seconds) {}

        @Override
        public void reconnectionFailed(Exception e) {
            logger.debug("Reconnection to server failed: ", e);
        }

        @Override
        public void reconnectionSuccessful() {
            logger.debug("Reconnection sucessful!");
        }

    }
}
