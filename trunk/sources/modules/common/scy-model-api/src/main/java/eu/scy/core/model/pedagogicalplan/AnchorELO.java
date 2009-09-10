package eu.scy.core.model.pedagogicalplan;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 10.sep.2009
 * Time: 11:24:58
 */
public interface AnchorELO extends BaseObject, Assessable {

    public LearningActivitySpace getProducedBy();
    public void setProducedBy(LearningActivitySpace producedBy);

    public LearningActivitySpace getInputTo();
    public void setInputTo(LearningActivitySpace inputTo);

    public Boolean isIncludedInPortfolio();
    public void setIncludedInPortfolio(Boolean includedInPortfolio);

    public void setObligatoryInPortfolio(Boolean obligatoryInPortfolio);
    public Boolean isObligatoryInPortfolio();

}
