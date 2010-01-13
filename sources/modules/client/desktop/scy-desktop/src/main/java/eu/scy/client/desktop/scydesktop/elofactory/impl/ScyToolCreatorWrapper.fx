/*
 * ScyToolCreatorWrapper.fx
 *
 * Created on 11-jan-2010, 12:30:20
 */

package eu.scy.client.desktop.scydesktop.elofactory.impl;
import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.config.Config;
import eu.scy.client.desktop.scydesktop.elofactory.ScyToolCreator;
import eu.scy.client.desktop.scydesktop.elofactory.ScyToolCreatorFX;
import eu.scy.client.desktop.scydesktop.elofactory.ServicesInjector;
import eu.scy.client.desktop.scydesktop.elofactory.SwingContentWrapper;
import eu.scy.client.desktop.scydesktop.utils.log4j.Logger;

/**
 * @author sikken
 */

public class ScyToolCreatorWrapper extends ScyToolCreatorFX {
   def logger = Logger.getLogger(this.getClass());

   public var config:Config on replace {injectServices()};

   public var scyToolCreator: ScyToolCreator on replace {injectServices()};

   function injectServices(){
      if (config!=null and scyToolCreator!=null){
         var servicesInjector = ServicesInjector{
            config:config;
         }
         servicesInjector.injectServices(scyToolCreator);
      }
   }


    override public function createScyToolNode () : Node {
      var component = scyToolCreator.createScyToolComponent();
      SwingContentWrapper{
         swingContent:component;
      }
    }
}
