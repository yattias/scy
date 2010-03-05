/**
 * 
 */
package eu.scy.core.model.impl.student;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import eu.scy.core.model.User;
import eu.scy.core.model.impl.SCYUserImpl;
import eu.scy.core.model.impl.UserRoleImpl;
import eu.scy.core.model.impl.pedagogicalplan.BaseObjectImpl;
import eu.scy.core.model.impl.pedagogicalplan.LearningActivitySpaceImpl;
import eu.scy.core.model.impl.pedagogicalplan.PedagogicalPlanImpl;
import eu.scy.core.model.pedagogicalplan.PedagogicalPlan;
import eu.scy.core.model.student.StudentPlanELO;
import eu.scy.core.model.student.StudentPlannedActivity;

/**
 * Implementation for student plan elo
 * 
 * @author anthonjp
 *
 */
@Entity
@Table(name = "studentplanelo" )
public class StudentPlanELOImpl extends BaseObjectImpl implements StudentPlanELO {

	
	private List<StudentPlannedActivity> studentPlannedActivities = new ArrayList<StudentPlannedActivity>();
	private PedagogicalPlan pedagogicalPlan;
	private User user;

	@Override
	public void addStudentPlannedActivity(StudentPlannedActivity activity) {
		getStudentPlannedActivities().add(activity);
        activity.setStudentPlan(this);
	}

    @OneToOne(targetEntity = PedagogicalPlanImpl.class, fetch = FetchType.EAGER)
    @JoinColumn(name="pedagogicalPlan_primKey")
	public PedagogicalPlan getPedagogicalPlan() {
		return pedagogicalPlan;
	}

    @OneToMany(targetEntity = StudentPlannedActivityImpl.class, mappedBy = "studentPlan", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	public List<StudentPlannedActivity> getStudentPlannedActivities() {
		return studentPlannedActivities;
	}

    public void removeStudentPlannedActivity(StudentPlannedActivity studentPlannedActivity) {
        studentPlannedActivity.setStudentPlan(null);
        getStudentPlannedActivities().remove(studentPlannedActivity);
    }

    @Override
    public void setStudentPlannedActivities(List<StudentPlannedActivity> studentPlannedActivities) {
        this.studentPlannedActivities = studentPlannedActivities;
    }

    @ManyToOne(targetEntity = SCYUserImpl.class,  fetch = FetchType.LAZY)
	@JoinColumn(name = "user_primKey")
    public User getUser() {
		return user;
	}

	@Override
	public void setPedagogicalPlan(PedagogicalPlan pedPlan) {
		this.pedagogicalPlan = pedPlan;

	}

	@Override
	public void setUser(User user) {
		this.user = user;
	}


}
