/*
 * BackgroundUpdater.fx
 *
 * Created on 16-mrt-2010, 17:14:13
 */

package eu.scy.client.desktop.scydesktop;
import eu.scy.client.desktop.scydesktop.tools.corner.missionmap.Las;
import eu.scy.client.desktop.scydesktop.uicontrols.DynamicTypeBackground;
import eu.scy.client.desktop.scydesktop.scywindows.scydesktop.EloInfoControl;

/**
 * @author sikken
 */

public class BackgroundUpdater {
   public-init var background: DynamicTypeBackground;
   public var activeLas:Las on replace {updateBackground()}
   public-init var eloInfoControl: EloInfoControl;

   init{
      updateBackground();
   }

   function updateBackground():Void{
      var type = eloInfoControl.getEloType(activeLas.mainAnchor.eloUri);
      background.type = type;
   }

}
