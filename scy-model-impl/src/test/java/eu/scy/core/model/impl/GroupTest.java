package eu.scy.core.model.impl;

import eu.scy.core.model.Group;
import eu.scy.core.model.User;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 16.okt.2008
 * Time: 06:30:33
 * To change this template use File | Settings | File Templates.
 */
@Test
public class GroupTest {

     private static Logger log = Logger.getLogger("GroupTest.class");

    private Group testGroup = null;
    private Group childGroup = null;

    private User user1 ;

    public static final String GROUP_NAME = "HEnriks cool group";

    @BeforeTest
    private void initializeTest() {
        testGroup = new GroupImpl();
        testGroup.setName(GROUP_NAME);

        user1 = new UserImpl();
        user1.setUserName("User_1");

    }


    @Test
    public void testName() {
        assert(testGroup.getName().equals(GROUP_NAME) ) : "Exptected something else";
    }

    @Test
    public void addUserToGroup() {
        testGroup.addUser(user1);
        System.out.println(testGroup.getUsers().size());
        assert(testGroup.getUsers().contains(user1));
    }

    @Test
    public void addNullUserToGroup() {
        int size = testGroup.getUsers().size();
        testGroup.addUser(null);
        assert(size == testGroup.getUsers().size()) : "Size is " + size + " group list is " + testGroup.getUsers().size();
    }

    @Test
    public void addChildGroupToGroup() {
        childGroup = new GroupImpl();
        testGroup.addChild(childGroup);
        assert(testGroup.getChildren().contains(childGroup));
    }

    @Test
    public void addNullChildGroup() {
        int size = testGroup.getChildren().size();
        testGroup.addChild(null);
        assert(size == testGroup.getChildren().size());
    }

}
