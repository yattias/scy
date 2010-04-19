/*
 * LeftDrawer.fx
 *
 * Created on 9-sep-2009, 13:39:12
 */

package eu.scy.client.desktop.scydesktop.scywindows.window;

import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import eu.scy.client.desktop.scydesktop.art.WindowColorScheme;

/**
 * @author sikkenj
 */

// place your code here
public class LeftDrawer extends Drawer{

   //override def absoluteMinimumHeight = 2*controlSize;
   override def horizontal = false;

   init{
      resizeXFactor = -1;
   }


   override function positionControlElements():Void{
      super.positionControlElements();
      if (opened){
         this.translateX = -width;
      }
      else{
         this.translateX = 1;
      }

      openControl.rotate = 180;
      openControl.layoutX = -2*openControl.mainRadius-borderSize;
//      println("this.layoutX:{this.layoutX}, this.translateX:{this.translateX},"
//         " drawerGroup.layoutX:{drawerGroup.layoutX}, drawerGroup.translateX:{drawerGroup.translateX}");
//      closeControl.layoutX = width-1.5*closeControlSize;
//      closeControl.layoutY = closeControlSize/2;
      resizeControl.layoutX = resizeControlSize;
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
               closedSize:80;
               layoutX:100;
               layoutY:120
               opened:true
            }

         ]
      }
   }


}
