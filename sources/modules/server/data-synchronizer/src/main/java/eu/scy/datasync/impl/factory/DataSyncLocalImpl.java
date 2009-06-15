package eu.scy.datasync.impl.factory;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import eu.scy.datasync.adapter.IScyCommunicationListener;
import eu.scy.datasync.adapter.ScyCommunicationAdapter;
import eu.scy.datasync.adapter.ScyCommunicationAdapterHelper;
import eu.scy.datasync.adapter.ScyCommunicationEvent;
import eu.scy.datasync.api.DataSyncException;
import eu.scy.datasync.api.IDataSyncModule;
import eu.scy.datasync.api.ISyncMessage;
import eu.scy.datasync.api.event.IDataSyncListener;
import eu.scy.datasync.api.session.IDataSyncSession;
import eu.scy.datasync.impl.SyncMessage;
import eu.scy.datasync.impl.event.DataSyncEvent;
import eu.scy.datasync.impl.session.DataSyncSessionFactory;

/**
 * Implementation of the collaboration service that is local and does not use an
 * server to coordinate synchronization
 * 
 * @author anthonyp
 */
public class DataSyncLocalImpl implements IDataSyncModule {
    
    private static final Logger logger = Logger.getLogger(DataSyncLocalImpl.class.getName());
    
    private ScyCommunicationAdapter scyCommunicationAdapter;
    private ArrayList<IDataSyncListener> dataSyncListeners = new ArrayList<IDataSyncListener>();
    
    
    /**
     * Creates an instance of a local collaboration service
     */
    public DataSyncLocalImpl() {
        this.scyCommunicationAdapter = ScyCommunicationAdapterHelper.getInstance();
        this.scyCommunicationAdapter.addScyCommunicationListener(new IScyCommunicationListener() {
            
            @Override
            public void handleCommunicationEvent(ScyCommunicationEvent e) {
                // get the scy message and send back to the whos listening
                try {
                    sendCallBack(e.getSyncMessage());
                } catch (DataSyncException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }
    
    @Override
    public void addDataSyncListener(IDataSyncListener dataSyncListener) {
        dataSyncListeners.add(dataSyncListener);
    }
    
    @Override
    public void connect(String username, String password) throws DataSyncException {
    // no need to connect when local
    }
    
    @Override
    public void connect(String username, String password, String group) throws DataSyncException {
    // TODO Auto-generated method stub
    }
    
    @Override
    public void create(ISyncMessage syncMessage) throws DataSyncException {
        this.scyCommunicationAdapter.create(syncMessage);
    }
    
    @Override
    public void delete(String id) throws DataSyncException {
        this.scyCommunicationAdapter.delete(id);
    }
    
    @Override
    public ISyncMessage read(String id) throws DataSyncException {
        ISyncMessage read = this.scyCommunicationAdapter.read(id);
        // TODO call exeception
        return read;
    }
    
    @Override
    public void sendCallBack(ISyncMessage syncMessage) throws DataSyncException {
        for (IDataSyncListener cl : dataSyncListeners) {
            if (cl != null) {
                DataSyncEvent dataSyncEvent = new DataSyncEvent(this, syncMessage);
                cl.handleDataSyncEvent(dataSyncEvent);
            }
        }
    }
    
    @Override
    public void update(ISyncMessage syncMessage) throws DataSyncException {
        if (syncMessage.getPersistenceId() != null) {
            this.scyCommunicationAdapter.update(syncMessage);
        }
        throw new DataSyncException();
    }
    

    @Override
    public ArrayList<ISyncMessage> doQuery(ISyncMessage queryMessage) {
        return this.scyCommunicationAdapter.doQuery(queryMessage);
    }
    
    @Override
    public ArrayList<ISyncMessage> synchronizeClientState(String userName, String client, String session, boolean includeChangesByUser) {
        //would have been nice to do a precise query, instead of filtering away userName afterwards
        //ISyncMessage syncMessage = ((SyncMessage) SyncMessage).createScyMessage(null, client, null, null, SyncMessage.MESSAGE_TYPE_QUERY, SyncMessage.QUERY_TYPE_ALL, null, null, null, 0, session);
        ISyncMessage queryMessage = SyncMessage.createSyncMessage(session, client, userName, null, SyncMessage.MESSAGE_TYPE_QUERY, null, 0);
        ArrayList<ISyncMessage> messages = this.scyCommunicationAdapter.doQuery(queryMessage);
        if (includeChangesByUser) {
            return messages;
        }
        ArrayList<ISyncMessage> messagesFiltered = new ArrayList<ISyncMessage>();
        for (ISyncMessage syncMessage : messages) {
            if (!userName.equals(syncMessage.getFrom())) {
                messagesFiltered.add(syncMessage);
            }
        }
        return messagesFiltered;
    }

    @Override
    public IDataSyncSession createSession(String toolName, String userName) {
        IDataSyncSession dataSyncSession = DataSyncSessionFactory.getDataSyncSession(null, toolName, userName);
        ISyncMessage message = dataSyncSession.convertToSyncMessage();
        try {
            this.create(message);
        } catch (DataSyncException e) {
            logger.error("Failed to create ScyMessage: " + message.toString());
            e.printStackTrace();
        }
        return dataSyncSession;
    }
    
    @Override
    public IDataSyncSession joinSession(String session, String userName, String toolName) {
        IDataSyncSession iCollaborationSession = null;
        if (sessionExists(session, userName)) {
            logger.warn(userName + " is already member of session " + session);
        } 
        else if (sessionExists(session, null)) {
            iCollaborationSession = createSession(toolName, userName);
            logger.debug(userName + " is now a member of session " + session);
        } else {
            logger.error("could not find session: " + session);
        }
        return iCollaborationSession;
    }
    
    @Override
    public boolean sessionExists(String session, String userName) {
        return getSessions(session, userName, null).size() > 0;
    }
    
    @Override
    public ArrayList<IDataSyncSession> getSessions(String session, String userName, String toolName) {
        //ISyncMessage syncMessage = SyncMessage.createScyMessage(userName, toolName, null, null, ScyMessage.MESSAGE_TYPE_QUERY, ScyMessage.QUERY_TYPE_ALL, null, null, null, 0, session);        
        ISyncMessage queryMessage = SyncMessage.createSyncMessage(session, toolName, null, SyncMessage.MESSAGE_TYPE_QUERY, userName, null, 0);
        ArrayList<ISyncMessage> messages = this.doQuery(queryMessage);
        ArrayList<IDataSyncSession> sessions = new ArrayList<IDataSyncSession>();
        for (ISyncMessage message : messages) {
            sessions.add(DataSyncSessionFactory.getDataSyncSession(message));
        }
        return sessions;
    }
    
    
    public void cleanSession(String sessionId) {
        ArrayList<IDataSyncSession> sessions = getSessions(sessionId, null, null);
        for (IDataSyncSession collaborationSession : sessions) {
            try {
                this.delete(collaborationSession.getPersistenceId());
            } catch (DataSyncException e) {
                logger.error("Trouble while deleting session: " + e);
                e.printStackTrace();
            }
        }
    }

}
