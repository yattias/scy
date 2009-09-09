/*
 * RightDrawer.fx
 *
 * Created on 9-sep-2009, 11:36:40
 */

package eu.scy.client.desktop.scydesktop.scywindows.window;

import javafx.stage.Stage;
import javafx.scene.Scene;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;



/**
 * @author sikkenj
 */

// place your code here
public class RightDrawer extends Drawer{

   override function positionControlElements():Void{
      closeControl.layoutX = width-controlSize/2;
      closeControl.layoutY = -controlSize/2;
      resizeControl.layoutX = width;
      resizeControl.layoutY = height;
   }
}


function run(){

      Stage {
      title : "Test right drawer"
      scene: Scene {
         width: 200
         height: 200
         content: [
            Rectangle {
               x: 10, y: 10
               width: 20, height: 100
               fill: null
               stroke:Color.GRAY
               strokeWidth:4;
            }
            RightDrawer{
               closedHeight:80;
               layoutX:30;
               layoutY:20
            }

         ]
      }
   }


}
