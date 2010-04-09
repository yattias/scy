package eu.scy.core.model.student;

import java.util.List;

import eu.scy.core.model.User;
import eu.scy.core.model.pedagogicalplan.PedagogicalPlan;
import eu.scy.core.model.pedagogicalplan.PlannedELO;

/**
 * This is the ELO is that produced when the student planns out their activities
 * 
 * @author anthonjp
 *
 */
public interface StudentPlanELO extends PlannedELO {
	
	/**
	 * sets the user who is making freaking plan
	 * 
	 * @param user
	 */
	public void setUser(User user);
	
	
	/**
	 * Gets the user associated with freaking plan
	 * 
	 * @return
	 */
	public User getUser();
	
	/**
	 * Gets the pedplan associated with this studentplan
	 * @return
	 */
	public PedagogicalPlan getPedagogicalPlan();
	
	/**
	 * Sets the pedagogical plan
	 * 
	 * @param pedPlan
	 */
	public void setPedagogicalPlan(PedagogicalPlan pedPlan);
	
	
	/**
	 * Adds an student planned activity to this plan
	 * 
	 * @param activity
	 */
	public void addStudentPlannedActivity(StudentPlannedActivity activity);
	
	/**
	 * get all the student planned activities
	 * 
	 * @return
	 */
	public List<StudentPlannedActivity> getStudentPlannedActivities();

    void setStudentPlannedActivities(List<StudentPlannedActivity> studentPlannedActivities);

    void removeStudentPlannedActivity(StudentPlannedActivity studentPlannedActivity);
}
