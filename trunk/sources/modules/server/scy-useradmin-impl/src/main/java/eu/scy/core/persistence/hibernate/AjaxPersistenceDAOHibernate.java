package eu.scy.core.persistence.hibernate;

import eu.scy.core.model.ScyBase;
import eu.scy.core.model.impl.SCYStudentUserDetails;
import eu.scy.core.model.impl.SCYTeacherUserDetails;
import eu.scy.core.model.impl.SCYUserDetails;
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
    public Object get(Class c, String id) {
        logger.info("CLASS IS " + c.getName());
        if(c.equals(SCYUserDetails.class) || c.equals(SCYStudentUserDetails.class) || c.equals(SCYTeacherUserDetails.class)) {
            Long longId = new Long(id);
            return getHibernateTemplate().get(c, longId.longValue());
        }
        return getHibernateTemplate().get(c, id);
    }
}
