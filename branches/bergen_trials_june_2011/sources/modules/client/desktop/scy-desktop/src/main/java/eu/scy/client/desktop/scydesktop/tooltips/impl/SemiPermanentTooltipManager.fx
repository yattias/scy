/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tooltips.impl;
import javafx.scene.input.MouseEvent;
import java.lang.System;

/**
 * @author SikkenJ
 */
public class SemiPermanentTooltipManager extends SimpleTooltipManager {

   def semiPermanentTime = 30s;

   var mouseEnteredTime:Long;

   init {
      startDisappearingTime += semiPermanentTime;
      fullDisappearingTime += semiPermanentTime;
   }

   package override function onMouseEntered(e: MouseEvent): Void {
      mouseEnteredTime = System.currentTimeMillis();
      super.onMouseEntered(e);
   }

   package override function onMouseExited(e: MouseEvent): Void {
      if ((System.currentTimeMillis()-mouseEnteredTime)<startAppearingTime.toMillis()){
         super.onMouseExited(e);
      }
   }
}
