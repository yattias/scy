/*
 * ContentFactory.fx
 *
 * Created on 23-sep-2009, 14:55:35
 */
package eu.scy.client.desktop.scydesktop.elofactory.impl;

import eu.scy.client.desktop.scydesktop.config.Config;
import java.util.HashSet;
import eu.scy.client.desktop.scydesktop.Initializer;

/**
 * @author sikkenj
 */
// place your code here
public class ContentFactory {

   public var config: Config;
   public var initializer: Initializer;
   def servicesInjectedSet = new HashSet();
   protected def servicesInjector = ServicesInjector {
         config: config
         initializer: initializer
      }

   protected function checkIfServicesInjected(object: Object) {
      if (not servicesInjectedSet.contains(object)) {
         if (object instanceof NeedsServiceInjection) {
            (object as NeedsServiceInjection).injectServices(servicesInjector);
         } else {
            servicesInjector.injectServices(object);
         }
         servicesInjectedSet.add(object);
      }
   }

}
