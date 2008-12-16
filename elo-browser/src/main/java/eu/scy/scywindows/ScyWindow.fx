/*
 * ScyWindow.fx
 *
 * Created on 15-dec-2008, 18:49:31
 */

package eu.scy.scywindows;

import colab.vt.whiteboard.component.WhiteboardPanel;
import eu.scy.scywindows.ScyWindow;
import java.lang.Math;
import java.lang.System;
import javafx.ext.swing.SwingComponent;
import javafx.scene.Cursor;
import javafx.scene.CustomNode;
import javafx.scene.effect.DropShadow;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.Scene;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javax.swing.JTree;

/**
 * @author sikken
 */

 // place your code here
public class ScyWindow extends CustomNode {
	public var title="???";
	public var color = Color.GREEN;
	public var backgroundColor = color.WHITE;
	public var width:Number = 100;
	public var height:Number = 100;
	public var scyContent:Node= Rectangle {
      x: 0,
      y: 0
      width: 10,
      height: 10
      fill: Color.TRANSPARENT
		};
   public var allowRotate = false;
   public var allowResize = true;

	var borderWidth = 3;
	var topLeftBlockSize = 23;
	var borderBlockOffset = 5;
	var dragStrokeWith = 4;
	var dragLength = 20;
	var dragBorder = 4;
	var titleFontsize = 12;
	var fontHeightCompensation = 0;
	var lineOffsetY=3;
	var lineWidth = 1;
	var contentBorder = 0;
	var textFont = Font.font("Verdana", FontWeight.BOLD, titleFontsize);
	var originalX:Number;
	var originalY:Number;
	var originalWidth:Number;
	var originalHeight:Number;
	var rotateCenterX:Number;
	var rotateCenterY:Number;
	var initialRotation:Number;
   var maxDifX:Number;
   var maxDifY:Number;

   public var minimumHeigth:Number = 100 on replace{
      minimumHeigth = Math.max(minimumHeigth, topLeftBlockSize + dragLength + 2 * dragBorder + borderWidth);
   }
   public var minimumWidth:Number = 100 on replace{
      minimumWidth = Math.max(minimumWidth, 2 * borderWidth + 3 * dragBorder + 2 * dragStrokeWith + 2 * dragLength + borderBlockOffset);
   }

	function startDragging(e: MouseEvent) {
		toFront();
		originalX = e.x;
		originalY = e.y;
		originalWidth = width;
		originalHeight = height;
		rotateCenterX = width / 2;
		rotateCenterY = height / 2;
		initialRotation = calculateRotation(e);
      maxDifX = 0;
      maxDifY = 0;
	}

   function printMousePos(label:String, e:MouseEvent) {
//      System.out.println("{label} - x:{e.x}, sceneX:{e.sceneX}, screenX:{e.screenX}, y:{e.y}, sceneY:{e.sceneY}, screenY:{e.screenY}");

   }

   function isInvalidMousePos(e: MouseEvent){
		def maxScreenCoordinate = 1e4;
		var invalid = Math.abs(e.screenX) > maxScreenCoordinate or Math.abs(e.screenY) > maxScreenCoordinate;
		if (invalid)
		{
			System.out.println("unreasonable mouse position, will ignore it");
      }
      return invalid;
   }

	function doDrag(e: MouseEvent) {
      printMousePos("drag",e);
      if (Math.abs(e.x) > 1e6 or Math.abs(e.y) > 1e6)
      {
         System.out.println("unreasonable mouse position, will ignore it");
         return;
      }
		var difX = e.x - originalX;
		var difY = e.y - originalY;
      maxDifX = Math.max(maxDifX, difX);
      maxDifY = Math.max(maxDifY, difY);
		//System.out.println("difX: {e.x}-{e.dragAnchorX} {difX}, difY: {e.y}-{e.dragAnchorY} {difY}");
		translateX += difX;
		translateY += difY;
//		System.out.println("dragged {title}, difX: {difX}, difY: {difY}, maxDifX:{maxDifX}, maxDifY: {maxDifY}");

   }

	function doResize(e: MouseEvent) {
      printMousePos("resize",e);
      if (isInvalidMousePos(e))
		return;
		var difX = e.x - originalX;
		var difY = e.y - originalY;
		width = Math.max(minimumWidth,originalWidth + difX);
		height =Math.max(minimumHeigth,originalHeight + difY);
		//System.out.println("resized {title}, difX: {difX}, difY: {difY}");

   }

	function doRotate(e: MouseEvent) {
      printMousePos("rotate",e);
      if (isInvalidMousePos(e))
		return;
		var newRotation = calculateRotation(e);
		var deltaRotation = Math.toDegrees(newRotation - initialRotation);
		rotate += deltaRotation;
		//System.out.println("rotated {title}, deltaRotation: {deltaRotation}");

   }

	function calculateRotation(e: MouseEvent):Number{
		var deltaX = e.x - rotateCenterX;
		var deltaY = e.y - rotateCenterY;
		var rotation = -Math.atan(deltaX / deltaY);
		if (deltaY >= 0)
		rotation += Math.PI;
		// System.out.println("deltaX " + deltaX + ", deltaY " + deltaY + ", rotation " + rotation);
		return rotation;
	}

	public override function create(): Node {
		blocksMouse = true;
		return Group {
			effect: DropShadow {
				offsetX: 4,
				offsetY: 4
			}
         cursor: Cursor.MOVE;

			content: [
				Rectangle { // main border
					x: 0,
					y: borderBlockOffset;
					width: bind width - borderBlockOffset,
					height: bind height - borderBlockOffset
					fill:bind backgroundColor;
					stroke:bind color
					strokeWidth:borderWidth;
				}
				Rectangle { // top left block
					x: bind width - topLeftBlockSize,
					y: 0;
					width: topLeftBlockSize,
					height: topLeftBlockSize
					fill: bind color
				}
				Text { // title
					font: textFont
					x: borderWidth,
					y: borderBlockOffset + borderWidth / 2 + titleFontsize - fontHeightCompensation
					clip:Rectangle {
						x: 0,
						y: 0
						width: bind width - topLeftBlockSize - borderWidth / 2 + 1,
						height: bind height
						fill: Color.BLACK
					}
					fill:bind color;
					content: bind title;
				}
				Line { // line under title
					startX: borderWidth / 2,
					startY:borderBlockOffset + borderWidth / 2 + titleFontsize - fontHeightCompensation + lineOffsetY
					endX: bind width - topLeftBlockSize - borderWidth / 2 + 1,
					endY: borderBlockOffset + borderWidth / 2 + titleFontsize - fontHeightCompensation + lineOffsetY
					strokeWidth: lineWidth
					stroke: bind color
				}
				Group{ // the content
               blocksMouse:true;
               cursor:Cursor.DEFAULT;
               translateX:borderWidth / 2 + contentBorder + 1
               translateY:borderBlockOffset + borderWidth / 2 + titleFontsize - fontHeightCompensation + lineOffsetY + contentBorder + lineWidth / 2 + 1
               clip:Rectangle {
                  x: 0,
                  y: 0
                  width: bind width - borderBlockOffset - borderWidth - 0,
                  height: bind height - borderBlockOffset - borderWidth - titleFontsize - fontHeightCompensation - lineOffsetY + lineWidth / 2 + 0
                  fill: Color.BLACK
               }
               content:bind scyContent
               onMousePressed: function( e: MouseEvent ):Void {
                  toFront();
               }
            }
				Group{ // bottom right resize element
               blocksMouse:true;
               visible:bind allowResize
               cursor:Cursor.NW_RESIZE;
               effect: DropShadow {
                  offsetX: 2,
                  offsetY: 2
               }
               content:[
						Line { // vertical line
                     startX: bind width - borderBlockOffset - borderWidth - dragBorder,
                     startY: bind height - dragStrokeWith - dragBorder - dragLength
                     endX: bind width - borderBlockOffset - borderWidth - dragBorder,
                     endY: bind height - dragStrokeWith - dragBorder
                     stroke:bind color
                     strokeWidth:bind dragStrokeWith;
                  }
						Line { // horizontal line
                     startX: bind width - borderBlockOffset - borderWidth - dragBorder - dragLength,
                     startY: bind height - dragStrokeWith - dragBorder
                     endX: bind width - borderBlockOffset - borderWidth - dragBorder,
                     endY: bind height - dragStrokeWith - dragBorder
                     stroke:bind color
                     strokeWidth:bind dragStrokeWith;
                  }
               ]
               onMousePressed: function( e: MouseEvent ):Void {
                  startDragging(e);
               }
               onMouseDragged: function( e: MouseEvent ):Void {
                  doResize(e);
               }
            }
				Arc { // bottom left rotate element
					blocksMouse:true;
               visible:bind allowRotate;
					effect: DropShadow {
						offsetX: 2,
						offsetY: 2
					}
					centerX: borderWidth + dragBorder + dragLength,
					centerY: bind height - borderWidth - dragBorder - dragLength,
					radiusX: dragLength,
					radiusY: dragLength
					startAngle: 180,
					length: 90
					type: ArcType.OPEN
					fill: Color.TRANSPARENT
					stroke:bind color
					strokeWidth:bind dragStrokeWith;
					onMousePressed: function( e: MouseEvent ):Void {
						startDragging(e);
					}
					onMouseDragged: function( e: MouseEvent ):Void {
						doRotate(e);
					}
				}
			]
			onMousePressed: function( e: MouseEvent ):Void {
				startDragging(e);
			}
			onMouseDragged: function( e: MouseEvent ):Void {
				doDrag(e);
			}
		};
	}
}

function run() {
	var image = Image{
		url: "{__DIR__}Water lilies.jpg"}
	var imageView = ImageView{
		image:image
  }
	var imageView2 = ImageView{
		image:image
  }
	var testContent = Rectangle {
		x: 0,
		y: 0
		width: 200,
		height: 200
		fill: Color.RED
}
	var whiteboard = new WhiteboardPanel();
	var whiteboardNode = SwingComponent.wrap(whiteboard);
	//whiteboardNode.cursor = null;

   var tree = new JTree();
	var treeNode = SwingComponent.wrap(tree);

	Stage {
		title: "Scy window test"
		width: 250
		height: 250
		scene: Scene {
			content: [
				Group{
					content:[
						ScyWindow{
							color:Color.BLUE
							title:"Blue"
							translateX:10,
							translateY:10
							//scyContent: imageView
						},
						ScyWindow{
							color:Color.GREEN
							title:"Green or greener"
							translateX:20,
							translateY:20
                     width:200
                     height:200
							scyContent: treeNode
						}
					]
				}
			]

		}
	}
}