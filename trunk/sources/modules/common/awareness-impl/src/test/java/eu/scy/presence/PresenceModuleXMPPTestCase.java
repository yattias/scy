package eu.scy.presence;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import eu.scy.presence.impl.PresenceModuleXMPPImpl;


public class PresenceModuleXMPPTestCase {

    
    private static final Logger logger = Logger.getLogger(PresenceModuleXMPPTestCase.class.getName());
    IPresenceModule presenceModule = null;        
    
    public PresenceModuleXMPPTestCase() {
    }
    

    @org.junit.Before
    public void presenceModuleSetup() {
        if (presenceModule == null) {
            try {
                presenceModule = PresenceModuleFactory.getPresenceModule(PresenceModuleFactory.XMPP_STYLE);
                ((PresenceModuleXMPPImpl) presenceModule).createPresenceModule("agentsmith", "agentsmith");
                //((PresenceModuleXMPPImpl) presenceModule).createPresenceModule("b1", "b1");
                //((PresenceModuleXMPPImpl) presenceModule).createPresenceModule("presence_spider", "presence_spider");
                //((PresenceModuleXMPPImpl) presenceModule).createPresenceModule("passerby", "passerby");
                //presenceModule.cr = PresenceModuleFactory.getPresenceModule(PresenceModuleFactory.MOCK_STYLE);
            } catch (PresenceModuleException e) {
                logger.error("presence noodle test case bummer");
                e.printStackTrace();
            }
        }
        assertNotNull(presenceModule);
    }
    
    @org.junit.Test
    public void testGetStatusForUsersInGroup() {      
        HashMap<String, String> users = new HashMap<String, String>();
        users = (HashMap<String, String>) ((PresenceModuleXMPPImpl) presenceModule).getStatusForUsersInGroup("everybody");     
        assertNotNull(users);
        logger.debug("users: " + users);
        assertTrue(users.size() > 5);      
    }
    
    
    @org.junit.Test
    public void testGetGroups() {      
        ArrayList<String> groups = null;
        try {
            //groups = (ArrayList<String>) presenceModule.getGroups("thomasd");
            groups = new ArrayList<String>(presenceModule.getGroups());
        } catch (PresenceModuleException e) {
            e.printStackTrace();
        }        
        assertNotNull(groups);
        logger.debug("groups.size: " + groups.size());
        assertTrue(groups.size() > 0);      
    }
    
    
    @org.junit.Test
    public void testGetBuddies() {
        ArrayList<String> buddies = null;
        try {
            buddies = new ArrayList<String>(presenceModule.getBuddies());
        } catch (PresenceModuleException e) {
            e.printStackTrace();
        }
        assertNotNull(buddies);
        logger.debug("buddies.size: " + buddies.size());
        assertTrue(buddies.size() > 0);

        //TODO: should also test getBuddies("userName")
    }
    
    
}
