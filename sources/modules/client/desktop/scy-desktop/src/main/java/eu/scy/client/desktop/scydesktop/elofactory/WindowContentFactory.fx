/*
 * WindowContentFactory.fx
 *
 * Created on 3-jul-2009, 15:25:10
 */

package eu.scy.client.desktop.scydesktop.elofactory;

import java.net.URI;

import eu.scy.client.desktop.scydesktop.elofactory.WindowContentCreatorFX;

import eu.scy.client.desktop.scydesktop.dummy.DummyWindowContentCreator;

import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;

import eu.scy.client.desktop.scydesktop.utils.UriUtils;

import org.apache.log4j.Logger;


import eu.scy.client.desktop.scydesktop.tools.ScyTool;





import eu.scy.client.desktop.scydesktop.config.Config;

import java.util.HashSet;

/**
 * @author sikkenj
 */

var logger = Logger.getLogger("eu.scy.client.desktop.elofactory.WindowContentFactory");

public class WindowContentFactory {
   public var windowContentCreatorRegistryFX: WindowContentCreatorRegistryFX;
   public var config:Config;
//   public var eloFactory:IELOFactory;
//   public var metadataTypeManager:IMetadataTypeManager;
//   public var extensionManager:IExtensionManager;
//   public var repository:IRepository;

   def defaultWindowContentCreator:WindowContentCreatorFX = DummyWindowContentCreator{};
   def servicesInjectedSet = new HashSet();

   public function fillWindowContent(eloUri:URI,scyWindow:ScyWindow){
      var type = getType(eloUri);
      var windowContentCreator = windowContentCreatorRegistryFX.getWindowContentCreatorFX(type);
      if (windowContentCreator==null){
         logger.warn("couldn't find a WindowContentCreatorFX for type {type}, now using the default");
         windowContentCreator = defaultWindowContentCreator;
      }
      checkIfServicesInjected(windowContentCreator);
      var contentNode = windowContentCreator.getScyWindowContent(eloUri, scyWindow);
      scyWindow.scyContent = contentNode;
      voidInspectContent(scyWindow);
   }

   public function fillWindowContent(scyWindow:ScyWindow){
      var type = scyWindow.eloType;
      var windowContentCreator = windowContentCreatorRegistryFX.getWindowContentCreatorFX(type);
      if (windowContentCreator==null){
         logger.warn("couldn't find a WindowContentCreatorFX for type {type}, now using the default");
         windowContentCreator = defaultWindowContentCreator;
      }
      checkIfServicesInjected(windowContentCreator);
      var contentNode = windowContentCreator.getScyWindowContentNew(scyWindow);
      scyWindow.scyContent = contentNode;
      voidInspectContent(scyWindow);
   }

   function checkIfServicesInjected(windowContentCreator:WindowContentCreatorFX){
      if (not servicesInjectedSet.contains(windowContentCreator)){
         var servicesInjector = ServicesInjector{
            config:config;
         }
         servicesInjector.injectServices(windowContentCreator);
         servicesInjectedSet.add(windowContentCreator);
      }
   }

   function getType(eloUri:URI):String{
      if (config.getExtensionManager()!=null){
         return config.getExtensionManager().getType(eloUri);
      }
      return UriUtils.getExtension(eloUri);
   }

   function voidInspectContent(scyWindow:ScyWindow){
      var scyContent = scyWindow.scyContent;
      if (scyContent instanceof ScyTool){
         scyWindow.scyTool = scyWindow.scyContent as ScyTool;
      }
      var servicesInjector = ServicesInjector{
         config:config;
      }
      servicesInjector.injectServices(scyContent);
   }
}
