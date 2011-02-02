package eu.scy.collaborationservice.session;

import java.util.UUID;

import org.apache.log4j.Logger;

import eu.scy.communications.message.IScyMessage;

/**
 * Class that implememts the factory design pattern. Gives the ability
 * to create many different collaboration sessions that implement the standard interfaces and
 * have an efficient way to access them
 * 
 * @author thomasd
 *
 */
public class CollaborationSessionFactory {
    
    private final static Logger logger = Logger.getLogger(CollaborationSessionFactory.class.getName());

    /**
     * Factory method
     * 
     * @param sessionId
     * @param toolName
     * @param userName
     * 
     * @return ICollaborationSession
     */
    public static ICollaborationSession getCollaborationSession(String sessionId, String toolName, String userName) {
        return getCollaborationSession(sessionId, toolName, userName, null);
    }
    
    
    /**
     * Factory method
     * 
     * @param sessionId
     * @param toolName
     * @param userName
     * @param persistenceId
     * 
     * @return ICollaborationSession
     */
    public static ICollaborationSession getCollaborationSession(String sessionId, String toolName, String userName, String persistenceId) {
        ICollaborationSession session = new CollaborationSession();
        if (sessionId == null) {
            session.setId(UUID.randomUUID().toString());            
        } else {
            session.setId(sessionId);
        }
        session.setPersistenceId(persistenceId);
        session.setTool(toolName);
        session.setUser(userName);
        return session;
    }

    
    /**
     * Factory method
     * 
     * @param scyMessage
     * @return ICollaborationSession
     */
    public static ICollaborationSession getCollaborationSession(IScyMessage scyMessage) {
        return getCollaborationSession(scyMessage.getSession(), scyMessage.getToolName(), scyMessage.getUserName(), scyMessage.getId());
    }

}
