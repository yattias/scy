/*
 * WindowMinimize.fx
 *
 * Created on 4-sep-2009, 15:15:10
 */

package eu.scy.client.desktop.scydesktop.scywindows.window;

import javafx.scene.Group;
import javafx.scene.Node;

import javafx.scene.input.MouseEvent;

import javafx.scene.Cursor;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.util.Math;
import javafx.scene.shape.Rectangle;
import javafx.scene.Scene;
import javafx.stage.Stage;
import eu.scy.client.desktop.scydesktop.art.WindowColorScheme;
import eu.scy.client.desktop.scydesktop.art.ScyColors;

/**
 * @author sikkenj
 */

// place your code here
public class WindowMinimize extends WindowActiveElement {

   public var minimized = false on replace { updateScaledTranslationCorrection()};
   public var minimizeAction: function():Void;
   public var unminimizeAction: function():Void;

   protected override var scaledTranslateXCorrection2 = 0;
//   protected override var scaledTranslateYCorrection2 = 3.5;

   def color = Color.RED;
   def subColor = Color.RED;
   def scaledTranslateYCorrection2Value = 3.5;

   def horizontalSize2 = size/Math.sqrt(2.0);

   function updateScaledTranslationCorrection(){
      if (minimized){
         scaledTranslateYCorrection2 = scaledTranslateYCorrection2Value;
      }
      else{
         scaledTranslateYCorrection2 = -scaledTranslateYCorrection2Value;
      }
   }


   public override function create(): Node {
      updateScaledTranslationCorrection();
      def separationStart = separatorLength+horizontalSize2-borderWidth/2;
      //def separationEnd = horizontalSize2;
      def secondLineStart = horizontalSize2-secondLineSeparation+0.25;
      def secondLineHeight = horizontalSize2-secondLineSeparation;
      def strokeWidthSecondLine = borderWidthSecondLine+0.5;
      var minimizeGroup = Group{
         visible:bind not minimized;
         content: [
            Rectangle {
               x: -horizontalSize2, y: -horizontalSize2
               width: 2*horizontalSize2, height: horizontalSize2
               fill: transparentColor
            }
            Line { // main line up
               startX: -horizontalSize2, startY: 0
               endX: 0 , endY: -horizontalSize2
               strokeWidth: borderWidth
               stroke: bind windowColorScheme.mainColor
            }
            Line { // main line down
               startX: 0, startY: -horizontalSize2
               endX: horizontalSize2 , endY: 0
               strokeWidth: borderWidth
               stroke: bind windowColorScheme.mainColor
            }
            Line { // second line up
               startX: -secondLineStart, startY: 0
               endX: 0 , endY: -secondLineHeight
               strokeWidth: strokeWidthSecondLine
               stroke: bind windowColorScheme.mainColor
            }
            Line { // second line down
               startX: 0, startY: -secondLineStart
               endX: secondLineHeight , endY: 0
               strokeWidth: strokeWidthSecondLine
               stroke: bind windowColorScheme.mainColor
            }
         ]
         onMouseClicked: function( e: MouseEvent ):Void {
            minimizeAction();
         }
      }
      var unminimizeGroup = Group{
         visible:bind minimized;
         content: [
            Rectangle {
               x: -horizontalSize2, y: 0
               width: 2*horizontalSize2, height: horizontalSize2
               fill: transparentColor
            }
            Line {
               startX: -horizontalSize2, startY: 0
               endX: 0 , endY: horizontalSize2
               strokeWidth: borderWidth
               stroke: bind windowColorScheme.mainColor
            }
            Line {
               startX: 0, startY: horizontalSize2
               endX: horizontalSize2 , endY: 0
               strokeWidth: borderWidth
               stroke: bind windowColorScheme.mainColor
            }
            Line {
               startX: -secondLineStart, startY: 0
               endX: 0 , endY: secondLineHeight
               strokeWidth: strokeWidthSecondLine
               stroke: bind windowColorScheme.mainColor
            }
            Line {
               startX: 0, startY: secondLineStart
               endX: secondLineHeight , endY: 0
               strokeWidth: strokeWidthSecondLine
               stroke: bind windowColorScheme.mainColor
            }
         ]
         onMouseClicked: function( e: MouseEvent ):Void {
            unminimizeAction();
         }
      }

      return Group {
         cursor: Cursor.HAND
         blocksMouse:true;
         content: [
            Line { // lift and right seprator and middle piece
               startX: -separationStart, startY: 0
               endX: separationStart , endY: 0
               strokeWidth: borderWidth
               stroke: windowColorScheme.emptyBackgroundColor
            }
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

function run(){
   var windowColorScheme = WindowColorScheme.getWindowColorScheme(ScyColors.darkGray);
   def width = 90.0;
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
   var minimizeElement = WindowMinimize{
      size:controlLength;
      borderWidth:borderWidth;
      windowColorScheme:windowColorScheme
//         activate: activate;
//      rotateWindow:this;
      layoutX: bind width / 4;
      layoutY: bind height;
   }
   var inMinimizeElement = WindowMinimize{
      minimized:true
      size:controlLength;
      borderWidth:borderWidth;
      windowColorScheme:windowColorScheme
//         activate: activate;
//      rotateWindow:this;
      layoutX: bind 3*width / 4;
      layoutY: bind height;
   }

   def scale = 4.0;
   Stage {
	title : "Test resize"
	scene: Scene {
		width: 400
		height: 200
      fill:Color.LIGHTGRAY
		content: [
         Group{
            layoutX:140
            layoutY:20
            scaleX:scale
            scaleY:scale
            content:[
               emptyWindow,
               minimizeElement,
               inMinimizeElement
            ]
         }

      ]
	}
}

}
