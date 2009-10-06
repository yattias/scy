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

import eu.scy.client.desktop.scydesktop.tools.drawers.xmlviewer.EloXmlViewerCreator;

/**
 * @author sikkenj
 */

InitLog4JFX.initLog4J("/config/scy-flying-saucer-log4j.xml");

//def scyFlyingSaucerType = "scy/url";
def scyFlyingSaucerId = "flying-saucer";

var scyDesktopCreator = ScyDesktopCreator{
   configClassPathConfigLocation:"config/scyDesktopFlyingSaucerTestConfig.xml";
}

scyDesktopCreator.windowContentCreatorRegistryFX.registerWindowContentCreatorFX(FlyingSaucerContentCreator{},scyFlyingSaucerId);
//scyDesktopCreator.newEloCreationRegistry.registerEloCreation(scyFlyingSaucerType,"Flying saucer");

scyDesktopCreator.drawerContentCreatorRegistryFX.registerDrawerContentCreator(new EloXmlViewerCreator(), "xmlViewer");

var scyDesktop = scyDesktopCreator.createScyDesktop();

scyDesktop.bottomLeftCornerTool = NewScyWindowTool{
      scyDesktop:scyDesktop;
      repository:scyDesktopCreator.config.getRepository();
      titleKey:scyDesktopCreator.config.getTitleKey();
      technicalFormatKey:scyDesktopCreator.config.getTechnicalFormatKey();
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
