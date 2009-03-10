package eu.scy.collaborationservice;

/**
 * Event object for collaboration service listeners 
 * 
 * @author anthonyp
 */
public interface ICollaborationServiceEvent {
    
    //TODO: refactor
    public abstract String getParticipant();
    
    //TODO: refactor 
    public abstract String getMessage();
    
}