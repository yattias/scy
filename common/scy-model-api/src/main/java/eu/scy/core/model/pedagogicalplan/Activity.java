package eu.scy.core.model.pedagogicalplan;

import java.sql.Time;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 10.sep.2009
 * Time: 13:37:56
 *
 * A LAS consists of a number of activities. The activity class represents one LAS activity.
 */
public interface Activity extends BaseObject {
    /**
     * Sets the anchor ELO that will be produced by this activity
     *
     * @param anchorELO the anchor ELO that this activity shall result inn
     */
    void setAnchorELO(AnchorELO anchorELO);

    /**
     * Gets the anchor ELO that will be produced by this activity
     *
     * @return the anchor ELO that this actitivy shall result in
     */
    AnchorELO getAnchorELO();

    /**
     * Provides access to the LAS associated with this activity
     *
     * @return the LAS associated with this activity
     */
    public LearningActivitySpace getLearningActivitySpace();

    /**
     * Sets the LAs associated with this actitivy
     *
     * @param learningActivitySpace the LAS associated with this activity
     */
    public void setLearningActivitySpace(LearningActivitySpace learningActivitySpace);

    /**
     * Provides access to tools configured for the activity
     *
     * @return the tools configured for the activity
     */
    public Set<LearningActivitySpaceToolConfiguration> getLearningActivitySpaceToolConfigurations();

    /**
     * Sets the tools configuration for the ativity
     *
     * @param learningActivitySpaceToolConfiguration the activity's LAS tool configuration
     */
    public void addLearningActivitySpaceToolConfiguration(LearningActivitySpaceToolConfiguration learningActivitySpaceToolConfiguration);

    /**
     * Sets the WorkArrangementType for the activity
     *
     * @param workArrangementType the activity's WorkArrangementType
     */
    public void setWorkArrangementType(WorkArrangementType workArrangementType);

    /**
     * Provides access to the activity's WorkArrangmentType
     *
     * @return the activity's WorkArrangmentType
     */
    public WorkArrangementType getWorkArrangementType();

    /**
     * Sets the activity's TeacherRoleType
     *
     * @param teacherRoleType the activity's TeacherRoletype
     */
    public void setTeacherRoleType(TeacherRoleType teacherRoleType);

    /**
     * Provides access to the activity's TeacherRoleType
     *
     * @return the activity's TeacherRoleType
     */
    public TeacherRoleType getTeacherRoleType();

    /**
     * The expected duration-time for this activity
     *
     * @return the activity's expected duration in minutes
     */
    public Integer getExpectedDurationInMinutes();

    /**
     * Sets the expected duration time in minutes for this activity
     *
     * @param expectedDurationInMinutes the activity's expected duration-time
     */
    public void setExpectedDurationInMinutes(Integer expectedDurationInMinutes);

    public List<PlannedELO> getPlannedELOs();
    public void setPlannedELOs(List <PlannedELO> plannedELOs);

    public void setLearningActivitySpaceToolConfigurations(Set<LearningActivitySpaceToolConfiguration> toolConfigurations);

    public Boolean getAutoaddToStudentPlan();
    public void setAutoaddToStudentPlan(Boolean autoaddToStudentPlan);

    public Date getStartDate();
    public void setStartDate(Date startDate);

    public Date getEndDate();
    public void setEndDate(Date endDate);

    public Time getStartTime();
    public void setStartTime(Time startTime);

    public Time getEndTime();
    public void setEndTime(Time endTime);    
}