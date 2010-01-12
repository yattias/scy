package eu.scy.server.pedagogicalplan;

import eu.scy.core.StudentPedagogicalPlanPersistenceService;
import eu.scy.core.model.User;
import eu.scy.core.model.pedagogicalplan.PedagogicalPlan;
import eu.scy.core.model.student.StudentPlanELO;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 11.jan.2010
 * Time: 12:29:02
 * To change this template use File | Settings | File Templates.
 */
public class StudentPedagogicalPlanServiceImpl extends AbstractPedagogicalPlanService implements StudentPedagogicalPlanService{

    private StudentPedagogicalPlanPersistenceService studentPedagogicalPlanPersistenceService;

    public StudentPedagogicalPlanPersistenceService getStudentPedagogicalPlanPersistenceService() {
        return studentPedagogicalPlanPersistenceService;
    }

    public void setStudentPedagogicalPlanPersistenceService(StudentPedagogicalPlanPersistenceService studentPedagogicalPlanPersistenceService) {
        this.studentPedagogicalPlanPersistenceService = studentPedagogicalPlanPersistenceService;
    }

    public StudentPlanELO createStudentPlan(PedagogicalPlan pedagogicalPlan, User user) {
        return getStudentPedagogicalPlanPersistenceService().createStudentPlan(pedagogicalPlan, user);
    }
}
