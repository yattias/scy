/**
 * 
 */
package eu.scy.core.model.impl.student;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import eu.scy.core.model.User;
import eu.scy.core.model.impl.SCYUserImpl;
import eu.scy.core.model.impl.UserRoleImpl;
import eu.scy.core.model.impl.pedagogicalplan.LearningActivitySpaceImpl;
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

    @OneToOne(targetEntity = PedagogicalPlan.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name="pedagogicalPlan_primKey")
	public PedagogicalPlan getPedagogicalPlan() {
		return pedagogicalPlan;
	}

    @OneToMany(targetEntity = StudentPlannedActivityImpl.class, mappedBy = "studentplannedactivity", fetch = FetchType.LAZY)
	public List<StudentPlannedActivity> getStudentPlannedActivities() {
		return studentPlannedActivities;
	}

	@ManyToOne(targetEntity = SCYUserImpl.class, cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
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
