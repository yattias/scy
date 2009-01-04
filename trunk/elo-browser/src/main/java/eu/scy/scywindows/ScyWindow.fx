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

/**
 * @author sikken
 */

 // place your code here
public class ScyWindow extends CustomNode {
	public var title="???";
	public var color = Color.GREEN;
	public var backgroundColor = color.WHITE;
	public var width:Number = 100 on replace{
		width = Math.max(width,minimumWidth);
		resizeTheContent()};
	public var height:Number = 100 on replace{
		height = Math.max(height, minimumHeight);
		resizeTheContent()};
	public var scyContent:Node= Rectangle {
      x: 0,
      y: 0
      width: 10,
      height: 10
      fill: Color.TRANSPARENT
		};
   public var allowRotate = true;
   public var allowResize = true;
   public var allowClose = true;
   public var allowMinimize = false;
   public var closeIsHide = false;
   public var scyDesktop:ScyDesktop;
	public var windowEffect:Effect;
	public var closeAction:function(ScyWindow):Void;
	public var minimizeAction:function(ScyWindow):Void;

	var borderWidth = 3;
	var topLeftBlockSize = 23;
   var closeColor = Color.WHITE;
   var closeMouseOverEffect:Effect = Glow{
      level:1}
   var closeStrokeWidth = 2;
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
	var resizeAnimationTime = 250ms;
	var opcityAnimationTime = 250ms;
	var closeAnimationTime = 350ms;

   public var minimumHeight:Number = 100 on replace{
      minimumHeight = Math.max(minimumHeight, topLeftBlockSize + dragLength + 2 * dragBorder + borderWidth);
   }
   public var minimumWidth:Number = 100 on replace{
      minimumWidth = Math.max(minimumWidth, 2 * borderWidth + 3 * dragBorder + 2 * dragStrokeWith + 2 * dragLength + borderBlockOffset);
   }

	postinit {
		resizeTheContent();
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
						scyDesktop.hideScyWindow(this);
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
						scyDesktop.removeScyWindow(this);
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
		var closeTimeline = getCloseTimeline(translateX+width/2,translateY-height/2);
		closeTimeline.play();
	}

	function resizeTheContent(){
		if (scyContent instanceof Resizable){
			var contentWidth = width - borderBlockOffset - borderWidth - 0;
			var contentHeight = height - borderBlockOffset - borderWidth - titleFontsize - fontHeightCompensation - lineOffsetY + lineWidth / 2 + 0;
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
      if (scyDesktop != null)
      scyDesktop.activateScyWindow(this);
   }


	public override function create(): Node {
		blocksMouse = true;
		return Group {
         cursor: Cursor.MOVE;

			content: [
				//					Rectangle { // main border, only for effect, applying effect on main slows things down
				//					x: 0,
				//					y: borderBlockOffset;
				//					width: bind width - borderBlockOffset,
				//					height: bind height - borderBlockOffset
				//					fill:bind backgroundColor;
				//					stroke:bind color
				//					strokeWidth:borderWidth;
				//					effect:bind windowEffect;
				//				}
				Rectangle { // main border
					x: borderBlockOffset,
					y: borderBlockOffset;
					width: bind width - 2 * borderBlockOffset,
					height: bind height - borderBlockOffset
					fill:bind backgroundColor;
					stroke:bind color
					strokeWidth:borderWidth;
					//effect:bind windowEffect;
				}
				Line { // right border line, only for the effect
					startX: bind width - borderBlockOffset,
					startY: borderBlockOffset
					endX: bind width - borderBlockOffset,
					endY: bind height
					stroke:bind color
					strokeWidth:borderWidth;
					effect:bind windowEffect;
				}
				Line { // bottom border line, only for the effect
					startX: borderBlockOffset,
					startY: bind height
					endX: bind width - borderBlockOffset,
					endY: bind height
					stroke:bind color
					strokeWidth:borderWidth;
					effect:bind windowEffect;
				}
				Line { // right top block border line, only for the effect
					startX: bind width - borderWidth / 2,
					startY: borderWidth / 2
					endX: bind width - borderWidth / 2,
					endY: topLeftBlockSize - borderWidth / 2
					stroke:bind color
					strokeWidth:borderWidth;
					effect:bind windowEffect;
				}
				Group{ // the content
               blocksMouse:true;
               cursor:Cursor.DEFAULT;
               translateX:borderBlockOffset + borderWidth / 2 + contentBorder + 1
               translateY:borderBlockOffset + borderWidth / 2 + titleFontsize - fontHeightCompensation + lineOffsetY + contentBorder + lineWidth / 2 + 1
               clip:Rectangle {
                  x: 0,
                  y: 0
                  width: bind width - 2 * borderBlockOffset - borderWidth - 0,
                  height: bind height - borderBlockOffset - borderWidth - titleFontsize - fontHeightCompensation - lineOffsetY + lineWidth / 2 + 0
                  fill: Color.BLACK
               }
               content:bind scyContent
               onMousePressed: function( e: MouseEvent ):Void {
                  activate();
               }
            }
				Rectangle { // top left block
               x: 0,
               y: 0;
               width: topLeftBlockSize,
               height: topLeftBlockSize
               fill: bind color
					//effect:bind windowEffect;
            }
				minimizeElement = Group{
					cursor:Cursor.HAND
					visible:bind allowMinimize
					content:[
						Rectangle { // top left block
							x: 0,
							y: 0;
							width: topLeftBlockSize,
							height: topLeftBlockSize
							fill: bind color
					//effect:bind windowEffect;
						}
						Polyline {
							points: [ closeStrokeWidth,closeStrokeWidth topLeftBlockSize - closeStrokeWidth - 1,closeStrokeWidth topLeftBlockSize / 2,topLeftBlockSize - closeStrokeWidth - 1 closeStrokeWidth,closeStrokeWidth]
							strokeWidth: closeStrokeWidth
							stroke: closeColor
						}
					]
               onMouseClicked: function( e: MouseEvent ):Void {
                  if (scyDesktop != null)
                  {
							if (minimizeAction != null){
								minimizeAction(this)
							}
							else {
								originalTranslateX = translateX;
								originalTranslateY = translateY;
								originalWidth = width;
								originalHeight = height;
								scyDesktop.hideScyWindow(this);
							}
							System.out.println("hided {title}");
                  }
					}
               onMouseEntered: function( e: MouseEvent ):Void {
                  minimizeElement.effect = closeMouseOverEffect;
               }
               onMouseExited: function( e: MouseEvent ):Void {
                  minimizeElement.effect = null;
               }

				}
				Rectangle { // top right block
               x: bind width - topLeftBlockSize,
               y: 0;
               width: topLeftBlockSize,
               height: topLeftBlockSize
               fill: bind color
					//effect:bind windowEffect;
            }
					closeElement = Group{ // close button
               cursor:Cursor.HAND
               visible:bind allowClose
               content:[
							Rectangle { // top left block
                     x: bind width - topLeftBlockSize,
                     y: 0;
                     width: topLeftBlockSize,
                     height: topLeftBlockSize
                     fill: bind color
                  }
							Group{ // close cross
								clip: Rectangle { // top left block
                        x: bind width - topLeftBlockSize,
                        y: 0;
                        width: topLeftBlockSize,
                        height: topLeftBlockSize
                     }
                     content:[
                        Line {
                           startX: bind width - topLeftBlockSize,
                           startY: 0
                           endX: bind width,
                           endY: bind topLeftBlockSize
                           strokeWidth: closeStrokeWidth
                           stroke: closeColor
                        }
                        Line {
                           startX: bind width,
                           startY: 0
                           endX: bind width - topLeftBlockSize,
                           endY: bind topLeftBlockSize
                           strokeWidth: closeStrokeWidth
                           stroke: closeColor
                        }
                     ]
                  }
               ]
               onMouseClicked: function( e: MouseEvent ):Void {
                  if (scyDesktop != null)
                  {
 							if (closeAction != null){
								closeAction(this);
							}
							else {
								if (closeIsHide){
									scyDesktop.hideScyWindow(this);
									System.out.println("hided {title}");
								}
								else {
									scyDesktop.removeScyWindow(this);
									System.out.println("closed {title}");
								}
							}
						}
					}
               onMouseEntered: function( e: MouseEvent ):Void {
                  closeElement.effect = closeMouseOverEffect;
               }
               onMouseExited: function( e: MouseEvent ):Void {
                  closeElement.effect = null;
               }
            }
				Text { // title
					font: textFont
					x: topLeftBlockSize + borderWidth,
					y: borderBlockOffset + borderWidth / 2 + titleFontsize - fontHeightCompensation
					clip:Rectangle {
						x: topLeftBlockSize,
						y: 0
						width: bind width - 2 * topLeftBlockSize - borderWidth  + 1,
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
					centerX: borderBlockOffset + borderWidth + dragBorder + dragLength,
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
						closeAction:closeScyWindow;
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
   var newScyWindow= ScyWindow{
      title:"New"
      color:Color.BLUEVIOLET
		height:150;
      scyContent:newGroup
      allowClose:false;
      allowResize:false;
      allowMinimize:false;
   };
   scyDesktop.addScyWindow(newScyWindow);

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