package eu.scy.core.persistence.hibernate;

import eu.scy.core.model.ScyBase;
import eu.scy.core.persistence.AjaxPersistenceDAO;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 24.mar.2010
 * Time: 06:14:10
 * To change this template use File | Settings | File Templates.
 */
public class AjaxPersistenceDAOHibernate extends ScyBaseDAOHibernate implements AjaxPersistenceDAO {
    @Override
    public ScyBase get(Class c, String id) {
        return (ScyBase) getHibernateTemplate().get(c, id);
    }
}
