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
import eu.scy.client.tools.fxchattool.registration.ChattoolPresenceDrawerContentCreatorFX;


import eu.scy.awareness.IAwarenessService;
import eu.scy.toolbroker.ToolBrokerImpl;
import roolo.elo.api.IMetadataKey;

/**
 * @author jeremyt
 */

InitLog4JFX.initLog4J();

def scychatId = "chat";
def scychatpresenceId = "presence";

var scyDesktopCreator = ScyDesktopCreator {
    configClassPathConfigLocation:"config/scyDesktopChatTestConfig.xml";
}

var tbi:ToolBrokerImpl = new ToolBrokerImpl("senders11@scy.intermedia.uio.no", "senders11");
var awarenessService:IAwarenessService = tbi.getAwarenessService();
awarenessService.init(tbi.getConnection("senders11@scy.intermedia.uio.no", "senders11"));

scyDesktopCreator.drawerContentCreatorRegistryFX.registerDrawerContentCreatorFX(ChattoolDrawerContentCreatorFX{iAwarenessService:awarenessService;}, scychatId);
scyDesktopCreator.drawerContentCreatorRegistryFX.registerDrawerContentCreatorFX(ChattoolPresenceDrawerContentCreatorFX{iAwarenessService:awarenessService;}, scychatpresenceId);
//scyDesktopCreator.windowContentCreatorRegistryFX.registerWindowContentCreatorFX(ChattoolDrawerContentCreatorFX{}, scychatId);


var scyDesktop = scyDesktopCreator.createScyDesktop();

scyDesktop.bottomLeftCornerTool = NewScyWindowTool {
    scyDesktop:scyDesktop;
    repository:scyDesktopCreator.config.getRepository();
    titleKey:scyDesktopCreator.config.getTitleKey();
    technicalFormatKey:scyDesktopCreator.config.getTechnicalFormatKey();
}


var stage = Stage {
    title: "SCY desktop with chat"
    width: 400
    height: 300
    scene: Scene {
        content: [
            scyDesktop
        ]
    }
}
