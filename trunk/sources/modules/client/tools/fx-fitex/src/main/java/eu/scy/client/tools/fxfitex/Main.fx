/*
 * Main.fx
 *
 * Created on 17 août 2009, 13:49:57
 */
package eu.scy.client.tools.fxfitex;

import javafx.stage.Stage;
import javafx.scene.Scene;

import eu.scy.client.desktop.scydesktop.tools.drawers.xmlviewer.EloXmlViewerCreator;
import eu.scy.client.desktop.scydesktop.Initializer;
import eu.scy.client.desktop.scydesktop.ScyDesktop;
import eu.scy.client.desktop.scydesktop.ScyDesktopCreator;
import eu.scy.client.tools.fxfitex.registration.FitexToolCreatorFX;
import eu.scy.client.desktop.scydesktop.mission.MissionRunConfigs;

/**
 * @author Marjolaine
 */
var initializer = Initializer {
           scyDesktopConfigFile: "config/scyDesktopFitexTestConfig.xml"
           authorMode:true
        }

function createScyDesktop(missionRunConfigs: MissionRunConfigs): ScyDesktop {
   def scyFitexId = "fitex";

   var scyDesktopCreator = ScyDesktopCreator {
              initializer: initializer;
              missionRunConfigs: missionRunConfigs;
           }


   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreatorFX(FitexToolCreatorFX{}, scyFitexId);
   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreator(new EloXmlViewerCreator(), "xmlViewer");

   var scyDesktop = scyDesktopCreator.createScyDesktop();

   return scyDesktop;
}
var stage: Stage;
var scene: Scene;

stage = Stage {
   title: "SCY desktop with fitex"
   width: 400
   height: 300
	scene: initializer.getScene(createScyDesktop);
}
