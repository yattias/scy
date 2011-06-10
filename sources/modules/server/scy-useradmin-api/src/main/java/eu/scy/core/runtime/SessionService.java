package eu.scy.core.runtime;

import eu.scy.common.mission.MissionSpecificationElo;
import eu.scy.core.model.transfer.LasActivityInfo;
import eu.scy.core.model.transfer.PedagogicalPlanTransfer;
import eu.scy.core.model.transfer.UserActivityInfo;

import java.net.URI;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 24.mai.2011
 * Time: 15:12:49
 * To change this template use File | Settings | File Templates.
 */
public interface SessionService {
    List getActiveStudentsOnMission(MissionSpecificationElo missionSpecificationElo);

    public List findElosFor(String username);

    public UserActivityInfo getUserActivityInfo(MissionSpecificationElo missionSpecificationElo, String userName);

    List getCurrentStudentActivity(MissionSpecificationElo missionSpecificationElo);

    List<LasActivityInfo> getActiveLasses(MissionSpecificationElo missionSpecificationElo);
}
