/*
 * WindowContent.fx
 *
 * Created on 9-sep-2009, 17:45:15
 */

package eu.scy.client.desktop.scydesktop.scywindows.window;

import javafx.scene.CustomNode;
import javafx.scene.Node;


import javafx.scene.layout.Resizable;
import java.lang.Void;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * @author sikken
 */

// place your code here
public class WindowContent extends CustomNode {

   public var width = 100.0 on replace {resizeTheContent()};
   public var height = 100.0 on replace {resizeTheContent()};
   public var content:Node;

   public var activated:Boolean;
   public var activate: function():Void;

   public var glassPaneBlocksMouse = false on replace{
      contentGlassPane.blocksMouse = glassPaneBlocksMouse;
   }


   def contentGlassPane = Rectangle {
      blocksMouse:bind not activated;
      x: 0, y: 0
      width: 140, height: 90
      fill: Color.TRANSPARENT
//      fill: Color.rgb(92,255,92,0.25)
      onMousePressed: function( e: MouseEvent ):Void {
         if (not activated){
            activate();
         }
      }
   }

	function resizeTheContent(){
      contentGlassPane.width=width;
      contentGlassPane.height = height;
      if (content instanceof Resizable){
			var resizeableContent = content as Resizable;
			resizeableContent.width = width;
			resizeableContent.height = height;
		}
	}

   public override function create(): Node {
      if (content instanceof Parent){
         (content as Parent).layout();
      }
      resizeTheContent();
      return Group {
         content: [
            Group{
               blocksMouse: true;
               cursor: Cursor.DEFAULT;
               clip: Rectangle {
                  x: 0,
                  y: 0
                  width: bind width
                  height: bind height
                  fill: Color.BLACK
               }
               content: bind [
                  content,
                  contentGlassPane
               ]
               onMousePressed: function( e: MouseEvent ):Void {
                  activate();
               }
            }
         ]
      };
   }
}
