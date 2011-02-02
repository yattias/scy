/**
 * 
 */
package eu.scy.jabber;

import java.util.Vector;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;

/**
 * @author giemza
 * 
 */
public class Buddyfier {

	private static Logger logger;

	private static String host = "scy.collide.info";

	private static final boolean createUsers = false;

	static {
		logger = Logger.getLogger(Buddyfier.class);
		BasicConfigurator.configure();
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		logger.debug("Buddyfier started!");

		// String[] users = {"adam", "alfons", "andreas", "barbara", "bjoerge",
		// "david", "jan", "jeremy", "jakob", "lars", "marjolaine", "stefan",
		// "ton", "tony", "wolfgang", "wouter"};
		String[] users = { "adam", "jeremy", "klaus", "stefan", "lars"};

		Vector<BuddyClient> clients = new Vector<BuddyClient>();

		for (String user : users) {
			clients.add(new BuddyClient(user));
		}
		
		// let clients connect and start up
		Thread.sleep(3000);

		for (BuddyClient client : clients) {
			for (String buddy : users) {
				if (!client.getUser().equals(buddy)) {
					client.subscribeToBuddy(buddy);
				}
			}
		}
		
		// let clients answer the requests
		Thread.sleep(10000);
		
		for (BuddyClient client : clients) {
			client.disconnect();
		}

	}

	static class BuddyClient implements PacketListener {

		private String user;

		private XMPPConnection connection;

		/**
		 * @param username
		 */
		public BuddyClient(String username) {
			this.user = username;

			ConnectionConfiguration config = new ConnectionConfiguration(host, 5222);
			config.setCompressionEnabled(true);
			config.setReconnectionAllowed(true);

			connection = new XMPPConnection(config);
			try {
				connection.connect();
				logger.debug("Connected to server.");
				if (createUsers) {
					connection.getAccountManager().createAccount(user, user);
					logger.debug("Created user " + user);
				}
				connection.login(user, user);
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
			RosterEntry entry = connection.getRoster().getEntry(buddy + "@" + host);
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
