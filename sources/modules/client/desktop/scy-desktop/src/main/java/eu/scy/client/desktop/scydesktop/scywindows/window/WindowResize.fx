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
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Line;
import javafx.scene.Scene;
import javafx.stage.Stage;
import eu.scy.client.desktop.desktoputils.art.WindowColorScheme;
import eu.scy.client.desktop.desktoputils.art.ScyColors;


/**
 * @author sikkenj
 */

// place your code here
public class WindowResize extends WindowActiveElement {

   public var startResize: function(e: MouseEvent):Void;
   public var doResize: function(e: MouseEvent):Void;
   public var stopResize: function(e: MouseEvent):Void;

   protected override var scaledTranslateXCorrection2 = -6.0;
   protected override var scaledTranslateYCorrection2 = -6.0;

   def secondLineLength = size - 3;

   var resizing = false;

   public override function create(): Node {
      def separationStart = separatorLength+size-borderWidth/2;
      def separationEnd = size+borderWidth/2;
      def mainStart = size;
      Group{ // bottom right resize element
         blocksMouse: true;
         cursor: Cursor.NW_RESIZE;
         content: [
            Rectangle {
               x: -size, y: -size
               width: size+secondLineSeparation, height: size+secondLineSeparation
               fill: transparentColor
            }
            Line { // horizontal main line
               startX: 0, startY: -mainStart
               endX: 0 , endY: 0
               strokeWidth: borderWidth
               stroke: bind windowColorScheme.mainColor
            }
            Line { // vertical main line
               startX: -mainStart, startY: 0
               endX: -0 , endY: 0
               strokeWidth: borderWidth
               stroke: bind windowColorScheme.mainColor
            }
            Line { // top separator
               startX: 0, startY: -separationStart
               endX: 0 , endY: -separationEnd
               strokeWidth: borderWidth
               stroke: windowColorScheme.emptyBackgroundColor
            }
            Line { // left separator
               startX: -separationStart, startY: 0
               endX: -separationEnd , endY: 0
               strokeWidth: borderWidth
               stroke: windowColorScheme.emptyBackgroundColor
            }
            Line {
               startX: secondLineSeparation, startY: secondLineSeparation-secondLineLength
               endX: secondLineSeparation , endY: secondLineSeparation
               strokeWidth: borderWidth/2
               stroke: bind windowColorScheme.mainColor
            }
            Line {
               startX: secondLineSeparation-secondLineLength, startY: secondLineSeparation
               endX: secondLineSeparation , endY: secondLineSeparation
               strokeWidth: borderWidth/2
               stroke: bind windowColorScheme.mainColor
            }
         ]
         onMousePressed: function( e: MouseEvent ):Void {
            activate();
            startResize(e);
            bubbleManager.pauze();
            resizing = true;
         }
         onMouseDragged: function( e: MouseEvent ):Void {
            doResize(e);
         }
         onMouseReleased: function( e: MouseEvent ):Void {
            stopResize(e);
            resizing = false;
            bubbleManager.resume();
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

function run(){
   var windowColorScheme = WindowColorScheme.getWindowColorScheme(ScyColors.darkGray);
   def width = 50.0;
   def height = 50.0;
   def borderWidth = 2.0;
   def controlLength = 10.0;
   def cornerRadius = 10;
   var emptyWindow = EmptyWindow{
      width: bind width;
      height:bind height;
      controlSize:cornerRadius;
      borderWidth:borderWidth;
      windowColorScheme:windowColorScheme
   }
   var resizeElement = WindowResize{
      size:controlLength;
      borderWidth:borderWidth;
      windowColorScheme:windowColorScheme
//         activate: activate;
//      rotateWindow:this;
      layoutX: bind width
      layoutY: bind height
   }

   def scale = 4.0;
   Stage {
	title : "Test resize"
	scene: Scene {
		width: 200
		height: 200
      fill:Color.LIGHTGRAY
		content: [
         Group{
            layoutX:40
            layoutY:40
            scaleX:scale
            scaleY:scale
            content:[
               emptyWindow,
               resizeElement
            ]
         }

      ]
	}
}

}
