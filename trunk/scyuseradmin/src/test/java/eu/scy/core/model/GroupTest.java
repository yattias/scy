package eu.scy.core.model;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeTest;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 14.aug.2008
 * Time: 13:01:17
 * To change this template use File | Settings | File Templates.
 */
@Test
public class GroupTest {

    Group testGroup = null;

    public static final String GROUP_NAME = "HEnriks cool group";

    @BeforeTest
    private void initializeTest() {
        testGroup = new Group();
        testGroup.setName(GROUP_NAME);

    }


    @Test 
    public void testName() {
        assert(testGroup.getName().equals(GROUP_NAME) ) : "Exptected something else";
    }

}
