package eu.scy.client.tools.fxchattool;

import javafx.scene.Scene;
import javafx.stage.Stage;
import eu.scy.client.tools.fxchattool.registration.ChattoolDrawerContentCreatorFX;
import eu.scy.client.tools.fxchattool.registration.ChattoolPresenceDrawerContentCreatorFX;
import java.util.logging.Logger;
import eu.scy.client.desktop.scydesktop.tools.content.text.TextEditorScyToolContentCreator;
import eu.scy.client.desktop.scydesktop.ScyDesktopCreator;
import eu.scy.client.desktop.scydesktop.Initializer;
import eu.scy.client.desktop.scydesktop.ScyDesktop;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import eu.scy.awareness.IAwarenessService;
import java.util.HashMap;
import eu.scy.client.desktop.scydesktop.corners.elomanagement.EloManagement;

/*
 * Main.fx
 *
 * Created on 8-jul-2009, 17:21:12
 * @author jeremyt
 */
var initializer = Initializer {
        scyDesktopConfigFile: "config/scyDesktopChatTestConfig.xml";
        loginType:"remote";
};
def logger = Logger.getLogger("eu.scy.client.desktop.scydesktop.login.LoginDialog");

function createScyDesktop(toolBrokerAPI: ToolBrokerAPI, userName: String): ScyDesktop {
   logger.info("top of createScyDesktop");

   def scychatId = "chat";
   def scychatpresenceId = "presence";
    def scyTextId = "text";

   var scyDesktopCreator = ScyDesktopCreator {
              initializer: initializer;
              toolBrokerAPI: toolBrokerAPI;
              userName: userName;
           }

    
    var awarenessService:IAwarenessService = toolBrokerAPI.getAwarenessService();
    
    var chatControllerMap = new HashMap();
    logger.info("awarenessService exists: {awarenessService.isConnected()}");

   scyDesktopCreator.drawerContentCreatorRegistryFX.registerDrawerContentCreatorFX(
            ChattoolDrawerContentCreatorFX {
                awarenessService: awarenessService;
                chatControllerMap: chatControllerMap;
                },
            scychatId);
            
    scyDesktopCreator.drawerContentCreatorRegistryFX.registerDrawerContentCreatorFX(
            ChattoolPresenceDrawerContentCreatorFX {
                awarenessService: awarenessService;
                chatControllerMap: chatControllerMap;
                toolBrokerAPI: toolBrokerAPI;
            },
            scychatpresenceId);


 

   var scyDesktop = scyDesktopCreator.createScyDesktop();

    scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreatorFX(TextEditorScyToolContentCreator {}, scyTextId);

   scyDesktop.bottomLeftCornerTool = EloManagement {
      scyDesktop: scyDesktop;
      repository: scyDesktopCreator.config.getRepository();
      metadataTypeManager:scyDesktopCreator.config.getMetadataTypeManager();
      titleKey: scyDesktopCreator.config.getTitleKey();
      technicalFormatKey: scyDesktopCreator.config.getTechnicalFormatKey();
      userId:userName
   }

   return scyDesktop;
}
var stage: Stage;
var scene: Scene;

stage = Stage {
   title: "SCY desktop with chat (jfx 1.3)"
   width: 400
   height: 300
	scene: initializer.getScene(createScyDesktop);
}
