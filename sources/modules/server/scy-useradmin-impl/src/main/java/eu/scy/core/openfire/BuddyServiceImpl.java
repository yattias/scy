package eu.scy.core.openfire;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.jivesoftware.smack.*;

import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 02.mar.2010
 * Time: 23:06:19
 * To change this template use File | Settings | File Templates.
 */
public class BuddyServiceImpl implements BuddyService {
    private Logger logger = Logger.getLogger(BuddyServiceImpl.class);

    private String host;

    static {
        BasicConfigurator.configure();
    }

    @Override
    public Collection <RosterEntry> getBuddies(String username, String password) {
        logger.info("Getting buddies for " + getUsernameWithHost(username));
        try {
            XMPPConnection connection = getConnection(getUsernameWithHost(username), password);
            Roster roster = getRoster(connection);
            Collection <RosterEntry> entries = roster.getEntries();
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
            return roster.contains(buddyUsername);
        } catch (XMPPException e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public void makeBuddies(String userName1, String password1, String buddyUsername) throws Exception {
        XMPPConnection connection = getConnection(userName1, password1);
        Roster roster = getRoster(connection);

        if (!userName1.equals(buddyUsername)) {
            RosterEntry entry = roster.getEntry(getUsernameWithHost(buddyUsername));
            if (entry != null) {
                roster.removeEntry(entry);
            }
            roster.createEntry(buddyUsername, buddyUsername, null);
        }


        connection.disconnect();
    }

    @Override
    public void removeBuddy(String userName1, String password1, String buddyUsername) throws Exception {
        XMPPConnection connection = getConnection(userName1, password1);
        Roster roster = getRoster(connection);

        if (!userName1.equals(buddyUsername)) {
            RosterEntry entry = roster.getEntry(buddyUsername);
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
}
