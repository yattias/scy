package eu.scy.core.openfire;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 02.mar.2010
 * Time: 23:06:58
 * To change this template use File | Settings | File Templates.
 */
public interface BuddyService {
    Collection <RosterEntry> getBuddies(String username, String password);

    boolean getAreBuddies(String userName1, String password1, String buddyUsername);

    void makeBuddies(String userName1, String password1, String buddyUsername, String byddyPassword) throws Exception;

    void removeBuddy(String userName1, String password1, String buddyUsername, String buddyPassword) throws Exception;

    Roster getRoster(XMPPConnection connection);

    XMPPConnection getConnection(String userName1, String password1) throws XMPPException;

    String getHost();

    void setHost(String host);

    String getBuddyPresenceStatus(String username, String password, String buddyusername) throws Exception;
}
