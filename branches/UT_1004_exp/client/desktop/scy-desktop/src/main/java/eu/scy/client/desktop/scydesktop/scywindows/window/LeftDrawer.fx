/*
 * LeftDrawer.fx
 *
 * Created on 9-sep-2009, 13:39:12
 */

package eu.scy.client.desktop.scydesktop.scywindows.window;

import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

/**
 * @author sikkenj
 */

// place your code here
public class LeftDrawer extends Drawer{

   //override def absoluteMinimumHeight = 2*controlSize;
   override def horizontal = false;

   init{
      resizeXFactor = -1;
   }


   override function positionControlElements():Void{
      if (opened){
         this.translateX = -width;
      }
      else{
         this.translateX = 1;
      }
//      println("this.layoutX:{this.layoutX}, this.translateX:{this.translateX},"
//         " drawerGroup.layoutX:{drawerGroup.layoutX}, drawerGroup.translateX:{drawerGroup.translateX}");
      closeControl.layoutX = width-1.5*closeControlSize;
      closeControl.layoutY = closeControlSize/2;
      resizeControl.layoutX = resizeControlSize;
      resizeControl.layoutY = height;
      resizeControl.rotate = 90;
   }
}


function run(){

      Stage {
      title : "Test left drawer"
      scene: Scene {
         width: 200
         height: 200
         content: [
            Rectangle {
               x: 100, y: 10
               width: 20, height: 100
               fill: null
               stroke:Color.GRAY
               strokeWidth:4;
            }
            LeftDrawer{
               closedSize:80;
               layoutX:100-3;
               layoutY:20
               opened:true
            }

         ]
      }
   }


}
