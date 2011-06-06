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
import eu.scy.client.desktop.desktoputils.art.WindowColorScheme;
import eu.scy.client.desktop.desktoputils.art.ScyColors;

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
      resizeControl.layoutX = width;
      resizeControl.layoutY = height;
   }
}


function run(){
   var windowColorScheme = WindowColorScheme.getWindowColorScheme(ScyColors.darkBlue);
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
         windowColorScheme: windowColorScheme
         layoutX:100;
         layoutY:100
      }
   var emptyWindow2 = EmptyWindow {
         width: bind width;
         height: bind height;
         controlSize: cornerRadius;
         borderWidth: borderWidth;
         windowColorScheme: windowColorScheme
         layoutX:250;
         layoutY:100
      }

      Stage {
      title : "Test bottom drawer"
      scene: Scene {
         width: 400
         height: 400
         fill: Color.ORANGE
         content: [
            emptyWindow,
            BottomDrawer{
               windowColorScheme: windowColorScheme
               closedSize:20;
               width:width-2*controlLength
               layoutX:emptyWindow.layoutX+controlLength;
               layoutY:emptyWindow.boundsInParent.maxY-borderWidth/2
               opened:true
               content:Rectangle {
                  x: 0, y: 0
                  width: 60, height: 60
                  fill: Color.RED
               }

            }
            emptyWindow2,
            BottomDrawer{
               windowColorScheme: windowColorScheme
               closedSize:20;
               width:width-2*controlLength
               layoutX:emptyWindow2.layoutX+controlLength;
               layoutY:emptyWindow2.boundsInParent.maxY-borderWidth/2
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
