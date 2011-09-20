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
import eu.scy.client.desktop.scydesktop.tooltips.BubbleLayer;
import eu.scy.client.desktop.scydesktop.tooltips.BubbleKey;
import eu.scy.client.common.scyi18n.ResourceBundleWrapper;
import org.apache.log4j.Logger;

/**
 * @author sikken
 */
public class SimpleBubbleManager extends BubbleManager, ShowNextBubble, IActionLogger {

   def logger = Logger.getLogger(this.getClass());
   public-init var tbi: ToolBrokerAPI;
   public-init var activateBubbleManager = true;
   def timeStep = 1s;
   def bubbleStore = new BubbleStoreImpl();
//   var activeLayerId: Object;
   var noBubbleFoundCounter = 0;
   def bubbleManagerTimer = new BubbleManagerTimer(this);
   def layerManager = new BubbleLayerManager();
   def resourceBundleWrapper = new ResourceBundleWrapper(this);

   init {
      showingLayer(BubbleLayer.DESKTOP);
   }

   public override function log(action: IAction): Void {
      println("actionLogged");
      bubbleManagerTimer.userDidSomething();
   }

   public override function start(): Void {
      if (activateBubbleManager) {
         bubbleManagerTimer.start();
         if (tbi.getActionLogger() instanceof CompletingActionLogger) {
            def internalLogger: IActionLogger = (tbi.getActionLogger() as CompletingActionLogger).getInternalLogger();
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
      def topLayerId = layerManager.getTopLayer();
      def bubbleToDisplay = if (topLayerId != null) bubbleStore.getNextBubble(topLayerId) as AbstractBubble else null;
      if (bubbleToDisplay != null) {
         showBubble(bubbleToDisplay);
         bubbleStore.removeBubbles(bubbleToDisplay.id);
         noBubbleFoundCounter = 0;
      } else {
         ++noBubbleFoundCounter;
         if (noBubbleFoundCounter < 3) {
            println("no bubble found to display");
         }
      }
   }

   function showBubble(bubble: AbstractBubble): Void {
      println("display bubble: {bubble}");
      def bubbleNode = bubble.getBubbleNode();
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

   public override function showingLayer(bubbleLayer: BubbleLayer): Void {
      logger.info("showing BubbleLayer: {bubbleLayer}");
      layerManager.showLayer(bubbleLayer);
      noBubbleFoundCounter = 0;
   }

   public override function hidingLayer(bubbleLayer: BubbleLayer): Void {
      layerManager.hideLayer(bubbleLayer);
      logger.info("hiding BubbleLayer: {bubbleLayer}, new top layer: {layerManager.getTopLayer()}");
      noBubbleFoundCounter = 0;
   }

   public override function createBubble(targetNode: Node, bubbleLayer: BubbleLayer, bubbleKey: BubbleKey): Bubble {
      if (bubbleKey != null) {
         createBubble(targetNode, bubbleKey.ordinal(), bubbleKey.toString(), bubbleLayer, bubbleKey, WindowColorScheme.getWindowColorScheme(ScyColors.darkGray));
      } else {
         null
      }
   }

   public override function createBubble(targetNode: Node, bubbleLayer: BubbleLayer, bubbleKey: BubbleKey, windowColorScheme: WindowColorScheme): Bubble {
      if (bubbleKey != null) {
         createBubble(targetNode, bubbleKey.ordinal(), bubbleKey.toString(), bubbleLayer, bubbleKey, windowColorScheme);
      } else {
         null
      }
   }

   public override function createBubble(targetNode: Node, id: String, bubbleLayer: BubbleLayer, bubbleKey: BubbleKey): Bubble {
      if (bubbleKey != null) {
         createBubble(targetNode, bubbleKey.ordinal(), id, bubbleLayer, bubbleKey, WindowColorScheme.getWindowColorScheme(ScyColors.darkGray));
      } else {
         null
      }
   }

   public override function createBubble(targetNode: Node, priority: Integer, id: String, bubbleLayer: BubbleLayer, bubbleKey: BubbleKey): Bubble {
      createBubble(targetNode, priority, id, bubbleLayer, bubbleKey, WindowColorScheme.getWindowColorScheme(ScyColors.darkGray));
   }

   public override function createBubble(targetNode: Node, id: String, bubbleLayer: BubbleLayer, bubbleKey: BubbleKey, windowColorScheme: WindowColorScheme): Bubble {
      if (bubbleKey != null) {
         createBubble(targetNode, bubbleKey.ordinal(), id, bubbleLayer, bubbleKey, windowColorScheme);
      } else {
         null
      }
   }

   public override function createBubble(targetNode: Node, priority: Integer, id: String, bubbleLayer: BubbleLayer, bubbleKey: BubbleKey, windowColorScheme: WindowColorScheme): Bubble {
      if (bubbleKey == null) {
         logger.info("no BubbleKey defined, for {id} in layer {bubbleLayer}");
         return null;
      }

      def bubble = TextBubble {
                 priority: priority
                 id: id
                 layerId: bubbleLayer
                 targetNode: targetNode
                 bubbleText: getBubbleText(bubbleKey)
                 windowColorScheme: windowColorScheme
              }
      bubbleStore.addBubble(bubble);
      return bubble;
   }

   function getBubbleText(bubbleKey: BubbleKey): String {
      resourceBundleWrapper.getString("bubbleHelp.{bubbleKey}")
   }

}
