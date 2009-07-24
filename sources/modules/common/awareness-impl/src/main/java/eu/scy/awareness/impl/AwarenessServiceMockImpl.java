package eu.scy.awareness.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jivesoftware.smack.XMPPConnection;

import eu.scy.awareness.AwarenessServiceException;
import eu.scy.awareness.AwarenessUser;
import eu.scy.awareness.IAwarenessService;
import eu.scy.awareness.IAwarenessUser;
import eu.scy.awareness.event.AwarenessEvent;
import eu.scy.awareness.event.AwarenessRosterEvent;
import eu.scy.awareness.event.AwarenessPresenceEvent;
import eu.scy.awareness.event.IAwarePresenceEvent;
import eu.scy.awareness.event.IAwarenessRosterEvent;
import eu.scy.awareness.event.IAwarenessRosterListener;
import eu.scy.awareness.event.IAwarenessMessageListener;
import eu.scy.awareness.event.IAwarenessPresenceListener;

public class AwarenessServiceMockImpl implements IAwarenessService {
    
    private List<IAwarenessRosterListener> awarenessListListeners = new ArrayList<IAwarenessRosterListener>();
    private List<IAwarenessPresenceListener> awarenessPresenceListeners = new ArrayList<IAwarenessPresenceListener>();
    private List<IAwarenessMessageListener> awarenessMessageListeners = new ArrayList<IAwarenessMessageListener>();
    private List<IAwarenessUser> buddies = new ArrayList<IAwarenessUser>();
    
    public AwarenessServiceMockImpl() {
        System.out.println("AwarenessServiceMockImpl.AwarenessServiceMockImpl()");
    }
    
    public void closeAwarenessService() {
        System.out.println("AwarenessServiceMockImpl.closeAwarenessService()");
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
            if (au.getUsername().equals(username)) {
                throw new AwarenessServiceException("buddy already added");
            }
        }
        
        IAwarenessUser user = new AwarenessUser();
        user.setUsername(username);
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
            if (au.getUsername().equals(username)) {
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
    public void sendMessage(String username, String message) throws AwarenessServiceException {
        //pretend to send message quick and dirty 
        
        //tell every one that a message was send
        for (IAwarenessMessageListener ll : awarenessMessageListeners) {
            ll.handleAwarenessMessageEvent(new AwarenessEvent(this, username, message));
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
    public void init(XMPPConnection connection) {
        
    }

    @Override
    public boolean isConnected() {
        return false;
    }

}
