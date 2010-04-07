/*
 * TopDrawer.fx
 *
 * Created on 9-sep-2009, 13:45:56
 */

package eu.scy.client.desktop.scydesktop.scywindows.window;

import javafx.scene.Scene;
import javafx.stage.Stage;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * @author sikkenj
 */

// place your code here
public class TopDrawer extends Drawer{

  // override def absoluteMinimumWidth = 2*controlSize;

   init{
      resizeYFactor = -1;
   }

   override function positionControlElements():Void{
      if (opened){
         this.translateY = -height;
      }
      else{
         this.translateY = 1;
      }
      closeControl.layoutX = width-1.5*closeControlSize;
      closeControl.layoutY = height-1.5*closeControlSize;
      resizeControl.layoutX = width;
      resizeControl.layoutY = resizeControlSize;
      resizeControl.rotate = -90;
   }
}


function run(){

      Stage {
      title : "Test bottom drawer"
      scene: Scene {
         width: 200
         height: 200
         content: [
             Rectangle {
               x: 10, y: 100
               width: 100, height: 20
               fill: null
               stroke:Color.GRAY
               strokeWidth:4;
            }
           TopDrawer{
               closedSize:80;
               layoutX:20;
               layoutY:100
               opened:true
            }

         ]
      }
   }


}
