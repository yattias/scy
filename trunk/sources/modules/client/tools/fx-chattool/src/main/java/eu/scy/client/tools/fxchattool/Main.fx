/*
 * Main.fx
 *
 * Created on 8-jul-2009, 17:21:12
 */

package eu.scy.client.tools.fxchattool;

import javafx.scene.Scene;
import javafx.stage.Stage;
import eu.scy.client.desktop.scydesktop.utils.log4j.InitLog4JFX;

import eu.scy.client.desktop.scydesktop.ScyDesktopCreator;
import eu.scy.client.desktop.scydesktop.corners.tools.NewScyWindowTool;
import eu.scy.client.tools.fxchattool.registration.ChattoolContentCreator;

/**
 * @author jeremyt
 */

InitLog4JFX.initLog4J();

//def scychatType = "scy/chat";
def scychatId = "chat";

var scyDesktopCreator = ScyDesktopCreator {
    configClassPathConfigLocation:"config/scyDesktopChatTestConfig.xml";
}

scyDesktopCreator.windowContentCreatorRegistryFX.registerWindowContentCreatorFX(ChattoolContentCreator{},scychatId);

var scyDesktop = scyDesktopCreator.createScyDesktop();

scyDesktop.bottomLeftCornerTool = NewScyWindowTool {
    scyDesktop:scyDesktop;
    repository:scyDesktopCreator.config.getRepository();
    titleKey:scyDesktopCreator.config.getTitleKey();
    technicalFormatKey:scyDesktopCreator.config.getTechnicalFormatKey();
}


var stage = Stage {
    title: "SCY desktop with chat tool"
    width: 400
    height: 300
    scene: Scene {
        content: [
            scyDesktop
        ]
    }
}
