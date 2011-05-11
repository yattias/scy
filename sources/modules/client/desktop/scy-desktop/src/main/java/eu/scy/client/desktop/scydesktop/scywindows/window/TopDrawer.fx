/*
 * TopDrawer.fx
 *
 * Created on 9-sep-2009, 13:45:56
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
public class TopDrawer extends Drawer{

   init{
      resizeYFactor = -1;
      openedYFactor = 0;
      closedYFactor = 1;
   }

   override function adjustClipRect():Void{
      clipRect.x = -clipSize/2;
      clipRect.y = -clipSize-borderSize/2+height;
   }

   override function positionControlElements():Void{
      super.positionControlElements();
      if (opened){
         this.translateY = -height;
      }
      else{
         this.translateY = 1;
      }
      openCloseControl.rotate = -90;
      openCloseControl.layoutX = width-closedSize/2-openCloseControl.mainRadius-handleOffset;
      openCloseControl.layoutY = -closedSize/2.0-openCloseControl.mainRadius-1;
      resizeControl.layoutX = width;
      resizeControl.layoutY = resizeControlSize+3;
      resizeControl.rotate = -90;
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
           TopDrawer{
               windowColorScheme: windowColorScheme
               closedSize:30;
               width:width-2*controlLength
               layoutX:emptyWindow.layoutX+controlLength;
               layoutY:emptyWindow.boundsInParent.minY+borderWidth
               opened:true
            }
             emptyWindow2,
           TopDrawer{
               windowColorScheme: windowColorScheme
               closedSize:30;
               width:width-2*controlLength
               layoutX:emptyWindow2.layoutX+controlLength;
               layoutY:emptyWindow2.boundsInParent.minY+borderWidth
               opened:false
            }

         ]
      }
   }


}
