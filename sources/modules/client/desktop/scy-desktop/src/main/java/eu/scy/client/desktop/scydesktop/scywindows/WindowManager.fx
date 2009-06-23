/*
 * ScyDesktop.fx
 *
 * Created on 22-jun-2009, 15:34:10
 */

package eu.scy.client.desktop.scydesktop.scywindows;

import javafx.scene.Group;

import eu.scy.client.desktop.scydesktop.missionmap.Anchor;

/**
 * @author sikkenj
 */

public mixin class WindowManager {

   public var scyWindows:Group;

   public var activeAnchor:Anchor;

   public function addScyWindow(scyWindow:ScyWindow){
   }

   public function removeScyWindow(scyWindow:ScyWindow){
   }

   public function activateScyWindow(scyWindow:ScyWindow){
   }

   public function hideScyWindow(scyWindow:ScyWindow){
   }

   public function showScyWindow(scyWindow:ScyWindow){
   }

	public abstract function findScyWindow(id:String):ScyWindow;

   public abstract function getScyWindows():ScyWindow[];

//   public function checkVisibilityScyWindows(showIt: function(ScyWindow):Boolean){
//   }

}
