/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tooltips;

import javafx.scene.Node;
import eu.scy.client.desktop.desktoputils.art.WindowColorScheme;
import eu.scy.client.desktop.scydesktop.tooltips.impl.AbstractBubble;

/**
 * @author SikkenJ
 */
public mixin class BubbleManager {

   public abstract function start():Void;

//   public abstract function registerNode(sourceNode: Node, bubbleCreator: TooltipCreator, priority: Integer, id: String, layerId: String): Void;

   public abstract function showingLayer(bubbleLayer: BubbleLayer): Void;

   public abstract function hidingLayer(bubbleLayer: BubbleLayer): Void;

   public abstract function createBubble(targetNode: Node, priority: Integer, id: String, bubbleLayer: BubbleLayer, displayKey: String): Bubble;
   
   public abstract function createBubble(targetNode: Node, priority: Integer, id: String, bubbleLayer: BubbleLayer, displayKey: String, windowColorScheme: WindowColorScheme): Bubble;

//   public abstract function addBubble(bubble: AbstractBubble): Void;

}
