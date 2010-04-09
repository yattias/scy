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
import javafx.scene.control.Button;



/**
 * @author sikkenj
 */

// place your code here
public class RightDrawer extends Drawer{

   //override def absoluteMinimumHeight = 2*controlSize;
   override def horizontal = false;

   override function positionControlElements():Void{
      closeControl.layoutX = closeControlSize/2;
      closeControl.layoutY = closeControlSize/2;
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
               closedSize:80;
               content:Button {
                     text: "Button"
                     action: function() {

                     }
                  }
               layoutX:30;
               layoutY:20
               opened:true
            }

         ]
      }
   }


}
