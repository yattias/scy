/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tooltips.impl;

import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.art.AnimationTiming;
import javafx.scene.Group;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.util.Math;
import javafx.scene.CacheHint;

/**
 * @author SikkenJ
 */
public class TooltipShower {

   public-init var tooltipNode: Node;
   public-init var sourceNode: Node;
   public-init var startAppearingTime = AnimationTiming.startAppearingTime;
   public-init var fullAppearingTime = AnimationTiming.fullAppearingTime;
   public-init var startDisappearingTime = AnimationTiming.startDisappearingTime;
   public-init var fullDisappearingTime = AnimationTiming.fullDisappearingTime;
   public-init var tooltipGroup: Group;
   def finalOpacity = 0.95;
   def tooltip = tooltipNode;
   def timeline = Timeline {
              keyFrames: [
                 KeyFrame {
                    time: 0s
                    values: tooltip.opacity => 0.0;
                 }
                 KeyFrame {
                    time: startAppearingTime
                    values: tooltip.opacity => 0.0;
                    action: function() {
                       insert tooltip into tooltipGroup.content;
                       FX.deferAction(positionTooltip);
                    }
                 }
                 KeyFrame {
                    time: fullAppearingTime
                    values: tooltip.opacity => finalOpacity tween Interpolator.EASEBOTH;
                 }
                 KeyFrame {
                    time: startDisappearingTime
                    values: tooltip.opacity => finalOpacity tween Interpolator.EASEBOTH;
                  }
                 KeyFrame {
                    time: fullDisappearingTime
                    values: tooltip.opacity => 0.0 tween Interpolator.EASEBOTH;
                    action: function() {
                       delete tooltip from tooltipGroup.content;
                    }
                 }
              ]
           }

   init {
      tooltip.cache = true;
      tooltip.cacheHint = CacheHint.SPEED;
      timeline.play();
   }

   public function remove(): Void {
      if (timeline.time < startDisappearingTime) {
         timeline.time = startDisappearingTime
      }
   }

   function positionTooltip(): Void {
      var sceneBounds = BoundingBox {
                 width: sourceNode.scene.width;
                 height: sourceNode.scene.height
              }

      var positionFunctions = [calculateBottomRightLayout, calculateBottomLeftLayout, calculateTopLeftLayout, calculateTopRightLayout];

      var outsideArea = Number.MAX_VALUE;
      var toolTipLayout: Point2D;
      //      println("sceneBounds: {sceneBounds}");
      for (positionFunction in positionFunctions) {
         var newTooltipLayout = positionFunction();
         var newOutsideArea = calculateTooltipAreaOutsideScene(tooltipNode, newTooltipLayout, sceneBounds);
         //         println("currentTooltip local bounds: {currentTooltip.localToScene(currentTooltip.layoutBounds)} -> {newOutsideArea}");
         if (newOutsideArea < outsideArea) {
            outsideArea = newOutsideArea;
            toolTipLayout = newTooltipLayout;
         }
      }

      tooltipNode.layoutX = toolTipLayout.x;
      tooltipNode.layoutY = toolTipLayout.y;
      if (outsideArea > 0) {
         moveTooltipInside(sceneBounds);
      }
   }

   function calculateTopLeftLayout(): Point2D {
      var sourceSceneBounds = sourceNode.localToScene(sourceNode.layoutBounds);
      Point2D {
         x: sourceSceneBounds.minX - tooltipNode.layoutBounds.width - tooltipNode.layoutBounds.minX;
         y: sourceSceneBounds.minY - tooltipNode.layoutBounds.height - tooltipNode.layoutBounds.minY;
      }
   }

   function calculateTopRightLayout(): Point2D {
      var sourceSceneBounds = sourceNode.localToScene(sourceNode.layoutBounds);
      Point2D {
         x: sourceSceneBounds.maxX;
         y: sourceSceneBounds.minY - tooltipNode.layoutBounds.height - tooltipNode.layoutBounds.minY;
      }
   }

   function calculateBottomRightLayout(): Point2D {
      var sourceSceneBounds = sourceNode.localToScene(sourceNode.layoutBounds);
      Point2D {
         x: sourceSceneBounds.maxX - tooltip.layoutBounds.minX;
         y: sourceSceneBounds.maxY - tooltip.layoutBounds.minY;
      }
   }

   function calculateBottomLeftLayout(): Point2D {
      var sourceSceneBounds = sourceNode.localToScene(sourceNode.layoutBounds);
      Point2D {
         x: sourceSceneBounds.minX - tooltipNode.layoutBounds.width - tooltipNode.layoutBounds.minX;
         y: sourceSceneBounds.maxY - tooltip.layoutBounds.minY;
      }
   }

   function calculateTooltipAreaOutsideScene(node: Node, layout: Point2D, sceneBounds: Bounds): Number {
      node.layoutX = layout.x;
      node.layoutY = layout.y;
      var nodeBounds = node.localToScene(node.layoutBounds);
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
      def tooltipSceneBounds = tooltipNode.localToScene(tooltipNode.layoutBounds);
      if (tooltipSceneBounds.minX < sceneBounds.minX) {
         // left side out window
         tooltipNode.layoutX += sceneBounds.minX - tooltipSceneBounds.minX
      } else if (alsoBottomRight and tooltipSceneBounds.maxX > sceneBounds.maxX) {
         // right side out window
         tooltipNode.layoutX += sceneBounds.maxX - tooltipSceneBounds.maxX
      }
      if (tooltipSceneBounds.minY < sceneBounds.minY) {
         // top side out window
         tooltipNode.layoutY += sceneBounds.minY - tooltipSceneBounds.minY
      } else if (alsoBottomRight and tooltipSceneBounds.maxY > sceneBounds.maxY) {
         // bottom side out window
         tooltipNode.layoutY += sceneBounds.maxY - tooltipSceneBounds.maxY
      }
   }

}
