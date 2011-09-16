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
import java.util.HashMap;
import eu.scy.client.desktop.desktoputils.log4j.Logger;
import eu.scy.client.desktop.scydesktop.tools.corner.missionmap.LasFX;
import javafx.util.Sequences;
import java.lang.Void;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.client.desktop.scydesktop.scywindows.DatasyncAttribute;
import eu.scy.client.desktop.desktoputils.StringUtils;
import eu.scy.client.desktop.scydesktop.scywindows.WindowPositionsState;

/**
 * @author sikken
 */
public class ScyWindowControlImpl extends ScyWindowControl {

   def logger = Logger.getLogger(this.getClass());
   public var showEloInfoDisplay = false;
   var firstNewLas = true;
   def techniocalFormatKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT.getId());
   def activeLas = bind missionModel.activeLas on replace oldActiveLas {
              activeLasChanged(oldActiveLas);
              if (firstNewLas and activeLas != null) {
                 FX.deferAction(function() {
                    activeLasChanged(activeLas);
                 });
                 firstNewLas = false;
              }
           }
//   def activeAnchor = bind missionModel.activeLas on replace oldActiveAnchor{
//      activeAnchorChanged(oldActiveAnchor);
//      if (firstNewLas and activeAnchor!=null){
//         FX.deferAction(function(){
//            activeAnchorChanged(activeAnchor);
//         });
//         firstNewLas = false;
//      }
//
//   };
   var activeAnchorWindow: ScyWindow;
   /**
    * as the uri of a window can change (when saving the elo content, this always result is an other uri),
    * a simple Map does not work (unless the uris are updated)
    * but lets keep it simple, use a sequence and a "slow" search
    */
   var scyWindows: ScyWindow[];
   def windowPositionStates = new HashMap();

   public override function newEloSaved(eloUri: URI) {
      var scyWindow = findScyWindow(eloUri);
      if (scyWindow == null) {
         // the elo is not yet in a window on the desktop, add it
         logger.info("new elo, is not yet on the desktop, {eloUri}");
         // TODO check if elo is a 'displayable/creatabke' elo
         def eloMetadata = ScyElo.loadMetadata(eloUri, tbi);
         if (windowManager.scyDesktop.newEloCreationRegistry.containsEloType(eloMetadata.getTechnicalFormat())) {
            logger.info("new elo's type ({eloMetadata.getTechnicalFormat()}) is registered in scy-desktop, trying to create a new scywindow/tool for it.");
            addOtherScyWindow(eloUri);
         } else {
            logger.info("new elo's type ({eloMetadata.getTechnicalFormat()}) is not registered in scy-desktop, doing nothing.");
         }
      } else {
         logger.info("new elo is already on desktop, {eloUri}");
      }
   }

   public override function removeOtherScyWindow(eloUri: URI): Void {
      missionModel.removeElo(eloUri);
      def scyWindow = findScyWindow(eloUri);
      if (scyWindow != null) {
         scyWindow.isQuiting = true;
         scyWindow.scyToolsList.onQuit();
         windowManager.removeScyWindow(scyWindow);
         windowPositioner.removeOtherWindow(scyWindow);
      }
   }

   public override function removeOtherScyWindow(scyWindow: ScyWindow): Void {
      if (scyWindow.eloUri != null) {
         removeOtherScyWindow(scyWindow.eloUri);
      } else {
         windowManager.removeScyWindow(scyWindow);
         windowPositioner.removeOtherWindow(scyWindow);
      }
   }

   public override function addOtherScyWindow(eloUri: URI): ScyWindow {
      logger.debug("trying to add another scy-windows with uri: {eloUri}");
      if (Sequences.indexOf(activeLas.otherEloUris, eloUri) <= 0) {
         missionModel.eloUriChanged(null, eloUri);
      //         insert eloUri into activeLas.otherEloUris;
      }
      var scyWindow = getScyWindow(eloUri);
      windowManager.addScyWindow(scyWindow);
      windowPositioner.placeOtherWindow(scyWindow);
      return scyWindow;
   }

   public override function addOtherCollaborativeScyWindow(eloUri: URI, mucid: String): ScyWindow {
      logger.debug("trying to add another collaborative scy-window with uri: {eloUri}");
      if (Sequences.indexOf(activeLas.otherEloUris, eloUri) <= 0) {
         missionModel.eloUriChanged(null, eloUri);
      }
      var scyWindow = getScyWindow(eloUri);
      scyWindow.scyElo.setMucId(mucid);
      windowManager.addScyWindow(scyWindow);
      windowPositioner.placeOtherWindow(scyWindow);
      return scyWindow;
   }

   public override function addOtherScyWindow(eloType: String): ScyWindow {
      var scyWindow = createScyWindow(eloType);
      windowManager.addScyWindow(scyWindow);
      windowPositioner.placeOtherWindow(scyWindow);
      return scyWindow;
   }

   override public function makeMainScyWindow(eloUri: URI): Void {
      var window = findScyWindow(eloUri);
      if (window == null) {
         logger.error("cannot find window with uri: {eloUri}");
         return;
      }
      windowPositioner.makeMainWindow(window);
      windowPositioner.positionWindows();
   }

   override public function makeMainScyWindow(window: ScyWindow): Void {
      windowPositioner.makeMainWindow(window);
      windowPositioner.positionWindows();
   }

   function activeLasChanged(oldActiveLas: LasFX) {
      logger.info("new active las: {activeLas.id}");
      repositoryWrapper.setLasId(activeLas.id);
      if (oldActiveLas != null) {
         // store window state of the old active las
         def windowPositionsState = windowPositioner.getWindowPositionsState();
         if (windowPositionsState!=null){
            windowPositionStates.put(oldActiveLas.id, windowPositionsState);
         }
         missionModel.setWindowStatesXml(oldActiveLas.id, windowPositionsState.getXml());
      }
      if (activeLas != null) {
         // remove all windows from the desktop
         windowManager.removeAllScyWindows();
         windowPositioner.clearWindows();
         // place the correct windows on the desktop
         var windowPositionState = windowPositionStates.get(activeLas.id) as WindowPositionsState;
         if (windowPositionState == null) {
            def windowsStatesXml = missionModel.getWindowStatesXml(activeLas.id);
            if (StringUtils.hasText(windowsStatesXml)) {
               windowPositionState = windowPositioner.createWindowPositionsState(windowsStatesXml);
            }
         }
         placeWindowsOnDesktop(windowPositionState);
      }
   }

   function placeWindowsOnDesktop(windowPositionsState: WindowPositionsState) {
      var mainWindow: ScyWindow;
      for (loEloUri in missionModel.loEloUris) {
         var loEloWindow = getScyWindow(loEloUri);
         if (windowPositioner.addGlobalLearningObjectWindow(loEloWindow)) {
            windowManager.addScyWindow(loEloWindow);
         }
      }
      for (loEloUri in activeLas.loEloUris) {
         var loEloWindow = getScyWindow(loEloUri);
         if (windowPositioner.addGlobalLearningObjectWindow(loEloWindow)) {
            windowManager.addScyWindow(loEloWindow);
         }
      }
      var mainAnchorWindow = getScyWindow(activeLas.mainAnchor.eloUri);
      if (windowPositioner.setAnchorWindow(mainAnchorWindow)) {
         windowManager.addScyWindow(mainAnchorWindow);
      }
      mainWindow = mainAnchorWindow;
      addAnchorRelated(activeLas.mainAnchor);
      for (intermediateAnchor in activeLas.intermediateAnchors) {
         if (intermediateAnchor.exists) {
            var intermediateAnchorWindow = getScyWindow(intermediateAnchor.eloUri);
            if (windowPositioner.addIntermediateWindow(intermediateAnchorWindow)) {
               windowManager.addScyWindow(intermediateAnchorWindow);
               addAnchorRelated(intermediateAnchor);
            }
         }
      }
      for (las in activeLas.nextLasses) {
         if (las.exists) {
            var anchorWindow = getScyWindow(las.mainAnchor.eloUri);
            if (windowPositioner.addNextAnchorWindow(anchorWindow, getAnchorDirection(las))) {
               windowManager.addScyWindow(anchorWindow);
            }
         }
      }
      for (las in activeLas.previousLasses) {
         if (las.exists) {
            var anchorWindow = getScyWindow(las.mainAnchor.eloUri);
            if (windowPositioner.addPreviousAnchorWindow(anchorWindow, getAnchorDirection(las))) {
               windowManager.addScyWindow(anchorWindow);
            }
         }
      }
      for (otherEloUri in activeLas.otherEloUris) {
         var otherEloWindow = getScyWindow(otherEloUri);
         if (windowPositioner.addGlobalLearningObjectWindow(otherEloWindow)) {
            windowManager.addScyWindow(otherEloWindow);
         }
      }
      // anchor window will not be opened, it will remain closed on the left side
      // windowPositioner.makeMainWindow(mainWindow);
      // all windows are placed on the desktop and now it is time to position them
      if (windowPositionsState != null) {
         // put the windows on the original positions
         windowPositioner.positionWindows(windowPositionsState);
      } else {
         // no old position information
         windowPositioner.positionWindows();
      }
   }

   function addAnchorRelated(missionAnchor: MissionAnchorFX) {
      for (anchor in missionAnchor.inputAnchors) {
         if (anchor.exists) {
            var anchorWindow = getScyWindow(anchor.eloUri);
            if (windowPositioner.addInputAnchorWindow(anchorWindow, getAnchorDirection(anchor.las))) {
               windowManager.addScyWindow(anchorWindow);
            }
         }
      }
      for (loEloUri in missionAnchor.loEloUris) {
         var loEloWindow = getScyWindow(loEloUri);
         if (windowPositioner.addLearningObjectWindow(loEloWindow)) {
            windowManager.addScyWindow(loEloWindow);
         }
      }
      for (relationName in missionAnchor.relationNames) {
      // add the related elos
      }
   }

   function findScyWindow(eloUri: URI): ScyWindow {
      for (window in scyWindows) {
         if (window.eloUri == eloUri) {
            return window;
         }
      }
      return null;
   }

   function getScyWindow(eloUri: URI): ScyWindow {
      var scyWindow = findScyWindow(eloUri);
      if (scyWindow == null) {
         scyWindow = createScyWindow(eloUri);
      }
      return scyWindow;
   }

   function createScyWindow(eloUri: URI): ScyWindow {
      def scyElo = ScyElo.loadElo(eloUri, tbi);
      var scyWindow = StandardScyWindow {
                 eloUri: eloUri;
                 scyElo: scyElo
                 eloType: scyElo.getTechnicalFormat()
                 eloToolConfig: eloConfigManager.getEloToolConfig(scyElo.getTechnicalFormat())
                 title: scyElo.getTitle()
                 windowColorScheme: windowStyler.getWindowColorScheme(scyElo)
                 eloIcon: windowStyler.getScyEloIcon(scyElo)
                 setScyContent: setScyContent;
                 missionModelFX: missionModel
                 tooltipManager: tooltipManager
                 bubbleManager: bubbleManager
                 dragAndDropManager: dragAndDropManager
                 tbi: tbi
                 windowControl: this
                 windowStyler: windowStyler
              }
      windowStyler.style(scyWindow);
      var anchorAttribute = missionMap.getAnchorAttribute(eloUri);
      if (anchorAttribute != null) {
         anchorAttribute.scyWindow = scyWindow;
         scyWindow.scyWindowAttributes = anchorAttribute;
         anchorAttribute.windowAction = function(anchor: MissionAnchorFX): Void {
                    windowPositioner.makeMainWindow(getScyWindow(anchor.eloUri));
                    windowPositioner.positionWindows();
                 }
      }
      if (showEloInfoDisplay) {
         //         insert getDataSyncAttribute(scyWindow) into scyWindow.scyWindowAttributes;
         insert getEloInfoDisplayAttribute(scyWindow) into scyWindow.scyWindowAttributes;
      }

      //      applyMetadataAttributes(scyWindow,eloUri);
      //      windowContentFactory.fillWindowContent(eloUri,scyWindow,null);
//      tooltipManager.registerNode(scyWindow, scyWindow);
      //windowStyler.style(scyWindow, eloUri);
      insert scyWindow into scyWindows;
      return scyWindow;
   }

   function createScyWindow(eloType: String): ScyWindow {
      var scyWindow = StandardScyWindow {
                 eloType: eloType;
                 eloToolConfig: eloConfigManager.getEloToolConfig(eloType)
                 windowColorScheme: windowStyler.getWindowColorScheme(eloType)
                 eloIcon: windowStyler.getScyEloIcon(eloType)
                 setScyContent: setScyContent;
                 missionModelFX: missionModel
                 tooltipManager: tooltipManager
                 bubbleManager: bubbleManager
                 dragAndDropManager: dragAndDropManager
                 tbi: tbi
                 windowControl: this
                 windowStyler: windowStyler
              }
      windowStyler.style(scyWindow);
      if (showEloInfoDisplay) {
         //         insert getDataSyncAttribute(scyWindow) into scyWindow.scyWindowAttributes;
         insert getEloInfoDisplayAttribute(scyWindow) into scyWindow.scyWindowAttributes;
      }
      //      tooltipManager.registerNode(scyWindow, scyWindow);
      //windowStyler.style(scyWindow);
      insert scyWindow into scyWindows;
      return scyWindow;
   }

   function getEloInfoDisplayAttribute(scyWindow: ScyWindow): EloInfoDisplayAttribute {
      EloInfoDisplayAttribute {
         window: scyWindow
         repository: repository
         metadataTypeManager: metadataTypeManager
         tooltipManager: tooltipManager
      }
   }

   function getDataSyncAttribute(scyWindow: ScyWindow): DatasyncAttribute {
      DatasyncAttribute {
         scyWindow: scyWindow
         tooltipManager: tooltipManager
      }

   }

   function getAnchor(eloUri: URI): MissionAnchorFX {
      for (las in missionModel.lasses) {
         if (las.mainAnchor.eloUri == eloUri) {
            return las.mainAnchor;
         }
         for (anchor in las.intermediateAnchors) {
            if (anchor.eloUri == eloUri) {
               return anchor;
            }
         }
      }
      return null;
   }

   function getAnchorDirection(las: LasFX): Number {
      return Math.atan2(las.yPos - activeLas.yPos, las.xPos - activeLas.xPos);
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
//      var query = new BasicMetadataQuery(relationKey,"EQUALS",activeAnchor.eloUri);
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
