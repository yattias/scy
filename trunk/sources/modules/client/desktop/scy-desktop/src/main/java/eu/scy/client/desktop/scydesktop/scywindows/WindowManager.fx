/*
 * ScyDesktop.fx
 *
 * Created on 22-jun-2009, 15:34:10
 */

package eu.scy.client.desktop.scydesktop.scywindows;

import javafx.scene.Group;


import java.net.URI;

import eu.scy.client.desktop.scydesktop.ScyDesktop;

/**
 * The WindowManager manages the windows on the desktop. It does not care if the window contains a anchor elo or not.
 *
 * @author sikkenj
 */

public mixin class WindowManager {

   public var scyWindows:Group;
   public var scyDesktop:ScyDesktop;
   public var activeWindow: ScyWindow;
   public var fullscreenWindow: ScyWindow;

//   public-read protected var activeWindow:ScyWindow;

//   public var activeAnchor:MissionAnchorFX;

   public function addScyWindow(scyWindow:ScyWindow):Void{
   }

   public function removeScyWindow(scyWindow:ScyWindow):Void{
   }

   public function activateScyWindow(scyWindow:ScyWindow):Void{
   }

   public function hideScyWindow(scyWindow:ScyWindow):Void{
   }

   public function showScyWindow(scyWindow:ScyWindow):Void{
   }

//	public abstract function findScyWindow(id:String):ScyWindow;
   
    public abstract function findScyWindow(uri:URI):ScyWindow;

   public abstract function getScyWindows():ScyWindow[];

   public abstract function removeAllScyWindows():Void;

//   public function checkVisibilityScyWindows(showIt: function(ScyWindow):Boolean){
//   }

   public abstract function getWindowUnderMouse(sceneX:Number,sceneY:Number):ScyWindow;
}
