package eu.scy.core.model.impl;

import eu.scy.core.model.SCYGroup;
import eu.scy.core.model.User;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 16.okt.2008
 * Time: 06:30:33
 * To change this template use File | Settings | File Templates.
 */

public class GroupTest extends TestCase {

     private static Logger log = Logger.getLogger("GroupTest.class");

    private SCYGroup testGroup = null;
    private SCYGroup childGroup = null;

    private User user1 ;

    public static final String GROUP_NAME = "HEnriks cool group";

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        testGroup = new SCYGroupImpl();
        testGroup.setName(GROUP_NAME);

        //user1 = new SCYUserImpl();
        //user1.setUserName("User_1");

    }


    @Test
    public void testName() {
        assert(testGroup.getName().equals(GROUP_NAME) ) : "Exptected something else";
    }

    @Test
    public void addUserToGroup() {
        testGroup.addMember(user1);
        //System.out.println(testGroup.getUsers().size());
        //assert(testGroup.getUsers().contains(user1));
    }

    @Test
    public void addNullUserToGroup() {
        //int size = testGroup.getUsers().size();
        testGroup.addMember(null);
        //assert(size == testGroup.getUsers().size()) : "Size is " + size + " group list is " + testGroup.getUsers().size();
    }

}
