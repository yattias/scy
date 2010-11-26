package eu.scy.core.model.impl.pedagogicalplan;

import eu.scy.core.model.ImageRef;
import eu.scy.core.model.impl.ImageRefImpl;
import eu.scy.core.model.pedagogicalplan.AnchorELO;
import eu.scy.core.model.pedagogicalplan.PedagogicalPlan;
import eu.scy.core.model.pedagogicalplan.PedagogicalPlanTemplate;

import javax.persistence.*;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 09.des.2009
 * Time: 06:27:57
 */
@Entity
@DiscriminatorValue("pedagogicalPlan")
public class PedagogicalPlanImpl extends PedagogicalPlanBaseImpl implements PedagogicalPlan {

    private PedagogicalPlanTemplate pedagogicalPlanTemplate = null;

    private Boolean published = false;
    private Boolean makeAllAssignedStudentsBuddies = false;
    private Integer minimumNumberOfAnchorELOsInPortfolio = 0;
    private Integer maximumNumberOfAnchorELOsInPortfolio = 0;
    private Boolean limitNumberOfELOsAvailableForPeerAssessment = false;

    private String overallSCYLabScaffoldingLevel= null;
    private String overallMissionContentScaffoldingLevel = null;
    private String missionURI;
    private List anchorELOsToBePeerAssessed;
    private ImageRef assessmentScoreIcon;


    @ManyToOne(targetEntity = PedagogicalPlanTemplateImpl.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "pedagogicalPlanTemplate_primKey")
    public PedagogicalPlanTemplate getTemplate() {
        return pedagogicalPlanTemplate;
    }

    public void setTemplate(PedagogicalPlanTemplate pedagogicalPlanTemplate) {
        this.pedagogicalPlanTemplate = pedagogicalPlanTemplate;
    }


    @Override
    public Boolean getPublished() {
        return published;
    }

    @Override
    public void setPublished(Boolean published) {
        this.published = published;
    }

    @Override
    public Boolean getMakeAllAssignedStudentsBuddies() {
        return makeAllAssignedStudentsBuddies;
    }

    @Override
    public void setMakeAllAssignedStudentsBuddies(Boolean makeAllAssignedStudentsBuddies) {
        this.makeAllAssignedStudentsBuddies = makeAllAssignedStudentsBuddies;
    }

    public Integer getMaximumNumberOfAnchorELOsInPortfolio() {
        return maximumNumberOfAnchorELOsInPortfolio;
    }

    public void setMaximumNumberOfAnchorELOsInPortfolio(Integer maximumNumberOfAnchorELOsInPortfolio) {
        this.maximumNumberOfAnchorELOsInPortfolio = maximumNumberOfAnchorELOsInPortfolio;
    }

    public Integer getMinimumNumberOfAnchorELOsInPortfolio() {
        return minimumNumberOfAnchorELOsInPortfolio;
    }

    public void setMinimumNumberOfAnchorELOsInPortfolio(Integer minimumNumberOfAnchorELOsInPortfolio) {
        this.minimumNumberOfAnchorELOsInPortfolio = minimumNumberOfAnchorELOsInPortfolio;
    }

    public String getOverallSCYLabScaffoldingLevel() {
        return overallSCYLabScaffoldingLevel;
    }

    public void setOverallSCYLabScaffoldingLevel(String overallSCYLabScaffoldingLevel) {
        this.overallSCYLabScaffoldingLevel = overallSCYLabScaffoldingLevel;
    }

    public String getOverallMissionContentScaffoldingLevel() {
        return overallMissionContentScaffoldingLevel;
    }

    public void setOverallMissionContentScaffoldingLevel(String overallMissionContentScaffoldingLevel) {
        this.overallMissionContentScaffoldingLevel = overallMissionContentScaffoldingLevel;
    }

    @Override
    public Boolean getLimitNumberOfELOsAvailableForPeerAssessment() {
        return limitNumberOfELOsAvailableForPeerAssessment;
    }

    @Override
    public void setLimitNumberOfELOsAvailableForPeerAssessment(Boolean limitNumberOfELOsAvailableForPeerAssessment) {
        this.limitNumberOfELOsAvailableForPeerAssessment = limitNumberOfELOsAvailableForPeerAssessment;
    }

    @Override
    @ManyToOne (targetEntity = ImageRefImpl.class)
    @JoinColumn (name="imageRef_primKey")
    public ImageRef getAssessmentScoreIcon() {
        return assessmentScoreIcon;
    }

    @Override
    public void setAssessmentScoreIcon(ImageRef assessmentScoreIcon) {
        this.assessmentScoreIcon = assessmentScoreIcon;
    }

    /**
     * the uri of the mission elo this plan is related to
     * @return
     */
    @Override
    public String getMissionURI() {
        return missionURI;
    }

    @Override
    public void setMissionURI(String missionURI) {
        this.missionURI = missionURI;
    }
}