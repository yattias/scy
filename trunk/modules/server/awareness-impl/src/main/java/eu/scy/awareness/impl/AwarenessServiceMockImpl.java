package eu.scy.awareness.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import eu.scy.awareness.AwarenessServiceException;
import eu.scy.awareness.AwarenessUser;
import eu.scy.awareness.IAwarenessService;
import eu.scy.awareness.IAwarenessUser;
import eu.scy.awareness.event.AwarenessEvent;
import eu.scy.awareness.event.AwarenessListEvent;
import eu.scy.awareness.event.AwarenessPresenceEvent;
import eu.scy.awareness.event.IAwarePresenceEvent;
import eu.scy.awareness.event.IAwarenessListEvent;
import eu.scy.awareness.event.IAwarenessListListener;
import eu.scy.awareness.event.IAwarenessMessageListener;
import eu.scy.awareness.event.IAwarenessPresenceListener;

public class AwarenessServiceMockImpl implements IAwarenessService {
    
    private List<IAwarenessListListener> awarenessListListeners = new ArrayList<IAwarenessListListener>();
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
    public void addAwarenessListListener(IAwarenessListListener awarenessListListener) {
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
        for (IAwarenessListListener ll : awarenessListListeners) {
            ll.handleAwarenessListEvent(new AwarenessListEvent(this, username, IAwarenessListEvent.ADD));
        }
        
    }
    
    @Override
    public void joinSession(String session) throws AwarenessServiceException {
        System.out.println("AwarenessServiceMockImpl.joinSession()");
    }
    
    @Override
    public void leaveSession(String session) throws AwarenessServiceException {
        System.out.println("AwarenessServiceMockImpl.joinSession()");
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
        for (IAwarenessListListener ll : awarenessListListeners) {
            ll.handleAwarenessListEvent(new AwarenessListEvent(this, username, IAwarenessListEvent.REMOVE));
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
    public List<String> getBuddies() throws AwarenessServiceException {
        List<String> b = new ArrayList<String>();
        for (IAwarenessUser au : buddies) {
            b.add(au.getUsername());
        }
        return b;
    }
    
    @Override
    public void setPresence(String username, String presence) throws AwarenessServiceException {
        for (IAwarenessUser au : buddies) {
            if (au.getUsername().equals(username)) {
                au.setPresence(presence);
                // fire the listen
                // tell everyone is listening
                for (IAwarenessPresenceListener ll : awarenessPresenceListeners) {
                    ll.handleAwarenessPresenceEvent(new AwarenessPresenceEvent(this, username, "changed presence", presence, au.getStatus()));
                }
                
            }
        }
    }
    
    @Override
    public void setStatus(String username, String status) throws AwarenessServiceException {
        for (IAwarenessUser au : buddies) {
            if (au.getUsername().equals(username)) {
                au.setStatus(status);
                // fire the listen
                // tell everyone is listening
                for (IAwarenessPresenceListener ll : awarenessPresenceListeners) {
                    ll.handleAwarenessPresenceEvent(new AwarenessPresenceEvent(this, username, "changed status", au.getPresence(), status));
                }
                
            }
        }
        
    }

}
