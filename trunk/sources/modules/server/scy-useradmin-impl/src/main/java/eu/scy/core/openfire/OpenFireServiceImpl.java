package eu.scy.core.openfire;

import eu.scy.common.configuration.Configuration;
import org.apache.log4j.Logger;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 09.apr.2010
 * Time: 15:22:48
 * To change this template use File | Settings | File Templates.
 */
public class OpenFireServiceImpl implements OpenFireService {

    private static final Logger logger = Logger.getLogger(OpenFireServiceImpl.class);

    private String host;

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

    @Override
    public String getUsernameWithHost(String username) {
        return username + "@" + getHost();
    }

    @Override
    public String getHost() {
        return Configuration.getInstance().getOpenFireHost();
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
			connection.removePacketListener(this);
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
