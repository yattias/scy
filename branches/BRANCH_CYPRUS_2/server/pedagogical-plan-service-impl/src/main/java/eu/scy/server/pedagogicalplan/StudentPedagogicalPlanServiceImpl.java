package eu.scy.server.pedagogicalplan;

import eu.scy.core.StudentPedagogicalPlanPersistenceService;
import eu.scy.core.model.User;
import eu.scy.core.model.impl.ScyBaseObject;
import eu.scy.core.model.pedagogicalplan.PedagogicalPlan;
import eu.scy.core.model.student.StudentPlanELO;
import eu.scy.core.model.student.StudentPlannedActivity;

import java.util.List;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 11.jan.2010
 * Time: 12:29:02
 * To change this template use File | Settings | File Templates.
 */
public class StudentPedagogicalPlanServiceImpl extends AbstractPedagogicalPlanService implements StudentPedagogicalPlanService{

    private static Logger log = Logger.getLogger("StudentPedagogicalPlanServiceImpl.class");

    private StudentPedagogicalPlanPersistenceService studentPedagogicalPlanPersistenceService;



    public StudentPedagogicalPlanPersistenceService getStudentPedagogicalPlanPersistenceService() {
        return studentPedagogicalPlanPersistenceService;
    }

    public void setStudentPedagogicalPlanPersistenceService(StudentPedagogicalPlanPersistenceService studentPedagogicalPlanPersistenceService) {
        this.studentPedagogicalPlanPersistenceService = studentPedagogicalPlanPersistenceService;
    }

    @Override
    public StudentPlanELO createStudentPlan(String username) {
        return getStudentPedagogicalPlanPersistenceService().createStudentPlan(username);
    }

    public StudentPlanELO createStudentPlan(PedagogicalPlan pedagogicalPlan, User user) {
        return getStudentPedagogicalPlanPersistenceService().createStudentPlan(pedagogicalPlan, user);
    }

    @Override
    public List<StudentPlanELO> getStudentPlans(User user) {
        return getStudentPedagogicalPlanPersistenceService().getStudentPlans(user);

    }

    @Override
    public List<StudentPlanELO> getStudentPlans(String username) {
        log.info("Getting student plans for user: " + username);
        return getStudentPedagogicalPlanPersistenceService().getStudentPlans(username);
    }

    @Override
    public void addMember(StudentPlannedActivity studentPlannedActivity, String user) {
        getStudentPedagogicalPlanPersistenceService().addMemberToStudentPlannedActivity(user, studentPlannedActivity);
    }

    @Override
    public void removeMember(StudentPlannedActivity studentPlannedActivity, String user) {
        getStudentPedagogicalPlanPersistenceService().removeMember(studentPlannedActivity, user);
    }

    @Override
    public void removeStudentPlannedActivityFromStudentPlan(StudentPlannedActivity studentPlannedActivity, StudentPlanELO studentPlanELO) {
        getStudentPedagogicalPlanPersistenceService().removeStudentPlannedActivityFromStudentPlan(studentPlannedActivity, studentPlanELO);
    }

    @Override
    public void save(ScyBaseObject scyBase) {
        if(scyBase instanceof StudentPlannedActivity) {
            log.info(((StudentPlannedActivity)scyBase).getNote());
        }
        getStudentPedagogicalPlanPersistenceService().save(scyBase);
    }

    @Override
    public StudentPlannedActivity getStudentPlannedActivity(String userName, String achorELOId, String studentPlanId) {
        return getStudentPedagogicalPlanPersistenceService().getStudentPlannedActivity(userName, achorELOId, studentPlanId);
    }

    @Override
    public StudentPlanELO getStudentPlanELO(String eloId){
        return getStudentPedagogicalPlanPersistenceService().getStudentPlanELOBasedOnELOId(eloId);
    }

    @Override
    public StudentPlanELO getCurrentStudentPlanELO() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setCurrentStudentPlanELO(StudentPlanELO currentStudentPlanELO) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
