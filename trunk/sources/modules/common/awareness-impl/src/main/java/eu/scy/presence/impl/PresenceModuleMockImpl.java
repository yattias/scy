package eu.scy.presence.impl;

import java.util.ArrayList;
import java.util.List;

import eu.scy.presence.IPresenceListEvent;
import eu.scy.presence.IPresenceListListener;
import eu.scy.presence.IPresenceListener;
import eu.scy.presence.IPresenceModule;
import eu.scy.presence.IPresenceStatusEvent;
import eu.scy.presence.IPresenceUser;
import eu.scy.presence.PresenceModuleException;
import eu.scy.presence.PresenceUser;
import eu.scy.presence.event.PresenceListEvent;
import eu.scy.presence.event.PresenceStatusEvent;

public class PresenceModuleMockImpl implements IPresenceModule {
    
    private List<IPresenceListListener> awarenessListListeners = new ArrayList<IPresenceListListener>();
    private List<IPresenceListener> awarenessPresenceListeners = new ArrayList<IPresenceListener>();
    private List<IPresenceUser> buddies = new ArrayList<IPresenceUser>();
    
    public PresenceModuleMockImpl() {
        System.out.println("AwarenessServiceMockImpl.AwarenessServiceMockImpl()");
    }
    
    public void closeAwarenessService() {
        System.out.println("AwarenessServiceMockImpl.closeAwarenessService()");
    }
    
  
    
    
    
    
    @Override
    public void addBuddy(String username) throws PresenceModuleException {
        // see if the buddy is already there
        
        for (IPresenceUser au : buddies) {
            if (au.getUsername().equals(username)) {
                throw new PresenceModuleException("buddy already added");
            }
        }
        
        IPresenceUser user = new PresenceUser();
        user.setUsername(username);
        user.setPresence(IPresenceStatusEvent.OFFLINE);
        
        buddies.add(user);
        
        // tell everyone is listening
        for (IPresenceListListener ll : awarenessListListeners) {
            ll.handlePresenceListEvent(new PresenceListEvent(this, username, IPresenceListEvent.ADD));
        }
        
    }
    
    @Override
    public void removeBuddy(String username) throws PresenceModuleException {
        
        boolean removed = false;
        IPresenceUser toRemove = null;
        for (IPresenceUser au : buddies) {
            if (au.getUsername().equals(username)) {
                toRemove = au;
            }
        }
        
       
        
        if (toRemove == null)
            throw new PresenceModuleException("tyring to remove a buddy that is not there");
        
        // tell everyone is listening
        for (IPresenceListListener ll : awarenessListListeners) {
            ll.handlePresenceListEvent(new PresenceListEvent(this, username, IPresenceListEvent.REMOVE));
        }
    }
   
    
    @Override
    public List<String> getBuddies() throws PresenceModuleException {
        List<String> b = new ArrayList<String>();
        for (IPresenceUser au : buddies) {
            b.add(au.getUsername());
        }
        return b;
    }
    
    @Override
    public void setPresence(String username, String presence) throws PresenceModuleException {
        for (IPresenceUser au : buddies) {
            if (au.getUsername().equals(username)) {
                au.setPresence(presence);
                // fire the listen
                // tell everyone is listening
                for (IPresenceListener ll : awarenessPresenceListeners) {
                    ll.handlePresenceEvent(new PresenceStatusEvent(this, username, "changed presence", presence, au.getStatus()));
                }
                
            }
        }
    }
    
    @Override
    public void setStatus(String username, String status) throws PresenceModuleException {
        for (IPresenceUser au : buddies) {
            if (au.getUsername().equals(username)) {
                au.setStatus(status);
                // fire the listen
                // tell everyone is listening
                for (IPresenceListener ll : awarenessPresenceListeners) {
                    ll.handlePresenceEvent(new PresenceStatusEvent(this, username, "changed status", au.getPresence(), status));
                }
                
            }
        }
        
    }

    
    @Override
    public void addListListener(IPresenceListListener awarenessListListener) {
        this.awarenessListListeners.add(awarenessListListener);
    }
    
    @Override
    public void addStatusListener(IPresenceListener awarenessPresenceListener) {
        this.awarenessPresenceListeners.add(awarenessPresenceListener);
    }

	@Override
	public List<String> getGroups() throws PresenceModuleException {
		return null;
	}

	@Override
	public List<String> getGroups(String userName)
			throws PresenceModuleException {
		return null;
	}

	@Override
	public void getPresence(String username) throws PresenceModuleException {
	}

	@Override
	public void getStatus(String username) throws PresenceModuleException {
	}

	@Override
	public void joinGroup(String groupName) throws PresenceModuleException {
	}

	@Override
	public void leaveGroup(String groupName) throws PresenceModuleException {
	}

	@Override
	public List<String> getBuddies(String arg0) throws PresenceModuleException {
		// TODO Auto-generated method stub
		return null;
	}
    

}
