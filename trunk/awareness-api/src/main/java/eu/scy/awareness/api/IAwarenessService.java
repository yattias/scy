package eu.scy.awareness.api;

import java.util.ArrayList;

public interface IAwarenessService {
    //to be replace with scy user
    public ArrayList<IAwarenessUser> getBuddies(String username);
    public void sendMessage(String recipient, String message);
    public void setStatus(String username, String status);

}
