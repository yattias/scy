package eu.scy.communications.adapter;

import java.util.EventObject;

import eu.scy.communications.message.ScyMessage;

/**
 * Represents an event in the scycommunication layer
 * 
 * @author anthonyp
 */
public class ScyCommunicationEvent extends EventObject {

    private ScyMessage scyMessage;
   
    /**
     * Contructor
     * 
     * @param source
     * @param scyMessage
     */
    public ScyCommunicationEvent(Object source, ScyMessage scyMessage){
        super(source);
        this.scyMessage = scyMessage;
    }

    
    /**
     * Get the scy message
     * 
     * @return
     */
    public ScyMessage getScyMessage() {
        return scyMessage;
    }
    
}
