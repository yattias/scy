package eu.scy.tools.planning.test;

import eu.scy.core.model.User;
import eu.scy.core.model.UserDetails;
import eu.scy.core.model.impl.SCYStudentUserDetails;
import eu.scy.core.model.impl.SCYUserImpl;
import eu.scy.core.model.impl.ScyBaseObject;
import eu.scy.core.model.pedagogicalplan.Activity;
import eu.scy.core.model.pedagogicalplan.AnchorELO;
import eu.scy.core.model.pedagogicalplan.LearningActivitySpace;
import eu.scy.core.model.student.StudentPlanELO;
import eu.scy.core.model.student.StudentPlannedActivity;
import eu.scy.server.pedagogicalplan.StudentPedagogicalPlanService;
import junit.framework.TestCase;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;

import java.util.List;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 08.feb.2010
 * Time: 05:29:41
 */
public class StudentPlannerDataLoadingTest extends TestCase {

    private final static String USER_NAME = "wiwo";

    private static Logger log = Logger.getLogger("StudentPlannerDataLoadingTest.class");

    public StudentPedagogicalPlanService getStudentPlanService() {
        StudentPedagogicalPlanService service = null;
        //service = getWithUrl("http://localhost:8080/server-external-components/remoting/studentPlan-httpinvoker");
        //service = getWithUrl("http://scy.collide.info:8080/extcomp/remoting/studentPlan-httpinvoker");
        //service = getWithUrl("http://83.168.205.138:8080/extcomp/remoting/studentPlan-httpinvoker");
        return service;

    }

    private StudentPedagogicalPlanService getWithUrl(String url) {
        HttpInvokerProxyFactoryBean fb = new HttpInvokerProxyFactoryBean();
        fb.setServiceInterface(StudentPedagogicalPlanService.class);
        fb.setServiceUrl(url);
        fb.afterPropertiesSet();
        return (StudentPedagogicalPlanService) fb.getObject();
    }

    /*
    public void testGetStudentPlans() {
        if (getStudentPlanService() != null) {
            SCYUserImpl user = new SCYUserImpl();
            SCYStudentUserDetails details = new SCYStudentUserDetails();
            details.setUsername("wiwo");
            user.setUserDetails(details);

            List<StudentPlanELO> studentPlans = getStudentPlans();
            assertTrue(studentPlans.size() > 0);
            log.info("plans:" + studentPlans.size());
            for (int i = 0; i < studentPlans.size(); i++) {
                StudentPlanELO studentPlanELO = studentPlans.get(i);
                System.out.println("PLAN:" + studentPlanELO);
                List<StudentPlannedActivity> activities = studentPlanELO.getStudentPlannedActivities();
                for (int j = 0; j < activities.size(); j++) {
                    StudentPlannedActivity studentPlannedActivity = activities.get(j);
                    studentPlannedActivity.getNote();
                    studentPlannedActivity.setNote("Henrik is totally cool!!");
                    getStudentPlanService().save((ScyBaseObject) studentPlannedActivity);
                    log.info("SPA:" + studentPlannedActivity.getName());
                    AnchorELO elo = studentPlannedActivity.getAssoicatedELO();
                    log.info("Anchor elo: " + elo);
                    if (elo != null) {
                        Activity activity = elo.getProducedBy();
                        log.info("Activity: " + activity);
                        if (activity != null) {
                            LearningActivitySpace las = activity.getLearningActivitySpace();
                            log.info("LAS: " + las);
                        }
                    }
                }
            }
        }

    }

    public void testAddMemberToStudentPlannedActivity() {
        if (getStudentPlanService() != null) {
            List<StudentPlanELO> studentPlans = getStudentPlans();
            if (studentPlans.size() > 0) {
                StudentPlanELO plan = studentPlans.get(0);
                List<StudentPlannedActivity> studentPlannedActivities = plan.getStudentPlannedActivities();
                if (studentPlannedActivities.size() > 0) {
                    StudentPlannedActivity activity = studentPlannedActivities.get(0);
                    getStudentPlanService().addMember(activity, USER_NAME);
                } else {
                    fail("Did not find any activities");
                }

            } else {
                fail("Did not find any plans");
            }


        }

    }
    */
    private List<StudentPlanELO> getStudentPlans() {
        List<StudentPlanELO> studentPlans = getStudentPlanService().getStudentPlans(USER_NAME);
        return studentPlans;
    }

    /*private StudentPlanELO getAStudentPlanELOForTesting() {
        List<StudentPlanELO> studentPlanELOs = getStudentPlans();
        if (studentPlanELOs != null && studentPlanELOs.size() > 0) return studentPlanELOs.get(0);
        return null;
    } */

    private StudentPlanELO createStudentPlanForUser(String username) {
        StudentPlanELO plan = getStudentPlanService().createStudentPlan(username);
        if(plan == null) fail("STUDENT PLAN IS NULL");
        getStudentPlanService().getStudentPlannedActivity(username, "report", plan.getId());

        return plan;

    }

    public void testRemoveStudentActivityFromStudentPlan() {
        if (getStudentPlanService() != null) {
            StudentPlanELO studentPlanELO = createStudentPlanForUser(USER_NAME);
            String id = studentPlanELO.getId();

            if (studentPlanELO != null) {
                assertTrue(studentPlanELO.getStudentPlannedActivities().size() > 0);
                log.info("Activities: " + studentPlanELO.getStudentPlannedActivities().size());

                StudentPlannedActivity activity = studentPlanELO.getStudentPlannedActivities().get(0);
                assertNotNull(activity);

                getStudentPlanService().removeStudentPlannedActivityFromStudentPlan(activity, studentPlanELO);
                studentPlanELO = getStudentPlanService().getStudentPlanELO(id);
                assertNotNull(studentPlanELO);
                
                assertFalse( studentPlanELO.getStudentPlannedActivities().contains(activity));


            }



        }
    }
    /*
    public void testCreateStudentPlan() {
        if (getStudentPlanService() != null) {
            StudentPlanELO elo = getStudentPlanService().createStudentPlan("hill");//put in the freakin username here
            String eloId = elo.getId();//this is the freakin elo id that I want you to store in the elo xml shit
            assertNotNull(elo);

            //when you want to load the elo again you do this:
            //1. get the elo id from the freakin xml content shit:
            //2. use the service to get the student plan and display it in funky tool
            StudentPlanELO loaded = getStudentPlanService().getStudentPlanELO(eloId);
            assertNotNull(loaded);

            for (int i = 0; i < loaded.getStudentPlannedActivities().size(); i++) {
                StudentPlannedActivity studentPlannedActivity = loaded.getStudentPlannedActivities().get(i);
                System.out.println("planned activity " + studentPlannedActivity.getName());
            }

            //freakin simple eh?
        }
    }

    public void testCreateStudentPlannedActivitiesBasedOnAnchorElos() {

        final String USER_NAME = "aa@collide.info";

        if (getStudentPlanService() != null) {
            StudentPlanELO studentPlanELO = getStudentPlanService().createStudentPlan(USER_NAME);
            String planId = studentPlanELO.getId();
            assertNotNull(planId);

            List studentPlannedActivities = studentPlanELO.getStudentPlannedActivities();
            assert (studentPlannedActivities != null); // the plan should have an empty list of planned activities
            assertEquals(0, studentPlannedActivities.size());

            StudentPlannedActivity plannedActivity = getStudentPlanService().getStudentPlannedActivity(USER_NAME, "start", planId);
            assertNotNull(plannedActivity);
            String startActivityId = plannedActivity.getId();
            System.out.println("FOUND ACTIVITY: " + startActivityId + " " + plannedActivity.getName());

            plannedActivity = getStudentPlanService().getStudentPlannedActivity(USER_NAME, "conceptMap", planId);
            assertNotNull(plannedActivity);
            String conceptMapActivityId = plannedActivity.getId();

            assert (!conceptMapActivityId.equals(startActivityId)); // these should NOT be the same

            //reload the plan from the server since new activities have been created there, and are not present on the freakin' client
            studentPlanELO = getStudentPlanService().getStudentPlanELO(planId);

            Integer activityCount = studentPlanELO.getStudentPlannedActivities().size();
            assert (activityCount > 0);

            StudentPlanELO loaded = getStudentPlanService().getStudentPlanELO(planId);
            assertNotNull(loaded);
            System.out.println("Loaded " + loaded.getStudentPlannedActivities().size() + " planned activities");
            assertTrue(loaded.getStudentPlannedActivities().size() > 0);

            assertEquals(activityCount, (Integer) loaded.getStudentPlannedActivities().size());   //these should be the same since they refer to the size of the same collections

            List loadedActivivties = loaded.getStudentPlannedActivities();
            for (int i = 0; i < loadedActivivties.size(); i++) {
                StudentPlannedActivity studentPlannedActivity = (StudentPlannedActivity) loadedActivivties.get(i);
                System.out.println(studentPlannedActivity.getName());
            }

            //test that changing the activity works :
            StudentPlannedActivity activity = getStudentPlanService().getStudentPlannedActivity(USER_NAME, "start", planId);
            assertNotNull(activity);
            activity.setNote("THis is the freakin start of something new and great! Long live innovation!");
            getStudentPlanService().save((ScyBaseObject) activity);


        }
    }

    */

    
}
