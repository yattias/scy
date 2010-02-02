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

    private Assessment assessment = null;

    private Activity producedBy = null;

    private LearningActivitySpace inputTo = null;

    private LearningActivitySpace outputFrom = null;

    private boolean includedInPortfolio = false;

    private boolean obligatoryInPortfolio = false;

    private int xPos;
    private int yPos;

    public void setAssessment(Assessment assessment) {
        this.assessment = assessment;
    }
    @Transient
    public Assessment getAssessment() {
        return assessment;
    }

    @Transient
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

    @Transient
    public boolean getIncludedInPortfolio() {
        return includedInPortfolio;
    }

    public void setIncludedInPortfolio(boolean includedInPortfolio) {
        this.includedInPortfolio = includedInPortfolio;
    }

    public void setObligatoryInPortfolio(boolean obligatoryInPortfolio) {
        this.obligatoryInPortfolio = obligatoryInPortfolio;
    }

    @Transient
    public boolean getObligatoryInPortfolio() {
        return this.obligatoryInPortfolio;
    }

    public int getXPos() {
        return xPos;
    }

    public void setXPos(int xPos) {
        this.xPos = xPos;
    }

    public int getYPos() {
        return yPos;
    }

    public void setYPos(int yPos) {
        this.yPos = yPos;
    }


}