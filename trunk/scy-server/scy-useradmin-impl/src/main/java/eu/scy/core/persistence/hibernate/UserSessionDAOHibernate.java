package eu.scy.core.persistence.hibernate;

import eu.scy.core.persistence.UserSessionDAO;
import eu.scy.core.model.User;
import eu.scy.core.model.UserSession;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationContext;
import org.springframework.beans.BeansException;

import javax.security.auth.login.LoginException;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 21.okt.2008
 * Time: 08:00:16
 * To change this template use File | Settings | File Templates.
 */
public class UserSessionDAOHibernate extends ScyBaseDAOHibernate implements UserSessionDAO, ApplicationContextAware {

     private static Logger log = Logger.getLogger("UserSessionDAOHibernate.class");
    private ApplicationContext applicationContext;

    public UserSession loginUser(String userName, String password) throws LoginException{
        /*User user = (User) getSession().createQuery("From SCYUserImpl where userName like :username and password like :password")
                .setString("username", userName)
                .setString("password", password)
                .setMaxResults(1)
                .uniqueResult();
        return loginUser(user);
        */
        throw new RuntimeException("NOT IMPLEMENTED");
    }
    public UserSession loginUser(User user) throws LoginException {
        if(user == null) {
            throw new LoginException("Login failed");
        }
        UserSession session = getActiveSession(user);

        if(session == null) {
            session = (UserSession) applicationContext.getBean("userSession");
        }
        session.setSessionStarted(System.currentTimeMillis());
        session.setSessionActive(true);
        //user.addUserSession(session);

        save(user);
        log.info("Found session...");
        //return session;
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    public void logoutUser(User user) {
        UserSession userSession = getActiveSession(user);
        if (userSession != null) {
            userSession.setSessionEnded(System.currentTimeMillis());
            userSession.setSessionActive(false);
            save(userSession);
        }
    }

    public UserSession getActiveSession(User user) {
        throw new RuntimeException("NOT IMPLEMENTED");
        /*log.info("Getting active session for user: " + user.getUserName());
        return (UserSession) getSession().createQuery("From UserSessionImpl where user = :user and sessionActive = :sessionActiveFlag")
                .setEntity("user", user)
                .setBoolean("sessionActiveFlag", Boolean.TRUE)
                .setMaxResults(1)
                .uniqueResult();
                */
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
