package eu.scy.awareness;

import java.util.List;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smackx.muc.MultiUserChat;

import eu.scy.awareness.event.IAwarenessInvitationListener;
import eu.scy.awareness.event.IAwarenessMessageListener;
import eu.scy.awareness.event.IAwarenessPresenceListener;
import eu.scy.awareness.event.IAwarenessRosterListener;
import eu.scy.awareness.tool.IChatPresenceToolListener;

/**
 * Awareness Service Interface
 * 
 * @author anthonyp
 */
public interface IAwarenessService {
    //to be replace with scy user
    
	public Connection getConnection();
	
    /**
     * Get all the buddies
     * 
     * @return all buddies
     * 
     */
    public List<IAwarenessUser> getBuddies() throws AwarenessServiceException;
    
    /**
     * 
     * 
     * @param recipient
     * @param message
     * @throws AwarenessServiceException
     */
    public void sendMessage(IAwarenessUser recipient, String message) throws AwarenessServiceException;
    
    
    /**
	 * sends a message to a MUC chat room
	 * 
	 * @param ELOUri
	 * @param message
	 */
	public void sendMUCMessage(String ELOUri, String message) throws AwarenessServiceException;
	
    
    /**
     * sets the presence of user either online or offline
     * 
     * @param username
     * @param presence
     * @throws AwarenessServiceException
     */
    public void setPresence(String presence) throws AwarenessServiceException;
    
    /**
     * sets the status of user
     * 
     * @param status
     * @throws AwarenessServiceException
     */
    public void setStatus(String status) throws AwarenessServiceException;

    /**
     * Sets the presence of the logged in user to available or unavailable.
     *
     * @param available if true the user will be available
     */
    public void setUserPresence(boolean available);
    
    /**
     * adds a presence listener
     * 
     * @param awarenessPresenceListener
     */
    public void addAwarenessPresenceListener(IAwarenessPresenceListener awarenessPresenceListener);
    
    /**
     * removes awareness presence listener
     * 
     * @param awarenessPresenceListener
     */
    public void removeAwarenessPresenceListener(IAwarenessPresenceListener awarenessPresenceListener);
    
    /**
     * adds a buddy list listener
     * 
     * @param awarenessListListener
     */
    public void addAwarenessRosterListener(IAwarenessRosterListener awarenessListListener);
    
    
    /**
     * adds a message listener
     * 
     * @param awarenessListListener
     */
    public void addAwarenessMessageListener(IAwarenessMessageListener awarenessMessageListener);
    
    /**
     * adds an invitation listener
     * 
     * @param invitationListener the invitation listener to add
     */
    public void addInvitationListener(IAwarenessInvitationListener invitationListener);
    
    /**
     * removes an invitation listener
     * 
     * @param invitationListener the invitation listener to remove
     */
    public void removeInvitationListener(IAwarenessInvitationListener invitationListener);
    
    /**
     * Add buddy
     * 
     * @param buddy
     * @throws AwarenessServiceException
     */
    public void addBuddy(String buddy) throws AwarenessServiceException;
    
    /**
     * remove buddy
     * 
     * @param buddy
     * @throws AwarenessServiceException
     */
    public void removeBuddy(String buddy) throws AwarenessServiceException;

    /**
     * initialize the service with connection.
     * 
     * @param connection
     */
    public void init(Connection connection);
    
    /**
     * is the service connected
     * 
     * @return
     */
    public boolean isConnected();
    
    /**
     * disconnect the xmpp session
     * 
     */
    public void disconnect();
    

    /**
     * listens for events coming from ChatTool
     * @param listener
     */
    public void addChatToolListener(IChatPresenceToolListener listener);
    
    /**
     * listens for presence tool events coming from ChatPresenceTool
     * @param listener
     */
    public void addPresenceToolListener(IChatPresenceToolListener listener);

    /**
     * updates all teh presence tool listeners
     * 
     * @param users
     */
	public void updatePresenceTool(List<IAwarenessUser> users);

	/**
	 * updates chat tool listeners
	 * 
	 * @param users
	 */
	public void updateChatTool(List<IAwarenessUser> users);
	
	/**
	 * Creates a Multi User Chat
	 * 
	 * @param ELOUri
	 * @param buddies
	 */
	public void joinMUCRoom(String ELOUri) throws AwarenessServiceException;;
	
	/**
	 * get the chat buddies
	 * 
	 * @param ELOUri
	 * @return
	 */
	public List<IAwarenessUser> getMUCBuddies(String ELOUri) throws AwarenessServiceException;
   
	/**
	 * add the buddy to MUC
	 * 
	 * @param buddy
	 */
	public void addBuddyToMUC(IAwarenessUser buddy, String ELOUri) throws AwarenessServiceException;
	
	/**
	 * removes a buddy from the MUC
	 * 
	 * @param buddy
	 */
	public void removeBuddyFromMUC(IAwarenessUser buddy, String ELOUri) throws AwarenessServiceException;

	/**
	 * checks to see if chat room is joined
	 * 
	 * @param ELOUri
	 * @param user
	 * @return
	 */
	public boolean hasJoinedRoom(String ELOUri, String user) throws AwarenessServiceException;
	
	/**
	 * checks to see if the room exists
	 * 
	 * @param ELOUri
	 * @return
	 */
	public boolean doesRoomExist(String ELOUri) throws AwarenessServiceException;

	/**
	 * destroy destroy
	 * 
	 * @param ELOUri
	 */
	public void destoryMUCRoom(String ELOUri) throws AwarenessServiceException;

	/**
	 * sets the address of the confernce extension for MUC
	 * 
	 * @param CONFERENCE_EXT
	 */
	public void setMUCConferenceExtension(String CONFERENCE_EXT);

	/**
	 * get the address conference extension, adds an "@" at the front of it.for MUC
	 * 
	 * @return
	 */
	public String getMUCConferenceExtension();

	
	/**
	 * gets a MUC for an ELO
	 * 
	 * @param ELOUri
	 * @return
	 */
	public MultiUserChat getMultiUserChat(String ELOUri) throws AwarenessServiceException;

        /**
         * sends an invitation to a user for the MUC with the room
         *
         * @param room
         * @param user
         */
        public void inviteUserToChat(String room, String user);

}
