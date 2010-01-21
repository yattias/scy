package eu.scy.core;

import eu.scy.core.model.User;
import eu.scy.core.model.pedagogicalplan.PedagogicalPlan;
import eu.scy.core.model.student.StudentPlanELO;
import eu.scy.core.persistence.hibernate.StudentPedagogicalPlanPersistenceDAOHibernate;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 12.jan.2010
 * Time: 13:28:09
 * To change this template use File | Settings | File Templates.
 */
public class StudentPedagogicalPlanPersistenceServiceImpl implements StudentPedagogicalPlanPersistenceService{

    private StudentPedagogicalPlanPersistenceDAOHibernate studentPedagogicalPlanPersistenceDAO;

    public StudentPedagogicalPlanPersistenceDAOHibernate getStudentPedagogicalPlanPersistenceDAO() {
        return studentPedagogicalPlanPersistenceDAO;
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
}
