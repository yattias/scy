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
import java.util.Random;
import java.lang.*;
import java.util.HashMap;
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
   // awarenessService.setMUCConferenceExtension("conference.scy.collide.info");
   logger.info("MUC Conference: {awarenessService.getMUCConferenceExtension()}");

    //var r = new Random(System.currentTimeMillis());
    //var eloUri = Long.toString(Math.abs(r.nextLong()), Math.random());
    var eloUri = "z168fb1jo51y";



   // logger.info("CHAT ELO ID exists: {eloUri}");
  // var chatController = new MUCChatController(null, null);
   var chatControllerMap = new HashMap();
    logger.info("awarenessService exists: {awarenessService.isConnected()}");
   
    scyDesktopCreator.drawerContentCreatorRegistryFX.registerDrawerContentCreatorFX(
            ChattoolPresenceDrawerContentCreatorFX {
                awarenessService: awarenessService;
                chatControllerMap: chatControllerMap;
            },
            scychatpresenceId);


    scyDesktopCreator.drawerContentCreatorRegistryFX.registerDrawerContentCreatorFX(
            ChattoolDrawerContentCreatorFX {
                awarenessService: awarenessService;
                chatControllerMap: chatControllerMap;
                },
            scychatId);

 //  scyDesktopCreator.windowContentCreatorRegistryFX.registerWindowContentCreatorFX(StudentPlanningToolContentCreator {}, scystudentplanningId);

    scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreatorFX(StudentPlanningToolContentCreator {}, scystudentplanningId);
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

