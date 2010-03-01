package eu.scy.client.common.datasync;

public interface IDataSyncService {


  /**
   * creates a new session
   * 
   * @return the newly created ISyncSession
   *
   * @throws Exception 
   */
  public ISyncSession createSession(ISyncListener listener) throws Exception;

  /**
   * joins a session and registers an IDataSyncListener
   *
   * @param mucID a String that identifies the session to be joined, @see ISyncSession#getId
   * @param iSyncListener the ISyncListener that will be registered to this session
   *
   * @return the joined ISyncSession
   */
  public ISyncSession joinSession(String mucID, ISyncListener iSyncListener);
  
  /**
   * leaves a session and de-registers an IDataSyncListener
   *
   * @param iSyncSession the ISyncSession that should be left
   * @param iSyncListener the ISyncListener that will be de-registered from the given session
   *
   */
  public void leaveSession(ISyncSession iSyncSession, ISyncListener iSyncListener);

}
