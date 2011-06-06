package eu.scy.core.persistence;

import eu.scy.core.model.pedagogicalplan.AnchorELO;
import eu.scy.core.model.pedagogicalplan.Scenario;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 18.mar.2010
 * Time: 21:33:40
 */
public interface AnchorELODAO extends SCYBaseDAO {
    AnchorELO getAnchorELO(String anchorEloId);

    List<AnchorELO> getAllAnchorELOsForScenario(Scenario scenario);
}
