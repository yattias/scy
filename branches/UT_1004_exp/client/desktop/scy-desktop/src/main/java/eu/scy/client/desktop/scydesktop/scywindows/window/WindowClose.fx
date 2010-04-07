/*
 * WindowClose.fx
 *
 * Created on 4-sep-2009, 14:33:40
 */
package eu.scy.client.desktop.scydesktop.scywindows.window;

import javafx.scene.Group;
import javafx.scene.Node;

import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;

/**
 * @author sikkenj
 */
// place your code here
public class WindowClose extends WindowActiveElement {

   public


    var closeAction: function(): Void;
   public var activated = true;
   public var outlineFactor = 0.5;
   public var backgroundExtender = 0.0;
   def closeCrossInset = 2 * strokeWidth;
   def outsideColor = bind if (activated) {
              if (highLighted) subColor else color
           } else {
              if (highLighted) color else subColor
           };
   def insideColor = bind if (activated) {
              if (highLighted) color else subColor
           } else {
              if (highLighted) subColor else color
           };

   public override function create(): Node {
      Group { // the close element
         cursor: Cursor.HAND
         blocksMouse: true;
         content: [
            Rectangle {
               x: -backgroundExtender, y: -backgroundExtender
               width: size+1+2*backgroundExtender, height: size+1+2*backgroundExtender
               fill: bind if (activated) color else subColor
            }
            Polyline {
               points: [0, 0, size, size]
               strokeWidth: strokeWidth
               stroke: bind outsideColor;
            }
            Polyline {
               points: [size, 0, 0, size]
               strokeWidth: strokeWidth
               stroke: bind outsideColor;
            }
            Polyline {
               points: [0, 0, size, size]
               strokeWidth: outlineFactor * strokeWidth
               stroke: bind insideColor;
            }
            Polyline {
               points: [size, 0, 0, size]
               strokeWidth: outlineFactor * strokeWidth
               stroke: bind insideColor;
            }
         ]
         onMousePressed: function( e: MouseEvent ):Void {
            activate();
         }
         onMouseClicked: function (e: MouseEvent): Void {
            closeAction();
         }
         onMouseEntered: function (e: MouseEvent): Void {
            highLighted = true;
         }
         onMouseExited: function (e: MouseEvent): Void {
            highLighted = false;
         }
      }
   }
}

function run()   {

   Stage {
      title: "window close test"
      scene: Scene {
         width: 200
         height: 200
         content: [
            WindowClose {
               layoutX: 20
               layoutY: 20
               activated: false
               highLighted: false
            }
            WindowClose {
               layoutX: 50
               layoutY: 20
               activated: false
               highLighted: true
            }
            Rectangle {
               x: 15, y: 40
               width: 140, height: 30
               fill: Color.GREEN
            }
            WindowClose {
               layoutX: 20
               layoutY: 50
               activated: true
               highLighted: false
            }
            WindowClose {
               layoutX: 50
               layoutY: 50
               activated: true
               highLighted: true
            }
         ]
      }
   }

}
