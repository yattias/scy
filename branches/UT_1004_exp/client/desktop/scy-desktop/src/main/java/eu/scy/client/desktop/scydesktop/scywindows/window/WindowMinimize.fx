/*
 * WindowMinimize.fx
 *
 * Created on 4-sep-2009, 15:15:10
 */

package eu.scy.client.desktop.scydesktop.scywindows.window;

import javafx.scene.Group;
import javafx.scene.Node;

import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Polygon;

import javafx.scene.Cursor;

/**
 * @author sikkenj
 */

// place your code here
public class WindowMinimize extends WindowActiveElement {

   public var minimized = false;
   public var minimizeAction: function():Void;
   public var unminimizeAction: function():Void;

   public override function create(): Node {
      var minimizeGroup = Group{
         visible:bind not minimized;
         translateX:-size/2;
         translateY:size/4;
         content: [
            Polygon {
               points: [ 0,0, size / 2,-size / 2, size,0 ]
               fill: bind if (highLighted) color else subColor
               strokeWidth: strokeWidth
               stroke: bind if (highLighted) subColor else color
            }
         ]
         onMouseClicked: function( e: MouseEvent ):Void {
            minimizeAction();
         }
      }
      var unminimizeGroup = Group{
         visible:bind minimized;
         translateX:-size/2;
         translateY:-size/4;
         content: [
            Polygon {
               points: [ 0,0, size / 2,size / 2, size,0 ]
               fill: bind if (highLighted) color else subColor
               strokeWidth: strokeWidth
               stroke: bind if (highLighted) subColor else color
            }
         ]
         onMouseClicked: function( e: MouseEvent ):Void {
            unminimizeAction();
         }
      }

      return Group {
         cursor: Cursor.HAND
         blocksMouse:true;
         translateY:strokeWidth
         content: [
            minimizeGroup,
            unminimizeGroup
         ]
         onMouseEntered: function( e: MouseEvent ):Void {
            highLighted = true;
         }
         onMouseExited: function( e: MouseEvent ):Void {
            highLighted = false;
         }
      };
   }
}

