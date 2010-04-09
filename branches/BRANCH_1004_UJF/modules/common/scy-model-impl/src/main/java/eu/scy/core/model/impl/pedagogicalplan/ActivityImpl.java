package eu.scy.core.model.impl.pedagogicalplan;

import eu.scy.core.model.pedagogicalplan.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 21.okt.2009
 * Time: 10:52:17
 */

@Entity
@Table(name="activity")
public class ActivityImpl extends BaseObjectImpl implements Activity {

    private LearningActivitySpace learningActivitySpace;

    private AnchorELO anchorELO;

    private Set <LearningActivitySpaceToolConfiguration> learningActivitySpaceToolConfigurations = new HashSet<LearningActivitySpaceToolConfiguration>();

    private Boolean autoaddToStudentPlan = false;

    private Integer expectedDurationInMinutes = 0;

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

    @Override
    public void setWorkArrangementType(WorkArrangementType workArrangementType) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Transient
    public WorkArrangementType getWorkArrangementType() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setTeacherRoleType(TeacherRoleType teacherRoleType) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Transient
    public TeacherRoleType getTeacherRoleType() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Integer getExpectedDurationInMinutes() {
        return expectedDurationInMinutes;
    }

    public void setExpectedDurationInMinutes(Integer expectedDurationInMinutes) {
        this.expectedDurationInMinutes = expectedDurationInMinutes;
    }

    @Transient
    public List<PlannedELO> getPlannedELOs() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setPlannedELOs(List<PlannedELO> plannedELOs) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public Boolean getAutoaddToStudentPlan() {
        return autoaddToStudentPlan;
    }

    public void setAutoaddToStudentPlan(Boolean autoaddToStudentPlan) {
        this.autoaddToStudentPlan = autoaddToStudentPlan;
    }
}
