package eu.scy.core.persistence.hibernate;

import eu.scy.core.model.User;
import eu.scy.core.model.impl.pedagogicalplan.AssignedPedagogicalPlanImpl;
import eu.scy.core.model.pedagogicalplan.AssignedPedagogicalPlan;
import eu.scy.core.model.pedagogicalplan.PedagogicalPlan;
import eu.scy.core.persistence.AssignedAssessmentDAO;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 26.mar.2010
 * Time: 05:41:12
 */
public class AssignedAssessmentDAOHibernate extends ScyBaseDAOHibernate implements AssignedAssessmentDAO {

    @Override
    public AssignedPedagogicalPlan assignPedagogicalPlanToUser(PedagogicalPlan plan, User user) {
        AssignedPedagogicalPlan assignedPedagogicalPlan= new AssignedPedagogicalPlanImpl();
        assignedPedagogicalPlan.setPedagogicalPlan(plan);
        assignedPedagogicalPlan.setUser(user);
        save(assignedPedagogicalPlan);
        return assignedPedagogicalPlan;
    }
    
    @Override
    public List<AssignedPedagogicalPlan> getAssignedPedagogicalPlans(User user) {
        return getSession().createQuery("From AssignedPedagogicalPlanImpl where user = :user")
                .setEntity("user", user)
                .list();


    }
}
