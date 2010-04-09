package eu.scy.collaborationservice.impl;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import eu.scy.collaborationservice.CollaborationServiceException;
import eu.scy.collaborationservice.ICollaborationService;
import eu.scy.collaborationservice.adapter.IScyCommunicationListener;
import eu.scy.collaborationservice.adapter.ScyCommunicationAdapter;
import eu.scy.collaborationservice.adapter.ScyCommunicationAdapterHelper;
import eu.scy.collaborationservice.adapter.ScyCommunicationEvent;
import eu.scy.collaborationservice.event.CollaborationServiceEvent;
import eu.scy.collaborationservice.event.ICollaborationServiceListener;
import eu.scy.collaborationservice.session.CollaborationSessionFactory;
import eu.scy.collaborationservice.session.ICollaborationSession;
import eu.scy.communications.message.IScyMessage;
import eu.scy.communications.message.impl.ScyMessage;

/**
 * Implementation of the collaboration service that is local and does not use an
 * server to coordinate synchronization
 * 
 * @author anthonyp
 */
public class CollaborationServiceLocalImpl implements ICollaborationService {
    
    private static final Logger logger = Logger.getLogger(CollaborationServiceLocalImpl.class.getName());
    
    private ScyCommunicationAdapter scyCommunicationAdapter;
    private ArrayList<ICollaborationServiceListener> collaborationListeners = new ArrayList<ICollaborationServiceListener>();
    
    
    /**
     * Creates an instance of a local collaboration service
     */
    public CollaborationServiceLocalImpl() {
        this.scyCommunicationAdapter = ScyCommunicationAdapterHelper.getInstance();
        this.scyCommunicationAdapter.addScyCommunicationListener(new IScyCommunicationListener() {
            
            @Override
            public void handleCommunicationEvent(ScyCommunicationEvent e) {
                // get the scy message and send back to the whos listening
                try {
                    sendCallBack(e.getScyMessage());
                } catch (CollaborationServiceException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }
    
    @Override
    public void addCollaborationListener(ICollaborationServiceListener collaborationListener) {
        collaborationListeners.add(collaborationListener);
    }
    
    @Override
    public void connect(String username, String password) throws CollaborationServiceException {
    // no need to connect when local
    }
    
    @Override
    public void connect(String username, String password, String group) throws CollaborationServiceException {
    // TODO Auto-generated method stub
    }
    
    @Override
    public void create(IScyMessage scyMessage) throws CollaborationServiceException {
        this.scyCommunicationAdapter.create(scyMessage);
    }
    
    @Override
    public void delete(String id) throws CollaborationServiceException {
        this.scyCommunicationAdapter.delete(id);
    }
    
    @Override
    public IScyMessage read(String id) throws CollaborationServiceException {
        IScyMessage read = this.scyCommunicationAdapter.read(id);
        // TODO call exeception
        return read;
    }
    
    @Override
    public void sendCallBack(IScyMessage scyMessage) throws CollaborationServiceException {
        for (ICollaborationServiceListener cl : collaborationListeners) {
            if (cl != null) {
                CollaborationServiceEvent collaborationEvent = new CollaborationServiceEvent(this, scyMessage);
                cl.handleCollaborationServiceEvent(collaborationEvent);
            }
        }
    }
    
    @Override
    public void update(IScyMessage scyMessage, String id) throws CollaborationServiceException {
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
    public ICollaborationSession createSession(String toolName, String userName) {
        ICollaborationSession collaborationSession = CollaborationSessionFactory.getCollaborationSession(null, toolName, userName);
        IScyMessage message = collaborationSession.convertToScyMessage();
        try {
            this.create(message);
        } catch (CollaborationServiceException e) {
            logger.error("Failed to create ScyMessage: " + message.toString());
            e.printStackTrace();
        }
        return collaborationSession;
    }
    
    @Override
    public ICollaborationSession joinSession(String session, String userName, String toolName) {
        ICollaborationSession iCollaborationSession = null;
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
    public ArrayList<ICollaborationSession> getSessions(String session, String userName, String toolName) {
        IScyMessage scyMessage = ScyMessage.createScyMessage(userName, toolName, null, null, ScyMessage.MESSAGE_TYPE_QUERY, ScyMessage.QUERY_TYPE_ALL, null, null, null, 0, session);        
        ArrayList<IScyMessage> messages = this.doQuery(scyMessage);
        ArrayList<ICollaborationSession> sessions = new ArrayList<ICollaborationSession>();
        for (IScyMessage message : messages) {
            sessions.add(CollaborationSessionFactory.getCollaborationSession(message));
        }
        return sessions;
    }
    
    
    public void cleanSession(String sessionId) {
        ArrayList<ICollaborationSession> sessions = getSessions(sessionId, null, null);
        for (ICollaborationSession collaborationSession : sessions) {
            try {
                this.delete(collaborationSession.getPersistenceId());
            } catch (CollaborationServiceException e) {
                logger.error("Trouble while deleting session: " + e);
                e.printStackTrace();
            }
        }
    }

}
