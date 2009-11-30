/*
 * Main.fx
 *
 * Created on 14-mei-2009, 17:41:07
 */

package eu.scy.client.desktop.scydesktop;

import javafx.stage.Stage;
import javafx.scene.Scene;

import eu.scy.client.desktop.scydesktop.tools.drawers.xmlviewer.EloXmlViewerCreator;
import eu.scy.client.desktop.scydesktop.tools.content.text.TextEditorScyToolContentCreator;

import eu.scy.client.desktop.scydesktop.corners.tools.NewScyWindowTool;
import eu.scy.client.desktop.scydesktop.utils.log4j.InitLog4JFX;


/**
 * @author sikkenj
 */

InitLog4JFX.initLog4J();

def scyTextId = "text";
def eloXmlViewerId = "xmlViewer";

var scyDesktopCreator = ScyDesktopCreator {
   servicesClassPathConfigLocation:"config/localWrappedRooloConfig.xml";
   configClassPathConfigLocation:"config/scyDesktopTestConfig.xml";
}

scyDesktopCreator.windowContentCreatorRegistryFX.registerWindowContentCreatorFX(TextEditorScyToolContentCreator{}, scyTextId);
scyDesktopCreator.drawerContentCreatorRegistryFX.registerDrawerContentCreator(new EloXmlViewerCreator(), "eloXmlViewerId");

var scyDesktop = scyDesktopCreator.createScyDesktop();

scyDesktop.bottomLeftCornerTool = NewScyWindowTool{
      scyDesktop:scyDesktop;
      repository:scyDesktopCreator.config.getRepository();
      titleKey:scyDesktopCreator.config.getTitleKey();
      technicalFormatKey:scyDesktopCreator.config.getTechnicalFormatKey();
}


var stage = Stage {
    title: "SCY Desktop"
    width: 400
    height: 300
    scene: Scene {
        content: [
            scyDesktop
        ]
    }
}
