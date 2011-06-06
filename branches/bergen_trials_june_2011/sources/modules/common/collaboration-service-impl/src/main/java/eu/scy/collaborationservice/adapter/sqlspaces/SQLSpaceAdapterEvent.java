package eu.scy.collaborationservice.adapter.sqlspaces;

import java.util.EventObject;

import eu.scy.communications.message.IScyMessage;

/**
 * Represents an event in the scycommunication layer
 * 
 * @author anthonyp
 */
public class SQLSpaceAdapterEvent extends EventObject {

    private IScyMessage scyMessage;
    private String action;
   
    /**
     * Contructor
     * 
     * @param source
     * @param scyMessage
     */
    public SQLSpaceAdapterEvent(Object source, IScyMessage scyMessage,String action){
        super(source);
        this.scyMessage = scyMessage;
        this.setAction(action);
    }

    
    /**
     * Get the scy message
     * 
     * @return
     */
    public IScyMessage getScyMessage() {
        return scyMessage;
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
