/*
 * Main.fx
 *
 * Created on 8-jul-2009, 17:21:12
 */
package eu.scy.client.tools.fxscymapper;

import javafx.scene.Scene;
import javafx.stage.Stage;

import eu.scy.client.desktop.scydesktop.ScyDesktopCreator;
import eu.scy.client.tools.fxscymapper.registration.SCYMapperContentCreator;

import eu.scy.client.desktop.scydesktop.tools.drawers.xmlviewer.EloXmlViewerCreator;
import eu.scy.client.desktop.scydesktop.Initializer;
import eu.scy.client.desktop.scydesktop.ScyDesktop;
import eu.scy.client.desktop.scydesktop.mission.MissionRunConfigs;
import eu.scy.client.desktop.scydesktop.tools.search.EloSearchCreator;

/**
 * @author sikkenj
 */
var initializer = Initializer {
           scyDesktopConfigFile: "config/scyMapperTestConfig.xml"
            loginType:"local"
            enableLocalLogging:true
            authorMode:true
        }

function createScyDesktop(missionRunConfigs: MissionRunConfigs): ScyDesktop {
   var scyDesktopCreator = ScyDesktopCreator {
              initializer: initializer;
              missionRunConfigs: missionRunConfigs;
           }

   def scyMapperId = "conceptmap";
   def searchId = "search";

   
   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreatorFX(SCYMapperContentCreator {userName: missionRunConfigs.tbi.getLoginUserName()}, scyMapperId);

   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreator(new EloXmlViewerCreator(), "xmlViewer");

   def eloSearchCreator = EloSearchCreator{};
   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreatorFX(eloSearchCreator, searchId);
   var scyDesktop = scyDesktopCreator.createScyDesktop();
   eloSearchCreator.scyDesktop = scyDesktop;

//   scyDesktop.bottomLeftCornerTool = EloManagement {
//      scyDesktop: scyDesktop;
//      repository: scyDesktopCreator.config.getRepository();
//      metadataTypeManager:scyDesktopCreator.config.getMetadataTypeManager();
//      titleKey: scyDesktopCreator.config.getTitleKey();
//      technicalFormatKey: scyDesktopCreator.config.getTechnicalFormatKey();
//   }

return scyDesktop;
}
var stage: Stage;
var scene: Scene;

stage = Stage {
   title: "SCY desktop with scymapper tool"
   width: 900
   height: 700
	scene: initializer.getScene(createScyDesktop);
}
