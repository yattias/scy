/*
 * Main.fx
 *
 * Created on 18 août 2009, 15:06:48
 */
package eu.scy.client.tools.fxresultbinder;

import javafx.stage.Stage;
import javafx.scene.Scene;

import eu.scy.client.desktop.scydesktop.ScyDesktopCreator;
import eu.scy.client.tools.fxresultbinder.registration.ResultBinderToolCreatorFX;

import eu.scy.client.desktop.scydesktop.tools.drawers.xmlviewer.EloXmlViewerCreator;
import eu.scy.client.desktop.scydesktop.Initializer;
import eu.scy.client.desktop.scydesktop.ScyDesktop;
import eu.scy.client.desktop.scydesktop.mission.MissionRunConfigs;
import eu.scy.client.desktop.scydesktop.tools.search.EloSearchCreator;

/**
 * @author Marjolaine
 */
var initializer = Initializer {
           scyDesktopConfigFile: "config/scyDesktopResultBinderTestConfig.xml"
           authorMode:true
           storeElosOnDisk:false
           showQuitConfirmation:false
        }

function createScyDesktop(missionRunConfigs: MissionRunConfigs): ScyDesktop {
   def searchId = "search";
   def scyResultCardId = "resultcard";

   var scyDesktopCreator = ScyDesktopCreator {
              initializer: initializer;
              missionRunConfigs: missionRunConfigs;
           }


   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreatorFX(ResultBinderToolCreatorFX{}, scyResultCardId);
   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreator(new EloXmlViewerCreator(), "xmlViewer");

   def eloSearchCreator = EloSearchCreator{};
   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreatorFX(eloSearchCreator, searchId);

   var scyDesktop = scyDesktopCreator.createScyDesktop();

   eloSearchCreator.scyDesktop = scyDesktop;

   return scyDesktop;
}
var stage: Stage;
var scene: Scene;

stage = Stage {
   title: "SCY desktop with result binder"
   width: 400
   height: 300
	scene: initializer.getScene(createScyDesktop);
}