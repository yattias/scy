/*
 * ScyDesktopCreator.fx
 *
 * Created on 7-jul-2009, 14:23:46
 */
package eu.scy.client.desktop.scydesktop;

import eu.scy.client.desktop.scydesktop.config.Config;
import eu.scy.client.desktop.scydesktop.scywindows.WindowStyler;
import eu.scy.client.desktop.scydesktop.tools.corner.missionmap.MissionModelFX;
import eu.scy.client.desktop.scydesktop.tools.corner.missionmap.MissionAnchorFX;
import eu.scy.client.desktop.scydesktop.config.SpringConfigFactory;
import java.lang.IllegalStateException;
import eu.scy.client.desktop.scydesktop.elofactory.NewEloCreationRegistry;
import eu.scy.client.desktop.scydesktop.elofactory.impl.NewEloCreationRegistryImpl;
import eu.scy.client.desktop.scydesktop.elofactory.DrawerContentCreatorRegistryFX;
import eu.scy.client.desktop.scydesktop.utils.log4j.Logger;
import javafx.scene.paint.Color;
import java.lang.System;
import eu.scy.client.desktop.scydesktop.config.BasicConfig;
import eu.scy.client.desktop.scydesktop.elofactory.ScyToolCreatorRegistryFX;
import eu.scy.client.desktop.scydesktop.elofactory.impl.ScyToolCreatorRegistryFXImpl;
import eu.scy.toolbrokerapi.ToolBrokerAPIRuntimeSetting;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import java.net.URI;
import eu.scy.client.desktop.scydesktop.elofactory.EloConfigManager;
import eu.scy.client.desktop.scydesktop.elofactory.impl.BasicEloConfigManager;
import eu.scy.client.desktop.scydesktop.mission.MissionRunConfigs;
import eu.scy.client.desktop.desktoputils.art.ScyColors;
import eu.scy.client.desktop.desktoputils.art.WindowColorScheme;
import eu.scy.client.desktop.scydesktop.elofactory.impl.BasicEloToolConfigManager;
import eu.scy.common.mission.EloSystemRole;
import eu.scy.common.scyelo.ScyRooloMetadataKeyIds;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.common.mission.MissionSpecificationElo;
import eu.scy.common.mission.impl.ApplyEloToolConfigDefaults;
import eu.scy.client.desktop.scydesktop.utils.ActivityTimer;
import roolo.elo.api.IMetadata;
import java.util.List;
import java.util.ArrayList;
import eu.scy.client.desktop.scydesktop.imagewindowstyler.JavaFxWindowStyler;
import eu.scy.client.desktop.desktoputils.art.eloicons.EloIconFactory;
import eu.scy.client.desktop.scydesktop.scywindows.scydesktop.DialogBox;

/**
 * @author sikkenj
 */
public class ScyDesktopCreator {

   def activityTimer = new ActivityTimer("ScyDesktopCreator", "creating");
   def logger = Logger.getLogger(this.getClass());
   public-init var initializer: Initializer;
   public-init var missionRunConfigs: MissionRunConfigs;
   public-init var config: Config;
   public-init var missionModelFX: MissionModelFX;
   public-init var windowStyler: WindowStyler;
   public-init var scyToolCreatorRegistryFX: ScyToolCreatorRegistryFX;
   public-init var newEloCreationRegistry: NewEloCreationRegistry;
   public-init var drawerContentCreatorRegistryFX: DrawerContentCreatorRegistryFX;
   public-init var eloConfigManager: EloConfigManager;
   var templateEloUris: URI[];
   def userNameKey = "userName";
   var titleKey: IMetadataKey;
   var technicalFormatKey: IMetadataKey;
   var missionIdKey: IMetadataKey;
   var anchorIdKey: IMetadataKey;
   var lasKey: IMetadataKey;
   var containsAssignmentEloKey: IMetadataKey;
   var functionalTypeKey: IMetadataKey;
   var iconTypeKey: IMetadataKey;
   def toolBrokerAPI = missionRunConfigs.tbi;
   def userName = toolBrokerAPI.getLoginUserName();

   init {
      activityTimer.startActivity("findConfig");
      findConfig();
      activityTimer.startActivity("findMetadataKeys");
      findMetadataKeys();
      activityTimer.startActivity("creating components");
      if (windowStyler == null) {
         //         windowStyler = ImageWindowStyler {
         //               eloTypeControl: eloDisplayTypeControl;
         //               impagesPath: initializer.eloImagesPath
         //               repository: config.getRepository()
         //               metadataTypeManager: config.getMetadataTypeManager()
         //            };
//         windowStyler = FxdWindowStyler {
//               //               impagesPath: initializer.eloImagesPath
//               repository: config.getRepository()
//               metadataTypeManager: config.getMetadataTypeManager()
//            };
         windowStyler = JavaFxWindowStyler{
            eloIconFactory: EloIconFactory{}
         }
      }
      var scyToolCreatorRegistryFXImpl = ScyToolCreatorRegistryFXImpl {
            config: config;
         }
      if (scyToolCreatorRegistryFX == null) {
         scyToolCreatorRegistryFX = scyToolCreatorRegistryFXImpl;
      }
      if (newEloCreationRegistry == null) {
         newEloCreationRegistry = NewEloCreationRegistryImpl {};
      }
      if (drawerContentCreatorRegistryFX == null) {
         drawerContentCreatorRegistryFX = scyToolCreatorRegistryFXImpl;
      }
      if (eloConfigManager == null) {
         def eloToolConfigs = missionRunConfigs.missionRuntimeModel.getEloToolConfigsElo().getTypedContent().getEloToolConfigs();
         if (eloToolConfigs.size() > 0) {
            eloConfigManager = new BasicEloToolConfigManager(missionRunConfigs.missionRuntimeModel.getEloToolConfigsElo().getTypedContent());
         } else {
            eloConfigManager = new BasicEloConfigManager(config);
         }
      }

      activityTimer.startActivity("handleMissionRunConfigs");
      handleMissionRunConfigs();

//      findMission();
//      handleToolRegistration();
//
//      if (missionModelFX == null) {
//         readMissionModel();
//      }
      if (toolBrokerAPI instanceof ToolBrokerAPIRuntimeSetting) {
         var toolBrokerAPIRuntimeSetting = toolBrokerAPI as ToolBrokerAPIRuntimeSetting;
      }
      activityTimer.endActivity();
   }

   function findConfig() {
      if (config == null) {
         // set properties, to make them avaible in spring config files
         System.setProperty(userNameKey, userName);

         var springConfigFactory = new SpringConfigFactory();
         if (initializer.scyDesktopConfigFile != null) {
            logger.info("reading spring config from class path: {initializer.scyDesktopConfigFile}");
            springConfigFactory.initFromClassPath(initializer.scyDesktopConfigFile);
         } else {
            throw new IllegalStateException("no spring config location defined");
         }

         var basicConfig = springConfigFactory.getConfig() as BasicConfig;
         if (toolBrokerAPI != null) {
            basicConfig.setToolBrokerAPI(toolBrokerAPI);
            (toolBrokerAPI as ToolBrokerAPIRuntimeSetting).setMissionRuntimeURI(missionRunConfigs.missionRuntimeModel.getMissionRuntimeElo().getUri());
         }
         config = basicConfig;
      }
      if (config == null) {
         throw new IllegalStateException("config is not defined and could not be found");
      }
   }

   function findMetadataKeys() {
      titleKey = findMetadataKey(CoreRooloMetadataKeyIds.TITLE.getId());
      technicalFormatKey = findMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT.getId());
      missionIdKey = findMetadataKey(ScyRooloMetadataKeyIds.MISSION.getId());
      anchorIdKey = findMetadataKey(ScyRooloMetadataKeyIds.ANCHOR_ID.getId());
      lasKey = findMetadataKey(ScyRooloMetadataKeyIds.LAS.getId());
      containsAssignmentEloKey = findMetadataKey(ScyRooloMetadataKeyIds.CONTAINS_ASSIGMENT_ELO.getId());
      functionalTypeKey = findMetadataKey(ScyRooloMetadataKeyIds.FUNCTIONAL_TYPE.getId());
      iconTypeKey = findMetadataKey(ScyRooloMetadataKeyIds.ICON_TYPE.getId());
   }

   function findMetadataKey(id: String): IMetadataKey {
      var key = config.getMetadataTypeManager().getMetadataKey(id);
      if (key == null) {
         throw new IllegalStateException("the metadata key cannot be found, id: {id}");
      }
      return key;
   }

   function handleMissionRunConfigs(): Void {
      activityTimer.startActivity("create MissionModelFX");
      if (windowStyler instanceof JavaFxWindowStyler){
         def javaFxWindowStyler = windowStyler as JavaFxWindowStyler;
         javaFxWindowStyler.colorSchemesElo = missionRunConfigs.missionRuntimeModel.getColorSchemesElo();
      }

      missionModelFX = missionRunConfigs.missionMapModel;
      if (missionModelFX == null) {
         missionModelFX = MissionModelFX {
            }
      }
      if (initializer.usingRooloCache) {
         activityTimer.startActivity("cache elos");
         // load all elos in one call into the roolo cache
         var anchorEloUris = missionModelFX.getEloUris(false);
         def templateEloUriList = missionRunConfigs.missionRuntimeModel.getTemplateElosElo().getTypedContent().getTemplateEloUris();
         if (templateEloUriList.size() > 0) {
            for (templateEloUri in templateEloUriList) {
               anchorEloUris.add(templateEloUri);
            }
         }
         config.getRepository().retrieveELOs(anchorEloUris);
      }
      activityTimer.startActivity("addEloInformationToMissionModel");
      addEloInformationToMissionModel();
      activityTimer.startActivity("handleEloToolConfigs");
      handleEloToolConfigs();
      activityTimer.startActivity("handleTemplateElos");
      handleTemplateElos();
      activityTimer.startActivity("print mission elo info");
      def missionSpecificationEloUri = missionRunConfigs.missionRuntimeModel.getMissionRuntimeElo().getTypedContent().getMissionSpecificationEloUri();
      logger.info("missionRunConfigs elos:\n""- mission specification : {missionSpecificationEloUri}\n""- mission runtime       : {missionRunConfigs.missionRuntimeModel.getMissionRuntimeElo().getUri()}\n""- mission map model     : {missionRunConfigs.missionMapModel.getMissionModelElo().getUri()}\n""- elo tool configs      : {missionRunConfigs.missionRuntimeModel.getEloToolConfigsElo().getUri()}\n""- template elos         : {missionRunConfigs.missionRuntimeModel.getTemplateElosElo().getUri()}\n""- runtime settings      : {missionRunConfigs.missionRuntimeModel.getRuntimeSettingsElo().getUri()}\n""- ePortfolio            : {missionRunConfigs.missionRuntimeModel.getMissionRuntimeElo().getTypedContent().getEPortfolioEloUri()}\n""- colorSchemes            : {missionRunConfigs.missionRuntimeModel.getColorSchemesElo().getUri()}");
      if (missionSpecificationEloUri != null) {
         def missionSpecificationElo = MissionSpecificationElo.loadElo(missionSpecificationEloUri, missionRunConfigs.tbi);
         def misssionSpecification = missionSpecificationElo.getTypedContent();
         logger.info("mission specification elos:\n""- mission map model : {misssionSpecification.getMissionMapModelEloUri()}\n""- elo tool configs  : {misssionSpecification.getEloToolConfigsEloUri()}\n""- template elos     : {misssionSpecification.getTemplateElosEloUri()}\n""- runtime settings  : {misssionSpecification.getRuntimeSettingsEloUri()}\n""- colorSchems  : {misssionSpecification.getColorSchemesEloUri()}");
      }
      if (initializer.debugMode) {
         activityTimer.startActivity("printConfiguration");
         printConfiguration();
      }

   }

   function addEloInformationToMissionModel(): Void {
      for (las in missionModelFX.lasses) {
         addEloInformationToMissionAnchor(las.mainAnchor);
         for (anchor in las.intermediateAnchors) {
            addEloInformationToMissionAnchor(anchor);
         }
         las.exists = las.mainAnchor.exists;
      }
   }

   function addEloInformationToMissionAnchor(missionAnchor: MissionAnchorFX): Void {
      if (missionAnchor.eloUri != null and missionAnchor.eloUri.toString() != "") {
         missionAnchor.windowColorScheme = windowStyler.getWindowColorScheme(missionAnchor.scyElo);
      } else {
         missionAnchor.windowColorScheme = WindowColorScheme.getWindowColorScheme(ScyColors.darkGray);
      }

      if (missionAnchor.scyElo != null) {
         missionAnchor.exists = true;
         missionAnchor.title = missionAnchor.scyElo.getTitle();
      } else {
         missionAnchor.exists = false;
         // change the color, to show the elo does not exists
         missionAnchor.windowColorScheme = WindowColorScheme.getWindowColorScheme(ScyColors.darkGray);
      }
   }

   function getNotExistingColor(color: Color): Color {
      Color.color(getLighterColorComponent(color.red), getLighterColorComponent(color.green), getLighterColorComponent(color.blue));
   }

   function getLighterColorComponent(value: Number): Number {
      return 1 - (1 - value) / 2.0;
   }

   function handleEloToolConfigs() {
      var eloToolConfigs = missionRunConfigs.missionRuntimeModel.getEloToolConfigsElo().getTypedContent().getEloToolConfigs();
      if (eloToolConfigs.size() > 0) {
         for (eloToolConfig in eloToolConfigs) {
            if ((initializer.authorMode or eloToolConfig.getEloSystemRole() == EloSystemRole.USER)
               and ApplyEloToolConfigDefaults.defaultEloToolConfigType != eloToolConfig.getEloType()) {
               newEloCreationRegistry.registerEloCreation(eloToolConfig.getEloType());
            }
         }
      } else {
         // no eloToolConfigs defined in the mission runtime, use the ones defined in the config
         for (newEloType in config.getNewEloTypes()) {
            newEloCreationRegistry.registerEloCreation(newEloType);
         }
      }
   }

   function handleTemplateElos() {
      def templateEloUriList = missionRunConfigs.missionRuntimeModel.getTemplateElosElo().getTypedContent().getTemplateEloUris();
      if (templateEloUriList.size() > 0) {
         for (templateEloUri in templateEloUriList) {
            def metadata = config.getRepository().retrieveMetadata(templateEloUri);
            if (metadata != null) {
               def templateEloUriType = metadata.getMetadataValueContainer(technicalFormatKey).getValue() as String;
               if (newEloCreationRegistry.containsEloType(templateEloUriType)) {
                  insert templateEloUri into templateEloUris;
               } else {
                  logger.warn("skipped template elo, because the type ({templateEloUriType}) ) is not configured for the user: {templateEloUri}");
               }
            } else {
               logger.warn("could not find template elo: {templateEloUri}");
            }

         }
      }
   }

   function printConfiguration(): Void {
      println("\nThe list of all elo uris, used in mission map runtime");
      printEloInfos(missionModelFX.getEloUris(false));
      println("\nThe list of template elo uris");
      def eloUris = new ArrayList();
      for (uri in templateEloUris) {
         eloUris.add(uri)
      }
      printEloInfos(eloUris);
      println("");
      println("Nr of anchor elos: {missionModelFX.getEloUris(false).size()}");
      println("Nr of all elos: {missionModelFX.getEloUris(true).size()}");
   }

   function printEloInfos(eloUris: List){
      def metadatas = config.getRepository().retrieveMetadatas(eloUris);
      for (uri in eloUris) {
         printEloInfoFromMetadata(uri as URI, metadatas.get(indexof uri));
      }
   }

   function printEloInfoFromMetadata(uri: URI, metadata: IMetadata): Void {
      var title = "?";
      var technicalType = "?";
      var functionalType = "?";
      if (metadata != null) {
      def scyElo = new ScyElo(metadata,config.getToolBrokerAPI());
         title = scyElo.getTitle();
         technicalType = scyElo.getTechnicalFormat();
         functionalType = "{scyElo.getFunctionalRole()}";
      }
      println("{uri}^t^{title}^t^{technicalType}^t^{functionalType}");
   }

   public function createScyDesktop(): ScyDesktop {
      eloConfigManager.setDebug(initializer.debugMode);
      def scyDesktop: ScyDesktop = ScyDesktop {
            initializer: initializer
            config: config;
            missionRunConfigs: missionRunConfigs
            missionModelFX: missionModelFX;
            windowStyler: windowStyler;
            scyToolCreatorRegistryFX: scyToolCreatorRegistryFX
            newEloCreationRegistry: newEloCreationRegistry;
            drawerContentCreatorRegistryFX: drawerContentCreatorRegistryFX;
            eloConfigManager: eloConfigManager;
            templateEloUris: templateEloUris
         };
      missionModelFX.scyDesktop = scyDesktop;
      //register for notifications
      //logger.debug("****************registering scyDesktop for notifications***************************");
      //scyDesktop.config.getToolBrokerAPI().registerForNotifications(scyDesktop);
      //logger.debug("****************registering RemoteControlRegistry for notifications***************************");
      //scyDesktop.config.getToolBrokerAPI().registerForNotifications(scyDesktop.remoteCommandRegistryFX);
      logger.debug("****************registering ScyDesktopNotificationRouter for notifications***************************");
      def scyDesktopNotificationRouter: ScyDesktopNotificationRouter = ScyDesktopNotificationRouter {
            scyDesktop: scyDesktop;
         }

      scyDesktop.config.getToolBrokerAPI().registerForNotifications(scyDesktopNotificationRouter);
      DialogBox.scyDesktop = scyDesktop;
      return scyDesktop;
   }

}
