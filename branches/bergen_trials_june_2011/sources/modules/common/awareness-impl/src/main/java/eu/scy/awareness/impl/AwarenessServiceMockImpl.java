package eu.scy.awareness.impl;

import java.util.ArrayList;
import java.util.List;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smackx.muc.MultiUserChat;

import eu.scy.awareness.AwarenessServiceException;
import eu.scy.awareness.AwarenessUser;
import eu.scy.awareness.IAwarenessService;
import eu.scy.awareness.IAwarenessUser;
import eu.scy.awareness.event.AwarenessRosterEvent;
import eu.scy.awareness.event.IAwarePresenceEvent;
import eu.scy.awareness.event.IAwarenessMessageListener;
import eu.scy.awareness.event.IAwarenessPresenceListener;
import eu.scy.awareness.event.IAwarenessRosterEvent;
import eu.scy.awareness.event.IAwarenessRosterListener;
import eu.scy.awareness.tool.IChatPresenceToolListener;

public class AwarenessServiceMockImpl implements IAwarenessService {
    
    private List<IAwarenessRosterListener> awarenessListListeners = new ArrayList<IAwarenessRosterListener>();
    private List<IAwarenessPresenceListener> awarenessPresenceListeners = new ArrayList<IAwarenessPresenceListener>();
    private List<IAwarenessMessageListener> awarenessMessageListeners = new ArrayList<IAwarenessMessageListener>();
    private List<IAwarenessUser> buddies = new ArrayList<IAwarenessUser>();
    
    public AwarenessServiceMockImpl() {
        // System.out.println("AwarenessServiceMockImpl.AwarenessServiceMockImpl()");
    }
    
    public void closeAwarenessService() {
        // System.out.println("AwarenessServiceMockImpl.closeAwarenessService()");
    }
    
    @Override
    public void addAwarenessRosterListener(IAwarenessRosterListener awarenessListListener) {
        this.awarenessListListeners.add(awarenessListListener);
    }
    
    @Override
    public void addAwarenessPresenceListener(IAwarenessPresenceListener awarenessPresenceListener) {
        this.awarenessPresenceListeners.add(awarenessPresenceListener);
    }
    
    @Override
    public void addAwarenessMessageListener(IAwarenessMessageListener awarenessMessageListener) {
        this.awarenessMessageListeners.add(awarenessMessageListener);
    }
    
    @Override
    public void addBuddy(String username) throws AwarenessServiceException {
        // see if the buddy is already there
        
        for (IAwarenessUser au : buddies) {
            if (au.getJid().equals(username)) {
                throw new AwarenessServiceException("buddy already added");
            }
        }
        
        IAwarenessUser user = new AwarenessUser();
        user.setJid(username);
        user.setPresence(IAwarePresenceEvent.OFFLINE);
        
        buddies.add(user);
        
        // tell everyone is listening
        for (IAwarenessRosterListener ll : awarenessListListeners) {
            ll.handleAwarenessRosterEvent(new AwarenessRosterEvent(this, username, IAwarenessRosterEvent.ADD, null));
        }
        
    }
    
    @Override
    public void removeBuddy(String username) throws AwarenessServiceException {
        
        boolean removed = false;
        IAwarenessUser toRemove = null;
        for (IAwarenessUser au : buddies) {
            if (au.getNickName().equals(username)) {
                toRemove = au;
            }
        }
        
        if (toRemove == null)
            throw new AwarenessServiceException("tyring to remove a buddy that is not there");
        
        // tell everyone is listening
        for (IAwarenessRosterListener ll : awarenessListListeners) {
            ll.handleAwarenessRosterEvent(new AwarenessRosterEvent(this, username, IAwarenessRosterEvent.REMOVE, null));
        }
    }
    
    @Override
    public void sendMessage(IAwarenessUser user, String message) throws AwarenessServiceException {
        //pretend to send message quick and dirty 
        
        //tell every one that a message was send
        for (IAwarenessMessageListener ll : awarenessMessageListeners) {
           // ll.handleAwarenessMessageEvent(new AwarenessEvent(this, username, message));
        }
    }
        
    @Override
    public List<IAwarenessUser> getBuddies() throws AwarenessServiceException {
        List<IAwarenessUser> b = new ArrayList<IAwarenessUser>();
        for (IAwarenessUser au : buddies) {
            b.add(au);
        }
        return b;
    }
    
    @Override
    public void setPresence(String presence) throws AwarenessServiceException {
//        for (IAwarenessUser au : buddies) {
//            if (au.getUsername().equals(username)) {
//                au.setPresence(presence);
//                // fire the listen
//                // tell everyone is listening
//                for (IAwarenessPresenceListener ll : awarenessPresenceListeners) {
//                    ll.handleAwarenessPresenceEvent(new AwarenessPresenceEvent(this, username, "changed presence", presence, au.getStatus()));
//                }
//                
//            }
//        }
    }
    
    @Override
    public void setStatus(String status) throws AwarenessServiceException {
//        for (IAwarenessUser au : buddies) {
//            if (au.getUsername().equals(username)) {
//                au.setStatus(status);
//                // fire the listen
//                // tell everyone is listening
//                for (IAwarenessPresenceListener ll : awarenessPresenceListeners) {
//                    ll.handleAwarenessPresenceEvent(new AwarenessPresenceEvent(this, username, "changed status", au.getPresence(), status));
//                }
//                
//            }
//        }
//        
    }

    @Override
    public void disconnect() {
        
    }

    @Override
    public void init(Connection connection) {
        
    }

    @Override
    public boolean isConnected() {
        return false;
    }

	@Override
	public void removeAwarenessPresenceListener(IAwarenessPresenceListener awarenessPresenceListener) {
		this.awarenessPresenceListeners.remove(awarenessPresenceListener);
	}

	@Override
	public void addChatToolListener(IChatPresenceToolListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addPresenceToolListener(IChatPresenceToolListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addBuddyToMUC(IAwarenessUser buddy, String ELOUri) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeBuddyFromMUC(IAwarenessUser buddy, String ELOUri) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void joinMUCRoom(String ELOUri) {
		// TODO Auto-generated method stub
		
	}

	public boolean doesRoomExist(String ELOUri) {
		return false;
	}

	public boolean hasJoinedRoom(String ELOUri, String user) {
		return false;
	}

	public void destoryMUCRoom(String ELOUri) {
	}

	

	public String getMUCConferenceExtension() {
		return null;
	}

	public void setMUCConferenceExtension(String CONFERENCE_EXT) {
	}

	@Override
	public void sendMUCMessage(String ELOUri, String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public MultiUserChat getMultiUserChat(String ELOUri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateChatTool(List<IAwarenessUser> users) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updatePresenceTool(List<IAwarenessUser> users) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Connection getConnection() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<IAwarenessUser> getMUCBuddies(String ELOUri)
			throws AwarenessServiceException {
		// TODO Auto-generated method stub
		return null;
	}

}
