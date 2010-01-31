/*
 * Main.fx
 *
 * Created on 8-jul-2009, 17:21:12
 */
package eu.scy.client.tools.studentplanningtool;

import javafx.scene.Scene;
import javafx.stage.Stage;

import eu.scy.client.desktop.scydesktop.ScyDesktopCreator;
import eu.scy.client.desktop.scydesktop.corners.tools.NewScyWindowTool;
import eu.scy.client.tools.studentplanningtool.registration.StudentPlanningToolContentCreator;
import eu.scy.client.desktop.scydesktop.Initializer;
import eu.scy.client.desktop.scydesktop.ScyDesktop;
import eu.scy.client.desktop.scydesktop.login.LoginDialog;
import eu.scy.client.tools.fxchattool.registration.ChattoolDrawerContentCreatorFX;
import eu.scy.client.tools.fxchattool.registration.ChattoolPresenceDrawerContentCreatorFX;
import eu.scy.client.desktop.scydesktop.tools.content.text.TextEditorScyToolContentCreator;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import eu.scy.awareness.IAwarenessService;
import org.apache.log4j.Logger;
import eu.scy.chat.controller.*;
/**
 * @author jeremyt
 */
var initializer = Initializer {
           scyDesktopConfigFile: "config/scyDesktopStudentPlanningToolConfig.xml"
           loginType:"remote"
        };
def logger = Logger.getLogger("eu.scy.client.desktop.scydesktop.login.LoginDialog");

function createScyDesktop(toolBrokerAPI: ToolBrokerAPI, userName: String): ScyDesktop {
   def scychatId = "chat";
   def scychatpresenceId = "presence";
   def scystudentplanningId = "studentplanningtool";
   def scyTextId = "text";

   var scyDesktopCreator = ScyDesktopCreator {
              initializer: initializer;
              toolBrokerAPI: toolBrokerAPI;
              userName: userName;
           }

    

    var awarenessService:IAwarenessService = toolBrokerAPI.getAwarenessService();
    var eloUri = "z168fb1jo51y";
   var chatController = new MUCChatController(awarenessService, eloUri);

    logger.info("awarenessService exists: {awarenessService.isConnected()}");
    scyDesktopCreator.drawerContentCreatorRegistryFX.registerDrawerContentCreatorFX(
            ChattoolDrawerContentCreatorFX {
                awarenessService: awarenessService;
                chatController: chatController;
                },
            scychatId);
    scyDesktopCreator.drawerContentCreatorRegistryFX.registerDrawerContentCreatorFX(
            ChattoolPresenceDrawerContentCreatorFX {
                awarenessService: awarenessService;
                chatController: chatController;
            },
            scychatpresenceId);

   scyDesktopCreator.windowContentCreatorRegistryFX.registerWindowContentCreatorFX(StudentPlanningToolContentCreator {}, scystudentplanningId);

   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreatorFX(TextEditorScyToolContentCreator {}, scyTextId);

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
   title: "SCY desktop with StudentPlanningTool"
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

