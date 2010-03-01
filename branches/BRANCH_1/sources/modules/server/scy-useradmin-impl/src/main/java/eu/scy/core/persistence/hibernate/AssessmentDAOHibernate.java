package eu.scy.core.persistence.hibernate;

import eu.scy.core.model.pedagogicalplan.Assessment;
import eu.scy.core.persistence.AssessmentDAO;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 22.jan.2010
 * Time: 05:45:50
 * To change this template use File | Settings | File Templates.
 */
public class AssessmentDAOHibernate extends ScyBaseDAOHibernate implements AssessmentDAO {
    @Override
    public Assessment findAssessmentByName(String s) {
        return (Assessment) getSession().createQuery("from AssessmentImpl where name like :name")
                .setString("name", s)
                .setMaxResults(1)
                .uniqueResult();
    }
}
