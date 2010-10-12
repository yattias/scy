package eu.scy.core.persistence.hibernate;

import eu.scy.core.StudentPedagogicalPlanPersistenceService;
import eu.scy.core.model.User;
import eu.scy.core.model.impl.pedagogicalplan.AssignedPedagogicalPlanImpl;
import eu.scy.core.model.pedagogicalplan.AssignedPedagogicalPlan;
import eu.scy.core.model.pedagogicalplan.PedagogicalPlan;
import eu.scy.core.openfire.BuddyService;
import eu.scy.core.persistence.AssignedPedagogicalPlanDAO;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 26.mar.2010
 * Time: 05:41:12
 */
public class AssignedPedagogicalPlanDAOHibernate extends ScyBaseDAOHibernate implements AssignedPedagogicalPlanDAO {

    private BuddyService buddyService;
    StudentPedagogicalPlanPersistenceService studentPedagogicalPlanPersistenceService;

    public BuddyService getBuddyService() {
        return buddyService;
    }

    public void setBuddyService(BuddyService buddyService) {
        this.buddyService = buddyService;
    }

    public StudentPedagogicalPlanPersistenceService getStudentPedagogicalPlanPersistenceService() {
        return studentPedagogicalPlanPersistenceService;
    }

    public void setStudentPedagogicalPlanPersistenceService(StudentPedagogicalPlanPersistenceService studentPedagogicalPlanPersistenceService) {
        this.studentPedagogicalPlanPersistenceService = studentPedagogicalPlanPersistenceService;
    }

    @Override
    public AssignedPedagogicalPlan assignPedagogicalPlanToUser(PedagogicalPlan plan, User user) {
        AssignedPedagogicalPlan assignedPedagogicalPlan= new AssignedPedagogicalPlanImpl();
        assignedPedagogicalPlan.setPedagogicalPlan(plan);
        assignedPedagogicalPlan.setUser(user);
        save(assignedPedagogicalPlan);

        if(plan.getMakeAllAssignedStudentsBuddies()) {
            createBuddies(user, plan);
        }

        assignStudentPlan(user, plan);

        return assignedPedagogicalPlan;
    }

    private void assignStudentPlan(User user, PedagogicalPlan plan) {

        getStudentPedagogicalPlanPersistenceService().createStudentPlan(plan, user);

    }

    private void createBuddies(User user, PedagogicalPlan plan) {
        List <User> users = getAssignedUsers(plan);
        for (int i = 0; i < users.size(); i++) {
            User user1 = users.get(i);
            try {
                getBuddyService().makeBuddies(user.getUserDetails().getUsername(), user.getUserDetails().getPassword(), user1.getUserDetails().getUsername(), user1.getUserDetails().getPassword());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public List <User> getAssignedUsers(PedagogicalPlan pedagogicalPlan) {
        return getSession().createQuery("select ap.user from AssignedPedagogicalPlanImpl as ap where ap.pedagogicalPlan = :pedagogicalPlan")
                .setEntity("pedagogicalPlan", pedagogicalPlan)
                .list();
    }

    @Override
    public List<AssignedPedagogicalPlan> getAssignedPedagogicalPlans(User user) {
        return getSession().createQuery("From AssignedPedagogicalPlanImpl where user = :user")
                .setEntity("user", user)
                .list();


    }

    @Override
    public List<AssignedPedagogicalPlan> getAssignedPedagogicalPlans(PedagogicalPlan pedagogicalPlan) {
        return getSession().createQuery("from AssignedPedagogicalPlanImpl where pedagogicalPlan = :pedagogicalPlan")
                .setEntity("pedagogicalPlan", pedagogicalPlan)
                .list();
    }

    public Long getAssignedPedagogicalPlansCount(PedagogicalPlan pedagogicalPlan) {
        return (Long) getSession().createQuery("select distinct count (app) from AssignedPedagogicalPlanImpl as app where app.pedagogicalPlan = :pedagogicalPlan")
                .setEntity("pedagogicalPlan", pedagogicalPlan)
                .uniqueResult();
    }

    @Override
    public void removeAssignedAssessment(User user, PedagogicalPlan plan) {
        AssignedPedagogicalPlan assignedPedagogicalPlan = (AssignedPedagogicalPlan) getSession().createQuery("from AssignedPedagogicalPlanImpl where user = :user and pedagogicalPlan = :pedagogicalPlan")
                .setEntity("user", user)
                .setEntity("pedagogicalPlan", plan)
                .setMaxResults(1)
                .uniqueResult();
        if(assignedPedagogicalPlan != null) {
            getHibernateTemplate().delete(assignedPedagogicalPlan);
        }

    }

    @Override
    public AssignedPedagogicalPlan getCurrentAssignedPedagogicalPlan(User currentUser) {
        List <AssignedPedagogicalPlan> assignedPedagogicalPlans = getAssignedPedagogicalPlans(currentUser);
        logger.warn("HACJK HACK");
        if(assignedPedagogicalPlans.size() > 0) return assignedPedagogicalPlans.get(0);
        return null;
    }
}
