/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.scywindows.window;

import javafx.scene.Group;
import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import javafx.scene.shape.Line;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;

/**
 * @author SikkenJ
 */
public class WindowStateControls extends WindowElement {

   public var window: ScyWindow;
   def elementWidth = 10.0;
   def elementHeight = 10.0;
   def lineWidth = 2.0;
   def elementSpacing = 5.0;

   def backgroundColor = Color.TRANSPARENT;

   public override function create(): Node {
      HBox {
         spacing: elementSpacing
         content: [
            createMinimizeNode(),
            createCenterNode(),
            createMaximizeNode()
         ]
      }
   }

   function createMinimizeNode(): Node {
      Group {
         content: [
            createElementBackground(),
            Line {
               startX: 0, startY: elementHeight
               endX: elementWidth, endY: elementHeight
               strokeWidth: lineWidth
               stroke: bind windowColorScheme.mainColor
            }
         ]
      }

   }

   function createCenterNode(): Node {
      Group {
         content: [
            createElementBackground(),
            Rectangle {
               x: 0, y: 0
               width: elementWidth, height: elementHeight
               fill: null
               strokeWidth: lineWidth
               stroke: bind windowColorScheme.mainColor
            }
            Rectangle {
               x: elementWidth/4, y: elementHeight/4
               width: elementWidth/2, height: elementHeight/2
               fill: bind windowColorScheme.mainColor
            }

         ]
      }
   }

   function createMaximizeNode(): Node {
      Group {
         content: [
            createElementBackground(),
            Rectangle {
               x: 0, y: 0
               width: elementWidth, height: elementHeight
               fill: null
               strokeWidth: lineWidth
               stroke: bind windowColorScheme.mainColor
            }
         ]
      }

   }

   function createElementBackground(): Node {
      Rectangle {
         x: 0, y: 0
         width: elementWidth, height: elementHeight
         fill: backgroundColor
      }

   }

}

function run() {
   def scale = 1.0;
   Stage {
      title: "MyApp"
      onClose: function() {
      }
      scene: Scene {
         width: 200
         height: 200
         content: [
            WindowStateControls {
               layoutX: 10
               layoutY: 10
            }
         ]
      }
   }
}
