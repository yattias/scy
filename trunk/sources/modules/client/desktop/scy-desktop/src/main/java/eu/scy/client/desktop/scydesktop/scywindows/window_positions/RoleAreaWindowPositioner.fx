/*
 * RoleAreaWindow.fx
 *
 * Created on 21-feb-2010, 15:44:04
 */
package eu.scy.client.desktop.scydesktop.scywindows.window_positions;

import eu.scy.client.desktop.scydesktop.scywindows.WindowPositioner;
import javafx.geometry.Bounds;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import eu.scy.client.desktop.scydesktop.scywindows.WindowPositionsState;
import eu.scy.client.desktop.desktoputils.log4j.Logger;
import eu.scy.client.desktop.scydesktop.ScyDesktop;
import javafx.geometry.BoundingBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.Group;
import javafx.util.Math;
import eu.scy.common.scyelo.ScyRooloMetadataKeyIds;
import java.net.URI;

/**
 * @author sikken
 */
public class RoleAreaWindowPositioner extends WindowPositioner {

   def logger = Logger.getLogger(this.getClass());
   public var scyDesktop: ScyDesktop;
   public var showAreas = false;
   var mainWindow: ScyWindow;
   var assignmentWindow: ScyWindow;
//   var inputAnchorWindows:ScyWindow[];
//   var anchorWindows:ScyWindow[];
//   var otherWindows:ScyWindow[];
   var mainWindowArea: Bounds;
   var fullMainWindowArea: Bounds;
   var assignmentWindowArea: Bounds;
   var anchorWindowArea: Bounds;
   var otherWindowArea: Bounds;
   def anchorWindowPositioner = AreaPositioner {
         name: "anchor elos"
         horizontal: true
      }
   def otherWindowPositioner = AreaPositioner {
         name: "other elos"
         horizontal: false
      }
   def areaRectangleStrokeColor = Color.color(0, 0, 0, 0.3);
   def mainWindowAreaRectangle = Rectangle {
         x: 0, y: 0
         width: 0, height: 0
         fill: Color.color(0, 1, 0, 0.1);
         stroke: areaRectangleStrokeColor
      }
   def assignmentWindowAreaRectangle = Rectangle {
         x: 0, y: 0
         width: 0, height: 0
         fill: Color.color(0, 0, 1, 0.1);
         stroke: areaRectangleStrokeColor
      }
   def anchorWindowAreaRectangle = Rectangle {
         x: 0, y: 0
         width: 0, height: 0
         fill: Color.color(1, 1, 0, 0.1);
         stroke: areaRectangleStrokeColor
      }
   def otherWindowAreaRectangle = Rectangle {
         x: 0, y: 0
         width: 100, height: 100
         fill: Color.color(1, 0, 1, 0.1);
         stroke: areaRectangleStrokeColor
      }
   var areaRectsAddedToScene = false;
   def containsAssignmentKey = scyDesktop.config.getMetadataTypeManager().getMetadataKey(ScyRooloMetadataKeyIds.CONTAINS_ASSIGMENT_ELO.getId());

   override public function addGlobalLearningObjectWindow(window: ScyWindow): Boolean {
      insert window into otherWindowPositioner.windows;
      return true;
   }

   public override function clearWindows(): Void {
      //       logger.info("clearWindows");
      mainWindow = null;
      assignmentWindow = null;
      anchorWindowPositioner.clear();
      otherWindowPositioner.clear();
   }

   override public function makeMainWindow(window: ScyWindow): Void {
      logger.info("makeMainWindow: {window.eloUri}");
      if (mainWindow != window) {
         mainWindow = window;
         assignmentWindow = null;
         anchorWindowPositioner.ignoreWindow = null;
         otherWindowPositioner.ignoreWindow = null;
         if (anchorWindowPositioner.contains(window)) {
            // it's a anchor window
            anchorWindowPositioner.ignoreWindow = mainWindow;
            assignmentWindow = findAssignmentWindow(mainWindow);
            if (assignmentWindow != null) {
               otherWindowPositioner.ignoreWindow = assignmentWindow;
            }
         } else {
            // it's not an anchor window
            otherWindowPositioner.ignoreWindow = window;
         }
      }
   //positionWindows();
   }

   function findAssignmentWindow(window: ScyWindow): ScyWindow {
      var metadata = scyDesktop.config.getRepository().retrieveMetadata(window.eloUri);
      var assignmentEloUri = metadata.getMetadataValueContainer(containsAssignmentKey).getValue() as URI;
      if (assignmentEloUri != null) {
         for (otherWindow in otherWindowPositioner.windows) {
            if (otherWindow.eloUri == assignmentEloUri) {
               return otherWindow;
            }
         }
      }
      return null;
   }

   public override function setAnchorWindow(window: ScyWindow): Boolean {
      insert window into anchorWindowPositioner.windows;
      return true;
   }

   public override function addNextAnchorWindow(window: ScyWindow, direction: Number): Boolean {
      return false;
   }

   public override function addPreviousAnchorWindow(window: ScyWindow, direction: Number): Boolean {
      return false;
   }

   public override function addInputAnchorWindow(window: ScyWindow, direction: Number): Boolean {
      insert window into anchorWindowPositioner.windows;
      return true;
   }

   public override function addIntermediateWindow(window: ScyWindow): Boolean {
      insert window into anchorWindowPositioner.windows;
      return true;
   }

   public override function addLearningObjectWindow(window: ScyWindow): Boolean {
      insert window into otherWindowPositioner.windows;
      return true;
   }

   public override function addOtherWindow(window: ScyWindow): Boolean {
      insert window into otherWindowPositioner.windows;
      return true;
   }

   public override function removeOtherWindow(window: ScyWindow): Void {
      delete window from otherWindowPositioner.windows;
   }

   public override function placeOtherWindow(window: ScyWindow): Boolean {
      otherWindowPositioner.positionNewWindow(window);
      return true;
   }

   public override function positionWindows(): Void {
      setAreas();
      if (assignmentWindow != null) {
         placeWindow(assignmentWindow, assignmentWindowArea);
         if (mainWindow != null) {
            placeWindow(mainWindow, mainWindowArea);
         }
      } else {
         if (mainWindow != null) {
            placeWindow(mainWindow, fullMainWindowArea);
         }
      }
      anchorWindowPositioner.area = anchorWindowArea;
      anchorWindowPositioner.positionWindows();
      otherWindowPositioner.area = otherWindowArea;
      otherWindowPositioner.positionWindows();
   }

   public override function positionWindows(windowPositionsState: WindowPositionsState): Void {
      positionWindows();
   }

   public override function getWindowPositionsState(): WindowPositionsState {
      WindowPositionsState {
      }
   }

   def mainHOffsetFactor = 0.4;
   def mainVOffsetFactor = 0.4;
   def maximumHOffset = 230;
   def maximumVOffset = 100;
   def mainAssignmentFactor = 0.6;
   def areaSeparation = 15;
   def windowBottomMargin = 10;
   def windowRightMargin = 10;

   function setAreas() {
      var mainHOffset = Math.min(mainHOffsetFactor * width, maximumHOffset);
      var mainVOffset = Math.min(mainVOffsetFactor * height, maximumVOffset);
      mainWindowArea = BoundingBox {
            minX: mainHOffset + areaSeparation
            width: mainAssignmentFactor * (width - mainHOffset - 3 * areaSeparation)
            minY: mainVOffset + areaSeparation
            height: height - mainVOffset - 2 * areaSeparation
         };
      assignmentWindowArea = BoundingBox {
            minX: mainWindowArea.maxX + areaSeparation
            width: width - mainWindowArea.maxX - 2 * areaSeparation
            minY: mainWindowArea.minY
            height: height - mainVOffset - 2 * areaSeparation - scyDesktop.bottomRightCorner.boundsInLocal.height - scyDesktop.bottomRightCorner.boundsInLocal.minY
         }
      fullMainWindowArea = BoundingBox {
            minX: mainHOffset + areaSeparation
            width: width - mainHOffset - 2*areaSeparation - scyDesktop.bottomRightCorner.boundsInLocal.width - scyDesktop.bottomRightCorner.boundsInLocal.minX
            minY: mainVOffset + areaSeparation
            height: height - mainVOffset - 2 * areaSeparation
         };
      //      println("scyDesktop.topLeftCorner.boundsInLocal: {scyDesktop.topLeftCorner.boundsInLocal}");
      //      println("scyDesktop.topRightCorner.boundsInLocal: {scyDesktop.topRightCorner.boundsInLocal}");
      var anchorAreaLeftOffset = scyDesktop.topLeftCorner.boundsInLocal.width + scyDesktop.topLeftCorner.boundsInLocal.minX;
      var anchorAreaRightOffset = scyDesktop.topRightCorner.boundsInLocal.width;
      anchorWindowArea = BoundingBox {
            minX: anchorAreaLeftOffset
            width: width - anchorAreaLeftOffset - anchorAreaRightOffset
            minY: 0
            height: mainVOffset
         }
      var otherAreaTopOffset = Math.max(scyDesktop.topLeftCorner.boundsInLocal.height, anchorWindowArea.maxY);
      var otherAreaBottomOffset = scyDesktop.bottomLeftCorner.boundsInLocal.height;
      otherWindowArea = BoundingBox {
            minX: 0
            width: mainHOffset
            minY: otherAreaTopOffset
            height: height - otherAreaTopOffset - otherAreaBottomOffset
         }
      if (showAreas) {
         showAreaRects();
         placeRectangleAsArea(mainWindowAreaRectangle, mainWindowArea);
         placeRectangleAsArea(assignmentWindowAreaRectangle, assignmentWindowArea);
         placeRectangleAsArea(anchorWindowAreaRectangle, anchorWindowArea);
         placeRectangleAsArea(otherWindowAreaRectangle, otherWindowArea);
      }
   }

   function showAreaRects() {
      if (showAreas and not areaRectsAddedToScene) {
         var areaGroup = Group {
               content: [
                  mainWindowAreaRectangle,
                  assignmentWindowAreaRectangle,
                  anchorWindowAreaRectangle,
                  otherWindowAreaRectangle
               ]
            }
         insert areaGroup into scyDesktop.highDebugGroup.content;
         areaRectsAddedToScene = true;
      }
   }

   function placeRectangleAsArea(rect: Rectangle, bounds: Bounds) {
      rect.x = bounds.minX;
      rect.y = bounds.minY;
      rect.width = bounds.width;
      rect.height = bounds.height;
   }

   function placeWindow(window: ScyWindow, bounds: Bounds) {
      window.layoutX = bounds.minX;
      window.layoutY = bounds.minY;
      window.desiredWidth = bounds.width;
      window.desiredHeight = bounds.height;
      if (window.isClosed) {
         window.openWindow(bounds.width, bounds.height);
      }
      else {
         window.close(true);
         window.width = bounds.width;
         window.height = bounds.height;
      }
   }

   override function makeWindowFullScreen(window:ScyWindow):Void {
       logger.debug("Full screen not implemented");
   }

}
