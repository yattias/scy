package eu.scy.awareness;

import java.util.ArrayList;

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
            as = AwarenessService.createAwarenessService("bling", "vaffel");            
        }
    }
    
    
    public void testCreateAwarenessService() {
        setUpAwarenessService();
        assertNotNull(as);
    }
    
    
    public void testGetBuddies(String username) {
        setUpAwarenessService();
        ArrayList<String> buddies = as.getBuddies("bling");
        assertNotNull(buddies);
        assertTrue(buddies.size() > 0);
        assertEquals(buddies.get(0), "asdf");
    }
    
    
    public void testSendMessage() {
        setUpAwarenessService();
        as.sendMessage("thomasd", "hoy");
        assertTrue(true);
    }
    
}
