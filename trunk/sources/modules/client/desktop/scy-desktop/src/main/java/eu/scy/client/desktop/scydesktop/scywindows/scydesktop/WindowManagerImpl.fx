/*
 * ScyDesktopImpl.fx
 *
 * Created on 22-jun-2009, 15:49:10
 */

package eu.scy.client.desktop.scydesktop.scywindows.scydesktop;


import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import eu.scy.client.desktop.scydesktop.scywindows.WindowManager;

import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import javafx.util.Sequences;

import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.paint.Color;

import javafx.scene.Group;

import java.net.URI;


/**
 * @author sikkenj
 */
def logger = Logger.getLogger("eu.scy.client.desktop.scydesktop.scywindows.scydesktop.WindowManagerImpl");

public class WindowManagerImpl extends WindowManager {
   public-read override var scyWindows = Group{};

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

   function setActiveWindowState(scyWindow:ScyWindow){
      scyWindow.effect = activeWindowEffect;
      scyWindow.activated = true;
   }

   function setDeactiveWindowState(scyWindow:ScyWindow){
      scyWindow.effect = inactiveWindowEffect;
      scyWindow.activated = false;
   }


   public override function addScyWindow(scyWindow:ScyWindow){
      logger.info("addScyWindow({scyWindow.eloUri})");
      scyWindow.scyDesktop = this;
      setDeactiveWindowState(scyWindow);
		if (not desktopContainsWindow(scyWindow)){
			insert scyWindow into scyWindows.content;
		}
		else {
         logger.warn("Trying to add scyWindow {scyWindow.eloUri}, but it is allready there");
		}
   }

   public override function removeScyWindow(scyWindow:ScyWindow){
      logger.info("removeScyWindow({scyWindow.eloUri})");
      var index = Sequences.indexOf(scyWindows.content, scyWindow);
      if (index>=0){
         delete scyWindows.content[index];
      }
      else {
         logger.warn("Trying to remove scyWindow {scyWindow.eloUri}, but it is not there");
      }
   }

   public override function activateScyWindow(scyWindow:ScyWindow){
      logger.info("activateScyWindow({scyWindow.eloUri})");
		if (desktopContainsWindow(scyWindow)){
			activeWindow = scyWindow;
         activeWindow.toFront();
		}
		else {
         logger.warn("There is no scyWindow {scyWindow.eloUri}");
		}
   }

   public override function hideScyWindow(scyWindow:ScyWindow){
      logger.info("hideScyWindow({scyWindow.eloUri})");
		if (desktopContainsWindow(scyWindow)){
			scyWindow.visible = false;
		}
		else {
         logger.warn("There is no scyWindow {scyWindow.eloUri}");
		}
   }

   public override function showScyWindow(scyWindow:ScyWindow){
      logger.info("showScyWindow({scyWindow.id})");
		if (desktopContainsWindow(scyWindow)){
			scyWindow.visible = true;
		}
		else {
         logger.warn("There is no scyWindow {scyWindow.eloUri}");
		}
   }

	public override  function findScyWindow(uri:URI):ScyWindow{
      for (node in scyWindows.content){
         if (node instanceof ScyWindow){
            var window = node as ScyWindow;
           if (window.eloUri == uri){
              return window;
           }
         }
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

   public override function removeAllScyWindows():Void{
      delete scyWindows.content;
      activeWindow = null;
   }

}
