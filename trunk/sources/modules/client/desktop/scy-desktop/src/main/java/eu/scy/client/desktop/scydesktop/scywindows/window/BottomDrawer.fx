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

   init{
      closedYFactor = -1;
   }

   override function adjustClipRect():Void{
      clipRect.x = -clipSize/2;
      clipRect.y = borderSize;
   }

   override function positionControlElements():Void{
      super.positionControlElements();
      openCloseControl.rotate = 90;
      openCloseControl.layoutX = width-closedSize+closedSize/2.0-openCloseControl.mainRadius-handleOffset;
      openCloseControl.layoutY = -closedSize/2.0+openCloseControl.mainRadius+1;
      if (opened){
         openCloseControl.layoutY += height;
      }
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
               closedSize:20;
               width:width-2*controlLength
               layoutX:emptyWindow.layoutX+controlLength;
               layoutY:emptyWindow.boundsInParent.maxY-borderWidth/2
               opened:false
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
