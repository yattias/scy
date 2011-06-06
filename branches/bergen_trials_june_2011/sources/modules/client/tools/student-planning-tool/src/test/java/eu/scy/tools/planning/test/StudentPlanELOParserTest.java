package eu.scy.tools.planning.test;

import eu.scy.core.model.User;
import eu.scy.core.model.impl.SCYUserDetails;
import eu.scy.core.model.impl.SCYUserImpl;
import eu.scy.core.model.impl.pedagogicalplan.AnchorELOImpl;
import eu.scy.core.model.impl.student.StudentPlanELOImpl;
import eu.scy.core.model.impl.student.StudentPlannedActivityImpl;
import eu.scy.core.model.pedagogicalplan.AnchorELO;
import eu.scy.core.model.student.StudentPlanELO;
import eu.scy.core.model.student.StudentPlannedActivity;
import eu.scy.tools.service.StudentPlanELOParser;
import junit.framework.TestCase;

import java.sql.Date;
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
        super.setUp();
        studentPlanELO = new StudentPlanELOImpl();
        studentPlanELO.setName("An ELO to be freakin' proud of.... NOT!");
        studentPlanELO.setDescription("Some description");
        studentPlanELO.setId("ANID");

        StudentPlannedActivity studentPlannedActivity = new StudentPlannedActivityImpl();
        studentPlannedActivity.setId("activity1");
        studentPlannedActivity.setName("ACTIVITY ONE");
        studentPlanELO.addStudentPlannedActivity(studentPlannedActivity);

        SCYUserImpl user1 = createUser("user1");
        SCYUserImpl user2 = createUser("user2");
        SCYUserImpl user3 = createUser("user3");

        studentPlannedActivity.addMember(user1);
        studentPlannedActivity.addMember(user2);
        studentPlannedActivity.addMember(user3);

        studentPlannedActivity.setStartDate(new Date(System.currentTimeMillis()));
        studentPlannedActivity.setEndDate(new Date(System.currentTimeMillis()));
        studentPlannedActivity.setDescription("A funky description");
        studentPlannedActivity.setNote("A freakin funky note");

        AnchorELO anchorELO = new AnchorELOImpl();
        anchorELO.setId("lsdkjf");
        studentPlannedActivity.setAssoicatedELO(anchorELO);

    }

    private SCYUserImpl createUser(String username) {
        SCYUserImpl scyUser = new SCYUserImpl();
        SCYUserDetails scyUserDetails = new SCYUserDetails();
        scyUserDetails.setUsername(username);
        scyUserDetails.setPassword(username);
        scyUser.setUserDetails(scyUserDetails);
        return scyUser;
    }

    public void testParseStudentPlanELO() {
        String text = StudentPlanELOParser.parseToXML(studentPlanELO);
        System.out.println(text);
    }

    public void testMarshallStringToElo() {
        String text = StudentPlanELOParser.parseToXML(studentPlanELO);
        StudentPlanELO marshalled = StudentPlanELOParser.parseFromXML(text);
        assertNotNull(marshalled);
        assertEquals(studentPlanELO.getName(), marshalled.getName());
        assertEquals(studentPlanELO.getId(), marshalled.getId());

        List activities = marshalled.getStudentPlannedActivities();
        assertEquals(studentPlanELO.getStudentPlannedActivities().size(), activities.size());

        List originalActivities = studentPlanELO.getStudentPlannedActivities();
        for (int i = 0; i < activities.size(); i++) {
            StudentPlannedActivity parsedActivity = (StudentPlannedActivity) activities.get(i);
            StudentPlannedActivity originalPlannedActivity = (StudentPlannedActivity) originalActivities.get(i);

            assertEquals(originalPlannedActivity.getStartDate().toString(), parsedActivity.getStartDate().toString());
            assertEquals(originalPlannedActivity.getEndDate().toString(), parsedActivity.getEndDate().toString());
            assertEquals(originalPlannedActivity.getDescription(), parsedActivity.getDescription());
            assertEquals(originalPlannedActivity.getNote(), parsedActivity.getNote());
            assertEquals(originalPlannedActivity.getId(), parsedActivity.getId());

            assertEquals(originalPlannedActivity.getMembers().size(), parsedActivity.getMembers().size());
            System.out.println("testing anchorelos");
            if(originalPlannedActivity.getAssoicatedELO() != null) {
                assertNotNull(parsedActivity.getAssoicatedELO());
                assertEquals(originalPlannedActivity.getAssoicatedELO().getId(), parsedActivity.getAssoicatedELO().getId());
            } else {
                System.out.println("no anchor elos for " + parsedActivity.getName());
            }

            assertEquals(originalPlannedActivity.getMembers().size(), parsedActivity.getMembers().size());
            for (int j = 0; j < originalPlannedActivity.getMembers().size(); j++) {
                User user = originalPlannedActivity.getMembers().get(j);
                User parsedUser = parsedActivity.getMembers().get(j);
                assertEquals(user.getUserDetails().getUsername(), parsedUser.getUserDetails().getUsername());
                assertEquals(user.getUserDetails().getPassword(), parsedUser.getUserDetails().getPassword());
            }

        }


    }

}
