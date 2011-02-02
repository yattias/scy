package eu.scy.tools.service;

import eu.scy.core.model.User;
import eu.scy.core.model.impl.SCYUserDetails;
import eu.scy.core.model.impl.SCYUserImpl;
import eu.scy.core.model.impl.ScyBaseObject;
import eu.scy.core.model.impl.pedagogicalplan.AnchorELOImpl;
import eu.scy.core.model.impl.student.StudentPlanELOImpl;
import eu.scy.core.model.impl.student.StudentPlannedActivityImpl;
import eu.scy.core.model.pedagogicalplan.AnchorELO;
import eu.scy.core.model.pedagogicalplan.PedagogicalPlan;
import eu.scy.core.model.student.StudentPlanELO;
import eu.scy.core.model.student.StudentPlannedActivity;
import eu.scy.server.pedagogicalplan.StudentPedagogicalPlanService;

import java.util.List;

/**
 * User: FuckingMonkey
 * Date: 16.aug.2010
 * Time: 08:10:31
 * The most silly idea ever (I think). From this line and down <professionalPride><value>zero</value></professionalPride>
 */
public class LocalFreakinStudentPedagogicalPlanService implements StudentPedagogicalPlanService {

    private StudentPlanELO currentStudentPlanELO;

    @Override
    public StudentPlanELO getCurrentStudentPlanELO() {
        return currentStudentPlanELO;
    }

    @Override
    public void setCurrentStudentPlanELO(StudentPlanELO currentStudentPlanELO) {
        this.currentStudentPlanELO = currentStudentPlanELO;
    }

    @Override
    public StudentPlanELO createStudentPlan(String username) {
        StudentPlanELO studentPlanELO = new StudentPlanELOImpl();
        studentPlanELO.setName("Student plan: " + username);
        studentPlanELO.setId(username + "-" + System.currentTimeMillis());
        setCurrentStudentPlanELO(studentPlanELO);
        return studentPlanELO;
    }

    @Override
    public StudentPlanELO createStudentPlan(PedagogicalPlan pedagogicalPlan, User user) {
        throw new RuntimeException("LOCAL FREAKIN STUDENT PEDAGOGICAL PLAN SERVICE createStudentPlan with ped plan and user");
    }

    @Override
    public List<StudentPlanELO> getStudentPlans(User user) {
        throw new RuntimeException("LOCAL FREAKIN STUDENT PEDAGOGICAL PLAN SERVICE getStudentPlans");
    }

    @Override
    public List<StudentPlanELO> getStudentPlans(String username) {
        throw new RuntimeException("LOCAL FREAKIN STUDENT PEDAGOGICAL PLAN SERVICE getStudentPlans");
    }

    @Override
    public void save(ScyBaseObject scyBase) {
        //wee wee - save me to roolo please
        // System.out.println("SAVING: " + scyBase + " " + scyBase.getClass().getName());
    }

    @Override
    public void addMember(StudentPlannedActivity studentPlannedActivity, String user) {
        SCYUserImpl scyUser = new SCYUserImpl();
        addDetails(scyUser, user);
        studentPlannedActivity.addMember(scyUser);
    }

    private void addDetails(SCYUserImpl scyUser, String user) {
        SCYUserDetails userDetails = new SCYUserDetails();
        userDetails.setUsername(user);
        userDetails.setPassword(user);
        scyUser.setUserDetails(userDetails);
    }

    @Override
    public void removeMember(StudentPlannedActivity studentPlannedActivity, String user) {
        // System.out.println("REMOVING USER WITH NICK NAME: " + user);
        List members = studentPlannedActivity.getMembers();
        SCYUserImpl userToRemove = null;
        for (int i = 0; i < members.size(); i++) {
            SCYUserImpl scyUser = (SCYUserImpl) members.get(i);
            if(scyUser.getUserDetails().getUsername().equals(user)) userToRemove = scyUser;
        }

        studentPlannedActivity.getMembers().remove(userToRemove);
        // System.out.println("REMOVED " + userToRemove);
    }

    @Override
    public void removeStudentPlannedActivityFromStudentPlan(StudentPlannedActivity studentPlannedActivity, StudentPlanELO studentPlanELO) {
        studentPlanELO.getStudentPlannedActivities().remove(studentPlannedActivity);
    }

    /**
     * will retrieve an already existing student planned activity, or create a new one
     * @param userName
     * @param achorELOId  @return
     * @param studentPlanId
     */

    @Override
    public StudentPlannedActivity getStudentPlannedActivity(String userName, String achorELOId, String studentPlanId) {
        // System.out.println("************************************");
        // System.out.println("USERNAME: " + userName);
        // System.out.println("AnchorELOId: " + achorELOId);
        // System.out.println("studentPlanID: " + studentPlanId);
        // System.out.println("************************************");

        if(getCurrentStudentPlanELO() != null) {
            StudentPlanELO elo = getCurrentStudentPlanELO();
            List activities = elo.getStudentPlannedActivities();
            for (int i = 0; i < activities.size(); i++) {
                StudentPlannedActivity activity = (StudentPlannedActivity) activities.get(i);
                if(activity != null && activity.getId().equals(achorELOId)) {
                    // System.out.println("Found existing activity: " + activity);
                    return activity;
                }
            }
        }

        StudentPlannedActivity studentPlannedActivity = createStudentPlannedActivity(userName, achorELOId, studentPlanId);
        return studentPlannedActivity;
    }

    private StudentPlannedActivity createStudentPlannedActivity(String userName, String anchorELOIde, String studentPlanId) {
        // System.out.println("DID NOT FIND EXISTING ACTIVITY FOR " + anchorELOIde + " creating new");

        StudentPlannedActivity studentPlannedActivity = new StudentPlannedActivityImpl();
        studentPlannedActivity.setId(anchorELOIde);
        studentPlannedActivity.setName(anchorELOIde);
        getCurrentStudentPlanELO().getStudentPlannedActivities().add(studentPlannedActivity);

        AnchorELO anchorELO = new AnchorELOImpl();
        anchorELO.setId(anchorELOIde);
        anchorELO.setName(studentPlannedActivity.getName());
        studentPlannedActivity.setAssoicatedELO(anchorELO);

        return studentPlannedActivity;
    }


    @Override
    public StudentPlanELO getStudentPlanELO(String eloId) {
        throw new RuntimeException("LOCAL FREAKIN STUDENT PEDAGOGICAL PLAN SERVICE getStudentPlanELO");
    }
}
