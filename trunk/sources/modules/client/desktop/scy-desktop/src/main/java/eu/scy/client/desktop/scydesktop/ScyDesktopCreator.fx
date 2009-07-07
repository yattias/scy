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

/**
 * @author sikkenj
 */

public class ScyDesktopCreator {

   public-init var config:Config;
   public-init var configClassPathConfigLocation:String;
   public-init var configFileSystemConfigLocation:String;

   public-init var missionModelFX: MissionModelFX;
   public-init var eloInfoControl: EloInfoControl;
   public-init var windowStyler: WindowStyler;
   public-init var windowContentCreatorRegistryFX: WindowContentCreatorRegistryFX;

   init{
      findConfig();
      windowStyler = DummyWindowStyler{};
      windowContentCreatorRegistryFX = WindowContentCreatorRegistryFXImpl{};
      eloInfoControl = RooloEloInfoControl{
         repository: config.getRepository();
         extensionManager: config.getExtensionManager();
         titleKey:config.getTitleKey();
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
            springConfigFactory.initFromClassPath(configClassPathConfigLocation);
         }
         else if (configFileSystemConfigLocation!=null){
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
      if (config.getRegisterWindowContentCreators()!=null){
         for (registerWindowContentCreators in config.getRegisterWindowContentCreators()){
            registerWindowContentCreators.registerWindowContentCreators(windowContentCreatorRegistryFX);
         }
      }
   }

   function readMissionModel(){
      var missionModel = config.getMissionModelCreator().createMissionModel();
      missionModelFX = MissionModelFX.createMissionModelFX(missionModel);
   }


   public function createScyDesktop():ScyDesktop{
      ScyDesktop{
         config:config;
         missionModelFX : missionModelFX;
         eloInfoControl: eloInfoControl;
         windowStyler:windowStyler;
         windowContentCreatorRegistryFX:windowContentCreatorRegistryFX;

      }

   }

}
