package eu.scy.core.persistence.hibernate;

import eu.scy.core.persistence.UserSessionDAO;
import eu.scy.core.model.User;
import eu.scy.core.model.UserSession;
import eu.scy.core.model.impl.UserSessionImpl;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationContext;
import org.springframework.beans.BeansException;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 21.okt.2008
 * Time: 08:00:16
 * To change this template use File | Settings | File Templates.
 */
public class UserSessionDAOHibernate extends ScyBaseDAOHibernate implements UserSessionDAO, ApplicationContextAware {

    private ApplicationContext applicationContext;

    public void loginUser(User user) {
        UserSession session = getActiveSession(user);
        if(session == null) {
            session = (UserSession) applicationContext.getBean("userSession");
        }
        session.setSessionStarted(System.currentTimeMillis());
        session.setSessionActive(true);
        user.addUserSession(session);
        save(user);
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
        return (UserSession) getSession().createQuery("From UserSessionImpl where user = :user and sessionActive = :sessionActiveFlag")
                .setEntity("user", user)
                .setBoolean("sessionActiveFlag", Boolean.TRUE)
                .setMaxResults(1)
                .uniqueResult();
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
