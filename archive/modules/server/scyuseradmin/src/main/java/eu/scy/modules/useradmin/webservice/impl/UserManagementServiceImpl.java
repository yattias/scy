package eu.scy.modules.useradmin.webservice.impl;

import eu.scy.core.model.User;
import eu.scy.core.model.UserSession;
import eu.scy.core.persistence.hibernate.UserDAOHibernate;
import eu.scy.core.persistence.hibernate.UserSessionDAOHibernate;
import eu.scy.modules.useradmin.webservice.UserManagementService;
import org.apache.log4j.Logger;

import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import java.util.List;
import java.util.Collections;


/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 15.aug.2008
 * Time: 11:50:43
 * To change this template use File | Settings | File Templates.
 */

@WebService(endpointInterface = "eu.scy.modules.useradmin.webservice.UserManagementService", serviceName = "UserManagement")
public class UserManagementServiceImpl implements UserManagementService {

    private static Logger log = Logger.getLogger(UserManagementServiceImpl.class);

    private UserDAOHibernate dao = null;
    private UserSessionDAOHibernate userSessionDAO;



    @WebMethod
    public String loginUser(@WebParam(name="userName")String userName, @WebParam(name="password")String password) {
        User user = getDao().getUserByUsername(userName);
        if (user != null) {
            if (user.getPassword().equals(password)) {
                UserSession session = getUserSessionDAO().createNewUserSession(user);                
                return session.getId();
            }
        }
        return "NO_SESSION_MAN!";

    }

    @WebMethod
    public List<User> getBuddies(String userName) {
        User user = getDao().getUserByUsername(userName);
        if(user != null) {
            return getDao().getBuddies(user);
        }
        log.debug("Returning empty buddy list....");
        return Collections.EMPTY_LIST;
    }

    public UserDAOHibernate getDao() {
        return dao;
    }

    public void setDao(UserDAOHibernate dao) {
        this.dao = dao;
    }

    public UserSessionDAOHibernate getUserSessionDAO() {
        return userSessionDAO;
    }

    public void setUserSessionDAO(UserSessionDAOHibernate userSessionDAO) {
        this.userSessionDAO = userSessionDAO;
    }
}
