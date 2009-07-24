package eu.scy.awareness.controller;

import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.DefaultListModel;
import javax.swing.JTextArea;

import roolo.elo.api.IMetadataKey;

import eu.scy.awareness.AwarenessServiceException;
import eu.scy.awareness.IAwarenessService;
import eu.scy.awareness.IAwarenessUser;
import eu.scy.awareness.event.IAwarePresenceEvent;
import eu.scy.awareness.event.IAwarenessEvent;
import eu.scy.awareness.event.IAwarenessMessageListener;
import eu.scy.awareness.event.IAwarenessPresenceListener;
import eu.scy.awareness.impl.AwarenessServiceXMPPImpl;
import eu.scy.collaborationservice.CollaborationServiceException;
import eu.scy.collaborationservice.CollaborationServiceFactory;
import eu.scy.collaborationservice.ICollaborationService;
import eu.scy.collaborationservice.event.ICollaborationServiceEvent;
import eu.scy.collaborationservice.event.ICollaborationServiceListener;
import eu.scy.communications.message.IScyMessage;
import eu.scy.communications.message.impl.ScyMessage;
import eu.scy.toolbroker.ToolBrokerImpl;
import eu.scy.toolbrokerapi.ToolBrokerAPI;


public class ChatController {
    
    private DefaultListModel buddyList = new DefaultListModel();
    private String password;
    private String username;
    private IAwarenessService awarenessService;
    private ICollaborationService cs;
    private ToolBrokerImpl<IMetadataKey> tbi;

    
    public ChatController() {
       populateBuddyList();
    }

    public ChatController(final String username, final String password) {
        this.username = username;
        this.password = password;

         //awarenessService = new AwarenessServiceXMPPImpl(); 
        tbi = new ToolBrokerImpl<IMetadataKey>();
        awarenessService = tbi.getAwarenessService();
        awarenessService.init(tbi.getConnection("obama", "obama"));
           
         //just working with the collaboration service right now
         System.out.println("awareness starting");
    }

    public void populateBuddyList() {
        //get this from the awareness service
        if( awarenessService != null && awarenessService.isConnected() ) {
            List<IAwarenessUser> buddies = null;
			try {
				buddies = awarenessService.getBuddies();
			} catch (AwarenessServiceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        
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
        if (recipient != null) {
            IAwarenessUser user = (IAwarenessUser) recipient;
            try {
				awarenessService.sendMessage(user.getUsername(), message);
			} catch (AwarenessServiceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
    	awarenessService.addAwarenessMessageListener(new IAwarenessMessageListener() {
			
			@Override
			public void handleAwarenessMessageEvent(IAwarenessEvent awarenessEvent) {
				String oldText = chatArea.getText();
                
                chatArea.setText(oldText+awarenessEvent.getUser() +": " + awarenessEvent.getMessage() + "\n");
				
			}
		});

    	
    }
}
