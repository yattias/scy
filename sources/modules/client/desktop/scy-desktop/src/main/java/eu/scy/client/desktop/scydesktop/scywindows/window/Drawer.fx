/*
 * Drawer.fx
 *
 * Created on 9-sep-2009, 11:18:17
 */
package eu.scy.client.desktop.scydesktop.scywindows.window;

import javafx.scene.CustomNode;
import javafx.scene.Node;

import javafx.scene.paint.Color;


import javafx.scene.shape.Rectangle;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Resizable;
import javafx.scene.shape.Line;
import javafx.util.Math;
import eu.scy.client.desktop.scydesktop.scywindows.window.MouseBlocker;
import eu.scy.client.desktop.scydesktop.scywindows.window.WindowClose;
import eu.scy.client.desktop.scydesktop.scywindows.window.WindowContent;
import eu.scy.client.desktop.scydesktop.scywindows.window.WindowResize;
import eu.scy.client.desktop.scydesktop.art.WindowColorScheme;

/**
 * @author sikkenj
 */
// place your code here

public abstract class Drawer extends CustomNode {

   public var windowColorScheme:WindowColorScheme;
//   public var color = Color.RED;
//   public var subColor = Color.WHITE;
//   public var highliteColor = Color.WHITE;
   public var borderSize = 2.0;
//   public var closedStrokeWidth = 2.0;
   public var closedSize = 40.0;
   public var content: Node;
   public var activated = false; // TODO, make only changeable from (sub) package
   public var activate: function():Void;

   def sideContentBorder = 5.0;
   def topContentBorder = 19.0;
   def bottomContentBorder = 10.0;
   protected var horizontal = true;
   protected def resizeControlSize = 10.0;
   protected def closeControlSize = 10.0;
   protected var opened = false;
   protected var width = 50.0 on replace {
              sizeChanged()
           };
   protected var height = 50.0 on replace {
              sizeChanged()
           };
   protected var absoluteMinimumWidth = closeControlSize+resizeControlSize;
   protected var absoluteMinimumHeight = closeControlSize+resizeControlSize;
   protected var resizeXFactor = 1.0;
   protected var resizeYFactor = 1.0;
   protected var drawerGroup: Group;
   protected var border: Rectangle;
   protected var contentElement: WindowContent;
   protected var openControl: OpenDrawerControl;
   protected var closeControl: WindowClose;
   protected var resizeControl: WindowResize;
   protected var horizontalSizeExternal = false;
   protected var verticalSizeExternal = false;
   var originalWidth: Number;
   var originalHeight: Number;
   var openedOnce = false;
   var resizeAllowed = false;

   function sizeChanged() {
      // show a filled rect as content for test purposes
//      content = Rectangle {
//         x: -100, y: -100
//         width: 1000, height: 1000
//         fill: Color.color(1,.25,.25,.75)
//      }
      positionControlElements();
//      println("layout: ({layoutX}, {layoutY})");
//      println("openControl.layout: ({openControl.layoutX}, {openControl.layoutY}), openControl: {openControl}, {openControl.boundsInParent}");
   }

   public override function create(): Node {
      var drawerNode = bind if (opened) {
                 createOpenDrawerNode();
              } else {
                 createClosedDrawerNode();
              };
      positionControlElements();
//      println("create:layout: ({layoutX}, {layoutY})");
//      println("create:openControl.layout: ({openControl.layoutX}, {openControl.layoutY}), openControl: {openControl}, {openControl.boundsInParent}");
      drawerGroup = Group {
         content: bind [
            drawerNode
         ]
      };
   }

   function createClosedDrawerNode(): Node {
//      var rect: Rectangle;
      var mouseEntered = false;
      Group {
         cursor: Cursor.HAND;
         content: [
            openControl = OpenDrawerControl{
               windowColorScheme:windowColorScheme
               size:closedSize
            }
         ]
         onMouseClicked: function (e: MouseEvent): Void {
            opened = true;
            positionControlElements();
         }
         onMouseEntered: function (e: MouseEvent): Void {
            mouseEntered = true;
         }
         onMouseExited: function (e: MouseEvent): Void {
            mouseEntered = false;
         }
      }
   }

   function createOpenDrawerNode(): Node {
      if (not openedOnce) {
         // it will be opened for the first time, find good initial size
         if (content instanceof Parent){
            (content as Parent).layout();
         }
         if (content instanceof Resizable) {
            var resizableContent = content as Resizable;
            if (not horizontal){
               width = Math.max(resizableContent.getPrefWidth(width), absoluteMinimumWidth);
            }
            if (horizontal){
               height = Math.max(resizableContent.getPrefHeight(height), absoluteMinimumHeight);
            }
         } else if (content != null) {
            if (not horizontal){
               width = content.boundsInLocal.width;
            }
            if (horizontal){
               height = content.boundsInLocal.height;
            }
            resizeAllowed = false;
         }

      }

      Group {
         content: [
//                        Rectangle {
//                           x: 0, y: 0
//                           width: bind width, height: bind height
//                           fill: subColor
//                           strokeWidth: borderSize
//                           stroke: bind color;
//                        }
            createControlElements()
         ]
      }
   }

   function createControlElements(): Node {
      border = Rectangle {
         x: 0, y: 0
         width: bind width, height: bind height
         fill: bind windowColorScheme.backgroundColor
         strokeWidth: borderSize
         stroke: bind windowColorScheme.mainColor;
      }
      contentElement = WindowContent {
         windowColorScheme:windowColorScheme
         width: bind width - 2 * sideContentBorder - borderSize - 1;
         height: bind height - topContentBorder - bottomContentBorder - borderSize - 1;
         content: bind content;
         activated: bind activated;
         activate:activate
         layoutX: sideContentBorder + borderSize / 2 + 1;
         layoutY: topContentBorder + borderSize / 2 + 1;
      }
      closeControl = WindowClose {
         size: closeControlSize;
//         strokeWidth: 4;
         windowColorScheme:windowColorScheme
//         color: bind color;
//         subColor: bind subColor;
         activated:false
         activate:activate
//         outlineFactor:0.5
         closeAction: function () {
            opened = false;
            positionControlElements();
         }
      }
      if (resizeAllowed) {
         resizeControl = WindowResize {
            size: resizeControlSize;
            windowColorScheme:windowColorScheme
//            color: bind color;
//            subColor: bind subColor;
            activate:activate
            startResize: startResize;
            doResize: doResize;
            stopResize: stopResize;
         }
      }
      Group {
         content: [
            border,
            contentElement,
            closeControl,
            resizeControl
         ]
      }
   }

   protected function positionControlElements(): Void {
      closeControl.layoutX = width-closeControlSize-sideContentBorder;
      closeControl.layoutY = topContentBorder/2-closeControlSize/2 + borderSize/2;

   }

   function startResize(e: MouseEvent): Void {
      originalWidth = width;
      originalHeight = height;
//      contentElement.glassPaneBlocksMouse = true;
      MouseBlocker.startMouseBlocking();
   }

   function doResize(e: MouseEvent): Void {
      var desiredWidth = originalWidth + resizeXFactor * e.dragX;
      var desiredHeight = originalHeight + resizeYFactor * e.dragY;
      desiredWidth = Math.max(desiredWidth, absoluteMinimumWidth);
      desiredHeight = Math.max(desiredHeight, absoluteMinimumHeight);
      if (content instanceof Resizable) {
         var resizableContent = content as Resizable;
         desiredWidth = Math.max(desiredWidth, resizableContent.getMinWidth());
         desiredHeight = Math.max(desiredHeight, resizableContent.getMinHeight());
         desiredWidth = Math.min(desiredWidth, resizableContent.getMaxWidth());
         desiredHeight = Math.min(desiredHeight, resizableContent.getMaxHeight());
      }

      width = desiredWidth;
      height = desiredHeight;
   }

   function stopResize(e: MouseEvent): Void {
//      contentElement.glassPaneBlocksMouse = false;
      MouseBlocker.stopMouseBlocking();
   }
}

