package eu.scy.tools.planning.test;

import eu.scy.core.model.User;
import eu.scy.core.model.impl.SCYUserDetails;
import eu.scy.core.model.impl.SCYUserImpl;
import eu.scy.core.model.impl.student.StudentPlanELOImpl;
import eu.scy.core.model.impl.student.StudentPlannedActivityImpl;
import eu.scy.core.model.pedagogicalplan.Activity;
import eu.scy.core.model.student.StudentPlanELO;
import eu.scy.core.model.student.StudentPlannedActivity;
import eu.scy.server.pedagogicalplan.StudentPedagogicalPlanService;
import eu.scy.tools.service.LocalFreakinStudentPedagogicalPlanService;
import junit.framework.TestCase;
import org.junit.Before;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 17.aug.2010
 * Time: 06:31:43
 * To change this template use File | Settings | File Templates.
 */
public class TestLocalFreakinStudentPedagogicalPlanService extends TestCase {

    private StudentPedagogicalPlanService service;
    private StudentPlanELO studentPlanELO ;

    @Override
    protected void setUp() throws Exception {
        super.setUp();    //To change body of overridden methods use File | Settings | File Templates.

        service = new LocalFreakinStudentPedagogicalPlanService();

        SCYUserImpl user1 = new SCYUserImpl();
        addDetails(user1, "user1");

        SCYUserImpl user2 = new SCYUserImpl();
        addDetails(user2, "user2");

        SCYUserImpl user3 = new SCYUserImpl();
        addDetails(user3, "user3");

        StudentPlannedActivity activity1 = new StudentPlannedActivityImpl();
        activity1.setName("Activity 1");
        activity1.addMember(user1);
        activity1.addMember(user2);
        activity1.addMember(user3);

        StudentPlannedActivity activity2 = new StudentPlannedActivityImpl();
        activity2.setName("Activity 2");

        StudentPlannedActivity activity3 = new StudentPlannedActivityImpl();
        activity3.setName("Activity 3");



        studentPlanELO = new StudentPlanELOImpl();
        studentPlanELO.addStudentPlannedActivity(activity1);
        studentPlanELO.addStudentPlannedActivity(activity2);
        studentPlanELO.addStudentPlannedActivity(activity3);


    }

    private void addDetails(SCYUserImpl user1, String s) {
        SCYUserDetails details = new SCYUserDetails();
        details.setUsername(s);
        details.setPassword(s);
        user1.setUserDetails(details);


    }

    public void testRemoveActivitiesFromPlan() {
        StudentPlannedActivity activity = studentPlanELO.getStudentPlannedActivities().get(0);
        assertNotNull(activity);
        assert(studentPlanELO.getStudentPlannedActivities().contains(activity));
        service.removeStudentPlannedActivityFromStudentPlan(activity, studentPlanELO);
        assert(studentPlanELO.getStudentPlannedActivities().contains(activity));
    }

    public void testAddMemberToActivity() {
        String username = "HenrikDaCoolDude";
        StudentPlannedActivity activity = new StudentPlannedActivityImpl();
        assertEquals(0, activity.getMembers().size());

        service.addMember(activity, username);
        assertEquals(1, activity.getMembers().size());
    }

    public void testRemoveMemberFromActivity() {
        StudentPlannedActivity activity = studentPlanELO.getStudentPlannedActivities().get(0);
        assertTrue(activity.getMembers().size() > 0);
        User user = activity.getMembers().get(0);
        assertTrue(activity.getMembers().contains(user));
        service.removeMember(activity, user.getUserDetails().getUsername());
        assertTrue(!activity.getMembers().contains(user));
    }





}
