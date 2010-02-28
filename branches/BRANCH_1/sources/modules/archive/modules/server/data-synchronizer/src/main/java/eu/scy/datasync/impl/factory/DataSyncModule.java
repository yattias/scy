package eu.scy.datasync.impl.factory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import eu.scy.common.configuration.Configuration;
import eu.scy.communications.datasync.event.DataSyncEvent;
import eu.scy.communications.datasync.event.IDataSyncListener;
import eu.scy.communications.datasync.properties.CommunicationProperties;
import eu.scy.communications.datasync.session.DataSyncSessionFactory;
import eu.scy.communications.datasync.session.IDataSyncSession;
import eu.scy.communications.message.ISyncMessage;
import eu.scy.communications.message.impl.SyncMessage;
import eu.scy.communications.message.impl.SyncMessageHelper;
import eu.scy.datasync.adapter.IScyCommunicationListener;
import eu.scy.datasync.adapter.ScyCommunicationAdapter;
import eu.scy.datasync.adapter.ScyCommunicationAdapterHelper;
import eu.scy.datasync.adapter.ScyCommunicationEvent;
import eu.scy.datasync.api.DataSyncException;
import eu.scy.datasync.api.DataSyncUnkownEventException;
import eu.scy.datasync.api.IDataSyncModule;


/**
 * Implementation of the collaboration service that is local and does not use an
 * server to coordinate synchronization
 * 
 * @author anthonyp
 */
public class DataSyncModule implements IDataSyncModule {
    
    private static final Logger logger = Logger.getLogger(DataSyncModule.class.getName());

    private ScyCommunicationAdapter scyCommunicationAdapter;
    private ArrayList<IDataSyncListener> dataSyncListeners = new ArrayList<IDataSyncListener>();
    private Configuration props = Configuration.getInstance();
    private Map<String, IDataSyncSession> sessionMap = new HashMap<String, IDataSyncSession>();
    
    
    /**
     * Creates an instance of a local collaboration service
     */
    public DataSyncModule() {
        logger.info("======================================================================");
        logger.info("======================================================================");
        logger.info("======================================================================");
        logger.info("======================================================================");
        logger.info("CREATING NON SPRING BASED DATA SYNC MODULE");
        logger.info("======================================================================");
        logger.info("======================================================================");
        logger.info("======================================================================");

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
        
        ISyncMessage allSessions = SyncMessageHelper.createSyncMessageWithDefaultExp(null, null, null, null, null, props.getClientEventCreateSession(), null);
        ArrayList<IDataSyncSession> sss = getSessions(allSessions);
        
        if (sss != null && !sss.isEmpty()) {
            for (IDataSyncSession iDataSyncSession : sss) {
                sessionMap.put(iDataSyncSession.getId(), iDataSyncSession);
            }
        }
    }

    public DataSyncModule(ScyCommunicationAdapter scyCommunicationAdapter) {
        logger.info("---------------------------------------------------");
        logger.info("-- CREATING SPRING BASED DATA SYNC MODULE");
        logger.info("---------------------------------------------------");
        this.scyCommunicationAdapter = scyCommunicationAdapter;
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

        ISyncMessage allSessions = SyncMessageHelper.createSyncMessageWithDefaultExp(null, null, null, null, null, props.getClientEventCreateSession(), null);
        ArrayList<IDataSyncSession> sss = getSessions(allSessions);

        if (sss != null && !sss.isEmpty()) {
            for (IDataSyncSession iDataSyncSession : sss) {
                sessionMap.put(iDataSyncSession.getId(), iDataSyncSession);
            }
        }


    }
    
    @Override
    public void processSyncMessage(ISyncMessage message) throws DataSyncUnkownEventException {
        
        //check event
        if (props.getClientEventCreateData().equals(message.getEvent())) {
            try {
                create(message);
            } catch (DataSyncException e) {
                logger.error("An event we did not anticipate " + message.getEvent());
                e.printStackTrace();
            }            
        }
        else {
            throw new DataSyncUnkownEventException();
        }        
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
        } else {
            // TODO also call exeception if update fails
            throw new DataSyncException();            
        }
    } 
    
    @Override
    public ArrayList<ISyncMessage> doQuery(ISyncMessage queryMessage) {
        return this.scyCommunicationAdapter.doQuery(queryMessage);
    }
    

    @Override
    public ArrayList<ISyncMessage> synchronizeClientState(ISyncMessage syncMessage) {
        return synchronizeClientState(syncMessage.getFrom(), syncMessage.getToolId(), syncMessage.getToolSessionId(), true);
    }
    
    
    @Override
    public ArrayList<ISyncMessage> synchronizeClientState(String userName, String toolId, String session, boolean includeChangesByUser) {
        //would have been nice to do a precise query, instead of filtering away userName afterwards
        ISyncMessage queryMessage = SyncMessageHelper.createSyncMessage(session, null, null, null, null, props.getClientEventCreateData(), null, 0);
        ArrayList<ISyncMessage> messages = this.scyCommunicationAdapter.doQuery(queryMessage);

        ArrayList<ISyncMessage> messagesFiltered = new ArrayList<ISyncMessage>();
        if (includeChangesByUser) {
            messagesFiltered = messages;
        } else {
            for (ISyncMessage syncMessage : messages) {
                if (!userName.equals(syncMessage.getFrom())) {
                    messagesFiltered.add(syncMessage);
                }
            }
        }
        
        for (ISyncMessage syncMessage : messagesFiltered) {
            syncMessage.setEvent(props.getClientEventSynchronize());
            syncMessage.setFrom(userName);
            for (IDataSyncListener cl : dataSyncListeners) {
                if (cl != null) {
                    DataSyncEvent dataSyncEvent = new DataSyncEvent(this, syncMessage);
                    cl.handleDataSyncEvent(dataSyncEvent);
                }
            }                    
        }
        
        return messagesFiltered;
    }

    @Override
    public IDataSyncSession createSession(String toolName, String userName) throws DataSyncException {
        IDataSyncSession dataSyncSession = DataSyncSessionFactory.getDataSyncSession(null, toolName, userName);
        
        //add to the map
        sessionMap.put(dataSyncSession.getId(), dataSyncSession);
        
        ISyncMessage sessionMessage = SyncMessageHelper.createSyncMessageWithDefaultExp(dataSyncSession.getId(), dataSyncSession.getToolId(), userName, userName,null, props.getClientEventCreateSession() , null);
        this.create(sessionMessage);
        return dataSyncSession;
    }
    
    @Override
    public IDataSyncSession createSession(ISyncMessage syncMessage) throws DataSyncException {
        return this.createSession(syncMessage.getToolId(), syncMessage.getFrom());
    }
    
    @Override
    public IDataSyncSession joinSession(ISyncMessage syncMessage) {
        return this.joinSession(syncMessage.getToolSessionId(), syncMessage.getFrom(), syncMessage.getToolId());
        
    }
    @Override
    public IDataSyncSession joinSession(String sessionId, String userName, String toolName) {
        IDataSyncSession dataSyncSession = null;
        
        //if the session doesnt exist create a new one
        //else add the user to the existing one
        if( !sessionMap.containsKey(sessionId) ) {
            try {
                dataSyncSession = createSession(toolName, userName);
                
            } catch (DataSyncException e) {
                e.printStackTrace();
                return null;
            }
        } else if( sessionMap.containsKey(sessionId)) {
            dataSyncSession = sessionMap.get(sessionId);
            dataSyncSession.addUser(userName);
        }
        
        ISyncMessage joinSessionMessage = SyncMessageHelper.createSyncMessage(dataSyncSession.getId(), dataSyncSession.getToolId(), userName, userName, null, props.getClientEventJoinSession(), null, 60*60*1000*24);
        try {
            this.create(joinSessionMessage);
        } catch (DataSyncException e) {
            e.printStackTrace();
        }
        
//        if (sessionExists(sessionId, userName)) {
//            logger.warn(userName + " is already member of session " + sessionId);
//        } 
//        else if (sessionExists(sessionId, null)) {
//            try {
//                dataSyncSession = createSession(toolName, userName);
//            } catch (DataSyncException e) {
//                e.printStackTrace();
//            }
//            logger.debug(userName + " is now a member of session " + sessionId);
//        } else {
//            logger.error("could not find session: " + sessionId);
//        }
        return dataSyncSession;
    }
    
    @Override
    public boolean sessionExists(String session, String userName) {
        return getSessions(session, userName, null).size() > 0;
    }
    
    @Override
    public ArrayList<IDataSyncSession> getSessions(ISyncMessage syncMessage) {
       return getSessions(syncMessage.getToolSessionId(), syncMessage.getFrom(),syncMessage.getToolId());
    }
    
    @Override
    public ArrayList<IDataSyncSession> getSessions(String session, String userName, String toolName) {
        ISyncMessage queryMessage = SyncMessageHelper.createSyncMessage(session, toolName, null, userName, null, props.getClientEventCreateSession(), null, 0);
        ArrayList<ISyncMessage> messages = this.doQuery(queryMessage);
        ArrayList<IDataSyncSession> sessions = new ArrayList<IDataSyncSession>();
        StringBuilder newGirlFriend = new StringBuilder();
        ISyncMessage message;
        for (int i = 0 ; i < messages.size() ; i++) {
            if (i > 0) {
                newGirlFriend.append(", ");
            }
            message = messages.get(i);
            IDataSyncSession dataSyncSession = DataSyncSessionFactory.getDataSyncSession(message);
            sessions.add(dataSyncSession);
            newGirlFriend.append(dataSyncSession.getId());
        }
        
        //TODO rewrite to xml
        //contruct uber message
        ISyncMessage resultMessage = new SyncMessage();
        resultMessage.setContent(newGirlFriend.toString());
        resultMessage.setEvent(props.getClientEventGetSessions());
        resultMessage.setFrom(userName);
        for (IDataSyncListener cl : dataSyncListeners) {
            if (cl != null) {
                DataSyncEvent dataSyncEvent = new DataSyncEvent(this, resultMessage);
                cl.handleDataSyncEvent(dataSyncEvent);
            }
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
