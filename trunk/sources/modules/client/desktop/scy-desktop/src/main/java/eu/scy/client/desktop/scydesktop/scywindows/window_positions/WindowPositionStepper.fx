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
   public-read var stepCount = 0;

   def initialX = 10;
   def initialY = 25;
   def xStep = 25;
   def yStep = 25;
   def maximumIntersectionTarget = 1.0;

   var x:Number;
   var y:Number;
   var moreSteps:Boolean;

   init{
      x = area.minX + initialX;
      y = area.minY + initialY;
      moreSteps = windowOutSideArea();
   }


   public function hasMoreSteps():Boolean{
      return moreSteps;
   }

   public function makeStep():Void{
      println("makeStep to {x},{y}");
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
      moreSteps = windowOutSideArea();
      ++stepCount;
   }

   function windowOutSideAreaRight():Boolean{
      return x+window.layoutBounds.width>area.maxX;
   }

   function windowOutSideAreaBottom():Boolean{
      return y+window.layoutBounds.height>area.maxY;
   }

   function windowOutSideArea():Boolean{
      return windowOutSideAreaRight() or windowOutSideAreaBottom();
   }


}
