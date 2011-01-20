/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.scywindows.window;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import eu.scy.client.desktop.scydesktop.art.WindowColorScheme;
import eu.scy.client.desktop.scydesktop.art.ArtSource;

/**
 * @author SikkenJ
 */

public class ClosedWindowResize extends WindowActiveElement {

   public var startResize: function(e: MouseEvent):Void;
   public var doResize: function(e: MouseEvent):Void;
   public var stopResize: function(e: MouseEvent):Void;

//   protected override var scaledTranslateXCorrection2 = -6.0;
//   protected override var scaledTranslateYCorrection2 = -6.0;

   def secondLineLength = size - 3;

   var resizing = false;

   public override function create(): Node {
      Group{ // bottom right resize element
         blocksMouse: true;
         cursor: Cursor.NW_RESIZE;
         content: [
            Rectangle {
               x: 0, y: 0
               width: size, height: size
               fill: transparentColor
            }
            Line { // horizontal main line
               startX: 0, startY: size
               endX: size , endY: size
               strokeWidth: borderWidth
               stroke: bind windowColorScheme.mainColor
            }
            Line { // vertical main line
               startX: size, startY: 0
               endX: size , endY: size
               strokeWidth: borderWidth
               stroke: bind windowColorScheme.mainColor
            }
         ]
         onMousePressed: function( e: MouseEvent ):Void {
            activate();
            startResize(e);
            resizing = true;
         }
         onMouseDragged: function( e: MouseEvent ):Void {
            doResize(e);
         }
         onMouseReleased: function( e: MouseEvent ):Void {
            stopResize(e);
            resizing = false;
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
   var highcontrastColorScheme = WindowColorScheme{
      mainColor:Color.BLUE
      backgroundColor:Color.ORANGE
      titleStartGradientColor:Color.LIGHTBLUE
      titleEndGradientColor:Color.WHITE
      emptyBackgroundColor:Color.WHITE
   }
   def width = 50.0;
   def height = 50.0;
   def borderWidth = 2.0;
   def controlLength = 10.0;
   def cornerRadius = 10;
   var closedWindow = ClosedWindow{
      windowColorScheme:highcontrastColorScheme
   }
   var resizeElement = ClosedWindowResize{
      size:controlLength;
      borderWidth:borderWidth;
      windowColorScheme:highcontrastColorScheme
//         activate: activate;
//      rotateWindow:this;
      layoutX: ArtSource.thumbnailWidth+ThumbnailView.eloIconOffset
      layoutY: ArtSource.thumbnailWidth
   }

   def scale = 4.0;
   Stage {
	title : "Test resize"
	scene: Scene {
		width: 400
		height: 400
      fill:Color.LIGHTGRAY
		content: [
         Group{
            layoutX:40
            layoutY:40
            scaleX:scale
            scaleY:scale
            content:[
               closedWindow,
               resizeElement
            ]
         }

      ]
	}
}

}
