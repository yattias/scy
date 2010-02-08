package eu.scy.tools.planning.test;

import eu.scy.core.model.User;
import eu.scy.core.model.UserDetails;
import eu.scy.core.model.impl.SCYStudentUserDetails;
import eu.scy.core.model.impl.SCYUserImpl;
import eu.scy.core.model.student.StudentPlanELO;
import eu.scy.server.pedagogicalplan.StudentPedagogicalPlanService;
import junit.framework.TestCase;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 08.feb.2010
 * Time: 05:29:41
 * To change this template use File | Settings | File Templates.
 */
public class StudentPlannerDataLoadingTest extends TestCase {

    public StudentPedagogicalPlanService getStudentPlanService() {
        HttpInvokerProxyFactoryBean fb = new HttpInvokerProxyFactoryBean();
        fb.setServiceInterface(StudentPedagogicalPlanService.class);
        fb.setServiceUrl("http://scy.collide.info:8080/extcomp/remoting/studentPlan-httpinvoker");
        fb.afterPropertiesSet();
        StudentPedagogicalPlanService service = (StudentPedagogicalPlanService) fb.getObject();
        return service;

    }

    public void testSetupStudentPlanService() {
        assertNotNull(getStudentPlanService());
    }

    public void testGetStudentPlan() {
        SCYUserImpl user = new SCYUserImpl();
        SCYStudentUserDetails details = new SCYStudentUserDetails();
        details.setUsername("digital");
        user.setUserDetails(details);
        List<StudentPlanELO> studentPlans = getStudentPlanService().getStudentPlans(user);
        assertTrue(studentPlans.size() > 0);
        for (int i = 0; i < studentPlans.size(); i++) {
            StudentPlanELO studentPlanELO = studentPlans.get(i);
            System.out.println("PLAN:" + studentPlanELO);
        }
    }

}
