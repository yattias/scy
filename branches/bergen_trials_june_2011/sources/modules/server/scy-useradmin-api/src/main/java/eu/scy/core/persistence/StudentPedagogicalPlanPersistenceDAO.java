package eu.scy.core.persistence;

import eu.scy.core.StudentPedagogicalPlanPersistenceService;
import eu.scy.core.model.User;
import eu.scy.core.model.impl.student.StudentPlanELOImpl;
import eu.scy.core.model.pedagogicalplan.PedagogicalPlan;
import eu.scy.core.model.student.StudentPlanELO;
import eu.scy.core.model.student.StudentPlannedActivity;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 12.jan.2010
 * Time: 11:56:01
 * To change this template use File | Settings | File Templates.
 */
public interface StudentPedagogicalPlanPersistenceDAO extends SCYBaseDAO{

    public StudentPlanELO createStudentPlan(PedagogicalPlan pedagogicalPlan, User user) ;

    void addMemberToStudentPlannedActivity(User member, StudentPlannedActivity studentPlannedActivity);

    void removeStudentPlannedActivityFromStudentPlan(StudentPlannedActivity studentPlannedActivity, StudentPlanELO studentPlanELO);

    void addMemberToStudentPlannedActivity(String user, StudentPlannedActivity studentPlannedActivity);

    StudentPlannedActivity getStudentPlannedActivity(String achorELOId, String userName, String studentPlanId);

    StudentPlanELO getStudentPlanELOBasedOnELOId(String eloId);

    void removeMember(StudentPlannedActivity studentPlannedActivity, String userName);

    StudentPlanELO getStudentPlanElo(String id);
}
