/*
 * ScyDesktopImpl.fx
 *
 * Created on 22-jun-2009, 15:49:10
 */

package eu.scy.client.desktop.scydesktop.scywindows.scydesktop;


import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import eu.scy.client.desktop.scydesktop.scywindows.WindowManager;

import eu.scy.client.desktop.scydesktop.missionmap.Anchor;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import javafx.util.Sequences;

import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.paint.Color;

import javafx.scene.Group;

/**
 * @author sikkenj
 */
var logger = Logger.getLogger("eu.scy.client.desktop.scydesktop.scywindows.scydesktop.WindowManagerImpl");

public class WindowManagerImpl extends WindowManager {
   public-read override var scyWindows = Group{};
   public override var activeAnchor on replace previousAnchor{
         switchActiveAnchor(previousAnchor);
   };
   var activeWindow:ScyWindow on replace previousActiveWindow {
      if (previousActiveWindow!=null){
         setDeactiveWindowState(previousActiveWindow);
      }
      if (activeWindow!=null){
         setActiveWindowState(activeWindow);
      }
   };
   var windowStateListMap:Map = new HashMap();

   def activeWindowEffect: Effect = DropShadow {
      offsetX: 6,
      offsetY: 6,
      color: Color.color(0.25,.25,.25)
   }
   def inactiveWindowEffect: Effect = null;

   function switchActiveAnchor(previousAnchor : Anchor){
      windowStateListMap.put(previousAnchor,getCurrentWindowsStates());
   }

   function getCurrentWindowsStates():WindowState[]{
      var windowStates:WindowState[];
      for (window in scyWindows.content){
         insert WindowState{} into windowStates;
      }
      return windowStates
   }

   function setWindowStates(windowStates:WindowState[]){
      delete scyWindows.content;
      for (windowState in windowStates){
         windowState.applyState();
         insert windowState.window into scyWindows.content;
      }
   }

   function setActiveWindowState(scyWindow:ScyWindow){
      scyWindow.effect = activeWindowEffect;
   }

   function setDeactiveWindowState(scyWindow:ScyWindow){
      scyWindow.effect = inactiveWindowEffect;
   }


   public override function addScyWindow(scyWindow:ScyWindow){
      logger.info("addScyWindow({scyWindow.id})");
      scyWindow.scyDesktop = this;
      setDeactiveWindowState(scyWindow);
		if (not desktopContainsWindow(scyWindow)){
			insert scyWindow into scyWindows.content;
		}
		else {
         logger.warn("Trying to add scyWindow {scyWindow.id}, but it is allready there");
		}
   }

   public override function removeScyWindow(scyWindow:ScyWindow){
      logger.info("removeScyWindow({scyWindow.id})");
      var index = Sequences.indexOf(scyWindows.content, scyWindow);
      if (index>=0){
         delete scyWindows.content[index];
      }
      else {
         logger.warn("Trying to remove scyWindow {scyWindow.id}, but it is not there");
      }
   }

   public override function activateScyWindow(scyWindow:ScyWindow){
      logger.info("activateScyWindow({scyWindow.id})");
		if (desktopContainsWindow(scyWindow)){
			activeWindow = scyWindow;
         activeWindow.toFront();
		}
		else {
         logger.warn("There is no scyWindow {scyWindow.id}");
		}
   }

   public override function hideScyWindow(scyWindow:ScyWindow){
      logger.info("hideScyWindow({scyWindow.id})");
		if (desktopContainsWindow(scyWindow)){
			scyWindow.visible = false;
		}
		else {
         logger.warn("There is no scyWindow {scyWindow.id}");
		}
   }

   public override function showScyWindow(scyWindow:ScyWindow){
      logger.info("showScyWindow({scyWindow.id})");
		if (desktopContainsWindow(scyWindow)){
			scyWindow.visible = true;
		}
		else {
         logger.warn("There is no scyWindow {scyWindow.id}");
		}
   }

	public override  function findScyWindow(id:String):ScyWindow{
      for (window in scyWindows.content){
         if (window.id == id)
         return window as ScyWindow;
      }
      return null;
   }

   public override function getScyWindows():ScyWindow[]{
      return
      for (window in scyWindows.content){
         window as ScyWindow;
      }
   }

   function desktopContainsWindow(scyWindow:ScyWindow) : Boolean{
      var index = Sequences.indexOf(scyWindows.content, scyWindow);
      return index>=0;
   }

}
