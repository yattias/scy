package eu.scy.core.model.impl.pedagogicalplan;

import eu.scy.core.model.pedagogicalplan.*;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 05.nov.2009
 * Time: 09:41:46
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name="anchorelo")
public class AnchorELOImpl extends BaseObjectImpl implements AnchorELO {

    private Assessment assessment;

    private Activity producedBy;

    private LearningActivitySpace inputTo;

    private Boolean includedInPortfolio;

    private Boolean obligatoryInPortfolio;

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
    public Boolean getIncludedInPortfolio() {
        return includedInPortfolio;
    }

    public void setIncludedInPortfolio(Boolean includedInPortfolio) {
        this.includedInPortfolio = includedInPortfolio;
    }

    public void setObligatoryInPortfolio(Boolean obligatoryInPortfolio) {
        this.obligatoryInPortfolio = obligatoryInPortfolio;
    }

    @Transient
    public Boolean getObligatoryInPortfolio() {
        return this.obligatoryInPortfolio;
    }
}
