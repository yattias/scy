package eu.scy.core;

import eu.scy.common.mission.MissionSpecificationElo;
import eu.scy.core.roolo.RooloAccessor;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 14.des.2010
 * Time: 05:55:27
 * To change this template use File | Settings | File Templates.
 */
public interface BaseELOService extends RooloAccessor {

        List getRuntimeElos(MissionSpecificationElo missionSpecificationElo);

}
