package eu.scy.awareness.event;

/**
 * Listener interface for MultiUserChat invitation listening
 * 
 * @author giemza
 */
public interface IAwarenessInvitationListener {
    
    /**
     * This method will be called to handle the invitation
     * 
     * @param inviter the inviter to the multi user chat
     * @param room the room to join
     */
    public void handleInvitationEvent(String inviter, String room);

}
