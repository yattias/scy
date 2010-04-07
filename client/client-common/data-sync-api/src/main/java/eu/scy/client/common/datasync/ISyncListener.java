package eu.scy.client.common.datasync;

import eu.scy.common.datasync.ISyncObject;

public interface ISyncListener {

	/**
    * is called when a SyncObject has been added to the session
	*
	* @param syncObject the SyncObject that has been added
    */
   public void syncObjectAdded(ISyncObject syncObject);
   
   	/**
    * is called when a SyncObject has been changed
	*
	* @param syncObject the SyncObject that has been changed, containing only the changed
    */
   public void syncObjectChanged(ISyncObject syncObject);
   
   	/**
    * is called when a SyncObject has been removed from the session
	*
	* @param syncObject the SyncObject that has been removed. the syncObject may only contain an identifier.
    */
   public void syncObjectRemoved(ISyncObject syncObject);

}
