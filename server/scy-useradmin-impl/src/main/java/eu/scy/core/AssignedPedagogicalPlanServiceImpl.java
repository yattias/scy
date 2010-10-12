package eu.scy.core;

import eu.scy.core.model.User;
import eu.scy.core.model.pedagogicalplan.AssignedPedagogicalPlan;
import eu.scy.core.model.pedagogicalplan.PedagogicalPlan;
import eu.scy.core.persistence.AssignedPedagogicalPlanDAO;
import eu.scy.core.persistence.SCYBaseDAO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 26.mar.2010
 * Time: 05:42:12
 */
public class AssignedPedagogicalPlanServiceImpl extends BaseServiceImpl implements AssignedPedagogicalPlanService {

    private AssignedPedagogicalPlanDAO assignedPedagogicalPlanDAO;

    @Override
    public List<AssignedPedagogicalPlan> getAssignedPedagogicalPlans(PedagogicalPlan pedagogicalPlan) {
        return getAssignedPedagogicalPlanDAO().getAssignedPedagogicalPlans(pedagogicalPlan);
    }

    @Override
    @Transactional
    public AssignedPedagogicalPlan assignPedagogicalPlanToUser(PedagogicalPlan plan, User user) {
        return getAssignedPedagogicalPlanDAO().assignPedagogicalPlanToUser(plan, user);
    }

    public Long getAssignedPedagogicalPlansCount(PedagogicalPlan pedagogicalPlan) {
        return getAssignedPedagogicalPlanDAO().getAssignedPedagogicalPlansCount(pedagogicalPlan);
    }

    @Override
    public List<AssignedPedagogicalPlan> getAssignedPedagogicalPlans(User user) {
        return getAssignedPedagogicalPlanDAO().getAssignedPedagogicalPlans(user);
    }

    @Override
    @Transactional
    public void removeAssignedAssessment(User user, PedagogicalPlan plan) {
        getAssignedPedagogicalPlanDAO().removeAssignedAssessment(user, plan);
    }

    @Override
    public AssignedPedagogicalPlan getCurrentAssignedPedagogicalPlan(User currentUser) {
        return getAssignedPedagogicalPlanDAO().getCurrentAssignedPedagogicalPlan(currentUser);
    }

    public AssignedPedagogicalPlanDAO getAssignedPedagogicalPlanDAO() {
        return assignedPedagogicalPlanDAO;
    }

    public void setAssignedPedagogicalPlanDAO(AssignedPedagogicalPlanDAO assignedPedagogicalPlanDAO) {
        setScyBaseDAO((SCYBaseDAO) assignedPedagogicalPlanDAO);
        this.assignedPedagogicalPlanDAO = assignedPedagogicalPlanDAO;
    }
}
