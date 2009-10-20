package eu.scy.client.common.datasync;

public interface IDataSyncService {

	 /**
    * initialize the connection
    * 
    * @param connection
	 *
	 * as we get the datasyncservice instance from the TBI, an "init" may not be needed
	 * the tbi will probably also handle the XMPPConnection 
    */
  //public void init(XMPPConnection connection);
	
  /**
   * creates a new session
   * 
   * @return SessionHandle unique identifier (e.g. an UUID) for a session, needed for joining and
	* leaving sessions
	*
	* do we need parameters to specify characteristics of a session?
   */
  public ISyncSession createSession(ISyncListener listener);

  /**
   * joins a session, i.e. registers an IDataSyncListener
	*
	* @param String mucID identifies the session to be joined by means of MultiUserChat identifier
	* @param iDataSyncListener an IDataSyncListener that will be called whenever something happens in the session
   */
  public ISyncSession joinSession(String mucID, ISyncListener iDataSyncListener);
 
  
  /**
   * disconnect the xmpp session
   * 
	* do we need this? also see "init()"
   */
  //public void disconnect();

}
