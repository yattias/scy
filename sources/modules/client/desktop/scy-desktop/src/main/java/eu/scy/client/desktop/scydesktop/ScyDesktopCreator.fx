/*
 * ScyDesktopCreator.fx
 *
 * Created on 7-jul-2009, 14:23:46
 */
package eu.scy.client.desktop.scydesktop;

import eu.scy.client.desktop.scydesktop.config.Config;
import eu.scy.client.desktop.scydesktop.elofactory.WindowContentCreatorRegistryFX;
import eu.scy.client.desktop.scydesktop.scywindows.WindowStyler;
import eu.scy.client.desktop.scydesktop.scywindows.EloInfoControl;
import eu.scy.client.desktop.scydesktop.tools.corner.missionmap.MissionModelFX;
import eu.scy.client.desktop.scydesktop.tools.corner.missionmap.MissionAnchorFX;


import eu.scy.client.desktop.scydesktop.config.SpringConfigFactory;
import java.lang.IllegalStateException;

import eu.scy.client.desktop.scydesktop.elofactory.NewEloCreationRegistry;

import eu.scy.client.desktop.scydesktop.elofactory.impl.NewEloCreationRegistryImpl;
import eu.scy.client.desktop.scydesktop.elofactory.DrawerContentCreatorRegistryFX;

import eu.scy.toolbrokerapi.ToolBrokerAPI;

import eu.scy.client.desktop.scydesktop.utils.log4j.Logger;
import javafx.scene.paint.Color;

import java.lang.System;
import eu.scy.client.desktop.scydesktop.config.BasicConfig;
import roolo.api.search.ISearchResult;
import roolo.cms.repository.mock.BasicMetadataQuery;
import roolo.cms.repository.search.BasicSearchOperations;
import roolo.api.search.AndQuery;
import eu.scy.client.desktop.scydesktop.tools.corner.missionmap.MissionModelXml;
import eu.scy.client.desktop.scydesktop.elofactory.ScyToolCreatorRegistryFX;
import eu.scy.client.desktop.scydesktop.elofactory.impl.ScyToolCreatorRegistryFXImpl;
import eu.scy.client.desktop.scydesktop.imagewindowstyler.ImageWindowStyler;
import eu.scy.client.desktop.scydesktop.config.MissionModelUtils;

import eu.scy.toolbrokerapi.ToolBrokerAPIRuntimeSetting;
import javax.swing.JOptionPane;
import java.awt.Component;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import java.net.URI;
import eu.scy.client.desktop.scydesktop.elofactory.EloConfigManager;
import eu.scy.client.desktop.scydesktop.elofactory.impl.BasicEloConfigManager;
import java.util.ArrayList;
import eu.scy.client.desktop.scydesktop.scywindows.scydesktop.EloInfoControlImpl;
import eu.scy.client.desktop.scydesktop.scywindows.EloDisplayTypeControl;
import eu.scy.client.desktop.scydesktop.scywindows.scydesktop.EloDisplayTypeControlImpl;

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
   public-init var eloDisplayTypeControl: EloDisplayTypeControl;
   public-init var windowStyler: WindowStyler;
   public-init var scyToolCreatorRegistryFX: ScyToolCreatorRegistryFX;
   public-init var newEloCreationRegistry: NewEloCreationRegistry;
   public-init var windowContentCreatorRegistryFX: WindowContentCreatorRegistryFX;
   public-init var drawerContentCreatorRegistryFX: DrawerContentCreatorRegistryFX;
   public-init var eloConfigManager: EloConfigManager;
   def userNameKey = "userName";

   var titleKey:IMetadataKey;
   var technicalFormatKey:IMetadataKey;
   var missionIdKey:IMetadataKey;
   var anchorIdKey:IMetadataKey;
   var lasKey:IMetadataKey;
   var containsAssignmentEloKey:IMetadataKey;
   var functionalTypeKey:IMetadataKey;
   var iconTypeKey:IMetadataKey;

   init {
      findConfig();
      if (eloInfoControl == null) {
         eloInfoControl = EloInfoControlImpl {
            repository: config.getRepository();
            extensionManager: config.getExtensionManager();
            metadataTypeManager: config.getMetadataTypeManager();
         }
      }
      if (eloDisplayTypeControl == null) {
         eloDisplayTypeControl = EloDisplayTypeControlImpl {
            repository: config.getRepository();
            extensionManager: config.getExtensionManager();
            metadataTypeManager: config.getMetadataTypeManager();
         }
      }
      findMetadataKeys();
      if (windowStyler == null) {
         windowStyler = ImageWindowStyler {
            eloTypeControl: eloDisplayTypeControl;
            impagesPath:initializer.eloImagesPath
            repository:config.getRepository()
            metadataTypeManager:config.getMetadataTypeManager()
         };
      }
      var scyToolCreatorRegistryFXImpl = ScyToolCreatorRegistryFXImpl{
            config:config;
         }
      if (scyToolCreatorRegistryFX == null) {
         scyToolCreatorRegistryFX = scyToolCreatorRegistryFXImpl;
      }
      if (newEloCreationRegistry == null) {
         newEloCreationRegistry = NewEloCreationRegistryImpl {};
      }
      if (windowContentCreatorRegistryFX == null) {
         windowContentCreatorRegistryFX = scyToolCreatorRegistryFXImpl;
      }
      if (drawerContentCreatorRegistryFX == null) {
         drawerContentCreatorRegistryFX = scyToolCreatorRegistryFXImpl;
      }
      if (eloConfigManager==null){
         eloConfigManager = new BasicEloConfigManager(config);
      }


      handleToolRegistration();
      if (missionModelFX == null) {
         readMissionModel();
      }
      if (toolBrokerAPI instanceof ToolBrokerAPIRuntimeSetting){
         var toolBrokerAPIRuntimeSetting = toolBrokerAPI as ToolBrokerAPIRuntimeSetting;
         toolBrokerAPIRuntimeSetting.setMissionId(missionModelFX.id);
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

   function findMetadataKeys(){
      titleKey = findMetadataKey(CoreRooloMetadataKeyIds.TITLE.getId());
      technicalFormatKey = findMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT.getId());
      missionIdKey = findMetadataKey(ScyRooloMetadataKeyIds.MISSION.getId());
      anchorIdKey = findMetadataKey(ScyRooloMetadataKeyIds.ANCHOR_ID.getId());
      lasKey = findMetadataKey(ScyRooloMetadataKeyIds.LAS.getId());
      containsAssignmentEloKey = findMetadataKey(ScyRooloMetadataKeyIds.CONTAINS_ASSIGMENT_ELO.getId());
      functionalTypeKey = findMetadataKey(ScyRooloMetadataKeyIds.FUNCTIONAL_TYPE.getId());
      iconTypeKey = findMetadataKey(ScyRooloMetadataKeyIds.ICON_TYPE.getId());
   }

   function findMetadataKey(id: String):IMetadataKey{
      var key = config.getMetadataTypeManager().getMetadataKey(id);
      if (key==null){
         throw new IllegalStateException("the metadata key cannot be found, id: {id}");
      }
      return key;
   }


   function handleToolRegistration() {
      if (config.getRegisterContentCreators() != null) {
         for (registerContentCreators in config.getRegisterContentCreators()) {
            registerContentCreators.registerWindowContentCreators(windowContentCreatorRegistryFX);
            registerContentCreators.registerDrawerContentCreators(drawerContentCreatorRegistryFX);
            registerContentCreators.registerNewEloCreation(newEloCreationRegistry);
         }
      }
      if (config.getNewEloDescriptions() != null){
         for (newEloDescription in config.getNewEloDescriptions()) {
            newEloCreationRegistry.registerEloCreation(newEloDescription.getType(), newEloDescription.getDisplay());
         }
      }
   }

   function readMissionModel() {
      if (initializer.debugMode){
         printAllSpecificationEloUris();
      }
      // cache the elo templates
      config.getRepository().retrieveELOs(config.getTemplateEloUris());
      missionModelFX = retrieveStoredMissionModel();
      if (missionModelFX==null){
         // first time login
         // cache the elos
         config.getRepository().retrieveELOs(config.getAllMissionEloUris());
         missionModelFX = MissionModelUtils.retrieveMissionModelFromConfig(config);
         addEloStatusInformationToMissionModel(missionModelFX);
         if (initializer.createPersonalMissionMap){
            makeItMyMissionModel(missionModelFX);
         }
      }
      else{
         var myMissionModelId = config.getBasicMissionMap().getId();
         if (missionModelFX.id!=myMissionModelId){
            JOptionPane.showMessageDialog(null as Component,"This SCY-Lab works only for mission id {myMissionModelId}, not for {missionModelFX.id}","Not configures",JOptionPane.ERROR_MESSAGE);
            FX.exit();
         }
         // cache the elos
         var eloUrisToCache = new ArrayList();
         for (uri in missionModelFX.getAllEloUris()){
            eloUrisToCache.add(uri);
         }

         config.getRepository().retrieveELOs(eloUrisToCache);
      }
 
      if (missionModelFX == null) {
         // still no mission model, create an empty one
         missionModelFX = MissionModelFX {};
      }
      missionModelFX.repository = config.getRepository();
      missionModelFX.eloFactory = config.getEloFactory();
   }

   function printAllSpecificationEloUris():Void{
      println("\nThe list of all elo uris, used in the mission map specifiaction");
      for (uri in config.getAllMissionEloUris()){
         printEloUri(uri);
      }
      println("\nThe list of template elo uris");
      for (uri in config.getTemplateEloUris()){
         printEloUri(uri);
      }
      println("");
   }

   function printEloUri(uri:URI):Void{
      var title = "?";
      var technicalType = "?";
      var functionalType = "?";
      var metadata = config.getRepository().retrieveMetadata(uri);
      if (metadata!=null){
         title = metadata.getMetadataValueContainer(titleKey).getValue() as String;
         technicalType = metadata.getMetadataValueContainer(technicalFormatKey).getValue() as String;
         functionalType = metadata.getMetadataValueContainer(functionalTypeKey).getValue() as String;
      }
      println("{uri}^t^{title}^t^{technicalType}^t^{functionalType}");
   }


   function retrieveStoredMissionModel():MissionModelFX{
      var typeQuery = new BasicMetadataQuery(config.getTechnicalFormatKey(),BasicSearchOperations.EQUALS,MissionModelFX.eloType,null);
      var titleQuery = new BasicMetadataQuery(config.getTitleKey(),BasicSearchOperations.EQUALS,userName,null);
      var andQuery = new AndQuery(typeQuery,titleQuery);
      var missionId = config.getBasicMissionMap().getId();
      if (missionId!=null){
         var missionIdQuery = new BasicMetadataQuery(config.getMetadataTypeManager().getMetadataKey(ScyRooloMetadataKeyIds.MISSION.getId()),BasicSearchOperations.EQUALS,missionId,null);
         andQuery.addQuery(missionIdQuery);
      }
      var results = config.getRepository().search(andQuery);
      logger.info("Nr of elos found: {results.size()}");
      if (results.size() == 1) {
         var searchResult = results.get(0) as ISearchResult;

         var missionModelElo = config.getRepository().retrieveELO(searchResult.getUri());
         var missionModelXml = missionModelElo.getContent().getXmlString();
         var missionModel = MissionModelXml.convertToMissionModel(missionModelXml);
         addEloStatusInformationToMissionModel(missionModel);
         missionModel.elo = missionModelElo;
         return missionModel;
      }
      return null;
   }

   function addEloStatusInformationToMissionModel(missionModel: MissionModelFX) {
      for (las in missionModel.lasses){
         addAnchorStatusInformation(las.mainAnchor);
         for (intermediateAnchor in las.intermediateAnchors){
            addAnchorStatusInformation(intermediateAnchor);
         }
         las.exists = las.mainAnchor.exists;
      }
   }

   function addAnchorStatusInformation(missionAnchor: MissionAnchorFX) {
      // fill in the missing info
      //var type = eloInfoControl.getEloType(missionAnchor.eloUri);
      missionAnchor.color = windowStyler.getScyColor(missionAnchor.eloUri);
      //missionAnchor.iconCharacter = windowStyler.getScyIconCharacter(missionAnchor.eloUri);
      if (missionAnchor.eloUri!=null){
         missionAnchor.metadata = config.getRepository().retrieveMetadata(missionAnchor.eloUri);
      }
      if (missionAnchor.metadata != null) {
         missionAnchor.exists = true;
         missionAnchor.title = missionAnchor.metadata.getMetadataValueContainer(config.getTitleKey()).getValue() as String;
         if (missionAnchor.iconType.length()>0){
            // add the icon type name as metadata
            var newMetadata = config.getEloFactory().createMetadata();
            newMetadata.getMetadataValueContainer(iconTypeKey).setValue(missionAnchor.iconType);
            config.getRepository().addMetadata(missionAnchor.eloUri,newMetadata);
            missionAnchor.metadata = config.getRepository().retrieveMetadata(missionAnchor.eloUri);
            missionAnchor.color = windowStyler.getScyColor(missionAnchor.eloUri);
         }

      } else {
         missionAnchor.exists = false;
         // change the color, to show the elo does not exists
         missionAnchor.color = getNotExistingColor(missionAnchor.color);
      }
   }

   function getNotExistingColor(color: Color): Color {
      Color.color(getLighterColorComponent(color.red), getLighterColorComponent(color.green), getLighterColorComponent(color.blue));
   }

   function getLighterColorComponent(value: Number): Number {
      return 1 - (1 - value) / 2.0;
   }

//   function getActiveMissionAnchor(missionModel: MissionModelFX, uri: URI): MissionAnchorFX {
//      for (missionAnchor in missionModel.anchors) {
//         if (uri == missionAnchor.eloUri) {
//            return missionAnchor;
//         }
//      }
//      logger.info("failed to get active mission anchor with uri: {uri}");
//      return null;
//   }

   function makeItMyMissionModel(missionModel: MissionModelFX){
      for (las in missionModel.lasses){
         makePersonalMissionAnchor(las.mainAnchor);
         for (anchor in las.intermediateAnchors){
            makePersonalMissionAnchor(anchor);
         }
      }

      missionModel.elo = config.getEloFactory().createELO();
      missionModel.elo.getMetadata().getMetadataValueContainer(titleKey).setValue(userName);
      missionModel.elo.getMetadata().getMetadataValueContainer(technicalFormatKey).setValue(MissionModelFX.eloType);
      missionModel.elo.getMetadata().getMetadataValueContainer(missionIdKey).setValue(missionModel.id);
      missionModel.elo.getContent().setXmlString(MissionModelXml.convertToXml(missionModel));
      var missionModelMetadata = config.getRepository().addNewELO(missionModel.elo);
      config.getEloFactory().updateELOWithResult(missionModel.elo,missionModelMetadata);
   }

   function makePersonalMissionAnchor(missionAnchor:MissionAnchorFX){
      if (missionAnchor.exists){
         var missionAnchorElo = config.getRepository().retrieveELO(missionAnchor.eloUri);
         if (missionAnchorElo!=null){
            missionAnchorElo.getMetadata().getMetadataValueContainer(anchorIdKey).setValue(missionAnchor.id);
            missionAnchorElo.getMetadata().getMetadataValueContainer(lasKey).setValue(missionAnchor.las.id);
            var assignmentEloUri = findAssignmentEloUri(missionAnchor);
            if (assignmentEloUri!=null){
               missionAnchorElo.getMetadata().getMetadataValueContainer(containsAssignmentEloKey).setValue(assignmentEloUri);
            }
            var eloConfig = config.getEloConfig(missionAnchorElo.getMetadata().getMetadataValueContainer(technicalFormatKey).getValue() as String);
            if (not eloConfig.isContentStatic()){
               var forkedMissionAnchorEloMetadata = config.getRepository().addForkedELO(missionAnchorElo);
               config.getEloFactory().updateELOWithResult(missionAnchorElo,forkedMissionAnchorEloMetadata);
               missionAnchor.eloUri = missionAnchorElo.getUri();
               missionAnchor.metadata = forkedMissionAnchorEloMetadata;
            }
         }
         else{
            logger.error("failed to load existing anchor elo, uri: {missionAnchor.eloUri}");
         }
      }
   }

   function findAssignmentEloUri(missionAnchor:MissionAnchorFX):URI{
      for (loEloUri in missionAnchor.loEloUris){
         var loMetadata = config.getRepository().retrieveMetadata(loEloUri);
         var functionalType = loMetadata.getMetadataValueContainer(functionalTypeKey).getValue() as String;
         if (FunctionalTypes.ASSIGMENT.equals(functionalType)){
            return loEloUri;
         }
      }
      if (sizeof missionAnchor.loEloUris > 0){
         return missionAnchor.loEloUris[0];
      }
      return null;
   }

   public function createScyDesktop(): ScyDesktop {
      eloConfigManager.setDebug(initializer.debugMode);
      def scyDesktop:ScyDesktop = ScyDesktop {
         initializer:initializer
         config: config;
         missionModelFX: missionModelFX;
         eloInfoControl: eloInfoControl;
         eloDisplayTypeControl: eloDisplayTypeControl;
         windowStyler: windowStyler;
         scyToolCreatorRegistryFX:scyToolCreatorRegistryFX
         newEloCreationRegistry: newEloCreationRegistry;
         windowContentCreatorRegistryFX: windowContentCreatorRegistryFX;
         drawerContentCreatorRegistryFX: drawerContentCreatorRegistryFX;
         eloConfigManager:eloConfigManager;
      };

      //register for notifications
      logger.debug("****************registering scyDesktop for notifications***************************");
      scyDesktop.config.getToolBrokerAPI().registerForNotifications(scyDesktop);
      logger.debug("****************registering RemoteControlRegistry for notifications***************************");
      scyDesktop.config.getToolBrokerAPI().registerForNotifications(scyDesktop.remoteCommandRegistryFX);

      return scyDesktop; 
   }
}
