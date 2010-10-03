package eu.scy.core.persistence.hibernate;

import eu.scy.core.model.User;
import eu.scy.core.model.impl.student.StudentPlanELOImpl;
import eu.scy.core.model.impl.student.StudentPlannedActivityImpl;
import eu.scy.core.model.pedagogicalplan.PedagogicalPlan;
import eu.scy.core.model.pedagogicalplan.PedagogicalPlanTemplate;
import eu.scy.core.model.student.StudentPlannedActivity;
import eu.scy.core.persistence.UserDAO;
import org.junit.Test;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 12.jan.2010
 * Time: 12:06:45
 */
public class StudentPedagogicalPlanPersistenceDAOHibernateTest extends AbstractPedagogicalPlanRelatedTest {

    private StudentPedagogicalPlanPersistenceDAOHibernate studentPedagogicalPlanPersistenceDAOHibernate;

    private UserDAO userDAOHibernate;

    public StudentPedagogicalPlanPersistenceDAOHibernate getStudentPedagogicalPlanPersistenceDAOHibernate() {
        return studentPedagogicalPlanPersistenceDAOHibernate;
    }

    public void setStudentPedagogicalPlanPersistenceDAOHibernate(StudentPedagogicalPlanPersistenceDAOHibernate studentPedagogicalPlanPersistenceDAOHibernate) {
        this.studentPedagogicalPlanPersistenceDAOHibernate = studentPedagogicalPlanPersistenceDAOHibernate;
    }

    public UserDAO getUserDAOHibernate() {
        return userDAOHibernate;
    }

    public void setUserDAOHibernate(UserDAO userDAOHibernate) {
        this.userDAOHibernate = userDAOHibernate;
    }

    @Test
    public void testThatDAOIsHere() {
        assertNotNull(getStudentPedagogicalPlanPersistenceDAOHibernate());
    }

    public void testCreateStudentPlanELO() {
        PedagogicalPlanTemplate template = createPedagogicalPlanTemplate("The coolest template ever");
        PedagogicalPlan pedagogicalPlan = createPedagogicalPlan(template);

        User student = getUserDAOHibernate().createUser("Hilly", "damageINC", "ROLE_TEACHER");
        assertNotNull(student);
        assertNotNull(student.getId());

        StudentPlanELOImpl studentPlan = (StudentPlanELOImpl) getStudentPedagogicalPlanPersistenceDAOHibernate().createStudentPlan(pedagogicalPlan, student);
        assertNotNull(studentPlan);
        assertNotNull(studentPlan.getId());
        assertNotNull(studentPlan.getPedagogicalPlan());
        assertNotNull(studentPlan.getUser());
    }

    public void testGetStudentPlans() {
       /*
        PedagogicalPlanTemplate template = createPedagogicalPlanTemplate("The coolest template ever");
        PedagogicalPlan pedagogicalPlan = createPedagogicalPlan(template);

        User student = getUserDAOHibernate().createUser("Hilly", "damageINC");
        assertNotNull(student);

        StudentPlanELOImpl studentPlan = (StudentPlanELOImpl) getStudentPedagogicalPlanPersistenceDAOHibernate().createStudentPlan(pedagogicalPlan, student);
        assertNotNull(studentPlan.getId());
        assertNotNull(studentPlan.getStudentPlannedActivities());
        assert(studentPlan.getStudentPlannedActivities().size() > 0);

        assert(studentPlan.getPedagogicalPlan() != null);

        for (int i = 0; i < studentPlan.getStudentPlannedActivities().size(); i++) {
            StudentPlannedActivityImpl studentPlannedActivity = (StudentPlannedActivityImpl) studentPlan.getStudentPlannedActivities().get(i);
            //assertNotNull(studentPlannedActivity.getAssoicatedELO());
            assertNotNull(studentPlannedActivity.getId());
        }
        */

    }

    @Test
    public void testAddMemberToStudentPlannedActivity() {
        User student = getUserDAOHibernate().createUser("hhh", "hhhh", "ROLE_STUDENT");

        StudentPlannedActivity studentPlannedActivity = new StudentPlannedActivityImpl();
        getStudentPedagogicalPlanPersistenceDAOHibernate().save(studentPlannedActivity);
        String id = studentPlannedActivity.getId();
        assertNotNull(studentPlannedActivity.getId());

        studentPlannedActivity.addMember(student);
        getStudentPedagogicalPlanPersistenceDAOHibernate().save(studentPlannedActivity);

        StudentPlannedActivity act = (StudentPlannedActivity) getStudentPedagogicalPlanPersistenceDAOHibernate().getObject(StudentPlannedActivityImpl.class, id);
        assertNotNull(act);
        assertTrue(act.getMembers().contains(student));
    }
}
