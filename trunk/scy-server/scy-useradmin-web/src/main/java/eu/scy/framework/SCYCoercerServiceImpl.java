package eu.scy.framework;

import org.apache.tapestry5.ioc.annotations.Inject;
import eu.scy.core.persistence.hibernate.UserDAOHibernate;
import eu.scy.core.model.impl.ScyBaseObject;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 24.nov.2008
 * Time: 09:12:53
 * To change this template use File | Settings | File Templates.
 */
public class SCYCoercerServiceImpl implements SCYCoercer{

    @Inject
    private UserDAOHibernate userDAOHibernate;


    public ScyBaseObject get(Class clazz, String id) {
        return (ScyBaseObject) userDAOHibernate.getObject(clazz, id);
    }
}
