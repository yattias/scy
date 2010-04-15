/*
 * WindowPositionStepper.fx
 *
 * Created on 22-feb-2010, 17:38:03
 */

package eu.scy.client.desktop.scydesktop.scywindows.window_positions;
import javafx.geometry.Bounds;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;

/**
 * @author sikken
 */

public class WindowPositionStepper {
   public-init var window:ScyWindow;
   public-init var area:Bounds;
   public-init var horizontal = true;
   public-init var windowClosed = true;
   public-read var stepCount = 0;

   def initialX = 10;
   def initialY = 25;
   def xStep = 10;
   def yStep = 10;
   def maximumIntersectionTarget = 1.0;

   var x:Number;
   var y:Number;
   var moreSteps:Boolean;
   public def windowWidth = if (windowClosed) window.closedBoundsWidth else window.width;
   public def windowHeight = if (windowClosed) window.closedBoundsHeight else window.height;


   init{

      x = area.minX + initialX;
      y = area.minY + initialY;
      moreSteps = not windowOutSideArea();
//      println("WindowPositionStepper for window {window.eloUri}");
//      println("window.layoutBounds: {window.layoutBounds}");
//      println("window.boundsInLocal: {window.boundsInLocal}");
//      println("window.boundsInParent: {window.boundsInParent}");
//      println("area: {area}");
   }


   public function hasMoreSteps():Boolean{
      return moreSteps;
   }

   public function makeStep():Void{
//      println("makeStep to {x},{y}");
      window.layoutX = x;
      window.layoutY = y;
      calculateNextStep();
   }

   function calculateNextStep():Void{
      if (horizontal){
         x += xStep;
         if (windowOutSideAreaRight()){
            x = area.minX + initialX;
            y += yStep;
         }
      }
      else{
         y+=yStep;
         if (windowOutSideAreaBottom()){
            y = area.minY + initialY;
            x += xStep;
         }
      }
      moreSteps = not windowOutSideArea();
      ++stepCount;
   }

   function windowOutSideAreaRight():Boolean{
      return (x+windowWidth)>area.maxX;
   }

   function windowOutSideAreaBottom():Boolean{
      return (y+windowHeight)>area.maxY;
   }

   function windowOutSideArea():Boolean{
//      println("windowOutSideAreaRight(): {windowOutSideAreaRight()}, windowOutSideAreaBottom(): {windowOutSideAreaBottom()}");
      return windowOutSideAreaRight() or windowOutSideAreaBottom();
   }

}
