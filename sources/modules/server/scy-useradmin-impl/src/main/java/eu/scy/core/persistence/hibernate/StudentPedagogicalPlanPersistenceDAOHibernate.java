package eu.scy.core.persistence.hibernate;

import eu.scy.core.model.User;
import eu.scy.core.model.impl.student.StudentPlanELOImpl;
import eu.scy.core.model.pedagogicalplan.Activity;
import eu.scy.core.model.pedagogicalplan.LearningActivitySpace;
import eu.scy.core.model.pedagogicalplan.PedagogicalPlan;
import eu.scy.core.model.pedagogicalplan.Scenario;
import eu.scy.core.model.student.StudentPlanELO;
import eu.scy.core.persistence.StudentPedagogicalPlanPersistenceDAO;

import java.util.Collections;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 12.jan.2010
 * Time: 12:05:10
 */
public class StudentPedagogicalPlanPersistenceDAOHibernate extends ScyBaseDAOHibernate implements StudentPedagogicalPlanPersistenceDAO {

    /**
     * sets up a student plan and assigns it to the student based on the pedagogical plan
     * @param student
     * @param pedagogicalPlan
     */
    public StudentPlanELO assignStudentPlanToStudent(User student, PedagogicalPlan pedagogicalPlan) {
        StudentPlanELO studentPlan = createStudentPlan(pedagogicalPlan,student );
        List <Activity> activities = getActivities(pedagogicalPlan);
        return null;
    }

    private List<Activity> getActivities(PedagogicalPlan pedagogicalPlan) {
        List <LearningActivitySpace> lass = getLearningActivitySpaces(pedagogicalPlan);
        return Collections.EMPTY_LIST;//getSession().createQuery("From ActivityImpl where learningActivitySpace = :las")
                //.setEntity("las", learningActivitySpace)
    }

    private List<LearningActivitySpace> getLearningActivitySpaces(PedagogicalPlan pedagogicalPlan) {
        Scenario scenario = pedagogicalPlan.getScenario();
        LearningActivitySpace las = scenario.getLearningActivitySpace();
        List Activities = las.getActivities();
        return null;
        
    }

    public StudentPlanELO createStudentPlan(PedagogicalPlan pedagogicalPlan, User user) {
        assert(pedagogicalPlan != null);
        assert(user != null);
        StudentPlanELOImpl plan = new StudentPlanELOImpl();
        plan.setPedagogicalPlan(pedagogicalPlan);
        plan.setUser(user);
        save(plan);
        return plan;
    }

    public List getStudentPlans(User user) {
        return getSession().createQuery("from StudentPlanELOImpl where user = :user")
                .setEntity("user", user)
                .list();
    }
}
