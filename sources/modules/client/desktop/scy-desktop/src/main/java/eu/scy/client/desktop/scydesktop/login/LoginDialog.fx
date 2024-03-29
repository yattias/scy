/*
 * LoginDialog.fx
 *
 * Created on 7-dec-2009, 10:07:06
 */
package eu.scy.client.desktop.scydesktop.login;

import javafx.scene.CustomNode;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import eu.scy.client.desktop.scydesktop.scywindows.window.StandardScyWindow;
import javafx.scene.Scene;
import javafx.stage.Stage;
import eu.scy.client.desktop.scydesktop.ScyDesktop;
import eu.scy.client.desktop.scydesktop.scywindows.window.MouseBlocker;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import eu.scy.client.desktop.scydesktop.Initializer;
import eu.scy.client.desktop.desktoputils.log4j.Logger;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.animation.Interpolator;
import javafx.scene.shape.Rectangle;
import eu.scy.toolbrokerapi.ServerNotRespondingException;
import eu.scy.toolbrokerapi.LoginFailedException;
import java.lang.System;
import eu.scy.client.desktop.desktoputils.art.WindowColorScheme;
import eu.scy.client.desktop.desktoputils.art.ScyColors;
import eu.scy.client.desktop.desktoputils.EmptyBorderNode;
import eu.scy.client.desktop.scydesktop.mission.MissionRunConfigs;
import eu.scy.client.desktop.scydesktop.LoginType;
import eu.scy.client.desktop.scydesktop.mission.Missions;
import eu.scy.client.desktop.desktoputils.InjectObjectsUtils;
import java.net.URI;
import eu.scy.client.desktop.scydesktop.scywindows.window.ProgressOverlay;
import eu.scy.client.desktop.scydesktop.mission.MissionLocator;
import eu.scy.client.desktop.desktoputils.art.javafx.LogoEloIcon;
import eu.scy.actionlogging.Action;
import eu.scy.actionlogging.api.ContextConstants;
import eu.scy.actionlogging.api.IAction;
import java.util.Locale;
import eu.scy.client.desktop.scydesktop.scywindows.scydesktop.DialogBox;
import java.lang.Exception;
import eu.scy.client.desktop.desktoputils.StringUtils;
import eu.scy.client.desktop.scydesktop.hacks.RepositoryTimer;

/**
 * @author sikken
 */
// place your code here
package def loginColor = Color.hsb(255, 1, 1.0);
package def successColor = Color.hsb(135, 1, 0.8);
package def failedColor = Color.hsb(360, 1, 0.9);

public class LoginDialog extends CustomNode, TbiReady {

   def logger = Logger.getLogger(this.getClass());
   public var initializer: Initializer;
   public var createScyDesktop: function(missionRunConfigs: MissionRunConfigs): ScyDesktop;
   var loginWindow: StandardScyWindow;
   var loginNode: LoginNode;
   var windowTitle: String = null;
   var initialStageTitle = "SCY-Lab";
   var serverHostTitle = "";
   var userName: String = null;
   var tbi: ToolBrokerAPI = null;

   postinit {

      FX.deferAction(initMouseBlocker);
   }

   function initMouseBlocker(): Void {
      var theStage = scene.stage;
      if (theStage == null) {
         System.err.println("defering initMouseBlocker, because of the missing stage");
         FX.deferAction(initMouseBlocker);
      } else {
         MouseBlocker.initMouseBlocker(scene.stage);
         ProgressOverlay.initOverlay(scene.stage);
         DialogBox.dialogScene = scene;
         setStageTitles();
      }
      initializer.launchTimer.endActivity();
   }

   function setStageTitles() {
      initialStageTitle = scene.stage.title;
      if (initializer.scyServerHost != "") {
         serverHostTitle = " on {initializer.scyServerHost}";
      }
      scene.stage.title = "{initialStageTitle}{serverHostTitle}";
   }

   public override function create(): Node {
      loginNode = LoginNode {
                 loginAction: loginAction
                 defaultUserName: initializer.defaultUserName
                 defaultPassword: initializer.defaultPassword
                 autoLogin: initializer.autoLogin
                 languages: initializer.languages
              }
      loginWindow = StandardScyWindow {
                 title: bind if (windowTitle == null) loginNode.loginTitle else windowTitle
                 windowColorScheme: WindowColorScheme.getWindowColorScheme(ScyColors.green)
                 scyContent: EmptyBorderNode {
                    content: loginNode
                    widthCorrection: 10.0
                    heightCorrection: 6.0
                 }
                 allowClose: false;
                 allowResize: false;
                 allowRotate: false;
                 allowMinimize: false;
                 allowDragging: false;
                 allowCenter: false;
                 allowMaximize: false;
                 opacity: 0.0;
                 layoutX: bind (scene.width / 2 - loginWindow.width / 2);
                 layoutY: bind (scene.height / 2 - loginWindow.height / 2);
              };
      loginWindow.windowColorScheme.mainColor = loginColor;
      loginWindow.activated = true;
      loginWindow.eloIcon = LogoEloIcon {
                 windowColorScheme: bind loginWindow.windowColorScheme
                 selected: true
              }

      loginWindow.openBoundWindow(0, 0);
      FX.deferAction(fadeWindowIn);
      return loginWindow;
   }

   function fadeWindowIn(): Void {
      def window = loginWindow;
      def delayDuration = 500ms;
      def appearDuration = 500ms;
      Timeline {
         repeatCount: 1
         keyFrames: [
            KeyFrame {
               time: delayDuration
               values: window.opacity => 0.0
            }
            KeyFrame {
               time: delayDuration + appearDuration
               values: window.opacity => 1.0
            }
         ]
      }.play();
   }

   function loginAction(userName: String, password: String): Void {
      //      println("userName: {userName}, password: {password}");
      try {
         initializer.launchTimer.startActivity("login tbi");
         def loginResult = initializer.toolBrokerLogin.login(userName, password);
         //         var toolBrokerAPI = initializer.toolBrokerLogin.login(userName, password);
         //         logger.info(
         //         "tbi.getLoginUserName() : {toolBrokerAPI.getLoginUserName()}\n""tbi.getMission() : {toolBrokerAPI.getMission()}\n""tbi.getRepository() : {toolBrokerAPI.getRepository()}\n""tbi.getMetaDataTypeManager() : {toolBrokerAPI.getMetaDataTypeManager()}\n""tbi.getExtensionManager() : {toolBrokerAPI.getExtensionManager()}\n""tbi.getELOFactory() : {toolBrokerAPI.getELOFactory()}\n""tbi.getActionLogger() : {toolBrokerAPI.getActionLogger()}\n""tbi.getAwarenessService() : {toolBrokerAPI.getAwarenessService()}\n""tbi.getDataSyncService() : {toolBrokerAPI.getDataSyncService()}\n""tbi.getPedagogicalPlanService() : {toolBrokerAPI.getPedagogicalPlanService()}\n""tbi.getStudentPedagogicalPlanService() : {toolBrokerAPI.getStudentPedagogicalPlanService()}");
         showLoginResult(loginResult, userName);
      //placeScyDescktop(toolBrokerAPI, userName);
      } catch (e: LoginFailedException) {
         logger.error("failed to login with {e.getUserName()}");
         showLoginResult(null, userName);
      } catch (e: ServerNotRespondingException) {
         logger.error("Could not connect to host {e.getServer()}:{e.getServer()}");
//         JOptionPane.showMessageDialog(null as Component, "Could not connect to host {e.getServer()}:{e.getPort()}", "Connection problems", JOptionPane.ERROR_MESSAGE);
         def message = StringUtils.putInValues(##"Could not connect to host %0%:%1%:\n%2%", e.getServer(),"{e.getPort()}",e.getMessage());
         DialogBox.showMessageDialog(message, ##"Connection problems", null, null, null);
      }
   }

   function showLoginResult(loginResult: Object, userName: String) {
      def window = loginWindow;
      def windowColorScheme = window.windowColorScheme;
      if (loginResult != null) {
         // successfull login
         initializer.launchTimer.startActivity("finish tbi creation");
         this.userName = userName;
         getReadyForUser(loginResult);
         windowTitle = ##"Welcome to SCY-Lab";
         loginNode.disable = true;
         Timeline {
            repeatCount: 1
            keyFrames: [
               KeyFrame {
                  time: 750ms
                  values: windowColorScheme.mainColor => successColor tween Interpolator.LINEAR;
               }
            ]
         }.play();
      } else {
         Timeline {
            repeatCount: 6
            autoReverse: true
            keyFrames: [
               KeyFrame {
                  time: 0ms
                  values: windowColorScheme.mainColor => loginColor tween Interpolator.LINEAR;
               }
               KeyFrame {
                  time: 500ms
                  values: windowColorScheme.mainColor => failedColor tween Interpolator.LINEAR;
               }
            ]
         }.play();
         loginNode.loginFailed();
      }
   }

   function getReadyForUser(loginResult: Object): Void {
      if (LoginType.LOCAL_MULTI_USER == initializer.loginTypeEnum) {
         initializer.setupLogging(userName);
      }
      ProgressOverlay.startShowWorking();
      var repositoryTimer:RepositoryTimer = null;
      if (initializer.useRepositoryTimer){
         repositoryTimer = new RepositoryTimer();
      }

      def backgroundGetReadyForUser = new BackgroundGetReadyForUser(initializer.toolBrokerLogin, loginResult, this, repositoryTimer);
      backgroundGetReadyForUser.start();
   }

   public override function tbiFailed(e: Exception): Void {
      logger.error("Failed to complete login {e.getMessage()}", e);
      FX.deferAction(function(): Void {
         def message = StringUtils.putInValues(##"Failed to complete login:\n%0%\nSCY-Lab will quit.", e.getMessage());
         DialogBox.showMessageDialog(message, ##"Connection problems", null, function(): Void {
            FX.exit();
         }, null);
      });
   }

   public override function tbiReady(toolBrokerAPI: ToolBrokerAPI, missions: Missions): Void {
      logger.info(
      "tbi.getLoginUserName() : {toolBrokerAPI.getLoginUserName()}\n""tbi.getMissionSpecificationURI() ) : {toolBrokerAPI.getMissionSpecificationURI()}\n""tbi.getRepository() : {toolBrokerAPI.getRepository()}\n""tbi.getMetaDataTypeManager() : {toolBrokerAPI.getMetaDataTypeManager()}\n""tbi.getExtensionManager() : {toolBrokerAPI.getExtensionManager()}\n""tbi.getELOFactory() : {toolBrokerAPI.getELOFactory()}\n""tbi.getActionLogger() : {toolBrokerAPI.getActionLogger()}\n""tbi.getAwarenessService() : {toolBrokerAPI.getAwarenessService()}\n""tbi.getDataSyncService() : {toolBrokerAPI.getDataSyncService()}");
      initializer.launchTimer.startActivity("start find mission");
      findMission(toolBrokerAPI, missions);
      ProgressOverlay.stopShowWorking();
   }

   function findMission(toolBrokerAPI: ToolBrokerAPI, missions: Missions) {
      if (initializer.showOnlyStartedMissions) {
         missions.removeMissionSpecifications();
      }

      def missionLocator: MissionLocator = MissionLocator {
                 tbi: toolBrokerAPI
                 userName: userName
                 initializer: initializer
                 missions: missions
                 window: loginWindow
                 startMission: startMission
                 cancelMission: cancelMission
              }
      missionLocator.locateMission();
   }

   function cancelMission(): Void {
      FX.exit();
   }

   function startMission(missionRunConfigs: MissionRunConfigs): Void {
      initializer.launchTimer.endActivity();
      logger.info("start mission with {missionRunConfigs}");
      initializer.launchTimer.endActivity();
      initializer.loadTimer.reset();
      initializer.loadTimer.startActivity("prepare mission loading");
      loginWindow.scyContent = WelcomeNode {
                 name: missionRunConfigs.tbi.getLoginUserName();
              }
      FX.deferAction(function(): Void {
         def userName = missionRunConfigs.tbi.getLoginUserName();
         loginWindow.scyContent = WelcomeNode {
                    name: userName
                 }
         var stage = scene.stage;
         var stageTitle = stage.title;
         stage.title = "{initialStageTitle} : {userName} - {missionRunConfigs.missionRuntimeModel.getMissionRuntimeElo().getTitle()}{serverHostTitle}";
         FX.deferAction(function(): Void {
            finishTbi(missionRunConfigs);
            logLoggedIn(missionRunConfigs);
            placeScyDesktop(missionRunConfigs);
         });
      });
   }

   function logLoggedIn(missionRunConfigs: MissionRunConfigs): Void {
      def action: IAction = new Action();
      action.setUser(missionRunConfigs.tbi.getLoginUserName());
      action.setType("logged_in");
      action.addContext(ContextConstants.tool, "scy-desktop");
      action.addContext(ContextConstants.mission, missionRunConfigs.missionRuntimeModel.getMissionRuntimeElo().getUri().toString());
      action.addContext(ContextConstants.session, "n/a");
      action.addContext(ContextConstants.eloURI, "n/a");
      action.addAttribute("language", Locale.getDefault().toString());
      action.addAttribute("missionSpecification", missionRunConfigs.tbi.getMissionSpecificationURI().toString());
      action.addAttribute("missionName", missionRunConfigs.missionRuntimeModel.getMissionRuntimeElo().getTitle());
	  var missionId = missionRunConfigs.missionRuntimeModel.getMissionRuntimeElo().getMissionId();
      if (missionId == null) {
		missionId = "n/a";
	  }
	  action.addAttribute("missionId", missionId);
      missionRunConfigs.tbi.getActionLogger().log(action);
      logger.info("logged logged_in-action: {action}");
   }

   function finishTbi(missionRunConfigs: MissionRunConfigs): Void {
      InjectObjectsUtils.injectObjectIfWantedJava(missionRunConfigs.tbi, URI.class, "missionRuntimeURI", missionRunConfigs.missionRuntimeURI);
      InjectObjectsUtils.injectObjectIfWantedJava(missionRunConfigs.tbi, URI.class, "missionSpecificationURI", missionRunConfigs.missionSpecificationElo.getUri());
   }

   function placeScyDesktop(missionRunConfigs: MissionRunConfigs): ScyDesktop {
      // either place the components "static" in the scene in initializer.getScene
      // or do it here "dynamic" (meaning after a succesfull login)
      //     insert ScyDesktop.scyDektopGroup into scene.content;
      //     insert ModalDialogBox.modalDialogGroup into scene.content;
      //     insert SimpleTooltipManager.tooltipGroup into scene.content;
      //     insert MouseBlocker.mouseBlockNode into scene.content;

      var scyDesktop = createScyDesktop(missionRunConfigs);

      // all components are already placed in the scene
      // so we only need to remove this login from the scene
      delete this from scene.content;

      return scyDesktop;
   }

}

function run() {
   var loginDialog = LoginDialog {
              layoutX: 10
              layoutY: 10
           }

   Stage {
      title: "Login dialog test"
      scene: Scene {
         width: 300
         height: 200
         content: [
            Rectangle {
               x: 10, y: 10
               width: 10, height: 10
               fill: loginColor
            }
            Rectangle {
               x: 30, y: 10
               width: 10, height: 10
               fill: successColor
            }
            Rectangle {
               x: 50, y: 10
               width: 10, height: 10
               fill: failedColor
            }

            loginDialog
         ]
      }
   }

}
