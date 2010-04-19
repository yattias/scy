package eu.scy.core;

import eu.scy.core.model.User;
import eu.scy.core.model.pedagogicalplan.PedagogicalPlan;
import eu.scy.core.model.student.StudentPlanELO;
import eu.scy.core.model.student.StudentPlannedActivity;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 12.jan.2010
 * Time: 11:55:12
 * To change this template use File | Settings | File Templates.
 */
public interface StudentPedagogicalPlanPersistenceService extends BaseService{

    public StudentPlanELO createStudentPlan(PedagogicalPlan pedagogicalPlan, User user);

    public List getStudentPlans(User user);

    List<StudentPlanELO> getStudentPlans(String username);

    public StudentPlanELO getStudentPlanElo(String id);

    void addMemberToStudentPlannedActivity(User member, StudentPlannedActivity studentPlannedActivity);

    void addMemberToStudentPlannedActivity(String user, StudentPlannedActivity studentPlannedActivity);

    void removeStudentPlannedActivityFromStudentPlan(StudentPlannedActivity studentPlannedActivity, StudentPlanELO studentPlanELO);

    StudentPlannedActivity getStudentPlannedActivity(String userName, String achorELOId, String studentPlanId);

    StudentPlanELO getStudentPlanELOBasedOnELOId(String eloId);

    void removeMember(StudentPlannedActivity studentPlannedActivity, String user);

    StudentPlanELO createStudentPlan(String username);
}
