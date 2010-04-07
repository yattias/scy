   /*
 * Main.fx
 *
 * Created on 14-mei-2009, 17:41:07
 */
package eu.scy.client.desktop.scydesktop;

import javafx.stage.Stage;

import eu.scy.client.desktop.scydesktop.tools.drawers.xmlviewer.EloXmlViewerCreator;
import eu.scy.client.desktop.scydesktop.tools.content.text.TextEditorScyToolContentCreator;

import eu.scy.toolbrokerapi.ToolBrokerAPI;
import eu.scy.client.desktop.scydesktop.tools.scytoolviewer.ScyToolViewerCreator;
import eu.scy.client.desktop.scydesktop.tools.propertiesviewer.PropertiesViewerCreator;
import eu.scy.client.desktop.scydesktop.tools.content.eloImporter.ExternalDocCreator;
import eu.scy.client.desktop.scydesktop.tools.imageviewer.ImageViewerCreator;
import eu.scy.client.desktop.scydesktop.corners.elomanagement.EloManagement;
import eu.scy.client.desktop.scydesktop.tools.speedtest.SpeedTestPanelCreator;

/**
 * @author sikkenj
 */

var initializer = Initializer{
   log4JInitFile:"/config/scy-desktop-log4j.xml"
   javaUtilLoggingInitFile:"/config/scy-desktop-java-util-logging.properties"
   scyDesktopConfigFile:"config/scyDesktopTestConfig.xml"
   loginType:"local"
   storeElosOnDisk:false
   createPersonalMissionMap:true
   enableLocalLogging:true
   redirectSystemStream:false
   debugMode:true
   authorMode:true
   missionMapNotSelectedImageScale:2.0
   missionMapSelectedImageScale:2.5
   missionMapPositionScale:2.0
//   eloImagesPath:"file:/D:/projects/scy/code/scy-trunk/sources/modules/client/desktop/scy-desktop/eloImages/"
//   eloImagesPath:"file:eloImages/"
}

function createScyDesktop(toolBrokerAPI:ToolBrokerAPI, userName:String): ScyDesktop {
   def scyTextId = "text";
   def scyImageId = "image";
   def eloXmlViewerId = "xmlViewer";
   def scyToolViewerId = "scyToolViewer";
   def propertiesViewerId = "propertiesViewer";
   def presentationViewerId = "presentationUpload";
   def speedTestPanelId = "speedTestPanel";

   var scyDesktopCreator = ScyDesktopCreator {
              initializer: initializer;
              toolBrokerAPI:toolBrokerAPI;
              userName:userName;
           }

   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreatorFX(ScyToolViewerCreator{}, scyToolViewerId);
   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreatorFX(TextEditorScyToolContentCreator {}, scyTextId);
   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreator(new EloXmlViewerCreator(), eloXmlViewerId);
   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreatorFX(new PropertiesViewerCreator(), propertiesViewerId);
   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreatorFX(ExternalDocCreator{extensions: ["ppt","pptx"];fileFilterDescription:"PowerPoint Presentations"}, presentationViewerId);
   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreatorFX(ImageViewerCreator{}, scyImageId);
   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreator(new SpeedTestPanelCreator(), speedTestPanelId);

   scyDesktopCreator.eloConfigManager.addDebugCreatorId(scyToolViewerId);
   
   var scyDesktop = scyDesktopCreator.createScyDesktop();

   scyDesktop.bottomLeftCornerTool = EloManagement {
      scyDesktop: scyDesktop;
      repository: scyDesktopCreator.config.getRepository();
      metadataTypeManager:scyDesktopCreator.config.getMetadataTypeManager();
      titleKey: scyDesktopCreator.config.getTitleKey();
      technicalFormatKey: scyDesktopCreator.config.getTechnicalFormatKey();
      userId:userName
   }
   return scyDesktop;
}

Stage {
	title : "SCY Desktop"
   width:400
   height:300
	scene: initializer.getScene(createScyDesktop);
}
