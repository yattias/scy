/*
 * WindowContentCreatorWrapper.fx
 *
 * Created on 3-jul-2009, 14:49:59
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
var logger = Logger.getLogger("eu.scy.client.desktop.elofactory.WindowContentCreatorWrapper");

public class WindowContentCreatorWrapper extends WindowContentCreatorFX {
   public var config:Config on replace {injectServices()};

   public var windowContentCreator: WindowContentCreator;

   function injectServices(){
      var servicesInjector = ServicesInjector{
         config:config;
      }
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


}
