/*
 * Drawer.fx
 *
 * Created on 9-sep-2009, 11:18:17
 */
package eu.scy.client.desktop.scydesktop.scywindows.window;

import javafx.scene.CustomNode;
import javafx.scene.Node;
import javafx.scene.shape.Rectangle;
import eu.scy.client.desktop.scydesktop.scywindows.window.WindowContent;
import eu.scy.client.desktop.desktoputils.art.WindowColorScheme;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import eu.scy.client.desktop.desktoputils.art.AnimationTiming;
import javafx.animation.Interpolator;
import javafx.scene.paint.Color;
import eu.scy.client.desktop.scydesktop.tooltips.TooltipManager;
import eu.scy.client.common.scyi18n.ResourceBundleWrapper;
import eu.scy.client.desktop.scydesktop.tools.ScyTool;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Resizable;
import javafx.util.Math;
import eu.scy.client.desktop.scydesktop.scywindows.window.MouseBlocker;
import eu.scy.client.desktop.scydesktop.scywindows.window.OpenDrawerControl;
import eu.scy.client.desktop.scydesktop.scywindows.window.WindowResize;
import eu.scy.client.desktop.scydesktop.tools.ScyToolGetter;
import eu.scy.client.desktop.scydesktop.tooltips.BubbleManager;
import eu.scy.client.desktop.scydesktop.tooltips.BubbleLayer;

/**
 * @author sikkenj
 */
// place your code here
public abstract class Drawer extends CustomNode {

   public var windowColorScheme: WindowColorScheme;
   public var tooltipManager: TooltipManager;
   public var bubbleManager: BubbleManager;
   public var borderSize = 2.0;
   public var closedSize = 20.0;
   public var content: Node;
   public var activated = false; // TODO, make only changeable from (sub) package
   public var activate: function(): Void;
   public var handleNumber = 0;
   public var otherDrawers:Drawer[];
   public var controlLetter = "";
   var contentLabel = ##"drawer";
   def sideContentBorder = 5.0;
   def topContentBorder = 5.0;
   def bottomContentBorder = 5.0;
   protected def handleOffset = topContentBorder + 2 + handleNumber*(closedSize+3*borderSize+0);
   protected var horizontal = true;
   protected def resizeControlSize = 10.0;
   protected def closeControlSize = 10.0;
   protected var opened = false on replace {
         // only do the animated stuff, when the creation is completed
         if (drawerCreated) {
            openCloseDrawer();
         } else {
            if (opened) {
               contentContainer.content = contentGroup;
            } else {
               delete  contentContainer.content;
            }
            positionControlElements();
         }
      };
   protected var width = 50.0 on replace {
         sizeChanged()
      };
   protected var height = 50.0 on replace {
         sizeChanged()
      };
   protected var absoluteMinimumWidth = closeControlSize + resizeControlSize;
   protected var absoluteMinimumHeight = closeControlSize + resizeControlSize;
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
   var resizeAllowed = true;
   def heightOverhead = topContentBorder + bottomContentBorder + borderSize + 1;
   def widthOverhead = 2 * sideContentBorder + borderSize + 1;
   protected def clipSize = 1000000.0;
   protected def clipRect = Rectangle {
         x: 0
         y: 0
         width: clipSize
         height: clipSize
      }
   def drawerGroup = Group {
      }
   def contentContainer = Group {
      }
   def contentGroup = Group {
      }
   var clipRectColor = Color.TRANSPARENT;
   var drawerCreated = false;
   def resourceBundleWrapper = new ResourceBundleWrapper(this);
   def openLabel = ##"open";
   def closeLabel = ##"close";

   function getLanguageValue(key: String, default: String): String{
      def value = resourceBundleWrapper.getString(key);
      if (value!=key){
         return value;
      }
      return default
   }

   function sizeChanged() {
      adjustClipRect();
      positionControlElements();
   //      println("{this.getClass()} size changed to {width}*{height}");
   //      println("layout: ({layoutX}, {layoutY})");
   //      println("openControl.layout: ({openControl.layoutX}, {openControl.layoutY}), openControl: {openControl}, {openControl.boundsInParent}");
   }

   public override function create(): Node {
      createElements();
      positionControlElements();
      adjustClipRect();
      contentGroup.content = [
            border,
            contentElement,
            resizeControl
         ];
      if (opened) {
         contentContainer.content = contentGroup;
      }
      drawerGroup.content = [
            contentContainer,
            openCloseControl
         ];
      drawerCreated = true;
      Group {
         clip: clipRect
         content: [
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

   function createElements(): Void {
      var scyTool:ScyTool = null;
      if (content instanceof ScyToolGetter){
         scyTool = (content as ScyToolGetter).getScyTool();
      }
      else if (content instanceof ScyTool){
         scyTool = (content as ScyTool)
      }
      if (scyTool!=null){
         def drawerUIIndicator = scyTool.getDrawerUIIndicator();
         if (drawerUIIndicator!=null){
            controlLetter = getLanguageValue("drawer.{drawerUIIndicator.toString().toLowerCase()}.letter",controlLetter);
            contentLabel = getLanguageValue("drawer.{drawerUIIndicator.toString().toLowerCase()}.label",contentLabel);
         }
//         println("content: {content}, drawerUIIndicator:{drawerUIIndicator}, controlLetter: {controlLetter}, contentLabel: {contentLabel}");
         def bubbleKey = scyTool.getBubbleKey();
         if (bubbleKey!=null){
            bubbleManager.createBubble(openCloseControl,BubbleLayer.DESKTOP,bubbleKey, windowColorScheme);
         }
      }
//      else{
//         println("content is not a ScyTool: {content}");
//      }

      openCloseControl = OpenDrawerControl {
            tooltipManager: tooltipManager
            windowColorScheme: windowColorScheme
            controlLetter:controlLetter
            size: closedSize
            onMouseClicked: function(e: MouseEvent): Void {
               opened = not opened;
            }
         }
      setOpenCloseDrawerTooltip();
      if (content instanceof Parent) {
         (content as Parent).layout();
      }
      if (content instanceof Resizable) {
         var resizableContent = content as Resizable;
         //println("preferred size: {resizableContent.getPrefWidth(width)}*{resizableContent.getPrefHeight(height)}");
         if (not horizontal) {
            width = Math.max(resizableContent.getPrefWidth(width) + widthOverhead + 1, absoluteMinimumWidth);
         }
         if (horizontal) {
            height = Math.max(resizableContent.getPrefHeight(height) + heightOverhead + 1, absoluteMinimumHeight);
         }
      } else if (content != null) {
         if (not horizontal) {
            width = content.boundsInLocal.width + widthOverhead;
         }
         if (horizontal) {
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
            windowColorScheme: windowColorScheme
            width: bind width - widthOverhead - 1
            height: bind height - heightOverhead - 1
            content: bind content;
            activated: bind activated;
            activate: activate
            layoutX: sideContentBorder + borderSize / 2 + 1;
            layoutY: topContentBorder + borderSize / 2 + 1;
         }
      if (resizeAllowed) {
         resizeControl = WindowResize {
               size: resizeControlSize;
               windowColorScheme: windowColorScheme
               activate: activate
               startResize: startResize;
               doResize: doResize;
               stopResize: stopResize;
            }
      }
   }

   function setOpenCloseDrawerTooltip():Void{
      if (opened){
         openCloseControl.tooltip = "{closeLabel} {contentLabel}";
      }
      else{
         openCloseControl.tooltip = "{openLabel} {contentLabel}";
      }
   }

   function openCloseDrawer(): Void {
      setOpenCloseDrawerTooltip();
      var openFactor = 0.0;
      var animationInterpolation = Interpolator.EASEBOTH;
      if (opened) {
         this.toBack();
         for (otherDrawer in otherDrawers){
            if (otherDrawer!=this){
               otherDrawer.opened = false;
            }
         }

         contentContainer.content = contentGroup;
         // after the first show of the content, fx content resizes to a "smaller content"
         // give it back the original size
         // we seems to have to wait some time, before doing it
         Timeline {
            repeatCount: 1
            keyFrames: [
               KeyFrame {
                  time: 20ms
                  action:function():Void{
                     contentElement.resizeTheContent();
                  }
               }
            ]
         }.play();
         openFactor = 1.0;
         positionControlElements();
         Timeline {
            repeatCount: 1
            keyFrames: [
               KeyFrame {
                  time: 0s
                  canSkip: true
                  values: [
                     drawerGroup.translateX => closedXFactor * width,
                     drawerGroup.translateY => closedYFactor * height
                  ]
               }
               KeyFrame {
                  time: AnimationTiming.drawerOpenCloseDuration
                  canSkip: true
                  values: [
                     drawerGroup.translateX => openFactor * openedXFactor * width tween animationInterpolation,
                     drawerGroup.translateY => openFactor * openedYFactor * height tween animationInterpolation
                  ]
               }
            ]
         }.play();
      }
      else {
         openFactor = 1;
         Timeline {
            repeatCount: 1
            keyFrames: [
               KeyFrame {
                  time: 0s
                  canSkip: true
                  values: [
                     drawerGroup.translateX => openFactor * openedXFactor * width,
                     drawerGroup.translateY => openFactor * openedYFactor * height
                  ]
               }
               KeyFrame {
                  time: AnimationTiming.drawerOpenCloseDuration
                  canSkip: true
                  values: [
                     drawerGroup.translateX => closedXFactor * width tween animationInterpolation,
                     drawerGroup.translateY => closedYFactor * height tween animationInterpolation
                  ]
                  action: function(): Void {
                     delete  contentContainer.content;
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
   }

   function startResize(e: MouseEvent): Void {
      originalWidth = width;
      originalHeight = height;
      MouseBlocker.startMouseBlocking();
   }

   function doResize(e: MouseEvent): Void {
      var desiredWidth = originalWidth + resizeXFactor * e.dragX;
      var desiredHeight = originalHeight + resizeYFactor * e.dragY;
      desiredWidth = Math.max(desiredWidth, absoluteMinimumWidth);
      desiredHeight = Math.max(desiredHeight, absoluteMinimumHeight);
      if (content instanceof Resizable) {
         var resizableContent = content as Resizable;
//         println("drawer content width limits {resizableContent.getMinWidth()} - {resizableContent.getMaxWidth()}");
         desiredWidth = Math.max(desiredWidth, resizableContent.getMinWidth());
         desiredHeight = Math.max(desiredHeight, resizableContent.getMinHeight());
         desiredWidth = Math.min(desiredWidth, resizableContent.getMaxWidth());
         desiredHeight = Math.min(desiredHeight, resizableContent.getMaxHeight());
      }
      if (horizontal) {
         height = desiredHeight;
      } else {
         width = desiredWidth;
      }
   }

   function stopResize(e: MouseEvent): Void {
      MouseBlocker.stopMouseBlocking();
   }

}

