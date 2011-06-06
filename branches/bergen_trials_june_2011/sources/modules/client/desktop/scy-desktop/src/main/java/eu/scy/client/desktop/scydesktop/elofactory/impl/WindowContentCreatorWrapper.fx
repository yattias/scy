/*
 * WindowContentCreatorWrapper.fx
 *
 * Created on 3-jul-2009, 14:49:59
 */

package eu.scy.client.desktop.scydesktop.elofactory.impl;

import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import java.net.URI;

import eu.scy.client.desktop.desktoputils.log4j.Logger;

import eu.scy.client.desktop.scydesktop.elofactory.WindowContentCreator;
import eu.scy.client.desktop.scydesktop.elofactory.WindowContentCreatorFX;

/**
 * @author sikkenj
 */

public class WindowContentCreatorWrapper extends WindowContentCreatorFX, NeedsServiceInjection {
   def logger = Logger.getLogger(this.getClass());

   public-init var windowContentCreator: WindowContentCreator;

   override public function injectServices(servicesInjector: ServicesInjector): Void{
      servicesInjector.injectServices(windowContentCreator);
   }

   public override function getScyWindowContent(eloUri:URI, scyWindow:ScyWindow):Node{
      var component = windowContentCreator.getScyWindowContent(eloUri);
      SwingContentWrapper{
         swingContent:component;
      }
   }

   public override function getScyWindowContentNew(scyWindow:ScyWindow):Node{
      var component = windowContentCreator.getScyWindowContentNew();
      SwingContentWrapper{
         swingContent:component;
      }
   }

   public override function supportType(type: String): Boolean{
      return windowContentCreator.supportType(type);
   }
}
