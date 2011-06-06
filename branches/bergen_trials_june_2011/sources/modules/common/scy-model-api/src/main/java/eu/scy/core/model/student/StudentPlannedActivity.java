package eu.scy.core.model.student;

import java.sql.Date;
import java.util.List;

import eu.scy.core.model.User;
import eu.scy.core.model.pedagogicalplan.AnchorELO;
import eu.scy.core.model.pedagogicalplan.BaseObject;
import eu.scy.core.model.pedagogicalplan.PlannedELO;

/**
 * Planning data associated with an activity
 * 
 * @author anthonjp
 *
 */
public interface StudentPlannedActivity extends BaseObject {
	
	/**
	 * sets the Start date of this activity
	 * 
	 * @param startDate
	 */
	public void setStartDate(Date startDate);
	
	/**
	 * gets the start date
	 * 
	 * @return
	 */
	public Date getStartDate();

	/**
	 * The end date of the activyt
	 * 
	 * @param endDate
	 */
	public void setEndDate(Date endDate);
	
	/**
	 * Gets the end date;
	 * 
	 * @return
	 */
	public Date getEndDate();
	
	
	/**
	 * Sets a note on this plan
	 * 
	 * @param note
	 * @return
	 */
	public void setNote(String note);
	
	/**
	 * Gets the note of this plan
	 * 
	 * @return
	 */
	public String getNote();
	
	
	/**
	 * adds a user to this plan
	 * 
	 * @param member
	 */
	public void addMember(User member);
	
	/**
	 * Gets the list of members
	 * 
	 * @return
	 */
	public List<User> getMembers();
	
	/**
	 * This is the elo that this activity is assoicatied with this activity
	 * 
	 * @param elo
	 */
	public void setAssoicatedELO(AnchorELO elo);
	
	/**
	 * Gets the elo that is associated with this activity
	 * 
	 * @return
	 */
	public AnchorELO getAssoicatedELO();

    StudentPlanELO getStudentPlan();

    void setStudentPlan(StudentPlanELO studentPlan);

    void setMembers(List <User> members);
}
