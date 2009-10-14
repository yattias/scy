/*
 * ScyDesktop.fx
 *
 * Created on 22-jun-2009, 15:34:10
 */

package eu.scy.client.desktop.scydesktop.scywindows;

import javafx.scene.Group;


import java.net.URI;

import eu.scy.client.desktop.scydesktop.tools.corner.missionmap.MissionAnchorFX;

/**
 * @author sikkenj
 */

public mixin class WindowManager {

   public var scyWindows:Group;

   public var activeAnchor:MissionAnchorFX;

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
   
	public abstract function findScyWindow(uri:URI):ScyWindow;

   public abstract function getScyWindows():ScyWindow[];

//   public function checkVisibilityScyWindows(showIt: function(ScyWindow):Boolean){
//   }

}
