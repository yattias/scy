package eu.scy.tools.planning.test;

import eu.scy.core.model.impl.student.StudentPlanELOImpl;
import eu.scy.core.model.impl.student.StudentPlannedActivityImpl;
import eu.scy.core.model.student.StudentPlanELO;
import eu.scy.core.model.student.StudentPlannedActivity;
import eu.scy.tools.service.StudentPlanELOParser;
import junit.framework.TestCase;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 17.aug.2010
 * Time: 08:42:48
 * To change this template use File | Settings | File Templates.
 */
public class StudentPlanELOParserTest extends TestCase {

    private StudentPlanELO studentPlanELO;

    @Override
    protected void setUp() throws Exception {
        super.setUp();    //To change body of overridden methods use File | Settings | File Templates.
        studentPlanELO = new StudentPlanELOImpl();
        studentPlanELO.setName("An ELO to be freakin' proud of.... NOT!");
        studentPlanELO.setDescription("Some description");
        studentPlanELO.setId("ANID");

        StudentPlannedActivity studentPlannedActivity = new StudentPlannedActivityImpl();
        studentPlannedActivity.setId("activity1");
        studentPlannedActivity.setName("ACTIVITY ONE");
        studentPlanELO.addStudentPlannedActivity(studentPlannedActivity);

    }

    public void testParseStudentPlanELO() {
        String text = StudentPlanELOParser.parseToXML(studentPlanELO);
        System.out.println(text);
    }

    public void testMarchallStringToElo() {
        String text = StudentPlanELOParser.parseToXML(studentPlanELO);
        StudentPlanELO marshalled = StudentPlanELOParser.parseFromXML(text);
        assertEquals(studentPlanELO.getName(), marshalled.getName());
        assertEquals(studentPlanELO.getId(), marshalled.getId());

        List activities = marshalled.getStudentPlannedActivities();
        assertEquals(studentPlanELO.getStudentPlannedActivities().size(), activities.size());


    }

}
