package eu.scy.core.model.impl.student;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import eu.scy.core.model.User;
import eu.scy.core.model.impl.SCYUserImpl;
import eu.scy.core.model.impl.pedagogicalplan.AnchorELOImpl;
import eu.scy.core.model.impl.pedagogicalplan.BaseObjectImpl;
import eu.scy.core.model.impl.pedagogicalplan.LearningActivitySpaceImpl;
import eu.scy.core.model.pedagogicalplan.AnchorELO;
import eu.scy.core.model.pedagogicalplan.PedagogicalPlan;
import eu.scy.core.model.pedagogicalplan.PlannedELO;
import eu.scy.core.model.student.StudentPlanELO;
import eu.scy.core.model.student.StudentPlannedActivity;

/**
 * Implementation of the studentplannedactivity
 *
 * @author anthonjp
 */
@Entity
@Table(name = "studentplannedactivity")
public class StudentPlannedActivityImpl extends BaseObjectImpl implements StudentPlannedActivity {

    private List<User> members = new ArrayList<User>();
    private AnchorELO associatedELO;
    private Date endDate;
    private String note;
    private Date startDate;
    private String description;
    private String name;
    private StudentPlanELO studentPlan;


    @Override
    public void addMember(User member) {
        if(!members.contains(member)) {
        members.add(member);    
        }

    }

    @OneToOne(targetEntity = AnchorELOImpl.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "associatedelo_primKey")
    public AnchorELO getAssoicatedELO() {
        return associatedELO;
    }

    @Override
    public Date getEndDate() {
        return endDate;
    }

    @ManyToMany(targetEntity = SCYUserImpl.class, fetch = FetchType.EAGER)
    @JoinTable(name = "studentplannedactivities_related_to_users", joinColumns = { @JoinColumn(name = "studentplannedactivity_fk", nullable = false) }, inverseJoinColumns = @JoinColumn(name = "user_fk", nullable = false))
    public List<User> getMembers() {
        return members;
    }

    @Override
    public void setMembers(List <User> members) {
        this.members = members;
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
    public void setAssoicatedELO(AnchorELO elo) {
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
    @ManyToOne(targetEntity = StudentPlanELOImpl.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "studentplanelo_primKey")
    public StudentPlanELO getStudentPlan() {
        return studentPlan;
    }

    @Override
    public void setStudentPlan(StudentPlanELO studentPlan) {
        this.studentPlan = studentPlan;
    }
}
