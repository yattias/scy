/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tooltips;
import javafx.scene.Node;

/**
 * @author SikkenJ
 */
public mixin class Bubble {

   public abstract function startBubbleling(): Void;

   public abstract function pauzeBubbleling(): Void;

   public abstract function resumeBubbleling(): Void;

   public abstract function stopBubbleling(): Void;

}
