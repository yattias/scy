/*
 * ScyWindowControl.fx
 *
 * Created on 23-mrt-2009, 11:45:08
 */

package eu.scy.scywindows;

import eu.scy.scywindows.ScyDesktop;
import eu.scy.scywindows.ScyWindow;
import eu.scy.scywindows.window_positions.WindowPositionerCenter;
import eu.scy.elobrowser.tool.elofactory.ScyWindowContentCreator;
import eu.scy.elobrowser.tool.missionmap.Anchor;
import eu.scy.elobrowser.tool.missionmap.MissionMap;
import eu.scy.elobrowser.tool.missionmap.MissionModel;
import java.lang.Math;
import java.net.URI;
import javafx.stage.Stage;
import javafx.util.Sequences;

/**
 * @author sikkenj
 */

 // place your code here
public class ScyWindowControl{
    public var missionModel: MissionModel;
    public var missionMap: MissionMap;
    public var scyWindowContentCreator: ScyWindowContentCreator;
    public var scyWindowStyler: ScyWindowStyler;
    public var scyDesktop: ScyDesktop;
    public var stage: Stage;

    def interWindowSpace = 20.0;

    var activeAnchor = bind missionModel.activeAnchor on replace{
        activeAnchorChanged()
    };
    var activeAnchorWindow: ScyWindow;
    var windowPositioner: WindowPositioner = WindowPositionerCenter{
        width: bind stage.width - 1;
        height: bind stage.height - 26;
    }
    var otherWindows:ScyWindow[];

    public function addOtherScyWindow(otherWindow:ScyWindow){
        insert otherWindow into otherWindows;
    }


    function activeAnchorChanged(){
        activeAnchorWindow = getScyWindow(activeAnchor);
        if (activeAnchorWindow != null){
            scyDesktop.addScyWindow(activeAnchorWindow);
            positionWindows();
            scyDesktop.checkVisibilityScyWindows(isRelevantScyWindow);
            scyDesktop.activateScyWindow(activeAnchorWindow);
        }
    }

    function getScyWindow(anchor:Anchor):ScyWindow{
        var scyWindow: ScyWindow = scyDesktop.findScyWindow(anchor.eloUri.toString());
        if (scyWindow == null){
            var content = scyWindowContentCreator.getScyWindowContent(anchor.eloUri);
            if (content != null){
                scyWindow = ScyWindow{
                    id: anchor.eloUri.toString();
                    title: anchor.title;
                    color: anchor.color;
                    scyContent: content;
//                    translateX: 100;
                    //                    translateY: 100;
                    scyWindowAttributes: [
                        missionMap.getAnchorAttribute(anchor)
                    ]
                }
            }
            scyWindowStyler.style(scyWindow, anchor.eloUri);
        }
        return scyWindow;
    }

    function isRelevantScyWindow(scyWindow:ScyWindow):Boolean{
        if (scyWindow.id != null){
            var scyWindowUri = new URI(scyWindow.id);
            if (scyWindowUri == activeAnchor.eloUri){
                return true;
            }
            for (anchor in activeAnchor.nextAnchors){
                if (scyWindowUri == anchor.eloUri){
                    return true;
                }
            }
            if (Sequences.indexOf(otherWindows, scyWindow)>=0){
                return true;
            }

        }
        return false;
    }

    public function positionWindows(){
        windowPositioner.clearWindows();
        windowPositioner.setCenterWindow(activeAnchorWindow);
        for (anchor in activeAnchor.nextAnchors){
            var scyWindow = getScyWindow(anchor);
            scyDesktop.addScyWindow(scyWindow);
            var anchorDirection = getAnchorDirection(anchor);
            windowPositioner.addLinkedWindow(scyWindow, anchorDirection);
        }
        for (window in otherWindows){
            windowPositioner.addOtherWindow(window);
        }
        windowPositioner.positionWindows();
    }

    function getAnchorDirection(anchor:Anchor):Number{
        return Math.atan2(anchor.yPos-activeAnchor.yPos , anchor.xPos-activeAnchor.xPos);
    }

}
