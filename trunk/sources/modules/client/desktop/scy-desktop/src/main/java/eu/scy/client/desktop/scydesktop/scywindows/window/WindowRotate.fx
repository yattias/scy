/*
 * WindowRotate.fx
 *
 * Created on 3-sep-2009, 17:37:22
 */

package eu.scy.client.desktop.scydesktop.scywindows.window;

import javafx.scene.Group;
import javafx.scene.Node;

import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;

/**
 * @author sikkenj
 */

// place your code here
public class WindowRotate extends WindowActiveElement {

   public var startRotate: function(e: MouseEvent):Void;
   public var doRotate: function(e: MouseEvent):Void;
   public var stopRotate: function(e: MouseEvent):Void;

   public override function create(): Node {
      Group{ // bottom left rotate element
         blocksMouse: true;
         content: [
            Arc {
               centerX: 0,
               centerY: 0,
               radiusX: size,
               radiusY: size
               startAngle: 180,
               length: 90
               type: ArcType.OPEN
               fill: Color.TRANSPARENT
               stroke: bind
               if (highLighted) subColor else color
               strokeWidth: bind strokeWidth;
            }
            Arc {
               centerX: 0,
               centerY: 0,
               radiusX: size,
               radiusY: size
               startAngle: 180,
               length: 90
               type: ArcType.OPEN
               fill: Color.TRANSPARENT
               stroke: bind
               if (highLighted) color else subColor
               strokeWidth: bind strokeWidth / 2;
            }
         ]
         onMousePressed: function( e: MouseEvent ):Void {
            activate();
            startRotate(e);
         }
         onMouseDragged: function( e: MouseEvent ):Void {
            doRotate(e);
         }
         onMouseReleased: function( e: MouseEvent ):Void {
            stopRotate(e);
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

