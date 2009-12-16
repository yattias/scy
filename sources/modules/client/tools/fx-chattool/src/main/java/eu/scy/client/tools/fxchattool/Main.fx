package eu.scy.client.tools.fxchattool;

import javafx.scene.Scene;
import javafx.stage.Stage;
import eu.scy.awareness.IAwarenessService;
import eu.scy.client.desktop.scydesktop.ScyDesktop;
import eu.scy.client.desktop.scydesktop.ScyDesktopCreator;
import eu.scy.client.desktop.scydesktop.corners.tools.NewScyWindowTool;
import eu.scy.client.desktop.scydesktop.login.LoginDialog;
import eu.scy.client.tools.fxchattool.registration.ChattoolDrawerContentCreatorFX;
import eu.scy.client.tools.fxchattool.registration.ChattoolPresenceDrawerContentCreatorFX;
import eu.scy.toolbrokerapi.ToolBrokerAPI;

import eu.scy.client.desktop.scydesktop.Initializer;

import org.apache.log4j.Logger;
/*
 * Main.fx
 *
 * Created on 8-jul-2009, 17:21:12
 * @author jeremyt
 */
var initializer = Initializer {
        scyDesktopConfigFile: "config/scyDesktopChatTestConfig.xml"
        loginType:"remote"
};
def logger = Logger.getLogger("eu.scy.client.desktop.scydesktop.login.LoginDialog");

function createScyDesktop(toolBrokerAPI: ToolBrokerAPI, userName: String): ScyDesktop {
   logger.info("top of createScyDesktop");

   def scychatId = "chat";
   def scychatpresenceId = "presence";

   var scyDesktopCreator = ScyDesktopCreator {
              initializer: initializer;
              toolBrokerAPI: toolBrokerAPI;
              userName: userName;
           }

    var awarenessService:IAwarenessService = toolBrokerAPI.getAwarenessService();
    logger.info("awarenessService exists: {awarenessService.isConnected()}");
    scyDesktopCreator.drawerContentCreatorRegistryFX.registerDrawerContentCreatorFX(ChattoolDrawerContentCreatorFX {awarenessService: awarenessService;}, scychatId);
    scyDesktopCreator.drawerContentCreatorRegistryFX.registerDrawerContentCreatorFX(ChattoolPresenceDrawerContentCreatorFX {awarenessService: awarenessService;}, scychatpresenceId);

   var scyDesktop = scyDesktopCreator.createScyDesktop();

   scyDesktop.bottomLeftCornerTool = NewScyWindowTool {
      scyDesktop: scyDesktop;
      repository: scyDesktopCreator.config.getRepository();
      titleKey: scyDesktopCreator.config.getTitleKey();
      technicalFormatKey: scyDesktopCreator.config.getTechnicalFormatKey();
   }

   return scyDesktop;
}
var stage: Stage;
var scene: Scene;

stage = Stage {
   title: "SCY desktop with chat"
   width: 400
   height: 300
   scene: scene = Scene {
      content: [
         initializer.getBackgroundImageView(scene),
         LoginDialog {
            createScyDesktop: createScyDesktop
            initializer: initializer;
         }
      ]
   }
}

