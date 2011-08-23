package eu.scy.core.roolo;

import eu.scy.common.mission.MissionRuntimeElo;
import eu.scy.core.BaseELOService;
import eu.scy.core.model.transfer.Portfolio;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 04.aug.2011
 * Time: 08:36:39
 * To change this template use File | Settings | File Templates.
 */
public interface PortfolioELOService extends BaseELOService {

    public Portfolio getPortfolio(MissionRuntimeElo missionRuntimeElo, String username);

}
