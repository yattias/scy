package eu.scy.core;

import eu.scy.core.model.ScyBase;
import eu.scy.core.model.pedagogicalplan.AnchorELO;
import eu.scy.core.model.pedagogicalplan.Scenario;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 18.mar.2010
 * Time: 21:33:16
 * To change this template use File | Settings | File Templates.
 */
public interface AnchorELOService extends BaseService{
    AnchorELO getAnchorELO(String parameter);

    public List<AnchorELO> getAllAnchorELOsForScenario(Scenario scenario);

}
