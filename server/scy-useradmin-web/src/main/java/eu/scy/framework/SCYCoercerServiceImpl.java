package eu.scy.framework;

import org.apache.tapestry5.ioc.annotations.Inject;
import eu.scy.core.persistence.hibernate.UserDAOHibernate;
import eu.scy.core.model.impl.ScyBaseObject;

import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 24.nov.2008
 * Time: 09:12:53
 * To change this template use File | Settings | File Templates.
 */
public class SCYCoercerServiceImpl implements SCYCoercer {
    private static Logger log = Logger.getLogger("SCYCoercerServiceImpl.class");

    @Inject
    private UserDAOHibernate userDAOHibernate;

    public UserDAOHibernate getUserDAOHibernate() {
        return userDAOHibernate;
    }

    public void setUserDAOHibernate(UserDAOHibernate userDAOHibernate) {
        this.userDAOHibernate = userDAOHibernate;
    }

    public ScyBaseObject get(Class clazz, String id) {
        System.out.println("getting object of type: " + clazz + " with id: " + id);
        //log.debug("------- Getting object " + id + " of type: " + clazz.getName());
        if(getUserDAOHibernate() == null) throw new NullPointerException("USERDAO IS NULL!!");
        return (ScyBaseObject) getUserDAOHibernate().getObject(clazz, id);
    }
}
