package eu.scy.datasync.impl.session;

import java.util.UUID;

import org.apache.log4j.Logger;

import eu.scy.communications.message.ISyncMessage;
import eu.scy.datasync.api.session.IDataSyncSession;

/**
 * Class that implememts the factory design pattern. Gives the ability
 * to create many different data sync sessions that implement the standard interfaces and
 * have an efficient way to access them
 * 
 * @author thomasd
 *
 */
public class DataSyncSessionFactory {
    
    private final static Logger logger = Logger.getLogger(DataSyncSessionFactory.class.getName());

    /**
     * Factory method
     * 
     * @param sessionId
     * @param toolName
     * @param userName
     * 
     * @return IDataSyncSession
     */
    public static IDataSyncSession getDataSyncSession(String sessionId, String toolName, String userName) {
        return getDataSyncSession(sessionId, toolName, userName, null);
    }
    
    
    /**
     * Factory method
     * 
     * @param sessionId
     * @param toolName
     * @param userName
     * @param persistenceId
     * 
     * @return IDataSyncSession
     */
    public static IDataSyncSession getDataSyncSession(String sessionId, String toolName, String userName, String persistenceId) {
        IDataSyncSession session = new DataSyncSession();
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
     * @param syncMessage
     * @return IDataSyncSession
     */
    public static IDataSyncSession getDataSyncSession(ISyncMessage syncMessage) {
        return getDataSyncSession(syncMessage.getToolSessionId(), syncMessage.getToolId(), syncMessage.getFrom(), syncMessage.getPersistenceId());
    }

}
