/*
 * ServicesInjector.fx
 *
 * Created on 8-jul-2009, 16:20:24
 */
package eu.scy.client.desktop.scydesktop.elofactory.impl;

import eu.scy.client.desktop.scydesktop.config.Config;

import java.lang.Class;

import roolo.api.IExtensionManager;
import roolo.api.IRepository;
import roolo.elo.api.IELOFactory;
import roolo.elo.api.IMetadataTypeManager;


import eu.scy.toolbrokerapi.ToolBrokerAPI;
import eu.scy.actionlogging.api.IActionLogger;
import eu.scy.awareness.IAwarenessService;
import eu.scy.server.pedagogicalplan.PedagogicalPlanService;
import eu.scy.client.common.datasync.IDataSyncService;
import eu.scy.client.desktop.desktoputils.log4j.Logger;
import eu.scy.client.desktop.scydesktop.Initializer;
import eu.scy.client.desktop.desktoputils.InjectObjectsUtils;
import eu.scy.client.desktop.scydesktop.scywindows.ShowMoreInfo;
import java.lang.System;
import eu.scy.client.desktop.desktoputils.ActivityTimer;


/**
 * @author sikkenj
 */

public class ServicesInjector {
   def logger = Logger.getLogger(this.getClass());

   public var config: Config;
   public var initializer:Initializer;
   public var showMoreInfo: ShowMoreInfo;

   public function injectServices(object: Object) {
      if (object != null) {
         def activityTimer = new ActivityTimer("injectServices({object})","injecting");
//         injectServiceIfWanted(object, Config.class, "config", config);

         if (config.getToolBrokerAPI() != null) {
            injectServiceIfWanted(object, ToolBrokerAPI.class, "toolBrokerAPI", config.getToolBrokerAPI());
//            if (config.getToolBrokerAPI().getRepository() != null) {
//               injectServiceIfWanted(object, IRepository.class, "repository", config.getToolBrokerAPI().getRepository());
//            }
//            if (config.getToolBrokerAPI().getExtensionManager() != null) {
//               injectServiceIfWanted(object, IExtensionManager.class, "extensionManager", config.getToolBrokerAPI().getExtensionManager());
//            }
//            if (config.getToolBrokerAPI().getMetaDataTypeManager() != null) {
//               injectServiceIfWanted(object, IMetadataTypeManager.class, "metadataTypeManager", config.getToolBrokerAPI().getMetaDataTypeManager());
//            }
//            if (config.getToolBrokerAPI().getELOFactory() != null) {
//               injectServiceIfWanted(object, IELOFactory.class, "eloFactory", config.getToolBrokerAPI().getELOFactory());
//            }
//            if (config.getToolBrokerAPI().getActionLogger() != null) {
//               injectServiceIfWanted(object, IActionLogger.class, "actionLogger", config.getToolBrokerAPI().getActionLogger());
//            }
//            if (config.getToolBrokerAPI().getAwarenessService() != null) {
//               injectServiceIfWanted(object, IAwarenessService.class, "awarenessService", config.getToolBrokerAPI().getAwarenessService());
//            }
//            if (config.getToolBrokerAPI().getDataSyncService() != null) {
//               injectServiceIfWanted(object, IDataSyncService.class, "dataSyncService", config.getToolBrokerAPI().getDataSyncService());
//            }
//            if (config.getToolBrokerAPI().getPedagogicalPlanService() != null) {
//               injectServiceIfWanted(object, PedagogicalPlanService.class, "pedagogicalPlanService", config.getToolBrokerAPI().getPedagogicalPlanService());
//            }
         }
         injectServiceIfWanted(object,Boolean.class,"authorMode",initializer.authorMode);
         injectServiceIfWanted(object,ShowMoreInfo.class,"showMoreInfo",showMoreInfo);
         activityTimer.endActivity();
      }
   }

   public function injectServiceIfWanted(object: Object, serviceClass: Class, propertyName: String, service: Object) {
      if (service != null) {
         InjectObjectsUtils.injectObjectIfWantedJava(object, serviceClass, propertyName, service);
         InjectObjectsUtils.injectObjectIfWantedFX(object, serviceClass, propertyName, service);
      }
   }

}
