package eu.scy.core.persistence.hibernate;

import eu.scy.core.persistence.SCYBaseDAO;
import eu.scy.core.model.ScyBase;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 16.okt.2008
 * Time: 06:37:58
 * To change this template use File | Settings | File Templates.
 */
public class ScyBaseDAOHibernate extends HibernateDaoSupport implements SCYBaseDAO {



    /**
     * Dangerous method - need to use ACL to be sure this one does not mess up a lot!
     */
    public Object save(Object object) {
        if (object instanceof ScyBase) {
            object = getHibernateTemplate().merge(object);
            ScyBase scyBaseObject = (ScyBase) object;
            if (scyBaseObject.getId() == null) {
                getHibernateTemplate().save(object);
            } else {
                getHibernateTemplate().update(object);
            }
        } else {
            //do something smart
        }

        return object;
    }

}
