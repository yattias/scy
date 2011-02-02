package eu.scy.collaborationservice.event;

import eu.scy.communications.message.IScyMessage;

/**
 * Event object for collaboration service listeners 
 * 
 * @author anthonyp
 */
public interface ICollaborationServiceEvent {
    
    //TODO: refactor 
    public abstract IScyMessage getScyMessage();
    
}