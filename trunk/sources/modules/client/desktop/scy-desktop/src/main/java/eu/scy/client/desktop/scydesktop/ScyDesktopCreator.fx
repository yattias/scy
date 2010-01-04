/*
 * ScyDesktopCreator.fx
 *
 * Created on 7-jul-2009, 14:23:46
 */
package eu.scy.client.desktop.scydesktop;

import eu.scy.client.desktop.scydesktop.config.Config;
import eu.scy.client.desktop.scydesktop.elofactory.WindowContentCreatorRegistryFX;
import eu.scy.client.desktop.scydesktop.scywindows.WindowStyler;
import eu.scy.client.desktop.scydesktop.scywindows.scydesktop.EloInfoControl;
import eu.scy.client.desktop.scydesktop.tools.corner.missionmap.MissionModelFX;
import eu.scy.client.desktop.scydesktop.tools.corner.missionmap.MissionAnchorFX;

import eu.scy.client.desktop.scydesktop.dummy.DummyWindowStyler;
import eu.scy.client.desktop.scydesktop.elofactory.WindowContentCreatorRegistryFXImpl;

import eu.scy.client.desktop.scydesktop.config.SpringConfigFactory;
import eu.scy.client.desktop.scydesktop.config.MissionModelUtils;
import java.lang.IllegalStateException;

import eu.scy.client.desktop.scydesktop.elofactory.NewEloCreationRegistry;

import eu.scy.client.desktop.scydesktop.elofactory.NewEloCreationRegistryImpl;
import eu.scy.client.desktop.scydesktop.elofactory.DrawerContentCreatorRegistryFX;
import eu.scy.client.desktop.scydesktop.elofactory.DrawerContentCreatorRegistryFXImpl;

import eu.scy.toolbrokerapi.ToolBrokerAPI;

import org.apache.log4j.Logger;
import javafx.scene.paint.Color;

import java.net.URI;
import java.lang.System;
import eu.scy.client.desktop.scydesktop.config.BasicConfig;
import roolo.elo.api.IELO;
import roolo.api.search.ISearchResult;
import roolo.cms.repository.mock.BasicMetadataQuery;
import roolo.cms.repository.search.BasicSearchOperations;
import eu.scy.client.desktop.scydesktop.tools.corner.missionmap.MissionModelXml;

/**
 * @author sikkenj
 */

public class ScyDesktopCreator {
   def logger = Logger.getLogger(this.getClass());

   public-init var initializer: Initializer;
   public-init var toolBrokerAPI: ToolBrokerAPI;
   public-init var userName: String;
   public-init var config: Config;
//   public-init var servicesClassPathConfigLocation: String;
   public-init var servicesFileSystemConfigLocation: String;
   public-init var configClassPathConfigLocation: String;
   public-init var configFileSystemConfigLocation: String;
   public-init var missionModelFX: MissionModelFX;
   public-init var eloInfoControl: EloInfoControl;
   public-init var windowStyler: WindowStyler;
   public-init var windowContentCreatorRegistryFX: WindowContentCreatorRegistryFX;
   public-init var newEloCreationRegistry: NewEloCreationRegistry;
   public-init var drawerContentCreatorRegistryFX: DrawerContentCreatorRegistryFX;
   def userNameKey = "userName";

   init {
      findConfig();
      if (eloInfoControl == null) {
         eloInfoControl = RooloEloInfoControl {
            repository: config.getRepository();
            extensionManager: config.getExtensionManager();
            titleKey: config.getTitleKey();
         }
      }
      if (windowStyler == null) {
         windowStyler = DummyWindowStyler {
            eloInfoControl: eloInfoControl;
         };
      }
      if (windowContentCreatorRegistryFX == null) {
         windowContentCreatorRegistryFX = WindowContentCreatorRegistryFXImpl {};
      }
      if (newEloCreationRegistry == null) {
         newEloCreationRegistry = NewEloCreationRegistryImpl {};
      }
      if (drawerContentCreatorRegistryFX == null) {
         drawerContentCreatorRegistryFX = DrawerContentCreatorRegistryFXImpl {};
      }

      handleToolRegistration();
      if (missionModelFX == null) {
         readMissionModel();
      }
   }

   function findConfig() {
      if (config == null) {
         // make it compatible with the situation that services location is not defined
//         if (servicesClassPathConfigLocation == null and servicesFileSystemConfigLocation == null) {
//            servicesClassPathConfigLocation = configClassPathConfigLocation;
//            configClassPathConfigLocation = null;
//            servicesFileSystemConfigLocation = configFileSystemConfigLocation;
//            configFileSystemConfigLocation = null;
//         }
         if (initializer.scyDesktopConfigFile.length()>0){
            configClassPathConfigLocation = initializer.scyDesktopConfigFile;
         }


         // set properties, to make them avaible in spring config files
         System.setProperty(userNameKey, userName);

         var springConfigFactory = new SpringConfigFactory();
         if (configClassPathConfigLocation != null) {
            logger.info("reading spring config from class path: {configClassPathConfigLocation}");
            springConfigFactory.initFromClassPath(configClassPathConfigLocation);
//         } else if (servicesFileSystemConfigLocation != null) {
//            logger.info("reading spring config from file system: {servicesFileSystemConfigLocation}");
//            springConfigFactory.initFromFileSystem(servicesFileSystemConfigLocation);
         } else {
            throw new IllegalStateException("no spring config location defined");
         }

//         if (configClassPathConfigLocation != null) {
//            logger.info("adding spring config from class path: {configClassPathConfigLocation}");
//            springConfigFactory.addFromClassPath(configClassPathConfigLocation);
//         } else if (configFileSystemConfigLocation != null) {
//            logger.info("adding spring config from file system: {configFileSystemConfigLocation}");
//            springConfigFactory.addFromFileSystem(configFileSystemConfigLocation);
//         }

         var basicConfig = springConfigFactory.getConfig() as BasicConfig;
         if (toolBrokerAPI!=null){
            basicConfig.setToolBrokerAPI(toolBrokerAPI);
         }
         config = basicConfig;
      }
      if (config == null) {
         throw new IllegalStateException("config is not defined and could not be found");
      }
   }

   function handleToolRegistration() {
      if (config.getRegisterContentCreators() != null) {
         for (registerContentCreators in config.getRegisterContentCreators()) {
            registerContentCreators.registerWindowContentCreators(windowContentCreatorRegistryFX);
            registerContentCreators.registerDrawerContentCreators(drawerContentCreatorRegistryFX);
            registerContentCreators.registerNewEloCreation(newEloCreationRegistry);
         }
      }
      for (newEloDescription in config.getNewEloDescriptions()) {
         newEloCreationRegistry.registerEloCreation(newEloDescription.getType(), newEloDescription.getDisplay());
      }

   }

   function readMissionModel() {
//      if (config.getMissionModelCreator().createMissionModel()!=null){
//         var missionModel = config.getMissionModelCreator().createMissionModel();
//         missionModelFX = MissionModelFX.createMissionModelFX(missionModel);
//      }
//      else
      missionModelFX = retrieveStoredMissionModel();
      if (missionModelFX==null){
         // first time login
         var missionAnchors = config.getMissionAnchors();
         if (missionAnchors != null) {
            missionModelFX = MissionModelUtils.createBasicMissionModelFX(missionAnchors);
            var activeAnchor = getActiveMissionAnchor(missionModelFX, config.getActiveMissionAnchorUri());
            if (activeAnchor.exists) {
               missionModelFX.activeAnchor = activeAnchor;
            } else {
               logger.error("specified active anchor elo does not exists: {activeAnchor.eloUri}");
            }
            addEloStatusInformationToMissionModel(missionModelFX);
            if (initializer.createPersonalMissionMap){
               makeItMyMissionModel(missionModelFX);
            }
         }
      }

      if (missionModelFX == null) {
         // still no mission model, create an empty one
         missionModelFX = MissionModelFX {};
      }
      missionModelFX.repository = config.getRepository();
      missionModelFX.eloFactory = config.getEloFactory();
   }

   function retrieveStoredMissionModel():MissionModelFX{
      var typeQuery = new BasicMetadataQuery(config.getTechnicalFormatKey(),BasicSearchOperations.EQUALS,MissionModelFX.eloType,null);
      var results = config.getRepository().search(typeQuery);
      logger.info("Nr of elos found: {results.size()}");
      if (results!=null and results.size()>0){
         for (result in results){
            var searchResult = result as ISearchResult;
            var resultMetadata = config.getRepository().retrieveMetadata(searchResult.getUri());
            var resultTitle = resultMetadata.getMetadataValueContainer(config.getTitleKey()).getValue() as String;
            if (userName.equalsIgnoreCase(resultTitle)){
               var missionModelElo = config.getRepository().retrieveELO(searchResult.getUri());
               var missionModel = MissionModelXml.convertToMissionModel(missionModelElo.getContent().getXmlString());
               addEloStatusInformationToMissionModel(missionModel);
               missionModel.elo = missionModelElo;
               return missionModel;
            }
         }
      }
      return null;
   }

   function addEloStatusInformationToMissionModel(missionModel: MissionModelFX) {
      for (missionAnchor in missionModel.anchors) {
         // fill in the missing info
         var type = eloInfoControl.getEloType(missionAnchor.eloUri);
         missionAnchor.color = windowStyler.getScyColor(type);
         missionAnchor.iconCharacter = windowStyler.getScyIconCharacter(type);
         var metadata = config.getRepository().retrieveMetadata(missionAnchor.eloUri);
         if (metadata != null) {
            missionAnchor.exists = true;
            missionAnchor.title = metadata.getMetadataValueContainer(config.getTitleKey()).getValue() as String;
         } else {
            missionAnchor.exists = false;
            // change the color, to show the elo does not exists
            missionAnchor.color = getNotExistingColor(missionAnchor.color);
         }
      }
   }

   function getNotExistingColor(color: Color): Color {
      Color.color(getLighterColorComponent(color.red), getLighterColorComponent(color.green), getLighterColorComponent(color.blue));
   }

   function getLighterColorComponent(value: Number): Number {
      return 1 - (1 - value) / 2.0;
   }

   function getActiveMissionAnchor(missionModel: MissionModelFX, uri: URI): MissionAnchorFX {
      for (missionAnchor in missionModel.anchors) {
         if (uri == missionAnchor.eloUri) {
            return missionAnchor;
         }
      }
      logger.info("failed to get active mission anchor with uri: {uri}");
      return null;
   }

   function makeItMyMissionModel(missionModel: MissionModelFX){
      for (missionAnchor in missionModel.anchors){
         if (missionAnchor.exists){
            var missionAnchorElo = config.getRepository().retrieveELO(missionAnchor.eloUri);
            if (missionAnchorElo!=null){
               var forkedMissionAnchorEloMetadata = config.getRepository().addForkedELO(missionAnchorElo);
               config.getEloFactory().updateELOWithResult(missionAnchorElo,forkedMissionAnchorEloMetadata);
               missionAnchor.eloUri = missionAnchorElo.getUri();
               missionAnchor.metadata = forkedMissionAnchorEloMetadata;
            }
            else{
               logger.error("failed to load existing anchor elo, uri: {missionAnchor.eloUri}");
            }

         }
      }
      missionModel.elo = config.getEloFactory().createELO();
      missionModel.elo.getMetadata().getMetadataValueContainer(config.getTitleKey()).setValue(userName);
      missionModel.elo.getMetadata().getMetadataValueContainer(config.getTechnicalFormatKey()).setValue(MissionModelFX.eloType);
      missionModel.elo.getContent().setXmlString(MissionModelXml.convertToXml(missionModel));
      var missionModelMetadata = config.getRepository().addNewELO(missionModel.elo);
      config.getEloFactory().updateELOWithResult(missionModel.elo,missionModelMetadata);
   }

   public function createScyDesktop(): ScyDesktop {
      ScyDesktop {
         config: config;
         missionModelFX: missionModelFX;
         eloInfoControl: eloInfoControl;
         windowStyler: windowStyler;
         windowContentCreatorRegistryFX: windowContentCreatorRegistryFX;
         newEloCreationRegistry: newEloCreationRegistry;
         drawerContentCreatorRegistryFX: drawerContentCreatorRegistryFX;
      }

   }
}
