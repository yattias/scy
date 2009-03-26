package eu.scy.collaborationservice;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;

import org.apache.log4j.Logger;

import eu.scy.collaborationservice.session.CollaborationSession;
import eu.scy.collaborationservice.session.ICollaborationSession;
import eu.scy.communications.message.IScyMessage;
import eu.scy.communications.message.impl.ScyMessage;


public class CollaborationServiceTestCase {
    
    private static final Logger logger = Logger.getLogger(CollaborationServiceTestCase.class.getName());
    private static ICollaborationService cs;
    
    
    public CollaborationServiceTestCase() {
    }
    
    
    public static Test suite() { 
        return new JUnit4TestAdapter(CollaborationServiceTestCase.class); 
    }   
    
    
    private ICollaborationService getCS() {
        if (cs == null) {
            try {
                cs = CollaborationServiceFactory.getCollaborationService(CollaborationServiceFactory.LOCAL_STYLE);
            } catch (CollaborationServiceException e) {
                logger.error("Failed to create collab service. Odd. " + e);
                e.printStackTrace();
            }            
        }        
        return cs;
    }
    
    @org.junit.Test
    public void testCreateAwarenessService() {
        assertNotNull(getCS());
    }
    
    
    @org.junit.Test
    public void testDelete() {
        String user = "thomasd";
        String tool = "Leatherman";
        String sessionId = "session mambo number five";
        IScyMessage message = ScyMessage.createScyMessage(user, tool, String.valueOf(this.hashCode()), this.getClass().getName(), "some name", "some description", null, null, null, CollaborationSession.DEFAULT_SESSION_EXPIRATION_TIME, sessionId);
        assertNotNull(message);
        try {
            getCS().create(message);
        } catch (CollaborationServiceException e) {
            logger.error("Failed to create. Odd. " + e);
            e.printStackTrace();
        }
        ArrayList<ICollaborationSession> sessions = getCS().getSessions(sessionId, user, tool);
        assertNotNull(sessions);
        assertTrue(sessions.size() > 0);
        ICollaborationSession session = (CollaborationSession) sessions.get(0);
        try {
            logger.debug("Session persistence id: " + session.getPersistenceId());
            getCS().delete(session.getPersistenceId());
        } catch (CollaborationServiceException e) {
            logger.error("Failed to delete. Odd. " + e);
            e.printStackTrace();
        }
        sessions = getCS().getSessions(sessionId, user, tool);
        assertNotNull(sessions);
        assertTrue(sessions.size() < 1);
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
