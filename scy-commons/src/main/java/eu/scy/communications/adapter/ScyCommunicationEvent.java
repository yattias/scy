package eu.scy.communications.adapter;

import java.util.EventObject;

import eu.scy.communications.message.ScyMessage;


public class ScyCommunicationEvent extends EventObject {

    private ScyMessage scyMessage;
    
    public ScyCommunicationEvent(Object source, ScyMessage scyMessage){
        super(source);
        this.scyMessage = scyMessage;
    }

    
    public ScyMessage getScyMessage() {
        return scyMessage;
    }
    
}
