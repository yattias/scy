package eu.scy.core;

import eu.scy.core.model.pedagogicalplan.AnchorELO;
import eu.scy.core.model.pedagogicalplan.Scenario;
import eu.scy.core.persistence.AnchorELODAO;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 18.mar.2010
 * Time: 21:35:32
 * To change this template use File | Settings | File Templates.
 */
public class AnchorELOServiceImpl extends BaseServiceImpl implements AnchorELOService {

    private AnchorELODAO anchorELODAO;

    public AnchorELODAO getAnchorELODAO() {
        return anchorELODAO;
    }

    public void setAnchorELODAO(AnchorELODAO anchorELODAO) {
        this.anchorELODAO = anchorELODAO;
    }

    @Override
    public AnchorELO getAnchorELO(String anchorEloId) {
        return getAnchorELODAO().getAnchorELO(anchorEloId);
    }

    @Override
    public List<AnchorELO> getAllAnchorELOsForScenario(Scenario scenario) {
        return getAnchorELODAO().getAllAnchorELOsForScenario(scenario);
    }
}
