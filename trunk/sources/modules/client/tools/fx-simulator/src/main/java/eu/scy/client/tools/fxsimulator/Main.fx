package eu.scy.client.tools.fxsimulator;

import javafx.scene.Scene;
import javafx.stage.Stage;

import eu.scy.client.desktop.scydesktop.ScyDesktopCreator;
import eu.scy.client.desktop.scydesktop.corners.tools.NewScyWindowTool;
import eu.scy.client.tools.fxsimulator.registration.SimulatorContentCreator;

import eu.scy.client.desktop.scydesktop.tools.drawers.xmlviewer.EloXmlViewerCreator;
import eu.scy.client.desktop.scydesktop.Initializer;
import eu.scy.client.desktop.scydesktop.ScyDesktop;
import eu.scy.client.desktop.scydesktop.login.LoginDialog;
import eu.scy.toolbrokerapi.ToolBrokerAPI;

var initializer = Initializer {
           scyDesktopConfigFile: "config/scyDesktopSimulatorTestConfig.xml"
        }

function createScyDesktop(toolBrokerAPI: ToolBrokerAPI, userName: String): ScyDesktop {
//def scySimulatorType = "scy/simconfig";
   def scySimulatorId = "simulator";

   var scyDesktopCreator = ScyDesktopCreator {
              initializer: initializer;
              toolBrokerAPI: toolBrokerAPI;
              userName: userName;
           }

   scyDesktopCreator.windowContentCreatorRegistryFX.registerWindowContentCreatorFX(SimulatorContentCreator {}, scySimulatorId);
//scyDesktopCreator.newEloCreationRegistry.registerEloCreation(scySimulatorType,"simconfig");

   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreator(new EloXmlViewerCreator(), "xmlViewer");

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
   title: "SCY desktop with simulator tool"
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
