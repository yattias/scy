package eu.scy.core.roolo;

import eu.scy.common.mission.MissionRuntimeElo;
import eu.scy.common.scyelo.ScyElo;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 14.des.2010
 * Time: 06:35:00
 * To change this template use File | Settings | File Templates.
 */
public class RuntimeELOServiceImpl extends BaseELOServiceImpl implements RuntimeELOService {

    @Override
    public List getRuntimeElosForUser(String userName) {
        List runtimeElos = new LinkedList();
        List<ScyElo> runtimeModels = getRuntimeElos(null);
        for (int i = 0; i < runtimeModels.size(); i++) {
            MissionRuntimeElo missionRuntimeElo = new MissionRuntimeElo(runtimeModels.get(i).getElo(), this);
            if (missionRuntimeElo != null) {
                String missionRunningHAHAHA = missionRuntimeElo.getMissionRunning();
                if (missionRunningHAHAHA != null && missionRunningHAHAHA.equals(userName)) {
                    runtimeElos.add(missionRuntimeElo);
                }
            }
        }
        return runtimeElos;
    }



}
