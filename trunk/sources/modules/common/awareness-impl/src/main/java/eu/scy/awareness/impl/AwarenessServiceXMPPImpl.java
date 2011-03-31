package eu.scy.awareness.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.FormField;
import org.jivesoftware.smackx.muc.Affiliate;
import org.jivesoftware.smackx.muc.DefaultParticipantStatusListener;
import org.jivesoftware.smackx.muc.DiscussionHistory;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.Occupant;
import org.jivesoftware.smackx.muc.RoomInfo;

import eu.scy.awareness.AwarenessServiceException;
import eu.scy.awareness.AwarenessUser;
import eu.scy.awareness.IAwarenessService;
import eu.scy.awareness.IAwarenessUser;
import eu.scy.awareness.event.AwarenessEvent;
import eu.scy.awareness.event.AwarenessPresenceEvent;
import eu.scy.awareness.event.AwarenessRosterEvent;
import eu.scy.awareness.event.IAwarePresenceEvent;
import eu.scy.awareness.event.IAwarenessEvent;
import eu.scy.awareness.event.IAwarenessMessageListener;
import eu.scy.awareness.event.IAwarenessPresenceListener;
import eu.scy.awareness.event.IAwarenessRosterEvent;
import eu.scy.awareness.event.IAwarenessRosterListener;
import eu.scy.awareness.tool.ChatPresenceToolEvent;
import eu.scy.awareness.tool.IChatPresenceToolEvent;
import eu.scy.awareness.tool.IChatPresenceToolListener;
import org.jivesoftware.smack.packet.PacketExtension;

public class AwarenessServiceXMPPImpl implements IAwarenessService, MessageListener {

    private String CONFERENCE_EXT = null;
    private final static Logger logger = Logger.getLogger(AwarenessServiceXMPPImpl.class.getName());
    private ConnectionConfiguration config;
    private ArrayList<IAwarenessPresenceListener> presenceListeners = new ArrayList<IAwarenessPresenceListener>();
    private List<IAwarenessMessageListener> messageListeners = new ArrayList<IAwarenessMessageListener>();
    private ArrayList<IAwarenessRosterListener> rosterListeners = new ArrayList<IAwarenessRosterListener>();
    private ArrayList<IChatPresenceToolListener> chatToolListeners = new ArrayList<IChatPresenceToolListener>();
    private ArrayList<IChatPresenceToolListener> presenceToolListeners = new ArrayList<IChatPresenceToolListener>();
    private Roster roster;
    public Map<String, MultiUserChat> joinedMUCRooms = new HashMap<String, MultiUserChat>();
    private XMPPConnection xmppConnection;

    public AwarenessServiceXMPPImpl() {
    }

    @Override
    public boolean isConnected() {
        if (xmppConnection != null) {
            return xmppConnection.isConnected();
        }

        return false;
    }

    public void setMUCConferenceExtension(String CONFERENCE_EXT) {
        this.CONFERENCE_EXT = "@" + CONFERENCE_EXT;
    }

    public String getMUCConferenceExtension() {
        return this.CONFERENCE_EXT;
    }

    @Override
    public void sendMessage(IAwarenessUser recipient, String message)
            throws AwarenessServiceException {
        Chat chat = xmppConnection.getChatManager().createChat(recipient.getJid(),
                (MessageListener) this);
        try {
            chat.sendMessage(message);
        } catch (XMPPException e) {
            logger.error("sendMessage: XMPPException: " + e);
        }
    }

    @Override
    public void sendMUCMessage(String ELOUri, String message)
            throws AwarenessServiceException {

        MultiUserChat muc = joinedMUCRooms.get(ELOUri);

        if (muc.isJoined()) {

            logger.debug("sendMESSAGE Awareness Service isJoined:" + muc.isJoined());
            Message createMessage = muc.createMessage();

            createMessage.setBody(message);
            createMessage.setProperty("timestamp", System.currentTimeMillis());
            try {
                muc.sendMessage(createMessage);
                // System.out.println("message sent for MUC: " + ELOUri);
            } catch (XMPPException e) {
                logger.error("sendMessage: XMPPException MUC: " + e);
                e.printStackTrace();
            }
        }

    }

    private void processPresence(Presence presence) {
        // System.out.println("presence of " + presence.toString());
        for (IAwarenessPresenceListener presenceListener : presenceListeners) {
            if (presenceListener != null) {


                String roomId = StringUtils.parseName(presence.getFrom());


                IAwarenessUser aw = new AwarenessUser();
                aw.setJid(presence.getTo());
                aw.setPresence(presence.getType().toString());

                // System.out.println("awareness user: " + aw);
                IAwarePresenceEvent presenceEvent = new AwarenessPresenceEvent(
                        AwarenessServiceXMPPImpl.this, aw,
                        "updated from awareness service", presence.getType().toString(), presence.getStatus());
                presenceEvent.setRoomId(roomId);
                presenceListener.handleAwarenessPresenceEvent(presenceEvent);
            }
        }
    }

    @Override
    public void processMessage(Chat chat, Message message) {
        if (message.getType() == Message.Type.chat) {
            processMessage(message, chat.getParticipant());
        }
    }

    protected void processMessage(final Message message,
            final String participant) {

        logger.debug("processMessage: " + participant + " says: "
                + message.getBody());

        String roomId = StringUtils.parseName(message.getFrom());
        long timestamp = 0;
        Object o = message.getProperty("timestamp");
        if (o != null) {
            Long t = (Long) o;
            timestamp = t.longValue();
        } else {
            timestamp = System.currentTimeMillis();
        }

        IAwarenessUser aw = new AwarenessUser();
        aw.setJid(participant);
        IAwarenessEvent awarenessEvent = new AwarenessEvent(this, aw, message.getBody(), timestamp);
        awarenessEvent.setRoomId(roomId);

        for (IAwarenessMessageListener al : messageListeners) {
            if (al != null) {
                try {
                    al.handleAwarenessMessageEvent(awarenessEvent);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @Override
    public void addAwarenessMessageListener(
            IAwarenessMessageListener awarenessMessageListener) {
        messageListeners.add(awarenessMessageListener);
    }

    @Override
    public void addAwarenessRosterListener(
            IAwarenessRosterListener awarenessListListener) {
        rosterListeners.add(awarenessListListener);
    }

    @Override
    public void addAwarenessPresenceListener(
            IAwarenessPresenceListener awarenessPresenceListener) {
        presenceListeners.add(awarenessPresenceListener);
    }

    public void removeAwarenessPresenceListener(
            IAwarenessPresenceListener awarenessPresenceListener) {
        presenceListeners.remove(awarenessPresenceListener);
    }

    @Override
    public void addBuddy(String buddyName) throws AwarenessServiceException {
        roster = this.xmppConnection.getRoster();
        Collection<RosterEntry> rosterEntries = roster.getEntries();
        try {
            roster.createEntry(buddyName, buddyName,
                    new String[]{"everybody"});
        } catch (XMPPException e) {
            logger.error("addBuddy: XMPPException: " + e);
        }
    }

    @Override
    public void removeBuddy(String buddyName) throws AwarenessServiceException {
        RosterEntry entry = xmppConnection.getRoster().getEntry(buddyName);
        try {
            xmppConnection.getRoster().removeEntry(entry);
        } catch (XMPPException e) {
            logger.error("removeBuddy: XMPPException: " + e);
        }
        Presence unsubbed = new Presence(Presence.Type.unsubscribed);
        unsubbed.setTo(buddyName);
        this.xmppConnection.sendPacket(unsubbed);

    }

    @Override
    public ArrayList<IAwarenessUser> getBuddies()
            throws AwarenessServiceException {
        roster = this.xmppConnection.getRoster();
        if (roster != null || !roster.getEntries().isEmpty()) {
            Collection<RosterEntry> rosterEntries = roster.getEntries();
            ArrayList<IAwarenessUser> buddies = new ArrayList<IAwarenessUser>();
            for (RosterEntry buddy : rosterEntries) {
                IAwarenessUser au = new AwarenessUser();
                au.setJid(buddy.getUser());
                au.setNickName(buddy.getName());
                au.setPresence(roster.getPresence(buddy.getUser()).toString());
                buddies.add(au);
            }
            return buddies;
        } else {
            return null;
        }
    }

    @Override
    public void disconnect() {
        this.xmppConnection.disconnect();
    }

    protected Roster getRoster() {
        return this.xmppConnection.getRoster();
    }

    protected ChatManager getChatManager() {
        return this.xmppConnection.getChatManager();
    }

    @Override
    public void init(XMPPConnection connection) {
        this.xmppConnection = connection;
        if (getRoster() == null) {
            return;
        }

        getRoster().setSubscriptionMode(Roster.SubscriptionMode.accept_all);
        getRoster().addRosterListener(new RosterListener() {

            @Override
            public void entriesAdded(Collection<String> addresses) {
                logger.debug("init: entriesAdded: " + addresses);

                for (IAwarenessRosterListener rosterListener : rosterListeners) {
                    if (rosterListener != null) {
                        IAwarenessRosterEvent rosterEvent = new AwarenessRosterEvent(
                                AwarenessServiceXMPPImpl.this,
                                AwarenessServiceXMPPImpl.this.xmppConnection.getUser(), IAwarenessRosterEvent.ADD,
                                addresses);
                        rosterListener.handleAwarenessRosterEvent(rosterEvent);
                    }
                }

            }

            @Override
            public void entriesDeleted(Collection<String> addresses) {
                logger.debug("init: entriesDeleted: " + addresses);
                for (IAwarenessRosterListener rosterListener : rosterListeners) {
                    if (rosterListener != null) {
                        IAwarenessRosterEvent rosterEvent = new AwarenessRosterEvent(
                                AwarenessServiceXMPPImpl.this,
                                AwarenessServiceXMPPImpl.this.xmppConnection.getUser(),
                                IAwarenessRosterEvent.REMOVE, addresses);
                        rosterListener.handleAwarenessRosterEvent(rosterEvent);
                    }
                }
            }

            @Override
            public void entriesUpdated(Collection<String> addresses) {
                logger.debug("init: entriesUpdated: " + addresses);
                for (IAwarenessRosterListener rosterListener : rosterListeners) {
                    if (rosterListener != null) {
                        IAwarenessRosterEvent rosterEvent = new AwarenessRosterEvent(
                                AwarenessServiceXMPPImpl.this,
                                AwarenessServiceXMPPImpl.this.xmppConnection.getUser(),
                                IAwarenessRosterEvent.UPDATED, addresses);
                        rosterListener.handleAwarenessRosterEvent(rosterEvent);
                    }
                }
            }

            @Override
            public void presenceChanged(Presence presence) {
                logger.debug("init: presenceChanged: " + presence);
                if (presence == null) {
                    return;
                }

                for (IAwarenessPresenceListener presenceListener : presenceListeners) {
                    if (presenceListener != null) {

                        IAwarenessUser aw = new AwarenessUser();
                        aw.setJid(presence.getFrom());
                        aw.setPresence(presence.getType().toString());

                        IAwarePresenceEvent presenceEvent = new AwarenessPresenceEvent(
                                AwarenessServiceXMPPImpl.this, aw,
                                "updated from awareness service", presence.getType().toString(), presence.getStatus());
                        presenceListener.handleAwarenessPresenceEvent(presenceEvent);
                    }
                }

            }
        });
    }

    public boolean hasJoinedRoom(String ELOUri, String user) throws AwarenessServiceException {
        Iterator<String> jrooms = MultiUserChat.getJoinedRooms(xmppConnection,
                user);

        for (Iterator i = jrooms; jrooms.hasNext();) {
            String room = (String) i.next();
            if (room.equals(ELOUri)) {
                return true;
            }
        }

        return false;
    }

    public boolean doesRoomExist(String ELOUri) {
        RoomInfo info;
        try {
            info = MultiUserChat.getRoomInfo(xmppConnection, ELOUri + CONFERENCE_EXT);
        } catch (XMPPException e) {
            // e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public void setStatus(String status) throws AwarenessServiceException {
        Presence presence = getRoster().getPresence(xmppConnection.getUser());
        presence.setStatus(status);

    }

    @Override
    public void setPresence(String presenceString)
            throws AwarenessServiceException {
        // Presence p = getRoster().getPresence(xmppConnection.getUser());
    }

    @Override
    public void addChatToolListener(IChatPresenceToolListener listener) {
        chatToolListeners.add(listener);

    }

    @Override
    public void addPresenceToolListener(IChatPresenceToolListener listener) {
        presenceToolListeners.add(listener);
    }

    public void updateChatTool(List<IAwarenessUser> users) {
        IChatPresenceToolEvent icpte = new ChatPresenceToolEvent(users);
        icpte.setMessage("presence tool calling with updates");
        for (IChatPresenceToolListener icptl : presenceToolListeners) {
            if (icptl != null) {
                icptl.handleChatPresenceToolEvent(icpte);
            }
        }
    }

    public void updatePresenceTool(List<IAwarenessUser> users) {
        logger.debug("Received something from Chattool");
        IChatPresenceToolEvent icpte = new ChatPresenceToolEvent(users);
        icpte.setMessage("chat tool calling with updates");
        for (IChatPresenceToolListener icptl : chatToolListeners) {
            if (icptl != null) {
                icptl.handleChatPresenceToolEvent(icpte);
            }
        }
    }

    public void destoryMUCRoom(String ELOUri) throws AwarenessServiceException {
        if (doesRoomExist(ELOUri)) {
            MultiUserChat muc = new MultiUserChat(xmppConnection, ELOUri
                    + CONFERENCE_EXT);
            try {
                muc.destroy("hate this room", null);
            } catch (XMPPException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void joinMUCRoom(String ELOUri) throws AwarenessServiceException {
        // first look of the muc to see if it as been create first

        if (ELOUri != null) {

            MultiUserChat muc = new MultiUserChat(xmppConnection, ELOUri + CONFERENCE_EXT);
            DiscussionHistory history = new DiscussionHistory();
            try {
                muc.addParticipantListener(new PacketListener() {

                    @Override
                    public void processPacket(Packet packet) {
                        if (packet instanceof Packet) {
                            Presence presence = (Presence) packet;
                            processPresence(presence);
                        }
                    }
                });
                muc.addMessageListener(new PacketListener() {

                    @Override
                    public void processPacket(Packet packet) {
                        logger.debug("proessing packact in awareness service ");
                        if (packet instanceof Message) {
                            Message message = (Message) packet;
                            logger.debug("this message is from:" + message.getFrom() + " to: " + message.getTo() + "with body: " + message.getBody());
                            String nickName = StringUtils.parseResource(message.getFrom());
                            if (!nickName.isEmpty()) {
                                // we only process messages from other buddies
                                processMessage(message, nickName);
                            }
                        }
                    }
                });
                muc.addParticipantStatusListener(new AwarenessParticipantListener());
                // first check if the room exists
                if (doesRoomExist(ELOUri)) {
                    // am i joined
                    if (hasJoinedRoom(ELOUri, xmppConnection.getUser()) == false) {
                        muc.join(xmppConnection.getUser(), null, history, SmackConfiguration.getPacketReplyTimeout());
                    }

                } else {
                    // create it
                    // we need to create
                    muc.create(xmppConnection.getUser());


                    // Get the the room's configuration form
                    Form form = muc.getConfigurationForm();

                    logger.debug(form.getInstructions());

                    // Create a new form to submit based on the original form
                    Form submitForm = form.createAnswerForm();
                    // Add default answers to the form to submit
                    for (Iterator fields = form.getFields(); fields.hasNext();) {
                        FormField field = (FormField) fields.next();
                        if (!FormField.TYPE_HIDDEN.equals(field.getType()) && field.getVariable() != null) {
                            // Sets the default value as the answer
                            submitForm.setDefaultAnswer(field.getVariable());
                        }
                    }
                    // Sets the new owner of the room
                    // List owners = new ArrayList();
                    // owners.add("djed11");
                    submitForm.setAnswer("muc#roomconfig_persistentroom", true);
                    submitForm.setAnswer("muc#roomconfig_enablelogging", true);
                    // Send the completed form (with default values) to the
                    // server to configure the room
                    muc.sendConfigurationForm(submitForm);
                    // if( joinedMUCRooms.contains(ELOUri) == false )
                    // joinedMUCRooms.add(ELOUri);
                }
            } catch (XMPPException e) {
                e.printStackTrace();
                return;
            }

            joinedMUCRooms.put(ELOUri, muc);

        }

    }

    @Override
    public List<IAwarenessUser> getMUCBuddies(String ELOUri)
            throws AwarenessServiceException {
        if (ELOUri != null) {
            List<IAwarenessUser> users = new ArrayList<IAwarenessUser>();
            MultiUserChat muc = new MultiUserChat(xmppConnection, ELOUri
                    + CONFERENCE_EXT);
            Iterator<String> occupants = muc.getOccupants();
            try {
                Collection<Occupant> participants = muc.getParticipants();
                for (Occupant occupant : participants) {
                    IAwarenessUser user = new AwarenessUser();
                    user.setNickName(occupant.getNick());
                    user.setJid(occupant.getJid());
                    users.add(user);
                }

                Collection<Affiliate> members = muc.getMembers();
                for (Affiliate affiliate : members) {
                    IAwarenessUser user = new AwarenessUser();
                    user.setNickName(affiliate.getNick());
                    user.setJid(affiliate.getJid());
                    users.add(user);
                }

                Collection<Occupant> moderators = muc.getModerators();
                for (Occupant occupant : moderators) {
                    IAwarenessUser user = new AwarenessUser();
                    user.setNickName(occupant.getNick());
                    user.setJid(occupant.getJid());
                    users.add(user);
                }

                Collection<Affiliate> admins = muc.getAdmins();
                for (Affiliate affiliate : admins) {
                    IAwarenessUser user = new AwarenessUser();
                    user.setJid(affiliate.getNick());
                    user.setNickName(affiliate.getJid());
                    users.add(user);
                }
                // System.out.println();
            } catch (XMPPException e) {
                e.printStackTrace();
            }
            return users;
        }
        return null;
    }

    @Override
    public void addBuddyToMUC(IAwarenessUser buddy, String ELOUri) throws AwarenessServiceException {
        if (ELOUri != null) {

            // check to see if the room exists
            if (this.doesRoomExist(ELOUri)) {
                MultiUserChat muc = new MultiUserChat(xmppConnection, ELOUri);
                try {
                    muc.join(buddy.getJid());
                } catch (XMPPException e) {
                    e.printStackTrace();
                }
            } else {
                // the room does not exist.
            }

        }

    }

    @Override
    public void removeBuddyFromMUC(IAwarenessUser buddy, String ELOUri) throws AwarenessServiceException {
        if (ELOUri != null) {
            MultiUserChat muc = new MultiUserChat(xmppConnection, ELOUri);
            try {
                muc.kickParticipant(buddy.getJid(), "had to go");
            } catch (XMPPException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public MultiUserChat getMultiUserChat(String ELOUri) throws AwarenessServiceException {

        if (!joinedMUCRooms.isEmpty()) {
            return joinedMUCRooms.get(ELOUri);
        }

        return null;
    }

    @Override
    public XMPPConnection getConnection() {
        return this.xmppConnection;
    }

    public class AwarenessParticipantListener extends DefaultParticipantStatusListener {

        public void banned(String arg0, String arg1, String arg2) {
        }

        public void joined(String participant) {
            String roomId = StringUtils.parseName(participant);
            participant = participant.substring(participant.indexOf("/") + 1);

            logger.debug("PARSED room ID for AwarenessParticipantListener: " + participant + " and roomID: " + roomId);

            for (IAwarenessRosterListener rosterListener : rosterListeners) {
                if (rosterListener != null) {

                    List<String> addresses = new ArrayList<String>();
                    addresses.add(participant);
                    if (!participant.equals(AwarenessServiceXMPPImpl.this.getConnection().getUser())) {
                        IAwarenessRosterEvent rosterEvent = new AwarenessRosterEvent(this, AwarenessServiceXMPPImpl.this.getConnection().getUser(), IAwarenessRosterEvent.ADD, addresses);
                        rosterEvent.setRoomId(roomId);
                        rosterListener.handleAwarenessRosterEvent(rosterEvent);
                    }
                }
            }
        }

        public void kicked(String participant, String arg1, String arg2) {
            removeParticipant(participant);
        }

        private void removeParticipant(String participant) {
            // System.out.println("AwarenessServiceXMPPImpl.AwarenessParticipantListener.removeParticipant() " + participant);
            String roomId = StringUtils.parseName(participant);

            participant = participant.substring(participant.indexOf("/") + 1);
            for (IAwarenessRosterListener rosterListener : rosterListeners) {
                if (rosterListener != null) {

                    List<String> addresses = new ArrayList<String>();
                    addresses.add(participant);
                    IAwarenessRosterEvent rosterEvent = new AwarenessRosterEvent(this, AwarenessServiceXMPPImpl.this.getConnection().getUser(), IAwarenessRosterEvent.REMOVE, addresses);
                    rosterEvent.setRoomId(roomId);
                    rosterListener.handleAwarenessRosterEvent(rosterEvent);
                }
            }
        }

        public void left(String participant) {
            removeParticipant(participant);
        }

        public void nicknameChanged(String arg0, String arg1) {
        }
    }
}
