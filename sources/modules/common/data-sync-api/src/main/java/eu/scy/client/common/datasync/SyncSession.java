package eu.scy.client.common.datasync;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smackx.muc.DefaultParticipantStatusListener;
import org.jivesoftware.smackx.muc.MultiUserChat;

import eu.scy.common.datasync.ISyncAction;
import eu.scy.common.datasync.ISyncAction.Type;
import eu.scy.common.datasync.ISyncObject;
import eu.scy.common.datasync.SyncAction;
import eu.scy.common.datasync.SyncActionPacketTransformer;
import eu.scy.common.message.DataSyncMessagePacketTransformer;
import eu.scy.common.message.SyncMessage;
import eu.scy.common.message.SyncMessage.Event;
import eu.scy.common.message.SyncMessage.Response;
import eu.scy.common.smack.SmacketExtension;
import eu.scy.common.smack.SmacketExtensionProvider;

public class SyncSession implements ISyncSession {

    private static final Logger logger = Logger.getLogger(SyncSession.class);

    private MultiUserChat muc;

    private Connection connection;

    private List<ISyncListener> listeners;

    private List<CollaboratorStatusListener> collabListeners;

    private String toolid;

    private String mucRoomId;

    private BlockingQueue<Packet> queryQueue;

    private boolean fetchState;

    private PacketListener syncPacketListener;

    private PacketListener queryAllListener;

    private PacketFilter syncPacketFilter;

    private PacketFilter queryAllFilter;

    public SyncSession(Connection xmppConnection, MultiUserChat muc, String toolid, ISyncListener listener) throws DataSyncException {
        this(xmppConnection, muc, toolid, listener, false);
    }

    public SyncSession(Connection xmppConnection, MultiUserChat muc, ISyncListener listener) throws DataSyncException {
        this(xmppConnection, muc, null, listener, false);
    }


    public SyncSession(Connection xmppConnection, MultiUserChat muc, String toolid, ISyncListener listener, boolean fetchState) throws DataSyncException {
        this.toolid = toolid;
        this.fetchState = fetchState;
        this.listeners = new ArrayList<ISyncListener>();
        this.collabListeners = new ArrayList<CollaboratorStatusListener>();
        this.connection = xmppConnection;
        this.muc = muc;

        mucRoomId = muc.getRoom().substring(0, muc.getRoom().indexOf("@"));

        listeners.add(listener);

        final SyncActionPacketTransformer syncActionTransformer = new SyncActionPacketTransformer();
        final DataSyncMessagePacketTransformer syncMessageTransformer = new DataSyncMessagePacketTransformer();

        queryQueue = new LinkedBlockingQueue<Packet>();

        // add extenison provider
        SmacketExtensionProvider.registerExtension(syncActionTransformer);
        SmacketExtensionProvider.registerExtension(syncMessageTransformer);

        ProviderManager providerManager = ProviderManager.getInstance();
        SmacketExtensionProvider extensionProvider = new SmacketExtensionProvider();
        providerManager.addExtensionProvider(syncActionTransformer.getElementname(), syncActionTransformer.getNamespace(), extensionProvider);
        providerManager.addExtensionProvider(syncMessageTransformer.getElementname(), syncMessageTransformer.getNamespace(), extensionProvider);

        syncPacketListener = new PacketListener() {
            
            @Override
            public void processPacket(Packet packet) {
                if (packet.getExtension(syncActionTransformer.getElementname(), syncActionTransformer.getNamespace()) != null) {
                    SmacketExtension extension = (SmacketExtension) packet.getExtension(syncActionTransformer.getElementname(), syncActionTransformer.getNamespace());
                    SmacketExtension se = (SmacketExtension) extension;
                    SyncAction syncAction = (SyncAction) se.getTransformer().getObject();
                    synchronized (listeners) {
                        for (ISyncListener listener : listeners) {
                            switch (syncAction.getType()) {
                                case add:
                                    listener.syncObjectAdded(syncAction.getSyncObject());
                                    break;
                                case change:
                                    listener.syncObjectChanged(syncAction.getSyncObject());
                                    break;
                                case remove:
                                    listener.syncObjectRemoved(syncAction.getSyncObject());
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                }
            }
        };
        syncPacketFilter = new PacketFilter() {
            
            @Override
            public boolean accept(Packet packet) {
                if (packet.getFrom().startsWith(mucRoomId)) {
                    if (packet.getExtension(syncActionTransformer.getElementname(), syncActionTransformer.getNamespace()) != null) {
                        return true;
                    }
                }
                return false;
            }
        };
        
        queryAllListener = new PacketListener() {
            
            @Override
            public void processPacket(Packet packet) {
                if (packet.getExtension(syncMessageTransformer.getElementname(), syncMessageTransformer.getNamespace()) != null) {
                    try {
                        logger.debug("Putting packet with id " + packet.getPacketID() + " into query queue.");
                        queryQueue.put(packet);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        queryAllFilter = new PacketFilter() {
            
            @Override
            public boolean accept(Packet packet) {
                if (packet.getFrom().startsWith("datasynclistener")) { // this string should be made generic somehow
                    if (packet.getExtension(syncMessageTransformer.getElementname(), syncMessageTransformer.getNamespace()) != null) {
                        PacketExtension extension = packet.getExtension(syncMessageTransformer.getElementname(), syncMessageTransformer.getNamespace());
                        SmacketExtension se = (SmacketExtension) extension;
                        SyncMessage message = (SyncMessage) se.getTransformer().getObject();
                        if (message.getType().equals(eu.scy.common.message.SyncMessage.Type.answer) && message.getEvent().equals(Event.queryall) && message.getSessionId().equals(getId())) {
                            logger.debug("Accepting query answer packet with id " + packet.getPacketID() + " for session " + message.getSessionId());
                            return true;
                        }
                    }
                }
                return false;
            }
        };
        xmppConnection.addPacketListener(syncPacketListener, syncPacketFilter);

        if (fetchState) {
            fetchState(toolid);
        }

        muc.addParticipantStatusListener(new DefaultParticipantStatusListener() {

            @Override
            public void joined(String participant) {
                for (CollaboratorStatusListener csl : collabListeners) {
                    csl.joined(getSimpleName(participant));
                }
            }

            @Override
            public void left(String participant) {
                for (CollaboratorStatusListener csl : collabListeners) {
                    csl.left(getSimpleName(participant));
                }
            }

        });
        muc.addParticipantListener(new PacketListener() {

            @Override
            public void processPacket(Packet packet) {
                if (packet instanceof Packet) {
                    Presence presence = (Presence) packet;
                    for (CollaboratorStatusListener csl : collabListeners) {
                        if (presence.isAvailable()) {
                            csl.wentOnline(getSimpleName(presence.getFrom()));
                        } else {
                            csl.wentOffline(getSimpleName(presence.getFrom()));
                        }
                    }
                }
            }
        });
    }

    private String getSimpleName(String syncSessionJid) {
        syncSessionJid = syncSessionJid.substring(syncSessionJid.indexOf('/') + 1);
        syncSessionJid = syncSessionJid.substring(0, syncSessionJid.indexOf('@'));
        return syncSessionJid;
    }

    @Override
    public void addSyncObject(ISyncObject syncObject) {
        syncObject.setCreator(connection.getUser());
        sendSyncAction(Type.add, syncObject);
    }

    @Override
    public void changeSyncObject(ISyncObject syncObject) {
        syncObject.setLastModificator(muc.getNickname());
        syncObject.setLastModificationTime(System.currentTimeMillis());
        sendSyncAction(Type.change, syncObject);
    }

    @Override
    public void removeSyncObject(ISyncObject syncObject) {
        sendSyncAction(Type.remove, syncObject);
    }

    private void sendSyncAction(Type type, ISyncObject syncObject) {
        Message message = muc.createMessage();
        message.setFrom(connection.getUser());

        ISyncAction syncAction = new SyncAction(this.getId(), connection.getUser(), type, syncObject);

        SyncActionPacketTransformer syncActionTransformer = new SyncActionPacketTransformer(syncAction);
        message.addExtension(new SmacketExtension(syncActionTransformer));

        try {
            muc.sendMessage(message);
        } catch (XMPPException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<ISyncObject> getAllSyncObjects(String toolId) throws DataSyncException {
        return getAllSyncObjects(toolId, 5, TimeUnit.SECONDS);
    }

    @Override
    public List<ISyncObject> getAllSyncObjects(String toolId, int time, TimeUnit unit) throws DataSyncException {
        List<ISyncObject> list = new LinkedList<ISyncObject>();
        SyncMessage request = new SyncMessage(eu.scy.common.message.SyncMessage.Type.request);
        request.setUserId(connection.getUser());
        request.setToolId(toolId);
        request.setEvent(Event.queryall);
        request.setSessionId(muc.getRoom());

        DataSyncMessagePacketTransformer syncMessageTransformer = new DataSyncMessagePacketTransformer();
        syncMessageTransformer.setObject(request);

        Message sentPacket = muc.createMessage();
        sentPacket.setFrom(connection.getUser());

        SmacketExtension extension = new SmacketExtension(syncMessageTransformer);
        sentPacket.addExtension(extension);

        try {
            connection.addPacketListener(queryAllListener, queryAllFilter);
            Packet packet = null; 
            int tries = 5;
            while (tries > 0) {
                logger.debug("Sending request for querying all objects from session " + getId());
                muc.sendMessage(sentPacket);
                
                packet = queryQueue.poll(time, unit);
                logger.debug("Polled packet from queue in " + (6-tries) + " is " + packet);
                if (packet != null) {
                    extension = (SmacketExtension) packet.getExtension(syncMessageTransformer.getElementname(), syncMessageTransformer.getNamespace());
                    SyncMessage reply = (SyncMessage) extension.getTransformer().getObject();
                    if (reply != null && Response.success.equals(reply.getResponse())) {
                        logger.debug("Successfully returning objects from received packet with id " + packet.getPacketID());
                        return reply.getSyncObjects();
                    }
                }
                logger.debug("Try " + (6-tries) + " failed. One more time?");
                tries--;
            }
            if (packet == null) {
                throw new DataSyncException("Exception during querying session data");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (XMPPException e) {
            e.printStackTrace();
        } finally {
            connection.removePacketListener(queryAllListener);
        }

        return list;
    }

    @Override
    public ISyncObject getSyncObject(String id) {
        ISyncObject syncObject;
        // TODO request a syncobject with a given id in a session from datasyncmodule
        // for this session
        return null;
    }

    @Override
    public void addSyncListener(ISyncListener listener) {
        synchronized (listeners) {
            this.listeners.add(listener);
        }
    }

    @Override
    public void removeSyncListener(ISyncListener listener) {
        synchronized (listeners) {
            this.listeners.remove(listener);
        }
    }

    @Override
    public String getId() {
        return muc.getRoom();
    }

    @Override
    public void leaveSession() {
        muc.leave();
        synchronized (listeners) {
            listeners.clear();
        }
    }

    @Override
    public void addCollaboratorStatusListener(CollaboratorStatusListener listener) {
        this.collabListeners.add(listener);
    }

    @Override
    public void removeCollaboratorStatusListener(CollaboratorStatusListener listener) {
        this.collabListeners.remove(listener);
    }

    @Override
    public String getUsername() {
        if (connection != null) {
            return connection.getUser();
        }
        return null;
    }

    @Override
    public void refreshOnlineCollaborators() {
        Iterator<String> it = muc.getOccupants();
        while (it.hasNext()) {
            for (CollaboratorStatusListener csl : collabListeners) {
                csl.wentOnline(getSimpleName(it.next()));
            }
        }
    }

    @Override
    public List<ISyncObject> getAllSyncObjects() throws DataSyncException {
        return getAllSyncObjects(toolid);
    }

    @Override
    public List<ISyncObject> getAllSyncObjects(int time, TimeUnit unit) throws DataSyncException {
        return getAllSyncObjects(toolid, time, unit);
    }

    @Override
    public void fetchState(String toolId) {
        try {
            List<ISyncObject> syncObjects = getAllSyncObjects(toolId);
            // System.out.println(syncObjects);
            Collections.sort(syncObjects, new Comparator<ISyncObject>() {

                @Override
                public int compare(ISyncObject o1, ISyncObject o2) {
                    return (int) (o1.getCreationTime() - o2.getCreationTime());
                }
            });
            for (ISyncObject syncObject : syncObjects) {
                synchronized (listeners) {
                    for (ISyncListener l : listeners) {
                        l.syncObjectAdded(syncObject);
                    }
                }
            }
        } catch (DataSyncException ex) {
            logger.error(ex);
        }
    }

    @Override
    public boolean isConnected() {
        return muc.isJoined();
    }
}
