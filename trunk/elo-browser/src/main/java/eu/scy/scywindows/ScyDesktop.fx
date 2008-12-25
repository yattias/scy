/*
 * ScyDesktop.fx
 *
 * Created on 16-dec-2008, 14:57:37
 */

package eu.scy.scywindows;

import eu.scy.scywindows.ScyWindow;
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
      //scyWindow.id = "id_{idCount++}";
      scyWindow.scyDesktop = this;
      //      scyWindow.translateX = windowOffsetStep * idCount;
      //      scyWindow.translateY = windowOffsetStep * idCount;
      deactivateScyWindow(scyWindow);
       insert scyWindow into desktop.content;
      System.out.println("Add scyWindow {scyWindow.title} to the desktop ({desktop.content.size()})");
   }

   public function removeScyWindow(scyWindow:ScyWindow){
      deactivateScyWindow(scyWindow);
      scyWindow.scyDesktop = null;
      if (desktopContainsWindow(scyWindow)){
         delete scyWindow from desktop.content;
         System.out.println("Remove scyWindow {scyWindow.title} from the desktop ({desktop.content.size()})");
      }
   }

   public function activateScyWindow(scyWindow:ScyWindow){
      if (activeWindow != null){
         deactivateScyWindow(activeWindow);
      }
      showScyWindow(scyWindow);
      scyWindow.toFront();
      activeWindow = scyWindow;
      activeWindow.windowEffect = activeWindowEffect;
   }

   function deactivateScyWindow(scyWindow:ScyWindow){
      scyWindow.windowEffect = inactiveWindowEffect;
      if (scyWindow == activeWindow){
         activeWindow = null;
      }
   }

   public function hideScyWindow(scyWindow:ScyWindow){
      if (scyWindow == activeWindow){
         deactivateScyWindow(scyWindow);
      }
      // scyWindow.visible = false;
      removeScyWindow(scyWindow);
   }

   public function showScyWindow(scyWindow:ScyWindow){
      //scyWindow.visible = true;
      addScyWindow(scyWindow);
   }

	public function findScyWindow(id:String):ScyWindow{
      for (window in desktop.content){
         if (window.id == id)
         return window as ScyWindow;
      }
   return null as ScyWindow;
 	}

   function desktopContainsWindow(scyWindow:ScyWindow) : Boolean{
      for (window in desktop.content){
         if (window == scyWindow)
         return true;
      }
   return false;
   }

}

var scyDesktop = ScyDesktop{};

public function getScyDesktop(){
	 return scyDesktop;
}