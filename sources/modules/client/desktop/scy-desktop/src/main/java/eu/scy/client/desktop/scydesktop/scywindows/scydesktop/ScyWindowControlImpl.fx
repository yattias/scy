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
import java.lang.Void;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.client.desktop.desktoputils.StringUtils;
import eu.scy.client.desktop.scydesktop.scywindows.WindowPositionsState;
import java.lang.Exception;
import eu.scy.common.mission.UriScyElo;
import eu.scy.client.desktop.desktoputils.ExceptionCatcher;

/**
 * @author sikken
 */
public class ScyWindowControlImpl extends ScyWindowControl {

   def logger = Logger.getLogger(this.getClass());
   public var showEloInfoDisplay = false;
   public var exceptionCatcher: ExceptionCatcher;
   var firstNewLas = true;
   def activeLas = bind missionModel.activeLas on replace oldActiveLas {
              activeLasChanged(oldActiveLas);
           //              if (firstNewLas and activeLas != null) {
           //                 FX.deferAction(function() {
           //                    activeLasChanged(oldActiveLas);
           //                 });
           //                 firstNewLas = false;
           //              }
           }
   /**
    * as the uri of a window can change (when saving the elo content, this always result is an other uri),
    * a simple Map does not work (unless the uris are updated)
    * but lets keep it simple, use a sequence and a "slow" search
    */
   var scyWindows: ScyWindow[];
   def windowPositionStates = new HashMap();

   function loadScyElo(eloUri: URI): ScyElo {
      def scyWindow = findScyWindow(eloUri);
      if (scyWindow != null) {
         return scyWindow.scyElo
      }
      return ScyElo.loadMetadata(eloUri, tbi)
   }

   public override function newEloSaved(eloUri: URI) {
      def scyElo = loadScyElo(eloUri);
      newEloSaved(scyElo)
   }

   public override function newEloSaved(scyElo: ScyElo) {
      def scyWindow = findScyWindow(scyElo.getUri());
      if (scyWindow == null) {
         // the elo is not yet in a window on the desktop, add it
         logger.info("new elo, is not yet on the desktop, {scyElo.getUri()}");
         // TODO check if elo is a 'displayable/creatabke' elo
         //         def eloMetadata = ScyElo.loadMetadata(eloUri, tbi);
         if (windowManager.scyDesktop.newEloCreationRegistry.containsEloType(scyElo.getTechnicalFormat())) {
            logger.info("new elo's type ({scyElo.getTechnicalFormat()}) is registered in scy-desktop, trying to create a new scywindow/tool for it.");
            addOtherScyWindow(scyElo);
         } else {
            logger.info("new elo's type ({scyElo.getTechnicalFormat()}) is not registered in scy-desktop, doing nothing.");
         }
      } else {
         logger.info("new elo is already on desktop, {scyElo.getUri()}");
      }
   }

   public override function removeOtherScyWindow(eloUri: URI): Void {
      def scyWindow = findScyWindow(eloUri);
      if (scyWindow != null) {
         scyWindow.isQuiting = true;
         scyWindow.scyToolsList.onQuit();
         windowManager.removeScyWindow(scyWindow);
         windowPositioner.removeOtherWindow(scyWindow);
         updateWindowStateXml(activeLas);
      }
      missionModel.removeElo(eloUri);
   }

   public override function removeOtherScyWindow(scyElo: ScyElo): Void {
      removeOtherScyWindow(scyElo.getUri())
   }

   public override function removeOtherScyWindow(scyWindow: ScyWindow): Void {
      if (scyWindow.eloUri != null) {
         removeOtherScyWindow(scyWindow.eloUri);
      } else {
         windowManager.removeScyWindow(scyWindow);
         windowPositioner.removeOtherWindow(scyWindow);
         updateWindowStateXml(activeLas);
      }
   }

   public override function addOtherScyWindow(eloUri: URI): ScyWindow {
      def scyElo = loadScyElo(eloUri);
      addOtherScyWindow(scyElo)
   }

   public override function addOtherScyWindow(scyElo: ScyElo): ScyWindow {
      logger.debug("trying to add another scy-windows with uri: {scyElo.getUri()}");
      def presentWindow = windowManager.findScyWindow(scyElo.getUri());
      if (presentWindow != null) {
         // it is already on screen, nothing more to do
         return presentWindow
      }
      var scyWindow = getScyWindow(scyElo);
      windowManager.addScyWindow(scyWindow);
      windowPositioner.placeOtherWindow(scyWindow);
      updateWindowStateXml(activeLas);
      if (not containsScyElo(activeLas.otherEloUris, scyElo)) {
         missionModel.scyEloChanged(null, scyElo);
      }
      return scyWindow;
   }

   function containsScyElo(uriScyElos: UriScyElo[], scyElo: ScyElo): Boolean {
      for (uriScyElo in uriScyElos) {
         if (uriScyElo.getUri() == scyElo.getUri()) {
            return true
         }
      }
      return false
   }

   public override function addOtherCollaborativeScyWindow(eloUri: URI, mucid: String): ScyWindow {
      def scyElo = loadScyElo(eloUri);
      addOtherCollaborativeScyWindow(scyElo, mucid)
   }

   public override function addOtherCollaborativeScyWindow(scyElo: ScyElo, mucid: String): ScyWindow {
      logger.debug("trying to add another collaborative scy-window with uri: {scyElo.getUri()}");
      def presentWindow = windowManager.findScyWindow(scyElo.getUri());
      if (presentWindow != null) {
         presentWindow.scyElo.setMucId(mucid);
         // it is already on screen, nothing more to do
         return presentWindow
      }
      var scyWindow = getScyWindow(scyElo);
      scyWindow.scyElo.setMucId(mucid);
      windowManager.addScyWindow(scyWindow);
      windowPositioner.placeOtherWindow(scyWindow);
      updateWindowStateXml(activeLas);
      if (not containsScyElo(activeLas.otherEloUris, scyElo)) {
         missionModel.scyEloChanged(null, scyElo);
      }
      return scyWindow;
   }

   public override function addOtherScyWindow(eloType: String): ScyWindow {
      var scyWindow = createScyWindow(eloType);
      windowManager.addScyWindow(scyWindow);
      windowPositioner.placeOtherWindow(scyWindow);
      return scyWindow;
   }

   public override function addOtherScyWindow(scyWindow: ScyWindow): ScyWindow {
      if (not windowManager.hasWindow(scyWindow)) {
         windowManager.addScyWindow(scyWindow);
         windowPositioner.placeOtherWindow(scyWindow);
      }
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
      windowManager.activateScyWindow(window);
   }

   override public function makeMainScyWindow(scyElo: ScyElo): Void {
      makeMainScyWindow(scyElo.getUri())
   }

   override public function makeMainScyWindow(window: ScyWindow): Void {
      windowPositioner.makeMainWindow(window);
      windowPositioner.positionWindows();
      windowManager.activateScyWindow(window);
   }

   override public function saveCurrentWindowState(): Void {
      if (activeLas != null) {
         def windowPositionsState = windowPositioner.getWindowPositionsState();
         missionModel.setWindowStatesXml(activeLas.id, windowPositionsState.getXml());
      }
   }

   function activeLasChanged(oldActiveLas: LasFX) {
      logger.info("new active las: {activeLas.id}");
      repositoryWrapper.setLasId(activeLas.id);
      updateWindowStateXml(oldActiveLas);
      //      if (oldActiveLas != null) {
      //         // store window state of the old active las
      //         def windowPositionsState = windowPositioner.getWindowPositionsState();
      //         if (windowPositionsState != null) {
      //            windowPositionStates.put(oldActiveLas.id, windowPositionsState);
      //         }
      //         missionModel.setWindowStatesXml(oldActiveLas.id, windowPositionsState.getXml());
      //      }
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

   function updateWindowStateXml(las: LasFX): Void {
      if (las != null) {
         def windowPositionsState = windowPositioner.getWindowPositionsState();
         if (windowPositionsState != null) {
            windowPositionStates.put(las.id, windowPositionsState);
         }
         missionModel.setWindowStatesXml(las.id, windowPositionsState.getXml());
      }
   }

   function placeWindowsOnDesktop(windowPositionsState: WindowPositionsState) {
      var mainWindow: ScyWindow;
      for (loEloUri in missionModel.loEloUris) {
         var loEloWindow = getScyWindow(loEloUri.getScyElo());
         if (windowPositioner.addGlobalLearningObjectWindow(loEloWindow)) {
            windowManager.addScyWindow(loEloWindow);
         }
      }
      for (loEloUri in activeLas.loEloUris) {
         var loEloWindow = getScyWindow(loEloUri.getScyElo());
         if (windowPositioner.addGlobalLearningObjectWindow(loEloWindow)) {
            windowManager.addScyWindow(loEloWindow);
         }
      }
      var mainAnchorWindow = getScyWindow(activeLas.mainAnchor.scyElo);
      if (windowPositioner.setAnchorWindow(mainAnchorWindow)) {
         windowManager.addScyWindow(mainAnchorWindow);
      }
      mainWindow = mainAnchorWindow;
      addAnchorRelated(activeLas.mainAnchor);
      for (intermediateAnchor in activeLas.intermediateAnchors) {
         if (intermediateAnchor.exists) {
            var intermediateAnchorWindow = getScyWindow(intermediateAnchor.scyElo);
            if (windowPositioner.addIntermediateWindow(intermediateAnchorWindow)) {
               windowManager.addScyWindow(intermediateAnchorWindow);
               addAnchorRelated(intermediateAnchor);
            }
         }
      }
      for (las in activeLas.nextLasses) {
         if (las.exists) {
            var anchorWindow = getScyWindow(las.mainAnchor.scyElo);
            if (windowPositioner.addNextAnchorWindow(anchorWindow, getAnchorDirection(las))) {
               windowManager.addScyWindow(anchorWindow);
            }
         }
      }
      for (las in activeLas.previousLasses) {
         if (las.exists) {
            var anchorWindow = getScyWindow(las.mainAnchor.scyElo);
            if (windowPositioner.addPreviousAnchorWindow(anchorWindow, getAnchorDirection(las))) {
               windowManager.addScyWindow(anchorWindow);
            }
         }
      }
      for (otherEloUri in activeLas.otherEloUris) {
         var otherEloWindow = getScyWindow(otherEloUri.getScyElo());
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
         // find the initial to open anchor elo
         var initialToopenAnchor: MissionAnchorFX = null;
         if (activeLas.initialAnchorToOpen != null) {
            initialToopenAnchor = activeLas.initialAnchorToOpen;
         } else if (sizeof activeLas.intermediateAnchors == 0) {
            initialToopenAnchor = activeLas.mainAnchor;
         }
         if (initialToopenAnchor != null) {
            def initialToOpenAnchorWindow = getScyWindow(initialToopenAnchor.scyElo);
            windowPositioner.makeMainWindow(initialToOpenAnchorWindow);
         }
         windowPositioner.positionWindows();
      }
   }

   function addAnchorRelated(missionAnchor: MissionAnchorFX) {
      for (anchor in missionAnchor.inputAnchors) {
         if (anchor.exists) {
            var anchorWindow = getScyWindow(anchor.scyElo);
            if (windowPositioner.addInputAnchorWindow(anchorWindow, getAnchorDirection(anchor.las))) {
               windowManager.addScyWindow(anchorWindow);
            }
         }
      }
      for (loEloUri in missionAnchor.loEloUris) {
         var loEloWindow = getScyWindow(loEloUri.getScyElo());
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
         def scyElo = loadScyElo(eloUri);
         scyWindow = createScyWindow(scyElo);
      }
      return scyWindow;
   }

   function getScyWindow(scyElo: ScyElo): ScyWindow {
      var scyWindow = findScyWindow(scyElo.getUri());
      if (scyWindow == null) {
         scyWindow = createScyWindow(scyElo);
      }
      return scyWindow;
   }

   function createScyWindow(scyElo: ScyElo): ScyWindow {
      var scyWindow = StandardScyWindow {
                 eloUri: scyElo.getUri()
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
      scyWindow.scyToolsList.exceptionCatcher = exceptionCatcher;
      windowStyler.style(scyWindow);
      var anchorAttribute = missionMap.getAnchorAttribute(scyElo.getUri());
      if (anchorAttribute != null) {
         anchorAttribute.scyWindow = scyWindow;
         scyWindow.scyWindowAttributes = anchorAttribute;
         anchorAttribute.windowAction = function(anchor: MissionAnchorFX): Void {
                    windowPositioner.makeMainWindow(getScyWindow(anchor.eloUri));
                    windowPositioner.positionWindows();
                 }
      }
      if (showEloInfoDisplay) {
         insert getEloInfoDisplayAttribute(scyWindow) into scyWindow.scyWindowAttributes;
      }
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
      scyWindow.scyToolsList.exceptionCatcher = exceptionCatcher;
      windowStyler.style(scyWindow);
      if (showEloInfoDisplay) {
         insert getEloInfoDisplayAttribute(scyWindow) into scyWindow.scyWindowAttributes;
      }
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

   function getAnchorDirection(las: LasFX): Number {
      return Math.atan2(las.yPos - activeLas.yPos, las.xPos - activeLas.xPos);
   }

   public override function askShutdownPermissionFromWindows(): Boolean {
      for (window in scyWindows) {
         if (window.eloUri != null) {
            if (not window.scyToolsList.aboutToClose()) {
               return false
            }
         }
      }
      return true
   }

   public override function saveBeforeQuit(): Void {
      for (window in scyWindows) {
         if (window.eloUri != null) {
            try {
               window.isQuiting = true;
               window.scyToolsList.onQuit();
            } catch (e: Exception) {
               logger.error("an exception occured during the aboutToClose of {window.eloUri}", e);
            }
         }
      }
   }

}
