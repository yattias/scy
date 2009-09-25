package eu.scy.client.tools.fxscydynamics;

import javafx.scene.Scene;
import javafx.stage.Stage;
import eu.scy.client.desktop.scydesktop.utils.log4j.InitLog4JFX;

import eu.scy.client.desktop.scydesktop.ScyDesktopCreator;
import eu.scy.client.desktop.scydesktop.corners.tools.NewScyWindowTool;
import eu.scy.client.tools.fxscydynamics.registration.ScyDynamicsContentCreator;

import eu.scy.client.desktop.scydesktop.tools.drawers.xmlviewer.EloXmlViewerCreator;


InitLog4JFX.initLog4J();

//def scyModelType = "scy/model";
def scyModelId = "scy-dynamics";

var scyDesktopCreator = ScyDesktopCreator{
   configClassPathConfigLocation:"config/scyDesktopScyDynamicsTestConfig.xml";
}

scyDesktopCreator.windowContentCreatorRegistryFX.registerWindowContentCreatorFX(ScyDynamicsContentCreator{},scyModelId);
//scyDesktopCreator.newEloCreationRegistry.registerEloCreation(scyModelType,"model");

scyDesktopCreator.drawerContentCreatorRegistryFX.registerDrawerContentCreator(new EloXmlViewerCreator(), "xmlViewer");

var scyDesktop = scyDesktopCreator.createScyDesktop();

scyDesktop.bottomLeftCornerTool = NewScyWindowTool{
      scyDesktop:scyDesktop;
      repository:scyDesktopCreator.config.getRepository();
      titleKey:scyDesktopCreator.config.getTitleKey();
      technicalFormatKey:scyDesktopCreator.config.getTechnicalFormatKey();
   }


var stage = Stage {
    title: "SCY desktop with modelling tool"
    width: 400
    height: 300
    scene: Scene {
        content: [
            scyDesktop
        ]
    }
}
