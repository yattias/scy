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
   def closeCrossInset = 2 * strokeWidth;
   def outsideColor = bind if (highLighted) {if (active) subColor else color } else {if (active) color else subColor };
   def insideColor = bind if (highLighted) {if (active) color else subColor } else {if (active) subColor else color };

   public override function create(): Node {
      Group { // the close element
         cursor: Cursor.HAND
         blocksMouse: true;
         //         translateX: bind width - topLeftBlockSize / 2
         //         translateY: -topLeftBlockSize / 2
         content: [
//            Rectangle { // background rect
//               x: 0,
//               y: 0;
//               width: size,
//               height: size
//               fill: bind if (not highLighted) subColor else color
//               stroke: bind color
//               strokeWidth: strokeWidth
//            },
//            Group { // close cross
//               translateX: 0
//               translateY: 0
//               content: [
//                  Line {
//                     startX: closeCrossInset,
//                     startY: closeCrossInset
//                     endX: size - closeCrossInset,
//                     endY: size - closeCrossInset
//                     strokeWidth: strokeWidth
//                     stroke: bind if (not highLighted) color else subColor
//                  }
//                  Line {
//                     startX: closeCrossInset,
//                     startY: size - closeCrossInset
//                     endX: size - closeCrossInset,
//                     endY: closeCrossInset
//                     strokeWidth: strokeWidth
//                     stroke: bind if (not highLighted) color else subColor
//                  }
//               ]
//            }
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
               strokeWidth: strokeWidth/2
               stroke: bind insideColor;
            }
            Polyline {
               points: [size, 0, 0, size]
               strokeWidth: strokeWidth/2
               stroke: bind insideColor;
            }
         ]
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

function run() {

   Stage {
      title: "window close test"
      scene: Scene {
         width: 200
         height: 200
         content: [
            WindowClose {
               layoutX: 20
               layoutY: 20
               active: false
               highLighted: false
            }
            WindowClose {
               layoutX: 40
               layoutY: 20
               active: false
               highLighted: true
            }
            WindowClose {
               layoutX: 20
               layoutY: 40
               active: true
               highLighted: false
            }
            WindowClose {
               layoutX: 40
               layoutY: 40
               active: true
               highLighted: true
            }
         ]
      }
   }

}
