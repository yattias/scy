/*
 * SimpleMyEloChanged.fx
 *
 * Created on 1-dec-2009, 13:10:07
 */

package eu.scy.client.desktop.scydesktop.scywindows.scydesktop;

import eu.scy.client.desktop.scydesktop.tools.MyEloChanged;

import roolo.elo.api.IELO;

import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;

import roolo.elo.api.IMetadataKey;

import org.apache.log4j.Logger;


/**
 * @author sikken
 */

def logger = Logger.getLogger("eu.scy.client.desktop.scydesktop.scywindows.scydesktop.SimpleMyEloChanged");

public class SimpleMyEloChanged extends MyEloChanged {

   public var window:ScyWindow;
   public var titleKey:IMetadataKey;

   public override function myEloChanged(elo: IELO):Void{
      window.eloUri = elo.getUri();
      var title = elo.getMetadata().getMetadataValueContainer(titleKey).getValue() as String;
      window.title = title;
      logger.info("set title and uri of window: {elo.getUri()}");
   }
}
