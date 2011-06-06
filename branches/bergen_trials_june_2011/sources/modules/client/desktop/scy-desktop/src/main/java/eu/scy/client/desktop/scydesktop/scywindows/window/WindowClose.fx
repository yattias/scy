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
import eu.scy.client.desktop.desktoputils.art.WindowColorScheme;
import eu.scy.client.desktop.desktoputils.art.ScyColors;

/**
 * @author sikkenj
 */
// place your code here
public class WindowClose extends WindowActiveElement {

   public var closeAction: function(): Void;
   public var activated = true;
   public var outlineFactor = 0.5;
   public var backgroundExtender = 0.0;
//   def closeCrossInset = 2 * strokeWidth;
   def outsideColor = bind if (not activated) {
              if (highLighted) windowColorScheme.emptyBackgroundColor else windowColorScheme.mainColor
           } else {
              if (highLighted) windowColorScheme.mainColor else windowColorScheme.emptyBackgroundColor
           };
   def insideColor = bind if (not activated) {
              if (highLighted) windowColorScheme.mainColor else windowColorScheme.emptyBackgroundColor
           } else {
              if (highLighted) windowColorScheme.emptyBackgroundColor else windowColorScheme.mainColor
           };

   public override function create(): Node {
      var box = Rectangle {
               x:0, y: 0
               width: size, height: size
               fill: bind outsideColor
            }
      var fillBox = Rectangle{
         x:1
         y:1
         width: size-2
         height:size-2
         fill: bind insideColor
      }

      Group { // the close element
         cursor: Cursor.HAND
         blocksMouse: true;
         content: [
            box,
            fillBox,
            Line{
               startX:1
               startY:1
               endX:size-2
               endY:size-2
               stroke:bind outsideColor
            }
            Line{
               startX:size-2
               startY:1
               endX:1
               endY:size-2
               stroke:bind outsideColor
            }

//            Rectangle {
//               x: -backgroundExtender, y: -backgroundExtender
//               width: size+1+2*backgroundExtender, height: size+1+2*backgroundExtender
//               fill: bind if (activated) color else subColor
//            }
//            Polyline {
//               points: [0, 0, size, size]
//               strokeWidth: strokeWidth
//               stroke: bind outsideColor;
//            }
//            Polyline {
//               points: [size, 0, 0, size]
//               strokeWidth: strokeWidth
//               stroke: bind outsideColor;
//            }
//            Polyline {
//               points: [0, 0, size, size]
//               strokeWidth: outlineFactor * strokeWidth
//               stroke: bind insideColor;
//            }
//            Polyline {
//               points: [size, 0, 0, size]
//               strokeWidth: outlineFactor * strokeWidth
//               stroke: bind insideColor;
//            }
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
   var windowColorScheme = WindowColorScheme.getWindowColorScheme(ScyColors.darkGray);

   Stage {
      title: "window close test"
      scene: Scene {
         width: 200
         height: 200
         content: [
            WindowClose {
               windowColorScheme: windowColorScheme
               layoutX: 20
               layoutY: 20
               activated: false
               highLighted: false
            }
            WindowClose {
               windowColorScheme: windowColorScheme
               layoutX: 50
               layoutY: 20
               activated: false
               highLighted: true
            }
            Rectangle {
               x: 15, y: 40
               width: 140, height: 30
               fill: windowColorScheme.mainColor
            }
            WindowClose {
               windowColorScheme: windowColorScheme
               layoutX: 20
               layoutY: 50
               activated: true
               highLighted: false
            }
            WindowClose {
               windowColorScheme: windowColorScheme
               layoutX: 50
               layoutY: 50
               activated: true
               highLighted: true
            }
         ]
      }
   }

}
