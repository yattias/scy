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
//   var drawerColor = mainColor;
   var drawerHighliteColor = Color.WHITE;
   var borderOffset = 4;
   title : "Drawer test"
   scene: Scene {
      width: 300
      height: 300
      content: [
         DrawerTest{
            layoutX:100;
            layoutY:100;
         }
         TopDrawer{
            color:drawerColor;
            highliteColor:drawerHighliteColor;
            closedSize:60;
            layoutX:120;
            layoutY:100-borderOffset;
         }
         RightDrawer{
            color:drawerColor;
            highliteColor:drawerHighliteColor;
            closedSize:60;
           layoutX:200+borderOffset;
            layoutY:120;
         }
         BottomDrawer{
            color:drawerColor;
            highliteColor:drawerHighliteColor;
            closedSize:60;
            layoutX:120;
            layoutY:200+borderOffset;
            opened:true
         }
         LeftDrawer{
            highliteColor:drawerHighliteColor;
            closedSize:60;
            color:drawerColor;
            layoutX:100-borderOffset;
            layoutY:120;
         }
      ]
   }
}

