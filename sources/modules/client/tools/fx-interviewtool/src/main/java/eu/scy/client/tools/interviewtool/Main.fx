package eu.scy.client.tools.interviewtool;

import javafx.scene.Scene;
import javafx.stage.Stage;
import eu.scy.client.desktop.scydesktop.Initializer;
import eu.scy.client.desktop.scydesktop.ScyDesktop;
import eu.scy.client.desktop.scydesktop.ScyDesktopCreator;
import eu.scy.client.desktop.scydesktop.tools.drawers.xmlviewer.EloXmlViewerCreator;
import eu.scy.client.desktop.scydesktop.corners.elomanagement.EloManagement;
import eu.scy.client.desktop.scydesktop.mission.MissionRunConfigs;

/**
 * @author kaido
 */
//var myLocale : java.util.Locale = new java.util.Locale("et", "EE");
//var myLocale : java.util.Locale = new java.util.Locale("en", "US");
//java.util.Locale.setDefault(myLocale);
var initializer = Initializer {
           scyDesktopConfigFile: "config/scyDesktopInterviewToolConfig.xml"
           authorMode:true
        }

function createScyDesktop(missionRunConfigs: MissionRunConfigs): ScyDesktop {
   def scyInterviewId = "interview";
   var scyDesktopCreator = ScyDesktopCreator {
              initializer: initializer;
              missionRunConfigs: missionRunConfigs;
           }
   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreatorFX(InterviewToolContentCreator{},scyInterviewId);
   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreator(new EloXmlViewerCreator(), "xmlViewer");
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
   title: ##"Interview Tool"
   width: 700
   height: 700
	scene: initializer.getScene(createScyDesktop);
}
