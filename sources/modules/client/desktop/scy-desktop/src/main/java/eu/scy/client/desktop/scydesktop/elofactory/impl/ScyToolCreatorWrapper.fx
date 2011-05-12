/*
 * ScyToolCreatorWrapper.fx
 *
 * Created on 11-jan-2010, 12:30:20
 */
package eu.scy.client.desktop.scydesktop.elofactory.impl;

import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.elofactory.ScyToolCreator;
import eu.scy.client.desktop.scydesktop.elofactory.ScyToolCreatorFX;
import eu.scy.client.desktop.desktoputils.log4j.Logger;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;

/**
 * @author sikken
 */
public class ScyToolCreatorWrapper extends ScyToolCreatorFX, NeedsServiceInjection {

   def logger = Logger.getLogger(this.getClass());
   public-init var scyToolCreator: ScyToolCreator;

   override public function injectServices(servicesInjector: ServicesInjector): Void {
      servicesInjector.injectServices(scyToolCreator);
   }

   override public function createScyToolNode(eloType: String, creatorId: String, scyWindow: ScyWindow, windowContent: Boolean): Node {
      var component = scyToolCreator.createScyToolComponent(eloType, creatorId, windowContent);
      var preferredSize = component.getPreferredSize();
      //      println("pref size component: {preferredSize}");
      SwingContentWrapper {
         swingContent: component;
         width: preferredSize.width
         height: preferredSize.height
      }
   }

}
