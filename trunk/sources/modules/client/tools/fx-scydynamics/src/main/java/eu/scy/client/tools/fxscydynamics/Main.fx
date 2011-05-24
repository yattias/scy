package eu.scy.client.tools.fxscydynamics;

import javafx.scene.Scene;
import javafx.stage.Stage;
import eu.scy.client.desktop.scydesktop.ScyDesktopCreator;
import eu.scy.client.tools.fxscydynamics.registration.ScyDynamicsContentCreator;
import eu.scy.client.desktop.scydesktop.tools.drawers.xmlviewer.EloXmlViewerCreator;
import eu.scy.client.desktop.scydesktop.Initializer;
import eu.scy.client.desktop.scydesktop.ScyDesktop;
import eu.scy.client.desktop.scydesktop.mission.MissionRunConfigs;

var initializer = Initializer {
           scyDesktopConfigFile: "config/scyDesktopScyDynamicsTestConfig.xml"
           storeElosOnDisk:false;
           authorMode:true;
        }

function createScyDesktop(missionRunConfigs: MissionRunConfigs): ScyDesktop {
   def scyModelId = "scy-dynamics";
   def eloXmlViewerId = "xmlViewer";

   var scyDesktopCreator = ScyDesktopCreator {
              initializer: initializer;
              missionRunConfigs: missionRunConfigs;
           }

//   scyDesktopCreator.windowContentCreatorRegistryFX.registerWindowContentCreatorFX(DrawingtoolContentCreator {}, scyDrawingId);

   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreatorFX(ScyDynamicsContentCreator{}, scyModelId);
   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreator(new EloXmlViewerCreator(), eloXmlViewerId);

   var scyDesktop = scyDesktopCreator.createScyDesktop();

   return scyDesktop;
}
var stage: Stage;
var scene: Scene;

stage = Stage {
   title: "SCY desktop with modelling tool"
   width: 400
   height: 300
	scene: initializer.getScene(createScyDesktop);
}
