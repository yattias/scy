/*
 * Drawer.fx
 *
 * Created on 9-sep-2009, 11:18:17
 */
package eu.scy.client.desktop.scydesktop.scywindows.window;

import javafx.scene.CustomNode;
import javafx.scene.Node;



import javafx.scene.shape.Rectangle;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Resizable;
import javafx.util.Math;
import eu.scy.client.desktop.scydesktop.scywindows.window.MouseBlocker;
import eu.scy.client.desktop.scydesktop.scywindows.window.WindowContent;
import eu.scy.client.desktop.scydesktop.scywindows.window.WindowResize;
import eu.scy.client.desktop.scydesktop.art.WindowColorScheme;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import eu.scy.client.desktop.scydesktop.art.AnimationTiming;
import javafx.animation.Interpolator;
import javafx.scene.paint.Color;

/**
 * @author sikkenj
 */
// place your code here

public abstract class Drawer extends CustomNode {

   public var windowColorScheme:WindowColorScheme;
   public var borderSize = 2.0;
   public var closedSize = 20.0;
   public var content: Node;
   public var activated = false; // TODO, make only changeable from (sub) package
   public var activate: function():Void;

   def sideContentBorder = 5.0;
   def topContentBorder = 5.0;
   def bottomContentBorder = 5.0;
   protected def handleOffset = topContentBorder+2;
   protected var horizontal = true;
   protected def resizeControlSize = 10.0;
   protected def closeControlSize = 10.0;
   protected var opened = false;
   protected var width = 50.0 on replace {
              sizeChanged()
           };
   protected var height = 50.0 on replace {
              sizeChanged()
           };
   protected var absoluteMinimumWidth = closeControlSize+resizeControlSize;
   protected var absoluteMinimumHeight = closeControlSize+resizeControlSize;
   protected var resizeXFactor = 1.0;
   protected var resizeYFactor = 1.0;
   protected var openedXFactor = 0.0;
   protected var openedYFactor = 0.0;
   protected var closedXFactor = 0.0;
   protected var closedYFactor = 0.0;
   protected var border: Rectangle;
   protected var contentElement: WindowContent;
   protected var openCloseControl: OpenDrawerControl;
   protected var resizeControl: WindowResize;
   protected var horizontalSizeExternal = false;
   protected var verticalSizeExternal = false;
   var originalWidth: Number;
   var originalHeight: Number;
   var resizeAllowed = false;

   def heightOverhead = topContentBorder + bottomContentBorder + borderSize+1;
   def widthOverhead = 2 * sideContentBorder + borderSize+1;
   protected def clipSize = 1000000.0;
   protected def clipRect = Rectangle{
      x:0
      y:0
      width: clipSize
      height: clipSize
   }
   def drawerGroup = Group{
   }
   def contentContainer = Group{
   }
   def contentGroup = Group{
   }
   var clipRectColor = Color.TRANSPARENT;


   function sizeChanged() {
      positionControlElements();
//      println("layout: ({layoutX}, {layoutY})");
//      println("openControl.layout: ({openControl.layoutX}, {openControl.layoutY}), openControl: {openControl}, {openControl.boundsInParent}");
   }

   public override function create(): Node {
      createElements();
      adjustClipRect();
      contentGroup.content = [
         border,
         contentElement,
         resizeControl
      ];
      if (opened){
         contentContainer.content = contentGroup;
      }
      drawerGroup.content=[
         contentContainer,
         openCloseControl
      ];
      Group{
         clip:clipRect
         content:[
//            Line{
//               startX: -100
//               startY:0
//               endX:100
//               endY:0
//               fill:Color.BLACK
//            }
//            Line{
//               startX: 0
//               startY:-100
//               endX:0
//               endY:100
//               fill:Color.BLACK
//            }

            drawerGroup
         ]
      }
   }

   function createElements():Void{
      openCloseControl = OpenDrawerControl{
         windowColorScheme:windowColorScheme
         size:closedSize
         onMouseClicked: function (e: MouseEvent): Void {
            openCloseDrawer();
         }
      }
      if (content instanceof Parent){
         (content as Parent).layout();
      }
      if (content instanceof Resizable) {
         var resizableContent = content as Resizable;
         println("preferred size: {resizableContent.getPrefWidth(width)}*{resizableContent.getPrefHeight(height)}");
         if (not horizontal){
            width = Math.max(resizableContent.getPrefWidth(width)+widthOverhead +1, absoluteMinimumWidth);
         }
         if (horizontal){
            height = Math.max(resizableContent.getPrefHeight(height)+heightOverhead + 1, absoluteMinimumHeight);
         }
      } else if (content != null) {
         if (not horizontal){
            width = content.boundsInLocal.width + widthOverhead;
         }
         if (horizontal){
            height = content.boundsInLocal.height + heightOverhead;
         }
         resizeAllowed = false;
      }
      border = Rectangle {
         x: 0, y: 0
         width: bind width, height: bind height
         fill: bind windowColorScheme.backgroundColor
         strokeWidth: borderSize
         stroke: bind windowColorScheme.mainColor;
      }
      contentElement = WindowContent {
         windowColorScheme:windowColorScheme
         width: bind width - widthOverhead - 1
         height: bind height - heightOverhead - 1
         content: bind content;
         activated: bind activated;
         activate:activate
         layoutX: sideContentBorder + borderSize / 2 + 1;
         layoutY: topContentBorder + borderSize / 2 + 1;
      }
      if (resizeAllowed) {
         resizeControl = WindowResize {
            size: resizeControlSize;
            windowColorScheme:windowColorScheme
            activate:activate
            startResize: startResize;
            doResize: doResize;
            stopResize: stopResize;
         }
      }
   }

   function openCloseDrawer():Void{
      var openFactor = 0.0;
      opened = not opened;
      var animationInterpolation = Interpolator.EASEBOTH;
      if (opened){
         contentContainer.content = contentGroup;
         openFactor = 1.0;
         positionControlElements();
         Timeline {
            repeatCount: 1
            keyFrames : [
               KeyFrame {
                  time : 0s
                  canSkip : true
                  values: [
                     drawerGroup.translateX => closedXFactor*width,
                     drawerGroup.translateY => closedYFactor*height
                  ]
               }
               KeyFrame {
                  time : AnimationTiming.drawerOpenCloseDuration
                  canSkip : true
                  values: [
                     drawerGroup.translateX => openFactor*openedXFactor*width tween animationInterpolation,
                     drawerGroup.translateY => openFactor*openedYFactor*height tween animationInterpolation
                  ]
               }
            ]
         }.play();
      }
      else{
//         delete contentContainer.content;
//         positionControlElements();
         openFactor = 1;
         Timeline {
            repeatCount: 1
            keyFrames : [
               KeyFrame {
                  time : 0s
                  canSkip : true
                  values: [
                     drawerGroup.translateX => openFactor*openedXFactor*width,
                     drawerGroup.translateY => openFactor*openedYFactor*height
                  ]
               }
               KeyFrame {
                  time : AnimationTiming.drawerOpenCloseDuration
                  canSkip : true
                  values: [
                     drawerGroup.translateX => closedXFactor*width tween animationInterpolation,
                     drawerGroup.translateY => closedYFactor*height tween animationInterpolation
                  ]
                  action:function():Void{
                     delete contentContainer.content;
                     positionControlElements();
                     drawerGroup.translateX = 0;
                     drawerGroup.translateY = 0;
                  }
               }
            ]
         }.play();
      }
   }

   protected function adjustClipRect(): Void {

   }

   protected function positionControlElements(): Void {
      // this is somehow not (always??) called for the collaboration tools in the drawers
      // as the closebox has now a fixed position, position is now handled by the binds in the creator.
//      closeControl.layoutX = width-closeControlSize-sideContentBorder;
//      closeControl.layoutY = topContentBorder/2-closeControlSize/2 + borderSize/2;

   }

   function startResize(e: MouseEvent): Void {
      originalWidth = width;
      originalHeight = height;
//      contentElement.glassPaneBlocksMouse = true;
      MouseBlocker.startMouseBlocking();
   }

   function doResize(e: MouseEvent): Void {
      var desiredWidth = originalWidth + resizeXFactor * e.dragX;
      var desiredHeight = originalHeight + resizeYFactor * e.dragY;
      desiredWidth = Math.max(desiredWidth, absoluteMinimumWidth);
      desiredHeight = Math.max(desiredHeight, absoluteMinimumHeight);
      if (content instanceof Resizable) {
         var resizableContent = content as Resizable;
         desiredWidth = Math.max(desiredWidth, resizableContent.getMinWidth());
         desiredHeight = Math.max(desiredHeight, resizableContent.getMinHeight());
         desiredWidth = Math.min(desiredWidth, resizableContent.getMaxWidth());
         desiredHeight = Math.min(desiredHeight, resizableContent.getMaxHeight());
      }

      width = desiredWidth;
      height = desiredHeight;
   }

   function stopResize(e: MouseEvent): Void {
//      contentElement.glassPaneBlocksMouse = false;
      MouseBlocker.stopMouseBlocking();
   }
}

