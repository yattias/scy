/*
 * MissionMap.fx
 *
 * Created on 18-mrt-2009, 15:29:35
 */

package eu.scy.elobrowser.tool.missionmap;

import eu.scy.scywindows.ScyWindowControl;
import eu.scy.elobrowser.tool.missionmap.Anchor;
import eu.scy.elobrowser.tool.missionmap.AnchorAttribute;
import eu.scy.elobrowser.tool.missionmap.AnchorDisplay;
import eu.scy.elobrowser.tool.missionmap.AnchorLink;
import eu.scy.elobrowser.tool.missionmap.MissionMap;
import eu.scy.elobrowser.tool.missionmap.MissionModel;
import eu.scy.elobrowser.tool.missionmap.MissionModelXml;
import java.lang.Object;
import java.net.URI;
import java.util.HashMap;
import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.Scene;
import javafx.stage.Stage;



/**
 * @author sikkenj
 */

public class MissionMap extends CustomNode {
    public var missionModel: MissionModel;
    public var scyWindowControl: ScyWindowControl;

    var anchorDisplays: AnchorDisplay[];
    var anchorMap = new HashMap();
    var anchorLinks: AnchorLink[];

    postinit{
        if (missionModel.activeAnchor!=null){
           getAnchorDisplay(missionModel.activeAnchor).selected = true;
        }
    }

    public override function create(): Node {
        anchorDisplays = createAnchorDisplays();
        anchorLinks = createAnchorLinks();
        return Group {
            content: [
                anchorLinks
                anchorDisplays
            ]
        };
    }

    function createAnchorDisplays():AnchorDisplay[]{
        for (anchor in missionModel.anchors){
            var anchorDisplay = AnchorDisplay{
                anchor: anchor,
                selectionAction: anchorSelected;
            }
            anchorMap.put(anchor,anchorDisplay);
            anchorDisplay
        }
    }

    function createAnchorLinks():AnchorLink[]{
        for (fromAnchor in anchorDisplays){
            for (toAnchor in fromAnchor.anchor.nextAnchors){
                AnchorLink{
                    fromAnchor: fromAnchor;
                    toAnchor: getAnchorDisplay(toAnchor);
                }
            }
        }
    }

    function getAnchorDisplay(anchor:Anchor):AnchorDisplay{
        return anchorMap.get(anchor) as AnchorDisplay;
    }


    public function anchorSelected(anchorDisplay:AnchorDisplay){
        if (missionModel.activeAnchor != null){
            var selectedAnchorDisplay = getAnchorDisplay(missionModel.activeAnchor);
            if (selectedAnchorDisplay == anchorDisplay){
            // already selected, but do reposition the windows again
                scyWindowControl.positionWindows();
                return;
            }
            selectedAnchorDisplay.selected = false;
        }
        anchorDisplay.selected = true;
        missionModel.activeAnchor = anchorDisplay.anchor;
    }

    public function getAnchorAttribute(anchor:Anchor):AnchorAttribute{
        AnchorAttribute{
            anchorDisplay: anchorMap.get(anchor) as AnchorDisplay
        }

    }


}

function run(){

    var anchor1 = Anchor{
        title: "1";
        xPos: 20;
        yPos: 20;
        color: Color.BLUE;
        eloUri:new URI("1");
    }
    var anchor2 = Anchor{
        title: "2";
        xPos: 60;
        yPos: 20;
        color: Color.GREEN;
        eloUri:new URI("2");
    }
    var anchor3 = Anchor{
        title: "3";
        xPos: 20;
        yPos: 60;
        color: Color.RED;
        eloUri:new URI("3");
    }
    var anchor4 = Anchor{
        title: "4";
        xPos: 60;
        yPos: 60;
        color: Color.ORANGE;
        eloUri:new URI("4");
    }
    anchor1.nextAnchors=[anchor2,anchor3];
    anchor2.nextAnchors=[anchor4];
    anchor3.nextAnchors=[anchor2];

    var missionModel = MissionModel{
        anchors: [anchor1,anchor2,anchor3,anchor4]
    }

    var missionModelXml = MissionModelXml.convertToXml(missionModel);
    var newMissionModel = MissionModelXml.convertToMissionModel(missionModelXml);
    var newMissionModelXml = MissionModelXml.convertToXml(newMissionModel);
    println(newMissionModelXml);

    var missionMap = MissionMap{
        missionModel: missionModel
    }


    Stage {
        title: "Test MissionMap"
        scene: Scene {
            width: 200
            height: 200
            content: [
                missionMap
            ]
        }
    }
}

