package eu.scy.awareness.controller;

import java.util.ArrayList;

import javax.swing.AbstractListModel;
import javax.swing.DefaultListModel;
import javax.swing.JTextArea;

import eu.scy.awareness.AwarenessService;
import eu.scy.awareness.api.IAwarenessEvent;
import eu.scy.awareness.api.IAwarenessListener;
import eu.scy.awareness.api.IAwarenessUser;
import eu.scy.collaborationservice.CollaborationService;
import eu.scy.collaborationservice.ICollaborationEvent;
import eu.scy.collaborationservice.ICollaborationListener;
import eu.scy.core.model.impl.ScyBaseObject;


public class ChatController {
    
    private DefaultListModel buddyList = new DefaultListModel();
    private String password;
    private String username;
    private AwarenessService awarenessService;
    private CollaborationService cs;
    
    public ChatController() {
       populateBuddyList();
    }

    public ChatController(final String username, final String password) {
        this.username = username;
        this.password = password;

         awarenessService = new AwarenessService(); 
           
         //just working with the collaboration service right now
         //System.out.println("awareness starting");
         //awarenessService.createAwarenessService(username, password);
         
         System.out.println("collaboration service starting");
         cs = new CollaborationService();
         cs.connect("biden", "biden", "colermo");
        
         System.out.println("collaboration starting");
    }

    public void populateBuddyList() {
        //get this from the awareness service
        if( awarenessService != null && awarenessService.isConnected() ) {
            ArrayList<IAwarenessUser> buddies = awarenessService.getBuddies(username);
        
            if( buddies != null ) {
                for (IAwarenessUser b : buddies) {
                    buddyList.addElement(b);
                }// for
            }
        } else {
            buddyList.addElement("no buddies");
        }
        
    }

    public  AbstractListModel getBuddyList() {
        return buddyList;
    }
    
    public void sendMessage(Object recipient, String message) {
       
        //send a message to the awareness server
//        if (recipient != null) {
//            IAwarenessUser user = (IAwarenessUser) recipient;
//            awarenessService.sendMessage(user.getUsername(), message);
//        }
        
        //send a message to the collaboration server
        System.out.println("sending message to CS");
        
        ScyBaseObject scy = new ScyBaseObject();
        scy.setDescription("this is a scy");
        scy.setId("6");
        scy.setName("im scy");
        cs.create(scy);
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
        
        cs.addCollaborationListener(new ICollaborationListener() {
            

            @Override
            public void handleCollaborationEvent(ICollaborationEvent e) {
                System.out.println("Collaboration Event called Chat controller" + e);
                String oldText = chatArea.getText();
                chatArea.setText(oldText+e.getParticipant() +": " + e.getMessage() + "\n");
            }
         });
    }
}
