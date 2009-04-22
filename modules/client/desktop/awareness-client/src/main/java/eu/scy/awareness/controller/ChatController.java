package eu.scy.awareness.controller;

import java.util.ArrayList;

import javax.swing.AbstractListModel;
import javax.swing.DefaultListModel;
import javax.swing.JTextArea;

import eu.scy.awareness.IAwarenessUser;
import eu.scy.awareness.event.IAwarenessEvent;
import eu.scy.awareness.event.IAwarenessPresenceListener;
import eu.scy.awareness.impl.AwarenessServiceXMPPImpl;
import eu.scy.collaborationservice.CollaborationServiceException;
import eu.scy.collaborationservice.CollaborationServiceFactory;
import eu.scy.collaborationservice.ICollaborationService;
import eu.scy.collaborationservice.event.ICollaborationServiceEvent;
import eu.scy.collaborationservice.event.ICollaborationServiceListener;
import eu.scy.communications.message.IScyMessage;
import eu.scy.communications.message.impl.ScyMessage;


public class ChatController {
    
    private DefaultListModel buddyList = new DefaultListModel();
    private String password;
    private String username;
    private AwarenessServiceXMPPImpl awarenessService;
    private ICollaborationService cs;
    
    public ChatController() {
       populateBuddyList();
    }

    public ChatController(final String username, final String password) {
        this.username = username;
        this.password = password;

         awarenessService = new AwarenessServiceXMPPImpl(); 
           
         //just working with the collaboration service right now
         //System.out.println("awareness starting");
         //awarenessService.createAwarenessService(username, password);
         
         System.out.println("collaboration service starting");
         
         try {
             cs = CollaborationServiceFactory.getCollaborationService(CollaborationServiceFactory.XMPP_STYLE);
             cs.connect("biden", "biden", "colermo");
        } catch (CollaborationServiceException e) {
            e.printStackTrace();
        }
         
        
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
        
        IScyMessage scy = new ScyMessage();
        scy.setDescription("this is a scy");
        scy.setId("6");
        scy.setName("im scy");
        try {
            cs.create(scy);
        } catch (CollaborationServiceException e) {
            e.printStackTrace();
        }
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
//        awarenessService.addAwarenessListener(new IAwarenessPresenceListener(){
//
//
//            @Override
//            public void handleAwarenessEvent(IAwarenessEvent e) {
//                String oldText = chatArea.getText();
//                
//                chatArea.setText(oldText+e.getParticipant() +": " + e.getMessage() + "\n");
//                
//            }});
//        
        cs.addCollaborationListener(new ICollaborationServiceListener() {
            

            @Override
            public void handleCollaborationServiceEvent(ICollaborationServiceEvent e) {
                System.out.println("Collaboration Event called Chat controller" + e);
                String oldText = chatArea.getText();
//                chatArea.setText(oldText+e.getParticipant() +": " + e.getMessage() + "\n");
            }
         });
    }
}
