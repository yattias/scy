/*
 * SwingScyDesktop.fx
 *
 * Created on 2-jul-2009, 14:01:11
 */

package eu.scy.client.desktop.scydesktop;




import eu.scy.client.desktop.scydesktop.missionmap.MissionModelFX;

import javafx.scene.Scene;
import javafx.stage.Stage;
import eu.scy.client.desktop.scydesktop.dummy.DummyEloInfoControl;
import eu.scy.client.desktop.scydesktop.dummy.DummyWindowStyler;

import eu.scy.client.desktop.scydesktop.utils.log4j.InitLog4JFX;


import eu.scy.client.desktop.scydesktop.elofactory.WindowContentCreatorRegistryFX;
import eu.scy.client.desktop.scydesktop.elofactory.WindowContentCreatorRegistryFXImpl;

import eu.scy.client.desktop.scydesktop.config.Config;

import eu.scy.client.desktop.scydesktop.config.SpringConfigFactory;

import java.lang.IllegalStateException;

import eu.scy.client.desktop.scydesktop.scywindows.scydesktop.EloInfoControl;

/**
 * @author sikkenj
 */

// argument option names
def titleOption = "title";
def classPathConfigLocationOption = "classPathConfigLocation";
def fileSystemConfigLocationOption = "fileSystemConfigLocation";
//def missionModelCreatorClassNameOptionOption = "missionModelCreatorClassName";
//def registerWindowContentCreatorsClassNameOption = "registerWindowContentCreatorsClassName";

InitLog4JFX.initLog4J();

// argument values
var title = "Swing SCY desktop";
var classPathConfigLocation = "config/scyDesktopTestConfig.xml";
var fileSystemConfigLocation = "";


function parseParamenters(args: String[]): Void{
   var i=0;
   while (i<args.size()){
      var lcArg = args[i].toLowerCase();
      if (lcArg.startsWith('-')){
         var option = lcArg.substring(1);
         if (option==titleOption.toLowerCase()){
            title = args[++i];
            println("{titleOption}: {title}");
         }
         else if (option==classPathConfigLocationOption.toLowerCase()){
            classPathConfigLocation = args[++i];
            fileSystemConfigLocation = "";
            println("{classPathConfigLocationOption}: {classPathConfigLocation}");
         }
         else if (option==fileSystemConfigLocationOption.toLowerCase()){
            fileSystemConfigLocation = args[++i];
            classPathConfigLocation = "";
            println("{fileSystemConfigLocationOption}: {fileSystemConfigLocation}");
         }
         else{
            println("Unknown option: {option}");
         }
      }
      else{
         println("ignored parameter: {args[i]}");
      }

      i++;
   }
}

parseParamenters(FX.getArguments());

// scy desktop elements
var config:Config;
var missionModelFX:MissionModelFX;
var windowContentCreatorRegistryFX:WindowContentCreatorRegistryFX = WindowContentCreatorRegistryFXImpl{};
var eloInfoControl: EloInfoControl;

function readConfig(){
   var springConfigFactory = new SpringConfigFactory();
   if (classPathConfigLocation!=null){
      springConfigFactory.initFromClassPath(classPathConfigLocation);
   }
   else if (fileSystemConfigLocation!=null){
      springConfigFactory.initFromFileSystem(fileSystemConfigLocation);
   }
   else{
      throw new IllegalStateException("no spring config location defined");
   }

   config = springConfigFactory.getConfig();
}

function createElements(){
   var missionModel = config.getMissionModelCreator().createMissionModel();
   missionModelFX = MissionModelFX.createMissionModelFX(missionModel);

   if (config.getRegisterWindowContentCreators()!=null){
      for (registerWindowContentCreators in config.getRegisterWindowContentCreators()){
         registerWindowContentCreators.registerWindowContentCreators(windowContentCreatorRegistryFX);
      }
   }

   eloInfoControl = RooloEloInfoControl{
      repository: config.getRepository();
      extensionManager: config.getExtensionManager();
      titleKey:config.getTitleKey();
   }

}

readConfig();
createElements();

var scyDesktop = ScyDesktop{
   config:config;
   missionModelFX : missionModelFX;
   eloInfoControl: eloInfoControl;
   windowStyler:DummyWindowStyler{
   };
   windowContentCreatorRegistryFX:windowContentCreatorRegistryFX;
//      topLeftCornerTool:MissionMap{
//         missionModel: missionModel
//      }
//      bottomRightCornerTool:MissionMap{
//         missionModel: missionModel
//      }
//   bottomLeftCornerTool:newWindowButton;
}

Stage {
    title: title
    width: 400
    height: 300
    scene: Scene {
        content: [
            scyDesktop
        ]
    }
}