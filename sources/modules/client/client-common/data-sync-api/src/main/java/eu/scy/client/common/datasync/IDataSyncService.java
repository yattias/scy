package eu.scy.client.common.datasync;

public interface IDataSyncService {

	/**
	 * creates a new session
	 * 
	 * @return the newly created ISyncSession
	 * @deprecated
	 * @throws DataSyncException
	 */
	public ISyncSession createSession(ISyncListener listener) throws DataSyncException;

	/**
	 * creates a new session for the specific tool
	 * 
	 * @return the newly created ISyncSession
	 * 
	 * @throws DataSyncException
	 */
	public ISyncSession createSession(ISyncListener listener, String toolid)
			throws DataSyncException;

	/**
	 * joins a session and registers an IDataSyncListener
	 * 
	 * @param mucID
	 *            a String that identifies the session to be joined, @see
	 *            ISyncSession#getId
	 * @param iSyncListener
	 *            the ISyncListener that will be registered to this session
	 * 
	 * @return the joined ISyncSession
	 * @throws DataSyncException 
	 * @deprecated use
	 *             {@link IDataSyncService#joinSession(String, ISyncListener, String)}
	 *             instead to provide a toolid
	 */
	public ISyncSession joinSession(String mucID, ISyncListener iSyncListener) throws DataSyncException;

	/**
	 * joins a session for a specific tool and registers an IDataSyncListener
	 * 
	 * @param mucID
	 *            a String that identifies the session to be joined, @see
	 *            ISyncSession#getId
	 * @param iSyncListener
	 *            the ISyncListener that will be registered to this session
	 * @param toolid
	 *            the toolid of the session
	 * 
	 * @return the joined ISyncSession
	 * @throws DataSyncException 
	 */
	public ISyncSession joinSession(String mucID, ISyncListener iSyncListener,
			String toolid) throws DataSyncException;

	/**
	 * Joins a session {@link IDataSyncService#joinSession(String, ISyncListener, String)}
	 * and returns a {@link ISyncSession} that contains the current state.
	 * 
	 * @param mucID
	 *            a String that identifies the session to be joined, @see
	 *            ISyncSession#getId
	 * @param iSyncListener
	 *            the ISyncListener that will be registered to this session
	 * @param toolid
	 *            the toolid of the session
	 * @param fetchState
	 *            whether to fetch the sessions content on startup or not
	 * @return
	 * @throws DataSyncException 
	 * @throws DataSyncException
	 */
	public ISyncSession joinSession(String mucID, ISyncListener iSyncListener,
			String toolid, boolean fetchState) throws DataSyncException;
}
