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
import com.sun.javafx.tk.swing.WindowStage;
import javafx.scene.Scene;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import eu.scy.client.desktop.scydesktop.scywindows.WindowManager;
import java.lang.IllegalArgumentException;

/**
 * @author sikken
 */
public class SimpleBubbleManager extends BubbleManager, ShowNextBubble, IActionLogger {

   def logger = Logger.getLogger(this.getClass());
   public-init var tbi: ToolBrokerAPI;
   public-init var activateBubbleManager = true;
   public-init var scene: Scene;
   public-init var windowManager: WindowManager;
   def bubbleStore = new BubbleStoreImpl();
//   var activeLayerId: Object;
   var noBubbleFoundCounter = 0;
   def bubbleManagerTimer = new BubbleManagerTimer(this);
   def layerManager = new BubbleLayerManager();
   def resourceBundleWrapper = new ResourceBundleWrapper(this);
   var debugBubbleKey: BubbleKey = null;
   var pauzeCount = 0;

   init {
      showingLayer(BubbleLayer.DESKTOP);
//      debugBubbleKey = BubbleKey.MISSION_MAP_ANCHOR_ELO_MAP;
   }

   public override function log(action: IAction): Void {
      //      println("actionLogged");
      userDidSomething();
   }

   public override function userDidSomething(): Void {
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

   public override function pauze(): Void {
      pauzeCount++ }

   public override function resume(): Void {
      pauzeCount--;
      if (pauzeCount < 0) {
         logger.warn("trying to decrease pauzeCount below 0");
         pauzeCount = 0;
      }
   }

   function isScyLabActive(): Boolean {
      var window = (scene.stage.get$Stage$impl_peer() as WindowStage).window;
      //      println("window.isActive(): {window.isActive()}, window.isFocused(): {window.isFocused()}");
      return window.isActive();
   }

   override function showNextBubble(): Void {
      if (pauzeCount <= 0 and isScyLabActive()) {
         FX.deferAction(bubbleStep);
      }
   }

   function bubbleStep(): Void {
      def topLayerId = layerManager.getTopLayer();
      def bubbleToDisplay = if (topLayerId != null) bubbleStore.getNextBubble(topLayerId) as AbstractBubble else null;
      if (bubbleToDisplay != null) {
         showBubble(bubbleToDisplay);
         noBubbleFoundCounter = 0;
         pauze();
      } else {
         ++noBubbleFoundCounter;
         if (noBubbleFoundCounter < 3) {
            logger.info("no bubble found to display");
         }
      }
   }

   function showBubble(bubble: AbstractBubble): Void {
      logger.info("display bubble: {bubble}");
      def bubbleNode = bubble.getBubbleContent();
      var targetNode = bubble.getTargetNode();
      if (targetNode == null) {
         targetNode = bubble.targetNode
      }

      BubbleShower {
         simpleBubbleManager: this
         bubble: bubble
         tooltipGroup: SimpleTooltipManager.tooltipGroup
         bubbleContent: bubbleNode
         windowColorScheme: bubble.windowColorScheme
         sourceNode: targetNode
      //         startAppearingTime: startAppearingTime
      //         fullAppearingTime: fullAppearingTime
      //         startDisappearingTime: startDisappearingTime
      //         fullDisappearingTime: fullDisappearingTime
      }

   }

   public function bubbleRemoved(bubble: AbstractBubble): Void {
      if (debugBubbleKey == null) {
         bubbleStore.removeBubbles(bubble.id);
      }
      resume()
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

   public override function createBubble(targetNode: Node, bubbleLayer: BubbleLayer, bubbleKey: BubbleKey, window: ScyWindow): Bubble {
      if (bubbleKey != null) {
         createBubble(targetNode, bubbleKey.ordinal(), bubbleKey.toString(), bubbleLayer, bubbleKey, window.windowColorScheme, window);
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

   public override function createBubble(targetNode: Node, id: String, bubbleLayer: BubbleLayer, bubbleKey: BubbleKey, window: ScyWindow): Bubble {
      if (bubbleKey != null) {
         createBubble(targetNode, bubbleKey.ordinal(), id, bubbleLayer, bubbleKey, window.windowColorScheme, window);
      } else {
         null
      }
   }

   public override function createBubble(targetNode: Node, priority: Integer, id: String, bubbleLayer: BubbleLayer,
           bubbleKey: BubbleKey, windowColorScheme: WindowColorScheme): Bubble {
      if (bubbleKey != null) {
         createBubble(targetNode, priority, id, bubbleLayer, bubbleKey, windowColorScheme, null);
      } else {
         null
      }
   }

   public override function createBubble(targetNode: Node, priority: Integer, id: String, bubbleLayer: BubbleLayer,
           bubbleKey: BubbleKey, window: ScyWindow): Bubble {
      if (bubbleKey != null) {
         createBubble(targetNode, priority, id, bubbleLayer, bubbleKey, null, window);
      } else {
         null
      }
   }

   function createBubble(targetNode: Node, priority: Integer, id: String, bubbleLayer: BubbleLayer,
           bubbleKey: BubbleKey, windowColorScheme: WindowColorScheme, window: ScyWindow): Bubble {
      if (bubbleKey == null) {
         throw new IllegalArgumentException("the bubbleKey must be set");
      }
      if (targetNode == null) {
         throw new IllegalArgumentException("the targetNode must be set");
      }
      //      if (windowColorScheme==null){
      //         throw new IllegalArgumentException("the windowColorScheme must be set");
      //      }

      var usePriority = priority;
      if (debugBubbleKey != null and bubbleKey == debugBubbleKey) {
         usePriority = 9999
      }

      def bubble = TextBubble {
                 priority: usePriority
                 id: id
                 layerId: bubbleLayer
                 targetNode: targetNode
                 bubbleText: getBubbleText(bubbleKey)
                 windowColorScheme: windowColorScheme
                 window: window
                 bubbleManagerCheckFunction: bubbleManagerCheckFunction
              }
      bubbleStore.addBubble(bubble);
      return bubble;
   }

   function getBubbleText(bubbleKey: BubbleKey): String {
      resourceBundleWrapper.getString("bubbleHelp.{bubbleKey}")
   }

   function bubbleManagerCheckFunction(bubble: AbstractBubble): Boolean {
      if (bubble.window != null) {
         // bubble is for a winodw/elo
         if (not windowManager.hasWindow(bubble.window)) {
            return false;
         }
         if (bubble.window != windowManager.activeWindow) {
            if (windowManager.activeWindow.isMaximized) {
               // an other window is full screen
               return false
            }
            if (not windowManager.activeWindow.isClosed) {
               // an other window is open
               return false
            }
         }
      } else {
         if (windowManager.activeWindow.isMaximized) {
            // an other window is full screen
            return false
         }
      }
      return true
   }

}
