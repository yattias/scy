package eu.scy.awareness.controller;

import java.util.ArrayList;
import java.util.EventObject;

import javax.swing.AbstractListModel;
import javax.swing.DefaultListModel;
import javax.swing.JTextArea;

import eu.scy.awareness.AwarenessEvent;
import eu.scy.awareness.AwarenessService;
import eu.scy.awareness.api.IAwarenessEvent;
import eu.scy.awareness.api.IAwarenessListener;
import eu.scy.awareness.api.IAwarenessUser;


public class ChatController {
    
    private DefaultListModel buddyList = new DefaultListModel();
    private String password;
    private String username;
    private AwarenessService awarenessService;
    
    
    
    public ChatController() {
       populateBuddyList();
        
    }

    public ChatController(final String username, final String password) {
        this.username = username;
        this.password = password;

         awarenessService = AwarenessService.createAwarenessService(username, password);
         System.out.println("awareness starting");
        
    }

    public void populateBuddyList() {
        //get this from the awareness service
        ArrayList<IAwarenessUser> buddies = awarenessService.getBuddies(username);
        
        for (IAwarenessUser b : buddies) {
            buddyList.addElement(b);
        }// for
        
    }

    public  AbstractListModel getBuddyList() {
        return buddyList;
    }
    
    public void sendMessage(Object recipient, String message) {
        
        IAwarenessUser user = (IAwarenessUser) recipient;
        
        awarenessService.sendMessage(user.getUsername(), message);
    }
    
    public void receiveMessage() {
        
    }
    
    public void addBuddy(String buddy){
        buddyList.addElement(buddy);
    }
    
    public void removeBuddy(String buddy){
        buddyList.removeElement(buddy);
    }
    
    public void registerChatArea(final JTextArea chatArea) {
        awarenessService.addAwarenessListener(new IAwarenessListener(){


            @Override
            public void handleAwarenessEvent(IAwarenessEvent e) {
                String oldText = chatArea.getText();
                
                chatArea.setText(oldText+e.getParticipant() +": " + e.getMessage() + "\n");
                
            }});
    }
}
