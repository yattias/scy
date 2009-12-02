package eu.scy.awareness.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.log4j.Logger;
import java.util.logging.Level;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;

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

public class AwarenessServiceXMPPImpl implements IAwarenessService, MessageListener {

	private final static Logger logger = Logger.getLogger(AwarenessServiceXMPPImpl.class.getName());
	private ConnectionConfiguration config;
	private XMPPConnection xmppConnection;
	private ArrayList<IAwarenessPresenceListener> presenceListeners = new ArrayList<IAwarenessPresenceListener>();
	private ArrayList<IAwarenessMessageListener> messageListeners = new ArrayList<IAwarenessMessageListener>();
	private ArrayList<IAwarenessRosterListener> rosterListeners = new ArrayList<IAwarenessRosterListener>();
	private Roster roster;
	protected Vector<Object> externalUsers = new Vector<Object>();
	protected Boolean hasAnswered = false;


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
				if (al != null) {
					IAwarenessEvent awarenessEvent = new AwarenessEvent(this, chat.getParticipant(), message.getBody());
					al.handleAwarenessMessageEvent(awarenessEvent);
					hasAnswered = true;
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
				else if((presence.toString()).equals("unavailable")) {
					String correctUserName = AwarenessServiceXMPPImpl.this.xmppConnection.getUser();
					logger.debug("init: presenceChanged: removing "+correctUserName);
					externalUsers.remove(correctUserName);
				}

				for (IAwarenessPresenceListener presenceListener : presenceListeners) {
					if (presenceListener != null) {
						IAwarePresenceEvent presenceEvent = new AwarenessPresenceEvent(AwarenessServiceXMPPImpl.this, AwarenessServiceXMPPImpl.this.xmppConnection.getUser(), "new presence", presence.getType().toString(), presence.getStatus());
						presenceListener.handleAwarenessPresenceEvent(presenceEvent);
					}
				}

			}

		});

		//Added by Jeremy Toussaint to track non-initialized chats
		getChatManager().addChatListener(new ChatManagerListener() {
			
			@Override
			public void chatCreated(Chat chat, boolean local) {
				chat.addMessageListener(new MessageListener() {
					
					@Override
					public void processMessage(Chat chat, Message message) {
						logger.debug("chatCreated: processMessage: "+chat.getParticipant() + " said -> " + message.getBody());
						logger.debug("chatCreated: processMessage: "+AwarenessServiceXMPPImpl.this.xmppConnection.getUser());
						if (message.getType() == Message.Type.chat) {
							String correctUserName = AwarenessServiceXMPPImpl.this.xmppConnection.getUser();
							for (IAwarenessMessageListener al : messageListeners) {
								if ((al != null) && (message.getBody() != null) && !hasAnswered) {
									IAwarenessEvent awarenessEvent = new AwarenessEvent(this, chat.getParticipant(), message.getBody());
									al.handleAwarenessMessageEvent(awarenessEvent);
									if(!externalUsers.contains(correctUserName)) {
										externalUsers.add(correctUserName);										
									}
								}
							}
						}
					}
				});
			}
		});
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
}
