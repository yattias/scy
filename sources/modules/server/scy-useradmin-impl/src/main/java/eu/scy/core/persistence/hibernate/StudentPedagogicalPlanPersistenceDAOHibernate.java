package eu.scy.core.persistence.hibernate;

import eu.scy.core.model.User;
import eu.scy.core.model.impl.student.StudentPlanELOImpl;
import eu.scy.core.model.pedagogicalplan.PedagogicalPlan;
import eu.scy.core.model.student.StudentPlanELO;
import eu.scy.core.persistence.StudentPedagogicalPlanPersistenceDAO;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 12.jan.2010
 * Time: 12:05:10
 */
public class StudentPedagogicalPlanPersistenceDAOHibernate extends ScyBaseDAOHibernate implements StudentPedagogicalPlanPersistenceDAO {
    
    public StudentPlanELO createStudentPlan(PedagogicalPlan pedagogicalPlan, User user) {
        assert(pedagogicalPlan != null);
        assert(user != null);
        StudentPlanELOImpl plan = new StudentPlanELOImpl();
        plan.setPedagogicalPlan(pedagogicalPlan);
        plan.setUser(user);
        save(plan);
        return plan;
    }

    public List getStudentPlans(User user) {
        return getSession().createQuery("from StudentPlanELOImpl where user = :user")
                .setEntity("user", user)
                .list();
    }
}
