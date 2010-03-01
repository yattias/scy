/*
 * DrawerContentCreatorWrapper.fx
 *
 * Created on 22-sep-2009, 17:35:04
 */

package eu.scy.client.desktop.scydesktop.elofactory.impl;

import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import java.net.URI;
import eu.scy.client.desktop.scydesktop.utils.log4j.Logger;

import eu.scy.client.desktop.scydesktop.config.Config;
import eu.scy.client.desktop.scydesktop.elofactory.DrawerContentCreator;
import eu.scy.client.desktop.scydesktop.elofactory.DrawerContentCreatorFX;

/**
 * @author sikkenj
 */

// place your code here

public class DrawerContentCreatorWrapper extends DrawerContentCreatorFX {
   def logger = Logger.getLogger(this.getClass());

   public var config:Config on replace {injectServices()};

   public var drawerContentCreator: DrawerContentCreator on replace {injectServices()};

   function injectServices(){
      if (config!=null and drawerContentCreator!=null){
         var servicesInjector = ServicesInjector{
            config:config;
         }
         servicesInjector.injectServices(drawerContentCreator);
      }
   }

   public override function getDrawerContent(eloUri:URI, scyWindow:ScyWindow):Node{
      var component = drawerContentCreator.getDrawerContent(eloUri);
      SwingContentWrapper{
         swingContent:component;
      }
   }


}
