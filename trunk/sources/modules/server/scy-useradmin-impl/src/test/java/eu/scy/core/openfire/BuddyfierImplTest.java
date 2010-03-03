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

    private String getHost() {
        //return "83.168.205.138";
        //return "scy.collide.info";
        //return null;
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
        for (int counter = 0; counter < 15; counter++) {
            logger.info("STARTING TEST!!");
        }
        if (getHost() != null) {
            Collection buddies = getBuddyService().getBuddies("adam", "adam");
            assertNotNull(buddies);
            int count = buddies.size();
            for (Iterator iterator = buddies.iterator(); iterator.hasNext();) {
                RosterEntry rosterEntry = (RosterEntry) iterator.next();
                logger.info("==============================>" + rosterEntry.getName() + " " + rosterEntry.getUser() + " " + rosterEntry.getStatus());
            }

            try {
                getBuddyService().makeBuddies("adam", "adam", "stefan", "stefan");
            } catch (Exception e) {
                e.printStackTrace();
                fail(e.getMessage());
            }
        }
        for (int counter = 0; counter < 15; counter++) {
            logger.info("TEST DONE!!");
        }

    }

    public void testBuddyCheck() {
        for (int counter = 0; counter < 15; counter++) {
            logger.info("STARTING TEST!!");
        }
        if (getHost() != null) {
            try {
                String user1 = "adam";
                String user2 = "ton";

                //getBuddyService().makeBuddies(user1, user1, user2, user2);
                //assertTrue(getBuddyService().getAreBuddies(user1, user1, user2));
                getBuddyService().removeBuddy(user1, user1, user2, user2);
                assertFalse(getBuddyService().getAreBuddies(user1, user1, user2));
            } catch (Exception e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

        }
    }
}
