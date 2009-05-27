package eu.scy.core.model.impl;

import eu.scy.core.model.SCYGroup;
import eu.scy.core.model.User;
import eu.scy.core.model.UserGroupConnection;
import org.testng.annotations.Test;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 27.mai.2009
 * Time: 20:12:10
 * To change this template use File | Settings | File Templates.
 */

@Test
public class UserGroupConnectionTest {

    public void testSetGroupAndUser() {
        User user = new SCYUserImpl();
        SCYGroup group = new SCYGroupImpl();

        UserGroupConnection connection = new UserGroupConnectionImpl(user, group);
        assert (connection.getUser() != null);
        assert (connection.getGroup() != null);
    }

}
