   /*
 * Main.fx
 *
 * Created on 14-mei-2009, 17:41:07
 */
package eu.scy.client.desktop.scydesktop;

import javafx.stage.Stage;

import eu.scy.client.desktop.scydesktop.tools.content.text.TextEditorScyToolContentCreator;

import eu.scy.client.desktop.scydesktop.tools.scytoolviewer.ScyToolViewerCreator;
import eu.scy.client.desktop.scydesktop.tools.propertiesviewer.PropertiesViewerCreator;
import eu.scy.client.desktop.scydesktop.tools.content.eloImporter.ExternalDocCreator;
import eu.scy.client.desktop.scydesktop.tools.imageviewer.ImageViewerCreator;
import eu.scy.client.desktop.scydesktop.corners.elomanagement.EloManagement;
import eu.scy.client.desktop.scydesktop.tools.speedtest.SpeedTestPanelCreator;
import eu.scy.client.desktop.scydesktop.tools.mission.MissionSpecificationEditorCreator;
import eu.scy.client.desktop.scydesktop.tools.mission.MissionMapModelEditorCreator;
import eu.scy.client.desktop.scydesktop.tools.mission.EloToolConfigurationEditorCreator;
import eu.scy.client.desktop.scydesktop.tools.mission.MissionRuntimeEditorCreator;
import eu.scy.client.desktop.scydesktop.mission.MissionRunConfigs;
import eu.scy.client.desktop.scydesktop.tools.mission.TemplateElosEloEditorCreator;
import eu.scy.client.desktop.scydesktop.tools.mission.RuntimeSettingsEditorCreator;
import eu.scy.client.desktop.scydesktop.tools.drawers.xmlviewer.EloXmlViewerCreatorFX;
import eu.scy.client.desktop.scydesktop.scywindows.moreinfomanager.TestMoreInfoNodeCreator;
import java.lang.System;
import eu.scy.client.desktop.scydesktop.tools.colorscheme.ColorSchemeEditorCreator;

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
   dontUseMissionRuntimeElos:false
   useBigMissionMap:true
   missionMapNotSelectedImageScale:1.0
   missionMapSelectedImageScale:1.5
   missionMapPositionScale:1.0
//   eloImagesPath:"file:/D:/projects/scy/code/scy-trunk/sources/modules/client/desktop/scy-desktop/eloImages/"
//   eloImagesPath:"file:eloImages/"
}

function createScyDesktop(missionRunConfigs: MissionRunConfigs): ScyDesktop {
   def scyTextId = "text";
   def scyImageId = "image";
   def eloXmlViewerId = "xmlViewer";
   def scyToolViewerId = "scyToolViewer";
   def propertiesViewerId = "propertiesViewer";
   def presentationViewerId = "presentationUpload";
   def speedTestPanelId = "speedTestPanel";
   def missionSpecificationId = "missionSpecification";
   def missionMapModelId = "missionMapModel";
   def eloToolConfigurationId = "eloToolConfiguration";
   def missionRuntimeId = "missionRuntime";
   def templateElosId = "templateElos";
   def runtimeSettingsId = "runtimeSettings";
   def testMoreInfoId = "testMoreInfo";
   def colorSchemesId = "colorSchemes";

   def startNanos = System.nanoTime();

   initializer.loadTimer.startActivity("creeate ScyDesktopCreator");

   var scyDesktopCreator = ScyDesktopCreator {
              initializer: initializer;
              missionRunConfigs: missionRunConfigs;
           }

   initializer.loadTimer.startActivity("registering tool creators");
   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreatorFX(ScyToolViewerCreator{}, scyToolViewerId);
   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreatorFX(TextEditorScyToolContentCreator {}, scyTextId);
//   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreator(new EloXmlViewerCreator(), eloXmlViewerId);
   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreatorFX(EloXmlViewerCreatorFX{}, eloXmlViewerId);
   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreatorFX(new PropertiesViewerCreator(), propertiesViewerId);
   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreatorFX(ExternalDocCreator{extensions: ["ppt","pptx"];fileFilterDescription:"PowerPoint Presentations"}, presentationViewerId);
   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreatorFX(ImageViewerCreator{}, scyImageId);
   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreator(new SpeedTestPanelCreator(), speedTestPanelId);
   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreatorFX(MissionSpecificationEditorCreator{}, missionSpecificationId);
   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreatorFX(MissionMapModelEditorCreator{}, missionMapModelId);
   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreatorFX(EloToolConfigurationEditorCreator{}, eloToolConfigurationId);
   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreatorFX(MissionRuntimeEditorCreator{}, missionRuntimeId);
   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreatorFX(TemplateElosEloEditorCreator{}, templateElosId);
   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreatorFX(RuntimeSettingsEditorCreator{}, runtimeSettingsId);
   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreatorFX(TestMoreInfoNodeCreator{}, testMoreInfoId);
   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreatorFX(ColorSchemeEditorCreator{}, colorSchemesId);

   scyDesktopCreator.eloConfigManager.addDebugCreatorId(scyToolViewerId);
   
   initializer.loadTimer.startActivity("creeate ScyDesktop");

   var scyDesktop = scyDesktopCreator.createScyDesktop();

   initializer.loadTimer.startActivity("creeate EloManagement");

   scyDesktop.bottomLeftCornerTool = EloManagement {
      scyDesktop: scyDesktop;
      repository: scyDesktopCreator.config.getRepository();
      metadataTypeManager:scyDesktopCreator.config.getMetadataTypeManager();
      titleKey: scyDesktopCreator.config.getTitleKey();
      technicalFormatKey: scyDesktopCreator.config.getTechnicalFormatKey();
   }

   initializer.loadTimer.startActivity("after creeate EloManagement");

   return scyDesktop;
}

Stage {
	title : "SCY Desktop"
   width:500
   height:400
	scene: initializer.getScene(createScyDesktop);
}
