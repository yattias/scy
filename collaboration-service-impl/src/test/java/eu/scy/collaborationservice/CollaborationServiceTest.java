package eu.scy.collaborationservice;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.log4j.Logger;


public class CollaborationServiceTest extends TestCase {
    
    private static final Logger logger = Logger.getLogger(CollaborationServiceTest.class.getName());
    private CollaborationService as;
    
    
    public CollaborationServiceTest(String testName) {
        super(testName);
    }
    
    
    public static Test suite() {
        return new TestSuite(CollaborationServiceTest.class);
    }
    
    
    private void setUpAwarenessService() {
//        if (as == null) {
//            as = CollaborationService.connect("bling", "vaffel");            
//        }
    }
    
    
    public void testCreateAwarenessService() {
        setUpAwarenessService();
        assertNotNull(as);
    }
    
    // TODO: write this one so that it passes on all clients running a test
//    public void testGetBuddies(String username) {
//        setUpAwarenessService();
//        ArrayList<String> buddies = as.getBuddies("bling");
//        assertNotNull(buddies);
//        assertTrue(buddies.size() > 0);
//        assertEquals(buddies.get(0), "aperritano@wiki.intermedia.uio.no");
//    }
    
    
//    public void testSendMessage() {
//        setUpAwarenessService();
//        as.sendMessage("aperritano@imediamac09.uio.no", "hoy");
//        assertTrue(true);
//    }
    
}
