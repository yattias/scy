/*
 * ScyWindowControl.fx
 *
 * Created on 23-mrt-2009, 11:45:08
 */

package eu.scy.scywindows;

import eu.scy.elobrowser.main.EdgesManager;
import eu.scy.elobrowser.main.Roolo;
import eu.scy.elobrowser.tool.elofactory.ScyWindowContentCreator;
import eu.scy.elobrowser.tool.missionmap.Anchor;
import eu.scy.elobrowser.tool.missionmap.MissionMap;
import eu.scy.elobrowser.tool.missionmap.MissionModel;
import eu.scy.scywindows.ScyDesktop;
import eu.scy.scywindows.ScyWindow;
import eu.scy.scywindows.ScyWindowStyler;
import eu.scy.scywindows.WindowPositioner;
import eu.scy.scywindows.window_positions.WindowPositionerCenterMinimized;
import java.lang.Math;
import java.net.URI;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.util.Sequences;
import roolo.api.search.ISearchResult;
import roolo.cms.repository.mock.BasicMetadataQuery;
import roolo.elo.api.IMetadataKey;

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
    public var roolo:Roolo;
    public var edgesManager:EdgesManager;
    public var width:Number= 400 on replace {sizeChanged()};
    public var height:Number = 300 on replace {sizeChanged()};
//    public var size:Point2D on replace {sizeChanged()};
    public var forbiddenNodes:Node[];

    def interWindowSpace = 20.0;

    var activeAnchor = bind missionModel.activeAnchor on replace{
        activeAnchorChanged()
    };
    var activeAnchorWindow: ScyWindow;
    var windowPositioner: WindowPositioner = WindowPositionerCenterMinimized{
        width: bind width;
        height: bind height;
//        width: bind size.x;
//        height: bind size.y;
        forbiddenNodes: bind forbiddenNodes
    }
    var otherWindows:ScyWindow[];
    var relatedWindows:ScyWindow[];
    var titleKey:IMetadataKey;

    function sizeChanged(){
        positionWindows();
    }

    public function addOtherScyWindow(otherWindow:ScyWindow, autoLocate: Boolean){
        if (autoLocate) {
            otherWindow.translateX = (otherWindow.scene.width - otherWindow.layoutBounds.width) / 2 - otherWindow.layoutBounds.minX;
            otherWindow.translateY = (otherWindow.scene.height - otherWindow.layoutBounds.height) / 2 - otherWindow.layoutBounds.minY;
        }
        insert otherWindow into otherWindows;
    }

    public function addOtherScyWindow(otherWindow:ScyWindow){
        addOtherScyWindow(otherWindow, false);
    }

    public function newEloSaved(eloUri : URI){
        positionWindows();
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
             scyWindow = ScyWindow{
                    id: anchor.eloUri.toString();
                    title: anchor.title;
                    color: anchor.color;
                    scyWindowAttributes: [
                        missionMap.getAnchorAttribute(anchor)
                    ]
                }
           applyMetadataAttributes(scyWindow,anchor.eloUri);
           var content = scyWindowContentCreator.getScyWindowContent(anchor.eloUri,scyWindow);
            if (content != null){
                scyWindow.scyContent = content;
            }
            scyWindowStyler.style(scyWindow, anchor.eloUri);
        }
        return scyWindow;
    }

    function getScyWindow(eloUri:URI):ScyWindow{
        var scyWindow: ScyWindow = scyDesktop.findScyWindow(eloUri.toString());
        if (scyWindow == null){
             scyWindow = ScyWindow{
                    id: eloUri.toString();
//                    title: anchor.title;
                }
             applyMetadataAttributes(scyWindow,eloUri);
            var content = scyWindowContentCreator.getScyWindowContent(eloUri,scyWindow);
            if (content != null){
                scyWindow.scyContent = content;
            }
            scyWindowStyler.style(scyWindow, eloUri);
        }
        return scyWindow;
    }

    function applyMetadataAttributes(scyWindow:ScyWindow,eloUri:URI){
        var metadata = roolo.repository.retrieveMetadata(eloUri);
        if (metadata==null){
            println("Couldn't find elo {eloUri}");
            return;
        }
        var tk = getTitleKey();
        if (metadata.metadataKeyExists(tk)){
            var title = metadata.getMetadataValueContainer(tk).getValue();
            if (title!=null){
                scyWindow.title = title as String;
            }
        }
    }

    function getTitleKey():IMetadataKey{
        if (titleKey==null){
            titleKey = roolo.metadataTypeManager.getMetadataKey("title");
        }
        return titleKey;
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
            if (Sequences.indexOf(relatedWindows, scyWindow)>=0){
                return true;
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
        findRelatedWindows();
        for (window in relatedWindows){
            windowPositioner.addOtherWindow(window);
        }
        for (window in otherWindows){
            windowPositioner.addOtherWindow(window);
        }
        windowPositioner.positionWindows();
    }

    var usedEdgeSourceWindows:ScyWindow[]; // TODO, don't use a "global" variable for it
    function findRelatedWindows(){
        delete relatedWindows;
        delete usedEdgeSourceWindows;
        for (relationName in activeAnchor.relationNames){
            findRelatedWindows(relationName);
        }

    }

    function findRelatedWindows(relationName:String){
        var relationKey = roolo.metadataTypeManager.getMetadataKey(relationName);
        if (relationKey==null){
            println("couldn't find the metadataKey named: {relationName}");
            var keys = roolo.getKeys();
            return;
        }
        var query = new BasicMetadataQuery(relationKey,"EQUALS",activeAnchor.eloUri,null);
        var results = roolo.repository.search(query);
        println("Query: {query.toString()}, results: {results.size()}");
        for (r in results){
            var result = r as ISearchResult;
            var metadata = roolo.repository.retrieveMetadata(result.getUri());
            var annotatesValue = metadata.getMetadataValueContainer(relationKey).getValue();
            var scyWindow = getScyWindow(result.getUri());
            if (Sequences.indexOf(usedEdgeSourceWindows, scyWindow)<0){
                edgesManager.createEdge(scyWindow,activeAnchorWindow,relationName);
                insert scyWindow into usedEdgeSourceWindows;
            }
            scyDesktop.addScyWindow(scyWindow);

            insert scyWindow into relatedWindows;

            println("added related elo {result.getUri()}");
        }
    }


    function getAnchorDirection(anchor:Anchor):Number{
        return Math.atan2(anchor.yPos-activeAnchor.yPos , anchor.xPos-activeAnchor.xPos);
    }

}
