package eu.scy.presence;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.sun.source.tree.AssertTree;

import eu.scy.presence.IPresenceModule;
import eu.scy.presence.PresenceModuleException;
import eu.scy.presence.PresenceModuleFactory;
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
                //((PresenceModuleXMPPImpl) presenceModule).createPresenceModule("presence_spider", "presence_spider");
                //((PresenceModuleXMPPImpl) presenceModule).createPresenceModule("passerby", "passerby");
                //((PresenceModuleXMPPImpl) presenceModule).createPresenceModule("thomasd", "fiskefor");
                //presenceModule.cr = PresenceModuleFactory.getPresenceModule(PresenceModuleFactory.MOCK_STYLE);
            } catch (PresenceModuleException e) {
                logger.error("presence noodle test case bummer");
                e.printStackTrace();
            }
        }
        assertNotNull(presenceModule);
    }
    
    
    @org.junit.Test
    public void testGetGroups() {      
        ArrayList<String> groups = null;
        try {
            //groups = (ArrayList<String>) presenceModule.getGroups("thomasd");
            groups = (ArrayList<String>) presenceModule.getGroups();
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
            buddies = (ArrayList<String>) presenceModule.getBuddies();
        } catch (PresenceModuleException e) {
            e.printStackTrace();
        }
        assertNotNull(buddies);
        assertTrue(buddies.size() > 0);

    
        buddies = null;
        try {
            buddies = (ArrayList<String>) presenceModule.getBuddies(); //TODO: should be getBuddies("userName")
        } catch (PresenceModuleException e) {
            e.printStackTrace();
        }
        assertNotNull(buddies);
        assertTrue(buddies.size() > 0);    
    }
    
    
}
