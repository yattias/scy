/*
 * Main.fx
 *
 * Created on 04.09.2009, 12:34:29
 */
package eu.scy.client.tools.fxwebresourcer;

import javafx.scene.Scene;
import javafx.stage.Stage;
import eu.scy.client.desktop.scydesktop.Initializer;
import eu.scy.client.desktop.scydesktop.ScyDesktop;
import eu.scy.client.desktop.scydesktop.ScyDesktopCreator;
import eu.scy.client.desktop.scydesktop.corners.tools.NewScyWindowTool;
import eu.scy.client.desktop.scydesktop.login.LoginDialog;
import eu.scy.client.desktop.scydesktop.tools.drawers.xmlviewer.EloXmlViewerCreator;
import eu.scy.toolbrokerapi.ToolBrokerAPI;

/**
 * @author pg
 */
var initializer = Initializer {
           scyDesktopConfigFile: "config/scyDesktopWebResourceTestConfig.xml"
        }

function createScyDesktop(toolBrokerAPI: ToolBrokerAPI, userName: String): ScyDesktop {
   //def scyWebType = "scy/webresource";
   def scyWebresourceId = "webresource";
//var test = JAXBTest{};
//test.show();

   var scyDesktopCreator = ScyDesktopCreator {
              initializer: initializer;
              toolBrokerAPI: toolBrokerAPI;
              userName: userName;
           }

   scyDesktopCreator.windowContentCreatorRegistryFX.registerWindowContentCreatorFX(WebResourceContentCreator {}, scyWebresourceId);
// scyDesktopCreator.newEloCreationRegistry.registerEloCreation(scyWebType, "webresource");

   scyDesktopCreator.drawerContentCreatorRegistryFX.registerDrawerContentCreator(new EloXmlViewerCreator(), "xmlViewer");



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
var scene: Scene;

stage = Stage {
   title: "webresourceR"
   width: 400
   height: 300
   scene: scene = Scene {
      content: [
         initializer.getBackgroundImageView(scene),
         LoginDialog {
            createScyDesktop: createScyDesktop
            initializer: initializer;
         }
      ]
   }
}

