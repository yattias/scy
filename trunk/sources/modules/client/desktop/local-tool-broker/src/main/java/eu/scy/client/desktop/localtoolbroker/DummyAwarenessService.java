package eu.scy.client.desktop.localtoolbroker;

import eu.scy.awareness.AwarenessServiceException;
import eu.scy.awareness.IAwarenessService;
import eu.scy.awareness.IAwarenessUser;
import eu.scy.awareness.event.IAwarenessInvitationListener;
import eu.scy.awareness.event.IAwarenessMessageListener;
import eu.scy.awareness.event.IAwarenessPresenceListener;
import eu.scy.awareness.event.IAwarenessRosterListener;
import eu.scy.awareness.tool.IChatPresenceToolListener;
import java.util.ArrayList;
import java.util.List;
import org.jivesoftware.smack.Connection;
import org.jivesoftware.smackx.muc.MultiUserChat;

/**
 *
 * @author sikken
 */
public class DummyAwarenessService implements IAwarenessService {

    private List<IAwarenessUser> buddies;

    public DummyAwarenessService() {
        super();
        buddies = new ArrayList<IAwarenessUser>();
        buddies.add(new MockAwarenessUser());
    }

    @Override
    public void addAwarenessMessageListener(IAwarenessMessageListener awarenessMessageListener) {
    }

    @Override
    public void addAwarenessPresenceListener(IAwarenessPresenceListener awarenessPresenceListener) {
    }

    @Override
    public void addAwarenessRosterListener(IAwarenessRosterListener awarenessListListener) {
    }

    @Override
    public void addBuddy(String buddy) throws AwarenessServiceException {
    }

    @Override
    public void addChatToolListener(IChatPresenceToolListener listener) {
    }

    @Override
    public void addPresenceToolListener(IChatPresenceToolListener listener) {
    }

    @Override
    public void disconnect() {
    }

    @Override
    public List<IAwarenessUser> getBuddies() throws AwarenessServiceException {
        System.out.println("++++++++++ getBuddies() on DummyAwarenessService called! ++++++++++++");
        return buddies;
    }

    @Override
    public void init(Connection connection) {
    }

    @Override
    public boolean isConnected() {
        return false;
    }

    @Override
    public void removeAwarenessPresenceListener(IAwarenessPresenceListener awarenessPresenceListener) {
    }

    @Override
    public void removeBuddy(String buddy) throws AwarenessServiceException {
    }

//   @Override
//   public void sendMessage(String recipient, String message) throws AwarenessServiceException
//   {
//   }
    @Override
    public void setPresence(String presence) throws AwarenessServiceException {
    }

    @Override
    public void setStatus(String status) throws AwarenessServiceException {
    }

    @Override
    public void updateChatTool(List<IAwarenessUser> users) {
    }

    @Override
    public void updatePresenceTool(List<IAwarenessUser> users) {
    }

    @Override
    public Connection getConnection() {
        return null;
    }

    @Override
    public void sendMUCMessage(String ELOUri, String message) throws AwarenessServiceException {
    }

    @Override
    public void joinMUCRoom(String ELOUri) {
    }

//   @Override
//   public List<IAwarenessUser> getChatBuddies(String ELOUri)
//   {
//      return new ArrayList<IAwarenessUser>();
//   }
    @Override
    public void addBuddyToMUC(IAwarenessUser buddy, String ELOUri) {
    }

    @Override
    public void removeBuddyFromMUC(IAwarenessUser buddy, String ELOUri) {
    }

    @Override
    public boolean hasJoinedRoom(String ELOUri, String user) {
        return false;
    }

    @Override
    public boolean doesRoomExist(String ELOUri) {
        return true;
    }

    @Override
    public void destoryMUCRoom(String ELOUri) {
    }

    @Override
    public void setMUCConferenceExtension(String CONFERENCE_EXT) {
    }

    @Override
    public String getMUCConferenceExtension() {
        return "ext";
    }

    @Override
    public MultiUserChat getMultiUserChat(String ELOUri) {
        return null;
    }

    @Override
    public void sendMessage(IAwarenessUser recipient, String message) throws AwarenessServiceException {
    }

    @Override
    public List<IAwarenessUser> getMUCBuddies(String ELOUri) throws AwarenessServiceException {
        return new ArrayList<IAwarenessUser>();
    }

    @Override
    public void addInvitationListener(IAwarenessInvitationListener invitationListener) {
    }

    @Override
    public void removeInvitationListener(IAwarenessInvitationListener invitationListener) {
    }

    @Override
    public void inviteUserToChat(String room, String user) {
    }

    @Override
    public void setUserPresence(boolean available) {
    }
}
