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
import eu.scy.client.desktop.scydesktop.art.WindowColorScheme;

/**
 * @author sikkenj
 */

// place your code here
public class BottomDrawer extends Drawer{

   //override def absoluteMinimumWidth = 2*controlSize;

   override function positionControlElements():Void{
      super.positionControlElements();
      openControl.rotate = 90;
//      openControl.layoutX = layoutX-openControl.mainRadius;
//      openControl.layoutY = -2*openControl.mainRadius+borderSize;
//      closeControl.layoutX = width-1.5*closeControlSize;
//      closeControl.layoutY = closeControlSize/2;
      resizeControl.layoutX = width;
      resizeControl.layoutY = height;
   }
}


function run(){
   var highcontrastColorScheme = WindowColorScheme {
         mainColor: Color.BLUE
         backgroundColor: Color.ORANGE
         titleStartGradientColor: Color.LIGHTBLUE
         titleEndGradientColor: Color.WHITE
         emptyBackgroundColor: Color.WHITE
      }
   def width = 100.0;
   def height = 100.0;
   def borderWidth = 2.0;
   def controlLength = 10.0;
   def cornerRadius = 10;
   var emptyWindow = EmptyWindow {
         width: bind width;
         height: bind height;
         controlSize: cornerRadius;
         borderWidth: borderWidth;
         windowColorScheme: highcontrastColorScheme
         layoutX:100;
         layoutY:100
      }

      Stage {
      title : "Test bottom drawer"
      scene: Scene {
         width: 300
         height: 300
         content: [
            emptyWindow,
            BottomDrawer{
               windowColorScheme: highcontrastColorScheme
               closedSize:80;
               layoutX:120;
               layoutY:200
               opened:true
               content:Rectangle {
                  x: 0, y: 0
                  width: 60, height: 60
                  fill: Color.RED
               }

            }

         ]
      }
   }


}
