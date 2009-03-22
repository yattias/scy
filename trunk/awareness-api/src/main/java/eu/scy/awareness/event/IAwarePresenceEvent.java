package eu.scy.awareness.event;

import eu.scy.awareness.AwarenessServiceException;


public interface IAwarePresenceEvent extends IAwarenessEvent {

    public String ONLINE = "ONLINE";
    public String OFFLINE = "OFFLINE";
    public String IDLE = "IDLE";
    
    public String getPresence();
    
    public String getStatus();
    
    
}
