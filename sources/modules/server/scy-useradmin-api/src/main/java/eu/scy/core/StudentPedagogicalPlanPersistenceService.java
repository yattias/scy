package eu.scy.core;

import eu.scy.core.model.User;
import eu.scy.core.model.pedagogicalplan.PedagogicalPlan;
import eu.scy.core.model.student.StudentPlanELO;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 12.jan.2010
 * Time: 11:55:12
 * To change this template use File | Settings | File Templates.
 */
public interface StudentPedagogicalPlanPersistenceService {

    public StudentPlanELO assignStudentPlanToStudent(User student, PedagogicalPlan pedagogicalPlan);

    public StudentPlanELO createStudentPlan(PedagogicalPlan pedagogicalPlan, User user);

    public List getStudentPlans(User user);
}
