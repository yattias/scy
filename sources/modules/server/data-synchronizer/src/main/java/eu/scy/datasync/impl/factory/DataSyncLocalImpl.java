package eu.scy.datasync.impl.factory;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import eu.scy.communications.message.IScyMessage;
import eu.scy.communications.message.impl.ScyMessage;
import eu.scy.datasync.adapter.IScyCommunicationListener;
import eu.scy.datasync.adapter.ScyCommunicationAdapter;
import eu.scy.datasync.adapter.ScyCommunicationAdapterHelper;
import eu.scy.datasync.adapter.ScyCommunicationEvent;
import eu.scy.datasync.api.DataSyncException;
import eu.scy.datasync.api.IDataSyncModule;
import eu.scy.datasync.api.event.IDataSyncListener;
import eu.scy.datasync.api.session.IDataSyncSession;
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
    private ArrayList<IDataSyncListener> collaborationListeners = new ArrayList<IDataSyncListener>();
    
    
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
                    sendCallBack(e.getScyMessage());
                } catch (DataSyncException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }
    
    @Override
    public void addCollaborationListener(IDataSyncListener collaborationListener) {
        collaborationListeners.add(collaborationListener);
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
    public void create(IScyMessage scyMessage) throws DataSyncException {
        this.scyCommunicationAdapter.create(scyMessage);
    }
    
    @Override
    public void delete(String id) throws DataSyncException {
        this.scyCommunicationAdapter.delete(id);
    }
    
    @Override
    public IScyMessage read(String id) throws DataSyncException {
        IScyMessage read = this.scyCommunicationAdapter.read(id);
        // TODO call exeception
        return read;
    }
    
    @Override
    public void sendCallBack(IScyMessage scyMessage) throws DataSyncException {
        for (IDataSyncListener cl : collaborationListeners) {
            if (cl != null) {
                DataSyncEvent collaborationEvent = new DataSyncEvent(this, scyMessage);
                cl.handleDataSyncEvent(collaborationEvent);
            }
        }
    }
    
    @Override
    public void update(IScyMessage scyMessage, String id) throws DataSyncException {
        this.scyCommunicationAdapter.update(scyMessage, id);
    }
    

    @Override
    public ArrayList<IScyMessage> doQuery(IScyMessage queryMessage) {
        return this.scyCommunicationAdapter.doQuery(queryMessage);
    }
    
    @Override
    public ArrayList<IScyMessage> synchronizeClientState(String userName, String client, String session, boolean includeChangesByUser) {
        //would have been nice to do a precise query, instead of filtering away userName afterwards
        IScyMessage scyMessage = ScyMessage.createScyMessage(null, client, null, null, ScyMessage.MESSAGE_TYPE_QUERY, ScyMessage.QUERY_TYPE_ALL, null, null, null, 0, session);
        ArrayList<IScyMessage> messages = this.scyCommunicationAdapter.doQuery(scyMessage);
        if (includeChangesByUser) {
            return messages;
        }
        ArrayList<IScyMessage> messagesFiltered = new ArrayList<IScyMessage>();
        for (IScyMessage iScyMessage : messages) {
            if (!userName.equals(iScyMessage.getUserName())) {
                messagesFiltered.add(iScyMessage);
            }
        }
        return messagesFiltered;
    }

    @Override
    public IDataSyncSession createSession(String toolName, String userName) {
        IDataSyncSession collaborationSession = DataSyncSessionFactory.getCollaborationSession(null, toolName, userName);
        IScyMessage message = collaborationSession.convertToScyMessage();
        try {
            this.create(message);
        } catch (DataSyncException e) {
            logger.error("Failed to create ScyMessage: " + message.toString());
            e.printStackTrace();
        }
        return collaborationSession;
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
        IScyMessage scyMessage = ScyMessage.createScyMessage(userName, toolName, null, null, ScyMessage.MESSAGE_TYPE_QUERY, ScyMessage.QUERY_TYPE_ALL, null, null, null, 0, session);        
        ArrayList<IScyMessage> messages = this.doQuery(scyMessage);
        ArrayList<IDataSyncSession> sessions = new ArrayList<IDataSyncSession>();
        for (IScyMessage message : messages) {
            sessions.add(DataSyncSessionFactory.getCollaborationSession(message));
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
