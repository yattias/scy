/*
 * Main.fx
 *
 * Created on 04.09.2009, 12:34:29
 */
package eu.scy.client.tools.fxpictureviewer;

import javafx.stage.Stage;
import javafx.scene.Scene;
import eu.scy.client.desktop.scydesktop.ScyDesktopCreator;

import eu.scy.client.desktop.scydesktop.corners.tools.NewScyWindowTool;

import eu.scy.client.desktop.scydesktop.tools.drawers.xmlviewer.EloXmlViewerCreator;
import eu.scy.client.desktop.scydesktop.Initializer;
import eu.scy.client.desktop.scydesktop.ScyDesktop;
import eu.scy.client.desktop.scydesktop.login.LoginDialog;
import eu.scy.client.desktop.scydesktop.mission.MissionRunConfigs;

/**
 * @author pg
 */
var initializer = Initializer {
           scyDesktopConfigFile: "config/scyDesktopPictureViewerTestConfig.xml"
        }

function createScyDesktop(missionRunConfigs: MissionRunConfigs): ScyDesktop {
// def scyPictureType = "scy/pictureviewer";
   def scyPictureViewerId = "pictureviewer";

   var scyDesktopCreator = ScyDesktopCreator {
              initializer: initializer;
              missionRunConfigs: missionRunConfigs;
           }

   scyDesktopCreator.windowContentCreatorRegistryFX.registerWindowContentCreatorFX(PictureViewerContentCreator {}, scyPictureViewerId);
// scyDesktopCreator.newEloCreationRegistry.registerEloCreation(scyPictureType, "pictureviewer");

   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreator(new EloXmlViewerCreator(), "xmlViewer");

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
   title: "SCY-Lab with pictureviewer"
   width: 350
   height: 250
   scene: initializer.getScene(createScyDesktop);
}

