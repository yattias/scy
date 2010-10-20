/*
 * LeftDrawer.fx
 *
 * Created on 9-sep-2009, 13:39:12
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
      openCloseControl.layoutX = -2*openCloseControl.mainRadius-borderSize+1;
      openCloseControl.layoutY = handleOffset;
      resizeControl.layoutX = resizeControlSize+2;
      resizeControl.layoutY = height;
      resizeControl.rotate = 90;
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
      title : "Test left drawer"
      scene: Scene {
         width: 300
         height: 300
         content: [
            emptyWindow,
            LeftDrawer{
               windowColorScheme: highcontrastColorScheme
               closedSize:40;
               height:height-2*controlLength
               layoutX:emptyWindow.boundsInParent.minX+borderWidth
               layoutY:emptyWindow.boundsInParent.minY+borderWidth+controlLength
               opened:true
            }

         ]
      }
   }


}
