/*
 * WindowState.fx
 *
 * Created on 22-jun-2009, 16:21:53
 */

package eu.scy.client.desktop.scydesktop.scywindows.scydesktop;

import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;

import java.net.URI;

/**
 * @author sikkenj
 */

public class WindowState {
   public var window:ScyWindow on replace {updateState()};
   public var eloUri:URI;
   var x: Number;
   var y: Number;
   var width: Number;
   var height: Number;
   var minimized: Boolean;

   function updateState(){
      x = window.translateX;
      y = window.translateY;
      minimized = window.isMinimized;
      if (minimized){
         width = window.originalWidth;
         height = window.originalHeight;
      }
      else {
         width = window.width;
         height = window.height;
      }

   }

   public function applyState(){
      window.translateX = x;
      window.translateY = y;
      if (minimized){
         // TODO, set original sizes and minimized state
      }
      else{
         window.width = width;
         window.height = height;
         // TODO, set not minimized state
      }
   }
}
