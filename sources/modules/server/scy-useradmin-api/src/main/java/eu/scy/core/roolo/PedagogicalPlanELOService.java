package eu.scy.core.roolo;

import eu.scy.common.mission.MissionSpecificationElo;
import eu.scy.core.BaseELOService;
import eu.scy.core.model.transfer.PedagogicalPlanTransfer;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 09.feb.2011
 * Time: 21:40:45
 * To change this template use File | Settings | File Templates.
 */
public interface PedagogicalPlanELOService extends BaseELOService {

    public PedagogicalPlanTransfer getPedagogicalPlanForMission(MissionSpecificationElo missionSpecificationElo);

    void initializePedagogicalPlanWithLasses(MissionSpecificationElo missionSpecificationElo);

    void clearMissionPlanning(MissionSpecificationElo missionSpecificationElo);
}
