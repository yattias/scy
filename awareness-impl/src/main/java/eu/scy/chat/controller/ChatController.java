package eu.scy.chat.controller;

import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.AbstractListModel;
import javax.swing.DefaultListModel;
import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;

import eu.scy.awareness.AwarenessServiceException;
import eu.scy.awareness.IAwarenessService;
import eu.scy.awareness.IAwarenessUser;

public class ChatController {

	private static final Logger logger = Logger.getLogger(ChatController.class
			.getName());
	private DefaultListModel buddyList = new DefaultListModel();
	private IAwarenessService awarenessService;

	public ChatController() {
		populateBuddyList();
	}

	public ChatController(IAwarenessService awarenessService) {
		this.awarenessService = awarenessService;
		// just working with the collaboration service right now
		logger.debug("ChatController: Awarness starting ... ");
	}

	public AbstractListModel populateBuddyList() {
		// get this from the awareness service
		logger.debug("ChatController: populateBuddyList: ####################### begin ###########################");
		logger.debug("ChatController: populateBuddyList: awarenessService: "+ awarenessService);
		logger.debug("ChatController: populateBuddyList: awarenessService.isConnected(): "+ awarenessService.isConnected());
		buddyList = new DefaultListModel();
		
		List<IAwarenessUser> buddies = null;
		if (awarenessService != null && awarenessService.isConnected()) {
			try {
				buddies = awarenessService.getBuddies();
			} catch (AwarenessServiceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (buddies != null) {
				logger
						.debug("ChatController: populateBuddyList: num of buddies: "
								+ buddies.size());
				for (IAwarenessUser b : buddies) {
					b.setUsername(correctName(b.getUsername()));
					buddyList.addElement(b);
					logger
							.debug("ChatController: populateBuddyList: buddy name: "
									+ b.getUsername());
				}
			}
		} else {
			//buddyList.addElement("no buddies");
			logger.debug("ChatController: populateBuddyList: ####################### No buddies ###########################");
		}
		logger.debug("ChatController: populateBuddyList: ####################### end ###########################");
		return buddyList;
	}

	private String correctName(String username) {
		StringTokenizer st = new StringTokenizer(username, "/");
		return st.nextToken();
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

	public void sendMessage(Object recipient, final String message) {
		// send a message to the awareness server
		if (recipient != null) {
			final IAwarenessUser user = (IAwarenessUser) recipient;
			// awarenessService.sendMessage(user.getUsername(), message);
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					try {
						awarenessService.sendMessage(user.getUsername(),
								message);
					} catch (AwarenessServiceException e) {
						logger.error("ChatController: sendMessage: AwarenessServiceException: "+e);
					}
				}
			});
		}
	}

	public void receiveMessage() {

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
}
