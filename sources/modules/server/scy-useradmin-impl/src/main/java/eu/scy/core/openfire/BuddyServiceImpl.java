package eu.scy.core.openfire;

import java.util.Collection;

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
public class BuddyServiceImpl implements BuddyService {
    
	private static final Logger logger = Logger.getLogger(BuddyServiceImpl.class);

    private String host;

    static {
        BasicConfigurator.configure();
    }

    @Override
    public Collection<RosterEntry> getBuddies(String username, String password) {
        logger.info("Getting buddies for " + getUsernameWithHost(username));
        try {
            XMPPConnection connection = getConnection(getUsernameWithHost(username), password);
            Roster roster = getRoster(connection);
            Collection<RosterEntry> entries = roster.getEntries();
            return entries;
        } catch (XMPPException e) {
            e.printStackTrace();
        }

        logger.info("Was not able to find any buddies for " + username);
        return null;

    }

    @Override
    public boolean getAreBuddies(String userName1, String password1, String buddyUsername) {
        try {
            XMPPConnection connection = getConnection(userName1, password1);
            Roster roster = getRoster(connection);
            return roster.contains(getUsernameWithHost(buddyUsername));
        } catch (XMPPException e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public void makeBuddies(String userName1, String password1, String buddyUsername, String buddyPassword) throws Exception {
        if (!userName1.equals(buddyUsername)) {
        	BuddyClient userClient = new BuddyClient(userName1, password1, getHost());
        	BuddyClient buddyClient = new BuddyClient(buddyUsername, buddyPassword, getHost());
        	
        	userClient.subscribeToBuddy(buddyUsername);
        	buddyClient.subscribeToBuddy(userName1);
        	
        	// give them some time to react on the requests
        	Thread.sleep(300);
        	
        	userClient.disconnect();
        	buddyClient.disconnect();
        }
    }

    @Override
    public void removeBuddy(String userName1, String password1, String buddyUsername, String buddyPassword) throws Exception {
        XMPPConnection connection = getConnection(userName1, password1);
        Roster roster = getRoster(connection);

        if (!userName1.equals(buddyUsername)) {
            RosterEntry entry = roster.getEntry(buddyUsername + "@" + getHost());
            if (entry != null) {
                roster.removeEntry(entry);
            }
        }


        connection.disconnect();
    }

    @Override
    public Roster getRoster(XMPPConnection connection) {
        Roster roster = connection.getRoster();
        return roster;
    }

    @Override
    public String getBuddyPresenceStatus(String username, String password, String buddyusername) throws Exception {
        XMPPConnection connection = getConnection(username, password);
        Roster roster = getRoster(connection);
        Presence presence = roster.getPresence(buddyusername);
        logger.info(presence.getMode());
        logger.info(presence.getFrom());

        return presence.getStatus();
    }

    @Override
    public XMPPConnection getConnection(String userName1, String password1) throws XMPPException {
        ConnectionConfiguration config = new ConnectionConfiguration(getHost(), 5222);
        config.setCompressionEnabled(true);
        config.setReconnectionAllowed(true);

        XMPPConnection connection;
        connection = new XMPPConnection(config);
        connection.connect();
        connection.login(userName1, password1);
        return connection;
    }

    private String getUsernameWithHost(String username) {
        return username + "@" + getHost();
    }

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public void setHost(String host) {
        this.host = host;
    }
    
    static class BuddyClient implements PacketListener {

		private String user;
		
		private String password;
		
		private String host;

		private XMPPConnection connection;

		/**
		 * @param username
		 * @param host 
		 */
		public BuddyClient(String username, String password, String host) {
			this.user = username;
			this.password = password;
			this.host = host;

			ConnectionConfiguration config = new ConnectionConfiguration(host, 5222);
			config.setCompressionEnabled(true);
			config.setReconnectionAllowed(true);

			connection = new XMPPConnection(config);
			try {
				connection.connect();
				logger.debug("Connected to server.");
				connection.login(user, password, "buddyfier");
				logger.debug("Logged in as " + connection.getUser());

				PacketFilter filter = new PacketTypeFilter(Presence.class);
				connection.addPacketListener(this, filter);
			} catch (XMPPException e) {
				e.printStackTrace();
			}
		}
		
		public void disconnect() {
			connection.disconnect();
		}

		public void subscribeToBuddy(String buddy) throws XMPPException {
			connection.getRoster().createEntry(buddy + "@" + host, buddy, new String[] {"friends"});
			logger.debug("Created roster: " + user + " -> " + buddy);
		}

		public void processPacket(Packet packet) {
			Presence presence = (Presence) packet;
			Presence response = null;

			switch (presence.getType()) {
			case subscribe:
				response = new Presence(Presence.Type.subscribed);
				response.setTo(presence.getFrom());
				connection.sendPacket(response);
				logger.debug("Accepted subscription: " + user + " -> " + presence.getFrom());
				break;
			}
		}
		
		public String getUser() {
			return user;
		}
	}
}
