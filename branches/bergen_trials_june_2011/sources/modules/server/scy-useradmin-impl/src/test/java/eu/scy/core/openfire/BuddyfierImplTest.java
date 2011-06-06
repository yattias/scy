package eu.scy.core.openfire;

import eu.scy.core.persistence.hibernate.AbstractDAOTest;
import org.jivesoftware.smack.RosterEntry;

import java.util.Collection;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 02.mar.2010
 * Time: 23:17:00
 * To change this template use File | Settings | File Templates.
 */
public class BuddyfierImplTest extends AbstractDAOTest {

    private BuddyService buddyfier;
    private String host = null;

    private final String USER_NAME = "adam";
    private final String PASSWORD = "adam";
    private final String BUDDY_USERNAME = "wouter";
    private final String BUDDY_PASSWORD ="wouter";

    private String getHost() {
        //return "83.168.205.138";
        //return "scy.collide.info";
        return null;
    }

    public BuddyService getBuddyService() {
        buddyfier.setHost(getHost());
        return buddyfier;
    }

    public void setBuddyfier(BuddyService buddyfier) {
        this.buddyfier = buddyfier;
    }

    public void testServiceExists() {
        assertNotNull(getBuddyService());
    }

    public void testCreateBuddies() {
        if (getHost() != null) {
            Collection buddies = getBuddyService().getBuddies(USER_NAME, PASSWORD);
            assertNotNull(buddies);
            int count = buddies.size();
            for (Iterator iterator = buddies.iterator(); iterator.hasNext();) {
                RosterEntry rosterEntry = (RosterEntry) iterator.next();
            }

            try {
                getBuddyService().makeBuddies(USER_NAME, PASSWORD, BUDDY_USERNAME, BUDDY_PASSWORD);
            } catch (Exception e) {
                e.printStackTrace();
                fail(e.getMessage());
            }
        }

    }

    public void testBuddyCheck() {
        if (getHost() != null) {
            try {

                //getBuddyService().makeBuddies(user1, user1, user2, user2);
                //assertTrue(getBuddyService().getAreBuddies(user1, user1, user2));
                //getBuddyService().removeBuddy(USER_NAME, PASSWORD, BUDDY_USERNAME, BUDDY_PASSWORD);
                //assertFalse(getBuddyService().getAreBuddies(USER_NAME, PASSWORD, BUDDY_USERNAME));
            } catch (Exception e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

        }
    }

    public void testGetBuddyPresenceStatus() {
    }
}
