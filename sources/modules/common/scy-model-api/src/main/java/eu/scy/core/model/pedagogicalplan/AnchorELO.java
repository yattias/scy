package eu.scy.core.model.pedagogicalplan;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 10.sep.2009
 * Time: 11:24:58
 * To change this template use File | Settings | File Templates.
 */
public interface AnchorELO {

    public LearningActivitySpace getProducedBy();

    public LearningActivitySpace getInputTo();

    public Boolean isIncludedInPortfolio();
    public void setIsIncludedInPortfolio(Boolean isIncludedInPortfolio);

}
