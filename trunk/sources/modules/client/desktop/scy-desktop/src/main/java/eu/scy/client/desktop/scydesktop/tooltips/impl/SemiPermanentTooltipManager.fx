/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tooltips.impl;
import javafx.scene.input.MouseEvent;

/**
 * @author SikkenJ
 */
public class SemiPermanentTooltipManager extends SimpleTooltipManager {

   def semiPermanentTime = 30s;

   init {
      startDisappearingTime += semiPermanentTime;
      fullDisappearingTime += semiPermanentTime;
   }

   package override function onMouseExited(e: MouseEvent): Void {
   }

}
