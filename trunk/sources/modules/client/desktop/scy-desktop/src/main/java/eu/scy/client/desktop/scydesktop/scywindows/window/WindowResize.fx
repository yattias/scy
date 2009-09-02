/*
 * WindowClose.fx
 *
 * Created on 2-sep-2009, 16:36:09
 */

package eu.scy.client.desktop.scydesktop.scywindows.window;

import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.Node;

import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Polyline;

import javafx.scene.paint.Color;

/**
 * @author sikkenj
 */

// place your code here
public class WindowResize extends CustomNode {

   public var controlLength = 10.0;
   public var color = Color.RED;
   public var subColor = Color.WHITE;
   public var strokeWidth = 4.0;
   public var allowResize = true;

   public var activate: function():Void;
   public var startResize: function(e: MouseEvent):Void;
   public var doResize: function(e: MouseEvent):Void;
   public var stopResize: function(e: MouseEvent):Void;

   var resizeHighLighted = false;

   public override function create(): Node {
      Group{ // bottom right resize element
         blocksMouse: true;
         cursor: Cursor.NW_RESIZE;
         content: [
            Polyline {
               points: [ 0,-controlLength, 0,0, -controlLength,0 ]
               stroke: bind
                    if (resizeHighLighted) subColor else color
               strokeWidth: bind strokeWidth;
            }
            Polyline {
               points: [ 0,-controlLength, 0,0, -controlLength,0 ]
               stroke: bind
                    if (resizeHighLighted) color else subColor
               strokeWidth: bind strokeWidth / 2;
            }
         ]
         onMousePressed: function( e: MouseEvent ):Void {
            activate();
            if (allowResize){
               startResize(e);
            }
//            else {
//               openWindow(minimumWidth,minimumHeight);
//            }
         }
         onMouseDragged: function( e: MouseEvent ):Void {
            if (allowResize){
               doResize(e);
            }
         }
         onMouseReleased: function( e: MouseEvent ):Void {
            if (allowResize){
               stopResize(e);
            }
         }
         onMouseEntered: function( e: MouseEvent ):Void {
            resizeHighLighted = true;
         }
         onMouseExited: function( e: MouseEvent ):Void {
            resizeHighLighted = false;
         }
      }
   }
}

