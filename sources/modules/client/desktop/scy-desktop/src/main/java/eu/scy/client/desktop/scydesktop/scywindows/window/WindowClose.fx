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

/**
 * @author sikkenj
 */

// place your code here
public class WindowClose extends WindowActiveElement {

   public var closeAction: function():Void;

   def closeCrossInset = 2*strokeWidth;

   public override function create(): Node {
      Group{ // the close element
         cursor: Cursor.HAND
         blocksMouse:true;
//         translateX: bind width - topLeftBlockSize / 2
//         translateY: -topLeftBlockSize / 2
         content: [
            Rectangle { // background rect
               x: 0,
               y: 0;
               width: size,
               height: size
               fill: bind if (not highLighted) subColor else color
               stroke: bind color
               strokeWidth: strokeWidth
            },
            Group{ // close cross
               translateX: 0
               translateY: 0
               content: [
                  Line {
                     startX: closeCrossInset,
                     startY: closeCrossInset
                     endX: size - closeCrossInset,
                     endY: size - closeCrossInset
                     strokeWidth: strokeWidth
                     stroke: bind if (not highLighted) color else subColor
                  }
                  Line {
                     startX: closeCrossInset,
                     startY: size - closeCrossInset
                     endX: size - closeCrossInset,
                     endY: closeCrossInset
                     strokeWidth: strokeWidth
                     stroke: bind if (not highLighted) color else subColor
                  }
               ]
            }
         ]
         onMouseClicked: function( e: MouseEvent ):Void {
            closeAction();
         }
         onMouseEntered: function( e: MouseEvent ):Void {
            highLighted = true;
         }
         onMouseExited: function( e: MouseEvent ):Void {
            highLighted = false;
         }
      }
   }
}

