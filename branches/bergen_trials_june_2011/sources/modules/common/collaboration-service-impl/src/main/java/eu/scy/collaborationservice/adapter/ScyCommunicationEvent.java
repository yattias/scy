package eu.scy.collaborationservice.adapter;

import java.util.EventObject;

import eu.scy.communications.message.IScyMessage;

/**
 * Represents an event in the scycommunication layer
 * 
 * @author anthonyp
 */
public class ScyCommunicationEvent extends EventObject {

    private IScyMessage scyMessage;
   
    /**
     * Contructor
     * 
     * @param source
     * @param scyMessage
     */
    public ScyCommunicationEvent(Object source, IScyMessage scyMessage){
        super(source);
        this.scyMessage = scyMessage;
    }

    
    /**
     * Get the scy message
     * 
     * @return
     */
    public IScyMessage getScyMessage() {
        return scyMessage;
    }
    
}
