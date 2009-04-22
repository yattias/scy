package eu.scy.core.persistence.hibernate;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import eu.scy.core.model.SCYBaseObject;
import eu.scy.core.persistence.BaseDAO;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 16.jun.2008
 * Time: 15:38:00
 * A hibernate base DAO
 */
public class BaseDAOHibernate extends HibernateDaoSupport implements BaseDAO {


    /**
     * Dangerous method - need to use ACL to be sure this one does not mess up a lot!
     */
    public Object save(Object object) {
        if (object instanceof SCYBaseObject) {
            object = getHibernateTemplate().merge(object);
            SCYBaseObject scyBaseObject = (SCYBaseObject) object;
            if (scyBaseObject.getId() == null) {
                System.out.println("CREATE NEW....");
                getHibernateTemplate().save(object);
            } else {
                System.out.println("OBJECT ALREADY EXISTS - UPDATE!");
                getHibernateTemplate().update(object);
            }
        } else {
            System.out.println("!!!! NOT SCY BASE OBJECT - WHAT TO DO????");
        }

        return object;
    }

}
