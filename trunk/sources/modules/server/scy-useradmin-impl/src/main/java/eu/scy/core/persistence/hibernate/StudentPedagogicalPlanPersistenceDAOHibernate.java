package eu.scy.core.persistence.hibernate;

import eu.scy.core.model.User;
import eu.scy.core.model.impl.student.StudentPlanELOImpl;
import eu.scy.core.model.impl.student.StudentPlannedActivityImpl;
import eu.scy.core.model.pedagogicalplan.*;
import eu.scy.core.model.student.StudentPlanELO;
import eu.scy.core.model.student.StudentPlannedActivity;
import eu.scy.core.persistence.StudentPedagogicalPlanPersistenceDAO;
import org.apache.log4j.Logger;

import java.util.Collections;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 12.jan.2010
 * Time: 12:05:10
 */
public class StudentPedagogicalPlanPersistenceDAOHibernate extends ScyBaseDAOHibernate implements StudentPedagogicalPlanPersistenceDAO {

    private static Logger log = Logger.getLogger("StudentPedagogicalPlanPersistenceDAOHibernate.class");

    /**
     * sets up a student plan and assigns it to the student based on the pedagogical plan
     *
     * @param student
     * @param pedagogicalPlan
     */
    public StudentPlanELO createStudentPlan(PedagogicalPlan pedagogicalPlan, User student) {
        assert (pedagogicalPlan != null);
        assert (student != null);
        save(student);
        StudentPlanELO plan = new StudentPlanELOImpl();
        plan.setPedagogicalPlan(pedagogicalPlan);
        plan.setUser(student);
        save(plan);

        plan = assignStudentPlanToStudent(plan, pedagogicalPlan);
        return plan;
    }


    private StudentPlanELO assignStudentPlanToStudent(StudentPlanELO studentPlan, PedagogicalPlan pedagogicalPlan) {
        List<Activity> activities = getActivities(pedagogicalPlan);
        for (int i = 0; i < activities.size(); i++) {
            Activity activity = activities.get(i);
            StudentPlannedActivity plannedActivity = new StudentPlannedActivityImpl();
            //plannedActivity = (StudentPlannedActivity) save(plannedActivity);
            plannedActivity.setName(activity.getName());
            plannedActivity.setDescription(activity.getDescription());
            AnchorELO anchorElo = activity.getAnchorELO();
            anchorElo = (AnchorELO) getHibernateTemplate().merge(anchorElo);
            plannedActivity.setStudentPlan(studentPlan);
            plannedActivity.setAssoicatedELO(activity.getAnchorELO());
            studentPlan.addStudentPlannedActivity(plannedActivity);
            getHibernateTemplate().saveOrUpdate(plannedActivity);
            getHibernateTemplate().saveOrUpdate(studentPlan);
        }

        save(studentPlan);

        return studentPlan;
    }

    private List<Activity> getActivities(PedagogicalPlan pedagogicalPlan) {
        List<LearningActivitySpace> lass = getLearningActivitySpaces(pedagogicalPlan);
        return getSession().createQuery("from ActivityImpl as activity where activity.learningActivitySpace in (:lass)")
                .setParameterList("lass", lass)
                .list();
    }

    private List<LearningActivitySpace> getLearningActivitySpaces(PedagogicalPlan pedagogicalPlan) {
        Scenario scenario = pedagogicalPlan.getScenario();
        return getSession().createQuery("from LearningActivitySpaceImpl as las where las.participatesIn = :scenario")
                .setEntity("scenario", scenario)
                .list();

    }

    public List getStudentPlans(User user) {
        List plans = getSession().createQuery("from StudentPlanELOImpl where user = :user")
                .setEntity("user", user)
                .list();

        for (int i = 0; i < plans.size(); i++) {
            StudentPlanELOImpl studentPlanELO = (StudentPlanELOImpl) plans.get(i);
            List activities = studentPlanELO.getStudentPlannedActivities();
            for (int j = 0; j < activities.size(); j++) {
                StudentPlannedActivityImpl studentPlannedActivity = (StudentPlannedActivityImpl) activities.get(j);
                log.info(studentPlannedActivity.getName());
                eagerLoad(studentPlannedActivity.getAssoicatedELO());
            }
        }

        return plans;

    }

    private void eagerLoad(AnchorELO assoicatedELO) {
        if (assoicatedELO != null) {
            assoicatedELO.getProducedBy();
            eagerLoadActivity(assoicatedELO.getProducedBy());
        }

    }

    private void eagerLoadActivity(Activity producedBy) {
        if (producedBy != null) {
            eagerLoadLAS(producedBy.getLearningActivitySpace());
        }

    }

    private void eagerLoadLAS(LearningActivitySpace learningActivitySpace) {
        if (learningActivitySpace != null) {
            log.info(learningActivitySpace.getName());
        }

    }

    public List<StudentPlanELO> getStudentPlans(String username) {
        log.info("Loading plans for " + username);
        User user = getUserByUsername(username);
        log.info("Found user: " + user.getUserDetails().getUsername());
        return getStudentPlans(user);
    }

    public User getUserByUsername(String username) {
        User user = (User) getSession().createQuery("from SCYUserImpl user where user.userDetails.username like :username")
                .setString("username", username)
                .uniqueResult();
        return user;
    }

}
