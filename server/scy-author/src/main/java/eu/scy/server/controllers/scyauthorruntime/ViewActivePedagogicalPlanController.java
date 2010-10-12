package eu.scy.server.controllers.scyauthorruntime;

import eu.scy.core.AssignedPedagogicalPlanService;
import eu.scy.core.PedagogicalPlanPersistenceService;
import eu.scy.core.StudentPedagogicalPlanPersistenceService;
import eu.scy.core.model.pedagogicalplan.AssignedPedagogicalPlan;
import eu.scy.core.model.pedagogicalplan.PedagogicalPlan;
import eu.scy.core.model.student.StudentPlanELO;
import eu.scy.server.controllers.BaseController;
import java.util.LinkedList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 30.mar.2010
 * Time: 08:20:16
 */
public class ViewActivePedagogicalPlanController extends BaseController {

    private PedagogicalPlanPersistenceService pedagogicalPlanPersistenceService = null;
    private AssignedPedagogicalPlanService assignedPedagogicalPlanService = null;
    private StudentPedagogicalPlanPersistenceService studentPedagogicalPlanPersistenceService = null;

    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
        String pedPlanId = request.getParameter("id");
        PedagogicalPlan pedagogicalPlan = getPedagogicalPlanPersistenceService().getPedagogicalPlan(pedPlanId);
        setModel(pedagogicalPlan);

        List<AssignedPedagogicalPlan> assignedList = getAssignedPedagogicalPlanService().getAssignedPedagogicalPlans(pedagogicalPlan);
        List<DataClass> data = new LinkedList<DataClass>();

        for (AssignedPedagogicalPlan plan : assignedList) {
            data.add(new DataClass(plan, getStudentPedagogicalPlanPersistenceService().getStudentPlans(plan.getUser())));
        }

        modelAndView.addObject("assignedPedagogicalPlans", data);

    }

    public PedagogicalPlanPersistenceService getPedagogicalPlanPersistenceService() {
        return pedagogicalPlanPersistenceService;
    }

    public void setPedagogicalPlanPersistenceService(PedagogicalPlanPersistenceService pedagogicalPlanPersistenceService) {
        this.pedagogicalPlanPersistenceService = pedagogicalPlanPersistenceService;
    }

    public AssignedPedagogicalPlanService getAssignedPedagogicalPlanService() {
        return assignedPedagogicalPlanService;
    }

    public void setAssignedPedagogicalPlanService(AssignedPedagogicalPlanService assignedPedagogicalPlanService) {
        this.assignedPedagogicalPlanService = assignedPedagogicalPlanService;
    }

    public StudentPedagogicalPlanPersistenceService getStudentPedagogicalPlanPersistenceService() {
        return studentPedagogicalPlanPersistenceService;
    }

    public void setStudentPedagogicalPlanPersistenceService(StudentPedagogicalPlanPersistenceService studentPedagogicalPlanPersistenceService) {
        this.studentPedagogicalPlanPersistenceService = studentPedagogicalPlanPersistenceService;
    }

    public class DataClass {
        private List<StudentPlanELO> studentPlans = null;
        private AssignedPedagogicalPlan assignedPedagogicalPlan = null;

        public DataClass(AssignedPedagogicalPlan assignedPedagogicalPlan, List<StudentPlanELO> studentPlans) {
            this.assignedPedagogicalPlan = assignedPedagogicalPlan;
            this.studentPlans = studentPlans;
        }

        public AssignedPedagogicalPlan getAssignedPedagogicalPlan() {
            return assignedPedagogicalPlan;
        }

        public List<StudentPlanELO> getStudentPlans() {
            return studentPlans;
        }
    }
}
