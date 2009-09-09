/*
 * Drawer.fx
 *
 * Created on 9-sep-2009, 11:18:17
 */

package eu.scy.client.desktop.scydesktop.scywindows.window;

import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.Node;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;

import javafx.util.Math;

/**
 * @author sikkenj
 */

// place your code here
public abstract class Drawer extends CustomNode {

   public var color= Color.RED;
   public var subColor= Color.WHITE;
   public var highliteColor = Color.PURPLE;
   public var borderSize = 2.0;

   public var closedWidth = 3.0;
   public var closedHeight = 3.0;

   public var content:Node;

   def contentBorder = 1;

   protected def controlSize = 10.0;
   protected var opened = true;
   protected var width = 50.0 on replace{sizeChanged()};
   protected var height = 50.0 on replace{sizeChanged()};

   protected var absoluteMinimumWidth = controlSize;
   protected var absoluteMinimumHeight = controlSize;

   protected var resizeXFactor = 1.0;
   protected var resizeYFactor = 1.0;

   protected var drawerGroup:Group;
   protected var border:Rectangle;
   protected var contentElement:WindowContent;
   protected var closeControl:WindowClose;
   protected var resizeControl:WindowResize;

   var originalWidth:Number;
   var originalHeight:Number;

   function sizeChanged(){
      // show a filled rect as content for test purposes
      content = Rectangle {
         x: -100, y: -100
         width: 1000, height: 1000
         fill: Color.color(1,.25,.25,.75)
      }
       positionControlElements();
   }


   public override function create(): Node {
      var drawerNode = bind if (opened){
               createOpenDrawerNode();
            }
            else{
               createClosedDrawerNode();
            };
      positionControlElements();
      drawerGroup = Group {
         content: bind [
            drawerNode
         ]
      };
   }

   function createClosedDrawerNode(): Node{
      var rect:Rectangle;
      Group{
         cursor: Cursor.HAND;
         content:[
            rect = Rectangle {
               x: 0, y: 0
               width: closedWidth, height: closedHeight
               fill: color
//               strokeWidth: borderSize
//               stroke: bind color;
            }
         ]
         onMouseClicked: function( e: MouseEvent ):Void {
            opened = true;
            positionControlElements();
         }
         onMouseEntered: function( e: MouseEvent ):Void {
            rect.fill = highliteColor;
         }
         onMouseExited: function( e: MouseEvent ):Void {
            rect.fill = color;
         }



      }
   }

   function createOpenDrawerNode(): Node{
      Group{
         content:[
//            Rectangle {
//               x: 0, y: 0
//               width: bind width, height: bind height
//               fill: subColor
//               strokeWidth: borderSize
//               stroke: bind color;
//            }
            createControlElements()
         ]
      }
   }

   function createControlElements():Node{
      border = Rectangle {
         x: 0, y: 0
         width: bind width, height: bind height
         fill: subColor
         strokeWidth: borderSize
         stroke: bind color;
      }
      contentElement = WindowContent{
         width:bind width-2*contentBorder-borderSize-1;
         height:bind height-2*contentBorder-borderSize-1;
         content:bind content;
         layoutX:contentBorder+borderSize/2+1;
         layoutY:contentBorder+borderSize/2+1;
      }
      closeControl = WindowClose{
         size:controlSize;
         strokeWidth:1.5;
         color:bind color;
         subColor:bind subColor;
         closeAction:function(){
            opened = false;
            positionControlElements();
         }

      }
      resizeControl = WindowResize{
         size:controlSize;
         color:bind color;
         subColor:bind subColor;
         startResize:startResize;
         doResize:doResize;
         stopResize:stopResize;
      }
      Group{
         content:[
            border,
            contentElement,
            closeControl,
            resizeControl
         ]
      }
   }

   function positionControlElements():Void{

   }

   function startResize(e: MouseEvent):Void{
      originalWidth = width;
      originalHeight = height;
      contentElement.glassPaneBlocksMouse = true;
   }

   function doResize(e: MouseEvent):Void{
		width = Math.max(absoluteMinimumWidth,originalWidth+resizeXFactor*e.dragX);
		height = Math.max(absoluteMinimumHeight,originalHeight+resizeYFactor*e.dragY);
   }

   function stopResize(e: MouseEvent):Void{
      contentElement.glassPaneBlocksMouse = false;
   }


}

