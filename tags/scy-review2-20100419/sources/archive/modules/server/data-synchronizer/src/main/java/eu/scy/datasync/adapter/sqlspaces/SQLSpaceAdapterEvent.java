package eu.scy.datasync.adapter.sqlspaces;

import java.util.EventObject;

import eu.scy.communications.message.ISyncMessage;


/**
 * Represents an event in the scycommunication layer
 * 
 * @author anthonyp
 */
public class SQLSpaceAdapterEvent extends EventObject {

    private ISyncMessage syncMessage;
    private String action;
   
    /**
     * Contructor
     * 
     * @param source
     * @param syncMessage
     */
    public SQLSpaceAdapterEvent(Object source, ISyncMessage syncMessage, String action){
        super(source);
        this.syncMessage = syncMessage;
        this.setAction(action);
    }

    
    /**
     * Get the sync message
     * 
     * @return syncMessage
     */
    public ISyncMessage getScyMessage() {
        return syncMessage;
    }


    /**
     * Type of Action
     * 
     * @param action
     */
    public void setAction(String action) {
        this.action = action;
    }


    /**
     * get the action type
     * 
     * @return
     */
    public String getAction() {
        return action;
    }
    
}
