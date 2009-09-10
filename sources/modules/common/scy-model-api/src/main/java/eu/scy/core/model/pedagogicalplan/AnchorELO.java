package eu.scy.core.model.pedagogicalplan;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 10.sep.2009
 * Time: 11:24:58
 */
public interface AnchorELO extends BaseObject, Assessable {

    public LearningActivitySpace getProducedBy();

    public LearningActivitySpace getInputTo();

    public Boolean isIncludedInPortfolio();
    public void setIsIncludedInPortfolio(Boolean isIncludedInPortfolio);

}
