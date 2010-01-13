package eu.scy.presence;

import eu.scy.presence.impl.PresenceModuleXMPPImpl;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

import org.junit.Ignore;


public class PresenceModuleXMPPTestCase {


    private static final Logger logger = Logger.getLogger(PresenceModuleXMPPTestCase.class.getName());
    IPresenceModule presenceModule = null;

    public PresenceModuleXMPPTestCase() {
    }


    @Ignore
    public void presenceModuleSetup() {
        logger.fine("presenceModuleSetup");
        if (presenceModule == null) {
            try {
                presenceModule = PresenceModuleFactory.getPresenceModule(PresenceModuleFactory.XMPP_STYLE);
                ((PresenceModuleXMPPImpl) presenceModule).createPresenceModule("jt11@scy.intermedia.uio.no", "jt11");
                initListeners();
            } catch (PresenceModuleException e) {
                logger.severe("presence noodle test case bummer");
                e.printStackTrace();
            }
        }
        assertNotNull(presenceModule);
    }


    private void initListeners() {
        presenceModule.addPacketListener(new IPresencePacketListener() {
            @Override
            public void handlePresencePacketEvent(IPresencePacketEvent e) {
                logger.fine("User: " + e.getUser() + " Message: " + e.getMessage() + " EventType: " + e.getEventType());
            }
        });
    }


    @org.junit.Test
    public void runNoTest() {
    }


    //@org.junit.Test
    public void testListeners() {
        logger.fine("running...");
        while (true) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    //    @org.junit.Test
    public void testGetStatusForUsersInGroup() {
        HashMap<String, String> users = new HashMap<String, String>();
        users = (HashMap<String, String>) ((PresenceModuleXMPPImpl) presenceModule).getStatusForUsersInGroup("everybody");
        assertNotNull(users);
        logger.fine("users: " + users);
        assertTrue(users.size() > 5);
    }


    //    @org.junit.Test
    public void testGetGroups() {
        ArrayList<String> groups = null;
        try {
            //groups = (ArrayList<String>) presenceModule.getGroups("thomasd");
            groups = new ArrayList<String>(presenceModule.getGroups());
        } catch (PresenceModuleException e) {
            e.printStackTrace();
        }
        assertNotNull(groups);
        logger.fine("groups.size: " + groups.size());
        assertTrue(groups.size() > 0);
    }


    //    @org.junit.Test
    public void testGetBuddies() {
        ArrayList<String> buddies = null;
        try {
            buddies = new ArrayList<String>(presenceModule.getBuddies());
        } catch (PresenceModuleException e) {
            e.printStackTrace();
        }
        assertNotNull(buddies);
        logger.fine("buddies.size: " + buddies.size());
        assertTrue(buddies.size() > 0);

        //TODO: should also test getBuddies("userName")
    }


}
