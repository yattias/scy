package eu.scy.tools.planning.test;

import eu.scy.core.model.User;
import eu.scy.core.model.UserDetails;
import eu.scy.core.model.impl.SCYStudentUserDetails;
import eu.scy.core.model.impl.SCYUserImpl;
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
 * To change this template use File | Settings | File Templates.
 */
public class StudentPlannerDataLoadingTest extends TestCase {

    private static Logger log = Logger.getLogger("StudentPlannerDataLoadingTest.class");

    public StudentPedagogicalPlanService getStudentPlanService() {
        StudentPedagogicalPlanService service = null;
        //service = getWithUrl("http://localhost:8080/server-external-components/remoting/studentPlan-httpinvoker");
        //service = getWithUrl("http://scy.collide.info:8080/extcomp/remoting/studentPlan-httpinvoker");
        return service;

    }

    private StudentPedagogicalPlanService getWithUrl(String url) {
        HttpInvokerProxyFactoryBean fb = new HttpInvokerProxyFactoryBean();
        fb.setServiceInterface(StudentPedagogicalPlanService.class);
        fb.setServiceUrl(url);
        fb.afterPropertiesSet();
        return (StudentPedagogicalPlanService) fb.getObject();
    }

    public void testGetStudentPlans() {
        if (getStudentPlanService() != null) {
            SCYUserImpl user = new SCYUserImpl();
            SCYStudentUserDetails details = new SCYStudentUserDetails();
            details.setUsername("wiwo");
            user.setUserDetails(details);
            List<StudentPlanELO> studentPlans = getStudentPlanService().getStudentPlans("wiwoo");
            assertTrue(studentPlans.size() > 0);
            log.info("plans:" + studentPlans.size());
            for (int i = 0; i < studentPlans.size(); i++) {
                StudentPlanELO studentPlanELO = studentPlans.get(i);
                System.out.println("PLAN:" + studentPlanELO);
                List<StudentPlannedActivity> activities = studentPlanELO.getStudentPlannedActivities();
                for (int j = 0; j < activities.size(); j++) {
                    StudentPlannedActivity studentPlannedActivity = activities.get(j);
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

}
