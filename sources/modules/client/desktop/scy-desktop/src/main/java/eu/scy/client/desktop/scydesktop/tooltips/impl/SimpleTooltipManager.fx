/*
 * SimpleTooltipManager.fx
 *
 * Created on 26-nov-2009, 17:40:11
 */
package eu.scy.client.desktop.scydesktop.tooltips.impl;

import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.tooltips.TooltipManager;
import eu.scy.client.desktop.scydesktop.tooltips.TooltipCreator;
import java.util.HashMap;
import javafx.scene.input.MouseEvent;
import javafx.animation.Interpolator;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import java.lang.Exception;
import eu.scy.client.desktop.scydesktop.utils.log4j.Logger;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.geometry.Bounds;
import javafx.util.Math;
import javafx.geometry.BoundingBox;
import javafx.geometry.Point2D;
import eu.scy.client.desktop.scydesktop.art.AnimationTiming;
import java.lang.UnsupportedOperationException;

/**
 * @author sikken
 */
public var scene: Scene;
public var tooltipGroup = Group {
   };

public class SimpleTooltipManager extends TooltipManager {

   def logger = Logger.getLogger(this.getClass());
   def sourceNodes = new HashMap();
   var currentSourceNode: Node;
   var currentTooltip: Node;
   var currentTimeLine: Timeline;
//   def startShowTime = 1.0s;
//   def appearTime = 250ms;
//   def showTime = 10s;
   def finalOpacity = 0.85;
   def useAnimation = true;
   protected var startAppearingTime = AnimationTiming.startAppearingTime;
   protected var fullAppearingTime = AnimationTiming.fullAppearingTime;
   protected var startDisappearingTime = AnimationTiming.startDisappearingTime;
   protected var fullDisappearingTime = AnimationTiming.fullDisappearingTime;

   function getTimeLine() {
      def tooltipNode = currentTooltip;
      Timeline {
         keyFrames: [
            KeyFrame {
               time: 0s
               values: tooltipNode.opacity => 0.0;
            //               action: function() {
            //                  println("start time line: {tooltipNode.opacity}, {currentTooltip != null}");
            //               }
            }
            KeyFrame {
               time: startAppearingTime
               values: tooltipNode.opacity => 0.0;
               action: function() {
//                  println("start opacity set: {tooltipNode.opacity}");
               }
            }
            KeyFrame {
               time: fullAppearingTime
               values: tooltipNode.opacity => finalOpacity tween Interpolator.EASEBOTH;
            //               action: function () {
            //                  println("final opacity set: {tooltipNode.opacity}");
            //               }
            }
            KeyFrame {
               time: startDisappearingTime
               values: tooltipNode.opacity => finalOpacity tween Interpolator.EASEBOTH;
            //               action: function () {
            //                  println("start time line: {tooltipNode.opacity}, {currentTooltip!=null}");
            //               }
            }
            KeyFrame {
               time: fullDisappearingTime
               values: tooltipNode.opacity => 0.0 tween Interpolator.EASEBOTH;
               action: function() {
                  removeTooltip();
//                  println("start time line: {tooltipNode.opacity}, {currentTooltip != null}");
               }
            }
         ]
      }
   }

   public override function registerNode(sourceNode: Node, tooltipCreator: TooltipCreator): Void {
      //println("registerNode({sourceNode},{tooltipCreator})");
      sourceNode.onMouseEntered = onMouseEntered;
      sourceNode.onMouseExited = onMouseExited;
      sourceNodes.put(sourceNode, tooltipCreator);
   }

   public override function unregisterNode(sourceNode: Node): Void {
      sourceNodes.remove(sourceNode);
      sourceNode.onMouseEntered = null;
      sourceNode.onMouseExited = null;
   }

   package function onMouseEntered(e: MouseEvent): Void {
      if (e.node == currentSourceNode) {
         // nothing to do
         return
      } else {
         removeTooltip();
      }

      if (currentTooltip != null) {
         var currentSourceLoc = currentSourceNode.sceneToLocal(e.sceneX, e.sceneY);
         if (currentSourceNode.contains(currentSourceLoc)) {
            logger.debug("aborted start of tooltip for {e.node}, because tooltip is active for {currentSourceNode}");
            return;
         }
      }

      setTooltipNode(e.node);
      if (currentTooltip != null) {
         if (useAnimation) {
            insert currentTooltip into tooltipGroup.content;
            FX.deferAction(positionTooltip);
            currentTimeLine = getTimeLine();
            currentTimeLine.play();
         }
      }
   }

   package function onMouseExited(e: MouseEvent): Void {
      removeTooltip();
   }

   public override function removeTooltip(): Void {
      if (currentTooltip != null) {
         if (useAnimation) {
            currentTimeLine.stop();
            currentTimeLine.time = 0s;
         }
         //delete currentTooltip from currentSourceNode.scene.content;
         delete currentTooltip from tooltipGroup.content;
         //         var contentList = currentSourceNode.scene.content;
         //         delete currentTooltip from contentList;
         //         currentSourceNode.scene.content = contentList;
         currentTooltip = null;
         currentSourceNode = null;
      }
   }

   function setTooltipNode(sourceNode: Node): Void {
      //println("setTooltipNode({sourceNode})");
      if (sourceNode != currentSourceNode) {
         try {
            var tooltipCreator = sourceNodes.get(sourceNode) as TooltipCreator;
            var newTooltip = tooltipCreator.createTooltipNode(sourceNode);
            if (newTooltip != null) {
               currentTooltip = newTooltip;
               currentSourceNode = sourceNode;
               currentTooltip.opacity = 0.0;
               // move it far away, so it does not intercept any mouseevents
               currentTooltip.layoutX = 1e6;
               currentTooltip.layoutY = 1e6;
            //               insert currentTooltip into tooltipGroup.content;
            }
         }
         catch (e: Exception) {
            logger.error("exception during tooltip creation", e);
         }
      }
   }

   function positionTooltip(): Void {
      var sceneBounds = BoundingBox {
            width: currentSourceNode.scene.width;
            height: currentSourceNode.scene.height
         }
      var sourceSceneBounds = currentSourceNode.localToScene(currentSourceNode.layoutBounds);

      var positionFunctions = [calculateBottomRightLayout, calculateBottomLeftLayout, calculateTopLeftLayout, calculateTopRightLayout];

      var outsideArea = Number.MAX_VALUE;
      var toolTipLayout: Point2D;
      //      println("sceneBounds: {sceneBounds}");
      for (positionFunction in positionFunctions) {
         var newTooltipLayout = positionFunction();
         var newOutsideArea = calculateTooltipAreaOutsideScene(currentTooltip, newTooltipLayout, sceneBounds);
         //         println("currentTooltip local bounds: {currentTooltip.localToScene(currentTooltip.layoutBounds)} -> {newOutsideArea}");
         if (newOutsideArea < outsideArea) {
            outsideArea = newOutsideArea;
            toolTipLayout = newTooltipLayout;
         }
      }

      currentTooltip.layoutX = toolTipLayout.x;
      currentTooltip.layoutY = toolTipLayout.y;
      if (outsideArea > 0) {
         moveTooltipInside(sceneBounds);
      }
   }

   function calculateTopLeftLayout(): Point2D {
      var sourceSceneBounds = currentSourceNode.localToScene(currentSourceNode.layoutBounds);
      Point2D {
         x: sourceSceneBounds.minX - currentTooltip.layoutBounds.width - currentTooltip.layoutBounds.minX;
         y: sourceSceneBounds.minY - currentTooltip.layoutBounds.height - currentTooltip.layoutBounds.minY;
      }
   }

   function calculateTopRightLayout(): Point2D {
      var sourceSceneBounds = currentSourceNode.localToScene(currentSourceNode.layoutBounds);
      Point2D {
         x: sourceSceneBounds.maxX;
         y: sourceSceneBounds.minY - currentTooltip.layoutBounds.height - currentTooltip.layoutBounds.minY;
      }
   }

   function calculateBottomRightLayout(): Point2D {
      var sourceSceneBounds = currentSourceNode.localToScene(currentSourceNode.layoutBounds);
      Point2D {
         x: sourceSceneBounds.maxX;
         y: sourceSceneBounds.maxY;
      }
   }

   function calculateBottomLeftLayout(): Point2D {
      var sourceSceneBounds = currentSourceNode.localToScene(currentSourceNode.layoutBounds);
      Point2D {
         x: sourceSceneBounds.minX - currentTooltip.layoutBounds.width - currentTooltip.layoutBounds.minX;
         y: sourceSceneBounds.maxY;
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
      def tooltipSceneBounds = currentTooltip.localToScene(currentTooltip.layoutBounds);
      if (tooltipSceneBounds.minX < sceneBounds.minX) {
         // left side out window
         currentTooltip.layoutX += sceneBounds.minX - tooltipSceneBounds.minX
      } else if (alsoBottomRight and tooltipSceneBounds.maxX > sceneBounds.maxX) {
         // right side out window
         currentTooltip.layoutX += sceneBounds.maxX - tooltipSceneBounds.maxX
      }
      if (tooltipSceneBounds.minY < sceneBounds.minY) {
         // top side out window
         currentTooltip.layoutY += sceneBounds.minY - tooltipSceneBounds.minY
      } else if (alsoBottomRight and tooltipSceneBounds.maxY > sceneBounds.maxY) {
         // bottom side out window
         currentTooltip.layoutY += sceneBounds.maxY - tooltipSceneBounds.maxY
      }
   }

}
