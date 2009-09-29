/*
 * ScyDesktopCreator.fx
 *
 * Created on 7-jul-2009, 14:23:46
 */

package eu.scy.client.desktop.scydesktop;

import eu.scy.client.desktop.scydesktop.config.Config;
import eu.scy.client.desktop.scydesktop.elofactory.WindowContentCreatorRegistryFX;
import eu.scy.client.desktop.scydesktop.missionmap.MissionModelFX;
import eu.scy.client.desktop.scydesktop.scywindows.WindowStyler;
import eu.scy.client.desktop.scydesktop.scywindows.scydesktop.EloInfoControl;

import eu.scy.client.desktop.scydesktop.dummy.DummyWindowStyler;
import eu.scy.client.desktop.scydesktop.elofactory.WindowContentCreatorRegistryFXImpl;

import eu.scy.client.desktop.scydesktop.config.SpringConfigFactory;
import java.lang.IllegalStateException;

import eu.scy.client.desktop.scydesktop.elofactory.NewEloCreationRegistry;

import eu.scy.client.desktop.scydesktop.elofactory.NewEloCreationRegistryImpl;
import eu.scy.client.desktop.scydesktop.elofactory.DrawerContentCreatorRegistryFX;
import eu.scy.client.desktop.scydesktop.elofactory.DrawerContentCreatorRegistryFXImpl;

import eu.scy.client.desktop.scydesktop.utils.ExceptionCatcher;
import java.lang.Thread;

import org.apache.log4j.Logger;

/**
 * @author sikkenj
 */

var logger = Logger.getLogger("eu.scy.client.desktop.scydesktop.ScyDesktopCreator");

public class ScyDesktopCreator {

   public-init var config:Config;
   public-init var configClassPathConfigLocation:String;
   public-init var configFileSystemConfigLocation:String;

   public-init var missionModelFX: MissionModelFX;
   public-init var eloInfoControl: EloInfoControl;
   public-init var windowStyler: WindowStyler;
   public-init var windowContentCreatorRegistryFX: WindowContentCreatorRegistryFX;
   public-init var newEloCreationRegistry: NewEloCreationRegistry;
   public-init var drawerContentCreatorRegistryFX: DrawerContentCreatorRegistryFX;

   init{
      Thread.setDefaultUncaughtExceptionHandler(new ExceptionCatcher("SCY-LAB"));
      findConfig();
      if (windowStyler==null){
         windowStyler = DummyWindowStyler{};
      }
      if (windowContentCreatorRegistryFX==null){
         windowContentCreatorRegistryFX = WindowContentCreatorRegistryFXImpl{};
      }
      if (newEloCreationRegistry==null){
         newEloCreationRegistry = NewEloCreationRegistryImpl{};
      }
      if (drawerContentCreatorRegistryFX==null){
         drawerContentCreatorRegistryFX = DrawerContentCreatorRegistryFXImpl{};
      }

      if (eloInfoControl==null){
         eloInfoControl = RooloEloInfoControl{
            repository: config.getRepository();
            extensionManager: config.getExtensionManager();
            titleKey:config.getTitleKey();
         }
      }
      handleToolRegistration();
      if (missionModelFX==null){
         readMissionModel();
      }

   }

   function findConfig(){
      if (config==null){
         var springConfigFactory = new SpringConfigFactory();
         if (configClassPathConfigLocation!=null){
            logger.info("reading config from class path: {configClassPathConfigLocation}");
            springConfigFactory.initFromClassPath(configClassPathConfigLocation);
         }
         else if (configFileSystemConfigLocation!=null){
            logger.info("reading config from file system: {configFileSystemConfigLocation}");
            springConfigFactory.initFromFileSystem(configFileSystemConfigLocation);
         }
         else{
            throw new IllegalStateException("no spring config location defined");
         }

         config = springConfigFactory.getConfig();
      }
      if (config==null){
         throw new IllegalStateException("config is not defined and could not be found");
      }
   }

   function handleToolRegistration(){
      if (config.getRegisterContentCreators()!=null){
         for (registerContentCreators in config.getRegisterContentCreators()){
            registerContentCreators.registerWindowContentCreators(windowContentCreatorRegistryFX);
            registerContentCreators.registerDrawerContentCreators(drawerContentCreatorRegistryFX);
            registerContentCreators.registerNewEloCreation(newEloCreationRegistry);
         }
      }
      for (newEloDescription in config.getNewEloDescriptions()){
         newEloCreationRegistry.registerEloCreation(newEloDescription.getType(),newEloDescription.getDisplay());
      }

   }

   function readMissionModel(){
      if (config.getMissionModelCreator().createMissionModel()!=null){
         var missionModel = config.getMissionModelCreator().createMissionModel();
         missionModelFX = MissionModelFX.createMissionModelFX(missionModel);
      }
      if (missionModelFX==null){
         missionModelFX = MissionModelFX{};
      }
   }


   public function createScyDesktop():ScyDesktop{
      ScyDesktop{
         config:config;
         missionModelFX : missionModelFX;
         eloInfoControl: eloInfoControl;
         windowStyler:windowStyler;
         windowContentCreatorRegistryFX:windowContentCreatorRegistryFX;
         newEloCreationRegistry: newEloCreationRegistry;
         drawerContentCreatorRegistryFX:drawerContentCreatorRegistryFX;
      }

   }

}
