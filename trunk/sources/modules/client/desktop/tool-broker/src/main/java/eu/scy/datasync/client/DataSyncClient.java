package eu.scy.datasync.client;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import eu.scy.datasync.adapter.ScyCommunicationAdapter;
import eu.scy.datasync.api.DataSyncException;
import eu.scy.datasync.api.ISyncMessage;
import eu.scy.datasync.api.event.IDataSyncListener;
import eu.scy.datasync.api.session.IDataSyncSession;
import eu.scy.datasync.impl.SyncMessage;
import eu.scy.datasync.impl.factory.DataSyncLocalImpl;
import eu.scy.datasync.impl.session.DataSyncSessionFactory;


/**
 * Tool client for doing data sync
 * 
 * @author thomasd
 *
 */
public class DataSyncClient {
    
    private static final Logger logger = Logger.getLogger(DataSyncClient.class.getName());
    
    private ScyCommunicationAdapter scyCommunicationAdapter;
    private ArrayList<IDataSyncListener> dataSyncListeners = new ArrayList<IDataSyncListener>();

    

//    public IDataSyncSession createSession(String toolName, String userName) {
//        IDataSyncSession dataSyncSession = DataSyncSessionFactory.getDataSyncSession(null, toolName, userName);
//        ISyncMessage message = dataSyncSession.convertToSyncMessage();
//        try {
//            this.create(message);
//        } catch (DataSyncException e) {
//            logger.error("Failed to create ScyMessage: " + message.toString());
//            e.printStackTrace();
//        }
//        return dataSyncSession;
//    }
//    
//    
//    public ArrayList<IDataSyncSession> getSessions(String session, String userName, String toolName) {
//        //ISyncMessage syncMessage = SyncMessage.createScyMessage(userName, toolName, null, null, ScyMessage.MESSAGE_TYPE_QUERY, ScyMessage.QUERY_TYPE_ALL, null, null, null, 0, session);        
//        ISyncMessage queryMessage = SyncMessage.createSyncMessage(session, toolName, null, SyncMessage.MESSAGE_TYPE_QUERY, userName, null, 0);
//        ArrayList<ISyncMessage> messages = this.doQuery(queryMessage);
//        ArrayList<IDataSyncSession> sessions = new ArrayList<IDataSyncSession>();
//        for (ISyncMessage message : messages) {
//            sessions.add(DataSyncSessionFactory.getDataSyncSession(message));
//        }
//        return sessions;
//    }
//    
//    
//    public IDataSyncSession joinSession(String session, String userName, String toolName) {
//        IDataSyncSession iCollaborationSession = null;
//        if (sessionExists(session, userName)) {
//            logger.warn(userName + " is already member of session " + session);
//        } 
//        else if (sessionExists(session, null)) {
//            iCollaborationSession = createSession(toolName, userName);
//            logger.debug(userName + " is now a member of session " + session);
//        } else {
//            logger.error("could not find session: " + session);
//        }
//        return iCollaborationSession;
//    }
//    
//    
//    public boolean sessionExists(String session, String userName) {
//        return getSessions(session, userName, null).size() > 0;
//    }
//    
//    
//    public ArrayList<ISyncMessage> synchronizeClientState(String userName, String client, String session, boolean includeChangesByUser) {
//        //would have been nice to do a precise query, instead of filtering away userName afterwards
//        //ISyncMessage syncMessage = ((SyncMessage) SyncMessage).createScyMessage(null, client, null, null, SyncMessage.MESSAGE_TYPE_QUERY, SyncMessage.QUERY_TYPE_ALL, null, null, null, 0, session);
//        ISyncMessage queryMessage = SyncMessage.createSyncMessage(session, client, userName, null, SyncMessage.MESSAGE_TYPE_QUERY, null, 0);
//        ArrayList<ISyncMessage> messages = this.scyCommunicationAdapter.doQuery(queryMessage);
//        if (includeChangesByUser) {
//            return messages;
//        }
//        ArrayList<ISyncMessage> messagesFiltered = new ArrayList<ISyncMessage>();
//        for (ISyncMessage syncMessage : messages) {
//            if (!userName.equals(syncMessage.getFrom())) {
//                messagesFiltered.add(syncMessage);
//            }
//        }
//        return messagesFiltered;
//    }
//    
//    
//    public void cleanSession(String sessionId) {
//        ArrayList<IDataSyncSession> sessions = getSessions(sessionId, null, null);
//        for (IDataSyncSession collaborationSession : sessions) {
//            try {
//                this.delete(collaborationSession.getPersistenceId());
//            } catch (DataSyncException e) {
//                logger.error("Trouble while deleting session: " + e);
//                e.printStackTrace();
//            }
//        }
//    }
//    
    
}



