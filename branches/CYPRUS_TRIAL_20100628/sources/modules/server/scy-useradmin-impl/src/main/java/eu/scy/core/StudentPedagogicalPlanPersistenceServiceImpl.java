package eu.scy.core;

import eu.scy.core.model.User;
import eu.scy.core.model.pedagogicalplan.PedagogicalPlan;
import eu.scy.core.model.student.StudentPlanELO;
import eu.scy.core.model.student.StudentPlannedActivity;
import eu.scy.core.persistence.hibernate.StudentPedagogicalPlanPersistenceDAOHibernate;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 12.jan.2010
 * Time: 13:28:09
 * To change this template use File | Settings | File Templates.
 */
public class StudentPedagogicalPlanPersistenceServiceImpl extends BaseServiceImpl implements StudentPedagogicalPlanPersistenceService{

    private StudentPedagogicalPlanPersistenceDAOHibernate studentPedagogicalPlanPersistenceDAO;

    public StudentPedagogicalPlanPersistenceDAOHibernate getStudentPedagogicalPlanPersistenceDAO() {
        return (StudentPedagogicalPlanPersistenceDAOHibernate) getScyBaseDAO();
    }

    public void setStudentPedagogicalPlanPersistenceDAO(StudentPedagogicalPlanPersistenceDAOHibernate studentPedagogicalPlanPersistenceDAO) {
        this.studentPedagogicalPlanPersistenceDAO = studentPedagogicalPlanPersistenceDAO;
    }


    @Transactional
    @Override
    public StudentPlanELO createStudentPlan(PedagogicalPlan pedagogicalPlan, User user) {
        return getStudentPedagogicalPlanPersistenceDAO().createStudentPlan(pedagogicalPlan, user);
    }

    @Override
    public List getStudentPlans(User user) {
        return getStudentPedagogicalPlanPersistenceDAO().getStudentPlans(user);
    }

    @Override
    public List<StudentPlanELO> getStudentPlans(String username) {
        return getStudentPedagogicalPlanPersistenceDAO().getStudentPlans(username);
    }


    @Override
    public StudentPlanELO getStudentPlanElo(String id) {
        return getStudentPedagogicalPlanPersistenceDAO().getStudentPlanElo(id);
    }

    @Override
    public void addMemberToStudentPlannedActivity(User member, StudentPlannedActivity studentPlannedActivity){
        getStudentPedagogicalPlanPersistenceDAO().addMemberToStudentPlannedActivity(member, studentPlannedActivity);

    }

    @Override
    public void addMemberToStudentPlannedActivity(String user, StudentPlannedActivity studentPlannedActivity) {
        getStudentPedagogicalPlanPersistenceDAO().addMemberToStudentPlannedActivity(user, studentPlannedActivity);
    }

    @Override
    public void removeStudentPlannedActivityFromStudentPlan(StudentPlannedActivity studentPlannedActivity, StudentPlanELO studentPlanELO) {
        getStudentPedagogicalPlanPersistenceDAO().removeStudentPlannedActivityFromStudentPlan(studentPlannedActivity, studentPlanELO);
    }

    @Override
    public StudentPlannedActivity getStudentPlannedActivity(String userName, String achorELOId, String studentPlanId) {
        return getStudentPedagogicalPlanPersistenceDAO().getStudentPlannedActivity(achorELOId, userName, studentPlanId);
    }

    @Override
    public StudentPlanELO getStudentPlanELOBasedOnELOId(String eloId) {
        return getStudentPedagogicalPlanPersistenceDAO().getStudentPlanELOBasedOnELOId(eloId);
    }

    @Override
    public void removeMember(StudentPlannedActivity studentPlannedActivity, String user) {
        getStudentPedagogicalPlanPersistenceDAO().removeMember(studentPlannedActivity, user);
    }

    @Transactional
    @Override
    public StudentPlanELO createStudentPlan(String username) {
        return getStudentPedagogicalPlanPersistenceDAO().createStudentPlan(username);
    }
}
