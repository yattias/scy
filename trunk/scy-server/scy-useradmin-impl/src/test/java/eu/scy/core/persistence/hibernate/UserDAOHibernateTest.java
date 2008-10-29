package eu.scy.core.persistence.hibernate;

import org.testng.annotations.Test;
import org.springframework.test.AbstractTransactionalSpringContextTests;
import eu.scy.core.persistence.UserDAO;
import eu.scy.core.model.User;
import eu.scy.core.model.impl.UserImpl;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 29.okt.2008
 * Time: 06:47:16
 * To change this template use File | Settings | File Templates.
 */
@Test
public class UserDAOHibernateTest extends AbstractTransactionalSpringContextTests {

    private UserDAO userDAO;

    private User user = null;

    public UserDAO getUserDAO() {
        return userDAO;
    }

    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    protected String[] getConfigLocations() {
        return new String[]{"classpath:/eu/scy/core/persistence/hibernate/applciationContext-hibernate-OnlyForTesting.xml"};
    }

    protected void setupForTest() {
        user = new UserImpl();
        user.addRole("ROLE_ADMIN");


    }

    @Test
    public void testGetUserInRole() {
        String userRole = "ROLE_ADMIN";
        assert(getUserDAO().getUserInRole(userRole, user));

    }

}
