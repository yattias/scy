/*
 * StandardScyWindow.fx
 *
 * Created on 2-sep-2009, 14:57:11
 */

package eu.scy.client.desktop.scydesktop.scywindows.window;

import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import eu.scy.client.desktop.scydesktop.scywindows.TestAttribute;

import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import javafx.scene.control.Button;
import eu.scy.client.desktop.scydesktop.tools.corner.missionmap.MissionModelFX;
import eu.scy.client.desktop.scydesktop.utils.log4j.Logger;

import eu.scy.client.desktop.scydesktop.tooltips.TooltipCreator;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Resizable;
import javafx.scene.shape.Rectangle;
import javafx.util.Math;
import javafx.util.Sequences;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindowAttribute;
import eu.scy.client.desktop.scydesktop.scywindows.window.BottomDrawer;
import eu.scy.client.desktop.scydesktop.scywindows.window.EmptyWindow;
import eu.scy.client.desktop.scydesktop.scywindows.window.LeftDrawer;
import eu.scy.client.desktop.scydesktop.scywindows.window.MouseBlocker;
import eu.scy.client.desktop.scydesktop.scywindows.window.MouseEventInScene;
import eu.scy.client.desktop.scydesktop.scywindows.window.RightDrawer;
import eu.scy.client.desktop.scydesktop.scywindows.window.ScyToolsList;
import eu.scy.client.desktop.scydesktop.scywindows.window.TopDrawer;
import eu.scy.client.desktop.scydesktop.scywindows.window.WindowChangesListener;
import eu.scy.client.desktop.scydesktop.scywindows.window.WindowClose;
import eu.scy.client.desktop.scydesktop.scywindows.window.WindowContent;
import eu.scy.client.desktop.scydesktop.scywindows.window.WindowMinimize;
import eu.scy.client.desktop.scydesktop.scywindows.window.WindowResize;
import eu.scy.client.desktop.scydesktop.scywindows.window.WindowRotate;
import eu.scy.client.desktop.scydesktop.scywindows.window.WindowTitleBar;
import eu.scy.client.desktop.scydesktop.ScyToolActionLogger;
import eu.scy.client.desktop.scydesktop.art.ScyColors;
import eu.scy.client.desktop.scydesktop.art.WindowColorScheme;
import javafx.scene.layout.Container;

/**
 * @author sikkenj
 */

public class StandardScyWindow extends ScyWindow, TooltipCreator {
   def logger = Logger.getLogger(this.getClass());
	def scyWindowAttributeDevider = 3.0;

	public override var title = "???";
	public override var eloType = "?123";
   public override var eloUri on replace oldEloUri {missionModelFX.eloUriChanged(oldEloUri, eloUri);};
	public override var width = 150 on replace oldWidth {
//      println("before width from {oldWidth} size: {width}*{height}, content: {contentWidth}*{contentHeight} of {eloUri}");
		if (not isAnimating){
         width = limitSize(width,height).x;
		}
      realWidth = width;
//      println("after width from {oldWidth} size: {width}*{height}, content: {contentWidth}*{contentHeight} of {eloUri}");
   };

	public override var height = 100 on replace oldHeight{
//      println("before height from {oldHeight} size: {width}*{height}, content: {contentWidth}*{contentHeight} of {eloUri}");
		if (not isAnimating){
			if (isClosed or isMinimized){
				height = closedHeight;
			}
			else {
            height = limitSize(width,height).y;
			}
		}
      realHeight = height;
//      println("after height from {oldHeight} size: {width}*{height}, content: {contentWidth}*{contentHeight} of {eloUri}");
   };
   public override var widthHeightProportion = -1.0;
	public override var scyContent on replace {
      scyContentChanged();
   };
//	public override var scyTool;
   public override var scyToolsList = ScyToolsList{};


	public override var activated on replace {activeStateChanged()};


   public override var scyWindowAttributes on replace {
      placeAttributes()
   };
   public override var allowRotate = true;
   public override var allowResize = true;
   public override var allowDragging = true;
   public override var allowClose = true;
   public override var allowMinimize = true;
//   public override var closeIsHide = false;
	public override var windowEffect;
	//	public var closeAction:function(ScyWindow):Void;
//	public var minimizeAction: function(ScyWindow):Void;
//	public var setScyContent: function(ScyWindow):Void;
//	public var aboutToCloseAction: function(ScyWindow):Boolean;
//	public var closedAction: function(ScyWindow):Void;

	// status variables
	var isAnimating = false;

	// layout constants
   def borderWidth = 2.0;
   def controlSize = 10.0;
   def cornerRadius = 10.0;
   def titleBarTopOffset = borderWidth/2 + 3;
   def titleBarLeftOffset = borderWidth/2 + 5;
   def titleBarRightOffset = titleBarLeftOffset;
   def closeBoxSize = 10.0;
   def iconSize = 16.0;
   def separatorLength = 7.0;

//   def iconSize = 16.0;
//   def iconGap = 2.0;
//	def borderWidth = 1.0;
//   def secondBorderWidth = 2.0;
//   def controlBorderOffset = (borderWidth+secondBorderWidth)/2;
//   def titleBarTopOffset = borderWidth/2 + 3;
//   def titleBarLeftOffset = 12.0;
   def closedHeight = titleBarTopOffset+iconSize+borderWidth+1;
//   def contentTopOffset = titleBarTopOffset+iconSize+borderWidth/2 + 1;
//   def contentBorder = 2.0;

   def drawerCornerOffset = controlSize+1.5*separatorLength;
//   def drawerBorderGap = secondBorderWidth;
   def drawerBorderOffset = borderWidth;

   def contentSideBorder = 5.0;
   def contentTopOffset = titleBarLeftOffset+iconSize+contentSideBorder;
   def deltaContentWidth = borderWidth + 2 * contentSideBorder + 1;
   def deltaContentHeight = contentTopOffset + borderWidth / 2 + controlSize;
   def contentWidth = bind realWidth - deltaContentWidth;
   def contentHeight = bind  realHeight - deltaContentHeight;
   var realWidth:Number;
   var realHeight:Number;

   def activeWindowEffect: Effect = DropShadow {
      offsetX: 6,
      offsetY: 6,
      color: Color.color(0.25,.25,.25)
   }
   def inactiveWindowEffect: Effect = null;

	var originalX: Number;
	var originalY: Number;
	var originalW: Number;
	var originalH: Number;
//	var rotateCenterX: Number;
//	var rotateCenterY: Number;
//	var initialRotation: Number;
   var maxDifX: Number;
   var maxDifY: Number;
	var sceneTopLeft: Point2D;

   var beingDragged = false;

	def animationDuration = 200ms;

//   var resizeHighLighted = false;
//   var rotateHighLighted = false;
//   var closeHighLighted = false;
//   var minimizeHighLighted = false;
//   var unminimizeHighLighted = false;

   public override var minimumHeight = 50 on replace{
      minimumHeight = Math.max(minimumHeight, contentTopOffset+2*controlSize);
   }
   public override var minimumWidth = 90 on replace{
      minimumWidth = Math.max(minimumWidth, 2 * borderWidth + 3 * controlSize);
   }

   var emptyWindow:EmptyWindow;
   var windowTitleBar:WindowTitleBar;
   var resizeElement:WindowResize;
   var rotateElement:WindowRotate;
   var closeElement: WindowClose;
   var minimizeElement: WindowMinimize;
   var contentElement: WindowContent;

   def drawerGroup:Group = Group{
      visible: bind not isClosed and not isMinimized
   };
   var topDrawer:TopDrawer;
   var rightDrawer:RightDrawer;
   var bottomDrawer:BottomDrawer;
   var leftDrawer:LeftDrawer;

   public override var topDrawerTool on replace {setTopDrawer()};
   public override var rightDrawerTool on replace {setRightDrawer()};
   public override var bottomDrawerTool on replace {setBottomDrawer()};
   public override var leftDrawerTool on replace {setLeftDrawer()};

   public var missionModelFX:MissionModelFX;

   var mainContentGroup:Group;

   public override var cache on replace {
//      mainContentGroup.cache = cache;
//      scyContent.cache = cache;
   }

   var changesListeners:WindowChangesListener[]; //WindowChangesListener are stored here. youse them to gain more control over ScyWindow events.

   postinit {
		if (isClosed){
			height = closedHeight;
		}
      closedBoundsWidth = minimumWidth+deltaContentWidth;
      closedBoundsHeight = closedHeight+deltaContentHeight;
      setTopDrawer();
      setRightDrawer();
      setBottomDrawer();
      setLeftDrawer();
      this.cache =true;
   }



   function scyContentChanged(){
      if (scyContent instanceof Parent){
         (scyContent as Parent).layout();
      }
      var newContentWidth = Container.getNodePrefWidth(scyContent);
      var newContentHeight = Container.getNodePrefHeight(scyContent);
      if (scyContent instanceof Resizable){
         if (desiredWidth>0 and desiredHeight>0){
            newContentWidth = desiredWidth - deltaContentWidth;
            newContentHeight = desiredHeight - deltaContentHeight;
         }
         else if (desiredContentWidth>0 and desiredContentHeight>0){
            newContentWidth = desiredContentWidth;
            newContentHeight = desiredContentHeight;
         }

         allowResize = true;
      }
      else{
         allowResize = false;
      }
//      println("scyContentChanged(): {newContentWidth}*{newContentHeight}");
//      println("preffered sizes    : {Container.getNodePrefWidth(scyContent)}*{Container.getNodePrefHeight(scyContent)}");
      FX.deferAction(function():Void{
            var limittedSize = limitSize(newContentWidth + deltaContentWidth,newContentHeight + deltaContentHeight);
      //      println("limitted to        : {limittedSize.x-deltaContentWidth}*{limittedSize.y-deltaContentHeight}");
            width = limittedSize.x;
            height = limittedSize.y;
         });
      scyToolsList.windowContentTool = scyContent;
   }

   function limitSize(w:Number, h:Number):Point2D{
      var limittedWidth = Math.max(w, minimumWidth);
      var limittedHeight = Math.max(h, minimumHeight);
      if (scyContent!=null){
//         println("limitSize(): Width:  {Container.getNodeMinWidth(scyContent)} - {Container.getNodePrefWidth(scyContent)} - {Container.getNodeMaxWidth(scyContent)}");
//         println("limitSize(): Height: {Container.getNodeMinHeight(scyContent)} - {Container.getNodePrefHeight(scyContent)} - {Container.getNodeMaxHeight(scyContent)}");
//         println("limitSize(): layout: {scyContent.layoutBounds}");
//         println("limitSize(): local:  {scyContent.boundsInLocal}");
       // this is check on content limits, subtract "border" sizes
         limittedWidth -= deltaContentWidth;
         limittedHeight -= deltaContentHeight;
         limittedWidth = Math.max(limittedWidth, Container.getNodeMinWidth(scyContent));
         limittedHeight = Math.max(limittedHeight, Container.getNodeMinHeight(scyContent));
         limittedWidth = Math.min(limittedWidth, Container.getNodeMaxWidth(scyContent));
         limittedHeight = Math.min(limittedHeight, Container.getNodeMaxHeight(scyContent));
         // this is check on content limits, add "border" sizes
         limittedWidth += deltaContentWidth;
         limittedHeight += deltaContentHeight;
      }
      else{
         // no content
         limittedHeight = closedHeight;
      }
      // now limit it to the scy desktop window size
      if (scene.width>0){
         limittedWidth = Math.min(limittedWidth, scene.width-deltaContentWidth);
         limittedHeight = Math.min(limittedHeight, scene.height-deltaContentHeight);
      }

//      println("limitSize({w},{h}):{limittedWidth},{limittedHeight} of {eloUri}, with: {scyContent}");
       return Point2D{
         x:limittedWidth;
         y:limittedHeight
      }
   }



   function activeStateChanged(){

      if (activated){
         scyToolsList.onGotFocus();
         //this.effect = activeWindowEffect;
         //display edges if on screen
         windowManager.scyDesktop.edgesManager.findLinks(this);
      }
      else{
         scyToolsList.onLostFocus();
         //this.effect = inactiveWindowEffect;
         //hide edges
      }
   }

   public override function canAcceptDrop(object:Object):Boolean{
      return scyToolsList.canAcceptDrop(object);
    }

   public override function acceptDrop(object:Object):Void{
      scyToolsList.acceptDrop(object);
   }

   public override function createTooltipNode(sourceNode:Node):Node{
      var tooltip:Node;
//      if (isClosed or isMinimized){
//         var titleTextWidth = windowTitleBar.titleTextWidth;
//         var titleDisplayWidth = windowTitleBar.titleDisplayWidth;
//         if (closeElement.visible){
//            titleDisplayWidth -= closeElement.layoutBounds.width;
//         }
//         //println("titleTextWidth:{titleTextWidth}, titleDisplayWidth:{titleDisplayWidth}, closeElement.visible:{closeElement.visible}");
//         if (titleTextWidth>titleDisplayWidth){
//            tooltip = ColoredTextTooltip{
//               content:title
//               color:windowColorScheme.mainColor
//            }
//         }
//      }

      return tooltip;
   }



   override function addChangesListener(wcl:WindowChangesListener) {
       insert wcl into changesListeners;
   }

   override function removeChangesListener(wcl:WindowChangesListener) {
       delete wcl from changesListeners;
   }

   /**
   *    method added to 'catch' the startResize event
   */
   function startResize(e: MouseEvent):Void {
        // TODO: check if listener notification takes too long -> thread
        for( wcl in changesListeners) {
            wcl.resizeStarted();
        }
        startDragging(e);
//        oldCacheValue = cache;
//        cache = false;
//        scyContent.cache = false;
//        contentElement.cache = false;
//        println("caching set from {oldCacheValue} to false");
   }

   /**
   *    method added to 'catch' the stopResize event
   */
   function stopResize(e: MouseEvent):Void {
        // TODO: check if listener notification takes too long -> thread
        for( wcl in changesListeners) {
            wcl.resizeFinished();
        }
        stopDragging(e);
//        cache = oldCacheValue;
//        scyContent.cache = oldCacheValue;
//        contentElement.cache = oldCacheValue;
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

    public override function open(): Void {
        checkScyContent();
        var openWidth = Container.getNodePrefWidth(scyContent);
        var openHeight = Container.getNodePrefHeight(scyContent);
        openWindow(openWidth + deltaContentWidth, openHeight+deltaContentHeight);
    }

    public override function openWindow(openWidth: Number, openHeight: Number): Void {
        checkScyContent();
        isClosed = false;
        var useSize = limitSize(openWidth, openHeight);
        width = useSize.x;
        height = useSize.y;
//        println("useSize: {useSize}, realy used: {width}*{height}");
        (scyToolsList.actionLoggerTool as ScyToolActionLogger).logToolOpened();
    }

   public override function setMinimize(state: Boolean):Void{
      if (isClosed){
         return;
      }
      if (isMinimized!=state){
         if (state){
            doMinimize();
         }
         else{
            doUnminimize();
         }
      }
   }


	function checkScyContent(){
		//println("checkScyContent: scyContent: {scyContent==null}, setScyContent: {setScyContent!=null}");
		if (scyContent == null and setScyContent != null){
			setScyContent(this)
		}
	}

	function getCloseTimeline():Timeline{
		return Timeline{
			keyFrames: [
				KeyFrame{
					time: animationDuration;
					values: [
						width => minimumWidth tween Interpolator.EASEBOTH,
						height => closedHeight tween Interpolator.EASEBOTH
					]
					action: function(){
                  if (closedAction!=null){
                     closedAction(this);
                  }
						isAnimating = false;
                  scyToolsList.onClosed();
					}
				}
			]
      }
	}

	function getMinimizeTimeline():Timeline{
		return Timeline{
			keyFrames: [
				KeyFrame{
					time: animationDuration;
					values: [
						width => minimumWidth tween Interpolator.EASEBOTH,
						height => closedHeight tween Interpolator.EASEBOTH
					]
					action: function(){
						isAnimating = false;
                  scyToolsList.onMinimized();
					}
				}
			]
      }
	}

	function getUnminimizeTimeline(endWidth:Number,endHeight:Number):Timeline{
		return Timeline{
			keyFrames: [
				KeyFrame{
					time: animationDuration;
					values: [
						width => endWidth tween Interpolator.EASEBOTH,
						height => endHeight tween Interpolator.EASEBOTH
					]
					action: function(){
						isAnimating = false;
                  scyToolsList.onUnMinimized();
 					}
				}
			]
      }
	}

	public override function close(){
      doClose();
	}

	function startDragging(e: MouseEvent):Void {
                for( wcl in changesListeners) {
                    wcl.draggingStarted();
                }


		activate();
		originalX = layoutX;
		originalY = layoutY;
		originalW = width;
		originalH = height;
      maxDifX = 0;
      maxDifY = 0;
		sceneTopLeft = localToScene(0,0);
      beingDragged = true;
//      contentElement.glassPaneBlocksMouse = true;
      MouseBlocker.startMouseBlocking();
	}

	function stopDragging(e: MouseEvent):Void {
            for( wcl in changesListeners) {
                        wcl.draggingFinished();
             }
//      contentElement.glassPaneBlocksMouse = false;
      beingDragged = false;
      MouseBlocker.stopMouseBlocking();
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
      var mouseEventInScene = MouseEventInScene{mouseEvent:e};
		var difX = mouseEventInScene.dragX;
		var difY = mouseEventInScene.dragY;
      maxDifX = Math.max(maxDifX, difX);
      maxDifY = Math.max(maxDifY, difY);
		//System.out.println("difX: {e.x}-{e.dragAnchorX} {difX}, difY: {e.y}-{e.dragAnchorY} {difY}");
		layoutX = originalX + difX;
		layoutY = originalY + difY;
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
      var mouseEventInScene = MouseEventInScene{mouseEvent:e};
		var angle = Math.toRadians(rotate);
		var difW = Math.cos(angle) * mouseEventInScene.dragX + Math.sin(angle) * mouseEventInScene.dragY;
		var difH = Math.cos(angle) * mouseEventInScene.dragY - Math.sin(angle) * mouseEventInScene.dragX;
		var difX: Number;
		var difY: Number;
		difX = (1 - Math.cos(angle)) * difW / 2;
		difY = (1 - Math.cos(angle)) * difH / 2;
		//difX = (1 - Math.cos(angle)) * e.dragX/2;// + (1 - Math.sin(angle)) * e.dragY/2;
		//difY = (1 - Math.cos(angle)) * e.dragY/2;// + (1 - Math.sin(angle)) * e.dragX/2;

      var newSize = limitSize( originalW + difW,originalH + difH);
      width = newSize.x;
      height = newSize.y;

//      width = originalW + difW;
//      height = originalH + difH;

		layoutX = originalX - difX;
		layoutY = originalY - difY;
//      for(edge in edges) {
//         edge.repaint();
//      }

//		var newSceneTopLeft = localToScene(0,0);
//		System.out.println("resized {title}, angle: {rotate}, difW: {difW}, difH: {difH}, difX: {difX}, difY: {difY}, dtlX:{sceneTopLeft.x-newSceneTopLeft.x}, dtlY:{sceneTopLeft.y-newSceneTopLeft.y}");

	}

	function doClose():Void{
      if (not scyToolsList.aboutToClose()){
         // abort close action
         return;
      }
		if (aboutToCloseAction != null){
			if (not aboutToCloseAction(this)){
				// close blocked
				return;
			}
		}
      isClosed = true;
      var closeTimeline = getCloseTimeline();

      if (closeTimeline != null){
         isAnimating = true;
         closeTimeline.play();
      }

      logger.debug("closed {title}");
	}

	function doMinimize(){
		if (minimizeAction != null){
			minimizeAction(this)
		}
		else {
			originalWidth = width;
			originalHeight = height;
			isMinimized = true;
			var minimizeTimeline = getMinimizeTimeline();
			isAnimating = true;
			minimizeTimeline.play();
		}
		logger.debug("minimized {title}");
	}

	function doUnminimize(){
		if (minimizeAction != null){
			minimizeAction(this)
		}
		else {
			isMinimized = false;
			var unminimizedTimeline = getUnminimizeTimeline(originalWidth,originalHeight);
			isAnimating = true;
			unminimizedTimeline.play();
		}
		logger.debug("unminimized {title}");
	}

   public function setMinimized(newMinimized:Boolean){
      if (newMinimized != isMinimized){
         if (newMinimized){
            doMinimize();
         }
         else {
            doUnminimize();
         }
      }
   }

   function handleDoubleClick(e:MouseEvent):Void{
      if (isClosed){
         open();
      }
      else if (isMinimized){
         setMinimized(not isMinimized);
      }
      else{
         if (eloUri==null){
            setMinimized(not isMinimized);
         }
         else{
            windowControl.makeMainScyWindow(eloUri);
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
      if (windowManager != null){
			windowManager.activateScyWindow(this);
		}
   }

   function setTopDrawer(){
      if (drawerGroup==null){
         // initialisation not yet ready, a call from postinit will be done again
         return;
      }
      if (topDrawer!=null){
         delete topDrawer from drawerGroup.content;
         topDrawer = null;
      }
      if (topDrawerTool!=null){
         topDrawer = TopDrawer{
            windowColorScheme:windowColorScheme
//            color:bind drawerColor;
//            highliteColor:controlColor;
//            closedSize:bind width-2*drawerCornerOffset;
//            closedStrokeWidth:controlStrokeWidth;
            content:topDrawerTool;
            activated:bind activated;
            activate: activate;
            layoutX:drawerCornerOffset;
            layoutY:0;
            width:bind realWidth-2*drawerCornerOffset
         }
         insert topDrawer into drawerGroup.content;
      }
      scyToolsList.topDrawerTool = topDrawerTool;
   }

   function setRightDrawer(){
      if (drawerGroup==null){
         // initialisation not yet ready, a call from postinit will be done again
         return;
      }
      if (rightDrawer!=null){
         delete rightDrawer from drawerGroup.content;
         rightDrawer = null;
      }
      if (rightDrawerTool!=null){
         rightDrawer = RightDrawer{
            windowColorScheme:windowColorScheme
//            color:bind drawerColor;
//            highliteColor:controlColor;
//            closedStrokeWidth:controlStrokeWidth;
//            closedSize:bind height-2*drawerCornerOffset;
            content:rightDrawerTool;
            activated:bind activated;
            activate: activate;
            layoutX:bind realWidth;
            layoutY:drawerCornerOffset;
            height:bind realHeight-2*drawerCornerOffset
         }
         insert rightDrawer into drawerGroup.content;
      }
      scyToolsList.rightDrawerTool = rightDrawerTool;
   }

   function setBottomDrawer(){
      if (drawerGroup==null){
         // initialisation not yet ready, a call from postinit will be done again
         return;
      }
      if (bottomDrawer!=null){
         delete bottomDrawer from drawerGroup.content;
         bottomDrawer = null;
      }
      if (bottomDrawerTool!=null){
         //println("new BottomDrawer with color {drawerColor}");
         bottomDrawer = BottomDrawer{
            windowColorScheme:windowColorScheme
//            color:bind drawerColor;
//            highliteColor:controlColor;
//            closedSize:bind width-2*drawerCornerOffset;
//            closedStrokeWidth:controlStrokeWidth;
            content:bottomDrawerTool;
            activated:bind activated;
            activate: activate;
            layoutX:drawerCornerOffset;
            layoutY:bind realHeight;
            width:bind realWidth-2*drawerCornerOffset
         }
         insert bottomDrawer into drawerGroup.content;
      }
      scyToolsList.bottomDrawerTool = bottomDrawerTool;
   }

   function setLeftDrawer(){
      if (drawerGroup==null){
         // initialisation not yet ready, a call from postinit will be done again
         return;
      }
      if (leftDrawer!=null){
         delete leftDrawer from drawerGroup.content;
         leftDrawer = null;
      }
      if (leftDrawerTool!=null){
         leftDrawer = LeftDrawer{
            windowColorScheme:windowColorScheme
//            color:bind drawerColor;
//            highliteColor:controlColor;
//            closedStrokeWidth:controlStrokeWidth;
//            closedSize:bind height-2*drawerCornerOffset;
            content:leftDrawerTool;
            activated:bind activated;
            activate: activate;
            layoutX:0;
            layoutY:drawerCornerOffset;
            height:bind realHeight-2*drawerCornerOffset
         }
         insert leftDrawer into drawerGroup.content;
      }
      scyToolsList.leftDrawerTool = leftDrawerTool;
   }

   public override function openDrawer(which:String):Void{
      if ("top".equalsIgnoreCase(which)){
         topDrawer.opened = true;
      }
      else if ("right".equalsIgnoreCase(which)){
         rightDrawer.opened = true;
      }
      else if ("bottom".equalsIgnoreCase(which)){
         bottomDrawer.opened = true;
      }
      else if ("left".equalsIgnoreCase(which)){
         leftDrawer.opened = true;
      }
   }

   function startDragIcon(e:MouseEvent):Void{
      if (eloUri!=null){
         def dragIcon = windowControl.windowStyler.getScyEloIcon(eloUri);
         def metadata = tbi.getRepository().retrieveMetadata(eloUri);
         dragAndDropManager.startDrag(dragIcon, metadata, this, e);
      }
   }

	public override function create(): Node {
      if (isClosed){
         width = minimumWidth;
         height = closedHeight;
      }

		blocksMouse = true;

      emptyWindow = EmptyWindow{
         width: bind realWidth;
         height:bind realHeight;
         controlSize:cornerRadius;
         borderWidth:borderWidth;
         windowColorScheme:windowColorScheme
      }

      contentElement = WindowContent{
         width:bind contentWidth;
         height:bind contentHeight;
         windowColorScheme:windowColorScheme
         content:bind scyContent;
         activated:bind activated;
         activate: activate;
         layoutX: borderWidth / 2 + 1 + contentSideBorder;
         layoutY: contentTopOffset;
      }

//      drawerGroup = Group{
//         content:[
//            Rectangle {
//               x: 0, y: 0
//               width: 10, height: 10
//               fill: Color.BLACK
//            }
//         ]
//      }
//
      windowTitleBar = WindowTitleBar{
         width:bind realWidth - titleBarLeftOffset-titleBarRightOffset
//         iconSize:iconSize;
//         iconGap:iconGap;
         closeBoxWidth:bind if (closeElement.visible) closeBoxSize+2*borderWidth+2 else 0.0;
         iconSize:iconSize
         title:bind title;
         eloIcon:bind eloIcon;
         activated:bind activated
         beingDragged:bind beingDragged
         startDragIcon:startDragIcon
         windowColorScheme:windowColorScheme
         layoutX:titleBarLeftOffset;
         layoutY:titleBarTopOffset;
      }

      resizeElement = WindowResize{
         visible: bind allowResize or isClosed;
         size:controlSize;
         borderWidth:borderWidth;
         separatorLength:separatorLength
         windowColorScheme:windowColorScheme
         activate: activate;
         startResize:startResize;
         doResize:doResize;
         stopResize:stopResize;
         layoutX: bind realWidth
         layoutY: bind realHeight
      }

      rotateElement = WindowRotate{
         visible: bind allowRotate;
         size:controlSize;
         borderWidth:borderWidth;
         separatorLength:separatorLength
         windowColorScheme:windowColorScheme
         activate: activate;
         rotateWindow:this;
         layoutX: 0;
         layoutY: bind realHeight;
      }

      closeElement = WindowClose{
         visible: bind allowClose and not isClosed;
         size:closeBoxSize;
         windowColorScheme:windowColorScheme
         activate: activate;
         activated:bind activated
         closeAction:doClose;
         layoutX: bind realWidth - titleBarRightOffset -1.0*borderWidth - closeBoxSize - 1;
         layoutY: borderWidth+closeBoxSize/2;
      }

      minimizeElement = WindowMinimize{
         visible: bind allowMinimize and not isClosed;
         size:controlSize;
         separatorLength:separatorLength
         windowColorScheme:windowColorScheme
         activate: activate;
         minimizeAction:doMinimize;
         unminimizeAction:doUnminimize;
         minimized: bind isMinimized;
         layoutX: bind realWidth / 2;
         layoutY: bind height;
      }

      // show a filled rect as content for test purposes
//      scyContent = Rectangle {
//         x: -100, y: -100
//         width: 1000, height: 1000
//         fill: Color.color(1,.25,.25,.75)
//      }

		return mainContentGroup = Group {
         cursor: Cursor.MOVE;
         cache:true;
			content: [
            emptyWindow,
            contentElement,
            drawerGroup,
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
         onMouseClicked: function( e: MouseEvent ):Void {
            if (e.clickCount==2){
               handleDoubleClick(e);
            }
         }
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
      windowColorScheme:WindowColorScheme.getWindowColorScheme(ScyColors.green)
      height: 150;
		//      scyContent:newGroup
      allowClose: false;
      allowResize: false;
      allowRotate: false;
      allowMinimize: false;
      translateX: 200;
      translateY: 40;
      topDrawerTool:Button {
            text: "Top"
            action: function() {
            }
         }
      rightDrawerTool:Button {
            text: "Right"
            action: function() {
            }
         }
      bottomDrawerTool:Button {
            text: "Bottom"
            action: function() {
           }
         }
      leftDrawerTool:Button {
            text: "Left"
            action: function() {
            }
         }
   };
   fixedScyWindow.openWindow(100, 100);
//   scyDesktop.addScyWindow(fixedScyWindow);

   var closedScyWindow = StandardScyWindow{
      title: "Closed and very closed"
      eloType: "M"
      windowColorScheme:WindowColorScheme.getWindowColorScheme(ScyColors.darkGray)
      height: 27;
      isClosed: true
      allowClose: true;
      allowResize: true;
      allowRotate: true;
      allowMinimize: true;
      translateX: 20;
      translateY: 200;
      topDrawerTool:Button {
            text: "Top"
            action: function() {
            }
         }
      rightDrawerTool:Button {
            text: "Right"
            action: function() {
            }
         }
      bottomDrawerTool:Button {
            text: "Bottom"
            action: function() {
           }
         }
      leftDrawerTool:Button {
            text: "Left"
            action: function() {
            }
         }
   };
   closedScyWindow.openWindow(100, 150);
//   scyDesktop.addScyWindow(closedScyWindow);

   var eloWindow = StandardScyWindow{
      title: bind "elo window";
      windowColorScheme:WindowColorScheme.getWindowColorScheme(ScyColors.pink)
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
//      topDrawerTool:Button {
//            text: "Top"
//            action: function() {
//            }
//         }
//      rightDrawerTool:Button {
//            text: "Right"
//            action: function() {
//            }
//         }
//      bottomDrawerTool:Button {
//            text: "Bottom"
//            action: function() {
//           }
//         }
//      leftDrawerTool:Button {
//            text: "Left"
//            action: function() {
//            }
//         }
   }
//   scyDesktop.addScyWindow(eloWindow);

   var stage:Stage;
   FX.deferAction(function(){MouseBlocker.initMouseBlocker(stage);});

   stage = Stage {
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
