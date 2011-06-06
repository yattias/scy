package eu.scy.core.model.impl.pedagogicalplan;

import eu.scy.core.model.pedagogicalplan.Activity;
import eu.scy.core.model.pedagogicalplan.AnchorELO;
import eu.scy.core.model.pedagogicalplan.LearningActivitySpace;
import eu.scy.core.model.pedagogicalplan.LearningActivitySpaceToolConfiguration;
import eu.scy.core.model.pedagogicalplan.PlannedELO;
import eu.scy.core.model.pedagogicalplan.TeacherRoleType;
import eu.scy.core.model.pedagogicalplan.WorkArrangementType;
import java.sql.Time;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 21.okt.2009
 * Time: 10:52:17
 */

@Entity
@Table(name="activity")
public class ActivityImpl extends BaseObjectImpl implements Activity {

    private Date startDate = null;
    private Date endDate = null;
    private Time startTime = null;
    private Time endTime = null;

    private LearningActivitySpace learningActivitySpace;

    private AnchorELO anchorELO;

    private Set <LearningActivitySpaceToolConfiguration> learningActivitySpaceToolConfigurations = new HashSet<LearningActivitySpaceToolConfiguration>();

    private Boolean autoaddToStudentPlan = false;

    private Integer expectedDurationInMinutes = 0;

    private WorkArrangementType workArrangementType = WorkArrangementType.INDIVIDUAL;
    private TeacherRoleType teacherRoleType = TeacherRoleType.OBSERVER;

    @Override
    public void setAnchorELO(AnchorELO anchorELO) {
        this.anchorELO = anchorELO;
    }


    @OneToOne  (targetEntity = AnchorELOImpl.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="anchorElo_primKey")
    public AnchorELO getAnchorELO() {
        return anchorELO;
    }

    @ManyToOne(targetEntity = LearningActivitySpaceImpl.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "learningActivitySpace_primKey")
    public LearningActivitySpace getLearningActivitySpace() {
        return learningActivitySpace;
    }

    @Override
    public void setLearningActivitySpace(LearningActivitySpace learningActivitySpace) {
        this.learningActivitySpace = learningActivitySpace;
    }

    @Transient
    //OneToMany(cascade = {CascadeType.ALL}, mappedBy = "activity", targetEntity = LearningActivitySpaceToolConfigurationImpl.class, fetch = FetchType.LAZY)
    public Set<LearningActivitySpaceToolConfiguration> getLearningActivitySpaceToolConfigurations() {
        return learningActivitySpaceToolConfigurations;
    }


    public void setLearningActivitySpaceToolConfigurations(Set<LearningActivitySpaceToolConfiguration> learningActivitySpaceToolConfigurations) {
        this.learningActivitySpaceToolConfigurations = learningActivitySpaceToolConfigurations;
    }

    @Transient
    public void addLearningActivitySpaceToolConfiguration(LearningActivitySpaceToolConfiguration learningActivitySpaceToolConfiguration) {
        getLearningActivitySpaceToolConfigurations().add(learningActivitySpaceToolConfiguration);
        learningActivitySpaceToolConfiguration.setActivity(this);
    }

    @Enumerated(EnumType.STRING)
    public WorkArrangementType getWorkArrangementType() {
        return workArrangementType;
    }

    public void setWorkArrangementType(WorkArrangementType workArrangementType) {
        this.workArrangementType = workArrangementType;
    }

    @Enumerated(EnumType.STRING)
    public TeacherRoleType getTeacherRoleType() {
        return teacherRoleType;
    }

    public void setTeacherRoleType(TeacherRoleType teacherRoleType) {
        this.teacherRoleType = teacherRoleType;
    }

    public Integer getExpectedDurationInMinutes() {
        return expectedDurationInMinutes;
    }

    public void setExpectedDurationInMinutes(Integer expectedDurationInMinutes) {
        this.expectedDurationInMinutes = expectedDurationInMinutes;
    }

    @Transient
    public List<PlannedELO> getPlannedELOs() {
        return null;
    }

    @Override
    public void setPlannedELOs(List<PlannedELO> plannedELOs) {
    }

    public Boolean getAutoaddToStudentPlan() {
        return autoaddToStudentPlan;
    }

    public void setAutoaddToStudentPlan(Boolean autoaddToStudentPlan) {
        this.autoaddToStudentPlan = autoaddToStudentPlan;
    }

    @Temporal(value = TemporalType.DATE)
    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Time getEndTime() {
        return endTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }

    @Temporal(value = TemporalType.DATE)
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }
}
