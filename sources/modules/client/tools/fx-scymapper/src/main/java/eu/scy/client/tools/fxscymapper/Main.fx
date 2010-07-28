/*
 * Main.fx
 *
 * Created on 8-jul-2009, 17:21:12
 */
package eu.scy.client.tools.fxscymapper;

import javafx.scene.Scene;
import javafx.stage.Stage;

import eu.scy.client.desktop.scydesktop.ScyDesktopCreator;
import eu.scy.client.tools.fxscymapper.registration.SCYMapperContentCreator;

import eu.scy.client.desktop.scydesktop.tools.drawers.xmlviewer.EloXmlViewerCreator;
import eu.scy.client.desktop.scydesktop.Initializer;
import eu.scy.client.desktop.scydesktop.ScyDesktop;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import eu.scy.client.desktop.scydesktop.corners.elomanagement.EloManagement;

/**
 * @author sikkenj
 */
var initializer = Initializer {
           scyDesktopConfigFile: "config/scyMapperTestConfig.xml"
            loginType:"remote"
            authorMode:true
        }

function createScyDesktop(toolBrokerAPI: ToolBrokerAPI, userName: String): ScyDesktop {
   var scyDesktopCreator = ScyDesktopCreator {
              initializer: initializer;
              toolBrokerAPI: toolBrokerAPI;
              userName: userName;
           }

   def scyMapperId = "conceptmap";

   
   scyDesktopCreator.windowContentCreatorRegistryFX.registerWindowContentCreatorFX(SCYMapperContentCreator {userName: userName}, scyMapperId);

   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreator(new EloXmlViewerCreator(), "xmlViewer");

   var scyDesktop = scyDesktopCreator.createScyDesktop();

   scyDesktop.bottomLeftCornerTool = EloManagement {
      scyDesktop: scyDesktop;
      repository: scyDesktopCreator.config.getRepository();
      metadataTypeManager:scyDesktopCreator.config.getMetadataTypeManager();
      titleKey: scyDesktopCreator.config.getTitleKey();
      technicalFormatKey: scyDesktopCreator.config.getTechnicalFormatKey();
      userId:userName
   }

return scyDesktop;
}
var stage: Stage;
var scene: Scene;

stage = Stage {
   title: "SCY desktop with scymapper tool"
   width: 900
   height: 700
	scene: initializer.getScene(createScyDesktop);
}
