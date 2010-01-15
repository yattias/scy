/*
 * Main.fx
 *
 * Created on 9-jul-2009, 15:06:15
 */
package eu.scy.client.desktop.scylab;

import javafx.scene.Scene;
import javafx.stage.Stage;

import eu.scy.client.desktop.scydesktop.ScyDesktopCreator;
import eu.scy.client.desktop.scydesktop.corners.tools.NewScyWindowTool;
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
import eu.scy.client.desktop.scydesktop.login.LoginDialog;
import eu.scy.client.desktop.scydesktop.tools.scytoolviewer.ScyToolViewerCreator;

/**
 * @author sikkenj
 */
var initializer = Initializer {
           scyDesktopConfigFile: "config/scyLabLocalConfig.xml"
           //localToolBrokerLoginConfigFile:"/config/localRemoteScyServices.xml"
           loginType:"local"
           storeElosOnDisk:true;
           createPersonalMissionMap:true
           redirectSystemStream:true
           enableLocalLogging:true
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

   var scyDesktopCreator = ScyDesktopCreator {
              initializer: initializer;
              toolBrokerAPI: toolBrokerAPI;
              userName: userName;
           }

   scyDesktopCreator.windowContentCreatorRegistryFX.registerWindowContentCreatorFX(StudentPlanningToolContentCreator {}, scyStudentPlanningTool);

   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreatorFX(DrawingToolCreatorFX{}, scyDrawingId);

   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreatorFX(FitexToolCreatorFX{}, scyFitexId);

   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreatorFX(CopexToolCreatorFX{}, scyCopexId);

   scyDesktopCreator.windowContentCreatorRegistryFX.registerWindowContentCreatorFX(SCYMapperContentCreator {}, scyMapperId);

   var simulationUriString: String = "http://www.scy-lab.eu/sqzx/HouseNew.sqzx";
//var simulationUriString:String = "http://www.scy-lab.eu/sqzx/balance.sqzx";
   scyDesktopCreator.windowContentCreatorRegistryFX.registerWindowContentCreatorFX(SimulatorContentCreator {simulationUriString: simulationUriString}, scySimulatorId);

   scyDesktopCreator.windowContentCreatorRegistryFX.registerWindowContentCreatorFX(ScyDynamicsContentCreator {}, scyModelId);

   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreator(new FlyingSaucerCreator(), scyFlyingSaucerId);

   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreatorFX(TextEditorScyToolContentCreator {}, scyTextId);

   scyDesktopCreator.windowContentCreatorRegistryFX.registerWindowContentCreatorFX(InterviewToolContentCreator{},scyInterviewId);

   scyDesktopCreator.windowContentCreatorRegistryFX.registerWindowContentCreatorFX(VideoContentCreator {}, scyVideoId);

   scyDesktopCreator.windowContentCreatorRegistryFX.registerWindowContentCreatorFX(WebResourceContentCreator {}, scyWebresourceId);

   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreator(new EloXmlViewerCreator(), "xmlViewer");
   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreatorFX(new ScyToolViewerCreator(), "progress");

   scyDesktopCreator.drawerContentCreatorRegistryFX.registerDrawerContentCreatorFX(ChattoolDrawerContentCreatorFX {}, scychatId);
   scyDesktopCreator.drawerContentCreatorRegistryFX.registerDrawerContentCreatorFX(ChattoolPresenceDrawerContentCreatorFX {}, scychatpresenceId);

   var scyDesktop = scyDesktopCreator.createScyDesktop();

   scyDesktop.bottomLeftCornerTool = NewScyWindowTool {
      scyDesktop: scyDesktop;
      repository: scyDesktopCreator.config.getRepository();
      titleKey: scyDesktopCreator.config.getTitleKey();
      technicalFormatKey: scyDesktopCreator.config.getTechnicalFormatKey();
   }
   return scyDesktop;
}
var scene: Scene;
var stage = Stage {
           title: "SCY-Lab"
           width: 1024
           height: 700
           scene: scene = Scene {
              content: [
                 initializer.getBackgroundImageView(scene),
//                 ImageView {
//                    image: initializer.backgroundImage
//                    fitWidth: bind scene.width
//                    fitHeight: bind scene.height
//                    preserveRatio: false
//                    cache: true
//                 }
                 LoginDialog {
                    createScyDesktop: createScyDesktop
                    initializer: initializer;
                 }
              ]
           }
        }
