/*
 * Main.fx
 *
 * Created on 17-sep-2009, 17:39:26
 */
package eu.scy.client.tools.fxflyingsaucer;

import javafx.scene.Scene;
import javafx.stage.Stage;

import eu.scy.client.desktop.scydesktop.ScyDesktopCreator;

import eu.scy.client.desktop.scydesktop.tools.drawers.xmlviewer.EloXmlViewerCreator;
import eu.scy.client.desktop.scydesktop.Initializer;
import eu.scy.client.desktop.scydesktop.ScyDesktop;
import eu.scy.client.tools.fxflyingsaucer.registration.FlyingSaucerCreator;
import eu.scy.client.desktop.scydesktop.corners.elomanagement.EloManagement;
import eu.scy.client.desktop.scydesktop.mission.MissionRunConfigs;
import eu.scy.client.desktop.scydesktop.scywindows.moreinfomanager.TestMoreInfoNodeCreator;

/**
 * @author sikkenj
 */
var initializer = Initializer {
           log4JInitFile: "/config/scy-flying-saucer-log4j.xml"
           scyDesktopConfigFile: "config/scyDesktopFlyingSaucerTestConfig.xml"
           storeElosOnDisk:true;
           loginType:"local"
        }

function createScyDesktop(missionRunConfigs: MissionRunConfigs): ScyDesktop {

   def scyFlyingSaucerId = "flying-saucer";
   def testMoreInfoId = "testMoreInfo";

   var scyDesktopCreator = ScyDesktopCreator {
              initializer: initializer;
              missionRunConfigs: missionRunConfigs;
           }

   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreatorFX(FlyingSaucerCreator{urlSource: null; drawerUIIndicator: null}, scyFlyingSaucerId);

   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreator(new EloXmlViewerCreator(), "xmlViewer");
   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreatorFX(TestMoreInfoNodeCreator{}, testMoreInfoId);

   var scyDesktop = scyDesktopCreator.createScyDesktop();

   scyDesktop.bottomLeftCornerTool = EloManagement {
      scyDesktop: scyDesktop;
      repository: scyDesktopCreator.config.getRepository();
      metadataTypeManager:scyDesktopCreator.config.getMetadataTypeManager();
      titleKey: scyDesktopCreator.config.getTitleKey();
      technicalFormatKey: scyDesktopCreator.config.getTechnicalFormatKey();
   }
   scyDesktop.moreInfoToolFactory = new FlyingSaucerMoreInfoToolFactory();

   return scyDesktop;
}
var stage: Stage;
var scene: Scene;

stage = Stage {
   title: "SCY desktop with Flying saucer tool"
   width: 400
   height: 300
	scene: initializer.getScene(createScyDesktop);
}
