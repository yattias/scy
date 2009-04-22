package eu.scy.core.persistence.hibernate;

import org.testng.annotations.Test;
import org.testng.annotations.AfterTest;
import org.springframework.test.AbstractTransactionalSpringContextTests;
import org.telscenter.sail.webapp.domain.authentication.Gender;
import eu.scy.core.persistence.UserDAO;
import eu.scy.core.model.User;
import eu.scy.core.model.SCYGroup;
import eu.scy.core.model.impl.SCYUserImpl;
import eu.scy.core.model.impl.SCYGroupImpl;
import eu.scy.core.model.impl.SCYUserDetails;

import java.util.List;
import java.util.Date;

import net.sf.sail.webapp.domain.authentication.impl.PersistentGrantedAuthority;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 29.okt.2008
 * Time: 06:47:16
 * To change this template use File | Settings | File Templates.
 */
@Test
public class UserDAOHibernateTest extends AbstractTransactionalSpringContextTests {

    public final static String USER_NAME = "IGetKnockedDownButIGetUpAgainYouNeverGonnaGetMeDown";
    public final static String STUDENT_USER_ROLE = "STUDENT_USER";
    public final static String TEACHER_USER_ROLE = "TEACHER_USER";

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

    @Override
    protected void onSetUpBeforeTransaction() throws Exception {
        super.onSetUpBeforeTransaction();
        this.user = createNewUser(USER_NAME);
    }

    @AfterTest
    protected void onTearDownAfterTransaction() throws Exception {
        super.onTearDownAfterTransaction();
        this.user = null;
    }


    @Test
    public void testAddUserDetails() {
        userDAO.save(user);
        SCYUserDetails details = (SCYUserDetails) user.getUserDetails();
        assert(details.getId() != null);
    }

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

        userDAO.deleteGroup(id);
        assertNull(userDAO.getGroup(id));



    }

    public void testDeleteUser() {
        user = (User) getUserDAO().save(user);
        Long userId = user.getId();

        User found = getUserDAO().getUser(userId);
        assertNotNull(found);

        getUserDAO().deleteUser(user.getId());
        User foundAfterDelete = (User) getUserDAO().getUser(userId);
        assertNull(foundAfterDelete);
    }

    public void testGetUserByUsername() {
        getUserDAO().save(user);
        assertNull(getUserDAO().getUserByUsername("Hi"));
        assertNotNull(getUserDAO().getUserByUsername(USER_NAME));
    }

    public void testGetSecureUsername() {
        getUserDAO().save(user);

        String userNameSuggestion = getUserDAO().getSecureUserName(USER_NAME);
        assertEquals(userNameSuggestion, USER_NAME + 1);

        User newUser = createNewUser(userNameSuggestion);

        getUserDAO().save(newUser);

        
        String newUserNameSuggestion = getUserDAO().getSecureUserName(USER_NAME);
        assertEquals(newUserNameSuggestion, USER_NAME + 2);


    }

    public void testGetUsers() {
        User userOne = createNewUser("USER_1");
        getUserDAO().save(userOne);

        User userTwo = createNewUser("USER_2");
        getUserDAO().save(userTwo);

        List users = getUserDAO().getUsers();
        assert(users.contains(userOne));
        assert(users.contains(userTwo));
    }

    public void testGetUsersAfterDelete() {
        User user1 = createAndSaveUser();
        User user2 = createAndSaveUser();
        User user3 = createAndSaveUser();

        assertTrue(getUserDAO().getUsers().size() == 3);
        getUserDAO().deleteUser(user1.getId());
        assertTrue(getUserDAO().getUsers().size() ==2);

    }

    public void testGetUserRole() {
        User daUser = createNewUser(getUserDAO().getSecureUserName(USER_NAME));
        getUserDAO().save(daUser);
        Long id = daUser.getId();

        User loaded = (User) getUserDAO().getUser(id);
        assert(loaded.getUserDetails().getAuthorities().length == 1);
    }

    public void testGetAllUserRoles() {
        User daUser = createNewUser(getUserDAO().getSecureUserName(USER_NAME));
        PersistentGrantedAuthority teacherAuth = new PersistentGrantedAuthority();
        teacherAuth.setAuthority(TEACHER_USER_ROLE);
        daUser.getUserDetails().addAuthority(teacherAuth);
        getUserDAO().save(daUser);

        User loaded = getUserDAO().getUser(daUser.getId());
        assert(loaded.getUserDetails().getAuthorities().length == 2);

    }


    private User createAndSaveUser() {
        User returnUser = createNewUser(getUserDAO().getSecureUserName(USER_NAME));
        getUserDAO().save(returnUser);
        return returnUser;
    }

    private User createNewUser(String userNameSuggestion) {
        User newUser = new SCYUserImpl();
        SCYUserDetails newDetails = new SCYUserDetails();
        newDetails.setPassword("ImALoserBabySoWhyDontYouKillMe");
        newDetails.setUsername(userNameSuggestion);
        newDetails.setFirstname("Henrik");
        newDetails.setLastname("Schralaffenland");
        newDetails.setSignupdate(new Date());
        newDetails.setGender(Gender.MALE);
        newDetails.setBirthday(new Date());
        newDetails.setNumberOfLogins(1);
        newDetails.setLastLoginTime(new Date());
        newDetails.setAccountAnswer("MeatLoaf");
        newDetails.setAccountQuestion("DoYouLikeIt??");
        newUser.setUserDetails(newDetails);

        PersistentGrantedAuthority persistentGrantedAuthority = new PersistentGrantedAuthority();
        persistentGrantedAuthority.setAuthority(STUDENT_USER_ROLE);
        newUser.getUserDetails().addAuthority(persistentGrantedAuthority);



        return newUser;
    }




}
