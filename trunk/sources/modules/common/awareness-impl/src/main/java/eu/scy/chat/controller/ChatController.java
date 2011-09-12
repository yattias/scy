package eu.scy.chat.controller;

import java.util.Vector;

import javax.swing.AbstractListModel;
import javax.swing.DefaultListModel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import eu.scy.awareness.AwarenessServiceException;
import eu.scy.awareness.AwarenessUser;
import eu.scy.awareness.IAwarenessService;
import eu.scy.awareness.IAwarenessUser;

/**
 * interface for MUC and OOO one on one chats
 * 
 * @author anthonjp
 *
 */
public interface ChatController {

	/**
	 * Get the current user that is logged into the xmpp connection
	 * 
	 * @return
	 */
	public abstract String getCurrentUser();

	/**
	 * Send a Message with an ELOUri, this is for MUC
	 * 
	 * @param ELOUri
	 * @param message
	 */
	public abstract void sendMessage(final String ELOUri, final String message);

        /**
	 * Send a Message
	 *
	 * @param message
	 */
	public abstract void sendMessage(final String message);

        /**
         * Sends an invitation to a user to join the MUC
         *
         * @param user
         */
        public void sendInvitation(String user);

	/**
	 * for OOO sends a message to the recipient
	 * 
	 * @param recipient
	 * @param message
	 */
	public void sendMessage(IAwarenessUser recipient, final String message);
	
	/**
	 * add a buddy to the list
	 * 
	 * @param user
	 */
	public abstract void addBuddy(final AwarenessUser user);

	/**
	 * remove a buddy from the llist
	 * 
	 * @param user
	 */
	public abstract void removeBuddy(final AwarenessUser user);

	/**
	 * register chat area to recieve updates
	 * 
	 * @param chatArea
	 */
	public abstract void registerChatArea(final JTextArea chatArea);

        /**
	 * register chat to recieve updates
	 *
	 * @param chatArea
	 */
	public abstract void registerChat(IChat chat);

	/**
	 * connect to a room specificed in the constructor
	 * 
	 */
	public abstract void connectToRoom();

	/**
	 * sets the awareness service
	 * 
	 * @param awarenessService
	 */
	public abstract void setAwarenessService(IAwarenessService awarenessService);

	/**
	 * gets teh awareness service
	 * 
	 * @return
	 */
	public abstract IAwarenessService getAwarenessService();

	/**
	 * sets elo uri
	 * 
	 * @param eLOUri
	 */
	public abstract void setELOUri(String eLOUri);

	/**
	 * get the elo uri
	 * 
	 * @return
	 */
	public abstract String getELOUri();

	/**
	 * sets the list model
	 * 
	 * @param buddyListModel
	 */
	public abstract void setBuddyListModel(DefaultListModel buddyListModel);

	/**
	 * Gets the list model
	 * 
	 * @return
	 */
	public abstract DefaultListModel getBuddyListModel();

	/**
	 * Registers textfield
	 * 
	 * @param sendMessageTextField
	 */
	public abstract void registerTextField(JTextField sendMessageTextField);

	

}