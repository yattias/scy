/*
 * Main.fx
 *
 * Created on 17-sep-2009, 17:39:26
 */

package eu.scy.client.tools.fxflyingsaucer;

import javafx.scene.Scene;
import javafx.stage.Stage;
import eu.scy.client.desktop.scydesktop.utils.log4j.InitLog4JFX;

import eu.scy.client.desktop.scydesktop.ScyDesktopCreator;
import eu.scy.client.desktop.scydesktop.corners.tools.NewScyWindowTool;

import eu.scy.client.tools.fxflyingsaucer.registration.FlyingSaucerContentCreator;

/**
 * @author sikkenj
 */

InitLog4JFX.initLog4J();

def scyFlyingSaucerType = "scy/text/xhtml";

var scyDesktopCreator = ScyDesktopCreator{
   configClassPathConfigLocation:"config/scyDesktopFlyingSaucerTestConfig.xml";
}

scyDesktopCreator.windowContentCreatorRegistryFX.registerWindowContentCreatorFX(FlyingSaucerContentCreator{},scyFlyingSaucerType);
scyDesktopCreator.newEloCreationRegistry.registerEloCreation(scyFlyingSaucerType,"Flying saucer");

var scyDesktop = scyDesktopCreator.createScyDesktop();

scyDesktop.bottomLeftCornerTool = NewScyWindowTool{
      scyDesktop:scyDesktop;
   }


var stage = Stage {
    title: "SCY desktop with Flying saucer tool"
    width: 400
    height: 300
    scene: Scene {
        content: [
            scyDesktop
        ]
    }
}
