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
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.FormField;
import org.jivesoftware.smackx.muc.Affiliate;
import org.jivesoftware.smackx.muc.MultiUserChat;
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

public class AwarenessServiceXMPPImpl implements IAwarenessService, MessageListener {

	private final static Logger logger = Logger.getLogger(AwarenessServiceXMPPImpl.class.getName());
	private ConnectionConfiguration config;
	public XMPPConnection xmppConnection;
	private ArrayList<IAwarenessPresenceListener> presenceListeners = new ArrayList<IAwarenessPresenceListener>();
	private ArrayList<IAwarenessMessageListener> messageListeners = new ArrayList<IAwarenessMessageListener>();
	private ArrayList<IAwarenessRosterListener> rosterListeners = new ArrayList<IAwarenessRosterListener>();
	private ArrayList<IChatPresenceToolListener> chatToolListeners = new ArrayList<IChatPresenceToolListener>();
	private ArrayList<IChatPresenceToolListener> presenceToolListeners = new ArrayList<IChatPresenceToolListener>();
	private Roster roster;
	private Map<String, MultiUserChat> mucToELOUris = new HashMap<String, MultiUserChat>();
	public List<String> joinedMUCRooms;


	public AwarenessServiceXMPPImpl() {
	}


	@Override
	public boolean isConnected() {
		if (xmppConnection != null)
			return xmppConnection.isConnected();

		return false;
	}


	@Override
	public void sendMessage(String recipient, String message) {
		Chat chat = xmppConnection.getChatManager().createChat(recipient, this);
		try {
			chat.sendMessage(message);
		} catch (XMPPException e) {
			logger.error("sendMessage: XMPPException: "+e);
			e.printStackTrace();
		}
	}


	public void processMessage(Chat chat, Message message) {
		if (message.getType() == Message.Type.chat) {
			logger.debug("processMessage: "+chat.getParticipant() + " says: " + message.getBody());
			
			for (IAwarenessMessageListener al : messageListeners) {
				if (al != null && message.getBody() != null) {
					
					String participant = chat.getParticipant();
					
					IAwarenessUser aw = new AwarenessUser();
					aw.setUsername(participant);
					
					
					IAwarenessEvent awarenessEvent = new AwarenessEvent(this,aw , message.getBody());
					al.handleAwarenessMessageEvent(awarenessEvent);
				}
			}
		}
	}


	@Override
	public void addAwarenessMessageListener(IAwarenessMessageListener awarenessMessageListener) {
		messageListeners.add(awarenessMessageListener);
	}


	@Override
	public void addAwarenessRosterListener(IAwarenessRosterListener awarenessListListener) {
		rosterListeners.add(awarenessListListener);
	}


	@Override
	public void addAwarenessPresenceListener(IAwarenessPresenceListener awarenessPresenceListener) {
		presenceListeners.add(awarenessPresenceListener);
	}


	public void removeAwarenessPresenceListener(IAwarenessPresenceListener awarenessPresenceListener) {
		presenceListeners.remove(awarenessPresenceListener);
	}


	@Override
	public void addBuddy(String buddyName) throws AwarenessServiceException {
		roster = this.xmppConnection.getRoster();
		Collection<RosterEntry> rosterEntries = roster.getEntries();
		try {
			roster.createEntry(buddyName, buddyName, new String[] { "everybody" });
		} catch (XMPPException e) {
			logger.error("addBuddy: XMPPException: "+e);
		}
	}


	@Override
	public void removeBuddy(String buddyName) throws AwarenessServiceException {
		RosterEntry entry = xmppConnection.getRoster().getEntry(buddyName);
		try {
			xmppConnection.getRoster().removeEntry(entry);
		} catch (XMPPException e) {
			logger.error("removeBuddy: XMPPException: "+e);
		}
		Presence unsubbed = new Presence(Presence.Type.unsubscribed);
		unsubbed.setTo(buddyName);
		this.xmppConnection.sendPacket(unsubbed);

	}


	@Override
	public ArrayList<IAwarenessUser> getBuddies() throws AwarenessServiceException {
		roster = this.xmppConnection.getRoster();
		if(roster != null || !roster.getEntries().isEmpty()) {
			Collection<RosterEntry> rosterEntries = roster.getEntries();
			ArrayList<IAwarenessUser> buddies = new ArrayList<IAwarenessUser>();
			for (RosterEntry buddy : rosterEntries) {
				IAwarenessUser au = new AwarenessUser();
				au.setName(buddy.getName());
				au.setUsername(buddy.getUser());
				au.setPresence(roster.getPresence(buddy.getUser()).toString());
				buddies.add(au);
			}
			return buddies;			
		}
		else {
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
		getRoster().setSubscriptionMode(Roster.SubscriptionMode.accept_all);
		getRoster().addRosterListener(new RosterListener() {

			@Override
			public void entriesAdded(Collection<String> addresses) {
				logger.debug("init: entriesAdded: "+addresses);

				for (IAwarenessRosterListener rosterListener : rosterListeners) {
					if (rosterListener != null) {
						IAwarenessRosterEvent rosterEvent = new AwarenessRosterEvent(AwarenessServiceXMPPImpl.this,
								AwarenessServiceXMPPImpl.this.xmppConnection.getUser(), IAwarenessRosterEvent.ADD,
								addresses);
						rosterListener.handleAwarenessRosterEvent(rosterEvent);
					}
				}

			}


			@Override
			public void entriesDeleted(Collection<String> addresses) {
				logger.debug("init: entriesDeleted: "+addresses);
				for (IAwarenessRosterListener rosterListener : rosterListeners) {
					if (rosterListener != null) {
						IAwarenessRosterEvent rosterEvent = new AwarenessRosterEvent(AwarenessServiceXMPPImpl.this,
								AwarenessServiceXMPPImpl.this.xmppConnection.getUser(), IAwarenessRosterEvent.REMOVE,
								addresses);
						rosterListener.handleAwarenessRosterEvent(rosterEvent);
					}
				}
			}


			@Override
			public void entriesUpdated(Collection<String> addresses) {
				logger.debug("init: entriesUpdated: "+addresses);
				for (IAwarenessRosterListener rosterListener : rosterListeners) {
					if (rosterListener != null) {
						IAwarenessRosterEvent rosterEvent = new AwarenessRosterEvent(AwarenessServiceXMPPImpl.this,
								AwarenessServiceXMPPImpl.this.xmppConnection.getUser(), IAwarenessRosterEvent.UPDATED,
								addresses);
						rosterListener.handleAwarenessRosterEvent(rosterEvent);
					}
				}
			}


			@Override
			public void presenceChanged(Presence presence) {
				logger.debug("init: presenceChanged: "+presence);
				if (presence == null) {
					return;					
				}

				for (IAwarenessPresenceListener presenceListener : presenceListeners) {
					if (presenceListener != null) {
						
						IAwarenessUser aw = new AwarenessUser();
						aw.setUsername(presence.getFrom());
						aw.setPresence(presence.getType().toString());
						
						IAwarePresenceEvent presenceEvent = new AwarenessPresenceEvent(AwarenessServiceXMPPImpl.this, aw , "updated from awareness service", presence.getType().toString(), presence.getStatus());
						presenceListener.handleAwarenessPresenceEvent(presenceEvent);
					}
				}

			}

		});

		
		//look up all the MUC chats this user is in
		
		
		//Added by Jeremy Toussaint to track non-initialized chats
		
		//we create a MUC now with ELOId
		
//		getChatManager().addChatListener(new ChatManagerListener() {
//			
//			@Override
//			public void chatCreated(Chat chat, boolean local) {
//				chat.addMessageListener(AwarenessServiceXMPPImpl.this);
//			}
//		});
	}
	
	public boolean hasJoinedRoom(String ELOUri, String user) {
		Iterator<String> jrooms = MultiUserChat.getJoinedRooms(xmppConnection, user);
		
		joinedMUCRooms = new ArrayList<String>();
		
		for (Iterator i = jrooms; jrooms.hasNext(); ) {
		   joinedMUCRooms.add((String) i.next());
		}
		
		return joinedMUCRooms.contains(ELOUri);
	}
	
	public boolean doesRoomExist(String ELOUri) {
		RoomInfo info;
		try {
			info = MultiUserChat.getRoomInfo(xmppConnection, ELOUri + "@conference.scy.intermedia.uio.no");
		} catch (XMPPException e) {
			//e.printStackTrace();
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
	public void setPresence(String presenceString) throws AwarenessServiceException {
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

	public void destoryMUCRoom(String ELOUri) {
		if( doesRoomExist(ELOUri)) {
			MultiUserChat muc = new MultiUserChat(xmppConnection, ELOUri+"@conference.scy.intermedia.uio.no");
			try {
				muc.destroy("hate this room", null);
			} catch (XMPPException e) {
				e.printStackTrace();
			}
		}
		
	}

	@Override
	public void joinMUCRoom(String ELOUri) {
		//first look of the muc to see if it as been create first
		
		if(ELOUri != null){
			
			
				MultiUserChat muc = new MultiUserChat(xmppConnection, ELOUri+"@conference.scy.intermedia.uio.no");
				try {
					
					//first check if the room exists
					if( doesRoomExist(ELOUri)) {
						//am i joined
						if( hasJoinedRoom(ELOUri, xmppConnection.getUser()) == false )
							muc.join(xmppConnection.getUser());
						
						
					} else {
						//create it
						//we need to create
					    muc.create(ELOUri);

						// Get the the room's configuration form
						Form form = muc.getConfigurationForm();
						// Create a new form to submit based on the original form
						Form submitForm = form.createAnswerForm();
						// Add default answers to the form to submit
						for (Iterator fields = form.getFields(); fields.hasNext();) {
							FormField field = (FormField) fields.next();
							if (!FormField.TYPE_HIDDEN.equals(field.getType())
									&& field.getVariable() != null) {
								// Sets the default value as the answer
								submitForm.setDefaultAnswer(field.getVariable());
							}
						}
						// Sets the new owner of the room
//						List owners = new ArrayList();
//						owners.add("djed11");
						submitForm.setAnswer("muc#roomconfig_persistentroom", true);
						// Send the completed form (with default values) to the
						// server to configure the room
						muc.sendConfigurationForm(submitForm);
//						if( joinedMUCRooms.contains(ELOUri) == false )
//							 joinedMUCRooms.add(ELOUri);
					}
				} catch (XMPPException e) {
					e.printStackTrace();
				}

		}
	
	}


	@Override
	public List<IAwarenessUser> getChatBuddies(String ELOUri) {
		if(ELOUri != null  ){
			List<IAwarenessUser> users = new ArrayList<IAwarenessUser>();
			MultiUserChat muc = new MultiUserChat(xmppConnection, ELOUri);
			try {
				Collection<Affiliate> members = muc.getMembers();
				
				for (Affiliate affiliate : members) {
					IAwarenessUser user = new AwarenessUser();
					user.setName(affiliate.getNick());
					user.setUsername(affiliate.getJid());
					users.add(user);
				}
				
				return users;
				
			} catch (XMPPException e) {
				e.printStackTrace();
			}
		}
		return null;
	}


	@Override
	public void addBuddyToMUC(IAwarenessUser buddy, String ELOUri) {
		if(ELOUri != null){
			
			//check to see if the room exists
			if( this.doesRoomExist(ELOUri) ) {
				MultiUserChat muc = new MultiUserChat(xmppConnection, ELOUri);
				try {
					muc.join(buddy.getUsername());
				} catch (XMPPException e) {
					e.printStackTrace();
				}
			} else {
				//the room does not exist.
			}
			
		}
		
	}


	@Override
	public void removeBuddyFromMUC(IAwarenessUser buddy, String ELOUri) {
		if(ELOUri != null){
			MultiUserChat muc = new MultiUserChat(xmppConnection, ELOUri);
			try {
				muc.kickParticipant(buddy.getUsername(), "had to go");
			} catch (XMPPException e) {
				e.printStackTrace();
			}
		}
	}

}
