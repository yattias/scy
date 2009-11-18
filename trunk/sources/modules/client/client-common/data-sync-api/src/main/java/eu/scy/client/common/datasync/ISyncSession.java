package eu.scy.client.common.datasync;

import java.util.List;

import eu.scy.common.datasync.ISyncObject;

public interface ISyncSession {

	/**
	 * adds a SyncObject to a session
	 * 
	 * @param sessionHandle
	 *            an identifier for the session to which the SyncObject will be
	 *            added
	 * @param syncObject
	 *            the SyncObject to be added
	 */
	public void addSyncObject(ISyncObject syncObject);

	/**
	 * changes a SyncObject in a session. if the SyncObject to be changed is
	 * currently not in the session (according to its ID), a new SyncObject will
	 * be created and a "syncObjectAdded()" will be triggered instead.
	 * 
	 * @param sessionHandle
	 *            an identifier for the session in which the SyncObject will be
	 *            changed
	 * @param syncObject
	 *            the SyncObject to be changed
	 * 
	 *            shall we throw an exception instead?
	 */
	public void changeSyncObject(ISyncObject syncObject);

	/**
	 * removes a SyncObject in a session. if the SyncObject to be removed is
	 * currently not in the session (according to its ID), nothing happens.
	 * 
	 * @param sessionHandle
	 *            an identifier for the session from which the SyncObject will
	 *            be removed
	 * @param syncObject
	 *            the SyncObject to be changed
	 * 
	 *            shall we throw an exception instead?
	 */
	public void removeSyncObject(ISyncObject syncObject);

	/**
	 * returns all SyncObjects in a session. if the parameterized session
	 * doesn`t exist, null will be returned; if the session is empty, an empty
	 * List will be returned.
	 * 
	 * @param sessionHandle
	 *            an identifier for the session to which the SyncObject will be
	 *            added
	 * @param syncObject
	 *            the SyncObject to be changed
	 * 
	 */
	public List<ISyncObject> getAllSyncObjects();

	public ISyncObject getSyncObject(String id);

	public void addSyncListener(ISyncListener listener);

	public void removeSyncListener(ISyncListener listener);

	public String getId();

}
