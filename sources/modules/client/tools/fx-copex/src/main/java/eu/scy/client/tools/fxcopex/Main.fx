/*
 * Main.fx
 *
 * Created on 18 août 2009, 15:06:48
 */

package eu.scy.client.tools.fxcopex;

import javafx.stage.Stage;
import javafx.scene.Scene;
import eu.scy.client.desktop.scydesktop.utils.log4j.InitLog4JFX;

import eu.scy.client.desktop.scydesktop.ScyDesktopCreator;
import eu.scy.client.desktop.scydesktop.corners.tools.NewScyWindowTool;

import eu.scy.client.desktop.scydesktop.tools.drawers.xmlviewer.EloXmlViewerCreator;

/**
 * @author Marjolaine
 */

InitLog4JFX.initLog4J();

//def scyCopexType = "scy/copex";
def scyCopexId = "copex";

var scyDesktopCreator = ScyDesktopCreator{
    configClassPathConfigLocation:"config/scyDesktopCopexTestConfig.xml";
}

scyDesktopCreator.windowContentCreatorRegistryFX.registerWindowContentCreatorFX(CopexContentCreator{},scyCopexId);
//scyDesktopCreator.newEloCreationRegistry.registerEloCreation(scyCopexType,"copex");

scyDesktopCreator.drawerContentCreatorRegistryFX.registerDrawerContentCreator(new EloXmlViewerCreator(), "xmlViewer");

var scyDesktop = scyDesktopCreator.createScyDesktop();

scyDesktop.bottomLeftCornerTool = NewScyWindowTool{
      scyDesktop:scyDesktop;
      repository:scyDesktopCreator.config.getRepository();
      titleKey:scyDesktopCreator.config.getTitleKey();
      technicalFormatKey:scyDesktopCreator.config.getTechnicalFormatKey();
}


var stage = Stage {
     title: "Copex"
     width: 400
     height: 300
     scene: Scene {
         content: [
         scyDesktop
      ]
    }
}
