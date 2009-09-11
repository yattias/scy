package eu.scy.core.model.pedagogicalplan;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 10.sep.2009
 * Time: 11:24:58
 *
 * The AnchorELO represents a planned ELO that should be the outcome of for instance a LAS, one Activity, etc, and that the teacher can decide should be included in the portfolio.
 */
public interface AnchorELO extends BaseObject, Assessable {

    /**
     * Returns the LAS that the AnchorELo should be produced by
     *
     * @return the LAS that produced the AnchorELO
     */
    public LearningActivitySpace getProducedBy();

    /**
     * Sets the LAS  that produced the AnchorELO
     *
     * @param producedBy the LAS that produces the AnchorELO
     */
    public void setProducedBy(LearningActivitySpace producedBy);

    public LearningActivitySpace getInputTo();
    public void setInputTo(LearningActivitySpace inputTo);

    public Boolean isIncludedInPortfolio();
    public void setIncludedInPortfolio(Boolean includedInPortfolio);

    public void setObligatoryInPortfolio(Boolean obligatoryInPortfolio);
    public Boolean isObligatoryInPortfolio();

}
