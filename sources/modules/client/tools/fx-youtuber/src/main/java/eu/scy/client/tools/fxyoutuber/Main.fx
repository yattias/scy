package eu.scy.client.tools.fxyoutuber;
import javafx.scene.Scene;
import javafx.stage.Stage;
import eu.scy.client.desktop.scydesktop.Initializer;
import eu.scy.client.desktop.scydesktop.ScyDesktop;
import eu.scy.client.desktop.scydesktop.ScyDesktopCreator;
import eu.scy.client.desktop.scydesktop.corners.elomanagement.EloManagement;
import eu.scy.client.desktop.scydesktop.mission.MissionRunConfigs;
import eu.scy.client.desktop.scydesktop.tools.drawers.xmlviewer.EloXmlViewerCreator;

//TODO:
/*
    - validate user input
    - survive gui check 'adam style'
    - propper elo connection

*/

var initializer = Initializer {
           scyDesktopConfigFile: "config/scyDesktopYouTuberTestConfig.xml"
           //loginType: "remote"
           loginType: "local"
           authorMode:true
           autoLogin: true
           defaultUserName: "phil"
           defaultPassword: "phil"

}
function createScyDesktop(missionRunConfigs: MissionRunConfigs): ScyDesktop {
   def scyYouTuberID = "youtuber";

   var scyDesktopCreator = ScyDesktopCreator {
              initializer: initializer;
              missionRunConfigs: missionRunConfigs;
   }

    scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreatorFX(YouTuberContentCreator {}, scyYouTuberID);
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
   title: "SCY-Lab with YouTuber"
   width: 400
   height: 300
   scene: initializer.getScene(createScyDesktop);
}