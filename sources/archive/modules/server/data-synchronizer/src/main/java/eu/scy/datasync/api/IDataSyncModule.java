package eu.scy.datasync.api;

import java.util.ArrayList;

import eu.scy.communications.datasync.event.IDataSyncListener;
import eu.scy.communications.datasync.session.IDataSyncSession;
import eu.scy.communications.message.ISyncMessage;


/**
 * DataSyncModule Interface
 * 
 * @author thomasd
 */
public interface IDataSyncModule {

 
  /**
   * Do a create operation for a object to be syncronized
   * 
   * @param syncMessage
   * @throws DataSyncException
   */
  public void create(ISyncMessage syncMessage) throws DataSyncException;
  
  /**
   * Return a specific message based on id
   * 
   * @param id
   * @return ISyncMessage
   * @throws DataSyncException
   */
  public ISyncMessage read(String id) throws DataSyncException;
  
  /**
   * Updates an object. Update can only happen if object.persistenceId is set.
   * 
   * @param syncMessage - update message
   * @throws DataSyncException
   */
  public void update(ISyncMessage syncMessage) throws DataSyncException;
  
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
    public void sendCallBack(ISyncMessage syncMessage) throws DataSyncException;

    /**
     * 
     * @param message defining the query
     * @return list of messages containing the query result
     */
    public ArrayList<ISyncMessage> doQuery(ISyncMessage queryMessage);

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
    public ArrayList<ISyncMessage> synchronizeClientState(String userName, String toolName, String session, boolean includeChangesByUser);    

    /**
     * Gives the complete set of ScyMessges which represent changes for this specific client
     * 
     * @param syncMessage
     * @return arrayList
     */
    public ArrayList<ISyncMessage> synchronizeClientState(ISyncMessage syncMessage);    
    
    /**
     * Creates and returns an instance of IDataSyncSession
     * 
     * @param toolName - name of tool
     * @param userName - user name
     * @return IDataSyncSession
     * @throws DataSyncException 
     */
    public IDataSyncSession createSession(String toolName, String userName) throws DataSyncException;
    
    /**
     * Creates and returns an instance of IDataSyncSession
     * 
     * @param syncMessage - sync message
     * @return IDataSyncSession
     * @throws DataSyncException 
     */
    public IDataSyncSession createSession(ISyncMessage syncMessage) throws DataSyncException;
    
    /**
     * Return an instance of IDataSyncSession if the user is not member and session exists
     * 
     * @param session - session to join
     * @param userName - user who wants to join
     * @param toolName - tool user is using as client
     * @return IDataSyncSession - session which was joined
     * @throws DataSyncException 
     */
    public IDataSyncSession joinSession(String session, String userName, String toolName) throws DataSyncException;
        
    /**
     * Joins a session with a syncMessage
     * 
     * @param syncMessage
     * @return
     */
    public IDataSyncSession joinSession(ISyncMessage syncMessage);
    
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
     * gets the session base on query
     * 
     * @param syncMessage
     * @return
     */
    public ArrayList<IDataSyncSession> getSessions(ISyncMessage syncMessage);

   
    /**
     * Removes all sessions which match sessionId
     * 
     * @param session - sessionId
     * 
     */
    public void cleanSession(String sessionId);

    /**
     * Process the SyncMessage
     * 
     * @param packet
     * 
     */
    public void processSyncMessage(ISyncMessage message) throws DataSyncUnkownEventException;




    
}
