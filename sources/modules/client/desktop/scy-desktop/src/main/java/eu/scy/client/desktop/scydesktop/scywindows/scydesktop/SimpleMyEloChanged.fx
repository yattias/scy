/*
 * SimpleMyEloChanged.fx
 *
 * Created on 1-dec-2009, 13:10:07
 */

package eu.scy.client.desktop.scydesktop.scywindows.scydesktop;

import eu.scy.client.desktop.scydesktop.tools.MyEloChanged;

import roolo.elo.api.IELO;

import eu.scy.client.desktop.scydesktop.scywindows.WindowManager;

import roolo.elo.api.IMetadataKey;

import org.apache.log4j.Logger;

import java.net.URI;

/**
 * @author sikken
 */

def logger = Logger.getLogger("eu.scy.client.desktop.scydesktop.scywindows.scydesktop.SimpleMyEloChanged");

public class SimpleMyEloChanged extends MyEloChanged {

   public var windowManager:WindowManager;
   public var titleKey:IMetadataKey;

   public override function myEloChanged(window:Object, elo: IELO):Void{
      if (window!=null){
//         window.eloUri = elo.getUri();
//         var title = elo.getMetadata().getMetadataValueContainer(titleKey).getValue() as String;
//         window.title = title;
//         logger.info("set title and uri of window: {elo.getUri()}");
      }
      else{
         logger.error("can't set title and uri, because there is no window specified");
      }
   }

   public override function myEloChanged(oldUri:URI, elo: IELO):Void{
      var window = windowManager.findScyWindow(oldUri);
      if (window!=null){
         myEloChanged(window,elo);
      }
      else{
         logger.error("can't set title and uri, because there is no window: {oldUri}");
      }
   }
}
