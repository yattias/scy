package eu.scy.core.persistence.hibernate;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeTest;
import org.springframework.test.AbstractTransactionalSpringContextTests;
import eu.scy.core.persistence.UserDAO;
import eu.scy.core.model.User;
import eu.scy.core.model.UserRole;
import eu.scy.core.model.Role;
import eu.scy.core.model.Group;
import eu.scy.core.model.impl.UserImpl;
import eu.scy.core.model.impl.GroupImpl;

import java.util.List;

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
        getUserDAO().addRole(user, "ROLE_ADMIN");
        String userRole = "ROLE_ADMIN";
        getUserDAO().save(user);
        assert(user.getId() != null);
        List userRoles = user.getUserRoles();
        assert (userRoles != null );
        assert (userRoles.size() > 0);
        UserRole first = (UserRole) userRoles.get(0);
        assert (first.getId() != null);
        Role role = first.getRole();
        assert(role != null);
        assert(role.getName().equals("ROLE_ADMIN"));
        assert(role.getId() != null);
        assert(getUserDAO().getIsUserInRole(userRole, user));

    }

    @Test
    public void testGetUser() {
        User user = new UserImpl();
        user.setUserName(getUserDAO().getSecureUserName("hh"));
        user = (User) getUserDAO().save(user);
        Long userId = user.getId();
        assert(userId != null);
        User loaded = getUserDAO().getUser(userId);
        assert(loaded != null);
    }


    @Test
    public void testDeleteGroup() {
        Group group = new GroupImpl();
        group = (Group) userDAO.save(group);
        assert(group.getId() != null);

        String id = group.getId();

        userDAO.deleteGroup(id);
        userDAO.getGroup(id);

    }
}
