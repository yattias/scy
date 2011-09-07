package eu.scy.agents;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: fschulz
 * Date: 01.09.11
 * Time: 12:06
 * To change this template use File | Settings | File Templates.
 */
public class MissionMapping {

    private Map<String, Mission> missionMapping;

    public MissionMapping() {
        missionMapping = new HashMap<String, Mission>();
        addMissionMapping("co2_2",Mission.MISSION1);
        addMissionMapping("co2_2_UT",Mission.MISSION1);
        addMissionMapping("co2_2_Oslo",Mission.MISSION1);
        addMissionMapping("eco",Mission.MISSION2);
        addMissionMapping("pizza2",Mission.MISSION3);
    }

    public synchronized Mission getMission(String missionId) {
        return missionMapping.get(missionId);
    }

    public synchronized void addMissionMapping(String missionId, Mission mission) {
        missionMapping.put(missionId, mission);
    }

    public void setMissionMapping(Map<String, Mission> mapping) {
        missionMapping = mapping;
    }
}
