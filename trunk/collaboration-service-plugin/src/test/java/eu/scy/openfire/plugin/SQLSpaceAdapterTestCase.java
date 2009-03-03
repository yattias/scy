package eu.scy.openfire.plugin;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.log4j.Logger;

import eu.scy.collaborationservice.CollaborationService;



public class SQLSpaceAdapterTestCase extends TestCase {
    
    private static final Logger logger = Logger.getLogger(SQLSpaceAdapter.class.getName());
    private SQLSpaceAdapter sqlSpaceAdapter;
    private ScyCommunicationAdapter communicationAdapter;
    
    
    public SQLSpaceAdapterTestCase(String testName) {
        super(testName);
    }
    
    
    public static Test suite() {
        return new TestSuite(SQLSpaceAdapterTestCase.class);
    }
    
    
    private SQLSpaceAdapter getSQLSpaceAdapter() {
        if (sqlSpaceAdapter == null) {
            sqlSpaceAdapter = SQLSpaceAdapter.createAdapter("thomasd", SQLSpaceAdapter.COLLABORATION_SERVICE_SPACE, getScyCommunicationAdapter());         
        }
        return sqlSpaceAdapter;
    }

    
    private ScyCommunicationAdapter getScyCommunicationAdapter() {
        if (communicationAdapter == null) {
            communicationAdapter = ScyCommunicationAdapter.getInstance();        
        }
        return communicationAdapter;
    }
    
    
    public void testCreateSQLSpaceAdapter() {
        assertNotNull(getSQLSpaceAdapter());
    }
    
    public void testScyCommunicationAdapter() {
        assertNotNull(getScyCommunicationAdapter());
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
