/*
 * ScyWindowControlImpl.fx
 *
 * Created on 13-okt-2009, 17:00:48
 */

package eu.scy.client.desktop.scydesktop.scywindows.scydesktop;

import javafx.util.Math;
import java.net.URI;

import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import eu.scy.client.desktop.scydesktop.scywindows.window.StandardScyWindow;

import eu.scy.client.desktop.scydesktop.tools.corner.missionmap.MissionAnchorFX;

import eu.scy.client.desktop.scydesktop.scywindows.ScyWindowControl;

import eu.scy.client.desktop.scydesktop.scywindows.DesktopState;

import java.util.HashMap;

import eu.scy.client.desktop.scydesktop.utils.log4j.Logger;
import java.lang.Void;


/**
 * @author sikken
 */

public class ScyWindowControlImpl extends ScyWindowControl {
   def logger = Logger.getLogger(this.getClass());

   var firstNewAnchor = true;
   def activeAnchor = bind missionModel.activeAnchor on replace oldActiveAnchor{
      activeAnchorChanged(oldActiveAnchor);
      if (firstNewAnchor and activeAnchor!=null){
         FX.deferAction(function(){
            activeAnchorChanged(activeAnchor);
         });
         firstNewAnchor = false;
      }

   };
   var activeAnchorWindow: ScyWindow;
   /**
   * as the uri of a window can change (when saving the elo content, this always result is an other uri),
   * a simple Map does not work (unless the uris are updated)
   * but lets keep it simple, use a sequence and a "slow" search
   */
   var scyWindows: ScyWindow[];
   def desktopStates = new HashMap();

   public override function newEloSaved(eloUri:URI){
      var scyWindow = findScyWindow(eloUri);
      if (scyWindow==null){
         // the elo is not yet in a window on the desktop, add it
         logger.info("new elo, is not yet on the desktop, {eloUri}");
         addOtherScyWindow(eloUri);
      }
      else{
         logger.info("new elo is already on desktop, {eloUri}");
      }
   }

   public override function addOtherScyWindow(eloUri:URI): ScyWindow{
      var scyWindow = getScyWindow(eloUri);
      windowManager.addScyWindow(scyWindow);
      windowPositioner.placeOtherWindow(scyWindow);
      return scyWindow;
   }

   public override function addOtherScyWindow(eloType:String):ScyWindow{
      var scyWindow = createScyWindow(eloType);
      windowManager.addScyWindow(scyWindow);
      windowPositioner.placeOtherWindow(scyWindow);
      return scyWindow;
   }

   function activeAnchorChanged(oldActiveAnchor:MissionAnchorFX){
      logger.info("new active anchor: {activeAnchor.eloUri}");
      if (oldActiveAnchor!=null){
         // store window state of the old active anchor
         desktopStates.put(oldActiveAnchor.eloUri, getDesktopState());
      }

      if (activeAnchor!=null){
         // remove all windows from the desktop
         windowManager.removeAllScyWindows();
         windowPositioner.clearWindows();
         // place the correct windows on the desktop
         var desktopState = desktopStates.get(activeAnchor.eloUri) as DesktopState;
         placeWindowsOnDesktop(desktopState);
      }
   }

   function getDesktopState(){
      var eloUris:URI[];
      for (window in windowManager.getScyWindows()){
         if (window.eloUri!=null){
            insert window.eloUri into eloUris;
         }
         else{
            // TODO, handle window with a new and not saved elo
         }
      }
      DesktopState{
         eloUris: eloUris;
         windowPositionsState:windowPositioner.getWindowPositionsState()
      }
   }

   function placeWindowsOnDesktop(desktopState:DesktopState){
      var activeAnchorWindow = getScyWindow(activeAnchor.eloUri);
      windowManager.addScyWindow(activeAnchorWindow);
      windowPositioner.setActiveAnchorWindow(activeAnchorWindow);
      for (anchor in activeAnchor.nextAnchors){
         if (anchor.exists){
            var anchorWindow = getScyWindow(anchor.eloUri);
            windowManager.addScyWindow(anchorWindow);
            windowPositioner.addNextAnchorWindow(anchorWindow,getAnchorDirection(anchor));
         }
      }
      for (anchor in activeAnchor.previousAnchors){
         if (anchor.exists){
            var anchorWindow = getScyWindow(anchor.eloUri);
            windowManager.addScyWindow(anchorWindow);
            windowPositioner.addPreviousAnchorWindow(anchorWindow,getAnchorDirection(anchor));
         }
      }
      for (anchor in activeAnchor.inputAnchors){
         if (anchor.exists){
            var anchorWindow = getScyWindow(anchor.eloUri);
            windowManager.addScyWindow(anchorWindow);
            windowPositioner.addInputAnchorWindow(anchorWindow,getAnchorDirection(anchor));
         }
      }
      for (relationName in activeAnchor.relationNames){
         // add the related elos
      }
      for (resourceEloUri in activeAnchor.resourceEloUris){
            var resourceEloWindow = getScyWindow(resourceEloUri);
            windowManager.addScyWindow(resourceEloWindow);
            windowPositioner.addResourceWindow(resourceEloWindow);
      }
      for (intermediateEloUri in activeAnchor.intermediateEloUris){
            var intermediateEloWindow = getScyWindow(intermediateEloUri);
            windowManager.addScyWindow(intermediateEloWindow);
            windowPositioner.addIntermediateWindow(intermediateEloWindow);
      }
      if (desktopState!=null){
         // add the user elos
         for (eloUri in desktopState.eloUris){
            var scyWindow = windowManager.findScyWindow(eloUri);
            if (scyWindow==null){
               scyWindow = getScyWindow(eloUri);
               windowManager.addScyWindow(scyWindow);
               windowPositioner.addOtherWindow(scyWindow);
            }
         }
      }
      // all windows are placed on the desktop and now it is time to position them
      if (desktopState!=null and desktopState.windowPositionsState!=null){
         // put the windows on the original positions
         windowPositioner.positionWindows(desktopState.windowPositionsState);
      }
      else{
         // no old position information
         windowPositioner.positionWindows();
      }
   }

   function findScyWindow(eloUri:URI):ScyWindow{
      for (window in scyWindows){
         if (window.eloUri==eloUri){
            return window;
         }
      }
      return null;
   }


   function getScyWindow(eloUri:URI):ScyWindow{
      var scyWindow = findScyWindow(eloUri);
      if (scyWindow==null){
         scyWindow = createScyWindow(eloUri);
      }
      return scyWindow;
   }

   function createScyWindow(eloUri:URI):ScyWindow{
      var scyWindow = StandardScyWindow{
         eloUri: eloUri;
         eloType: eloInfoControl.getEloType(eloUri);
         title: eloInfoControl.getEloTitle(eloUri);
         setScyContent:setScyContent;
         missionModelFX:missionModel
         tooltipManager:tooltipManager
      }
      tooltipManager.registerNode(scyWindow, scyWindow);
      var anchor = getAnchor(eloUri);
      if (anchor!=null){
          scyWindow.scyWindowAttributes = missionMap.getAnchorAttribute(anchor);
      }
//      applyMetadataAttributes(scyWindow,eloUri);
//      windowContentFactory.fillWindowContent(eloUri,scyWindow,null);
      windowStyler.style(scyWindow, eloUri);
      insert scyWindow into scyWindows;
      return scyWindow;
   }

   function createScyWindow(eloType:String):ScyWindow{
      var scyWindow = StandardScyWindow{
         eloType: eloType;
         setScyContent:setScyContent;
         missionModelFX:missionModel
         tooltipManager:tooltipManager
      }
      tooltipManager.registerNode(scyWindow, scyWindow);
      windowStyler.style(scyWindow);
      insert scyWindow into scyWindows;
      return scyWindow;
   }

   function getAnchor(eloUri:URI):MissionAnchorFX{
      for (anchor in missionModel.anchors){
         if (anchor.eloUri==eloUri){
            return anchor;
         }
      }
      return null;
   }

   function getAnchorDirection(anchor:MissionAnchorFX):Number{
      return Math.atan2(anchor.yPos - activeAnchor.yPos , anchor.xPos - activeAnchor.xPos);
   }


//   function getScyWindow(anchor:MissionAnchorFX):ScyWindow{
//      var scyWindow: ScyWindow = windowManager.findScyWindow(anchor.eloUri);
//      if (scyWindow == null){
//         scyWindow = createScyWindow(anchor.eloUri);
//         scyWindow.scyWindowAttributes = missionMap.getAnchorAttribute(anchor);
////         scyWindow = ScyWindow{
////            id: anchor.eloUri.toString();
////            title: anchor.title;
////            color: anchor.color;
////            scyWindowAttributes: [
////               missionMap.getAnchorAttribute(anchor)
////            ]
////         }
////         applyMetadataAttributes(scyWindow,anchor.eloUri);
////         windowContentFactory.fillWindowContent(anchor.eloUri,scyWindow);
////         windowStyler.style(scyWindow, anchor.eloUri);
//      }
//      return scyWindow;
//   }
//
//   function getScyWindow(eloUri:URI):ScyWindow{
//      var scyWindow: ScyWindow = windowManager.findScyWindow(eloUri);
//      if (scyWindow == null){
//         scyWindow = createScyWindow(eloUri);
////         scyWindow = ScyWindow{
////            id: eloUri.toString();
//////                    title: anchor.title;
////
////         }
////         applyMetadataAttributes(scyWindow,eloUri);
////         windowContentFactory.fillWindowContent(eloUri,scyWindow);
////         windowStyler.style(scyWindow, eloUri);
//      }
//      return scyWindow;
//   }
//
//   function createScyWindow(eloUri:URI):ScyWindow{
//      var scyWindow = StandardScyWindow{
//         id: eloUri.toString();
//         eloUri: eloUri;
//         eloType: eloInfoControl.getEloType(eloUri);
//         title: eloInfoControl.getEloTitle(eloUri);
//      }
//      applyMetadataAttributes(scyWindow,eloUri);
//      windowContentFactory.fillWindowContent(eloUri,scyWindow,null);
//      windowStyler.style(scyWindow, eloUri);
//      return scyWindow;
//   }
//
//
//   function applyMetadataAttributes(scyWindow:ScyWindow,eloUri:URI){
//      var title = eloInfoControl.getEloTitle(eloUri);
//      if (title!=null){
//         scyWindow.title = title;
//      }
//
////      var metadata = repository.retrieveMetadata(eloUri);
////      if (metadata == null){
////         println("Couldn't find elo {eloUri}");
////         return;
////      }
////      var tk = getTitleKey();
////      if (metadata.metadataKeyExists(tk)){
////         var title = metadata.getMetadataValueContainer(tk).getValue();
////         if (title != null){
////            scyWindow.title = title as String;
////         }
////      }
//   }
//
////   function getTitleKey():IMetadataKey{
////      if (titleKey == null){
////         titleKey = metadataTypeManager.getMetadataKey("title");
////      }
////      return titleKey;
////   }
//
//   function isRelevantScyWindow(scyWindow:ScyWindow):Boolean{
//      if (scyWindow.id != null){
//         var scyWindowUri = new URI(scyWindow.id);
//         if (scyWindowUri == activeAnchor.eloUri){
//            return true;
//         }
//         for (anchor in activeAnchor.nextAnchors){
//            if (scyWindowUri == anchor.eloUri){
//               return true;
//            }
//         }
//         if (Sequences.indexOf(relatedWindows, scyWindow) >= 0){
//            return true;
//         }
//         if (Sequences.indexOf(otherWindows, scyWindow) >= 0){
//            return true;
//         }
//
//      }
//      return false;
//   }
//
//   public override function positionWindows(onlyNewWindows:Boolean){
//      //logger.info("for size ({width}*{height}) and {scyDesktop.scyWindows.content.size()} windows");
//      if (windowManager.scyWindows.content.size()==0){
//         // no windows, nothing to do
//         return;
//      }
//      var newPlacedWindows: ScyWindow[];
//      if (not onlyNewWindows){
//         delete placedWindows;
//      }
//      windowPositioner.clearWindows();
//      windowPositioner.setCenterWindow(activeAnchorWindow);
//      if (not onlyNewWindows){
//         insert activeAnchorWindow into placedWindows;
//      }
//         insert activeAnchorWindow into newPlacedWindows;
//      for (anchor in activeAnchor.nextAnchors){
//         var scyWindow = getScyWindow(anchor);
//         windowManager.addScyWindow(scyWindow);
//         var anchorDirection = getAnchorDirection(anchor);
//         windowPositioner.addLinkedWindow(scyWindow, anchorDirection);
//         if (not onlyNewWindows){
//               insert scyWindow into placedWindows;
//         }
//            insert scyWindow into newPlacedWindows;
//      }
////      findRelatedWindows();
//      for (window in relatedWindows){
//         windowPositioner.addOtherWindow(window);
//         if (not onlyNewWindows){
//               insert window into placedWindows;
//         }
//            insert window into newPlacedWindows;
//      }
//      for (window in otherWindows){
//         windowPositioner.addOtherWindow(window);
//         if (not onlyNewWindows){
//               insert window into placedWindows;
//         }
//            insert window into newPlacedWindows;
//      }
//      if (onlyNewWindows){
//         windowPositioner.setFixedWindows(placedWindows);
//      }
//      windowPositioner.positionWindows();
//      if (onlyNewWindows){
//         for (window in newPlacedWindows){
//            if (Sequences.indexOf(placedWindows,window) < 0){
//                 insert window into placedWindows;
//            }
//         }
//      }
//
//   }

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



}
