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
 * To change this template use File | Settings | File Templates.
 */
public interface AssignedAssessmentDAO extends BaseDAO{
    AssignedPedagogicalPlan assignPedagogicalPlanToUser(PedagogicalPlan plan, User user);

    List<AssignedPedagogicalPlan> getAssignedPedagogicalPlans(User user);
}
