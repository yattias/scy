/*
 * DrawerContentCreatorWrapper.fx
 *
 * Created on 22-sep-2009, 17:35:04
 */

package eu.scy.client.desktop.scydesktop.elofactory;

import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import java.net.URI;
import org.apache.log4j.Logger;

import eu.scy.client.desktop.scydesktop.config.Config;

/**
 * @author sikkenj
 */

// place your code here

def logger = Logger.getLogger("eu.scy.client.desktop.elofactory.DrawerContentCreatorWrapper");

public class DrawerContentCreatorWrapper extends DrawerContentCreatorFX {
   public var config:Config on replace {injectServices()};

   public var drawerContentCreator: DrawerContentCreator;

   function injectServices(){
      var servicesInjector = ServicesInjector{
         config:config;
      }
      servicesInjector.injectServices(drawerContentCreator);
   }


   public override function getDrawerContent(eloUri:URI, scyWindow:ScyWindow):Node{
      var component = drawerContentCreator.getDrawerContent(eloUri);
      SwingContentWrapper{
         swingContent:component;
      }
   }


}
