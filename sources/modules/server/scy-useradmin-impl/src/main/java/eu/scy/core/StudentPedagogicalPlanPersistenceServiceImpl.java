package eu.scy.core;

import eu.scy.core.model.User;
import eu.scy.core.model.pedagogicalplan.PedagogicalPlan;
import eu.scy.core.model.student.StudentPlanELO;
import eu.scy.core.model.student.StudentPlannedActivity;
import eu.scy.core.persistence.hibernate.StudentPedagogicalPlanPersistenceDAOHibernate;

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
    public StudentPlannedActivity getStudentPlannedActivity(String achorELOId) {
        return getStudentPedagogicalPlanPersistenceDAO().getStudentPlannedActivity(achorELOId);
    }

    @Override
    public StudentPlanELO getStudentPlanELO(String eloId) {
        return getStudentPedagogicalPlanPersistenceDAO().getStudentPlanELO(eloId);
    }

    @Override
    public void removeMember(StudentPlannedActivity studentPlannedActivity, String user) {
        getStudentPedagogicalPlanPersistenceDAO().removeMember(studentPlannedActivity, user);
    }
}
