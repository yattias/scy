/*
 * WindowRotate.fx
 *
 * Created on 3-sep-2009, 17:37:22
 */

package eu.scy.client.desktop.scydesktop.scywindows.window;





import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;
import javafx.util.Math;
import eu.scy.client.desktop.desktoputils.art.WindowColorScheme;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import eu.scy.client.desktop.scydesktop.scywindows.window.EmptyWindow;
import eu.scy.client.desktop.scydesktop.scywindows.window.MouseBlocker;
import javafx.scene.shape.Rectangle;
import eu.scy.client.desktop.desktoputils.art.ScyColors;

/**
 * @author sikkenj
 */

// place your code here
public class WindowRotate extends WindowActiveElement {

   public var rotateWindow: ScyWindow;

   var sceneCenterPoint:Point2D;
   var initialRotation:Number;
   var initialMouseRotation:Number;
   var rotating = false;

   protected override var scaledTranslateXCorrection2 = 6.5;
   protected override var scaledTranslateYCorrection2 = -5.25;

   public override function create(): Node {
      def separationStart = separatorLength+size-borderWidth/2;
      def separationEnd = size+borderWidth/2;
      def arrowSize2 = 4.0;
      def smallArcFactor = 0.7;
      var outerArc:Arc;
      Group{ // bottom left rotate element
         blocksMouse: true;
//         scaleX:highLightScale
//         scaleY:highLightScale
//         translateX:3
//         translateY:-2.5
         content: [
            Rectangle {
               x: -arrowSize2, y: -size
               width: size+arrowSize2, height: size
               fill: transparentColor
            }
            Line {
               startX: 0, startY: -separationStart
               endX: 0 , endY: -separationEnd
               strokeWidth: borderWidth
               stroke: windowColorScheme.emptyBackgroundColor
            }
            Line {
               startX: separationStart, startY: 0
               endX: separationEnd , endY: 0
               strokeWidth: borderWidth
               stroke: windowColorScheme.emptyBackgroundColor
            }
            Arc { // hide original corner arc
               centerX: size,
               centerY: - size,
               radiusX: size,
               radiusY: size
               startAngle: 180,
               length: 90
               type: ArcType.OPEN
               strokeWidth: borderWidth
               fill: windowColorScheme.backgroundColor
               //fill: Color.RED
               stroke: windowColorScheme.backgroundColor
               //stroke: Color.RED
            }
            Arc { // inner arc
               centerX: smallArcFactor*size,
               centerY: - smallArcFactor*size+1,
               radiusX: smallArcFactor*size,
               radiusY: smallArcFactor*size
               startAngle: 180,
               length: 120
               type: ArcType.OPEN
               strokeWidth: borderWidthSecondLine
               fill: null
               stroke: bind windowColorScheme.mainColor
            }
            Polygon {
               layoutX:0.0//+(1-smallArcFactor)*size
               layoutY:-separationEnd+arrowSize2+1
               points : [ -arrowSize2,0, 0,-arrowSize2, arrowSize2,0 ]
               fill: bind windowColorScheme.mainColor
            }

            outerArc= Arc {
               centerX: smallArcFactor*size-0.5*borderWidth,
               centerY: -smallArcFactor*size+secondLineSeparation,
               radiusX: smallArcFactor*size+borderWidth,
               radiusY: smallArcFactor*size+borderWidth/2
               startAngle: 180,
               length: 130
               type: ArcType.OPEN
               fill: null
               stroke: bind windowColorScheme.mainColor
               strokeWidth: borderWidth;
            }
            outerArc= Arc {
               centerX: outerArc.centerX,
               centerY: outerArc.centerY,
               radiusX: outerArc.radiusX,
               radiusY: outerArc.radiusY
               startAngle: outerArc.startAngle,
               length: outerArc.length
               type: ArcType.ROUND
               fill: transparentColor
               stroke: transparentColor
               strokeWidth: borderWidth;
            }

//            Arc {
//               centerX: 0,
//               centerY: 0,
//               radiusX: size,
//               radiusY: size
//               startAngle: 180,
//               length: 90
//               type: ArcType.OPEN
//               fill: Color.TRANSPARENT
//               stroke: bind
//               if (highLighted or rotating) color else subColor
//               strokeWidth: bind strokeWidth / 2;
//            }
         ]
         onMousePressed: function( e: MouseEvent ):Void {
            activate();
            startRotate(e);
         }
         onMouseDragged: function( e: MouseEvent ):Void {
            doRotate(e);
         }
         onMouseReleased: function( e: MouseEvent ):Void {
            stopRotate(e);
         }
         onMouseEntered: function( e: MouseEvent ):Void {
            highLighted = true;
         }
         onMouseExited: function( e: MouseEvent ):Void {
            highLighted = false;
         }
      }
   }

   function startRotate(e: MouseEvent){
      rotating = true;
      sceneCenterPoint = rotateWindow.localToScene(rotateWindow.width/2, rotateWindow.height/2);
      initialMouseRotation = calculateRotation(e);
      initialRotation = rotateWindow.rotate;
      MouseBlocker.startMouseBlocking();
//      println("startRotate() initialRotation:{initialRotation}, initialMouseRotation:{initialMouseRotation}");
    }

   function calculateRotation(e: MouseEvent){
      var scenePoint = localToScene(e.x,e.y);
		var deltaX = scenePoint.x - sceneCenterPoint.x;
		var deltaY = scenePoint.y - sceneCenterPoint.y;
      var rotation = Math.atan2(deltaY , deltaX);
//      println("calculateRotation({e.x},{e.y}): dx: {deltaX}, dx: {deltaY}, rotation: {rotation}");
      return rotation;
   }

   function doRotate(e:MouseEvent){
		var newRotation = calculateRotation(e);
		var deltaRotation = Math.toDegrees(newRotation - initialMouseRotation);
//      println("deltaRotation: {deltaRotation}");
		var newRotate = initialRotation + deltaRotation;
		if (e.controlDown){
			newRotate = 45 * Math.round(newRotate  /  45);
		}
		rotateWindow.rotate = newRotate;
   }

   function stopRotate(e:MouseEvent){
      rotating = false;
      MouseBlocker.stopMouseBlocking();
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
   var rotateElement = WindowRotate{
      size:controlLength;
      borderWidth:borderWidth;
      windowColorScheme:windowColorScheme
//         activate: activate;
//      rotateWindow:this;
      layoutX: 0;
      layoutY: bind height;
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
            layoutX:110
            layoutY:40
            scaleX:scale
            scaleY:scale
            content:[
               emptyWindow,
               rotateElement
            ]
         }

      ]
	}
}

}
