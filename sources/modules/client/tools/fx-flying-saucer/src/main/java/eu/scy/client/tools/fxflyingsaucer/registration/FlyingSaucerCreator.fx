/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.tools.fxflyingsaucer.registration;

import eu.scy.client.desktop.scydesktop.elofactory.ScyToolCreatorFX;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import javafx.scene.Node;
import eu.scy.client.tools.fxflyingsaucer.UrlSource;
import eu.scy.client.desktop.scydesktop.tools.DrawerUIIndicator;
import eu.scy.client.tools.fxflyingsaucer.EloFlyingSaucerPanel;
import eu.scy.client.tools.fxflyingsaucer.FlyingSaucerBrowser;
import eu.scy.client.desktop.scydesktop.tooltips.BubbleKey;

/**
 * @author weinbrenner
 */
public class FlyingSaucerCreator extends ScyToolCreatorFX {

   public-init var urlSource: UrlSource;
   public-init var drawerUIIndicator: DrawerUIIndicator;
   public-init var bubbleKey: BubbleKey;

   override public function createScyToolNode(eloType: String, creatorId: String, scyWindow: ScyWindow, windowContent: Boolean): Node {
      if (urlSource == null) {
         urlSource = urlSource.ELO;
         drawerUIIndicator = DrawerUIIndicator.ASSIGNMENT; // ?
      }

      def flyingSaucer = new EloFlyingSaucerPanel(urlSource, drawerUIIndicator, bubbleKey);
      return FlyingSaucerBrowser {
                 urlSource: urlSource;
                 flyingSaucer: flyingSaucer;
                 scyWindow: scyWindow;
              };
   }

}
