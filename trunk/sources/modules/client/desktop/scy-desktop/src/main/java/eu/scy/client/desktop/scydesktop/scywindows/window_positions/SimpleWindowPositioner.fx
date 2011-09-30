/*
 * SimpleWindowPositioner.fx
 *
 * Created on 1-okt-2009, 10:11:09
 */

package eu.scy.client.desktop.scydesktop.scywindows.window_positions;

import eu.scy.client.desktop.scydesktop.scywindows.WindowPositioner;

import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import eu.scy.client.desktop.scydesktop.scywindows.WindowPositionsState;

import eu.scy.client.desktop.desktoputils.log4j.Logger;

import javafx.geometry.Bounds;
import javafx.util.Math;

import java.lang.System;

/**
 * @author sikkenj
 */

public class SimpleWindowPositioner extends WindowPositioner {
   def logger = Logger.getLogger(this.getClass());

   var windows:ScyWindow[];

   def initialX = 10;
   def initialY = 25;
   def xStep = 25;
   def yStep = 25;
   def centerWidthFactor = 0.25;
   def centerHeightFactor = 0.15;

   def maximumIntersectionTarget = 1.0;

   override public function addGlobalLearningObjectWindow (window : ScyWindow) : Boolean {
      addWindowImmediately(window);
      return true;
   }

   public override function clearWindows():Void{
   //       logger.info("clearWindows");
      delete windows;
   }

   override public function makeMainWindow (window : ScyWindow): Void {
      logger.info("makeMainWindow: {window.eloUri}");
   }

   public override function setAnchorWindow(window:ScyWindow):Boolean{
   //       logger.info("setCenterWindow");
      //addWindowImmediately(window);
      placeWindowInCenter(window);
      return true;
   }

   public override function addNextAnchorWindow(window:ScyWindow, direction:Number):Boolean{
   //       logger.info("addLinkedWindow");
      addWindowImmediately(window);
      return true;
   }

   public override function addPreviousAnchorWindow(window:ScyWindow, direction:Number):Boolean{
   //       logger.info("addLinkedWindow");
      addWindowImmediately(window);
      return true;
   }

   public override function addInputAnchorWindow(window:ScyWindow, direction:Number):Boolean{
   //       logger.info("addLinkedWindow");
      addWindowImmediately(window);
      return true;
   }

   public override function addIntermediateWindow(window:ScyWindow):Boolean{
   //       logger.info("addOtherWindow({window.title})");
      addWindowImmediately(window);
      return true;
   }

   public override function addLearningObjectWindow(window:ScyWindow):Boolean{
   //       logger.info("addOtherWindow({window.title})");
      addWindowImmediately(window);
      return true;
   }

   public override function addOtherWindow(window:ScyWindow):Boolean{
   //       logger.info("addOtherWindow({window.title})");
      addWindowImmediately(window);
      return true;
   }

   public override function removeOtherWindow(window:ScyWindow):Void{
   //       logger.info("removeOtherWindow({window.title})");
   }

   public override function placeOtherWindow(window:ScyWindow):Boolean{
      addWindowImmediately(window);
      return true;
   }

   public override function positionWindows():Void{
      // nothing to to, all window are positioned immediately
   }

   public override function positionWindows(windowPositionsState:WindowPositionsState):Void{
      // nothing to to, all window are positioned immediately
   }

   public override function getWindowPositionsState():WindowPositionsState{
      WindowPositionsState{

      }
   }

   function placeWindowInCenter(window:ScyWindow){
      window.layoutX = centerWidthFactor * width;
      window.layoutY = centerHeightFactor * height;
      // assume enough space to place to closed window, assume enough space for the opened window
      var windowWidth = (1-2*centerWidthFactor)*width;
      var windowHeigth = (1-2*centerHeightFactor)*height;
      println("center window size: {windowWidth}*{windowHeigth}");
      window.desiredWidth = windowWidth;
      window.desiredHeight = windowHeigth;
      if (window.isClosed){
         window.openWindow(windowWidth, windowHeigth);
      }
      else{
         window.close(true);
         window.width = windowHeigth;
         window.height = windowHeigth;
      }
      insert window into windows;
   }

   function isKnownOtherWindow(window:ScyWindow){
      for (win in windows){
         if (FX.isSameObject(window, win)){
            return true;
         }
      }
      return false;
   }

   function addWindowImmediately(window:ScyWindow){
      if (not isKnownOtherWindow(window)){
         minimizeWindow(window);
         positionWindow(window);
         insert window into windows;
      }
   }

   function minimizeWindow(window:ScyWindow){
      if (not window.isClosed){
         window.close(true);
      }

   }


   function positionWindow(window:ScyWindow){
      var startNanos = System.nanoTime();
      window.layoutX = initialX;
      window.layoutY = initialY;
      var minimumIntersection = Number.MAX_VALUE;
      var bestLayoutX:Number;
      var bestLayoutY:Number;
      var positionFound = false;
      var tryCount = 0;
      while(not positionFound){
         ++tryCount;
         var maximumIntersection = calculateMaximumIntersection(window,minimumIntersection);
//         println("position: {window.layoutX}, {window.layoutY}, maximumIntersection: {maximumIntersection}");
         if (maximumIntersection<maximumIntersectionTarget){
            minimumIntersection = maximumIntersection;
            positionFound = true;
         }
         else{
            if (maximumIntersection<minimumIntersection){
               minimumIntersection = maximumIntersection;
               bestLayoutX = window.layoutX;
               bestLayoutY = window.layoutY;
            }

            window.layoutY += yStep;
            if (window.boundsInParent.maxY>height){
               // we are at the bottom of the window, try a next "collumn"
                window.layoutX += xStep;
                window.layoutY = initialX;
                 if (window.boundsInParent.maxX>width){
                    // we are at the right of the window and not found a perfect position
                    // let's use the best found sofar
                    window.layoutX = bestLayoutX;
                    window.layoutY = bestLayoutY;
                    positionFound = true;
                 }

            }
         }
      }
      var usedNanos = System.nanoTime()-startNanos;
      var usedMillis = usedNanos/1e6;
      logger.info("with {sizeof windows} other windows, found position in {tryCount} tries and in {usedMillis} ms, intersection: {minimumIntersection}");
   }

    function calculateMaximumIntersection(window:ScyWindow, currentMinimumIntersection:Number):Number{
       var newWindowBounds = window.boundsInParent;
//       println("title: {window.title}, newWindowBounds: {newWindowBounds}");
       var maximumIntersection = 0.0;
       for (node in forbiddenNodes){
          var intersection = calculateRectangleIntersection(newWindowBounds,node.boundsInParent);
          if (intersection>currentMinimumIntersection){
             // no need to continue, a better position was already found
             return intersection
          }
          maximumIntersection = Math.max(maximumIntersection, intersection);
       }
       for (win in windows){
          var winBounds = win.boundsInParent;
          var intersection = calculateRectangleIntersection(newWindowBounds,winBounds);
          maximumIntersection = Math.max(maximumIntersection, intersection);
          if (intersection>currentMinimumIntersection){
             // no need to continue, a better position was already found
             return intersection
          }
       }
//       logger.info("maximumIntersection: {maximumIntersection}");
       return maximumIntersection;
    }

   function calculateRectangleIntersection(rect1:Bounds,rect2:Bounds):Number{
      if (rect1.intersects(rect2)){
         var xLength = calculateIntersectionLength(rect1.minX,rect1.maxX,rect2.minX,rect2.maxX);
         var yLength = calculateIntersectionLength(rect1.minY,rect1.maxY,rect2.minY,rect2.maxY);
         return xLength * yLength;
      }
      return 0.0;
   }

   function calculateIntersectionLength(min1:Number,max1:Number,min2:Number,max2:Number):Number{
      return Math.min(max1, max2) - Math.max(min1,min2);
   }

   override function makeWindowFullScreen(window:ScyWindow):Void {
       logger.debug("Full screen not implemented");
   }

}
