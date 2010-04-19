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

   //override def absoluteMinimumHeight = 2*controlSize;
   override def horizontal = false;

   override function positionControlElements():Void{
      super.positionControlElements();
//      closeControl.layoutX = closeControlSize/2;
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
      title : "Test right drawer"
      scene: Scene {
         width: 300
         height: 300
         content: [
            emptyWindow,
            RightDrawer{
               windowColorScheme: highcontrastColorScheme
               closedSize:80;
               content:Button {
                     text: "Button"
                     action: function() {

                     }
                  }
               layoutX:200;
               layoutY:120
               opened:true
            }

         ]
      }
   }


}
