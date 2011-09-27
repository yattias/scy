/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tooltips.impl;

import eu.scy.client.desktop.scydesktop.tooltips.Bubble;
import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.tooltips.BubbleLayer;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;

/**
 * @author sikken
 */
public class AbstractBubble extends Bubble, JavaBubble {

   public-init var id: String;
   public-init var layerId: BubbleLayer;
   public-init var priority: Integer;
   public-init var targetNode: Node;
   public-init var window: ScyWindow;
   public-init var bubbleManagerCheckFunction: function(:AbstractBubble): Boolean;

   public override function toString(): String {
      "id={id},priority={priority},layerId={layerId},window={window.eloUri}"
   }

   public override function getId(): String {
      return id
   }

   public override function getLayerId(): BubbleLayer {
      return layerId
   }

   public override function getPriority(): java.lang.Integer {
      return priority
   }

   public override function isVisible(): Boolean {
      var node = targetNode;
      while (node != null) {
         if (not node.visible) {
            return false
         }
         node = node.parent;
      }
      return true
   }

   public override function canBeUsedNow(): Boolean {
      if (bubbleManagerCheckFunction!=null){
         if (not bubbleManagerCheckFunction(this)){
            return false;
         }
      }
      if (canBeUsed != null) {
         return canBeUsed()
      }
      return true;
   }

   public function getBubbleNode(): Node {
      TextTooltip {
         content: id
         windowColorScheme: windowColorScheme
      }
   }

   public function getBubbleContent(): Node {
      TextTooltip {
         content: id
         windowColorScheme: windowColorScheme
      }
   }

   public override function startBubbleling(): Void {

   }

   public override function pauzeBubbleling(): Void {

   }

   public override function resumeBubbleling(): Void {

   }

   public override function stopBubbleling(): Void {

   }

}
