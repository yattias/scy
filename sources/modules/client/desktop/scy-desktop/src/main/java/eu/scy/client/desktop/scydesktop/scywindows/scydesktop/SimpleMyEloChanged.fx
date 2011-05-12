/*
 * SimpleMyEloChanged.fx
 *
 * Created on 1-dec-2009, 13:10:07
 */
package eu.scy.client.desktop.scydesktop.scywindows.scydesktop;

import eu.scy.client.desktop.scydesktop.tools.MyEloChanged;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import eu.scy.client.desktop.desktoputils.log4j.Logger;
import eu.scy.common.scyelo.ScyElo;

/**
 * @author sikken
 */
public class SimpleMyEloChanged extends MyEloChanged {

   def logger = Logger.getLogger(this.getClass());
   public var window: ScyWindow;

   public override function myEloChanged(scyElo: ScyElo): Void {
      window.scyElo = scyElo;
      window.title = scyElo.getTitle();
      window.eloType = scyElo.getTechnicalFormat();
      window.eloUri = scyElo.getUri();
      window.scyToolsList.loadedEloChanged(window.eloUri);
      logger.info("set title and uri of window: {scyElo.getUri()}");
   }

}
