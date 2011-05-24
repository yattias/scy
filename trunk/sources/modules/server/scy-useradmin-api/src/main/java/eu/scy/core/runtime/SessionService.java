package eu.scy.core.runtime;

import eu.scy.common.mission.MissionSpecificationElo;

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
}
