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
                ((PresenceModuleXMPPImpl) presenceModule).createPresenceModule("presence_spider", "presence_spider");
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
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        assertNotNull(groups);
        assertTrue(groups.size() > 0);
    }
    
    
}
