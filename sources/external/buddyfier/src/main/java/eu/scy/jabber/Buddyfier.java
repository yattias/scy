/**
 * 
 */
package eu.scy.jabber;


import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPConnection;

/**
 * @author giemza
 *
 */
public class Buddyfier {
	
	private static Logger logger;
	
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
		
//		String[] users = {"adam", "alfons", "andreas", "barbara", "bjoerge", "david", "jan", "jeremy", "jakob", "lars", "marjolaine", "stefan", "ton", "tony", "wolfgang", "wouter"};
		String[] users = {"hhh", "hillary", "adie", "lars"};
		
		//String host = "scy.collide.info";
		String host = "83.168.205.138";

		ConnectionConfiguration config = new ConnectionConfiguration(host, 5222);
		config.setCompressionEnabled(true);
        config.setReconnectionAllowed(true);
		
        XMPPConnection connection;
        for (String user : users) {
        	logger.debug("Creating buddies for " + user);
        	connection = new XMPPConnection(config);
        	connection.connect();
        	logger.debug("Connected to server.");
        	connection.login(user, user);
        	logger.debug("Logged in as " + connection.getUser());
        	
        	Roster roster = connection.getRoster();
        	for (String buddy : users) {
        		if(!user.equals(buddy)) {
        			RosterEntry entry = roster.getEntry(buddy + "@" + host);
        			if(entry != null) {
        				roster.removeEntry(entry);
        				logger.debug("Removed roster: " + user + " -> " + buddy);
        			}
        			roster.createEntry(buddy + "@" + host, buddy, null);
        			logger.debug("Created roster: " + user + " -> " + buddy);
        		}
			}
        	connection.disconnect();
		}
	}
}
