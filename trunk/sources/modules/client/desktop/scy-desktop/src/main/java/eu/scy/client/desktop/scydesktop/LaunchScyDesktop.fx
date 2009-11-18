/*
 * LaunchScyDesktop.fx
 *
 * Created on 7-jul-2009, 14:40:33
 */

package eu.scy.client.desktop.scydesktop;

import javafx.scene.Scene;
import javafx.stage.Stage;
import eu.scy.client.desktop.scydesktop.utils.log4j.InitLog4JFX;
import eu.scy.client.desktop.scydesktop.corners.tools.NewScyWindowTool;
import eu.scy.client.desktop.scydesktop.tools.content.text.TextEditorToolContentCreator;
import eu.scy.client.desktop.scydesktop.tools.content.eloImporter.ELOImporterToolContentCreator;

/**
 * @author sikkenj
 */

// place your code here
// argument option names
def titleOption = "title";
def classPathConfigLocationOption = "classPathConfigLocation";
def fileSystemConfigLocationOption = "fileSystemConfigLocation";
//def missionModelCreatorClassNameOptionOption = "missionModelCreatorClassName";
//def registerWindowContentCreatorsClassNameOption = "registerWindowContentCreatorsClassName";

InitLog4JFX.initLog4J();

// argument values
var title = "SCY-Desktop";
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

var scyDesktopCreator = ScyDesktopCreator{
   configClassPathConfigLocation:classPathConfigLocation;
   configFileSystemConfigLocation:fileSystemConfigLocation;
}

scyDesktopCreator.windowContentCreatorRegistryFX.registerWindowContentCreatorFX(TextEditorToolContentCreator{}, "text");
//scyDesktopCreator.drawerContentCreatorRegistryFX.registerDrawerContentCreator(new EloXmlViewerCreator(), "xmlViewer");

scyDesktopCreator.windowContentCreatorRegistryFX.registerWindowContentCreatorFX(ELOImporterToolContentCreator{}, "ppt");

var scyDesktop = scyDesktopCreator.createScyDesktop();

scyDesktop.bottomLeftCornerTool = NewScyWindowTool{
      scyDesktop:scyDesktop;
      scyWindowControl:scyDesktop.scyWindowControl;
      repository:scyDesktopCreator.config.getRepository();
      titleKey:scyDesktopCreator.config.getTitleKey();
      technicalFormatKey:scyDesktopCreator.config.getTechnicalFormatKey();
   }

var stage = Stage {
    title: title
    width: 400
    height: 300
    scene: Scene {
        content: [
            scyDesktop
        ]
    }
}
