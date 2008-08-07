package eu.scy.core.persistence.hibernate;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 16.jun.2008
 * Time: 15:38:00
 * A hibernate base DAO
 */
public class BaseDAOHibernate extends HibernateDaoSupport {


    /**
     * Dangerous method - need to use ACL to be sure this one does not mess up a lot!
     */
    public Object save(Object object) {
        object = getHibernateTemplate().merge(object);
        getHibernateTemplate().save(object);
        return object;
    }

}
