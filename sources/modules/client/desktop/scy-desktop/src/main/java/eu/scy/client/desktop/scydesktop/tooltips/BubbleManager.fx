/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tooltips;

import javafx.scene.Node;

/**
 * @author SikkenJ
 */
public mixin class BubbleManager {

   public abstract function start():Void;

//   public abstract function registerNode(sourceNode: Node, bubbleCreator: TooltipCreator, priority: Integer, id: String, layerId: String): Void;

   public abstract function showingLayer(layerId: Object): Void;

   public abstract function hidingLayer(layerId: Object): Void;

   public abstract function createBubble(targetNode: Node, priority: Integer, id: String, layerId: Object, displayKey: String): Bubble;

}
