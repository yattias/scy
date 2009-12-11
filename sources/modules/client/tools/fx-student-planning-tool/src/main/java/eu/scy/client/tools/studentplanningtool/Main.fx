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
import eu.scy.toolbrokerapi.ToolBrokerAPI;

/**
 * @author jeremyt
 */
var initializer = Initializer {
           scyDesktopConfigFile: "config/scyDesktopStudentPlanningToolConfig.xml"
        }

function createScyDesktop(toolBrokerAPI: ToolBrokerAPI, userName: String): ScyDesktop {
   def scychatId = "chat";
   def scystudentplanningId = "studentplanningtool";
   def scypresenceId = "presence";
//def scytaskId = "task";
//def scyprogressId = "progress";

   var scyDesktopCreator = ScyDesktopCreator {
              initializer: initializer;
              toolBrokerAPI: toolBrokerAPI;
              userName: userName;
           }


   scyDesktopCreator.windowContentCreatorRegistryFX.registerWindowContentCreatorFX(StudentPlanningToolContentCreator {}, scystudentplanningId);

//scyDesktopCreator.drawerContentCreatorRegistryFX.registerDrawerContentCreatorFX(ChattoolDrawerContentCreatorFX{}, scychatId);
//scyDesktopCreator.drawerContentCreatorRegistryFX.registerDrawerContentCreatorFX(ChattoolDrawerContentPresenceCreatorFX{}, scypresenceId);
//scyDesktopCreator.drawerContentCreatorRegistryFX.registerDrawerContentCreatorFX(ChattoolDrawerContentTaskCreatorFX{}, scytaskId);
//scyDesktopCreator.drawerContentCreatorRegistryFX.registerDrawerContentCreatorFX(ChattoolDrawerContentProgressCreatorFX{}, scyprogressId);



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

