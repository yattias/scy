/*
 * ContentFactory.fx
 *
 * Created on 23-sep-2009, 14:55:35
 */

package eu.scy.client.desktop.scydesktop.elofactory.impl;

import eu.scy.client.desktop.scydesktop.config.Config;
import java.util.HashSet;

/**
 * @author sikkenj
 */

// place your code here

public class ContentFactory{

   public var config:Config;

   def servicesInjectedSet = new HashSet();
   protected def servicesInjector = ServicesInjector{
            config:config;
         }

   protected function checkIfServicesInjected(object:Object){
      if (not servicesInjectedSet.contains(object)){
//         var servicesInjector = ServicesInjector{
//            config:config;
//         }
         servicesInjector.injectServices(object);
         servicesInjectedSet.add(object);
      }
   }
}
