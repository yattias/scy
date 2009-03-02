package eu.scy.collaborationservice;

import java.util.EventObject;

public interface ICollaborationListener  {
    
    public void handleCollaborationEvent(ICollaborationEvent e);

}
