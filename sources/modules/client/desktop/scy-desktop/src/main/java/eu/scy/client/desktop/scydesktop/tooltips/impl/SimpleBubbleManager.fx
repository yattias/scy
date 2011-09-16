/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tooltips.impl;

import eu.scy.client.desktop.scydesktop.tooltips.BubbleManager;
import eu.scy.client.desktop.scydesktop.tooltips.Bubble;
import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.tooltips.impl.bubblestore.BubbleStoreImpl;
import eu.scy.client.desktop.desktoputils.art.ScyColors;
import eu.scy.client.desktop.desktoputils.art.WindowColorScheme;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import eu.scy.actionlogging.api.IActionLogger;
import eu.scy.actionlogging.api.IAction;
import eu.scy.actionlogging.CompletingActionLogger;
import eu.scy.actionlogging.MultiActionLogger;
import java.lang.RuntimeException;

/**
 * @author sikken
 */
public class SimpleBubbleManager extends BubbleManager, ShowNextBubble, IActionLogger {

   public-init var tbi: ToolBrokerAPI;
   def timeStep = 1s;
   def bubbleStore = new BubbleStoreImpl();
   var activeLayerId: Object;
   var noBubbleFoundCounter = 0;
   def bubbleManagerTimer = new BubbleManagerTimer(this);
   def activateBubbleManager = false;

   init {
   }

   public override function log(action : IAction): Void {
      println("actionLogged");
      bubbleManagerTimer.userDidSomething();
   }

   public override function start(): Void {
      if (activateBubbleManager) {
         bubbleManagerTimer.start();
         if (tbi.getActionLogger() instanceof CompletingActionLogger) {
             def internalLogger : IActionLogger = (tbi.getActionLogger() as CompletingActionLogger).getInternalLogger();
             if (internalLogger instanceof MultiActionLogger) {
                 (internalLogger as MultiActionLogger).addLogger(this);
             }
         } else {
             throw new RuntimeException("BubbleManager could not be added to action logger list")
         }

      }
   }

   override function showNextBubble(): Void {
      FX.deferAction(bubbleStep);
   }

   function bubbleStep(): Void {
      def bubbleToDisplay = bubbleStore.getNextBubble(activeLayerId) as AbstractBubble;
      if (bubbleToDisplay != null) {
         showBubble(bubbleToDisplay);
         //         bubbleStore.removeBubbles(bubbleToDisplay.id);
         noBubbleFoundCounter = 0;
      } else {
         ++noBubbleFoundCounter;
         if (noBubbleFoundCounter < 5) {
            println("no bubble found to display");
         }
      }
   }

   function showBubble(bubble: AbstractBubble): Void {
      println("display bubble: {bubble}");
      def bubbleNode = TextTooltip {
                 content: bubble.id
                 windowColorScheme: bubble.windowColorScheme
              }

      TooltipShower {
         tooltipGroup: SimpleTooltipManager.tooltipGroup
         tooltipNode: bubbleNode
         sourceNode: bubble.targetNode
      //         startAppearingTime: startAppearingTime
      //         fullAppearingTime: fullAppearingTime
      //         startDisappearingTime: startDisappearingTime
      //         fullDisappearingTime: fullDisappearingTime
      }

   }

   public function bubbleRemoved(bubble: AbstractBubble): Void {
      bubbleStore.removeBubble(bubble);
   }

   public override function showingLayer(layerId: Object): Void {
      activeLayerId = layerId;
   }

   public override function hidingLayer(layerId: Object): Void {

   }

   public override function createBubble(targetNode: Node, priority: Integer, id: String, layerId: Object, displayKey: String): Bubble {
      createBubble(targetNode, priority, id, layerId, displayKey, WindowColorScheme.getWindowColorScheme(ScyColors.darkGray));
   }

   public override function createBubble(targetNode: Node, priority: Integer, id: String, layerId: Object, displayKey: String, windowColorScheme: WindowColorScheme): Bubble {
      def bubble = TextBubble {
                 priority: priority
                 id: id
                 layerId: layerId
                 targetNode: targetNode
                 bubbleText: displayKey
                 windowColorScheme: windowColorScheme
              }
      bubbleStore.addBubble(bubble);
      return bubble;
   }

}
