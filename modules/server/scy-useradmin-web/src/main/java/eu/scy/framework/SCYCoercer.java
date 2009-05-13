package eu.scy.framework;

import eu.scy.core.model.impl.ScyBaseObject;
import eu.scy.core.persistence.hibernate.UserDAOHibernate;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 24.nov.2008
 * Time: 09:14:09
 * To change this template use File | Settings | File Templates.
 */
public interface SCYCoercer {

    public ScyBaseObject get(Class clazz, String id);

    public UserDAOHibernate getUserDAOHibernate();

    public void setUserDAOHibernate(UserDAOHibernate userDAOHibernate);


}
