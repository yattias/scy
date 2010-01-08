package eu.scy.chat.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.AbstractListModel;
import javax.swing.DefaultListModel;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;
import org.jivesoftware.smack.util.StringUtils;

import eu.scy.awareness.AwarenessServiceException;
import eu.scy.awareness.IAwarenessService;
import eu.scy.awareness.IAwarenessUser;
import eu.scy.awareness.event.IAwarenessEvent;
import eu.scy.awareness.event.IAwarenessMessageListener;

public class ChatController {

	private static final Logger logger = Logger.getLogger(ChatController.class
			.getName());
	private DefaultListModel buddyList = new DefaultListModel();
	private IAwarenessService awarenessService;
	private String ELOUri;
	private boolean isTempMUCRoom;

	public ChatController(IAwarenessService awarenessService, String ELOUri) {
		logger.debug("ChatController: starting ... ");
		logger.debug("ChatController: populateBuddyList: awarenessService.isConnected(): "+ awarenessService.isConnected());
		this.ELOUri = ELOUri;
		this.awarenessService = awarenessService;
	}
	
	public String getCurrentUser() {
		String user = getAwarenessService().getConnection().getUser();
		if( user != null)
			return StringUtils.parseName(user);
		
		return "NO ONE";
	}
	

	public AbstractListModel populateBuddyList() {
		// get this from the awareness service
		
		buddyList = new DefaultListModel();
		
		List<IAwarenessUser> buddies = null;
		if (getAwarenessService() != null && getAwarenessService().isConnected()) {
			try {
				buddies = getAwarenessService().getBuddies();
			} catch (AwarenessServiceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (buddies != null) {
				logger
						.debug("ChatController: populateBuddyList: num of buddies: "
								+ buddies.size());
				for (IAwarenessUser b : buddies) {
					b.setNickName(b.getNickName());
					buddyList.addElement(b);
					logger
							.debug("ChatController: populateBuddyList: buddy name: "
									+ b.getNickName());
				}
			}
		} else {
			//buddyList.addElement("no buddies");
			logger.debug("ChatController: populateBuddyList: ####################### No buddies ###########################");
		}
		logger.debug("ChatController: populateBuddyList: ####################### end ###########################");
		return buddyList;
	}

	

	public AbstractListModel getBuddyList() {
		return buddyList;
	}

	public Vector<IAwarenessUser> getBuddyListArray() {
		Vector<IAwarenessUser> vec;
		if (buddyList == null) {
			return null;
		} else {
			vec = new Vector<IAwarenessUser>();
			for (int i = 0; i < buddyList.getSize(); i++) {
				vec.addElement((IAwarenessUser) buddyList.elementAt(i));
			}
			return vec;
		}
	}
	
	public void sendMUCMessage(final String ELOUri, final String message){
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					awarenessService.sendMUCMessage(ELOUri, message);
				} catch (AwarenessServiceException e) {
					logger.error("ChatController: sendMessageMUC: AwarenessServiceException for ELOUri:" + ELOUri + " " + e);
				}
			}
		});
		
	}

	public void sendMessage(Object recipient, final String message) {
		// send a message to the awareness server
		if (recipient != null) {
			final IAwarenessUser user = (IAwarenessUser) recipient;
			// awarenessService.sendMessage(user.getUsername(), message);
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					try {
						awarenessService.sendMessage(user.getJid(),
								message);
					} catch (AwarenessServiceException e) {
						logger.error("ChatController: sendMessage: AwarenessServiceException: "+e);
					}
				}
			});
		}
	}

	public void addBuddy(final String buddy) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				buddyList.addElement(buddy);				
			}
		});
	}

	public void removeBuddy(final String buddy) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				buddyList.removeElement(buddy);
			}
		});
	}

	public void registerChatArea(final JTextArea chatArea) {
		
		awarenessService.addAwarenessMessageListener(new IAwarenessMessageListener() {
			@Override
			public void handleAwarenessMessageEvent(final IAwarenessEvent awarenessEvent) {
			
				System.out.println("calling message event");
				
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							
							String oldText = chatArea.getText();
							List<IAwarenessUser> users = new ArrayList<IAwarenessUser>();
							
							if(awarenessEvent.getMessage() != null) {
								chatArea.setText(oldText + awarenessEvent.getUser().getNickName() + ": " + awarenessEvent.getMessage() + "\n");	
								logger.debug("message sent from: " + awarenessEvent.getUser().getNickName() + " message: " + awarenessEvent.getMessage());
							}	
							
							users.add(awarenessEvent.getUser());
							awarenessService.updatePresenceTool(users);
						}
					});
			
			}
		});
		
		
	
		
	}
	
	public void connectToRoom() {
		logger.debug("ChatController: Joining room with ELOUri: "+ getELOUri());
		this.getAwarenessService().setMUCConferenceExtension("conference.scy.intermedia.uio.no");
		this.getAwarenessService().joinMUCRoom(this.ELOUri);
	}

	public void setAwarenessService(IAwarenessService awarenessService) {
		this.awarenessService = awarenessService;
	}

	public IAwarenessService getAwarenessService() {
		return awarenessService;
	}

	public void setELOUri(String eLOUri) {
		ELOUri = eLOUri;
	}

	public String getELOUri() {
		return ELOUri;
	}
}
