package eu.scy.core.model.impl.pedagogicalplan;

import eu.scy.core.model.pedagogicalplan.*;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 21.okt.2009
 * Time: 10:52:17
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(name="activity")
public class ActivityImpl extends BaseObjectImpl implements Activity {

    private LearningActivitySpace learningActivitySpace;

    private AnchorELO anchorELO;

    @Override
    public void setAnchorELO(AnchorELO anchorELO) {
        this.anchorELO = anchorELO;
    }


    @OneToOne  (targetEntity = AnchorELOImpl.class, cascade = CascadeType.ALL)
    @JoinColumn(name="activity_primKey")
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
    public Set<LearningActivitySpaceToolConfiguration> getLearningActivitySpaceToolConfigurations() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setLearningActivitySpaceToolConfiguration(LearningActivitySpaceToolConfiguration learningActivitySpaceToolConfiguration) {
        //To change body of implemented methods use File | Settings | File Templates.
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

    @Transient
    public int expectedDurationInMinutes() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setExpectedDurationInMinutes(int expectedDurationInMinutes) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Transient
    public List<PlannedELO> getPlannedELOs() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setPlannedELOs(List<PlannedELO> plannedELOs) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
