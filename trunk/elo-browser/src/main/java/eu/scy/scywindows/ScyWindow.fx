/*
 * ScyWindow2.fx
 *
 * Created on 12-dec-2008, 19:01:31
 */

package eu.scy.scywindows;

import colab.vt.whiteboard.component.WhiteboardPanel;
import eu.scy.scywindows.ScyDesktop;
import eu.scy.scywindows.ScyWindow;
import java.awt.Dimension;
import java.lang.Math;
import java.lang.System;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.ext.swing.SwingButton;
import javafx.ext.swing.SwingComponent;
import javafx.ext.swing.SwingScrollPane;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.CustomNode;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Glow;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Resizable;
import javafx.scene.layout.VBox;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.Scene;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javafx.scene.shape.Polygon;

/**
 * @author sikken
 */

 // place your code here
public class ScyWindow extends CustomNode {
	public var title="???";
	public var eloType="?123";
	public var color = Color.GREEN;
	public var backgroundColor = color.WHITE;
	public var width:Number = 100 on replace{
		if (not isAnimating){
			width = Math.max(width,minimumWidth);
			resizeTheContent()
		}
  };
	public var height:Number = 100 on replace{
		if (not isAnimating){
			if (isClosed or isMinimized){
				height = closedHeight;
			}
			else {
				height = Math.max(height, minimumHeight);
			}
			resizeTheContent()
		}
  };
	public var scyContent:Node= null;
   public var allowRotate = true;
   public var allowResize = true;
   public var allowClose = true;
   public var allowMinimize = true;
   public var closeIsHide = false;
   public var scyDesktop:ScyDesktop;
	public var windowEffect:Effect;
	//	public var closeAction:function(ScyWindow):Void;
	public var minimizeAction:function(ScyWindow):Void;
	public var setScyContent:function(ScyWindow):Void;
	public var aboutToCloseAction:function(ScyWindow):Boolean;
	public var closedAction:function(ScyWindow):Void;

	// status variables
	var isMinimized = false;
	var isClosed = true;
	var isAnimating = false;

	// layout constants
	def windowBackgroundColor = Color.WHITE;
//	def controlColor = Color.BLACK;
//	def controlColor = Color.color(lighterColorElement(color.red),lighterColorElement(color.green),lighterColorElement(color.blue));
	def controlColor = Color.WHITE;
	def controlLength = 21;
	def controlStrokeWidth = 4;
	def closeCrossInset = 4;
   def controlStrokeDashArray = [0.0,7.0];
   def controlStrokeDashOffset = 0;

   def iconSize = 16;

	def borderWidth = 4;
	def topLeftBlockSize = 17;
	def closedHeight = iconSize + topLeftBlockSize / 2 + borderWidth / 2;
   def closeColor = Color.WHITE;
   def closeMouseOverEffect:Effect = Glow{
      level:1}
   def closeStrokeWidth = 2;
//	var borderBlockOffset = 5;
//	var dragStrokeWith = 4;
//	var dragLength = 20;
//	var dragBorder = 4;
	var titleFontsize = 12;
	var fontHeightCompensation = 3;
	var lineOffsetY=3;
	var lineWidth = 1;
	var contentBorder = 2;
	var textFont = Font.font("Verdana", FontWeight.BOLD, titleFontsize);
	var eloTypeFontsize = 14;
	var eloTypeFont = Font.font("Verdana", FontWeight.BOLD, eloTypeFontsize);
	//def minimumHeigth = topLeftBlockSize + dragLength + 2 * dragBorder + borderWidth;
	//def minimumWidth = 2 * borderWidth + 3 * dragBorder + 2 * dragStrokeWith + 2 * dragLength + borderBlockOffset;
	public-read var originalTranslateX:Number;
	public-read var originalTranslateY:Number;
	public-read var originalWidth:Number;
	public-read var originalHeight:Number;
	var originalX:Number;
	var originalY:Number;
	var originalW:Number;
	var originalH:Number;
	var rotateCenterX:Number;
	var rotateCenterY:Number;
	var initialRotation:Number;
   var maxDifX:Number;
   var maxDifY:Number;
	var sceneTopLeft:Point2D;

   var closeElement:Node;
   var minimizeElement:Node;
   var unminimizeElement:Node;
	def resizeAnimationTime = 250ms;
	def opcityAnimationTime = 250ms;
	def closeAnimationTime = 350ms;

   public var minimumHeight:Number = 100 on replace{
      minimumHeight = Math.max(minimumHeight, topLeftBlockSize + controlLength + borderWidth);
   }
   public var minimumWidth:Number = 100 on replace{
      minimumWidth = Math.max(minimumWidth, 2 * borderWidth + 2 * controlStrokeWidth + 2 * controlLength);
   }

	postinit {
		if (isClosed){
			height = closedHeight;
		}
		resizeTheContent();
	}

   function lighterColorElement(elementValue:Number):Number{
   var whiteDif = 1-elementValue;
   return 1-whiteDif/2;
}

	public function openWindow(openWidth:Number,openHeight:Number){
		checkScyContent();
		isClosed = false;
		width = openWidth;
		height = openHeight;
	}

	function checkScyContent(){
		//println("checkScyContent: scyContent: {scyContent==null}, setScyContent: {setScyContent!=null}");
		if (scyContent == null and setScyContent != null){
			setScyContent(this)
		}
	}

	function getHideTimeline(endX:Number,endY:Number):Timeline{
		return Timeline{
			keyFrames: [
				KeyFrame{
					time:resizeAnimationTime;
					values:[
						translateX => endX tween Interpolator.EASEBOTH
						translateY => endY tween Interpolator.EASEBOTH
						width => minimumWidth tween Interpolator.EASEBOTH
						height => minimumHeight tween Interpolator.EASEBOTH
					]
				}
				KeyFrame{
					time:resizeAnimationTime + opcityAnimationTime;
					values:[
						opacity => 0 tween Interpolator.EASEBOTH
					]
					action:function(){
						if (scyDesktop != null){
							scyDesktop.hideScyWindow(this);
						}
						isAnimating = false;
					}
				}
			];
		}
	}

	function getShowTimeline(beginX:Number,beginY:Number,endX:Number,endY:Number,endWidth:Number,endHeight:Number):Timeline{
		return Timeline{
			keyFrames: [
				KeyFrame{
					time:0ms;
					values:[
						translateX => beginX
						translateY => beginY
						width => minimumWidth
						height => minimumHeight
						opacity => 0.0
					]
				}
				KeyFrame{
					time:opcityAnimationTime;
					values:[
						translateX => beginX
						translateY => beginY
						width => minimumWidth
						height => minimumHeight
						opacity => 1.0 tween Interpolator.EASEBOTH
					]
				}
				KeyFrame{
					time:resizeAnimationTime + opcityAnimationTime;
					values:[
						translateX => endX tween Interpolator.EASEBOTH
						translateY => endY tween Interpolator.EASEBOTH
						width => endWidth tween Interpolator.EASEBOTH
						height => endHeight tween Interpolator.EASEBOTH
					]
					action: function(){
						isAnimating = false;
					}
				}
			];
		}
	}

	function getCloseTimeline(endX:Number,endY:Number):Timeline{
		return Timeline{
			keyFrames:[
				KeyFrame{
					time:closeAnimationTime;
					values:[
						translateX => endX
						translateY => endY
						scaleX =>
						1.0 / width
						scaleY =>
						1.0 / height
					]
					action: function(){
						closedAction(this);
//						if (scyDesktop != null){
//							scyDesktop.removeScyWindow(this);
//						}
						isAnimating = false;
					}
				}
			]
		 }
	}
 
	function getMinimizeTimeline(endX:Number,endY:Number):Timeline{
		return Timeline{
			keyFrames:[
				KeyFrame{
					time:closeAnimationTime;
					values:[
						translateX => endX  tween Interpolator.EASEBOTH
						translateY => endY tween Interpolator.EASEBOTH
						width => minimumWidth tween Interpolator.EASEBOTH
						height => closedHeight tween Interpolator.EASEBOTH
					]
					action: function(){
						isAnimating = false;
					}
				}
			]
		 }
	}

	function getUnminimizeTimeline(endX:Number,endY:Number,endWidth:Number,endHeight:Number):Timeline{
		return Timeline{
			keyFrames:[
				KeyFrame{
					time:closeAnimationTime;
					values:[
						translateX => endX
						translateY => endY
						width => endWidth
						height => endHeight
					]
					action: function(){
						isAnimating = false;
					}
				}
			]
		 }
	}

	public function hideTo(x:Number,y:Number){
		originalTranslateX = translateX;
		originalTranslateY = translateY;
		originalWidth = width;
		originalHeight = height;
		var hideTimeline = getHideTimeline(x,y);
		hideTimeline.play();
	}

	public function showFrom(x:Number,y:Number){
		var showTimeline = getShowTimeline(x,y,originalTranslateX,originalTranslateY,originalWidth,originalHeight);
		scyDesktop.showScyWindow(this);
		showTimeline.play();
	}

	public function openFrom(x:Number,y:Number){
		// cannot add myself to scyDesktop, because the scyDesktop variable is set in the addScyWindow call
		var openTimeline = getShowTimeline(x,y,translateX,translateY,width,height);
		openTimeline.play();
	}
	
	public function closeIt(){
		var closeTimeline = getCloseTimeline(translateX + width / 2,translateY - height / 2);
		closeTimeline.play();
	}

	function resizeTheContent(){
		if (scyContent instanceof Resizable){
			var contentWidth = width - borderWidth - 2 * contentBorder - 1;
			var contentHeight = height - borderWidth - iconSize - topLeftBlockSize / 2 + 1 - 2 * contentBorder;
			var resizeableScyContent = scyContent as Resizable;
			resizeableScyContent.width = contentWidth;
			resizeableScyContent.height = contentHeight;
		}
	}


	function startDragging(e: MouseEvent) {
		activate();
		originalX = translateX;
		originalY = translateY;
		originalW = width;
		originalH = height;
		rotateCenterX = width / 2;
		rotateCenterY = height / 2;
		initialRotation = calculateRotation(e);
      maxDifX = 0;
      maxDifY = 0;
		sceneTopLeft = localToScene(0,0);
	}

   function printMousePos(label:String, e:MouseEvent) {
//      System.out.println("{label} - x:{e.x}, sceneX:{e.sceneX}, screenX:{e.screenX}, y:{e.y}, sceneY:{e.sceneY}, screenY:{e.screenY}");
   
   }

   function isInvalidMousePos(e: MouseEvent){
		def maxScreenCoordinate = 1e4;
		var invalid = Math.abs(e.screenX) > maxScreenCoordinate or Math.abs(e.screenY) > maxScreenCoordinate;
		if (invalid){
         println("unreasonable mouse position, will ignore it");
      }
      return invalid;
   }

	function doDrag(e: MouseEvent) {
      printMousePos("drag",e);
      if (isInvalidMousePos(e))
		return;
		var difX = e.dragX;
		var difY = e.dragY;
      maxDifX = Math.max(maxDifX, difX);
      maxDifY = Math.max(maxDifY, difY);
		//System.out.println("difX: {e.x}-{e.dragAnchorX} {difX}, difY: {e.y}-{e.dragAnchorY} {difY}");
		translateX = originalX + difX;
		translateY = originalY + difY;
//		System.out.println("dragged {title}, difX: {difX}, difY: {difY}, maxDifX:{maxDifX}, maxDifY: {maxDifY}");

   }

	function doResize(e: MouseEvent) {
      printMousePos("resize",e);
      if (isInvalidMousePos(e))
		return;
		if (isClosed){
			isClosed = false;
		}
		if (isMinimized){
			isMinimized = false;
		}
		checkScyContent();
		var angle = Math.toRadians(rotate);
		var difW = Math.cos(angle) * e.dragX + Math.sin(angle) * e.dragY;
		var difH = Math.cos(angle) * e.dragY - Math.sin(angle) * e.dragX;
		var difX:Number;
		var difY:Number;
		difX = (1 - Math.cos(angle)) * difW / 2;
		difY = (1 - Math.cos(angle)) * difH / 2;
		//difX = (1 - Math.cos(angle)) * e.dragX/2;// + (1 - Math.sin(angle)) * e.dragY/2;
		//difY = (1 - Math.cos(angle)) * e.dragY/2;// + (1 - Math.sin(angle)) * e.dragX/2;
		width = Math.max(minimumWidth,originalW + difW);
		height =Math.max(minimumHeight,originalH + difH);
		translateX = originalX - difX;
		translateY = originalY - difY;
//		var newSceneTopLeft = localToScene(0,0);
//		System.out.println("resized {title}, angle: {rotate}, difW: {difW}, difH: {difH}, difX: {difX}, difY: {difY}, dtlX:{sceneTopLeft.x-newSceneTopLeft.x}, dtlY:{sceneTopLeft.y-newSceneTopLeft.y}");

	}

	function doRotate(e: MouseEvent) {
      printMousePos("rotate",e);
      if (isInvalidMousePos(e))
		return;
		var newRotation = calculateRotation(e);
		var deltaRotation = Math.toDegrees(newRotation - initialRotation);
		var newRotate = rotate + deltaRotation;
		if (e.controlDown){
			newRotate = 45 * Math.round(newRotate  /  45);
		}
		rotate = newRotate;
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

	function doClose(){
		//		if (scyDesktop != null)
		//		{
		if (aboutToCloseAction != null){
			if (not aboutToCloseAction(this)){
				// close blocked
				return;
			}
		}
			isClosed = true;
			var closeTimeline = null;
			if (closeIsHide){
				isClosed = false;
				scyDesktop.hideScyWindow(this);
			}
			else {
				closeTimeline = getMinimizeTimeline(translateX,translateY);
			}

			if (closeTimeline != null){
				isAnimating = true;
				closeTimeline.play();
			}

			//scyDesktop.removeScyWindow(this);
			System.out.println("closed {title}");
	}

	function doMinimize(){
		//		if (scyDesktop != null)
		//		{
		if (minimizeAction != null){
			minimizeAction(this)
		}
		else {
			originalTranslateX = translateX;
			originalTranslateY = translateY;
			originalWidth = width;
			originalHeight = height;
			isMinimized = true;
			var minimizeTimeline = getMinimizeTimeline(translateX,translateY);
			isAnimating = true;
			minimizeTimeline.play();
		}
		System.out.println("minimized {title}");
//		}

	}

	function doUnminimize(){
		//		if (scyDesktop != null)
		//		{
		if (minimizeAction != null){
			minimizeAction(this)
		}
		else {
			isMinimized = false;
			//				translateX = originalTranslateX;
			//				translateY = originalTranslateY;
			//				width = originalWidth;
			//				height = originalHeight;
			var unminimizedTimeline = getUnminimizeTimeline(originalTranslateX,originalTranslateY,originalWidth,originalHeight);
			isAnimating = true;
			unminimizedTimeline.play();
		}
		System.out.println("unminimized {title}");
//		}

	}

	function getScyContent(scyCont:Node): Node {
		if (scyCont != null)
		return scyCont;
		return Rectangle {
			x: 0,
			y: 0
			width: 10,
			height: 10
			fill: Color.TRANSPARENT
		};
	}

   function activate(){
      if (scyDesktop != null){
			scyDesktop.activateScyWindow(this);
		}
   }


	public override function create(): Node {
		blocksMouse = true;
		return Group {
         cursor: Cursor.MOVE;

			content: [
					Group{ // the white background of the window
					content:[
						Rectangle { // top part until the arc
							x: 0,
							y: 0
							width: bind width,
							height: bind height - controlLength
							strokeWidth: borderWidth
							fill: windowBackgroundColor
							stroke: windowBackgroundColor
						},
						Rectangle { // bottom left part until the arc
							x: bind controlLength,
							y: bind height - controlLength
							width: bind width - controlLength,
							height: bind controlLength
							strokeWidth: borderWidth
							fill: windowBackgroundColor
							stroke: windowBackgroundColor
						},
						Arc { // the bottom left rotate arc part
							centerX: controlLength,
							centerY: bind height - controlLength,
							radiusX: controlLength,
							radiusY: controlLength
							startAngle: 180,
							length: 90
							type: ArcType.ROUND
							strokeWidth: borderWidth
							fill: windowBackgroundColor
							stroke: windowBackgroundColor
						}
					]
				}
					Line { // the left border line
					startX: 0,
					startY: bind height - controlLength - borderWidth / 2 - closeStrokeWidth / 2
					endX: 0,
					endY: 0
					strokeWidth: borderWidth
					stroke: bind color
				}
					Line { // the top border line
					startX: 0,
					startY: 0
					endX: bind width,
					endY: 0
					strokeWidth: borderWidth
					stroke: bind color
				}
					Line { // the right border line
					startX: bind width,
					startY: 0
					endX: bind width,
					endY: bind height,
					strokeWidth: borderWidth
					stroke: bind color
					effect:bind windowEffect
				}
					Line { // the bottom border line
					startX: bind width,
					startY: bind height
					endX: bind controlLength + borderWidth / 2 + closeStrokeWidth / 2,
					endY: bind height,
					strokeWidth: borderWidth
					stroke: bind color
					effect:bind windowEffect
				}
					Arc { // the bottom left "disabled" rotate arc
					centerX: controlLength,
					centerY: bind height - controlLength,
					radiusX: controlLength,
					radiusY: controlLength
					startAngle: 180,
					length: 90
					type: ArcType.OPEN
					fill: Color.TRANSPARENT
					strokeWidth: borderWidth
					stroke: bind color
					//effect:bind windowEffect
				}
					Group{ // the content
               blocksMouse:true;
               cursor:Cursor.DEFAULT;
               translateX:borderWidth / 2 + 1 + contentBorder
               translateY:iconSize + topLeftBlockSize / 2 + 1 + contentBorder
               clip:Rectangle {
                  x: 0,
                  y: 0
                  width: bind width - borderWidth - 2 * contentBorder - 1,
                  height: bind height - borderWidth - iconSize - topLeftBlockSize / 2 + 1 - 2 * contentBorder
                  fill: Color.BLACK
               }
               content:bind scyContent
               onMousePressed: function( e: MouseEvent ):Void {
                  activate();
               }
            }
				//            Group{ // group for testing the content clip
				//               blocksMouse:true;
				//               cursor:Cursor.DEFAULT;
				//               translateX:borderWidth/2+1+contentBorder
				//               translateY:iconSize + topLeftBlockSize/2+1+contentBorder
				//               content:Rectangle {
				//                  x: 0,
				//                  y: 0
				//                  width: bind width - borderWidth - 2*contentBorder-1,
				//                  height: bind height - borderWidth - iconSize - topLeftBlockSize/2+1-2*contentBorder
				//                  fill: Color.RED
				//               }
				//            }
					Group{ // icon for title
					translateX:3 * topLeftBlockSize / 4
					translateY:topLeftBlockSize / 4+1
					content:[
						Rectangle{
							x:0
							y:0
							width:iconSize
							height:iconSize
							fill:bind color
						}
						Text {
							font:eloTypeFont
							x: eloTypeFont.size / 4,
							y: eloTypeFont.size - 1
							content:bind eloType.substring(0, 1)
							fill:Color.WHITE
						}
					]
				},
					Text { // title
					font: textFont
					x: 3 * topLeftBlockSize / 4 + iconSize + 3,
					y: borderWidth + titleFontsize + fontHeightCompensation
					clip:Rectangle {
						x: 3 * topLeftBlockSize / 4 + iconSize,
						y: 0
						width: bind width - 3 * topLeftBlockSize / 4 - iconSize-4,
						height: bind height
						fill: Color.BLACK
					}
					fill:bind color;
					content: bind title;
				},
				//				Group{ // just for checking title clip
				//					content:[
				//						Rectangle {
				//							x: 3 * topLeftBlockSize / 4 + iconSize,
				//							y: 0
				//							width: bind width - 3 * topLeftBlockSize / 4 - iconSize,
				//							height: bind height
				//							fill: Color.BLACK
				//						}
				//					]
				//				},
				Line { // line under title
					startX:3 * topLeftBlockSize / 4,
					startY:iconSize + topLeftBlockSize / 2
					endX: bind width - 2 * borderWidth,
					endY: iconSize + topLeftBlockSize / 2
					strokeWidth: lineWidth
					stroke: bind color
				},
				minimizeElement = Group{
					cursor:Cursor.HAND
					visible:bind allowMinimize and not isMinimized and not isClosed
					translateX:bind width / 2 -topLeftBlockSize/2
               translateY: bind height+topLeftBlockSize/4
					//effect:bind windowEffect
					content:[
//                  Arc {
//                     centerX: 0,
//                     centerY: 0
//                     radiusX: topLeftBlockSize / 2,
//                     radiusY: topLeftBlockSize / 2
//                     startAngle: 180,
//                     length: 180
//                     type: ArcType.OPEN
//                     fill: Color.TRANSPARENT
//							strokeWidth: controlStrokeWidth
//							stroke: bind color
//                  }
                  Polygon {
                     points: [ 0,0, topLeftBlockSize/2,-topLeftBlockSize/2, topLeftBlockSize,0 ]
                     fill: Color.WHITE
							strokeWidth: controlStrokeWidth/2
							stroke: bind color
                  }
					]
               onMouseClicked: function( e: MouseEvent ):Void {
						doMinimize();
					}
               onMouseEntered: function( e: MouseEvent ):Void {
                  minimizeElement.effect = closeMouseOverEffect;
               }
               onMouseExited: function( e: MouseEvent ):Void {
                  minimizeElement.effect = null;
               }

				}
				unminimizeElement = Group{
					cursor:Cursor.HAND
					visible:bind allowMinimize and isMinimized and not isClosed
					translateX:bind width / 2 -topLeftBlockSize/2
               translateY: bind height-topLeftBlockSize/4
					//effect:bind windowEffect
					content:[
//                  Arc {
//                     centerX: 0,
//                     centerY: 0
//                     radiusX: topLeftBlockSize/2,
//                     radiusY: topLeftBlockSize/2
//                     startAngle: 180,
//                     length: 180
//                     type: ArcType.OPEN
//                     fill: Color.TRANSPARENT
//							strokeWidth: controlStrokeWidth
//							stroke: bind color
//                  }
                  Polygon {
                     points: [ 0,0, topLeftBlockSize/2,topLeftBlockSize/2, topLeftBlockSize,0 ]
                     fill: Color.WHITE
							strokeWidth: controlStrokeWidth/2
							stroke: bind color
                  }
					]
               onMouseClicked: function( e: MouseEvent ):Void {
						doUnminimize();
					}
               onMouseEntered: function( e: MouseEvent ):Void {
                  unminimizeElement.effect = closeMouseOverEffect;
               }
               onMouseExited: function( e: MouseEvent ):Void {
                  unminimizeElement.effect = null;
               }

				}
					Group{ // the complete close element
               translateX:bind width - topLeftBlockSize / 2
               translateY:-topLeftBlockSize / 2
               content:[
						//						Rectangle { // top right block
						//                     x: 0,
						//                     y: 0;
						//                     width: topLeftBlockSize,
						//                     height: topLeftBlockSize
						//                     fill: bind color
						//					//effect:bind windowEffect;
						//                  }
							closeElement = Group{ // close button
                     cursor:Cursor.HAND
                     visible:bind allowClose and not isClosed
                     content:[
									Rectangle { // top left block
                           x: 0,
                           y: 0;
                           width: topLeftBlockSize,
                           height: topLeftBlockSize
                           fill: bind color
                        }
									Group{ // close cross
                     translateX:0
                     translateY:-1
//										clip: Rectangle { // top left block
//                              x: closeCrossInset,
//                              y: closeCrossInset;
//                              width: topLeftBlockSize - 2 * closeCrossInset,
//                              height: topLeftBlockSize - 2 * closeCrossInset
//                           }
                           content:[
                              Line {
                                 startX: closeCrossInset,
                                 startY: closeCrossInset
                                 endX: topLeftBlockSize-closeCrossInset,
                                 endY: topLeftBlockSize-closeCrossInset
                                 strokeWidth: closeStrokeWidth
                                 stroke: closeColor
                              }
                              Line {
                                 startX: closeCrossInset,
                                 startY: topLeftBlockSize-closeCrossInset
                                 endX: topLeftBlockSize-closeCrossInset,
                                 endY: closeCrossInset
                                 strokeWidth: closeStrokeWidth
                                 stroke: closeColor
                              }
                           ]
                        }
                     ]
                     onMouseClicked: function( e: MouseEvent ):Void {
								doClose();
                     }
                     onMouseEntered: function( e: MouseEvent ):Void {
                        closeElement.effect = closeMouseOverEffect;
                     }
                     onMouseExited: function( e: MouseEvent ):Void {
                        closeElement.effect = null;
                     }
                  }
               ]
				}
				Group{ // bottom right resize element
               blocksMouse:true;
               visible:bind allowResize or isClosed
               cursor:Cursor.NW_RESIZE;
               translateX:bind width;
               translateY:bind height;
               content:[
                  Polyline {
                     points: [ 0,-controlLength, 0,0, -controlLength,0 ]
                     stroke:bind Color.WHITE
                     strokeWidth:bind controlStrokeWidth;
                  }
                  Polyline {
                     points: [ 0,-controlLength, 0,0, -controlLength,0 ]
                     stroke:bind color
                     strokeWidth:bind controlStrokeWidth;
                     strokeDashArray: controlStrokeDashArray;
                     strokeDashOffset:controlStrokeDashOffset;
                  }
//                  Line { // vertical line
//                     startX: bind width,
//                     startY: bind height - controlLength
//                     endX: bind width,
//                     endY: bind height
//                     stroke:bind Color.WHITE
//                     strokeWidth:bind controlStrokeWidth;
//                  }
//                  Line { // vertical line
//                     startX: bind width,
//                     startY: bind height - controlLength
//                     endX: bind width,
//                     endY: bind height
//                     stroke:bind color
//                     strokeWidth:bind controlStrokeWidth;
//                     strokeDashArray: controlStrokeDashArray;
//                     strokeDashOffset:controlStrokeDashOffset;
//                  }
//                  Line { // horizontal line
//                     startX: bind width,
//                     startY: bind height
//                     endX: bind width - controlLength,
//                     endY: bind height
//                     stroke:bind Color.WHITE
//                     strokeWidth:bind controlStrokeWidth;
//                  }
//                  Line { // horizontal line
//                     startX: bind width,
//                     startY: bind height
//                     endX: bind width - controlLength,
//                     endY: bind height
//                     stroke:bind color
//                     strokeWidth:bind controlStrokeWidth;
//                     strokeDashArray: controlStrokeDashArray;
//                     strokeDashOffset:controlStrokeDashOffset;
//                  }
//							Line { // vertical line
//                     startX: bind width,
//                     startY: bind height - controlLength
//                     endX: bind width,
//                     endY: bind height
//                     stroke:bind color
//                     strokeWidth:bind controlStrokeWidth;
//                 }
//							Line { // horizontal line
//                     startX: bind width,
//                     startY: bind height
//                     endX: bind width - controlLength,
//                     endY: bind height
//                     stroke:bind color
//                     strokeWidth:bind controlStrokeWidth;
//                  }
//							Line { // top-right devider
//							startX: bind width,
//							startY: bind height - controlLength - controlStrokeWidth / 2
//							endX: bind width,
//							endY: bind height - controlLength - controlStrokeWidth / 2
//							strokeWidth:bind controlStrokeWidth;
//							stroke: Color.WHITE
//						}
//							Line { // bottom left devider
//							startX:bind width - controlLength,
//							startY: bind height
//							endX:bind width - controlLength,
//							endY: bind height
//							strokeWidth:bind controlStrokeWidth;
//							stroke: Color.WHITE
//						}
               ]
               onMousePressed: function( e: MouseEvent ):Void {
						if (allowResize){
							startDragging(e);
						}
						else {
							activate();
							openWindow(minimumWidth,minimumHeight);
						}
               }
               onMouseDragged: function( e: MouseEvent ):Void {
						if (allowResize){
							doResize(e);
						}
               }
            },
					Group{ // bottom left rotate element
					blocksMouse:true;
               visible:bind allowRotate;
					content:[
						Arc {
							centerX: controlLength,
							centerY: bind height - controlLength,
							radiusX: controlLength,
							radiusY: controlLength
							startAngle: 180,
							length: 90
							type: ArcType.OPEN
							fill: Color.TRANSPARENT
							stroke:Color.WHITE
							strokeWidth:bind controlStrokeWidth;
//                     strokeDashArray: controlStrokeDashArray;
//                     strokeDashOffset:controlStrokeDashOffset;
						}
						Arc {
							centerX: controlLength,
							centerY: bind height - controlLength,
							radiusX: controlLength,
							radiusY: controlLength
							startAngle: 180,
							length: 90
							type: ArcType.OPEN
							fill: Color.TRANSPARENT
							stroke:bind color
							strokeWidth:bind controlStrokeWidth;
                     strokeDashArray: controlStrokeDashArray;
                     strokeDashOffset:controlStrokeDashOffset;
						}
//							Line { // top-left devider
//							startX: 0,
//							startY: bind height - controlLength - controlStrokeWidth / 2
//							endX: 0,
//							endY: bind height - controlLength - controlStrokeWidth / 2
//							strokeWidth:bind controlStrokeWidth;
//							stroke: Color.WHITE
//						}
//							Line { // bottom right devider
//							startX: controlLength,
//							startY: bind height
//							endX: controlLength,
//							endY: bind height
//							strokeWidth:bind controlStrokeWidth;
//							stroke: Color.WHITE
//						}
					]
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
		};;
	}
}

	function hideScyWindow(scyWindow:ScyWindow):Void{
      scyWindow.hideTo(scyWindow.translateX, scyWindow.translateY);
	}

	function showScyWindow(scyWindow:ScyWindow):Void{
      scyWindow.showFrom(scyWindow.translateX, scyWindow.translateY);
	}

	function closeScyWindow(scyWindow:ScyWindow):Void{
      scyWindow.closeIt();
	}

function run() {
   //	var image = Image{
   //		url: "{__DIR__}Water lilies.jpg"}
   //	var imageView = ImageView{
   //		image:image
   //  }
   //	var imageView2 = ImageView{
   //		image:image
   //  }
   //	var testContent = Rectangle {
   //		x: 0,
   //		y: 0
   //		width: 200,
   //		height: 200
   //		fill: Color.RED
   //   }
   //	var whiteboard = new WhiteboardPanel();
   //	var whiteboardNode = SwingComponent.wrap(whiteboard);
   //	//whiteboardNode.cursor = null;
   //
   //   var tree = new JTree();
   //	var treeNode = SwingComponent.wrap(tree);
   //

   var scyDesktop:ScyDesktop = ScyDesktop{};

   var newGroup = VBox
   {
		translateX:5
		translateY:5;
		spacing:3;
      content:[
         SwingButton{
            text: "Drawing"
            action: function() {
               var newWhiteboard = new WhiteboardPanel();
					//newWhiteboard.setPreferredSize(new Dimension(2000,2000));
               var newWhiteboardNode = SwingComponent.wrap(newWhiteboard);
               var drawingWindow = ScyWindow{
                  color:Color.BLUE
                  title:"Drawing"
                  scyContent: newWhiteboardNode
                  visible:true
						cache:true
               }
               scyDesktop.addScyWindow(drawingWindow)
            }
         }
         SwingButton{
            text: "Tree"
            action: function() {
               var tree = new JTree();
					var treeSize = new Dimension(2000,2000);
					//tree.setMinimumSize(treeSize);
					//tree.setMaximumSize(treeSize);
					tree.setPreferredSize(treeSize);
					//tree.setSize(treeSize);
               var treeNode = SwingComponent.wrap(tree);
               var drawingWindow = ScyWindow{
                  color:Color.BLUE
                  title:"Drawing"
						width:150
						height:150
                  scyContent: treeNode
						//swingContent:tree
                  visible:true
               }
               scyDesktop.addScyWindow(drawingWindow)
            }
         }
         SwingButton{
            text: "Text"
            action: function() {
					var textArea = new JTextArea();
					textArea.setPreferredSize(new Dimension(2000,2000));
					textArea.setEditable(true);
					textArea.setText("gfggfggfdgdgdfgfgfgfdgafgfgd");
               var textNode = SwingComponent.wrap(textArea);
               var drawingWindow = ScyWindow{
                  color:Color.BLUE
                  title:"Drawing"
						width:150
						height:150
                  scyContent: SwingScrollPane{
							view:textNode
							scrollable:true;
						}
                  visible:true
               }
               scyDesktop.addScyWindow(drawingWindow)
            }
         }
         SwingButton{
            text: "Text 2"
            action: function() {
					var textArea = new JTextArea();
					//textArea.setPreferredSize(new Dimension(2000,2000));
					textArea.setEditable(true);
					textArea.setWrapStyleWord(true);
					textArea.setLineWrap(true);
					//textArea.setText("gfggfggfdgdgdfgfgfgfdgafgfgd");
					var scrollPane = new JScrollPane(textArea);
					//scrollPane.setPreferredSize(new Dimension(200,200));
               var textNode = SwingComponent.wrap(scrollPane);
               var drawingWindow = ScyWindow{
                  color:Color.BLUE
                  title:"Drawing"
                  scyContent: textNode
						visible:true
						//swingContent:scrollPane
						width:150
						height:150
               }
               scyDesktop.addScyWindow(drawingWindow);
					drawingWindow.width = 151;
            }
         }
         SwingButton{
            text: "Red"
            action: function() {
               var drawingWindow = ScyWindow{
						//						 translateX:100;
						//						 translateY:100;
                  color:Color.BLUE
                  title:"Red"
                  scyContent: Rectangle {
                     x: 10,
                     y: 10
                     width: 140,
                     height: 90
                     fill: Color.PERU
                  }
                  visible:true
						//opacity:0;
						//closeAction:closeScyWindow;
               }
               scyDesktop.addScyWindow(drawingWindow);
					var opacityTimeline = Timeline{
						keyFrames: [
						 at (0s){ 
							//drawingWindow.opacity => 0.0;
							drawingWindow.translateX => 0;
							drawingWindow.translateY => 0;
							drawingWindow.width => 0;
							drawingWindow.height => 0;
						 }
						 at (500ms){
							//drawingWindow.opacity => 1.0;
							drawingWindow.translateX => 200;
							drawingWindow.translateY => 200;
							drawingWindow.width => 150;
							drawingWindow.height => 150;
						 }
						];
					}
					opacityTimeline.play();
            }
         }
      ]
   };

   var newScyWindow:ScyWindow= ScyWindow{
      title:"New"
      color:Color.BLUEVIOLET
		height:150;
      //scyContent:newGroup
      allowClose:true;
      allowResize:true;
      allowMinimize:true;
		translateX:20;
		translateY:20;
	   setScyContent:function(scyWindow:ScyWindow){
			println("setScyContent");
			scyWindow.scyContent = newGroup;
//			scyWindow.color =
//				 Color.CORAL;
		};
		closedAction:function(scyWindow:ScyWindow){
			println("closedAction");
			scyWindow.scyContent = null
		}
   };
	newScyWindow.openWindow(0, 150);
   scyDesktop.addScyWindow(newScyWindow);

   var fixedScyWindow= ScyWindow{
      title:"Fixed"
      color:Color.BLUEVIOLET
		height:150;
		//      scyContent:newGroup
      allowClose:false;
      allowResize:false;
      allowRotate:false;
      allowMinimize:false;
		translateX:200;
		translateY:20;
   };
	fixedScyWindow.openWindow(100, 150);
   scyDesktop.addScyWindow(fixedScyWindow);

   var closedScyWindow= ScyWindow{
      title:"Closed and very closed"
      color:Color.GRAY
		height:27;
		isClosed:true;
      allowClose:true;
      allowResize:true;
      allowRotate:true;
      allowMinimize:true;
		translateX:20;
		translateY:200;
   };
//	closedScyWindow.openWindow(100, 150);
   scyDesktop.addScyWindow(closedScyWindow);

	var whiteboard = new WhiteboardPanel();
	whiteboard.setPreferredSize(new Dimension(300,150));
	var whiteboardNode = SwingComponent.wrap(whiteboard);
	whiteboardNode.translateX = 150;
	whiteboardNode.translateY = 25;

	var whiteboard2 = new WhiteboardPanel();
	whiteboard2.setPreferredSize(new Dimension(300,150));
	var whiteboardNode2 = SwingComponent.wrap(whiteboard2);
	var drawingWindow2 = ScyWindow{
		translateX:50
		translateY:200
		width:300;
		height:150
		color:Color.GREEN
		title:"Drawing 2"
		scyContent: whiteboardNode2
		visible:true
		effect: DropShadow {
			offsetX: 4,
			offsetY: 4,
			color: Color.BLACK
		}
   }

	var whiteboard3 = new WhiteboardPanel();
	whiteboard3.setPreferredSize(new Dimension(2000,1500));
	var whiteboardNode3 = SwingComponent.wrap(whiteboard3);
	var drawingWindow3 = ScyWindow{
		translateX:50
		translateY:400
		width:300;
		height:150
		color:Color.GREEN
		title:"Drawing 3"
		scyContent: whiteboardNode3
		visible:true
		//		windowEffect: Lighting {
		//			light: DistantLight {
		//				azimuth: -135
		//			}
		//			surfaceScale: 5
		//		}
		//		windowEffect:Shadow{
		//		}
		windowEffect:DropShadow {
			offsetX: 4,
			offsetY: 4,
			color: Color.BLACK
		}

   }

	//	function setEloContent(scyWindow:ScyWindow):Void{};
	var eloWindow = ScyWindow{
		title:bind "elo window";
		color:bind Color.RED;
		allowClose:true;
		allowMinimize:true;
		allowResize:false;
		allowRotate:true;
		//setScyContent:setEloContent;
		translateX:200
		translateY:200
		}
   scyDesktop.addScyWindow(eloWindow);


	Stage {
		title: "Scy window test"
		width: 400
		height: 600
		scene: Scene {
			content:[
				 scyDesktop.desktop,
//				whiteboardNode,
//				drawingWindow2
//				drawingWindow3
			]

		}
	}
}