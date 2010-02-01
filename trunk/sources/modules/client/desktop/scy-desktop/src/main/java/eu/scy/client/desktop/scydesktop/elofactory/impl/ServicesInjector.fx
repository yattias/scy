/*
 * ServicesInjector.fx
 *
 * Created on 8-jul-2009, 16:20:24
 */
package eu.scy.client.desktop.scydesktop.elofactory.impl;

import eu.scy.client.desktop.scydesktop.config.Config;

import javafx.reflect.FXClassType;
import javafx.reflect.FXLocal;
import javafx.reflect.FXVarMember;
import java.lang.Class;
import java.lang.NoSuchMethodException;

import roolo.api.IExtensionManager;
import roolo.api.IRepository;
import roolo.elo.api.IELOFactory;
import roolo.elo.api.IMetadataTypeManager;

import java.lang.Exception;

import javafx.reflect.FXType;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import eu.scy.actionlogging.api.IActionLogger;
import eu.scy.awareness.IAwarenessService;
import eu.scy.server.pedagogicalplan.PedagogicalPlanService;
import eu.scy.client.common.datasync.IDataSyncService;
import eu.scy.client.desktop.scydesktop.utils.log4j.Logger;


/**
 * @author sikkenj
 */

public class ServicesInjector {
   def logger = Logger.getLogger(this.getClass());

   public var config: Config;

   public function injectServices(object: Object) {
      if (object != null) {
         injectServiceIfWanted(object, Config.class, "config", config);

         if (config.getToolBrokerAPI() != null) {
            injectServiceIfWanted(object, ToolBrokerAPI.class, "toolBrokerAPI", config.getToolBrokerAPI());
            if (config.getToolBrokerAPI().getRepository() != null) {
               injectServiceIfWanted(object, IRepository.class, "repository", config.getToolBrokerAPI().getRepository());
            }
            if (config.getToolBrokerAPI().getExtensionManager() != null) {
               injectServiceIfWanted(object, IExtensionManager.class, "extensionManager", config.getToolBrokerAPI().getExtensionManager());
            }
            if (config.getToolBrokerAPI().getMetaDataTypeManager() != null) {
               injectServiceIfWanted(object, IMetadataTypeManager.class, "metadataTypeManager", config.getToolBrokerAPI().getMetaDataTypeManager());
            }
            if (config.getToolBrokerAPI().getELOFactory() != null) {
               injectServiceIfWanted(object, IELOFactory.class, "eloFactory", config.getToolBrokerAPI().getELOFactory());
            }
            if (config.getToolBrokerAPI().getActionLogger() != null) {
               injectServiceIfWanted(object, IActionLogger.class, "actionLogger", config.getToolBrokerAPI().getActionLogger());
            }
            if (config.getToolBrokerAPI().getAwarenessService() != null) {
               injectServiceIfWanted(object, IAwarenessService.class, "awarenessService", config.getToolBrokerAPI().getAwarenessService());
            }
            if (config.getToolBrokerAPI().getDataSyncService() != null) {
               injectServiceIfWanted(object, IDataSyncService.class, "dataSyncService", config.getToolBrokerAPI().getDataSyncService());
            }
            if (config.getToolBrokerAPI().getPedagogicalPlanService() != null) {
               injectServiceIfWanted(object, PedagogicalPlanService.class, "pedagogicalPlanService", config.getToolBrokerAPI().getPedagogicalPlanService());
            }
         } else {
            // TODO: remove this and the properties from Config, when the toolBrokerAPI is used every where
            injectServiceIfWanted(object, IELOFactory.class, "eloFactory", config.getEloFactory());
            injectServiceIfWanted(object, IMetadataTypeManager.class, "metadataTypeManager", config.getMetadataTypeManager());
            injectServiceIfWanted(object, IExtensionManager.class, "extensionManager", config.getExtensionManager());
            injectServiceIfWanted(object, IRepository.class, "repository", config.getRepository());
         }

      }
   }

   public function injectServiceIfWanted(object: Object, serviceClass: Class, propertyName: String, service: Object) {
      if (service != null) {
         injectServiceIfWantedJava(object, serviceClass, propertyName, service);
         injectServiceIfWantedFX(object, serviceClass, propertyName, service);
      }
   }

   public function injectServiceIfWantedJava(object: Object, serviceClass: Class, propertyName: String, service: Object) {
      try {
         var setterName = "set{propertyName.substring(0, 1).toUpperCase()}{propertyName.substring(1)}";
         var setServiceMethod = object.getClass().getMethod(setterName, serviceClass);
         setServiceMethod.invoke(object, service);
         logger.info("injected {serviceClass.getName()} into tool");
      } catch (e: NoSuchMethodException) {
         // no service setter method
         logger.info("tool ({object.getClass().getName()}) does not have method set{serviceClass.getSimpleName()}");
      }
   }

   public function injectServiceIfWantedFX(object: Object, serviceClass: Class, propertyName: String, service: Object) {
      var context: FXLocal.Context = FXLocal.getContext();
      var objectValue: FXLocal.ObjectValue  = new FXLocal.ObjectValue(object,context);
      var cls:FXClassType = objectValue.getClassType();
      if (not cls.isJfxType() and not cls.isMixin()){
         // object is not a javafx object
         //println("not a javafx object: {object.getClass()}");
         return;
      }
      var varRef:FXVarMember = cls.getVariable(propertyName);
      if (varRef!=null){
         var type:FXType = varRef.getType();
         //logger.info("there is a variable {propertyName} of type {type.getName()} in {object.getClass().getName()}");
         try{
            varRef.setValue(objectValue, context.mirrorOf(service));
            logger.info("the variable {propertyName} in {object.getClass().getName()} is set");
         } catch (e: Exception) {
            logger.error("failed to set FX property {propertyName} of type {type.getName()} in {object.getClass().getName()}, with type {service.getClass()}, error {e.getMessage()}");
//            logger.error("failed to set FX property {propertyName} of type {type.getName()} in {object.getClass().getName()}, with type {service.getClass()}", e);
         }
      } else {
         logger.info("no variable {propertyName} in {object.getClass().getName()}");
      }
   }
}
