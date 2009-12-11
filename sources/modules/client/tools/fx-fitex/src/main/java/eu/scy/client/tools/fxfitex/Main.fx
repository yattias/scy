/*
 * Main.fx
 *
 * Created on 17 août 2009, 13:49:57
 */
package eu.scy.client.tools.fxfitex;

import javafx.stage.Stage;
import javafx.scene.Scene;

import eu.scy.client.desktop.scydesktop.tools.drawers.xmlviewer.EloXmlViewerCreator;
import eu.scy.client.desktop.scydesktop.Initializer;
import eu.scy.client.desktop.scydesktop.ScyDesktop;
import eu.scy.client.desktop.scydesktop.login.LoginDialog;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import eu.scy.client.desktop.scydesktop.ScyDesktopCreator;
import eu.scy.client.desktop.scydesktop.corners.tools.NewScyWindowTool;

/**
 * @author Marjolaine
 */
var initializer = Initializer {
           scyDesktopConfigFile: "config/scyDesktopFitexTestConfig.xml"
        }

function createScyDesktop(toolBrokerAPI: ToolBrokerAPI, userName: String): ScyDesktop {
//def scyFitexType = "scy/fitex";
   def scyFitexId = "fitex";

   var scyDesktopCreator = ScyDesktopCreator {
              initializer: initializer;
              toolBrokerAPI: toolBrokerAPI;
              userName: userName;
           }

   scyDesktopCreator.windowContentCreatorRegistryFX.registerWindowContentCreatorFX(FitexContentCreator {}, scyFitexId);
//scyDesktopCreator.newEloCreationRegistry.registerEloCreation(scyFitexType,"fitex");

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
   title: "Data Processing Tool"
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
