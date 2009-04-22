package eu.scy.core.persistence.hibernate;

import eu.scy.core.persistence.SCYBaseDAO;
import eu.scy.core.model.ScyBase;
import eu.scy.core.model.impl.ScyBaseObject;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import net.sf.sail.webapp.domain.Persistable;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 16.okt.2008
 * Time: 06:37:58
 * To change this template use File | Settings | File Templates.
 */
public class ScyBaseDAOHibernate extends HibernateDaoSupport implements SCYBaseDAO {


    public Object getObject(Class clazz, String id) {
        return getHibernateTemplate().get(clazz, id);
    }

    /**
     * Dangerous method - need to use ACL to be sure this one does not mess up a lot!
     */
    public Object save(Object object) {
        assert(object != null);
        if(object instanceof ScyBase) {
            ScyBase scyBaseObject = (ScyBase) object;

            if(scyBaseObject.getId() != null) {
                scyBaseObject = (ScyBase) getHibernateTemplate().merge(object);
                getHibernateTemplate().update(scyBaseObject);
            } else {
                getHibernateTemplate().saveOrUpdate(object);
            }
        } else if(object instanceof Persistable) {
            Persistable persistable = (Persistable) object;
            if(persistable.getId() != null) {
                persistable = (Persistable) getHibernateTemplate().merge(persistable);
                getHibernateTemplate().update(persistable);
            } else {
                getHibernateTemplate().saveOrUpdate(persistable);
            }
        } else {
            throw new RuntimeException("WAS NOT ABLE TO STORE OBJECT : " + object + "_____" + object.getClass().getName());
        }

        return object;
    }

}
