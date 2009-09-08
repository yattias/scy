/*
 * StandardScyWindow.fx
 *
 * Created on 2-sep-2009, 14:57:11
 */

package eu.scy.client.desktop.scydesktop.scywindows.window;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Resizable;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Math;
import javafx.util.Sequences;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindowAttribute;
import eu.scy.client.desktop.scydesktop.scywindows.TestAttribute;

import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;

import java.lang.System;

/**
 * @author sikkenj
 */

public class StandardScyWindow extends ScyWindow {
	def scyWindowAttributeDevider = 3.0;

	public override var title = "???";
	public override var eloType = "?123";
   public override var eloUri;
   public override var iconCharacter = "?";
	public override var color = Color.GREEN;
	public override var backgroundColor = color.WHITE;
	public override var width = 100 on replace{
		if (not isAnimating){
			width = Math.max(width,minimumWidth);
			resizeTheContent()
		}
   };

	public override var height = 100 on replace{
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
   public override var widthHeightProportion = -1.0;
	public override var scyContent;
	public override var scyTool;
   public override var scyWindowAttributes on replace {
      placeAttributes()
   };
   public override var allowRotate = true;
   public override var allowResize = true;
   public override var allowDragging = true;
   public override var allowClose = true;
   public override var allowMinimize = true;
   public override var closeIsHide = false;
   public override var scyDesktop;
	public override var windowEffect;
	//	public var closeAction:function(ScyWindow):Void;
//	public var minimizeAction: function(ScyWindow):Void;
//	public var setScyContent: function(ScyWindow):Void;
//	public var aboutToCloseAction: function(ScyWindow):Boolean;
//	public var closedAction: function(ScyWindow):Void;

	// status variables
	var isAnimating = false;

	// layout constants
	def windowBackgroundColor = Color.WHITE;
	def controlColor = Color.WHITE;
	def controlLength = 18;
	def controlStrokeWidth = 4;

   def iconSize = 16;
	def borderWidth = 4;
   def titleBarTopOffset = borderWidth/2 + 3;
   def titleBarLeftOffset = 12;
   def closedHeight = titleBarTopOffset+iconSize+borderWidth;
   def contentTopOffset = titleBarTopOffset+iconSize+borderWidth/2 + 1;
   def contentBorder = 2;

   def contentWidth = bind width - borderWidth - 2 * contentBorder - 1;
   def contentHeight = bind height - contentTopOffset - borderWidth / 2 - 2 * contentBorder;

	//def minimumHeigth = topLeftBlockSize + dragLength + 2 * dragBorder + borderWidth;
	//def minimumWidth = 2 * borderWidth + 3 * dragBorder + 2 * dragStrokeWith + 2 * dragLength + borderBlockOffset;
	public-read var originalTranslateX: Number;
	public-read var originalTranslateY: Number;
//	public-read var originalWidth: Number;
//	public-read var originalHeight: Number;

   def contentGlassPane = Rectangle {
         x: 0, y: 0
         width: 140, height: 90
         fill: Color.TRANSPARENT
//         fill: Color.rgb(128,128,128,0.5)
      }

	var originalX: Number;
	var originalY: Number;
	var originalW: Number;
	var originalH: Number;
	var rotateCenterX: Number;
	var rotateCenterY: Number;
	var initialRotation: Number;
   var maxDifX: Number;
   var maxDifY: Number;
	var sceneTopLeft: Point2D;

	def resizeAnimationTime = 250ms;
	def opcityAnimationTime = 250ms;
	def closeAnimationTime = 350ms;

   var resizeHighLighted = false;
   var rotateHighLighted = false;
   var closeHighLighted = false;
   var minimizeHighLighted = false;
   var unminimizeHighLighted = false;

   public override var minimumHeight = 50 on replace{
      minimumHeight = Math.max(minimumHeight, contentTopOffset+2*contentBorder);
   }
   public override var minimumWidth = 70 on replace{
      minimumWidth = Math.max(minimumWidth, 2 * borderWidth + 3 * controlLength);
   }

   var emptyWindow:EmptyWindow;
   var windowTitleBar:WindowTitleBar;
   var resizeElement:WindowResize;
   var rotateElement:WindowRotate;
   var closeElement: WindowClose;
   var minimizeElement: WindowMinimize;

	postinit {
		if (isClosed){
			height = closedHeight;
		}
		resizeTheContent();
   }

   function placeAttributes(){
      var sortedScyWindowAttributes =
      Sequences.sort(scyWindowAttributes,null) as ScyWindowAttribute[];
      var x = 0.0;
      for (scyWindowAttribute in reverse sortedScyWindowAttributes){
         scyWindowAttribute.translateX = x;
         x += scyWindowAttribute.boundsInLocal.width;
         x += scyWindowAttributeDevider;
      }

   }


	public override function openWindow(openWidth:Number,openHeight:Number){
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
					time: resizeAnimationTime;
					values: [
						translateX => endX tween Interpolator.EASEBOTH,
						translateY => endY tween Interpolator.EASEBOTH,
						width => minimumWidth tween Interpolator.EASEBOTH,
						height => minimumHeight tween Interpolator.EASEBOTH
					]
				}
				KeyFrame{
					time: resizeAnimationTime + opcityAnimationTime;
					values: [
						opacity => 0 tween Interpolator.EASEBOTH
					]
					action: function(){
						if (scyDesktop != null){
							scyDesktop.hideScyWindow(this);
						}
						isAnimating = false;
					}
				}
			];
		}
	}

	function getCloseTimeline(endX:Number,endY:Number):Timeline{
		return Timeline{
			keyFrames: [
				KeyFrame{
					time: closeAnimationTime;
					values: [
						translateX => endX,
						translateY => endY,
						scaleX =>
						1.0 / width,
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
			keyFrames: [
				KeyFrame{
					time: closeAnimationTime;
					values: [
						translateX => endX  tween Interpolator.EASEBOTH,
						translateY => endY tween Interpolator.EASEBOTH,
						width => minimumWidth tween Interpolator.EASEBOTH,
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
			keyFrames: [
				KeyFrame{
					time: closeAnimationTime;
					values: [
						translateX => endX,
						translateY => endY,
						width => endWidth,
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

	public override function close(){
		var closeTimeline = getCloseTimeline(translateX + width / 2,translateY - height / 2);
		closeTimeline.play();
	}

	function resizeTheContent(){
      contentGlassPane.width=contentWidth;
      contentGlassPane.height = contentHeight;
      if (scyContent instanceof Resizable){
			var resizeableScyContent = scyContent as Resizable;
			resizeableScyContent.width = contentWidth;
			resizeableScyContent.height = contentHeight;
		}
	}


	function startDragging(e: MouseEvent):Void {
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
      contentGlassPane.blocksMouse = true;
	}

	function stopDragging(e: MouseEvent):Void {
      contentGlassPane.blocksMouse = false;
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
        /* done by a hack in the edges now
        for(edge in edges) {
            edge.repaint();
        } */
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
		var difX: Number;
		var difY: Number;
		difX = (1 - Math.cos(angle)) * difW / 2;
		difY = (1 - Math.cos(angle)) * difH / 2;
		//difX = (1 - Math.cos(angle)) * e.dragX/2;// + (1 - Math.sin(angle)) * e.dragY/2;
		//difY = (1 - Math.cos(angle)) * e.dragY/2;// + (1 - Math.sin(angle)) * e.dragX/2;
		width = Math.max(minimumWidth,originalW + difW);
		height =Math.max(minimumHeight,originalH + difH);
		translateX = originalX - difX;
		translateY = originalY - difY;
//      for(edge in edges) {
//         edge.repaint();
//      }

//		var newSceneTopLeft = localToScene(0,0);
//		System.out.println("resized {title}, angle: {rotate}, difW: {difW}, difH: {difH}, difX: {difX}, difY: {difY}, dtlX:{sceneTopLeft.x-newSceneTopLeft.x}, dtlY:{sceneTopLeft.y-newSceneTopLeft.y}");

	}

	function doRotate(e: MouseEvent):Void {
      printMousePos("rotate",e);
//      if (isInvalidMousePos(e))
//		return;
		var newRotation = calculateRotation(e);
		var deltaRotation = Math.toDegrees(newRotation - initialRotation);
		var newRotate = rotate + deltaRotation;
		if (e.controlDown){
			newRotate = 45 * Math.round(newRotate  /  45);
		}
		rotate = newRotate;

        // quick & dirty for the demo: if the content is the Simulator Tool,
      // pass the rotation value to it
      // in future versions, we may add an interface "RotationAware" that
      // indicates a tool`s interest in rotation values
//      if (scyContent instanceof eu.scy.elobrowser.tool.scysimulator.SimQuestNode) {
//         (scyContent as eu.scy.elobrowser.tool.scysimulator.SimQuestNode).doRotate(rotate);
//      }
		//System.out.println("rotated {title}, deltaRotation: {deltaRotation}");

   }

	function calculateRotation(e: MouseEvent):Number{
		var deltaX = e.x - rotateCenterX;
		var deltaY = e.y - rotateCenterY;
      return Math.atan2(deltaY , deltaX);
//		var rotation = -Math.atan(deltaX / deltaY);
//		if (deltaY >= 0)
//		rotation += Math.PI;
//		// System.out.println("deltaX " + deltaX + ", deltaY " + deltaY + ", rotation " + rotation);
//		return rotation;
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

   public function setMinimized(newMinimized:Boolean){
 //       isClosed = false;
      if (newMinimized != isMinimized){
         if (newMinimized){
            //                originalTranslateX = translateX;
            //                originalTranslateY = translateY;
            originalWidth = width;
            originalHeight = height;
            width = minimumWidth;
            height = closedHeight;
            isMinimized = true;
         }
            else {
            //                translateX = originalTranslateX;
            //                translateY = originalTranslateY;
            width = originalWidth;
            height = originalHeight;
            isMinimized = false;
         }
      }
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

      emptyWindow = EmptyWindow{
         width: bind width;
         height:bind height;
         controlSize:controlLength;
         borderWidth:borderWidth;
         borderColor:bind color;
         backgroundColor:bind windowBackgroundColor;
      }

      windowTitleBar = WindowTitleBar{
         width:bind width - 1 * borderWidth - titleBarLeftOffset
         iconSize:iconSize;
         iconGap:borderWidth/2;
         color:bind color;
         title:bind title;
         typeChar:bind eloType;
         layoutX:titleBarLeftOffset;
         layoutY:titleBarTopOffset;
      }

      resizeElement = WindowResize{
         visible: bind allowResize or isClosed;
         size:controlLength;
         color:bind color;
         subColor:controlColor;
         activate: activate;
         startResize:startDragging;
         doResize:doResize;
         stopResize:stopDragging;
         layoutX: bind width;
         layoutY: bind height;
      }

      rotateElement = WindowRotate{
         visible: bind allowRotate;
         size:controlLength;
         color:bind color;
         subColor:controlColor;
         activate: activate;
         startRotate:startDragging;
         doRotate:doRotate;
         stopRotate:stopDragging;
         layoutX: controlLength;
         layoutY: bind height-controlLength;
      }

      closeElement = WindowClose{
         visible: bind allowClose and not isClosed;
         size:controlLength;
         strokeWidth:controlStrokeWidth/2;
         color:bind color;
         subColor:controlColor;
         activate: activate;
         closeAction:doClose;
         layoutX: bind width - controlLength / 2;
         layoutY: -controlLength / 2;
      }

      minimizeElement = WindowMinimize{
         visible: bind allowMinimize and not isClosed;
         size:controlLength;
         strokeWidth:controlStrokeWidth/2;
         color:bind color;
         subColor:controlColor;
         activate: activate;
         minimizeAction:doMinimize;
         unminimizeAction:doUnminimize;
         minimized: bind isMinimized;
         layoutX: bind width / 2;
         layoutY: bind height;
      }

      // show a filled rect as content for test purposes
//      scyContent = Rectangle {
//         x: -100, y: -100
//         width: 1000, height: 1000
//         fill: Color.color(1,.25,.25,.75)
//      }

		return Group {
         cursor: Cursor.MOVE;

			content: [
            emptyWindow,
            Group{ // the content
               blocksMouse: true;
               cursor: Cursor.DEFAULT;
               translateX: borderWidth / 2 + 1 + contentBorder
               translateY: contentTopOffset + contentBorder
               clip: Rectangle {
                  x: 0,
                  y: 0
                  width: bind contentWidth
                  height: bind contentHeight
                  fill: Color.BLACK
               }
               content: bind [
                  scyContent,
                  contentGlassPane
               ]
               onMousePressed: function( e: MouseEvent ):Void {
                  activate();
               }
            }
            windowTitleBar,
            minimizeElement,
            resizeElement,
            rotateElement,
            closeElement,
            Group{ // the scy window attributes
               translateY: -borderWidth / 2;
               content: bind scyWindowAttributes,
            },
//            draggingLayer,
//            Group {
//               content: [circleLayer]
//               effect: DropShadow {
//                  offsetX: 3
//                  offsetY: 3
//                  color: Color.BLACK
//                  radius: 10
//               }
//               onMouseReleased: function( e: MouseEvent ):Void {
//                  println("G entered");
//               }
//            }


			]
			onMousePressed: function( e: MouseEvent ):Void {
            if (allowDragging) {
               startDragging(e);
            }
			}
			onMouseDragged: function( e: MouseEvent ):Void {
            if (allowDragging) {
               doDrag(e);
            }
			}
         onMouseReleased: function( e: MouseEvent ):Void {
            if (allowDragging){
               stopDragging(e);
            }
         }
		};
	}
}

//function hideScyWindow(scyWindow:ScyWindow):Void{
//   scyWindow.hideTo(scyWindow.translateX, scyWindow.translateY);
//}
//
//function showScyWindow(scyWindow:ScyWindow):Void{
//   scyWindow.showFrom(scyWindow.translateX, scyWindow.translateY);
//}
//
//function closeScyWindow(scyWindow:ScyWindow):Void{
//   scyWindow.closeIt();
//}

function run() {

//   var scyDesktop: WindowManager = WindowManagerImpl{
//   };

//   var newGroup = VBox {
//      translateX: 5
//      translateY: 5;
//      spacing: 3;
//      content: [
//         SwingButton{
//            text: "Tree"
//            action: function() {
//               var tree = new JTree();
//               var treeSize = new Dimension(2000,2000);
//					//tree.setMinimumSize(treeSize);
//               //tree.setMaximumSize(treeSize);
//               tree.setPreferredSize(treeSize);
//					//tree.setSize(treeSize);
//               var treeNode = SwingComponent.wrap(tree);
//               var drawingWindow = StandardScyWindow{
//                  color: Color.BLUE
//                  title: "Drawing"
//                  width: 150
//                  height: 150
//                  scyContent: treeNode
//						//swingContent:tree
//                  visible: true
//               }
//               scyDesktop.addScyWindow(drawingWindow)
//            }
//         }
//         SwingButton{
//            text: "Text"
//            action: function() {
//               var textArea = new JTextArea();
//               textArea.setPreferredSize(new Dimension(2000,2000));
//               textArea.setEditable(true);
//               textArea.setText("gfggfggfdgdgdfgfgfgfdgafgfgd");
//               var textNode = SwingComponent.wrap(textArea);
//               var drawingWindow = StandardScyWindow{
//                  color: Color.BLUE
//                  title: "Drawing"
//                  width: 150
//                  height: 150
//                  scyContent: SwingScrollPane{
//                     view: textNode
//                     scrollable: true;
//                  }
//                  visible: true
//               }
//               scyDesktop.addScyWindow(drawingWindow)
//            }
//         }
//         SwingButton{
//            text: "Text 2"
//            action: function() {
//               var textArea = new JTextArea();
//					//textArea.setPreferredSize(new Dimension(2000,2000));
//               textArea.setEditable(true);
//               textArea.setWrapStyleWord(true);
//               textArea.setLineWrap(true);
//					//textArea.setText("gfggfggfdgdgdfgfgfgfdgafgfgd");
//               var scrollPane = new JScrollPane(textArea);
//					//scrollPane.setPreferredSize(new Dimension(200,200));
//               var textNode = SwingComponent.wrap(scrollPane);
//               var drawingWindow = StandardScyWindow{
//                  color: Color.BLUE
//                  title: "Drawing"
//                  scyContent: textNode
//                  visible: true
//						//swingContent:scrollPane
//                  width: 150
//                  height: 150
//               }
//               scyDesktop.addScyWindow(drawingWindow);
//               drawingWindow.width = 151;
//            }
//         }
//         SwingButton{
//            text: "Red"
//            action: function() {
//               var drawingWindow = StandardScyWindow{
//						//						 translateX:100;
//                  //						 translateY:100;
//                  color: Color.BLUE
//                  title: "Red"
//                  scyContent: Rectangle {
//                     x: 10,
//                     y: 10
//                     width: 140,
//                     height: 90
//                     fill: Color.PERU
//                  }
//                  visible: true
//						//opacity:0;
//						//closeAction:closeScyWindow;
//
//               }
//               scyDesktop.addScyWindow(drawingWindow);
//               var opacityTimeline = Timeline{
//                  keyFrames: [
//						 at (0s){
//							//drawingWindow.opacity => 0.0;
//                     drawingWindow.translateX => 0;
//                     drawingWindow.translateY => 0;
//                     drawingWindow.width => 0;
//                     drawingWindow.height => 0;
//						 }
//						 at (500ms){
//							//drawingWindow.opacity => 1.0;
//                     drawingWindow.translateX => 200;
//                     drawingWindow.translateY => 200;
//                     drawingWindow.width => 150;
//                     drawingWindow.height => 150;
//						 }
//                  ];
//               }
//               opacityTimeline.play();
//            }
//         }
//      ]
//   };

//   var newScyWindow: ScyWindow= StandardScyWindow{
//      title: "New"
//      color: Color.BLUEVIOLET
//      height: 150;
//      //scyContent:newGroup
//      allowClose: true;
//      allowResize: true;
//      allowMinimize: true;
//      translateX: 20;
//      translateY: 20;
//      setScyContent: function(scyWindow:ScyWindow){
//         println("setScyContent");
//         scyWindow.scyContent = newGroup;
////			scyWindow.color =
////				 Color.CORAL;
//
//
//      };
//      closedAction: function(scyWindow:ScyWindow){
//         println("closedAction");
//         scyWindow.scyContent = null
//      }
//   };
//   newScyWindow.open();
//   //newScyWindow.openWindow(0, 150);
//   scyDesktop.addScyWindow(newScyWindow);

   var fixedScyWindow = StandardScyWindow{
      title: "Fixed"
      color: Color.GREEN
      height: 150;
		//      scyContent:newGroup
      allowClose: false;
      allowResize: false;
      allowRotate: false;
      allowMinimize: false;
      translateX: 200;
      translateY: 20;
   };
   fixedScyWindow.openWindow(100, 150);
//   scyDesktop.addScyWindow(fixedScyWindow);

   var closedScyWindow = StandardScyWindow{
      title: "Closed and very closed"
      eloType: "M"
      color: Color.GRAY
      height: 27;
      isClosed: true
      allowClose: true;
      allowResize: true;
      allowRotate: true;
      allowMinimize: true;
      translateX: 20;
      translateY: 200;
   };
   //	closedScyWindow.openWindow(100, 150);
//   scyDesktop.addScyWindow(closedScyWindow);

   var eloWindow = StandardScyWindow{
      title: bind "elo window";
      color: bind Color.RED;
      allowClose: true;
      allowMinimize: true;
      allowResize: false;
      allowRotate: true;
		//setScyContent:setEloContent;
		translateX: 200
		translateY: 200
      scyWindowAttributes: [
         TestAttribute{
         }

      ]
   }
//   scyDesktop.addScyWindow(eloWindow);


   Stage {
      title: "Scy window test"
      width: 400
      height: 600
      scene: Scene {
         content: [
            fixedScyWindow,
            eloWindow,
            closedScyWindow
//            scyDesktop.scyWindows
//				whiteboardNode,
//				drawingWindow2
//				drawingWindow3
         ]

      }
   }
}