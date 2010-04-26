package eu.scy.core.model.impl.pedagogicalplan;

import eu.scy.core.model.pedagogicalplan.PedagogicalPlan;
import eu.scy.core.model.pedagogicalplan.PedagogicalPlanTemplate;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

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

    private String overallSCYLabScaffoldingLevel= null;
    private String overallMissionContentScaffoldingLevel = null;


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
}