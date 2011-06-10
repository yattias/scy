package eu.scy.core.roolo;

import eu.scy.common.mission.MissionRuntimeElo;
import eu.scy.common.scyelo.Access;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.core.model.transfer.UserActivityInfo;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 14.des.2010
 * Time: 06:35:00
 * To change this template use File | Settings | File Templates.
 */
public class RuntimeELOServiceImpl extends BaseELOServiceImpl implements RuntimeELOService {

    private static Logger log = Logger.getLogger("RuntimeELOServiceImpl.class");

    @Override
    public List getRuntimeElosForUser(String userName) {
        List runtimeElos = new LinkedList();
        List<ScyElo> runtimeModels = getRuntimeElos(null);
        for (int i = 0; i < runtimeModels.size(); i++) {
            MissionRuntimeElo missionRuntimeElo = new MissionRuntimeElo(runtimeModels.get(i).getElo(), this);
            if (missionRuntimeElo != null) {
                String missionRunningHAHAHA = missionRuntimeElo.getUserRunningMission();
                if (missionRunningHAHAHA != null && missionRunningHAHAHA.equals(userName)) {
                    runtimeElos.add(missionRuntimeElo);
                }
            }
        }
        return runtimeElos;
    }

    @Override
    public void deleteRuntimeElosForUser(String userName) {
        log.info("deleting runtime elos for user: " + userName);
        List runtimeElos = getRuntimeElosForUser(userName);
        for (int i = 0; i < runtimeElos.size(); i++) {
            MissionRuntimeElo missionRuntimeElo = (MissionRuntimeElo) runtimeElos.get(i);
            log.info("DELETING MISSION: " + missionRuntimeElo.getUri());
            missionRuntimeElo.setAccess(Access.DELETED);
            missionRuntimeElo.updateElo();

        }
    }

    


}
