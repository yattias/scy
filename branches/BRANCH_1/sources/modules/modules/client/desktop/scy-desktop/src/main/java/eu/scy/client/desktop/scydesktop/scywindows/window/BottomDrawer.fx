/*
 * BottomDrawer.fx
 *
 * Created on 9-sep-2009, 13:43:52
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
public class BottomDrawer extends Drawer{

   //override def absoluteMinimumWidth = 2*controlSize;

   override function positionControlElements():Void{
      closeControl.layoutX = width-1.5*closeControlSize;
      closeControl.layoutY = closeControlSize/2;
      resizeControl.layoutX = width;
      resizeControl.layoutY = height;
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
               x: 10, y: 10
               width: 100, height: 20
               fill: null
               stroke:Color.GRAY
               strokeWidth:4;
            }
            BottomDrawer{
               closedSize:80;
               layoutX:20;
               layoutY:30
               opened:true
            }

         ]
      }
   }


}
