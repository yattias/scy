/*
 * MouseInSceneLimitter.fx
 *
 * Created on 17-dec-2009, 17:05:01
 */

package eu.scy.client.desktop.scydesktop.scywindows.window;
import javafx.scene.input.MouseEvent;

/**
 * @author sikken
 */

// place your code here

public class MouseEventInScene{
   public-read var x:Number;
   public-read var y:Number;
   public-read var dragX:Number;
   public-read var dragY:Number;

   public var mouseEvent:MouseEvent on replace {newMouseEvent()}

   function newMouseEvent(){
      x = mouseEvent.x;
      dragX = mouseEvent.dragX;
      if (mouseEvent.sceneX<0){
         var deltaX = mouseEvent.sceneX;
         x -= deltaX;
         dragX -= deltaX;
      }
      else if (mouseEvent.sceneX>mouseEvent.node.scene.width){
         var deltaX = mouseEvent.sceneX-mouseEvent.node.scene.width;
         x -= deltaX;
         dragX -= deltaX;
       }
      y = mouseEvent.y;
      dragY = mouseEvent.dragY;
      if (mouseEvent.sceneY<0){
         var deltaY = mouseEvent.sceneY;
         y -= deltaY;
         dragY -= deltaY;
      }
      else if (mouseEvent.sceneY>mouseEvent.node.scene.height){
         var deltaY = mouseEvent.sceneY-mouseEvent.node.scene.height;
         y -= deltaY;
         dragY -= deltaY;
      }
   }
}
