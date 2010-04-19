package eu.scy.core.persistence.hibernate;

import eu.scy.core.model.UserSession;
import eu.scy.core.model.User;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 03.sep.2008
 * Time: 11:34:37
 * To change this template use File | Settings | File Templates.
 */
public class UserSessionDAOHibernate extends BaseDAOHibernate {

    public UserSession createNewUserSession(User user) {
        UserSession session = new UserSession();
        session.setSessionStarted(System.currentTimeMillis());
        session.setUser(user);
        session = (UserSession) save(session);
        return session;
    }

}
