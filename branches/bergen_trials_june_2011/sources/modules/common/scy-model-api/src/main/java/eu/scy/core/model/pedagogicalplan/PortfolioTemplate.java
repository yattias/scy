package eu.scy.core.model.pedagogicalplan;

import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 10.sep.2009
 * Time: 11:30:57
 * To change this template use File | Settings | File Templates.
 */
public interface PortfolioTemplate extends BaseObject{

    public Set<AnchorELO> getAnchorElos();
    public void setAnchorElos(Set<AnchorELO> anchorElos);

}
