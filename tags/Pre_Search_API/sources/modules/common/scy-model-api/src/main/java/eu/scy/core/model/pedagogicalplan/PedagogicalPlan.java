package eu.scy.core.model.pedagogicalplan;

import eu.scy.core.model.ImageRef;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 10.sep.2009
 * Time: 11:00:58
 */
public interface PedagogicalPlan extends PedagogicalPlanBase, BasedOnTemplate{


    /**
     * Sets the template this pedagogical plan is based on. The plan will inherit all default values from the template
     * @return
     */
    public PedagogicalPlanTemplate getTemplate();
    public void setTemplate(PedagogicalPlanTemplate template);

    Boolean getPublished();
    void setPublished(Boolean published);

    Boolean getMakeAllAssignedStudentsBuddies();
    void setMakeAllAssignedStudentsBuddies(Boolean makeAllAssignedStudentsBuddies);

    public Integer getMaximumNumberOfAnchorELOsInPortfolio();
    public void setMaximumNumberOfAnchorELOsInPortfolio(Integer maximumNumberOfAnchorELOsInPortfolio);

    public Integer getMinimumNumberOfAnchorELOsInPortfolio();
    public void setMinimumNumberOfAnchorELOsInPortfolio(Integer minimumNumberOfAnchorELOsInPortfolio);

    public String getOverallSCYLabScaffoldingLevel();
    public void setOverallSCYLabScaffoldingLevel(String overallSCYLabScaffoldingLevel);

    public String getOverallMissionContentScaffoldingLevel();
    public void setOverallMissionContentScaffoldingLevel(String overallMissionContentScaffoldingLevel);

    Boolean getLimitNumberOfELOsAvailableForPeerAssessment();
    void setLimitNumberOfELOsAvailableForPeerAssessment(Boolean limitNumberOfELOsAvailableForPeerAssessment);

    ImageRef getAssessmentScoreIcon();

    void setAssessmentScoreIcon(ImageRef assessmentScoreIcon);

    String getMissionURI();

    void setMissionURI(String missionURI);
}
