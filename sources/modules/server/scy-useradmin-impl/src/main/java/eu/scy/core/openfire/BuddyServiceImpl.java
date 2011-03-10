package eu.scy.core.openfire;

import java.util.Collection;

import eu.scy.common.configuration.Configuration;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 02.mar.2010
 * Time: 23:06:19
 * To change this template use File | Settings | File Templates.
 */
public class BuddyServiceImpl extends OpenFireServiceImpl implements BuddyService {
    
	private static final Logger logger = Logger.getLogger(BuddyServiceImpl.class);

    static {
        BasicConfigurator.configure();
    }

    @Override
    public Collection<RosterEntry> getBuddies(String username, String password) {
        logger.info("Getting buddies for " + getUsernameWithHost(username));
        XMPPConnection connection = null;
        try {
            connection = getConnection(getUsernameWithHost(username), password);
            Roster roster = getRoster(connection);
            Collection<RosterEntry> entries = roster.getEntries();
            return entries;
        } catch (XMPPException e) {
            e.printStackTrace();
        } finally {
        	if (connection != null) {
        		connection.disconnect();
        	}
        }
        logger.info("Was not able to find any buddies for " + username);
        return null;
    }

    @Override
    public boolean getAreBuddies(String userName1, String password1, String buddyUsername) {
    	XMPPConnection connection = null;
    	try {
            connection = getConnection(userName1, password1);
            Roster roster = getRoster(connection);
            return roster.contains(getUsernameWithHost(buddyUsername));
        } catch (XMPPException e) {
            e.printStackTrace();
        } finally {
        	if (connection != null) {
        		connection.disconnect();
        	}
        }
        return false;
    }


    @Override
    public void makeBuddies(String userName1, String password1, String buddyUsername, String buddyPassword) throws Exception {
    	if (!userName1.equals(buddyUsername)) {
    		BuddyClient userClient = null;
    		BuddyClient buddyClient = null;
    		try {
	        	userClient = new BuddyClient(userName1, password1, getHost());
	        	buddyClient = new BuddyClient(buddyUsername, buddyPassword, getHost());
	        	
	        	userClient.subscribeToBuddy(buddyUsername);
	        	buddyClient.subscribeToBuddy(userName1);
	        	
	        	// give them some time to react on the requests
	        	Thread.sleep(300);
    		} finally {
    			if (userClient != null) {
    				userClient.disconnect();
    			}
    			if (buddyClient != null) {
    				buddyClient.disconnect();
    			}
    		}
        }
    }

    @Override
    public void removeBuddy(String userName1, String password1, String buddyUsername, String buddyPassword) throws Exception {
        XMPPConnection connection = null;
        try {
        	connection = getConnection(userName1, password1);
        	Roster roster = getRoster(connection);
        	if (!userName1.equals(buddyUsername)) {
        		RosterEntry entry = roster.getEntry(buddyUsername + "@" + getHost());
        		if (entry != null) {
        			roster.removeEntry(entry);
        		}
        	}
        } finally {
        	if (connection != null) {
        		connection.disconnect();
        	}
        }
    }

    @Override
    public Roster getRoster(XMPPConnection connection) {
        Roster roster = connection.getRoster();
        return roster;
    }

    @Override
    public String getBuddyPresenceStatus(String username, String password, String buddyusername) throws Exception {
    	XMPPConnection connection = null;
    	try {
	    	connection = getConnection(username, password);
	        Roster roster = getRoster(connection);
	        Presence presence = roster.getPresence(buddyusername);
	        logger.info(presence.getMode());
	        logger.info(presence.getFrom());
	        String status = presence.getStatus();
	        return status;
        } finally {
        	if (connection != null) {
        		connection.disconnect();
        	}
        }
    }


}
