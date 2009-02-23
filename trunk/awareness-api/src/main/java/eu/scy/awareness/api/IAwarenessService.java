package eu.scy.awareness.api;

import java.util.ArrayList;

public interface IAwarenessService {
    
    public ArrayList<String> getBuddies(String username);
    public void sendMessage(String recipient, String message);
    public void setStatus(String username, String status);

}
