/*
 * WindowContent.fx
 *
 * Created on 9-sep-2009, 17:45:15
 */
package eu.scy.client.desktop.scydesktop.scywindows.window;

import javafx.scene.CustomNode;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.layout.Container;
import eu.scy.client.desktop.scydesktop.swingwrapper.ScySwingWrapper;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import eu.scy.client.desktop.desktoputils.EmptyBorderNode;
import eu.scy.client.desktop.desktoputils.art.WindowColorScheme;

/**
 * @author sikken
 */
// place your code here
public class WindowContent extends CustomNode {

   public var width = 100.0 on replace { resizeTheContent() };
   public var height = 100.0 on replace { resizeTheContent() };
   public var content: Node on replace {
              setSwingCustomMetalTheme();
              resizeTheContent();
           };
   public var windowColorScheme: WindowColorScheme on replace { setSwingCustomMetalTheme() };
   public var activated: Boolean;
   public var activate: function(): Void;
   public var mouseClickedAction: function(: MouseEvent): Void;
   /**
    * workaround for the npe problems with swing content
    * first mouse click on content is used to "fix" problem
    * this means that the first click on the window content is lossed
    * is currently not used, a compatible fix in ScySwingWrapper is used
    */
   public var swingInitMode = false;
   public var glassPaneBlocksMouse = false on replace {
              contentGlassPane.blocksMouse = glassPaneBlocksMouse;
           }
   def contentGlassPane = Rectangle {
              blocksMouse: bind not activated or swingInitMode;
              x: 0, y: 0
              width: 140, height: 90
              fill: Color.TRANSPARENT
              //               fill: bind if (activated) Color.rgb(92,255,92,0.15) else Color.rgb(255,92,92,0.15)
              onMousePressed: function(e: MouseEvent): Void {
                 if (not activated or swingInitMode) {
                    activate();
                    if (swingInitMode) {
                       swingInitMode = false;
                       // moving this to the front, fixes the problem with swing content in the scene graph
                       toFront();
                       println("turned swingInitMode off!");
                    }
                 }
              }
           }
   def borderWidth = 0.5;

   public function resizeTheContent() {
      contentGlassPane.width = width;
      contentGlassPane.height = height;
      Container.resizeNode(content, width, height);
   //        println("content resized to {width}*{height}");
   }

   var firstMouseEnter = false;

   public override function create(): Node {
      if (content instanceof Parent) {
         (content as Parent).layout();
      }
      resizeTheContent();
      return Group {
                 content: [
                    Rectangle {
                       x: -borderWidth
                       y: -borderWidth
                       width: bind width + 2 * borderWidth
                       height: bind height + 2 * borderWidth
                       fill: null
                       strokeWidth: borderWidth
                       stroke: windowColorScheme.mainColor
                    }
                    Group {
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
                       onMousePressed: function(e: MouseEvent): Void {
                          activate();
                       }
                       onMouseClicked: mouseClickedAction
                    }
                 ]
              };
   }

   function setSwingCustomMetalTheme(): Void {
      setSwingCustomMetalTheme(content);
   }

   function setSwingCustomMetalTheme(node: Node): Void {
      if (node instanceof ScySwingWrapper) {
         println("applying custom metal theme on a ScySwingWrapper");
         def scySwingWrapper = node as ScySwingWrapper;
         scySwingWrapper.windowColorScheme = windowColorScheme;
      } else if (node instanceof Container) {
         def container = node as Container;
         for (childNode in container.content) {
            setSwingCustomMetalTheme(childNode);
         }
      } else if (node instanceof Parent) {
         def parent = node as Parent;
         for (childNode in parent.impl_getChildren()) {
            setSwingCustomMetalTheme(childNode);
         }
      } else if (node instanceof EmptyBorderNode) {
         def emptyBorderNode = node as EmptyBorderNode;
         setSwingCustomMetalTheme(emptyBorderNode.content);
      }
   }

}

