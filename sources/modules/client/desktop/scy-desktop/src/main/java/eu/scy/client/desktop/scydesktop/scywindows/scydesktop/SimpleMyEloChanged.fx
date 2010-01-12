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

import eu.scy.client.desktop.scydesktop.utils.log4j.Logger;


/**
 * @author sikken
 */

public class SimpleMyEloChanged extends MyEloChanged {
   def logger = Logger.getLogger(this.getClass());

   public var window:ScyWindow;
   public var titleKey:IMetadataKey;
   public var technicalFormatKey:IMetadataKey;

   public override function myEloChanged(elo: IELO):Void{
      window.eloUri = elo.getUri();
      var title = elo.getMetadata().getMetadataValueContainer(titleKey).getValue() as String;
      window.title = title;
      var eloType = elo.getMetadata().getMetadataValueContainer(technicalFormatKey).getValue() as String;
      window.eloType = eloType;
      window.scyToolsList.loadedEloChanged(window.eloUri);
      logger.info("set title and uri of window: {elo.getUri()}");
   }
}
