/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tooltips;
import eu.scy.client.desktop.desktoputils.art.ScyColors;
import eu.scy.client.desktop.desktoputils.art.WindowColorScheme;

/**
 * @author SikkenJ
 */
public mixin class Bubble {

   public var windowColorScheme = WindowColorScheme.getWindowColorScheme(ScyColors.darkGray);

   public var canBeUsed: function():Boolean;

   public abstract function startBubbleling(): Void;

   public abstract function pauzeBubbleling(): Void;

   public abstract function resumeBubbleling(): Void;

   public abstract function stopBubbleling(): Void;

}
