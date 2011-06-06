package eu.scy.core;

import eu.scy.core.model.User;
import eu.scy.core.model.pedagogicalplan.AssignedPedagogicalPlan;
import eu.scy.core.model.pedagogicalplan.PedagogicalPlan;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 26.mar.2010
 * Time: 05:40:17
 */
public interface AssignedPedagogicalPlanService extends BaseService{

    List <AssignedPedagogicalPlan> getAssignedPedagogicalPlans(PedagogicalPlan pedagogicalPlan);

    AssignedPedagogicalPlan assignPedagogicalPlanToUser(PedagogicalPlan plan, User user);

    Long getAssignedPedagogicalPlansCount(PedagogicalPlan pedagogicalPlan);

    List<AssignedPedagogicalPlan> getAssignedPedagogicalPlans(User user);

    void removeAssignedAssessment(User user, PedagogicalPlan plan);

    AssignedPedagogicalPlan getCurrentAssignedPedagogicalPlan(User currentUser);
}
