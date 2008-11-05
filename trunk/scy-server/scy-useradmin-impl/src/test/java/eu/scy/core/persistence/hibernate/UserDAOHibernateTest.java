package eu.scy.core.persistence.hibernate;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeTest;
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

    @BeforeTest
    protected void setupForTest() {
        user = new UserImpl();
        user.setUserName("H_IS_COOL" + System.currentTimeMillis());
        user = (User) userDAO.save(user);
        user.addRole("ROLE_ADMIN");


    }

    @Test
    public void testGetUserInRole() {
        user = new UserImpl();
        user.setUserName("H_IS_COOL" + System.currentTimeMillis());
        user = (User) userDAO.save(user);
        user.addRole("ROLE_ADMIN");
        String userRole = "ROLE_ADMIN";
        assert(getUserDAO().getIsUserInRole(userRole, user));

    }

}
