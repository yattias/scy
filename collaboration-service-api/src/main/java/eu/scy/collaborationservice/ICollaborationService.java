package eu.scy.collaborationservice;

import eu.scy.collaborationservice.event.ICollaborationServiceListener;
import eu.scy.communications.message.ScyMessage;
import eu.scy.core.model.impl.ScyBaseObject;


/**
 * Collaboration Service Interface
 * 
 * @author anthonyp
 */
public interface ICollaborationService {

 
  /**
   * Do a create operation for a object to be syncronized
   * 
   * @param scyMessage
   * @return
   * @throws CollaborationServiceException
   */
  public String create(ScyMessage scyMessage) throws CollaborationServiceException;
  
  /**
   * Return a specific message based on id
   * 
   * @param id
   * @return
   */
  public ScyMessage read(String id);
  
  /**
   * Updates an object with the following id
   * 
   * @param scyMessage - update message
   * @param id - id of the object to update
   * @return 
   */
  public String update(ScyMessage scyMessage, String id);
  
  /**
   * Deletes the specified message
   * 
   * @param id of the object to delete
   *
   */
  public String delete(String id);
  
  
  /**
   * Gives the ability for a client to add a listener. When the collaboration service
   * updates it notify all clients listening.
   * 
   * @param collaborationListener
   */
  public void addCollaborationListener(ICollaborationServiceListener collaborationListener);
  
  /**
   * test method
   */ 
   public void sendCallBack(String something);
   
}
