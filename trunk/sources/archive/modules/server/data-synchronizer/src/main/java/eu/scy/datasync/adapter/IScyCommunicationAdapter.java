package eu.scy.datasync.adapter;

import eu.scy.communications.message.ISyncMessage;

/**
 * The ScyCommunicationAdapter allows a coordinating server
 * to use it to write and retrieve data
 * 
 * @author thomasd
 *
 */
public interface IScyCommunicationAdapter {

    /**
     * Adds listeners to the adapter
     * 
     * @param listener
     */
    public void addScyCommunicationListener(IScyCommunicationListener listener);

    /**
     * performs an action upon write
     * 
     * @param syncMessage
     */
    void actionUponWrite(ISyncMessage syncMessage);
    
    /**
     * performs an action upon delete
     * 
     * @param syncMessage
     */
	void actionUponDelete(ISyncMessage syncMessage);
	
	   /**
     * performs an action upon update
     * 
     * @param syncMessage
     */
    void actionUponUpdate(ISyncMessage syncMessage);
	
	/**
	 * Does a create operation
	 * 
	 * @param syncMessage
	 * @return
	 */
    public String create(ISyncMessage syncMessage);
    
    /**
     * Does a read operation
     * 
     * @param id
     * @return
     */
    public ISyncMessage read(String id);
    
    /**
     * Does an update operation
     * 
     * @param syncMessage
     * @param id
     * @return
     */
    public String update(ISyncMessage syncMessage);
    
    /**
     * Does a delete operation
     * 
     * @param id
     * @return
     */
    public String delete(String id);
    
    /**
     * Sends a call back to a receiver
     * 
     * @param syncMessage
     */
    public void sendCallBack(ISyncMessage syncMessage);
}
