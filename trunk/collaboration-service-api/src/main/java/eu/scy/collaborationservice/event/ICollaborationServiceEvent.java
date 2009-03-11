package eu.scy.collaborationservice.event;

import eu.scy.communications.message.ScyMessage;

/**
 * Event object for collaboration service listeners 
 * 
 * @author anthonyp
 */
public interface ICollaborationServiceEvent {
    
    //TODO: refactor 
    public abstract ScyMessage getScyMessage();
    
}