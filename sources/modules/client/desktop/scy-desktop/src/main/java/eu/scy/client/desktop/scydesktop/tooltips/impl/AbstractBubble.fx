/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tooltips.impl;

import eu.scy.client.desktop.scydesktop.tooltips.Bubble;
import javafx.scene.Node;
import eu.scy.client.desktop.desktoputils.art.WindowColorScheme;
import eu.scy.client.desktop.desktoputils.art.ScyColors;

/**
 * @author sikken
 */
public class AbstractBubble extends Bubble, JavaBubble {

   public-init var id: String;
   public-init var layerId: Object;
   public-init var priority: Integer;
   public-init var targetNode: Node;
   public-init var windowColorScheme = WindowColorScheme.getWindowColorScheme(ScyColors.darkGray);

   public override function toString(): String {
      "id={id},priority={priority},layerId={layerId}"
   }

   public override function getId(): String {
      return id
   }

   public override function getLayerId(): Object {
      return layerId
   }

   public override function getPriority(): java.lang.Integer {
      return priority
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
