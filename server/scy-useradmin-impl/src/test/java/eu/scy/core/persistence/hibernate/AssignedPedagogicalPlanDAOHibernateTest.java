package eu.scy.core.persistence.hibernate;

import eu.scy.core.model.User;
import eu.scy.core.model.pedagogicalplan.AssignedPedagogicalPlan;
import eu.scy.core.model.pedagogicalplan.PedagogicalPlan;
import eu.scy.core.model.pedagogicalplan.PedagogicalPlanTemplate;
import eu.scy.core.persistence.AssignedPedagogicalPlanDAO;
import eu.scy.core.persistence.UserDAO;
import org.junit.Test;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 26.mar.2010
 * Time: 05:48:16
 * To change this template use File | Settings | File Templates.
 */
public class AssignedPedagogicalPlanDAOHibernateTest extends AbstractPedagogicalPlanRelatedTest {

    private AssignedPedagogicalPlanDAO assignedPedagogicalPlanDAO;
    private UserDAO userDAO;

    public AssignedPedagogicalPlanDAO getAssignedAssessmentDAO() {
        return assignedPedagogicalPlanDAO;
    }

    public void setAssignedAssessmentDAO(AssignedPedagogicalPlanDAO assignedPedagogicalPlanDAO) {
        this.assignedPedagogicalPlanDAO = assignedPedagogicalPlanDAO;
    }

    public UserDAO getUserDAO() {
        return userDAO;
    }

    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Test
    public void testService() {
        assertNotNull(getAssignedAssessmentDAO());
    }

    public void testAssignPedagogicalPlanToUser() {
        PedagogicalPlan plan = createPedagogicalPlan("test");
        assertNotNull(plan);
        assertNotNull(plan.getId());

        User user = getUserDAO().createUser("Henrik", "Hillary", "ROLE_STUDENT");

        AssignedPedagogicalPlan assignedPedagogicalPlan = getAssignedAssessmentDAO().assignPedagogicalPlanToUser(plan, user);
        assertNotNull(assignedPedagogicalPlan);
        assertNotNull(assignedPedagogicalPlan.getUser());
        assertNotNull(assignedPedagogicalPlan.getPedagogicalPlan());

    }

    public void testGetAssignedPedagogicalPlansForUser() {

        PedagogicalPlan plan1 = createPedagogicalPlan("Plan1");
        PedagogicalPlan plan2 = createPedagogicalPlan("Plan2");

        User user = getUserDAO().createUser("Henrik", "Hillary", "ROLE_STUDENT");

        AssignedPedagogicalPlan assignedPedagogicalPlan = getAssignedAssessmentDAO().assignPedagogicalPlanToUser(plan1, user);
        assertTrue(plan1.getId() != null);
        assertTrue(user.getId() > -1);
        assertTrue(assignedPedagogicalPlan.getId() != null);
        List <AssignedPedagogicalPlan> assignedPedagogicalPlans = getAssignedAssessmentDAO().getAssignedPedagogicalPlans(user);
        assertNotNull(assignedPedagogicalPlans);
        assertNotNull(plan1);
        assert(assignedPedagogicalPlans.size() > 0);
        



    }

    private PedagogicalPlan createPedagogicalPlan (String name) {
        PedagogicalPlanTemplate template = createPedagogicalPlanTemplate("A ped plan");
        PedagogicalPlan plan = createPedagogicalPlan(template);
        return plan;
    }







}
