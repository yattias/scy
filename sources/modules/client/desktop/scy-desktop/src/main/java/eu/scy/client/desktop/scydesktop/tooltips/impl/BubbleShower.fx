/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tooltips.impl;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.CacheHint;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.util.Math;
import eu.scy.client.desktop.desktoputils.art.AnimationTiming;
import eu.scy.client.desktop.desktoputils.art.ScyColors;
import eu.scy.client.desktop.desktoputils.art.WindowColorScheme;

/**
 * @author SikkenJ
 */
class LayoutResult {

   public-init var point: Point2D;
   public-init var arrowPosition: ArrowPosition;
}

public class BubbleShower {

   public-init var simpleBubbleManager: SimpleBubbleManager;
   public-init var bubble: AbstractBubble;
   public-init var bubbleContent: Node;
   public-init var windowColorScheme = WindowColorScheme.getWindowColorScheme(ScyColors.darkGray);
   public-init var sourceNode: Node;
   public-init var startAppearingTime = AnimationTiming.startAppearingTime;
   public-init var fullAppearingTime = AnimationTiming.fullAppearingTime;
   public-init var startDisappearingTime = AnimationTiming.startDisappearingTime;
   public-init var fullDisappearingTime = AnimationTiming.fullDisappearingTime;
   public-init var tooltipGroup: Group;
   def finalOpacity = 0.95;
   def minimalDisplayTimeForDeleteBubble = fullAppearingTime+2s;
   def bubbleNode:NodeBubble = NodeBubble {
              content: bubbleContent
              windowColorScheme: windowColorScheme
              clickedInBubble: clickedInBubble
           }
   def timeline = Timeline {
              keyFrames: [
                 KeyFrame {
                    time: 0s
                    values: bubbleNode.opacity => 0.0;
                 }
                 KeyFrame {
                    time: startAppearingTime
                    values: bubbleNode.opacity => 0.0;
                    action: function() {
                       insert bubbleNode into tooltipGroup.content;
                       FX.deferAction(startPositioning);
                    }
                 }
                 KeyFrame {
                    time: fullAppearingTime
                    values: bubbleNode.opacity => finalOpacity tween Interpolator.EASEBOTH;
                 }
                 KeyFrame {
                    time: startDisappearingTime
                    values: bubbleNode.opacity => finalOpacity tween Interpolator.EASEBOTH;
                 }
                 KeyFrame {
                    time: fullDisappearingTime
                    values: bubbleNode.opacity => 0.0 tween Interpolator.EASEBOTH;
                    action: function() {
                       delete bubbleNode from tooltipGroup.content;
                       stopPositioning();
                    }
                 }
              ]
           }
   var showingBubble = false;
   var shouldDeleteBubble = true;
   def sourceMovedDetectionTimeLine = Timeline {
              repeatCount: Timeline.INDEFINITE
              keyFrames: [
                 KeyFrame {
                    time: 1ms
                    canSkip: true
                    action: checkIfSourceMoved;
                 }
              ];
           }
   var sourceNodeSceneBounds: Bounds;

   init {
      bubbleNode.cache = true;
      bubbleNode.cacheHint = CacheHint.SPEED;
      timeline.play();
   }

   public function userDidSomething():Void{
      shouldDeleteBubble = timeline.time>=minimalDisplayTimeForDeleteBubble;
      remove();
   }

   function remove(): Void {
      if (timeline.time < startAppearingTime) {
         timeline.stop();
      } else if (timeline.time < fullAppearingTime) {
         def delta = (timeline.time - startAppearingTime) / (fullAppearingTime - startAppearingTime);
         timeline.time = startDisappearingTime + delta * (fullDisappearingTime - startDisappearingTime)
      } else if (timeline.time < startDisappearingTime) {
         timeline.time = startDisappearingTime
      }
   }

   function clickedInBubble():Void{
      remove();
   }
//   var checkCounter = 0;

   function checkIfSourceMoved(): Void {
//      println("checkIfSourceMoved - {checkCounter++}");
      if (showingBubble) {
         def currentSourceSceneBounds = sourceNode.localToScene(sourceNode.layoutBounds);
         if (currentSourceSceneBounds != sourceNodeSceneBounds) {
            sourceNodeMoved();
         }
      }
   }

   function sourceNodeMoved(): Void {
//      println("sourceNodeMoved: {showingBubble}");
      if (showingBubble) {
         positionTooltip();
         sourceNodeSceneBounds = sourceNode.localToScene(sourceNode.layoutBounds);
      }
   }

   function startPositioning(): Void {
      showingBubble = true;
      sourceNodeMoved();
      sourceMovedDetectionTimeLine.play();
   }

   function stopPositioning(): Void {
      showingBubble = false;
      simpleBubbleManager.bubbleRemoved(bubble,shouldDeleteBubble);
      sourceMovedDetectionTimeLine.stop();
   }

   function positionTooltip(): Void {
      var sceneBounds = BoundingBox {
                 width: sourceNode.scene.width;
                 height: sourceNode.scene.height
              }

      var positionFunctions = [calculateBottomRightLayout, calculateBottomLeftLayout, calculateTopLeftLayout, calculateTopRightLayout];

      var outsideArea = Number.MAX_VALUE;
      var toolTipLayout: LayoutResult;
      //      println("sceneBounds: {sceneBounds}");
      for (positionFunction in positionFunctions) {
         var newTooltipLayout = positionFunction();
         var newOutsideArea = calculateTooltipAreaOutsideScene(bubbleNode, newTooltipLayout.point, sceneBounds);
         //         println("currentTooltip local bounds: {currentTooltip.localToScene(currentTooltip.layoutBounds)} -> {newOutsideArea}");
         if (newOutsideArea < outsideArea) {
            outsideArea = newOutsideArea;
            toolTipLayout = newTooltipLayout;
         }
      }

      bubbleNode.layoutX = toolTipLayout.point.x;
      bubbleNode.layoutY = toolTipLayout.point.y;
      bubbleNode.arrowPosition = toolTipLayout.arrowPosition;
      bubbleNode.arrowPoint = sourceNode.localToScene(Point2D {
                 x: sourceNode.layoutBounds.minX + sourceNode.layoutBounds.width / 2
                 y: sourceNode.layoutBounds.minY + sourceNode.layoutBounds.height / 2
              });
      if (outsideArea > 0) {
         moveTooltipInside(sceneBounds);
      }
      bubbleNode.updateShape();
   }

   function calculateTopLeftLayout(): LayoutResult {
      var sourceSceneBounds = sourceNode.localToScene(sourceNode.layoutBounds);
      LayoutResult {
         point: Point2D {
            x: sourceSceneBounds.minX - bubbleNode.positionBounds.width - bubbleNode.positionBounds.minX;
            y: sourceSceneBounds.minY - bubbleNode.positionBounds.height - bubbleNode.positionBounds.minY;
         }
         arrowPosition: ArrowPosition.BOTTOM_RIGHT
      }
   }

   function calculateTopRightLayout(): LayoutResult {
      var sourceSceneBounds = sourceNode.localToScene(sourceNode.layoutBounds);
      LayoutResult {
         point: Point2D {
            x: sourceSceneBounds.maxX;
            y: sourceSceneBounds.minY - bubbleNode.positionBounds.height - bubbleNode.positionBounds.minY;
         }
         arrowPosition: ArrowPosition.BOTTOM_LEFT
      }
   }

   function calculateBottomRightLayout(): LayoutResult {
      var sourceSceneBounds = sourceNode.localToScene(sourceNode.layoutBounds);
      LayoutResult {
         point: Point2D {
            x: sourceSceneBounds.maxX - bubbleNode.positionBounds.minX;
            y: sourceSceneBounds.maxY - bubbleNode.positionBounds.minY;
         }
         arrowPosition: ArrowPosition.TOP_LEFT
      }
   }

   function calculateBottomLeftLayout(): LayoutResult {
      var sourceSceneBounds = sourceNode.localToScene(sourceNode.layoutBounds);
      LayoutResult {
         point: Point2D {
            x: sourceSceneBounds.minX - bubbleNode.positionBounds.width - bubbleNode.positionBounds.minX;
            y: sourceSceneBounds.maxY - bubbleNode.positionBounds.minY;
         }
         arrowPosition: ArrowPosition.TOP_RIGHT
      }
   }

   function calculateTooltipAreaOutsideScene(node: NodeBubble, layout: Point2D, sceneBounds: Bounds): Number {
      node.layoutX = layout.x;
      node.layoutY = layout.y;
      var nodeBounds = node.localToScene(node.positionBounds);
      var intersection = calculateRectangleIntersection(nodeBounds, sceneBounds);
      return nodeBounds.width * nodeBounds.height - intersection;
   }

   function calculateRectangleIntersection(rect1: Bounds, rect2: Bounds): Number {
      if (rect1.intersects(rect2)) {
         var xLength = calculateIntersectionLength(rect1.minX, rect1.maxX, rect2.minX, rect2.maxX);
         var yLength = calculateIntersectionLength(rect1.minY, rect1.maxY, rect2.minY, rect2.maxY);
         return xLength * yLength;
      }
      return 0.0;
   }

   function calculateIntersectionLength(min1: Number, max1: Number, min2: Number, max2: Number): Number {
      return Math.min(max1, max2) - Math.max(min1, min2);
   }

   function moveTooltipInside(sceneBounds: BoundingBox) {
      realMoveTooltipInside(sceneBounds, true);
      // do it a second time, in case the top/left is moved out of the window
      // this can only happen if the window is to small to contain the complete tooltip
      // in this case, the top/left will be inside the window
      realMoveTooltipInside(sceneBounds, false);
   }

   function realMoveTooltipInside(sceneBounds: BoundingBox, alsoBottomRight: Boolean) {
      def tooltipSceneBounds = bubbleNode.localToScene(bubbleNode.positionBounds);
      if (tooltipSceneBounds.minX < sceneBounds.minX) {
         // left side out window
         bubbleNode.layoutX += sceneBounds.minX - tooltipSceneBounds.minX
      } else if (alsoBottomRight and tooltipSceneBounds.maxX > sceneBounds.maxX) {
         // right side out window
         bubbleNode.layoutX += sceneBounds.maxX - tooltipSceneBounds.maxX
      }
      if (tooltipSceneBounds.minY < sceneBounds.minY) {
         // top side out window
         bubbleNode.layoutY += sceneBounds.minY - tooltipSceneBounds.minY
      } else if (alsoBottomRight and tooltipSceneBounds.maxY > sceneBounds.maxY) {
         // bottom side out window
         bubbleNode.layoutY += sceneBounds.maxY - tooltipSceneBounds.maxY
      }
   }

}
