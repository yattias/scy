/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tooltips.impl;

import eu.scy.client.desktop.scydesktop.tooltips.BubbleManager;
import eu.scy.client.desktop.scydesktop.tooltips.Bubble;
import javafx.scene.Node;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import java.lang.System;

/**
 * @author sikken
 */
public class SimpleBubbleManager extends BubbleManager {

   def timeStep = 1s;
   def bubbleDelayMillis = 10000;
   def bubbleStore = new BubbleStore();

   var lastShownBubbleTime: Long = System.currentTimeMillis();
   var activeLayerId: Object;

   init {
      Timeline {
         repeatCount: Timeline.INDEFINITE
         keyFrames: [
            KeyFrame {
               time: timeStep
               action: bubbleStep
            }
         ];
      }.play()
   }

   function bubbleStep(): Void {
      if (System.currentTimeMillis()>lastShownBubbleTime+bubbleDelayMillis){
         def bubbleToDisplay = bubbleStore.getNextBubble(activeLayerId) as AbstractBubble;
         if (bubbleToDisplay!=null){
            println("display bubble: {bubbleToDisplay}");
            bubbleStore.removeBubbles(bubbleToDisplay.id);
         }
      }
   }

   public function bubbleRemoved(bubble: AbstractBubble):Void{
      lastShownBubbleTime = System.currentTimeMillis();
      bubbleStore.removeBubble(bubble);
   }

   public override function showingLayer(layerId: Object): Void {
      activeLayerId = layerId;
   }

   public override function hidingLayer(layerId: Object): Void {

   }

   public override function createBubble(targetNode: Node, priority: Integer, id: String, layerId: Object, displayKey: String): Bubble {
      def bubble = TextBubble {
         priority: priority
         id: id
         layerId: layerId
         targetNode: targetNode
         bubbleText: displayKey
      }
      bubbleStore.addBubble(bubble);
      return bubble;
   }

}
