package eu.scy.collaborationservice;

public interface ICollaborationEvent {
    
    public abstract String getParticipant();
    
    public abstract String getMessage();
    
}