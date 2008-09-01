package eu.scy.modules.useradmin.webservice.impl;

import eu.scy.modules.useradmin.webservice.UserManagementService;
import eu.scy.core.persistence.hibernate.UserDAOHibernate;
import eu.scy.core.model.User;
import org.apache.log4j.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 15.aug.2008
 * Time: 11:50:43
 * To change this template use File | Settings | File Templates.
 */
public class UserManagementServiceImpl implements UserManagementService {

    private static Logger log = Logger.getLogger(UserManagementServiceImpl.class);

    private UserDAOHibernate dao = null;


    public String loginUser(String userName, String password) {
        User user = getDao().getUserByUsername(userName);
        if (user != null) {
            if (user.getPassword().equals(password)) {
                //TODO: Replace this with a real session. This will at least get the IMO-guys running.
                return userName;
            }
        }
        return "NO_SESSION_MAN!";

    }

    public UserDAOHibernate getDao() {
        return dao;
    }

    public void setDao(UserDAOHibernate dao) {
        this.dao = dao;
    }
}
