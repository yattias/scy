package eu.scy.core.persistence.hibernate;

import eu.scy.core.model.User;
import eu.scy.core.model.impl.SCYUserImpl;
import eu.scy.core.model.impl.pedagogicalplan.PedagogicalPlanImpl;
import eu.scy.core.model.impl.student.StudentPlanELOImpl;
import eu.scy.core.model.impl.student.StudentPlannedActivityImpl;
import eu.scy.core.model.pedagogicalplan.*;
import eu.scy.core.model.student.StudentPlanELO;
import eu.scy.core.model.student.StudentPlannedActivity;
import eu.scy.core.persistence.StudentPedagogicalPlanPersistenceDAO;
import org.apache.log4j.Logger;

import java.sql.Date;
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
        //save(student);
        student = (User) getHibernateTemplate().load(SCYUserImpl.class, student.getId());
        pedagogicalPlan = (PedagogicalPlan) getHibernateTemplate().load(PedagogicalPlanImpl.class, pedagogicalPlan.getId());
        StudentPlanELO plan = new StudentPlanELOImpl();
        plan.setPedagogicalPlan(pedagogicalPlan);
        plan.setUser(student);
        save(plan);
        plan = assignStudentPlanToStudent(plan, pedagogicalPlan);
        return plan;
    }


    private StudentPlanELO assignStudentPlanToStudent(StudentPlanELO studentPlan, PedagogicalPlan pedagogicalPlan) {
        addActivities(studentPlan, pedagogicalPlan);

        save(studentPlan);

        return studentPlan;
    }

    private void addActivities(StudentPlanELO studentPlan, PedagogicalPlan pedagogicalPlan) {
        pedagogicalPlan = (PedagogicalPlan) getHibernateTemplate().merge(pedagogicalPlan);
        log.info("Adding activities go plan: " + pedagogicalPlan);
        List<Activity> activities = getActivities(pedagogicalPlan);
        for (int i = 0; i < activities.size(); i++) {
            Activity activity = (Activity) getHibernateTemplate().merge(activities.get(i));
            if (activity.getAutoaddToStudentPlan()) {
                StudentPlannedActivity plannedActivity = new StudentPlannedActivityImpl();
                plannedActivity.setName(activity.getName());
                plannedActivity.setDescription(activity.getDescription());
                AnchorELO anchorElo = activity.getAnchorELO();
                anchorElo = (AnchorELO) getHibernateTemplate().merge(anchorElo);
                plannedActivity.setStudentPlan(studentPlan);
                plannedActivity.setAssoicatedELO(activity.getAnchorELO());

                if (activity != null) {
                    if (activity.getStartDate() != null) {
                        long startDate = activity.getStartDate().getTime();
                        plannedActivity.setStartDate(new Date(startDate));
                        log.info("Created start date from studentPlan");
                    } else {
                        log.info("No start date found");
                    }
                    if (activity.getEndDate() != null) {
                        long endDL = activity.getEndDate().getTime();
                        plannedActivity.setEndDate(new Date(endDL));
                        log.info("Created end date from studentplan");
                    } else {
                        log.info("No end date found!");
                    }


                }


                studentPlan.addStudentPlannedActivity(plannedActivity);
                getHibernateTemplate().saveOrUpdate(plannedActivity);
                getHibernateTemplate().saveOrUpdate(studentPlan);
            }
        }
    }

    private List<Activity> getActivities(PedagogicalPlan pedagogicalPlan) {
        pedagogicalPlan = (PedagogicalPlan) getHibernateTemplate().merge(pedagogicalPlan);
        List<LearningActivitySpace> lass = getLearningActivitySpaces(pedagogicalPlan);
        return getSession().createQuery("from ActivityImpl as activity where activity.learningActivitySpace in (:lass)")
                .setParameterList("lass", lass)
                .list();
    }

    private List<LearningActivitySpace> getLearningActivitySpaces(PedagogicalPlan pedagogicalPlan) {
        pedagogicalPlan = (PedagogicalPlan) getHibernateTemplate().merge(pedagogicalPlan);
        log.info("GOT PED PLAN: " + pedagogicalPlan);
        Scenario scenario = (Scenario) getHibernateTemplate().merge(pedagogicalPlan.getScenario());
        log.info("GOT SCENARIO: " + scenario);
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
        username = trimUsername(username);
        log.info("Loading plans for " + username);
        User user = getUserByUsername(username);
        log.info("Found user: " + user.getUserDetails().getUsername());
        return getStudentPlans(user);
    }

    public User getUserByUsername(String username) {
        username = trimUsername(username);
        User user = (User) getSession().createQuery("from SCYUserImpl user where user.userDetails.username like :username")
                .setString("username", username)
                .uniqueResult();
        return user;
    }

    @Override
    public void addMemberToStudentPlannedActivity(User member, StudentPlannedActivity studentPlannedActivity) {
        if (member == null || studentPlannedActivity == null) {
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
        user = trimUsername(user);
        User realUser = getUserByUsername(user);
        if (realUser != null) {
            addMemberToStudentPlannedActivity(realUser, studentPlannedActivity);
        }

    }

    @Override
    public void removeStudentPlannedActivityFromStudentPlan(StudentPlannedActivity studentPlannedActivity, StudentPlanELO studentPlanELO) {
        getHibernateTemplate().refresh(studentPlanELO);
        getHibernateTemplate().refresh(studentPlannedActivity);

        log.info("REMOVING : " + studentPlannedActivity.getName() + " from " + studentPlannedActivity.getStudentPlan().getName());

        //studentPlannedActivity.setStudentPlan(null);
        studentPlannedActivity.getStudentPlan().removeStudentPlannedActivity(studentPlannedActivity);
        save(studentPlanELO);
        getHibernateTemplate().delete(studentPlannedActivity);
    }

    @Override
    public StudentPlannedActivity getStudentPlannedActivity(String anchorELOId, String userName, String studentPlanId) {
        userName = trimUsername(userName);
        User user = getUserByUsername(userName);
        StudentPlannedActivity studentPlannedActivity = null;
        if (user != null) {
            studentPlannedActivity = (StudentPlannedActivity) getSession().createQuery("from StudentPlannedActivityImpl as spa where spa.assoicatedELO.missionMapId like :anchorELOId and spa.studentPlan.user = :user and spa.studentPlan.id like :studentPlanId")
                    .setString("anchorELOId", anchorELOId)
                    .setEntity("user", user)
                    .setString("studentPlanId", studentPlanId)
                    .uniqueResult();

        }

        if (studentPlannedActivity == null) {
            log.info("Did not find an existing activity - creating a new one!");

            StudentPlanELO studentPlan = (StudentPlanELO) getHibernateTemplate().get(StudentPlanELOImpl.class, studentPlanId);

            AnchorELO anchorELO = getAnchorEloFromMissionMapId(anchorELOId);
            log.info("Got " + anchorELO.getName());
            Activity activity = anchorELO.getProducedBy();//getActivityThatProduces(studentPlan, anchorELO);
            log.info("Found activity " + activity.getName());
            studentPlannedActivity = new StudentPlannedActivityImpl();
            studentPlannedActivity.setName(activity.getName());
            studentPlannedActivity.setDescription(activity.getDescription());
            studentPlannedActivity.setStudentPlan(studentPlan);
            studentPlannedActivity.setAssoicatedELO(anchorELO);

            if (activity != null) {
                if (activity.getStartDate() != null) {
                    long startDate = activity.getStartDate().getTime();
                    studentPlannedActivity.setStartDate(new java.sql.Date(startDate));
                    log.info("Created start date from studentPlan");
                } else {
                    log.info("No start date found");
                }
                if (activity.getEndDate() != null) {
                    long endDL = activity.getEndDate().getTime();
                    studentPlannedActivity.setEndDate(new Date(endDL));
                    log.info("Created end date from studentplan");
                } else {
                    log.info("No end date found!");
                }


            }


            studentPlan.addStudentPlannedActivity(studentPlannedActivity);
            getHibernateTemplate().saveOrUpdate(studentPlannedActivity);

            getHibernateTemplate().saveOrUpdate(studentPlan);

        }

        return studentPlannedActivity;
    }

    private Activity getActivityThatProduces(StudentPlanELO studentPlan, AnchorELO anchorELO) {
        PedagogicalPlan plan = studentPlan.getPedagogicalPlan();
        List<Activity> activities = getActivities(plan);
        for (int i = 0; i < activities.size(); i++) {
            Activity activity = activities.get(i);
            if (activity.getAnchorELO().equals(anchorELO)) return activity;

        }
        log.warn("Did not find activity that produces " + anchorELO);
        return null;
    }


    private AnchorELO getAnchorEloFromMissionMapId(String anchorELOId) {
        log.info("Trying to get anchor elo with mission map id " + anchorELOId);
        return (AnchorELO) getSession().createQuery("from AnchorELOImpl where missionMapId like :id")
                .setString("id", anchorELOId)
                .setMaxResults(1)
                .uniqueResult();
    }


    @Override
    public StudentPlanELO getStudentPlanELOBasedOnELOId(String eloId) {
        log.info("Trying to load studentplanelo: " + eloId);
        return (StudentPlanELO) getSession().createQuery("from StudentPlanELOImpl where id like :id")
                .setString("id", eloId)
                .uniqueResult();
    }

    @Override
    public void removeMember(StudentPlannedActivity studentPlannedActivity, String userName) {
        userName = trimUsername(userName);
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
        username = trimUsername(username);

        User user = getUserByUsername(username);
        PedagogicalPlan pedagogicalPlan = null;
        if (user != null) {
            List pedPlans = getSession().createQuery("from PedagogicalPlanImpl")
                    .list();
            for (int i = 0; i < pedPlans.size(); i++) {
                PedagogicalPlan suggestedPedagogicalPlan = (PedagogicalPlan) pedPlans.get(i);
                if (suggestedPedagogicalPlan.getPublished()) {
                    pedagogicalPlan = (PedagogicalPlan) getHibernateTemplate().load(PedagogicalPlanImpl.class, suggestedPedagogicalPlan.getId());
                    pedagogicalPlan = (PedagogicalPlan) getHibernateTemplate().merge(suggestedPedagogicalPlan);
                }
            }
        }


        log.info("Found pedagogical plan :" + pedagogicalPlan);
        log.info("Searching for user: " + username);

        StudentPlanELOImpl studentPlan = (StudentPlanELOImpl) getSession().createQuery("from StudentPlanELOImpl where user = :user and pedagogicalPlan = :pedplan")
                .setEntity("user", user)
                .setEntity("pedplan", pedagogicalPlan)
                .setMaxResults(1)
                .uniqueResult();


        if (pedagogicalPlan != null && studentPlan == null)

        {
            log.info("STUDENT PLAN IS NULL - CREATING NEW");
            //return createStudentPlan(pedagogicalPlan, user);
            studentPlan = new StudentPlanELOImpl();
            log.info("PED PLAN: " + pedagogicalPlan);

            studentPlan.setPedagogicalPlan(pedagogicalPlan);
            studentPlan.setUser(user);
            save(studentPlan);

            //studentPlan = (StudentPlanELOImpl) getHibernateTemplate().load(StudentPlanELOImpl.class, studentPlan.getId());
            pedagogicalPlan = (PedagogicalPlan) getHibernateTemplate().merge(pedagogicalPlan);
            addActivities(studentPlan, pedagogicalPlan);

        } else

        {
            log.info("STUDENT PLAN WAS NOT NULL");
        }

        if (studentPlan == null)

        {
            log.warn("DID NOT FIND A PEDAGOGICAL PLAN TO ASSIGN - ARE NO PLANS PUBLISHED?");
            return null;
        }


        return studentPlan;

    }

    private String trimUsername(String username) {
        if (username.indexOf("@") > -1) {
            return username.substring(0, username.indexOf("@"));
        }
        return username;
    }
}
