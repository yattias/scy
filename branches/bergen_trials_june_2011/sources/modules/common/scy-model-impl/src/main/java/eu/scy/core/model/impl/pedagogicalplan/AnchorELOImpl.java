package eu.scy.core.model.impl.pedagogicalplan;

import eu.scy.core.model.pedagogicalplan.*;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 05.nov.2009
 * Time: 09:41:46
 */
@Entity
@Table(name="anchorelo")
public class AnchorELOImpl extends BaseObjectImpl implements AnchorELO {

    private String humanReadableName;

    private Assessment assessment = null;

    private Activity producedBy = null;

    private LearningActivitySpace inputTo = null;

    private LearningActivitySpace outputFrom = null;

    private boolean includedInPortfolio = false;

    private boolean obligatoryInPortfolio = false;

    private String missionMapId;

    private int xPos;
    private int yPos;

    public void setAssessment(Assessment assessment) {
        this.assessment = assessment;
    }

    @OneToOne(targetEntity = AssessmentImpl.class, fetch=FetchType.LAZY, cascade=CascadeType.ALL)
    @JoinColumn (name = "assessment_primKey")
    public Assessment getAssessment() {
        return assessment;
    }

    @OneToOne(targetEntity = ActivityImpl.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name="producedBy_primKey")
    public Activity getProducedBy() {
        return producedBy;
    }

    public void setProducedBy(Activity producedBy) {
        this.producedBy = producedBy;
    }

    @OneToOne(targetEntity = LearningActivitySpaceImpl.class, cascade = CascadeType.ALL)
    @JoinColumn(name="inputTo_primKey")
    public LearningActivitySpace getInputTo() {
        return inputTo;
    }

    public void setInputTo(LearningActivitySpace inputTo) {
        this.inputTo = inputTo;
    }

    public Boolean getIncludedInPortfolio() {
        return includedInPortfolio;
    }

    public void setIncludedInPortfolio(Boolean includedInPortfolio) {
        this.includedInPortfolio = includedInPortfolio;
    }

    public void setObligatoryInPortfolio(Boolean obligatoryInPortfolio) {
        this.obligatoryInPortfolio = obligatoryInPortfolio;
    }

    public Boolean getObligatoryInPortfolio() {
        return this.obligatoryInPortfolio;
    }

    public Integer getXPos() {
        return xPos;
    }

    public void setXPos(Integer xPos) {
        this.xPos = xPos;
    }

    public Integer getYPos() {
        return yPos;
    }

    public void setYPos(Integer yPos) {
        this.yPos = yPos;
    }

    @Override
    public String getMissionMapId() {
        return missionMapId;
    }

    @Override
    public void setMissionMapId(String missionMapId) {
        this.missionMapId = missionMapId;
    }

    @Override
    public String getHumanReadableName() {
        return humanReadableName;
    }

    @Override
    public void setHumanReadableName(String humanReadableName) {
        this.humanReadableName = humanReadableName;
    }
}