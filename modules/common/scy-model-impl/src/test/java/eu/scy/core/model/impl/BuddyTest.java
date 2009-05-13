package eu.scy.core.model.impl;

import eu.scy.core.model.User;
import eu.scy.core.model.BuddyConnection;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 16.okt.2008
 * Time: 06:25:03
 * To change this template use File | Settings | File Templates.
 */
@Test
public class BuddyTest {


    private User me;
    private User bestBuddy;
    private User secondBestBuddy;


    @BeforeTest
    private void initialize() {
        me = new SCYUserImpl();
        //me.setUserName("myself");

        bestBuddy = new SCYUserImpl();
        //bestBuddy.setUserName("Best buddy");

        secondBestBuddy = new SCYUserImpl();
        //secondBestBuddy.setUserName("Second best buddy");
    }

    public void testAddBuddy() {
        //want to get hold of spring prototype here - so I do not have to hard code the connection type (impl)
        BuddyConnection connection = new BuddyConnectionImpl();
        connection.setMyself(me);
        connection.setBuddy(bestBuddy);

        assert(connection.getBuddy() != null);
        assert(connection.getMyself() != null);
    }

}
