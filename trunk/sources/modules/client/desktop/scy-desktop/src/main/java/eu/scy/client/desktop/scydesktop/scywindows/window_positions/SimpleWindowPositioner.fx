/*
 * SimpleWindowPositioner.fx
 *
 * Created on 1-okt-2009, 10:11:09
 */

package eu.scy.client.desktop.scydesktop.scywindows.window_positions;

import eu.scy.client.desktop.scydesktop.scywindows.WindowPositioner;

import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;

import org.apache.log4j.Logger;

import javafx.geometry.Bounds;
import javafx.util.Math;

import java.lang.System;

/**
 * @author sikkenj
 */

def logger = Logger.getLogger("eu.scy.client.desktop.scydesktop.scywindows.window_positions.SimpleWindowPositioner");

public class SimpleWindowPositioner extends WindowPositioner {

   var otherWindows:ScyWindow[];

   def initialX = 10;
   def initialY = 10;
   def xStep = 25;
   def yStep = 25;

   def maximumIntersectionTarget = 1.0;

    public override function clearWindows():Void{
//       logger.info("clearWindows");
    }

    public override function setCenterWindow(window:ScyWindow):Void{
//       logger.info("setCenterWindow");

    }

    public override function addLinkedWindow(window:ScyWindow, direction:Number):Void{
//       logger.info("addLinkedWindow");

    }

    public override function addOtherWindow(window:ScyWindow):Void{
//       logger.info("addOtherWindow({window.title})");
       if (not isKnownOtherWindow(window)){
          positionWindow(window);
          insert window into otherWindows;
       }
    }

    public override function setFixedWindows(fixedWindows:ScyWindow[]):Void{
//       logger.info("setFixedWindows");

    }

    public override function positionWindows():Void{
//       logger.info("positionWindows");

    }

    function isKnownOtherWindow(window:ScyWindow){
       for (win in otherWindows){
          if (FX.isSameObject(window, win)){
             return true;
          }
       }
       return false;
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
//      logger.info("found position in {tryCount} tries and in {usedMillis} ms, intersection: {minimumIntersection}");
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
       for (win in otherWindows){
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
}
