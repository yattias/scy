/*
 * Main.fx
 *
 * Created on 14-mei-2009, 17:41:07
 */
package eu.scy.client.desktop.scydesktop;

import javafx.stage.Stage;
import javafx.scene.Scene;

import eu.scy.client.desktop.scydesktop.tools.drawers.xmlviewer.EloXmlViewerCreator;
import eu.scy.client.desktop.scydesktop.tools.content.text.TextEditorScyToolContentCreator;

import eu.scy.client.desktop.scydesktop.corners.tools.NewScyWindowTool;
import eu.scy.client.desktop.scydesktop.login.LoginDialog;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import eu.scy.client.desktop.scydesktop.tools.scytoolviewer.ScyToolViewerCreator;
import eu.scy.client.desktop.scydesktop.tools.propertiesviewer.PropertiesViewerCreator;

/**
 * @author sikkenj
 */
var initializer = Initializer{
   log4JInitFile:"/config/scy-desktop-log4j.xml"
   javaUtilLoggingInitFile:"/config/scy-desktop-java-util-logging.properties"
   scyDesktopConfigFile:"config/scyDesktopTestConfig.xml"
   loginType:"local"
   storeElosOnDisk:false
   createPersonalMissionMap:true
           enableLocalLogging:false
           redirectSystemStream:false
}

function createScyDesktop(toolBrokerAPI:ToolBrokerAPI, userName:String): ScyDesktop {
   def scyTextId = "text";
   def eloXmlViewerId = "xmlViewer";
   def scyToolViewerId = "scyToolViewer";
   def propertiesViewerId = "propertiesViewer";

   var scyDesktopCreator = ScyDesktopCreator {
              initializer: initializer;
              toolBrokerAPI:toolBrokerAPI;
              userName:userName;
           }

   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreatorFX(ScyToolViewerCreator{}, scyToolViewerId);
   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreatorFX(TextEditorScyToolContentCreator {}, scyTextId);
   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreator(new EloXmlViewerCreator(), eloXmlViewerId);
   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreatorFX(new PropertiesViewerCreator(), propertiesViewerId);

   var scyDesktop = scyDesktopCreator.createScyDesktop();

   scyDesktop.bottomLeftCornerTool = NewScyWindowTool {
      scyDesktop: scyDesktop;
      repository: scyDesktopCreator.config.getRepository();
      titleKey: scyDesktopCreator.config.getTitleKey();
      technicalFormatKey: scyDesktopCreator.config.getTechnicalFormatKey();
   }
   return scyDesktop;
}

var stage: Stage;
var scene:Scene;

stage = Stage {
   title: "SCY Desktop"
   width: 400
   height: 300
   scene: scene=Scene {
      content: [
         initializer.getBackgroundImageView(scene),
         LoginDialog {
           createScyDesktop: createScyDesktop
           initializer:initializer;
        }
      ]
   }
}
