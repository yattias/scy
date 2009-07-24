package eu.scy.chat.controller;

import java.util.Collection;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.DefaultListModel;
import javax.swing.JTextArea;

import eu.scy.awareness.AwarenessServiceException;
import eu.scy.awareness.IAwarenessService;
import eu.scy.awareness.IAwarenessUser;
import eu.scy.awareness.event.IAwarenessEvent;
import eu.scy.awareness.event.IAwarenessMessageListener;


public class ChatController {
    
    private DefaultListModel buddyList = new DefaultListModel();
    private IAwarenessService awarenessService;

    
    public ChatController() {
       populateBuddyList();
    }

    public ChatController(IAwarenessService awarenessService) {
        this.awarenessService = awarenessService;
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
        this.getBuddyListArray();
        return buddyList;
    }
    
    public String[] getBuddyListArray() {
        if( buddyList == null )
            return null;
        
        String[] a = new String[buddyList.size()];
        buddyList.copyInto(a);
        return a;
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
