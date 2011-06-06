package eu.scy.server.pedagogicalplan;

import eu.scy.core.model.User;
import eu.scy.core.model.impl.ScyBaseObject;
import eu.scy.core.model.pedagogicalplan.PedagogicalPlan;
import eu.scy.core.model.student.StudentPlanELO;
import eu.scy.core.model.student.StudentPlannedActivity;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 12.jan.2010
 * Time: 11:51:16
 * This one will be exposed by remoting technology
 */
public interface StudentPedagogicalPlanService {

    public StudentPlanELO createStudentPlan(String username);

    public StudentPlanELO createStudentPlan(PedagogicalPlan pedagogicalPlan, User user);

    List<StudentPlanELO> getStudentPlans(User user);

    List<StudentPlanELO> getStudentPlans(String username);

    public void save(ScyBaseObject scyBase);

    public void addMember(StudentPlannedActivity studentPlannedActivity, String user);

    public void removeMember(StudentPlannedActivity studentPlannedActivity, String user);

    public void removeStudentPlannedActivityFromStudentPlan(StudentPlannedActivity studentPlannedActivity, StudentPlanELO studentPlanELO);

    /**
     * will retrieve an already existing student planned activity, or create a new one
     * @param userName
     * @param achorELOId  @return
     * @param studentPlanId
     */
    StudentPlannedActivity getStudentPlannedActivity(String userName, String achorELOId, String studentPlanId);

    StudentPlanELO getStudentPlanELO(String eloId);

    StudentPlanELO getCurrentStudentPlanELO();

    void setCurrentStudentPlanELO(StudentPlanELO currentStudentPlanELO);
}
