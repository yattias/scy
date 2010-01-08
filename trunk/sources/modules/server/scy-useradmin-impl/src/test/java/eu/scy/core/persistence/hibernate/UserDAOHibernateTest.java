package eu.scy.core.persistence.hibernate;

import eu.scy.core.model.User;
import eu.scy.core.persistence.UserDAO;
import org.junit.Test;
import org.springframework.test.AbstractTransactionalSpringContextTests;


/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 29.okt.2008
 * Time: 06:47:16
 * To change this template use File | Settings | File Templates.
 */

public class UserDAOHibernateTest extends AbstractDAOTest {

    private UserDAO userDAO = null;

    public UserDAO getUserDAO() {
        return userDAO;
    }

    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Test
    public void testDAONotNull() {
        assert(userDAO != null);
    }

    @Test
    public void testCreateUser() {
        User user = getUserDAO().createUser("Henrik", "Hillary");
        assert(user != null);
        assert(user.getUserDetails() != null);
    }








}
