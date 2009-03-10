package eu.scy.collaborationservice.impl;

import eu.scy.collaborationservice.CollaborationServiceException;
import eu.scy.collaborationservice.ICollaborationService;
import eu.scy.collaborationservice.event.ICollaborationServiceListener;
import eu.scy.communications.message.ScyMessage;
import eu.scy.core.model.impl.ScyBaseObject;

/**
 * Implementation of the collaboration service that is local and does  not use an server to 
 * coordinate synchronization
 * 
 * @author anthonyp
 */
public class CollaborationServiceLocalImpl implements ICollaborationService {

    @Override
    public void addCollaborationListener(ICollaborationServiceListener collaborationListener) throws CollaborationServiceException {
    }

    @Override
    public void connect(String username, String password) throws CollaborationServiceException {
    }

    @Override
    public void create(ScyMessage scyMessage) throws CollaborationServiceException {
    }

    @Override
    public void delete(String id) throws CollaborationServiceException {
    }

    @Override
    public ScyMessage read(String id) throws CollaborationServiceException {
        return null;
    }

    @Override
    public void sendCallBack(String something) throws CollaborationServiceException {
    }

    @Override
    public void update(ScyMessage scyMessage, String id) throws CollaborationServiceException {
    }
  
}
