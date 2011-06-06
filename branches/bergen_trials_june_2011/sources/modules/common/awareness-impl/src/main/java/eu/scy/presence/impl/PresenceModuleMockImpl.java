package eu.scy.presence.impl;

import java.util.List;

import eu.scy.presence.IPresenceModule;
import eu.scy.presence.IPresencePacketListener;
import eu.scy.presence.IPresenceRosterListener;
import eu.scy.presence.PresenceModuleException;


public class PresenceModuleMockImpl implements IPresenceModule {

    @Override
	public List<String> getBuddies() throws PresenceModuleException {
		// TODO Auto-generated method stub
		return null;
	}

    
	@Override
	public List<String> getBuddies(String userName) throws PresenceModuleException {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	public List<String> getGroups(String userName) throws PresenceModuleException {
		// TODO Auto-generated method stub
		return null;
	}
	

	@Override
	public List<String> getGroups() throws PresenceModuleException {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	public void getPresence(String username) throws PresenceModuleException {
		// TODO Auto-generated method stub		
	}

	
	@Override
	public void getStatus(String username) throws PresenceModuleException {
		// TODO Auto-generated method stub		
	}

	
	@Override
	public void joinGroup(String groupName, String userName) throws PresenceModuleException {
		// TODO Auto-generated method stub		
	}

	
	@Override
	public void leaveGroup(String groupName, String userName) throws PresenceModuleException {
		// TODO Auto-generated method stub
	}

	
	@Override
	public void removeBuddy(String buddy) throws PresenceModuleException {
		// TODO Auto-generated method stub
	}

	
	@Override
	public void setPresence(String username, String presence) throws PresenceModuleException {
		// TODO Auto-generated method stub
	}

	
	@Override
	public void setStatus(String username, String status) throws PresenceModuleException {
		// TODO Auto-generated method stub		
	}


	@Override
	public void addBuddy(String buddy, String group) throws PresenceModuleException {
		// TODO Auto-generated method stub		
	}
	

    @Override
    public void addPacketListener(IPresencePacketListener packetListener) {
        // TODO Auto-generated method stub        
    }

    
    @Override
    public void addRosterListener(IPresenceRosterListener rosterListener) {
        // TODO Auto-generated method stub        
    }

}