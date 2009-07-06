/*
 * ScyWindowControl.fx
 *
 * Created on 23-mrt-2009, 11:45:08
 */

package eu.scy.client.desktop.scydesktop.scywindows;

import eu.scy.client.desktop.scydesktop.missionmap.AnchorFX;
import eu.scy.client.desktop.scydesktop.missionmap.MissionMap;
import eu.scy.client.desktop.scydesktop.missionmap.MissionModelFX;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import eu.scy.client.desktop.scydesktop.scywindows.WindowStyler;
import eu.scy.client.desktop.scydesktop.scywindows.window_positions.WindowPositionerCenterMinimized;
import eu.scy.client.desktop.scydesktop.scywindows.WindowPositioner;
import java.lang.Math;
import java.net.URI;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.util.Sequences;

import eu.scy.client.desktop.scydesktop.scywindows.scydesktop.EloInfoControl;

import org.apache.log4j.Logger;

import eu.scy.client.desktop.scydesktop.elofactory.WindowContentFactory;
//import roolo.api.IExtensionManager;
//import roolo.api.IRepository;
//import roolo.api.search.ISearchResult;
//import roolo.cms.repository.mock.BasicMetadataQuery;
//import roolo.elo.api.IMetadataKey;
//import roolo.elo.api.IMetadataTypeManager;

/**
 * @author sikkenj
 */

 // place your code here
var logger = Logger.getLogger("eu.scy.client.desktop.scydesktop.scywindows.ScyWindowControl");

public class ScyWindowControl{
   public var missionModel: MissionModelFX;
   public var missionMap: MissionMap;
   public var windowContentFactory: WindowContentFactory;
   public var windowStyler: WindowStyler;
   public var scyDesktop: WindowManager;
   public var stage: Stage;
   public var eloInfoControl: EloInfoControl;
//   public var metadataTypeManager: IMetadataTypeManager;
//   public var extensionManager: IExtensionManager;
//   public var repository: IRepository;
   //   public var edgesManager: EdgesManager;
   public var width: Number= 400 on replace {
      sizeChanged()
   };
   public var height: Number = 300 on replace {
      sizeChanged()
   };
   //    public var size:Point2D on replace {sizeChanged()};
   public var forbiddenNodes: Node[];

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
   var otherWindows: ScyWindow[];
   var relatedWindows: ScyWindow[];
   var placedWindows: ScyWindow[];
//   var titleKey: IMetadataKey;

   function sizeChanged(){
      //logger.info("new size ({width}*{height})");
      positionWindows(false);
   }

   public function addOtherScyWindow(otherWindow:ScyWindow, autoLocate: Boolean){
      if (autoLocate) {
         otherWindow.translateX = (otherWindow.scene.width - otherWindow.layoutBounds.width) / 2 - otherWindow.layoutBounds.minX;
         otherWindow.translateY = (otherWindow.scene.height - otherWindow.layoutBounds.height) / 2 - otherWindow.layoutBounds.minY;
      }
        insert otherWindow into otherWindows;
   }

   public function addOtherScyWindow(otherWindow:ScyWindow){
      println("addOtherScyWindow, id:{otherWindow.id}");
      addOtherScyWindow(otherWindow, false);
   }

//   public function newEloSaved(eloUri : URI){
//      println("newEloSaved: {eloUri}");
//      var eloType = extensionManager.getType(eloUri);
//      if ("scy/melo" == eloType){
//         var eloWindow = getScyWindow(eloUri);
//         scyDesktop.addScyWindow(eloWindow);
//         addOtherScyWindow(eloWindow);
//         positionWindows(true);
//      }
//       else {
//         addRelatedWindow(eloUri);
//         positionWindows(true);
//      }
//   }


   public function activeAnchorChanged(){
      if (activeAnchor!=null){
         activeAnchorWindow = getScyWindow(activeAnchor);
         if (activeAnchorWindow != null){
            scyDesktop.addScyWindow(activeAnchorWindow);
            positionWindows();
   // TODO         scyDesktop.checkVisibilityScyWindows(isRelevantScyWindow);
            scyDesktop.activateScyWindow(activeAnchorWindow);
         }
      }
   }

   function getScyWindow(anchor:AnchorFX):ScyWindow{
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
         windowContentFactory.fillWindowContent(anchor.eloUri,scyWindow);
         windowStyler.style(scyWindow, anchor.eloUri);
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
         windowContentFactory.fillWindowContent(eloUri,scyWindow);
         windowStyler.style(scyWindow, eloUri);
      }
      return scyWindow;
   }

   function applyMetadataAttributes(scyWindow:ScyWindow,eloUri:URI){
      var title = eloInfoControl.getEloTitle(eloUri);
      if (title!=null){
         scyWindow.title = title;
      }

//      var metadata = repository.retrieveMetadata(eloUri);
//      if (metadata == null){
//         println("Couldn't find elo {eloUri}");
//         return;
//      }
//      var tk = getTitleKey();
//      if (metadata.metadataKeyExists(tk)){
//         var title = metadata.getMetadataValueContainer(tk).getValue();
//         if (title != null){
//            scyWindow.title = title as String;
//         }
//      }
   }

//   function getTitleKey():IMetadataKey{
//      if (titleKey == null){
//         titleKey = metadataTypeManager.getMetadataKey("title");
//      }
//      return titleKey;
//   }

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
         if (Sequences.indexOf(relatedWindows, scyWindow) >= 0){
            return true;
         }
         if (Sequences.indexOf(otherWindows, scyWindow) >= 0){
            return true;
         }

      }
      return false;
   }

   public function positionWindows(){
      positionWindows(false);
   }

   public function positionWindows(onlyNewWindows:Boolean){
      //logger.info("for size ({width}*{height}) and {scyDesktop.scyWindows.content.size()} windows");
      if (scyDesktop.scyWindows.content.size()==0){
         // no windows, nothing to do
         return;
      }
      var newPlacedWindows: ScyWindow[];
      if (not onlyNewWindows){
         delete placedWindows;
      }
      windowPositioner.clearWindows();
      windowPositioner.setCenterWindow(activeAnchorWindow);
      if (not onlyNewWindows){
         insert activeAnchorWindow into placedWindows;
      }
         insert activeAnchorWindow into newPlacedWindows;
      for (anchor in activeAnchor.nextAnchors){
         var scyWindow = getScyWindow(anchor);
         scyDesktop.addScyWindow(scyWindow);
         var anchorDirection = getAnchorDirection(anchor);
         windowPositioner.addLinkedWindow(scyWindow, anchorDirection);
         if (not onlyNewWindows){
               insert scyWindow into placedWindows;
         }
            insert scyWindow into newPlacedWindows;
      }
//      findRelatedWindows();
      for (window in relatedWindows){
         windowPositioner.addOtherWindow(window);
         if (not onlyNewWindows){
               insert window into placedWindows;
         }
            insert window into newPlacedWindows;
      }
      for (window in otherWindows){
         windowPositioner.addOtherWindow(window);
         if (not onlyNewWindows){
               insert window into placedWindows;
         }
            insert window into newPlacedWindows;
      }
      if (onlyNewWindows){
         windowPositioner.setFixedWindows(placedWindows);
      }
      windowPositioner.positionWindows();
      if (onlyNewWindows){
         for (window in newPlacedWindows){
            if (Sequences.indexOf(placedWindows,window) < 0){
                 insert window into placedWindows;
            }
         }
      }

   }

//   var usedEdgeSourceWindows: ScyWindow[]; // TODO, don't use a "global" variable for it
//   function findRelatedWindows(){
//      delete relatedWindows;
//      delete usedEdgeSourceWindows;
//      for (relationName in activeAnchor.relationNames){
//         findRelatedWindows(relationName);
//      }
//
//   }

//   function findRelatedWindows(relationName:String){
//      var relationKey = metadataTypeManager.getMetadataKey(relationName);
//      if (relationKey == null){
//         println("couldn't find the metadataKey named: {relationName}");
//         //         var keys = roolo.getKeys();
//         return;
//      }
//      var query = new BasicMetadataQuery(relationKey,"EQUALS",activeAnchor.eloUri,null);
//      var results = repository.search(query);
//      println("Query: {query.toString()}, results: {results.size()}");
//      for (r in results){
//         var result = r as ISearchResult;
//         if (not activeAnchor.eloUri.equals(result.getUri())){
//            var metadata = repository.retrieveMetadata(result.getUri());
//            var annotatesValue = metadata.getMetadataValueContainer(relationKey).getValue();
//            var scyWindow = getScyWindow(result.getUri());
////            if (Sequences.indexOf(usedEdgeSourceWindows, scyWindow) < 0){
//            //               edgesManager.createEdge(scyWindow,activeAnchorWindow,relationName);
//            //                   insert scyWindow into usedEdgeSourceWindows;
//            //            }
//            scyDesktop.addScyWindow(scyWindow);
//
//               insert scyWindow into relatedWindows;
//
//            println("added related elo {result.getUri()}");
//         }
//      }
//   }
//
//   function addRelatedWindow(eloUri:URI){
//      var metadata = repository.retrieveMetadata(eloUri);
//      for (relationName in activeAnchor.relationNames){
//         var relationKey = metadataTypeManager.getMetadataKey(relationName);
//         if (relationKey != null){
//            if (metadata.metadataKeyExists(relationKey)){
//               var relationValue = metadata.getMetadataValueContainer(relationKey).getValue();
//               if (activeAnchor.eloUri.equals(relationValue)){
//                  addRelatedWindow(eloUri,relationName);
//                  return;
//               }
//            }
//         }
//      }
//
//   }
//
//
//   function addRelatedWindow(eloUri:URI, relationName:String){
//      if (not activeAnchor.eloUri.equals(eloUri)){
//            //var metadata = roolo.repository.retrieveMetadata(eloUri);
//         //var annotatesValue = metadata.getMetadataValueContainer(relationKey).getValue();
//         var scyWindow = getScyWindow(eloUri);
////         if (Sequences.indexOf(usedEdgeSourceWindows, scyWindow) < 0){
//         //            edgesManager.createEdge(scyWindow,activeAnchorWindow,relationName);
//         //                insert scyWindow into usedEdgeSourceWindows;
//         //         }
//         scyDesktop.addScyWindow(scyWindow);
//
//            insert scyWindow into relatedWindows;
//
//         println("added related elo {eloUri}");
//      }
//   }
//


   function getAnchorDirection(anchor:AnchorFX):Number{
      return Math.atan2(anchor.yPos - activeAnchor.yPos , anchor.xPos - activeAnchor.xPos);
   }

}
