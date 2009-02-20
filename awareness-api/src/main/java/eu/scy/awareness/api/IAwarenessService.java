package eu.scy.awareness.api;

import java.util.Collection;

public interface IAwarenessService {
    
    public Collection getBuddies(String username);
    public void sendMessage(String recipient, String message);
    public void setStatus(String username, String status);

}
