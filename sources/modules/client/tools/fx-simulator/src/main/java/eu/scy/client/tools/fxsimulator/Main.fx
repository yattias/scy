package eu.scy.client.tools.fxsimulator;

import javafx.scene.Scene;
import javafx.stage.Stage;
import eu.scy.client.desktop.scydesktop.utils.log4j.InitLog4JFX;

import eu.scy.client.desktop.scydesktop.ScyDesktopCreator;
import eu.scy.client.desktop.scydesktop.corners.tools.NewScyWindowTool;
import eu.scy.client.tools.fxsimulator.registration.SimulatorContentCreator;


InitLog4JFX.initLog4J();

def scySimulatorType = "scy/simconfig";

var scyDesktopCreator = ScyDesktopCreator{
   configClassPathConfigLocation:"config/scyDesktopSimulatorTestConfig.xml";
}

scyDesktopCreator.windowContentCreatorRegistryFX.registerWindowContentCreatorFX(SimulatorContentCreator{},scySimulatorType);
scyDesktopCreator.newEloCreationRegistry.registerEloCreation(scySimulatorType,"simconfig");

var scyDesktop = scyDesktopCreator.createScyDesktop();

scyDesktop.bottomLeftCornerTool = NewScyWindowTool{
      scyDesktop:scyDesktop;
   }


var stage = Stage {
    title: "SCY desktop with drawing tool"
    width: 400
    height: 300
    scene: Scene {
        content: [
            scyDesktop
        ]
    }
}
