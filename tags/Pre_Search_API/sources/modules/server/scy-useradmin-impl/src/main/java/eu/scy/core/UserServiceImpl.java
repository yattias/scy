package eu.scy.core;

import eu.scy.core.model.SCYGrantedAuthority;
import eu.scy.core.model.User;
import eu.scy.core.persistence.UserDAO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 14.des.2009
 * Time: 10:26:07
 * To change this template use File | Settings | File Templates.
 */
public class UserServiceImpl extends BaseServiceImpl implements UserService{

    private static Logger log = Logger.getLogger("UserServiceImpl.class");

    private UserDAO userDAO;



    @Override
    public User getUser(String username) {
        return getUserDAO().getUserByUsername(username); 
    }

    @Transactional
    @Override
    public User createUser(String username, String password, String role) {
        log.info("Creating user :" + username);
        return getUserDAO().createUser(username, password, role);
    }

    @Override
    public List<User> getUsers() {
        return getUserDAO().getUsers();
    }

    @Override
    public List<User> getStudents() {
        return getUserDAO().getStudents();
    }

    @Transactional
    @Override
    public User save(User user) {
        return (User) getUserDAO().save(user);
    }

    @Override
    public List<SCYGrantedAuthority> getGrantedAuthorities() {
        return getUserDAO().getGrantedAuthorities();
    }

    public UserDAO getUserDAO() {
        return (UserDAO) getScyBaseDAO();
    }

    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }
}