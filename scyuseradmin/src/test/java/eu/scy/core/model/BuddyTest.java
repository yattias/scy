package eu.scy.core.model;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeTest;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 05.okt.2008
 * Time: 13:15:19
 * To change this template use File | Settings | File Templates.
 */
@Test
public class BuddyTest {

    private User me;
    private User bestBuddy;
    private User secondBestBuddy;


    @BeforeTest
    private void initialize() {
        me = new User();
        me.setUserName("myself");

        bestBuddy = new User();
        bestBuddy.setUserName("Best buddy");

        secondBestBuddy = new User();
        secondBestBuddy.setUserName("Second best buddy");
    }

    public void testAddBuddy() {
        BuddyConnection connection = new BuddyConnection();
        connection.setMyself(me);
        connection.setBuddy(bestBuddy);

        assert(connection.getBuddy() != null);
        assert(connection.getMyself() != null);
    }

}
