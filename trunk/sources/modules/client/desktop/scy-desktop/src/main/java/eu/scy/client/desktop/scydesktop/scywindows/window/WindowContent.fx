/*
 * WindowContent.fx
 *
 * Created on 9-sep-2009, 17:45:15
 */

package eu.scy.client.desktop.scydesktop.scywindows.window;

import javafx.scene.CustomNode;
import javafx.scene.Node;


import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Resizable;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.lang.Void;

/**
 * @author sikken
 */

// place your code here
public class WindowContent extends CustomNode {

   public var width = 100.0 on replace {resizeTheContent()};
   public var height = 100.0 on replace {resizeTheContent()};
   public var content:Node;

   public var activate: function():Void;

   public var glassPaneBlocksMouse = false on replace{
      contentGlassPane.blocksMouse = glassPaneBlocksMouse;
   }


   def contentGlassPane = Rectangle {
      x: 0, y: 0
      width: 140, height: 90
      fill: Color.TRANSPARENT
//        fill: Color.rgb(128,128,128,0.5)
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

