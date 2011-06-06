package eu.scy.collaborationservice.adapter;

import eu.scy.communications.message.IScyMessage;
import eu.scy.communications.message.impl.ScyMessage;

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
    void actionUponWrite(IScyMessage scyMessage);
    
    /**
     * performs an action upon delete
     * 
     * @param scyMessage
     */
	void actionUponDelete(IScyMessage scyMessage);
	
	   /**
     * performs an action upon update
     * 
     * @param scyMessage
     */
    void actionUponUpdate(IScyMessage scyMessage);
	
	/**
	 * Does a create operation
	 * 
	 * @param scyMessage
	 * @return
	 */
    public String create(IScyMessage scyMessage);
    
    /**
     * Does a read operation
     * 
     * @param id
     * @return
     */
    public IScyMessage read(String id);
    
    /**
     * Does an update operation
     * 
     * @param sm
     * @param id
     * @return
     */
    public String update(IScyMessage sm, String id);
    
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
    public void sendCallBack(IScyMessage scyMessage);
}
