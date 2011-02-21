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
import eu.scy.client.tools.fxscydynamics.registration.ScyDynamicsContentCreator;


import eu.scy.client.tools.fxscymapper.registration.SCYMapperContentCreator;

import eu.scy.client.desktop.scydesktop.tools.drawers.xmlviewer.EloXmlViewerCreatorFX;

import eu.scy.client.tools.fxflyingsaucer.registration.FlyingSaucerCreator;
import eu.scy.client.tools.fxsocialtaggingtool.SocialTaggingDrawerCreator;

import eu.scy.client.tools.fxchattool.registration.ChattoolDrawerContentCreatorFX;
import eu.scy.client.tools.fxchattool.registration.ChattoolPresenceDrawerContentCreatorFX;

import eu.scy.client.desktop.scydesktop.tools.content.text.TextEditorScyToolContentCreator;
import eu.scy.client.tools.interviewtool.InterviewToolContentCreator;
import eu.scy.client.tools.fxvideo.VideoContentCreator;
import eu.scy.client.tools.fxwebresourcer.WebResourceContentCreator;
import eu.scy.client.desktop.scydesktop.Initializer;
import eu.scy.client.desktop.scydesktop.ScyDesktop;
import eu.scy.client.desktop.scydesktop.tools.content.eloImporter.ExternalDocCreator;
import eu.scy.client.tools.fxrichtexteditor.registration.RichTextEditorContentCreatorFX;
import eu.scy.awareness.IAwarenessService;
import java.util.HashMap;
import eu.scy.client.desktop.scydesktop.tools.speedtest.SpeedTestPanelCreator;
import eu.scy.client.desktop.scydesktop.mission.MissionRunConfigs;
import eu.scy.client.desktop.scydesktop.tools.mission.EloToolConfigurationEditorCreator;
import eu.scy.client.desktop.scydesktop.tools.mission.MissionMapModelEditorCreator;
import eu.scy.client.desktop.scydesktop.tools.mission.MissionRuntimeEditorCreator;
import eu.scy.client.desktop.scydesktop.tools.mission.MissionSpecificationEditorCreator;
import eu.scy.client.desktop.scydesktop.tools.mission.TemplateElosEloEditorCreator;
import eu.scy.client.desktop.scydesktop.tools.imageviewer.ImageViewerCreator;
import eu.scy.client.tools.fxmathtool.registration.MathToolContentCreatorFX;
import eu.scy.client.desktop.scydesktop.scywindows.moreinfomanager.TestMoreInfoNodeCreator;
import eu.scy.client.tools.fxflyingsaucer.FlyingSaucerMoreInfoToolFactory;
import eu.scy.client.tools.fxflyingsaucer.UrlSource;
import eu.scy.client.tools.fxformauthor.FormAuthorContentCreator;
import eu.scy.client.tools.fxyoutuber.YouTuberContentCreator;
import eu.scy.client.desktop.scydesktop.feedbackquestion.FeedbackQuestionNodeCreator;

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
//           enableLocalLogging:false
           authorMode:false
           debugMode: false
//           redirectSystemStream:true
//           enableLocalLogging:true
        }

function createScyDesktop(missionRunConfigs: MissionRunConfigs): ScyDesktop {

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
   def mathToolId = "mathtool";
   def speedTestPanelId = "speedTestPanel";
   def missionSpecificationId = "missionSpecification";
   def missionMapModelId = "missionMapModel";
   def eloToolConfigurationId = "eloToolConfiguration";
   def missionRuntimeId = "missionRuntime";
   def templateElosId = "templateElos";
   def scyImageId = "image";
   def scyFlyingSaucerAssignmentId = "assingmentInfo";
   def scyFlyingSaucerResourcesId = "resourcesInfo";
   def testMoreInfoId = "testMoreInfo";
   def scyFormAuthorId = "formauthor";
   def scyYouTuberID = "youtuber";
   def feedbackQuestionId = "feedbackQuestion";
   def socialtaggingId = "socialtagging";

   var scyDesktopCreator = ScyDesktopCreator {
              initializer: initializer;
              missionRunConfigs: missionRunConfigs;
           }

   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreatorFX(MathToolContentCreatorFX{},mathToolId);

   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreatorFX(DrawingToolCreatorFX{}, scyDrawingId);

   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreatorFX(FitexToolCreatorFX{}, scyFitexId);

   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreatorFX(CopexToolCreatorFX{}, scyCopexId);

   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreatorFX(SCYMapperContentCreator {}, scyMapperId);

   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreatorFX(SimulatorContentCreator {}, scySimulatorId);

   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreatorFX(ScyDynamicsContentCreator {}, scyModelId);

   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreator(new FlyingSaucerCreator(), scyFlyingSaucerId);
   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreator(new FlyingSaucerCreator(UrlSource.ASSIGNMENT), scyFlyingSaucerAssignmentId);
   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreator(new FlyingSaucerCreator(UrlSource.RESOURCES), scyFlyingSaucerResourcesId);

   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreatorFX(TextEditorScyToolContentCreator {}, scyTextId);

   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreatorFX(InterviewToolContentCreator{},scyInterviewId);

   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreatorFX(VideoContentCreator {}, scyVideoId);

   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreatorFX(WebResourceContentCreator {}, scyWebresourceId);

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

   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreatorFX(RichTextEditorContentCreatorFX{},scyRichTextId);
   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreatorFX(ImageViewerCreator{}, scyImageId);
   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreatorFX(FormAuthorContentCreator {}, scyFormAuthorId);
   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreatorFX(YouTuberContentCreator {}, scyYouTuberID);

   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreator(new SpeedTestPanelCreator(), speedTestPanelId);

   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreatorFX(MissionSpecificationEditorCreator{}, missionSpecificationId);
   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreatorFX(MissionMapModelEditorCreator{}, missionMapModelId);
   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreatorFX(EloToolConfigurationEditorCreator{}, eloToolConfigurationId);
   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreatorFX(MissionRuntimeEditorCreator{}, missionRuntimeId);
   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreatorFX(TemplateElosEloEditorCreator{}, templateElosId);

   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreatorFX(EloXmlViewerCreatorFX{}, "xmlViewer");
//   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreatorFX(new ScyToolViewerCreator(), "progress");
   scyDesktopCreator.eloConfigManager.addDebugCreatorId("xmlViewer");
//   scyDesktopCreator.eloConfigManager.addDebugCreatorId("progress");
   scyDesktopCreator.eloConfigManager.addDebugCreatorId("testMoreInfoId");

   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreatorFX(TestMoreInfoNodeCreator{}, testMoreInfoId);

   var feedbackQuestionNodeCreator = FeedbackQuestionNodeCreator{};
   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreatorFX(feedbackQuestionNodeCreator, feedbackQuestionId);
   
   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreatorFX(SocialTaggingDrawerCreator{}, socialtaggingId);

   var awarenessService:IAwarenessService = missionRunConfigs.tbi.getAwarenessService();
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

   var scyDesktop = scyDesktopCreator.createScyDesktop();

   scyDesktop.bottomLeftCornerTool = EloManagement {
      scyDesktop: scyDesktop;
      repository: scyDesktopCreator.config.getRepository();
      metadataTypeManager:scyDesktopCreator.config.getMetadataTypeManager();
      titleKey: scyDesktopCreator.config.getTitleKey();
      technicalFormatKey: scyDesktopCreator.config.getTechnicalFormatKey();
   }
   scyDesktop.moreInfoToolFactory = new FlyingSaucerMoreInfoToolFactory();
   feedbackQuestionNodeCreator.scyDesktop = scyDesktop;
   return scyDesktop;
}
var scene: Scene;
var stage = Stage {
           title: "SCY-Lab"
           width: 1024
           height: 700
           scene: initializer.getScene(createScyDesktop);
        }
