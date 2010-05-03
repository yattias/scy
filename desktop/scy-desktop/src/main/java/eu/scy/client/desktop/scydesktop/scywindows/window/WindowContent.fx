/*
 * WindowContent.fx
 *
 * Created on 9-sep-2009, 17:45:15
 */

package eu.scy.client.desktop.scydesktop.scywindows.window;

import javafx.scene.CustomNode;
import javafx.scene.Node;


import java.lang.Void;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import eu.scy.client.desktop.scydesktop.art.WindowColorScheme;
import javafx.scene.layout.Container;

/**
 * @author sikken
 */

// place your code here
public class WindowContent extends CustomNode {

   public var width = 100.0 on replace {resizeTheContent()};
   public var height = 100.0 on replace {resizeTheContent()};
   public var content:Node on replace {resizeTheContent() };
   public var windowColorScheme:WindowColorScheme;

   public var activated:Boolean;
   public var activate: function():Void;

   public var glassPaneBlocksMouse = false on replace{
      contentGlassPane.blocksMouse = glassPaneBlocksMouse;
   }


   def contentGlassPane = Rectangle {
      blocksMouse:bind not activated;
      x: 0, y: 0
      width: 140, height: 90
//      fill: Color.TRANSPARENT
      fill: bind if (activated) Color.rgb(92,255,92,0.25) else Color.rgb(255,92,92,0.25)
      onMousePressed: function( e: MouseEvent ):Void {
         if (not activated){
            activate();
         }
      }
   }

   def borderWidth = 0.5;

	function resizeTheContent(){
      contentGlassPane.width=width;
      contentGlassPane.height = height;
      Container.resizeNode(content, width, height);
      println("content resized to {width}*{height}");
//      if (content instanceof Resizable){
//			var resizeableContent = content as Resizable;
//			resizeableContent.width = width;
//			resizeableContent.height = height;
//		}
	}

   public override function create(): Node {
      if (content instanceof Parent){
         (content as Parent).layout();
      }
      resizeTheContent();
      return Group {
         content: [
            Rectangle{
               x:-borderWidth
               y:-borderWidth
               width:bind width+2*borderWidth
               height:bind height+2*borderWidth
               fill:null
               strokeWidth:borderWidth
               stroke:windowColorScheme.mainColor
            }

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

