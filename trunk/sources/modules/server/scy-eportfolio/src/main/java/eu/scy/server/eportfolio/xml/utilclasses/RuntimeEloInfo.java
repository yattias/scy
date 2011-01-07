package eu.scy.server.eportfolio.xml.utilclasses;

import eu.scy.common.mission.MissionAnchor;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 08.des.2010
 * Time: 14:51:59
 * To change this template use File | Settings | File Templates.
 */
public class RuntimeEloInfo {

    private String missionName;
    private String uri;
    private List obligatoryElos = new LinkedList();

    public String getMissionName() {
        return missionName;
    }

    public void setMissionName(String missionName) {
        this.missionName = missionName;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public List getObligatoryElos() {
        return obligatoryElos;
    }

    public void setObligatoryElos(List obligatoryElos) {
        this.obligatoryElos = obligatoryElos;
    }

    public void addObligatoryElo(MissionAnchor missionAnchor) {
        obligatoryElos.add(missionAnchor);
    }
}
