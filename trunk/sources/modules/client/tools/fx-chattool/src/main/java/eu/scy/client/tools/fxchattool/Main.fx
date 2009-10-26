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
import eu.scy.client.tools.fxchattool.registration.ChattoolDrawerContentCreatorFX;
import eu.scy.client.tools.fxchattool.registration.ChattoolDrawerContentPresenceCreatorFX;
import eu.scy.client.tools.fxchattool.registration.ChattoolDrawerContentTaskCreatorFX;
import eu.scy.client.tools.fxchattool.registration.ChattoolDrawerContentProgressCreatorFX;

/**
 * @author jeremyt
 */

InitLog4JFX.initLog4J();

//def scychatType = "scy/chat";
def scychatId = "chat";
def scypresenceId = "presence";
def scytaskId = "task";
def scyprogressId = "progress";

var scyDesktopCreator = ScyDesktopCreator {
    configClassPathConfigLocation:"config/scyDesktopChatTestConfig.xml";
}

scyDesktopCreator.drawerContentCreatorRegistryFX.registerDrawerContentCreatorFX(ChattoolDrawerContentCreatorFX{}, scychatId);
scyDesktopCreator.drawerContentCreatorRegistryFX.registerDrawerContentCreatorFX(ChattoolDrawerContentPresenceCreatorFX{}, scypresenceId);
scyDesktopCreator.drawerContentCreatorRegistryFX.registerDrawerContentCreatorFX(ChattoolDrawerContentTaskCreatorFX{}, scytaskId);
scyDesktopCreator.drawerContentCreatorRegistryFX.registerDrawerContentCreatorFX(ChattoolDrawerContentProgressCreatorFX{}, scyprogressId);


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
