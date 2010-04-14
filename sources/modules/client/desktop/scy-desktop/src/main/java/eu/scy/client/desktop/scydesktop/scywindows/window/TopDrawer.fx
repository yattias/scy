/*
 * TopDrawer.fx
 *
 * Created on 9-sep-2009, 13:45:56
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
public class TopDrawer extends Drawer{

  // override def absoluteMinimumWidth = 2*controlSize;

   init{
      resizeYFactor = -1;
   }

   override function positionControlElements():Void{
      super.positionControlElements();
      if (opened){
         this.translateY = -height;
      }
      else{
         this.translateY = 1;
      }
      openControl.rotate = -90;
//      openControl.layoutX = layoutX-openControl.mainRadius;
      openControl.layoutX = width-closedSize/2-openControl.mainRadius;
      openControl.layoutY = -5*openControl.mainRadius-1;
//      closeControl.layoutX = width-1.5*closeControlSize;
//      closeControl.layoutY = height-1.5*closeControlSize;
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
               closedSize:80;
               layoutX:120;
               layoutY:100
               opened:true
            }

         ]
      }
   }


}
