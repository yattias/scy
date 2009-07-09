/*
 * ServicesInjector.fx
 *
 * Created on 8-jul-2009, 16:20:24
 */

package eu.scy.client.desktop.scydesktop.elofactory;

import eu.scy.client.desktop.scydesktop.config.Config;

import javafx.reflect.FXClassType;
import javafx.reflect.FXLocal;
import javafx.reflect.FXVarMember;
import java.lang.Class;
import java.lang.NoSuchMethodException;
import org.apache.log4j.Logger;

import roolo.api.IExtensionManager;
import roolo.api.IRepository;
import roolo.elo.api.IELOFactory;
import roolo.elo.api.IMetadataTypeManager;

import java.lang.Exception;

import javafx.reflect.FXType;

/**
 * @author sikkenj
 */

var logger = Logger.getLogger("eu.scy.client.desktop.elofactory.ServicesInjector");

public class ServicesInjector {
   public var config:Config;

   public function injectServices(object:Object){
      if (object!=null){
         injectServiceIfWanted(object,Config.class,"config",config);
         injectServiceIfWanted(object,IELOFactory.class,"eloFactory",config.getEloFactory());
         injectServiceIfWanted(object,IMetadataTypeManager.class,"metadataTypeManager",config.getMetadataTypeManager());
         injectServiceIfWanted(object,IExtensionManager.class,"extensionManager",config.getExtensionManager());
         injectServiceIfWanted(object,IRepository.class,"repository",config.getRepository());
      }
   }

   function injectServiceIfWanted(object:Object, serviceClass:Class,propertyName:String,service:Object){
      if (service!=null){
         injectServiceIfWantedJava(object, serviceClass,propertyName,service);
         injectServiceIfWantedFX(object, serviceClass,propertyName,service);
      }
   }

   function injectServiceIfWantedJava(object:Object, serviceClass:Class,propertyName:String,service:Object){
      try{
         var setterName = "set{propertyName.substring(0, 1).toUpperCase()}{propertyName.substring(1)}";
         var setServiceMethod = object.getClass().getMethod(setterName, serviceClass);
         setServiceMethod.invoke(object, service);
         logger.info("injected {serviceClass.getName()} into tool");
      }
      catch (e:NoSuchMethodException){
         // no service setter method
         logger.info("tool ({object.getClass().getName()}) does not have method set{serviceClass.getSimpleName()}");
      }
   }

   function injectServiceIfWantedFX(object:Object, serviceClass:Class,propertyName:String,service:Object){
      var context:FXLocal.Context=FXLocal.getContext();
      var objectValue:FXLocal.ObjectValue = new FXLocal.ObjectValue(object,context);
      var cls:FXClassType = objectValue.getClassType();
      var varRef:FXVarMember = cls.getVariable(propertyName);
      if (varRef!=null){
         var type:FXType = varRef.getType();
         //logger.info("there is a variable {propertyName} of type {type.getName()} in {object.getClass().getName()}");
         try{
            varRef.setValue(objectValue, context.mirrorOf(service));
            logger.info("the variable {propertyName} in {object.getClass().getName()} is set");
         }
         catch (e:Exception){
            logger.error("failed to set FX property {propertyName} of type {type.getName()} in {object.getClass().getName()}, with type {service.getClass()}", e);
         }
      }
      else{
         logger.info("no variable {propertyName} in {object.getClass().getName()}");
      }
   }
}
