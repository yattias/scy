package eu.scy.server.controllers.scyauthorruntime;

import eu.scy.core.PedagogicalPlanPersistenceService;
import eu.scy.core.StudentPedagogicalPlanPersistenceService;
import eu.scy.server.controllers.BaseController;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by IntelliJ IDEA.
 * User: Oyvind
 * Date: 8.apr.2010
 * Time: 08:20:16
 */
public class ViewStudentPlanController extends BaseController {
    private StudentPedagogicalPlanPersistenceService studentPedagogicalPlanPersistenceService = null;
    private PedagogicalPlanPersistenceService pedagogicalPlanPersistenceService = null;

    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
        String studentPlanId = request.getParameter("studentPlanId");
        if (studentPlanId != null) {
            modelAndView.addObject("studentPlan", getStudentPedagogicalPlanPersistenceService().getStudentPlanElo(studentPlanId));
        }
    }

    public PedagogicalPlanPersistenceService getPedagogicalPlanPersistenceService() {
        return pedagogicalPlanPersistenceService;
    }

    public void setPedagogicalPlanPersistenceService(PedagogicalPlanPersistenceService pedagogicalPlanPersistenceService) {
        this.pedagogicalPlanPersistenceService = pedagogicalPlanPersistenceService;
    }

    public StudentPedagogicalPlanPersistenceService getStudentPedagogicalPlanPersistenceService() {

        return studentPedagogicalPlanPersistenceService;
    }

    public void setStudentPedagogicalPlanPersistenceService(StudentPedagogicalPlanPersistenceService studentPedagogicalPlanPersistenceService) {
        this.studentPedagogicalPlanPersistenceService = studentPedagogicalPlanPersistenceService;
    }
}