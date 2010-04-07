/*
 * WindowRotate.fx
 *
 * Created on 3-sep-2009, 17:37:22
 */

package eu.scy.client.desktop.scydesktop.scywindows.window;

import javafx.scene.Group;
import javafx.scene.Node;

import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;

import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;

import javafx.geometry.Point2D;

import javafx.util.Math;

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

   public override function create(): Node {
      Group{ // bottom left rotate element
         blocksMouse: true;
         content: [
            Arc {
               centerX: 0,
               centerY: 0,
               radiusX: size,
               radiusY: size
               startAngle: 180,
               length: 90
               type: ArcType.OPEN
               fill: Color.TRANSPARENT
               stroke: bind
               if (highLighted or rotating) subColor else color
               strokeWidth: bind strokeWidth;
            }
            Arc {
               centerX: 0,
               centerY: 0,
               radiusX: size,
               radiusY: size
               startAngle: 180,
               length: 90
               type: ArcType.OPEN
               fill: Color.TRANSPARENT
               stroke: bind
               if (highLighted or rotating) color else subColor
               strokeWidth: bind strokeWidth / 2;
            }
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

