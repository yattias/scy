package eu.scy.awareness.controller;

import javax.swing.AbstractListModel;
import javax.swing.DefaultListModel;


public class ChatController {
    
    DefaultListModel buddyList = new DefaultListModel();
    
    
    public ChatController() {
        
       populateBuddyList();
        
    }

    protected void populateBuddyList() {
        //get this from the awareness service
        
        buddyList.addElement("obama");
        buddyList.addElement("biden");
        buddyList.addElement("bill"); 
        
    }

    public  AbstractListModel getBuddyList() {
        return buddyList;
    }
    
    public void sendMessage(String message) {
        
    }
    
    public void receiveMessage() {
        
    }
    
    public void addBuddy(String buddy){
        buddyList.addElement(buddy);
    }
    
    public void removeBuddy(String buddy){
        buddyList.removeElement(buddy);
    }
}
