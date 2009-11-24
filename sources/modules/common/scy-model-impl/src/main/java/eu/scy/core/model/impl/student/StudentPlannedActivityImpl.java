package eu.scy.core.model.impl.student;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import eu.scy.core.model.User;
import eu.scy.core.model.pedagogicalplan.PlannedELO;
import eu.scy.core.model.student.StudentPlannedActivity;

/**
 * Implementation of the studentplannedactivity
 * 
 * @author anthonjp
 *
 */
public class StudentPlannedActivityImpl implements StudentPlannedActivity {

	private List<User> members = new ArrayList<User>();
	private PlannedELO associatedELO;
	private Date endDate;
	private String note;
	private Date startDate;
	private String description;
	private String name;
	
	
	@Override
	public void addMember(User member) {
		members.add(member);
	}

	@Override
	public PlannedELO getAssoicatedELO() {
		return associatedELO;
	}

	@Override
	public Date getEndDate() {
		return endDate;
	}

	@Override
	public List<User> getMembers() {
		return members;
	}

	@Override
	public String getNote() {
		return note;
	}

	@Override
	public Date getStartDate() {
		return startDate;
	}

	@Override
	public void setAssoicatedELO(PlannedELO elo) {
		this.associatedELO = elo;
	}

	@Override
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Override
	public void setNote(String note) {
		this.note = note;
	}

	@Override
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
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
