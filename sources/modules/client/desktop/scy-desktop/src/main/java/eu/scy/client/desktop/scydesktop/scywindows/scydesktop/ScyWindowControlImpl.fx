/*
 * ScyWindowControlImpl.fx
 *
 * Created on 13-okt-2009, 17:00:48
 */

package eu.scy.client.desktop.scydesktop.scywindows.scydesktop;

import javafx.util.Math;
import javafx.util.Sequences;
import java.net.URI;

import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import eu.scy.client.desktop.scydesktop.scywindows.WindowPositioner;
import eu.scy.client.desktop.scydesktop.scywindows.window.StandardScyWindow;
import eu.scy.client.desktop.scydesktop.scywindows.window_positions.SimpleWindowPositioner;

import eu.scy.client.desktop.scydesktop.tools.corner.missionmap.MissionAnchorFX;

import eu.scy.client.desktop.scydesktop.scywindows.ScyWindowControl;

/**
 * @author sikken
 */

public class ScyWindowControlImpl extends ScyWindowControl {

   public override var width on replace {
      sizeChanged()
   };
   public override var height on replace {
      sizeChanged()
   };

   def interWindowSpace = 20.0;

   var activeAnchor = bind missionModel.activeAnchor on replace{
      activeAnchorChanged()
   };
   var activeAnchorWindow: ScyWindow;
//   var windowPositioner: WindowPositioner = WindowPositionerCenterMinimized{
   var windowPositioner: WindowPositioner = SimpleWindowPositioner{
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

   public override function addOtherScyWindow(otherWindow:ScyWindow){
      println("addOtherScyWindow, id:{otherWindow.id}");
      insert otherWindow into otherWindows;
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

   function getScyWindow(anchor:MissionAnchorFX):ScyWindow{
      var scyWindow: ScyWindow = scyDesktop.findScyWindow(anchor.eloUri.toString());
      if (scyWindow == null){
         scyWindow = createScyWindow(anchor.eloUri);
         scyWindow.scyWindowAttributes = missionMap.getAnchorAttribute(anchor);
//         scyWindow = ScyWindow{
//            id: anchor.eloUri.toString();
//            title: anchor.title;
//            color: anchor.color;
//            scyWindowAttributes: [
//               missionMap.getAnchorAttribute(anchor)
//            ]
//         }
//         applyMetadataAttributes(scyWindow,anchor.eloUri);
//         windowContentFactory.fillWindowContent(anchor.eloUri,scyWindow);
//         windowStyler.style(scyWindow, anchor.eloUri);
      }
      return scyWindow;
   }

   function getScyWindow(eloUri:URI):ScyWindow{
      var scyWindow: ScyWindow = scyDesktop.findScyWindow(eloUri.toString());
      if (scyWindow == null){
         scyWindow = createScyWindow(eloUri);
//         scyWindow = ScyWindow{
//            id: eloUri.toString();
////                    title: anchor.title;
//
//         }
//         applyMetadataAttributes(scyWindow,eloUri);
//         windowContentFactory.fillWindowContent(eloUri,scyWindow);
//         windowStyler.style(scyWindow, eloUri);
      }
      return scyWindow;
   }

   function createScyWindow(eloUri:URI):ScyWindow{
      var scyWindow = StandardScyWindow{
         id: eloUri.toString();
         eloUri: eloUri;
         eloType: eloInfoControl.getEloType(eloUri);
         title: eloInfoControl.getEloTitle(eloUri);
      }
      applyMetadataAttributes(scyWindow,eloUri);
      windowContentFactory.fillWindowContent(eloUri,scyWindow,null);
      windowStyler.style(scyWindow, eloUri);
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

   public override function positionWindows(onlyNewWindows:Boolean){
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


   function getAnchorDirection(anchor:MissionAnchorFX):Number{
      return Math.atan2(anchor.yPos - activeAnchor.yPos , anchor.xPos - activeAnchor.xPos);
   }

}
