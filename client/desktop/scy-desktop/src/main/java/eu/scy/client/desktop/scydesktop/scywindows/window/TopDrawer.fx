/*
 * TopDrawer.fx
 *
 * Created on 9-sep-2009, 13:45:56
 */

package eu.scy.client.desktop.scydesktop.scywindows.window;

import javafx.scene.Scene;
import javafx.stage.Stage;

import javafx.scene.paint.Color;
import eu.scy.client.desktop.scydesktop.art.WindowColorScheme;

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
      resizeControl.layoutY = resizeControlSize;
      resizeControl.rotate = -90;
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
           TopDrawer{
               windowColorScheme: highcontrastColorScheme
               closedSize:30;
               width:width-2*controlLength
               layoutX:emptyWindow.layoutX+controlLength;
               layoutY:emptyWindow.boundsInParent.minY+borderWidth
               opened:false
            }

         ]
      }
   }


}
