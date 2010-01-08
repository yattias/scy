package eu.scy.core;

import eu.scy.core.model.User;
import eu.scy.core.persistence.UserDAO;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 14.des.2009
 * Time: 10:26:07
 * To change this template use File | Settings | File Templates.
 */
public class UserServiceImpl implements UserService{

    UserDAO userDAO;


    @Override
    public User getUser(String username) {
        return getUserDAO().getUserByUsername(username); 
    }

    @Transactional
    @Override
    public User createUser(String username, String password) {
        return getUserDAO().createUser(username, password);
    }

    public UserDAO getUserDAO() {
        return userDAO;
    }

    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }
}
