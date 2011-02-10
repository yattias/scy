package eu.scy.client.desktop.scydesktop.art.eloicons;

import eu.scy.client.desktop.scydesktop.art.WindowColorScheme;
import eu.scy.client.desktop.scydesktop.scywindows.EloIcon;

/**
 * @author lars
 */

public abstract class AbstractEloIcon extends EloIcon {

    public var windowColorScheme: WindowColorScheme;

    public override function clone(): EloIcon {
      null
   }

}
