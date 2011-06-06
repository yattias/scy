/*
 * LeftDrawer.fx
 *
 * Created on 9-sep-2009, 13:39:12
 */

package eu.scy.client.desktop.scydesktop.scywindows.window;

import javafx.scene.Scene;
import javafx.stage.Stage;
import eu.scy.client.desktop.desktoputils.art.WindowColorScheme;
import eu.scy.client.desktop.desktoputils.art.ScyColors;
import javafx.scene.paint.Color;

/**
 * @author sikkenj
 */

// place your code here
public class LeftDrawer extends Drawer{

   override def horizontal = false;

   init{
      resizeXFactor = -1;
      openedXFactor = 0;
      closedXFactor = 1;
    }

   override function adjustClipRect():Void{
      clipRect.x = -clipSize-borderSize/2+width;
      clipRect.y = -clipSize/2;
   }

   override function positionControlElements():Void{
      super.positionControlElements();
      if (opened){
         this.translateX = -width;
      }
      else{
         this.translateX = 1;
      }

      openCloseControl.rotate = 180;
      openCloseControl.textRotate = 180;
      openCloseControl.layoutX = -2*openCloseControl.mainRadius-borderSize-1.0;
      openCloseControl.layoutY = handleOffset;
      if (not opened){
         openCloseControl.layoutX -= 2;
      }

      resizeControl.layoutX = resizeControlSize+2;
      resizeControl.layoutY = height;
      resizeControl.rotate = 90;
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
         layoutX:100;
         layoutY:250
      }

      Stage {
      title : "Test left drawer"
      scene: Scene {
         width: 400
         height: 400
         fill: Color.ORANGE
         content: [
            emptyWindow,
            LeftDrawer{
               windowColorScheme: windowColorScheme
               closedSize:40;
               height:height-2*controlLength
               layoutX:emptyWindow.boundsInParent.minX+borderWidth
               layoutY:emptyWindow.boundsInParent.minY+borderWidth+controlLength
               opened:true
            }
            emptyWindow2,
            LeftDrawer{
               windowColorScheme: windowColorScheme
               closedSize:40;
               height:height-2*controlLength
               layoutX:emptyWindow2.boundsInParent.minX+borderWidth
               layoutY:emptyWindow2.boundsInParent.minY+borderWidth+controlLength
               opened:false
            }

         ]
      }
   }


}
