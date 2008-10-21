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
public class UserSessionDAOImpl extends ScyBaseDAOHibernate implements UserSessionDAO, ApplicationContextAware {
    private ApplicationContext applicationContext;

    public void loginUser(User user) {
        UserSession session = (UserSession) applicationContext.getBean("userSession");
        
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
