/*
 * AreaPositioner.fx
 *
 * Created on 21-feb-2010, 16:40:35
 */
package eu.scy.client.desktop.scydesktop.scywindows.window_positions;

import javafx.geometry.Bounds;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import javafx.util.Sequences;
import javafx.util.Math;
import java.lang.System;
import org.apache.log4j.Logger;
import javafx.geometry.BoundingBox;

/**
 * @author sikken
 */

 // TODO, now a simple rectangle is used for the window position calculation, attributes and drawers are ignored.
 
public class AreaPositioner {

   def logger = Logger.getLogger(this.getClass());
   public var name = "???";
   public var area: Bounds;
   public var horizontal = true;
   public var windows: ScyWindow[];
   public var ignoreWindow: ScyWindow;
   def initialX = 10;
   def initialY = 25;
   def xStep = 10;
   def yStep = 10;
   def maximumIntersectionTarget = 1.0;
   var placedWindows: ScyWindow[];

   public function clear() {
      delete  windows;
      ignoreWindow = null;
   }

   public function contains(window: ScyWindow): Boolean {
      return Sequences.indexOf(windows, window) >= 0
   }

   public function positionWindows(): Void {
      removeDuplicateWindows();
      logger.info("to position {sizeof windows} in {name}");
//      println("AreaPositioner {name}:");
//      for (window in windows){
//         println("{indexof window}: {window.eloUri}");
//      }

      delete  placedWindows;
      insert ignoreWindow into placedWindows;
      for (window in windows) {
         if (Sequences.indexOf(placedWindows, window) < 0) {
            minimizeWindow(window);
            positionWindow(window);
            insert window into placedWindows;
         }
      }
   }

   public function positionNewWindow(window:ScyWindow):Void{
      if (Sequences.indexOf(windows, window) < 0){
         positionWindow(window);
         insert window into windows;
         insert window into placedWindows;
      }
   }


   function removeDuplicateWindows(){
      var cleanedWindows:ScyWindow[];
      var nrOfDuplcates = 0;
      for (window in windows){
         if (Sequences.indexOf(cleanedWindows, window) < 0){
            insert window into cleanedWindows;
         }
         else{
            ++nrOfDuplcates;
         }

      }
      if (nrOfDuplcates>0){
         logger.info("removed {nrOfDuplcates} duplicate windows from {name}");
      }
      windows = cleanedWindows
   }


   function minimizeWindow(window: ScyWindow) {
      if (not window.isClosed) {
         window.close();
      }
   }

   function positionWindow(window: ScyWindow) {
      var startNanos = System.nanoTime();
      var windowPositionStepper = WindowPositionStepper{
         window:window
         area:area
         horizontal:horizontal;
      }

//      window.layoutX = area.minX + initialX;
//      window.layoutY = area.minY + initialY;
      var minimumIntersection = Number.MAX_VALUE;
      var bestLayoutX: Number;
      var bestLayoutY: Number;
      var positionFound = false;
      var tryCount = 0;
      while (not positionFound and windowPositionStepper.hasMoreSteps()) {
         ++tryCount;
         windowPositionStepper.makeStep();
         var windowBounds = BoundingBox{
            minX:window.layoutX;
            width:windowPositionStepper.windowWidth
            minY:window.layoutY;
            height:windowPositionStepper.windowHeight
         }
         var maximumIntersection = calculateMaximumIntersection(windowBounds, minimumIntersection);
//         println("position: {window.layoutX}, {window.layoutY}, maximumIntersection: {maximumIntersection}");
         if (maximumIntersection < maximumIntersectionTarget) {
            minimumIntersection = maximumIntersection;
            positionFound = true;
         }
         else {
            if (maximumIntersection < minimumIntersection) {
               minimumIntersection = maximumIntersection;
               bestLayoutX = window.layoutX;
               bestLayoutY = window.layoutY;
            }
//
//
//            window.layoutY += yStep;
//            if (window.boundsInParent.maxY > area.maxX) {
//               // we are at the bottom of the window, try a next "collumn"
//               window.layoutX += xStep;
//               window.layoutY = initialX;
//               if (window.boundsInParent.maxX > area.maxY) {
//                  // we are at the right of the window and not found a perfect position
//                  // let's use the best found sofar
//                  window.layoutX = bestLayoutX;
//                  window.layoutY = bestLayoutY;
//                  positionFound = true;
//               }
//            }
         }
      }
      if (not positionFound){
         // the perfect position is not found, use the best one
         window.layoutX = bestLayoutX;
         window.layoutY = bestLayoutY;
         positionFound = true;
      }

      var usedNanos = System.nanoTime() - startNanos;
      var usedMillis = usedNanos / 1e6;
      logger.info("placed window {window.eloUri} at ({window.layoutX},{window.layoutY})");
      logger.info("with {sizeof windows} other windows, found position in {tryCount} tries and in {usedMillis} ms, intersection: {minimumIntersection}");
   }

   function calculateMaximumIntersection(newWindowBounds: Bounds, currentMinimumIntersection: Number): Number {
      //var newWindowBounds = window.boundsInParent;
      //       println("title: {window.title}, newWindowBounds: {newWindowBounds}");
      var maximumIntersection = 0.0;
      for (win in placedWindows) {
         var winBounds = win.boundsInParent;

         var closeWindowsBounds = BoundingBox{
            minX: win.layoutX
            width: win.closedBoundsWidth
            minY: win.layoutY
            height: win.closedBoundsHeight
         };
         winBounds = closeWindowsBounds;

         var intersection = calculateRectangleIntersection(newWindowBounds, winBounds);
         maximumIntersection = Math.max(maximumIntersection, intersection);
         if (intersection > currentMinimumIntersection) {
            // no need to continue, a better position was already found
            return intersection
         }
      }
      //       logger.info("maximumIntersection: {maximumIntersection}");
      return maximumIntersection;
   }

   function calculateRectangleIntersection(rect1: Bounds, rect2: Bounds): Number {
      if (rect1.intersects(rect2)) {
         var xLength = calculateIntersectionLength(rect1.minX, rect1.maxX, rect2.minX, rect2.maxX);
         var yLength = calculateIntersectionLength(rect1.minY, rect1.maxY, rect2.minY, rect2.maxY);
         return xLength * yLength;
      }
      return 0.0;
   }

   function calculateIntersectionLength(min1: Number, max1: Number, min2: Number, max2: Number): Number {
      return Math.min(max1, max2) - Math.max(min1, min2);
   }

}
