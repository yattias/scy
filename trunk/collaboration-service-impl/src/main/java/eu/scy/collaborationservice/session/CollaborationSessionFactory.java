package eu.scy.collaborationservice.session;

import java.util.UUID;

import eu.scy.collaborationservice.impl.CollaborationServiceLocalImpl;
import eu.scy.collaborationservice.impl.CollaborationServiceXMPPImpl;
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

    /**
     * Factory method
     * 
     * @param toolName
     * @param userName
     * @return ICollaborationSession
     */
    public static ICollaborationSession getCollaborationSession(String id, String toolName, String userName) {
        ICollaborationSession session = new CollaborationSession();
        if (id == null) {
            session.setId(UUID.randomUUID().toString());            
        } else {
            session.setId(id);   
        }
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
        return getCollaborationSession(scyMessage.getSession(), scyMessage.getToolName(), scyMessage.getUserName());
    }

}
