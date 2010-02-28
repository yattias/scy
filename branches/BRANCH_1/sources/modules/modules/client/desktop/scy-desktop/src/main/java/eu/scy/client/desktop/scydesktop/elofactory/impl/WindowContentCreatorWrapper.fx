/*
 * WindowContentCreatorWrapper.fx
 *
 * Created on 3-jul-2009, 14:49:59
 */

package eu.scy.client.desktop.scydesktop.elofactory.impl;

import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import java.net.URI;

import eu.scy.client.desktop.scydesktop.utils.log4j.Logger;

import eu.scy.client.desktop.scydesktop.config.Config;
import eu.scy.client.desktop.scydesktop.elofactory.WindowContentCreator;
import eu.scy.client.desktop.scydesktop.elofactory.WindowContentCreatorFX;

/**
 * @author sikkenj
 */

public class WindowContentCreatorWrapper extends WindowContentCreatorFX {
   def logger = Logger.getLogger(this.getClass());

   public var config:Config on replace {injectServices()};

   public var windowContentCreator: WindowContentCreator on replace {injectServices()};

   function injectServices(){
      if (config!=null and windowContentCreator!=null){
         var servicesInjector = ServicesInjector{
            config:config;
         }
         servicesInjector.injectServices(windowContentCreator);
      }
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
