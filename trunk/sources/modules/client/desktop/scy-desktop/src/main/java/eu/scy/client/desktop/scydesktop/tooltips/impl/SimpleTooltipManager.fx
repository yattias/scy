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

/**
 * @author sikken
 */
public var scene:Scene;
public var tooltipGroup = Group{

};


public class SimpleTooltipManager extends TooltipManager {
   def logger = Logger.getLogger(this.getClass());

   def sourceNodes = new HashMap();
   var currentSourceNode: Node;
   var currentTooltip: Node;
   var currentTimeLine: Timeline;
   def startShowTime = 1.0s;
   def appearTime = 250ms;
   def showTime = 10s;
   def finalOpacity = 0.75;
   def useAnimation = true;

   function getTimeLine() {
      def tooltipNode = currentTooltip;
      Timeline {
         keyFrames: [
            KeyFrame {
               time: 0s
               values: tooltipNode.opacity => 0.0;
            //               action: function () {
            //                  println("start time line: {tooltipNode.opacity}, {currentTooltip!=null}");
            //               }
            }
            KeyFrame {
               time: startShowTime
               values: tooltipNode.opacity => 0.0;
            //               action: function () {
            //                  println("start opacity set: {tooltipNode.opacity}");
            //               }
            }
            KeyFrame {
               time: startShowTime + appearTime
               values: tooltipNode.opacity => finalOpacity tween Interpolator.EASEBOTH;
            //               action: function () {
            //                  println("final opacity set: {tooltipNode.opacity}");
            //               }
            }
            KeyFrame {
               time: showTime
               values: tooltipNode.opacity => finalOpacity tween Interpolator.EASEBOTH;
            //               action: function () {
            //                  println("start time line: {tooltipNode.opacity}, {currentTooltip!=null}");
            //               }
            }
            KeyFrame {
               time: showTime + appearTime
               values: tooltipNode.opacity => 0.0 tween Interpolator.EASEBOTH;
            //               action: function () {
            //                  println("start time line: {tooltipNode.opacity}, {currentTooltip!=null}");
            //               }
            }
         ]
      }
   }

   public override function registerNode(sourceNode: Node, tooltipCreator: TooltipCreator): Void {
      sourceNode.onMouseEntered = onMouseEntered;
      sourceNode.onMouseExited = onMouseExited;
      sourceNodes.put(sourceNode, tooltipCreator);
   }

   public override function unregisterNode(sourceNode: Node): Void {
      sourceNodes.remove(sourceNode);
      sourceNode.onMouseEntered = null;
      sourceNode.onMouseExited = null;
   }

   function onMouseEntered(e: MouseEvent): Void {
      setTooltipNode(e.node);
      if (currentTooltip != null) {
         if (useAnimation) {
            currentTimeLine = getTimeLine();
            currentTimeLine.play();
         }
      }
   }

   function onMouseExited(e: MouseEvent): Void {
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
      if (sourceNode != currentSourceNode) {
         try {
            var tooltipCreator = sourceNodes.get(sourceNode) as TooltipCreator;
            currentSourceNode = sourceNode;
            currentTooltip = tooltipCreator.createTooltipNode(currentSourceNode);
            currentTooltip.opacity = 0.0;
            //insert currentTooltip into currentSourceNode.scene.content;
            insert currentTooltip into tooltipGroup.content;
//            var contentList = currentSourceNode.scene.content;
//            insert currentTooltip into contentList;
//            currentSourceNode.scene.content = contentList;
            var sourceSceneBounds = currentSourceNode.localToScene(currentSourceNode.layoutBounds);
            currentTooltip.layoutX = sourceSceneBounds.minX - currentTooltip.layoutBounds.width - currentTooltip.layoutBounds.minX;
            currentTooltip.layoutY = sourceSceneBounds.minY - currentTooltip.layoutBounds.height - currentTooltip.layoutBounds.minY;
         } catch (e: Exception) {
            logger.error("exception during tooltip creation", e);
         }

      }
   }
}
