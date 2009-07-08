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

import roolo.elo.api.IELOFactory;
import roolo.elo.api.IMetadataTypeManager;
import roolo.api.IExtensionManager;
import roolo.api.IRepository;

import java.lang.NoSuchMethodException;

import javafx.scene.Node;
import java.lang.Class;

import javafx.reflect.FXClassType;
import javafx.reflect.FXLocal;
import javafx.reflect.FXVarMember;

/**
 * @author sikkenj
 */

var logger = Logger.getLogger("eu.scy.client.desktop.elofactory.WindowContentFactory");

public class WindowContentFactory {
   public var windowContentCreatorRegistryFX: WindowContentCreatorRegistryFX;
   public var eloFactory:IELOFactory;
   public var metadataTypeManager:IMetadataTypeManager;
   public var extensionManager:IExtensionManager;
   public var repository:IRepository;

   def defaultWindowContentCreator:WindowContentCreatorFX = DummyWindowContentCreator{};

   public function fillWindowContent(eloUri:URI,scyWindow:ScyWindow){
      var type = getType(eloUri);
      var windowContentCreator = windowContentCreatorRegistryFX.getWindowContentCreatorFX(type);
      if (windowContentCreator==null){
         logger.warn("couldn't find a WindowContentCreatorFX for type {type}, now using the default");
         windowContentCreator = defaultWindowContentCreator;
      }
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
      var contentNode = windowContentCreator.getScyWindowContentNew(scyWindow);
      scyWindow.scyContent = contentNode;
      voidInspectContent(scyWindow);
   }

   function getType(eloUri:URI):String{
      if (extensionManager!=null){
         return extensionManager.getType(eloUri);
      }
      return UriUtils.getExtension(eloUri);
   }

   function voidInspectContent(scyWindow:ScyWindow){
      var scyContent = scyWindow.scyContent;
      if (scyContent instanceof ScyTool){
         scyWindow.scyTool = scyWindow.scyContent as ScyTool;
      }
      var contentClass = scyContent.getClass();
      injectServiceIfWanted(scyContent,IELOFactory.class,"eloFactory",eloFactory);
      injectServiceIfWanted(scyContent,IMetadataTypeManager.class,"metadataTypeManager",metadataTypeManager);
      injectServiceIfWanted(scyContent,IExtensionManager.class,"extensionManager",extensionManager);
      injectServiceIfWanted(scyContent,IRepository.class,"repository",repository);
   }

   function injectServiceIfWanted(scyContent: Node, serviceClass:Class,propertyName:String,service:Object){
      if (service!=null){
         injectServiceIfWantedJava(scyContent, serviceClass,propertyName,service);
         injectServiceIfWantedFX(scyContent, serviceClass,propertyName,service);
      }
   }

   function injectServiceIfWantedJava(scyContent: Node, serviceClass:Class,propertyName:String,service:Object){
      try{
         var setterName = "set{propertyName.substring(0, 1).toUpperCase()}{propertyName.substring(1)}";
         var setServiceMethod = scyContent.getClass().getMethod(setterName, serviceClass);
         setServiceMethod.invoke(scyContent, service);
         logger.info("injected {serviceClass.getName()} into tool");
      }
      catch (e:NoSuchMethodException){
         // no service setter method
         logger.info("tool ({scyContent.getClass().getName()}) does not have method set{serviceClass.getSimpleName()}");
      }
   }

   function injectServiceIfWantedFX(scyContent: Node, serviceClass:Class,propertyName:String,service:Object){
      var context:FXLocal.Context=FXLocal.getContext();
      var objectValue:FXLocal.ObjectValue = new FXLocal.ObjectValue(scyContent,context);
      var cls:FXClassType = objectValue.getClassType();
      var varRef:FXVarMember = cls.getVariable(propertyName);
      if (varRef!=null){
//         logger.info("there is a variable {propertyName} in {scyContent.getClass().getName()}");
         varRef.setValue(objectValue, context.mirrorOf(service));
         logger.info("the variable {propertyName} in {scyContent.getClass().getName()} is set");
      }
      else{
         logger.info("no variable {propertyName} in {scyContent.getClass().getName()}");
      }
   }
}
