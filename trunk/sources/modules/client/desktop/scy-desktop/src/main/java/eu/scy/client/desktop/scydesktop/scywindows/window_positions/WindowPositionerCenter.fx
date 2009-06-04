/*
 * WindowPositionerCenter.fx
 *
 * Created on 25-mrt-2009, 15:24:43
 */

package eu.scy.client.desktop.scydesktop.scywindows.window_positions;

import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import eu.scy.client.desktop.scydesktop.scywindows.WindowPositioner;
import java.lang.Math;
import javafx.geometry.Rectangle2D;

/**
 * @author sikkenj
 */

public class WindowPositionerCenter extends WindowPositioner {
    public override var width = 200.0 on replace {
      calculateInternals()
   };
    public override var height = 200.0 on replace {
      calculateInternals()
   };
   //    public override var forbiddenAreas:Rectangle2D[];

   var centerX = 0.0;
   var centerY = 0.0;
   var xMin = 0.0;
   var xMax = 0.0;
   var yMin = 0.0;
   var yMax = 0.0;
   var centerWidth = 0.0;
   var outsideWidth = 0.0;
   var centerHeight = 0.0;
   var outsideHeight = 0.0;

   var topLeftAngle = 0.0;
   var topRightAngle = 0.0;
   var bottomRightAngle = 0.0;
   var bottomLeftAngle = 0.0;

   var centerWindowPosition: WindowPosition;
   var linkedWindowPositions: WindowPosition[];
   var otherWindowPositions: WindowPosition[];

   def horizontalInterWindowSpace = 20.0;
   def verticalInterWindowSpace = 30.0;
   def topCompensation = 20;
   def rightCompensation = 10;

   def maxCorrectionTries = 20;
   def maxRandomTries = 10;
   def deltaDirection = Math.PI / 100;
   def noIntersection = 1e-3;


   function calculateInternals(){
      var useWidth = width - rightCompensation;
      var useHeight = height - topCompensation;
      centerX = useWidth / 2;
      centerY = topCompensation + useHeight / 2;
      centerWidth = useWidth / 2 - horizontalInterWindowSpace;
      outsideWidth = useWidth / 4 - horizontalInterWindowSpace;
      centerHeight = useHeight / 2 - verticalInterWindowSpace;
      outsideHeight = useHeight / 4 - verticalInterWindowSpace;
      var xBorder = useWidth / 8;
      xMin = xBorder;
      xMax = useWidth - xBorder;
      var yBorder = useHeight / 8;
      yMin = topCompensation + yBorder;
      yMax = topCompensation + useHeight - yBorder;

      var angle = Math.atan2(yMax - yMin,xMax - xMin);
      topLeftAngle = -Math .PI + angle;
      topRightAngle = -angle;
      bottomRightAngle = angle;
      bottomLeftAngle = -topLeftAngle;

//        println("width:{width}, height:{height}, xMin:{xMin}, xMax:{xMax}, yMin:{yMin}, yMax:{yMax}");
//        println("centerWidth:{centerWidth}, centerHeight:{centerHeight}, outsideWidth:{outsideWidth}, outsideHeight:{outsideHeight}");
//        println("topLeftAngle:{topLeftAngle},topRightAngle:{topRightAngle},bottomRightAngle:{bottomRightAngle},bottomLeftAngle:{bottomLeftAngle},");


   }

   public override function clearWindows():Void{
      centerWindowPosition = null;
      delete linkedWindowPositions;
      delete otherWindowPositions;
   }


   public override function setCenterWindow(window:ScyWindow){
      centerWindowPosition = WindowPosition{
         window: window;
      };
   }

   public override function addLinkedWindow(window:ScyWindow, direction:Number){
       //println("addLinkedWindow({window.title},{direction})");
      var linkedWindowPosition = WindowPosition{
         window: window;
         preferredDirection: direction;
      };
        insert linkedWindowPosition into linkedWindowPositions;
   }

   public override function addOtherWindow(window:ScyWindow){
        //println("addOtherWindow({window.title})");
      var otherWindowPosition = WindowPosition{
         window: window;
      };
        insert otherWindowPosition into otherWindowPositions;
   }

   public override function setFixedWindows(fixedWindows:ScyWindow[]):Void{
   }

   public override function positionWindows():Void{
      calculateWindowPosition(centerWindowPosition,centerX,centerY,centerWidth, centerHeight);
      for (windowPosition in linkedWindowPositions){
         calculateWindowPosition(windowPosition,windowPosition.preferredDirection,outsideWidth,outsideHeight);
      }
      for (windowPosition in otherWindowPositions){
         windowPosition.preferredDirection = calculateDirection(windowPosition.window);
         calculateWindowPosition(windowPosition,windowPosition.preferredDirection,outsideWidth,outsideHeight);
      }
      correctWindowPositions();
      applyWindowPositions();
   }

   function calculateWindowPosition(windowPosition:WindowPosition, direction:Number,w:Number,h:Number){
      var x = xMin;
      var y = yMin;
      if (direction >= topLeftAngle and direction <= topRightAngle){
            // at the top
         x = centerX + (centerY - yMin) * - Math.tan(Math.PI   /   2   -   direction);
         y = yMin ;
      }
        else if (direction > topRightAngle and direction <= bottomRightAngle){
            // at the right
         x = xMax;
         y = centerY - (centerX - xMax) * Math.tan(direction);
      }
        else if (direction > bottomRightAngle and direction <= bottomLeftAngle){
            // at the bottom
         x = centerX + (centerY - yMax) * - Math.tan(Math.PI   /   2   -   direction);
         y = yMax
      }
        else {
            // at the left
         x = xMin;
         y = centerY - (centerX - xMin) * Math.tan(direction);
      };
      windowPosition.useDirection = direction;
      calculateWindowPosition(windowPosition,x,y,w,h);
   }

   function calculateWindowPosition(windowPosition:WindowPosition, x:Number,y:Number,w:Number,h:Number){
      windowPosition.width = Math.max(w,windowPosition.window.minimumWidth);
      windowPosition.height = Math.max(h,windowPosition.window.minimumHeight);
      if (windowPosition.window.widthHeightProportion > 0.0){
         if (windowPosition.window.widthHeightProportion > 1.0){
				windowPosition.width = windowPosition.height / windowPosition.window.widthHeightProportion;
         }
			  else {
				windowPosition.height = windowPosition.width / windowPosition.window.widthHeightProportion;
         }
      }

      windowPosition.x = x - windowPosition.width / 2;
      windowPosition.y = y - windowPosition.height / 2;
//        windowPosition.x = x - w/2;
      //        windowPosition.y = y - h/2;
      windowPosition.rectangle = Rectangle2D{
         minX: windowPosition.x - horizontalInterWindowSpace / 2
         minY: windowPosition.y - verticalInterWindowSpace / 2
         width: windowPosition.width + horizontalInterWindowSpace
         height: windowPosition.height + verticalInterWindowSpace
      }
   }

   function calculateDirection(window:ScyWindow):Number{
      var windowCenterX = window.translateX + window.width / 2;
      var windowCenterY = window.translateY + window.height / 2;
      return Math.atan2(windowCenterY - centerY , windowCenterX - centerX);
   }

   function correctWindowPositions(){
      var usedRects: Rectangle2D[];
      for (node in forbiddenNodes){
          var sceneBound = node.localToScene(node.boundsInLocal);
            insert Rectangle2D {
              height: sceneBound.height,
              width: sceneBound.width,
              minX: sceneBound.minX,
              minY: sceneBound.minY,
            } into usedRects;
      };
      for (windowPosition in linkedWindowPositions){
         correctWindowPosition(usedRects,windowPosition);
            insert windowPosition.rectangle into usedRects;
      }
      for (windowPosition in otherWindowPositions){
         correctWindowPosition(usedRects,windowPosition);
            insert windowPosition.rectangle into usedRects;
      }
   }

   function correctWindowPosition(usedRects:Rectangle2D[],windowPosition:WindowPosition):Void{
      windowPosition.intersection = calculateIntersection(usedRects,windowPosition.rectangle);
      if (windowPosition.intersection <= noIntersection){
         //            println("no intersection for {windowPosition.window.title}");
         return;
      }
        // try near by
      var bestNearByPosition = findBestNearByPosition(usedRects,windowPosition);
      if (bestNearByPosition.intersection <= noIntersection){
         //            println("improved intersection of {windowPosition.window.title} form {windowPosition.intersection} to nearby {bestNearByPosition.intersection} ({windowPosition.usedDirection} -> {bestNearByPosition.usedDirection} in {bestNearByPosition.correctionCount} tries)");
         windowPosition.copyFrom(bestNearByPosition);
         return;
      }
      var bestPosition = findBestRandomPosition(usedRects,bestNearByPosition);
      //        println("improved intersection of {windowPosition.window.title} form {windowPosition.intersection} to random {bestPosition.intersection} ({windowPosition.usedDirection} -> {bestPosition.usedDirection} in {bestPosition.correctionCount} tries)");
      windowPosition.copyFrom(bestPosition);
   }

   function findBestNearByPosition(usedRects:Rectangle2D[],windowPosition:WindowPosition):WindowPosition{
      var bestNearByPosition = windowPosition;
      var nearByMinusPosition = calculateNewWindowPosition(usedRects,windowPosition,windowPosition.useDirection - deltaDirection);
      var nearByPlusPosition = calculateNewWindowPosition(usedRects,windowPosition,windowPosition.useDirection + deltaDirection);
      if (nearByMinusPosition.intersection > bestNearByPosition.intersection and nearByPlusPosition.intersection > bestNearByPosition.intersection){
         return bestNearByPosition;
      }

      var deltaFactor: Number = 1.0;
      bestNearByPosition = nearByPlusPosition;
      if (nearByMinusPosition.intersection < nearByPlusPosition.intersection){
         deltaFactor = -1.0;
         bestNearByPosition = nearByMinusPosition;
      }
      var correctCount = maxCorrectionTries;
       while 
      (bestNearByPosition.intersection > noIntersection and correctCount > 0){
         var newNearByPosition = calculateNewWindowPosition(usedRects,bestNearByPosition,bestNearByPosition.useDirection + deltaFactor * deltaDirection);
         if (newNearByPosition.intersection > bestNearByPosition.intersection){
            return bestNearByPosition;
         }
         bestNearByPosition = newNearByPosition;
           --correctCount;
      }
      return bestNearByPosition;
   }

   function findBestRandomPosition(usedRects:Rectangle2D[],windowPosition:WindowPosition):WindowPosition{
      var randomCount = maxRandomTries;
      var bestRandomPosition = windowPosition;
       while 
      (bestRandomPosition.intersection > noIntersection and randomCount > 0){
         var newRandomPosition = calculateNewWindowPosition(usedRects,bestRandomPosition,-Math .PI + Math.random() * 2 * Math.PI);
         if (newRandomPosition.intersection < bestRandomPosition.intersection){
            bestRandomPosition = newRandomPosition;
         }
           --randomCount;
      }
      return bestRandomPosition;
   }



   function calculateNewWindowPosition(usedRects:Rectangle2D[],windowPosition:WindowPosition,direction:Number):WindowPosition{
      var newWindowPosition = WindowPosition.clone(windowPosition);
      calculateWindowPosition(newWindowPosition,direction,newWindowPosition.width,newWindowPosition.height);
      newWindowPosition.intersection = calculateIntersection(usedRects,newWindowPosition.rectangle);
        ++newWindowPosition.correctionCount;
      return newWindowPosition;
   }


   function calculateIntersection(usedRects:Rectangle2D[],windowRect:Rectangle2D):Number{
      var maximumIntersection = 0.0;
      for (rect in usedRects){
         var intersection = calculateRectangleIntersection(rect,windowRect);
         if (intersection > maximumIntersection)
         maximumIntersection = intersection;
      }
      return maximumIntersection;
   }

   function calculateRectangleIntersection(rect1:Rectangle2D,rect2:Rectangle2D):Number{
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


   function applyWindowPositions(){
      applyWindowPosition(centerWindowPosition);
      for (windowPosition in linkedWindowPositions){
         applyWindowPosition(windowPosition);
      }
      for (windowPosition in otherWindowPositions){
         applyWindowPosition(windowPosition);
      }
   }

   function applyWindowPosition(windowPosition:WindowPosition){
      windowPosition.window.translateX = windowPosition.x;
      windowPosition.window.translateY = windowPosition.y;
      windowPosition.window.openWindow(windowPosition.window.width, windowPosition.window.height);
      windowPosition.window.width = windowPosition.width;
      windowPosition.window.height = windowPosition.height;
   }

}
