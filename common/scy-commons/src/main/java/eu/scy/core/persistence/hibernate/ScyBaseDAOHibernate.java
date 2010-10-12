package eu.scy.core.persistence.hibernate;

import eu.scy.core.persistence.SCYBaseDAO;
import eu.scy.core.model.ScyBase;
import eu.scy.core.model.impl.ScyBaseObject;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 16.okt.2008
 * Time: 06:37:58
 * To change this template use File | Settings | File Templates.
 */
public class ScyBaseDAOHibernate extends HibernateDaoSupport implements SCYBaseDAO {

    private TransactionTemplate transactionTemplate;

    public Object getObject(Class clazz, String id) {
        return getHibernateTemplate().get(clazz, id);
    }


    public void save(ScyBase scyBase) {
        save((Object) scyBase);
    }



    /**
     * Dangerous method - need to use ACL to be sure this one does not mess up a lot!
     */
    public Object save(Object object) {
        logger.info("***saving: " + object);
        //getHibernateTemplate().saveOrUpdate(object);
        //object =  getHibernateTemplate().get(object.getClass(), id);
        //return object;

        assert(object != null);
        if(object instanceof ScyBase) {
            logger.info("SAVING SCY BASE!");
            ScyBase scyBaseObject = (ScyBase) object;

            if(scyBaseObject.getId() != null) {
                scyBaseObject = (ScyBase) getHibernateTemplate().merge(object);
                return scyBaseObject;
                //getHibernateTemplate().update(scyBaseObject);
            } else {
                getHibernateTemplate().saveOrUpdate(object);
            }
        } else {
            getHibernateTemplate().saveOrUpdate(object);
        }
        return object;
    }

    public TransactionTemplate getTransactionTemplate() {
        return transactionTemplate;
    }

    public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
        this.transactionTemplate = transactionTemplate;
    }


}
