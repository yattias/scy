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
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;

/**
 * @author SikkenJ
 */
public class WindowStateControls extends WindowElement {

   public var window: ScyWindow;
   public var rotateNormalAction: function(): Void;
   public var minimizeAction: function(): Void;
   public var centerAction: function(): Void;
   public var maximizeAction: function(): Void;
   public var enableRotateNormal = true;
   public var enableMinimize = true;
   public var enableCenter = true;
   public var enableMaximize = true;
   def elementWidth = 10.0;
   def elementHeight = 10.0;
   def lineWidth = 2.0;
   def elementSpacing = 7.0;
   def backgroundColor = Color.TRANSPARENT;
   def disabledColor = Color.LIGHTGREY;
   def rotateNormalColor = bind if (enableRotateNormal) windowColorScheme.mainColor else disabledColor;
   def minmizeColor = bind if (enableMinimize) windowColorScheme.mainColor else disabledColor;
   def centerColor = bind if (enableCenter) windowColorScheme.mainColor else disabledColor;
   def maximizeColor = bind if (enableMaximize) windowColorScheme.mainColor else disabledColor;

   public override function create(): Node {
      HBox {
         blocksMouse:true
         spacing: elementSpacing
         content: [
            createRotateNormalNode(),
            createMinimizeNode(),
            createCenterNode(),
            createMaximizeNode(),
         ]
      }
   }

   function createRotateNormalNode(): Node {
      Group {
         cursor: bind if (enableRotateNormal) Cursor.HAND else null
         disable:bind not enableRotateNormal
         content: [
            createElementBackground(),
            Line {
               startX: 0, startY: elementHeight
               endX: elementWidth, endY: elementHeight
               strokeWidth: lineWidth
               stroke: bind rotateNormalColor
            }
            Arc {
               def radius = elementWidth/2 - 1;
               centerX: elementWidth / 2, centerY: elementWidth / 2
               radiusX: radius, radiusY: radius
               startAngle: 0, length: 180
               type: ArcType.OPEN
               fill: null
               strokeWidth: lineWidth
               stroke: bind rotateNormalColor
            }
         ]
         onMouseClicked: function(m: MouseEvent): Void {
            rotateNormalAction();
         }
      }
   }

   function createMinimizeNode(): Node {
      Group {
         cursor: bind if (enableMinimize) Cursor.HAND else null
         disable:bind not enableMinimize
         content: [
            createElementBackground(),
            Line {
               startX: 0, startY: elementHeight
               endX: elementWidth, endY: elementHeight
               strokeWidth: lineWidth
               stroke: bind minmizeColor
            }
         ]
         onMouseClicked: function(m: MouseEvent): Void {
            minimizeAction();
         }
      }
   }

   function createCenterNode(): Node {
      Group {
         cursor: bind if (enableCenter) Cursor.HAND else null
         disable:bind not enableCenter
         content: [
            createElementBackground(),
            Rectangle {
               x: 0, y: 0
               width: elementWidth, height: elementHeight
               fill: null
               strokeWidth: lineWidth
               stroke: bind centerColor
            }
            Rectangle {
               x: elementWidth / 4, y: elementHeight / 4
               width: elementWidth / 2, height: elementHeight / 2
               fill: bind centerColor
            }
         ]
         onMouseClicked: function(m: MouseEvent): Void {
            centerAction();
         }
      }
   }

   function createMaximizeNode(): Node {
      Group {
         cursor: bind if (enableMaximize) Cursor.HAND else null
         disable:bind not enableMaximize
         content: [
            createElementBackground(),
            Rectangle {
               x: 0, y: 0
               width: elementWidth, height: elementHeight
               fill: null
               strokeWidth: lineWidth
               stroke: bind maximizeColor
            }
         ]
         onMouseClicked: function(m: MouseEvent): Void {
            maximizeAction();
         }
      }
   }

   function createElementBackground(): Node {
      Rectangle {
         x: -lineWidth/2, y: -lineWidth/2
         width: elementWidth+lineWidth, height: elementHeight+lineWidth
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
            WindowStateControls {
               layoutX: 10
               layoutY: 50
               enableRotateNormal: false
               enableMinimize: false
               enableCenter: false
               enableMaximize: false
            }
         ]
      }
   }
}
