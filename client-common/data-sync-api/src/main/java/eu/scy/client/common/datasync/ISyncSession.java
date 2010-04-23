package eu.scy.client.common.datasync;

import java.util.List;

import eu.scy.common.datasync.ISyncObject;

public interface ISyncSession {

	/**
	 * adds an ISyncObject to the session
	 * 
	 * @param syncObject the SyncObject to be added
	 */
	public void addSyncObject(ISyncObject syncObject);

	/**
	 * changes an ISyncObject in a session.
	 * note, that the ISyncObject's ID has to match a previously created
	 * ISyncObject in this session to successfully update.
	 * only the properties that have changed in the ISyncObject need to be set.
	 * if the ISyncObject to be changed is currently not in the session
	 * (according to its ID), a new SyncObject will
	 * be created and a "syncObjectAdded()" will be triggered instead.
	 * note, that the ISyncObject's ID has to match only the properties that 
	 * 
	 * @param syncObject the SyncObject to be changed
	 * 
	 */
	public void changeSyncObject(ISyncObject syncObject);

	/**
	 * removes a SyncObject in a session. if the SyncObject to be removed is
	 * currently not in the session (according to its ID), nothing happens.
	 * 
	 * @param syncObject the SyncObject to be changed
	 */
	public void removeSyncObject(ISyncObject syncObject);

	/**
	 * returns all ISyncObjects in this session.
	 * 
	 * @return a List of all ISyncObjects that are currently in this session
	 */
	public List<ISyncObject> getAllSyncObjects();
	
	/**
	 * returns a single SyncObjects in this session. if no ISyncObject
	 * with the given id is currently in this session, null will be returned
	 * 
	 * @param id the id of ISyncObject that is to be returned
	 * 
	 * @return the ISyncObject that matches the given id
	 */	
	public ISyncObject getSyncObject(String id);

	/**
	 * add an ISyncListener to this session.
	 * 
	 * @param listener the ISyncListener that will be registered to this session
	 */
	public void addSyncListener(ISyncListener listener);

	/**
	 * removes an ISyncListener from this session.
	 * 
	 * @param listener the ISyncListener that will be removed from this session
	 */
	public void removeSyncListener(ISyncListener listener);

	/**
	 * return an unique string that identifies this session.
	 * needed to get an existing ISyncSession object from the IDataSyncService
	 * 
	 * @return a string that identifies this session
	 */
	public String getId();

}
