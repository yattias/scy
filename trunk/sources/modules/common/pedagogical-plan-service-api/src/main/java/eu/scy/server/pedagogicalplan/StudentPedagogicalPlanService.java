package eu.scy.server.pedagogicalplan;

import eu.scy.core.model.User;
import eu.scy.core.model.pedagogicalplan.PedagogicalPlan;
import eu.scy.core.model.student.StudentPlanELO;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 12.jan.2010
 * Time: 11:51:16
 * This one will be exposed by remoting technology
 */
public interface StudentPedagogicalPlanService {

    public StudentPlanELO createStudentPlan(PedagogicalPlan pedagogicalPlan, User user);

    List<StudentPlanELO> getStudentPlans(User user);

    List<StudentPlanELO> getStudentPlans(String username);
}
