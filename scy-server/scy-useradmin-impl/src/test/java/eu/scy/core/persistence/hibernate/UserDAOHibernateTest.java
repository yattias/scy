package eu.scy.core.persistence.hibernate;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.AfterTest;
import org.springframework.test.AbstractTransactionalSpringContextTests;
import org.acegisecurity.GrantedAuthority;
import org.telscenter.sail.webapp.domain.authentication.Gender;
import eu.scy.core.persistence.UserDAO;
import eu.scy.core.model.User;
import eu.scy.core.model.UserRole;
import eu.scy.core.model.Role;
import eu.scy.core.model.SCYGroup;
import eu.scy.core.model.impl.SCYUserImpl;
import eu.scy.core.model.impl.SCYGroupImpl;
import eu.scy.core.model.impl.SCYUserDetails;

import java.util.List;
import java.util.Date;

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
    private SCYUserDetails details = null;

    public UserDAO getUserDAO() {
        return userDAO;
    }

    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    protected String[] getConfigLocations() {
        return new String[]{"classpath:/eu/scy/core/persistence/hibernate/applciationContext-hibernate-OnlyForTesting.xml"};
    }

    @Override
    protected void onSetUpBeforeTransaction() throws Exception {
        super.onSetUpBeforeTransaction();
        this.user = new SCYUserImpl();
        this.details = new SCYUserDetails();
        details.setPassword("ImALoserBabySoWhyDontYouKillMe");
        details.setUsername("IGetKnockedDownButIGetUpAgainYouNeverGonnaGetMeDown");
        details.setFirstname("Henrik");
        details.setLastname("Schralaffenland");
        details.setSignupdate(new Date());
        details.setGender(Gender.MALE);
        details.setBirthday(new Date());
        details.setNumberOfLogins(1);
        details.setLastLoginTime(new Date());
        details.setAccountAnswer("MeatLoaf");
        details.setAccountQuestion("DoYouLikeIt??");
        user.setUserDetails(details);
    }

    @AfterTest
    protected void onTearDownAfterTransaction() throws Exception {
        super.onTearDownAfterTransaction();
        this.details = null;
        this.user = null;
    }


    @Test
    public void testAddUserDetails() {
        userDAO.save(user);
        //this.user = new SCYUserImpl();
        //this.user.setUserDetails(details);
        //details = (SCYUserDetails) getUserDAO().save(details);
        assert(details.getId() != null);
    }


    /*@Test
    public void testGetUserInRole() {
        user = new SCYUserImpl();
        SCYUserDetails details = new SCYUserDetails();

        //user.setUserName("H_IS_COOL" + System.currentTimeMillis());
        user = (User) userDAO.save(user);
        getUserDAO().addRole(user, "ROLE_ADMIN");
        String userRole = "ROLE_ADMIN";
        getUserDAO().save(user);
        assert(user.getId() != null);
        GrantedAuthority[]  userRoles = user.getUserDetails().getAuthorities();//getUserRoles();
        assert (userRoles != null );
        assert (userRoles.length > 0);
        GrantedAuthority first = (GrantedAuthority) userRoles[0];
        assert(first.getAuthority().equals("ROLE_ADMIN"));
    }
    */
    @Test
    public void testGetUser() {
        user = (User) getUserDAO().save(user);

        Long userId = user.getId();
        assert(userId != null);
        User loaded = getUserDAO().getUser(userId);
        //assert(loaded != null);
    }

    @Test
    public void testDeleteGroup() {                                                                        
        SCYGroup group = new SCYGroupImpl();
        group = (SCYGroup) userDAO.save(group);
        assert(group.getId() != null);

        String id = group.getId();
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++UserDAOHibernateTest.testDeleteGroup " + id);
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++UserDAOHibernateTest.testDeleteGroup " + id);

        userDAO.deleteGroup(id);
        assertNull(userDAO.getGroup(id));



    }


}
