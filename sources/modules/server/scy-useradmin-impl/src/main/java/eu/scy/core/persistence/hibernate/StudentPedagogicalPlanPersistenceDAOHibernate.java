package eu.scy.core.persistence.hibernate;

import eu.scy.core.model.User;
import eu.scy.core.model.impl.student.StudentPlanELOImpl;
import eu.scy.core.model.impl.student.StudentPlannedActivityImpl;
import eu.scy.core.model.pedagogicalplan.*;
import eu.scy.core.model.student.StudentPlanELO;
import eu.scy.core.model.student.StudentPlannedActivity;
import eu.scy.core.persistence.StudentPedagogicalPlanPersistenceDAO;
import org.apache.log4j.Logger;
import roolo.api.IRepository;
import roolo.elo.JDomBasicELOFactory;

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

    @Override
    public void addMemberToStudentPlannedActivity(User member, StudentPlannedActivity studentPlannedActivity) {
        if(member == null || studentPlannedActivity == null) {
            log.warn("TRIED TO ADD MEMBER " + member + " TO ACTIVITY " + studentPlannedActivity + "..... NULL....");
            return;
        }

        studentPlannedActivity = (StudentPlannedActivity) getHibernateTemplate().load(StudentPlannedActivityImpl.class, studentPlannedActivity.getId());

        getSession().refresh(studentPlannedActivity);
        getSession().refresh(member);
        
        studentPlannedActivity.addMember(member);
        save(studentPlannedActivity);
    }

    @Override
    public void addMemberToStudentPlannedActivity(String user, StudentPlannedActivity studentPlannedActivity) {
        User realUser = getUserByUsername(user);
        if(realUser != null) {
            addMemberToStudentPlannedActivity(realUser, studentPlannedActivity);    
        }

    }

    @Override
    public void removeStudentPlannedActivityFromStudentPlan(StudentPlannedActivity studentPlannedActivity, StudentPlanELO studentPlanELO) {
        getHibernateTemplate().refresh(studentPlanELO);
        getHibernateTemplate().refresh(studentPlannedActivity);
        studentPlanELO.getStudentPlannedActivities().remove(studentPlannedActivity);
        save(studentPlanELO);
    }

    @Override
    public StudentPlannedActivity getStudentPlannedActivity(String anchorELOId, String userName) {
        User user = getUserByUsername(userName);
        if(user != null) {
            return (StudentPlannedActivity) getSession().createQuery("from StudentPlannedActivityImpl as spa where spa.assoicatedELO.missionMapId like :anchorELOId and spa.studentPlan.user = :user ")
                            .setString("anchorELOId", anchorELOId)
                            .setEntity("user", user)
                            .uniqueResult();

        }

        log.warn("DID NOT FIND A MATCHING STUDENT PLANNED ACTIVITY");
        return null;

    }

    @Override
    public StudentPlanELO getStudentPlanELOBasedOnELOId(String eloId) {
        log.info("I AM TOTALLY NOT IMPLEMENTED YET!!");
        log.info("HERE IS THE ELO ID:" + eloId);
        return null;
    }

    @Override
    public void removeMember(StudentPlannedActivity studentPlannedActivity, String userName) {
        getHibernateTemplate().refresh(studentPlannedActivity);
        User user = getUserByUsername(userName);
        studentPlannedActivity.getMembers().remove(user);
        save(studentPlannedActivity);
    }

    @Override
    public StudentPlanELO getStudentPlanElo(String id) {
        return (StudentPlanELO) getSession().createQuery("from StudentPlanELOImpl where id like :id")
                .setString("id", id)
                .uniqueResult();

    }

    public StudentPlanELO createStudentPlan(String username) {
        User user = getUserByUsername(username);
        if(user != null) {
            PedagogicalPlan pedagogicalPlan = (PedagogicalPlan) getSession().createQuery("from PedagogicalPlanImpl where published = :published")
                    .setBoolean("published", true)
                    .setMaxResults(0)
                    .uniqueResult();
        }

        return null;
    }
}
