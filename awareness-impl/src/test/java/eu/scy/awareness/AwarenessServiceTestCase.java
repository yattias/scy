package eu.scy.awareness;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.log4j.Logger;


public class AwarenessServiceTestCase extends TestCase {
    
    private static final Logger logger = Logger.getLogger(AwarenessServiceTestCase.class.getName());
    private AwarenessService as;
    
    
    public AwarenessServiceTestCase(String testName) {
        super(testName);
    }
    
    
    public static Test suite() {
        return new TestSuite(AwarenessServiceTestCase.class);
    }
    
    
    private void setUpAwarenessService() {
        if (as == null) {
            as = AwarenessService.createAwarenessService("talk2me", "talk2me");            
        }
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
    
    
    public void testSendMessage() {
        setUpAwarenessService();
        as.sendMessage("aperritano@imediamac09.uio.no", "hoy");
        assertTrue(true);
    }
    
}
