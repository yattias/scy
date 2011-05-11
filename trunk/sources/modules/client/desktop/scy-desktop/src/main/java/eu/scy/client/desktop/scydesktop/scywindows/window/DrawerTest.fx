/*
 * DrawerTest.fx
 *
 * Created on 9-sep-2009, 14:52:47
 */

package eu.scy.client.desktop.scydesktop.scywindows.window;

import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.Node;

import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.Scene;
import eu.scy.client.desktop.desktoputils.art.WindowColorScheme;
import eu.scy.client.desktop.desktoputils.art.ScyColors;

/**
 * @author sikkenj
 */

// place your code here

var mainColor = Color.color(0, 0.5, 0);
var drawerColor = Color.color(0.3, 0.7, 0.3);
class DrawerTest extends CustomNode {

   def width = 100.0;
   def height = 100.0;
   def controlLength = 18.0;
   def borderWidth = 4.0;
   def color = mainColor;
   def windowBackgroundColor = Color.WHITE;

   public override function create(): Node {
      return Group {
         content: [
            EmptyWindow{
               width: bind width;
               height:bind height;
               controlSize:controlLength;
               borderWidth:borderWidth;
               borderColor:bind color;
               backgroundColor:bind windowBackgroundColor;
            }

         ]
      };
   }
}

Stage {
   var windowColorScheme = WindowColorScheme.getWindowColorScheme(ScyColors.darkGray);
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
   def borderOffset = 0.0;
   title : "Drawer test"
   scene: Scene {
      width: 300
      height: 300
      content: [
         emptyWindow,
         TopDrawer{
            windowColorScheme: windowColorScheme
            closedSize:60;
            layoutX:120;
            layoutY:100-borderOffset;
         }
         RightDrawer{
            windowColorScheme: windowColorScheme
            closedSize:60;
           layoutX:200+borderOffset;
            layoutY:120;
         }
         BottomDrawer{
            windowColorScheme: windowColorScheme
            closedSize:60;
            layoutX:120;
            layoutY:200+borderOffset;
            opened:true
         }
         LeftDrawer{
            windowColorScheme: windowColorScheme
            layoutX:100-borderOffset;
            layoutY:120;
         }
      ]
   }
}

