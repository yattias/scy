package eu.scy.communications.adapter;

import eu.scy.communications.message.ScyMessage;

/**
 * The ScyCommunicationAdapter allows a coordinating server
 * to use it to write and retrieve data
 * 
 * @author anthonyp
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
     * @param scyMessage
     */
    void actionUponWrite(ScyMessage scyMessage);
    
    /**
     * performs an action upon delete
     * 
     * @param scyMessage
     */
	void actionUponDelete(ScyMessage scyMessage);
	
	/**
	 * Does a create operation
	 * 
	 * @param scyMessage
	 * @return
	 */
    public String create(ScyMessage scyMessage);
    
    /**
     * Does a read operation
     * 
     * @param id
     * @return
     */
    public ScyMessage read(String id);
    
    /**
     * Does an update operation
     * 
     * @param sm
     * @param id
     * @return
     */
    public String update(ScyMessage sm, String id);
    
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
     * @param scyMessage
     */
    public void sendCallBack(ScyMessage scyMessage);
}
