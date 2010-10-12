package eu.scy.server.utils;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 22.feb.2010
 * Time: 13:34:52
 * To change this template use File | Settings | File Templates.
 */
public class ImportedXMLMission {

    private BasicMissionMap basicMissionMap;

    private String id;
    private List basicMissionAnchors;

    public BasicMissionMap getBasicMissionMap() {
        return basicMissionMap;
    }

    public void setBasicMissionMap(BasicMissionMap basicMissionMap) {
        this.basicMissionMap = basicMissionMap;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setBasicMissionAnchors(List basicMissionAnchors) {
        this.basicMissionAnchors = basicMissionAnchors;
    }

    public List getBasicMissionAnchors() {
        return basicMissionAnchors;
    }

    public   BasicMissionAnchor getAnchor(String id) {
        List anchors = getBasicMissionAnchors();
        for (int i = 0; i < anchors.size(); i++) {
            BasicMissionAnchor basicMissionAnchor = (BasicMissionAnchor) anchors.get(i);
            if(basicMissionAnchor.getId().equals(id)) return basicMissionAnchor;
        }
        return null;
    }
}
