/*
 * Main.fx
 *
 * Created on 27.01.2010, 17:04:55
 */

package eu.scy.client.tools.fxeportfolio;

import javafx.scene.Scene;
import javafx.stage.Stage;
import eu.scy.client.desktop.scydesktop.Initializer;
import eu.scy.client.desktop.scydesktop.ScyDesktop;
import eu.scy.client.desktop.scydesktop.ScyDesktopCreator;
import eu.scy.client.desktop.scydesktop.tools.drawers.xmlviewer.EloXmlViewerCreator;
import eu.scy.client.tools.fxeportfolio.registration.EportfolioContentCreatorFX;
import eu.scy.client.desktop.scydesktop.corners.elomanagement.EloManagement;
import eu.scy.client.desktop.scydesktop.mission.MissionRunConfigs;


/**
 * @author kaido
 */
var initializer = Initializer {
           scyDesktopConfigFile: "config/scyDesktopFxEportfolioConfig.xml"
           authorMode:true;
        }

function createScyDesktop(missionRunConfigs: MissionRunConfigs): ScyDesktop {
   def scyEportfolioId = "eportfolio";
   var scyDesktopCreator = ScyDesktopCreator {
              initializer: initializer;
              missionRunConfigs: missionRunConfigs;
           }
   scyDesktopCreator.windowContentCreatorRegistryFX.registerWindowContentCreatorFX(EportfolioContentCreatorFX{},scyEportfolioId);
   //scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreator(new EloXmlViewerCreator(), "xmlViewer");
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
//var myLocale : java.util.Locale = new java.util.Locale("et", "EE");
//var myLocale : java.util.Locale = new java.util.Locale("en", "US");
//java.util.Locale.setDefault(myLocale);

var title = ##"EPortfolio Tool";

stage = Stage {
   title: "SCY-Lab with eportfolio"
   width: 800
   height: 600
	scene: initializer.getScene(createScyDesktop);
}
