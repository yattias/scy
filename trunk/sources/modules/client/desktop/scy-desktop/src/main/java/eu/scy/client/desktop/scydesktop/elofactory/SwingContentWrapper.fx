/*
 * SwingWindowContentWrapper.fx
 *
 * Created on 6-jul-2009, 11:01:54
 */

package eu.scy.client.desktop.scydesktop.elofactory;

import javafx.scene.CustomNode;
import javafx.scene.Node;

import javax.swing.JComponent;

import javafx.ext.swing.SwingComponent;

import java.lang.Class;
import java.lang.NoSuchMethodException;
import org.apache.log4j.Logger;

import roolo.elo.api.IELOFactory;
import roolo.elo.api.IMetadataTypeManager;
import roolo.api.IExtensionManager;
import roolo.api.IRepository;

/**
 * @author sikken
 */

// place your code here
var logger = Logger.getLogger("eu.scy.client.desktop.elofactory.SwingContentWrapper");

public class SwingContentWrapper extends CustomNode {
   public var swingContent: JComponent;
   public var eloFactory:IELOFactory on replace{realSetEloFactory(eloFactory)};
   public var metadataTypeManager:IMetadataTypeManager on replace{realSetMetadataTypeManager(metadataTypeManager)};
   public var extensionManager:IExtensionManager on replace{realSetExtensionManager(extensionManager)};
   public var repository:IRepository on replace{realSetRepository(repository)};

   public override function create(): Node {
      return SwingComponent.wrap(swingContent);
   }

   function realSetEloFactory(eloFactory:IELOFactory):Void{
      injectServiceIfWanted(IELOFactory.class,"eloFactory",eloFactory);
   }

   function realSetMetadataTypeManager(metadataTypeManager:IMetadataTypeManager):Void{
      injectServiceIfWanted(IMetadataTypeManager.class,"metadataTypeManager",metadataTypeManager);
   }

   function realSetExtensionManager(extensionManager:IExtensionManager):Void{
      injectServiceIfWanted(IExtensionManager.class,"extensionManager",extensionManager);
   }

   function realSetRepository(repository:IRepository):Void{
      injectServiceIfWanted(IRepository.class,"repository",repository);
   }


   function injectServiceIfWanted(serviceClass:Class,propertyName:String,service:Object){
      try{
         var setterName = "set{propertyName.substring(0, 1).toUpperCase()}{propertyName.substring(1)}";
         var setServiceMethod = swingContent.getClass().getMethod(setterName, serviceClass);
         setServiceMethod.invoke(swingContent, service);
         logger.info("injected {serviceClass.getName()} into component");
      }
      catch (e:NoSuchMethodException){
         // no service setter method
         logger.info("component does not want {serviceClass.getName()}");
      }
   }
}

