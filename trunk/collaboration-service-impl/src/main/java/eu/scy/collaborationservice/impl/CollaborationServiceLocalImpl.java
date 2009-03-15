package eu.scy.collaborationservice.impl;

import java.util.ArrayList;

import eu.scy.collaborationservice.CollaborationServiceException;
import eu.scy.collaborationservice.ICollaborationService;
import eu.scy.collaborationservice.event.CollaborationServiceEvent;
import eu.scy.collaborationservice.event.ICollaborationServiceListener;
import eu.scy.communications.adapter.IScyCommunicationListener;
import eu.scy.communications.adapter.ScyCommunicationAdapter;
import eu.scy.communications.adapter.ScyCommunicationAdapterHelper;
import eu.scy.communications.adapter.ScyCommunicationEvent;
import eu.scy.communications.message.IScyMessage;
import eu.scy.communications.message.impl.ScyMessage;

/**
 * Implementation of the collaboration service that is local and does not use an
 * server to coordinate synchronization
 * 
 * @author anthonyp
 */
public class CollaborationServiceLocalImpl implements ICollaborationService {
    
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
            }// if
        }// for
    }
    
    @Override
    public void update(IScyMessage scyMessage, String id) throws CollaborationServiceException {
        this.scyCommunicationAdapter.update(scyMessage, id);
    }
    

    @Override
    public ArrayList<IScyMessage> doQuery(ScyMessage queryMessage) {
        return this.scyCommunicationAdapter.doQuery(queryMessage);
    }
    
    @Override
    public ArrayList<IScyMessage> synchronizeClientState(String client, String session) {
        IScyMessage scyMessage = ScyMessage.createScyMessage("anthonjp", client, null, null, ScyMessage.MESSAGE_TYPE_QUERY, ScyMessage.QUERY_TYPE_ALL, null, null, null, 0, session);
        return this.scyCommunicationAdapter.doQuery(scyMessage);
    }
    
}
