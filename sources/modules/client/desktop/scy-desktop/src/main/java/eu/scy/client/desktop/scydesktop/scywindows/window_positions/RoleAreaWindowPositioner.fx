/*
 * RoleAreaWindow.fx
 *
 * Created on 21-feb-2010, 15:44:04
 */
package eu.scy.client.desktop.scydesktop.scywindows.window_positions;

import eu.scy.client.desktop.scydesktop.scywindows.WindowPositioner;
import javafx.geometry.Bounds;
import javafx.util.Math;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import eu.scy.client.desktop.scydesktop.scywindows.WindowPositionsState;
import eu.scy.client.desktop.scydesktop.utils.log4j.Logger;
import eu.scy.client.desktop.scydesktop.ScyDesktop;
import java.lang.System;
import javafx.geometry.BoundingBox;
import javafx.util.Sequences;

/**
 * @author sikken
 */
public class RoleAreaWindowPositioner extends WindowPositioner {

   def logger = Logger.getLogger(this.getClass());
   public var scyDesktop: ScyDesktop;
   var mainWindow: ScyWindow;
   var assignmentWindow: ScyWindow;
//   var inputAnchorWindows:ScyWindow[];
//   var anchorWindows:ScyWindow[];
//   var otherWindows:ScyWindow[];
   var mainWindowArea: Bounds;
   var assignmentWindowArea: Bounds;
   var anchorWindowArea: Bounds;
   var otherWindowArea: Bounds;
   def anchorWindowPositioner = AreaPositioner {
         }
   def otherWindowPositioner = AreaPositioner {
         }

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
      mainWindow = window;
      if (anchorWindowPositioner.contains(window)) {
         // it's a anchor window
         anchorWindowPositioner.ignoreWindow = window;
      } else {
         // it's not an anchor window
         otherWindowPositioner.ignoreWindow = window;
      }
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

   public override function placeOtherWindow(window: ScyWindow): Boolean {
      insert window into otherWindowPositioner.windows;
      return true;
   }

   public override function positionWindows(): Void {
      setAreas();
      if (mainWindow != null) {
         placeWindow(mainWindow, mainWindowArea);
      }
      if (assignmentWindow != null) {
         placeWindow(assignmentWindow, assignmentWindowArea);
      }
      anchorWindowPositioner.area = anchorWindowArea;
      anchorWindowPositioner.positionWindows();
      otherWindowPositioner.area = otherWindowArea;
      otherWindowPositioner.positionWindows();
   }

   public override function positionWindows(windowPositionsState: WindowPositionsState): Void {
      // nothing to to, all window are positioned immediately
   }

   public override function getWindowPositionsState(): WindowPositionsState {
      WindowPositionsState {
         }
   }

   def mainHOffsetFactor = 0.4;
   def mainVOffsetFactor = 0.4;
   def mainAssignmentFactor = 0.6;

   function setAreas() {
      var mainHOffset = mainHOffsetFactor * width;
      var mainVOffset = mainVOffsetFactor * height;
      mainWindowArea = BoundingBox {
         minX: mainHOffset
         width: mainAssignmentFactor * (width - mainHOffset)
         minY: mainVOffset
         height: height - mainVOffset
      };
      assignmentWindowArea = BoundingBox {
         minX: mainHOffset + mainWindowArea.width
         width: width - mainHOffset - mainWindowArea.width
         minY: mainVOffset
         height: height - mainVOffset - scyDesktop.bottomRightCorner.boundsInLocal.height
      }
      var anchorAreaLeftOffset = scyDesktop.topLeftCorner.boundsInLocal.width;
      var anchorAreaRightOffset = scyDesktop.topRightCorner.boundsInLocal.width;
      anchorWindowArea = BoundingBox {
         minX: anchorAreaLeftOffset
         width: width - anchorAreaLeftOffset - anchorAreaRightOffset
         minY: 0
         height: mainVOffset
      }
      var otherAreaTopOffset = scyDesktop.topLeftCorner.boundsInLocal.height;
      var otherAreaBottomOffset = scyDesktop.bottomLeftCorner.boundsInLocal.height;
      otherWindowArea = BoundingBox {
         minX: 0
         width: mainHOffset
         minY: otherAreaTopOffset
         height: height - otherAreaTopOffset - otherAreaBottomOffset
      }
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
         window.setMinimize(false);
         window.width = bounds.width;
         window.height = bounds.height;
      }
   }

}
