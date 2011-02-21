package eu.scy.client.tools.fxscydynamics;

import javafx.scene.Scene;
import javafx.stage.Stage;
import eu.scy.client.desktop.scydesktop.ScyDesktopCreator;
import eu.scy.client.tools.fxscydynamics.registration.ScyDynamicsContentCreator;
import eu.scy.client.desktop.scydesktop.tools.drawers.xmlviewer.EloXmlViewerCreator;
import eu.scy.client.desktop.scydesktop.Initializer;
import eu.scy.client.desktop.scydesktop.ScyDesktop;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import eu.scy.client.desktop.scydesktop.corners.elomanagement.EloManagement;

var initializer = Initializer {
           scyDesktopConfigFile: "config/scyDesktopScyDynamicsTestConfig.xml"
           storeElosOnDisk:false;
        }

function createScyDesktop(toolBrokerAPI: ToolBrokerAPI, userName: String): ScyDesktop {
   def scyModelId = "scy-dynamics";
   def eloXmlViewerId = "xmlViewer";

   var scyDesktopCreator = ScyDesktopCreator {
              initializer: initializer;
              toolBrokerAPI: toolBrokerAPI;
              userName: userName;
           }

//   scyDesktopCreator.windowContentCreatorRegistryFX.registerWindowContentCreatorFX(DrawingtoolContentCreator {}, scyDrawingId);

   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreatorFX(ScyDynamicsContentCreator{}, scyModelId);
   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreator(new EloXmlViewerCreator(), eloXmlViewerId);

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
   title: "SCY desktop with modelling tool (jfx 1.3)"
   width: 400
   height: 300
	scene: initializer.getScene(createScyDesktop);
}