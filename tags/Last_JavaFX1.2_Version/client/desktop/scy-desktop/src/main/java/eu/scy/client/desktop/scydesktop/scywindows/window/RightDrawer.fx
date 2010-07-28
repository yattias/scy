/*
 * RightDrawer.fx
 *
 * Created on 9-sep-2009, 11:36:40
 */

package eu.scy.client.desktop.scydesktop.scywindows.window;

import javafx.stage.Stage;
import javafx.scene.Scene;

import javafx.scene.paint.Color;
import javafx.scene.control.Button;
import eu.scy.client.desktop.scydesktop.art.WindowColorScheme;



/**
 * @author sikkenj
 */

// place your code here
public class RightDrawer extends Drawer{

   override def horizontal = false;

   init{
      closedXFactor = -1;
   }

   override function adjustClipRect():Void{
      clipRect.x = borderSize;
      clipRect.y = -clipSize/2;
   }

   override function positionControlElements():Void{
      super.positionControlElements();
      openCloseControl.layoutY = handleOffset;
      openCloseControl.layoutX = 0;
      if (opened){
         openCloseControl.layoutX += width;
      }
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
      title : "Test right drawer"
      scene: Scene {
         width: 300
         height: 300
         content: [
            emptyWindow,
            RightDrawer{
               windowColorScheme: highcontrastColorScheme
               closedSize:40;
               content:Button {
                     text: "Button"
                     action: function() {

                     }
                  }
               height:height-2*controlLength
               layoutX:emptyWindow.boundsInParent.maxX-borderWidth/2
               layoutY:emptyWindow.boundsInParent.minY+borderWidth+controlLength
               opened:true
            }

         ]
      }
   }


}
