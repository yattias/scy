/*
 * BackgroundUpdater.fx
 *
 * Created on 16-mrt-2010, 17:14:13
 */

package eu.scy.client.desktop.scydesktop;
import eu.scy.client.desktop.scydesktop.tools.corner.missionmap.LasFX;
import eu.scy.client.desktop.scydesktop.uicontrols.DynamicTypeBackground;
import eu.scy.client.desktop.scydesktop.scywindows.WindowStyler;

/**
 * @author sikken
 */

public class BackgroundUpdater {
   public-init var background: DynamicTypeBackground;
   public var activeLas:LasFX on replace {updateBackground()}
   public-init var windowStyler: WindowStyler;


   init{
      updateBackground();
   }

   function updateBackground():Void{
      var type = windowStyler.getDisplayIconType(activeLas.mainAnchor.scyElo);
      background.type = type;
   }

}
