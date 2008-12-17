/*
 * ScyDesktop.fx
 *
 * Created on 16-dec-2008, 14:57:37
 */

package eu.scy.scywindows;

import java.lang.System;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.Group;
import javafx.scene.paint.Color;

/**
 * @author sikkenj
 */

 // place your code here

public class ScyDesktop{
   public var desktop:Group = Group{};

   var idCount = 0;
   var windowOffsetStep = 10;
   var activeWindow:ScyWindow;
   var activeWindowEffect:Effect = DropShadow {
      offsetX: 4,
      offsetY: 4,
      color: Color.BLACK
			}
   var inactiveWindowEffect:Effect = DropShadow {
      offsetX: 1,
      offsetY: 1
      color: Color.BLACK
			}

   public function addScyWindow(scyWindow:ScyWindow){
      scyWindow.id = "id_{idCount++}";
      scyWindow.scyDesktop = this;
      scyWindow.translateX = windowOffsetStep*idCount;
      scyWindow.translateY = windowOffsetStep*idCount;
      deactivateScyWindow(scyWindow);
       insert scyWindow into desktop.content;
      System.out.println("Add scyWindow {scyWindow.title} to the desktop ({desktop.content.size()})");
   }

   public function removeScyWindow(scyWindow:ScyWindow){
      deactivateScyWindow(scyWindow);
      scyWindow.scyDesktop = null;
      delete scyWindow from desktop.content;
      System.out.println("Remove scyWindow {scyWindow.title} from the desktop ({desktop.content.size()})");
   }

   public function activateScyWindow(scyWindow:ScyWindow){
      if (activeWindow != null){
         deactivateScyWindow(activeWindow);
      }
      scyWindow.toFront();
      activeWindow = scyWindow;
      activeWindow.effect = activeWindowEffect;
   }

   function deactivateScyWindow(scyWindow:ScyWindow){
      scyWindow.effect = inactiveWindowEffect;
      if (scyWindow == activeWindow){
         activeWindow = null;
      }
   }

}