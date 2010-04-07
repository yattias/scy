/*
 * Main.fx
 *
 * Created on 9-jul-2009, 15:06:15
 */
package eu.scy.client.desktop.scylab;

import javafx.scene.Scene;
import javafx.stage.Stage;

import eu.scy.client.desktop.scydesktop.ScyDesktopCreator;
import eu.scy.client.desktop.scydesktop.corners.elomanagement.EloManagement;
import eu.scy.client.tools.fxdrawingtool.registration.DrawingToolCreatorFX;
import eu.scy.client.tools.fxsimulator.registration.SimulatorContentCreator;
import eu.scy.client.tools.fxfitex.registration.FitexToolCreatorFX;
import eu.scy.client.tools.fxcopex.registration.CopexToolCreatorFX;
import eu.scy.client.tools.studentplanningtool.registration.StudentPlanningToolContentCreator;
import eu.scy.client.tools.fxscydynamics.registration.ScyDynamicsContentCreator;


import eu.scy.client.tools.fxscymapper.registration.SCYMapperContentCreator;

import eu.scy.client.desktop.scydesktop.tools.drawers.xmlviewer.EloXmlViewerCreator;

import eu.scy.client.tools.fxflyingsaucer.registration.FlyingSaucerCreator;

import eu.scy.client.tools.fxchattool.registration.ChattoolDrawerContentCreatorFX;
import eu.scy.client.tools.fxchattool.registration.ChattoolPresenceDrawerContentCreatorFX;

import eu.scy.client.desktop.scydesktop.tools.content.text.TextEditorScyToolContentCreator;
import eu.scy.client.tools.interviewtool.InterviewToolContentCreator;
import eu.scy.client.tools.fxvideo.VideoContentCreator;
import eu.scy.client.tools.fxwebresourcer.WebResourceContentCreator;
import eu.scy.client.desktop.scydesktop.Initializer;
import eu.scy.client.desktop.scydesktop.ScyDesktop;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import eu.scy.client.desktop.scydesktop.tools.scytoolviewer.ScyToolViewerCreator;
import eu.scy.client.desktop.scydesktop.tools.content.eloImporter.ExternalDocCreator;
import eu.scy.client.tools.fxrichtexteditor.registration.RichTextEditorContentCreatorFX;
import eu.scy.awareness.IAwarenessService;
import java.util.HashMap;
import eu.scy.client.desktop.scydesktop.tools.speedtest.SpeedTestPanelCreator;
import eu.scy.client.desktop.scydesktop.tools.imageviewer.ImageViewerCreator;

/**
 * @author sikkenj
 */
var initializer = Initializer {
           scyDesktopConfigFile: "config/scyLabLocalConfig.xml"
           //localToolBrokerLoginConfigFile:"/config/localRemoteScyServices.xml"
           loginType:"local"
           storeElosOnDisk:true;
           createPersonalMissionMap:true
           redirectSystemStream:false
           languageList:"en"
           missionMapNotSelectedImageScale:2.0
           missionMapSelectedImageScale:2.5
           missionMapPositionScale:1.4
//           enableLocalLogging:false
//           authorMode:true
//           redirectSystemStream:true
//           enableLocalLogging:true
        }

function createScyDesktop(toolBrokerAPI: ToolBrokerAPI, userName: String): ScyDesktop {

   def scyDrawingId = "drawing";
   def scyFitexId = "fitex";
   def scyCopexId = "copex";
   def scySimulatorId = "simulator";
   def scyModelId = "scy-dynamics";
   def scyFlyingSaucerId = "flying-saucer";
   def scyMapperId = "conceptmap";
   def scyStudentPlanningTool = "studentplanningtool";
   def scyTextId = "text";
   def scychatId = "chat";
   def scychatpresenceId = "presence";
   def scyInterviewId = "interview";
   def scyVideoId = "video";
   def scyWebresourceId = "webresource";
   def presentationViewerId = "presentationUpload";
   def sketchUpUploadId = "sketchUpUpload";
   def wordUploadId = "wordUpload";
   def scyRichTextId = "richtext";
   def scyImageId = "image";
   def speedTestPanelId = "speedTestPanel";

   var scyDesktopCreator = ScyDesktopCreator {
              initializer: initializer;
              toolBrokerAPI: toolBrokerAPI;
              userName: userName;
           }

//   scyDesktopCreator.windowContentCreatorRegistryFX.registerWindowContentCreatorFX(StudentPlanningToolContentCreator {}, scyStudentPlanningTool);
   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreatorFX(StudentPlanningToolContentCreator {}, scyStudentPlanningTool);

   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreatorFX(DrawingToolCreatorFX{}, scyDrawingId);

   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreatorFX(FitexToolCreatorFX{}, scyFitexId);

   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreatorFX(CopexToolCreatorFX{}, scyCopexId);

   scyDesktopCreator.windowContentCreatorRegistryFX.registerWindowContentCreatorFX(SCYMapperContentCreator {}, scyMapperId);

   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreatorFX(SimulatorContentCreator {}, scySimulatorId);

   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreatorFX(ScyDynamicsContentCreator {}, scyModelId);

   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreator(new FlyingSaucerCreator(), scyFlyingSaucerId);

   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreatorFX(TextEditorScyToolContentCreator {}, scyTextId);

   scyDesktopCreator.windowContentCreatorRegistryFX.registerWindowContentCreatorFX(InterviewToolContentCreator{},scyInterviewId);

   scyDesktopCreator.windowContentCreatorRegistryFX.registerWindowContentCreatorFX(VideoContentCreator {}, scyVideoId);

   scyDesktopCreator.windowContentCreatorRegistryFX.registerWindowContentCreatorFX(WebResourceContentCreator {}, scyWebresourceId);

   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreatorFX(ImageViewerCreator{}, scyImageId);

var presentationExternalDocCreator = ExternalDocCreator{
      extensions: ["ppt","pptx"]
      fileFilterDescription:"PowerPoint Presentations"
   }
   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreatorFX(presentationExternalDocCreator, presentationViewerId);
   var sketchUpExternalDocCreator = ExternalDocCreator{
      extensions: ["skp"]
      fileFilterDescription:"Google SketchUp drawings"
   }
   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreatorFX(sketchUpExternalDocCreator, sketchUpUploadId);
   var wordExternalDocCreator = ExternalDocCreator{
      extensions: ["doc","docx"]
      fileFilterDescription:"Word documents"
   }
   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreatorFX(wordExternalDocCreator, wordUploadId);

   scyDesktopCreator.windowContentCreatorRegistryFX.registerWindowContentCreatorFX(RichTextEditorContentCreatorFX{},scyRichTextId);

   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreator(new SpeedTestPanelCreator(), speedTestPanelId);

scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreator(new EloXmlViewerCreator(), "xmlViewer");
   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreatorFX(new ScyToolViewerCreator(), "progress");
   scyDesktopCreator.eloConfigManager.addDebugCreatorId("xmlViewer");
   scyDesktopCreator.eloConfigManager.addDebugCreatorId("progress");

   var awarenessService:IAwarenessService = toolBrokerAPI.getAwarenessService();
   var chatControllerMap = new HashMap();
   scyDesktopCreator.drawerContentCreatorRegistryFX.registerDrawerContentCreatorFX(
            ChattoolDrawerContentCreatorFX {
                awarenessService: awarenessService;
                chatControllerMap: chatControllerMap;
                },
            scychatId);

   scyDesktopCreator.drawerContentCreatorRegistryFX.registerDrawerContentCreatorFX(
            ChattoolPresenceDrawerContentCreatorFX {
                awarenessService: awarenessService;
                chatControllerMap: chatControllerMap;
            },
            scychatpresenceId);

//scyDesktopCreator.drawerContentCreatorRegistryFX.registerDrawerContentCreatorFX(ChattoolDrawerContentCreatorFX {}, scychatId);
//   scyDesktopCreator.drawerContentCreatorRegistryFX.registerDrawerContentCreatorFX(ChattoolPresenceDrawerContentCreatorFX {}, scychatpresenceId);

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
var scene: Scene;
var stage = Stage {
           title: "SCY-Lab"
           width: 1024
           height: 700
           scene: initializer.getScene(createScyDesktop);
        }
