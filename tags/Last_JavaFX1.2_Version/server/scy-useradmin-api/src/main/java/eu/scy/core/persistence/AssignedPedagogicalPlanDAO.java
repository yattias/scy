package eu.scy.core.persistence;

import eu.scy.core.model.User;
import eu.scy.core.model.pedagogicalplan.AssignedPedagogicalPlan;
import eu.scy.core.model.pedagogicalplan.PedagogicalPlan;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 26.mar.2010
 * Time: 05:40:46
 */
public interface AssignedPedagogicalPlanDAO extends BaseDAO{
    AssignedPedagogicalPlan assignPedagogicalPlanToUser(PedagogicalPlan plan, User user);

    List<AssignedPedagogicalPlan> getAssignedPedagogicalPlans(User user);

    List<AssignedPedagogicalPlan> getAssignedPedagogicalPlans(PedagogicalPlan pedagogicalPlan);

    public Long getAssignedPedagogicalPlansCount(PedagogicalPlan pedagogicalPlan);

    void removeAssignedAssessment(User user, PedagogicalPlan plan);

    AssignedPedagogicalPlan getCurrentAssignedPedagogicalPlan(User currentUser);
}
