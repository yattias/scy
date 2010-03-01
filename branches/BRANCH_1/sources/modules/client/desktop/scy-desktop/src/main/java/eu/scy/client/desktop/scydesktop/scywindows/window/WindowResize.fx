/*
 * WindowClose.fx
 *
 * Created on 2-sep-2009, 16:36:09
 */

package eu.scy.client.desktop.scydesktop.scywindows.window;

import javafx.scene.Group;
import javafx.scene.Node;

import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Polyline;


/**
 * @author sikkenj
 */

// place your code here
public class WindowResize extends WindowActiveElement {

   public var startResize: function(e: MouseEvent):Void;
   public var doResize: function(e: MouseEvent):Void;
   public var stopResize: function(e: MouseEvent):Void;

   var resizing = false;

   public override function create(): Node {
      Group{ // bottom right resize element
         blocksMouse: true;
         cursor: Cursor.NW_RESIZE;
         content: [
            Polyline {
               points: [ 0,-size, 0,0, -size,0 ]
               stroke: bind
                    if (highLighted or resizing) subColor else color
               strokeWidth: bind strokeWidth;
            }
            Polyline {
               points: [ 0,-size, 0,0, -size,0 ]
               stroke: bind
                    if (highLighted or resizing) color else subColor
               strokeWidth: bind strokeWidth / 2;
            }
         ]
         onMousePressed: function( e: MouseEvent ):Void {
            activate();
            startResize(e);
            resizing = true;
         }
         onMouseDragged: function( e: MouseEvent ):Void {
            doResize(e);
         }
         onMouseReleased: function( e: MouseEvent ):Void {
            stopResize(e);
            resizing = false;
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

