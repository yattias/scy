package eu.scy.datasync.api;

import java.util.ArrayList;

import eu.scy.communications.message.IScyMessage;
import eu.scy.datasync.api.event.IDataSyncListener;
import eu.scy.datasync.api.session.IDataSyncSession;


/**
 * DataSyncModule Interface
 * 
 * @author anthonyp
 */
public interface IDataSyncModule {

 
  /**
   * Do a create operation for a object to be syncronized
   * 
   * @param scyMessage
   * @return
   * @throws DataSyncException
   */
  public void create(IScyMessage scyMessage) throws DataSyncException;
  
  /**
   * Return a specific message based on id
   * 
   * @param id
   * @return
   * @throws DataSyncException
   */
  public IScyMessage read(String id) throws DataSyncException;
  
  /**
   * Updates an object with the following id
   * 
   * @param scyMessage - update message
   * @param id - id of the object to update
   * @return 
   * @throws DataSyncException
   */
  public void update(IScyMessage scyMessage, String id) throws DataSyncException;
  
  /**
   * Deletes the specified message
   * 
   * @param id of the object to delete
   * @throws DataSyncException
   */
  public void delete(String id) throws DataSyncException;
  
  
  /**
   * Gives the ability for a client to add a listener. When the dataSync module
   * updates it notify all clients listening.
   * 
   * @param dataSyncListener
   * @throws DataSyncException
   */
  public void addDataSyncListener(IDataSyncListener dataSyncListener);
  
   /**
    * Connects to the DataSync module of the specific implementation
    * 
    * @param username
    * @param password
    * @throws DataSyncException
    */
   public void connect(String username, String password) throws DataSyncException;
   
   /**
    * Connects to the dataSync module of the specific implementation
    * 
    * @param username
    * @param password
    * @param group
    * @throws DataSyncException
    */
   public void connect(String username, String password, String group) throws DataSyncException;
   
   /**
    * test method
    */ 
    public void sendCallBack(IScyMessage scyMessage) throws DataSyncException;

    /**
     * 
     * @param message defining the query
     * @return list of messages containing the query result
     */
    public ArrayList<IScyMessage> doQuery(IScyMessage queryMessage);

    /**
     * Gives the complete set of ScyMessges which represent changes for this specific client
     * 
     * @param userName - name of user
     * @param toolName - name of tool
     * @param session -  id of session shared by client instances
     * @param includeChangesByUser - true if the return should include changes done by the user 
     * 
     * @return arrayList
     */
    public ArrayList<IScyMessage> synchronizeClientState(String userName, String toolName, String session, boolean includeChangesByUser);    
    
    /**
     * Creates and returns an instance of IDataSyncSession
     * 
     * @param toolName - name of tool
     * @param userName - user name
     * @return IDataSyncSession
     */
    public IDataSyncSession createSession(String toolName, String userName);
    
    /**
     * Return an instance of IDataSyncSession if the user is not member and session exists
     * 
     * @param session - session to join
     * @param userName - user who wants to join
     * @param toolName - tool user is using as client
     * @return IDataSyncSession - session which was joined
     */
    public IDataSyncSession joinSession(String session, String userName, String toolName);
        
    /**
     * 
     * @param session - session
     * @param userName - userName
     * @return boolean
     */
    public boolean sessionExists(String session, String userName);
    
    /**
     * Returns all sessions matching the supplied params
     * 
     * @param session - session
     * @param userName - userName
     * @param toolName - userName
     * @return ArrayList<IDataSyncSession>
     */
    public ArrayList<IDataSyncSession> getSessions(String session, String userName, String toolName);
    
    /**
     * Removes all sessions which match sessionId
     * 
     * @param session - sessionId
     * 
     * @return void
     */
    public void cleanSession(String sessionId);
    
}
