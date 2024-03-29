/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tooltips;

import javafx.scene.Node;
import eu.scy.client.desktop.desktoputils.art.WindowColorScheme;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;

/**
 * @author SikkenJ
 */
public mixin class BubbleManager {

   public abstract function start(): Void;

   public abstract function pauze(): Void;

   public abstract function resume(): Void;

   public abstract function userDidSomething(): Void;

//   public abstract function registerNode(sourceNode: Node, bubbleCreator: TooltipCreator, priority: Integer, id: String, layerId: String): Void;
   public abstract function showingLayer(bubbleLayer: BubbleLayer): Void;

   public abstract function hidingLayer(bubbleLayer: BubbleLayer): Void;

   public abstract function createBubble(targetNode: Node, bubbleLayer: BubbleLayer, bubbleKey: BubbleKey): Bubble;

   public abstract function createBubble(targetNode: Node, bubbleLayer: BubbleLayer, bubbleKey: BubbleKey, windowColorScheme: WindowColorScheme): Bubble;

   public abstract function createBubble(targetNode: Node, bubbleLayer: BubbleLayer, bubbleKey: BubbleKey, window: ScyWindow): Bubble;

   public abstract function createBubble(targetNode: Node, id: String, bubbleLayer: BubbleLayer, bubbleKey: BubbleKey): Bubble;

   public abstract function createBubble(targetNode: Node, id: String, bubbleLayer: BubbleLayer, bubbleKey: BubbleKey, windowColorScheme: WindowColorScheme): Bubble;

   public abstract function createBubble(targetNode: Node, id: String, bubbleLayer: BubbleLayer, bubbleKey: BubbleKey, window: ScyWindow): Bubble;

   public abstract function createBubble(targetNode: Node, priority: Integer, id: String, bubbleLayer: BubbleLayer, bubbleKey: BubbleKey): Bubble;

   public abstract function createBubble(targetNode: Node, priority: Integer, id: String, bubbleLayer: BubbleLayer, bubbleKey: BubbleKey, windowColorScheme: WindowColorScheme): Bubble;

   public abstract function createBubble(targetNode: Node, priority: Integer, id: String, bubbleLayer: BubbleLayer, bubbleKey: BubbleKey, window: ScyWindow): Bubble;

//   public abstract function addBubble(bubble: AbstractBubble): Void;
}
