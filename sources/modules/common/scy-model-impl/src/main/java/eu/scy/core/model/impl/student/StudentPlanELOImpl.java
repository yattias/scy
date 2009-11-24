/**
 * 
 */
package eu.scy.core.model.impl.student;

import java.util.ArrayList;
import java.util.List;

import eu.scy.core.model.User;
import eu.scy.core.model.pedagogicalplan.PedagogicalPlan;
import eu.scy.core.model.student.StudentPlanELO;
import eu.scy.core.model.student.StudentPlannedActivity;

/**
 * Implementation for student plan elo
 * 
 * @author anthonjp
 *
 */
public class StudentPlanELOImpl implements StudentPlanELO {

	
	private List<StudentPlannedActivity> studentPlannedActivities = new ArrayList<StudentPlannedActivity>();
	private PedagogicalPlan pedagogicalPlan;
	private User user;
	private String description;
	private String name;
	
	@Override
	public void addStudentPlannedActivity(StudentPlannedActivity activity) {
		studentPlannedActivities.add(activity);
	}

	@Override
	public PedagogicalPlan getPedagogicalPlan() {
		return pedagogicalPlan;
	}

	@Override
	public List<StudentPlannedActivity> getStudentPlannedActivities() {
		return studentPlannedActivities;
	}

	@Override
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

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

}
