/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.tools.fxsocialtaggingtool;



import javafx.stage.Stage;
import javafx.scene.Scene;


import eu.scy.client.desktop.scydesktop.ScyDesktopCreator;
import eu.scy.client.tools.fxsocialtaggingtool.registration.SocialTaggingToolCreatorFX;
//
import eu.scy.client.desktop.scydesktop.tools.drawers.xmlviewer.EloXmlViewerCreator;
import eu.scy.client.desktop.scydesktop.Initializer;
import eu.scy.client.desktop.scydesktop.ScyDesktop;
import eu.scy.client.desktop.scydesktop.tools.propertiesviewer.PropertiesViewerCreator;
import eu.scy.client.desktop.scydesktop.tools.scytoolviewer.ScyToolViewerCreator;
import eu.scy.client.desktop.scydesktop.corners.elomanagement.EloManagement;
import eu.scy.client.desktop.scydesktop.mission.MissionRunConfigs;
import eu.scy.client.desktop.scydesktop.tools.content.text.TextEditorScyToolContentCreator;


/**
 * @author sindre
 */

 var initializer = Initializer {
           scyDesktopConfigFile: "config/scyDesktopTaggingTestConfig.xml"
           storeElosOnDisk:false;
           loginType:"local"
           authorMode:true
           debugMode: true
        }




function createScyDesktop(missionRunConfigs: MissionRunConfigs): ScyDesktop {
//   def scyDrawingId = "taggable";
   def eloXmlViewerId = "xmlViewer";
   def scyToolViewerId = "scyToolViewer";
   def propertiesViewerId = "propertiesViewer";
   def socialTaggingDrawerId = "socialtagging";
   def scyTextId = "text";

   var scyDesktopCreator = ScyDesktopCreator {
              initializer: initializer
        missionRunConfigs: missionRunConfigs;
   }
//   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreatorFX(SocialTaggingToolCreatorFX{}, scyDrawingId);
   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreator(new EloXmlViewerCreator(), eloXmlViewerId);
   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreatorFX(ScyToolViewerCreator{}, scyToolViewerId);
   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreatorFX(new PropertiesViewerCreator(), propertiesViewerId);
   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreatorFX(new SocialTaggingDrawerCreator(), socialTaggingDrawerId);
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
   title: "SCY desktop with social tagging tool demo"
   width: 500
   height: 400
	scene: initializer.getScene(createScyDesktop);
}
