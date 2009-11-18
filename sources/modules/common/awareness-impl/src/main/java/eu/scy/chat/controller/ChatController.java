package eu.scy.chat.controller;

import java.util.List;
import java.util.Vector;

import javax.swing.AbstractListModel;
import javax.swing.DefaultListModel;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;

import eu.scy.awareness.AwarenessServiceException;
import eu.scy.awareness.IAwarenessService;
import eu.scy.awareness.IAwarenessUser;
import eu.scy.awareness.event.IAwarenessEvent;
import eu.scy.awareness.event.IAwarenessMessageListener;


public class ChatController {
    
	private static final Logger logger = Logger.getLogger(ChatController.class.getName());
    private DefaultListModel buddyList = new DefaultListModel();
    private IAwarenessService awarenessService;

    
    public ChatController() {
       populateBuddyList();
    }

    public ChatController(IAwarenessService awarenessService) {
        this.awarenessService = awarenessService;
         //just working with the collaboration service right now
        logger.debug("ChatController: Awarness starting ... ");
    }

    public void populateBuddyList() {
        //get this from the awareness service
    	logger.debug("ChatController: populateBuddyList: ####################### begin ###########################");
    	logger.debug("ChatController: populateBuddyList: awarenessService: "+awarenessService);
    	logger.debug("ChatController: populateBuddyList: awarenessService.isConnected(): "+awarenessService.isConnected());
    	buddyList = new DefaultListModel();
        if( awarenessService != null && awarenessService.isConnected() ) {
            List<IAwarenessUser> buddies = null;
			try {
				buddies = awarenessService.getBuddies();
			} catch (AwarenessServiceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        
            if( buddies != null ) {
            	logger.debug("ChatController: populateBuddyList: num of buddies: "+buddies.size());
                for (IAwarenessUser b : buddies) {
                    buddyList.addElement(b);
                    logger.debug("ChatController: populateBuddyList: buddy name: "+b.getName());
                }
            }
        } else {
            buddyList.addElement("no buddies");
        }
        logger.debug("ChatController: populateBuddyList: ####################### end ###########################");
        
    }

    public  AbstractListModel getBuddyList() {
        return buddyList;
    }
    
    public Vector<IAwarenessUser> getBuddyListArray() {
    	Vector<IAwarenessUser> vec;
    	if( buddyList == null ) {
        	return null;        	
        }
        else {    
        	vec = new Vector<IAwarenessUser>();
        	for(int i=0; i<buddyList.getSize();i++) {
        		vec.addElement((IAwarenessUser) buddyList.elementAt(i));
        	}       	
        	return vec;  	
        }
    }
    
    
    public void sendMessage(Object recipient, final String message) {       
        //send a message to the awareness server
        if (recipient != null) {
            final IAwarenessUser user = (IAwarenessUser) recipient;
            //awarenessService.sendMessage(user.getUsername(), message);
			SwingUtilities.invokeLater(new Runnable() {
			    public void run() {
			    	try {
						awarenessService.sendMessage(user.getUsername(), message);
					} catch (AwarenessServiceException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			    }
			  });
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
}
