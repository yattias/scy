/*
 * Main.fx
 *
 * Created on 8-jul-2009, 17:21:12
 */
package eu.scy.client.tools.studentplanningtool;

import javafx.scene.Scene;
import javafx.stage.Stage;

import eu.scy.client.desktop.scydesktop.ScyDesktopCreator;
import eu.scy.client.tools.studentplanningtool.registration.StudentPlanningToolContentCreator;
import eu.scy.client.desktop.scydesktop.Initializer;
import eu.scy.client.desktop.scydesktop.ScyDesktop;
import eu.scy.client.tools.fxchattool.registration.ChattoolDrawerContentCreatorFX;
import eu.scy.client.tools.fxchattool.registration.ChattoolPresenceDrawerContentCreatorFX;
import eu.scy.client.desktop.scydesktop.tools.content.text.TextEditorScyToolContentCreator;
import eu.scy.awareness.IAwarenessService;
import org.apache.log4j.Logger;
import java.lang.*;
import java.util.HashMap;
import eu.scy.client.desktop.scydesktop.corners.elomanagement.EloManagement;
import eu.scy.client.desktop.scydesktop.mission.MissionRunConfigs;
/**
 * @author jeremyt
 */
var initializer = Initializer {
           scyDesktopConfigFile: "config/scyDesktopStudentPlanningToolConfig.xml"
           loginType:"remote"
        };
def logger = Logger.getLogger("eu.scy.client.desktop.scydesktop.login.LoginDialog");

function createScyDesktop(missionRunConfigs: MissionRunConfigs): ScyDesktop {
   def scychatId = "chat";
   def scychatpresenceId = "presence";
   def scystudentplanningId = "studentplanningtool";
   def scyTextId = "text";

   var scyDesktopCreator = ScyDesktopCreator {
              initializer: initializer;
              missionRunConfigs: missionRunConfigs;
           }

    

    var awarenessService:IAwarenessService = missionRunConfigs.tbi.getAwarenessService();
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

   scyDesktop.bottomLeftCornerTool = EloManagement {
      scyDesktop: scyDesktop;
      repository: scyDesktopCreator.config.getRepository();
      metadataTypeManager:scyDesktopCreator.config.getMetadataTypeManager();
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
   scene: initializer.getScene(createScyDesktop);
}

