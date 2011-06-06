/*
 * DrawerContentCreatorWrapper.fx
 *
 * Created on 22-sep-2009, 17:35:04
 */
package eu.scy.client.desktop.scydesktop.elofactory.impl;

import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import java.net.URI;
import eu.scy.client.desktop.desktoputils.log4j.Logger;
import eu.scy.client.desktop.scydesktop.elofactory.DrawerContentCreator;
import eu.scy.client.desktop.scydesktop.elofactory.DrawerContentCreatorFX;

/**
 * @author sikkenj
 */
// place your code here
public class DrawerContentCreatorWrapper extends DrawerContentCreatorFX, NeedsServiceInjection {

   def logger = Logger.getLogger(this.getClass());
   public-init var drawerContentCreator: DrawerContentCreator;

   override public function injectServices(servicesInjector: ServicesInjector): Void {
      servicesInjector.injectServices(drawerContentCreator);
   }

   public override function getDrawerContent(eloUri: URI, scyWindow: ScyWindow): Node {
      var component = drawerContentCreator.getDrawerContent(eloUri);
      SwingContentWrapper {
         swingContent: component;
      }
   }

}
