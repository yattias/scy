/*
 * Main.fx
 *
 * Created on 04.09.2009, 12:34:29
 */

package eu.scy.client.tools.fxpictureviewer;

import javafx.stage.Stage;
import javafx.scene.Scene;
import eu.scy.client.desktop.scydesktop.ScyDesktopCreator;

import eu.scy.client.desktop.scydesktop.corners.tools.NewScyWindowTool;

import eu.scy.client.desktop.scydesktop.tools.drawers.xmlviewer.EloXmlViewerCreator;

/**
 * @author pg
 */
//InitLog4JFX.initLog4J();

// def scyPictureType = "scy/pictureviewer";
def scyPictureViewerId = "pictureviewer";

 var scyDesktopCreator = ScyDesktopCreator {
     configClassPathConfigLocation: "config/scyDesktopPictureViewerTestConfig.xml";
 }

 scyDesktopCreator.windowContentCreatorRegistryFX.registerWindowContentCreatorFX(PictureViewerContentCreator{}, scyPictureViewerId);
// scyDesktopCreator.newEloCreationRegistry.registerEloCreation(scyPictureType, "pictureviewer");

scyDesktopCreator.drawerContentCreatorRegistryFX.registerDrawerContentCreator(new EloXmlViewerCreator(), "xmlViewer");

 var scyDesktop = scyDesktopCreator.createScyDesktop();
 scyDesktop.bottomLeftCornerTool = NewScyWindowTool {

      scyDesktop:scyDesktop;
      repository:scyDesktopCreator.config.getRepository();
      titleKey:scyDesktopCreator.config.getTitleKey();
      technicalFormatKey:scyDesktopCreator.config.getTechnicalFormatKey();
 }


Stage {
    title: "pictureviewer"
    width: 250
    height: 80
    scene: Scene {
        content: [
                scyDesktop
        ]
    }
}
