package eu.scy.core.persistence;

import eu.scy.core.model.pedagogicalplan.AnchorELO;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 18.mar.2010
 * Time: 21:33:40
 * To change this template use File | Settings | File Templates.
 */
public interface AnchorELODAO extends BaseDAO{
    AnchorELO getAnchorELO(String anchorEloId);
}
